package org.rogo.spinel.interfaces;

public interface ILogger {

    void warning(String TAG, String msg);

    void error(String TAG, String msg);

    void debug(String TAG, String msg);

    void info(String TAG, String msg);

}
