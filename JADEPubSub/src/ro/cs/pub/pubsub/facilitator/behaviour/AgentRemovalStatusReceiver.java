package ro.cs.pub.pubsub.facilitator.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.facilitator.agent.Facilitator;
import ro.cs.pub.pubsub.tera.simulation.message.AgentRemovalStatus;

public class AgentRemovalStatusReceiver extends
		BaseTemplateBehaviour<Facilitator> {
	private static final long serialVersionUID = 1L;

	private int flushCounter = 0;
	private PrintWriter pwLinks;
	private PrintWriter pwUndead;
	private PrintWriter pwFinished;
		
	private final static MessageTemplate template = MessageTemplate.and(
			MessageTemplate.MatchProtocol(Names.SIMULATION_CYCLON_RESILIANCE),
			MessageTemplate.MatchPerformative(ACLMessage.INFORM));

	public AgentRemovalStatusReceiver(Facilitator facilitator) {
		super(facilitator, template);
		try
		{
			pwLinks = new PrintWriter(new FileWriter("logs\\links.txt"));
			pwUndead = new PrintWriter(new FileWriter("logs\\undead.txt"));
			pwFinished = new PrintWriter(new FileWriter("logs\\finished.txt"));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			
			AgentRemovalStatus status = (AgentRemovalStatus) agent
					.getMessageFactory().extractContent(message);

			AID sender = message.getSender();
			
			for (Set<AID> refs : agent.droppedAgentReferences.values())
				refs.remove(sender);
				
			for (AID agentId : status.getAgentsRemaining())
				agent.droppedAgentReferences.get(agentId).add(sender);
			
			int linksRemain = 0;
			int agentsRemain = 0;
			for (Set<AID> refs : agent.droppedAgentReferences.values())
			{
				linksRemain += refs.size();
				if (refs.size() != 0) agentsRemain++;
			}

			if (status.getAgentsRemaining().size() == 0)
				agent.finishedAgents.add(sender);
			else
				agent.finishedAgents.remove(sender);
			
			long timestamp = System.currentTimeMillis() - agent.agentDroppingStarted;
			pwFinished.println(timestamp + ", " + agent.finishedAgents.size());
			pwLinks.println(timestamp + ", " + linksRemain);
			pwUndead.println(timestamp + ", " + agentsRemain);
			
			flushCounter++;
			if (flushCounter == 1)
			{
				pwFinished.flush();
				pwLinks.flush();
				pwUndead.flush();
				flushCounter = 0;
			}
			
			agent.print( timestamp + 
					" Finished: " + agent.finishedAgents.size() +
					" Links: " + linksRemain +
					" Undead: " + agentsRemain);

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
