package com.florian_ligneul.canbus.model.message;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.IntStream;

/**
 * Represent a CAN bus message
 */
public class CanBusMessage {
    private static long CAN_EFF_FLAG = 0x80000000L;
    private static long CAN_RTR_FLAG = 0x40000000L;
    private static long CAN_ERR_FLAG = 0x20000000L;

    private static long CAN_SFF_MASK = 0x000007FFL;
    private static long CAN_EFF_MASK = 0x1FFFFFFFL;

    private static final int ID_BYTE_LENGTH = 4;
    private static final int DLC_BYTE_POSITION = 4;
    private static final int DATA_BYTE_OFFSET = 5;

    private final IntegerProperty id;
    private final BooleanProperty isExtendedFrameFormat;
    private final BooleanProperty isRemoteTransmissionRequest;
    private final BooleanProperty isErrorMessage;
    private final IntegerProperty payloadSize; // max 8 bytes
    private final ListProperty<CanBusMessageDatum> messageData;

    private CanBusMessage(int id, boolean isExtendedFrameFormat, boolean isRemoteTransmissionRequest, boolean isErrorMessage, int payloadSize, List<CanBusMessageDatum> data) {
        this.id = new SimpleIntegerProperty(id);
        this.isExtendedFrameFormat = new SimpleBooleanProperty(isExtendedFrameFormat);
        this.isRemoteTransmissionRequest = new SimpleBooleanProperty(isRemoteTransmissionRequest);
        this.isErrorMessage = new SimpleBooleanProperty(isErrorMessage);
        this.payloadSize = new SimpleIntegerProperty(payloadSize);
        this.messageData = new SimpleListProperty<>(FXCollections.observableList(data));
    }

    /**
     * Convert a raw CAN bus message into Java object
     *
     * @param rawMessage CAN bus message byte array
     * @return a Java CAN bus message
     */
    public static CanBusMessage parseCanBusMessage(byte[] rawMessage) {
        if (rawMessage.length < 6) { // 4 bytes for id + 1 byte DLC + 1 byte '\n'
            throw new IllegalArgumentException("Wrong CAN Bus message size");
        }

        int rawId = ByteBuffer.wrap(rawMessage, 0, ID_BYTE_LENGTH).order(ByteOrder.LITTLE_ENDIAN).getInt();

        boolean isExtendedFrameFormat = (rawId & CAN_EFF_FLAG) > 0;
        boolean isRemoteTransmissionRequest = (rawId & CAN_RTR_FLAG) > 0;
        boolean isErrorMessage = (rawId & CAN_ERR_FLAG) > 0;

        int id = (int) (rawId & (isExtendedFrameFormat ? CAN_EFF_MASK : CAN_SFF_MASK));

        int dlc = rawMessage[DLC_BYTE_POSITION];
        List<CanBusMessageDatum> data = new ArrayList<>();

        IntStream.range(0, dlc)
                .map(idx -> rawMessage[idx + DATA_BYTE_OFFSET] & 0xFF)
                .mapToObj(CanBusMessageDatum::new)
                .forEach(data::add);

        return new CanBusMessage(id, isExtendedFrameFormat, isRemoteTransmissionRequest, isErrorMessage, dlc, data);
    }

    /**
     * Compute delta with the previously received {@link CanBusMessage}.
     * All changed data are flagged with the hasChanged property.
     *
     * @param oldMessage the previously received {@link CanBusMessage}
     */
    public void computeDelta(CanBusMessage oldMessage) {
        for (int i = 0; i < getPayloadSize(); i++) {
            if (messageData.get(i).getDatum() != oldMessage.getMessageDatum(i).getDatum()) {
                messageData.set(i, new CanBusMessageDatum(messageData.get(i).getDatum(), true));
            }
        }
    }

    /**
     * @return the CAN bus message ID (29 or 11 bits)
     */
    public int getId() {
        return id.get();
    }

    /**
     * @return the CAN bus message ID read only property
     */
    public ReadOnlyIntegerProperty idProperty() {
        return id;
    }

    /**
     * @return true if the CAN bus message is in extended frame format
     */
    public boolean isExtendedFrameFormat() {
        return isExtendedFrameFormat.get();
    }

    /**
     * @return the CAN bus message EFF read only property
     */
    public ReadOnlyBooleanProperty isExtendedFrameFormatProperty() {
        return isExtendedFrameFormat;
    }

    /**
     * @return true if the CAN bus message is a remote transmission request
     */
    public boolean isRemoteTransmissionRequest() {
        return isRemoteTransmissionRequest.get();
    }

    /**
     * @return the CAN bus message RTR read only property
     */
    public ReadOnlyBooleanProperty isRemoteTransmissionRequestProperty() {
        return isRemoteTransmissionRequest;
    }

    /**
     * @return true if the CAN bus message is an error message
     */
    public boolean isErrorMessage() {
        return isErrorMessage.get();
    }

    /**
     * @return the CAN bus message ERR read only property
     */
    public ReadOnlyBooleanProperty isErrorMessageProperty() {
        return isErrorMessage;
    }

    /**
     * @return the payload size of the CAN bus message
     */
    public int getPayloadSize() {
        return payloadSize.get();
    }

    /**
     * @return the CAN bus message data list
     */
    public ReadOnlyListProperty<CanBusMessageDatum> getMessageData() {
        return messageData;
    }

    /**
     * Return a specific message datum
     *
     * @param n number of the data byte
     * @return the datum byte or null if the byte is not present
     */
    public CanBusMessageDatum getMessageDatum(int n) {
        return n < messageData.size() ? messageData.get(n) : null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CanBusMessage.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("isExtendedFrameFormat=" + isExtendedFrameFormat())
                .add("isRemoteTransmissionRequest=" + isRemoteTransmissionRequest())
                .add("isErrorMessage=" + isErrorMessage())
                .add("payloadSize=" + getPayloadSize())
                .add("messageData=" + getMessageData())
                .toString();
    }
}
