package com.echostreams.pulsar.mqtt.sender.config;

public class ConfigProperties {

    //PULSAR
    public static final String PULSAR_SERVICE_URL = "pulsar.service.url";
    public static final String PULSAR_SUBSCRIPTION_TYPE = "pulsar.subscription.type";
    public static final String PULSAR_READ_MESSAGE_TIMEOUT = "pulsar.read.message.timeout";
    public static final String PULSAR_READ_MESSAGE_TIME_UNIT = "pulsar.read.message.time.unit";

    //MQTT
    public static final String MQTT_BROKER_URL = "mqtt.broker.url";
    public static final String MQTT_QOS = "mqtt.qos";

}
