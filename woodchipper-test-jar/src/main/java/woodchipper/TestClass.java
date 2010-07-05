package woodchipper;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class TestClass {
    
	static Logger LOG1 = Logger.getLogger(TestClass.class);
	static {
	    LOG1.info("first message!");
	}

	Logger LOG3;
	Priority priority;

	public TestClass() {
		LOG3 = Logger.getLogger(TestClass.class);
		LOG3.info("third message!");
	}

	public static void main(String[] argv) {
		Logger LOG2 = Logger.getLogger(TestClass.class);
	    LOG2.info("second message!");
		new TestClass();

		LOG2.log(Level.INFO, "fourth message!");
	}

}