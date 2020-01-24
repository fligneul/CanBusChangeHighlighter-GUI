package com.florian_ligneul.canbus.model.message;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.StringJoiner;

/**
 * CAN bus message datum
 * Datum is enhanced with the changed status used for highlighting
 */
public class CanBusMessageDatum {

    private final IntegerProperty datum;
    private final BooleanProperty hasChanged;

    /**
     * Constructor
     *
     * @param datum      a datum of the message
     * @param hasChanged true if the data has changed during the last cycle
     */
    public CanBusMessageDatum(int datum, boolean hasChanged) {
        this.datum = new SimpleIntegerProperty(datum);
        this.hasChanged = new SimpleBooleanProperty(hasChanged);
    }

    /**
     * Constructor
     *
     * @param datum the message datum
     */
    public CanBusMessageDatum(int datum) {
        this(datum, false);
    }

    /**
     * @return datum
     */
    public int getDatum() {
        return datum.get();
    }

    /**
     * @return true if the data has changed during last cycle
     */
    public boolean hasChanged() {
        return hasChanged.get();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CanBusMessageDatum.class.getSimpleName() + "[", "]")
                .add("datum=" + getDatum())
                .add("hasChanged=" + hasChanged())
                .toString();
    }
}
