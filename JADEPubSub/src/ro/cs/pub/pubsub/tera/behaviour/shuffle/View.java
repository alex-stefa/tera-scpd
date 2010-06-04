package ro.cs.pub.pubsub.tera.behaviour.shuffle;

import jade.core.AID;

import java.util.Set;

public class View {
	private final AID receiver;
	private final Set<AID> neighbors;

	public View(AID receiver, Set<AID> neighbors) {
		this.receiver = receiver;
		this.neighbors = neighbors;
	}

	public AID getReceiver() {
		return receiver;
	}

	public Set<AID> getNeighbors() {
		return neighbors;
	}
}
