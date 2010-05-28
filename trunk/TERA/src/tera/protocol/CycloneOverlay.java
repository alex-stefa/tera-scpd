package tera.protocol;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;


public class CycloneOverlay
{
	public static int DEFAULT_CYCLE_DURATION = 10;
	
	private HashSet<Node> neighbors;
	private int cyclonePeriod;
	private TeraNetworkManager tera;
	private Timer cycleTimer; 
		
	private TimerTask viewExchangeTask = new TimerTask() {
		@Override
		public void run()
		{
			basicShuffle();
		}
	};
	
	public CycloneOverlay(TeraNetworkManager tera, int cyclonePeriod)
	{
		this.tera = tera;
		this.cyclonePeriod = cyclonePeriod;
		neighbors = new HashSet<Node>(50);
		cycleTimer = new Timer();
		cycleTimer.schedule(viewExchangeTask, cyclonePeriod * 1000, cyclonePeriod * 1000);
	}
		
	public CycloneOverlay(TeraNetworkManager tera)
	{
		this(tera, DEFAULT_CYCLE_DURATION);
	}
	
	public int getCyclonePeriod()
	{
		return cyclonePeriod;
	}
	
	public void basicShuffle()
	{
		// TODO: implement shuffle
		
	}
	
	public void addNode(Node node)
	{
		neighbors.add(node);
	}
	
	public void removeNode(Node node)
	{
		neighbors.remove(node);
	}
	
	public void stop()
	{
		neighbors.clear();
		cycleTimer.cancel();
		cycleTimer.purge();
	}
}
