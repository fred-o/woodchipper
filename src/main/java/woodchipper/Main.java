package woodchipper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Main {

    public static void main(String[] argv) throws Exception {

		String file = "target/classes/woodchipper/TestClass.class";

		System.out.println("reading...");

		InputStream in = new FileInputStream(file);
		ClassReader reader = new ClassReader(in);


		ClassWriter cw = new ClassWriter(0);

		reader.accept(
			new LogRenamer(cw, "org/apache/log4j/", "woodchipper/log4j/"), 0);

		System.out.println("writing...");
		
		FileOutputStream out = new FileOutputStream(file);
		out.write(cw.toByteArray());
		out.flush();
		out.close();

	}

}