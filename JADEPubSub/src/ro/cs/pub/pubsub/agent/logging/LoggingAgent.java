package ro.cs.pub.pubsub.agent.logging;

import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Collection;
import java.util.LinkedList;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.AgentContext;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.agent.logging.behaviour.LoggingMessageReceiver;
import ro.cs.pub.pubsub.message.MessageFactory;

public class LoggingAgent extends BaseAgent {
	private static final long serialVersionUID = 1L;

	private AgentContext context;

	@Override
	protected void setup() {
		super.setup();

		this.context = new AgentContext(new MessageFactory());

		addBehaviour(new LoggingMessageReceiver(this));
	}

	@Override
	protected Collection<ServiceDescription> prepareServiceDescriptions() {
		Collection<ServiceDescription> descriptions = new LinkedList<ServiceDescription>();
		ServiceDescription sd;

		sd = new ServiceDescription();
		sd.setType(Names.SERVICE_LOGGING);
		sd.setName(getLocalName());
		descriptions.add(sd);

		return descriptions;
	}

	@Override
	public AgentContext getContext() {
		return context;
	}
}
