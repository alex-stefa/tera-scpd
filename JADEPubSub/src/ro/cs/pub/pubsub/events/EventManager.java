package ro.cs.pub.pubsub.events;

import jade.lang.acl.ACLMessage;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.model.Event;
import ro.cs.pub.pubsub.model.EventContent;
import ro.cs.pub.pubsub.model.Topic;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;


public class EventManager extends Component<TeraAgent>
{
	private static final long serialVersionUID = 1L;
	
	private int publishedCount; 

	public EventManager(TeraAgent agent, int oldMessageCacheSize)
	{
		super(agent);
		addSubBehaviour(new EventDiffuser(agent, oldMessageCacheSize));
		publishedCount = 0;
	}
	
	
	public void publish(Topic topic, EventContent eventContent)
	{
		
		MessageFactory mf = agent.getMessageFactory();

		ACLMessage message = mf.buildMessage( //
				ACLMessage.INFORM, Names.PROTOCOL_EVENT_PROPAGATION);

		// select receivers
		//for (AID peer : ?)) {
		//	message.addReceiver(peer);
		//}

		publishedCount++;
		MessageContent content = new Event(agent.getAID(), 
				agent.getAID().toString() + publishedCount, topic, eventContent);

		try {
			mf.fillContent(message, content);
		}
		catch (MessageException e) {
			e.printStackTrace();
		}
		
		agent.send(message);
	}
}
