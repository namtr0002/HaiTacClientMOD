package com.deplor.haitactihontool.mob;

import com.deplor.haitactihontool.config.AppConfig;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobDataManager {
   public static List<MobTemplate> loadMobs() {
      List<MobTemplate> mobs = new ArrayList<>();
      String sqlPath = AppConfig.getResolvedPath("path_mob_sql", "Data/Mob/mobs.sql");
      File file = new File(sqlPath);
      if (!file.exists()) {
         file.getParentFile().mkdirs();
         return mobs;
      } else {
         try {
            List<String> lines = Files.readAllLines(Paths.get(sqlPath), StandardCharsets.UTF_8);
            Pattern pattern = Pattern.compile("(?i)INSERT\\s+INTO\\s+[`]?mobs[`]?\\s+VALUES\\s*\\((.*)\\);");

            for (String line : lines) {
               line = line.trim();
               Matcher m = pattern.matcher(line);
               if (m.find()) {
                  String valuesStr = m.group(1);
                  List<String> tokens = parseSqlValues(valuesStr);
                  if (tokens.size() >= 11) {
                     int id = Integer.parseInt(tokens.get(0));
                     String name = tokens.get(1);
                     short level = Short.parseShort(tokens.get(2));
                     short hOne = Short.parseShort(tokens.get(3));
                     int hp = Integer.parseInt(tokens.get(4));
                     byte typemove = Byte.parseByte(tokens.get(5));
                     byte ishuman = Byte.parseByte(tokens.get(6));
                     byte typemonster = Byte.parseByte(tokens.get(7));
                     String idicon = tokens.get(8);
                     String skill = tokens.get(9);
                     int dame = Integer.parseInt(tokens.get(10));
                     mobs.add(new MobTemplate(id, name, level, hOne, hp, typemove, ishuman, typemonster, idicon, skill, dame));
                  }
               }
            }
         } catch (Exception var21) {
            var21.printStackTrace();
         }

         return mobs;
      }
   }

   public static void saveMobs(List<MobTemplate> mobs) {
      String sqlPath = AppConfig.getResolvedPath("path_mob_sql", "Data/Mob/mobs.sql");
      StringBuilder sb = new StringBuilder();
      sb.append("SET NAMES utf8mb4;\n");
      sb.append("SET FOREIGN_KEY_CHECKS = 0;\n\n");
      sb.append("DROP TABLE IF EXISTS `mobs`;\n");
      sb.append("CREATE TABLE `mobs`  (\n");
      sb.append("  `id` int NOT NULL,\n");
      sb.append("  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\n");
      sb.append("  `level` smallint NULL DEFAULT NULL,\n");
      sb.append("  `hOne` smallint NULL DEFAULT NULL,\n");
      sb.append("  `hp` int NULL DEFAULT NULL,\n");
      sb.append("  `typemove` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `ishuman` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `typemonster` tinyint NULL DEFAULT NULL,\n");
      sb.append("  `idicon` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\n");
      sb.append("  `skill` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\n");
      sb.append("  `dame` int NULL DEFAULT NULL,\n");
      sb.append("  PRIMARY KEY (`id`) USING BTREE\n");
      sb.append(") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;\n\n");

      for (MobTemplate m : mobs) {
         String cleanName = m.name.replace("\\'", "'").replace("'", "\\'");
         String cleanIdIcon = m.idicon.replace("\\'", "'").replace("'", "\\'");
         String cleanSkill = m.skill.replace("\\'", "'").replace("'", "\\'");
         sb.append(
            String.format(
               "INSERT INTO `mobs` VALUES (%d, '%s', %d, %d, %d, %d, %d, %d, '%s', '%s', %d);\n",
               m.id,
               cleanName,
               m.level,
               m.hOne,
               m.hp,
               m.typemove,
               m.ishuman,
               m.typemonster,
               cleanIdIcon,
               cleanSkill,
               m.dame
            )
         );
      }

      sb.append("\nSET FOREIGN_KEY_CHECKS = 1;\n");

      try {
         Files.writeString(Paths.get(sqlPath), sb.toString(), StandardCharsets.UTF_8);
      } catch (Exception var8) {
         var8.printStackTrace();
      }
   }

   private static List<String> parseSqlValues(String str) {
      List<String> res = new ArrayList<>();
      boolean inStr = false;
      StringBuilder cur = new StringBuilder();

      for (int i = 0; i < str.length(); i++) {
         char c = str.charAt(i);
         if (c != '\'' || i != 0 && str.charAt(i - 1) == '\\') {
            if (c == ',' && !inStr) {
               res.add(cur.toString().trim());
               cur = new StringBuilder();
            } else {
               cur.append(c);
            }
         } else {
            inStr = !inStr;
         }
      }

      res.add(cur.toString().trim());
      return res;
   }
}
