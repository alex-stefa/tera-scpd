package ro.cs.pub.pubsub.tera.behaviour.randomWalk;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

			if (query instanceof DistanceQuery && content.getTTL() == 0) {
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

			forward(message);
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	private void sendToOrigin(MessageFactory mf, ACLMessage originalMessage,
			AID origin, AgentResponse responseContent) throws MessageException {
		ACLMessage reply = originalMessage.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		// we only need the conversation id
		reply.clearAllReceiver();
		reply.clearAllReplyTo();
		
		reply.addReceiver(origin);
		
		mf.fillContent(reply, responseContent);
		agent.send(reply);
	}

	private void forward(ACLMessage message) {
		Set<AID> neighbours = agent.getContext().getNeighbours();
		if (neighbours.size() == 1) {
			return;
		}
		
		List<AID> ns = new ArrayList<AID>(neighbours);
		ns.remove(message.getSender());
		int r = (int) Math.random() * ns.size();
		AID receiver = ns.get(r);
		
		message.clearAllReceiver();
		message.clearAllReplyTo();
		
		message.addReceiver(receiver);
		agent.send(message);
	}
}
