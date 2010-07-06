package woodchipper;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.objectweb.asm.ClassVisitor;

public abstract class LogSystemHandler {

	private String origPackage;
	private String replacementPackage;

	public LogSystemHandler(String origPackage, String replacementPackage) {
		this.origPackage = origPackage;
		this.replacementPackage = replacementPackage;
	}
    
	public ClassReplacer makeClassReplacer(ClassVisitor parent) {
		return new ClassReplacer(parent, origPackage, replacementPackage);
	}

	public IOFileFilter getFilter() {
		return new IOFileFilter() {
				public boolean accept(File file) {
					return file.getAbsolutePath().contains("org/apache/log4j");
				}
				public boolean accept(File file, String name) {
					return file.getAbsolutePath().contains("org/apache/log4j");
				}
			};
	}

	public abstract String getSystemName();
	
}