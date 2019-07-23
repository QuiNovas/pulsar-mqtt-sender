package com.echostreams.pulsar.mqtt.sender.utils;

import com.echostreams.pulsar.mqtt.sender.config.PulsarMqttConstants;
import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;

public class TopicUtils {

    private TopicUtils() {
    }

    /**
     * Create topic prefix like this persistent://public/default/my-topic
     *
     * @param topicPrefix
     * @param topicName
     * @return
     */
    public static String createTopicWithPrefix(String topicPrefix,
                                               String topicName) {

        if (StringUtils.isNotEmpty(topicName))
            return topicPrefix;
        else if ("/".contains(topicName))
            return topicPrefix + topicName;
        else
            return topicPrefix + "/" + topicName;
    }

    /**
     * Create a new topic prefix for the given persistentName, tenantName, namespaceName
     *
     * @param persistentName
     * @param tenantName
     * @param namespaceName
     * @return
     */
    public static String createOnlyPrefixWithoutTopic(String persistentName, String tenantName, String namespaceName) {
        return persistentName + PulsarMqttConstants.DOUBLE_FORWARD_SLASH + tenantName + PulsarMqttConstants.SINGLE_FORWARD_SLASH + namespaceName;

    }

    // Create topic prefix with value given in config file
    public static String getDefaultPrefixWithoutTopic() {
        return PulsarMqttConstants.PERSISTENT_NAME + PulsarMqttConstants.DOUBLE_FORWARD_SLASH + PulsarMqttConstants.TENANT_NAME +
                PulsarMqttConstants.SINGLE_FORWARD_SLASH + PulsarMqttConstants.NAMESPACE_NAME;

    }

    /**
     * Return true if message storage type  is persistent or non-persistent in config file,
     * else returns false
     *
     * @param messageStorageType
     * @return
     */
    public static boolean isValidMessageStorageTypeName(String messageStorageType) {
        return PulsarMqttConstants.PERSISTENT_NAME.equals(messageStorageType)
                || PulsarMqttConstants.NON_PERSISTENT_NAME.equals(messageStorageType);
    }

    /**
     * Return true if only topic name without persistent:// or non-persistent://
     * @param topicName
     * @return
     */
    public static boolean isOnlyTopicName(String topicName) {
        return topicName.contains(PulsarMqttConstants.PERSISTENT_NAME+PulsarMqttConstants.DOUBLE_FORWARD_SLASH)
                || topicName.contains(PulsarMqttConstants.NON_PERSISTENT_NAME+PulsarMqttConstants.DOUBLE_FORWARD_SLASH);
    }

}
