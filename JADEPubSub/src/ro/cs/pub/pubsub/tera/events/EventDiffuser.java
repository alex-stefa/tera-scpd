package ro.cs.pub.pubsub.tera.events;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.exception.TopicNotSubscribed;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.message.shared.LogMessageContent;
import ro.cs.pub.pubsub.model.Event;
import ro.cs.pub.pubsub.overlay.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.util.LRUCache;

public class EventDiffuser extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	private LRUCache<Event> prevMessages;

	private static final MessageTemplate template = MessageTemplate.and(
			//
			MessageTemplate.MatchProtocol(Names.PROTOCOL_EVENT_PROPAGATION),
			MessageTemplate.MatchPerformative(ACLMessage.INFORM));

	public EventDiffuser(TeraAgent agent, int cacheSize) {
		super(agent, template);
		prevMessages = new LRUCache<Event>(cacheSize);
	}

	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getMessageFactory();
			Event event = (Event) mf.extractContent(message);

			if (prevMessages.contains(event))
				return;
			prevMessages.put(event);
			agent.sendLoggingMessage(new LogMessageContent(System.currentTimeMillis() + ""));

			if (!this.agent.getSubscriptionManager().isSubscribed(
					event.getTopic()))
				throw new TopicNotSubscribed(event.getTopic());

			NeighborProvider neighbors = this.agent.getOverlayManager()
					.getOverlayContext(event.getTopic()).getNeighborProvider();

			if (neighbors.size() == 0)
				return;

			ACLMessage forward = mf.buildMessage( //
					ACLMessage.INFORM, Names.PROTOCOL_EVENT_PROPAGATION);

			String receivers = "";
			for (AID peer : neighbors) {
				if (!message.getSender().equals(peer)) {
					forward.addReceiver(peer);
					receivers += peer.getLocalName() + " ";
				}
			}

			mf.fillContent(forward, event);

			agent.sendMessage(forward);

			agent.print(" received " + event + " from "
					+ message.getSender().getLocalName()
					+ " forwarding to other " + neighbors.size() + " ["
					+ receivers + "]");

		} catch (MessageException e) {
			e.printStackTrace();
		}
	}
}
