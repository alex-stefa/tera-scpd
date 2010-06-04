package ro.cs.pub.pubsub.tera.agent;

import jade.core.AID;

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
	private final int maxSize;
	private final Set<AID> neighbors;

	public NeighborProvider(int maxSize) {
		this.maxSize = maxSize;
		neighbors = new HashSet<AID>();
	}

	@Override
	public Iterator<AID> iterator() {
		return neighbors.iterator();
	}

	public Iterator<AID> randomIterator() {
		List<AID> list = new ArrayList<AID>(neighbors);
		return new RandomIterator<AID>(list, new Random());
	}

	public boolean add(AID e) {
		if (isFull())
			throw new IllegalArgumentException();
		return neighbors.add(e);
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
}
