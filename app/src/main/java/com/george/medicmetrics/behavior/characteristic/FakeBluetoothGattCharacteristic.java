package com.george.medicmetrics.behavior.characteristic;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.george.medicmetrics.behavior.descriptor.Descriptor;

import java.util.List;
import java.util.UUID;

public class FakeBluetoothGattCharacteristic implements GattCharacteristic {

    private String mUuid;
    private List<Integer> mIntegerList;
    private int mCurrent;

    public FakeBluetoothGattCharacteristic(String uuid, List<Integer> integerList) {
        mUuid = uuid;
        mIntegerList = integerList;
    }

    @NonNull
    @Override
    public UUID getUuid() {
        return UUID.fromString(mUuid);
    }

    @Override
    public int getProperties() {
        return BluetoothGattCharacteristic.PROPERTY_NOTIFY;
    }

    @Nullable
    @Override
    public Integer getIntValue(int formatType, int offset) {
        if (mCurrent == mIntegerList.size()) {
            mCurrent = 0;
        }
        return mIntegerList.get(mCurrent++);
    }

    @Nullable
    @Override
    public byte[] getValue() {
        // TODO: Implement
        return new byte[0];
    }

    @NonNull
    @Override
    public Descriptor getDescriptor(@NonNull UUID uuid) {
        // TODO: Implement
        return null;
    }
}