package tera.protocol;

public class TopicOverlay extends CycloneOverlay
{
	private long topicId;
	
	public TopicOverlay(TeraNetworkManager tera, long topicId, int cyclonePeriod, int maxNeighborCount)
	{
		super(tera, cyclonePeriod, maxNeighborCount);
		this.topicId = topicId;	
	}
	
	public TopicOverlay(TeraNetworkManager tera, long id)
	{
		super(tera);
		this.topicId = id;	
	}
	
	public long getId()
	{
		return topicId;
	}
	

}
