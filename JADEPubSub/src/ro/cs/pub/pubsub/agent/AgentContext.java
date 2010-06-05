package ro.cs.pub.pubsub.agent;

import ro.cs.pub.pubsub.message.MessageFactory;

public class AgentContext {
	private final MessageFactory messageFactory;

	public AgentContext(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}

	public MessageFactory getMessageFactory() {
		return messageFactory;
	}
}
