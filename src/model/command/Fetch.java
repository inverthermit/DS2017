package model.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Resource;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class Fetch extends Request{
	private String command;
	private Resource resource;
	
	public Fetch() {
	}
	
	public Fetch(String command, Resource resource) {
		this.command = command;
		this.resource = resource;
	}
	
	@Override
	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}
