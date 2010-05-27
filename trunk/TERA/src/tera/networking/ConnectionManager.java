package tera.networking;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import tera.messaging.GreetingMessage;
import tera.utils.TeraLoggingService;


public class ConnectionManager 
{
	private int listenPort;
	private boolean running;
	private ConnectionListener listener;
	private MessageHandler messageHandler;
	private Logger logger;

	private Hashtable<String, ConnectionHandler> connections;
	
	public ConnectionManager(MessageHandler messageHandler, int listenPort)
	{
		this.messageHandler = messageHandler;
		this.listenPort = listenPort;
		this.logger = TeraLoggingService.getLogger(listenPort);
		connections = new Hashtable<String, ConnectionHandler>(50);
		running = false;
	}

	public int getListenPort()
	{
		return listenPort;
	}

	public String getLocalIP()
	{
		String result = listener.getLocalIP();
		if (result == null) 
			try { result = InetAddress.getLocalHost().getHostAddress(); } 
			catch(Exception ex) {};
		if (result == null) result = "";
		return result;
	}

	public boolean isRunning()
	{
		return running;
	}	

	public void dealError(String message, Exception exception, boolean isFatal)
	{
		if (isFatal)
		{
			logger.log(Level.SEVERE, message, exception);
			drop();
		}
		else
			logger.log(Level.WARNING, message, exception);
	}

	public void showMessage(String message)
	{
		logger.log(Level.INFO, message);
	}

	public synchronized void start()
	{
		if (running) return;
		listener = new ConnectionListener(this);
		listener.start();
		running = true;
	}

	public synchronized void drop()
	{
		if (!running) return;
		running = false;
		Collection<ConnectionHandler> cconns = connections.values();
		for (ConnectionHandler conn : cconns) conn.close();
		connections.clear();
		if (listener != null) listener.close();
	}
	
	public void handleMessage(Object oMsg, ConnectionHandler connectionHandler)
	{
		if (oMsg instanceof GreetingMessage)
		{
			GreetingMessage msg = (GreetingMessage) oMsg;
			connectionHandler.setRemoteListenPort(msg.sourceListenPort());
			connections.put(connectionHandler.toString(), connectionHandler);
			showMessage(connectionHandler + " says '" + msg.message() + "'");
		}
		else
			if (messageHandler != null) messageHandler.handleMessage(oMsg);
	}

	public void sendMessage(Object oMsg, String remoteIP, int remoteListenPort)
	{
		assert(remoteListenPort >= 0);
		String hash = remoteIP + ":" + remoteListenPort;
		ConnectionHandler handler = connections.get(hash);
		if (handler == null)
		{
			handler = new ConnectionHandler(this, remoteIP, remoteListenPort);
			connections.put(handler.toString(), handler);
		}
		handler.sendMessage(oMsg);	
	}
}
