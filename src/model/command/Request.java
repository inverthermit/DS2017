package model.command;

public abstract class Request {
	public String command;
	public abstract String toJSON();
}
