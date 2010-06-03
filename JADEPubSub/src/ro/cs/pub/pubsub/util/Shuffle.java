package ro.cs.pub.pubsub.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Shuffle {
	public static <T> T shuffle(Collection<T> collection,
			Collection<T> removable) {
		if (collection.size() == 0) {
			return null;
		}

		List<T> list = new ArrayList<T>(collection);
		for (T r : removable) {
			list.remove(r);
		}
		int r = (int) Math.random() * list.size();

		return list.get(r);
	}

	public static <T> T shuffle(Collection<T> collection, T removable) {
		LinkedList<T> list = new LinkedList<T>();
		if (removable != null) {
			list.add(removable);
		}
		return shuffle(collection, list);
	}

	public static <T> T shuffle(Collection<T> collection) {
		return shuffle(collection, (T)null);
	}
}
