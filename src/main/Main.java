package main;

import model.Resource;
import model.ServerModel;
import model.Response.NormalResponse;
import model.command.Exchange;
import model.command.Fetch;
import model.command.Publish;
import model.command.Query;
import model.command.Remove;
import model.command.Share;
import tool.ErrorMessage;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class Main {

	public static int isValidResource(String json){
		JSONParser parser = new JSONParser();
		JSONObject command = null;
		try {
			command = (JSONObject) parser.parse(json);//invalid command
		} 	catch (Exception e) {
			return -1;
		}
		String resource = "";
		try {
			resource = command.get("resourceTemplate").toString();
		} 	catch (Exception e) {
			return -2;
			
		}
		try {
			JSONObject reTemplate = (JSONObject) parser.parse(resource);
		} 	catch (Exception e) {
			return -3;
			
		}
		return 1;
		
	}
	
	public static void main(String[] args) {
		System.out.println(isValidResource("{\"resourceTemplate\":{}}"));
		
		/*
		Resource resource = new Resource();
		resource.setName("teng");
		resource.setDescription("baby");
		String[] a = {"teng", "fei"};
		resource.setChannel("abc");
		resource.setTags(a);
		
		Publish publish = new Publish("PUBLISH", resource);
		System.out.println(publish.toJSON());
		Publish publish2 = new Publish();
		publish2.fromJSON(publish.toJSON());
		System.out.println(publish2);
		System.out.println("=============================");
		
		Remove remove = new Remove("REMOVE", resource);
		System.out.println(remove.toJSON());
		System.out.println("=============================");
		
		Share share = new Share("SHARE", "asjfkgho;wq", resource);
		System.out.println(share.toJSON());
		System.out.println("=============================");
		
		Query query = new Query("QUERY", true, resource);
		System.out.println(query.toJSON());
		System.out.println("=============================");
		
		String command = "EXCHANGE";
		ServerModel sm1 = new ServerModel("baidu.com", 3067);
		ServerModel sm2 = new ServerModel("google.com", 4000);
		ArrayList<ServerModel> serverList = new ArrayList<ServerModel>();
		serverList.add(sm1);
		serverList.add(sm2);
		Exchange exchange = new Exchange(command, serverList);
		System.out.println(exchange.toJSON());
		Exchange exchange2 = new Exchange();
		exchange2.fromJSON(exchange.toJSON());
		System.out.println(exchange2);
		System.out.println("=============================");
		
		Fetch fetch = new Fetch("fetch", resource);
		System.out.println(fetch.toJSON());
		System.out.println("=============================");
		
		ErrorMessage error = new ErrorMessage();
		NormalResponse response = new NormalResponse("error", error.GENERIC_INVALID);
		System.out.println(response.toJSON());
		System.out.println("=============================");
		
		NormalResponse response1 = new NormalResponse("success");
		System.out.println(response1.toJSON());
		System.out.println("=============================");
		*/
		/*String queryExample = "{\"command\": \"QUERY\",    \"relay\": true, \"resourceTemplate\": {\"name\": \"\",\"tags\": [],\"description\": \"\",\"uri\": \"\",\"channel\": \"\",\"owner\": \"\",\"ezserver\": null}}";
	    String publishExample = "{ \"command\": \"PUBLISH\", \"resource\" : { \"name\" : \"Unimelb website 6667\", \"tags\" : [\"web\", \"html\"], \"description\" : \"The main page for the University of Melbournee\", \"uri\" : \"http://www.unimelb1.edu.au\", \"channel\" : \"\", \"owner\" : \"\", \"ezserver\" : null } }";
	    String shareExample = "{ \"command\": \"SHARE\", \"secret\": \"2os41f58vkd9e1q4ua6ov5emlv\", \"resource\": { \"name\": \"EZShare JAR\", \"tags\": [ \"jar\" ], \"description\": \"The jar file for EZShare. Use with caution.\", \"uri\":\"file:\\/\\/\\/\\/home\\/aaron\\/EZShare\\/ezshare.jar\", \"channel\": \"my_private_channel\", \"owner\": \"aaron010\", \"ezserver\": null } }";
	    String removeExample = "{ \"command\": \"REMOVE\", \"resource\": { \"name\": \"aaa\", \"tags\": [], \"description\": \"\", \"uri\": \"http:\\/\\/www.unimelb1.edu.au\", \"channel\": \"\", \"owner\": \"\", \"ezserver\": null } }";
	    String exchangeExample = "{ \"command\": \"EXCHANGE\", \"serverList\": [ { \"hostname\": \"115.146.85.165\", \"port\": 3780 }, { \"hostname\": \"115.146.85.24\", \"port\": 3780 },{ \"hostname\": \"115.146.85.165\", \"port\": 3780 }, { \"hostname\": \"115.146.85.24\", \"port\": 3780 } ] }";	    
	    String fetchExample = "{\"command\": \"FETCH\",    \"resourceTemplate\": {\"name\": \"\",\"tags\": [],\"description\": \"\",\"uri\":\"D:/FrontEnd/pokerMemory.iso\",\"channel\": \"\",\"owner\": \"\",\"ezserver\": null}}";;

		
//		Query query = new Query();
//		query.fromJSON(fetchExample);
//		System.out.println(query.toJSON());
	    
//	    Publish publish = new Publish();
//	    publish.fromJSON(removeExample);
//	    System.out.println(publish.toJSON());
	    
	    Exchange exchange  = new Exchange();
	    exchange.fromJSON(exchangeExample);
	    System.out.println(exchange.toJSON());*/
	}

}
