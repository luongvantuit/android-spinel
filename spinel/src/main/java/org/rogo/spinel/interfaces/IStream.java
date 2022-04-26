package org.rogo.spinel.interfaces;

/**
 * IStream template stream
 */
public interface IStream {

    void open();

    void write(byte[] bytes);

    void close();
}
