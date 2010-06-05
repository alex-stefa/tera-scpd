package ro.cs.pub.pubsub.tera.behaviour.neighbor;

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
	 * @param receiver
	 * @param size
	 * @return a random view
	 */
	public View generateView(AID receiver, int size) {
		NeighborProvider np = agent.getContext().getNeighborProvider();

		// assure we obtain a random selection of neighbors
		Iterator<AID> it = np.randomIterator();

		// build the neighbor view
		Set<AID> viewSet = new HashSet<AID>();
		while (it.hasNext() && viewSet.size() <= size) {
			AID n = it.next();
			if (!n.equals(receiver)) {
				viewSet.add(n);
			}
		}

		// add the current agent
		viewSet.add(agent.getAID());

		return new View(viewSet);
	}
}
