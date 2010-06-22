package ro.cs.pub.pubsub.facilitator.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.facilitator.Facilitator;
import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.message.shared.InitiationReply;

/**
 * Replies to an initiation request.
 * 
 * Waits for a specified number of agents to make the request and their messages
 * are added to a list of seeds. The following requests are replied immediately
 * with the set of seeds.
 */
public class InitiationResponder extends BaseTemplateBehaviour<Facilitator> {
	private static final long serialVersionUID = 1L;

	private static final MessageTemplate template = MessageTemplate.and(
			//
			MessageTemplate.MatchProtocol(Names.PROTOCOL_INITIATION),
			MessageTemplate.MatchPerformative(ACLMessage.CFP));

	private final int waitFor;
	private final List<ACLMessage> seedMessages;


	public InitiationResponder(Facilitator agent, int waitFor) {
		super(agent, template);
		this.waitFor = waitFor;
		this.seedMessages = new LinkedList<ACLMessage>();
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			if (seedMessages.size() < waitFor) {
				// add to the seed message list
				seedMessages.add(message);
				this.agent.addAgent(message.getSender());

				if (seedMessages.size() == waitFor) {
					// reply to seeders first
					for (ACLMessage msg : seedMessages) {
						sendReply(msg);
					}
				}
			} else {
				sendReply(message);
			}
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	private void sendReply(ACLMessage message) throws MessageException {
		MessageFactory mf = agent.getMessageFactory();
		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.PROPOSE);

		// select neighbors
		Set<AID> neighbors = new HashSet<AID>();
		for (ACLMessage sm : seedMessages) {
			AID n = sm.getSender();
			if (!n.equals(message.getSender())) {
				// do not add the sender to its own list
				neighbors.add(n);
			}
		}

		MessageContent content = new InitiationReply(neighbors);
		mf.fillContent(reply, content);
		agent.send(reply);
	}
}
