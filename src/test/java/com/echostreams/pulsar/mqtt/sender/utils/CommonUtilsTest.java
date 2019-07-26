package com.echostreams.pulsar.mqtt.sender.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class CommonUtilsTest {

    @Test
    public void testIsAnyObjectNull() {
        assertNull(null);
        assertFalse(CommonUtils.isAnyObjectNull("testObject"));
    }
}
