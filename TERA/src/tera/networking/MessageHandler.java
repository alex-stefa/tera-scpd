package tera.networking;

public interface MessageHandler
{
	public void handleMessageReceived(Object message);
	public void handleReplyTimeout(Object requestMessage);
}
