package ro.cs.pub.pubsub.facilitator.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTickerBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.facilitator.agent.Facilitator;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.simulation.message.DroppedAgentsList;
import ro.cs.pub.pubsub.util.RandomIterator;

public class AgentDropper extends BaseTickerBehaviour<Facilitator> {
	private static final long serialVersionUID = 1L;

	private int dropCount;

	public AgentDropper(Facilitator facilitator, int waitFor, int dropCount) {
		super(facilitator, waitFor);
		this.dropCount = dropCount;
	}

	@Override
	protected void onTick() {
		List<AID> dropped = new ArrayList<AID>(dropCount);
		RandomIterator<AID> iterator = new RandomIterator<AID>(agent
				.getAllAgents());
		while (dropped.size() < dropCount && iterator.hasNext())
			dropped.add(iterator.next());

		MessageFactory mf = agent.getMessageFactory();
		ACLMessage message = mf.buildMessage(ACLMessage.INFORM,
				Names.SIMULATION_CYCLON_RESILIANCE);
		for (AID agentId : agent.getAllAgents())
			message.addReceiver(agentId);

		try {
			mf.fillContent(message, new DroppedAgentsList(dropped));
		} catch (MessageException e) {
			e.printStackTrace();
		}

		agent.send(message);

		agent.agentDroppingStarted = System.currentTimeMillis();

		stop();
	}
}
