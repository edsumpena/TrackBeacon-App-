package com.example.emilysumpena.myapplication;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.example.emilysumpena.myapplication.MainActivity.settings;

public class recalibrate extends AppCompatActivity {
    private static final String TAG = "Calibration_Activity";
    String mac = "";
    boolean switchOn = false;
    int calibrationCounter = 0;
    double RSSI = 0;
    private Runnable runnable;
    int flag = 0;
    int textViewCounter = 1;
    int calibrationThreshHold = 20;
    double[] rssiArray = new double[5];
    double rssiMedian = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recalibrate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mac = settings.getString("mac", "0");
        final Button button = (Button) findViewById(R.id.startCalibration);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(flag == 0) {
                    switchOn = true;
                    flag = 1;
                    textViewCounter = 1;
                    calibrationSearch();
                }
            }


        });
        final Button button2 = (Button) findViewById(R.id.skipCalibration);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textViewCounter = 1;
                Intent myIntent = new Intent(recalibrate.this, MapsActivity.class);
                recalibrate.this.startActivity(myIntent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder100 = new AlertDialog.Builder(this);
        // Simply Do noting!
        builder100.setMessage("Are you sure you want to exit the app?");
        builder100.setTitle("Exit App");
        builder100.setCancelable(false);
        builder100.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder100.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
            }
        });
        AlertDialog alert = builder100.create();
        alert.show();
    }
    private BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            calibrationCounter = calibrationCounter + 1;
            System.out.println("calibrationCounter" + calibrationCounter);
            Log.d(TAG, "onReceive");

            String action = intent.getAction();
            String temp = "";
            Log.d(TAG, "ACTION = " + action);
//            Log.e("ywq", action);
            if (BluetoothDevice.ACTION_FOUND.equals(action) && switchOn) {
                Log.d(TAG, "ACTION = bluetooth device");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {    //显示已配对设备
                    Log.d(TAG, "device.getBondState == Bluetooth Device");
                    System.out.println("\n" + device.getName() + "==>" + device.getAddress() + "\n");
                } else if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    temp = temp + "\n" + device.getName() + "==>" + device.getAddress() + " " + intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    //mTextMessage2.setText("\n"+device.getName()+"==>"+device.getAddress()+"==> " + intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI ));
                    if (device.getAddress().equals(mac)) {
                        calibrationCounter = 0;
                        RSSI = Double.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
                        if(textViewCounter == 1) {
                            TextView locationTv = (TextView) findViewById(R.id.status1);
                            locationTv.setTextColor(Color.parseColor("#ff669900"));
                            locationTv.setText("RSSI Found");
                            rssiArray[0] = RSSI;
                        }
                        if(textViewCounter == 2) {
                            TextView locationTv = (TextView) findViewById(R.id.status2);
                            locationTv.setTextColor(Color.parseColor("#ff669900"));
                            locationTv.setText("RSSI Found");
                            rssiArray[1] = RSSI;
                        }
                        if(textViewCounter == 3) {
                            TextView locationTv = (TextView) findViewById(R.id.status3);
                            locationTv.setTextColor(Color.parseColor("#ff669900"));
                            locationTv.setText("RSSI Found");
                            rssiArray[2] = RSSI;
                        }
                        if(textViewCounter == 4) {
                            TextView locationTv = (TextView) findViewById(R.id.status4);
                            locationTv.setTextColor(Color.parseColor("#ff669900"));
                            locationTv.setText("RSSI Found");
                            rssiArray[3] = RSSI;
                        }
                        if(textViewCounter == 5) {
                            TextView locationTv = (TextView) findViewById(R.id.status5);
                            locationTv.setTextColor(Color.parseColor("#ff669900"));
                            locationTv.setText("RSSI Found");
                            rssiArray[4] = RSSI;
                            Arrays.sort(rssiArray);
                            rssiMedian = rssiArray[2];
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child("calibration").child(mac).removeValue();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                    mDatabase.child("calibration").child(mac).child((int) rssiMedian +"").setValue("");
                                    switchOn = false;
                                    calibrationCounter = 1;
                                    displayMessage("Calibration Successful!");
                                    Intent myIntent = new Intent(recalibrate.this, MapsActivity.class);
                                    recalibrate.this.startActivity(myIntent);
                                        }
                                    },500);
                                }
                            },600);
                        }
                        textViewCounter = textViewCounter + 1;
                        Log.d(TAG, "Address found");
                        try {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("mac", device.getAddress());
//                            loginByGet(jsonObject1.toString(),"http://143.89.144.200:12000/final/rawData.php?info=");
                            System.out.println(jsonObject1.toString());
                            //Toast.makeText(getApplicationContext(), "MAC = " + device.getAddress() + " RSSI = " + String.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI) + " Approximate Distance Away from Beacon = " + predictedDistance), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                        }

                        // loginByGet("device-name:" + device.getName() +",targer-mac:"  + device.getAddress() + ",RSSI:" +  intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI )+ ",sensor-mac:" +  getLocalMacAddressFromWifiInfo(context) + ",latitude:" + x + ",longitude:" + y + ",time:" + System.currentTimeMillis()/1000 );

                    }


                } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
