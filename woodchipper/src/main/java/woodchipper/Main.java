package woodchipper;

import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Main {

    public static void main(String[] argv) throws Exception {

		Opts opts = new Opts();
		CmdLineParser parser = new CmdLineParser(opts);
		try {
			parser.parseArgument(argv);
			new JarProcessor(opts.in, opts.out).process();

		} catch (CmdLineException cle) {
			System.err.println(cle.getMessage());
			System.err.println("java -jar woodchipper.jar -i <in> -o <out>");
			parser.printUsage(System.err);
		}
	}

	static class Opts {

		@Option(name="-i", usage=".jar file to read", required=true)
		private File in;

		@Option(name="-o", usage=".jar file to write to", required=true)
		private File out;

	}

}