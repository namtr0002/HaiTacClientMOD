package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AutoMapGeneratorDialog extends JDialog {
   private GameMap generatedMap;
   private GameMapCanvas previewCanvas;
   private Consumer<GameMap> applyCallback;
   private JTextField txtId;
   private JTextField txtName;
   private JTextField txtWidth;
   private JTextField txtHeight;
   private JTextField txtSeed;
   private JComboBox<AutoMapEngine.BiomeArchetype> cmbBiome;
   private JComboBox<AutoMapEngine.TerrainStyle> cmbTerrainStyle;
   private JTextField txtTileSet;
   private JTextField txtIdBack;
   private JTextField txtHBack;
   private JSlider sldGroundHeight;
   private JSlider sldHillRoughness;
   private JSlider sldPlatformDensity;
   private JCheckBox chkPlatforms;
   private JCheckBox chkItems;
   private JCheckBox chkMobs;
   private JCheckBox chkVgos;
   private JSlider sldItemDensity;
   private JTextField txtMobCount;
   private JTextField txtMobLevel;
   private JTextField txtLeftVgo;
   private JTextField txtRightVgo;
   private JTextField txtBatchCount;
   private JTextField txtBatchStartId;
   private JTextField txtBatchPrefix;
   private JComboBox<AutoMapEngine.BiomeArchetype> cmbBatchBiome;
   private boolean isUpdating = false;

   public AutoMapGeneratorDialog(Frame owner, GameMap currentMap) {
      super(owner, "\ud83c\udfb2 HTTH AUTO MAP GENERATOR & PROCEDURAL STUDIO v6.0", true);
      this.setSize(1220, 800);
      this.setMinimumSize(new Dimension(980, 680));
      this.setLocationRelativeTo(owner);
      this.initUI(currentMap);
      this.triggerGeneration();
   }

   public void setApplyCallback(Consumer<GameMap> callback) {
      this.applyCallback = callback;
   }

   private void initUI(GameMap currentMap) {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBackground(Theme.BG_DARKER);
      mainPanel.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 2));
      JPanel headerBar = new JPanel(new BorderLayout());
      headerBar.setBackground(Theme.BG_DARK);
      headerBar.setPreferredSize(new Dimension(0, 42));
      headerBar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      JLabel titleLabel = StyledUI.createLabel("  \ud83c\udfb2 AUTO MAP GENERATOR STUDIO  —  HTTH Procedural Map Engine v6.0", Theme.F_TINY, Color.WHITE);
      titleLabel.setFont(new Font("Segoe UI", 1, 13));
      headerBar.add(titleLabel, "West");
      JButton btnClose = new JButton("✕") {
         {
            this.setFont(new Font("Segoe UI", 1, 14));
            this.setForeground(Theme.TEXT_DIM);
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setCursor(new Cursor(12));
         }
      };
      btnClose.addActionListener(e -> this.dispose());
      headerBar.add(btnClose, "East");
      mainPanel.add(headerBar, "North");
      this.previewCanvas = new GameMapCanvas();
      this.previewCanvas.setShowGrid(true);
      JScrollPane previewScroll = new JScrollPane(this.previewCanvas);
      StyledUI.styleScrollPane(previewScroll);
      previewScroll.getVerticalScrollBar().setUnitIncrement(32);
      previewScroll.getHorizontalScrollBar().setUnitIncrement(32);
      JPanel previewContainer = new JPanel(new BorderLayout());
      previewContainer.setOpaque(false);
      JPanel previewToolbar = new JPanel(new FlowLayout(0, 8, 4));
      previewToolbar.setBackground(Theme.BG_DARK);
      previewToolbar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      JLabel lblPrev = StyledUI.createLabel("Live Preview:", Theme.F_TINY, Theme.TEXT_MUTED);
      previewToolbar.add(lblPrev);
      String[] zoomLevels = new String[]{"100%", "200%", "300%", "400%", "50%"};
      JComboBox<String> cmbZoom = new JComboBox<>(zoomLevels);
      cmbZoom.setFont(Theme.F_TINY);
      cmbZoom.addActionListener(e -> {
         String z = (String)cmbZoom.getSelectedItem();
         if (z != null) {
            int val = Integer.parseInt(z.replace("%", ""));
            this.previewCanvas.setZoom(val / 100);
         }
      });
      previewToolbar.add(cmbZoom);
      JCheckBox chkGrid = StyledUI.createCheckBox("Grid");
      chkGrid.setSelected(true);
      chkGrid.addActionListener(e -> {
         this.previewCanvas.setShowGrid(chkGrid.isSelected());
         this.previewCanvas.repaint();
      });
      previewToolbar.add(chkGrid);
      JCheckBox chkCol = StyledUI.createCheckBox("Collision");
      chkCol.setSelected(false);
      chkCol.addActionListener(e -> {
         this.previewCanvas.setShowCollision(chkCol.isSelected());
         this.previewCanvas.repaint();
      });
      previewToolbar.add(chkCol);
      previewContainer.add(previewToolbar, "North");
      previewContainer.add(previewScroll, "Center");
      JPanel leftSettingsPanel = this.buildSettingsPanel(currentMap);
      JScrollPane settingsScroll = new JScrollPane(leftSettingsPanel);
      StyledUI.styleScrollPane(settingsScroll);
      settingsScroll.setPreferredSize(new Dimension(460, 0));
      settingsScroll.getVerticalScrollBar().setUnitIncrement(24);
      JSplitPane splitPane = new JSplitPane(1, settingsScroll, previewContainer);
      splitPane.setDividerLocation(460);
      splitPane.setDividerSize(4);
      splitPane.setBackground(Theme.BG_DARKER);
      splitPane.setBorder(null);
      mainPanel.add(splitPane, "Center");
      JPanel footerBar = new JPanel(new FlowLayout(2, 10, 8));
      footerBar.setBackground(Theme.BG_DARK);
      footerBar.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      JButton btnDice = StyledUI.createButton("\ud83c\udfb2 RANDOM SEED", new Color(138, 43, 226));
      btnDice.addActionListener(e -> {
         this.txtSeed.setText(String.valueOf(System.currentTimeMillis()));
         this.triggerGeneration();
      });
      JButton btnRegen = StyledUI.createButton("\ud83d\udd04 RE-GENERATE", new Color(255, 140, 0));
      btnRegen.addActionListener(e -> this.triggerGeneration());
      JButton btnSaveProject = StyledUI.createButton("\ud83d\udcbe SAVE TO PROJECT", new Color(0, 150, 255));
      btnSaveProject.addActionListener(e -> this.saveDirectlyToProject());
      JButton btnApply = StyledUI.createButton("✅ APPLY TO EDITOR", Theme.SUCCESS);
      btnApply.addActionListener(e -> {
         if (this.generatedMap != null && this.applyCallback != null) {
            this.applyCallback.accept(this.generatedMap);
            this.dispose();
         }
      });
      footerBar.add(btnDice);
      footerBar.add(btnRegen);
      footerBar.add(btnSaveProject);
      footerBar.add(btnApply);
      mainPanel.add(footerBar, "South");
      this.setContentPane(mainPanel);
   }

   private JPanel buildSettingsPanel(GameMap currentMap) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(Theme.BG_DARK);
      JTabbedPane tabs = new JTabbedPane();
      tabs.setFont(Theme.F_TINY);
      int nextId = currentMap != null ? currentMap.id + 1 : 999;
      int defaultW = currentMap != null ? Math.max(30, currentMap.width) : 50;
      int defaultH = currentMap != null ? Math.max(17, currentMap.height) : 17;
      int defaultTs = currentMap != null ? currentMap.getTileSetId() : 0;
      JPanel terrainTab = new JPanel(new GridBagLayout());
      terrainTab.setOpaque(false);
      terrainTab.setBorder(new EmptyBorder(10, 10, 10, 10));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(3, 3, 3, 3);
      gbc.fill = 2;
      gbc.weightx = 1.0;
      gbc.gridx = 0;
      int row = 0;
      JPanel idNameRow = new JPanel(new GridLayout(1, 2, 8, 0));
      idNameRow.setOpaque(false);
      this.txtId = this.createField(String.valueOf(nextId));
      this.txtName = this.createField("Đảo Tân Thế Giới");
      idNameRow.add(this.wrapFieldWithLabel("Map ID:", this.txtId));
      idNameRow.add(this.wrapFieldWithLabel("Tên Bản Đồ:", this.txtName));
      gbc.gridy = row++;
      terrainTab.add(idNameRow, gbc);
      JPanel dimRow = new JPanel(new GridLayout(1, 2, 8, 0));
      dimRow.setOpaque(false);
      this.txtWidth = this.createField(String.valueOf(defaultW));
      this.txtHeight = this.createField(String.valueOf(defaultH));
      dimRow.add(this.wrapFieldWithLabel("Chiều Rộng (W):", this.txtWidth));
      dimRow.add(this.wrapFieldWithLabel("Chiều Cao (H):", this.txtHeight));
      gbc.gridy = row++;
      terrainTab.add(dimRow, gbc);
      this.cmbBiome = new JComboBox<>(AutoMapEngine.BiomeArchetype.values());
      this.cmbBiome.setFont(Theme.F_TINY);
      this.cmbBiome.setPreferredSize(new Dimension(0, 28));
      boolean foundMatch = false;

      for (AutoMapEngine.BiomeArchetype a : AutoMapEngine.BiomeArchetype.values()) {
         if (a.defaultTileSet == defaultTs) {
            this.cmbBiome.setSelectedItem(a);
            foundMatch = true;
            break;
         }
      }

      if (!foundMatch && this.cmbBiome.getItemCount() > 0) {
         this.cmbBiome.setSelectedIndex(0);
      }

      AutoMapEngine.BiomeArchetype initBiome = (AutoMapEngine.BiomeArchetype)this.cmbBiome.getSelectedItem();
      if (initBiome == null) {
         initBiome = AutoMapEngine.BiomeArchetype.ISLAND_BEACH;
      }

      AutoMapEngine.BiomeProfile initProf = AutoMapEngine.getProfile(initBiome);
      this.cmbBiome.addActionListener(e -> {
         if (!this.isUpdating) {
            AutoMapEngine.BiomeArchetype selected = (AutoMapEngine.BiomeArchetype)this.cmbBiome.getSelectedItem();
            if (selected != null) {
               AutoMapEngine.BiomeProfile prof = AutoMapEngine.getProfile(selected);
               this.isUpdating = true;
               this.txtTileSet.setText(String.valueOf(prof.tileSetId));
               this.txtIdBack.setText(String.valueOf(prof.idBack));
               this.txtHBack.setText(String.valueOf(prof.hBack));
               this.isUpdating = false;
               this.triggerGeneration();
            }
         }
      });
      gbc.gridy = row++;
      terrainTab.add(this.wrapComponentWithLabel("Hệ Sinh Thái (Biome):", this.cmbBiome), gbc);
      this.cmbTerrainStyle = new JComboBox<>(AutoMapEngine.TerrainStyle.values());
      this.cmbTerrainStyle.setFont(Theme.F_TINY);
      this.cmbTerrainStyle.setPreferredSize(new Dimension(0, 28));
      this.cmbTerrainStyle.addActionListener(e -> this.triggerGeneration());
      gbc.gridy = row++;
      terrainTab.add(this.wrapComponentWithLabel("Kiểu Cấu Trúc Địa Hình:", this.cmbTerrainStyle), gbc);
      JPanel bgRow = new JPanel(new GridLayout(1, 3, 6, 0));
      bgRow.setOpaque(false);
      this.txtTileSet = this.createField(String.valueOf(initProf.tileSetId));
      this.txtIdBack = this.createField(String.valueOf(initProf.idBack));
      this.txtHBack = this.createField(String.valueOf(initProf.hBack));
      bgRow.add(this.wrapFieldWithLabel("Tileset ID:", this.txtTileSet));
      bgRow.add(this.wrapFieldWithLabel("IDBack:", this.txtIdBack));
      bgRow.add(this.wrapFieldWithLabel("HBack:", this.txtHBack));
      gbc.gridy = row++;
      terrainTab.add(bgRow, gbc);
      this.sldGroundHeight = new JSlider(20, 80, 45);
      this.sldGroundHeight.setOpaque(false);
      this.sldGroundHeight.setPreferredSize(new Dimension(0, 24));
      gbc.gridy = row++;
      terrainTab.add(this.createSliderRow("Độ Cao Mặt Đất:", this.sldGroundHeight, "%"), gbc);
      this.sldHillRoughness = new JSlider(0, 100, 35);
      this.sldHillRoughness.setOpaque(false);
      this.sldHillRoughness.setPreferredSize(new Dimension(0, 24));
      gbc.gridy = row++;
      terrainTab.add(this.createSliderRow("Độ Gồ Ghề / Bậc Cấp:", this.sldHillRoughness, "%"), gbc);
      this.chkPlatforms = StyledUI.createCheckBox("Bậc Thang / Platform Lơ Lửng (Floating Ledges)");
      this.chkPlatforms.setSelected(true);
      this.chkPlatforms.addActionListener(e -> this.triggerGeneration());
      gbc.gridy = row++;
      terrainTab.add(this.chkPlatforms, gbc);
      this.sldPlatformDensity = new JSlider(5, 80, 25);
      this.sldPlatformDensity.setOpaque(false);
      this.sldPlatformDensity.setPreferredSize(new Dimension(0, 24));
      gbc.gridy = row++;
      terrainTab.add(this.createSliderRow("Mật Độ Platform:", this.sldPlatformDensity, "%"), gbc);
      JPanel seedRow = new JPanel(new BorderLayout(6, 0));
      seedRow.setOpaque(false);
      this.txtSeed = this.createField(String.valueOf(System.currentTimeMillis()));
      seedRow.add(this.txtSeed, "Center");
      JButton btnDiceInline = StyledUI.createButton("\ud83c\udfb2", new Color(138, 43, 226));
      btnDiceInline.setPreferredSize(new Dimension(36, 26));
      btnDiceInline.addActionListener(e -> {
         this.txtSeed.setText(String.valueOf(System.currentTimeMillis()));
         this.triggerGeneration();
      });
      seedRow.add(btnDiceInline, "East");
      gbc.gridy = row++;
      terrainTab.add(this.wrapComponentWithLabel("Random Seed:", seedRow), gbc);
      gbc.gridy = row++;
      gbc.weighty = 1.0;
      gbc.fill = 1;
      terrainTab.add(Box.createGlue(), gbc);
      tabs.addTab("\ud83c\udf0d Biome & Địa Hình", terrainTab);
      JPanel decorTab = new JPanel(new GridBagLayout());
      decorTab.setOpaque(false);
      decorTab.setBorder(new EmptyBorder(10, 10, 10, 10));
      GridBagConstraints gbc2 = new GridBagConstraints();
      gbc2.insets = new Insets(3, 3, 3, 3);
      gbc2.fill = 2;
      gbc2.weightx = 1.0;
      gbc2.gridx = 0;
      int row2 = 0;
      this.chkItems = StyledUI.createCheckBox("Tự Động Đặt Vật Thể & Decor (Cây, Thùng, Đèn, Rương)");
      this.chkItems.setSelected(true);
      this.chkItems.addActionListener(e -> this.triggerGeneration());
      gbc2.gridy = row2++;
      decorTab.add(this.chkItems, gbc2);
      this.sldItemDensity = new JSlider(2, 40, 12);
      this.sldItemDensity.setOpaque(false);
      this.sldItemDensity.setPreferredSize(new Dimension(0, 24));
      gbc2.gridy = row2++;
      decorTab.add(this.createSliderRow("Mật Độ Decor:", this.sldItemDensity, "%"), gbc2);
      this.chkMobs = StyledUI.createCheckBox("Tự Động Sinh Quái (Mobs Theo Biome)");
      this.chkMobs.setSelected(true);
      this.chkMobs.addActionListener(e -> this.triggerGeneration());
      gbc2.gridy = row2++;
      decorTab.add(this.chkMobs, gbc2);
      JPanel mobRow = new JPanel(new GridLayout(1, 2, 8, 0));
      mobRow.setOpaque(false);
      this.txtMobCount = this.createField("8");
      this.txtMobLevel = this.createField("10");
      mobRow.add(this.wrapFieldWithLabel("Số Lượng Quái:", this.txtMobCount));
      mobRow.add(this.wrapFieldWithLabel("Cấp Độ Quái (Level):", this.txtMobLevel));
      gbc2.gridy = row2++;
      decorTab.add(mobRow, gbc2);
      this.chkVgos = StyledUI.createCheckBox("Tự Động Tạo Cổng Dịch Chuyển (VGO 2 Đầu)");
      this.chkVgos.setSelected(true);
      this.chkVgos.addActionListener(e -> this.triggerGeneration());
      gbc2.gridy = row2++;
      decorTab.add(this.chkVgos, gbc2);
      JPanel vgoRow = new JPanel(new GridLayout(1, 2, 8, 0));
      vgoRow.setOpaque(false);
      this.txtLeftVgo = this.createField("-1");
      this.txtRightVgo = this.createField("1");
      vgoRow.add(this.wrapFieldWithLabel("Cổng Trái (Map ID):", this.txtLeftVgo));
      vgoRow.add(this.wrapFieldWithLabel("Cổng Phải (Map ID):", this.txtRightVgo));
      gbc2.gridy = row2++;
      decorTab.add(vgoRow, gbc2);
      gbc2.gridy = row2++;
      gbc2.weighty = 1.0;
      gbc2.fill = 1;
      decorTab.add(Box.createGlue(), gbc2);
      tabs.addTab("\ud83c\udf34 Decor & Quái Vật", decorTab);
      JPanel batchTab = new JPanel(new GridBagLayout());
      batchTab.setOpaque(false);
      batchTab.setBorder(new EmptyBorder(10, 10, 10, 10));
      GridBagConstraints gbc3 = new GridBagConstraints();
      gbc3.insets = new Insets(3, 3, 3, 3);
      gbc3.fill = 2;
      gbc3.weightx = 1.0;
      gbc3.gridx = 0;
      int row3 = 0;
      this.txtBatchCount = this.createField("5");
      this.txtBatchStartId = this.createField(String.valueOf(nextId));
      this.txtBatchPrefix = this.createField("Đảo Hoang ");
      gbc3.gridy = row3++;
      batchTab.add(this.wrapFieldWithLabel("Số Lượng Map Chuỗi Cần Sinh:", this.txtBatchCount), gbc3);
      gbc3.gridy = row3++;
      batchTab.add(this.wrapFieldWithLabel("Start Map ID:", this.txtBatchStartId), gbc3);
      gbc3.gridy = row3++;
      batchTab.add(this.wrapFieldWithLabel("Tiền Tố Tên (Prefix):", this.txtBatchPrefix), gbc3);
      this.cmbBatchBiome = new JComboBox<>(AutoMapEngine.BiomeArchetype.values());
      this.cmbBatchBiome.setFont(Theme.F_TINY);
      this.cmbBatchBiome.setPreferredSize(new Dimension(0, 28));
      gbc3.gridy = row3++;
      batchTab.add(this.wrapComponentWithLabel("Chọn Biome Cho Chuỗi:", this.cmbBatchBiome), gbc3);
      JButton btnBatchExport = StyledUI.createButton("\ud83d\udce6 XUẤT CHUỖI BATCH MAPS (SQL / JSON)", Theme.ACCENT);
      btnBatchExport.setPreferredSize(new Dimension(0, 36));
      btnBatchExport.addActionListener(e -> this.executeBatchGenerate());
      gbc3.gridy = row3++;
      gbc3.insets = new Insets(12, 3, 3, 3);
      batchTab.add(btnBatchExport, gbc3);
      gbc3.gridy = row3++;
      gbc3.weighty = 1.0;
      gbc3.fill = 1;
      batchTab.add(Box.createGlue(), gbc3);
      tabs.addTab("\ud83d\udce6 Sinh Hàng Loạt (Batch)", batchTab);
      panel.add(tabs, "Center");
      return panel;
   }

   private JTextField createField(String defaultValue) {
      JTextField f = new JTextField(defaultValue);
      f.setBackground(Theme.BG_DARKER);
      f.setForeground(Theme.TEXT_MAIN);
      f.setCaretColor(Theme.ACCENT);
      f.setFont(Theme.F_TINY);
      f.setPreferredSize(new Dimension(0, 26));
      f.setMinimumSize(new Dimension(0, 26));
      f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), new EmptyBorder(3, 8, 3, 8)));
      f.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            AutoMapGeneratorDialog.this.triggerGeneration();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            AutoMapGeneratorDialog.this.triggerGeneration();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            AutoMapGeneratorDialog.this.triggerGeneration();
         }
      });
      return f;
   }

   private JPanel wrapFieldWithLabel(String labelText, JTextField field) {
      JPanel p = new JPanel(new BorderLayout(0, 2));
      p.setOpaque(false);
      JLabel lbl = StyledUI.createLabel(labelText, Theme.F_TINY, Theme.TEXT_MUTED);
      p.add(lbl, "North");
      p.add(field, "Center");
      return p;
   }

   private JPanel wrapComponentWithLabel(String labelText, JComponent comp) {
      JPanel p = new JPanel(new BorderLayout(0, 2));
      p.setOpaque(false);
      JLabel lbl = StyledUI.createLabel(labelText, Theme.F_TINY, Theme.TEXT_MUTED);
      p.add(lbl, "North");
      p.add(comp, "Center");
      return p;
   }

   private JPanel createSliderRow(String title, JSlider slider, String unit) {
      JPanel p = new JPanel(new BorderLayout(0, 2));
      p.setOpaque(false);
      JLabel lbl = StyledUI.createLabel(title + " " + slider.getValue() + unit, Theme.F_TINY, Theme.TEXT_MUTED);
      slider.addChangeListener(e -> {
         lbl.setText(title + " " + slider.getValue() + unit);
         this.triggerGeneration();
      });
      p.add(lbl, "North");
      p.add(slider, "Center");
      return p;
   }

   private void triggerGeneration() {
      if (!this.isUpdating) {
         try {
            AutoMapEngine.GenConfig config = new AutoMapEngine.GenConfig();
            config.id = Integer.parseInt(this.txtId.getText().trim());
            config.name = this.txtName.getText().trim();
            config.width = Math.min(127, Math.max(10, Integer.parseInt(this.txtWidth.getText().trim())));
            config.height = Math.min(127, Math.max(10, Integer.parseInt(this.txtHeight.getText().trim())));
            config.biome = (AutoMapEngine.BiomeArchetype)this.cmbBiome.getSelectedItem();
            config.terrainStyle = (AutoMapEngine.TerrainStyle)this.cmbTerrainStyle.getSelectedItem();
            config.tileSetId = Integer.parseInt(this.txtTileSet.getText().trim());
            config.idBack = Integer.parseInt(this.txtIdBack.getText().trim());
            config.hBack = Integer.parseInt(this.txtHBack.getText().trim());
            config.seed = Long.parseLong(this.txtSeed.getText().trim());
            config.groundHeightRatio = this.sldGroundHeight.getValue() / 100.0F;
            config.hillRoughness = this.sldHillRoughness.getValue() / 100.0F;
            config.enablePlatforms = this.chkPlatforms.isSelected();
            config.platformDensity = this.sldPlatformDensity.getValue() / 100.0F;
            config.autoItems = this.chkItems.isSelected();
            config.itemDensity = this.sldItemDensity.getValue() / 100.0F;
            config.autoMobs = this.chkMobs.isSelected();
            config.mobCount = Integer.parseInt(this.txtMobCount.getText().trim());
            config.mobLevel = Integer.parseInt(this.txtMobLevel.getText().trim());
            config.autoVgos = this.chkVgos.isSelected();
            config.targetLeftMapId = Short.parseShort(this.txtLeftVgo.getText().trim());
            config.targetRightMapId = Short.parseShort(this.txtRightVgo.getText().trim());
            this.generatedMap = AutoMapEngine.generateMap(config);
            this.previewCanvas.setMap(this.generatedMap);
            this.previewCanvas.repaint();
         } catch (Exception var2) {
         }
      }
   }

   private void saveDirectlyToProject() {
      if (this.generatedMap != null) {
         try {
            MapDataLoader.saveMap(this.generatedMap, "");
            JOptionPane.showMessageDialog(
               this, "✔ Đã lưu bản đồ ID " + this.generatedMap.id + " ('" + this.generatedMap.name + "') vào cơ sở dữ liệu dự án!", "Thành Công", 1
            );
         } catch (Exception var2) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu bản đồ: " + var2.getMessage(), "Lỗi", 0);
         }
      }
   }

   private void executeBatchGenerate() {
      try {
         int count = Integer.parseInt(this.txtBatchCount.getText().trim());
         int startId = Integer.parseInt(this.txtBatchStartId.getText().trim());
         String prefix = this.txtBatchPrefix.getText().trim();
         AutoMapEngine.BiomeArchetype batchBiome = (AutoMapEngine.BiomeArchetype)this.cmbBatchBiome.getSelectedItem();
         BatchMapExporter.BatchConfig batchConfig = new BatchMapExporter.BatchConfig();
         batchConfig.count = count;
         batchConfig.startId = startId;
         batchConfig.namePrefix = prefix;
         batchConfig.width = Integer.parseInt(this.txtWidth.getText().trim());
         batchConfig.height = Integer.parseInt(this.txtHeight.getText().trim());
         batchConfig.biome = batchBiome;
         List<GameMap> maps = BatchMapExporter.generateBatchChain(batchConfig);
         JFileChooser fc = new JFileChooser();
         fc.setSelectedFile(new File("batch_maps_" + startId + ".sql"));
         if (fc.showSaveDialog(this) == 0) {
            File file = fc.getSelectedFile();
            if (file.getName().endsWith(".json")) {
               BatchMapExporter.exportBatchToJson(maps, file);
            } else {
               BatchMapExporter.exportBatchToSql(maps, file);
            }

            JOptionPane.showMessageDialog(
               this, "✔ Đã tạo chuỗi " + count + " maps liên hoàn (ID " + startId + " -> " + (startId + count - 1) + ") và xuất thành công!", "Thành Công", 1
            );
         }
      } catch (Exception var9) {
         JOptionPane.showMessageDialog(this, "Lỗi sinh batch: " + var9.getMessage(), "Lỗi", 0);
      }
   }
}
