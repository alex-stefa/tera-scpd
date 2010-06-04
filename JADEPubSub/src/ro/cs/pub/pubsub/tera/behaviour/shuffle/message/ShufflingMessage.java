package ro.cs.pub.pubsub.tera.behaviour.shuffle.message;

import jade.core.AID;

import java.util.Set;

import ro.cs.pub.pubsub.message.MessageContent;

public class ShufflingMessage implements MessageContent {
	private static final long serialVersionUID = 1L;

	private final Set<AID> neighbors;
	private final boolean reply;

	public ShufflingMessage(Set<AID> neighbors, boolean reply) {
		this.neighbors = neighbors;
		this.reply = reply;
	}

	public Set<AID> getNeighbors() {
		return neighbors;
	}

	public boolean isReply() {
		return reply;
	}

	@Override
	public String toString() {
		return "ShufflingMessage [neighbors=" + neighbors + ", reply=" + reply
				+ "]";
	}
}
