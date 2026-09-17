package com.deplor.haitactihontool.tool;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class ImageEditorPanel extends JPanel {
   private BufferedImage currentImg;
   private final Deque<BufferedImage> undoStack = new ArrayDeque<>();
   private final Deque<BufferedImage> redoStack = new ArrayDeque<>();
   private static final int MAX_HISTORY = 25;
   private double zoomLevel = 4.0;
   private boolean showGrid = true;
   private String currentTool = "pencil";
   private int brushSize = 1;
   private Color brushColor = new Color(255, 0, 0, 255);
   private int tolerance = 25;
   private Point cropStart = null;
   private Rectangle cropBox = null;
   private ImageEditorPanel.EditorCanvas canvas;
   private JComboBox<String> cbZoom;
   private JCheckBox chkGrid;
   private JButton btnColorPreview;
   private JSpinner spinBrushSize;
   private JSpinner spinTolerance;
   private JLabel lblStatus;
   private final Map<String, JButton> toolBtns = new HashMap<>();

   public ImageEditorPanel() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      this.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.buildUI();
      this.createNewBlank(32, 32);
   }

   private void buildUI() {
      JPanel topBar = new JPanel(new BorderLayout(8, 0));
      topBar.setBackground(Theme.BG_DARK);
      topBar.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER), new EmptyBorder(6, 10, 6, 10)));
      JPanel leftActions = new JPanel(new FlowLayout(0, 4, 0));
      leftActions.setOpaque(false);
      JButton btnOpen = StyledUI.createButton("\ud83d\udcc1 Mở Ảnh...", Theme.BG_CARD);
      btnOpen.addActionListener(e -> this.loadImage());
      leftActions.add(btnOpen);
      JButton btnSave = StyledUI.createButton("\ud83d\udcbe Lưu PNG (Alpha)", Theme.ACCENT);
      btnSave.addActionListener(e -> this.saveImage());
      leftActions.add(btnSave);
      JButton btnPaste = StyledUI.createButton("\ud83d\udccb Dán (Ctrl+V)", Theme.BG_CARD);
      btnPaste.setForeground(Theme.SUCCESS);
      btnPaste.addActionListener(e -> this.pasteFromClipboard());
      leftActions.add(btnPaste);
      JButton btnCopy = StyledUI.createButton("\ud83d\udcd1 Sao Chép", Theme.BG_CARD);
      btnCopy.addActionListener(e -> this.copyToClipboard());
      leftActions.add(btnCopy);
      JButton btnNew = StyledUI.createButton("\ud83c\udd95 Tạo Ảnh Mới", Theme.BG_CARD);
      btnNew.setForeground(Theme.WARNING);
      btnNew.addActionListener(e -> this.promptNewCanvas());
      leftActions.add(btnNew);
      JButton btnUndo = StyledUI.createButton("↶ Undo", Theme.BG_CARD);
      btnUndo.addActionListener(e -> this.undo());
      leftActions.add(btnUndo);
      JButton btnRedo = StyledUI.createButton("↷ Redo", Theme.BG_CARD);
      btnRedo.addActionListener(e -> this.redo());
      leftActions.add(btnRedo);
      topBar.add(leftActions, "West");
      JPanel rightZoom = new JPanel(new FlowLayout(2, 6, 0));
      rightZoom.setOpaque(false);
      rightZoom.add(StyledUI.createLabel("Zoom:", Theme.F_SMALL, Theme.TEXT_MUTED));
      this.cbZoom = new JComboBox<>(new String[]{"100%", "200%", "300%", "400%", "600%", "800%", "1200%", "1600%"});
      this.cbZoom.setSelectedIndex(3);
      StyledUI.styleComboBox(this.cbZoom);
      this.cbZoom.addActionListener(e -> this.onZoomChanged());
      rightZoom.add(this.cbZoom);
      this.chkGrid = StyledUI.createCheckBox("Pixel Grid");
      this.chkGrid.setSelected(true);
      this.chkGrid.addActionListener(e -> {
         this.showGrid = this.chkGrid.isSelected();
         this.canvas.repaint();
      });
      rightZoom.add(this.chkGrid);
      topBar.add(rightZoom, "East");
      this.add(topBar, "North");
      JPanel center = new JPanel(new BorderLayout(6, 0));
      center.setOpaque(false);
      center.setBorder(new EmptyBorder(6, 0, 6, 0));
      center.add(this.buildLeftToolbox(), "West");
      this.canvas = new ImageEditorPanel.EditorCanvas();
      JScrollPane spCanvas = new JScrollPane(this.canvas);
      StyledUI.styleScrollPane(spCanvas);
      center.add(spCanvas, "Center");
      center.add(this.buildRightActionPanel(), "East");
      this.add(center, "Center");
      this.lblStatus = StyledUI.createLabel("Size: 32x32 px | Zoom: 400% | Mode: ARGB", Theme.F_TINY, Theme.TEXT_MUTED);
      this.add(this.lblStatus, "South");
   }

   private JPanel buildLeftToolbox() {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, 1));
      p.setBackground(Theme.BG_DARK);
      p.setPreferredSize(new Dimension(130, 0));
      p.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER), new EmptyBorder(8, 8, 8, 8)));
      p.add(StyledUI.createLabel("CÔNG CỤ VẼ", Theme.F_BOLD, Theme.ACCENT));
      p.add(Box.createVerticalStrut(6));
      String[][] tools = new String[][]{
         {"pencil", "✏️ Cọ Vẽ (Pencil)"},
         {"eraser", "\ud83e\uddfd Tẩy (Eraser)"},
         {"wand", "\ud83e\ude84 Xóa Nền (Wand)"},
         {"bucket", "\ud83e\udea3 Đổ Màu (Fill)"},
         {"picker", "\ud83e\uddea Hút Màu (Pick)"},
         {"crop", "✂️ Cắt Chọn (Crop)"}
      };

      for (String[] t : tools) {
         String tid = t[0];
         JButton btn = StyledUI.createButton(t[1], tid.equals(this.currentTool) ? Theme.ACCENT : Theme.BG_CARD);
         btn.setHorizontalAlignment(2);
         btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
         btn.addActionListener(e -> this.selectTool(tid));
         this.toolBtns.put(tid, btn);
         p.add(btn);
         p.add(Box.createVerticalStrut(3));
      }

      p.add(Box.createVerticalStrut(8));
      p.add(StyledUI.createLabel("Cỡ Cọ (Brush):", Theme.F_TINY, Theme.TEXT_MUTED));
      this.spinBrushSize = new JSpinner(new SpinnerNumberModel(1, 1, 32, 1));
      this.spinBrushSize.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
      this.spinBrushSize.addChangeListener(e -> this.brushSize = (Integer)this.spinBrushSize.getValue());
      p.add(this.spinBrushSize);
      p.add(Box.createVerticalStrut(8));
      p.add(StyledUI.createLabel("Màu Vẽ (Color):", Theme.F_TINY, Theme.TEXT_MUTED));
      this.btnColorPreview = new JButton();
      this.btnColorPreview.setBackground(this.brushColor);
      this.btnColorPreview.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
      this.btnColorPreview.addActionListener(e -> this.pickColor());
      p.add(this.btnColorPreview);
      p.add(Box.createVerticalStrut(4));
      JPanel pal = new JPanel(new GridLayout(2, 4, 2, 2));
      pal.setOpaque(false);
      Color[] quickColors = new Color[]{Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, new Color(0, 136, 255), Color.YELLOW, Color.MAGENTA, Color.ORANGE};

      for (Color qc : quickColors) {
         JButton qb = new JButton();
         qb.setBackground(qc);
         qb.setPreferredSize(new Dimension(20, 20));
         qb.addActionListener(e -> this.setBrushColor(qc));
         pal.add(qb);
      }

      pal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
      p.add(pal);
      p.add(Box.createVerticalStrut(8));
      p.add(StyledUI.createLabel("Dung Sai (Tol %):", Theme.F_TINY, Theme.TEXT_MUTED));
      this.spinTolerance = new JSpinner(new SpinnerNumberModel(25, 0, 100, 1));
      this.spinTolerance.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
      this.spinTolerance.addChangeListener(e -> this.tolerance = (Integer)this.spinTolerance.getValue());
      p.add(this.spinTolerance);
      p.add(Box.createVerticalGlue());
      return p;
   }

   private JPanel buildRightActionPanel() {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, 1));
      p.setBackground(Theme.BG_DARK);
      p.setPreferredSize(new Dimension(210, 0));
      p.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER), new EmptyBorder(10, 10, 10, 10)));
      p.add(StyledUI.createLabel("\ud83e\ude84 XÓA NỀN THÔNG MINH", Theme.F_BOLD, Theme.SUCCESS));
      p.add(Box.createVerticalStrut(4));
      JButton btnAutoBorder = StyledUI.createButton("⚡ Tự Động Xóa Nền 4 Viền", Theme.BG_CARD);
      btnAutoBorder.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
      btnAutoBorder.addActionListener(e -> this.autoRemoveBorderBg());
      p.add(btnAutoBorder);
      p.add(Box.createVerticalStrut(3));
      JButton btnColorKey = StyledUI.createButton("\ud83c\udfa8 Xóa Màu Đang Chọn (Chroma)", Theme.BG_CARD);
      btnColorKey.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
      btnColorKey.addActionListener(e -> this.removeSelectedColorBg());
      p.add(btnColorKey);
      p.add(Box.createVerticalStrut(3));
      JButton btnTrim = StyledUI.createButton("✂️ Auto Trim Viền Trong Suốt", Theme.BG_CARD);
      btnTrim.setForeground(Theme.WARNING);
      btnTrim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
      btnTrim.addActionListener(e -> this.autoTrimTransparent());
      p.add(btnTrim);
      p.add(Box.createVerticalStrut(3));
      JButton btnApplyCrop = StyledUI.createButton("✂️ Cắt Vùng Chọn (Crop)", Theme.ACCENT);
      btnApplyCrop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
      btnApplyCrop.addActionListener(e -> this.applyCrop());
      p.add(btnApplyCrop);
      p.add(Box.createVerticalStrut(10));
      p.add(StyledUI.createLabel("✨ TẠO VIỀN SPRITE (OUTLINE)", Theme.F_BOLD, Theme.WARNING));
      p.add(Box.createVerticalStrut(4));
      JButton btnOutBlack = StyledUI.createButton("\ud83d\udda4 Viền Đen 1px (Game Outline)", Theme.BG_CARD);
      btnOutBlack.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
      btnOutBlack.addActionListener(e -> this.addSpriteOutline(Color.BLACK, 1));
      p.add(btnOutBlack);
      p.add(Box.createVerticalStrut(3));
      JButton btnOutGold = StyledUI.createButton("\ud83d\udc9b Viền Vàng Kim (Glow Gold)", Theme.BG_CARD);
      btnOutGold.setForeground(new Color(16766720));
      btnOutGold.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
      btnOutGold.addActionListener(e -> this.addSpriteOutline(new Color(255, 215, 0), 1));
      p.add(btnOutGold);
      p.add(Box.createVerticalStrut(3));
      JButton btnOutCyan = StyledUI.createButton("\ud83d\udc99 Viền Xanh Cyan (Glow)", Theme.BG_CARD);
      btnOutCyan.setForeground(new Color(58879));
      btnOutCyan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
      btnOutCyan.addActionListener(e -> this.addSpriteOutline(new Color(0, 229, 255), 1));
      p.add(btnOutCyan);
      p.add(Box.createVerticalStrut(10));
      p.add(StyledUI.createLabel("\ud83d\udd04 XOAY & LẬT", Theme.F_BOLD, Theme.ACCENT));
      p.add(Box.createVerticalStrut(4));
      JPanel rRot = new JPanel(new GridLayout(1, 3, 2, 0));
      rRot.setOpaque(false);
      JButton btnR90 = StyledUI.createButton("↶ 90°", Theme.BG_CARD);
      btnR90.addActionListener(e -> this.rotateImage(270));
      rRot.add(btnR90);
      JButton btnL90 = StyledUI.createButton("↷ 90°", Theme.BG_CARD);
      btnL90.addActionListener(e -> this.rotateImage(90));
      rRot.add(btnL90);
      JButton btn180 = StyledUI.createButton("180°", Theme.BG_CARD);
      btn180.addActionListener(e -> this.rotateImage(180));
      rRot.add(btn180);
      rRot.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
      p.add(rRot);
      p.add(Box.createVerticalStrut(3));
      JPanel rFlip = new JPanel(new GridLayout(1, 2, 2, 0));
      rFlip.setOpaque(false);
      JButton btnFlH = StyledUI.createButton("↔ Lật Ngang", Theme.BG_CARD);
      btnFlH.addActionListener(e -> this.flipHorizontal());
      rFlip.add(btnFlH);
      JButton btnFlV = StyledUI.createButton("↕ Lật Dọc", Theme.BG_CARD);
      btnFlV.addActionListener(e -> this.flipVertical());
      rFlip.add(btnFlV);
      rFlip.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
      p.add(rFlip);
      p.add(Box.createVerticalStrut(10));
      p.add(StyledUI.createLabel("\ud83e\udde9 SPRITE SHEET CẮT / GHÉP", Theme.F_BOLD, Theme.TEXT_MAIN));
      p.add(Box.createVerticalStrut(4));
      JButton btnSlice = StyledUI.createButton("\ud83d\udce6 Cắt Lưới Xuất Frames...", Theme.BG_CARD);
      btnSlice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
      btnSlice.addActionListener(e -> this.exportSpriteSheetFrames());
      p.add(btnSlice);
      p.add(Box.createVerticalStrut(3));
      JButton btnMerge = StyledUI.createButton("➕ Ghép Nhiều Ảnh Thành Sheet...", Theme.BG_CARD);
      btnMerge.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
      btnMerge.addActionListener(e -> this.mergeImagesToSheet());
      p.add(btnMerge);
      p.add(Box.createVerticalGlue());
      return p;
   }

   private void pushUndo() {
      if (this.currentImg != null) {
         BufferedImage copy = new BufferedImage(this.currentImg.getWidth(), this.currentImg.getHeight(), 2);
         Graphics2D g = copy.createGraphics();
         g.drawImage(this.currentImg, 0, 0, null);
         g.dispose();
         this.undoStack.push(copy);
         if (this.undoStack.size() > 25) {
            this.undoStack.removeLast();
         }

         this.redoStack.clear();
      }
   }

   private void undo() {
      if (!this.undoStack.isEmpty()) {
         BufferedImage copy = new BufferedImage(this.currentImg.getWidth(), this.currentImg.getHeight(), 2);
         Graphics2D g = copy.createGraphics();
         g.drawImage(this.currentImg, 0, 0, null);
         g.dispose();
         this.redoStack.push(copy);
         this.currentImg = this.undoStack.pop();
         this.canvas.recalcSize();
         this.canvas.repaint();
         this.updateStatus();
      }
   }

   private void redo() {
      if (!this.redoStack.isEmpty()) {
         BufferedImage copy = new BufferedImage(this.currentImg.getWidth(), this.currentImg.getHeight(), 2);
         Graphics2D g = copy.createGraphics();
         g.drawImage(this.currentImg, 0, 0, null);
         g.dispose();
         this.undoStack.push(copy);
         this.currentImg = this.redoStack.pop();
         this.canvas.recalcSize();
         this.canvas.repaint();
         this.updateStatus();
      }
   }

   private void createNewBlank(int w, int h) {
      this.pushUndo();
      this.currentImg = new BufferedImage(Math.max(1, w), Math.max(1, h), 2);
      this.canvas.recalcSize();
      this.canvas.repaint();
      this.updateStatus();
   }

   private void promptNewCanvas() {
      JTextField txtW = new JTextField("32", 5);
      JTextField txtH = new JTextField("32", 5);
      JPanel p = new JPanel(new FlowLayout());
      p.add(new JLabel("Width:"));
      p.add(txtW);
      p.add(new JLabel("Height:"));
      p.add(txtH);
      int res = JOptionPane.showConfirmDialog(this, p, "Tạo Ảnh Mới", 2);
      if (res == 0) {
         try {
            int w = Integer.parseInt(txtW.getText().trim());
            int h = Integer.parseInt(txtH.getText().trim());
            if (w > 0 && h > 0) {
               this.createNewBlank(w, h);
            }
         } catch (Exception var7) {
         }
      }
   }

   private void selectTool(String toolId) {
      this.currentTool = toolId;

      for (Entry<String, JButton> entry : this.toolBtns.entrySet()) {
         entry.getValue().setBackground(entry.getKey().equals(toolId) ? Theme.ACCENT : Theme.BG_CARD);
      }
   }

   private void setBrushColor(Color c) {
      this.brushColor = c;
      this.btnColorPreview.setBackground(c);
   }

   private void pickColor() {
      Color c = JColorChooser.showDialog(this, "Chọn Màu Vẽ", this.brushColor);
      if (c != null) {
         this.setBrushColor(c);
      }
   }

   private void onZoomChanged() {
      String s = (String)this.cbZoom.getSelectedItem();
      if (s != null) {
         this.zoomLevel = Double.parseDouble(s.replace("%", "").trim()) / 100.0;
         this.canvas.recalcSize();
         this.canvas.repaint();
         this.updateStatus();
      }
   }

   private void updateStatus() {
      if (this.currentImg != null) {
         this.lblStatus
            .setText(
               "Size: "
                  + this.currentImg.getWidth()
                  + "x"
                  + this.currentImg.getHeight()
                  + " px | Zoom: "
                  + (int)(this.zoomLevel * 100.0)
                  + "% | Cọ: "
                  + this.currentTool
                  + " ("
                  + this.brushSize
                  + "px)"
            );
      }
   }

   private void autoRemoveBorderBg() {
      if (this.currentImg != null) {
         this.pushUndo();
         int w = this.currentImg.getWidth();
         int h = this.currentImg.getHeight();
         long sumR = 0L;
         long sumG = 0L;
         long sumB = 0L;
         int count = 0;

         for (int x = 0; x < w; x++) {
            Color c1 = new Color(this.currentImg.getRGB(x, 0), true);
            Color c2 = new Color(this.currentImg.getRGB(x, h - 1), true);
            sumR += c1.getRed() + c2.getRed();
            sumG += c1.getGreen() + c2.getGreen();
            sumB += c1.getBlue() + c2.getBlue();
            count += 2;
         }

         for (int y = 0; y < h; y++) {
            Color c1 = new Color(this.currentImg.getRGB(0, y), true);
            Color c2 = new Color(this.currentImg.getRGB(w - 1, y), true);
            sumR += c1.getRed() + c2.getRed();
            sumG += c1.getGreen() + c2.getGreen();
            sumB += c1.getBlue() + c2.getBlue();
            count += 2;
         }

         int avgR = (int)(sumR / count);
         int avgG = (int)(sumG / count);
         int avgB = (int)(sumB / count);
         int tolVal = (int)(this.tolerance / 100.0 * 255.0);

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               Color c = new Color(this.currentImg.getRGB(x, y), true);
               if (Math.abs(c.getRed() - avgR) <= tolVal && Math.abs(c.getGreen() - avgG) <= tolVal && Math.abs(c.getBlue() - avgB) <= tolVal) {
                  this.currentImg.setRGB(x, y, 0);
               }
            }
         }

         this.canvas.repaint();
         JOptionPane.showMessageDialog(this, "Đã quét và xóa nền viền thành công!");
      }
   }

   private void removeSelectedColorBg() {
      if (this.currentImg != null) {
         this.pushUndo();
         int w = this.currentImg.getWidth();
         int h = this.currentImg.getHeight();
         int tr = this.brushColor.getRed();
         int tg = this.brushColor.getGreen();
         int tb = this.brushColor.getBlue();
         int tolVal = (int)(this.tolerance / 100.0 * 255.0);

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               Color c = new Color(this.currentImg.getRGB(x, y), true);
               if (Math.abs(c.getRed() - tr) <= tolVal && Math.abs(c.getGreen() - tg) <= tolVal && Math.abs(c.getBlue() - tb) <= tolVal) {
                  this.currentImg.setRGB(x, y, 0);
               }
            }
         }

         this.canvas.repaint();
      }
   }

   private void autoTrimTransparent() {
      if (this.currentImg != null) {
         int w = this.currentImg.getWidth();
         int h = this.currentImg.getHeight();
         int minX = w;
         int maxX = -1;
         int minY = h;
         int maxY = -1;

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               int alpha = this.currentImg.getRGB(x, y) >> 24 & 0xFF;
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

         if (minX <= maxX && minY <= maxY) {
            this.pushUndo();
            this.currentImg = this.currentImg.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
            this.canvas.recalcSize();
            this.canvas.repaint();
            this.updateStatus();
         } else {
            JOptionPane.showMessageDialog(this, "Ảnh hoàn toàn trong suốt!");
         }
      }
   }

   private void applyCrop() {
      if (this.cropBox != null && this.currentImg != null) {
         this.pushUndo();
         int x = Math.max(0, this.cropBox.x);
         int y = Math.max(0, this.cropBox.y);
         int w = Math.min(this.currentImg.getWidth() - x, this.cropBox.width);
         int h = Math.min(this.currentImg.getHeight() - y, this.cropBox.height);
         if (w > 0 && h > 0) {
            this.currentImg = this.currentImg.getSubimage(x, y, w, h);
            this.cropBox = null;
            this.canvas.recalcSize();
            this.canvas.repaint();
            this.updateStatus();
         }
      }
   }

   private void addSpriteOutline(Color outlineColor, int thickness) {
      if (this.currentImg != null) {
         this.pushUndo();
         int w = this.currentImg.getWidth();
         int h = this.currentImg.getHeight();
         BufferedImage out = new BufferedImage(w, h, 2);
         Graphics2D g2 = out.createGraphics();

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               int a = this.currentImg.getRGB(x, y) >> 24 & 0xFF;
               if (a == 0) {
                  boolean hasOpaque = false;

                  for (int dx = -thickness; dx <= thickness && !hasOpaque; dx++) {
                     for (int dy = -thickness; dy <= thickness && !hasOpaque; dy++) {
                        int nx = x + dx;
                        int ny = y + dy;
                        if (nx >= 0 && nx < w && ny >= 0 && ny < h && (this.currentImg.getRGB(nx, ny) >> 24 & 0xFF) > 0) {
                           hasOpaque = true;
                        }
                     }
                  }

                  if (hasOpaque) {
                     out.setRGB(x, y, outlineColor.getRGB());
                  }
               }
            }
         }

         g2.drawImage(this.currentImg, 0, 0, null);
         g2.dispose();
         this.currentImg = out;
         this.canvas.repaint();
      }
   }

   private void rotateImage(int angle) {
      if (this.currentImg != null) {
         this.pushUndo();
         int w = this.currentImg.getWidth();
         int h = this.currentImg.getHeight();
         BufferedImage rotated;
         if (angle == 90) {
            rotated = new BufferedImage(h, w, 2);

            for (int x = 0; x < w; x++) {
               for (int y = 0; y < h; y++) {
                  rotated.setRGB(h - 1 - y, x, this.currentImg.getRGB(x, y));
               }
            }
         } else if (angle == 270) {
            rotated = new BufferedImage(h, w, 2);

            for (int x = 0; x < w; x++) {
               for (int y = 0; y < h; y++) {
                  rotated.setRGB(y, w - 1 - x, this.currentImg.getRGB(x, y));
               }
            }
         } else {
            rotated = new BufferedImage(w, h, 2);

            for (int x = 0; x < w; x++) {
               for (int y = 0; y < h; y++) {
                  rotated.setRGB(w - 1 - x, h - 1 - y, this.currentImg.getRGB(x, y));
               }
            }
         }

         this.currentImg = rotated;
         this.canvas.recalcSize();
         this.canvas.repaint();
         this.updateStatus();
      }
   }

   private void flipHorizontal() {
      if (this.currentImg != null) {
         this.pushUndo();
         int w = this.currentImg.getWidth();
         int h = this.currentImg.getHeight();
         BufferedImage flipped = new BufferedImage(w, h, 2);

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               flipped.setRGB(w - 1 - x, y, this.currentImg.getRGB(x, y));
            }
         }

         this.currentImg = flipped;
         this.canvas.repaint();
      }
   }

   private void flipVertical() {
      if (this.currentImg != null) {
         this.pushUndo();
         int w = this.currentImg.getWidth();
         int h = this.currentImg.getHeight();
         BufferedImage flipped = new BufferedImage(w, h, 2);

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               flipped.setRGB(x, h - 1 - y, this.currentImg.getRGB(x, y));
            }
         }

         this.currentImg = flipped;
         this.canvas.repaint();
      }
   }

   private void exportSpriteSheetFrames() {
      if (this.currentImg != null) {
         JTextField txtFW = new JTextField("32", 5);
         JTextField txtFH = new JTextField("32", 5);
         JPanel p = new JPanel(new FlowLayout());
         p.add(new JLabel("Frame Width:"));
         p.add(txtFW);
         p.add(new JLabel("Frame Height:"));
         p.add(txtFH);
         int res = JOptionPane.showConfirmDialog(this, p, "Kích Thước Frame Cắt", 2);
         if (res == 0) {
            try {
               int fw = Integer.parseInt(txtFW.getText().trim());
               int fh = Integer.parseInt(txtFH.getText().trim());
               if (fw <= 0 || fh <= 0) {
                  return;
               }

               JFileChooser fc = new JFileChooser(".");
               fc.setFileSelectionMode(1);
               if (fc.showOpenDialog(this) == 0) {
                  File outDir = fc.getSelectedFile();
                  int w = this.currentImg.getWidth();
                  int h = this.currentImg.getHeight();
                  int cols = w / fw;
                  int rows = h / fh;
                  int count = 0;

                  for (int r = 0; r < rows; r++) {
                     for (int c = 0; c < cols; c++) {
                        BufferedImage frame = this.currentImg.getSubimage(c * fw, r * fh, fw, fh);
                        ImageIO.write(frame, "PNG", new File(outDir, String.format("frame_%03d.png", count++)));
                     }
                  }

                  JOptionPane.showMessageDialog(this, "Đã xuất " + count + " frame thành công!");
               }
            } catch (Exception var17) {
               JOptionPane.showMessageDialog(this, "Lỗi cắt frames: " + var17.getMessage());
            }
         }
      }
   }

   private void mergeImagesToSheet() {
      JFileChooser fc = new JFileChooser(".");
      fc.setMultiSelectionEnabled(true);
      if (fc.showOpenDialog(this) == 0) {
         File[] files = fc.getSelectedFiles();
         if (files.length == 0) {
            return;
         }

         try {
            List<BufferedImage> images = new ArrayList<>();
            int maxW = 0;
            int maxH = 0;

            for (File f : files) {
               BufferedImage img = ImageIO.read(f);
               if (img != null) {
                  images.add(img);
                  maxW = Math.max(maxW, img.getWidth());
                  maxH = Math.max(maxH, img.getHeight());
               }
            }

            if (images.isEmpty()) {
               return;
            }

            int cols = (int)Math.ceil(Math.sqrt(images.size()));
            int rows = (int)Math.ceil((double)images.size() / cols);
            this.pushUndo();
            BufferedImage sheet = new BufferedImage(cols * maxW, rows * maxH, 2);
            Graphics2D g = sheet.createGraphics();

            for (int i = 0; i < images.size(); i++) {
               int c = i % cols;
               int r = i / cols;
               g.drawImage(images.get(i), c * maxW, r * maxH, null);
            }

            g.dispose();
            this.currentImg = sheet;
            this.canvas.recalcSize();
            this.canvas.repaint();
            this.updateStatus();
            JOptionPane.showMessageDialog(this, "Đã ghép " + images.size() + " ảnh thành Sprite Sheet!");
         } catch (Exception var13) {
            JOptionPane.showMessageDialog(this, "Lỗi ghép ảnh: " + var13.getMessage());
         }
      }
   }

   private void loadImage() {
      JFileChooser fc = new JFileChooser(".");
      if (fc.showOpenDialog(this) == 0) {
         try {
            BufferedImage img = ImageIO.read(fc.getSelectedFile());
            if (img != null) {
               this.pushUndo();
               BufferedImage argb = new BufferedImage(img.getWidth(), img.getHeight(), 2);
               Graphics2D g = argb.createGraphics();
               g.drawImage(img, 0, 0, null);
               g.dispose();
               this.currentImg = argb;
               this.canvas.recalcSize();
               this.canvas.repaint();
               this.updateStatus();
            }
         } catch (Exception var5) {
            JOptionPane.showMessageDialog(this, "Lỗi mở ảnh: " + var5.getMessage());
         }
      }
   }

   private void saveImage() {
      if (this.currentImg != null) {
         JFileChooser fc = new JFileChooser(".");
         fc.setSelectedFile(new File("icon_output.png"));
         if (fc.showSaveDialog(this) == 0) {
            try {
               File out = fc.getSelectedFile();
               if (!out.getName().toLowerCase().endsWith(".png")) {
                  out = new File(out.getAbsolutePath() + ".png");
               }

               ImageIO.write(this.currentImg, "PNG", out);
               JOptionPane.showMessageDialog(this, "Đã lưu ảnh trong suốt PNG thành công!");
            } catch (Exception var3) {
               JOptionPane.showMessageDialog(this, "Lỗi lưu ảnh: " + var3.getMessage());
            }
         }
      }
   }

   private void copyToClipboard() {
      if (this.currentImg != null) {
         ImageEditorPanel.TransferableImage trans = new ImageEditorPanel.TransferableImage(this.currentImg);
         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
         JOptionPane.showMessageDialog(this, "Đã sao chép ảnh vào Clipboard!");
      }
   }

   private void pasteFromClipboard() {
      Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
      if (trans != null && trans.isDataFlavorSupported(DataFlavor.imageFlavor)) {
         try {
            Image img = (Image)trans.getTransferData(DataFlavor.imageFlavor);
            if (img != null) {
               this.pushUndo();
               BufferedImage argb = new BufferedImage(img.getWidth(null), img.getHeight(null), 2);
               Graphics2D g = argb.createGraphics();
               g.drawImage(img, 0, 0, null);
               g.dispose();
               this.currentImg = argb;
               this.canvas.recalcSize();
               this.canvas.repaint();
               this.updateStatus();
               JOptionPane.showMessageDialog(this, "Đã dán ảnh từ Clipboard thành công!");
            }
         } catch (Exception var5) {
            JOptionPane.showMessageDialog(this, "Lỗi dán ảnh: " + var5.getMessage());
         }
      }
   }

   private class EditorCanvas extends JPanel {
      public EditorCanvas() {
         this.setBackground(new Color(16, 16, 24));
         MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               EditorCanvas.this.handlePress(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
               EditorCanvas.this.handleDrag(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               EditorCanvas.this.handleRelease(e);
            }
         };
         this.addMouseListener(ma);
         this.addMouseMotionListener(ma);
      }

      public void recalcSize() {
         if (ImageEditorPanel.this.currentImg != null) {
            int dw = (int)(ImageEditorPanel.this.currentImg.getWidth() * ImageEditorPanel.this.zoomLevel);
            int dh = (int)(ImageEditorPanel.this.currentImg.getHeight() * ImageEditorPanel.this.zoomLevel);
            this.setPreferredSize(new Dimension(dw + 40, dh + 40));
            this.revalidate();
         }
      }

      private Point toImgCoords(Point p) {
         if (ImageEditorPanel.this.currentImg == null) {
            return null;
         } else {
            int x = (int)((p.x - 20) / ImageEditorPanel.this.zoomLevel);
            int y = (int)((p.y - 20) / ImageEditorPanel.this.zoomLevel);
            return x >= 0 && x < ImageEditorPanel.this.currentImg.getWidth() && y >= 0 && y < ImageEditorPanel.this.currentImg.getHeight()
               ? new Point(x, y)
               : null;
         }
      }

      private void handlePress(MouseEvent e) {
         Point ip = this.toImgCoords(e.getPoint());
         if (ip != null) {
            if (SwingUtilities.isRightMouseButton(e)) {
               ImageEditorPanel.this.setBrushColor(new Color(ImageEditorPanel.this.currentImg.getRGB(ip.x, ip.y), true));
            } else if ("crop".equals(ImageEditorPanel.this.currentTool)) {
               ImageEditorPanel.this.cropStart = ip;
            } else {
               ImageEditorPanel.this.pushUndo();
               this.applyTool(ip.x, ip.y);
            }
         }
      }

      private void handleDrag(MouseEvent e) {
         Point ip = this.toImgCoords(e.getPoint());
         if (ip != null) {
            if ("crop".equals(ImageEditorPanel.this.currentTool) && ImageEditorPanel.this.cropStart != null) {
               int x0 = Math.min(ImageEditorPanel.this.cropStart.x, ip.x);
               int y0 = Math.min(ImageEditorPanel.this.cropStart.y, ip.y);
               int x1 = Math.max(ImageEditorPanel.this.cropStart.x, ip.x);
               int y1 = Math.max(ImageEditorPanel.this.cropStart.y, ip.y);
               ImageEditorPanel.this.cropBox = new Rectangle(x0, y0, x1 - x0 + 1, y1 - y0 + 1);
               this.repaint();
            } else {
               if ("pencil".equals(ImageEditorPanel.this.currentTool) || "eraser".equals(ImageEditorPanel.this.currentTool)) {
                  this.applyTool(ip.x, ip.y);
               }
            }
         }
      }

      private void handleRelease(MouseEvent e) {
         if ("crop".equals(ImageEditorPanel.this.currentTool) && ImageEditorPanel.this.cropBox != null) {
            this.repaint();
         }
      }

      private void applyTool(int ix, int iy) {
         if (ImageEditorPanel.this.currentImg != null) {
            int w = ImageEditorPanel.this.currentImg.getWidth();
            int h = ImageEditorPanel.this.currentImg.getHeight();
            if ("pencil".equals(ImageEditorPanel.this.currentTool)) {
               for (int dx = 0; dx < ImageEditorPanel.this.brushSize; dx++) {
                  for (int dy = 0; dy < ImageEditorPanel.this.brushSize; dy++) {
                     int px = ix + dx;
                     int py = iy + dy;
                     if (px < w && py < h) {
                        ImageEditorPanel.this.currentImg.setRGB(px, py, ImageEditorPanel.this.brushColor.getRGB());
                     }
                  }
               }

               this.repaint();
            } else if ("eraser".equals(ImageEditorPanel.this.currentTool)) {
               for (int dx = 0; dx < ImageEditorPanel.this.brushSize; dx++) {
                  for (int dyx = 0; dyx < ImageEditorPanel.this.brushSize; dyx++) {
                     int px = ix + dx;
                     int py = iy + dyx;
                     if (px < w && py < h) {
                        ImageEditorPanel.this.currentImg.setRGB(px, py, 0);
                     }
                  }
               }

               this.repaint();
            } else if ("picker".equals(ImageEditorPanel.this.currentTool)) {
               ImageEditorPanel.this.setBrushColor(new Color(ImageEditorPanel.this.currentImg.getRGB(ix, iy), true));
               ImageEditorPanel.this.selectTool("pencil");
            } else if ("bucket".equals(ImageEditorPanel.this.currentTool)) {
               this.floodFill(
                  ix, iy, ImageEditorPanel.this.currentImg.getRGB(ix, iy), ImageEditorPanel.this.brushColor.getRGB(), ImageEditorPanel.this.tolerance
               );
               this.repaint();
            } else if ("wand".equals(ImageEditorPanel.this.currentTool)) {
               this.floodFill(ix, iy, ImageEditorPanel.this.currentImg.getRGB(ix, iy), 0, ImageEditorPanel.this.tolerance);
               this.repaint();
            }
         }
      }

      private void floodFill(int startX, int startY, int targetRgb, int replaceRgb, int tol) {
         if (targetRgb != replaceRgb) {
            int w = ImageEditorPanel.this.currentImg.getWidth();
            int h = ImageEditorPanel.this.currentImg.getHeight();
            int tolVal = (int)(tol / 100.0 * 255.0);
            Color targetColor = new Color(targetRgb, true);
            boolean[][] visited = new boolean[w][h];
            Queue<Point> q = new LinkedList<>();
            q.add(new Point(startX, startY));

            while (!q.isEmpty()) {
               Point p = q.poll();
               int x = p.x;
               int y = p.y;
               if (x >= 0 && x < w && y >= 0 && y < h && !visited[x][y]) {
                  visited[x][y] = true;
                  Color c = new Color(ImageEditorPanel.this.currentImg.getRGB(x, y), true);
                  if (Math.abs(c.getRed() - targetColor.getRed()) <= tolVal
                     && Math.abs(c.getGreen() - targetColor.getGreen()) <= tolVal
                     && Math.abs(c.getBlue() - targetColor.getBlue()) <= tolVal) {
                     ImageEditorPanel.this.currentImg.setRGB(x, y, replaceRgb);
                     q.add(new Point(x + 1, y));
                     q.add(new Point(x - 1, y));
                     q.add(new Point(x, y + 1));
                     q.add(new Point(x, y - 1));
                  }
               }
            }
         }
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (ImageEditorPanel.this.currentImg != null) {
            Graphics2D g2 = (Graphics2D)g.create();
            int ox = 20;
            int oy = 20;
            int dw = (int)(ImageEditorPanel.this.currentImg.getWidth() * ImageEditorPanel.this.zoomLevel);
            int dh = (int)(ImageEditorPanel.this.currentImg.getHeight() * ImageEditorPanel.this.zoomLevel);
            int cs = Math.max(4, (int)(4.0 * (ImageEditorPanel.this.zoomLevel / 4.0)));

            for (int cx = 0; cx < dw; cx += cs) {
               for (int cy = 0; cy < dh; cy += cs) {
                  boolean dark = (cx / cs + cy / cs) % 2 == 0;
                  g2.setColor(dark ? new Color(30, 30, 40) : new Color(45, 45, 58));
                  g2.fillRect(ox + cx, oy + cy, Math.min(cs, dw - cx), Math.min(cs, dh - cy));
               }
            }

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.drawImage(ImageEditorPanel.this.currentImg, ox, oy, dw, dh, null);
            if (ImageEditorPanel.this.showGrid && ImageEditorPanel.this.zoomLevel >= 4.0) {
               g2.setColor(new Color(42, 42, 62, 180));

               for (int x = 0; x <= ImageEditorPanel.this.currentImg.getWidth(); x++) {
                  int gx = ox + (int)(x * ImageEditorPanel.this.zoomLevel);
                  g2.drawLine(gx, oy, gx, oy + dh);
               }

               for (int y = 0; y <= ImageEditorPanel.this.currentImg.getHeight(); y++) {
                  int gy = oy + (int)(y * ImageEditorPanel.this.zoomLevel);
                  g2.drawLine(ox, gy, ox + dw, gy);
               }
            }

            if (ImageEditorPanel.this.cropBox != null) {
               int cx0 = ox + (int)(ImageEditorPanel.this.cropBox.x * ImageEditorPanel.this.zoomLevel);
               int cy0 = oy + (int)(ImageEditorPanel.this.cropBox.y * ImageEditorPanel.this.zoomLevel);
               int cw = (int)(ImageEditorPanel.this.cropBox.width * ImageEditorPanel.this.zoomLevel);
               int ch = (int)(ImageEditorPanel.this.cropBox.height * ImageEditorPanel.this.zoomLevel);
               g2.setColor(new Color(0, 255, 234));
               g2.setStroke(new BasicStroke(2.0F, 0, 0, 10.0F, new float[]{4.0F}, 0.0F));
               g2.drawRect(cx0, cy0, cw, ch);
            }

            g2.dispose();
         }
      }
   }

   private static class TransferableImage implements Transferable {
      private final Image img;

      public TransferableImage(Image img) {
         this.img = img;
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
         return new DataFlavor[]{DataFlavor.imageFlavor};
      }

      @Override
      public boolean isDataFlavorSupported(DataFlavor flavor) {
         return DataFlavor.imageFlavor.equals(flavor);
      }

      @Override
      public Object getTransferData(DataFlavor flavor) {
         return this.img;
      }
   }
}
