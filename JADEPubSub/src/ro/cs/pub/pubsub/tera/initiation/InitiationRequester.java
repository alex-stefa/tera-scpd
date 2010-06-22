package ro.cs.pub.pubsub.tera.initiation;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.Set;

import ro.cs.pub.pubsub.agent.BaseTickerBehaviour;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.model.Names;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

/**
 * Makes a request to an initiation service in order to enter the network.
 */
public class InitiationRequester extends BaseTickerBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public InitiationRequester(TeraAgent agent, long period) {
		super(agent, period);
	}

	@Override
	public void onTick() {
		Set<AID> agents = agent.findAgents(Names.SERVICE_INITIATION);
		if (agents.size() == 0) {
			return;
		}

		MessageFactory mf = agent.getMessageFactory();
		ACLMessage msg = mf.buildMessage(ACLMessage.CFP,
				Names.PROTOCOL_INITIATION);
		msg.addReceiver(agents.iterator().next());
		agent.send(msg);
		stop();
	}
}