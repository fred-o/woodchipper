package woodchipper.log4j;

/**
 * Replacement for {@link org.apache.log4j.Category}.
 * 
 * @author fredrik
 */
public class Category {
    
	public static Category getInstance(Class<?> clazz) {
		return new Category();
	}

	public static Category getInstance(String name) {
		return new Category();
	}

	public void debug(Object message, Throwable t) {
	}

	public void debug(Object message) {
	}

	public void error(Object message, Throwable t) {
		System.err.println(message);
		t.printStackTrace();
	}

	public void error(Object message) {
		System.err.println(message);
	}

	public void fatal(Object message, Throwable t) {
		System.err.println(message);
		t.printStackTrace();
	}

	public void fatal(Object message) {
		System.err.println(message);
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
		return level.level >= Level.INFO.level;
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public void log(Priority priority, Object message, Throwable t) {
		if (priority.level >= Level.ERROR_INT) 
			error(message, t);
		else if (priority.level >= Level.INFO_INT) 
			info(message, t);
	}

	public void log(Priority priority, Object message) {
		if (priority.level >= Level.ERROR_INT) 
			error(message);
		else if (priority.level >= Level.INFO_INT) 
			info(message);
	}

	public void log(String callerFQCN, Priority priority, Object message, Throwable t) {
		if (priority.level >= Level.ERROR_INT) 
			error(message, t);
		else if (priority.level >= Level.INFO_INT) 
			info(message, t);
	}

	public void warn(Object message, Throwable t) {
		System.out.println(message);
		t.printStackTrace();
	}

	public void warn(Object message) {
		System.out.println(message);
	}

	public String getName() {
		return "";
	}

}