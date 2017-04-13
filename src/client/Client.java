package client;
import java.net.*;
import java.io.*;
/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class Client {
	public static void main(String[] args){
		int serverPort = 3780;
		String serverHostname = "sunrise.cis.unimelb.edu.au";
		//TODO: 1. Check if parameters are valid
		//Commandline.isValid(args);
		//TODO:2.Translate cli to query
	    String query = "";//Translated query
		doSend(serverHostname,serverPort,query);
	}
	
	public static void doSend(String hostname, int port, String query){
		try{
			//3.Create socket/input/output
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
