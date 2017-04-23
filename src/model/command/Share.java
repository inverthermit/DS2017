/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model.command;

import com.google.gson.Gson;
import model.Resource;

/**
 * This class inherits the Request class and it is utilized to create a
 * Share object which contains its server command "SHARE", a random string secret and a resource
 * instance.
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class Share extends Request {
	private String command;
	private String secret;
	private Resource resource;

	public Share() {
	}

	public Share(String command, String secret, Resource resource) {
		this.command = command;
		this.secret = secret;
		this.resource = resource;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Share obj = gson.fromJson(json, Share.class);
		this.command = obj.command;
		this.secret = obj.secret;
		this.resource = new Resource(obj.resource);
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
