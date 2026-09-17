package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class EffectPartPanel extends JPanel {
   private static final Color BG = new Color(16, 16, 22);
   private static final Color BG_TABLE = new Color(22, 22, 30);
   private static final Color HDR_COLOR = new Color(110, 110, 140);
   private static final Color SEL_COLOR = new Color(0, 95, 210);
   private static final Color BORDER = new Color(38, 38, 52);
   private EffectModel model;
   private int selectedSpriteIdx = -1;
   private EffFrame currentFrame;
   private int selectedFramePartIdx = -1;
   private EffectPartPanel.OnSmallImgSelectedListener spriteSelectListener;
   private EffectPartPanel.OnSmallImgChangedListener spriteChangeListener;
   private EffectPartPanel.OnFramePartActionListener framePartListener;
   private final EffectPartPanel.AtlasPreview atlasPreview;
   private final JLabel headerLabel;
   private final JLabel lblSelectedSpriteBadge;
   private final JTabbedPane tabbedPane;
   private final DefaultTableModel spriteTableModel;
   private final JTable spriteTable;
   private final DefaultTableModel frameTableModel;
   private final JTable frameTable;
   private final JButton btnAddFPWithSprite;
   private final JButton btnReplaceFPSprite;

   public EffectPartPanel() {
      this.setLayout(new BorderLayout(0, 2));
      this.setBackground(BG);
      this.setPreferredSize(new Dimension(320, 0));
      JPanel headerPanel = new JPanel(new BorderLayout(4, 0));
      headerPanel.setBackground(BG);
      headerPanel.setBorder(new EmptyBorder(4, 6, 4, 6));
      this.headerLabel = new JLabel("ATTACHMENTS & SPRITES");
      this.headerLabel.setFont(new Font("Segoe UI", 1, 10));
      this.headerLabel.setForeground(HDR_COLOR);
      headerPanel.add(this.headerLabel, "West");
      this.lblSelectedSpriteBadge = new JLabel("Sprite: #0");
      this.lblSelectedSpriteBadge.setFont(new Font("Segoe UI", 1, 10));
      this.lblSelectedSpriteBadge.setForeground(new Color(0, 200, 255));
      headerPanel.add(this.lblSelectedSpriteBadge, "East");
      this.add(headerPanel, "North");
      JSplitPane split = new JSplitPane(0);
      split.setBorder(null);
      split.setDividerSize(4);
      split.setBackground(BG);
      split.setDividerLocation(190);
      JPanel atlasContainer = new JPanel(new BorderLayout(0, 2));
      atlasContainer.setBackground(BG);
      this.atlasPreview = new EffectPartPanel.AtlasPreview();
      JScrollPane atlasScroll = new JScrollPane(this.atlasPreview);
      atlasScroll.setBorder(BorderFactory.createLineBorder(BORDER));
      atlasScroll.setBackground(new Color(12, 12, 16));
      atlasScroll.getViewport().setBackground(new Color(12, 12, 16));
      atlasScroll.getHorizontalScrollBar().setUnitIncrement(20);
      atlasScroll.getVerticalScrollBar().setUnitIncrement(20);
      atlasContainer.add(atlasScroll, "Center");
      JPanel atlasQuickBar = new JPanel(new FlowLayout(2, 3, 2));
      atlasQuickBar.setBackground(new Color(18, 18, 24));
      atlasQuickBar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER));
      JButton btnAddFromAtlas = this.actionBtn("➕ Thêm vào Frame", Theme.ACCENT);
      btnAddFromAtlas.setToolTipText("Thêm Sprite đang chọn vào Frame hiện tại (Hoặc Double-click lên Sprite trong Atlas)");
      btnAddFromAtlas.addActionListener(e -> {
         if (this.framePartListener != null) {
            int spr = this.selectedSpriteIdx >= 0 ? this.selectedSpriteIdx : 0;
            this.framePartListener.onAddFramePart(spr);
         }
      });
      JButton btnCutterAtlas = this.actionBtn("✂ Cắt Sprite", new Color(160, 80, 0));
      btnCutterAtlas.setToolTipText("Mở bộ cắt Sprite Atlas chuyên dụng");
      btnCutterAtlas.addActionListener(e -> {
         if (this.framePartListener != null) {
            this.framePartListener.onOpenSpriteCutter();
         }
      });
      atlasQuickBar.add(btnAddFromAtlas);
      atlasQuickBar.add(btnCutterAtlas);
      atlasContainer.add(atlasQuickBar, "South");
      split.setTopComponent(atlasContainer);
      this.tabbedPane = new JTabbedPane();
      this.tabbedPane.setFont(new Font("Segoe UI", 1, 9));
      this.tabbedPane.setBackground(new Color(24, 24, 32));
      this.tabbedPane.setForeground(new Color(170, 170, 190));
      JPanel framePartsTab = new JPanel(new BorderLayout(0, 2));
      framePartsTab.setBackground(BG_TABLE);
      String[] fCols = new String[]{"#", "Icon", "Sprite", "dx", "dy", "Lớp", "Lật", "Góc"};
      this.frameTableModel = new DefaultTableModel(fCols, 0) {
         @Override
         public boolean isCellEditable(int r, int c) {
            return c >= 2;
         }

         @Override
         public Class<?> getColumnClass(int c) {
            return c == 1 ? ImageIcon.class : super.getColumnClass(c);
         }

         @Override
         public void setValueAt(Object aValue, int row, int col) {
            if (EffectPartPanel.this.currentFrame != null
               && EffectPartPanel.this.currentFrame.allParts != null
               && row >= 0
               && row < EffectPartPanel.this.currentFrame.allParts.size()) {
               EffPartFrame p = EffectPartPanel.this.currentFrame.allParts.get(row);
               String strVal = aValue != null ? aValue.toString().trim() : "";

               try {
                  switch (col) {
                     case 2:
                        int sid = Integer.parseInt(strVal);
                        if (EffectPartPanel.this.model != null
                           && EffectPartPanel.this.model.smallImages != null
                           && sid >= 0
                           && sid < EffectPartPanel.this.model.smallImages.length) {
                           p.idSmallImg = sid;
                        }
                        break;
                     case 3:
                        p.dx = Integer.parseInt(strVal);
                        break;
                     case 4:
                        p.dy = Integer.parseInt(strVal);
                        break;
                     case 5:
                        p.onTop = !strVal.equalsIgnoreCase("top") && !strVal.equalsIgnoreCase("đè") && !strVal.equalsIgnoreCase("có") && !strVal.equals("1")
                           ? 0
                           : 1;
                        break;
                     case 6:
                        p.flip = !strVal.equalsIgnoreCase("lật") && !strVal.equalsIgnoreCase("có") && !strVal.equalsIgnoreCase("true") && !strVal.equals("1")
                           ? 0
                           : 1;
                        break;
                     case 7:
                        String cleanDeg = strVal.replace("°", "").replace("deg", "").trim();
                        int rot = Integer.parseInt(cleanDeg);
                        p.rotate = (rot % 360 + 360) % 360;
                  }

                  EffectPartPanel.this.currentFrame.rebuildLayers();
                  EffectPartPanel.this.updateFramePartValues(row, p);
                  if (EffectPartPanel.this.framePartListener != null) {
                     EffectPartPanel.this.framePartListener.onFramePartSelected(row);
                  }

                  EffectPartPanel.this.atlasPreview.repaint();
               } catch (Exception var9) {
                  EffectPartPanel.this.updateFramePartValues(row, p);
               }
            }
         }
      };
      this.frameTable = new JTable(this.frameTableModel);
      this.styleTable(this.frameTable, true);
      this.frameTable.getSelectionModel().addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int row = this.frameTable.getSelectedRow();
            if (row >= 0 && this.currentFrame != null && row < this.currentFrame.allParts.size()) {
               this.selectedFramePartIdx = row;
               if (this.framePartListener != null) {
                  this.framePartListener.onFramePartSelected(row);
               }

               this.atlasPreview.repaint();
            }
         }
      });
      this.frameTable.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
               int r = EffectPartPanel.this.frameTable.rowAtPoint(e.getPoint());
               if (r >= 0 && EffectPartPanel.this.currentFrame != null && r < EffectPartPanel.this.currentFrame.allParts.size()) {
                  PartQuickEditDialog.showDialog(EffectPartPanel.this, EffectPartPanel.this.model, EffectPartPanel.this.currentFrame, r, updatedPart -> {
                     EffectPartPanel.this.updateFramePartValues(r, updatedPart);
                     if (EffectPartPanel.this.framePartListener != null) {
                        EffectPartPanel.this.framePartListener.onFramePartSelected(r);
                     }

                     EffectPartPanel.this.atlasPreview.repaint();
                  });
               }
            } else if (SwingUtilities.isRightMouseButton(e)) {
               int r = EffectPartPanel.this.frameTable.rowAtPoint(e.getPoint());
               if (r >= 0 && EffectPartPanel.this.currentFrame != null && r < EffectPartPanel.this.currentFrame.allParts.size()) {
                  EffectPartPanel.this.frameTable.setRowSelectionInterval(r, r);
                  EffectPartPanel.this.selectedFramePartIdx = r;
                  if (EffectPartPanel.this.framePartListener != null) {
                     EffectPartPanel.this.framePartListener.onFramePartSelected(r);
                  }

                  EffectPartPanel.this.showFramePartContextMenu(e.getComponent(), e.getX(), e.getY(), r);
               }
            }
         }
      });
      this.frameTable.getInputMap(1).put(KeyStroke.getKeyStroke(127, 0), "delPart");
      this.frameTable.getActionMap().put("delPart", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int r = EffectPartPanel.this.frameTable.getSelectedRow();
            if (r >= 0 && EffectPartPanel.this.framePartListener != null) {
               EffectPartPanel.this.framePartListener.onDeleteFramePart(r);
            }
         }
      });
      this.frameTable.getInputMap(1).put(KeyStroke.getKeyStroke(10, 0), "quickEditPart");
      this.frameTable.getActionMap().put("quickEditPart", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int r = EffectPartPanel.this.frameTable.getSelectedRow();
            if (r >= 0 && EffectPartPanel.this.currentFrame != null && r < EffectPartPanel.this.currentFrame.allParts.size()) {
               PartQuickEditDialog.showDialog(EffectPartPanel.this, EffectPartPanel.this.model, EffectPartPanel.this.currentFrame, r, updatedPart -> {
                  EffectPartPanel.this.updateFramePartValues(r, updatedPart);
                  if (EffectPartPanel.this.framePartListener != null) {
                     EffectPartPanel.this.framePartListener.onFramePartSelected(r);
                  }

                  EffectPartPanel.this.atlasPreview.repaint();
               });
            }
         }
      });
      JScrollPane frameScroll = new JScrollPane(this.frameTable);
      frameScroll.setBorder(BorderFactory.createLineBorder(BORDER));
      frameScroll.setBackground(BG_TABLE);
      frameScroll.getViewport().setBackground(BG_TABLE);
      framePartsTab.add(frameScroll, "Center");
      JPanel fpToolbar = new JPanel(new GridLayout(1, 6, 2, 0));
      fpToolbar.setBackground(BG_TABLE);
      fpToolbar.setBorder(new EmptyBorder(3, 3, 3, 3));
      this.btnAddFPWithSprite = this.actionBtn("➕ Thêm", Theme.ACCENT);
      this.btnAddFPWithSprite.setToolTipText("Thêm Part mới với Sprite đang chọn vào Frame");
      this.btnAddFPWithSprite.addActionListener(e -> {
         if (this.framePartListener != null) {
            int spr = this.selectedSpriteIdx >= 0 ? this.selectedSpriteIdx : 0;
            this.framePartListener.onAddFramePart(spr);
         }
      });
      this.btnReplaceFPSprite = this.actionBtn("\ud83d\udd04 Đổi Sprite", new Color(0, 140, 80));
      this.btnReplaceFPSprite.setToolTipText("Gán Sprite đang chọn cho Part đang chọn trong Frame");
      this.btnReplaceFPSprite.addActionListener(e -> {
         int r = this.frameTable.getSelectedRow();
         if (r >= 0 && this.selectedSpriteIdx >= 0 && this.framePartListener != null) {
            this.framePartListener.onReplacePartSprite(r, this.selectedSpriteIdx);
         }
      });
      JButton btnDupFP = this.actionBtn("⎘ Nhân bản", new Color(38, 38, 52));
      btnDupFP.setToolTipText("Nhân bản Part đang chọn");
      btnDupFP.addActionListener(e -> {
         int r = this.frameTable.getSelectedRow();
         if (r >= 0 && this.framePartListener != null) {
            this.framePartListener.onDuplicateFramePart(r);
         }
      });
      JButton btnDelFP = this.actionBtn("✖ Xóa", new Color(180, 50, 50));
      btnDelFP.setToolTipText("Xóa Part đang chọn khỏi Frame (Phím Del)");
      btnDelFP.addActionListener(e -> {
         int r = this.frameTable.getSelectedRow();
         if (r >= 0 && this.framePartListener != null) {
            this.framePartListener.onDeleteFramePart(r);
         }
      });
      JButton btnUpFP = this.actionBtn("▲ Lên", new Color(38, 38, 52));
      btnUpFP.setToolTipText("Đưa Part lên trước (Z-Order)");
      btnUpFP.addActionListener(e -> {
         int r = this.frameTable.getSelectedRow();
         if (r >= 0 && this.framePartListener != null) {
            this.framePartListener.onMoveFramePart(r, -1);
         }
      });
      JButton btnDnFP = this.actionBtn("▼ Xuống", new Color(38, 38, 52));
      btnDnFP.setToolTipText("Đưa Part xuống sau (Z-Order)");
      btnDnFP.addActionListener(e -> {
         int r = this.frameTable.getSelectedRow();
         if (r >= 0 && this.framePartListener != null) {
            this.framePartListener.onMoveFramePart(r, 1);
         }
      });
      fpToolbar.add(this.btnAddFPWithSprite);
      fpToolbar.add(this.btnReplaceFPSprite);
      fpToolbar.add(btnDupFP);
      fpToolbar.add(btnDelFP);
      fpToolbar.add(btnUpFP);
      fpToolbar.add(btnDnFP);
      framePartsTab.add(fpToolbar, "South");
      this.tabbedPane.addTab("\ud83e\udde9 FRAME PARTS", framePartsTab);
      JPanel spriteTab = new JPanel(new BorderLayout(0, 2));
      spriteTab.setBackground(BG_TABLE);
      String[] cols = new String[]{"#", "Icon", "x", "y", "w", "h"};
      this.spriteTableModel = new DefaultTableModel(cols, 0) {
         @Override
         public boolean isCellEditable(int r, int c) {
            return false;
         }

         @Override
         public Class<?> getColumnClass(int c) {
            return c == 1 ? ImageIcon.class : super.getColumnClass(c);
         }
      };
      this.spriteTable = new JTable(this.spriteTableModel);
      this.styleTable(this.spriteTable, true);
      this.spriteTable.getSelectionModel().addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int row = this.spriteTable.getSelectedRow();
            if (row >= 0 && this.model != null && this.model.smallImages != null && row < this.model.smallImages.length) {
               this.highlightSprite(row, false);
               if (this.spriteSelectListener != null) {
                  this.spriteSelectListener.onSelected(row, this.model.smallImages[row]);
               }
            }
         }
      });
      this.spriteTable.getInputMap(1).put(KeyStroke.getKeyStroke(127, 0), "delSprite");
      this.spriteTable.getActionMap().put("delSprite", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            EffectPartPanel.this.deleteSelectedSprite();
         }
      });
      this.spriteTable.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
               int r = EffectPartPanel.this.spriteTable.rowAtPoint(e.getPoint());
               if (r >= 0) {
                  EffectPartPanel.this.spriteTable.setRowSelectionInterval(r, r);
                  EffectPartPanel.this.highlightSprite(r, false);
                  EffectPartPanel.this.showSpriteContextMenu(e.getComponent(), e.getX(), e.getY());
               }
            } else if (e.getClickCount() == 2) {
               int r = EffectPartPanel.this.spriteTable.getSelectedRow();
               if (r >= 0 && EffectPartPanel.this.framePartListener != null) {
                  EffectPartPanel.this.framePartListener.onAddFramePart(r);
               }
            }
         }
      });
      JScrollPane spriteScroll = new JScrollPane(this.spriteTable);
      spriteScroll.setBorder(BorderFactory.createLineBorder(BORDER));
      spriteScroll.setBackground(BG_TABLE);
      spriteScroll.getViewport().setBackground(BG_TABLE);
      spriteTab.add(spriteScroll, "Center");
      JPanel sprToolbar = new JPanel(new GridLayout(1, 4, 2, 0));
      sprToolbar.setBackground(BG_TABLE);
      sprToolbar.setBorder(new EmptyBorder(3, 3, 3, 3));
      JButton btnAddAsPart = this.actionBtn("➕ Thêm vào Frame", Theme.ACCENT);
      btnAddAsPart.setToolTipText("Thêm Sprite đang chọn vào Frame hiện tại");
      btnAddAsPart.addActionListener(e -> {
         int r = this.spriteTable.getSelectedRow();
         if (r >= 0 && this.framePartListener != null) {
            this.framePartListener.onAddFramePart(r);
         }
      });
      JButton btnReplaceSpr = this.actionBtn("\ud83d\udd04 Gán vào Part", new Color(0, 140, 80));
      btnReplaceSpr.setToolTipText("Gán Sprite này cho Part đang chọn trong Frame");
      btnReplaceSpr.addActionListener(e -> {
         int r = this.spriteTable.getSelectedRow();
         if (r >= 0 && this.selectedFramePartIdx >= 0 && this.framePartListener != null) {
            this.framePartListener.onReplacePartSprite(this.selectedFramePartIdx, r);
         }
      });
      JButton btnDelSpr = this.actionBtn("✖ Xóa Sprite", new Color(180, 50, 50));
      btnDelSpr.setToolTipText("Xóa Sprite đang chọn khỏi danh mục (Phím Del)");
      btnDelSpr.addActionListener(e -> this.deleteSelectedSprite());
      JButton btnOpenCutter = this.actionBtn("✂ Cắt Sprite", new Color(160, 80, 0));
      btnOpenCutter.setToolTipText("Mở công cụ Cắt Sprite Atlas chuyên dụng");
      btnOpenCutter.addActionListener(e -> {
         if (this.framePartListener != null) {
            this.framePartListener.onOpenSpriteCutter();
         }
      });
      sprToolbar.add(btnAddAsPart);
      sprToolbar.add(btnReplaceSpr);
      sprToolbar.add(btnDelSpr);
      sprToolbar.add(btnOpenCutter);
      spriteTab.add(sprToolbar, "South");
      this.tabbedPane.addTab("\ud83d\uddbc SPRITES LIST", spriteTab);
      split.setBottomComponent(this.tabbedPane);
      this.add(split, "Center");
   }

   private void showSpriteContextMenu(Component comp, int x, int y) {
      if (this.selectedSpriteIdx >= 0 && this.model != null && this.model.smallImages != null && this.selectedSpriteIdx < this.model.smallImages.length) {
         JPopupMenu menu = new JPopupMenu();
         menu.setBackground(new Color(28, 28, 38));
         JMenuItem mAdd = new JMenuItem("➕ Thêm Sprite #" + this.selectedSpriteIdx + " vào Frame (Double-click)");
         mAdd.setForeground(Color.WHITE);
         mAdd.addActionListener(e -> {
            if (this.framePartListener != null) {
               this.framePartListener.onAddFramePart(this.selectedSpriteIdx);
            }
         });
         JMenuItem mAssign = new JMenuItem("\ud83d\udd04 Gán Sprite #" + this.selectedSpriteIdx + " cho Part đang chọn");
         mAssign.setForeground(Color.WHITE);
         mAssign.addActionListener(e -> {
            if (this.framePartListener != null && this.selectedFramePartIdx >= 0) {
               this.framePartListener.onReplacePartSprite(this.selectedFramePartIdx, this.selectedSpriteIdx);
            }
         });
         JMenuItem mCutter = new JMenuItem("✂ Mở bộ cắt Sprite Atlas...");
         mCutter.setForeground(new Color(255, 200, 100));
         mCutter.addActionListener(e -> {
            if (this.framePartListener != null) {
               this.framePartListener.onOpenSpriteCutter();
            }
         });
         JMenuItem mDel = new JMenuItem("✖ Xóa Sprite #" + this.selectedSpriteIdx + " (Del)");
         mDel.setForeground(new Color(255, 100, 100));
         mDel.addActionListener(e -> this.deleteSelectedSprite());
         menu.add(mAdd);
         menu.add(mAssign);
         menu.addSeparator();
         menu.add(mCutter);
         menu.add(mDel);
         menu.show(comp, x, y);
      }
   }

   private void deleteSelectedSprite() {
      if (this.model != null && this.model.smallImages != null && this.selectedSpriteIdx >= 0 && this.selectedSpriteIdx < this.model.smallImages.length) {
         int ans = JOptionPane.showConfirmDialog(
            this, "Xác nhận xóa Sprite #" + this.selectedSpriteIdx + "?\n(Các part sử dụng sprite này sẽ được chuyển về Sprite 0)", "Xác nhận xóa", 0
         );
         if (ans == 0) {
            int delIdx = this.selectedSpriteIdx;
            int oldLen = this.model.smallImages.length;
            if (oldLen <= 1) {
               this.model.smallImages = new SmallImageDef[0];
            } else {
               SmallImageDef[] newArr = new SmallImageDef[oldLen - 1];
               int ni = 0;

               for (int i = 0; i < oldLen; i++) {
                  if (i != delIdx) {
                     newArr[ni++] = this.model.smallImages[i];
                  }
               }

               this.model.smallImages = newArr;
            }

            if (this.model.frames != null) {
               for (EffFrame f : this.model.frames) {
                  if (f != null && f.allParts != null) {
                     for (EffPartFrame p : f.allParts) {
                        if (p.idSmallImg == delIdx) {
                           p.idSmallImg = 0;
                        } else if (p.idSmallImg > delIdx) {
                           p.idSmallImg--;
                        }
                     }

                     f.rebuildLayers();
                  }
               }
            }

            this.setModel(this.model);
            if (this.currentFrame != null) {
               this.setCurrentFrame(this.currentFrame);
            }

            if (this.spriteChangeListener != null) {
               this.spriteChangeListener.onSmallImgChanged(delIdx, null);
            }
         }
      }
   }

   private void showFramePartContextMenu(Component comp, int x, int y, int row) {
      if (this.currentFrame != null && this.currentFrame.allParts != null && row >= 0 && row < this.currentFrame.allParts.size()) {
         EffPartFrame p = this.currentFrame.allParts.get(row);
         JPopupMenu menu = new JPopupMenu();
         menu.setBackground(new Color(28, 28, 38));
         JMenuItem mEdit = new JMenuItem("✎ Chỉnh sửa chi tiết (Double-Click)...");
         mEdit.setFont(new Font("Segoe UI", 1, 11));
         mEdit.setForeground(new Color(0, 200, 255));
         mEdit.addActionListener(e -> PartQuickEditDialog.showDialog(this, this.model, this.currentFrame, row, updatedPart -> {
            this.updateFramePartValues(row, updatedPart);
            if (this.framePartListener != null) {
               this.framePartListener.onFramePartSelected(row);
            }

            this.atlasPreview.repaint();
         }));
         JMenuItem mDup = new JMenuItem("⎘ Nhân bản Part");
         mDup.setForeground(Color.WHITE);
         mDup.addActionListener(e -> {
            if (this.framePartListener != null) {
               this.framePartListener.onDuplicateFramePart(row);
            }
         });
         JMenuItem mRot0 = new JMenuItem("⟲ Đặt góc về 0°");
         mRot0.setForeground(Color.WHITE);
         mRot0.addActionListener(e -> {
            p.rotate = 0;
            this.updateFramePartValues(row, p);
            if (this.framePartListener != null) {
               this.framePartListener.onFramePartSelected(row);
            }

            this.atlasPreview.repaint();
         });
         JMenuItem mRot90 = new JMenuItem("↷ Xoay thêm +90°");
         mRot90.setForeground(Color.WHITE);
         mRot90.addActionListener(e -> {
            p.rotate = (p.rotate + 90) % 360;
            this.updateFramePartValues(row, p);
            if (this.framePartListener != null) {
               this.framePartListener.onFramePartSelected(row);
            }

            this.atlasPreview.repaint();
         });
         JMenuItem mFlip = new JMenuItem("↔ Đổi trạng thái Lật (Hiện: " + (p.flip == 1 ? "Đang Lật" : "Không") + ")");
         mFlip.setForeground(Color.WHITE);
         mFlip.addActionListener(e -> {
            p.flip = p.flip == 1 ? 0 : 1;
            this.updateFramePartValues(row, p);
            if (this.framePartListener != null) {
               this.framePartListener.onFramePartSelected(row);
            }

            this.atlasPreview.repaint();
         });
         JMenuItem mTop = new JMenuItem("⬆ Đổi trạng thái Layer (Hiện: " + (p.onTop == 1 ? "Top" : "Bottom") + ")");
         mTop.setForeground(Color.WHITE);
         mTop.addActionListener(e -> {
            p.onTop = p.onTop == 1 ? 0 : 1;
            this.currentFrame.rebuildLayers();
            this.updateFramePartValues(row, p);
            if (this.framePartListener != null) {
               this.framePartListener.onFramePartSelected(row);
            }

            this.atlasPreview.repaint();
         });
         JMenuItem mDel = new JMenuItem("✖ Xóa Part (Del)");
         mDel.setForeground(new Color(255, 100, 100));
         mDel.addActionListener(e -> {
            if (this.framePartListener != null) {
               this.framePartListener.onDeleteFramePart(row);
            }
         });
         menu.add(mEdit);
         menu.addSeparator();
         menu.add(mDup);
         menu.add(mRot0);
         menu.add(mRot90);
         menu.add(mFlip);
         menu.add(mTop);
         menu.addSeparator();
         menu.add(mDel);
         menu.show(comp, x, y);
      }
   }

   private void styleTable(JTable t, boolean hasIcon) {
      t.setBackground(BG_TABLE);
      t.setForeground(new Color(190, 190, 210));
      t.setGridColor(new Color(36, 36, 48));
      t.setSelectionBackground(SEL_COLOR);
      t.setSelectionForeground(Color.WHITE);
      t.setFont(new Font("Segoe UI", 0, 11));
      t.setRowHeight(hasIcon ? 36 : 22);
      t.getTableHeader()
         .setDefaultRenderer(
            new DefaultTableCellRenderer() {
               @Override
               public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean hasF, int r, int c) {
                  JLabel l = (JLabel)super.getTableCellRendererComponent(table, val, isSel, hasF, r, c);
                  l.setBackground(new Color(26, 26, 34));
                  l.setForeground(new Color(160, 160, 180));
                  l.setFont(new Font("Segoe UI", 1, 10));
                  l.setHorizontalAlignment(0);
                  l.setBorder(
                     BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(42, 42, 54)), BorderFactory.createEmptyBorder(2, 2, 2, 2)
                     )
                  );
                  return l;
               }
            }
         );
      DefaultTableCellRenderer center = new DefaultTableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean hasF, int r, int c) {
            Component comp = super.getTableCellRendererComponent(table, val, isSel, hasF, r, c);
            if (isSel) {
               comp.setBackground(EffectPartPanel.SEL_COLOR);
               comp.setForeground(Color.WHITE);
            } else {
               comp.setBackground(r % 2 == 0 ? EffectPartPanel.BG_TABLE : new Color(20, 20, 26));
               comp.setForeground(new Color(190, 190, 210));
            }

            return comp;
         }
      };
      center.setHorizontalAlignment(0);

      for (int i = 0; i < t.getColumnCount(); i++) {
         if (!hasIcon || i != 1) {
            t.getColumnModel().getColumn(i).setCellRenderer(center);
         }
      }

      if (hasIcon) {
         if (t.getColumnCount() == 8) {
            int[] w = new int[]{18, 34, 28, 28, 28, 26, 26, 30};

            for (int ix = 0; ix < w.length; ix++) {
               t.getColumnModel().getColumn(ix).setPreferredWidth(w[ix]);
            }
         } else if (t.getColumnCount() == 6) {
            int[] w = new int[]{20, 36, 30, 30, 30, 30};

            for (int ix = 0; ix < w.length; ix++) {
               t.getColumnModel().getColumn(ix).setPreferredWidth(w[ix]);
            }
         }
      }
   }

   private JButton actionBtn(String t, final Color bg) {
      final JButton b = new JButton(t);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setBackground(bg);
      b.setForeground(Color.WHITE);
      b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 55, 75)), new EmptyBorder(3, 4, 3, 4)));
      b.setFocusPainted(false);
      b.setCursor(Cursor.getPredefinedCursor(12));
      b.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            b.setBackground(bg.brighter());
         }

         @Override
         public void mouseExited(MouseEvent e) {
            b.setBackground(bg);
         }
      });
      return b;
   }

   public void setSpriteSelectListener(EffectPartPanel.OnSmallImgSelectedListener l) {
      this.spriteSelectListener = l;
   }

   public void setSpriteChangeListener(EffectPartPanel.OnSmallImgChangedListener l) {
      this.spriteChangeListener = l;
   }

   public void setFramePartListener(EffectPartPanel.OnFramePartActionListener l) {
      this.framePartListener = l;
   }

   public void setModel(EffectModel m) {
      this.model = m;
      int sprCount = m != null && m.smallImages != null ? m.smallImages.length : 0;
      this.headerLabel.setText("ATTACHMENTS (" + sprCount + " sprites)");
      this.spriteTableModel.setRowCount(0);
      if (m != null && m.smallImages != null) {
         for (int i = 0; i < m.smallImages.length; i++) {
            SmallImageDef s = m.smallImages[i];
            ImageIcon icon = this.createScaledIcon(s, 30, 30);
            this.spriteTableModel.addRow(new Object[]{i, icon, s.x, s.y, s.w, s.h});
         }
      }

      if (this.selectedSpriteIdx >= sprCount) {
         this.selectedSpriteIdx = Math.max(0, sprCount - 1);
      }

      this.updateSelectedSpriteBadge();
      this.atlasPreview.revalidate();
      this.atlasPreview.repaint();
   }

   public void setCurrentFrame(EffFrame frame) {
      this.currentFrame = frame;
      this.frameTableModel.setRowCount(0);
      if (frame != null && frame.allParts != null) {
         for (int i = 0; i < frame.allParts.size(); i++) {
            EffPartFrame p = frame.allParts.get(i);
            ImageIcon icon = null;
            if (this.model != null && this.model.smallImages != null && p.idSmallImg >= 0 && p.idSmallImg < this.model.smallImages.length) {
               icon = this.createScaledIcon(this.model.smallImages[p.idSmallImg], 30, 30);
            }

            this.frameTableModel
               .addRow(
                  new Object[]{
                     i, icon, p.idSmallImg, p.dx, p.dy, p.onTop == 1 ? "Top" : "Bot", p.flip == 1 ? "Lật" : "0", p.rotate != 0 ? p.rotate + "°" : "0°"
                  }
               );
         }
      }

      if (this.selectedFramePartIdx >= this.frameTableModel.getRowCount()) {
         this.selectedFramePartIdx = -1;
      }

      this.atlasPreview.repaint();
   }

   public void selectFramePartRow(int idx, boolean switchTab) {
      this.selectedFramePartIdx = idx;
      if (idx >= 0 && idx < this.frameTableModel.getRowCount()) {
         this.frameTable.getSelectionModel().setSelectionInterval(idx, idx);
         this.frameTable.scrollRectToVisible(this.frameTable.getCellRect(idx, 0, true));
         if (switchTab) {
            this.tabbedPane.setSelectedIndex(0);
         }
      } else {
         this.frameTable.clearSelection();
      }

      this.atlasPreview.repaint();
   }

   public void updateFramePartValues(int idx, EffPartFrame p) {
      if (idx >= 0 && idx < this.frameTableModel.getRowCount()) {
         ImageIcon icon = null;
         if (this.model != null && this.model.smallImages != null && p.idSmallImg >= 0 && p.idSmallImg < this.model.smallImages.length) {
            icon = this.createScaledIcon(this.model.smallImages[p.idSmallImg], 30, 30);
         }

         this.frameTableModel.setValueAt(icon, idx, 1);
         this.frameTableModel.setValueAt(p.idSmallImg, idx, 2);
         this.frameTableModel.setValueAt(p.dx, idx, 3);
         this.frameTableModel.setValueAt(p.dy, idx, 4);
         this.frameTableModel.setValueAt(p.onTop == 1 ? "Top" : "Bot", idx, 5);
         this.frameTableModel.setValueAt(p.flip == 1 ? "Lật" : "0", idx, 6);
         this.frameTableModel.setValueAt(p.rotate != 0 ? p.rotate + "°" : "0°", idx, 7);
      }

      this.atlasPreview.repaint();
   }

   public int getSelectedSpriteIdx() {
      return this.selectedSpriteIdx;
   }

   public int getSelectedFramePartIdx() {
      return this.selectedFramePartIdx;
   }

   public void highlightSprite(int smallImgIdx, boolean switchTab) {
      this.selectedSpriteIdx = smallImgIdx;
      this.updateSelectedSpriteBadge();
      if (smallImgIdx >= 0 && smallImgIdx < this.spriteTableModel.getRowCount()) {
         this.spriteTable.getSelectionModel().setSelectionInterval(smallImgIdx, smallImgIdx);
         this.spriteTable.scrollRectToVisible(this.spriteTable.getCellRect(smallImgIdx, 0, true));
         if (switchTab) {
            this.tabbedPane.setSelectedIndex(1);
         }
      }

      this.atlasPreview.repaint();
   }

   private void updateSelectedSpriteBadge() {
      if (this.model != null && this.model.smallImages != null && this.selectedSpriteIdx >= 0 && this.selectedSpriteIdx < this.model.smallImages.length) {
         SmallImageDef s = this.model.smallImages[this.selectedSpriteIdx];
         this.lblSelectedSpriteBadge.setText("Sprite #" + this.selectedSpriteIdx + " (" + s.w + "x" + s.h + ")");
         if (this.btnAddFPWithSprite != null) {
            this.btnAddFPWithSprite.setText("➕ Thêm #" + this.selectedSpriteIdx);
         }

         if (this.btnReplaceFPSprite != null) {
            this.btnReplaceFPSprite.setText("\ud83d\udd04 Đổi #" + this.selectedSpriteIdx);
         }
      } else {
         this.lblSelectedSpriteBadge.setText("Chưa chọn");
         if (this.btnAddFPWithSprite != null) {
            this.btnAddFPWithSprite.setText("➕ Thêm");
         }

         if (this.btnReplaceFPSprite != null) {
            this.btnReplaceFPSprite.setText("\ud83d\udd04 Đổi Sprite");
         }
      }
   }

   private ImageIcon createScaledIcon(SmallImageDef s, int maxW, int maxH) {
      if (this.model != null && this.model.atlasImage != null && s != null) {
         try {
            int cx = Math.max(0, s.x * 4);
            int cy = Math.max(0, s.y * 4);
            int cw = Math.min(s.w * 4, this.model.atlasImage.getWidth() - cx);
            int ch = Math.min(s.h * 4, this.model.atlasImage.getHeight() - cy);
            if (cw > 0 && ch > 0) {
               BufferedImage sub = this.model.atlasImage.getSubimage(cx, cy, cw, ch);
               double scale = Math.min((double)maxW / cw, (double)maxH / ch);
               if (scale > 1.0) {
                  scale = 1.0;
               }

               int nw = Math.max(1, (int)(cw * scale));
               int nh = Math.max(1, (int)(ch * scale));
               Image img = sub.getScaledInstance(nw, nh, 2);
               return new ImageIcon(img);
            } else {
               return null;
            }
         } catch (Exception var14) {
            return null;
         }
      } else {
         return null;
      }
   }

   private void addNewSpritePart(int x, int y, int w, int h) {
      if (this.model != null) {
         int newId = 0;
         if (this.model.smallImages != null && this.model.smallImages.length > 0) {
            for (SmallImageDef s : this.model.smallImages) {
               if (s.id >= newId) {
                  newId = s.id + 1;
               }
            }
         }

         SmallImageDef ns = new SmallImageDef(newId, x, y, w, h);
         if (this.model.smallImages == null) {
            this.model.smallImages = new SmallImageDef[]{ns};
         } else {
            SmallImageDef[] arr = new SmallImageDef[this.model.smallImages.length + 1];
            System.arraycopy(this.model.smallImages, 0, arr, 0, this.model.smallImages.length);
            arr[arr.length - 1] = ns;
            this.model.smallImages = arr;
         }

         this.setModel(this.model);
         int newIdx = this.model.smallImages.length - 1;
         this.highlightSprite(newIdx, false);
         if (this.spriteChangeListener != null) {
            this.spriteChangeListener.onSmallImgChanged(newIdx, ns);
         }
      }
   }

   private class AtlasPreview extends JPanel {
      private static final int PAD = 8;
      private double zoom = 1.0;
      private Point dragStart = null;
      private Rectangle currentDragRect = null;

      AtlasPreview() {
         this.setBackground(new Color(12, 12, 16));
         this.setToolTipText(
            "<html><b>Thao tác Atlas:</b><br>• <b>Click</b>: Chọn Sprite (Không chuyển tab)<br>• <b>Double-Click</b>: Thêm ngay Sprite vào Frame<br>• <b>Chuột Phải</b>: Mở Menu thao tác nhanh<br>• <b>Cuộn Chuột</b>: Zoom ảnh<br>• <b>Kéo Chuột</b>: Cắt vùng Sprite mới</html>"
         );
         this.setupInteractions();
      }

      public void resetZoom() {
         this.zoom = 1.0;
         this.revalidate();
         this.repaint();
      }

      private void setupInteractions() {
         this.setFocusable(true);
         this.addMouseWheelListener(e -> {
            if (EffectPartPanel.this.model != null && EffectPartPanel.this.model.atlasImage != null) {
               if (e.getWheelRotation() < 0) {
                  this.zoom *= 1.15;
               } else {
                  this.zoom /= 1.15;
               }

               this.zoom = Math.max(0.2, Math.min(12.0, this.zoom));
               this.revalidate();
               this.repaint();
            }
         });
         this.addMouseListener(
            new MouseAdapter() {
               @Override
               public void mousePressed(MouseEvent e) {
                  if (EffectPartPanel.this.model != null && EffectPartPanel.this.model.atlasImage != null) {
                     if (SwingUtilities.isRightMouseButton(e)) {
                        int clickX = (int)Math.round((e.getX() - 8) / (AtlasPreview.this.zoom * 4.0));
                        int clickY = (int)Math.round((e.getY() - 8) / (AtlasPreview.this.zoom * 4.0));
                        if (EffectPartPanel.this.model.smallImages != null) {
                           for (int i = 0; i < EffectPartPanel.this.model.smallImages.length; i++) {
                              SmallImageDef s = EffectPartPanel.this.model.smallImages[i];
                              if (clickX >= s.x && clickX < s.x + s.w && clickY >= s.y && clickY < s.y + s.h) {
                                 EffectPartPanel.this.highlightSprite(i, false);
                                 EffectPartPanel.this.showSpriteContextMenu(e.getComponent(), e.getX(), e.getY());
                                 return;
                              }
                           }
                        }
                     } else if (SwingUtilities.isLeftMouseButton(e)) {
                        AtlasPreview.this.dragStart = e.getPoint();
                        AtlasPreview.this.currentDragRect = null;
                     }
                  }
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                  if (SwingUtilities.isLeftMouseButton(e) && AtlasPreview.this.dragStart != null) {
                     int dist = (int)AtlasPreview.this.dragStart.distance(e.getPoint());
                     if (dist < 4) {
                        int clickX = (int)Math.round((e.getX() - 8) / (AtlasPreview.this.zoom * 4.0));
                        int clickY = (int)Math.round((e.getY() - 8) / (AtlasPreview.this.zoom * 4.0));
                        if (EffectPartPanel.this.model != null && EffectPartPanel.this.model.smallImages != null) {
                           for (int i = 0; i < EffectPartPanel.this.model.smallImages.length; i++) {
                              SmallImageDef s = EffectPartPanel.this.model.smallImages[i];
                              if (clickX >= s.x && clickX < s.x + s.w && clickY >= s.y && clickY < s.y + s.h) {
                                 EffectPartPanel.this.highlightSprite(i, false);
                                 if (e.getClickCount() == 2 && EffectPartPanel.this.framePartListener != null) {
                                    EffectPartPanel.this.framePartListener.onAddFramePart(i);
                                 }
                                 break;
                              }
                           }
                        }
                     } else if (AtlasPreview.this.currentDragRect != null
                        && AtlasPreview.this.currentDragRect.width > 2
                        && AtlasPreview.this.currentDragRect.height > 2
                        && EffectPartPanel.this.model != null) {
                        int imgW = EffectPartPanel.this.model.atlasImage.getWidth();
                        int imgH = EffectPartPanel.this.model.atlasImage.getHeight();
                        int rx1 = (int)Math.round((AtlasPreview.this.currentDragRect.x - 8) / (AtlasPreview.this.zoom * 4.0));
                        int ry1 = (int)Math.round((AtlasPreview.this.currentDragRect.y - 8) / (AtlasPreview.this.zoom * 4.0));
                        int rx2 = (int)Math.round(
                           (AtlasPreview.this.currentDragRect.x + AtlasPreview.this.currentDragRect.width - 8) / (AtlasPreview.this.zoom * 4.0)
                        );
                        int ry2 = (int)Math.round(
                           (AtlasPreview.this.currentDragRect.y + AtlasPreview.this.currentDragRect.height - 8) / (AtlasPreview.this.zoom * 4.0)
                        );
                        rx1 = Math.max(0, Math.min(rx1, imgW / 4));
                        ry1 = Math.max(0, Math.min(ry1, imgH / 4));
                        rx2 = Math.max(0, Math.min(rx2, imgW / 4));
                        ry2 = Math.max(0, Math.min(ry2, imgH / 4));
                        int finalX = Math.min(rx1, rx2);
                        int finalY = Math.min(ry1, ry2);
                        int finalW = Math.abs(rx1 - rx2);
                        int finalH = Math.abs(ry1 - ry2);
                        if (finalW > 1 && finalH > 1) {
                           EffectPartPanel.this.addNewSpritePart(finalX, finalY, finalW, finalH);
                        }
                     }
                  }

                  AtlasPreview.this.dragStart = null;
                  AtlasPreview.this.currentDragRect = null;
                  AtlasPreview.this.repaint();
               }
            }
         );
         this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
               if (AtlasPreview.this.dragStart != null) {
                  int x1 = Math.min(AtlasPreview.this.dragStart.x, e.getX());
                  int y1 = Math.min(AtlasPreview.this.dragStart.y, e.getY());
                  int w = Math.abs(AtlasPreview.this.dragStart.x - e.getX());
                  int h = Math.abs(AtlasPreview.this.dragStart.y - e.getY());
                  AtlasPreview.this.currentDragRect = new Rectangle(x1, y1, w, h);
                  AtlasPreview.this.repaint();
               }
            }
         });
      }

      @Override
      public Dimension getPreferredSize() {
         if (EffectPartPanel.this.model != null && EffectPartPanel.this.model.atlasImage != null) {
            int dw = (int)(EffectPartPanel.this.model.atlasImage.getWidth() * this.zoom) + 16;
            int dh = (int)(EffectPartPanel.this.model.atlasImage.getHeight() * this.zoom) + 16;
            return new Dimension(dw, dh);
         } else {
            return new Dimension(180, 180);
         }
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         g2.setColor(new Color(12, 12, 16));
         g2.fillRect(0, 0, this.getWidth(), this.getHeight());
         if (EffectPartPanel.this.model != null && EffectPartPanel.this.model.atlasImage != null) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            int dw = (int)(EffectPartPanel.this.model.atlasImage.getWidth() * this.zoom);
            int dh = (int)(EffectPartPanel.this.model.atlasImage.getHeight() * this.zoom);
            g2.drawImage(EffectPartPanel.this.model.atlasImage, 8, 8, dw, dh, null);
            if (EffectPartPanel.this.model.smallImages != null) {
               int partSpriteId = -1;
               if (EffectPartPanel.this.currentFrame != null
                  && EffectPartPanel.this.selectedFramePartIdx >= 0
                  && EffectPartPanel.this.selectedFramePartIdx < EffectPartPanel.this.currentFrame.allParts.size()) {
                  EffPartFrame p = EffectPartPanel.this.currentFrame.allParts.get(EffectPartPanel.this.selectedFramePartIdx);
                  if (p != null) {
                     partSpriteId = p.idSmallImg;
                  }
               }

               for (int i = 0; i < EffectPartPanel.this.model.smallImages.length; i++) {
                  SmallImageDef s = EffectPartPanel.this.model.smallImages[i];
                  int sx = 8 + (int)(s.x * 4 * this.zoom);
                  int sy = 8 + (int)(s.y * 4 * this.zoom);
                  int sw = (int)(s.w * 4 * this.zoom);
                  int sh = (int)(s.h * 4 * this.zoom);
                  boolean sel = i == EffectPartPanel.this.selectedSpriteIdx;
                  boolean isPartSpr = i == partSpriteId;
                  if (sel) {
                     g2.setColor(new Color(0, 180, 255, 60));
                     g2.fillRect(sx, sy, sw, sh);
                     g2.setColor(new Color(0, 220, 255));
                     g2.setStroke(new BasicStroke(2.0F));
                     g2.drawRect(sx, sy, sw, sh);
                  } else if (isPartSpr) {
                     g2.setColor(new Color(255, 215, 0, 50));
                     g2.fillRect(sx, sy, sw, sh);
                     g2.setColor(new Color(255, 215, 0));
                     g2.setStroke(new BasicStroke(1.5F, 0, 2, 0.0F, new float[]{4.0F}, 0.0F));
                     g2.drawRect(sx, sy, sw, sh);
                  } else {
                     g2.setColor(new Color(255, 200, 0, 120));
                     g2.setStroke(new BasicStroke(1.0F));
                     g2.drawRect(sx, sy, sw, sh);
                  }

                  g2.setFont(new Font("Segoe UI", 1, 10));
                  if (sel) {
                     g2.setColor(Color.WHITE);
                     g2.drawString("#" + i, sx + 2, sy + 11);
                  } else if (isPartSpr) {
                     g2.setColor(new Color(255, 230, 100));
                     g2.drawString("★#" + i, sx + 2, sy + 11);
                  } else {
                     g2.setColor(new Color(255, 200, 0, 180));
                     g2.drawString(String.valueOf(i), sx + 2, sy + 10);
                  }
               }

               if (this.currentDragRect != null) {
                  g2.setColor(Color.WHITE);
                  g2.setStroke(new BasicStroke(1.0F, 0, 2, 0.0F, new float[]{3.0F}, 0.0F));
                  g2.drawRect(this.currentDragRect.x, this.currentDragRect.y, this.currentDragRect.width, this.currentDragRect.height);
               }

               g2.setFont(new Font("Segoe UI", 0, 9));
               g2.setColor(new Color(120, 120, 150));
               g2.drawString(String.format("Zoom: %d%%", (int)(this.zoom * 100.0)), 8, this.getHeight() - 4);
            }
         } else {
            g2.setColor(new Color(70, 70, 90));
            g2.setFont(new Font("Segoe UI", 0, 11));
            g2.drawString("Không có ảnh Atlas", 8, 22);
         }
      }
   }

   public interface OnFramePartActionListener {
      void onFramePartSelected(int var1);

      default void onAddFramePart() {
         this.onAddFramePart(-1);
      }

      default void onAddFramePartWithSprite(int sid) {
         this.onAddFramePart(sid);
      }

      default void onAddFramePart(int spriteId) {
      }

      default void onDuplicateFramePart(int partIndex) {
      }

      default void onDeleteFramePart(int partIndex) {
      }

      default void onMoveFramePart(int partIndex, int direction) {
      }

      default void onReplacePartSprite(int partIndex, int newSpriteId) {
      }

      default void onOpenSpriteCutter() {
      }
   }

   public interface OnSmallImgChangedListener {
      void onSmallImgChanged(int var1, SmallImageDef var2);
   }

   public interface OnSmallImgSelectedListener {
      void onSelected(int var1, SmallImageDef var2);
   }
}
