package com.haowen.bugreport.internal;

import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

class CrashUtils {

    /**
     * 崩溃信息
     *
     * @param throwable 异常
     * @return 崩溃信息
     */
    static SpannableStringBuilder getCrashInfo(Throwable throwable, String highlightContent) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
        dejaVu.add(throwable);

        builder.append(highlightSpan(throwable.toString())).append("\n");

        // Print our stack trace
        StackTraceElement[] trace = throwable.getStackTrace();
        for (StackTraceElement traceElement : trace) {
            if (traceElement.toString().contains(highlightContent)) {
                builder.append("\tat ");
                printStackTraceElement(traceElement, builder);
                builder.append("\n");
            } else {
                builder.append("\tat ").append(traceElement.toString()).append("\n");
            }
        }

        // Print suppressed exceptions, if any
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (Throwable se : throwable.getSuppressed())
                printEnclosedStackTrace(se, builder, trace, "Suppressed: ", "\t", dejaVu, highlightContent);
        }

        // Print cause, if any
        Throwable ourCause = throwable.getCause();
        if (ourCause != null) {
            printEnclosedStackTrace(ourCause, builder, trace, "Caused by: ", "", dejaVu, highlightContent);
        }
        return builder;
    }

    private static void printEnclosedStackTrace(Throwable throwable,
                                                SpannableStringBuilder builder,
                                                StackTraceElement[] enclosingTrace,
                                                String caption,
                                                String prefix,
                                                Set<Throwable> dejaVu,
                                                String highlightPackage) {
        if (dejaVu.contains(throwable)) {
            builder.append("\t[CIRCULAR REFERENCE:").append(throwable.toString()).append("]\n");
        } else {
            dejaVu.add(throwable);
            // Compute number of frames in common between this and enclosing trace
            StackTraceElement[] trace = throwable.getStackTrace();
            int m = trace.length - 1;
            int n = enclosingTrace.length - 1;
            while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
                m--;
                n--;
            }
            int framesInCommon = trace.length - 1 - m;

            // Print our stack trace
            builder.append(prefix).append(caption).append(throwable.toString()).append("\n");
            for (int i = 0; i <= m; i++) {
                if (trace[i].toString().contains(highlightPackage)) {
                    builder.append("\tat ");
                    printStackTraceElement(trace[i], builder);
                    builder.append("\n");
                } else {
                    builder.append(prefix).append("\tat ").append(trace[i].toString()).append("\n");
                }
            }
            if (framesInCommon != 0) {
                builder.append(prefix).append("\t... ").append(String.valueOf(framesInCommon)).append(" more\n");
            }

            // Print suppressed exceptions, if any
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (Throwable se : throwable.getSuppressed()) {
                    printEnclosedStackTrace(se, builder, trace, "Suppressed: ", "\t", dejaVu, highlightPackage);
                }
            }

            // Print cause, if any
            Throwable ourCause = throwable.getCause();
            if (ourCause != null) {
                printEnclosedStackTrace(ourCause, builder, trace, "Caused by: ", prefix, dejaVu, highlightPackage);
            }
        }
    }

    /**
     * 输出栈元素信息
     *
     * @param traceElement 栈元素
     * @param builder      崩溃信息
     */
    private static void printStackTraceElement(StackTraceElement traceElement, SpannableStringBuilder builder) {
        // Android-changed: When ART cannot find a line number, the lineNumber field is set
        // to the dex_pc and the fileName field is set to null.
        builder.append(traceElement.getClassName()).append(".").append(traceElement.getMethodName());
        if (traceElement.isNativeMethod()) {
            builder.append("(Native Method)");
        } else if (traceElement.getFileName() != null) {
            if (traceElement.getLineNumber() >= 0) {
                builder.append("(")
                        .append(highlightSpan(traceElement.getFileName()))
                        .append(highlightSpan(":"))
                        .append(highlightSpan(String.valueOf(traceElement.getLineNumber())))
                        .append(")");
            } else {
                builder.append("(").append(traceElement.getFileName()).append(")");
            }
        } else {
            if (traceElement.getLineNumber() >= 0) {
                // The line number is actually the dex pc.
                builder.append("(Unknown Source:").append(String.valueOf(traceElement.getLineNumber())).append(")");
            } else {
                builder.append("(Unknown Source)");
            }
        }
    }

    /**
     * 高亮内容
     *
     * @param content 内容
     * @return SpannableString对象
     */
    private static SpannableString highlightSpan(String content) {
        SpannableString highlightSpan = new SpannableString(content);
        ForegroundColorSpan textColorSpan = new ForegroundColorSpan(0xff42a5f5);
        TypefaceSpan typefaceSpan = new TypefaceSpan("monospace");
        highlightSpan.setSpan(textColorSpan, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        highlightSpan.setSpan(typefaceSpan, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return highlightSpan;
    }

    /**
     * 获取异常类名
     *
     * @param exception 异常
     * @return 异常类名
     */
    static String getCause(Throwable exception) {
        if (exception == null) {
            return "Unknown Exception";
        }
        while (exception.getCause() != null) {
            exception = exception.getCause();
        }
        return exception.getClass().getName();
    }

    /**
     * 获取异常描述
     *
     * @param exception 异常
     * @return 异常描述
     */
    static String getCauseDesc(Throwable exception) {
        if (exception == null) {
            return "Unknown Exception";
        }
        while (exception.getCause() != null) {
            exception = exception.getCause();
        }
        return exception.getMessage();
    }
}