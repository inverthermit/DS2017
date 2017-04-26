/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package tool;

import java.text.*;
import java.util.*;

public class Log{
	public static boolean debug = false;
	/**
	 * This method outputs an message.
	 * @param msg A message to be output.
	 * 
	 */
	public static void log(String msg){
		if(debug){
			System.out.println(msg);
		}
	}
	/**
	 * This method outputs an message in the form of system log.
	 * @param msg A message to be output in a log form.
	 * 
	 */
	public static void log(String className, String level, String content){
		if(debug){
			//get dd/MM/yyyy HH:mm:ss.SSS
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
			Date date = new Date();
			//System.out.println(dateFormat.format(date));
			String time = dateFormat.format(date);
			System.out.println(
					time+" - ["+className+"] - ["+level+"] - "+content 
					);
		}
	}
}