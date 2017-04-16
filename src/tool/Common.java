package tool;

import java.security.SecureRandom;

/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class Common {
	public static final String[] LOGGIN_LEVEL = {"SEVERE","WARNING","INFO","CONFIG","FINE","FINER","FINEST"};
	
	public static final String[] BASIC_OP = {"EXCHANGE","FETCH","SHARE","PUBLISH","QUERY","REMOVE"};
	static SecureRandom rnd = new SecureRandom();
	public static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static String getOperationfromJson(String json){
		for(int i=0;i<BASIC_OP.length;i++){
			if(json.contains("\"command\": \""+BASIC_OP[i]+"\"")){
				return BASIC_OP[i];
			}
		}
		return null;
	}
	public String randomString( int len ){
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
	
}
