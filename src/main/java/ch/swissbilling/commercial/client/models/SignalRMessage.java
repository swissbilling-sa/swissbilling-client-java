package ch.swissbilling.commercial.client.models;

public class SignalRMessage {
	public int type;
	public String target;
	public String[] arguments;
	public String invocationId;
	public Object result;
	public String error;
}
