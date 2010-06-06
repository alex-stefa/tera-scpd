package ro.cs.pub.pubsub.overlay.context;

import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.overlay.OverlayCommunicationInitiator;
import ro.cs.pub.pubsub.overlay.OverlayId;
import ro.cs.pub.pubsub.overlay.OverlayManager;
import ro.cs.pub.pubsub.overlay.view.ViewGenerator;

public class OverlayContextFactory {
	private final int maxNeighbors;
	private final int viewSize;
	private final long updatePeriod;

	public OverlayContextFactory(int maxNeighbors, int viewSize,
			long updatePeriod) {
		this.maxNeighbors = maxNeighbors;
		this.viewSize = viewSize;
		this.updatePeriod = updatePeriod;
	}

	public OverlayContext buildContext(OverlayManager manager,
			OverlayId overlayId) {
		NeighborProvider neighborProvider = new NeighborProvider( //
				manager, maxNeighbors);
		ViewGenerator viewGenerator = new ViewGenerator( //
				manager, neighborProvider, viewSize);
		OverlayCommunicationInitiator initiator = new OverlayCommunicationInitiator( //
				manager, overlayId, updatePeriod);

		return new OverlayContext(neighborProvider, viewGenerator, initiator);
	}
}
