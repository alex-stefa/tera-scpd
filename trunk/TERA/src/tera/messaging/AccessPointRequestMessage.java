package tera.messaging;

import tera.protocol.Node;


public class AccessPointRequestMessage extends RandomWalkRequestMessage
{
	private static final long serialVersionUID = 1L;
	
	private long topicId;
	
	public AccessPointRequestMessage(Node source, Node destination,
			Node initiator, int timeToLive, long conversationId, long topicId)
	{
		super(source, destination, initiator, timeToLive, conversationId);
		this.topicId = topicId;
	}
	
	public long getTopicId()
	{
		return topicId;
	}
	
	@Override
	public AccessPointRequestMessage getForwardMessage(Node forwardTo)
	{
		return new AccessPointRequestMessage(this.destination, forwardTo,
				this.getInitiator(), this.getTimeToLive() - 1, this.getConversationId(), this.getTopicId());  
	}
}
