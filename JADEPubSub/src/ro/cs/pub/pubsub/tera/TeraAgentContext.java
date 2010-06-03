package ro.cs.pub.pubsub.tera;

import jade.core.AID;

import java.util.Set;

import ro.cs.pub.pubsub.agent.AgentContext;
import ro.cs.pub.pubsub.protocol.AccessPointProvider;

public class TeraAgentContext extends AgentContext {
	private AccessPointProvider accessPointProvider;
	private Set<AID> neighbors;
	
	public void setAccessPointProvider(AccessPointProvider accessPointProvider) {
		this.accessPointProvider = accessPointProvider;
	}
	
	public AccessPointProvider getAccessPointProvider() {
		return accessPointProvider;
	}
	
	public void setNeighbors(Set<AID> neighbours) {
		this.neighbors = neighbours;
	}
	
	public Set<AID> getNeighbors() {
		return neighbors;
	}
}
