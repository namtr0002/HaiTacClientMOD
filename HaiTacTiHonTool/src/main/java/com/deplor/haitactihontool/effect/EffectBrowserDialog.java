package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.ImageZoomHelper;
import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EffectBrowserDialog extends JDialog {
   private final MainUI mainUI;
   private final String dataDir;
   private final String imgDir;
   private final List<Integer> allIds = new ArrayList<>();
   private final List<EffectBrowserDialog.EffectBrowserCard> allCards = new ArrayList<>();
   private final JPanel gridPanel;
   private final JTextField txtSearch;
   private final ExecutorService loaderPool = Executors.newFixedThreadPool(4);
   private final Timer animTimer;
   private int currentGlobalTick = 0;
   private Integer selectedId = null;

   public EffectBrowserDialog(Frame owner, MainUI mainUI, String dataDir, String imgDir) {
      super(owner, "Duyệt Hiệu Ứng", true);
      this.mainUI = mainUI;
      this.dataDir = dataDir;
      this.imgDir = imgDir;
      this.setSize(900, 650);
      this.setLocationRelativeTo(owner);
      this.getContentPane().setBackground(Theme.BG_DARKER);
      this.setLayout(new BorderLayout());
      JPanel header = new JPanel(new BorderLayout(10, 0));
      header.setBackground(Theme.BG_DARK);
      header.setBorder(new EmptyBorder(12, 16, 12, 16));
      JLabel title = new JLabel("Duyệt Hiệu Ứng");
      title.setFont(new Font("Segoe UI", 1, 16));
      title.setForeground(Color.WHITE);
      header.add(title, "West");
      JPanel tools = new JPanel(new FlowLayout(2, 10, 0));
      tools.setOpaque(false);
      this.txtSearch = new JTextField();
      this.txtSearch.setPreferredSize(new Dimension(200, 28));
      this.txtSearch.putClientProperty("JTextField.placeholderText", "Tìm theo ID...");
      this.txtSearch.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            EffectBrowserDialog.this.filterCards();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            EffectBrowserDialog.this.filterCards();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            EffectBrowserDialog.this.filterCards();
         }
      });
      tools.add(this.txtSearch);
      JButton btnNew = new JButton("+ Tạo Hiệu Ứng Mới");
      btnNew.setFont(new Font("Segoe UI", 1, 11));
      btnNew.setBackground(Theme.ACCENT);
      btnNew.setForeground(Color.WHITE);
      btnNew.setFocusPainted(false);
      btnNew.addActionListener(e -> this.createNewEffect());
      tools.add(btnNew);
      header.add(tools, "East");
      this.add(header, "North");
      this.gridPanel = new JPanel(new GridLayout(0, 4, 12, 12));
      this.gridPanel.setBackground(Theme.BG_DARKER);
      this.gridPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
      JScrollPane scroll = new JScrollPane(this.gridPanel);
      scroll.setBorder(null);
      scroll.getVerticalScrollBar().setUnitIncrement(16);
      this.add(scroll, "Center");
      this.scanAllAvailableIds();

      for (int id : this.allIds) {
         EffectBrowserDialog.EffectBrowserCard card = new EffectBrowserDialog.EffectBrowserCard(id);
         this.allCards.add(card);
         this.gridPanel.add(card);
      }

      this.animTimer = new Timer(120, e -> {
         this.currentGlobalTick++;

         for (EffectBrowserDialog.EffectBrowserCard cardx : this.allCards) {
            if (cardx.isShowing()) {
               cardx.tick(this.currentGlobalTick);
            }
         }
      });
      this.animTimer.start();
      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            EffectBrowserDialog.this.animTimer.stop();
            EffectBrowserDialog.this.loaderPool.shutdownNow();
         }
      });
   }

   private void scanFolderForIds(File folder, Set<Integer> ids, boolean isImage) {
      if (folder != null && folder.isDirectory()) {
         File[] files = folder.listFiles();
         if (files != null) {
            for (File f : files) {
               if (f.isDirectory()) {
                  if (isImage) {
                     this.scanFolderForIds(f, ids, true);
                  }
               } else {
                  String name = f.getName();
                  if (isImage) {
                     if (name.toLowerCase().endsWith(".png")) {
                        try {
                           String idStr = name.substring(0, name.length() - 4);
                           ids.add(Integer.parseInt(idStr));
                        } catch (NumberFormatException var12) {
                        }
                     }
                  } else {
                     try {
                        String clean = name;
                        if (name.toLowerCase().endsWith(".eff")) {
                           clean = name.substring(0, name.length() - 4);
                        }

                        if (clean.toLowerCase().startsWith("effect_")) {
                           clean = clean.substring(7);
                        }

                        ids.add(Integer.parseInt(clean));
                     } catch (NumberFormatException var11) {
                     }
                  }
               }
            }
         }
      }
   }

   private void scanAllAvailableIds() {
      Set<Integer> ids = new HashSet<>();
      this.scanFolderForIds(new File(this.dataDir), ids, false);
      this.scanFolderForIds(new File(this.imgDir), ids, true);
      if (ids.isEmpty()) {
         String appFallback = AppConfig.getAppDirectory().replace('\\', '/') + "/Data/Effect/";
         this.scanFolderForIds(new File(appFallback + "data"), ids, false);
         this.scanFolderForIds(new File(appFallback + "img"), ids, true);
      }

      this.allIds.addAll(ids);
      Collections.sort(this.allIds);
   }

   private void filterCards() {
      String query = this.txtSearch.getText().trim();
      this.gridPanel.removeAll();

      for (EffectBrowserDialog.EffectBrowserCard card : this.allCards) {
         if (query.isEmpty() || String.valueOf(card.id).contains(query)) {
            this.gridPanel.add(card);
         }
      }

      this.gridPanel.revalidate();
      this.gridPanel.repaint();
   }

   private void createNewEffect() {
      String idStr = JOptionPane.showInputDialog(this, "Nhập ID hiệu ứng mới:", "ID");
      if (idStr != null) {
         try {
            int id = Integer.parseInt(idStr.trim());
            this.animTimer.stop();
            this.loaderPool.shutdownNow();
            this.selectedId = id;
            this.dispose();
         } catch (NumberFormatException var3) {
            JOptionPane.showMessageDialog(this, "ID không hợp lệ!");
         }
      }
   }

   public Integer getSelectedId() {
      return this.selectedId;
   }

   private class EffectBrowserCard extends JPanel {
      final int id;
      EffectModel cardModel = null;
      BufferedImage staticImage = null;
      boolean loaded = false;
      boolean hasData = false;
      boolean hasImage = false;
      private int tickPos = 0;
      private boolean hovered = false;

      EffectBrowserCard(int id) {
         this.id = id;
         this.setLayout(new BorderLayout());
         this.setPreferredSize(new Dimension(180, 200));
         this.setBackground(Theme.BG_CARD);
         this.setBorder(new LineBorder(Theme.BORDER, 1, true));
         this.setCursor(Cursor.getPredefinedCursor(12));
         JPanel topBar = new JPanel(new BorderLayout());
         topBar.setOpaque(false);
         topBar.setBorder(new EmptyBorder(6, 8, 4, 8));
         JLabel idLbl = new JLabel("ID: " + id);
         idLbl.setFont(new Font("Segoe UI", 1, 12));
         idLbl.setForeground(Theme.PURPLE);
         topBar.add(idLbl, "West");
         JLabel statusLbl = new JLabel("Đang tải...");
         statusLbl.setFont(new Font("Segoe UI", 0, 10));
         statusLbl.setForeground(Theme.TEXT_DIM);
         topBar.add(statusLbl, "East");
         this.add(topBar, "North");
         JPanel canvasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D)g;
               g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               int W = this.getWidth();
               int H = this.getHeight();
               g2.setColor(new Color(15, 15, 20));
               g2.fillRect(0, 0, W, H);
               g2.setColor(new Color(25, 25, 30));

               for (int x = 0; x < W; x += 16) {
                  g2.drawLine(x, 0, x, H);
               }

               for (int y = 0; y < H; y += 16) {
                  g2.drawLine(0, y, W, y);
               }

               int ax = W / 2;
               int ay = (int)(H * 0.7);
               if (!EffectBrowserCard.this.loaded) {
                  g2.setColor(Theme.TEXT_DIM);
                  g2.drawString("Đang tải...", W / 2 - 25, H / 2);
               } else {
                  if (EffectBrowserCard.this.hasData && EffectBrowserCard.this.cardModel != null) {
                     int len = EffectBrowserCard.this.cardModel.getSeqLen();
                     if (len > 0) {
                        int seqIndex = EffectBrowserCard.this.tickPos % len;
                        EffFrame frame = EffectBrowserCard.this.cardModel.getSeqFrame(seqIndex);
                        if (frame != null && EffectBrowserCard.this.cardModel.atlasImage != null && EffectBrowserCard.this.cardModel.smallImages != null) {
                           for (EffPartFrame p : frame.allParts) {
                              if (p.idSmallImg < EffectBrowserCard.this.cardModel.smallImages.length) {
                                 SmallImageDef si = EffectBrowserCard.this.cardModel.smallImages[p.idSmallImg];
                                 int destX = ax + p.dx * 2;
                                 int destY = ay + p.dy * 2;
                                 int dw = si.w * 2;
                                 int dh = si.h * 2;
                                 int cx = si.x * 4;
                                 int cy = si.y * 4;
                                 int cw = si.w * 4;
                                 int ch = si.h * 4;
                                 BufferedImage atlas = EffectBrowserCard.this.cardModel.atlasImage;
                                 int iw = atlas.getWidth();
                                 int ih = atlas.getHeight();
                                 if (cx >= iw) {
                                    cx = 0;
                                 }

                                 if (cy >= ih) {
                                    cy = 0;
                                 }

                                 if (cx + cw > iw) {
                                    cw = iw - cx;
                                 }

                                 if (cy + ch > ih) {
                                    ch = ih - cy;
                                 }

                                 if (cw > 0 && ch > 0) {
                                    if (p.flip == 1) {
                                       g2.drawImage(atlas, destX + dw, destY, destX, destY + dh, cx, cy, cx + cw, cy + ch, null);
                                    } else {
                                       g2.drawImage(atlas, destX, destY, destX + dw, destY + dh, cx, cy, cx + cw, cy + ch, null);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  } else if (EffectBrowserCard.this.hasImage && EffectBrowserCard.this.staticImage != null) {
                     int iwx = EffectBrowserCard.this.staticImage.getWidth();
                     int ihx = EffectBrowserCard.this.staticImage.getHeight();
                     double scale = Math.min((double)(W - 16) / iwx, (double)(H - 16) / ihx);
                     int sw = (int)(iwx * scale);
                     int sh = (int)(ihx * scale);
                     int sx = (W - sw) / 2;
                     int sy = (H - sh) / 2;
                     g2.drawImage(EffectBrowserCard.this.staticImage, sx, sy, sw, sh, null);
                  } else {
                     g2.setColor(new Color(60, 60, 70));
                     g2.setFont(new Font("Segoe UI", 1, 24));
                     g2.drawString("?", W / 2 - 8, H / 2 + 8);
                     g2.setFont(new Font("Segoe UI", 0, 10));
                     g2.drawString("Không có ảnh & dữ liệu", W / 2 - 50, H / 2 + 26);
                  }

                  g2.setColor(new Color(255, 50, 50, 100));
                  g2.fillRect(ax - 2, ay - 2, 4, 4);
               }
            }
         };
         this.add(canvasPanel, "Center");
         this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
               EffectBrowserCard.this.hovered = true;
               EffectBrowserCard.this.setBorder(new LineBorder(Theme.ACCENT, 1, true));
               EffectBrowserCard.this.setBackground(Theme.BG_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
               EffectBrowserCard.this.hovered = false;
               EffectBrowserCard.this.setBorder(new LineBorder(Theme.BORDER, 1, true));
               EffectBrowserCard.this.setBackground(Theme.BG_CARD);
            }

            @Override
            public void mousePressed(MouseEvent e) {
               EffectBrowserDialog.this.animTimer.stop();
               EffectBrowserDialog.this.loaderPool.shutdownNow();
               EffectBrowserDialog.this.selectedId = id;
               EffectBrowserDialog.this.dispose();
            }
         });
         EffectBrowserDialog.this.loaderPool.submit(() -> {
            try {
               File dFile = new File(EffectBrowserDialog.this.dataDir, String.valueOf(id));
               if (dFile.exists() && dFile.length() > 0L) {
                  EffectBinaryParser p = new EffectBinaryParser(EffectBrowserDialog.this.dataDir, EffectBrowserDialog.this.imgDir);
                  this.cardModel = p.parse(id);
                  if (this.cardModel != null) {
                     this.hasData = true;
                  }
               }

               File imgFile = new File(EffectBrowserDialog.this.imgDir, id + ".png");
               if (!imgFile.exists()) {
                  imgFile = new File(EffectBrowserDialog.this.imgDir, "x4/" + id + ".png");
               }

               if (imgFile.exists()) {
                  this.staticImage = ImageIO.read(imgFile);
                  if (this.staticImage != null) {
                     this.hasImage = true;
                     int zoomDetected = ImageZoomHelper.detectImageZoom(this.staticImage, this.cardModel != null ? this.cardModel.smallImages : null);
                     if (zoomDetected != 4) {
                        this.staticImage = ImageZoomHelper.scaleToZoom4(this.staticImage, zoomDetected);
                     }

                     if (this.cardModel != null) {
                        this.cardModel.atlasImage = this.staticImage;
                     }
                  }
               }
            } catch (Exception var9) {
            } finally {
               this.loaded = true;
               SwingUtilities.invokeLater(() -> {
                  if (this.hasData) {
                     statusLbl.setText("Active");
                     statusLbl.setForeground(new Color(50, 200, 100));
                  } else if (this.hasImage) {
                     statusLbl.setText("Image only");
                     statusLbl.setForeground(Color.ORANGE);
                  } else {
                     statusLbl.setText("Empty");
                     statusLbl.setForeground(Theme.TEXT_DIM);
                  }

                  this.repaint();
               });
            }
         });
      }

      void tick(int tick) {
         this.tickPos = tick;
         if (this.hasData && this.loaded) {
            this.repaint();
         }
      }
   }
}
