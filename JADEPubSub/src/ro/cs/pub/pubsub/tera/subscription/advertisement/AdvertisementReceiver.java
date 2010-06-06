package ro.cs.pub.pubsub.tera.subscription.advertisement;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.subscription.AccessPointProvider;
import ro.cs.pub.pubsub.tera.subscription.SubscriptionManager;

/**
 * Receives subscription advertisements and saves them using the agent's
 * {@link AccessPointProvider}.
 */
public class AdvertisementReceiver extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public AdvertisementReceiver(SubscriptionManager manager) {
		super(manager.getAgent());
	}

	@Override
	protected MessageTemplate setupTemplate() {
		return MessageTemplate.and(
				//
				MessageTemplate
						.MatchProtocol(Names.PROTOCOL_TOPIC_ADVERTISEMENT),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getMessageFactory();
			AdvertisementMessage ad = (AdvertisementMessage) mf
					.extractContent(message);

			// add the topics
			Set<Topic> adTopics = ad.getTopics();
			AccessPointProvider app = agent.getAccessPointProvider();
			for (Topic topic : adTopics) {
				app.put(topic, message.getSender());
			}
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}
