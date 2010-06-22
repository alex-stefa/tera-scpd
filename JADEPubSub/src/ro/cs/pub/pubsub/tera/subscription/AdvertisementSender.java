package ro.cs.pub.pubsub.tera.subscription;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.Set;

import ro.cs.pub.pubsub.agent.BaseTickerBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.model.Names;
import ro.cs.pub.pubsub.model.Topic;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

/**
 * Sends advertisements with the agent's own subscribed topics. Ads are at a
 * specified time period to a randomly selected set of peers.
 */
public class AdvertisementSender extends BaseTickerBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private final SubscriptionManager manager;
	private final int peersPerRound;

	public AdvertisementSender(SubscriptionManager manager, long period,
			int peersPerRound) {
		super(manager.getAgent(), period);
		this.manager = manager;
		this.peersPerRound = peersPerRound;
	}

	@Override
	protected void onTick() {
		try {
			sendAdvertisement();
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	private void sendAdvertisement() throws MessageException {
		// peers are selected from the base overlay
		NeighborProvider np = agent.getOverlayManager(). //
				getOverlayContext(Names.OVERLAY_BASE).getNeighborProvider();

		Set<Topic> topics = manager.getSubscribedTopics();

		if (np.size() == 0 || topics.size() == 0) {
			return;
		}

		MessageFactory mf = agent.getMessageFactory();

		ACLMessage message = mf.buildMessage( //
				ACLMessage.INFORM, Names.PROTOCOL_TOPIC_ADVERTISEMENT);

		// select receivers randomly
		for (AID peer : np.getRandomSet(peersPerRound)) {
			message.addReceiver(peer);
		}

		MessageContent content = new AdvertisementMessage(topics);
		mf.fillContent(message, content);
		agent.send(message);
	}
}
