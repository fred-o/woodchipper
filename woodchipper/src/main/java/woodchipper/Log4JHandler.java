package woodchipper;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.objectweb.asm.ClassVisitor;

public class Log4JHandler extends LogSystemHandler {
    
	@Override
	public ClassReplacer makeClassReplacer(ClassVisitor parent) {
		return new ClassReplacer(parent, "org/apache/log4j/", "woodchipper/log4j/");
	}

	@Override
	public FileFilter getFilter() {
		return new OrFileFilter(
			new NameFileFilter("log4j.properties"),
			new IOFileFilter() {
				public boolean accept(File file) {
					return file.getAbsolutePath().contains("org/apache/log4j");
				}
				public boolean accept(File file, String name) {
					return file.getAbsolutePath().contains("org/apache/log4j");
				}
			});
	}

	@Override
	public String getSystemName() {
		return "Log4J";
	}
	
}