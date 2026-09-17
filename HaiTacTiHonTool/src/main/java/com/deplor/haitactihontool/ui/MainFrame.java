package com.deplor.haitactihontool.ui;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.Lang;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.map.ImageCache;
import com.deplor.haitactihontool.map.MainUI;
import com.deplor.haitactihontool.map.MapDataLoader;
import com.deplor.haitactihontool.map.SeasonKeywordStore;
import com.deplor.haitactihontool.map.SeasonThemeManager;
import com.deplor.haitactihontool.map.TemplateManager;
import com.deplor.haitactihontool.pet.PetCanvas;
import com.deplor.haitactihontool.ui.components.SleekFolderChooser;
import com.deplor.haitactihontool.ui.components.StyledUI;
import com.deplor.haitactihontool.ui.components.Toast;
import com.deplor.haitactihontool.ultimate_security.KeyAuth;
import com.deplor.haitactihontool.ultimate_security.LoginUI;
import com.deplor.haitactihontool.ultimate_security.SessionInfo;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class MainFrame extends JFrame {
   private static final String TOOL_MAP = "MAP";
   private static final String TOOL_PART = "PART";
   private static final String TOOL_FASHIONV2 = "FASHION V2";
   private static final String TOOL_PET = "PET";
   private static final String TOOL_MOB = "MOB";
   private static final String TOOL_EFFECT = "EFFECT";
   private static final String TOOL_EFFECTAUTO = "EFFECT AUTO";
   private static final String TOOL_LEAKRES = "LEAK RES";
   private static final String TOOL_TOOL = "TOOL";
   private static final String TOOL_SETTINGS = "SETTINGS";
   private static final String[] TOOL_ORDER = new String[]{"MAP", "PART", "FASHION V2", "PET", "MOB", "EFFECT", "EFFECT AUTO", "LEAK RES", "TOOL", "SETTINGS"};
   private JPanel centerContainer;
   private final Map<String, JButton> toolButtons = new HashMap<>();
   private final Map<String, JComponent> loadedPanels = new HashMap<>();
   private JLabel statusLabel;
   private JLabel expiryLabel;
   private String activeTool = null;
   private Point initialClick;
   private ExecutorService mapWorkerPool;
   private Timer heartbeatTimer;
   private final List<Runnable> pathSaveActions = new ArrayList<>();

   public MainFrame() {
      this.setUndecorated(true);
      this.setTitle(Lang.get("app_title"));
      this.setDefaultCloseOperation(3);
      this.setSize(1600, 900);
      this.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentMoved(ComponentEvent e) {
            MainFrame.this.adjustMaximizedBounds();
         }

         @Override
         public void componentResized(ComponentEvent e) {
            MainFrame.this.adjustMaximizedBounds();
         }
      });
      this.adjustMaximizedBounds();
      this.setExtendedState(6);
      this.setLocationRelativeTo(null);
      this.buildUI();
      this.startHeartbeat();
      SessionInfo.setOnSessionUpdatedListener(() -> {
         this.updateButtonStates();
         this.updateStatus();
         if (this.activeTool != null && !"SETTINGS".equals(this.activeTool) && !SessionInfo.canUseTool(this.activeTool)) {
            this.switchTool(this.activeTool);
         }
      });

      for (String t : TOOL_ORDER) {
         if ("SETTINGS".equals(t) || SessionInfo.canUseTool(t)) {
            this.switchTool(t);
            break;
         }
      }
   }

   private void buildUI() {
      this.setLayout(new BorderLayout());
      this.getContentPane().setBackground(Theme.BG_DARKER);
      JPanel northWrapper = new JPanel(new BorderLayout());
      northWrapper.setOpaque(false);
      northWrapper.add(this.buildCustomTitleBar(), "North");
      northWrapper.add(this.buildTopBar(), "South");
      this.add(northWrapper, "North");
      this.centerContainer = new JPanel(new CardLayout());
      this.centerContainer.setOpaque(false);
      this.centerContainer.setBorder(new EmptyBorder(15, 15, 15, 15));
      this.add(this.centerContainer, "Center");
      this.centerContainer.add(this.buildLockedPanel(), "LOCKED");
      this.add(this.buildStatusBar(), "South");
   }

   private JPanel buildTopBar() {
      JPanel bar = new JPanel(new BorderLayout());
      bar.setBackground(Theme.BG_DARK);
      bar.setPreferredSize(new Dimension(0, 60));
      bar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      JPanel left = new JPanel(new FlowLayout(0, 25, 0));
      left.setOpaque(false);
      JLabel logo = StyledUI.createLabel("HTTH", Theme.F_TITLE, Theme.ACCENT);
      left.add(logo);
      JPanel nav = new JPanel(new FlowLayout(0, 5, 12));
      nav.setOpaque(false);

      for (String tool : TOOL_ORDER) {
         JButton btn = this.buildNavButton(tool);
         this.toolButtons.put(tool, btn);
         boolean allowed = "SETTINGS".equals(tool) || SessionInfo.canUseTool(tool);
         btn.setEnabled(allowed);
         btn.addActionListener(e -> this.switchTool(tool));
         nav.add(btn);
      }

      left.add(nav);
      bar.add(left, "West");
      JPanel right = new JPanel(new FlowLayout(2, 20, 22));
      right.setOpaque(false);
      JLabel title = StyledUI.createLabel("PREMIUM DEVELOPMENT SUITE", Theme.F_TINY, Theme.TEXT_MUTED);
      right.add(title);
      bar.add(right, "East");
      return bar;
   }

   private JPanel buildStatusBar() {
      JPanel bar = new JPanel(new BorderLayout());
      bar.setBackground(Theme.BG_DARKER);
      bar.setPreferredSize(new Dimension(0, 30));
      bar.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      this.statusLabel = StyledUI.createLabel("Ready", Theme.F_TINY, Theme.TEXT_DIM);
      this.statusLabel.setBorder(new EmptyBorder(0, 15, 0, 0));
      this.expiryLabel = StyledUI.createLabel("", Theme.F_TINY, Theme.ACCENT);
      this.expiryLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
      bar.add(this.statusLabel, "West");
      bar.add(this.expiryLabel, "East");
      return bar;
   }

   private void switchTool(final String toolName) {
      if (!"SETTINGS".equals(toolName) && !SessionInfo.canUseTool(toolName)) {
         ((CardLayout)this.centerContainer.getLayout()).show(this.centerContainer, "LOCKED");
      } else {
         if ("MAP".equals(this.activeTool) && !"MAP".equals(toolName)) {
            this.suspendMapPool();
         }

         if ("MAP".equals(toolName) && !"MAP".equals(this.activeTool)) {
            this.ensureMapPool();
         }

         this.activeTool = toolName;
         if (!this.loadedPanels.containsKey(toolName)) {
            JPanel loading = this.buildLoadingPanel("LOADING " + toolName);
            this.centerContainer.add(loading, toolName + "_LOADING");
            ((CardLayout)this.centerContainer.getLayout()).show(this.centerContainer, toolName + "_LOADING");
            (new SwingWorker<JComponent, Void>() {
               protected JComponent doInBackground() {
                  return MainFrame.this.createToolPanel(toolName);
               }

               @Override
               protected void done() {
                  try {
                     JComponent panel = this.get();
                     if (panel != null) {
                        MainFrame.this.loadedPanels.put(toolName, panel);
                        MainFrame.this.centerContainer.add(panel, toolName);
                     }

                     if (toolName.equals(MainFrame.this.activeTool)) {
                        ((CardLayout)MainFrame.this.centerContainer.getLayout()).show(MainFrame.this.centerContainer, panel != null ? toolName : "LOCKED");
                     }
                  } catch (Exception var2) {
                     var2.printStackTrace();
                  }
               }
            }).execute();
         } else {
            ((CardLayout)this.centerContainer.getLayout()).show(this.centerContainer, toolName);
         }

         this.updateButtonStates();
         this.updateStatus();
      }
   }

   private JComponent createToolPanel(String toolName) {
      try {
         switch (toolName) {
            case "MAP":
               return new MainUI();
            case "PART":
               return new com.deplor.haitactihontool.part.MainUI();
            case "FASHION V2":
               return new com.deplor.haitactihontool.fashionv2.MainUI();
            case "PET":
               return new com.deplor.haitactihontool.pet.MainUI();
            case "MOB":
               return new com.deplor.haitactihontool.mob.MainUI();
            case "EFFECT":
               return new com.deplor.haitactihontool.effect.MainUI();
            case "EFFECT AUTO":
               return new com.deplor.haitactihontool.effectauto.MainUI();
            case "LEAK RES":
               return new com.deplor.haitactihontool.leakres.MainUI();
            case "TOOL":
               return new com.deplor.haitactihontool.tool.MainUI();
            case "SETTINGS":
               return this.buildSettingsPanel();
            default:
               return null;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   private JPanel buildSettingsPanel() {
      this.pathSaveActions.clear();
      JPanel p = new JPanel(new BorderLayout());
      p.setBackground(Theme.BG_DARK);
      p.setBorder(new EmptyBorder(30, 50, 30, 50));
      JPanel content = new JPanel();
      content.setLayout(new BoxLayout(content, 1));
      content.setOpaque(false);
      content.add(StyledUI.createHeader(Lang.get("tab_settings")));
      content.add(Box.createVerticalStrut(20));
      content.add(StyledUI.createLabel(Lang.get("settings_lang"), Theme.F_BOLD, Theme.TEXT_MAIN));
      content.add(Box.createVerticalStrut(10));
      JPanel langBox = new JPanel(new FlowLayout(0, 0, 0));
      langBox.setOpaque(false);
      JButton btnVi = StyledUI.createButton("TIẾNG VIỆT", Lang.getLocale() == Lang.Locale.VI ? Theme.ACCENT : Theme.BG_CARD);
      JButton btnEn = StyledUI.createButton("ENGLISH", Lang.getLocale() == Lang.Locale.EN ? Theme.ACCENT : Theme.BG_CARD);
      btnVi.setPreferredSize(new Dimension(140, 40));
      btnEn.setPreferredSize(new Dimension(140, 40));
      btnVi.addActionListener(e -> {
         AppConfig.setLanguage(Lang.Locale.VI);
         this.refreshAll();
      });
      btnEn.addActionListener(e -> {
         AppConfig.setLanguage(Lang.Locale.EN);
         this.refreshAll();
      });
      langBox.add(btnVi);
      langBox.add(Box.createHorizontalStrut(10));
      langBox.add(btnEn);
      content.add(langBox);
      content.add(Box.createVerticalStrut(30));
      content.add(StyledUI.createLabel(Lang.get("settings_data_dir"), Theme.F_BOLD, Theme.TEXT_MAIN));
      content.add(Box.createVerticalStrut(10));
      JPanel pathBox = new JPanel(new BorderLayout(10, 0));
      pathBox.setOpaque(false);
      pathBox.setMaximumSize(new Dimension(800, 36));
      pathBox.setAlignmentX(0.0F);
      final JTextField txtPath = StyledUI.createTextField(AppConfig.getDataDirectory());
      txtPath.putClientProperty("JTextField.placeholderText", Lang.get("settings_data_dir_placeholder"));
      txtPath.setPreferredSize(new Dimension(0, 36));
      this.pathSaveActions.add(() -> AppConfig.setDataDirectory(txtPath.getText().trim()));
      JButton btnBrowse = new JButton("...") {
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
            g2.setColor(this.hovered ? Theme.BG_HOVER : Theme.BG_CARD);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 6, 6);
            g2.setColor(Theme.BORDER);
            g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 6, 6);
            g2.setColor(Theme.TEXT_MAIN);
            g2.setFont(Theme.F_MAIN);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (this.getWidth() - fm.stringWidth(this.getText())) / 2;
            int ty = (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(this.getText(), tx, ty);
            g2.dispose();
         }
      };
      btnBrowse.setPreferredSize(new Dimension(50, 36));
      btnBrowse.setContentAreaFilled(false);
      btnBrowse.setBorderPainted(false);
      btnBrowse.setFocusPainted(false);
      btnBrowse.setCursor(new Cursor(12));
      btnBrowse.addActionListener(e -> {
         String current = txtPath.getText().trim();
         if (current.isEmpty()) {
            current = AppConfig.getDataDirectory();
         }

         String selected = SleekFolderChooser.showFolderChooser(this, current);
         if (selected != null) {
            txtPath.setText(selected);
            AppConfig.setDataDirectory(selected);
            this.commitAllSettingsAndReload();
            Toast.show(this, "Đã cập nhật thư mục Data gốc!", Theme.SUCCESS);
         }
      });
      txtPath.addActionListener(e -> this.commitAllSettingsAndReload());
      txtPath.addFocusListener(new FocusAdapter() {
         @Override
         public void focusLost(FocusEvent e) {
            AppConfig.setDataDirectory(txtPath.getText().trim());
         }
      });
      pathBox.add(txtPath, "Center");
      pathBox.add(btnBrowse, "East");
      content.add(pathBox);
      content.add(Box.createVerticalStrut(30));
      JPanel pathHeaderBox = new JPanel(new BorderLayout());
      pathHeaderBox.setOpaque(false);
      pathHeaderBox.setMaximumSize(new Dimension(800, 36));
      pathHeaderBox.setAlignmentX(0.0F);
      JLabel lblPathHeader = StyledUI.createLabel("INDIVIDUAL DATA PATHS", Theme.F_BOLD, Theme.TEXT_MAIN);
      JButton btnReloadAll = StyledUI.createButton("\ud83d\udd04 CẬP NHẬT & NẠP LẠI TẤT CẢ TOOL", Theme.ACCENT);
      btnReloadAll.setPreferredSize(new Dimension(240, 34));
      btnReloadAll.setFont(Theme.F_TINY);
      btnReloadAll.setToolTipText("Lưu tất cả thay đổi đường dẫn và nạp lại dữ liệu cho toàn bộ Tool ngay lập tức");
      btnReloadAll.addActionListener(e -> {
         this.commitAllSettingsAndReload();
         Toast.show(this, "Đã lưu cài đặt và làm mới dữ liệu cho tất cả Tool!", Theme.SUCCESS);
      });
      pathHeaderBox.add(lblPathHeader, "West");
      pathHeaderBox.add(btnReloadAll, "East");
      content.add(pathHeaderBox);
      content.add(Box.createVerticalStrut(10));
      content.add(StyledUI.createLabel("Tile Images Directory", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Tile", "path_tile", "Data/Map/ServerImage/", true));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("Water Images Directory", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Water", "path_water", "Data/Map/ServerImage/", true));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("Part Config File (.json)", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Part JSON", "path_part", "Data/Part/parts.json", false));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("Part Images Directory", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Part Img", "path_part_img", "Data/Part/img/", true));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("Map Config File (.json)", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Map JSON", "path_map", "Data/Map/ServerData/maps.json", false));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("Effect Data Directory", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Effect", "path_effect", "Data/Effect/", true));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("EffectAuto Data Directory", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("EffectAuto", "path_effectauto", "Data/EffectAuto/", true));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("Pet Config File (.sql)", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Pet SQL", "path_pet_sql", "Data/Pet/pet_template.sql", false));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("Pet Images Directory", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Pet Img", "path_pet_img", "Data/Pet/img/", true));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("Mob Config File (.sql)", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Mob SQL", "path_mob_sql", "Data/Mob/mobs.sql", false));
      content.add(Box.createVerticalStrut(12));
      content.add(StyledUI.createLabel("Mob Images Directory", Theme.F_SMALL, Theme.TEXT_DIM));
      content.add(Box.createVerticalStrut(4));
      content.add(this.createPathSettingRow("Mob Img", "path_mob_img", "Data/Mob/img/", true));
      content.add(Box.createVerticalStrut(20));
      JPanel bottomUpdateRow = new JPanel(new FlowLayout(0, 0, 0));
      bottomUpdateRow.setOpaque(false);
      JButton btnSaveSettingsBottom = StyledUI.createButton("\ud83d\udcbe LƯU CẤU HÌNH & NẠP LẠI TẤT CẢ", Theme.SUCCESS);
      btnSaveSettingsBottom.setPreferredSize(new Dimension(280, 38));
      btnSaveSettingsBottom.addActionListener(e -> {
         this.commitAllSettingsAndReload();
         Toast.show(this, "Đã lưu cài đặt và làm mới toàn bộ dữ liệu!", Theme.SUCCESS);
      });
      bottomUpdateRow.add(btnSaveSettingsBottom);
      content.add(bottomUpdateRow);
      content.add(Box.createVerticalStrut(30));
      content.add(StyledUI.createLabel("PREFERENCES", Theme.F_BOLD, Theme.TEXT_MAIN));
      content.add(Box.createVerticalStrut(10));
      JCheckBox cbAutoSave = StyledUI.createCheckBox("Auto-save Map Data on change");
      cbAutoSave.setSelected(AppConfig.isAutoSave());
      cbAutoSave.addActionListener(e -> AppConfig.setAutoSave(cbAutoSave.isSelected()));
      content.add(cbAutoSave);
      content.add(Box.createVerticalStrut(10));
      JCheckBox cbGrid = StyledUI.createCheckBox("Show Grid by default");
      cbGrid.setSelected(AppConfig.isShowGrid());
      cbGrid.addActionListener(e -> AppConfig.setShowGrid(cbGrid.isSelected()));
      content.add(cbGrid);
      content.add(Box.createVerticalStrut(30));
      content.add(StyledUI.createLabel("SYSTEM", Theme.F_BOLD, Theme.TEXT_MAIN));
      content.add(Box.createVerticalStrut(10));
      JPanel sysBox = new JPanel(new FlowLayout(0, 0, 0));
      sysBox.setOpaque(false);
      JButton btnSyncKey = StyledUI.createButton("ĐỒNG BỘ KEY TỪ GITHUB", Theme.ACCENT);
      btnSyncKey.setPreferredSize(new Dimension(220, 40));
      btnSyncKey.addActionListener(
         e -> new Thread(
               () -> {
                  boolean ok = KeyAuth.validateSession();
                  SwingUtilities.invokeLater(
                     () -> {
                        if (ok) {
                           JOptionPane.showMessageDialog(
                              this,
                              "Đã cập nhật dữ liệu Key thành công từ GitHub!\nLoại Key: " + SessionInfo.getLicenseType().toUpperCase(),
                              "Đồng Bộ Thành Công",
                              1
                           );
                        } else {
                           JOptionPane.showMessageDialog(this, "Không thể đồng bộ hoặc Key đã bị thay đổi!", "Lỗi Đồng Bộ", 0);
                        }
                     }
                  );
               },
               "sync-key"
            )
            .start()
      );
      JButton btnLogout = StyledUI.createButton("LOGOUT / SWITCH KEY", Theme.ERROR);
      btnLogout.setPreferredSize(new Dimension(200, 40));
      btnLogout.addActionListener(e -> {
         this.dispose();
         new LoginUI().setVisible(true);
      });
      sysBox.add(btnSyncKey);
      sysBox.add(Box.createHorizontalStrut(15));
      sysBox.add(btnLogout);
      content.add(sysBox);
      JScrollPane scroll = new JScrollPane(content);
      StyledUI.styleScrollPane(scroll);
      p.add(scroll, "Center");
      return p;
   }

   public void commitAllSettingsAndReload() {
      for (Runnable saveAction : this.pathSaveActions) {
         try {
            saveAction.run();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      this.invalidateToolPanels();
   }

   public void invalidateToolPanels() {
      MapDataLoader.clearCache();
      ImageCache.clearCache();
      TemplateManager.reset();
      PetCanvas.clearPetImageCache();
      SeasonKeywordStore.reset();
      SeasonThemeManager.reset();
      JComponent settingsPanel = this.loadedPanels.get("SETTINGS");
      this.loadedPanels.clear();
      if (settingsPanel != null) {
         this.loadedPanels.put("SETTINGS", settingsPanel);
      }

      Component[] components = this.centerContainer.getComponents();

      for (Component c : components) {
         if (c != settingsPanel) {
            this.centerContainer.remove(c);
         }
      }

      this.centerContainer.revalidate();
      this.centerContainer.repaint();
   }

   private JPanel createPathSettingRow(String labelText, final String key, String defaultValue, boolean isFolder) {
      JPanel row = new JPanel(new BorderLayout(10, 0));
      row.setOpaque(false);
      row.setMaximumSize(new Dimension(800, 36));
      row.setAlignmentX(0.0F);
      final JTextField txt = StyledUI.createTextField(AppConfig.getPathSetting(key, defaultValue));
      txt.setPreferredSize(new Dimension(0, 36));
      this.pathSaveActions.add(() -> AppConfig.setPathSetting(key, txt.getText().trim()));
      txt.addActionListener(e -> this.commitAllSettingsAndReload());
      txt.addFocusListener(new FocusAdapter() {
         @Override
         public void focusLost(FocusEvent e) {
            AppConfig.setPathSetting(key, txt.getText().trim());
         }
      });
      JButton btn = new JButton("...") {
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
            g2.setColor(this.hovered ? Theme.BG_HOVER : Theme.BG_CARD);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 6, 6);
            g2.setColor(Theme.BORDER);
            g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 6, 6);
            g2.setColor(Theme.TEXT_MAIN);
            g2.setFont(Theme.F_MAIN);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (this.getWidth() - fm.stringWidth(this.getText())) / 2, (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      btn.setPreferredSize(new Dimension(50, 36));
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      btn.addActionListener(e -> {
         String current = txt.getText().trim();
         String selected;
         if (isFolder) {
            selected = SleekFolderChooser.showFolderChooser(this, current);
         } else {
            selected = SleekFolderChooser.showFileChooser(this, current);
         }

         if (selected != null) {
            txt.setText(selected);
            AppConfig.setPathSetting(key, selected);
            this.commitAllSettingsAndReload();
         }
      });
      row.add(txt, "Center");
      row.add(btn, "East");
      return row;
   }

   private void refreshAll() {
      String prevTool = this.activeTool != null ? this.activeTool : "SETTINGS";
      this.loadedPanels.clear();
      this.centerContainer.removeAll();
      this.centerContainer.add(this.buildLockedPanel(), "LOCKED");
      this.getContentPane().removeAll();
      this.buildUI();
      this.switchTool(prevTool);
      this.revalidate();
      this.repaint();
   }

   private void updateButtonStates() {
      for (Entry<String, JButton> e : this.toolButtons.entrySet()) {
         boolean allowed = "SETTINGS".equals(e.getKey()) || SessionInfo.canUseTool(e.getKey());
         e.getValue().setEnabled(allowed);
         boolean active = e.getKey().equals(this.activeTool);
         e.getValue().setBackground(active ? Theme.ACCENT : Theme.BG_DARKER);
         e.getValue().setForeground(active ? Color.WHITE : (allowed ? Theme.TEXT_DIM : Theme.TEXT_MUTED));
      }
   }

   private JButton buildNavButton(final String name) {
      String label = Lang.get("tab_" + name.toLowerCase().replace(" ", ""));
      JButton btn = new JButton(label) {
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
            boolean active = name.equals(MainFrame.this.activeTool);
            if (active) {
               g2.setColor(Theme.ACCENT);
               g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 8, 8);
            } else if (this.hovered) {
               g2.setColor(Theme.BG_HOVER);
               g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 8, 8);
            }

            g2.setColor(active ? Color.WHITE : (this.hovered ? Theme.TEXT_MAIN : Theme.TEXT_DIM));
            g2.setFont(Theme.F_BOLD);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (this.getWidth() - fm.stringWidth(this.getText())) / 2, (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      btn.setPreferredSize(new Dimension(110, 36));
      btn.setOpaque(false);
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      return btn;
   }

   private JPanel buildLockedPanel() {
      JPanel p = new JPanel(new GridBagLayout());
      p.setOpaque(false);
      p.add(StyledUI.createLabel("ACCESS DENIED - UPGRADE LICENSE", Theme.F_TITLE, Theme.TEXT_MUTED));
      return p;
   }

   private JPanel buildLoadingPanel(String msg) {
      JPanel p = new JPanel(new GridBagLayout());
      p.setOpaque(false);
      p.add(StyledUI.createLabel(msg, Theme.F_TITLE, Theme.ACCENT));
      return p;
   }

   private void ensureMapPool() {
      if (this.mapWorkerPool == null || this.mapWorkerPool.isShutdown()) {
         this.mapWorkerPool = Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors() - 1), r -> {
            Thread t = new Thread(r, "HTTH-MapWorker");
            t.setDaemon(true);
            return t;
         });
         MainFrame.MapWorkerPool.setPool(this.mapWorkerPool);
      }
   }

   private void suspendMapPool() {
      if (this.mapWorkerPool != null && !this.mapWorkerPool.isShutdown()) {
         this.mapWorkerPool.shutdown();
         this.mapWorkerPool = null;
         MainFrame.MapWorkerPool.setPool(null);
      }
   }

   private void startHeartbeat() {
      this.heartbeatTimer = new Timer(60000, e -> {
         if (SessionInfo.isExpired()) {
            JOptionPane.showMessageDialog(this, "License Expired", "Error", 0);
            System.exit(0);
         }

         this.updateStatus();
      });
      this.heartbeatTimer.setInitialDelay(0);
      this.heartbeatTimer.start();
   }

   private void updateStatus() {
      this.statusLabel.setText(SessionInfo.getStatusText());
      if (SessionInfo.isExpired()) {
         this.expiryLabel.setText(Lang.get("status_expired"));
         this.expiryLabel.setForeground(Theme.ERROR);
      } else {
         long mins = SessionInfo.minutesRemaining();
         this.expiryLabel.setText(mins == Long.MAX_VALUE ? "LIFETIME" : mins + "m left");
      }
   }

   private void adjustMaximizedBounds() {
      GraphicsConfiguration gc = this.getGraphicsConfiguration();
      if (gc == null) {
         gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
      }

      Rectangle bounds = gc.getBounds();
      Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
      Rectangle maxBounds = new Rectangle(insets.left, insets.top, bounds.width - (insets.left + insets.right), bounds.height - (insets.top + insets.bottom));
      this.setMaximizedBounds(maxBounds);
   }

   private JPanel buildCustomTitleBar() {
      JPanel bar = new JPanel(new BorderLayout());
      bar.setBackground(Theme.BG_DARKER);
      bar.setPreferredSize(new Dimension(0, 36));
      bar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      MouseAdapter dragListener = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            MainFrame.this.initialClick = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            int thisX = MainFrame.this.getLocation().x;
            int thisY = MainFrame.this.getLocation().y;
            int xMoved = e.getX() - MainFrame.this.initialClick.x;
            int yMoved = e.getY() - MainFrame.this.initialClick.y;
            MainFrame.this.setLocation(thisX + xMoved, thisY + yMoved);
         }
      };
      bar.addMouseListener(dragListener);
      bar.addMouseMotionListener(dragListener);
      JLabel lblTitle = StyledUI.createLabel("  [HTTH] - PREMIUM DEV SUITE v5.2", Theme.F_TINY, Theme.TEXT_DIM);
      bar.add(lblTitle, "West");
      JPanel controls = new JPanel(new FlowLayout(2, 0, 0));
      controls.setOpaque(false);
      JButton btnMin = this.createTitleBarButton("MIN", Theme.BG_DARKER, Theme.BG_HOVER, Theme.TEXT_MAIN);
      btnMin.addActionListener(e -> this.setExtendedState(1));
      JButton btnMax = this.createTitleBarButton("MAX", Theme.BG_DARKER, Theme.BG_HOVER, Theme.TEXT_MAIN);
      btnMax.addActionListener(e -> {
         if ((this.getExtendedState() & 6) == 6) {
            this.setExtendedState(0);
         } else {
            this.setExtendedState(6);
         }
      });
      JButton btnClose = this.createTitleBarButton("CLOSE", Theme.BG_DARKER, Theme.ERROR, Theme.TEXT_MAIN);
      btnClose.addActionListener(e -> System.exit(0));
      this.addWindowStateListener(e -> btnMax.repaint());
      controls.add(btnMin);
      controls.add(btnMax);
      controls.add(btnClose);
      bar.add(controls, "East");
      return bar;
   }

   private JButton createTitleBarButton(final String actionType, final Color bg, final Color hoverBg, final Color fg) {
      JButton btn = new JButton() {
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
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(this.hovered ? hoverBg : bg);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            g2.setColor(this.hovered ? Color.WHITE : fg);
            g2.setStroke(new BasicStroke(1.2F));
            int w = this.getWidth();
            int h = this.getHeight();
            int size = 10;
            int cx = w / 2;
            int cy = h / 2;
            if ("MIN".equals(actionType)) {
               g2.drawLine(cx - size / 2, cy, cx + size / 2, cy);
            } else if ("MAX".equals(actionType)) {
               boolean isMax = (MainFrame.this.getExtendedState() & 6) == 6;
               if (isMax) {
                  int offset = 2;
                  g2.drawRect(cx - size / 2 + offset, cy - size / 2 - offset, size - 2, size - 2);
                  g2.setColor(this.hovered ? hoverBg : bg);
                  g2.fillRect(cx - size / 2, cy - size / 2, size - 2, size - 2);
                  g2.setColor(this.hovered ? Color.WHITE : fg);
                  g2.drawRect(cx - size / 2, cy - size / 2, size - 2, size - 2);
               } else {
                  g2.drawRect(cx - size / 2, cy - size / 2, size, size);
               }
            } else if ("CLOSE".equals(actionType)) {
               int hs = size / 2;
               g2.drawLine(cx - hs, cy - hs, cx + hs, cy + hs);
               g2.drawLine(cx + hs, cy - hs, cx - hs, cy + hs);
            }

            g2.dispose();
         }
      };
      btn.setPreferredSize(new Dimension(46, 36));
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      return btn;
   }

   public static void main(String[] args) {
      Bootstrap.setupTheme();
      SessionInfo.activate("admin", "00:00 01/01/2000", "00:00 01/01/2100", "Dev Test Session");
      SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
   }

   public static class MapWorkerPool {
      private static volatile ExecutorService pool;

      public static void setPool(ExecutorService p) {
         pool = p;
      }

      public static void submit(Runnable task) {
         ExecutorService p = pool;
         if (p != null && !p.isShutdown()) {
            p.submit(task);
         } else {
            new Thread(task).start();
         }
      }
   }
}
