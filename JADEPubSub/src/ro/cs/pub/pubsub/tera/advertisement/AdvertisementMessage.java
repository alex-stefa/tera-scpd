package ro.cs.pub.pubsub.tera.advertisement;

import java.util.Set;

import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.message.MessageContent;

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
