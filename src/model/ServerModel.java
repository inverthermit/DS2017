/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import tool.Common;
import tool.Log;
import model.command.Exchange;
import model.command.Subscribe;
import client.Client;

import com.google.gson.Gson;

/**
 * This class is model of servers. It contains settings of advertisedHostName,
 * hostName, port, intervalLimit, exchangeInterval, secret, and resources of
 * socket, serverList, resourceList, clientList and ipTimeList.
 * 
 * @author Group - Alpha Panthers
 * @version 1.1
 */
public class ServerModel {
	/** All of attributes of server command line and initialize them. */
	public String advertisedHostName;
	public String hostname;
	public String intervalLimit;
	public String exchangeInterval;
	public int port;
	public int sport;
	public String secret;
	public Socket socket;
	public ArrayList<ServerModel> serverList;
	public ArrayList<ServerModel> secureServerList;
	public ArrayList<ClientModel> clientList;
	public ArrayList<Resource> resourceList;
	public ArrayList<Subscribe> subscribeList;
	public HashMap<String, Long> ipTimeList;

	/** Default constructor of the class ServerModel. */
	public ServerModel() {
		init();
	}

	public void init() {
		serverList = new ArrayList<ServerModel>();
		secureServerList = new ArrayList<ServerModel>();
		clientList = new ArrayList<ClientModel>();
		resourceList = new ArrayList<Resource>();
		subscribeList = new ArrayList<Subscribe>();
	}

	public ServerModel(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		init();
	}

	/**
	 * This is a setter() function to set the HostName for server command line
	 * 
	 * @param a
	 *            String type hostName
	 */
	public void setHostName(String hostName) {
		this.hostname = hostName;
	}

	/**
	 * This is a setter() function to set the connection interval limit for
	 * server command line
	 * 
	 * @param a
	 *            String type intervalLimit
	 */
	public void setIntervalLimit(String intervalLimit) {
		this.intervalLimit = intervalLimit;
	}

	/**
	 * This is a setter() function to set the exchange interval for server
	 * command line
	 * 
	 * @param a
	 *            String type exchangeInterval
	 */
	public void setExchangeInterval(String exchangeInterval) {
		this.exchangeInterval = exchangeInterval;
	}

	/**
	 * This is a setter() function to set the port for server command line
	 * 
	 * @param a
	 *            int type port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setSport(int sport){
		this.sport = sport;
	}

	/**
	 * This is a setter() function to set the secret for server command line
	 * 
	 * @param a
	 *            String type secret
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * This is a getter() function to get the advertised hostname for server
	 * command line
	 * 
	 * @return advertisedhostname
	 */
	public String getAdvertisedhostname() {
		return advertisedHostName;
	}

	/**
	 * This is a setter() function to set the advertised hostname for server
	 * command line
	 * 
	 * @param a
	 *            String type advertisedhostname
	 */
	public void setAdvertisedhostname(String advertisedHostName) {
		this.advertisedHostName = advertisedHostName;
	}

	public synchronized int addDelServer(ServerModel server, boolean isAdd) {
		if (isAdd) {
			for (int i = 0; i < this.serverList.size(); i++) {
				ServerModel element = this.serverList.get(i);
				if (server.hostname.equals(element.hostname)
						&& server.port == element.port) {
					this.serverList.remove(i);
					break;
				}
			}
			this.serverList.add(server);
			// System.out.println("Server List in server after ADD:"+this.toServerListJson());
			return 1;
		} else {
			int flag = 0;// Record if there's a successful deletion
			for (int i = 0; i < this.serverList.size(); i++) {
				ServerModel element = this.serverList.get(i);
				if (server.hostname.equals(element.hostname)
						&& server.port == element.port) {
					this.serverList.remove(i);
					flag = 1;
				}
			}
			// System.out.println("Server List in server after DELETE:"+this.toServerListJson());
			if (flag == 1) {
				return 2;
			} else {
				return -2;
			}
		}
	}
	
	public synchronized int addDelServerSecure(ServerModel server, boolean isAdd) {
		if (isAdd) {
			for (int i = 0; i < this.secureServerList.size(); i++) {
				ServerModel element = this.secureServerList.get(i);
				if (server.hostname.equals(element.hostname)
						&& server.sport == element.sport) {
					this.secureServerList.remove(i);
					break;
				}
			}
			this.secureServerList.add(server);
			// System.out.println("Server List in server after ADD:"+this.toServerListJson());
			return 1;
		} else {
			int flag = 0;// Record if there's a successful deletion
			for (int i = 0; i < this.secureServerList.size(); i++) {
				ServerModel element = this.secureServerList.get(i);
				if (server.hostname.equals(element.hostname)
						&& server.sport == element.sport) {
					this.secureServerList.remove(i);
					flag = 1;
				}
			}
			// System.out.println("Server List in server after DELETE:"+this.toServerListJson());
			if (flag == 1) {
				return 2;
			} else {
				return -2;
			}
		}
	}

