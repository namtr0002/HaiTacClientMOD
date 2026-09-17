package com.deplor.haitactihontool.mob;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.part.PartImage;
import com.deplor.haitactihontool.part.mPart;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MobPartSelectorDialog extends JDialog {
   private int selectedId = -2;

   public static int selectPart(Frame parent, MobCanvas canvas, String title, int[] partTypes) {
      MobPartSelectorDialog dlg = new MobPartSelectorDialog(parent, canvas, title, partTypes, false);
      dlg.setVisible(true);
      return dlg.selectedId;
   }

   public static int selectMobIcon(Frame parent, String title) {
      MobPartSelectorDialog dlg = new MobPartSelectorDialog(parent, null, title, null, true);
      dlg.setVisible(true);
      return dlg.selectedId;
   }

   private MobPartSelectorDialog(Frame parent, MobCanvas canvas, String title, int[] initialPartTypes, boolean isMobIconMode) {
      super(parent, title, true);
      this.setUndecorated(true);
      this.setSize(860, 620);
      this.setLocationRelativeTo(parent);
      JPanel content = new JPanel(new BorderLayout());
      content.setBackground(Theme.BG_DARKER);
      content.setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 2));
      this.setContentPane(content);
      JPanel header = new JPanel(new BorderLayout(10, 0));
      header.setBackground(Theme.BG_DARK);
      header.setBorder(new EmptyBorder(12, 20, 12, 15));
      final Point[] dragPoint = new Point[]{null};
      MouseAdapter dragAdapter = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            dragPoint[0] = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            if (dragPoint[0] != null) {
               Point curr = MobPartSelectorDialog.this.getLocation();
               MobPartSelectorDialog.this.setLocation(curr.x + e.getX() - dragPoint[0].x, curr.y + e.getY() - dragPoint[0].y);
            }
         }
      };
      header.addMouseListener(dragAdapter);
      header.addMouseMotionListener(dragAdapter);
      JLabel lblTitle = StyledUI.createLabel(title, Theme.F_BOLD, Theme.ACCENT);
      header.add(lblTitle, "West");
      final JTextField txtSearch = StyledUI.createTextField("");
      txtSearch.setPreferredSize(new Dimension(220, 28));
      txtSearch.putClientProperty("JTextField.placeholderText", "\ud83d\udd0d Lọc ID...");
      header.add(txtSearch, "Center");
      JButton btnClose = StyledUI.createButton("✕", Theme.ERROR);
      btnClose.setPreferredSize(new Dimension(32, 28));
      btnClose.addActionListener(e -> {
         this.selectedId = -2;
         this.dispose();
      });
      header.add(btnClose, "East");
      content.add(header, "North");
      final JLabel lblStatus = StyledUI.createLabel(" Sẵn sàng", Theme.F_TINY, Theme.TEXT_MUTED);
      lblStatus.setBorder(new EmptyBorder(4, 15, 6, 15));
      content.add(lblStatus, "South");
      final JPanel grid = new JPanel(new GridLayout(0, 6, 10, 10));
      grid.setOpaque(false);
      grid.setBorder(new EmptyBorder(12, 12, 12, 12));
      final List<MobPartSelectorDialog.PartCardData> cardDataList = new ArrayList<>();
      if (isMobIconMode) {
         cardDataList.add(new MobPartSelectorDialog.PartCardData(-1, "Không có", null, -1));
         String baseDir = AppConfig.getResolvedPath("path_mob_img", "Data/Mob/img/");
         File dir = new File(baseDir);
         List<Integer> iconIds = new ArrayList<>();
         if (dir.exists()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));
            if (files != null) {
               for (File f : files) {
                  try {
                     String name = f.getName().replace(".png", "");
                     int val = Integer.parseInt(name);
                     int iconId = val >= 1000 ? val - 1000 : val;
                     if (!iconIds.contains(iconId)) {
                        iconIds.add(iconId);
                     }
                  } catch (Exception var28) {
                  }
               }
            }
         }

         Collections.sort(iconIds);

         for (int iconId : iconIds) {
            BufferedImage iconImg = null;

            try {
               File imgFile = new File(baseDir + (iconId + 1000) + ".png");
               if (!imgFile.exists()) {
                  imgFile = new File(baseDir + iconId + ".png");
               }

               if (imgFile.exists()) {
                  iconImg = ImageIO.read(imgFile);
               }
            } catch (Exception var27) {
            }

            cardDataList.add(new MobPartSelectorDialog.PartCardData(iconId, "Icon " + iconId, iconImg, -1));
         }
      } else {
         cardDataList.add(new MobPartSelectorDialog.PartCardData(-1, "Trống (-1)", null, -1));
         List<mPart> allParts = canvas != null ? canvas.getAllParts() : null;
         if (allParts != null) {
            List<mPart> sortedParts = new ArrayList<>(allParts);
            Collections.sort(sortedParts, (a, b) -> Integer.compare(a.id, b.id));

            for (mPart p : sortedParts) {
               BufferedImage partImg = null;
               if (p.pi != null && p.pi.length > 0) {
                  for (PartImage pi : p.pi) {
                     if (pi != null && pi.id >= 0) {
                        partImg = canvas.getPartImage(pi.id);
                        if (partImg != null) {
                           break;
                        }
                     }
                  }
               }

               cardDataList.add(new MobPartSelectorDialog.PartCardData(p.id, "ID: " + p.id, partImg, p.type));
            }
         }
      }

      if (!isMobIconMode) {
         JPanel catBar = new JPanel(new FlowLayout(0, 4, 4));
         catBar.setBackground(Theme.BG_DARK);
         catBar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
         String[] categories = new String[]{"Tất cả", "Đầu (0)", "Tóc (0/4)", "Nón (4)", "Vũ khí (1)", "VK Trang phục (1)", "Áo (2)", "Cánh (3)", "Quần (5)"};
         final int[][] catTypes = new int[][]{null, {0}, {0, 4, 5}, {4}, {1}, {1}, {2}, {3}, {5}};
         ButtonGroup catGroup = new ButtonGroup();
         int selectedCatIdx = 0;
         if (initialPartTypes != null && initialPartTypes.length > 0) {
            int t = initialPartTypes[0];
            if (t == 1) {
               selectedCatIdx = 4;
            } else if (t == 2) {
               selectedCatIdx = 6;
            } else if (t == 3) {
               selectedCatIdx = 7;
            } else if (t == 4) {
               selectedCatIdx = 3;
            } else if (t == 5) {
               selectedCatIdx = 8;
            }
         }

         final int[] activeCatIdx = new int[]{selectedCatIdx};

         for (int i = 0; i < categories.length; i++) {
            int catIdx = i;
            JToggleButton btnCat = new JToggleButton(categories[i]);
            btnCat.setFont(Theme.F_TINY);
            btnCat.setFocusPainted(false);
            btnCat.setSelected(i == selectedCatIdx);
            btnCat.addActionListener(e -> {
               activeCatIdx[0] = catIdx;
               this.filterCards(grid, cardDataList, txtSearch.getText().trim(), catTypes[catIdx], lblStatus);
            });
            catGroup.add(btnCat);
            catBar.add(btnCat);
         }

         JPanel centerBox = new JPanel(new BorderLayout());
         centerBox.setOpaque(false);
         centerBox.add(catBar, "North");
         JScrollPane scroll = new JScrollPane(grid);
         StyledUI.styleScrollPane(scroll);
         scroll.setBorder(null);
         scroll.getViewport().setOpaque(false);
         scroll.getVerticalScrollBar().setUnitIncrement(18);
         centerBox.add(scroll, "Center");
         content.add(centerBox, "Center");
         txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
               this.runFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               this.runFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               this.runFilter();
            }

            private void runFilter() {
               MobPartSelectorDialog.this.filterCards(grid, cardDataList, txtSearch.getText().trim(), catTypes[activeCatIdx[0]], lblStatus);
            }
         });
         this.filterCards(grid, cardDataList, txtSearch.getText().trim(), catTypes[selectedCatIdx], lblStatus);
      } else {
         JScrollPane scroll = new JScrollPane(grid);
         StyledUI.styleScrollPane(scroll);
         scroll.setBorder(null);
         scroll.getViewport().setOpaque(false);
         scroll.getVerticalScrollBar().setUnitIncrement(18);
         content.add(scroll, "Center");
         txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
               this.runFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               this.runFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               this.runFilter();
            }

            private void runFilter() {
               MobPartSelectorDialog.this.filterCards(grid, cardDataList, txtSearch.getText().trim(), null, lblStatus);
            }
         });
         this.filterCards(grid, cardDataList, "", null, lblStatus);
      }
   }

   private void filterCards(JPanel grid, List<MobPartSelectorDialog.PartCardData> cardDataList, String searchQ, int[] targetTypes, JLabel lblStatus) {
      grid.removeAll();
      String q = searchQ.toLowerCase();
      int count = 0;

      for (MobPartSelectorDialog.PartCardData data : cardDataList) {
         if (data.id == -1) {
            grid.add(this.createCard(data));
            count++;
         } else {
            boolean typeMatch = true;
            if (targetTypes != null && targetTypes.length > 0) {
               typeMatch = false;

               for (int t : targetTypes) {
                  if (data.type == t) {
                     typeMatch = true;
                     break;
                  }
               }
            }

            boolean textMatch = q.isEmpty() || String.valueOf(data.id).contains(q) || data.label.toLowerCase().contains(q);
            if (typeMatch && textMatch) {
               grid.add(this.createCard(data));
               count++;
            }
         }
      }

      grid.revalidate();
      grid.repaint();
      lblStatus.setText(String.format(" Hiển thị %d kết quả", count));
   }

   private JPanel createCard(final MobPartSelectorDialog.PartCardData data) {
      final JPanel card = new JPanel(new BorderLayout());
      card.setPreferredSize(new Dimension(105, 115));
      card.setBackground(Theme.BG_CARD);
      card.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
      card.setCursor(Cursor.getPredefinedCursor(12));
      JLabel imgLbl = new JLabel("", 0);
      if (data.img != null) {
         int w = data.img.getWidth();
         int h = Math.min(data.img.getHeight(), 64);
         double scale = Math.min(60.0 / Math.max(1, w), 60.0 / Math.max(1, h));
         int sw = Math.max(1, (int)(w * scale));
         int sh = Math.max(1, (int)(h * scale));
         Image scaled = data.img.getSubimage(0, 0, w, h).getScaledInstance(sw, sh, 4);
         imgLbl.setIcon(new ImageIcon(scaled));
      } else {
         imgLbl.setText(data.id == -1 ? "TRỐNG" : "?");
         imgLbl.setFont(Theme.F_BOLD);
         imgLbl.setForeground(Theme.TEXT_MUTED);
      }

      JLabel txtLbl = new JLabel(data.label, 0);
      txtLbl.setFont(Theme.F_TINY);
      txtLbl.setForeground(Theme.TEXT_MAIN);
      card.add(imgLbl, "Center");
      card.add(txtLbl, "South");
      card.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            MobPartSelectorDialog.this.selectedId = data.id;
            MobPartSelectorDialog.this.dispose();
         }

         @Override
         public void mouseEntered(MouseEvent e) {
            card.setBackground(Theme.BG_HOVER);
            card.setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 2));
         }

         @Override
         public void mouseExited(MouseEvent e) {
            card.setBackground(Theme.BG_CARD);
            card.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
         }
      });
      return card;
   }

   private static class PartCardData {
      int id;
      String label;
      BufferedImage img;
      int type;

      PartCardData(int id, String label, BufferedImage img, int type) {
         this.id = id;
         this.label = label;
         this.img = img;
         this.type = type;
      }
   }
}
