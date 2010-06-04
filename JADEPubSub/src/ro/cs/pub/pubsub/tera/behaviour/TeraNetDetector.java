package ro.cs.pub.pubsub.tera.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.tera.agent.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.util.RandomIterator;

public class TeraNetDetector extends TickerBehaviour {
	private static final long serialVersionUID = 1L;

	private final TeraAgent agent;
	private final int agentCount;

	public TeraNetDetector(TeraAgent agent, long period, int agentCount) {
		super(agent, period);
		this.agent = agent;
		this.agentCount = agentCount;
	}

	@Override
	public void onTick() {
		List<AID> peers = new ArrayList<AID>(agent.findAgents(Names.SERVICE_TERA));

		if (peers.size() >= agentCount) {
			NeighborProvider np = agent.getContext().getNeighborProvider();
			
			// add the discovered peers
			Iterator<AID> it = new RandomIterator<AID>(peers, new Random());
			while (it.hasNext() && !np.isFull()) {
				np.add(it.next());
			}
			stop();
		}
	}
}
