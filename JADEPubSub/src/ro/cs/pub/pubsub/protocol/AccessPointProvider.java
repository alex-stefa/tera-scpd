package ro.cs.pub.pubsub.protocol;

import jade.core.AID;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides one agent for a given topic.
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
