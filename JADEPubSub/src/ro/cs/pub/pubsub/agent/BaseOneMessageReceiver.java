package ro.cs.pub.pubsub.agent;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Waits for a single message.
 * 
 * Based on code from {@link jade.proto.states.MsgReceiver}.
 */
public abstract class BaseOneMessageReceiver<A extends BaseAgent> extends
		Behaviour {
	private static final long serialVersionUID = 1L;

	/**
	 * A numeric constant to mean that a timeout expired.
	 */
	public static final int TIMEOUT_EXPIRED = -1001;

	/**
	 * A numeric constant to mean that the receive operation was interrupted.
	 */
	public static final int INTERRUPTED = -1002;

	/**
	 * A numeric constant to mean that the deadline for the receive operation
	 * will never expire.
	 */
	public static final int INFINITE = -1;

	protected final A agent;

	protected MessageTemplate template;
	protected Long deadline;

	private boolean received;
	private boolean expired;
	private boolean interrupted;
	private int ret;

	public BaseOneMessageReceiver(A agent, MessageTemplate template,
			Long deadline) {
		super(agent);
		this.agent = agent;
		this.template = template;
		this.deadline = deadline;
		this.received = false;
		this.expired = false;
		this.interrupted = false;
	}

	public BaseOneMessageReceiver(A agent) {
		this(agent, null, null);
	}

	public void action() {
		if (interrupted) {
			ret = INTERRUPTED;
			return;
		}

		ACLMessage msg = myAgent.receive(template);
		if (msg != null) {
			received = true;
			ret = msg.getPerformative();
			onMessage(msg);
		} else {
			if (deadline != null) {
				// if a timeout was set, then check if it is expired
				long blockTime = deadline - System.currentTimeMillis();
				if (blockTime <= 0) {
					// timeout expired
					expired = true;
					ret = TIMEOUT_EXPIRED;
					onMessage(null);
				} else {
					block(blockTime);
				}
			} else {
				block();
			}
		}
	}

	public boolean done() {
		return received || expired || interrupted;
	}

	/**
	 * @return the performative if a message arrived,
	 *         <code>TIMEOUT_EXPIRED</code> if the timeout expired or
	 *         <code>INTERRUPTED</code> if this <code>MsgReceiver</code> was
	 *         interrupted calling the <code>interrupt()</code> method.
	 **/
	public int onEnd() {
		received = false;
		expired = false;
		interrupted = false;
		return ret;
	}

	/**
	 * This is invoked when a message matching the specified template is
	 * received or the timeout has expired (the <code>message</code> parameter
	 * is null in this case).
	 */
	protected abstract void onMessage(ACLMessage message);

	/**
	 * Signal an interruption to this receiver, and cause the ongoing receive
	 * operation to abort.
	 */
	public void interrupt() {
		interrupted = true;
		restart();
	}
}
