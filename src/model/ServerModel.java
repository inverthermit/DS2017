package model;

import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class ServerModel {
	
	public String hostname;
	public String port;
	public Socket socket;
	public ArrayList<ServerModel> serverList;
	public ArrayList<ClientModel> clientList;
	public ArrayList<Resource> resourceList;
	
	public ServerModel() {
		
	}
	
	public ServerModel(String hostname, String port) {
		this.hostname = hostname;
		this.port = port;
	}
	
	public synchronized void addResource(Resource resource){
		resourceList.add(resource);
	}
	
}