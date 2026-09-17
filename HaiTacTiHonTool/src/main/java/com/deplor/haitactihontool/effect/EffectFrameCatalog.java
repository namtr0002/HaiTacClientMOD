package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class EffectFrameCatalog extends JPanel {
   private static final Color BG_PANEL = new Color(16, 16, 22);
   private static final Color BG_CARD = new Color(24, 24, 32);
   private static final Color BG_SELECTED = new Color(0, 95, 210);
   private static final Color BORDER_NORM = new Color(42, 42, 56);
   private static final Color BORDER_SEL = new Color(0, 160, 255);
   private static final Color TEXT_MAIN = new Color(210, 210, 225);
   private static final Color TEXT_DIM = new Color(130, 130, 150);
   private static final int THUMB_W = 68;
   private static final int THUMB_H = 68;
   private EffectModel model;
   private List<BufferedImage> thumbs;
   private int selectedFrameIdx = 0;
   private EffectFrameCatalog.OnFrameCatalogListener listener;
   private final DefaultListModel<Integer> listModel = new DefaultListModel<>();
   private final JList<Integer> jList;
   private final JLabel headerLabel;

   public EffectFrameCatalog() {
      this.setLayout(new BorderLayout(0, 4));
      this.setBackground(BG_PANEL);
      this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(36, 36, 48)));
      this.setPreferredSize(new Dimension(250, 0));
      this.headerLabel = new JLabel("DANH SÁCH FRAMES (KEYFRAMES)");
      this.headerLabel.setFont(new Font("Segoe UI", 1, 10));
      this.headerLabel.setForeground(Theme.ACCENT);
      this.headerLabel.setBorder(new EmptyBorder(6, 8, 4, 8));
      this.add(this.headerLabel, "North");
      this.jList = new JList<>(this.listModel);
      this.jList.setBackground(BG_PANEL);
      this.jList.setForeground(TEXT_MAIN);
      this.jList.setSelectionBackground(BG_SELECTED);
      this.jList.setSelectionForeground(Color.WHITE);
      this.jList.setFixedCellWidth(84);
      this.jList.setFixedCellHeight(102);
      this.jList.setLayoutOrientation(2);
      this.jList.setVisibleRowCount(-1);
      this.jList.setCellRenderer(new EffectFrameCatalog.FrameCatalogCellRenderer());
      this.jList.setBorder(new EmptyBorder(4, 4, 4, 4));
      this.jList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int idx = this.jList.getSelectedIndex();
            if (idx >= 0 && idx < this.listModel.size()) {
               this.selectedFrameIdx = idx;
               if (this.listener != null) {
                  this.listener.onFrameSelected(idx);
               }
            }
         }
      });
      this.jList.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
               int idx = EffectFrameCatalog.this.jList.locationToIndex(e.getPoint());
               if (idx >= 0) {
                  EffectFrameCatalog.this.jList.setSelectedIndex(idx);
                  EffectFrameCatalog.this.showContextMenu(e.getComponent(), e.getX(), e.getY());
               }
            }
         }
      });
      JScrollPane scroll = new JScrollPane(this.jList);
      scroll.setBorder(null);
      scroll.setBackground(BG_PANEL);
      scroll.getViewport().setBackground(BG_PANEL);
      scroll.getVerticalScrollBar().setUnitIncrement(20);
      this.add(scroll, "Center");
      this.add(this.buildBottomToolbar(), "South");
   }

   private JPanel buildBottomToolbar() {
      JPanel bar = new JPanel(new GridLayout(3, 2, 4, 4));
      bar.setOpaque(false);
      bar.setBorder(new EmptyBorder(4, 6, 6, 6));
      JButton btnAdd = this.actionBtn("➕ Thêm Frame", Theme.ACCENT);
      btnAdd.setToolTipText("Thêm một Frame rỗng mới vào danh sách");
      btnAdd.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onAddBlankFrame();
         }
      });
      JButton btnDup = this.actionBtn("⎘ Nhân bản", new Color(38, 38, 52));
      btnDup.setToolTipText("Nhân bản Frame đang chọn (sao chép toàn bộ part)");
      btnDup.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onDuplicateFrame(this.selectedFrameIdx);
         }
      });
      JButton btnInsert = this.actionBtn("▶ Thêm Seq", new Color(0, 140, 80));
      btnInsert.setToolTipText("Chèn Frame đang chọn vào chuỗi Timeline Sequence");
      btnInsert.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onInsertFrameToSequence(this.selectedFrameIdx);
         }
      });
      JButton btnAutoSeq = this.actionBtn("⚡ Auto Seq", new Color(0, 120, 190));
      btnAutoSeq.setToolTipText("Tự động áp dụng chuỗi Sequence chạy tuần tự theo tất cả Frame");
      btnAutoSeq.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onAutoSequence();
         }
      });
      JButton btnEditSeq = this.actionBtn("\ud83d\udcdd Sửa Chuỗi", new Color(110, 60, 170));
      btnEditSeq.setToolTipText("Mở bảng nhập chuỗi Sequence tùy chỉnh");
      btnEditSeq.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onOpenSequenceEditor();
         }
      });
      JButton btnDel = this.actionBtn("✖ Xóa Frame", new Color(180, 50, 50));
      btnDel.setToolTipText("Xóa Frame đang chọn khỏi Effect");
      btnDel.addActionListener(
         e -> {
            if (this.listener != null && this.model != null && this.model.getFrameCount() > 0) {
               int ans = JOptionPane.showConfirmDialog(
                  this,
                  "Bạn có chắc muốn XÓA Frame " + this.selectedFrameIdx + " không?\n(Các bước sequence trỏ vào Frame này sẽ tự động cập nhật)",
                  "Xác nhận xóa Frame",
                  0,
                  2
               );
               if (ans == 0) {
                  this.listener.onDeleteFrame(this.selectedFrameIdx);
               }
            }
         }
      );
      bar.add(btnAdd);
      bar.add(btnDup);
      bar.add(btnInsert);
      bar.add(btnAutoSeq);
      bar.add(btnEditSeq);
      bar.add(btnDel);
      return bar;
   }

   private JButton actionBtn(String txt, final Color bg) {
      final JButton b = new JButton(txt);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setForeground(Color.WHITE);
      b.setBackground(bg);
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 55, 75), 1), new EmptyBorder(4, 4, 4, 4)));
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

   private void showContextMenu(Component c, int x, int y) {
      JPopupMenu menu = new JPopupMenu();
      menu.setBackground(new Color(28, 28, 38));
      JMenuItem i1 = new JMenuItem("➕ Thêm Frame rỗng mới");
      i1.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onAddBlankFrame();
         }
      });
      JMenuItem i2 = new JMenuItem("⎘ Nhân bản Frame " + this.selectedFrameIdx);
      i2.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onDuplicateFrame(this.selectedFrameIdx);
         }
      });
      JMenuItem i3 = new JMenuItem("▶ Chèn vào Timeline Sequence");
      i3.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onInsertFrameToSequence(this.selectedFrameIdx);
         }
      });
      JMenuItem iAuto = new JMenuItem("⚡ Áp dụng Sequence tuần tự (0..N-1)");
      iAuto.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onAutoSequence();
         }
      });
      JMenuItem iEdit = new JMenuItem("\ud83d\udcdd Mở bảng sửa chuỗi Sequence...");
      iEdit.addActionListener(e -> {
         if (this.listener != null) {
            this.listener.onOpenSequenceEditor();
         }
      });
      JMenuItem i4 = new JMenuItem("✖ Xóa Frame " + this.selectedFrameIdx);
      i4.addActionListener(e -> {
         if (this.listener != null && this.model != null && this.model.getFrameCount() > 0) {
            int ans = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn XÓA Frame " + this.selectedFrameIdx + " không?", "Xác nhận xóa Frame", 0, 2);
            if (ans == 0) {
               this.listener.onDeleteFrame(this.selectedFrameIdx);
            }
         }
      });
      menu.add(i1);
      menu.add(i2);
      menu.add(i3);
      menu.addSeparator();
      menu.add(iAuto);
      menu.add(iEdit);
      menu.addSeparator();
      menu.add(i4);
      menu.show(c, x, y);
   }

   public void setListener(EffectFrameCatalog.OnFrameCatalogListener l) {
      this.listener = l;
   }

   public void setModel(EffectModel model, List<BufferedImage> thumbs) {
      this.model = model;
      this.thumbs = thumbs;
      this.listModel.clear();
      if (model != null && model.frames != null) {
         int count = model.frames.length;
         this.headerLabel.setText("DANH SÁCH FRAMES (" + count + " frames)");

         for (int i = 0; i < count; i++) {
            this.listModel.addElement(i);
         }
      } else {
         this.headerLabel.setText("DANH SÁCH FRAMES (0)");
      }

      if (this.selectedFrameIdx >= this.listModel.size()) {
         this.selectedFrameIdx = Math.max(0, this.listModel.size() - 1);
      }

      this.selectFrame(this.selectedFrameIdx);
   }

   public void selectFrame(int frameIdx) {
      this.selectedFrameIdx = frameIdx;
      if (frameIdx >= 0 && frameIdx < this.listModel.size()) {
         this.jList.setSelectedIndex(frameIdx);
         this.jList.scrollRectToVisible(this.jList.getCellBounds(frameIdx, frameIdx));
      }
   }

   public int getSelectedFrameIdx() {
      return this.selectedFrameIdx;
   }

   private class FrameCatalogCellRenderer extends DefaultListCellRenderer {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         JPanel cell = new JPanel(new BorderLayout(0, 2));
         cell.setOpaque(true);
         cell.setBackground(isSelected ? EffectFrameCatalog.BG_SELECTED : EffectFrameCatalog.BG_CARD);
         cell.setBorder(
            BorderFactory.createCompoundBorder(
               BorderFactory.createLineBorder(isSelected ? EffectFrameCatalog.BORDER_SEL : EffectFrameCatalog.BORDER_NORM, isSelected ? 2 : 1),
               new EmptyBorder(4, 4, 4, 4)
            )
         );
         final int frameIdx = (Integer)value;
         final BufferedImage thumb = EffectFrameCatalog.this.thumbs != null && frameIdx < EffectFrameCatalog.this.thumbs.size()
            ? EffectFrameCatalog.this.thumbs.get(frameIdx)
            : null;
         JLabel imgLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D)g;
               g2.setColor(new Color(12, 12, 16));
               g2.fillRoundRect(0, 0, 68, 68, 4, 4);
               if (thumb != null) {
                  g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                  g2.drawImage(thumb, 0, 0, 68, 68, null);
               }

               g2.setColor(new Color(0, 0, 0, 160));
               g2.fillRoundRect(0, 0, 26, 14, 3, 3);
               g2.setFont(new Font("Segoe UI", 1, 9));
               g2.setColor(Color.WHITE);
               g2.drawString("F" + frameIdx, 3, 10);
            }

            @Override
            public Dimension getPreferredSize() {
               return new Dimension(68, 68);
            }
         };
         cell.add(imgLabel, "Center");
         int parts = 0;
         if (EffectFrameCatalog.this.model != null && EffectFrameCatalog.this.model.frames != null && frameIdx < EffectFrameCatalog.this.model.frames.length) {
            EffFrame fr = EffectFrameCatalog.this.model.frames[frameIdx];
            if (fr != null) {
               parts = fr.getTotalParts();
            }
         }

         JLabel infoLbl = new JLabel(parts + " parts");
         infoLbl.setFont(new Font("Segoe UI", 0, 9));
         infoLbl.setForeground(isSelected ? Color.WHITE : EffectFrameCatalog.TEXT_DIM);
         infoLbl.setHorizontalAlignment(0);
         cell.add(infoLbl, "South");
         return cell;
      }
   }

   public interface OnFrameCatalogListener {
      void onFrameSelected(int var1);

      void onAddBlankFrame();

      void onDuplicateFrame(int var1);

      void onDeleteFrame(int var1);

      void onInsertFrameToSequence(int var1);

      void onReorderFrame(int var1, int var2);

      default void onAutoSequence() {
      }

      default void onOpenSequenceEditor() {
      }
   }
}
