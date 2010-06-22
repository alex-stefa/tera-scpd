package ro.cs.pub.pubsub.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.TimerDispatcher;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.util.TimerDispatcherPool;

public abstract class BaseAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private long conversationId;
	protected MessageFactory messageFactory;

	@Override
	protected void setup() {
		super.setup();

		setupTimer();

		// register services
		try {
			registerDF(prepareServiceDescriptions());
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		messageFactory = new MessageFactory();
	}

	private void setupTimer() {
		Class<?> cls = this.getClass();
		while (!cls.equals(Agent.class)) {
			cls = cls.getSuperclass();
		}

		Field field;
		try {
			field = cls.getDeclaredField("theDispatcher");
			field.setAccessible(true);
			TimerDispatcher dispatcher = TimerDispatcherPool.getInstance()
					.select();
			field.set(this, dispatcher);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates service descriptions to be used in setup().
	 */
	protected abstract Collection<ServiceDescription> prepareServiceDescriptions();

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

	public Set<AID> findAgents(String serviceType, SearchConstraints constraints) {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
		template.addServices(sd);

		DFAgentDescription[] ds;
		try {
			ds = DFService.search(this, template, constraints);
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

	public Set<AID> findAgents(String serviceType) {
		return findAgents(serviceType, null);
	}

	public String generateConversationId() {
		long cId = conversationId++;
		return "C" + getAID() + "_" + System.currentTimeMillis() + "_" + cId;
	}

	// CONTEXT

	public MessageFactory getMessageFactory() {
		return messageFactory;
	}

	// OUTPUT

	public void print(Object str) {
		System.out.println(getAID().getLocalName() + ": " + str);
	}

	@Override
	public String toString() {
		return getAID().getLocalName();
	}
	
	public void sendMessage(ACLMessage message)
	{
		send(message);
	}
}
