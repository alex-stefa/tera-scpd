package ro.cs.pub.pubsub.tera.lookup;

import jade.core.AID;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.Topic;
import ro.cs.pub.pubsub.agent.Component;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.randomWalk.RandomWalkGroupCallback;
import ro.cs.pub.pubsub.randomWalk.RandomWalkGroupInitiator;
import ro.cs.pub.pubsub.randomWalk.RandomWalkProcessor;
import ro.cs.pub.pubsub.randomWalk.RandomWalkResponder;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

public class AccessPointManager extends Component<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private final int peersPerLookup;
	private final int ttl;
	private final long waitFor;
	private final Map<Topic, AID> accessPoints;

	public AccessPointManager(TeraAgent agent, int lookupPeerCount,
			int lookupTTL, long lookupWaitFor) {
		super(agent);
		this.peersPerLookup = lookupPeerCount;
		this.ttl = lookupTTL;
		this.waitFor = lookupWaitFor;
		this.accessPoints = new HashMap<Topic, AID>();

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

	/**
	 * Searches for an access point to the given topic. The search is done as a
	 * random walk on a random set of peers. In addition, the random walk can
	 * also be started from the agent itself.
	 * 
	 * @param topic
	 * @param callback
	 * @param includeSelf
	 * @param waitForAll
	 */
	public void lookup(Topic topic, RandomWalkGroupCallback callback,
			boolean includeSelf, boolean waitForAll) {
		// select peers
		NeighborProvider np = agent.getOverlayManager(). //
				getOverlayContext(Names.OVERLAY_BASE).getNeighborProvider();
		Set<AID> peers = np.getRandomSet(peersPerLookup);

		if (includeSelf) {
			peers.add(agent.getAID());
		}

		// set up query
		RandomWalkQuery query = new TopicQuery(topic);

		// add the behavior
		addSubBehaviour(new RandomWalkGroupInitiator( //
				agent, callback, peers, ttl, query, //
				System.currentTimeMillis() + waitFor, //
				waitForAll));
	}
}
