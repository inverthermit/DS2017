/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model.command;

import com.google.gson.Gson;

import model.ClientModel;
import model.Resource;

/**
 * This class inherits the Request class and it is utilized to create a Query
 * object which contains its server command "QUERY", a boolean relay and a
 * resource instance. The relay field sets as true then the server sends a QUERY
 * command to each of the servers in its serverList.
 * 
 * @author Group - Alpha Panthers
 * @version 1.1
 */
public class Subscribe extends Request {
	private static final int ID_LEN = 5;
	private String command;
	private boolean relay = true;
	private Resource resourceTemplate;
	private String id;

	// These two fields are actually related to the subscriber
	private transient ClientModel client;
	private transient int numOfHits;

	public Subscribe() {
		this.id = tool.Common.randomString(ID_LEN);
	}

	public Subscribe(String command, boolean relay, Resource resourceTemplate) {
		super();
		this.command = command;
		this.relay = relay;
		this.resourceTemplate = resourceTemplate;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Subscribe obj = gson.fromJson(json, Subscribe.class);
		this.command = obj.command;
		this.relay = obj.relay;
		this.id = obj.id;
		this.resourceTemplate = new Resource(obj.resourceTemplate);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isRelay() {
		return relay;
	}

	public void setRelay(boolean relay) {
		this.relay = relay;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Resource getResource() {
		return resourceTemplate;
	}

	public void setResource(Resource resourceTemplate) {
		this.resourceTemplate = resourceTemplate;
	}

	public ClientModel getClient() {
		return client;
	}

	public void setClient(ClientModel client) {
		this.client = client;
	}

	public int getNumOfHits() {
		return numOfHits;
	}

	public void setNumOfHits(int numOfHits) {
		this.numOfHits = numOfHits;
	}
}
