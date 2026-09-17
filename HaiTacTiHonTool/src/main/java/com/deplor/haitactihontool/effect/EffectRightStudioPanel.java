package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.effectauto.EffectAutoModel;
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
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class EffectRightStudioPanel extends JPanel {
   private static final Color BG = new Color(16, 16, 22);
   private static final Color BG_CARD = new Color(22, 22, 30);
   private static final Color BG_TABLE = new Color(20, 20, 28);
   private static final Color BORDER = new Color(38, 38, 52);
   private static final Color TEXT_MAIN = new Color(210, 210, 225);
   private static final Color TEXT_DIM = new Color(130, 130, 150);
   private EffectModel model;
   private List<BufferedImage> thumbs;
   private int selectedFrameIdx = 0;
   private int selectedPartIdx = -1;
   private int selectedSpriteIdx = -1;
   private boolean isEffectAuto = false;
   private boolean updatingProps = false;
   private EffectRightStudioPanel.OnStudioActionListener listener;
   private final JTabbedPane tabbedPane;
   private final DefaultListModel<Integer> frameListModel = new DefaultListModel<>();
   private final JList<Integer> frameJList;
   private final JLabel lblFrameBadge;
   private final DefaultTableModel partsTableModel;
   private final JTable partsTable;
   private final SpriteSheetViewer spriteSheetViewer;
   private final DefaultTableModel spriteTableModel;
   private final JTable spriteTable;
   private final JLabel lblSpriteBadge;
   private final JSpinner spDx;
   private final JSpinner spDy;
   private final JSpinner spImgId;
   private final JSpinner spRotate;
   private JSpinner spAutoType;
   private JSpinner spAutoValue;
   private final JToggleButton btnFlip;
   private final JToggleButton btnOnTop;
   private final JLabel partPropsTitle;
   private final EffectRightStudioPanel.PartSubPreview subPreview;

   public EffectRightStudioPanel(boolean isAuto) {
      this.isEffectAuto = isAuto;
      this.setLayout(new BorderLayout(0, 0));
      this.setBackground(BG);
      this.setPreferredSize(new Dimension(380, 0));
      this.tabbedPane = new JTabbedPane();
      this.tabbedPane.setFont(new Font("Segoe UI", 1, 10));
      this.tabbedPane.setBackground(new Color(24, 24, 32));
      this.tabbedPane.setForeground(new Color(180, 180, 200));
      JPanel tabFrames = new JPanel(new BorderLayout(0, 4));
      tabFrames.setBackground(BG);
      JPanel frameGalleryPnl = new JPanel(new BorderLayout(0, 2));
      frameGalleryPnl.setBackground(BG_CARD);
      frameGalleryPnl.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      JPanel frameHdr = new JPanel(new BorderLayout());
      frameHdr.setBackground(new Color(24, 24, 34));
      frameHdr.setBorder(new EmptyBorder(4, 8, 4, 8));
      JLabel lblFH = new JLabel("DANH SÁCH KEYFRAMES");
      lblFH.setFont(new Font("Segoe UI", 1, 10));
      lblFH.setForeground(Theme.ACCENT);
      this.lblFrameBadge = new JLabel("0 frames");
      this.lblFrameBadge.setFont(new Font("Segoe UI", 1, 10));
      this.lblFrameBadge.setForeground(new Color(0, 200, 255));
      frameHdr.add(lblFH, "West");
      frameHdr.add(this.lblFrameBadge, "East");
      frameGalleryPnl.add(frameHdr, "North");
      this.frameJList = new JList<>(this.frameListModel);
      this.frameJList.setBackground(BG_TABLE);
      this.frameJList.setForeground(TEXT_MAIN);
      this.frameJList.setSelectionBackground(new Color(0, 95, 210));
      this.frameJList.setSelectionForeground(Color.WHITE);
      this.frameJList.setFixedCellWidth(72);
      this.frameJList.setFixedCellHeight(86);
      this.frameJList.setLayoutOrientation(2);
      this.frameJList.setVisibleRowCount(1);
      this.frameJList.setCellRenderer(new EffectRightStudioPanel.FrameCardCellRenderer());
      this.frameJList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int idx = this.frameJList.getSelectedIndex();
            if (idx >= 0 && idx < this.frameListModel.size()) {
               this.selectedFrameIdx = idx;
               this.updatePartsTable();
               if (this.listener != null) {
                  this.listener.onFrameSelected(idx);
               }
            }
         }
      });
      JScrollPane frameScroll = new JScrollPane(this.frameJList);
      frameScroll.setBorder(null);
      frameScroll.setPreferredSize(new Dimension(0, 100));
      frameScroll.getHorizontalScrollBar().setUnitIncrement(20);
      frameGalleryPnl.add(frameScroll, "Center");
      JPanel frameBtns = new JPanel(new FlowLayout(0, 4, 4));
      frameBtns.setBackground(new Color(20, 20, 28));
      JButton btnAddF = actionBtn("Thêm Frame", Theme.ACCENT);
      btnAddF.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onAddFrame();
         }
      });
      JButton btnDupF = actionBtn("Nhân Bản", new Color(40, 40, 55));
      btnDupF.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onDuplicateFrame(this.selectedFrameIdx);
         }
      });
      JButton btnDelF = actionBtn("Xóa Frame", new Color(180, 50, 50));
      btnDelF.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onDeleteFrame(this.selectedFrameIdx);
         }
      });
      JButton btnInsertSeq = actionBtn("Chèn Vào Seq", new Color(0, 140, 80));
      btnInsertSeq.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onInsertFrameToSeq(this.selectedFrameIdx);
         }
      });
      frameBtns.add(btnAddF);
      frameBtns.add(btnDupF);
      frameBtns.add(btnDelF);
      frameBtns.add(btnInsertSeq);
      frameGalleryPnl.add(frameBtns, "South");
      JPanel partsContainer = new JPanel(new BorderLayout(0, 2));
      partsContainer.setBackground(BG);
      JPanel partsHdr = new JPanel(new BorderLayout());
      partsHdr.setBackground(new Color(24, 24, 34));
      partsHdr.setBorder(new EmptyBorder(4, 8, 4, 8));
      JLabel lblPH = new JLabel("CÁC LAYER PARTS TRONG FRAME ĐANG CHỌN");
      lblPH.setFont(new Font("Segoe UI", 1, 10));
      lblPH.setForeground(new Color(170, 170, 190));
      partsHdr.add(lblPH, "West");
      partsContainer.add(partsHdr, "North");
      String[] partCols = new String[]{"#", "Sprite", "dx", "dy", "Góc", "Lật", "Đè"};
      this.partsTableModel = new DefaultTableModel(partCols, 0) {
         @Override
         public boolean isCellEditable(int r, int c) {
            return c >= 1;
         }

         @Override
         public void setValueAt(Object aValue, int row, int col) {
            if (EffectRightStudioPanel.this.model != null
               && EffectRightStudioPanel.this.model.frames != null
               && EffectRightStudioPanel.this.selectedFrameIdx >= 0
               && EffectRightStudioPanel.this.selectedFrameIdx < EffectRightStudioPanel.this.model.frames.length) {
               EffFrame f = EffectRightStudioPanel.this.model.frames[EffectRightStudioPanel.this.selectedFrameIdx];
               if (f != null && f.allParts != null && row >= 0 && row < f.allParts.size()) {
                  EffPartFrame p = f.allParts.get(row);
                  String strVal = aValue != null ? aValue.toString().trim() : "";

                  try {
                     switch (col) {
                        case 1:
                           int sid = Integer.parseInt(strVal);
                           if (EffectRightStudioPanel.this.model.smallImages != null && sid >= 0 && sid < EffectRightStudioPanel.this.model.smallImages.length) {
                              p.idSmallImg = sid;
                           }
                           break;
                        case 2:
                           p.dx = Integer.parseInt(strVal);
                           break;
                        case 3:
                           p.dy = Integer.parseInt(strVal);
                           break;
                        case 4:
                           String cleanDeg = strVal.replace("°", "").replace("deg", "").trim();
                           int rot = Integer.parseInt(cleanDeg);
                           p.rotate = (rot % 360 + 360) % 360;
                           break;
                        case 5:
                           p.flip = !strVal.equalsIgnoreCase("có")
                                 && !strVal.equalsIgnoreCase("true")
                                 && !strVal.equals("1")
                                 && !strVal.equalsIgnoreCase("yes")
                                 && !strVal.equalsIgnoreCase("lật")
                              ? 0
                              : 1;
                           break;
                        case 6:
                           p.onTop = !strVal.equalsIgnoreCase("có")
                                 && !strVal.equalsIgnoreCase("true")
                                 && !strVal.equals("1")
                                 && !strVal.equalsIgnoreCase("yes")
                                 && !strVal.equalsIgnoreCase("top")
                                 && !strVal.equalsIgnoreCase("đè")
                              ? 0
                              : 1;
                     }

                     f.rebuildLayers();
                     EffectRightStudioPanel.this.updatePartsTableRow(row, p);
                     if (row == EffectRightStudioPanel.this.selectedPartIdx) {
                        EffectRightStudioPanel.this.syncInspectorWithSelectedPart();
                     }

                     if (EffectRightStudioPanel.this.listener != null) {
                        EffectRightStudioPanel.this.listener.onPartPropsChanged(row, p);
                     }
                  } catch (Exception var10) {
                     EffectRightStudioPanel.this.updatePartsTableRow(row, p);
                  }
               }
            }
         }
      };
      this.partsTable = new JTable(this.partsTableModel);
      styleTable(this.partsTable);
      this.partsTable.getColumnModel().getColumn(0).setPreferredWidth(28);
      this.partsTable.getColumnModel().getColumn(1).setPreferredWidth(45);
      this.partsTable.getColumnModel().getColumn(2).setPreferredWidth(40);
      this.partsTable.getColumnModel().getColumn(3).setPreferredWidth(40);
      this.partsTable.getColumnModel().getColumn(4).setPreferredWidth(40);
      this.partsTable.getColumnModel().getColumn(5).setPreferredWidth(35);
      this.partsTable.getColumnModel().getColumn(6).setPreferredWidth(35);
      this.partsTable.getSelectionModel().addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int row = this.partsTable.getSelectedRow();
            this.selectedPartIdx = row;
            if (this.listener != null && row >= 0) {
               this.listener.onPartSelected(row);
            }

            this.syncInspectorWithSelectedPart();
         }
      });
      this.partsTable
         .addMouseListener(
            new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 2) {
                     int row = EffectRightStudioPanel.this.partsTable.rowAtPoint(e.getPoint());
                     if (row >= 0
                        && EffectRightStudioPanel.this.model != null
                        && EffectRightStudioPanel.this.model.frames != null
                        && EffectRightStudioPanel.this.selectedFrameIdx >= 0
                        && EffectRightStudioPanel.this.selectedFrameIdx < EffectRightStudioPanel.this.model.frames.length) {
                        EffFrame f = EffectRightStudioPanel.this.model.frames[EffectRightStudioPanel.this.selectedFrameIdx];
                        PartQuickEditDialog.showDialog(EffectRightStudioPanel.this, EffectRightStudioPanel.this.model, f, row, updatedPart -> {
                           EffectRightStudioPanel.this.updatePartsTableRow(row, updatedPart);
                           if (row == EffectRightStudioPanel.this.selectedPartIdx) {
                              EffectRightStudioPanel.this.syncInspectorWithSelectedPart();
                           }

                           if (EffectRightStudioPanel.this.listener != null) {
                              EffectRightStudioPanel.this.listener.onPartPropsChanged(row, updatedPart);
                           }
                        });
                     }
                  } else if (SwingUtilities.isRightMouseButton(e)) {
                     int r = EffectRightStudioPanel.this.partsTable.rowAtPoint(e.getPoint());
                     if (r >= 0) {
                        EffectRightStudioPanel.this.partsTable.setRowSelectionInterval(r, r);
                        EffectRightStudioPanel.this.selectedPartIdx = r;
                        if (EffectRightStudioPanel.this.listener != null) {
                           EffectRightStudioPanel.this.listener.onPartSelected(r);
                        }

                        EffectRightStudioPanel.this.syncInspectorWithSelectedPart();
                        EffectRightStudioPanel.this.showPartsContextMenu(e.getComponent(), e.getX(), e.getY(), r);
                     }
                  }
               }
            }
         );
      this.partsTable.getInputMap(1).put(KeyStroke.getKeyStroke(127, 0), "delPart");
      this.partsTable.getActionMap().put("delPart", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int r = EffectRightStudioPanel.this.partsTable.getSelectedRow();
            if (r >= 0 && EffectRightStudioPanel.this.listener != null) {
               EffectRightStudioPanel.this.listener.onDeletePart(r);
            }
         }
      });
      this.partsTable.getInputMap(1).put(KeyStroke.getKeyStroke(10, 0), "quickEditPart");
      this.partsTable
         .getActionMap()
         .put(
            "quickEditPart",
            new AbstractAction() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  int row = EffectRightStudioPanel.this.partsTable.getSelectedRow();
                  if (row >= 0
                     && EffectRightStudioPanel.this.model != null
                     && EffectRightStudioPanel.this.model.frames != null
                     && EffectRightStudioPanel.this.selectedFrameIdx >= 0
                     && EffectRightStudioPanel.this.selectedFrameIdx < EffectRightStudioPanel.this.model.frames.length) {
                     EffFrame f = EffectRightStudioPanel.this.model.frames[EffectRightStudioPanel.this.selectedFrameIdx];
                     PartQuickEditDialog.showDialog(EffectRightStudioPanel.this, EffectRightStudioPanel.this.model, f, row, updatedPart -> {
                        EffectRightStudioPanel.this.updatePartsTableRow(row, updatedPart);
                        if (row == EffectRightStudioPanel.this.selectedPartIdx) {
                           EffectRightStudioPanel.this.syncInspectorWithSelectedPart();
                        }

                        if (EffectRightStudioPanel.this.listener != null) {
                           EffectRightStudioPanel.this.listener.onPartPropsChanged(row, updatedPart);
                        }
                     });
                  }
               }
            }
         );
      JScrollPane partsScroll = new JScrollPane(this.partsTable);
      partsScroll.setBorder(null);
      partsContainer.add(partsScroll, "Center");
      JPanel partsBtns = new JPanel(new FlowLayout(0, 4, 4));
      partsBtns.setBackground(new Color(20, 20, 28));
      JButton btnAddP = actionBtn("Thêm Part", Theme.ACCENT);
      btnAddP.addActionListener(e -> {
         if (this.listener != null) {
            int sid = this.selectedSpriteIdx >= 0 ? this.selectedSpriteIdx : 0;
            this.listener.onAddPart(sid);
         }
      });
      JButton btnDupP = actionBtn("Nhân Bản", new Color(40, 40, 55));
      btnDupP.addActionListener(e -> {
         if (this.listener != null && this.selectedPartIdx >= 0) {
            this.listener.onDuplicatePart(this.selectedPartIdx);
         }
      });
      JButton btnDelP = actionBtn("Xóa Part", new Color(180, 50, 50));
      btnDelP.addActionListener(e -> {
         if (this.listener != null && this.selectedPartIdx >= 0) {
            this.listener.onDeletePart(this.selectedPartIdx);
         }
      });
      JButton btnUpP = actionBtn("Lên Layer", new Color(0, 120, 180));
      btnUpP.addActionListener(e -> {
         if (this.listener != null && this.selectedPartIdx > 0) {
            this.listener.onMovePartLayer(this.selectedPartIdx, -1);
         }
      });
      JButton btnDnP = actionBtn("Xuống Layer", new Color(0, 120, 180));
      btnDnP.addActionListener(e -> {
         if (this.listener != null && this.selectedPartIdx >= 0) {
            this.listener.onMovePartLayer(this.selectedPartIdx, 1);
         }
      });
      partsBtns.add(btnAddP);
      partsBtns.add(btnDupP);
      partsBtns.add(btnDelP);
      partsBtns.add(btnUpP);
      partsBtns.add(btnDnP);
      partsContainer.add(partsBtns, "South");
      JSplitPane splitFrames = new JSplitPane(0, frameGalleryPnl, partsContainer);
      splitFrames.setDividerLocation(150);
      splitFrames.setBorder(null);
      splitFrames.setDividerSize(4);
      tabFrames.add(splitFrames, "Center");
      this.tabbedPane.addTab("Danh Sách Frame", tabFrames);
      JPanel tabAtlas = new JPanel(new BorderLayout(0, 2));
      tabAtlas.setBackground(BG);
      this.spriteSheetViewer = new SpriteSheetViewer();
      this.spriteSheetViewer.setListener(new SpriteSheetViewer.OnSpriteActionListener() {
         @Override
         public void onSpriteSelected(int spriteId, SmallImageDef def) {
            EffectRightStudioPanel.this.selectedSpriteIdx = spriteId;
            EffectRightStudioPanel.this.lblSpriteBadge.setText("Sprite #" + spriteId);
            EffectRightStudioPanel.this.highlightSpriteTableRow(spriteId);
            if (EffectRightStudioPanel.this.listener != null) {
               EffectRightStudioPanel.this.listener.onSpriteSelected(spriteId, def);
            }
         }

         @Override
         public void onSpriteDoubleClicked(int spriteId, SmallImageDef def) {
            EffectRightStudioPanel.this.selectedSpriteIdx = spriteId;
            if (EffectRightStudioPanel.this.listener != null) {
               EffectRightStudioPanel.this.listener.onAddPart(spriteId);
            }
         }

         @Override
         public void onOpenSpriteCutter() {
            if (EffectRightStudioPanel.this.listener != null) {
               EffectRightStudioPanel.this.listener.onOpenSpriteCutter();
            }
         }
      });
      JPanel atlasHdr = new JPanel(new BorderLayout());
      atlasHdr.setBackground(new Color(24, 24, 34));
      atlasHdr.setBorder(new EmptyBorder(4, 8, 4, 8));
      JLabel lblAH = new JLabel("BẢN ĐỒ SPRITE SHEET ATLAS");
      lblAH.setFont(new Font("Segoe UI", 1, 10));
      lblAH.setForeground(Theme.ACCENT);
      this.lblSpriteBadge = new JLabel("Sprite: —");
      this.lblSpriteBadge.setFont(new Font("Segoe UI", 1, 10));
      this.lblSpriteBadge.setForeground(new Color(0, 200, 255));
      atlasHdr.add(lblAH, "West");
      atlasHdr.add(this.lblSpriteBadge, "East");
      String[] sprCols = new String[]{"ID", "X", "Y", "W", "H"};
      this.spriteTableModel = new DefaultTableModel(sprCols, 0) {
         @Override
         public boolean isCellEditable(int r, int c) {
            return false;
         }
      };
      this.spriteTable = new JTable(this.spriteTableModel);
      styleTable(this.spriteTable);
      this.spriteTable.getSelectionModel().addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int row = this.spriteTable.getSelectedRow();
            if (row >= 0 && this.model != null && this.model.smallImages != null && row < this.model.smallImages.length) {
               this.selectedSpriteIdx = row;
               this.spriteSheetViewer.setSelectedSprite(row);
               this.lblSpriteBadge.setText("Sprite #" + row);
               if (this.listener != null) {
                  this.listener.onSpriteSelected(row, this.model.smallImages[row]);
               }
            }
         }
      });
      JScrollPane spriteTableScroll = new JScrollPane(this.spriteTable);
      spriteTableScroll.setBorder(null);
      JSplitPane atlasSplit = new JSplitPane(0, this.spriteSheetViewer, spriteTableScroll);
      atlasSplit.setDividerLocation(220);
      atlasSplit.setDividerSize(4);
      atlasSplit.setBorder(null);
      JPanel atlasQuickBar = new JPanel(new FlowLayout(0, 4, 4));
      atlasQuickBar.setBackground(new Color(20, 20, 28));
      JButton btnCutter = actionBtn("Cắt Sprite", new Color(160, 80, 0));
      btnCutter.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onOpenSpriteCutter();
         }
      });
      JButton btnImport = actionBtn("Import Atlas", new Color(110, 60, 170));
      btnImport.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onImportAtlas();
         }
      });
      JButton btnAddFromSpr = actionBtn("Thêm Vào Frame", Theme.ACCENT);
      btnAddFromSpr.addActionListener(e -> {
         if (this.listener != null) {
            int sid = this.selectedSpriteIdx >= 0 ? this.selectedSpriteIdx : 0;
            this.listener.onAddPart(sid);
         }
      });
      atlasQuickBar.add(btnAddFromSpr);
      atlasQuickBar.add(btnCutter);
      atlasQuickBar.add(btnImport);
      tabAtlas.add(atlasHdr, "North");
      tabAtlas.add(atlasSplit, "Center");
      tabAtlas.add(atlasQuickBar, "South");
      this.tabbedPane.addTab("Sprite Sheet", tabAtlas);
      JPanel tabInspector = new JPanel(new BorderLayout(0, 6));
      tabInspector.setBackground(BG);
      tabInspector.setBorder(new EmptyBorder(8, 8, 8, 8));
      this.partPropsTitle = new JLabel("Thuộc tính Part (Chưa chọn)");
      this.partPropsTitle.setFont(new Font("Segoe UI", 1, 11));
      this.partPropsTitle.setForeground(Theme.ACCENT);
      this.partPropsTitle.setHorizontalAlignment(0);
      tabInspector.add(this.partPropsTitle, "North");
      this.spDx = makeSpin(-9999, 9999);
      this.spDy = makeSpin(-9999, 9999);
      this.spImgId = makeSpin(0, 9999);
      this.spRotate = makeSpin(-360, 360);

      for (JSpinner s : new JSpinner[]{this.spDx, this.spDy, this.spImgId, this.spRotate}) {
         s.addChangeListener(e -> this.applyPropsFromUI());
      }

      this.subPreview = new EffectRightStudioPanel.PartSubPreview();
      JPanel inspContent = new JPanel();
      inspContent.setLayout(new BoxLayout(inspContent, 1));
      inspContent.setOpaque(false);
      JPanel row1 = new JPanel(new BorderLayout(8, 0));
      row1.setOpaque(false);
      row1.add(this.subPreview, "West");
      JPanel sprSelectPnl = new JPanel(new GridLayout(2, 1, 0, 4));
      sprSelectPnl.setOpaque(false);
      sprSelectPnl.add(dimLbl("Sprite ID:"));
      JPanel spinSprRow = new JPanel(new BorderLayout(3, 0));
      spinSprRow.setOpaque(false);
      JButton btnPrevSpr = makeNudgeBtn("Trước", "Sprite trước");
      JButton btnNextSpr = makeNudgeBtn("Kế", "Sprite kế tiếp");
      btnPrevSpr.addActionListener(e -> {
         int v = (Integer)this.spImgId.getValue();
         if (v > 0) {
            this.spImgId.setValue(v - 1);
         }
      });
      btnNextSpr.addActionListener(e -> {
         int v = (Integer)this.spImgId.getValue();
         if (this.model != null && this.model.smallImages != null && v < this.model.smallImages.length - 1) {
            this.spImgId.setValue(v + 1);
         }
      });
      spinSprRow.add(btnPrevSpr, "West");
      spinSprRow.add(this.spImgId, "Center");
      spinSprRow.add(btnNextSpr, "East");
      sprSelectPnl.add(spinSprRow);
      row1.add(sprSelectPnl, "Center");
      inspContent.add(row1);
      inspContent.add(Box.createVerticalStrut(8));
      JPanel coordsGrid = new JPanel(new GridLayout(1, 2, 6, 0));
      coordsGrid.setOpaque(false);
      JPanel xPnl = new JPanel(new BorderLayout(4, 0));
      xPnl.setOpaque(false);
      xPnl.add(dimLbl("dx:"), "West");
      xPnl.add(this.spDx, "Center");
      JPanel yPnl = new JPanel(new BorderLayout(4, 0));
      yPnl.setOpaque(false);
      yPnl.add(dimLbl("dy:"), "West");
      yPnl.add(this.spDy, "Center");
      coordsGrid.add(xPnl);
      coordsGrid.add(yPnl);
      inspContent.add(coordsGrid);
      inspContent.add(Box.createVerticalStrut(6));
      JPanel nudgePnl = new JPanel(new FlowLayout(1, 3, 0));
      nudgePnl.setOpaque(false);
      JButton btnNL = makeNudgeBtn("Trái", "Dịch trái (Shift=10px, Ctrl=5px)");
      JButton btnNU = makeNudgeBtn("Lên", "Dịch lên (Shift=10px, Ctrl=5px)");
      JButton btnND = makeNudgeBtn("Xuống", "Dịch xuống (Shift=10px, Ctrl=5px)");
      JButton btnNR = makeNudgeBtn("Phải", "Dịch phải (Shift=10px, Ctrl=5px)");
      JButton btnNRst = makeNudgeBtn("Về 0", "Đặt về tọa độ (0, 0)");
      btnNL.addActionListener(ev -> this.handleNudge(-1, 0, ev));
      btnNU.addActionListener(ev -> this.handleNudge(0, -1, ev));
      btnND.addActionListener(ev -> this.handleNudge(0, 1, ev));
      btnNR.addActionListener(ev -> this.handleNudge(1, 0, ev));
      btnNRst.addActionListener(ev -> {
         this.spDx.setValue(0);
         this.spDy.setValue(0);
         this.applyPropsFromUI();
      });
      nudgePnl.add(btnNL);
      nudgePnl.add(btnNU);
      nudgePnl.add(btnND);
      nudgePnl.add(btnNR);
      nudgePnl.add(btnNRst);
      inspContent.add(nudgePnl);
      inspContent.add(Box.createVerticalStrut(8));
      JPanel rotRow = new JPanel(new BorderLayout(4, 0));
      rotRow.setOpaque(false);
      rotRow.add(dimLbl("Góc:"), "West");
      rotRow.add(this.spRotate, "Center");
      JPanel rotBtns = new JPanel(new FlowLayout(2, 2, 0));
      rotBtns.setOpaque(false);
      JButton btnR0 = makeNudgeBtn("0°", "Đặt 0°");
      JButton btnR90 = makeNudgeBtn("90°", "+90°");
      JButton btnR180 = makeNudgeBtn("180°", "+180°");
      JButton btnR270 = makeNudgeBtn("270°", "+270°");
      btnR0.addActionListener(e -> this.spRotate.setValue(0));
      btnR90.addActionListener(e -> this.spRotate.setValue(((Integer)this.spRotate.getValue() + 90) % 360));
      btnR180.addActionListener(e -> this.spRotate.setValue(((Integer)this.spRotate.getValue() + 180) % 360));
      btnR270.addActionListener(e -> this.spRotate.setValue(((Integer)this.spRotate.getValue() + 270) % 360));
      rotBtns.add(btnR0);
      rotBtns.add(btnR90);
      rotBtns.add(btnR180);
      rotBtns.add(btnR270);
      rotRow.add(rotBtns, "East");
      inspContent.add(rotRow);
      inspContent.add(Box.createVerticalStrut(8));
      JPanel toggleGrid = new JPanel(new GridLayout(1, 2, 6, 0));
      toggleGrid.setOpaque(false);
      this.btnFlip = createToggle("Lật Ngang", Theme.ACCENT);
      this.btnOnTop = createToggle("Vẽ Đè Lên", new Color(140, 60, 200));
      this.btnFlip.addActionListener(e -> this.applyPropsFromUI());
      this.btnOnTop.addActionListener(e -> this.applyPropsFromUI());
      toggleGrid.add(this.btnFlip);
      toggleGrid.add(this.btnOnTop);
      inspContent.add(toggleGrid);
      if (this.isEffectAuto) {
         inspContent.add(Box.createVerticalStrut(10));
         JPanel autoPnl = new JPanel(new GridLayout(1, 2, 6, 0));
         autoPnl.setOpaque(false);
         autoPnl.setBorder(
            BorderFactory.createTitledBorder(
               BorderFactory.createLineBorder(new Color(50, 50, 65)), "EFFECT AUTO PARAMS", 0, 0, new Font("Segoe UI", 1, 9), Theme.ACCENT
            )
         );
         this.spAutoType = makeSpin(0, 255);
         this.spAutoValue = makeSpin(0, 255);
         this.spAutoType.addChangeListener(e -> {
            if (this.model instanceof EffectAutoModel && !this.updatingProps) {
               ((EffectAutoModel)this.model).typeEffect = (Integer)this.spAutoType.getValue();
               if (this.listener != null) {
                  this.listener.onAutoPropsChanged();
               }
            }
         });
         this.spAutoValue.addChangeListener(e -> {
            if (this.model instanceof EffectAutoModel && !this.updatingProps) {
               ((EffectAutoModel)this.model).valueEffect = (Integer)this.spAutoValue.getValue();
               if (this.listener != null) {
                  this.listener.onAutoPropsChanged();
               }
            }
         });
         JPanel tPnl = new JPanel(new BorderLayout(3, 0));
         tPnl.setOpaque(false);
         tPnl.add(dimLbl("Type:"), "West");
         tPnl.add(this.spAutoType, "Center");
         JPanel vPnl = new JPanel(new BorderLayout(3, 0));
         vPnl.setOpaque(false);
         vPnl.add(dimLbl("Value:"), "West");
         vPnl.add(this.spAutoValue, "Center");
         autoPnl.add(tPnl);
         autoPnl.add(vPnl);
         inspContent.add(autoPnl);
      }

      tabInspector.add(inspContent, "Center");
      this.tabbedPane.addTab("Thuộc Tính", tabInspector);
      this.add(this.tabbedPane, "Center");
   }

   public void setListener(EffectRightStudioPanel.OnStudioActionListener l) {
      this.listener = l;
   }

   public void setModel(EffectModel m, List<BufferedImage> thumbs) {
      this.model = m;
      this.thumbs = thumbs;
      this.spriteSheetViewer.setModel(m);
      this.frameListModel.clear();
      if (m != null && m.frames != null) {
         for (int i = 0; i < m.frames.length; i++) {
            this.frameListModel.addElement(i);
         }

         this.lblFrameBadge.setText(m.frames.length + " frames");
      } else {
         this.lblFrameBadge.setText("0 frames");
      }

      if (this.selectedFrameIdx >= this.frameListModel.size()) {
         this.selectedFrameIdx = Math.max(0, this.frameListModel.size() - 1);
      }

      if (this.frameListModel.size() > 0) {
         this.frameJList.setSelectedIndex(this.selectedFrameIdx);
      }

      this.spriteTableModel.setRowCount(0);
      if (m != null && m.smallImages != null) {
         for (int i = 0; i < m.smallImages.length; i++) {
            SmallImageDef s = m.smallImages[i];
            this.spriteTableModel.addRow(new Object[]{i, s.x, s.y, s.w, s.h});
         }
      }

      if (this.isEffectAuto && m instanceof EffectAutoModel && this.spAutoType != null) {
         this.updatingProps = true;
         this.spAutoType.setValue(((EffectAutoModel)m).typeEffect);
         this.spAutoValue.setValue(((EffectAutoModel)m).valueEffect);
         this.updatingProps = false;
      }

      this.updatePartsTable();
      this.syncInspectorWithSelectedPart();
      this.repaint();
   }

   public void selectFrame(int frameIdx) {
      if (frameIdx >= 0 && frameIdx < this.frameListModel.size() && frameIdx != this.frameJList.getSelectedIndex()) {
         this.selectedFrameIdx = frameIdx;
         this.frameJList.setSelectedIndex(frameIdx);
         this.frameJList.ensureIndexIsVisible(frameIdx);
         this.updatePartsTable();
         this.syncInspectorWithSelectedPart();
      }
   }

   public void selectPartRow(int partIdx) {
      if (partIdx >= 0 && partIdx < this.partsTableModel.getRowCount()) {
         this.selectedPartIdx = partIdx;
         this.partsTable.setRowSelectionInterval(partIdx, partIdx);
         this.partsTable.scrollRectToVisible(this.partsTable.getCellRect(partIdx, 0, true));
      } else {
         this.selectedPartIdx = -1;
         this.partsTable.clearSelection();
      }

      this.syncInspectorWithSelectedPart();
   }

   public void updatePartsTable() {
      this.partsTableModel.setRowCount(0);
      if (this.model != null && this.model.frames != null && this.selectedFrameIdx >= 0 && this.selectedFrameIdx < this.model.frames.length) {
         EffFrame f = this.model.frames[this.selectedFrameIdx];
         if (f != null && f.allParts != null) {
            for (int i = 0; i < f.allParts.size(); i++) {
               EffPartFrame p = f.allParts.get(i);
               this.partsTableModel.addRow(new Object[]{i, p.idSmallImg, p.dx, p.dy, p.rotate + "°", p.flip == 1 ? "Có" : "-", p.onTop == 1 ? "Có" : "-"});
            }

            if (this.selectedPartIdx >= 0 && this.selectedPartIdx < f.allParts.size()) {
               this.partsTable.setRowSelectionInterval(this.selectedPartIdx, this.selectedPartIdx);
            }
         }
      }
   }

   private void syncInspectorWithSelectedPart() {
      this.updatingProps = true;
      EffFrame f = this.model != null && this.model.frames != null && this.selectedFrameIdx >= 0 && this.selectedFrameIdx < this.model.frames.length
         ? this.model.frames[this.selectedFrameIdx]
         : null;
      if (f != null && this.selectedPartIdx >= 0 && f.allParts != null && this.selectedPartIdx < f.allParts.size()) {
         EffPartFrame p = f.allParts.get(this.selectedPartIdx);
         this.partPropsTitle.setText("Part #" + this.selectedPartIdx + " (Sprite #" + p.idSmallImg + ")");
         this.spDx.setValue(p.dx);
         this.spDy.setValue(p.dy);
         this.spRotate.setValue(p.rotate);
         this.spImgId.setValue(p.idSmallImg);
         this.btnFlip.setSelected(p.flip == 1);
         this.btnOnTop.setSelected(p.onTop == 1);
      } else {
         this.partPropsTitle.setText("Thuộc tính Part (Chưa chọn)");
         this.spDx.setValue(0);
         this.spDy.setValue(0);
         this.spRotate.setValue(0);
         this.spImgId.setValue(0);
         this.btnFlip.setSelected(false);
         this.btnOnTop.setSelected(false);
      }

      this.updatingProps = false;
      if (this.subPreview != null) {
         this.subPreview.repaint();
      }
   }

   private void applyPropsFromUI() {
      if (!this.updatingProps && this.model != null && this.model.frames != null) {
         if (this.selectedFrameIdx >= 0 && this.selectedFrameIdx < this.model.frames.length) {
            EffFrame f = this.model.frames[this.selectedFrameIdx];
            if (f != null && this.selectedPartIdx >= 0 && f.allParts != null && this.selectedPartIdx < f.allParts.size()) {
               EffPartFrame p = f.allParts.get(this.selectedPartIdx);
               p.dx = (Integer)this.spDx.getValue();
               p.dy = (Integer)this.spDy.getValue();
               p.rotate = (Integer)this.spRotate.getValue();
               p.idSmallImg = (Integer)this.spImgId.getValue();
               p.flip = this.btnFlip.isSelected() ? 1 : 0;
               p.onTop = this.btnOnTop.isSelected() ? 1 : 0;
               f.rebuildLayers();
               this.updatePartsTableRow(this.selectedPartIdx, p);
               if (this.listener != null) {
                  this.listener.onPartPropsChanged(this.selectedPartIdx, p);
               }

               if (this.subPreview != null) {
                  this.subPreview.repaint();
               }
            }
         }
      }
   }

   private void updatePartsTableRow(int row, EffPartFrame p) {
      if (row >= 0 && row < this.partsTableModel.getRowCount()) {
         this.partsTableModel.setValueAt(p.idSmallImg, row, 1);
         this.partsTableModel.setValueAt(p.dx, row, 2);
         this.partsTableModel.setValueAt(p.dy, row, 3);
         this.partsTableModel.setValueAt(p.rotate + "°", row, 4);
         this.partsTableModel.setValueAt(p.flip == 1 ? "Có" : "-", row, 5);
         this.partsTableModel.setValueAt(p.onTop == 1 ? "Có" : "-", row, 6);
      }
   }

   private void showPartsContextMenu(Component comp, int x, int y, int row) {
      if (this.model != null && this.model.frames != null && this.selectedFrameIdx >= 0 && this.selectedFrameIdx < this.model.frames.length) {
         EffFrame f = this.model.frames[this.selectedFrameIdx];
         if (f != null && f.allParts != null && row >= 0 && row < f.allParts.size()) {
            EffPartFrame p = f.allParts.get(row);
            JPopupMenu menu = new JPopupMenu();
            menu.setBackground(new Color(28, 28, 38));
            JMenuItem mEdit = new JMenuItem("✎ Chỉnh sửa chi tiết (Double-Click)...");
            mEdit.setFont(new Font("Segoe UI", 1, 11));
            mEdit.setForeground(new Color(0, 200, 255));
            mEdit.addActionListener(e -> PartQuickEditDialog.showDialog(this, this.model, f, row, updatedPart -> {
               this.updatePartsTableRow(row, updatedPart);
               if (row == this.selectedPartIdx) {
                  this.syncInspectorWithSelectedPart();
               }

               if (this.listener != null) {
                  this.listener.onPartPropsChanged(row, updatedPart);
               }
            }));
            JMenuItem mDup = new JMenuItem("⎘ Nhân bản Part");
            mDup.setForeground(Color.WHITE);
            mDup.addActionListener(e -> {
               if (this.listener != null) {
                  this.listener.onDuplicatePart(row);
               }
            });
            JMenuItem mRot0 = new JMenuItem("⟲ Đặt góc về 0°");
            mRot0.setForeground(Color.WHITE);
            mRot0.addActionListener(e -> {
               p.rotate = 0;
               this.updatePartsTableRow(row, p);
               if (row == this.selectedPartIdx) {
                  this.syncInspectorWithSelectedPart();
               }

               if (this.listener != null) {
                  this.listener.onPartPropsChanged(row, p);
               }
            });
            JMenuItem mRot90 = new JMenuItem("↷ Xoay thêm +90°");
            mRot90.setForeground(Color.WHITE);
            mRot90.addActionListener(e -> {
               p.rotate = (p.rotate + 90) % 360;
               this.updatePartsTableRow(row, p);
               if (row == this.selectedPartIdx) {
                  this.syncInspectorWithSelectedPart();
               }

               if (this.listener != null) {
                  this.listener.onPartPropsChanged(row, p);
               }
            });
            JMenuItem mFlip = new JMenuItem("↔ Đổi trạng thái Lật (Hiện: " + (p.flip == 1 ? "Đang Lật" : "Không") + ")");
            mFlip.setForeground(Color.WHITE);
            mFlip.addActionListener(e -> {
               p.flip = p.flip == 1 ? 0 : 1;
               this.updatePartsTableRow(row, p);
               if (row == this.selectedPartIdx) {
                  this.syncInspectorWithSelectedPart();
               }

               if (this.listener != null) {
                  this.listener.onPartPropsChanged(row, p);
               }
            });
            JMenuItem mTop = new JMenuItem("⬆ Đổi trạng thái Đè Layer (Hiện: " + (p.onTop == 1 ? "Top" : "Bottom") + ")");
            mTop.setForeground(Color.WHITE);
            mTop.addActionListener(e -> {
               p.onTop = p.onTop == 1 ? 0 : 1;
               f.rebuildLayers();
               this.updatePartsTableRow(row, p);
               if (row == this.selectedPartIdx) {
                  this.syncInspectorWithSelectedPart();
               }

               if (this.listener != null) {
                  this.listener.onPartPropsChanged(row, p);
               }
            });
            JMenuItem mDel = new JMenuItem("✖ Xóa Part (Del)");
            mDel.setForeground(new Color(255, 100, 100));
            mDel.addActionListener(e -> {
               if (this.listener != null) {
                  this.listener.onDeletePart(row);
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
   }

   private void highlightSpriteTableRow(int sid) {
      if (sid >= 0 && sid < this.spriteTableModel.getRowCount()) {
         this.spriteTable.setRowSelectionInterval(sid, sid);
         this.spriteTable.scrollRectToVisible(this.spriteTable.getCellRect(sid, 0, true));
      }
   }

   private void handleNudge(int dxM, int dyM, ActionEvent e) {
      if (this.selectedPartIdx >= 0) {
         int nudgeAmount = 1;
         if ((e.getModifiers() & 1) != 0) {
            nudgeAmount = 10;
         } else if ((e.getModifiers() & 2) != 0) {
            nudgeAmount = 5;
         }

         this.spDx.setValue((Integer)this.spDx.getValue() + dxM * nudgeAmount);
         this.spDy.setValue((Integer)this.spDy.getValue() + dyM * nudgeAmount);
         this.applyPropsFromUI();
      }
   }

   private static JLabel dimLbl(String t) {
      JLabel l = new JLabel(t);
      l.setFont(new Font("Segoe UI", 0, 10));
      l.setForeground(TEXT_DIM);
      return l;
   }

   private static JButton actionBtn(String text, Color bg) {
      JButton b = new JButton(text);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setForeground(Color.WHITE);
      b.setBackground(bg);
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 55, 75)), new EmptyBorder(3, 8, 3, 8)));
      b.setCursor(Cursor.getPredefinedCursor(12));
      return b;
   }

   private static JButton makeNudgeBtn(String text, String tooltip) {
      JButton btn = new JButton(text);
      btn.setFont(new Font("Segoe UI", 1, 9));
      btn.setBackground(new Color(30, 30, 42));
      btn.setForeground(new Color(190, 190, 210));
      btn.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 68)));
      btn.setFocusPainted(false);
      btn.setPreferredSize(new Dimension(38, 20));
      btn.setCursor(new Cursor(12));
      btn.setToolTipText(tooltip);
      return btn;
   }

   private static JToggleButton createToggle(String text, Color activeColor) {
      JToggleButton btn = new JToggleButton(text);
      btn.setFont(new Font("Segoe UI", 1, 9));
      btn.setBackground(new Color(30, 30, 40));
      btn.setForeground(new Color(170, 170, 190));
      btn.setFocusPainted(false);
      btn.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 65)));
      btn.setPreferredSize(new Dimension(0, 22));
      btn.setCursor(new Cursor(12));
      btn.addItemListener(e -> {
         btn.setBackground(btn.isSelected() ? activeColor : new Color(30, 30, 40));
         btn.setForeground(btn.isSelected() ? Color.WHITE : new Color(170, 170, 190));
      });
      return btn;
   }

   private static JSpinner makeSpin(int min, int max) {
      JSpinner s = new JSpinner(new SpinnerNumberModel(0, min, max, 1));
      s.setFont(new Font("Segoe UI", 0, 10));
      s.setBackground(new Color(28, 28, 36));
      s.setForeground(new Color(210, 210, 225));
      s.setBorder(BorderFactory.createLineBorder(new Color(55, 55, 70)));
      JComponent editor = s.getEditor();
      if (editor instanceof DefaultEditor) {
         JFormattedTextField tf = ((DefaultEditor)editor).getTextField();
         tf.setBackground(new Color(28, 28, 36));
         tf.setForeground(new Color(210, 210, 225));
         tf.setBorder(null);
      }

      return s;
   }

   private static void styleTable(JTable table) {
      table.setBackground(BG_TABLE);
      table.setForeground(TEXT_MAIN);
      table.setSelectionBackground(new Color(0, 95, 210));
      table.setSelectionForeground(Color.WHITE);
      table.setGridColor(new Color(35, 35, 48));
      table.setRowHeight(20);
      table.setFont(new Font("Segoe UI", 0, 10));
      table.getTableHeader().setBackground(new Color(26, 26, 36));
      table.getTableHeader().setForeground(new Color(160, 160, 180));
      table.getTableHeader().setFont(new Font("Segoe UI", 1, 9));
      DefaultTableCellRenderer center = new DefaultTableCellRenderer();
      center.setHorizontalAlignment(0);
      table.setDefaultRenderer(Object.class, center);
   }

   private class FrameCardCellRenderer extends JPanel implements ListCellRenderer<Integer> {
      private final JLabel lblThumb = new JLabel();
      private final JLabel lblInfo = new JLabel();

      FrameCardCellRenderer() {
         this.setLayout(new BorderLayout(0, 2));
         this.setOpaque(true);
         this.setBorder(new EmptyBorder(4, 4, 4, 4));
         this.lblThumb.setPreferredSize(new Dimension(64, 60));
         this.lblThumb.setHorizontalAlignment(0);
         this.lblThumb.setBorder(BorderFactory.createLineBorder(new Color(45, 45, 60)));
         this.add(this.lblThumb, "Center");
         this.lblInfo.setFont(new Font("Segoe UI", 1, 9));
         this.lblInfo.setHorizontalAlignment(0);
         this.add(this.lblInfo, "South");
      }

      public Component getListCellRendererComponent(JList<? extends Integer> list, Integer value, int index, boolean isSelected, boolean cellHasFocus) {
         int frameIdx = value;
         int partCount = EffectRightStudioPanel.this.model != null
               && EffectRightStudioPanel.this.model.frames != null
               && frameIdx < EffectRightStudioPanel.this.model.frames.length
            ? (EffectRightStudioPanel.this.model.frames[frameIdx].allParts != null ? EffectRightStudioPanel.this.model.frames[frameIdx].allParts.size() : 0)
            : 0;
         this.lblInfo.setText("F#" + frameIdx + " (" + partCount + "p)");
         if (isSelected) {
            this.setBackground(new Color(0, 95, 210));
            this.lblInfo.setForeground(Color.WHITE);
            this.lblThumb.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 255), 2));
         } else {
            this.setBackground(new Color(24, 24, 32));
            this.lblInfo.setForeground(EffectRightStudioPanel.TEXT_MAIN);
            this.lblThumb.setBorder(BorderFactory.createLineBorder(new Color(45, 45, 60)));
         }

         if (EffectRightStudioPanel.this.thumbs != null && frameIdx >= 0 && frameIdx < EffectRightStudioPanel.this.thumbs.size()) {
            BufferedImage thumb = EffectRightStudioPanel.this.thumbs.get(frameIdx);
            if (thumb != null) {
               Image scaled = thumb.getScaledInstance(60, 56, 4);
               this.lblThumb.setIcon(new ImageIcon(scaled));
               this.lblThumb.setText("");
            } else {
               this.lblThumb.setIcon(null);
               this.lblThumb.setText("F#" + frameIdx);
            }
         } else {
            this.lblThumb.setIcon(null);
            this.lblThumb.setText("F#" + frameIdx);
         }

         return this;
      }
   }

   public interface OnStudioActionListener {
      void onFrameSelected(int var1);

      void onAddFrame();

      void onDuplicateFrame(int var1);

      void onDeleteFrame(int var1);

      void onInsertFrameToSeq(int var1);

      void onReorderFrame(int var1, int var2);

      void onPartSelected(int var1);

      void onAddPart(int var1);

      void onDuplicatePart(int var1);

      void onDeletePart(int var1);

      void onMovePartLayer(int var1, int var2);

      void onPartPropsChanged(int var1, EffPartFrame var2);

      void onSpriteSelected(int var1, SmallImageDef var2);

      void onOpenSpriteCutter();

      void onImportAtlas();

      void onAutoPropsChanged();
   }

   private class PartSubPreview extends JPanel {
      PartSubPreview() {
         this.setPreferredSize(new Dimension(56, 56));
         this.setBackground(new Color(15, 15, 20));
         this.setBorder(BorderFactory.createLineBorder(new Color(48, 48, 62)));
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (EffectRightStudioPanel.this.model != null
            && EffectRightStudioPanel.this.model.atlasImage != null
            && EffectRightStudioPanel.this.model.smallImages != null) {
            int sid = (Integer)EffectRightStudioPanel.this.spImgId.getValue();
            if (sid >= 0 && sid < EffectRightStudioPanel.this.model.smallImages.length) {
               SmallImageDef s = EffectRightStudioPanel.this.model.smallImages[sid];
               Graphics2D g2 = (Graphics2D)g;
               g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
               int tw = this.getWidth() - 4;
               int th = this.getHeight() - 4;
               if (tw > 0 && th > 0) {
                  double scale = Math.min((double)tw / Math.max(1, s.w), (double)th / Math.max(1, s.h));
                  if (scale > 4.0) {
                     scale = 4.0;
                  }

                  int dw = (int)(s.w * scale);
                  int dh = (int)(s.h * scale);
                  int dx = (this.getWidth() - dw) / 2;
                  int dy = (this.getHeight() - dh) / 2;
                  g2.drawImage(EffectRightStudioPanel.this.model.atlasImage, dx, dy, dx + dw, dy + dh, s.x * 4, s.y * 4, (s.x + s.w) * 4, (s.y + s.h) * 4, null);
               }
            }
         }
      }
   }
}
