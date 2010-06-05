package ro.cs.pub.pubsub.tera.behaviour.neighbor;

import jade.core.AID;
import jade.util.leap.Serializable;

import java.util.Set;

public class View implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Set<AID> neighbors;

	public View(Set<AID> neighbors) {
		this.neighbors = neighbors;
	}

	public Set<AID> getNeighbors() {
		return neighbors;
	}

	@Override
	public String toString() {
		return "View [neighbors=" + neighbors + "]";
	}
}
