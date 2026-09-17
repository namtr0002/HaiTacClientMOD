package com.deplor.haitactihontool.util;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.effect.EffectModel;
import com.deplor.haitactihontool.effectauto.EffectAutoModel;
import com.deplor.haitactihontool.part.mPart;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class GifExportDialog extends JDialog {
   private final GifExportDialog.TargetType targetType;
   private EffectModel effectModel;
   private EffectAutoModel effectAutoModel;
   private short[] partFashion;
   private List<mPart> partList;
   private int partCurrentAction = 0;
   private int partDirection = 0;
   private String dataDir;
   private String imgDir;
   private JRadioButton rbSingle;
   private JRadioButton rbAll;
   private JRadioButton rbPartSeparate;
   private JRadioButton rbPartCombo;
   private JSpinner spDelay;
   private JComboBox<String> cbScale;
   private JCheckBox chkTransparent;
   private JTextField txtOutPath;
   private JProgressBar progressBar;
   private JLabel lblStatus;
   private JButton btnExport;
   private JButton btnCancel;

   public GifExportDialog(Frame owner, GifExportDialog.TargetType type, Object targetData, String defaultOutDir) {
      super(owner, "Xuất Bản Hoạt Họa Animated GIF", true);
      this.targetType = type;
      this.dataDir = defaultOutDir;
      if (type == GifExportDialog.TargetType.EFFECT) {
         this.effectModel = (EffectModel)targetData;
      } else if (type == GifExportDialog.TargetType.EFFECT_AUTO) {
         this.effectAutoModel = (EffectAutoModel)targetData;
      }

      this.initUI();
   }

   public static GifExportDialog forPart(Frame owner, short[] fashion, List<mPart> allParts, int currentAction, int direction, String defaultOutDir) {
      GifExportDialog dlg = new GifExportDialog(owner, GifExportDialog.TargetType.PART, null, defaultOutDir);
      dlg.partFashion = fashion;
      dlg.partList = allParts;
      dlg.partCurrentAction = currentAction;
      dlg.partDirection = direction;
      dlg.updateDefaultPath();
      return dlg;
   }

   public void setPaths(String dataDir, String imgDir) {
      this.dataDir = dataDir;
      this.imgDir = imgDir;
   }

   private void initUI() {
      this.setSize(520, 480);
      this.setLocationRelativeTo(this.getOwner());
      this.setResizable(false);
      this.getContentPane().setBackground(new Color(20, 20, 28));
      this.setLayout(new BorderLayout(0, 0));
      JPanel header = new JPanel(new BorderLayout());
      header.setBackground(new Color(28, 28, 38));
      header.setBorder(new EmptyBorder(12, 16, 12, 16));
      String titleStr = "XUẤT BẢN ANIMATED GIF";
      if (this.targetType == GifExportDialog.TargetType.EFFECT) {
         titleStr = titleStr + " - EFFECT #" + (this.effectModel != null ? this.effectModel.id : "");
      } else if (this.targetType == GifExportDialog.TargetType.EFFECT_AUTO) {
         titleStr = titleStr + " - EFFECT AUTO #" + (this.effectAutoModel != null ? this.effectAutoModel.id : "");
      } else if (this.targetType == GifExportDialog.TargetType.PART) {
         titleStr = titleStr + " - NHÂN VẬT (CHARACTER ACTIONS)";
      }

      JLabel title = new JLabel(titleStr);
      title.setFont(new Font("Segoe UI", 1, 13));
      title.setForeground(Theme.ACCENT);
      header.add(title, "West");
      this.add(header, "North");
      JPanel body = new JPanel();
      body.setLayout(new BoxLayout(body, 1));
      body.setOpaque(false);
      body.setBorder(new EmptyBorder(14, 18, 14, 18));
      JPanel scopePnl = createSection("CHẾ ĐỘ XUẤT");
      ButtonGroup bgScope = new ButtonGroup();
      if (this.targetType == GifExportDialog.TargetType.EFFECT) {
         this.rbSingle = new JRadioButton("Xuất Effect hiện tại (theo chuỗi Sequence)", true);
         this.rbAll = new JRadioButton("Xuất TẤT CẢ các Effect trong thư mục Data/Effect", false);
      } else if (this.targetType == GifExportDialog.TargetType.EFFECT_AUTO) {
         this.rbSingle = new JRadioButton("Xuất EffectAuto hiện tại (theo chuỗi Sequence)", true);
         this.rbAll = new JRadioButton("Xuất TẤT CẢ các EffectAuto trong thư mục Data/EffectAuto", false);
      } else {
         this.rbSingle = new JRadioButton("Xuất hành động hiện tại (" + GifExportHelper.getActionDisplayName(this.partCurrentAction) + ")", true);
         this.rbAll = new JRadioButton("Xuất TẤT CẢ các hành động (Stand, Move, Jump, Fall, Attack, Die)", false);
      }

      styleRadio(this.rbSingle);
      styleRadio(this.rbAll);
      bgScope.add(this.rbSingle);
      bgScope.add(this.rbAll);
      scopePnl.add(this.rbSingle);
      scopePnl.add(this.rbAll);
      if (this.targetType == GifExportDialog.TargetType.PART) {
         JPanel partOptions = new JPanel(new FlowLayout(0, 16, 2));
         partOptions.setOpaque(false);
         ButtonGroup bgPart = new ButtonGroup();
         this.rbPartSeparate = new JRadioButton("Tách riêng từng file", true);
         this.rbPartCombo = new JRadioButton("Gộp thành 1 file combo duy nhất", false);
         styleRadio(this.rbPartSeparate);
         styleRadio(this.rbPartCombo);
         bgPart.add(this.rbPartSeparate);
         bgPart.add(this.rbPartCombo);
         partOptions.add(this.rbPartSeparate);
         partOptions.add(this.rbPartCombo);
         this.rbAll.addActionListener(e -> {
            this.rbPartSeparate.setEnabled(this.rbAll.isSelected());
            this.rbPartCombo.setEnabled(this.rbAll.isSelected());
            this.updateDefaultPath();
         });
         this.rbSingle.addActionListener(e -> {
            this.rbPartSeparate.setEnabled(false);
            this.rbPartCombo.setEnabled(false);
            this.updateDefaultPath();
         });
         this.rbPartSeparate.setEnabled(false);
         this.rbPartCombo.setEnabled(false);
         scopePnl.add(partOptions);
      } else {
         this.rbSingle.addActionListener(e -> this.updateDefaultPath());
         this.rbAll.addActionListener(e -> this.updateDefaultPath());
      }

      body.add(scopePnl);
      body.add(Box.createVerticalStrut(10));
      JPanel settingsPnl = createSection("CÀI ĐẶT THÔNG SỐ");
      JPanel grid = new JPanel(new GridLayout(2, 2, 10, 8));
      grid.setOpaque(false);
      JPanel delayPnl = new JPanel(new BorderLayout(4, 0));
      delayPnl.setOpaque(false);
      JLabel lblDelay = new JLabel("Độ trễ (ms / frame):");
      lblDelay.setFont(new Font("Segoe UI", 0, 10));
      lblDelay.setForeground(new Color(180, 180, 200));
      this.spDelay = new JSpinner(new SpinnerNumberModel(100, 20, 1000, 10));
      styleSpinner(this.spDelay);
      delayPnl.add(lblDelay, "West");
      delayPnl.add(this.spDelay, "Center");
      JPanel scalePnl = new JPanel(new BorderLayout(4, 0));
      scalePnl.setOpaque(false);
      JLabel lblScale = new JLabel("Độ phóng to (Scale):");
      lblScale.setFont(new Font("Segoe UI", 0, 10));
      lblScale.setForeground(new Color(180, 180, 200));
      this.cbScale = new JComboBox<>(new String[]{"1.0x (Chuẩn)", "1.5x", "2.0x (Sắc nét)", "3.0x", "4.0x (Cực đại)"});
      styleCombo(this.cbScale);
      this.cbScale.setSelectedIndex(0);
      scalePnl.add(lblScale, "West");
      scalePnl.add(this.cbScale, "Center");
      grid.add(delayPnl);
      grid.add(scalePnl);
      this.chkTransparent = new JCheckBox("Nền trong suốt (Transparent Background)", true);
      this.chkTransparent.setOpaque(false);
      this.chkTransparent.setForeground(Color.WHITE);
      this.chkTransparent.setFont(new Font("Segoe UI", 0, 11));
      grid.add(this.chkTransparent);
      settingsPnl.add(grid);
      body.add(settingsPnl);
      body.add(Box.createVerticalStrut(10));
      JPanel outPnl = createSection("ĐƯỜNG DẪN XUẤT FILE");
      JPanel outRow = new JPanel(new BorderLayout(6, 0));
      outRow.setOpaque(false);
      this.txtOutPath = new JTextField();
      this.txtOutPath.setBackground(new Color(28, 28, 38));
      this.txtOutPath.setForeground(Color.WHITE);
      this.txtOutPath.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)));
      this.txtOutPath.setFont(new Font("Segoe UI", 0, 11));
      JButton btnBrowse = actionBtn("Chọn Thư Mục / File", Theme.ACCENT);
      btnBrowse.addActionListener(e -> this.browseOutput());
      outRow.add(this.txtOutPath, "Center");
      outRow.add(btnBrowse, "East");
      outPnl.add(outRow);
      body.add(outPnl);
      body.add(Box.createVerticalStrut(10));
      this.progressBar = new JProgressBar(0, 100);
      this.progressBar.setStringPainted(true);
      this.progressBar.setVisible(false);
      this.progressBar.setForeground(Theme.ACCENT);
      this.progressBar.setBackground(new Color(28, 28, 38));
      body.add(this.progressBar);
      this.lblStatus = new JLabel("Sẵn sàng xuất file GIF");
      this.lblStatus.setFont(new Font("Segoe UI", 0, 10));
      this.lblStatus.setForeground(new Color(150, 150, 170));
      this.lblStatus.setAlignmentX(0.0F);
      body.add(this.lblStatus);
      this.add(body, "Center");
      JPanel footer = new JPanel(new FlowLayout(2, 10, 10));
      footer.setBackground(new Color(24, 24, 32));
      footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(38, 38, 52)));
      this.btnCancel = actionBtn("Đóng", new Color(45, 45, 55));
      this.btnCancel.addActionListener(e -> this.dispose());
      this.btnExport = actionBtn("BẮT ĐẦU XUẤT GIF", Theme.ACCENT);
      this.btnExport.setPreferredSize(new Dimension(150, 30));
      this.btnExport.addActionListener(e -> this.startExport());
      footer.add(this.btnCancel);
      footer.add(this.btnExport);
      this.add(footer, "South");
      this.updateDefaultPath();
   }

   private void updateDefaultPath() {
      String baseDir = System.getProperty("user.dir");
      if (this.targetType == GifExportDialog.TargetType.EFFECT) {
         int id = this.effectModel != null ? this.effectModel.id : 1;
         if (this.rbSingle.isSelected()) {
            this.txtOutPath.setText(new File(baseDir, "Data/Effect/gifs/effect_" + id + ".gif").getAbsolutePath());
         } else {
            this.txtOutPath.setText(new File(baseDir, "Data/Effect/gifs/").getAbsolutePath());
         }
      } else if (this.targetType == GifExportDialog.TargetType.EFFECT_AUTO) {
         int id = this.effectAutoModel != null ? this.effectAutoModel.id : 1;
         if (this.rbSingle.isSelected()) {
            this.txtOutPath.setText(new File(baseDir, "Data/EffectAuto/gifs/effectauto_" + id + ".gif").getAbsolutePath());
         } else {
            this.txtOutPath.setText(new File(baseDir, "Data/EffectAuto/gifs/").getAbsolutePath());
         }
      } else if (this.rbSingle.isSelected()) {
         String actName = GifExportHelper.getActionName(this.partCurrentAction);
         this.txtOutPath.setText(new File(baseDir, "Data/Fashion/gifs/char_" + actName + ".gif").getAbsolutePath());
      } else {
         this.txtOutPath.setText(new File(baseDir, "Data/Fashion/gifs/").getAbsolutePath());
      }
   }

   private void browseOutput() {
      JFileChooser fc = new JFileChooser(new File(this.txtOutPath.getText()).getParentFile());
      boolean isFolderMode = this.rbAll.isSelected() || this.targetType == GifExportDialog.TargetType.PART && this.rbAll.isSelected();
      if (isFolderMode) {
         fc.setFileSelectionMode(1);
         fc.setDialogTitle("Chọn Thư Mục Lưu Hàng Loạt GIF");
      } else {
         fc.setFileSelectionMode(0);
         fc.setDialogTitle("Chọn Nơi Lưu File Animated GIF");
         fc.setSelectedFile(new File(this.txtOutPath.getText()));
      }

      if (fc.showSaveDialog(this) == 0) {
         File selFile = fc.getSelectedFile();
         if (!isFolderMode && !selFile.getName().toLowerCase().endsWith(".gif")) {
            selFile = new File(selFile.getParentFile(), selFile.getName() + ".gif");
         }

         this.txtOutPath.setText(selFile.getAbsolutePath());
      }
   }

   private double getSelectedScale() {
      int idx = this.cbScale.getSelectedIndex();
      switch (idx) {
         case 1:
            return 1.5;
         case 2:
            return 2.0;
         case 3:
            return 3.0;
         case 4:
            return 4.0;
         default:
            return 1.0;
      }
   }

   private void startExport() {
      final int delay = (Integer)this.spDelay.getValue();
      final double scale = this.getSelectedScale();
      final boolean transparent = this.chkTransparent.isSelected();
      String outPathStr = this.txtOutPath.getText().trim();
      if (outPathStr.isEmpty()) {
         JOptionPane.showMessageDialog(this, "Vui lòng chọn đường dẫn lưu file GIF!", "Thông báo", 2);
      } else {
         boolean isFolderMode = this.rbAll.isSelected() || this.targetType == GifExportDialog.TargetType.PART && this.rbAll.isSelected();
         if (!isFolderMode && !outPathStr.toLowerCase().endsWith(".gif")) {
            outPathStr = outPathStr + ".gif";
            this.txtOutPath.setText(outPathStr);
         }

         final File targetFile = new File(outPathStr);
         this.btnExport.setEnabled(false);
         this.btnCancel.setEnabled(false);
         this.progressBar.setVisible(true);
         this.progressBar.setIndeterminate(true);
         this.lblStatus.setText("Đang xử lý xuất GIF...");
         SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            protected Void doInBackground() throws Exception {
               if (GifExportDialog.this.targetType == GifExportDialog.TargetType.EFFECT) {
                  if (GifExportDialog.this.rbSingle.isSelected()) {
                     GifExportHelper.exportEffectSequenceToGif(GifExportDialog.this.effectModel, targetFile, delay, scale, transparent, null);
                  } else {
                     String effData = GifExportDialog.this.dataDir != null ? GifExportDialog.this.dataDir : "Data/Effect/data/";
                     String effImg = GifExportDialog.this.imgDir != null ? GifExportDialog.this.imgDir : "Data/Effect/img/";
                     GifExportHelper.exportAllEffectsToGif(effData, effImg, targetFile, delay, scale, transparent, null, (cur, total, msg) -> {
                        this.publish(msg);
                        GifExportDialog.this.progressBar.setIndeterminate(false);
                        GifExportDialog.this.progressBar.setMaximum(total);
                        GifExportDialog.this.progressBar.setValue(cur);
                     });
                  }
               } else if (GifExportDialog.this.targetType == GifExportDialog.TargetType.EFFECT_AUTO) {
                  if (GifExportDialog.this.rbSingle.isSelected()) {
                     GifExportHelper.exportEffectAutoSequenceToGif(GifExportDialog.this.effectAutoModel, targetFile, delay, scale, transparent, null);
                  } else {
                     String autoData = GifExportDialog.this.dataDir != null ? GifExportDialog.this.dataDir : "Data/EffectAuto/data/";
                     String autoImg = GifExportDialog.this.imgDir != null ? GifExportDialog.this.imgDir : "Data/EffectAuto/img/";
                     GifExportHelper.exportAllEffectAutosToGif(autoData, autoImg, targetFile, delay, scale, transparent, null, (cur, total, msg) -> {
                        this.publish(msg);
                        GifExportDialog.this.progressBar.setIndeterminate(false);
                        GifExportDialog.this.progressBar.setMaximum(total);
                        GifExportDialog.this.progressBar.setValue(cur);
                     });
                  }
               } else if (GifExportDialog.this.targetType == GifExportDialog.TargetType.PART) {
                  if (GifExportDialog.this.rbSingle.isSelected()) {
                     GifExportHelper.exportPartActionToGif(
                        GifExportDialog.this.partFashion,
                        GifExportDialog.this.partList,
                        GifExportDialog.this.partCurrentAction,
                        GifExportDialog.this.partDirection,
                        targetFile,
                        delay,
                        scale,
                        transparent,
                        null
                     );
                  } else {
                     boolean separate = GifExportDialog.this.rbPartSeparate.isSelected();
                     GifExportHelper.exportAllPartActionsToGif(
                        GifExportDialog.this.partFashion,
                        GifExportDialog.this.partList,
                        GifExportDialog.this.partDirection,
                        targetFile,
                        "char",
                        delay,
                        scale,
                        transparent,
                        null,
                        separate,
                        (cur, total, msg) -> {
                           this.publish(msg);
                           GifExportDialog.this.progressBar.setIndeterminate(false);
                           GifExportDialog.this.progressBar.setMaximum(total);
                           GifExportDialog.this.progressBar.setValue(cur);
                        }
                     );
                  }
               }

               return null;
            }

            @Override
            protected void process(List<String> chunks) {
               if (!chunks.isEmpty()) {
                  GifExportDialog.this.lblStatus.setText(chunks.get(chunks.size() - 1));
               }
            }

            @Override
            protected void done() {
               GifExportDialog.this.btnExport.setEnabled(true);
               GifExportDialog.this.btnCancel.setEnabled(true);
               GifExportDialog.this.progressBar.setVisible(false);

               try {
                  this.get();
                  GifExportDialog.this.lblStatus.setText("Xuất bản Animated GIF hoàn tất thành công!");
                  JOptionPane.showMessageDialog(GifExportDialog.this, "Đã xuất bản thành công vào:\n" + targetFile.getAbsolutePath(), "Xuất GIF Thành Công", 1);
                  GifExportDialog.this.dispose();
               } catch (Exception var3) {
                  Throwable cause = (Throwable)(var3.getCause() != null ? var3.getCause() : var3);
                  GifExportDialog.this.lblStatus.setText("Lỗi: " + cause.getMessage());
                  JOptionPane.showMessageDialog(GifExportDialog.this, "Lỗi khi xuất GIF: " + cause.getMessage(), "Lỗi", 0);
               }
            }
         };
         worker.execute();
      }
   }

   private static JPanel createSection(String title) {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, 1));
      p.setOpaque(false);
      p.setBorder(
         BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(45, 45, 60)), title, 0, 0, new Font("Segoe UI", 1, 10), Theme.ACCENT)
      );
      return p;
   }

   private static void styleRadio(JRadioButton rb) {
      rb.setOpaque(false);
      rb.setForeground(Color.WHITE);
      rb.setFont(new Font("Segoe UI", 0, 11));
      rb.setFocusPainted(false);
   }

   private static void styleSpinner(JSpinner sp) {
      sp.setBackground(new Color(28, 28, 38));
      sp.setForeground(Color.WHITE);
      sp.setFont(new Font("Segoe UI", 0, 11));
      sp.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)));
      JComponent editor = sp.getEditor();
      if (editor instanceof DefaultEditor) {
         JFormattedTextField tf = ((DefaultEditor)editor).getTextField();
         tf.setBackground(new Color(28, 28, 38));
         tf.setForeground(Color.WHITE);
         tf.setBorder(null);
      }
   }

   private static void styleCombo(JComboBox<?> cb) {
      cb.setBackground(new Color(28, 28, 38));
      cb.setForeground(Color.WHITE);
      cb.setFont(new Font("Segoe UI", 0, 11));
      cb.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)));
   }

   private static JButton actionBtn(String text, Color bg) {
      JButton b = new JButton(text);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setForeground(Color.WHITE);
      b.setBackground(bg);
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)), new EmptyBorder(4, 10, 4, 10)));
      b.setCursor(Cursor.getPredefinedCursor(12));
      return b;
   }

   public static enum TargetType {
      EFFECT,
      EFFECT_AUTO,
      PART;
   }
}
