package com.example.emilysumpena.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.emilysumpena.myapplication.MainActivity.settings;

public class phoneNumber extends Activity {
    String phoneNumber = "";
    String mac = "";
    private static final String TAG = "Debug phoneNumber Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Button button = (Button) findViewById(R.id.verify);
        mac = settings.getString("mac", "0");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText placeholder = (EditText) findViewById(R.id.phone);
                EditText placeholder2 = (EditText) findViewById(R.id.confirmphone);
                builder.setMessage("Do you confirm that " + placeholder.getText() + " is your phone number? (You can change this later)");
                builder.setTitle("Final Confirmation!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("beaconPhone");
                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // if (!dataSnapshot.hasChild("date&time" + macAddressList.get(size))){
                                String a = ""+placeholder.getText();
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("beaconPhone").child(mac).child(a).setValue("");
                                Toast.makeText(getApplicationContext(), "Phone number successfully saved!", Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(phoneNumber.this, MapsActivity.class);
                                phoneNumber.this.startActivity(myIntent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Failed to save phone number.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        Toast.makeText(getApplicationContext(), "You must confirm that this is your phone number!", Toast.LENGTH_SHORT).show();
                    }
                });
                String a = ""+placeholder.getText();
                String b = ""+placeholder2.getText();
                if(a.equals(b) && !a.equals("") && !b.equals("")){

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    //alert.setTitle("AlertDialogExample");
                    alert.show();
                }
                if(!a.equals(b)){
                    Toast.makeText(getApplicationContext(), "Your phone numbers do not match!", Toast.LENGTH_SHORT).show();
                }
                if(placeholder.equals("")){
                    Toast.makeText(getApplicationContext(), "You must give a phone number!", Toast.LENGTH_SHORT).show();
                }
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

}
