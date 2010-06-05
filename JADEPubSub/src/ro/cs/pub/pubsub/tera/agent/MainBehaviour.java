package ro.cs.pub.pubsub.tera.agent;

import jade.core.behaviours.ParallelBehaviour;

import org.apache.commons.configuration.Configuration;

import ro.cs.pub.pubsub.tera.neighbor.NeighborController;
import ro.cs.pub.pubsub.tera.randomWalk.behaviour.RandomWalkResponder;

public class MainBehaviour extends ParallelBehaviour {
	private static final long serialVersionUID = 1L;

	public MainBehaviour(TeraAgent agent, Configuration configuration) {
		super(agent, ParallelBehaviour.WHEN_ALL);

		addSubBehaviour(new RandomWalkResponder(agent));
		addSubBehaviour(new NeighborController(agent, //
				configuration.getLong("shuffling.period"), //
				configuration.getInt("shuffling.view.size")));
	}
}
