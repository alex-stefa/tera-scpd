package tera.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

public class AgingCache<T> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Hashtable<T, Integer> cache;
	private int maxSize;
	private Random rnd;

	public AgingCache(int maxSize)
	{
		this.maxSize = Math.max(maxSize, 0);
		this.rnd = new Random();
		this.cache = new Hashtable<T, Integer>(maxSize + 2);
	}
	
	private Entry<T, Integer> findOldest()
	{
		Entry<T, Integer> oldestEntry = null;
		for (Entry<T, Integer> entry : cache.entrySet())
		{
			if (entry.getValue().intValue() > oldestEntry.getValue().intValue() || oldestEntry == null)
				oldestEntry = entry;
		}
		return oldestEntry;
	}
	
	public int getMaxSize()
	{
		return maxSize;
	}
	
	public int getSize()
	{
		return cache.size();
	}
	
	public CacheEntry<T> removeOldest()
	{
		Entry<T, Integer> oldestEntry = findOldest();
		if (oldestEntry == null) return null;
		cache.remove(oldestEntry.getKey());
		return new CacheEntry<T>(oldestEntry.getKey(), oldestEntry.getValue());
	}
	
	public CacheEntry<T> peekOldest()
	{
		Entry<T, Integer> oldestEntry = findOldest();
		if (oldestEntry == null) return null;
		return new CacheEntry<T>(oldestEntry.getKey(), oldestEntry.getValue());
	}
	
	public CacheEntry<T> put(CacheEntry<T> entry)
	{
		if (entry == null) return null;
		Integer prevAge = cache.get(entry.getEntry());
		int newAge = entry.getAge();
		if (prevAge != null) newAge = Math.min(newAge, prevAge.intValue());
		
		if (cache.size() < maxSize || prevAge != null)
		{
			cache.put(entry.getEntry(), newAge);
			return null;
		}

		CacheEntry<T> oldestEntry = peekOldest();
		if (oldestEntry == null) return null;
		if (oldestEntry.getAge() < newAge) return null;
		cache.remove(oldestEntry.getEntry());
		cache.put(entry.getEntry(), newAge);
		return oldestEntry;
	}
	
	public void put(Iterable<CacheEntry<T>> entries)
	{
		for (CacheEntry<T> entry : entries) put(entry);
	}
	
	@SuppressWarnings("unchecked")
	public CacheEntry<T> removeRandom()
	{
		Object[] keys = cache.keySet().toArray();
		T key = (T) keys[rnd.nextInt(keys.length)];
		int age = cache.remove(key).intValue();
		return new CacheEntry<T>(key, age);
	}
	
	public void incAge()
	{
		for (Entry<T, Integer> entry : cache.entrySet())
			entry.setValue(new Integer(entry.getValue().intValue() + 1));
	}
	
	public List<CacheEntry<T>> getEntries()
	{
		List<CacheEntry<T>> entries = new ArrayList<CacheEntry<T>>(cache.size());
		for (Entry<T, Integer> entry : cache.entrySet())
			entries.add(new CacheEntry<T>(entry.getKey(), entry.getValue()));
		return entries;
	}
	
	@SuppressWarnings("hiding")
	public class CacheEntry<T> implements Comparable<CacheEntry<T>>, Serializable
	{
		private static final long serialVersionUID = 1L;

		private T entry;
		private int age;
		
		public CacheEntry(T entry)
		{
			this.entry = entry;
			this.age = 0;
		}
		
		public CacheEntry(T entry, int age)
		{
			this.entry = entry;
			this.age = age;
		}
		
		public int getAge()
		{
			return age;
		}
		
		public T getEntry()
		{
			return entry;
		}
		
		public void setAge(int age)
		{
			this.age = age;
		}
		
		public void setEntry(T entry)
		{
			this.entry = entry;
		}
		
		public void incAge()
		{
			age++;
		}

		@Override
		public int compareTo(CacheEntry<T> other) // oldest first!
		{
			return other.getAge() - this.age; 
		}
	}
}
