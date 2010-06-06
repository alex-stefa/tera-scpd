package tera.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SamplingUtils
{
	public static final Random rnd = new Random();
	
	public static <T> List<T> getRandomSamples(List<T> elements, int count)
	{
		if (elements == null || elements.size() == 0) return null;
		if (count >= elements.size()) return elements;
		List<Integer> unused = new ArrayList<Integer>(elements.size());
		List<T> samples = new ArrayList<T>(count);
		for (int i = 0; i < elements.size(); i++) unused.set(i, i);
		for (int i = 0; i < count; i++)
		{
			int index = rnd.nextInt(unused.size());
			samples.add(elements.get(unused.get(index)));
			unused.remove(index);
		}
		return samples;
	}
	
	public static <T> T getRandomSample(List<T> elements)
	{
		if (elements.size() == 0) return null;
		return elements.get(rnd.nextInt(elements.size()));
	}
}
