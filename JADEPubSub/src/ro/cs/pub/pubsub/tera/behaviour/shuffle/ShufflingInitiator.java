package ro.cs.pub.pubsub.tera.behaviour.shuffle;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.message.ShuffleMessage;
import ro.cs.pub.pubsub.util.Shuffle;

public class ShufflingInitiator extends TickerBehaviour {
	private static final long serialVersionUID = 1L;

	private final TeraAgent agent;

	public ShufflingInitiator(TeraAgent agent, long period) {
		super(agent, period);
		this.agent = agent;
	}

	@Override
	protected void onTick() {
		try {

			MessageFactory mf = agent.getContext().getMessageFactory();
			ACLMessage msg = mf.buildMessage(ACLMessage.INFORM,
					Names.PROTOCOL_SHUFFLE);

			Set<AID> neighbors = agent.getContext().getNeighbors();

			// pick a random receiver
			AID receiver = Shuffle.shuffle(neighbors);
			if (receiver == null) {
				// we have no neighbors
				return;
			}
			msg.addReceiver(receiver);

			// setup the known peer set
			Set<AID> known = new HashSet<AID>(neighbors);
			known.remove(receiver);
			ShuffleMessage content = new ShuffleMessage(known, true);
			mf.fillContent(msg, content);
			
			agent.print(content);
			
			agent.send(msg);
		} catch (MessageException e) {
			e.printStackTrace();
		}

	}
}
