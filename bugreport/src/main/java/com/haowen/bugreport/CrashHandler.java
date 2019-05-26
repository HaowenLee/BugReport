package com.haowen.bugreport;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Process;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final Context myContext;

    public CrashHandler(Context context) {
        myContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        Intent intent = new Intent(
                "com.haowen.action.CRASH",
                new Uri.Builder().scheme(exception.getClass().getSimpleName()).build()
        );
        intent.setPackage("org.sample.bugreport");

        System.out.println(thread.getName());
        System.out.println(thread.getId());

        try {
            myContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent = new Intent(myContext, BugReportActivity.class);
            intent.putExtra("Thread", thread.getName() + "(" + thread.getId() + ")");
            intent.putExtra(BugReportActivity.STACKTRACE, exception);
            myContext.startActivity(intent);
        }

        if (myContext instanceof Activity) {
            ((Activity) myContext).finish();
        }

        Process.killProcess(Process.myPid());
        System.exit(10);
    }
}