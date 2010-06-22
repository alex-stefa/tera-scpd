package ro.cs.pub.pubsub;

import ro.cs.pub.pubsub.overlay.OverlayId;

public interface Names {
	public static final String PROTOCOL_INITIATION = "initiation";
	public static final String PROTOCOL_LOGGING = "logging";
	public static final String PROTOCOL_ACCESS_POINT_LOOKUP = "access-point-lookup";
	public static final String PROTOCOL_OVERLAY_MANAGEMENT = "overlay-management";
	public static final String PROTOCOL_TOPIC_ADVERTISEMENT = "topic-advertisement";
	public static final String PROTOCOL_EVENT_PROPAGATION = "event-propagation";
	
	public static final String SIMULATION_CYCLON_RESILIANCE = "cyclon-resiliance";
	public static final String SIMULATION_MESSAGE_COUNTER = "message-counter";
	
	public static final String SERVICE_TERA = "tera-agent";
	public static final String SERVICE_LOGGING = "logging-agent";
	public static final String SERVICE_INITIATION = "initiator";
	public static final String SERVICE_SIMULATION = "simulation";

	public static final OverlayId OVERLAY_BASE = new OverlayId("base");
}
