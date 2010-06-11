package ro.cs.pub.pubsub.tera.simulation;

import ro.cs.pub.pubsub.agent.BaseTickerBehaviour;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.model.Topic;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

public class Simulator extends Component<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public Simulator(TeraAgent agent) {
		super(agent);

		double p = Math.random();
		if (p < 0.5) {
			TopicSubscriptionTest t = new TopicSubscriptionTest(agent, 3000,
					new Topic("A"));
			addSubBehaviour(t);
		} else {
			TopicSubscriptionTest t = new TopicSubscriptionTest(agent, 6000,
					new Topic("A"));
			addSubBehaviour(t);
		}
		if (p < 0.2) {
			TopicSubscriptionTest t = new TopicSubscriptionTest(agent, 6000,
					new Topic("B"));
			addSubBehaviour(t);
		}
	}

	private class TopicSubscriptionTest extends BaseTickerBehaviour<TeraAgent> {
		private static final long serialVersionUID = 1L;

		private final Topic topic;

		public TopicSubscriptionTest(TeraAgent agent, long period, Topic topic) {
			super(agent, period);
			this.topic = topic;
		}

		@Override
		protected void onTick() {
			agent.print("started " + topic);
			agent.getSubscriptionManager().subscribe(topic);
			stop();
		}
	}
}
