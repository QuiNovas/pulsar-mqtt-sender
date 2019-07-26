package com.echostreams.pulsar.mqtt.sender.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TopicUtilsTest {

    @Test
    public void testIsOnlyTopicName() {
        assertNotNull("test-topic");
        assertFalse(TopicUtils.isOnlyTopicName("test-topic"));
        assertTrue(TopicUtils.isOnlyTopicName("persistent://my-tenant/my-namespace/test-topic"));
    }
}
