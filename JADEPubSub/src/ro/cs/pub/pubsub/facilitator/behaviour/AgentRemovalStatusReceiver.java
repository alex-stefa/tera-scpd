package ro.cs.pub.pubsub.facilitator.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.facilitator.agent.Facilitator;
import ro.cs.pub.pubsub.tera.simulation.message.AgentRemovalStatus;

public class AgentRemovalStatusReceiver extends
		BaseTemplateBehaviour<Facilitator> {
	private static final long serialVersionUID = 1L;

	private final static MessageTemplate template = MessageTemplate.and(
			MessageTemplate.MatchProtocol(Names.SIMULATION_CYCLON_RESILIANCE),
			MessageTemplate.MatchPerformative(ACLMessage.INFORM));

	public AgentRemovalStatusReceiver(Facilitator facilitator) {
		super(facilitator, template);
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			
			agent.print("Finished: " + agent.finishedAgents.size());
			
			AgentRemovalStatus status = (AgentRemovalStatus) agent
					.getMessageFactory().extractContent(message);

			AID sender = message.getSender();

			if (status.getAgentsRemaining() == 0)
				agent.finishedAgents.add(sender);
			else
				agent.finishedAgents.remove(sender);

			if (agent.finishedAgents.size() == agent.getAgentCount()
					&& agent.agentDroppingCompleted < 0) {
				agent.agentDroppingCompleted = System.currentTimeMillis();
				agent
						.print("\n\n\n"
								+ "**************************************************************\n"
								+ "   Dropped agents flushed after: "
								+ (agent.agentDroppingCompleted - agent.agentDroppingStarted)
								+ " ms\n"
								+ "**************************************************************\n\n");
			}
		} catch (MessageException e) {
			e.printStackTrace();
		}

	}

}
