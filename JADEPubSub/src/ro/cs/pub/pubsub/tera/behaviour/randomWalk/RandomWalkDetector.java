package ro.cs.pub.pubsub.tera.behaviour.randomWalk;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.AgentResponse;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.DistanceQuery;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.message.TopicQuery;
import ro.cs.pub.pubsub.util.Shuffle;

public class RandomWalkDetector extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public RandomWalkDetector(TeraAgent agent) {
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
		AID receiver = Shuffle.shuffle(agent.getContext().getNeighbors(),
				originalMessage.getSender());
		if (receiver == null) {
			return;
		}

		prepare(originalMessage);

		originalMessage.addReceiver(receiver);
		mf.fillContent(originalMessage, randomWalkRequest.decreaseTTL());
		agent.send(originalMessage);
	}

	private void prepare(ACLMessage message) {
		// we only need the conversation id
		message.clearAllReceiver();
		message.clearAllReplyTo();
	}
}
