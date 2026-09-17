package com.deplor.haitactihontool.ui.components;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class BaseDarkDialog extends JDialog {
   private Point dragPoint;

   public BaseDarkDialog(Window owner, String title, int width, int height) {
      super(owner, ModalityType.APPLICATION_MODAL);
      this.setUndecorated(true);
      this.setSize(width, height);
      this.setLocationRelativeTo(owner);
      this.setBackground(new Color(0, 0, 0, 0));
      JPanel root = new JPanel(new BorderLayout()) {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRoundRect(4, 4, this.getWidth() - 4, this.getHeight() - 4, 16, 16);
            g2.setColor(Theme.BG_DARK);
            g2.fillRoundRect(0, 0, this.getWidth() - 4, this.getHeight() - 4, 16, 16);
            g2.setColor(Theme.BORDER);
            g2.drawRoundRect(0, 0, this.getWidth() - 5, this.getHeight() - 5, 16, 16);
            g2.dispose();
         }
      };
      root.setOpaque(false);
      this.setContentPane(root);
      JPanel titleBar = new JPanel(new BorderLayout());
      titleBar.setOpaque(false);
      titleBar.setPreferredSize(new Dimension(0, 42));
      titleBar.setBorder(new EmptyBorder(0, 14, 0, 0));
      JLabel lblTitle = new JLabel(title);
      lblTitle.setFont(Theme.F_BOLD);
      lblTitle.setForeground(Theme.TEXT_MAIN);
      JButton close = this.createCloseButton();
      titleBar.add(lblTitle, "West");
      titleBar.add(close, "East");
      MouseAdapter dragger = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            BaseDarkDialog.this.dragPoint = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            Point p = BaseDarkDialog.this.getLocation();
            BaseDarkDialog.this.setLocation(p.x + e.getX() - BaseDarkDialog.this.dragPoint.x, p.y + e.getY() - BaseDarkDialog.this.dragPoint.y);
         }
      };
      titleBar.addMouseListener(dragger);
      titleBar.addMouseMotionListener(dragger);
      root.add(titleBar, "North");
   }

   public void setBody(Component c) {
      JPanel wrap = new JPanel(new BorderLayout());
      wrap.setOpaque(false);
      wrap.setBorder(new EmptyBorder(10, 14, 14, 14));
      wrap.add(c);
      this.getContentPane().add(wrap, "Center");
   }

   private JButton createCloseButton() {
      JButton btn = new JButton("✕") {
         private boolean hover;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hover = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hover = false;
                  repaint();
               }
            });
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (this.hover) {
               g2.setColor(new Color(220, 70, 70));
               g2.fillRoundRect(6, 6, this.getWidth() - 12, this.getHeight() - 12, 8, 8);
            }

            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (this.getWidth() - fm.stringWidth(this.getText())) / 2, (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      btn.setPreferredSize(new Dimension(42, 42));
      btn.setBorderPainted(false);
      btn.setContentAreaFilled(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      btn.addActionListener(e -> this.dispose());
      return btn;
   }
}
