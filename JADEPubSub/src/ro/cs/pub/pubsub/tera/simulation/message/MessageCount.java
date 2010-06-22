package ro.cs.pub.pubsub.tera.simulation.message;

import ro.cs.pub.pubsub.message.MessageContent;
import ro.cs.pub.pubsub.message.shared.LogMessageContent;
import ro.cs.pub.pubsub.model.Event;
import ro.cs.pub.pubsub.overlay.OverlayMessage;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkRequest;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResponse;
import ro.cs.pub.pubsub.tera.subscription.AdvertisementMessage;

public class MessageCount implements MessageContent {
	private static final long serialVersionUID = 1L;

	private static final int typeCount = 8;

	public static final int TYPE_LOGGING = 0;
	public static final int TYPE_EVENT = 1;
	public static final int TYPE_SUBSCRIPTION_ADVERTISEMENT = 2;
	public static final int TYPE_SHUFFLE_REQUEST = 3;
	public static final int TYPE_SHUFFLE_RESPONSE = 4;
	public static final int TYPE_LOOKUP_REQUEST = 5;
	public static final int TYPE_LOOKUP_RESPONSE = 6;
	public static final int TYPE_LOOKUP_FORWARD = 7;

	private int[] counter;

	public MessageCount() {
		counter = new int[typeCount];
		clear();
	}

	public void clear() {
		for (int i = 0; i < typeCount; i++)
			counter[i] = 0;
	}

	public MessageCount(MessageCount other) {
		this();
		for (int i = 0; i < typeCount; i++)
			counter[i] = other.counter[i];
	}

	public void add(MessageCount other) {
		for (int i = 0; i < typeCount; i++)
			counter[i] += other.counter[i];
	}

	public int sum() {
		int sum = 0;
		for (int i = 0; i < typeCount; i++)
			sum += counter[i];
		return sum;
	}

	public void count(MessageContent message) {
		count(message, 1);
	}
	
	public void count(MessageContent message, int times) {
		if (message == null)
			return;

		if (message instanceof LogMessageContent)
			counter[TYPE_LOGGING] += times;

		if (message instanceof Event)
			counter[TYPE_EVENT] += times;

		if (message instanceof AdvertisementMessage)
			counter[TYPE_SUBSCRIPTION_ADVERTISEMENT] += times;

		if (message instanceof OverlayMessage) {
			if (((OverlayMessage) message).isReply())
				counter[TYPE_SHUFFLE_RESPONSE] += times;
			else
				counter[TYPE_SHUFFLE_REQUEST] += times;
		}

		if (message instanceof RandomWalkRequest)
			counter[TYPE_LOOKUP_REQUEST] += times;

		if (message instanceof RandomWalkResponse)
			counter[TYPE_LOOKUP_RESPONSE] += times;
	}

	@Override
	public String toString() {
		String s = "[MSG COUNT] ";
		for (int i = 0; i < typeCount; i++)
			s += counter[i] + " | ";
		return s;
	}
}
