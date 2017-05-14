/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package tool;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import org.apache.commons.cli.*;
import model.Resource;
import model.ServerModel;
import model.Response.NormalResponse;
import model.command.*;

/**
 * This class is implemented to get command and arguments from the command line
 * of client and turn the command and arguments into JSON string which will be
 * sent to the server. And this class also check whether the context from the
 * command line is valid or not and get host and port from the client command
 * line.
 * 
 * @author Group - Alpha Panthers
 * @version 1.1
 */
public class ClientCommandLine {

	// resource arg 1name 2tags 3description 4uri 5channel 6owner 7ezserver
	// 8secret 9hostname 10port
	public static final int[] FETCH = { -1, -1, -1, 1, 0, -1, -1, -1, -1, -1 };
	public static final int[] EXCHANGE = { -1, -1, -1, -1, -1, -1, -1, -1, 1, 1 };
	public static final int[] REMOVE = { -1, -1, -1, 0, 0, 0, -1, -1, -1, -1 };
	public static final int[] QUERY = { 0, 0, 0, 0, 0, 0, 0, -1, -1, -1 };
	public static final int[] PUBLISH = { 0, 0, 0, 1, 0, 0, 0, -1, -1, 1, 1 };
	public static final int[] SHARE = { 0, 0, 0, 1, 0, 0, -1, 1, -1, -1 };
	public static final int[] SUBSCRIBE = { 0, 0, 0, 0, 0, 0, 0, -1, -1, -1 };
	public static final int RESOURCE_ARGS_NUM = 6;
	private static boolean valid = true;
	public static Option[] allcommandline;
	public static int errorSet = 0;

	/**
	 * The function of this method is to transmit command line arguments into
	 * JSON string if the context of the command line has invalid command or
	 * invalid arguments. The method will return null.
	 * 
	 * @return a JSON string
	 */
	public static String ClientCommandLine(String[] args) {
		String request = null;
		Resource resource = new Resource();
		Options opt = addOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(opt, args);
			Option[] commandline = commandLine.getOptions();
			checkError(commandline);
			// check error here
			if (commandline.length == 0) {
				ErrorMessage error = new ErrorMessage();
				NormalResponse response = new NormalResponse("error", error.GENERIC_INVALID);
				Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
			} else {
				allcommandline = commandline;
				String command = commandline[0].getOpt();
				switch (command) {
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
					request = cliShare(commandline);
					break;
				case "exchange":
					request = cliExchange(commandline);
					break;
				case "subscribe":
					request = cliSubscribe(commandline);
					break;
				default:
					ErrorMessage error = new ErrorMessage();
					NormalResponse response = new NormalResponse("error", error.GENERIC_INVALID);
					Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
					break;
				}
				if (valid == false) {
					request = null;
				}
				return request;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			ErrorMessage error = new ErrorMessage();
			NormalResponse response = new NormalResponse("error", error.GENERIC_INVALID);
			Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
			return request;
		}
		return request;

	}

