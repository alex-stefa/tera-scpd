package ro.cs.pub.pubsub.tera.agent;

import jade.core.AID;

import java.util.HashMap;
import java.util.Map;

import ro.cs.pub.pubsub.Topic;

/**
 * Provides one agent for each known topic.
 * 
 * @author Mihai Paraschiv
 */
public class AccessPointProvider {
	private Map<Topic, AID> map;

	public AccessPointProvider() {
		map = new HashMap<Topic, AID>();
	}

	public void put(Topic topic, AID agent) {
		map.put(topic, agent);
	}

	public AID get(Topic topic) {
		return map.get(topic);
	}
}
