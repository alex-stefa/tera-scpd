package ro.cs.pub.pubsub.agent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public abstract class BaseAgent extends Agent {
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();

		// register services
		try {
			registerDF(prepareServiceDescriptions());
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates service descriptions to be used in setup().
	 */
	protected abstract Collection<ServiceDescription> prepareServiceDescriptions();

	public abstract AgentContext getContext();

	/**
	 * Registers agent services.
	 * 
	 * @param services
	 * @throws FIPAException
	 */
	protected void registerDF(Collection<ServiceDescription> services)
			throws FIPAException {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		for (ServiceDescription sd : services) {
			dfd.addServices(sd);
		}
		DFService.register(this, dfd);
	}

	/**
	 * Searches for agents.
	 * 
	 * @param serviceType
	 */
	public Set<AID> findAgents(String serviceType) {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
		template.addServices(sd);

		DFAgentDescription[] ds;
		try {
			ds = DFService.search(this, template);
		} catch (FIPAException e) {
			throw new RuntimeException(e);
		}

		Set<AID> agents = new HashSet<AID>();
		for (DFAgentDescription d : ds) {
			agents.add(d.getName());
		}

		agents.remove(getAID());

		return agents;
	}

	public void print(Object str) {
		System.out.println(getAID().getLocalName() + ": " + str);
	}

	@Override
	public String toString() {
		return getAID().getLocalName();
	}
}
