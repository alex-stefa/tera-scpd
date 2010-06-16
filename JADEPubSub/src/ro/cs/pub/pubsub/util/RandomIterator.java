package ro.cs.pub.pubsub.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomIterator<E> implements Iterator<E> {
	
	private static final Random localRnd = new Random();

	private final List<E> list;
	private List<Integer> available;
	private Random rnd;
	
	public RandomIterator(List<E> list) {
		this(list, localRnd);
	}
	
	public RandomIterator(List<E> list, Random rnd) {
		this.list = list;
		this.rnd = rnd;
		available = new ArrayList<Integer>(list.size());
		for (int i = 0; i < list.size(); i++) available.set(i, i);
	}

	@Override
	public boolean hasNext() {
		return available.size() > 0;
	}

	@Override
	public E next() {
		int index = rnd.nextInt(available.size());
		E result = list.get(available.get(index));
		available.remove(index);
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
