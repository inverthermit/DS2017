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
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Query obj = gson.fromJson(json, Query.class);
		this.command = obj.command;
		this.reply = obj.reply;
		this.resource = new Resource(obj.resource);
	}

}
