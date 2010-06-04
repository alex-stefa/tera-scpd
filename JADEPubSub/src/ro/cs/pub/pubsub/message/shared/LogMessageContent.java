package ro.cs.pub.pubsub.message.shared;

import ro.cs.pub.pubsub.message.MessageContent;

public class LogMessageContent implements MessageContent {
	private static final long serialVersionUID = 1L;

	private String message;

	public LogMessageContent(String message) {
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
