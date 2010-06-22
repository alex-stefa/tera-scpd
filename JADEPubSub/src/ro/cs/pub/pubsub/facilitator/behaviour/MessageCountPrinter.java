package ro.cs.pub.pubsub.facilitator.behaviour;

import ro.cs.pub.pubsub.agent.BaseTickerBehaviour;
import ro.cs.pub.pubsub.facilitator.agent.Facilitator;

public class MessageCountPrinter extends BaseTickerBehaviour<Facilitator> {
	private static final long serialVersionUID = 1L;

	public MessageCountPrinter(Facilitator agent, long period) {
		super(agent, period);
	}

	@Override
	protected void onTick() {
		agent.print(agent.getTotalMessageCount());
	}
}
