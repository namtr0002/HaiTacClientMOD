package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SequenceEditorDialog extends JDialog {
   private final EffectModel model;
   private final Consumer<int[]> onApplyCallback;
   private final JTextArea txtSequence = new JTextArea();
   private final JLabel lblStats = new JLabel("Tổng bước: 0 | Chuỗi: —");
   private final JLabel lblFramesInfo;
   private boolean isApplied = false;

   public SequenceEditorDialog(Window owner, EffectModel model, Consumer<int[]> onApplyCallback) {
      super(owner, "Chỉnh Sửa Chuỗi Sequence Hoạt Họa", ModalityType.APPLICATION_MODAL);
      this.model = model;
      this.onApplyCallback = onApplyCallback;
      this.setSize(650, 460);
      this.setLocationRelativeTo(owner);
      this.getContentPane().setBackground(Theme.BG_DARKER);
      this.setLayout(new BorderLayout(0, 10));
      JPanel header = new JPanel(new BorderLayout(8, 4));
      header.setBackground(Theme.BG_DARK);
      header.setBorder(new EmptyBorder(12, 16, 12, 16));
      JLabel lblTitle = new JLabel("CHỈNH SỬA CHUỖI SEQUENCE (TIMELINE)");
      lblTitle.setFont(new Font("Segoe UI", 1, 14));
      lblTitle.setForeground(Theme.ACCENT);
      header.add(lblTitle, "North");
      int frameCount = model != null ? model.getFrameCount() : 0;
      this.lblFramesInfo = new JLabel("Hiệu ứng hiện có: " + frameCount + " Keyframes (Index hợp lệ từ 0 đến " + Math.max(0, frameCount - 1) + ")");
      this.lblFramesInfo.setFont(new Font("Segoe UI", 0, 11));
      this.lblFramesInfo.setForeground(new Color(180, 180, 200));
      header.add(this.lblFramesInfo, "South");
      this.add(header, "North");
      JPanel center = new JPanel(new BorderLayout(0, 8));
      center.setOpaque(false);
      center.setBorder(new EmptyBorder(0, 16, 0, 16));
      JPanel presetBar = new JPanel(new FlowLayout(0, 6, 0));
      presetBar.setOpaque(false);
      JButton btnLinear = this.presetBtn("⚡ 1:1 Tuần tự (0..N-1)", "Tạo chuỗi chạy tuần tự từ Frame 0 đến Frame cuối");
      JButton btnHold2 = this.presetBtn("\ud83d\udd01 Lặp đôi (0,0,1,1..)", "Mỗi frame lặp lại 2 lần để giảm tốc độ");
      JButton btnPingPong = this.presetBtn("\ud83c\udfd3 Ping-Pong", "Chạy tiến từ 0..N rồi chạy lui về 1");
      JButton btnReverse = this.presetBtn("\ud83d\udd04 Đảo ngược (N-1..0)", "Chạy ngược từ Frame cuối về Frame 0");
      JButton btnClear = this.presetBtn("\ud83e\uddf9 Xóa", "Làm trống ô nhập liệu");
      btnLinear.addActionListener(e -> {
         if (model != null && model.getFrameCount() > 0) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < model.getFrameCount(); i++) {
               if (i > 0) {
                  sb.append(", ");
               }

               sb.append(i);
            }

            this.txtSequence.setText(sb.toString());
         }
      });
      btnHold2.addActionListener(e -> {
         if (model != null && model.getFrameCount() > 0) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < model.getFrameCount(); i++) {
               if (i > 0) {
                  sb.append(", ");
               }

               sb.append(i).append(", ").append(i);
            }

            this.txtSequence.setText(sb.toString());
         }
      });
      btnPingPong.addActionListener(e -> {
         if (model != null && model.getFrameCount() > 0) {
            int n = model.getFrameCount();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < n; i++) {
               if (sb.length() > 0) {
                  sb.append(", ");
               }

               sb.append(i);
            }

            for (int i = n - 2; i >= 1; i--) {
               sb.append(", ").append(i);
            }

            this.txtSequence.setText(sb.toString());
         }
      });
      btnReverse.addActionListener(e -> {
         if (model != null && model.getFrameCount() > 0) {
            StringBuilder sb = new StringBuilder();

            for (int i = model.getFrameCount() - 1; i >= 0; i--) {
               if (sb.length() > 0) {
                  sb.append(", ");
               }

               sb.append(i);
            }

            this.txtSequence.setText(sb.toString());
         }
      });
      btnClear.addActionListener(e -> this.txtSequence.setText("0"));
      presetBar.add(btnLinear);
      presetBar.add(btnHold2);
      presetBar.add(btnPingPong);
      presetBar.add(btnReverse);
      presetBar.add(btnClear);
      center.add(presetBar, "North");
      this.txtSequence.setFont(new Font("Consolas", 1, 14));
      this.txtSequence.setBackground(new Color(24, 24, 32));
      this.txtSequence.setForeground(Color.WHITE);
      this.txtSequence.setCaretColor(Theme.ACCENT);
      this.txtSequence.setLineWrap(true);
      this.txtSequence.setWrapStyleWord(true);
      this.txtSequence.setBorder(new EmptyBorder(8, 8, 8, 8));
      this.txtSequence.putClientProperty("JTextField.placeholderText", "Nhập chuỗi sequence, vd: 0, 1, 2, 3, 4 hoặc 0-4 hoặc 0*2, 1*2");
      if (model != null) {
         this.txtSequence.setText(model.getSequenceString());
      }

      JScrollPane scroll = new JScrollPane(this.txtSequence);
      scroll.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 70), 1));
      center.add(scroll, "Center");
      JPanel statsPanel = new JPanel(new BorderLayout(0, 2));
      statsPanel.setOpaque(false);
      this.lblStats.setFont(new Font("Segoe UI", 1, 11));
      this.lblStats.setForeground(Theme.ACCENT);
      JLabel lblSyntaxHint = new JLabel("\ud83d\udca1 Mẹo cú pháp: Phân cách bằng dấu phẩy/khoảng trắng. Hỗ trợ dải \"0-5\" hoặc nhân \"0*3, 1*3\".");
      lblSyntaxHint.setFont(new Font("Segoe UI", 2, 10));
      lblSyntaxHint.setForeground(Theme.TEXT_DIM);
      statsPanel.add(this.lblStats, "North");
      statsPanel.add(lblSyntaxHint, "South");
      center.add(statsPanel, "South");
      this.txtSequence.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            SequenceEditorDialog.this.updateStats();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            SequenceEditorDialog.this.updateStats();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            SequenceEditorDialog.this.updateStats();
         }
      });
      this.updateStats();
      this.add(center, "Center");
      JPanel bottomBar = new JPanel(new FlowLayout(2, 10, 10));
      bottomBar.setBackground(Theme.BG_DARK);
      bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(38, 38, 52)));
      JButton btnCancel = this.actionBtn("Hủy", new Color(40, 40, 52));
      btnCancel.setPreferredSize(new Dimension(100, 34));
      btnCancel.addActionListener(e -> this.dispose());
      JButton btnApply = this.actionBtn("✓ ÁP DỤNG SEQUENCE", Theme.SUCCESS);
      btnApply.setPreferredSize(new Dimension(180, 34));
      btnApply.addActionListener(e -> {
         this.applySequence();
         this.dispose();
      });
      bottomBar.add(btnCancel);
      bottomBar.add(btnApply);
      this.add(bottomBar, "South");
   }

   private void updateStats() {
      if (this.model != null) {
         int frameCount = this.model.getFrameCount();
         int[] parsed = EffectModel.parseSequenceString(this.txtSequence.getText(), frameCount);
         this.lblStats
            .setText(
               "Tổng số bước: "
                  + parsed.length
                  + " bước Sequence | Bắt đầu: Frame "
                  + (parsed.length > 0 ? parsed[0] : 0)
                  + " ➔ Kết thúc: Frame "
                  + (parsed.length > 0 ? parsed[parsed.length - 1] : 0)
            );
      }
   }

   private void applySequence() {
      if (this.model != null) {
         int frameCount = this.model.getFrameCount();
         int[] parsed = EffectModel.parseSequenceString(this.txtSequence.getText(), frameCount);
         this.model.setCustomSequence(parsed);
         this.isApplied = true;
         if (this.onApplyCallback != null) {
            this.onApplyCallback.accept(parsed);
         }
      }
   }

   public boolean isApplied() {
      return this.isApplied;
   }

   private JButton presetBtn(String text, String tip) {
      final JButton b = new JButton(text);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setForeground(new Color(220, 220, 240));
      b.setBackground(new Color(36, 36, 48));
      b.setFocusPainted(false);
      b.setToolTipText(tip);
      b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 1), new EmptyBorder(4, 8, 4, 8)));
      b.setCursor(new Cursor(12));
      b.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            b.setBackground(Theme.ACCENT);
         }

         @Override
         public void mouseExited(MouseEvent e) {
            b.setBackground(new Color(36, 36, 48));
         }
      });
      return b;
   }

   private JButton actionBtn(String text, final Color bg) {
      final JButton b = new JButton(text);
      b.setFont(new Font("Segoe UI", 1, 11));
      b.setForeground(Color.WHITE);
      b.setBackground(bg);
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
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
