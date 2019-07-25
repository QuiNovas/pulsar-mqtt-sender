package com.echostreams.pulsar.mqtt.sender.utils;

import com.echostreams.pulsar.mqtt.sender.config.PulsarMqttConstants;

public class TopicUtils {

    private TopicUtils() {
    }

    /**
     * Return true if only topic name without persistent:// or non-persistent://
     *
     * @param topicName
     * @return
     */
    public static boolean isOnlyTopicName(String topicName) {
        return topicName.contains(PulsarMqttConstants.PERSISTENT_NAME + PulsarMqttConstants.DOUBLE_FORWARD_SLASH)
                || topicName.contains(PulsarMqttConstants.NON_PERSISTENT_NAME + PulsarMqttConstants.DOUBLE_FORWARD_SLASH);
    }

}
