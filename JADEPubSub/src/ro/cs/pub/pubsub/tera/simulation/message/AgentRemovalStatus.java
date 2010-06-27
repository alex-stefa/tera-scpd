package ro.cs.pub.pubsub.tera.simulation.message;

import jade.core.AID;

import java.util.List;

import ro.cs.pub.pubsub.message.MessageContent;

public class AgentRemovalStatus implements MessageContent {
	private static final long serialVersionUID = 1L;

	private List<AID> agentsRemaining;

	public AgentRemovalStatus(List<AID> agentsRemaining) {
		this.agentsRemaining = agentsRemaining;
	}

	public List<AID> getAgentsRemaining() {
		return agentsRemaining;
	}
}
