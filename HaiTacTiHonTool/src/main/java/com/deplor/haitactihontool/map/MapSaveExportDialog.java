package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class MapSaveExportDialog extends JDialog {
   private final GameMap currentMap;
   private final List<GameMap> allMaps;
   private JRadioButton rbCurrentMap;
   private JRadioButton rbAllMaps;
   private JCheckBox chkBinary;
   private JCheckBox chkImage;
   private JCheckBox chkSql;
   private JCheckBox chkJson;
   private JTextField txtDirectory;
   private JCheckBox chkOrganizeFolders;
   private JProgressBar progressBar;
   private JLabel lblStatus;
   private JButton btnBrowse;
   private JButton btnStart;
   private JButton btnOpenFolder;
   private JButton btnClose;
   private boolean isExporting = false;

   public MapSaveExportDialog(Frame owner, GameMap currentMap, List<GameMap> allMaps) {
      super(owner, "\ud83d\udcbe LƯU & XUẤT DỮ LIỆU MAP", true);
      this.currentMap = currentMap;
      this.allMaps = (List<GameMap>)(allMaps != null ? allMaps : new ArrayList<>());
      this.setUndecorated(true);
      this.setSize(680, 580);
      this.setLocationRelativeTo(owner);
      this.initUI();
   }

   private void initUI() {
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBackground(Theme.BG_DARKER);
      mainPanel.setBorder(BorderFactory.createLineBorder(Theme.BORDER_LIGHT, 2));
      JPanel headerBar = new JPanel(new BorderLayout());
      headerBar.setBackground(Theme.BG_DARK);
      headerBar.setPreferredSize(new Dimension(0, 42));
      headerBar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      JLabel titleLabel = StyledUI.createLabel("  \ud83d\udcbe LƯU & XUẤT DỮ LIỆU MAP (SAVE & EXPORT)", Theme.F_BOLD, Color.WHITE);
      headerBar.add(titleLabel, "West");
      JButton btnCloseHeader = new JButton("X") {
         {
            this.setFont(Theme.F_BOLD);
            this.setForeground(Theme.TEXT_DIM);
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setCursor(new Cursor(12));
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  setForeground(Theme.ERROR);
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  setForeground(Theme.TEXT_DIM);
               }
            });
         }
      };
      btnCloseHeader.addActionListener(e -> {
         if (!this.isExporting) {
            this.dispose();
         }
      });
      headerBar.add(btnCloseHeader, "East");
      final Point[] dragPoint = new Point[]{null};
      headerBar.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            dragPoint[0] = e.getPoint();
         }
      });
      headerBar.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (dragPoint[0] != null) {
               Point p = e.getLocationOnScreen();
               MapSaveExportDialog.this.setLocation(p.x - dragPoint[0].x, p.y - dragPoint[0].y);
            }
         }
      });
      mainPanel.add(headerBar, "North");
      JPanel contentPanel = new JPanel();
      contentPanel.setLayout(new BoxLayout(contentPanel, 1));
      contentPanel.setBackground(Theme.BG_DARKER);
      contentPanel.setBorder(new EmptyBorder(14, 18, 14, 18));
      JPanel scopeCard = this.createSectionCard("1. PHẠM VI LƯU & XUẤT (SCOPE)");
      scopeCard.setLayout(new GridLayout(1, 2, 10, 5));
      String currentMapLabel = this.currentMap != null
         ? "Map hiện tại: #" + this.currentMap.id + " (" + this.currentMap.name + ")"
         : "Map hiện tại (Chưa chọn)";
      this.rbCurrentMap = new JRadioButton(currentMapLabel, this.currentMap != null);
      this.styleRadioButton(this.rbCurrentMap);
      String allMapsLabel = "Toàn bộ danh sách (" + this.allMaps.size() + " map)";
      this.rbAllMaps = new JRadioButton(allMapsLabel, this.currentMap == null);
      this.styleRadioButton(this.rbAllMaps);
      ButtonGroup scopeGroup = new ButtonGroup();
      scopeGroup.add(this.rbCurrentMap);
      scopeGroup.add(this.rbAllMaps);
      scopeCard.add(this.rbCurrentMap);
      scopeCard.add(this.rbAllMaps);
      contentPanel.add(scopeCard);
      contentPanel.add(Box.createVerticalStrut(10));
      JPanel formatCard = this.createSectionCard("2. ĐỊNH DẠNG DỮ LIỆU CẦN LƯU (FORMATS)");
      formatCard.setLayout(new GridLayout(4, 1, 4, 6));
      this.chkBinary = StyledUI.createCheckBox("\ud83d\udcbe Data Binary ({id}_data block map + {id}_item items) - Chuẩn nhị phân tối ưu");
      this.chkBinary.setSelected(true);
      this.chkBinary.setToolTipText("Lưu 2 file binary riêng biệt: {id}_data (w*h bytes block) và {id}_item (items nhị phân)");
      this.chkImage = StyledUI.createCheckBox("\ud83d\udcf7 Ảnh Map Hoàn Chỉnh PNG ({id}.png) - Không nền trong suốt ARGB");
      this.chkImage.setSelected(true);
      this.chkImage.setToolTipText("Xuất ảnh render đầy đủ tất cả layer item và tile, nền trong suốt");
      this.chkSql = StyledUI.createCheckBox("\ud83d\udcdc File SQL Script (maps.sql) - Schema cột riêng biệt (w, h, tile_id, ...)");
      this.chkSql.setSelected(true);
      this.chkSql.setToolTipText("Xuất câu lệnh SQL với các cột riêng biệt, không còn data JSON cồng kềnh");
      this.chkJson = StyledUI.createCheckBox("\ud83d\udce6 File JSON Data (maps.json) - Cấu trúc JSON chuẩn");
      this.chkJson.setSelected(true);
      this.chkJson.setToolTipText("Xuất toàn bộ dữ liệu map ra file JSON hoàn chỉnh");
      formatCard.add(this.chkBinary);
      formatCard.add(this.chkImage);
      formatCard.add(this.chkSql);
      formatCard.add(this.chkJson);
      contentPanel.add(formatCard);
      contentPanel.add(Box.createVerticalStrut(10));
      JPanel dirCard = this.createSectionCard("3. THƯ MỤC LƯU DỮ LIỆU (DESTINATION DIRECTORY)");
      dirCard.setLayout(new BorderLayout(8, 8));
      String defaultDirPath = this.getDefaultDirectoryPath();
      this.txtDirectory = StyledUI.createTextField(defaultDirPath);
      this.txtDirectory.setFont(Theme.F_SMALL);
      this.btnBrowse = StyledUI.createButton("\ud83d\udcc1 CHỌN THƯ MỤC...", Theme.ACCENT);
      this.btnBrowse.setPreferredSize(new Dimension(150, 32));
      this.btnBrowse.addActionListener(e -> this.chooseDirectory());
      JPanel dirInputRow = new JPanel(new BorderLayout(6, 0));
      dirInputRow.setOpaque(false);
      dirInputRow.add(this.txtDirectory, "Center");
      dirInputRow.add(this.btnBrowse, "East");
      dirCard.add(dirInputRow, "North");
      JPanel dirOptionsRow = new JPanel(new BorderLayout(10, 0));
      dirOptionsRow.setOpaque(false);
      JPanel presetButtons = new JPanel(new FlowLayout(0, 5, 0));
      presetButtons.setOpaque(false);
      JButton btnPresetTool = StyledUI.createButton("\ud83c\udfe0 Thư mục Tool (Mặc định)", Theme.BG_CARD);
      btnPresetTool.setFont(Theme.F_TINY);
      btnPresetTool.addActionListener(e -> this.txtDirectory.setText(this.getDefaultDirectoryPath()));
      JButton btnPresetExport = StyledUI.createButton("\ud83d\udcc2 Thư mục Export Riêng", Theme.BG_CARD);
      btnPresetExport.setFont(Theme.F_TINY);
      btnPresetExport.addActionListener(e -> {
         File expDir = new File("Data/Map/Export");
         if (!expDir.exists()) {
            expDir.mkdirs();
         }

         try {
            this.txtDirectory.setText(expDir.getCanonicalPath());
         } catch (Exception var4x) {
            this.txtDirectory.setText(expDir.getAbsolutePath());
         }
      });
      presetButtons.add(btnPresetTool);
      presetButtons.add(btnPresetExport);
      this.chkOrganizeFolders = StyledUI.createCheckBox("Tự động phân loại thư mục (Data/Map/...)");
      this.chkOrganizeFolders.setSelected(true);
      this.chkOrganizeFolders.setFont(Theme.F_TINY);
      dirOptionsRow.add(presetButtons, "West");
      dirOptionsRow.add(this.chkOrganizeFolders, "East");
      dirCard.add(dirOptionsRow, "South");
      contentPanel.add(dirCard);
      contentPanel.add(Box.createVerticalStrut(10));
      JPanel progressCard = this.createSectionCard("4. TIẾN ĐỘ & TRẠNG THÁI (STATUS)");
      progressCard.setLayout(new BorderLayout(5, 5));
      this.lblStatus = StyledUI.createLabel("Sẵn sàng thực hiện lưu & xuất dữ liệu.", Theme.F_SMALL, Theme.TEXT_MAIN);
      this.progressBar = new JProgressBar(0, 100);
      this.progressBar.setStringPainted(true);
      this.progressBar.setBackground(Theme.BG_DARK);
      this.progressBar.setForeground(Theme.SUCCESS);
      this.progressBar.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
      this.progressBar.setPreferredSize(new Dimension(0, 22));
      progressCard.add(this.lblStatus, "North");
      progressCard.add(this.progressBar, "Center");
      contentPanel.add(progressCard);
      mainPanel.add(contentPanel, "Center");
      JPanel footerBar = new JPanel(new FlowLayout(2, 10, 10));
      footerBar.setBackground(Theme.BG_DARK);
      footerBar.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      this.btnOpenFolder = StyledUI.createButton("\ud83d\udcc1 MỞ THƯ MỤC", new Color(0, 150, 200));
      this.btnOpenFolder.setEnabled(false);
      this.btnOpenFolder.addActionListener(e -> this.openOutputDirectory());
      this.btnClose = StyledUI.createButton("ĐÓNG", Theme.BG_CARD);
      this.btnClose.addActionListener(e -> {
         if (!this.isExporting) {
            this.dispose();
         }
      });
      this.btnStart = StyledUI.createButton("\ud83d\udcbe BẮT ĐẦU LƯU & XUẤT", Theme.SUCCESS);
      this.btnStart.setPreferredSize(new Dimension(200, 36));
      this.btnStart.addActionListener(e -> this.executeSaveAndExport());
      footerBar.add(this.btnOpenFolder);
      footerBar.add(this.btnClose);
      footerBar.add(this.btnStart);
      mainPanel.add(footerBar, "South");
      this.setContentPane(mainPanel);
   }

   private String getDefaultDirectoryPath() {
      try {
         return new File(".").getCanonicalPath();
      } catch (Exception var2) {
         return System.getProperty("user.dir", ".");
      }
   }

   private void chooseDirectory() {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileSelectionMode(1);
      chooser.setDialogTitle("Chọn Thư Mục Lưu Dữ Liệu Map");
      String cur = this.txtDirectory.getText().trim();
      if (!cur.isEmpty()) {
         File f = new File(cur);
         if (f.exists()) {
            chooser.setCurrentDirectory(f);
         }
      }

      if (chooser.showOpenDialog(this) == 0) {
         try {
            this.txtDirectory.setText(chooser.getSelectedFile().getCanonicalPath());
         } catch (Exception var4) {
            this.txtDirectory.setText(chooser.getSelectedFile().getAbsolutePath());
         }
      }
   }

   private void openOutputDirectory() {
      try {
         String dirStr = this.txtDirectory.getText().trim();
         File dir = new File(dirStr);
         if (dir.exists() && Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(dir);
         }
      } catch (Exception var3) {
         JOptionPane.showMessageDialog(this, "Không thể mở thư mục: " + var3.getMessage());
      }
   }

   private JPanel createSectionCard(String title) {
      JPanel card = new JPanel();
      card.setBackground(Theme.BG_CARD);
      card.setBorder(
         BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), title, 1, 2, Theme.F_TINY, Theme.TEXT_MUTED),
            new EmptyBorder(6, 10, 8, 10)
         )
      );
      return card;
   }

   private void styleRadioButton(JRadioButton rb) {
      rb.setOpaque(false);
      rb.setForeground(Theme.TEXT_MAIN);
      rb.setFont(Theme.F_MAIN);
      rb.setFocusPainted(false);
      rb.setCursor(new Cursor(12));
   }

   private void executeSaveAndExport() {
      if (!this.isExporting) {
         boolean doBinary = this.chkBinary.isSelected();
         boolean doImage = this.chkImage.isSelected();
         boolean doSql = this.chkSql.isSelected();
         boolean doJson = this.chkJson.isSelected();
         if (!doBinary && !doImage && !doSql && !doJson) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 định dạng cần lưu/xuất!", "Thông báo", 2);
         } else {
            String baseDirStr = this.txtDirectory.getText().trim();
            if (baseDirStr.isEmpty()) {
               baseDirStr = this.getDefaultDirectoryPath();
            }

            File baseDir = new File(baseDirStr);
            if (!baseDir.exists()) {
               baseDir.mkdirs();
            }

            boolean isBatch = this.rbAllMaps.isSelected();
            List<GameMap> targetMaps = new ArrayList<>();
            if (isBatch) {
               if (this.allMaps.isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Danh sách map trống!", "Lỗi", 0);
                  return;
               }

               targetMaps.addAll(this.allMaps);
            } else {
               if (this.currentMap == null) {
                  JOptionPane.showMessageDialog(this, "Chưa chọn map nào để lưu!", "Lỗi", 0);
                  return;
               }

               targetMaps.add(this.currentMap);
            }

            boolean organize = this.chkOrganizeFolders.isSelected();
            File binaryDir = organize ? new File(baseDir, "Data/Map/ServerData/binary") : baseDir;
            File imagesDir = organize ? new File(baseDir, "Data/Map/Export/images") : baseDir;
            File sqlDir = organize ? new File(baseDir, "Data/Map/ServerData") : baseDir;
            File jsonDir = organize ? new File(baseDir, "Data/Map/ServerData") : baseDir;
            this.isExporting = true;
            this.btnStart.setEnabled(false);
            this.btnBrowse.setEnabled(false);
            this.btnClose.setEnabled(false);
            this.progressBar.setValue(0);
            this.progressBar.setMaximum(targetMaps.size());
            new Thread(
                  () -> {
                     try {
                        if (doImage) {
                           SwingUtilities.invokeLater(() -> this.lblStatus.setText("Đang nạp cache hình ảnh..."));
                           ImageCache.preloadAll("");
                        }

                        int total = targetMaps.size();
                        int successCount = 0;

                        for (int i = 0; i < total; i++) {
                           GameMap map = targetMaps.get(i);
                           if (map != null) {
                              int currentIdx = i + 1;
                              SwingUtilities.invokeLater(() -> {
                                 this.progressBar.setValue(currentIdx);
                                 this.lblStatus.setText("Đang xử lý map " + map.id + " (" + currentIdx + "/" + total + ")...");
                              });
                              if (doBinary) {
                                 MapDataExporter.exportMapBinaryFiles(map, binaryDir);
                              }

                              if (doImage) {
                                 File imgFile = new File(imagesDir, map.id + ".png");
                                 MapImageExporter.exportMapToPng(map, imgFile);
                              }

                              successCount++;
                           }
                        }

                        if (doSql) {
                           SwingUtilities.invokeLater(() -> this.lblStatus.setText("Đang tạo file SQL..."));
                           if (!sqlDir.exists()) {
                              sqlDir.mkdirs();
                           }

                           if (isBatch) {
                              File sqlFile = new File(sqlDir, "maps.sql");
                              BatchMapExporter.exportBatchToSql(targetMaps, sqlFile);
                           } else {
                              File sqlFile = new File(sqlDir, this.currentMap.id + ".sql");

                              try (FileWriter fw = new FileWriter(sqlFile, StandardCharsets.UTF_8)) {
                                 fw.write(MapDataGenerator.generateSQL(this.currentMap) + "\n");
                              }
                           }
                        }

                        if (doJson) {
                           SwingUtilities.invokeLater(() -> this.lblStatus.setText("Đang tạo file JSON..."));
                           if (!jsonDir.exists()) {
                              jsonDir.mkdirs();
                           }

                           if (isBatch) {
                              File jsonFile = new File(jsonDir, "maps.json");
                              BatchMapExporter.exportBatchToJson(targetMaps, jsonFile);
                           } else {
                              File jsonFile = new File(jsonDir, this.currentMap.id + ".json");

                              try (FileWriter fw = new FileWriter(jsonFile, StandardCharsets.UTF_8)) {
                                 fw.write(MapDataGenerator.generateJSON(this.currentMap));
                              }
                           }
                        }

                        MapDataLoader.setCachedMaps(this.allMaps);
                        int finalCount = successCount;
                        SwingUtilities.invokeLater(
                           () -> {
                              this.isExporting = false;
                              this.btnStart.setEnabled(true);
                              this.btnBrowse.setEnabled(true);
                              this.btnClose.setEnabled(true);
                              this.btnOpenFolder.setEnabled(true);
                              this.progressBar.setValue(this.progressBar.getMaximum());
                              this.lblStatus.setText("\ud83c\udf89 Hoàn tất thành công! Đã lưu/xuất " + finalCount + " map.");
                              JOptionPane.showMessageDialog(
                                 this,
                                 "\ud83c\udf89 Lưu & Xuất dữ liệu hoàn tất thành công!\n• Số lượng map: "
                                    + finalCount
                                    + "\n• Thư mục đích: "
                                    + baseDir.getAbsolutePath()
                                    + "\n"
                                    + (doBinary ? "  - Binary: " + binaryDir.getAbsolutePath() + "\n" : "")
                                    + (doImage ? "  - Ảnh PNG: " + imagesDir.getAbsolutePath() + "\n" : "")
                                    + (doSql ? "  - SQL: " + sqlDir.getAbsolutePath() + "\n" : "")
                                    + (doJson ? "  - JSON: " + jsonDir.getAbsolutePath() + "\n" : ""),
                                 "Thành Công",
                                 1
                              );
                           }
                        );
                     } catch (Exception var22) {
                        SwingUtilities.invokeLater(() -> {
                           this.isExporting = false;
                           this.btnStart.setEnabled(true);
                           this.btnBrowse.setEnabled(true);
                           this.btnClose.setEnabled(true);
                           this.lblStatus.setText("❌ Lỗi khi xuất: " + var22.getMessage());
                           JOptionPane.showMessageDialog(this, "Lỗi trong quá trình xuất: " + var22.getMessage(), "Lỗi", 0);
                        });
                     }
                  }
               )
               .start();
         }
      }
   }
}
