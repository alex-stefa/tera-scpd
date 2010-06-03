package ro.cs.pub.pubsub.tera;

import jade.core.AID;

import java.util.Set;

import ro.cs.pub.pubsub.agent.AgentContext;
import ro.cs.pub.pubsub.protocol.AccessPointProvider;

public class TeraAgentContext extends AgentContext {
	private AccessPointProvider accessPointProvider;
	private Set<AID> neighbours;
	
	public void setAccessPointProvider(AccessPointProvider accessPointProvider) {
		this.accessPointProvider = accessPointProvider;
	}
	
	public AccessPointProvider getAccessPointProvider() {
		return accessPointProvider;
	}
	
	public void setNeighbours(Set<AID> neighbours) {
		this.neighbours = neighbours;
	}
	
	public Set<AID> getNeighbours() {
		return neighbours;
	}
}
