package com.haitac.manager.gui;

import com.haitac.manager.model.KeyItem;
import com.haitac.manager.model.LicenseRoot;
import com.haitac.manager.model.ProductLine;
import com.haitac.manager.util.DateUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Hộp thoại Thêm mới / Chỉnh sửa Key cho từng Client
 */
public class KeyDialog extends JDialog {

    private final LicenseRoot licenseRoot;
    private KeyItem currentItem;
    private boolean saved = false;

    private JComboBox<ProductLine> cbLine;
    private JTextField txtKey;
    private JTextField txtCustomer;
    private JTextField txtNote;
    private JTextField txtAllowedIps;
    private JTextField txtExpireDate;
    private JComboBox<String> cbStatus;
    private JTextArea txtServers;

    public KeyDialog(Window parent, LicenseRoot root, KeyItem itemToEdit) {
        super(parent, itemToEdit == null ? "Tạo Key Riêng Cho Client Mới" : "Chỉnh Sửa Key Client", ModalityType.APPLICATION_MODAL);
        this.licenseRoot = root;
        this.currentItem = itemToEdit;

        initComponents();
        if (currentItem != null) {
            loadItemData(currentItem);
        } else {
            generateRandomKey();
            txtExpireDate.setText(DateUtils.format(DateUtils.addDays(System.currentTimeMillis(), 30)));
        }

        pack();
        setMinimumSize(new Dimension(620, 580));
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBackground(UITheme.BG_DARK);
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel(currentItem == null ? "TẠO KEY RIÊNG CHO CLIENT MỚI" : "CHỈNH SỬA KEY CLIENT");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.PRIMARY_BLUE);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.BG_PANEL);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // Dòng MOD
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Dòng MOD:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        cbLine = new JComboBox<ProductLine>();
        for (ProductLine pl : licenseRoot.getLines()) {
            cbLine.addItem(pl);
        }
        cbLine.setBackground(UITheme.BG_INPUT);
        cbLine.setForeground(UITheme.TEXT_PRIMARY);
        formPanel.add(cbLine, gbc);
        row++;

        // Mã Key / Client ID
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Mã Key / ID Client:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        JPanel keyPanel = new JPanel(new BorderLayout(6, 0));
        keyPanel.setOpaque(false);
        txtKey = UITheme.createTextField(20);
        keyPanel.add(txtKey, BorderLayout.CENTER);
        JButton btnGen = UITheme.createButton("🎲 Sinh mã", UITheme.PURPLE_ACCENT, new Color(0x7c, 0x3a, 0xed));
        btnGen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateRandomKey();
            }
        });
        keyPanel.add(btnGen, BorderLayout.EAST);
        formPanel.add(keyPanel, gbc);
        row++;

        // Tên Khách Hàng
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Tên khách hàng:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        txtCustomer = UITheme.createTextField(20);
        formPanel.add(txtCustomer, gbc);
        row++;

        // Ghi chú
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Ghi chú / Gói:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        txtNote = UITheme.createTextField(20);
        formPanel.add(txtNote, gbc);
        row++;

        // Trạng thái
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        cbStatus = new JComboBox<String>(new String[]{"ACTIVE (Cho phép)", "LOCKED (Tạm khóa)"});
        cbStatus.setBackground(UITheme.BG_INPUT);
        cbStatus.setForeground(UITheme.TEXT_PRIMARY);
        formPanel.add(cbStatus, gbc);
        row++;

        // Hạn sử dụng
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Hạn dùng (yyyy-MM-dd HH:mm):"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        JPanel expPanel = new JPanel(new BorderLayout(5, 5));
        expPanel.setOpaque(false);
        txtExpireDate = UITheme.createTextField(16);
        expPanel.add(txtExpireDate, BorderLayout.NORTH);

        JPanel quickBtnP = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        quickBtnP.setOpaque(false);
        addQuickBtn(quickBtnP, "+1N", 1);
        addQuickBtn(quickBtnP, "+7N", 7);
        addQuickBtn(quickBtnP, "+30N", 30);
        addQuickBtn(quickBtnP, "+1Năm", 365);
        JButton btnLife = UITheme.createButton("Vĩnh viễn", UITheme.SUCCESS_GREEN, UITheme.SUCCESS_GREEN_HOVER);
        btnLife.setFont(UITheme.FONT_SMALL);
        btnLife.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtExpireDate.setText("Vĩnh viễn");
            }
        });
        quickBtnP.add(btnLife);
        expPanel.add(quickBtnP, BorderLayout.SOUTH);
        formPanel.add(expPanel, gbc);
        row++;

        // Danh sách server riêng
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        formPanel.add(new JLabel("<html>Server riêng:<br/><small>(mỗi dòng 1 server<br/>hoặc để trống dùng chung)</small></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        txtServers = UITheme.createTextArea(4, 20);
        JScrollPane spServers = new JScrollPane(txtServers);
        spServers.setPreferredSize(new Dimension(300, 75));
        formPanel.add(spServers, gbc);
        row++;

        // IP cho phép
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        formPanel.add(new JLabel("IP cho phép (* = tất cả):"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        txtAllowedIps = UITheme.createTextField(20);
        txtAllowedIps.setText("*");
        formPanel.add(txtAllowedIps, gbc);

        contentPane.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);

        JButton btnCancel = UITheme.createButton("Hủy bỏ", UITheme.BG_CARD, UITheme.BORDER_COLOR);
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bottomPanel.add(btnCancel);

        JButton btnSave = UITheme.createButton("💾 Lưu Key", UITheme.SUCCESS_GREEN, UITheme.SUCCESS_GREEN_HOVER);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        bottomPanel.add(btnSave);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addQuickBtn(JPanel p, String title, final int days) {
        JButton btn = UITheme.createButton(title, UITheme.BG_CARD, UITheme.BORDER_COLOR);
        btn.setFont(UITheme.FONT_SMALL);
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtExpireDate.setText(DateUtils.format(DateUtils.addDays(System.currentTimeMillis(), days)));
            }
        });
        p.add(btn);
    }

    private void generateRandomKey() {
        ProductLine pl = (ProductLine) cbLine.getSelectedItem();
        String prefix = "HTTH";
        if (pl != null) {
            if ("MOD_J2ME".equalsIgnoreCase(pl.getLineId())) prefix = "HTTH-J2ME";
            else if ("MOD_UNITY".equalsIgnoreCase(pl.getLineId())) prefix = "HTTH-UNI";
            else if ("MOD_LIBGDX".equalsIgnoreCase(pl.getLineId())) prefix = "HTTH-LIB";
        }
        txtKey.setText(DateUtils.generateKey(prefix));
    }

    private void loadItemData(KeyItem item) {
        txtKey.setText(item.getKey());
        txtCustomer.setText(item.getCustomer());
        txtNote.setText(item.getNote());
        txtAllowedIps.setText(item.getAllowedIpsStr());
        txtExpireDate.setText(item.getExpireDateStr());
        txtServers.setText(item.getServersStr());

        if (KeyItem.STATUS_LOCKED.equalsIgnoreCase(item.getStatus())) {
            cbStatus.setSelectedIndex(1);
        } else {
            cbStatus.setSelectedIndex(0);
        }

        for (int i = 0; i < cbLine.getItemCount(); i++) {
            ProductLine pl = cbLine.getItemAt(i);
            if (pl.getLineId().equalsIgnoreCase(item.getLineId())) {
                cbLine.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveData() {
        String key = txtKey.getText().trim();
        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã key không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProductLine pl = (ProductLine) cbLine.getSelectedItem();
        String lineId = pl != null ? pl.getLineId() : "MOD_UNITY";

        if (currentItem == null) {
            KeyItem existing = licenseRoot.findKey(key);
            if (existing != null) {
                JOptionPane.showMessageDialog(this, "Mã key này đã tồn tại trong danh sách!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentItem = new KeyItem();
            currentItem.setCreatedAt(System.currentTimeMillis());
            licenseRoot.getKeys().add(currentItem);
        }

        currentItem.setKey(key);
        currentItem.setLineId(lineId);
        currentItem.setCustomer(txtCustomer.getText().trim());
        currentItem.setNote(txtNote.getText().trim());
        currentItem.setStatus(cbStatus.getSelectedIndex() == 1 ? KeyItem.STATUS_LOCKED : KeyItem.STATUS_ACTIVE);
        currentItem.setAllowedIpsFromStr(txtAllowedIps.getText().trim());
        currentItem.setExpireAt(DateUtils.parse(txtExpireDate.getText().trim()));
        currentItem.setServersFromStr(txtServers.getText().trim());

        this.saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public KeyItem getCurrentItem() {
        return currentItem;
    }
}
