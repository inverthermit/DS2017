package tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Tim Luo on 2017/3/27.
 */
public class ErrorMessage {
	public static final String GENERIC_INVALID = "invalid command";
	public static final String GENERIC_MISS_INCORRECT = "missing or incorrect type for command";
	public static final String PUBLISH_BROKEN = "cannot publish resource";
	
	public ErrorMessage() {
		
	}
	
}
