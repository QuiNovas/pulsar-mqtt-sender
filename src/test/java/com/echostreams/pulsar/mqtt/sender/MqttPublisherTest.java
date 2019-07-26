package com.echostreams.pulsar.mqtt.sender;

import org.junit.Test;

import java.net.ConnectException;

public class MqttPublisherTest {

    @Test(expected = ConnectException.class)
    public void testConsumeAndPublish() {
        MqttPublisher mqttPublisher = new MqttPublisher("test-topic");
        mqttPublisher.publishToMqttBroker("Hello message!".getBytes());
    }
}
