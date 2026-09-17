package com.deplor.haitactihontool.part;

import com.deplor.haitactihontool.config.AppConfig;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class PartCanvas extends JPanel {
   private int frameIndex = 0;
   private int direction = 0;
   private double zoom = 1.0;
   private int offsetX = 0;
   private int offsetY = 0;
   private Point dragStart;
   private short[] mWearing;
   private List<mPart> allParts;
   private mPart draggingPart = null;
   private int draggingSlot = -1;
   private int draggingImageIdx = -1;
   private Point lastMousePos;
   private PartCanvas.OnPartInteraction listener;
   private PartCanvas.OnAnimTickListener animListener;
   private boolean isPlaying = false;
   private static final int DRAG_THRESHOLD = 5;
   private Point dragOriginPoint = null;
   private boolean dragBeyondThreshold = false;
   private int startDx;
   private int startDy;
   private Timer animTimer;
   private int animTick = 0;
   private int statusMe = 0;
   private int singlePoseFrame = 0;
   private int currentAllActionIdx = 0;
   private int clazz = 0;
   private boolean isBigBodyManual = false;
   private boolean onionSkinning = false;
   private boolean showGrid = true;
   private boolean showAxes = true;
   private int indexEye = 0;
   private int dxEye = 0;
   private int dyEye = 0;
   public static final String[] STATUS_NAMES = new String[]{
      "0: Stand (Đứng nhấp nhô)",
      "1: Move / Run (Chạy 6 bước)",
      "2: Jump (Nhảy lên)",
      "3: Fall (Rơi xuống)",
      "4: Attack 1 - Melee (Đấm/Đá combo)",
      "5: Attack 2 - Slash (Chém liên hoàn)",
      "6: Attack 3 - Ranged/Cast (Bắn/Phép)",
      "7: Attack 4 - Heavy (Đại chiêu/Gồng)",
      "8: Hit (Thụ thương / Bị đánh)",
      "9: Die (Gục ngã / Bất tỉnh)",
      "10: Sit Boat (Ngồi thuyền)",
      "11: ALL (62 Poses Loop)",
      "12: Single Pose (Tĩnh 1 Frame)"
   };
   private static final double LOGIC_SCALE = 4.0;
   private static final BufferedImage MISSING = new BufferedImage(1, 1, 2);
   private final Map<Integer, BufferedImage> imageCache = new HashMap<>();
   private BufferedImage imgEye;

   public PartCanvas() {
      this.setBackground(new Color(20, 20, 25));
      this.setFocusable(true);
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            PartCanvas.this.requestFocusInWindow();
            PartCanvas.this.dragStart = e.getPoint();
            PartCanvas.this.lastMousePos = e.getPoint();
            PartCanvas.this.dragOriginPoint = e.getPoint();
            PartCanvas.this.dragBeyondThreshold = false;
            PartCanvas.this.draggingPart = null;
            PartCanvas.this.draggingSlot = -1;
            PartCanvas.this.draggingImageIdx = -1;
            if (PartCanvas.this.mWearing != null && PartCanvas.this.allParts != null) {
               int[] sort = PartCanvas.this.sortOrder();
               int ox = PartCanvas.this.charOriginX();
               int oy = PartCanvas.this.charOriginY();
               int fr = PartCanvas.this.safeFrame();

               for (int i = sort.length - 1; i >= 0; i--) {
                  int slot = sort[i];
                  mPart p = PartCanvas.this.getpaint(slot);
                  if (p != null) {
                     int ciSlot = slot;
                     if (slot == 6) {
                        ciSlot = 3;
                     } else if (slot == 7) {
                        ciSlot = 6;
                     }

                     if (ciSlot >= 0 && ciSlot < CharInfoData.CharInfo[fr].length) {
                        int ciIdx = CharInfoData.CharInfo[fr][ciSlot][0];
                        if (ciIdx >= 0 && ciIdx < p.pi.length) {
                           PartImage pi = p.pi[ciIdx];
                           if (pi != null && pi.id >= 0) {
                              BufferedImage img = PartCanvas.this.getImage(pi.id);
                              if (img != null && PartCanvas.this.isHit(ox, oy, fr, slot, pi, img, e.getX(), e.getY(), PartCanvas.this.zoom)) {
                                 PartCanvas.this.draggingPart = p;
                                 PartCanvas.this.draggingSlot = slot;
                                 PartCanvas.this.draggingImageIdx = ciIdx;
                                 PartCanvas.this.startDx = pi.dx;
                                 PartCanvas.this.startDy = pi.dy;
                                 if (PartCanvas.this.listener != null) {
                                    PartCanvas.this.listener.onPartSelected(slot, ciIdx);
                                 }

                                 if (SwingUtilities.isRightMouseButton(e)) {
                                    PartCanvas.this.showAutoAlignMenu(e.getComponent(), e.getX(), e.getY(), p, ciIdx, img);
                                 }
                                 break;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            PartCanvas.this.draggingPart = null;
            PartCanvas.this.draggingSlot = -1;
            PartCanvas.this.draggingImageIdx = -1;
            PartCanvas.this.dragOriginPoint = null;
         }
      });
      this.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (PartCanvas.this.dragOriginPoint != null) {
               int dxTotal = e.getX() - PartCanvas.this.dragOriginPoint.x;
               int dyTotal = e.getY() - PartCanvas.this.dragOriginPoint.y;
               if (!PartCanvas.this.dragBeyondThreshold) {
                  if (!(Math.hypot(dxTotal, dyTotal) > 5.0)) {
                     return;
                  }

                  PartCanvas.this.dragBeyondThreshold = true;
               }

               if (PartCanvas.this.draggingPart != null && PartCanvas.this.draggingImageIdx != -1) {
                  PartImage pi = PartCanvas.this.draggingPart.pi[PartCanvas.this.draggingImageIdx];
                  double totalScale = 4.0 * PartCanvas.this.zoom;
                  int scaledDx = (int)(dxTotal / totalScale);
                  int scaledDy = (int)(dyTotal / totalScale);
                  if (PartCanvas.this.direction == 2) {
                     pi.dx = (byte)(PartCanvas.this.startDx - scaledDx);
                  } else {
                     pi.dx = (byte)(PartCanvas.this.startDx + scaledDx);
                  }

                  pi.dy = (byte)(PartCanvas.this.startDy + scaledDy);
                  if (PartCanvas.this.listener != null) {
                     PartCanvas.this.listener.onPartDragged();
                  }

                  PartCanvas.this.repaint();
               } else if (PartCanvas.this.dragStart != null) {
                  PartCanvas.this.offsetX = PartCanvas.this.offsetX + (e.getX() - PartCanvas.this.dragStart.x);
                  PartCanvas.this.offsetY = PartCanvas.this.offsetY + (e.getY() - PartCanvas.this.dragStart.y);
                  PartCanvas.this.dragStart = e.getPoint();
                  PartCanvas.this.repaint();
               }
            }
         }
      });
      this.addMouseWheelListener(e -> {
         if (e.isControlDown()) {
            double oldZoom = this.zoom;
            if (e.getWheelRotation() < 0) {
               this.zoom *= 1.1;
            } else {
               this.zoom /= 1.1;
            }

            this.zoom = Math.max(0.1, Math.min(20.0, this.zoom));
            this.adjustScrollAfterZoom(oldZoom, this.zoom);
         } else if (e.isShiftDown()) {
            this.offsetX = this.offsetX - e.getWheelRotation() * 40;
         } else {
            this.offsetY = this.offsetY - e.getWheelRotation() * 40;
         }

         this.revalidate();
         this.repaint();
      });
      this.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (e.isControlDown()) {
               if (e.getKeyCode() == 38) {
                  double oldZoom = PartCanvas.this.zoom;
                  PartCanvas.this.zoom *= 1.12;
                  PartCanvas.this.zoom = Math.max(0.1, Math.min(20.0, PartCanvas.this.zoom));
                  PartCanvas.this.adjustScrollAfterZoom(oldZoom, PartCanvas.this.zoom);
               } else if (e.getKeyCode() == 40) {
                  double oldZoom = PartCanvas.this.zoom;
                  PartCanvas.this.zoom /= 1.12;
                  PartCanvas.this.zoom = Math.max(0.1, Math.min(20.0, PartCanvas.this.zoom));
                  PartCanvas.this.adjustScrollAfterZoom(oldZoom, PartCanvas.this.zoom);
               }

               PartCanvas.this.revalidate();
               PartCanvas.this.repaint();
            } else if (e.isShiftDown()) {
               if (e.getKeyCode() == 37) {
                  PartCanvas.this.offsetX += 40;
               } else if (e.getKeyCode() == 39) {
                  PartCanvas.this.offsetX -= 40;
               } else if (e.getKeyCode() == 38) {
                  PartCanvas.this.offsetY += 40;
               } else if (e.getKeyCode() == 40) {
                  PartCanvas.this.offsetY -= 40;
               }

               PartCanvas.this.revalidate();
               PartCanvas.this.repaint();
            } else if (e.getKeyCode() == 65) {
               if (PartCanvas.this.draggingPart != null && PartCanvas.this.draggingImageIdx != -1) {
                  PartImage pi = PartCanvas.this.draggingPart.pi[PartCanvas.this.draggingImageIdx];
                  BufferedImage img = PartCanvas.this.getImage(pi.id);
                  if (img != null) {
                     pi.dx = (byte)(-(img.getWidth() / 4.0) / 2.0);
                     pi.dy = (byte)(-(img.getHeight() / 4.0));
                     if (PartCanvas.this.listener != null) {
                        PartCanvas.this.listener.onPartDragged();
                     }

                     PartCanvas.this.repaint();
                  }
               }
            } else if (e.getKeyCode() == 67) {
               if (PartCanvas.this.draggingPart != null && PartCanvas.this.draggingImageIdx != -1) {
                  PartImage pi = PartCanvas.this.draggingPart.pi[PartCanvas.this.draggingImageIdx];
                  BufferedImage img = PartCanvas.this.getImage(pi.id);
                  if (img != null) {
                     pi.dx = (byte)(-(img.getWidth() / 4.0) / 2.0);
                     pi.dy = (byte)(-(img.getHeight() / 4.0) / 2.0);
                     if (PartCanvas.this.listener != null) {
                        PartCanvas.this.listener.onPartDragged();
                     }

                     PartCanvas.this.repaint();
                  }
               }
            } else if (e.getKeyCode() == 82 && PartCanvas.this.draggingPart != null && PartCanvas.this.draggingImageIdx != -1) {
               PartImage pi = PartCanvas.this.draggingPart.pi[PartCanvas.this.draggingImageIdx];
               pi.dx = 0;
               pi.dy = 0;
               if (PartCanvas.this.listener != null) {
                  PartCanvas.this.listener.onPartDragged();
               }

               PartCanvas.this.repaint();
            }
         }
      });
      this.animTimer = new Timer(150, e -> {
         if (this.isPlaying) {
            this.updateAnimation();
            this.repaint();
         }
      });
      this.animTimer.start();
   }

   private void adjustScrollAfterZoom(double oldZ, double newZ) {
      if (this.getParent() instanceof JViewport viewport) {
         Point p = viewport.getViewPosition();
         Dimension size = viewport.getExtentSize();
         double centerX = p.x + size.width / 2.0;
         double centerY = p.y + size.height / 2.0;
         double factor = newZ / oldZ;
         int newCenterX = (int)(centerX * factor);
         int newCenterY = (int)(centerY * factor);
         viewport.setViewPosition(new Point(newCenterX - size.width / 2, newCenterY - size.height / 2));
      }
   }

   private void updateAnimation() {
      int[] seq = this.getSeq(this.statusMe);
      this.animTick++;
      if (this.animTick >= seq.length) {
         this.animTick = 0;
      }

      this.frameIndex = seq[this.animTick];
      if (this.animListener != null) {
         this.animListener.onTick(this.animTick, seq.length, this.frameIndex);
      }
   }

   public int[] getSeq(int status) {
      switch (status) {
         case 0:
            return CharInfoData.FE_STAND;
         case 1:
            return CharInfoData.FE_RUN;
         case 2:
            return CharInfoData.FE_JUMP;
         case 3:
            return CharInfoData.FE_FALL;
         case 4:
            return CharInfoData.FE_ATTACK_MELEE;
         case 5:
            return CharInfoData.FE_ATTACK_SLASH;
         case 6:
            return CharInfoData.FE_ATTACK_RANGED;
         case 7:
            return CharInfoData.FE_ATTACK_HEAVY;
         case 8:
            return CharInfoData.FE_HIT;
         case 9:
            return CharInfoData.FE_DIE;
         case 10:
            return CharInfoData.FE_BOAT;
         case 11:
            int[] all = new int[CharInfoData.CharInfo.length];
            int i = 0;

            while (i < all.length) {
               all[i] = i++;
            }

            return all;
         case 12:
            return new int[]{this.singlePoseFrame % CharInfoData.CharInfo.length};
         default:
            return CharInfoData.FE_STAND;
      }
   }

   public String getStatusName() {
      return this.statusMe >= 0 && this.statusMe < STATUS_NAMES.length ? STATUS_NAMES[this.statusMe] : "Stand";
   }

   public void setSinglePoseFrame(int frame) {
      this.singlePoseFrame = Math.max(0, Math.min(CharInfoData.CharInfo.length - 1, frame));
      this.frameIndex = this.singlePoseFrame;
      if (this.animListener != null) {
         int[] seq = this.getSeq(this.statusMe);
         this.animListener.onTick(this.animTick, seq.length, this.frameIndex);
      }

      this.repaint();
   }

   public int getSinglePoseFrame() {
      return this.singlePoseFrame;
   }

   public static List<Integer> getPoseFramesUsingPartImage(int ciSlot, int imgIdx) {
      List<Integer> list = new ArrayList<>();

      for (int fr = 0; fr < CharInfoData.CharInfo.length; fr++) {
         if (ciSlot >= 0 && ciSlot < CharInfoData.CharInfo[fr].length && CharInfoData.CharInfo[fr][ciSlot][0] == imgIdx) {
            list.add(fr);
         }
      }

      return list;
   }

   private int charOriginX() {
      return this.getWidth() / 2 + this.offsetX;
   }

   private int charOriginY() {
      return this.getHeight() / 2 + (int)(50.0 * this.zoom) + this.offsetY;
   }

   private int safeFrame() {
      return this.frameIndex % CharInfoData.CharInfo.length;
   }

   public int getDirection() {
      return this.direction;
   }

   public double getZoom() {
      return this.zoom;
   }

   public int getFrameIndex() {
      return this.frameIndex;
   }

   public void setStatusMe(int status) {
      this.statusMe = status;
      this.animTick = 0;
      int[] seq = this.getSeq(this.statusMe);
      if (seq.length > 0) {
         this.frameIndex = seq[0];
         if (this.statusMe == 12) {
            this.frameIndex = this.singlePoseFrame;
         }

         if (this.animListener != null) {
            this.animListener.onTick(0, seq.length, this.frameIndex);
         }
      }

      this.repaint();
   }

   private int[] sortOrder() {
      return this.direction == 2 ? new int[]{7, 1, 2, 3, 6, 0, 5, 4} : new int[]{7, 1, 2, 0, 5, 4, 3, 6};
   }

   private int[] screenTopLeft(int ox, int oy, int frame, int slotIndex, PartImage pi, BufferedImage img) {
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
      if (this.isBigBodyManual && lechYHead == 0) {
         lechYHead = -6;
      }

      boolean isHeadRelated = slotIndex == 0 || slotIndex == 4 || slotIndex == 5;
      if (isHeadRelated && (slotIndex != 0 || !OffsetUtility.isKoLechHead(headId))) {
         ciY += lechYHead;
      }

      if (slotIndex == 3 && lechYHead != 0 && this.clazz != 0 && this.clazz != 1) {
         ciX += OffsetUtility.getLechWeaponX(this.clazz, frame);
         ciY += OffsetUtility.getLechWeaponY(this.clazz, frame);
      }

      if (ciSlot == 5 && (bodyId == 950 || bodyId == 963 || bodyId == 972)) {
         ciY -= 20;
         ciX -= 5;
      }

      int finalCiX = (int)((ciX + pi.dx) * 4.0 * this.zoom);
      int finalCiY = (int)((ciY + pi.dy) * 4.0 * this.zoom);
      int screenY = oy + finalCiY;
      int screenX;
      if (this.direction == 2) {
         screenX = ox - finalCiX;
      } else {
         screenX = ox + finalCiX;
      }

      return new int[]{screenX, screenY};
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      if (this.showGrid) {
         g2d.setColor(new Color(40, 40, 45));
         int gs = Math.max(1, (int)(20.0 * this.zoom));
         int gox = (this.offsetX % gs + gs) % gs;
         int goy = (this.offsetY % gs + gs) % gs;

         for (int x = gox; x < this.getWidth(); x += gs) {
            g2d.drawLine(x, 0, x, this.getHeight());
         }

         for (int y = goy; y < this.getHeight(); y += gs) {
            g2d.drawLine(0, y, this.getWidth(), y);
         }
      }

      int ox = this.charOriginX();
      int oy = this.charOriginY();
      if (this.showAxes) {
         g2d.setColor(new Color(180, 60, 60));
         g2d.drawLine(ox, 0, ox, this.getHeight());
         g2d.drawLine(0, oy, this.getWidth(), oy);
      }

      g2d.setColor(new Color(0, 0, 0, 70));
      int shadowW = (int)(88.0 * this.zoom);
      int shadowH = (int)(24.0 * this.zoom);
      g2d.fillOval(ox - shadowW / 2, oy - shadowH / 2 - (int)(4.0 * this.zoom), shadowW, shadowH);
      Point viewPos = new Point(0, 0);
      Container parent = this.getParent();
      if (parent instanceof JViewport) {
         viewPos = ((JViewport)parent).getViewPosition();
      }

      int hudX = viewPos.x + 10;
      int hudY = viewPos.y + 20;
      g2d.setColor(new Color(0, 255, 200));
      g2d.setFont(new Font("Segoe UI", 1, 12));
      String poseDesc = CharInfoData.getPoseName(this.frameIndex);
      g2d.drawString(String.format("Pose F%d: %s | Mode: %s", this.frameIndex, poseDesc, this.getStatusName()), hudX, hudY);
      hudY += 16;
      g2d.setFont(new Font("Segoe UI", 0, 11));
      g2d.setColor(Color.YELLOW);
      g2d.drawString(
         String.format(
            "Dir: %s | Zoom: %.1fx | Body ID: %s",
            this.direction == 2 ? "Right (Flip)" : "Left (Normal)",
            this.zoom,
            this.mWearing != null && this.mWearing.length > 3 ? this.mWearing[3] : "N/A"
         ),
         hudX,
         hudY
      );
      if (this.mWearing != null && this.allParts != null) {
         int fr = this.safeFrame();
         int[] sort = this.sortOrder();
         if (this.onionSkinning && this.mWearing != null && this.allParts != null) {
            int activeStatus = this.statusMe;
            if (this.statusMe == 7) {
               activeStatus = this.currentAllActionIdx;
            }

            int[] seq = this.getSeq(activeStatus);
            int prevFr;
            int nextFr;
            if (seq.length > 1) {
               int prevTick = (this.animTick - 1 + seq.length) % seq.length;
               int nextTick = (this.animTick + 1) % seq.length;
               prevFr = seq[prevTick] % CharInfoData.CharInfo.length;
               nextFr = seq[nextTick] % CharInfoData.CharInfo.length;
            } else {
               int maxF = CharInfoData.CharInfo.length;
               prevFr = (fr - 1 + maxF) % maxF;
               nextFr = (fr + 1) % maxF;
            }

            this.drawOnionSkinFrame(g2d, prevFr, ox, oy, sort, 0.25F);
            this.drawOnionSkinFrame(g2d, nextFr, ox, oy, sort, 0.25F);
         }

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
                     BufferedImage img = this.getImage(pi.id);
                     if (img != null) {
                        int[] sc = this.screenTopLeft(ox, oy, fr, slotIndex, pi, img);
                        AffineTransform at = new AffineTransform();
                        at.translate(sc[0], sc[1]);
                        if (this.direction == 2) {
                           at.scale(-this.zoom, this.zoom);
                        } else {
                           at.scale(this.zoom, this.zoom);
                        }

                        g2d.drawImage(img, at, null);
                        if (ciSlot == 0 && this.indexEye >= 0) {
                           short bodyId = this.mWearing != null && this.mWearing.length > 3 ? this.mWearing[3] : -1;
                           int lHead = OffsetUtility.getLechYHead(bodyId);
                           if (this.isBigBodyManual && lHead == 0) {
                              int var43 = -6;
                           }

                           int headCiX = CharInfoData.CharInfo[fr][0][1];
                           int var28 = CharInfoData.CharInfo[fr][0][2];
                        }

                        int sw = (int)(img.getWidth() * this.zoom);
                        int sh = (int)(img.getHeight() * this.zoom);
                        if (this.draggingPart == part && this.draggingSlot == slotIndex && this.draggingImageIdx == ciIdx) {
                           g2d.setColor(new Color(0, 200, 255, 150));
                           int rectX = sc[0];
                           if (this.direction == 2) {
                              rectX -= sw;
                           }

                           g2d.drawRect(rectX, sc[1], sw, sh);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void renderEye(Graphics2D g2d, int ox, int oy, int ciX, int ciY, int lechYHead, int piDy, double zoom) {
      if (this.imgEye == null) {
         try {
            File f = new File(AppConfig.getPath("Data/interface/eye.png"));
            if (!f.exists()) {
               return;
            }

            this.imgEye = ImageIO.read(f);
         } catch (Exception var17) {
            return;
         }
      }

      int num4 = this.direction == 0 ? 3 + this.dxEye : -8 - this.dxEye;
      if (this.isBigBodyManual && lechYHead == 0) {
         lechYHead = -6;
      }

      int totalY = ciY + piDy + 6 + this.dyEye + lechYHead;
      int ex = ox + (int)((ciX + num4) * 4.0 * zoom);
      int ey = oy + (int)(totalY * 4.0 * zoom);
      int srcY = (int)(this.indexEye * 12 * 4.0);
      int ew = (int)(48.0 * zoom);
      int eh = (int)(48.0 * zoom);
      if (this.direction == 2) {
         g2d.drawImage(this.imgEye, ex + ew, ey, ex, ey + eh, 0, srcY, 48, srcY + 48, null);
      } else {
         g2d.drawImage(this.imgEye, ex, ey, ex + ew, ey + eh, 0, srcY, 48, srcY + 48, null);
      }
   }

   private mPart findPart(short id) {
      if (this.allParts == null) {
         return null;
      } else {
         for (mPart p : this.allParts) {
            if (p.id == id) {
               return p;
            }
         }

         return null;
      }
   }

   private mPart getpaint(int index) {
      if (this.mWearing == null) {
         return null;
      } else {
         short partId = -1;
         switch (index) {
            case 0:
               if (this.mWearing.length > 6) {
                  partId = this.mWearing[6];
               }
               break;
            case 1:
               if (this.mWearing.length > 5) {
                  partId = this.mWearing[5];
               }
               break;
            case 2:
               if (this.mWearing.length > 3) {
                  partId = this.mWearing[3];
               }
               break;
            case 3:
               if (this.mWearing.length > 0) {
                  partId = this.mWearing[0];
               }
               break;
            case 4:
               if (this.mWearing.length > 1) {
                  partId = this.mWearing[1];
               }
               break;
            case 5:
               if (this.mWearing.length > 7) {
                  partId = this.mWearing[7];
               }
               break;
            case 6:
               if (this.mWearing.length > 2) {
                  partId = this.mWearing[2];
               }
               break;
            case 7:
               if (this.mWearing.length > 4) {
                  partId = this.mWearing[4];
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
      }
   }

   private boolean isHit(int ox, int oy, int frame, int slot, PartImage pi, BufferedImage img, int mx, int my, double zoom) {
      int[] sc = this.screenTopLeft(ox, oy, frame, slot, pi, img);
      int sw = (int)(img.getWidth() * zoom);
      int sh = (int)(img.getHeight() * zoom);
      int rectX = this.direction == 2 ? sc[0] - sw : sc[0];
      Rectangle rect = new Rectangle(rectX, sc[1], sw, sh);
      if (rect.contains(mx, my)) {
         int imgX = (int)((mx - rectX) / zoom);
         int imgY = (int)((my - sc[1]) / zoom);
         if (imgX >= 0 && imgX < img.getWidth() && imgY >= 0 && imgY < img.getHeight()) {
            if (this.direction == 2) {
               imgX = img.getWidth() - 1 - imgX;
            }

            int argb = img.getRGB(imgX, imgY);
            return (argb >> 24 & 0xFF) > 50;
         }
      }

      return false;
   }

   public BufferedImage getImage(short id) {
      int key = id & '\uffff';
      BufferedImage cached = this.imageCache.get(key);
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
                  this.imageCache.put(key, img);
                  return img;
               } catch (Exception var10) {
               }
            }
         }

         this.imageCache.put(key, MISSING);
         return null;
      }
   }

   public void setFrameIndex(int idx) {
      this.frameIndex = idx;
      this.repaint();
   }

   public void setDirection(int dir) {
      this.direction = dir;
      this.repaint();
   }

   public void setZoom(double z) {
      this.zoom = z;
      this.revalidate();
      this.repaint();
   }

   public void setClazz(int c) {
      this.clazz = c;
      this.repaint();
   }

   public void setBigBody(boolean b) {
      this.isBigBodyManual = b;
      this.repaint();
   }

   public void invalidateImageCache(int logicalId) {
      this.imageCache.remove(logicalId & 65535);
   }

   public void clearImageCache() {
      this.imageCache.clear();
   }

   public void setPlaying(boolean p) {
      this.isPlaying = p;
   }

   public boolean getPlaying() {
      return this.isPlaying;
   }

   public void setWearing(short[] wearing, List<mPart> allParts) {
      this.mWearing = wearing;
      this.allParts = allParts;
      this.repaint();
   }

   public short[] getWearing() {
      return this.mWearing;
   }

   public int getStatusMe() {
      return this.statusMe;
   }

   public void setAnimListener(PartCanvas.OnAnimTickListener l) {
      this.animListener = l;
   }

   public void setAnimTick(int tick) {
      int activeStatus = this.statusMe;
      if (this.statusMe == 7) {
         activeStatus = this.currentAllActionIdx;
      }

      int[] seq = this.getSeq(activeStatus);
      if (tick >= 0 && tick < seq.length) {
         this.animTick = tick;
         this.frameIndex = seq[tick];
         this.repaint();
      }
   }

   public int getCurrentSequenceLength() {
      int activeStatus = this.statusMe;
      if (this.statusMe == 7) {
         activeStatus = this.currentAllActionIdx;
      }

      return this.getSeq(activeStatus).length;
   }

   public int getCurrentCiIdx(int wearSlot) {
      int ciSlot;
      switch (wearSlot) {
         case 0:
            ciSlot = 3;
            break;
         case 1:
            ciSlot = 4;
            break;
         case 2:
            ciSlot = 3;
            break;
         case 3:
            ciSlot = 2;
            break;
         case 4:
            ciSlot = 6;
            break;
         case 5:
            ciSlot = 1;
            break;
         case 6:
            ciSlot = 0;
            break;
         case 7:
            ciSlot = 5;
            break;
         default:
            return -1;
      }

      int fr = this.safeFrame();
      return CharInfoData.CharInfo[fr][ciSlot][0];
   }

   public int getCurrentCiIdxByPaintSlot(int paintSlot) {
      int ciSlot = paintSlot;
      if (paintSlot == 6) {
         ciSlot = 3;
      } else if (paintSlot == 7) {
         ciSlot = 6;
      }

      int fr = this.safeFrame();
      return ciSlot >= 0 && ciSlot < CharInfoData.CharInfo[fr].length ? CharInfoData.CharInfo[fr][ciSlot][0] : 0;
   }

   public void setListener(PartCanvas.OnPartInteraction l) {
      this.listener = l;
   }

   private void drawOnionSkinFrame(Graphics2D g2d, int fr, int ox, int oy, int[] sort, float alpha) {
      Composite originalComposite = g2d.getComposite();
      g2d.setComposite(AlphaComposite.getInstance(3, alpha));

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
                  BufferedImage img = this.getImage(pi.id);
                  if (img != null) {
                     int[] sc = this.screenTopLeft(ox, oy, fr, slotIndex, pi, img);
                     AffineTransform at = new AffineTransform();
                     at.translate(sc[0], sc[1]);
                     if (this.direction == 2) {
                        at.scale(-this.zoom, this.zoom);
                     } else {
                        at.scale(this.zoom, this.zoom);
                     }

                     g2d.drawImage(img, at, null);
                  }
               }
            }
         }
      }

      g2d.setComposite(originalComposite);
   }

   public void setOnionSkinning(boolean b) {
      this.onionSkinning = b;
      this.repaint();
   }

   public boolean isOnionSkinning() {
      return this.onionSkinning;
   }

   public void setShowGrid(boolean b) {
      this.showGrid = b;
      this.repaint();
   }

   public boolean isShowGrid() {
      return this.showGrid;
   }

   public void setShowAxes(boolean b) {
      this.showAxes = b;
      this.repaint();
   }

   public boolean isShowAxes() {
      return this.showAxes;
   }

   private void showAutoAlignMenu(Component comp, int x, int y, mPart part, int imageIdx, BufferedImage img) {
      JPopupMenu menu = new JPopupMenu();
      JMenuItem itemBottom = new JMenuItem("Auto-Align (Center-Bottom) [A]");
      itemBottom.addActionListener(e -> {
         if (part != null && part.pi != null && imageIdx >= 0 && imageIdx < part.pi.length) {
            PartImage pi = part.pi[imageIdx];
            pi.dx = (byte)(-(img.getWidth() / 4.0) / 2.0);
            pi.dy = (byte)(-(img.getHeight() / 4.0));
            if (this.listener != null) {
               this.listener.onPartDragged();
            }

            this.repaint();
         }
      });
      menu.add(itemBottom);
      JMenuItem itemCenter = new JMenuItem("Auto-Align (Center-Center) [C]");
      itemCenter.addActionListener(e -> {
         if (part != null && part.pi != null && imageIdx >= 0 && imageIdx < part.pi.length) {
            PartImage pi = part.pi[imageIdx];
            pi.dx = (byte)(-(img.getWidth() / 4.0) / 2.0);
            pi.dy = (byte)(-(img.getHeight() / 4.0) / 2.0);
            if (this.listener != null) {
               this.listener.onPartDragged();
            }

            this.repaint();
         }
      });
      menu.add(itemCenter);
      JMenuItem itemReset = new JMenuItem("Reset Offset (0, 0) [R]");
      itemReset.addActionListener(e -> {
         if (part != null && part.pi != null && imageIdx >= 0 && imageIdx < part.pi.length) {
            PartImage pi = part.pi[imageIdx];
            pi.dx = 0;
            pi.dy = 0;
            if (this.listener != null) {
               this.listener.onPartDragged();
            }

            this.repaint();
         }
      });
      menu.add(itemReset);
      menu.show(comp, x, y);
   }

   public BufferedImage exportNpcSprite() {
      int w = 300;
      int h = 300;
      double oldZoom = this.zoom;
      int oldDir = this.direction;
      int oldFrame = this.frameIndex;
      this.zoom = 1.0;
      this.direction = 0;
      int ox = w / 2;
      int oy = h - 60;
      this.frameIndex = 0;
      BufferedImage frame0 = new BufferedImage(w, h, 2);
      Graphics2D g0 = frame0.createGraphics();
      this.drawPartsOnly(g0, ox, oy);
      g0.dispose();
      this.frameIndex = 1;
      BufferedImage frame1 = new BufferedImage(w, h, 2);
      Graphics2D g1 = frame1.createGraphics();
      this.drawPartsOnly(g1, ox, oy);
      g1.dispose();
      this.zoom = oldZoom;
      this.direction = oldDir;
      this.frameIndex = oldFrame;
      int[] box0 = this.getBoundingBox(frame0);
      int[] box1 = this.getBoundingBox(frame1);
      if (box0 == null && box1 == null) {
         return new BufferedImage(1, 2, 2);
      } else {
         int minDx = Integer.MAX_VALUE;
         int maxDx = Integer.MIN_VALUE;
         int minDy = Integer.MAX_VALUE;
         int maxDy = Integer.MIN_VALUE;
         if (box0 != null) {
            minDx = Math.min(minDx, box0[0] - ox);
            maxDx = Math.max(maxDx, box0[2] - ox);
            minDy = Math.min(minDy, box0[1] - oy);
            maxDy = Math.max(maxDy, box0[3] - oy);
         }

         if (box1 != null) {
            minDx = Math.min(minDx, box1[0] - ox);
            maxDx = Math.max(maxDx, box1[2] - ox);
            minDy = Math.min(minDy, box1[1] - oy);
            maxDy = Math.max(maxDy, box1[3] - oy);
         }

         minDx -= 2;
         maxDx += 2;
         minDy -= 2;
         maxDy += 2;
         minDx = Math.max(minDx, -ox);
         maxDx = Math.min(maxDx, w - ox - 1);
         minDy = Math.max(minDy, -oy);
         maxDy = Math.min(maxDy, h - oy - 1);
         int cropW = maxDx - minDx + 1;
         int cropH = maxDy - minDy + 1;
         int subX = ox + minDx;
         int subY = oy + minDy;
         BufferedImage cropped0 = frame0.getSubimage(subX, subY, cropW, cropH);
         BufferedImage cropped1 = frame1.getSubimage(subX, subY, cropW, cropH);
         BufferedImage combined = new BufferedImage(cropW, cropH * 2, 2);
         Graphics2D g2d = combined.createGraphics();
         g2d.drawImage(cropped0, 0, 0, null);
         g2d.drawImage(cropped1, 0, cropH, null);
         g2d.dispose();
         return combined;
      }
   }

   private int[] getBoundingBox(BufferedImage img) {
      int minX = img.getWidth();
      int minY = img.getHeight();
      int maxX = -1;
      int maxY = -1;

      for (int y = 0; y < img.getHeight(); y++) {
         for (int x = 0; x < img.getWidth(); x++) {
            int alpha = img.getRGB(x, y) >> 24 & 0xFF;
            if (alpha > 0) {
               if (x < minX) {
                  minX = x;
               }

               if (x > maxX) {
                  maxX = x;
               }

               if (y < minY) {
                  minY = y;
               }

               if (y > maxY) {
                  maxY = y;
               }
            }
         }
      }

      return maxX == -1 ? null : new int[]{minX, minY, maxX, maxY};
   }

   private void drawPartsOnly(Graphics2D g2d, int ox, int oy) {
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      if (this.mWearing != null && this.allParts != null) {
         int fr = this.safeFrame();
         int[] sort = this.sortOrder();

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
                     BufferedImage img = this.getImage(pi.id);
                     if (img != null) {
                        int[] sc = this.screenTopLeft(ox, oy, fr, slotIndex, pi, img);
                        AffineTransform at = new AffineTransform();
                        at.translate(sc[0], sc[1]);
                        if (this.direction == 2) {
                           at.scale(-this.zoom, this.zoom);
                        } else {
                           at.scale(this.zoom, this.zoom);
                        }

                        g2d.drawImage(img, at, null);
                     }
                  }
               }
            }
         }
      }
   }

   private BufferedImage cropTransparent(BufferedImage source) {
      int minX = source.getWidth();
      int minY = source.getHeight();
      int maxX = 0;
      int maxY = 0;
      boolean hasPixels = false;

      for (int y = 0; y < source.getHeight(); y++) {
         for (int x = 0; x < source.getWidth(); x++) {
            int alpha = source.getRGB(x, y) >> 24 & 0xFF;
            if (alpha > 0) {
               hasPixels = true;
               if (x < minX) {
                  minX = x;
               }

               if (x > maxX) {
                  maxX = x;
               }

               if (y < minY) {
                  minY = y;
               }

               if (y > maxY) {
                  maxY = y;
               }
            }
         }
      }

      if (!hasPixels) {
         return source;
      } else {
         minX = Math.max(0, minX - 2);
         minY = Math.max(0, minY - 2);
         maxX = Math.min(source.getWidth() - 1, maxX + 2);
         maxY = Math.min(source.getHeight() - 1, maxY + 2);
         int width = maxX - minX + 1;
         int height = maxY - minY + 1;
         return source.getSubimage(minX, minY, width, height);
      }
   }

   public interface OnAnimTickListener {
      void onTick(int var1, int var2, int var3);
   }

   public interface OnPartInteraction {
      void onPartSelected(int var1, int var2);

      void onPartDragged();
   }
}
