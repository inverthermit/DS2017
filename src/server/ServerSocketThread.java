package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLSocket;

import model.ClientModel;
import model.ServerModel;
import tool.Common;
import tool.Config;
import tool.Log;

public class ServerSocketThread implements Runnable {
	private int port;
	private ServerModel selfModel;
	
	
	
	public ServerSocketThread(int port, ServerModel selfModel) {
		this.port = port;
		this.selfModel = selfModel;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.log(Common.getMethodName(), "INFO", "Server fail to start: Port already in used(" + port + ")");
			System.exit(0);
		}
	}
	

}
