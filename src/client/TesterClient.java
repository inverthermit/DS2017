package client;
import java.io.*;
import java.net.*;
import java.util.Arrays;

import model.Resource;
import tool.Common;
import tool.Config;
public class TesterClient {
	public static void main(String[] args){
		int serverPort = 3780;//3780;
		String serverHostname = "sunrise.cis.unimelb.edu.au";//"sunrise.cis.unimelb.edu.au";
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
	    
	    String query = removeExample;//Translated query
	    doSend(serverHostname,serverPort,query);
	}

	public static void doSend(String hostname, int port, String query) {
		String op = Common.getOperationfromJson(query);
		try {
			// 3.Create socket/input/output
			Socket socket = new Socket(hostname, port);
			// TODO: Output the log of connction
			System.out.println("Connection Established");
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			// 4.Send the query to the server
			out.writeUTF(query);
			// TODO:5.Listen for the results and output to log. End the
			// listening based on commands
			boolean endFlag = false;
			while (!endFlag) {
				if (in.available() > 0) {
					if(op == null){
						System.out.println("Error: No such operation");
						break;
					}
					if (op.equals("FETCH")) {
						String message = in.readUTF();
						// TODO: Output result
						System.out.println(message);
						if (message.equals("{\"response\":\"success\"}")) {
							String resourceStr = in.readUTF();
							// TODO: Output result
							System.out.println(resourceStr);
							if(resourceStr.equals("{\"resultSize\":0}")){
								break;
							}
							Resource resource = new Resource();
							resource.fromJSON(resourceStr);
							// The file location
							// Create a RandomAccessFile to read and write the
							// output file.
							RandomAccessFile downloadingFile = new RandomAccessFile(
									resource.uri, "rw");

							// Find out how much size is remaining to get from
							// the server.
							long fileSizeRemaining = (Long) resource.resourceSize;

							int chunkSize = setChunkSize(fileSizeRemaining);

							// Represents the receiving buffer
							byte[] receiveBuffer = new byte[chunkSize];

							// Variable used to read if there are remaining size
							// left to read.
							int num;

							System.out.println("Downloading " + resource.uri
									+ " of size " + fileSizeRemaining);
							while ((num = in.read(receiveBuffer)) > 0) {
								// Write the received bytes into the
								// RandomAccessFile
								downloadingFile.write(Arrays.copyOf(
										receiveBuffer, num));

								// Reduce the file size left to read..
								fileSizeRemaining -= num;

								// Set the chunkSize again
								chunkSize = setChunkSize(fileSizeRemaining);
								receiveBuffer = new byte[chunkSize];

								// If you're done then break
								if (fileSizeRemaining == 0) {
									break;
								}
							}
							System.out.println("File received!");
							downloadingFile.close();
						}

						break;
					}
					else if (op.equals("QUERY")) {
						while(true){
							String message = in.readUTF();
							// TODO: Output result
							System.out.println(message);
							// TODO: set {"resultSize":6} as end flag
							if(message.contains("{\"resultSize\":")){
								break;
							}
						}
					} else {
						String message = in.readUTF();
						// TODO: Output result
						System.out.println(message);
						// TODO: set response as end flag
						if(message.contains("{\"response\":\"")){
							break;
						}
					}
					// Move to if else
					break;
				}
			}
			// 6.Close connection
			socket.close();
			in.close();
			out.close();
		} catch (Exception ee) {
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
