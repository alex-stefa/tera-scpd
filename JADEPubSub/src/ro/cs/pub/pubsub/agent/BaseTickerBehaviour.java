package ro.cs.pub.pubsub.agent;

import java.util.TimerTask;

/**
 * This class was created to allow a large number of ticker behaviors to be
 * added on an agent.
 */
public abstract class BaseTickerBehaviour<A extends BaseAgent> extends
		BaseBehaviour<A> {
	private static final long serialVersionUID = 1L;

	protected final A agent;
	private final long period;

	/**
	 * The minimum time left until the {@link #onTick()} is called.
	 */
	private long wakeupTime;

	/**
	 * The current wake up task.
	 */
	private TimerTask task;

	public BaseTickerBehaviour(A agent, long period) {
		super(agent);
		this.agent = agent;
		this.period = period;
	}

	public void onStart() {
		startTimer();
	}

	private synchronized void startTimer() {
		wakeupTime = System.currentTimeMillis() + period;

		if (task != null) {
			task.cancel();
		}

		task = new TimerTask() {
			@Override
			public void run() {
				restart();
			}
		};
		agent.getTimer().schedule(task, period);
	}

	@Override
	public void restart() {
		super.restart();

		startTimer();
	}

	@Override
	public final void action() {
		long blockTime = wakeupTime - System.currentTimeMillis();
		if (blockTime <= 0) {
			onTick();
			wakeupTime = System.currentTimeMillis() + period;

			// Maybe this behavior has been removed within the onTick() method
			if (myAgent != null && !done()) {
				restart();
			}
		} else {
			block();
		}
	}

	protected abstract void onTick();

	public void stop() {
		setDone();
	}
}
