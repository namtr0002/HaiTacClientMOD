package com.haitac.manager.gui;

import com.haitac.manager.model.KeyItem;
import com.haitac.manager.model.LicenseRoot;
import com.haitac.manager.model.ManagerConfig;
import com.haitac.manager.service.CryptoService;
import com.haitac.manager.service.GitHubService;
import com.haitac.manager.service.JsonHelper;
import com.haitac.manager.util.DateUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class MainFrame extends JFrame {
    private final ManagerConfig config;
    private LicenseRoot currentLicense;

    private KeyTablePanel keyTablePanel;
    private LineConfigPanel lineConfigPanel;
    private GitHubSettingsPanel gitHubSettingsPanel;

    private final JLabel lblStatus;
    private final JLabel lblSummary;

    public MainFrame() {
        super("HaiTac Client Manager - Quản Lý Bản Quyền Mod Game");
        this.config = ManagerConfig.load();
        this.currentLicense = new LicenseRoot();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_DARK);

        // Top Toolbar
        JPanel topBar = createTopBar();
        getContentPane().add(topBar, BorderLayout.NORTH);

        // Center Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(UITheme.BG_PANEL);
        tabbedPane.setForeground(UITheme.TEXT_PRIMARY);

        keyTablePanel = new KeyTablePanel(currentLicense, new Runnable() {
            @Override
            public void run() {
                updateSummary();
            }
        });
        lineConfigPanel = new LineConfigPanel();
        gitHubSettingsPanel = new GitHubSettingsPanel(config);

        tabbedPane.addTab("  Danh Sách Key Khách Hàng  ", keyTablePanel);
        tabbedPane.addTab("  Cấu Hình Dòng Mod & Master Switch  ", lineConfigPanel);
        tabbedPane.addTab("  Cài Đặt GitHub & Bảo Mật  ", gitHubSettingsPanel);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Bottom Status Bar
        JPanel statusBar = new JPanel(new BorderLayout(10, 0));
        statusBar.setBackground(UITheme.BG_PANEL);
        statusBar.setBorder(new EmptyBorder(6, 15, 6, 15));

        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setForeground(UITheme.TEXT_MUTED);
        statusBar.add(lblStatus, BorderLayout.WEST);

        lblSummary = new JLabel("Tổng: 0 key");
        lblSummary.setForeground(UITheme.PRIMARY_BLUE);
        lblSummary.setFont(UITheme.FONT_BOLD);
        statusBar.add(lblSummary, BorderLayout.EAST);

        getContentPane().add(statusBar, BorderLayout.SOUTH);

        // Tự động tải từ file local nếu có hoặc tải từ GitHub
        loadInitialData();
    }

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout(10, 10));
        bar.setBackground(UITheme.BG_PANEL);
        bar.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Title
        JLabel lblTitle = new JLabel("HAITAC MOD CLIENT MANAGER");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.TEXT_PRIMARY);
        bar.add(lblTitle, BorderLayout.WEST);

        // Actions
        JPanel actPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actPanel.setOpaque(false);

        JButton btnPull = UITheme.createButton("Tải Từ GitHub (Pull)", UITheme.BG_CARD, UITheme.BORDER_COLOR);
        btnPull.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pullFromGitHub();
            }
        });
        actPanel.add(btnPull);

        JButton btnPush = UITheme.createButton("Lưu & Đẩy Lên GitHub (Push)", UITheme.SUCCESS_GREEN, UITheme.SUCCESS_GREEN_HOVER);
        btnPush.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pushToGitHub();
            }
        });
        actPanel.add(btnPush);

        JButton btnExport = UITheme.createButton("Xuất license.enc", UITheme.BG_CARD, UITheme.BORDER_COLOR);
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportEncryptedFile();
            }
        });
        actPanel.add(btnExport);

        JButton btnImport = UITheme.createButton("Nạp Tệp Cục Bộ", UITheme.BG_CARD, UITheme.BORDER_COLOR);
        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importLocalFile();
            }
        });
        actPanel.add(btnImport);

        bar.add(actPanel, BorderLayout.EAST);
        return bar;
    }

    @SuppressWarnings("unchecked")
    private void loadInitialData() {
        // Kiểm tra file local license_github_decrypted.json
        File localJson = new File("license_github_decrypted.json");
        if (!localJson.exists()) {
            localJson = new File("../license_github_decrypted.json");
        }

        if (localJson.exists()) {
            try {
                FileInputStream fis = new FileInputStream(localJson);
                byte[] data = new byte[(int) localJson.length()];
                fis.read(data);
                fis.close();

                String jsonStr = new String(data, StandardCharsets.UTF_8);
                Map<String, Object> map = (Map<String, Object>) JsonHelper.parse(jsonStr);
                LicenseRoot root = LicenseRoot.fromMap(map);
                setLicenseData(root);
                lblStatus.setText("Đã nạp dữ liệu từ " + localJson.getName());
                return;
            } catch (Exception ex) {
                System.err.println("Không thể đọc local json: " + ex.getMessage());
            }
        }

        // Nếu có token GitHub thì thử pull
        if (config.getGithubToken() != null && !config.getGithubToken().trim().isEmpty()) {
            pullFromGitHub();
        } else {
            setLicenseData(new LicenseRoot());
        }
    }

    private void setLicenseData(LicenseRoot root) {
        this.currentLicense = root;
        keyTablePanel.setLicenseRoot(root);
        lineConfigPanel.setLicenseData(root);
        updateSummary();
    }

    private void updateSummary() {
        if (currentLicense == null || currentLicense.getKeys() == null) {
            lblSummary.setText("Tổng: 0 key");
            return;
        }

        int total = currentLicense.getKeys().size();
        int active = 0;
        int expired = 0;
        int locked = 0;

        for (KeyItem item : currentLicense.getKeys()) {
            String status = item.getEffectiveStatus();
            if (KeyItem.STATUS_LOCKED.equalsIgnoreCase(status)) {
                locked++;
            } else if (KeyItem.STATUS_EXPIRED.equalsIgnoreCase(status)) {
                expired++;
            } else {
                active++;
            }
        }

        lblSummary.setText(String.format("Tổng: %d | Hoạt động: %d | Hết hạn: %d | Khóa: %d", total, active, expired, locked));
    }

    private void syncDataFromUI() {
        if (currentLicense == null) {
            currentLicense = new LicenseRoot();
        }
        lineConfigPanel.applyToLicense(currentLicense);
        currentLicense.setUpdatedAt(System.currentTimeMillis());
    }

    @SuppressWarnings("unchecked")
    private void pullFromGitHub() {
        lblStatus.setText("Đang tải dữ liệu từ GitHub...");
        lblStatus.setForeground(UITheme.PRIMARY_BLUE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GitHubService service = new GitHubService(config);
                    String encBase64 = service.pullLicenseEncrypted();

                    String decryptedJson = CryptoService.decrypt(encBase64, config.getEncryptionKey());
                    Map<String, Object> map = (Map<String, Object>) JsonHelper.parse(decryptedJson);
                    final LicenseRoot root = LicenseRoot.fromMap(map);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setLicenseData(root);
                            lblStatus.setForeground(UITheme.SUCCESS_GREEN);
                            lblStatus.setText("Tải và giải mã thành công từ GitHub! Cập nhật: " + DateUtils.format(root.getUpdatedAt()));
                        }
                    });
                } catch (Exception ex) {
                    final String msg = ex.getMessage();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            lblStatus.setForeground(UITheme.DANGER_RED);
                            lblStatus.setText("Lỗi khi tải từ GitHub: " + msg);
                            JOptionPane.showMessageDialog(MainFrame.this,
                                    "Không thể tải từ GitHub: " + msg, "Lỗi Tải", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
            }
        }).start();
    }

    private void pushToGitHub() {
        if (config.getGithubToken() == null || config.getGithubToken().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng cấu hình GitHub Token trong tab 'Cài Đặt GitHub & Bảo Mật' trước!",
                    "Thiếu Token", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn mã hóa và đẩy toàn bộ danh sách key lên GitHub?",
                "Xác nhận Đẩy lên GitHub", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        syncDataFromUI();

        lblStatus.setText("Đang mã hóa và đẩy lên GitHub...");
        lblStatus.setForeground(UITheme.PRIMARY_BLUE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = JsonHelper.toJson(currentLicense.toMap());
                    String encryptedBase64 = CryptoService.encrypt(json, config.getEncryptionKey());

                    GitHubService service = new GitHubService(config);
                    String commitMsg = "Update license and client keys [" + DateUtils.format(System.currentTimeMillis()) + "]";
                    String newSha = service.pushLicenseEncrypted(encryptedBase64, commitMsg);

                    // Lưu kèm bản backup json cục bộ
                    try {
                        FileOutputStream fos = new FileOutputStream("license_github_decrypted.json");
                        fos.write(json.getBytes(StandardCharsets.UTF_8));
                        fos.close();
                    } catch (Exception ignored) {}

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            lblStatus.setForeground(UITheme.SUCCESS_GREEN);
                            lblStatus.setText("Đã cập nhật và đẩy lên GitHub thành công! (SHA: " + (newSha != null ? newSha.substring(0, Math.min(7, newSha.length())) : "ok") + ")");
                            updateSummary();
                            JOptionPane.showMessageDialog(MainFrame.this,
                                    "Đã cập nhật license lên GitHub thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                } catch (Exception ex) {
                    final String msg = ex.getMessage();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            lblStatus.setForeground(UITheme.DANGER_RED);
                            lblStatus.setText("Lỗi khi đẩy lên GitHub: " + msg);
                            JOptionPane.showMessageDialog(MainFrame.this,
                                    "Không thể đẩy lên GitHub: " + msg, "Lỗi Đẩy", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
            }
        }).start();
    }

    private void exportEncryptedFile() {
        syncDataFromUI();
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("license.enc"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String json = JsonHelper.toJson(currentLicense.toMap());
                String encryptedBase64 = CryptoService.encrypt(json, config.getEncryptionKey());
                FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                fos.write(encryptedBase64.getBytes(StandardCharsets.UTF_8));
                fos.close();

                lblStatus.setForeground(UITheme.SUCCESS_GREEN);
                lblStatus.setText("Đã xuất ra tệp: " + fc.getSelectedFile().getName());
                JOptionPane.showMessageDialog(this, "Đã xuất license.enc thành công!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void importLocalFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String content = new String(data, StandardCharsets.UTF_8).trim();

                LicenseRoot root;
                if (content.startsWith("{")) {
                    Map<String, Object> map = (Map<String, Object>) JsonHelper.parse(content);
                    root = LicenseRoot.fromMap(map);
                } else {
                    String decrypted = CryptoService.decrypt(content, config.getEncryptionKey());
                    Map<String, Object> map = (Map<String, Object>) JsonHelper.parse(decrypted);
                    root = LicenseRoot.fromMap(map);
                }

                setLicenseData(root);
                lblStatus.setForeground(UITheme.SUCCESS_GREEN);
                lblStatus.setText("Đã nạp tệp " + file.getName() + " thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc tệp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
