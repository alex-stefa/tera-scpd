package ro.cs.pub.pubsub.model;

import java.io.Serializable;

public class EventContent implements Serializable {
	private static final long serialVersionUID = 1L;

	private String message;

	public EventContent(String message) {
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
