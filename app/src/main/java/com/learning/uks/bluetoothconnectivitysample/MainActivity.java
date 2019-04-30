package com.learning.uks.bluetoothconnectivitysample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    /**
     * Defining the member variable
     */
    private ListView mListView;
    private ArrayAdapter aAdapter;
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothBroadcastReceiver mBluetoothBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Getting the instance of Button controls
         */
        Button btnGetPairedDevice = (Button)findViewById(R.id.btnGet);
        Button btntBluetoothOn = (Button)findViewById(R.id.btnOn);
        Button btnBluetoothOff = (Button)findViewById(R.id.btnOFF);
        Button btnBluetoothDiscovery = (Button)findViewById(R.id.btnDiscoverable);
        mListView = (ListView) findViewById(R.id.deviceList);
        final ArrayList<BluetoothDevice> mlistBluetoothDevice = new ArrayList<>();
        /**
         * Initializing the BluetoothBroadcastReceiver
         */
        mBluetoothBroadcastReceiver = new BluetoothBroadcastReceiver();
        /**
         * Enable the Bluetooth on Button Click
         */
        btntBluetoothOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bAdapter == null)
                {
                    Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!bAdapter.isEnabled()){
                        startActivityForResult(
                                new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                                1);
                        Toast.makeText(getApplicationContext(),"Bluetooth Turned ON",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /**
         * Disable the Bluetooth on Button Click
         */
        btnBluetoothOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bAdapter.disable();
                Toast.makeText(getApplicationContext(),"Bluetooth Turned OFF", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * Start Discovery of the devices on button click
         */
        btnBluetoothDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bAdapter.isDiscovering()){
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE),1);
                    Toast.makeText(getApplicationContext(),"Making Device Discoverable",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * Show the list of paired devices in a list view
         */
        btnGetPairedDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bAdapter==null){
                    Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
                }
                else{
                    Set<BluetoothDevice> pairedDevices =
                            bAdapter.getBondedDevices();
                    ArrayList listViewContent = new ArrayList();
                    if(pairedDevices.size()>0){
                        for(BluetoothDevice device: pairedDevices){
                            String devicename = device.getName();
                            String macAddress = device.getAddress();
                            int outcome = device.getBondState();
                            if (outcome == BluetoothDevice.BOND_BONDED) {
                                mlistBluetoothDevice.add(device);
                                listViewContent.add("Name: " + devicename + "MAC Address: " + macAddress);
                            }else {
                                Toast.makeText(getApplicationContext(),"Bonding Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                        aAdapter = new ArrayAdapter(getApplicationContext(),
                                android.R.layout.simple_list_item_1, listViewContent);
                        mListView.setAdapter(aAdapter);
                    }
                }
            }
        });
        /**
         * Set the item click Listener
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Toast.makeText(getApplicationContext(), "An item of the ListView is clicked.", Toast.LENGTH_LONG).show();
                String profile = null;

                Toast.makeText(getApplicationContext(), "OnReceiveMethodCalled", Toast.LENGTH_LONG).show();
                if (bAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) ==
                        BluetoothProfile.STATE_CONNECTED){
                    Toast.makeText(getApplicationContext(), "HFP Profile Connected", Toast.LENGTH_LONG).show();
                }
                else if (bAdapter.getProfileConnectionState(BluetoothProfile.A2DP) ==
                        BluetoothProfile.STATE_CONNECTED){
                    Toast.makeText(getApplicationContext(), "A2DP Profile Connected", Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    private final class BluetoothBroadcastReceiver extends BroadcastReceiver {
        BluetoothBroadcastReceiver()
        {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            getApplicationContext().registerReceiver(this, filter);
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            String profile = null;
            Toast.makeText(getApplicationContext(), "OnReceiveMethodCalled", Toast.LENGTH_LONG).show();
            if (bAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) ==
                    BluetoothProfile.STATE_CONNECTED){
                Toast.makeText(getApplicationContext(), "HFP Profile Connected", Toast.LENGTH_LONG).show();
            }
            else if (bAdapter.getProfileConnectionState(BluetoothProfile.A2DP) ==
                    BluetoothProfile.STATE_CONNECTED){
                Toast.makeText(getApplicationContext(), "A2DP Profile Connected", Toast.LENGTH_LONG).show();

            }

        }
    }
}

