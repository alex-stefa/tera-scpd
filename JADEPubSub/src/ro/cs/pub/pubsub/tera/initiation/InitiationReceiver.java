package ro.cs.pub.pubsub.tera.initiation;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Iterator;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.message.shared.InitiationReply;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.overlay.context.OverlayContext;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

/**
 * Receives the initiation acceptance message, which contains a set of seeds.
 */
public class InitiationReceiver extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private static final MessageTemplate template = MessageTemplate.and(
			//
			MessageTemplate.MatchProtocol(Names.PROTOCOL_INITIATION),
			MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));

	public InitiationReceiver(TeraAgent agent) {
		super(agent, template);
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getMessageFactory();
			InitiationReply content;
			content = (InitiationReply) mf.extractContent(message);

			// add the discovered peers
			OverlayContext context = agent.getOverlayManager()
					.getOverlayContext(Names.OVERLAY_BASE);
			NeighborProvider np = context.getNeighborProvider();
			Iterator<AID> it = content.getNeighbors().iterator();
			while (it.hasNext() && !np.isFull()) {
				np.add(it.next());
			}
			context.getInitiator().initiateCommunication();
			setDone();
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}