package tool;

public class Log{
	public static boolean debug = false;
	public static void log(String msg){
		if(debug){
			System.out.println(msg);
		}
	}
	public static void log(String className, String level, String content){
		if(debug){
			//TODO:get dd/MM/yyyy hh:mm:ss.msmsms
			String time = "";
			System.out.println(
					time+" - ["+className+"] - ["+level+"] - "+content 
					);
		}
	}
}