package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.ImageZoomHelper;
import com.deplor.haitactihontool.config.Lang;
import com.deplor.haitactihontool.ui.components.SmartFolderChooser;
import com.deplor.haitactihontool.util.GifExportHelper;
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
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EffectSaveDialog extends JDialog {
   private static final Color BG = new Color(14, 14, 20);
   private static final Color BG2 = new Color(20, 20, 28);
   private static final Color BG3 = new Color(24, 24, 34);
   private static final Color ACCENT = new Color(0, 110, 235);
   private static final Color SUCCESS = new Color(30, 160, 80);
   private static final Color WARN = new Color(200, 140, 0);
   private static final Color ERROR = new Color(200, 50, 50);
   private static final Color TEXT = new Color(200, 200, 215);
   private static final Color TEXTDIM = new Color(130, 130, 150);
   private static final Color BORDER = new Color(36, 36, 52);
   private static final Font F_BOLD = new Font("Segoe UI", 1, 12);
   private static final Font F_TINY = new Font("Segoe UI", 0, 10);
   private static final Font F_MONO = new Font("Consolas", 0, 11);
   private final EffectModel model;
   private final EffectWriter writer;
   private JTextField txtOutputDir;
   private JSpinner spinNewId;
   private JCheckBox chkSaveData;
   private JComboBox<String> cbBinaryFormat;
   private JCheckBox chkSaveImg;
   private JCheckBox chkSaveGif;
   private JCheckBox chkSaveSql;
   private JCheckBox chkSaveJson;
   private JTextArea txtPreview;
   private JProgressBar progress;
   private JLabel statusLabel;
   private JButton btnSave;
   private boolean saved = false;
   private String outputPath = null;
   private SwingWorker<String, Void> saveWorker;

   public EffectSaveDialog(Frame owner, EffectModel model, String defaultDataDir) {
      super(owner, true);
      this.model = model;
      this.writer = new EffectWriter();
      this.setUndecorated(true);
      this.setSize(700, 530);
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
      this.updatePreview();
   }

   private JPanel buildTitleBar() {
      JPanel bar = new JPanel(new BorderLayout());
      bar.setBackground(BG2);
      bar.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      bar.setPreferredSize(new Dimension(0, 44));
      JLabel title = new JLabel("  " + Lang.get("effect_save_title"));
      title.setFont(F_BOLD);
      title.setForeground(ACCENT);
      bar.add(title, "West");
      JLabel sub = new JLabel(Lang.get("effect_save_subtitle") + "  ");
      sub.setFont(F_TINY);
      sub.setForeground(TEXTDIM);
      bar.add(sub, "East");
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
               Point loc = EffectSaveDialog.this.getLocation();
               EffectSaveDialog.this.setLocation(loc.x + e.getX() - drag[0].x, loc.y + e.getY() - drag[0].y);
            }
         }
      });
      return bar;
   }

   private JPanel buildBody() {
      JPanel body = new JPanel(new BorderLayout(0, 0));
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
      p.add(dimLbl(Lang.get("current_id")), c);
      JLabel lblCurrentId = new JLabel(String.valueOf(this.model != null ? this.model.id : "?"));
      lblCurrentId.setFont(F_MONO);
      lblCurrentId.setForeground(TEXTDIM);
      c.gridx = 1;
      c.weightx = 0.0;
      p.add(lblCurrentId, c);
      c.gridx = 2;
      c.weightx = 0.0;
      p.add(dimLbl(Lang.get("new_id")), c);
      this.spinNewId = makeSpin(1, 99999, this.model != null ? this.model.id : 1);
      this.spinNewId.addChangeListener(e -> this.updatePreview());
      c.gridx = 3;
      c.weightx = 0.5;
      p.add(this.spinNewId, c);
      JLabel hint = new JLabel(Lang.get("keep_id_hint"));
      hint.setFont(F_TINY);
      hint.setForeground(TEXTDIM);
      c.gridx = 4;
      c.weightx = 0.5;
      p.add(hint, c);
      c.gridx = 0;
      c.gridy = 1;
      c.weightx = 0.0;
      p.add(dimLbl(Lang.get("output_folder")), c);
      this.txtOutputDir = styledField(AppConfig.getResolvedPath("path_effect", "Data/Effect/"));
      this.txtOutputDir.putClientProperty("JTextField.placeholderText", Lang.get("effect_output_dir_placeholder"));
      this.txtOutputDir.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EffectSaveDialog.this.updatePreview();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EffectSaveDialog.this.updatePreview();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EffectSaveDialog.this.updatePreview();
         }
      });
      c.gridx = 1;
      c.weightx = 1.0;
      c.gridwidth = 3;
      p.add(this.txtOutputDir, c);
      c.gridwidth = 1;
      JButton btnBrowse = accentBtn(Lang.get("btn_browse"), ACCENT);
      btnBrowse.addActionListener(e -> {
         String chosen = SmartFolderChooser.showChoose(this, this.txtOutputDir.getText().trim());
         if (chosen != null) {
            this.txtOutputDir.setText(chosen);
            this.updatePreview();
         }
      });
      c.gridx = 4;
      c.weightx = 0.0;
      p.add(btnBrowse, c);
      c.gridx = 0;
      c.gridy = 2;
      c.gridwidth = 5;
      JPanel opts = new JPanel(new GridLayout(2, 3, 10, 6));
      opts.setOpaque(false);
      opts.setBorder(
         BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 68)), "CHỌN ĐỊNH DẠNG LƯU DỮ LIỆU", 0, 0, new Font("Segoe UI", 1, 9), ACCENT
         )
      );
      this.chkSaveData = styledCheck("Binary Game:", true);
      this.cbBinaryFormat = new JComboBox<>(new String[]{"Có góc (Mới - Marker 128)", "Không góc (Cũ - Legacy)", "Xuất cả 2 loại (Mới & Cũ)"});
      this.cbBinaryFormat.setFont(new Font("Segoe UI", 1, 10));
      this.cbBinaryFormat.setBackground(new Color(30, 30, 42));
      this.cbBinaryFormat.setForeground(new Color(220, 220, 240));
      this.cbBinaryFormat.addActionListener(e -> this.updatePreview());
      JPanel dataChoicePnl = new JPanel(new BorderLayout(4, 0));
      dataChoicePnl.setOpaque(false);
      dataChoicePnl.add(this.chkSaveData, "West");
      dataChoicePnl.add(this.cbBinaryFormat, "Center");
      this.chkSaveImg = styledCheck("Ảnh Atlas PNG (img/{id}.png)", true);
      this.chkSaveGif = styledCheck("Hoạt họa Animated GIF ({id}.gif)", true);
      this.chkSaveSql = styledCheck("Script SQL (sql/{id}.sql)", true);
      this.chkSaveJson = styledCheck("Cấu trúc JSON (json/{id}.json)", true);
      this.chkSaveData.addActionListener(e -> {
         this.cbBinaryFormat.setEnabled(this.chkSaveData.isSelected());
         this.updatePreview();
      });

      for (JCheckBox chk : new JCheckBox[]{this.chkSaveImg, this.chkSaveGif, this.chkSaveSql, this.chkSaveJson}) {
         chk.addActionListener(e -> this.updatePreview());
      }

      opts.add(dataChoicePnl);
      opts.add(this.chkSaveImg);
      opts.add(this.chkSaveGif);
      opts.add(this.chkSaveSql);
      opts.add(this.chkSaveJson);
      p.add(opts, c);
      return p;
   }

   private JPanel buildPreviewPanel() {
      JPanel p = new JPanel(new BorderLayout());
      p.setBackground(BG);
      JLabel hdr = new JLabel("  " + Lang.get("output_preview"));
      hdr.setFont(F_BOLD);
      hdr.setForeground(TEXTDIM);
      hdr.setBorder(new EmptyBorder(8, 14, 4, 0));
      p.add(hdr, "North");
      this.txtPreview = new JTextArea();
      this.txtPreview.setEditable(false);
      this.txtPreview.setBackground(BG3);
      this.txtPreview.setForeground(new Color(100, 220, 140));
      this.txtPreview.setFont(F_MONO);
      this.txtPreview.setBorder(new EmptyBorder(10, 16, 10, 16));
      JScrollPane scroll = new JScrollPane(this.txtPreview);
      scroll.setBorder(null);
      scroll.setBackground(BG3);
      scroll.getViewport().setBackground(BG3);
      p.add(scroll, "Center");
      return p;
   }

   private JPanel buildFooter() {
      JPanel footer = new JPanel(new BorderLayout(10, 0));
      footer.setBackground(BG2);
      footer.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, BORDER), new EmptyBorder(10, 18, 10, 18)));
      this.statusLabel = new JLabel(Lang.get("status_ready_simple"));
      this.statusLabel.setFont(F_TINY);
      this.statusLabel.setForeground(TEXTDIM);
      footer.add(this.statusLabel, "West");
      this.progress = new JProgressBar(0, 100);
      this.progress.setBackground(BG3);
      this.progress.setForeground(ACCENT);
      this.progress.setBorder(null);
      this.progress.setPreferredSize(new Dimension(0, 4));
      footer.add(this.progress, "Center");
      JPanel btns = new JPanel(new FlowLayout(2, 8, 0));
      btns.setOpaque(false);
      JButton btnCancel = accentBtn(Lang.get("btn_cancel"), new Color(50, 50, 65));
      btnCancel.addActionListener(e -> this.doCancel());
      this.btnSave = accentBtn(Lang.get("btn_save"), SUCCESS);
      this.btnSave.addActionListener(e -> this.doSave());
      btns.add(btnCancel);
      btns.add(this.btnSave);
      footer.add(btns, "East");
      return footer;
   }

   private void updatePreview() {
      if (this.txtPreview != null) {
         int newId = this.getNewId();
         String outDir = this.txtOutputDir != null ? this.txtOutputDir.getText().trim() : "...";
         String ts = "yyyyMMdd_HHmmss";
         String folderName = "effect_" + newId + "_" + ts + "/";
         StringBuilder sb = new StringBuilder();
         sb.append(outDir).append("/").append(folderName).append("\n");
         if (this.chkSaveData != null && this.chkSaveData.isSelected()) {
            int binMode = this.cbBinaryFormat != null ? this.cbBinaryFormat.getSelectedIndex() : 0;
            if (binMode == 0) {
               sb.append("  ├── data/\n");
               sb.append("  │     └── ").append(newId).append("   (Binary Game Format - Có góc xoay)\n");
            } else if (binMode == 1) {
               sb.append("  ├── data/\n");
               sb.append("  │     └── ").append(newId).append("   (Binary Game Format - Chuẩn cũ không góc)\n");
            } else {
               sb.append("  ├── data/\n");
               sb.append("  │     └── ").append(newId).append("   (Binary Game Format - Có góc xoay)\n");
               sb.append("  ├── data_legacy/\n");
               sb.append("  │     └── ").append(newId).append("   (Binary Game Format - Chuẩn cũ không góc)\n");
            }
         }

         if (this.chkSaveImg != null && this.chkSaveImg.isSelected()) {
            sb.append("  ├── img/\n");
            sb.append("  │     ├── x4/").append(newId).append(".png   (100%)\n");
            sb.append("  │     ├── x3/").append(newId).append(".png   (75%)\n");
            sb.append("  │     ├── x2/").append(newId).append(".png   (50%)\n");
            sb.append("  │     └── x1/").append(newId).append(".png   (25%)\n");
         }

         if (this.chkSaveGif != null && this.chkSaveGif.isSelected()) {
            sb.append("  ├── gif/\n");
            sb.append("  │     └── ").append(newId).append(".gif   (Animated GIF theo sequence)\n");
         }

         if (this.chkSaveSql != null && this.chkSaveSql.isSelected()) {
            sb.append("  ├── sql/\n");
            sb.append("  │     └── effect_").append(newId).append(".sql   (MySQL Insert Script - Chuẩn Server)\n");
         }

         if (this.chkSaveJson != null && this.chkSaveJson.isSelected()) {
            sb.append("  └── json/\n");
            sb.append("        └── effect_").append(newId).append(".json  (Structured JSON Data)\n");
         }

         if (this.model != null) {
            sb.append("\n— ")
               .append(Lang.get("current_id"))
               .append(" ")
               .append(this.model.id)
               .append("  →  ")
               .append(Lang.get("new_id"))
               .append(" ")
               .append(newId)
               .append("\n");
            sb.append("  ").append(Lang.get("effect_preview_frames")).append(this.model.frames != null ? this.model.frames.length : 0).append("\n");
            sb.append("  ").append("Sequence Steps: ").append(this.model.getSeqLen()).append("\n");
            boolean hasImg = this.model.atlasImage != null;
            sb.append("  ")
               .append(Lang.get("effect_atlas_lbl"))
               .append(hasImg ? Lang.get("effect_preview_available") : Lang.get("effect_preview_no_image"))
               .append("\n");
         }

         this.txtPreview.setText(sb.toString());
         this.txtPreview.setCaretPosition(0);
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
      if (this.model == null) {
         this.setStatus(Lang.get("err_no_effect_data"), WARN);
      } else {
         String outDirStr = this.txtOutputDir.getText().trim();
         if (outDirStr.isEmpty()) {
            this.setStatus(Lang.get("err_select_folder"), WARN);
         } else {
            final File outRoot = new File(outDirStr);
            if (!outRoot.exists() && !outRoot.mkdirs()) {
               this.setStatus(Lang.get("err_create_folder"), ERROR);
            } else {
               final int newId = this.getNewId();
               this.model.id = newId;
               this.btnSave.setEnabled(false);
               this.setStatus(Lang.get("status_saving"), TEXTDIM);
               this.progress.setValue(20);
               this.saveWorker = new SwingWorker<String, Void>() {
                  protected String doInBackground() throws Exception {
                     String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                     File subFolder = EffectSaveDialog.makeUniqueFolder(outRoot, "effect_" + newId + "_" + ts);

                     try {
                        if (this.isCancelled()) {
                           EffectSaveDialog.deleteFolder(subFolder);
                           return null;
                        }

                        if (EffectSaveDialog.this.chkSaveData.isSelected()) {
                           int binMode = EffectSaveDialog.this.cbBinaryFormat != null ? EffectSaveDialog.this.cbBinaryFormat.getSelectedIndex() : 0;
                           if (binMode == 0) {
                              File dataDir = new File(subFolder, "data");
                              dataDir.mkdirs();
                              File dataFile = new File(dataDir, String.valueOf(newId));
                              EffectSaveDialog.this.writer.write(EffectSaveDialog.this.model, dataFile, true);
                           } else if (binMode == 1) {
                              File dataDir = new File(subFolder, "data");
                              dataDir.mkdirs();
                              File dataFile = new File(dataDir, String.valueOf(newId));
                              EffectSaveDialog.this.writer.write(EffectSaveDialog.this.model, dataFile, false);
                           } else {
                              File dataDir = new File(subFolder, "data");
                              dataDir.mkdirs();
                              File dataFile = new File(dataDir, String.valueOf(newId));
                              EffectSaveDialog.this.writer.write(EffectSaveDialog.this.model, dataFile, true);
                              File legacyDir = new File(subFolder, "data_legacy");
                              legacyDir.mkdirs();
                              File legacyFile = new File(legacyDir, String.valueOf(newId));
                              EffectSaveDialog.this.writer.write(EffectSaveDialog.this.model, legacyFile, false);
                           }
                        }

                        if (EffectSaveDialog.this.chkSaveImg.isSelected() && EffectSaveDialog.this.model.atlasImage != null) {
                           File imgDir = new File(subFolder, "img");
                           ImageZoomHelper.saveImageWithZoomLevels(EffectSaveDialog.this.model.atlasImage, imgDir.getAbsolutePath(), newId + ".png");
                        }

                        if (EffectSaveDialog.this.chkSaveGif.isSelected()) {
                           File gifDir = new File(subFolder, "gif");
                           gifDir.mkdirs();
                           File gifFile = new File(gifDir, newId + ".gif");
                           GifExportHelper.exportEffectSequenceToGif(EffectSaveDialog.this.model, gifFile, 40, 1.0, true, null);
                        }

                        if (EffectSaveDialog.this.chkSaveSql.isSelected()) {
                           String sqlData = EffectSaveDialog.this.writer.generateEffectDataSql(EffectSaveDialog.this.model);
                           File sqlDir = new File(subFolder, "sql");
                           sqlDir.mkdirs();
                           File sqlFile = new File(sqlDir, "effect_" + newId + ".sql");
                           EffectSaveDialog.this.writer.writeText(sqlData, sqlFile);
                        }

                        if (EffectSaveDialog.this.chkSaveJson.isSelected()) {
                           String jsonStr = EffectSaveDialog.this.writer.toJsonObject(EffectSaveDialog.this.model).toString(2);
                           File jsonDir = new File(subFolder, "json");
                           jsonDir.mkdirs();
                           File jsonFile = new File(jsonDir, "effect_" + newId + ".json");
                           EffectSaveDialog.this.writer.writeText(jsonStr, jsonFile);
                        }

                        if (this.isCancelled()) {
                           EffectSaveDialog.deleteFolder(subFolder);
                           return null;
                        }
                     } catch (Exception var8) {
                        EffectSaveDialog.deleteFolder(subFolder);
                        throw var8;
                     }

                     return subFolder.getAbsolutePath();
                  }

                  @Override
                  protected void done() {
                     if (this.isCancelled()) {
                        EffectSaveDialog.this.setStatus(Lang.get("status_cancel"), EffectSaveDialog.WARN);
                        EffectSaveDialog.this.btnSave.setEnabled(true);
                        EffectSaveDialog.this.btnSave.setText(Lang.get("btn_save"));
                     } else {
                        try {
                           EffectSaveDialog.this.outputPath = this.get();
                           EffectSaveDialog.this.saved = true;
                           EffectSaveDialog.this.progress.setValue(100);
                           EffectSaveDialog.this.setStatus("Đã lưu thành công vào: " + EffectSaveDialog.this.outputPath, EffectSaveDialog.SUCCESS);
                           EffectSaveDialog.this.btnSave.setText("ĐÃ LƯU XONG");
                           EffectSaveDialog.this.updatePreview();
                        } catch (Exception var2) {
                           EffectSaveDialog.this.setStatus(Lang.get("status_error_prefix") + var2.getMessage(), EffectSaveDialog.ERROR);
                           EffectSaveDialog.this.btnSave.setEnabled(true);
                        }
                     }
                  }
               };
               this.saveWorker.execute();
            }
         }
      }
   }

   private int getNewId() {
      if (this.spinNewId == null) {
         return this.model != null ? this.model.id : 1;
      } else {
         return (Integer)this.spinNewId.getValue();
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

   private void setStatus(String text, Color color) {
      if (this.statusLabel != null) {
         this.statusLabel.setText(text);
         this.statusLabel.setForeground(color);
      }
   }

   public boolean isSaved() {
      return this.saved;
   }

   public String getOutputPath() {
      return this.outputPath;
   }

   private static JLabel dimLbl(String t) {
      JLabel l = new JLabel(t);
      l.setFont(F_TINY);
      l.setForeground(TEXTDIM);
      return l;
   }

   private static JSpinner makeSpin(int min, int max, int val) {
      JSpinner s = new JSpinner(new SpinnerNumberModel(val, min, max, 1));
      s.setFont(F_MONO);
      s.setBackground(BG3);
      s.setForeground(TEXT);
      s.setBorder(BorderFactory.createLineBorder(BORDER));
      if (s.getEditor() instanceof DefaultEditor de) {
         de.getTextField().setBackground(BG3);
         de.getTextField().setForeground(TEXT);
         de.getTextField().setBorder(null);
      }

      return s;
   }

   private static JTextField styledField(String val) {
      JTextField f = new JTextField(val);
      f.setFont(F_MONO);
      f.setBackground(BG3);
      f.setForeground(TEXT);
      f.setCaretColor(TEXT);
      f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(4, 8, 4, 8)));
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
}
