package ro.cs.pub.pubsub.tera.randomWalk.message;

import ro.cs.pub.pubsub.message.MessageContent;
import jade.core.AID;

/**
 * Contains the query and the state of the message.
 */
public class RandomWalkRequest implements MessageContent {
	private static final long serialVersionUID = 1L;

	private final AID origin;
	private final int ttl;
	private final RandomWalkQuery query;

	public RandomWalkRequest(AID origin, int ttl, RandomWalkQuery query) {
		this.origin = origin;
		this.ttl = ttl;
		this.query = query;
	}

	public AID getOrigin() {
		return origin;
	}

	public int getTTL() {
		return ttl;
	}

	public RandomWalkQuery getQuery() {
		return query;
	}

	public RandomWalkRequest decreaseTTL() {
		return new RandomWalkRequest(origin, ttl - 1, query);
	}
}
