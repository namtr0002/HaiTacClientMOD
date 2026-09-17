package com.deplor.haitactihontool.fashionv2;

import java.io.File;

public class KatakuriFashionV2Builder {
   public static void main(String[] args) {
      try {
         System.out.println("==========================================================");
         System.out.println("HAITACTIHONTOOL - KATAKURI FASHION V2 REBUILD (ICON ZOOM 4)");
         System.out.println("==========================================================");
         int fashionId = 132;
         String name = "Thời Trang Katakuri";
         String mwear = "[-1,-2,-1,1160,-1,1161,1159,-2]";
         File partsSqlFile = new File("Data/Part/parts.sql");
         File iconDir = new File("../HaiTacTiHonServer/data/icon/4");
         if (!iconDir.exists() || iconDir.listFiles() == null || iconDir.listFiles().length == 0) {
            iconDir = new File("Data/LeakRes/res/icon/4");
         }

         if (!iconDir.exists() || iconDir.listFiles() == null || iconDir.listFiles().length == 0) {
            iconDir = new File("../HaiTacTiHonServer/data/icon/1");
         }

         File outputDir = new File("Data/FashionV2");
         FashionV2Merger.clearDirectory(outputDir);
         System.out.println("-> Reading parts.sql: " + partsSqlFile.getAbsolutePath());
         System.out.println("-> Reading High-Res Icons: " + iconDir.getAbsolutePath());
         System.out.println("-> Parsing Katakuri mwear: " + mwear);
         FashionV2Merger.MergeResult result = FashionV2Merger.mergeFashionFromTemplate(fashionId, name, mwear, partsSqlFile, iconDir);
         FashionV2Merger.saveMergedResult(result, outputDir, "132");
         FashionV2Merger.saveMergedResult(result, outputDir, "1");
         System.out.println("==========================================================");
         System.out.println("[SUCCESS] KATAKURI FASHION V2 REBUILT FULLY WITH HIGH-RES 4X ICONS (29311.png -> 29348.png)!");
         System.out.println("Out folder: " + outputDir.getAbsolutePath());
         System.out.println("Generated files: 132.json, 132.bin, 132.binary, 132.png");
         System.out.println("Generated files: 1.json, 1.bin, 1.binary, 1.png");
         System.out.println("==========================================================");
      } catch (Exception var8) {
         var8.printStackTrace();
         System.err.println("[ERROR] Failed to rebuild Katakuri Fashion V2: " + var8.getMessage());
      }
   }
}
