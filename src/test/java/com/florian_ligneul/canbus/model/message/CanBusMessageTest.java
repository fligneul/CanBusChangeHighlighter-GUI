package com.florian_ligneul.canbus.model.message;

import org.junit.Assert;
import org.junit.Test;

public class CanBusMessageTest {

    @Test
    public void parseBaseCanBusMessageTest() {
        byte[] rawCanBusMessage = new byte[]{(byte) 0xC8,
                (byte) 0x04,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x02,
                (byte) 0x42,
                (byte) 0xFB};
        CanBusMessage canBusMessage = CanBusMessage.parseCanBusMessage(rawCanBusMessage);
        Assert.assertEquals(0x4C8, canBusMessage.getId());
        Assert.assertFalse(canBusMessage.isErrorMessage());
        Assert.assertFalse(canBusMessage.isExtendedFrameFormat());
        Assert.assertFalse(canBusMessage.isRemoteTransmissionRequest());
        Assert.assertEquals(2, canBusMessage.getPayloadSize());
        Assert.assertEquals(0x42, canBusMessage.getMessageDatum(0).getDatum());
        Assert.assertEquals(0xFB, canBusMessage.getMessageDatum(1).getDatum());
    }

    @Test
    public void parseExtendedCanBusMessageTest() {
        byte[] rawCanBusMessage = new byte[]{(byte) 0x00,
                (byte) 0x40,
                (byte) 0x35,
                (byte) 0x86,
                (byte) 0x02,
                (byte) 0x42,
                (byte) 0xFB};
        CanBusMessage canBusMessage = CanBusMessage.parseCanBusMessage(rawCanBusMessage);
        Assert.assertEquals(0x06354000, canBusMessage.getId());
        Assert.assertFalse(canBusMessage.isErrorMessage());
        Assert.assertTrue(canBusMessage.isExtendedFrameFormat());
        Assert.assertFalse(canBusMessage.isRemoteTransmissionRequest());
        Assert.assertEquals(2, canBusMessage.getPayloadSize());
        Assert.assertEquals(0x42, canBusMessage.getMessageDatum(0).getDatum());
        Assert.assertEquals(0xFB, canBusMessage.getMessageDatum(1).getDatum());
    }

    @Test
    public void parseRemoteTransmissionCanBusMessageTest() {
        byte[] rawCanBusMessage = new byte[]{(byte) 0xC8,
                (byte) 0x04,
                (byte) 0x00,
                (byte) 0x40,
                (byte) 0x00};
        CanBusMessage canBusMessage = CanBusMessage.parseCanBusMessage(rawCanBusMessage);
        Assert.assertEquals(0x4C8, canBusMessage.getId());
        Assert.assertFalse(canBusMessage.isErrorMessage());
        Assert.assertFalse(canBusMessage.isExtendedFrameFormat());
        Assert.assertTrue(canBusMessage.isRemoteTransmissionRequest());
        Assert.assertEquals(0, canBusMessage.getPayloadSize());
    }

    @Test
    public void parseErrorCanBusMessageTest() {
        byte[] rawCanBusMessage = new byte[]{(byte) 0xC8,
                (byte) 0x04,
                (byte) 0x00,
                (byte) 0x20,
                (byte) 0x00};
        CanBusMessage canBusMessage = CanBusMessage.parseCanBusMessage(rawCanBusMessage);
        Assert.assertEquals(0x4C8, canBusMessage.getId());
        Assert.assertTrue(canBusMessage.isErrorMessage());
        Assert.assertFalse(canBusMessage.isExtendedFrameFormat());
        Assert.assertFalse(canBusMessage.isRemoteTransmissionRequest());
        Assert.assertEquals(0, canBusMessage.getPayloadSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseWrongCanBusMessageTest() {
        byte[] rawCanBusMessage = new byte[]{(byte) 0xC8,
                (byte) 0x04,
                (byte) 0x00,
                (byte) 0x20};
        CanBusMessage.parseCanBusMessage(rawCanBusMessage);
    }

    @Test
    public void computeDeltaTest() {
        // Original message
        byte[] rawCanBusMessage = new byte[]{(byte) 0x00,
                (byte) 0x40,
                (byte) 0x35,
                (byte) 0x86,
                (byte) 0x02,
                (byte) 0x42,
                (byte) 0xFB};
        CanBusMessage canBusMessage = CanBusMessage.parseCanBusMessage(rawCanBusMessage);

        // New message
        byte[] rawCanBusNewMessage = new byte[]{(byte) 0x00,
                (byte) 0x40,
                (byte) 0x35,
                (byte) 0x86,
                (byte) 0x02,
                (byte) 0xA7,
                (byte) 0xFB};
        CanBusMessage canBusNewMessage = CanBusMessage.parseCanBusMessage(rawCanBusNewMessage);
        Assert.assertFalse(canBusNewMessage.getMessageDatum(0).hasChanged());
        Assert.assertFalse(canBusNewMessage.getMessageDatum(1).hasChanged());

        // Compute delta
        canBusNewMessage.computeDelta(canBusMessage);
        Assert.assertTrue(canBusNewMessage.getMessageDatum(0).hasChanged());
        Assert.assertFalse(canBusNewMessage.getMessageDatum(1).hasChanged());
    }

    @Test
    public void toStringTest() {
        byte[] rawCanBusMessage = new byte[]{(byte) 0x00,
                (byte) 0x40,
                (byte) 0x35,
                (byte) 0x86,
                (byte) 0x02,
                (byte) 0x42,
                (byte) 0xFB};
        Assert.assertNotNull(CanBusMessage.parseCanBusMessage(rawCanBusMessage).toString());
    }
}
