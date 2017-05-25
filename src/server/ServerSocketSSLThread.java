package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import model.ClientModel;
import model.ServerModel;
import tool.Common;
import tool.Config;
import tool.Keystore;
import tool.Log;

public class ServerSocketSSLThread implements Runnable {
	private int sport;
	private ServerModel selfModel;
	
	
	
	public ServerSocketSSLThread(int sport, ServerModel selfModel) {
		this.sport = sport;
		this.selfModel = selfModel;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ExecutorService pool = Executors.newCachedThreadPool();
		pool.execute(new HeartbeatThread(selfModel));
	
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		InputStream keystoreInput = ServerSocketSSLThread.class.
			    getResourceAsStream("/key/server.jks");//serverKeystore/server.jks
		InputStream truststoreInput = ServerSocketSSLThread.class
			    .getResourceAsStream("/key/client.jks");///xty/clientKeystore/client.jks
		try {
				Keystore.setSSLFactories(keystoreInput, "comp90015",truststoreInput);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
		
		//InputStream path = this.getClass().getResourceAsStream("/serverKeystore/aGreatName");
		//System.out.println(path);
		//System.setProperty("javax.net.ssl.keyStore","xty/serverKeystore/server.jks");
		//System.setProperty("javax.net.ssl.keyStorePassword","comp90015");
		//System.setProperty("javax.net.debug","all");
		try {
			System.out.println("start to conneting through secure socket");
			
			
			SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
					.getDefault();
			SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(sport);
			
			Log.log(Common.getMethodName(), "INFO", "started");
			boolean bool = true;
			while (bool) {
				ClientModel client = new ClientModel();
				// Log.log(Common.getMethodName(), "INFO", "started");
				client.sslsocket = (SSLSocket)sslserversocket.accept();
				client.ip = client.sslsocket.getRemoteSocketAddress().toString().split(":")[0];
				long timestamp = Common.getCurrentSecTimestamp();
				if (selfModel.ipTimeList == null) {
					selfModel.ipTimeList = new HashMap<String, Long>();
				} else if (timestamp - selfModel.getLastClientTime(client.ip) < Config.CONNECTION_LIMIT_INTERVAL) {
					System.out.println("Less than connection limit interval");
					client.sslsocket.close();
					continue;
				}
				selfModel.ipTimeList.put(client.ip, timestamp);
				// client.timestamp = timestamp;

				// Timeout
				client.sslsocket.setSoTimeout(Config.CONNECTION_TIMEOUT);
				// System.out.println(client.socket.getSoTimeout());
				//System.out.println("add clients");
				selfModel.clientList.add(client);
				// TODO: Output client connection log
				// System.out.println("Connected");
				Log.log(Common.getMethodName(), "INFO",
						"New Connection:" + client.sslsocket.getRemoteSocketAddress().toString().split(":")[0] + ":"
								+ client.sslsocket.getPort());
				System.out.println("doing the operation thread");
				pool.execute(new SecureServerThread(client, selfModel));
			}
			sslserversocket.close();
			//keystoreInput.close();
			//truststoreInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Common.getMethodName(), "INFO", "Server fail to start: Port already in used(" + sport + ")");
			System.exit(0);
		}
	}
	
}
