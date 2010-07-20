package woodchipper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.filefilter.OrFileFilter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import woodchipper.LogSystemHandler;

/**
 * Main processor class; needs an {@link AbstractFileSystem} and a
 * list of {@link LogSystemHandler}s to go to work.
 *
 * @author fredrik
 */
public class WoodChipper {

	AbstractFileSystem fileSystem;
	List<LogSystemHandler> handlers;
	FileFilter fileFilter;

	Set<Class<?>> referenced = new HashSet<Class<?>>();
	Set<String> names = new HashSet<String>();

	boolean modified = false;

	byte[] buf = new byte[10000];
	int len;

	public WoodChipper(AbstractFileSystem fileSystem, List<LogSystemHandler> handlers) throws IOException {
		this.fileSystem = fileSystem;
		this.handlers = handlers;

		List<FileFilter> filters = new LinkedList<FileFilter>();
		for(LogSystemHandler handler: handlers) {
			filters.add(handler.getFilter());
		}
		this.fileFilter = new OrFileFilter(filters);
	}

	private void reference(Class<?> clazz) {
		if (clazz == null)
			return;
		if (referenced.contains(clazz))
			return;
		if (Object.class == clazz) 
			return;
		referenced.add(clazz);
		reference(clazz.getSuperclass());
		for(Class<?> iface: clazz.getInterfaces()) {
			reference(iface);
		}
	}

	private void copy(InputStream in, OutputStream out) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(in);
		try {
			while((len = bis.read(buf, 0, buf.length)) != -1)
				out.write(buf, 0, len);
		} finally {
			bis.close();
		}
	}

	private static class HandlerReplacerPair {
		public final LogSystemHandler handler;
		public final ClassReplacer replacer;

		public HandlerReplacerPair(LogSystemHandler handler, ClassReplacer replacer) {
			this.handler = handler;
			this.replacer = replacer;
		}
	}

	protected void copyEntries() throws IOException {
		Set<LogSystemHandler> messageShown = new HashSet<LogSystemHandler>();

		while (fileSystem.next()) {
			String fileName = fileSystem.getInputPath();

			if(!names.contains(fileName) && !this.fileFilter.accept(new File(fileName))) {
				if (!fileName.endsWith(".class")) {
					fileSystem.nextOutput();
					copy(fileSystem.getInputStream(), fileSystem.getOutputStream());
				} else {
					fileSystem.nextOutput(fileName);
					ClassWriter writer = new ClassWriter(0);

					List<HandlerReplacerPair> pairs = new LinkedList<HandlerReplacerPair>();
					ClassVisitor cv = writer;
					for(LogSystemHandler handler: handlers) {
						pairs.add(0, new HandlerReplacerPair(handler, handler.makeClassReplacer(cv)));
						cv = pairs.get(0).replacer;
					}

					ClassReader reader = new ClassReader(fileSystem.getInputStream());
					reader.accept(cv, 0);
					fileSystem.getOutputStream().write(writer.toByteArray());

					for(HandlerReplacerPair pair: pairs) {
						for(Class<?> ref: pair.replacer.getReferenced()) {
							reference(ref);
						}
						if (!messageShown.contains(pair.handler) && pair.replacer.isModified()) {
							System.out.println("Removing " + pair.handler.getSystemName() + " references from " 
									+ fileSystem.getInputPath());
							messageShown.add(pair.handler);
							this.modified = true;
						}
					}
				}
				names.add(fileName);
			}
		}
	}

	protected void addReferencedDirectories() throws IOException {
		for(Class<?> ref: referenced) {
		    StringBuilder sb = new StringBuilder();
			for(String elt: ref.getPackage().getName().split("\\.")) {
				sb.append(elt).append("/");
				String name = sb.toString();

				if(!names.contains(name)) {
					fileSystem.addOutputDirectory(name);
					names.add(name);
				}
			}
		}
	}
	
	protected void addReferencedClasses() throws IOException {
		for(Class<?> ref: referenced) {
			String name = ref.getCanonicalName().replaceAll("\\.", "/") + ".class";
			if (!names.contains(name)) {
				fileSystem.nextOutput(name);
				copy(ref.getClassLoader().getResourceAsStream(name), fileSystem.getOutputStream());
				names.add(name);
			} 
		}
	}

	public void process() throws IOException {
		try {
			try {
				copyEntries();
				if (modified) {
					addReferencedDirectories();
					addReferencedClasses();
				} 
			} finally {
				fileSystem.closeOutput();
			}
		
			if (modified) {
				fileSystem.commit();
			}
		} catch (CouldNotReplaceException cnre) {
			System.out.println(fileSystem.getInputPath() + ": the class " + cnre.getReferencingClass() + 
					" references " + cnre.getSignature() + 
					", which woodchipper unfortunatly could not handle.");
		}
	}

}