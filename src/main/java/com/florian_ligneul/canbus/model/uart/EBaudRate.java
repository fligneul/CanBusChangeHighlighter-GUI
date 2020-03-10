package com.florian_ligneul.canbus.model.uart;

import javafx.util.StringConverter;

import java.util.stream.Stream;

/**
 * UART standard baud rates
 */
public enum EBaudRate {
    B4800(4800),
    B9600(9600),
    B14400(14400),
    B19200(19200),
    B38400(38400),
    B57600(57600),
    B115200(115200),
    B128000(128000),
    B256000(256000);

    /**
     * Available UART baud rate
     *
     * @param baudRate the baud rate value
     */
    EBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    private int baudRate;

    /**
     * @return the baud rate value (in bps)
     */
    public int getBaudRate() {
        return this.baudRate;
    }

    /**
     * @return the {@link StringConverter} for displaying BaudRate
     */
    public static StringConverter<EBaudRate> getConverter() {
        return new StringConverter<EBaudRate>() {
            @Override
            public String toString(EBaudRate baudRate) {
                return String.valueOf(baudRate.getBaudRate());
            }

            @Override
            public EBaudRate fromString(String baudRate) {
                return Stream.of(EBaudRate.values())
                        .filter(bd -> bd.getBaudRate() == Integer.parseInt(baudRate))
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new);
            }
        };
    }

}
