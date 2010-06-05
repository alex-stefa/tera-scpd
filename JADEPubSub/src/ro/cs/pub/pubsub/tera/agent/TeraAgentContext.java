package ro.cs.pub.pubsub.tera.agent;

import ro.cs.pub.pubsub.agent.AgentContext;

public class TeraAgentContext extends AgentContext {
	private AccessPointProvider accessPointProvider;
	private NeighborProvider neighborProvider;

	public AccessPointProvider getAccessPointProvider() {
		return accessPointProvider;
	}

	public void setAccessPointProvider(AccessPointProvider accessPointProvider) {
		this.accessPointProvider = accessPointProvider;
	}

	public NeighborProvider getNeighborProvider() {
		return neighborProvider;
	}

	public void setNeighborProvider(NeighborProvider neighborProvider) {
		this.neighborProvider = neighborProvider;
	}
}
