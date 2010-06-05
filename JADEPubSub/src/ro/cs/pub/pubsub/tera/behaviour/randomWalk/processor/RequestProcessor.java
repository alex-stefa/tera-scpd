package ro.cs.pub.pubsub.tera.behaviour.randomWalk.processor;

import jade.core.AID;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.AgentResult;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.DistanceQuery;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkResponse;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.TopicQuery;

/**
 * Processes a random walk request and creates a {@link ProcessingResult}.
 */
public class RequestProcessor {
	private final TeraAgent agent;

	public RequestProcessor(TeraAgent agent) {
		this.agent = agent;
	}

	/**
	 * Processes a request from a peer.
	 * 
	 * @param request
	 * @return the processing result
	 */
	public ProcessingResult process(RandomWalkRequest request) {
		RandomWalkQuery query = request.getQuery();

		if (query instanceof DistanceQuery) {
			return processDistanceRequest(request);
		}

		if (query instanceof TopicQuery) {
			return processTopicRequest(request);
		}

		// maybe we should throw an exception
		return ProcessingResult.createForget();
	}

	private ProcessingResult processDistanceRequest(RandomWalkRequest request) {
		if (request.getTTL() <= 0) {
			return ProcessingResult.createSendToOrigin( //
					new RandomWalkResponse(new AgentResult(agent.getAID())));
		}

		return ProcessingResult.createForward(request.decreaseTTL());
	}

	private ProcessingResult processTopicRequest(RandomWalkRequest request) {
		RandomWalkQuery query = request.getQuery();
		// find a peer for given topic
		AID peer = agent.getContext().getAccessPointProvider(). //
				get(((TopicQuery) query).getTopic());

		if (peer != null) {
			return ProcessingResult.createSendToOrigin( //
					new RandomWalkResponse(new AgentResult(agent.getAID())));
		}

		if (request.getTTL() > 0) {
			return ProcessingResult.createForward(request);
		}

		return ProcessingResult.createForget();
	}
}
