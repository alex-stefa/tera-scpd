package ro.cs.pub.pubsub.facilitator.agent;

import org.apache.commons.configuration.Configuration;

public class FacilitatorArguments {
	private final Configuration configuration;

	public FacilitatorArguments(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return configuration;
	}
}
