package com.echostreams.pulsar.mqtt.sender;

import com.echostreams.pulsar.mqtt.sender.config.PulsarMqttConstants;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PulsarBrokerConnect {
    private static final Logger LOGGER = LoggerFactory.getLogger(PulsarBrokerConnect.class);

    /**
     * volatile to prevent cache incoherence issues. In fact, the Java memory model allows
     * the publication of partially initialized objects and this may lead in turn to subtle bugs.
     */

    private static volatile PulsarBrokerConnect pulsarBrokerConnect;
    public static PulsarClient client;

    public static synchronized PulsarBrokerConnect getPulsarCon() {
        if (pulsarBrokerConnect == null) {
            //synchronized block to remove overhead
            synchronized (PulsarBrokerConnect.class) {
                if (pulsarBrokerConnect == null) {
                    // if instance is null, initialize
                    pulsarBrokerConnect = new PulsarBrokerConnect();
                }
            }
        }
        return pulsarBrokerConnect;
    }

    public static void closePulsarConnection() {
        if (client != null) {
            try {
                client.close();
            } catch (PulsarClientException e) {
                // ignore
            }
        }
    }

    private PulsarBrokerConnect() {
        try {
            LOGGER.info("Connecting to pulsar broker: " + PulsarMqttConstants.PULSAR_SERVICE_URL);
            client = PulsarClient.builder()
                    .serviceUrl(PulsarMqttConstants.PULSAR_SERVICE_URL)
                    .build();
            LOGGER.info("Build client for pulsar broker");
        } catch (PulsarClientException e) {
            LOGGER.error("Error occurred during pulsar client build: {}", e);
        }
    }

}
