package ro.cs.pub.pubsub.randomWalk;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.model.Names;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResult;

public class RandomWalkGroupInitiator extends SequentialBehaviour {
	private static final long serialVersionUID = 1L;

	private final RandomWalkGroupCallback callback;
	private final boolean waitForAll;
	private final Collection<RandomWalkResult> results;
	private final Collection<RandomWalkInitiator> initiators;

	public RandomWalkGroupInitiator(BaseAgent agent,
			RandomWalkGroupCallback callback, Set<AID> peers, int ttl,
			RandomWalkQuery query, Long deadline, boolean waitForAll) {
		this.callback = callback;
		this.waitForAll = waitForAll;

		ParallelBehaviour main;
		if (waitForAll) {
			main = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		} else {
			main = new ParallelBehaviour(ParallelBehaviour.WHEN_ANY);
		}

		this.results = new LinkedList<RandomWalkResult>();

		this.initiators = new LinkedList<RandomWalkInitiator>();
		for (AID peer : peers) {
			RandomWalkInitiator initiator = new RandomWalkInitiator(agent,
					Names.PROTOCOL_ACCESS_POINT_LOOKUP, peer, ttl, query,
					deadline);
			main.addSubBehaviour(initiator);
			initiators.add(initiator);
		}

		addSubBehaviour(main);
		addSubBehaviour(new OnEndBehaviour());
	}

	public Collection<RandomWalkResult> getResults() {
		return results;
	}

	public class OnEndBehaviour extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			for (RandomWalkInitiator b : initiators) {
				if (b.getResult() != null) {
					results.add(b.getResult());
					if (!waitForAll) {
						break;
					}
				}
			}

			callback.onEnd(results);
		}
	}
}
