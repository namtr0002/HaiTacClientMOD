package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.border.EmptyBorder;

public class EffectSequencePanel extends JPanel {
   private static final Color BG_PANEL = new Color(16, 16, 22);
   private static final Color BG_CARD = new Color(24, 24, 32);
   private static final Color BG_SELECTED = new Color(0, 95, 210);
   private static final Color BORDER_NORM = new Color(38, 38, 50);
   private static final Color BORDER_SEL = new Color(0, 160, 255);
   private static final Color TEXT_MAIN = new Color(210, 210, 225);
   private static final Color TEXT_DIM = new Color(130, 130, 150);
   private static final int THUMB_SIZE = 48;
   private EffectModel model;
   private List<BufferedImage> thumbs;
   private int selectedSeqPos = 0;
   private EffectSequencePanel.OnSequenceActionListener listener;
   private final DefaultListModel<Integer> listModel = new DefaultListModel<>();
   private final JList<Integer> jList;
   private final JLabel headerBadge;

   public EffectSequencePanel() {
      this.setLayout(new BorderLayout(0, 4));
      this.setBackground(BG_PANEL);
      this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(36, 36, 48)));
      this.setPreferredSize(new Dimension(230, 0));
      JPanel headerPanel = new JPanel(new BorderLayout(4, 0));
      headerPanel.setBackground(new Color(22, 22, 30));
      headerPanel.setBorder(new EmptyBorder(6, 8, 6, 8));
      JLabel title = new JLabel("CHUỖI HOẠT HỌA");
      title.setFont(new Font("Segoe UI", 1, 10));
      title.setForeground(Theme.ACCENT);
      headerPanel.add(title, "West");
      this.headerBadge = new JLabel("0 bước");
      this.headerBadge.setFont(new Font("Segoe UI", 1, 10));
      this.headerBadge.setForeground(new Color(0, 200, 255));
      headerPanel.add(this.headerBadge, "East");
      this.add(headerPanel, "North");
      this.jList = new JList<>(this.listModel);
      this.jList.setBackground(BG_PANEL);
      this.jList.setForeground(TEXT_MAIN);
      this.jList.setSelectionBackground(BG_SELECTED);
      this.jList.setSelectionForeground(Color.WHITE);
      this.jList.setFixedCellHeight(64);
      this.jList.setCellRenderer(new EffectSequencePanel.SeqStepCellRenderer());
      this.jList.setBorder(new EmptyBorder(4, 4, 4, 4));
      this.jList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int idx = this.jList.getSelectedIndex();
            if (idx >= 0 && idx < this.listModel.size()) {
               this.selectedSeqPos = idx;
               if (this.listener != null) {
                  this.listener.onSeqPosSelected(idx);
               }
            }
         }
      });
      this.jList.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            int idx = EffectSequencePanel.this.jList.locationToIndex(e.getPoint());
            if (idx >= 0) {
               EffectSequencePanel.this.jList.setSelectedIndex(idx);
               EffectSequencePanel.this.selectedSeqPos = idx;
               if (SwingUtilities.isRightMouseButton(e)) {
                  EffectSequencePanel.this.showContextMenu(e.getComponent(), e.getX(), e.getY(), idx);
               } else if (e.getClickCount() == 2) {
                  EffectSequencePanel.this.promptChangeFrameTarget(idx);
               }
            }
         }
      });
      this.jList.setDragEnabled(true);
      this.jList.setDropMode(DropMode.INSERT);
      this.jList.setTransferHandler(new EffectSequencePanel.ListTransferHandler());
      JScrollPane scroll = new JScrollPane(this.jList);
      scroll.setBorder(null);
      scroll.setBackground(BG_PANEL);
      scroll.getViewport().setBackground(BG_PANEL);
      scroll.getVerticalScrollBar().setUnitIncrement(20);
      this.add(scroll, "Center");
      this.add(this.buildBottomToolbar(), "South");
   }

   public void setListener(EffectSequencePanel.OnSequenceActionListener l) {
      this.listener = l;
   }

   public void setModel(EffectModel m, List<BufferedImage> thumbs) {
      this.model = m;
      this.thumbs = thumbs;
      this.listModel.clear();
      if (m != null && m.sequence != null) {
         for (int i = 0; i < m.sequence.length; i++) {
            this.listModel.addElement(i);
         }

         this.headerBadge.setText(m.sequence.length + " bước");
      } else {
         this.headerBadge.setText("0 bước");
      }

      if (this.selectedSeqPos >= this.listModel.size()) {
         this.selectedSeqPos = Math.max(0, this.listModel.size() - 1);
      }

      if (this.listModel.size() > 0) {
         this.jList.setSelectedIndex(this.selectedSeqPos);
      }

      this.repaint();
   }

   public void selectPos(int pos) {
      if (pos >= 0 && pos < this.listModel.size() && pos != this.jList.getSelectedIndex()) {
         this.selectedSeqPos = pos;
         this.jList.setSelectedIndex(pos);
         this.jList.ensureIndexIsVisible(pos);
      }
   }

   public int getSelectedPos() {
      return this.selectedSeqPos;
   }

   private JPanel buildBottomToolbar() {
      JPanel bar = new JPanel(new GridLayout(3, 2, 4, 4));
      bar.setOpaque(false);
      bar.setBorder(new EmptyBorder(4, 6, 6, 6));
      JButton btnAdd = actionBtn("Thêm Bước", Theme.ACCENT);
      btnAdd.setToolTipText("Thêm bước hoạt họa mới vào cuối hoặc sau vị trí đang chọn");
      btnAdd.addActionListener(e -> {
         if (this.model != null) {
            int curF = this.model.resolveSeqFrame(this.selectedSeqPos);
            int newPos = this.selectedSeqPos >= 0 ? this.selectedSeqPos + 1 : -1;
            if (this.listener != null) {
               this.listener.onAddSeqStep(newPos, curF);
            }
         }
      });
      JButton btnDel = actionBtn("Xóa Bước", new Color(180, 50, 50));
      btnDel.setToolTipText("Xóa bước sequence đang chọn");
      btnDel.addActionListener(e -> {
         if (this.listener != null && this.model != null && this.model.getSeqLen() > 1) {
            this.listener.onDeleteSeqStep(this.selectedSeqPos);
         }
      });
      JButton btnEditSeq = actionBtn("Sửa Chuỗi", new Color(110, 60, 170));
      btnEditSeq.setToolTipText("Mở bảng sửa chuỗi Sequence tùy chỉnh");
      btnEditSeq.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onOpenSequenceEditor();
         }
      });
      JButton btnAuto = actionBtn("Auto Seq", new Color(0, 120, 190));
      btnAuto.setToolTipText("Tự động áp dụng Sequence 1:1 theo các Frames");
      btnAuto.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onAutoSequence();
         }
      });
      JButton btnReverse = actionBtn("Đảo Chuỗi", new Color(160, 90, 0));
      btnReverse.setToolTipText("Đảo ngược thứ tự toàn bộ chuỗi Sequence");
      btnReverse.addActionListener(e -> this.reverseSequence());
      JButton btnGif = actionBtn("Xuất GIF", new Color(0, 150, 90));
      btnGif.setToolTipText("Xuất hoạt họa Sequence thành file Animated GIF");
      btnGif.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onExportGif();
         }
      });
      bar.add(btnAdd);
      bar.add(btnDel);
      bar.add(btnEditSeq);
      bar.add(btnAuto);
      bar.add(btnReverse);
      bar.add(btnGif);
      return bar;
   }

   private void reverseSequence() {
      if (this.model != null && this.model.sequence != null && this.model.sequence.length > 1) {
         int len = this.model.sequence.length;
         int[] reversed = new int[len];

         for (int i = 0; i < len; i++) {
            reversed[i] = this.model.sequence[len - 1 - i];
         }

         if (this.listener != null) {
            this.listener.onApplySequence(reversed);
         }
      }
   }

   private void duplicateSequence() {
      if (this.model != null && this.model.sequence != null && this.model.sequence.length != 0) {
         int len = this.model.sequence.length;
         int[] dup = new int[len * 2];
         System.arraycopy(this.model.sequence, 0, dup, 0, len);
         System.arraycopy(this.model.sequence, 0, dup, len, len);
         if (this.listener != null) {
            this.listener.onApplySequence(dup);
         }
      }
   }

   private void showContextMenu(Component comp, int x, int y, int pos) {
      if (this.model != null) {
         JPopupMenu menu = new JPopupMenu();
         menu.setBackground(new Color(24, 24, 32));
         int curTargetFrame = this.model.resolveSeqFrame(pos);
         JMenuItem miChange = this.item("Đổi Frame Đích (Đang trỏ Frame #" + curTargetFrame + ")...");
         miChange.addActionListener(e -> this.promptChangeFrameTarget(pos));
         menu.add(miChange);
         JMenuItem miAddAfter = this.item("Thêm Bước Sau Vị Trí Này");
         miAddAfter.addActionListener(e -> {
            if (this.listener != null) {
               this.listener.onAddSeqStep(pos + 1, curTargetFrame);
            }
         });
         menu.add(miAddAfter);
         JMenuItem miDup = this.item("Nhân Bản Bước Này");
         miDup.addActionListener(e -> {
            if (this.listener != null) {
               this.listener.onAddSeqStep(pos + 1, curTargetFrame);
            }
         });
         menu.add(miDup);
         JMenuItem miDel = this.item("Xóa Bước #" + (pos + 1));
         miDel.setForeground(new Color(255, 90, 90));
         miDel.addActionListener(e -> {
            if (this.listener != null && this.model.getSeqLen() > 1) {
               this.listener.onDeleteSeqStep(pos);
            }
         });
         menu.add(miDel);
         menu.addSeparator();
         JMenuItem miEditSeq = this.item("Sửa Chuỗi Sequence (Text)...");
         miEditSeq.addActionListener(e -> {
            if (this.listener != null) {
               this.listener.onOpenSequenceEditor();
            }
         });
         menu.add(miEditSeq);
         JMenuItem miAuto = this.item("Tự Động Sequence 1:1");
         miAuto.addActionListener(e -> {
            if (this.listener != null) {
               this.listener.onAutoSequence();
            }
         });
         menu.add(miAuto);
         JMenuItem miRev = this.item("Đảo Ngược Toàn Bộ Chuỗi");
         miRev.addActionListener(e -> this.reverseSequence());
         menu.add(miRev);
         JMenuItem miDupLoop = this.item("Nhân Đôi Vòng Lặp (x2 Loop)");
         miDupLoop.addActionListener(e -> this.duplicateSequence());
         menu.add(miDupLoop);
         menu.addSeparator();
         JMenuItem miGif = this.item("Xuất Animated GIF...");
         miGif.setForeground(new Color(0, 220, 150));
         miGif.addActionListener(e -> {
            if (this.listener != null) {
               this.listener.onExportGif();
            }
         });
         menu.add(miGif);
         menu.show(comp, x, y);
      }
   }

   private void promptChangeFrameTarget(int pos) {
      if (this.model != null && this.model.frames != null && this.model.frames.length != 0) {
         int frameCount = this.model.frames.length;
         String[] options = new String[frameCount];

         for (int i = 0; i < frameCount; i++) {
            options[i] = "Frame #" + i + " (" + (this.model.frames[i].allParts != null ? this.model.frames[i].allParts.size() : 0) + " parts)";
         }

         int curTarget = this.model.resolveSeqFrame(pos);
         Object sel = JOptionPane.showInputDialog(
            this, "Chọn Frame đích cho Bước #" + (pos + 1) + ":", "Đổi Frame Đích", -1, null, options, options[Math.min(curTarget, options.length - 1)]
         );
         if (sel != null) {
            for (int i = 0; i < options.length; i++) {
               if (options[i].equals(sel)) {
                  if (this.listener != null) {
                     this.listener.onChangeSeqTarget(pos, i);
                  }
                  break;
               }
            }
         }
      }
   }

   private JMenuItem item(String text) {
      JMenuItem mi = new JMenuItem(text);
      mi.setFont(new Font("Segoe UI", 0, 11));
      mi.setForeground(new Color(210, 210, 230));
      mi.setBackground(new Color(24, 24, 32));
      return mi;
   }

   private static JButton actionBtn(String text, Color bg) {
      JButton b = new JButton(text);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setForeground(Color.WHITE);
      b.setBackground(bg);
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 55, 75)), new EmptyBorder(4, 6, 4, 6)));
      b.setCursor(Cursor.getPredefinedCursor(12));
      return b;
   }

   private class ListTransferHandler extends TransferHandler {
      private int fromIndex = -1;

      @Override
      public int getSourceActions(JComponent c) {
         return 2;
      }

      @Override
      protected Transferable createTransferable(JComponent c) {
         this.fromIndex = EffectSequencePanel.this.jList.getSelectedIndex();
         return new StringSelection(String.valueOf(this.fromIndex));
      }

      @Override
      public boolean canImport(TransferSupport support) {
         return support.isDataFlavorSupported(DataFlavor.stringFlavor);
      }

      @Override
      public boolean importData(TransferSupport support) {
         if (!this.canImport(support)) {
            return false;
         } else {
            try {
               JList.DropLocation dl = (JList.DropLocation)support.getDropLocation();
               int toIndex = dl.getIndex();
               if (this.fromIndex >= 0 && toIndex >= 0 && this.fromIndex != toIndex) {
                  if (this.fromIndex < toIndex) {
                     toIndex--;
                  }

                  if (EffectSequencePanel.this.listener != null) {
                     EffectSequencePanel.this.listener.onReorderSeq(this.fromIndex, toIndex);
                  }

                  return true;
               }
            } catch (Exception var4) {
            }

            return false;
         }
      }
   }

   public interface OnSequenceActionListener {
      void onSeqPosSelected(int var1);

      void onAddSeqStep(int var1, int var2);

      void onDeleteSeqStep(int var1);

      void onChangeSeqTarget(int var1, int var2);

      void onReorderSeq(int var1, int var2);

      void onApplySequence(int[] var1);

      void onAutoSequence();

      void onOpenSequenceEditor();

      void onExportGif();
   }

   private class SeqStepCellRenderer extends JPanel implements ListCellRenderer<Integer> {
      private final JLabel lblStepNum = new JLabel();
      private final JLabel lblFrameNum = new JLabel();
      private final JLabel lblThumb = new JLabel();

      SeqStepCellRenderer() {
         this.setLayout(new BorderLayout(8, 0));
         this.setBorder(new EmptyBorder(4, 8, 4, 8));
         this.setOpaque(true);
         this.lblThumb.setPreferredSize(new Dimension(48, 48));
         this.lblThumb.setHorizontalAlignment(0);
         this.lblThumb.setBorder(BorderFactory.createLineBorder(new Color(45, 45, 60)));
         this.add(this.lblThumb, "West");
         JPanel textPnl = new JPanel(new GridLayout(2, 1, 0, 2));
         textPnl.setOpaque(false);
         this.lblStepNum.setFont(new Font("Segoe UI", 1, 11));
         this.lblFrameNum.setFont(new Font("Segoe UI", 0, 10));
         textPnl.add(this.lblStepNum);
         textPnl.add(this.lblFrameNum);
         this.add(textPnl, "Center");
      }

      public Component getListCellRendererComponent(JList<? extends Integer> list, Integer value, int index, boolean isSelected, boolean cellHasFocus) {
         int seqPos = value;
         int targetFrame = EffectSequencePanel.this.model != null ? EffectSequencePanel.this.model.resolveSeqFrame(seqPos) : 0;
         int partCount = EffectSequencePanel.this.model != null
               && EffectSequencePanel.this.model.frames != null
               && targetFrame < EffectSequencePanel.this.model.frames.length
            ? (EffectSequencePanel.this.model.frames[targetFrame].allParts != null ? EffectSequencePanel.this.model.frames[targetFrame].allParts.size() : 0)
            : 0;
         this.lblStepNum.setText("Bước #" + (seqPos + 1));
         this.lblFrameNum.setText("Frame #" + targetFrame + " (" + partCount + " parts)");
         if (isSelected) {
            this.setBackground(EffectSequencePanel.BG_SELECTED);
            this.lblStepNum.setForeground(Color.WHITE);
            this.lblFrameNum.setForeground(new Color(220, 235, 255));
            this.lblThumb.setBorder(BorderFactory.createLineBorder(EffectSequencePanel.BORDER_SEL, 2));
         } else {
            this.setBackground(index % 2 == 0 ? EffectSequencePanel.BG_CARD : new Color(20, 20, 28));
            this.lblStepNum.setForeground(EffectSequencePanel.TEXT_MAIN);
            this.lblFrameNum.setForeground(EffectSequencePanel.TEXT_DIM);
            this.lblThumb.setBorder(BorderFactory.createLineBorder(EffectSequencePanel.BORDER_NORM));
         }

         if (EffectSequencePanel.this.thumbs != null && targetFrame >= 0 && targetFrame < EffectSequencePanel.this.thumbs.size()) {
            BufferedImage thumb = EffectSequencePanel.this.thumbs.get(targetFrame);
            if (thumb != null) {
               Image scaled = thumb.getScaledInstance(46, 46, 4);
               this.lblThumb.setIcon(new ImageIcon(scaled));
               this.lblThumb.setText("");
            } else {
               this.lblThumb.setIcon(null);
               this.lblThumb.setText("F#" + targetFrame);
            }
         } else {
            this.lblThumb.setIcon(null);
            this.lblThumb.setText("F#" + targetFrame);
         }

         return this;
      }
   }
}
