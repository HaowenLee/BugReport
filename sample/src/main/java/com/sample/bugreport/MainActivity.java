package com.sample.bugreport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.haowen.bugreport.CrashHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));

        String a = null;
        a.toString();
    }
}
