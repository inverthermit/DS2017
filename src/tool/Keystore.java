package tool;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class Keystore {
	
	public static void setSSLFactories(InputStream keyStream, String keyStorePassword, 
		    InputStream trustStream) throws Exception
		{    
		  // Get keyStore
		  KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());    
		  //KeyStore keyStore = KeyStore.getInstance("JKS");  
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
		  //SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		  //SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("server0", 3781);
		}

}
