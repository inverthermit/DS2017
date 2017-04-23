/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 *  
 *  This class inherits the Request class and it is utilized to create a
 *  Publish object which contains its server command "PUBLISH" and a resource
 *  instance. 
 *  
 */
package model.command;

import java.util.Arrays;
import com.google.gson.Gson;
import model.Resource;

public class Publish extends Request {
	private String command;
	private Resource resource;

	public Publish() {
	}

	public Publish(String command, Resource resource) {
		this.command = command;
		this.resource = resource;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Publish obj = gson.fromJson(json, Publish.class);
		
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

