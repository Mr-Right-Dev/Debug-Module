package com.debug;

public class Debugger {
    public static void success(String message) {
        Logger.log(OutputType.success, message);
    }

    public static void log(String message) {
        Logger.log(OutputType.log, message);
    }

    public static void warn(String message) {
        Logger.log(OutputType.warn, message);
    }

    public static void warn(String message, Throwable throwable) {
        Logger.log(OutputType.warn, message, throwable);
    }

    public static void error(String message) {
        Logger.log(OutputType.error, message);
    }

    public static void error(String message, Throwable throwable) {
        Logger.log(OutputType.error, message, throwable);
    }

    public static void critical(String message, Throwable throwable) {
        Logger.log(OutputType.critical, message, throwable);
    }
}