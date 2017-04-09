package client;
import java.net.*;
import java.io.*;
/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class Client {
	public static void main(String[] args){
		//TODO: 1. Check if parameters are valid
		//Commandline.isValid(args);
		try{
			//2.Create socket/input/output
			int serverPort = 3780;
		    Socket socket = new Socket("sunrise.cis.unimelb.edu.au", serverPort);  
		    //TODO: Output the log of connction
		    System.out.println("Connection Established");
		    DataInputStream in = new DataInputStream( socket.getInputStream());
		    DataOutputStream out =new DataOutputStream( socket.getOutputStream());
			//TODO:3.Translate cli to query
		    String query = "";//Translated query
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
			ee.printStackTrace();;
		}
	}
	
}