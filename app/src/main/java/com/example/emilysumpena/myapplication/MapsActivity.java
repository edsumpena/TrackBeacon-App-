package com.example.emilysumpena.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;

import static com.example.emilysumpena.myapplication.MainActivity.settings;
import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.asin;
import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.atan2;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Debug MapsActivity";
    private GoogleMap mMap;
    private Menu menu;
    private LocationManager locationManager;
    String mac;
    String lat;
    String longitude;
    String rssi;
    double lati, longi;
    double RSSI = 0;
    int hold = 0;
    int getDataFlag = 0;
    int flag = 0;
    int blueflag = 0;
    int indicator = 0;
    int errorkiller = 0;
    int autoCounter = 0;
    int length = 0;
    double predictedDistance = 0;
    double savePredictedDistance = 0;
    double latiSave = 0, longiSave = 0;
    private Runnable runnable;
    private Runnable run_able;
    boolean autoflag = true;
    boolean override = false;
    boolean switchOn = false;
    String macSave = "";
    //boolean check = true;
    boolean beaconInRange = true;
    int errorkiller2 = 0;
    int v = 0;
    boolean beginning = true;
    int flagTree = 0;
    String d = "";
    private Switch mySwitch;
    Circle myCircle2;
    private Switch mySwitch2;
    private Switch mySwitch3;
    boolean missingSwitchFlag;
    boolean autoSwitchFlag;
    int kk = 0;
    int mm = 0;
    int nn = 0;
    boolean missingSearchFlag;
    String autoSwitch = "";
    String missingSwitch = "";
    String missingSearch = "";
    static SharedPreferences settinggs;
    static SharedPreferences.Editor editor;
    boolean signal = false;
    boolean switchOn69 = false;
    Circle circle2;
    boolean switchOn70;
    //check = true;
    boolean autoflag69 = true;
    int autoCounter69 = 0;
    int errorkiller69 = 0;
    double latiSave69, longiSave69 = 0;
    double savePredictedDistance69 = 0;
    private String deviceMacAddress;
    Circle myCircle;
    Circle myCircle3;
    String name = "";
    String name2 = "";
    int a0 = 0;
    int size = 0;
    String timeZone = "";
    String savePredictedDistance1000 = "";
    String deviceTimeZone = "";
    ArrayList<String> macAddressList = new ArrayList<String>();
    ArrayList<String> deviceMacAddressList = new ArrayList<String>();
    ArrayList<String> latiLongiList = new ArrayList<String>();
    ArrayList<String> distanceAwayList = new ArrayList<String>();
    ArrayList<String> dateList = new ArrayList<String>();
    ArrayList<List<Double>> updatedLocation = new ArrayList<>();
    ArrayList<Double> updatedDistance = new ArrayList<>();
    ArrayList<Double> zValues = new ArrayList<>();
    String notDate = "";
    String notTime = "";
    int nullCounter = 6;
    boolean debugger = true;
    String latiSave10 = null;
    String longiSave10 = null;
    String savePredictedDistance10 = null;
    String dateTimeZone10 = null;
    double latiSaveDouble = -999999999;
    double longiSaveDouble = -999999999;
    int missingSearchThreshHold = 6;
    int pp = -2;
    int dd = 0;
    int ev = 0;
    int av = 0;
    int iv = 0;
    double qwer = 0;
    int mn = 0;
    int zzz = 0;
    String fx = "";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    int ccounter = 7;
    int autoThreshHold = 17;
    int looploop = 0;
    int anonymousFound = 0;
    double rssiUnit = -69.0;
    int locationValue = 2;
    float precision = 0;
    int listMacAddressFlag = 0;
    int listMacAddressFlagToo = 0;
    double [][] tempArray;
    double [] tempArray2;
    double[] zArray;
    Circle circle10;
    Circle circle11;
    Circle circle12;
    Circle circle13;
    Circle circle14;
    Circle circle15;
    Circle circle16;
    Circle circle17;
    Circle circle18;
    Circle circle19;
    Circle circle20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("calibration");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if (!dataSnapshot.hasChild("date&time" + macAddressList.get(size))){
                if (dataSnapshot.hasChild(mac) && !mac.equals("")){
                    DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference().child("calibration").child(mac);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                rssiUnit = Double.valueOf(ds.getKey());
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    ref0.addListenerForSingleValueEvent(eventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to reach Database.", Toast.LENGTH_SHORT).show();
            }
        });
        detectP1();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_Missing_Mode:
                        if (ev == 0 && !mac.equals("")) {
                            menuItem.setChecked(true);
                            ev = ev + 1;
                            switchOn70 = true;
                            //Toast.makeText(getApplicationContext(), "Missing mode is now ON!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Missing was just turned ON");
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Switches").child("missingSwitch" + mac).setValue("on");
                            saveMac(mac);
                            checkMissing();
                            listMacAddressFlagToo = 0;
                            flagTree = 0;
                            final Handler hqandler = new Handler();
                            hqandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                  getData();
                                    final Handler handddler = new Handler();
                                    handddler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                           // notificationOther();
                                        }
                                    }, 1000);
                                }
                            }, 1000);
                        } else if (ev == 1 && !mac.equals("")) {
                            menuItem.setChecked(false);
                            ev = 0;
                            switchOn70 = false;
                           // Toast.makeText(getApplicationContext(), "Missing mode is now OFF!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Missing was just turned OFF");
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Switches").child("missingSwitch" + mac).setValue("off");
                            mDatabase.child("MacIDs").child(mac).removeValue();
                        } else {
                            Toast.makeText(getApplicationContext(), "You must register your own beacon to use this feature! Missing mode autoset to false!", Toast.LENGTH_SHORT).show();
                            menuItem.setChecked(false);
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_Auto_Search:
                        if (iv == 0 && !mac.equals("")) {
                            menuItem.setChecked(true);
                            iv = iv + 1;
                            //Toast.makeText(getApplicationContext(), "autoSearch is now ON!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "autoSearch was just turned ON");
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Switches").child("autoSwitch" + mac).setValue("on");
                            switchOn = true;
                            //check = true;
                            autoflag = true;
                            autoCounter = 0;
                            errorkiller2 = 0;
                            autoCounter = 0;
                            autoSearch();
                        } else if (iv == 1 && !mac.equals("")) {
                            menuItem.setChecked(false);
                            iv = 0;
                            //.makeText(getApplicationContext(), "autoSearch is now OFF!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "autoSearch was just turned OFF");
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Switches").child("autoSwitch" + mac).setValue("off");
                            switchOn = false;
                            autoflag = false;
                            autoCounter = 0;
                        } else {
                            Toast.makeText(getApplicationContext(), "You must register your own beacon to use this feature!", Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_Search_All:
                        if (av == 0) {
                            autoThreshHold = 17;
                            av = av + 1;
                            menuItem.setChecked(true);
                            //Toast.makeText(getApplicationContext(), "Missing Search is now ON!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "MissingSearch was just turned ON");
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Switches").child("missingSearch" + mac).setValue("on");
                            switchOn69 = true;
                            //check = true;
                            autoflag69 = true;
                            autoCounter69 = 0;
                            errorkiller69 = 0;
                            missingSearch = "on";
                            missingSearchLoop();
                        } else if (av == 1) {
                            autoThreshHold = 17;
                            av = 0;
                            menuItem.setChecked(false);
                            //Toast.makeText(getApplicationContext(), "Missing Search is now OFF!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "MissingSearch was just turned OFF");
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Switches").child("missingSearch" + mac).setValue("off");
                            switchOn69 = false;
                            missingSearch = "off";
                            autoflag69 = false;
                            autoCounter69 = 0;
                            errorkiller69 = 0;
                            Log.d(TAG, "2.0jj switchOn69 (@switch) = " + switchOn69);
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_MissingNumber:
                        menuItem.setChecked(false);
                        Intent myIntent = new Intent(MapsActivity.this, numberCheck.class);
                        MapsActivity.this.startActivity(myIntent);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_ChangeNumber:
                        menuItem.setChecked(false);
                        drawerLayout.closeDrawers();
                        DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference().child("beaconPhone").child(mac);
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    name2 = ds.getKey();
                                    fx = name2;
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        ref0.addListenerForSingleValueEvent(eventListener);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!fx.equals("")) {
                                    Intent myIntent2 = new Intent(MapsActivity.this, changeNumber.class);
                                    MapsActivity.this.startActivity(myIntent2);
                                } else {
                                    Toast.makeText(getApplicationContext(), "You must register your own beacon and sync a phone number to it to use this feature!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },500);
                        return true;
                    case R.id.nav_Refresh:
                        menuItem.setChecked(false);
                        if (!mac.equals("")) {
                            errorkiller = 0;
                            blueflag = 0;
                            BluetoothLoop();
                        } else {
                            Toast.makeText(getApplicationContext(), "You must register your own beacon to use this feature!", Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_MyLocation:
                        menuItem.setChecked(false);
                        getLocationgps();
                        LatLng latLng = new LatLng(lati, longi);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_BeaconLocation:
                        menuItem.setChecked(false);
                        if (latiSaveDouble == -999999999 && longiSaveDouble == -999999999) {
                            Toast.makeText(getApplicationContext(), "Your iBeacon has not been found by another user! (Values found if beacon set to missing mode)", Toast.LENGTH_SHORT).show();
                        } else if (latiSaveDouble != -999999999 && longiSaveDouble != -999999999) {
                            LatLng latjng = new LatLng(latiSaveDouble, longiSaveDouble);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latjng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_ClearData:
                        menuItem.setChecked(false);
                        dd = 0;
                        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference().child("Notification Data");
                        latiSaveDouble = -999999999;
                        longiSaveDouble = -999999999;
                        savePredictedDistance1000 = "0";
                        final Handler handleri = new Handler();
                        handleri.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!mac.equals("")){
                                    firebase.child(mac).removeValue();
                                    Toast.makeText(getApplicationContext(), "Data successfully cleared! (Enabling missing beacon will update values)", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },1000);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_ChangeBeacon:
                        menuItem.setChecked(false);
                        Intent myIntent3 = new Intent(MapsActivity.this, MainActivity.class);
                        MapsActivity.this.startActivity(myIntent3);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_ClearAnonymous:
                        menuItem.setChecked(false);
                        anonymousFound = 0;
                        displayMessage("Points Cleared!");
                        mMap.clear();
                        drawerLayout.closeDrawers();
                        return true;
                   /* case R.id.nav_inside:
                        if (mn == 0) {
                            menuItem.setChecked(true);
                            mn = mn + 1;
                            locationValue = 3;
                        } else if (mn == 1) {
                            menuItem.setChecked(false);
                            mn = 0;
                            locationValue = 2;
                        }
                        drawerLayout.closeDrawers();
                        return true;*/
                    case R.id.nav_calibrate:
                        Intent myIntentp = new Intent(MapsActivity.this, recalibrate.class);
                        MapsActivity.this.startActivity(myIntentp);
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });
        settinggs = this.getPreferences(MODE_MULTI_PROCESS);
        mac = settings.getString("mac", "0");
        lat = settings.getString("lat", "0");
        longitude = settings.getString("long", "0");
        rssi = settings.getString("rssi", "0");
        //int predictedDistance = settings.getInt("predictedDistance", 0);
        String ts = settings.getString("ts", "0");
        if (!rssi.equals("")) {
            RSSI = Double.valueOf(rssi);
        } else {
            RSSI = 0.0000;
        }
        Log.d(TAG, String.valueOf("RSSI: -> " + RSSI));
        Log.d(TAG, "MAC = " + mac + " RSSI = " + rssi + " Latitude = " + lat + " Longitude = " + longitude + " Predicted Distance " + " WIP");
       // getData();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        switchOn70 = true;
       // notificationOther();
        switchOn70 = false;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        bluetoothDataLoop();
        //-----------------------------------------------------------------  \/ Buttons, Switches
        /*final Button button = (Button) findViewById(R.id.refresh);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!mac.equals("")) {
                    errorkiller = 0;
                    blueflag = 0;
                    BluetoothLoop();
                } else {
                    Toast.makeText(getApplicationContext(), "You must register your own beacon to use this feature!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Button button2 = (Button) findViewById(R.id.jump);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getLocationgps();
                LatLng latLng = new LatLng(lati, longi);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
            }
        });
        final Button button3 = (Button) findViewById(R.id.change);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*if(switchOn69 || switchOn || switchOn70){
                    Toast.makeText(getApplicationContext(), "Please turn off all switches!", Toast.LENGTH_SHORT).show();
                } else {*/
        //Intent myIntent = new Intent(MapsActivity.this, MainActivity.class);
        //MapsActivity.this.startActivity(myIntent);
        //}
    }
        /*});
        final Button button4 = (Button) findViewById(R.id.button69);
        button4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dd = 0;
                /*DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Notification Data").child("date&time"+mac).removeValue();
                mDatabase.child("Notification Data").child("predictedDistance"+mac).removeValue();
                mDatabase.child("Notification Data").child("latitude"+mac).removeValue();
                mDatabase.child("Notification Data").child("longitude"+mac).removeValue();*/
                /*TextView locationTv = (TextView) findViewById(R.id.e);
                locationTv.setText("No data from other users!");
                locationTv = (TextView) findViewById(R.id.f);
                locationTv.setText("No data from other users!");
                locationTv = (TextView) findViewById(R.id.g);
                locationTv.setText("No data from other users!");
                locationTv = (TextView) findViewById(R.id.h);
                locationTv.setText("No data from other users!");
                Toast.makeText(getApplicationContext(), "Data successfully cleared! (Enabling missing beacon will update values)", Toast.LENGTH_SHORT).show();
            }
        });
        final Button button5 = (Button) findViewById(R.id.jumpmissing);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(latiSaveDouble == -999999999 && longiSaveDouble == -999999999){
                    Toast.makeText(getApplicationContext(), "Your iBeacon has not been found by another user! (Values found if beacon set to missing mode)", Toast.LENGTH_SHORT).show();
                } else if(latiSaveDouble != -999999999 && longiSaveDouble != -999999999) {
                    LatLng latjng = new LatLng(latiSaveDouble, longiSaveDouble);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latjng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
                }
            }
        });
        final Button button6 = (Button) findViewById(R.id.findNumber);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MapsActivity.this, numberCheck.class);
                MapsActivity.this.startActivity(myIntent);
            }
        });
        final Button button7 = (Button) findViewById(R.id.changeNumber);
        button7.setOnClickListener(new View.OnClickListener() {
            String zx = "";
            public void onClick(View v) {
                DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference().child("beaconPhone").child(mac);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            name2 = ds.getKey();
                            zx = name2;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                ref0.addListenerForSingleValueEvent(eventListener);
                if(!zx.equals("")) {
                    Intent myIntent = new Intent(MapsActivity.this, changeNumber.class);
                    MapsActivity.this.startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "You must register your own beacon and sync a phone number to it to use this feature!", Toast.LENGTH_SHORT).show();
                    mySwitch.setChecked(false);
                }
            }
        });
        switchChildDetector();
        checkMissing();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mac.equals("")){
                    getState("missing");
                    getState("auto");
                    getState("search");
                }
            }
            },1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switches();
            }
        }, 3000);*/


//------------------------------------------------------------------------------------  \/ Get User Location

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,locationListener);
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
        String currentPosition="latitude is"+location.getLatitude()+", "+"longitude is"+location.getLongitude();
        precision = location.getAccuracy();
        System.out.println(currentPosition);
        lati = location.getLatitude();
        longi = location.getLongitude();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lat", String.valueOf(lati));
            jsonObject.put("long", String.valueOf(longi));
//            loginByGet(jsonObject.toString(),"http://143.89.144.200:12000/final/sensorData.php?info=");
        }catch (JSONException e){
        }
    }
//------------------------------------------------------------------------------------  \/ Update Data Textviews
    public void bluetoothDataLoop() {
        getLocationgps();
        System.out.println("Very beginning of dataLoop");
        final Handler handler1 = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(flag == 999999998){
                    flag = 50;
                }
                if(flag <= 999999999) {   //how to stop  20909.07/3
                    // Add a marker in Sydney and move the camera
                    updatedLocation.clear();
                    updatedDistance.clear();
                    Log.d(TAG, String.valueOf("flag = " + flag));
                    mMap.clear();
                    LatLng latLng = new LatLng(lati, longi);
                    if (beaconInRange){
                        updatedLocation.add(Arrays.asList(((lati - 53.178469) / 0.00001) * 0.12179047095976932582726898256213, (longi - 6.503091) / 0.000001 * 0.00728553580298947812081345114627));
                    updatedDistance.add(predictedDistance);
                    zValues.add(6371000*sin(Math.toRadians(lati)));
                    }
                    //LatLng latlng = new LatLng(latiSaveDouble, longiSaveDouble);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
                    if(precision > 10){
                        ImageView imgView = (ImageView)findViewById(R.id.attention);
                        imgView .setVisibility(View.VISIBLE);
                        TextView textview = (TextView)findViewById(R.id.precisionMessage);
                        textview.setVisibility(View.VISIBLE);
                    } else {
                        ImageView imgView = (ImageView)findViewById(R.id.attention);
                        imgView .setVisibility(View.GONE);
                        TextView textview = (TextView)findViewById(R.id.precisionMessage);
                        textview.setVisibility(View.GONE);
                    }
                    if(!mac.equals("") && beaconInRange) {
                        double metersPerPx = 156543.03392 * Math.cos(lati * Math.PI / 180) / Math.pow(2, 19);
                        double Radius = (predictedDistance / metersPerPx) - 0.5;
                        CircleOptions circleOptions = (new CircleOptions()
                                .center(new LatLng(lati, longi))   //set center
                                .radius(predictedDistance)   //set radius in meters
                                .fillColor(0x200000FF)  //default
                                .strokeColor(Color.BLUE)
                                .strokeWidth(5));
                        myCircle = mMap.addCircle(circleOptions);
                           /* mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lati, longi))
                                    .radius(radius)
                                    .strokeColor(Color.BLUE)
                                    .fillColor(Color.BLUE));*/
                    }
                    TextView locationTv = (TextView) findViewById(R.id.lati);
                    locationTv.setText(lati + "");
                    locationTv = (TextView) findViewById(R.id.longi);
                    locationTv.setText(longi + "");
                    if(!mac.equals("")) {
                        locationTv = (TextView) findViewById(R.id.address);
                        locationTv.setText(mac + "");
                    }
                    Log.d(TAG, String.valueOf("RSSI: -> " + RSSI));
                    handler1.postDelayed(this, 4000);
                    if(flag == 0) {
                        Log.d(TAG, String.valueOf("flag = " + flag));
                        RSSI = (rssiUnit) - RSSI;
                        hold = 10 * locationValue;
                        RSSI = RSSI / hold;
                        RSSI = Math.pow(10, RSSI);
                        if(mac.equals("")){
                            RSSI = 0.00000;
                        }
                        predictedDistance = RSSI;
                        roundDistance();
                        if(!mac.equals("")) {
                            locationTv = (TextView) findViewById(R.id.distance);
                            locationTv.setText("About " + predictedDistance + " Meters Away");
                        }
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
                        if(!mac.equals("") && beaconInRange) {
                            double metersPerPx = 156543.03392 * Math.cos(lati * Math.PI / 180) / Math.pow(2, 19);
                            double Radius = (predictedDistance / metersPerPx) - 0.5;
                            CircleOptions circleOptions = (new CircleOptions()
                                    .center(new LatLng(lati, longi))   //set center
                                    .radius(predictedDistance)   //set radius in meters
                                    .fillColor(0x200000FF)  //default
                                    .strokeColor(Color.BLUE)
                                    .strokeWidth(5));
                            myCircle = mMap.addCircle(circleOptions);
                           /* mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lati, longi))
                                    .radius(radius)
                                    .strokeColor(Color.BLUE)
                                    .fillColor(Color.BLUE));*/
                        }
                    }else if (indicator == 1){
                        Log.d(TAG, String.valueOf("indicator.69 = " + indicator));
                        if(!mac.equals("")) {
                            locationTv = (TextView) findViewById(R.id.distance);
                            locationTv.setText("About " + predictedDistance + " Meters Away");
                        }
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
                        indicator = 0;

                    } else if (flag <= 3){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                    flag = flag + 1;
                    kk = 0;
                    nn = 0;
                    mm = 0;
                    if(!latiLongiList.isEmpty() && ev == 1) {
                        while (kk == 0 && !latiLongiList.isEmpty() && !distanceAwayList.isEmpty()/* && dateList.get(0)!= null*/) {
                            LatLng devicePoint = new LatLng(Double.valueOf(latiLongiList.get(nn)), Double.valueOf(latiLongiList.get(nn + 1)));
                            savePredictedDistance10 = distanceAwayList.get(mm);
                            //dateTimeZone10 = dateList.get(mm);
                            int b = 0;
                            int c = 0;
                            while (!latiLongiList.isEmpty() && b+1 <= distanceAwayList.size()){
                                updatedLocation.add(Arrays.asList(((Double.valueOf(latiLongiList.get(c)) - 53.178469) / 0.00001) * 0.12179047095976932582726898256213, (Double.valueOf(latiLongiList.get(c+1)) - 6.503091) / 0.000001 * 0.00728553580298947812081345114627));
                                updatedDistance.add(Double.valueOf(distanceAwayList.get(b)));
                                b = b + 1;
                                c = c + 2;
                            }
                            zValues.add(6371000*sin(Double.valueOf(latiLongiList.get(nn))));
                            v = 0;
                            if (latiSaveDouble != -999999999 && longiSaveDouble != -999999999) {
                                //circleDisplayer();
                                if(distanceAwayList.size() >= 1){
                                    v=0;
                                    Log.d(TAG,"updateDistanceSize1 = " + updatedDistance.size());
                                    Log.d(TAG, "Info1: " + "updatedLocation = " + latiLongiList.get(0) + ", " + latiLongiList.get(1) + " updatedDistance = " + updatedDistance.get(v));
                                    LatLng devicePoint1010 = new LatLng(Double.valueOf(latiLongiList.get(0)), Double.valueOf(latiLongiList.get(1)));
                                    mMap.addMarker(new MarkerOptions().position(devicePoint1010).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(0) + ", " + latiLongiList.get(1)));
                                    CircleOptions circleOptions1010 = (new CircleOptions()
                                            .center(devicePoint1010)   //set center
                                            .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                            .fillColor(0x20FF0000)  //default
                                            .strokeColor(Color.RED)
                                            .strokeWidth(5));
                                    circle10 = mMap.addCircle(circleOptions1010);
                                    if(distanceAwayList.size() >= 2){
                                        v = 1;
                                        Log.d(TAG,"updateDistanceSize2 = " + updatedDistance.size());
                                        Log.d(TAG, "Info2: " + "updatedLocation = " + latiLongiList.get(2) + ", " + latiLongiList.get(3) + " updatedDistance = " + updatedDistance.get(v));
                                        LatLng devicePoint100 = new LatLng(Double.valueOf(latiLongiList.get(2)), Double.valueOf(latiLongiList.get(3)));
                                        mMap.addMarker(new MarkerOptions().position(devicePoint100).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(2) + ", " + latiLongiList.get(3)));
                                        CircleOptions circleOptions11 = (new CircleOptions()
                                                .center(devicePoint100)   //set center
                                                .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                .fillColor(0x20FF0000)  //default
                                                .strokeColor(Color.RED)
                                                .strokeWidth(5));
                                        circle11 = mMap.addCircle(circleOptions11);
                                        if(distanceAwayList.size() >= 3){
                                            v = 2;
                                            Log.d(TAG,"updateDistanceSize3 = " + updatedDistance.size());
                                            Log.d(TAG, "Info3: " + "updatedLocation = " + latiLongiList.get(4) + ", " + latiLongiList.get(5) + " updatedDistance = " + updatedDistance.get(v));
                                            LatLng devicePoint101 = new LatLng(Double.valueOf(latiLongiList.get(4)), Double.valueOf(latiLongiList.get(5)));
                                            mMap.addMarker(new MarkerOptions().position(devicePoint101).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(4) + ", " + latiLongiList.get(5)));
                                            CircleOptions circleOptions12 = (new CircleOptions()
                                                    .center(devicePoint101)   //set center
                                                    .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                    .fillColor(0x20FF0000)  //default
                                                    .strokeColor(Color.RED)
                                                    .strokeWidth(5));
                                            circle12 = mMap.addCircle(circleOptions12);
                                            if(distanceAwayList.size() >= 4){
                                                v = 3;
                                                Log.d(TAG,"updateDistanceSize4 = " + updatedDistance.size());
                                                Log.d(TAG, "Info4: " + "updatedLocation = " + latiLongiList.get(6) + ", " + latiLongiList.get(7) + " updatedDistance = " + updatedDistance.get(v));
                                                LatLng devicePoint102 = new LatLng(Double.valueOf(latiLongiList.get(6)), Double.valueOf(latiLongiList.get(7)));
                                                mMap.addMarker(new MarkerOptions().position(devicePoint102).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(6) + ", " + latiLongiList.get(7)));
                                                CircleOptions circleOptions13 = (new CircleOptions()
                                                        .center(devicePoint102)   //set center
                                                        .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                        .fillColor(0x20FF0000)  //default
                                                        .strokeColor(Color.RED)
                                                        .strokeWidth(5));
                                                circle13 = mMap.addCircle(circleOptions13);
                                                if(distanceAwayList.size() >= 5){
                                                    v = 4;
                                                    Log.d(TAG,"updateDistanceSize5 = " + updatedDistance.size());
                                                    Log.d(TAG, "Info5: " + "updatedLocation = " + latiLongiList.get(8) + ", " +latiLongiList.get(9) + " updatedDistance = " + updatedDistance.get(v));
                                                    LatLng devicePoint103 = new LatLng(Double.valueOf(latiLongiList.get(8)), Double.valueOf(latiLongiList.get(9)));
                                                    mMap.addMarker(new MarkerOptions().position(devicePoint103).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(8) + ", " + latiLongiList.get(9)));
                                                    CircleOptions circleOptions14 = (new CircleOptions()
                                                            .center(devicePoint103)   //set center
                                                            .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                            .fillColor(0x20FF0000)  //default
                                                            .strokeColor(Color.RED)
                                                            .strokeWidth(5));
                                                    circle14 = mMap.addCircle(circleOptions14);
                                                    if(distanceAwayList.size() >= 6){
                                                        v = 5;
                                                        Log.d(TAG,"updateDistanceSize6 = " + updatedDistance.size());
                                                        Log.d(TAG, "Info6: " + "updatedLocation = " + latiLongiList.get(10) + ", " + latiLongiList.get(11) + " updatedDistance = " + updatedDistance.get(v));
                                                        LatLng devicePoint104 = new LatLng(Double.valueOf(latiLongiList.get(10)), Double.valueOf(latiLongiList.get(11)));
                                                        mMap.addMarker(new MarkerOptions().position(devicePoint104).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(10) + ", " + latiLongiList.get(11)));
                                                        CircleOptions circleOptions15 = (new CircleOptions()
                                                                .center(devicePoint104)   //set center
                                                                .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                                .fillColor(0x20FF0000)  //default
                                                                .strokeColor(Color.RED)
                                                                .strokeWidth(5));
                                                        circle15 = mMap.addCircle(circleOptions15);
                                                        if(distanceAwayList.size() >= 7){
                                                            v = 6;
                                                            Log.d(TAG,"updateDistanceSize7 = " + updatedDistance.size());
                                                            Log.d(TAG, "Info7: " + "updatedLocation = " + latiLongiList.get(12) + ", " + latiLongiList.get(13) + " updatedDistance = " + updatedDistance.get(v));
                                                            LatLng devicePoint105 = new LatLng(Double.valueOf(latiLongiList.get(12)), Double.valueOf(latiLongiList.get(13)));
                                                            mMap.addMarker(new MarkerOptions().position(devicePoint105).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(12) + ", " + latiLongiList.get(13)));
                                                            CircleOptions circleOptions16 = (new CircleOptions()
                                                                    .center(devicePoint105)   //set center
                                                                    .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                                    .fillColor(0x20FF0000)  //default
                                                                    .strokeColor(Color.RED)
                                                                    .strokeWidth(5));
                                                            circle16 = mMap.addCircle(circleOptions16);
                                                            if(distanceAwayList.size() >= 8){
                                                                v = 7;
                                                                Log.d(TAG,"updateDistanceSize8 = " + updatedDistance.size());
                                                                Log.d(TAG, "Info8: " + "updatedLocation = " + latiLongiList.get(14) + ", " + latiLongiList.get(15) + " updatedDistance = " + updatedDistance.get(v));
                                                                LatLng devicePoint106 = new LatLng(Double.valueOf(latiLongiList.get(14)), Double.valueOf(latiLongiList.get(15)));
                                                                mMap.addMarker(new MarkerOptions().position(devicePoint106).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(14) + ", " + latiLongiList.get(15)));
                                                                CircleOptions circleOptions17 = (new CircleOptions()
                                                                        .center(devicePoint106)   //set center
                                                                        .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                                        .fillColor(0x20FF0000)  //default
                                                                        .strokeColor(Color.RED)
                                                                        .strokeWidth(5));
                                                                circle17 = mMap.addCircle(circleOptions17);
                                                                if(distanceAwayList.size() >= 9){
                                                                    v = 8;
                                                                    Log.d(TAG,"updateDistanceSize9 = " + updatedDistance.size());
                                                                    Log.d(TAG, "Info9: " + "updatedLocation = " + latiLongiList.get(16) + ", " + latiLongiList.get(17) + " updatedDistance = " + updatedDistance.get(v));
                                                                    LatLng devicePoint107 = new LatLng(Double.valueOf(latiLongiList.get(16)), Double.valueOf(latiLongiList.get(17)));
                                                                    mMap.addMarker(new MarkerOptions().position(devicePoint107).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(16) + ", " + latiLongiList.get(17)));
                                                                    CircleOptions circleOptions18 = (new CircleOptions()
                                                                            .center(devicePoint107)   //set center
                                                                            .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                                            .fillColor(0x20FF0000)  //default
                                                                            .strokeColor(Color.RED)
                                                                            .strokeWidth(5));
                                                                    circle18 = mMap.addCircle(circleOptions18);
                                                                    if(distanceAwayList.size() >= 10){
                                                                        v = 9;
                                                                        Log.d(TAG,"updateDistanceSize10 = " + updatedDistance.size());
                                                                        Log.d(TAG, "Info10: " + "updatedLocation = " + latiLongiList.get(18) + ", " + latiLongiList.get(19) + " updatedDistance = " + updatedDistance.get(v));
                                                                        LatLng devicePoint108 = new LatLng(Double.valueOf(latiLongiList.get(18)), Double.valueOf(latiLongiList.get(19)));
                                                                        mMap.addMarker(new MarkerOptions().position(devicePoint108).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(18) + ", " + latiLongiList.get(19)));
                                                                        CircleOptions circleOptions19 = (new CircleOptions()
                                                                                .center(devicePoint108)   //set center
                                                                                .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                                                .fillColor(0x20FF0000)  //default
                                                                                .strokeColor(Color.RED)
                                                                                .strokeWidth(5));
                                                                        circle19 = mMap.addCircle(circleOptions19);
                                                                        if(distanceAwayList.size() >= 11){
                                                                            v = 10;
                                                                            Log.d(TAG,"updateDistanceSize11 = " + updatedDistance.size());
                                                                            Log.d(TAG, "Info11: " + "updatedLocation = " + latiLongiList.get(20) + ", " + latiLongiList.get(21) + " updatedDistance = " + updatedDistance.get(v));
                                                                            LatLng devicePoint109 = new LatLng(Double.valueOf(latiLongiList.get(20)), Double.valueOf(latiLongiList.get(21)));
                                                                            mMap.addMarker(new MarkerOptions().position(devicePoint109).title("Your Beacon is " + distanceAwayList.get(v) + " Meters away from " + latiLongiList.get(20) + ", " + latiLongiList.get(21)));
                                                                            CircleOptions circleOptions20 = (new CircleOptions()
                                                                                    .center(devicePoint109)   //set center
                                                                                    .radius(Double.valueOf(distanceAwayList.get(v)))   //set radius in meters
                                                                                    .fillColor(0x20FF0000)  //default
                                                                                    .strokeColor(Color.RED)
                                                                                    .strokeWidth(5));
                                                                            circle20 = mMap.addCircle(circleOptions20);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if(nn+2 > latiLongiList.size()-1){
                                    kk = 1;
                                    tempArray = new double[updatedLocation.size()][updatedLocation.size()];
                                    int i = 0;
                                    while (i <= updatedLocation.size() - 1 && nn+2 > latiLongiList.size()-1) {
                                        tempArray[i][0] = updatedLocation.get(i).get(0);
                                        tempArray[i][1] = updatedLocation.get(i).get(1);
                                        Log.d(TAG, "convert#1-1: "+updatedLocation.get(i).get(0));
                                        Log.d(TAG, "convert#1-2: "+updatedLocation.get(i).get(1));
                                        Log.d(TAG, "convert#1-3: "+tempArray.length);
                                        i = i + 1;
                                    }
                                    tempArray2 = new double[updatedDistance.size()];
                                    int j = 0;
                                    while (j <= updatedDistance.size() - 1 && nn+2 > latiLongiList.size()-1) {
                                        tempArray2[j] = updatedDistance.get(j);
                                        Log.d(TAG, "convert#2-1: "+updatedDistance.get(j));
                                        Log.d(TAG, "convert#2-2: "+tempArray2.length);
                                        j = j + 1;
                                    }
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (tempArray.length > 1 && tempArray2.length > 1) {
                                                Log.d(TAG,"Trilateration Area");
                                                NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(tempArray, tempArray2), new LevenbergMarquardtOptimizer());
                                                LeastSquaresOptimizer.Optimum optimum = solver.solve();
// the answer
                                                double[] calculatedPosition = optimum.getPoint().toArray();
                                                LatLng latllng = new LatLng(calculatedPosition[0]/0.12179047095976932582726898256213 * 0.00001 + 53.178469, calculatedPosition[1] / 0.00728553580298947812081345114627 * 0.000001 + 6.503091);
                                                Log.d(TAG, "TrilaterationPosition (lat): "+calculatedPosition[0]/0.12179047095976932582726898256213 * 0.00001 + 53.178469);
                                                Log.d(TAG, "TrilaterationPosition (long): "+ calculatedPosition[1] / 0.00728553580298947812081345114627 * 0.000001 + 6.503091);
                                                double ten = (calculatedPosition[0]/0.12179047095976932582726898256213 * 0.00001 + 53.178469);
                                                double eleven = (calculatedPosition[1] / 0.00728553580298947812081345114627 * 0.000001 + 6.503091);
                                                mMap.addMarker(new MarkerOptions().position(latllng).title("Estimated location of your beacon is at " + ten + ", " + eleven));
// error and geometry information
                                                //RealVector standardDeviation = optimum.getSigma(0);
                                                //RealMatrix covarianceMatrix = optimum.getCovariances(0);
                                                /*String lol = String.valueOf(standardDeviation);
                                                double lolFinal = Double.valueOf(lol);
                                                CircleOptions circleOptions4 = (new CircleOptions()
                                                        .center(new LatLng(calculatedPosition[0], calculatedPosition[1]))   //set center
                                                        .radius(lolFinal)   //set radius in meters
                                                        .fillColor(0x20EE82EE)  //default
                                                        .strokeColor(Color.MAGENTA)
                                                        .strokeWidth(5));
                                                myCircle3 = mMap.addCircle(circleOptions4);*/
                                                tempArray = null;
                                                tempArray2 = null;
                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //deviceMacAddressList.clear();
                                                    }
                                                    },500);
                                            }

                                        }
                                    }, 500);

                                    break;
                                } else {
                                    nn = nn + 2;
                                    mm = mm + 1;
                                }
                            } else {
                                kk = 1;
                                tempArray = new double[updatedLocation.size()][updatedLocation.size()];
                                int i = 0;
                                while (i <= updatedLocation.size() - 1 && nn+2 > latiLongiList.size()-1) {
                                    tempArray[i][0] = updatedLocation.get(i).get(0);
                                    tempArray[i][1] = updatedLocation.get(i).get(1);
                                    Log.d(TAG, "convert#1-1: "+updatedLocation.get(i).get(0));
                                    Log.d(TAG, "convert#1-2: "+updatedLocation.get(i).get(1));
                                    Log.d(TAG, "convert#1-3: "+tempArray.length);
                                    i = i + 1;
                                }
                                tempArray2 = new double[updatedDistance.size()];
                                int j = 0;
                                while (j <= updatedDistance.size() - 1 && nn+2 > latiLongiList.size()-1) {
                                    tempArray2[j] = updatedDistance.get(j);
                                    Log.d(TAG, "convert#2-1: "+updatedDistance.get(j));
                                    Log.d(TAG, "convert#2-2: "+tempArray2.length);
                                    j = j + 1;
                                }
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (tempArray.length > 1 && tempArray2.length > 1) {
                                            Log.d(TAG,"Trilateration Area");
                                            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(tempArray, tempArray2), new LevenbergMarquardtOptimizer());
                                            LeastSquaresOptimizer.Optimum optimum = solver.solve();
// the answer
                                            double[] calculatedPosition = optimum.getPoint().toArray();
                                            LatLng latllng = new LatLng(calculatedPosition[0]/0.12179047095976932582726898256213 * 0.00001 + 53.178469, calculatedPosition[1] / 0.00728553580298947812081345114627 * 0.000001 + 6.503091);
                                            Log.d(TAG, "TrilaterationPosition (lat): "+calculatedPosition[0]/0.12179047095976932582726898256213 * 0.00001 + 53.178469);
                                            Log.d(TAG, "TrilaterationPosition (long): "+ calculatedPosition[1] / 0.00728553580298947812081345114627 * 0.000001 + 6.503091);
                                            double ten = (asin((6371000*sin(Math.toRadians(calculatedPosition[0])))/6371000));
                                            double eleven = atan2(Math.toRadians(calculatedPosition[0]),Math.toRadians(calculatedPosition[1]));
                                            mMap.addMarker(new MarkerOptions().position(latllng).title("Estimated location of your beacon is at " + ten + ", " + eleven));
// error and geometry information
                                            //RealVector standardDeviation = optimum.getSigma(0);
                                            //RealMatrix covarianceMatrix = optimum.getCovariances(0);
                                                /*String lol = String.valueOf(standardDeviation);
                                                double lolFinal = Double.valueOf(lol);
                                                CircleOptions circleOptions4 = (new CircleOptions()
                                                        .center(new LatLng(calculatedPosition[0], calculatedPosition[1]))   //set center
                                                        .radius(lolFinal)   //set radius in meters
                                                        .fillColor(0x20EE82EE)  //default
                                                        .strokeColor(Color.MAGENTA)
                                                        .strokeWidth(5));
                                                myCircle3 = mMap.addCircle(circleOptions4);*/
                                            tempArray = null;
                                            tempArray2 = null;
                                        }
                                    }
                                }, 1000);

                                break;

                            }


                                }

                    }
                    /*if(ev == 1) {
                        double[][] tempArray = new double[updatedLocation.size()][updatedLocation.size()];
                        int i = 0;
                        while (i <= updatedLocation.size() - 1) {
                            tempArray[i][i] = updatedLocation.get(i).get(i);
                            i = i + 1;
                        }
                        double[] tempArray2 = new double[updatedDistance.size()];
                        int j = 0;
                        while (i <= updatedDistance.size() - 1) {
                            tempArray2[j] = updatedDistance.get(j);
                            j = j + 1;
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (tempArray.length < 1 && tempArray2.length < 1) {
                                    displayMessage("here#2");
                                    NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(tempArray, tempArray2), new LevenbergMarquardtOptimizer());
                                    LeastSquaresOptimizer.Optimum optimum = solver.solve();
// the answer
                                    double[] calculatedPosition = optimum.getPoint().toArray();
                                    LatLng latllng = new LatLng(calculatedPosition[0], calculatedPosition[1]);
                                    mMap.addMarker(new MarkerOptions().position(latllng).title("Estimated location of your beacon is at " + calculatedPosition));
// error and geometry information
                                    RealVector standardDeviation = optimum.getSigma(0);
                                    RealMatrix covarianceMatrix = optimum.getCovariances(0);
                                    String lol = String.valueOf(standardDeviation);
                                    double lolFinal = Double.valueOf(lol);
                                    CircleOptions circleOptions4 = (new CircleOptions()
                                            .center(new LatLng(calculatedPosition[0], calculatedPosition[1]))   //set center
                                            .radius(lolFinal)   //set radius in meters
                                            .fillColor(0x20EE82EE)  //default
                                            .strokeColor(Color.MAGENTA)
                                            .strokeWidth(5));
                                    myCircle3 = mMap.addCircle(circleOptions4);
                                }
                            }
                        }, 1000);
                    }*/
                    if(anonymousFound == 1){
                        LatLng latllng = new LatLng(latiSave69, longiSave69);
                        mMap.addMarker(new MarkerOptions().position(latllng).title("An anonymous iBeacon is " + savePredictedDistance69 + " away from "+ latiSave10+ ", " + longiSave10));
                        double metersPerPx = 156543.03392 * Math.cos(latiSave69 * Math.PI / 180) / Math.pow(2, 19);
                        double Radius = (savePredictedDistance69 / metersPerPx) - 0.5;
                        CircleOptions circleOptions3 = (new CircleOptions()
                                .center(new LatLng(latiSave69, longiSave69))   //set center
                                .radius(savePredictedDistance69)   //set radius in meters
                                .fillColor(0x20008000)  //default
                                .strokeColor(Color.GREEN)
                                .strokeWidth(5));
                        myCircle3 = mMap.addCircle(circleOptions3);
                    }
                }
                Log.d(TAG,"What is the listMacAddressFlagToo? " + listMacAddressFlagToo);
                if(ccounter >= 5 && ev == 1) {
                    ccounter = 0;
                    final Handler handlerrr = new Handler();
                    handlerrr.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "ccounter = " + ccounter + " (in)");
                            listMacAddressFlagToo = 0;
                            latiLongiList.clear();
                            distanceAwayList.clear();
                            dateList.clear();
                            loopGrab();
                        }
                    }, 1000);
                } else {
                    ccounter = ccounter + 1;
                    Log.d(TAG, "ccounter = " + ccounter);
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
            }
        }


    };

    /*RSSI = Double.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
                        RSSI = -64 - RSSI;
                        hold = 10 * 4;
                        RSSI = RSSI / hold;
                        RSSI = Math.pow(10, RSSI);
                        predictedDistance = RSSI;
                        TextView locationTv = (TextView) findViewById(R.id.distance);
                        locationTv.setText(predictedDistance + " Meters");*/


    // ---------------------------------------------------------  \/ Distance Search Button

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");

            String action = intent.getAction();
            String temp = "";
            Log.d(TAG, "ACTION = " + action);
