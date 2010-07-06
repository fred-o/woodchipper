package woodchipper;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;

public class Log4JHandler extends LogSystemHandler {

	public Log4JHandler() {
		super("org/apache/log4j/", "woodchipper/log4j/");
	}

	@Override
	public IOFileFilter getFilter() {
		return new OrFileFilter(
			new NameFileFilter(
				new String[] {
					"log4j.properties",
					"log4j.xml" }),
			super.getFilter());
	}

	@Override
	public String getSystemName() {
		return "Log4J";
	}
	
}