package com.florian_ligneul.canbus.change_highlighter.service.reader;

import com.florian_ligneul.canbus.model.message.CanBusMessage;
import javafx.collections.ObservableList;

/**
 * CAN bus service interface.
 */
public interface ICanBusReaderService {

    /**
     * @return {@link ObservableList} of the received {@link CanBusMessage}
     */
    ObservableList<CanBusMessage> getCanBusMessageList();

    /**
     * @return {@link ObservableList} of the available com port
     */
    ObservableList<String> getComPortList();

    /**
     * Connect service to the message provider
     */
    void connect();

    /**
     * Disconnect service from the message provider
     */
    void disconnect();

}
