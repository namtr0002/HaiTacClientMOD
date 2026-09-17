package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.Theme;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GameMapCanvas extends JPanel {
   private GameMap map;
   private int viewZoom = 100;
   private final int W_TILE_DRAW = 24;
   private BufferedImage imgTile;
   private BufferedImage imgTileWater;
   private BufferedImage imgBack;
   private HashMap<Integer, BufferedImage> itemImageCache = new HashMap<>();
   private List<ItemMapEntity> cachedLayer3Items = new ArrayList<>();
   private Runnable onMapChanged;
   private GameMapCanvas.ToolMode currentTool = GameMapCanvas.ToolMode.TILE_PENCIL;
   private int selectedTileId = 1;
   private short selectedTemplateId = 1;
   private int selectedItemLayer = 0;
   private short targetVgoId = -1;
   private Point targetVgoPos = new Point(100, 100);
   private ItemMapEntity previewItem;
   private GameMap.Npc draggingNpc = null;
   private GameMap.Mob draggingMob = null;
   private GameMap.Vgo draggingVgo = null;
   private Point dragOffset = new Point();
   private boolean[] layerVisible = new boolean[8];
   private boolean showGrid = false;
   private boolean showCollision = false;
   private boolean showNPC = true;
   private boolean showMob = true;
   private boolean showVGO = true;
   private int gameTick = 0;
   private Timer animTimer;
   private static final int RESIZE_HANDLE_PX = 10;
   private GameMapCanvas.ResizeDir resizeDragging = GameMapCanvas.ResizeDir.NONE;
   private int resizeDragStartX;
   private int resizeDragStartY;
   private int resizeOrigW;
   private int resizeOrigH;
   private Runnable onMapResized;
   private static final Color C_NPC_BG = new Color(255, 200, 0, 100);
   private static final Color C_NPC_FG = new Color(255, 200, 0);
   private static final Color C_MOB_BG = new Color(255, 50, 50, 100);
   private static final Color C_MOB_FG = new Color(255, 50, 50);
   private static final Color C_COL_BG = new Color(255, 0, 0, 80);
   private static final Color C_GRID = new Color(255, 255, 255, 30);
   private static final Font[] CACHED_FONTS = new Font[]{
      null, new Font("SansSerif", 1, 10), new Font("SansSerif", 1, 20), new Font("SansSerif", 1, 30), new Font("SansSerif", 1, 40)
   };
   private static final int W_TILE_LOGICAL = 24;
   private boolean isPaintingStroke = false;
   private Runnable onToolChanged;
   private final Stack<GameMapCanvas.MapState> undoStack = new Stack<>();
   private final Stack<GameMapCanvas.MapState> redoStack = new Stack<>();
   private static final int MAX_STACK_SIZE = 50;

   public void setOnMapResized(Runnable r) {
      this.onMapResized = r;
   }

   public int getSelectedTileId() {
      return this.selectedTileId;
   }

   public int getSelectedIconId() {
      return this.selectedTemplateId;
   }

   public short getSelectedTemplateId() {
      return this.selectedTemplateId;
   }

   public int getSelectedItemLayer() {
      return this.selectedItemLayer;
   }

   public void setZoom(int zoom) {
      this.viewZoom = Math.max(50, Math.min(zoom * 100, 400));
      this.revalidate();
      this.repaint();
   }

   public void setShowObjects(boolean show) {
      for (int i = 2; i < 8; i++) {
         this.layerVisible[i] = show;
      }

      this.repaint();
   }

   public void setShowLayer(int l, boolean v) {
      if (l >= 0 && l < 8) {
         this.layerVisible[l] = v;
      }

      this.repaint();
   }

   public void setSelectedIconId(int id) {
      this.selectedTemplateId = (short)id;
   }

   public void setSelectedItemLayer(int l) {
      this.selectedItemLayer = l;
   }

   public GameMapCanvas() {
      this.setBackground(new Color(15, 15, 20));

      for (int i = 0; i < 8; i++) {
         this.layerVisible[i] = true;
      }

      this.setFocusable(true);
      this.setupKeyBindings();
      this.setupInteraction();
      this.animTimer = new Timer(150, e -> {
         if (this.isShowing()) {
            this.gameTick++;
            if (this.layerVisible[1] && this.imgTileWater != null && this.map != null) {
               this.repaint();
            }
         }
      });
      this.animTimer.start();
   }

   public void setOnMapChanged(Runnable r) {
      this.onMapChanged = r;
   }

   public void setMap(GameMap map) {
      this.map = map;
      if (map != null) {
         if (map.mapPaint == null || map.mapPaint.length == 0) {
            String bin1 = AppConfig.getPath("Data/Map/ServerData/binary/");
            String bin2 = AppConfig.getPath("Data/Map/binary/");
            if (!SQLMapLoader.loadMapFromBinaryFiles(map, bin1)) {
               SQLMapLoader.loadMapFromBinaryFiles(map, bin2);
            }
         }

         map.ensureBuffers();

         if (map.items != null) {
            for (ItemMapEntity entity : map.items) {
               if (entity.template == null) {
                  entity.template = TemplateManager.gI().getItemTemplate(entity.templateId);
               }
            }
         }
      }

      this.loadTileImages();
      this.loadItemImages();
      this.updateItemCache();
      this.viewZoom = 100;
      this.revalidate();
      this.repaint();
   }

   public void updateItemCache() {
      if (this.map != null && this.map.items != null) {
         this.cachedLayer3Items.clear();

         for (ItemMapEntity entity : this.map.items) {
            if (entity.template == null) {
               entity.template = TemplateManager.gI().getItemTemplate(entity.templateId);
            }
            if (entity.template != null && entity.template.layer == 3) {
               this.cachedLayer3Items.add(entity);
            }
         }

         this.cachedLayer3Items.sort((a, b) -> Integer.compare(a.tileY, b.tileY));
      }
   }

   public void loadTileImages() {
      if (this.map != null) {
         this.imgTile = this.loadImageFromServer(23020 + this.map.getTileSetId());
         this.imgTileWater = this.loadImageFromServer(23070 + this.map.getTileSetId());
         this.imgBack = this.loadImageFromServer(887 + this.map.IDBack);
      }
   }

   public void loadItemImages() {
      if (this.map != null && this.map.items != null) {
         this.itemImageCache.clear();

         for (ItemMapEntity item : this.map.items) {
            if (item.template == null) {
               item.template = TemplateManager.gI().getItemTemplate(item.templateId);
            }
            if (item.template != null) {
               int id = item.template.idImage;
               if (!this.itemImageCache.containsKey(id)) {
                  BufferedImage img = ImageCache.getServerImage(id);
                  if (img != null) {
                     this.itemImageCache.put(id, img);
                  }
               }
            }
         }
      }
   }

   private BufferedImage loadImageFromServer(int imageId) {
      return ImageCache.getServerImage(imageId);
   }

   @Override
   public Dimension getPreferredSize() {
      if (this.map == null) {
         return new Dimension(800, 600);
      } else {
         int w = (int)(this.map.width * 24 * (this.viewZoom / 100.0));
         int h = (int)(this.map.height * 24 * (this.viewZoom / 100.0));
         return new Dimension(w, h);
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (this.map != null) {
         Graphics2D g2d = (Graphics2D)g;
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
         g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
         Rectangle clip;
         if (this.getParent() instanceof JViewport) {
            clip = ((JViewport)this.getParent()).getViewRect();
         } else {
            clip = g.getClipBounds();
            if (clip == null) {
               clip = new Rectangle(0, 0, Math.max(1, this.getWidth()), Math.max(1, this.getHeight()));
            }
         }

         this.paintBackground(g2d, clip);
         if (this.layerVisible[2]) {
            this.paintItemsByLayer(g2d, -1, clip);
         }

         if (this.layerVisible[0]) {
            this.paintTiles(g2d, clip);
         }

         if (this.layerVisible[1]) {
            this.paintWater(g2d, clip);
         }

         if (this.layerVisible[3]) {
            this.paintItemsByLayer(g2d, 0, clip);
         }

         if (this.layerVisible[4]) {
            this.paintItemsByLayer(g2d, 1, clip);
         }

         if (this.layerVisible[5]) {
            this.paintItemsByLayer(g2d, 2, clip);
         }

         if (this.layerVisible[6]) {
            this.paintLayer3Sorted(g2d, clip);
         }

         if (this.layerVisible[7]) {
            this.paintItemsByLayer(g2d, 4, clip);
            this.paintItemsByLayer(g2d, 5, clip);
         }

         if (this.showNPC) {
            this.paintNPCs(g2d, clip);
         }

         if (this.showMob) {
            this.paintMobs(g2d, clip);
         }

         if (this.showVGO) {
            this.paintVGOs(g2d, clip);
         }

         if (this.showCollision) {
            this.paintCollision(g2d, clip);
         }

         if (this.showGrid) {
            this.paintGrid(g2d, clip);
         }

         this.paintHover(g2d);
      }
   }

   private int getAssetZoom() {
      if (this.imgTile != null) {
         int w = this.imgTile.getWidth();
         int h = this.imgTile.getHeight();
         if (w % 96 == 0 && h % 96 == 0) {
            return 4;
         }

         if (w % 72 == 0 && h % 72 == 0) {
            return 3;
         }

         if (w % 48 == 0 && h % 48 == 0) {
            return 2;
         }

         if (w % 24 == 0 && h % 24 == 0) {
            return 1;
         }

         for (int z = 4; z >= 1; z--) {
            if (h % (z * 24) == 0) {
               return z;
            }
         }
      }

      return 4;
   }

   private void paintTiles(Graphics2D g, Rectangle clip) {
      if (this.imgTile != null && this.map != null && this.map.mapPaint != null) {
         int fWater = TileMapConfig.getfWater(this.map.getTileSetId());
         int fStand = TileMapConfig.getfStand(this.map.getTileSetId());
         double zoom = this.viewZoom / 100.0;
         int wTileScaled = (int)(24.0 * zoom);
         int physicalTileW = this.getAssetZoom() * 24;
         int startX = Math.max(0, clip.x / wTileScaled);
         int startY = Math.max(0, clip.y / wTileScaled);
         int endX = Math.min(this.map.width, (clip.x + clip.width) / wTileScaled + 1);
         int endY = Math.min(this.map.height, (clip.y + clip.height) / wTileScaled + 1);

         for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
               int idx = y * this.map.width + x;
               if (idx >= this.map.mapPaint.length) {
                  break;
               }

               int id = this.map.mapPaint[idx] & 0xFF;
               if (id > 0) {
                  boolean isWater = id >= fWater && id < fStand;
                  if (!isWater) {
                     this.drawTile(g, this.imgTile, id - 1, x, y, physicalTileW, zoom);
                  }
               }
            }
         }
      }
   }

   private void paintWater(Graphics2D g, Rectangle clip) {
      if (this.imgTileWater != null && this.map != null && this.map.mapPaint != null) {
         int fWater = TileMapConfig.getfWater(this.map.getTileSetId());
         int fStand = TileMapConfig.getfStand(this.map.getTileSetId());
         int waterSize = fStand - fWater;
         if (waterSize > 0) {
            int frame = this.gameTick / 4 % 2;
            double zoom = this.viewZoom / 100.0;
            int wTileScaled = (int)(24.0 * zoom);
            int physicalTileW = this.getAssetZoom() * 24;
            int startX = Math.max(0, clip.x / wTileScaled);
            int startY = Math.max(0, clip.y / wTileScaled);
            int endX = Math.min(this.map.width, (clip.x + clip.width) / wTileScaled + 1);
            int endY = Math.min(this.map.height, (clip.y + clip.height) / wTileScaled + 1);

            for (int y = startY; y < endY; y++) {
               for (int x = startX; x < endX; x++) {
                  int idx = y * this.map.width + x;
                  if (idx >= this.map.mapPaint.length) {
                     break;
                  }

                  int id = this.map.mapPaint[idx] & 0xFF;
                  if (id > 0) {
                     boolean isWater = id >= fWater && id < fStand;
                     if (isWater) {
                        int frameOffset = frame == 1 ? waterSize : 0;
                        int waterIdx = id - fWater + frameOffset;
                        this.drawWaterTile(g, this.imgTileWater, waterIdx, x, y, physicalTileW, zoom);
                     }
                  }
               }
            }
         }
      }
   }

   private void drawTile(Graphics2D g, BufferedImage img, int idx, int tx, int ty, int physicalTileW, double zoom) {
      if (img != null && idx >= 0 && physicalTileW > 0) {
         int srcX = idx / 10 * physicalTileW;
         int srcY = idx % 10 * physicalTileW;
         if (srcX + physicalTileW <= img.getWidth() && srcY + physicalTileW <= img.getHeight()) {
            g.drawImage(
               img,
               (int)(tx * 24 * zoom),
               (int)(ty * 24 * zoom),
               (int)((tx + 1) * 24 * zoom),
               (int)((ty + 1) * 24 * zoom),
               srcX,
               srcY,
               srcX + physicalTileW,
               srcY + physicalTileW,
               null
            );
         }
      }
   }

   private void drawWaterTile(Graphics2D g, BufferedImage img, int idx, int tx, int ty, int physicalTileW, double zoom) {
      if (img != null && idx >= 0 && physicalTileW > 0) {
         int srcX = idx / 10 * physicalTileW;
         int srcY = idx % 10 * physicalTileW;
         if (srcX + physicalTileW <= img.getWidth() && srcY + physicalTileW <= img.getHeight()) {
            g.drawImage(
               img,
               (int)(tx * 24 * zoom),
               (int)(ty * 24 * zoom),
               (int)((tx + 1) * 24 * zoom),
               (int)((ty + 1) * 24 * zoom),
               srcX,
               srcY,
               srcX + physicalTileW,
               srcY + physicalTileW,
               null
            );
         }
      }
   }

   private void paintItemsByLayer(Graphics2D g, int layer, Rectangle clip) {
      if (this.map != null && this.map.items != null) {
         for (ItemMapEntity item : this.map.items) {
            if (item.template == null) {
               item.template = TemplateManager.gI().getItemTemplate(item.templateId);
            }
            if (item.template != null && item.template.layer == layer && this.isInsideClip(item, clip)) {
               this.drawItem(g, item);
            }
         }
      }
   }

   private void paintLayer3Sorted(Graphics2D g, Rectangle clip) {
      for (ItemMapEntity item : this.cachedLayer3Items) {
         if (this.isInsideClip(item, clip)) {
            this.drawItem(g, item);
         }
      }
   }

   private boolean isInsideClip(ItemMapEntity item, Rectangle clip) {
      double zoom = this.viewZoom / 100.0;
      int px = (int)(item.tileX * 24 * zoom);
      int py = (int)(item.tileY * 24 * zoom);
      int pad = (int)(120.0 * zoom);
      return px >= clip.x - pad && px <= clip.x + clip.width + pad && py >= clip.y - pad && py <= clip.y + clip.height + pad;
   }

   private void drawItem(Graphics2D g, ItemMapEntity item) {
      if (item == null) {
         return;
      }
      if (item.template == null) {
         item.template = TemplateManager.gI().getItemTemplate(item.templateId);
      }
      if (item.template == null) {
         return;
      }
      BufferedImage img = this.itemImageCache.get(Integer.valueOf(item.template.idImage));
      if (img == null) {
         img = ImageCache.getServerImage(item.template.idImage);
         if (img != null) {
            this.itemImageCache.put(Integer.valueOf(item.template.idImage), img);
         }
      }
      if (img != null) {
         double zoom = this.viewZoom / 100.0;
         int px = (int)((item.tileX * 24 + item.template.dx) * zoom);
         int py = (int)((item.tileY * 24 + item.template.dy) * zoom);
         int assetZoom = this.getAssetZoom();
         int dw = (int)Math.round((double)img.getWidth() / assetZoom * zoom);
         int dh = (int)Math.round((double)img.getHeight() / assetZoom * zoom);
         g.drawImage(img, px, py, dw, dh, null);
      }
   }

   private void paintNPCs(Graphics2D g, Rectangle clip) {
      if (this.map != null && this.map.npcs != null) {
         double zoom = this.viewZoom / 100.0;
         g.setFont(Theme.F_TINY);

         for (GameMap.Npc npc : this.map.npcs) {
            int dx = (int)(npc.x * zoom);
            int dy = (int)(npc.y * zoom);
            if (dx >= clip.x - 100 && dx <= clip.x + clip.width + 100 && dy >= clip.y - 100 && dy <= clip.y + clip.height + 100) {
               int size = (int)(24.0 * zoom);
               boolean isDragging = npc == this.draggingNpc;
               g.setColor(isDragging ? new Color(255, 255, 255, 80) : C_NPC_BG);
               g.fillRect(dx - size / 2, dy - size, size, size);
               g.setColor(isDragging ? Theme.ACCENT : C_NPC_FG);
               g.setStroke(new BasicStroke(isDragging ? 2.0F : 1.0F));
               g.drawRect(dx - size / 2, dy - size, size, size);
               g.setColor(Color.WHITE);
               String label = "[" + npc.iditem + "] " + (npc.name != null ? npc.name : "NPC");
               g.drawString(label, dx - size / 2 + 4, dy - size - 4);
            }
         }

         g.setStroke(new BasicStroke(1.0F));
      }
   }

   private void paintMobs(Graphics2D g, Rectangle clip) {
      if (this.map != null && this.map.list_mob != null) {
         double zoom = this.viewZoom / 100.0;

         for (GameMap.Mob mob : this.map.list_mob) {
            int dx = (int)(mob.x * zoom);
            int dy = (int)(mob.y * zoom);
            if (dx >= clip.x - 100 && dx <= clip.x + clip.width + 100 && dy >= clip.y - 100 && dy <= clip.y + clip.height + 100) {
               int size = (int)(20.0 * zoom);
               boolean isDragging = mob == this.draggingMob;
               g.setColor(isDragging ? new Color(255, 255, 255, 80) : C_MOB_BG);
               g.fillRect(dx - size / 2, dy - size, size, size);
               g.setColor(isDragging ? Theme.ACCENT : C_MOB_FG);
               g.setStroke(new BasicStroke(isDragging ? 2.0F : 1.0F));
               g.drawRect(dx - size / 2, dy - size, size, size);
               g.setColor(Color.WHITE);
               g.drawString("MOB " + mob.templateId, dx - size / 2 + 4, dy - size - 4);
            }
         }
      }
   }

   private void paintVGOs(Graphics2D g, Rectangle clip) {
      if (this.map != null && this.map.vgos != null) {
         double zoom = this.viewZoom / 100.0;

         for (GameMap.Vgo vgo : this.map.vgos) {
            int dx = (int)(vgo.xold * zoom);
            int dy = (int)(vgo.yold * zoom);
            if (dx >= clip.x - 100 && dx <= clip.x + clip.width + 100 && dy >= clip.y - 100 && dy <= clip.y + clip.height + 100) {
               int size = (int)(24.0 * zoom);
               boolean isDragging = vgo == this.draggingVgo;
               g.setColor(isDragging ? Theme.ACCENT : Color.YELLOW);
               g.setStroke(new BasicStroke(2.0F));
               g.drawOval(dx - size / 2, dy - size / 2, size, size);
               int tx = (int)(vgo.xnew * zoom);
               int ty = (int)(vgo.ynew * zoom);
               g.drawLine(
                  dx,
                  dy,
                  dx + (int)(20.0 * zoom * Math.signum((float)(vgo.xnew - vgo.xold))),
                  dy + (int)(20.0 * zoom * Math.signum((float)(vgo.ynew - vgo.yold)))
               );
               g.setColor(Color.WHITE);
               g.setFont(Theme.F_TINY);
               g.drawString("VGO TO " + vgo.id_map_go, dx - size / 2, dy - size / 2 - 5);
            }
         }
      }
   }

   private void paintCollision(Graphics2D g, Rectangle clip) {
      if (this.map != null && this.map.mapType != null) {
         double zoom = this.viewZoom / 100.0;
         int wTileScaled = (int)(24.0 * zoom);
         g.setColor(C_COL_BG);
         int startX = Math.max(0, clip.x / wTileScaled);
         int startY = Math.max(0, clip.y / wTileScaled);
         int endX = Math.min(this.map.width, (clip.x + clip.width) / wTileScaled + 1);
         int endY = Math.min(this.map.height, (clip.y + clip.height) / wTileScaled + 1);

         for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
               int idx = y * this.map.width + x;
               if (idx >= this.map.mapType.length) {
                  break;
               }

               if (this.map.mapType[idx] == 1) {
                  g.fillRect((int)(x * 24 * zoom), (int)(y * 24 * zoom), wTileScaled, wTileScaled);
               }
            }
         }
      }
   }

   private void paintGrid(Graphics2D g, Rectangle clip) {
      double zoom = this.viewZoom / 100.0;
      int wTileScaled = (int)(24.0 * zoom);
      g.setColor(C_GRID);
      int startX = Math.max(0, clip.x / wTileScaled);
      int startY = Math.max(0, clip.y / wTileScaled);
      int endX = Math.min(this.map.width, (clip.x + clip.width) / wTileScaled + 1);
      int endY = Math.min(this.map.height, (clip.y + clip.height) / wTileScaled + 1);

      for (int x = startX; x <= endX; x++) {
         g.drawLine((int)(x * 24 * zoom), (int)(startY * 24 * zoom), (int)(x * 24 * zoom), (int)(endY * 24 * zoom));
      }

      for (int y = startY; y <= endY; y++) {
         g.drawLine((int)(startX * 24 * zoom), (int)(y * 24 * zoom), (int)(endX * 24 * zoom), (int)(y * 24 * zoom));
      }

      int mapPxW = (int)(this.map.width * 24 * zoom);
      int mapPxH = (int)(this.map.height * 24 * zoom);
      int hs = 20;
      g.setColor(new Color(58879));
      g.fillRect(mapPxW - hs / 2, mapPxH / 2 - hs / 2, hs, hs);
      g.setColor(new Color(0, 0, 0, 120));
      g.drawRect(mapPxW - hs / 2, mapPxH / 2 - hs / 2, hs, hs);
      g.setColor(new Color(58879));
      g.fillRect(mapPxW / 2 - hs / 2, mapPxH - hs / 2, hs, hs);
      g.setColor(new Color(0, 0, 0, 120));
      g.drawRect(mapPxW / 2 - hs / 2, mapPxH - hs / 2, hs, hs);
      g.setColor(new Color(16739584));
      g.fillRect(mapPxW - hs / 2, mapPxH - hs / 2, hs, hs);
      g.setColor(new Color(0, 0, 0, 120));
      g.drawRect(mapPxW - hs / 2, mapPxH - hs / 2, hs, hs);
      g.setColor(new Color(16739584));
      g.setFont(new Font("Segoe UI", 1, 11));
      g.drawString(this.map.width + " × " + this.map.height, mapPxW + 4, mapPxH + 14);
      if (this.resizeDragging != GameMapCanvas.ResizeDir.NONE) {
         g.setColor(new Color(16739584, true));
         g.setStroke(new BasicStroke(2.0F, 0, 0, 1.0F, new float[]{6.0F, 4.0F}, 0.0F));
         g.drawRect(0, 0, mapPxW - 1, mapPxH - 1);
         g.setStroke(new BasicStroke(1.0F));
      }
   }

   private void paintHover(Graphics2D g) {
      if (this.previewItem != null && this.currentTool == GameMapCanvas.ToolMode.ITEM_PENCIL) {
         g.setComposite(AlphaComposite.getInstance(3, 0.5F));
         this.drawItem(g, this.previewItem);
         g.setComposite(AlphaComposite.getInstance(3, 1.0F));
      }
   }

   private void setupInteraction() {
      MouseAdapter ma = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            if (GameMapCanvas.this.map != null) {
               GameMapCanvas.this.requestFocusInWindow();
               if (SwingUtilities.isLeftMouseButton(e)) {
                  if (GameMapCanvas.this.showGrid && GameMapCanvas.this.map != null) {
                     GameMapCanvas.ResizeDir hit = GameMapCanvas.this.hitTestResizeHandle(e.getX(), e.getY());
                     if (hit != GameMapCanvas.ResizeDir.NONE) {
                        GameMapCanvas.this.saveState();
                        GameMapCanvas.this.resizeDragging = hit;
                        GameMapCanvas.this.resizeDragStartX = e.getX();
                        GameMapCanvas.this.resizeDragStartY = e.getY();
                        GameMapCanvas.this.resizeOrigW = GameMapCanvas.this.map.width;
                        GameMapCanvas.this.resizeOrigH = GameMapCanvas.this.map.height;
                        return;
                     }
                  }

                  double zoom = GameMapCanvas.this.viewZoom / 100.0;
                  int mx = (int)(e.getX() / zoom);
                  int my = (int)(e.getY() / zoom);
                  if (GameMapCanvas.this.showNPC) {
                     for (GameMap.Npc npc : GameMapCanvas.this.map.npcs) {
                        if (Math.abs(npc.x - mx) < 16 && Math.abs(npc.y - 12 - my) < 16) {
                           GameMapCanvas.this.saveState();
                           GameMapCanvas.this.draggingNpc = npc;
                           GameMapCanvas.this.dragOffset.setLocation(npc.x - mx, npc.y - my);
                           return;
                        }
                     }
                  }

                  if (GameMapCanvas.this.showMob) {
                     for (GameMap.Mob mob : GameMapCanvas.this.map.list_mob) {
                        if (Math.abs(mob.x - mx) < 12 && Math.abs(mob.y - 10 - my) < 12) {
                           GameMapCanvas.this.saveState();
                           GameMapCanvas.this.draggingMob = mob;
                           GameMapCanvas.this.dragOffset.setLocation(mob.x - mx, mob.y - my);
                           return;
                        }
                     }
                  }

                  if (GameMapCanvas.this.showVGO) {
                     for (GameMap.Vgo vgo : GameMapCanvas.this.map.vgos) {
                        if (Math.abs(vgo.xold - mx) < 12 && Math.abs(vgo.yold - my) < 12) {
                           GameMapCanvas.this.saveState();
                           GameMapCanvas.this.draggingVgo = vgo;
                           GameMapCanvas.this.dragOffset.setLocation(vgo.xold - mx, vgo.yold - my);
                           return;
                        }
                     }
                  }
               }

               GameMapCanvas.this.saveState();
               GameMapCanvas.this.isPaintingStroke = true;
               GameMapCanvas.this.handleAction(e);
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            if (GameMapCanvas.this.resizeDragging != GameMapCanvas.ResizeDir.NONE && GameMapCanvas.this.map != null) {
               int finalW = GameMapCanvas.this.map.width;
               int finalH = GameMapCanvas.this.map.height;
               GameMapCanvas.this.resizeDragging = GameMapCanvas.ResizeDir.NONE;
               GameMapCanvas.this.revalidate();
               GameMapCanvas.this.repaint();
               if (GameMapCanvas.this.onMapResized != null) {
                  GameMapCanvas.this.onMapResized.run();
               }

               if (GameMapCanvas.this.onMapChanged != null) {
                  GameMapCanvas.this.onMapChanged.run();
               }

               GameMapCanvas.this.setCursor(Cursor.getDefaultCursor());
            } else {
               GameMapCanvas.this.draggingNpc = null;
               GameMapCanvas.this.draggingMob = null;
               GameMapCanvas.this.draggingVgo = null;
               GameMapCanvas.this.isPaintingStroke = false;
               if (GameMapCanvas.this.onMapChanged != null) {
                  GameMapCanvas.this.onMapChanged.run();
               }

               GameMapCanvas.this.repaint();
            }
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            if (GameMapCanvas.this.resizeDragging != GameMapCanvas.ResizeDir.NONE && GameMapCanvas.this.map != null) {
               double zoom = GameMapCanvas.this.viewZoom / 100.0;
               int dx = e.getX() - GameMapCanvas.this.resizeDragStartX;
               int dy = e.getY() - GameMapCanvas.this.resizeDragStartY;
               int tileDx = (int)Math.round(dx / (24.0 * zoom));
               int tileDy = (int)Math.round(dy / (24.0 * zoom));
               int newW = GameMapCanvas.this.resizeOrigW;
               int newH = GameMapCanvas.this.resizeOrigH;
               if (GameMapCanvas.this.resizeDragging == GameMapCanvas.ResizeDir.RIGHT || GameMapCanvas.this.resizeDragging == GameMapCanvas.ResizeDir.CORNER) {
                  newW = Math.max(5, Math.min(127, GameMapCanvas.this.resizeOrigW + tileDx));
               }

               if (GameMapCanvas.this.resizeDragging == GameMapCanvas.ResizeDir.BOTTOM || GameMapCanvas.this.resizeDragging == GameMapCanvas.ResizeDir.CORNER) {
                  newH = Math.max(5, Math.min(127, GameMapCanvas.this.resizeOrigH + tileDy));
               }

               if (newW != GameMapCanvas.this.map.width || newH != GameMapCanvas.this.map.height) {
                  GameMapCanvas.this.map.resize(newW, newH);
                  GameMapCanvas.this.revalidate();
                  GameMapCanvas.this.repaint();
               }
            } else {
               double zoomx = GameMapCanvas.this.viewZoom / 100.0;
               if (GameMapCanvas.this.draggingNpc != null) {
                  GameMapCanvas.this.draggingNpc.x = (short)(e.getX() / zoomx + GameMapCanvas.this.dragOffset.x);
                  GameMapCanvas.this.draggingNpc.y = (short)(e.getY() / zoomx + GameMapCanvas.this.dragOffset.y);
                  GameMapCanvas.this.repaint();
               } else if (GameMapCanvas.this.draggingMob != null) {
                  GameMapCanvas.this.draggingMob.x = (short)(e.getX() / zoomx + GameMapCanvas.this.dragOffset.x);
                  GameMapCanvas.this.draggingMob.y = (short)(e.getY() / zoomx + GameMapCanvas.this.dragOffset.y);
                  GameMapCanvas.this.repaint();
               } else if (GameMapCanvas.this.draggingVgo != null) {
                  GameMapCanvas.this.draggingVgo.xold = (short)(e.getX() / zoomx + GameMapCanvas.this.dragOffset.x);
                  GameMapCanvas.this.draggingVgo.yold = (short)(e.getY() / zoomx + GameMapCanvas.this.dragOffset.y);
                  GameMapCanvas.this.repaint();
               } else if (GameMapCanvas.this.isPaintingStroke) {
                  GameMapCanvas.this.handleAction(e);
               }
            }
         }

         @Override
         public void mouseMoved(MouseEvent e) {
            if (GameMapCanvas.this.showGrid && GameMapCanvas.this.map != null) {
               GameMapCanvas.ResizeDir hit = GameMapCanvas.this.hitTestResizeHandle(e.getX(), e.getY());
               if (hit == GameMapCanvas.ResizeDir.CORNER) {
                  GameMapCanvas.this.setCursor(Cursor.getPredefinedCursor(5));
                  GameMapCanvas.this.previewItem = null;
                  return;
               }

               if (hit == GameMapCanvas.ResizeDir.RIGHT) {
                  GameMapCanvas.this.setCursor(Cursor.getPredefinedCursor(11));
                  GameMapCanvas.this.previewItem = null;
                  return;
               }

               if (hit == GameMapCanvas.ResizeDir.BOTTOM) {
                  GameMapCanvas.this.setCursor(Cursor.getPredefinedCursor(9));
                  GameMapCanvas.this.previewItem = null;
                  return;
               }
            }

            GameMapCanvas.this.setCursor(Cursor.getDefaultCursor());
            double zoom = GameMapCanvas.this.viewZoom / 100.0;
            int wTileScaled = (int)(24.0 * zoom);
            int tx = e.getX() / wTileScaled;
            int ty = e.getY() / wTileScaled;
            if (GameMapCanvas.this.currentTool == GameMapCanvas.ToolMode.ITEM_PENCIL) {
               GameMapCanvas.this.previewItem = new ItemMapEntity(GameMapCanvas.this.selectedTemplateId, (short)tx, (short)ty);
               GameMapCanvas.this.previewItem.template = TemplateManager.gI().getItemTemplate(GameMapCanvas.this.selectedTemplateId);
            } else {
               GameMapCanvas.this.previewItem = null;
            }

            GameMapCanvas.this.repaint();
         }
      };
      this.addMouseListener(ma);
      this.addMouseMotionListener(ma);
      this.addMouseWheelListener(e -> {
         if (e.isControlDown()) {
            int oldZoom = this.viewZoom;
            if (e.getPreciseWheelRotation() < 0.0) {
               this.viewZoom += 25;
            } else {
               this.viewZoom -= 25;
            }

            this.viewZoom = Math.max(50, Math.min(this.viewZoom, 400));
            if (oldZoom != this.viewZoom) {
               Point p = e.getPoint();
               if (this.getParent() instanceof JViewport) {
                  JViewport viewport = (JViewport)this.getParent();
                  Point viewPos = viewport.getViewPosition();
                  double ratio = (double)this.viewZoom / oldZoom;
                  int newX = (int)((viewPos.x + p.x) * ratio - p.x);
                  int newY = (int)((viewPos.y + p.y) * ratio - p.y);
                  this.revalidate();
                  this.repaint();
                  SwingUtilities.invokeLater(() -> viewport.setViewPosition(new Point(Math.max(0, newX), Math.max(0, newY))));
               } else {
                  this.revalidate();
                  this.repaint();
               }
            }
         } else if (this.getParent() != null) {
            this.getParent().dispatchEvent(e);
         }
      });
   }

   private void handleAction(MouseEvent e) {
      if (this.map != null) {
         double zoom = this.viewZoom / 100.0;
         int wTileScaled = (int)(24.0 * zoom);
         int tx = e.getX() / wTileScaled;
         int ty = e.getY() / wTileScaled;
         if (tx >= 0 && tx < this.map.width && ty >= 0 && ty < this.map.height) {
            if (SwingUtilities.isLeftMouseButton(e)) {
               switch (this.currentTool) {
                  case TILE_PENCIL:
                  case PENCIL:
                     this.map.setTileId(tx, ty, this.selectedTileId + 1);
                     break;
                  case TILE_ERASER:
                  case ERASER:
                     this.map.setTileId(tx, ty, 0);
                     break;
                  case TILE_BUCKET:
                     this.floodFillTile(tx, ty, this.selectedTileId + 1);
                     break;
                  case ITEM_PENCIL:
                  case ITEM_LAYER_0:
                  case ITEM_LAYER_1:
                  case ITEM_LAYER_2:
                     this.map.addItem(this.selectedTemplateId, (short)tx, (short)ty, this.selectedItemLayer);
                     this.loadItemImages();
                     this.updateItemCache();
                     break;
                  case ITEM_ERASER:
                     this.map.removeItem(tx, ty);
                     this.updateItemCache();
                     break;
                  case COLLISION_TOGGLE:
                     this.map.mapType[ty * this.map.width + tx] = 1;
                     break;
                  case NPC_PENCIL:
                     GameMap.Npc npc = new GameMap.Npc();
                     npc.iditem = this.selectedTemplateId;
                     npc.name = "New NPC";
                     npc.x = (short)(tx * 24 + 12);
                     npc.y = (short)(ty * 24 + 24);
                     this.map.npcs.add(npc);
                     break;
                  case MOB_PENCIL:
                     GameMap.Mob mob = new GameMap.Mob();
                     mob.templateId = this.selectedTemplateId;
                     mob.x = (short)(tx * 24 + 12);
                     mob.y = (short)(ty * 24 + 20);
                     this.map.list_mob.add(mob);
                     break;
                  case VGO_PENCIL:
                     GameMap.Vgo vgo = new GameMap.Vgo();
                     vgo.id_map_go = this.targetVgoId;
                     vgo.xold = (short)(tx * 24 + 12);
                     vgo.yold = (short)(ty * 24 + 20);
                     vgo.xnew = (short)this.targetVgoPos.x;
                     vgo.ynew = (short)this.targetVgoPos.y;
                     this.map.vgos.add(vgo);
               }
            } else if (SwingUtilities.isRightMouseButton(e)) {
               switch (this.currentTool) {
                  case TILE_PENCIL:
                  case TILE_ERASER:
                  case PENCIL:
                  case ERASER:
                     this.map.setTileId(tx, ty, 0);
                  case TILE_BUCKET:
                  default:
                     break;
                  case ITEM_PENCIL:
                  case ITEM_ERASER:
                  case ITEM_LAYER_0:
                  case ITEM_LAYER_1:
                  case ITEM_LAYER_2:
                     this.map.removeItem(tx, ty);
                     this.updateItemCache();
                     break;
                  case COLLISION_TOGGLE:
                     this.map.mapType[ty * this.map.width + tx] = 0;
                     break;
                  case NPC_PENCIL:
                  case MOB_PENCIL:
                  case VGO_PENCIL:
                     int mx = (int)(e.getX() / zoom);
                     int my = (int)(e.getY() / zoom);
                     if (this.showNPC) {
                        this.map.npcs.removeIf(n -> Math.abs(n.x - mx) < 16 && Math.abs(n.y - 12 - my) < 16);
                     }

                     if (this.showMob) {
                        this.map.list_mob.removeIf(m -> Math.abs(m.x - mx) < 12 && Math.abs(m.y - 10 - my) < 12);
                     }

                     if (this.showVGO) {
                        this.map.vgos.removeIf(v -> Math.abs(v.xold - mx) < 12 && Math.abs(v.yold - my) < 12);
                     }
               }
            }

            if (this.onMapChanged != null) {
               this.onMapChanged.run();
            }

            this.repaint();
         }
      }
   }

   public GameMap getMap() {
      return this.map;
   }

   public void setSelectedTile(int id) {
      this.selectedTileId = id;
   }

   public void setSelectedTemplateId(short id) {
      this.selectedTemplateId = id;
      this.previewItem = null;
   }

   public void setOnToolChanged(Runnable r) {
      this.onToolChanged = r;
   }

   public GameMapCanvas.ToolMode getTool() {
      return this.currentTool;
   }

   public void setTool(GameMapCanvas.ToolMode tool) {
      this.currentTool = tool;
      if (this.onToolChanged != null) {
         this.onToolChanged.run();
      }

      this.repaint();
   }

   public void setLayerVisible(int l, boolean v) {
      this.layerVisible[l] = v;
      this.repaint();
   }

   public void setShowGrid(boolean s) {
      this.showGrid = s;
      this.repaint();
   }

   public void setShowCollision(boolean s) {
      this.showCollision = s;
      this.repaint();
   }

   public void setShowNPC(boolean s) {
      this.showNPC = s;
      this.repaint();
   }

   public void setShowMob(boolean s) {
      this.showMob = s;
      this.repaint();
   }

   public void setShowVGO(boolean s) {
      this.showVGO = s;
      this.repaint();
   }

   public boolean isShowGrid() {
      return this.showGrid;
   }

   public boolean isShowCollision() {
      return this.showCollision;
   }

   public boolean isShowNPC() {
      return this.showNPC;
   }

   public boolean isShowMob() {
      return this.showMob;
   }

   public boolean isShowVGO() {
      return this.showVGO;
   }

   public void setTargetVgo(short id, short x, short y) {
      this.targetVgoId = id;
      this.targetVgoPos.setLocation((int)x, (int)y);
   }

   private void paintBackground(Graphics2D g, Rectangle clip) {
      if (this.imgBack != null) {
         int bw = this.imgBack.getWidth();

         for (int x = clip.x / bw * bw; x < clip.x + clip.width; x += bw) {
            g.drawImage(this.imgBack, x, 0, null);
         }
      } else if (this.map == null || this.map.IDBack != 1 && this.map.IDBack != 21) {
         g.setColor(this.getBackground());
         g.fillRect(clip.x, clip.y, clip.width, clip.height);
      } else {
         GradientPaint gp = new GradientPaint(0.0F, 0.0F, new Color(30, 80, 150), 0.0F, this.getHeight(), new Color(10, 30, 60));
         g.setPaint(gp);
         g.fillRect(clip.x, clip.y, clip.width, clip.height);
      }
   }

   public void saveState() {
      if (this.map != null) {
         if (this.undoStack.size() >= 50) {
            this.undoStack.remove(0);
         }

         this.undoStack.push(new GameMapCanvas.MapState(this.map));
         this.redoStack.clear();
      }
   }

   public void undo() {
      if (!this.undoStack.isEmpty() && this.map != null) {
         this.redoStack.push(new GameMapCanvas.MapState(this.map));
         GameMapCanvas.MapState state = this.undoStack.pop();
         state.restore(this.map);
         this.loadItemImages();
         this.updateItemCache();
         if (this.onMapChanged != null) {
            this.onMapChanged.run();
         }

         this.repaint();
      }
   }

   public void redo() {
      if (!this.redoStack.isEmpty() && this.map != null) {
         this.undoStack.push(new GameMapCanvas.MapState(this.map));
         GameMapCanvas.MapState state = this.redoStack.pop();
         state.restore(this.map);
         this.loadItemImages();
         this.updateItemCache();
         if (this.onMapChanged != null) {
            this.onMapChanged.run();
         }

         this.repaint();
      }
   }

   private void setupKeyBindings() {
      this.getInputMap(2).put(KeyStroke.getKeyStroke(90, 128), "Undo");
      this.getActionMap().put("Undo", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            GameMapCanvas.this.undo();
         }
      });
      this.getInputMap(2).put(KeyStroke.getKeyStroke(89, 128), "Redo");
      this.getActionMap().put("Redo", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            GameMapCanvas.this.redo();
         }
      });
      GameMapCanvas.ToolMode[] modes = new GameMapCanvas.ToolMode[]{
         GameMapCanvas.ToolMode.TILE_PENCIL,
         GameMapCanvas.ToolMode.TILE_ERASER,
         GameMapCanvas.ToolMode.TILE_BUCKET,
         GameMapCanvas.ToolMode.COLLISION_TOGGLE,
         GameMapCanvas.ToolMode.NPC_PENCIL,
         GameMapCanvas.ToolMode.MOB_PENCIL,
         GameMapCanvas.ToolMode.VGO_PENCIL
      };
      int[] keys = new int[]{49, 50, 51, 52, 53, 54, 55};

      for (int i = 0; i < keys.length; i++) {
         final GameMapCanvas.ToolMode mode = modes[i];
         String name = "SetTool_" + mode.name();
         this.getInputMap(2).put(KeyStroke.getKeyStroke(keys[i], 0), name);
         this.getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               GameMapCanvas.this.setTool(mode);
            }
         });
      }
   }

   private GameMapCanvas.ResizeDir hitTestResizeHandle(int mouseX, int mouseY) {
      if (this.map == null) {
         return GameMapCanvas.ResizeDir.NONE;
      } else {
         double zoom = this.viewZoom / 100.0;
         int mapPxW = (int)(this.map.width * 24 * zoom);
         int mapPxH = (int)(this.map.height * 24 * zoom);
         int r = 10;
         if (Math.abs(mouseX - mapPxW) <= r && Math.abs(mouseY - mapPxH) <= r) {
            return GameMapCanvas.ResizeDir.CORNER;
         } else if (Math.abs(mouseX - mapPxW) <= r && Math.abs(mouseY - mapPxH / 2) <= r) {
            return GameMapCanvas.ResizeDir.RIGHT;
         } else {
            return Math.abs(mouseX - mapPxW / 2) <= r && Math.abs(mouseY - mapPxH) <= r ? GameMapCanvas.ResizeDir.BOTTOM : GameMapCanvas.ResizeDir.NONE;
         }
      }
   }

   private void floodFillTile(int startX, int startY, int targetTileId) {
      if (this.map != null) {
         this.map.ensureBuffers();
         if (startX >= 0 && startX < this.map.width && startY >= 0 && startY < this.map.height) {
            int originalTileId = this.map.mapPaint[startY * this.map.width + startX];
            if (originalTileId != targetTileId) {
               Queue<Point> queue = new LinkedList<>();
               queue.add(new Point(startX, startY));

               while (!queue.isEmpty()) {
                  Point p = queue.poll();
                  int idx = p.y * this.map.width + p.x;
                  if (this.map.mapPaint[idx] == originalTileId) {
                     this.map.setTileId(p.x, p.y, targetTileId);
                     if (p.x > 0) {
                        queue.add(new Point(p.x - 1, p.y));
                     }

                     if (p.x < this.map.width - 1) {
                        queue.add(new Point(p.x + 1, p.y));
                     }

                     if (p.y > 0) {
                        queue.add(new Point(p.x, p.y - 1));
                     }

                     if (p.y < this.map.height - 1) {
                        queue.add(new Point(p.x, p.y + 1));
                     }
                  }
               }
            }
         }
      }
   }

   private static class MapState {
      public int[] mapPaint;
      public int[] mapType;
      public List<ItemMapEntity> items;
      public List<GameMap.Npc> npcs;
      public List<GameMap.Mob> list_mob;
      public List<GameMap.Vgo> vgos;
      public List<GameMap.Boat_In_Map> list_boat;

      public MapState(GameMap map) {
         if (map != null) {
            map.ensureBuffers();
            this.mapPaint = map.mapPaint != null ? (int[])map.mapPaint.clone() : new int[0];
            this.mapType = map.mapType != null ? (int[])map.mapType.clone() : new int[0];
         } else {
            this.mapPaint = new int[0];
            this.mapType = new int[0];
         }

         this.items = new ArrayList<>();
         if (map != null && map.items != null) {
            for (ItemMapEntity it : map.items) {
               ItemMapEntity cloned = new ItemMapEntity(it.templateId, it.tileX, it.tileY);
               cloned.template = it.template;
               this.items.add(cloned);
            }
         }

         this.npcs = new ArrayList<>();

         for (GameMap.Npc n : map.npcs) {
            GameMap.Npc cloned = new GameMap.Npc();
            cloned.iditem = n.iditem;
            cloned.name = n.name;
            cloned.namegt = n.namegt;
            cloned.chat = n.chat;
            cloned.x = n.x;
            cloned.y = n.y;
            cloned.isPerson = n.isPerson;
            cloned.typeIcon = n.typeIcon;
            cloned.wBlock = n.wBlock;
            cloned.hBlock = n.hBlock;
            cloned.b3 = n.b3;
            cloned.dataFrame = n.dataFrame != null ? (byte[])n.dataFrame.clone() : null;
            cloned.head = n.head;
            cloned.hair = n.hair;
            cloned.wearing = n.wearing != null ? (short[])n.wearing.clone() : null;
            this.npcs.add(cloned);
         }

         this.list_mob = new ArrayList<>();

         for (GameMap.Mob m : map.list_mob) {
            GameMap.Mob cloned = new GameMap.Mob();
            cloned.templateId = m.templateId;
            cloned.x = m.x;
            cloned.y = m.y;
            this.list_mob.add(cloned);
         }

         this.vgos = new ArrayList<>();

         for (GameMap.Vgo v : map.vgos) {
            GameMap.Vgo cloned = new GameMap.Vgo();
            cloned.id_map_go = v.id_map_go;
            cloned.xold = v.xold;
            cloned.yold = v.yold;
            cloned.xnew = v.xnew;
            cloned.ynew = v.ynew;
            this.vgos.add(cloned);
         }

         this.list_boat = new ArrayList<>();

         for (GameMap.Boat_In_Map b : map.list_boat) {
            GameMap.Boat_In_Map cloned = new GameMap.Boat_In_Map();
            cloned.x = b.x;
            cloned.y = b.y;
            this.list_boat.add(cloned);
         }
      }

      public void restore(GameMap map) {
         map.mapPaint = (int[])this.mapPaint.clone();
         map.mapType = (int[])this.mapType.clone();
         map.items.clear();

         for (ItemMapEntity it : this.items) {
            ItemMapEntity cloned = new ItemMapEntity(it.templateId, it.tileX, it.tileY);
            cloned.template = it.template;
            map.items.add(cloned);
         }

         map.npcs.clear();

         for (GameMap.Npc n : this.npcs) {
            GameMap.Npc cloned = new GameMap.Npc();
            cloned.iditem = n.iditem;
            cloned.name = n.name;
            cloned.namegt = n.namegt;
            cloned.chat = n.chat;
            cloned.x = n.x;
            cloned.y = n.y;
            cloned.isPerson = n.isPerson;
            cloned.typeIcon = n.typeIcon;
            cloned.wBlock = n.wBlock;
            cloned.hBlock = n.hBlock;
            cloned.b3 = n.b3;
            cloned.dataFrame = n.dataFrame != null ? (byte[])n.dataFrame.clone() : null;
            cloned.head = n.head;
            cloned.hair = n.hair;
            cloned.wearing = n.wearing != null ? (short[])n.wearing.clone() : null;
            map.npcs.add(cloned);
         }

         map.list_mob.clear();

         for (GameMap.Mob m : this.list_mob) {
            GameMap.Mob cloned = new GameMap.Mob();
            cloned.templateId = m.templateId;
            cloned.x = m.x;
            cloned.y = m.y;
            map.list_mob.add(cloned);
         }

         map.vgos.clear();

         for (GameMap.Vgo v : this.vgos) {
            GameMap.Vgo cloned = new GameMap.Vgo();
            cloned.id_map_go = v.id_map_go;
            cloned.xold = v.xold;
            cloned.yold = v.yold;
            cloned.xnew = v.xnew;
            cloned.ynew = v.ynew;
            map.vgos.add(cloned);
         }

         map.list_boat.clear();

         for (GameMap.Boat_In_Map b : this.list_boat) {
            GameMap.Boat_In_Map cloned = new GameMap.Boat_In_Map();
            cloned.x = b.x;
            cloned.y = b.y;
            map.list_boat.add(cloned);
         }
      }
   }

   private static enum ResizeDir {
      NONE,
      RIGHT,
      BOTTOM,
      CORNER;
   }

   public static enum ToolMode {
      TILE_PENCIL,
      TILE_ERASER,
      TILE_BUCKET,
      ITEM_PENCIL,
      ITEM_ERASER,
      COLLISION_TOGGLE,
      NPC_PENCIL,
      MOB_PENCIL,
      VGO_PENCIL,
      PENCIL,
      ERASER,
      ITEM_LAYER_0,
      ITEM_LAYER_1,
      ITEM_LAYER_2;
   }
}
