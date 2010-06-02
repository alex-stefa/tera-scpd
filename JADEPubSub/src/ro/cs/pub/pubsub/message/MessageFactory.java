package ro.cs.pub.pubsub.message;

import jade.lang.acl.ACLMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ro.cs.pub.pubsub.exception.MessageException;

public class MessageFactory {
	public ACLMessage buildMessage(int performative, String protocol) {
		ACLMessage message = new ACLMessage(performative);
		message.setProtocol(protocol);
		return message;
	}

	/**
	 * Fills an {@link ACLMessage} with a {@link MessageContent}.
	 * 
	 * @param message
	 * @param content
	 * @throws MessageException
	 */
	public void fillContent(ACLMessage message, MessageContent content)
			throws MessageException {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(content);
			out.close();
			message.setByteSequenceContent(bos.toByteArray());
		} catch (Exception e) {
			throw new MessageException(e);
		}
	}

	/**
	 * Extracts a {@link MessageContent} from an {@link ACLMessage}.
	 * 
	 * @param message
	 * @return
	 * @throws MessageException
	 */
	public MessageContent extractContent(ACLMessage message)
			throws MessageException {
		try {
			byte[] bytes = message.getByteSequenceContent();
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bytes));
			MessageContent content = (MessageContent) in.readObject();
			in.close();
			return content;
		} catch (Exception e) {
			throw new MessageException(e);
		}
	}
}
