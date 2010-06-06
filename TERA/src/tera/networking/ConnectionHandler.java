package tera.networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import tera.messaging.GreetingMessage;
 

public class ConnectionHandler
{
	private String remoteIP;
	private int remoteListenPort;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ConnectionManager connectionManager;
	private boolean closed;
	private long lastUsed;	

	class MessageReader extends Thread {
		@Override
		public void run()
		{
			readMessage();
		}};

	public ConnectionHandler(ConnectionManager connectionManager, Socket clientSocket)
	{
		try
		{
			remoteIP = clientSocket.getInetAddress().getHostAddress();
		}
		catch (Exception ex)
		{
			connectionManager.dealError("Could not get remote IP!", ex, false);
		}

		this.lastUsed = System.currentTimeMillis();
		this.connectionManager = connectionManager;
		this.socket = clientSocket;

		closed = false;
		remoteListenPort = -1;

		(new MessageReader()).start();
	}
	
	public ConnectionHandler(ConnectionManager connectionManager, String remoteIP, int remoteListenPort)
	{
		this.connectionManager = connectionManager;
		this.remoteIP = remoteIP;
		this.remoteListenPort = remoteListenPort;
		closed = true;
		connect();
	}

	private synchronized void connect()
	{
		if (remoteIP == null || remoteListenPort < 0)
			connectionManager.dealError("Cannot create connection. No remote host specified!", null, false);
		try
		{
			connectionManager.showMessage("Connecting to " + this);
			socket = new Socket(remoteIP, remoteListenPort);
			closed = false;
		}
		catch (Exception ex)
		{
			connectionManager.dealError("Could not connect to " + this + "!", ex, false);
			connectionManager.signalUnreachablePeer(this);
			close();
			return;
		}
		sendMessage(new GreetingMessage(connectionManager.getLocalIP(), connectionManager.getListenPort()));
		(new MessageReader()).start();
	}

	private void setOOS()
	{
		try
		{
			oos = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (Exception ex)
		{
			connectionManager.dealError("Could open output stream to " + this + "!", ex, false);
			close();
		}
	}

	private void setOIS()
	{
		try
		{
			ois = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception ex)
		{
			if (!closed)
			{
				connectionManager.dealError("Could not open input stream from " + this + "!", ex, false);
				close();
			}
		}
	}

	public synchronized void sendMessage(Object oMsg)
	{
		if (socket == null || !socket.isConnected()) connect();
		if (closed) return;
		if (oos == null) setOOS();
		try
		{
			oos.writeObject(oMsg);
			oos.flush();
			oos.reset();
			this.lastUsed = System.currentTimeMillis();
		}
		catch (Exception ex)
		{
			connectionManager.dealError("Could not send message to " + this + "!", ex, false);
			close();
			return;
		}
	}

	private void readMessage()
	{
		if (ois == null) setOIS();
		while (ois != null && !closed && socket.isConnected())
		{
			try
			{
				this.lastUsed = System.currentTimeMillis();
				connectionManager.handleMessage(ois.readObject(), this);
			}
			catch (Exception ex)
			{
				if (!closed)
				{
					connectionManager.dealError("Could not receive message from " + this + "!", ex, false);
					close();
				}
			}
		}
	}

	public void close()
	{
		connectionManager.showMessage("Closing connection to " + this);
		connectionManager.signalConnectionClosed(this);
		closed = true;
		if (ois != null)
			try { ois. close(); }
			catch (Exception ex) { connectionManager.dealError("Could not close input stream for " + this + "!", ex, false); }
		if (oos != null)
			try { oos. close(); }
			catch (Exception ex) { connectionManager.dealError("Could not close output stream for " + this + "!", ex, false); }
		if (socket != null)
			try { socket.close(); }
			catch (Exception ex) { connectionManager.dealError("Could not close connection with " + this + "!", ex, false); }
		ois = null;
		oos = null;
		socket = null;
		System.gc();
	}

	@Override
	public String toString()
	{
		return remoteIP + ":" + (remoteListenPort < 0 ? "?" : remoteListenPort);
	}

	public String getRemoteIP()
	{
		return remoteIP;
	}

	public int getRemoteListenPort()
	{
		return remoteListenPort;
	}
	
	public void setRemoteListenPort(int port)
	{
		remoteListenPort = port;
	}
	
	public long getLastUsed()
	{
		return lastUsed;
	}
}