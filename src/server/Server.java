package server;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tool.Common;
import tool.Config;
import tool.Log;
import tool.ServerCommandLine;
import model.ClientModel;
import model.ServerModel;
/**
	 * Created by Tim Luo on 2017/3/27.
	 */
/*
-advertisedhostname localhost -connectionintervallimit 10 -exchangeinterval 10 -port 10000 -secret asdfwefwasdf -debug
*/
public class Server {
	private static ServerModel selfModel = new ServerModel();
	public static void main(String[] args){
		ServerModel sm = ServerCommandLine.ServerCommandLine(args);
		if(sm == null){
			return;
		}
		else{
			selfModel = sm;
		}
		//TODO: Get port from command line?
		if(selfModel.port == 0){
			selfModel.port = Config.DEFAULT_PORT;
		}
		if(selfModel.advertisedhostname == null){
			selfModel.advertisedhostname = Config.DEFAULT_ADVERTISED_HOSTNAME;
		}
		if(selfModel.secret == null){
			selfModel.secret = Common.SECRET;
		}
		if(selfModel.hostname == null){
			selfModel.hostname = "127.0.0.1";
		}
		if(selfModel.exchangeInterval!=null){
			//to int catch exception
			try{
				int interval = Integer.parseInt(selfModel.exchangeInterval);
				Config.HEARTBEAT_INTERVAL = interval*1000;
			}
			catch(Exception ee){
				//System.out.println("exchange interval is not an int");
				Log.log(Common.getMethodName(), "INFO", "Exchange interval is not an int");
				return;
			}
		}
		if(selfModel.intervallimit!=null){
			//to int catch exception
			try{
				int interval = Integer.parseInt(selfModel.intervallimit);
				Config.CONNECTION_LIMIT_INTERVAL = interval*1000;
			}
			catch(Exception ee){
				//System.out.println("connection interval is not an int");
				Log.log(Common.getMethodName(), "INFO", "Connection interval is not an int");
				return;
			}
		}
		selfModel.init();
		int port = selfModel.port;
		//System.out.println(selfModel.hostname+":"+selfModel.port+" "+selfModel.exchangeInterval+" "+selfModel.intervallimit+" "+selfModel.secret);
		Log.log(Common.getMethodName(), "INFO", "Starting the EZShare Server");
		Log.log(Common.getMethodName(), "INFO", "using advertised hostname: "+selfModel.hostname);
		Log.log(Common.getMethodName(), "INFO", "using secret: "+selfModel.secret);
		//int port = 10001;
		//int port = 10002;
		ServerModel sm1 = new ServerModel("127.0.0.1",10001);
		ServerModel sm2 = new ServerModel("127.0.0.1",10000);
		//selfModel.serverList.add(sm1);
		ExecutorService pool = Executors.newCachedThreadPool();
		pool.execute(new HeartbeatThread(selfModel));
		try{
			Log.log(Common.getMethodName(), "INFO", "bound to port "+port);
			ServerSocket server = new ServerSocket(port);
			boolean bool=true;
			while(bool){
				ClientModel client = new ClientModel();
				//Log.log(Common.getMethodName(), "INFO", "started");
				client.socket=server.accept();
				//Timeout
				client.socket.setSoTimeout(Config.CONNECTION_TIMEOUT);
				//System.out.println(client.socket.getSoTimeout());
				selfModel.clientList.add(client);
				//TODO: Output client connection log
				//System.out.println("Connected");
				Log.log(Common.getMethodName(), "INFO", "New Connection:"+client.socket.getInetAddress()+":"+client.socket.getPort());
				pool.execute(new ServerThread(client,selfModel));
			}
			server.close();
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
    }
}
