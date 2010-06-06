package tera.messaging;

import tera.protocol.Node;

public class RandomWalkReplyMessage extends ConversationMessage
{
	private static final long serialVersionUID = 1L;
	
	private Node response;
	
	public RandomWalkReplyMessage(Node source, Node destination, long conversationId, Node response)
	{
		super(source, destination, conversationId);
		this.response = response;
	}
	
	public RandomWalkReplyMessage(RandomWalkRequestMessage request, Node response)
	{
		this(request.getDestination(), request.getInitiator(), request.getConversationId(), response);
	}

	public Node getResponse()
	{
		return response;
	}
}
