package ro.cs.pub.pubsub.tera.simulation.message;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

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
	
	private List<Long>[] counter;


	@SuppressWarnings("unchecked")
	public MessageCount() {
		counter = new LinkedList[typeCount];
		for (int i = 0; i < typeCount; i++) counter[i] = new LinkedList<Long>();
		clear();
	}

	public void clear() {
		for (int i = 0; i < typeCount; i++)
			counter[i].clear();
	}

	public MessageCount(MessageCount other) {
		this();
		for (int i = 0; i < typeCount; i++)
		{
			counter[i].clear();
			counter[i].addAll(other.counter[i]);
		}
	}

	public void add(MessageCount other) {
		for (int i = 0; i < typeCount; i++)
			counter[i].addAll(other.counter[i]);
	}

	public int sum() {
		int sum = 0;
		for (int i = 0; i < typeCount; i++)
			sum += counter[i].size();
		return sum;
	}

	public void count(MessageContent message) {
		count(message, 1);
	}
	
	public void count(MessageContent message, int times) {
		if (message == null)
			return;
		
		long timestamp = System.currentTimeMillis();
		List<Long> additional = new LinkedList<Long>();
		for (int i = 0; i < times; i++) additional.add(timestamp);

		if (message instanceof LogMessageContent)
			counter[TYPE_LOGGING].addAll(additional);

		if (message instanceof Event)
			counter[TYPE_EVENT].addAll(additional);

		if (message instanceof AdvertisementMessage)
			counter[TYPE_SUBSCRIPTION_ADVERTISEMENT].addAll(additional);

		if (message instanceof OverlayMessage) {
			if (((OverlayMessage) message).isReply())
				counter[TYPE_SHUFFLE_RESPONSE].addAll(additional);
			else
				counter[TYPE_SHUFFLE_REQUEST].addAll(additional);
		}

		if (message instanceof RandomWalkRequest)
			counter[TYPE_LOOKUP_REQUEST].addAll(additional);

		if (message instanceof RandomWalkResponse)
			counter[TYPE_LOOKUP_RESPONSE].addAll(additional);
	}

	@Override
	public String toString() {
		String s = "[MSG COUNT] ";
		for (int i = 0; i < typeCount; i++)
			s += counter[i] + " | ";
		return s;
	}

	public void print(PrintWriter pw, int typeEvent)
	{
		for (long timestamp : counter[typeEvent])
			pw.println(timestamp);
		
	}
}
