package com.sample.bugreport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 崩溃测试页
 */
public class CrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
    }

    /**
     * 点击崩溃
     */
    public void onCrashClick(View view) {
        try {
            String girlFriend = null;
            girlFriend.toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String money = "none";
                int amount = Integer.parseInt(money);
            }
        }).start();
    }
}
