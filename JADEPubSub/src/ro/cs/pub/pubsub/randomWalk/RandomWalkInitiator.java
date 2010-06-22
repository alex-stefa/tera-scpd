package ro.cs.pub.pubsub.randomWalk;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.agent.BaseOneMessageReceiver;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResponse;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResult;

/**
 * Initiates a random walk. The behavior has two states: Message Sending and
 * Message Receiving. When the behavior finishes, the result is available
 * through getResult().
 */
public class RandomWalkInitiator extends SequentialBehaviour {
	private static final long serialVersionUID = 1L;

	private final BaseAgent agent;
	private final String protocol;
	private final AID peer;
	private final int ttl;
	private final RandomWalkQuery query;
	private final Long timeout;
	private final RandomWalkCallback callback;

	/**
	 * Kept throughout the random walk.
	 */
	private final String conversationId;

	/**
	 * The result of the random walk.
	 */
	private RandomWalkResult result;

	public RandomWalkInitiator(BaseAgent agent, String protocol, AID peer,
			int ttl, RandomWalkQuery query, Long timeout,
			RandomWalkCallback callback) {
		super(agent);
		this.agent = agent;
		this.protocol = protocol;
		this.peer = peer;
		this.ttl = ttl;
		this.query = query;
		this.timeout = timeout;
		this.callback = callback;

		conversationId = agent.generateConversationId();

		addSubBehaviour(new MessageSender());
		addSubBehaviour(new MessageReceiver());
	}

	public RandomWalkInitiator(BaseAgent agent, String protocol, AID peer,
			int ttl, RandomWalkQuery query, Long deadline) {
		this(agent, protocol, peer, ttl, query, deadline, null);
	}

	/**
	 * Starts the random walk.
	 */
	class MessageSender extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageFactory mf = agent.getMessageFactory();
			ACLMessage msg = mf.buildMessage(ACLMessage.REQUEST, protocol);
			msg.addReceiver(peer);
			msg.setConversationId(conversationId);
			RandomWalkRequest content = new RandomWalkRequest( //
					agent.getAID(), ttl, query);
			try {
				mf.fillContent(msg, content);
				agent.sendMessage(msg);
			} catch (MessageException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Receives the response from the random walk and saves it.
	 */
	private class MessageReceiver extends BaseOneMessageReceiver<BaseAgent> {
		private static final long serialVersionUID = 1L;

		public MessageReceiver() {
			super(RandomWalkInitiator.this.agent);
			this.template = setupTemplate();
			this.timeout = RandomWalkInitiator.this.timeout;
		}

		protected MessageTemplate setupTemplate() {
			return MessageTemplate.and( //
					MessageTemplate.and(
							//
							MessageTemplate.MatchProtocol(protocol),
							MessageTemplate
									.MatchPerformative(ACLMessage.INFORM)),
					//
					MessageTemplate.MatchConversationId(conversationId));
		}

		@Override
		protected void onMessage(ACLMessage message) {
			MessageFactory mf = agent.getMessageFactory();
			try {
				result = ((RandomWalkResponse) mf.extractContent(message))
						.getResult();
				if (callback != null) {
					callback.onResult(result);
				}
			} catch (MessageException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onTimeout() {
			if (callback != null) {
				callback.onTimeout();
			}
		}
	}

	public RandomWalkResult getResult() {
		return result;
	}
}
