package ro.cs.pub.pubsub.overlay;

import jade.core.behaviours.ParallelBehaviour;

import java.util.HashMap;
import java.util.Map;

import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.exception.OverlayAlreadyRegistered;

public class OverlayManager extends ParallelBehaviour {
	private static final long serialVersionUID = 1L;
	
	public static final OverlayId BASE_OVERLAY_ID = new OverlayId("base");

	private final BaseAgent agent;
	private final Map<OverlayId, OverlayContext> contexts;

	public OverlayManager(BaseAgent agent) {
		super(agent, ParallelBehaviour.WHEN_ALL);
		this.agent = agent;
		this.contexts = new HashMap<OverlayId, OverlayContext>();

		addSubBehaviour(new OverlayReceiver(this));
	}

	protected BaseAgent getAgent() {
		return agent;
	}

	public void registerOverlay(OverlayId id, int maxNeighbors, int viewSize,
			long updatePeriod) {
		if (contexts.containsKey(id)) {
			throw new OverlayAlreadyRegistered();
		}

		// setup context
		NeighborProvider neighborProvider = new NeighborProvider( //
				agent, maxNeighbors);
		ViewGenerator viewGenerator = new ViewGenerator( //
				agent, neighborProvider, viewSize);
		OverlayMessageInitiator initiator = new OverlayMessageInitiator( //
				this, id, updatePeriod);
		OverlayContext context = new OverlayContext( //
				neighborProvider, viewGenerator, initiator);

		// add the context
		contexts.put(id, context);

		// add the initiation behavior
		addSubBehaviour(initiator);
	}

	public void unregisterOverlay(OverlayId id) {
		removeSubBehaviour(contexts.get(id).getInitiator());
		contexts.remove(id);
	}
	
	public OverlayContext getOverlayContext(OverlayId id) {
		return contexts.get(id);
	}
}
