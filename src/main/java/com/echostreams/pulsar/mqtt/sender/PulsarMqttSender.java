package com.echostreams.pulsar.mqtt.sender;

import com.echostreams.pulsar.mqtt.sender.config.PulsarMqttConstants;
import com.echostreams.pulsar.mqtt.sender.service.PulsarMqttService;
import com.echostreams.pulsar.mqtt.sender.service.impl.PulsarMqttServiceImpl;
import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class PulsarMqttSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(PulsarMqttSender.class);

    private PulsarMqttService pulsarMqttService;

    public PulsarMqttSender() {
    }

    public static void main(String args[]) throws IOException {

        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(r);
        LOGGER.info(" Please enter topic names to process[, separated in case more than 1] OR filepath containing topics");
        String topicsOrFilepath = br.readLine();

        if (StringUtils.isEmpty(topicsOrFilepath)) {
            LOGGER.warn(" You must enter exactly 1 input as topic name or .txt file containing topics");
            return;
        }

        new PulsarMqttSender().parseTopicsOrFile(topicsOrFilepath);

        // close the connection resources
        MqttBrokerConnect.closeMqttConnection();
        PulsarBrokerConnect.closePulsarConnection();

    }

    private void parseTopicsOrFile(String topicsOrFilepath) {
        pulsarMqttService = new PulsarMqttServiceImpl();

        if (StringUtils.isEmpty(PulsarMqttConstants.PULSAR_SERVICE_URL)
                || StringUtils.isEmpty(PulsarMqttConstants.MQTT_BROKER_URL)) {
            LOGGER.error("pulsar/mqtt broker url can not be null, check your config file!");
            return;
        }

        if (topicsOrFilepath.contains(PulsarMqttConstants.FILE_EXTENSION)) {
            // Read file if valid and get each line topic to consume from pulsar and send to mqtt
            pulsarMqttService.parseFile(new File(topicsOrFilepath));

        } else if (topicsOrFilepath.split(PulsarMqttConstants.COMMA_SEPARATOR).length > 1) {
            // Iterate over array to get each topic  to consume from pulsar and send to mqtt
            String topicNameArray[] = topicsOrFilepath.split(PulsarMqttConstants.COMMA_SEPARATOR);
            for (String topicName : topicNameArray) {
                pulsarMqttService.processTopic(topicName);
            }
        } else {
            // Only one topic to consume from pulsar and send to mqtt
            pulsarMqttService.processTopic(topicsOrFilepath);
        }
    }
}
