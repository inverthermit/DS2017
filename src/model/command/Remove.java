package model.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

}
