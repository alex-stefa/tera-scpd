package ro.cs.pub.pubsub.overlay;

import jade.core.behaviours.ParallelBehaviour;

import java.util.HashMap;
import java.util.Map;

import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.exception.OverlayAlreadyRegistered;
import ro.cs.pub.pubsub.overlay.context.OverlayContext;
import ro.cs.pub.pubsub.overlay.context.OverlayContextFactory;

/**
 * Main behavior for controlling a group of overlays.
 * 
 * An overlay is identified by an {@link OverlayId} and has an associated
 * {@link OverlayContext}. Communication between agents is done separately for
 * each overlay and is started by an {@link OverlayCommunicationInitiator}.
 */
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

	public BaseAgent getAgent() {
		return agent;
	}

	/**
	 * Registers a behavior by setting up an {@link OverlayContext} and
	 * attaching a {@link OverlayCommunicationInitiator}.
	 * 
	 * @param id
	 * @param factory
	 * @throws OverlayAlreadyRegistered
	 */
	public void registerOverlay(OverlayId id, OverlayContextFactory factory) throws OverlayAlreadyRegistered {
		if (contexts.containsKey(id)) {
			throw new OverlayAlreadyRegistered();
		}

		// set up context
		OverlayContext context = factory.buildContext(this, id);

		// add the context
		contexts.put(id, context);

		// add the initiation behavior
		addSubBehaviour(context.getInitiator());
	}
	
	/**
	 * Unregisters an overlay.
	 * 
	 * @param id
	 */
	public void unregisterOverlay(OverlayId id) {
		OverlayCommunicationInitiator initiator = contexts.get(id)
				.getInitiator();

		// TODO see if this is necessary
		initiator.stop();

		// remove the behavior and the context
		removeSubBehaviour(initiator);
		contexts.remove(id);
	}

	public OverlayContext getOverlayContext(OverlayId id) {
		return contexts.get(id);
	}
}
