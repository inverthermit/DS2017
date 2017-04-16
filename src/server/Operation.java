package server;
import model.ServerModel;
import model.command.*;
import java.util.*;

public class Operation {
	public ArrayList<String> dispatcher(String json){
		//TODO:1.get the command of json
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
	public ArrayList<String> doServerExchange(Exchange exchange,ServerModel server){
		
		return null;
	}

}
