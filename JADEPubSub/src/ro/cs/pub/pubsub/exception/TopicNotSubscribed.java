package ro.cs.pub.pubsub.exception;

import ro.cs.pub.pubsub.model.Topic;

public class TopicNotSubscribed extends RuntimeException
{
	private Topic topic;
	
	public TopicNotSubscribed(Topic topic)
	{
		this.topic = topic;
	}

	private static final long serialVersionUID = 1L;
	
	public Topic getTopic()
	{
		return topic;
	}
}
