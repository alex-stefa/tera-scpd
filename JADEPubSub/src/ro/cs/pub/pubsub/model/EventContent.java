package ro.cs.pub.pubsub.model;

import ro.cs.pub.pubsub.message.MessageContent;

public class EventContent implements MessageContent {
	private static final long serialVersionUID = 1L;

	private String message;

	public EventContent(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return message;
	}
}
