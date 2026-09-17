package com.haitac.manager.gui;

import com.haitac.manager.model.LicenseRoot;
import com.haitac.manager.model.ProductLine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LineConfigPanel extends JPanel {
    private final JCheckBox chkMasterEnabled;
    private final JCheckBox chkServerCheckEnabled;
    private final JTextField txtGlobalNotice;

    private final Map<String, JCheckBox> lineEnabledBoxes = new HashMap<String, JCheckBox>();
    private final Map<String, JCheckBox> lineRequireKeyBoxes = new HashMap<String, JCheckBox>();
    private final Map<String, JTextField> lineNoticeFields = new HashMap<String, JTextField>();
    private final Map<String, JTextField> lineFreeIpsFields = new HashMap<String, JTextField>();

    private LicenseRoot currentLicense;

    public LineConfigPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BG_DARK);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UITheme.BG_DARK);

        // Master Switch Section
        JPanel masterPanel = new JPanel(new GridBagLayout());
        masterPanel.setBackground(UITheme.BG_PANEL);
        TitledBorder masterBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR), "Master Switch (Toàn bộ hệ thống)"
        );
        masterBorder.setTitleColor(UITheme.PRIMARY_BLUE);
        masterBorder.setTitleFont(UITheme.FONT_HEADER);
        masterPanel.setBorder(BorderFactory.createCompoundBorder(masterBorder, new EmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        chkMasterEnabled = new JCheckBox("Kích hoạt toàn bộ MOD (Master Enabled - Bỏ tích = Khóa toàn bộ Client)");
        chkMasterEnabled.setBackground(UITheme.BG_PANEL);
        chkMasterEnabled.setForeground(UITheme.TEXT_PRIMARY);
        chkMasterEnabled.setFont(UITheme.FONT_BOLD);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        masterPanel.add(chkMasterEnabled, gbc);

        chkServerCheckEnabled = new JCheckBox("Bật kiểm tra server online từ xa (Server Check Enabled)");
        chkServerCheckEnabled.setBackground(UITheme.BG_PANEL);
        chkServerCheckEnabled.setForeground(UITheme.TEXT_PRIMARY);
        chkServerCheckEnabled.setFont(UITheme.FONT_REGULAR);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        masterPanel.add(chkServerCheckEnabled, gbc);

        JLabel lblGlobalMsg = new JLabel("Thông báo toàn cầu (Global Notice):");
        lblGlobalMsg.setForeground(UITheme.TEXT_SECONDARY);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        masterPanel.add(lblGlobalMsg, gbc);

        txtGlobalNotice = UITheme.createTextField(30);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        masterPanel.add(txtGlobalNotice, gbc);

        contentPanel.add(masterPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Product Lines Section
        JPanel linesPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        linesPanel.setBackground(UITheme.BG_DARK);

        String[] lineKeys = {"MOD_J2ME", "MOD_UNITY", "MOD_LIBGDX"};
        String[] lineTitles = {"J2ME Client (ProjectJ2me129)", "Unity Client (ProjectUnity129)", "LibGDX Client (Mobile/PC)"};

        for (int i = 0; i < lineKeys.length; i++) {
            String key = lineKeys[i];
            String title = lineTitles[i];

            JPanel p = new JPanel(new GridBagLayout());
            p.setBackground(UITheme.BG_CARD);
            TitledBorder tb = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(UITheme.BORDER_COLOR), title + " [" + key + "]"
            );
            tb.setTitleColor(UITheme.TEXT_PRIMARY);
            tb.setTitleFont(UITheme.FONT_HEADER);
            p.setBorder(BorderFactory.createCompoundBorder(tb, new EmptyBorder(8, 12, 8, 12)));

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(4, 6, 4, 6);
            c.fill = GridBagConstraints.HORIZONTAL;

            JCheckBox chkLine = new JCheckBox("Kích hoạt dòng mod này (Enabled)");
            chkLine.setBackground(UITheme.BG_CARD);
            chkLine.setForeground(UITheme.TEXT_PRIMARY);
            c.gridx = 0; c.gridy = 0; c.gridwidth = 1;
            p.add(chkLine, c);
            lineEnabledBoxes.put(key, chkLine);

            JCheckBox chkReqKey = new JCheckBox("Bắt buộc phải có Key hợp lệ (Require Key)");
            chkReqKey.setBackground(UITheme.BG_CARD);
            chkReqKey.setForeground(UITheme.TEXT_PRIMARY);
            c.gridx = 1; c.gridy = 0; c.gridwidth = 1;
            p.add(chkReqKey, c);
            lineRequireKeyBoxes.put(key, chkReqKey);

            JLabel lblFreeIps = new JLabel("IP miễn phí (Free IPs, cách nhau bởi dấu phẩy):");
            lblFreeIps.setForeground(UITheme.TEXT_SECONDARY);
            c.gridx = 0; c.gridy = 1; c.gridwidth = 1;
            p.add(lblFreeIps, c);

            JTextField txtFreeIps = UITheme.createTextField(20);
            c.gridx = 1; c.gridy = 1; c.weightx = 1.0;
            p.add(txtFreeIps, c);
            lineFreeIpsFields.put(key, txtFreeIps);

            JLabel lblNotice = new JLabel("Thông báo riêng cho dòng này:");
            lblNotice.setForeground(UITheme.TEXT_SECONDARY);
            c.gridx = 0; c.gridy = 2; c.weightx = 0;
            p.add(lblNotice, c);

            JTextField txtNotice = UITheme.createTextField(20);
            c.gridx = 1; c.gridy = 2; c.weightx = 1.0;
            p.add(txtNotice, c);
            lineNoticeFields.put(key, txtNotice);

            linesPanel.add(p);
        }

        contentPanel.add(linesPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setLicenseData(LicenseRoot license) {
        this.currentLicense = license;
        if (license == null) return;

        chkMasterEnabled.setSelected(license.isMasterEnabled());
        chkServerCheckEnabled.setSelected(license.isServerCheckEnabled());
        txtGlobalNotice.setText(license.getGlobalNotice() != null ? license.getGlobalNotice() : "");

        for (Map.Entry<String, JCheckBox> entry : lineEnabledBoxes.entrySet()) {
            String key = entry.getKey();
            ProductLine pl = license.findLineById(key);
            if (pl != null) {
                entry.getValue().setSelected(pl.isEnabled());

                JCheckBox reqBox = lineRequireKeyBoxes.get(key);
                if (reqBox != null) reqBox.setSelected(pl.isRequireKey());

                JTextField freeIpsField = lineFreeIpsFields.get(key);
                if (freeIpsField != null) freeIpsField.setText(pl.getFreeIpsStr());

                JTextField noticeField = lineNoticeFields.get(key);
                if (noticeField != null) noticeField.setText(pl.getNotice() != null ? pl.getNotice() : "");
            } else {
                entry.getValue().setSelected(true);
            }
        }
    }

    public void applyToLicense(LicenseRoot license) {
        if (license == null) return;

        license.setMasterEnabled(chkMasterEnabled.isSelected());
        license.setServerCheckEnabled(chkServerCheckEnabled.isSelected());
        license.setGlobalNotice(txtGlobalNotice.getText().trim());

        for (Map.Entry<String, JCheckBox> entry : lineEnabledBoxes.entrySet()) {
            String key = entry.getKey();
            ProductLine pl = license.findLineById(key);
            if (pl == null) {
                pl = new ProductLine(key, key, entry.getValue().isSelected(), true);
                license.getLines().add(pl);
            }
            pl.setEnabled(entry.getValue().isSelected());

            JCheckBox reqBox = lineRequireKeyBoxes.get(key);
            if (reqBox != null) pl.setRequireKey(reqBox.isSelected());

            JTextField freeIpsField = lineFreeIpsFields.get(key);
            if (freeIpsField != null) pl.setFreeIpsFromStr(freeIpsField.getText().trim());

            JTextField noticeField = lineNoticeFields.get(key);
            if (noticeField != null) pl.setNotice(noticeField.getText().trim());
        }
    }
}
