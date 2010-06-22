package ro.cs.pub.pubsub.tera.subscription;

import java.util.Set;

import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.model.Topic;

public class AdvertisementMessage implements MessageContent {
	private static final long serialVersionUID = 1L;

	private final Set<Topic> topics;

	public AdvertisementMessage(Set<Topic> topics) {
		this.topics = topics;
	}

	public Set<Topic> getTopics() {
		return topics;
	}
}
