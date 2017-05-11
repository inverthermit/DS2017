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
public class Unsubscribe extends Request {
	private String command;
	private String id;

	public Unsubscribe() {
	}

	public Unsubscribe(String command, String id) {
		this.command = command;
		this.id = id;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Unsubscribe obj = gson.fromJson(json, Unsubscribe.class);
		this.command = obj.command;
		this.id = obj.id;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
