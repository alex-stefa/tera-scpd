package ro.cs.pub.pubsub.tera.behaviour.randomWalk.message;

import jade.core.AID;

public class AgentResponse implements RandomWalkResponse {
	private static final long serialVersionUID = 1L;

	private final AID agent;

	public AgentResponse(AID peer) {
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
