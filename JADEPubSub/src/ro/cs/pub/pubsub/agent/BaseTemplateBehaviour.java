package ro.cs.pub.pubsub.agent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public abstract class BaseTemplateBehaviour<A extends BaseAgent> extends
		BaseBehaviour<A> {
	private static final long serialVersionUID = 1L;

	protected MessageTemplate messageTemplate;

	public BaseTemplateBehaviour(A agent, MessageTemplate template) {
		super(agent);
		this.messageTemplate = template;
	}

	public final void action() {
		ACLMessage msg = agent.receive(messageTemplate);
		if (msg == null) {
			block();
			return;
		}

		onMessage(msg);
	}

	protected abstract void onMessage(ACLMessage message);
}
