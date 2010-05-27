package tera.networking;

import java.net.ServerSocket;
import java.net.SocketException;


public class ConnectionListener extends Thread
{
	private ConnectionManager connectionManager;
	private ServerSocket serverSocket;

	public ConnectionListener(ConnectionManager connectionManager)
	{
		super("Connection Listening Thread");
		this.connectionManager = connectionManager;
	}

	public void run()
	{
		try
		{
			serverSocket = new ServerSocket(connectionManager.getListenPort());
		}
		catch (Exception ex)
		{
			connectionManager.dealError("Server socket creation error", ex, true);
		}

		if (serverSocket == null) return;

		while (connectionManager.isRunning())
		{
			try
			{
				new ConnectionHandler(connectionManager, serverSocket.accept());
			}
			catch (SocketException se) {}
			catch (Exception ex)
			{
				connectionManager.dealError("Server socket listening error", ex, true);
			}
		}
	}

	public void close()
	{
		if (serverSocket == null) return;

		try
		{
			serverSocket.close();
		}
		catch (Exception ex)
		{
			connectionManager.dealError("Server socket closing error", ex, true);
		}
	}
	
	public String getLocalIP()
	{
		if (serverSocket == null) return null;
		return serverSocket.getInetAddress().getHostAddress();
	}
}

