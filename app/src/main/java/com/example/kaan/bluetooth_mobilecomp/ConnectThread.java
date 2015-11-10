package com.example.kaan.bluetooth_mobilecomp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class ConnectThread extends Thread {
    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;

    public ConnectThread(BluetoothDevice device) {
        mDevice = device;
        BluetoothSocket tempSocket = null;

        mSocket = tempSocket;
    }

    @Override
    public void run() {

    }

    public void cancel() {

    }
}