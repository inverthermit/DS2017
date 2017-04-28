/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package server;

import tool.Config;
import model.ServerModel;
/**
 * This class is a thread class doing "heartbeat" check with other servers in serverList.
 * It implements Runnable interface.
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class HeartbeatThread implements Runnable{
	private ServerModel server;
	public HeartbeatThread(ServerModel server){
		this.server = server;
	}
	/**
	 * This method runs a thread which do server exchange operation
	 * in every heart beat interval.
	 * 
	 */
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
