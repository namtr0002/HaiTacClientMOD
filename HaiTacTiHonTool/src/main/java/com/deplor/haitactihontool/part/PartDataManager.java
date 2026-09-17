package com.deplor.haitactihontool.part;

import com.deplor.haitactihontool.config.AppConfig;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;

public class PartDataManager {
   public static final String DATA_DIR = "Data/Part/";
   public static final String PARTS_FILE = "Data/Part/parts.json";
   private static boolean hasShownDebug = false;
   private static final Map<Integer, BufferedImage> imgCache = new ConcurrentHashMap<>();

   public static List<mPart> loadParts() {
      List<mPart> partsList = new ArrayList<>();
      String partsFile = AppConfig.getResolvedPath("path_part", "Data/Part/parts.json");
      File file = new File(partsFile);
      System.out.println("=== LOADING PARTS ===");
      System.out.println("File path: " + file.getAbsolutePath());
      System.out.println("File exists: " + file.exists());
      if (!file.exists()) {
         return partsList;
      } else {
         Exception loadException = null;

         try {
            String content = new String(Files.readAllBytes(Paths.get(partsFile)), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(content);
            JSONArray records = root.getJSONArray("RECORDS");
            System.out.println("RECORDS count in JSON: " + records.length());

            for (int i = 0; i < records.length(); i++) {
               JSONObject obj = records.getJSONObject(i);
               mPart part = new mPart();
               part.id = obj.getInt("id");
               part.type = obj.getInt("type");
               if (part.type == 0) {
                  part.pi = new PartImage[5];
               } else if (part.type == 2) {
                  part.pi = new PartImage[15];
               } else if (part.type == 1) {
                  part.pi = new PartImage[20];
               } else if (part.type == 4) {
                  part.pi = new PartImage[2];
               } else if (part.type == 5) {
                  part.pi = new PartImage[2];
               } else if (part.type == 3) {
                  part.pi = new PartImage[24];
               } else {
                  part.pi = new PartImage[0];
               }

               String dataStr = obj.getString("data");
               if (dataStr != null && !dataStr.isEmpty() && !dataStr.equals("null")) {
                  JSONArray dataArr = new JSONArray(dataStr);

                  for (int j = 0; j < dataArr.length() && j < part.pi.length; j++) {
                     JSONArray piData = dataArr.getJSONArray(j);
                     PartImage pi = new PartImage();
                     pi.id = (short)piData.getInt(0);
                     pi.dx = (byte)(piData.length() > 1 ? piData.getInt(1) : 0);
                     pi.dy = (byte)(piData.length() > 2 ? piData.getInt(2) : 0);
                     part.pi[j] = pi;
                  }

                  for (int j = 0; j < part.pi.length; j++) {
                     if (part.pi[j] == null) {
                        part.pi[j] = new PartImage();
                     }
                  }
               }

               partsList.add(part);
            }
         } catch (Exception var15) {
            var15.printStackTrace();
         }

         return partsList;
      }
   }

   public static void saveParts(List<mPart> parts) throws IOException {
      JSONObject root = new JSONObject();
      JSONArray records = new JSONArray();
      StringBuilder sqlBuilder = new StringBuilder();
      sqlBuilder.append("-- Auto-generated parts SQL dump\n");

      for (mPart part : parts) {
         JSONObject obj = new JSONObject();
         obj.put("id", part.id);
         obj.put("type", part.type);
         JSONArray dataArr = new JSONArray();
         if (part.pi != null) {
            for (PartImage pi : part.pi) {
               JSONArray piArr = new JSONArray();
               if (pi != null && pi.id >= 0) {
                  piArr.put(pi.id);
                  if (pi.dx != 0 || pi.dy != 0) {
                     piArr.put(pi.dx);
                     if (pi.dy != 0) {
                        piArr.put(pi.dy);
                     }
                  }
               } else {
                  piArr.put(-1);
               }

               dataArr.put(piArr);
            }
         }

         obj.put("data", dataArr.toString());
         records.put(obj);
         String dataEscaped = dataArr.toString().replace("'", "''");
         sqlBuilder.append(
            String.format(
               "INSERT INTO `parts` (`id`, `type`, `data`) VALUES (%d, %d, '%s') ON DUPLICATE KEY UPDATE `type` = %d, `data` = '%s';\n",
               part.id,
               part.type,
               dataEscaped,
               part.type,
               dataEscaped
            )
         );
      }

      root.put("RECORDS", records);
      String partsFile = AppConfig.getResolvedPath("path_part", "Data/Part/parts.json");
      Files.write(Paths.get(partsFile), root.toString(2).getBytes(StandardCharsets.UTF_8));
      String sqlFile = partsFile.replace(".json", ".sql");
      Files.write(Paths.get(sqlFile), sqlBuilder.toString().getBytes(StandardCharsets.UTF_8));
   }

   public static BufferedImage getImage(short id) {
      int key = id & '\uffff';
      if (imgCache.containsKey(key)) {
         return imgCache.get(key);
      } else {
         String baseDir = AppConfig.getResolvedPath("path_part_img", "Data/Part/img/");
         if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
            baseDir = baseDir + "/";
         }

         List<Integer> physicalIds = new ArrayList<>();
         if (id > 10000) {
            physicalIds.add(id + 16000);
            physicalIds.add(Integer.valueOf(id));
         } else if (id > 0) {
            physicalIds.add(id + 10000);
            physicalIds.add(id + 26000);
            physicalIds.add(Integer.valueOf(id));
         } else {
            physicalIds.add(Integer.valueOf(id));
         }

         for (int physId : physicalIds) {
            File f = new File(baseDir + "x4/" + physId + ".png");
            if (!f.exists()) {
               f = new File(baseDir + physId + ".png");
            }

            if (f.exists()) {
               try {
                  BufferedImage img = ImageIO.read(f);
                  if (img != null) {
                     imgCache.put(key, img);
                     return img;
                  }
               } catch (Exception var8) {
               }
            }
         }

         return null;
      }
   }
}
