package ro.cs.pub.pubsub.overlay;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.overlay.context.OverlayContext;
import ro.cs.pub.pubsub.overlay.view.View;
import ro.cs.pub.pubsub.overlay.view.ViewGenerator;

/**
 * Sends a {@link View} that contains a random selection of neighbors.
 * 
 * Phases:
 * 
 * - Initial: The {@link OverlayManager} request the sending of a message. a
 * view is sent to a randomly chosen neighbor. the selected peer is removed from
 * the agent's neighbor set, being put again after a reply is received.
 * 
 * - Reply: A reply message is sent to the sender of the initial message.
 */
public class OverlaySender extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;

	private final OverlayManager manager;
	private final OverlayId id;
	private final ACLMessage initialMessage;

	public OverlaySender(OverlayManager manager, OverlayId id,
			ACLMessage initialMessage) {
		this.manager = manager;
		this.id = id;
		this.initialMessage = initialMessage;
	}

	public OverlaySender(OverlayManager manager, OverlayId id) {
		this(manager, id, null);
	}

	@Override
	public void action() {
		try {
			BaseAgent agent = manager.getAgent();
			OverlayContext context = manager.getOverlayContext(id);
			NeighborProvider np = context.getNeighborProvider();
			ViewGenerator vg = manager.getOverlayContext(id).getViewGenerator();
			MessageFactory mf = agent.getMessageFactory();

			if (initialMessage == null) {
				sendInitialMessage(agent, np, mf, vg);
			} else {
				sendReplyMessage(agent, np, mf, vg);
			}
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	private void sendInitialMessage(BaseAgent agent, NeighborProvider np,
			MessageFactory mf, ViewGenerator vg) throws MessageException {
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
		View view = vg.generateView(receiver);

		// remove the receiver from the neighbor set
		np.remove(receiver);

		// prepare the message
		ACLMessage msg = mf.buildMessage( //
				ACLMessage.INFORM, Names.PROTOCOL_OVERLAY_MANAGEMENT);
		msg.addReceiver(receiver);

		// build the content
		OverlayMessage content = new OverlayMessage(id, view, false);
		mf.fillContent(msg, content);

		// send the message
		agent.send(msg);
	}

	private void sendReplyMessage(BaseAgent agent, NeighborProvider np,
			MessageFactory mf, ViewGenerator vg) throws MessageException {
		// pick a random peer
		AID receiver = initialMessage.getSender();

		// generate the view
		View view = vg.generateView(receiver);

		// prepare the message
		ACLMessage reply = initialMessage.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		// build the content
		OverlayMessage content = new OverlayMessage(id, view, true);
		mf.fillContent(reply, content);

		// send the message
		agent.send(reply);
	}
}
