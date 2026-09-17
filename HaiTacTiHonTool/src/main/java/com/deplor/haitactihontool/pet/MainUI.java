package com.deplor.haitactihontool.pet;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MainUI extends JPanel {
   private static final int[] PET_TYPE_IDS = new int[]{0, 1, 2, 3, 4, 5, 21};
   private static final String[] PET_TYPE_LABELS = new String[]{
      "0 – Loại A (9 frame)",
      "1 – Loại B (9 frame)",
      "2 – Loại C (5 frame)",
      "3 – Loại D (6 frame)",
      "4 – Loại E (3 frame)",
      "5 – Loại F (6 frame)",
      "21 – Loại G (5/7 frame)"
   };
   private static final String[] PET_TYPE_DESC = new String[]{
      "9 frame – Stand/Walk/Attack/Swim/Sleep đầy đủ",
      "9 frame – Stand/Walk/Attack/Swim/Sleep đầy đủ",
      "5 frame – Loại đặc biệt, chỉ 5 frame",
      "6 frame – Bốn chân / Bay / Lội nước",
      "3 frame – Đơn giản 3 frame",
      "6 frame – Tương tự Type 3",
      "5 frame (7 nếu frameID=55/56) – Dạng đặc biệt"
   };
   private List<PetTemplate> allPets;
   private List<PetTemplate> filteredPets;
   private PetTemplate editingPet;
   private DefaultListModel<PetTemplate> petListModel;
   private JList<PetTemplate> petList;
   private JTextField txtSearch;
   private PetCanvas canvas;
   private JPanel stripContainer;
   private JTextField txtId;
   private JTextField txtName;
   private JTextField txtIcon;
   private JComboBox<String> comboType;
   private JLabel lblFrameInfo;
   private JTextField txtFrame;
   private JTextField txtOp;
   private JComboBox<String> comboAction;
   private JCheckBox chkFlip;
   private JButton btnPlay;
   private JLabel lblAnimInfo;
   private boolean isUpdatingFields = false;
   private JPanel leftPanel;
   private JPanel centerPanel;
   private JPanel rightPanel;

   public MainUI() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      this.allPets = PetDataManager.loadPets();
      this.filteredPets = new ArrayList<>(this.allPets);
      JSplitPane splitLeft = new JSplitPane(1, this.leftPanel(), this.centerPanel());
      splitLeft.setDividerLocation(260);
      splitLeft.setDividerSize(2);
      splitLeft.setContinuousLayout(true);
      splitLeft.setBorder(null);
      splitLeft.setBackground(Theme.BG_DARKER);
      JSplitPane splitRight = new JSplitPane(1, splitLeft, this.rightPanel());
      splitRight.setDividerLocation(1100);
      splitRight.setDividerSize(2);
      splitRight.setContinuousLayout(true);
      splitRight.setBorder(null);
      splitRight.setBackground(Theme.BG_DARKER);
      this.add(splitRight, "Center");
      if (!this.filteredPets.isEmpty()) {
         this.petList.setSelectedIndex(0);
      }
   }

   private JPanel leftPanel() {
      this.leftPanel = new JPanel(new BorderLayout());
      this.leftPanel.setPreferredSize(new Dimension(260, 0));
      this.leftPanel.setBackground(Theme.BG_DARK);
      this.leftPanel.setBorder(new MatteBorder(0, 0, 0, 1, Theme.BORDER));
      JPanel topArea = new JPanel(new BorderLayout());
      topArea.setOpaque(false);
      JPanel header = StyledUI.createHeader("THÚ NUÔI");
      header.setBorder(new EmptyBorder(14, 15, 6, 15));
      topArea.add(header, "North");
      JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
      searchPanel.setOpaque(false);
      searchPanel.setBorder(new EmptyBorder(4, 10, 8, 10));
      this.txtSearch = StyledUI.createTextField("");
      this.txtSearch.putClientProperty("JTextField.placeholderText", "Tìm ID hoặc Tên...");
      this.txtSearch.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            MainUI.this.filterPets();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            MainUI.this.filterPets();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            MainUI.this.filterPets();
         }
      });
      searchPanel.add(this.txtSearch, "Center");
      topArea.add(searchPanel, "South");
      this.leftPanel.add(topArea, "North");
      this.petListModel = new DefaultListModel<>();

      for (PetTemplate p : this.filteredPets) {
         this.petListModel.addElement(p);
      }

      this.petList = new JList<>(this.petListModel);
      this.petList.setBackground(Theme.BG_DARK);
      this.petList.setForeground(Theme.TEXT_MAIN);
      this.petList.setSelectionBackground(Theme.ACCENT_SOFT);
      this.petList.setSelectionForeground(Theme.ACCENT);
      this.petList.setFixedCellHeight(44);
      this.petList.setCellRenderer(new MainUI.PetListCellRenderer());
      this.petList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            this.selectPet(this.petList.getSelectedValue());
         }
      });
      JScrollPane scroll = new JScrollPane(this.petList);
      StyledUI.styleScrollPane(scroll);
      this.leftPanel.add(scroll, "Center");
      JLabel countLabel = new JLabel() {
         {
            this.setHorizontalAlignment(0);
            this.setFont(Theme.F_TINY);
            this.setForeground(Theme.TEXT_MUTED);
         }

         @Override
         public void paint(Graphics g) {
            this.setText(MainUI.this.filteredPets.size() + " / " + MainUI.this.allPets.size() + " thú nuôi");
            super.paint(g);
         }
      };
      JPanel statusBar = new JPanel(new BorderLayout());
      statusBar.setBackground(Theme.BG_DARKER);
      statusBar.setBorder(new EmptyBorder(5, 10, 5, 10));
      statusBar.add(countLabel, "Center");
      this.leftPanel.add(statusBar, "South");
      return this.leftPanel;
   }

   private static String getTypeShortLabel(int type) {
      switch (type) {
         case 0:
            return "T0 · 9f";
         case 1:
            return "T1 · 9f";
         case 2:
            return "T2 · 5f";
         case 3:
            return "T3 · 6f";
         case 4:
            return "T4 · 3f";
         case 5:
            return "T5 · 6f";
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         default:
            return "T" + type;
         case 21:
            return "T21 · 5f";
      }
   }

   private void filterPets() {
      String q = this.txtSearch.getText().toLowerCase().trim();
      this.filteredPets.clear();

      for (PetTemplate p : this.allPets) {
         if (q.isEmpty() || String.valueOf(p.id).contains(q) || p.name.toLowerCase().contains(q)) {
            this.filteredPets.add(p);
         }
      }

      this.petListModel.clear();

      for (PetTemplate px : this.filteredPets) {
         this.petListModel.addElement(px);
      }

      if (!this.filteredPets.isEmpty()) {
         this.petList.setSelectedIndex(0);
      } else {
         this.selectPet(null);
      }

      this.leftPanel.repaint();
   }

   private void selectPet(PetTemplate pet) {
      this.editingPet = pet;
      this.canvas.setPet(pet);
      this.updateFields();
      this.updateFrameStrip();
   }

   private JPanel centerPanel() {
      this.centerPanel = new JPanel(new BorderLayout());
      this.centerPanel.setBackground(Theme.BG_DARKER);
      this.canvas = new PetCanvas();
      this.canvas.setAnimListener((tick, max) -> SwingUtilities.invokeLater(() -> {
         if (this.lblAnimInfo != null) {
            this.lblAnimInfo.setText(String.format("Frame: %d / %d", tick + 1, max));
         }
      }));
      this.centerPanel.add(this.canvas, "Center");
      JPanel toolbar = new JPanel(new FlowLayout(0, 10, 8));
      toolbar.setBackground(Theme.BG_DARK);
      toolbar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      this.btnPlay = StyledUI.createButton("PAUSE", Theme.ACCENT);
      this.btnPlay.setPreferredSize(new Dimension(100, 30));
      this.btnPlay.addActionListener(e -> {
         boolean playing = !this.canvas.isPlaying();
         this.canvas.setPlaying(playing);
         this.btnPlay.setText(playing ? "PAUSE" : "PLAY");
      });
      this.comboAction = new JComboBox<>(new String[]{"Stand (0)", "Walk (1)", "⚔ Attack (2)", "Swim (3)", "Sleep (4)"});
      this.styleCombo(this.comboAction);
      this.comboAction.setPreferredSize(new Dimension(140, 30));
      this.comboAction.addActionListener(e -> this.canvas.setAction(this.comboAction.getSelectedIndex()));
      this.chkFlip = StyledUI.createCheckBox("↔ Quay phải");
      this.chkFlip.addActionListener(e -> this.canvas.setFacingRight(this.chkFlip.isSelected()));
      JSpinner spinFps = new JSpinner(new SpinnerNumberModel(10, 1, 60, 1));
      spinFps.setPreferredSize(new Dimension(55, 30));
      spinFps.addChangeListener(e -> this.canvas.setFps((Integer)spinFps.getValue()));
      JButton btnZoomIn = StyledUI.createButton("+", Theme.BG_CARD);
      btnZoomIn.setPreferredSize(new Dimension(50, 30));
      btnZoomIn.addActionListener(e -> this.canvas.setZoom(Math.min(10.0, this.canvas.getZoom() + 0.5)));
      JButton btnZoomOut = StyledUI.createButton("-", Theme.BG_CARD);
      btnZoomOut.setPreferredSize(new Dimension(50, 30));
      btnZoomOut.addActionListener(e -> this.canvas.setZoom(Math.max(1.0, this.canvas.getZoom() - 0.5)));
      JButton btnReset = StyledUI.createButton("Reset View", Theme.BG_DARK);
      btnReset.setPreferredSize(new Dimension(90, 30));
      btnReset.addActionListener(e -> this.canvas.resetView());
      this.lblAnimInfo = new JLabel("Frame: 0 / 0");
      this.lblAnimInfo.setFont(Theme.F_TINY);
      this.lblAnimInfo.setForeground(Theme.TEXT_MUTED);
      toolbar.add(this.btnPlay);
      toolbar.add(this.comboAction);
      toolbar.add(this.chkFlip);
      toolbar.add(new JLabel("FPS:"));
      toolbar.add(spinFps);
      toolbar.add(new JSeparator(1));
      toolbar.add(btnZoomIn);
      toolbar.add(btnZoomOut);
      toolbar.add(btnReset);
      toolbar.add(Box.createHorizontalStrut(10));
      toolbar.add(this.lblAnimInfo);
      this.centerPanel.add(toolbar, "North");
      this.stripContainer = new JPanel(new FlowLayout(0, 8, 6));
      this.stripContainer.setBackground(Theme.BG_DARK);
      JScrollPane scrollStrip = new JScrollPane(this.stripContainer);
      scrollStrip.setPreferredSize(new Dimension(0, 100));
      scrollStrip.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      scrollStrip.setVerticalScrollBarPolicy(21);
      scrollStrip.getHorizontalScrollBar().setUnitIncrement(20);
      StyledUI.styleScrollPane(scrollStrip);
      JPanel stripHeader = new JPanel(new BorderLayout());
      stripHeader.setBackground(Theme.BG_DARKER);
      stripHeader.setBorder(new EmptyBorder(3, 10, 3, 10));
      JLabel stripTitle = new JLabel("FRAMES");
      stripTitle.setFont(Theme.F_TINY);
      stripTitle.setForeground(Theme.TEXT_MUTED);
      stripHeader.add(stripTitle, "West");
      JPanel stripWrapper = new JPanel(new BorderLayout());
      stripWrapper.add(stripHeader, "North");
      stripWrapper.add(scrollStrip, "Center");
      this.centerPanel.add(stripWrapper, "South");
      return this.centerPanel;
   }

   private void updateFrameStrip() {
      this.stripContainer.removeAll();
      if (this.editingPet == null) {
         this.stripContainer.revalidate();
         this.stripContainer.repaint();
      } else {
         PetCanvas.PetImageResult res = PetCanvas.resolvePetImage(this.editingPet);
         if (res != null && res.image != null) {
            try {
               BufferedImage img = res.image;
               int presetFrames = PetCanvas.getFrameCount(this.editingPet.type, this.editingPet.frame);
               int w = img.getWidth();
               int h = img.getHeight();
               int totalFrames;
               int hOne;
               if (h > (int)(w * 1.25) && h >= presetFrames * 4) {
                  totalFrames = presetFrames;
                  hOne = Math.max(1, h / presetFrames);
               } else {
                  totalFrames = 1;
                  hOne = h;
               }

               for (int i = 0; i < totalFrames; i++) {
                  final int frameIndex = i;
                  BufferedImage frameImg = img.getSubimage(0, Math.min(frameIndex * hOne, h - hOne), w, hOne);
                  final JPanel box = new JPanel(new BorderLayout()) {
                     @Override
                     protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D)g;
                        g2.setColor(this.getBackground());
                        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 6, 6);
                     }
                  };
                  box.setOpaque(false);
                  box.setBackground(Theme.BG_CARD);
                  box.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
                  box.setPreferredSize(new Dimension(66, 78));
                  box.setCursor(Cursor.getPredefinedCursor(12));
                  double scale = Math.min(50.0 / Math.max(1, w), 50.0 / Math.max(1, hOne));
                  int sw = Math.max(1, (int)(w * scale));
                  int sh = Math.max(1, (int)(hOne * scale));
                  Image scaled = frameImg.getScaledInstance(sw, sh, 4);
                  JLabel imgLbl = new JLabel(new ImageIcon(scaled), 0);
                  JLabel idxLbl = new JLabel("#" + frameIndex, 0);
                  idxLbl.setFont(Theme.F_TINY);
                  idxLbl.setForeground(Theme.TEXT_MUTED);
                  box.add(imgLbl, "Center");
                  box.add(idxLbl, "South");
                  box.addMouseListener(new MouseAdapter() {
                     @Override
                     public void mousePressed(MouseEvent e) {
                        MainUI.this.canvas.setPlaying(false);
                        MainUI.this.btnPlay.setText("▶ PLAY");
                        MainUI.this.canvas.setAnimTick(frameIndex);

                        for (Component c : MainUI.this.stripContainer.getComponents()) {
                           if (c instanceof JPanel) {
                              ((JPanel)c).setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
                           }
                        }

                        box.setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 2));
                     }

                     @Override
                     public void mouseEntered(MouseEvent e) {
                        box.setBackground(Theme.BG_HOVER);
                        box.repaint();
                     }

                     @Override
                     public void mouseExited(MouseEvent e) {
                        box.setBackground(Theme.BG_CARD);
                        box.repaint();
                     }
                  });
                  this.stripContainer.add(box);
               }
            } catch (Exception var19) {
               var19.printStackTrace();
            }
         } else {
            int spriteId = PetCanvas.getPetSpriteId(this.editingPet);
            String pathInfo = AppConfig.getResolvedPath("path_pet_img", "Data/Pet/img/");
            JLabel miss = new JLabel("  ⚠  Chưa có ảnh – ID file: " + spriteId + ".png trong " + pathInfo);
            miss.setFont(Theme.F_SMALL);
            miss.setForeground(Theme.WARNING);
            this.stripContainer.add(miss);
         }

         this.stripContainer.revalidate();
         this.stripContainer.repaint();
      }
   }

   private JPanel rightPanel() {
      this.rightPanel = new JPanel(new BorderLayout());
      this.rightPanel.setPreferredSize(new Dimension(340, 0));
      this.rightPanel.setBackground(Theme.BG_DARK);
      this.rightPanel.setBorder(new MatteBorder(0, 1, 0, 0, Theme.BORDER));
      JPanel inspector = new JPanel();
      inspector.setLayout(new BoxLayout(inspector, 1));
      inspector.setOpaque(false);
      inspector.setBorder(new EmptyBorder(15, 14, 15, 14));
      inspector.add(this.sectionHeader("THÔNG TIN CƠ BẢN"));
      inspector.add(Box.createVerticalStrut(10));
      this.txtId = StyledUI.createTextField("");
      this.txtName = StyledUI.createTextField("");
      this.txtIcon = StyledUI.createTextField("");
      inspector.add(this.propRow("ID Pet:", this.txtId));
      inspector.add(Box.createVerticalStrut(8));
      inspector.add(this.propRow("Tên:", this.txtName));
      inspector.add(Box.createVerticalStrut(8));
      inspector.add(this.propRow("Mã Icon:", this.txtIcon));
      inspector.add(Box.createVerticalStrut(8));
      this.txtFrame = StyledUI.createTextField("");
      inspector.add(this.propRow("Frame ID:", this.txtFrame));
      inspector.add(Box.createVerticalStrut(16));
      inspector.add(this.sectionHeader("LOẠI THÚ (TYPE)"));
      inspector.add(Box.createVerticalStrut(10));
      this.comboType = new JComboBox<>(PET_TYPE_LABELS);
      this.styleCombo(this.comboType);
      this.comboType.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
      this.comboType.setAlignmentX(0.0F);
      inspector.add(this.comboType);
      inspector.add(Box.createVerticalStrut(6));
      this.lblFrameInfo = new JLabel("", 2);
      this.lblFrameInfo.setFont(Theme.F_SMALL);
      this.lblFrameInfo.setForeground(Theme.ACCENT);
      this.lblFrameInfo.setAlignmentX(0.0F);
      inspector.add(this.lblFrameInfo);
      inspector.add(Box.createVerticalStrut(16));
      inspector.add(this.sectionHeader("THUỘC TÍNH (OP)"));
      inspector.add(Box.createVerticalStrut(10));
      this.txtOp = StyledUI.createTextField("");
      this.txtOp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
      this.txtOp.setAlignmentX(0.0F);
      inspector.add(this.txtOp);
      inspector.add(Box.createVerticalStrut(20));
      inspector.add(this.sectionHeader("THAO TÁC"));
      inspector.add(Box.createVerticalStrut(10));
      JPanel crudGrid = new JPanel(new GridLayout(1, 3, 8, 0));
      crudGrid.setOpaque(false);
      crudGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
      crudGrid.setAlignmentX(0.0F);
      JButton btnNew = StyledUI.createButton("Thêm", Theme.SUCCESS);
      JButton btnDelete = StyledUI.createButton("Xóa", Theme.ERROR);
      JButton btnSave = StyledUI.createButton("Lưu SQL", Theme.ACCENT);
      btnNew.addActionListener(e -> this.createNewPet());
      btnDelete.addActionListener(e -> this.deleteSelectedPet());
      btnSave.addActionListener(e -> this.saveSqlData());
      crudGrid.add(btnNew);
      crudGrid.add(btnDelete);
      crudGrid.add(btnSave);
      inspector.add(crudGrid);
      inspector.add(Box.createVerticalStrut(24));
      inspector.add(this.sectionHeader("XỬ LÝ HÌNH ẢNH (ZOOM 4)"));
      inspector.add(Box.createVerticalStrut(10));
      JButton btnImportV = StyledUI.createButton("Import Sprite Dọc", Theme.PURPLE);
      btnImportV.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
      btnImportV.setAlignmentX(0.0F);
      btnImportV.addActionListener(e -> this.importVerticalSheet());
      JButton btnStitchFiles = StyledUI.createButton("Ghép Ảnh Lẻ -> Dọc", Theme.PURPLE);
      btnStitchFiles.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
      btnStitchFiles.setAlignmentX(0.0F);
      btnStitchFiles.addActionListener(e -> this.stitchIndividualFrames());
      JButton btnStitchH = StyledUI.createButton("Cắt Ngang -> Dọc", Theme.PURPLE);
      btnStitchH.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
      btnStitchH.setAlignmentX(0.0F);
      btnStitchH.addActionListener(e -> this.stitchHorizontalSheet());
      inspector.add(btnImportV);
      inspector.add(Box.createVerticalStrut(8));
      inspector.add(btnStitchFiles);
      inspector.add(Box.createVerticalStrut(8));
      inspector.add(btnStitchH);
      DocumentListener dl = new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            MainUI.this.applyFields();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            MainUI.this.applyFields();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            MainUI.this.applyFields();
         }
      };
      this.txtId.getDocument().addDocumentListener(dl);
      this.txtName.getDocument().addDocumentListener(dl);
      this.txtIcon.getDocument().addDocumentListener(dl);
      this.txtFrame.getDocument().addDocumentListener(dl);
      this.txtOp.getDocument().addDocumentListener(dl);
      this.comboType.addActionListener(e -> {
         if (!this.isUpdatingFields) {
            int selIdx = this.comboType.getSelectedIndex();
            if (selIdx >= 0 && this.editingPet != null) {
               this.editingPet.type = PET_TYPE_IDS[selIdx];
               this.updateFrameInfoLabel(this.editingPet);
               this.canvas.reloadImageOnly();
               this.updateFrameStrip();
               this.petList.repaint();
            }
         }
      });
      JScrollPane scroll = new JScrollPane(inspector);
      StyledUI.styleScrollPane(scroll);
      this.rightPanel.add(scroll, "Center");
      return this.rightPanel;
   }

   private JPanel sectionHeader(String title) {
      JPanel row = new JPanel(new BorderLayout());
      row.setOpaque(false);
      row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
      row.setAlignmentX(0.0F);
      JLabel lbl = new JLabel(title);
      lbl.setFont(new Font("Segoe UI", 1, 11));
      lbl.setForeground(Theme.TEXT_MUTED);
      row.add(lbl, "West");
      JPanel line = new JPanel();
      line.setBackground(Theme.BORDER);
      line.setPreferredSize(new Dimension(0, 1));
      row.add(line, "South");
      return row;
   }

   private JPanel propRow(String label, JTextField tf) {
      JPanel row = new JPanel(new BorderLayout(8, 0));
      row.setOpaque(false);
      row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
      row.setAlignmentX(0.0F);
      JLabel l = StyledUI.createLabel(label, Theme.F_SMALL, Theme.TEXT_DIM);
      l.setPreferredSize(new Dimension(82, 36));
      tf.setPreferredSize(new Dimension(0, 36));
      row.add(l, "West");
      row.add(tf, "Center");
      return row;
   }

   private void styleCombo(JComboBox<?> cb) {
      cb.setBackground(Theme.BG_CARD);
      cb.setForeground(Theme.TEXT_MAIN);
      cb.setFont(Theme.F_SMALL);
      cb.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
      cb.setFocusable(false);
      cb.setRenderer(new DefaultListCellRenderer() {
         @Override
         public Component getListCellRendererComponent(JList<?> list, Object val, int idx, boolean isSel, boolean hasFocus) {
            JLabel lbl = (JLabel)super.getListCellRendererComponent(list, val, idx, isSel, hasFocus);
            lbl.setBackground(isSel ? Theme.ACCENT_SOFT : Theme.BG_CARD);
            lbl.setForeground(isSel ? Theme.ACCENT : Theme.TEXT_MAIN);
            lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
            lbl.setFont(Theme.F_SMALL);
            return lbl;
         }
      });
   }

   private void updateFields() {
      this.isUpdatingFields = true;
      if (this.editingPet == null) {
         this.txtId.setText("");
         this.txtName.setText("");
         this.txtIcon.setText("");
         this.txtFrame.setText("");
         this.txtOp.setText("");
         this.comboType.setSelectedIndex(0);
         this.lblFrameInfo.setText("");
      } else {
         this.txtId.setText(String.valueOf(this.editingPet.id));
         this.txtName.setText(this.editingPet.name);
         this.txtIcon.setText(String.valueOf(this.editingPet.icon));
         this.txtFrame.setText(String.valueOf(this.editingPet.frame));
         this.txtOp.setText(this.editingPet.op != null ? this.editingPet.op : "");
         int typeIdx = this.typeIdToIndex(this.editingPet.type);
         this.comboType.setSelectedIndex(typeIdx);
         this.updateFrameInfoLabel(this.editingPet);
      }

      this.isUpdatingFields = false;
   }

   private void updateFrameInfoLabel(PetTemplate p) {
      if (p == null) {
         this.lblFrameInfo.setText("");
      } else {
         int typeIdx = this.typeIdToIndex(p.type);
         int fc = PetCanvas.getFrameCount(p.type, p.frame);
         String desc = typeIdx >= 0 ? PET_TYPE_DESC[typeIdx] : "Type không xác định";
         this.lblFrameInfo.setText("<html><b>" + fc + " frame</b> – " + desc + "</html>");
      }
   }

   private int typeIdToIndex(int typeId) {
      for (int i = 0; i < PET_TYPE_IDS.length; i++) {
         if (PET_TYPE_IDS[i] == typeId) {
            return i;
         }
      }

      return 0;
   }

   private void applyFields() {
      if (this.editingPet != null && !this.isUpdatingFields) {
         try {
            this.editingPet.id = Integer.parseInt(this.txtId.getText().trim());
            this.editingPet.name = this.txtName.getText().trim();
            this.editingPet.icon = Integer.parseInt(this.txtIcon.getText().trim());
            this.editingPet.frame = Integer.parseInt(this.txtFrame.getText().trim());
            String opVal = this.txtOp.getText().trim();
            this.editingPet.op = opVal.isEmpty() ? null : opVal;
            this.updateFrameInfoLabel(this.editingPet);
            this.canvas.reloadImageOnly();
            this.updateFrameStrip();
            this.petList.repaint();
         } catch (NumberFormatException var2) {
         }
      }
   }

   private void createNewPet() {
      int nextId = 700;

      for (PetTemplate p : this.allPets) {
         if (p.id >= nextId) {
            nextId = p.id + 1;
         }
      }

      JPanel dlg = new JPanel(new GridLayout(4, 2, 8, 8));
      dlg.setBackground(Theme.BG_CARD);
      JTextField fId = new JTextField(String.valueOf(nextId));
      JTextField fName = new JTextField("Thú nuôi mới");
      JTextField fIcon = new JTextField(String.valueOf(nextId - 300));
      JTextField fFrame = new JTextField(String.valueOf(nextId - 500));
      dlg.add(new JLabel("ID:"));
      dlg.add(fId);
      dlg.add(new JLabel("Tên:"));
      dlg.add(fName);
      dlg.add(new JLabel("Mã Icon:"));
      dlg.add(fIcon);
      dlg.add(new JLabel("Frame ID:"));
      dlg.add(fFrame);
      int res = JOptionPane.showConfirmDialog(this, dlg, "Thêm Thú Nuôi Mới", 2, -1);
      if (res == 0) {
         try {
            PetTemplate newPet = new PetTemplate(
               Integer.parseInt(fId.getText().trim()),
               fName.getText().trim(),
               Integer.parseInt(fIcon.getText().trim()),
               21,
               Integer.parseInt(fFrame.getText().trim()),
               null
            );
            this.allPets.add(newPet);
            this.filterPets();

            for (int i = 0; i < this.petListModel.size(); i++) {
               if (this.petListModel.get(i).id == newPet.id) {
                  this.petList.setSelectedIndex(i);
                  break;
               }
            }
         } catch (NumberFormatException var10) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", 0);
         }
      }
   }

   private void deleteSelectedPet() {
      if (this.editingPet != null) {
         int choice = JOptionPane.showConfirmDialog(
            this, String.format("Xóa thú nuôi [%d] %s?", this.editingPet.id, this.editingPet.name), "Xác nhận xóa", 0, 2
         );
         if (choice == 0) {
            this.allPets.remove(this.editingPet);
            this.filterPets();
         }
      }
   }

   private void saveSqlData() {
      try {
         PetDataManager.savePets(this.allPets);
         JOptionPane.showMessageDialog(this, "Đã lưu thành công dữ liệu vào pet_template.sql!", "Thành công", 1);
      } catch (Exception var2) {
         var2.printStackTrace();
         JOptionPane.showMessageDialog(this, "Lỗi khi lưu SQL: " + var2.getMessage(), "Lỗi", 0);
      }
   }

   private int detectFrameCount(int width, int height) {
      int[] candidates = new int[]{3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 15, 16, 18, 20, 24};
      int bestN = 5;
      double bestDiff = Double.MAX_VALUE;

      for (int n : candidates) {
         double fh = (double)height / n;
         double aspect = fh / width;
         double diff = Math.min(Math.abs(aspect - 1.0), Math.abs(aspect - 0.6));
         if (diff < bestDiff) {
            bestDiff = diff;
            bestN = n;
         }
      }

      return bestN;
   }

   private String getBaseDir() {
      String d = AppConfig.getResolvedPath("path_pet_img", "Data/Pet/img/");
      if (!d.endsWith("/") && !d.endsWith("\\")) {
         d = d + "/";
      }

      return d;
   }

   private void importVerticalSheet() {
      if (this.editingPet == null) {
         this.warn("Vui lòng chọn hoặc tạo thú nuôi trước!");
      } else {
         FileDialog fd = new FileDialog((Frame)SwingUtilities.getWindowAncestor(this), "Chọn Sprite Sheet dọc (Zoom 4)", 0);
         fd.setDirectory(System.getProperty("user.dir"));
         fd.setVisible(true);
         if (fd.getFile() != null) {
            try {
               BufferedImage img = ImageIO.read(new File(fd.getDirectory(), fd.getFile()));
               if (img == null) {
                  this.err("Không đọc được ảnh!");
                  return;
               }

               int W = img.getWidth();
               int H = img.getHeight();
               int detectedN = this.detectFrameCount(W, H);
               int frameH = H / detectedN;
               if ((W < 16 || frameH < 16 || W > 512 || frameH > 512)
                  && JOptionPane.showConfirmDialog(
                        this, String.format("Cảnh báo: Kích thước mỗi frame (%dx%d) có vẻ bất thường.\nVẫn import?", W, frameH), "Cảnh báo", 0, 2
                     )
                     != 0) {
                  return;
               }

               File dest = new File(this.getBaseDir() + (this.editingPet.frame + 1000) + ".png");
               dest.getParentFile().mkdirs();
               ImageIO.write(img, "png", dest);
               int suggestedType = this.frameCountToType(detectedN);
               String msg = String.format(
                  "<html>Nhận dạng ảnh có <b>%d frame</b>.<br>Type phù hợp: <b>%s</b><br><br>Tự động cập nhật Type?</html>",
                  detectedN,
                  PET_TYPE_LABELS[this.typeIdToIndex(suggestedType)]
               );
               if (JOptionPane.showConfirmDialog(this, msg, "Cập nhật Type", 0) == 0) {
                  this.isUpdatingFields = true;
                  this.comboType.setSelectedIndex(this.typeIdToIndex(suggestedType));
                  this.isUpdatingFields = false;
                  this.editingPet.type = suggestedType;
                  this.updateFrameInfoLabel(this.editingPet);
               }

               this.canvas.reloadImageOnly();
               this.updateFrameStrip();
               JOptionPane.showMessageDialog(this, "Import thành công!", "Thành công", 1);
            } catch (Exception var10) {
               var10.printStackTrace();
               this.err("Lỗi import: " + var10.getMessage());
            }
         }
      }
   }

   private int frameCountToType(int n) {
      if (n == 9) {
         return 1;
      } else if (n == 6) {
         return 3;
      } else if (n == 3) {
         return 4;
      } else {
         return n == 7 ? 21 : 21;
      }
   }

   private void stitchIndividualFrames() {
      if (this.editingPet == null) {
         this.warn("Vui lòng chọn hoặc tạo thú nuôi trước!");
      } else {
         int currentFrames = PetCanvas.getFrameCount(this.editingPet.type, this.editingPet.frame);
         JPanel dlg = new JPanel(new GridLayout(2, 2, 8, 8));
         dlg.setBackground(Theme.BG_CARD);
         JComboBox<String> typeCombo = new JComboBox<>(PET_TYPE_LABELS);
         typeCombo.setSelectedIndex(this.typeIdToIndex(this.editingPet.type));
         JTextField tfN = new JTextField(String.valueOf(currentFrames));
         typeCombo.addActionListener(ex -> {
            int idx = typeCombo.getSelectedIndex();
            if (idx >= 0) {
               int fc = PetCanvas.getFrameCount(PET_TYPE_IDS[idx], this.editingPet.frame);
               tfN.setText(String.valueOf(fc));
            }
         });
         dlg.add(new JLabel("Loại thú:"));
         dlg.add(typeCombo);
         dlg.add(new JLabel("Số frame:"));
         dlg.add(tfN);
         if (JOptionPane.showConfirmDialog(this, dlg, "Ghép Ảnh Lẻ → Dọc", 2) == 0) {
            int nFrame;
            try {
               nFrame = Integer.parseInt(tfN.getText().trim());
               if (nFrame < 1 || nFrame > 50) {
                  throw new Exception();
               }
            } catch (Exception var17) {
               this.err("Số frame không hợp lệ!");
               return;
            }

            FileDialog fd = new FileDialog((Frame)SwingUtilities.getWindowAncestor(this), "Chọn các ảnh lẻ (giữ Ctrl để chọn nhiều)", 0);
            fd.setDirectory(System.getProperty("user.dir"));
            fd.setMultipleMode(true);
            fd.setVisible(true);
            File[] files = fd.getFiles();
            if (files != null && files.length != 0) {
               List<File> fileList = Arrays.asList(files);
               Collections.sort(fileList, (a, b) -> a.getName().compareTo(b.getName()));

               try {
                  List<BufferedImage> images = new ArrayList<>();
                  int maxW = 0;
                  int maxH = 0;

                  for (File file : fileList) {
                     BufferedImage tmp = ImageIO.read(file);
                     if (tmp != null) {
                        images.add(tmp);
                        maxW = Math.max(maxW, tmp.getWidth());
                        maxH = Math.max(maxH, tmp.getHeight());
                     }
                  }

                  if (images.isEmpty()) {
                     this.err("Không có ảnh hợp lệ!");
                     return;
                  }

                  BufferedImage stitched = new BufferedImage(maxW, maxH * nFrame, 2);
                  Graphics2D g2 = stitched.createGraphics();
                  g2.setComposite(AlphaComposite.Clear);
                  g2.fillRect(0, 0, maxW, maxH * nFrame);
                  g2.setComposite(AlphaComposite.SrcOver);

                  for (int i = 0; i < nFrame; i++) {
                     BufferedImage fi = images.get(Math.min(i, images.size() - 1));
                     g2.drawImage(fi, (maxW - fi.getWidth()) / 2, i * maxH + (maxH - fi.getHeight()), null);
                  }

                  g2.dispose();
                  File dest = new File(this.getBaseDir() + (this.editingPet.frame + 1000) + ".png");
                  dest.getParentFile().mkdirs();
                  ImageIO.write(stitched, "png", dest);
                  this.canvas.reloadImageOnly();
                  this.updateFrameStrip();
                  JOptionPane.showMessageDialog(this, "Ghép thành công!", "Thành công", 1);
               } catch (Exception var16) {
                  var16.printStackTrace();
                  this.err("Lỗi ghép ảnh: " + var16.getMessage());
               }
            }
         }
      }
   }

   private void stitchHorizontalSheet() {
      if (this.editingPet == null) {
         this.warn("Vui lòng chọn hoặc tạo thú nuôi trước!");
      } else {
         int currentFrames = PetCanvas.getFrameCount(this.editingPet.type, this.editingPet.frame);
         JPanel dlg = new JPanel(new GridLayout(2, 2, 8, 8));
         dlg.setBackground(Theme.BG_CARD);
         JComboBox<String> typeCombo = new JComboBox<>(PET_TYPE_LABELS);
         typeCombo.setSelectedIndex(this.typeIdToIndex(this.editingPet.type));
         JTextField tfN = new JTextField(String.valueOf(currentFrames));
         typeCombo.addActionListener(ex -> {
            int idx = typeCombo.getSelectedIndex();
            if (idx >= 0) {
               tfN.setText(String.valueOf(PetCanvas.getFrameCount(PET_TYPE_IDS[idx], this.editingPet.frame)));
            }
         });
         dlg.add(new JLabel("Loại thú:"));
         dlg.add(typeCombo);
         dlg.add(new JLabel("Số frame:"));
         dlg.add(tfN);
         if (JOptionPane.showConfirmDialog(this, dlg, "Cắt Ngang → Ghép Dọc", 2) == 0) {
            int nFrame;
            try {
               nFrame = Integer.parseInt(tfN.getText().trim());
               if (nFrame < 1 || nFrame > 50) {
                  throw new Exception();
               }
            } catch (Exception var14) {
               this.err("Số frame không hợp lệ!");
               return;
            }

            FileDialog fd = new FileDialog((Frame)SwingUtilities.getWindowAncestor(this), "Chọn Sprite Sheet ngang (Zoom 4)", 0);
            fd.setDirectory(System.getProperty("user.dir"));
            fd.setVisible(true);
            if (fd.getFile() != null) {
               try {
                  BufferedImage src = ImageIO.read(new File(fd.getDirectory(), fd.getFile()));
                  if (src == null) {
                     this.err("Không đọc được ảnh!");
                     return;
                  }

                  int fw = src.getWidth() / nFrame;
                  int fh = src.getHeight();
                  if (fw < 1) {
                     this.err("Ảnh quá nhỏ!");
                     return;
                  }

                  BufferedImage dest = new BufferedImage(fw, fh * nFrame, 2);
                  Graphics2D g2 = dest.createGraphics();
                  g2.setComposite(AlphaComposite.Clear);
                  g2.fillRect(0, 0, fw, fh * nFrame);
                  g2.setComposite(AlphaComposite.SrcOver);

                  for (int i = 0; i < nFrame; i++) {
                     g2.drawImage(src, 0, i * fh, fw, (i + 1) * fh, i * fw, 0, (i + 1) * fw, fh, null);
                  }

                  g2.dispose();
                  int spriteId = PetCanvas.getPetSpriteId(this.editingPet);
                  File destFile = new File(this.getBaseDir() + spriteId + ".png");
                  destFile.getParentFile().mkdirs();
                  ImageIO.write(dest, "png", destFile);
                  this.canvas.reloadImageOnly();
                  this.updateFrameStrip();
                  JOptionPane.showMessageDialog(this, "Cắt ghép thành công!", "Thành công", 1);
               } catch (Exception var13) {
                  var13.printStackTrace();
                  this.err("Lỗi: " + var13.getMessage());
               }
            }
         }
      }
   }

   private void warn(String msg) {
      JOptionPane.showMessageDialog(this, msg, "Thông báo", 2);
   }

   private void err(String msg) {
      JOptionPane.showMessageDialog(this, msg, "Lỗi", 0);
   }

   private static class PetListCellRenderer extends JPanel implements ListCellRenderer<PetTemplate> {
      private final JLabel idLabel;
      private final JLabel nameLabel;
      private final JLabel typeLabel;

      public PetListCellRenderer() {
         super(new BorderLayout(6, 0));
         this.setOpaque(true);
         this.setBorder(new EmptyBorder(6, 12, 6, 12));
         this.idLabel = new JLabel();
         this.idLabel.setFont(new Font("Consolas", 1, 11));
         this.idLabel.setPreferredSize(new Dimension(42, 14));
         this.nameLabel = new JLabel();
         this.nameLabel.setFont(Theme.F_BOLD);
         this.typeLabel = new JLabel();
         this.typeLabel.setFont(Theme.F_TINY);
         this.typeLabel.setHorizontalAlignment(4);
         this.add(this.idLabel, "West");
         this.add(this.nameLabel, "Center");
         this.add(this.typeLabel, "East");
      }

      public Component getListCellRendererComponent(JList<? extends PetTemplate> list, PetTemplate p, int index, boolean isSelected, boolean cellHasFocus) {
         this.setBackground(isSelected ? Theme.ACCENT_SOFT : (index % 2 == 0 ? Theme.BG_DARK : Theme.BG_CARD));
         if (p != null) {
            this.idLabel.setText(String.format("[%d]", p.id));
            this.idLabel.setForeground(isSelected ? Theme.ACCENT : Theme.TEXT_MUTED);
            this.nameLabel.setText(p.name);
            this.nameLabel.setForeground(isSelected ? Theme.ACCENT : Theme.TEXT_MAIN);
            this.typeLabel.setText(MainUI.getTypeShortLabel(p.type));
            this.typeLabel.setForeground(isSelected ? Theme.ACCENT : Theme.TEXT_MUTED);
         } else {
            this.idLabel.setText("");
            this.nameLabel.setText("");
            this.typeLabel.setText("");
         }

         return this;
      }
   }
}
