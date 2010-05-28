package tera.utils;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TeraLoggingService
{
	public static final String logsFolder = "logs";
	
	public static void initLogger(int listenPort)
	{
		Logger logger = Logger.getLogger("tera.local-peer-" + listenPort);
		logger.setLevel(Level.ALL);
		try { logger.addHandler(new FileHandler(logsFolder + "\\local-peer-" + listenPort + ".txt")); }
		catch (Exception ex) { ex.printStackTrace(); }
	}
	
	public static void initSimulationLogger()
	{
		Logger logger = Logger.getLogger("tera.simulator");
		logger.setLevel(Level.ALL);
		try { logger.addHandler(new FileHandler(logsFolder + "\\simulation.txt")); }
		catch (Exception ex) { ex.printStackTrace(); }
	}
		
	public static synchronized Logger getLogger(int listenPort)
	{
		return Logger.getLogger("tera.local-peer-" + listenPort);
	}

	public static synchronized Logger getSimulationLogger()
	{
		return Logger.getLogger("tera.simulator");
	}
}
