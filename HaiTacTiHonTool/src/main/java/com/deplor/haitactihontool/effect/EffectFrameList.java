package com.deplor.haitactihontool.effect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class EffectFrameList extends JPanel {
   private EffectFrameList.OnFrameDoubleClickListener doubleClickListener;
   private static final Color BG_PANEL = new Color(16, 16, 20);
   private static final Color BG_CELL = new Color(24, 24, 30);
   private static final Color BG_SELECTED = new Color(0, 90, 200);
   private static final Color FG_NORMAL = new Color(170, 170, 190);
   private static final Color FG_SELECTED = Color.WHITE;
   private static final Color BORDER_SEL = new Color(0, 140, 255);
   private static final Color BORDER_NORM = new Color(40, 40, 50);
   private static final int THUMB_W = 70;
   private static final int THUMB_H = 70;
   private static final int CELL_PAD = 6;
   private EffectModel model;
   private List<BufferedImage> thumbs;
   private int selectedSeqPos = 0;
   private EffectFrameList.OnFrameSelectedListener listener;
   private EffectFrameList.OnFrameActionListener actionListener;
   private final DefaultListModel<Integer> listModel = new DefaultListModel<>();
   private final JList<Integer> jList;
   private final JLabel headerLabel;

   public void setDoubleClickListener(EffectFrameList.OnFrameDoubleClickListener l) {
      this.doubleClickListener = l;
   }

   public EffectFrameList() {
      this.setLayout(new BorderLayout(0, 0));
      this.setBackground(BG_PANEL);
      this.setPreferredSize(new Dimension(0, 130));
      this.headerLabel = new JLabel("DANH SÁCH KHUNG HÌNH (FRAMES)");
      this.headerLabel.setFont(new Font("Segoe UI", 1, 10));
      this.headerLabel.setForeground(new Color(100, 100, 130));
      this.headerLabel.setBorder(new EmptyBorder(4, 8, 4, 0));
      this.add(this.headerLabel, "North");
      this.jList = new JList<>(this.listModel);
      this.jList.setBackground(BG_PANEL);
      this.jList.setForeground(FG_NORMAL);
      this.jList.setSelectionBackground(BG_SELECTED);
      this.jList.setSelectionForeground(FG_SELECTED);
      this.jList.setFixedCellWidth(82);
      this.jList.setFixedCellHeight(98);
      this.jList.setLayoutOrientation(2);
      this.jList.setVisibleRowCount(1);
      this.jList.setCellRenderer(new EffectFrameList.FrameCellRenderer());
      this.jList.setBorder(null);
      this.jList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int idx = this.jList.getSelectedIndex();
            if (idx >= 0) {
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
            if (e.getClickCount() == 2) {
               int idx = EffectFrameList.this.jList.locationToIndex(e.getPoint());
               if (idx >= 0 && EffectFrameList.this.doubleClickListener != null) {
                  EffectFrameList.this.doubleClickListener.onFrameDoubleClicked(idx);
               }
            } else if (SwingUtilities.isRightMouseButton(e)) {
               int idx = EffectFrameList.this.jList.locationToIndex(e.getPoint());
               if (idx >= 0) {
                  EffectFrameList.this.jList.setSelectedIndex(idx);
                  EffectFrameList.this.selectedSeqPos = idx;
                  EffectFrameList.this.showContextMenu(e.getComponent(), e.getX(), e.getY());
               }
            }
         }
      });
      this.jList.setDragEnabled(true);
      this.jList.setDropMode(DropMode.INSERT);
      this.jList.setTransferHandler(new EffectFrameList.ListTransferHandler());
      JScrollPane scroll = new JScrollPane(this.jList);
      scroll.setBorder(null);
      scroll.setBackground(BG_PANEL);
      scroll.getViewport().setBackground(BG_PANEL);
      scroll.getVerticalScrollBar().setUnitIncrement(16);
      this.add(scroll, "Center");
      JPanel actionPnl = new JPanel(new GridLayout(2, 3, 2, 2));
      actionPnl.setBackground(BG_PANEL);
      actionPnl.setBorder(
         BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(35, 35, 45)), BorderFactory.createEmptyBorder(4, 4, 4, 4))
      );
      actionPnl.setPreferredSize(new Dimension(175, 0));
      JButton btnAdd = this.actionBtn("+ Thêm");
      btnAdd.setToolTipText("Thêm/Chèn Frame mới hoặc sao chép");
      btnAdd.addActionListener(e -> this.showAddMenu(btnAdd));
      JButton btnAuto = this.actionBtn("⚡ Auto");
      btnAuto.setToolTipText("Tự động áp dụng Sequence 1:1 theo frames");
      btnAuto.addActionListener(e -> {
         if (this.model != null) {
            this.model.autoGenerateSequenceLinear();
            if (this.actionListener != null) {
               this.actionListener.onAutoSequence();
            }
         }
      });
      JButton btnEditSeq = this.actionBtn("\ud83d\udcdd Sửa");
      btnEditSeq.setToolTipText("Mở bảng sửa toàn bộ chuỗi Sequence");
      btnEditSeq.addActionListener(e -> this.promptSequenceEditor());
      JButton btnEditTgt = this.actionBtn("Sửa F");
      btnEditTgt.setToolTipText("Sửa Target Frame ID");
      btnEditTgt.addActionListener(e -> this.doEditTarget());
      JButton btnDel = this.actionBtn("Xóa");
      btnDel.setToolTipText("Xóa Frame đang chọn");
      btnDel.addActionListener(e -> this.doDelete());
      actionPnl.add(btnAdd);
      actionPnl.add(btnAuto);
      actionPnl.add(btnEditSeq);
      actionPnl.add(btnEditTgt);
      actionPnl.add(btnDel);
      this.add(actionPnl, "East");
   }

   public void promptSequenceEditor() {
      if (this.model != null) {
         Window owner = SwingUtilities.getWindowAncestor(this);
         SequenceEditorDialog dlg = new SequenceEditorDialog(owner, this.model, newSeq -> {
            if (this.actionListener != null) {
               this.actionListener.onApplySequence(newSeq);
            }
         });
         dlg.setVisible(true);
      }
   }

   private void showContextMenu(Component c, int x, int y) {
      JPopupMenu menu = new JPopupMenu();
      menu.setBackground(new Color(30, 30, 38));
      JMenuItem iDup = new JMenuItem("⎘ Nhân bản Frame này");
      iDup.addActionListener(e -> {
         if (this.actionListener != null) {
            this.actionListener.onAddFrame(this.selectedSeqPos, true);
         }
      });
      JMenuItem iNew = new JMenuItem("➕ Tạo Frame Rỗng mới");
      iNew.addActionListener(e -> {
         if (this.actionListener != null) {
            this.actionListener.onAddFrame(this.selectedSeqPos, false);
         }
      });
      JMenuItem iAuto = new JMenuItem("⚡ Tự động Sequence 1:1 (0..N-1)");
      iAuto.addActionListener(e -> {
         if (this.model != null) {
            this.model.autoGenerateSequenceLinear();
            if (this.actionListener != null) {
               this.actionListener.onAutoSequence();
            }
         }
      });
      JMenuItem iEditSeq = new JMenuItem("\ud83d\udcdd Mở bảng sửa chuỗi Sequence...");
      iEditSeq.addActionListener(e -> this.promptSequenceEditor());
      JMenuItem iEditTgt = new JMenuItem("⚙ Đổi Frame đích...");
      iEditTgt.addActionListener(e -> this.doEditTarget());
      JMenuItem iDel = new JMenuItem("✖ Xóa bước này");
      iDel.addActionListener(e -> this.doDelete());
      menu.add(iDup);
      menu.add(iNew);
      menu.addSeparator();
      menu.add(iAuto);
      menu.add(iEditSeq);
      menu.add(iEditTgt);
      menu.addSeparator();
      menu.add(iDel);
      menu.show(c, x, y);
   }

   private JButton actionBtn(String txt) {
      final JButton b = new JButton(txt);
      b.setUI(new BasicButtonUI());
      b.setFont(new Font("Segoe UI", 1, 12));
      b.setFocusPainted(false);
      b.setBackground(new Color(30, 30, 38));
      b.setForeground(new Color(180, 180, 200));
      b.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 65)));
      b.setMargin(new Insets(2, 2, 2, 2));
      b.setCursor(new Cursor(12));
      b.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            b.setBackground(new Color(40, 40, 55));
            b.setForeground(Color.WHITE);
         }

         @Override
         public void mouseExited(MouseEvent e) {
            b.setBackground(new Color(30, 30, 38));
            b.setForeground(new Color(180, 180, 200));
         }
      });
      return b;
   }

   private void showAddMenu(Component c) {
      if (this.actionListener != null) {
         JPopupMenu menu = new JPopupMenu();
         menu.setBackground(new Color(30, 30, 38));
         JMenuItem i1 = new JMenuItem("Sao chép Frame hiện tại");
         i1.addActionListener(e -> this.actionListener.onAddFrame(this.selectedSeqPos, true));
         JMenuItem i2 = new JMenuItem("Tạo Frame Rỗng mới hoàn toàn");
         i2.addActionListener(e -> this.actionListener.onAddFrame(this.selectedSeqPos, false));

         for (JMenuItem mi : new JMenuItem[]{i1, i2}) {
            mi.setForeground(new Color(200, 200, 210));
            mi.setBackground(new Color(30, 30, 38));
            menu.add(mi);
         }

         menu.show(c, 0, -menu.getPreferredSize().height);
      }
   }

   private void doDelete() {
      if (this.actionListener != null && this.listModel.size() > 0) {
         int ans = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn Xóa bước Sequence " + this.selectedSeqPos + " này không?", "Xác nhận", 0, 2);
         if (ans == 0) {
            this.actionListener.onDeleteFrame(this.selectedSeqPos);
         }
      }
   }

   private void doEditTarget() {
      if (this.actionListener != null && this.model != null) {
         int frameIdx = this.model.resolveSeqFrame(this.selectedSeqPos);
         String input = JOptionPane.showInputDialog(
            this, "Nhập index Target Frame mới cho Sequence " + this.selectedSeqPos + ":\n(Hiện tại: Frame " + frameIdx + ")", frameIdx
         );
         if (input != null) {
            try {
               int val = Integer.parseInt(input.trim());
               if (val >= 0 && val < this.model.getFrameCount()) {
                  this.actionListener.onChangeFrameTarget(this.selectedSeqPos, val);
               } else {
                  JOptionPane.showMessageDialog(this, "Lỗi: Index frame " + val + " không tồn tại trong Effect Model!", "Lỗi", 0);
               }
            } catch (Exception var4) {
            }
         }
      }
   }

   public void setActionListener(EffectFrameList.OnFrameActionListener l) {
      this.actionListener = l;
   }

   public void setListener(EffectFrameList.OnFrameSelectedListener l) {
      this.listener = l;
   }

   public void setModel(EffectModel model, List<BufferedImage> thumbs) {
      this.model = model;
      this.thumbs = thumbs;
      this.listModel.clear();
      if (model != null && model.sequence != null) {
         this.headerLabel.setText("DANH SÁCH KHUNG HÌNH (" + model.getSeqLen() + ")");

         for (int i = 0; i < model.getSeqLen(); i++) {
            this.listModel.addElement(i);
         }
      } else {
         this.headerLabel.setText("DANH SÁCH KHUNG HÌNH");
      }

      this.selectPos(0);
   }

   public void selectPos(int seqPos) {
      this.selectedSeqPos = seqPos;
      if (seqPos >= 0 && seqPos < this.listModel.size()) {
         this.jList.setSelectedIndex(seqPos);
         this.jList.scrollRectToVisible(this.jList.getCellBounds(seqPos, seqPos));
      }
   }

   private class FrameCellRenderer extends DefaultListCellRenderer {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, final boolean isSelected, boolean cellHasFocus) {
         JPanel cell = new JPanel(new BorderLayout(0, 3));
         cell.setOpaque(true);
         cell.setBackground(isSelected ? EffectFrameList.BG_SELECTED : EffectFrameList.BG_CELL);
         cell.setBorder(
            BorderFactory.createCompoundBorder(
               BorderFactory.createMatteBorder(0, 0, 1, 0, isSelected ? EffectFrameList.BORDER_SEL : EffectFrameList.BORDER_NORM), new EmptyBorder(6, 6, 6, 6)
            )
         );
         final int seqIdx = (Integer)value;
         final int frameIdx = EffectFrameList.this.model != null ? EffectFrameList.this.model.resolveSeqFrame(seqIdx) : seqIdx;
         final BufferedImage thumb = EffectFrameList.this.thumbs != null && frameIdx < EffectFrameList.this.thumbs.size()
            ? EffectFrameList.this.thumbs.get(frameIdx)
            : null;
         JLabel imgLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D)g;
               g2.setColor(new Color(12, 12, 16));
               g2.fillRoundRect(0, 0, 70, 70, 4, 4);
               if (thumb != null) {
                  g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                  g2.drawImage(thumb, 0, 0, 70, 70, null);
               }

               g2.setColor(new Color(0, 0, 0, 140));
               g2.fillRoundRect(0, 54, 70, 16, 3, 3);
               g2.setFont(new Font("Segoe UI", 1, 9));
               g2.setColor(isSelected ? Color.WHITE : new Color(180, 180, 200));
               String lbl = "S" + seqIdx + " F" + frameIdx;
               FontMetrics fm = g2.getFontMetrics();
               g2.drawString(lbl, (70 - fm.stringWidth(lbl)) / 2, 66);
            }

            @Override
            public Dimension getPreferredSize() {
               return new Dimension(70, 70);
            }
         };
         cell.add(imgLabel, "Center");
         int parts = 0;
         if (EffectFrameList.this.model != null && EffectFrameList.this.model.frames != null) {
            EffFrame fr = EffectFrameList.this.model.frames[frameIdx];
            if (fr != null) {
               parts = fr.getTotalParts();
            }
         }

         JLabel infoLbl = new JLabel(parts + " parts");
         infoLbl.setFont(new Font("Segoe UI", 0, 9));
         infoLbl.setForeground(isSelected ? EffectFrameList.FG_SELECTED : new Color(100, 100, 130));
         infoLbl.setHorizontalAlignment(0);
         cell.add(infoLbl, "South");
         return cell;
      }
   }

   private class ListTransferHandler extends TransferHandler {
      private int index = -1;

      @Override
      public int getSourceActions(JComponent c) {
         return 2;
      }

      @Override
      protected Transferable createTransferable(JComponent c) {
         this.index = EffectFrameList.this.jList.getSelectedIndex();
         return new StringSelection(String.valueOf(this.index));
      }

      @Override
      public boolean canImport(TransferSupport support) {
         return support.isDrop() && support.isDataFlavorSupported(DataFlavor.stringFlavor);
      }

      @Override
      public boolean importData(TransferSupport support) {
         if (!this.canImport(support)) {
            return false;
         } else {
            JList.DropLocation dl = (JList.DropLocation)support.getDropLocation();
            int dropIndex = dl.getIndex();
            if (this.index != -1 && dropIndex != -1 && this.index != dropIndex) {
               int target = dropIndex;
               if (dropIndex > this.index) {
                  target = dropIndex - 1;
               }

               if (EffectFrameList.this.actionListener != null) {
                  EffectFrameList.this.actionListener.onReorderFrameSequence(this.index, target);
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public interface OnFrameActionListener {
      void onAddFrame(int var1, boolean var2);

      void onDeleteFrame(int var1);

      void onChangeFrameTarget(int var1, int var2);

      void onReorderFrameSequence(int var1, int var2);

      default void onApplySequence(int[] newSequence) {
      }

      default void onAutoSequence() {
      }
   }

   public interface OnFrameDoubleClickListener {
      void onFrameDoubleClicked(int var1);
   }

   public interface OnFrameSelectedListener {
      void onSeqPosSelected(int var1);
   }
}
