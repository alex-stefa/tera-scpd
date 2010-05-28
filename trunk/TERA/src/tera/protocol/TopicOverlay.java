package tera.protocol;

public class TopicOverlay extends CycloneOverlay
{
	private long id;
	
	public TopicOverlay(TeraNetworkManager tera, long id, int cyclonePeriod)
	{
		super(tera, cyclonePeriod);
		this.id = id;	
	}
	
	public TopicOverlay(TeraNetworkManager tera, long id)
	{
		super(tera);
		this.id = id;	
	}
	
	public long getId()
	{
		return id;
	}
	

}
