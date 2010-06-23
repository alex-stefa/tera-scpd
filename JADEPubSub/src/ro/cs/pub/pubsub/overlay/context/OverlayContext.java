package ro.cs.pub.pubsub.overlay.context;

import jade.core.AID;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.overlay.OverlayCommunicationInitiator;
import ro.cs.pub.pubsub.overlay.view.ViewGenerator;
import ro.cs.pub.pubsub.util.LRUCache;

public class OverlayContext {
	private final NeighborProvider neighborProvider;
	private final ViewGenerator viewGenerator;
	private final OverlayCommunicationInitiator initiator;
	private LRUCache<AID> droppedNodes;

	protected OverlayContext(NeighborProvider neighborProvider,
			ViewGenerator viewGenerator, OverlayCommunicationInitiator initiator) {
		this.neighborProvider = neighborProvider;
		this.viewGenerator = viewGenerator;
		this.initiator = initiator;
		this.droppedNodes = new LRUCache<AID>(1000);
	}

	public NeighborProvider getNeighborProvider() {
		return neighborProvider;
	}

	public ViewGenerator getViewGenerator() {
		return viewGenerator;
	}

	public OverlayCommunicationInitiator getInitiator() {
		return initiator;
	}
	
	public LRUCache<AID> getDroppedNodes()
	{
		return droppedNodes;
	}
}
