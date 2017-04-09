package model.command;

public abstract class Request {

	public abstract String toJSON();

	public abstract void fromJSON(String json);
}
