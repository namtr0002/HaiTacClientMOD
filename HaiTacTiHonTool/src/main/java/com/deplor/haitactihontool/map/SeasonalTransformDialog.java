package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class SeasonalTransformDialog extends JDialog {
   private final GameMap sourceMap;
   private final SeasonalTransformDialog.TransformCallback callback;
   private SeasonTheme selectedTheme;
   private SeasonThemeManager.TransformPreview currentPreview;
   public JComboBox<SeasonTheme> cmbSeason;
   private JPanel headerPanel;
   private JLabel lblSeasonIcon;
   private JLabel lblSeasonTitle;
   private JPanel statsPanel;
   private JLabel lblTotal;
   private JLabel lblMatched;
   private JLabel lblTileset;
   private JTextArea previewArea;
   private JButton btnApply;
   private JButton btnCancel;
   private JCheckBox chkChangeTileset;
   private boolean modeSwap = true;
   private JPanel panelSwapMode;
   private JPanel panelAutoMode;
   private JPanel contentSwitcher;
   private JSpinner spnAutoSpacing;
   private JCheckBox chkAutoEdge;
   private JCheckBox chkAutoFill;

   public SeasonalTransformDialog(Frame owner, GameMap sourceMap, SeasonalTransformDialog.TransformCallback callback) {
      super(owner, "SEASONAL MAP TRANSFORM", true);
      this.sourceMap = sourceMap;
      this.callback = callback;
      this.setUndecorated(true);
      this.setSize(620, 680);
      this.setLocationRelativeTo(owner);
      this.buildUI();
      if (this.cmbSeason.getItemCount() > 0) {
         this.cmbSeason.setSelectedIndex(0);
      }
   }

   private void buildUI() {
      JPanel root = new JPanel(new BorderLayout());
      root.setBackground(Theme.BG_DARKER);
      root.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 2));
      JPanel titleBar = this.buildTitleBar();
      root.add(titleBar, "North");
      JPanel center = this.buildCenterPanel();
      root.add(center, "Center");
      JPanel footer = this.buildFooter();
      root.add(footer, "South");
      this.setContentPane(root);
   }

   private JPanel buildTitleBar() {
      JPanel bar = new JPanel(new BorderLayout());
      bar.setBackground(Theme.BG_DARK);
      bar.setPreferredSize(new Dimension(0, 40));
      bar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      JLabel title = StyledUI.createLabel("  \ud83c\udfad  SEASONAL MAP TRANSFORM", Theme.F_TINY, Color.WHITE);
      bar.add(title, "West");
      if (this.sourceMap != null) {
         JLabel mapInfo = StyledUI.createLabel("  " + this.sourceMap.name + " [" + this.sourceMap.items.size() + " items]  ", Theme.F_TINY, Theme.TEXT_DIM);
         bar.add(mapInfo, "Center");
      }

      JButton btnX = this.buildXButton();
      bar.add(btnX, "East");
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
               Point p = e.getLocationOnScreen();
               SeasonalTransformDialog.this.setLocation(p.x - drag[0].x, p.y - drag[0].y);
            }
         }
      });
      return bar;
   }

   private JPanel buildCenterPanel() {
      JPanel center = new JPanel(new BorderLayout(0, 0));
      center.setBackground(Theme.BG_DARKER);
      JPanel topBar = new JPanel(new BorderLayout(8, 0));
      topBar.setBackground(Theme.BG_DARK);
      topBar.setBorder(new EmptyBorder(10, 16, 10, 16));
      JPanel selectorRow = new JPanel(new BorderLayout(8, 0));
      selectorRow.setOpaque(false);
      JLabel lblPick = StyledUI.createLabel("Sự kiện:", Theme.F_TINY, Theme.TEXT_MUTED);
      lblPick.setPreferredSize(new Dimension(65, 26));
      List<SeasonTheme> themeList = SeasonThemeManager.gI().getAllThemes();
      this.cmbSeason = new JComboBox<>(themeList.toArray(new SeasonTheme[0]));
      this.cmbSeason.setFont(new Font("Segoe UI", 0, 12));
      this.cmbSeason.setBackground(Theme.BG_CARD);
      this.cmbSeason.setForeground(Theme.TEXT_MAIN);
      this.cmbSeason.setRenderer(new SeasonalTransformDialog.SeasonComboRenderer());
      this.cmbSeason.addActionListener(e -> this.onSeasonSelected());
      selectorRow.add(lblPick, "West");
      selectorRow.add(this.cmbSeason, "Center");
      topBar.add(selectorRow, "Center");
      JPanel modeToggle = new JPanel(new GridLayout(1, 2, 2, 0));
      modeToggle.setOpaque(false);
      JButton btnSwap = this.buildModeButton("\ud83d\udd01 Swap Items", true);
      JButton btnAuto = this.buildModeButton("✨ Auto-Gen", false);
      btnSwap.putClientProperty("isSelected", true);
      btnAuto.putClientProperty("isSelected", false);
      ActionListener modeListener = evt -> {
         boolean isSwap = evt.getSource() == btnSwap;
         this.modeSwap = isSwap;
         btnSwap.putClientProperty("isSelected", isSwap);
         btnAuto.putClientProperty("isSelected", !isSwap);
         btnSwap.repaint();
         btnAuto.repaint();
         ((CardLayout)this.contentSwitcher.getLayout()).show(this.contentSwitcher, isSwap ? "SWAP" : "AUTO");
         if (isSwap) {
            this.refreshPreview();
         }
      };
      btnSwap.addActionListener(modeListener);
      btnAuto.addActionListener(modeListener);
      modeToggle.add(btnSwap);
      modeToggle.add(btnAuto);
      topBar.add(modeToggle, "East");
      center.add(topBar, "North");
      this.contentSwitcher = new JPanel(new CardLayout());
      this.contentSwitcher.setBackground(Theme.BG_DARKER);
      this.contentSwitcher.add(this.buildSwapPanel(), "SWAP");
      this.contentSwitcher.add(this.buildAutoGenPanel(), "AUTO");
      center.add(this.contentSwitcher, "Center");
      return center;
   }

   private JPanel buildSwapPanel() {
      JPanel scrollContent = new JPanel(new BorderLayout(0, 8));
      scrollContent.setBackground(Theme.BG_DARKER);
      scrollContent.setBorder(new EmptyBorder(10, 16, 10, 16));
      this.headerPanel = this.buildSeasonHeader();
      scrollContent.add(this.headerPanel, "North");
      JPanel body = new JPanel(new BorderLayout(0, 8));
      body.setOpaque(false);
      this.statsPanel = this.buildStatsPanel();
      body.add(this.statsPanel, "North");
      JPanel previewPanel = new JPanel(new BorderLayout(0, 4));
      previewPanel.setOpaque(false);
      JLabel lblPreviewTitle = StyledUI.createLabel("\ud83d\udccb Chi tiết các item sẽ được thay thế:", Theme.F_TINY, Theme.ACCENT);
      previewPanel.add(lblPreviewTitle, "North");
      this.previewArea = new JTextArea();
      this.previewArea.setBackground(Theme.BG_DARK);
      this.previewArea.setForeground(new Color(8967339));
      this.previewArea.setFont(Theme.F_MONO);
      this.previewArea.setEditable(false);
      this.previewArea.setBorder(new EmptyBorder(8, 10, 8, 10));
      JScrollPane scrollPreview = new JScrollPane(this.previewArea);
      scrollPreview.setPreferredSize(new Dimension(0, 200));
      StyledUI.styleScrollPane(scrollPreview);
      previewPanel.add(scrollPreview, "Center");
      body.add(previewPanel, "Center");
      scrollContent.add(body, "Center");
      JPanel optPanel = new JPanel(new FlowLayout(0, 16, 4));
      optPanel.setBackground(Theme.BG_DARKER);
      this.chkChangeTileset = new JCheckBox("Thay đổi Tileset theo mùa (nếu có)");
      this.chkChangeTileset.setFont(Theme.F_TINY);
      this.chkChangeTileset.setForeground(Theme.TEXT_MAIN);
      this.chkChangeTileset.setOpaque(false);
      this.chkChangeTileset.setSelected(true);
      this.chkChangeTileset.addActionListener(e -> this.refreshPreview());
      optPanel.add(this.chkChangeTileset);
      scrollContent.add(optPanel, "South");
      JScrollPane outer = new JScrollPane(scrollContent);
      outer.setBorder(null);
      StyledUI.styleScrollPane(outer);
      JPanel wrap = new JPanel(new BorderLayout());
      wrap.setBackground(Theme.BG_DARKER);
      wrap.add(outer, "Center");
      return wrap;
   }

   private JPanel buildAutoGenPanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, 1));
      panel.setBackground(Theme.BG_DARKER);
      panel.setBorder(new EmptyBorder(14, 20, 14, 20));
      JLabel lblExplain = new JLabel(
         "<html><b>✨ Auto-Generate</b>: Tự đặt items theo mùa lên map mới<br>(không cần items có sẵn — tự tạo layout từ đầu)</html>"
      );
      lblExplain.setFont(Theme.F_TINY);
      lblExplain.setForeground(Theme.TEXT_MAIN);
      lblExplain.setAlignmentX(0.0F);
      panel.add(lblExplain);
      panel.add(Box.createVerticalStrut(14));
      this.addAutoOptLabel(panel, "Sắp xếp:");
      this.chkAutoEdge = this.addAutoCheckbox(panel, "\ud83c\udf33 Đặt viền cây / hàng rào dọc biên map", true);
      this.chkAutoFill = this.addAutoCheckbox(panel, "\ud83c\udf1f Rải ngẫu nhiên bên trong map", false);
      panel.add(Box.createVerticalStrut(10));
      this.addAutoOptLabel(panel, "Khoảng cách giữa items:");
      JPanel spacingRow = new JPanel(new FlowLayout(0, 8, 0));
      spacingRow.setOpaque(false);
      spacingRow.setAlignmentX(0.0F);
      this.spnAutoSpacing = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
      this.spnAutoSpacing.setPreferredSize(new Dimension(60, 26));
      this.spnAutoSpacing.getEditor().getComponent(0).setBackground(Theme.BG_CARD);
      ((DefaultEditor)this.spnAutoSpacing.getEditor()).getTextField().setForeground(Theme.TEXT_MAIN);
      spacingRow.add(this.spnAutoSpacing);
      spacingRow.add(StyledUI.createLabel("tiles", Theme.F_TINY, Theme.TEXT_DIM));
      panel.add(spacingRow);
      panel.add(Box.createVerticalStrut(14));
      JButton btnPreviewAuto = StyledUI.createButton("\ud83d\udd0d Preview Auto-Layout", Theme.BG_CARD);
      btnPreviewAuto.setAlignmentX(0.0F);
      btnPreviewAuto.setMaximumSize(new Dimension(32767, 32));
      btnPreviewAuto.addActionListener(e -> this.previewAutoGenerate());
      panel.add(btnPreviewAuto);
      panel.add(Box.createVerticalStrut(8));
      JTextArea autoPreviewArea = new JTextArea();
      autoPreviewArea.setName("autoPreviewArea");
      autoPreviewArea.setBackground(Theme.BG_DARK);
      autoPreviewArea.setForeground(new Color(16766784));
      autoPreviewArea.setFont(Theme.F_MONO);
      autoPreviewArea.setEditable(false);
      autoPreviewArea.setText(
         "Bấm [Preview Auto-Layout] để xem trước\n\nItems sẽ được tìm tự động dựa trên rules của mùa:\n  - Tags \"tree\" → cây viền (edge)   \n  - Tags \"lamp\"/\"den\" → đèn cổng\n  - Tags \"flower\"/\"hoa\" → rải ngẫu nhiên"
      );
      autoPreviewArea.setBorder(new EmptyBorder(8, 10, 8, 10));
      JScrollPane autoScroll = new JScrollPane(autoPreviewArea);
      autoScroll.setAlignmentX(0.0F);
      StyledUI.styleScrollPane(autoScroll);
      autoScroll.setName("autoScroll");
      panel.add(autoScroll);
      JPanel wrap = new JPanel(new BorderLayout());
      wrap.setBackground(Theme.BG_DARKER);
      JScrollPane outerScroll = new JScrollPane(panel);
      outerScroll.setBorder(null);
      StyledUI.styleScrollPane(outerScroll);
      wrap.add(outerScroll, "Center");
      return wrap;
   }

   private JButton buildModeButton(String text, boolean isSwap) {
      JButton btn = new JButton(text) {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean sel = Boolean.TRUE.equals(this.getClientProperty("isSelected"));
            g2.setColor(sel ? Theme.ACCENT : Theme.BG_CARD);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 8, 8);
            g2.setColor(sel ? Color.WHITE : Theme.TEXT_DIM);
            g2.setFont(new Font("Segoe UI Emoji", 1, 11));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (this.getWidth() - fm.stringWidth(this.getText())) / 2, (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(Cursor.getPredefinedCursor(12));
      btn.setPreferredSize(new Dimension(110, 30));
      return btn;
   }

   private void addAutoOptLabel(JPanel p, String text) {
      JLabel lbl = StyledUI.createLabel(text, Theme.F_TINY, Theme.TEXT_MUTED);
      lbl.setAlignmentX(0.0F);
      p.add(lbl);
      p.add(Box.createVerticalStrut(4));
   }

   private JCheckBox addAutoCheckbox(JPanel p, String text, boolean selected) {
      JCheckBox chk = new JCheckBox(text, selected);
      chk.setFont(Theme.F_TINY);
      chk.setForeground(Theme.TEXT_MAIN);
      chk.setOpaque(false);
      chk.setAlignmentX(0.0F);
      p.add(chk);
      p.add(Box.createVerticalStrut(2));
      return chk;
   }

   private JPanel buildSeasonHeader() {
      JPanel header = new JPanel(new BorderLayout(12, 0)) {
         @Override
         protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (SeasonalTransformDialog.this.selectedTheme != null) {
               Graphics2D g2 = (Graphics2D)g.create();
               Color c = SeasonalTransformDialog.this.selectedTheme.accentColor;
               Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 60);
               g2.setPaint(new GradientPaint(0.0F, 0.0F, c2, this.getWidth(), 0.0F, new Color(0, 0, 0, 0)));
               g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 12, 12);
               g2.dispose();
            }
         }
      };
      header.setBorder(new EmptyBorder(12, 16, 12, 16));
      header.setOpaque(false);
      this.lblSeasonIcon = new JLabel("\ud83c\udfad");
      this.lblSeasonIcon.setFont(new Font("Segoe UI Emoji", 0, 42));
      header.add(this.lblSeasonIcon, "West");
      JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
      textPanel.setOpaque(false);
      this.lblSeasonTitle = new JLabel("Chọn một sự kiện...");
      this.lblSeasonTitle.setFont(new Font("Segoe UI", 1, 18));
      this.lblSeasonTitle.setForeground(Color.WHITE);
      JLabel lblDesc = StyledUI.createLabel("Tự động thay thế items trên map theo chủ đề sự kiện", Theme.F_TINY, Theme.TEXT_DIM);
      textPanel.add(this.lblSeasonTitle);
      textPanel.add(lblDesc);
      header.add(textPanel, "Center");
      return header;
   }

   private JPanel buildStatsPanel() {
      JPanel panel = new JPanel(new GridLayout(1, 3, 12, 0));
      panel.setOpaque(false);
      this.lblTotal = this.buildStatCard("Total Items", "0", Theme.TEXT_DIM);
      this.lblMatched = this.buildStatCard("Items Matched", "0", Theme.SUCCESS);
      this.lblTileset = this.buildStatCard("Tileset", "Unchanged", Theme.ACCENT);
      panel.add(this.lblTotal.getParent());
      panel.add(this.lblMatched.getParent());
      panel.add(this.lblTileset.getParent());
      return panel;
   }

   private JLabel buildStatCard(String title, String value, Color valueColor) {
      JPanel card = new JPanel(new BorderLayout(0, 2));
      card.setBackground(Theme.BG_DARK);
      card.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER, 1, true), new EmptyBorder(10, 14, 10, 14)));
      JLabel lblTitle = StyledUI.createLabel(title, Theme.F_TINY, Theme.TEXT_MUTED);
      JLabel lblValue = new JLabel(value);
      lblValue.setFont(new Font("Segoe UI", 1, 20));
      lblValue.setForeground(valueColor);
      card.add(lblTitle, "North");
      card.add(lblValue, "Center");
      card.putClientProperty("valueLabel", lblValue);
      return lblValue;
   }

   private JPanel buildFooter() {
      JPanel footer = new JPanel(new FlowLayout(2, 12, 12));
      footer.setBackground(Theme.BG_DARK);
      footer.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      this.btnCancel = new JButton("CANCEL");
      this.styleButton(this.btnCancel, Theme.BG_CARD, Theme.TEXT_DIM);
      this.btnCancel.addActionListener(e -> this.dispose());
      this.btnApply = new JButton("✨  MAKE SEASONAL") {
         private boolean hovered = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hovered = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hovered = false;
                  repaint();
               }
            });
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color base = SeasonalTransformDialog.this.selectedTheme != null ? SeasonalTransformDialog.this.selectedTheme.accentColor : Theme.ACCENT;
            Color top = this.hovered ? base.brighter() : base;
            Color bot = top.darker();
            g2.setPaint(new GradientPaint(0.0F, 0.0F, top, 0.0F, this.getHeight(), bot));
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", 1, 12));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (this.getWidth() - fm.stringWidth(this.getText())) / 2;
            int ty = (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(this.getText(), tx, ty);
            g2.dispose();
         }
      };
      this.btnApply.setPreferredSize(new Dimension(180, 34));
      this.btnApply.setContentAreaFilled(false);
      this.btnApply.setBorderPainted(false);
      this.btnApply.setFocusPainted(false);
      this.btnApply.setCursor(new Cursor(12));
      this.btnApply.addActionListener(e -> this.doApplyTransform());
      footer.add(this.btnCancel);
      footer.add(this.btnApply);
      return footer;
   }

   private void styleButton(JButton btn, Color bg, Color fg) {
      btn.setFont(Theme.F_TINY);
      btn.setBackground(bg);
      btn.setForeground(fg);
      btn.setPreferredSize(new Dimension(90, 34));
      btn.setBorder(new LineBorder(Theme.BORDER, 1, true));
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
   }

   private JButton buildXButton() {
      final JButton btn = new JButton("✕");
      btn.setFont(Theme.F_TINY);
      btn.setForeground(Theme.TEXT_DIM);
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      btn.setPreferredSize(new Dimension(40, 40));
      btn.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            btn.setForeground(Color.RED);
         }

         @Override
         public void mouseExited(MouseEvent e) {
            btn.setForeground(Theme.TEXT_DIM);
         }
      });
      btn.addActionListener(e -> this.dispose());
      return btn;
   }

   private void onSeasonSelected() {
      this.selectedTheme = (SeasonTheme)this.cmbSeason.getSelectedItem();
      if (this.selectedTheme != null) {
         this.lblSeasonIcon.setText(this.selectedTheme.emoji);
         this.lblSeasonTitle.setText(this.selectedTheme.emoji + "  " + this.selectedTheme.name);
         this.lblSeasonTitle.setForeground(this.selectedTheme.accentColor.brighter());
         this.headerPanel.repaint();
         this.btnApply.repaint();
         this.refreshPreview();
      }
   }

   private void refreshPreview() {
      if (this.selectedTheme != null && this.sourceMap != null) {
         int savedTileset = this.selectedTheme.tilesetOverride;
         if (!this.chkChangeTileset.isSelected()) {
            this.selectedTheme.tilesetOverride = -1;
         }

         this.currentPreview = SeasonThemeManager.gI().previewTransform(this.sourceMap, this.selectedTheme);
         this.selectedTheme.tilesetOverride = savedTileset;
         this.lblTotal.setText(String.valueOf(this.currentPreview.totalItems));
         this.lblMatched.setText(String.valueOf(this.currentPreview.matchedItems));
         if (this.currentPreview.tilesetChanged && this.chkChangeTileset.isSelected()) {
            this.lblTileset.setText("→ " + this.currentPreview.newTileset);
         } else {
            this.lblTileset.setText("Unchanged");
         }

         StringBuilder sb = new StringBuilder();
         if (this.currentPreview.entries.isEmpty()) {
            sb.append("⚠️  Không có item nào match với rules của mùa này.\n\n");
            sb.append("Hãy gán tags cho các ItemMapTemplate trước:\n");
            sb.append("  • Chọn item trong palette\n");
            sb.append("  • Nhập tags vào field 'Tags' (vd: tree, house, rock)\n");
            sb.append("  • Nhấn SAVE TAGS\n");
         } else {
            Map<String, Integer> byTag = new LinkedHashMap<>();

            for (SeasonThemeManager.TransformPreview.TransformEntry e : this.currentPreview.entries) {
               byTag.merge(e.matchedTag != null ? e.matchedTag : "?", 1, Integer::sum);
            }

            sb.append("\ud83d\udcca Tóm tắt:\n");

            for (Entry<String, Integer> e : byTag.entrySet()) {
               Short newId = this.selectedTheme.tagToTemplateId.get(e.getKey());
               sb.append(String.format("   %-12s → template #%-6s  × %d\n", e.getKey(), newId != null ? newId : "?", e.getValue()));
            }

            sb.append("\n\ud83d\udccd Chi tiết từng item:\n");

            for (SeasonThemeManager.TransformPreview.TransformEntry e : this.currentPreview.entries) {
               sb.append(
                  String.format(
                     "   Tile(%3d,%3d)  #%-4d  [%s]  →  #%d\n", e.tileX, e.tileY, e.oldTemplateId, e.matchedTag != null ? e.matchedTag : "?", e.newTemplateId
                  )
               );
            }
         }

         this.previewArea.setText(sb.toString());
         this.previewArea.setCaretPosition(0);
      }
   }

   private void doApplyTransform() {
      if (this.selectedTheme != null && this.sourceMap != null) {
         if (this.modeSwap) {
            if (this.currentPreview == null || this.currentPreview.matchedItems == 0) {
               JOptionPane.showMessageDialog(this, "Không có item nào được transform.\nHãy gán tags cho các template trước!", "Không có thay đổi", 2);
               return;
            }

            int result = JOptionPane.showConfirmDialog(
               this,
               String.format(
                  "<html><b>Xác nhận Transform?</b><br><br>Mùa: %s %s<br>Sẽ tạo 1 bản copy mới của map <b>%s</b><br>Tên mới: <b>%s</b><br>Items thay thế: <b>%d / %d</b><br><br>Bản gốc KHÔNG bị thay đổi.</html>",
                  this.selectedTheme.emoji,
                  this.selectedTheme.name,
                  this.sourceMap.name,
                  this.sourceMap.name + this.selectedTheme.mapNameSuffix,
                  this.currentPreview.matchedItems,
                  this.currentPreview.totalItems
               ),
               "Confirm Transform",
               0,
               3
            );
            if (result != 0) {
               return;
            }

            int savedTileset = this.selectedTheme.tilesetOverride;
            if (!this.chkChangeTileset.isSelected()) {
               this.selectedTheme.tilesetOverride = -1;
            }

            GameMap transformed = SeasonThemeManager.gI().applyTransform(this.sourceMap, this.selectedTheme);
            this.selectedTheme.tilesetOverride = savedTileset;
            if (transformed != null && this.callback != null) {
               this.callback.onTransformComplete(transformed);
            }
         } else {
            List<Short> edgeTemplates = this.findAutoTemplates("tree", "rao", "fence", "cay");
            List<Short> fillTemplates = this.findAutoTemplates("flower", "hoa", "rock", "da");
            if (edgeTemplates.isEmpty() && fillTemplates.isEmpty()) {
               JOptionPane.showMessageDialog(
                  this, "Không tìm thấy templates phù hợp!\nHãy gán tags và cấu hình rules trong SeasonThemeManager.", "Không có templates", 2
               );
               return;
            }

            int spacing = (Integer)this.spnAutoSpacing.getValue();
            boolean doEdge = this.chkAutoEdge != null && this.chkAutoEdge.isSelected();
            boolean doFill = this.chkAutoFill != null && this.chkAutoFill.isSelected();
            int resultx = JOptionPane.showConfirmDialog(
               this,
               String.format(
                  "<html><b>Xác nhận Auto-Generate?</b><br><br>Mùa: %s %s<br>Spacing: %d tiles<br>Viền biên: %s | Rải trong: %s<br><br>Tên map mới: <b>%s</b><br>Bản gốc KHÔNG bị thay đổi.</html>",
                  this.selectedTheme.emoji,
                  this.selectedTheme.name,
                  spacing,
                  doEdge ? "✅" : "❌",
                  doFill ? "✅" : "❌",
                  this.sourceMap.name + this.selectedTheme.mapNameSuffix + " [Auto]"
               ),
               "Confirm Auto-Gen",
               0,
               3
            );
            if (resultx != 0) {
               return;
            }

            GameMap generated = this.autoGenerateMap(spacing, doEdge, doFill, edgeTemplates, fillTemplates);
            if (generated != null && this.callback != null) {
               this.callback.onTransformComplete(generated);
            }
         }

         this.dispose();
      }
   }

   private List<Short> findAutoTemplates(String... tags) {
      List<Short> result = new ArrayList<>();
      if (this.selectedTheme == null) {
         return result;
      } else {
         for (String tag : tags) {
            Short id = this.selectedTheme.tagToTemplateId.get(tag);
            if (id != null && TemplateManager.gI().getItemTemplate(id) != null && !result.contains(id)) {
               result.add(id);
            }
         }

         return result;
      }
   }

   private GameMap autoGenerateMap(int spacing, boolean doEdge, boolean doFill, List<Short> edgeTemplates, List<Short> fillTemplates) {
      GameMap copy = SeasonThemeManager.gI().applyTransform(this.sourceMap, this.selectedTheme);
      copy.items.clear();
      copy.name = this.sourceMap.name + this.selectedTheme.mapNameSuffix + " [Auto]";
      Random rng = new Random(System.currentTimeMillis());
      int w = copy.width;
      int h = copy.height;
      int edgeIdx = 0;
      int fillIdx = 0;
      if (doEdge && !edgeTemplates.isEmpty()) {
         int x = 0;

         while (x < w) {
            copy.addItem(edgeTemplates.get(edgeIdx++ % edgeTemplates.size()), (short)x, 0, 0);
            copy.addItem(edgeTemplates.get(edgeIdx++ % edgeTemplates.size()), (short)x, (short)(h - 1), 0);
            x += spacing;
         }

         for (int y = spacing; y < h - spacing; y += spacing) {
            copy.addItem(edgeTemplates.get(edgeIdx++ % edgeTemplates.size()), 0, (short)y, 0);
            copy.addItem(edgeTemplates.get(edgeIdx++ % edgeTemplates.size()), (short)(w - 1), (short)y, 0);
         }
      }

      if (doFill && !fillTemplates.isEmpty()) {
         for (int y = 1 + rng.nextInt(Math.max(1, spacing)); y < h - 1; y += spacing + rng.nextInt(2)) {
            for (int x = 1 + rng.nextInt(Math.max(1, spacing)); x < w - 1; x += spacing + rng.nextInt(2)) {
               copy.addItem(fillTemplates.get(fillIdx++ % fillTemplates.size()), (short)x, (short)y, 0);
            }
         }
      }

      return copy;
   }

   private void previewAutoGenerate() {
      if (this.selectedTheme != null && this.sourceMap != null) {
         List<Short> edgeTemplates = this.findAutoTemplates("tree", "rao", "fence", "cay");
         List<Short> fillTemplates = this.findAutoTemplates("flower", "hoa", "rock", "da");
         int spacing = (Integer)this.spnAutoSpacing.getValue();
         boolean doEdge = this.chkAutoEdge != null && this.chkAutoEdge.isSelected();
         boolean doFill = this.chkAutoFill != null && this.chkAutoFill.isSelected();
         int w = this.sourceMap.width;
         int h = this.sourceMap.height;
         int edgeCount = doEdge ? 2 * (w / spacing) + 2 * (h / spacing) : 0;
         int fillCount = doFill ? (w - 2) / spacing * ((h - 2) / spacing) : 0;
         StringBuilder sb = new StringBuilder();
         sb.append("\ud83d\udcd0 Map: ").append(w).append(" × ").append(h).append("  Spacing: ").append(spacing).append(" tiles\n\n");
         sb.append("── Viền biên ").append(doEdge ? "✅" : "❌").append(" ────────────────────\n");
         if (doEdge && !edgeTemplates.isEmpty()) {
            sb.append("  Templates: ");

            for (Short t : edgeTemplates) {
               sb.append("#").append(t).append(" ");
            }

            sb.append("\n  Ước tính: ~").append(edgeCount).append(" items\n");
         } else if (doEdge) {
            sb.append("  ⚠️ Cần tags: tree/rao/fence/cay\n");
         }

         sb.append("\n── Rải bên trong ").append(doFill ? "✅" : "❌").append(" ─────────────────\n");
         if (doFill && !fillTemplates.isEmpty()) {
            sb.append("  Templates: ");

            for (Short t : fillTemplates) {
               sb.append("#").append(t).append(" ");
            }

            sb.append("\n  Ước tính: ~").append(fillCount).append(" items\n");
         } else if (doFill) {
            sb.append("  ⚠️ Cần tags: flower/hoa/rock/da\n");
         }

         sb.append("\n── Tổng: ~").append(edgeCount + fillCount).append(" items mới");
         this.findTextArea(this.contentSwitcher, sb.toString());
      }
   }

   private boolean findTextArea(Container parent, String text) {
      for (Component c : parent.getComponents()) {
         if (c instanceof JTextArea && "autoPreviewArea".equals(c.getName())) {
            ((JTextArea)c).setText(text);
            ((JTextArea)c).setCaretPosition(0);
            return true;
         }

         if (c instanceof Container && this.findTextArea((Container)c, text)) {
            return true;
         }
      }

      return false;
   }

   private static class SeasonComboRenderer extends DefaultListCellRenderer {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         if (value instanceof SeasonTheme t) {
            this.setText("  " + t.emoji + "  " + t.name);
            this.setFont(new Font("Segoe UI Emoji", 0, 13));
            this.setBackground(isSelected ? Theme.ACCENT_SOFT : Theme.BG_CARD);
            this.setForeground(isSelected ? Color.WHITE : Theme.TEXT_MAIN);
         }

         this.setBorder(new EmptyBorder(6, 8, 6, 8));
         return this;
      }
   }

   public interface TransformCallback {
      void onTransformComplete(GameMap var1);
   }
}
