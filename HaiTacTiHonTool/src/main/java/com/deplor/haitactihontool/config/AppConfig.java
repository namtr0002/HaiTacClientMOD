package com.deplor.haitactihontool.config;

import java.io.File;
import java.net.URI;
import java.util.prefs.Preferences;

public class AppConfig {
   private static final Preferences prefs = Preferences.userNodeForPackage(AppConfig.class);
   private static final String K_LANG = "language";
   private static final String K_AUTO_SAVE = "auto_save";
   private static final String K_ZOOM = "canvas_zoom";
   private static final String K_SHOW_GRID = "show_grid";
   private static final String K_GRID_SIZE = "grid_size";
   private static final String K_DATA_DIR = "data_directory";
   private static boolean autoSave = true;
   private static int canvasZoom = 100;
   private static boolean showGrid = true;
   private static int gridSize = 24;
   private static String dataDirectory = "";

   public static void load() {
      String lang = prefs.get("language", "VI");

      try {
         Lang.setLocale(Lang.Locale.valueOf(lang));
      } catch (Exception var2) {
         Lang.setLocale(Lang.Locale.VI);
      }

      autoSave = prefs.getBoolean("auto_save", true);
      canvasZoom = prefs.getInt("canvas_zoom", 100);
      showGrid = prefs.getBoolean("show_grid", true);
      gridSize = prefs.getInt("grid_size", 24);
      dataDirectory = prefs.get("data_directory", "");
   }

   public static String getDataDirectory() {
      if (dataDirectory == null || dataDirectory.isEmpty()) {
         dataDirectory = prefs.get("data_directory", "");
      }

      return dataDirectory;
   }

   public static void setDataDirectory(String val) {
      dataDirectory = val == null ? "" : val.trim();
      prefs.put("data_directory", dataDirectory);
   }

   public static String getAppDirectory() {
      String appHome = System.getProperty("app.home");
      if (appHome != null && !appHome.trim().isEmpty()) {
         return appHome;
      }

      String userDir = System.getProperty("user.dir");
      if (userDir != null && new File(userDir, "Data").exists()) {
         return userDir;
      }

      try {
         URI uri = AppConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI();
         File f = new File(uri);
         if (f.isFile() && f.getName().toLowerCase().endsWith(".jar")) {
            File parent = f.getParentFile();
            if (parent != null) {
               if (new File(parent, "Data").exists()) {
                  return parent.getAbsolutePath();
               }
               File grandParent = parent.getParentFile();
               if (grandParent != null && new File(grandParent, "Data").exists()) {
                  return grandParent.getAbsolutePath();
               }
               return parent.getAbsolutePath();
            }
         } else if (f.isDirectory()) {
            File parent = f.getParentFile();
            if (parent != null) {
               File grandParent = parent.getParentFile();
               if (grandParent != null && new File(grandParent, "Data").exists()) {
                  return grandParent.getAbsolutePath();
               }
            }
         }
      } catch (Exception var3) {
      }

      return userDir != null ? userDir : ".";
   }

   public static String getPath(String relativePath) {
      String base = getDataDirectory().trim();
      if (base.isEmpty()) {
         base = getAppDirectory();
      }

      base = base.replace('\\', '/');
      if (!base.endsWith("/")) {
         base = base + "/";
      }

      String rel = relativePath == null ? "" : relativePath.replace('\\', '/');

      while (rel.startsWith("/")) {
         rel = rel.substring(1);
      }

      if (base.toLowerCase().endsWith("/data/") && rel.toLowerCase().startsWith("data/")) {
         rel = rel.substring(5);
      }

      String full = base + rel;
      File check = new File(full);
      if (!check.exists()) {
         String userDir = System.getProperty("user.dir");
         if (userDir != null) {
            File inUserDir = new File(userDir, rel);
            if (inUserDir.exists()) {
               return inUserDir.getAbsolutePath().replace('\\', '/');
            }
         }

         String appBase = getAppDirectory().replace('\\', '/');
         if (!appBase.endsWith("/")) {
            appBase = appBase + "/";
         }

         String relClean = relativePath == null ? "" : relativePath.replace('\\', '/');

         while (relClean.startsWith("/")) {
            relClean = relClean.substring(1);
         }

         File inApp = new File(appBase + relClean);
         if (inApp.exists()) {
            return appBase + relClean;
         }
      }

      return full;
   }

   public static String getResolvedPath(String key, String defaultRelative) {
      String sub = prefs.get(key, defaultRelative).trim();
      if (sub.isEmpty()) {
         sub = defaultRelative;
      }

      File f = new File(sub);
      if (f.isAbsolute()) {
         if (f.exists()) {
            return sub.replace('\\', '/');
         }
         return getPath(defaultRelative);
      }

      return getPath(sub);
   }

   public static String getPathSetting(String key, String defaultRelative) {
      return prefs.get(key, defaultRelative);
   }

   public static void setPathSetting(String key, String val) {
      if (val == null) {
         val = "";
      }

      prefs.put(key, val.trim());
   }

   public static void setLanguage(Lang.Locale locale) {
      Lang.setLocale(locale);
      prefs.put("language", locale.name());
   }

   public static boolean isAutoSave() {
      return autoSave;
   }

   public static void setAutoSave(boolean val) {
      autoSave = val;
      prefs.putBoolean("auto_save", val);
   }

   public static int getCanvasZoom() {
      return canvasZoom;
   }

   public static void setCanvasZoom(int val) {
      canvasZoom = val;
      prefs.putInt("canvas_zoom", val);
   }

   public static boolean isShowGrid() {
      return showGrid;
   }

   public static void setShowGrid(boolean val) {
      showGrid = val;
      prefs.putBoolean("show_grid", val);
   }

   public static int getGridSize() {
      return gridSize;
   }

   public static void setGridSize(int val) {
      gridSize = val;
      prefs.putInt("grid_size", val);
   }

   static {
      load();
   }
}
