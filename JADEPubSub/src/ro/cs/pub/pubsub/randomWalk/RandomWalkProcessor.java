package ro.cs.pub.pubsub.randomWalk;

import ro.cs.pub.pubsub.randomWalk.message.RandomWalkRequest;

/**
 * Processes a random walk request and creates a
 * {@link RandomWalkProcessingResult}.
 */
public interface RandomWalkProcessor {

	/**
	 * Processes a request from a peer.
	 * 
	 * @param request
	 * @return the processing result
	 */
	RandomWalkProcessingResult process(RandomWalkRequest request);
}
