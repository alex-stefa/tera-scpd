package tera.messaging;

import java.io.Serializable;


public class GreetingMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String sourceIP;
	private int sourceListenPort;
	private String message;

	public GreetingMessage(String sourceIP, int sourceListenPort, String message)
	{
		this.sourceIP = sourceIP;
		this.sourceListenPort = sourceListenPort;
		this.message = message;
	}
	
	public GreetingMessage(String sourceIP, int sourceListenPort)
	{
		this(sourceIP, sourceListenPort, "Hello TERA Peer!");
	}
	
	public String message()
	{
		return message;
	}
	
	public String sourceIP()
	{
		return sourceIP;
	}
	
	public int sourceListenPort()
	{
		return sourceListenPort;
	}
}
