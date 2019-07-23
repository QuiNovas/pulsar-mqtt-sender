package com.echostreams.pulsar.mqtt.sender.config;

public final class PulsarMqttConstants {

    /**
     * Properties (fixed/not user configurable)
     */

    public static final String DEFAULT_CONFIG_FILEPATH = "application.properties";

    // PULSAR
    public static final String DOUBLE_FORWARD_SLASH = "://";
    public static final String SINGLE_FORWARD_SLASH = "/";
    public static final String PERSISTENT_NAME = "persistent";
    public static final String TENANT_NAME = "public";
    public static final String NAMESPACE_NAME = "default";
    public static final String NON_PERSISTENT_NAME = "non-persistent";
    //persistent://public/default/my-topic
    public static final String DEFAULT_PULSAR_TOPIC_NAME_PREFIX = PERSISTENT_NAME + DOUBLE_FORWARD_SLASH + TENANT_NAME + SINGLE_FORWARD_SLASH + NAMESPACE_NAME;
    public static final String TOPIC_PROCESSING_IDENTIFIER = null;
    public static final String PULSAR_SERVICE_URL1 = "pulsar://localhost:6650";
    public static final String FILE_EXTENSION = ".txt";
    public static final String COMMA_SEPARATOR = ",";
    public static final String PULSAR_SUBSCRIPTION_NAME = "subscription-";

    /**
     * Properties (user configurable)
     */

    // PULSAR
    public static final String PULSAR_SERVICE_URL = ConfigurationLoader.getConfig().getProperty(ConfigProperties.PULSAR_SERVICE_URL);
    public static final String PULSAR_SUBSCRIPTION_TYPE = ConfigurationLoader.getConfig().getProperty(ConfigProperties.PULSAR_SUBSCRIPTION_TYPE);
    public static final String PULSAR_READ_MESSAGE_TIMEOUT = ConfigurationLoader.getConfig().getProperty(ConfigProperties.PULSAR_READ_MESSAGE_TIMEOUT);
    public static final String PULSAR_READ_MESSAGE_TIME_UNIT = ConfigurationLoader.getConfig().getProperty(ConfigProperties.PULSAR_READ_MESSAGE_TIME_UNIT);

    //MQTT
    public static final String MQTT_BROKER_URL = ConfigurationLoader.getConfig().getProperty(ConfigProperties.MQTT_BROKER_URL);
    public static final Integer MQTT_QOS = Integer.parseInt(ConfigurationLoader.getConfig().getProperty(ConfigProperties.MQTT_QOS));
}

