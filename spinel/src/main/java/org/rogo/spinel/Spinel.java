package org.rogo.spinel;

import org.rogo.spinel.helpers.SpinelLogger;
import org.rogo.spinel.interfaces.ILogger;
import org.rogo.spinel.interfaces.IStream;

public class Spinel {

    public final static String ACTION_REQUEST_PERMISSION_USB_ACCESSORY = "com.android.spinel.USB_PERMISSION";

    /**
     * Initialization variable
     */
    private final static String TAG = Spinel.class.getSimpleName();

    private final WpanApi wpanApi;


    /**
     * Requirement variable
     */
    private final IStream stream;

    public Spinel(IStream stream) {
        this.stream = stream;
        this.wpanApi = new WpanApi(stream);
        SpinelLogger.getInstance().debug(TAG, "Initialization class");
    }


    public WpanApi getWpanApi() {
        return wpanApi;
    }
}
