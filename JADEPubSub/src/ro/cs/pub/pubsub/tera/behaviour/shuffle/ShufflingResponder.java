package ro.cs.pub.pubsub.tera.behaviour.shuffle;

import java.util.Iterator;
import java.util.Set;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ro.cs.pub.pubsub.Names;
import ro.cs.pub.pubsub.agent.BaseTemplateBehaviour;
import ro.cs.pub.pubsub.exception.MessageException;
import ro.cs.pub.pubsub.message.MessageFactory;
import ro.cs.pub.pubsub.tera.agent.NeighborProvider;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.behaviour.shuffle.message.ShufflingMessage;

public class ShufflingResponder extends BaseTemplateBehaviour<TeraAgent> {
	private static final long serialVersionUID = 1L;

	public ShufflingResponder(TeraAgent agent) {
		super(agent);
	}

	@Override
	protected MessageTemplate setupTemplate() {
		return MessageTemplate.and(
				//
				MessageTemplate.MatchProtocol(Names.PROTOCOL_SHUFFLING),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	}
	
	@Override
	protected void onMessage(ACLMessage message) {
		try {
			MessageFactory mf = agent.getContext().getMessageFactory();
			ShufflingMessage content = (ShufflingMessage)mf.extractContent(message);
			updateNeighbors(content.getNeighbors());
			
			ACLMessage reply = mf.buildMessage(ACLMessage.INFORM,
					Names.PROTOCOL_SHUFFLING);
			agent.print(content);
		} catch (MessageException e) {
			e.printStackTrace();
		}
	}

	private void updateNeighbors(Set<AID> incoming) {
		NeighborProvider np = agent.getContext().getNeighborProvider();
		
		// remove neighbors in a random fashion
		int viewSize = incoming.size();
		while (np.size() > np.getMaxSize() - viewSize) {
			Iterator<AID> it = np.randomIterator();
			it.next();
			it.remove();
		}
		
		// add the new neighbors
		
	}
}
