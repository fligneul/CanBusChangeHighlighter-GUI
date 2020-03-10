package com.florian_ligneul.canbus.change_highlighter.service.config;

import io.reactivex.observers.TestObserver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConfigServiceTest {

    ConfigService configService;

    @Before
    public void setUp() {
        configService = new ConfigService();
    }

    @Test
    public void dataFormatterTest() {
        TestObserver<EMessageDataFormat> dataFormatTestSubscriber = new TestObserver<>();
        configService.getDataFormatter().subscribe(dataFormatTestSubscriber);

        dataFormatTestSubscriber.assertNoErrors();
        dataFormatTestSubscriber.assertNotComplete();
        dataFormatTestSubscriber.assertNoValues();

        configService.setDataFormatter(EMessageDataFormat.DECIMAL);
        dataFormatTestSubscriber.assertValueCount(1);
        dataFormatTestSubscriber.assertValueAt(0, EMessageDataFormat.DECIMAL);

        configService.setDataFormatter(EMessageDataFormat.OCTAL);
        dataFormatTestSubscriber.assertValueCount(2);
        dataFormatTestSubscriber.assertValueAt(1, EMessageDataFormat.OCTAL);
    }

    @Test
    public void defaultFormatterTest() {
        Assert.assertEquals(EMessageDataFormat.HEXADECIMAL, configService.getDefaultFormatter());
    }
}