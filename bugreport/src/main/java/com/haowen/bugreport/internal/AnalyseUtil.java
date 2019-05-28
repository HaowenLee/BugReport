package com.haowen.bugreport.internal;

class AnalyseUtil {

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