	/**
	 * This method returns the validation of resource template.
	 * 
	 * @param Resource
	 *            Resource template isAdd True for add operation, false for
	 *            delete operation.
	 * 
	 * @return Integer(1:Successfully added;-1:Resource with same primary key
	 *         found, add failed;2:Successfully deleted;-2:No such resource to
	 *         delete)
	 */
	public synchronized int addDelResource(Resource resource, boolean isAdd) {
		if (isAdd) {
			for (int i = 0; i < this.resourceList.size(); i++) {
				Resource element = this.resourceList.get(i);
				if (resource.owner.equals(element.owner)
						&& resource.channel.equals(element.channel)
						&& resource.uri.equals(element.uri)) {
					resourceList.remove(i);
					break;
				}
				// According to Aeron's server
				else if (!resource.owner.equals(element.owner)
						&& resource.channel.equals(element.channel)
						&& resource.uri.equals(element.uri)) {
					return -1;
				}
			}
			resource.setEZserver(this.advertisedHostName);
			this.resourceList.add(resource);
			System.out.println("Resource List in server:");
			for (int i = 0; i < this.resourceList.size(); i++) {
				System.out.println(resourceList.get(i).toJSON());
			}

			// check subscribe list, returning this added resource to all
			// subscribers.
			for (Subscribe subscribe : subscribeList) {
				if (Common.isMatchedResource(subscribe.getResource(), resource)) {
					ClientModel clientModel = subscribe.getClient();
					subscribe.setNumOfHits(subscribe.getNumOfHits() + 1);
					try {
						DataOutputStream out = new DataOutputStream(
								clientModel.socket.getOutputStream());
						ArrayList<String> resultSet = new ArrayList<String>();
						resultSet.add(resource.toJSON());
						for (int i = 0; i < resultSet.size(); i++) {
							out.writeUTF(resultSet.get(i));
							Log.log(Common.getMethodName(), "FINE", "SENDING: "
									+ resultSet.get(i));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return 1;
		} else {
			int flag = 0;// Record if there's a successful deletion
			for (int i = 0; i < this.resourceList.size(); i++) {
				Resource element = this.resourceList.get(i);
				if (resource.owner.equals(element.owner)
						&& resource.channel.equals(element.channel)
						&& resource.uri.equals(element.uri)) {
					this.resourceList.remove(i);
					flag = 1;
				}
			}
			for (int i = 0; i < this.resourceList.size(); i++) {
				System.out.println(resourceList.get(i).toJSON());
			}
			if (flag == 1) {
				return 2;
			} else {
				return -2;
			}
		}
	}

	public synchronized int addDelSubscribe(Subscribe subscribe, boolean isAdd) {
		if (isAdd) {
			subscribeList.add(subscribe);
		}

		return 0;
	}

	public Subscribe getSubscribe(String id) {
		for (Subscribe subscribe : subscribeList) {
			System.out.println(subscribe.getId());
			if (subscribe.getId().equals(id)) {
				return subscribe;
			}
		}
		return null;
	}

	public Subscribe removeSubscribe(String id) {
		for (int i = 0; i < subscribeList.size(); i++) {
			if (subscribeList.get(i).getId().equals(id)) {
				return subscribeList.remove(i);
			}
		}
		return null;
	}

	/**
	 * This is a getter() function to get the last client time
	 * 
	 * @param a
	 *            String type ip
	 * @return ipTimeList
	 */
	public long getLastClientTime(String ip) {
		return this.ipTimeList.get(ip);
	}

	/**
	 * This function is used to transmit the server list instance to JSON
	 * string.This method contains its server command "EXCHANGE" and a
	 * serverList that need to do the exchange operation.
	 * 
	 * @return A JSON string contains all of information of sever list need to
	 *         do the exchange operation
	 */
	public String toServerListJson() {
		String command = "EXCHANGE";
		Exchange exchange = new Exchange(command, this.serverList);
		String json = exchange.toJSON();
		return json;
	}
	
	public String toSecureServerListJson() {
		String command = "EXCHANGE";
		Exchange exchange = new Exchange(command, this.secureServerList);
		String json = exchange.toJSON();
		return json;
	}

}