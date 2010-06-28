package woodchipper;

import org.objectweb.asm.ClassVisitor;

public class Log4jReplacer extends ClassReplacer {
    
	public Log4jReplacer(ClassVisitor cv) {
		super(cv, "org/apache/log4j/", "woodchipper/log4j/");
		// super(cv, new Remapper() {
		// 		@Override
		// 		public String map(String typeName) {
		// 			System.out.println("REMAP: " + typeName);
		// 			return typeName.replaceAll("org/apache/log4j/", "woodchipper/log4j/");
		// 		}

		// 		@Override
		// 		public String mapMethodName(String owner, String name, String desc) {
		// 			return super.mapMethodName(owner, name, desc);
		// 		}
		// 	});
	}
	
}