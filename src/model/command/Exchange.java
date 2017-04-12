package model.command;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.ServerModel;

/**
 * Created by Tim Luo on 2017/3/27.
 */
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
	
	public String toString() {
		return "Command: " + this.command + ", Resource: [server1="
				+ this.serverList.get(0) + ", server2="
				+ this.serverList.get(1) + "]";
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
