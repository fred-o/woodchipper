package woodchipper.log4j;

/**
 * Replacement för {@link org.apache.log4j.Priority}
 *
 * @author fredrik
 */
public class Priority {

	public final static int OFF_INT = Integer.MAX_VALUE;
	public final static int FATAL_INT = 50000;
	public final static int ERROR_INT = 40000;
	public final static int WARN_INT  = 30000;
	public final static int INFO_INT  = 20000;
	public final static int DEBUG_INT = 10000;
	public final static int ALL_INT = Integer.MIN_VALUE;

	final static public Priority FATAL = new Level(FATAL_INT);
	final static public Priority ERROR = new Level(ERROR_INT);
	final static public Priority WARN  = new Level(WARN_INT);
	final static public Priority INFO  = new Level(INFO_INT);
	final static public Priority DEBUG = new Level(DEBUG_INT);

	int level;

	protected Priority(int level) {
		this.level = level;
	}


}