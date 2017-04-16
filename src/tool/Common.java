package tool;
/**
	 * Created by Tim Luo on 2017/3/27.
	 */
public class Common {
	public static final String[] BASIC_OP = {"EXCHANGE","FETCH","SHARE","PUBLISH","QUERY","REMOVE"};
	public static String getOperationfromJson(String json){
		for(int i=0;i<BASIC_OP.length;i++){
			if(json.contains("\"command\": \""+BASIC_OP[i]+"\"")){
				return BASIC_OP[i];
			}
		}
		return null;
	}
	
}
