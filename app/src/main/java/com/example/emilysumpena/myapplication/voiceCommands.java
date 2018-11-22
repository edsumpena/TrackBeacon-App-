package com.example.emilysumpena.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import static com.example.emilysumpena.myapplication.MainActivity.settings;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class voiceCommands extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static final String TAG = "Debug voiceCommands";
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    int currentVolume = 0;
    boolean done = false;
    boolean loading = true;
    private TextToSpeech tts;
    boolean trilateratedPoint = false;
    String mac = "";
    boolean unknownTrilaterated = false;
    double trilateratedLat = 0;
    double trilateratedLong = 0;
    double trilateratedDistance = 0;
    boolean error = false;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_commands);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mac = settings.getString("mac", "0");
        setSupportActionBar(toolbar);
        tts = new TextToSpeech(this, this);
        init(this);
        startRecognizing();
        loadingLoop();
        final Button button = (Button) findViewById(R.id.leaveVoiceCommands);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                voiceCommands.this.startActivity(myIntent);
            }
        });
    }

    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    private class SpeechRecognitionListener extends Activity implements RecognitionListener {
        String lastPartialText;

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.v(TAG, "lol >>> onReadyForSpeech");
            Log.d(TAG, "lol ready");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.v(TAG, "lol >>> onBeginningOfSpeech");
            Log.d(TAG, "lol recognizing");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            Log.v(TAG, "lol >>> onEndOfSpeech");
            Log.d(TAG, "lol waiting result");
        }

        @Override
        public void onError(int error) {
            Log.v(TAG, "lol >>> onError : " + error);
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    Log.e(TAG, "ERROR_AUDIO");
                    loading = false;
                    TextView tv = (TextView) findViewById(R.id.debugTextview);
                    loading = false;
                    tv.setTextColor(Color.parseColor("#ffCC0000"));
                    tv.setText("Audio Error!");
                    returnToMaps();
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    Log.e(TAG, "ERROR_CLIENT");
                    loading = false;
                    tv = (TextView) findViewById(R.id.debugTextview);
                    tv.setTextColor(Color.parseColor("#ffCC0000"));
                    tv.setText("Client Error!");
                    returnToMaps();
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    Log.e(TAG, "ERROR_INSUFFICIENT_PERMISSIONS");
                    loading = false;
                    tv = (TextView) findViewById(R.id.debugTextview);
                    tv.setTextColor(Color.parseColor("#ffCC0000"));
                    tv.setText("Insufficient Permissions!");
                    returnToMaps();
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    Log.e(TAG, "ERROR_NETWORK");
                    loading = false;
                    tv = (TextView) findViewById(R.id.debugTextview);
                    tv.setTextColor(Color.parseColor("#ffCC0000"));
                    tv.setText("Network Error!");
                    returnToMaps();
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    Log.e(TAG, "ERROR_NETWORK_TIMEOUT");
                    loading = false;
                    tv = (TextView) findViewById(R.id.debugTextview);
                    tv.setTextColor(Color.parseColor("#ffCC0000"));
                    tv.setText("Network has Timed Out!");
                    returnToMaps();
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    Log.e(TAG, "ERROR_RECOGNIZER_BUSY");
                    startRecognizing();
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    Log.e(TAG, "ERROR_SERVER");
                    loading = false;
                    tv = (TextView) findViewById(R.id.debugTextview);
                    tv.setTextColor(Color.parseColor("#ffCC0000"));
                    tv.setText("Server Error!");
                    returnToMaps();
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    Log.v(TAG, "ERROR_NO_MATCH");
                    startRecognizing();
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    Log.v(TAG, "ERROR_SPEECH_TIMEOUT");
                    startRecognizing();
                    break;
                default:
                    Log.v(TAG, "ERROR_UNKOWN");
                    loading = false;
                    tv = (TextView) findViewById(R.id.debugTextview);
                    tv.setTextColor(Color.parseColor("#ffCC0000"));
                    tv.setText("Unknown Error!");
                    returnToMaps();
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.v(TAG, "lol >>> onPartialResults");
            List<String> resultList = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (resultList != null) {
                String text = resultList.get(0);
                if (text.equals(lastPartialText)) {
                    return;
                }
                lastPartialText = text;
                Log.v(TAG, "lol partial : " + text);
            }
        }

        @Override
        public void onResults(Bundle results) {
            Log.v(TAG, "lol >>> onResults");
            ArrayList<String> resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (resultList != null) {
                String text = resultList.get(0);
                Log.d(TAG, "lol Voice result = " + text);
                if (text.toLowerCase().indexOf("on".toLowerCase()) != -1 && text.toLowerCase().indexOf("missing mode".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("on".toLowerCase()) != -1 && text.toLowerCase().indexOf("auto search".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("search".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1 || text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("show RSSI".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("off".toLowerCase()) != -1 && text.toLowerCase().indexOf("voice commands".toLowerCase()) != -1 || text.toLowerCase().indexOf("voice command".toLowerCase()) != -1 && text.toLowerCase().indexOf("turn off".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("phone numbers".toLowerCase()) != -1 || text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("phone number".toLowerCase()) != -1) {
                    done = true;
                    counter = counter + 1;
                }
                if (text.toLowerCase().indexOf("change".toLowerCase()) != -1 && text.toLowerCase().indexOf("phone number".toLowerCase()) != -1) {
                    counter = counter + 1;
                    done = true;
                }
                if (text.toLowerCase().indexOf("clear".toLowerCase()) != -1 && text.toLowerCase().indexOf("found".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("clear".toLowerCase()) != -1 && text.toLowerCase().indexOf("anonymous".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("my location".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("beacon location".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("recalibrate".toLowerCase()) != -1 || text.toLowerCase().indexOf("calibrate".toLowerCase()) != -1) {
                    counter = counter + 1;
                    done = true;
                }
                if (text.toLowerCase().indexOf("exit".toLowerCase()) != -1 || text.toLowerCase().indexOf("change beacon".toLowerCase()) != -1) {
                    counter = counter + 1;
                    done = true;
                }
                if (text.toLowerCase().indexOf("off".toLowerCase()) != -1 && text.toLowerCase().indexOf("missing mode".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("off".toLowerCase()) != -1 && text.toLowerCase().indexOf("auto search".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("stop".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1 || text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("RSSI".toLowerCase()) != -1 && text.toLowerCase().indexOf("hide".toLowerCase()) != -1 || text.toLowerCase().indexOf("RSSI".toLowerCase()) != -1 && text.toLowerCase().indexOf("stop".toLowerCase()) != -1) {
                    done = true;
                }
                if (text.toLowerCase().indexOf("estimated".toLowerCase()) != -1 && text.toLowerCase().indexOf("location".toLowerCase()) != -1 || text.toLowerCase().indexOf("where is my beacon".toLowerCase()) != -1 || text.toLowerCase().indexOf("predicted beacon".toLowerCase()) != -1 && text.toLowerCase().indexOf("location".toLowerCase()) != -1 || text.toLowerCase().indexOf("beacon's".toLowerCase()) != -1 && text.toLowerCase().indexOf("location".toLowerCase()) != -1 || text.toLowerCase().indexOf("predicted".toLowerCase()) != -1 && text.toLowerCase().indexOf("location".toLowerCase()) != -1 || text.toLowerCase().indexOf("trilateration".toLowerCase()) != -1) {
                    trilateratedPoint = true;
                }
                if (!done && !trilateratedPoint) {
                    loading = false;
                    cancelRecognizing();
                    textViewOne();
                    say("This is not a command that I understand. Please try again.");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                loading = true;
                                loadingLoop();
                                startRecognizing();
                        }
                    }, 5000);
                }
                if (done || trilateratedPoint) {
                    if(trilateratedPoint) {
                        loading = false;
                        Log.d(TAG,"mac = "+mac);
                        textViewTwo();
                        DatabaseReference mDatabase22 = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference mRSSI22 = mDatabase22.child("Trilaterated Point");
                        mRSSI22.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("lat" + mac)) {
                                    trilateratedLat = (dataSnapshot.child("lat" + mac).getValue(Double.class));
                                } else {
                                    unknownTrilaterated = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d(TAG, "Failed to get missingSwitch state.");
                            }
                        });
                        DatabaseReference mDatabase23 = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference mRSSI23 = mDatabase23.child("Trilaterated Point");
                        mRSSI23.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("long" + mac)) {
                                    trilateratedLong = (dataSnapshot.child("long" + mac).getValue(Double.class));
                                } else {
                                    unknownTrilaterated = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d(TAG, "Failed to get missingSwitch state.");
                            }
                        });
                        DatabaseReference mDatabase24 = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference mRSSI24 = mDatabase24.child("Trilaterated Point");
                        mRSSI24.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("distance" + mac)) {
                                    trilateratedDistance = (dataSnapshot.child("distance" + mac).getValue(Double.class));
                                } else {
                                    unknownTrilaterated = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d(TAG, "Failed to get missingSwitch state.");
                            }
                        });
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!unknownTrilaterated) {
                                    roundLatLong();
                                    cancelRecognizing();
                                    resumeVolume();
                                    say("You are " + trilateratedDistance + " meters away from estimated Beacon location of about latitude " + trilateratedLat + ", longitude " + trilateratedLong);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (done) {
                                                textViewThree();
                                                resumeVolume();
                                                say("Please wait while your other requests are being fulfilled.");
                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (counter > 1) {
                                                            error = true;
                                                        }
                                                        if (!error && text.toLowerCase().indexOf("my location".toLowerCase()) == -1 && text.toLowerCase().indexOf("beacon location".toLowerCase()) != -1 || text.toLowerCase().indexOf("my location".toLowerCase()) != -1 && text.toLowerCase().indexOf("beacon location".toLowerCase()) == -1) {
                                                            if (text.toLowerCase().indexOf("my location".toLowerCase()) != -1) {
                                                                FirebaseDatabase.getInstance().getReference().child("Voice Commands").child("jump2My" + mac).setValue("true");
                                                                Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                                voiceCommands.this.startActivity(myIntent);
                                                            }
                                                            if (text.toLowerCase().indexOf("beacon location".toLowerCase()) != -1) {
                                                                FirebaseDatabase.getInstance().getReference().child("Voice Commands").child("jump2Beacon" + mac).setValue("true");
                                                                Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                                voiceCommands.this.startActivity(myIntent);
                                                            }
                                                        } else if (text.toLowerCase().indexOf("my location".toLowerCase()) != -1 && text.toLowerCase().indexOf("beacon location".toLowerCase()) != -1 || counter > 1) {
                                                            error = true;
                                                        }
                                                        if (!error && counter == 1) {
                                                            if (text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("phone numbers".toLowerCase()) != -1 || text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("phone number".toLowerCase()) != -1) {
                                                                Intent myIntent = new Intent(voiceCommands.this, numberCheck.class);
                                                                voiceCommands.this.startActivity(myIntent);
                                                            }
                                                            if (!mac.equals("")&&text.toLowerCase().indexOf("change".toLowerCase()) != -1 && text.toLowerCase().indexOf("phone number".toLowerCase()) != -1) {
                                                                Intent myIntent = new Intent(voiceCommands.this, changeNumber.class);
                                                                voiceCommands.this.startActivity(myIntent);
                                                            }
                                                            if (!mac.equals("")&&text.toLowerCase().indexOf("recalibrate".toLowerCase()) != -1) {
                                                                Intent myIntent = new Intent(voiceCommands.this, recalibrate.class);
                                                                voiceCommands.this.startActivity(myIntent);
                                                            }
                                                            if (text.toLowerCase().indexOf("exit".toLowerCase()) != -1 || text.toLowerCase().indexOf("change beacon".toLowerCase()) != -1) {
                                                                Intent myIntent = new Intent(voiceCommands.this, MainActivity.class);
                                                                voiceCommands.this.startActivity(myIntent);
                                                            }
                                                        } else if (counter > 1) {
                                                            error = true;
                                                        }
                                                        if (!mac.equals("")&&!error && text.toLowerCase().indexOf("on".toLowerCase()) != -1 && text.toLowerCase().indexOf("missing mode".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Switches").child("missingSwitch" + mac).setValue("on");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!mac.equals("")&&!error && text.toLowerCase().indexOf("on".toLowerCase()) != -1 && text.toLowerCase().indexOf("auto search".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Switches").child("autoSwitch" + mac).setValue("on");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!error && text.toLowerCase().indexOf("search".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1 || text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Switches").child("missingSearch" + mac).setValue("on");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!error && text.toLowerCase().indexOf("show RSSI".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Switches").child("rssiSwitch" + mac).setValue("on");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!error && text.toLowerCase().indexOf("off".toLowerCase()) != -1 && text.toLowerCase().indexOf("voice commands".toLowerCase()) != -1 || text.toLowerCase().indexOf("voice command".toLowerCase()) != -1 && text.toLowerCase().indexOf("turn off".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Switches").child("voiceSwitch" + mac).setValue("off");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!error && text.toLowerCase().indexOf("clear".toLowerCase()) != -1 && text.toLowerCase().indexOf("anonymous".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Voice Commands").child("clearAnonymous" + mac).setValue("true");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!error && text.toLowerCase().indexOf("clear".toLowerCase()) != -1 && text.toLowerCase().indexOf("found".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Voice Commands").child("clearFound" + mac).setValue("true");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!mac.equals("")&&!error && text.toLowerCase().indexOf("off".toLowerCase()) != -1 && text.toLowerCase().indexOf("missing mode".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Switches").child("missingSwitch" + mac).setValue("off");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!mac.equals("")&&!error && text.toLowerCase().indexOf("off".toLowerCase()) != -1 && text.toLowerCase().indexOf("auto search".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Switches").child("autoSwitch" + mac).setValue("off");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!error && text.toLowerCase().indexOf("stop".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1 || text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Switches").child("missingSearch" + mac).setValue("off");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (!error && text.toLowerCase().indexOf("RSSI".toLowerCase()) != -1 && text.toLowerCase().indexOf("hide".toLowerCase()) != -1 || text.toLowerCase().indexOf("RSSI".toLowerCase()) != -1 && text.toLowerCase().indexOf("stop".toLowerCase()) != -1) {
                                                            FirebaseDatabase.getInstance().getReference().child("Switches").child("rssiSwitch" + mac).setValue("off");
                                                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                                            voiceCommands.this.startActivity(myIntent);
                                                        }
                                                        if (error) {
                                                            loading = false;
                                                            cancelRecognizing();
                                                            textViewFour();
                                                            resumeVolume();
                                                            say("Your commands could not be processed since they conflict with each other. Please try again.");
                                                            final Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    loading = true;
                                                                    loadingLoop();
                                                                    startRecognizing();
                                                                    error = false;
                                                                    counter = 0;
                                                                }
                                                            }, 6000);
                                                        }

                                                    }
                                                }, 3000);
                                            } else {
                                                loading = true;
                                                startRecognizing();
                                                loadingLoop();
                                            }
                                        }
                                    }, 12500);

                                } else {
                                    cancelRecognizing();
                                    resumeVolume();
                                    say("Your beacon does not have a estimated location. Make sure that other devices have detected the beacon and missing mode is on.");
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loading = true;
                                            loadingLoop();
                                            startRecognizing();
                                            error = false;
                                            counter = 0;
                                        }
                                    }, 8000);
                                }
                            }
                        }, 500);
                    } else if(done && !trilateratedPoint){
                        if(!error && text.toLowerCase().indexOf("my location".toLowerCase()) == -1 && text.toLowerCase().indexOf("beacon location".toLowerCase()) != -1 || text.toLowerCase().indexOf("my location".toLowerCase()) != -1 && text.toLowerCase().indexOf("beacon location".toLowerCase()) == -1) {
                            if (text.toLowerCase().indexOf("my location".toLowerCase()) != -1) {
                                FirebaseDatabase.getInstance().getReference().child("Voice Commands").child("jump2My" + mac).setValue("true");
                                Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                voiceCommands.this.startActivity(myIntent);
                            }
                            if (text.toLowerCase().indexOf("beacon location".toLowerCase()) != -1) {
                                FirebaseDatabase.getInstance().getReference().child("Voice Commands").child("jump2Beacon" + mac).setValue("true");
                                Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                                voiceCommands.this.startActivity(myIntent);
                            }
                        } else if(text.toLowerCase().indexOf("my location".toLowerCase()) != -1 && text.toLowerCase().indexOf("beacon location".toLowerCase()) != -1 || counter > 1){
                            error = true;
                        }
                        if(!error && counter == 1) {
                            if (text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("phone numbers".toLowerCase()) != -1 || text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("phone number".toLowerCase()) != -1) {
                                Intent myIntent = new Intent(voiceCommands.this, numberCheck.class);
                                voiceCommands.this.startActivity(myIntent);
                            }
                            if (!mac.equals("")&&text.toLowerCase().indexOf("change".toLowerCase()) != -1 && text.toLowerCase().indexOf("phone number".toLowerCase()) != -1) {
                                Intent myIntent = new Intent(voiceCommands.this, changeNumber.class);
                                voiceCommands.this.startActivity(myIntent);
                            }
                            if (!mac.equals("")&&text.toLowerCase().indexOf("recalibrate".toLowerCase()) != -1) {
                                Intent myIntent = new Intent(voiceCommands.this, recalibrate.class);
                                voiceCommands.this.startActivity(myIntent);
                            }
                            if (text.toLowerCase().indexOf("exit".toLowerCase()) != -1 || text.toLowerCase().indexOf("change beacon".toLowerCase()) != -1) {
                                Intent myIntent = new Intent(voiceCommands.this, MainActivity.class);
                                voiceCommands.this.startActivity(myIntent);
                            }
                        } else if(counter > 1){
                            error = true;
                        }
                        if (!mac.equals("")&&!error && text.toLowerCase().indexOf("on".toLowerCase()) != -1 && text.toLowerCase().indexOf("missing mode".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Switches").child("missingSwitch" + mac).setValue("on");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!mac.equals("")&&!error && text.toLowerCase().indexOf("on".toLowerCase()) != -1 && text.toLowerCase().indexOf("auto search".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Switches").child("autoSwitch" + mac).setValue("on");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!error && text.toLowerCase().indexOf("search".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1 || text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Switches").child("missingSearch" + mac).setValue("on");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!error && text.toLowerCase().indexOf("show RSSI".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Switches").child("rssiSwitch" + mac).setValue("on");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!error && text.toLowerCase().indexOf("off".toLowerCase()) != -1 && text.toLowerCase().indexOf("voice commands".toLowerCase()) != -1 || text.toLowerCase().indexOf("voice command".toLowerCase()) != -1 && text.toLowerCase().indexOf("turn off".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Switches").child("voiceSwitch" + mac).setValue("off");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!error && text.toLowerCase().indexOf("clear".toLowerCase()) != -1 && text.toLowerCase().indexOf("anonymous".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Voice Commands").child("clearAnonymous" + mac).setValue("true");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!error && text.toLowerCase().indexOf("clear".toLowerCase()) != -1 && text.toLowerCase().indexOf("found".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Voice Commands").child("clearFound" + mac).setValue("true");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!mac.equals("")&&!error && text.toLowerCase().indexOf("off".toLowerCase()) != -1 && text.toLowerCase().indexOf("missing mode".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Switches").child("missingSwitch" + mac).setValue("off");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!mac.equals("")&&!error && text.toLowerCase().indexOf("off".toLowerCase()) != -1 && text.toLowerCase().indexOf("auto search".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Switches").child("autoSwitch" + mac).setValue("off");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!error && text.toLowerCase().indexOf("stop".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1 || text.toLowerCase().indexOf("find".toLowerCase()) != -1 && text.toLowerCase().indexOf("other beacons".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Switches").child("missingSearch" + mac).setValue("off");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if (!error && text.toLowerCase().indexOf("RSSI".toLowerCase()) != -1 && text.toLowerCase().indexOf("hide".toLowerCase()) != -1 || text.toLowerCase().indexOf("RSSI".toLowerCase()) != -1 && text.toLowerCase().indexOf("stop".toLowerCase()) != -1) {
                            FirebaseDatabase.getInstance().getReference().child("Switches").child("rssiSwitch" + mac).setValue("off");
                            Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                            voiceCommands.this.startActivity(myIntent);
                        }
                        if(error) {
                            textViewFour();
                            cancelRecognizing();
                            loading = false;
                            say("Your commands could not be processed since they conflict with each other. Please try again.");
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loading = true;
                                    loadingLoop();
                                    startRecognizing();
                                    error = false;
                                    counter = 0;
                                }
                            }, 6000);
                        }

                    }
                }
            }

        }


        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.v(TAG, "lol >>> onEvent type = " + eventType);
        }
    }

        @Override
        protected void onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy();
            if (mSpeechRecognizer != null) {
                mSpeechRecognizer.destroy();
            }
            if(tts != null){
                tts.shutdown();
            }
        }

        public void init(Context context) {
            Log.d(TAG, "lol intializing mic");
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            mSpeechRecognizer.setRecognitionListener(new voiceCommands.SpeechRecognitionListener());
            startGoogleSr();
        }

        public void muteBeep() {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }

        public void resumeVolume() {
            final Handler handleri = new Handler();
            handleri.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
            }, 800);
        }

        public void startGoogleSr() {
            Log.d(TAG, "lol starting mic (outside of mSpeechRecognizer != null)");
            if (mSpeechRecognizer != null) {
                Log.d(TAG, "lol starting mic (mSpeechRecognizer != null)");
                mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
            }
        }

        void startRecognizing() {
            if (mSpeechRecognizer != null) {
                muteBeep();
                final Handler handleri = new Handler();
                handleri.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        final Handler handleri = new Handler();
                        handleri.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resumeVolume();
                            }
                        }, 800);
                    }
                }, 1300);
                Log.d(TAG, "lol Listening for Speech");
            }
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

        public void returnToMaps() {
            final Handler handleri = new Handler();
            handleri.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent myIntent = new Intent(voiceCommands.this, MapsActivity.class);
                    voiceCommands.this.startActivity(myIntent);
                }
            }, 1000);
        }

        public void loadingLoop() {
            if (loading) {
                TextView locationTv = (TextView) findViewById(R.id.debugTextview);
                locationTv.setTextColor(Color.parseColor("#FF0099CC"));
                locationTv.setText("Listening.");
            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (loading) {
                        TextView locationTv = (TextView) findViewById(R.id.debugTextview);
                        locationTv.setTextColor(Color.parseColor("#FF0099CC"));
                        locationTv.setText("Listening..");
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loading) {
                                TextView locationTv = (TextView) findViewById(R.id.debugTextview);
                                locationTv.setTextColor(Color.parseColor("#FF0099CC"));
                                locationTv.setText("Listening...");
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (loading) {
                                            loadingLoop();
                                        }
                                    }
                                }, 2000);
                            }
                        }
                    }, 1000);
                }
            }, 1000);
        }
