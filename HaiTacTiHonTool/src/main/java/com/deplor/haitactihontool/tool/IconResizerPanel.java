package com.deplor.haitactihontool.tool;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class IconResizerPanel extends JPanel {
   private JTextField txtSrc;
   private JTextField txtDst;
   private JTextField txtRange;
   private JComboBox<String> cbSrcZoom;
   private JComboBox<String> cbFilter;
   private JCheckBox chkSubfolders;
   private JProgressBar progressBar;
   private JTextArea txtLog;
   private JComboBox<String> cbPreviewFile;
   private JLabel lblPreviewImg;
   private JLabel lblPreviewInfo;
   private volatile boolean isRunning = false;

   public IconResizerPanel() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      this.setBorder(new EmptyBorder(15, 15, 15, 15));
      this.buildUI();
   }

   private void buildUI() {
      JPanel hdr = new JPanel(new BorderLayout());
      hdr.setOpaque(false);
      hdr.setBorder(new EmptyBorder(0, 0, 10, 0));
      JLabel title = StyledUI.createLabel("⚡ HTTH ICON RESIZER STUDIO (x1, x2, x3, x4)", Theme.F_TITLE, Theme.ACCENT);
      JLabel sub = StyledUI.createLabel(
         "Resize icon id.png / Small<id>.png chuẩn HTTH | Giữ nguyên kênh Alpha trong suốt | Lọc Range ID", Theme.F_TINY, Theme.TEXT_MUTED
      );
      hdr.add(title, "North");
      hdr.add(sub, "South");
      this.add(hdr, "North");
      JPanel body = new JPanel(new GridBagLayout());
      body.setOpaque(false);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = 1;
      gbc.weighty = 1.0;
      gbc.gridx = 0;
      gbc.weightx = 0.6;
      gbc.insets = new Insets(0, 0, 0, 10);
      body.add(this.buildLeftCard(), gbc);
      gbc.gridx = 1;
      gbc.weightx = 0.4;
      gbc.insets = new Insets(0, 0, 0, 0);
      body.add(this.buildRightCard(), gbc);
      this.add(body, "Center");
   }

   private JPanel buildLeftCard() {
      JPanel p = new JPanel(new BorderLayout(0, 8));
      p.setBackground(Theme.BG_DARK);
      p.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER), new EmptyBorder(14, 14, 14, 14)));
      JPanel form = new JPanel();
      form.setLayout(new BoxLayout(form, 1));
      form.setOpaque(false);
      form.add(StyledUI.createLabel("\ud83d\udcc1 Thư mục Icon Gốc (Source Folder):", Theme.F_BOLD, Theme.WARNING));
      form.add(Box.createVerticalStrut(3));
      JPanel rSrc = new JPanel(new BorderLayout(6, 0));
      rSrc.setOpaque(false);
      this.txtSrc = StyledUI.createTextField("");
      JButton btnBrowseSrc = StyledUI.createButton("Chọn Thư Mục...", Theme.BG_CARD);
      btnBrowseSrc.addActionListener(e -> this.browseSrc());
      rSrc.add(this.txtSrc, "Center");
      rSrc.add(btnBrowseSrc, "East");
      form.add(rSrc);
      form.add(Box.createVerticalStrut(10));
      form.add(StyledUI.createLabel("\ud83d\udcbe Thư mục Xuất Icon (Output Folder):", Theme.F_BOLD, Theme.ACCENT));
      form.add(Box.createVerticalStrut(3));
      JPanel rDst = new JPanel(new BorderLayout(6, 0));
      rDst.setOpaque(false);
      this.txtDst = StyledUI.createTextField("");
      JButton btnBrowseDst = StyledUI.createButton("Chọn Thư Mục...", Theme.BG_CARD);
      btnBrowseDst.addActionListener(e -> this.browseDst());
      rDst.add(this.txtDst, "Center");
      rDst.add(btnBrowseDst, "East");
      form.add(rDst);
      form.add(Box.createVerticalStrut(10));
      form.add(StyledUI.createLabel("\ud83c\udfaf Lọc ID Icon (VD: 1-50, 100, 105-120 | Để trống = Resize tất cả):", Theme.F_BOLD, Theme.SUCCESS));
      form.add(Box.createVerticalStrut(3));
      JPanel rRng = new JPanel(new BorderLayout(6, 0));
      rRng.setOpaque(false);
      this.txtRange = StyledUI.createTextField("");
      this.txtRange.setForeground(Theme.SUCCESS);
      JButton btnClearRng = StyledUI.createButton("Xóa Lọc", Theme.BG_CARD);
      btnClearRng.setForeground(Theme.ERROR);
      btnClearRng.addActionListener(e -> this.txtRange.setText(""));
      rRng.add(this.txtRange, "Center");
      rRng.add(btnClearRng, "East");
      form.add(rRng);
      form.add(Box.createVerticalStrut(10));
      JPanel optBox = new JPanel(new GridLayout(2, 2, 8, 8));
      optBox.setBackground(Theme.BG_DARKER);
      optBox.setBorder(new EmptyBorder(8, 8, 8, 8));
      JPanel rOpt1 = new JPanel(new FlowLayout(0, 4, 0));
      rOpt1.setOpaque(false);
      rOpt1.add(StyledUI.createLabel("Nguồn gốc Zoom:", Theme.F_SMALL, Theme.TEXT_MAIN));
      this.cbSrcZoom = new JComboBox<>(new String[]{"x4", "x3", "x2", "x1"});
      this.cbSrcZoom.setSelectedIndex(0);
      StyledUI.styleComboBox(this.cbSrcZoom);
      rOpt1.add(this.cbSrcZoom);
      optBox.add(rOpt1);
      JPanel rOpt2 = new JPanel(new FlowLayout(0, 4, 0));
      rOpt2.setOpaque(false);
      rOpt2.add(StyledUI.createLabel("Thuật toán:", Theme.F_SMALL, Theme.TEXT_MAIN));
      this.cbFilter = new JComboBox<>(new String[]{"Nearest (Pixel Art)", "Bicubic (Mịn)", "Bilinear"});
      StyledUI.styleComboBox(this.cbFilter);
      rOpt2.add(this.cbFilter);
      optBox.add(rOpt2);
      this.chkSubfolders = StyledUI.createCheckBox("Tự động tạo thư mục con (x1, x2, x3, x4)");
      this.chkSubfolders.setSelected(true);
      optBox.add(this.chkSubfolders);
      form.add(optBox);
      form.add(Box.createVerticalStrut(10));
      JPanel btnGrid = new JPanel(new GridLayout(1, 5, 4, 0));
      btnGrid.setOpaque(false);
      JButton btnX1 = StyledUI.createButton("\ud83d\udfe2 x1 (25%)", new Color(1793568));
      btnX1.addActionListener(e -> this.startResize(Collections.singletonList(1)));
      btnGrid.add(btnX1);
      JButton btnX2 = StyledUI.createButton("\ud83d\udd35 x2 (50%)", new Color(870305));
      btnX2.addActionListener(e -> this.startResize(Collections.singletonList(2)));
      btnGrid.add(btnX2);
      JButton btnX3 = StyledUI.createButton("\ud83d\udfe1 x3 (75%)", new Color(15094016));
      btnX3.addActionListener(e -> this.startResize(Collections.singletonList(3)));
      btnGrid.add(btnX3);
      JButton btnX4 = StyledUI.createButton("\ud83d\udfe3 x4 (100%)", new Color(4854924));
      btnX4.addActionListener(e -> this.startResize(Collections.singletonList(4)));
      btnGrid.add(btnX4);
      JButton btnAll = StyledUI.createButton("\ud83d\ude80 TẤT CẢ", new Color(12720219));
      btnAll.addActionListener(e -> this.startResize(Arrays.asList(1, 2, 3, 4)));
      btnGrid.add(btnAll);
      form.add(btnGrid);
      form.add(Box.createVerticalStrut(8));
      this.progressBar = new JProgressBar(0, 100);
      this.progressBar.setStringPainted(true);
      this.progressBar.setForeground(Theme.ACCENT);
      this.progressBar.setBackground(Theme.BG_DARKER);
      form.add(this.progressBar);
      p.add(form, "North");
      JPanel logP = new JPanel(new BorderLayout());
      logP.setOpaque(false);
      logP.add(StyledUI.createLabel("\ud83d\udccb Nhật Ký Xử Lý (Log Console):", Theme.F_TINY, Theme.TEXT_MUTED), "North");
      this.txtLog = new JTextArea();
      this.txtLog.setBackground(new Color(8, 8, 14));
      this.txtLog.setForeground(new Color(205, 214, 244));
      this.txtLog.setFont(Theme.F_MONO);
      this.txtLog.setEditable(false);
      JScrollPane spLog = new JScrollPane(this.txtLog);
      StyledUI.styleScrollPane(spLog);
      logP.add(spLog, "Center");
      p.add(logP, "Center");
      return p;
   }

   private JPanel buildRightCard() {
      JPanel p = new JPanel(new BorderLayout(0, 8));
      p.setBackground(Theme.BG_DARK);
      p.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER), new EmptyBorder(14, 14, 14, 14)));
      JPanel top = new JPanel(new BorderLayout(0, 4));
      top.setOpaque(false);
      top.add(StyledUI.createLabel("\ud83d\udd0d XEM TRƯỚC ICON (LIVE PREVIEW)", Theme.F_BOLD, Theme.ACCENT), "North");
      this.cbPreviewFile = new JComboBox<>();
      StyledUI.styleComboBox(this.cbPreviewFile);
      this.cbPreviewFile.addActionListener(e -> this.onSelectPreview());
      top.add(this.cbPreviewFile, "South");
      p.add(top, "North");
      JPanel pCenter = new JPanel(new BorderLayout());
      pCenter.setBackground(new Color(18, 18, 28));
      pCenter.setBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER));
      this.lblPreviewImg = new JLabel("", 0);
      pCenter.add(this.lblPreviewImg, "Center");
      this.lblPreviewInfo = StyledUI.createLabel("Chưa chọn file ảnh", Theme.F_SMALL, Theme.TEXT_DIM);
      this.lblPreviewInfo.setHorizontalAlignment(0);
      this.lblPreviewInfo.setBorder(new EmptyBorder(6, 6, 6, 6));
      pCenter.add(this.lblPreviewInfo, "South");
      p.add(pCenter, "Center");
      JPanel bot = new JPanel(new GridLayout(3, 1, 0, 4));
      bot.setOpaque(false);
      JButton btnScan = StyledUI.createButton("\ud83d\udd04 Quét Lại Thư Mục Nguồn", Theme.BG_CARD);
      btnScan.addActionListener(e -> this.refreshFileList());
      bot.add(btnScan);
      JButton btnOpen = StyledUI.createButton("\ud83d\udcc2 Mở Thư Mục Output", Theme.BG_CARD);
      btnOpen.addActionListener(e -> this.openOutputDir());
      bot.add(btnOpen);
      JButton btnClean = StyledUI.createButton("\ud83e\uddf9 Dọn Dẹp File Rác (.tmp / .bak)", Theme.BG_CARD);
      btnClean.addActionListener(e -> this.cleanTempFiles());
      bot.add(btnClean);
      p.add(bot, "South");
      return p;
   }

   private void log(String s) {
      SwingUtilities.invokeLater(() -> {
         this.txtLog.append(s + "\n");
         this.txtLog.setCaretPosition(this.txtLog.getDocument().getLength());
      });
   }

   private void browseSrc() {
      JFileChooser fc = new JFileChooser(this.txtSrc.getText().trim().isEmpty() ? "." : this.txtSrc.getText().trim());
      fc.setFileSelectionMode(1);
      if (fc.showOpenDialog(this) == 0) {
         String path = fc.getSelectedFile().getAbsolutePath();
         this.txtSrc.setText(path);
         if (this.txtDst.getText().trim().isEmpty()) {
            this.txtDst.setText(path);
         }

         this.refreshFileList();
      }
   }

   private void browseDst() {
      JFileChooser fc = new JFileChooser(this.txtDst.getText().trim().isEmpty() ? "." : this.txtDst.getText().trim());
      fc.setFileSelectionMode(1);
      if (fc.showOpenDialog(this) == 0) {
         this.txtDst.setText(fc.getSelectedFile().getAbsolutePath());
      }
   }

   private void refreshFileList() {
      String src = this.txtSrc.getText().trim();
      this.cbPreviewFile.removeAllItems();
      if (!src.isEmpty() && new File(src).isDirectory()) {
         File[] files = new File(src).listFiles((dir, name) -> {
            String n = name.toLowerCase();
            return n.endsWith(".png") || n.endsWith(".jpg") || n.endsWith(".jpeg");
         });
         if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));

            for (File f : files) {
               this.cbPreviewFile.addItem(f.getName());
            }

            if (this.cbPreviewFile.getItemCount() > 0) {
               this.cbPreviewFile.setSelectedIndex(0);
            }
         }
      }
   }

   private void onSelectPreview() {
      String src = this.txtSrc.getText().trim();
      String fname = (String)this.cbPreviewFile.getSelectedItem();
      if (!src.isEmpty() && fname != null && !fname.isEmpty()) {
         File f = new File(src, fname);
         if (f.isFile()) {
            try {
               BufferedImage img = ImageIO.read(f);
               if (img != null) {
                  int w = img.getWidth();
                  int h = img.getHeight();
                  this.lblPreviewInfo.setText("File: " + fname + " | Size: " + w + "x" + h + " px");
                  int maxDisplay = 180;
                  double scale = Math.min((double)maxDisplay / w, (double)maxDisplay / h);
                  scale = Math.min(scale, 6.0);
                  int dw = Math.max(1, (int)(w * scale));
                  int dh = Math.max(1, (int)(h * scale));
                  Image scaled = img.getScaledInstance(dw, dh, 2);
                  this.lblPreviewImg.setIcon(new ImageIcon(scaled));
               }
            } catch (Exception var13) {
               this.lblPreviewInfo.setText("Lỗi đọc ảnh: " + var13.getMessage());
            }
         }
      }
   }

   private void openOutputDir() {
      String dst = this.txtDst.getText().trim();
      if (!dst.isEmpty() && new File(dst).exists()) {
         try {
            Desktop.getDesktop().open(new File(dst));
         } catch (Exception var3) {
            JOptionPane.showMessageDialog(this, "Không thể mở thư mục: " + var3.getMessage());
         }
      } else {
         JOptionPane.showMessageDialog(this, "Thư mục Output chưa tồn tại!");
      }
   }

   private void cleanTempFiles() {
      String dst = this.txtDst.getText().trim();
      if (!dst.isEmpty() && new File(dst).exists()) {
         int count = 0;
         File[] all = new File(dst).listFiles();
         if (all != null) {
            for (File f : all) {
               if ((f.getName().endsWith(".tmp") || f.getName().endsWith(".bak")) && f.delete()) {
                  count++;
               }
            }
         }

         JOptionPane.showMessageDialog(this, "Đã xóa " + count + " file tạm.");
      }
   }

   public static Set<Integer> parseIdRange(String rangeStr) {
      if (rangeStr != null && !rangeStr.trim().isEmpty()) {
         Set<Integer> set = new HashSet<>();
         String[] parts = rangeStr.replace(";", ",").replace(" ", "").split(",");

         for (String part : parts) {
            if (!part.isEmpty()) {
               if (part.contains("-")) {
                  String[] sub = part.split("-");
                  if (sub.length == 2) {
                     try {
                        int start = Integer.parseInt(sub[0]);
                        int end = Integer.parseInt(sub[1]);
                        if (start > end) {
                           int t = start;
                           start = end;
                           end = t;
                        }

                        for (int i = start; i <= end; i++) {
                           set.add(i);
                        }
                     } catch (Exception var12) {
                     }
                  }
               } else {
                  try {
                     set.add(Integer.parseInt(part));
                  } catch (Exception var11) {
                  }
               }
            }
         }

         return set;
      } else {
         return null;
      }
   }

   public static Integer extractIdFromFilename(String filename) {
      Matcher m = Pattern.compile("\\d+").matcher(filename);
      if (m.find()) {
         try {
            return Integer.parseInt(m.group());
         } catch (Exception var3) {
         }
      }

      return null;
   }

   private void startResize(List<Integer> targetZooms) {
      if (this.isRunning) {
         JOptionPane.showMessageDialog(this, "Đang có tiến trình resize đang chạy!");
      } else {
         String src = this.txtSrc.getText().trim();
         String dst = this.txtDst.getText().trim();
         if (src.isEmpty() || !new File(src).isDirectory()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thư mục Icon Gốc hợp lệ!");
         } else if (dst.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thư mục Output!");
         } else {
            String rangeStr = this.txtRange.getText().trim();
            int srcZoom = 4 - this.cbSrcZoom.getSelectedIndex();
            int filterIdx = this.cbFilter.getSelectedIndex();
            boolean useSubfolders = this.chkSubfolders.isSelected();
            this.isRunning = true;
            this.txtLog.setText("");
            this.progressBar.setValue(0);
            new Thread(() -> {
               try {
                  this.runResizeTask(src, dst, targetZooms, rangeStr, srcZoom, filterIdx, useSubfolders);
               } finally {
                  this.isRunning = false;
               }
            }, "icon-resizer-worker").start();
         }
      }
   }

   private void runResizeTask(String srcDir, String outDir, List<Integer> targetZooms, String rangeStr, int srcZoom, int filterIdx, boolean useSubfolders) {
      Set<Integer> filterIds = parseIdRange(rangeStr);
      if (filterIds != null) {
         this.log("\ud83c\udfaf Đang áp dụng lọc ID: " + filterIds.size() + " IDs");
      }

      File[] allFiles = new File(srcDir).listFiles((dir, name) -> {
         String n = name.toLowerCase();
         return (n.endsWith(".png") || n.endsWith(".jpg") || n.endsWith(".jpeg")) && !new File(dir, name).isDirectory();
      });
      if (allFiles != null && allFiles.length != 0) {
         List<File> files = new ArrayList<>();
         if (filterIds != null) {
            for (File f : allFiles) {
               Integer id = extractIdFromFilename(f.getName());
               if (id != null && filterIds.contains(id)) {
                  files.add(f);
               }
            }
         } else {
            files.addAll(Arrays.asList(allFiles));
         }

         if (files.isEmpty()) {
            this.log("⚠️ Không có file nào khớp với dải ID đã nhập!");
         } else {
            this.log("\ud83d\udd0e Đã khớp " + files.size() + " / " + allFiles.length + " file ảnh.");
            this.log("⚙️ Cấu hình: Nguồn = x" + srcZoom + " | Mục tiêu = " + targetZooms);
            this.log("============================================================");
            int totalOps = files.size() * targetZooms.size();
            int doneOps = 0;
            int successCount = 0;
            int errCount = 0;

            for (int tz : targetZooms) {
               double scale = (double)tz / srcZoom;
               File destDir = useSubfolders ? new File(outDir, "x" + tz) : new File(outDir);
               destDir.mkdirs();
               this.log("\n\ud83d\ude80 Đang xử lý Zoom x" + tz + " (" + String.format("%.1f", scale * 100.0) + "%) -> " + destDir.getAbsolutePath() + " ...");

               for (File fx : files) {
                  File destFile = new File(destDir, fx.getName());

                  try {
                     BufferedImage srcImg = ImageIO.read(fx);
                     if (srcImg != null) {
                        int w = srcImg.getWidth();
                        int h = srcImg.getHeight();
                        if (scale == 1.0) {
                           ImageIO.write(srcImg, "PNG", destFile);
                        } else {
                           int tw = Math.max(1, (int)Math.floor(w * scale + 0.5));
                           int th = Math.max(1, (int)Math.floor(h * scale + 0.5));
                           BufferedImage resized = new BufferedImage(tw, th, 2);
                           Graphics2D g2 = resized.createGraphics();
                           if (filterIdx == 0) {
                              g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                           } else if (filterIdx == 1) {
                              g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                              g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                              g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                           } else {
                              g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                           }

                           g2.drawImage(srcImg, 0, 0, tw, th, null);
                           g2.dispose();
                           ImageIO.write(resized, "PNG", destFile);
                        }

                        successCount++;
                     }
                  } catch (Exception var30) {
                     errCount++;
                     this.log("⚠️ Lỗi file " + fx.getName() + " (Zoom x" + tz + "): " + var30.getMessage());
                  }

                  doneOps++;
                  int prog = (int)((double)doneOps / totalOps * 100.0);
                  SwingUtilities.invokeLater(() -> this.progressBar.setValue(prog));
               }

               this.log("✅ Hoàn tất Zoom x" + tz + " (" + files.size() + " files).");
            }

            this.log("\n============================================================");
            this.log("\ud83c\udf89 TỔNG KẾT: Thành công " + successCount + " lượt | Lỗi: " + errCount + " lượt.");
            JOptionPane.showMessageDialog(this, "Đã resize xong " + successCount + " file ảnh sang zoom " + targetZooms + "!");
         }
      } else {
         this.log("❌ Không tìm thấy file ảnh nào trong: " + srcDir);
      }
   }
}
