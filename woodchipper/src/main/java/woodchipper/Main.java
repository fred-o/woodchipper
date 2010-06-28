package woodchipper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Main {

    public static void main(String[] argv) throws Exception {

		JarProcessor jp = new JarProcessor(
			new File("../woodchipper-test-jar/target/woodchipper-test-jar-1.0-SNAPSHOT.jar"),
			new File("target/out.jar"));
		jp.process();

	}

}