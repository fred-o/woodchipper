package woodchipper;


public class Log4Replacement {
	
	public void info(Object o) {
		System.out.println("replaced: " + o);
	}

	public static Log4Replacement getLogger(Class<?> clazz) {
		return new Log4Replacement();
	}

	

}