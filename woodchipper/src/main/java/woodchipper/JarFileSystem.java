package woodchipper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FileUtils;

public class JarFileSystem extends AbstractFileSystem {

	JarFile in;
	JarOutputStream out;
	File input;
	File output;
	File tmp;

	Enumeration<JarEntry> entries;
	JarEntry current;
	boolean firstOutput = false;

	public JarFileSystem(File input, File output) throws IOException {

		this.in = new JarFile(input);
		this.input = input;
		this.output = output;
		this.tmp = File.createTempFile("woodchipper", "jar");
		this.out = new JarOutputStream(new FileOutputStream(tmp));

		this.entries = in.entries();
	}
	
	@Override
	public boolean next() {
		if (entries.hasMoreElements()) {
			current = entries.nextElement();
			return true;
		}
		return false;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return in.getInputStream(current);
	}

	@Override
	public String getInputPath() {
		return current.getName();
	}

	@Override
	public void nextOutput() throws IOException {
		if (!firstOutput)
			out.closeEntry();
		out.putNextEntry(current);
		firstOutput = false;
	}

	@Override
	public void nextOutput(String path) throws IOException {
		if (!firstOutput)
			out.closeEntry();
		out.putNextEntry(new JarEntry(path));
		firstOutput = false;
	}

	@Override
	public OutputStream getOutputStream() {
		return out;
	}

	@Override
	public void addOutputDirectory(String name) throws IOException {
		out.putNextEntry(new JarEntry(name));
	}

	@Override
	public void closeOutput() throws IOException {
		out.closeEntry();
		out.flush();
		out.close();
	}

	@Override
	public void commit() throws IOException {
		FileUtils.copyFile(tmp, output);
		FileUtils.deleteQuietly(tmp);
	}

}