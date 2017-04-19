package model;

import java.net.Socket;
import java.util.ArrayList;

import model.command.Exchange;

import com.google.gson.Gson;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class ServerModel {

	public String hostname;
	public int port;
	public Socket socket;
	public ArrayList<ServerModel> serverList = new ArrayList<ServerModel>();
	public ArrayList<ClientModel> clientList = new ArrayList<ClientModel>();
	public ArrayList<Resource> resourceList = new ArrayList<Resource>();

	public ServerModel() {

	}

	public ServerModel(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	/**
	 * addDelResource Parameters: Resource(including resource template),
	 * isAdd(true for add operation, false for delete operation) Return:
	 * Integer(1:Successfully added;-1:Resource with same primary key found, add
	 * failed;2:Successfully deleted;-2:No such resource to delete)
	 */
	public synchronized int addDelResource(Resource resource, boolean isAdd) {
		if (isAdd) {
			int flag = 0;
			for (int i = 0; i < this.resourceList.size(); i++) {
				Resource element = this.resourceList.get(i);
				if (resource.owner.equals(element.owner) && resource.channel.equals(element.channel)
						&& resource.uri.equals(element.uri)) {
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				this.resourceList.add(resource);
				return 1;
			} else {
				return -1;
			}
		} else {
			int flag = 0;// Record if there's a successful deletion
			for (int i = 0; i < this.resourceList.size(); i++) {
				Resource element = this.resourceList.get(i);
				if (resource.owner.equals(element.owner) && resource.channel.equals(element.channel)
						&& resource.uri.equals(element.uri)) {
					this.resourceList.remove(i);
					flag = 1;
				}
			}
			if (flag == 1) {
				return 2;
			} else {
				return -2;
			}
		}
	}

	public String toServerListJson() {
		String command = "EXCHANGE";
		Exchange exchange = new Exchange(command, this.serverList);
		String json = exchange.toJSON();
		return json;
	}

}