public void textViewOne(){
    TextView locationTv = (TextView) findViewById(R.id.debugTextview);
    locationTv.setTextColor(Color.parseColor("#FF0099CC"));
    locationTv.setText("This is not a command that I understand!");
}
        public void say(String message) {
            tts.speak(message, TextToSpeech.QUEUE_ADD, null);
        }


        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                Log.d(TAG,"lll TextToSpeech successfully initalized!");
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    displayMessage("Unfortunately, text to speech is not supported.");
                    returnToMaps();
                }
            } else if(status == TextToSpeech.ERROR) {
                displayMessage("Text to speech failed to initialize.");
            }
        }
    public void roundLatLong() {
        DecimalFormat twoDForm = new DecimalFormat("##.####");
       trilateratedLat = Double.valueOf(twoDForm.format(trilateratedLat));
       trilateratedLong = Double.valueOf(twoDForm.format(trilateratedLong));
    }
    public void textViewTwo(){
        TextView locationTv = (TextView) findViewById(R.id.debugTextview);
        locationTv.setTextColor(Color.parseColor("#FF0099CC"));
        locationTv.setText("Processing commands...");
    }
    public void textViewThree(){
        TextView locationTv = (TextView) findViewById(R.id.debugTextview);
        locationTv.setTextColor(Color.parseColor("#FF0099CC"));
        locationTv.setText("Processing your other commands...");
    }
    public void textViewFour(){
        TextView locationTv = (TextView) findViewById(R.id.debugTextview);
        locationTv.setTextColor(Color.parseColor("#ffCC0000"));
        locationTv.setText("Error!");
    }
    void cancelRecognizing() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.cancel();
            Log.d(TAG,"lol canceled Speech");
        }
    }

    }




