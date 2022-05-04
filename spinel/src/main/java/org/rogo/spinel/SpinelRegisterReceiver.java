package org.rogo.spinel;

import android.hardware.usb.UsbDevice;

public interface SpinelRegisterReceiver {

    void listen(UsbDevice usbDevice);

}
