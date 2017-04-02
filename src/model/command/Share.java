package model.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Resource;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class Share extends Request{
	private String command;
	private String  secret;
	private Resource resource;
	
	public Share() {
	}
	
	public Share(String command, String secret, Resource resource) {
		this.command = command;
		this.secret = secret;
		this.resource = resource;
	}

	@Override
	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}
