package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ObjectPalettePanel extends JPanel {
   private Consumer<Object> onSelected;
   private JTextField txtNpcId;
   private JTextField txtMobId;
   private JTextField txtVgoId;
   private JTextField txtVgoX;
   private JTextField txtVgoY;

   public ObjectPalettePanel(Consumer<Object> onSelected) {
      this.onSelected = onSelected;
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      JPanel container = new JPanel();
      container.setLayout(new BoxLayout(container, 1));
      container.setOpaque(false);
      container.setBorder(new EmptyBorder(12, 12, 12, 12));
      container.add(StyledUI.createHeader("PLACABLE OBJECTS"));
      container.add(Box.createVerticalStrut(15));
      JPanel topRow = new JPanel(new GridLayout(1, 2, 15, 0));
      topRow.setOpaque(false);
      topRow.add(this.buildConfigSection("NPC CONFIG", "NPC ID:", this.txtNpcId = StyledUI.createTextField("1"), "PLACE NPC", Theme.ACCENT, "NPC"));
      topRow.add(this.buildConfigSection("MOB CONFIG", "MOB ID:", this.txtMobId = StyledUI.createTextField("1"), "PLACE MOB", Theme.BG_CARD, "MOB"));
      container.add(topRow);
      container.add(Box.createVerticalStrut(15));
      container.add(this.buildVgoSection());
      this.add(new JScrollPane(container) {
         {
            this.setBorder(null);
            this.setOpaque(false);
            this.getViewport().setOpaque(false);
            this.getVerticalScrollBar().setUnitIncrement(12);
         }
      }, "Center");
   }

   private JPanel buildConfigSection(String title, String labelText, JTextField field, String btnText, Color btnColor, String type) {
      JPanel p = new JPanel(new BorderLayout(0, 8));
      p.setOpaque(false);
      p.setBorder(BorderFactory.createCompoundBorder(StyledUI.createTitledBorder(title), new EmptyBorder(10, 10, 10, 10)));
      JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
      inputPanel.setOpaque(false);
      inputPanel.add(StyledUI.createLabel(labelText, Theme.F_TINY, Theme.TEXT_DIM), "West");
      inputPanel.add(field, "Center");
      p.add(inputPanel, "North");
      JButton btn = StyledUI.createButton(btnText, btnColor);
      btn.setPreferredSize(new Dimension(0, 36));
      btn.addActionListener(e -> {
         try {
            String val = field.getText().trim();
            if (type.equals("NPC")) {
               this.onSelected.accept("NPC:" + Short.parseShort(val));
            } else {
               this.onSelected.accept("MOB:" + Integer.parseInt(val));
            }
         } catch (Exception var5x) {
         }
      });
      p.add(btn, "South");
      return p;
   }

   private JPanel buildVgoSection() {
      JPanel p = new JPanel(new BorderLayout(0, 10));
      p.setOpaque(false);
      p.setBorder(BorderFactory.createCompoundBorder(StyledUI.createTitledBorder("VGO (TELEPORT GATE)"), new EmptyBorder(10, 10, 10, 10)));
      JPanel grid = new JPanel(new GridLayout(1, 3, 10, 0));
      grid.setOpaque(false);
      this.txtVgoId = StyledUI.createTextField("-1");
      this.txtVgoX = StyledUI.createTextField("100");
      this.txtVgoY = StyledUI.createTextField("100");
      grid.add(this.buildLabeledField("TARGET MAP", this.txtVgoId));
      grid.add(this.buildLabeledField("COORD X", this.txtVgoX));
      grid.add(this.buildLabeledField("COORD Y", this.txtVgoY));
      p.add(grid, "Center");
      JButton btnVgo = StyledUI.createButton("PLACE VGO GATE", Theme.PURPLE);
      btnVgo.setPreferredSize(new Dimension(0, 40));
      btnVgo.addActionListener(e -> {
         try {
            short tid = Short.parseShort(this.txtVgoId.getText().trim());
            short tx = Short.parseShort(this.txtVgoX.getText().trim());
            short ty = Short.parseShort(this.txtVgoY.getText().trim());
            this.onSelected.accept("VGO:" + tid + "," + tx + "," + ty);
         } catch (Exception var5) {
         }
      });
      p.add(btnVgo, "South");
      return p;
   }

   private JPanel buildLabeledField(String label, JTextField field) {
      JPanel p = new JPanel(new BorderLayout(0, 4));
      p.setOpaque(false);
      p.add(StyledUI.createLabel(label, Theme.F_TINY, Theme.TEXT_MUTED), "North");
      p.add(field, "Center");
      return p;
   }
}
