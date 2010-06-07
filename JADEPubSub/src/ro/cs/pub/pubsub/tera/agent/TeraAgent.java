package ro.cs.pub.pubsub.tera.agent;

import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.configuration.Configuration;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.message.shared.LogMessageContent;
import ro.cs.pub.pubsub.overlay.OverlayManager;
import ro.cs.pub.pubsub.overlay.context.OverlayContextFactory;
import ro.cs.pub.pubsub.tera.initiation.InitiationReceiver;
import ro.cs.pub.pubsub.tera.initiation.InitiationRequester;
import ro.cs.pub.pubsub.tera.lookup.AccessPointManager;

public class TeraAgent extends BaseAgent {
	private static final long serialVersionUID = 1L;

	private MessageFactory messageFactory;
	private OverlayManager overlayManager;
	private AccessPointManager accessPointManager;

	@Override
	protected void setup() {
		super.setup();

		TeraAgentArguments args = (TeraAgentArguments) getArguments()[0];
		Configuration configuration = args.getConfiguration();

		// set up context
		messageFactory = new MessageFactory();

		// set up behaviors
		SequentialBehaviour root = new SequentialBehaviour(this);

		// initiation
		root.addSubBehaviour(new InitiationRequester(this, //
				configuration.getLong("initiation.detection.period")));
		root.addSubBehaviour(new InitiationReceiver(this));

		// main
		ParallelBehaviour main = new ParallelBehaviour(
				ParallelBehaviour.WHEN_ALL);

		// overlay management
		overlayManager = new OverlayManager(this);
		OverlayContextFactory ocf = new OverlayContextFactory( //
				configuration.getInt("neighbors.max"), //
				configuration.getInt("shuffling.view.size"), //
				configuration.getLong("shuffling.period"));
		overlayManager.registerOverlay(Names.OVERLAY_BASE, ocf);
		main.addSubBehaviour(overlayManager);

		// access point manager
		accessPointManager = new AccessPointManager( //
				this, //
				configuration.getInt("accessPoint.lookup.peerCount"), //
				configuration.getInt("accessPoint.lookup.ttl"), //
				configuration.getLong("accessPoint.lookup.waitFor"));
		main.addSubBehaviour(accessPointManager);

		// add the main behavior
		root.addSubBehaviour(main);

		// add root behavior
		addBehaviour(root);
	}

	@Override
	protected Collection<ServiceDescription> prepareServiceDescriptions() {
		Collection<ServiceDescription> descriptions = new LinkedList<ServiceDescription>();
		ServiceDescription sd;

		sd = new ServiceDescription();
		sd.setType(Names.SERVICE_TERA);
		sd.setName(getLocalName());
		descriptions.add(sd);

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
			send(msg);
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
}
