package org.rogo.spinel;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import org.rogo.spinel.helpers.SpinelLogger;
import org.rogo.spinel.interfaces.ILogger;
import org.rogo.spinel.interfaces.IStream;

public class Spinel {

    private final class SpinelBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Spinel.ACTION_REQUEST_PERMISSION_USB_ACCESSORY)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            spinelRegisterReceiver.listen(device);
                        }
                    } else {
                        SpinelLogger.getInstance().debug(TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    }


    public final static String ACTION_REQUEST_PERMISSION_USB_ACCESSORY = "com.android.spinel.USB_PERMISSION";

    /**
     * Initialization variable
     */
    private final static String TAG = Spinel.class.getSimpleName();

    private final WpanApi wpanApi;

    private final SpinelRegisterReceiver spinelRegisterReceiver;


    /**
     * Requirement variable
     */
    private final IStream stream;

    public Spinel(IStream stream, SpinelRegisterReceiver spinelRegisterReceiver) {
        this.stream = stream;
        this.wpanApi = new WpanApi(stream);
        this.spinelRegisterReceiver = spinelRegisterReceiver;
        SpinelLogger.getInstance().debug(TAG, "Initialization class");
    }


    public WpanApi getWpanApi() {
        return wpanApi;
    }

    public static void requestPermission(Context context, UsbDevice usbDevice) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(Spinel.ACTION_REQUEST_PERMISSION_USB_ACCESSORY), 0);
        usbManager.requestPermission(usbDevice, pendingIntent);
    }
}
