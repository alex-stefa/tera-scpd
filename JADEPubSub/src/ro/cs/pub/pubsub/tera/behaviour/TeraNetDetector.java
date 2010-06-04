package ro.cs.pub.pubsub.tera.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.tera.agent.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

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
		Set<AID> peers = agent.findAgents(Names.SERVICE_TERA);

		if (peers.size() >= agentCount) {
			NeighborProvider np = agent.getContext().getNeighborProvider();
			for (AID peer : peers) {
				np.add(peer);
			}
			stop();
		}
	}
}
