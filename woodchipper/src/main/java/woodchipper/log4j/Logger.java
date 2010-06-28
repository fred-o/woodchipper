package woodchipper.log4j;

/**
 * Replacement for {@link org.apache.log4j.Logger}, with an implicit
 * log level of INFO.
 *
 * @author fredrik
 */
public class Logger extends Category {

	public static Logger getLogger(Class<?> clazz) {
		return new Logger();
	}

	public static Logger getLogger(String s) {
		return new Logger();
	}

	public static Logger getRootLogger() {
		return new Logger();
	}

	public boolean isTraceEnabled() {
		return false;
	}

	public void trace(Object message, Throwable t) {
	}

	public void trace(Object message) {
	}

}