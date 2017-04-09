package model.command;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.ServerModel;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class Exchange extends Request {
	private String command;
	private ArrayList<ServerModel> serverList;

	public Exchange() {

	}

	public Exchange(String command, ArrayList<ServerModel> serverList) {
		this.command = command;
		this.serverList = serverList;
	}

	@Override
	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	@Override
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Exchange obj = gson.fromJson(json, Exchange.class);
		this.command = obj.command;
		this.serverList = obj.serverList;
	}

}
