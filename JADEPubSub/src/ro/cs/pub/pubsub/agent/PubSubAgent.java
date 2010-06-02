package ro.cs.pub.pubsub.agent;

import ro.cs.pub.pubsub.message.MessageFactory;
import jade.core.Agent;

public class PubSubAgent extends Agent {
	private static final long serialVersionUID = 1L;
	
	private Context context;
	
	@Override
	protected void setup() {
		context = new Context(new MessageFactory());
	}
	
	public Context getContext() {
		return context;
	}
}
