package ro.cs.pub.pubsub.randomWalk;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.util.leap.Iterator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResult;

public class RandomWalkGroupInitiator extends SequentialBehaviour {
	private static final long serialVersionUID = 1L;

	private final RandomWalkGroupCallback callback;
	private final boolean waitForAll;
	private final ParallelBehaviour main;
	private final Collection<RandomWalkResult> results;

	public RandomWalkGroupInitiator(BaseAgent agent,
			RandomWalkGroupCallback callback, Set<AID> peers, int ttl,
			RandomWalkQuery query, Long deadline, boolean waitForAll) {
		this.callback = callback;
		this.waitForAll = waitForAll;
		if (waitForAll) {
			this.main = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		} else {
			this.main = new ParallelBehaviour(ParallelBehaviour.WHEN_ANY);
		}
		this.results = new LinkedList<RandomWalkResult>();

		for (AID peer : peers) {
			main.addSubBehaviour(new RandomWalkInitiator(agent,
					Names.PROTOCOL_ACCESS_POINT_LOOKUP, peer, ttl, query,
					deadline));
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
			Iterator it = main.getChildren().iterator();
			while (it.hasNext()) {
				RandomWalkInitiator b = (RandomWalkInitiator) it.next();
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
