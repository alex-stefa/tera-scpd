package ro.cs.pub.pubsub.agent.tera;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Collection;
import java.util.LinkedList;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.agent.tera.behaviour.TeraNetDetector;
import ro.cs.pub.pubsub.message.LoggingMessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;

public class TeraAgent extends BaseAgent {
	private static final long serialVersionUID = 1L;

	private static final long DISCOVERY_UPDATE_PERIOD = 100;

	private TeraAgentContext context;

	@Override
	protected void setup() {
		super.setup();

		context = new TeraAgentContext(new MessageFactory());

		addBehaviour(new TeraNetDetector(this, DISCOVERY_UPDATE_PERIOD));
	}

	@Override
	protected Collection<ServiceDescription> prepareServiceDescriptions() {
		Collection<ServiceDescription> descriptions = new LinkedList<ServiceDescription>();
		ServiceDescription sd;

		sd = new ServiceDescription();
		sd.setType(Names.SERVICE_TERA);
		sd.setName(getLocalName());
		descriptions.add(sd);

		return descriptions;
	}

	@Override
	public TeraAgentContext getContext() {
		return context;
	}

	/**
	 * Send a message to the logging service.
	 * 
	 * @param content
	 */
	public void sendLoggingMessage(LoggingMessageContent content) {
		try {
			MessageFactory mf = context.getMessageFactory();
			ACLMessage msg;
			msg = mf.buildMessage(ACLMessage.INFORM, Names.PROTOCOL_LOGGING);
			AID ls = findAgents(Names.SERVICE_LOGGING).iterator().next();
			msg.addReceiver(ls);
			mf.fillContent(msg, content);
			send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
