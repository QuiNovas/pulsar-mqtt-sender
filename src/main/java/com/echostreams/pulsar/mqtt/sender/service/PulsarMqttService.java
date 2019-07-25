package com.echostreams.pulsar.mqtt.sender.service;

import java.io.File;
import java.io.FileNotFoundException;

public interface PulsarMqttService {

    void parseFile(File file) throws FileNotFoundException;

    void processTopic(String topicName);
}
