package ro.cs.pub.pubsub.tera.subscription;

import jade.core.AID;

import java.util.HashSet;
import java.util.Set;

import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.overlay.OverlayCommunicationInitiator;
import ro.cs.pub.pubsub.overlay.OverlayId;
import ro.cs.pub.pubsub.overlay.context.OverlayContext;
import ro.cs.pub.pubsub.overlay.context.OverlayContextFactory;
import ro.cs.pub.pubsub.randomWalk.RandomWalkCallback;
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
			OverlayContextFactory overlayContextFactory, long adSendInterval,
			int adPeersPerRound) {
		super(agent);
		this.overlayContextFactory = overlayContextFactory;
		this.subscribedTopics = new HashSet<Topic>();

		addSubBehaviour(new AdvertisementSender( //
				this, adSendInterval, adPeersPerRound));
		addSubBehaviour(new AdvertisementReceiver(this));
	}

	public void subscribe(Topic topic) {
		subscribedTopics.add(topic);

		// set up the overlay
		agent.getOverlayManager().registerOverlay(topic, overlayContextFactory);

		// find an agent subscribed to the topic and join the overlay
		RandomWalkCallback callback = new Callback(topic);
		agent.getAccessPointManager().lookup(topic, callback);
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

	public class Callback implements RandomWalkCallback {
		private final OverlayId topic;

		public Callback(OverlayId topic) {
			this.topic = topic;
		}

		@Override
		public void onSuccess(RandomWalkResult result) {
			OverlayContext context = agent.getOverlayManager()
					.getOverlayContext(topic);

			// add the peer to the overlay network
			AID peer = ((AgentResult) result).getAgent();
			context.getNeighborProvider().add(peer);

			// force initiation
			OverlayCommunicationInitiator initiator = context.getInitiator();
			initiator.initiateCommunication();
		}
	}
}
