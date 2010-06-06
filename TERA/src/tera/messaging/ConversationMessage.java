package tera.messaging;

import tera.protocol.Node;


public class ConversationMessage extends TeraMessage
{
	private static final long serialVersionUID = 1L;

	private long conversationId;

	public ConversationMessage(Node source, Node destination, long conversationId)
	{
		super(source, destination);
		this.conversationId = conversationId;
	}
	
	public long getConversationId()
	{
		return conversationId;
	}
}
