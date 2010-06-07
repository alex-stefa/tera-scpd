package ro.cs.pub.pubsub.randomWalk;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.util.leap.Iterator;

import java.util.Set;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkQuery;
import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResult;

public class RandomWalkMultipleInitiator extends SequentialBehaviour {
	private static final long serialVersionUID = 1L;

	private final RandomWalkCallback callback;
	private final ParallelBehaviour main;

	private RandomWalkResult result;

	public RandomWalkMultipleInitiator(BaseAgent agent,
			RandomWalkCallback callback, Set<AID> peers, int ttl,
			RandomWalkQuery query, Long deadline) {
		this.callback = callback;

		this.main = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);

		for (AID peer : peers) {
			main.addSubBehaviour(new RandomWalkInitiator(agent,
					Names.PROTOCOL_ACCESS_POINT_LOOKUP, peer, ttl, query,
					deadline));
		}

		addSubBehaviour(main);
		addSubBehaviour(new OnEndBehaviour());
	}

	public RandomWalkResult getResult() {
		return result;
	}

	public class OnEndBehaviour extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			Iterator it = main.getChildren().iterator();
			while (it.hasNext() && result == null) {
				RandomWalkInitiator b = (RandomWalkInitiator) it.next();
				result = b.getResult();
			}

			callback.onSuccess(result);
		}
	}
}
