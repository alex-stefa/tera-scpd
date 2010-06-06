package ro.cs.pub.pubsub.randomWalk;

import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResponse;

/**
 * Contains the result of a request processing operation.
 */
public class RandomWalkProcessingResult {
	public enum Type {
		FORWARD, SEND_TO_ORIGIN, FORGET
	};

	public static RandomWalkProcessingResult createForward(RandomWalkRequest request) {
		return new RandomWalkProcessingResult(request, Type.FORWARD);
	}

	public static RandomWalkProcessingResult createSendToOrigin(
			RandomWalkResponse response) {
		return new RandomWalkProcessingResult(response, Type.FORWARD);
	}

	public static RandomWalkProcessingResult createForget() {
		return new RandomWalkProcessingResult(null, Type.FORWARD);
	}

	private final MessageContent content;
	private final Type type;

	private RandomWalkProcessingResult(MessageContent content, Type type) {
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
