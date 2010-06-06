package ro.cs.pub.pubsub.tera.subscription.advertisement;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.overlay.OverlayManager;
import ro.cs.pub.pubsub.tera.subscription.SubscriptionManager;

/**
 * Sends advertisements with the agent's own subscribed topics. Ads are at a
 * specified time period to a randomly selected set of peers.
 */
public class AdvertisementSender extends TickerBehaviour {
	private static final long serialVersionUID = 1L;

	private final SubscriptionManager manager;
	private final int peersPerRound;

	public AdvertisementSender(SubscriptionManager manager, long period, int peersPerRound) {
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
		NeighborProvider np = manager.getAgent().getOverlayManager().getOverlayContext(
				OverlayManager.BASE_OVERLAY_ID).getNeighborProvider();
		
		Set<Topic> topics = manager.getSubscribedTopics();

		if (np.size() == 0 || topics.size() == 0) {
			return;
		}

		MessageFactory mf = manager.getAgent().getMessageFactory();

		ACLMessage message = mf.buildMessage( //
				ACLMessage.INFORM, Names.PROTOCOL_TOPIC_ADVERTISEMENT);

		// select receivers randomly
		Iterator<AID> it = np.randomIterator();
		int size = 0;
		while (it.hasNext() && size < peersPerRound) {
			AID n = it.next();
			message.addReceiver(n);
			size++;
		}

		MessageContent content = new AdvertisementMessage(topics);
		mf.fillContent(message, content);
		manager.getAgent().send(message);
	}
}
