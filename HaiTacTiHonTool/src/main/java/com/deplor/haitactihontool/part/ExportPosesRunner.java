package com.deplor.haitactihontool.part;

import com.deplor.haitactihontool.util.GifExportHelper;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ExportPosesRunner {
    public static void main(String[] args) {
        try {
            System.out.println("Loading parts...");
            List<mPart> allParts = PartDataManager.loadParts();
            System.out.println("Loaded parts: " + allParts.size());

            // Fashion array slots:
            // 0: Weapon, 1: Hat, 2: WFashion, 3: Body, 4: Cloak, 5: Leg, 6: Head, 7: Hair
            String charName = args.length > 0 ? args[0].toLowerCase() : "luffy";
            short[] fashion;
            if ("uta".equals(charName)) {
                fashion = new short[] { -1, -2, -1, 1043, -1, 1044, 1042, -2 };
            } else if ("kaido".equals(charName)) {
                fashion = new short[] { -1, -1, -1, 1063, -1, 1064, 1062, -2 };
            } else {
                // Default: Luffy bare-handed with Straw Hat and black hair
                charName = "luffy";
                fashion = new short[] { -1, 2, -1, 3, -1, 4, 0, 1 };
            }
            System.out.println("Using Character: " + charName + " | Fashion (Bare-handed): " + Arrays.toString(fashion));

            File outDir = new File("preview/" + charName + "_all_poses");
            outDir.mkdirs();
            File gifDir = new File("preview/" + charName + "_all_poses/gif");
            gifDir.mkdirs();

            System.out.println("Exporting all 62 pose frames (PNG)...");
            GifExportHelper.exportAllPoseFramesToPng(fashion, allParts, 0, outDir, "char", 1.0, true, null,
                (cur, total, msg) -> System.out.println("  " + msg)
            );

            System.out.println("Exporting all GIFs (11 individual actions + All Poses Loop)...");
            GifExportHelper.exportAllPartActionsToGif(fashion, allParts, 0, gifDir, "char", 80, 1.0, true, null, true,
                (cur, total, msg) -> System.out.println("  " + msg)
            );

            System.out.println("Export completed successfully into: " + outDir.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