	/**
	 * The function of this method is to add command line options
	 * 
	 * @return it will return the options that can be identified in command line
	 */
	public static Options addOptions() {
		Options opt = new Options();
		opt.addOption("channel", true, "channel");
		opt.addOption("name", true, "resource name");
		Option debug = Option.builder("debug").desc("print debug informaton").build();
		opt.addOption(debug);
		opt.addOption("subscribe", false, "ddf");
		Option description = Option.builder("description").hasArg().desc("rescource description").build();
		opt.addOption(description);
		Option exchange = Option.builder("exchange").desc("exchange server list with server").build();
		opt.addOption(exchange);
		Option fetch = Option.builder("fetch").desc("fetch resources from server").build();
		opt.addOption(fetch);
		Option host = Option.builder("host").hasArg().desc("server host, a domain name or IP address").build();
		opt.addOption(host);
		Option owner = Option.builder("owner").hasArg().desc("owner").build();
		opt.addOption(owner);
		Option port = Option.builder("port").hasArg().desc("server port, an integer").build();
		opt.addOption(port);
		Option publish = Option.builder("publish").desc("publish resource on server").build();
		opt.addOption(publish);
		Option query = Option.builder("query").desc("query for resource from server").build();
		opt.addOption(query);
		Option remove = Option.builder("remove").desc("remove resource from server").build();
		opt.addOption(remove);
		Option secret = Option.builder("secret").hasArg().desc("secret").build();
		opt.addOption(secret);
		Option share = Option.builder("share").desc("share resource on server").build();
		opt.addOption(share);
		Option servers = Option.builder("servers").hasArgs().desc("server list, name1:port1,name2:port2,...").build();
		opt.addOption(servers);
		Option tags = Option.builder("tags").hasArgs().desc("resource tags, tag1,tag2,tag3,...").build();
		opt.addOption(tags);
		Option uri = Option.builder("uri").hasArg().desc("resource URI").build();
		opt.addOption(uri);
		Option relay = Option.builder("relay").hasArg().desc("query relay").build();
		opt.addOption(relay);
		Option secure = Option.builder("secure").desc("attempt to make a secure connection").build();
		opt.addOption(secure);
		opt.addOption("h", "help", false, "help");
		return opt;
	}

	/**
	 * The function of this method is transmit publish command and its
	 * parameters into JSON string and check whether the parameters are invalid
	 * or not
	 * 
	 * @return a JSON string
	 */
	public static String cliPublish(Option[] commandline) {
		String request;
		Resource resource = new Resource();
		resource = cliResource(commandline, PUBLISH, "publish");
		Publish publish = new Publish("PUBLISH", resource);
		request = publish.toJSON();
		return request;
	}

	/**
	 * The function of this method is transmit remove command and its parameters
	 * into JSON string and check whether the parameters are invalid or not
	 * 
	 * @return a JSON string
	 */
	public static String cliRemove(Option[] commandline) {
		String request;
		Resource resource = new Resource();
		resource = cliResource(commandline, REMOVE, "remove");
		Remove remove = new Remove("REMOVE", resource);
		request = remove.toJSON();
		return request;
	}

	/**
	 * The function of this method is transmit query command and its parameters
	 * into JSON string and check whether the parameters are invalid or not
	 * 
	 * @return a JSON string
	 */
	public static String cliQuery(Option[] commandline) {
		String request;
		Resource resource = new Resource();
		resource = cliResource(commandline, QUERY, "query");
		Query query = new Query("QUERY", true, resource);
		request = query.toJSON();
		return request;
	}

	/**
	 * The function of this method is transmit fetch command and its parameters
	 * into JSON string and check whether the parameters are invalid or not
	 * 
	 * @return a JSON string
	 */
	public static String cliFetch(Option[] commandline) {
		String request;
		Resource resource = new Resource();
		resource = cliResource(commandline, FETCH, "fetch");
		Fetch fetch = new Fetch("FETCH", resource);
		request = fetch.toJSON();
		return request;
	}

	/**
	 * The function of this method is transmit share command and its parameters
	 * into JSON string and check whether the parameters are invalid or not
	 * 
	 * @return a JSON string
	 */
	public static String cliShare(Option[] commandline) {
		String request;
		String secret = null;
		boolean flag = false;
		Resource resource = new Resource();
		resource = cliResource(commandline, SHARE, "share");
		for (int i = 1; i < commandline.length; i++) {
			if (commandline[i].getOpt() != null && commandline[i].getOpt().equals("secret")) {
				secret = commandline[i].getValue();
				checkString(secret);
				flag = true;
			}
		}
		// missing secret
		if (!flag) {
			ErrorMessage error = new ErrorMessage();
			NormalResponse response = new NormalResponse("error", error.SHARE_MISSING);
			Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
			secret = "";
			valid = false;
		}
		Share share = new Share("SHARE", secret, resource);
		request = share.toJSON();
		return request;
	}

