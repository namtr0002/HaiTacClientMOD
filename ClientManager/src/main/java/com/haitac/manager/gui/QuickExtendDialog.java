package com.haitac.manager.gui;

import com.haitac.manager.model.KeyItem;
import com.haitac.manager.util.DateUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Hộp thoại Gia hạn thời gian nhanh cho các key được chọn
 */
public class QuickExtendDialog extends JDialog {

    private final List<KeyItem> selectedItems;
    private boolean applied = false;
    private JTextField txtCustomDays;

    public QuickExtendDialog(Window parent, List<KeyItem> items) {
        super(parent, "Gia Hạn Nhanh Key Client", ModalityType.APPLICATION_MODAL);
        this.selectedItems = items;

        initComponents();
        pack();
        setMinimumSize(new Dimension(460, 340));
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout(12, 12));
        contentPane.setBackground(UITheme.BG_DARK);
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("GIA HẠN THỜI GIAN (" + selectedItems.size() + " key được chọn)");
        lblTitle.setFont(UITheme.FONT_HEADER);
        lblTitle.setForeground(UITheme.PRIMARY_BLUE);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        centerPanel.setBackground(UITheme.BG_PANEL);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        addExtendBtn(centerPanel, "+1 Ngày", 1);
        addExtendBtn(centerPanel, "+3 Ngày", 3);
        addExtendBtn(centerPanel, "+7 Ngày (1 Tuần)", 7);
        addExtendBtn(centerPanel, "+15 Ngày", 15);
        addExtendBtn(centerPanel, "+30 Ngày (1 Tháng)", 30);
        addExtendBtn(centerPanel, "+90 Ngày (3 Tháng)", 90);
        addExtendBtn(centerPanel, "+180 Ngày (6 Tháng)", 180);
        addExtendBtn(centerPanel, "+365 Ngày (1 Năm)", 365);

        JButton btnLifetime = UITheme.createButton("♾️ Vĩnh Viễn", UITheme.PURPLE_ACCENT, new Color(0x7c, 0x3a, 0xed));
        btnLifetime.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyLifetime();
            }
        });
        centerPanel.add(btnLifetime);

        contentPane.add(centerPanel, BorderLayout.CENTER);

        // Custom days & Close
        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setOpaque(false);

        JPanel customPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        customPanel.setOpaque(false);
        customPanel.add(new JLabel("Số ngày tùy chọn:"));
        txtCustomDays = UITheme.createTextField(6);
        txtCustomDays.setText("30");
        customPanel.add(txtCustomDays);
        JButton btnApplyCustom = UITheme.createButton("Áp dụng", UITheme.PRIMARY_BLUE, UITheme.PRIMARY_BLUE_HOVER);
        btnApplyCustom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int d = Integer.parseInt(txtCustomDays.getText().trim());
                    if (d > 0) applyDays(d);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(QuickExtendDialog.this, "Vui lòng nhập số nguyên hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        customPanel.add(btnApplyCustom);
        bottomPanel.add(customPanel, BorderLayout.CENTER);

        JButton btnClose = UITheme.createButton("Đóng", UITheme.BG_CARD, UITheme.BORDER_COLOR);
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bottomPanel.add(btnClose, BorderLayout.EAST);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addExtendBtn(JPanel parent, String title, final int days) {
        JButton btn = UITheme.createButton(title, UITheme.BG_CARD, UITheme.BORDER_COLOR);
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyDays(days);
            }
        });
        parent.add(btn);
    }

    private void applyDays(int days) {
        long now = System.currentTimeMillis();
        for (KeyItem item : selectedItems) {
            long base = (item.isLifetime() || item.isExpired()) ? now : item.getExpireAt();
            item.setExpireAt(DateUtils.addDays(base, days));
            if (KeyItem.STATUS_LOCKED.equalsIgnoreCase(item.getStatus())) {
                item.setStatus(KeyItem.STATUS_ACTIVE);
            }
        }
        this.applied = true;
        JOptionPane.showMessageDialog(this, "Đã gia hạn +" + days + " ngày cho " + selectedItems.size() + " key thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void applyLifetime() {
        for (KeyItem item : selectedItems) {
            item.setExpireAt(-1);
            if (KeyItem.STATUS_LOCKED.equalsIgnoreCase(item.getStatus())) {
                item.setStatus(KeyItem.STATUS_ACTIVE);
            }
        }
        this.applied = true;
        JOptionPane.showMessageDialog(this, "Đã chuyển " + selectedItems.size() + " key sang trạng thái VĨNH VIỄN!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public boolean isApplied() {
        return applied;
    }
}
