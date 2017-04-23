package client;

import java.net.*;
import java.util.Arrays;
import java.io.*;

import model.Resource;
import model.Response.NormalResponse;
import model.command.Fetch;
import tool.ClientCommandLine;
import tool.Common;
import tool.Config;
import tool.Log;
import java.util.*;
/**
 * Created by Tim Luo on 2017/3/27.
 */
/*
-query -channel myprivatechannel -debug
-exchange -servers 115.146.85.165:3780,115.146.85.24:3780 -debug
-fetch -channel myprivatechannel -uri file:///home/aaron/EZShare/ezshare.jar -debug
-share -uri file:///home/aaron/EZShare/ezshare.jar -name "EZShare JAR" -description "The jar file for EZShare. Use with caution." -tags jar -channel myprivatechannel -owner aaron010 -secret 2os41f58vkd9e1q4ua6ov5emlv -debug
-publish -name "Unimelb website" -description "The main page for the University of Melbourne" -uri http://www.unimelb.edu.au -tags web,html -debug
-query
-remove -uri http://www.unimelb.edu.au

*
*
*/


public class Client {
	public static void main(String[] args) {
		int serverPort = 3780;
		String serverHostname = "sunrise.cis.unimelb.edu.au";
		// 1. Check if parameters are valid
		// 2.Translate cli to query
		// translate cli to query and if the commanline is unvalid will print
		// out the error message
		// and send null to query
		/*for(int i=0;i<args.length;i++){
			System.out.println(args[i]);
		}*/
		String query = ClientCommandLine.ClientCommandLine(args);
		if(ClientCommandLine.getDebug()){
			Log.debug = true;
		}
		if(ClientCommandLine.getPort()!=0){
			serverPort = ClientCommandLine.getPort();
		}
		if(ClientCommandLine.getHost()!=null){
			serverHostname = ClientCommandLine.getHost();
		}
		//System.out.println("Client Main Print:"+query);
		Log.log(Common.getMethodName(), "FINE", "SENDING: "+query);
		if (query != null) {
			doSend(serverHostname,serverPort,query,null);
		}
		else{
			//System.out.println("query==null");
			Log.log(Common.getMethodName(), "FINE", "Nothing to send to server.");
		}
	}

	public static boolean doSend(String hostname, int port, String query, ArrayList<String> resultArr) {
		String op = Common.getOperationfromJson(query);
		try {
			// 3.Create socket/input/output
			Socket socket = new Socket();
			if(op.equals("QUERY")){
				//Config.CONNECTION_TIMEOUT = Config.CONNECTION_TIMEOUT*5;
			}
			socket.connect(new InetSocketAddress(hostname, port), Config.CONNECTION_TIMEOUT);
			// This stops the request from dragging on after connection succeeds.
			socket.setSoTimeout(Config.CONNECTION_TIMEOUT);
			Log.log(Common.getMethodName(), "FINE", "Connection Established("+hostname+":"+port+")");
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			// 4.Send the query to the server
			out.writeUTF(query);
			// 5.Listen for the results and output to log. End the listening based on commands
			boolean endFlag = false;
			while (!endFlag) {
				if (in.available() > 0) {
					if (op.equals("FETCH")) {
						String message = in.readUTF();
						//Output result
						//System.out.println(message);
						Log.log(Common.getMethodName(), "FINE", "RECEIVED: "+message);
						NormalResponse nr = new NormalResponse();
						nr.fromJSON(message);
						if (nr.getResponse().equals("success")) {
							String resourceStr = in.readUTF();
							// Output result
							//System.out.println(resourceStr);
							Log.log(Common.getMethodName(), "FINE", "RECEIVED: "+resourceStr);
							if(resourceStr.equals("{\"resultSize\":0}")){
								break;
							}
							Resource resource = new Resource();
							resource.fromJSON(resourceStr);
							// The file location
							// Create a RandomAccessFile to read and write the
							// output file.
							File file = new File(resource.uri);
							String filename = file.getName();
							String pathString = "/ezdownload/";
							File path = new File(pathString);
							path.mkdirs();
							String absolutePath = path.getAbsolutePath()+"/"+filename;
							Log.log(Common.getMethodName(), "FINE", "Downloading to Destination: "+absolutePath);
							RandomAccessFile downloadingFile = new RandomAccessFile(
									pathString+filename, "rw");

							// Find out how much size is remaining to get from
							// the server.
							long fileSizeRemaining = (Long) resource.resourceSize;

							int chunkSize = setChunkSize(fileSizeRemaining);

							// Represents the receiving buffer
							byte[] receiveBuffer = new byte[chunkSize];

							// Variable used to read if there are remaining size
							// left to read.
							int num;

							//System.out.println("Downloading " + resource.uri+ " of size " + fileSizeRemaining);
							Log.log(Common.getMethodName(), "FINE","Downloading " + resource.uri+ " of size " + fileSizeRemaining);
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
							//System.out.println("File received!");
							Log.log(Common.getMethodName(), "FINE", "FILE RECEIVED.");
							downloadingFile.close();
						}

						break;
					}
					else if (op.equals("QUERY")) {
						String messageResponse = in.readUTF();
						Log.log(Common.getMethodName(), "FINE", "RECEIVED: "+messageResponse);
						NormalResponse nr = new NormalResponse();
						nr.fromJSON(messageResponse);
						if(nr.getResponse().equals("success")){
							while(true){
								String message = in.readUTF();
								// TODO: Output result
								//System.out.println(message);
								Log.log(Common.getMethodName(), "FINE", "RECEIVED: "+message);
								// TODO: set {"resultSize":6} as end flag
								if(message.contains("{\"resultSize\":")){
									break;
								}
								else{
									if(resultArr!=null){
										resultArr.add(message);
									}
								}
							}
						}
					} else {
						String message = in.readUTF();
						// Output result
						//System.out.println(message);
						Log.log(Common.getMethodName(), "FINE", "RECEIVED: "+message);
						// set response as end flag
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
			//ee.printStackTrace();
			Log.log(Common.getMethodName(), "FINE", "CONNECTION ERROR: Please check the network or server("+hostname+":"+port+"). Timeout or connection refused.");
			return false;
		}
		return true;
	}

	public static int setChunkSize(long fileSizeRemaining) {
		// Determine the chunkSize
		int chunkSize = Config.TRUNK_SIZE;

		// If the file size remaining is less than the chunk size
		// then set the chunk size to be equal to the file size.
		if (fileSizeRemaining < chunkSize) {
			chunkSize = (int) fileSizeRemaining;
		}

		return chunkSize;
	}

}
