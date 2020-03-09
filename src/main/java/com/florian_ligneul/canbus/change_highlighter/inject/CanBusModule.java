package com.florian_ligneul.canbus.change_highlighter.inject;

import com.florian_ligneul.canbus.change_highlighter.service.config.ConfigService;
import com.florian_ligneul.canbus.change_highlighter.service.reader.CanBusReaderService;
import com.florian_ligneul.canbus.change_highlighter.service.reader.FakeCanBusReaderService;
import com.florian_ligneul.canbus.change_highlighter.service.reader.ICanBusReaderService;
import com.florian_ligneul.canbus.change_highlighter.view.model.CanBusConnectionModel;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * GUICE Injection module for CAN bus service
 */
public class CanBusModule extends AbstractModule {

    protected void configure() {
        bind(ConfigService.class).asEagerSingleton();
        bind(CanBusConnectionModel.class).asEagerSingleton();
    }

    /**
     * Provide a {@link ICanBusReaderService} implementation based on UART communications.
     * If application is started with -Dmock=true, {@link FakeCanBusReaderService} is used
     *
     * @param canBusConnectionModel {@link CanBusConnectionModel}
     * @return the {@link CanBusReaderService} for connecting to the Arduino based hardware
     */
    @Provides
    @Singleton
    public ICanBusReaderService provideCanBusReaderService(CanBusConnectionModel canBusConnectionModel) {
        if (Boolean.getBoolean("mock")) {
            return new FakeCanBusReaderService(canBusConnectionModel);
        }
        return new CanBusReaderService(canBusConnectionModel);
    }

}
