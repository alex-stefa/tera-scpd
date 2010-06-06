package ro.cs.pub.pubsub.tera.lookup;

import jade.core.AID;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.randomWalk.RandomWalkCallback;
import ro.cs.pub.pubsub.randomWalk.RandomWalkMultipleInitiator;
import ro.cs.pub.pubsub.randomWalk.RandomWalkProcessor;
import ro.cs.pub.pubsub.randomWalk.RandomWalkResponder;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

public class AccessPointManager extends Component<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private Map<Topic, AID> accessPoints;
	private final int peersPerLookup;
	private final int ttl;

	public AccessPointManager(TeraAgent agent, int peersPerLookup, int ttl) {
		super(agent);
		this.accessPoints = new HashMap<Topic, AID>();
		this.peersPerLookup = peersPerLookup;
		this.ttl = ttl;

		// add access point lookup responder
		NeighborProvider np = agent.getOverlayManager(). //
				getOverlayContext(Names.OVERLAY_BASE).getNeighborProvider();
		String protocol = Names.PROTOCOL_ACCESS_POINT_LOOKUP;
		RandomWalkProcessor apr = new APRequestProcessor(agent);
		addSubBehaviour(new RandomWalkResponder(agent, apr, protocol, np));
	}

	public void put(Topic topic, AID agent) {
		accessPoints.put(topic, agent);
	}

	public AID get(Topic topic) {
		return accessPoints.get(topic);
	}

	public void lookup(Topic topic, RandomWalkCallback callback) {
		// select peers
		NeighborProvider np = agent.getOverlayManager(). //
				getOverlayContext(Names.OVERLAY_BASE).getNeighborProvider();
		Set<AID> peers = np.getRandomSet(peersPerLookup);

		// set up query
		RandomWalkQuery query = new TopicQuery(topic);

		// add the behavior
		addSubBehaviour(new RandomWalkMultipleInitiator( //
				agent, callback, peers, ttl, query));
	}
}
