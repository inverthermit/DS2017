package model.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Resource;

/**
 * Created by Tim Luo on 2017/3/27.
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
