package ro.cs.pub.pubsub.agent;

import ro.cs.pub.pubsub.message.MessageFactory;

public class Context {
	private final MessageFactory messageFactory;

	public Context(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}
	
	public MessageFactory getMessageFactory() {
		return messageFactory;
	}
}
