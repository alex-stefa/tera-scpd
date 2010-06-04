package ro.cs.pub.pubsub.tera.behaviour.randomWalk;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkResponse;

/**
 * Initiates and interprets the result of a random walk.
 */
public class RandomWalkInitiator extends SequentialBehaviour {
	private static final long serialVersionUID = 1L;

	private final TeraAgent agent;
	private final AID peer;
	private final int ttl;
	private final RandomWalkQuery query;

	/**
	 * Kept throughout the random walk.
	 */
	private final String conversationId;
	
	/**
	 * The response of the random walk.
	 */
	private RandomWalkResponse response;

	public RandomWalkInitiator(TeraAgent agent, AID peer, int ttl,
			RandomWalkQuery query) {
		super(agent);
		this.agent = agent;
		this.peer = peer;
		this.ttl = ttl;
		this.query = query;

		conversationId = agent.generateConversationId();

		addSubBehaviour(new MessageSender());
		addSubBehaviour(new MessageReceiver(agent));
	}

	/**
	 * Starts the random walk.
	 */
	class MessageSender extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageFactory mf = agent.getContext().getMessageFactory();
			ACLMessage msg = mf.buildMessage(ACLMessage.REQUEST,
					Names.PROTOCOL_RANDOM_WALK);
			msg.addReceiver(peer);
			msg.setConversationId(conversationId);
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

	/**
	 * Receives the response from the random walk and saves it.
	 */
	class MessageReceiver extends BaseTemplateBehaviour<TeraAgent> {
		private static final long serialVersionUID = 1L;

		public MessageReceiver(TeraAgent agent) {
			super(agent);
		}

		@Override
		protected MessageTemplate setupTemplate() {
			return MessageTemplate.and( //
					MessageTemplate.and(
							//
							MessageTemplate
									.MatchProtocol(Names.PROTOCOL_RANDOM_WALK),
							MessageTemplate
									.MatchPerformative(ACLMessage.INFORM)),
					//
					MessageTemplate.MatchConversationId(conversationId));
		}

		@Override
		protected void onMessage(ACLMessage message) {
			MessageFactory mf = agent.getContext().getMessageFactory();
			try {
				response = (RandomWalkResponse) mf.extractContent(message);
			} catch (MessageException e) {
				e.printStackTrace();
			}
		}
	}
	
	public RandomWalkResponse getResponse() {
		return response;
	}
}
