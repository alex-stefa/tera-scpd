package ro.cs.pub.pubsub.tera.behaviour.shuffle.message;

import jade.core.AID;

import java.util.Set;

import ro.cs.pub.pubsub.message.MessageContent;

public class ShuffleMessage implements MessageContent {
	private static final long serialVersionUID = 1L;

	private final Set<AID> neighbors;
	private final boolean first;

	public ShuffleMessage(Set<AID> neighbors, boolean first) {
		this.neighbors = neighbors;
		this.first = first;
	}
	
	public Set<AID> getNeighbors() {
		return neighbors;
	}
	
	public boolean isFirst() {
		return first;
	}

	@Override
	public String toString() {
		return "ShuffleMessage [first=" + first + ", neighbors=" + neighbors
				+ "]";
	}
}
