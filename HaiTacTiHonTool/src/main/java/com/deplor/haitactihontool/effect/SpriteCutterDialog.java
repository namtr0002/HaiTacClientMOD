package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.ImageZoomHelper;
import com.deplor.haitactihontool.config.Theme;
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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SpriteCutterDialog extends JDialog {
   private static final Color BG = new Color(22, 22, 30);
   private static final Color PANEL = new Color(30, 30, 40);
   private static final Color BORDER = new Color(50, 50, 68);
   private static final Color ACCENT = new Color(0, 130, 255);
   private static final Color FG = new Color(230, 230, 245);
   private static final Color FG_DIM = new Color(150, 150, 175);
   private final EffectModel model;
   private final Component parentUI;
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
   private SpriteCutterDialog.AtlasCanvas atlasCanvas;
   private JList<Rectangle> regionJList;
   private DefaultListModel<Rectangle> regionModel;
   private JLabel imgInfoLabel;
   private JSpinner spThreshold;
   private JSpinner spMinSize;
   private JSpinner spPadding;
   private JSpinner spGapMerge;
   private JSpinner spCols;
   private JSpinner spRows;
   private JSpinner spGridCellW;
   private JSpinner spGridCellH;
   private JLabel statusLabel;
   private CardLayout configCards;
   private JPanel cardPanel;
   private JButton btnTabAlpha;
   private JButton btnTabGrid;

   public SpriteCutterDialog(Frame owner, EffectModel model, Component parentUI) {
      super(owner, "Pro Spritesheet Cutter - Effect ID " + model.id, true);
      this.model = model;
      this.parentUI = parentUI;
      this.setUndecorated(true);
      this.setSize(1240, 820);
      this.setLocationRelativeTo(owner);
      this.buildUI();
      this.setupGlobalKeyBindings();
      if (model.atlasImage != null) {
         this.originalLoadedImage = model.atlasImage;
         this.loadedImage = model.atlasImage;
         if (model.smallImages != null) {
            for (SmallImageDef si : model.smallImages) {
               this.detectedRegions.add(new Rectangle(si.x * 4, si.y * 4, si.w * 4, si.h * 4));
            }
         }

         this.imgInfoLabel.setText("Effect " + model.id + " (" + this.originalLoadedImage.getWidth() + "x" + this.originalLoadedImage.getHeight() + " px)");
         this.comboSourceZoom.setSelectedIndex(0);
         this.updateStatus();
      } else {
         SwingUtilities.invokeLater(() -> {
            this.loadImage();
            if (this.loadedImage == null) {
               this.dispose();
            }
         });
      }
   }

   private void buildUI() {
      JPanel contentPanelOuter = new JPanel(new BorderLayout());
      contentPanelOuter.setBackground(BG);
      contentPanelOuter.setBorder(BorderFactory.createLineBorder(BORDER, 2));
      this.setContentPane(contentPanelOuter);
      JPanel root = new JPanel(new BorderLayout(8, 8));
      root.setBackground(BG);
      root.setBorder(new EmptyBorder(8, 10, 10, 10));
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
            int thisX = SpriteCutterDialog.this.getLocation().x;
            int thisY = SpriteCutterDialog.this.getLocation().y;
            int xMoved = e.getX() - initialClick[0].x;
            int yMoved = e.getY() - initialClick[0].y;
            SpriteCutterDialog.this.setLocation(thisX + xMoved, thisY + yMoved);
         }
      };
      topBar.addMouseListener(dragListener);
      topBar.addMouseMotionListener(dragListener);
      JPanel topLeft = new JPanel(new FlowLayout(0, 8, 0));
      topLeft.setOpaque(false);
      JButton btnLoad = accentBtn("\ud83d\udcc1 Load PNG Image...");
      btnLoad.setFont(new Font("Segoe UI", 1, 11));
      btnLoad.addActionListener(e -> this.loadImage());
      topLeft.add(btnLoad);
      this.comboSourceZoom = new JComboBox<>(new String[]{"Source: Zoom 4 (100%)", "Source: Zoom 3 (75%)", "Source: Zoom 2 (50%)", "Source: Zoom 1 (25%)"});
      this.comboSourceZoom.setBackground(PANEL);
      this.comboSourceZoom.setForeground(FG);
      this.comboSourceZoom.setFont(new Font("Segoe UI", 0, 11));
      this.comboSourceZoom.addActionListener(e -> this.updateScaledImage());
      topLeft.add(this.comboSourceZoom);
      this.imgInfoLabel = new JLabel("Hãy load file PNG.");
      this.imgInfoLabel.setFont(new Font("Segoe UI", 1, 11));
      this.imgInfoLabel.setForeground(new Color(120, 200, 255));
      topLeft.add(this.imgInfoLabel);
      topBar.add(topLeft, "West");
      JPanel topRight = new JPanel(new FlowLayout(2, 10, 0));
      topRight.setOpaque(false);
      JLabel titleLbl = new JLabel("BỘ CẮT SPRITE HIỆU ỨNG (ID: " + this.model.id + ")");
      titleLbl.setFont(new Font("Segoe UI", 1, 13));
      titleLbl.setForeground(Color.WHITE);
      topRight.add(titleLbl);
      JButton btnClose = new JButton("✕");
      btnClose.setFont(new Font("Segoe UI", 1, 13));
      btnClose.setForeground(Color.WHITE);
      btnClose.setBackground(new Color(180, 45, 45));
      btnClose.setFocusPainted(false);
      btnClose.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
      btnClose.setCursor(new Cursor(12));
      btnClose.addActionListener(e -> this.dispose());
      topRight.add(btnClose);
      topBar.add(topRight, "East");
      root.add(topBar, "North");
      this.atlasCanvas = new SpriteCutterDialog.AtlasCanvas();
      JScrollPane canvasScroll = new JScrollPane(this.atlasCanvas);
      canvasScroll.setBorder(BorderFactory.createLineBorder(BORDER));
      canvasScroll.setBackground(new Color(14, 14, 18));
      canvasScroll.getViewport().setBackground(new Color(14, 14, 18));
      canvasScroll.getHorizontalScrollBar().setUnitIncrement(25);
      canvasScroll.getVerticalScrollBar().setUnitIncrement(25);
      JPanel rightPanel = new JPanel(new BorderLayout(0, 8));
      rightPanel.setPreferredSize(new Dimension(330, 0));
      rightPanel.setOpaque(false);
      JPanel tabs = new JPanel(new GridLayout(1, 2, 4, 0));
      tabs.setOpaque(false);
      this.btnTabAlpha = tabBtn("Tự Động Alpha", true);
      this.btnTabGrid = tabBtn("Cắt Theo Lưới", false);
      this.btnTabAlpha.addActionListener(e -> this.switchTab(false));
      this.btnTabGrid.addActionListener(e -> this.switchTab(true));
      tabs.add(this.btnTabAlpha);
      tabs.add(this.btnTabGrid);
      JPanel configWrap = new JPanel(new BorderLayout(0, 6));
      configWrap.setOpaque(false);
      configWrap.add(tabs, "North");
      this.configCards = new CardLayout();
      this.cardPanel = new JPanel(this.configCards);
      this.cardPanel.setOpaque(false);
      this.cardPanel.add(this.buildAlphaPanel(), "ALPHA");
      this.cardPanel.add(this.buildGridPanel(), "GRID");
      configWrap.add(this.cardPanel, "Center");
      rightPanel.add(configWrap, "North");
      JPanel listPanel = new JPanel(new BorderLayout(0, 4));
      listPanel.setOpaque(false);
      listPanel.setBorder(new CompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(6, 8, 6, 8)));
      JPanel listHdr = new JPanel(new BorderLayout());
      listHdr.setOpaque(false);
      JLabel listTitle = new JLabel("DANH SÁCH SPRITE ĐÃ CẮT");
      listTitle.setFont(new Font("Segoe UI", 1, 10));
      listTitle.setForeground(FG_DIM);
      this.statusLabel = new JLabel("0 sprites");
      this.statusLabel.setFont(new Font("Segoe UI", 1, 10));
      this.statusLabel.setForeground(Theme.ACCENT);
      listHdr.add(listTitle, "West");
      listHdr.add(this.statusLabel, "East");
      listPanel.add(listHdr, "North");
      this.regionModel = new DefaultListModel<>();
      this.regionJList = new JList<>(this.regionModel);
      this.regionJList.setBackground(new Color(20, 20, 26));
      this.regionJList.setForeground(FG);
      this.regionJList.setSelectionBackground(new Color(0, 95, 210));
      this.regionJList.setSelectionForeground(Color.WHITE);
      this.regionJList.setCellRenderer(new SpriteCutterDialog.RegionCellRenderer());
      this.regionJList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            this.selectedRegionIdx = this.regionJList.getSelectedIndex();
            this.atlasCanvas.repaint();
         }
      });
      this.regionJList.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
               int idx = SpriteCutterDialog.this.regionJList.locationToIndex(e.getPoint());
               if (idx >= 0) {
                  SpriteCutterDialog.this.regionJList.setSelectedIndex(idx);
                  SpriteCutterDialog.this.selectedRegionIdx = idx;
                  SpriteCutterDialog.this.showContextMenu(e.getComponent(), e.getX(), e.getY());
               }
            }
         }
      });
      JScrollPane listScroll = new JScrollPane(this.regionJList);
      listScroll.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 52)));
      listScroll.getViewport().setBackground(new Color(20, 20, 26));
      listScroll.getVerticalScrollBar().setUnitIncrement(20);
      listPanel.add(listScroll, "Center");
      JPanel listBtns = new JPanel(new GridLayout(1, 4, 3, 0));
      listBtns.setOpaque(false);
      JButton btnDelSel = actionBtn("✖ Xóa", new Color(170, 45, 45));
      btnDelSel.setToolTipText("Xóa Sprite đang chọn (Phím Delete)");
      btnDelSel.addActionListener(e -> this.deleteSelectedRegion());
      JButton btnDupSel = actionBtn("⎘ Nhân Bản", new Color(38, 38, 52));
      btnDupSel.setToolTipText("Nhân bản Sprite đang chọn");
      btnDupSel.addActionListener(e -> this.duplicateSelectedRegion());
      JButton btnSort = actionBtn("\ud83d\udd04 Sắp Xếp", new Color(0, 120, 160));
      btnSort.setToolTipText("Sắp xếp lại Sprite theo thứ tự Đọc Trên-Dưới, Trái-Phải");
      btnSort.addActionListener(e -> this.sortRegions());
      JButton btnClear = actionBtn("\ud83d\uddd1 Xóa Hết", new Color(40, 40, 52));
      btnClear.setToolTipText("Xóa toàn bộ các vùng cắt");
      btnClear.addActionListener(e -> {
         if (!this.detectedRegions.isEmpty()) {
            int ans = JOptionPane.showConfirmDialog(this, "Xác nhận xóa TẤT CẢ các sprite đã cắt?", "Xác nhận", 0);
            if (ans == 0) {
               this.detectedRegions.clear();
               this.selectedRegionIdx = -1;
               this.updateStatus();
            }
         }
      });
      listBtns.add(btnDelSel);
      listBtns.add(btnDupSel);
      listBtns.add(btnSort);
      listBtns.add(btnClear);
      listPanel.add(listBtns, "South");
      rightPanel.add(listPanel, "Center");
      JSplitPane centerSplit = new JSplitPane(1, canvasScroll, rightPanel);
      centerSplit.setDividerLocation(880);
      centerSplit.setResizeWeight(0.75);
      centerSplit.setBorder(null);
      centerSplit.setDividerSize(4);
      root.add(centerSplit, "Center");
      JPanel bottomBar = new JPanel(new BorderLayout(10, 0));
      bottomBar.setOpaque(false);
      bottomBar.setBorder(new EmptyBorder(6, 4, 0, 4));
      JLabel tipLbl = new JLabel(
         "\ud83d\udca1 Tips: Kéo tâm để di chuyển | Kéo 8 cạnh/góc để co giãn | Kéo vùng trống để cắt mới | Chuột phải hoặc Phím Del để xóa"
      );
      tipLbl.setFont(new Font("Segoe UI", 0, 11));
      tipLbl.setForeground(new Color(140, 200, 255));
      bottomBar.add(tipLbl, "West");
      JPanel btnFlow = new JPanel(new FlowLayout(2, 8, 0));
      btnFlow.setOpaque(false);
      JButton btnCancel = actionBtn("Hủy bỏ", new Color(45, 45, 58));
      btnCancel.addActionListener(e -> this.dispose());
      btnFlow.add(btnCancel);
      JButton btnConfirm = accentBtn("✔ Xác nhận & Cập nhật");
      btnConfirm.setFont(new Font("Segoe UI", 1, 12));
      btnConfirm.addActionListener(e -> {
         if (this.detectedRegions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có sprite nào được cắt!", "Thông báo", 2);
         } else {
            this.buildResult();
            this.confirmed = true;
            this.dispose();
         }
      });
      btnFlow.add(btnConfirm);
      bottomBar.add(btnFlow, "East");
      root.add(bottomBar, "South");
   }

   private void setupGlobalKeyBindings() {
      KeyStroke delKey = KeyStroke.getKeyStroke(127, 0);
      KeyStroke backKey = KeyStroke.getKeyStroke(8, 0);
      Action delAction = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            SpriteCutterDialog.this.deleteSelectedRegion();
         }
      };
      this.getRootPane().getInputMap(2).put(delKey, "delRegion");
      this.getRootPane().getInputMap(2).put(backKey, "delRegion");
      this.getRootPane().getActionMap().put("delRegion", delAction);
   }

   private void deleteSelectedRegion() {
      if (this.selectedRegionIdx >= 0 && this.selectedRegionIdx < this.detectedRegions.size()) {
         this.detectedRegions.remove(this.selectedRegionIdx);
         this.selectedRegionIdx = Math.min(this.selectedRegionIdx, this.detectedRegions.size() - 1);
         this.updateStatus();
      }
   }

   private void duplicateSelectedRegion() {
      if (this.selectedRegionIdx >= 0 && this.selectedRegionIdx < this.detectedRegions.size()) {
         Rectangle orig = this.detectedRegions.get(this.selectedRegionIdx);
         Rectangle dup = new Rectangle(orig.x + 8, orig.y + 8, orig.width, orig.height);
         this.detectedRegions.add(dup);
         this.selectedRegionIdx = this.detectedRegions.size() - 1;
         this.updateStatus();
      }
   }

   private void sortRegions() {
      if (this.detectedRegions.size() > 1) {
         this.detectedRegions.sort((r1, r2) -> {
            int rowTol = 14;
            return Math.abs(r1.y - r2.y) > rowTol ? Integer.compare(r1.y, r2.y) : Integer.compare(r1.x, r2.x);
         });
         this.updateStatus();
      }
   }

   private void showContextMenu(Component comp, int x, int y) {
      JPopupMenu menu = new JPopupMenu();
      menu.setBackground(new Color(28, 28, 38));
      JMenuItem mDel = new JMenuItem("✖ Xóa Sprite #" + this.selectedRegionIdx + " (Del)");
      mDel.setForeground(new Color(255, 100, 100));
      mDel.addActionListener(e -> this.deleteSelectedRegion());
      JMenuItem mDup = new JMenuItem("⎘ Nhân bản Sprite #" + this.selectedRegionIdx);
      mDup.setForeground(FG);
      mDup.addActionListener(e -> this.duplicateSelectedRegion());
      JMenuItem mTight = new JMenuItem("✂ Tinh chỉnh viền (Tighten Alpha)");
      mTight.setForeground(FG);
      mTight.addActionListener(e -> {
         if (this.selectedRegionIdx >= 0 && this.selectedRegionIdx < this.detectedRegions.size() && this.loadedImage != null) {
            Rectangle r = this.detectedRegions.get(this.selectedRegionIdx);
            int th = (Integer)this.spThreshold.getValue();
            Rectangle tight = this.tightenRectangle(this.loadedImage, r.x, r.y, r.width, r.height, th);
            if (tight != null) {
               this.detectedRegions.set(this.selectedRegionIdx, tight);
               this.updateStatus();
            }
         }
      });
      menu.add(mDel);
      menu.add(mDup);
      menu.add(mTight);
      menu.show(comp, x, y);
   }

   private JPanel buildAlphaPanel() {
      JPanel p = new JPanel(new GridLayout(5, 2, 4, 6));
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(6, 6, 6, 6));
      this.spThreshold = spin(0, 0, 255, 1);
      this.spMinSize = spin(4, 1, 500, 1);
      this.spPadding = spin(0, 0, 50, 1);
      this.spGapMerge = spin(6, 0, 100, 1);
      p.add(lbl("Threshold (Độ nhạy):"));
      p.add(this.spThreshold);
      p.add(lbl("Kích thước tối thiểu:"));
      p.add(this.spMinSize);
      p.add(lbl("Padding (Độ rộng viền):"));
      p.add(this.spPadding);
      p.add(lbl("Gộp khoảng cách (Gap):"));
      p.add(this.spGapMerge);
      JButton btnScan = accentBtn("Quét Alpha Tự Động");
      btnScan.addActionListener(e -> this.detectAlpha());
      p.add(new JLabel(""));
      p.add(btnScan);
      return p;
   }

   private JPanel buildGridPanel() {
      JPanel p = new JPanel(new GridLayout(5, 2, 4, 6));
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(6, 6, 6, 6));
      this.spCols = spin(4, 1, 100, 1);
      this.spRows = spin(4, 1, 100, 1);
      this.spGridCellW = spin(0, 0, 2000, 1);
      this.spGridCellH = spin(0, 0, 2000, 1);
      p.add(lbl("Số Cột (Columns):"));
      p.add(this.spCols);
      p.add(lbl("Số Hàng (Rows):"));
      p.add(this.spRows);
      p.add(lbl("Rộng Ô (Cell Width):"));
      p.add(this.spGridCellW);
      p.add(lbl("Cao Ô (Cell Height):"));
      p.add(this.spGridCellH);
      JButton btnGrid = accentBtn("Cắt Lưới Đều");
      btnGrid.addActionListener(e -> this.detectGrid());
      p.add(new JLabel(""));
      p.add(btnGrid);
      return p;
   }

   private void switchTab(boolean grid) {
      this.isGridMode = grid;
      this.configCards.show(this.cardPanel, grid ? "GRID" : "ALPHA");
      this.btnTabAlpha.setBackground(grid ? PANEL : ACCENT);
      this.btnTabGrid.setBackground(grid ? ACCENT : PANEL);
   }

   private void loadImage() {
      JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
      fc.setDialogTitle("Chọn hình ảnh PNG Spritesheet để cắt");
      fc.setFileFilter(new FileNameExtensionFilter("Ảnh PNG (*.png)", "png"));
      if (fc.showOpenDialog(this) == 0) {
         try {
            BufferedImage img = ImageIO.read(fc.getSelectedFile());
            if (img != null) {
               this.originalLoadedImage = img;
               int z = ImageZoomHelper.detectImageZoom(img, null);
               if (z == 1) {
                  this.comboSourceZoom.setSelectedIndex(1);
               } else if (z == 2) {
                  this.comboSourceZoom.setSelectedIndex(2);
               } else {
                  this.comboSourceZoom.setSelectedIndex(0);
               }

               this.rebuildScaledSourceImage();
               this.detectedRegions.clear();
               this.selectedRegionIdx = -1;
               this.imgInfoLabel
                  .setText(fc.getSelectedFile().getName() + " (" + this.originalLoadedImage.getWidth() + "x" + this.originalLoadedImage.getHeight() + " px)");
               this.updateStatus();
               this.detectAlpha();
            }
         } catch (IOException var4) {
            JOptionPane.showMessageDialog(this, "Không thể đọc file ảnh: " + var4.getMessage(), "Lỗi", 0);
         }
      }
   }

   private void rebuildScaledSourceImage() {
      if (this.originalLoadedImage != null) {
         int idx = this.comboSourceZoom.getSelectedIndex();
         int srcZoom = 4;
         if (idx == 1) {
            srcZoom = 1;
         } else if (idx == 2) {
            srcZoom = 2;
         }

         this.loadedImage = ImageZoomHelper.scaleToZoom4(this.originalLoadedImage, srcZoom);
         this.atlasCanvas.revalidate();
         this.atlasCanvas.repaint();
      }
   }

   private void updateScaledImage() {
      this.rebuildScaledSourceImage();
   }

   private void detectAlpha() {
      if (this.loadedImage != null) {
         this.detectedRegions.clear();
         int threshold = (Integer)this.spThreshold.getValue();
         int minSize = (Integer)this.spMinSize.getValue();
         int pad = (Integer)this.spPadding.getValue();
         List<Rectangle> regions = this.segmentAlphaRegions(this.loadedImage, threshold, minSize, pad);
         this.detectedRegions.addAll(regions);
         this.updateStatus();
      }
   }

   private List<Rectangle> segmentAlphaRegions(BufferedImage img, int threshold, int minSize, int pad) {
      int W = img.getWidth();
      int H = img.getHeight();
      int[][] mask = new int[W][H];

      for (int y = 0; y < H; y++) {
         for (int x = 0; x < W; x++) {
            mask[x][y] = img.getRGB(x, y) >> 24 & 0xFF;
         }
      }

      boolean[][] visited = new boolean[W][H];
      List<Rectangle> rawBoxes = new ArrayList<>();
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
               if (rw >= 2 && rh >= 2) {
                  rawBoxes.add(new Rectangle(minX, minY, rw, rh));
               }
            }
         }
      }

      int gapMerge = this.spGapMerge != null ? (Integer)this.spGapMerge.getValue() : 6;
      if (gapMerge > 0 && rawBoxes.size() > 1) {
         boolean mergedAny = true;

         while (mergedAny) {
            mergedAny = false;

            for (int i = 0; i < rawBoxes.size(); i++) {
               Rectangle a = rawBoxes.get(i);

               for (int j = i + 1; j < rawBoxes.size(); j++) {
                  Rectangle b = rawBoxes.get(j);
                  int distH = Math.max(0, Math.max(a.x - (b.x + b.width), b.x - (a.x + a.width)));
                  int distV = Math.max(0, Math.max(a.y - (b.y + b.height), b.y - (a.y + a.height)));
                  if (distH <= gapMerge && distV <= gapMerge) {
                     rawBoxes.set(i, a.union(b));
                     rawBoxes.remove(j);
                     mergedAny = true;
                     break;
                  }
               }

               if (mergedAny) {
                  break;
               }
            }
         }
      }

      List<Rectangle> finalResults = new ArrayList<>();

      for (Rectangle r : rawBoxes) {
         Rectangle tight = this.tightenMask(mask, r.x, r.y, r.width, r.height, threshold, pad, W, H);
         if (tight != null && tight.width >= minSize && tight.height >= minSize) {
            finalResults.add(tight);
         }
      }

      finalResults.sort((r1, r2) -> {
         int rowTol = 14;
         return Math.abs(r1.y - r2.y) > rowTol ? Integer.compare(r1.y, r2.y) : Integer.compare(r1.x, r2.x);
      });
      return finalResults;
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
      if (this.loadedImage != null) {
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
      if (this.model != null) {
         this.model.smallImages = this.result;
         this.model.atlasImage = this.resultAtlas;
         if (this.result.length > 0
            && (this.model.frames == null || this.model.frames.length == 0 || this.model.frames.length == 1 && this.model.frames[0].allParts.isEmpty())) {
            EffFrame[] newFrames = new EffFrame[this.result.length];

            for (int i = 0; i < this.result.length; i++) {
               EffFrame f = EffFrame.createEmpty();
               EffPartFrame p = new EffPartFrame(0, 0, i, 0, 1);
               f.allParts.add(p);
               f.rebuildLayers();
               newFrames[i] = f;
            }

            this.model.frames = newFrames;
            this.model.autoGenerateSequenceLinear();
         } else {
            this.model.sanitizeSequence();
         }
      }
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

   private static JLabel lbl(String t) {
      JLabel l = new JLabel(t);
      l.setFont(new Font("Segoe UI", 0, 10));
      l.setForeground(FG_DIM);
      return l;
   }

   private static JSpinner spin(int def, int min, int max, int step) {
      JSpinner s = new JSpinner(new SpinnerNumberModel(def, min, max, step));
      s.setFont(new Font("Segoe UI", 0, 10));
      s.setBackground(PANEL);
      s.setForeground(FG);
      s.setBorder(BorderFactory.createLineBorder(BORDER));
      JComponent editor = s.getEditor();
      if (editor instanceof DefaultEditor) {
         ((DefaultEditor)editor).getTextField().setBackground(PANEL);
         ((DefaultEditor)editor).getTextField().setForeground(FG);
      }

      return s;
   }

   private static JButton accentBtn(String t) {
      JButton b = new JButton(t);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setBackground(ACCENT);
      b.setForeground(Color.WHITE);
      b.setBorder(new EmptyBorder(5, 10, 5, 10));
      b.setFocusPainted(false);
      b.setCursor(new Cursor(12));
      return b;
   }

   private static JButton actionBtn(String t, Color bg) {
      JButton b = new JButton(t);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setBackground(bg);
      b.setForeground(Color.WHITE);
      b.setBorder(new EmptyBorder(4, 6, 4, 6));
      b.setFocusPainted(false);
      b.setCursor(new Cursor(12));
      return b;
   }

   private static JButton tabBtn(String t, boolean active) {
      JButton b = new JButton(t);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setBackground(active ? ACCENT : PANEL);
      b.setForeground(Color.WHITE);
      b.setBorder(new EmptyBorder(6, 0, 6, 0));
      b.setFocusPainted(false);
      b.setCursor(new Cursor(12));
      return b;
   }

   private class AtlasCanvas extends JPanel {
      private static final int OFFSET = 20;
      private static final int HANDLE_SIZE = 8;
      private SpriteCutterDialog.HandleType activeHandle = SpriteCutterDialog.HandleType.NONE;
      private Point dragStartScreen = null;
      private Point dragStartImg = null;
      private Rectangle origRegionRect = null;
      private Rectangle currentNewCutRect = null;

      AtlasCanvas() {
         this.setBackground(new Color(12, 12, 16));
         this.setupInteractions();
      }

      private void setupInteractions() {
         this.setFocusable(true);
         this.addMouseListener(
            new MouseAdapter() {
               @Override
               public void mousePressed(MouseEvent e) {
                  if (SpriteCutterDialog.this.loadedImage != null) {
                     AtlasCanvas.this.requestFocusInWindow();
                     Point sp = e.getPoint();
                     Point ip = AtlasCanvas.this.screenToImage(sp);
                     AtlasCanvas.this.dragStartScreen = sp;
                     AtlasCanvas.this.dragStartImg = ip;
                     if (SwingUtilities.isRightMouseButton(e)) {
                        int clicked = AtlasCanvas.this.findRegionAt(ip);
                        if (clicked >= 0) {
                           SpriteCutterDialog.this.selectedRegionIdx = clicked;
                           SpriteCutterDialog.this.regionJList.setSelectedIndex(clicked);
                           SpriteCutterDialog.this.showContextMenu(e.getComponent(), sp.x, sp.y);
                        } else {
                           AtlasCanvas.this.showEmptyContextMenu(e.getComponent(), sp.x, sp.y);
                        }

                        AtlasCanvas.this.repaint();
                     } else {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                           if (SpriteCutterDialog.this.selectedRegionIdx >= 0
                              && SpriteCutterDialog.this.selectedRegionIdx < SpriteCutterDialog.this.detectedRegions.size()) {
                              Rectangle r = SpriteCutterDialog.this.detectedRegions.get(SpriteCutterDialog.this.selectedRegionIdx);
                              SpriteCutterDialog.HandleType h = AtlasCanvas.this.getHandleAtScreen(sp, r);
                              if (h != SpriteCutterDialog.HandleType.NONE) {
                                 AtlasCanvas.this.activeHandle = h;
                                 AtlasCanvas.this.origRegionRect = new Rectangle(r);
                                 AtlasCanvas.this.repaint();
                                 return;
                              }
                           }

                           int clicked = AtlasCanvas.this.findRegionAt(ip);
                           if (clicked >= 0) {
                              SpriteCutterDialog.this.selectedRegionIdx = clicked;
                              SpriteCutterDialog.this.regionJList.setSelectedIndex(clicked);
                              SpriteCutterDialog.this.regionJList.ensureIndexIsVisible(clicked);
                              AtlasCanvas.this.activeHandle = SpriteCutterDialog.HandleType.MOVE;
                              AtlasCanvas.this.origRegionRect = new Rectangle(SpriteCutterDialog.this.detectedRegions.get(clicked));
                              AtlasCanvas.this.repaint();
                              return;
                           }

                           SpriteCutterDialog.this.selectedRegionIdx = -1;
                           SpriteCutterDialog.this.regionJList.clearSelection();
                           AtlasCanvas.this.activeHandle = SpriteCutterDialog.HandleType.NEW_CUT;
                           AtlasCanvas.this.currentNewCutRect = new Rectangle(ip.x, ip.y, 0, 0);
                           AtlasCanvas.this.repaint();
                        }
                     }
                  }
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                  if (AtlasCanvas.this.activeHandle == SpriteCutterDialog.HandleType.NEW_CUT
                     && AtlasCanvas.this.currentNewCutRect != null
                     && AtlasCanvas.this.currentNewCutRect.width >= 4
                     && AtlasCanvas.this.currentNewCutRect.height >= 4) {
                     int threshold = (Integer)SpriteCutterDialog.this.spThreshold.getValue();
                     Rectangle tight = SpriteCutterDialog.this.tightenRectangle(
                        SpriteCutterDialog.this.loadedImage,
                        AtlasCanvas.this.currentNewCutRect.x,
                        AtlasCanvas.this.currentNewCutRect.y,
                        AtlasCanvas.this.currentNewCutRect.width,
                        AtlasCanvas.this.currentNewCutRect.height,
                        threshold
                     );
                     if (tight != null) {
                        SpriteCutterDialog.this.detectedRegions.add(tight);
                     } else {
                        SpriteCutterDialog.this.detectedRegions.add(new Rectangle(AtlasCanvas.this.currentNewCutRect));
                     }

                     SpriteCutterDialog.this.updateStatus();
                     SpriteCutterDialog.this.selectedRegionIdx = SpriteCutterDialog.this.detectedRegions.size() - 1;
                     SpriteCutterDialog.this.regionJList.setSelectedIndex(SpriteCutterDialog.this.selectedRegionIdx);
                  }

                  AtlasCanvas.this.activeHandle = SpriteCutterDialog.HandleType.NONE;
                  AtlasCanvas.this.currentNewCutRect = null;
                  AtlasCanvas.this.dragStartScreen = null;
                  AtlasCanvas.this.dragStartImg = null;
                  AtlasCanvas.this.origRegionRect = null;
                  AtlasCanvas.this.setCursor(Cursor.getDefaultCursor());
                  AtlasCanvas.this.repaint();
               }
            }
         );
         this.addMouseMotionListener(
            new MouseMotionAdapter() {
               @Override
               public void mouseMoved(MouseEvent e) {
                  if (SpriteCutterDialog.this.loadedImage != null) {
                     Point sp = e.getPoint();
                     Point ip = AtlasCanvas.this.screenToImage(sp);
                     if (SpriteCutterDialog.this.selectedRegionIdx >= 0
                        && SpriteCutterDialog.this.selectedRegionIdx < SpriteCutterDialog.this.detectedRegions.size()) {
                        Rectangle r = SpriteCutterDialog.this.detectedRegions.get(SpriteCutterDialog.this.selectedRegionIdx);
                        SpriteCutterDialog.HandleType h = AtlasCanvas.this.getHandleAtScreen(sp, r);
                        AtlasCanvas.this.setCursor(AtlasCanvas.this.getCursorForHandle(h));
                     } else {
                        int hit = AtlasCanvas.this.findRegionAt(ip);
                        if (hit >= 0) {
                           AtlasCanvas.this.setCursor(Cursor.getPredefinedCursor(12));
                        } else {
                           AtlasCanvas.this.setCursor(Cursor.getPredefinedCursor(1));
                        }
                     }
                  }
               }

               @Override
               public void mouseDragged(MouseEvent e) {
                  if (SpriteCutterDialog.this.loadedImage != null && AtlasCanvas.this.dragStartScreen != null && AtlasCanvas.this.dragStartImg != null) {
                     Point sp = e.getPoint();
                     Point ip = AtlasCanvas.this.screenToImage(sp);
                     int iw = SpriteCutterDialog.this.loadedImage.getWidth();
                     int ih = SpriteCutterDialog.this.loadedImage.getHeight();
                     if (AtlasCanvas.this.activeHandle == SpriteCutterDialog.HandleType.NEW_CUT) {
                        int x = Math.min(AtlasCanvas.this.dragStartImg.x, ip.x);
                        int y = Math.min(AtlasCanvas.this.dragStartImg.y, ip.y);
                        int w = Math.abs(AtlasCanvas.this.dragStartImg.x - ip.x);
                        int h = Math.abs(AtlasCanvas.this.dragStartImg.y - ip.y);
                        AtlasCanvas.this.currentNewCutRect = new Rectangle(x, y, w, h);
                        AtlasCanvas.this.repaint();
                     } else if (SpriteCutterDialog.this.selectedRegionIdx >= 0
                        && SpriteCutterDialog.this.selectedRegionIdx < SpriteCutterDialog.this.detectedRegions.size()
                        && AtlasCanvas.this.origRegionRect != null) {
                        int dImgX = (int)Math.round((sp.x - AtlasCanvas.this.dragStartScreen.x) / SpriteCutterDialog.this.canvasZoom);
                        int dImgY = (int)Math.round((sp.y - AtlasCanvas.this.dragStartScreen.y) / SpriteCutterDialog.this.canvasZoom);
                        Rectangle r = new Rectangle(AtlasCanvas.this.origRegionRect);
                        switch (AtlasCanvas.this.activeHandle) {
                           case MOVE:
                              r.x = Math.max(0, Math.min(iw - r.width, AtlasCanvas.this.origRegionRect.x + dImgX));
                              r.y = Math.max(0, Math.min(ih - r.height, AtlasCanvas.this.origRegionRect.y + dImgY));
                              break;
                           case NW:
                              r.x = Math.max(
                                 0,
                                 Math.min(
                                    AtlasCanvas.this.origRegionRect.x + AtlasCanvas.this.origRegionRect.width - 4, AtlasCanvas.this.origRegionRect.x + dImgX
                                 )
                              );
                              r.y = Math.max(
                                 0,
                                 Math.min(
                                    AtlasCanvas.this.origRegionRect.y + AtlasCanvas.this.origRegionRect.height - 4, AtlasCanvas.this.origRegionRect.y + dImgY
                                 )
                              );
                              r.width = AtlasCanvas.this.origRegionRect.x + AtlasCanvas.this.origRegionRect.width - r.x;
                              r.height = AtlasCanvas.this.origRegionRect.y + AtlasCanvas.this.origRegionRect.height - r.y;
                              break;
                           case N:
                              r.y = Math.max(
                                 0,
                                 Math.min(
                                    AtlasCanvas.this.origRegionRect.y + AtlasCanvas.this.origRegionRect.height - 4, AtlasCanvas.this.origRegionRect.y + dImgY
                                 )
                              );
                              r.height = AtlasCanvas.this.origRegionRect.y + AtlasCanvas.this.origRegionRect.height - r.y;
                              break;
                           case NE:
                              r.y = Math.max(
                                 0,
                                 Math.min(
                                    AtlasCanvas.this.origRegionRect.y + AtlasCanvas.this.origRegionRect.height - 4, AtlasCanvas.this.origRegionRect.y + dImgY
                                 )
                              );
                              r.width = Math.max(4, Math.min(iw - AtlasCanvas.this.origRegionRect.x, AtlasCanvas.this.origRegionRect.width + dImgX));
                              r.height = AtlasCanvas.this.origRegionRect.y + AtlasCanvas.this.origRegionRect.height - r.y;
                              break;
                           case E:
                              r.width = Math.max(4, Math.min(iw - AtlasCanvas.this.origRegionRect.x, AtlasCanvas.this.origRegionRect.width + dImgX));
                              break;
                           case SE:
                              r.width = Math.max(4, Math.min(iw - AtlasCanvas.this.origRegionRect.x, AtlasCanvas.this.origRegionRect.width + dImgX));
                              r.height = Math.max(4, Math.min(ih - AtlasCanvas.this.origRegionRect.y, AtlasCanvas.this.origRegionRect.height + dImgY));
                              break;
                           case S:
                              r.height = Math.max(4, Math.min(ih - AtlasCanvas.this.origRegionRect.y, AtlasCanvas.this.origRegionRect.height + dImgY));
                              break;
                           case SW:
                              r.x = Math.max(
                                 0,
                                 Math.min(
                                    AtlasCanvas.this.origRegionRect.x + AtlasCanvas.this.origRegionRect.width - 4, AtlasCanvas.this.origRegionRect.x + dImgX
                                 )
                              );
                              r.width = AtlasCanvas.this.origRegionRect.x + AtlasCanvas.this.origRegionRect.width - r.x;
                              r.height = Math.max(4, Math.min(ih - AtlasCanvas.this.origRegionRect.y, AtlasCanvas.this.origRegionRect.height + dImgY));
                              break;
                           case W:
                              r.x = Math.max(
                                 0,
                                 Math.min(
                                    AtlasCanvas.this.origRegionRect.x + AtlasCanvas.this.origRegionRect.width - 4, AtlasCanvas.this.origRegionRect.x + dImgX
                                 )
                              );
                              r.width = AtlasCanvas.this.origRegionRect.x + AtlasCanvas.this.origRegionRect.width - r.x;
                        }

                        SpriteCutterDialog.this.detectedRegions.set(SpriteCutterDialog.this.selectedRegionIdx, r);
                        SpriteCutterDialog.this.regionModel.set(SpriteCutterDialog.this.selectedRegionIdx, r);
                        AtlasCanvas.this.repaint();
                     }
                  }
               }
            }
         );
         this.addMouseWheelListener(e -> {
            if (!e.isControlDown()) {
            }

            if (e.getWheelRotation() < 0) {
               SpriteCutterDialog.this.canvasZoom *= 1.15;
            } else {
               SpriteCutterDialog.this.canvasZoom /= 1.15;
            }

            SpriteCutterDialog.this.canvasZoom = Math.max(0.1, Math.min(20.0, SpriteCutterDialog.this.canvasZoom));
            this.revalidate();
            this.repaint();
         });
      }

      private int findRegionAt(Point ip) {
         for (int i = SpriteCutterDialog.this.detectedRegions.size() - 1; i >= 0; i--) {
            if (SpriteCutterDialog.this.detectedRegions.get(i).contains(ip)) {
               return i;
            }
         }

         return -1;
      }

      private SpriteCutterDialog.HandleType getHandleAtScreen(Point sp, Rectangle imgR) {
         int cx = this.getWidth() / 2;
         int cy = this.getHeight() / 2;
         int iw = SpriteCutterDialog.this.loadedImage.getWidth();
         int ih = SpriteCutterDialog.this.loadedImage.getHeight();
         int dw = (int)(iw * SpriteCutterDialog.this.canvasZoom);
         int dh = (int)(ih * SpriteCutterDialog.this.canvasZoom);
         int dx = cx - dw / 2;
         int dy = cy - dh / 2;
         int rx = dx + (int)(imgR.x * SpriteCutterDialog.this.canvasZoom);
         int ry = dy + (int)(imgR.y * SpriteCutterDialog.this.canvasZoom);
         int rw = (int)(imgR.width * SpriteCutterDialog.this.canvasZoom);
         int rh = (int)(imgR.height * SpriteCutterDialog.this.canvasZoom);
         int tol = 8;
         if (this.isNear(sp, rx, ry, tol)) {
            return SpriteCutterDialog.HandleType.NW;
         } else if (this.isNear(sp, rx + rw, ry, tol)) {
            return SpriteCutterDialog.HandleType.NE;
         } else if (this.isNear(sp, rx, ry + rh, tol)) {
            return SpriteCutterDialog.HandleType.SW;
         } else if (this.isNear(sp, rx + rw, ry + rh, tol)) {
            return SpriteCutterDialog.HandleType.SE;
         } else if (this.isNear(sp, rx + rw / 2, ry, tol)) {
            return SpriteCutterDialog.HandleType.N;
         } else if (this.isNear(sp, rx + rw / 2, ry + rh, tol)) {
            return SpriteCutterDialog.HandleType.S;
         } else if (this.isNear(sp, rx, ry + rh / 2, tol)) {
            return SpriteCutterDialog.HandleType.W;
         } else if (this.isNear(sp, rx + rw, ry + rh / 2, tol)) {
            return SpriteCutterDialog.HandleType.E;
         } else {
            return sp.x >= rx && sp.x <= rx + rw && sp.y >= ry && sp.y <= ry + rh ? SpriteCutterDialog.HandleType.MOVE : SpriteCutterDialog.HandleType.NONE;
         }
      }

      private boolean isNear(Point p, int x, int y, int tol) {
         return Math.abs(p.x - x) <= tol && Math.abs(p.y - y) <= tol;
      }

      private Cursor getCursorForHandle(SpriteCutterDialog.HandleType h) {
         switch (h) {
            case MOVE:
               return Cursor.getPredefinedCursor(13);
            case NW:
               return Cursor.getPredefinedCursor(6);
            case N:
               return Cursor.getPredefinedCursor(8);
            case NE:
               return Cursor.getPredefinedCursor(7);
            case E:
               return Cursor.getPredefinedCursor(11);
            case SE:
               return Cursor.getPredefinedCursor(5);
            case S:
               return Cursor.getPredefinedCursor(9);
            case SW:
               return Cursor.getPredefinedCursor(4);
            case W:
               return Cursor.getPredefinedCursor(10);
            default:
               return Cursor.getPredefinedCursor(1);
         }
      }

      private void showEmptyContextMenu(Component comp, int x, int y) {
         JPopupMenu menu = new JPopupMenu();
         menu.setBackground(new Color(28, 28, 38));
         JMenuItem mClear = new JMenuItem("\ud83d\uddd1 Xóa tất cả các Sprite");
         mClear.setForeground(new Color(255, 100, 100));
         mClear.addActionListener(e -> {
            SpriteCutterDialog.this.detectedRegions.clear();
            SpriteCutterDialog.this.selectedRegionIdx = -1;
            SpriteCutterDialog.this.updateStatus();
         });
         JMenuItem mScan = new JMenuItem("⚡ Quét Alpha tự động");
         mScan.setForeground(SpriteCutterDialog.FG);
         mScan.addActionListener(e -> SpriteCutterDialog.this.detectAlpha());
         menu.add(mScan);
         menu.add(mClear);
         menu.show(comp, x, y);
      }

      private Point screenToImage(Point sp) {
         int cx = this.getWidth() / 2;
         int cy = this.getHeight() / 2;
         int iw = SpriteCutterDialog.this.loadedImage == null ? 100 : SpriteCutterDialog.this.loadedImage.getWidth();
         int ih = SpriteCutterDialog.this.loadedImage == null ? 100 : SpriteCutterDialog.this.loadedImage.getHeight();
         double w = iw * SpriteCutterDialog.this.canvasZoom;
         double h = ih * SpriteCutterDialog.this.canvasZoom;
         double x = cx - w / 2.0;
         double y = cy - h / 2.0;
         int ix = (int)Math.floor((sp.x - x) / SpriteCutterDialog.this.canvasZoom);
         int iy = (int)Math.floor((sp.y - y) / SpriteCutterDialog.this.canvasZoom);
         return new Point(Math.max(0, Math.min(iw - 1, ix)), Math.max(0, Math.min(ih - 1, iy)));
      }

      @Override
      public Dimension getPreferredSize() {
         return SpriteCutterDialog.this.loadedImage == null
            ? new Dimension(800, 600)
            : new Dimension(
               (int)(SpriteCutterDialog.this.loadedImage.getWidth() * SpriteCutterDialog.this.canvasZoom) + 40,
               (int)(SpriteCutterDialog.this.loadedImage.getHeight() * SpriteCutterDialog.this.canvasZoom) + 40
            );
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (SpriteCutterDialog.this.loadedImage == null) {
            g.setColor(SpriteCutterDialog.FG_DIM);
            g.setFont(new Font("Segoe UI", 1, 16));
            g.drawString("Hãy load file ảnh SpriteSheet để bắt đầu cắt.", this.getWidth() / 2 - 150, this.getHeight() / 2);
         } else {
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            int cx = this.getWidth() / 2;
            int cy = this.getHeight() / 2;
            int iw = SpriteCutterDialog.this.loadedImage.getWidth();
            int ih = SpriteCutterDialog.this.loadedImage.getHeight();
            int dw = (int)(iw * SpriteCutterDialog.this.canvasZoom);
            int dh = (int)(ih * SpriteCutterDialog.this.canvasZoom);
            int dx = cx - dw / 2;
            int dy = cy - dh / 2;
            g2.setColor(new Color(15, 15, 20));
            g2.fillRect(dx, dy, dw, dh);
            g2.setColor(new Color(25, 25, 32));
            int cs = Math.max(4, (int)(8.0 * SpriteCutterDialog.this.canvasZoom));

            for (int y = dy; y < dy + dh; y += cs * 2) {
               for (int x = dx; x < dx + dw; x += cs * 2) {
                  g2.fillRect(x, y, cs, cs);
                  g2.fillRect(x + cs, y + cs, cs, cs);
               }
            }

            g2.drawImage(SpriteCutterDialog.this.loadedImage, dx, dy, dw, dh, null);

            for (int i = 0; i < SpriteCutterDialog.this.detectedRegions.size(); i++) {
               Rectangle r = SpriteCutterDialog.this.detectedRegions.get(i);
               int rx = dx + (int)(r.x * SpriteCutterDialog.this.canvasZoom);
               int ry = dy + (int)(r.y * SpriteCutterDialog.this.canvasZoom);
               int rw = (int)(r.width * SpriteCutterDialog.this.canvasZoom);
               int rh = (int)(r.height * SpriteCutterDialog.this.canvasZoom);
               boolean selected = i == SpriteCutterDialog.this.selectedRegionIdx;
               g2.setColor(selected ? new Color(255, 130, 0) : SpriteCutterDialog.ACCENT);
               g2.setStroke(new BasicStroke(selected ? 2.5F : 1.2F));
               g2.drawRect(rx, ry, rw, rh);
               g2.setFont(new Font("Segoe UI", 1, 10));
               g2.setColor(new Color(0, 0, 0, 180));
               g2.fillRoundRect(rx, ry, 22, 14, 3, 3);
               g2.setColor(Color.WHITE);
               g2.drawString("#" + i, rx + 3, ry + 11);
               if (selected) {
                  this.drawHandles(g2, rx, ry, rw, rh);
               }
            }

            if (this.currentNewCutRect != null) {
               int rx = dx + (int)(this.currentNewCutRect.x * SpriteCutterDialog.this.canvasZoom);
               int ry = dy + (int)(this.currentNewCutRect.y * SpriteCutterDialog.this.canvasZoom);
               int rw = (int)(this.currentNewCutRect.width * SpriteCutterDialog.this.canvasZoom);
               int rh = (int)(this.currentNewCutRect.height * SpriteCutterDialog.this.canvasZoom);
               g2.setColor(new Color(0, 255, 120));
               g2.setStroke(new BasicStroke(1.8F, 0, 2, 0.0F, new float[]{4.0F}, 0.0F));
               g2.drawRect(rx, ry, rw, rh);
               g2.setColor(new Color(0, 255, 120, 40));
               g2.fillRect(rx, ry, rw, rh);
            }
         }
      }

      private void drawHandles(Graphics2D g2, int rx, int ry, int rw, int rh) {
         int hs = 8;
         int hs2 = hs / 2;
         g2.setColor(Color.WHITE);
         g2.setStroke(new BasicStroke(1.2F));
         int[] xs = new int[]{rx, rx + rw / 2, rx + rw, rx + rw, rx + rw, rx + rw / 2, rx, rx};
         int[] ys = new int[]{ry, ry, ry, ry + rh / 2, ry + rh, ry + rh, ry + rh, ry + rh / 2};

         for (int i = 0; i < 8; i++) {
            g2.setColor(new Color(255, 140, 0));
            g2.fillRect(xs[i] - hs2, ys[i] - hs2, hs, hs);
            g2.setColor(Color.WHITE);
            g2.drawRect(xs[i] - hs2, ys[i] - hs2, hs, hs);
         }
      }
   }

   private static enum HandleType {
      NONE,
      MOVE,
      NW,
      N,
      NE,
      E,
      SE,
      S,
      SW,
      W,
      NEW_CUT;
   }

   private class RegionCellRenderer extends JPanel implements ListCellRenderer<Rectangle> {
      private final JLabel icon = new JLabel("", 0);
      private final JLabel info = new JLabel();
      private final JLabel tag = new JLabel("", 0);

      RegionCellRenderer() {
         this.setLayout(new BorderLayout(8, 0));
         this.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, SpriteCutterDialog.BORDER), new EmptyBorder(4, 6, 4, 6)));
         this.setOpaque(true);
         this.tag.setFont(new Font("Segoe UI", 1, 11));
         this.tag.setPreferredSize(new Dimension(28, 0));
         this.tag.setForeground(Color.ORANGE);
         this.icon.setPreferredSize(new Dimension(34, 34));
         this.icon.setOpaque(true);
         this.icon.setBackground(new Color(10, 10, 15));
         this.icon.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 60)));
         this.info.setFont(new Font("Segoe UI", 0, 10));
         this.info.setForeground(SpriteCutterDialog.FG);
         this.add(this.tag, "West");
         this.add(this.icon, "Center");
         this.add(this.info, "East");
      }

      public Component getListCellRendererComponent(JList<? extends Rectangle> list, Rectangle r, int index, boolean isSelected, boolean cellHasFocus) {
         this.setBackground(isSelected ? new Color(0, 95, 210) : new Color(20, 20, 26));
         this.tag.setText("#" + index);
         this.info.setText(String.format("<html>X:%d Y:%d<br><b>W:%d H:%d</b></html>", r.x, r.y, r.width, r.height));
         if (SpriteCutterDialog.this.loadedImage != null && r.width > 0 && r.height > 0) {
            try {
               int cx = Math.max(0, r.x);
               int cy = Math.max(0, r.y);
               int cw = Math.min(r.width, SpriteCutterDialog.this.loadedImage.getWidth() - cx);
               int ch = Math.min(r.height, SpriteCutterDialog.this.loadedImage.getHeight() - cy);
               if (cw > 0 && ch > 0) {
                  BufferedImage sub = SpriteCutterDialog.this.loadedImage.getSubimage(cx, cy, cw, ch);
                  double scale = Math.min(30.0 / cw, 30.0 / ch);
                  if (scale > 1.0) {
                     scale = 1.0;
                  }

                  int sw = Math.max(1, (int)(cw * scale));
                  int sh = Math.max(1, (int)(ch * scale));
                  this.icon.setIcon(new ImageIcon(sub.getScaledInstance(sw, sh, 2)));
               }
            } catch (Exception var15) {
               this.icon.setIcon(null);
            }
         }

         return this;
      }
   }
}
