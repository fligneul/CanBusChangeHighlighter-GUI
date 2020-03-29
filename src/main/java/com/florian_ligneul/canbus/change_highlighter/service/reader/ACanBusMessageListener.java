package com.florian_ligneul.canbus.change_highlighter.service.reader;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;

/**
 * Event based message reception implementation of the {@link SerialPortDataListener}
 * Callback will be triggered whenever some amount of data has actually been read from the serial port.
 */
public abstract class ACanBusMessageListener implements SerialPortDataListener {

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }
}

