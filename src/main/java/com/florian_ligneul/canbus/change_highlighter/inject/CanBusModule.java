package com.florian_ligneul.canbus.change_highlighter.inject;

import com.florian_ligneul.canbus.change_highlighter.service.config.ConfigService;
import com.florian_ligneul.canbus.change_highlighter.service.reader.CanBusReaderService;
import com.florian_ligneul.canbus.change_highlighter.service.reader.FakeCanBusReaderService;
import com.florian_ligneul.canbus.change_highlighter.service.reader.ICanBusReaderService;
import com.florian_ligneul.canbus.change_highlighter.view.model.CanBusConnectionModel;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * GUICE Injection module for CAN bus service
 */
public class CanBusModule extends AbstractModule {
    public static final String NAMED_CAN_BUS_READER_SERVICE = "CanBusReaderService";
    public static final String NAMED_FAKE_CAN_BUS_READER_SERVICE = "FakeCanBusReaderService";

    protected void configure() {
        bind(ConfigService.class).asEagerSingleton();
        bind(CanBusConnectionModel.class).asEagerSingleton();
    }

    /**
     * Provide a {@link ICanBusReaderService} implementation based on UART communications.
     *
     * @param canBusConnectionModel {@link CanBusConnectionModel}
     * @return the {@link CanBusReaderService} for connecting to the Arduino based hardware
     */
    @Provides
    @Singleton
    @Named(NAMED_CAN_BUS_READER_SERVICE)
    public ICanBusReaderService provideCanBusReaderService(CanBusConnectionModel canBusConnectionModel) {
        return new CanBusReaderService(canBusConnectionModel);
    }

    /**
     * Provide a {@link ICanBusReaderService} testing implementation.
     *
     * @param canBusConnectionModel {@link CanBusConnectionModel}
     * @return a {@link FakeCanBusReaderService} that emit mocked value. Used for testing
     */
    @Provides
    @Singleton
    @Named(NAMED_FAKE_CAN_BUS_READER_SERVICE)
    public ICanBusReaderService provideFakeCanBusReaderService(CanBusConnectionModel canBusConnectionModel) {
        return new FakeCanBusReaderService(canBusConnectionModel);
    }

}
