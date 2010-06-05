package ro.cs.pub.pubsub.tera.initiation;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

/**
 * Makes a request to an initiation service in order to enter the network.
 */
public class InitiationRequester extends TickerBehaviour {
	private static final long serialVersionUID = 1L;

	private final TeraAgent agent;

	public InitiationRequester(TeraAgent agent, long period) {
		super(agent, period);
		this.agent = agent;
	}

	@Override
	public void onTick() {
		Set<AID> agents = agent.findAgents(Names.SERVICE_INITIATION);
		if (agents.size() == 0) {
			return;
		}

		MessageFactory mf = agent.getContext().getMessageFactory();
		ACLMessage msg = mf.buildMessage(ACLMessage.CFP,
				Names.PROTOCOL_INITIATION);
		msg.addReceiver(agents.iterator().next());
		agent.send(msg);
		stop();
	}
}