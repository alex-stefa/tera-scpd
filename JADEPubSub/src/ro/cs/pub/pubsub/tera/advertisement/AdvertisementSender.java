package ro.cs.pub.pubsub.tera.advertisement;

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
import ro.cs.pub.pubsub.tera.agent.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

public class AdvertisementSender extends TickerBehaviour {
	private static final long serialVersionUID = 1L;

	private final TeraAgent agent;
	private final int peersPerRound;

	public AdvertisementSender(TeraAgent agent, long period, int peersPerRound) {
		super(agent, period);
		this.agent = agent;
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
		NeighborProvider np = agent.getNeighborProvider();
		Set<Topic> topics = agent.getSubscribedTopics();

		if (np.size() == 0 || topics.size() == 0) {
			return;
		}

		MessageFactory mf = agent.getMessageFactory();

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
		agent.send(message);
	}
}
