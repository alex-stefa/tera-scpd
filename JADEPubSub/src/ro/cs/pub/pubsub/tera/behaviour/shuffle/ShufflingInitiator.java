package ro.cs.pub.pubsub.tera.behaviour.shuffle;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.message.ShufflingMessage;

/**
 * Initiates a neighbor exchange based on the shuffle protocol.
 */
public class ShufflingInitiator extends TickerBehaviour {
	private static final long serialVersionUID = 1L;

	private final TeraAgent agent;
	private final int viewSize;

	public ShufflingInitiator(TeraAgent agent, long period, int viewSize) {
		super(agent, period);
		this.agent = agent;
		this.viewSize = viewSize;
	}

	@Override
	protected void onTick() {
		try {
			// generate the view
			ViewGenerator generator = agent.getContext().getViewGenerator();
			View view = generator.generateView(viewSize);
			if (view == null) {
				return;
			}
			
			// prepare the message
			MessageFactory mf = agent.getContext().getMessageFactory();
			ACLMessage msg = mf.buildMessage(ACLMessage.INFORM,
					Names.PROTOCOL_SHUFFLING);
			msg.addReceiver(view.getReceiver());
			
			// build the content
			ShufflingMessage content = new ShufflingMessage(view.getNeighbors(), true);
			mf.fillContent(msg, content);
			
			// send the message
			agent.send(msg);
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}
