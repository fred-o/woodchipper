package woodchipper;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class InnerClassTest {

	static void processClass(String file) throws Exception {
		System.out.println("Reading " + file);

		ClassWriter cw = new ClassWriter(0);
		ClassVisitor lr = new Log4JHandler().makeClassReplacer(cw);

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