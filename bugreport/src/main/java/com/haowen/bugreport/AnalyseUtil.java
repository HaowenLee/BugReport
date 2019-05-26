package com.haowen.bugreport;

public class AnalyseUtil {

    public static String getCause(Throwable exception) {
        if (exception == null) {
            return "Unknown Exception";
        }
        while (exception.getCause() != null) {
            exception = exception.getCause();
        }
        return exception.getClass().getName();
    }

    public static String getCauseDesc(Throwable exception) {
        if (exception == null) {
            return "Unknown Exception";
        }
        while (exception.getCause() != null) {
            exception = exception.getCause();
        }
        return exception.getMessage();
    }
}
