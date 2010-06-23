package ro.cs.pub.pubsub.overlay.view;

import jade.core.AID;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.overlay.OverlayManager;

public class ViewGenerator {
	private final BaseAgent agent;
	private final NeighborProvider provider;
	private final int viewSize;

	public ViewGenerator(OverlayManager manager, NeighborProvider provider,
			int viewSize) {
		this.agent = manager.getAgent();
		this.provider = provider;
		this.viewSize = viewSize;
	}

	/**
	 * Generates a random view of the neighbor list. The length of the list is
	 * specified in the constructor.
	 * 
	 * @param receiver
	 * @return a random view
	 */
	public View generateView(AID receiver) {
		// assure we obtain a random selection of neighbors
		Iterator<AID> it = provider.randomIterator();

		// build the neighbor view
		Set<AID> viewSet = new HashSet<AID>();
		while (it.hasNext() && viewSet.size() < viewSize) {
			AID n = it.next();
			if (!n.equals(receiver)) {
				viewSet.add(n);
			}
		}

		// add the current agent
		// viewSet.add(agent.getAID());

		return new View(viewSet);
	}
}
