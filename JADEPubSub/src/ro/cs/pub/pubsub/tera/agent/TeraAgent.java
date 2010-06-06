package ro.cs.pub.pubsub.tera.agent;

import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.configuration.Configuration;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.message.shared.LogMessageContent;
import ro.cs.pub.pubsub.overlay.OverlayManager;
import ro.cs.pub.pubsub.tera.initiation.InitiationReceiver;
import ro.cs.pub.pubsub.tera.initiation.InitiationRequester;
import ro.cs.pub.pubsub.tera.subscription.AccessPointProvider;

public class TeraAgent extends BaseAgent {
	private static final long serialVersionUID = 1L;

	private MessageFactory messageFactory;
	private AccessPointProvider accessPointProvider;
	private MainBehaviour mainBehaviour;

	@Override
	protected void setup() {
		super.setup();

		TeraAgentArguments args = (TeraAgentArguments) getArguments()[0];
		Configuration configuration = args.getConfiguration();

		// set up context
		messageFactory = new MessageFactory();
		accessPointProvider = new AccessPointProvider();

		// set up behaviors
		SequentialBehaviour root = new SequentialBehaviour(this);

		// initiation
		root.addSubBehaviour(new InitiationRequester(this, //
				configuration.getLong("initiation.detection.period")));
		root.addSubBehaviour(new InitiationReceiver(this));

		// main
		mainBehaviour = new MainBehaviour(this, configuration);
		root.addSubBehaviour(mainBehaviour);

		// add root behavior
		addBehaviour(root);
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

	/**
	 * Send a message to the logging service.
	 * 
	 * @param content
	 */
	public void sendLoggingMessage(LogMessageContent content) {
		try {
			ACLMessage msg;
			msg = messageFactory.buildMessage(ACLMessage.INFORM,
					Names.PROTOCOL_LOGGING);
			AID ls = findAgents(Names.SERVICE_LOGGING).iterator().next();
			msg.addReceiver(ls);
			messageFactory.fillContent(msg, content);
			send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AccessPointProvider getAccessPointProvider() {
		return accessPointProvider;
	}

	public OverlayManager getOverlayManager() {
		return mainBehaviour.getOverlayManager();
	}
}
