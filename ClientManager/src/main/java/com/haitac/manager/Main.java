package com.haitac.manager;

import com.haitac.manager.gui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                }

                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
