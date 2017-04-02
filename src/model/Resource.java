package model;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import org.json.simple.JSONObject;
/**
	 * Created by Tim Luo on 2017/3/27.
	 */

public class Resource {
	
	public String name;
	public String description;
	public String[] tags;
	public String url;
	public String channel;
	public String owner;
	public String EZserver;
	
	public Resource() {

	}		
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setEZserver(String eZserver) {
		EZserver = eZserver;
	}
	
	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
	@Override
	public String toString() {
		return "Resource [name=" + name + ", description=" + description + ", tags=" + Arrays.toString(tags) + "]";
	}
}
