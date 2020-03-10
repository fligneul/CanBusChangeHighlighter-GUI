package com.florian_ligneul.canbus.change_highlighter.service.config;

import org.junit.Assert;
import org.junit.Test;

public class EMessageDataFormatTest {

    @Test
    public void formatNameTest() {
        // Check baud rate list size
        Assert.assertEquals(4, EMessageDataFormat.values().length);

        // Check each baud rate value
        Assert.assertEquals("Binary", EMessageDataFormat.BINARY.getName());
        Assert.assertEquals("Octal", EMessageDataFormat.OCTAL.getName());
        Assert.assertEquals("Decimal", EMessageDataFormat.DECIMAL.getName());
        Assert.assertEquals("Hexadecimal", EMessageDataFormat.HEXADECIMAL.getName());
    }

    @Test
    public void converterTest() {
        Assert.assertEquals(EMessageDataFormat.BINARY, EMessageDataFormat.fromString("Binary"));
        Assert.assertEquals(EMessageDataFormat.OCTAL, EMessageDataFormat.fromString("Octal"));
        Assert.assertEquals(EMessageDataFormat.DECIMAL, EMessageDataFormat.fromString("Decimal"));
        Assert.assertEquals(EMessageDataFormat.HEXADECIMAL, EMessageDataFormat.fromString("Hexadecimal"));
    }

    @Test
    public void converterErroTest() {
        Assert.assertNull(EMessageDataFormat.fromString("a"));
    }

    @Test
    public void formatterTest() {
        Assert.assertEquals("00101010", EMessageDataFormat.getFormatter(EMessageDataFormat.BINARY).apply(42));
        Assert.assertEquals("052", EMessageDataFormat.getFormatter(EMessageDataFormat.OCTAL).apply(42));
        Assert.assertEquals("42", EMessageDataFormat.getFormatter(EMessageDataFormat.DECIMAL).apply(42));
        Assert.assertEquals("2A", EMessageDataFormat.getFormatter(EMessageDataFormat.HEXADECIMAL).apply(42));
    }

}