package com.george.medicmetrics.ui.connect;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.george.medicmetrics.bluetooth.adapter.Adapter;
import com.george.medicmetrics.bluetooth.characteristic.GattCharacteristic;
import com.george.medicmetrics.bluetooth.device.Device;
import com.george.medicmetrics.bluetooth.gatt.ConnectGattCallback;
import com.george.medicmetrics.bluetooth.gatt.Gatt;
import com.george.medicmetrics.bluetooth.service.GattService;
import com.george.medicmetrics.data.Injection;
import com.george.medicmetrics.objects.Record;
import com.george.medicmetrics.ui.base.BaseService;

import java.util.List;

public class ConnectDeviceService extends BaseService<ConnectDeviceContract.Presenter> implements ConnectDeviceContract.View, DeviceConnection {

    /**
     * Defines the intent actions that will be sent through the broadcast.
     *
     * @see DeviceConnection
     */
    public final static String ACTION_GATT_CONNECTED = "com.george.medicmetrics.ui.connect.GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.george.medicmetrics.ui.connect.GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.george.medicmetrics.ui.connect.GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.george.medicmetrics.ui.connect.DATA_AVAILABLE";

    public final static String EXTRA_DATA = "com.george.medicmetrics.ui.connect.DATA";
    public final static String EXTRA_UUID = "com.george.medicmetrics.ui.connect.UUID";

    private final IBinder mIBinder = new LocalBinder();

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, ConnectDeviceService.class);
    }

    @NonNull
    @Override
    protected ConnectDeviceContract.Presenter createPresenter() {
        Adapter adapter = Injection.provideAdapter(this);
        return new ConnectDevicePresenter(adapter);
    }

    public class LocalBinder extends Binder {
        public ConnectDeviceService getService() {
            return ConnectDeviceService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        return mIBinder;
    }

    @Override
    public void connect(@NonNull String deviceAddress) {
        mPresenter.connect(deviceAddress);
    }

    @Override
    public void disconnect() {
        mPresenter.disconnect();
    }

    @Nullable
    @Override
    public Gatt getDeviceGatt(@NonNull Device device, boolean autoConnect, @NonNull ConnectGattCallback callback) {
        return device.connectGatt(this, autoConnect, callback);
    }

    @Nullable
    @Override
    public List<GattService> getGattServices() {
        return mPresenter.getGattServices();
    }

    @Override
    public void readGattCharacteristic(@NonNull GattCharacteristic gattCharacteristic) {
        mPresenter.readGattCharacteristic(gattCharacteristic);
    }

    @Override
    public void notifyGattCharacteristic(@NonNull GattCharacteristic gattCharacteristic, boolean enabled) {
        mPresenter.notifyGattCharacteristic(gattCharacteristic, enabled);
    }

    @Override
    public void broadcastAction(@NonNull String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    @Override
    public void broadcastAction(@NonNull String action, @NonNull String uuid, @NonNull Record record) {
        Intent intent = new Intent(action);
        intent.putExtra(EXTRA_UUID, uuid);
        intent.putExtra(EXTRA_DATA, record);
        sendBroadcast(intent);
    }
}
