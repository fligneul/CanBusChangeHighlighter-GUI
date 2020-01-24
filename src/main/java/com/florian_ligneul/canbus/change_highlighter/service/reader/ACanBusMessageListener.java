package com.florian_ligneul.canbus.change_highlighter.service.reader;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortMessageListener;

/**
 * Event based message reception implementation of the {@link SerialPortMessageListener} with LF message delimiter.
 */
public abstract class ACanBusMessageListener implements SerialPortMessageListener {
    private static byte[] LF_DELIMITER = new byte[]{(byte) 0x0A};

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public byte[] getMessageDelimiter() {
        return LF_DELIMITER;
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return true;
    }
}

