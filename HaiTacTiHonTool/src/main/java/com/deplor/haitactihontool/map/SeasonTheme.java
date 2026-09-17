package com.deplor.haitactihontool.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class SeasonTheme {
   public String id;
   public String name;
   public String emoji;
   public Color accentColor;
   public int tilesetOverride = -1;
   public Map<String, Short> tagToTemplateId = new LinkedHashMap<>();
   public String mapNameSuffix;

   public SeasonTheme(String id, String name, String emoji, Color accentColor) {
      this.id = id;
      this.name = name;
      this.emoji = emoji;
      this.accentColor = accentColor;
      this.mapNameSuffix = " [" + name + "]";
   }

   public SeasonTheme addRule(String tag, int templateId) {
      this.tagToTemplateId.put(tag.toLowerCase().trim(), (short)templateId);
      return this;
   }

   public boolean hasRuleForTag(String tag) {
      return this.tagToTemplateId.containsKey(tag.toLowerCase().trim());
   }

   public Short findReplacement(Set<String> tags) {
      if (tags != null && !tags.isEmpty()) {
         for (Entry<String, Short> rule : this.tagToTemplateId.entrySet()) {
            if (tags.contains(rule.getKey())) {
               return rule.getValue();
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @Override
   public String toString() {
      return this.emoji + " " + this.name;
   }

   public String toJson() {
      StringBuilder sb = new StringBuilder();
      sb.append("  {\n");
      sb.append("    \"id\": \"").append(escapeJson(this.id)).append("\",\n");
      sb.append("    \"name\": \"").append(escapeJson(this.name)).append("\",\n");
      sb.append("    \"emoji\": \"").append(escapeJson(this.emoji)).append("\",\n");
      sb.append("    \"accentColor\": \"#").append(String.format("%06X", this.accentColor.getRGB() & 16777215)).append("\",\n");
      sb.append("    \"tilesetOverride\": ").append(this.tilesetOverride).append(",\n");
      sb.append("    \"mapNameSuffix\": \"").append(escapeJson(this.mapNameSuffix)).append("\",\n");
      sb.append("    \"rules\": {\n");
      List<Entry<String, Short>> entries = new ArrayList<>(this.tagToTemplateId.entrySet());

      for (int i = 0; i < entries.size(); i++) {
         Entry<String, Short> e = entries.get(i);
         sb.append("      \"").append(escapeJson(e.getKey())).append("\": ").append(e.getValue());
         if (i < entries.size() - 1) {
            sb.append(",");
         }

         sb.append("\n");
      }

      sb.append("    }\n");
      sb.append("  }");
      return sb.toString();
   }

   private static String escapeJson(String s) {
      return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
   }
}
