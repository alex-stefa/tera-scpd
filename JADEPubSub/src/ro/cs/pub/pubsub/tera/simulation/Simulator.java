package ro.cs.pub.pubsub.tera.simulation;

import jade.core.AID;
import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.agent.BaseTickerBehaviour;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

public class Simulator extends Component<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public Simulator(TeraAgent agent) {
		super(agent);

		if (agent.getAID().equals(new AID("a_1", false))) {
			TopicSubscriptionTest t = new TopicSubscriptionTest(agent, 3000);
			addSubBehaviour(t);
		}
	}

	private class TopicSubscriptionTest extends BaseTickerBehaviour<TeraAgent> {
		private static final long serialVersionUID = 1L;

		private final Topic topic;

		public TopicSubscriptionTest(TeraAgent agent, long period) {
			super(agent, period);
			topic = new Topic("topic");
		}

		@Override
		protected void onTick() {
			if (!agent.getSubscriptionManager().isSubscribed(topic)) {
				agent.print("started");
				agent.getSubscriptionManager().subscribe(topic);
			}
		}
	}
}
