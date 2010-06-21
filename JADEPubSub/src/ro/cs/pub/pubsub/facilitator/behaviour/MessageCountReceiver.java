package ro.cs.pub.pubsub.facilitator.behaviour;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.facilitator.agent.Facilitator;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.simulation.MessageCount;


public class MessageCountReceiver extends BaseTemplateBehaviour<Facilitator>
{
	private static final long serialVersionUID = 1L;

	private static final MessageTemplate template = MessageTemplate.and(
			MessageTemplate.MatchProtocol(Names.SIMULATION_MESSAGE_COUNTER),
			MessageTemplate.MatchPerformative(ACLMessage.INFORM));

	public MessageCountReceiver(Facilitator agent)
	{
		super(agent, template);
	}

	@Override
	protected void onMessage(ACLMessage message)
	{
		try
		{
			MessageFactory mf = agent.getMessageFactory();
			MessageCount count = (MessageCount) mf.extractContent(message);
			agent.addMessageCount(message.getSender(), count);
		}
		catch (MessageException e)
		{
			e.printStackTrace();
		}
	}
}
