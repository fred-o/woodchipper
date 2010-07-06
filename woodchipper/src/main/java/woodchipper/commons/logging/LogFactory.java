package woodchipper.commons.logging;

import woodchipper.commons.logging.impl.SimpleLog;

public class LogFactory extends SimpleLog {
    
	public static Log getLog(Class<?> clazz) {
		return new SimpleLog();
	}

	public static Log getLog(String name) {
		return new SimpleLog();
	}

}