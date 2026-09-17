package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.AppConfig;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SeasonThemeManager {
   private static SeasonThemeManager instance;
   private final List<SeasonTheme> themes = new ArrayList<>();
   private String jsonFilePath = AppConfig.getPath("Data/Map/seasons.json");

   private SeasonThemeManager() {
      this.initBuiltInThemes();
      this.loadCustomThemes();
   }

   public static SeasonThemeManager gI() {
      if (instance == null) {
         instance = new SeasonThemeManager();
      }

      return instance;
   }

   public static void reset() {
      instance = null;
   }

   private void initBuiltInThemes() {
      SeasonTheme halloween = new SeasonTheme("halloween", "Halloween", "\ud83c\udf83", new Color(16739072));
      halloween.addRule("tree", 200)
         .addRule("lamp", 201)
         .addRule("den", 201)
         .addRule("house", 202)
         .addRule("nha", 202)
         .addRule("rock", 203)
         .addRule("da", 203)
         .addRule("fence", 204)
         .addRule("rao", 204)
         .addRule("flower", 205)
         .addRule("hoa", 205);
      halloween.tilesetOverride = -1;
      SeasonTheme tet = new SeasonTheme("tet", "Tết Nguyên Đán", "\ud83e\udde7", new Color(15204397));
      tet.addRule("tree", 210)
         .addRule("cay", 210)
         .addRule("lamp", 211)
         .addRule("den", 211)
         .addRule("house", 212)
         .addRule("nha", 212)
         .addRule("flower", 213)
         .addRule("hoa", 213)
         .addRule("fence", 214)
         .addRule("rao", 214)
         .addRule("rock", 215)
         .addRule("da", 215);
      tet.tilesetOverride = -1;
      SeasonTheme christmas = new SeasonTheme("christmas", "Giáng Sinh", "\ud83c\udf84", new Color(1796922));
      christmas.addRule("tree", 220)
         .addRule("cay", 220)
         .addRule("lamp", 221)
         .addRule("den", 221)
         .addRule("house", 222)
         .addRule("nha", 222)
         .addRule("flower", 223)
         .addRule("hoa", 223)
         .addRule("rock", 224)
         .addRule("da", 224);
      christmas.tilesetOverride = -1;
      SeasonTheme summer = new SeasonTheme("summer", "Mùa Hè", "☀️", new Color(16766720));
      summer.addRule("tree", 230).addRule("cay", 230).addRule("flower", 231).addRule("hoa", 231).addRule("rock", 232).addRule("da", 232);
      summer.tilesetOverride = -1;
      SeasonTheme winter = new SeasonTheme("winter", "Mùa Đông", "❄️", new Color(8308963));
      winter.addRule("tree", 240).addRule("cay", 240).addRule("flower", 241).addRule("hoa", 241).addRule("rock", 242).addRule("da", 242);
      winter.tilesetOverride = -1;
      this.themes.add(halloween);
      this.themes.add(tet);
      this.themes.add(christmas);
      this.themes.add(summer);
      this.themes.add(winter);
   }

   public List<SeasonTheme> getAllThemes() {
      return Collections.unmodifiableList(this.themes);
   }

   public SeasonTheme getThemeById(String id) {
      for (SeasonTheme t : this.themes) {
         if (t.id.equals(id)) {
            return t;
         }
      }

      return null;
   }

   public void addOrUpdateTheme(SeasonTheme theme) {
      for (int i = 0; i < this.themes.size(); i++) {
         if (this.themes.get(i).id.equals(theme.id)) {
            this.themes.set(i, theme);
            this.saveCustomThemes();
            return;
         }
      }

      this.themes.add(theme);
      this.saveCustomThemes();
   }

   public boolean removeTheme(String id) {
      Iterator<SeasonTheme> it = this.themes.iterator();

      while (it.hasNext()) {
         SeasonTheme t = it.next();
         if (t.id.equals(id)) {
            it.remove();
            this.saveCustomThemes();
            return true;
         }
      }

      return false;
   }

   public SeasonThemeManager.TransformPreview previewTransform(GameMap map, SeasonTheme theme) {
      SeasonThemeManager.TransformPreview preview = new SeasonThemeManager.TransformPreview();
      if (map != null && theme != null) {
         preview.totalItems = map.items.size();
         SeasonKeywordStore kws = SeasonKeywordStore.gI();

         for (ItemMapEntity entity : map.items) {
            Set<String> tags = kws.getTags(entity.templateId);
            Short replacement = theme.findReplacement(tags);
            if (replacement != null && replacement != entity.templateId) {
               SeasonThemeManager.TransformPreview.TransformEntry entry = new SeasonThemeManager.TransformPreview.TransformEntry();
               entry.oldTemplateId = entity.templateId;
               entry.newTemplateId = replacement;
               entry.tileX = entity.tileX;
               entry.tileY = entity.tileY;

               for (String tag : tags) {
                  if (theme.hasRuleForTag(tag)) {
                     entry.matchedTag = tag;
                     break;
                  }
               }

               preview.entries.add(entry);
               preview.matchedItems++;
            }
         }

         if (theme.tilesetOverride >= 0) {
            preview.tilesetChanged = true;
            preview.newTileset = theme.tilesetOverride;
         }

         return preview;
      } else {
         return preview;
      }
   }

   public GameMap applyTransform(GameMap map, SeasonTheme theme) {
      if (map != null && theme != null) {
         GameMap copy = this.deepCopy(map);
         copy.name = map.name + theme.mapNameSuffix;
         SeasonKeywordStore kws = SeasonKeywordStore.gI();

         for (ItemMapEntity entity : copy.items) {
            Set<String> tags = kws.getTags(entity.templateId);
            Short replacement = theme.findReplacement(tags);
            if (replacement != null && replacement != entity.templateId) {
               entity.templateId = replacement;
               entity.template = TemplateManager.gI().getItemTemplate(replacement);
            }
         }

         if (theme.tilesetOverride >= 0) {
            copy.setTileSetId(theme.tilesetOverride);
         }

         return copy;
      } else {
         return null;
      }
   }

   private GameMap deepCopy(GameMap src) {
      GameMap copy = new GameMap();
      copy.id = src.id;
      copy.name = src.name;
      copy.max_zone = src.max_zone;
      copy.max_player = src.max_player;
      copy.type_view_p = src.type_view_p;
      copy.isOnlinemap = src.isOnlinemap;
      copy.specMap = src.specMap;
      copy.id_eff_map = src.id_eff_map;
      copy.level = src.level;
      copy.typeChangeMap = src.typeChangeMap;
      copy.strTimeChange = src.strTimeChange;
      copy.IDBack = src.IDBack;
      copy.HBack = src.HBack;
      copy.maxW = src.maxW;
      copy.maxH = src.maxH;
      copy.width = src.width;
      copy.height = src.height;
      copy.tileSetId = src.tileSetId;
      if (src.mapPaint != null) {
         copy.mapPaint = Arrays.copyOf(src.mapPaint, src.mapPaint.length);
      }

      if (src.mapType != null) {
         copy.mapType = Arrays.copyOf(src.mapType, src.mapType.length);
      }

      if (src.data != null) {
         copy.data = new byte[src.data.length][];

         for (int i = 0; i < src.data.length; i++) {
            if (src.data[i] != null) {
               copy.data[i] = Arrays.copyOf(src.data[i], src.data[i].length);
            }
         }
      }

      for (ItemMapEntity entity : src.items) {
         ItemMapEntity ec = new ItemMapEntity(entity.templateId, entity.tileX, entity.tileY);
         ec.template = entity.template;
         copy.items.add(ec);
      }

      copy.npcs.addAll(src.npcs);

      for (GameMap.Mob m : src.list_mob) {
         GameMap.Mob mc = new GameMap.Mob();
         mc.templateId = m.templateId;
         mc.x = m.x;
         mc.y = m.y;
         copy.list_mob.add(mc);
      }

      for (GameMap.Vgo v : src.vgos) {
         GameMap.Vgo vc = new GameMap.Vgo();
         vc.id_map_go = v.id_map_go;
         vc.xold = v.xold;
         vc.yold = v.yold;
         vc.xnew = v.xnew;
         vc.ynew = v.ynew;
         copy.vgos.add(vc);
      }

      return copy;
   }

   private void loadCustomThemes() {
      File file = new File(this.jsonFilePath);
      if (file.exists()) {
         System.out.println("[SeasonThemeManager] Custom seasons file found: " + this.jsonFilePath);
      }
   }

   public boolean saveCustomThemes() {
      File file = new File(this.jsonFilePath);
      File parent = file.getParentFile();
      if (parent != null && !parent.exists()) {
         parent.mkdirs();
      }

      try {
         boolean var9;
         try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            pw.println("[");

            for (int i = 0; i < this.themes.size(); i++) {
               pw.print(this.themes.get(i).toJson());
               if (i < this.themes.size() - 1) {
                  pw.print(",");
               }

               pw.println();
            }

            pw.println("]");
            System.out.println("[SeasonThemeManager] Saved " + this.themes.size() + " themes to: " + this.jsonFilePath);
            var9 = true;
         }

         return var9;
      } catch (IOException var8) {
         System.err.println("[SeasonThemeManager] Error saving: " + var8.getMessage());
         return false;
      }
   }

   public static class TransformPreview {
      public int totalItems;
      public int matchedItems;
      public List<SeasonThemeManager.TransformPreview.TransformEntry> entries = new ArrayList<>();
      public boolean tilesetChanged;
      public int newTileset;

      public static class TransformEntry {
         public short oldTemplateId;
         public short newTemplateId;
         public String matchedTag;
         public int tileX;
         public int tileY;
      }
   }
}
