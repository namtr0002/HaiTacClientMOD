package com.deplor.haitactihontool.ui;

import com.deplor.haitactihontool.ultimate_security.SessionInfo;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BoostrapLoader {
   public static void main(String[] args) {
      setupTheme();
      SwingUtilities.invokeLater(() -> {
         AuthUI auth = new AuthUI(key -> showMainApplication());
         auth.setVisible(true);
      });
   }

   private static void setupTheme() {
      FlatDarkLaf.setup();
      Color bgDarker = new Color(18, 18, 24);
      Color bgDark = new Color(24, 24, 30);
      Color border = new Color(36, 36, 44);
      Color accent = new Color(120, 80, 220);
      Color textMain = new Color(200, 200, 210);
      Color textDim = new Color(150, 150, 160);
      Color selBg = new Color(120, 80, 220, 40);
      UIManager.put("Component.focusWidth", 0);
      UIManager.put("Component.innerFocusWidth", 0);
      UIManager.put("Component.arc", 8);
      UIManager.put("Button.arc", 8);
      UIManager.put("TextComponent.arc", 8);
      UIManager.put("CheckBox.arc", 4);
      UIManager.put("Panel.background", bgDarker);
      UIManager.put("Panel.foreground", textMain);
      UIManager.put("Viewport.background", bgDarker);
      UIManager.put("TabbedPane.background", bgDark);
      UIManager.put("TabbedPane.foreground", textDim);
      UIManager.put("TabbedPane.selectedBackground", bgDarker);
      UIManager.put("TabbedPane.selectedForeground", Color.WHITE);
      UIManager.put("TabbedPane.underlineColor", accent);
      UIManager.put("TabbedPane.showTabSeparators", false);
      UIManager.put("TabbedPane.tabHeight", 34);
      UIManager.put("List.background", bgDark);
      UIManager.put("List.foreground", textMain);
      UIManager.put("List.selectionBackground", selBg);
      UIManager.put("List.selectionForeground", Color.WHITE);
      UIManager.put("List.cellMargins", new Insets(6, 12, 6, 12));
      UIManager.put("Table.background", bgDark);
      UIManager.put("Table.foreground", textMain);
      UIManager.put("Table.gridColor", border);
      UIManager.put("Table.selectionBackground", selBg);
      UIManager.put("Table.selectionForeground", Color.WHITE);
      UIManager.put("TableHeader.background", bgDarker);
      UIManager.put("TableHeader.foreground", Color.WHITE);
      UIManager.put("TableHeader.separatorColor", border);
      UIManager.put("SplitPane.background", bgDarker);
      UIManager.put("SplitPane.dividerSize", 6);
      UIManager.put("SplitPaneDivider.border", BorderFactory.createEmptyBorder());
      UIManager.put("ComboBox.background", bgDark);
      UIManager.put("ComboBox.foreground", textMain);
      UIManager.put("ComboBox.buttonBackground", bgDark);
      UIManager.put("ComboBox.buttonEditableBackground", bgDark);
      UIManager.put("ComboBox.selectionBackground", selBg);
      UIManager.put("ComboBox.selectionForeground", Color.WHITE);
      UIManager.put("ComboBox.padding", new Insets(6, 10, 6, 10));
      UIManager.put("ScrollPane.background", bgDarker);
      UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
      UIManager.put("ScrollBar.width", 10);
      UIManager.put("ScrollBar.height", 10);
      UIManager.put("ScrollBar.track", bgDarker);
      UIManager.put("ScrollBar.thumb", new Color(55, 55, 68));
      UIManager.put("ScrollBar.thumbHover", accent);
      UIManager.put("ScrollBar.thumbArc", 999);
      UIManager.put("ScrollBar.trackArc", 999);
      UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
      UIManager.put("ScrollBar.showButtons", false);
      UIManager.put("TextField.background", bgDark);
      UIManager.put("TextField.foreground", Color.WHITE);
      UIManager.put("TextField.caretColor", accent);
      UIManager.put("TextField.focusedBorderColor", accent);
      UIManager.put("TextField.placeholderForeground", new Color(100, 100, 110));
      UIManager.put("PasswordField.background", bgDark);
      UIManager.put("PasswordField.foreground", Color.WHITE);
      UIManager.put("PasswordField.caretColor", accent);
      UIManager.put("PasswordField.focusedBorderColor", accent);
      UIManager.put("Tree.background", bgDark);
      UIManager.put("Tree.foreground", textMain);
      UIManager.put("Tree.selectionBackground", selBg);
      UIManager.put("Tree.selectionForeground", Color.WHITE);
      UIManager.put("OptionPane.background", bgDarker);
      UIManager.put("OptionPane.messageForeground", Color.WHITE);
   }

   private static void showMainApplication() {
      JWindow loader = new JWindow();
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(new Color(30, 30, 35));
      panel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));
      JLabel msg = new JLabel("Initializing Core Components...", 0);
      msg.setForeground(new Color(200, 200, 210));
      msg.setFont(new Font("SansSerif", 0, 12));
      msg.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
      panel.add(msg, "Center");
      JProgressBar pb = new JProgressBar();
      pb.setIndeterminate(true);
      pb.setPreferredSize(new Dimension(0, 4));
      pb.setBackground(new Color(30, 30, 35));
      pb.setForeground(new Color(0, 150, 255));
      pb.setBorder(null);
      panel.add(pb, "South");
      loader.add(panel);
      loader.pack();
      loader.setLocationRelativeTo(null);
      loader.setVisible(true);
      new Thread(() -> {
         try {
            Thread.sleep(1500L);
            SwingUtilities.invokeLater(() -> {
               SessionInfo.activate("admin", "00:00 01/01/2000", "00:00 01/01/2100", "Dev Test Session");
               MainFrame mainFrame = new MainFrame();
               mainFrame.setVisible(true);
               loader.dispose();
            });
         } catch (Exception var2x) {
            var2x.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to initialize tool: " + var2x.getMessage());
            System.exit(1);
         }
      }).start();
   }
}
