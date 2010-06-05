package ro.cs.pub.pubsub.tera.behaviour.randomWalk.message;

import jade.core.AID;

public class AgentResult implements RandomWalkResult {
	private static final long serialVersionUID = 1L;

	private final AID agent;

	public AgentResult(AID peer) {
		this.agent = peer;
	}

	public AID getAgent() {
		return agent;
	}

	@Override
	public String toString() {
		return "AgentResponse [agent=" + agent + "]";
	}
}
