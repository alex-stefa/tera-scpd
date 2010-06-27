package ro.cs.pub.pubsub.facilitator.behaviour;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.facilitator.agent.Facilitator;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.simulation.message.MessageCount;

public class MessageCountReceiver extends BaseTemplateBehaviour<Facilitator> {
	private static final long serialVersionUID = 1L;
	
	private PrintWriter pw;

	private static final MessageTemplate template = MessageTemplate.and(
			MessageTemplate.MatchProtocol(Names.SIMULATION_MESSAGE_COUNTER),
			MessageTemplate.MatchPerformative(ACLMessage.INFORM));

	public MessageCountReceiver(Facilitator agent) {
		super(agent, template);
		try
		{
			pw = new PrintWriter(new FileWriter("logs\\events.txt"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getMessageFactory();
			MessageCount count = (MessageCount) mf.extractContent(message);
			agent.addMessageCount(message.getSender(), count);
			count.print(pw, MessageCount.TYPE_EVENT);
			pw.flush();
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}
