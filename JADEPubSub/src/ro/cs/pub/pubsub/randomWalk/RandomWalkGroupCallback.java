package ro.cs.pub.pubsub.randomWalk;

import java.util.Collection;

import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResult;

public interface RandomWalkGroupCallback {
	/**
	 * Called when all results have received or when all timeouts have expired.
	 * 
	 * @param results
	 */
	void onEnd(Collection<RandomWalkResult> results);
}
