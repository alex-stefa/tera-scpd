package ro.cs.pub.pubsub.overlay;

import jade.core.behaviours.TickerBehaviour;

public class OverlayMessageInitiator extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	
	private final OverlayManager manager;
	private final OverlayId id;

	public OverlayMessageInitiator(OverlayManager manager, OverlayId id, long period) {
		super(manager.getAgent(), period);
		this.manager = manager;
		this.id = id;
	}

	@Override
	protected void onTick() {
		manager.addSubBehaviour(new OverlaySender(manager, id));
	}
}