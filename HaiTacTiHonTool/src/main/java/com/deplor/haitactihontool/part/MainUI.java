package com.deplor.haitactihontool.part;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.ImageZoomHelper;
import com.deplor.haitactihontool.config.Lang;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import com.deplor.haitactihontool.util.GifExportDialog;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainUI extends JPanel {
   private List<mPart> allParts;
   private short[] currentFashion;
   private int selectedSlot = -1;
   private mPart editingPart;
   private int currentFrameIndex = -1;
   private JTextField txtFashionArray;
   private JTextArea[] txtSlots;
   private DefaultListModel<String> frameListModel;
   private JList<String> frameList;
   private PartCanvas canvas;
   private JTextField txtImgId;
   private JSpinner spinDx;
   private JSpinner spinDy;
   private JSpinner spinBatchDx;
   private JSpinner spinBatchDy;
   private JSlider animSlider;
   private JPanel stripContainer;
   private JScrollPane stripScroll;
   private JCheckBox chkPlay;
   private JComboBox<String> comboStatus;
   private JSpinner spinPoseFrame;
   private JCheckBox chkFlip;
   private boolean isUpdatingFields = false;
   private boolean isUpdatingFromCanvas = false;
   private static String lastBrowseDir = System.getProperty("user.dir");
   private DefaultListModel<String> presetListModel;
   private JList<String> presetList;
   private Map<String, short[]> presetsMap;
   private JPanel dollGridPanel;
   private PartImage copiedFrameData = null;
   private JLabel lblCanvasInfo;
   private JLabel lblFrameInfo;
   private boolean isSelectingPreset = false;

   public MainUI() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      this.allParts = PartDataManager.loadParts();
      this.canvas = new PartCanvas();
      this.canvas.setListener(new PartCanvas.OnPartInteraction() {
         @Override
         public void onPartSelected(int slotId, int imageIdx) {
            int wearIdx = MainUI.this.paintSlotToWearSlot(slotId);
            if (wearIdx != -1) {
               MainUI.this.selectedSlot = wearIdx;
               MainUI.this.updateEditingPart();
               MainUI.this.updateDollGrid();
               SwingUtilities.invokeLater(() -> {
                  if (imageIdx >= 0 && imageIdx < MainUI.this.frameList.getModel().getSize()) {
                     MainUI.this.frameList.setSelectedIndex(imageIdx);
                     MainUI.this.scrollStripToIndex(imageIdx);
                  }
               });
            }
         }

         @Override
         public void onPartDragged() {
            MainUI.this.updateInspector();
            MainUI.this.updateFashionData();
         }
      });
      this.animSlider = new JSlider(0, 10, 0);
      this.animSlider.setOpaque(false);
      this.animSlider.addChangeListener(e -> {
         if (!this.isUpdatingFromCanvas) {
            this.canvas.setAnimTick(this.animSlider.getValue());
         }
      });
      this.canvas
         .setAnimListener(
            (tick, max, charFrame) -> {
               this.isUpdatingFromCanvas = true;
               this.animSlider.setMaximum(max > 0 ? max - 1 : 0);
               this.animSlider.setValue(tick);
               if (this.spinPoseFrame != null && !this.spinPoseFrame.getValue().equals(charFrame)) {
                  this.spinPoseFrame.setValue(charFrame);
               }

               if (this.lblFrameInfo != null) {
                  this.lblFrameInfo.setText("F:" + charFrame + " (" + (tick + 1) + "/" + max + ")");
               }

               if (this.lblCanvasInfo != null && this.canvas != null) {
                  String pName = CharInfoData.getPoseName(charFrame);
                  this.lblCanvasInfo
                     .setText(
                        "Pose: F"
                           + charFrame
                           + " ("
                           + pName
                           + ") | Mode: "
                           + this.canvas.getStatusName()
                           + " | Dir: "
                           + (this.canvas.getDirection() == 2 ? "Right" : "Left")
                           + " | Zoom: "
                           + String.format("%.1fx", this.canvas.getZoom())
                     );
               }

               this.isUpdatingFromCanvas = false;
               SwingUtilities.invokeLater(() -> this.syncStripHighlightToCanvas());
            }
         );
      this.canvas
         .addKeyListener(
            new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                  int key = e.getKeyCode();
                  int step = e.isShiftDown() ? 5 : 1;
                  if (key == 32) {
                     boolean p = !MainUI.this.canvas.getPlaying();
                     MainUI.this.canvas.setPlaying(p);
                     if (MainUI.this.chkPlay != null) {
                        MainUI.this.chkPlay.setSelected(p);
                     }

                     if (MainUI.this.spinPoseFrame != null) {
                        MainUI.this.spinPoseFrame.setEnabled(!p || MainUI.this.comboStatus != null && MainUI.this.comboStatus.getSelectedIndex() == 12);
                     }
                  } else if (key == 70) {
                     int d = MainUI.this.canvas.getDirection() == 2 ? 0 : 2;
                     MainUI.this.canvas.setDirection(d);
                     if (MainUI.this.chkFlip != null) {
                        MainUI.this.chkFlip.setSelected(d == 2);
                     }
                  } else if (key == 82) {
                     MainUI.this.resetOffsets();
                  } else if (MainUI.this.editingPart != null
                     && MainUI.this.currentFrameIndex >= 0
                     && MainUI.this.currentFrameIndex < MainUI.this.editingPart.pi.length) {
                     if (key == 38 || key == 87) {
                        MainUI.this.nudgeDy(-step);
                     } else if (key == 40 || key == 83) {
                        MainUI.this.nudgeDy(step);
                     } else if (key == 37 || key == 65) {
                        MainUI.this.nudgeDx(MainUI.this.canvas.getDirection() == 2 ? step : -step);
                     } else if (key == 39 || key == 68) {
                        MainUI.this.nudgeDx(MainUI.this.canvas.getDirection() == 2 ? -step : step);
                     }
                  }
               }
            }
         );
      this.initTopBar();
      JPanel leftPanel = this.buildLeftPanel();
      JPanel centerPanel = this.buildCenterPanel();
      JPanel rightPanel = this.buildRightPanel();
      JSplitPane splitLeft = new JSplitPane(1, leftPanel, centerPanel);
      splitLeft.setDividerLocation(300);
      splitLeft.setDividerSize(4);
      splitLeft.setContinuousLayout(true);
      splitLeft.setBorder(null);
      splitLeft.setBackground(Theme.BG_DARKER);
      JSplitPane splitRight = new JSplitPane(1, splitLeft, rightPanel);
      splitRight.setDividerLocation(980);
      splitRight.setDividerSize(4);
      splitRight.setContinuousLayout(true);
      splitRight.setBorder(null);
      splitRight.setBackground(Theme.BG_DARKER);
      this.add(splitRight, "Center");
      this.setupDragAndDrop();
      this.txtFashionArray.setText("[1045,-2,-1,1043,-1,1044,1042,-2]");
      this.loadPresets();
      this.loadFashion();
      this.syncPresetHighlight();
   }

   private static int resolveLogicalId(int filename) {
      if (filename >= 26000) {
         return filename - 16000;
      } else {
         return filename >= 10000 ? filename - 10000 : filename;
      }
   }

   private static int resolvePhysicalId(int filename) {
      if (filename >= 10000) {
         return filename;
      } else {
         return filename > 0 ? filename + 10000 : filename;
      }
   }

   private void initTopBar() {
      JPanel topPanel = new JPanel(new BorderLayout());
      topPanel.setBackground(Theme.BG_DARK);
      topPanel.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      topPanel.setPreferredSize(new Dimension(0, 60));
      JPanel leftSide = new JPanel(new FlowLayout(0, 14, 14));
      leftSide.setOpaque(false);
      JLabel lblFashion = StyledUI.createLabel(Lang.get("lbl_fashion_array"), Theme.F_BOLD, Theme.TEXT_DIM);
      this.txtFashionArray = StyledUI.createTextField("[1045,-2,-1,1043,-1,1044,1042,-2]");
      this.txtFashionArray.setPreferredSize(new Dimension(310, 30));
      JButton btnLoadFashion = StyledUI.createButton(Lang.get("load"), Theme.ACCENT);
      btnLoadFashion.setPreferredSize(new Dimension(80, 30));
      btnLoadFashion.addActionListener(e -> this.loadFashion());
      this.lblCanvasInfo = StyledUI.createLabel("Frame: - | Dir: - | Zoom: -", Theme.F_TINY, Theme.TEXT_MUTED);
      leftSide.add(lblFashion);
      leftSide.add(this.txtFashionArray);
      leftSide.add(btnLoadFashion);
      leftSide.add(this.lblCanvasInfo);
      JPanel rightSide = new JPanel(new FlowLayout(2, 10, 14));
      rightSide.setOpaque(false);
      JButton btnSavePresets = StyledUI.createButton("LƯU PRESET", Theme.PURPLE);
      btnSavePresets.setPreferredSize(new Dimension(120, 30));
      btnSavePresets.addActionListener(e -> this.saveCurrentPresetDialog());
      JButton btnSave = StyledUI.createButton("LƯU DỮ LIỆU", Theme.SUCCESS);
      btnSave.setPreferredSize(new Dimension(140, 30));
      btnSave.setToolTipText("Lưu Part (Tùy chọn SQL, JSON, SmallImage, Animated GIF)");
      btnSave.addActionListener(e -> this.triggerSessionSave());
      rightSide.add(btnSavePresets);
      rightSide.add(btnSave);
      topPanel.add(leftSide, "West");
      topPanel.add(rightSide, "East");
      this.add(topPanel, "North");
   }

   private void openPartGifExport(boolean allMode) {
      if (this.currentFashion != null && this.allParts != null) {
         Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
         String defaultOut = AppConfig.getAppDirectory() + "/Data/Fashion/gifs/";
         GifExportDialog dlg = GifExportDialog.forPart(
            owner,
            this.currentFashion,
            this.allParts,
            this.canvas != null ? this.canvas.getFrameIndex() : 0,
            this.canvas != null ? this.canvas.getDirection() : 0,
            defaultOut
         );
         dlg.setVisible(true);
      } else {
         JOptionPane.showMessageDialog(this, "Chưa có dữ liệu trang phục để xuất GIF!");
      }
   }

   private JPanel buildLeftPanel() {
      JPanel p = new JPanel(new GridBagLayout());
      p.setPreferredSize(new Dimension(300, 0));
      p.setBackground(Theme.BG_DARK);
      p.setBorder(new MatteBorder(0, 0, 0, 1, Theme.BORDER));
      GridBagConstraints c = new GridBagConstraints();
      c.fill = 1;
      c.insets = new Insets(8, 8, 8, 8);
      c.weightx = 1.0;
      JPanel presetsCard = StyledUI.createCard(new BorderLayout(5, 5));
      presetsCard.setBorder(new CompoundBorder(StyledUI.createTitledBorder("OUTFIT PRESETS"), new EmptyBorder(5, 5, 5, 5)));
      this.presetListModel = new DefaultListModel<>();
      this.presetList = new JList<>(this.presetListModel);
      this.presetList.setBackground(Theme.BG_DARKER);
      this.presetList.setForeground(Theme.TEXT_MAIN);
      this.presetList.setSelectionBackground(Theme.ACCENT_SOFT);
      this.presetList.setSelectionForeground(Theme.ACCENT);
      this.presetList.setFont(Theme.F_SMALL);
      this.presetList.setFixedCellHeight(26);
      this.presetList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting() && !this.isSelectingPreset) {
            String val = this.presetList.getSelectedValue();
            if (val != null && this.presetsMap.containsKey(val)) {
               short[] fashion = this.presetsMap.get(val);
               StringBuilder sb = new StringBuilder("[");

               for (int i = 0; i < fashion.length; i++) {
                  sb.append(fashion[i]);
                  if (i < fashion.length - 1) {
                     sb.append(",");
                  }
               }

               sb.append("]");
               this.txtFashionArray.setText(sb.toString());
               this.loadFashion();
            }
         }
      });
      JScrollPane presetScroll = new JScrollPane(this.presetList);
      StyledUI.styleScrollPane(presetScroll);
      presetScroll.setPreferredSize(new Dimension(0, 100));
      presetsCard.add(presetScroll, "Center");
      JPanel presetBtns = new JPanel(new GridLayout(1, 2, 5, 0));
      presetBtns.setOpaque(false);
      JButton btnAddP = StyledUI.createButton("THÊM", Theme.SUCCESS);
      btnAddP.setFont(Theme.F_TINY);
      btnAddP.addActionListener(e -> this.saveCurrentPresetDialog());
      JButton btnDelP = StyledUI.createButton("XÓA", Theme.ERROR);
      btnDelP.setFont(Theme.F_TINY);
      btnDelP.addActionListener(e -> this.deleteSelectedPreset());
      presetBtns.add(btnAddP);
      presetBtns.add(btnDelP);
      presetsCard.add(presetBtns, "South");
      c.gridx = 0;
      c.gridy = 0;
      c.weighty = 0.3;
      p.add(presetsCard, c);
      JPanel dollGridCard = StyledUI.createCard(new BorderLayout());
      dollGridCard.setBorder(new CompoundBorder(StyledUI.createTitledBorder("EQUIPMENT SLOTS"), new EmptyBorder(5, 5, 5, 5)));
      this.dollGridPanel = new JPanel(new GridLayout(4, 2, 8, 8));
      this.dollGridPanel.setOpaque(false);
      JScrollPane dollScroll = new JScrollPane(this.dollGridPanel);
      StyledUI.styleScrollPane(dollScroll);
      dollGridCard.add(dollScroll, "Center");
      JPanel dollActions = new JPanel(new GridLayout(1, 2, 5, 0));
      dollActions.setOpaque(false);
      JButton btnNewPart = StyledUI.createButton("TẠO PART MỚI", Theme.SUCCESS);
      btnNewPart.setFont(Theme.F_TINY);
      btnNewPart.addActionListener(e -> this.createNewPart());
      JButton btnSelector = StyledUI.createButton("CHỌN PART", Theme.ACCENT);
      btnSelector.setFont(Theme.F_TINY);
      btnSelector.addActionListener(e -> this.showQuickPartSelector());
      dollActions.add(btnNewPart);
      dollActions.add(btnSelector);
      dollGridCard.add(dollActions, "South");
      c.gridx = 0;
      c.gridy = 1;
      c.weighty = 0.7;
      p.add(dollGridCard, c);
      return p;
   }

   private JPanel buildCenterPanel() {
      JPanel centerPanel = new JPanel(new BorderLayout());
      centerPanel.setBackground(Theme.BG_DARKER);
      JPanel controlBar = new JPanel(new FlowLayout(0, 10, 6));
      controlBar.setBackground(Theme.BG_DARK);
      controlBar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      this.chkPlay = StyledUI.createCheckBox(Lang.get("chk_play"));
      this.chkPlay.addActionListener(e -> {
         boolean playing = this.chkPlay.isSelected();
         this.canvas.setPlaying(playing);
         if (this.spinPoseFrame != null) {
            this.spinPoseFrame.setEnabled(!playing || this.comboStatus != null && this.comboStatus.getSelectedIndex() == 12);
         }
      });
      this.comboStatus = new JComboBox<>(PartCanvas.STATUS_NAMES);
      this.comboStatus.setBackground(Theme.BG_CARD);
      this.comboStatus.setForeground(Theme.TEXT_MAIN);
      this.comboStatus.setFont(Theme.F_TINY);
      this.comboStatus.addActionListener(e -> {
         int sel = this.comboStatus.getSelectedIndex();
         this.canvas.setStatusMe(sel);
         this.animSlider.setValue(0);
         this.updateSliderMax();
         if (this.spinPoseFrame != null) {
            this.spinPoseFrame.setEnabled(sel == 12 || !this.canvas.getPlaying());
            if (sel == 12) {
               this.canvas.setSinglePoseFrame(((Number)this.spinPoseFrame.getValue()).intValue());
            }
         }
      });
      JLabel lblPose = StyledUI.createLabel("POSE:", Theme.F_TINY, Theme.TEXT_DIM);
      this.spinPoseFrame = new JSpinner(new SpinnerNumberModel(0, 0, CharInfoData.CharInfo.length - 1, 1));
      this.spinPoseFrame.setPreferredSize(new Dimension(48, 24));
      this.spinPoseFrame.setFont(Theme.F_TINY);
      this.spinPoseFrame.setToolTipText("Chọn trực tiếp Pose 0..61 (dùng cho Single Pose hoặc preview)");
      this.spinPoseFrame.addChangeListener(e -> {
         if (!this.isUpdatingFromCanvas) {
            int targetPose = ((Number)this.spinPoseFrame.getValue()).intValue();
            this.canvas.setSinglePoseFrame(targetPose);
         }
      });
      this.chkFlip = StyledUI.createCheckBox(Lang.get("chk_flip"));
      this.chkFlip.addActionListener(e -> this.canvas.setDirection(this.chkFlip.isSelected() ? 2 : 0));
      JCheckBox chkGrid = StyledUI.createCheckBox("Grid");
      chkGrid.setSelected(true);
      chkGrid.addActionListener(e -> this.canvas.setShowGrid(chkGrid.isSelected()));
      JCheckBox chkAxes = StyledUI.createCheckBox("Axes");
      chkAxes.setSelected(true);
      chkAxes.addActionListener(e -> this.canvas.setShowAxes(chkAxes.isSelected()));
      JCheckBox chkOnion = StyledUI.createCheckBox("Onion");
      chkOnion.addActionListener(e -> this.canvas.setOnionSkinning(chkOnion.isSelected()));
      JButton btnJumpFrame = StyledUI.createButton("ĐỒNG BỘ FRAME", Theme.BG_CARD);
      btnJumpFrame.setFont(Theme.F_TINY);
      btnJumpFrame.setToolTipText("Nhảy đến frame đang chạy trên canvas");
      btnJumpFrame.addActionListener(e -> this.jumpToCanvasFrame());
      controlBar.add(this.chkPlay);
      controlBar.add(this.comboStatus);
      controlBar.add(lblPose);
      controlBar.add(this.spinPoseFrame);
      controlBar.add(this.chkFlip);
      controlBar.add(new JSeparator(1));
      controlBar.add(chkGrid);
      controlBar.add(chkAxes);
      controlBar.add(chkOnion);
      controlBar.add(btnJumpFrame);
      JPanel sliderBar = new JPanel(new BorderLayout(6, 0));
      sliderBar.setBackground(Theme.BG_DARK);
      sliderBar.setBorder(new EmptyBorder(3, 8, 3, 8));
      sliderBar.setPreferredSize(new Dimension(0, 30));
      JLabel lblSlider = StyledUI.createLabel("TICK:", Theme.F_TINY, Theme.TEXT_DIM);
      lblSlider.setPreferredSize(new Dimension(40, 0));
      sliderBar.add(lblSlider, "West");
      sliderBar.add(this.animSlider, "Center");
      this.lblFrameInfo = StyledUI.createLabel("", Theme.F_TINY, Theme.ACCENT);
      this.lblFrameInfo.setPreferredSize(new Dimension(80, 0));
      sliderBar.add(this.lblFrameInfo, "East");
      JPanel canvasWrap = new JPanel(new BorderLayout());
      canvasWrap.setOpaque(false);
      canvasWrap.add(controlBar, "North");
      canvasWrap.add(this.canvas, "Center");
      centerPanel.add(sliderBar, "North");
      centerPanel.add(canvasWrap, "Center");
      JPanel timelinePanel = new JPanel(new BorderLayout());
      timelinePanel.setBackground(Theme.BG_DARK);
      timelinePanel.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      JPanel timelineHeader = new JPanel(new BorderLayout(8, 0));
      timelineHeader.setBackground(new Color(22, 22, 30));
      timelineHeader.setBorder(new EmptyBorder(4, 10, 4, 10));
      JLabel lblTimeline = StyledUI.createLabel("FRAME TIMELINE", Theme.F_BOLD, Theme.ACCENT);
      timelineHeader.add(lblTimeline, "West");
      JPanel timelineActions = new JPanel(new FlowLayout(2, 6, 0));
      timelineActions.setOpaque(false);
      JButton btnDupFrame = this.mkTimelineBtn("⧉ DUP", Theme.ACCENT_SOFT, "Duplicate frame hiện tại sang frame kế");
      btnDupFrame.addActionListener(e -> this.duplicateFrame());
      JButton btnCopyFrame = this.mkTimelineBtn("COPY", Theme.BG_CARD, "Copy frame data");
      btnCopyFrame.addActionListener(e -> this.copyFrame());
      JButton btnPasteFrame = this.mkTimelineBtn("PASTE", Theme.BG_CARD, "Paste frame data");
      btnPasteFrame.addActionListener(e -> this.pasteFrame());
      JButton btnClearFrame = this.mkTimelineBtn("CLEAR", Theme.ERROR, "Xóa frame hiện tại");
      btnClearFrame.addActionListener(e -> this.clearFrame());
      JButton btnImportFolder = this.mkTimelineBtn("IMPORT FOLDER", Theme.PURPLE, "Import toàn bộ ảnh từ thư mục vào part từ frame đang chọn");
      btnImportFolder.addActionListener(e -> this.browseMultipleImages());
      timelineActions.add(btnDupFrame);
      timelineActions.add(btnCopyFrame);
      timelineActions.add(btnPasteFrame);
      timelineActions.add(btnClearFrame);
      timelineActions.add(btnImportFolder);
      timelineHeader.add(timelineActions, "East");
      timelinePanel.add(timelineHeader, "North");
      this.stripContainer = new JPanel(new FlowLayout(0, 8, 8));
      this.stripContainer.setBackground(new Color(14, 14, 18));
      this.stripScroll = new JScrollPane(this.stripContainer);
      this.stripScroll.setPreferredSize(new Dimension(0, 110));
      this.stripScroll.setBorder(null);
      this.stripScroll.setVerticalScrollBarPolicy(21);
      this.stripScroll.getHorizontalScrollBar().setUnitIncrement(24);
      StyledUI.styleScrollPane(this.stripScroll);
      timelinePanel.add(this.stripScroll, "Center");
      centerPanel.add(timelinePanel, "South");
      return centerPanel;
   }

   private JButton mkTimelineBtn(String text, Color bg, String tip) {
      JButton b = StyledUI.createButton(text, bg);
      b.setFont(Theme.F_TINY);
      b.setPreferredSize(new Dimension(text.length() * 6 + 20, 24));
      b.setToolTipText(tip);
      return b;
   }

   private JPanel buildRightPanel() {
      JPanel rightPanel = new JPanel(new GridBagLayout());
      rightPanel.setPreferredSize(new Dimension(300, 0));
      rightPanel.setBackground(Theme.BG_DARK);
      rightPanel.setBorder(new MatteBorder(0, 1, 0, 0, Theme.BORDER));
      GridBagConstraints c = new GridBagConstraints();
      c.fill = 1;
      c.insets = new Insets(8, 8, 8, 8);
      c.weightx = 1.0;
      JPanel inspectorCard = StyledUI.createCard(new GridBagLayout());
      inspectorCard.setBorder(new CompoundBorder(StyledUI.createTitledBorder("FRAME INSPECTOR"), new EmptyBorder(6, 6, 6, 6)));
      GridBagConstraints gc = new GridBagConstraints();
      gc.fill = 2;
      gc.insets = new Insets(3, 3, 3, 3);
      gc.gridx = 0;
      gc.gridy = 0;
      gc.gridwidth = 4;
      JLabel lblSlotInfo = StyledUI.createLabel("Slot: — | Part: — | Frame Index: —", Theme.F_TINY, Theme.ACCENT);
      lblSlotInfo.setName("slotInfo");
      inspectorCard.add(lblSlotInfo, gc);
      gc.gridx = 0;
      gc.gridy = 1;
      gc.gridwidth = 1;
      inspectorCard.add(StyledUI.createLabel(Lang.get("lbl_img_id"), Theme.F_SMALL, Theme.TEXT_DIM), gc);
      this.txtImgId = StyledUI.createTextField("");
      this.txtImgId.setPreferredSize(new Dimension(80, 26));
      this.txtImgId.setToolTipText("Logical ID của ảnh (tool tự tính physical filename)");
      this.txtImgId.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            MainUI.this.applyFields();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            MainUI.this.applyFields();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            MainUI.this.applyFields();
         }
      });
      gc.gridx = 1;
      gc.gridwidth = 2;
      inspectorCard.add(this.txtImgId, gc);
      JButton btnBrowse = StyledUI.createButton("...", Theme.PURPLE);
      btnBrowse.setPreferredSize(new Dimension(32, 26));
      btnBrowse.setFont(Theme.F_TINY);
      btnBrowse.setToolTipText("Browse image file");
      btnBrowse.addActionListener(e -> this.browseImage());
      gc.gridx = 3;
      gc.gridwidth = 1;
      inspectorCard.add(btnBrowse, gc);
      this.spinDx = new JSpinner(new SpinnerNumberModel(0, -128, 127, 1));
      this.spinDy = new JSpinner(new SpinnerNumberModel(0, -128, 127, 1));
      this.spinDx.addChangeListener(e -> this.applyFields());
      this.spinDy.addChangeListener(e -> this.applyFields());
      gc.gridx = 0;
      gc.gridy = 2;
      gc.gridwidth = 1;
      inspectorCard.add(StyledUI.createLabel("Offset X", Theme.F_SMALL, Theme.TEXT_DIM), gc);
      gc.gridx = 1;
      gc.gridwidth = 3;
      inspectorCard.add(this.buildNudgeRow(() -> this.nudgeDx(-10), () -> this.nudgeDx(-1), () -> this.nudgeDx(1), () -> this.nudgeDx(10), this.spinDx), gc);
      gc.gridx = 0;
      gc.gridy = 3;
      gc.gridwidth = 1;
      inspectorCard.add(StyledUI.createLabel("Offset Y", Theme.F_SMALL, Theme.TEXT_DIM), gc);
      gc.gridx = 1;
      gc.gridwidth = 3;
      inspectorCard.add(this.buildNudgeRow(() -> this.nudgeDy(-10), () -> this.nudgeDy(-1), () -> this.nudgeDy(1), () -> this.nudgeDy(10), this.spinDy), gc);
      JButton btnReset = StyledUI.createButton("RESET OFFSETS", Theme.WARNING);
      btnReset.setFont(Theme.F_TINY);
      btnReset.addActionListener(e -> this.resetOffsets());
      gc.gridx = 0;
      gc.gridy = 4;
      gc.gridwidth = 4;
      inspectorCard.add(btnReset, gc);
      gc.gridy = 5;
      inspectorCard.add(StyledUI.createLabel("Hotkeys: WASD / Arrow Keys trên canvas", Theme.F_TINY, Theme.TEXT_MUTED), gc);
      c.gridx = 0;
      c.gridy = 0;
      c.weighty = 0.4;
      rightPanel.add(inspectorCard, c);
      JPanel batchCard = StyledUI.createCard(new GridBagLayout());
      batchCard.setBorder(new CompoundBorder(StyledUI.createTitledBorder("BATCH UTILITIES"), new EmptyBorder(6, 6, 6, 6)));
      GridBagConstraints bgc = new GridBagConstraints();
      bgc.fill = 2;
      bgc.insets = new Insets(3, 3, 3, 3);
      bgc.gridx = 0;
      bgc.gridy = 0;
      bgc.gridwidth = 1;
      batchCard.add(StyledUI.createLabel("Shift X", Theme.F_SMALL, Theme.TEXT_DIM), bgc);
      this.spinBatchDx = new JSpinner(new SpinnerNumberModel(0, -128, 127, 1));
      bgc.gridx = 1;
      batchCard.add(this.spinBatchDx, bgc);
      bgc.gridx = 2;
      batchCard.add(StyledUI.createLabel("Shift Y", Theme.F_SMALL, Theme.TEXT_DIM), bgc);
      this.spinBatchDy = new JSpinner(new SpinnerNumberModel(0, -128, 127, 1));
      bgc.gridx = 3;
      batchCard.add(this.spinBatchDy, bgc);
      JButton btnBatchShift = StyledUI.createButton("SHIFT TẤT CẢ FRAMES", Theme.ACCENT);
      btnBatchShift.setFont(Theme.F_TINY);
      btnBatchShift.addActionListener(
         e -> this.applyBatchShift(((Number)this.spinBatchDx.getValue()).intValue(), ((Number)this.spinBatchDy.getValue()).intValue())
      );
      bgc.gridx = 0;
      bgc.gridy = 1;
      bgc.gridwidth = 4;
      batchCard.add(btnBatchShift, bgc);
      JButton btnNewPart = StyledUI.createButton(Lang.get("btn_new_part"), Theme.SUCCESS);
      btnNewPart.setFont(Theme.F_TINY);
      btnNewPart.addActionListener(e -> this.createNewPart());
      bgc.gridy = 2;
      batchCard.add(btnNewPart, bgc);
      JButton btnBatchIds = StyledUI.createButton(Lang.get("btn_batch_update"), Theme.BG_DARKER);
      btnBatchIds.setFont(Theme.F_TINY);
      btnBatchIds.addActionListener(e -> this.batchUpdateIds());
      bgc.gridy = 3;
      batchCard.add(btnBatchIds, bgc);
      c.gridy = 1;
      c.weighty = 0.3;
      rightPanel.add(batchCard, c);
      JPanel sessionCard = StyledUI.createCard(new BorderLayout(5, 5));
      sessionCard.setBorder(new CompoundBorder(StyledUI.createTitledBorder("SESSION EXPORT"), new EmptyBorder(5, 5, 5, 5)));
      JTabbedPane tabbedSlots = new JTabbedPane();
      tabbedSlots.setUI(new BasicTabbedPaneUI());
      tabbedSlots.setFont(Theme.F_TINY);
      tabbedSlots.setBackground(Theme.BG_DARKER);
      tabbedSlots.setForeground(Theme.TEXT_DIM);
      this.txtSlots = new JTextArea[8];

      for (int i = 0; i < 8; i++) {
         this.txtSlots[i] = new JTextArea();
         this.txtSlots[i].setEditable(false);
         this.txtSlots[i].setBackground(Theme.BG_DARKER);
         this.txtSlots[i].setForeground(Theme.ACCENT);
         this.txtSlots[i].setFont(Theme.F_MONO);
         this.txtSlots[i].setLineWrap(true);
         JScrollPane scroll = new JScrollPane(this.txtSlots[i]);
         scroll.setBorder(null);
         tabbedSlots.addTab(this.getSlotName(i).substring(0, Math.min(3, this.getSlotName(i).length())), scroll);
      }

      sessionCard.add(tabbedSlots, "Center");
      JButton btnExport = StyledUI.createButton("DEPLOY TO DATABASE / FILES", Theme.SUCCESS);
      btnExport.setPreferredSize(new Dimension(0, 36));
      btnExport.addActionListener(e -> this.triggerSessionSave());
      sessionCard.add(btnExport, "South");
      c.gridy = 2;
      c.weighty = 0.3;
      rightPanel.add(sessionCard, c);
      this.frameListModel = new DefaultListModel<>();
      this.frameList = new JList<>(this.frameListModel);
      this.frameList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            this.currentFrameIndex = this.frameList.getSelectedIndex();
            this.updateInspector();
         }
      });
      return rightPanel;
   }

   private JPanel buildNudgeRow(Runnable act10L, Runnable act1L, Runnable act1R, Runnable act10R, JSpinner spinner) {
      JPanel row = new JPanel(new GridLayout(1, 5, 2, 0));
      row.setOpaque(false);
      row.add(this.makeNudgeBtn("-10", act10L));
      row.add(this.makeNudgeBtn("-1", act1L));
      row.add(spinner);
      row.add(this.makeNudgeBtn("+1", act1R));
      row.add(this.makeNudgeBtn("+10", act10R));
      return row;
   }

   private JButton makeNudgeBtn(String t, Runnable act) {
      JButton b = StyledUI.createButton(t, Theme.BG_CARD);
      b.setFont(Theme.F_BOLD);
      b.setPreferredSize(new Dimension(30, 22));
      b.addActionListener(e -> {
         act.run();
         this.canvas.requestFocusInWindow();
      });
      return b;
   }

   private int paintSlotToWearSlot(int paintSlot) {
      switch (paintSlot) {
         case 0:
            return 6;
         case 1:
            return 5;
         case 2:
            return 3;
         case 3:
            return 0;
         case 4:
            return 1;
         case 5:
            return 7;
         case 6:
            return 2;
         case 7:
            return 4;
         default:
            return -1;
      }
   }

   private String getSlotName(int i) {
      switch (i) {
         case 0:
            return "Weapon";
         case 1:
            return "Hat";
         case 2:
            return "W-Fashion";
         case 3:
            return "Body";
         case 4:
            return "Cloak";
         case 5:
            return "Leg";
         case 6:
            return "Head";
         case 7:
            return "Hair";
         default:
            return "Slot " + i;
      }
   }

   private String getSlotEmoji(int i) {
      switch (i) {
         case 0:
            return "⚔";
         case 1:
            return "⛑";
         case 2:
            return "✨";
         case 3:
            return "◆";
         case 4:
            return "▼";
         case 5:
            return "▰";
         case 6:
            return "☺";
         case 7:
            return "☼";
         default:
            return "▫";
      }
   }

   private void saveCurrentPresetDialog() {
      String name = JOptionPane.showInputDialog(this, "Enter preset name:");
      if (name != null && !name.trim().isEmpty()) {
         this.presetsMap.put(name.trim(), (short[])this.currentFashion.clone());
         this.savePresets();
         this.loadPresets();
      }
   }

   private void deleteSelectedPreset() {
      String val = this.presetList.getSelectedValue();
      if (val != null) {
         this.presetsMap.remove(val);
         this.savePresets();
         this.loadPresets();
      }
   }

   private void syncPresetHighlight() {
      if (this.currentFashion != null && this.presetsMap != null && this.presetList != null) {
         String matchName = null;

         for (Entry<String, short[]> entry : this.presetsMap.entrySet()) {
            short[] pf = entry.getValue();
            if (pf.length == this.currentFashion.length) {
               boolean same = true;

               for (int i = 0; i < pf.length; i++) {
                  if (pf[i] != this.currentFashion[i]) {
                     same = false;
                     break;
                  }
               }

               if (same) {
                  matchName = entry.getKey();
                  break;
               }
            }
         }

         if (matchName != null) {
            this.isSelectingPreset = true;
            this.presetList.setSelectedValue(matchName, true);
            this.isSelectingPreset = false;
         }
      }
   }

   private void triggerSessionSave() {
      List<mPart> activeParts = new ArrayList<>();
      if (this.currentFashion != null && this.allParts != null) {
         Set<Integer> added = new HashSet<>();

         for (short pId : this.currentFashion) {
            if (pId > -1 && !added.contains(Integer.valueOf(pId))) {
               for (mPart p : this.allParts) {
                  if (p.id == pId) {
                     activeParts.add(p);
                     added.add(Integer.valueOf(pId));
                     break;
                  }
               }
            }
         }
      }

      if (activeParts.isEmpty()) {
         if (this.editingPart == null) {
            JOptionPane.showMessageDialog(this, Lang.get("part_no_display"), Lang.get("part_notification"), 2);
            return;
         }

         activeParts.add(this.editingPart);
      }

      Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
      PartSaveDialog dlg = new PartSaveDialog(owner, activeParts, this.canvas);
      dlg.setVisible(true);
   }

   private void loadFashion() {
      String text = this.txtFashionArray.getText().trim();
      if (text.startsWith("[") && text.endsWith("]")) {
         text = text.substring(1, text.length() - 1);
      }

      String[] parts = text.split(",");
      this.currentFashion = new short[parts.length];

      for (int i = 0; i < parts.length; i++) {
         try {
            this.currentFashion[i] = Short.parseShort(parts[i].trim());
         } catch (NumberFormatException var5) {
            this.currentFashion[i] = -1;
         }
      }

      this.canvas.setWearing(this.currentFashion, this.allParts);
      this.updateDollGrid();
      this.updateFashionData();
      if (this.selectedSlot == -1 && this.currentFashion.length > 0) {
         this.selectedSlot = 0;
      }

      this.updateEditingPart();
   }

   private void updateFashionArrayField() {
      StringBuilder sb = new StringBuilder("[");

      for (int i = 0; i < this.currentFashion.length; i++) {
         sb.append(this.currentFashion[i]);
         if (i < this.currentFashion.length - 1) {
            sb.append(",");
         }
      }

      sb.append("]");
      this.txtFashionArray.setText(sb.toString());
   }

   private void updateFashionData() {
      if (this.currentFashion != null && this.txtSlots != null && this.allParts != null) {
         for (int i = 0; i < 8; i++) {
            if (i >= this.currentFashion.length) {
               this.txtSlots[i].setText("N/A");
            } else {
               short pId = this.currentFashion[i];
               if (pId == -1) {
                  this.txtSlots[i].setText("Empty (-1)");
               } else {
                  mPart target = null;

                  for (mPart p : this.allParts) {
                     if (p.id == pId) {
                        target = p;
                        break;
                     }
                  }

                  if (target == null) {
                     this.txtSlots[i].setText("ID: " + pId + " (Not Loaded)");
                  } else {
                     JSONArray dataArr = new JSONArray();

                     for (int j = 0; j < target.pi.length; j++) {
                        PartImage pi = target.pi[j];
                        JSONArray piArr = new JSONArray();
                        if (pi != null && pi.id != 0) {
                           piArr.put(pi.id);
                           if (pi.dx != 0) {
                              piArr.put(pi.dx);
                           }

                           if (pi.dy != 0) {
                              piArr.put(pi.dy);
                           }
                        } else {
                           piArr.put(0);
                        }

                        dataArr.put(piArr);
                     }

                     this.txtSlots[i].setText(dataArr.toString());
                     this.txtSlots[i].setCaretPosition(0);
                  }
               }
            }
         }
      }
   }

   private void updateEditingPart() {
      if (this.selectedSlot >= 0 && this.currentFashion != null) {
         if (this.selectedSlot < this.currentFashion.length) {
            short partId = this.currentFashion[this.selectedSlot];
            this.editingPart = null;
            if (partId >= 0) {
               for (mPart p : this.allParts) {
                  if (p.id == partId) {
                     this.editingPart = p;
                     break;
                  }
               }
            }

            this.updateFrameList();
         }
      }
   }

   private void updateFrameList() {
      this.frameListModel.clear();
      if (this.editingPart != null && this.editingPart.pi != null) {
         for (int i = 0; i < this.editingPart.pi.length; i++) {
            PartImage pi = this.editingPart.pi[i];
            String idStr = pi != null && pi.id > 0 ? " [Img: " + pi.id + "]" : " [Empty]";
            String frameLabel = this.getFramesForIndex(this.selectedSlot, i);
            this.frameListModel.addElement("Idx " + i + frameLabel + idStr);
         }
      }

      this.updateVisualStrip();
      if (this.frameListModel.getSize() > 0) {
         this.frameList.setSelectedIndex(0);
      } else {
         this.currentFrameIndex = -1;
         this.updateInspector();
      }
   }

   private void updateVisualStrip() {
      this.stripContainer.removeAll();
      if (this.editingPart != null && this.editingPart.pi != null) {
         int canvasActiveIdx = this.getCanvasActiveCiIdx();
         int ciSlot = wearSlotToCiSlot(this.selectedSlot);

         for (int i = 0; i < this.editingPart.pi.length; i++) {
            final int frameIdx = i;
            PartImage pi = this.editingPart.pi[frameIdx];
            final boolean isSelected = frameIdx == this.currentFrameIndex;
            boolean isCanvasActive = frameIdx == canvasActiveIdx;
            final List<Integer> poses = ciSlot >= 0 ? PartCanvas.getPoseFramesUsingPartImage(ciSlot, frameIdx) : Collections.emptyList();
            final JPanel cell = new JPanel(new BorderLayout(0, 2));
            Color borderColor;
            final Color bgColor;
            if (isSelected && isCanvasActive) {
               borderColor = new Color(0, 255, 120);
               bgColor = new Color(0, 60, 30);
            } else if (isCanvasActive) {
               borderColor = new Color(0, 200, 100);
               bgColor = new Color(0, 35, 18);
            } else if (isSelected) {
               borderColor = Theme.ACCENT;
               bgColor = Theme.BG_HOVER;
            } else {
               borderColor = Theme.BORDER;
               bgColor = Theme.BG_CARD;
            }

            cell.setBackground(bgColor);
            cell.setBorder(BorderFactory.createLineBorder(borderColor, !isSelected && !isCanvasActive ? 1 : 2));
            cell.setPreferredSize(new Dimension(64, 86));
            cell.setCursor(Cursor.getPredefinedCursor(12));
            BufferedImage img = pi != null && pi.id > 0 ? this.canvas.getImage(pi.id) : null;
            JLabel imgLabel = new JLabel("", 0);
            if (img != null) {
               imgLabel.setIcon(createCrispIcon(img, 42, 42));
            } else {
               imgLabel.setText("?");
               imgLabel.setFont(new Font("SansSerif", 1, 16));
               imgLabel.setForeground(new Color(60, 60, 70));
            }

            JPanel bottom = new JPanel(new GridLayout(2, 1));
            bottom.setOpaque(false);
            String poseTag = this.formatCompactPoses(poses);
            JLabel idxLabel = new JLabel("Idx " + frameIdx + " " + poseTag, 0);
            idxLabel.setForeground(isSelected ? Theme.ACCENT : (isCanvasActive ? new Color(0, 220, 120) : Theme.TEXT_DIM));
            idxLabel.setFont(new Font("SansSerif", 0, 9));
            String offsetStr = "";
            if (pi != null && pi.id > 0) {
               offsetStr = pi.dx + "," + pi.dy;
            }

            JLabel offsetLabel = new JLabel(offsetStr, 0);
            offsetLabel.setForeground(new Color(120, 120, 140));
            offsetLabel.setFont(new Font("Monospaced", 0, 8));
            if (isCanvasActive) {
               JLabel badge = new JLabel("▶", 0);
               badge.setForeground(new Color(0, 220, 100));
               badge.setFont(new Font("SansSerif", 0, 8));
               cell.add(badge, "North");
            }

            bottom.add(idxLabel);
            bottom.add(offsetLabel);
            cell.add(imgLabel, "Center");
            cell.add(bottom, "South");
            cell.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  if (!isSelected) {
                     cell.setBackground(Theme.BG_DARK);
                  }
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  if (!isSelected) {
                     cell.setBackground(bgColor);
                  }
               }

               @Override
               public void mousePressed(MouseEvent e) {
                  MainUI.this.frameList.setSelectedIndex(frameIdx);
                  if (!poses.isEmpty()) {
                     int targetPose = poses.get(0);
                     MainUI.this.canvas.setSinglePoseFrame(targetPose);
                     if (MainUI.this.spinPoseFrame != null) {
                        MainUI.this.spinPoseFrame.setValue(targetPose);
                     }
                  }

                  MainUI.this.canvas.requestFocusInWindow();
                  if (SwingUtilities.isRightMouseButton(e)) {
                     MainUI.this.showStripMenu(e, frameIdx);
                  } else if (e.getClickCount() == 2) {
                     MainUI.this.browseImage();
                  }
               }
            });
            this.stripContainer.add(cell);
         }

         JPanel menuBtn = this.buildStripMenuBtn();
         this.stripContainer.add(menuBtn);
         this.stripContainer.revalidate();
         this.stripContainer.repaint();
      } else {
         this.stripContainer.revalidate();
         this.stripContainer.repaint();
      }
   }

   private JPanel buildStripMenuBtn() {
      JPanel btn = new JPanel(new BorderLayout());
      btn.setBackground(Theme.BG_DARK);
      btn.setPreferredSize(new Dimension(34, 82));
      btn.setBorder(BorderFactory.createLineBorder(Theme.ACCENT_SOFT, 1));
      JLabel lbl = new JLabel("≡", 0);
      lbl.setFont(new Font("SansSerif", 1, 18));
      lbl.setForeground(Theme.ACCENT);
      btn.add(lbl, "Center");
      btn.setCursor(Cursor.getPredefinedCursor(12));
      btn.setToolTipText("Thêm menu");
      btn.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            MainUI.this.showFullPartMenu(e);
         }
      });
      return btn;
   }

   private int getCanvasActiveCiIdx() {
      return this.selectedSlot >= 0 && this.canvas != null ? this.canvas.getCurrentCiIdx(this.selectedSlot) : -1;
   }

   private void syncStripHighlightToCanvas() {
      this.updateVisualStrip();
      if (this.lblFrameInfo != null) {
         int canvasIdx = this.getCanvasActiveCiIdx();
         this.lblFrameInfo.setText("CI:" + canvasIdx);
      }
   }

   private void jumpToCanvasFrame() {
      int canvasIdx = this.getCanvasActiveCiIdx();
      if (canvasIdx >= 0 && this.editingPart != null && canvasIdx < this.editingPart.pi.length) {
         this.frameList.setSelectedIndex(canvasIdx);
         this.scrollStripToIndex(canvasIdx);
      }
   }

   private void scrollStripToIndex(int idx) {
      SwingUtilities.invokeLater(() -> {
         Component[] cells = this.stripContainer.getComponents();
         if (idx >= 0 && idx < cells.length) {
            cells[idx].requestFocusInWindow();
            Rectangle bounds = cells[idx].getBounds();
            if (this.stripScroll != null) {
               this.stripScroll.getHorizontalScrollBar().setValue(bounds.x);
            }
         }
      });
   }

   private void updateDollGrid() {
      if (this.dollGridPanel != null && this.currentFashion != null) {
         this.dollGridPanel.removeAll();

         for (int i = 0; i < 8; i++) {
            final int slotIdx = i;
            short pId = i < this.currentFashion.length ? this.currentFashion[i] : -2;
            final JPanel card = new JPanel(new BorderLayout(2, 2));
            boolean isSel = this.selectedSlot == slotIdx;
            card.setBackground(isSel ? new Color(30, 50, 80) : Theme.BG_CARD);
            card.setBorder(BorderFactory.createLineBorder(isSel ? Theme.ACCENT : Theme.BORDER, isSel ? 2 : 1));
            card.setPreferredSize(new Dimension(100, 115));
            card.setCursor(new Cursor(12));
            JPanel cardHeader = new JPanel(new BorderLayout(2, 0));
            cardHeader.setOpaque(false);
            cardHeader.setBorder(new EmptyBorder(2, 4, 0, 4));
            JLabel nameLbl = StyledUI.createLabel(this.getSlotEmoji(i) + " " + this.getSlotName(i), Theme.F_TINY, isSel ? Theme.ACCENT : Theme.TEXT_DIM);
            cardHeader.add(nameLbl, "West");
            if (pId > -1) {
               JButton clearBtn = new JButton("×");
               clearBtn.setFocusPainted(false);
               clearBtn.setContentAreaFilled(false);
               clearBtn.setBorderPainted(false);
               clearBtn.setForeground(Theme.ERROR);
               clearBtn.setFont(new Font("Segoe UI", 1, 10));
               clearBtn.setCursor(new Cursor(12));
               clearBtn.setMargin(new Insets(0, 0, 0, 0));
               clearBtn.setPreferredSize(new Dimension(14, 14));
               clearBtn.addActionListener(e -> {
                  this.currentFashion[slotIdx] = -1;
                  this.updateFashionArrayField();
                  this.loadFashion();
               });
               cardHeader.add(clearBtn, "East");
            }

            card.add(cardHeader, "North");
            JLabel iconLbl = new JLabel("", 0);
            if (pId < 0) {
               iconLbl.setText("—");
               iconLbl.setForeground(new Color(50, 50, 60));
            } else {
               mPart p = null;
               if (this.allParts != null) {
                  for (mPart part : this.allParts) {
                     if (part.id == pId) {
                        p = part;
                        break;
                     }
                  }
               }

               if (p != null && p.pi != null && p.pi.length > 0 && p.pi[0] != null && p.pi[0].id > 0) {
                  BufferedImage img = this.canvas.getImage(p.pi[0].id);
                  if (img != null) {
                     iconLbl.setIcon(createCrispIcon(img, 40, 40));
                  } else {
                     iconLbl.setText("?");
                     iconLbl.setForeground(Theme.TEXT_MUTED);
                  }
               } else {
                  iconLbl.setText("?");
                  iconLbl.setForeground(Theme.TEXT_MUTED);
               }
            }

            card.add(iconLbl, "Center");
            JLabel idLbl = new JLabel(pId >= 0 ? "ID: " + pId : "Empty", 0);
            idLbl.setFont(Theme.F_TINY);
            idLbl.setForeground(pId >= 0 ? Theme.ACCENT : new Color(50, 50, 60));
            idLbl.setBorder(new EmptyBorder(0, 0, 2, 0));
            card.add(idLbl, "South");
            card.addMouseListener(new MouseAdapter() {
               @Override
               public void mousePressed(MouseEvent e) {
                  MainUI.this.selectedSlot = slotIdx;
                  MainUI.this.updateEditingPart();
                  MainUI.this.updateDollGrid();
               }

               @Override
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 2) {
                     MainUI.this.showQuickPartSelector();
                  }
               }

               @Override
               public void mouseEntered(MouseEvent e) {
                  if (MainUI.this.selectedSlot != slotIdx) {
                     card.setBorder(BorderFactory.createLineBorder(Theme.BORDER_LIGHT, 1));
                  }
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  if (MainUI.this.selectedSlot != slotIdx) {
                     card.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
                  }
               }
            });
            this.dollGridPanel.add(card);
         }

         this.dollGridPanel.revalidate();
         this.dollGridPanel.repaint();
      }
   }

   private void updateInspector() {
      this.isUpdatingFields = true;
      JLabel slotInfoLbl = this.findSlotInfoLabel();
      if (slotInfoLbl != null) {
         String slotName = this.selectedSlot >= 0 ? this.getSlotName(this.selectedSlot) : "—";
         String partName = this.editingPart != null ? "ID:" + this.editingPart.id : "—";
         slotInfoLbl.setText("Slot: " + slotName + " | Part: " + partName + " | Frame: " + this.currentFrameIndex);
      }

      if (this.editingPart != null && this.currentFrameIndex >= 0 && this.currentFrameIndex < this.editingPart.pi.length) {
         PartImage pi = this.editingPart.pi[this.currentFrameIndex];
         if (pi == null) {
            pi = new PartImage();
            this.editingPart.pi[this.currentFrameIndex] = pi;
         }

         this.txtImgId.setText(pi.id > 0 ? String.valueOf(pi.id) : "");
         this.spinDx.setValue(Integer.valueOf(pi.dx));
         this.spinDy.setValue(Integer.valueOf(pi.dy));
      } else {
         this.txtImgId.setText("");
         this.spinDx.setValue(0);
         this.spinDy.setValue(0);
      }

      this.isUpdatingFields = false;
      this.updateVisualStrip();
      this.canvas.repaint();
   }

   private JLabel findSlotInfoLabel() {
      return this.findNamedLabel(this, "slotInfo");
   }

   private JLabel findNamedLabel(Container cont, String name) {
      for (Component c : cont.getComponents()) {
         if (c instanceof JLabel && name.equals(c.getName())) {
            return (JLabel)c;
         }

         if (c instanceof Container) {
            JLabel found = this.findNamedLabel((Container)c, name);
            if (found != null) {
               return found;
            }
         }
      }

      return null;
   }

   private void applyFields() {
      if (!this.isUpdatingFields && this.editingPart != null && this.currentFrameIndex >= 0 && this.currentFrameIndex < this.editingPart.pi.length) {
         try {
            PartImage pi = this.editingPart.pi[this.currentFrameIndex];
            String idText = this.txtImgId.getText().trim();
            if (!idText.isEmpty()) {
               pi.id = Short.parseShort(idText);
            } else {
               pi.id = 0;
            }

            pi.dx = ((Number)this.spinDx.getValue()).byteValue();
            pi.dy = ((Number)this.spinDy.getValue()).byteValue();
            int tempIdx = this.currentFrameIndex;
            SwingUtilities.invokeLater(() -> {
               if (tempIdx >= 0 && tempIdx < this.frameListModel.getSize()) {
                  this.frameListModel.set(tempIdx, "Idx " + tempIdx + " [Img: " + pi.id + "]");
                  this.updateFashionData();
               }

               this.updateVisualStrip();
            });
            this.canvas.repaint();
         } catch (Exception var4) {
         }
      }
   }

   private void nudgeDx(int amount) {
      if (this.editingPart != null && this.currentFrameIndex >= 0 && this.currentFrameIndex < this.editingPart.pi.length) {
         int val = Math.max(-128, Math.min(127, ((Number)this.spinDx.getValue()).intValue() + amount));
         this.spinDx.setValue(val);
         this.applyFields();
      }
   }

   private void nudgeDy(int amount) {
      if (this.editingPart != null && this.currentFrameIndex >= 0 && this.currentFrameIndex < this.editingPart.pi.length) {
         int val = Math.max(-128, Math.min(127, ((Number)this.spinDy.getValue()).intValue() + amount));
         this.spinDy.setValue(val);
         this.applyFields();
      }
   }

   private void resetOffsets() {
      if (this.editingPart != null && this.currentFrameIndex >= 0 && this.currentFrameIndex < this.editingPart.pi.length) {
         this.spinDx.setValue(0);
         this.spinDy.setValue(0);
         this.applyFields();
      }
   }

   private void applyBatchShift(int dxShift, int dyShift) {
      if (this.editingPart != null && this.editingPart.pi != null) {
         for (PartImage pi : this.editingPart.pi) {
            if (pi != null && pi.id > 0) {
               pi.dx = (byte)Math.max(-128, Math.min(127, pi.dx + dxShift));
               pi.dy = (byte)Math.max(-128, Math.min(127, pi.dy + dyShift));
            }
         }

         this.updateInspector();
         this.updateFashionData();
         this.canvas.repaint();
         JOptionPane.showMessageDialog(this, "Batch shift applied: dx+" + dxShift + " dy+" + dyShift);
      }
   }

   private void duplicateFrame() {
      if (this.editingPart != null && this.currentFrameIndex >= 0 && this.currentFrameIndex < this.editingPart.pi.length) {
         int nextIndex = this.currentFrameIndex + 1;
         if (nextIndex < this.editingPart.pi.length) {
            PartImage curr = this.editingPart.pi[this.currentFrameIndex];
            PartImage next = this.editingPart.pi[nextIndex];
            if (curr != null && next != null) {
               next.id = curr.id;
               next.dx = curr.dx;
               next.dy = curr.dy;
               this.frameList.setSelectedIndex(nextIndex);
            }
         }
      }
   }

   private void clearFrame() {
      if (this.editingPart != null && this.currentFrameIndex >= 0 && this.currentFrameIndex < this.editingPart.pi.length) {
         PartImage pi = this.editingPart.pi[this.currentFrameIndex];
         if (pi != null) {
            pi.id = 0;
            pi.dx = 0;
            pi.dy = 0;
            this.updateInspector();
            this.updateFrameList();
            this.canvas.repaint();
         }
      }
   }

   private void copyFrame() {
      if (this.editingPart != null && this.currentFrameIndex >= 0 && this.currentFrameIndex < this.editingPart.pi.length) {
         PartImage curr = this.editingPart.pi[this.currentFrameIndex];
         if (curr != null) {
            this.copiedFrameData = new PartImage();
            this.copiedFrameData.id = curr.id;
            this.copiedFrameData.dx = curr.dx;
            this.copiedFrameData.dy = curr.dy;
            JOptionPane.showMessageDialog(this, "Copied frame " + this.currentFrameIndex + " (ID:" + curr.id + ")", "Copy", 1);
         }
      }
   }

   private void pasteFrame() {
      if (this.editingPart != null && this.currentFrameIndex >= 0 && this.currentFrameIndex < this.editingPart.pi.length && this.copiedFrameData != null) {
         PartImage curr = this.editingPart.pi[this.currentFrameIndex];
         if (curr != null) {
            curr.id = this.copiedFrameData.id;
            curr.dx = this.copiedFrameData.dx;
            curr.dy = this.copiedFrameData.dy;
            this.updateInspector();
            this.updateFrameList();
            this.canvas.repaint();
         }
      }
   }

   private void browseImage() {
      if (this.editingPart != null && this.currentFrameIndex >= 0) {
         Frame parentFrame = (Frame)SwingUtilities.getWindowAncestor(this);
         FileDialog fd = new FileDialog(parentFrame, "Select Image for Frame " + this.currentFrameIndex, 0);
         fd.setDirectory(lastBrowseDir);
         fd.setFile("*.png");
         fd.setVisible(true);
         if (fd.getFile() != null) {
            lastBrowseDir = fd.getDirectory();
            this.importImageWithAutoFill(new File(fd.getDirectory(), fd.getFile()));
         }
      }
   }

   private void importImageWithAutoFill(File file) {
      if (this.editingPart != null && this.currentFrameIndex >= 0) {
         try {
            String name = file.getName().replace(".png", "");
            int baseVal = Integer.parseInt(name);
            String dir = file.getParent();

            for (int i = this.currentFrameIndex; i < this.editingPart.pi.length; i++) {
               int currentVal = baseVal + (i - this.currentFrameIndex);
               File nextFile = new File(dir, currentVal + ".png");
               if (i > this.currentFrameIndex && !nextFile.exists()) {
                  break;
               }

               int logicalId = resolveLogicalId(currentVal);
               int physicalId = resolvePhysicalId(currentVal);
               this.editingPart.pi[i].id = (short)logicalId;
               if (nextFile.exists()) {
                  String baseDir = AppConfig.getResolvedPath("path_part_img", "Data/Part/img/");
                  ImageZoomHelper.saveImageWithZoomLevels(nextFile, baseDir, physicalId + ".png");
                  this.canvas.invalidateImageCache(logicalId);
                  this.autoAlignPartImage(this.editingPart.pi[i]);
               }
            }

            this.updateFrameList();
            this.updateInspector();
            this.updateFashionData();
            this.canvas.repaint();
         } catch (NumberFormatException var11) {
            this.importSingleFileByName(file);
         } catch (Exception var12) {
            JOptionPane.showMessageDialog(this, "Lỗi import: " + var12.getMessage());
         }
      }
   }

   private void importSingleFileByName(File file) {
      if (this.editingPart != null && this.currentFrameIndex >= 0) {
         String idStr = JOptionPane.showInputDialog(this, "Không nhận ra ID từ tên file '" + file.getName() + "'.\nNhập Logical ID:", "Import Image", 3);
         if (idStr != null) {
            try {
               int logicalId = Integer.parseInt(idStr.trim());
               int physicalId = resolvePhysicalId(logicalId);
               String baseDir = AppConfig.getResolvedPath("path_part_img", "Data/Part/img/");
               ImageZoomHelper.saveImageWithZoomLevels(file, baseDir, physicalId + ".png");
               this.editingPart.pi[this.currentFrameIndex].id = (short)logicalId;
               this.canvas.invalidateImageCache(logicalId);
               this.autoAlignPartImage(this.editingPart.pi[this.currentFrameIndex]);
               this.updateFrameList();
               this.updateInspector();
               this.canvas.repaint();
            } catch (Exception var6) {
               JOptionPane.showMessageDialog(this, "ID không hợp lệ.");
            }
         }
      }
   }

   private void importImageFile(File file) {
      if (this.editingPart != null && this.currentFrameIndex >= 0) {
         try {
            String name = file.getName().replace(".png", "");
            int val = Integer.parseInt(name);
            int logicalId = resolveLogicalId(val);
            int physicalId = resolvePhysicalId(val);
            String baseDir = AppConfig.getResolvedPath("path_part_img", "Data/Part/img/");
            ImageZoomHelper.saveImageWithZoomLevels(file, baseDir, physicalId + ".png");
            this.canvas.invalidateImageCache(logicalId);
            if (this.editingPart.pi[this.currentFrameIndex] == null) {
               this.editingPart.pi[this.currentFrameIndex] = new PartImage();
            }

            this.editingPart.pi[this.currentFrameIndex].id = (short)logicalId;
            this.autoAlignPartImage(this.editingPart.pi[this.currentFrameIndex]);
            this.txtImgId.setText(String.valueOf(logicalId));
         } catch (Exception var7) {
         }
      }
   }

   private void browseMultipleImages() {
      if (this.editingPart != null) {
         Frame parentFrame = (Frame)SwingUtilities.getWindowAncestor(this);
         FileDialog fd = new FileDialog(parentFrame, "Select Multiple Images (Starting from Frame " + this.currentFrameIndex + ")", 0);
         fd.setMultipleMode(true);
         fd.setDirectory(lastBrowseDir);
         fd.setVisible(true);
         File[] files = fd.getFiles();
         if (files != null && files.length != 0) {
            lastBrowseDir = fd.getDirectory();
            Arrays.sort(files, Comparator.comparing(File::getName));
            int startIdx = this.currentFrameIndex >= 0 ? this.currentFrameIndex : 0;

            for (int i = 0; i < files.length && startIdx + i < this.editingPart.pi.length; i++) {
               try {
                  String name = files[i].getName().replace(".png", "");
                  int val = Integer.parseInt(name);
                  int logicalId = resolveLogicalId(val);
                  int physicalId = resolvePhysicalId(val);
                  this.editingPart.pi[startIdx + i].id = (short)logicalId;
                  String baseDir = AppConfig.getResolvedPath("path_part_img", "Data/Part/img/");
                  ImageZoomHelper.saveImageWithZoomLevels(files[i], baseDir, physicalId + ".png");
                  this.canvas.invalidateImageCache(logicalId);
                  this.autoAlignPartImage(this.editingPart.pi[startIdx + i]);
               } catch (Exception var11) {
               }
            }

            this.updateFrameList();
            this.updateInspector();
            this.canvas.repaint();
         }
      }
   }

   private void setupDragAndDrop() {
      this.setDropTarget(new DropTarget(this, new DropTargetAdapter() {
         @Override
         public void drop(DropTargetDropEvent dtde) {
            try {
               dtde.acceptDrop(1);

               List<?> fileList = (List<?>)dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
               for (Object obj : fileList) {
                  if (obj instanceof File) {
                     File file = (File)obj;
                     if (file.getName().toLowerCase().endsWith(".png")) {
                        MainUI.this.importImageFile(file);
                        break;
                     }
                  }
               }
            } catch (Exception var5) {
            }
         }
      }));
   }

   private void showStripMenu(MouseEvent e, int index) {
      JPopupMenu menu = new JPopupMenu();
      JMenuItem itemBrowse = new JMenuItem("Browse Image for this Frame");
      itemBrowse.addActionListener(al -> {
         this.frameList.setSelectedIndex(index);
         this.browseImage();
      });
      JMenuItem itemClear = new JMenuItem("Clear this Frame");
      itemClear.addActionListener(al -> {
         if (this.editingPart != null && index < this.editingPart.pi.length) {
            this.editingPart.pi[index].id = 0;
            this.editingPart.pi[index].dx = 0;
            this.editingPart.pi[index].dy = 0;
            this.updateFrameList();
            this.canvas.repaint();
         }
      });
      JMenuItem itemCopy = new JMenuItem("Copy this Frame");
      itemCopy.addActionListener(al -> {
         this.frameList.setSelectedIndex(index);
         this.copyFrame();
      });
      menu.add(itemBrowse);
      menu.add(itemClear);
      menu.add(itemCopy);
      menu.show(e.getComponent(), e.getX(), e.getY());
   }

   private void showFullPartMenu(MouseEvent e) {
      JPopupMenu menu = new JPopupMenu();
      JMenuItem itemReplaceAll = new JMenuItem(Lang.get("part_menu_batch_replace"));
      itemReplaceAll.addActionListener(al -> this.browseMultipleImages());
      JMenuItem itemClearAll = new JMenuItem(Lang.get("part_menu_clear"));
      itemClearAll.addActionListener(al -> {
         if (this.editingPart != null && JOptionPane.showConfirmDialog(this, Lang.get("part_confirm_clear")) == 0) {
            for (PartImage pi : this.editingPart.pi) {
               pi.id = 0;
               pi.dx = 0;
               pi.dy = 0;
            }

            this.updateFrameList();
            this.canvas.repaint();
         }
      });
      menu.add(itemReplaceAll);
      menu.add(itemClearAll);
      menu.show(e.getComponent(), e.getX(), e.getY());
   }

   private void showQuickPartSelector() {
      if (this.selectedSlot >= 0) {
         String title = "SELECT PART - " + this.getSlotName(this.selectedSlot).toUpperCase();
         int[] types;
         switch (this.selectedSlot) {
            case 0:
            case 2:
               types = new int[]{3};
               break;
            case 1:
               types = new int[]{4};
               break;
            case 3:
               types = new int[]{1};
               break;
            case 4:
               types = new int[]{5};
               break;
            case 5:
               types = new int[]{2};
               break;
            case 6:
               types = new int[]{0};
               break;
            case 7:
               types = new int[]{5};
               break;
            default:
               return;
         }

         PartSelectorDialog dialog = new PartSelectorDialog((Frame)SwingUtilities.getWindowAncestor(this), this.allParts, title, types, this.canvas);
         dialog.setVisible(true);
         int selectedId = dialog.getSelectedId();
         if (selectedId != -2) {
            this.currentFashion[this.selectedSlot] = (short)selectedId;
            this.updateFashionArrayField();
            this.loadFashion();
         }
      }
   }

   private void createNewPart() {
      final JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "PART CONFIGURATION", true);
      dialog.setUndecorated(true);
      dialog.setLayout(new BorderLayout());
      dialog.setSize(1020, 720);
      dialog.setLocationRelativeTo(this);
      JPanel outerPanel = new JPanel(new BorderLayout());
      outerPanel.setBackground(Theme.BG_DARKER);
      outerPanel.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 2));
      dialog.setContentPane(outerPanel);
      JPanel header = new JPanel(new BorderLayout());
      header.setBackground(Theme.BG_DARK);
      header.setBorder(new EmptyBorder(12, 25, 12, 20));
      final Point[] initialClick = new Point[]{null};
      MouseAdapter dragListener = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            initialClick[0] = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            if (initialClick[0] != null) {
               dialog.setLocation(dialog.getLocation().x + e.getX() - initialClick[0].x, dialog.getLocation().y + e.getY() - initialClick[0].y);
            }
         }
      };
      header.addMouseListener(dragListener);
      header.addMouseMotionListener(dragListener);
      JLabel title = StyledUI.createLabel("➕ PART BATCH CONFIGURATION", Theme.F_BOLD, Theme.ACCENT);
      header.add(title, "West");
      JLabel hint = StyledUI.createLabel("Tip: Click index 0 để auto-fill IDs liên tiếp", Theme.F_TINY, Theme.TEXT_MUTED);
      hint.setHorizontalAlignment(0);
      header.add(hint, "Center");
      JButton btnClose = this.buildWindowCloseBtn(() -> dialog.dispose());
      header.add(btnClose, "East");
      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, 1));
      mainPanel.setOpaque(false);
      mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
      String[] labels = new String[]{"Head", "Body", "Leg", "Hat", "Weapon", "Cloak", "Hair"};
      int[] types = new int[]{0, 1, 2, 4, 3, 5, 5};
      mPart[] tempParts = new mPart[7];
      JTextField[] idFields = new JTextField[7];
      JPanel[] stripPanels = new JPanel[7];

      for (int i = 0; i < 7; i++) {
         int rowIdx = i;
         mPart p = new mPart();
         p.type = types[i];
         int len = this.getPartLen(p.type);
         p.pi = new PartImage[len];

         for (int j = 0; j < len; j++) {
            p.pi[j] = new PartImage();
         }

         tempParts[i] = p;
         JPanel row = new JPanel(new BorderLayout(16, 0));
         row.setBackground(Theme.BG_CARD);
         row.setBorder(
            new CompoundBorder(new EmptyBorder(4, 0, 4, 0), new CompoundBorder(BorderFactory.createLineBorder(Theme.BORDER), new EmptyBorder(8, 12, 8, 12)))
         );
         row.setMaximumSize(new Dimension(2000, 120));
         JPanel left = new JPanel(new FlowLayout(0, 6, 8));
         left.setOpaque(false);
         left.setPreferredSize(new Dimension(200, 0));
         JLabel lbl = StyledUI.createLabel(labels[i], Theme.F_BOLD, Theme.TEXT_MAIN);
         lbl.setPreferredSize(new Dimension(70, 25));
         idFields[i] = StyledUI.createTextField("-1");
         idFields[i].setPreferredSize(new Dimension(70, 25));
         idFields[i].setToolTipText("Part ID trong database");
         left.add(lbl);
         left.add(idFields[i]);
         stripPanels[i] = new JPanel(new FlowLayout(0, 8, 0));
         stripPanels[i].setOpaque(false);
         this.updateTempStrip(tempParts[i], stripPanels[i], idFields[i]);
         JScrollPane sp = new JScrollPane(stripPanels[i]);
         sp.setBorder(null);
         sp.setOpaque(false);
         sp.getViewport().setOpaque(false);
         sp.getHorizontalScrollBar().setUnitIncrement(16);
         JButton btnClear = StyledUI.createButton("CLEAR", Theme.BG_DARK);
         btnClear.setPreferredSize(new Dimension(70, 30));
         btnClear.addActionListener(al -> {
            for (PartImage pi : tempParts[rowIdx].pi) {
               pi.id = 0;
               pi.dx = 0;
               pi.dy = 0;
            }

            idFields[rowIdx].setText("-1");
            this.updateTempStrip(tempParts[rowIdx], stripPanels[rowIdx], idFields[rowIdx]);
         });
         row.add(left, "West");
         row.add(sp, "Center");
         row.add(btnClear, "East");
         mainPanel.add(row);
      }

      JButton btnDeploy = StyledUI.createButton("DEPLOY ALL TO SESSION", Theme.SUCCESS);
      btnDeploy.setPreferredSize(new Dimension(0, 50));
      int[] slotMapping = new int[]{6, 3, 5, 1, 0, 4, 7};
      btnDeploy.addActionListener(al -> {
         int count = 0;
         int lastDeployedSlot = -1;
         if (this.currentFashion == null || this.currentFashion.length < 8) {
            this.currentFashion = new short[]{-1, -1, -1, -1, -1, -1, -1, -1};
         }

         for (int i = 0; i < 7; i++) {
            try {
               int id = Integer.parseInt(idFields[i].getText().trim());
               if (id >= 0) {
                  tempParts[i].id = id;
                  this.allParts.removeIf(p -> p.id == id);
                  this.allParts.add(tempParts[i]);
                  int targetSlot = slotMapping[i];
                  this.currentFashion[targetSlot] = (short)id;
                  lastDeployedSlot = targetSlot;
                  count++;
               }
            } catch (Exception var12x) {
            }
         }

         if (count > 0) {
            if (lastDeployedSlot != -1) {
               this.selectedSlot = lastDeployedSlot;
            }

            this.updateFashionArrayField();
            this.loadFashion();
            JOptionPane.showMessageDialog(dialog, count + Lang.get("part_deploy_success"));
            dialog.dispose();
         }
      });
      JPanel footer = new JPanel(new BorderLayout());
      footer.setBackground(Theme.BG_DARK);
      footer.setBorder(new EmptyBorder(12, 25, 12, 25));
      footer.add(btnDeploy, "Center");
      outerPanel.add(header, "North");
      JScrollPane mainScroll = new JScrollPane(mainPanel);
      StyledUI.styleScrollPane(mainScroll);
      outerPanel.add(mainScroll, "Center");
      outerPanel.add(footer, "South");
      dialog.setVisible(true);
   }

   private JButton buildWindowCloseBtn(Runnable onClose) {
      JButton b = new JButton() {
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
            g2.setColor(this.hovered ? Theme.ERROR : Theme.BG_DARK);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 6, 6);
            g2.setColor(this.hovered ? Color.WHITE : Theme.TEXT_DIM);
            g2.setStroke(new BasicStroke(1.2F));
            int cx = this.getWidth() / 2;
            int cy = this.getHeight() / 2;
            int hs = 4;
            g2.drawLine(cx - hs, cy - hs, cx + hs, cy + hs);
            g2.drawLine(cx + hs, cy - hs, cx - hs, cy + hs);
            g2.dispose();
         }
      };
      b.setPreferredSize(new Dimension(28, 28));
      b.setContentAreaFilled(false);
      b.setBorderPainted(false);
      b.setFocusPainted(false);
      b.setCursor(new Cursor(12));
      b.addActionListener(e -> onClose.run());
      return b;
   }

   private void updateTempStrip(final mPart p, final JPanel strip, final JTextField idField) {
      strip.removeAll();

      for (int i = 0; i < p.pi.length; i++) {
         final int partIdx = i;
         JPanel box = new JPanel(new BorderLayout());
         box.setBackground(new Color(38, 38, 48));
         box.setBorder(BorderFactory.createLineBorder(new Color(55, 55, 70)));
         box.setPreferredSize(new Dimension(52, 68));
         box.setCursor(Cursor.getPredefinedCursor(12));
         JLabel iconLabel = new JLabel("", 0);
         if (p.pi[partIdx] != null && p.pi[partIdx].id > 0) {
            BufferedImage img = this.canvas.getImage(p.pi[partIdx].id);
            if (img != null) {
               iconLabel.setIcon(createCrispIcon(img, 36, 36));
            } else {
               iconLabel.setText("?");
               iconLabel.setForeground(Theme.TEXT_MUTED);
            }
         } else {
            iconLabel.setText(partIdx + "");
            iconLabel.setForeground(new Color(60, 60, 80));
         }

         JLabel idLabel = new JLabel(p.pi[partIdx] != null && p.pi[partIdx].id > 0 ? String.valueOf(p.pi[partIdx].id) : "—", 0);
         idLabel.setFont(new Font("Monospaced", 0, 8));
         idLabel.setForeground(p.pi[partIdx] != null && p.pi[partIdx].id > 0 ? Color.WHITE : new Color(60, 60, 80));
         box.add(iconLabel, "Center");
         box.add(idLabel, "South");
         box.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               MainUI.this.browseImagesForPartAtIdx(p, strip, idField, partIdx);
            }
         });
         strip.add(box);
      }

      strip.revalidate();
      strip.repaint();
   }

   private void browseImagesForPartAtIdx(mPart p, JPanel strip, JTextField idField, int startIdx) {
      Frame parentFrame = (Frame)SwingUtilities.getWindowAncestor(this);
      FileDialog fd = new FileDialog(parentFrame, "Select Image for Index " + startIdx, 0);
      fd.setDirectory(lastBrowseDir);
      fd.setVisible(true);
      if (fd.getFile() != null) {
         lastBrowseDir = fd.getDirectory();

         try {
            String name = fd.getFile().replace(".png", "");
            int baseVal = Integer.parseInt(name);

            for (int i = startIdx; i < p.pi.length; i++) {
               int currentVal = baseVal + (i - startIdx);
               File nextFile = new File(lastBrowseDir, currentVal + ".png");
               if (!nextFile.exists()) {
                  break;
               }

               int logicalId = resolveLogicalId(currentVal);
               int physicalId = resolvePhysicalId(currentVal);
               p.pi[i].id = (short)logicalId;
               String baseDir = AppConfig.getResolvedPath("path_part_img", "Data/Part/img/");
               ImageZoomHelper.saveImageWithZoomLevels(nextFile, baseDir, physicalId + ".png");
               this.canvas.invalidateImageCache(logicalId);
               this.autoAlignPartImage(p.pi[i]);
            }

            if (startIdx == 0 && idField.getText().equals("-1") && p.pi[0].id > 0) {
               idField.setText(String.valueOf(p.pi[0].id));
            }

            this.updateTempStrip(p, strip, idField);
         } catch (Exception var15) {
         }
      }
   }

   private void autoAlignPartImage(PartImage pi) {
      if (pi == null || pi.id <= 0) {
         ;
      }
   }

   private int getPartLen(int type) {
      switch (type) {
         case 0:
            return 5;
         case 1:
            return 20;
         case 2:
            return 15;
         case 3:
            return 24;
         case 4:
            return 2;
         case 5:
            return 2;
         default:
            return 0;
      }
   }

   private void batchUpdateIds() {
      if (this.editingPart != null && this.currentFrameIndex >= 0) {
         String input = JOptionPane.showInputDialog(this, Lang.get("part_enter_image_ids"));
         if (input != null && !input.isEmpty()) {
            String[] ids = input.split(",");

            for (int i = 0; i < ids.length && this.currentFrameIndex + i < this.editingPart.pi.length; i++) {
               try {
                  int val = Integer.parseInt(ids[i].trim());
                  int logicalId = resolveLogicalId(val);
                  this.editingPart.pi[this.currentFrameIndex + i].id = (short)logicalId;
               } catch (Exception var6) {
               }
            }

            this.updateFrameList();
            this.updateFashionData();
            this.canvas.repaint();
         }
      }
   }

   public static int wearSlotToCiSlot(int wearSlot) {
      switch (wearSlot) {
         case 0:
            return 3;
         case 1:
            return 4;
         case 2:
            return 3;
         case 3:
            return 2;
         case 4:
            return 6;
         case 5:
            return 1;
         case 6:
            return 0;
         case 7:
            return 5;
         default:
            return -1;
      }
   }

   private String getFramesForIndex(int wearSlot, int index) {
      int ciSlot = wearSlotToCiSlot(wearSlot);
      if (ciSlot < 0) {
         return "";
      } else {
         List<Integer> poses = PartCanvas.getPoseFramesUsingPartImage(ciSlot, index);
         if (poses.isEmpty()) {
            return "";
         } else {
            StringBuilder sb = new StringBuilder(" (F:");

            for (int i = 0; i < poses.size(); i++) {
               if (i > 0) {
                  sb.append(",");
               }

               sb.append(poses.get(i));
            }

            sb.append(")");
            return sb.toString();
         }
      }
   }

   private String formatCompactPoses(List<Integer> poses) {
      if (poses != null && !poses.isEmpty()) {
         if (poses.size() <= 2) {
            StringBuilder sb = new StringBuilder("F:");

            for (int i = 0; i < poses.size(); i++) {
               if (i > 0) {
                  sb.append(",");
               }

               sb.append(poses.get(i));
            }

            return sb.toString();
         } else {
            boolean contiguous = true;

            for (int i = 1; i < poses.size(); i++) {
               if (poses.get(i) != poses.get(i - 1) + 1) {
                  contiguous = false;
                  break;
               }
            }

            return contiguous ? "F:" + poses.get(0) + ".." + poses.get(poses.size() - 1) : "F:" + poses.get(0) + ",+" + (poses.size() - 1);
         }
      } else {
         return "F:—";
      }
   }

   private void updateSliderMax() {
      if (this.canvas != null && this.animSlider != null) {
         this.isUpdatingFromCanvas = true;
         int max = this.canvas.getCurrentSequenceLength();
         this.animSlider.setMaximum(max > 0 ? max - 1 : 0);
         this.animSlider.setValue(0);
         this.isUpdatingFromCanvas = false;
         this.canvas.setAnimTick(0);
      }
   }

   public static ImageIcon createCrispIcon(BufferedImage img, int maxW, int maxH) {
      if (img == null) {
         return null;
      } else {
         int w = img.getWidth();
         int h = img.getHeight();
         double scale = Math.min((double)maxW / w, (double)maxH / h);
         if (scale > 1.0) {
            scale = Math.floor(scale);
         }

         int nw = Math.max(1, (int)(w * scale));
         int nh = Math.max(1, (int)(h * scale));
         BufferedImage scaled = new BufferedImage(nw, nh, 2);
         Graphics2D g2d = scaled.createGraphics();
         g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
         g2d.drawImage(img, 0, 0, nw, nh, null);
         g2d.dispose();
         return new ImageIcon(scaled);
      }
   }

   private void loadPresets() {
      this.presetsMap = new LinkedHashMap<>();
      this.presetListModel.clear();
      this.presetsMap.put("Uta", new short[]{1045, -2, -1, 1043, -1, 1044, 1042, -2});
      File file = new File(AppConfig.getResolvedPath("path_part_presets", "Data/Part/presets.json"));
      if (file.exists()) {
         try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            JSONObject obj = new JSONObject(content);

            for (String key : obj.keySet()) {
               JSONArray arr = obj.getJSONArray(key);
               short[] fashion = new short[arr.length()];

               for (int i = 0; i < arr.length(); i++) {
                  fashion[i] = (short)arr.getInt(i);
               }

               this.presetsMap.put(key, fashion);
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }

      for (String name : this.presetsMap.keySet()) {
         this.presetListModel.addElement(name);
      }
   }

   private void savePresets() {
      File file = new File(AppConfig.getResolvedPath("path_part_presets", "Data/Part/presets.json"));

      try {
         JSONObject obj = new JSONObject();

         for (Entry<String, short[]> entry : this.presetsMap.entrySet()) {
            JSONArray arr = new JSONArray();

            for (short v : entry.getValue()) {
               arr.put(v);
            }

            obj.put(entry.getKey(), arr);
         }

         if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
         }

         Files.write(file.toPath(), obj.toString(2).getBytes(StandardCharsets.UTF_8));
      } catch (Exception var10) {
         var10.printStackTrace();
      }
   }
}
