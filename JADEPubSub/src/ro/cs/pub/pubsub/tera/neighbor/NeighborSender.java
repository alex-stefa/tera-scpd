package ro.cs.pub.pubsub.tera.neighbor;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

/**
 * Sends a {@link View} that contains a random selection of neighbors.
 * 
 * Phases:
 * 
 * - Initial: The {@link NeighborController} request the sending of a message. a
 * view is sent to a randomly chosen neighbor. the selected peer is removed from
 * the agent's neighbor set, being put again after a reply is received.
 * 
 * - Reply: A reply message is sent to the sender of the initial message.
 */
public class NeighborSender extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;

	private final NeighborController controller;
	private final ACLMessage initialMessage;

	public NeighborSender(NeighborController controller,
			ACLMessage initialMessage) {
		this.controller = controller;
		this.initialMessage = initialMessage;
	}

	public NeighborSender(NeighborController controller) {
		this(controller, null);
	}

	@Override
	public void action() {
		try {
			TeraAgent agent = controller.getAgent();
			NeighborProvider np = agent.getNeighborProvider();
			MessageFactory mf = agent.getMessageFactory();

			if (initialMessage == null) {
				sendInitialMessage(agent, np, mf);
			} else {
				sendReplyMessage(agent, np, mf);
			}
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	private void sendInitialMessage(TeraAgent agent, NeighborProvider np,
			MessageFactory mf) throws MessageException {
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

		// remove the receiver from the neighbor set
		np.remove(receiver);

		// prepare the message
		ACLMessage msg = mf.buildMessage( //
				ACLMessage.INFORM, Names.PROTOCOL_NEIGHBOR_GOSSIP);
		msg.addReceiver(receiver);

		// build the content
		NeighborMessage content = new NeighborMessage(view, false);
		mf.fillContent(msg, content);

		// send the message
		agent.send(msg);
	}

	private void sendReplyMessage(TeraAgent agent, NeighborProvider np,
			MessageFactory mf) throws MessageException {
		// pick a random peer
		AID receiver = initialMessage.getSender();

		// generate the view
		ViewGenerator generator = controller.getViewGenerator();
		View view = generator.generateView(receiver, //
				controller.getViewSize());

		// prepare the message
		ACLMessage reply = initialMessage.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		// build the content
		NeighborMessage content = new NeighborMessage(view, true);
		mf.fillContent(reply, content);

		// send the message
		agent.send(reply);
	}
}
