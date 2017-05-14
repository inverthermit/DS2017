/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package tool;

import org.apache.commons.cli.*;
import model.ServerModel;

/**
 * This class is implemented to add server command line options  and initialize
 * the server.To be noticed,the default advertised host name will be the operating 
 * system supplied hostname.
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class ServerCommandLine {
	
	/**
     * The function of this method is to add server command line options and
     * initialize the sever
     */
	public static ServerModel ServerCommandLine(String[] args) {
		Options opt = new Options();
		// add command line option to options
		opt.addOption("advertisedhostname", true, "advised hostname");
		Option connectionintervallimit = Option
				.builder("connectionintervallimit").hasArg()
				.desc("connection interval limit in seconds").build();
		opt.addOption(connectionintervallimit);
		Option exchangeinterval = Option.builder("exchangeinterval").hasArg()
				.desc("exchange interval in seconds").build();
		opt.addOption(exchangeinterval);
		Option port = Option.builder("port").hasArg()
				.desc("server port, an integer").build();
		opt.addOption(port);
		Option secret = Option.builder("secret").hasArg().desc("secret")
				.build();
		opt.addOption(secret);
		Option debug = Option.builder("debug").desc("print debug informaton")
				.build();
		opt.addOption(debug);
		Option sport = Option.builder("sport").hasArg()
				.desc("the secure port number").build();
		opt.addOption(sport);
		opt.addOption("h", "help", false, "help");

		ServerModel server = new ServerModel();
		String formatterHelp = "WRONG COMMAND" + "\n" + "HELP";
		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = null;
		// if command is wrong print out help message
		try {
			commandLine = parser.parse(opt, args);

			// if command line has -h or -help
			if (commandLine.hasOption("h")) {
				HelpFormatter hf = new HelpFormatter();
				hf.printHelp(formatterHelp, "", opt, "");
				//System.out.println("has h ");
				return null;
			}

			// if command line has -advertisedhostname
			if (commandLine.hasOption("advertisedhostname")) {
				String hostName = commandLine
						.getOptionValue("advertisedhostname");
				server.advertisedHostName=(hostName);
				//System.out.println("has advertisedhostname and its arg is "+ hostName);
			}

			// if command line has -connectionintervallimit
			if (commandLine.hasOption("connectionintervallimit")) {
				String intervalLimit = commandLine
						.getOptionValue("connectionintervallimit");
				// ServerModel server = new ServerModel();
				server.setIntervalLimit(intervalLimit);
				//System.out.println("has connectionintervallimit and its arg is "+ intervalLimit);
			}

			// if command line has -exchangeinterval
			if (commandLine.hasOption("exchangeinterval")) {
				String exchangeInterval = commandLine
						.getOptionValue("exchangeinterval");
				// ServerModel server = new ServerModel();
				server.setExchangeInterval(exchangeInterval);
				//System.out.println("has exchangeinterval and its arg is "+ exchangeInterval);
			}

			// if command line has -port
			if (commandLine.hasOption("port")) {
				String portName = commandLine.getOptionValue("port");
				int portNum;
				portNum = Integer.parseInt(portName);
				// ServerModel server = new ServerModel();
				server.setPort(portNum);
				//System.out.println("has port and its arg is " + portNum);
			}

			// if command line has -secret
			if (commandLine.hasOption("secret")) {
				String secretName = commandLine.getOptionValue("secret");
				// ServerModel server = new ServerModel();
				server.setSecret(secretName);
				//System.out.println("has secret and its arg is " + secretName);
			}
			
			// if command line has -sport
			if (commandLine.hasOption("sport")) {
				String sportName = commandLine.getOptionValue("sport");
				int sportNum;
				sportNum = Integer.parseInt(sportName);
				// ServerModel server = new ServerModel();
				server.setPort(sportNum);
				//System.out.println("has port and its arg is " + portNum);
			}

			// if command line has -debug
			if (commandLine.hasOption("debug")) {
				Log.debug = true;
				//System.out.println("has debug");
			}
			return server;
		} catch (ParseException e) {
			formatter.printHelp(formatterHelp, opt);
		}
		return null;
	}

}
