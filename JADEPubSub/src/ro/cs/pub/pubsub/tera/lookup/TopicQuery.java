package ro.cs.pub.pubsub.tera.lookup;

import ro.cs.pub.pubsub.model.Topic;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkQuery;

public class TopicQuery implements RandomWalkQuery {
	private static final long serialVersionUID = 1L;

	private final Topic topic;

	public TopicQuery(Topic topic) {
		this.topic = topic;
	}

	public Topic getTopic() {
		return topic;
	}

	@Override
	public String toString() {
		return "TopicQuery [topic=" + topic + "]";
	}
}
