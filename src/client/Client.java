package client;
import java.net.*;
import java.util.Arrays;
import java.io.*;

import model.Resource;
import model.command.Fetch;
import tool.ClientCommandLine;
import tool.Common;
import tool.Config;
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
	    // translate cli to query and if the commanline is unvalid will print out the error message 
	    // and send null to query
	    query = ClientCommandLine.ClientCommandLine(args);
		doSend(serverHostname,serverPort,query);
	}
	
	public static void doSend(String hostname, int port, String query){
		String op = Common.getOperationfromJson(query);
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
		    		if(op.equals("FETCH")){
		    			String message = in.readUTF();
	                    //TODO: Output result
	                    System.out.println(message);
	                    if(message.equals("{\"response\":\"success\"}")){
	                    	String resourceStr = in.readUTF();
		                    //TODO: Output result
		                    System.out.println(resourceStr);
		                    Resource resource = new Resource();
		                    resource.fromJSON(resourceStr);
			    			// The file location
							// Create a RandomAccessFile to read and write the output file.
							RandomAccessFile downloadingFile = new RandomAccessFile(resource.uri, "rw");
							
							// Find out how much size is remaining to get from the server.
							long fileSizeRemaining = (Long) resource.resourceSize;
							
							int chunkSize = setChunkSize(fileSizeRemaining);
							
							// Represents the receiving buffer
							byte[] receiveBuffer = new byte[chunkSize];
							
							// Variable used to read if there are remaining size left to read.
							int num;
							
							System.out.println("Downloading "+resource.uri+" of size "+fileSizeRemaining);
							while((num=in.read(receiveBuffer))>0){
								// Write the received bytes into the RandomAccessFile
								downloadingFile.write(Arrays.copyOf(receiveBuffer, num));
								
								// Reduce the file size left to read..
								fileSizeRemaining-=num;
								
								// Set the chunkSize again
								chunkSize = setChunkSize(fileSizeRemaining);
								receiveBuffer = new byte[chunkSize];
								
								// If you're done then break
								if(fileSizeRemaining==0){
									break;
								}
							}
							System.out.println("File received!");
							downloadingFile.close();
	                    }
	                    
		    			break;
		    		}
		    		String message = in.readUTF();
                    //TODO: Output result
                    System.out.println(message);
                    if(op.equals("QUERY")){
                    	//TODO: set {"resultSize":6} as end flag
                    }
                    else{
                    	//TODO: set response as end flag
                    }
		    			
                    // Move to if else
                    break;
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
	public static int setChunkSize(long fileSizeRemaining){
		// Determine the chunkSize
		int chunkSize=Config.TRUNK_SIZE;
		
		// If the file size remaining is less than the chunk size
		// then set the chunk size to be equal to the file size.
		if(fileSizeRemaining<chunkSize){
			chunkSize=(int) fileSizeRemaining;
		}
		
		return chunkSize;
	}
	
}
