/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import model.command.Exchange;

import com.google.gson.Gson;


public class ServerModel {
	/** All of attributes of server command line and initialize them. */
	public String advertisedHostName;
	public String hostName;
	public String intervalLimit;
	public String exchangeInterval;
	public int port;
	public String secret;
	public Socket socket;
	public ArrayList<ServerModel> serverList;
	public ArrayList<ClientModel> clientList;
	public ArrayList<Resource> resourceList;
	public HashMap<String,Long> ipTimeList;

	/** Default constructor of the class ServerModel. */
	public ServerModel() {

	}
	
	public void init(){
		serverList = new ArrayList<ServerModel>();
		clientList = new ArrayList<ClientModel>();
		resourceList = new ArrayList<Resource>();
	}

	public ServerModel(String hostname, int port) {
		this.hostName = hostname;
		this.port = port;
		}
	
	/**
     * This is a setter() function to set the HostName for server command line
     * 
     * @param a String type hostName
     */
	public void setHostName(String hostName){
		this.hostName = hostName;
	}
	
	/**
     * This is a setter() function to set the connection interval limit
     * for server command line
     * 
     * @param a String type intervalLimit
     */
	public void setIntervalLimit(String intervalLimit){
		this.intervalLimit = intervalLimit;
	}
	
	/**
     * This is a setter() function to set the exchange interval
     * for server command line
     * 
     * @param a String type exchangeInterval
     */
	public void setExchangeInterval(String exchangeInterval){
		this.exchangeInterval = exchangeInterval;
	}
	
	/**
     * This is a setter() function to set the port for server command line
     * 
     * @param a int type port
     */
	public void setPort(int port){
		this.port = port;
	}
	
	/**
     * This is a setter() function to set the secret for server command line
     * 
     * @param a String type secret
     */
	public void setSecret(String secret){
		this.secret = secret;
	}
	
	/**
     * This is a getter() function to get the advertised hostname
     *  for server command line
     * 
     * @return advertisedhostname
     */
	public String getAdvertisedhostname() {
		return advertisedHostName;
	}

	/**
     * This is a setter() function to set the advertised hostname for server command line
     * 
     * @param a String type advertisedhostname
     */
	public void setAdvertisedhostname(String advertisedHostName) {
		this.advertisedHostName = advertisedHostName;
	}
	
	public synchronized int addDelServer(ServerModel server, boolean isAdd) {
		if (isAdd) {
			for (int i = 0; i < this.serverList.size(); i++) {
				ServerModel element = this.serverList.get(i);
				if (server.hostName.equals(element.hostName) && server.port==element.port) {
					this.serverList.remove(i);
					break;
				}
			}
			this.serverList.add(server);
            //System.out.println("Server List in server after ADD:"+this.toServerListJson());
		    return 1;
		} else {
			int flag = 0;// Record if there's a successful deletion
			for (int i = 0; i < this.serverList.size(); i++) {
				ServerModel element = this.serverList.get(i);
				if (server.hostName.equals(element.hostName) && server.port==element.port) {
					this.serverList.remove(i);
					flag = 1;
				}
			}
			//System.out.println("Server List in server after DELETE:"+this.toServerListJson());
			if (flag == 1) {
				return 2;
			} else {
				return -2;
			}
		}
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
				//According to Aeron's server
				else if(!resource.owner.equals(element.owner) && resource.channel.equals(element.channel)
						&& resource.uri.equals(element.uri)){
					return -1;
				}
			}
			resource.setEZserver(this.advertisedHostName);
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
	
	/**
     * This is a getter() function to get the last client time
     * 
     * @param a String type ip
     * @return ipTimeList
     */
	public long getLastClientTime(String ip){
		return this.ipTimeList.get(ip);
	}

	/**
     * This function is used to transmit the server list instance to JSON
     * string.This method contains its server command "EXCHANGE" and a
     * serverList that need to do the exchange operation.
     * 
     * @return A JSON string contains all of information of sever list
     * need to do the exchange operation
     */
	public String toServerListJson() {
		String command = "EXCHANGE";
		Exchange exchange = new Exchange(command, this.serverList);
		String json = exchange.toJSON();
		return json;
	}

}