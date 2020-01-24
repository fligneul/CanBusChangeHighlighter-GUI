package com.florian_ligneul.canbus.change_highlighter.service.config;

import java.util.function.Function;

/**
 * Enum of available message data formatter
 */
public enum EMessageDataFormat {
    HEXADECIMAL("Hexadecimal"),
    DECIMAL("Decimal"),
    BINARY("Binary"),
    OCTAL("Octal");

    private String name;

    EMessageDataFormat(String name) {
        this.name = name;
    }

    /**
     * @return formatter name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the {@link EMessageDataFormat} for the provided name
     *
     * @param name the formatter name
     * @return the {@link EMessageDataFormat}
     */
    public static EMessageDataFormat fromString(String name) {
        for (EMessageDataFormat b : EMessageDataFormat.values()) {
            if (b.name.equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Return the datum converter function
     *
     * @param messageDataFormat the {@link EMessageDataFormat}
     * @return the datum converter function
     */
    public static Function<Integer, String> getFormatter(EMessageDataFormat messageDataFormat) {
        switch (messageDataFormat) {
            case HEXADECIMAL:
                return i -> String.format("%02X", i);
            case DECIMAL:
                return String::valueOf;
            case BINARY:
                return i -> String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0');
            case OCTAL:
                return i -> String.format("%03o", i);
            default:
                throw new IllegalStateException("Unexpected value: " + messageDataFormat);
        }
    }
}
