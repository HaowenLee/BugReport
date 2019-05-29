package com.haowen.bugreport.internal;

class CrashUtils {

    /**
     * 获取造成异常
     *
     * @param exception 异常
     * @return 造成异常
     */
    static Throwable getCauseThrowable(Throwable exception) {
        if (exception == null) {
            return null;
        }
        while (exception.getCause() != null) {
            exception = exception.getCause();
        }
        return exception;
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