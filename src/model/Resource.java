package model;

import java.util.Arrays;

import model.command.Share;

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
	public String uri;
	public String channel;
	public String owner;
	public String[] EZserver;
	public long resourceSize;

	public Resource() {

	}

	//Tim: this shouldn't be a Constructor, it actually finish the job of deep copy of java object. Change function name to copyOf/clone
	//Refer to http://stackoverflow.com/questions/64036/how-do-you-make-a-deep-copy-of-an-object-in-java
	//Use serialization code in the page.
	public Resource(Resource obj) {
		this.name = obj.name;
		this.description = obj.description;
		this.tags = obj.tags;
		this.uri = obj.uri;
		this.channel = obj.channel;
		this.owner = obj.owner;
		this.EZserver = obj.EZserver;
		this.resourceSize = obj.resourceSize;
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

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setEZserver(String[] eZserver) {
		EZserver = eZserver;
	}

	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	public String toString() {
		return "Resource [name=" + name + ", description=" + description
				+ ", tags=" + Arrays.toString(tags) + "]";
	}
	
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Resource obj = gson.fromJson(json, Resource.class);
		this.name = obj.name;
		this.description = obj.description;
		this.tags = obj.tags;
		this.uri = obj.uri;
		this.channel = obj.channel;
		this.owner = obj.owner;
		this.EZserver = obj.EZserver;
		this.resourceSize = obj.resourceSize;
	}
}
