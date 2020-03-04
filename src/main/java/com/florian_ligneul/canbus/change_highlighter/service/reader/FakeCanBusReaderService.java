package com.florian_ligneul.canbus.change_highlighter.service.reader;

import com.florian_ligneul.canbus.change_highlighter.view.model.CanBusConnectionModel;
import com.google.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Test implementation of {@link ACanBusReaderService}.
 */
public class FakeCanBusReaderService extends ACanBusReaderService {
    private Disposable mockServiceSub;

    @Inject
    public FakeCanBusReaderService(CanBusConnectionModel canBusConnectionModel) {
        super(canBusConnectionModel);
        comPortList.add("MOCK");
    }

    @Override
    public void connect() {
        canBusMessageList.clear();
        canBusConnectionModel.setIsConnected(true);
        Random randomGen = new Random();

        mockServiceSub = Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext(i -> handleNewMessage(new byte[]{0x0, 0x40, 0x35, (byte) 0x86, 0x2, (byte) randomGen.nextInt(2), (byte) randomGen.nextInt(255)}))
                .subscribe();
    }

    @Override
    public void disconnect() {
        canBusConnectionModel.setIsConnected(false);
        mockServiceSub.dispose();
    }
}
