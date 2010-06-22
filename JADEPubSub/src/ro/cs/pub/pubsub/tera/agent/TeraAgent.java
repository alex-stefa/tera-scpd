package ro.cs.pub.pubsub.tera.agent;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.configuration.Configuration;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.message.shared.LogMessageContent;
import ro.cs.pub.pubsub.overlay.OverlayManager;
import ro.cs.pub.pubsub.overlay.context.OverlayContextFactory;
import ro.cs.pub.pubsub.tera.events.EventManager;
import ro.cs.pub.pubsub.tera.initiation.InitiationReceiver;
import ro.cs.pub.pubsub.tera.initiation.InitiationRequester;
import ro.cs.pub.pubsub.tera.lookup.AccessPointManager;
import ro.cs.pub.pubsub.tera.simulation.Simulator;
import ro.cs.pub.pubsub.tera.subscription.SubscriptionManager;

public class TeraAgent extends BaseAgent {
	private static final long serialVersionUID = 1L;

	private MessageFactory messageFactory;
	private OverlayManager overlayManager;
	private AccessPointManager accessPointManager;
	private SubscriptionManager subscriptionManager;
	private EventManager eventManager;
	private Simulator simulator;

	@Override
	protected void setup() {
		super.setup();

		TeraAgentArguments args = (TeraAgentArguments) getArguments()[0];
		Configuration c = args.getConfiguration();

		// set up context
		messageFactory = new MessageFactory();

		// set up behaviors
		SequentialBehaviour root = new SequentialBehaviour(this);

		// initiation
		root.addSubBehaviour(new InitiationRequester(this, //
				c.getLong("initiation.detection.period")));
		root.addSubBehaviour(new InitiationReceiver(this));

		// main
		ParallelBehaviour main = new ParallelBehaviour(
				ParallelBehaviour.WHEN_ALL);

		// overlay management
		overlayManager = new OverlayManager(this);
		OverlayContextFactory ocf = new OverlayContextFactory( //
				c.getInt("overlay.base.neighbors.max"), //
				c.getInt("overlay.base.view.size"), //
				c.getLong("overlay.base.initiation.period"));
		overlayManager.registerOverlay(Names.OVERLAY_BASE, ocf);
		main.addSubBehaviour(overlayManager);

		// access point manager
		accessPointManager = new AccessPointManager( //
				this, //
				c.getInt("accessPoint.lookup.peerCount"), //
				c.getInt("accessPoint.lookup.ttl"), //
				c.getLong("accessPoint.lookup.waitFor"));
		main.addSubBehaviour(accessPointManager);

		// subscription manager
		OverlayContextFactory ocfs = new OverlayContextFactory( //
				c.getInt("overlay.topic.neighbors.max"), //
				c.getInt("overlay.topic.view.size"), //
				c.getLong("overlay.topic.initiation.period"));
		subscriptionManager = new SubscriptionManager(
				this, //
				ocfs,
				c.getInt("subscriptionManager.advertisements.round.interval"),
				c.getInt("subscriptionManager.advertisements.round.peerCount"));
		main.addSubBehaviour(subscriptionManager);

		eventManager = new EventManager(this, c.getInt("event.cacheSize"));
		main.addSubBehaviour(eventManager);

		// simulator
		simulator = new Simulator(this, c);
		main.addSubBehaviour(simulator);

		// add the main behavior
		root.addSubBehaviour(main);

		// add end behavior
		root.addSubBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				TeraAgent.this.print("agent finished");
			}
		});

		// add root behavior
		addBehaviour(root);
	}

	@Override
	protected Collection<ServiceDescription> prepareServiceDescriptions() {
		Collection<ServiceDescription> descriptions = new LinkedList<ServiceDescription>();

		/*
		 * ServiceDescription sd; sd = new ServiceDescription();
		 * sd.setType(Names.SERVICE_TERA); sd.setName(getLocalName());
		 * descriptions.add(sd);
		 */

		return descriptions;
	}

	/**
	 * Send a message to the logging service.
	 * 
	 * @param content
	 */
	public void sendLoggingMessage(LogMessageContent content) {
		try {
			ACLMessage msg;
			msg = messageFactory.buildMessage(ACLMessage.INFORM,
					Names.PROTOCOL_LOGGING);
			AID ls = findAgents(Names.SERVICE_LOGGING).iterator().next();
			msg.addReceiver(ls);
			messageFactory.fillContent(msg, content);
			sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public OverlayManager getOverlayManager() {
		return overlayManager;
	}

	public AccessPointManager getAccessPointManager() {
		return accessPointManager;
	}

	public SubscriptionManager getSubscriptionManager() {
		return subscriptionManager;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	@Override
	public void sendMessage(ACLMessage message) {
		
		Set<AID> receivers = new HashSet<AID>();
		Iterator it = message.getAllIntendedReceiver();
		while (it.hasNext()) receivers.add((AID) it.next());
				
		try {
			simulator.countMessage(messageFactory.extractContent(message), receivers.size());
		} catch (MessageException e) {
			e.printStackTrace();
		}

		send(message);
	}
}
