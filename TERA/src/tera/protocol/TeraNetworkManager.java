package tera.protocol;

import java.util.Collection;
import java.util.Hashtable;
import java.util.logging.Logger;

import tera.messaging.TeraMessage;
import tera.networking.ConnectionManager;
import tera.networking.MessageHandler;
import tera.utils.TeraLoggingService;


public class TeraNetworkManager implements MessageHandler
{
	private Hashtable<Long, TopicOverlay> topics;
	private CycloneOverlay baseOverlay;
	private Node localNode;
	private ConnectionManager network;
	private Logger logger;
	
	public TeraNetworkManager(int listenPort, int cyclonePeriod)
	{
		logger = TeraLoggingService.getLogger(listenPort);
		topics = new Hashtable<Long, TopicOverlay>(50);
		baseOverlay = new CycloneOverlay(this, cyclonePeriod);
		network = new ConnectionManager(this, listenPort);
		network.start();
		localNode = new Node(network.getLocalIP(), listenPort);
	}
	
	public Node getLocalNode()
	{
		return localNode;
	}
		
	public void sendMessage(TeraMessage message)
	{
		network.sendMessage(message, message.destination().getIpAddress(), message.destination().getPort());
	}

	@Override
	public void handleMessage(Object oMsg)
	{
		if (!(oMsg instanceof TeraMessage))
			logger.severe("Invalid message type received!");
		
		TeraMessage message = (TeraMessage) oMsg;
		
		// TODO: handle message
		
		
	}
	
	public void addPeer(String remoteIP, int remoteListenPort)
	{
		
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

}
