package ro.cs.pub.pubsub.tera.randomWalk.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Iterator;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.agent.context.NeighborProvider;
import ro.cs.pub.pubsub.tera.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.tera.randomWalk.message.RandomWalkResponse;
import ro.cs.pub.pubsub.tera.randomWalk.processor.ProcessingResult;
import ro.cs.pub.pubsub.tera.randomWalk.processor.RequestProcessor;

/**
 * Detects random walk requests and tries to answer. If it cannot answer
 * properly, the responder propagates the requests.
 * 
 * This class contains the propagation algorithms. The response decisions are
 * made by a {@link RequestProcessor}.
 */
public class RandomWalkResponder extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private final RequestProcessor processor;

	public RandomWalkResponder(TeraAgent agent) {
		super(agent);
		this.processor = new RequestProcessor(agent);
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
			RandomWalkRequest request;
			request = (RandomWalkRequest) mf.extractContent(message);

			// process the request
			ProcessingResult processingResult = processor.process(request);

			switch (processingResult.getType()) {
			case SEND_TO_ORIGIN:
				sendToOrigin(mf, message, request.getOrigin(), //
						(RandomWalkResponse) processingResult.getContent());
				break;
			case FORWARD:
				forward(mf, message, //
						(RandomWalkRequest) processingResult.getContent());
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
