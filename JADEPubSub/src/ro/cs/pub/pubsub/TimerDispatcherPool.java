package ro.cs.pub.pubsub;

import jade.core.TimerDispatcher;

import java.util.ArrayList;
import java.util.List;

public class TimerDispatcherPool {
	private static TimerDispatcherPool instance;

	public static TimerDispatcherPool buildInstance(int size) {
		instance = new TimerDispatcherPool(size);
		return instance;
	}

	public static TimerDispatcherPool getInstance() {
		return instance;
	}

	@SuppressWarnings("unused")
	private final List<TimerDispatcher> list;

	private TimerDispatcherPool(int size) {
		list = new ArrayList<TimerDispatcher>();
		for (int i = 0; i < size; i++) {
			// list.add(new TimerDispatcher());
		}
	}

	public TimerDispatcher select() {
		// Iterator<TimerDispatcher> it = new RandomIterator<TimerDispatcher>(
		// list, new Random());
		// return it.next();
		return new TimerDispatcher();
	}
}
