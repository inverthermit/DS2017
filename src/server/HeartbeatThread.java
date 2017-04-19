package server;

import tool.Config;
import model.ServerModel;

public class HeartbeatThread implements Runnable{
	private ServerModel server;
	public HeartbeatThread(ServerModel server){
		this.server = server;
	}

	@Override
	public void run() {
		Operation op = new Operation();
		int i=0;
		while(true){
			try {
				Thread.sleep(Config.HEARTBEAT_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("Heartbeat Count:"+i);
			i++;
			if(server.serverList.size()>0){
				op.doServerExchange(this.server);
			}
			else{
				continue;
			}
		}
	}
	public ServerModel getServer() {
		return server;
	}

	public void setServer(ServerModel server) {
		this.server = server;
	}

}
