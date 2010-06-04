package ro.cs.pub.pubsub.tera.behaviour.randomWalk.message;

import ro.cs.pub.pubsub.tera.Topic;

public class TopicQuery implements RandomWalkQuery {
	private static final long serialVersionUID = 1L;

	private final Topic topic;

	public TopicQuery(Topic topic) {
		this.topic = topic;
	}

	public Topic getTopic() {
		return topic;
	}
}
