/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package client;

import java.net.*;
import java.util.Arrays;
import java.io.*;
//import com.sun.net.ssl.internal.ssl.Provider;
import model.Resource;
import model.Response.NormalResponse;
import model.Response.SubscribeResponse;
import model.command.Unsubscribe;
import server.ServerSocketSSLThread;
import tool.ClientCommandLine;
import tool.Common;
import tool.Config;
import tool.ErrorMessage;
import tool.Keystore;
import tool.Log;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.json.JSONObject;

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

/**
 * This class is the client main class which basically runs the client. It deals
 * with server connection, request and response.
 * 
 * @author Group - Alpha Panthers
 * @version 1.1
 */
public class Client {
	/**
	 * This method reads and parse the command line into json string.
	 * 
	 * @param args
	 *            CLI parameters
	 */
	public static void main(String[] args) {
		int serverPort = Config.DEFAULT_PORT;
		String serverHostname = "127.0.0.1";
		// 1. Check if parameters are valid
		// 2.Translate cli to query
		// translate cli to query and if the commanline is unvalid will print
		// out the error message
		// and send null to query
		/*
		 * for(int i=0;i<args.length;i++){ System.out.println(args[i]); }
		 */
		ClientCommandLine.checkError(args);
		String query = ClientCommandLine.ClientCommandLine(args);
		if (ClientCommandLine.getDebug()) {
			Log.debug = true;
			Log.log(Common.getMethodName(), "INFO", "setting debug on");
		}
		if (ClientCommandLine.errorSet == 0) {
			query = ClientCommandLine.ClientCommandLine(args);
			if (ClientCommandLine.getPort() != 0) {
				serverPort = ClientCommandLine.getPort();
			}
			if (ClientCommandLine.getHost() != null) {
				serverHostname = ClientCommandLine.getHost();
			}
			// System.out.println("Client Main Print:"+query);

			if (query != null) {
				if (ClientCommandLine.getSecure()) {
					serverPort = Config.DEFAULT_SECURE_PORT;
					if (ClientCommandLine.getPort() != 0) {
						serverPort = ClientCommandLine.getPort();
					}
					doSendSecure(serverHostname, serverPort, query, null, Log.debug);
				} else {
					doSend(serverHostname, serverPort, query, null, Log.debug);
				}

			} else {
				// System.out.println("query==null");
				Log.log(Common.getMethodName(), "FINE", "Not connecting to server. Please check your command.");
			}
		}
	}

	public static boolean doSend(String hostname, int port, String query, ArrayList<String> resultArr,
			boolean printLog) {
		String op = Common.getOperationfromJson(query);
		if (op == null) {
			NormalResponse nr = new NormalResponse("error", ErrorMessage.GENERIC_INVALID);
			Log.log(Common.getMethodName(), "FINE", "CHECK:" + nr.toJSON());
			return false;
		}
		try {
			Socket socket = new Socket();
			if (op.equals("QUERY")) {
				// Config.CONNECTION_TIMEOUT = Config.CONNECTION_TIMEOUT*5;
			}
			socket.connect(new InetSocketAddress(hostname, port), Config.CONNECTION_TIMEOUT);
			// This stops the request from dragging on after connection
			// succeeds.
			// socket.setSoTimeout(Config.CONNECTION_TIMEOUT);
			Log.log(Common.getMethodName(), "FINE", op.toLowerCase() + "ing to " + hostname + ":" + port);
			Log.log(Common.getMethodName(), "FINE", "SENT: " + query);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			// 4.Send the query to the server
			out.writeUTF(query);
			doIN(in, op, printLog, resultArr, out, port, hostname);
			in.close();
			out.flush();
			out.close();
			socket.close();
		} catch (Exception ee) {
			Log.log(Common.getMethodName(), "FINE", "CONNECTION ERROR: Please check the network or server(" + hostname
					+ ":" + port + "). Timeout or connection refused.");
			return false;
		}
		return true;
	}

