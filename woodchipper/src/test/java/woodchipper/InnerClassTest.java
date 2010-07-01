package woodchipper;

import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import woodchipper.Log4jReplacer;

public class InnerClassTest {

	static void processClass(String file) throws Exception {
		System.out.println("Reading " + file);

		ClassWriter cw = new ClassWriter(0);
		Log4jReplacer lr = new Log4jReplacer(cw);
		ClassReader cr = new ClassReader(
			new FileInputStream(file));
		cr.accept(lr, 0);

		FileOutputStream out = new FileOutputStream(file);
		out.write(cw.toByteArray());
	}

	@Test
	public void testProcessInnerClasses() throws Exception {
		processClass("../woodchipper-test-jar/target/classes/woodchipper/TestClass3.class");
		processClass("../woodchipper-test-jar/target/classes/woodchipper/TestClass3$1.class");
		System.out.println("done");
	}

}