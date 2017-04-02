package model.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Resource;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class Query extends Request {
	private String command;
	private boolean reply; 
	private Resource resource;
	
	public Query() {
	}
	
	public Query(String command, boolean reply, Resource resource) {
		this.command = command;
		this.reply = reply;
		this.resource = resource;
	}

	@Override
	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

}
