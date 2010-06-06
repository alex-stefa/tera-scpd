package tera.protocol;

import java.util.Collection;
import java.util.Hashtable;
import java.util.logging.Logger;

import tera.messaging.TeraMessage;
import tera.networking.ConnectionManager;
import tera.networking.MessageHandler;
import tera.networking.ReplyRequester;
import tera.utils.TeraLoggingService;


public class TeraNetworkManager implements MessageHandler
{
	private Hashtable<Long, TopicOverlay> topics;
	private CycloneOverlay baseOverlay;
	private Node localNode;
	private ConnectionManager network;
	private Logger logger;
	private Hashtable<Long, ReplyRequester> requests;
	
	public TeraNetworkManager(int listenPort, int cyclonePeriod, int maxActiveConns)
	{
		TeraLoggingService.initLogger(listenPort);
		logger = TeraLoggingService.getLogger(listenPort);
		topics = new Hashtable<Long, TopicOverlay>(50);
		requests = new Hashtable<Long, ReplyRequester>(100);
		baseOverlay = new CycloneOverlay(this, cyclonePeriod);
		network = new ConnectionManager(this, listenPort, maxActiveConns);
		network.start();
		localNode = new Node(network.getLocalIP(), listenPort);
	}
	
	public Node getLocalNode()
	{
		return localNode;
	}
		
	public void sendMessage(TeraMessage message)
	{
		network.sendMessage(message, message.getDestination().getIpAddress(), message.getDestination().getPort());
	}

	@Override
	public synchronized void handleMessageReceived(Object oMsg)
	{
		if (!(oMsg instanceof TeraMessage))
			logger.severe("Invalid message type received!");
		
		TeraMessage message = (TeraMessage) oMsg;
		
		// TODO: handle message
		
		
	}
	
	public void addPeer(String remoteIP, int remoteListenPort)
	{
		baseOverlay.addNode(new Node(remoteIP, remoteListenPort));		
	}
	
	
	public void diconnect()
	{
		Collection<TopicOverlay> overlays = topics.values();
		for (TopicOverlay overlay : overlays) overlay.stop();
		topics.clear();
		baseOverlay.stop();
		network.drop();
	}
	
	public void subscribe(long topicId)
	{
		
		
	}
	
	public void unsubscribe(long topicId)
	{
		
		
	}
	
	public void publish(long topicId, String event)
	{
		
	}

	@Override
	public void handleReplyTimeout(Object requestMessage)
	{
		// TODO Auto-generated method stub
		
	}

}
