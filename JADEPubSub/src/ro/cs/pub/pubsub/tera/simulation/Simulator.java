package ro.cs.pub.pubsub.tera.simulation;

import ro.cs.pub.pubsub.agent.BaseTickerBehaviour;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.model.EventContent;
import ro.cs.pub.pubsub.model.Topic;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

public class Simulator extends Component<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public Simulator(TeraAgent agent) {
		super(agent);
		
		Topic a = new Topic("A");
		Topic b = new Topic("B");
		Topic c = new Topic("C");

		double p = Math.random();
		if (p < 0.5) {
			TopicSubscriptionTest t = new TopicSubscriptionTest(agent, 3000, a);
			addSubBehaviour(t);
		} else {
			TopicSubscriptionTest t = new TopicSubscriptionTest(agent, 6000, b);
			addSubBehaviour(t);
		}
		if (p < 0.2) {
			TopicSubscriptionTest t = new TopicSubscriptionTest(agent, 6000, c);
			addSubBehaviour(t);
		}
		

		if (p < 0.5) {
			EventPublishingTest t = new EventPublishingTest(agent, 10000, a, new EventContent("Hello!"));
			addSubBehaviour(t);
		} 
		if (p < 0.1) {
			EventPublishingTest t = new EventPublishingTest(agent, 15000, b, new EventContent("Hello!"));
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

	private class EventPublishingTest extends BaseTickerBehaviour<TeraAgent> {
		private static final long serialVersionUID = 1L;

		private final Topic topic;
		private final EventContent content;

		public EventPublishingTest(TeraAgent agent, long period, Topic topic, EventContent content) {
			super(agent, period);
			this.topic = topic;
			this.content = content;
		}

		@Override
		protected void onTick() {
			agent.print("publishing " + content + " on topic " + topic);
			agent.getEventManager().publish(topic, content);
			stop();
		}
	}



}
