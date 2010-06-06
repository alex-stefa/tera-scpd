package ro.cs.pub.pubsub.overlay;


public class OverlayContext {
	private final NeighborProvider neighborProvider;
	private final ViewGenerator viewGenerator;
	private final OverlayMessageInitiator initiator;
	
	public OverlayContext(NeighborProvider neighborProvider,
			ViewGenerator viewGenerator,
			OverlayMessageInitiator initiator) {
		this.neighborProvider = neighborProvider;
		this.viewGenerator = viewGenerator;
		this.initiator = initiator;
	}
	
	public NeighborProvider getNeighborProvider() {
		return neighborProvider;
	}
	
	public ViewGenerator getViewGenerator() {
		return viewGenerator;
	}
	
	public OverlayMessageInitiator getInitiator() {
		return initiator;
	}
}
