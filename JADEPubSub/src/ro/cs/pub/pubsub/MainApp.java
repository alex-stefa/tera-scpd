package ro.cs.pub.pubsub;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

import ro.cs.pub.pubsub.facilitator.agent.Facilitator;
import ro.cs.pub.pubsub.facilitator.agent.FacilitatorArguments;
import ro.cs.pub.pubsub.util.TimerDispatcherPool;

public class MainApp {
	public static void main(String args[]) throws Exception {
		PropertiesConfiguration config = new PropertiesConfiguration(args[0]);
		MainApp mainApp = new MainApp(config);
		mainApp.start();
	}

	private PropertiesConfiguration configuration;

	public MainApp(PropertiesConfiguration configuration)
			throws StaleProxyException {
		this.configuration = configuration;
	}

	public void start() throws StaleProxyException {
		// set up the platform
		Runtime rt = Runtime.instance();
		rt.setCloseVM(true);

		// set up the main profile
		Configuration jadeConfig = configuration.subset("jade");
		Properties aProp = new Properties();
		Iterator<?> it = jadeConfig.getKeys();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) jadeConfig.getString(key);
			aProp.put(key, value);
		}
		Profile pMain = new ProfileImpl(aProp);

		// set up the main container
		AgentContainer mainContainer = rt.createMainContainer(pMain);

		// timer dispatcher
		TimerDispatcherPool.buildInstance(1);

		// facilitator
		Object[] fArgs = { new FacilitatorArguments( //
				configuration.subset("pubsub.facilitator")) };
		mainContainer.createNewAgent( //
				configuration.getString("pubsub.facilitator.name"), //
				Facilitator.class.getCanonicalName(), fArgs).start();
	}
}
