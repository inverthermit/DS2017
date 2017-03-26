package model;

import java.net.Socket;
import java.util.ArrayList;

/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class ServerModel {
	
	public String ip;
	public String port;
	public Socket socket;
	public ArrayList<ServerModel> serverList;
	public ArrayList<ClientModel> clientList;
	public ArrayList<Resource> resourceList;
	
}
