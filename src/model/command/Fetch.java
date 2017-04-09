package model.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Resource;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class Fetch extends Request {
	private String command;
	private Resource resource;

	public Fetch() {
	}

	public Fetch(String command, Resource resource) {
		this.command = command;
		this.resource = resource;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Fetch obj = gson.fromJson(json, Fetch.class);
		this.command = obj.command;
		this.resource = new Resource(obj.resource);
	}
}
