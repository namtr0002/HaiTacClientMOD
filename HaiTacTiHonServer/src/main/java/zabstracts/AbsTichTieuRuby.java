package zabstracts;

import model.Player;
import network.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public abstract class AbsTichTieuRuby {
    protected int type;
    protected String key;
    protected String timeStart = "";
    protected String timeEnd = "";
    protected final List<template.TichLuyEntry> entries = new java.util.ArrayList<>();

    public void timeStart(String time) {
        if (time != null) this.timeStart = time.trim();
    }

    public void timeEnd(String time) {
        if (time != null) this.timeEnd = time.trim();
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public static java.util.Date parseDate(String str, boolean isStart) {
        if (str == null || str.trim().isEmpty()) return null;
        str = str.trim();
        String[] patterns = new String[]{
            "H:mm:ss d/M/yyyy",
            "H:mm d/M/yyyy",
            "d/M/yyyy H:mm:ss",
            "d/M/yyyy H:mm",
            "d/M/yyyy",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd"
        };
        for (String pattern : patterns) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
                sdf.setLenient(true);
                java.util.Date date = sdf.parse(str);
                if (!pattern.contains("H") && !pattern.contains("m")) {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(date);
                    if (isStart) {
                        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
                        cal.set(java.util.Calendar.MINUTE, 0);
                        cal.set(java.util.Calendar.SECOND, 0);
                        cal.set(java.util.Calendar.MILLISECOND, 0);
                    } else {
                        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
                        cal.set(java.util.Calendar.MINUTE, 59);
                        cal.set(java.util.Calendar.SECOND, 59);
                        cal.set(java.util.Calendar.MILLISECOND, 999);
                    }
                    return cal.getTime();
                }
                return date;
            } catch (Exception ignored) {}
        }
        return null;
    }

    public boolean isRunning() {
        if (timeStart != null && !timeStart.isEmpty() && timeEnd != null && !timeEnd.isEmpty()) {
            try {
                java.util.Date start = parseDate(timeStart, true);
                java.util.Date end = parseDate(timeEnd, false);
                if (start != null && end != null) {
                    long now = System.currentTimeMillis();
                    return now >= start.getTime() && now <= end.getTime();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean checkTimeActive() {
        return isRunning();
    }

    public String getSeasonKey() {
        return key != null ? key : "TichTieuRuby_" + type;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    public List<template.TichLuyEntry> getEntries() {
        return entries;
    }

    private static final Map<Integer, AbsTichTieuRuby> instances = new HashMap<>();

    static {
        init();
    }

    public static void init() {
        instances.clear();

        try {
            AbsTichTieuRuby inst1 = new model.TichTieuRuby();
            instances.put(inst1.getType(), inst1);
            AbsTichTieuRuby inst2 = new model.TichTieuRubySuKien();
            instances.put(inst2.getType(), inst2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Class<?>> classes = core.ZUtil.getClasses("model");
        for (Class<?> clazz : classes) {
            if (AbsTichTieuRuby.class.isAssignableFrom(clazz) && !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                try {
                    java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    AbsTichTieuRuby instance = (AbsTichTieuRuby) constructor.newInstance();
                    instances.put(instance.getType(), instance);
                } catch (NoSuchMethodException e) {
                    // Ignore
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static AbsTichTieuRuby get(int type) {
        AbsTichTieuRuby inst = instances.get(type);
        if (inst == null) {
            if (type == 1) {
                inst = new model.TichTieuRuby();
                instances.put(1, inst);
            } else if (type == 4) {
                inst = new model.TichTieuRubySuKien();
                instances.put(4, inst);
            } else if (type == 5) {
                inst = new model.TichTieuTuan();
                instances.put(5, inst);
            } else if (type == 6) {
                inst = new model.TichTieuTong();
                instances.put(6, inst);
            }
        }
        return inst;
    }

    public int getType() {
        return type;
    }

    public abstract void showTable(Player p) throws IOException;
    public abstract void process(Player p, Message m) throws IOException;
}