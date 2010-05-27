package tera.protocol;

import java.io.Serializable;

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
}
