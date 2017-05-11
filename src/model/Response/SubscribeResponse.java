package model.Response;

import com.google.gson.Gson;

public class SubscribeResponse extends NormalResponse {
	private String id;

	public SubscribeResponse() {

	}

	public SubscribeResponse(String successResponce, String id) {
		super(successResponce);
		this.id = id;
	}

	public void fromJSON(String json) {
		Gson gson = new Gson();
		SubscribeResponse obj = gson.fromJson(json, SubscribeResponse.class);
		this.setResponse(obj.getResponse());
		this.setErrorType(obj.getErrorType());
		this.id = obj.id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
