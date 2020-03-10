package com.florian_ligneul.canbus.model.uart;

import org.junit.Assert;
import org.junit.Test;

public class EBaudRateTest {

    @Test
    public void baudRateTest() {
        // Check baud rate list size
        Assert.assertEquals(9, EBaudRate.values().length);

        // Check each baud rate value
        Assert.assertEquals(4800, EBaudRate.B4800.getBaudRate());
        Assert.assertEquals(9600, EBaudRate.B9600.getBaudRate());
        Assert.assertEquals(14400, EBaudRate.B14400.getBaudRate());
        Assert.assertEquals(19200, EBaudRate.B19200.getBaudRate());
        Assert.assertEquals(38400, EBaudRate.B38400.getBaudRate());
        Assert.assertEquals(57600, EBaudRate.B57600.getBaudRate());
        Assert.assertEquals(115200, EBaudRate.B115200.getBaudRate());
        Assert.assertEquals(128000, EBaudRate.B128000.getBaudRate());
        Assert.assertEquals(256000, EBaudRate.B256000.getBaudRate());
    }

    @Test
    public void converterTest() {
        Assert.assertEquals(EBaudRate.B4800, EBaudRate.getConverter().fromString("4800"));
        Assert.assertEquals("4800", EBaudRate.getConverter().toString(EBaudRate.B4800));

        Assert.assertEquals(EBaudRate.B256000, EBaudRate.getConverter().fromString("256000"));
        Assert.assertEquals("256000", EBaudRate.getConverter().toString(EBaudRate.B256000));
    }

    @Test(expected = IllegalArgumentException.class)
    public void converterExceptionTest() {
        EBaudRate wrongBaudRate = EBaudRate.getConverter().fromString("a");
    }
}