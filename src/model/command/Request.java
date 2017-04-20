package model.command;

import java.util.Arrays;

import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

import model.Resource;

public abstract class Request {

	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public abstract void fromJSON(String json);

}
