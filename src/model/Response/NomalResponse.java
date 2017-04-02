package model.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NomalResponse {
	private String response;
	private String errorType;

	public NomalResponse() {
		
	}
	
	public NomalResponse(String sucResponce) {
		this.response = sucResponce;
	}
	
	public NomalResponse(String errorResponce, String errorType) {
		this.response = errorResponce;
		this.errorType = errorType;
	}
	
	
	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
}
