package server;
import model.ServerModel;
import model.command.*;

import java.util.*;

import client.Client;
import tool.Common;

public class Operation {
	public ArrayList<String> dispatcher(String json, ServerModel server){
		//1.get the command of json
		String op = Common.getOperationfromJson(json);
		ArrayList<String> result = null;
		switch (op){
		case "PUBLISH":
			Publish publish = new Publish();
			publish.fromJSON(json);
			result = doClientPublish(publish, server);
			break;
		case "REMOVE":
			Remove remove = new Remove();
			remove.fromJSON(json);
			result = doClientRemove(remove, server);
			break;
		case "SHARE":
			Share share = new Share();
			share.fromJSON(json);
			result = doClientShare(share, server);
			break;
		case "QUERY":
			Query query = new Query();
			query.fromJSON(json);
			result = doClientQuery(query, server);
			break;
		case "EXCHANGE":
			Exchange exchange = new Exchange();
			exchange.fromJSON(json);
			result = doClientExchange(exchange, server);
			break;
		case "FETCH":
			Fetch fetch = new Fetch();
			fetch.fromJSON(json);
			result = doClientFetch(fetch, server);
			break;
		default:
			result = null;
			break;
		}
		return result;
	}
	
	public ArrayList<String> doClientExchange(Exchange exchange,ServerModel server){
		
		return null;
	}
	
	//TODO:Add file stream transfer API!!!!!!!!! This is a special operation
	public ArrayList<String> doClientFetch(Fetch fetch,ServerModel server){

		return null;	
	}
	public ArrayList<String> doClientPublish(Publish publish,ServerModel server){
		server.addDelResource(publish.getResource(), true);
		return null;
	}
	public ArrayList<String> doClientQuery(Query query,ServerModel server){

		return null;
	}
	public ArrayList<String> doClientRemove(Remove remove,ServerModel server){
		server.addDelResource(remove.getResource(), false);//Add getters and setters
		return null;
	}
	public ArrayList<String> doClientShare(Share share,ServerModel server){
		//TODO:Check if the resource uri is a file which exists
		server.addDelResource(share.getResource(), true);
		return null;
	}
	
	//Send using the EXCHANGE request to other servers in server.serverList
	public ArrayList<String> doServerExchange(ServerModel server){
		String query = server.toServerListJson();
		for(int i=0;i<server.serverList.size();i++){
			ServerModel tempServer = server.serverList.get(i);
			Client.doSend(tempServer.hostname, tempServer.port, query);
		}
		return null;
	}
}
