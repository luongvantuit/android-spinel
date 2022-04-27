package org.rogo.spinel;

import org.rogo.spinel.helpers.SpinelLogger;
import org.rogo.spinel.interfaces.ILogger;
import org.rogo.spinel.interfaces.IStream;

public class Spinel {


    /**
     * Initialization variable
     */
    private final static String TAG = Spinel.class.getSimpleName();


    /**
     * Option custom logger
     */
    private ILogger logger;

    /**
     * Requirement variable
     */
    private final IStream stream;

    public Spinel(IStream stream) {
        this(stream, SpinelLogger.getInstance());
    }

    public Spinel(IStream stream, ILogger logger) {
        this.stream = stream;
        logger.debug(TAG, "Initialization class");
    }
}
