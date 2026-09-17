package core;

import database.DbManager;
import org.joda.time.DateTime;
import template.Top_Dame;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.joda.time.LocalTime;
import java.text.SimpleDateFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ZUtil {
    public static final SimpleDateFormat fmt_save_log = new SimpleDateFormat("dd_MM_yyyy");
    private static final Random random = new Random();

    /**
     * Tạo chuỗi token MD5 cho các phiên/đợt phó bản (dùng định danh, trao quà, chống trùng lặp).
     */
    public static String generateMD5Token(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return Long.toHexString(System.currentTimeMillis()) + "_" + Integer.toHexString(input.hashCode());
        }
    }

    private static final java.util.concurrent.ConcurrentHashMap<String, byte[]> FILE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    public static void clearFileCache() {
        FILE_CACHE.clear();
    }

    public static byte[] loadfile(String url) throws IOException {
        if (url == null || url.isEmpty()) {
            return new byte[0];
        }
        String key = url.replace('\\', '/');
        byte[] cached = FILE_CACHE.get(key);
        if (cached != null) {
            return cached;
        }

        java.io.File file = new java.io.File(url);
        if (!file.exists() || !file.isFile()) {
            if (url.startsWith("data/")) {
                java.io.File relFile = new java.io.File("release/" + url);
                if (relFile.exists() && relFile.isFile()) {
                    file = relFile;
                }
            } else if (url.startsWith("release/data/")) {
                java.io.File rootFile = new java.io.File(url.substring("release/".length()));
                if (rootFile.exists() && rootFile.isFile()) {
                    file = rootFile;
                }
            }
        }
        if (!file.exists() || !file.isFile()) {
            throw new java.io.FileNotFoundException("File not found: " + url);
        }

        byte[] data = java.nio.file.Files.readAllBytes(file.toPath());
        FILE_CACHE.put(key, data);
        return data;
    }

    public static String get_time_str_by_sec2(long time_ship) {
        time_ship /= 1000;
        int input = (int) time_ship;
        int numberOfDays;
        int numberOfHours;
        int numberOfMinutes;
        int numberOfSeconds;
        numberOfDays = input / 86400;
        numberOfHours = (input % 86400) / 3600;
        numberOfMinutes = ((input % 86400) % 3600) / 60;
        numberOfSeconds = ((input % 86400) % 3600) % 60;
        return String.format("%sd %sh %sp %ss", numberOfDays, numberOfHours, numberOfMinutes,
                numberOfSeconds);
    }

    public static boolean is_DayofWeek(int day) {
        // thu2 = 1 ->
        // thu3 = 2->
        // thu4 = 3 ->
        // thu5 = 4 ->
        // thu6 = 5 ->
        // thu7 = 6 ->
        // chu nhat = 7
        DateTime dateTime = DateTime.now();
        return dateTime.getDayOfWeek() == day;
    }

    public static int random(int a1, int a2) {
        if (a1 >= a2) {
            return a1;
        }
        return ThreadLocalRandom.current().nextInt(a1, a2);
    }

    public static int random(int a2) {
        if (a2 <= 0) {
            return 0;
        }
        return ThreadLocalRandom.current().nextInt(a2);
    }

    /**
     * Quay ngẫu nhiên thuộc tính Kích Ẩn (0 đến 12, tương ứng Ẩn 1 đến Ẩn 13)
     * Đảm bảo phân phối đồng đều 100% (mỗi loại có tỷ lệ chuẩn xác 1/13).
     */
    public static byte rollKichAn() {
        return (byte) ThreadLocalRandom.current().nextInt(13);
    }

    public static boolean isnumber(String txt) {
        try {
            Integer.valueOf(txt);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean is_same_day(DateTime now, DateTime d) {
        String strDate_1 = now.toString().split("T")[0];
        String strDate_2 = d.toString().split("T")[0];
        return strDate_1.equals(strDate_2);
    }

    public synchronized static List<Top_Dame> sort(List<Top_Dame> list_select) {
        List<Top_Dame> result = new ArrayList<>(list_select);
        result.sort((o1, o2) -> Long.compare(o2.dame, o1.dame));
        if (result.size() > 10) {
            result = new ArrayList<>(result.subList(0, 10));
        }
        return result;
    }

    public static String number_format(long n) {
        return (NumberFormat.getInstance(Locale.ITALY).format(n));
    }

    public static LocalTime get_local_time_now() {
        return LocalTime.now();
    }

    public static String str_time_now(int type) {
        if (type == 1) {
            return get_local_time_now().toString();
        } else {
            String[] result = get_date_time_now().toString().split("T");
            return result[0] + " " + result[1].split("\\+")[0];
        }
    }
    
     public static DateTime get_date_time_now() {
        return DateTime.now();
    }
       private static int getDayInt(String dayStr) {
    switch (dayStr) {
      case "MON":
        return 1;
      case "TUE":
        return 2;
      case "WED":
        return 3;
      case "THU":
        return 4;
      case "FRI":
        return 5;
      case "SAT":
        return 6;
      default: // SUN
        return 7;
    }
  }
        public static boolean is_DayofWeek(String dayStr) {
    // thu2 = 1 ->
    // thu3 = 2->
    // thu4 = 3 ->
    // thu5 = 4 ->
    // thu6 = 5 ->
    // thu7 = 6 ->
    // chu nhat = 7
    DateTime dateTime = DateTime.now();
    return dateTime.getDayOfWeek() == getDayInt(dayStr);
  }
        public static final Object NULL = new Null();
        public static boolean isNull(JSONArray arr, int index) {
            return NULL.equals(opt(arr, index));
        }

    public static int getSkillIconOffset(int rawIconId) {
        if (rawIconId < 0) {
            return -1;
        }
        if (rawIconId >= 4000) {
            return rawIconId - 4000;
        }
        return rawIconId;
    }

    public static int getInt(JSONObject obj, String key, int def) {
        Object v = obj.get(key);
        if (v == null) {
            return def;
        }
        try {
            return Integer.parseInt(String.valueOf(v));
        } catch (Exception e) {
            return def;
        }
    }
        
        private static final class Null {

            @Override
            protected final Object clone() {
                return this;
            }

            @Override
            public boolean equals(Object object) {
                return object == null || object == this;
            }

            @Override
            public String toString() {
                return "null";
            }
        }
        
        private static Object opt(JSONArray arr, int index) {
            return (index < 0 || index >= arr.size()) ? null : arr.get(index);
        }

        public static <T> List<Class<? extends T>> findSubclasses(Class<T> parentClass) {
            List<Class<? extends T>> result = new ArrayList<>();
            try {
                java.net.URL location = parentClass.getProtectionDomain().getCodeSource().getLocation();
                if (location == null) {
                    location = ZUtil.class.getProtectionDomain().getCodeSource().getLocation();
                }
                if (location != null) {
                    java.io.File codeSourceFile = new java.io.File(location.toURI());
                    if (codeSourceFile.isDirectory()) {
                        scanDirectoryForSubclasses(codeSourceFile, "", parentClass, result);
                    } else if (codeSourceFile.isFile() && codeSourceFile.getName().endsWith(".jar")) {
                        scanJarForSubclasses(codeSourceFile, parentClass, result);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        private static <T> void scanDirectoryForSubclasses(java.io.File dir, String packageName, Class<T> parentClass, List<Class<? extends T>> result) {
            java.io.File[] files = dir.listFiles();
            if (files == null) return;
            for (java.io.File file : files) {
                if (file.isDirectory()) {
                    String subPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                    scanDirectoryForSubclasses(file, subPackage, parentClass, result);
                } else if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                    String className = (packageName.isEmpty() ? "" : packageName + ".") + file.getName().substring(0, file.getName().length() - 6);
                    checkAndAddSubclass(className, parentClass, result);
                }
            }
        }

        private static <T> void scanJarForSubclasses(java.io.File jarFile, Class<T> parentClass, List<Class<? extends T>> result) {
            try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
                java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    java.util.jar.JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.endsWith(".class") && !name.contains("$")) {
                        String className = name.replace('/', '.').substring(0, name.length() - 6);
                        checkAndAddSubclass(className, parentClass, result);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @SuppressWarnings("unchecked")
        private static <T> void checkAndAddSubclass(String className, Class<T> parentClass, List<Class<? extends T>> result) {
            try {
                Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
                if (parentClass.isAssignableFrom(clazz)
                        && !clazz.isInterface()
                        && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                    result.add((Class<? extends T>) clazz);
                }
            } catch (Throwable ignored) {
            }
        }

        public static List<Class<?>> getClasses(String packageName) {
            List<Class<?>> classes = new ArrayList<>();
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                String path = packageName.replace('.', '/');
                java.util.Enumeration<java.net.URL> resources = classLoader.getResources(path);
                while (resources.hasMoreElements()) {
                    java.net.URL resource = resources.nextElement();
                    java.net.URLConnection connection = resource.openConnection();
                    if (connection instanceof java.net.JarURLConnection) {
                        java.net.JarURLConnection jarConnection = (java.net.JarURLConnection) connection;
                        java.util.jar.JarFile jarFile = jarConnection.getJarFile();
                        java.util.Enumeration<java.util.jar.JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            java.util.jar.JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                                String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                                try {
                                    classes.add(Class.forName(className));
                                } catch (ClassNotFoundException e) {
                                    // Ignore
                                }
                            }
                        }
                    } else {
                        try {
                            java.net.URI uri = resource.toURI();
                            java.io.File directory = new java.io.File(uri);
                            classes.addAll(findClasses(directory, packageName));
                        } catch (Exception ex) {
                            String filePath = resource.getPath().replace("%20", " ");
                            java.io.File directory = new java.io.File(filePath);
                            classes.addAll(findClasses(directory, packageName));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return classes;
        }

        private static List<Class<?>> findClasses(java.io.File directory, String packageName) {
            List<Class<?>> classes = new ArrayList<>();
            if (!directory.exists()) {
                return classes;
            }
            java.io.File[] files = directory.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    if (file.isDirectory()) {
                        classes.addAll(findClasses(file, packageName + "." + file.getName()));
                    } else if (file.getName().endsWith(".class")) {
                        try {
                            classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                        } catch (ClassNotFoundException e) {
                            // Ignore
                        }
                    }
                }
            }
            return classes;
        }
}
