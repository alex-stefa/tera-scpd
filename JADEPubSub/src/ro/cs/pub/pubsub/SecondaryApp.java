package ro.cs.pub.pubsub;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.agent.TeraAgentArguments;

public class SecondaryApp {
	public static void main(String args[]) throws Exception {
		PropertiesConfiguration config = new PropertiesConfiguration(args[0]);
		SecondaryApp mainApp = new SecondaryApp(config);
		mainApp.start();
	}

	private PropertiesConfiguration configuration;

	public SecondaryApp(PropertiesConfiguration configuration)
			throws StaleProxyException {
		this.configuration = configuration;
	}

	public void start() throws StaleProxyException {
		// set up the platform
		Runtime rt = Runtime.instance();
		rt.setCloseVM(true);

		// set up the profile
		Configuration jadeConfig = configuration.subset("jade");
		Properties aProp = new Properties();
		Iterator<?> it = jadeConfig.getKeys();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) jadeConfig.getString(key);
			aProp.put(key, value);
		}
		Profile p = new ProfileImpl(aProp);

		// set up the container
		AgentContainer container = rt.createAgentContainer(p);

		// timer dispatcher
		TimerDispatcherPool.buildInstance(configuration
				.getInt("pubsub.timerdispatcher.pool.size"));

		// TERA agents
		Set<AgentController> agents = new HashSet<AgentController>();

		int agentCount = configuration.getInt("pubsub.tera.agent.count");
		String prefix = configuration
				.getString("pubsub.tera.agent.name.prefix");
		for (int id = 0; id < agentCount; id++) {
			Object[] args = { new TeraAgentArguments( //
					configuration.subset("pubsub.tera")) };
			agents.add(container.createNewAgent(prefix + id, TeraAgent.class
					.getCanonicalName(), args));
		}

		System.out.println("Launching agents...");
		for (AgentController ac : agents) {
			ac.start();
		}
	}
}
