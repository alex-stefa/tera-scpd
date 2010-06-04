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

	private static int CC = 0;

	private static final long DISCOVERY_UPDATE_PERIOD = 100;
	private static final int DISCOVERY_AGENT_COUNT = 5;
	private static final long SHUFFLING_PERIOD = 2000;
	private static final int NEIGHBORS_MAX = 5;
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

		addBehaviour(new TeraNetDetector(this, //
				DISCOVERY_UPDATE_PERIOD, DISCOVERY_AGENT_COUNT));
		addBehaviour(new RandomWalkResponder(this));
		addBehaviour(new ShufflingInitiator(this, SHUFFLING_PERIOD, SHUFFLING_VIEW_SIZE));
		addBehaviour(new ShufflingResponder(this));

//		if (CC < 0) {
//			addBehaviour(new WakerBehaviour(this, 2000) {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				protected void onWake() {
//					AID peer = TeraAgent.this.getContext().getNeighbors()
//							.iterator().next();
//					addBehaviour(new RandomWalkInitiator(TeraAgent.this, peer,
//							5, new DistanceQuery()));
//				}
//			});
//		}

		CC++;
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
