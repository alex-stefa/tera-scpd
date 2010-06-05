package ro.cs.pub.pubsub.tera.behaviour.neighbor;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;
import java.util.Map;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.neighbor.message.ShufflingMessage;

/**
 * Sends a shuffling message.
 */
public class NeighborSender extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;

	private final NeighborController controller;

	public NeighborSender(NeighborController controller,
			ACLMessage initialMessage) {
		this.controller = controller;
	}

	public NeighborSender(NeighborController controller) {
		this(controller, null);
	}

	@Override
	public void action() {
		try {
			TeraAgent agent = controller.getAgent();
			NeighborProvider np = agent.getContext().getNeighborProvider();
			MessageFactory mf = agent.getContext().getMessageFactory();

			if (np.size() == 0) {
				// we don't know any other agent
				return;
			}

			// even if we know only one other agent, send a view in order to
			// receive a reply

			// pick a random peer
			Iterator<AID> it = np.randomIterator();
			AID receiver = it.next();

			// generate the view
			ViewGenerator generator = controller.getViewGenerator();
			View view = generator.generateView(receiver, //
					controller.getViewSize());

			// prepare the message
			ACLMessage msg = mf.buildMessage( //
					ACLMessage.INFORM, Names.PROTOCOL_NEIGHBOR_GOSSIP);
			msg.addReceiver(receiver);

			// build the content
			ShufflingMessage content = new ShufflingMessage(view);
			mf.fillContent(msg, content);
			
			// register conversation
			String conversationId = agent.generateConversationId();
			Map<String, View> conversationViews = controller.getConversationViews();
			conversationViews.put(conversationId, view);

			// send the message
			agent.send(msg);
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}
