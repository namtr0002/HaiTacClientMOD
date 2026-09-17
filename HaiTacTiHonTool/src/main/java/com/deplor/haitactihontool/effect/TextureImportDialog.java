package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.ImageZoomHelper;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextureImportDialog extends JDialog {
   private static final Color BG = new Color(25, 25, 32);
   private static final Color PANEL = new Color(32, 32, 42);
   private static final Color BORDER = new Color(50, 50, 65);
   private static final Color ACCENT = new Color(0, 130, 255);
   private static final Color FG = new Color(230, 230, 240);
   private static final Color FG_DIM = new Color(150, 150, 170);
   private BufferedImage originalLoadedImage;
   private JComboBox<String> comboSourceZoom;
   private BufferedImage loadedImage;
   private final List<Rectangle> detectedRegions = new ArrayList<>();
   private int selectedRegionIdx = -1;
   private double canvasZoom = 1.0;
   private boolean isGridMode = false;
   private SmallImageDef[] result;
   private BufferedImage resultAtlas;
   private boolean confirmed = false;
   private TextureImportDialog.AtlasCanvas atlasCanvas;
   private JList<Rectangle> regionJList;
   private DefaultListModel<Rectangle> regionModel;
   private JLabel imgInfoLabel;
   private JSpinner spThreshold;
   private JSpinner spMinSize;
   private JSpinner spPadding;
   private JSpinner spCols;
   private JSpinner spRows;
   private JSpinner spGridCellW;
   private JSpinner spGridCellH;
   private JLabel statusLabel;
   private CardLayout configCards;
   private JPanel cardPanel;
   private JButton btnTabAlpha;
   private JButton btnTabGrid;

   public TextureImportDialog(Frame owner) {
      super(owner, "Pro Spritesheet Cutter", true);
      this.setUndecorated(true);
      this.setSize(1200, 800);
      this.setLocationRelativeTo(owner);
      this.buildUI();
   }

   private void buildUI() {
      JPanel contentPanelOuter = new JPanel(new BorderLayout());
      contentPanelOuter.setBackground(BG);
      contentPanelOuter.setBorder(BorderFactory.createLineBorder(BORDER, 2));
      this.setContentPane(contentPanelOuter);
      JPanel root = new JPanel(new BorderLayout(10, 10));
      root.setBackground(BG);
      root.setBorder(new EmptyBorder(10, 10, 10, 10));
      contentPanelOuter.add(root, "Center");
      JPanel topBar = new JPanel(new BorderLayout(10, 0));
      topBar.setOpaque(false);
      final Point[] initialClick = new Point[]{null};
      MouseAdapter dragListener = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            initialClick[0] = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            int thisX = TextureImportDialog.this.getLocation().x;
            int thisY = TextureImportDialog.this.getLocation().y;
            int xMoved = e.getX() - initialClick[0].x;
            int yMoved = e.getY() - initialClick[0].y;
            TextureImportDialog.this.setLocation(thisX + xMoved, thisY + yMoved);
         }
      };
      topBar.addMouseListener(dragListener);
      topBar.addMouseMotionListener(dragListener);
      JPanel topLeft = new JPanel(new FlowLayout(0, 10, 0));
      topLeft.setOpaque(false);
      JButton btnLoad = accentBtn("Load PNG Image...");
      btnLoad.setFont(new Font("Segoe UI", 1, 12));
      btnLoad.addActionListener(e -> this.loadImage());
      topLeft.add(btnLoad);
      this.comboSourceZoom = new JComboBox<>(new String[]{"Source: Zoom 4 (100%)", "Source: Zoom 3 (75%)", "Source: Zoom 2 (50%)", "Source: Zoom 1 (25%)"});
      this.comboSourceZoom.setBackground(PANEL);
      this.comboSourceZoom.setForeground(FG);
      this.comboSourceZoom.setFont(new Font("Segoe UI", 0, 11));
      this.comboSourceZoom.addActionListener(e -> this.updateScaledImage());
      topLeft.add(this.comboSourceZoom);
      this.imgInfoLabel = new JLabel("Hãy load file PNG.");
      this.imgInfoLabel.setFont(new Font("Segoe UI", 1, 12));
      this.imgInfoLabel.setForeground(new Color(120, 200, 255));
      topLeft.add(this.imgInfoLabel);
      topBar.add(topLeft, "West");
      JPanel topRight = new JPanel(new FlowLayout(2, 10, 0));
      topRight.setOpaque(false);
      JLabel titleLbl = new JLabel("PRO INTERACTIVE CROPPER");
      titleLbl.setFont(new Font("Segoe UI", 1, 15));
      titleLbl.setForeground(Color.WHITE);
      topRight.add(titleLbl);
      JButton btnClose = new JButton() {
         private boolean hovered = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hovered = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hovered = false;
                  repaint();
               }
            });
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(this.hovered ? new Color(220, 50, 50) : TextureImportDialog.BG);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 6, 6);
            g2.setColor(this.hovered ? Color.WHITE : TextureImportDialog.FG_DIM);
            g2.setStroke(new BasicStroke(1.2F));
            int cx = this.getWidth() / 2;
            int cy = this.getHeight() / 2;
            int hs = 4;
            g2.drawLine(cx - hs, cy - hs, cx + hs, cy + hs);
            g2.drawLine(cx + hs, cy - hs, cx - hs, cy + hs);
            g2.dispose();
         }
      };
      btnClose.setPreferredSize(new Dimension(28, 28));
      btnClose.setContentAreaFilled(false);
      btnClose.setBorderPainted(false);
      btnClose.setFocusPainted(false);
      btnClose.setCursor(new Cursor(12));
      btnClose.addActionListener(e -> this.dispose());
      topRight.add(btnClose);
      topBar.add(topRight, "East");
      root.add(topBar, "North");
      this.atlasCanvas = new TextureImportDialog.AtlasCanvas();
      JScrollPane canvasScroll = new JScrollPane(this.atlasCanvas);
      canvasScroll.setBorder(BorderFactory.createLineBorder(BORDER));
      canvasScroll.setBackground(new Color(15, 15, 20));
      canvasScroll.getViewport().setBackground(new Color(15, 15, 20));
      JPanel centerWrapper = new JPanel(new BorderLayout());
      centerWrapper.setOpaque(false);
      centerWrapper.add(canvasScroll, "Center");
      JPanel canvasToolbar = new JPanel(new FlowLayout(0, 10, 5));
      canvasToolbar.setBackground(PANEL);
      JLabel canvasHint = new JLabel("Tips: Cuộn chuột Zoom | Click TRÁI vẽ Cắt Tay | Click PHẢI xóa vùng | Drag TRÁI lên gạch để chọn");
      canvasHint.setFont(new Font("Segoe UI", 2, 11));
      canvasHint.setForeground(FG_DIM);
      canvasToolbar.add(canvasHint);
      centerWrapper.add(canvasToolbar, "South");
      root.add(centerWrapper, "Center");
      JPanel eastPanel = new JPanel(new BorderLayout(0, 10));
      eastPanel.setPreferredSize(new Dimension(350, 0));
      eastPanel.setOpaque(false);
      JPanel modePanel = new JPanel(new BorderLayout());
      modePanel.setOpaque(false);
      JPanel tabs = new JPanel(new GridLayout(1, 2, 5, 0));
      tabs.setOpaque(false);
      this.btnTabAlpha = tabBtn("Alpha Detect", true);
      this.btnTabGrid = tabBtn("Grid Mode", false);
      this.btnTabAlpha.addActionListener(e -> this.switchMode(false));
      this.btnTabGrid.addActionListener(e -> this.switchMode(true));
      tabs.add(this.btnTabAlpha);
      tabs.add(this.btnTabGrid);
      modePanel.add(tabs, "North");
      this.configCards = new CardLayout();
      this.cardPanel = new JPanel(this.configCards);
      this.cardPanel.setBackground(PANEL);
      this.cardPanel.setBorder(new CompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(12, 12, 12, 12)));
      JPanel pnlAlpha = new JPanel();
      pnlAlpha.setLayout(new BoxLayout(pnlAlpha, 1));
      pnlAlpha.setOpaque(false);
      JPanel gAlpha = new JPanel(new GridLayout(3, 2, 5, 8));
      gAlpha.setOpaque(false);
      gAlpha.add(label("Threshold (Alpha):"));
      this.spThreshold = spinner(35, 1, 254);
      gAlpha.add(this.spThreshold);
      gAlpha.add(label("Min Width/Height:"));
      this.spMinSize = spinner(4, 1, 512);
      gAlpha.add(this.spMinSize);
      gAlpha.add(label("Extra Padding:"));
      this.spPadding = spinner(0, 0, 32);
      gAlpha.add(this.spPadding);
      pnlAlpha.add(gAlpha);
      pnlAlpha.add(Box.createVerticalStrut(12));
      JButton btnRunAlpha = accentBtn("Chạy Tự Động Quét (Alpha)");
      btnRunAlpha.addActionListener(e -> this.detectAlpha());
      pnlAlpha.add(btnRunAlpha);
      this.cardPanel.add(pnlAlpha, "ALPHA");

      for (JSpinner s : new JSpinner[]{this.spThreshold, this.spMinSize, this.spPadding}) {
         s.addChangeListener(e -> {
            if (this.loadedImage != null) {
               this.detectAlpha();
            }
         });
      }

      JPanel pnlGrid = new JPanel();
      pnlGrid.setLayout(new BoxLayout(pnlGrid, 1));
      pnlGrid.setOpaque(false);
      JPanel gGrid = new JPanel(new GridLayout(4, 2, 5, 8));
      gGrid.setOpaque(false);
      gGrid.add(label("Số Cột:"));
      this.spCols = spinner(4, 1, 100);
      gGrid.add(this.spCols);
      gGrid.add(label("Số Hàng:"));
      this.spRows = spinner(1, 1, 100);
      gGrid.add(this.spRows);
      gGrid.add(label("Rộng Ô (Nếu có):"));
      this.spGridCellW = spinner(0, 0, 2048);
      gGrid.add(this.spGridCellW);
      gGrid.add(label("Cao Ô (Nếu có):"));
      this.spGridCellH = spinner(0, 0, 2048);
      gGrid.add(this.spGridCellH);
      pnlGrid.add(gGrid);
      pnlGrid.add(Box.createVerticalStrut(12));
      JButton btnRunGrid = accentBtn("Tự Động Cắt Lưới (Grid)");
      btnRunGrid.addActionListener(e -> this.detectGrid());
      pnlGrid.add(btnRunGrid);
      this.cardPanel.add(pnlGrid, "GRID");

      for (JSpinner s : new JSpinner[]{this.spCols, this.spRows, this.spGridCellW, this.spGridCellH}) {
         s.addChangeListener(e -> {
            if (this.loadedImage != null) {
               this.detectGrid();
            }
         });
      }

      modePanel.add(this.cardPanel, "Center");
      eastPanel.add(modePanel, "North");
      JPanel listContainer = new JPanel(new BorderLayout());
      listContainer.setBackground(PANEL);
      listContainer.setBorder(BorderFactory.createLineBorder(BORDER));
      JPanel listHeader = new JPanel(new BorderLayout());
      listHeader.setBackground(new Color(40, 40, 52));
      listHeader.setBorder(new EmptyBorder(6, 10, 6, 10));
      JLabel listTitle = new JLabel("KẾT QUẢ VÙNG CẮT");
      listTitle.setFont(new Font("Segoe UI", 1, 11));
      listTitle.setForeground(Color.WHITE);
      listHeader.add(listTitle, "West");
      this.statusLabel = new JLabel("0 sprites");
      this.statusLabel.setFont(new Font("Segoe UI", 1, 11));
      this.statusLabel.setForeground(new Color(0, 220, 140));
      listHeader.add(this.statusLabel, "East");
      listContainer.add(listHeader, "North");
      this.regionModel = new DefaultListModel<>();
      this.regionJList = new JList<>(this.regionModel);
      this.regionJList.setBackground(new Color(22, 22, 28));
      this.regionJList.setSelectionBackground(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 50));
      this.regionJList.setSelectionForeground(Color.WHITE);
      this.regionJList.setCellRenderer(new TextureImportDialog.RegionCellRenderer());
      this.regionJList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            this.selectedRegionIdx = this.regionJList.getSelectedIndex();
            this.atlasCanvas.repaint();
         }
      });
      JScrollPane listScroll = new JScrollPane(this.regionJList);
      listScroll.setBorder(null);
      listContainer.add(listScroll, "Center");
      JPanel listBottom = new JPanel(new GridLayout(1, 1));
      listBottom.setOpaque(false);
      JButton btnClear = btn("✕ Xóa toàn bộ vùng cắt");
      btnClear.setBackground(new Color(90, 35, 35));
      btnClear.addActionListener(e -> {
         this.detectedRegions.clear();
         this.updateStatus();
      });
      listBottom.add(btnClear);
      listContainer.add(listBottom, "South");
      eastPanel.add(listContainer, "Center");
      root.add(eastPanel, "East");
      JPanel bottomPanel = new JPanel(new FlowLayout(2, 15, 8));
      bottomPanel.setBackground(PANEL);
      bottomPanel.setBorder(new MatteBorder(1, 0, 0, 0, BORDER));
      JButton btnCancel = btn("✕ Hủy bỏ");
      btnCancel.setPreferredSize(new Dimension(100, 35));
      btnCancel.addActionListener(e -> this.dispose());
      JButton btnOk = accentBtn("✔ Xác nhận Import & Lưu");
      btnOk.setPreferredSize(new Dimension(220, 35));
      btnOk.setFont(new Font("Segoe UI", 1, 12));
      btnOk.addActionListener(e -> {
         if (this.detectedRegions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy cắt ít nhất 1 Sprite!", "Thông báo", 2);
         } else {
            this.buildResult();
            this.confirmed = true;
            this.dispose();
         }
      });
      bottomPanel.add(btnCancel);
      bottomPanel.add(btnOk);
      root.add(bottomPanel, "South");
      this.setContentPane(root);
   }

   private void switchMode(boolean grid) {
      this.isGridMode = grid;
      this.configCards.show(this.cardPanel, grid ? "GRID" : "ALPHA");
      this.btnTabAlpha.setBackground(grid ? PANEL : ACCENT);
      this.btnTabAlpha.setForeground(grid ? FG : Color.WHITE);
      this.btnTabGrid.setBackground(grid ? ACCENT : PANEL);
      this.btnTabGrid.setForeground(grid ? Color.WHITE : FG);
   }

   private void updateScaledImage() {
      if (this.originalLoadedImage != null) {
         int sourceZoom = 4 - this.comboSourceZoom.getSelectedIndex();
         this.loadedImage = ImageZoomHelper.scaleToZoom4(this.originalLoadedImage, sourceZoom);
         this.imgInfoLabel
            .setText(
               "Scaled: "
                  + this.originalLoadedImage.getWidth()
                  + "x"
                  + this.originalLoadedImage.getHeight()
                  + " (Zoom "
                  + sourceZoom
                  + ") -> "
                  + this.loadedImage.getWidth()
                  + "x"
                  + this.loadedImage.getHeight()
                  + " Zoom 4"
            );
         this.detectAlpha();
         if (this.atlasCanvas != null) {
            this.atlasCanvas.revalidate();
            this.atlasCanvas.repaint();
         }
      }
   }

   private void loadImage() {
      JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
      fc.setFileFilter(new FileNameExtensionFilter("PNG Images", "png", "PNG"));
      fc.setDialogTitle("Chọn ảnh SpriteSheet PNG");
      if (fc.showOpenDialog(this) == 0) {
         try {
            File selectedFile = fc.getSelectedFile();
            String path = selectedFile.getAbsolutePath().replace('\\', '/');
            ActionListener[] listeners = this.comboSourceZoom.getActionListeners();

            for (ActionListener al : listeners) {
               this.comboSourceZoom.removeActionListener(al);
            }

            if (path.contains("/x1/")) {
               this.comboSourceZoom.setSelectedIndex(3);
            } else if (path.contains("/x2/")) {
               this.comboSourceZoom.setSelectedIndex(2);
            } else if (path.contains("/x3/")) {
               this.comboSourceZoom.setSelectedIndex(1);
            } else if (path.contains("/x4/")) {
               this.comboSourceZoom.setSelectedIndex(0);
            }

            for (ActionListener al : listeners) {
               this.comboSourceZoom.addActionListener(al);
            }

            this.originalLoadedImage = ImageIO.read(selectedFile);
            if (this.originalLoadedImage == null) {
               throw new IOException("Không thể đọc định dạng ảnh.");
            }

            int sourceZoom = 4 - this.comboSourceZoom.getSelectedIndex();
            this.loadedImage = ImageZoomHelper.scaleToZoom4(this.originalLoadedImage, sourceZoom);
            this.imgInfoLabel
               .setText(
                  selectedFile.getName()
                     + " ("
                     + this.originalLoadedImage.getWidth()
                     + "x"
                     + this.originalLoadedImage.getHeight()
                     + " px, Zoom "
                     + sourceZoom
                     + " -> "
                     + this.loadedImage.getWidth()
                     + "x"
                     + this.loadedImage.getHeight()
                     + " Zoom 4)"
               );
            this.canvasZoom = 1.0;
            this.detectAlpha();
         } catch (Exception var9) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc ảnh: " + var9.getMessage());
         }
      }
   }

   private void detectAlpha() {
      if (this.loadedImage == null) {
         JOptionPane.showMessageDialog(this, "Hãy load ảnh trước!");
      } else {
         int threshold = (Integer)this.spThreshold.getValue();
         int minSize = (Integer)this.spMinSize.getValue();
         int padding = (Integer)this.spPadding.getValue();
         this.detectedRegions.clear();
         this.detectedRegions.addAll(this.findSpritesBFS(this.loadedImage, threshold, minSize, padding));
         this.updateStatus();
      }
   }

   private List<Rectangle> findSpritesBFS(BufferedImage img, int threshold, int minSize, int pad) {
      return this.isolateSpritesRobust(img, threshold, minSize, pad);
   }

   private List<Rectangle> isolateSpritesRobust(BufferedImage img, int threshold, int minSize, int pad) {
      int W = img.getWidth();
      int H = img.getHeight();
      if (W > 2 && H > 2) {
         int[][] mask = new int[W][H];
         boolean allOpaque = true;
         int bg1 = img.getRGB(0, 0);
         int bg2 = img.getRGB(W - 1, 0);
         int bg3 = img.getRGB(0, H - 1);
         int bg4 = img.getRGB(W - 1, H - 1);

         for (int y = 0; y < H; y += 2) {
            for (int x = 0; x < W; x += 2) {
               int a = img.getRGB(x, y) >> 24 & 0xFF;
               if (a < 240) {
                  allOpaque = false;
                  break;
               }
            }

            if (!allOpaque) {
               break;
            }
         }

         for (int y = 0; y < H; y++) {
            for (int xx = 0; xx < W; xx++) {
               if (xx != 0 && y != 0 && xx != W - 1 && y != H - 1) {
                  int rgb = img.getRGB(xx, y);
                  if (!allOpaque) {
                     mask[xx][y] = rgb >> 24 & 0xFF;
                  } else {
                     int r = rgb >> 16 & 0xFF;
                     int g = rgb >> 8 & 0xFF;
                     int b = rgb & 0xFF;
                     int br = bg1 >> 16 & 0xFF;
                     int bg = bg1 >> 8 & 0xFF;
                     int bb = bg1 & 0xFF;
                     double dist = Math.sqrt((r - br) * (r - br) + (g - bg) * (g - bg) + (b - bb) * (b - bb));
                     mask[xx][y] = (int)Math.min(255.0, dist * 1.75);
                  }
               } else {
                  mask[xx][y] = 0;
               }
            }
         }

         List<Rectangle> candidates = new ArrayList<>();
         boolean[][] visited = new boolean[W][H];
         int[] dx = new int[]{1, -1, 0, 0};
         int[] dy = new int[]{0, 0, 1, -1};

         for (int sy = 0; sy < H; sy++) {
            for (int sx = 0; sx < W; sx++) {
               if (!visited[sx][sy] && mask[sx][sy] > threshold) {
                  Queue<int[]> q = new LinkedList<>();
                  q.add(new int[]{sx, sy});
                  visited[sx][sy] = true;
                  int minX = sx;
                  int minY = sy;
                  int maxX = sx;
                  int maxY = sy;

                  while (!q.isEmpty()) {
                     int[] curr = q.poll();
                     int cx = curr[0];
                     int cy = curr[1];
                     minX = Math.min(minX, cx);
                     maxX = Math.max(maxX, cx);
                     minY = Math.min(minY, cy);
                     maxY = Math.max(maxY, cy);

                     for (int d = 0; d < 4; d++) {
                        int nx = cx + dx[d];
                        int ny = cy + dy[d];
                        if (nx >= 0 && nx < W && ny >= 0 && ny < H && !visited[nx][ny]) {
                           visited[nx][ny] = true;
                           if (mask[nx][ny] > threshold) {
                              q.add(new int[]{nx, ny});
                           }
                        }
                     }
                  }

                  int rw = maxX - minX + 1;
                  int rh = maxY - minY + 1;
                  if (rw >= minSize && rh >= minSize) {
                     int px = Math.max(0, minX - pad);
                     int py = Math.max(0, minY - pad);
                     int pw = Math.min(W, maxX + 1 + pad) - px;
                     int ph = Math.min(H, maxY + 1 + pad) - py;
                     candidates.add(new Rectangle(px, py, pw, ph));
                  }
               }
            }
         }

         List<Rectangle> finalResults = new ArrayList<>();

         for (Rectangle r : candidates) {
            this.splitAndAdd(mask, r, threshold, minSize, pad, W, H, finalResults);
         }

         return finalResults;
      } else {
         return new ArrayList<>();
      }
   }

   private void splitAndAdd(int[][] mask, Rectangle r, int threshold, int minSize, int pad, int W, int H, List<Rectangle> dest) {
      int splitY = -1;

      for (int y = r.y + minSize; y < r.y + r.height - minSize; y++) {
         boolean rowEmpty = true;

         for (int x = r.x; x < r.x + r.width; x++) {
            if (mask[x][y] > threshold) {
               rowEmpty = false;
               break;
            }
         }

         if (rowEmpty) {
            splitY = y;
            break;
         }
      }

      if (splitY != -1) {
         Rectangle rTop = this.tightenMask(mask, r.x, r.y, r.width, splitY - r.y, threshold, pad, W, H);
         Rectangle rBot = this.tightenMask(mask, r.x, splitY + 1, r.width, r.y + r.height - (splitY + 1), threshold, pad, W, H);
         if (rTop != null) {
            this.splitAndAdd(mask, rTop, threshold, minSize, pad, W, H, dest);
         }

         if (rBot != null) {
            this.splitAndAdd(mask, rBot, threshold, minSize, pad, W, H, dest);
         }
      } else {
         int splitX = -1;

         for (int xx = r.x + minSize; xx < r.x + r.width - minSize; xx++) {
            boolean colEmpty = true;

            for (int y = r.y; y < r.y + r.height; y++) {
               if (mask[xx][y] > threshold) {
                  colEmpty = false;
                  break;
               }
            }

            if (colEmpty) {
               splitX = xx;
               break;
            }
         }

         if (splitX != -1) {
            Rectangle rLeft = this.tightenMask(mask, r.x, r.y, splitX - r.x, r.height, threshold, pad, W, H);
            Rectangle rRight = this.tightenMask(mask, splitX + 1, r.y, r.x + r.width - (splitX + 1), r.height, threshold, pad, W, H);
            if (rLeft != null) {
               this.splitAndAdd(mask, rLeft, threshold, minSize, pad, W, H, dest);
            }

            if (rRight != null) {
               this.splitAndAdd(mask, rRight, threshold, minSize, pad, W, H, dest);
            }
         } else {
            if (r.width >= minSize && r.height >= minSize) {
               dest.add(r);
            }
         }
      }
   }

   private Rectangle tightenMask(int[][] mask, int rx, int ry, int rw, int rh, int threshold, int pad, int maxW, int maxH) {
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      boolean found = false;

      for (int y = ry; y < ry + rh && y < maxH; y++) {
         for (int x = rx; x < rx + rw && x < maxW; x++) {
            if (mask[x][y] > threshold) {
               minX = Math.min(minX, x);
               maxX = Math.max(maxX, x);
               minY = Math.min(minY, y);
               maxY = Math.max(maxY, y);
               found = true;
            }
         }
      }

      if (!found) {
         return null;
      } else {
         int px = Math.max(0, minX - pad);
         int py = Math.max(0, minY - pad);
         int pw = Math.min(maxW, maxX + 1 + pad) - px;
         int ph = Math.min(maxH, maxY + 1 + pad) - py;
         return new Rectangle(px, py, pw, ph);
      }
   }

   private void detectGrid() {
      if (this.loadedImage == null) {
         JOptionPane.showMessageDialog(this, "Hãy load ảnh trước!");
      } else {
         int cols = (Integer)this.spCols.getValue();
         int rows = (Integer)this.spRows.getValue();
         int cellW = (Integer)this.spGridCellW.getValue();
         int cellH = (Integer)this.spGridCellH.getValue();
         if (cellW <= 0) {
            cellW = this.loadedImage.getWidth() / cols;
         }

         if (cellH <= 0) {
            cellH = this.loadedImage.getHeight() / rows;
         }

         if (cellW > 1 && cellH > 1) {
            int realCols = this.loadedImage.getWidth() / cellW;
            int realRows = this.loadedImage.getHeight() / cellH;
            this.detectedRegions.clear();

            for (int r = 0; r < realRows; r++) {
               for (int c = 0; c < realCols; c++) {
                  int rx = c * cellW;
                  int ry = r * cellH;
                  int threshold = (Integer)this.spThreshold.getValue();
                  Rectangle tight = this.tightenRectangle(this.loadedImage, rx, ry, cellW, cellH, threshold);
                  if (tight != null) {
                     this.detectedRegions.add(tight);
                  }
               }
            }

            this.updateStatus();
         } else {
            JOptionPane.showMessageDialog(this, "Kích thước ô quá nhỏ!");
         }
      }
   }

   private Rectangle tightenRectangle(BufferedImage img, int rx, int ry, int rw, int rh, int threshold) {
      int W = img.getWidth();
      int H = img.getHeight();
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      boolean found = false;

      for (int j = ry; j < ry + rh && j < H; j++) {
         for (int i = rx; i < rx + rw && i < W; i++) {
            int alpha = img.getRGB(i, j) >> 24 & 0xFF;
            if (alpha > threshold) {
               if (i < minX) {
                  minX = i;
               }

               if (i > maxX) {
                  maxX = i;
               }

               if (j < minY) {
                  minY = j;
               }

               if (j > maxY) {
                  maxY = j;
               }

               found = true;
            }
         }
      }

      return !found ? null : new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
   }

   private boolean hasNonTransparentPixels(int x, int y, int w, int h) {
      for (int j = y; j < y + h && j < this.loadedImage.getHeight(); j++) {
         for (int i = x; i < x + w && i < this.loadedImage.getWidth(); i++) {
            int alpha = this.loadedImage.getRGB(i, j) >> 24 & 0xFF;
            if (alpha > 10) {
               return true;
            }
         }
      }

      return false;
   }

   private void updateStatus() {
      this.regionModel.clear();

      for (Rectangle r : this.detectedRegions) {
         this.regionModel.addElement(r);
      }

      this.statusLabel.setText(this.detectedRegions.size() + " sprites");
      if (this.selectedRegionIdx >= this.detectedRegions.size()) {
         this.selectedRegionIdx = -1;
      }

      if (this.selectedRegionIdx >= 0) {
         this.regionJList.setSelectedIndex(this.selectedRegionIdx);
      }

      this.atlasCanvas.revalidate();
      this.atlasCanvas.repaint();
   }

   private void buildResult() {
      this.result = new SmallImageDef[this.detectedRegions.size()];

      for (int i = 0; i < this.detectedRegions.size(); i++) {
         Rectangle r = this.detectedRegions.get(i);
         this.result[i] = new SmallImageDef(i, r.x / 4, r.y / 4, r.width / 4, r.height / 4);
      }

      this.resultAtlas = this.loadedImage;
   }

   public boolean isConfirmed() {
      return this.confirmed;
   }

   public SmallImageDef[] getResult() {
      return this.result;
   }

   public BufferedImage getAtlas() {
      return this.resultAtlas;
   }

   private static JLabel label(String t) {
      JLabel l = new JLabel(t);
      l.setForeground(FG_DIM);
      l.setFont(new Font("Segoe UI", 1, 11));
      return l;
   }

   private static JSpinner spinner(int val, int min, int max) {
      JSpinner s = new JSpinner(new SpinnerNumberModel(val, min, max, 1));
      s.setFont(new Font("Segoe UI", 1, 11));
      s.setMaximumSize(new Dimension(90, 25));
      return s;
   }

   private static JButton btn(String t) {
      JButton b = new JButton(t);
      b.setFont(new Font("Segoe UI", 1, 11));
      b.setForeground(Color.WHITE);
      b.setBackground(new Color(45, 45, 55));
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 85)));
      b.setAlignmentX(0.0F);
      b.setMaximumSize(new Dimension(500, 30));
      return b;
   }

   private static JButton accentBtn(String t) {
      JButton b = new JButton(t);
      b.setBackground(ACCENT);
      b.setForeground(Color.WHITE);
      b.setFont(new Font("Segoe UI", 1, 11));
      b.setFocusPainted(false);
      b.setBorderPainted(false);
      b.setAlignmentX(0.0F);
      b.setMaximumSize(new Dimension(500, 30));
      return b;
   }

   private static JButton tabBtn(String t, boolean act) {
      JButton b = new JButton(t);
      b.setFont(new Font("Segoe UI", 1, 11));
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createLineBorder(BORDER));
      b.setBackground(act ? ACCENT : PANEL);
      b.setForeground(act ? Color.WHITE : FG);
      return b;
   }

   private class AtlasCanvas extends JPanel {
      private static final int OFFSET = 20;
      private Point dragStart = null;
      private Rectangle currentDragRect = null;

      AtlasCanvas() {
         this.setBackground(new Color(10, 10, 14));
         this.setupMouse();
      }

      private void setupMouse() {
         this.addMouseListener(
            new MouseAdapter() {
               @Override
               public void mousePressed(MouseEvent e) {
                  if (TextureImportDialog.this.loadedImage != null) {
                     Point p = AtlasCanvas.this.screenToImage(e.getPoint());
                     if (SwingUtilities.isRightMouseButton(e)) {
                        for (int i = TextureImportDialog.this.detectedRegions.size() - 1; i >= 0; i--) {
                           if (TextureImportDialog.this.detectedRegions.get(i).contains(p)) {
                              TextureImportDialog.this.detectedRegions.remove(i);
                              TextureImportDialog.this.updateStatus();
                              return;
                           }
                        }
                     } else {
                        for (int ix = TextureImportDialog.this.detectedRegions.size() - 1; ix >= 0; ix--) {
                           if (TextureImportDialog.this.detectedRegions.get(ix).contains(p)) {
                              TextureImportDialog.this.selectedRegionIdx = ix;
                              TextureImportDialog.this.regionJList.setSelectedIndex(ix);
                              TextureImportDialog.this.regionJList.ensureIndexIsVisible(ix);
                              AtlasCanvas.this.repaint();
                              return;
                           }
                        }

                        if (SwingUtilities.isLeftMouseButton(e)) {
                           TextureImportDialog.this.selectedRegionIdx = -1;
                           TextureImportDialog.this.regionJList.clearSelection();
                           AtlasCanvas.this.dragStart = p;
                           AtlasCanvas.this.currentDragRect = null;
                        }
                     }
                  }
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                  if (SwingUtilities.isLeftMouseButton(e) && AtlasCanvas.this.currentDragRect != null) {
                     Rectangle b = AtlasCanvas.this.currentDragRect
                        .intersection(new Rectangle(0, 0, TextureImportDialog.this.loadedImage.getWidth(), TextureImportDialog.this.loadedImage.getHeight()));
                     if (b.width > 2 && b.height > 2) {
                        TextureImportDialog.this.detectedRegions.add(b);
                        TextureImportDialog.this.updateStatus();
                     }
                  }

                  AtlasCanvas.this.dragStart = null;
                  AtlasCanvas.this.currentDragRect = null;
                  AtlasCanvas.this.repaint();
               }
            }
         );
         this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
               if (AtlasCanvas.this.dragStart != null && TextureImportDialog.this.loadedImage != null) {
                  Point p = AtlasCanvas.this.screenToImage(e.getPoint());
                  int x1 = Math.min(AtlasCanvas.this.dragStart.x, p.x);
                  int y1 = Math.min(AtlasCanvas.this.dragStart.y, p.y);
                  int w = Math.abs(AtlasCanvas.this.dragStart.x - p.x);
                  int h = Math.abs(AtlasCanvas.this.dragStart.y - p.y);
                  AtlasCanvas.this.currentDragRect = new Rectangle(x1, y1, w, h);
                  AtlasCanvas.this.repaint();
               }
            }
         });
         this.setFocusable(true);
         InputMap im = this.getInputMap(2);
         ActionMap am = this.getActionMap();
         im.put(KeyStroke.getKeyStroke(38, 128), "pZIn");
         am.put("pZIn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               TextureImportDialog.this.canvasZoom *= 1.12;
               TextureImportDialog.this.canvasZoom = Math.max(0.1, Math.min(10.0, TextureImportDialog.this.canvasZoom));
               AtlasCanvas.this.revalidate();
               AtlasCanvas.this.repaint();
            }
         });
         im.put(KeyStroke.getKeyStroke(40, 128), "pZOut");
         am.put("pZOut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               TextureImportDialog.this.canvasZoom /= 1.12;
               TextureImportDialog.this.canvasZoom = Math.max(0.1, Math.min(10.0, TextureImportDialog.this.canvasZoom));
               AtlasCanvas.this.revalidate();
               AtlasCanvas.this.repaint();
            }
         });
         AbstractAction shiftLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (AtlasCanvas.this.getParent() instanceof JViewport vp) {
                  Point pt = vp.getViewPosition();
                  pt.x = Math.max(0, pt.x - 40);
                  vp.setViewPosition(pt);
               }
            }
         };
         AbstractAction shiftRight = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (AtlasCanvas.this.getParent() instanceof JViewport vp) {
                  Point pt = vp.getViewPosition();
                  pt.x = Math.min(Math.max(0, AtlasCanvas.this.getWidth() - vp.getWidth()), pt.x + 40);
                  vp.setViewPosition(pt);
               }
            }
         };
         im.put(KeyStroke.getKeyStroke(37, 64), "shL_L");
         im.put(KeyStroke.getKeyStroke(38, 64), "shL_U");
         im.put(KeyStroke.getKeyStroke(39, 64), "shR_R");
         im.put(KeyStroke.getKeyStroke(40, 64), "shR_D");
         am.put("shL_L", shiftLeft);
         am.put("shL_U", shiftLeft);
         am.put("shR_R", shiftRight);
         am.put("shR_D", shiftRight);
         this.addMouseWheelListener(e -> {
            if (TextureImportDialog.this.loadedImage != null) {
               if (e.isControlDown()) {
                  if (e.getWheelRotation() < 0) {
                     TextureImportDialog.this.canvasZoom *= 1.15;
                  } else {
                     TextureImportDialog.this.canvasZoom /= 1.15;
                  }

                  TextureImportDialog.this.canvasZoom = Math.max(0.1, Math.min(10.0, TextureImportDialog.this.canvasZoom));
                  this.revalidate();
                  this.repaint();
               } else {
                  this.getParent().dispatchEvent(e);
               }
            }
         });
      }

      private Point screenToImage(Point scrPt) {
         int x = (int)Math.round((scrPt.x - 20) / TextureImportDialog.this.canvasZoom);
         int y = (int)Math.round((scrPt.y - 20) / TextureImportDialog.this.canvasZoom);
         return new Point(x, y);
      }

      @Override
      public Dimension getPreferredSize() {
         if (TextureImportDialog.this.loadedImage == null) {
            return new Dimension(800, 600);
         } else {
            int w = (int)(TextureImportDialog.this.loadedImage.getWidth() * TextureImportDialog.this.canvasZoom) + 40;
            int h = (int)(TextureImportDialog.this.loadedImage.getHeight() * TextureImportDialog.this.canvasZoom) + 40;
            return new Dimension(w, h);
         }
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         g2.setColor(new Color(15, 15, 20));
         g2.fillRect(0, 0, this.getWidth(), this.getHeight());

         for (int y = 0; y < this.getHeight(); y += 16) {
            for (int x = 0; x < this.getWidth(); x += 16) {
               if ((x / 16 + y / 16) % 2 == 0) {
                  g2.setColor(new Color(22, 22, 28));
                  g2.fillRect(x, y, 16, 16);
               }
            }
         }

         if (TextureImportDialog.this.loadedImage == null) {
            g2.setFont(new Font("Segoe UI", 1, 13));
            g2.setColor(TextureImportDialog.FG_DIM);
            g2.drawString("CHƯA CÓ ẢNH. HÃY ẤN NÚT 'LOAD PNG IMAGE' Ở TRÊN.", 50, this.getHeight() / 2);
         } else {
            int dw = (int)(TextureImportDialog.this.loadedImage.getWidth() * TextureImportDialog.this.canvasZoom);
            int dh = (int)(TextureImportDialog.this.loadedImage.getHeight() * TextureImportDialog.this.canvasZoom);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.drawImage(TextureImportDialog.this.loadedImage, 20, 20, dw, dh, null);

            for (int i = 0; i < TextureImportDialog.this.detectedRegions.size(); i++) {
               Rectangle r = TextureImportDialog.this.detectedRegions.get(i);
               boolean s = i == TextureImportDialog.this.selectedRegionIdx;
               int rx = 20 + (int)(r.x * TextureImportDialog.this.canvasZoom);
               int ry = 20 + (int)(r.y * TextureImportDialog.this.canvasZoom);
               int rw = (int)(r.width * TextureImportDialog.this.canvasZoom);
               int rh = (int)(r.height * TextureImportDialog.this.canvasZoom);
               g2.setColor(s ? new Color(0, 230, 255, 65) : new Color(0, 150, 255, 30));
               g2.fillRect(rx, ry, rw, rh);
               g2.setStroke(new BasicStroke(s ? 2.0F : 1.0F));
               g2.setColor(s ? new Color(0, 230, 255) : new Color(0, 130, 255, 180));
               g2.drawRect(rx, ry, rw, rh);
               g2.setColor(new Color(15, 15, 20, 210));
               g2.fillRect(rx + 1, ry + 1, 16, 12);
               g2.setColor(Color.WHITE);
               g2.setFont(new Font("Monospaced", 1, 8));
               g2.drawString(String.valueOf(i), rx + 3, ry + 10);
            }

            if (this.currentDragRect != null) {
               int rx = 20 + (int)(this.currentDragRect.x * TextureImportDialog.this.canvasZoom);
               int ry = 20 + (int)(this.currentDragRect.y * TextureImportDialog.this.canvasZoom);
               int rw = (int)(this.currentDragRect.width * TextureImportDialog.this.canvasZoom);
               int rh = (int)(this.currentDragRect.height * TextureImportDialog.this.canvasZoom);
               g2.setColor(new Color(255, 220, 0, 60));
               g2.fillRect(rx, ry, rw, rh);
               g2.setStroke(new BasicStroke(1.5F, 0, 2, 0.0F, new float[]{5.0F}, 0.0F));
               g2.setColor(Color.YELLOW);
               g2.drawRect(rx, ry, rw, rh);
            }

            g2.setColor(Color.ORANGE);
            g2.setFont(new Font("Segoe UI", 1, 11));
            g2.drawString(String.format("Zoom: %.0f%%", TextureImportDialog.this.canvasZoom * 100.0), 10, this.getHeight() - 12);
         }
      }
   }

   private class RegionCellRenderer extends JPanel implements ListCellRenderer<Rectangle> {
      private final JLabel icon = new JLabel("", 0);
      private final JLabel info = new JLabel();
      private final JLabel tag = new JLabel("", 0);

      RegionCellRenderer() {
         this.setLayout(new BorderLayout(8, 0));
         this.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, TextureImportDialog.BORDER), new EmptyBorder(5, 6, 5, 6)));
         this.setOpaque(true);
         this.tag.setFont(new Font("Monospaced", 1, 12));
         this.tag.setPreferredSize(new Dimension(25, 0));
         this.tag.setForeground(Color.ORANGE);
         this.icon.setPreferredSize(new Dimension(36, 36));
         this.icon.setOpaque(true);
         this.icon.setBackground(new Color(10, 10, 15));
         this.icon.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 60)));
         this.info.setFont(new Font("Monospaced", 0, 11));
         this.info.setForeground(TextureImportDialog.FG);
         JPanel lGrp = new JPanel(new FlowLayout(0, 6, 0));
         lGrp.setOpaque(false);
         lGrp.add(this.tag);
         lGrp.add(this.icon);
         this.add(lGrp, "West");
         this.add(this.info, "Center");
      }

      public Component getListCellRendererComponent(JList<? extends Rectangle> list, Rectangle r, int index, boolean isSelected, boolean hasFocus) {
         this.tag.setText("#" + index);
         this.info.setText(String.format("<html>X:%-3d Y:%-3d<br/><b>W:%-3d H:%-3d</b></html>", r.x, r.y, r.width, r.height));
         if (TextureImportDialog.this.loadedImage != null && r.width > 0 && r.height > 0) {
            try {
               BufferedImage sub = TextureImportDialog.this.loadedImage.getSubimage(r.x, r.y, r.width, r.height);
               Image sc = sub.getScaledInstance(Math.min(34, r.width), Math.min(34, r.height), 2);
               this.icon.setIcon(new ImageIcon(sc));
            } catch (Exception var8) {
               this.icon.setIcon(null);
            }
         } else {
            this.icon.setIcon(null);
         }

         if (isSelected) {
            this.setBackground(new Color(45, 65, 90));
            this.info.setForeground(Color.WHITE);
         } else {
            this.setBackground(new Color(24, 24, 30));
            this.info.setForeground(TextureImportDialog.FG);
         }

         return this;
      }
   }
}
