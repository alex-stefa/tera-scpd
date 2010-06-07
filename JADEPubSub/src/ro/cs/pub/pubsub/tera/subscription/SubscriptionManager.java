package ro.cs.pub.pubsub.tera.subscription;

import jade.core.AID;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.exception.TopicAlreadySubscribed;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.overlay.OverlayCommunicationInitiator;
import ro.cs.pub.pubsub.overlay.OverlayId;
import ro.cs.pub.pubsub.overlay.context.OverlayContext;
import ro.cs.pub.pubsub.overlay.context.OverlayContextFactory;
import ro.cs.pub.pubsub.randomWalk.RandomWalkGroupCallback;
import ro.cs.pub.pubsub.randomWalk.message.AgentResult;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResult;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.subscription.advertisement.AdvertisementReceiver;
import ro.cs.pub.pubsub.tera.subscription.advertisement.AdvertisementSender;

public class SubscriptionManager extends Component<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private final OverlayContextFactory overlayContextFactory;
	private final Set<Topic> subscribedTopics;

	public SubscriptionManager(TeraAgent agent,
			OverlayContextFactory overlayContextFactory, long adRoundInterval,
			int adPeersPerRound) {
		super(agent);
		this.overlayContextFactory = overlayContextFactory;
		this.subscribedTopics = new HashSet<Topic>();

		addSubBehaviour(new AdvertisementSender( //
				this, adRoundInterval, adPeersPerRound));
		addSubBehaviour(new AdvertisementReceiver(this));
	}

	/**
	 * Subscribes to a topic by adding it a local table and registering a new
	 * topic overlay.
	 * 
	 * @param topic
	 * @throws TopicAlreadySubscribed
	 */
	public void subscribe(Topic topic) throws TopicAlreadySubscribed {
		if (isSubscribed(topic)) {
			throw new TopicAlreadySubscribed();
		}

		subscribedTopics.add(topic);

		// set up the overlay
		agent.getOverlayManager().registerOverlay(topic, overlayContextFactory);

		// find an agent subscribed to the topic and join the overlay
		agent.getAccessPointManager().lookup( //
				topic, new Callback(topic), true, true);
	}

	/**
	 * Removes the subscription to a topic. Removes the registration to the
	 * topic overlay.
	 */
	public void unsubscribe(Topic topic) {
		subscribedTopics.remove(topic);

		// remove the overlay
		agent.getOverlayManager().unregisterOverlay(topic);
	}

	public boolean isSubscribed(Topic topic) {
		return subscribedTopics.contains(topic);
	}

	public Set<Topic> getSubscribedTopics() {
		return new HashSet<Topic>(subscribedTopics);
	}

	private class Callback implements RandomWalkGroupCallback {
		private final OverlayId topic;

		public Callback(OverlayId topic) {
			this.topic = topic;
		}

		@Override
		public void onEnd(Collection<RandomWalkResult> results) {
			OverlayContext context = agent.getOverlayManager()
					.getOverlayContext(topic);
			NeighborProvider np = context.getNeighborProvider();

			// add the peers to the overlay network
			for (RandomWalkResult r : results) {
				AID peer = ((AgentResult) r).getAgent();
				np.add(peer);
			}

			// force initiation
			OverlayCommunicationInitiator initiator = context.getInitiator();
			initiator.initiateCommunication();

			agent.print("subscribed to " + topic + " " + np.size());
		}
	}
}
