/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package tool;

import java.security.SecureRandom;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Common {
	public static String SECRET = "2os41f58vkd9e1q4ua6ov5emlv";
	public static final String[] LOGGIN_LEVEL = {"SEVERE","WARNING","INFO","CONFIG","FINE","FINER","FINEST"};
	public static final String[] BASIC_OP = {"EXCHANGE","FETCH","SHARE","PUBLISH","QUERY","REMOVE"};
	public static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static String queryExample = "{\"command\": \"QUERY\",    \"reply\": false, \"resourceTemplate\": {\"name\": \"randomname\",\"tags\": [],\"description\": \"\",\"uri\": \"randomurl\",\"channel\": \"\",\"owner\": \"randomowner\",\"ezserver\": null}}";
	private static SecureRandom rnd = new SecureRandom();
	
	public static String getOperationfromJson(String json){
		JSONParser parser = new JSONParser();
		JSONObject command = null;
		try {
			command = (JSONObject) parser.parse(json);//invalid command
		} 	catch (Exception e) {
			return null;
		}
		String resource = "";
		try {
			resource = command.get("command").toString();
		} 	catch (Exception e) {
			return null;
			
		}
		for(int i=0;i<BASIC_OP.length;i++){
			if(resource.equals(BASIC_OP[i])){
				return BASIC_OP[i];
			}
		}
		return null;
	}
	public static String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}
	
	public static String getMethodName(){
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	    StackTraceElement e = stacktrace[2];//coz 0th will be getStackTrace so 1st
	    String methodName = "EZShare."+e.getClassName()+"."+e.getMethodName();
	    return methodName;
	}
	
	public static boolean arrayInArray(String[] arrSmall, String[] arrBig){
		if(arrSmall.length>arrBig.length){
			return false;
		}
		for(int i=0;i<arrSmall.length;i++){
			boolean containFlag = false;
			for(int j=0;j<arrBig.length;j++){
				if(arrSmall[i].equals(arrBig)){
					containFlag = true;
					break;
				}
			}
			if(containFlag==false){
				return false;
			}
		}
		return true;
	}
	
	public static long getCurrentSecTimestamp(){
		Date date = new Date();
	    long unixTime = (long) date.getTime()/1000;
	    //System.out.println(unixTime );
	    return unixTime;
	}
	
}
