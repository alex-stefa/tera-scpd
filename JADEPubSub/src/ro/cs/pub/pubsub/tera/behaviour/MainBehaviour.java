package ro.cs.pub.pubsub.tera.behaviour;

import jade.core.behaviours.ParallelBehaviour;

import org.apache.commons.configuration.Configuration;

import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.RandomWalkResponder;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.ShufflingInitiator;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.ShufflingResponder;

public class MainBehaviour extends ParallelBehaviour{
	private static final long serialVersionUID = 1L;
	
	public MainBehaviour(TeraAgent agent, Configuration configuration) {
		super(agent, ParallelBehaviour.WHEN_ALL);
		
		addSubBehaviour(new RandomWalkResponder(agent));
		addSubBehaviour(new ShufflingInitiator(agent, //
				configuration.getLong("shuffling.period"), //
				configuration.getInt("shuffling.view.size")));
		addSubBehaviour(new ShufflingResponder(agent));
	}
}
