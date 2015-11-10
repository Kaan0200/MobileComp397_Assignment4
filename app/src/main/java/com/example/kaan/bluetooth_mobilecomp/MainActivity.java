package com.example.kaan.bluetooth_mobilecomp;

import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;

    private TextView commandTextView;
    private Button refreshBluetoothDevicesButton;
    private Button startConnectionButton;
    private Button resetFileButton;
    private Button sendCommandButton;
    private Spinner selectedDeviceSpinner;

    public final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private String outputFileName = "Assignment4Datadump.txr";
    private File outputFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        selectedDeviceSpinner = (Spinner) findViewById(R.id.spinner);
        commandTextView = (TextView) findViewById(R.id.commandText);
        startConnectionButton = (Button) findViewById(R.id.startButton);
        resetFileButton = (Button) findViewById(R.id.resetFileButton);
        refreshBluetoothDevicesButton = (Button) findViewById(R.id.devicesButton);
        sendCommandButton = (Button) findViewById(R.id.sendButton);

        //button to refresh bluetooth devices
        commandTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (commandTextView.getText().equals("")){
                    sendCommandButton.setEnabled(false);
                } else {
                    sendCommandButton.setEnabled(true);
                }
            }
        });

        refreshBluetoothDevicesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshPairedListSpinner(v);
            }
        });
        startConnectionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectToSelectedDevice(v);
            }
        });
        resetFileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetDataFile(v);
            }
        });
        sendCommandButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                sendCommand(v);
            }
        });
        //get the blue tooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    private void doDiscover() {

        // indicate that we are now scanning

        // check if discovering, stop if is
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // start up
        mBluetoothAdapter.startDiscovery();
    }

    public void BluetoothOn(View v) {
        if (!mBluetoothAdapter.isEnabled()) {

        }
    }

    //
    public void connectToSelectedDevice(View v) {
        BluetoothDevice selectedDevice = null;
        if (mBluetoothAdapter != null) {
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

            // get the bluetooth device that is the selected string
            String selectedString = selectedDeviceSpinner.getSelectedItem().toString();

            for (BluetoothDevice d : devices) {
                if (d.getName().equals(selectedString)) {
                    selectedDevice = d;
                }
                // otherwise skip
            }
            // do run
            new ConnectThread(selectedDevice).run();
        }
    }

    // button to refresh the spinner with the selection of devices for bluetooth
    public void refreshPairedListSpinner(View v) {
        // get all the unique blue tooth devices
        if (mBluetoothAdapter != null) {
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

            ArrayList<String> convertList = new ArrayList<>();
            // convert all bluetooth devices into devices for the spinner
            for (BluetoothDevice d : devices) {
                convertList.add(d.getName());
            }

            // populate spinner
            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, convertList);
            selectedDeviceSpinner.setAdapter(adapter);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("No Bluetooth Adapter")
                    .setMessage("Unable to get list of bonded devices")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // nothing
                        }
                    }).show();
        }
    }

    private void resetDataFile(View v){
        try {
            outputFile = new File(Environment.getExternalStoragePublicDirectory("DIRECTORY_RINGTONES"), outputFileName);
            outputFile.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean sendCommand(View v) {
        //new ConnectThread(selectedDevice).run();
        return false;
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mSocket;

        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tempSocket = null;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tempSocket = device.createRfcommSocketToServiceRecord(myUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket = tempSocket;

            try {
                tmpIn = mSocket.getInputStream();
                tmpOut = mSocket.getOutputStream();
            } catch (IOException e){
                e.printStackTrace();
            }
            mmInputStream = tmpIn;
            mmOutputStream = tmpOut;
        }

        @Override
        public void run() {
            // turn off
            mBluetoothAdapter.cancelDiscovery();
            try {
                mSocket.connect();

            } catch (IOException e) {
                try {
                    mSocket.close();
                } catch (IOException ee ){
                    return;
                }
            }

            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInputStream.read(buffer);

                    FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write(bytes);

                } catch (IOException e) {
                    break;
                }
            }
        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
