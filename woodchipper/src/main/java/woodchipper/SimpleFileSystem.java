package woodchipper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.io.FileUtils;

/**
 * @author fredrik
 */
public class SimpleFileSystem extends AbstractFileSystem {
	static Random r = new Random();

	File inputDir;
	File outputDir;
	File tmpDir;

	Iterator<File> files;
	File current;
	InputStream currentInput;

	OutputStream currentOutput;
	
	@SuppressWarnings("unchecked")
	public SimpleFileSystem(File inputDir, File outputDir) throws IOException {
		this.inputDir = inputDir;
		this.outputDir = outputDir;
		this.tmpDir = createTempDir();

		this.files = FileUtils.iterateFiles(inputDir,
				new String[] { "class", "properties" },
				true
			);
	}

	@Override
	public boolean next() throws IOException {
		if (files.hasNext()) {
			this.current = files.next();
			this.currentInput = new BufferedInputStream(new FileInputStream(current));
			return true;
		}
		return false;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return currentInput;
	}

	@Override
	public String getInputPath() {
		return current.getAbsolutePath().substring(inputDir.getAbsolutePath().length());
	}

	@Override
	public void nextOutput() throws IOException {
		nextOutput(getInputPath());
	}

	@Override
	public void nextOutput(String path) throws IOException {
		closeOutput();
		File o = new File(tmpDir, path);
		new File(o.getParent()).mkdirs();
		this.currentOutput = new BufferedOutputStream(new FileOutputStream(o));
	}

	@Override
	public OutputStream getOutputStream() {
		return currentOutput;
	}

	@Override
	public void addOutputDirectory(String name) throws IOException {
		File d = new File(tmpDir, name);
		if (!d.exists())
			d.mkdirs();
	}

	@Override
	public void commit() throws IOException {
		FileUtils.copyDirectory(tmpDir, outputDir);
		FileUtils.deleteDirectory(tmpDir);
	}

	@Override
	public void closeOutput() throws IOException {
		if (currentOutput != null) {
			currentOutput.flush();
			currentOutput.close();
		}
	}

	public static File createTempDir() throws IOException {
		File f;
		do {
			f = new File(System.getProperty("java.io.tmpdir") + File.separator + "woodchipper" + r.nextInt());
		} while (f.exists());
		f.mkdirs();
		return f;
	}

}