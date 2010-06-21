package ro.cs.pub.pubsub.facilitator.agent;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.facilitator.behaviour.InitiationResponder;
import ro.cs.pub.pubsub.facilitator.behaviour.LogMessageReceiver;
import ro.cs.pub.pubsub.facilitator.behaviour.MessageCountPrinter;
import ro.cs.pub.pubsub.facilitator.behaviour.MessageCountReceiver;
import ro.cs.pub.pubsub.tera.simulation.messageCounter.MessageCount;


public class Facilitator extends BaseAgent
{
	private static final long serialVersionUID = 1L;

	private final Set<AID> allAgents = new HashSet<AID>();
	private final Hashtable<AID, MessageCount> messageCounts = new Hashtable<AID, MessageCount>();

	@Override
	protected void setup()
	{
		super.setup();

		FacilitatorArguments args = (FacilitatorArguments) getArguments()[0];
		Configuration configuration = args.getConfiguration();

		addBehaviour(new LogMessageReceiver(this));
		addBehaviour(new InitiationResponder(this, configuration.getInt("initiation.waitFor")));
		addBehaviour(new MessageCountReceiver(this));
		addBehaviour(new MessageCountPrinter(this, configuration.getInt("simulation.messageCount.printInterval")));
	}

	@Override
	protected Collection<ServiceDescription> prepareServiceDescriptions()
	{
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

		// simulation
		sd = new ServiceDescription();
		sd.setType(Names.SERVICE_SIMULATION);
		sd.setName(getLocalName());
		descriptions.add(sd);

		return descriptions;
	}

	public void addAgent(AID agent)
	{
		allAgents.add(agent);
	}

	public List<AID> getAllAgents()
	{
		return new ArrayList<AID>(allAgents);
	}

	public void addMessageCount(AID sender, MessageCount count)
	{
		MessageCount prevCount = messageCounts.get(sender);
		if (prevCount == null)
			messageCounts.put(sender, count);
		else
			prevCount.add(count);
	}

	public MessageCount getTotalMessageCount()
	{
		MessageCount total = new MessageCount();
		for (MessageCount count : messageCounts.values())
			total.add(count);
		return total;
	}
}
