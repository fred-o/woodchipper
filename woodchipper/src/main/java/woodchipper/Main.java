package woodchipper;

import java.io.File;
import java.util.Arrays;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Main entry point for command line execution.
 *
 * @author fredrik
 */
public class Main {

    public static void main(String[] argv) throws Exception {

		Opts opts = new Opts();
		CmdLineParser parser = new CmdLineParser(opts);
		try {
			parser.parseArgument(argv);

			if (opts.out == null)
				opts.out = opts.in;

			AbstractFileSystem fileSystem;
			if (opts.in.isDirectory())
				fileSystem = new SimpleFileSystem(opts.in, opts.out);
			else
				fileSystem = new JarFileSystem(opts.in, opts.out);
			
			new WoodChipper(
				fileSystem,
				Arrays.<LogSystemHandler>asList(
					new Log4JHandler(),
					new CommonsLoggingHandler())
				).process();

		} catch (CmdLineException cle) {
			System.err.println(cle.getMessage());
			System.err.println("java -jar woodchipper.jar -i <in> -o <out>");
			parser.printUsage(System.err);
		}
	}

	static class Opts {

		@Option(name="-i", usage=".jar file or directory to read from", required=true)
		private File in;

		@Option(name="-o", usage=".jar file or directory to write to")
		private File out;

	}

}