package ro.cs.pub.pubsub.agent;

import jade.core.behaviours.ParallelBehaviour;

public class Component<A extends BaseAgent> extends ParallelBehaviour {
	private static final long serialVersionUID = 1L;
	
	protected A agent;
	
	public Component(A agent) {
		super(agent, ParallelBehaviour.WHEN_ALL);
		this.agent = agent;
	}
	
	public A getAgent() {
		return agent;
	}
}
