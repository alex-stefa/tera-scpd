package ro.cs.pub.pubsub.util;

import java.util.LinkedList;
import java.util.Queue;

public class LRUCache<T>
{
	private Queue<T> queue;
	private int maxSize;
	
	public LRUCache(int maxSize)
	{
		this.maxSize = maxSize;
		queue = new LinkedList<T>();
	}
	
	public boolean contains(T object)
	{
		return queue.contains(object);
	}
	
	public T put(T object)
	{
		queue.offer(object);
		if (queue.size() > maxSize)
			return queue.poll();
		return null;
	}
	
	public int getMaxSize()
	{
		return maxSize;
	}
	
	public int getSize()
	{
		return queue.size();
	}
}
