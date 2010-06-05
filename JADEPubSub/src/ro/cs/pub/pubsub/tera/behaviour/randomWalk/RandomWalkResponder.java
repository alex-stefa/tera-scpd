package ro.cs.pub.pubsub.tera.behaviour.randomWalk;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Iterator;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.AgentResponse;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.DistanceQuery;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.TopicQuery;

/**
 * Detects random walk requests. Tries to answer them or propagates the
 * requests.
 */
public class RandomWalkResponder extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public RandomWalkResponder(TeraAgent agent) {
		super(agent);
	}

	@Override
	protected MessageTemplate setupTemplate() {
		return MessageTemplate.and(
				//
				MessageTemplate.MatchProtocol(Names.PROTOCOL_RANDOM_WALK),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getContext().getMessageFactory();
			RandomWalkRequest content;
			content = (RandomWalkRequest) mf.extractContent(message);
			RandomWalkQuery query = content.getQuery();

			if (query instanceof DistanceQuery && content.getTTL() <= 0) {
				sendToOrigin(mf, message, content.getOrigin(),
						new AgentResponse(agent.getAID()));
				return;
			}

			if (query instanceof TopicQuery) {
				// find peer for given topic
				AID peer = agent.getContext().getAccessPointProvider(). //
						get(((TopicQuery) query).getTopic());
				if (peer != null) {
					sendToOrigin(mf, message, content.getOrigin(),
							new AgentResponse(peer));
					return;
				}
			}

			forward(mf, message, content);
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	private void sendToOrigin(MessageFactory mf, ACLMessage originalMessage,
			AID origin, AgentResponse responseContent) throws MessageException {
		ACLMessage reply = originalMessage.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		prepare(reply);

		reply.addReceiver(origin);
		mf.fillContent(reply, responseContent);
		agent.send(reply);
	}

	private void forward(MessageFactory mf, ACLMessage originalMessage,
			RandomWalkRequest randomWalkRequest) throws MessageException {
		// find the current set of neighbors and pick a random one
		NeighborProvider np = agent.getContext().getNeighborProvider();

		AID receiver = null;
		Iterator<AID> it = np.randomIterator();
		while (it.hasNext()) {
			AID r = it.next();
			if (!r.equals(originalMessage.getSender())) {
				// stop when we find a suitable receiver
				receiver = r;
				break;
			}
		}
		if (receiver == null) {
			// we have no neighbors to forward the message to
			return;
		}

		prepare(originalMessage);

		originalMessage.addReceiver(receiver);
		mf.fillContent(originalMessage, randomWalkRequest.decreaseTTL());
		agent.send(originalMessage);
	}

	/**
	 * Clears unnecessary fields.
	 * 
	 * @param message
	 */
	private void prepare(ACLMessage message) {
		// we only need the conversation id
		message.clearAllReceiver();
		message.clearAllReplyTo();
	}
}
