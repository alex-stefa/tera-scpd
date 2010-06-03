package ro.cs.pub.pubsub.tera.behaviour.randomWalk;

import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkRequest;

public class RandomWalkController extends FSMBehaviour {
	private static final long serialVersionUID = 1L;

	private static final String STATE_MESSAGE_SENDING = "message-sending";

	private final TeraAgent agent;
	private final AID peer;
	private final int ttl;
	private final RandomWalkQuery query;

	public RandomWalkController(TeraAgent agent, AID peer, int ttl, RandomWalkQuery query) {
		super(agent);
		this.agent = agent;
		this.peer = peer;
		this.ttl = ttl;
		this.query = query;

		registerFirstState(new MessageSender(), STATE_MESSAGE_SENDING);
	}

	class MessageSender extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageFactory mf = agent.getContext().getMessageFactory();
			ACLMessage msg = mf.buildMessage(ACLMessage.REQUEST,
					Names.PROTOCOL_RANDOM_WALK);
			msg.addReceiver(peer);
			msg.setConversationId(agent.generateConversationId());
			RandomWalkRequest content = new RandomWalkRequest( //
					agent.getAID(), ttl, query);
			try {
				mf.fillContent(msg, content);
				agent.send(msg);
			} catch (MessageException e) {
				e.printStackTrace();
			}
		}
	}
}
