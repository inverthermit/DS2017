package client;
import java.io.*;
import java.net.*;
import java.util.Arrays;

import model.Resource;
import model.Response.NormalResponse;
import tool.Common;
import tool.Config;
public class TesterClient {
	public static void main(String[] args){
		int serverPort = 10000;//3780;
		String serverHostname = "127.0.0.1";//"sunrise.cis.unimelb.edu.au";
		//TODO: 1. Check if parameters are valid
		//Commandline.isValid(args);
		//TODO:2.Translate cli to query
		//{"EXCHANGE","FETCH","SHARE","PUBLISH","QUERY","REMOVE"};
		String queryExample = "{\"command\": \"QUERY\",    \"relay\": true, \"resourceTemplate\": {\"name\": \"\",\"tags\": [],\"description\": \"\",\"uri\": \"\",\"channel\": \"\",\"owner\": \"\",\"ezserver\": null}}";
	    String publishExample = "{ \"command\": \"PUBLISH\", \"resource\" : { \"name\" : \"Unimelb website 6667\", \"tags\" : [\"web\", \"html\"], \"description\" : \"The main page for the University of Melbournee\", \"uri\" : \"http://www.unimelb1.edu.au\", \"channel\" : \"\", \"owner\" : \"\", \"ezserver\" : null } }";
	    String shareExample = "{ \"command\": \"SHARE\", \"secret\": \"2os41f58vkd9e1q4ua6ov5emlv\", \"resource\": { \"name\": \"EZShare JAR\", \"tags\": [ \"jar\" ], \"description\": \"The jar file for EZShare. Use with caution.\", \"uri\":\"file:\\/\\/\\/\\/home\\/aaron\\/EZShare\\/ezshare.jar\", \"channel\": \"my_private_channel\", \"owner\": \"aaron010\", \"ezserver\": null } }";
	    String removeExample = "{ \"command\": \"REMOVE\", \"resource\": { \"name\": \"aaa\", \"tags\": [], \"description\": \"\", \"uri\": \"http:\\/\\/www.unimelb1.edu.au\", \"channel\": \"\", \"owner\": \"\", \"ezserver\": null } }";
	    String exchangeExample = "{ \"command\": \"EXCHANGE\", \"serverList\": [ { \"hostname\": \"115.146.85.165\", \"port\": 3780 }, { \"hostname\": \"115.146.85.24\", \"port\": 3780 },{ \"hostname\": \"115.146.85.165\", \"port\": 3780 }, { \"hostname\": \"115.146.85.24\", \"port\": 3780 } ] }";	    
	    String fetchExample = "{\"command\": \"FETCH\",    \"relay\": true, \"resourceTemplate\": {\"name\": \"\",\"tags\": [],\"description\": \"\",\"uri\":\"D:/FrontEnd/pokerMemory.iso\",\"channel\": \"\",\"owner\": \"\",\"ezserver\": null}}";;
	    
	    String query = fetchExample;//Translated query
	    Client.doSend(serverHostname,serverPort,query);
	}

}
