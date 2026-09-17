package com.deplor.haitactihontool.ui.components;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class StyledUI {
   public static JButton createButton(String text, final Color accentColor) {
      JButton btn = new JButton(text) {
         private boolean hovered = false;
         private boolean pressed = false;

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

               @Override
               public void mousePressed(MouseEvent e) {
                  pressed = true;
                  repaint();
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                  pressed = false;
                  repaint();
               }
            });
         }

         @Override
         public void setFont(Font f) {
            super.setFont(Theme.getFontForText(this.getText(), f));
         }

         @Override
         public void setText(String t) {
            super.setText(t);
            super.setFont(Theme.getFontForText(t, this.getFont()));
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = this.hovered ? accentColor.brighter() : accentColor;
            if (this.pressed) {
               bg = accentColor.darker();
            }

            if (!this.isEnabled()) {
               bg = new Color(20, 20, 28);
            }

            if (this.hovered && this.isEnabled()) {
               g2.setColor(new Color(0, 0, 0, 50));
               g2.fillRoundRect(2, 2, this.getWidth() - 2, this.getHeight() - 2, 10, 10);
            }

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, this.getWidth() - (this.hovered ? 2 : 0), this.getHeight() - (this.hovered ? 2 : 0), 10, 10);
            g2.setColor(this.isEnabled() ? Color.WHITE : new Color(255, 255, 255, 64));
            g2.setFont(this.getFont());
            FontMetrics fm = g2.getFontMetrics();
            int tx = (this.getWidth() - fm.stringWidth(this.getText())) / 2;
            int ty = (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(this.getText(), tx, ty);
            g2.dispose();
         }
      };
      btn.setFont(Theme.F_BOLD);
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      return btn;
   }

   public static JTextField createTextField(String text) {
      final JTextField f = new JTextField(text);
      f.setBackground(Theme.BG_DARKER);
      f.setForeground(Theme.TEXT_MAIN);
      f.setCaretColor(Theme.ACCENT);
      f.setFont(Theme.F_MAIN);
      f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), new EmptyBorder(8, 12, 8, 12)));
      f.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent e) {
            f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.ACCENT, 1), new EmptyBorder(8, 12, 8, 12)));
         }

         @Override
         public void focusLost(FocusEvent e) {
            f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), new EmptyBorder(8, 12, 8, 12)));
         }
      });
      return f;
   }

   public static JLabel createLabel(String text, Font font, Color color) {
      JLabel l = new JLabel(text);
      l.setFont(Theme.getFontForText(text, font));
      l.setForeground(color);
      return l;
   }

   public static JPanel createCard(LayoutManager layout) {
      JPanel p = new JPanel(layout);
      p.setBackground(Theme.BG_CARD);
      p.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER), new EmptyBorder(10, 10, 10, 10)));
      return p;
   }

   public static JCheckBox createCheckBox(String text) {
      JCheckBox cb = new JCheckBox(text);
      cb.setOpaque(false);
      cb.setForeground(Theme.TEXT_DIM);
      cb.setFont(Theme.F_SMALL);
      cb.setFocusPainted(false);
      cb.setCursor(new Cursor(12));
      return cb;
   }

   public static JPanel createHeader(String title) {
      JPanel p = new JPanel(new BorderLayout());
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(0, 0, 10, 0));
      JLabel lbl = createLabel(title.toUpperCase(), Theme.F_BOLD, Theme.PURPLE);
      p.add(lbl, "West");
      p.add(new JSeparator(0), "South");
      return p;
   }

   public static Border createTitledBorder(String title) {
      return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.BORDER), " " + title + " ", 1, 2, Theme.F_TINY, Theme.ACCENT);
   }

   public static void styleScrollPane(JScrollPane scrollPane) {
      scrollPane.setBorder(null);
      scrollPane.setBackground(Theme.BG_DARK);
      scrollPane.getViewport().setBackground(Theme.BG_DARK);
      scrollPane.getVerticalScrollBar().setUI(new StyledUI.SleekScrollBarUI());
      scrollPane.getHorizontalScrollBar().setUI(new StyledUI.SleekScrollBarUI());
      scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
      scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
      scrollPane.getVerticalScrollBar().setUnitIncrement(24);
      scrollPane.getHorizontalScrollBar().setUnitIncrement(24);
   }

   public static void styleComboBox(JComboBox<?> cb) {
      cb.setBackground(Theme.BG_DARKER);
      cb.setForeground(Theme.TEXT_MAIN);
      cb.setFont(Theme.F_MAIN);
      cb.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER, 1), new EmptyBorder(4, 6, 4, 6)));
   }

   public static class SleekScrollBarUI extends BasicScrollBarUI {
      @Override
      protected JButton createDecreaseButton(int orientation) {
         return this.createZeroButton();
      }

      @Override
      protected JButton createIncreaseButton(int orientation) {
         return this.createZeroButton();
      }

      private JButton createZeroButton() {
         JButton b = new JButton();
         b.setPreferredSize(new Dimension(0, 0));
         b.setMinimumSize(new Dimension(0, 0));
         b.setMaximumSize(new Dimension(0, 0));
         return b;
      }

      @Override
      protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
         Graphics2D g2 = (Graphics2D)g.create();
         g2.setColor(Theme.BG_DARKER);
         g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
         g2.dispose();
      }

      @Override
      protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
         if (!thumbBounds.isEmpty() && this.scrollbar.isEnabled()) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(this.isThumbRollover() ? Theme.ACCENT : Theme.BORDER);
            g2.fillRoundRect(thumbBounds.x + 1, thumbBounds.y + 1, thumbBounds.width - 2, thumbBounds.height - 2, 4, 4);
            g2.dispose();
         }
      }
   }
}
