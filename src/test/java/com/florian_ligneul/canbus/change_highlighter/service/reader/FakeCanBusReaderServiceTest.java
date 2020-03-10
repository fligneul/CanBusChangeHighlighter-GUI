package com.florian_ligneul.canbus.change_highlighter.service.reader;

import com.florian_ligneul.canbus.change_highlighter.view.model.CanBusConnectionModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class FakeCanBusReaderServiceTest {

    FakeCanBusReaderService canBusReaderService;
    CanBusConnectionModel canBusConnectionModel;

    @Before
    public void setUp() {
        canBusConnectionModel = new CanBusConnectionModel();
        canBusReaderService = new FakeCanBusReaderService(canBusConnectionModel);
    }

    @Test
    public void comPortTest() {
        Assert.assertEquals(1, canBusReaderService.getComPortList().size());
        Assert.assertEquals("MOCK", canBusReaderService.getComPortList().get(0));
    }

    @Test
    public void connectionTest() {
        // Initial state
        Assert.assertFalse(canBusConnectionModel.isConnected());

        // Test connection & values
        canBusReaderService.connect();
        Assert.assertTrue(canBusReaderService.getCanBusMessageList().isEmpty());
        Assert.assertTrue(canBusConnectionModel.isConnected());
        await().atMost(2, TimeUnit.SECONDS).until(() -> !canBusReaderService.getCanBusMessageList().isEmpty());

        // Test disconnection
        canBusReaderService.disconnect();
        Assert.assertFalse(canBusConnectionModel.isConnected());
    }
}