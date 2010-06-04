package ro.cs.pub.pubsub.tera.agent;

import ro.cs.pub.pubsub.agent.AgentContext;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.ViewGenerator;

public class TeraAgentContext extends AgentContext {
	private AccessPointProvider accessPointProvider;
	private NeighborProvider neighborProvider;
	private ViewGenerator viewGenerator;

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

	public ViewGenerator getViewGenerator() {
		return viewGenerator;
	}

	public void setViewGenerator(ViewGenerator viewGenerator) {
		this.viewGenerator = viewGenerator;
	}
}
