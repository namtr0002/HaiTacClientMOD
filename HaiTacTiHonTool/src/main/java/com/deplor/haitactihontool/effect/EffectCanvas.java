package com.deplor.haitactihontool.effect;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class EffectCanvas extends JPanel {
   public static final double LOGIC_SCALE = 4.0;
   private static final Color BG = new Color(18, 18, 22);
   private static final Color GRID = new Color(32, 32, 40);
   private static final Color AXIS_X = new Color(70, 200, 70, 80);
   private static final Color AXIS_Y = new Color(200, 70, 70, 80);
   private static final Color ANCHOR_COL = new Color(0, 180, 255);
   private static final Color CHAR_FILL = new Color(60, 100, 160, 55);
   private static final Color CHAR_LINE = new Color(80, 140, 220, 120);
   private static final Color PART_SEL = new Color(0, 220, 255, 230);
   private static final Color PART_HL = new Color(255, 200, 0, 100);
   private static final Color HUD_COLOR = new Color(200, 200, 220);
   private static final int CHAR_W = 48;
   private static final int CHAR_H = 216;
   private EffectModel model;
   private int seqPos = 0;
   private int directFrameIdx = -1;
   private double zoom = 1.0;
   private int offsetX = 0;
   private int offsetY = 0;
   private boolean showChar = true;
   private boolean showGrid = true;
   private boolean showAxis = true;
   private boolean showPartBounds = false;
   private boolean useLayers = true;
   private boolean editMode = false;
   private int selectedPartIdx = -1;
   private boolean initialized = false;
   private int typeMove = 0;
   private int hOne = 48;
   private Point dragStart;
   private int dragStartDx;
   private int dragStartDy;
   private boolean isDraggingPart = false;
   private boolean isDraggingRotation = false;
   private double dragStartMouseAngle = 0.0;
   private int dragStartPartRotate = 0;
   public static final int HANDLE_NONE = -1;
   public static final int HANDLE_TOP_ROT = 100;
   public static final int HANDLE_CORNER_TL = 0;
   public static final int HANDLE_CORNER_TR = 1;
   public static final int HANDLE_CORNER_BR = 2;
   public static final int HANDLE_CORNER_BL = 3;
   private final List<BufferedImage> thumbCache = new ArrayList<>();
   private EffectCanvas.OnPartClickListener clickListener;
   private EffectCanvas.OnPartMovedListener moveListener;
   private final EffectCharRenderer charRenderer = new EffectCharRenderer();

   public EffectCanvas() {
      this.setBackground(BG);
      this.setPreferredSize(new Dimension(2000, 2000));
      this.setFocusable(true);
      this.setupMouse();
      this.setupKeyboard();
   }

   private void setupKeyboard() {
      InputMap im = this.getInputMap(2);
      ActionMap am = this.getActionMap();
      im.put(KeyStroke.getKeyStroke(38, 128), "zoomIn");
      am.put("zoomIn", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EffectCanvas.this.zoom *= 1.12;
            EffectCanvas.this.zoom = Math.max(0.05, Math.min(20.0, EffectCanvas.this.zoom));
            EffectCanvas.this.repaint();
         }
      });
      im.put(KeyStroke.getKeyStroke(40, 128), "zoomOut");
      am.put("zoomOut", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EffectCanvas.this.zoom /= 1.12;
            EffectCanvas.this.zoom = Math.max(0.05, Math.min(20.0, EffectCanvas.this.zoom));
            EffectCanvas.this.repaint();
         }
      });
      AbstractAction panLeft = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EffectCanvas.this.offsetX += 40;
            EffectCanvas.this.repaint();
         }
      };
      AbstractAction panRight = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EffectCanvas.this.offsetX -= 40;
            EffectCanvas.this.repaint();
         }
      };
      im.put(KeyStroke.getKeyStroke(37, 64), "panLeftL");
      im.put(KeyStroke.getKeyStroke(38, 64), "panLeftU");
      im.put(KeyStroke.getKeyStroke(39, 64), "panRightR");
      im.put(KeyStroke.getKeyStroke(40, 64), "panRightD");
      am.put("panLeftL", panLeft);
      am.put("panLeftU", panLeft);
      am.put("panRightR", panRight);
      am.put("panRightD", panRight);
   }

   private void setupMouse() {
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            EffectCanvas.this.dragStart = e.getPoint();
            EffectCanvas.this.isDraggingPart = false;
            EffectCanvas.this.isDraggingRotation = false;
            EffectCanvas.this.requestFocusInWindow();
            if (EffectCanvas.this.selectedPartIdx >= 0 && EffectCanvas.this.model != null) {
               int handle = EffectCanvas.this.hitTestRotationHandle(e.getX(), e.getY());
               if (handle != -1) {
                  EffectCanvas.PartGeom g = EffectCanvas.this.getSelectedPartGeom();
                  if (g != null) {
                     EffFrame frame = EffectCanvas.this.getCurrentFrame();
                     if (frame != null && EffectCanvas.this.selectedPartIdx < frame.allParts.size()) {
                        EffPartFrame p = frame.allParts.get(EffectCanvas.this.selectedPartIdx);
                        EffectCanvas.this.isDraggingRotation = true;
                        EffectCanvas.this.dragStartMouseAngle = Math.toDegrees(Math.atan2(e.getY() - g.cy, e.getX() - g.cx));
                        EffectCanvas.this.dragStartPartRotate = p.rotate;
                        EffectCanvas.this.repaint();
                        return;
                     }
                  }
               }
            }

            if (EffectCanvas.this.model != null) {
               EffectCanvas.this.trySelectPart(e.getX(), e.getY());
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            EffectCanvas.this.isDraggingPart = false;
            EffectCanvas.this.isDraggingRotation = false;
            EffectCanvas.this.repaint();
         }
      });
      this.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (EffectCanvas.this.dragStart != null) {
               int dx = e.getX() - EffectCanvas.this.dragStart.x;
               int dy = e.getY() - EffectCanvas.this.dragStart.y;
               if (EffectCanvas.this.isDraggingRotation && EffectCanvas.this.selectedPartIdx >= 0 && EffectCanvas.this.model != null) {
                  EffectCanvas.PartGeom g = EffectCanvas.this.getSelectedPartGeom();
                  if (g != null) {
                     EffFrame frame = EffectCanvas.this.getCurrentFrame();
                     if (frame != null && EffectCanvas.this.selectedPartIdx < frame.allParts.size()) {
                        EffPartFrame p = frame.allParts.get(EffectCanvas.this.selectedPartIdx);
                        double currentMouseAngle = Math.toDegrees(Math.atan2(e.getY() - g.cy, e.getX() - g.cx));
                        double delta = currentMouseAngle - EffectCanvas.this.dragStartMouseAngle;
                        double newRot = (EffectCanvas.this.dragStartPartRotate + delta) % 360.0;
                        if (newRot < 0.0) {
                           newRot += 360.0;
                        }

                        if (e.isShiftDown()) {
                           newRot = Math.round(newRot / 15.0) * 15.0;
                           if (newRot >= 360.0) {
                              newRot = 0.0;
                           }
                        }

                        p.rotate = (int)Math.round(newRot);
                        if (EffectCanvas.this.moveListener != null) {
                           EffectCanvas.this.moveListener.onPartMoved(EffectCanvas.this.selectedPartIdx, p);
                        }

                        EffectCanvas.this.repaint();
                        return;
                     }
                  }
               }

               if (EffectCanvas.this.selectedPartIdx >= 0 && EffectCanvas.this.model != null && EffectCanvas.this.isDraggingPart) {
                  EffFrame frame = EffectCanvas.this.getCurrentFrame();
                  if (frame != null && EffectCanvas.this.selectedPartIdx < frame.allParts.size()) {
                     EffPartFrame px = frame.allParts.get(EffectCanvas.this.selectedPartIdx);
                     double scale = 4.0 * EffectCanvas.this.zoom;
                     px.dx = EffectCanvas.this.dragStartDx + (int)(dx / scale);
                     px.dy = EffectCanvas.this.dragStartDy + (int)(dy / scale);
                     if (EffectCanvas.this.moveListener != null) {
                        EffectCanvas.this.moveListener.onPartMoved(EffectCanvas.this.selectedPartIdx, px);
                     }

                     EffectCanvas.this.repaint();
                     return;
                  }
               }

               EffectCanvas.this.offsetX += dx;
               EffectCanvas.this.offsetY += dy;
               EffectCanvas.this.dragStart = e.getPoint();
               EffectCanvas.this.repaint();
            }
         }

         @Override
         public void mouseMoved(MouseEvent e) {
            if (EffectCanvas.this.selectedPartIdx >= 0 && EffectCanvas.this.model != null) {
               int handle = EffectCanvas.this.hitTestRotationHandle(e.getX(), e.getY());
               if (handle != -1) {
                  EffectCanvas.this.setCursor(Cursor.getPredefinedCursor(1));
                  EffectCanvas.this.setToolTipText("Kéo chuột để xoay góc (Giữ Shift để snap góc 15°)");
                  return;
               }
            }

            EffectCanvas.this.setCursor(Cursor.getDefaultCursor());
            EffectCanvas.this.setToolTipText(null);
         }
      });
      this.addMouseWheelListener(e -> {
         if (e.isAltDown() && this.selectedPartIdx >= 0 && this.model != null) {
            EffFrame frame = this.getCurrentFrame();
            if (frame != null && this.selectedPartIdx < frame.allParts.size()) {
               EffPartFrame p = frame.allParts.get(this.selectedPartIdx);
               int delta = (e.getWheelRotation() < 0 ? 5 : -5) * (e.isShiftDown() ? 3 : 1);
               int newRot = (p.rotate + delta) % 360;
               if (newRot < 0) {
                  newRot += 360;
               }

               p.rotate = newRot;
               if (this.moveListener != null) {
                  this.moveListener.onPartMoved(this.selectedPartIdx, p);
               }

               this.repaint();
               return;
            }
         }

         if (e.isControlDown()) {
            double oldZ = this.zoom;
            if (e.getWheelRotation() < 0) {
               this.zoom *= 1.12;
            } else {
               this.zoom /= 1.12;
            }

            this.zoom = Math.max(0.05, Math.min(20.0, this.zoom));
            int mx = e.getX();
            int my = e.getY();
            int ax = this.anchorScreenX();
            int ay = this.anchorScreenY();
            double f = this.zoom / oldZ;
            this.offsetX = (int)(mx - f * (mx - ax - this.offsetX) - ax);
            this.offsetY = (int)(my - f * (my - ay - this.offsetY) - ay);
            this.repaint();
         } else if (e.isShiftDown()) {
            this.offsetX = this.offsetX - e.getWheelRotation() * 35;
            this.repaint();
         } else {
            this.offsetY = this.offsetY - e.getWheelRotation() * 35;
            this.repaint();
         }
      });
   }

   public void setModel(EffectModel m) {
      this.model = m;
      this.seqPos = 0;
      this.directFrameIdx = -1;
      this.selectedPartIdx = -1;
      this.thumbCache.clear();
      if (m != null) {
         this.buildThumbs();
      }

      this.repaint();
   }

   public void setSeqPos(int pos) {
      if (this.model != null) {
         int len = this.model.getSeqLen();
         this.directFrameIdx = -1;
         this.seqPos = len > 0 ? (pos % len + len) % len : 0;
         this.repaint();
      }
   }

   public int getSeqPos() {
      return this.seqPos;
   }

   public void setDirectFrameIdx(int frameIdx) {
      this.directFrameIdx = frameIdx;
      this.repaint();
   }

   public int getDirectFrameIdx() {
      return this.directFrameIdx;
   }

   public EffFrame getCurrentFrame() {
      if (this.model == null) {
         return null;
      } else {
         return this.directFrameIdx >= 0 && this.model.frames != null && this.directFrameIdx < this.model.frames.length
            ? this.model.frames[this.directFrameIdx]
            : this.model.getSeqFrame(this.seqPos);
      }
   }

   public int getCurrentFrameIndex() {
      if (this.model == null) {
         return 0;
      } else {
         return this.directFrameIdx >= 0 && this.model.frames != null && this.directFrameIdx < this.model.frames.length
            ? this.directFrameIdx
            : this.model.resolveSeqFrame(this.seqPos);
      }
   }

   public void resetView() {
      this.zoom = 1.1;
      this.offsetX = 0;
      int cy = this.getHeight() / 2;
      int ay = this.anchorScreenY();
      this.offsetY = cy - ay + 55;
      this.repaint();
      SwingUtilities.invokeLater(() -> {
         if (this.getParent() instanceof JViewport vp) {
            if (vp.getWidth() <= 0 || vp.getHeight() <= 0) {
               Timer autoCenterTimer = new Timer(150, ev -> this.resetView());
               autoCenterTimer.setRepeats(false);
               autoCenterTimer.start();
               return;
            }

            int vx = (this.getWidth() - vp.getWidth()) / 2;
            int vy = (this.getHeight() - vp.getHeight()) / 2;
            vp.setViewPosition(new Point(Math.max(0, vx), Math.max(0, vy)));
         }
      });
   }

   public void setShowChar(boolean v) {
      this.showChar = v;
      this.repaint();
   }

   public void setShowGrid(boolean v) {
      this.showGrid = v;
      this.repaint();
   }

   public void setShowAxis(boolean v) {
      this.showAxis = v;
      this.repaint();
   }

   public void setShowPartBounds(boolean v) {
      this.showPartBounds = v;
      this.repaint();
   }

   public void setUseLayers(boolean v) {
      this.useLayers = v;
      this.repaint();
   }

   public void setEditMode(boolean v) {
      this.editMode = v;
      this.repaint();
   }

   public void setSelectedPart(int idx) {
      this.selectedPartIdx = idx;
      this.repaint();
   }

   public int getSelectedPart() {
      return this.selectedPartIdx;
   }

   public void setClickListener(EffectCanvas.OnPartClickListener l) {
      this.clickListener = l;
   }

   public void setMoveListener(EffectCanvas.OnPartMovedListener l) {
      this.moveListener = l;
   }

   public List<BufferedImage> getThumbs() {
      return this.thumbCache;
   }

   public void forceThumbRebuild() {
      this.buildThumbs();
   }

   public boolean isEditMode() {
      return this.editMode;
   }

   public void setTypeMove(int val) {
      this.typeMove = val;
      this.repaint();
   }

   public void setHOne(int val) {
      this.hOne = val;
      this.repaint();
   }

   private int anchorScreenX() {
      return this.getWidth() / 2;
   }

   private int anchorScreenY() {
      return (int)(this.getHeight() * 0.7);
   }

   private int drawAX() {
      return this.anchorScreenX() + this.offsetX;
   }

   private int drawAY() {
      return this.anchorScreenY() + this.offsetY;
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      if (!this.initialized && this.getWidth() > 0 && this.getHeight() > 0) {
         this.resetView();
         this.initialized = true;
      }

      g2.setColor(BG);
      g2.fillRect(0, 0, this.getWidth(), this.getHeight());
      if (this.showGrid) {
         this.drawGrid(g2);
      }

      if (this.showAxis) {
         this.drawAxis(g2);
      }

      int ax = this.drawAX();
      int ay = this.drawAY();
      if (this.model == null) {
         if (this.showChar) {
            this.drawCharSilhouette(g2, ax, ay);
         }

         this.drawAnchorMark(g2, ax, ay);
         this.drawPlaceholder(g2);
      } else {
         EffFrame frame = this.getCurrentFrame();
         if (frame == null) {
            this.drawAnchorMark(g2, ax, ay);
         } else {
            if (this.useLayers) {
               for (int i = 0; i < frame.partsBottom.size(); i++) {
                  int gi = frame.allParts.indexOf(frame.partsBottom.get(i));
                  this.drawPart(g2, frame.partsBottom.get(i), ax, ay, gi);
               }

               if (this.showChar) {
                  this.drawCharSilhouette(g2, ax, ay);
               }

               for (int i = 0; i < frame.partsTop.size(); i++) {
                  int gi = frame.allParts.indexOf(frame.partsTop.get(i));
                  this.drawPart(g2, frame.partsTop.get(i), ax, ay, gi);
               }
            } else {
               if (this.showChar) {
                  this.drawCharSilhouette(g2, ax, ay);
               }

               for (int i = 0; i < frame.allParts.size(); i++) {
                  this.drawPart(g2, frame.allParts.get(i), ax, ay, i);
               }
            }

            this.drawAnchorMark(g2, ax, ay);
            this.drawHUD(g2);
            if (this.editMode) {
               this.drawEditHint(g2);
            }
         }
      }
   }

   private void drawPart(Graphics2D g2, EffPartFrame p, int ax, int ay, int gi) {
      if (this.model.atlasImage != null && this.model.smallImages != null && p.idSmallImg >= 0 && p.idSmallImg < this.model.smallImages.length) {
         SmallImageDef si = this.model.smallImages[p.idSmallImg];
         int shiftY = 0;
         if (this.hOne == 62 && this.model != null && this.model.minAbsDy >= 50) {
            shiftY = -32;
         }

         int destX = ax + (int)(p.dx * 4.0 * this.zoom);
         int destY = ay + (int)((p.dy + shiftY) * 4.0 * this.zoom);
         int dw = (int)(si.w * 4.0 * this.zoom);
         int dh = (int)(si.h * 4.0 * this.zoom);
         BufferedImage atlas = this.model.atlasImage;
         int iw = atlas.getWidth();
         int ih = atlas.getHeight();
         int cx = (int)(si.x * 4.0);
         int cy = (int)(si.y * 4.0);
         int cw = (int)(si.w * 4.0);
         int ch = (int)(si.h * 4.0);
         if (cx >= iw) {
            cx = 0;
         }

         if (cy >= ih) {
            cy = 0;
         }

         if (cx + cw > iw) {
            cw = iw - cx;
         }

         if (cy + ch > ih) {
            ch = ih - cy;
         }

         if (cw > 0 && ch > 0) {
            if (p.rotate != 0) {
               AffineTransform oldTx = g2.getTransform();
               g2.rotate(Math.toRadians(p.rotate), destX + dw / 2.0, destY + dh / 2.0);
               if (p.flip == 1) {
                  g2.drawImage(atlas, destX + dw, destY, destX, destY + dh, cx, cy, cx + cw, cy + ch, null);
               } else {
                  g2.drawImage(atlas, destX, destY, destX + dw, destY + dh, cx, cy, cx + cw, cy + ch, null);
               }

               g2.setTransform(oldTx);
            } else if (p.flip == 1) {
               g2.drawImage(atlas, destX + dw, destY, destX, destY + dh, cx, cy, cx + cw, cy + ch, null);
            } else {
               g2.drawImage(atlas, destX, destY, destX + dw, destY + dh, cx, cy, cx + cw, cy + ch, null);
            }
         }

         boolean sel = gi == this.selectedPartIdx;
         if (this.showPartBounds || sel) {
            g2.setColor(sel ? PART_SEL : PART_HL);
            g2.setStroke(new BasicStroke(sel ? 2.0F : 0.8F));
            if (p.rotate != 0) {
               AffineTransform oldTx = g2.getTransform();
               g2.rotate(Math.toRadians(p.rotate), destX + dw / 2.0, destY + dh / 2.0);
               g2.drawRect(destX, destY, dw, dh);
               if (sel) {
                  this.drawRotationVisuals(g2, destX, destY, dw, dh, p);
               }

               g2.setTransform(oldTx);
            } else {
               g2.drawRect(destX, destY, dw, dh);
               if (sel) {
                  this.drawRotationVisuals(g2, destX, destY, dw, dh, p);
               }
            }

            g2.setStroke(new BasicStroke(1.0F));
         }

         if (this.editMode && dw > 12 && dh > 12) {
            g2.setFont(new Font("Segoe UI", 1, 10));
            g2.setColor(sel ? new Color(0, 220, 255) : new Color(255, 200, 0, 200));
            String lbl = gi + (p.rotate != 0 ? " " + p.rotate + "°" : "");
            g2.drawString(lbl, destX + 2, destY + 11);
         }
      }
   }

   private void drawRotationVisuals(Graphics2D g2, int destX, int destY, int dw, int dh, EffPartFrame p) {
      int cx = destX + dw / 2;
      int stalkLen = Math.max(18, (int)(22.0 * Math.min(this.zoom, 1.5)));
      int knobY = destY - stalkLen;
      g2.setColor(new Color(0, 220, 255, 220));
      g2.setStroke(new BasicStroke(1.5F));
      g2.drawLine(cx, destY, cx, knobY);
      int kr = 6;
      g2.setColor(new Color(255, 150, 0, 240));
      g2.fillOval(cx - kr, knobY - kr, kr * 2, kr * 2);
      g2.setColor(Color.WHITE);
      g2.setStroke(new BasicStroke(1.5F));
      g2.drawOval(cx - kr, knobY - kr, kr * 2, kr * 2);
      g2.setColor(new Color(0, 40, 80));
      g2.fillOval(cx - 2, knobY - 2, 4, 4);
      int cr = 3;
      int[][] corners = new int[][]{{destX, destY}, {destX + dw, destY}, {destX + dw, destY + dh}, {destX, destY + dh}};

      for (int[] c : corners) {
         g2.setColor(new Color(0, 220, 255));
         g2.fillRect(c[0] - cr, c[1] - cr, cr * 2, cr * 2);
         g2.setColor(Color.WHITE);
         g2.drawRect(c[0] - cr, c[1] - cr, cr * 2, cr * 2);
      }

      if (this.isDraggingRotation) {
         String txt = "Góc: " + p.rotate + "°";
         g2.setFont(new Font("Segoe UI", 1, 11));
         FontMetrics fm = g2.getFontMetrics();
         int tw = fm.stringWidth(txt);
         int th = fm.getHeight();
         int bx = cx - tw / 2 - 6;
         int by = knobY - th - 8;
         g2.setColor(new Color(15, 15, 22, 220));
         g2.fillRoundRect(bx, by, tw + 12, th + 4, 6, 6);
         g2.setColor(new Color(0, 220, 255));
         g2.drawRoundRect(bx, by, tw + 12, th + 4, 6, 6);
         g2.setColor(Color.WHITE);
         g2.drawString(txt, bx + 6, by + fm.getAscent() + 2);
      }
   }

   private void drawCharSilhouette(Graphics2D g2, int ax, int ay) {
      if (this.typeMove != 2) {
         this.charRenderer.render(g2, ax, ay, this.zoom, this.hOne == 62);
      }
   }

   private void drawGrid(Graphics2D g2) {
      g2.setColor(GRID);
      g2.setStroke(new BasicStroke(0.5F));
      int step = Math.max(4, (int)(32.0 * this.zoom));
      int gox = (this.drawAX() % step + step) % step;
      int goy = (this.drawAY() % step + step) % step;

      for (int x = gox; x < this.getWidth(); x += step) {
         g2.drawLine(x, 0, x, this.getHeight());
      }

      for (int y = goy; y < this.getHeight(); y += step) {
         g2.drawLine(0, y, this.getWidth(), y);
      }

      g2.setStroke(new BasicStroke(1.0F));
   }

   private void drawAxis(Graphics2D g2) {
      int ax = this.drawAX();
      int ay = this.drawAY();
      g2.setColor(AXIS_X);
      g2.drawLine(0, ay, this.getWidth(), ay);
      g2.setColor(AXIS_Y);
      g2.drawLine(ax, 0, ax, this.getHeight());
   }

   private void drawAnchorMark(Graphics2D g2, int ax, int ay) {
      g2.setColor(ANCHOR_COL);
      g2.setStroke(new BasicStroke(1.5F));
      g2.drawLine(ax - 8, ay, ax + 8, ay);
      g2.drawLine(ax, ay - 8, ax, ay + 8);
      g2.fillOval(ax - 3, ay - 3, 6, 6);
      g2.setStroke(new BasicStroke(1.0F));
   }

   private void drawPlaceholder(Graphics2D g2) {
      g2.setFont(new Font("Segoe UI", 0, 14));
      g2.setColor(new Color(80, 80, 100));
      String msg = "Chưa load effect — Nhập ID và bấm Load, hoặc Import Texture để tạo mới";
      FontMetrics fm = g2.getFontMetrics();
      g2.drawString(msg, (this.getWidth() - fm.stringWidth(msg)) / 2, this.getHeight() / 2);
   }

   private void drawHUD(Graphics2D g2) {
      g2.setFont(new Font("Segoe UI", 1, 11));
      g2.setColor(HUD_COLOR);
      EffFrame fr = this.getCurrentFrame();
      int parts = fr != null ? fr.getTotalParts() : 0;
      int curFrameIdx = this.getCurrentFrameIndex();
      String info;
      if (this.directFrameIdx >= 0) {
         info = String.format(
            "ID:%d | Frame:%d/%d (Trực tiếp) | Parts:%d | Zoom:%.1fx", this.model.id, this.directFrameIdx, this.model.getFrameCount(), parts, this.zoom
         );
      } else {
         info = String.format(
            "ID:%d | Seq:%d/%d → Frame:%d/%d | Parts:%d | Zoom:%.1fx",
            this.model.id,
            this.seqPos + 1,
            this.model.getSeqLen(),
            curFrameIdx,
            this.model.getFrameCount(),
            parts,
            this.zoom
         );
      }

      g2.drawString(info, 8, 18);
   }

   private void drawEditHint(Graphics2D g2) {
      g2.setFont(new Font("Segoe UI", 1, 10));
      g2.setColor(new Color(255, 160, 60));
      g2.drawString("✎ EDIT MODE — Click part để chọn & kéo thả | Alt+Cuộn chuột để xoay | Shift/Ctrl để phóng đại", 8, this.getHeight() - 8);
   }

   private void buildThumbs() {
      this.thumbCache.clear();
      if (this.model != null && this.model.frames != null) {
         for (EffFrame f : this.model.frames) {
            this.thumbCache.add(this.renderThumb(f, 72, 72));
         }
      }
   }

   public BufferedImage renderThumb(EffFrame frame, int tw, int th) {
      BufferedImage img = new BufferedImage(tw, th, 2);
      Graphics2D g2 = img.createGraphics();
      g2.setColor(new Color(22, 22, 28));
      g2.fillRect(0, 0, tw, th);
      if (frame != null && this.model != null && this.model.atlasImage != null && !frame.allParts.isEmpty()) {
         int minX = Integer.MAX_VALUE;
         int minY = Integer.MAX_VALUE;
         int maxX = Integer.MIN_VALUE;
         int maxY = Integer.MIN_VALUE;

         for (EffPartFrame p : frame.allParts) {
            if (this.model.smallImages != null && p.idSmallImg >= 0 && p.idSmallImg < this.model.smallImages.length) {
               SmallImageDef si = this.model.smallImages[p.idSmallImg];
               int px = p.dx;
               int py = p.dy;
               int pw = si.w;
               int ph = si.h;
               minX = Math.min(minX, px);
               minY = Math.min(minY, py);
               maxX = Math.max(maxX, px + pw);
               maxY = Math.max(maxY, py + ph);
            }
         }

         if (minX <= maxX && minY <= maxY) {
            int rX = Math.max(1, maxX - minX);
            int rY = Math.max(1, maxY - minY);
            double scale = Math.min((double)(tw - 10) / rX, (double)(th - 10) / rY);
            if (scale > 2.0) {
               scale = 2.0;
            }

            int ox = (int)((tw - rX * scale) / 2.0) - (int)(minX * scale);
            int oy = (int)((th - rY * scale) / 2.0) - (int)(minY * scale);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            for (EffPartFrame px : frame.allParts) {
               if (this.model.smallImages != null && px.idSmallImg >= 0 && px.idSmallImg < this.model.smallImages.length) {
                  SmallImageDef si = this.model.smallImages[px.idSmallImg];
                  int destX = ox + (int)(px.dx * scale);
                  int destY = oy + (int)(px.dy * scale);
                  int destW = Math.max(1, (int)(si.w * scale));
                  int destH = Math.max(1, (int)(si.h * scale));
                  int iw = this.model.atlasImage.getWidth();
                  int ih = this.model.atlasImage.getHeight();
                  int cx = si.x * 4;
                  int cy = si.y * 4;
                  int cw = si.w * 4;
                  int ch = si.h * 4;
                  if (cx < iw && cy < ih) {
                     if (cx + cw > iw) {
                        cw = iw - cx;
                     }

                     if (cy + ch > ih) {
                        ch = ih - cy;
                     }

                     if (px.rotate != 0) {
                        AffineTransform oldTx = g2.getTransform();
                        g2.rotate(Math.toRadians(px.rotate), destX + destW / 2.0, destY + destH / 2.0);
                        if (px.flip == 1) {
                           g2.drawImage(this.model.atlasImage, destX + destW, destY, destX, destY + destH, cx, cy, cx + cw, cy + ch, null);
                        } else {
                           g2.drawImage(this.model.atlasImage, destX, destY, destX + destW, destY + destH, cx, cy, cx + cw, cy + ch, null);
                        }

                        g2.setTransform(oldTx);
                     } else if (px.flip == 1) {
                        g2.drawImage(this.model.atlasImage, destX + destW, destY, destX, destY + destH, cx, cy, cx + cw, cy + ch, null);
                     } else {
                        g2.drawImage(this.model.atlasImage, destX, destY, destX + destW, destY + destH, cx, cy, cx + cw, cy + ch, null);
                     }
                  }
               }
            }

            g2.dispose();
            return img;
         } else {
            g2.dispose();
            return img;
         }
      } else {
         g2.dispose();
         return img;
      }
   }

   public EffectCanvas.PartGeom getSelectedPartGeom() {
      if (this.selectedPartIdx >= 0 && this.model != null && this.model.smallImages != null) {
         EffFrame frame = this.getCurrentFrame();
         if (frame != null && this.selectedPartIdx < frame.allParts.size()) {
            EffPartFrame p = frame.allParts.get(this.selectedPartIdx);
            if (p.idSmallImg >= 0 && p.idSmallImg < this.model.smallImages.length) {
               SmallImageDef si = this.model.smallImages[p.idSmallImg];
               int ax = this.drawAX();
               int ay = this.drawAY();
               int shiftY = 0;
               if (this.hOne == 62 && this.model != null && this.model.minAbsDy >= 50) {
                  shiftY = -32;
               }

               EffectCanvas.PartGeom g = new EffectCanvas.PartGeom();
               g.destX = ax + (int)(p.dx * 4.0 * this.zoom);
               g.destY = ay + (int)((p.dy + shiftY) * 4.0 * this.zoom);
               g.dw = (int)(si.w * 4.0 * this.zoom);
               g.dh = (int)(si.h * 4.0 * this.zoom);
               g.cx = g.destX + g.dw / 2.0;
               g.cy = g.destY + g.dh / 2.0;
               g.rotRad = Math.toRadians(p.rotate);
               double hw = g.dw / 2.0;
               double hh = g.dh / 2.0;
               double cos = Math.cos(g.rotRad);
               double sin = Math.sin(g.rotRad);
               g.pTL = new Double(g.cx + -hw * cos - -hh * sin, g.cy + -hw * sin + -hh * cos);
               g.pTR = new Double(g.cx + hw * cos - -hh * sin, g.cy + hw * sin + -hh * cos);
               g.pBR = new Double(g.cx + hw * cos - hh * sin, g.cy + hw * sin + hh * cos);
               g.pBL = new Double(g.cx + -hw * cos - hh * sin, g.cy + -hw * sin + hh * cos);
               double stalkLen = Math.max(18.0, 22.0 * Math.min(this.zoom, 1.5));
               g.pTopKnob = new Double(g.cx + 0.0 * cos - (-hh - stalkLen) * sin, g.cy + 0.0 * sin + (-hh - stalkLen) * cos);
               return g;
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private int hitTestRotationHandle(int mx, int my) {
      EffectCanvas.PartGeom g = this.getSelectedPartGeom();
      if (g == null) {
         return -1;
      } else if (g.pTopKnob.distance(mx, my) <= 12.0) {
         return 100;
      } else if (g.pTL.distance(mx, my) <= 9.0) {
         return 0;
      } else if (g.pTR.distance(mx, my) <= 9.0) {
         return 1;
      } else if (g.pBR.distance(mx, my) <= 9.0) {
         return 2;
      } else {
         return g.pBL.distance(mx, my) <= 9.0 ? 3 : -1;
      }
   }

   private void trySelectPart(int mx, int my) {
      EffFrame frame = this.getCurrentFrame();
      if (frame != null) {
         int ax = this.drawAX();
         int ay = this.drawAY();
         int shiftY = 0;
         if (this.hOne == 62 && this.model != null && this.model.minAbsDy >= 50) {
            shiftY = -32;
         }

         List<EffPartFrame> all = frame.allParts;

         for (int i = all.size() - 1; i >= 0; i--) {
            EffPartFrame p = all.get(i);
            if (this.model.smallImages != null && p.idSmallImg >= 0 && p.idSmallImg < this.model.smallImages.length) {
               SmallImageDef si = this.model.smallImages[p.idSmallImg];
               int dx2 = ax + (int)(p.dx * 4.0 * this.zoom);
               int dy2 = ay + (int)((p.dy + shiftY) * 4.0 * this.zoom);
               int dw2 = (int)(si.w * 4.0 * this.zoom);
               int dh2 = (int)(si.h * 4.0 * this.zoom);
               double cx = dx2 + dw2 / 2.0;
               double cy = dy2 + dh2 / 2.0;
               double testX = mx;
               double testY = my;
               if (p.rotate != 0) {
                  double rad = Math.toRadians(-p.rotate);
                  double cos = Math.cos(rad);
                  double sin = Math.sin(rad);
                  testX = cx + (mx - cx) * cos - (my - cy) * sin;
                  testY = cy + (mx - cx) * sin + (my - cy) * cos;
               }

               if (testX >= dx2 && testX <= dx2 + dw2 && testY >= dy2 && testY <= dy2 + dh2) {
                  this.selectedPartIdx = i;
                  this.dragStartDx = p.dx;
                  this.dragStartDy = p.dy;
                  this.isDraggingPart = true;
                  this.repaint();
                  if (this.clickListener != null) {
                     this.clickListener.onPartClicked(i, p);
                  }

                  return;
               }
            }
         }

         this.selectedPartIdx = -1;
         this.repaint();
      }
   }

   public interface OnPartClickListener {
      void onPartClicked(int var1, EffPartFrame var2);
   }

   public interface OnPartMovedListener {
      void onPartMoved(int var1, EffPartFrame var2);
   }

   public static class PartGeom {
      public int destX;
      public int destY;
      public int dw;
      public int dh;
      public double cx;
      public double cy;
      public double rotRad;
      public Double pTL;
      public Double pTR;
      public Double pBR;
      public Double pBL;
      public Double pTopKnob;
   }
}
