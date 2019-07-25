package com.echostreams.pulsar.mqtt.sender;

import com.echostreams.pulsar.mqtt.sender.config.PulsarMqttConstants;
import com.echostreams.pulsar.mqtt.sender.utils.TopicUtils;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.shade.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class PulsarConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PulsarConsumer.class);

    private String topic;

    public PulsarConsumer(String topic) {
        this.topic = topic;
    }

    public void consumeAndPublish() {

        ConsumerBuilder<byte[]> consumerBuilder = configureConsumer();
        Consumer<byte[]> consumer = null;

        try {
            consumer = consumerBuilder.subscribe();
        } catch (PulsarClientException e) {
            LOGGER.error("Failed during subscribing to consumer: ", e);
            return;
        }

        // Once the consumer is created, it can be used for the entire application lifecycle
        LOGGER.info("Created consumer for the topic {}", topic);

        do {

            // Wait until a message is available
            Message<byte[]> msg = null;
            try {
                msg = consumer.receive();
            } catch (PulsarClientException e) {
                LOGGER.error("Failed during consuming msg: ", e);
            }

            // Extract the message as a printable string and then log
            String content = new String(msg.getData());
            LOGGER.info("Received message '{}' from pulsar with ID {}", content, msg.getMessageId());

            //publish message to mqtt broker
            new MqttPublisher(topic).publishToMqttBroker(msg.getData());


            // Acknowledge the message so that it can be deleted by the message broker
            try {
                consumer.acknowledge(msg);
            } catch (PulsarClientException e) {
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg);
            }
        } while (true);

    }

    private ConsumerBuilder<byte[]> configureConsumer() {
        ConsumerBuilder<byte[]> consumer = PulsarBrokerConnect.getPulsarCon().client.newConsumer()
                .topic(topic);

        if (StringUtils.isNotEmpty(PulsarMqttConstants.PULSAR_SUBSCRIPTION_TYPE)) {
            consumer.subscriptionType(getSubscriptionType(PulsarMqttConstants.PULSAR_SUBSCRIPTION_TYPE));
        }

        if (StringUtils.isNotEmpty(PulsarMqttConstants.PULSAR_READ_MESSAGE_TIMEOUT)
                && StringUtils.isNotEmpty(PulsarMqttConstants.PULSAR_READ_MESSAGE_TIME_UNIT)) {
            consumer.ackTimeout(Long.parseLong(PulsarMqttConstants.PULSAR_READ_MESSAGE_TIMEOUT),
                    getTimeUnitType(PulsarMqttConstants.PULSAR_READ_MESSAGE_TIME_UNIT));
        }

        if (TopicUtils.isOnlyTopicName(topic)) {
            consumer.subscriptionName(PulsarMqttConstants.PULSAR_SUBSCRIPTION_NAME + topic);
        } else {
            // take only topic name from after last slash
            consumer.subscriptionName(PulsarMqttConstants.PULSAR_SUBSCRIPTION_NAME
                    + topic.substring(topic.lastIndexOf(PulsarMqttConstants.SINGLE_FORWARD_SLASH) + 1).trim());
        }

        return consumer;
    }

    private TimeUnit getTimeUnitType(String pulsarReadMessageTimeUnit) {

        switch (pulsarReadMessageTimeUnit) {
            case "NANOSECONDS":
                return TimeUnit.NANOSECONDS;
            case "MICROSECONDS":
                return TimeUnit.MICROSECONDS;
            case "MILLISECONDS":
                return TimeUnit.MILLISECONDS;
            case "SECONDS":
                return TimeUnit.SECONDS;
            case "MINUTES":
                return TimeUnit.MINUTES;
            case "HOURS":
                return TimeUnit.HOURS;
            default:
                return TimeUnit.DAYS;
        }
    }

    private SubscriptionType getSubscriptionType(String pulsarSubscriptionType) {

        switch (pulsarSubscriptionType) {
            case "Exclusive":
                return SubscriptionType.Exclusive;
            case "Failover":
                return SubscriptionType.Failover;
            case "Shared":
                return SubscriptionType.Shared;
            default:
                return SubscriptionType.Key_Shared;
        }
    }

}
