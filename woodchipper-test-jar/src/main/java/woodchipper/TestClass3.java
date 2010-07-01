package woodchipper;

import org.apache.log4j.Logger;

public class TestClass3 {
	
	private static final Logger LOG = Logger.getLogger(TestClass3.class);
    
	public static void main(String[] argv) {
		(new Runnable() {
				public void run() {
					LOG.info("hello, world!");
				}
			}).run();
	}
}