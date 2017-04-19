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
	private Resource resourceTemplate;	

	public Query() {
	}

	public Query(String command, boolean reply, Resource resourceTemplate) {
		this.command = command;
		this.reply = reply;
		this.resourceTemplate = resourceTemplate;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Query obj = gson.fromJson(json, Query.class);
		this.command = obj.command;
		this.reply = obj.reply;
		this.resourceTemplate = new Resource(obj.resourceTemplate);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isReply() {
		return reply;
	}

	public void setReply(boolean reply) {
		this.reply = reply;
	}

	public Resource getResource() {
		return resourceTemplate;
	}

	public void setResource(Resource resourceTemplate) {
		this.resourceTemplate = resourceTemplate;
	}

}
