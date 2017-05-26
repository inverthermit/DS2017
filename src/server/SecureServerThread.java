/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package server;

import java.net.*;
import java.io.*;
import java.util.*;

import javax.net.ssl.SSLSocket;

import tool.Common;
import tool.Config;
import tool.Log;
import model.ClientModel;
import model.ServerModel;

/**
 * This class is a thread class doing "heartbeat" check with other servers in
 * serverList. It implements Runnable interface.
 * 
 * @author Group - Alpha Panthers
 * @version 1.1
 */
public class SecureServerThread implements Runnable {
	private SSLSocket client;
	private ClientModel clientModel;
	private InputStream input;
	private InputStreamReader inReader;
	private BufferedReader in;
	private OutputStream output;
	private OutputStreamWriter outWriter;
	private BufferedWriter out;
	private ServerModel selfModel;

	/**
	 * This method is a constructor which initialize client socket, clientModel,
	 * data input stream, output stream and serverModel.
	 * 
	 */
	public SecureServerThread(ClientModel clientModel, ServerModel selfModel) {
		this.clientModel = clientModel;
		this.client = clientModel.sslsocket;
		this.selfModel = selfModel;
		try {
			this.input = client.getInputStream();
			this.inReader = new InputStreamReader(this.input);
			this.in = new BufferedReader(inReader);
			this.output = client.getOutputStream();
			this.outWriter = new OutputStreamWriter(this.output);
			this.out = new BufferedWriter(outWriter);
			
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	/**
	 * This method handles the socket of a client. It does the following jobs:
	 * 1.Get message from client 2.Parse message, do operations 3.Send results
	 * back to client 4.Close the socket and quit the thread and leave it to be
	 * dealt by the threadpool
	 * 
	 */
	@Override
	public void run() {
		try {
			while (true) {
				String message;
				if ((message = in.readLine())!=null) {
					// 1.Get message
					String option = Common.getOperationfromJson(message);
					boolean persistentConnFlag = option == "SUBSCRIBE";
					boolean unsubscribe = option == "UNSUBSCRIBE";
					// Print out log
					// System.out.println(message);
					Log.log(Common.getMethodName(), "FINE", "RECEIVED: "
							+ message);
					// 2.Parse message, do operations, return ArrayList<String>
					// to send back to client
					OperationSecure sop = new OperationSecure();
					ArrayList<String> resultSet = sop.dispatcher(message,
							selfModel, clientModel);
					if (resultSet == null) {
						// Print log
						// System.out.print("Nothing to output...");
						return;
					}
					// 3.Send results back to client
					for (int i = 0; i < resultSet.size(); i++) {
						out.write(resultSet.get(i)+'\n');
						out.flush();
						Log.log(Common.getMethodName(), "FINE", "SENDING: "
								+ resultSet.get(i));
					}
					if (!persistentConnFlag || unsubscribe) {
						break;
					}
				}
			}
			// 4.Close the socket and quit the thread and leave it to be dealt
			// by the threadpool
			//in.close();
			//out.close();
			//client.close();
		} catch (Exception ee) {
			//ee.printStackTrace();
		}

	}

}
