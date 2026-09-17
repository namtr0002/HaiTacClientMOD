package com.deplor.haitactihontool.config;

import java.awt.Color;
import java.awt.Font;

public class Theme {
   public static final Color BG_DARKER = new Color(10, 10, 15);
   public static final Color BG_DARK = new Color(16, 16, 22);
   public static final Color BG_CARD = new Color(24, 24, 32);
   public static final Color BG_HOVER = new Color(32, 32, 45);
   public static final Color BORDER = new Color(40, 40, 55);
   public static final Color BORDER_LIGHT = new Color(55, 55, 75);
   public static final Color ACCENT = new Color(0, 140, 255);
   public static final Color ACCENT_SOFT = new Color(0, 140, 255, 40);
   public static final Color ACCENT_GLOW = new Color(0, 140, 255, 40);
   public static final Color ACCENT_DARK = new Color(0, 100, 220);
   public static final Color ACCENT_HOVER = new Color(20, 160, 255);
   public static final Color PURPLE = new Color(160, 80, 255);
   public static final Color TEXT_MAIN = new Color(240, 240, 250);
   public static final Color TEXT_DIM = new Color(160, 160, 180);
   public static final Color TEXT_MUTED = new Color(100, 100, 120);
   public static final Color SUCCESS = new Color(0, 230, 140);
   public static final Color ERROR = new Color(255, 60, 80);
   public static final Color WARNING = new Color(255, 180, 40);
   public static final Color GLASS_LIGHT = new Color(255, 255, 255, 10);
   public static final Color GLASS_DARK = new Color(0, 0, 0, 40);
   public static final Font F_TITLE_LG = new Font("Segoe UI", 1, 32);
   public static final Font F_TITLE = new Font("Segoe UI", 1, 18);
   public static final Font F_MAIN = new Font("Segoe UI", 0, 14);
   public static final Font F_BOLD = new Font("Segoe UI", 1, 14);
   public static final Font F_SMALL = new Font("Segoe UI", 0, 12);
   public static final Font F_TINY = new Font("Segoe UI", 0, 10);
   public static final Font F_MONO = new Font("Consolas", 0, 13);

   public static Font getFontForText(String text, Font baseFont) {
      if (text != null && baseFont != null && baseFont.canDisplayUpTo(text) != -1) {
         Font f = new Font("Segoe UI Symbol", baseFont.getStyle(), baseFont.getSize());
         return f.canDisplayUpTo(text) == -1 ? f : new Font("Dialog", baseFont.getStyle(), baseFont.getSize());
      } else {
         return baseFont;
      }
   }
}
