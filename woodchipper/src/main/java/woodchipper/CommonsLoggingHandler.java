package woodchipper;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;

public class CommonsLoggingHandler extends LogSystemHandler {
    
	public CommonsLoggingHandler() {
		super("org/apache/commons/logging/", "woodchipper/commons/logging/");
	}

	@Override
	public IOFileFilter getFilter() {
		return new OrFileFilter(
			new NameFileFilter("commons-logging.properties"),
			super.getFilter());
	}

	@Override
	public String getSystemName() {
		return "Commons Logging";
	}

}