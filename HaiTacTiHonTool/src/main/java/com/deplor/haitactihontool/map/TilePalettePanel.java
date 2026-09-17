package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class TilePalettePanel extends JPanel {
   private int selectedTileId = 1;
   private BufferedImage tileImage;
   private static final int DISPLAY_SIZE = 48;
   private static final int GAP = 4;
   private TilePalettePanel.TileSelectionListener listener;
   private boolean isWaterPalette = false;

   public TilePalettePanel(boolean isWater) {
      this.isWaterPalette = isWater;
      this.setBackground(Theme.BG_DARKER);
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            int cols = Math.max(1, TilePalettePanel.this.getWidth() / 52);
            int col = e.getX() / 52;
            int row = e.getY() / 52;
            int index = row * cols + col;
            if (index >= 0 && index < TilePalettePanel.this.getTotalSprites()) {
               TilePalettePanel.this.selectTile(index);
            }
         }
      });
   }

   private int getTotalSprites() {
      if (this.tileImage == null) {
         return 20;
      } else {
         int wTileImg = this.getAssetZoom(this.tileImage) * 24;
         if (wTileImg <= 0) {
            return 20;
         } else {
            int colsInSheet = this.tileImage.getWidth() / wTileImg;
            return colsInSheet * 10;
         }
      }
   }

   public void selectTile(int id) {
      this.selectedTileId = id;
      if (this.listener != null) {
         this.listener.onTileSelected(id);
      }

      this.repaint();
   }

   public void setTileSelectionListener(TilePalettePanel.TileSelectionListener listener) {
      this.listener = listener;
   }

   public void loadTileSet(int tileSetId, String basePath) {
      this.tileImage = this.isWaterPalette ? ImageCache.getWaterImage(tileSetId, basePath) : ImageCache.getTileImage(tileSetId, basePath);
      this.updatePreferredSize();
      this.revalidate();
      this.repaint();
   }

   private int getAssetZoom(BufferedImage img) {
      if (img == null) {
         return 4;
      } else {
         int w = img.getWidth();
         if (w % 96 == 0) {
            return 4;
         } else if (w % 72 == 0) {
            return 3;
         } else {
            return w % 48 == 0 ? 2 : 1;
         }
      }
   }

   private void updatePreferredSize() {
      int total = this.getTotalSprites();
      int cols = 4;
      if (this.getWidth() > 0) {
         cols = Math.max(1, this.getWidth() / 52);
      }

      int rows = (total + cols - 1) / cols;
      int targetH = rows * 52 + 20;
      if (this.getPreferredSize().height != targetH) {
         this.setPreferredSize(new Dimension(100, targetH));
         this.revalidate();
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      int total = this.getTotalSprites();
      int cols = Math.max(1, this.getWidth() / 52);
      int wTileImg = 24;
      if (this.tileImage != null) {
         wTileImg = this.getAssetZoom(this.tileImage) * 24;
      }

      for (int i = 0; i < total; i++) {
         int row = i / cols;
         int col = i % cols;
         int x = col * 52 + 5;
         int y = row * 52 + 5;
         g2.setColor(Theme.BG_CARD);
         g2.fillRoundRect(x, y, 48, 48, 8, 8);
         if (this.tileImage != null) {
            int sx = i / 10 * wTileImg;
            int sy = i % 10 * wTileImg;
            if (sx + wTileImg <= this.tileImage.getWidth() && sy + wTileImg <= this.tileImage.getHeight()) {
               g2.drawImage(this.tileImage, x + 2, y + 2, x + 48 - 2, y + 48 - 2, sx, sy, sx + wTileImg, sy + wTileImg, null);
            }
         }

         if (i == this.selectedTileId) {
            g2.setColor(Theme.ACCENT);
            g2.setStroke(new BasicStroke(2.0F));
            g2.drawRoundRect(x, y, 48, 48, 8, 8);
            g2.setColor(Theme.ACCENT_SOFT);
            g2.fillRoundRect(x, y, 48, 48, 8, 8);
         } else {
            g2.setColor(Theme.BORDER);
            g2.setStroke(new BasicStroke(1.0F));
            g2.drawRoundRect(x, y, 48, 48, 8, 8);
         }
      }
   }

   public interface TileSelectionListener {
      void onTileSelected(int var1);
   }
}
