package tera.protocol;

import java.util.Timer;
import java.util.TimerTask;

import tera.utils.AgingCache;


public class CycloneOverlay
{
	public static int DEFAULT_CYCLE_DURATION = 10;
	public static int DEAFULT_MAX_NEIGHBOR_COUNT = 10;
	
	private AgingCache<Node> neighbors;
	private int cyclonePeriod;
	private int maxNeighborCount;
	private TeraNetworkManager tera;
	private Timer cycleTimer; 
		
	private TimerTask viewExchangeTask = new TimerTask() {
		@Override
		public void run()
		{
			basicShuffle();
		}
	};
	
	public CycloneOverlay(TeraNetworkManager tera, int cyclonePeriod, int maxNeighborCount)
	{
		this.tera = tera;
		this.cyclonePeriod = cyclonePeriod;
		this.maxNeighborCount = maxNeighborCount;
		neighbors = new AgingCache<Node>(maxNeighborCount);
		cycleTimer = new Timer();
		cycleTimer.schedule(viewExchangeTask, cyclonePeriod * 1000, cyclonePeriod * 1000);
	}
		
	public CycloneOverlay(TeraNetworkManager tera)
	{
		this(tera, DEFAULT_CYCLE_DURATION, DEAFULT_MAX_NEIGHBOR_COUNT);
	}
	
	public int getCyclonePeriod()
	{
		return cyclonePeriod;
	}
	
	public int getMaxNeighborCount()
	{
		return maxNeighborCount;
	}
	
	public void basicShuffle()
	{
		// TODO: implement shuffle
		
	}
	
	public void addNode(Node node)
	{
		neighbors.put(node);
	}
	
	
	public void stop()
	{
		neighbors.clear();
		cycleTimer.cancel();
		cycleTimer.purge();
	}
}
