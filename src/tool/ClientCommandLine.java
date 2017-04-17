package tool;

import java.util.ArrayList;

import org.apache.commons.cli.*;

import model.Resource;
import model.ServerModel;
import model.Response.NormalResponse;
import model.command.*;
/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class ClientCommandLine {
	public static final int[] FETCH = {-1,-1,-1,1,1,-1,-1,-1,-1,-1};
	public static final int[] EXCHANGE = {-1,-1,-1,-1,-1,-1,-1,-1,1,1};
	public static final int[] REMOVE= {-1,-1,-1,1,1,1,-1,-1,-1,-1};
	public static final int[] QUERY={0,0,0,0,1,0,0,-1,-1,-1};
	public static final int[] PUBLISH= {0,0,0,1,0,0,0,-1,-1,1,1};
	public static final int[] SHARE= {0,0,0,1,0,0,-1,1,-1,-1};
	private static boolean valid;
	
	public static String ClientCommandLine(String[] args) {
		Resource resource = new Resource();
		String request = null;
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
        
        CommandLineParser parser = new  DefaultParser();
        CommandLine commandLine = null;
        //check commandline
        try {
            commandLine = parser.parse( opt, args);
            Option[] commandline = commandLine.getOptions();
            if(commandline.length==0){
            	ErrorMessage error = new ErrorMessage();
        		NormalResponse response = new NormalResponse("error", error.GENERIC_MISS_INCORRECT);
        		System.out.println(response.toJSON());
            } else {
            	String command = commandline[0].toString();
            	switch (command){
            	case "publish":
            		request = cliPublish(commandline);
            		break;
            	case "remove":
            		request = cliRemove(commandline);
            		break;
            	case "query":
            		request = cliQuery(commandline);
            		break;
            	case "fetch":
            		request = cliFetch(commandline);
            		break;
            	case "share":
            		request = cliPublish(commandline);
            		break;
            	case "exchange":
            		request = cliPublish(commandline);
            		break;
            	default:
            		ErrorMessage error = new ErrorMessage();
            		NormalResponse response = new NormalResponse("error", error.GENERIC_INVALID);
            		System.out.println(response.toJSON());
            		break;
            	}
            	if(valid == false){
            		request = null;
            	}
            	return request;
            }
        } catch (ParseException e) {
            //formatter.printHelp( formatterHelp,opt ); 
        }
		return request;      
    }

	public static String cliPublish(Option[] commandline) {
		String request;
		Resource resource = new Resource();
		resource = cliResource(commandline, PUBLISH, "publish");
		Publish publish = new Publish("PUBLISH", resource);
		request = publish.toJSON();
		return request;
	}

	public static String cliRemove(Option[] commandline) {
		String request;
		Resource resource = new Resource();
		resource = cliResource(commandline, REMOVE, "remove");
		Remove remove = new Remove("REMOVE", resource);
		request = remove.toJSON();
		return request;
	}

	public static String cliQuery(Option[] commandline) {
		String request;
		Resource resource = new Resource();
		resource = cliResource(commandline,QUERY, "query");
		Query query = new Query("QUERY",true, resource);
		request = query.toJSON();
		return request;
	}

	public static String cliFetch(Option[] commandline) {
		String request;
		Resource resource = new Resource();
		resource = cliResource(commandline, FETCH, "fetch");
		Fetch fetch = new Fetch("FETCH", resource);
		request = fetch.toJSON();
		return request;
	}

	public static String cliShare(Option[] commandline) {
		String request;
		String secret = null;
		Resource resource = new Resource();
		resource = cliResource(commandline, PUBLISH, "publish");
		for(int i =1;i<commandline.length;i++){
			if (commandline[i].toString().equals("share")){
				secret = commandline[i].getValue();
				checkString(secret);
			} else {
				ErrorMessage error = new ErrorMessage();
        		NormalResponse response = new NormalResponse("error", error.SHARE_MISSING);
        		System.out.println(response.toJSON());
        		secret = "";
        		valid = false;
			}
		}
		Share share = new Share("SHARE",secret, resource);
		request = share.toJSON();
		return request;
	}

	public static String cliExchange(Option[] commandline) {
		String request,hostName;
		int portNum;
		String[] serverlists = null,servers;
		ArrayList<ServerModel> serverList = new ArrayList<ServerModel>();
		for(int i =1;i<commandline.length;i++){
			if (commandline[i].toString().equals("servers")){
				serverlists = commandline[i].getValues();
		}
		for(int m =0;i<serverlists.length;i++){
			String serverlist = serverlists[i];
			servers = serverlist.split(":");
			hostName = servers[0];
			portNum = Integer.parseInt(servers[1]);
			ServerModel sm = new ServerModel(hostName,portNum);
			serverList.add(sm);
		}
		}
		Exchange exchange = new Exchange("EXCHANGE", serverList);
		request = exchange.toJSON();
		return request;
	}

	public static Resource cliResource(Option[] commandline, int[] command, String commandName) {
		Resource resource = new Resource();
		int[] arg = new int[command.length];
		String resourceFeature;
		for (int i = 1; i < commandline.length; i++) {
			resourceFeature = commandline[i].toString();
			switch (resourceFeature) {
			case "name":
			   checkString(commandline[i].getValue());
			   resource.setName(commandline[i].getValue());
			   arg[0]=1;
			   break;
			case "tags":
				checkStringArray(commandline[i].getValues());
				resource.setTags(commandline[i].getValues());
			    arg[1]=1;
			    break;
			case "description":
				checkString(commandline[i].getValue());
				resource.setDescription(commandline[i].getValue());
			    arg[2]=1;
			    break;
			case "uri":
				checkString(commandline[i].getValue());
			    resource.setUri(commandline[i].getValue());
			    arg[3]=1;
			    break;
			case "channel":
				checkString(commandline[i].getValue());
			    resource.setChannel(commandline[i].getValue());
			    arg[4]=1;
			    break;
			case "owner":
				checkString(commandline[i].getValue());
			    resource.setOwner(commandline[i].getValue());
			    arg[5]=1;
			    break;
			case "servers":
				checkStringArray(commandline[i].getValues());
			    resource.setEZserver(commandline[i].getValues());
			    arg[6]=1;
			    break;
			default:
			   break;
			}
		}
		checkArg(arg,command,commandName);
		return resource;
	}

	public static void checkArg(int[] arg, int[] command, String commandName) {
		ErrorMessage error = new ErrorMessage();
		NormalResponse response;
		for(int i =0;i<arg.length;i++){
			if( command[i]==1 && arg[i]!=command[i]){
				switch(commandName){
				case "publish":
				case "remove":
					response = new NormalResponse("error", error.PUBLISH_REMOVE_RESOURCE_MISSING);
	        		System.out.println(response.toJSON());
	        		break;
				case "query":
				case "fetch":
				case "exchange":
	        		response = new NormalResponse("error", error.QUERY_FETCH_EXCHANGE_RESOURCETEMPLATE_MISSING);
	        		System.out.println(response.toJSON());
	        		break;
				case "share":
					response = new NormalResponse("error", error.SHARE_MISSING );
	        		System.out.println(response.toJSON());
	        		break;
	        	default:
	        		break;	
				}
				break;
			}
		}
		valid = false;
	}

	// treat this situation for invalid
	public static boolean checkString(String str) {
		if(str.contains("\0")||str.charAt(0)==' ' || str.charAt(str.length())==' '){
			ErrorMessage error = new ErrorMessage();
    		NormalResponse response = new NormalResponse("error", error.GENERIC_INVALID);
    		System.out.println(response.toJSON());	
    		valid = false;
    		return true;
		} else { return false;}
	}

	public static void checkStringArray(String[] strs) {
		for(int i =0; i<strs.length;i++){
			String str =strs[i];
			if (checkString(str)){
				valid = false;
				break;
			}
		}
	}
	
}
