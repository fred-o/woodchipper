package woodchipper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class JarProcessor {

	JarFile in;
	JarOutputStream out;

	Set<Class<?>> referenced = new HashSet<Class<?>>();
	Set<String> names = new HashSet<String>();

	byte[] buf = new byte[10000];
	int len;

	public JarProcessor(File input, File output) throws IOException {
		this.in = new JarFile(input);
		this.out = new JarOutputStream(new FileOutputStream(output));
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

	protected void copyEntries() throws IOException {
		for(Enumeration<JarEntry> entries = in.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			if (!entry.getName().endsWith(".class")) {
				out.putNextEntry(entry);
				copy(in.getInputStream(entry), out);
			} else {
				out.putNextEntry(new JarEntry(entry.getName()));
				ClassWriter writer = new ClassWriter(0);
				ClassReplacer replacer = new Log4jReplacer(writer);
				ClassReader reader = new ClassReader(in.getInputStream(entry));
				reader.accept(replacer, 0);
				out.write(writer.toByteArray());
				for(Class<?> ref: replacer.getReferenced()) {
					reference(ref);
				}
			}
			names.add(entry.getName());
			out.closeEntry();
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
			out.putNextEntry(new JarEntry(name));
			copy(ref.getClassLoader().getResourceAsStream(name), out);
			out.closeEntry();
		}
	}

	public void process() throws IOException {

		try {
			copyEntries();
			addReferencedDirectories();
			addReferencedClasses();
		}
		finally {
			out.flush();
			out.close();
		}

	}

}