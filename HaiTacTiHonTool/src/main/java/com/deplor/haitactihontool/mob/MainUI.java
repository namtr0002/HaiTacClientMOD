package com.deplor.haitactihontool.mob;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONArray;

public class MainUI extends JPanel {
   private List<MobTemplate> mobsList;
   private List<MobTemplate> filteredMobs;
   private JList<MobTemplate> mobJList;
   private DefaultListModel<MobTemplate> listModel;
   private MainUI.MobListCellRenderer cellRenderer;
   private JTextField txtSearch;
   private MobCanvas mobCanvas;
   private JTextField txtId;
   private JTextField txtName;
   private JTextField txtLevel;
   private JTextField txtHOne;
   private JTextField txtHp;
   private JComboBox<String> comboIsHuman;
   private JComboBox<String> comboTypeMove;
   private JComboBox<String> comboTypeMonster;
   private JTextField txtIdIcon;
   private JTextField txtSkill;
   private JTextField txtDame;
   private JPanel outfitSlotPanel;
   private JButton[] slotButtons = new JButton[8];
   private JLabel lblIconSpritePreview;
   private JButton btnSelectIconSprite;
   private JComboBox<String> comboAction;
   private JCheckBox chkFlip;
   private JCheckBox chkBoxOverlay;
   private JCheckBox chkAnchorOverlay;
   private JButton btnPlay;
   private JButton btnStepPrev;
   private JButton btnStepNext;
   private JButton btnLoop;
   private JSlider slideScrub;
   private JSpinner spinFps;
   private boolean isLooping = true;
   private JPanel timelineStripPanel;
   private JLabel lblTimelineInfo;
   private MobTemplate currentMob;
   private boolean isUpdatingForm = false;
   private boolean isScrubbing = false;
   private static final String[] SLOT_NAMES = new String[]{"0. Đầu", "1. Tóc", "2. Nón", "3. Vũ khí", "4. VK Trang phục", "5. Áo", "6. Cánh", "7. Quần"};

