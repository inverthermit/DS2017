/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model.command;

import com.google.gson.Gson;
import model.Resource;

/**
 * This class inherits the Request class and it is utilized to create a
 * Query object which contains its server command "QUERY", a boolean relay
 * and a resource instance. The relay field sets as true then the server
 * sends a QUERY command to each of the servers in its serverList.
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class Query extends Request {
	private String command;
	private boolean relay = true;
	private Resource resourceTemplate;	

	public Query() {
	}

	public Query(String command, boolean relay, Resource resourceTemplate) {
		this.command = command;
		this.relay = relay;
		this.resourceTemplate = resourceTemplate;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Query obj = gson.fromJson(json, Query.class);
		this.command = obj.command;
		this.relay = obj.relay;
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

	public Resource getResource() {
		return resourceTemplate;
	}

	public void setResource(Resource resourceTemplate) {
		this.resourceTemplate = resourceTemplate;

	}
}
