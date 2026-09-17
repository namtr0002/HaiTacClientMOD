package com.deplor.haitactihontool.mob;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.part.CharInfoData;
import com.deplor.haitactihontool.part.OffsetUtility;
import com.deplor.haitactihontool.part.PartDataManager;
import com.deplor.haitactihontool.part.PartImage;
import com.deplor.haitactihontool.part.mPart;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import org.json.JSONArray;

public class MobCanvas extends JPanel {
   private MobTemplate currentMob;
   private BufferedImage currentSprite;
   private File currentMobImgFile;
   private int animTick = 0;
   private int action = 0;
   private boolean facingRight = false;
   private double zoom = 2.0;
   private int offsetX = 0;
   private int offsetY = 0;
   private Point dragStart;
   private boolean isPlaying = true;
   private int fps = 10;
   private Timer animTimer;
   private boolean showBoundingBox = true;
   private boolean showAnchor = true;
   private short[] mWearing;
   private List<mPart> allParts;
   private final Map<Integer, BufferedImage> partImageCache = new HashMap<>();
   private static final BufferedImage MISSING = new BufferedImage(1, 1, 2);
   private static final double LOGIC_SCALE = 4.0;
   private static final int[][] MON_012 = new int[][]{
      {0, 0, 0, 0, 1, 1}, {1, 1, 0, 0, 2, 2}, {3, 3, 3, 3, 2, 2, 2, 2}, {4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, {4, 4, 4, 4, 4, 4}
   };
   private static final int[][] MON_234 = new int[][]{
      {0, 0, 0, 0, 0, 1, 1, 1}, {2, 2, 3, 3, 4, 4}, {5, 5, 5, 5, 4, 4, 4, 4}, {6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, {6, 6, 6, 6, 6, 6}
   };
   private static final int[][] MON_12 = new int[][]{
      {0, 0, 0, 0, 0, 0, 0, 1, 1, 1}, {1, 1, 1, 2, 2, 2}, {3, 3, 3, 3, 2, 2, 2, 2}, {4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, {4, 4, 4, 4, 4, 4}
   };
   private static final int[][] MON_2345 = new int[][]{
      {0, 0, 0, 0, 0, 1, 1, 1}, {2, 2, 3, 3, 4, 4, 5, 5}, {6, 6, 6, 6, 5, 5, 5, 5}, {7, 7, 7, 7, 7, 7, 7, 7, 7, 7}, {7, 7, 7, 7, 7, 7, 7, 7, 7, 7}
   };
   private static final int[][] MON_1234 = new int[][]{
      {0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
      {2, 2, 2, 3, 3, 3, 4, 4, 4, 1, 1, 1},
      {5, 5, 5, 5, 4, 4, 4, 4},
      {6, 6, 6, 6, 6, 6, 6, 6, 6, 6},
      {6, 6, 6, 6, 6, 6, 6, 6, 6, 6}
   };
   private static final int[][] MON_23 = new int[][]{
      {0, 0, 0, 0, 0, 0, 0, 1, 1, 1}, {3, 3, 3, 3, 2, 2, 2, 2}, {3, 3, 3, 3, 4, 4, 4, 4}, {5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, {5, 5, 5, 5, 5, 5}
   };
   private static final int[][] MON_123 = new int[][]{
      {0, 0, 0, 0, 1, 1}, {3, 3, 2, 2, 1, 1}, {3, 3, 3, 3, 4, 4, 4, 4}, {5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, {5, 5, 5, 5, 5, 5}
   };
   private static final int[][] MON_0MOVE = new int[][]{
      {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1}, {0, 0, 0, 0, 1, 1}, {2, 2, 2, 3, 3, 3, 3}, {4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, {4, 4, 4, 4, 4, 4}
   };
   private static final int[][] MON_BUNHIN = new int[][]{{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1}};
   private static final int[][] MON_MOVE01 = new int[][]{
      {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 1, 1}, {1, 1, 1, 2, 2, 2}, {3, 3, 3, 3, 3, 3, 3, 3, 3, 3}, {3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
   };
   private static final int[][] MON_2343 = new int[][]{
      {0, 0, 0, 0, 0, 1, 1, 1}, {2, 2, 3, 3, 4, 4, 3, 3}, {5, 5, 5, 5, 4, 4, 4, 4}, {6, 6, 6, 6, 6, 6, 6, 6, 6, 6}, {6, 6, 6, 6, 6, 6}
   };
   private static final int[][] MON_BINGO = new int[][]{
      {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1},
      {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1},
      {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1},
      {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1},
      {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1}
   };
   private static final int[][] MON_KUNGFU = new int[][]{
      {0, 0, 0, 0, 0, 1, 1, 1}, {1, 1, 1, 2, 2, 2, 3, 3, 3, 2, 2, 2}, {4, 4, 5, 5, 5, 6, 6, 7, 7, 7}, {8, 8, 8, 8, 8, 8, 8, 8, 8}, {8, 8, 8, 8, 8, 8, 8, 8, 8}
   };
   private static final int[][] MON_1232 = new int[][]{
      {0, 0, 0, 0, 0, 0, 0, 0}, {1, 1, 1, 2, 2, 2, 3, 3, 3, 2, 2, 2}, {3, 3, 3, 4, 4, 4, 4, 4}, {5, 5, 5, 5, 5, 5, 5, 5}, {5, 5, 5, 5, 5, 5}
   };
   private static final int[][] MON_ICE_SNOW = new int[][]{
      {0, 0, 0, 0, 2, 2, 2, 2}, {1, 1, 1, 2, 2, 3, 3, 3, 2, 2}, {1, 1, 1, 2, 2, 3, 3, 3, 2, 2}, {1, 1, 1, 2, 2, 3, 3, 3, 2, 2}, {1, 1, 1, 2, 2, 3, 3, 3, 2, 2}
   };
   private static final int[][] MON_POKEMON = new int[][]{
      {0, 0, 0, 0, 1, 1, 1, 1}, {1, 1, 2, 2, 1, 1, 3, 3}, {1, 1, 2, 2, 1, 1, 3, 3}, {1, 1, 2, 2, 1, 1, 3, 3}, {1, 1, 2, 2, 1, 1, 3, 3}
   };
   private static final int[][] MON_TRU = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}};
   private static final int[][] MON_BANH_KEM = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
   private static final int[][] MON_PET_DOG = new int[][]{
      {0, 0, 0, 0, 1, 1, 1, 1}, {0, 0, 1, 1, 2, 2, 3, 3}, {0, 0, 1, 1, 2, 2, 3, 3}, {0, 0, 1, 1, 2, 2, 3, 3}, {0, 0, 1, 1, 2, 2, 3, 3}
   };
   private MobCanvas.OnAnimTickListener animListener;
   private static final Map<Integer, BufferedImage> mobSpriteCache = new ConcurrentHashMap<>();
   private static final BufferedImage MISSING_MOB_SPRITE = new BufferedImage(1, 1, 2);

   public MobCanvas() {
      this.setBackground(new Color(20, 20, 25));
      this.setFocusable(true);

      try {
         this.allParts = PartDataManager.loadParts();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            MobCanvas.this.requestFocusInWindow();
            MobCanvas.this.dragStart = e.getPoint();
         }
      });
      this.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (MobCanvas.this.dragStart != null) {
               MobCanvas.this.offsetX = MobCanvas.this.offsetX + (e.getX() - MobCanvas.this.dragStart.x);
               MobCanvas.this.offsetY = MobCanvas.this.offsetY + (e.getY() - MobCanvas.this.dragStart.y);
               MobCanvas.this.dragStart = e.getPoint();
               MobCanvas.this.repaint();
            }
         }
      });
      this.addMouseWheelListener(e -> {
         if (e.isShiftDown()) {
            int delta = e.getWheelRotation() > 0 ? -30 : 30;
            this.panX(delta);
         } else if (e.getWheelRotation() < 0) {
            this.zoomIn();
         } else {
            this.zoomOut();
         }
      });
      this.setupKeyBindings();
      this.startTimer();
   }

   private void setupKeyBindings() {
      InputMap im = this.getInputMap(2);
      ActionMap am = this.getActionMap();
      im.put(KeyStroke.getKeyStroke(38, 0), "zoom_in");
      im.put(KeyStroke.getKeyStroke(38, 128), "zoom_in");
      am.put("zoom_in", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            MobCanvas.this.zoomIn();
         }
      });
      im.put(KeyStroke.getKeyStroke(40, 0), "zoom_out");
      im.put(KeyStroke.getKeyStroke(40, 128), "zoom_out");
      am.put("zoom_out", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            MobCanvas.this.zoomOut();
         }
      });
      im.put(KeyStroke.getKeyStroke(37, 0), "pan_left");
      im.put(KeyStroke.getKeyStroke(37, 64), "pan_left");
      am.put("pan_left", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            MobCanvas.this.panX(-25);
         }
      });
      im.put(KeyStroke.getKeyStroke(39, 0), "pan_right");
      im.put(KeyStroke.getKeyStroke(39, 64), "pan_right");
      am.put("pan_right", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            MobCanvas.this.panX(25);
         }
      });
      im.put(KeyStroke.getKeyStroke(38, 64), "pan_up");
      am.put("pan_up", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            MobCanvas.this.panY(-25);
         }
      });
      im.put(KeyStroke.getKeyStroke(40, 64), "pan_down");
      am.put("pan_down", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            MobCanvas.this.panY(25);
         }
      });
   }

   private void startTimer() {
      if (this.animTimer != null) {
         this.animTimer.stop();
      }

      int delay = Math.max(20, 1000 / this.fps);
      this.animTimer = new Timer(delay, e -> {
         if (this.isPlaying && (this.currentSprite != null || this.currentMob != null && this.currentMob.ishuman == 1)) {
            this.animTick++;
            int max = this.getSequenceLength();
            if (this.animTick >= max) {
               this.animTick = 0;
            }

            if (this.animListener != null) {
               this.animListener.onTick(this.animTick, max);
            }

            this.repaint();
         }
      });
      this.animTimer.start();
   }

   public void setFps(int fps) {
      this.fps = Math.max(1, Math.min(60, fps));
      this.startTimer();
   }

   public int getFps() {
      return this.fps;
   }

   public void setAnimListener(MobCanvas.OnAnimTickListener listener) {
      this.animListener = listener;
   }

   public void reloadPartsData() {
      try {
         this.allParts = PartDataManager.loadParts();
         this.partImageCache.clear();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.setMob(this.currentMob);
   }

   public void setMob(MobTemplate mob) {
      this.currentMob = mob;
      this.currentSprite = null;
      this.currentMobImgFile = null;
      this.animTick = 0;
      this.mWearing = null;
      this.partImageCache.clear();
      if (mob != null) {
         if (mob.ishuman == 0) {
            this.loadNonHumanMobImage(mob);
         } else if (mob.ishuman == 1) {
            this.parseHumanMobParts(mob);
         }
      }

      this.resetView();
   }

   public static void clearMobSpriteCache() {
      mobSpriteCache.clear();
   }

   public BufferedImage loadSpriteImageForMob(MobTemplate mob) {
      if (mob != null && mob.ishuman != 1) {
         try {
            int iconId = 0;
            String rawIdIcon = mob.idicon != null ? mob.idicon.trim() : "";
            if (rawIdIcon.startsWith("[")) {
               String[] parts = rawIdIcon.replace("[", "").replace("]", "").split(",");
               if (parts.length >= 2) {
                  iconId = Integer.parseInt(parts[1].trim());
               } else if (parts.length >= 1 && !parts[0].trim().isEmpty()) {
                  iconId = Integer.parseInt(parts[0].trim());
               }
            } else if (!rawIdIcon.isEmpty()) {
               iconId = Integer.parseInt(rawIdIcon);
            }

            BufferedImage cached = mobSpriteCache.get(iconId);
            if (cached == MISSING_MOB_SPRITE) {
               return null;
            }

            if (cached != null) {
               return cached;
            }

            String baseDir = AppConfig.getResolvedPath("path_mob_img", "Data/Mob/img/");
            if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
               baseDir = baseDir + "/";
            }

            List<String> candidates = new ArrayList<>();
            candidates.add(baseDir + "x4/" + (iconId + 1000) + ".png");
            candidates.add(baseDir + (iconId + 1000) + ".png");
            candidates.add(baseDir + "x4/" + iconId + ".png");
            candidates.add(baseDir + iconId + ".png");
            candidates.add(baseDir + "x4/" + (iconId + 1200) + ".png");
            candidates.add(baseDir + (iconId + 1200) + ".png");

            for (String p : candidates) {
               File f = new File(p);
               if (f.exists()) {
                  BufferedImage img = ImageIO.read(f);
                  if (img != null) {
                     mobSpriteCache.put(iconId, img);
                     return img;
                  }
               }
            }

            mobSpriteCache.put(iconId, MISSING_MOB_SPRITE);
         } catch (Exception var11) {
         }

         return null;
      } else {
         return null;
      }
   }

   private void loadNonHumanMobImage(MobTemplate mob) {
      this.currentSprite = this.loadSpriteImageForMob(mob);
   }

   public static short[] parseHumanWearing(String idicon) {
      if (idicon != null && !idicon.isEmpty()) {
         try {
            JSONArray js = new JSONArray(idicon.trim());
            int headId = js.length() > 1 ? js.getInt(1) : -1;
            int hairId = js.length() > 2 ? js.getInt(2) : -1;
            short[] wear = null;
            if (js.length() > 3 && !js.isNull(3)) {
               JSONArray js2 = js.getJSONArray(3);
               wear = new short[js2.length()];

               for (int i = 0; i < js2.length(); i++) {
                  wear[i] = (short)js2.getInt(i);
               }
            }

            short[] mWearing = new short[8];

            for (int i = 0; i < 8; i++) {
               mWearing[i] = -1;
            }

            mWearing[6] = (short)headId;
            mWearing[7] = (short)hairId;
            if (wear != null) {
               if (wear.length > 0) {
                  mWearing[0] = wear[0];
               }

               if (wear.length > 1) {
                  mWearing[1] = wear[1];
               }

               if (wear.length > 2) {
                  mWearing[2] = wear[2];
               }

               if (wear.length > 3) {
                  mWearing[3] = wear[3];
               }

               if (wear.length > 4) {
                  mWearing[4] = wear[4];
               }

               if (wear.length > 5) {
                  mWearing[5] = wear[5];
               }
            }

            return mWearing;
         } catch (Exception var7) {
            return null;
         }
      } else {
         return null;
      }
   }

   private void parseHumanMobParts(MobTemplate mob) {
      this.mWearing = parseHumanWearing(mob.idicon);
   }

   public BufferedImage getPartImage(short id) {
      if (id < 0) {
         return null;
      } else {
         int key = id & '\uffff';
         BufferedImage cached = this.partImageCache.get(key);
         if (cached == MISSING) {
            return null;
         } else if (cached != null) {
            return cached;
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
                        this.partImageCache.put(key, img);
                        return img;
                     }
                  } catch (Exception var10) {
                  }
               }
            }

            this.partImageCache.put(key, MISSING);
            return null;
         }
      }
   }

   public List<mPart> getAllParts() {
      return this.allParts;
   }

   private mPart getpaintForWearing(short[] wearArray, int index) {
      if (wearArray != null && this.allParts != null) {
         short partId = -1;
         switch (index) {
            case 0:
               if (wearArray.length > 6) {
                  partId = wearArray[6];
               }
               break;
            case 1:
               if (wearArray.length > 5) {
                  partId = wearArray[5];
               }
               break;
            case 2:
               if (wearArray.length > 3) {
                  partId = wearArray[3];
               }
               break;
            case 3:
               if (wearArray.length > 0) {
                  partId = wearArray[0];
               }
               break;
            case 4:
               if (wearArray.length > 1) {
                  partId = wearArray[1];
               }
               break;
            case 5:
               if (wearArray.length > 7) {
                  partId = wearArray[7];
               }
               break;
            case 6:
               if (wearArray.length > 2) {
                  partId = wearArray[2];
               }
               break;
            case 7:
               if (wearArray.length > 4) {
                  partId = wearArray[4];
               }
         }

         if (partId < 0) {
            return null;
         } else {
            for (mPart p : this.allParts) {
               if (p.id == partId) {
                  return p;
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   private mPart getpaint(int index) {
      return this.getpaintForWearing(this.mWearing, index);
   }

   private int[] screenTopLeft(int ox, int oy, int frame, int slotIndex, PartImage pi) {
      int ciSlot = slotIndex;
      if (slotIndex == 6) {
         ciSlot = 3;
      } else if (slotIndex == 7) {
         ciSlot = 6;
      }

      int ciX = CharInfoData.CharInfo[frame][ciSlot][1];
      int ciY = CharInfoData.CharInfo[frame][ciSlot][2];
      short bodyId = this.mWearing != null && this.mWearing.length > 3 ? this.mWearing[3] : -1;
      short headId = this.mWearing != null && this.mWearing.length > 6 ? this.mWearing[6] : -1;
      int lechYHead = OffsetUtility.getLechYHead(bodyId);
      boolean isHeadRelated = slotIndex == 0 || slotIndex == 4 || slotIndex == 5;
      if (isHeadRelated && (slotIndex != 0 || !OffsetUtility.isKoLechHead(headId))) {
         ciY += lechYHead;
      }

      if (ciSlot == 5 && (bodyId == 950 || bodyId == 963 || bodyId == 972)) {
         ciY -= 20;
         ciX -= 5;
      }

      int finalCiX = (int)((ciX + pi.dx) * 4.0 * this.zoom);
      int finalCiY = (int)((ciY + pi.dy) * 4.0 * this.zoom);
      int screenY = oy + finalCiY;
      int screenX;
      if (this.facingRight) {
         screenX = ox - finalCiX;
      } else {
         screenX = ox + finalCiX;
      }

      return new int[]{screenX, screenY};
   }

   public void setAction(int action) {
      this.action = action;
      this.animTick = 0;
      this.repaint();
   }

   public int getAction() {
      return this.action;
   }

   public void setFacingRight(boolean facingRight) {
      this.facingRight = facingRight;
      this.repaint();
   }

   public boolean isFacingRight() {
      return this.facingRight;
   }

   public void setShowBoundingBox(boolean show) {
      this.showBoundingBox = show;
      this.repaint();
   }

   public boolean isShowBoundingBox() {
      return this.showBoundingBox;
   }

   public void setShowAnchor(boolean show) {
      this.showAnchor = show;
      this.repaint();
   }

   public boolean isShowAnchor() {
      return this.showAnchor;
   }

   public void setZoom(double zoom) {
      this.zoom = Math.max(0.5, Math.min(10.0, zoom));
      this.offsetX = 0;
      this.offsetY = 0;
      this.repaint();
   }

   public double getZoom() {
      return this.zoom;
   }

   public void zoomIn() {
      this.setZoom(this.zoom + 0.5);
   }

   public void zoomOut() {
      this.setZoom(this.zoom - 0.5);
   }

   public void panX(int deltaX) {
      this.offsetX += deltaX;
      this.repaint();
   }

   public void panY(int deltaY) {
      this.offsetY += deltaY;
      this.repaint();
   }

   public void setPlaying(boolean playing) {
      this.isPlaying = playing;
   }

   public boolean isPlaying() {
      return this.isPlaying;
   }

   public void setAnimTick(int tick) {
      this.animTick = tick;
      this.repaint();
   }

   public int getAnimTick() {
      return this.animTick;
   }

   public BufferedImage renderFrameThumbnail(int act, int tick, int thumbW, int thumbH) {
      return this.renderMobThumbnail(this.currentMob, act, tick, thumbW, thumbH);
   }

   public BufferedImage renderMobThumbnail(MobTemplate mob, int act, int tick, int thumbW, int thumbH) {
      if (mob == null) {
         return null;
      } else {
         BufferedImage thumb = new BufferedImage(thumbW, thumbH, 2);
         Graphics2D g2 = thumb.createGraphics();
         g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
         g2.setColor(new Color(22, 22, 28));
         g2.fillRect(0, 0, thumbW, thumbH);
         if (mob.ishuman == 1) {
            short[] wear = parseHumanWearing(mob.idicon);
            if (wear != null && this.allParts != null) {
               int fr;
               if (act == -1) {
                  int standLen = CharInfoData.FE_STAND.length;
                  int walkLen = CharInfoData.FE_WALK.length;
                  int attackLen = CharInfoData.FE_ATTACK.length;
                  int totalLen = standLen + walkLen + attackLen;
                  int t = tick % Math.max(1, totalLen);
                  if (t < standLen) {
                     fr = CharInfoData.FE_STAND[t];
                  } else if (t < standLen + walkLen) {
                     fr = CharInfoData.FE_WALK[t - standLen];
                  } else {
                     fr = CharInfoData.FE_ATTACK[t - standLen - walkLen];
                  }
               } else {
                  int[] seq = CharInfoData.FE_STAND;
                  if (act == 1) {
                     seq = CharInfoData.FE_WALK;
                  } else if (act == 2) {
                     seq = CharInfoData.FE_ATTACK;
                  }

                  fr = seq[tick % Math.max(1, seq.length)];
               }

               double mobW = 128.0;
               double mobH = 200.0;
               double scale = Math.min((thumbW - 4) / mobW, (thumbH - 4) / mobH);
               scale = Math.min(1.2, scale);
               int ox = thumbW / 2;
               int oy = (int)(thumbH / 2.0 + mobH * scale / 2.0 - 4.0 * scale);
               int[] sort = this.facingRight ? new int[]{7, 1, 2, 0, 5, 4, 3, 6} : new int[]{6, 3, 4, 5, 0, 2, 1, 7};

               for (int slotIndex : sort) {
                  mPart part = this.getpaintForWearing(wear, slotIndex);
                  if (part != null) {
                     int ciSlot = slotIndex == 6 ? 3 : (slotIndex == 7 ? 6 : slotIndex);
                     if (fr >= 0 && fr < CharInfoData.CharInfo.length) {
                        int ciIdx = CharInfoData.CharInfo[fr][ciSlot][0];
                        if (ciIdx >= 0 && ciIdx < part.pi.length) {
                           PartImage pi = part.pi[ciIdx];
                           if (pi != null && pi.id >= 0) {
                              BufferedImage img = this.getPartImage(pi.id);
                              if (img != null) {
                                 int ciX = CharInfoData.CharInfo[fr][ciSlot][1];
                                 int ciY = CharInfoData.CharInfo[fr][ciSlot][2];
                                 short bodyId = wear.length > 3 ? wear[3] : -1;
                                 short headId = wear.length > 6 ? wear[6] : -1;
                                 int lechYHead = OffsetUtility.getLechYHead(bodyId);
                                 boolean isHeadRelated = slotIndex == 0 || slotIndex == 4 || slotIndex == 5;
                                 if (isHeadRelated && (slotIndex != 0 || !OffsetUtility.isKoLechHead(headId))) {
                                    ciY += lechYHead;
                                 }

                                 if (ciSlot == 5 && (bodyId == 950 || bodyId == 963 || bodyId == 972)) {
                                    ciY -= 20;
                                    ciX -= 5;
                                 }

                                 int finalCiX = (int)((ciX + pi.dx) * 4.0 * scale);
                                 int finalCiY = (int)((ciY + pi.dy) * 4.0 * scale);
                                 int sx = this.facingRight ? ox - finalCiX : ox + finalCiX;
                                 int sy = oy + finalCiY;
                                 AffineTransform at = new AffineTransform();
                                 at.translate(sx, sy);
                                 if (this.facingRight) {
                                    at.scale(-scale, scale);
                                 } else {
                                    at.scale(scale, scale);
                                 }

                                 g2.drawImage(img, at, null);
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            BufferedImage spriteImg = mob == this.currentMob && this.currentSprite != null ? this.currentSprite : this.loadSpriteImageForMob(mob);
            if (spriteImg != null) {
               int W = spriteImg.getWidth();
               int H = spriteImg.getHeight();
               int presetFrames = getFrameCountByMoveType(mob.typemove);
               int hOne;
               int totalFrames;
               if (mob.hOne == 1 || mob.hOne <= 0 || mob.typemove == 20) {
                  hOne = H;
                  totalFrames = 1;
               } else if (mob.hOne > 1 && mob.hOne < 15) {
                  totalFrames = Math.max(1, mob.hOne);
                  hOne = Math.max(1, H / totalFrames);
               } else {
                  totalFrames = Math.max(1, presetFrames);
                  hOne = Math.max(1, H / totalFrames);
               }

               int frameIdx = 0;
               if (totalFrames > 1) {
                  int[] seq = act == -1 ? getAllActionSeq(mob.typemove) : getMonsterActionSeq(mob.typemove, act);
                  frameIdx = seq[tick % Math.max(1, seq.length)] % totalFrames;
               }

               double scale = Math.min((double)(thumbW - 4) / Math.max(1, W), (double)(thumbH - 4) / Math.max(1, hOne));
               scale = Math.min(2.0, scale);
               int drawW = Math.max(1, (int)(W * scale));
               int drawH = Math.max(1, (int)(hOne * scale));
               int dx1 = (thumbW - drawW) / 2;
               int dy1 = (thumbH - drawH) / 2;
               int dx2 = dx1 + drawW;
               int dy2 = dy1 + drawH;
               int sy1 = frameIdx * hOne;
               int sy2 = Math.min(H, sy1 + hOne);
               if (this.facingRight) {
                  g2.drawImage(spriteImg, dx2, dy1, dx1, dy2, 0, sy1, W, sy2, null);
               } else {
                  g2.drawImage(spriteImg, dx1, dy1, dx2, dy2, 0, sy1, W, sy2, null);
               }
            }
         }

         g2.dispose();
         return thumb;
      }
   }

   public int getSequenceLength() {
      if (this.currentMob == null) {
         return 1;
      } else if (this.currentMob.ishuman == 1) {
         if (this.action == -1) {
            return CharInfoData.FE_STAND.length + CharInfoData.FE_WALK.length + CharInfoData.FE_ATTACK.length;
         } else {
            int[] seq = CharInfoData.FE_STAND;
            if (this.action == 1) {
               seq = CharInfoData.FE_WALK;
            } else if (this.action == 2) {
               seq = CharInfoData.FE_ATTACK;
            }

            return seq.length;
         }
      } else if (this.action == -1) {
         return getAllActionSeq(this.currentMob.typemove).length;
      } else {
         int[] seq = getMonsterActionSeq(this.currentMob.typemove, this.action);
         return seq.length;
      }
   }

   public static int getFrameCountByMoveType(byte typemove) {
      switch (typemove) {
         case 0:
            return 5;
         case 1:
            return 7;
         case 2:
            return 8;
         case 3:
            return 5;
         case 4:
            return 7;
         case 5:
            return 6;
         case 6:
            return 6;
         case 7:
         case 13:
         default:
            return 4;
         case 8:
            return 2;
         case 9:
            return 5;
         case 10:
            return 4;
         case 11:
            return 7;
         case 12:
            return 6;
         case 14:
            return 4;
         case 15:
            return 9;
         case 16:
            return 6;
         case 17:
            return 4;
         case 18:
            return 4;
         case 19:
            return 2;
         case 20:
            return 1;
         case 21:
            return 5;
      }
   }

   public static int[] getAllActionSeq(byte typemove) {
      int[] s0 = getMonsterActionSeq(typemove, 0);
      int[] s1 = getMonsterActionSeq(typemove, 1);
      int[] s2 = getMonsterActionSeq(typemove, 2);
      int[] s3 = getMonsterActionSeq(typemove, 3);
      int[] s4 = getMonsterActionSeq(typemove, 4);
      int[] combined = new int[s0.length + s1.length + s2.length + s3.length + s4.length];
      System.arraycopy(s0, 0, combined, 0, s0.length);
      System.arraycopy(s1, 0, combined, s0.length, s1.length);
      System.arraycopy(s2, 0, combined, s0.length + s1.length, s2.length);
      System.arraycopy(s3, 0, combined, s0.length + s1.length + s2.length, s3.length);
      System.arraycopy(s4, 0, combined, s0.length + s1.length + s2.length + s3.length, s4.length);
      return combined;
   }

   public static int[] getMonsterActionSeq(byte typemove, int action) {
      int actIdx = action;
      if (action < 0 || action > 4) {
         actIdx = 0;
      }
      int[][] matrix = switch (typemove) {
         case 0 -> MON_12;
         case 1 -> MON_234;
         case 2 -> MON_2345;
         case 3 -> MON_012;
         case 4 -> MON_1234;
         case 5, 12 -> MON_23;
         case 6 -> MON_123;
         default -> MON_MOVE01;
         case 8 -> MON_BUNHIN;
         case 9 -> MON_0MOVE;
         case 10 -> MON_MOVE01;
         case 11 -> MON_2343;
         case 14 -> MON_BINGO;
         case 15 -> MON_KUNGFU;
         case 16 -> MON_1232;
         case 17 -> MON_ICE_SNOW;
         case 18 -> MON_POKEMON;
         case 19 -> MON_TRU;
         case 20 -> MON_BANH_KEM;
         case 21 -> MON_PET_DOG;
      };
      return actIdx < matrix.length ? matrix[actIdx] : matrix[0];
   }

   public void resetView() {
      this.offsetX = 0;
      this.offsetY = 0;
      this.zoom = 2.0;
      this.repaint();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      g2.setColor(new Color(30, 30, 35));
      int gs = Math.max(10, (int)(16.0 * this.zoom));
      int gox = (this.offsetX % gs + gs) % gs;
      int goy = (this.offsetY % gs + gs) % gs;

      for (int x = gox; x < this.getWidth(); x += gs) {
         g2.drawLine(x, 0, x, this.getHeight());
      }

      for (int y = goy; y < this.getHeight(); y += gs) {
         g2.drawLine(0, y, this.getWidth(), y);
      }

      int cx = this.getWidth() / 2;
      int cy = this.getHeight() / 2;
      int ox = cx + this.offsetX;
      if (this.currentMob == null) {
         g2.setColor(Color.YELLOW);
         g2.setFont(Theme.F_MAIN);
         g2.drawString("Chưa chọn Mob", 15, 25);
      } else {
         int oy = cy + this.offsetY;
         if (this.currentMob.ishuman == 1) {
            oy = cy + (int)(104.0 * this.zoom / 2.0) + this.offsetY;
         } else if (this.currentSprite != null) {
            int H = this.currentSprite.getHeight();
            int presetFrames = getFrameCountByMoveType(this.currentMob.typemove);
            int hOne;
            if (this.currentMob.hOne == 1 || this.currentMob.hOne <= 0 || this.currentMob.typemove == 20) {
               hOne = H;
            } else if (this.currentMob.hOne > 1 && this.currentMob.hOne < 15) {
               hOne = Math.max(1, H / Math.max(1, this.currentMob.hOne));
            } else {
               hOne = Math.max(1, H / Math.max(1, presetFrames));
            }

            oy = cy + (int)(hOne * this.zoom / 2.0) + this.offsetY;
         }

         g2.setColor(Theme.ACCENT);
         g2.drawLine(0, oy, this.getWidth(), oy);
         g2.drawLine(ox, 0, ox, this.getHeight());
         g2.setColor(Color.YELLOW);
         g2.setFont(Theme.F_TINY);
         String mobTypeStr = this.currentMob.ishuman == 1 ? "Human Mob (Part)" : "Monster/Beast (Sprite)";
         String actName = this.action == -1 ? "All Pose" : String.valueOf(this.action);
         g2.drawString(
            String.format(
               "Mob ID: %d | Tên: %s | Lv: %d | Type: %s | MoveType: %d | Action: %s | Zoom: %.1fx",
               this.currentMob.id,
               this.currentMob.name,
               this.currentMob.level,
               mobTypeStr,
               this.currentMob.typemove,
               actName,
               this.zoom
            ),
            15,
            25
         );
         g2.setColor(new Color(0, 0, 0, 100));
         int shadowW = (int)(36.0 * this.zoom);
         int shadowH = (int)(12.0 * this.zoom);
         g2.fillOval(ox - shadowW / 2, oy - shadowH / 2, shadowW, shadowH);
         if (this.currentMob.ishuman == 1) {
            if (this.mWearing != null && this.allParts != null) {
               int fr;
               if (this.action == -1) {
                  int standLen = CharInfoData.FE_STAND.length;
                  int walkLen = CharInfoData.FE_WALK.length;
                  int attackLen = CharInfoData.FE_ATTACK.length;
                  int totalLen = standLen + walkLen + attackLen;
                  int tick = this.animTick % totalLen;
                  if (tick < standLen) {
                     fr = CharInfoData.FE_STAND[tick];
                  } else if (tick < standLen + walkLen) {
                     fr = CharInfoData.FE_WALK[tick - standLen];
                  } else {
                     fr = CharInfoData.FE_ATTACK[tick - standLen - walkLen];
                  }
               } else {
                  int[] seq = CharInfoData.FE_STAND;
                  if (this.action == 1) {
                     seq = CharInfoData.FE_WALK;
                  } else if (this.action == 2) {
                     seq = CharInfoData.FE_ATTACK;
                  }

                  fr = seq[this.animTick % seq.length];
               }

               int[] sort = this.facingRight ? new int[]{7, 1, 2, 0, 5, 4, 3, 6} : new int[]{6, 3, 4, 5, 0, 2, 1, 7};

               for (int slotIndex : sort) {
                  mPart part = this.getpaint(slotIndex);
                  if (part != null) {
                     int ciSlot = slotIndex;
                     if (slotIndex == 6) {
                        ciSlot = 3;
                     } else if (slotIndex == 7) {
                        ciSlot = 6;
                     }

                     int ciIdx = CharInfoData.CharInfo[fr][ciSlot][0];
                     if (ciIdx >= 0 && ciIdx < part.pi.length) {
                        PartImage pi = part.pi[ciIdx];
                        if (pi != null && pi.id >= 0) {
                           BufferedImage img = this.getPartImage(pi.id);
                           if (img != null) {
                              int[] sc = this.screenTopLeft(ox, oy, fr, slotIndex, pi);
                              AffineTransform at = new AffineTransform();
                              at.translate(sc[0], sc[1]);
                              if (this.facingRight) {
                                 at.scale(-this.zoom, this.zoom);
                              } else {
                                 at.scale(this.zoom, this.zoom);
                              }

                              g2.drawImage(img, at, null);
                           }
                        }
                     }
                  }
               }
            } else {
               g2.setColor(Theme.ERROR);
               g2.setFont(Theme.F_MAIN);
               g2.drawString("Không thể parse Part idicon của Mob human!", ox - 100, oy - 20);
            }
         } else if (this.currentSprite != null) {
            int W = this.currentSprite.getWidth();
            int H = this.currentSprite.getHeight();
            int presetFrames = getFrameCountByMoveType(this.currentMob.typemove);
            int hOne;
            int totalFrames;
            if (this.currentMob.hOne == 1 || this.currentMob.hOne <= 0 || this.currentMob.typemove == 20) {
               hOne = H;
               totalFrames = 1;
            } else if (this.currentMob.hOne > 1 && this.currentMob.hOne < 15) {
               totalFrames = Math.max(1, this.currentMob.hOne);
               hOne = Math.max(1, H / totalFrames);
            } else {
               totalFrames = Math.max(1, presetFrames);
               hOne = Math.max(1, H / totalFrames);
            }

            int frameIdx = 0;
            if (totalFrames > 1) {
               int[] seq;
               if (this.action == -1) {
                  seq = getAllActionSeq(this.currentMob.typemove);
               } else {
                  seq = getMonsterActionSeq(this.currentMob.typemove, this.action);
               }

               frameIdx = seq[this.animTick % seq.length] % totalFrames;
            }

            int destX1 = ox - (int)(W * this.zoom / 2.0);
            int destY1 = oy - (int)(hOne * this.zoom);
            int destX2 = ox + (int)(W * this.zoom / 2.0);
            int srcX1 = 0;
            int srcY1 = frameIdx * hOne;
            int srcY2 = Math.min(H, srcY1 + hOne);
            if (this.facingRight) {
               g2.drawImage(this.currentSprite, destX2, destY1, destX1, oy, srcX1, srcY1, W, srcY2, null);
            } else {
               g2.drawImage(this.currentSprite, destX1, destY1, destX2, oy, srcX1, srcY1, W, srcY2, null);
            }

            if (this.showBoundingBox) {
               g2.setColor(new Color(0, 255, 200, 180));
               g2.setStroke(new BasicStroke(1.5F, 0, 2, 0.0F, new float[]{4.0F, 4.0F}, 0.0F));
               g2.drawRect(destX1, destY1, Math.abs(destX2 - destX1), Math.abs(oy - destY1));
               g2.setStroke(new BasicStroke(1.0F));
            }
         } else {
            g2.setColor(Theme.ERROR);
            g2.setFont(Theme.F_MAIN);
            String pathInfo = AppConfig.getResolvedPath("path_mob_img", "Data/Mob/img/");
            String errStr = "Không tìm thấy ảnh Mob trong: " + pathInfo;
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(errStr, ox - fm.stringWidth(errStr) / 2, oy - (int)(20.0 * this.zoom));
         }

         if (this.showBoundingBox && this.currentMob.ishuman == 1) {
            g2.setColor(new Color(0, 255, 200, 180));
            g2.setStroke(new BasicStroke(1.5F, 0, 2, 0.0F, new float[]{4.0F, 4.0F}, 0.0F));
            int boxW = (int)(128.0 * this.zoom);
            int boxH = (int)(208.0 * this.zoom);
            g2.drawRect(ox - boxW / 2, oy - boxH, boxW, boxH);
            g2.setStroke(new BasicStroke(1.0F));
         }

         if (this.showAnchor) {
            g2.setColor(Color.RED);
            g2.drawLine(ox - 8, oy, ox + 8, oy);
            g2.drawLine(ox, oy - 8, ox, oy + 8);
            g2.drawOval(ox - 3, oy - 3, 6, 6);
         }
      }
   }

   public interface OnAnimTickListener {
      void onTick(int var1, int var2);
   }
}