	/**
	 * The function of this method is transmit exchange command and its
	 * parameters into JSON string and check whether the parameters are invalid
	 * or not
	 * 
	 * @return a JSON string
	 */
	public static String cliExchange(Option[] commandline) {
		String request, hostName;
		int portNum;
		String servers = null;
		boolean flag = false;
		ArrayList<ServerModel> serverList = new ArrayList<ServerModel>();
		for (int i = 1; i < commandline.length; i++) {
			if (commandline[i].getOpt() != null && commandline[i].getOpt().equals("servers")) {
				servers = commandline[i].getValue();
				flag = true;
			}
		}
		// missing -servers
		if (!flag) {
			ErrorMessage error = new ErrorMessage();
			NormalResponse response = new NormalResponse("error", error.EXCHANGE_SERVERLIST_MISSING);
			Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
			valid = false;
		}
		String[] server = servers.split(",");
		for (int m = 0; m < server.length; m++) {
			String[] serverlist = server[m].split(":");
			hostName = serverlist[0];
			portNum = Integer.parseInt(serverlist[1]);
			ServerModel sm = new ServerModel(hostName, portNum);
			serverList.add(sm);
		}
		Exchange exchange = new Exchange("EXCHANGE", serverList);
		request = exchange.toJSON();
		return request;
	}
	
	/**
	 * 
	 * 
	 * @return a JSON string
	 */
	public static String cliSubscribe(Option[] commandline) {
		String request;
		Resource resource = new Resource();
		resource = cliResource(commandline, SUBSCRIBE, "subscribe");
		Subscribe subscribe = new Subscribe("SUBSCRIBE", true, resource);
		request = subscribe.toJSON();
		return request;
	}

	/**
	 * The function of this method is to get the resource parameters from the
	 * command line and check the parameters are valid or not and check whether
	 * the parameters are enough for the command.
	 * 
	 * @return a resource object
	 */
	public static Resource cliResource(Option[] commandline, int[] command, String commandName) {
		Resource resource = new Resource();
		int[] arg = new int[RESOURCE_ARGS_NUM];
		String resourceFeature, resourceValue;
		for (int i = 1; i < commandline.length; i++) {
			resourceFeature = commandline[i].getOpt();
			resourceValue = commandline[i].getValue();
			if (resourceValue != null) {
				switch (resourceFeature) {
				case "name":
					checkString(commandline[i].getValue());
					resource.setName(commandline[i].getValue());
					arg[0] = 1;
					break;
				case "tags":
					checkString(commandline[i].getValue());
					String tags = commandline[i].getValue();
					String[] tag = tags.split(",");
					resource.setTags(tag);
					arg[1] = 1;
					break;
				case "description":
					checkString(commandline[i].getValue());
					resource.setDescription(commandline[i].getValue());
					arg[2] = 1;
					break;
				case "uri":
					checkString(commandline[i].getValue());
					// checkUri(commandline[i].getValue());
					resource.setUri(commandline[i].getValue());
					arg[3] = 1;
					break;
				case "channel":
					checkString(commandline[i].getValue());
					resource.setChannel(commandline[i].getValue());
					arg[4] = 1;
					break;
				case "owner":
					checkString(commandline[i].getValue());
					checkOwner(commandline[i].getValue());
					resource.setOwner(commandline[i].getValue());
					arg[5] = 1;
					break;
				/*
				 * case "servers": checkStringArray(commandline[i].getValues());
				 * resource.setEZserver(commandline[i].getValues()); arg[6]=1;
				 * break;
				 */
				default:
					break;
				}
			}
		}
		checkArg(arg, command, commandName);
		return resource;
	}

	/**
	 * The function of this method is to get host name from the command line. If
	 * command line does not has the host name then it will return null
	 */
	public static String getHost() {
		for (int i = 0; i < allcommandline.length; i++) {
			if (allcommandline[i].getOpt() != null) {
				if (allcommandline[i].getOpt().equals("host")) {
					return allcommandline[i].getValue();
				}
			}
		}
		return null;
	}

