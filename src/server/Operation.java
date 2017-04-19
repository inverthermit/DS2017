package server;
import model.ClientModel;
import model.Resource;
import model.ServerModel;
import model.Response.NormalResponse;
import model.command.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import client.Client;
import tool.Common;
import tool.Config;
import tool.ErrorMessage;

public class Operation {
	public ArrayList<String> dispatcher(String json, ServerModel server, ClientModel client){
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
			result = doClientFetch(fetch, server, client);
			break;
		default:
			result = null;
			break;
		}
		return result;
	}
	
	public ArrayList<String> doClientExchange(Exchange exchange,ServerModel server){
		System.out.println("doClientExchange:"+exchange.toJSON());
		ArrayList<String> result = new ArrayList<String>();
		NormalResponse nr = new NormalResponse("success");
		result.add(nr.toJSON());
		return result;
	}
	
	public ArrayList<String> doClientFetch(Fetch fetch,ServerModel server, ClientModel client){
		String fileName = (String) fetch.getResource().uri;
		// Check if file exists
		File f = new File(fileName);
		if(f.exists()){
			// Send this back to client so that they know what the file is.			
			try {
				DataOutputStream output =new DataOutputStream( client.socket.getOutputStream());
				// Send trigger to client
				NormalResponse nr = new NormalResponse("success");
				output.writeUTF(nr.toJSON());
				fetch.getResource().resourceSize = f.length();
				//System.out.println(fetch.getResource().toJSON());
				output.writeUTF(fetch.getResource().toJSON());				
				// Start sending file
				RandomAccessFile byteFile = new RandomAccessFile(f,"r");
				byte[] sendingBuffer = new byte[Config.TRUNK_SIZE];
				int num;
				// While there are still bytes to send..
				while((num = byteFile.read(sendingBuffer)) > 0){
					output.write(Arrays.copyOf(sendingBuffer, num));
				}
				byteFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			// Throw an error here..
		}
		return null;	
	}
	public ArrayList<String> doClientPublish(Publish publish,ServerModel server){
		ArrayList<String> result = new ArrayList<String>();
		int status = server.addDelResource(publish.getResource(), true); 
		if(status>0){
			NormalResponse nr = new NormalResponse("success");
			result.add(nr.toJSON());
		}
		else{
			//Temporary only return 1
		}
		return result;
	}
	public ArrayList<String> doClientQuery(Query query,ServerModel server){
		ArrayList<String> result = new ArrayList<String>();
		NormalResponse nr = new NormalResponse("success");
		result.add(nr.toJSON());
		int count =0;
		for(int i=0;i<server.resourceList.size();i++){
			Resource resource = server.resourceList.get(i);
			/*
			1.(The template channel equals (case sensitive) the resource channel AND
			2.If the template contains an owner that is not "", then the candidate owner must equal it (case sensitive) AND
			3.Any tags present in the template also are present in the candidate (case insensitive) AND
			4.If the template contains a URI then the candidate URI matches (case sensitive) AND
			5.(The candidate name contains the template name as a substring (for non "" template name) OR
			6.The candidate description contains the template description as a substring (for non "" template descriptions)
			OR
			7.The template description and name are both ""))
			*/
			if(query.getResource().channel.equals(resource.channel) &&//1
					(query.getResource().owner.equals("")||((!query.getResource().owner.equals(""))&&query.getResource().owner.equals(resource.owner))) &&//2
					(Common.arrayInArray(query.getResource().tags, resource.tags)) && //3
					(query.getResource().uri.equals(resource.uri)) &&//4
					(resource.name.contains(query.getResource().name) ||//5
					 query.getResource().description.equals("")||resource.description.contains(query.getResource().description) ||//6
					 (query.getResource().description.equals("")&&query.getResource().name.equals(""))//7
							)
					) {
				result.add(resource.toJSON());		
				count++;
			}
		}
		result.add("{\"resultSize\":"+count+"}");
		return result;
	}
	public ArrayList<String> doClientRemove(Remove remove,ServerModel server){
		ArrayList<String> result = new ArrayList<String>();
		int status = server.addDelResource(remove.getResource(), false); 
		if(status>0){
			NormalResponse nr = new NormalResponse("success");
			result.add(nr.toJSON());
		}
		else{
			NormalResponse nr = new NormalResponse("error",ErrorMessage.REMOVE_RESOURCE_NOT_EXIST);
			result.add(nr.toJSON());
		}
		return result;
	}
	public ArrayList<String> doClientShare(Share share,ServerModel server){
		ArrayList<String> result = new ArrayList<String>();
		if(!share.getSecret().equals(Common.SECRET)){
			NormalResponse nr = new NormalResponse("error",ErrorMessage.SHARE_SECRET_INCORRECT);
			result.add(nr.toJSON());
			return result;
		}
		//Check if the resource uri is a file which exists
		File f = new File(share.getResource().uri);
		if(!(f.exists() && !f.isDirectory())) { 
			NormalResponse nr = new NormalResponse("error",ErrorMessage.SHARE_MISSING);
			result.add(nr.toJSON());
			return result;
		}
		int status = server.addDelResource(share.getResource(), true); 
		if(status>0){
			NormalResponse nr = new NormalResponse("success");
			result.add(nr.toJSON());
		}
		else{
			//Temporary only return 1
		}
		return result;
	}
	
	//Send using the EXCHANGE request to other servers in server.serverList
	public ArrayList<String> doServerExchange(ServerModel server){
		String query = server.toServerListJson();
		System.out.println("In doServerExchange"+query);
		for(int i=0;i<server.serverList.size();i++){
			ServerModel tempServer = server.serverList.get(i);
			System.out.println("In doServerExchange start client.doSend"+tempServer.hostname+tempServer.port);
			//TODO: java.net.ConnectException Then Delete the address (Add a return to doSend)
			Client.doSend(tempServer.hostname, tempServer.port, query);
			System.out.println("Finished doServerExchange start client.doSend"+tempServer.hostname+tempServer.port);
		}
		return null;
	}
}
