package ro.cs.pub.pubsub.facilitator.behaviour;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.facilitator.agent.Facilitator;
import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;

public class LogMessageReceiver extends BaseTemplateBehaviour<Facilitator> {
	private static final long serialVersionUID = 1L;

	public LogMessageReceiver(Facilitator agent) {
		super(agent);
	}

	@Override
	protected MessageTemplate setupTemplate() {
		return MessageTemplate.and(
				//
				MessageTemplate.MatchProtocol(Names.PROTOCOL_LOGGING),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getMessageFactory();
			MessageContent content = mf.extractContent(message);
			agent.print(content);
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}
