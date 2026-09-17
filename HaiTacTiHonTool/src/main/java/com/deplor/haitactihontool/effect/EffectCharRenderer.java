package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.part.CharInfoData;
import com.deplor.haitactihontool.part.OffsetUtility;
import com.deplor.haitactihontool.part.PartDataManager;
import com.deplor.haitactihontool.part.PartImage;
import com.deplor.haitactihontool.part.mPart;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectCharRenderer {
   private static final double LOGIC_SCALE = 4.0;

   // Preset Uta matching presets.json: [1045, -2, -1, 1043, -1, 1044, 1042, -2]
   // Index 0: Weapon (1045)
   // Index 1: Hat (-2)
   // Index 2: Weapon Fashion (-1)
   // Index 3: Body (1043)
   // Index 4: Other (-1)
   // Index 5: Leg (1044)
   // Index 6: Head (1042)
   // Index 7: Hair (-2)
   public static final short[] DEFAULT_WEARING = new short[]{1045, -2, -1, 1043, -1, 1044, 1042, -2};

   // Drawing order matching Client & PartCanvas:
   // Slot 7: Other, Slot 1: Leg, Slot 2: Body, Slot 0: Head, Slot 5: Hair, Slot 4: Hat, Slot 3: Weapon, Slot 6: Weapon Fashion
   private static final int[] DRAW_ORDER = new int[]{7, 1, 2, 0, 5, 4, 3, 6};

   private short[] wearing = DEFAULT_WEARING.clone();
   private final Map<Integer, mPart> partsMap = new HashMap<>();

   public EffectCharRenderer() {
      this.loadParts();
   }

   public void loadParts() {
      this.partsMap.clear();
      List<mPart> list = PartDataManager.loadParts();
      for (mPart p : list) {
         this.partsMap.put(p.id, p);
      }
   }

   public void setWearing(short[] wearing) {
      if (wearing != null && wearing.length >= 8) {
         this.wearing = wearing.clone();
      }
   }

   public short[] getWearing() {
      return this.wearing;
   }

   public BufferedImage getPartImage(short id) {
      if (id <= 0) {
         return null;
      }
      return PartDataManager.getImage(id);
   }

   private mPart getPartForSlot(int slotIndex) {
      if (this.wearing == null || this.wearing.length < 8) {
         return null;
      }
      short partId = -1;
      switch (slotIndex) {
         case 0: // Head
            partId = this.wearing[6];
            break;
         case 1: // Leg
            partId = this.wearing[5];
            break;
         case 2: // Body
            partId = this.wearing[3];
            break;
         case 3: // Weapon
            partId = this.wearing[0];
            break;
         case 4: // Hat
            partId = this.wearing[1];
            break;
         case 5: // Hair
            partId = this.wearing[7];
            break;
         case 6: // Weapon Fashion
            partId = this.wearing[2];
            break;
         case 7: // Other
            partId = this.wearing[4];
            break;
      }

      if (partId < 0) {
         return null;
      }
      return this.partsMap.get((int) partId);
   }

   public void render(Graphics2D g2d, int ox, int oy, double zoom) {
      this.render(g2d, ox, oy, zoom, false);
   }

   public void render(Graphics2D g2d, int ox, int oy, double zoom, boolean isBigBody) {
      int frame = 0; // Status 0: Stand
      short bodyId = this.wearing != null && this.wearing.length > 3 ? this.wearing[3] : -1;
      short headId = this.wearing != null && this.wearing.length > 6 ? this.wearing[6] : -1;

      int lechYHead = OffsetUtility.getLechYHead(bodyId);
      if (isBigBody && lechYHead == 0) {
         lechYHead = -6;
      }

      // Draw standard shadow under character
      g2d.setColor(new Color(0, 0, 0, 70));
      int shadowW = (int)(88.0 * zoom);
      int shadowH = (int)(24.0 * zoom);
      g2d.fillOval(ox - shadowW / 2, oy - shadowH / 2 - (int)(4.0 * zoom), shadowW, shadowH);

      for (int slotIndex : DRAW_ORDER) {
         mPart part = this.getPartForSlot(slotIndex);
         if (part != null && part.pi != null) {
            int ciSlot = slotIndex;
            if (slotIndex == 6) {
               ciSlot = 3;
            } else if (slotIndex == 7) {
               ciSlot = 6;
            }

            if (ciSlot >= 0 && ciSlot < CharInfoData.CharInfo[frame].length) {
               int ciIdx = CharInfoData.CharInfo[frame][ciSlot][0];
               if (ciIdx >= 0 && ciIdx < part.pi.length) {
                  PartImage pi = part.pi[ciIdx];
                  if (pi != null && pi.id > 0) {
                     BufferedImage img = this.getPartImage(pi.id);
                     if (img != null) {
                        int ciX = CharInfoData.CharInfo[frame][ciSlot][1];
                        int ciY = CharInfoData.CharInfo[frame][ciSlot][2];

                        boolean isHeadRelated = (slotIndex == 0 || slotIndex == 4 || slotIndex == 5);
                        if (isHeadRelated && (slotIndex != 0 || !OffsetUtility.isKoLechHead(headId))) {
                           ciY += lechYHead;
                        }

                        if (ciSlot == 5 && (bodyId == 950 || bodyId == 963 || bodyId == 972)) {
                           ciY -= 20;
                           ciX -= 5;
                        }

                        int finalCiX = (int)((ciX + pi.dx) * LOGIC_SCALE * zoom);
                        int finalCiY = (int)((ciY + pi.dy) * LOGIC_SCALE * zoom);
                        int sx = ox + finalCiX;
                        int sy = oy + finalCiY;

                        AffineTransform at = new AffineTransform();
                        at.translate(sx, sy);
                        at.scale(zoom, zoom);
                        g2d.drawImage(img, at, null);
                     }
                  }
               }
            }
         }
      }
   }
}