	public static boolean doSendSecure(String hostname, int port, String query, ArrayList<String> resultArr,
			boolean printLog) {
		String op = Common.getOperationfromJson(query);
		if (op == null) {
			NormalResponse nr = new NormalResponse("error", ErrorMessage.GENERIC_INVALID);
			Log.log(Common.getMethodName(), "FINE", "CHECK:" + nr.toJSON());
			return false;
		}
		try {
			// create ssl socket

			InputStream keystoreInput = Client.class.getResourceAsStream("/key/server.jks");// serverKeystore/server.jks
			InputStream truststoreInput = Client.class.getResourceAsStream("/key/client.jks");/// xty/clientKeystore/client.jks

			Keystore.setSSLFactories(keystoreInput, "comp90015", truststoreInput);

			//System.out.println("start to connecting the server");
			System.setProperty("javax.net.ssl.trustStore", "xty/clientKeyStore/client.jks");
			//System.out.println("starting to certification");
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(hostname, port);

			Log.log(Common.getMethodName(), "FINE", op.toLowerCase() + "ing to " + hostname + ":" + port);
			Log.log(Common.getMethodName(), "FINE", "SENT: " + query);

			// read from the server
			InputStream inputstream = sslsocket.getInputStream();
			InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
			BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
			// write to the server
			OutputStream outputstream = sslsocket.getOutputStream();
			OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
			BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);
			//System.out.println("ready to send sth to the server");
			bufferedwriter.write(query + '\n');
			bufferedwriter.flush();

			// DataInputStream in = new
			// DataInputStream(sslsocket.getInputStream());
			// DataOutputStream out = new
			// DataOutputStream(sslsocket.getOutputStream());

			// out.writeUTF(query);
			// out.flush();
			//System.out.println("doing response from server");
			doInSecure(bufferedreader, op, printLog, resultArr, bufferedwriter, port, hostname);
			// in.close();

			// out.close();
			inputstream.close();
			inputstreamreader.close();
			outputstream.close();
			outputstreamwriter.close();
			bufferedreader.close();
			bufferedwriter.close();
			sslsocket.close();

		} catch (Exception ee) {
			Log.log(Common.getMethodName(), "FINE", "CONNECTION ERROR: Please check the network or server(" + hostname
					+ ":" + port + "). Timeout or connection refused.");
			//ee.printStackTrace();
			return false;
		}
		return true;
	}



	public static boolean doIN(DataInputStream in, String op, boolean printLog, ArrayList<String> resultArr,
			DataOutputStream out, int port, String hostname) {
		long start = Common.getCurrentSecTimestamp();
		// 5.Listen for the results and output to log. End the listening
		// based on commands
		boolean endFlag = false;
		try {
			while (!endFlag) {
				if (Common.getCurrentSecTimestamp() - start >= Config.CONNECTION_TIMEOUT / 1000) {
					throw new Exception();
				}
				if (in.available() > 0) {
					if (op.equals("FETCH")) {
						String message = in.readUTF();
						// Output result
						// System.out.println(message);
						Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + message);
						NormalResponse nr = new NormalResponse();
						nr.fromJSON(message);
						if (nr.getResponse().equals("success")) {
							String resourceStr = in.readUTF();
							Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + resourceStr);
							if (resourceStr.equals("{\"resultSize\":0}")) {
								break;
							}
							resourceStr = in.readUTF();
							Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + resourceStr);
							Resource resource = new Resource();
							resource.fromJSON(resourceStr);
							// The file location
							// Create a RandomAccessFile to read and write the
							// output file.
							File file = new File(resource.uri);
							String filename = file.getName();
							String pathString = "./ezdownload/";
							File path = new File(pathString);
							path.mkdirs();
							String absolutePath = path.getAbsolutePath() + "/" + filename;
							// File cFile = new File(absolutePath);
							// cFile.createNewFile();
							Log.log(Common.getMethodName(), "FINE", "Downloading to Destination: " + absolutePath);
							RandomAccessFile downloadingFile = new RandomAccessFile(pathString + filename, "rw");

							// Find out how much size is remaining to get from
							// the server.
							long fileSizeRemaining = (Long) resource.resourceSize;

							int chunkSize = setChunkSize(fileSizeRemaining);

							// Represents the receiving buffer
							byte[] receiveBuffer = new byte[chunkSize];

							// Variable used to read if there are remaining size
							// left to read.
							int num;

							// System.out.println("Downloading " + resource.uri+
							// " of size " + fileSizeRemaining);
							Log.log(Common.getMethodName(), "FINE",
									"Downloading " + resource.uri + " of size " + fileSizeRemaining);
							while ((num = in.read(receiveBuffer)) > 0) {
								// Write the received bytes into the
								// RandomAccessFile
								downloadingFile.write(Arrays.copyOf(receiveBuffer, num));

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
							// System.out.println("File received!");
							Log.log(Common.getMethodName(), "FINE", "FILE RECEIVED.");
							downloadingFile.close();
						}

						break;
					} else if (op.equals("QUERY")) {
						String messageResponse = in.readUTF();
						if (printLog)
							Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + messageResponse);
						NormalResponse nr = new NormalResponse();
						nr.fromJSON(messageResponse);
						if (nr.getResponse().equals("success")) {
							while (true) {
								String message = in.readUTF();
								// TODO: Output result
								// System.out.println(message);
								if (printLog)
									Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + message);
								// TODO: set {"resultSize":6} as end flag
								if (message.contains("{\"resultSize\":")) {
									break;
								} else {
									if (resultArr != null) {
										resultArr.add(message);
									}
								}
							}
						}
					} else if (op.equals("SUBSCRIBE")) {
						String messageResponse = in.readUTF();
						Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + messageResponse);
						SubscribeResponse sr = new SubscribeResponse();
						sr.fromJSON(messageResponse);
						if (sr.getResponse().equals("success")) {
							// listen unsubscribe event
							ExecutorService pool = Executors.newCachedThreadPool();
							pool.execute(new Runnable() {
								@Override
								public void run() {
									BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
									try {
										while (true) {
											char input = (char) br.read();
											if (input == '\n' || input == '\r') {
												Unsubscribe unsubscribe = new Unsubscribe("UNSUBSCRIBE", sr.getId());
												out.writeUTF(unsubscribe.toJSON());
												pool.shutdown();
												break;
											}
										}
										br.close();
									} catch (IOException e) {
										//e.printStackTrace();
										System.exit(0);
									}
								}
							});

							while (true) {
								String message = in.readUTF();
								Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + message);
								if (message.contains("{\"resultSize\":")) {
									break;
								} else {
									if (resultArr != null) {
										resultArr.add(message);
									}
								}
							}
						}
					} else {
						String message = in.readUTF();
						// Output result
						// System.out.println(message);
						Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + message);
						// set response as end flag
						if (message.contains("{\"response\":\"")) {
							break;
						}
					}
					// Move to if else
					break;
				}
			}
		} catch (Exception ee) {
			// ee.printStackTrace();
			Log.log(Common.getMethodName(), "FINE", "CONNECTION ERROR: Please check the network or server(" + hostname
					+ ":" + port + "). Timeout or connection refused.");
			return false;
		}
		return true;
	}

	public static boolean doInSecure(BufferedReader in, String op, boolean printLog, ArrayList<String> resultArr,
			BufferedWriter out, int port, String hostname) {
		long start = Common.getCurrentSecTimestamp();
		// 5.Listen for the results and output to log. End the listening
		// based on commands
		boolean endFlag = false;
		try {
			while (!endFlag) {
				if (Common.getCurrentSecTimestamp() - start >= Config.CONNECTION_TIMEOUT / 1000) {
					throw new Exception();
				}
				String message;
				//System.out.println("getting response");
				if ((message = in.readLine()) != null) {
					//System.out.println(message);
					if (op.equals("FETCH")) {
						// Output result
						// System.out.println(message);
						Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + message);
						NormalResponse nr = new NormalResponse();
						nr.fromJSON(message);
						if (nr.getResponse().equals("success")) {
							String resourceStr = in.readLine();
							// Output result
							// System.out.println(resourceStr);
							Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + resourceStr);
							if (resourceStr.equals("{\"resultSize\":0}")) {
								break;
							}
							resourceStr = in.readLine();
							Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + resourceStr);

							Resource resource = new Resource();
							resource.fromJSON(resourceStr);
							// The file location
							// Create a RandomAccessFile to read and write the
							// output file.
							File file = new File(resource.uri);
							String filename = file.getName();
							String pathString = "./ezdownload/";
							File path = new File(pathString);
							path.mkdirs();
							String absolutePath = path.getAbsolutePath() + "/" + filename;
							// File cFile = new File(absolutePath);
							// cFile.createNewFile();
							Log.log(Common.getMethodName(), "FINE", "Downloading to Destination: " + absolutePath);
							RandomAccessFile downloadingFile = new RandomAccessFile(pathString + filename, "rw");

							// Find out how much size is remaining to get from
							// the server.
							long fileSizeRemaining = (Long) resource.resourceSize;

							int chunkSize = setChunkSize(fileSizeRemaining);

							// Represents the receiving buffer
							byte[] receiveBuffer = new byte[chunkSize];

							// Variable used to read if there are remaining size
							// left to read.
							int num = 0;

							// System.out.println("Downloading " + resource.uri+
							// " of size " + fileSizeRemaining);
							Log.log(Common.getMethodName(), "FINE",
									"Downloading " + resource.uri + " of size " + fileSizeRemaining);
							while (1 > 0) {
								// while ((num = in.readLine()) > 0) {
								// Write the received bytes into the
								// RandomAccessFile
								downloadingFile.write(Arrays.copyOf(receiveBuffer, num));

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
							// System.out.println("File received!");
							Log.log(Common.getMethodName(), "FINE", "FILE RECEIVED.");
							downloadingFile.close();
						}

						break;
					} else if (op.equals("QUERY")) {
						if (printLog)
							Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + message);
						NormalResponse nr = new NormalResponse();
						nr.fromJSON(message);
						if (nr.getResponse().equals("success")) {
							while (true) {
								String messageResponse = in.readLine();
								// TODO: Output result
								// System.out.println(message);
								if (printLog)
									Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + messageResponse);
								// TODO: set {"resultSize":6} as end flag
								if (messageResponse.contains("{\"resultSize\":")) {
								//	System.out.println("end");
									break;
								} else {
									if (resultArr != null) {
										resultArr.add(messageResponse);
									}
									}
								}
							}
						} else if (op.equals("SUBSCRIBE")) {
							// String messageResponse = in.readUTF();
							Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + message);
							SubscribeResponse sr = new SubscribeResponse();
							sr.fromJSON(message);
							if (sr.getResponse().equals("success")) {
								// listen unsubscribe event
								ExecutorService pool = Executors.newCachedThreadPool();
								pool.execute(new Runnable() {
									@Override
									public void run() {
										BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
										try {
											while (true) {
												char input = (char) br.read();
												if (input == '\n' || input == '\r') {
													Unsubscribe unsubscribe = new Unsubscribe("UNSUBSCRIBE",
															sr.getId());
													out.write(unsubscribe.toJSON()+"\n");
													out.flush();
													pool.shutdown();
													break;
												}
											}
											br.close();
										} catch (IOException e) {
											//e.printStackTrace();
											System.exit(0);
										}
									}
								});

								while (true) {
									String messageResponse = in.readLine();
									Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + messageResponse);
									if (messageResponse.contains("{\"resultSize\":")) {
										break;
									} else {
										if (resultArr != null) {
											resultArr.add(messageResponse);
										}
									}
								}
							}
						} else {
							// String message = in.readUTF();
							// Output result
							// System.out.println(message);
							Log.log(Common.getMethodName(), "FINE", "RECEIVED: " + message);
							// set response as end flag
							if (message.contains("{\"response\":\"")) {
								break;
							}
						}
						// Move to if else
						break;
					}
				}
			
		} catch (Exception ee) {
			// ee.printStackTrace();
			Log.log(Common.getMethodName(), "FINE", "CONNECTION ERROR: Please check the network or server(" + hostname
					+ ":" + port + "). Timeout or connection refused.");
			return false;
		}
		return true;
	}

	/**
	 * This method sets the size of current chunk based on remaining file size.
	 * 
	 * @param fileSizeRemaining
	 *            Remaining file size
	 * @return Length of current chunk size
	 */
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
