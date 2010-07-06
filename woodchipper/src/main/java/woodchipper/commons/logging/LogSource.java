package woodchipper.commons.logging;

import woodchipper.commons.logging.impl.SimpleLog;

public class LogSource extends SimpleLog {
    
	public static Log getInstance(Class<?> clazz) {
		return new SimpleLog();
	}

	public static Log getInstance(String name) {
		return new SimpleLog();
	}

}