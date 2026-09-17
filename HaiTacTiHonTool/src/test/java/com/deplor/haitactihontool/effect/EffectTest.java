package com.deplor.haitactihontool.effect;

import java.io.File;

public class EffectTest {

    public static void main(String[] args) throws Exception {
        File dataDir = new File("Data/Effect/data");
        if (!dataDir.exists()) {
            dataDir = new File("HaiTacTiHonTool/Data/Effect/data");
        }
        System.out.println("Testing directory: " + dataDir.getAbsolutePath());
        File[] files = dataDir.listFiles();
        if (files == null) {
            System.out.println("No files found!");
            return;
        }

        EffectBinaryParser parser = new EffectBinaryParser(dataDir.getAbsolutePath() + "/", "Data/Effect/img/");
        int successCount = 0;
        int errorCount = 0;
        for (File f : files) {
            if (f.isDirectory()) continue;
            try {
                int id = Integer.parseInt(f.getName());
                EffectModel model = parser.parse(id);
                successCount++;
            } catch (Exception ex) {
                System.out.println("Error parsing effect ID " + f.getName() + " (size: " + f.length() + " bytes): " + ex.getClass().getName() + " - " + ex.getMessage());
                errorCount++;
            }
        }
        System.out.println("Finished test: " + successCount + " success, " + errorCount + " errors.");

        // Testing Sequence Generator & Parser
        System.out.println("Testing Sequence Utilities...");
        EffectModel m = new EffectModel(1);
        m.frames = new EffFrame[]{EffFrame.createEmpty(), EffFrame.createEmpty(), EffFrame.createEmpty(), EffFrame.createEmpty()};

        m.autoGenerateSequenceLinear();
        System.out.println("Linear: " + m.getSequenceString());
        if (!m.getSequenceString().equals("0, 1, 2, 3")) throw new AssertionError();

        m.autoGenerateSequencePingPong();
        System.out.println("PingPong: " + m.getSequenceString());
        if (!m.getSequenceString().equals("0, 1, 2, 3, 2, 1")) throw new AssertionError();

        m.autoGenerateSequenceHold(2);
        System.out.println("Hold 2: " + m.getSequenceString());
        if (!m.getSequenceString().equals("0, 0, 1, 1, 2, 2, 3, 3")) throw new AssertionError();

        int[] p1 = EffectModel.parseSequenceString("0-3", 4);
        m.setCustomSequence(p1);
        System.out.println("Parsed 0-3: " + m.getSequenceString());
        if (!m.getSequenceString().equals("0, 1, 2, 3")) throw new AssertionError();

        int[] p2 = EffectModel.parseSequenceString("0*2, 1*2, 2*2", 4);
        m.setCustomSequence(p2);
        System.out.println("Parsed 0*2...: " + m.getSequenceString());
        if (!m.getSequenceString().equals("0, 0, 1, 1, 2, 2")) throw new AssertionError();

        System.out.println("[OK] ALL SEQUENCE TESTS PASSED!");

        // Testing GIF Export
        System.out.println("Testing Animated GIF Export...");
        try {
            File tempGif = File.createTempFile("test_effect_", ".gif");
            tempGif.deleteOnExit();
            com.deplor.haitactihontool.util.GifExportHelper.exportEffectSequenceToGif(m, tempGif, 50, 1.0, true, null);
            if (tempGif.exists() && tempGif.length() > 0) {
                System.out.println("[OK] GIF Export successful! File size: " + tempGif.length() + " bytes");
            } else {
                throw new RuntimeException("GIF file is empty or missing!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("GIF export failed", ex);
        }
    }
}
