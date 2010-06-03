package ro.cs.pub.pubsub.agent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public abstract class BaseTemplateBehaviour<A extends BaseAgent> extends
		BaseBehaviour<A> {
	private static final long serialVersionUID = 1L;

	protected final MessageTemplate template;

	public BaseTemplateBehaviour(A agent) {
		super(agent);
		this.template = setupTemplate();
	}

	protected abstract MessageTemplate setupTemplate();

	protected abstract void onMessage(ACLMessage message);

	public final void action() {
		ACLMessage msg = agent.receive(template);
		if (msg == null) {
			block();
			return;
		}

		onMessage(msg);
	}
}
