package com.example.emilysumpena.myapplication;


import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int x = 0;
    int flag = 0;
    double RSSI = 0;
    double hold = 0;
    double predictedDistance = 666666666;
    private double z = 0, v = 0;
    private LocationManager locationManager;
    private Runnable runnable;
    String ID = "";
    boolean signal = true;
    private static final String TAG = "Debug MainActivity";
    EditText BeaconID;
    static SharedPreferences settings;
    static SharedPreferences.Editor editor;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
    int lol = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = this.getPreferences(MODE_MULTI_PROCESS);
        super.onCreate(savedInstanceState);
        int PERMISSION_ALL = 1;
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Button button = (Button) findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                x = 1;
                Log.d(TAG, "x = 1");
                EditText placeholder = (EditText) findViewById(R.id.BeaconID);
                ID = "" + placeholder.getText();
                Log.d(TAG, "ID = " + ID);
                flag = 0;
                BluetoothLoop();
            }
        });
        final Button button2 = (Button) findViewById(R.id.info);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Instructions.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        final Button button3 = (Button) findViewById(R.id.skip);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editor = settings.edit();
                editor.putString("mac", "");
                editor.putString("rssi", "");
                editor.putString("ts", "");
                editor.commit();
                Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(myIntent);
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

    public void BlueToothDiscovery() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.d(TAG, "Device does not support Bluetooth");
            Toast.makeText(getApplicationContext(), "Your device does not support bluetooth!", Toast.LENGTH_LONG).show();
            flag = 5;
        } else {
            Log.d(TAG, "Bluetooth Supported");
            // 不做提示，强行打开
            // mBluetoothAdapter.disable();
            // mBluetoothAdapter.enable();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);
            IntentFilter filter2 = new IntentFilter(mBluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(mReceiver, filter2);

            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
                Log.d(TAG, " if (mBluetoothAdapter.isDiscovering())");
            }
            mBluetoothAdapter.startDiscovery();
            Log.d(TAG, " NO IF mBluetoothAdapter.isDiscovering()");
        }

    }
    private void getLocationwifi(){
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String locationProvider;

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //此处的判定是主要问题，API23之后需要先判断之后才能调用locationManager中的方法
            //包括这里的getLastKnewnLocation方法和requestLocationUpdates方法
            List<String> providers = locationManager.getProviders(true);
            if(providers.contains(LocationManager.GPS_PROVIDER)){
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
                //如果是Network
                locationProvider = LocationManager.NETWORK_PROVIDER;
            }else{
                Toast.makeText(this, "Location provider not available.", Toast.LENGTH_SHORT).show();
                return ;
            }

            Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null){
                showLocation(location);
            }else{
                Toast.makeText(this, "No location data.", Toast.LENGTH_SHORT).show();
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,locationListener);
        }
    }

    private void getLocationgps(){
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String locationProvider;

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //此处的判定是主要问题，API23之后需要先判断之后才能调用locationManager中的方法
            //包括这里的getLastKnewnLocation方法和requestLocationUpdates方法
            List<String> providers = locationManager.getProviders(true);
            if(providers.contains(LocationManager.GPS_PROVIDER)){
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
                //如果是Network
                locationProvider = LocationManager.NETWORK_PROVIDER;
            }else{
                Toast.makeText(this, "Location provider not available.", Toast.LENGTH_SHORT).show();
                return ;
            }

            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null){
                showLocation(location);
            }else{
                Toast.makeText(this, "No location data.", Toast.LENGTH_SHORT).show();
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,locationListener);
        }
    }

    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void showLocation(Location location){
        String currentPosition="latitude is "+location.getLatitude()+", "+"longitude is "+location.getLongitude();
        System.out.println(currentPosition);
        z = location.getLatitude();
        v = location.getLongitude();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mac",getLocalMacAddressFromWifiInfo(MainActivity.this));
            jsonObject.put("lat", String.valueOf(z));
            jsonObject.put("long", String.valueOf(v));
            jsonObject.put("ts", System.currentTimeMillis()/1000);
//            loginByGet(jsonObject.toString(),"http://143.89.144.200:12000/final/sensorData.php?info=");
        }catch (JSONException e){
        }
    }
    // get device macaddress
    public static String getLocalMacAddressFromWifiInfo(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac =  winfo.getMacAddress();
        return mac;
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");

            String action = intent.getAction();
            String temp = "";
            Log.d(TAG, "ACTION = " + action);
//            Log.e("ywq", action);
            if (BluetoothDevice.ACTION_FOUND.equals(action) && signal) {
                Log.d(TAG, "ACTION = bluetooth device");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {    //显示已配对设备
                    Log.d(TAG, "device.getBondState == Bluetooth Device");
                    System.out.println("\n" + device.getName() + "==>" + device.getAddress() + "\n");
                } else if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    temp = temp + "\n" + device.getName() + "==>" + device.getAddress() + " " + intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    //mTextMessage2.setText("\n"+device.getName()+"==>"+device.getAddress()+"==> " + intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI ));
                    if (device.getAddress().equals(ID)) {
                        signal = false;
                        RSSI = Double.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
                        Log.d(TAG, String.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI))+ "<- WHat came out     What's in variable -> " + RSSI);
                        RSSI = -69 - RSSI;
                        hold = 10*2;
                        RSSI = RSSI / hold;
                        RSSI = Math.pow(10,RSSI);
                        predictedDistance = RSSI;
                        Log.d(TAG, "Predicted Distance: " + predictedDistance);
                        editor = settings.edit();
                        editor.putString("mac", device.getAddress());
                        editor.putString("rssi", String.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI)));
                        editor.putString("ts", String.valueOf(System.currentTimeMillis() / 1000));
                        editor.commit();
                        flag = 5;
                        Toast.makeText(getApplicationContext(), "Device Found!!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Address found");
                        getLocationgps();
                        DatabaseReference mDatabase6 = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference mRSSI6 = mDatabase6.child("beaconPhone");
                        mRSSI6.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(ID)){
                                    Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
                                    MainActivity.this.startActivity(myIntent);
                                } else {
                                    DatabaseReference mDatabase10 = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference mRSSI10 = mDatabase10.child("calibration");
                                    mRSSI10.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(ID)){
                                                Intent myIntent = new Intent(MainActivity.this, calibration.class);
                                                MainActivity.this.startActivity(myIntent);
                                            } else {
                                                Intent myIntent = new Intent(MainActivity.this, calibration.class);
                                                MainActivity.this.startActivity(myIntent);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(), "Error getting data from Database.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Error getting data from Database.", Toast.LENGTH_LONG).show();
                            }
                        });
                        /*final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                        if(lol == 1){
                            DatabaseReference mDatabase10 = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference mRSSI10 = mDatabase10.child("calibration");
                            mRSSI10.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(ID)){
                                        Intent myIntent = new Intent(MainActivity.this, calibration.class);
                                        MainActivity.this.startActivity(myIntent);
                                    } else {
                                        Intent myIntent = new Intent(MainActivity.this, calibration.class);
                                        MainActivity.this.startActivity(myIntent);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Error getting data from Database.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                            }
                        }, 1000);*/
                        try {
                            setFindBeacon(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("mac", device.getAddress());
                            jsonObject1.put("lat", String.valueOf(z));
                            jsonObject1.put("long", String.valueOf(v));
                            jsonObject1.put("rssi", String.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI)));
                            jsonObject1.put("ts", String.valueOf(System.currentTimeMillis() / 1000));
                            jsonObject1.put("predictedDistance", predictedDistance);
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

    private void setFindBeacon(int a) {
        Log.d(TAG, " rssi=" + a + " MAC = " + ID);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void BluetoothLoop() {
        System.out.println("Very beginning of BluetoothLoop");
        final Handler handler1 = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(flag == 4) {
                    Toast.makeText(getApplicationContext(), "Could not find the device! Make sure bluetooth and the device is turned on and that the Beacon Address is entered correctly.", Toast.LENGTH_SHORT).show();
                }
                if(flag <= 3) {
                    System.out.println("public void run()");
                    Toast.makeText(getApplicationContext(), "Scanning... Please wait, do NOT press button again!", Toast.LENGTH_LONG).show();
                    BlueToothDiscovery();
                    handler1.postDelayed(this, 5000);
                    System.out.println("BlueToothDiscovery again");
                    flag = flag + 1;
                    System.out.println("flag = " + flag);
                }
            }
        };
        handler.postDelayed(runnable, 100);
    }

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            short now = bundle.getShort("msg");
            if (msg.what == 0x01) {
                System.out.println("new discovery");
                BlueToothDiscovery();
            }
            }


    };





}







