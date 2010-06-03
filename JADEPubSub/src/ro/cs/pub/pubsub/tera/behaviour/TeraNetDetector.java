package ro.cs.pub.pubsub.tera.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;

import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.message.LoggingMessageContent;
import ro.cs.pub.pubsub.tera.TeraAgent;

public class TeraNetDetector extends TickerBehaviour {
	private static final long serialVersionUID = 1L;

	private TeraAgent agent;

	public TeraNetDetector(TeraAgent agent, long period) {
		super(agent, period);
		this.agent = agent;
	}

	@Override
	public void onTick() {
		Set<AID> peers = agent.findAgents(Names.SERVICE_TERA);

		if (peers.size() > 0) {
			LoggingMessageContent c = new LoggingMessageContent(
					"Found another agent: " + peers.iterator().next());
			agent.sendLoggingMessage(c);
			stop();
		}
	}
}
