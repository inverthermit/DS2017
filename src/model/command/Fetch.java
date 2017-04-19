package model.command;

import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

import model.Resource;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class Fetch extends Request {
	private String command;
	//TODO:Should be resource template
	private Resource resourceTemplate;

	public Fetch() {
	}

	public Fetch(String command, Resource resourceTemplate) {
		this.command = command;
		this.resourceTemplate = resourceTemplate;
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
