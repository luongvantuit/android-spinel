package org.rogo.spinel.stream;

import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;

import org.rogo.spinel.helpers.SpinelLogger;
import org.rogo.spinel.interfaces.IStream;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SpinelStream implements IStream {

    private final static String TAG = SpinelStream.class.getSimpleName();

    private final UsbAccessory usbAccessory;

    private ParcelFileDescriptor parcelFileDescriptor;
    private FileInputStream inputStream;
    private FileOutputStream outputStream;

    public SpinelStream(UsbAccessory usbAccessory) {
        this.usbAccessory = usbAccessory;
    }

    @Override
    public void open(Context context) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        this.parcelFileDescriptor = usbManager.openAccessory(this.usbAccessory);
        if (this.parcelFileDescriptor == null) {
            throw new RuntimeException();
        }
        FileDescriptor fileDescriptor = this.parcelFileDescriptor.getFileDescriptor();
        this.inputStream = new FileInputStream(fileDescriptor);
        this.outputStream = new FileOutputStream(fileDescriptor);
    }

    @Override
    public void write(byte[] buffer) {
        try {
            this.outputStream.write(buffer);
        } catch (IOException e) {
            SpinelLogger.getInstance().error(SpinelStream.TAG, e.getMessage());
        }
    }

    @Override
    public byte[] read() {
        int available = 0;
        try {
            available = this.inputStream.available();
        } catch (IOException e) {
            SpinelLogger.getInstance().error(SpinelStream.TAG, e.getMessage());
        }
        byte[] buffer = new byte[available];
        try {
            final int i = this.inputStream.read(buffer, 0, available);
            SpinelLogger.getInstance().debug(SpinelStream.TAG, "Read bytes response --> " + i);
        } catch (IOException e) {
            SpinelLogger.getInstance().error(SpinelStream.TAG, e.getMessage());
        }
        return buffer;
    }

    @Override
    public byte readByte() {
        byte[] buffer = new byte[1];
        try {
            final int i = this.inputStream.read(buffer, 0, 1);
            SpinelLogger.getInstance().debug(SpinelStream.TAG, "Read bytes response --> " + i);
        } catch (IOException e) {
            SpinelLogger.getInstance().error(SpinelStream.TAG, e.getMessage());
        }
        return buffer[0];
    }

    @Override
    public void close() {
        try {
            this.outputStream.close();
            this.inputStream.close();
            this.parcelFileDescriptor.close();
        } catch (IOException e) {
            SpinelLogger.getInstance().error(TAG, e.getMessage());
        }
    }

    @Override
    public boolean getIsDataAvailable() {
        int available = 0;
        try {
            available = this.inputStream.available();
        } catch (IOException e) {
            SpinelLogger.getInstance().error(SpinelStream.TAG, e.getMessage());
        }
        return available != 0;
    }
}
