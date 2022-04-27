package org.rogo.spinel.interfaces;

/**
 * IStream template stream
 */
public abstract class IStream {

    private boolean isDataAvailable;

    public abstract void open();

    public abstract void write(byte[] buffer);

    public abstract byte[] read();

    public abstract byte readByte();

    public abstract void close();

    public boolean getIsDataAvailable() {
        return isDataAvailable;
    }

    public void setIsDataAvailable(boolean dataAvailable) {
        isDataAvailable = dataAvailable;
    }
}
