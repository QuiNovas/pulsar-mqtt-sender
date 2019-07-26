package com.echostreams.pulsar.mqtt.sender.service;

import com.echostreams.pulsar.mqtt.sender.service.impl.PulsarMqttServiceImpl;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class PulsarMqttServiceImplTest {

    @Test(expected = FileNotFoundException.class)
    public void testParseFileNotFoundException() throws FileNotFoundException {
        File file = new File("G:\\test");
        PulsarMqttService pulsarMqttService = new PulsarMqttServiceImpl();
        pulsarMqttService.parseFile(file);
    }
}
