/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package server;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;

import tool.Common;
import tool.Config;
import tool.Log;
import tool.ServerCommandLine;
import model.ClientModel;
import model.ServerModel;

/*
-advertisedhostname server1 -connectionintervallimit 0 -exchangeinterval 20 -port 10000 -secret asdfwefwasdf -debug
*/
/**
 * This class is the server main class which basically runs the server. It deals
 * with client connection, request and response. It also controls the heartbeat
 * process.
 * 
 * @author Group - Alpha Panthers
 * @version 1.1
 */
public class Server {
	private static ServerModel selfModel = new ServerModel();

	/**
	 * This method runs the server according to different arguments. It does the
	 * following jobss 1.Check if parameters are valid 2.Create a heartbeat
	 * thread 3.Accept connections from clients 4.Dispatch clients to different
	 * server threads
	 * 
	 * @param args
	 *            commandline arguments
	 */
	public static void main(String[] args) {
		ServerModel sm = ServerCommandLine.ServerCommandLine(args);
		if (sm == null) {
			return;
		} else {
			selfModel = sm;
		}
		if (selfModel.port == 0) {
//			selfModel.port = Config.DEFAULT_PORT;
			selfModel.port = 10001;
		}
		if (selfModel.advertisedHostName == null) {
			selfModel.advertisedHostName = Config.DEFAULT_ADVERTISED_HOSTNAME;
		}
		if (selfModel.secret == null) {
			selfModel.secret = Common.SECRET;
		}
		if (selfModel.hostName == null) {
			selfModel.hostName = Config.DEFAULT_HOST;
		}
		if (selfModel.exchangeInterval != null) {
			// to int catch exception
			try {
				int interval = Integer.parseInt(selfModel.exchangeInterval);
				Config.HEARTBEAT_INTERVAL = interval * 1000;
			} catch (Exception ee) {
				// System.out.println("exchange interval is not an int");
				Log.log(Common.getMethodName(), "INFO", "Exchange interval is not an int");
				return;
			}
		}
		if (selfModel.intervalLimit != null) {
			// to int catch exception
			try {
				int interval = Integer.parseInt(selfModel.intervalLimit);
				Config.CONNECTION_LIMIT_INTERVAL = interval;
			} catch (Exception ee) {
				// System.out.println("connection interval is not an int");
				Log.log(Common.getMethodName(), "INFO", "Connection interval is not an int");
				return;
			}
		}
		selfModel.init();
		int port = selfModel.port;
		// System.out.println(selfModel.hostname+":"+selfModel.port+"
		// "+selfModel.exchangeInterval+" "+selfModel.intervallimit+"
		// "+selfModel.secret);
		Log.log(Common.getMethodName(), "INFO", "Starting the EZShare Server");
		Log.log(Common.getMethodName(), "INFO", "using advertised hostname: " + selfModel.advertisedHostName);
		Log.log(Common.getMethodName(), "INFO", "using secret: " + selfModel.secret);
		
//		ServerModel sm1 = new ServerModel("127.0.0.1", 10001);
//		selfModel.serverList.add(sm1);
		
		ExecutorService pool = Executors.newCachedThreadPool();
		pool.execute(new HeartbeatThread(selfModel));
		try {
			Log.log(Common.getMethodName(), "INFO", "bound to port " + port);
			Log.log(Common.getMethodName(), "INFO", "started");
			ServerSocket server = new ServerSocket(port);
			boolean bool = true;
			while (bool) {
				ClientModel client = new ClientModel();
				// Log.log(Common.getMethodName(), "INFO", "started");
				client.socket = server.accept();
				client.ip = client.socket.getRemoteSocketAddress().toString().split(":")[0];
				long timestamp = Common.getCurrentSecTimestamp();
				if (selfModel.ipTimeList == null) {
					selfModel.ipTimeList = new HashMap<String, Long>();
				} else if (timestamp - selfModel.getLastClientTime(client.ip) < Config.CONNECTION_LIMIT_INTERVAL) {
					System.out.println("Less than connection limit interval");
					client.socket.close();
					continue;
				}
				selfModel.ipTimeList.put(client.ip, timestamp);
				// client.timestamp = timestamp;

				// Timeout
				client.socket.setSoTimeout(Config.CONNECTION_TIMEOUT);
				// System.out.println(client.socket.getSoTimeout());
				selfModel.clientList.add(client);
				// TODO: Output client connection log
				// System.out.println("Connected");
				Log.log(Common.getMethodName(), "INFO",
						"New Connection:" + client.socket.getRemoteSocketAddress().toString().split(":")[0] + ":"
								+ client.socket.getPort());
				pool.execute(new ServerThread(client, selfModel));
			}
			server.close();
		} catch (Exception ee) {
			// ee.printStackTrace();
			Log.log(Common.getMethodName(), "INFO", "Server fail to start: Port already in used(" + port + ")");
			System.exit(0);
		}
	}
}
