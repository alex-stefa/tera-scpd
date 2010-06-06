package ro.cs.pub.pubsub.overlay;

import jade.core.behaviours.TickerBehaviour;

public class OverlayCommunicationInitiator extends TickerBehaviour {
	private static final long serialVersionUID = 1L;

	private final OverlayManager manager;
	private final OverlayId id;

	public OverlayCommunicationInitiator(OverlayManager manager, OverlayId id,
			long period) {
		super(manager.getAgent(), period);
		this.manager = manager;
		this.id = id;
	}

	@Override
	protected void onTick() {
		manager.addSubBehaviour(new OverlaySender(manager, id));
	}
}