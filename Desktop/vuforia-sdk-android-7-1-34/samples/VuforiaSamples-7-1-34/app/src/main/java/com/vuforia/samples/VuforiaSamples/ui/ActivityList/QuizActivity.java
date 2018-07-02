package com.vuforia.samples.VuforiaSamples.ui.ActivityList;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vuforia.samples.VuforiaSamples.R;

import java.util.HashMap;

public class QuizActivity extends Activity implements RadioGroup.OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private RadioButton radioButton_01;
    private RadioButton radioButton_02;
    private RadioButton radioButton_03;
    private RadioButton radioButton_04;
    private Button button;
    private TextView textView;

    private int userAnswer = 0;
    private int correctAnswer = 3;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myref;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private LocationManager status;

    private Handler handler;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            Log.d("zxc","jih");
            mGoogleApiClient.connect();
        }

        if(status.isProviderEnabled(LocationManager.GPS_PROVIDER)&& status.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            if(mGoogleApiClient != null){
                if(mGoogleApiClient.isConnected()){
                    getMyLocation();
                }else{
                    Toast.makeText(QuizActivity.this,
                            "!mGoogleApiClient.isConnected()", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(QuizActivity.this,
                        "mGoogleApiClient == null", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(QuizActivity.this,"請開啟定位",Toast.LENGTH_LONG).show();
        }

        init();

        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);

        rg.setOnCheckedChangeListener(this);

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                myref = firebaseDatabase.getReference("score");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("CSIE",count+1);
                myref.updateChildren(hashMap);
            }
        };

    }


    private void init(){
        radioButton_01 = (RadioButton)findViewById(R.id.radioButton);
        radioButton_02 = (RadioButton)findViewById(R.id.radioButton2);
        radioButton_03 = (RadioButton)findViewById(R.id.radioButton3);
        radioButton_04 = (RadioButton)findViewById(R.id.radioButton4);
        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);

        textView.setText("下列何者不是自由軟體？");
        radioButton_01.setText("A: Libreoffice");
        radioButton_02.setText("B: EZGO");
        radioButton_03.setText("C: Microsoft Word");
        radioButton_04.setText("D: SCRATCH");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userAnswer != 0){
                    if (userAnswer == correctAnswer){
                        Toast.makeText(QuizActivity.this,"恭喜答對",Toast.LENGTH_LONG).show();
                        myref = firebaseDatabase.getReference("score");
                        myref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String fire = dataSnapshot.getValue().toString();
                                String[] user = fire.substring(1,fire.length()-1).split("=");
                                count = Integer.parseInt(user[1]);
                                handler.sendEmptyMessage(0);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        finish();
                    }else {
                        Toast.makeText(QuizActivity.this,"恭喜答錯",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(QuizActivity.this,"請回答問題",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void getMyLocation(){
        try{
            /* code should explicitly check to see if permission is available
            (with 'checkPermission') or explicitly handle a potential 'SecurityException'
             */
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                Toast.makeText(QuizActivity.this,
                        String.valueOf(mLastLocation.getLatitude()) + "\n"
                                + String.valueOf(mLastLocation.getLongitude()),
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(QuizActivity.this,
                        "mLastLocation == null",
                        Toast.LENGTH_LONG).show();
            }
        } catch (SecurityException e){
            Toast.makeText(QuizActivity.this,
                    "SecurityException:\n" + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        //依選取項目顯示不同訊息
        switch(i){
            case R.id.radioButton:
                userAnswer = 1;
                break;
            case R.id.radioButton2:
                userAnswer = 2;
                break;
            case R.id.radioButton3:
                userAnswer = 3;
                break;
            case R.id.radioButton4:
                userAnswer = 4;
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
