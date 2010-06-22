package ro.cs.pub.pubsub.tera.subscription;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.model.Topic;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.lookup.AccessPointManager;

/**
 * Receives subscription advertisements and saves them using the agent's
 * {@link AccessPointProvider}.
 */
public class AdvertisementReceiver extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private static final MessageTemplate template = MessageTemplate.and(
			//
			MessageTemplate.MatchProtocol(Names.PROTOCOL_TOPIC_ADVERTISEMENT),
			MessageTemplate.MatchPerformative(ACLMessage.INFORM));;

	public AdvertisementReceiver(SubscriptionManager manager) {
		super(manager.getAgent(), template);
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getMessageFactory();
			AdvertisementMessage ad = (AdvertisementMessage) mf
					.extractContent(message);

			// add the topics
			Set<Topic> adTopics = ad.getTopics();
			AccessPointManager apm = agent.getAccessPointManager();
			for (Topic topic : adTopics) {
				apm.put(topic, message.getSender());
				if (agent.getSubscriptionManager().isSubscribed(topic)) {
					NeighborProvider np = agent.getOverlayManager()
							.getOverlayContext(topic).getNeighborProvider();
					if (!np.isFull()) {
						np.add(message.getSender());
					}
				}
			}
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}
