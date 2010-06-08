package ro.cs.pub.pubsub.agent;

import jade.core.behaviours.TickerBehaviour;

public abstract class BaseTickerBehaviour<A extends BaseAgent> extends
		TickerBehaviour {
	private static final long serialVersionUID = 1L;

	protected final A agent;
	
	public BaseTickerBehaviour(A agent, long period) {
		super(agent, period);
		this.agent = agent;
	}
}
