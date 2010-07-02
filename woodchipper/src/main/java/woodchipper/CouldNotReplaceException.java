package woodchipper;

public class CouldNotReplaceException extends RuntimeException {

	private static final long serialVersionUID = -2681322825663095051L;

	private String referencingClass;

	public CouldNotReplaceException(String referencingClass, String signature) {
		super(signature);
		this.referencingClass = referencingClass;
	}

	public String getReferencingClass() {
		return referencingClass;
	}

	public String getSignature() {
		return getMessage();
	}

}