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
	public String intervallimit;
	public String exchangeInterval;
	public int port;
	public String secret;
	public Socket socket;
	public ArrayList<ServerModel> serverList;
	public ArrayList<ClientModel> clientList;
	public ArrayList<Resource> resourceList;

	public ServerModel() {

	}
	
	public void init(){
		serverList = new ArrayList<ServerModel>();
		clientList = new ArrayList<ClientModel>();
		resourceList = new ArrayList<Resource>();
	}

	public ServerModel(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		}
	
	public void setHostName(String hostName){
		this.hostname = hostName;
	}
	
	public void setIntervalLimit(String intervalLimit){
		this.intervallimit = intervalLimit;
	}
	
	public void setExchangeInterval(String exchangeInterval){
		this.exchangeInterval = exchangeInterval;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public void setSecret(String secret){
		this.secret = secret;
	}

	/**
	 * addDelResource Parameters: Resource(including resource template),
	 * isAdd(true for add operation, false for delete operation) Return:
	 * Integer(1:Successfully added;-1:Resource with same primary key found, add
	 * failed;2:Successfully deleted;-2:No such resource to delete)
	 */

	public synchronized int addDelResource(Resource resource, boolean isAdd) {
		if (isAdd) {
			for (int i = 0; i < this.resourceList.size(); i++) {
				Resource element = this.resourceList.get(i);
				if (resource.owner.equals(element.owner) && resource.channel.equals(element.channel)
						&& resource.uri.equals(element.uri)) {
					resourceList.remove(i);
					break;
				}
			}
			this.resourceList.add(resource);
			System.out.println("Resource List in server:");
			for(int i=0;i<this.resourceList.size();i++){
				System.out.println(resourceList.get(i).toJSON());
			}
			return 1;
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
			for(int i=0;i<this.resourceList.size();i++){
				System.out.println(resourceList.get(i).toJSON());
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