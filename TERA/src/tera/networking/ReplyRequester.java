package tera.networking;

import java.util.Timer;
import java.util.TimerTask;

import tera.messaging.ConversationMessage;


public class ReplyRequester
{
	private MessageHandler receiver;
	private ConversationMessage requestMessage;
	private Timer timer;
	private boolean timedOut;
	private int replyCount;
	private int maxReplyCount;
	
	private TimerTask timeoutTask = new TimerTask(){
		@Override
		public void run()
		{
			timedOut = true;
			receiver.handleReplyTimeout(requestMessage);
		}
	};

	public ReplyRequester(MessageHandler receiver, ConversationMessage requestMessage)
	{
		this(receiver, requestMessage, -1, 1);
	}
	
	public ReplyRequester(MessageHandler receiver, ConversationMessage requestMessage, int maxReplyCount)
	{
		this(receiver, requestMessage, -1, maxReplyCount);
	}
	
	public ReplyRequester(MessageHandler receiver, ConversationMessage requestMessage, int maxReplyCount, long timeout)
	{
		this.timedOut = false;
		this.replyCount = 0;
		this.receiver = receiver;
		this.requestMessage = requestMessage;
		this.maxReplyCount = maxReplyCount;
		timer = null;
		if (timeout > 0)
		{
			timer = new Timer();
			timer.schedule(timeoutTask, timeout);
		}
	}
	
	public void notifyHandler(ConversationMessage message)
	{
		if (message.getConversationId() != requestMessage.getConversationId()) return;
		if (timedOut || replyCount >= maxReplyCount) return;

		if (timer != null && replyCount == 0)
		{
			timer.cancel();
			timer.purge();
		}
		
		replyCount++;
		receiver.handleMessageReceived(message);
	}
	
	public void cancel()
	{
		if (timer != null)
		{
			timer.cancel();
			timer.purge();
		}
		timedOut = true;
	}
	
	public boolean isTimedOut()
	{
		return timedOut;
	}
	
	public int getReplyCount()
	{
		return replyCount;
	}
	
	public int getMaxReplyCount()
	{
		return maxReplyCount;
	}
}
