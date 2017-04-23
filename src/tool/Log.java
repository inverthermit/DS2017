package tool;
import java.text.*;
import java.util.*;
public class Log{
	public static boolean debug = false;
	public static void log(String msg){
		if(debug){
			System.out.println(msg);
		}
	}
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