package com.deplor.haitactihontool.part;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.ImageZoomHelper;
import com.deplor.haitactihontool.config.Lang;
import com.deplor.haitactihontool.ui.components.SmartFolderChooser;
import com.deplor.haitactihontool.util.GifExportHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

public class PartSaveDialog extends JDialog {
   private static final Color BG = new Color(14, 14, 20);
   private static final Color BG2 = new Color(20, 20, 28);
   private static final Color BG3 = new Color(24, 24, 34);
   private static final Color ACCENT = new Color(100, 60, 200);
   private static final Color SUCCESS = new Color(30, 160, 80);
   private static final Color WARN = new Color(200, 140, 0);
   private static final Color ERROR = new Color(200, 50, 50);
   private static final Color TEXT = new Color(200, 200, 215);
   private static final Color TEXTDIM = new Color(130, 130, 150);
   private static final Color BORDER = new Color(36, 36, 52);
   private static final Font F_BOLD = new Font("Segoe UI", 1, 12);
   private static final Font F_TINY = new Font("Segoe UI", 0, 10);
   private static final Font F_MONO = new Font("Consolas", 0, 11);
   private final List<mPart> parts;
   private final PartCanvas canvas;
   private JTextField txtOutputDir;
   private JSpinner spinStartPartId;
   private JSpinner spinStartImgId;
   private JTable previewTable;
   private PartSaveDialog.PartPreviewTableModel tableModel;
   private JProgressBar progress;
   private JLabel statusLabel;
   private JButton btnSave;
   private JCheckBox chkSaveSql;
   private JCheckBox chkSaveJson;
   private JCheckBox chkSaveImg;
   private JCheckBox chkSaveCurGif;
   private JCheckBox chkSaveAllGif;
   private JCheckBox chkSaveNpc;
   private boolean saved = false;
   private SwingWorker<Void, Integer> saveWorker;

