package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class SpriteSheetViewer extends JPanel {
   private EffectModel model;
   private int selectedSpriteIdx = -1;
   private int hoveredSpriteIdx = -1;
   private double zoom = 1.0;
   private int offsetX = 0;
   private int offsetY = 0;
   private Point dragStart;
   private SpriteSheetViewer.OnSpriteActionListener listener;

   public SpriteSheetViewer() {
      this.setBackground(new Color(14, 14, 18));
      this.setFocusable(true);
      this.setupInteraction();
   }

   public void setListener(SpriteSheetViewer.OnSpriteActionListener l) {
      this.listener = l;
   }

   public void setModel(EffectModel m) {
      this.model = m;
      this.selectedSpriteIdx = -1;
      this.hoveredSpriteIdx = -1;
      this.resetView();
      this.repaint();
   }

   public void setSelectedSprite(int idx) {
      this.selectedSpriteIdx = idx;
      this.repaint();
   }

   public int getSelectedSprite() {
      return this.selectedSpriteIdx;
   }

   public void resetView() {
      this.zoom = 1.0;
      this.offsetX = 20;
      this.offsetY = 20;
      this.repaint();
   }

   private void setupInteraction() {
      this.addMouseListener(
         new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               SpriteSheetViewer.this.requestFocusInWindow();
               SpriteSheetViewer.this.dragStart = e.getPoint();
               int clicked = SpriteSheetViewer.this.findSpriteAt(e.getX(), e.getY());
               if (clicked >= 0) {
                  SpriteSheetViewer.this.selectedSpriteIdx = clicked;
                  if (SpriteSheetViewer.this.listener != null
                     && SpriteSheetViewer.this.model != null
                     && SpriteSheetViewer.this.model.smallImages != null
                     && clicked < SpriteSheetViewer.this.model.smallImages.length) {
                     SpriteSheetViewer.this.listener.onSpriteSelected(clicked, SpriteSheetViewer.this.model.smallImages[clicked]);
                  }

                  if (e.getClickCount() == 2 && SpriteSheetViewer.this.listener != null) {
                     SpriteSheetViewer.this.listener.onSpriteDoubleClicked(clicked, SpriteSheetViewer.this.model.smallImages[clicked]);
                  } else if (SwingUtilities.isRightMouseButton(e)) {
                     SpriteSheetViewer.this.showContextMenu(e.getComponent(), e.getX(), e.getY(), clicked);
                  }

                  SpriteSheetViewer.this.repaint();
               } else if (SwingUtilities.isRightMouseButton(e)) {
                  SpriteSheetViewer.this.showContextMenu(e.getComponent(), e.getX(), e.getY(), -1);
               }
            }
         }
      );
      this.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (SpriteSheetViewer.this.dragStart != null) {
               SpriteSheetViewer.this.offsetX = SpriteSheetViewer.this.offsetX + (e.getX() - SpriteSheetViewer.this.dragStart.x);
               SpriteSheetViewer.this.offsetY = SpriteSheetViewer.this.offsetY + (e.getY() - SpriteSheetViewer.this.dragStart.y);
               SpriteSheetViewer.this.dragStart = e.getPoint();
               SpriteSheetViewer.this.repaint();
            }
         }

         @Override
         public void mouseMoved(MouseEvent e) {
            int hover = SpriteSheetViewer.this.findSpriteAt(e.getX(), e.getY());
            if (hover != SpriteSheetViewer.this.hoveredSpriteIdx) {
               SpriteSheetViewer.this.hoveredSpriteIdx = hover;
               SpriteSheetViewer.this.repaint();
            }
         }
      });
      this.addMouseWheelListener(e -> {
         if (e.isControlDown()) {
            double oldZ = this.zoom;
            if (e.getWheelRotation() < 0) {
               this.zoom *= 1.15;
            } else {
               this.zoom /= 1.15;
            }

            this.zoom = Math.max(0.2, Math.min(10.0, this.zoom));
            int mx = e.getX();
            int my = e.getY();
            double f = this.zoom / oldZ;
            this.offsetX = (int)(mx - f * (mx - this.offsetX));
            this.offsetY = (int)(my - f * (my - this.offsetY));
            this.repaint();
         } else if (e.isShiftDown()) {
            this.offsetX = this.offsetX - e.getWheelRotation() * 40;
            this.repaint();
         } else {
            this.offsetY = this.offsetY - e.getWheelRotation() * 40;
            this.repaint();
         }
      });
      InputMap im = this.getInputMap(2);
      ActionMap am = this.getActionMap();
      im.put(KeyStroke.getKeyStroke(38, 128), "zoomIn");
      am.put("zoomIn", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            SpriteSheetViewer.this.zoom *= 1.15;
            SpriteSheetViewer.this.zoom = Math.min(10.0, SpriteSheetViewer.this.zoom);
            SpriteSheetViewer.this.repaint();
         }
      });
      im.put(KeyStroke.getKeyStroke(40, 128), "zoomOut");
      am.put("zoomOut", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            SpriteSheetViewer.this.zoom /= 1.15;
            SpriteSheetViewer.this.zoom = Math.max(0.2, SpriteSheetViewer.this.zoom);
            SpriteSheetViewer.this.repaint();
         }
      });
      im.put(KeyStroke.getKeyStroke(37, 64), "panLeft");
      am.put("panLeft", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            SpriteSheetViewer.this.offsetX += 40;
            SpriteSheetViewer.this.repaint();
         }
      });
      im.put(KeyStroke.getKeyStroke(39, 64), "panRight");
      am.put("panRight", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            SpriteSheetViewer.this.offsetX -= 40;
            SpriteSheetViewer.this.repaint();
         }
      });
   }

   private int findSpriteAt(int mx, int my) {
      if (this.model != null && this.model.smallImages != null) {
         for (int i = this.model.smallImages.length - 1; i >= 0; i--) {
            SmallImageDef s = this.model.smallImages[i];
            int sx = this.offsetX + (int)(s.x * 4 * this.zoom);
            int sy = this.offsetY + (int)(s.y * 4 * this.zoom);
            int sw = (int)(s.w * 4 * this.zoom);
            int sh = (int)(s.h * 4 * this.zoom);
            if (mx >= sx && mx <= sx + sw && my >= sy && my <= sy + sh) {
               return i;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
      this.drawCheckerboard(g2);
      if (this.model != null && this.model.atlasImage != null) {
         BufferedImage atlas = this.model.atlasImage;
         int aw = (int)(atlas.getWidth() * this.zoom);
         int ah = (int)(atlas.getHeight() * this.zoom);
         g2.drawImage(atlas, this.offsetX, this.offsetY, aw, ah, null);
         g2.setColor(new Color(60, 60, 80));
         g2.drawRect(this.offsetX, this.offsetY, aw, ah);
         if (this.model.smallImages != null) {
            for (int i = 0; i < this.model.smallImages.length; i++) {
               SmallImageDef s = this.model.smallImages[i];
               int sx = this.offsetX + (int)(s.x * 4 * this.zoom);
               int sy = this.offsetY + (int)(s.y * 4 * this.zoom);
               int sw = (int)(s.w * 4 * this.zoom);
               int sh = (int)(s.h * 4 * this.zoom);
               boolean isSel = i == this.selectedSpriteIdx;
               boolean isHov = i == this.hoveredSpriteIdx;
               if (isSel) {
                  g2.setColor(new Color(0, 180, 255, 60));
                  g2.fillRect(sx, sy, sw, sh);
                  g2.setColor(new Color(0, 220, 255));
                  g2.setStroke(new BasicStroke(2.0F));
                  g2.drawRect(sx, sy, sw, sh);
               } else if (isHov) {
                  g2.setColor(new Color(255, 200, 0, 50));
                  g2.fillRect(sx, sy, sw, sh);
                  g2.setColor(new Color(255, 200, 0));
                  g2.setStroke(new BasicStroke(1.2F));
                  g2.drawRect(sx, sy, sw, sh);
               } else {
                  g2.setColor(new Color(0, 150, 220, 160));
                  g2.setStroke(new BasicStroke(0.8F));
                  g2.drawRect(sx, sy, sw, sh);
               }

               if (sw > 10 && sh > 10) {
                  g2.setFont(new Font("Segoe UI", 1, 9));
                  g2.setColor(isSel ? Color.WHITE : new Color(200, 220, 255));
                  g2.drawString("#" + i, sx + 2, sy + 10);
               }
            }
         }

         g2.setFont(new Font("Segoe UI", 0, 10));
         g2.setColor(new Color(160, 160, 180));
         String info = String.format(
            "Atlas: %d×%d | Sprites: %d | Zoom: %.1fx (Ctrl+Lăn: Zoom, Shift+Lăn: Pan)",
            atlas.getWidth(),
            atlas.getHeight(),
            this.model.smallImages != null ? this.model.smallImages.length : 0,
            this.zoom
         );
         g2.drawString(info, 8, this.getHeight() - 8);
      } else {
         g2.setFont(new Font("Segoe UI", 0, 12));
         g2.setColor(new Color(100, 100, 130));
         g2.drawString("Chưa có ảnh Sprite Sheet Atlas", 20, 30);
      }
   }

   private void drawCheckerboard(Graphics2D g2) {
      int sz = 16;

      for (int y = 0; y < this.getHeight(); y += sz) {
         for (int x = 0; x < this.getWidth(); x += sz) {
            if ((x / sz + y / sz) % 2 == 0) {
               g2.setColor(new Color(18, 18, 24));
            } else {
               g2.setColor(new Color(12, 12, 16));
            }

            g2.fillRect(x, y, sz, sz);
         }
      }
   }

   private void showContextMenu(Component comp, int x, int y, int spriteId) {
      JPopupMenu menu = new JPopupMenu();
      menu.setBackground(new Color(24, 24, 32));
      if (spriteId >= 0 && this.model != null && this.model.smallImages != null && spriteId < this.model.smallImages.length) {
         SmallImageDef s = this.model.smallImages[spriteId];
         JMenuItem miAdd = this.item("Thêm Sprite #" + spriteId + " vào Frame hiện tại (Double-click)");
         miAdd.setFont(new Font("Segoe UI", 1, 11));
         miAdd.setForeground(Theme.ACCENT);
         miAdd.addActionListener(e -> {
            if (this.listener != null) {
               this.listener.onSpriteDoubleClicked(spriteId, s);
            }
         });
         menu.add(miAdd);
         JMenuItem miInfo = this.item("Kích thước: " + s.w + "×" + s.h + " (Atlas: " + s.x + ", " + s.y + ")");
         miInfo.setEnabled(false);
         menu.add(miInfo);
         menu.addSeparator();
      }

      JMenuItem miCutter = this.item("Mở Bộ Cắt Sprite (Sprite Cutter)...");
      miCutter.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onOpenSpriteCutter();
         }
      });
      menu.add(miCutter);
      JMenuItem miReset = this.item("Đặt lại góc nhìn (Reset View)");
      miReset.addActionListener(e -> this.resetView());
      menu.add(miReset);
      menu.show(comp, x, y);
   }

   private JMenuItem item(String text) {
      JMenuItem mi = new JMenuItem(text);
      mi.setFont(new Font("Segoe UI", 0, 11));
      mi.setForeground(new Color(210, 210, 230));
      mi.setBackground(new Color(24, 24, 32));
      return mi;
   }

   public interface OnSpriteActionListener {
      void onSpriteSelected(int var1, SmallImageDef var2);

      void onSpriteDoubleClicked(int var1, SmallImageDef var2);

      void onOpenSpriteCutter();
   }
}
