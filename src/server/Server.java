package server;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.ClientModel;
import model.ServerModel;
/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class Server {
	private static ServerModel selfModel = new ServerModel();
	public static void main(String[] args){
		//TODO: Get port from command line?
		int port = 10000;
		//int port = 10001;
		//int port = 10002;
		ServerModel sm1 = new ServerModel("127.0.0.1",10001);
		ServerModel sm2 = new ServerModel("127.0.0.1",10000);
		//selfModel.serverList.add(sm1);
		ExecutorService pool = Executors.newCachedThreadPool();
		pool.execute(new HeartbeatThread(selfModel));
		try{
			ServerSocket server = new ServerSocket(port);
			boolean bool=true;
			while(bool){
				ClientModel client = new ClientModel();
				client.socket=server.accept();
				selfModel.clientList.add(client);
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
