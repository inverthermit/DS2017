/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model.command;

import com.google.gson.Gson;
import model.Resource;

/**
 * This class inherits the Request class and it is utilized to create a Fetch
 * object which contains its server command "FETCH" and a resource instance. 
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class Fetch extends Request {
	private String command;
	private Resource resourceTemplate;

	public Fetch() {
	}

	public Fetch(String command, Resource resourceTemplate) {
		this.command = command;
		this.resourceTemplate = resourceTemplate;
		this.resourceTemplate.resourceSize = 0l;
	}
	
	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Fetch obj = gson.fromJson(json, Fetch.class);
		this.command = obj.command;
		this.resourceTemplate = new Resource(obj.resourceTemplate);
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Resource getResource() {
		return resourceTemplate;
	}

	public void setResource(Resource resourceTemplate) {
		this.resourceTemplate = resourceTemplate;
	}
}
