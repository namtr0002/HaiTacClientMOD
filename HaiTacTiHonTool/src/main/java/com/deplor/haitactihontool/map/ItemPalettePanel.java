package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.Lang;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ItemPalettePanel extends JPanel {
   private List<ItemMapTemplate> allTemplates = new ArrayList<>();
   private List<ItemMapTemplate> filteredTemplates = new ArrayList<>();
   private short selectedId = -1;
   private ItemPalettePanel.ItemSelectionListener listener;
   private ItemPalettePanel.GridPanel gridPanel;
   private static final int ICON_SIZE = 72;
   private static final int GAP = 6;

   public ItemPalettePanel() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      this.loadTemplates();
      JPanel searchBarPanel = new JPanel(new BorderLayout(5, 5));
      searchBarPanel.setBackground(Theme.BG_DARKER);
      searchBarPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      final JTextField txtSearch = StyledUI.createTextField("");
      txtSearch.setPreferredSize(new Dimension(0, 30));
      txtSearch.putClientProperty("JTextField.placeholderText", Lang.get("search_placeholder"));
      searchBarPanel.add(txtSearch, "Center");
      this.add(searchBarPanel, "North");
      this.gridPanel = new ItemPalettePanel.GridPanel();
      this.add(this.gridPanel, "Center");
      txtSearch.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            this.filter();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            this.filter();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            this.filter();
         }

         private void filter() {
            String query = txtSearch.getText().trim().toLowerCase();
            ItemPalettePanel.this.filteredTemplates.clear();

            for (ItemMapTemplate temp : ItemPalettePanel.this.allTemplates) {
               if (query.isEmpty()) {
                  ItemPalettePanel.this.filteredTemplates.add(temp);
               } else if (!String.valueOf(temp.idItem).contains(query) && !String.valueOf(temp.idImage).contains(query)) {
                  String tagStr = SeasonKeywordStore.gI().getTagString(temp.idItem).toLowerCase();
                  if (!tagStr.isEmpty() && tagStr.contains(query)) {
                     ItemPalettePanel.this.filteredTemplates.add(temp);
                  }
               } else {
                  ItemPalettePanel.this.filteredTemplates.add(temp);
               }
            }

            ItemPalettePanel.this.gridPanel.revalidate();
            ItemPalettePanel.this.gridPanel.repaint();
         }
      });
   }

   private void loadTemplates() {
      Map<Short, ItemMapTemplate> all = TemplateManager.gI().getAllItemTemplates();
      this.allTemplates = new ArrayList<>(all.values());
      this.allTemplates.sort((a, b) -> Short.compare(a.idItem, b.idItem));
      this.filteredTemplates = new ArrayList<>(this.allTemplates);
   }

   public void reloadTemplates() {
      this.loadTemplates();
      if (this.gridPanel != null) {
         this.gridPanel.revalidate();
         this.gridPanel.repaint();
      }
   }

   public void setItemSelectionListener(ItemPalettePanel.ItemSelectionListener listener) {
      this.listener = listener;
   }

   private class GridPanel extends JPanel {
      public GridPanel() {
         this.setBackground(Theme.BG_DARKER);
         this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               int w = GridPanel.this.getWidth();
               if (w > 0) {
                  int cols = Math.max(1, w / 78);
                  int col = e.getX() / 78;
                  int row = e.getY() / 78;
                  int idx = row * cols + col;
                  if (idx >= 0 && idx < ItemPalettePanel.this.filteredTemplates.size()) {
                     ItemPalettePanel.this.selectedId = ItemPalettePanel.this.filteredTemplates.get(idx).idItem;
                     if (ItemPalettePanel.this.listener != null) {
                        ItemPalettePanel.this.listener.onTemplateSelected(ItemPalettePanel.this.selectedId);
                     }

                     GridPanel.this.repaint();
                  }
               }
            }
         });
         this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
               GridPanel.this.revalidate();
            }
         });
      }

      @Override
      public Dimension getPreferredSize() {
         int w = this.getParent() != null ? this.getParent().getWidth() : 800;
         if (w <= 0) {
            w = 800;
         }

         int cols = Math.max(1, w / 78);
         int rows = (int)Math.ceil((double)ItemPalettePanel.this.filteredTemplates.size() / cols);
         int h = rows * 78 + 20;
         return new Dimension(w - 20, h);
      }

      @Override
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         int w = this.getWidth();
         if (w > 0) {
            int cols = Math.max(1, w / 78);
            if (cols != 0) {
               Rectangle clip = g.getClipBounds();

               for (int i = 0; i < ItemPalettePanel.this.filteredTemplates.size(); i++) {
                  int row = i / cols;
                  int col = i % cols;
                  int x = col * 78 + 5;
                  int y = row * 78 + 5;
                  if (clip == null || y + 72 >= clip.y && y <= clip.y + clip.height) {
                     ItemMapTemplate temp = ItemPalettePanel.this.filteredTemplates.get(i);
                     g2.setColor(Theme.BG_CARD);
                     g2.fillRoundRect(x, y, 72, 72, 10, 10);
                     BufferedImage img = ImageCache.getServerImage(temp.idImage);
                     if (img != null) {
                        int iw = img.getWidth();
                        int ih = img.getHeight();
                        double scale = Math.min(56.0 / iw, 56.0 / ih);
                        int rw = (int)(iw * scale);
                        int rh = (int)(ih * scale);
                        g2.drawImage(img, x + (72 - rw) / 2, y + (72 - rh) / 2, rw, rh, null);
                     }

                     if (temp.idItem == ItemPalettePanel.this.selectedId) {
                        g2.setColor(Theme.ACCENT);
                        g2.setStroke(new BasicStroke(2.0F));
                        g2.drawRoundRect(x, y, 72, 72, 10, 10);
                        g2.setColor(Theme.ACCENT_SOFT);
                        g2.fillRoundRect(x, y, 72, 72, 10, 10);
                     } else {
                        g2.setColor(Theme.BORDER);
                        g2.setStroke(new BasicStroke(1.0F));
                        g2.drawRoundRect(x, y, 72, 72, 10, 10);
                     }

                     g2.setColor(Theme.TEXT_MUTED);
                     g2.setFont(Theme.F_TINY);
                     g2.drawString("#" + temp.idItem, x + 6, y + 14);
                     String tagStr = SeasonKeywordStore.gI().getTagString(temp.idItem);
                     if (!tagStr.isEmpty()) {
                        g2.setColor(new Color(51283));
                        g2.fillOval(x + 72 - 14, y + 4, 8, 8);
                        String firstTag = tagStr.split(",")[0].trim();
                        g2.setFont(new Font("Segoe UI", 0, 9));
                        g2.setColor(new Color(6732650));
                        g2.drawString(firstTag, x + 5, y + 72 - 4);
                     }
                  }
               }
            }
         }
      }
   }

   public interface ItemSelectionListener {
      void onTemplateSelected(short var1);
   }
}
