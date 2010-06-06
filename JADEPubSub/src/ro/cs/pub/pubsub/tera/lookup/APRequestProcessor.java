package ro.cs.pub.pubsub.tera.lookup;

import jade.core.AID;
import ro.cs.pub.pubsub.randomWalk.ProcessingResult;
import ro.cs.pub.pubsub.randomWalk.RequestProcessor;
import ro.cs.pub.pubsub.randomWalk.message.AgentResult;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResponse;
import ro.cs.pub.pubsub.randomWalk.message.TopicQuery;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

/**
 * Processes a random walk request and creates a {@link ProcessingResult}.
 */
public class APRequestProcessor implements RequestProcessor {
	private final TeraAgent agent;

	public APRequestProcessor(TeraAgent agent) {
		this.agent = agent;
	}

	@Override
	public ProcessingResult process(RandomWalkRequest request) {
		RandomWalkQuery query = request.getQuery();
		
		// find a peer for given topic
		AID peer = agent.getAccessPointProvider(). //
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