	/**
	 * The function of this method is to get port number from the command line.
	 * If command line does not has the port number then it will return 0.
	 */
	public static int getPort() {
		for (int i = 0; i < allcommandline.length; i++) {
			if (allcommandline[i].getOpt() != null) {
				if (allcommandline[i].getOpt().equals("port")) {
					return Integer.parseInt(allcommandline[i].getValue());
				}
			}
		}
		return 0;
	}

	/**
	 * The function of this method is to check whether the command line has
	 * -debug
	 */

	public static boolean getDebug() {
		for (int i = 0; i < allcommandline.length; i++) {
			if (allcommandline[i].getOpt() != null && allcommandline[i].getOpt().equals("debug")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The method is to check whether there are enough parameters for the
	 * command
	 * 
	 * @param arg
	 *            the parameters array from the command line
	 * @param command
	 *            the parameters array that this command must havd
	 * @param commandName
	 *            the command name
	 */
	public static void checkArg(int[] arg, int[] command, String commandName) {
		ErrorMessage error = new ErrorMessage();
		NormalResponse response;
		for (int i = 0; i < arg.length; i++) {
			if (command[i] == 1 && arg[i] != command[i]) {
				switch (commandName) {
				case "publish":
				case "remove":
					response = new NormalResponse("error", error.PUBLISH_REMOVE_RESOURCE_MISSING);
					Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
					break;
				case "query":
				case "fetch":
				case "exchange":
					response = new NormalResponse("error", error.QUERY_FETCH_EXCHANGE_RESOURCETEMPLATE_MISSING);
					Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
					break;
				case "share":
					response = new NormalResponse("error", error.SHARE_MISSING);
					Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
					break;
				case "subscribe":
					break;
				default:
					break;
				}
				valid = false;
				break;
			}
		}
	}

	/** check the parameter contains whitespace or not */
	public static boolean checkString(String str) {
		if (str.contains("\0") || str.charAt(0) == ' ' || str.charAt(str.length() - 1) == ' ') {
			ErrorMessage error = new ErrorMessage();
			NormalResponse response = new NormalResponse("error", error.GENERIC_MISS_INCORRECT);
			Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
			valid = false;
			return true;
		} else {
			return false;
		}
	}

	/** check the owner is "*" or not */
	public static void checkOwner(String owner) {
		if (owner != null && owner.equals("*")) {
			ErrorMessage error = new ErrorMessage();
			NormalResponse response = new NormalResponse("error", error.GENERIC_MISS_INCORRECT);
			Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
			valid = false;
		}
	}

	/** check the uri is vaild or not */
	public static void checkUri(String uri) {
		try {
			URL uri1 = new URL(uri);
		} catch (MalformedURLException e) {
			ErrorMessage error = new ErrorMessage();
			NormalResponse response = new NormalResponse("error", error.GENERIC_MISS_INCORRECT);
			Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
			valid = false;
			// e.printStackTrace();
		}
	}

	/**
	 * The function of this method is to check whether the command line has
	 * -debug
	 */
	
	public static boolean checkError(Option[] commandline) {
		try {
			for (int i = 0; i < allcommandline.length; i++) {
				if (allcommandline[i].getOpt() != null && allcommandline[i].getOpt().equals("debug")) {
					return true;
				}
			}
		} catch (Exception e) {
			Log.debug = true;
			ErrorMessage error = new ErrorMessage();
			NormalResponse response = new NormalResponse("error", error.GENERIC_MISS_INCORRECT);
			Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
			Log.debug = false;
			errorSet = 1;
			System.exit(0);
		}
		return false;
	}
	
	public static void checkError(String[] args) {
		String request = null;
		Resource resource = new Resource();
		Options opt = addOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(opt, args);
			Option[] commandline = commandLine.getOptions();
			checkError(commandline);
		} catch (Exception e) {
			Log.debug = true;
			ErrorMessage error = new ErrorMessage();
			NormalResponse response = new NormalResponse("error", error.GENERIC_MISS_INCORRECT);
			Log.log(Common.getMethodName(), "FINE", "CHECK: " + response.toJSON());
			Log.debug = false;
			errorSet = 1;
			System.exit(0);
		}
	}

}
