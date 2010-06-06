package ro.cs.pub.pubsub.tera.agent;

import jade.core.behaviours.ParallelBehaviour;

import org.apache.commons.configuration.Configuration;

import ro.cs.pub.pubsub.overlay.OverlayManager;
import ro.cs.pub.pubsub.overlay.context.OverlayContextFactory;
import ro.cs.pub.pubsub.tera.randomWalk.RandomWalkResponder;

public class MainBehaviour extends ParallelBehaviour {
	private static final long serialVersionUID = 1L;

	private OverlayManager overlayManager;

	public MainBehaviour(TeraAgent agent, Configuration configuration) {
		super(agent, ParallelBehaviour.WHEN_ALL);

		// overlay management
		overlayManager = new OverlayManager(agent);
		OverlayContextFactory ocf = new OverlayContextFactory( //
				configuration.getInt("neighbors.max"), //
				configuration.getInt("shuffling.view.size"), //
				configuration.getLong("shuffling.period"));
		overlayManager.registerOverlay(OverlayManager.BASE_OVERLAY_ID, ocf);
		addSubBehaviour(overlayManager);

		// random walk
		addSubBehaviour(new RandomWalkResponder(agent));
	}

	public OverlayManager getOverlayManager() {
		return overlayManager;
	}
}
