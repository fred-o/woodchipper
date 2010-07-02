package woodchipper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import woodchipper.LogSystemHandler;

public class JarProcessor {

	JarFile in;
	JarOutputStream out;
	File input;
	File output;
	File tmp;
	List<LogSystemHandler> handlers;

	Set<Class<?>> referenced = new HashSet<Class<?>>();
	Set<String> names = new HashSet<String>();
	FileFilter fileFilter;

	boolean modified = false;

	byte[] buf = new byte[10000];
	int len;

	public JarProcessor(File input, File output, List<LogSystemHandler> handlers) throws IOException {
		this.in = new JarFile(input);
		this.input = input;
		this.output = output;
		this.tmp = File.createTempFile("woodchipper", "jar");
		this.out = new JarOutputStream(new FileOutputStream(tmp));
		this.handlers = handlers;
		
		List<FileFilter> filters = new LinkedList<FileFilter>();
		for(LogSystemHandler handler: handlers) {
			filters.add(handler.getFilter());
		}
		this.fileFilter = new OrFileFilter(filters);
	}

	private void reference(Class<?> clazz) {
		if (referenced.contains(clazz))
			return;
		if (Object.class == clazz) 
			return;
		referenced.add(clazz);
		reference(clazz.getSuperclass());
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
		for(Enumeration<JarEntry> entries = in.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			String fileName = entry.getName();

			if(!this.fileFilter.accept(new File(fileName))) {
				if (!fileName.endsWith(".class")) {
					out.putNextEntry(entry);
					copy(in.getInputStream(entry), out);
				} else {
					out.putNextEntry(new JarEntry(fileName));
					ClassWriter writer = new ClassWriter(0);

					List<HandlerReplacerPair> pairs = new LinkedList<HandlerReplacerPair>();
					ClassVisitor cv = writer;
					for(LogSystemHandler handler: handlers) {
						pairs.add(0, new HandlerReplacerPair(handler, handler.makeClassReplacer(cv)));
						cv = pairs.get(0).replacer;
					}

					ClassReader reader = new ClassReader(in.getInputStream(entry));
					reader.accept(cv, 0);
					out.write(writer.toByteArray());

					for(HandlerReplacerPair pair: pairs) {
						for(Class<?> ref: pair.replacer.getReferenced()) {
							reference(ref);
						}
						if (!this.modified && pair.replacer.isModified()) {
							System.out.println("Removed " + pair.handler.getSystemName() + " references from " 
									+ input.getName());
							this.modified = true;
						}
					}
				}
				names.add(entry.getName());
				out.closeEntry();
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
					out.putNextEntry(new JarEntry(name));
					out.closeEntry();
					names.add(name);
				}
			}
		}
	}
	
	protected void addReferencedClasses() throws IOException {
		for(Class<?> ref: referenced) {
			String name = ref.getCanonicalName().replaceAll("\\.", "/") + ".class";
			if (!names.contains(name)) {
				out.putNextEntry(new JarEntry(name));
				copy(ref.getClassLoader().getResourceAsStream(name), out);
				out.closeEntry();
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
				out.flush();
				out.close();
			}
		
			if (modified) {
				FileUtils.copyFile(tmp, output);
				FileUtils.deleteQuietly(tmp);
			}
		} catch (CouldNotReplaceException cnre) {
			System.out.println(input.getName() + ": the class " + cnre.getReferencingClass() + 
					" references " + cnre.getSignature() + 
					", which woodchipper unfortunatly could not handle.");
			cnre.printStackTrace();
		}
	}

}