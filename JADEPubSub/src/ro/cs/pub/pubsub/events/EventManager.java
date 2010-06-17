package ro.cs.pub.pubsub.events;

import jade.lang.acl.ACLMessage;

import java.util.Collection;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.model.Event;
import ro.cs.pub.pubsub.model.EventContent;
import ro.cs.pub.pubsub.model.Topic;
import ro.cs.pub.pubsub.randomWalk.RandomWalkGroupCallback;
import ro.cs.pub.pubsub.randomWalk.message.AgentResult;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResult;
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
		agent.getAccessPointManager().lookup(topic, new Callback(topic, eventContent), true, true);
	}
	
	private class Callback implements RandomWalkGroupCallback
	{
		private Topic topic;
		private EventContent content;
		
		public Callback(Topic topic, EventContent content)
		{
			super();
			this.topic = topic;
			this.content = content;
		}

		@Override
		public void onEnd(Collection<RandomWalkResult> results)
		{
			if (results == null || results.size() == 0)
			{
				agent.print("[ERROR] Could not find access point for sending event on topic " + topic);
				return;
			}

			MessageFactory mf = agent.getMessageFactory();

			ACLMessage message = mf.buildMessage( //
					ACLMessage.INFORM, Names.PROTOCOL_EVENT_PROPAGATION);

			for (RandomWalkResult result : results) {
				message.addReceiver(((AgentResult) result).getAgent());
			}
			
			publishedCount++;
			MessageContent messageContent = new Event(agent.getAID(), 
					publishedCount + "", topic, content);

			try {
				mf.fillContent(message, messageContent);
			}
			catch (MessageException e) {
				e.printStackTrace();
			}
			
			agent.send(message);
		}
	}
}
