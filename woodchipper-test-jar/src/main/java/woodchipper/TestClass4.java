package woodchipper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.LogSource;

public class TestClass4 {
    
	private static final Log LOG1 = LogFactory.getLog(TestClass3.class);
	static {
	    LOG1.info("first message!");
	}

	Log LOG3;

	public TestClass4() {
		LOG3 = LogSource.getInstance(TestClass4.class);
		LOG3.info("third message!");
	}

	public static void main(String[] argv) {
		Log LOG2 = LogFactory.getLog(TestClass.class);
	    LOG2.info("second message!");
		new TestClass4();

	}

}