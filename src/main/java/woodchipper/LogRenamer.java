package woodchipper;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LogRenamer extends ClassAdapter implements Opcodes {

	private String from;
	private String to;
    
	public LogRenamer(ClassVisitor cv, String from, String to) {
		super(cv);
		this.from = from;
		this.to = to;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		System.out.println("visit: " + name + " - " + desc + " - " + signature);
		return cv.visitField(access, name, desc.replaceAll(from, to), signature, value);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return new LogMethodRenamer(cv.visitMethod(access, name, desc, signature, exceptions));
	}

	class LogMethodRenamer extends MethodAdapter {
		public LogMethodRenamer(MethodVisitor mv) {
			super(mv);
		}

		@Override 
		public void visitFieldInsn (int opcode, String owner, String name, String desc) {
			System.out.println("FIELD opcode: " + opcode + " - " + " - " +name + "- " + desc);
			mv.visitFieldInsn(opcode, owner.replaceAll(from, to), name, desc.replaceAll(from, to));
		}

		@Override
		public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
			System.out.println("LOCALVAR: " + name + " - " + signature);
			mv.visitLocalVariable(name, desc, signature, start, end, index);
		}

		@Override
		public void visitMethodInsn (int opcode, String owner, String name, String desc) {
			System.out.println("METHOD opcode: " + opcode + " - " + owner + " - " + " - " +name + "- " + desc);
			mv.visitMethodInsn(opcode, owner.replaceAll(from, to), name, desc.replaceAll(from, to));
		}


	}

}