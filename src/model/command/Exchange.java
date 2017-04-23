/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 *  
 *  This class inherits the Request class and it is utilized to create a
 *  Exchange object which contains its server command "EXCHANGE" and a
 *  serverList that need to do the exchange operation.
 *  
 */
package model.command;

import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;
import model.ServerModel;

public class Exchange extends Request {
	private String command;
	private ArrayList<ServerModel> serverList;
  
	public Exchange() {

	}

	public Exchange(String command, ArrayList<ServerModel> serverList) {
		this.command = command;
		this.serverList = serverList;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Exchange obj = gson.fromJson(json, Exchange.class);
		this.command = obj.command;
		this.serverList = obj.serverList;
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public ArrayList<ServerModel> getServerList() {
		return serverList;
	}

	public void setServerList(ArrayList<ServerModel> serverList) {
		this.serverList = serverList;
	}
}
