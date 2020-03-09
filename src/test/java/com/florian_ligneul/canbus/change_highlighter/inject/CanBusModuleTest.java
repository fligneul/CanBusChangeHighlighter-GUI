package com.florian_ligneul.canbus.change_highlighter.inject;

import com.florian_ligneul.canbus.change_highlighter.service.reader.CanBusReaderService;
import com.florian_ligneul.canbus.change_highlighter.service.reader.FakeCanBusReaderService;
import com.florian_ligneul.canbus.change_highlighter.service.reader.ICanBusReaderService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CanBusModuleTest {

    @Inject
    Provider<ICanBusReaderService> canBusReaderServiceProvider;

    @Before
    public void setUp() {
        Guice.createInjector(new CanBusModule(), BoundFieldModule.of(this))
                .injectMembers(this);
    }

    @Test
    public void testCanBusReaderServiceInject() {
        Assert.assertNotNull(canBusReaderServiceProvider.get());
        Assert.assertTrue("Default injection should provide CanBusReaderService", canBusReaderServiceProvider.get() instanceof CanBusReaderService);
    }

    @Test
    public void testCanBusMockReaderServiceInject() {
        System.setProperty("mock", "true");
        Assert.assertNotNull(canBusReaderServiceProvider.get());
        Assert.assertTrue("Injection with mock system property should provide FakeCanBusReaderService", canBusReaderServiceProvider.get() instanceof FakeCanBusReaderService);
    }
}