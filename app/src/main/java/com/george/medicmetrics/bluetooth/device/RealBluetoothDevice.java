package com.george.medicmetrics.bluetooth.device;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.george.medicmetrics.bluetooth.gatt.ConnectGattCallback;
import com.george.medicmetrics.bluetooth.gatt.Gatt;
import com.george.medicmetrics.bluetooth.gatt.RealBluetoothGatt;
import com.george.medicmetrics.bluetooth.characteristic.GattCharacteristic;
import com.george.medicmetrics.bluetooth.characteristic.RealBluetoothGattCharacteristic;

public class RealBluetoothDevice implements Device {

    private BluetoothDevice mBluetoothDevice;

    public RealBluetoothDevice(BluetoothDevice bluetoothDevice) {
        mBluetoothDevice = bluetoothDevice;
    }

    @NonNull
    public String getName() {
        return mBluetoothDevice.getName() == null ? "n/a" : mBluetoothDevice.getName();
    }

    @NonNull
    public String getAddress() {
        return mBluetoothDevice.getAddress();
    }

    @Nullable
    public Gatt connectGatt(@NonNull Context context, boolean autoConnect, @NonNull final ConnectGattCallback callback) {
        // TODO: Don't create additional GATTs
        BluetoothGatt bluetoothGatt = mBluetoothDevice.connectGatt(context, autoConnect, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int status, int newState) {
                callback.onConnectionStateChange(new RealBluetoothGatt(bluetoothGatt), status, newState);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int status) {
                callback.onServicesDiscovered(new RealBluetoothGatt(bluetoothGatt), status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic, int status) {
                GattCharacteristic gattCharacteristic = new RealBluetoothGattCharacteristic(characteristic);
                callback.onCharacteristicRead(new RealBluetoothGatt(bluetoothGatt), gattCharacteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic) {
                GattCharacteristic gattCharacteristic = new RealBluetoothGattCharacteristic(characteristic);
                callback.onCharacteristicChanged(new RealBluetoothGatt(bluetoothGatt), gattCharacteristic);
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
