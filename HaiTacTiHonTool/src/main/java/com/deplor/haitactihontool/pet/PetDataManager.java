package com.deplor.haitactihontool.pet;

import com.deplor.haitactihontool.config.AppConfig;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetDataManager {
   public static List<PetTemplate> loadPets() {
      List<PetTemplate> pets = new ArrayList<>();
      String sqlPath = AppConfig.getResolvedPath("path_pet_sql", "Data/Pet/pet_template.sql");
      File file = new File(sqlPath);
      if (!file.exists()) {
         file.getParentFile().mkdirs();
         return pets;
      } else {
         try {
            List<String> lines = Files.readAllLines(Paths.get(sqlPath), StandardCharsets.UTF_8);
            Pattern pattern = Pattern.compile("(?i)INSERT\\s+INTO\\s+[`]?pet_template[`]?\\s+VALUES\\s*\\((.*)\\);");

            for (String line : lines) {
               line = line.trim();
               Matcher m = pattern.matcher(line);
               if (m.find()) {
                  String valuesStr = m.group(1);
                  List<String> tokens = parseSqlValues(valuesStr);
                  if (tokens.size() >= 5) {
                     int id = Integer.parseInt(tokens.get(0));
                     String name = tokens.get(1);
                     int icon = Integer.parseInt(tokens.get(2));
                     int type = Integer.parseInt(tokens.get(3));
                     int frame = Integer.parseInt(tokens.get(4));
                     String op = null;
                     if (tokens.size() >= 6) {
                        String rawOp = tokens.get(5);
                        if (rawOp != null && !rawOp.equalsIgnoreCase("NULL")) {
                           op = rawOp;
                        }
                     }

                     pets.add(new PetTemplate(id, name, icon, type, frame, op));
                  }
               }
            }
         } catch (Exception var17) {
            var17.printStackTrace();
         }

         return pets;
      }
   }

   private static List<String> parseSqlValues(String valStr) {
      List<String> tokens = new ArrayList<>();
      StringBuilder sb = new StringBuilder();
      boolean inQuotes = false;

      for (int i = 0; i < valStr.length(); i++) {
         char c = valStr.charAt(i);
         if (c == '\'') {
            if (inQuotes && i + 1 < valStr.length() && valStr.charAt(i + 1) == '\'') {
               sb.append('\'');
               i++;
            } else {
               inQuotes = !inQuotes;
            }
         } else if (c == ',' && !inQuotes) {
            tokens.add(sb.toString().trim());
            sb.setLength(0);
         } else {
            sb.append(c);
         }
      }

      tokens.add(sb.toString().trim());
      return tokens;
   }

   public static void savePets(List<PetTemplate> pets) throws IOException {
      String sqlPath = AppConfig.getResolvedPath("path_pet_sql", "Data/Pet/pet_template.sql");
      StringBuilder sb = new StringBuilder();
      sb.append("/*\n");
      sb.append(" Navicat Premium Data Transfer\n\n");
      sb.append(" Source Server Type    : MySQL\n");
      sb.append(" File Encoding         : 65001\n");
      sb.append("*/\n\n");
      sb.append("SET NAMES utf8mb4;\n");
      sb.append("SET FOREIGN_KEY_CHECKS = 0;\n\n");
      sb.append("-- ----------------------------\n");
      sb.append("-- Table structure for pet_template\n");
      sb.append("-- ----------------------------\n");
      sb.append("DROP TABLE IF EXISTS `pet_template`;\n");
      sb.append("CREATE TABLE `pet_template`  (\n");
      sb.append("  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,\n");
      sb.append("  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,\n");
      sb.append("  `icon` smallint NOT NULL,\n");
      sb.append("  `type` tinyint NOT NULL,\n");
      sb.append("  `frame` smallint NOT NULL,\n");
      sb.append("  `op` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\n");
      sb.append("  PRIMARY KEY (`id`) USING BTREE\n");
      sb.append(") ENGINE = InnoDB AUTO_INCREMENT = 1000 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;\n\n");
      sb.append("-- ----------------------------\n");
      sb.append("-- Records of pet_template\n");
      sb.append("-- ----------------------------\n");

      for (PetTemplate pet : pets) {
         String opVal = pet.op != null && !pet.op.equalsIgnoreCase("null") && !pet.op.trim().isEmpty() ? "'" + pet.op.replace("'", "''") + "'" : "NULL";
         String nameEscaped = pet.name.replace("'", "''");
         sb.append(String.format("INSERT INTO `pet_template` VALUES (%d, '%s', %d, %d, %d, %s);\n", pet.id, nameEscaped, pet.icon, pet.type, pet.frame, opVal));
      }

      sb.append("\nSET FOREIGN_KEY_CHECKS = 1;\n");
      File file = new File(sqlPath);
      if (file.getParentFile() != null) {
         file.getParentFile().mkdirs();
      }

      Files.write(Paths.get(sqlPath), sb.toString().getBytes(StandardCharsets.UTF_8));
   }
}