//            Log.e("ywq", action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(TAG, "ACTION = bluetooth device");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {    //显示已配对设备
                    Log.d(TAG, "device.getBondState == Bluetooth Device");
                    System.out.println("\n" + device.getName() + "==>" + device.getAddress() + "\n");
                } else if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    temp = temp + "\n" + device.getName() + "==>" + device.getAddress() + " " + intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    //mTextMessage2.setText("\n"+device.getName()+"==>"+device.getAddress()+"==> " + intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI ));
                    if (device.getAddress().equals(mac) && errorkiller == 1) {
                        errorkiller = 0;
                        blueflag = 5;
                        RSSI = Double.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
                        Log.d(TAG, String.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI))+ "<- WHat came out     What's in variable -> " + RSSI);
                        RSSI = (rssiUnit) - RSSI;
                        hold = 10*locationValue;
                        RSSI = RSSI / hold;
                        RSSI = Math.pow(10,RSSI);
                        predictedDistance = RSSI;
                        if(mac.equals("")){
                            RSSI = 0.000;
                        }
                        roundDistance();
                        TextView locationTv = (TextView) findViewById(R.id.distance);
                        locationTv.setText("About "+ predictedDistance + " Meters Away");
                        flag = 1;
                        indicator = 1;
                        Log.d(TAG, String.valueOf("flag set to " + flag + " indicator set to " + indicator));
                        Toast.makeText(getApplicationContext(), "Distance from iBeacon refreshed!!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Address found");
                        getLocationgps();
                        try {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("mac", device.getAddress());
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
    public void BlueToothDiscovery() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.d(TAG, "Device does not support Bluetooth");
            Toast.makeText(getApplicationContext(), "Your device does not support bluetooth!", Toast.LENGTH_LONG).show();
            blueflag = 5;
        } else {
            Log.d(TAG, "Bluetooth Supported");
            errorkiller = 1;
            Log.d(TAG, String.valueOf("errorkiller = " + errorkiller));
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
    public void BluetoothLoop() {
        System.out.println("Very beginning of BluetoothLoop");
        final Handler handler1 = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(blueflag == 4) {
                    Toast.makeText(getApplicationContext(), "Could not find the device! Make sure bluetooth and the device is turned on and that you device can detect it.", Toast.LENGTH_SHORT).show();
                    blueflag = 5;
                    TextView locationTv = (TextView) findViewById(R.id.distance);
                    locationTv.setText("Unknown/Beacon not in Range");
                    flag = 1;
                    indicator = 1;
                }
                if(blueflag <= 3) {
                    System.out.println("public void run()");
                    Toast.makeText(getApplicationContext(), "Refreshing... Please do NOT press button again!", Toast.LENGTH_LONG).show();
                    BlueToothDiscovery();
                    System.out.println("BlueToothDiscovery again");
                    blueflag = blueflag + 1;
                    System.out.println("flag = " + blueflag);
                }
                handler1.postDelayed(this, 5000);
            }
        };
        handle.postDelayed(runnable, 100);
    }

    public Handler handle = new Handler() {

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
    double roundDistance()
    {
        DecimalFormat twoDForm = new DecimalFormat("##.##");
        predictedDistance = Double.valueOf(twoDForm.format(predictedDistance));
        return Double.valueOf(twoDForm.format(predictedDistance));
    }
    double roundDistance2()
    {
        DecimalFormat twoDForm = new DecimalFormat("##.##");
        savePredictedDistance69 = Double.valueOf(twoDForm.format(savePredictedDistance69));
        return Double.valueOf(twoDForm.format(savePredictedDistance69));
    }

    //----------------------------------------------------------------------------   \/ AutoSearch

    private BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            autoCounter = autoCounter + 1;
            System.out.println("autoCounter" + autoCounter);
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
                    if (device.getAddress().equals(mac) && errorkiller2 == 1) {
                        getLocationgps();
                        autoCounter = 0;
                        errorkiller2 = 0;
                        RSSI = Double.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
                        displayMessage("RSSI = " + RSSI);
                        Log.d(TAG, String.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI))+ "<- WHat came out     What's in variable -> " + RSSI);
                        RSSI = (rssiUnit) - RSSI;
                        hold = 10*locationValue;
                        RSSI = RSSI / hold;
                        RSSI = Math.pow(10,RSSI);
                        predictedDistance = RSSI;
                        roundDistance();
                        TextView locationTv = (TextView) findViewById(R.id.distance);
                        locationTv.setText("About "+ predictedDistance + " Meters Away");
                        flag = 1;
                        indicator = 1;
                        Log.d(TAG, String.valueOf("flag set to " + flag + " indicator set to " + indicator));
                        if(!beaconInRange) {
                            latiSave = lati;
                            longiSave = longi;
                            //Toast.makeText(getApplicationContext(), "iBeacon detected!!", Toast.LENGTH_SHORT).show();
                            beaconInRange = true;
                            savePredictedDistance = predictedDistance;
                            notificationFound();
                        }
                        autoCounter = 0;
                        Log.d(TAG, "Address found");
                        try {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("mac", device.getAddress());
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
    public void BlueToothDiscoveryy() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.d(TAG, "Device does not support Bluetooth");
            Toast.makeText(getApplicationContext(), "Your device does not support bluetooth!", Toast.LENGTH_LONG).show();
            autoflag = false;
        } else {
            Log.d(TAG, "Bluetooth Supported");
            errorkiller2 = 1;
            Log.d(TAG, String.valueOf("errorkiller2 = " + errorkiller2));
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
    public void autoSearch() {
        System.out.println("Very beginning of BluetoothLoop");
        final Handler handler1 = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (switchOn && autoCounter <= autoThreshHold - 1 && autoSwitch.equals("on")) {
                    System.out.println("public void run()");
                    BlueToothDiscoveryy();
                    handler1.postDelayed(this, 5000);
                    System.out.println("BlueToothDiscovery again");
                }
                if(switchOn && autoCounter >= autoThreshHold){
                    if(beaconInRange = true) {
                        //Toast.makeText(getApplicationContext(), "No beacons were detected, retrying...", Toast.LENGTH_SHORT).show();
                    }
                    TextView locationTv = (TextView) findViewById(R.id.distance);
                    locationTv.setText("Unknown/Beacon not in Range");
                    autoCounter = 0;
                    beaconInRange = false;
                    latiSave = lati;
                    longiSave = longi;
                    savePredictedDistance = predictedDistance;
                    notificationLost();
                    autoSearch();
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

    //------------------------------------------------------------   \/ Notifications when beacon detected by OWN device.

    public void notificationFound(){
        String subject="TrackBeacon-Beacon Found!";
        String body= "iBeacon was detected at MyLocation: " + latiSave + ", " + longiSave+ ", Est. Distance: " + savePredictedDistance;
        int mNotificationId = 0;
        String latSave = String.valueOf(latiSave);
        String longSave = String.valueOf(longiSave);
        String savedPredictedDistance = String.valueOf(savePredictedDistance);
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.look)
                .setContentTitle(subject)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentText(body)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
        d = "Reappeared";

        Calendar cd = Calendar.getInstance();
        System.out.println("Current time => "+cd.getTime());
        SimpleDateFormat dd = new SimpleDateFormat("HH:mm");
        String saveTime = dd.format(cd.getTime());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
        String saveDate  = df.format(c);
        deviceTimeZone = TimeZone.getDefault().getDisplayName();
        editor = settinggs.edit();
        editor.putString("latiSave", latSave);
        editor.putString("saveTime", saveTime);
        editor.putString("saveDate", saveDate);
        editor.putString("longiSave", longSave);
        editor.putString("savedPredictedDistance", savedPredictedDistance);
        editor.putString("deviceTimeZone",deviceTimeZone);
        editor.putString("mac",mac);
        editor.putString("d", d);
        editor.commit();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, data.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mBuilder.setAutoCancel(true);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
    public void notificationLost(){
        int mNotificationId = 0;
        //Intent intent = new Intent(this, notReciever.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
        String subjject="TrackBeacon-Beacon Lost!";
        String bodyy= "iBeacon's last sighting was at MyLocation: " + latiSave + ", " + longiSave+", Est. Distance: " + savePredictedDistance;

        String latSave = String.valueOf(latiSave);
        String longSave = String.valueOf(longiSave);
        String savedPredictedDistance = String.valueOf(savePredictedDistance);
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.look)
                .setContentTitle(subjject)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bodyy))
                .setContentText(bodyy)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
        d = "Disappeared";

        Calendar cd = Calendar.getInstance();
        System.out.println("Current time => "+cd.getTime());
        SimpleDateFormat dd = new SimpleDateFormat("HH:mm");
        String saveTime = dd.format(cd.getTime());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
        String saveDate  = df.format(c);

        deviceTimeZone = TimeZone.getDefault().getDisplayName();

        editor = settinggs.edit();
        editor.putString("latiSave", latSave);
        editor.putString("saveTime", saveTime);
        editor.putString("saveDate", saveDate);
        editor.putString("longiSave", longSave);
        editor.putString("savedPredictedDistance", savedPredictedDistance);
        editor.putString("deviceTimeZone",deviceTimeZone);
        editor.putString("mac",mac);
        editor.putString("d", d);
        editor.commit();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, data.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        //mBuilder.setDeleteIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    //------------------------------------------------------  \/ Notification when beacon detected by other devices

    /*public void notificationOther(){
        getData();
        if(savePredictedDistance10 == null && dateTimeZone10 == null && latiSave10 == null && longiSave10 == null){
            pp = -1;
        }
        if(savePredictedDistance10 != null && dateTimeZone10 != null && latiSave10 != null && longiSave10 != null && !beginning) {
            if (pp == -1) {
                pp = -2;
                dd = 1;
                String subject = "TrackBeacon-Beacon Found by another device!";
                String body = "iBeacon was detected at the user's Location: " + latiSave10 + ", " + longiSave10 + ", Estimated Distance away from user: " + savePredictedDistance10 + ", Date and Time detected: " + dateTimeZone10;
                int mNotificationId = 0;
                NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.look)
                        .setContentTitle(subject)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                        .setContentText(body)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true);
                d = "Reappeared";
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        }
        if(beginning){
            beginning = false;
        }
            if(switchOn70){
                final Handler handdler = new Handler();
                handdler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationOther();
                    }
                },1500);
            }
    }*/

    //------------------------------------------------------------   \/ Save beacon MAC Address to Firebase Database

    public void saveMac(String beaconID){
        Log.d(TAG, "checkRssiDatabase reached");
        DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference().child("beaconPhone").child(mac);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    name2 = ds.getKey();
                    Log.d(TAG, "name2 = " + name2);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("MacIDs").child(beaconID).child(name2).setValue("");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref0.addListenerForSingleValueEvent(eventListener);
    }

    //------------------------------------------------------------  \/ Use of server to save states of switches

    public void getState(String AutoOrMissingOrSearch){
        if(AutoOrMissingOrSearch.equals("auto")){
            DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mRSSI3 = mDatabase1.child("Switches").child("autoSwitch"+mac);
            mRSSI3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (autoSwitchFlag) {
                        autoSwitch = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "169autoSwitch = " + autoSwitch);
                    } else {
                        mDatabase1.child("Switches").child("autoSwitch" + mac).setValue("off");
                        autoSwitch = "off";
                        autoSwitchFlag = true;
                        Log.d(TAG, "369autoSwitch = " + autoSwitch);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG,"Failed to get autoSwitch state.");
                }
            });
        } else if(AutoOrMissingOrSearch.equals("missing")){
            DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mRSSI1 = mDatabase2.child("Switches").child("missingSwitch"+mac);
            mRSSI1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (missingSwitchFlag) {
                        missingSwitch = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "269missingSwitch = " + missingSwitch);
                    } else {
                        mDatabase2.child("Switches").child("missingSwitch" + mac).setValue("off");
                        missingSwitch = "off";
                        missingSwitchFlag = true;
                        Log.d(TAG, "469missingSwitch = " + missingSwitch);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Failed to get missingSwitch state.");
                }
            });
        } else if(AutoOrMissingOrSearch.equals("search")){
            DatabaseReference mDatabase5 = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mRSSI5 = mDatabase5.child("Switches").child("missingSearch"+mac);
            mRSSI5.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (missingSearchFlag) {
                        missingSearch = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "10069missingSearch = " + missingSearch);
                    } else {
                        mDatabase5.child("Switches").child("missingSearch" + mac).setValue("off");
                        missingSearch = "off";
                        missingSearchFlag = true;
                        Log.d(TAG, "20069missingSearch = " + missingSearch);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Failed to get missingSwitch state.");
                }
            });
        }
    }

    //----------------------------------------------------------  \/ Switches listener

   /*public void switches(){
        mySwitch = (Switch) findViewById(R.id.autoSearch);
        mySwitch3 = (Switch) findViewById(R.id.missingSearch);
        Log.d(TAG, "#1-1234567890 " + autoSwitch);
        if(autoSwitch.equals("on") && !mac.equals("")){
            mySwitch.setChecked(true);
            switchOn = true;
            //check = true;
            autoflag = true;
            autoCounter = 0;
            autoSearch();
            Log.d(TAG, "switch autoset to ON");
        }else if(autoSwitch.equals("off") && !mac.equals("")){
            mySwitch.setChecked(false);
            Log.d(TAG, "switch autoset to OFF");
            switchOn = false;
            autoflag = false;
            autoCounter = 0;
        } else {
            mySwitch.setChecked(false);
        }
        //mySwitch.setChecked(false);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked && !mac.equals("")){
                    Toast.makeText(getApplicationContext(), "autoSearch is now ON!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "autoSearch was just turned ON");
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Switches").child("autoSwitch" + mac).setValue("on");
                    switchOn = true;
                    //check = true;
                    autoflag = true;
                    autoCounter = 0;
                    errorkiller2 = 0;
                    autoCounter = 0;
                    autoSearch();
                }else if(!isChecked && !mac.equals("")){
                    Toast.makeText(getApplicationContext(), "autoSearch is now OFF!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "autoSearch was just turned OFF");
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Switches").child("autoSwitch" + mac).setValue("off");
                    switchOn = false;
                    autoflag = false;
                    autoCounter = 0;
                } else {
                    Toast.makeText(getApplicationContext(), "You must register your own beacon to use this feature!", Toast.LENGTH_SHORT).show();
                    mySwitch.setChecked(false);
                }

            }
        });

       /* if(mySwitch.isChecked()){
            Log.d(TAG, "Switch is currently ON");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Switches").child("autoSwitch" + mac).setValue("on");
            switchOn = true;
            //check = true;
            autoflag = true;
            autoCounter = 0;
            errorkiller2 = 0;
            autoCounter = 0;
        }else{
            Log.d(TAG, "Switch is currently OFF");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Switches").child("autoSwitch" + mac).setValue("off");
            switchOn = false;
            autoflag = false;
            autoCounter = 0;
        }*/
        /*mySwitch2 = (Switch) findViewById(R.id.setMissing);
        Log.d(TAG, "#2-1234567890 "+missingSwitch);
        if(missingSwitch.equals("on")&& !mac.equals("")){
            switchOn70 = true;
            Log.d(TAG, "Missing switch autoset to ON");
            mySwitch2.setChecked(true);
            saveMac(mac);
            checkMissing();
            final Handler hqqandler = new Handler();
            hqqandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getData();
                    final Handler handddler = new Handler();
                    handddler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notificationOther();
                        }
                    },1000);
                }
            },1000);
        }else if(missingSwitch.equals("off") && !mac.equals("")){
            switchOn70 = false;
            Log.d(TAG, "Missing switch autoset to OFF");
            mySwitch2.setChecked(false);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Switches").child("missingSwitch" + mac).setValue("off");
            mDatabase.child("MacIDs").child(mac).removeValue();
        } else {
            mySwitch2.setChecked(false);
        }
        mySwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked && !mac.equals("")){
                    switchOn70 = true;
                    Toast.makeText(getApplicationContext(), "Missing mode is now ON!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Missing was just turned ON");
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Switches").child("missingSwitch" + mac).setValue("on");
                    saveMac(mac);
                    checkMissing();
                    final Handler hqandler = new Handler();
                    hqandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData();
                            final Handler handddler = new Handler();
                            handddler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    notificationOther();
                                }
                            },1000);
                        }
                    },1000);
                }else if(!isChecked && !mac.equals("")){
                    switchOn70 = false;
                    Toast.makeText(getApplicationContext(), "Missing mode is now OFF!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Missing was just turned OFF");
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Switches").child("missingSwitch" + mac).setValue("off");
                    mDatabase.child("MacIDs").child(mac).removeValue();
                } else {
                    Toast.makeText(getApplicationContext(), "You must register your own beacon to use this feature!", Toast.LENGTH_SHORT).show();
                    mySwitch2.setChecked(false);
                }

            }
        });


        Log.d(TAG, "#3-1234567890 " + missingSearch);
        if(missingSearch.equals("on")){
            Log.d(TAG, "Missing search autoset to ON");
            mySwitch3.setChecked(true);
            switchOn69 = true;
            //check = true;
            autoflag69 = true;
            autoCounter69 = 0;
            missingSearch = "on";
            //missingSearchLoop();
            //notificationOther();
        }else if(missingSearch.equals("off")){
            Log.d(TAG, "Missing search autoset to OFF");
            mySwitch3.setChecked(false);
            switchOn69 = false;
            autoflag69 = false;
            autoCounter69 = 0;
            errorkiller69 = 0;
            missingSearch = "off";
        }
        mySwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    Toast.makeText(getApplicationContext(), "Missing Search is now ON!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "MissingSearch was just turned ON");
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Switches").child("missingSearch" + mac).setValue("on");
                    switchOn69 = true;
                    //check = true;
                    autoflag69 = true;
                    autoCounter69 = 0;
                    errorkiller69 = 0;
                    missingSearch = "on";
                    missingSearchLoop();
                }else{
                    Toast.makeText(getApplicationContext(), "Missing Search is now OFF!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "MissingSearch was just turned OFF");
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Switches").child("missingSearch" + mac).setValue("off");
                    switchOn69 = false;
                    missingSearch = "off";
                    autoflag69 = false;
                    autoCounter69 = 0;
                    errorkiller69 = 0;
                    Log.d(TAG, "2.0jj switchOn69 (@switch) = " + switchOn69);
                }

            }
        });
        /*if(mySwitch3.isChecked()){
            Log.d(TAG, "missingSearch is currently ON");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Switches").child("missingSearch" + mac).setValue("on");
            switchOn69 = true;
            //check = true;
            autoflag69 = true;
            autoCounter69 = 0;
            //missingSearchLoop();
        }else{
            Log.d(TAG, "missingSearch is currently OFF");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Switches").child("missingSearch" + mac).setValue("off");
            switchOn69 = false;
            autoflag69 = false;
            autoCounter69 = 0;
            errorkiller69 = 0;
        }*/
    //}

    //----------------------------------------------------------  \/ More use of server to save state of switch.

    public void switchChildDetector(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Switches");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("missingSwitch" + mac)){
                    missingSwitchFlag = true;
                }
                if (dataSnapshot.hasChild("autoSwitch" + mac)) {
                    autoSwitchFlag = true;
                }
                if (dataSnapshot.hasChild("missingSearch" + mac)) {
                    missingSearchFlag = true;
                } else if (!dataSnapshot.hasChild("autoSwitch" + mac)){
                    autoSwitchFlag = false;
                } else if (!dataSnapshot.hasChild("missingSwitch" + mac)){
                    missingSwitchFlag = false;
                } else if (!dataSnapshot.hasChild("missingSearch" + mac)) {
                    missingSearchFlag = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to get missingSwitch state.");
            }
        });
    }

    //----------------------------------------------------------------------  \/ Missing Search

    public void BblueToothDiscovery() {
        if(switchOn69) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                Log.d(TAG, "Device does not support Bluetooth");
                Toast.makeText(getApplicationContext(), "Your device does not support bluetooth!", Toast.LENGTH_LONG).show();
                flag = 5;
            } else if(switchOn69) {
                Log.d(TAG, "Bluetooth Supported");
                errorkiller69 = 1;
                // 不做提示，强行打开
                // mBluetoothAdapter.disable();
                // mBluetoothAdapter.enable();
                IntentFilter filter69 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mmmReceiver, filter69);
                IntentFilter filter70 = new IntentFilter(mBluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                registerReceiver(mmmReceiver, filter70);
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    Log.d(TAG, " if (mBluetoothAdapter.isDiscovering())");
                }
                mBluetoothAdapter.startDiscovery();
                Log.d(TAG, " NO IF mBluetoothAdapter.isDiscovering()");
            }
        }

    }
    private BroadcastReceiver mmmReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive (Context context, Intent intent){   ////
                Log.d(TAG, "2.0jj switchOn69 = " + switchOn69);
                if(switchOn69){
            autoCounter69 = autoCounter69 + 1;
            if (autoCounter69 >= missingSearchThreshHold) {
                autoCounter69 = missingSearchThreshHold;
            }
            Log.d(TAG, "2.0jj 10autoCounter 69 = " + autoCounter69);
            Log.d(TAG, "onReceive");
            String action = intent.getAction();
            String temp = "";
            Log.d(TAG, "ACTION = " + action);

//            Log.e("ywq", action);
            final Handler handler100000 = new Handler();
            handler100000.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (macAddressList.size() == 0) {    ////
                        Log.d(TAG, "nullCounter = " + nullCounter);
                        autoCounter69 = 0;
                        macAddressList.clear();
                        checkMissing();
                        if (nullCounter >= 6) {
                            nullCounter = 0;
                           // Toast.makeText(getApplicationContext(), "No missing beacons. Finding again...", Toast.LENGTH_SHORT).show();
                        }
                        nullCounter = nullCounter + 1;
                    }
                    }
                    }, 700);


            if (BluetoothDevice.ACTION_FOUND.equals(action) && switchOn69 && macAddressList.size() != 0) {
                Log.d(TAG, "ACTION = bluetooth device");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {    //显示已配对设备
                    Log.d(TAG, "device.getBondState == Bluetooth Device");
                    System.out.println("\n" + device.getName() + "==>" + device.getAddress() + "\n");
                } else if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    temp = temp + "\n" + device.getName() + "==>" + device.getAddress() + " " + intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    //mTextMessage2.setText("\n"+device.getName()+"==>"+device.getAddress()+"==> " + intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI ));
                    while (zzz + 1 <= macAddressList.size() && macAddressList.size() != 0 && looploop < 4){
                        Log.d(TAG, "zzz = " + zzz);
                        Log.d(TAG, "zzzmacAddressList.size = " + macAddressList.size());
                        Log.d(TAG, "zzzmacAddressList.get(" + zzz +") = " + macAddressList.get(zzz));
                        Log.d(TAG, "zzzLooploop = " + looploop);
                        Log.d(TAG, "zzzDevice.getAddress = " + device.getAddress());
                    if (device.getAddress().equals(macAddressList.get(zzz)) && errorkiller69 == 1) {
                        getLocationgps();
                        autoCounter69 = 0;
                        errorkiller69 = 0;
                        nullCounter = 0;
                        macSave = macAddressList.get(zzz);
                        RSSI = Double.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI));
                        displayMessage("RSSI = " + RSSI);
                        Log.d(TAG, String.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI)) + "<- WHat came out     What's in variable -> " + RSSI);
                        RSSI = (rssiUnit) - RSSI;
                        hold = 10 * locationValue;
                        RSSI = RSSI / hold;
                        RSSI = Math.pow(10, RSSI);
                        savePredictedDistance69 = RSSI;
                        roundDistance2();
                        latiSave69 = lati;
                        longiSave69 = longi;
                        //Toast.makeText(getApplicationContext(), "BEACON " + macAddressList.get(size) + " found! Data saved!", Toast.LENGTH_SHORT).show();
                        //savePredictedDistance69 = predictedDistance1234567890;
                        autoCounter69 = 0;
                        Log.d(TAG, "Address found");
                        Calendar cc = Calendar.getInstance();
                        System.out.println("Current time => " + cc.getTime());        //Get time
                        SimpleDateFormat ee = new SimpleDateFormat("HH:mm");
                        notTime = ee.format(cc.getTime());
                        Date cj = Calendar.getInstance().getTime();
                        SimpleDateFormat dc = new SimpleDateFormat("MMM/dd/yyyy");      //Get date
                        notDate = dc.format(cj);
                        timeZone = TimeZone.getDefault().getDisplayName();
                        notDataDetector();
                        anonymousFound = 1;
                        try {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("mac", device.getAddress());
                            jsonObject1.put("predictedDistance", predictedDistance);
//                            loginByGet(jsonObject1.toString(),"http://143.89.144.200:12000/final/rawData.php?info=");
                            System.out.println(jsonObject1.toString());
                            //Toast.makeText(getApplicationContext(), "MAC = " + device.getAddress() + " RSSI = " + String.valueOf(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI) + " Approximate Distance Away from Beacon = " + predictedDistance), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                        }
                    }
                    if(looploop == 3) {
                        looploop = 0;
                        zzz = 0;
                        Log.d(TAG, "zzzLoopLoopIs3 = " + zzz);
                        final Handler handler1000 = new Handler();
                        handler1000.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                macAddressList.clear();
                                checkMissing();
                            }
                        }, 700);
                        /*Handler handler10000 = new Handler();
                        handler10000.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 1400);*/
                        break;
                    }
                    if(zzz + 1 == macAddressList.size()){
                        zzz = 0;
                        Log.d(TAG, "zzz0 = " + zzz);
                        looploop = looploop + 1;
                    } else {
                        zzz = zzz + 1;
                        Log.d(TAG, "zzzend = " + zzz);
                    }
                        // loginByGet("device-name:" + device.getName() +",targer-mac:"  + device.getAddress() + ",RSSI:" +  intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI )+ ",sensor-mac:" +  getLocalMacAddressFromWifiInfo(context) + ",latitude:" + x + ",longitude:" + y + ",time:" + System.currentTimeMillis()/1000 );
                    }
                } else {
//                Message message = new Message();
//                Bundle bundle = new Bundle();
//                bundle.clear();
//                message.what = 0x01;
//                bundle.putShort("msg", (short) 0);
//                message.setData(bundle);
//                handler.sendMessage(message);
                    Log.d(TAG, "Search Completed...");
                }

            }

            }
        }
    };

    public void missingSearchLoop() {
        Log.d(TAG, "2.0jj switchOn69 = " + switchOn69);
        if(switchOn69) {
            System.out.println("Very beginning of BluetoothLoop");
            final Handler handler70 = new Handler();
            run_able = new Runnable() {
                @Override
                public void run() {
                    if (switchOn69 && missingSearch.equals("on") && autoCounter69 <= missingSearchThreshHold - 1) {

                        System.out.println("public void run()");
                        Log.d(TAG, "2autoCounter 69 = " + autoCounter69);
                        errorkiller69 = 0;
                        if(switchOn69) {
                            BblueToothDiscovery();
                            handler70.postDelayed(this, 5000);
                        }
                        System.out.println("BlueToothDiscovery again");
                    }
                    if (switchOn69 && missingSearch.equals("on") && autoCounter69 == missingSearchThreshHold - 1) {
                        //Toast.makeText(getApplicationContext(), "BEACON " + macAddressList.get(size) + " not found!  Moving on...", Toast.LENGTH_SHORT).show();
                        //Move onto the next beacon
                        macAddressList.clear();
                        checkMissing();
                        getState("search");
                        errorkiller69 = 0;
                        Log.d(TAG, "1.0jjautoCounter 69 = " + autoCounter69);
                        autoCounter69 = autoCounter69 + 1;
                        //Log.d(TAG, "1.0jjmacAddressList size = " + length);
                        //Log.d(TAG, "1.0jjmacAddressList size variable value = " + size);
                        //autoCounter69 = 0;
                        //handler69.postDelayed(this, 4000);
                        errorkiller69 = 0;
                        nullCounter = 0;
                        //missingSearchLoop();
                    /*if(size + 1 == macAddressList.size()){
                        size = 0;
                    } else {
                        size = size + 1;
                    }
                    autoCounter69 = 0;*/
                        // missingSearchLoop();
                    }
                    if (switchOn69 && missingSearch.equals("on") && autoCounter69 == missingSearchThreshHold) {
                        Log.d(TAG, "autoCounter69 = 6");
                        //length = macAddressList.size();
                        Log.d(TAG, "2.0jjmacAddressList size = " + length);
                        Log.d(TAG, "2.0jjmacAddressList size variable value = " + size);
                        /*if (length == size + 1) {
                            size = 0;
                        } else {
                            size = size + 1;
                        }*/
                        autoCounter69 = 0;
                        errorkiller69 = 0;
                        nullCounter = 0;
                    }
                }
            };
            hhhandler.postDelayed(run_able, 100);
        }
    }

    public Handler hhhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            short now = bundle.getShort("msg");
            if (msg.what == 0x01) {
                System.out.println("new discovery");
                BblueToothDiscovery();
            }
        }


    };

    //------------------------------------------------    \/ Updates list of missing beacon MAC Addresses

    public void checkMissing(){
        Log.d(TAG, "checkMissing");
        a0 = -1;
        DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference().child("MacIDs");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    name = ds.getKey();
                    if(!name.equals(mac) && !name.equals("PlaceHolder")){       //Changed for testing
                        a0 = a0 + 1;
                        macAddressList.add(name);
                        Log.d(TAG, "3.0name = "+name);
                        Log.d(TAG, "3.0macAddressList = "+macAddressList.get(a0));
                        Log.d(TAG, "3.0 " + macAddressList.size());
                    }
                }
                Log.d(TAG, "3.2.0 " + macAddressList.size());
                length = macAddressList.size();
                if(length <= 6){
                    missingSearchThreshHold = 20;
                }if(length <= 15 && length > 6){
                    missingSearchThreshHold = 16;
                }if(length > 15 && length <= 20){
                    missingSearchThreshHold = 13;
                }if(length > 20 && length <= 30){
                    missingSearchThreshHold = 10;
                }if(length > 30){
                    missingSearchThreshHold = 8;
                }
                Log.d(TAG, "3.0MissingSearchThreshHold = "+missingSearchThreshHold);
                /*if(size + 1 > macAddressList.size()) {
                    size = 0;
                }*/
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref0.addListenerForSingleValueEvent(eventListener);
    }

    //------------------------------------------------------  \/ Uploads discovered data onto the server

    public void notDataDetector(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Notification Data");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deviceMacAddress = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                // if (!dataSnapshot.hasChild("date&time" + macAddressList.get(size))){
                    rootRef.child(macSave).child(deviceMacAddress).child("date&time" + macSave).setValue(String.valueOf(notDate) + ", " + String.valueOf(notTime) + " " + String.valueOf(timeZone));
                //}
               // if (!dataSnapshot.hasChild("predictedDistance" + macAddressList.get(size))) {
                rootRef.child(macSave).child(deviceMacAddress).child("predictedDistance" + macSave).setValue(String.valueOf(savePredictedDistance69) + " Meters");
                //}
                //if (!dataSnapshot.hasChild("location" + mac)) {
                    rootRef.child(macSave).child(deviceMacAddress).child("latitude" + macSave).setValue(String.valueOf(latiSave69));
                rootRef.child(macSave).child(deviceMacAddress).child("longitude" + macSave).setValue(String.valueOf(longiSave69));
                rootRef.child(macSave).child(deviceMacAddress).child("raw" + macSave).setValue(String.valueOf(savePredictedDistance69));

                //}
                //if (!dataSnapshot.hasChild("macAddress" + mac)){
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to get missingSwitch state.");
            }
        });
    }

    //---------------------------------------------  \/ Get info from server about iBeacon location

    public void getData(){
            //listMacAddressFlagToo = 0;
        Log.d(TAG,"getData();");
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Notification Data");
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // if (!dataSnapshot.hasChild("date&time" + macAddressList.get(size))){
                    if (dataSnapshot.hasChild(mac)) {
                        DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference().child("Notification Data").child(mac);
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                deviceMacAddressList.clear();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    deviceMacAddressList.add(ds.getKey());
                                    Log.d(TAG, "ds.getKey():" + ds.getKey());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        ref0.addListenerForSingleValueEvent(eventListener);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Failed to save phone number.", Toast.LENGTH_SHORT).show();
                }
            });
        /*final Handler hqqandler = new Handler();
        hqqandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(flagTree >= 10){
                    latiLongiList.clear();
                    distanceAwayList.clear();
                    dateList.clear();
                }
                flagTree = flagTree + 1;
            }
        }, 1500);*/
        if (ev == 1) {
            final Handler hqandler = new Handler();
            hqandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getData();
                }
            }, 6000);
        }
    }
    public void loopGrab(){
        if(listMacAddressFlagToo == 0 && !deviceMacAddressList.isEmpty() || override) {
            listMacAddressFlagToo = 1;
            Log.d(TAG, "Allowed to loop.");
            Log.d(TAG, "PreLoop: " + deviceMacAddressList.size() + " " + deviceMacAddressList.get(listMacAddressFlag));
            latiSave10 = null;
            longiSave10 = null;
            savePredictedDistance10 = null;
            dateTimeZone10 = null;
            listMacAddressFlagToo = 1;
            DatabaseReference mDatabase7 = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mRSSI7 = mDatabase7.child("Notification Data").child(mac).child(deviceMacAddressList.get(listMacAddressFlag));
            mRSSI7.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("latitude" + mac)) {
                        latiSave10 = dataSnapshot.child("latitude" + mac).getValue(String.class);
                        latiSaveDouble = Double.valueOf(latiSave10);
                    }
                    Log.d(TAG, "latiSave10 " + latiSave10);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Failed to get missingSwitch state.");
                }
            });
            DatabaseReference mDatabase10 = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mRSSI10 = mDatabase10.child("Notification Data").child(mac).child(deviceMacAddressList.get(listMacAddressFlag));
            mRSSI10.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("longitude" + mac)) {
                        longiSave10 = dataSnapshot.child("longitude" + mac).getValue(String.class);
                        longiSaveDouble = Double.valueOf(longiSave10);
                    }
                    Log.d(TAG, "longiSave10 " + longiSave10);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Failed to get missingSwitch state.");
                }
            });
            DatabaseReference mDatabase9 = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mRSSI9 = mDatabase9.child("Notification Data").child(mac).child(deviceMacAddressList.get(listMacAddressFlag)).child("raw" + mac);
            mRSSI9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    savePredictedDistance10 = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "savePredictedDistance10 " + savePredictedDistance10);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Failed to get missingSwitch state.");
                }
            });
            /*DatabaseReference mDatabase19 = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mRSSI19 = mDatabase19.child("Notification Data").child(mac).child(deviceMacAddressList.get(listMacAddressFlag));
            mRSSI19.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dateTimeZone10 = dataSnapshot.child("date&time" + mac).getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Failed to get missingSwitch state.");
                }
            });*/
            Log.d(TAG,"listMacAddressFlag (right before conditions) = "+listMacAddressFlag);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (latiSave10 != null && longiSave10 != null && savePredictedDistance10 != null) {
                        Log.d(TAG, "Loop: " + latiSave10 + " " + longiSave10 + " " + savePredictedDistance10 + " Run Number: " + listMacAddressFlag);
                        latiLongiList.add(latiSave10);
                        latiLongiList.add(longiSave10);
                        distanceAwayList.add(savePredictedDistance10);
                        Log.d(TAG, "Size: "+ latiLongiList.size() + ", " + distanceAwayList.size());
                        listMacAddressFlag = listMacAddressFlag + 1;
                        //dateList.add(dateTimeZone10);
                    }
                    final Handler handlerpo = new Handler();
                    handlerpo.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                    if (listMacAddressFlag <= deviceMacAddressList.size()-1) {
                        Log.d(TAG, "Allowed to loop again: " + listMacAddressFlag);
                        override = true;
                        loopGrab();
                    } else if(listMacAddressFlag > deviceMacAddressList.size()-1){
                        override = false;
                        listMacAddressFlag = 0;
                        listMacAddressFlagToo = 0;
                    }
                        }
                    }, 300);
                }
            }, 700);
        }
    }
    public void displayMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
    public void detectP1(){
        switchChildDetector();
        checkMissing();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mac.equals("")){
                    getState("missing");
                    getState("auto");
                    getState("search");
                }
            }
        },1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                detectP2();
            }
        }, 3000);
    }
    public void detectP2(){
        if(missingSwitch.equals("on")&& !mac.equals("")){
            switchOn70 = true;
            ev = 1;
            Log.d(TAG, "Missing switch autoset to ON");
            navigationView.getMenu().getItem(0).setChecked(true);
            saveMac(mac);
            checkMissing();
            flagTree = 0;
            listMacAddressFlagToo = 0;
            final Handler hqqandler = new Handler();
            hqqandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   getData();
                    final Handler handddler = new Handler();
                    handddler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //notificationOther();
                        }
                    },1000);
                }
            },1000);
        }else if(missingSwitch.equals("off") && !mac.equals("")){
            switchOn70 = false;
            ev = 0;
            Log.d(TAG, "Missing switch autoset to OFF");
            navigationView.getMenu().getItem(0).setChecked(false);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Switches").child("missingSwitch" + mac).setValue("off");
            mDatabase.child("MacIDs").child(mac).removeValue();
        } else {
            navigationView.getMenu().getItem(0).setChecked(false);
            ev = 0;
        }
        if(missingSearch.equals("on") && autoCounter69 == 0){
            autoThreshHold = 17;
            Log.d(TAG, "Missing search autoset to ON");
            av=1;
            navigationView.getMenu().getItem(2).setChecked(true);
            switchOn69 = true;
            //check = true;
            autoflag69 = true;
            autoCounter69 = 0;
            missingSearch = "on";
            //missingSearchLoop();
            //notificationOther();
        } else if(missingSearch.equals("off")){
            autoThreshHold = 17;
            av=0;
            Log.d(TAG, "Missing search autoset to OFF");
            navigationView.getMenu().getItem(2).setChecked(false);
            switchOn69 = false;
            autoflag69 = false;
            autoCounter69 = 0;
            errorkiller69 = 0;
            missingSearch = "off";
        } else {
            navigationView.getMenu().getItem(2).setChecked(false);
            av = 0;
        }
        if(autoSwitch.equals("on") && !mac.equals("") && autoCounter == 0){
            iv = 1;
            navigationView.getMenu().getItem(1).setChecked(true);
            switchOn = true;
            //check = true;
            autoflag = true;
            autoCounter = 0;
            autoSearch();
            Log.d(TAG, "switch autoset to ON");
        } else if(autoSwitch.equals("off") && !mac.equals("")){
            iv = 0;
            navigationView.getMenu().getItem(1).setChecked(false);
            Log.d(TAG, "switch autoset to OFF");
            switchOn = false;
            autoflag = false;
            autoCounter = 0;
        } else {
            navigationView.getMenu().getItem(1).setChecked(false);
            iv = 0;
        }
    }
}
