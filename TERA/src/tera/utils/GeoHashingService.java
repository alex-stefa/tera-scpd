package tera.utils;

import tera.protocol.Node;


public class GeoHashingService
{
	public static long GetGeoHash(String ip)
	{
		// TODO: find public geohashing services 
		
		String[] bytes = ip.split("[.]");
		assert bytes.length == 4;
		
		long hash = 0;
		for (int i = 0; i < 4; i++)
			hash += (1 << (8 * i)) * Integer.parseInt(bytes[i]);

		return hash;
	}

	public static long getDistance(Node first, Node second)
	{
		if (first.getGeoHash() == second.getGeoHash())
			return Math.abs(first.getPort() - second.getPort());
		else
			return Math.abs(first.getGeoHash() - second.getGeoHash());
	}
}
