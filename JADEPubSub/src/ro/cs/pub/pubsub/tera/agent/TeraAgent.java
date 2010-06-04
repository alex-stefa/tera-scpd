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
import ro.cs.pub.pubsub.tera.behaviour.MainBehaviour;
import ro.cs.pub.pubsub.tera.behaviour.initiation.InitiationReceiver;
import ro.cs.pub.pubsub.tera.behaviour.initiation.InitiationRequester;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.ViewGenerator;

public class TeraAgent extends BaseAgent {
	private static final long serialVersionUID = 1L;

	private TeraAgentContext context;

	@Override
	protected void setup() {
		super.setup();

		TeraAgentArguments args = (TeraAgentArguments) getArguments()[0];
		Configuration configuration = args.getConfiguration();

		context = new TeraAgentContext();
		context.setMessageFactory(new MessageFactory());
		context.setAccessPointProvider(new AccessPointProvider());
		context.setNeighborProvider(new NeighborProvider( //
				this, configuration.getInt("neighbors.max")));
		context.setViewGenerator(new ViewGenerator(this));

		// setup behaviors
		SequentialBehaviour root = new SequentialBehaviour(this);

		// initiation
		root.addSubBehaviour(new InitiationRequester(this, //
				configuration.getLong("initiation.detection.period")));
		root.addSubBehaviour(new InitiationReceiver(this));

		// main
		root.addSubBehaviour(new MainBehaviour(this, configuration));

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

	@Override
	public TeraAgentContext getContext() {
		return context;
	}

	/**
	 * Send a message to the logging service.
	 * 
	 * @param content
	 */
	public void sendLoggingMessage(LogMessageContent content) {
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
