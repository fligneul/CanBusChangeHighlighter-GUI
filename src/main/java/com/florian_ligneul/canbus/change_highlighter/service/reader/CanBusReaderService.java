package com.florian_ligneul.canbus.change_highlighter.service.reader;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.florian_ligneul.canbus.change_highlighter.view.model.CanBusConnectionModel;
import com.google.inject.Inject;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * UART implementation of {@link ACanBusReaderService}.
 */
public class CanBusReaderService extends ACanBusReaderService {
    private SerialPort comPort;

    @Inject
    public CanBusReaderService(CanBusConnectionModel canBusConnectionModel) {
        super(canBusConnectionModel);
        refreshComPortAvailableList();
    }

    @Override
    public void connect() {
        canBusMessageList.clear();
        comPort = SerialPort.getCommPort(canBusConnectionModel.getComPort());
        comPort.setBaudRate(canBusConnectionModel.getBaudRate());
        if (comPort.openPort()) {
            canBusConnectionModel.setIsConnected(true);
            ACanBusMessageListener listener = new ACanBusMessageListener() {
                @Override
                public void serialEvent(SerialPortEvent event) {
                    handleNewMessage(event.getReceivedData());
                }
            };
            comPort.addDataListener(listener);
        }
    }

    @Override
    public void disconnect() {
        comPort.removeDataListener();
        comPort.closePort();
        canBusConnectionModel.setIsConnected(false);
    }

    private void refreshComPortAvailableList() {
        comPortList.setAll(
                Stream.of(SerialPort.getCommPorts())
                        .map(SerialPort::getSystemPortName)
                        .collect(Collectors.toList())
        );
    }
}
