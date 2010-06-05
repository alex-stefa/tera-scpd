package ro.cs.pub.pubsub.tera.behaviour.neighbor;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.agent.context.NeighborProvider;

public class NeighborReceiver extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private final NeighborController controller;

	public NeighborReceiver(NeighborController controller) {
		super(controller.getAgent());
		this.controller = controller;
	}

	@Override
	protected MessageTemplate setupTemplate() {
		return MessageTemplate.and(
				//
				MessageTemplate.MatchProtocol(Names.PROTOCOL_NEIGHBOR_GOSSIP),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getContext().getMessageFactory();
			NeighborMessage content = (NeighborMessage) mf
					.extractContent(message);

			// update the neighbor set
			updateNeighbors(content.getView());

			if (!content.isReply()) {
				// send the agent's own view
				controller.addSubBehaviour( //
						new NeighborSender(controller, message));
			}
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	int round = 0;

	private void updateNeighbors(View view) {
		NeighborProvider np = agent.getContext().getNeighborProvider();
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

		agent.print("\t   " + round++ + "    " + np.size());
	}
}
