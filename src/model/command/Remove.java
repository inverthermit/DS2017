package model.command;

import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

import model.Resource;

/**
 * Created by Tim Luo on 2017/3/27.
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
