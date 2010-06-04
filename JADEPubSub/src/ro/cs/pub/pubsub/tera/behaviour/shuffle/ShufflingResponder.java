package ro.cs.pub.pubsub.tera.behaviour.shuffle;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Iterator;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.message.ShufflingMessage;

public class ShufflingResponder extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public ShufflingResponder(TeraAgent agent) {
		super(agent);
	}

	@Override
	protected MessageTemplate setupTemplate() {
		return MessageTemplate.and(
				//
				MessageTemplate.MatchProtocol(Names.PROTOCOL_SHUFFLING),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getContext().getMessageFactory();
			ShufflingMessage content = (ShufflingMessage) mf
					.extractContent(message);
			
			// update the neighbor set
			Set<AID> newNeighbors = content.getNeighbors();
			updateNeighbors(newNeighbors);
			
			if (content.isReply()) {
				return;
			}
			
			// build the reply
			ViewGenerator generator = agent.getContext().getViewGenerator();
			View view = generator.generateView(newNeighbors.size());
			if (view == null) {
				return;
			}
			
			ACLMessage reply = mf.buildMessage(ACLMessage.INFORM,
					Names.PROTOCOL_SHUFFLING);
			reply.addReceiver(message.getSender());
			content = new ShufflingMessage(view.getNeighbors(), false);
			mf.fillContent(reply, content);
			agent.send(reply);
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
	
	int round = 0;

	private void updateNeighbors(Set<AID> incoming) {
		NeighborProvider np = agent.getContext().getNeighborProvider();

		// remove neighbors in a random fashion
		// TODO we should remove only the ones that were sent by the initiator
		int viewSize = incoming.size();
		while (np.size() > np.getMaxSize() - viewSize) {
			Iterator<AID> it = np.randomIterator();
			np.remove(it.next());
		}

		// add the new neighbors
		for (AID n : incoming) {
			np.add(n);
		}
		
		agent.print("\t   " + round++ + "    " + np.size());
	}
}
