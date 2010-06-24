package woodchipper;

import org.apache.log4j.Logger;

public class TestClass {
    
	static Logger LOG1 = Logger.getLogger(TestClass.class);
	static {
	    LOG1.info("first message!");
	}

	Logger LOG3;

	public TestClass() {
		LOG3 = Logger.getLogger(TestClass.class);
		LOG3.info("third message!");
	}
	

	public static void main(String[] argv) {
		Logger LOG2 = Logger.getLogger(TestClass.class);
	    LOG2.info("second message!");
		new TestClass();
	}

}