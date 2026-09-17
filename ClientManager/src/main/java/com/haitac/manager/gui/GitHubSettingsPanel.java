package com.haitac.manager.gui;

import com.haitac.manager.model.ManagerConfig;
import com.haitac.manager.service.GitHubService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GitHubSettingsPanel extends JPanel {
    private final JTextField txtToken;
    private final JTextField txtOwner;
    private final JTextField txtRepo;
    private final JTextField txtFilePath;
    private final JTextField txtBranch;
    private final JTextField txtSecretKey;
    private final JLabel lblStatus;

    private ManagerConfig config;

    public GitHubSettingsPanel(ManagerConfig config) {
        this.config = config;
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.BG_DARK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.BG_CARD);
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR), "Cấu hình Đồng bộ GitHub & Bảo mật Mã hóa"
        );
        border.setTitleColor(UITheme.PRIMARY_BLUE);
        border.setTitleFont(UITheme.FONT_HEADER);
        formPanel.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(15, 20, 15, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // GitHub Token
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        JLabel lblTok = new JLabel("GitHub Personal Access Token:");
        lblTok.setForeground(UITheme.TEXT_PRIMARY);
        formPanel.add(lblTok, gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.75;
        txtToken = UITheme.createTextField(30);
        formPanel.add(txtToken, gbc);

        // Repo Owner
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        JLabel lblOwn = new JLabel("Tài khoản sở hữu (Owner):");
        lblOwn.setForeground(UITheme.TEXT_PRIMARY);
        formPanel.add(lblOwn, gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.75;
        txtOwner = UITheme.createTextField(30);
        formPanel.add(txtOwner, gbc);

        // Repo Name
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        JLabel lblRep = new JLabel("Kho lưu trữ (Repo Name):");
        lblRep.setForeground(UITheme.TEXT_PRIMARY);
        formPanel.add(lblRep, gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.75;
        txtRepo = UITheme.createTextField(30);
        formPanel.add(txtRepo, gbc);

        // File Path
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        JLabel lblFile = new JLabel("Đường dẫn file (File Path):");
        lblFile.setForeground(UITheme.TEXT_PRIMARY);
        formPanel.add(lblFile, gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.75;
        txtFilePath = UITheme.createTextField(30);
        formPanel.add(txtFilePath, gbc);

        // Branch
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        JLabel lblBranch = new JLabel("Nhánh (Branch):");
        lblBranch.setForeground(UITheme.TEXT_PRIMARY);
        formPanel.add(lblBranch, gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.75;
        txtBranch = UITheme.createTextField(30);
        formPanel.add(txtBranch, gbc);

        // Secret Key
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        JLabel lblSec = new JLabel("AES Secret Key (Mã hóa license):");
        lblSec.setForeground(UITheme.WARNING_ORANGE);
        lblSec.setFont(UITheme.FONT_BOLD);
        formPanel.add(lblSec, gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 0.75;
        txtSecretKey = UITheme.createTextField(30);
        formPanel.add(txtSecretKey, gbc);

        // Status Label
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UITheme.FONT_REGULAR);
        formPanel.add(lblStatus, gbc);

        // Buttons
        row++;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnTest = UITheme.createButton("Kiểm tra kết nối", UITheme.BG_PANEL, UITheme.BORDER_COLOR);
        btnTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testConnection();
            }
        });
        btnPanel.add(btnTest);

        JButton btnSave = UITheme.createButton("Lưu cấu hình", UITheme.PRIMARY_BLUE, UITheme.PRIMARY_BLUE_HOVER);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveConfig();
            }
        });
        btnPanel.add(btnSave);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        loadFromConfig();
    }

    private void loadFromConfig() {
        if (config == null) return;
        txtToken.setText(config.getGithubToken() != null ? config.getGithubToken() : "");
        txtOwner.setText(config.getGithubOwner() != null ? config.getGithubOwner() : "namtr0002");
        txtRepo.setText(config.getGithubRepo() != null ? config.getGithubRepo() : "HaiTacClientMOD");
        txtFilePath.setText(config.getGithubFilePath() != null ? config.getGithubFilePath() : "data/license.enc");
        txtBranch.setText(config.getGithubBranch() != null ? config.getGithubBranch() : "main");
        txtSecretKey.setText(config.getEncryptionKey() != null ? config.getEncryptionKey() : "HTTH_CLIENT_KEY_DEFAULT_SECRET_2026");
    }

    private void saveConfig() {
        if (config == null) return;
        config.setGithubToken(txtToken.getText().trim());
        config.setGithubOwner(txtOwner.getText().trim());
        config.setGithubRepo(txtRepo.getText().trim());
        config.setGithubFilePath(txtFilePath.getText().trim());
        config.setGithubBranch(txtBranch.getText().trim());
        config.setEncryptionKey(txtSecretKey.getText().trim());
        config.save();

        lblStatus.setForeground(UITheme.SUCCESS_GREEN);
        lblStatus.setText("Đã lưu cấu hình thành công vào config.properties!");
    }

    private void testConnection() {
        saveConfig();
        lblStatus.setForeground(UITheme.PRIMARY_BLUE);
        lblStatus.setText("Đang kiểm tra kết nối tới GitHub...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GitHubService service = new GitHubService(config);
                    final String msg = service.testConnection();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (msg.contains("thành công")) {
                                lblStatus.setForeground(UITheme.SUCCESS_GREEN);
                            } else {
                                lblStatus.setForeground(UITheme.DANGER_RED);
                            }
                            lblStatus.setText(msg);
                        }
                    });
                } catch (Exception ex) {
                    final String err = ex.getMessage();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            lblStatus.setForeground(UITheme.DANGER_RED);
                            lblStatus.setText("Lỗi kết nối: " + err);
                        }
                    });
                }
            }
        }).start();
    }
}
