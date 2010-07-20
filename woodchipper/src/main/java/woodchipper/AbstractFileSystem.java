package woodchipper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractFileSystem {

	public abstract boolean next() throws IOException;

	public abstract InputStream getInputStream() throws IOException;

	public abstract String getInputPath();

	public abstract void nextOutput() throws IOException;

	public abstract void nextOutput(String path) throws IOException;

	public abstract OutputStream getOutputStream() throws IOException;

	public abstract void addOutputDirectory(String name) throws IOException;

	public abstract void closeOutput() throws IOException;

	public abstract void commit() throws IOException;

}