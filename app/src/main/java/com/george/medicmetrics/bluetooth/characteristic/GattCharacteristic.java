package com.george.medicmetrics.bluetooth.characteristic;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.george.medicmetrics.bluetooth.descriptor.Descriptor;

import java.util.UUID;

public interface GattCharacteristic {

    String UUID_HEART_RATE = "00002a37-0000-1000-8000-00805f9b34fb";
    String UUID_BODY_TEMPERATURE = "0000ff72-0000-1000-8000-00805f9b34fb";
    String UUID_CONFIG_CHARACTERISTIC = "00002902-0000-1000-8000-00805f9b34fb";

    @NonNull
    UUID getUuid();

    int getProperties();

    @Nullable
    Integer getIntValue(int formatType, int offset);

    @Nullable
    byte[] getValue();

    @Nullable
    Descriptor getDescriptor(@NonNull UUID uuid);
}