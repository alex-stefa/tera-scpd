package ro.cs.pub.pubsub.randomWalk;

import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResult;

public interface RandomWalkCallback {

	/**
	 * Called when a result is received.
	 * 
	 * @param result
	 */
	void onResult(RandomWalkResult result);

	/**
	 * Called when no result arrives in the specified timeout.
	 */
	void onTimeout();
}
