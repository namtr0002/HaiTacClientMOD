package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.AppConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class SeasonKeywordStore {
   private static SeasonKeywordStore instance;
   private final Map<Short, Set<String>> store = new HashMap<>();
   private String jsonFilePath = AppConfig.getPath("Data/Map/season_keywords.json");

   private SeasonKeywordStore() {
      this.loadFromFile();
   }

   public static SeasonKeywordStore gI() {
      if (instance == null) {
         instance = new SeasonKeywordStore();
      }

      return instance;
   }

   public static void reset() {
      instance = null;
   }

   public Set<String> getTags(short templateId) {
      return this.store.getOrDefault(templateId, Collections.emptySet());
   }

   public String[] getTagArray(short templateId) {
      Set<String> tags = this.getTags(templateId);
      return tags.toArray(new String[0]);
   }

   public String getTagString(short templateId) {
      return String.join(", ", this.getTags(templateId));
   }

   public void setTagString(short templateId, String tagStr) {
      if (tagStr != null && !tagStr.trim().isEmpty()) {
         String[] parts = tagStr.split("[,;\\s]+");
         Set<String> tags = new LinkedHashSet<>();

         for (String p : parts) {
            String t = p.trim().toLowerCase();
            if (!t.isEmpty()) {
               tags.add(t);
            }
         }

         if (tags.isEmpty()) {
            this.store.remove(templateId);
         } else {
            this.store.put(templateId, tags);
         }
      } else {
         this.store.remove(templateId);
      }
   }

   public void setTags(short templateId, String[] tags) {
      if (tags != null && tags.length != 0) {
         Set<String> set = new LinkedHashSet<>();

         for (String t : tags) {
            if (t != null && !t.trim().isEmpty()) {
               set.add(t.trim().toLowerCase());
            }
         }

         if (set.isEmpty()) {
            this.store.remove(templateId);
         } else {
            this.store.put(templateId, set);
         }
      } else {
         this.store.remove(templateId);
      }
   }

   public void addTag(short templateId, String tag) {
      if (tag != null && !tag.trim().isEmpty()) {
         this.store.computeIfAbsent(templateId, k -> new LinkedHashSet<>()).add(tag.trim().toLowerCase());
      }
   }

   public void removeTag(short templateId, String tag) {
      Set<String> tags = this.store.get(templateId);
      if (tags != null) {
         tags.remove(tag.trim().toLowerCase());
         if (tags.isEmpty()) {
            this.store.remove(templateId);
         }
      }
   }

   public List<Short> findByTag(String tag) {
      String normalized = tag.trim().toLowerCase();
      List<Short> result = new ArrayList<>();

      for (Entry<Short, Set<String>> entry : this.store.entrySet()) {
         if (entry.getValue().contains(normalized)) {
            result.add(entry.getKey());
         }
      }

      return result;
   }

   public String suggestTagsFromImageName(int imageId) {
      String basePath = AppConfig.getResolvedPath("path_map_img", "Data/Map/ServerImage/");
      if (!basePath.endsWith("/") && !basePath.endsWith("\\")) {
         basePath = basePath + "/";
      }

      File f = new File(basePath + imageId + ".png");
      String fileName = f.exists() ? f.getName().replace(".png", "") : String.valueOf(imageId);
      String[][] heuristics = new String[][]{
         {"tree", "cay"},
         {"palm", "cay"},
         {"bamboo", "tre"},
         {"pine", "cay"},
         {"oak", "cay"},
         {"house", "nha"},
         {"building", "nha"},
         {"shop", "nha"},
         {"temple", "nha"},
         {"pagoda", "nha"},
         {"rock", "da"},
         {"stone", "da"},
         {"boulder", "da"},
         {"water", "nuoc"},
         {"lake", "nuoc"},
         {"river", "nuoc"},
         {"lamp", "den"},
         {"torch", "den"},
         {"lantern", "den"},
         {"flower", "hoa"},
         {"bush", "bui"},
         {"grass", "co"},
         {"fence", "rao"},
         {"gate", "cong"},
         {"wall", "tuong"},
         {"boat", "thuyen"},
         {"ship", "thuyen"}
      };
      String nameLower = fileName.toLowerCase();
      List<String> suggestions = new ArrayList<>();

      for (String[] pair : heuristics) {
         String keyword = pair[0];
         if (nameLower.contains(keyword)) {
            suggestions.add(keyword);
         }
      }

      return suggestions.isEmpty() ? "" : String.join(", ", suggestions);
   }

   public Map<Short, Set<String>> getAll() {
      return Collections.unmodifiableMap(this.store);
   }

   public int size() {
      return this.store.size();
   }

   public boolean saveToFile() {
      File file = new File(this.jsonFilePath);
      File parent = file.getParentFile();
      if (parent != null && !parent.exists()) {
         parent.mkdirs();
      }

      try {
         boolean var15;
         try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            pw.println("{");
            List<Short> keys = new ArrayList<>(this.store.keySet());
            keys.sort(Short::compare);

            for (int i = 0; i < keys.size(); i++) {
               Short id = keys.get(i);
               Set<String> tags = this.store.get(id);
               StringBuilder sb = new StringBuilder();
               sb.append("  \"").append(id).append("\": [");
               boolean first = true;

               for (String tag : tags) {
                  if (!first) {
                     sb.append(", ");
                  }

                  sb.append("\"").append(tag.replace("\"", "\\\"")).append("\"");
                  first = false;
               }

               sb.append("]");
               if (i < keys.size() - 1) {
                  sb.append(",");
               }

               pw.println(sb.toString());
            }

            pw.println("}");
            System.out.println("[SeasonKeywordStore] Saved " + this.store.size() + " entries to: " + this.jsonFilePath);
            var15 = true;
         }

         return var15;
      } catch (IOException var14) {
         System.err.println("[SeasonKeywordStore] Error saving: " + var14.getMessage());
         return false;
      }
   }

   public void loadFromFile() {
      this.store.clear();
      File file = new File(this.jsonFilePath);
      if (file.exists()) {
         try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
               sb.append(line).append('\n');
            }

            this.parseSimpleJson(sb.toString());
            System.out.println("[SeasonKeywordStore] Loaded " + this.store.size() + " tagged templates from: " + this.jsonFilePath);
         } catch (IOException var7) {
            System.err.println("[SeasonKeywordStore] Error loading: " + var7.getMessage());
         }
      }
   }

   private void parseSimpleJson(String json) {
      json = json.trim();
      if (json.startsWith("{")) {
         json = json.substring(1);
      }

      if (json.endsWith("}")) {
         json = json.substring(0, json.length() - 1);
      }

      int pos = 0;

      while (pos < json.length()) {
         int keyStart = json.indexOf(34, pos);
         if (keyStart == -1) {
            break;
         }

         int keyEnd = json.indexOf(34, keyStart + 1);
         if (keyEnd == -1) {
            break;
         }

         String key = json.substring(keyStart + 1, keyEnd);
         pos = keyEnd + 1;
         int arrStart = json.indexOf(91, pos);
         if (arrStart == -1) {
            break;
         }

         int arrEnd = json.indexOf(93, arrStart);
         if (arrEnd == -1) {
            break;
         }

         String arrContent = json.substring(arrStart + 1, arrEnd);
         pos = arrEnd + 1;

         try {
            short id = Short.parseShort(key.trim());
            Set<String> tags = new LinkedHashSet<>();
            int tPos = 0;

            while (true) {
               if (tPos < arrContent.length()) {
                  int tStart = arrContent.indexOf(34, tPos);
                  if (tStart != -1) {
                     int tEnd = arrContent.indexOf(34, tStart + 1);
                     if (tEnd != -1) {
                        String tag = arrContent.substring(tStart + 1, tEnd).trim();
                        if (!tag.isEmpty()) {
                           tags.add(tag);
                        }

                        tPos = tEnd + 1;
                        continue;
                     }
                  }
               }

               if (!tags.isEmpty()) {
                  this.store.put(id, tags);
               }
               break;
            }
         } catch (NumberFormatException var15) {
         }
      }
   }
}
