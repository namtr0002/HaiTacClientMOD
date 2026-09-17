package com.deplor.haitactihontool.ui.components;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public final class Toast {
   private static JWindow currentToast;

   private Toast() {
   }

   public static void success(Component parent, String text) {
      show(parent, text, new Color(60, 200, 120));
   }

   public static void error(Component parent, String text) {
      show(parent, text, new Color(220, 70, 70));
   }

   public static void warning(Component parent, String text) {
      show(parent, text, new Color(255, 180, 60));
   }

   public static void info(Component parent, String text) {
      show(parent, text, Theme.ACCENT);
   }

   public static void show(Component parent, String text, final Color accent) {
      Window owner = parent == null ? KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow() : SwingUtilities.getWindowAncestor(parent);
      if (owner != null) {
         if (currentToast != null) {
            currentToast.dispose();
            currentToast = null;
         }

         JWindow toast = new JWindow(owner);
         currentToast = toast;
         JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D)g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.setColor(new Color(0, 0, 0, 90));
               g2.fillRoundRect(4, 4, this.getWidth() - 4, this.getHeight() - 4, 14, 14);
               g2.setColor(Theme.BG_CARD);
               g2.fillRoundRect(0, 0, this.getWidth() - 4, this.getHeight() - 4, 14, 14);
               g2.setColor(accent);
               g2.fillRoundRect(0, 0, 5, this.getHeight() - 4, 14, 14);
               g2.dispose();
            }
         };
         root.setOpaque(false);
         root.setBorder(new EmptyBorder(12, 18, 12, 20));
         JLabel label = new JLabel(text);
         label.setFont(Theme.F_MAIN);
         label.setForeground(Theme.TEXT_MAIN);
         root.add(label, "Center");
         toast.setBackground(new Color(0, 0, 0, 0));
         toast.setContentPane(root);
         toast.pack();
         Point p = owner.getLocationOnScreen();
         int x = p.x + (owner.getWidth() - toast.getWidth()) / 2;
         int y = p.y + owner.getHeight() - toast.getHeight() - 28;
         toast.setLocation(x, y);
         toast.setOpacity(0.0F);
         toast.setVisible(true);
         animate(toast);
      }
   }

   private static void animate(final JWindow toast) {
      final AtomicBoolean fadeOut = new AtomicBoolean(false);
      final Timer timer = new Timer(15, null);
      timer.addActionListener(new ActionListener() {
         float opacity = 0.0F;
         int life = 0;

         @Override
         public void actionPerformed(ActionEvent e) {
            if (!fadeOut.get()) {
               this.opacity += 0.08F;
               if (this.opacity >= 1.0F) {
                  this.opacity = 1.0F;
                  this.life++;
                  if (this.life >= 120) {
                     fadeOut.set(true);
                  }
               }
            } else {
               this.opacity -= 0.08F;
               if (this.opacity <= 0.0F) {
                  timer.stop();
                  toast.dispose();
                  if (Toast.currentToast == toast) {
                     Toast.currentToast = null;
                  }

                  return;
               }
            }

            toast.setOpacity(Math.max(0.0F, Math.min(1.0F, this.opacity)));
         }
      });
      timer.start();
   }
}
