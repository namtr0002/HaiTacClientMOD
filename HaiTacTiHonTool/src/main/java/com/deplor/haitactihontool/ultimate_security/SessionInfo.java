package com.deplor.haitactihontool.ultimate_security;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.SwingUtilities;

public final class SessionInfo {
   public static final String TYPE_MAP = "map";
   public static final String TYPE_PART = "part";
   public static final String TYPE_EFFECT = "effect";
   public static final String TYPE_EFFECTAUTO = "effectauto";
   public static final String TYPE_SKILL = "skill";
   public static final String TYPE_ADMIN = "admin";
   public static final String[] ALL_TOOLS = new String[]{"MAP", "PART", "PET", "MOB", "EFFECT", "EFFECT AUTO", "LEAK RES", "SKILL", "TOOL"};
   private static Runnable onSessionUpdatedListener = null;
   private static volatile String licenseType = "";
   private static volatile String note = "";
   private static volatile String startStr = "";
   private static volatile String endStr = "";
   private static volatile LocalDateTime expiryTime = null;
   private static volatile boolean valid = false;

   private SessionInfo() {
   }

   public static void setOnSessionUpdatedListener(Runnable listener) {
      onSessionUpdatedListener = listener;
   }

   public static void activate(String type, String start, String end, String note) {
      licenseType = type != null ? type.trim().toLowerCase() : "";
      startStr = start != null ? start.trim() : "";
      endStr = end != null ? end.trim() : "";
      SessionInfo.note = note != null ? note.trim() : "";
      valid = true;
      expiryTime = parseToLocalDateTime(endStr);
      if (onSessionUpdatedListener != null) {
         SwingUtilities.invokeLater(onSessionUpdatedListener);
      }
   }

   public static LocalDateTime parseToLocalDateTime(String dateStr) {
      if (dateStr != null && !dateStr.trim().isEmpty()) {
         String s = dateStr.trim();
         if (!s.equalsIgnoreCase("unlimited")
            && !s.equalsIgnoreCase("forever")
            && !s.equalsIgnoreCase("never")
            && !s.equalsIgnoreCase("vinhvien")
            && !s.contains("vĩnh viễn")) {
            String[] patterns = new String[]{
               "yyyy-MM-dd HH:mm:ss",
               "yyyy-MM-dd HH:mm",
               "yyyy-MM-dd",
               "dd/MM/yyyy HH:mm:ss",
               "dd/MM/yyyy HH:mm",
               "dd/MM/yyyy",
               "HH:mm dd/MM/yyyy",
               "HH:mm:ss dd/MM/yyyy"
            };

            for (String pattern : patterns) {
               try {
                  DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
                  if (!pattern.equals("yyyy-MM-dd") && !pattern.equals("dd/MM/yyyy")) {
                     return LocalDateTime.parse(s, dtf);
                  }

                  LocalDate date = LocalDate.parse(s, dtf);
                  return date.atTime(23, 59, 59);
               } catch (Exception var9) {
               }
            }

            return null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void invalidate() {
      valid = false;
      licenseType = "";
      expiryTime = null;
   }

   public static boolean isExpired() {
      return expiryTime == null ? false : LocalDateTime.now().isAfter(expiryTime);
   }

   public static long minutesRemaining() {
      if (expiryTime == null) {
         return Long.MAX_VALUE;
      } else {
         Duration d = Duration.between(LocalDateTime.now(), expiryTime);
         return d.isNegative() ? 0L : d.toMinutes();
      }
   }

   public static boolean canUseTool(String toolCard) {
      if (!valid || isExpired()) {
         return false;
      } else if (licenseType != null && !licenseType.isEmpty()) {
         String normLicense = licenseType.toLowerCase().replace(" ", "");
         if (!"admin".equals(normLicense)
            && !"all".equals(normLicense)
            && !"full".equals(normLicense)
            && !"vip".equals(normLicense)
            && !"*".equals(normLicense)) {
            if (toolCard == null) {
               return false;
            } else {
               String requestedTool = toolCard.toLowerCase().replace(" ", "");
               String[] allowedTools = normLicense.split(",");
               String[] var4 = allowedTools;
               int var5 = allowedTools.length;
               int var6 = 0;

               while (var6 < var5) {
                  String allowedTool = var4[var6];
                  String normAllowed = allowedTool.trim().replace(" ", "");
                  if (requestedTool.equals(normAllowed)) {
                     return true;
                  }

                  if (!"leakres".equals(requestedTool) || !"skill".equals(normAllowed) && !"leakres".equals(normAllowed) && !"leak".equals(normAllowed)) {
                     if (!"effectauto".equals(requestedTool) || !"effect".equals(normAllowed) && !"auto".equals(normAllowed)) {
                        if ("tool".equals(requestedTool)) {
                           return true;
                        }

                        var6++;
                        continue;
                     }

                     return true;
                  }

                  return true;
               }

               return false;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean isValid() {
      return valid;
   }

   public static String getLicenseType() {
      return licenseType;
   }

   public static String getNote() {
      return note;
   }

   public static String getStartStr() {
      return startStr;
   }

   public static String getEndStr() {
      return endStr;
   }

   public static LocalDateTime getExpiryTime() {
      return expiryTime;
   }

   public static String getStatusText() {
      if (!valid) {
         return "Not authenticated";
      } else if (isExpired()) {
         return "[EXPIRED] License EXPIRED (" + endStr + ")";
      } else {
         long mins = minutesRemaining();
         if (mins == Long.MAX_VALUE) {
            return "[OK] " + licenseType.toUpperCase() + (note.isEmpty() ? "" : " | " + note) + " | No expiry";
         } else {
            return mins < 60L
               ? "[WARN] " + licenseType.toUpperCase() + (note.isEmpty() ? "" : " | " + note) + " | Expires in " + mins + " min"
               : "[OK] "
                  + licenseType.toUpperCase()
                  + (note.isEmpty() ? "" : " | " + note)
                  + " | Expires: "
                  + endStr
                  + " ("
                  + mins / 60L
                  + "h "
                  + mins % 60L
                  + "m left)";
         }
      }
   }
}