   public PartSaveDialog(Frame owner, List<mPart> parts, PartCanvas canvas) {
      super(owner, true);
      this.parts = parts;
      this.canvas = canvas;
      this.setUndecorated(true);
      this.setSize(900, 640);
      this.setLocationRelativeTo(owner);
      this.getContentPane().setBackground(BG);
      this.setLayout(new BorderLayout());
      JPanel root = new JPanel(new BorderLayout());
      root.setBackground(BG);
      root.setBorder(BorderFactory.createLineBorder(BORDER, 2));
      root.add(this.buildTitleBar(), "North");
      root.add(this.buildBody(), "Center");
      root.add(this.buildFooter(), "South");
      this.add(root);
      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            PartSaveDialog.this.doCancel();
         }
      });
      this.refreshPreview();
   }

   private JPanel buildTitleBar() {
      JPanel bar = new JPanel(new BorderLayout());
      bar.setBackground(BG2);
      bar.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      bar.setPreferredSize(new Dimension(0, 44));
      JLabel title = new JLabel("  " + Lang.get("part_save_title"));
      title.setFont(F_BOLD);
      title.setForeground(ACCENT);
      bar.add(title, "West");
      JLabel hint = new JLabel(Lang.get("part_save_subtitle") + "  ");
      hint.setFont(F_TINY);
      hint.setForeground(TEXTDIM);
      bar.add(hint, "East");
      final Point[] drag = new Point[]{null};
      bar.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            drag[0] = e.getPoint();
         }
      });
      bar.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (drag[0] != null) {
               Point loc = PartSaveDialog.this.getLocation();
               PartSaveDialog.this.setLocation(loc.x + e.getX() - drag[0].x, loc.y + e.getY() - drag[0].y);
            }
         }
      });
      return bar;
   }

   private JPanel buildBody() {
      JPanel body = new JPanel(new BorderLayout());
      body.setBackground(BG);
      body.add(this.buildConfigPanel(), "North");
      body.add(this.buildPreviewPanel(), "Center");
      return body;
   }

   private JPanel buildConfigPanel() {
      JPanel p = new JPanel(new GridBagLayout());
      p.setBackground(BG2);
      p.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, BORDER), new EmptyBorder(12, 18, 12, 18)));
      GridBagConstraints c = new GridBagConstraints();
      c.insets = new Insets(4, 5, 4, 5);
      c.fill = 2;
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 0.0;
      p.add(dimLbl(Lang.get("output_folder")), c);
      String defaultDir = System.getProperty("user.dir");
      this.txtOutputDir = styledField(defaultDir != null ? defaultDir : "");
      this.txtOutputDir.putClientProperty("JTextField.placeholderText", Lang.get("output_folder_placeholder"));
      c.gridx = 1;
      c.weightx = 1.0;
      c.gridwidth = 3;
      p.add(this.txtOutputDir, c);
      c.gridwidth = 1;
      JButton btnBrowse = accentBtn(Lang.get("btn_browse"), ACCENT);
      btnBrowse.addActionListener(e -> this.browseOutputDir());
      c.gridx = 4;
      c.weightx = 0.0;
      p.add(btnBrowse, c);
      c.gridx = 0;
      c.gridy = 1;
      c.weightx = 0.0;
      p.add(dimLbl(Lang.get("start_part_id")), c);
      this.spinStartPartId = makeSpin(1, 99999, 1000);
      c.gridx = 1;
      c.weightx = 0.3;
      p.add(this.spinStartPartId, c);
      c.gridx = 2;
      c.weightx = 0.0;
      p.add(dimLbl(Lang.get("start_img_id")), c);
      this.spinStartImgId = makeSpin(1, 99999, 10000);
      c.gridx = 3;
      c.weightx = 0.3;
      p.add(this.spinStartImgId, c);
      JButton btnRefresh = accentBtn(Lang.get("btn_preview"), new Color(50, 50, 70));
      btnRefresh.addActionListener(e -> this.refreshPreview());
      c.gridx = 4;
      c.weightx = 0.0;
      p.add(btnRefresh, c);
      c.gridx = 0;
      c.gridy = 2;
      c.gridwidth = 5;
      JPanel opts = new JPanel(new GridLayout(2, 3, 10, 4));
      opts.setOpaque(false);
      opts.setBorder(
         BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 68)), "CHỌN ĐỊNH DẠNG LƯU DỮ LIỆU", 0, 0, new Font("Segoe UI", 1, 9), ACCENT
         )
      );
      this.chkSaveSql = styledCheck("Script SQL (parts.sql)", true);
      this.chkSaveJson = styledCheck("File JSON (parts.json)", true);
      this.chkSaveImg = styledCheck("Ảnh SmallImage x1..x4", true);
      this.chkSaveCurGif = styledCheck("Animated GIF (Hành động hiện tại)", true);
      this.chkSaveAllGif = styledCheck("Animated GIF (Tất cả 6 hành động)", false);
      this.chkSaveNpc = styledCheck("Lưu NPC Sprite (Frame 0 & 1)", false);
      opts.add(this.chkSaveSql);
      opts.add(this.chkSaveJson);
      opts.add(this.chkSaveImg);
      opts.add(this.chkSaveCurGif);
      opts.add(this.chkSaveAllGif);
      opts.add(this.chkSaveNpc);
      p.add(opts, c);
      return p;
   }

   private JPanel buildPreviewPanel() {
      JPanel p = new JPanel(new BorderLayout());
      p.setBackground(BG);
      JLabel hdr = new JLabel("  " + Lang.get("rename_preview"));
      hdr.setFont(F_BOLD);
      hdr.setForeground(TEXTDIM);
      hdr.setBorder(new EmptyBorder(8, 14, 4, 0));
      p.add(hdr, "North");
      String[] cols = new String[]{
         Lang.get("col_part"),
         Lang.get("col_type"),
         Lang.get("col_old_part"),
         Lang.get("col_new_part"),
         Lang.get("col_frames"),
         Lang.get("col_old_img"),
         Lang.get("col_new_img"),
         Lang.get("col_status")
      };
      this.tableModel = new PartSaveDialog.PartPreviewTableModel(cols);
      this.previewTable = new JTable(this.tableModel) {
         @Override
         public Component prepareRenderer(TableCellRenderer r, int row, int col) {
            Component c = super.prepareRenderer(r, row, col);
            c.setBackground(row % 2 == 0 ? PartSaveDialog.BG3 : PartSaveDialog.BG);
            c.setForeground(col == 7 ? PartSaveDialog.SUCCESS : PartSaveDialog.TEXT);
            if (col == 2 || col == 4) {
               c.setForeground(PartSaveDialog.TEXTDIM);
            }

            if (col == 3 || col == 6) {
               c.setForeground(new Color(120, 200, 120));
            }

            if (this.isRowSelected(row)) {
               c.setBackground(new Color(60, 40, 120));
               c.setForeground(Color.WHITE);
            }

            if (c instanceof JLabel l) {
               l.setFont(PartSaveDialog.F_MONO);
               l.setBorder(new EmptyBorder(3, 8, 3, 8));
            }

            return c;
         }
      };
      this.previewTable.setBackground(BG3);
      this.previewTable.setForeground(TEXT);
      this.previewTable.setGridColor(BORDER);
      this.previewTable.setFont(F_MONO);
      this.previewTable.setRowHeight(24);
      this.previewTable.setShowVerticalLines(false);
      this.previewTable.getTableHeader().setBackground(BG2);
      this.previewTable.getTableHeader().setForeground(TEXTDIM);
      this.previewTable.getTableHeader().setFont(F_TINY);
      this.previewTable.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      this.previewTable.setSelectionBackground(new Color(60, 40, 120));
      JScrollPane scroll = new JScrollPane(this.previewTable);
      scroll.setBorder(null);
      scroll.setBackground(BG);
      scroll.getViewport().setBackground(BG);
      scroll.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      p.add(scroll, "Center");
      return p;
   }

   private JPanel buildFooter() {
      JPanel footer = new JPanel(new BorderLayout(10, 0));
      footer.setBackground(BG2);
      footer.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, BORDER), new EmptyBorder(10, 18, 10, 18)));
      this.statusLabel = new JLabel(Lang.get("status_ready_part"));
      this.statusLabel.setFont(F_TINY);
      this.statusLabel.setForeground(TEXTDIM);
      footer.add(this.statusLabel, "West");
      this.progress = new JProgressBar(0, 100);
      this.progress.setStringPainted(false);
      this.progress.setBackground(BG3);
      this.progress.setForeground(ACCENT);
      this.progress.setBorder(null);
      this.progress.setPreferredSize(new Dimension(0, 4));
      this.progress.setValue(0);
      footer.add(this.progress, "Center");
      JPanel btns = new JPanel(new FlowLayout(2, 8, 0));
      btns.setOpaque(false);
      JButton btnCancel = accentBtn(Lang.get("btn_cancel"), new Color(50, 50, 65));
      btnCancel.addActionListener(e -> this.doCancel());
      this.btnSave = accentBtn(Lang.get("btn_save"), SUCCESS);
      this.btnSave.setFont(F_BOLD);
      this.btnSave.setPreferredSize(new Dimension(140, 34));
      this.btnSave.addActionListener(e -> this.doSave());
      btns.add(btnCancel);
      btns.add(this.btnSave);
      footer.add(btns, "East");
      return footer;
   }

   private void browseOutputDir() {
      String chosen = SmartFolderChooser.showChoose(this, this.txtOutputDir.getText().trim());
      if (chosen != null) {
         this.txtOutputDir.setText(chosen);
      }
   }

   private void refreshPreview() {
      this.tableModel.clearRows();
      if (this.parts != null && !this.parts.isEmpty()) {
         int startPartId = (Integer)this.spinStartPartId.getValue();
         int startImgId = (Integer)this.spinStartImgId.getValue();
         int currentImgId = startImgId;
         Map<Integer, Integer> oldToNew = new HashMap<>();

         for (int i = 0; i < this.parts.size(); i++) {
            mPart p = this.parts.get(i);
            int newPartId = startPartId + i;
            int oldImgStart = p.pi != null && p.pi.length > 0 && p.pi[0] != null ? p.pi[0].id : 0;
            int partImgStart = -1;
            int frames = p.pi != null ? p.pi.length : 0;

            for (int j = 0; j < frames; j++) {
               PartImage pi = p.pi[j];
               if (pi != null && pi.id > 0) {
                  if (!oldToNew.containsKey(Integer.valueOf(pi.id))) {
                     oldToNew.put(Integer.valueOf(pi.id), currentImgId);
                     if (partImgStart == -1) {
                        partImgStart = currentImgId;
                     }

                     currentImgId++;
                  } else if (partImgStart == -1) {
                     partImgStart = oldToNew.get(Integer.valueOf(pi.id));
                  }
               }
            }

            if (partImgStart == -1) {
               partImgStart = currentImgId;
            }

            this.tableModel.addRow(new Object[]{getTypeName(p.type), p.type, p.id, newPartId, frames, oldImgStart, partImgStart, Lang.get("status_ready_row")});
         }

         this.tableModel.fireTableDataChanged();
      }
   }

   private void doCancel() {
      if (this.saveWorker != null && !this.saveWorker.isDone()) {
         this.saveWorker.cancel(true);
      }

      this.dispose();
   }

   private static void deleteFolder(File folder) {
      if (folder != null && folder.exists()) {
         File[] files = folder.listFiles();
         if (files != null) {
            for (File f : files) {
               if (f.isDirectory()) {
                  deleteFolder(f);
               } else {
                  f.delete();
               }
            }
         }

         folder.delete();
      }
   }

   private void doSave() {
      String outDirStr = this.txtOutputDir.getText().trim();
      if (outDirStr.isEmpty()) {
         this.setStatus(Lang.get("err_select_folder"), WARN);
      } else {
         final File outRoot = new File(outDirStr);
         if (!outRoot.exists() && !outRoot.mkdirs()) {
            this.setStatus(Lang.get("err_create_folder") + outDirStr, ERROR);
         } else {
            this.btnSave.setEnabled(false);
            this.setStatus(Lang.get("status_saving"), TEXTDIM);
            this.progress.setValue(0);
            final boolean saveSql = this.chkSaveSql.isSelected();
            final boolean saveJson = this.chkSaveJson.isSelected();
            final boolean saveImg = this.chkSaveImg.isSelected();
            final boolean saveCurGif = this.chkSaveCurGif.isSelected();
            final boolean saveAllGif = this.chkSaveAllGif.isSelected();
            final boolean saveNpc = this.chkSaveNpc.isSelected();
            this.saveWorker = new SwingWorker<Void, Integer>() {
               protected Void doInBackground() throws Exception {
                  int startPartId = (Integer)PartSaveDialog.this.spinStartPartId.getValue();
                  int startImgId = (Integer)PartSaveDialog.this.spinStartImgId.getValue();
                  String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                  File exportFolder = PartSaveDialog.makeUniqueFolder(outRoot, "parts_export_" + ts);
                  File imgDir = new File(exportFolder, "img");
                  File gifDir = new File(exportFolder, "gif");
                  StringBuilder sqlAll = new StringBuilder();
                  sqlAll.append("-- HTTH Part Export — ").append(ts).append("\n");
                  sqlAll.append("-- Generated by HaiTacTiHonTool\n\n");
                  JSONObject jsonRoot = new JSONObject();
                  JSONArray jsonRecords = new JSONArray();
                  int currentImgId = startImgId;
                  int total = PartSaveDialog.this.parts.size();
                  Map<Integer, Integer> oldToNewImgIdMap = new HashMap<>();

                  try {
                     for (int i = 0; i < total; i++) {
                        if (this.isCancelled()) {
                           PartSaveDialog.deleteFolder(exportFolder);
                           return null;
                        }

                        mPart p = PartSaveDialog.this.parts.get(i);
                        int newPartId = startPartId + i;
                        int frames = p.pi != null ? p.pi.length : 0;
                        JSONArray dataArr = new JSONArray();

                        for (int j = 0; j < frames; j++) {
                           if (this.isCancelled()) {
                              PartSaveDialog.deleteFolder(exportFolder);
                              return null;
                           }

                           PartImage pi = p.pi[j];
                           int mappedImgId = 0;
                           if (pi != null && pi.id > 0) {
                              int oldId = pi.id;
                              if (oldToNewImgIdMap.containsKey(oldId)) {
                                 mappedImgId = oldToNewImgIdMap.get(oldId);
                              } else {
                                 mappedImgId = currentImgId;
                                 oldToNewImgIdMap.put(oldId, currentImgId);
                                 currentImgId++;
                                 int resolvedNewId = mappedImgId;
                                 if (mappedImgId > 10000) {
                                    resolvedNewId = mappedImgId + 16000;
                                 } else if (mappedImgId > 0) {
                                    resolvedNewId = mappedImgId + 10000;
                                 }

                                 if (saveImg) {
                                    String srcBaseDir = AppConfig.getResolvedPath("path_part_img", "Data/Part/img/");
                                    if (!srcBaseDir.endsWith("/")) {
                                       srcBaseDir = srcBaseDir + "/";
                                    }

                                    File src = PartSaveDialog.findSourceImage(srcBaseDir, oldId);
                                    if (src != null) {
                                       ImageZoomHelper.saveImageWithZoomLevels(src, imgDir.getAbsolutePath(), resolvedNewId + ".png");
                                    } else {
                                       BufferedImage cached = PartSaveDialog.this.canvas != null ? PartSaveDialog.this.canvas.getImage((short)oldId) : null;
                                       if (cached != null) {
                                          ImageZoomHelper.saveImageWithZoomLevels(cached, imgDir.getAbsolutePath(), resolvedNewId + ".png");
                                       }
                                    }
                                 }
                              }
                           }

                           JSONArray piArr = new JSONArray();
                           piArr.put(mappedImgId);
                           piArr.put(pi != null ? pi.dx : 0);
                           piArr.put(pi != null ? pi.dy : 0);
                           dataArr.put(piArr);
                        }

                        if (saveSql) {
                           String dataJson = dataArr.toString();
                           String sqlLine = String.format(
                              "INSERT INTO `parts` (`id`, `type`, `data`) VALUES (%d, %d, '%s');\n", newPartId, p.type, dataJson.replace("'", "''")
                           );
                           sqlAll.append(sqlLine);
                        }

                        if (saveJson) {
                           JSONObject partObj = new JSONObject();
                           partObj.put("id", newPartId);
                           partObj.put("type", p.type);
                           partObj.put("data", dataArr.toString());
                           jsonRecords.put(partObj);
                        }

                        this.publish((i + 1) * 80 / total);
                     }

                     if (this.isCancelled()) {
                        PartSaveDialog.deleteFolder(exportFolder);
                        return null;
                     } else {
                        if (saveSql) {
                           String sqlFileName = "parts_export_" + ts + ".sql";
                           Files.write(new File(exportFolder, sqlFileName).toPath(), sqlAll.toString().getBytes(StandardCharsets.UTF_8));
                        }

                        if (saveJson) {
                           jsonRoot.put("RECORDS", jsonRecords);
                           Files.write(new File(exportFolder, "parts.json").toPath(), jsonRoot.toString(2).getBytes(StandardCharsets.UTF_8));
                        }

                        if (saveNpc) {
                           try {
                              BufferedImage npcSprite = PartSaveDialog.this.canvas.exportNpcSprite();
                              if (npcSprite != null) {
                                 File npcFile = new File(exportFolder, "npc_sprite.png");
                                 ImageIO.write(npcSprite, "PNG", npcFile);
                              }
                           } catch (Exception var26) {
                              System.err.println("Failed to export NPC sprite: " + var26.getMessage());
                           }
                        }

                        if (PartSaveDialog.this.canvas != null && (saveCurGif || saveAllGif)) {
                           gifDir.mkdirs();
                           short[] fashion = PartSaveDialog.this.canvas.getWearing();
                           int dir = PartSaveDialog.this.canvas.getDirection();
                           int curAct = PartSaveDialog.this.canvas.getStatusMe();
                           if (saveCurGif) {
                              String actName = GifExportHelper.getActionName(Math.max(0, Math.min(curAct, 5)));
                              File gf = new File(gifDir, "char_" + actName + ".gif");
                              GifExportHelper.exportPartActionToGif(fashion, PartSaveDialog.this.parts, curAct, dir, gf, 80, 1.0, true, null);
                           }

                           if (saveAllGif) {
                              for (int a = 0; a < 6; a++) {
                                 String actName = GifExportHelper.getActionName(a);
                                 File gf = new File(gifDir, "char_" + actName + ".gif");
                                 GifExportHelper.exportPartActionToGif(fashion, PartSaveDialog.this.parts, a, dir, gf, 80, 1.0, true, null);
                              }
                           }
                        }

                        this.publish(100);
                        return null;
                     }
                  } catch (Exception var27) {
                     PartSaveDialog.deleteFolder(exportFolder);
                     throw var27;
                  }
               }

               @Override
               protected void process(List<Integer> chunks) {
                  int val = chunks.get(chunks.size() - 1);
                  PartSaveDialog.this.progress.setValue(val);
                  PartSaveDialog.this.setStatus(Lang.get("status_saving_percent") + val + "%", PartSaveDialog.TEXTDIM);
               }

               @Override
               protected void done() {
                  if (this.isCancelled()) {
                     PartSaveDialog.this.setStatus(Lang.get("status_cancel"), PartSaveDialog.WARN);
                     PartSaveDialog.this.btnSave.setEnabled(true);
                     PartSaveDialog.this.btnSave.setText(Lang.get("btn_save"));
                  } else {
                     try {
                        this.get();
                        PartSaveDialog.this.saved = true;
                        PartSaveDialog.this.progress.setValue(100);
                        PartSaveDialog.this.setStatus("Đã lưu dữ liệu Part thành công!", PartSaveDialog.SUCCESS);
                        PartSaveDialog.this.btnSave.setText("ĐÃ LƯU XONG");
                     } catch (Exception var2) {
                        PartSaveDialog.this.setStatus(Lang.get("status_error_prefix") + var2.getMessage(), PartSaveDialog.ERROR);
                        PartSaveDialog.this.btnSave.setEnabled(true);
                     }
                  }
               }
            };
            this.saveWorker.execute();
         }
      }
   }

   private static File findSourceImage(String baseDir, int oldId) {
      int phys = oldId;
      if (oldId > 10000) {
         phys = oldId + 16000;
      } else if (oldId > 0) {
         phys = oldId + 10000;
      }

      File f = new File(baseDir + "x4/" + phys + ".png");
      if (f.exists()) {
         return f;
      } else {
         f = new File(baseDir + phys + ".png");
         if (f.exists()) {
            return f;
         } else {
            f = new File(baseDir + "x4/" + oldId + ".png");
            if (f.exists()) {
               return f;
            } else {
               f = new File(baseDir + oldId + ".png");
               return f.exists() ? f : null;
            }
         }
      }
   }

   private static File makeUniqueFolder(File root, String base) {
      File dir = new File(root, base);
      if (!dir.exists()) {
         dir.mkdirs();
         return dir;
      } else {
         Random rnd = new Random();
         int attempts = 0;

         File candidate;
         do {
            candidate = new File(root, base + "_" + String.format("%03d", rnd.nextInt(1000)));
         } while (candidate.exists() && ++attempts < 1000);

         candidate.mkdirs();
         return candidate;
      }
   }

   private static String getTypeName(int type) {
      return switch (type) {
         case 0 -> "Head (Đầu - 0)";
         case 1 -> "Body (Thân - 1)";
         case 2 -> "Leg (Chân - 2)";
         case 3 -> "Weapon (Vũ khí - 3)";
         case 4 -> "Hat (Nón - 4)";
         case 5 -> "Cloak/Hair (Choàng/Tóc - 5)";
         default -> "Type " + type;
      };
   }

   private void setStatus(String text, Color color) {
      SwingUtilities.invokeLater(() -> {
         this.statusLabel.setText(text);
         this.statusLabel.setForeground(color);
      });
   }

   public boolean isSaved() {
      return this.saved;
   }

   private static JLabel dimLbl(String t) {
      JLabel l = new JLabel(t);
      l.setFont(F_TINY);
      l.setForeground(TEXTDIM);
      return l;
   }

   private static JSpinner makeSpin(int min, int max, int defVal) {
      JSpinner s = new JSpinner(new SpinnerNumberModel(defVal, min, max, 1));
      s.setBackground(BG3);
      DefaultEditor ed = (DefaultEditor)s.getEditor();
      ed.getTextField().setBackground(BG3);
      ed.getTextField().setForeground(TEXT);
      ed.getTextField().setFont(F_MONO);
      ed.getTextField().setBorder(new EmptyBorder(2, 6, 2, 6));
      s.setBorder(BorderFactory.createLineBorder(BORDER));
      return s;
   }

   private static JTextField styledField(String text) {
      JTextField f = new JTextField(text);
      f.setBackground(BG3);
      f.setForeground(TEXT);
      f.setCaretColor(ACCENT);
      f.setFont(F_MONO);
      f.setBorder(new CompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(4, 10, 4, 10)));
      return f;
   }

   private static JCheckBox styledCheck(String text, boolean sel) {
      JCheckBox c = new JCheckBox(text, sel);
      c.setFont(F_TINY);
      c.setForeground(TEXT);
      c.setOpaque(false);
      c.setFocusPainted(false);
      c.setCursor(new Cursor(12));
      return c;
   }

   private static JButton accentBtn(String text, final Color bg) {
      final JButton b = new JButton(text);
      b.setFont(F_BOLD);
      b.setForeground(Color.WHITE);
      b.setBackground(bg);
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)), new EmptyBorder(6, 14, 6, 14)));
      b.setCursor(new Cursor(12));
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

   private static class PartPreviewTableModel extends DefaultTableModel {
      PartPreviewTableModel(String[] cols) {
         super(cols, 0);
      }

      @Override
      public boolean isCellEditable(int r, int c) {
         return false;
      }

      void clearRows() {
         this.setRowCount(0);
      }
   }
}
