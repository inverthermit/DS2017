/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package tool;

import java.security.SecureRandom;
import java.util.*;

import model.Resource;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * This class is for commonly used constants and functions.
 * 
 * @author Group - Alpha Panthers
 * @version 1.1
 */
public class Common {
	public static String SECRET = "2os41f58vkd9e1q4ua6ov5emlv";
	public static final String[] LOGGIN_LEVEL = { "SEVERE", "WARNING", "INFO",
			"CONFIG", "FINE", "FINER", "FINEST" };
	public static final String[] BASIC_OP = { "EXCHANGE", "FETCH", "SHARE",
			"PUBLISH", "QUERY", "REMOVE", "SUBSCRIBE" };
	public static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static String queryExample = "{\"command\": \"QUERY\",    \"reply\": false, \"resourceTemplate\": {\"name\": \"randomname\",\"tags\": [],\"description\": \"\",\"uri\": \"randomurl\",\"channel\": \"\",\"owner\": \"randomowner\",\"ezserver\": null}}";
	private static SecureRandom rnd = new SecureRandom();

	/**
	 * This method returns the operation type of a json string.
	 * 
	 * @param json
	 *            The json string which is needed to check.
	 * 
	 * @return String The operation name of the json. If operations are wrong,
	 *         return null.
	 */
	public static String getOperationfromJson(String json) {
		JSONParser parser = new JSONParser();
		JSONObject command = null;
		try {
			command = (JSONObject) parser.parse(json);// invalid command
		} catch (Exception e) {
			return null;
		}
		String resource = "";
		try {
			resource = command.get("command").toString();
		} catch (Exception e) {
			return null;

		}
		for (int i = 0; i < BASIC_OP.length; i++) {
			if (resource.equals(BASIC_OP[i])) {
				return BASIC_OP[i];
			}
		}
		return null;
	}

	/**
	 * This method returns random string.
	 * 
	 * @param len
	 *            Length of the string.
	 * 
	 * @return Random string.
	 */
	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	/**
	 * This method returns the method name of which calls this method.
	 * 
	 * @return Method name string.
	 */
	public static String getMethodName() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[2];// coz 0th will be getStackTrace so
											// 1st
		String methodName = "EZShare." + e.getClassName() + "."
				+ e.getMethodName();
		return methodName;
	}

	/**
	 * This method returns whether an array contains another.
	 * 
	 * @param arrSmall
	 *            The array which should be contained in arrBig arrBig The array
	 *            which should contains arrSmall
	 * 
	 * @return Random string.
	 */
	public static boolean arrayInArray(String[] arrSmall, String[] arrBig) {
		/*
		 * System.out.println("arrSmall"); for(int i=0;i<arrSmall.length;i++){
		 * System.out.println(arrSmall[i]); } System.out.println("arrBig");
		 * for(int i=0;i<arrBig.length;i++){ System.out.println(arrBig[i]); }
		 * if(arrSmall.length>arrBig.length){ return false; }
		 */
		for (int i = 0; i < arrSmall.length; i++) {
			boolean containFlag = false;
			for (int j = 0; j < arrBig.length; j++) {
				if (arrSmall[i].equals(arrBig[j])) {
					containFlag = true;
					break;
				}
			}
			if (containFlag == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method returns current unix time stamp.
	 * 
	 * @return Current unix time stamp.
	 */
	public static long getCurrentSecTimestamp() {
		Date date = new Date();
		long unixTime = (long) date.getTime() / 1000;
		// System.out.println(unixTime );
		return unixTime;
	}
	
	public static boolean isMatchedResource(Resource queryResource, Resource resource) {
		/*
		 * 1.(The template channel equals (case sensitive) the resource
		 * channel AND 2.If the template contains an owner that is not "",
		 * then the candidate owner must equal it (case sensitive) AND 3.Any
		 * tags present in the template also are present in the candidate
		 * (case insensitive) AND 4.If the template contains a URI then the
		 * candidate URI matches (case sensitive) AND 5.(The candidate name
		 * contains the template name as a substring (for non "" template
		 * name) OR 6.The candidate description contains the template
		 * description as a substring (for non "" template descriptions) OR
		 * 7.The template description and name are both ""))
		 */
		/*
		 * System.out.println("1"+queryResource.channel.equals(
		 * resource.channel));
		 * System.out.println("2"+(queryResource.owner.equals("") ||
		 * ((!queryResource.owner.equals("")) &&
		 * queryResource.owner.equals(resource.owner)))); System.out
		 * .println("3"+(Common.arrayInArray(queryResource.tags,
		 * resource.tags))); System.out.println("4"+ (queryResource
		 * .uri.equals("")||((!queryResource.uri .equals(""))&&query
		 * .getResource().uri.equals(resource.uri)))); System.out.println
		 * ("5"+resource.name.contains(queryResource.name)); System
		 * .out.println("6"+(queryResource.description.equals("") ||
		 * resource.description.contains(queryResource.description )));
		 * System.out.println("7"+(queryResource.description.equals ("") &&
		 * queryResource.name.equals("")));
		 */
		if (queryResource.channel.equals(resource.channel)
				&& // 1
				(queryResource.owner.equals("") || ((!queryResource.owner
						.equals("")) && queryResource.owner
						.equals(resource.owner))) && // 2
				(Common.arrayInArray(queryResource.tags, resource.tags)) && // 3
				(queryResource.uri.equals("") || ((!queryResource.uri
						.equals("")) && queryResource.uri
						.equals(resource.uri))) && // 4
				(!queryResource.name.equals("")
						&& resource.name.contains(queryResource.name) || // 5
						(!queryResource.description.equals("") && resource.description
								.contains(queryResource.description)) || // 6
				(queryResource.description.equals("") && queryResource.name
						.equals(""))// 7
				)) {
			return true;
		}
		return false;
	}

}
