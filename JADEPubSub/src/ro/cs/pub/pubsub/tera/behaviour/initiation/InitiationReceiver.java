package ro.cs.pub.pubsub.tera.behaviour.initiation;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Iterator;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.message.shared.InitiationReply;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.agent.context.NeighborProvider;

/**
 * Receives the initiation acceptance message, which contains a set of seeds.
 */
public class InitiationReceiver extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public InitiationReceiver(TeraAgent agent) {
		super(agent);
	}

	@Override
	protected MessageTemplate setupTemplate() {
		return MessageTemplate.and(
				//
				MessageTemplate.MatchProtocol(Names.PROTOCOL_INITIATION),
				MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getContext().getMessageFactory();
			InitiationReply content;
			content = (InitiationReply) mf.extractContent(message);

			// add the discovered peers
			NeighborProvider np = agent.getContext().getNeighborProvider();
			Iterator<AID> it = content.getNeighbors().iterator();
			while (it.hasNext() && !np.isFull()) {
				np.add(it.next());
			}
			setDone();
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}