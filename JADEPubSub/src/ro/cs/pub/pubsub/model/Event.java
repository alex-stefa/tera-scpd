package ro.cs.pub.pubsub.model;

import jade.core.AID;
import ro.cs.pub.pubsub.message.MessageContent;

public class Event implements MessageContent {
	private static final long serialVersionUID = 1L;

	protected final AID source;
	protected final String id;
	protected final Topic topic;
	protected final EventContent content;

	public Event(AID source, String id, Topic topic, EventContent content) {
		this.source = source;
		this.id = id;
		this.topic = topic;
		this.content = content;
	}

	public AID getSource() {
		return source;
	}

	public String getId() {
		return id;
	}

	public Topic getTopic() {
		return topic;
	}

	public EventContent getContent() {
		return content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
}
