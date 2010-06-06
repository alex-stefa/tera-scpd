package ro.cs.pub.pubsub.facilitator.agent;

import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.configuration.Configuration;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.facilitator.behaviour.InitiationResponder;
import ro.cs.pub.pubsub.facilitator.behaviour.LogMessageReceiver;

public class Facilitator extends BaseAgent {
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();

		FacilitatorArguments args = (FacilitatorArguments) getArguments()[0];
		Configuration configuration = args.getConfiguration();

		addBehaviour(new LogMessageReceiver(this));
		addBehaviour(new InitiationResponder(this, configuration
				.getInt("initiation.waitFor")));
	}

	@Override
	protected Collection<ServiceDescription> prepareServiceDescriptions() {
		Collection<ServiceDescription> descriptions = new LinkedList<ServiceDescription>();
		ServiceDescription sd;

		// logging
		sd = new ServiceDescription();
		sd.setType(Names.SERVICE_LOGGING);
		sd.setName(getLocalName());
		descriptions.add(sd);

		// initiation
		sd = new ServiceDescription();
		sd.setType(Names.SERVICE_INITIATION);
		sd.setName(getLocalName());
		descriptions.add(sd);

		return descriptions;
	}
}
