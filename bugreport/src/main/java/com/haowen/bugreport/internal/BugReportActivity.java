package com.haowen.bugreport.internal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haowen.bugreport.R;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 崩溃信息显示
 */
public class BugReportActivity extends Activity {

    public static final String STACKTRACE = "bugreport.stacktrace";
    private ScrollView scrollView;
    private TextView tvThread;
    private TextView tvException;
    private TextView tvExceptionDesc;
    private TextView tvReportDetail;
    private Button btnSend;
    private Button btnCancel;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.com_haowen_activity_bug_report);

        findViewByIds();
        initListener();
        setCrashDetail2UI();
    }

    /**
     * findViewById
     */
    private void findViewByIds() {
        scrollView = findViewById(R.id.scrollView);
        tvThread = findViewById(R.id.tvThread);
        tvException = findViewById(R.id.tvException);
        tvExceptionDesc = findViewById(R.id.tvExceptionDesc);
        tvReportDetail = findViewById(R.id.tvReportDetail);
        btnSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.btnCancel);
    }

    /**
     * 监听按钮点击事件
     */
    private void initListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtils.shareImage(BugReportActivity.this, scrollView);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 设置崩溃信息到UI
     */
    private void setCrashDetail2UI() {
        Throwable exception = (Throwable) getIntent().getSerializableExtra(STACKTRACE);

        final StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        tvThread.setText(getIntent().getStringExtra("Thread"));
        tvException.setText(CrashUtils.getCause(exception));
        tvExceptionDesc.setText(CrashUtils.getCauseDesc(exception));

        tvReportDetail.setText(CrashUtils.getCrashInfo(exception, Utils.getPackageName(this)));
    }
}