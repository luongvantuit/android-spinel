package org.rogo.spinel.interfaces;

import android.content.Context;

/**
 * IStream template stream
 */
public interface IStream {

    void open(Context context);

    void write(byte[] buffer);

    public byte[] read();

    byte readByte();

    void close();

    boolean getIsDataAvailable();
}
