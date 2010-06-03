package ro.cs.pub.pubsub.agent;

import jade.core.behaviours.Behaviour;

public abstract class BaseBehaviour<A extends BaseAgent> extends Behaviour {
	private static final long serialVersionUID = 1L;

	protected final A agent;
	protected boolean isDone = false;

	public BaseBehaviour(A agent) {
		super(agent);
		this.agent = agent;
	}

	@Override
	public boolean done() {
		return isDone;
	}

	public void setDone() {
		isDone = true;
	}

	@Override
	public void reset() {
		super.reset();
		isDone = false;
	}
}
