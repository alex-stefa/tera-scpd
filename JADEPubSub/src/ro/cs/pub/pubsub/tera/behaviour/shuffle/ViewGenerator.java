package ro.cs.pub.pubsub.tera.behaviour.shuffle;

import jade.core.AID;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ro.cs.pub.pubsub.tera.agent.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

public class ViewGenerator {
	private final TeraAgent agent;

	public ViewGenerator(TeraAgent agent) {
		this.agent = agent;
	}

	/**
	 * Generates a random view of the neighbor list. The length of the list is
	 * specified in the constructor.
	 * 
	 * @param size
	 * @return a random view of null if we have only one neighbor
	 */
	public View generateView(int size) {
		NeighborProvider np = agent.getContext().getNeighborProvider();
		if (np.size() <= 1) {
			// we have only one neighbor
			return null;
		}
		
		// assure we obtain a random selection of neighbors
		Iterator<AID> it = np.randomIterator();
		AID receiver = it.next();
		
		// build the neighbor view
		Set<AID> viewSet = new HashSet<AID>();
		int count = 1;
		while (it.hasNext() && count <= size) {
			viewSet.add(it.next());
		}
		
		// add the current agent
		viewSet.add(agent.getAID());

		return new View(receiver, viewSet);
	}
}
