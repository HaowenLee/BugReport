package com.haowen.bugreport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Process;

import com.haowen.bugreport.internal.BugReportActivity;

/**
 * 异常捕获
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    @SuppressLint("StaticFieldLeak")
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        Intent intent = new Intent(
                "com.haowen.action.CRASH",
                new Uri.Builder().scheme(exception.getClass().getSimpleName()).build()
        );
        intent.setPackage("com.sample.bugreport");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        StackTraceElement[] stackTrace = exception.getStackTrace();
        for (StackTraceElement s:stackTrace){
            System.out.println(s.getMethodName());
        }

        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent = new Intent(mContext, BugReportActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Thread", thread.getName() + "(" + thread.getId() + ")");
            intent.putExtra(BugReportActivity.STACKTRACE, exception);
            mContext.startActivity(intent);
        }

        if (mContext instanceof Activity) {
            ((Activity) mContext).finish();
        }

        Process.killProcess(Process.myPid());
        System.exit(10);
    }
}