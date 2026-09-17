package core;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static String timeStamp() {
        return "[" + LocalTime.now().format(TIME_FMT);
    }

    public static void info(String tag, String msg) {
        System.out.println(timeStamp() + " INFO] [" + tag + "] " + msg);
    }

    public static void success(String tag, String msg) {
        System.out.println(timeStamp() + " INFO] [" + tag + "] " + msg);
    }

    public static void warn(String tag, String msg) {
        System.out.println(timeStamp() + " WARN] [" + tag + "] " + msg);
    }

    public static void warning(String tag, String msg) {
        warn(tag, msg);
    }

    public static void error(String tag, String msg) {
        System.err.println(timeStamp() + " ERROR] [" + tag + "] " + msg);
    }

    public static void error(String tag, String msg, Throwable t) {
        System.err.println(timeStamp() + " ERROR] [" + tag + "] " + msg);
        if (t != null) {
            t.printStackTrace();
        }
    }

    public static void step(String name, String detail) {
        System.out.println(timeStamp() + " INFO] [Templates] Loaded " + name + ": " + detail);
    }

    public static void stepLast(String name, String detail) {
        System.out.println(timeStamp() + " INFO] [Templates] Loaded " + name + ": " + detail);
    }

    public static void debug(String tag, String msg) {
        if (Manager.gI() != null && Manager.gI().debug) {
            System.out.println(timeStamp() + " DEBUG] [" + tag + "] " + msg);
        }
    }

    public static void banner() {
        
    }

    public static void summary(int port, long bootTimeMs) {
        System.out.println(timeStamp() + " INFO] [ServerManager] Server successfully started in " + bootTimeMs + " ms! Listening on port " + port + ".");
    }
}
