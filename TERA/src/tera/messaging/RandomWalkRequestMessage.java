package tera.messaging;

import tera.protocol.Node;


public class RandomWalkRequestMessage extends ConversationMessage
{
	private static final long serialVersionUID = 1L;
	
	private Node initiator;
	private int timeToLive;
	
	public RandomWalkRequestMessage(Node source, Node destination, Node initiator, 
			int timeToLive, long conversationId)
	{
		super(source, destination, conversationId);
		this.initiator = initiator;
		this.timeToLive = timeToLive;
	}
	
	public Node getInitiator()
	{
		return initiator;
	}
	
	public int getTimeToLive()
	{
		return timeToLive;
	}
	
	public RandomWalkRequestMessage getForwardMessage(Node forwardTo)
	{
		return new RandomWalkRequestMessage(this.destination, forwardTo,
				this.initiator, this.timeToLive - 1, this.getConversationId());  
	}
}
