package com.george.medicmetrics.ui.connect;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.george.medicmetrics.bluetooth.device.Device;
import com.george.medicmetrics.bluetooth.gatt.ConnectGattCallback;
import com.george.medicmetrics.bluetooth.gatt.Gatt;
import com.george.medicmetrics.bluetooth.characteristic.GattCharacteristic;
import com.george.medicmetrics.bluetooth.service.GattService;
import com.george.medicmetrics.objects.Record;
import com.george.medicmetrics.ui.base.BaseContract;

import java.util.List;

interface ConnectDeviceContract {

    interface View extends BaseContract.View {

        @Nullable
        Gatt getDeviceGatt(@NonNull Device device, boolean autoConnect, @NonNull ConnectGattCallback callback);

        void broadcastAction(@NonNull String action);

        void broadcastAction(@NonNull String action, @NonNull String uuid, @NonNull Record record);
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void connect(@NonNull String address);

        void disconnect();

        @Nullable
        List<GattService> getGattServices();

        void readGattCharacteristic(@NonNull GattCharacteristic characteristic);

        void notifyGattCharacteristic(@NonNull GattCharacteristic characteristic, boolean enabled);
    }
}
