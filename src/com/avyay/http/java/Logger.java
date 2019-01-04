package com.avyay.http.java;

public class Logger {
    private boolean enabled;

    public Logger(boolean enabled) {
        this.enabled = enabled;
    }

    public Logger() {
        this(true);
    }

    public static String concat(Object... args) {
        String out = "";
        for (Object arg: args) {
            out += String.valueOf(arg) + " ";
        }
        return out;
    }

    public static String concatNoSpaces(Object... args) {
        String out = "";
        for (Object arg: args) {
            out += String.valueOf(arg);
        }
        return out;
    }

    public void log(Object... args) {
        System.out.println(Logger.concat(args));
    }

    public void error(Object... args) {
        System.err.println(Logger.concat(args));
    }
}
