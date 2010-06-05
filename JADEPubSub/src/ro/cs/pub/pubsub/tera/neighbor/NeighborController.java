package ro.cs.pub.pubsub.tera.neighbor;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;

public class NeighborController extends ParallelBehaviour {
	private static final long serialVersionUID = 1L;

	private final TeraAgent agent;
	private final int viewSize;
	private final ViewGenerator viewGenerator;

	public NeighborController(TeraAgent agent, long period, int viewSize) {
		super(agent, ParallelBehaviour.WHEN_ALL);
		this.agent = agent;
		this.viewSize = viewSize;
		this.viewGenerator = new ViewGenerator(agent);

		addSubBehaviour(new ShufflingInitiator(agent, period));
		addSubBehaviour(new NeighborReceiver(this));
	}

	protected TeraAgent getAgent() {
		return agent;
	}

	protected int getViewSize() {
		return viewSize;
	}

	protected ViewGenerator getViewGenerator() {
		return viewGenerator;
	}

	private class ShufflingInitiator extends TickerBehaviour {
		private static final long serialVersionUID = 1L;

		public ShufflingInitiator(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void onTick() {
			addSubBehaviour(new NeighborSender(NeighborController.this));
		}
	}
}
