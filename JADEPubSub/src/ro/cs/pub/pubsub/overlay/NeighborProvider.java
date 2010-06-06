package ro.cs.pub.pubsub.overlay;

import jade.core.AID;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ro.cs.pub.pubsub.util.RandomIterator;

/**
 * Provides the neighbor view of the agent.
 * 
 * TODO consider the age of the entries
 */
public class NeighborProvider implements Iterable<AID> {
	private final Agent agent;
	private final int maxSize;
	private final Set<AID> neighbors;

	public NeighborProvider(OverlayManager manager, int maxSize) {
		this.agent = manager.getAgent();
		this.maxSize = maxSize;
		this.neighbors = new HashSet<AID>();
	}

	@Override
	public Iterator<AID> iterator() {
		return neighbors.iterator();
	}

	public Iterator<AID> randomIterator() {
		List<AID> list = new ArrayList<AID>(neighbors);
		return new RandomIterator<AID>(list, new Random());
	}

	public void add(AID e) {
		if (e.equals(agent.getAID())) {
			// TODO maybe we should throw an exception
			return;
		}

		if (isFull())
			throw new IllegalArgumentException();

		neighbors.add(e);
	}

	public boolean contains(AID o) {
		return neighbors.contains(o);
	}

	public boolean remove(AID o) {
		return neighbors.remove(o);
	}

	public int size() {
		return neighbors.size();
	}

	public int getMaxSize() {
		return maxSize;
	}

	public boolean isFull() {
		return size() == maxSize;
	}

	@Override
	public String toString() {
		return "NeighborProvider [maxSize=" + maxSize + ", neighbors="
				+ neighbors + "]";
	}
}
