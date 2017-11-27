package com.george.medicmetrics.ui.scan;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.george.medicmetrics.R;
import com.george.medicmetrics.behavior.bluetooth.BluetoothScanBehavior;
import com.george.medicmetrics.data.Device;
import com.george.medicmetrics.injection.Injection;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ScanDeviceFragment extends Fragment implements ScanDeviceContract.View {

    private DeviceAdapter mDeviceAdapter;
    private ScanDeviceContract.Presenter mPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static ScanDeviceFragment newInstance() {
        return new ScanDeviceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Handler handler = new Handler();
        BluetoothScanBehavior bluetoothScanBehavior = Injection.provideBluetoothScanBehavior(getContext());
        Executor executor = Executors.newSingleThreadExecutor();

        mPresenter = new ScanDevicePresenter(handler, bluetoothScanBehavior, executor);
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_device, container, false);

        setupRefreshLayout(view);
        setupAdapter();
        setupRecyclerView(view);

        return view;
    }

    private void setupRefreshLayout(@NonNull View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDeviceAdapter.setDeviceList(null);
                mDeviceAdapter.notifyDataSetChanged();

                mPresenter.scanDevices();
            }
        });
    }

    private void setupAdapter() {
        mDeviceAdapter = new DeviceAdapter(null);
        mDeviceAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Device device) {
                // TODO: Connect to device
            }
        });
    }

    private void setupRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mDeviceAdapter);
    }

    @Override
    public void showDevices(@NonNull List<Device> deviceList) {
        mDeviceAdapter.setDeviceList(deviceList);
        mDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressIndicator() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressIndicator() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void openBluetoothSettings(int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.scanDevices();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        mPresenter.handleBluetoothSettingsResult(requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        mPresenter.stopScanning();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
