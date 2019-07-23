package com.echostreams.pulsar.mqtt.sender.service.impl;

import com.echostreams.pulsar.mqtt.sender.PulsarConsumer;
import com.echostreams.pulsar.mqtt.sender.service.PulsarMqttService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PulsarMqttServiceImpl implements PulsarMqttService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PulsarMqttServiceImpl.class);

    @Override
    public void parseFile(File file) {
        LOGGER.info("Reading file. Path = {}.", file.getAbsolutePath());
        if (file.isDirectory()) {
            LOGGER.error("The given file is a directory. Path = {}.", file.getAbsolutePath());
            throw new RuntimeException("File \"" + file + "\" is a directory!");
        } else if (!file.exists()) {
            LOGGER.error("The file does not exist. Path = {}.", file.getAbsolutePath());
            return;
        }
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(file.toPath(), UTF_8);
            BufferedReader br = new BufferedReader(reader);
            String line;

            while ((line = br.readLine()) != null) {
                // consume from pulsar and publish to mqtt
                processTopic(line);
            }
        } catch (IOException e) {
            LOGGER.error("The file does not exist. Path = {}.", file.getAbsolutePath());
            return;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    @Override
    public void processTopic(String topicName) {
        PulsarConsumer pulsarConsumer = new PulsarConsumer(topicName);
        pulsarConsumer.consumeAndPublish();
    }


}
