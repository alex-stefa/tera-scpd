package ro.cs.pub.pubsub.tera.subscription;

import jade.core.behaviours.ParallelBehaviour;

import java.util.HashSet;
import java.util.Set;

import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.overlay.context.OverlayContextFactory;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.subscription.advertisement.AdvertisementReceiver;
import ro.cs.pub.pubsub.tera.subscription.advertisement.AdvertisementSender;

public class SubscriptionManager extends ParallelBehaviour {
	private static final long serialVersionUID = 1L;

	private final TeraAgent agent;
	private final OverlayContextFactory overlayContextFactory;
	private final Set<Topic> subscribedTopics;

	public SubscriptionManager(TeraAgent agent,
			OverlayContextFactory overlayContextFactory, long adSendInterval,
			int adPeersPerRound) {
		this.agent = agent;
		this.overlayContextFactory = overlayContextFactory;
		this.subscribedTopics = new HashSet<Topic>();

		addSubBehaviour(new AdvertisementSender( //
				this, adSendInterval, adPeersPerRound));
		addSubBehaviour(new AdvertisementReceiver(this));
	}

	public TeraAgent getAgent() {
		return agent;
	}

	public void subscribe(Topic topic) {
		subscribedTopics.add(topic);

		// set up the overlay
		agent.getOverlayManager().registerOverlay(topic, overlayContextFactory);
	}

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
}
