/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model.command;

import com.google.gson.Gson;
import model.Resource;

/**
 * This class inherits the Request class and it is utilized to create a
 * Remove object which contains its server command "REMOVE" and a resource
 * instance. 
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class Remove extends Request {
	private String command;
	private Resource resource;

	public Remove() {
	}

	public Remove(String command, Resource resource) {
		this.command = command;
		this.resource = resource;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Remove obj = gson.fromJson(json, Remove.class);
		this.command = obj.command;
		this.resource = new Resource(obj.resource);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
