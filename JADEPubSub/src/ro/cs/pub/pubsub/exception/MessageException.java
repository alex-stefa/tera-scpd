package ro.cs.pub.pubsub.exception;

public class MessageException extends Exception {
	private static final long serialVersionUID = 1L;

	public MessageException(Exception e) {
		super(e);
	}

	public MessageException() {
	}
}
