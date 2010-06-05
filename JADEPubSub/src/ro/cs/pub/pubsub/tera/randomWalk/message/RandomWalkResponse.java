package ro.cs.pub.pubsub.tera.randomWalk.message;

import ro.cs.pub.pubsub.message.MessageContent;

/**
 * Contains the result of the random walk.
 */
public class RandomWalkResponse implements MessageContent {
	private static final long serialVersionUID = 1L;
	
	private final RandomWalkResult result;

	public RandomWalkResponse(RandomWalkResult result) {
		this.result = result;
	}
	
	public RandomWalkResult getResult() {
		return result;
	}
}
