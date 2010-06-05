package ro.cs.pub.pubsub.tera.agent;

import org.apache.commons.configuration.Configuration;

public class TeraAgentArguments {
	private final Configuration configuration;

	public TeraAgentArguments(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return configuration;
	}
}
