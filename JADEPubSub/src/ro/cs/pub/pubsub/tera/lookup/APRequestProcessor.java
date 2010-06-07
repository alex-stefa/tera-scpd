package ro.cs.pub.pubsub.tera.lookup;

import jade.core.AID;
import ro.cs.pub.pubsub.randomWalk.RandomWalkProcessingResult;
import ro.cs.pub.pubsub.randomWalk.RandomWalkProcessor;
import ro.cs.pub.pubsub.randomWalk.message.AgentResult;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResponse;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

/**
 * Processes a random walk request and creates a
 * {@link RandomWalkProcessingResult}.
 */
public class APRequestProcessor implements RandomWalkProcessor {
	private final TeraAgent agent;

	public APRequestProcessor(TeraAgent agent) {
		this.agent = agent;
	}

	@Override
	public RandomWalkProcessingResult process(RandomWalkRequest request) {
		RandomWalkQuery query = request.getQuery();

		// find a peer for given topic
		AID peer = agent.getAccessPointManager(). //
				get(((TopicQuery) query).getTopic());

		if (peer != null) {
			return RandomWalkProcessingResult.createSendToOrigin( //
					new RandomWalkResponse(new AgentResult(agent.getAID())));
		}

		if (request.getTTL() > 0) {
			return RandomWalkProcessingResult.createForward(request);
		}

		return RandomWalkProcessingResult.createForget();
	}
}
