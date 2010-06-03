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

import ro.cs.pub.pubsub.agent.logging.LoggingAgent;
import ro.cs.pub.pubsub.agent.tera.TeraAgent;
import ro.cs.pub.pubsub.agent.tera.TeraAgentArguments;

public class App {
	private static final String TERA_AGENT_PREFIX = "a_";
	private static final String LOGGING_AGENT = "logger";

	public static void main(String args[]) throws Exception {
		PropertiesConfiguration config = new PropertiesConfiguration(args[0]);
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

		// setup agents
		Set<AgentController> agents = new HashSet<AgentController>();

		// TERA agents
		int agentCount = configuration.getInt("pubsub.agent.count");
		for (int id = 0; id < agentCount; id++) {
			Object[] args = { new TeraAgentArguments() };
			agents.add(container.createNewAgent(TERA_AGENT_PREFIX + id,
					TeraAgent.class.getCanonicalName(), args));
		}

		// logging service
		Object[] args = {};
		agents.add(container.createNewAgent(LOGGING_AGENT, LoggingAgent.class
				.getCanonicalName(), args));

		System.out.println("Launching agents...");
		for (AgentController ac : agents) {
			ac.start();
		}
	}
}
