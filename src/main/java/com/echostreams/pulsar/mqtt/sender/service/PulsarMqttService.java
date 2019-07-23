package com.echostreams.pulsar.mqtt.sender.service;

import java.io.File;

public interface PulsarMqttService {

    void parseFile(File file);

    void processTopic(String topicName);
}
