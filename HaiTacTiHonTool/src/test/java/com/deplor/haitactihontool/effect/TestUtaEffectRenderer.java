package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.part.PartDataManager;
import com.deplor.haitactihontool.part.mPart;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class TestUtaEffectRenderer {

    public static void main(String[] args) {
        System.out.println("=== Running TestUtaEffectRenderer ===");
        
        List<mPart> parts = PartDataManager.loadParts();
        if (parts.isEmpty()) {
            throw new AssertionError("Parts list should not be empty");
        }
        
        mPart head = null, body = null, leg = null, weapon = null;
        for (mPart p : parts) {
            if (p.id == 1042) head = p;
            if (p.id == 1043) body = p;
            if (p.id == 1044) leg = p;
            if (p.id == 1045) weapon = p;
        }
        
        if (head == null) throw new AssertionError("Part 1042 Head must exist");
        if (body == null) throw new AssertionError("Part 1043 Body must exist");
        if (leg == null) throw new AssertionError("Part 1044 Leg must exist");
        if (weapon == null) throw new AssertionError("Part 1045 Weapon must exist");
        
        short headImgId = head.pi[0].id;
        short bodyImgId = body.pi[0].id;
        short legImgId = leg.pi[0].id;
        short weaponImgId = weapon.pi[0].id;
        
        System.out.println("Head img ID: " + headImgId + ", Body img ID: " + bodyImgId + ", Leg img ID: " + legImgId + ", Weapon img ID: " + weaponImgId);
        
        BufferedImage headImg = PartDataManager.getImage(headImgId);
        BufferedImage bodyImg = PartDataManager.getImage(bodyImgId);
        BufferedImage legImg = PartDataManager.getImage(legImgId);
        BufferedImage weaponImg = PartDataManager.getImage(weaponImgId);
        
        if (headImg == null) throw new AssertionError("Head image 12043 must not be null");
        if (bodyImg == null) throw new AssertionError("Body image 12048 must not be null");
        if (legImg == null) throw new AssertionError("Leg image 12068 must not be null");
        if (weaponImg == null) throw new AssertionError("Weapon image 12083 must not be null");
        
        System.out.println("Head img size: " + headImg.getWidth() + "x" + headImg.getHeight());
        System.out.println("Body img size: " + bodyImg.getWidth() + "x" + bodyImg.getHeight());
        System.out.println("Leg img size: " + legImg.getWidth() + "x" + legImg.getHeight());
        System.out.println("Weapon img size: " + weaponImg.getWidth() + "x" + weaponImg.getHeight());
        
        if (headImg.getWidth() == 80 && headImg.getHeight() == 80) {
            throw new AssertionError("Head should NOT be 80x80 clan icon (22043)!");
        }
        if (bodyImg.getWidth() == 80 && bodyImg.getHeight() == 80) {
            throw new AssertionError("Body should NOT be 80x80 clan icon (22048)!");
        }
        
        EffectCharRenderer renderer = new EffectCharRenderer();
        BufferedImage rendHead = renderer.getPartImage(headImgId);
        if (rendHead == null || rendHead.getWidth() != 108 || rendHead.getHeight() != 124) {
            throw new AssertionError("renderer.getPartImage(12043) must be real Uta head (108x124), got: " 
                + (rendHead == null ? "null" : rendHead.getWidth() + "x" + rendHead.getHeight()));
        }
        BufferedImage canvas = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = canvas.createGraphics();
        renderer.render(g2, 200, 300, 1.0);
        g2.dispose();
        
        int nonTransparentPixels = 0;
        for (int y = 0; y < 400; y++) {
            for (int x = 0; x < 400; x++) {
                int argb = canvas.getRGB(x, y);
                if ((argb >>> 24) > 0) {
                    nonTransparentPixels++;
                }
            }
        }
        System.out.println("Rendered non-transparent pixels: " + nonTransparentPixels);
        if (nonTransparentPixels < 1000) {
            throw new AssertionError("Should have rendered non-empty pixels for Uta");
        }
        
        System.out.println("=> [OK] TEST PASSED SUCCESSFULLY!");
    }
}
