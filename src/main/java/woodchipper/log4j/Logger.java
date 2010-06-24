package woodchipper.log4j;

/**
 * Replacement for {@link org.apache.log4j.Logger}, with an implicit
 * log level of INFO.
 *
 * @author fredrik
 */
public class Logger {

	public static Logger getLogger(Class<?> clazz) {
		return new Logger();
	}

	public static Logger getLogger(String s) {
		return new Logger();
	}

	public static Logger getRootLogger() {
		return new Logger();
	}

	public void debug(Object message, Throwable t) {
	}

	public void debug(Object message) {
	}

	public void error(Object message, Throwable t) {
		System.out.println(message);
		t.printStackTrace();
	}

	public void error(Object message) {
		System.out.println(message);
	}

	public void fatal(Object message, Throwable t) {
		System.out.println(message);
		t.printStackTrace();
	}

	public void fatal(Object message) {
		System.out.println(message);
	}

	public void info(Object message, Throwable t) {
		System.out.println(message);
		t.printStackTrace();
	}

	public void info(Object message) {
		System.out.println(message);
	}

	public boolean isDebugEnabled() {
		return false;
	}

	public boolean isEnabledFor(Priority level) {
		return false;
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public boolean isTraceEnabled() {
		return false;
	}

	public void log(Priority priority, Object message, Throwable t) {
	}

	public void log(Priority priority, Object message) {
	}

	public void log(String callerFQCN, Priority level, Object message, Throwable t) {
	}

	public void trace(Object message, Throwable t) {
	}

	public void trace(Object message) {
	}

	public void warn(Object message, Throwable t) {
		System.out.println(message);
		t.printStackTrace();
	}

	public void warn(Object message) {
		System.out.println(message);
	}

}