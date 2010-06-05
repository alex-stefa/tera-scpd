package ro.cs.pub.pubsub.tera.behaviour.randomWalk.processor;

import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkResponse;

/**
 * Contains the result of a request processing operation.
 */
public class ProcessingResult {
	public enum Type {FORWARD, SEND_TO_ORIGIN, FORGET};
	
	public static ProcessingResult createForward(RandomWalkRequest request) {
		return new ProcessingResult(request, Type.FORWARD);
	}
	
	public static ProcessingResult createSendToOrigin(RandomWalkResponse response) {
		return new ProcessingResult(response, Type.FORWARD);
	}
	
	public static ProcessingResult createForget() {
		return new ProcessingResult(null, Type.FORWARD);
	}
	
	private final MessageContent content;
	private final Type type;
	
	private ProcessingResult(MessageContent content, Type type) {
		this.content = content;
		this.type = type;
	}
	
	public MessageContent getContent() {
		return content;
	}
	
	public Type getType() {
		return type;
	}
}
