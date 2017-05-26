/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package server;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

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
		Log.infoDebug = true;
		if (sm == null) {
			return;
		} else {
			selfModel = sm;
		}
		if (selfModel.port == 0) {
			selfModel.port = Config.DEFAULT_PORT;
//			selfModel.port = 10001;
		}
		if (selfModel.sport == 0) {
			selfModel.sport = Config.DEFAULT_SECURE_PORT;
//			selfModel.port = 3781;
		}
		if (selfModel.advertisedHostName == null) {
			selfModel.advertisedHostName = Config.DEFAULT_ADVERTISED_HOSTNAME;
		}
		if (selfModel.secret == null) {
			selfModel.secret = Common.SECRET;
		}
		if (selfModel.hostname == null) {
			selfModel.hostname = Config.DEFAULT_HOST;
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
		int sport = selfModel.sport;
		// System.out.println(selfModel.hostname+":"+selfModel.port+"
		// "+selfModel.exchangeInterval+" "+selfModel.intervallimit+"
		// "+selfModel.secret);
		Log.infoDebug = true;
		Log.log(Common.getMethodName(), "INFO", "Starting the EZShare Server");
		Log.log(Common.getMethodName(), "INFO", "using advertised hostname: " + selfModel.advertisedHostName);
		Log.log(Common.getMethodName(), "INFO", "using secret: " + selfModel.secret);
		Log.log(Common.getMethodName(), "INFO", "bound to port " + port);
		Log.log(Common.getMethodName(), "INFO", "bound to sport " + sport);
		Log.infoDebug = false;
		ServerSocketThread unsecure=new ServerSocketThread(port,selfModel);
		ServerSocketSSLThread secure=new ServerSocketSSLThread(sport,selfModel);
	    Thread t1 = new Thread(unsecure);
	    Thread t2 = new Thread(secure);
	    t1.start();
	    t2.start();
	}
}