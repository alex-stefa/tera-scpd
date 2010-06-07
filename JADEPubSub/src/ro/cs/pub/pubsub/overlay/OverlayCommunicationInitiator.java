package ro.cs.pub.pubsub.overlay;

import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.agent.BaseTickerBehaviour;

public class OverlayCommunicationInitiator extends
		BaseTickerBehaviour<BaseAgent> {
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
		initiateCommunication();
	}

	public void initiateCommunication() {
		manager.addSubBehaviour(new OverlaySender(manager, id));
	}
}