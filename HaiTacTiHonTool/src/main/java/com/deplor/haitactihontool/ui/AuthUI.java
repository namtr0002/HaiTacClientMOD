package com.deplor.haitactihontool.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class AuthUI extends JDialog {
   private JTextField keyField;
   private JLabel statusLabel;
   private JButton loginBtn;
   private Consumer<String> onAuthSuccess;

   public AuthUI(Consumer<String> onAuthSuccess) {
      this.onAuthSuccess = onAuthSuccess;
      this.setTitle("Authentication Required");
      this.setModal(true);
      this.setUndecorated(true);
      this.setSize(380, 240);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(2);
      this.initComponents();
   }

   private void initComponents() {
      JPanel content = new JPanel(new BorderLayout());
      content.setBackground(new Color(25, 25, 30));
      content.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70), 1));
      this.setContentPane(content);
      JPanel header = new JPanel(new BorderLayout());
      header.setOpaque(false);
      header.setBorder(new EmptyBorder(15, 20, 5, 20));
      JLabel titleLabel = new JLabel("HTTH TOOL SUITE");
      titleLabel.setFont(new Font("SansSerif", 1, 14));
      titleLabel.setForeground(new Color(0, 150, 255));
      header.add(titleLabel, "West");
      JLabel closeBtn = new JLabel("✕");
      closeBtn.setForeground(new Color(150, 150, 160));
      closeBtn.setCursor(new Cursor(12));
      closeBtn.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            System.exit(0);
         }
      });
      header.add(closeBtn, "East");
      content.add(header, "North");
      JPanel body = new JPanel(new GridBagLayout());
      body.setOpaque(false);
      body.setBorder(new EmptyBorder(0, 30, 10, 30));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = 2;
      gbc.gridx = 0;
      gbc.weightx = 1.0;
      JLabel hintLabel = new JLabel("Please enter your access key:");
      hintLabel.setFont(new Font("SansSerif", 0, 11));
      hintLabel.setForeground(new Color(180, 180, 190));
      gbc.insets = new Insets(0, 0, 8, 0);
      body.add(hintLabel, gbc);
      this.keyField = new JTextField();
      this.keyField.setBackground(new Color(35, 35, 45));
      this.keyField.setForeground(Color.WHITE);
      this.keyField.setCaretColor(new Color(0, 150, 255));
      this.keyField.setFont(new Font("SansSerif", 0, 13));
      this.keyField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70, 70, 80)), new EmptyBorder(8, 12, 8, 12)));
      gbc.insets = new Insets(0, 0, 15, 0);
      body.add(this.keyField, gbc);
      this.loginBtn = new JButton("ACCESS TOOL");
      this.loginBtn.setBackground(new Color(0, 120, 215));
      this.loginBtn.setForeground(Color.WHITE);
      this.loginBtn.setFont(new Font("SansSerif", 1, 12));
      this.loginBtn.setFocusPainted(false);
      this.loginBtn.setBorder(new EmptyBorder(10, 0, 10, 0));
      this.loginBtn.setCursor(new Cursor(12));
      this.loginBtn.addActionListener(e -> this.handleLogin());
      body.add(this.loginBtn, gbc);
      this.statusLabel = new JLabel(" ");
      this.statusLabel.setHorizontalAlignment(0);
      this.statusLabel.setFont(new Font("SansSerif", 0, 11));
      this.statusLabel.setForeground(new Color(255, 80, 80));
      body.add(this.statusLabel, gbc);
      content.add(body, "Center");
      MouseAdapter dragListener = new MouseAdapter() {
         private Point mouseDownCompCoords = null;

         @Override
         public void mouseReleased(MouseEvent e) {
            this.mouseDownCompCoords = null;
         }

         @Override
         public void mousePressed(MouseEvent e) {
            this.mouseDownCompCoords = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            AuthUI.this.setLocation(currCoords.x - this.mouseDownCompCoords.x, currCoords.y - this.mouseDownCompCoords.y);
         }
      };
      header.addMouseListener(dragListener);
      header.addMouseMotionListener(dragListener);
   }

   private void handleLogin() {
      String key = this.keyField.getText().trim();
      if (key.isEmpty()) {
         this.showError("Access key cannot be empty.");
      } else {
         this.loginBtn.setEnabled(false);
         this.loginBtn.setText("VERIFYING...");
         this.statusLabel.setText("Connecting to server...");
         this.statusLabel.setForeground(new Color(150, 150, 160));
         Timer timer = new Timer(1000, e -> {
            if (this.isValidKey(key)) {
               this.dispose();
               this.onAuthSuccess.accept(key);
            } else {
               this.showError("Invalid access key. Please try again.");
               this.loginBtn.setEnabled(true);
               this.loginBtn.setText("ACCESS TOOL");
            }
         });
         timer.setRepeats(false);
         timer.start();
      }
   }

   private boolean isValidKey(String key) {
      return key.startsWith("HTTH-") && key.length() > 10;
   }

   private void showError(String msg) {
      this.statusLabel.setText(msg);
      this.statusLabel.setForeground(new Color(255, 80, 80));
      Point loc = this.getLocation();
      Timer shake = new Timer(50, null);
      int[] count = new int[]{0};
      shake.addActionListener(e -> {
         int offset = count[0] % 2 == 0 ? 5 : -5;
         this.setLocation(loc.x + offset, loc.y);
         count[0]++;
         if (count[0] > 6) {
            this.setLocation(loc);
            shake.stop();
         }
      });
      shake.start();
   }
}
