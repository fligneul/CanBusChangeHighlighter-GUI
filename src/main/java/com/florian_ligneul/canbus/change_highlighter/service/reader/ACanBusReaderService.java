package com.florian_ligneul.canbus.change_highlighter.service.reader;

import com.florian_ligneul.canbus.change_highlighter.view.model.CanBusConnectionModel;
import com.florian_ligneul.canbus.model.message.CanBusMessage;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

/**
 * Abstract implementation of {@link ICanBusReaderService}.
 */
public abstract class ACanBusReaderService implements ICanBusReaderService {
    protected CanBusConnectionModel canBusConnectionModel;

    protected ObservableList<CanBusMessage> canBusMessageList = FXCollections.observableArrayList();
    protected ObservableList<String> comPortList = FXCollections.observableArrayList();

    @Inject
    public ACanBusReaderService(CanBusConnectionModel canBusConnectionModel) {
        this.canBusConnectionModel = canBusConnectionModel;
    }

    @Override
    public ObservableList<CanBusMessage> getCanBusMessageList() {
        return canBusMessageList;
    }

    @Override
    public ObservableList<String> getComPortList() {
        return comPortList;
    }

    /**
     * Parse a raw message and publish it
     *
     * @param rawMessage a raw CAN bus message
     */
    protected void handleNewMessage(byte[] rawMessage) {
        CanBusMessage receivedMessage = CanBusMessage.parseCanBusMessage(rawMessage);
        Optional<CanBusMessage> existingMessage = canBusMessageList.stream()
                .filter(c -> c.getId() == receivedMessage.getId())
                .findFirst();
        if (existingMessage.isPresent()) {
            receivedMessage.computeDelta(existingMessage.get());
            canBusMessageList.set(canBusMessageList.indexOf(existingMessage.get()), receivedMessage);
        } else {
            canBusMessageList.add(receivedMessage);
        }
    }
}
