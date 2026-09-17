package com.deplor.haitactihontool.ultimate_security;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.map.ImageCache;
import com.deplor.haitactihontool.map.MapDataLoader;
import com.deplor.haitactihontool.ui.MainFrame;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.RoundRectangle2D.Double;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class LoginUI extends JFrame {
   private static final String PREF_REMEMBER_KEY = "htth_remembered_key";
   private static final Preferences prefs = Preferences.userNodeForPackage(LoginUI.class);
   private JPasswordField txtKey;
   private JTextField txtKeyVisible;
   private boolean isKeyVisible = false;
   private JButton btnEyeToggle;
   private JCheckBox chkRememberKey;
   private JLabel lblStatus;
   private JLabel lblHWID;
   private JButton btnLogin;
   private Point dragPoint;
   private Timer animTimer;
   private float pulseScale = 1.0F;
   private float pulseAngle = 0.0F;
   private boolean pulseGrowing = true;
   private final List<LoginUI.Node> nodes = new ArrayList<>();
   private final Random rnd = new Random();
   private String toastMessage = "";
   private float toastOpacity = 0.0F;
   private Timer toastTimer;

   public LoginUI() {
      this.setSize(500, 720);
      this.setDefaultCloseOperation(3);
      this.setLocationRelativeTo(null);
      this.setResizable(false);
      this.setUndecorated(true);
      this.setShape(new Double(0.0, 0.0, 500.0, 720.0, 36.0, 36.0));
      this.initNodes();
      this.buildUI();
      this.loadRememberedKey();
      this.startAnimations();
   }

   private void initNodes() {
      this.nodes.clear();

      for (int i = 0; i < 45; i++) {
         this.nodes.add(new LoginUI.Node(500, 720));
      }
   }

   private void startAnimations() {
      this.animTimer = new Timer(25, e -> {
         this.pulseAngle += 0.03F;
         if (this.pulseAngle > Math.PI * 2) {
            this.pulseAngle = 0.0F;
         }

         if (this.pulseGrowing) {
            this.pulseScale += 0.003F;
            if (this.pulseScale > 1.08F) {
               this.pulseGrowing = false;
            }
         } else {
            this.pulseScale -= 0.003F;
            if (this.pulseScale < 0.94F) {
               this.pulseGrowing = true;
            }
         }

         for (LoginUI.Node n : this.nodes) {
            n.x = n.x + n.vx;
            n.y = n.y + n.vy;
            if (n.x < 10.0 || n.x > this.getWidth() - 10) {
               n.vx *= -1.0;
            }

            if (n.y < 10.0 || n.y > this.getHeight() - 10) {
               n.vy *= -1.0;
            }
         }

         if (this.toastOpacity > 0.0F && (this.toastTimer == null || !this.toastTimer.isRunning())) {
            this.toastOpacity -= 0.03F;
            if (this.toastOpacity < 0.0F) {
               this.toastOpacity = 0.0F;
            }
         }

         this.repaint();
      });
      this.animTimer.start();
   }

   private void buildUI() {
      JPanel mainPanel = new JPanel(null) {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = this.getWidth();
            int h = this.getHeight();
            GradientPaint bgGradient = new GradientPaint(0.0F, 0.0F, new Color(8, 9, 16), w, h, new Color(15, 12, 28));
            g2.setPaint(bgGradient);
            g2.fill(new Double(0.0, 0.0, w, h, 36.0, 36.0));
            RadialGradientPaint glow1 = new RadialGradientPaint(
               new Float(120.0F, 100.0F), 250.0F, new float[]{0.0F, 1.0F}, new Color[]{new Color(0, 240, 255, 25), new Color(0, 0, 0, 0)}
            );
            g2.setPaint(glow1);
            g2.fill(new Double(0.0, 0.0, w, h, 36.0, 36.0));
            RadialGradientPaint glow2 = new RadialGradientPaint(
               new Float(380.0F, 600.0F), 300.0F, new float[]{0.0F, 1.0F}, new Color[]{new Color(157, 0, 255, 20), new Color(0, 0, 0, 0)}
            );
            g2.setPaint(glow2);
            g2.fill(new Double(0.0, 0.0, w, h, 36.0, 36.0));

            for (int i = 0; i < LoginUI.this.nodes.size(); i++) {
               LoginUI.Node n1 = LoginUI.this.nodes.get(i);

               for (int j = i + 1; j < LoginUI.this.nodes.size(); j++) {
                  LoginUI.Node n2 = LoginUI.this.nodes.get(j);
                  double dist = Math.hypot(n1.x - n2.x, n1.y - n2.y);
                  if (dist < 110.0) {
                     int alpha = (int)((1.0 - dist / 110.0) * 70.0);
                     g2.setColor(new Color(0, 200, 255, alpha));
                     g2.setStroke(new BasicStroke(0.7F));
                     g2.drawLine((int)n1.x, (int)n1.y, (int)n2.x, (int)n2.y);
                  }
               }

               g2.setColor(n1.color);
               int size = (int)n1.radius;
               g2.fill(new java.awt.geom.Ellipse2D.Double(n1.x - size / 2.0, n1.y - size / 2.0, size, size));
            }

            int cx = w / 2;
            int cy = 160;
            float p = LoginUI.this.pulseScale;
            AffineTransform oldTx = g2.getTransform();
            g2.rotate(LoginUI.this.pulseAngle, cx, cy);
            g2.setStroke(new BasicStroke(1.5F, 1, 1, 1.0F, new float[]{8.0F, 12.0F}, 0.0F));
            g2.setColor(new Color(0, 240, 255, 45));
            g2.draw(new java.awt.geom.Ellipse2D.Double(cx - 75.0F * p, cy - 75.0F * p, 150.0F * p, 150.0F * p));
            g2.rotate(-LoginUI.this.pulseAngle * 2.0F, cx, cy);
            g2.setStroke(new BasicStroke(1.2F, 1, 1, 1.0F, new float[]{4.0F, 8.0F}, 0.0F));
            g2.setColor(new Color(157, 0, 255, 40));
            g2.draw(new java.awt.geom.Ellipse2D.Double(cx - 92.0F * p, cy - 92.0F * p, 184.0F * p, 184.0F * p));
            g2.setTransform(oldTx);
            g2.setPaint(
               new RadialGradientPaint(new Float(cx, cy), 50.0F, new float[]{0.0F, 1.0F}, new Color[]{new Color(0, 220, 255, 60), new Color(0, 0, 0, 0)})
            );
            g2.fill(new java.awt.geom.Ellipse2D.Double(cx - 50, cy - 50, 100.0, 100.0));
            LoginUI.this.drawCyberEmblem(g2, cx, cy);
            g2.setStroke(new BasicStroke(1.5F));
            g2.setPaint(new GradientPaint(0.0F, 0.0F, new Color(0, 240, 255, 120), w, h, new Color(157, 0, 255, 120)));
            g2.draw(new Double(1.0, 1.0, w - 2, h - 2, 36.0, 36.0));
            if (LoginUI.this.toastOpacity > 0.01F) {
               g2.setComposite(AlphaComposite.getInstance(3, LoginUI.this.toastOpacity));
               g2.setFont(new Font("Segoe UI", 1, 12));
               FontMetrics fm = g2.getFontMetrics();
               int tw = fm.stringWidth(LoginUI.this.toastMessage) + 36;
               int th = 34;
               int tx = (w - tw) / 2;
               int ty = h - 65;
               g2.setColor(new Color(20, 25, 40, 230));
               g2.fill(new Double(tx, ty, tw, th, 16.0, 16.0));
               g2.setColor(new Color(0, 240, 255, 180));
               g2.setStroke(new BasicStroke(1.0F));
               g2.draw(new Double(tx, ty, tw, th, 16.0, 16.0));
               g2.setColor(Color.WHITE);
               g2.drawString(LoginUI.this.toastMessage, tx + 18, ty + 22);
            }

            g2.dispose();
         }
      };
      mainPanel.setOpaque(false);
      this.setContentPane(mainPanel);
      JPanel topBar = new JPanel(new FlowLayout(2, 10, 12));
      topBar.setOpaque(false);
      topBar.setBounds(0, 0, 500, 50);
      JButton btnClose = this.createMinimalIcon("✕", e -> System.exit(0));
      topBar.add(btnClose);
      mainPanel.add(topBar);
      JPanel stack = new JPanel();
      stack.setLayout(new BoxLayout(stack, 1));
      stack.setOpaque(false);
      stack.setBounds(50, 260, 400, 430);
      JLabel lblTitle = new JLabel("HẢI TẶC TÍ HÓN TOOL");
      lblTitle.setFont(new Font("Segoe UI", 1, 24));
      lblTitle.setForeground(Color.WHITE);
      lblTitle.setAlignmentX(0.5F);
      JLabel lblSub = new JLabel("VIP ULTIMATE EDITION • NEXUS V7.5");
      lblSub.setFont(new Font("Segoe UI", 1, 10));
      lblSub.setForeground(new Color(0, 220, 255));
      lblSub.setAlignmentX(0.5F);
      stack.add(lblTitle);
      stack.add(Box.createVerticalStrut(4));
      stack.add(lblSub);
      stack.add(Box.createVerticalStrut(32));
      JPanel keyPanel = new JPanel(new BorderLayout(8, 0)) {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean focused = LoginUI.this.txtKey != null && LoginUI.this.txtKey.isFocusOwner()
               || LoginUI.this.txtKeyVisible != null && LoginUI.this.txtKeyVisible.isFocusOwner();
            g2.setColor(new Color(20, 24, 38, 200));
            g2.fill(new Double(0.0, 0.0, this.getWidth(), this.getHeight(), 16.0, 16.0));
            if (focused) {
               g2.setColor(new Color(0, 220, 255, 200));
               g2.setStroke(new BasicStroke(1.5F));
            } else {
               g2.setColor(new Color(255, 255, 255, 30));
               g2.setStroke(new BasicStroke(1.0F));
            }

            g2.draw(new Double(0.0, 0.0, this.getWidth() - 1, this.getHeight() - 1, 16.0, 16.0));
            g2.dispose();
         }
      };
      keyPanel.setOpaque(false);
      keyPanel.setPreferredSize(new Dimension(400, 52));
      keyPanel.setMaximumSize(new Dimension(400, 52));
      keyPanel.setBorder(new EmptyBorder(4, 14, 4, 10));
      JLabel lblLockIcon = new JLabel("\ud83d\udd11");
      lblLockIcon.setFont(new Font("Segoe UI Emoji", 0, 16));
      keyPanel.add(lblLockIcon, "West");
      JPanel textContainer = new JPanel(new CardLayout());
      textContainer.setOpaque(false);
      this.txtKey = new JPasswordField();
      this.styleInputField(this.txtKey);
      this.txtKeyVisible = new JTextField();
      this.styleInputField(this.txtKeyVisible);
      textContainer.add(this.txtKey, "PASS");
      textContainer.add(this.txtKeyVisible, "TEXT");
      keyPanel.add(textContainer, "Center");
      this.btnEyeToggle = new JButton("\ud83d\udc41");
      this.btnEyeToggle.setFont(new Font("Segoe UI Emoji", 0, 16));
      this.btnEyeToggle.setForeground(new Color(160, 170, 190));
      this.btnEyeToggle.setFocusPainted(false);
      this.btnEyeToggle.setBorderPainted(false);
      this.btnEyeToggle.setContentAreaFilled(false);
      this.btnEyeToggle.setCursor(new Cursor(12));
      this.btnEyeToggle.setToolTipText("Hiện/Ẩn mật khẩu");
      this.btnEyeToggle.addActionListener(e -> this.toggleKeyVisibility(textContainer));
      keyPanel.add(this.btnEyeToggle, "East");
      stack.add(keyPanel);
      stack.add(Box.createVerticalStrut(10));
      JPanel optionsPanel = new JPanel(new BorderLayout());
      optionsPanel.setOpaque(false);
      optionsPanel.setMaximumSize(new Dimension(400, 24));
      this.chkRememberKey = new JCheckBox("Ghi nhớ Key");
      this.chkRememberKey.setOpaque(false);
      this.chkRememberKey.setFont(new Font("Segoe UI", 0, 12));
      this.chkRememberKey.setForeground(new Color(170, 180, 200));
      this.chkRememberKey.setFocusPainted(false);
      this.chkRememberKey.setCursor(new Cursor(12));
      optionsPanel.add(this.chkRememberKey, "West");
      stack.add(optionsPanel);
      stack.add(Box.createVerticalStrut(22));
      JPanel hwidCard = new JPanel(new BorderLayout(8, 0)) {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(15, 18, 30, 180));
            g2.fill(new Double(0.0, 0.0, this.getWidth(), this.getHeight(), 12.0, 12.0));
            g2.setColor(new Color(255, 255, 255, 15));
            g2.draw(new Double(0.0, 0.0, this.getWidth() - 1, this.getHeight() - 1, 12.0, 12.0));
            g2.dispose();
         }
      };
      hwidCard.setOpaque(false);
      hwidCard.setPreferredSize(new Dimension(400, 36));
      hwidCard.setMaximumSize(new Dimension(400, 36));
      hwidCard.setBorder(new EmptyBorder(4, 12, 4, 12));
      this.lblHWID = new JLabel("STATION ID: " + KeyAuth.getHWID());
      this.lblHWID.setFont(new Font("Consolas", 1, 12));
      this.lblHWID.setForeground(new Color(150, 170, 200));
      hwidCard.add(this.lblHWID, "West");
      JButton btnCopy = new JButton("SAO CHÉP");
      btnCopy.setFont(new Font("Segoe UI", 1, 10));
      btnCopy.setForeground(new Color(0, 220, 255));
      btnCopy.setFocusPainted(false);
      btnCopy.setBorderPainted(false);
      btnCopy.setContentAreaFilled(false);
      btnCopy.setCursor(new Cursor(12));
      btnCopy.addActionListener(e -> {
         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(KeyAuth.getHWID()), null);
         this.showToast("✓ Đã sao chép HWID vào Clipboard!");
      });
      hwidCard.add(btnCopy, "East");
      stack.add(hwidCard);
      stack.add(Box.createVerticalStrut(26));
      this.btnLogin = new JButton("ĐĂNG NHẬP HỆ THỐNG") {
         private boolean isHovered = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  isHovered = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  isHovered = false;
                  repaint();
               }
            });
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = this.getWidth();
            int h = this.getHeight();
            if (this.isEnabled()) {
               Color c1 = this.isHovered ? new Color(0, 240, 255) : new Color(0, 180, 255);
               Color c2 = this.isHovered ? new Color(180, 60, 255) : new Color(140, 40, 240);
               g2.setPaint(new GradientPaint(0.0F, 0.0F, c1, w, 0.0F, c2));
               if (this.isHovered) {
                  g2.setColor(new Color(0, 220, 255, 60));
                  g2.fill(new Double(0.0, 2.0, w, h, 18.0, 18.0));
               }
            } else {
               g2.setColor(new Color(40, 45, 60));
            }

            g2.fill(new Double(0.0, 0.0, w, h, 16.0, 16.0));
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", 1, 13));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (w - fm.stringWidth(this.getText())) / 2, (h + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      this.btnLogin.setPreferredSize(new Dimension(400, 50));
      this.btnLogin.setMaximumSize(new Dimension(400, 50));
      this.btnLogin.setFocusPainted(false);
      this.btnLogin.setBorderPainted(false);
      this.btnLogin.setContentAreaFilled(false);
      this.btnLogin.setCursor(new Cursor(12));
      this.btnLogin.addActionListener(e -> this.triggerLogin());
      stack.add(this.btnLogin);
      ActionListener enterAction = e -> this.triggerLogin();
      this.txtKey.addActionListener(enterAction);
      this.txtKeyVisible.addActionListener(enterAction);
      stack.add(Box.createVerticalStrut(18));
      this.lblStatus = new JLabel("HỆ THỐNG SẴN SÀNG");
      this.lblStatus.setFont(new Font("Segoe UI", 1, 10));
      this.lblStatus.setForeground(new Color(150, 160, 180, 180));
      this.lblStatus.setAlignmentX(0.5F);
      stack.add(this.lblStatus);
      mainPanel.add(stack);
      MouseAdapter drag = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            LoginUI.this.dragPoint = e.getPoint();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            if (LoginUI.this.dragPoint != null) {
               Point s = e.getLocationOnScreen();
               LoginUI.this.setLocation(s.x - LoginUI.this.dragPoint.x, s.y - LoginUI.this.dragPoint.y);
            }
         }
      };
      mainPanel.addMouseListener(drag);
      mainPanel.addMouseMotionListener(drag);
   }

   private void styleInputField(final JTextField field) {
      field.setOpaque(false);
      field.setForeground(Color.WHITE);
      field.setCaretColor(new Color(0, 240, 255));
      field.setFont(new Font("Segoe UI", 0, 15));
      field.setBorder(null);
      if (field instanceof JPasswordField) {
         ((JPasswordField)field).setEchoChar('•');
      }

      field.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent e) {
            if (field.getParent() != null && field.getParent().getParent() != null) {
               field.getParent().getParent().repaint();
            }
         }

         @Override
         public void focusLost(FocusEvent e) {
            if (field.getParent() != null && field.getParent().getParent() != null) {
               field.getParent().getParent().repaint();
            }
         }
      });
   }

   private void toggleKeyVisibility(JPanel container) {
      CardLayout cl = (CardLayout)container.getLayout();
      if (this.isKeyVisible) {
         this.txtKey.setText(this.txtKeyVisible.getText());
         cl.show(container, "PASS");
         this.btnEyeToggle.setText("\ud83d\udc41");
         this.btnEyeToggle.setToolTipText("Hiện mật khẩu");
         this.txtKey.requestFocusInWindow();
      } else {
         this.txtKeyVisible.setText(new String(this.txtKey.getPassword()));
         cl.show(container, "TEXT");
         this.btnEyeToggle.setText("\ud83d\ude48");
         this.btnEyeToggle.setToolTipText("Ẩn mật khẩu");
         this.txtKeyVisible.requestFocusInWindow();
      }

      this.isKeyVisible = !this.isKeyVisible;
   }

   private String getEnteredKey() {
      return this.isKeyVisible ? this.txtKeyVisible.getText().trim() : new String(this.txtKey.getPassword()).trim();
   }

   private void setEnteredKey(String key) {
      this.txtKey.setText(key);
      this.txtKeyVisible.setText(key);
   }

   private void loadRememberedKey() {
      String savedKey = prefs.get("htth_remembered_key", "");
      if (!savedKey.isEmpty()) {
         this.setEnteredKey(savedKey);
         this.chkRememberKey.setSelected(true);
      }
   }

   private void saveRememberedKey(String key) {
      if (this.chkRememberKey.isSelected()) {
         prefs.put("htth_remembered_key", key);
      } else {
         prefs.remove("htth_remembered_key");
      }
   }

   private void showToast(String msg) {
      this.toastMessage = msg;
      this.toastOpacity = 1.0F;
      if (this.toastTimer != null && this.toastTimer.isRunning()) {
         this.toastTimer.stop();
      }

      this.toastTimer = new Timer(2200, e -> {});
      this.toastTimer.setRepeats(false);
      this.toastTimer.start();
      this.repaint();
   }

   private void drawCyberEmblem(Graphics2D g2, int cx, int cy) {
      Path2D shield = new java.awt.geom.Path2D.Double();
      shield.moveTo(cx, cy - 35);
      shield.lineTo(cx + 30, cy - 20);
      shield.lineTo(cx + 25, cy + 15);
      shield.lineTo(cx, cy + 35);
      shield.lineTo(cx - 25, cy + 15);
      shield.lineTo(cx - 30, cy - 20);
      shield.closePath();
      g2.setPaint(new GradientPaint(cx - 30, cy - 35, new Color(0, 240, 255, 80), cx + 30, cy + 35, new Color(157, 0, 255, 80)));
      g2.fill(shield);
      g2.setColor(new Color(0, 240, 255, 200));
      g2.setStroke(new BasicStroke(2.0F));
      g2.draw(shield);
      g2.setColor(Color.WHITE);
      g2.setFont(new Font("Segoe UI", 1, 28));
      FontMetrics fm = g2.getFontMetrics();
      String logo = "H";
      g2.drawString(logo, cx - fm.stringWidth(logo) / 2, cy + fm.getAscent() / 2 - 4);
   }

   private JButton createMinimalIcon(String text, ActionListener al) {
      JButton b = new JButton(text);
      b.setFont(new Font("Segoe UI", 0, 15));
      b.setForeground(new Color(160, 170, 190));
      b.setFocusPainted(false);
      b.setBorderPainted(false);
      b.setContentAreaFilled(false);
      b.setCursor(new Cursor(12));
      b.addActionListener(al);
      return b;
   }

   private void setStatus(String msg, Color color) {
      this.lblStatus.setText(msg.toUpperCase());
      this.lblStatus.setForeground(color);
   }

   private void triggerLogin() {
      String key = this.getEnteredKey();
      if (key.isEmpty()) {
         this.setStatus("VUI LÒNG NHẬP MÃ KEY XÁC THỰC", Theme.ERROR);
         this.shakeFrame();
      } else {
         this.saveRememberedKey(key);
         this.btnLogin.setEnabled(false);
         this.setStatus("ĐANG XÁC THỰC MÃ ACCESS KEY...", new Color(0, 220, 255));
         new Thread(() -> {
            try {
               boolean ok = KeyAuth.checkKey(key);
               SwingUtilities.invokeLater(() -> {
                  if (!ok) {
                     this.btnLogin.setEnabled(true);
                     this.setStatus("XÁC THỰC THẤP BẠI: MÃ KEY KHÔNG HỢP LỆ", Theme.ERROR);
                     this.shakeFrame();
                  } else {
                     this.setStatus("XÁC THỰC THÀNH CÔNG! ĐANG TẢI ASSET...", Theme.SUCCESS);
                     new Thread(() -> {
                        try {
                           System.out.println(">> preloadAll START");
                           ImageCache.preloadAll("", (c, t, msg) -> SwingUtilities.invokeLater(() -> {
                              if (t > 0) {
                                 int p = (int)(c * 100.0F / t);
                                 this.setStatus(String.format("ĐANG TẢI TÀI NGUYÊN... %d%% [%d/%d]", p, c, t), new Color(0, 220, 255));
                              } else {
                                 this.setStatus(msg, new Color(0, 220, 255));
                              }
                           }));
                           System.out.println(">> preloadAll DONE");
                           System.out.println(">> loadAllMaps START");
                           MapDataLoader.loadAllMaps("", (c, t, msg) -> SwingUtilities.invokeLater(() -> this.setStatus(msg, new Color(0, 220, 255))));
                           System.out.println(">> loadAllMaps DONE");
                           SwingUtilities.invokeLater(() -> {
                              try {
                                 System.out.println(">> MainFrame START");
                                 MainFrame frame = new MainFrame();
                                 frame.setVisible(true);
                                 this.dispose();
                                 System.out.println(">> MainFrame SHOWN");
                              } catch (Throwable var2xx) {
                                 var2xx.printStackTrace();
                                 this.logError(var2xx);
                                 this.btnLogin.setEnabled(true);
                                 this.setStatus("LỖI KHỞI CHẠY GIAO DIỆN CHÍNH: " + var2xx.getClass().getSimpleName(), Theme.ERROR);
                              }
                           });
                        } catch (Throwable var2x) {
                           var2x.printStackTrace();
                           this.logError(var2x);
                           SwingUtilities.invokeLater(() -> {
                              this.btnLogin.setEnabled(true);
                              this.setStatus("LỖI LOAD TÀI NGUYÊN: " + var2x.getClass().getSimpleName(), Theme.ERROR);
                           });
                        }
                     }, "login-loader").start();
                  }
               });
            } catch (Throwable var3) {
               var3.printStackTrace();
               this.logError(var3);
               SwingUtilities.invokeLater(() -> {
                  this.btnLogin.setEnabled(true);
                  this.setStatus("LỖI KẾT NỐI XÁC THỰC: " + var3.getClass().getSimpleName(), Theme.ERROR);
               });
            }
         }, "auth-thread").start();
      }
   }

   private void shakeFrame() {
      Point loc = this.getLocation();
      Timer shake = new Timer(35, null);
      int[] count = new int[]{0};
      shake.addActionListener(e -> {
         int offset = count[0] % 2 == 0 ? 6 : -6;
         this.setLocation(loc.x + offset, loc.y);
         count[0]++;
         if (count[0] > 7) {
            this.setLocation(loc);
            shake.stop();
         }
      });
      shake.start();
   }

   @Override
   public void dispose() {
      if (this.animTimer != null && this.animTimer.isRunning()) {
         this.animTimer.stop();
      }

      if (this.toastTimer != null && this.toastTimer.isRunning()) {
         this.toastTimer.stop();
      }

      super.dispose();
   }

   private void logError(Throwable t) {
      try (PrintWriter pw = new PrintWriter(new FileWriter("error_login.log", true))) {
         pw.println("--- ERROR OCCURRED AT " + new Date() + " ---");
         t.printStackTrace(pw);
         pw.println();
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
   }

   private static class Node {
      double x;
      double y;
      double vx;
      double vy;
      double radius;
      Color color;

      Node(int maxX, int maxY) {
         Random r = new Random();
         this.x = r.nextInt(maxX);
         this.y = r.nextInt(maxY);
         this.vx = (r.nextDouble() - 0.5) * 1.0;
         this.vy = (r.nextDouble() - 0.5) * 1.0;
         this.radius = 3.0 + r.nextDouble() * 3.0;
         this.color = r.nextBoolean() ? new Color(0, 240, 255, 140) : new Color(157, 0, 255, 120);
      }
   }
}
