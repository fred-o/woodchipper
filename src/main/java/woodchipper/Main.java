package woodchipper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class Main {

    public static void main(String[] argv) throws Exception {

		String file = "target/classes/woodchipper/TestClass.class";

		System.out.println("reading...");

		InputStream in = new FileInputStream(file);
		ClassReader reader = new ClassReader(in);


		Set from = new HashSet();
		from.add("Lorg/apache/log4j/Logger");

		ClassWriter cw = new ClassWriter(0);

		reader.accept(
			new LogRenamer(cw, "org/apache/log4j/Logger", "woodchipper/Log4Replacement"), 0);

		System.out.println("writing...");
		
		FileOutputStream out = new FileOutputStream(file);
		out.write(cw.toByteArray());
		out.flush();
		out.close();

	}

}