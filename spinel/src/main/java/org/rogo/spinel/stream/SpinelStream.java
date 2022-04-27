package org.rogo.spinel.stream;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import org.rogo.spinel.interfaces.IStream;

public class SpinelStream extends IStream {

    private final UsbManager usbManager;
    private final UsbDevice usbDevice;


    /**
     * Usb define
     */
    private UsbDeviceConnection usbDeviceConnection;
    private UsbInterface usbInterface;
    private UsbEndpoint usbEndpoint;

    public SpinelStream(UsbManager usbManager, UsbDevice usbDevice) {
        this.usbManager = usbManager;
        this.usbDevice = usbDevice;
    }


    @Override
    public void open() {
        this.usbDeviceConnection = usbManager.openDevice(usbDevice);
        this.usbInterface = this.usbDevice.getInterface(0);
        this.usbEndpoint = this.usbInterface.getEndpoint(0);
    }

    @Override
    public void write(byte[] buffer) {
        this.write(buffer, buffer.length, 0);
    }

    public void write(byte[] buffer, int timeout) {
        this.write(buffer, buffer.length, timeout);
    }

    public void write(byte[] buffer, int length, int timeout) {
        this.usbDeviceConnection.bulkTransfer(this.usbEndpoint, buffer, length, timeout);
    }


    @Override
    public byte[] read() {
        return new byte[0];
    }

    @Override
    public byte readByte() {
        return 0;
    }

    @Override
    public void close() {
        this.usbDeviceConnection.close();
    }

    public void setUsbInterface(UsbInterface usbInterface) {
        this.usbInterface = usbInterface;
    }

    public void setUsbEndpoint(UsbEndpoint usbEndpoint) {
        this.usbEndpoint = usbEndpoint;
    }
}
