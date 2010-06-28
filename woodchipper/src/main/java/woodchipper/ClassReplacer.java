package woodchipper;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import woodchipper.CouldNotReplaceException;

public abstract class ClassReplacer extends ClassAdapter implements Opcodes {

	private String from;
	private String to;
	private String currentClassName;
	private boolean modified;

	protected Set<String> cached = new HashSet<String>();
	protected Set<Class<?>> referenced = new HashSet<Class<?>>();
    
	public ClassReplacer(ClassVisitor cv, String from, String to) {
		super(cv);
		this.from = from;
		this.to = to;
	}

	protected boolean hasReplacementMethod(String owner, String name, String desc) {
		String key = owner + "|" + name + "|" + desc;
		if (cached.contains(key)) 
			return true;

		try {
			Class<?> clazz = Class.forName(owner.replaceAll("/", "."));

			Type[] signature = Type.getArgumentTypes(desc);
			Class<?>[] args = new Class[signature.length];
			
			for(int i = 0; i < signature.length; i++) {
			    args[i] = Class.forName(signature[i].getClassName());
			}

			clazz.getMethod(name, args);

			referenced.add(clazz);
			cached.add(key);
			modified = true;

			return true;

		} catch (ClassNotFoundException cnfe) {
		} catch (NoSuchMethodException nsme) {
		}
		return false;
	}

	protected boolean hasReplacementClass(String desc) {
		String key = "||" + desc;
		if (cached.contains(key))
			return true;
		
		try {
			referenced.add(Class.forName(Type.getType(desc).getClassName()));
			cached.add(key);
			modified = true;

			return true;
		} catch (ClassNotFoundException cnfe) {
		}

		return false;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName,
			String[] interfaces) {
		this.modified = false;
		this.currentClassName = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
//		System.out.println("visit: " + name + " - " + desc + " - " + signature);

		String modifiedDesc = desc.replaceAll(from, to);

		if (!modifiedDesc.equals(desc)) {
			if (hasReplacementClass(modifiedDesc)) {
				return super.visitField(access, name, modifiedDesc, signature, value);
			} else {
				throw new CouldNotReplaceException(currentClassName + "#" + name + desc);
			}
		} else {
			return super.visitField(access, name, desc, signature, value);
		}
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
			// System.out.println("FIELD opcode: " + opcode + " - " + " - " +name + " - " + desc);

			String modifiedOwner = owner.replaceAll(from, to);
			String modifiedDesc = desc.replaceAll(from, to);

			if (!modifiedOwner.equals(owner) || !modifiedDesc.equals(desc)) {
				if (hasReplacementClass(modifiedDesc)) {
					super.visitFieldInsn(opcode, modifiedOwner, name, modifiedDesc);
				} else {
					throw new CouldNotReplaceException(currentClassName + "#" + name + desc);
				}
			} else {
				super.visitFieldInsn(opcode, owner, name, desc);
			}
		}

		@Override
		public void visitMethodInsn (int opcode, String owner, String name, String desc) {
			// System.out.println("METHOD opcode: " + opcode + " - " + owner + " - " + " - " +name + "- " + desc);

			String modifiedOwner = owner.replaceAll(from, to);
			String modifiedDesc = desc.replaceAll(from, to);

			if (!modifiedOwner.equals(owner) || !modifiedDesc.equals(desc)) {
				if (hasReplacementMethod(modifiedOwner, name, modifiedDesc)) {
					super.visitMethodInsn(opcode, modifiedOwner, name, modifiedDesc);
				} else {
					throw new CouldNotReplaceException(currentClassName + "#" + name + desc);
				}
			} else {
				super.visitMethodInsn(opcode, owner, name, desc);
			}
		}
	}

	public Set<Class<?>> getReferenced() {
		return referenced;
	}

	public boolean isModified() {
		return modified;
	}

}