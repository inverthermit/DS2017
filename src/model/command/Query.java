package model.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Resource;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class Query extends Request {
	private String command;
	private boolean relay = true;
	private Resource resourceTemplate;	



	public Query() {
	}

	public Query(String command, boolean relay, Resource resourceTemplate) {
		this.command = command;
		this.relay = relay;

		this.resourceTemplate = resourceTemplate;

		this.resourceTemplate = resourceTemplate;
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Query obj = gson.fromJson(json, Query.class);
		this.command = obj.command;
		this.relay = obj.relay;
		this.resourceTemplate = new Resource(obj.resourceTemplate);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isRelay() {
		return relay;
	}

	public void setRelay(boolean relay) {
		this.relay = relay;
	}

	public Resource getResource() {
		return resourceTemplate;
	}


	public void setResource(Resource resourceTemplate) {
		this.resourceTemplate = resourceTemplate;

	}

}
