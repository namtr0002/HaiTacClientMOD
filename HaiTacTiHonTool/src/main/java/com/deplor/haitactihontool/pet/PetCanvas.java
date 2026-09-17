package com.deplor.haitactihontool.pet;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.Theme;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PetCanvas extends JPanel {
   private PetTemplate currentPet;
   private BufferedImage petImage;
   private File petImageFile;
   private int action = 0;
   private boolean facingRight = false;
   private double zoom = 3.0;
   private int offsetX = 0;
   private int offsetY = 0;
   private Point dragStart;
   private boolean isPlaying = true;
   private int fps = 10;
   private int animTick = 0;
   private Timer animTimer;
   private PetCanvas.OnAnimTickListener animListener;
   private static final Map<String, PetCanvas.PetImageResult> petImageCache = new ConcurrentHashMap<>();

   public PetCanvas() {
      this.setBackground(new Color(20, 20, 25));
      this.setFocusable(true);
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            PetCanvas.this.requestFocusInWindow();
            PetCanvas.this.dragStart = e.getPoint();
         }
      });
      this.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (PetCanvas.this.dragStart != null) {
               PetCanvas.this.offsetX = PetCanvas.this.offsetX + (e.getX() - PetCanvas.this.dragStart.x);
               PetCanvas.this.offsetY = PetCanvas.this.offsetY + (e.getY() - PetCanvas.this.dragStart.y);
               PetCanvas.this.dragStart = e.getPoint();
               PetCanvas.this.repaint();
            }
         }
      });
      this.addMouseWheelListener(e -> {
         if (e.getWheelRotation() < 0) {
            this.zoom *= 1.1;
         } else {
            this.zoom /= 1.1;
         }

         this.zoom = Math.max(1.0, Math.min(10.0, this.zoom));
         this.repaint();
      });
      this.startTimer();
   }

   private void startTimer() {
      if (this.animTimer != null) {
         this.animTimer.stop();
      }

      int delay = Math.max(20, 1000 / this.fps);
      this.animTimer = new Timer(delay, e -> {
         if (this.isPlaying && this.currentPet != null && this.petImage != null) {
            this.updateAnimation();
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

   public void setPet(PetTemplate pet) {
      this.currentPet = pet;
      this.animTick = 0;
      this.loadPetImage();
      this.repaint();
   }

   public void reloadImageOnly() {
      this.loadPetImage();
      this.repaint();
   }

   private void loadPetImage() {
      if (this.currentPet == null) {
         this.petImage = null;
         this.petImageFile = null;
      } else {
         PetCanvas.PetImageResult res = resolvePetImage(this.currentPet);
         if (res != null) {
            this.petImage = res.image;
            this.petImageFile = res.file;
         } else {
            this.petImage = null;
            this.petImageFile = null;
         }
      }
   }

   public static void clearPetImageCache() {
      petImageCache.clear();
   }

   public static int getPetSpriteId(PetTemplate pet) {
      if (pet == null) return -1;
      return pet.frame >= 1000 ? pet.frame : (pet.frame + 1000);
   }

   public static PetCanvas.PetImageResult resolvePetImage(PetTemplate pet) {
      if (pet == null) {
         return null;
      } else {
         int spriteId = getPetSpriteId(pet);
         String cacheKey = pet.id + "_" + pet.type + "_" + pet.frame + "_" + pet.icon;
         PetCanvas.PetImageResult cached = petImageCache.get(cacheKey);
         if (cached != null) {
            return cached;
         } else {
            String baseDir = AppConfig.getResolvedPath("path_pet_img", "Data/Pet/img/");
            if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
               baseDir = baseDir + "/";
            }

            List<String> candidates = new ArrayList<>();
            candidates.add(baseDir + "x4/" + spriteId + ".png");
            candidates.add(baseDir + spriteId + ".png");

            for (String path : candidates) {
               File f = new File(path);
               if (f.exists()) {
                  try {
                     BufferedImage img = ImageIO.read(f);
                     if (img != null) {
                        PetCanvas.PetImageResult res = new PetCanvas.PetImageResult(img, f);
                        petImageCache.put(cacheKey, res);
                        return res;
                     }
                  } catch (Exception var10) {
                  }
               }
            }

            return null;
         }
      }
   }

   private void updateAnimation() {
      if (this.currentPet != null) {
         int[] seq = getAnimSequence(this.currentPet.type, this.currentPet.frame, this.action);
         this.animTick++;
         if (this.animTick >= seq.length) {
            this.animTick = 0;
         }

         if (this.animListener != null) {
            this.animListener.onTick(this.animTick, seq.length);
         }
      }
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

   public void setZoom(double zoom) {
      this.zoom = zoom;
      this.repaint();
   }

   public double getZoom() {
      return this.zoom;
   }

   public void setPlaying(boolean playing) {
      this.isPlaying = playing;
   }

   public boolean isPlaying() {
      return this.isPlaying;
   }

   public void setAnimTick(int tick) {
      if (this.currentPet != null) {
         int[] seq = getAnimSequence(this.currentPet.type, this.currentPet.frame, this.action);
         if (tick >= 0 && tick < seq.length) {
            this.animTick = tick;
            this.repaint();
         }
      }
   }

   public int getAnimTick() {
      return this.animTick;
   }

   public int getCurrentSequenceLength() {
      return this.currentPet == null ? 1 : getAnimSequence(this.currentPet.type, this.currentPet.frame, this.action).length;
   }

   public void setAnimListener(PetCanvas.OnAnimTickListener l) {
      this.animListener = l;
   }

   public void resetView() {
      this.offsetX = 0;
      this.offsetY = 0;
      this.zoom = 3.0;
      this.repaint();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      g2d.setColor(new Color(30, 30, 35));
      int gs = Math.max(10, (int)(16.0 * this.zoom));
      int gox = (this.offsetX % gs + gs) % gs;
      int goy = (this.offsetY % gs + gs) % gs;

      for (int x = gox; x < this.getWidth(); x += gs) {
         g2d.drawLine(x, 0, x, this.getHeight());
      }

      for (int y = goy; y < this.getHeight(); y += gs) {
         g2d.drawLine(0, y, this.getWidth(), y);
      }

      int ox = this.getWidth() / 2 + this.offsetX;
      int oy = this.getHeight() / 2 + (int)(40.0 * this.zoom) + this.offsetY;
      g2d.setColor(new Color(120, 60, 60));
      g2d.drawLine(ox, 0, ox, this.getHeight());
      g2d.drawLine(0, oy, this.getWidth(), oy);
      int dyMain = 0;
      if (this.currentPet != null) {
         if (this.currentPet.type == 3 || this.currentPet.type == 5) {
            dyMain = 3 + (int)(Math.sin(this.animTick * 0.25) * 4.0);
         } else if (this.currentPet.type == 4) {
            dyMain = 18 + (int)(Math.sin(this.animTick * 0.35) * 5.0);
         }
      }

      g2d.setColor(Color.YELLOW);
      g2d.setFont(Theme.F_TINY);
      if (this.currentPet != null) {
         int spriteId = getPetSpriteId(this.currentPet);
         String imgFileName = this.petImageFile != null ? this.petImageFile.getName() : spriteId + ".png";
         g2d.drawString(
            String.format(
               "Pet ID: %d | Name: %s | Type: %d | Frame ID: %d (Sprite: %d.png) | File: %s | Zoom: %.1fx",
               this.currentPet.id,
               this.currentPet.name,
               this.currentPet.type,
               this.currentPet.frame,
               spriteId,
               imgFileName,
               this.zoom
            ),
            15,
            20
         );
      } else {
         g2d.drawString("No Pet Selected", 15, 20);
      }

      if (this.petImage != null && this.currentPet != null) {
         int presetFrames = getFrameCount(this.currentPet.type, this.currentPet.frame);
         int W = this.petImage.getWidth();
         int H = this.petImage.getHeight();
         int totalFrames;
         int hOne;
         if (H > (int)(W * 1.25) && H >= presetFrames * 4) {
            totalFrames = presetFrames;
            hOne = Math.max(1, H / presetFrames);
         } else {
            totalFrames = 1;
            hOne = H;
         }

         int[] seq = getAnimSequence(this.currentPet.type, this.currentPet.frame, this.action);
         int rawFrameIdx = seq[this.animTick % seq.length];
         int frameIdx = rawFrameIdx % totalFrames;
         int destX1 = ox - (int)(W * this.zoom / 2.0);
         int destY1 = oy - (int)((hOne + dyMain) * this.zoom);
         int destX2 = ox + (int)(W * this.zoom / 2.0);
         int destY2 = oy - (int)(dyMain * this.zoom);
         int srcX1 = 0;
         int srcY1 = frameIdx * hOne;
         int srcY2 = Math.min(H, srcY1 + hOne);
         if (this.facingRight) {
            g2d.drawImage(this.petImage, destX2, destY1, destX1, destY2, srcX1, srcY1, W, srcY2, null);
         } else {
            g2d.drawImage(this.petImage, destX1, destY1, destX2, destY2, srcX1, srcY1, W, srcY2, null);
         }
      } else if (this.currentPet != null) {
         g2d.setColor(Theme.ERROR);
         g2d.setFont(Theme.F_MAIN);
         int spriteId = getPetSpriteId(this.currentPet);
         String pathInfo = AppConfig.getResolvedPath("path_pet_img", "Data/Pet/img/");
         String warningStr = "Chưa có ảnh Sprite Pet: " + spriteId + ".png trong " + pathInfo;
         FontMetrics fm = g2d.getFontMetrics();
         g2d.drawString(warningStr, ox - fm.stringWidth(warningStr) / 2, oy - (int)(20.0 * this.zoom));
      }
   }

   public static int getFrameCount(int type, int frame) {
      switch (type) {
         case 0:
         case 1:
            return 9;
         case 2:
            return 5;
         case 3:
         case 5:
            return 6;
         case 4:
            return 3;
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         default:
            return 5;
         case 21:
            return frame != 55 && frame != 56 ? 5 : 7;
      }
   }

   public static int[] getAnimSequence(int type, int frameId, int action) {
      if (action < 0 || action > 4) {
         action = 0;
      }

      switch (action) {
         case 0:
            if (type == 0) {
               return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
            } else if (type == 1) {
               return new int[]{0, 0, 0, 0, 0, 1, 1, 1};
            } else if (type == 2) {
               return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
            } else {
               if (type != 3 && type != 5) {
                  if (type == 4) {
                     return new int[]{0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2};
                  }

                  if (type == 21) {
                     if (frameId != 55 && frameId != 56) {
                        return new int[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
                     }

                     return new int[]{5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6};
                  }

                  return new int[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
               }

               return new int[]{0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1};
            }
         case 1:
            if (type == 0) {
               return new int[]{3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6};
            } else if (type == 1) {
               return new int[]{1, 1, 1, 2, 2, 2, 3, 3, 3, 2, 2, 2};
            } else if (type == 2) {
               return new int[]{1, 1, 1, 2, 2, 2, 1, 1, 1, 3, 3, 3};
            } else {
               if (type != 3 && type != 5) {
                  if (type == 4) {
                     return new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2};
                  }

                  if (type == 21) {
                     return new int[]{2, 2, 3, 3, 4, 4};
                  }

                  return new int[]{2, 2, 3, 3, 4, 4};
               }

               return new int[]{3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5};
            }
         case 2:
            if (type == 0) {
               return new int[]{7, 7, 7, 7, 7, 8, 8, 8};
            } else if (type == 1) {
               return new int[]{4, 4, 5, 5, 5, 6, 6, 7, 7, 7};
            } else if (type == 2) {
               return new int[]{1, 1, 4, 4, 4, 4, 4, 4};
            } else {
               if (type != 3 && type != 5) {
                  if (type == 4) {
                     return new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2};
                  }

                  if (type == 21) {
                     return new int[]{2, 2, 3, 3, 4, 4};
                  }

                  return new int[]{2, 2, 3, 3, 4, 4};
               }

               return new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1};
            }
         case 3:
            if (type == 0) {
               return new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
            } else if (type == 1) {
               return new int[]{8, 8, 8, 8, 8, 8, 8, 8, 8};
            } else if (type == 2) {
               return new int[]{1, 1, 1, 1, 1, 1, 1, 1};
            } else {
               if (type != 3 && type != 5) {
                  if (type == 4) {
                     return new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2};
                  }

                  if (type == 21) {
                     return new int[]{2, 2, 3, 3, 4, 4};
                  }

                  return new int[]{2, 2, 3, 3, 4, 4};
               }

               return new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1};
            }
         case 4:
            if (type == 0) {
               return new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
            } else if (type == 1) {
               return new int[]{8, 8, 8, 8, 8, 8, 8, 8, 8};
            } else if (type == 2) {
               return new int[]{1, 1, 1, 1, 1, 1, 1, 1};
            } else {
               if (type != 3 && type != 5) {
                  if (type == 4) {
                     return new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2};
                  }

                  if (type == 21) {
                     return new int[]{2, 2, 3, 3, 4, 4};
                  }

                  return new int[]{2, 2, 3, 3, 4, 4};
               }

               return new int[]{0, 0, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1};
            }
         default:
            return new int[]{0};
      }
   }

   public interface OnAnimTickListener {
      void onTick(int var1, int var2);
   }

   public static class PetImageResult {
      public BufferedImage image;
      public File file;

      public PetImageResult(BufferedImage image, File file) {
         this.image = image;
         this.file = file;
      }
   }
}
