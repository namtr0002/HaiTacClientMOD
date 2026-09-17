package com.haitac.manager.gui;

import com.haitac.manager.model.KeyItem;
import com.haitac.manager.model.LicenseRoot;
import com.haitac.manager.model.ProductLine;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Bảng quản lý danh sách Key, tìm kiếm, lọc, tạo mới, sửa, gia hạn, khóa/mở khóa
 */
public class KeyTablePanel extends JPanel {

    private LicenseRoot licenseRoot;
    private final Runnable onDataChanged;

    private JTable table;
    private KeyTableModel tableModel;
    private final List<KeyItem> displayedKeys = new ArrayList<KeyItem>();

    private JTextField txtSearch;
    private JComboBox<String> cbFilterLine;
    private JComboBox<String> cbFilterStatus;
    private JLabel lblSummary;

    public KeyTablePanel(LicenseRoot root, Runnable onDataChanged) {
        this.licenseRoot = root;
        this.onDataChanged = onDataChanged;

        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BG_DARK);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        // TOP: Filter & Search Bar
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));
        topPanel.setBackground(UITheme.BG_PANEL);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchGroup.setOpaque(false);

        searchGroup.add(new JLabel("🔍 Tìm kiếm:"));
        txtSearch = UITheme.createTextField(16);
        txtSearch.setToolTipText("Tìm theo mã key, tên khách, ghi chú...");
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                applyFilter();
            }
        });
        searchGroup.add(txtSearch);

        searchGroup.add(new JLabel("Dòng:"));
        cbFilterLine = new JComboBox<String>();
        cbFilterLine.addItem("Tất cả dòng");
        for (ProductLine pl : licenseRoot.getLines()) {
            cbFilterLine.addItem(pl.getLineId());
        }
        cbFilterLine.setBackground(UITheme.BG_INPUT);
        cbFilterLine.setForeground(UITheme.TEXT_PRIMARY);
        cbFilterLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });
        searchGroup.add(cbFilterLine);

        searchGroup.add(new JLabel("Trạng thái:"));
        cbFilterStatus = new JComboBox<String>(new String[]{"Tất cả", "ACTIVE (Hoạt động)", "LOCKED (Đã khóa)", "EXPIRED (Quá hạn)"});
        cbFilterStatus.setBackground(UITheme.BG_INPUT);
        cbFilterStatus.setForeground(UITheme.TEXT_PRIMARY);
        cbFilterStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });
        searchGroup.add(cbFilterStatus);

        topPanel.add(searchGroup, BorderLayout.WEST);

        lblSummary = new JLabel("Tổng: 0 key");
        lblSummary.setForeground(UITheme.TEXT_SECONDARY);
        lblSummary.setFont(UITheme.FONT_BOLD);
        topPanel.add(lblSummary, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // CENTER: Data Table
        tableModel = new KeyTableModel();
        table = new JTable(tableModel);
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setGridColor(UITheme.BORDER_COLOR);
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(0x38, 0x38, 0x4a));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(UITheme.BG_PANEL);
        table.getTableHeader().setForeground(UITheme.TEXT_SECONDARY);
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setMaxWidth(40); // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(210); // Mã Key
        table.getColumnModel().getColumn(2).setPreferredWidth(90);  // Dòng
        table.getColumnModel().getColumn(3).setPreferredWidth(120); // Khách
        table.getColumnModel().getColumn(4).setPreferredWidth(90);  // Trạng thái
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Hạn dùng
        table.getColumnModel().getColumn(6).setPreferredWidth(100); // Còn lại
        table.getColumnModel().getColumn(7).setPreferredWidth(140); // Server riêng / Ghi chú

        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    editSelectedKey();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(UITheme.BG_DARK);
        add(scrollPane, BorderLayout.CENTER);

        // BOTTOM: Action Toolbar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bottomBar.setOpaque(false);

        JButton btnAdd = UITheme.createButton("➕ Tạo Key Mới", UITheme.SUCCESS_GREEN, UITheme.SUCCESS_GREEN_HOVER);
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddDialog();
            }
        });
        bottomBar.add(btnAdd);

        JButton btnEdit = UITheme.createButton("✏️ Chỉnh Sửa", UITheme.PRIMARY_BLUE, UITheme.PRIMARY_BLUE_HOVER);
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedKey();
            }
        });
        bottomBar.add(btnEdit);

        JButton btnExtend = UITheme.createButton("⏳ Gia Hạn Nhanh", UITheme.WARNING_ORANGE, new Color(0xd9, 0x77, 0x06));
        btnExtend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openQuickExtendDialog();
            }
        });
        bottomBar.add(btnExtend);

        JButton btnToggleLock = UITheme.createButton("🔒 Khóa / Mở Khóa", UITheme.BG_CARD, UITheme.BORDER_COLOR);
        btnToggleLock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleLockSelected();
            }
        });
        bottomBar.add(btnToggleLock);

        JButton btnCopy = UITheme.createButton("📋 Copy Key Gửi Khách", UITheme.PURPLE_ACCENT, new Color(0x7c, 0x3a, 0xed));
        btnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copySelectedKeyFormatted();
            }
        });
        bottomBar.add(btnCopy);

        JButton btnDelete = UITheme.createButton("🗑️ Xóa", UITheme.DANGER_RED, UITheme.DANGER_RED_HOVER);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedKeys();
            }
        });
        bottomBar.add(btnDelete);

        add(bottomBar, BorderLayout.SOUTH);
    }

    public void setLicenseRoot(LicenseRoot root) {
        this.licenseRoot = root;
        refreshTable();
    }

    public LicenseRoot getLicenseRoot() {
        return licenseRoot;
    }

    public List<KeyItem> getKeys() {
        return licenseRoot != null ? licenseRoot.getKeys() : new ArrayList<KeyItem>();
    }

    public void refreshTable() {
        if (licenseRoot == null) {
            displayedKeys.clear();
            tableModel.fireTableDataChanged();
            return;
        }
        cbFilterLine.removeAllItems();
        cbFilterLine.addItem("Tất cả dòng");
        for (ProductLine pl : licenseRoot.getLines()) {
            cbFilterLine.addItem(pl.getLineId());
        }
        applyFilter();
    }

    private void applyFilter() {
        displayedKeys.clear();
        String kw = txtSearch.getText().trim().toLowerCase();
        String selectedLine = (String) cbFilterLine.getSelectedItem();
        int statusIdx = cbFilterStatus.getSelectedIndex();

        int total = licenseRoot.getKeys().size();
        int activeCount = 0;

        for (KeyItem item : licenseRoot.getKeys()) {
            String effStatus = item.getEffectiveStatus();
            if (KeyItem.STATUS_ACTIVE.equals(effStatus)) activeCount++;

            // Filter by Line
            if (selectedLine != null && !selectedLine.equals("Tất cả dòng")) {
                if (!selectedLine.equalsIgnoreCase(item.getLineId())) continue;
            }

            // Filter by Status
            if (statusIdx == 1 && !KeyItem.STATUS_ACTIVE.equals(effStatus)) continue;
            if (statusIdx == 2 && !KeyItem.STATUS_LOCKED.equals(effStatus)) continue;
            if (statusIdx == 3 && !KeyItem.STATUS_EXPIRED.equals(effStatus)) continue;

            // Search keyword
            if (!kw.isEmpty()) {
                String matchStr = (item.getKey() + " " + item.getCustomer() + " " + item.getNote() + " " + item.getLineId()).toLowerCase();
                if (!matchStr.contains(kw)) continue;
            }

            displayedKeys.add(item);
        }

        tableModel.fireTableDataChanged();
        lblSummary.setText(String.format("Hiển thị: %d / %d key (Đang hoạt động: %d)", displayedKeys.size(), total, activeCount));
    }

    private void openAddDialog() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        KeyDialog dlg = new KeyDialog(parent, licenseRoot, null);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            refreshTable();
            if (onDataChanged != null) onDataChanged.run();
        }
    }

    private void editSelectedKey() {
        int row = table.getSelectedRow();
        if (row == -1 || row >= displayedKeys.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một key để chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        KeyItem item = displayedKeys.get(row);
        Window parent = SwingUtilities.getWindowAncestor(this);
        KeyDialog dlg = new KeyDialog(parent, licenseRoot, item);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            refreshTable();
            if (onDataChanged != null) onDataChanged.run();
        }
    }

    private void openQuickExtendDialog() {
        int[] rows = table.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một key để gia hạn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<KeyItem> items = new ArrayList<KeyItem>();
        for (int r : rows) {
            if (r < displayedKeys.size()) items.add(displayedKeys.get(r));
        }
        Window parent = SwingUtilities.getWindowAncestor(this);
        QuickExtendDialog dlg = new QuickExtendDialog(parent, items);
        dlg.setVisible(true);
        if (dlg.isApplied()) {
            refreshTable();
            if (onDataChanged != null) onDataChanged.run();
        }
    }

    private void toggleLockSelected() {
        int[] rows = table.getSelectedRows();
        if (rows.length == 0) return;
        for (int r : rows) {
            if (r < displayedKeys.size()) {
                KeyItem item = displayedKeys.get(r);
                if (KeyItem.STATUS_LOCKED.equalsIgnoreCase(item.getStatus())) {
                    item.setStatus(KeyItem.STATUS_ACTIVE);
                } else {
                    item.setStatus(KeyItem.STATUS_LOCKED);
                }
            }
        }
        refreshTable();
        if (onDataChanged != null) onDataChanged.run();
    }

    private void copySelectedKeyFormatted() {
        int row = table.getSelectedRow();
        if (row == -1 || row >= displayedKeys.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một key để copy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        KeyItem item = displayedKeys.get(row);
        String msg = "====================================\n"
                + "🔑 KHÓA BẢN QUYỀN HẢI TẶC CLIENT MOD\n"
                + "====================================\n"
                + "• Mã Key: " + item.getKey() + "\n"
                + "• Dòng MOD: " + item.getLineId() + "\n"
                + "• Khách hàng: " + (item.getCustomer().isEmpty() ? "VIP" : item.getCustomer()) + "\n"
                + "• Hạn sử dụng: " + item.getExpireDateStr() + " (" + item.getRemainingTimeStr() + ")\n"
                + "• Hỗ trợ & Thuê Mod: t.me/@ThanhNamYe\n"
                + "====================================";

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(msg), null);
        JOptionPane.showMessageDialog(this, "Đã copy thông tin key gửi khách vào Clipboard!\n\n" + msg, "Copy Thành Công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelectedKeys() {
        int[] rows = table.getSelectedRows();
        if (rows.length == 0) return;
        int opt = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa " + rows.length + " key đã chọn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            for (int r : rows) {
                if (r < displayedKeys.size()) {
                    licenseRoot.getKeys().remove(displayedKeys.get(r));
                }
            }
            refreshTable();
            if (onDataChanged != null) onDataChanged.run();
        }
    }

    private class KeyTableModel extends AbstractTableModel {
        private final String[] columns = {"STT", "Mã Key / ID Client", "Dòng", "Khách Hàng", "Trạng Thái", "Hạn Dùng", "Còn Lại", "Ghi Chú"};

        public int getRowCount() { return displayedKeys.size(); }
        public int getColumnCount() { return columns.length; }
        public String getColumnName(int c) { return columns[c]; }

        public Object getValueAt(int r, int c) {
            if (r >= displayedKeys.size()) return "";
            KeyItem item = displayedKeys.get(r);
            switch (c) {
                case 0: return r + 1;
                case 1: return item.getKey();
                case 2: return item.getLineId();
                case 3: return item.getCustomer();
                case 4: return item.getEffectiveStatus();
                case 5: return item.getExpireDateStr();
                case 6: return item.getRemainingTimeStr();
                case 7: {
                    String s = item.getNote();
                    if (item.getServers() != null && !item.getServers().isEmpty()) {
                        s += " [" + item.getServers().size() + " sv riêng]";
                    }
                    return s;
                }
                default: return "";
            }
        }
    }

    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            l.setHorizontalAlignment(CENTER);
            String st = String.valueOf(value);
            if (KeyItem.STATUS_ACTIVE.equalsIgnoreCase(st)) {
                l.setForeground(UITheme.SUCCESS_GREEN);
                l.setText("● HOẠT ĐỘNG");
            } else if (KeyItem.STATUS_LOCKED.equalsIgnoreCase(st)) {
                l.setForeground(UITheme.DANGER_RED);
                l.setText("■ ĐÃ KHÓA");
            } else if (KeyItem.STATUS_EXPIRED.equalsIgnoreCase(st)) {
                l.setForeground(UITheme.WARNING_ORANGE);
                l.setText("▲ HẾT HẠN");
            }
            return l;
        }
    }
}
