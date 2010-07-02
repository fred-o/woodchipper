package woodchipper;

import java.io.FileFilter;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.objectweb.asm.ClassVisitor;

public class Log4JHandler extends LogSystemHandler {
    
	@Override
	public ClassReplacer makeClassReplacer(ClassVisitor parent) {
		return new ClassReplacer(parent, "org/apache/log4j/", "woodchipper/log4j/");
	}

	@Override
	public FileFilter getFilter() {
		return new NameFileFilter("log4j.properties");
	}

	@Override
	public String getSystemName() {
		return "Log4J";
	}
	
}