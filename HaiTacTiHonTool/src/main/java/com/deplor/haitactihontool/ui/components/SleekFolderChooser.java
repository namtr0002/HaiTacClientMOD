package com.deplor.haitactihontool.ui.components;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class SleekFolderChooser extends JDialog {
   private File currentDir;
   private File selectedFile;
   private final boolean directoriesOnly;
   private final DefaultListModel<SleekFolderChooser.FileItem> listModel;
   private final JList<SleekFolderChooser.FileItem> fileList;
   private final JTextField txtAddress;
   private boolean approved = false;
   private Point initialClick;

   public static String showFolderChooser(Component parent, String initialPath) {
      SleekFolderChooser chooser = new SleekFolderChooser((Frame)SwingUtilities.getWindowAncestor(parent), initialPath, true);
      chooser.setVisible(true);
      return chooser.isApproved() ? chooser.getSelectedPath() : null;
   }

   public static String showFileChooser(Component parent, String initialPath) {
      SleekFolderChooser chooser = new SleekFolderChooser((Frame)SwingUtilities.getWindowAncestor(parent), initialPath, false);
      chooser.setVisible(true);
      return chooser.isApproved() ? chooser.getSelectedPath() : null;
   }

   public SleekFolderChooser(Frame owner, String initialPath, boolean directoriesOnly) {
      super(owner, true);
      this.directoriesOnly = directoriesOnly;
      this.setUndecorated(true);
      this.setSize(700, 500);
      this.setLocationRelativeTo(owner);
      this.currentDir = new File(initialPath != null && !initialPath.isEmpty() ? initialPath : ".");
      if (!this.currentDir.exists() || !this.currentDir.isDirectory()) {
         this.currentDir = new File(".");
      }

      try {
         this.currentDir = this.currentDir.getAbsoluteFile().getCanonicalFile();
      } catch (Exception var5) {
      }

      this.listModel = new DefaultListModel<>();
      this.fileList = new JList<>(this.listModel);
      this.txtAddress = StyledUI.createTextField(this.currentDir.getAbsolutePath());
      this.initUI();
      this.refreshList();
   }

   private void initUI() {
      JPanel root = new JPanel(new BorderLayout());
      root.setBackground(Theme.BG_DARK);
      root.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
      this.setContentPane(root);
      JPanel titleBar = new JPanel(new BorderLayout());
      titleBar.setBackground(Theme.BG_DARKER);
      titleBar.setPreferredSize(new Dimension(0, 36));
      titleBar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      MouseAdapter dragListener = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            SleekFolderChooser.this.initialClick = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            int thisX = SleekFolderChooser.this.getLocation().x;
            int thisY = SleekFolderChooser.this.getLocation().y;
            SleekFolderChooser.this.setLocation(
               thisX + e.getX() - SleekFolderChooser.this.initialClick.x, thisY + e.getY() - SleekFolderChooser.this.initialClick.y
            );
         }
      };
      titleBar.addMouseListener(dragListener);
      titleBar.addMouseMotionListener(dragListener);
      JLabel lblTitle = StyledUI.createLabel(this.directoriesOnly ? "  CHOOSE DIRECTORY" : "  CHOOSE FILE", Theme.F_BOLD, Theme.TEXT_MAIN);
      titleBar.add(lblTitle, "West");
      JButton btnClose = new JButton("X") {
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
            g2.setColor(this.hovered ? Theme.ERROR : Theme.BG_DARKER);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            g2.setColor(Theme.TEXT_MAIN);
            g2.setFont(Theme.F_SMALL);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (this.getWidth() - fm.stringWidth(this.getText())) / 2, (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      btnClose.setPreferredSize(new Dimension(46, 36));
      btnClose.setContentAreaFilled(false);
      btnClose.setBorderPainted(false);
      btnClose.setFocusPainted(false);
      btnClose.setCursor(new Cursor(12));
      btnClose.addActionListener(e -> this.dispose());
      titleBar.add(btnClose, "East");
      root.add(titleBar, "North");
      JPanel toolbar = new JPanel(new BorderLayout(8, 0));
      toolbar.setOpaque(false);
      toolbar.setBorder(new EmptyBorder(12, 15, 6, 15));
      JButton btnUp = StyledUI.createButton("UP", Theme.BG_CARD);
      btnUp.setPreferredSize(new Dimension(60, 36));
      btnUp.addActionListener(e -> {
         File parent = this.currentDir.getParentFile();
         if (parent != null) {
            this.currentDir = parent;
            this.txtAddress.setText(this.currentDir.getAbsolutePath());
            this.refreshList();
         }
      });
      toolbar.add(btnUp, "West");
      this.txtAddress.addActionListener(e -> {
         File f = new File(this.txtAddress.getText().trim());
         if (f.exists() && f.isDirectory()) {
            this.currentDir = f;
            this.refreshList();
         }
      });
      toolbar.add(this.txtAddress, "Center");
      JPanel centerPanel = new JPanel(new BorderLayout());
      centerPanel.setOpaque(false);
      centerPanel.setBorder(new EmptyBorder(6, 15, 12, 15));
      this.fileList.setBackground(Theme.BG_CARD);
      this.fileList.setForeground(Theme.TEXT_MAIN);
      this.fileList.setSelectionBackground(Theme.ACCENT_SOFT);
      this.fileList.setSelectionForeground(Color.WHITE);
      this.fileList.setFont(Theme.F_MAIN);
      this.fileList.setCellRenderer(new SleekFolderChooser.FileListRenderer());
      this.fileList.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
               SleekFolderChooser.FileItem item = SleekFolderChooser.this.fileList.getSelectedValue();
               if (item != null) {
                  if (item.file.isDirectory()) {
                     SleekFolderChooser.this.currentDir = item.file;
                     SleekFolderChooser.this.txtAddress.setText(SleekFolderChooser.this.currentDir.getAbsolutePath());
                     SleekFolderChooser.this.refreshList();
                  } else if (!SleekFolderChooser.this.directoriesOnly) {
                     SleekFolderChooser.this.selectedFile = item.file;
                     SleekFolderChooser.this.approved = true;
                     SleekFolderChooser.this.dispose();
                  }
               }
            }
         }
      });
      JScrollPane scroll = new JScrollPane(this.fileList);
      StyledUI.styleScrollPane(scroll);
      centerPanel.add(scroll, "Center");
      JPanel contentWrapper = new JPanel(new BorderLayout());
      contentWrapper.setOpaque(false);
      contentWrapper.add(toolbar, "North");
      contentWrapper.add(centerPanel, "Center");
      root.add(contentWrapper, "Center");
      JPanel bottom = new JPanel(new FlowLayout(2, 12, 12));
      bottom.setBackground(Theme.BG_DARKER);
      bottom.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      JButton btnCancel = StyledUI.createButton("CANCEL", Theme.BG_CARD);
      btnCancel.setPreferredSize(new Dimension(100, 36));
      btnCancel.addActionListener(e -> this.dispose());
      JButton btnSelect = StyledUI.createButton("SELECT", Theme.ACCENT);
      btnSelect.setPreferredSize(new Dimension(100, 36));
      btnSelect.addActionListener(e -> {
         if (this.directoriesOnly) {
            this.selectedFile = this.currentDir;
            this.approved = true;
            this.dispose();
         } else {
            SleekFolderChooser.FileItem item = this.fileList.getSelectedValue();
            if (item != null && !item.file.isDirectory()) {
               this.selectedFile = item.file;
               this.approved = true;
               this.dispose();
            } else {
               JOptionPane.showMessageDialog(this, "Please select a file.", "Error", 2);
            }
         }
      });
      bottom.add(btnCancel);
      bottom.add(btnSelect);
      root.add(bottom, "South");
   }

   private void refreshList() {
      this.listModel.clear();

      try {
         this.currentDir = this.currentDir.getAbsoluteFile().getCanonicalFile();
      } catch (Exception var5) {
      }

      File[] files = this.currentDir.listFiles();
      if (files != null) {
         List<File> list = new ArrayList<>(Arrays.asList(files));
         Collections.sort(list, (f1, f2) -> {
            if (f1.isDirectory() && !f2.isDirectory()) {
               return -1;
            } else {
               return !f1.isDirectory() && f2.isDirectory() ? 1 : f1.getName().compareToIgnoreCase(f2.getName());
            }
         });

         for (File f : list) {
            if (!this.directoriesOnly || f.isDirectory()) {
               this.listModel.addElement(new SleekFolderChooser.FileItem(f));
            }
         }
      }
   }

   public boolean isApproved() {
      return this.approved;
   }

   public String getSelectedPath() {
      return this.selectedFile != null ? this.selectedFile.getAbsolutePath() : null;
   }

   private static class FileItem {
      final File file;

      FileItem(File file) {
         this.file = file;
      }

      @Override
      public String toString() {
         return this.file.getName();
      }
   }

   private static class FileListRenderer extends DefaultListCellRenderer {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         JPanel p = new JPanel(new BorderLayout(10, 0));
         p.setOpaque(true);
         p.setBackground(isSelected ? Theme.ACCENT_SOFT : Theme.BG_CARD);
         p.setBorder(new EmptyBorder(8, 12, 8, 12));
         if (value instanceof SleekFolderChooser.FileItem item) {
            String icon = item.file.isDirectory() ? "DIR" : "FILE";
            JLabel lblIcon = StyledUI.createLabel("[" + icon + "]", Theme.F_MONO, Theme.ACCENT);
            p.add(lblIcon, "West");
            JLabel lblName = StyledUI.createLabel(item.file.getName(), Theme.F_MAIN, isSelected ? Color.WHITE : Theme.TEXT_MAIN);
            p.add(lblName, "Center");
         }

         if (isSelected) {
            p.setBorder(new MatteBorder(0, 3, 0, 0, Theme.ACCENT));
         }

         return p;
      }
   }
}
