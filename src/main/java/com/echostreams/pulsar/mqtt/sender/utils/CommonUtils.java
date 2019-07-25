package com.echostreams.pulsar.mqtt.sender.utils;

public class CommonUtils {

    private CommonUtils() {
    }

    /**
     * It validates one or more objects for nullity
     *
     * @param objects
     * @return
     */
    public static boolean isAnyObjectNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }
}
