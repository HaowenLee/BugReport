package com.haowen.bugreport.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.TextView;

import com.haowen.bugreport.Config;
import com.haowen.bugreport.R;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BugReportActivity extends Activity {

    public static final String STACKTRACE = "bugreport.stacktrace";
    private TextView tvThread;
    private TextView tvException;
    private TextView tvExceptionDesc;
    private TextView reportTextView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.com_haowen_activity_bug_report);

        findViewByIds();

        Throwable exception = (Throwable) getIntent().getSerializableExtra(STACKTRACE);

        Throwable causeThrowable = CrashUtils.getCauseThrowable(exception);
        final StringWriter stackTrace = new StringWriter();
        causeThrowable.printStackTrace(new PrintWriter(stackTrace));

        tvThread.setText(getIntent().getStringExtra("Thread"));
        tvException.setText(CrashUtils.getCause(exception));
        tvExceptionDesc.setText(CrashUtils.getCauseDesc(exception));

        SpannableStringBuilder builder = new SpannableStringBuilder();

        // 异常message
        builder.append(highlightSpan(exception.getMessage()))
                .append("\n......")
                .append("\nCaused by:")
                .append("\n");

        String content = stackTrace.toString();
        builder.append(content);

        reportTextView.setText(builder);

        findViewById(R.id.send_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Config.DEVELOPER_EMAIL});
                    sendIntent.putExtra(Intent.EXTRA_TEXT, reportTextView.getText().toString());
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, Utils.getAppInfo(BugReportActivity.this));
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
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findViewByIds() {
        tvThread = findViewById(R.id.tvThread);
        tvException = findViewById(R.id.tvException);
        tvExceptionDesc = findViewById(R.id.tvExceptionDesc);
        reportTextView = findViewById(R.id.report_text);
    }

    private SpannableString highlightSpan(String content) {
        SpannableString highlightSpan = new SpannableString(content);
        ForegroundColorSpan textColorSpan = new ForegroundColorSpan(0xff42a5f5);
        TypefaceSpan typefaceSpan = new TypefaceSpan("monospace");
        highlightSpan.setSpan(textColorSpan, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        highlightSpan.setSpan(typefaceSpan, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return highlightSpan;
    }
}