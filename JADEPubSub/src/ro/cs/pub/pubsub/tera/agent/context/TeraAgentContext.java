package ro.cs.pub.pubsub.tera.agent.context;

import java.util.Set;

import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.agent.AgentContext;
import ro.cs.pub.pubsub.message.MessageFactory;

public class TeraAgentContext extends AgentContext {
	private final AccessPointProvider accessPointProvider;
	private final NeighborProvider neighborProvider;
	private final Set<Topic> subscribedTopics;

	public TeraAgentContext(MessageFactory messageFactory,
			AccessPointProvider accessPointProvider,
			NeighborProvider neighborProvider, Set<Topic> subscribedTopics) {
		super(messageFactory);
		this.accessPointProvider = accessPointProvider;
		this.neighborProvider = neighborProvider;
		this.subscribedTopics = subscribedTopics;
	}

	public AccessPointProvider getAccessPointProvider() {
		return accessPointProvider;
	}

	public NeighborProvider getNeighborProvider() {
		return neighborProvider;
	}

	public Set<Topic> getSubscribedTopics() {
		return subscribedTopics;
	}
}
