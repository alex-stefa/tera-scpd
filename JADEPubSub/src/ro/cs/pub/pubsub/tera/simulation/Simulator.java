package ro.cs.pub.pubsub.tera.simulation;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

import org.apache.commons.configuration.Configuration;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.agent.BaseTickerBehaviour;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.model.EventContent;
import ro.cs.pub.pubsub.model.Topic;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.simulation.message.AgentRemovalStatus;
import ro.cs.pub.pubsub.tera.simulation.message.DroppedAgentsList;
import ro.cs.pub.pubsub.tera.simulation.message.MessageCount;

public class Simulator extends Component<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private static int pubCount = 0;

	private MessageCount messageCount;
	private AID lastKnownSimulator;
	private List<AID> droppedAgents;
	private int lastRemaining;

	public Simulator(TeraAgent agent, Configuration config) {
		super(agent);

		messageCount = new MessageCount();
		lastKnownSimulator = null;
		droppedAgents = null;
		lastRemaining = -1;

		addSubBehaviour(new MessageCountSender(agent, config
				.getInt("simulation.messageCount.reportInterval")));
		addSubBehaviour(new DroppedAgentsNotifier(agent, config
				.getInt("simulation.cyclonResiliance.checkInterval")));
		addSubBehaviour(new DroppedAgentsReceiver(agent));

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

		if (pubCount++ < 1) {
			EventPublishingTest t = new EventPublishingTest(agent, 20000, a,
					new EventContent("Hello!"));
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

		public EventPublishingTest(TeraAgent agent, long period, Topic topic,
				EventContent content) {
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

	private class DroppedAgentsNotifier extends BaseTickerBehaviour<TeraAgent> {
		private static final long serialVersionUID = 1L;

		public DroppedAgentsNotifier(TeraAgent agent, long period) {
			super(agent, period);
		}

		@Override
		protected void onTick() {
			if (droppedAgents == null)
				return;

			int remaining = 0;
			NeighborProvider neighbors = agent.getOverlayManager()
					.getOverlayContext(Names.OVERLAY_BASE)
					.getNeighborProvider();
			for (AID agentId : droppedAgents)
				if (neighbors.contains(agentId))
					remaining++;

			if (remaining != lastRemaining) {
				if (remaining == 0)
					agent.print("Flushed all dropped nodes!");
				else {
					if (lastRemaining == 0)
						agent.print(remaining + " dropped nodes came back!");
					else
						agent.print(remaining
								+ " dropped nodes remain in cache!");
				}

				lastRemaining = remaining;

				if (lastKnownSimulator == null)
					lastKnownSimulator = agent.findAgents(
							Names.SERVICE_SIMULATION).iterator().next();
				if (lastKnownSimulator == null)
					throw new RuntimeException("No Simulation Service found!");

				MessageFactory mf = agent.getMessageFactory();
				ACLMessage message = mf.buildMessage(ACLMessage.INFORM,
						Names.SIMULATION_CYCLON_RESILIANCE);
				message.addReceiver(lastKnownSimulator);

				try {
					mf.fillContent(message, new AgentRemovalStatus(remaining));
				} catch (MessageException e) {
					e.printStackTrace();
				}

				agent.send(message);
			}
		}
	}

	private class DroppedAgentsReceiver extends
			BaseTemplateBehaviour<TeraAgent> {
		private static final long serialVersionUID = 1L;

		public DroppedAgentsReceiver(TeraAgent agent) {
			super(agent, MessageTemplate.and(MessageTemplate
					.MatchProtocol(Names.SIMULATION_CYCLON_RESILIANCE),
					MessageTemplate.MatchPerformative(ACLMessage.INFORM)));
		}

		@Override
		protected void onMessage(ACLMessage message) {
			try {
				DroppedAgentsList dropped = (DroppedAgentsList) agent
						.getMessageFactory().extractContent(message);
				droppedAgents = dropped.getDroppedAgents();
				setDone();
			} catch (MessageException e) {
				e.printStackTrace();
			}

			if (droppedAgents != null && droppedAgents.contains(agent.getAID())) {
				agent.print("Received order to drop!");
				agent.doWait();
			} else
				agent.print("Received " + droppedAgents.size()
						+ " dropped list!");
		}
	}

	private class MessageCountSender extends BaseTickerBehaviour<TeraAgent> {
		private static final long serialVersionUID = 1L;

		public MessageCountSender(TeraAgent agent, long period) {
			super(agent, period);
		}

		@Override
		protected void onTick() {
			MessageFactory mf = agent.getMessageFactory();

			ACLMessage message = mf.buildMessage(ACLMessage.INFORM,
					Names.SIMULATION_MESSAGE_COUNTER);

			if (lastKnownSimulator == null)
				lastKnownSimulator = agent.findAgents(Names.SERVICE_SIMULATION)
						.iterator().next();

			if (lastKnownSimulator == null)
				throw new RuntimeException("No Simulation Service found!");

			message.addReceiver(lastKnownSimulator);

			try {
				mf.fillContent(message, messageCount);
			} catch (MessageException e) {
				e.printStackTrace();
			}

			agent.send(message);

			messageCount.clear();
		}
	}

	public void countMessage(MessageContent content, int times) {
		messageCount.count(content, times);
	}
}
