package ro.cs.pub.pubsub.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RandomSelection {
	/**
	 * Selects a random element from a collection.
	 * 
	 * @param <T>
	 *            a collection with at least one element
	 * @param collection
	 * @return a random element
	 */
	public static <T> T selectOne(Collection<T> collection) {
		if (collection.size() == 0) {
			throw new NullPointerException(
					"The collection must have at least one element");
		}

		List<T> list = null;
		if (collection instanceof ArrayList<?>) {
			list = (List<T>) collection;
		} else {
			list = new ArrayList<T>(collection);
		}

		int r = (int) Math.random() * list.size();

		return list.get(r);
	}
}
