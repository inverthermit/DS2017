/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model.Response;

import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class NormalResponse {
	private String response;
	private String errorType;

	public NormalResponse() {
		
	}
	
	public NormalResponse(String sucResponce) {
		this.response = sucResponce;
	}
	
	public NormalResponse(String errorResponce, String errorType) {
		this.response = errorResponce;
		this.errorType = errorType;
	}
	
	
	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	public void fromJSON(String json) {
		Gson gson = new Gson();
		NormalResponse obj = gson.fromJson(json, NormalResponse.class);
		this.response = obj.response;
		this.errorType = obj.errorType;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	
}
