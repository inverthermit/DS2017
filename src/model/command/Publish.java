package model.command;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import net.sf.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Resource;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class Publish extends Request {
	private String command;
	private Resource resource;
	
	public Publish() {
	}
	
	public Publish(String command, Resource resource) {
		this.command = command;
		this.resource = resource;
	}
	
	@Override
	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

}

// @Override
// public String toJSON() {

/* EXAMPLE */

// How to build a json object
// JSONObject entry=new JSONObject();
// entry.put("commit_fr", "pb");
// entry.put("ie", "utf-8");
// entry.put("tbs", "d316085121f93c631441951687");
// entry.put("kw", "�������ҵ��ѧ");
// entry.put("fid", "35522");
// entry.put("tid", tid);
// entry.put("pid", pid);
// entry.put("is_vipdel", "0");
// entry.put("is_finf", "false");
//
// //How to build a json array
// JSONObject scholarshipItem;
// List<JSONObject> scholarshipList=new ArrayList<JSONObject>();
// for(Map.Entry<String, String>e:major.getScholarship().entrySet()){
// scholarshipItem=new JSONObject();
// scholarshipItem.put("name", e.getKey());
// scholarshipItem.put("value", e.getValue());
// scholarshipList.add(scholarshipItem);
// }
//
// entry.put("asdf", scholarshipList);
//
//
// return null;
// }
// }