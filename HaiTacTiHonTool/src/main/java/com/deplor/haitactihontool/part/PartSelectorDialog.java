package com.deplor.haitactihontool.part;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;

public class PartSelectorDialog extends JDialog {
   private final List<mPart> filteredParts;
   private final PartCanvas sharedCanvas;
   private int selectedId = -2;

   public PartSelectorDialog(Frame parent, List<mPart> allParts, String title, int[] types, PartCanvas canvas) {
      super(parent, title, true);
      this.sharedCanvas = canvas;
      this.filteredParts = allParts.stream().filter(p -> {
         for (int t : types) {
            if (p.type == t) {
               return true;
            }
         }

         return false;
      }).sorted((a, b) -> Integer.compare(a.id, b.id)).collect(Collectors.toList());
      this.setupUI();
   }

   private void setupUI() {
      this.setUndecorated(true);
      this.setSize(800, 600);
      this.setLocationRelativeTo(this.getParent());
      JPanel contentPanelOuter = new JPanel(new BorderLayout());
      contentPanelOuter.setBackground(Theme.BG_DARKER);
      contentPanelOuter.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 2));
      this.setContentPane(contentPanelOuter);
      JPanel header = new JPanel(new BorderLayout());
      header.setBackground(Theme.BG_DARK);
      header.setBorder(new EmptyBorder(12, 20, 12, 15));
      final Point[] initialClick = new Point[]{null};
      MouseAdapter dragListener = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            initialClick[0] = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            int thisX = PartSelectorDialog.this.getLocation().x;
            int thisY = PartSelectorDialog.this.getLocation().y;
            int xMoved = e.getX() - initialClick[0].x;
            int yMoved = e.getY() - initialClick[0].y;
            PartSelectorDialog.this.setLocation(thisX + xMoved, thisY + yMoved);
         }
      };
      header.addMouseListener(dragListener);
      header.addMouseMotionListener(dragListener);
      JLabel title = StyledUI.createLabel(this.getTitle(), Theme.F_BOLD, Theme.ACCENT);
      header.add(title, "West");
      JButton btnClose = new JButton() {
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
            g2.setColor(this.hovered ? Theme.ERROR : Theme.BG_DARK);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 6, 6);
            g2.setColor(this.hovered ? Color.WHITE : Theme.TEXT_DIM);
            g2.setStroke(new BasicStroke(1.2F));
            int cx = this.getWidth() / 2;
            int cy = this.getHeight() / 2;
            int hs = 4;
            g2.drawLine(cx - hs, cy - hs, cx + hs, cy + hs);
            g2.drawLine(cx + hs, cy - hs, cx - hs, cy + hs);
            g2.dispose();
         }
      };
      btnClose.setPreferredSize(new Dimension(28, 28));
      btnClose.setContentAreaFilled(false);
      btnClose.setBorderPainted(false);
      btnClose.setFocusPainted(false);
      btnClose.setCursor(new Cursor(12));
      btnClose.addActionListener(e -> this.dispose());
      header.add(btnClose, "East");
      contentPanelOuter.add(header, "North");
      JPanel grid = new JPanel(new GridLayout(0, 6, 10, 10));
      grid.setOpaque(false);
      grid.add(this.createPartCard(-1, null));

      for (mPart part : this.filteredParts) {
         grid.add(this.createPartCard(part.id, part));
      }

      JScrollPane scroll = new JScrollPane(grid);
      scroll.setBorder(null);
      scroll.setOpaque(false);
      scroll.getViewport().setOpaque(false);
      scroll.setHorizontalScrollBarPolicy(31);
      scroll.getVerticalScrollBar().setUnitIncrement(16);
      contentPanelOuter.add(scroll, "Center");
   }

   private JPanel createPartCard(final int id, mPart part) {
      final JPanel card = new JPanel(new BorderLayout());
      card.setPreferredSize(new Dimension(100, 120));
      card.setBackground(Theme.BG_CARD);
      card.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
      card.setCursor(Cursor.getPredefinedCursor(12));
      JLabel preview = new JLabel("", 0);
      if (id == -1) {
         preview.setText("EMPTY");
         preview.setForeground(Theme.TEXT_MUTED);
      } else if (part != null && part.pi != null && part.pi.length > 0) {
         BufferedImage img = this.sharedCanvas.getImage(part.pi[0].id);
         if (img != null) {
            int w = img.getWidth();
            int h = img.getHeight();
            double scale = Math.min(64.0 / w, 64.0 / h);
            if (scale > 2.0) {
               scale = 2.0;
            }

            Image scaled = img.getScaledInstance((int)(w * scale), (int)(h * scale), 4);
            preview.setIcon(new ImageIcon(scaled));
         } else {
            preview.setText("?");
            preview.setForeground(Color.RED);
         }
      }

      JLabel label = new JLabel("#" + id, 0);
      label.setFont(Theme.F_TINY);
      label.setForeground(Theme.TEXT_DIM);
      label.setBorder(new EmptyBorder(5, 0, 5, 0));
      card.add(preview, "Center");
      card.add(label, "South");
      card.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            card.setBackground(Theme.BG_DARK);
            card.setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 1));
         }

         @Override
         public void mouseExited(MouseEvent e) {
            card.setBackground(Theme.BG_CARD);
            card.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
         }

         @Override
         public void mousePressed(MouseEvent e) {
            PartSelectorDialog.this.selectedId = id;
            PartSelectorDialog.this.dispose();
         }
      });
      return card;
   }

   public int getSelectedId() {
      return this.selectedId;
   }

   static class WrapLayout extends FlowLayout {
      public WrapLayout(int align, int hgap, int vgap) {
         super(align, hgap, vgap);
      }

      @Override
      public Dimension preferredLayoutSize(Container target) {
         return this.layoutSize(target, true);
      }

      @Override
      public Dimension minimumLayoutSize(Container target) {
         Dimension minimum = this.layoutSize(target, false);
         minimum.width = minimum.width - (this.getHgap() + 1);
         return minimum;
      }

      private Dimension layoutSize(Container target, boolean preferred) {
         synchronized (target.getTreeLock()) {
            int targetWidth = 0;
            Container parent = target.getParent();
            if (parent instanceof JViewport) {
               targetWidth = parent.getWidth();
            }

            if (targetWidth <= 0) {
               targetWidth = target.getWidth();
            }

            if (targetWidth <= 0) {
               targetWidth = Integer.MAX_VALUE;
            }

            int hgap = this.getHgap();
            int vgap = this.getVgap();
            Insets insets = target.getInsets();
            int horizontalInsetsAndGap = insets.left + insets.right + hgap * 2;
            int maxWidth = targetWidth - horizontalInsetsAndGap;
            Dimension dim = new Dimension(0, 0);
            int rowWidth = 0;
            int rowHeight = 0;
            int nmembers = target.getComponentCount();

            for (int i = 0; i < nmembers; i++) {
               Component m = target.getComponent(i);
               if (m.isVisible()) {
                  Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                  if (rowWidth + d.width > maxWidth) {
                     this.addRow(dim, rowWidth, rowHeight);
                     rowWidth = 0;
                     rowHeight = 0;
                  }

                  if (rowWidth != 0) {
                     rowWidth += hgap;
                  }

                  rowWidth += d.width;
                  rowHeight = Math.max(rowHeight, d.height);
               }
            }

            this.addRow(dim, rowWidth, rowHeight);
            dim.width += horizontalInsetsAndGap;
            dim.height = dim.height + insets.top + insets.bottom + vgap * 2;
            return dim;
         }
      }

      private void addRow(Dimension dim, int rowWidth, int rowHeight) {
         dim.width = Math.max(dim.width, rowWidth);
         if (dim.height > 0) {
            dim.height = dim.height + this.getVgap();
         }

         dim.height += rowHeight;
      }

      @Override
      public void layoutContainer(Container target) {
         synchronized (target.getTreeLock()) {
            int targetWidth = 0;
            Container parent = target.getParent();
            if (parent instanceof JViewport) {
               targetWidth = parent.getWidth();
            }

            if (targetWidth <= 0) {
               targetWidth = target.getWidth();
            }

            if (targetWidth <= 0) {
               super.layoutContainer(target);
            } else {
               int hgap = this.getHgap();
               int vgap = this.getVgap();
               Insets insets = target.getInsets();
               int maxwidth = targetWidth - (insets.left + insets.right + hgap * 2);
               int nmembers = target.getComponentCount();
               int x = insets.left + hgap;
               int y = insets.top + vgap;
               int rowHeight = 0;

               for (int i = 0; i < nmembers; i++) {
                  Component m = target.getComponent(i);
                  if (m.isVisible()) {
                     Dimension d = m.getPreferredSize();
                     m.setSize(d.width, d.height);
                     if (x + d.width > maxwidth + insets.left + hgap && x > insets.left + hgap) {
                        x = insets.left + hgap;
                        y += vgap + rowHeight;
                        rowHeight = 0;
                     }

                     m.setLocation(x, y);
                     x += d.width + hgap;
                     rowHeight = Math.max(rowHeight, d.height);
                  }
               }
            }
         }
      }
   }
}
