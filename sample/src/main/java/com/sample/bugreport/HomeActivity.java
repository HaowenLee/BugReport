package com.sample.bugreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     * 点击跳转
     */
    public void onJumpClick(View view) {
        startActivity(new Intent(this, CrashActivity.class));
    }

    /**
     * 点击崩溃
     */
    public void onCrashClick(View view) {
        String money = "none";
        int amount = Integer.parseInt(money);
    }
}
