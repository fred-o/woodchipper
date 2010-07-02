package woodchipper;

import java.io.FileFilter;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.objectweb.asm.ClassVisitor;

public abstract class LogSystemHandler {
    
	public abstract ClassReplacer makeClassReplacer(ClassVisitor parent);

	public FileFilter getFilter() {
		return FalseFileFilter.FALSE;
	}

	public abstract String getSystemName();
	
}