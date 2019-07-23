package com.echostreams.pulsar.mqtt.sender;

import com.echostreams.pulsar.mqtt.sender.config.PulsarMqttConstants;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttPublisher.class);

    private String topic;

    public MqttPublisher(String topic) {
        this.topic = topic;
    }

    public void publishToMqttBroker(byte[] msgFromPulsar) {
        LOGGER.info("Publishing message: " + new String(msgFromPulsar));
        MqttMessage message = new MqttMessage(msgFromPulsar);
        message.setQos(PulsarMqttConstants.MQTT_QOS);
        try {
            MqttBrokerConnect.getMqttBrokerConnection().client
                    .publish(topic, message);
            LOGGER.info("Message published!");
        } catch (MqttException e) {
            LOGGER.error("Failed while publishing message to mqtt broker ", e);
        }
    }
}