//                Message message = new Message();
//                Bundle bundle = new Bundle();
//                bundle.clear();
//                message.what = 0x01;
//                bundle.putShort("msg", (short) 0);
//                message.setData(bundle);
//                handler.sendMessage(message);
                    System.out.println("Search Completed...");
                }
            }
        }
    };
    public void BlueToothDiscoveryy() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.d(TAG, "Device does not support Bluetooth");
            Toast.makeText(getApplicationContext(), "Your device does not support bluetooth!", Toast.LENGTH_LONG).show();
            flag = 0;
            switchOn = false;
        } else {
            Log.d(TAG, "Bluetooth Supported");
            // 不做提示，强行打开
            // mBluetoothAdapter.disable();
            // mBluetoothAdapter.enable();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver2, filter);
            IntentFilter filter2 = new IntentFilter(mBluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(mReceiver2, filter2);
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
                Log.d(TAG, " if (mBluetoothAdapter.isDiscovering())");
            }
            mBluetoothAdapter.startDiscovery();
            Log.d(TAG, " NO IF mBluetoothAdapter.isDiscovering()");
        }

    }
    public void calibrationSearch() {
        TextView locationTv = (TextView) findViewById(R.id.status1);
        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
        locationTv.setText("RSSI not found");
        locationTv = (TextView) findViewById(R.id.status2);
        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
        locationTv.setText("RSSI not found");
        locationTv = (TextView) findViewById(R.id.status3);
        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
        locationTv.setText("RSSI not found");
        locationTv = (TextView) findViewById(R.id.status4);
        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
        locationTv.setText("RSSI not found");
        locationTv = (TextView) findViewById(R.id.status5);
        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
        locationTv.setText("RSSI not found");
        System.out.println("Very beginning of BluetoothLoop");
        final Handler handler1 = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (switchOn && calibrationCounter <= calibrationThreshHold - 1) {
                    if(textViewCounter == 1) {
                        TextView locationTv = (TextView) findViewById(R.id.status1);
                        locationTv.setTextColor(Color.parseColor("#ffffbb33"));
                        locationTv.setText("Searching...");
                    }
                    if(textViewCounter == 2) {
                        TextView locationTv = (TextView) findViewById(R.id.status2);
                        locationTv.setTextColor(Color.parseColor("#ffffbb33"));
                        locationTv.setText("Searching...");
                    }
                    if(textViewCounter == 3) {
                        TextView locationTv = (TextView) findViewById(R.id.status3);
                        locationTv.setTextColor(Color.parseColor("#ffffbb33"));
                        locationTv.setText("Searching...");
                    }
                    if(textViewCounter == 4) {
                        TextView locationTv = (TextView) findViewById(R.id.status4);
                        locationTv.setTextColor(Color.parseColor("#ffffbb33"));
                        locationTv.setText("Searching...");
                    }
                    if(textViewCounter == 5) {
                        TextView locationTv = (TextView) findViewById(R.id.status5);
                        locationTv.setTextColor(Color.parseColor("#ffffbb33"));
                        locationTv.setText("Searching...");
                    }
                    System.out.println("public void run()");
                    BlueToothDiscoveryy();
                    handler1.postDelayed(this, 5000);
                    System.out.println("BlueToothDiscovery again");
                }
                if(switchOn && calibrationCounter >= calibrationThreshHold){
                    if(textViewCounter == 1) {
                        TextView locationTv = (TextView) findViewById(R.id.status1);
                        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
                        locationTv.setText("Failed to find RSSI");
                        displayMessage("Failed to find beacon, please try again.");
                        flag = 0;
                    }
                    if(textViewCounter == 2) {
                        TextView locationTv = (TextView) findViewById(R.id.status2);
                        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
                        locationTv.setText("Failed to find RSSI");
                        displayMessage("Failed to find beacon, please try again.");
                        flag = 0;
                    }
                    if(textViewCounter == 3) {
                        TextView locationTv = (TextView) findViewById(R.id.status3);
                        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
                        locationTv.setText("Failed to find RSSI");
                        displayMessage("Failed to find beacon, please try again.");
                        flag = 0;
                    }
                    if(textViewCounter == 4) {
                        TextView locationTv = (TextView) findViewById(R.id.status4);
                        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
                        locationTv.setText("Failed to find RSSI");
                        displayMessage("Failed to find beacon, please try again.");
                        flag = 0;
                    }
                    if(textViewCounter == 5) {
                        TextView locationTv = (TextView) findViewById(R.id.status5);
                        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
                        locationTv.setText("Failed to find RSSI");
                        displayMessage("Failed to find beacon, please try again.");
                        flag = 0;
                    }
                    calibrationCounter = 1;
                }
            }
        };
        handles.postDelayed(runnable, 100);
    }

    public Handler handles = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            short now = bundle.getShort("msg");
            if (msg.what == 0x01) {
                System.out.println("new discovery");
                //BlueToothDiscoveryy();
            }
        }


    };
    public void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
    public void areUSure(){
        final AlertDialog.Builder builder1000 = new AlertDialog.Builder(this);
        // Simply Do noting!
        builder1000.setMessage("Are you sure you want to skip the calibration process? Predicted Distances from beacon may be inaccurate! (you can re-calibrate beacon later)");
        builder1000.setTitle("Skip Calibration");
        builder1000.setCancelable(false);
        builder1000.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switchOn = false;
                flag = 1;
                Intent myIntent = new Intent(recalibrate.this, MapsActivity.class);
                recalibrate.this.startActivity(myIntent);
            }
        });
        builder1000.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
            }
        });
        AlertDialog alert = builder1000.create();
        alert.show();
    }

}
