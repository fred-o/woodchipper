package woodchipper;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * A {@link ClassVisitor} implementation that does a wildcard
 * replacement of the packages of classes referenced.
 * 
 * @author fredrik
 */
public class ClassReplacer extends ClassAdapter implements Opcodes {

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

	private Class<?> getClassForType(Type type) throws ClassNotFoundException {
		switch(type.getSort()) {
		case Type.INT: return Integer.TYPE;
		case Type.LONG: return Long.TYPE;
		case Type.SHORT: return Short.TYPE;
		case Type.BYTE: return Byte.TYPE;
		case Type.CHAR: return Character.TYPE;
		case Type.FLOAT: return Float.TYPE;
		case Type.DOUBLE: return Double.TYPE;
		case Type.BOOLEAN: return Boolean.TYPE;
		default: return Class.forName(type.getClassName());
		}
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
			    args[i] = getClassForType(signature[i]);
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
		this.currentClassName = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		// System.out.println("visitFIELD: " + name + " - " + desc + " - " + signature);

		String modifiedDesc = desc.replaceAll(from, to);

		if (!modifiedDesc.equals(desc)) {
			if (hasReplacementClass(modifiedDesc)) {
				return super.visitField(access, name, modifiedDesc, signature, value);
			} else {
				throw new CouldNotReplaceException(currentClassName, currentClassName + "#" + name + desc);
			}
		} else {
			return super.visitField(access, name, desc, signature, value);
		}
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		// System.out.println("visitMETHOD: " + name + " - " + desc + " - " + signature);
		return new LogMethodRenamer(cv.visitMethod(access, name, desc.replaceAll(from, to), signature, exceptions));
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
					throw new CouldNotReplaceException(currentClassName, owner + "#" + name + desc);
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

			if (!modifiedOwner.equals(owner)) {
				if (hasReplacementMethod(modifiedOwner, name, modifiedDesc)) {
					super.visitMethodInsn(opcode, modifiedOwner, name, modifiedDesc);
					return;
				} else {
					throw new CouldNotReplaceException(currentClassName, owner + "#" + name + desc);
				}
			} 
			super.visitMethodInsn(opcode, owner, name, modifiedDesc);
		}
	}

	public Set<Class<?>> getReferenced() {
		return referenced;
	}

	public boolean isModified() {
		return modified;
	}

}