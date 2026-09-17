package com.deplor.haitactihontool.ui.components;

import com.deplor.haitactihontool.config.Theme;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePreviewPanel extends JPanel {
   private BufferedImage image;
   private double zoom = 1.0;
   private double minZoom = 0.05;
   private double maxZoom = 20.0;
   private final Double offset = new Double();
   private Point dragStart;
   private boolean autoFit = true;

   public ImagePreviewPanel() {
      this.setBackground(Theme.BG_DARK);
      this.setOpaque(true);
      this.addMouseWheelListener(e -> {
         if (this.image != null) {
            double factor = e.getPreciseWheelRotation() > 0.0 ? 0.9 : 1.1;
            this.zoomAt(e.getPoint(), factor);
         }
      });
      MouseAdapter ma = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            ImagePreviewPanel.this.dragStart = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            if (ImagePreviewPanel.this.image != null && ImagePreviewPanel.this.dragStart != null) {
               ImagePreviewPanel.this.offset.x = ImagePreviewPanel.this.offset.x + (e.getX() - ImagePreviewPanel.this.dragStart.x);
               ImagePreviewPanel.this.offset.y = ImagePreviewPanel.this.offset.y + (e.getY() - ImagePreviewPanel.this.dragStart.y);
               ImagePreviewPanel.this.dragStart = e.getPoint();
               ImagePreviewPanel.this.autoFit = false;
               ImagePreviewPanel.this.repaint();
            }
         }

         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
               ImagePreviewPanel.this.fit();
            }
         }
      };
      this.addMouseListener(ma);
      this.addMouseMotionListener(ma);
      this.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            if (ImagePreviewPanel.this.autoFit && ImagePreviewPanel.this.image != null) {
               ImagePreviewPanel.this.fit();
            }
         }
      });
   }

   public void setImage(BufferedImage image) {
      this.image = image;
      this.autoFit = true;
      this.fit();
      this.repaint();
   }

   public void setImage(Image image) {
      if (image == null) {
         this.setImage((BufferedImage)null);
      } else {
         BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
         Graphics2D g2 = bi.createGraphics();
         g2.drawImage(image, 0, 0, null);
         g2.dispose();
         this.setImage(bi);
      }
   }

   public void clearImage() {
      this.image = null;
      this.repaint();
   }

   public void setZoomLimits(double minZoom, double maxZoom) {
      this.minZoom = Math.max(0.01, minZoom);
      this.maxZoom = Math.max(this.minZoom, maxZoom);
      this.zoom = this.clamp(this.zoom, this.minZoom, this.maxZoom);
      this.repaint();
   }

   public void fit() {
      if (this.image != null && this.getWidth() > 0 && this.getHeight() > 0) {
         double zx = (double)this.getWidth() / this.image.getWidth();
         double zy = (double)this.getHeight() / this.image.getHeight();
         this.zoom = this.clamp(Math.min(zx, zy), this.minZoom, this.maxZoom);
         this.offset.x = (this.getWidth() - this.image.getWidth() * this.zoom) / 2.0;
         this.offset.y = (this.getHeight() - this.image.getHeight() * this.zoom) / 2.0;
         this.repaint();
      }
   }

   public void resetView() {
      this.zoom = 1.0;
      this.offset.x = 0.0;
      this.offset.y = 0.0;
      this.autoFit = false;
      this.repaint();
   }

   public void zoomIn() {
      if (this.image != null) {
         this.zoomAt(new Point(this.getWidth() / 2, this.getHeight() / 2), 1.15);
      }
   }

   public void zoomOut() {
      if (this.image != null) {
         this.zoomAt(new Point(this.getWidth() / 2, this.getHeight() / 2), 0.87);
      }
   }

   private void zoomAt(Point anchor, double factor) {
      if (this.image != null) {
         double beforeX = (anchor.x - this.offset.x) / this.zoom;
         double beforeY = (anchor.y - this.offset.y) / this.zoom;
         this.zoom = this.clamp(this.zoom * factor, this.minZoom, this.maxZoom);
         this.offset.x = anchor.x - beforeX * this.zoom;
         this.offset.y = anchor.y - beforeY * this.zoom;
         this.autoFit = false;
         this.repaint();
      }
   }

   private double clamp(double v, double min, double max) {
      return Math.max(min, Math.min(max, v));
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g.create();
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      this.paintChecker(g2);
      if (this.image == null) {
         g2.setColor(Theme.TEXT_MUTED);
         g2.setFont(new Font("Segoe UI", 0, 14));
         String s = "No image";
         FontMetrics fm = g2.getFontMetrics();
         g2.drawString(s, (this.getWidth() - fm.stringWidth(s)) / 2, this.getHeight() / 2);
         g2.dispose();
      } else {
         g2.translate(this.offset.x, this.offset.y);
         g2.scale(this.zoom, this.zoom);
         g2.drawImage(this.image, 0, 0, null);
         g2.dispose();
      }
   }

   private void paintChecker(Graphics2D g2) {
      int size = 16;
      Color a = new Color(38, 38, 46);
      Color b = new Color(30, 30, 36);

      for (int y = 0; y < this.getHeight(); y += size) {
         for (int x = 0; x < this.getWidth(); x += size) {
            boolean on = (x / size + y / size) % 2 == 0;
            g2.setColor(on ? a : b);
            g2.fillRect(x, y, size, size);
         }
      }
   }
}
