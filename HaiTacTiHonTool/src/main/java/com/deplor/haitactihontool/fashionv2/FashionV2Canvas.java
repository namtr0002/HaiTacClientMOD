package com.deplor.haitactihontool.fashionv2;

import com.deplor.haitactihontool.config.Theme;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class FashionV2Canvas extends JPanel {
   private FashionV2Model.FashionV2 fashion;
   private FashionV2Model.SequenceV2 currentSequence;
   private int currentFrameIndex = 0;
   private FashionV2Model.FramePartTransformV2 selectedTransform = null;
   private float camX = 0.0F;
   private float camY = 0.0F;
   private float zoom = 2.5F;
   private Point lastMousePos = new Point();
   private boolean showGrid = true;
   private boolean showPivot = true;
   private boolean showOnionSkin = false;
   private boolean showHitbox = true;
   private boolean isPlaying = false;
   private Timer animTimer;
   private Runnable onFrameChangedListener;
   private Runnable onTransformSelectedListener;
   private final Map<String, BufferedImage> atlasCache = new ConcurrentHashMap<>();

   public FashionV2Canvas() {
      this.setBackground(Theme.BG_DARKER);
      this.setFocusable(true);
      this.setupMouseListeners();
      this.setupAnimationTimer();
   }

   public void setFashion(FashionV2Model.FashionV2 fashion) {
      this.fashion = fashion;
      if (fashion != null && !fashion.sequences.isEmpty()) {
         this.setSequence(fashion.sequences.get(0));
      } else {
         this.currentSequence = null;
         this.currentFrameIndex = 0;
         this.selectedTransform = null;
      }

      this.repaint();
   }

   public void setSequence(FashionV2Model.SequenceV2 sequence) {
      this.currentSequence = sequence;
      this.currentFrameIndex = 0;
      this.selectedTransform = null;
      if (this.animTimer != null && this.currentSequence != null) {
         int delay = Math.max(16, Math.round(1000.0F / this.currentSequence.fps));
         this.animTimer.setDelay(delay);
      }

      this.repaint();
      if (this.onFrameChangedListener != null) {
         this.onFrameChangedListener.run();
      }
   }

   public void setFrameIndex(int index) {
      if (this.currentSequence != null && !this.currentSequence.frames.isEmpty()) {
         this.currentFrameIndex = Math.max(0, Math.min(index, this.currentSequence.frames.size() - 1));
         this.repaint();
         if (this.onFrameChangedListener != null) {
            this.onFrameChangedListener.run();
         }
      }
   }

   public int getCurrentFrameIndex() {
      return this.currentFrameIndex;
   }

   public FashionV2Model.SequenceV2 getCurrentSequence() {
      return this.currentSequence;
   }

   public FashionV2Model.FrameV2 getCurrentFrame() {
      return this.currentSequence != null && this.currentFrameIndex < this.currentSequence.frames.size()
         ? this.currentSequence.frames.get(this.currentFrameIndex)
         : null;
   }

   public FashionV2Model.FramePartTransformV2 getSelectedTransform() {
      return this.selectedTransform;
   }

   public void setSelectedTransform(FashionV2Model.FramePartTransformV2 t) {
      this.selectedTransform = t;
      this.repaint();
   }

   public void setOnFrameChangedListener(Runnable listener) {
      this.onFrameChangedListener = listener;
   }

   public void setOnTransformSelectedListener(Runnable listener) {
      this.onTransformSelectedListener = listener;
   }

   public void playAnimation() {
      if (this.currentSequence != null && !this.currentSequence.frames.isEmpty()) {
         this.isPlaying = true;
         this.animTimer.start();
      }
   }

   public void pauseAnimation() {
      this.isPlaying = false;
      this.animTimer.stop();
   }

   public boolean isPlaying() {
      return this.isPlaying;
   }

   public void setShowGrid(boolean show) {
      this.showGrid = show;
      this.repaint();
   }

   public void setShowPivot(boolean show) {
      this.showPivot = show;
      this.repaint();
   }

   public void setShowOnionSkin(boolean show) {
      this.showOnionSkin = show;
      this.repaint();
   }

   public void setShowHitbox(boolean show) {
      this.showHitbox = show;
      this.repaint();
   }

   public void setZoom(float zoom) {
      this.zoom = Math.max(0.5F, Math.min(zoom, 10.0F));
      this.repaint();
   }

   public float getZoom() {
      return this.zoom;
   }

   public void resetCamera() {
      this.camX = 0.0F;
      this.camY = 0.0F;
      this.zoom = 2.5F;
      this.repaint();
   }

   private void setupAnimationTimer() {
      this.animTimer = new Timer(83, e -> {
         if (this.currentSequence != null && !this.currentSequence.frames.isEmpty()) {
            this.currentFrameIndex = (this.currentFrameIndex + 1) % this.currentSequence.frames.size();
            this.repaint();
            if (this.onFrameChangedListener != null) {
               this.onFrameChangedListener.run();
            }
         }
      });
   }

   private void setupMouseListeners() {
      MouseAdapter adapter = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            FashionV2Canvas.this.lastMousePos = e.getPoint();
            if (SwingUtilities.isLeftMouseButton(e)) {
               FashionV2Canvas.this.selectPartAtScreenPos(e.getPoint());
            }
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            Point currentPos = e.getPoint();
            int dx = currentPos.x - FashionV2Canvas.this.lastMousePos.x;
            int dy = currentPos.y - FashionV2Canvas.this.lastMousePos.y;
            if (SwingUtilities.isRightMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
               FashionV2Canvas.this.camX += dx;
               FashionV2Canvas.this.camY += dy;
               FashionV2Canvas.this.repaint();
            } else if (SwingUtilities.isLeftMouseButton(e) && FashionV2Canvas.this.selectedTransform != null) {
               FashionV2Canvas.this.selectedTransform.posX = FashionV2Canvas.this.selectedTransform.posX + dx / FashionV2Canvas.this.zoom;
               FashionV2Canvas.this.selectedTransform.posY = FashionV2Canvas.this.selectedTransform.posY + dy / FashionV2Canvas.this.zoom;
               FashionV2Canvas.this.repaint();
               if (FashionV2Canvas.this.onTransformSelectedListener != null) {
                  FashionV2Canvas.this.onTransformSelectedListener.run();
               }
            }

            FashionV2Canvas.this.lastMousePos = currentPos;
         }

         @Override
         public void mouseWheelMoved(MouseWheelEvent e) {
            float zoomFactor = e.getWheelRotation() < 0 ? 1.15F : 0.85F;
            FashionV2Canvas.this.zoom = Math.max(0.5F, Math.min(FashionV2Canvas.this.zoom * zoomFactor, 10.0F));
            FashionV2Canvas.this.repaint();
         }
      };
      this.addMouseListener(adapter);
      this.addMouseMotionListener(adapter);
      this.addMouseWheelListener(adapter);
   }

   private void selectPartAtScreenPos(Point mousePt) {
      FashionV2Model.FrameV2 frame = this.getCurrentFrame();
      if (frame != null) {
         int centerX = this.getWidth() / 2 + Math.round(this.camX);
         int centerY = this.getHeight() / 2 + Math.round(this.camY);

         for (int i = frame.partTransforms.size() - 1; i >= 0; i--) {
            FashionV2Model.FramePartTransformV2 t = frame.partTransforms.get(i);
            int partScreenX = centerX + Math.round(t.posX * this.zoom);
            int partScreenY = centerY + Math.round(t.posY * this.zoom);
            int boxWidth = Math.round(30.0F * t.scaleX * this.zoom);
            int boxHeight = Math.round(30.0F * t.scaleY * this.zoom);
            Rectangle rect = new Rectangle(partScreenX - boxWidth / 2, partScreenY - boxHeight / 2, boxWidth, boxHeight);
            if (rect.contains(mousePt)) {
               this.selectedTransform = t;
               this.repaint();
               if (this.onTransformSelectedListener != null) {
                  this.onTransformSelectedListener.run();
               }

               return;
            }
         }

         this.selectedTransform = null;
         this.repaint();
         if (this.onTransformSelectedListener != null) {
            this.onTransformSelectedListener.run();
         }
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g.create();
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      int centerX = this.getWidth() / 2 + Math.round(this.camX);
      int centerY = this.getHeight() / 2 + Math.round(this.camY);
      if (this.showGrid) {
         this.drawGrid(g2d, centerX, centerY);
      }

      this.drawFloorShadow(g2d, centerX, centerY);
      if (this.showOnionSkin && this.currentSequence != null && this.currentSequence.frames.size() > 1) {
         int prevFrameIdx = (this.currentFrameIndex - 1 + this.currentSequence.frames.size()) % this.currentSequence.frames.size();
         FashionV2Model.FrameV2 prevFrame = this.currentSequence.frames.get(prevFrameIdx);
         this.drawFrameContent(g2d, prevFrame, centerX, centerY, 0.25F, false);
      }

      FashionV2Model.FrameV2 currentFrame = this.getCurrentFrame();
      if (currentFrame != null) {
         this.drawFrameContent(g2d, currentFrame, centerX, centerY, 1.0F, true);
      }

      if (this.showPivot) {
         this.drawPivotAxis(g2d, centerX, centerY);
      }

      this.drawHUD(g2d);
      g2d.dispose();
   }

   private void drawGrid(Graphics2D g2d, int cx, int cy) {
      g2d.setColor(Theme.BORDER);
      int gridSize = Math.round(20.0F * this.zoom);
      if (gridSize < 10) {
         gridSize = 10;
      }

      for (int x = cx % gridSize; x < this.getWidth(); x += gridSize) {
         g2d.drawLine(x, 0, x, this.getHeight());
      }

      for (int y = cy % gridSize; y < this.getHeight(); y += gridSize) {
         g2d.drawLine(0, y, this.getWidth(), y);
      }
   }

   private void drawFloorShadow(Graphics2D g2d, int cx, int cy) {
      g2d.setColor(new Color(0, 0, 0, 80));
      int shadowW = Math.round(50.0F * this.zoom);
      int shadowH = Math.round(12.0F * this.zoom);
      g2d.fillOval(cx - shadowW / 2, cy - shadowH / 2, shadowW, shadowH);
   }

   private void drawPivotAxis(Graphics2D g2d, int cx, int cy) {
      g2d.setStroke(new BasicStroke(1.5F));
      g2d.setColor(new Color(255, 70, 70, 200));
      g2d.drawLine(cx - 30, cy, cx + 30, cy);
      g2d.setColor(new Color(70, 255, 70, 200));
      g2d.drawLine(cx, cy - 30, cx, cy + 30);
      g2d.setColor(Theme.ACCENT);
      g2d.fillOval(cx - 4, cy - 4, 8, 8);
   }

   private BufferedImage getAtlasImage(String atlasPath) {
      if (atlasPath != null && !atlasPath.isEmpty()) {
         BufferedImage cached = this.atlasCache.get(atlasPath);
         if (cached != null) {
            return cached;
         } else {
            File atlasFile = new File("Data/FashionV2", atlasPath);
            if (atlasFile.exists()) {
               try {
                  BufferedImage img = ImageIO.read(atlasFile);
                  if (img != null) {
                     this.atlasCache.put(atlasPath, img);
                     return img;
                  }
               } catch (Exception var5) {
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public void clearAtlasCache() {
      this.atlasCache.clear();
   }

   private void drawFrameContent(Graphics2D g2d, FashionV2Model.FrameV2 frame, int cx, int cy, float globalAlpha, boolean isCurrent) {
      for (FashionV2Model.FramePartTransformV2 t : frame.partTransforms) {
         int drawX = cx + Math.round(t.posX * this.zoom);
         int drawY = cy + Math.round(t.posY * this.zoom);
         int boxWidth = Math.round(32.0F * t.scaleX * this.zoom);
         int boxHeight = Math.round(32.0F * t.scaleY * this.zoom);
         AffineTransform oldTx = g2d.getTransform();
         g2d.translate(drawX, drawY);
         if (t.rotation != 0.0F) {
            g2d.rotate(Math.toRadians(t.rotation));
         }

         float alpha = t.opacity * globalAlpha;
         g2d.setComposite(AlphaComposite.getInstance(3, alpha));
         boolean spriteDrawn = false;
         if (this.fashion != null && !this.fashion.sprites.isEmpty()) {
            for (FashionV2Model.SpriteV2 sprite : this.fashion.sprites) {
               if ((t.spriteId != null && t.spriteId.equals(sprite.spriteId) || this.fashion.sprites.size() == 1) && sprite.atlasPath != null) {
                  BufferedImage atlasImg = this.getAtlasImage(sprite.atlasPath);
                  if (atlasImg != null && sprite.width > 0 && sprite.height > 0) {
                     int cropX = Math.min(sprite.x, atlasImg.getWidth() - 1);
                     int cropY = Math.min(sprite.y, atlasImg.getHeight() - 1);
                     int cropW = Math.min(sprite.width, atlasImg.getWidth() - cropX);
                     int cropH = Math.min(sprite.height, atlasImg.getHeight() - cropY);
                     if (cropW > 0 && cropH > 0) {
                        BufferedImage subImg = atlasImg.getSubimage(cropX, cropY, cropW, cropH);
                        int renderW = Math.round(cropW * t.scaleX * this.zoom);
                        int renderH = Math.round(cropH * t.scaleY * this.zoom);
                        g2d.drawImage(subImg, -renderW / 2, -renderH / 2, renderW, renderH, null);
                        spriteDrawn = true;
                        break;
                     }
                  }
               }
            }
         }

         if (!spriteDrawn) {
            Color partColor = this.getSlotColor(t.partId);
            g2d.setColor(partColor);
            g2d.fillRect(-boxWidth / 2, -boxHeight / 2, boxWidth, boxHeight);
            g2d.setColor(partColor.brighter());
            g2d.setStroke(new BasicStroke(1.5F));
            g2d.drawRect(-boxWidth / 2, -boxHeight / 2, boxWidth, boxHeight);
            g2d.setColor(Color.WHITE);
            g2d.setFont(Theme.F_TINY);
            FontMetrics fm = g2d.getFontMetrics();
            String label = t.partId;
            g2d.drawString(label, -fm.stringWidth(label) / 2, 4);
         }

         if (isCurrent && t == this.selectedTransform) {
            g2d.setColor(Theme.ACCENT);
            g2d.setStroke(new BasicStroke(2.0F, 0, 0, 10.0F, new float[]{4.0F}, 0.0F));
            g2d.drawRect(-boxWidth / 2 - 3, -boxHeight / 2 - 3, boxWidth + 6, boxHeight + 6);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(-boxWidth / 2 - 6, -boxHeight / 2 - 6, 6, 6);
            g2d.fillRect(boxWidth / 2, -boxHeight / 2 - 6, 6, 6);
            g2d.fillRect(-boxWidth / 2 - 6, boxHeight / 2, 6, 6);
            g2d.fillRect(boxWidth / 2, boxHeight / 2, 6, 6);
         }

         g2d.setTransform(oldTx);
      }
   }

   private Color getSlotColor(String partId) {
      if (partId == null) {
         return new Color(100, 100, 120);
      } else {
         String lower = partId.toLowerCase();
         if (lower.contains("hair")) {
            return new Color(240, 120, 60, 200);
         } else if (lower.contains("body")) {
            return new Color(60, 160, 240, 200);
         } else if (lower.contains("weapon")) {
            return new Color(240, 60, 120, 200);
         } else if (lower.contains("hat")) {
            return new Color(160, 240, 60, 200);
         } else {
            return !lower.contains("cloak") && !lower.contains("wing") ? new Color(100, 180, 180, 200) : new Color(180, 80, 240, 200);
         }
      }
   }

   private void drawHUD(Graphics2D g2d) {
      g2d.setFont(Theme.F_SMALL);
      g2d.setColor(Theme.TEXT_MUTED);
      String seqName = this.currentSequence != null ? this.currentSequence.name : "None";
      int totalFrames = this.currentSequence != null ? this.currentSequence.frames.size() : 0;
      String hudText = String.format(
         "Sequence: %s | Frame: %d / %d | Zoom: %.1fx | FPS: %s",
         seqName,
         this.currentFrameIndex + 1,
         totalFrames,
         this.zoom,
         this.currentSequence != null ? this.currentSequence.fps : 12.0F
      );
      g2d.drawString(hudText, 15, 25);
      if (this.selectedTransform != null) {
         String selectedInfo = String.format(
            "Selected Part: %s [X: %.1f, Y: %.1f, Rot: %.0f°]",
            this.selectedTransform.partId,
            this.selectedTransform.posX,
            this.selectedTransform.posY,
            this.selectedTransform.rotation
         );
         g2d.setColor(Theme.ACCENT);
         g2d.drawString(selectedInfo, 15, 45);
      }
   }
}
