package ro.cs.pub.pubsub;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.tools.rma.rma;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.PropertiesConfiguration;

import ro.cs.pub.pubsub.agent.AgentArguments;
import ro.cs.pub.pubsub.agent.PubSubAgent;

public class App {
	private static final String AGENT_PREFIX = "a_";

	public static void main(String args[]) throws Exception {
		PropertiesConfiguration config = new PropertiesConfiguration(
				args[0]);
		App app = new App(config);
		app.start();
	}

	private PropertiesConfiguration configuration;

	public App(PropertiesConfiguration configuration)
			throws StaleProxyException {
		this.configuration = configuration;
	}

	public void start() throws StaleProxyException {
		// setup platform
		Runtime rt = Runtime.instance();
		rt.setCloseVM(true);
		Profile pMain = new ProfileImpl(null,
				configuration.getInt("jade.port"), null);

		// setup the main container
		AgentContainer container = rt.createMainContainer(pMain);
		if (configuration.getBoolean("jade.gui")) {
			AgentController rma = container.createNewAgent("rma", //
					rma.class.getCanonicalName(), new Object[0]);
			rma.start();
		}

		Set<AgentController> agents = new HashSet<AgentController>();
		
		int agentCount = configuration.getInt("pubsub.agent.count");
		for (int id = 0; id < agentCount; id++) {
			Object[] args = {new AgentArguments()};
			agents.add(container.createNewAgent(AGENT_PREFIX + id,
					PubSubAgent.class.getCanonicalName(), args));
		}
		
		System.out.println("Launching agents...");
		for (AgentController ac : agents) {
			ac.start();
		}
	}
}
