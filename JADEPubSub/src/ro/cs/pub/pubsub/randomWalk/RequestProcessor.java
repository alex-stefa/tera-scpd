package ro.cs.pub.pubsub.randomWalk;

import ro.cs.pub.pubsub.randomWalk.message.RandomWalkRequest;

/**
 * Processes a random walk request and creates a {@link ProcessingResult}.
 */
public interface RequestProcessor {

	/**
	 * Processes a request from a peer.
	 * 
	 * @param request
	 * @return the processing result
	 */
	ProcessingResult process(RandomWalkRequest request);
}
