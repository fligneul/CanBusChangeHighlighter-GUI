package com.florian_ligneul.canbus.change_highlighter.view.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * CAN bus connection model
 * It contains connection settings and state
 */
public class CanBusConnectionModel {
    private ObjectProperty<String> comPort = new SimpleObjectProperty<>();
    private ObjectProperty<Integer> baudRate = new SimpleObjectProperty<>();
    private ObjectProperty<Boolean> isConnected = new SimpleObjectProperty<>(false);

    /**
     * @return the UART connection com port
     */
    public String getComPort() {
        return comPort.get();
    }

    /**
     * Set the UART com port value
     *
     * @param comPort new com port value
     */
    public void setComPort(String comPort) {
        this.comPort.set(comPort);
    }

    /**
     * @return the UART connection baud rate
     */
    public Integer getBaudRate() {
        return baudRate.get();
    }

    /**
     * Set the UART baud rate value
     *
     * @param baudRate new baud rate value
     */
    public void setBaudRate(Integer baudRate) {
        this.baudRate.set(baudRate);
    }

    /**
     * @return the UART connection state
     */
    public Boolean isConnected() {
        return isConnected.get();
    }

    /**
     * @return the UART connection state property. This property is in ReadOnly state
     */
    public ReadOnlyObjectProperty<Boolean> isConnectedProperty() {
        return isConnected;
    }

    /**
     * Set the UART connection state value
     *
     * @param isConnected new connection state
     */
    public void setIsConnected(Boolean isConnected) {
        this.isConnected.set(isConnected);
    }
}
