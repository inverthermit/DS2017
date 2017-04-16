package server;
import model.ClientModel;
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
		NormalResponse nr = new NormalResponse("success");
		ArrayList<String> arr = new ArrayList<String>();
		arr.add(nr.toJSON());
		return arr;
	}
	
	//TODO:Add file stream transfer API!!!!!!!!! This is a special operation
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
