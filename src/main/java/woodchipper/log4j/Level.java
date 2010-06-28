package woodchipper.log4j;

/**
 * Replacement for {@link org.apache.log4j.Level}.
 * 
 * @author fredrik
 */
public class Level extends Priority {

	final static public Level OFF = new Level(OFF_INT);
	final static public Level FATAL = new Level(FATAL_INT);
	final static public Level ERROR = new Level(ERROR_INT);
	final static public Level WARN  = new Level(WARN_INT);
	final static public Level INFO  = new Level(INFO_INT);
	final static public Level DEBUG = new Level(DEBUG_INT);
	final static public Level ALL = new Level(ALL_INT);

	protected Level(int level) {
		super(level);
	}

}