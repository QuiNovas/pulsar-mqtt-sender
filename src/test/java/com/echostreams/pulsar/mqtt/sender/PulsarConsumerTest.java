package com.echostreams.pulsar.mqtt.sender;

import org.junit.Test;

import java.net.ConnectException;

public class PulsarConsumerTest {

    @Test(expected = ConnectException.class)
    public void testConsumeAndPublish() {
        PulsarConsumer pulsarConsumer = new PulsarConsumer("test-topic");
        pulsarConsumer.consumeAndPublish();
    }

}
