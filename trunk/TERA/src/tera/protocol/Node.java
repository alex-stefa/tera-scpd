package tera.protocol;

import java.io.Serializable;
import java.util.
import tera.utils.GeoHashingService;


public class Node implements Serializable
{
	private static final long serialVersionUID = 1L;

	private long id;
	private long geoHash;
	private String address;
	private int port;

	public Node(long id, String address, int port)
	{
		this.id = id;
		this.address = address;
		this.port = port;
		this.geoHash = GeoHashingService.GetGeoHash(address);
	}
	
	public Node(String address, int port)
	{
		this.address = address;
		this.port = port;
		this.geoHash = GeoHashingService.GetGeoHash(address);
		this.id = this.geoHash * (1 << 16) + port;
	}

	@Override
	public String toString()
	{
		return id + "@" + address + ":" + port;
	}

	public String getIpAddress()
	{
		return address;
	}

	public int getPort()
	{
		return port;
	}

	public long getID()
	{
		return id;
	}

	public long getGeoHash()
	{
		return geoHash;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Node)) return false;
		Node node = (Node) other;
		return this.id == node.getID();
	}

}
