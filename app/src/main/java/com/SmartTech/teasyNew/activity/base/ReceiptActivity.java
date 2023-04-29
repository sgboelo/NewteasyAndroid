package com.SmartTech.teasyNew.activity.base;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.SmartTech.teasyNew.popups.PopupOperationInProcess;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ReceiptActivity extends BaseActivity {

    private String KEY_LAST_USED_BT_PRINTER_ADDRESS = "KEY_LAST_USED_BT_PRINTER_ADDRESS";

    protected BluetoothAdapter mBtAdapter;
    private List<BluetoothDevice> foundDevices;
    protected String lastUsedDeviceAddress;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        this.lastUsedDeviceAddress = prefs.getString(KEY_LAST_USED_BT_PRINTER_ADDRESS, "");

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);
    }

    protected void startBtDeviceDiscovery() {
        foundDevices = new ArrayList<>();

        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        mBtAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        this.unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //sometimes it notifies twice the same device so that we should filter
                for(BluetoothDevice found : foundDevices) {
                    if(found.getAddress().equals(device.getAddress())) {
                        return;
                    }
                }
                foundDevices.add(device);

                btDeviceFound(device);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                btDiscoveryFinished();
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                if (state == BluetoothDevice.BOND_BONDED) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devicePaired(device);
                }
            }
        }
    };

    protected void devicePaired(BluetoothDevice device) {

    }

    protected void btDeviceFound(BluetoothDevice device) {

    }

    protected void btDiscoveryFinished() {

    }

    protected void connectAndPrint(Activity context, BluetoothDevice device, byte[] receipt, PrintCallback callback) {
        this.lastUsedDeviceAddress = device.getAddress();
        prefs.edit().putString(KEY_LAST_USED_BT_PRINTER_ADDRESS, device.getAddress()).apply();

        new ConnectAndPrint(context, device, receipt, callback).execute();
    }

    protected static class ConnectAndPrint extends AsyncTask<Object, Integer, Boolean> {
        private BluetoothDevice device;
        private byte[] receipt;
        private PopupOperationInProcess popup;
        private Activity context;
        private BluetoothSocket btSocket;
        private PrintCallback callback;

        public ConnectAndPrint(Activity context, BluetoothDevice device, byte[] receipt, PrintCallback callback) {
            this.device = device;
            this.receipt = receipt;
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            popup = new PopupOperationInProcess(context);
            popup.setPopupText("Please wait");
            popup.show();
        }

        @Override
        protected Boolean doInBackground(Object[] objects) {
            try { Thread.sleep(1000); }
            catch (InterruptedException e) { e.printStackTrace(); }

            try {
                btSocket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                try {
                    btSocket.connect();
                    OutputStream os = btSocket.getOutputStream();
                    os.write(receipt);
                    os.flush();

                    Thread.sleep(1500);

                    os.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean successful) {
            popup.dismiss();

            if(btSocket != null) {
                try {
                    btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(!successful) {
                callback.onFail();
            }
            else {
                callback.onSuccess();
            }
        }
    }

    public interface PrintCallback {
        void onSuccess();
        void onFail();
    }

}
