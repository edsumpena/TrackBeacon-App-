package com.example.emilysumpena.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.example.emilysumpena.myapplication.MainActivity.settings;

public class numberCheck extends AppCompatActivity {
    String mac = "";
    String name2 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_check);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Button button2 = (Button) findViewById(R.id.search);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText placeholder = (EditText) findViewById(R.id.macInput);
                String a = ""+ placeholder.getText();
                DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference().child("MacIDs").child(a);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            name2 = ds.getKey();
                            TextView locationTv = (TextView) findViewById(R.id.phoneNumber);
                            locationTv.setText(name2 + "");
                        }
                        if(name2.equals("")){
                            Toast.makeText(getApplicationContext(), "This beacon does not exist/is not in missing mode.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                ref0.addListenerForSingleValueEvent(eventListener);
            }
        });
        final Button button = (Button) findViewById(R.id.leave);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(numberCheck.this, MapsActivity.class);
                numberCheck.this.startActivity(myIntent);
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
