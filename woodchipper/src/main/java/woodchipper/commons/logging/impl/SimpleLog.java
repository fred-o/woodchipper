package woodchipper.commons.logging.impl;


import woodchipper.commons.logging.Log;

public class SimpleLog implements Log {
    
    public boolean isDebugEnabled() {
		return false;
	}

    public boolean isErrorEnabled() {
		return true;
	}

    public boolean isFatalEnabled() {
		return true;
	}

    public boolean isInfoEnabled() {
		return true;
	}

    public boolean isTraceEnabled() {
		return false;
	}

    public boolean isWarnEnabled() {
		return true;
	}

    public void trace(Object message) {
	}

    public void trace(Object message, Throwable t) {
	}

    public void debug(Object message) {
	}

    public void debug(Object message, Throwable t) {
	}

    public void info(Object message) {
		System.out.println(message);
	}

    public void info(Object message, Throwable t) {
		System.out.println(message);
		t.printStackTrace();
	}

    public void warn(Object message) {
		System.out.println(message);
	}

    public void warn(Object message, Throwable t) {
		System.out.println(message);
		t.printStackTrace();
	}

    public void error(Object message) {
		System.err.println(message);
	}

    public void error(Object message, Throwable t) {
		System.err.println(message);
		t.printStackTrace();
	}

    public void fatal(Object message) {
		System.err.println(message);
	}

    public void fatal(Object message, Throwable t) {
		System.err.println(message);
		t.printStackTrace();
	}

}