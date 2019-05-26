package com.haowen.bugreport;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BugReportActivity extends Activity {

    private TextView tvThread;
    private TextView tvException;
    private TextView tvExceptionDesc;
    private TextView tvExceptionMessage;
    private TextView reportTextView;

    public static final String STACKTRACE = "bugreport.stacktrace";

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.bug_report_view);

        findViewByIds();

        Throwable exception = (Throwable) getIntent().getSerializableExtra(STACKTRACE);
        final StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);

        tvThread.setText(getIntent().getStringExtra("Thread"));
        tvException.setText(AnalyseUtil.getCause(exception));
        tvExceptionDesc.setText(AnalyseUtil.getCauseDesc(exception));
        tvExceptionMessage.setText(exception.getMessage());
        reportTextView.setText(stackTrace.toString().replace(exception.getMessage(), ""));

        findViewById(R.id.send_report).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"haowenhello@gmail.com"});
                    sendIntent.putExtra(Intent.EXTRA_TEXT, tvExceptionMessage.getText().toString() + reportTextView.getText().toString());
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "BugReport " + "1.0.0" + " exception report");
                    sendIntent.setType("message/rfc822");
                    startActivity(sendIntent);
                } catch (ActivityNotFoundException e) {
                    // Empty
                } finally {
                    finish();
                }
            }
        });

        findViewById(R.id.cancel_report).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findViewByIds() {
        tvThread = findViewById(R.id.tvThread);
        tvException = findViewById(R.id.tvException);
        tvExceptionDesc = findViewById(R.id.tvExceptionDesc);
        tvExceptionMessage = findViewById(R.id.tvExceptionMessage);
        reportTextView = findViewById(R.id.report_text);
    }
}