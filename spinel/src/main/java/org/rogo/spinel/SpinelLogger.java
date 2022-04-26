package org.rogo.spinel;

import android.util.Log;

import org.rogo.spinel.interfaces.ILogger;

final public class SpinelLogger implements ILogger {

    private static SpinelLogger instance;


    private SpinelLogger() {

    }

    @Override
    public void warning(String TAG, String msg) {
        Log.w(TAG, msg);
    }

    @Override
    public void error(String TAG, String msg) {
        Log.e(TAG, msg);
    }

    @Override
    public void debug(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    @Override
    public void info(String TAG, String msg) {
        Log.i(TAG, msg);
    }


    public static SpinelLogger getInstance() {
        if (instance == null) {
            synchronized (SpinelLogger.class) {
                instance = new SpinelLogger();
            }
        }
        return instance;
    }
}
