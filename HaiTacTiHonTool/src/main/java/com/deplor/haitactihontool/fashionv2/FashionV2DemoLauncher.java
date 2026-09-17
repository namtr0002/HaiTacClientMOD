package com.deplor.haitactihontool.fashionv2;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FashionV2DemoLauncher {
   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
         try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
         } catch (Exception var2) {
            var2.printStackTrace();
         }

         JFrame frame = new JFrame("HAITACTIHONTOOL - FASHION SYSTEM V2 EDITOR DEMO");
         frame.setDefaultCloseOperation(3);
         frame.setSize(1400, 850);
         frame.setLocationRelativeTo(null);
         MainUI fashionUI = new MainUI();
         frame.add(fashionUI, "Center");
         frame.setVisible(true);
         System.out.println("=================================================");
         System.out.println("HAITACTIHONTOOL - FASHION V2 DEMO IS NOW RUNNING!");
         System.out.println("=================================================");
      });
   }
}
