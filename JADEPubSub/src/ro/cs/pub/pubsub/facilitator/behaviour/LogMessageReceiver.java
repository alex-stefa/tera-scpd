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
import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;

public class LogMessageReceiver extends BaseTemplateBehaviour<Facilitator> {
	private static final long serialVersionUID = 1L;
	
	private PrintWriter pw;

	private final static MessageTemplate template = MessageTemplate.and(
			//
			MessageTemplate.MatchProtocol(Names.PROTOCOL_LOGGING),
			MessageTemplate.MatchPerformative(ACLMessage.INFORM));

	public LogMessageReceiver(Facilitator agent) {
		super(agent, template);
		try
		{
			pw = new PrintWriter(new FileWriter("logs\\log.txt"));
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
			MessageContent content = mf.extractContent(message);
			agent.print(content);
			pw.println(content);
			pw.flush();
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}
