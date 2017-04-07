package server;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.ServerModel;
/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class Server {
	private static ServerModel selfModel = new ServerModel();
	public static void main(String[] args){
		//TODO: Get port from command line?
		int port = 9999;
		ExecutorService pool = Executors.newCachedThreadPool();
		try{
			ServerSocket server = new ServerSocket(port);
			Socket client = null;
			boolean bool=true;
			while(bool){
				client=server.accept();
				//TODO: Output client connection log
				System.out.println("Connected");
				pool.execute(new ServerThread(client,selfModel));
			}
			server.close();
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
    }
}
