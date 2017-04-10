package tool;
import org.apache.commons.cli.*;
/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class ClientCommandLine {
	public static void main(String[] args) {
        Options opt = new Options();
        // add command line option to options
        /*
        Option channel = Option.builder("channel")
        		.hasArg()
        		.desc("channel")
        		.build();
        opt.addOption(channel);
        */
        opt.addOption("channel", true, "channel");
        Option debug = Option.builder("debug")
        		.desc("print debug informaton")
        		.build();
        opt.addOption(debug);
        Option description = Option.builder("description")
        		.hasArg()
        		.desc("rescource description")
        		.build();
        opt.addOption(description);
        Option exchange = Option.builder("exchange")
        		.desc("exchange server list with server")
        		.build();
        opt.addOption(exchange);
        Option fetch = Option.builder("fetch")
        		.desc("fetch resources from server")
        		.build();
        opt.addOption(fetch);
        Option host = Option.builder("host")
        		.hasArg()
        		.desc("server host, a domain name or IP address")
        		.build();
        opt.addOption(host);
        Option owner = Option.builder("owner")
        		.hasArg()
        		.desc("owner")
        		.build();
        opt.addOption(owner);
        Option port = Option.builder("port")
        		.hasArg()
        		.desc("server port, an integer")
        		.build();
        opt.addOption(port);
        Option publish = Option.builder("publish")
        		.desc("publish resource on server")
        		.build();
        opt.addOption(publish);
        Option query = Option.builder("query")
        		.desc("query for resource from server")
        		.build();
        opt.addOption(query);
        Option remove = Option.builder("remove")
        		.desc("remove resource from server")
        		.build();
        opt.addOption(remove);
        Option secret = Option.builder("secret")
        		.hasArg()
        		.desc("secret")
        		.build();
        opt.addOption(secret);
        Option share = Option.builder("share")
        		.hasArg()
        		.desc("share resource on server")
        		.build();
        opt.addOption(share);
        Option servers = Option.builder("servers")
        		.hasArgs()
        		.desc("server list, name1:port1,name2:port2,...")
        		.build();
        opt.addOption(servers);
        Option tags = Option.builder("tags")
        		.hasArgs()
        		.desc("resource tags, tag1,tag2,tag3,...")
        		.build();
        opt.addOption(tags);
        Option uri = Option.builder("uri")
        		.hasArg()
        		.desc("resource URI")
        		.build();
        opt.addOption(uri);
        opt.addOption("h", "help", false, "help");
        
        String formatterHelp  = "WRONG COMMAND" + "\n" + "HELP";
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new  DefaultParser();
        CommandLine commandLine = null;
        //if command is wrong print out help message
        try {
            commandLine = parser.parse( opt, args);
        
        // if command line has -h or -help
        if (commandLine.hasOption("h")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(formatterHelp, "", opt, "");
            System.out.println("has h ");
            return;
        }
        
        // if command line has -channel 
        if (commandLine.hasOption("channel")) {
        	String channel1 = commandLine.getOptionValue("channel");
            System.out.println("has channel and its arg is " + channel1);
        }
        
         // if command line has -debug
        if (commandLine.hasOption("debug")) {
            System.out.println("has debug");
        }
        
        //if command line has -description
        if (commandLine.hasOption("description")) {
        	String descip = commandLine.getOptionValue("description");
            System.out.println("has description and its arg is " + descip);
        }
        
        // if command line has -exchange
        if (commandLine.hasOption("exchange")) {
            System.out.println("has exchange");
        }
        
        // if command line has -fetch
        if (commandLine.hasOption("fetch")) {
            System.out.println("has fetch");
        }
        
        //if command line has -port
        if (commandLine.hasOption("host")) {
        	String hostName = commandLine.getOptionValue("host");
            System.out.println("has host and its arg is " + hostName);
        }
        
        //if command line has -name
        if (commandLine.hasOption("name")) {
        	String resourceName = commandLine.getOptionValue("name");
            System.out.println("has name and its arg is " + resourceName);
        }
        
        //if command line has -owner
        if (commandLine.hasOption("owner")) {
        	String ownerName = commandLine.getOptionValue("owner");
            System.out.println("has owner and its arg is " + ownerName);
        }
        
        //if command line has -port
        if (commandLine.hasOption("port")) {
        	String portName = commandLine.getOptionValue("port");
        	int portNum;
        	portNum = Integer.parseInt(portName);
            System.out.println("has port and its arg is " + portNum);
        }
        
        // if command line has -publish
        if (commandLine.hasOption("publish")) {
            System.out.println("has publish");
        }
        
        // if command line has -query
        if (commandLine.hasOption("query")) {
            System.out.println("has query");
        }
        
        // if command line has -remove
        if (commandLine.hasOption("remove")) {
            System.out.println("has remove");
        }
        
        //if command line has -secret
        if (commandLine.hasOption("secret")) {
        	String secretName = commandLine.getOptionValue("secret");
            System.out.println("has secretl and its arg is " + secretName);
        }
        
        //if command line has -servers
        if (commandLine.hasOption("servers")) {
        	String serversName = commandLine.getOptionValue("servers");
            System.out.println("has servers and its arg is " + serversName);
        }
        
        
        // if command line has -share
        if (commandLine.hasOption("share")) {
            System.out.println("has share");
        }
        
        //if command line has -uri
        if (commandLine.hasOption("uri")) {
        	String uriName = commandLine.getOptionValue("uri");
            System.out.println("has uri and its arg is " + uriName);
        }
        
        } catch (ParseException e) {
            formatter.printHelp( formatterHelp,opt ); 
        }
        

        
    }
	
}
