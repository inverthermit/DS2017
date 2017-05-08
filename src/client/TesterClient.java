/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package client;

import tool.Log;

/**
 * This class is for testing client.
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class TesterClient {
	public static void main(String[] args){
		int serverPort = 10000;//3780;
		String serverHostname = "127.0.0.1";//"sunrise.cis.unimelb.edu.au";
		//TODO: 1. Check if parameters are valid
		//Commandline.isValid(args);
		//TODO:2.Translate cli to query
		//{"EXCHANGE","FETCH","SHARE","PUBLISH","QUERY","REMOVE"};
		String queryExample = "{\"command\": \"QUERY\",    \"reply\": true}";
	    String publishExample = "{ \"command\": \"PUBLISH\", \"resource\" : {  \"tags\" : [\"web\", \"html\"], \"description\" : \"The main page for the University of Melbournee\", \"uri\" : \"www.baidu.com\" , \"channel\" : \"\", \"owner\" : \"\", \"ezserver\" : null } }";
	    String shareExample = "{ \"command\": \"SHARE\",  \"resource\": { \"name\": \"EZShare JAR\", \"tags\": [ \"jar\" ], \"description\": \"The jar file for EZShare. Use with caution.\", \"uri\":\"d://temp.txt\", \"channel\": \"\", \"owner\": \"aaron010\", \"ezserver\": null } }";
	    String removeExample = "{ \"command\": \"REMOVE\", \"resource\": { \"name\": \"\", \"tags\": [], \"description\": \"\", \"uri\": \"http:\\/\\/www.unimelb1.edu.au\", \"channel\": \"\", \"owner\": \"\", \"ezserver\": null } }";
	    String exchangeExample = "{ \"command\": \"EXCHANGE\", \"serverList\": [ { \"hostname\": \"115.146.85.165\", \"port\": 3780 }, { \"hostname\": \"115.146.85.24\", \"port\": 3780 },{ \"hostname\": \"115.146.85.165\", \"port\": 3780 }, { \"hostname\": \"115.146.85.24\", \"port\": 3780 } ] }";	    
	    String fetchExample = "{\"command\": \"FETCH\",    \"reply\": true, \"resourceTemplate\": {\"name\": \"aEZShare JAR\",\"tags\": [],\"description\": \"\",\"uri\":\"d://temp.txt\",\"channel\": \"\",\"owner\": \"\",\"ezserver\": null}}";;
	    String SubscribeExample = "{ \"command\": \"SUBSCRIBE\", \"relay\": true, \"resource\" : {  \"tags\" : [\"web\", \"html\"], \"description\" : \"The main page for the University of Melbournee\", \"uri\" : \"www.baidu.com\" , \"channel\" : \"\", \"owner\" : \"\", \"ezserver\" : null } }";
	    Log.debug =true;
	    String query = SubscribeExample;//Translated query
	    Client.doSend(serverHostname,serverPort,query,null,Log.debug);
	}

}
