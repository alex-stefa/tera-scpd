package ro.cs.pub.pubsub.overlay;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.overlay.context.OverlayContext;
import ro.cs.pub.pubsub.overlay.view.View;

public class OverlayReceiver extends BaseTemplateBehaviour<BaseAgent> {
	private static final long serialVersionUID = 1L;

	private static final MessageTemplate template = MessageTemplate.and(
			//
			MessageTemplate.MatchProtocol(Names.PROTOCOL_OVERLAY_MANAGEMENT),
			MessageTemplate.MatchPerformative(ACLMessage.INFORM));

	private final OverlayManager manager;

	public OverlayReceiver(OverlayManager manager) {
		super(manager.getAgent(), template);
		this.manager = manager;
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getMessageFactory();
			OverlayMessage content = (OverlayMessage) mf
					.extractContent(message);

			// update the neighbor set
			updateNeighbors(content.getOverlayId(), content.getView());

			if (!content.isReply()) {
				// send the agent's own view
				manager.addSubBehaviour(new OverlaySender( //
						manager, content.getOverlayId(), message));
			}
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	private void updateNeighbors(OverlayId overlayId, View view) {
		OverlayContext context = manager.getOverlayContext(overlayId);

		if (context == null) {
			return;
		}

		NeighborProvider np = context.getNeighborProvider();
		Set<AID> incoming = new HashSet<AID>(view.getNeighbors());

		// remove the nodes we know about
		Iterator<AID> it = incoming.iterator();
		while (it.hasNext()) {
			if (np.contains(it.next())) {
				it.remove();
			}
		}

		// remove neighbors in a random fashion
		int viewSize = incoming.size();
		while (np.size() > np.getMaxSize() - viewSize) {
			it = np.randomIterator();
			np.remove(it.next());
		}

		// add the new neighbors
		for (AID n : incoming) {
			np.add(n);
		}
	}
}
