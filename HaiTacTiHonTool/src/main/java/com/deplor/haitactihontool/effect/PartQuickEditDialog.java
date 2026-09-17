package com.deplor.haitactihontool.effect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class PartQuickEditDialog extends JDialog {
   private static final Color BG = new Color(18, 18, 26);
   private static final Color BG2 = new Color(24, 24, 34);
   private static final Color BG3 = new Color(30, 30, 42);
   private static final Color BORDER = new Color(45, 45, 62);
   private static final Color ACCENT = new Color(0, 140, 255);
   private static final Color TEXT_PRI = new Color(225, 225, 240);
   private static final Color TEXT_SEC = new Color(140, 140, 160);
   private final EffectModel model;
   private final EffFrame frame;
   private final int partIndex;
   private final EffPartFrame part;
   private final Consumer<EffPartFrame> onChange;
   private JSpinner spDx;
   private JSpinner spDy;
   private JSpinner spImgId;
   private JSpinner spRotate;
   private JSlider slideRotate;
   private JToggleButton btnFlip;
   private JToggleButton btnOnTop;
   private JLabel lblSpriteDim;
   private PartQuickEditDialog.PartThumbnailPanel thumbPanel;
   private boolean isUpdating = false;

   public static void showDialog(Component parent, EffectModel model, EffFrame frame, int partIndex, Consumer<EffPartFrame> onChange) {
      Window win = SwingUtilities.getWindowAncestor(parent);
      PartQuickEditDialog dlg = new PartQuickEditDialog(win, model, frame, partIndex, onChange);
      dlg.setVisible(true);
   }

   public PartQuickEditDialog(Window owner, EffectModel model, EffFrame frame, int partIndex, Consumer<EffPartFrame> onChange) {
      super(owner, "Chỉnh sửa Part #" + partIndex, ModalityType.MODELESS);
      this.model = model;
      this.frame = frame;
      this.partIndex = partIndex;
      this.part = frame != null && partIndex >= 0 && partIndex < frame.allParts.size() ? frame.allParts.get(partIndex) : new EffPartFrame();
      this.onChange = onChange;
      this.setResizable(false);
      this.setLayout(new BorderLayout());
      this.getContentPane().setBackground(BG);
      this.add(this.buildTitleBar(), "North");
      this.add(this.buildBody(), "Center");
      this.add(this.buildFooter(), "South");
      this.pack();
      this.setLocationRelativeTo(owner);
      this.initKeyShortcuts();
      this.syncValuesToUI();
   }

   private JPanel buildTitleBar() {
      JPanel bar = new JPanel(new BorderLayout());
      bar.setBackground(BG2);
      bar.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      bar.setPreferredSize(new Dimension(380, 38));
      JLabel title = new JLabel("  ✎ CHỈNH SỬA THÔNG SỐ PART #" + this.partIndex);
      title.setFont(new Font("Segoe UI", 1, 11));
      title.setForeground(ACCENT);
      bar.add(title, "West");
      JLabel sub = new JLabel("Frame hiện tại  ");
      sub.setFont(new Font("Segoe UI", 0, 10));
      sub.setForeground(TEXT_SEC);
      bar.add(sub, "East");
      return bar;
   }

   private JPanel buildBody() {
      JPanel body = new JPanel();
      body.setLayout(new BoxLayout(body, 1));
      body.setBackground(BG);
      body.setBorder(new EmptyBorder(10, 14, 10, 14));
      JPanel sprPanel = new JPanel(new BorderLayout(8, 0));
      sprPanel.setOpaque(false);
      sprPanel.setBorder(createTitledBorder("SPRITE ATLAS"));
      this.thumbPanel = new PartQuickEditDialog.PartThumbnailPanel();
      sprPanel.add(this.thumbPanel, "West");
      JPanel sprCtrl = new JPanel(new GridLayout(2, 1, 0, 4));
      sprCtrl.setOpaque(false);
      JPanel sprRow1 = new JPanel(new BorderLayout(4, 0));
      sprRow1.setOpaque(false);
      sprRow1.add(dimLabel("ID Sprite:"), "West");
      this.spImgId = makeSpinner(0, 9999);
      this.spImgId.addChangeListener(e -> {
         if (!this.isUpdating) {
            this.part.idSmallImg = (Integer)this.spImgId.getValue();
            this.updateSpriteDimLabel();
            this.thumbPanel.repaint();
            this.notifyChanged();
         }
      });
      JButton btnPrevSpr = makeNudgeBtn("◀", "Sprite trước");
      JButton btnNextSpr = makeNudgeBtn("▶", "Sprite kế tiếp");
      btnPrevSpr.addActionListener(e -> {
         int v = (Integer)this.spImgId.getValue();
         if (v > 0) {
            this.spImgId.setValue(v - 1);
         }
      });
      btnNextSpr.addActionListener(e -> {
         int v = (Integer)this.spImgId.getValue();
         if (this.model != null && this.model.smallImages != null && v < this.model.smallImages.length - 1) {
            this.spImgId.setValue(v + 1);
         }
      });
      JPanel spinHolder = new JPanel(new BorderLayout(2, 0));
      spinHolder.setOpaque(false);
      spinHolder.add(btnPrevSpr, "West");
      spinHolder.add(this.spImgId, "Center");
      spinHolder.add(btnNextSpr, "East");
      sprRow1.add(spinHolder, "Center");
      sprCtrl.add(sprRow1);
      this.lblSpriteDim = new JLabel("Kích thước: - x -");
      this.lblSpriteDim.setFont(new Font("Segoe UI", 0, 10));
      this.lblSpriteDim.setForeground(TEXT_SEC);
      sprCtrl.add(this.lblSpriteDim);
      sprPanel.add(sprCtrl, "Center");
      body.add(sprPanel);
      body.add(Box.createVerticalStrut(8));
      JPanel posPanel = new JPanel(new BorderLayout(0, 6));
      posPanel.setOpaque(false);
      posPanel.setBorder(createTitledBorder("TỌA ĐỘ VỊ TRÍ (dx, dy)"));
      JPanel xyGrid = new JPanel(new GridLayout(1, 2, 8, 0));
      xyGrid.setOpaque(false);
      JPanel xRow = new JPanel(new BorderLayout(4, 0));
      xRow.setOpaque(false);
      xRow.add(dimLabel("dx (X):"), "West");
      this.spDx = makeSpinner(-9999, 9999);
      this.spDx.addChangeListener(e -> {
         if (!this.isUpdating) {
            this.part.dx = (Integer)this.spDx.getValue();
            this.notifyChanged();
         }
      });
      xRow.add(this.spDx, "Center");
      JPanel yRow = new JPanel(new BorderLayout(4, 0));
      yRow.setOpaque(false);
      yRow.add(dimLabel("dy (Y):"), "West");
      this.spDy = makeSpinner(-9999, 9999);
      this.spDy.addChangeListener(e -> {
         if (!this.isUpdating) {
            this.part.dy = (Integer)this.spDy.getValue();
            this.notifyChanged();
         }
      });
      yRow.add(this.spDy, "Center");
      xyGrid.add(xRow);
      xyGrid.add(yRow);
      posPanel.add(xyGrid, "North");
      JPanel nudgePnl = new JPanel(new FlowLayout(1, 3, 0));
      nudgePnl.setOpaque(false);
      JButton btnL = makeNudgeBtn("◀ Trái", "Dịch trái 1px (Shift=10px, Ctrl=5px)");
      JButton btnU = makeNudgeBtn("▲ Lên", "Dịch lên 1px (Shift=10px, Ctrl=5px)");
      JButton btnD = makeNudgeBtn("▼ Xuống", "Dịch xuống 1px (Shift=10px, Ctrl=5px)");
      JButton btnR = makeNudgeBtn("▶ Phải", "Dịch phải 1px (Shift=10px, Ctrl=5px)");
      JButton btn0 = makeNudgeBtn("⟲ (0,0)", "Đặt về tọa độ (0, 0)");
      btnL.addActionListener(e -> this.applyNudge(-1, 0, e));
      btnU.addActionListener(e -> this.applyNudge(0, -1, e));
      btnD.addActionListener(e -> this.applyNudge(0, 1, e));
      btnR.addActionListener(e -> this.applyNudge(1, 0, e));
      btn0.addActionListener(e -> {
         this.spDx.setValue(0);
         this.spDy.setValue(0);
      });
      nudgePnl.add(btnL);
      nudgePnl.add(btnU);
      nudgePnl.add(btnD);
      nudgePnl.add(btnR);
      nudgePnl.add(btn0);
      posPanel.add(nudgePnl, "South");
      body.add(posPanel);
      body.add(Box.createVerticalStrut(8));
      JPanel rotPanel = new JPanel(new BorderLayout(0, 6));
      rotPanel.setOpaque(false);
      rotPanel.setBorder(createTitledBorder("GÓC NGHIÊNG / ROTATION ANGLE (ĐỘ)"));
      JPanel rotRow = new JPanel(new BorderLayout(6, 0));
      rotRow.setOpaque(false);
      rotRow.add(dimLabel("Góc:"), "West");
      this.spRotate = makeSpinner(-360, 360);
      this.spRotate.setPreferredSize(new Dimension(65, 22));
      this.spRotate.addChangeListener(e -> {
         if (!this.isUpdating) {
            int val = (Integer)this.spRotate.getValue();
            int norm = (val % 360 + 360) % 360;
            this.part.rotate = norm;
            this.isUpdating = true;
            this.slideRotate.setValue(norm);
            this.isUpdating = false;
            this.notifyChanged();
         }
      });
      rotRow.add(this.spRotate, "West");
      this.slideRotate = new JSlider(0, 360, 0);
      this.slideRotate.setOpaque(false);
      this.slideRotate.setBackground(BG);
      this.slideRotate.setForeground(ACCENT);
      this.slideRotate.addChangeListener(e -> {
         if (!this.isUpdating) {
            int val = this.slideRotate.getValue();
            this.part.rotate = val;
            this.isUpdating = true;
            this.spRotate.setValue(val);
            this.isUpdating = false;
            this.notifyChanged();
         }
      });
      rotRow.add(this.slideRotate, "Center");
      rotPanel.add(rotRow, "North");
      JPanel presetRotPnl = new JPanel(new GridLayout(1, 7, 3, 0));
      presetRotPnl.setOpaque(false);
      String[] angles = new String[]{"0°", "45°", "90°", "135°", "180°", "270°", "-90°"};
      int[] angleVals = new int[]{0, 45, 90, 135, 180, 270, 270};

      for (int i = 0; i < angles.length; i++) {
         int deg = angleVals[i];
         JButton b = makeNudgeBtn(angles[i], "Đặt góc " + angles[i]);
         b.addActionListener(e -> this.spRotate.setValue(deg));
         presetRotPnl.add(b);
      }

      rotPanel.add(presetRotPnl, "South");
      body.add(rotPanel);
      body.add(Box.createVerticalStrut(8));
      JPanel toggleGrid = new JPanel(new GridLayout(1, 2, 8, 0));
      toggleGrid.setOpaque(false);
      this.btnFlip = createToggle("↔ Lật Ngang (Mirror)", new Color(0, 120, 220));
      this.btnFlip.addItemListener(e -> {
         if (!this.isUpdating) {
            this.part.flip = this.btnFlip.isSelected() ? 1 : 0;
            this.notifyChanged();
         }
      });
      this.btnOnTop = createToggle("⬆ Vẽ Đè Lên (Top Layer)", new Color(140, 60, 200));
      this.btnOnTop.addItemListener(e -> {
         if (!this.isUpdating) {
            this.part.onTop = this.btnOnTop.isSelected() ? 1 : 0;
            if (this.frame != null) {
               this.frame.rebuildLayers();
            }

            this.notifyChanged();
         }
      });
      toggleGrid.add(this.btnFlip);
      toggleGrid.add(this.btnOnTop);
      body.add(toggleGrid);
      return body;
   }

   private JPanel buildFooter() {
      JPanel footer = new JPanel(new FlowLayout(2, 8, 8));
      footer.setBackground(BG2);
      footer.setBorder(new MatteBorder(1, 0, 0, 0, BORDER));
      JButton btnClose = new JButton("Đóng (ESC)");
      btnClose.setFont(new Font("Segoe UI", 1, 10));
      btnClose.setBackground(new Color(40, 40, 55));
      btnClose.setForeground(Color.WHITE);
      btnClose.setFocusPainted(false);
      btnClose.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)), new EmptyBorder(4, 12, 4, 12)));
      btnClose.setCursor(new Cursor(12));
      btnClose.addActionListener(e -> this.dispose());
      JButton btnOk = new JButton("✓ XONG");
      btnOk.setFont(new Font("Segoe UI", 1, 10));
      btnOk.setBackground(new Color(0, 140, 80));
      btnOk.setForeground(Color.WHITE);
      btnOk.setFocusPainted(false);
      btnOk.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0, 180, 100)), new EmptyBorder(4, 16, 4, 16)));
      btnOk.setCursor(new Cursor(12));
      btnOk.addActionListener(e -> this.dispose());
      footer.add(btnClose);
      footer.add(btnOk);
      return footer;
   }

   private void syncValuesToUI() {
      this.isUpdating = true;
      this.spDx.setValue(this.part.dx);
      this.spDy.setValue(this.part.dy);
      this.spImgId.setValue(this.part.idSmallImg);
      int rotNorm = (this.part.rotate % 360 + 360) % 360;
      this.spRotate.setValue(rotNorm);
      this.slideRotate.setValue(rotNorm);
      this.btnFlip.setSelected(this.part.flip == 1);
      this.btnOnTop.setSelected(this.part.onTop == 1);
      this.updateSpriteDimLabel();
      if (this.thumbPanel != null) {
         this.thumbPanel.repaint();
      }

      this.isUpdating = false;
   }

   private void updateSpriteDimLabel() {
      if (this.model != null && this.model.smallImages != null && this.part.idSmallImg >= 0 && this.part.idSmallImg < this.model.smallImages.length) {
         SmallImageDef s = this.model.smallImages[this.part.idSmallImg];
         this.lblSpriteDim.setText("Kích thước: " + s.w + " × " + s.h + " px (x=" + s.x + ", y=" + s.y + ")");
      } else {
         this.lblSpriteDim.setText("Kích thước: Không xác định");
      }
   }

   private void applyNudge(int dxM, int dyM, ActionEvent e) {
      int nudgeAmount = 1;
      if ((e.getModifiers() & 1) != 0) {
         nudgeAmount = 10;
      } else if ((e.getModifiers() & 2) != 0) {
         nudgeAmount = 5;
      }

      this.spDx.setValue((Integer)this.spDx.getValue() + dxM * nudgeAmount);
      this.spDy.setValue((Integer)this.spDy.getValue() + dyM * nudgeAmount);
   }

   private void notifyChanged() {
      if (this.frame != null) {
         this.frame.rebuildLayers();
      }

      if (this.onChange != null) {
         this.onChange.accept(this.part);
      }
   }

   private void initKeyShortcuts() {
      JRootPane rp = this.getRootPane();
      rp.getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), "closeDlg");
      rp.getActionMap().put("closeDlg", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            PartQuickEditDialog.this.dispose();
         }
      });
   }

   private static CompoundBorder createTitledBorder(String title) {
      return BorderFactory.createCompoundBorder(
         BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER), title, 0, 0, new Font("Segoe UI", 1, 9), ACCENT), new EmptyBorder(6, 8, 6, 8)
      );
   }

   private static JLabel dimLabel(String text) {
      JLabel l = new JLabel(text);
      l.setFont(new Font("Segoe UI", 0, 10));
      l.setForeground(TEXT_SEC);
      return l;
   }

   private static JSpinner makeSpinner(int min, int max) {
      JSpinner s = new JSpinner(new SpinnerNumberModel(0, min, max, 1));
      s.setFont(new Font("Segoe UI", 0, 10));
      s.setBackground(BG3);
      s.setForeground(TEXT_PRI);
      s.setBorder(BorderFactory.createLineBorder(BORDER));
      JComponent editor = s.getEditor();
      if (editor instanceof DefaultEditor) {
         JFormattedTextField tf = ((DefaultEditor)editor).getTextField();
         tf.setBackground(BG3);
         tf.setForeground(TEXT_PRI);
         tf.setBorder(null);
      }

      return s;
   }

   private static JButton makeNudgeBtn(String text, String tooltip) {
      JButton btn = new JButton(text);
      btn.setFont(new Font("Segoe UI", 1, 9));
      btn.setBackground(BG3);
      btn.setForeground(TEXT_PRI);
      btn.setBorder(BorderFactory.createLineBorder(BORDER));
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      btn.setToolTipText(tooltip);
      return btn;
   }

   private static JToggleButton createToggle(String text, Color activeColor) {
      JToggleButton btn = new JToggleButton(text);
      btn.setFont(new Font("Segoe UI", 1, 9));
      btn.setBackground(BG3);
      btn.setForeground(TEXT_SEC);
      btn.setFocusPainted(false);
      btn.setBorder(BorderFactory.createLineBorder(BORDER));
      btn.setPreferredSize(new Dimension(0, 24));
      btn.setCursor(new Cursor(12));
      btn.addItemListener(e -> {
         btn.setBackground(btn.isSelected() ? activeColor : BG3);
         btn.setForeground(btn.isSelected() ? Color.WHITE : TEXT_SEC);
      });
      return btn;
   }

   private class PartThumbnailPanel extends JPanel {
      PartThumbnailPanel() {
         this.setPreferredSize(new Dimension(54, 54));
         this.setBackground(new Color(12, 12, 18));
         this.setBorder(BorderFactory.createLineBorder(PartQuickEditDialog.BORDER));
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (PartQuickEditDialog.this.model != null && PartQuickEditDialog.this.model.atlasImage != null && PartQuickEditDialog.this.model.smallImages != null) {
            int sid = PartQuickEditDialog.this.part.idSmallImg;
            if (sid >= 0 && sid < PartQuickEditDialog.this.model.smallImages.length) {
               SmallImageDef s = PartQuickEditDialog.this.model.smallImages[sid];
               Graphics2D g2 = (Graphics2D)g;
               g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
               int tw = this.getWidth() - 4;
               int th = this.getHeight() - 4;
               if (tw > 0 && th > 0) {
                  double scale = Math.min((double)tw / Math.max(1, s.w), (double)th / Math.max(1, s.h));
                  if (scale > 4.0) {
                     scale = 4.0;
                  }

                  int dw = (int)(s.w * scale);
                  int dh = (int)(s.h * scale);
                  int dx = (this.getWidth() - dw) / 2;
                  int dy = (this.getHeight() - dh) / 2;
                  g2.drawImage(PartQuickEditDialog.this.model.atlasImage, dx, dy, dx + dw, dy + dh, s.x * 4, s.y * 4, (s.x + s.w) * 4, (s.y + s.h) * 4, null);
               }
            }
         }
      }
   }
}
