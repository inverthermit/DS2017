package server;
import java.net.*;
import java.io.*;
import java.util.*;

import model.ServerModel;

public class ServerThread implements Runnable{
	private Socket client;
	private DataInputStream in;
	private DataOutputStream out;
	private ServerModel selfModel;
	
	public ServerThread(Socket client, ServerModel selfModel){
		this.client=client;
		this.selfModel = selfModel;
		try{
			this.in = new DataInputStream( this.client.getInputStream());
		    this.out =new DataOutputStream( this.client.getOutputStream());
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
	}
	@Override
	public void run() {
		try{
			while(true){
		    	if(in.available() > 0) {
		    		//1.Get message
	                String message = in.readUTF();
	                //TODO: Print out log
	                System.out.println(message);
	                //TODO:2.Parse message, do operations, return ArrayList<String> to send back to client
	                Operation op = new Operation();
	                ArrayList<String> resultSet = op.dispatcher(message, selfModel);
	                if(resultSet==null){
	                	//Print log
	                	System.out.print("Nothing to output...");
	                	return;
	                }
	                //3.Send results back to client
	                for(int i=0;i<resultSet.size();i++){
	                	out.writeUTF(resultSet.get(i));
	                }
	                break;
	            }
		    }
			//4.Close the socket and quit the thread and leave it to be dealt by the threadpool
			in.close();
			out.close();
			client.close();
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
		
		
	}

}
