package tera.messaging;

import java.io.Serializable;

import tera.protocol.Node;


public class TeraMessage implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected Node source, destination;
	protected long timestamp;

	public TeraMessage(Node source, Node destination)
	{
		this.source = source;
		this.destination = destination;
		this.timestamp = System.currentTimeMillis();
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public Node getSource()
	{
		return source;
	}

	public Node getDestination()
	{
		return destination;
	}
}
