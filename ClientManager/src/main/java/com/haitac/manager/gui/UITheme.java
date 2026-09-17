package com.haitac.manager.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Quản lý màu sắc, font chữ và phong cách giao diện Dark Flat hiện đại
 */
public class UITheme {

    public static final Color BG_DARK = new Color(0x18, 0x18, 0x1e);
    public static final Color BG_PANEL = new Color(0x22, 0x22, 0x2b);
    public static final Color BG_CARD = new Color(0x2b, 0x2b, 0x36);
    public static final Color BG_INPUT = new Color(0x1a, 0x1a, 0x22);

    public static final Color BORDER_COLOR = new Color(0x3e, 0x3e, 0x4f);
    public static final Color TEXT_PRIMARY = new Color(0xf5, 0xf5, 0xf7);
    public static final Color TEXT_SECONDARY = new Color(0xa0, 0xa0, 0xb2);
    public static final Color TEXT_MUTED = new Color(0x70, 0x70, 0x82);

    public static final Color PRIMARY_BLUE = new Color(0x00, 0x84, 0xff);
    public static final Color PRIMARY_BLUE_HOVER = new Color(0x00, 0x6e, 0xd6);
    public static final Color SUCCESS_GREEN = new Color(0x22, 0xc5, 0x5e);
    public static final Color SUCCESS_GREEN_HOVER = new Color(0x16, 0xa3, 0x4a);
    public static final Color WARNING_ORANGE = new Color(0xf5, 0x9e, 0x0b);
    public static final Color DANGER_RED = new Color(0xef, 0x44, 0x44);
    public static final Color DANGER_RED_HOVER = new Color(0xdc, 0x26, 0x26);
    public static final Color PURPLE_ACCENT = new Color(0x8b, 0x5c, 0xf6);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);

    public static void applyGlobalTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("Panel.background", BG_PANEL);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Label.font", FONT_REGULAR);
        UIManager.put("Button.font", FONT_BOLD);
        UIManager.put("TextField.font", FONT_REGULAR);
        UIManager.put("TextArea.font", FONT_MONO);
        UIManager.put("ComboBox.font", FONT_REGULAR);
        UIManager.put("Table.font", FONT_REGULAR);
        UIManager.put("TableHeader.font", FONT_BOLD);
    }

    public static JButton createButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(7, 14, 7, 14));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    public static JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setBackground(BG_INPUT);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(TEXT_PRIMARY);
        tf.setFont(FONT_REGULAR);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    public static JTextArea createTextArea(int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setBackground(BG_INPUT);
        ta.setForeground(TEXT_PRIMARY);
        ta.setCaretColor(TEXT_PRIMARY);
        ta.setFont(FONT_MONO);
        ta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 8, 6, 8)
        ));
        return ta;
    }

    public static JLabel createBadge(String text, Color bg, Color fg) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER);
        badge.setFont(FONT_SMALL);
        badge.setOpaque(true);
        badge.setBackground(bg);
        badge.setForeground(fg);
        badge.setBorder(new EmptyBorder(2, 6, 2, 6));
        return badge;
    }
}
