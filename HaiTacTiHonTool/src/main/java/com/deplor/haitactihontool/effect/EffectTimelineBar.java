package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class EffectTimelineBar extends JPanel {
   private static final Color BG_BOTTOM = new Color(18, 18, 24);
   private static final Color BG_CARD = new Color(26, 26, 34);
   private static final Color BG_SELECTED = new Color(0, 110, 235);
   private static final Color BORDER_NORM = new Color(42, 42, 56);
   private static final Color BORDER_SEL = new Color(0, 180, 255);
   private static final Color TEXT_MAIN = new Color(210, 210, 225);
   private static final Color TEXT_DIM = new Color(130, 130, 150);
   private static final Color BTN_BG = new Color(28, 28, 38);
   private static final int STEP_THUMB_W = 54;
   private static final int STEP_THUMB_H = 54;
   private EffectModel model;
   private List<BufferedImage> thumbs;
   private int selectedSeqPos = 0;
   private boolean isPlaying = false;
   private int animInterval = 120;
   private EffectTimelineBar.OnTimelineActionListener listener;
   private final DefaultListModel<Integer> listModel = new DefaultListModel<>();
   private final JList<Integer> jList;
   private final JLabel seqInfoLabel;
   private final JLabel playBtn;
   private final JSlider speedSlider;
   private final EffectTimelineBar.ScrubBar scrubBar;

   public EffectTimelineBar() {
      this.setLayout(new BorderLayout(6, 0));
      this.setBackground(BG_BOTTOM);
      this.setBorder(new MatteBorder(1, 0, 0, 0, new Color(38, 38, 52)));
      this.setPreferredSize(new Dimension(0, 115));
      JPanel leftControls = new JPanel(new FlowLayout(0, 4, 6));
      leftControls.setOpaque(false);
      leftControls.setPreferredSize(new Dimension(320, 0));
      JLabel btnFirst = this.navBtn("Đầu", "Về bước đầu tiên");
      JLabel btnBack = this.navBtn("Lùi", "Lùi 1 bước");
      this.playBtn = this.navBtn("Phát", "Phát / Tạm dừng Hoạt họa (Space)");
      this.playBtn.setBackground(Theme.ACCENT);
      this.playBtn.setForeground(Color.WHITE);
      JLabel btnFwd = this.navBtn("Tiến", "Tiến 1 bước");
      JLabel btnLast = this.navBtn("Cuối", "Đến bước cuối");
      btnFirst.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (EffectTimelineBar.this.listener != null) {
               EffectTimelineBar.this.listener.onJumpSeq(0);
            }
         }
      });
      btnBack.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (EffectTimelineBar.this.listener != null) {
               EffectTimelineBar.this.listener.onStepSeq(-1);
            }
         }
      });
      this.playBtn.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (EffectTimelineBar.this.listener != null) {
               EffectTimelineBar.this.listener.onTogglePlay();
            }
         }
      });
      btnFwd.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (EffectTimelineBar.this.listener != null) {
               EffectTimelineBar.this.listener.onStepSeq(1);
            }
         }
      });
      btnLast.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (EffectTimelineBar.this.listener != null && EffectTimelineBar.this.model != null) {
               EffectTimelineBar.this.listener.onJumpSeq(EffectTimelineBar.this.model.getSeqLen() - 1);
            }
         }
      });
      leftControls.add(btnFirst);
      leftControls.add(btnBack);
      leftControls.add(this.playBtn);
      leftControls.add(btnFwd);
      leftControls.add(btnLast);
      leftControls.add(vsep());
      JLabel lblSpd = new JLabel("Tốc độ:");
      lblSpd.setFont(new Font("Segoe UI", 0, 10));
      lblSpd.setForeground(TEXT_DIM);
      leftControls.add(lblSpd);
      this.speedSlider = new JSlider(30, 500, this.animInterval);
      this.speedSlider.setOpaque(false);
      this.speedSlider.setPreferredSize(new Dimension(80, 22));
      this.speedSlider.addChangeListener(e -> {
         this.animInterval = 530 - this.speedSlider.getValue();
         if (this.listener != null) {
            this.listener.onSpeedChanged(this.animInterval);
         }
      });
      leftControls.add(this.speedSlider);
      this.seqInfoLabel = new JLabel("Seq: —");
      this.seqInfoLabel.setFont(new Font("Segoe UI", 1, 10));
      this.seqInfoLabel.setForeground(Theme.ACCENT);
      leftControls.add(this.seqInfoLabel);
      this.add(leftControls, "West");
      JPanel centerPanel = new JPanel(new BorderLayout(0, 2));
      centerPanel.setOpaque(false);
      this.jList = new JList<>(this.listModel);
      this.jList.setBackground(BG_BOTTOM);
      this.jList.setForeground(TEXT_MAIN);
      this.jList.setSelectionBackground(BG_SELECTED);
      this.jList.setSelectionForeground(Color.WHITE);
      this.jList.setFixedCellWidth(66);
      this.jList.setFixedCellHeight(76);
      this.jList.setLayoutOrientation(2);
      this.jList.setVisibleRowCount(1);
      this.jList.setCellRenderer(new EffectTimelineBar.SeqStepCellRenderer());
      this.jList.setBorder(null);
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
      this.jList.setDragEnabled(true);
      this.jList.setDropMode(DropMode.INSERT);
      this.jList.setTransferHandler(new EffectTimelineBar.ListTransferHandler());
      JScrollPane scroll = new JScrollPane(this.jList);
      scroll.setBorder(null);
      scroll.setBackground(BG_BOTTOM);
      scroll.getViewport().setBackground(BG_BOTTOM);
      scroll.getHorizontalScrollBar().setUnitIncrement(20);
      this.scrubBar = new EffectTimelineBar.ScrubBar();
      centerPanel.add(scroll, "Center");
      centerPanel.add(this.scrubBar, "South");
      this.add(centerPanel, "Center");
      JPanel rightActions = new JPanel(new GridLayout(2, 3, 3, 3));
      rightActions.setOpaque(false);
      rightActions.setBorder(
         BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(38, 38, 52)), new EmptyBorder(4, 6, 4, 6))
      );
      rightActions.setPreferredSize(new Dimension(240, 0));
      JButton btnAutoSeq = this.actionBtn("Auto Seq", new Color(0, 130, 200));
      btnAutoSeq.setToolTipText("Tự động áp dụng Sequence tuần tự theo danh sách Frame");
      btnAutoSeq.addActionListener(e -> this.promptAutoSequenceMenu(btnAutoSeq));
      JButton btnEditSeqStr = this.actionBtn("Sửa Chuỗi", new Color(110, 60, 170));
      btnEditSeqStr.setToolTipText("Mở bảng chỉnh sửa toàn bộ chuỗi Sequence bằng chuỗi số tùy ý");
      btnEditSeqStr.addActionListener(e -> this.promptSequenceEditor());
      JButton btnAddStep = this.actionBtn("Thêm", Theme.ACCENT);
      btnAddStep.setToolTipText("Thêm một bước vào Timeline Sequence");
      btnAddStep.addActionListener(e -> {
         if (this.listener != null && this.model != null) {
            int curF = this.model.resolveSeqFrame(this.selectedSeqPos);
            this.listener.onAddSeqStep(this.selectedSeqPos, curF);
         }
      });
      JButton btnEditTarget = this.actionBtn("Đổi Frame", new Color(38, 38, 52));
      btnEditTarget.setToolTipText("Đổi Frame đích của bước Sequence đang chọn");
      btnEditTarget.addActionListener(e -> this.promptEditTarget());
      JButton btnDelStep = this.actionBtn("Xóa", new Color(180, 50, 50));
      btnDelStep.setToolTipText("Xóa bước Sequence đang chọn khỏi Timeline");
      btnDelStep.addActionListener(e -> {
         if (this.listener != null && this.model != null && this.model.getSeqLen() > 1) {
            this.listener.onDeleteSeqStep(this.selectedSeqPos);
         }
      });
      JButton btnPingPong = this.actionBtn("Ping-Pong", new Color(40, 120, 80));
      btnPingPong.setToolTipText("Áp dụng chuỗi chuyển động qua lại Ping-Pong");
      btnPingPong.addActionListener(e -> {
         if (this.model != null) {
            this.model.autoGenerateSequencePingPong();
            if (this.listener != null) {
               this.listener.onApplySequence(this.model.sequence);
            }
         }
      });
      rightActions.add(btnAutoSeq);
      rightActions.add(btnEditSeqStr);
      rightActions.add(btnAddStep);
      rightActions.add(btnPingPong);
      rightActions.add(btnEditTarget);
      rightActions.add(btnDelStep);
      this.add(rightActions, "East");
   }

   private void promptAutoSequenceMenu(Component source) {
      if (this.model != null && this.model.getFrameCount() != 0) {
         JPopupMenu menu = new JPopupMenu();
         menu.setBackground(new Color(28, 28, 38));
         JMenuItem iLinear = new JMenuItem("1:1 Tuần tự (0, 1, 2... " + (this.model.getFrameCount() - 1) + ")");
         iLinear.addActionListener(e -> {
            this.model.autoGenerateSequenceLinear();
            if (this.listener != null) {
               this.listener.onApplySequence(this.model.sequence);
            }
         });
         JMenuItem iHold2 = new JMenuItem("Lặp đôi 2x (0, 0, 1, 1, 2, 2...)");
         iHold2.addActionListener(e -> {
            this.model.autoGenerateSequenceHold(2);
            if (this.listener != null) {
               this.listener.onApplySequence(this.model.sequence);
            }
         });
         JMenuItem iHold3 = new JMenuItem("Lặp 3x (0, 0, 0, 1, 1, 1...)");
         iHold3.addActionListener(e -> {
            this.model.autoGenerateSequenceHold(3);
            if (this.listener != null) {
               this.listener.onApplySequence(this.model.sequence);
            }
         });
         JMenuItem iPingPong = new JMenuItem("Ping-Pong (0..N-1..1)");
         iPingPong.addActionListener(e -> {
            this.model.autoGenerateSequencePingPong();
            if (this.listener != null) {
               this.listener.onApplySequence(this.model.sequence);
            }
         });
         JMenuItem iReverse = new JMenuItem("Đảo ngược (" + (this.model.getFrameCount() - 1) + "..0)");
         iReverse.addActionListener(e -> {
            this.model.autoGenerateSequenceReverse();
            if (this.listener != null) {
               this.listener.onApplySequence(this.model.sequence);
            }
         });
         menu.add(iLinear);
         menu.add(iHold2);
         menu.add(iHold3);
         menu.add(iPingPong);
         menu.add(iReverse);
         menu.show(source, 0, source.getHeight());
      }
   }

   public void promptSequenceEditor() {
      if (this.model != null) {
         Window owner = SwingUtilities.getWindowAncestor(this);
         SequenceEditorDialog dlg = new SequenceEditorDialog(owner, this.model, newSeq -> {
            if (this.listener != null) {
               this.listener.onApplySequence(newSeq);
            }
         });
         dlg.setVisible(true);
      }
   }

   private void promptEditTarget() {
      if (this.model != null && this.model.getSeqLen() != 0 && this.model.getFrameCount() != 0) {
         int currentFrame = this.model.resolveSeqFrame(this.selectedSeqPos);
         Integer[] framesList = new Integer[this.model.getFrameCount()];

         for (int i = 0; i < this.model.getFrameCount(); i++) {
            framesList[i] = i;
         }

         JComboBox<Integer> cb = new JComboBox<>(framesList);
         cb.setSelectedItem(currentFrame);
         cb.setBackground(new Color(30, 30, 40));
         cb.setForeground(Color.WHITE);
         int res = JOptionPane.showConfirmDialog(this, cb, "Chọn Frame đích cho Sequence #" + (this.selectedSeqPos + 1) + ":", 2, 3);
         if (res == 0 && cb.getSelectedItem() != null) {
            int newTarget = (Integer)cb.getSelectedItem();
            if (this.listener != null) {
               this.listener.onChangeSeqTarget(this.selectedSeqPos, newTarget);
            }
         }
      }
   }

   private JLabel navBtn(String icon, String tip) {
      final JLabel l = new JLabel(icon);
      l.setFont(new Font("Segoe UI", 1, 10));
      l.setForeground(TEXT_MAIN);
      l.setCursor(new Cursor(12));
      l.setOpaque(true);
      l.setBackground(BTN_BG);
      l.setHorizontalAlignment(0);
      l.setToolTipText(tip);
      l.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(48, 48, 62), 1), new EmptyBorder(3, 8, 3, 8)));
      l.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            if (!l.equals(EffectTimelineBar.this.playBtn) || !EffectTimelineBar.this.isPlaying) {
               l.setBackground(new Color(45, 45, 60));
            }
         }

         @Override
         public void mouseExited(MouseEvent e) {
            if (!l.equals(EffectTimelineBar.this.playBtn) || !EffectTimelineBar.this.isPlaying) {
               l.setBackground(EffectTimelineBar.BTN_BG);
            }
         }
      });
      return l;
   }

   private JButton actionBtn(String txt, final Color bg) {
      final JButton b = new JButton(txt);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setForeground(Color.WHITE);
      b.setBackground(bg);
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 55, 75), 1), new EmptyBorder(3, 6, 3, 6)));
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

   private static JSeparator vsep() {
      JSeparator s = new JSeparator(1);
      s.setPreferredSize(new Dimension(1, 20));
      s.setForeground(new Color(45, 45, 60));
      return s;
   }

   public void setListener(EffectTimelineBar.OnTimelineActionListener l) {
      this.listener = l;
   }

   public void setPlaying(boolean playing) {
      this.isPlaying = playing;
      if (playing) {
         this.playBtn.setText("Dừng");
         this.playBtn.setBackground(new Color(200, 100, 0));
      } else {
         this.playBtn.setText("Phát");
         this.playBtn.setBackground(Theme.ACCENT);
      }
   }

   public void setModel(EffectModel model, List<BufferedImage> thumbs) {
      this.model = model;
      this.thumbs = thumbs;
      this.listModel.clear();
      if (model != null && model.sequence != null) {
         for (int i = 0; i < model.sequence.length; i++) {
            this.listModel.addElement(i);
         }
      }

      if (this.selectedSeqPos >= this.listModel.size()) {
         this.selectedSeqPos = Math.max(0, this.listModel.size() - 1);
      }

      this.selectPos(this.selectedSeqPos);
      this.scrubBar.repaint();
   }

   public void selectPos(int pos) {
      this.selectedSeqPos = pos;
      if (pos >= 0 && pos < this.listModel.size()) {
         this.jList.setSelectedIndex(pos);
         this.jList.scrollRectToVisible(this.jList.getCellBounds(pos, pos));
      }

      this.updateInfoLabel();
      this.scrubBar.repaint();
   }

   private void updateInfoLabel() {
      if (this.model != null && this.model.getSeqLen() != 0) {
         int curF = this.model.resolveSeqFrame(this.selectedSeqPos);
         this.seqInfoLabel.setText(String.format("S%d/%d → F%d", this.selectedSeqPos + 1, this.model.getSeqLen(), curF));
      } else {
         this.seqInfoLabel.setText("Seq: —");
      }
   }

   private class ListTransferHandler extends TransferHandler {
      private int fromIndex = -1;

      @Override
      public int getSourceActions(JComponent c) {
         return 2;
      }

      @Override
      protected Transferable createTransferable(JComponent c) {
         this.fromIndex = EffectTimelineBar.this.jList.getSelectedIndex();
         return new StringSelection(String.valueOf(this.fromIndex));
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
            if (this.fromIndex != -1 && dropIndex != -1 && this.fromIndex != dropIndex) {
               int target = dropIndex;
               if (dropIndex > this.fromIndex) {
                  target = dropIndex - 1;
               }

               if (EffectTimelineBar.this.listener != null) {
                  EffectTimelineBar.this.listener.onReorderSeq(this.fromIndex, target);
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public interface OnTimelineActionListener {
      void onSeqPosSelected(int var1);

      void onAddSeqStep(int var1, int var2);

      void onDeleteSeqStep(int var1);

      void onChangeSeqTarget(int var1, int var2);

      void onReorderSeq(int var1, int var2);

      default void onApplySequence(int[] newSequence) {
      }

      void onTogglePlay();

      void onStepSeq(int var1);

      void onJumpSeq(int var1);

      void onSpeedChanged(int var1);
   }

   private class ScrubBar extends JPanel {
      ScrubBar() {
         this.setOpaque(false);
         this.setPreferredSize(new Dimension(0, 16));
         this.setCursor(new Cursor(12));
         MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               this.scrub(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
               this.scrub(e);
            }

            private void scrub(MouseEvent e) {
               if (EffectTimelineBar.this.model != null && EffectTimelineBar.this.model.getSeqLen() != 0) {
                  int len = EffectTimelineBar.this.model.getSeqLen();
                  int w = ScrubBar.this.getWidth() - 10;
                  if (w > 0) {
                     int clickX = Math.max(0, Math.min(e.getX() - 5, w));
                     int pos = (int)((double)clickX / w * len);
                     pos = Math.max(0, Math.min(pos, len - 1));
                     if (EffectTimelineBar.this.listener != null) {
                        EffectTimelineBar.this.listener.onJumpSeq(pos);
                     }
                  }
               }
            }
         };
         this.addMouseListener(ma);
         this.addMouseMotionListener(ma);
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (EffectTimelineBar.this.model != null && EffectTimelineBar.this.model.getSeqLen() != 0) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int len = EffectTimelineBar.this.model.getSeqLen();
            int w = this.getWidth() - 10;
            int h = this.getHeight() - 6;
            int y0 = 3;
            g2.setColor(new Color(28, 28, 38));
            g2.fillRoundRect(5, y0, w, h, 4, 4);
            double cellW = (double)w / len;

            for (int i = 0; i < len; i++) {
               int x = 5 + (int)(i * cellW);
               int cw = Math.max(2, (int)cellW - 1);
               boolean isCur = i == EffectTimelineBar.this.selectedSeqPos;
               g2.setColor(isCur ? new Color(0, 200, 255) : new Color(0, 100, 180));
               g2.fillRoundRect(x, y0 + (isCur ? 0 : 2), cw, h - (isCur ? 0 : 4), 2, 2);
            }
         }
      }
   }

   private class SeqStepCellRenderer extends DefaultListCellRenderer {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, final boolean isSelected, boolean cellHasFocus) {
         JPanel cell = new JPanel(new BorderLayout(0, 2));
         cell.setOpaque(true);
         cell.setBackground(isSelected ? EffectTimelineBar.BG_SELECTED : EffectTimelineBar.BG_CARD);
         cell.setBorder(
            BorderFactory.createCompoundBorder(
               BorderFactory.createLineBorder(isSelected ? EffectTimelineBar.BORDER_SEL : EffectTimelineBar.BORDER_NORM, isSelected ? 2 : 1),
               new EmptyBorder(3, 3, 3, 3)
            )
         );
         final int seqIdx = (Integer)value;
         final int frameIdx = EffectTimelineBar.this.model != null ? EffectTimelineBar.this.model.resolveSeqFrame(seqIdx) : 0;
         final BufferedImage thumb = EffectTimelineBar.this.thumbs != null && frameIdx < EffectTimelineBar.this.thumbs.size()
            ? EffectTimelineBar.this.thumbs.get(frameIdx)
            : null;
         JLabel imgLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D)g;
               g2.setColor(new Color(12, 12, 16));
               g2.fillRoundRect(0, 0, 54, 54, 4, 4);
               if (thumb != null) {
                  g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                  g2.drawImage(thumb, 0, 0, 54, 54, null);
               }

               g2.setColor(new Color(0, 0, 0, 160));
               g2.fillRoundRect(0, 40, 54, 14, 3, 3);
               g2.setFont(new Font("Segoe UI", 1, 8));
               g2.setColor(isSelected ? Color.WHITE : new Color(200, 220, 255));
               String lbl = "S" + seqIdx + " ➔ F" + frameIdx;
               FontMetrics fm = g2.getFontMetrics();
               g2.drawString(lbl, (54 - fm.stringWidth(lbl)) / 2, 51);
            }

            @Override
            public Dimension getPreferredSize() {
               return new Dimension(54, 54);
            }
         };
         cell.add(imgLabel, "Center");
         return cell;
      }
   }
}
