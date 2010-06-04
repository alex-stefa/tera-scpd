package ro.cs.pub.pubsub.tera.agent;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Collection;
import java.util.LinkedList;

import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseAgent;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.message.shared.LoggingMessageContent;
import ro.cs.pub.pubsub.tera.behaviour.TeraNetDetector;
import ro.cs.pub.pubsub.tera.behaviour.randomWalk.RandomWalkResponder;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.ShufflingInitiator;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.ShufflingResponder;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.ViewGenerator;

public class TeraAgent extends BaseAgent {
	private static final long serialVersionUID = 1L;

	private static final long DISCOVERY_UPDATE_PERIOD = 100;
	private static final int DISCOVERY_AGENT_COUNT = 10;
	private static final long SHUFFLING_PERIOD = 10000;
	private static final int NEIGHBORS_MAX = 30;
	private static final int SHUFFLING_VIEW_SIZE = 5;

	private TeraAgentContext context;

	@Override
	protected void setup() {
		super.setup();
		
		context = new TeraAgentContext();
		context.setMessageFactory(new MessageFactory());
		context.setAccessPointProvider(new AccessPointProvider());
		context.setNeighborProvider(new NeighborProvider(NEIGHBORS_MAX));
		context.setViewGenerator(new ViewGenerator(this));

		// start the agent at a random time
//		addBehaviour(new WakerBehaviour(this, (int)Math.random() * 500) {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onWake() {
//				setupBehaviors();
//			}
//		});
		
		setupBehaviors();
	}

	protected void setupBehaviors() {
		addBehaviour(new TeraNetDetector(this, //
				DISCOVERY_UPDATE_PERIOD, DISCOVERY_AGENT_COUNT));
		addBehaviour(new RandomWalkResponder(this));
		addBehaviour(new ShufflingInitiator(this, SHUFFLING_PERIOD, SHUFFLING_VIEW_SIZE));
		addBehaviour(new ShufflingResponder(this));
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

	@Override
	public TeraAgentContext getContext() {
		return context;
	}

	/**
	 * Send a message to the logging service.
	 * 
	 * @param content
	 */
	public void sendLoggingMessage(LoggingMessageContent content) {
		try {
			MessageFactory mf = context.getMessageFactory();
			ACLMessage msg;
			msg = mf.buildMessage(ACLMessage.INFORM, Names.PROTOCOL_LOGGING);
			AID ls = findAgents(Names.SERVICE_LOGGING).iterator().next();
			msg.addReceiver(ls);
			mf.fillContent(msg, content);
			send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
