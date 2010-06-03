package ro.cs.pub.pubsub.tera.behaviour.shuffle;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.TeraAgent;

public class ShufflingResponder extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public ShufflingResponder(TeraAgent agent) {
		super(agent);
	}

	@Override
	protected MessageTemplate setupTemplate() {
		return MessageTemplate.and(
				//
				MessageTemplate.MatchProtocol(Names.PROTOCOL_SHUFFLE),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	}
	
	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getContext().getMessageFactory();
			ACLMessage reply = mf.buildMessage(ACLMessage.INFORM,
					Names.PROTOCOL_SHUFFLE);
			MessageContent content =  mf.extractContent(message);
			agent.print(content);
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}
