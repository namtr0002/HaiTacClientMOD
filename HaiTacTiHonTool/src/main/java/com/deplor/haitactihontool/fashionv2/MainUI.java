package com.deplor.haitactihontool.fashionv2;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.nio.file.Files;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONObject;

public class MainUI extends JPanel {
   private FashionV2Model.FashionV2 currentFashion;
   private FashionV2Canvas canvas;
   private DefaultListModel<String> sequenceListModel;
   private JList<String> sequenceJList;
   private DefaultListModel<String> partListModel;
   private JList<String> partJList;
   private JSpinner spinPosX;
   private JSpinner spinPosY;
   private JSpinner spinScaleX;
   private JSpinner spinScaleY;
   private JSpinner spinRotation;
   private JSpinner spinZOrder;
   private JCheckBox chkFlipX;
   private JCheckBox chkFlipY;
   private JSlider sliderOpacity;
   private DefaultListModel<String> frameListModel;
   private JList<String> frameJList;
   private JButton btnPlayPause;

   public MainUI() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARK);
      this.canvas = new FashionV2Canvas();
      this.add(this.buildTopToolbar(), "North");
      this.add(this.buildLeftPanel(), "West");
      this.add(this.canvas, "Center");
      this.add(this.buildRightInspector(), "East");
      this.add(this.buildBottomTimeline(), "South");
      this.canvas.setOnFrameChangedListener(this::onCanvasFrameChanged);
      this.canvas.setOnTransformSelectedListener(this::onCanvasTransformSelected);
      this.loadPresetKatakuri();
   }

   private JPanel buildTopToolbar() {
      JPanel bar = new JPanel(new FlowLayout(0, 8, 8));
      bar.setBackground(Theme.BG_CARD);
      bar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      JButton btnNew = this.createStyledButton("NEW V2", Theme.ACCENT);
      btnNew.addActionListener(e -> this.createNewDefaultFashion());
      JButton btnOpen = this.createStyledButton("OPEN V2", Theme.BG_HOVER);
      btnOpen.addActionListener(e -> this.openFashionV2File());
      JButton btnSave = this.createStyledButton("SAVE V2", Theme.SUCCESS);
      btnSave.addActionListener(e -> this.saveFashionV2File());
      JButton btnImportV1 = this.createStyledButton("IMPORT LEGACY V1", Theme.PURPLE);
      btnImportV1.addActionListener(e -> this.importLegacyV1());
      JButton btnExportSql = this.createStyledButton("EXPORT LEGACY SQL", Theme.WARNING);
      btnExportSql.addActionListener(e -> this.exportLegacySql());
      JComboBox<String> comboPreset = new JComboBox<>(
         new String[]{"★ PRESET: Katakuri #132 V2 (Merged)", "★ PRESET: Sabo Thần Thoại V2", "★ PRESET: Luffy Gear 5 V2", "★ PRESET: Zoro Tam Kiếm V2"}
      );
      comboPreset.setFont(Theme.F_SMALL);
      comboPreset.setBackground(Theme.BG_DARK);
      comboPreset.setForeground(Theme.TEXT_MAIN);
      comboPreset.addActionListener(e -> {
         int sel = comboPreset.getSelectedIndex();
         if (sel == 0) {
            this.loadPresetKatakuri();
         } else if (sel == 1) {
            this.loadPresetSabo();
         } else if (sel == 2) {
            this.loadPresetLuffy();
         } else if (sel == 3) {
            this.loadPresetZoro();
         }
      });
      JToggleButton tglGrid = new JToggleButton("GRID", true);
      this.styleToggleButton(tglGrid);
      tglGrid.addActionListener(e -> this.canvas.setShowGrid(tglGrid.isSelected()));
      JToggleButton tglPivot = new JToggleButton("PIVOT", true);
      this.styleToggleButton(tglPivot);
      tglPivot.addActionListener(e -> this.canvas.setShowPivot(tglPivot.isSelected()));
      JToggleButton tglOnion = new JToggleButton("ONION SKIN", false);
      this.styleToggleButton(tglOnion);
      tglOnion.addActionListener(e -> this.canvas.setShowOnionSkin(tglOnion.isSelected()));
      JButton btnResetCam = this.createStyledButton("RESET VIEW", Theme.BG_HOVER);
      btnResetCam.addActionListener(e -> this.canvas.resetCamera());
      bar.add(btnNew);
      bar.add(btnOpen);
      bar.add(btnSave);
      bar.add(Box.createHorizontalStrut(5));
      bar.add(btnImportV1);
      bar.add(btnExportSql);
      bar.add(Box.createHorizontalStrut(5));
      bar.add(comboPreset);
      bar.add(Box.createHorizontalStrut(10));
      bar.add(tglGrid);
      bar.add(tglPivot);
      bar.add(tglOnion);
      bar.add(btnResetCam);
      return bar;
   }

   private JPanel buildLeftPanel() {
      JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
      panel.setPreferredSize(new Dimension(240, 0));
      panel.setBackground(Theme.BG_DARK);
      panel.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel seqPanel = new JPanel(new BorderLayout(5, 5));
      seqPanel.setOpaque(false);
      seqPanel.setBorder(
         BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.BORDER), "Sequences (Động Tác)", 0, 0, Theme.F_BOLD, Theme.TEXT_MAIN)
      );
      this.sequenceListModel = new DefaultListModel<>();
      this.sequenceJList = new JList<>(this.sequenceListModel);
      this.styleJList(this.sequenceJList);
      this.sequenceJList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting() && this.sequenceJList.getSelectedIndex() >= 0 && this.currentFashion != null) {
            int idx = this.sequenceJList.getSelectedIndex();
            if (idx < this.currentFashion.sequences.size()) {
               this.canvas.setSequence(this.currentFashion.sequences.get(idx));
               this.updateTimelineList();
            }
         }
      });
      JScrollPane seqScroll = new JScrollPane(this.sequenceJList);
      seqScroll.setBorder(null);
      JPanel seqBtns = new JPanel(new FlowLayout(2, 5, 0));
      seqBtns.setOpaque(false);
      JButton btnAddSeq = this.createStyledButton("+", Theme.ACCENT);
      btnAddSeq.setPreferredSize(new Dimension(35, 25));
      btnAddSeq.addActionListener(e -> this.addNewSequencePrompt());
      JButton btnDelSeq = this.createStyledButton("-", Theme.ERROR);
      btnDelSeq.setPreferredSize(new Dimension(35, 25));
      btnDelSeq.addActionListener(e -> this.deleteSelectedSequence());
      seqBtns.add(btnAddSeq);
      seqBtns.add(btnDelSeq);
      seqPanel.add(seqScroll, "Center");
      seqPanel.add(seqBtns, "South");
      JPanel partPanel = new JPanel(new BorderLayout(5, 5));
      partPanel.setOpaque(false);
      partPanel.setBorder(
         BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.BORDER), "Fashion Parts (Bộ Phận)", 0, 0, Theme.F_BOLD, Theme.TEXT_MAIN)
      );
      this.partListModel = new DefaultListModel<>();
      this.partJList = new JList<>(this.partListModel);
      this.styleJList(this.partJList);
      JScrollPane partScroll = new JScrollPane(this.partJList);
      partScroll.setBorder(null);
      JPanel partBtns = new JPanel(new FlowLayout(2, 5, 0));
      partBtns.setOpaque(false);
      JButton btnAddPart = this.createStyledButton("+", Theme.ACCENT);
      btnAddPart.setPreferredSize(new Dimension(35, 25));
      btnAddPart.addActionListener(e -> this.addNewPartPrompt());
      JButton btnDelPart = this.createStyledButton("-", Theme.ERROR);
      btnDelPart.setPreferredSize(new Dimension(35, 25));
      btnDelPart.addActionListener(e -> this.deleteSelectedPart());
      partBtns.add(btnAddPart);
      partBtns.add(btnDelPart);
      partPanel.add(partScroll, "Center");
      partPanel.add(partBtns, "South");
      panel.add(seqPanel);
      panel.add(partPanel);
      return panel;
   }

   private JPanel buildRightInspector() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setPreferredSize(new Dimension(280, 0));
      panel.setBackground(Theme.BG_CARD);
      panel.setBorder(new MatteBorder(0, 1, 0, 0, Theme.BORDER));
      JLabel title = new JLabel("TRANSFORM INSPECTOR");
      title.setFont(Theme.F_BOLD);
      title.setForeground(Theme.ACCENT);
      title.setBorder(new EmptyBorder(15, 15, 10, 15));
      panel.add(title, "North");
      JPanel form = new JPanel(new GridBagLayout());
      form.setOpaque(false);
      form.setBorder(new EmptyBorder(10, 15, 10, 15));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = 2;
      gbc.insets = new Insets(6, 4, 6, 4);
      this.spinPosX = this.createSpinner();
      this.spinPosY = this.createSpinner();
      this.addFormRow(form, gbc, 0, "Position X:", this.spinPosX, "Position Y:", this.spinPosY);
      this.spinScaleX = this.createSpinner(1.0, 0.1, 10.0, 0.1);
      this.spinScaleY = this.createSpinner(1.0, 0.1, 10.0, 0.1);
      this.addFormRow(form, gbc, 1, "Scale X:", this.spinScaleX, "Scale Y:", this.spinScaleY);
      this.spinRotation = this.createSpinner(0.0, -360.0, 360.0, 5.0);
      this.spinZOrder = this.createIntegerSpinner(0, -50, 50, 1);
      this.addFormRow(form, gbc, 2, "Rotation (°):", this.spinRotation, "Z-Order:", this.spinZOrder);
      this.chkFlipX = new JCheckBox("Flip X");
      this.styleCheckBox(this.chkFlipX);
      this.chkFlipY = new JCheckBox("Flip Y");
      this.styleCheckBox(this.chkFlipY);
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbc.gridwidth = 1;
      form.add(this.chkFlipX, gbc);
      gbc.gridx = 1;
      gbc.gridy = 3;
      form.add(this.chkFlipY, gbc);
      gbc.gridx = 0;
      gbc.gridy = 4;
      gbc.gridwidth = 2;
      JLabel lblOpacity = new JLabel("Opacity:");
      lblOpacity.setForeground(Theme.TEXT_DIM);
      form.add(lblOpacity, gbc);
      this.sliderOpacity = new JSlider(0, 100, 100);
      this.sliderOpacity.setOpaque(false);
      gbc.gridx = 0;
      gbc.gridy = 5;
      gbc.gridwidth = 2;
      form.add(this.sliderOpacity, gbc);
      ChangeListener cl = e -> this.applyInspectorToTransform();
      this.spinPosX.addChangeListener(cl);
      this.spinPosY.addChangeListener(cl);
      this.spinScaleX.addChangeListener(cl);
      this.spinScaleY.addChangeListener(cl);
      this.spinRotation.addChangeListener(cl);
      this.spinZOrder.addChangeListener(cl);
      this.chkFlipX.addActionListener(e -> this.applyInspectorToTransform());
      this.chkFlipY.addActionListener(e -> this.applyInspectorToTransform());
      this.sliderOpacity.addChangeListener(cl);
      panel.add(form, "Center");
      return panel;
   }

   private JPanel buildBottomTimeline() {
      JPanel panel = new JPanel(new BorderLayout(10, 5));
      panel.setPreferredSize(new Dimension(0, 130));
      panel.setBackground(Theme.BG_CARD);
      panel.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      JPanel playBar = new JPanel(new FlowLayout(0, 10, 8));
      playBar.setOpaque(false);
      this.btnPlayPause = this.createStyledButton("▶ PLAY", Theme.ACCENT);
      this.btnPlayPause.addActionListener(e -> {
         if (this.canvas.isPlaying()) {
            this.canvas.pauseAnimation();
            this.btnPlayPause.setText("▶ PLAY");
            this.btnPlayPause.setBackground(Theme.ACCENT);
         } else {
            this.canvas.playAnimation();
            this.btnPlayPause.setText("⏸ PAUSE");
            this.btnPlayPause.setBackground(Theme.WARNING);
         }
      });
      JButton btnAddFrame = this.createStyledButton("+ ADD FRAME", Theme.SUCCESS);
      btnAddFrame.addActionListener(e -> this.addNewFrame());
      JButton btnDelFrame = this.createStyledButton("- DEL FRAME", Theme.ERROR);
      btnDelFrame.addActionListener(e -> this.deleteSelectedFrame());
      playBar.add(this.btnPlayPause);
      playBar.add(btnAddFrame);
      playBar.add(btnDelFrame);
      panel.add(playBar, "West");
      this.frameListModel = new DefaultListModel<>();
      this.frameJList = new JList<>(this.frameListModel);
      this.frameJList.setLayoutOrientation(2);
      this.frameJList.setVisibleRowCount(1);
      this.styleJList(this.frameJList);
      this.frameJList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting() && this.frameJList.getSelectedIndex() >= 0) {
            this.canvas.setFrameIndex(this.frameJList.getSelectedIndex());
         }
      });
      JScrollPane scroll = new JScrollPane(this.frameJList);
      scroll.setBorder(new EmptyBorder(5, 5, 5, 5));
      panel.add(scroll, "Center");
      return panel;
   }

   public void loadPresetKatakuri() {
      File partsSqlFile = new File("Data/Part/parts.sql");
      if (partsSqlFile.exists()) {
         try {
            int fashionId = 132;
            String name = "Thời Trang Katakuri";
            String mwear = "[-1,-2,-1,1160,-1,1161,1159,-2]";
            File iconDir = new File("../HaiTacTiHonServer/data/icon/1");
            FashionV2Merger.MergeResult res = FashionV2Merger.mergeFashionFromTemplate(fashionId, name, mwear, partsSqlFile, iconDir);
            this.currentFashion = res.fashion;
            this.applyFashionToUI();
            File outputDir = new File("Data/FashionV2");
            FashionV2Merger.saveMergedResult(res, outputDir, "132");
            FashionV2Merger.saveMergedResult(res, outputDir, "1");
            return;
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }

      this.currentFashion = new FashionV2Model.FashionV2("fashion_katakuri_132", "Thời Trang Katakuri V2");
      this.currentFashion.parts.add(new FashionV2Model.PartV2("weapon_1160", "sprite_1160", 3, 3));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("cloak_1161", "sprite_1161", 5, 1));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("pet_1159", "sprite_1159", 6, 0));
      FashionV2Model.SequenceV2 seqIdle = new FashionV2Model.SequenceV2("seq_idle", "Idle (Đứng chờ)");
      seqIdle.fps = 12.0F;

      for (int i = 0; i < 4; i++) {
         FashionV2Model.FrameV2 f = new FashionV2Model.FrameV2(i, 83);
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("weapon_1160", 0.0F, 0.0F));
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("cloak_1161", -5.0F, -10 + i % 2));
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("pet_1159", 15.0F, -20.0F));
         seqIdle.frames.add(f);
      }

      this.currentFashion.sequences.add(seqIdle);
      this.applyFashionToUI();
   }

   public void loadPresetSabo() {
      this.currentFashion = new FashionV2Model.FashionV2("fashion_sabo_mythic", "Trang Phục Sabo Thần Thoại V2");
      this.currentFashion.parts.add(new FashionV2Model.PartV2("hair_sabo", "sprite_hair_sabo", 0, 5));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("body_sabo", "sprite_body_sabo", 1, 2));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("weapon_sabo", "sprite_weapon_pipe", 2, 4));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("cloak_sabo", "sprite_cloak_black", 4, 1));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("flame_aura", "sprite_flame_eff", 6, 0));
      FashionV2Model.SequenceV2 seqIdle = new FashionV2Model.SequenceV2("seq_idle", "Idle (Đứng chờ)");
      seqIdle.fps = 8.0F;

      for (int i = 0; i < 4; i++) {
         FashionV2Model.FrameV2 f = new FashionV2Model.FrameV2(i, 125);
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("body_sabo", 0.0F, 0.0F));
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("hair_sabo", 0.0F, -26 + i % 2));
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("weapon_sabo", 16.0F, -12.0F));
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("cloak_sabo", -8.0F, -10 + i % 2));
         FashionV2Model.FramePartTransformV2 flame = new FashionV2Model.FramePartTransformV2("flame_aura", 0.0F, -5.0F);
         flame.scaleX = 1.0F + i * 0.05F;
         flame.scaleY = 1.0F + i * 0.05F;
         flame.opacity = 0.8F;
         f.partTransforms.add(flame);
         seqIdle.frames.add(f);
      }

      FashionV2Model.SequenceV2 seqAttack = new FashionV2Model.SequenceV2("seq_attack", "Flame Slash (Chém Lửa)");
      seqAttack.fps = 14.0F;

      for (int i = 0; i < 6; i++) {
         FashionV2Model.FrameV2 f = new FashionV2Model.FrameV2(i, 71);
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("body_sabo", i * 3, 0.0F));
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("hair_sabo", i * 3, -25.0F));
         FashionV2Model.FramePartTransformV2 w = new FashionV2Model.FramePartTransformV2("weapon_sabo", 22 + i * 4, -14 - i * 3);
         w.rotation = i * 25.0F;
         f.partTransforms.add(w);
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("cloak_sabo", -12 + i * 2, -10.0F));
         FashionV2Model.FramePartTransformV2 flame = new FashionV2Model.FramePartTransformV2("flame_aura", 25 + i * 4, -15.0F);
         flame.scaleX = 1.5F + i * 0.2F;
         flame.rotation = i * 30.0F;
         f.partTransforms.add(flame);
         seqAttack.frames.add(f);
      }

      this.currentFashion.sequences.add(seqIdle);
      this.currentFashion.sequences.add(seqAttack);
      this.applyFashionToUI();
   }

   public void loadPresetLuffy() {
      this.currentFashion = new FashionV2Model.FashionV2("fashion_luffy_gear5", "Vua Hải Tặc Luffy Gear 5 V2");
      this.currentFashion.parts.add(new FashionV2Model.PartV2("hat_straw", "sprite_straw_hat", 3, 5));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("hair_luffy", "sprite_white_cloud_hair", 0, 4));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("body_gear5", "sprite_gear5_body", 1, 2));
      FashionV2Model.SequenceV2 seqLaugh = new FashionV2Model.SequenceV2("seq_laugh", "Joy Boy Laugh (Cười Nika)");
      seqLaugh.fps = 10.0F;

      for (int i = 0; i < 5; i++) {
         FashionV2Model.FrameV2 f = new FashionV2Model.FrameV2(i, 100);
         FashionV2Model.FramePartTransformV2 b = new FashionV2Model.FramePartTransformV2("body_gear5", 0.0F, i % 2 == 0 ? -4.0F : 0.0F);
         b.scaleY = i % 2 == 0 ? 1.1F : 0.95F;
         f.partTransforms.add(b);
         FashionV2Model.FramePartTransformV2 h = new FashionV2Model.FramePartTransformV2("hair_luffy", 0.0F, -28 + (i % 2 == 0 ? -6 : 0));
         h.rotation = i % 2 == 0 ? -10.0F : 10.0F;
         f.partTransforms.add(h);
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("hat_straw", -5.0F, -42.0F));
         seqLaugh.frames.add(f);
      }

      this.currentFashion.sequences.add(seqLaugh);
      this.applyFashionToUI();
   }

   public void loadPresetZoro() {
      this.currentFashion = new FashionV2Model.FashionV2("fashion_zoro_asura", "Kiếm Sĩ Zoro Tam Kiếm V2");
      this.currentFashion.parts.add(new FashionV2Model.PartV2("hair_zoro", "sprite_green_hair", 0, 4));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("body_zoro", "sprite_green_robe", 1, 2));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("weapon_k katana_1", "sprite_sword_enma", 2, 5));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("weapon_k katana_2", "sprite_sword_wado", 2, 1));
      FashionV2Model.SequenceV2 seqSlash = new FashionV2Model.SequenceV2("seq_slash", "Sanzen Sekai (Tam Thiên Thế Giới)");
      seqSlash.fps = 16.0F;

      for (int i = 0; i < 6; i++) {
         FashionV2Model.FrameV2 f = new FashionV2Model.FrameV2(i, 62);
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("body_zoro", i * 4, 0.0F));
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("hair_zoro", i * 4, -25.0F));
         FashionV2Model.FramePartTransformV2 w1 = new FashionV2Model.FramePartTransformV2("weapon_k katana_1", 20 + i * 5, -15.0F);
         w1.rotation = i * 45.0F;
         f.partTransforms.add(w1);
         FashionV2Model.FramePartTransformV2 w2 = new FashionV2Model.FramePartTransformV2("weapon_k katana_2", -15 - i * 3, -10.0F);
         w2.rotation = -i * 35.0F;
         f.partTransforms.add(w2);
         seqSlash.frames.add(f);
      }

      this.currentFashion.sequences.add(seqSlash);
      this.applyFashionToUI();
   }

   private void applyFashionToUI() {
      this.canvas.setFashion(this.currentFashion);
      this.updateSequenceList();
      this.updatePartList();
      this.updateTimelineList();
   }

   private void createNewDefaultFashion() {
      this.currentFashion = new FashionV2Model.FashionV2("fashion_v2_sample", "Thời Trang V2 Mới");
      this.currentFashion.parts.add(new FashionV2Model.PartV2("hair_sample", "sprite_hair_01", 0, 4));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("body_sample", "sprite_body_01", 1, 1));
      this.currentFashion.parts.add(new FashionV2Model.PartV2("weapon_sample", "sprite_weapon_01", 2, 3));
      FashionV2Model.SequenceV2 seqIdle = new FashionV2Model.SequenceV2("seq_idle", "Idle (Đứng)");
      seqIdle.fps = 8.0F;

      for (int i = 0; i < 4; i++) {
         FashionV2Model.FrameV2 f = new FashionV2Model.FrameV2(i, 125);
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("body_sample", 0.0F, 0.0F));
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("hair_sample", 0.0F, -25 + i % 2));
         f.partTransforms.add(new FashionV2Model.FramePartTransformV2("weapon_sample", 15.0F, -10.0F));
         seqIdle.frames.add(f);
      }

      this.currentFashion.sequences.add(seqIdle);
      this.applyFashionToUI();
   }

   private void updateSequenceList() {
      this.sequenceListModel.clear();
      if (this.currentFashion != null) {
         for (FashionV2Model.SequenceV2 s : this.currentFashion.sequences) {
            this.sequenceListModel.addElement(s.name);
         }

         if (!this.sequenceListModel.isEmpty()) {
            this.sequenceJList.setSelectedIndex(0);
         }
      }
   }

   private void updatePartList() {
      this.partListModel.clear();
      if (this.currentFashion != null) {
         for (FashionV2Model.PartV2 p : this.currentFashion.parts) {
            this.partListModel.addElement(p.partId + " (Slot " + p.slotType + ")");
         }
      }
   }

   private void updateTimelineList() {
      this.frameListModel.clear();
      FashionV2Model.SequenceV2 seq = this.canvas.getCurrentSequence();
      if (seq != null) {
         for (int i = 0; i < seq.frames.size(); i++) {
            this.frameListModel.addElement("Frame " + (i + 1));
         }

         if (!this.frameListModel.isEmpty()) {
            this.frameJList.setSelectedIndex(this.canvas.getCurrentFrameIndex());
         }
      }
   }

   private void onCanvasFrameChanged() {
      if (this.frameJList != null && this.canvas.getCurrentFrameIndex() < this.frameListModel.size()) {
         this.frameJList.setSelectedIndex(this.canvas.getCurrentFrameIndex());
      }

      this.onCanvasTransformSelected();
   }

   private void onCanvasTransformSelected() {
      FashionV2Model.FramePartTransformV2 t = this.canvas.getSelectedTransform();
      if (t != null) {
         this.spinPosX.setValue((double)t.posX);
         this.spinPosY.setValue((double)t.posY);
         this.spinScaleX.setValue((double)t.scaleX);
         this.spinScaleY.setValue((double)t.scaleY);
         this.spinRotation.setValue((double)t.rotation);
         this.spinZOrder.setValue(t.zOrder);
         this.chkFlipX.setSelected(t.flipX);
         this.chkFlipY.setSelected(t.flipY);
         this.sliderOpacity.setValue(Math.round(t.opacity * 100.0F));
      }
   }

   private void applyInspectorToTransform() {
      FashionV2Model.FramePartTransformV2 t = this.canvas.getSelectedTransform();
      if (t != null) {
         t.posX = ((Number)this.spinPosX.getValue()).floatValue();
         t.posY = ((Number)this.spinPosY.getValue()).floatValue();
         t.scaleX = ((Number)this.spinScaleX.getValue()).floatValue();
         t.scaleY = ((Number)this.spinScaleY.getValue()).floatValue();
         t.rotation = ((Number)this.spinRotation.getValue()).floatValue();
         t.zOrder = ((Number)this.spinZOrder.getValue()).intValue();
         t.flipX = this.chkFlipX.isSelected();
         t.flipY = this.chkFlipY.isSelected();
         t.opacity = this.sliderOpacity.getValue() / 100.0F;
         this.canvas.repaint();
      }
   }

   private void addNewSequencePrompt() {
      String name = JOptionPane.showInputDialog(this, "Nhập tên Sequence động tác mới:", "Tạo Sequence V2", -1);
      if (name != null && !name.trim().isEmpty() && this.currentFashion != null) {
         FashionV2Model.SequenceV2 seq = new FashionV2Model.SequenceV2("seq_" + System.currentTimeMillis(), name.trim());
         FashionV2Model.FrameV2 f = new FashionV2Model.FrameV2(0, 83);

         for (FashionV2Model.PartV2 p : this.currentFashion.parts) {
            f.partTransforms.add(new FashionV2Model.FramePartTransformV2(p.partId, 0.0F, 0.0F));
         }

         seq.frames.add(f);
         this.currentFashion.sequences.add(seq);
         this.updateSequenceList();
         this.sequenceJList.setSelectedIndex(this.currentFashion.sequences.size() - 1);
      }
   }

   private void deleteSelectedSequence() {
      int idx = this.sequenceJList.getSelectedIndex();
      if (idx >= 0 && this.currentFashion != null && this.currentFashion.sequences.size() > 1) {
         this.currentFashion.sequences.remove(idx);
         this.updateSequenceList();
      }
   }

   private void addNewPartPrompt() {
      String partId = JOptionPane.showInputDialog(this, "Nhập Part ID mới (vd: cloak_sabo_01):", "Thêm Part V2", -1);
      if (partId != null && !partId.trim().isEmpty() && this.currentFashion != null) {
         FashionV2Model.PartV2 part = new FashionV2Model.PartV2(partId.trim(), "sprite_" + partId.trim(), 4, 2);
         this.currentFashion.parts.add(part);
         FashionV2Model.SequenceV2 seq = this.canvas.getCurrentSequence();
         if (seq != null) {
            for (FashionV2Model.FrameV2 f : seq.frames) {
               f.partTransforms.add(new FashionV2Model.FramePartTransformV2(part.partId, 0.0F, 0.0F));
            }
         }

         this.updatePartList();
         this.canvas.repaint();
      }
   }

   private void deleteSelectedPart() {
      int idx = this.partJList.getSelectedIndex();
      if (idx >= 0 && this.currentFashion != null) {
         FashionV2Model.PartV2 p = this.currentFashion.parts.remove(idx);
         if (this.currentFashion != null) {
            for (FashionV2Model.SequenceV2 s : this.currentFashion.sequences) {
               for (FashionV2Model.FrameV2 f : s.frames) {
                  f.partTransforms.removeIf(t -> t.partId.equalsIgnoreCase(p.partId));
               }
            }
         }

         this.updatePartList();
         this.canvas.repaint();
      }
   }

   private void addNewFrame() {
      FashionV2Model.SequenceV2 seq = this.canvas.getCurrentSequence();
      if (seq != null) {
         int newIdx = seq.frames.size();
         FashionV2Model.FrameV2 f = new FashionV2Model.FrameV2(newIdx, 83);
         if (!seq.frames.isEmpty()) {
            FashionV2Model.FrameV2 lastFrame = seq.frames.get(seq.frames.size() - 1);

            for (FashionV2Model.FramePartTransformV2 t : lastFrame.partTransforms) {
               FashionV2Model.FramePartTransformV2 copyT = new FashionV2Model.FramePartTransformV2(t.partId, t.posX, t.posY);
               copyT.scaleX = t.scaleX;
               copyT.scaleY = t.scaleY;
               copyT.rotation = t.rotation;
               copyT.flipX = t.flipX;
               copyT.flipY = t.flipY;
               copyT.zOrder = t.zOrder;
               copyT.opacity = t.opacity;
               f.partTransforms.add(copyT);
            }
         }

         seq.frames.add(f);
         this.updateTimelineList();
         this.canvas.setFrameIndex(newIdx);
      }
   }

   private void deleteSelectedFrame() {
      FashionV2Model.SequenceV2 seq = this.canvas.getCurrentSequence();
      if (seq != null && seq.frames.size() > 1) {
         int idx = this.canvas.getCurrentFrameIndex();
         seq.frames.remove(idx);
         this.updateTimelineList();
         this.canvas.setFrameIndex(Math.min(idx, seq.frames.size() - 1));
      }
   }

   private void openFashionV2File() {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileFilter(new FileNameExtensionFilter("Fashion V2 Files (*.json, *.bin)", "json", "bin"));
      if (chooser.showOpenDialog(this) == 0) {
         try {
            File file = chooser.getSelectedFile();
            if (file.getName().endsWith(".bin")) {
               byte[] data = Files.readAllBytes(file.toPath());
               this.currentFashion = FashionV2Parser.fromBinaryFormat(data);
            } else {
               String content = Files.readString(file.toPath());
               JSONObject json = new JSONObject(content);
               this.currentFashion = FashionV2Parser.fromJsonObject(json);
            }

            this.applyFashionToUI();
            JOptionPane.showMessageDialog(this, "Đã mở thành công Fashion V2: " + this.currentFashion.name, "Thành công", 1);
         } catch (Exception var5) {
            var5.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi mở file: " + var5.getMessage(), "Lỗi", 0);
         }
      }
   }

   private void saveFashionV2File() {
      if (this.currentFashion != null) {
         JFileChooser chooser = new JFileChooser();
         chooser.setSelectedFile(new File(this.currentFashion.fashionId + ".json"));
         chooser.setFileFilter(new FileNameExtensionFilter("JSON Format (*.json)", "json"));
         if (chooser.showSaveDialog(this) == 0) {
            try {
               File file = chooser.getSelectedFile();
               if (!file.getName().endsWith(".json") && !file.getName().endsWith(".bin")) {
                  file = new File(file.getAbsolutePath() + ".json");
               }

               if (file.getName().endsWith(".bin")) {
                  byte[] bin = FashionV2Parser.toBinaryFormat(this.currentFashion);
                  Files.write(file.toPath(), bin);
               } else {
                  JSONObject json = FashionV2Parser.toJsonObject(this.currentFashion);
                  Files.writeString(file.toPath(), json.toString(2));
               }

               JOptionPane.showMessageDialog(this, "Đã lưu thành công!", "Thành công", 1);
            } catch (Exception var4) {
               var4.printStackTrace();
               JOptionPane.showMessageDialog(this, "Lỗi khi lưu file: " + var4.getMessage(), "Lỗi", 0);
            }
         }
      }
   }

   private void importLegacyV1() {
      File defaultPartsSql = new File("Data/Part/parts.sql");
      String inputStr = JOptionPane.showInputDialog(this, "Nhập ID Part V1 cũ (vd: 5, 17, 1001) để parse từ parts.sql:", "Import Legacy V1 Data", -1);
      if (inputStr != null && !inputStr.trim().isEmpty()) {
         try {
            short partId = Short.parseShort(inputStr.trim());
            if (defaultPartsSql.exists()) {
               FashionV2Model.FashionV2 imported = FashionV2Converter.parseLegacyPartFromSql(defaultPartsSql, partId);
               if (imported != null) {
                  this.currentFashion = imported;
                  this.applyFashionToUI();
                  JOptionPane.showMessageDialog(this, "Đã import trực tiếp Part V1 #" + partId + " từ Data/Part/parts.sql!", "Thành công", 1);
                  return;
               }
            }

            this.currentFashion = FashionV2Converter.convertLegacyToV2(partId, (byte)1, null);
            this.applyFashionToUI();
            JOptionPane.showMessageDialog(this, "Đã Convert thành công Part V1 #" + partId + " sang Fashion V2!", "Import Thành công", 1);
         } catch (Exception var5) {
            JOptionPane.showMessageDialog(this, "Lỗi khi import Part V1: " + var5.getMessage(), "Lỗi", 0);
         }
      }
   }

   private void exportLegacySql() {
      if (this.currentFashion != null) {
         try {
            String sql = FashionV2Converter.exportV2ToLegacySql(this.currentFashion, 1001);
            JTextArea textArea = new JTextArea(sql, 15, 50);
            textArea.setFont(Theme.F_MONO);
            JScrollPane scroll = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(this, scroll, "SQL Output Tương Thích Ngược V1", -1);
         } catch (Exception var4) {
            JOptionPane.showMessageDialog(this, "Lỗi export SQL: " + var4.getMessage(), "Lỗi", 0);
         }
      }
   }

   private JButton createStyledButton(String text, Color bg) {
      JButton btn = new JButton(text);
      btn.setFont(Theme.F_BOLD);
      btn.setForeground(Color.WHITE);
      btn.setBackground(bg);
      btn.setFocusPainted(false);
      btn.setBorder(new EmptyBorder(6, 10, 6, 10));
      btn.setCursor(new Cursor(12));
      return btn;
   }

   private void styleToggleButton(JToggleButton tgl) {
      tgl.setFont(Theme.F_SMALL);
      tgl.setForeground(Theme.TEXT_MAIN);
      tgl.setBackground(Theme.BG_CARD);
      tgl.setFocusPainted(false);
      tgl.setBorder(new EmptyBorder(5, 10, 5, 10));
   }

   private void styleJList(JList<String> list) {
      list.setBackground(Theme.BG_CARD);
      list.setForeground(Theme.TEXT_MAIN);
      list.setFont(Theme.F_MAIN);
      list.setSelectionBackground(Theme.ACCENT);
      list.setSelectionForeground(Color.WHITE);
   }

   private void styleCheckBox(JCheckBox chk) {
      chk.setOpaque(false);
      chk.setForeground(Theme.TEXT_MAIN);
      chk.setFont(Theme.F_SMALL);
   }

   private JSpinner createSpinner() {
      return this.createSpinner(0.0, -1000.0, 1000.0, 1.0);
   }

   private JSpinner createSpinner(double val, double min, double max, double step) {
      SpinnerNumberModel model = new SpinnerNumberModel(val, min, max, step);
      JSpinner spinner = new JSpinner(model);
      spinner.setPreferredSize(new Dimension(80, 25));
      return spinner;
   }

   private JSpinner createIntegerSpinner(int val, int min, int max, int step) {
      SpinnerNumberModel model = new SpinnerNumberModel(val, min, max, step);
      JSpinner spinner = new JSpinner(model);
      spinner.setPreferredSize(new Dimension(80, 25));
      return spinner;
   }

   private void addFormRow(JPanel form, GridBagConstraints gbc, int row, String lbl1Text, Component comp1, String lbl2Text, Component comp2) {
      gbc.gridy = row * 2;
      gbc.gridx = 0;
      gbc.gridwidth = 1;
      JLabel lbl1 = new JLabel(lbl1Text);
      lbl1.setForeground(Theme.TEXT_DIM);
      form.add(lbl1, gbc);
      gbc.gridx = 1;
      JLabel lbl2 = new JLabel(lbl2Text);
      lbl2.setForeground(Theme.TEXT_DIM);
      form.add(lbl2, gbc);
      gbc.gridy = row * 2 + 1;
      gbc.gridx = 0;
      form.add(comp1, gbc);
      gbc.gridx = 1;
      form.add(comp2, gbc);
   }
}
