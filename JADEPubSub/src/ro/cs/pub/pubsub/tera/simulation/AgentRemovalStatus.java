package ro.cs.pub.pubsub.tera.simulation;

import ro.cs.pub.pubsub.message.MessageContent;

public class AgentRemovalStatus implements MessageContent
{
	private static final long serialVersionUID = 1L;
	
	private int agentsRemaining;
	
	public AgentRemovalStatus(int agentsRemaining)
	{
		this.agentsRemaining = agentsRemaining; 
	}
	
	public int getAgentsRemaining()
	{
		return agentsRemaining;
	}
}
