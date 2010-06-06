package tera.simulator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import tera.protocol.TeraNetworkManager;
import tera.utils.TeraLoggingService;


public class TeraSimulator implements Runnable
{
	private int portMin, portMax, maxConns;
	private List<String> peerAddresses;
	private int cyclonePeriod;
	private Logger logger;
	
	public TeraSimulator(String settingsFile)
	{
		TeraLoggingService.initSimulationLogger();
		logger = TeraLoggingService.getSimulationLogger();
		loadSettings(settingsFile);
	}
		
	private void loadSettings(String settingsFile)
	{
		portMin = 65000;
		portMax = 65010;
		cyclonePeriod = 5;
		maxConns = 5;
		peerAddresses = new LinkedList<String>();
		String peerFile = "peers.txt";

		Properties props = new Properties();
		try
		{
			props.load(new FileInputStream(settingsFile));
			portMin = Integer.parseInt(props.getProperty("port-min"));
			portMax = Integer.parseInt(props.getProperty("port-max"));
			maxConns = Integer.parseInt(props.getProperty("max-conns"));
			cyclonePeriod = Integer.parseInt(props.getProperty("cyclone-period"));
			peerFile = props.getProperty("peers-file").trim();
		}
        catch(IOException ioe)
        {
            logger.log(Level.WARNING, "Simulation config file '" + settingsFile + "' could not be found!");
            props.setProperty("port-min", portMin + "");
            props.setProperty("port-max", portMax + "");
            props.setProperty("max-conns", maxConns + "");
            props.setProperty("peers-file", peerFile);
            props.setProperty("cyclone-period", cyclonePeriod + "");
            try
            {
            	props.store(new FileOutputStream(settingsFile), "Tera simulation config file");
            	logger.log(Level.INFO, "Simulation config file '" + settingsFile + "' recreated!");
            }
            catch (Exception ex)
           	{
            	logger.log(Level.WARNING, "Could not recreate simulation config file '" + settingsFile + "'!", ex);
           	}
        }
        catch(NumberFormatException nfe)
        {
        	logger.log(Level.WARNING, "Invalid configuration data!", nfe);
        }
        try
        {
        	BufferedReader br = new BufferedReader(new FileReader(peerFile));
        	String line = null;
        	while ((line = br.readLine()) != null)
        		peerAddresses.add(line.trim());
        }
        catch (Exception ex)
        {
        	logger.log(Level.WARNING, "Could not parse peer list file!", ex);
        }
	}
		
	public void simulate()
	{
		(new Thread(this)).start();
	}

	@Override
	public void run()
	{
		logger.info("Simulation started on " + new Date());
		List<TeraNetworkManager> peers = new LinkedList<TeraNetworkManager>();
		for (int port = portMin; port <= portMax; port++)
			peers.add(new TeraNetworkManager(port, cyclonePeriod, maxConns));
		try { Thread.sleep(5000); }
		catch (Exception ex) {}
		for (TeraNetworkManager peer : peers) peer.diconnect();
		logger.info("Simulation ended on " + new Date());
	}
}
