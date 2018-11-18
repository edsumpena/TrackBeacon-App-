package com.example.emilysumpena.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class changeNumber extends AppCompatActivity {
    String mac = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Button button = (Button) findViewById(R.id.con);
        mac = settings.getString("mac", "0");
        Log.d("qqq(mac) = ", mac);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText placeholder2 = (EditText) findViewById(R.id.editText70);
                EditText placeholder = (EditText) findViewById(R.id.editText71);
                builder.setMessage("Do you confirm that " + placeholder.getText() + " is your phone number? (You can change this later)");
                builder.setTitle("Final Confirmation!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("beaconPhone").child(mac).removeValue();
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("beaconPhone");
                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // if (!dataSnapshot.hasChild("date&time" + macAddressList.get(size))){
                                String a = ""+placeholder.getText();
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("beaconPhone").child(mac).child(a).setValue("");
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                Toast.makeText(getApplicationContext(), "Phone number successfully saved!", Toast.LENGTH_SHORT).show();
                                DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference().child("MacIDs");
                                rootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(mac)){
                                            rootRef2.child(mac).removeValue();
                                            //rootRef.child(mac).child(a).setValue("");
                                        }
                                        /*final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                            }
                                        }, 700);*/
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(), "Failed to save phone number.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Intent myIntent = new Intent(changeNumber.this, MapsActivity.class);
                                changeNumber.this.startActivity(myIntent);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                        public void run() {
                                Intent myIntent2 = new Intent(changeNumber.this, MapsActivity.class);
                                changeNumber.this.startActivity(myIntent2);
                                    }
                                }, 500);
                                    }
                                }, 500);
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
                if(a.equals(b) && !a.equals("") && !b.equals("") && a.length() == 12 && b.length() == 12 && String.valueOf(a.charAt(3)).equals("-") && String.valueOf(a.charAt(7)).equals("-") && String.valueOf(b.charAt(3)).equals("-") && String.valueOf(b.charAt(7)).equals("-")){

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    //alert.setTitle("AlertDialogExample");
                    alert.show();
                }
                if(!a.equals(b)){
                    Toast.makeText(getApplicationContext(), "Your phone numbers do not match!", Toast.LENGTH_SHORT).show();
                } else if(a.length() != 12 || b.length() != 12 || !String.valueOf(a.charAt(3)).equals("-") || !String.valueOf(a.charAt(7)).equals("-") || !String.valueOf(b.charAt(3)).equals("-") || !String.valueOf(a.charAt(7)).equals("-")){
                    Toast.makeText(getApplicationContext(), "Your phone number should be in the format XXX-XXX-XXXX!", Toast.LENGTH_SHORT).show();
                }
                if(placeholder.equals("")){
                    Toast.makeText(getApplicationContext(), "You must give a phone number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Button button2 = (Button) findViewById(R.id.can);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(changeNumber.this, MapsActivity.class);
                changeNumber.this.startActivity(myIntent);
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
