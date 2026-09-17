package com.deplor.haitactihontool.tool;

import com.deplor.haitactihontool.config.Theme;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainUI extends JPanel {
   private JTabbedPane masterTabs;

   public MainUI() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      this.buildUI();
   }

   private void buildUI() {
      this.masterTabs = new JTabbedPane();
      this.masterTabs.setFont(Theme.F_BOLD);
      this.masterTabs.setBackground(Theme.BG_DARK);
      this.masterTabs.setForeground(Theme.TEXT_MAIN);
      ImageEditorPanel editorPanel = new ImageEditorPanel();
      this.masterTabs.addTab(" \ud83d\udd8c️ Studio Đồ Họa & Vẽ Icon ", editorPanel);
      IconResizerPanel resizerPanel = new IconResizerPanel();
      this.masterTabs.addTab(" ⚡ Icon Resizer (x1..x4) ", resizerPanel);
      DataConverterPanel dataPanel = new DataConverterPanel();
      this.masterTabs.addTab(" \ud83e\uddec Binary & SQL/JSON Data ", dataPanel);
      CharsetCryptoPanel cryptoPanel = new CharsetCryptoPanel();
      this.masterTabs.addTab(" \ud83d\udd20 Bảng Mã & Crypto ", cryptoPanel);
      this.add(this.masterTabs, "Center");
   }
}
