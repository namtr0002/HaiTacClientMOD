package com.deplor.haitactihontool.ui.components;

import com.deplor.haitactihontool.config.Theme;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BreadcrumbBar extends JPanel {
   private final FlowLayout layout = new FlowLayout(0, 4, 0);

   public BreadcrumbBar() {
      this.setLayout(this.layout);
      this.setOpaque(false);
   }

   public void setPath(File file, Consumer<File> action) {
      this.removeAll();
      if (file == null) {
         this.repaint();
      } else {
         List<File> parts = new ArrayList<>();

         for (File cur = file; cur != null; cur = cur.getParentFile()) {
            parts.add(0, cur);
         }

         for (int i = 0; i < parts.size(); i++) {
            File part = parts.get(i);
            String name = part.getName().isBlank() ? part.getAbsolutePath() : part.getName();
            JButton btn = StyledUI.createButton(name, Theme.BG_CARD);
            btn.setFont(Theme.F_SMALL);
            btn.setPreferredSize(new Dimension(Math.max(70, btn.getFontMetrics(btn.getFont()).stringWidth(name) + 26), 26));
            btn.addActionListener(e -> action.accept(part));
            this.add(btn);
            if (i < parts.size() - 1) {
               JLabel sep = new JLabel(">");
               sep.setForeground(Theme.TEXT_MUTED);
               this.add(sep);
            }
         }

         this.revalidate();
         this.repaint();
      }
   }
}
