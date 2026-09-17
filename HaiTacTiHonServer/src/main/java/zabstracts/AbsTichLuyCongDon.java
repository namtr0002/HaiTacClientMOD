package zabstracts;

import model.Player;
import network.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public abstract class AbsTichLuyCongDon {
    protected int type;
    protected final List<template.TichLuyEntry> entries = new java.util.ArrayList<>();
    protected String key;
    protected String timeStart = "";
    protected String timeEnd = "";

    private static final Map<Integer, AbsTichLuyCongDon> instances = new HashMap<>();

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
        String baseKey = (key != null && !key.trim().isEmpty()) ? key : "TICH_LUY_CD";
        event.Event activeEvent = event.EventManager.gI().getActiveEvent();
        if (activeEvent != null && activeEvent.getSeasonKey() != null && !activeEvent.getSeasonKey().trim().isEmpty()) {
            return historys.HistoryManager.formatKey(baseKey, activeEvent.getSeasonKey());
        }
        return historys.HistoryManager.formatKey(baseKey, "SEASON_DEFAULT");
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    static {
        init();
    }

    public static void init() {
        instances.clear();

        try {
            AbsTichLuyCongDon inst1 = new model.TichLuyCongDon();
            instances.put(inst1.getType(), inst1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Class<?>> classes = core.ZUtil.getClasses("model");
        for (Class<?> clazz : classes) {
            if (AbsTichLuyCongDon.class.isAssignableFrom(clazz) && !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                try {
                    java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    AbsTichLuyCongDon instance = (AbsTichLuyCongDon) constructor.newInstance();
                    // putIfAbsent: không đè instance đã được đăng ký thủ công ở trên
                    instances.putIfAbsent(instance.getType(), instance);
                } catch (NoSuchMethodException e) {
                    // Ignore
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static AbsTichLuyCongDon get(int type) {
        AbsTichLuyCongDon inst = instances.get(type);
        if (inst == null) {
            inst = instances.get(4);
        }
        if (inst == null) {
            inst = new model.TichLuyCongDon();
            instances.put(4, inst);
        }
        return inst;
    }

    public int getType() {
        return type;
    }

    public List<template.TichLuyEntry> getEntries() {
        return entries;
    }

    public abstract void showTable(Player p) throws IOException;
    public abstract void process(Player p, Message m) throws IOException;
}