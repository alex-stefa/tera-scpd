package ro.cs.pub.pubsub.tera.initiation;

import jade.core.AID;

import java.util.Set;

import ro.cs.pub.pubsub.message.MessageContent;

public class InitiationReply implements MessageContent {
	private static final long serialVersionUID = 1L;

	private final Set<AID> neighbors;

	public InitiationReply(Set<AID> neighbors) {
		this.neighbors = neighbors;
	}

	public Set<AID> getNeighbors() {
		return neighbors;
	}
}
