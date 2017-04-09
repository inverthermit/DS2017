package main;

import model.Resource;
import model.ServerModel;
import model.Response.NomalResponse;
import model.command.Exchange;
import model.command.Fetch;
import model.command.Publish;
import model.command.Query;
import model.command.Remove;
import model.command.Share;
import tool.ErrorMessage;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) {
		
		Resource resource = new Resource();
		resource.setName("teng");
		resource.setDescription("baby");
		String[] a = {"teng", "fei"};
		resource.setChannel("abc");
		resource.setTags(a);
		
		Publish publish = new Publish("PUBLISH", resource);
		System.out.println(publish.toJSON());
		System.out.println(publish.toJSON());
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
		ServerModel sm1 = new ServerModel("baidu.com", "3067");
		ServerModel sm2 = new ServerModel("google.com", "4000");
		ArrayList<ServerModel> serverList = new ArrayList<ServerModel>();
		serverList.add(sm1);
		serverList.add(sm2);
		Exchange exchange = new Exchange(command, serverList);
		System.out.println(exchange.toJSON());
		System.out.println("=============================");
		
		Fetch fetch = new Fetch("fetch", resource);
		System.out.println(fetch.toJSON());
		System.out.println("=============================");
		
		ErrorMessage error = new ErrorMessage();
		NomalResponse response = new NomalResponse("error", error.GENERIC_INVALID);
		System.out.println(response.toJSON());
		System.out.println("=============================");
		
		NomalResponse response1 = new NomalResponse("success");
		System.out.println(response1.toJSON());
		System.out.println("=============================");
		
	
		
		
		
	}

}
