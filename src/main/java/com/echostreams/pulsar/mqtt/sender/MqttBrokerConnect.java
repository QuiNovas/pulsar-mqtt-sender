package com.echostreams.pulsar.mqtt.sender;

import com.echostreams.pulsar.mqtt.sender.config.PulsarMqttConstants;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttBrokerConnect {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttBrokerConnect.class);

    /**
     * volatile to prevent cache incoherence issues. In fact, the Java memory model allows
     * the publication of partially initialized objects and this may lead in turn to subtle bugs.
     */

    private static volatile MqttBrokerConnect mqttBrokerConnect;
    public static MqttClient client;

    public static synchronized MqttBrokerConnect getMqttBrokerConnection() {
        if (mqttBrokerConnect == null) {
            //synchronized block to remove overhead
            synchronized (PulsarBrokerConnect.class) {
                if (mqttBrokerConnect == null) {
                    // if instance is null, initialize
                    mqttBrokerConnect = new MqttBrokerConnect();
                }
            }
        }
        return mqttBrokerConnect;
    }

    private MqttBrokerConnect() {
        try {
            client = new MqttClient(PulsarMqttConstants.MQTT_BROKER_URL, MqttClient.generateClientId());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            LOGGER.info("Connecting to mqtt broker: " + PulsarMqttConstants.MQTT_BROKER_URL);
            client.connect(connOpts);
            LOGGER.info("Connected to mqtt broker");
        } catch (MqttException e) {
            LOGGER.info("reason ", e.getReasonCode());
            LOGGER.info("msg ", e.getMessage());
            LOGGER.info("loc ", e.getLocalizedMessage());
            LOGGER.info("cause ", e.getCause());
            LOGGER.error("Error occurred while creating connection to mqtt broker : {}", e);
        }
    }

    public static void closeMqttConnection() {
        if (client != null) {
            try {
                client.disconnect();
                client.close();
            } catch (MqttException e) {
                // ignore
            }
        }
    }
}
