package ro.cs.pub.pubsub.randomWalk;

import ro.cs.pub.pubsub.randomWalk.message.RandomWalkResult;

public interface RandomWalkCallback {
	void onSuccess(RandomWalkResult result);
}
