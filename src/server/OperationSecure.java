/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package server;

import model.ClientModel;
import model.Resource;
import model.ServerModel;
import model.Response.NormalResponse;
import model.Response.SubscribeResponse;
import model.command.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import client.Client;
import tool.Common;
import tool.Config;
import tool.ErrorMessage;
import tool.Log;

public class OperationSecure {

	public ArrayList<String> dispatcher(String json, ServerModel server,
			ClientModel client) {
		// 1.get the command of json
		ArrayList<String> result = null;
		if (checkResource(json, "") == -1) {
			result = new ArrayList<String>();
			NormalResponse nr = new NormalResponse("error",
					ErrorMessage.GENERIC_INVALID);
			result.add(nr.toJSON());
		} else {
			String op = Common.getOperationfromJson(json);
			if (op == null) {
				result = new ArrayList<String>();
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.GENERIC_INVALID);
				result.add(nr.toJSON());
				return result;
			}
			if (checkValidMissing(op, json) != null) {
				result = new ArrayList<String>();
				result.add(checkValidMissing(op, json));
				return result;
			} else {
				switch (op) {
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
				case "SUBSCRIBE":
					Subscribe subscribe = new Subscribe();
					subscribe.fromJSON(json);
					result = doClientSubscribe(subscribe, server, client);
					break;
				case "UNSUBSCRIBE":
					Unsubscribe unsubscribe = new Unsubscribe();
					unsubscribe.fromJSON(json);
					result = doClientUnsubscribe(unsubscribe, server, client);
					break;
				default:
					result = null;
					break;
				}
			}
		}
		return result;
	}

	public ArrayList<String> doClientExchange(Exchange exchange,
			ServerModel server) {
		// System.out.println("doClientExchange:" + exchange.toJSON());
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<ServerModel> queryServerList = exchange.getServerList();
		for (int i = 0; i < queryServerList.size(); i++) {
			server.addDelServerSecure(queryServerList.get(i), true);
		}
		System.out.println("Server List in server after ADD:"
				+ server.toSecureServerListJson());
		NormalResponse nr = new NormalResponse("success");
		result.add(nr.toJSON());
		return result;
	}

	public ArrayList<String> doClientFetch(Fetch fetch, ServerModel server,
			ClientModel client) {
		ArrayList<String> result = new ArrayList<String>();

		if (checkServerFetch(fetch) != null) {
			result.add(checkServerFetch(fetch));
			return result;
		} else {

			boolean hasResource = false;
			Resource fetchResource = fetch.getResource();
			for (int i = 0; i < server.resourceList.size(); i++) {
				Resource rc = server.resourceList.get(i);
				/*
				 * System.out.println(fetchResource.name+" "+rc.name);
				 * System.out.println(fetchResource.channel+" "+rc.channel);
				 * System.out.println(fetchResource.uri+" "+rc.uri);
				 */
				// Only the channel and URI fields in the template is relevant
				// as it must be an exact match for the command to work.
				if (fetchResource.channel.equals(rc.channel)
						&& fetchResource.uri.equals(rc.uri)) {
					hasResource = true;
					break;
				}
			}
			if (!hasResource) {
				NormalResponse nr = new NormalResponse("success");
				result.add(nr.toJSON());
				result.add("{\"resultSize\":0}");
				return result;
			}
			String fileName = (String) fetch.getResource().uri;
			if (fileName.startsWith("file:")) {
				fileName = fileName.replace("file:", "");
			}
			// Check if file exists
			File f = new File(fileName);
			if (f.exists()) {
				// Send this back to client so that they know what the file is.
				try {
					DataOutputStream output = new DataOutputStream(
							client.socket.getOutputStream());
					// Send trigger to client
					NormalResponse nr = new NormalResponse("success");
					output.writeUTF(nr.toJSON());
					Log.log(Common.getMethodName(), "FINE",
							"SENDING: " + nr.toJSON());
					fetch.getResource().resourceSize = f.length();
					// System.out.println(fetch.getResource().toJSON());
					output.writeUTF(fetch.getResource().toJSON());
					Log.log(Common.getMethodName(), "FINE", "SENDING: "
							+ fetch.getResource().toJSON());
					// Start sending file
					RandomAccessFile byteFile = new RandomAccessFile(f, "r");
					byte[] sendingBuffer = new byte[Config.TRUNK_SIZE];
					int num;
					// While there are still bytes to send..
					while ((num = byteFile.read(sendingBuffer)) > 0) {
						output.write(Arrays.copyOf(sendingBuffer, num));
					}
					byteFile.close();
				} catch (IOException e) {
					e.printStackTrace();
					NormalResponse nr = new NormalResponse("error",
							ErrorMessage.QUERY_FETCH_RESOURCETEMPLATE_INVALID
									+ "2");
					result.add(nr.toJSON());
					return result;
				}
			} else {
				// Throw an error here..
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.QUERY_FETCH_RESOURCETEMPLATE_INVALID + "3");
				result.add(nr.toJSON());
				return result;
			}
		}
		return null;
	}

	public ArrayList<String> doClientPublish(Publish publish, ServerModel server) {
		ArrayList<String> result = new ArrayList<String>();

		if (checkServerPublish(publish) != null) {
			result.add(checkServerPublish(publish));
			return result;
		} else {
			int status = server.addDelResource(publish.getResource(), true);
			if (status > 0) {
				NormalResponse nr = new NormalResponse("success");
				result.add(nr.toJSON());
			} else { // error 4
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.PUBLISH_BROKEN);
				result.add(nr.toJSON());
			}
		}
		return result;
	}

	public ArrayList<Resource> resourceMatch(Resource queryResource,
			ArrayList<Resource> resourceList) {
		ArrayList<Resource> matchedResource = new ArrayList<Resource>();
		for (Resource resource : resourceList) {
			if (Common.isMatchedResource(queryResource, resource)) { // queryresource is resource template
				matchedResource.add(resource);
			}
		}
		return matchedResource;
	}

	public ArrayList<String> doClientQuery(Query query, ServerModel server) {
		ArrayList<String> result = new ArrayList<String>();

		if (checkServerQuery(query) != null) {
			result.add(checkServerQuery(query));
			return result;
		} else {
			NormalResponse nr = new NormalResponse("success");
			result.add(nr.toJSON());
			ArrayList<Resource> matchedResource = resourceMatch(
					query.getResource(), server.resourceList);
			for (Resource resource : matchedResource) {
				result.add(resource.toJSON());
			}

			if (query.isRelay()) {
				// TODO: Connect with other and add result to it
				// Check which servers are available
				Query relayQuery = new Query();
				relayQuery.fromJSON(query.toJSON());
				relayQuery.setRelay(false);
				relayQuery.getResource().channel = "";
				relayQuery.getResource().owner = "";
				String forwardQuery = relayQuery.toJSON();
				for (int i = 0; i < server.secureServerList.size(); i++) {
					ServerModel tempServer = server.secureServerList.get(i);
					if (server.hostName.equals(tempServer.hostName)
							&& server.sport == tempServer.sport) {
						continue;
					}
					Client.doSendSecure(tempServer.hostName, tempServer.sport,
							forwardQuery, result, Log.debug);
				}
			}
			result.add("{\"resultSize\":" + (result.size() - 1) + "}");
		}
		return result;
	}
	

	public ArrayList<String> doClientSubscribe(Subscribe subscribe,
			ServerModel server, ClientModel client) {
		ArrayList<String> result = new ArrayList<String>();

		// TODO: checkServerQuery(query)

		SubscribeResponse sr = new SubscribeResponse("success",
				subscribe.getId());
		result.add(sr.toJSON());

		// first check if already exists subscribed resource in the
		// resourceList, just like query.
		ArrayList<Resource> matchedResource = resourceMatch(
				subscribe.getResource(), server.resourceList);
		for (Resource resource : matchedResource) {
			result.add(resource.toJSON());
		}
		subscribe.setClient(client);
		subscribe.setNumOfHits(matchedResource.size());
		server.addDelSubscribe(subscribe, true);

		if (subscribe.isRelay()) {
			Subscribe relaySubscribe = new Subscribe();
			relaySubscribe.fromJSON(subscribe.toJSON());
			relaySubscribe.setId(subscribe.getId());
			relaySubscribe.setRelay(false);

			for (ServerModel forwardServer : server.secureServerList) {
				if (server.hostName.equals(forwardServer.hostName)
						&& server.sport == forwardServer.sport) {
					continue;
				}
				ExecutorService pool = Executors.newCachedThreadPool();
				pool.execute(new Runnable() {
					@Override
					public void run() {
						forwardSubscribe(client, forwardServer,
								relaySubscribe.toJSON());
					}
				});
			}
		}

		return result;
	}

	public void forwardSubscribe(ClientModel client, ServerModel forwardServer,
			String query) {
		try {
			// change to secure socket
			Socket socket = new Socket();
			String hostname = forwardServer.hostName;
			int port = forwardServer.sport;
			socket.connect(new InetSocketAddress(hostname, port),
					Config.CONNECTION_TIMEOUT);
			Log.log(Common.getMethodName(), "FINE", "SENT: " + query);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			out.writeUTF(query);

			while (true) {
				if (in.available() > 0) {
					String messageResponse = in.readUTF();
					Log.log(Common.getMethodName(), "FINE", "RECEIVED: "
							+ messageResponse);
					SubscribeResponse sr = new SubscribeResponse();
					sr.fromJSON(messageResponse);
					if (sr.getResponse().equals("success")) {
						DataOutputStream outClient = new DataOutputStream(
								client.socket.getOutputStream());
						while (true) {
							String message = in.readUTF();
							Log.log(Common.getMethodName(), "FINE",
									"RECEIVED: " + message);
							ArrayList<String> resultSet = new ArrayList<String>();
							resultSet.add(message);
							for (int i = 0; i < resultSet.size(); i++) {
								outClient.writeUTF(resultSet.get(i));
								Log.log(Common.getMethodName(), "FINE",
										"SENDING: " + resultSet.get(i));
							}
						}
					}
					break;
				}
			}
			// in.close();
			// out.flush();
			// out.close();
			// socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> doClientUnsubscribe(Unsubscribe unsubscribe,
			ServerModel server, ClientModel client) {
		ArrayList<String> result = new ArrayList<String>();
		String id = unsubscribe.getId();
		Subscribe subscribe = server.removeSubscribe(id);
		if (subscribe == null) {
			return null;
		}
		int numOfHits = subscribe.getNumOfHits();
		if (server.secureServerList != null) {
			for (ServerModel subscribedServer : server.secureServerList) {
				if (server.hostName.equals(subscribedServer.hostName)
						&& server.sport == subscribedServer.sport) {
					continue;
				}
				numOfHits += getRelayNumOfHits(subscribedServer,
						unsubscribe.toJSON(), Log.debug);
			}
		}
		result.add("{\"resultSize\":" + numOfHits + "}");
		return result;
	}

	public int getRelayNumOfHits(ServerModel forwardServer, String query,
			boolean printLog) {
		int relayNumOfHits = 0;
		Socket socket = new Socket();
		String hostname = forwardServer.hostName;
		int port = forwardServer.sport;
		try {
			socket.connect(new InetSocketAddress(hostname, port),
					Config.CONNECTION_TIMEOUT);
			Log.log(Common.getMethodName(), "FINE", "SENT: " + query);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			out.writeUTF(query);
			while (true) {
				if (in.available() > 0) {
					String messageResponse = in.readUTF();
					Log.log(Common.getMethodName(), "FINE", "RECEIVED: "
							+ messageResponse);
					org.json.JSONObject json = new org.json.JSONObject(
							messageResponse);
					relayNumOfHits = json.getInt("resultSize");
					if (messageResponse.contains("resultSize")) {
						break;
					}
				}
			}
			// in.close();
			// out.flush();
			// out.close();
			// socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return relayNumOfHits;
	}

	public ArrayList<String> doClientRemove(Remove remove, ServerModel server) {
		ArrayList<String> result = new ArrayList<String>();

		if (checkServerRemove(remove) != null) {
			result.add(checkServerRemove(remove));
			return result;
		} else {

			int status = server.addDelResource(remove.getResource(), false);
			if (status > 0) {
				NormalResponse nr = new NormalResponse("success");
				result.add(nr.toJSON());
			} else { // 3
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.REMOVE_RESOURCE_NOT_EXIST);
				result.add(nr.toJSON());
			}
		}
		return result;
	}

	public ArrayList<String> doClientShare(Share share, ServerModel server) {
		ArrayList<String> result = new ArrayList<String>();

		if (checkServerShare(share) != null) {
			result.add(checkServerShare(share));
			return result;
		} else {

			// System.out.println(share.getSecret()+"--"+server.secret);
			if (share.getSecret() == null
					|| (share.getSecret() != null && !share.getSecret().equals(
							server.secret))) { // error 5
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.SHARE_SECRET_INCORRECT);
				result.add(nr.toJSON());
				return result;
			}
			// Check if the resource uri is a file which exists
			String uri = share.getResource().uri;
			if (uri.startsWith("file:")) {
				uri = uri.replace("file:", "");
			}
			File f = new File(uri);
			// System.out.println(share.getResource().uri+" "+f.exists()+" "+f.isDirectory()+" ");
			if (!(f.exists() && !f.isDirectory())) {
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.PUBLISH_REMOVE_RESOURCE_INCORRECT);
				result.add(nr.toJSON());
				return result;
			}
			int status = server.addDelResource(share.getResource(), true);
			if (status > 0) {
				NormalResponse nr = new NormalResponse("success");
				result.add(nr.toJSON());
			} else { // error 4
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.SHARE_BROKEN);
				result.add(nr.toJSON());
			}
		}
		return result;
	}

	// Send using the EXCHANGE request to other servers in server.serverList
	public ArrayList<String> doServerExchange(ServerModel server) {

		// Check which servers are available
		for (int i = 0; i < server.secureServerList.size(); i++) {
			ServerModel tempServer = server.secureServerList.get(i);
			// System.out.println(server.hostname+" "+(tempServer.hostname)+" "+server.port+" "+tempServer.port);
			if (server.hostName.equals(tempServer.hostName)
					&& server.sport == tempServer.sport) {
				continue;
			}
			// Log.log(Common.getMethodName(), "INFO",
			// "Starting server exchange with:" + tempServer.hostname +":"+
			// tempServer.port);
			// java.net.ConnectException Then Delete the address (Add a return
			// to doSend)
			// TODO: Needed test
			boolean success = Client.doSendSecure(tempServer.hostName,
					tempServer.sport, Common.queryExample, null, false);
			if (!success) {
				// Log.log(Common.getMethodName(), "INFO",
				// "Server unreachable, deleting server from list:" +
				// tempServer.hostname +":"+ tempServer.port);
				server.addDelServerSecure(server.secureServerList.get(i), false);
				// System.out.println("Server List in server after DELETE:"+server.toServerListJson());
				i--;
			}
			// Log.log(Common.getMethodName(), "INFO",
			// "Finished server exchange with:" + tempServer.hostname +":"+
			// tempServer.port);
		}
		if (server.secureServerList.size() > 0) {
			String query = server.toSecureServerListJson();
			int randomNum = ThreadLocalRandom.current().nextInt(0,
					server.secureServerList.size());
			ServerModel tempServer = server.secureServerList.get(randomNum);
			if (server.hostName.equals(tempServer.hostName)
					&& server.sport == tempServer.sport) {
				return null;
			}
			boolean success = Client.doSendSecure(tempServer.hostName,
					tempServer.sport, query, null, Log.debug);
			if (!success) {
				server.addDelServerSecure(server.secureServerList.get(randomNum), false);
			}
		}
		// System.out.println("In doServerExchange" + query);
		/*
		 * for (int i = 0; i < server.serverList.size(); i++) { ServerModel
		 * tempServer = server.serverList.get(i);
		 * //System.out.println(server.hostname
		 * +" "+(tempServer.hostname)+" "+server.port+" "+tempServer.port);
		 * if(server
		 * .hostname.equals(tempServer.hostname)&&server.port==tempServer.port){
		 * continue; } //Log.log(Common.getMethodName(), "INFO",
		 * "Starting server exchange with:" + tempServer.hostname +":"+
		 * tempServer.port); // java.net.ConnectException Then Delete the
		 * address (Add a return to doSend) //TODO: Needed test boolean success
		 * = Client.doSend(tempServer.hostname, tempServer.port, query,null);
		 * if(!success){ //Log.log(Common.getMethodName(), "INFO",
		 * "Server unreachable, deleting server from list:" +
		 * tempServer.hostname +":"+ tempServer.port);
		 * server.addDelServer(server.serverList.get(i),false);
		 * //System.out.println
		 * ("Server List in server after DELETE:"+server.toServerListJson());
		 * i--; } //Log.log(Common.getMethodName(), "INFO",
		 * "Finished server exchange with:" + tempServer.hostname +":"+
		 * tempServer.port); }
		 */
		return null;
	}

	/**
	 * checking publish parameters are valid or not 1. resource is not a JSON
	 * --- invalid resource 2. uri is not valid or "" ---- cannot publish
	 * resource 3. owner just "*" and string contains "/0" and missing
	 * resource-----missing resource 4. the same channel ,the same uri the same
	 * owner -- cannot publish resource
	 */
	public String checkServerPublish(Publish publish) {
		Resource resource = publish.getResource();
		if (resource.uri.equals("") || !resource.isUriPublish()) { // 2
			NormalResponse nr = new NormalResponse("error",
					ErrorMessage.PUBLISH_BROKEN);
			return nr.toJSON();
		} else if (!resource.isOwnerValid() || !resource.isArgValid()) { // 3
			NormalResponse nr = new NormalResponse("error",
					ErrorMessage.PUBLISH_REMOVE_RESOURCE_MISSING);
			return nr.toJSON();
		}
		return null;
	}

	/**
	 * checking remove parameters are valid or not 1. missing resource or owner
	 * just "*" or string is not valid ----missing resource 2. uri is not valid
	 * or "" ---- cannot remove resource 3. the resource did not exist ---
	 * cannot remove resource 4. resource is not a JSON --- invalid resource
	 */
	public String checkServerRemove(Remove remove) {
		Resource resource = remove.getResource();
		if (resource.uri.equals("") || !resource.isUriVaild()) { // 2
			NormalResponse nr = new NormalResponse("error",
					ErrorMessage.REMOVE_RESOURCE_NOT_EXIST);
			return nr.toJSON();
		} else if (!resource.isOwnerValid() || !resource.isArgValid()) { // 1
			NormalResponse nr = new NormalResponse("error",
					ErrorMessage.PUBLISH_REMOVE_RESOURCE_MISSING);
			return nr.toJSON();
		}
		return null;
	}

	/**
	 * checking query parameters are valid or not 1. resource is not a JSON ----
	 * invalid resourceTemplate 2. uri is not valid or missing resource or owner
	 * is "*" or string not valid ---- missing resourceTemplate
	 */
	public String checkServerQuery(Query query) {
		Resource resource = query.getResource();
		if (!resource.isUriVaild() || !resource.isArgValid()
				|| !resource.isOwnerValid()) { // 2
			NormalResponse nr = new NormalResponse("error",
					ErrorMessage.QUERY_FETCH_EXCHANGE_RESOURCETEMPLATE_MISSING);
			return nr.toJSON();
		}
		return null;
	}

	/**
	 * checking fetch parameters are valid or not 1. resource is not a JSON ----
	 * invalid resourceTemplate 2. uri is not valid or "" or missing resource or
	 * owner is "*" or string not valid ---- missing resourceTemplate
	 */
	public String checkServerFetch(Fetch fetch) {
		Resource resource = fetch.getResource();
		if (resource.uri.equals("") || // !resource.isUriShare()||
				!resource.isArgValid() || !resource.isOwnerValid()) { // 2
			NormalResponse nr = new NormalResponse("error",
					ErrorMessage.QUERY_FETCH_EXCHANGE_RESOURCETEMPLATE_MISSING);
			return nr.toJSON();
		}
		return null;
	}

	/**
	 * checking share parameters are valid or not 1. resource is not a JSON ----
	 * invalid resource 2. uri is not valid or "" ---- cannot share resource 3.
	 * owner is "*" or string not valid or missing secret or missing
	 * resource---- missing resourceTemplate 4. the same channel the same uri
	 * different owner --- cannot share resource 5. incorrect secret ---
	 * incorrect secret
	 */
	public String checkServerShare(Share share) {
		Resource resource = share.getResource();
		String secret = share.getSecret();
		if (resource.uri.equals("") || (!resource.isUriShare())) { // 2
																	// ||(!resource.isUriShare())
			NormalResponse nr = new NormalResponse("error",
					ErrorMessage.SHARE_BROKEN);
			return nr.toJSON();
		} else if (!resource.isOwnerValid() || !resource.isArgValid()) { // 3
			NormalResponse nr = new NormalResponse("error",
					ErrorMessage.SHARE_MISSING);
			return nr.toJSON();
		}
		return null;
	}

	/**
	 * checking exchange parameters are valid or not 1. serverlist is not a JSON
	 * or missing (TO DO) ---- missing or invalid server list 2. server record
	 * is found to be valid (TO DO) --- missing resourceTemplate
	 */
	public String checkServerExchange(Exchange exchange) {

		return null;
	}

	public String checkValidMissing(String op, String json) {
		String result = null;
		switch (op) {
		case "PUBLISH":
		case "REMOVE":
			if (checkResource(json, "resource") == -2) {
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.PUBLISH_REMOVE_RESOURCE_MISSING);
				result = nr.toJSON();
			} else if (checkResource(json, "resource") == -3) {
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.PUBLISH_REMOVE_RESOURCE_INCORRECT);
				result = nr.toJSON();
			}
			break;
		case "SHARE":
			if (checkResource(json, "resource") == -2) {
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.SHARE_MISSING);
				result = nr.toJSON();
			} else if (checkResource(json, "resource") == -3) {
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.PUBLISH_REMOVE_RESOURCE_INCORRECT);
				result = nr.toJSON();
			}
			break;
		case "QUERY":
		case "FETCH":
		case "SUBSCRIBE":
			if (checkResource(json, "resourceTemplate") == -2) {
				NormalResponse nr = new NormalResponse(
						"error",
						ErrorMessage.QUERY_FETCH_EXCHANGE_RESOURCETEMPLATE_MISSING);
				result = nr.toJSON();
			} else if (checkResource(json, "resourceTemplate") == -3) {
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.QUERY_FETCH_RESOURCETEMPLATE_INVALID);
				result = nr.toJSON();
			}
			break;
		case "EXCHANGE":
			if (checkServerListMissing(json)) {
				NormalResponse nr = new NormalResponse("error",
						ErrorMessage.EXCHANGE_SERVERLIST_MISSING);
				result = nr.toJSON();
			}
			break;
		default:
			result = null;
			break;
		}
		return result;
	}

	public boolean checkServerListMissing(String json) {
		JSONParser parser = new JSONParser();
		JSONObject command = null;
		try {
			command = (JSONObject) parser.parse(json);// invalid command
		} catch (Exception e) {
			return true;
		}
		String resource = "";
		try {
			resource = command.get("serverList").toString();
		} catch (Exception e) {
			return true;

		}
		return false;
	}

	/**
	 * This method returns the validation of resource template.
	 * 
	 * @param json
	 *            The json string which is needed to check. key In PUBLISH, it
	 *            is "resource"; in others, it is "resourceTemplate"
	 * 
	 * @return -1(The input json string is not in a json form) -2(The json
	 *         string is in json form but does not have
	 *         "resourceTemplate"/"resource") -3(The json string is in json
	 *         from, it has "resourceTemplate"/"resource", but
	 *         "resourceTemplate"/"resource" is not in json form) 1(The json
	 *         string has valid "resourceTemplate"/"resource")
	 */
	public static int checkResource(String json, String key) {
		JSONParser parser = new JSONParser();
		JSONObject command = null;
		try {
			command = (JSONObject) parser.parse(json);// invalid command
		} catch (Exception e) {
			return -1;
		}
		String resource = "";
		try {
			resource = command.get(key).toString();
		} catch (Exception e) {
			return -2;

		}
		try {
			JSONObject reTemplate = (JSONObject) parser.parse(resource);
		} catch (Exception e) {
			return -3;

		}
		return 1;

	}

}
