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
		InputStream keystoreInput = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream("/serverKeystore/aGreatName");
			InputStream truststoreInput = Thread.currentThread().getContextClassLoader()
			    .getResourceAsStream("/clientKeyStore/myGreatName");
			try {
				setSSLFactories(keystoreInput, "comp90015",truststoreInput);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		//InputStream path = this.getClass().getResourceAsStream("/serverKeystore/aGreatName");
		//System.out.println(path);
		//System.setProperty("javax.net.ssl.keyStore",path.toString());
		//System.setProperty("javax.net.ssl.keyStorePassword","comp90015");
		//System.setProperty("javax.net.debug","all");
		try {
			//System.out.println("start to conneting through secure socket");
			
			
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
			keystoreInput.close();
			truststoreInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Common.getMethodName(), "INFO", "Server fail to start: Port already in used(" + sport + ")");
			System.exit(0);
		}
	}
	
	

		private static void setSSLFactories(InputStream keyStream, String keyStorePassword, 
		    InputStream trustStream) throws Exception
		{    
		  // Get keyStore
		  KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());    

		  // if your store is password protected then declare it (it can be null however)
		  char[] keyPassword = keyStorePassword.toCharArray();

		  // load the stream to your store
		  keyStore.load(keyStream, keyPassword);

		  // initialize a trust manager factory with the trusted store
		  KeyManagerFactory keyFactory = 
		  KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());    
		  keyFactory.init(keyStore, keyPassword);

		  // get the trust managers from the factory
		  KeyManager[] keyManagers = keyFactory.getKeyManagers();

		  // Now get trustStore
		  KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());    

		  // if your store is password protected then declare it (it can be null however)
		  //char[] trustPassword = password.toCharArray();

		  // load the stream to your store
		  trustStore.load(trustStream, null);

		  // initialize a trust manager factory with the trusted store
		  TrustManagerFactory trustFactory = 
		  TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());    
		  trustFactory.init(trustStore);

		  // get the trust managers from the factory
		  TrustManager[] trustManagers = trustFactory.getTrustManagers();

		  // initialize an ssl context to use these managers and set as default
		  SSLContext sslContext = SSLContext.getInstance("SSL");
		  sslContext.init(keyManagers, trustManagers, null);
		  SSLContext.setDefault(sslContext);    
		}
	
}
