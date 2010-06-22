package ro.cs.pub.pubsub.randomWalk;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Iterator;

import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResponse;

/**
 * Detects random walk requests and tries to answer. If it cannot answer
 * properly, the responder propagates the requests.
 * 
 * This class contains the propagation algorithms. The response decisions are
 * made by a {@link RandomWalkProcessor}.
 */
public class RandomWalkResponder extends BaseTemplateBehaviour<BaseAgent> {
	private static final long serialVersionUID = 1L;

	private final RandomWalkProcessor processor;
	private final String protocol;
	private final NeighborProvider neighborProvider;

	public RandomWalkResponder(BaseAgent agent, RandomWalkProcessor processor,
			String protocol, NeighborProvider neighborProvider) {
		super(agent, null);
		this.processor = processor;
		this.protocol = protocol;
		this.neighborProvider = neighborProvider;
		this.messageTemplate = setupTemplate();
	}

	protected MessageTemplate setupTemplate() {
		return MessageTemplate.and(
		//
				MessageTemplate.MatchProtocol(protocol), MessageTemplate
						.MatchPerformative(ACLMessage.REQUEST));
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getMessageFactory();
			RandomWalkRequest request;
			request = (RandomWalkRequest) mf.extractContent(message);

			// process the request
			RandomWalkProcessingResult result = processor.process(request);

			switch (result.getType()) {
			case SEND_TO_ORIGIN:
				sendToOrigin(mf, message, request.getOrigin(), //
						(RandomWalkResponse) result.getContent());
				break;
			case FORWARD:
				forward(mf, message, //
						(RandomWalkRequest) result.getContent());
				break;
			}
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a response to the origin.
	 * 
	 * @param mf
	 * @param originalMessage
	 * @param origin
	 * @param content
	 * @throws MessageException
	 */
	private void sendToOrigin(MessageFactory mf, ACLMessage originalMessage,
			AID origin, RandomWalkResponse content) throws MessageException {
		ACLMessage reply = originalMessage.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		prepare(reply);

		reply.addReceiver(origin);
		mf.fillContent(reply, content);
		agent.send(reply);
	}

	/**
	 * Forwards the message to a random peer.
	 * 
	 * @param mf
	 * @param originalMessage
	 * @param randomWalkRequest
	 * @throws MessageException
	 */
	private void forward(MessageFactory mf, ACLMessage originalMessage,
			RandomWalkRequest randomWalkRequest) throws MessageException {
		// find the current set of neighbors and pick a random one
		AID receiver = null;
		Iterator<AID> it = neighborProvider.randomIterator();
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
		agent.sendMessage(originalMessage);
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
