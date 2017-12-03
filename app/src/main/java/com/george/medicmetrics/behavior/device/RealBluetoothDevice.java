package com.george.medicmetrics.behavior.device;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.george.medicmetrics.behavior.gatt.ConnectGattCallback;
import com.george.medicmetrics.behavior.gatt.Gatt;
import com.george.medicmetrics.behavior.gatt.RealBluetoothGatt;
import com.george.medicmetrics.behavior.gatt.characteristic.GattCharacteristic;
import com.george.medicmetrics.behavior.gatt.characteristic.RealGattCharacteristic;

public class RealBluetoothDevice implements Device {

    private BluetoothDevice mBluetoothDevice;

    public RealBluetoothDevice(BluetoothDevice bluetoothDevice) {
        mBluetoothDevice = bluetoothDevice;
    }

    @NonNull
    public String getName() {
        return mBluetoothDevice.getName();
    }

    @NonNull
    public String getAddress() {
        return mBluetoothDevice.getAddress();
    }

    @Nullable
    public Gatt connectGatt(@NonNull Context context, boolean autoConnect, @NonNull final ConnectGattCallback callback) {
        BluetoothGatt bluetoothGatt = mBluetoothDevice.connectGatt(context, autoConnect, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int status, int newState) {
                Gatt gatt = new RealBluetoothGatt(bluetoothGatt);
                callback.onConnectionStateChange(gatt, status, newState);
            }
        });
        return new RealBluetoothGatt(bluetoothGatt);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RealBluetoothDevice)) {
            return false;
        }

        RealBluetoothDevice device = (RealBluetoothDevice) obj;
        return getAddress().equals(device.getAddress());
    }
}