   public MainUI() {
      this.setLayout(new BorderLayout());
      this.setOpaque(false);
      this.mobsList = MobDataManager.loadMobs();
      this.filteredMobs = new ArrayList<>(this.mobsList);
      JPanel leftPanel = new JPanel(new BorderLayout());
      leftPanel.setPreferredSize(new Dimension(280, 0));
      leftPanel.setBackground(Theme.BG_DARK);
      leftPanel.setBorder(new MatteBorder(0, 0, 0, 1, Theme.BORDER));
      JPanel leftTopHeader = new JPanel(new BorderLayout(0, 5));
      leftTopHeader.setOpaque(false);
      leftTopHeader.setBorder(new EmptyBorder(10, 10, 5, 10));
      this.txtSearch = StyledUI.createTextField("");
      this.txtSearch.putClientProperty("JTextField.placeholderText", "\ud83d\udd0d Tìm ID hoặc Tên Mob...");
      this.txtSearch.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            MainUI.this.filterMobs();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            MainUI.this.filterMobs();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            MainUI.this.filterMobs();
         }
      });
      leftTopHeader.add(this.txtSearch, "North");
      JPanel presetBar = new JPanel(new BorderLayout(5, 0));
      presetBar.setOpaque(false);
      JComboBox<String> comboPresets = new JComboBox<>(
         new String[]{
            "-- Chọn Mob Mẫu (Preset) --",
            "1. Luffy (Mob người)",
            "2. Zoro (Mob người)",
            "3. Thủ Lĩnh Hải Tặc",
            "4. Quái Thường (Sprite)",
            "5. Boss Trùm Khổng Lồ",
            "6. Linh Thú (Pet Dog)",
            "7. Trụ / Pháo Bắn"
         }
      );
      comboPresets.setFont(Theme.F_TINY);
      comboPresets.setBackground(Theme.BG_CARD);
      comboPresets.setForeground(Theme.TEXT_MAIN);
      comboPresets.addActionListener(e -> this.applyMobPreset(comboPresets.getSelectedIndex()));
      presetBar.add(comboPresets, "Center");
      leftTopHeader.add(presetBar, "South");
      JPanel leftBtns = new JPanel(new GridLayout(2, 3, 4, 4));
      leftBtns.setOpaque(false);
      leftBtns.setBorder(new EmptyBorder(4, 10, 6, 10));
      JButton btnAdd = StyledUI.createButton("＋ Thêm", Theme.BG_CARD);
      JButton btnDuplicate = StyledUI.createButton("\ud83d\udccb Copy", Theme.PURPLE);
      JButton btnDelete = StyledUI.createButton("\ud83d\uddd1️ Xóa", Theme.ERROR);
      JButton btnSaveAll = StyledUI.createButton("\ud83d\udcbe Lưu SQL", Theme.SUCCESS);
      JButton btnReload = StyledUI.createButton("\ud83d\udd04 Tải lại", Theme.ACCENT);
      leftBtns.add(btnAdd);
      leftBtns.add(btnDuplicate);
      leftBtns.add(btnDelete);
      leftBtns.add(btnSaveAll);
      leftBtns.add(btnReload);
      JPanel leftNorth = new JPanel(new BorderLayout());
      leftNorth.setOpaque(false);
      leftNorth.add(leftTopHeader, "North");
      leftNorth.add(leftBtns, "South");
      leftPanel.add(leftNorth, "North");
      this.mobCanvas = new MobCanvas();
      this.mobCanvas.setAnimListener((tick, max) -> this.onCanvasAnimTick(tick, max));
      this.listModel = new DefaultListModel<>();

      for (MobTemplate m : this.filteredMobs) {
         this.listModel.addElement(m);
      }

      this.cellRenderer = new MainUI.MobListCellRenderer();
      this.mobJList = new JList<>(this.listModel);
      this.mobJList.setCellRenderer(this.cellRenderer);
      this.mobJList.setFixedCellHeight(52);
      this.mobJList.setBackground(Theme.BG_DARK);
      this.mobJList.setForeground(Theme.TEXT_MAIN);
      this.mobJList.setSelectionBackground(Theme.ACCENT);
      this.mobJList.setSelectionForeground(Color.WHITE);
      this.mobJList.setFont(Theme.F_MAIN);
      JScrollPane scroll = new JScrollPane(this.mobJList);
      StyledUI.styleScrollPane(scroll);
      scroll.setBorder(null);
      leftPanel.add(scroll, "Center");
      JLabel pathInfoLabel = new JLabel("SQL: " + AppConfig.getResolvedPath("path_mob_sql", "Data/Mob/mobs.sql"));
      pathInfoLabel.setFont(Theme.F_TINY);
      pathInfoLabel.setForeground(Theme.TEXT_MUTED);
      pathInfoLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
      leftPanel.add(pathInfoLabel, "South");
      JPanel centerPanel = new JPanel(new BorderLayout());
      centerPanel.setOpaque(false);
      JPanel toolbar = new JPanel(new FlowLayout(0, 6, 6));
      toolbar.setBackground(Theme.BG_DARK);
      toolbar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      this.btnPlay = StyledUI.createButton("⏸ PAUSE", Theme.ACCENT);
      this.btnPlay.setPreferredSize(new Dimension(90, 28));
      this.btnPlay.addActionListener(e -> this.togglePlayPause());
      this.btnStepPrev = StyledUI.createButton("|<", Theme.BG_CARD);
      this.btnStepPrev.setPreferredSize(new Dimension(36, 28));
      this.btnStepPrev.setToolTipText("Khung hình trước");
      this.btnStepPrev.addActionListener(e -> this.stepAnimFrame(-1));
      this.btnStepNext = StyledUI.createButton(">|", Theme.BG_CARD);
      this.btnStepNext.setPreferredSize(new Dimension(36, 28));
      this.btnStepNext.setToolTipText("Khung hình tiếp");
      this.btnStepNext.addActionListener(e -> this.stepAnimFrame(1));
      this.btnLoop = StyledUI.createButton("\ud83d\udd01 Lặp", Theme.SUCCESS);
      this.btnLoop.setPreferredSize(new Dimension(65, 28));
      this.btnLoop.addActionListener(e -> {
         this.isLooping = !this.isLooping;
         this.btnLoop.setBackground(this.isLooping ? Theme.SUCCESS : Theme.BG_CARD);
      });
      this.comboAction = new JComboBox<>(
         new String[]{
            "\ud83c\udfac Tất cả Pose (-1)",
            "\ud83e\uddcd Đứng (Stand 0)",
            "\ud83c\udfc3 Đi (Walk 1)",
            "⚔️ Tấn công (Attack 2)",
            "\ud83d\udca5 Bị đánh (Hurt 3)",
            "\ud83d\udc80 Bị hạ (Die 4)"
         }
      );
      this.comboAction.setBackground(Theme.BG_CARD);
      this.comboAction.setForeground(Theme.TEXT_MAIN);
      this.comboAction.setFont(Theme.F_SMALL);
      this.comboAction.setPreferredSize(new Dimension(145, 28));
      this.comboAction.addActionListener(e -> {
         int sel = this.comboAction.getSelectedIndex();
         this.mobCanvas.setAction(sel == 0 ? -1 : sel - 1);
         this.updateTimelineStrip();
      });
      this.chkFlip = StyledUI.createCheckBox("↔ Quay phải");
      this.chkFlip.addActionListener(e -> this.mobCanvas.setFacingRight(this.chkFlip.isSelected()));
      this.chkBoxOverlay = StyledUI.createCheckBox("\ud83d\udd32 Box");
      this.chkBoxOverlay.setSelected(true);
      this.chkBoxOverlay.addActionListener(e -> this.mobCanvas.setShowBoundingBox(this.chkBoxOverlay.isSelected()));
      this.chkAnchorOverlay = StyledUI.createCheckBox("\ud83c\udfaf Anchor");
      this.chkAnchorOverlay.setSelected(true);
      this.chkAnchorOverlay.addActionListener(e -> this.mobCanvas.setShowAnchor(this.chkAnchorOverlay.isSelected()));
      this.spinFps = new JSpinner(new SpinnerNumberModel(10, 1, 60, 1));
      this.spinFps.setPreferredSize(new Dimension(50, 28));
      this.spinFps.addChangeListener(e -> this.mobCanvas.setFps((Integer)this.spinFps.getValue()));
      JButton btnZoomIn = StyledUI.createButton("+", Theme.BG_CARD);
      btnZoomIn.setPreferredSize(new Dimension(36, 28));
      btnZoomIn.addActionListener(e -> this.mobCanvas.zoomIn());
      JButton btnZoomOut = StyledUI.createButton("-", Theme.BG_CARD);
      btnZoomOut.setPreferredSize(new Dimension(36, 28));
      btnZoomOut.addActionListener(e -> this.mobCanvas.zoomOut());
      JButton btnReset = StyledUI.createButton("\ud83c\udfaf Reset", Theme.BG_DARK);
      btnReset.setPreferredSize(new Dimension(75, 28));
      btnReset.addActionListener(e -> this.mobCanvas.resetView());
      toolbar.add(this.btnPlay);
      toolbar.add(this.btnStepPrev);
      toolbar.add(this.btnStepNext);
      toolbar.add(this.btnLoop);
      toolbar.add(this.comboAction);
      toolbar.add(this.chkFlip);
      toolbar.add(this.chkBoxOverlay);
      toolbar.add(this.chkAnchorOverlay);
      toolbar.add(new JLabel("FPS:"));
      toolbar.add(this.spinFps);
      toolbar.add(new JSeparator(1));
      toolbar.add(btnZoomIn);
      toolbar.add(btnZoomOut);
      toolbar.add(btnReset);
      centerPanel.add(toolbar, "North");
      centerPanel.add(this.mobCanvas, "Center");
      JPanel bottomTimelineContainer = new JPanel(new BorderLayout());
      bottomTimelineContainer.setBackground(Theme.BG_DARK);
      bottomTimelineContainer.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      bottomTimelineContainer.setPreferredSize(new Dimension(0, 115));
      JPanel timelineHeader = new JPanel(new BorderLayout(10, 0));
      timelineHeader.setOpaque(false);
      timelineHeader.setBorder(new EmptyBorder(4, 10, 2, 10));
      this.lblTimelineInfo = StyledUI.createLabel(" \ud83c\udfac Animation Timeline Sequence", Theme.F_TINY, Theme.TEXT_MUTED);
      timelineHeader.add(this.lblTimelineInfo, "West");
      this.slideScrub = new JSlider(0, 10, 0);
      this.slideScrub.setOpaque(false);
      this.slideScrub.setFocusable(false);
      this.slideScrub.addChangeListener(e -> {
         if (this.isScrubbing && !this.slideScrub.getValueIsAdjusting()) {
            this.mobCanvas.setAnimTick(this.slideScrub.getValue());
         }
      });
      this.slideScrub.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            MainUI.this.isScrubbing = true;
            MainUI.this.mobCanvas.setPlaying(false);
            MainUI.this.btnPlay.setText("▶ PLAY");
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            MainUI.this.isScrubbing = false;
            MainUI.this.mobCanvas.setAnimTick(MainUI.this.slideScrub.getValue());
         }
      });
      timelineHeader.add(this.slideScrub, "Center");
      bottomTimelineContainer.add(timelineHeader, "North");
      this.timelineStripPanel = new JPanel(new FlowLayout(0, 6, 4));
      this.timelineStripPanel.setOpaque(false);
      JScrollPane timelineScroll = new JScrollPane(this.timelineStripPanel);
      StyledUI.styleScrollPane(timelineScroll);
      timelineScroll.setBorder(null);
      timelineScroll.getViewport().setOpaque(false);
      timelineScroll.getVerticalScrollBar().setUnitIncrement(18);
      bottomTimelineContainer.add(timelineScroll, "Center");
      centerPanel.add(bottomTimelineContainer, "South");
      JPanel propsPanel = new JPanel(new BorderLayout());
      propsPanel.setBackground(Theme.BG_DARKER);
      propsPanel.setBorder(new MatteBorder(0, 1, 0, 0, Theme.BORDER));
      propsPanel.setPreferredSize(new Dimension(320, 0));
      JPanel formGrid = new JPanel(new GridBagLayout());
      formGrid.setOpaque(false);
      formGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(3, 3, 3, 3);
      gbc.fill = 2;
      this.txtId = this.addProp(formGrid, gbc, 0, "ID:");
      this.txtName = this.addProp(formGrid, gbc, 1, "Tên Mob:");
      this.txtLevel = this.addProp(formGrid, gbc, 2, "Cấp độ (Lv):");
      this.txtHOne = this.addProp(formGrid, gbc, 3, "hOne (Cao 1 Frame):");
      this.txtHp = this.addProp(formGrid, gbc, 4, "Máu (HP):");
      gbc.gridx = 0;
      gbc.gridy = 5;
      formGrid.add(StyledUI.createLabel("Loại Mob:", Theme.F_BOLD, Theme.TEXT_MAIN), gbc);
      gbc.gridx = 1;
      this.comboIsHuman = new JComboBox<>(new String[]{"0 - Quái / Linh thú (Sprite)", "1 - Mob người (Part)"});
      this.comboIsHuman.setBackground(Theme.BG_DARK);
      this.comboIsHuman.setForeground(Theme.TEXT_MAIN);
      this.comboIsHuman.setFont(Theme.F_MAIN);
      this.comboIsHuman.setPreferredSize(new Dimension(190, 26));
      formGrid.add(this.comboIsHuman, gbc);
      gbc.gridx = 0;
      gbc.gridy = 6;
      formGrid.add(StyledUI.createLabel("Move Type:", Theme.F_BOLD, Theme.TEXT_MAIN), gbc);
      gbc.gridx = 1;
      String[] moveTypes = new String[]{
         "0 - Move 12 (5f)",
         "1 - Move 234 (7f)",
         "2 - Move 2345 (8f)",
         "3 - Move 012 (5f)",
         "4 - Move 1234 (7f)",
         "5 - Move 23 (6f)",
         "6 - Move 123 (6f)",
         "8 - BuNhin (2f)",
         "9 - 0Move (5f)",
         "10 - Move 01 (4f)",
         "11 - Move 2343 (7f)",
         "12 - Fly (6f)",
         "14 - Bingo (4f)",
         "15 - Kungfu (9f)",
         "16 - Move 1232 (6f)",
         "17 - Ice Snow (4f)",
         "18 - Pokemon (4f)",
         "19 - Trụ / Tower (2f)",
         "20 - Bánh Kem / Cake (1f)",
         "21 - Pet Dog (5f)"
      };
      this.comboTypeMove = new JComboBox<>(moveTypes);
      this.comboTypeMove.setBackground(Theme.BG_DARK);
      this.comboTypeMove.setForeground(Theme.TEXT_MAIN);
      this.comboTypeMove.setFont(Theme.F_MAIN);
      this.comboTypeMove.setPreferredSize(new Dimension(190, 26));
      formGrid.add(this.comboTypeMove, gbc);
      gbc.gridx = 0;
      gbc.gridy = 7;
      formGrid.add(StyledUI.createLabel("Cấp bậc:", Theme.F_BOLD, Theme.TEXT_MAIN), gbc);
      gbc.gridx = 1;
      this.comboTypeMonster = new JComboBox<>(new String[]{"0 - Quái thường", "1 - Thủ lĩnh / Tinh anh", "2 - Boss / Trùm"});
      this.comboTypeMonster.setBackground(Theme.BG_DARK);
      this.comboTypeMonster.setForeground(Theme.TEXT_MAIN);
      this.comboTypeMonster.setFont(Theme.F_MAIN);
      this.comboTypeMonster.setPreferredSize(new Dimension(190, 26));
      formGrid.add(this.comboTypeMonster, gbc);
      this.txtIdIcon = this.addProp(formGrid, gbc, 8, "IdIcon JSON:");
      this.txtSkill = this.addProp(formGrid, gbc, 9, "Skill JSON:");
      this.txtDame = this.addProp(formGrid, gbc, 10, "Sát thương:");
      this.outfitSlotPanel = new JPanel(new GridLayout(4, 2, 4, 4));
      this.outfitSlotPanel.setOpaque(false);
      this.outfitSlotPanel
         .setBorder(
            BorderFactory.createTitledBorder(
               BorderFactory.createLineBorder(Theme.ACCENT, 1), "\ud83c\udfa8 Bộ Trang Bị / Icon Trực Quan", 0, 0, Theme.F_BOLD, Theme.ACCENT
            )
         );

      for (int i = 0; i < 8; i++) {
         int slotIdx = i;
         JButton btnSlot = StyledUI.createButton(SLOT_NAMES[i] + ": [-1]", Theme.BG_CARD);
         btnSlot.setFont(Theme.F_TINY);
         btnSlot.setPreferredSize(new Dimension(135, 32));
         btnSlot.addActionListener(e -> this.openSlotSelector(slotIdx));
         this.slotButtons[i] = btnSlot;
         this.outfitSlotPanel.add(btnSlot);
      }

      JPanel monsterIconPanel = new JPanel(new BorderLayout(8, 0));
      monsterIconPanel.setOpaque(false);
      monsterIconPanel.setBorder(
         BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.ACCENT, 1), "\ud83d\udc7e Icon Quái Sprite", 0, 0, Theme.F_BOLD, Theme.ACCENT)
      );
      this.lblIconSpritePreview = new JLabel("Icon: -1", 0);
      this.lblIconSpritePreview.setFont(Theme.F_BOLD);
      this.lblIconSpritePreview.setForeground(Theme.TEXT_MAIN);
      this.btnSelectIconSprite = StyledUI.createButton("\ud83c\udfa8 Chọn Icon...", Theme.PURPLE);
      this.btnSelectIconSprite.addActionListener(e -> this.openMonsterIconSelector());
      JButton btnImportSprite = StyledUI.createButton("\ud83d\uddbc️ Import PNG", Theme.ACCENT);
      btnImportSprite.addActionListener(e -> this.importMobSpriteImage());
      JPanel iconBtns = new JPanel(new GridLayout(1, 2, 4, 0));
      iconBtns.setOpaque(false);
      iconBtns.add(this.btnSelectIconSprite);
      iconBtns.add(btnImportSprite);
      monsterIconPanel.add(this.lblIconSpritePreview, "West");
      monsterIconPanel.add(iconBtns, "Center");
      JPanel actionApplyBox = new JPanel(new BorderLayout());
      actionApplyBox.setOpaque(false);
      actionApplyBox.setBorder(new EmptyBorder(8, 0, 0, 0));
      JButton btnApply = StyledUI.createButton("✔️ Áp dụng thay đổi", Theme.SUCCESS);
      btnApply.setPreferredSize(new Dimension(280, 36));
      actionApplyBox.add(btnApply, "Center");
      JPanel rightContainer = new JPanel(new BorderLayout(0, 8));
      rightContainer.setOpaque(false);
      rightContainer.add(formGrid, "North");
      JPanel midSlotBox = new JPanel(new BorderLayout(0, 6));
      midSlotBox.setOpaque(false);
      midSlotBox.add(this.outfitSlotPanel, "North");
      midSlotBox.add(monsterIconPanel, "Center");
      midSlotBox.add(actionApplyBox, "South");
      rightContainer.add(midSlotBox, "Center");
      JScrollPane rightScroll = new JScrollPane(rightContainer);
      StyledUI.styleScrollPane(rightScroll);
      rightScroll.setBorder(null);
      rightScroll.getViewport().setOpaque(false);
      propsPanel.add(rightScroll, "Center");
      centerPanel.add(propsPanel, "East");
      this.add(leftPanel, "West");
      this.add(centerPanel, "Center");
      this.mobJList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int idx = this.mobJList.getSelectedIndex();
            if (idx >= 0 && idx < this.filteredMobs.size()) {
               this.currentMob = this.filteredMobs.get(idx);
               this.updateForm();
               this.mobCanvas.setMob(this.currentMob);
               this.updateOutfitSlotButtons();
               this.updateTimelineStrip();
            }
         }
      });
      DocumentListener previewListener = new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            MainUI.this.triggerPreview();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            MainUI.this.triggerPreview();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            MainUI.this.triggerPreview();
         }
      };
      this.txtIdIcon.getDocument().addDocumentListener(previewListener);
      this.txtHOne.getDocument().addDocumentListener(previewListener);
      this.comboIsHuman.addActionListener(e -> {
         this.toggleSlotPanelsVisibility();
         this.triggerPreview();
         this.updateOutfitSlotButtons();
         this.updateTimelineStrip();
      });
      this.comboTypeMove.addActionListener(e -> {
         this.triggerPreview();
         this.updateTimelineStrip();
      });
      this.comboTypeMonster.addActionListener(e -> this.triggerPreview());
      btnApply.addActionListener(e -> this.saveCurrentMobForm());
      btnSaveAll.addActionListener(e -> {
         MobDataManager.saveMobs(this.mobsList);
         JOptionPane.showMessageDialog(this, "Đã lưu thành công tất cả dữ liệu mobs.sql!", "Thành công", 1);
      });
      btnReload.addActionListener(e -> {
         this.mobsList = MobDataManager.loadMobs();
         this.filterMobs();
         this.mobCanvas.reloadPartsData();
         if (this.cellRenderer != null) {
            this.cellRenderer.clearCache();
         }

         JOptionPane.showMessageDialog(this, "Đã tải lại danh sách Mob từ file SQL!", "Thông báo", 1);
      });
      btnAdd.addActionListener(e -> this.addNewMob());
      btnDuplicate.addActionListener(e -> this.duplicateCurrentMob());
      btnDelete.addActionListener(e -> this.deleteCurrentMob());
      if (!this.filteredMobs.isEmpty()) {
         this.mobJList.setSelectedIndex(0);
      }
   }

   private void togglePlayPause() {
      boolean playing = !this.mobCanvas.isPlaying();
      this.mobCanvas.setPlaying(playing);
      this.btnPlay.setText(playing ? "⏸ PAUSE" : "▶ PLAY");
   }

   private void stepAnimFrame(int delta) {
      this.mobCanvas.setPlaying(false);
      this.btnPlay.setText("▶ PLAY");
      int max = this.mobCanvas.getSequenceLength();
      int cur = this.mobCanvas.getAnimTick();
      int next = (cur + delta + max) % max;
      this.mobCanvas.setAnimTick(next);
      this.slideScrub.setValue(next);
   }

   private void onCanvasAnimTick(int tick, int max) {
      if (!this.isScrubbing) {
         this.slideScrub.setMaximum(Math.max(1, max - 1));
         this.slideScrub.setValue(tick);
         this.updateTimelineHighlight(tick, max);
      }
   }

   private void toggleSlotPanelsVisibility() {
      int isHuman = this.comboIsHuman.getSelectedIndex();
      this.outfitSlotPanel.setVisible(isHuman == 1);
   }

   private void updateOutfitSlotButtons() {
      if (this.currentMob != null) {
         if (this.comboIsHuman.getSelectedIndex() == 1) {
            int head = -1;
            int hair = -1;
            int weapon = -1;
            int hat = -1;
            int wFashion = -1;
            int body = -1;
            int cloak = -1;
            int leg = -1;

            try {
               JSONArray js = new JSONArray(this.txtIdIcon.getText().trim());
               head = js.length() > 1 ? js.getInt(1) : -1;
               hair = js.length() > 2 ? js.getInt(2) : -1;
               if (js.length() > 3 && !js.isNull(3)) {
                  JSONArray js2 = js.getJSONArray(3);
                  if (js2.length() > 0) {
                     weapon = js2.getInt(0);
                  }

                  if (js2.length() > 1) {
                     hat = js2.getInt(1);
                  }

                  if (js2.length() > 2) {
                     wFashion = js2.getInt(2);
                  }

                  if (js2.length() > 3) {
                     body = js2.getInt(3);
                  }

                  if (js2.length() > 4) {
                     cloak = js2.getInt(4);
                  }

                  if (js2.length() > 5) {
                     leg = js2.getInt(5);
                  }
               }
            } catch (Exception var11) {
            }

            int[] ids = new int[]{head, hair, hat, weapon, wFashion, body, cloak, leg};

            for (int i = 0; i < 8; i++) {
               this.slotButtons[i].setText(SLOT_NAMES[i] + ": [" + (ids[i] >= 0 ? ids[i] : "Trống") + "]");
            }
         } else {
            int iconId = this.parseIconIdFromText(this.txtIdIcon.getText().trim());
            this.lblIconSpritePreview.setText("Icon Sprite: [" + iconId + "]");
         }
      }
   }

   private int parseIconIdFromText(String raw) {
      try {
         if (raw.startsWith("[")) {
            String[] parts = raw.replace("[", "").replace("]", "").split(",");
            if (parts.length >= 2) {
               return Integer.parseInt(parts[1].trim());
            }

            if (parts.length >= 1 && !parts[0].trim().isEmpty()) {
               return Integer.parseInt(parts[0].trim());
            }
         } else if (!raw.isEmpty()) {
            return Integer.parseInt(raw.trim());
         }
      } catch (Exception var3) {
      }

      return 0;
   }

   private void openSlotSelector(int slotIdx) {
      Frame parent = (Frame)SwingUtilities.getWindowAncestor(this);

      int selectedPartId = MobPartSelectorDialog.selectPart(parent, this.mobCanvas, "Chọn Part cho " + SLOT_NAMES[slotIdx], switch (slotIdx) {
         case 0, 1 -> new int[]{0, 4, 5};
         case 2 -> new int[]{4};
         case 3, 4 -> new int[]{1};
         case 5 -> new int[]{2};
         case 6 -> new int[]{3};
         case 7 -> new int[]{5};
         default -> new int[]{0};
      });
      if (selectedPartId >= -1) {
         this.updateHumanIdIcon(slotIdx, selectedPartId);
         this.updateOutfitSlotButtons();
         this.triggerPreview();
      }
   }

   private void openMonsterIconSelector() {
      Frame parent = (Frame)SwingUtilities.getWindowAncestor(this);
      int selectedIcon = MobPartSelectorDialog.selectMobIcon(parent, "Chọn Icon Quái / Sprite (Monster)");
      if (selectedIcon >= -1) {
         this.txtIdIcon.setText(String.format("[0,%d]", Math.max(0, selectedIcon)));
         this.updateOutfitSlotButtons();
         this.triggerPreview();
      }
   }

   private void updateHumanIdIcon(int slotIdx, int newPartId) {
      int head = -1;
      int hair = -1;
      int weapon = -1;
      int hat = -1;
      int wFashion = -1;
      int body = -1;
      int cloak = -1;
      int leg = -1;

      try {
         JSONArray js = new JSONArray(this.txtIdIcon.getText().trim());
         head = js.length() > 1 ? js.getInt(1) : -1;
         hair = js.length() > 2 ? js.getInt(2) : -1;
         if (js.length() > 3 && !js.isNull(3)) {
            JSONArray js2 = js.getJSONArray(3);
            if (js2.length() > 0) {
               weapon = js2.getInt(0);
            }

            if (js2.length() > 1) {
               hat = js2.getInt(1);
            }

            if (js2.length() > 2) {
               wFashion = js2.getInt(2);
            }

            if (js2.length() > 3) {
               body = js2.getInt(3);
            }

            if (js2.length() > 4) {
               cloak = js2.getInt(4);
            }

            if (js2.length() > 5) {
               leg = js2.getInt(5);
            }
         }
      } catch (Exception var13) {
      }

      switch (slotIdx) {
         case 0:
            head = newPartId;
            break;
         case 1:
            hair = newPartId;
            break;
         case 2:
            hat = newPartId;
            break;
         case 3:
            weapon = newPartId;
            break;
         case 4:
            wFashion = newPartId;
            break;
         case 5:
            body = newPartId;
            break;
         case 6:
            cloak = newPartId;
            break;
         case 7:
            leg = newPartId;
      }

      String jsonResult = String.format("[1,%d,%d,[%d,%d,%d,%d,%d,%d]]", head, hair, weapon, hat, wFashion, body, cloak, leg);
      this.txtIdIcon.setText(jsonResult);
   }

   private void updateTimelineStrip() {
      this.timelineStripPanel.removeAll();
      if (this.currentMob == null) {
         this.timelineStripPanel.revalidate();
         this.timelineStripPanel.repaint();
      } else {
         final int maxTicks = this.mobCanvas.getSequenceLength();
         this.lblTimelineInfo.setText(String.format(" \ud83c\udfac Timeline Frame Sequence (%d ticks | Action: %d)", maxTicks, this.mobCanvas.getAction()));
         this.slideScrub.setMaximum(Math.max(1, maxTicks - 1));

         for (int i = 0; i < maxTicks; i++) {
            final int tickIndex = i;
            JPanel frameCard = new JPanel(new BorderLayout(0, 2));
            frameCard.setPreferredSize(new Dimension(48, 58));
            frameCard.setBackground(tickIndex == this.mobCanvas.getAnimTick() ? Theme.ACCENT : Theme.BG_CARD);
            frameCard.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
            frameCard.setCursor(Cursor.getPredefinedCursor(12));
            BufferedImage thumbImg = this.mobCanvas.renderMobThumbnail(this.currentMob, this.mobCanvas.getAction(), tickIndex, 44, 40);
            JLabel lblThumb = new JLabel("", 0);
            if (thumbImg != null) {
               lblThumb.setIcon(new ImageIcon(thumbImg));
            } else {
               lblThumb.setText("#" + tickIndex);
               lblThumb.setFont(Theme.F_TINY);
               lblThumb.setForeground(Color.WHITE);
            }

            JLabel lblTickNum = new JLabel(String.valueOf(tickIndex), 0);
            lblTickNum.setFont(Theme.F_TINY);
            lblTickNum.setForeground(Theme.TEXT_MUTED);
            frameCard.add(lblThumb, "Center");
            frameCard.add(lblTickNum, "South");
            frameCard.addMouseListener(new MouseAdapter() {
               @Override
               public void mousePressed(MouseEvent e) {
                  MainUI.this.mobCanvas.setPlaying(false);
                  MainUI.this.btnPlay.setText("▶ PLAY");
                  MainUI.this.mobCanvas.setAnimTick(tickIndex);
                  MainUI.this.slideScrub.setValue(tickIndex);
                  MainUI.this.updateTimelineHighlight(tickIndex, maxTicks);
               }
            });
            this.timelineStripPanel.add(frameCard);
         }

         this.timelineStripPanel.revalidate();
         this.timelineStripPanel.repaint();
      }
   }

   private void updateTimelineHighlight(int currentTick, int max) {
      Component[] comps = this.timelineStripPanel.getComponents();

      for (int i = 0; i < comps.length; i++) {
         if (comps[i] instanceof JPanel card) {
            if (i == currentTick) {
               card.setBackground(Theme.ACCENT);
               card.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            } else {
               card.setBackground(Theme.BG_CARD);
               card.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
            }
         }
      }
   }

   private void applyMobPreset(int presetIdx) {
      if (presetIdx > 0) {
         int newId = this.mobsList.isEmpty() ? 0 : this.mobsList.get(this.mobsList.size() - 1).id + 1;
         MobTemplate p = null;
         switch (presetIdx) {
            case 1:
               p = new MobTemplate(newId, "Luffy Thuyền Trưởng", (short)10, (short)52, 5000, (byte)0, (byte)1, (byte)0, "[1,0,0,[0,10,-1,1,-1,1]]", "[0]", 500);
               break;
            case 2:
               p = new MobTemplate(newId, "Zoro Kiếm Sĩ", (short)15, (short)52, 8000, (byte)0, (byte)1, (byte)0, "[1,1,2,[12,-1,-1,5,-1,5]]", "[0]", 900);
               break;
            case 3:
               p = new MobTemplate(newId, "Thủ Lĩnh Hải Tặc", (short)50, (short)64, 80000, (byte)1, (byte)0, (byte)1, "[0,35]", "[0,1,2]", 2500);
               break;
            case 4:
               p = new MobTemplate(newId, "Quái Hải Tặc Tí Hon", (short)5, (short)40, 1500, (byte)10, (byte)0, (byte)0, "[0,12]", "[0]", 200);
               break;
            case 5:
               p = new MobTemplate(newId, "Boss Trùm Khổng Lồ", (short)100, (short)120, 500000, (byte)4, (byte)0, (byte)2, "[0,105]", "[0,1,2,3]", 12000);
               break;
            case 6:
               p = new MobTemplate(newId, "Chú Chó Linh Thú", (short)1, (short)32, 1000, (byte)21, (byte)0, (byte)0, "[0,200]", "[0]", 50);
               break;
            case 7:
               p = new MobTemplate(newId, "Trụ Bắn Pháo", (short)30, (short)60, 20000, (byte)19, (byte)0, (byte)0, "[0,80]", "[0]", 1500);
         }

         if (p != null) {
            this.mobsList.add(p);
            this.filterMobs();

            for (int i = 0; i < this.listModel.size(); i++) {
               if (this.filteredMobs.get(i).id == p.id) {
                  this.mobJList.setSelectedIndex(i);
                  break;
               }
            }

            JOptionPane.showMessageDialog(this, "Đã tạo Mob mẫu: " + p.name + " (ID: " + p.id + ")!", "Thành công", 1);
         }
      }
   }

   private byte parseMoveTypeIndex(int comboIdx) {
      int[] moveValues = new int[]{0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21};
      return comboIdx >= 0 && comboIdx < moveValues.length ? (byte)moveValues[comboIdx] : 0;
   }

   private int getComboIndexFromMoveType(byte typemove) {
      int[] moveValues = new int[]{0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21};

      for (int i = 0; i < moveValues.length; i++) {
         if (moveValues[i] == typemove) {
            return i;
         }
      }

      return 0;
   }

   private void importMobSpriteImage() {
      JFileChooser fc = new JFileChooser();
      fc.setDialogTitle("Chọn file ảnh Quái Sprite (.png)");
      fc.setFileFilter(new FileNameExtensionFilter("Hình ảnh PNG", "png"));
      int ret = fc.showOpenDialog(this);
      if (ret == 0) {
         File selectedFile = fc.getSelectedFile();

         try {
            BufferedImage img = ImageIO.read(selectedFile);
            if (img == null) {
               JOptionPane.showMessageDialog(this, "Không thể đọc định dạng file ảnh!", "Lỗi", 0);
               return;
            }

            String inputId = JOptionPane.showInputDialog(
               this, "Nhập ID Icon (Ví dụ: 35, 120):", this.currentMob != null ? String.valueOf(this.currentMob.id) : "100"
            );
            if (inputId == null || inputId.trim().isEmpty()) {
               return;
            }

            int iconId = Integer.parseInt(inputId.trim());
            String targetDir = AppConfig.getResolvedPath("path_mob_img", "Data/Mob/img/");
            File dirFile = new File(targetDir);
            if (!dirFile.exists()) {
               dirFile.mkdirs();
            }

            File destFile = new File(targetDir, iconId + 1000 + ".png");
            Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            byte moveType = this.parseMoveTypeIndex(this.comboTypeMove.getSelectedIndex());
            int frames = MobCanvas.getFrameCountByMoveType(moveType);
            int calculatedHOne = Math.max(1, img.getHeight() / Math.max(1, frames));
            this.txtIdIcon.setText(String.format("[0,%d]", iconId));
            this.txtHOne.setText(String.valueOf(calculatedHOne));
            this.comboIsHuman.setSelectedIndex(0);
            this.triggerPreview();
            this.updateOutfitSlotButtons();
            if (this.cellRenderer != null) {
               this.cellRenderer.clearCache();
            }

            JOptionPane.showMessageDialog(this, "Đã import ảnh quái icon " + iconId + " thành công!\nhOne tự động tính: " + calculatedHOne, "Thành công", 1);
         } catch (Exception var13) {
            var13.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi import ảnh: " + var13.getMessage(), "Lỗi", 0);
         }
      }
   }

   private void filterMobs() {
      String q = this.txtSearch.getText().toLowerCase().trim();
      this.filteredMobs.clear();

      for (MobTemplate m : this.mobsList) {
         if (q.isEmpty() || String.valueOf(m.id).contains(q) || m.name.toLowerCase().contains(q)) {
            this.filteredMobs.add(m);
         }
      }

      this.listModel.clear();

      for (MobTemplate mx : this.filteredMobs) {
         this.listModel.addElement(mx);
      }

      if (!this.filteredMobs.isEmpty()) {
         this.mobJList.setSelectedIndex(0);
      }
   }

   private JTextField addProp(JPanel p, GridBagConstraints gbc, int y, String label) {
      gbc.gridx = 0;
      gbc.gridy = y;
      JLabel lbl = StyledUI.createLabel(label, Theme.F_BOLD, Theme.TEXT_MAIN);
      p.add(lbl, gbc);
      gbc.gridx = 1;
      JTextField txt = StyledUI.createTextField("");
      txt.setPreferredSize(new Dimension(190, 26));
      p.add(txt, gbc);
      return txt;
   }

   private void updateForm() {
      if (this.currentMob != null) {
         this.isUpdatingForm = true;
         this.txtId.setText(String.valueOf(this.currentMob.id));
         this.txtName.setText(this.currentMob.name);
         this.txtLevel.setText(String.valueOf(this.currentMob.level));
         this.txtHOne.setText(String.valueOf(this.currentMob.hOne));
         this.txtHp.setText(String.valueOf(this.currentMob.hp));
         this.comboIsHuman.setSelectedIndex(this.currentMob.ishuman == 1 ? 1 : 0);
         this.comboTypeMove.setSelectedIndex(this.getComboIndexFromMoveType(this.currentMob.typemove));
         this.comboTypeMonster.setSelectedIndex(Math.max(0, Math.min(2, this.currentMob.typemonster)));
         this.txtIdIcon.setText(this.currentMob.idicon);
         this.txtSkill.setText(this.currentMob.skill);
         this.txtDame.setText(String.valueOf(this.currentMob.dame));
         this.toggleSlotPanelsVisibility();
         this.isUpdatingForm = false;
      }
   }

   private void saveCurrentMobForm() {
      if (this.currentMob != null) {
         this.currentMob.name = this.txtName.getText().trim();

         try {
            this.currentMob.id = Integer.parseInt(this.txtId.getText().trim());
            this.currentMob.level = Short.parseShort(this.txtLevel.getText().trim());
            this.currentMob.hOne = Short.parseShort(this.txtHOne.getText().trim());
            this.currentMob.hp = Integer.parseInt(this.txtHp.getText().trim());
            this.currentMob.ishuman = (byte)this.comboIsHuman.getSelectedIndex();
            this.currentMob.typemove = this.parseMoveTypeIndex(this.comboTypeMove.getSelectedIndex());
            this.currentMob.typemonster = (byte)Math.max(0, this.comboTypeMonster.getSelectedIndex());
            this.currentMob.idicon = this.txtIdIcon.getText().trim();
            this.currentMob.skill = this.txtSkill.getText().trim();
            this.currentMob.dame = Integer.parseInt(this.txtDame.getText().trim());
            int idx = this.mobJList.getSelectedIndex();
            if (idx >= 0) {
               this.listModel.set(idx, this.currentMob);
               if (this.cellRenderer != null) {
                  this.cellRenderer.clearCache();
               }
            }

            this.mobCanvas.setMob(this.currentMob);
            this.updateOutfitSlotButtons();
            this.updateTimelineStrip();
            JOptionPane.showMessageDialog(this, "Đã lưu thông tin Mob thành công!", "Thành công", 1);
         } catch (Exception var2) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu! Kiểm tra lại các trường số.", "Lỗi", 0);
         }
      }
   }

   private void addNewMob() {
      int newId = this.mobsList.isEmpty() ? 0 : this.mobsList.get(this.mobsList.size() - 1).id + 1;
      MobTemplate m = new MobTemplate(newId, "Mob mới", (short)1, (short)52, 100, (byte)0, (byte)0, (byte)0, "[0,0]", "[0]", 10);
      this.mobsList.add(m);
      this.filterMobs();

      for (int i = 0; i < this.listModel.size(); i++) {
         if (this.filteredMobs.get(i).id == m.id) {
            this.mobJList.setSelectedIndex(i);
            break;
         }
      }
   }

   private void duplicateCurrentMob() {
      if (this.currentMob != null) {
         int newId = this.mobsList.isEmpty() ? 0 : this.mobsList.get(this.mobsList.size() - 1).id + 1;
         MobTemplate copy = this.currentMob.cloneMob(newId);
         this.mobsList.add(copy);
         this.filterMobs();

         for (int i = 0; i < this.listModel.size(); i++) {
            if (this.filteredMobs.get(i).id == copy.id) {
               this.mobJList.setSelectedIndex(i);
               break;
            }
         }

         JOptionPane.showMessageDialog(this, "Đã nhân bản Mob ID " + this.currentMob.id + " -> ID " + newId + "!", "Thành công", 1);
      }
   }

   private void deleteCurrentMob() {
      if (this.currentMob != null) {
         int confirm = JOptionPane.showConfirmDialog(
            this, "Bạn có chắc chắn muốn xóa Mob ID " + this.currentMob.id + " (" + this.currentMob.name + ") không?", "Xác nhận xóa", 0
         );
         if (confirm == 0) {
            this.mobsList.remove(this.currentMob);
            this.filterMobs();
            if (!this.filteredMobs.isEmpty()) {
               this.mobJList.setSelectedIndex(0);
            } else {
               this.currentMob = null;
               this.mobCanvas.setMob(null);
            }
         }
      }
   }

   private void triggerPreview() {
      if (this.currentMob != null && !this.isUpdatingForm) {
         short hOne = this.currentMob.hOne;

         try {
            hOne = Short.parseShort(this.txtHOne.getText().trim());
         } catch (Exception var3) {
         }

         MobTemplate temp = new MobTemplate(
            this.currentMob.id,
            this.txtName.getText().trim(),
            this.currentMob.level,
            hOne,
            this.currentMob.hp,
            this.parseMoveTypeIndex(this.comboTypeMove.getSelectedIndex()),
            (byte)this.comboIsHuman.getSelectedIndex(),
            (byte)Math.max(0, this.comboTypeMonster.getSelectedIndex()),
            this.txtIdIcon.getText().trim(),
            this.txtSkill.getText().trim(),
            this.currentMob.dame
         );
         this.mobCanvas.setMob(temp);
         if (this.cellRenderer != null) {
            this.cellRenderer.clearCache();
         }
      }
   }

   private class MobListCellRenderer extends JPanel implements ListCellRenderer<MobTemplate> {
      private JLabel lblIcon;
      private JLabel lblName;
      private JLabel lblSub;
      private Map<String, ImageIcon> iconCache = new HashMap<>();

      public MobListCellRenderer() {
         this.setLayout(new BorderLayout(8, 0));
         this.setBorder(new EmptyBorder(5, 8, 5, 8));
         this.setOpaque(true);
         this.lblIcon = new JLabel("", 0);
         this.lblIcon.setPreferredSize(new Dimension(44, 44));
         this.lblIcon.setOpaque(true);
         this.lblIcon.setBackground(new Color(22, 22, 28));
         this.lblIcon.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
         JPanel textBox = new JPanel(new GridLayout(2, 1, 0, 1));
         textBox.setOpaque(false);
         this.lblName = new JLabel();
         this.lblName.setFont(Theme.F_BOLD);
         this.lblSub = new JLabel();
         this.lblSub.setFont(Theme.F_TINY);
         textBox.add(this.lblName);
         textBox.add(this.lblSub);
         this.add(this.lblIcon, "West");
         this.add(textBox, "Center");
      }

      public Component getListCellRendererComponent(JList<? extends MobTemplate> list, MobTemplate m, int index, boolean isSelected, boolean cellHasFocus) {
         if (m == null) {
            return this;
         } else {
            if (isSelected) {
               this.setBackground(Theme.ACCENT);
               this.lblName.setForeground(Color.WHITE);
               this.lblSub.setForeground(new Color(220, 225, 255));
               this.lblIcon.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));
            } else {
               this.setBackground(index % 2 == 0 ? Theme.BG_DARK : new Color(26, 26, 32));
               this.lblName.setForeground(Theme.TEXT_MAIN);
               this.lblSub.setForeground(Theme.TEXT_MUTED);
               this.lblIcon.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
            }

            this.lblName.setText(m.id + " - " + m.name);
            String kindStr = m.ishuman == 1 ? "Part Mob" : "Sprite Mob";
            this.lblSub.setText(kindStr + " | Lv." + m.level);
            String key = m.id + "_" + m.ishuman + "_" + m.idicon + "_" + m.hOne + "_" + m.typemove;
            ImageIcon cached = this.iconCache.get(key);
            if (cached == null && MainUI.this.mobCanvas != null) {
               BufferedImage img = MainUI.this.mobCanvas.renderMobThumbnail(m, 0, 0, 40, 40);
               if (img != null) {
                  cached = new ImageIcon(img);
                  this.iconCache.put(key, cached);
               }
            }

            this.lblIcon.setIcon(cached);
            return this;
         }
      }

      public void clearCache() {
         this.iconCache.clear();
      }
   }
}
