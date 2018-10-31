package com.example.emilysumpena.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.emilysumpena.myapplication.MapsActivity.settinggs;
import static com.example.emilysumpena.myapplication.data.settingggs;



public class data extends Activity {
    private static final String TAG = "Debug DataActivity";
    String lati = "";
    String longi = "";
    String saveDate = "";
    String saveTime = "";
    String d = "";
    String deviceTimeZone = "";
    String predictedDistance = "";
    int arrayCount = 0;
    int counter = 0;
    int counter2 = 0;
    int counter3 = 0;
    CheckBox row1;
    CheckBox row2;
    CheckBox row3;
    CheckBox row4;
    CheckBox row5;
    CheckBox row6;
    CheckBox row7;
    CheckBox row8;
    CheckBox row9;
    static SharedPreferences settingggs;
    static SharedPreferences.Editor edittor;
    String holder = "";
    String latiArray[] = new String[]{holder, holder, holder, holder, holder, holder, holder, holder, holder};
    String longiArray[] = new String[]{holder, holder, holder, holder, holder, holder, holder, holder, holder};
    String dateArray[] = new String[]{holder, holder, holder, holder, holder, holder, holder, holder, holder};
    String timeArray[] = new String[]{holder, holder, holder, holder, holder, holder, holder, holder, holder};
    String statusArray[] = new String[]{holder, holder, holder, holder, holder, holder, holder, holder, holder};
    String distanceArray[] = new String[]{holder, holder, holder, holder, holder, holder, holder, holder, holder};
    String zoneArray[] = new String[]{holder, holder, holder, holder, holder, holder, holder, holder, holder};
    String mac = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        settingggs = this.getPreferences(MODE_MULTI_PROCESS);
        TextView locationTv = (TextView) findViewById(R.id.zeroOne);
        lati = settinggs.getString("latiSave", "0");
        longi = settinggs.getString("longiSave", "0");
        saveDate = settinggs.getString("saveDate", "0");
        saveTime = settinggs.getString("saveTime", "0");
        mac = settinggs.getString("mac", "0");
        d = settinggs.getString("d", "0");
        deviceTimeZone = settinggs.getString("deviceTimeZone", "0");
        predictedDistance = settinggs.getString("savedPredictedDistance", "0");
        Log.d(TAG, "latiSave = " + lati + " longiSave = " + longi + " predictedDistance = " + predictedDistance + " saveDate = " + saveDate);
        locationTv.setText(lati);
        locationTv = (TextView) findViewById(R.id.zeroTwo);
        locationTv.setText(longi);
        locationTv = (TextView) findViewById(R.id.zeroThree);
        locationTv.setText(saveDate + ", " + saveTime + " " + deviceTimeZone);
        locationTv = (TextView) findViewById(R.id.zeroFour);
        locationTv.setText(predictedDistance + " Meters");
        locationTv = (TextView) findViewById(R.id.zeroFive);
        locationTv.setText(d);
        readValues();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                savedData();
            }
        },1000);
        final Button button = (Button) findViewById(R.id.exit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                counter3 = 0;
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Device Notification");
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        while(counter3 < 9) {
                            Log.d(TAG, "jj counter 3 = " + counter3);
                            if (dataSnapshot.hasChild("date" + counter3 + mac) && dataSnapshot.child("date" + counter3 + mac).getValue(String.class).equals("")) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Device Notification").child("date" + counter3 + mac).removeValue();
                                Log.d(TAG, "ggdate"+counter3+" removed!");
                            }
                            if (dataSnapshot.hasChild("time" + counter3 + mac) && dataSnapshot.child("time" + counter3 + mac).getValue(String.class).equals("")) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Device Notification").child("time" + counter3 + mac).removeValue();
                                Log.d(TAG, "ggtime"+counter3+" removed!");
                            }
                            if (dataSnapshot.hasChild("lati" + counter3 + mac) && dataSnapshot.child("lati" + counter3 + mac).getValue(String.class).equals("")) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Device Notification").child("lati" + counter3 + mac).removeValue();
                                Log.d(TAG, "gglati"+counter3+" removed!");
                            }
                            if (dataSnapshot.hasChild("longi" + counter3 + mac) && dataSnapshot.child("longi" + counter3 + mac).getValue(String.class).equals("")) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Device Notification").child("longi" + counter3 + mac).removeValue();
                                Log.d(TAG, "gglongi"+counter3+" removed!");
                            }
                            if (dataSnapshot.hasChild("status" + counter3 + mac) && dataSnapshot.child("status" + counter3 + mac).getValue(String.class).equals("")) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Device Notification").child("status" + counter3 + mac).removeValue();
                                Log.d(TAG, "gglongi"+counter3+" removed!");
                            }
                            if (dataSnapshot.hasChild("distance" + counter3 + mac) && dataSnapshot.child("distance" + counter3 + mac).getValue(String.class).equals("")) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Device Notification").child("distance" + counter3 + mac).removeValue();
                                Log.d(TAG, "ggdistance"+counter3+" removed!");
                            }
                            if (dataSnapshot.hasChild("zone" + counter3 + mac) && dataSnapshot.child("zone" + counter3 + mac).getValue(String.class).equals("")) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Device Notification").child("zone" + counter3 + mac).removeValue();
                                Log.d(TAG, "ggzone"+counter3+" removed!");
                            }
                            counter3 = counter3 + 1;
                        }
                        if(counter3 >= 9) {
                            Intent myIntent = new Intent(data.this, MapsActivity.class);
                            data.this.startActivity(myIntent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "Failed to get missingSwitch state.");
                    }
                });
            }
        });
        final Button button2 = (Button) findViewById(R.id.save);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                savetoDataBase();
            }
        });
        final Button button3 = (Button) findViewById(R.id.clear);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkBoxes();
            }
        });

        //latiArray[arrayCount] = lati;

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

    public void savedData() {
        TextView locationTv = (TextView) findViewById(R.id.oneOne);
        locationTv.setText("1.  " + latiArray[0]);
        locationTv = (TextView) findViewById(R.id.oneTwo);
        locationTv.setText(longiArray[0]);
        locationTv = (TextView) findViewById(R.id.oneThree);
        locationTv.setText(dateArray[0] + ", " + timeArray[0] + " " + zoneArray[0]);
        locationTv = (TextView) findViewById(R.id.oneFour);
        locationTv.setText(distanceArray[0] + " Meters");
        locationTv = (TextView) findViewById(R.id.oneFive);
        locationTv.setText(statusArray[0]);

        locationTv = (TextView) findViewById(R.id.twoOne);
        locationTv.setText("2.  " + latiArray[1]);
        locationTv = (TextView) findViewById(R.id.twoTwo);
        locationTv.setText(longiArray[1]);
        locationTv = (TextView) findViewById(R.id.twoThree);
        locationTv.setText(dateArray[1] + ", " + timeArray[1] + " " + zoneArray[1]);
        locationTv = (TextView) findViewById(R.id.twoFour);
        locationTv.setText(distanceArray[1] + " Meters");
        locationTv = (TextView) findViewById(R.id.twoFive);
        locationTv.setText(statusArray[1]);

        locationTv = (TextView) findViewById(R.id.threeOne);
        locationTv.setText("3.  " + latiArray[2]);
        locationTv = (TextView) findViewById(R.id.threeTwo);
        locationTv.setText(longiArray[2]);
        locationTv = (TextView) findViewById(R.id.threeThree);
        locationTv.setText(dateArray[2] + ", " + timeArray[2] + " " + zoneArray[2]);
        locationTv = (TextView) findViewById(R.id.threeFour);
        locationTv.setText(distanceArray[2] + " Meters");
        locationTv = (TextView) findViewById(R.id.threeFive);
        locationTv.setText(statusArray[2]);

        locationTv = (TextView) findViewById(R.id.fourOne);
        locationTv.setText("4.  " + latiArray[3]);
        locationTv = (TextView) findViewById(R.id.fourTwo);
        locationTv.setText(longiArray[3]);
        locationTv = (TextView) findViewById(R.id.fourThree);
        locationTv.setText(dateArray[3] + ", " + timeArray[3] + " " + zoneArray[3]);
        locationTv = (TextView) findViewById(R.id.fourFour);
        locationTv.setText(distanceArray[3] + " Meters");
        locationTv = (TextView) findViewById(R.id.fourFive);
        locationTv.setText(statusArray[3]);

        locationTv = (TextView) findViewById(R.id.fiveOne);
        locationTv.setText("5.  " + latiArray[4]);
        locationTv = (TextView) findViewById(R.id.fiveTwo);
        locationTv.setText(longiArray[4]);
        locationTv = (TextView) findViewById(R.id.fiveThree);
        locationTv.setText(dateArray[4] + ", " + timeArray[4] + " " + zoneArray[4]);
        locationTv = (TextView) findViewById(R.id.fiveFour);
        locationTv.setText(distanceArray[4] + " Meters");
        locationTv = (TextView) findViewById(R.id.fiveFive);
        locationTv.setText(statusArray[4]);

        locationTv = (TextView) findViewById(R.id.sixOne);
        locationTv.setText("6.  " + latiArray[5]);
        locationTv = (TextView) findViewById(R.id.sixTwo);
        locationTv.setText(longiArray[5]);
        locationTv = (TextView) findViewById(R.id.sixThree);
        locationTv.setText(dateArray[5] + ", " + timeArray[5] + " " + zoneArray[5]);
        locationTv = (TextView) findViewById(R.id.sixFour);
        locationTv.setText(distanceArray[5] + " Meters");
        locationTv = (TextView) findViewById(R.id.sixFive);
        locationTv.setText(statusArray[5]);

        locationTv = (TextView) findViewById(R.id.sevenOne);
        locationTv.setText("7.  " + latiArray[6]);
        locationTv = (TextView) findViewById(R.id.sevenTwo);
        locationTv.setText(longiArray[6]);
        locationTv = (TextView) findViewById(R.id.sevenThree);
        locationTv.setText(dateArray[6] + ", " + timeArray[6] + " " + zoneArray[6]);
        locationTv = (TextView) findViewById(R.id.sevenFour);
        locationTv.setText(distanceArray[6] + " Meters");
        locationTv = (TextView) findViewById(R.id.sevenFive);
        locationTv.setText(statusArray[6]);

        locationTv = (TextView) findViewById(R.id.eightOne);
        locationTv.setText("8.  " + latiArray[7]);
        locationTv = (TextView) findViewById(R.id.eightTwo);
        locationTv.setText(longiArray[7]);
        locationTv = (TextView) findViewById(R.id.eightThree);
        locationTv.setText(dateArray[7] + ", " + timeArray[7] + " " + zoneArray[7]);
        locationTv = (TextView) findViewById(R.id.eightFour);
        locationTv.setText(distanceArray[7] + " Meters");
        locationTv = (TextView) findViewById(R.id.eightFive);
        locationTv.setText(statusArray[7]);

        locationTv = (TextView) findViewById(R.id.nineOne);
        locationTv.setText("9.  " + latiArray[8]);
        locationTv = (TextView) findViewById(R.id.nineTwo);
        locationTv.setText(longiArray[8]);
        locationTv = (TextView) findViewById(R.id.nineThree);
        locationTv.setText(dateArray[8] + ", " + timeArray[8] + " " + zoneArray[8]);
        locationTv = (TextView) findViewById(R.id.nineFour);
        locationTv.setText(distanceArray[8] + " Meters");
        locationTv = (TextView) findViewById(R.id.nineFive);
        locationTv.setText(statusArray[8]);
    }

    public void savetoDataBase() {
        arrayCount = 0;
        while (arrayCount < 9) {
            if (latiArray[arrayCount].equals(holder) || latiArray[arrayCount].equals("") || latiArray[arrayCount].equals(null)) {
                Log.d(TAG, "jj arrayCount = " + arrayCount + " --> when loop stopped");
                latiArray[arrayCount] = lati;
                longiArray[arrayCount] = longi;
                dateArray[arrayCount] = saveDate;
                timeArray[arrayCount] = saveTime;
                distanceArray[arrayCount] = predictedDistance;
                statusArray[arrayCount] = d;
                zoneArray[arrayCount] = deviceTimeZone;
                arrayCount = 0;
                writeValues();
                savedData();
                break;
            } else {
                arrayCount = arrayCount + 1;
                Log.d(TAG, "jj arrayCount = " + arrayCount + " -- Still Looping");
            }
        }
        if (arrayCount >= 9) {
            Toast.makeText(getApplicationContext(), "Error, all 9 save slots are FULL!!", Toast.LENGTH_LONG).show();
            arrayCount = 0;
        }
    }

    public void checkBoxes() {
        row1 = (CheckBox) findViewById(R.id.row1);
        row2 = (CheckBox) findViewById(R.id.row2);
        row3 = (CheckBox) findViewById(R.id.row3);
        row4 = (CheckBox) findViewById(R.id.row4);
        row5 = (CheckBox) findViewById(R.id.row5);
        row6 = (CheckBox) findViewById(R.id.row6);
        row7 = (CheckBox) findViewById(R.id.row7);
        row8 = (CheckBox) findViewById(R.id.row8);
        row9 = (CheckBox) findViewById(R.id.row9);

        //is chkIos checked?
        if (row1.isChecked()) {
            latiArray[0] = holder;
            longiArray[0] = holder;
            dateArray[0] = holder;
            timeArray[0] = holder;
            distanceArray[0] = holder;
            statusArray[0] = holder;
            zoneArray[0] = holder;
            writeValues();
            savedData();
            Log.d(TAG, "Row1");
        }


        //is chkIos checked?
        if (row2.isChecked()) {
            latiArray[1] = holder;
            longiArray[1] = holder;
            dateArray[1] = holder;
            timeArray[1] = holder;
            distanceArray[1] = holder;
            statusArray[1] = holder;
            zoneArray[1] = holder;
            writeValues();
            savedData();
            Log.d(TAG, "Row2");
        }


        //is chkIos checked?
        if (row3.isChecked()) {
            latiArray[2] = holder;
            longiArray[2] = holder;
            dateArray[2] = holder;
            timeArray[2] = holder;
            distanceArray[2] = holder;
            statusArray[2] = holder;
            zoneArray[2] = holder;
            writeValues();
            savedData();
            Log.d(TAG, "Row3");
        }


        //is chkIos checked?
        if (row4.isChecked()) {
            latiArray[3] = holder;
            longiArray[3] = holder;
            dateArray[3] = holder;
            timeArray[3] = holder;
            distanceArray[3] = holder;
            statusArray[3] = holder;
            zoneArray[3] = holder;
            writeValues();
            savedData();
            Log.d(TAG, "Row4");
        }


        //is chkIos checked?
        if (row5.isChecked()) {
            latiArray[4] = holder;
            longiArray[4] = holder;
            dateArray[4] = holder;
            timeArray[4] = holder;
            distanceArray[4] = holder;
            statusArray[4] = holder;
            zoneArray[4] = holder;
            writeValues();
            savedData();
            Log.d(TAG, "Row5");
        }


        //is chkIos checked?
        if (row6.isChecked()) {
            latiArray[5] = holder;
            longiArray[5] = holder;
            dateArray[5] = holder;
            timeArray[5] = holder;
            distanceArray[5] = holder;
            statusArray[5] = holder;
            zoneArray[5] = holder;
            writeValues();
            savedData();
            Log.d(TAG, "Row6");
        }


        //is chkIos checked?
        if (row7.isChecked()) {
            latiArray[6] = holder;
            longiArray[6] = holder;
            dateArray[6] = holder;
            timeArray[6] = holder;
            distanceArray[6] = holder;
            statusArray[6] = holder;
            zoneArray[6] = holder;
            writeValues();
            savedData();
            Log.d(TAG, "Row7");
        }


        //is chkIos checked?
        if (row8.isChecked()) {
            latiArray[7] = holder;
            longiArray[7] = holder;
            dateArray[7] = holder;
            timeArray[7] = holder;
            distanceArray[7] = holder;
            statusArray[7] = holder;
            zoneArray[7] = holder;
            writeValues();
            savedData();
            Log.d(TAG, "Row8");
        }


        //is chkIos checked?
        if (row9.isChecked()) {
            latiArray[8] = holder;
            longiArray[8] = holder;
            dateArray[8] = holder;
            timeArray[8] = holder;
            distanceArray[8] = holder;
            statusArray[8] = holder;
            zoneArray[8] = holder;
            writeValues();
            savedData();
            Log.d(TAG, "Row9");
        }
        Toast.makeText(getApplicationContext(), "Data successfully cleared!!", Toast.LENGTH_SHORT).show();
    }
    public void readValues(){
        counter2 = 0;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Device Notification");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                while(counter2 < 9) {
                    Log.d(TAG, "jj counter 2 = " + counter2);
                    if (!dataSnapshot.hasChild("date" + counter2 + mac)) {
                        rootRef.child("date" + counter2 + mac).setValue(dateArray[counter2]);
                        Log.d(TAG, "no");
                    }
                    if (!dataSnapshot.hasChild("time" + counter2 + mac)) {
                        rootRef.child("time" + counter2 + mac).setValue(timeArray[counter2]);
                        Log.d(TAG, "no");
                    }
                    if (!dataSnapshot.hasChild("lati" + counter2 + mac)) {
                        rootRef.child("lati" + counter2 + mac).setValue(latiArray[counter2]);
                        Log.d(TAG, "no");
                    }
                    if (!dataSnapshot.hasChild("longi" + counter2 + mac)) {
                        rootRef.child("longi" + counter2 + mac).setValue(longiArray[counter2]);
                        Log.d(TAG, "no");
                    }
                    if (!dataSnapshot.hasChild("status" + counter2 + mac)) {
                        rootRef.child("status" + counter2 + mac).setValue(statusArray[counter2]);
                        Log.d(TAG, "no");
                    }
                    if (!dataSnapshot.hasChild("distance" + counter2 + mac)) {
                        rootRef.child("distance" + counter2 + mac).setValue(distanceArray[counter2]);
                        Log.d(TAG, "no");
                    }
                    if (!dataSnapshot.hasChild("zone" + counter2 + mac)) {
                        rootRef.child("zone" + counter2 + mac).setValue(zoneArray[counter2]);
                        Log.d(TAG, "no");
                    }
                    if (dataSnapshot.hasChild("date" + counter2 + mac)) {
                        dateArray[counter2] = dataSnapshot.child("date" + counter2 + mac).getValue(String.class);
                        Log.d(TAG, "yes");
                    }
                    if (dataSnapshot.hasChild("time" + counter2 + mac)) {
                        timeArray[counter2] = dataSnapshot.child("time" + counter2 + mac).getValue(String.class);
                        Log.d(TAG, "yes");
                    }
                    if (dataSnapshot.hasChild("lati" + counter2 + mac)) {
                        latiArray[counter2] = dataSnapshot.child("lati" + counter2 + mac).getValue(String.class);
                        Log.d(TAG, "yes");
                    }
                    if (dataSnapshot.hasChild("longi" + counter2 + mac)) {
                        longiArray[counter2] = dataSnapshot.child("longi" + counter2 + mac).getValue(String.class);
                        Log.d(TAG, "yes");
                    }
                    if (dataSnapshot.hasChild("status" + counter2 + mac)) {
                        statusArray[counter2] = dataSnapshot.child("status" + counter2 + mac).getValue(String.class);
                        Log.d(TAG, "yes");
                    }
                    if (dataSnapshot.hasChild("distance" + counter2 + mac)) {
                        distanceArray[counter2] = dataSnapshot.child("distance" + counter2 + mac).getValue(String.class);
                        Log.d(TAG, "yes");
                    }
                    if (dataSnapshot.hasChild("zone" + counter2 + mac)) {
                        zoneArray[counter2] = dataSnapshot.child("zone" + counter2 + mac).getValue(String.class);
                        Log.d(TAG, "yes");
                    }
                    counter2 = counter2 + 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to get missingSwitch state.");
            }
        });
    }
    public void writeValues(){
        counter = 0;
        DatabaseReference rootRef5 = FirebaseDatabase.getInstance().getReference().child("Device Notification");
        rootRef5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                while(counter < 9) {
                    Log.d(TAG, "jj counter = " + counter2);
                    rootRef5.child("date" + counter + mac).setValue(dateArray[counter]);
                    rootRef5.child("time" + counter + mac).setValue(timeArray[counter]);
                    rootRef5.child("lati" + counter + mac).setValue(latiArray[counter]);
                    rootRef5.child("longi" + counter + mac).setValue(longiArray[counter]);
                    rootRef5.child("status" + counter + mac).setValue(statusArray[counter]);
                    rootRef5.child("distance" + counter + mac).setValue(distanceArray[counter]);
                    rootRef5.child("zone" + counter + mac).setValue(zoneArray[counter]);
                    counter = counter + 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to get missingSwitch state.");
            }
        });
    }

}


