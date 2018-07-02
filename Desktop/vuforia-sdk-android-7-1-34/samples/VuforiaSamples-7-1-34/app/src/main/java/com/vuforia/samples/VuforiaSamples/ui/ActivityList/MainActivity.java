package com.vuforia.samples.VuforiaSamples.ui.ActivityList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.vuforia.Image;
import com.vuforia.samples.VuforiaSamples.R;

public class MainActivity extends Activity {

    private EditText edit_account;
    private EditText edit_password;
    private TextView score;
    private Button button_send;
    private ImageView imageView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference ref;
    private DatabaseReference ref2;

    private String mClassToLaunch;
    private String mClassToLaunchPackage;

    public static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_account = findViewById(R.id.edit_account);
        edit_password = findViewById(R.id.edit_password);
        imageView = findViewById(R.id.imageView);

        ref2 = firebaseDatabase.getReference("Image");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fire = dataSnapshot.getValue().toString();
                String[] user = fire.substring(1,fire.length()-1).split("=");
                imageView.setImageBitmap(ImageCode.imagedecode(user[1]));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        score = findViewById(R.id.score);
        ref = firebaseDatabase.getReference("score");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fire = dataSnapshot.getValue().toString();
                String[] user = fire.substring(1,fire.length()-1).split("=");
                score.setText(user[1]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref2 = firebaseDatabase.getReference("CSIE");
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String fire = dataSnapshot.getValue().toString();
                        String[] user = fire.substring(1,fire.length()-1).split("=");

                        if (edit_account.getText().length() != 0 && edit_password.getText().length() != 0) {
                            if (user[0].equals(edit_account.getText().toString()) && user[1].equals(edit_password.getText().toString())) {
                                mClassToLaunchPackage = getPackageName();
                                mClassToLaunch = mClassToLaunchPackage + "." + "app.ImageTargets.ImageTargets";
                                Intent i = new Intent();
                                i.setClassName(mClassToLaunchPackage, mClassToLaunch);
                                startActivity(i);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }


    protected void onStart() {

        super.onStart();
        flag = 0;
    }
}
