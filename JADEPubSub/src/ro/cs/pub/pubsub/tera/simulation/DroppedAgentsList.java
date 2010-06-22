package ro.cs.pub.pubsub.tera.simulation;

import jade.core.AID;

import java.util.List;

import ro.cs.pub.pubsub.message.MessageContent;

public class DroppedAgentsList implements MessageContent
{
	private static final long serialVersionUID = 1L;
	
	private List<AID> droppedAgents;

	public DroppedAgentsList(List<AID> droppedAgents)
	{
		this.droppedAgents = droppedAgents;
	}
	
	public List<AID> getDroppedAgents()
	{
		return droppedAgents;
	}
}
