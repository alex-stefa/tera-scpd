package tera.utils;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class TeraLoggingService
{
	public static final String logsFolder = "logs";
	
	public static void initLogger(int listenPort)
	{
		Logger logger = Logger.getLogger("tera.local-peer-" + listenPort);
		logger.setLevel(Level.ALL);
		try
		{ 
			FileHandler handler = new FileHandler(logsFolder + File.separator + "local-peer-" + listenPort + ".log");
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
		}
		catch (Exception ex) { ex.printStackTrace(); }
	}
	
	public static void initSimulationLogger()
	{
		Logger logger = Logger.getLogger("tera.simulator");
		logger.setLevel(Level.ALL);
		try
		{ 
			FileHandler handler = new FileHandler(logsFolder + File.separator + "simulation.log");
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
		}
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
