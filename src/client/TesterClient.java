package client;
import java.io.*;
import java.net.*;
public class TesterClient {
	public static void main(String[] args){
		int serverPort = 3780;
		String serverHostname = "sunrise.cis.unimelb.edu.au";
		//TODO: 1. Check if parameters are valid
		//Commandline.isValid(args);
		//TODO:2.Translate cli to query
		//{"EXCHANGE","FETCH","SHARE","PUBLISH","QUERY","REMOVE"};
		String queryExample = "{\"command\": \"QUERY\",    \"relay\": true, \"resourceTemplate\": {\"name\": \"\",\"tags\": [],\"description\": \"\",\"uri\": \"\",\"channel\": \"\",\"owner\": \"\",\"ezserver\": null}}";
	    String publishExample = "{ \"command\" : \"PUBLISH\", \"resource\" : { \"name\" : \"Unimelb website 6667\", \"tags\" : [\"web\", \"html\"], \"description\" : \"The main page for the University of Melbourne\", \"uri\" : \"http://www.unimelb1.edu.au\", \"channel\" : \"\", \"owner\" : \"\", \"ezserver\" : null } }";
	    String shareExample = "{ \"command\": \"SHARE\", \"secret\": \"2os41f58vkd9e1q4ua6ov5emlv\", \"resource\": { \"name\": \"EZShare JAR\", \"tags\": [ \"jar\" ], \"description\": \"The jar file for EZShare. Use with caution.\", \"uri\":\"file:\\/\\/\\/\\/home\\/aaron\\/EZShare\\/ezshare.jar\", \"channel\": \"my_private_channel\", \"owner\": \"aaron010\", \"ezserver\": null } }";
	    String removeExample = "{ \"command\": \"REMOVE\", \"resource\": { \"name\": \"\", \"tags\": [], \"description\": \"\", \"uri\": \"http:\\/\\/www.unimelb1.edu.au\", \"channel\": \"\", \"owner\": \"\", \"ezserver\": null } }";
	    String exchangeExample = "{ \"command\": \"EXCHANGE\", \"serverList\": [ { \"hostname\": \"115.146.85.165\", \"port\": 3780 }, { \"hostname\": \"115.146.85.24\", \"port\": 3780 },{ \"hostname\": \"115.146.85.165\", \"port\": 3780 }, { \"hostname\": \"115.146.85.24\", \"port\": 3780 } ] }";	    
	    String fetchExample = "{\"command\": \"FETCH\",    \"relay\": true, \"resourceTemplate\": {\"name\": \"\",\"tags\": [],\"description\": \"\",\"uri\":\"file:\\/\\/\\/usr\\/local\\/share\\/ezshare\\/photo.jpg\",\"channel\": \"\",\"owner\": \"\",\"ezserver\": null}}";;
	    
	    String query = fetchExample;//Translated query
		doSend(serverHostname,serverPort,query);
	}
	
	public static void doSend(String hostname, int port, String query){
		try{
		    Socket socket = new Socket(hostname, port);  
		    //TODO: Output the log of connction
		    System.out.println("Connection Established");
		    DataInputStream in = new DataInputStream( socket.getInputStream());
		    DataOutputStream out =new DataOutputStream( socket.getOutputStream());
		    //4.Send the query to the server
		    out.writeUTF(query);
		    //TODO:5.Listen for the results and output to log. End the listening based on commands
		    boolean endFlag = false;
		    while(!endFlag){
		    	if(in.available() > 0) {
                    String message = in.readUTF();
                    //TODO: Output result
                    System.out.println(message);
                }
		    }
			//6.Close connection
		    socket.close();
		    in.close();
		    out.close();
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
	}

}
