package com.vuforia.samples.VuforiaSamples.ui.ActivityList;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vuforia.samples.VuforiaSamples.R;

public class Main2Activity extends Activity {

    private String mClassToLaunch;
    private String mClassToLaunchPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mClassToLaunchPackage = getPackageName();
        mClassToLaunch = mClassToLaunchPackage + "." + "app.ImageTargets.ImageTargets";
        Intent i = new Intent();
        i.setClassName(mClassToLaunchPackage, mClassToLaunch);
        startActivity(i);
    }
}
