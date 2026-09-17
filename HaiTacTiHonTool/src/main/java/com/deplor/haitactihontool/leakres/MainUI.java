package com.deplor.haitactihontool.leakres;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.config.Lang;
import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.ui.components.StyledUI;
import com.deplor.haitactihontool.map.GameMap;
import com.deplor.haitactihontool.map.MapDataLoader;
import com.deplor.haitactihontool.map.TileMapConfig;

public class MainUI extends JPanel {

    private DefaultListModel<ServerEntry> serverListModel;
    private JList<ServerEntry> serverList;

    private JTextField txtName;
    private JTextField txtHost;
    private JTextField txtPort;
    private JComboBox<String> comboServerZoom;

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JComboBox<String> comboZoom;

    private JTextField txtFromId;
    private JTextField txtToId;


    private JProgressBar progressIcon;
    private JProgressBar progressPart;
    private JProgressBar progressEffect;
    private JProgressBar progressEffectAuto;
    private JProgressBar progressFashion;
    private JProgressBar progressDataServer;
    private JProgressBar progressMap;
    private JProgressBar progressItem347;
    private JProgressBar progressClanHT;
    
    private JTextArea console;
    private JButton btnConnect;
    private JButton btnLogin;
    
    private JButton btnLeakIcon;
    private JButton btnLeakPart;
    private JButton btnLeakEffect;
    private JButton btnLeakEffectAuto;
    private JButton btnLeakFashion;
    private JButton btnLeakDataServer;
    private JButton btnLeakMap;
    private JButton btnLeakItem347;
    private JButton btnLeakClanHT;
    private JButton btnLeakFullDataServer;
    private JButton btnStopLeak;

    private LeakSession activeSession = null;
    private final Object sqlFileLock = new Object();

    // Leak status flags
    private volatile boolean leakingIcon = false;
    private volatile boolean leakingEffect = false;
    private volatile boolean leakingEffectAuto = false;
    private volatile boolean leakingPart = false;
    private volatile boolean leakingFashion = false;
    private volatile boolean leakingDataServer = false;
    private volatile boolean leakingMap = false;
    private volatile boolean leakingItem347 = false;
    private volatile boolean leakingClanHT = false;

    // Counters & timestamps for accurate progress tracking
    private volatile int iconSavedCount = 0;
    private volatile int partSavedCount = 0;
    private volatile int effectSavedCount = 0;
    private volatile int effectAutoSavedCount = 0;
    private volatile int mapSavedCount = 0;

    private volatile long lastIconReceivedTime = 0;
    private volatile long lastPartReceivedTime = 0;
    private volatile long lastEffectReceivedTime = 0;
    private volatile long lastEffectAutoReceivedTime = 0;
    private volatile long lastMapReceivedTime = 0;

    // DataServer state variables
    private int dataServerTotal = 0;
    private int dataServerCurrent = 0;
    private String dataServerCategory = "";

    public MainUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_DARKER);

        serverListModel = new DefaultListModel<>();
        serverList = new JList<>(serverListModel);
        serverList.setBackground(Theme.BG_DARK);
        serverList.setForeground(Theme.TEXT_MAIN);
        serverList.setFont(Theme.F_MAIN);
        serverList.setSelectionBackground(Theme.ACCENT);
        serverList.setSelectionForeground(Color.WHITE);
        serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load servers
        loadServers();

        // Selection Listener
        serverList.addListSelectionListener(e -> {
            ServerEntry sel = serverList.getSelectedValue();
            if (sel != null) {
                txtName.setText(sel.name);
                txtHost.setText(sel.host);
                txtPort.setText(String.valueOf(sel.port));
                comboServerZoom.setSelectedIndex(sel.zoomIndex);
                comboZoom.setSelectedIndex(sel.zoomIndex);
            }
        });

        // Left Panel (Scrollable Connection Selector)
        JScrollPane scrollLeft = new JScrollPane(buildLeftPanel());
        StyledUI.styleScrollPane(scrollLeft);
        scrollLeft.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollLeft.setPreferredSize(new Dimension(340, 0));
        scrollLeft.setMinimumSize(new Dimension(300, 0));

        // Right Panel split (Top: operations & progress | Bottom: console)
        JSplitPane splitRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildRightTopPanel(), buildConsolePanel());
        splitRight.setDividerLocation(340);
        splitRight.setDividerSize(2);
        splitRight.setContinuousLayout(true);
        splitRight.setBorder(null);
        splitRight.setBackground(Theme.BG_DARKER);

        // Main Layout Split
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollLeft, splitRight);
        mainSplit.setDividerLocation(340);
        mainSplit.setDividerSize(2);
        mainSplit.setContinuousLayout(true);
        mainSplit.setBorder(null);
        mainSplit.setBackground(Theme.BG_DARKER);

        add(mainSplit, BorderLayout.CENTER);
        updateUIState();
    }

    private JPanel buildLeftPanel() {
        JPanel leftContainer = new JPanel(new GridBagLayout());
        leftContainer.setOpaque(false);
        leftContainer.setBorder(new EmptyBorder(0, 0, 0, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Card 1: Server List & Details
        JPanel serverCard = StyledUI.createCard(new BorderLayout(0, 8));
        serverCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER),
            new EmptyBorder(12, 12, 12, 12)
        ));

        serverCard.add(StyledUI.createHeader("SERVER SELECTOR"), BorderLayout.NORTH);

        JScrollPane scrollList = new JScrollPane(serverList);
        StyledUI.styleScrollPane(scrollList);
        scrollList.setPreferredSize(new Dimension(0, 120));
        serverCard.add(scrollList, BorderLayout.CENTER);

        // Server edit form
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 4));
        form.setOpaque(false);

        form.add(StyledUI.createLabel("Server Name", Theme.F_SMALL, Theme.TEXT_DIM));
        txtName = StyledUI.createTextField("");
        form.add(txtName);

        form.add(StyledUI.createLabel("Host/IP Address", Theme.F_SMALL, Theme.TEXT_DIM));
        txtHost = StyledUI.createTextField("");
        form.add(txtHost);

        form.add(StyledUI.createLabel("Port", Theme.F_SMALL, Theme.TEXT_DIM));
        txtPort = StyledUI.createTextField("");
        form.add(txtPort);

        form.add(StyledUI.createLabel("Default Zoom", Theme.F_SMALL, Theme.TEXT_DIM));
        comboServerZoom = new JComboBox<>(new String[]{"Zoom 1", "Zoom 2", "Zoom 3", "Zoom 4"});
        comboServerZoom.setBackground(Theme.BG_DARKER);
        comboServerZoom.setForeground(Theme.TEXT_MAIN);
        comboServerZoom.setFont(Theme.F_MAIN);
        form.add(comboServerZoom);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 0));
        buttons.setOpaque(false);

        JButton btnAdd = StyledUI.createButton("Add/Save", Theme.ACCENT);
        btnAdd.addActionListener(e -> addOrSaveServer());
        buttons.add(btnAdd);

        JButton btnDel = StyledUI.createButton("Delete", Theme.ERROR);
        btnDel.addActionListener(e -> deleteServer());
        buttons.add(btnDel);

        form.add(buttons);
        serverCard.add(form, BorderLayout.SOUTH);

        leftContainer.add(serverCard, gbc);

        // Card 2: Credentials & Connection
        gbc.gridy++;
        gbc.insets = new Insets(12, 0, 0, 0);

        JPanel loginCard = StyledUI.createCard(new GridBagLayout());
        loginCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER),
            new EmptyBorder(12, 12, 12, 12)
        ));

        GridBagConstraints lcGbc = new GridBagConstraints();
        lcGbc.gridx = 0;
        lcGbc.gridy = 0;
        lcGbc.weightx = 1.0;
        lcGbc.fill = GridBagConstraints.HORIZONTAL;
        lcGbc.anchor = GridBagConstraints.WEST;

        loginCard.add(StyledUI.createHeader("TARGET CREDENTIALS"), lcGbc);

        lcGbc.gridy++;
        lcGbc.insets = new Insets(8, 0, 2, 0);
        loginCard.add(StyledUI.createLabel("ACCOUNT USERNAME", Theme.F_SMALL, Theme.TEXT_DIM), lcGbc);
        lcGbc.gridy++;
        lcGbc.insets = new Insets(0, 0, 8, 0);
        txtUsername = StyledUI.createTextField("1");
        txtUsername.putClientProperty("JTextField.placeholderText", "Username / Email");
        loginCard.add(txtUsername, lcGbc);

        lcGbc.gridy++;
        lcGbc.insets = new Insets(0, 0, 2, 0);
        loginCard.add(StyledUI.createLabel("ACCOUNT PASSWORD", Theme.F_SMALL, Theme.TEXT_DIM), lcGbc);
        lcGbc.gridy++;
        lcGbc.insets = new Insets(0, 0, 8, 0);
        txtPassword = StyledUI.createTextField("1");
        txtPassword.putClientProperty("JTextField.placeholderText", "Password");
        loginCard.add(txtPassword, lcGbc);
        lcGbc.gridy++;
        lcGbc.insets = new Insets(0, 0, 2, 0);
        loginCard.add(StyledUI.createLabel("TARGET ZOOM", Theme.F_SMALL, Theme.TEXT_DIM), lcGbc);
        lcGbc.gridy++;
        lcGbc.insets = new Insets(0, 0, 8, 0);
        comboZoom = new JComboBox<>(new String[]{"Zoom 1", "Zoom 2", "Zoom 3", "Zoom 4"});
        comboZoom.setSelectedIndex(3);
        comboZoom.setBackground(Theme.BG_DARKER);
        comboZoom.setForeground(Theme.TEXT_MAIN);
        comboZoom.setFont(Theme.F_MAIN);
        loginCard.add(comboZoom, lcGbc);

        lcGbc.gridy++;
        lcGbc.insets = new Insets(4, 0, 0, 0);
        JPanel connBtns = new JPanel(new GridLayout(1, 2, 8, 0));
        connBtns.setOpaque(false);
        btnConnect = StyledUI.createButton("Connect", Theme.ACCENT);
        btnConnect.addActionListener(e -> toggleConnection());
        connBtns.add(btnConnect);

        btnLogin = StyledUI.createButton("Login", Theme.ACCENT_HOVER);
        btnLogin.addActionListener(e -> toggleLogin());
        connBtns.add(btnLogin);
        loginCard.add(connBtns, lcGbc);

        leftContainer.add(loginCard, gbc);

        // Card 3: Target Range
        gbc.gridy++;
        gbc.insets = new Insets(12, 0, 0, 0);
        JPanel rangeCard = StyledUI.createCard(new GridBagLayout());
        rangeCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER),
            new EmptyBorder(12, 12, 12, 12)
        ));
        GridBagConstraints rcGbc = new GridBagConstraints();
        rcGbc.gridx = 0;
        rcGbc.gridy = 0;
        rcGbc.weightx = 1.0;
        rcGbc.fill = GridBagConstraints.HORIZONTAL;
        rangeCard.add(StyledUI.createHeader("ID RANGE"), rcGbc);

        rcGbc.gridy++;
        rcGbc.insets = new Insets(8, 0, 2, 0);
        rangeCard.add(StyledUI.createLabel("FROM ID", Theme.F_SMALL, Theme.TEXT_DIM), rcGbc);
        rcGbc.gridy++;
        rcGbc.insets = new Insets(0, 0, 8, 0);
        txtFromId = StyledUI.createTextField("0");
        rangeCard.add(txtFromId, rcGbc);

        rcGbc.gridy++;
        rcGbc.insets = new Insets(0, 0, 2, 0);
        rangeCard.add(StyledUI.createLabel("TO ID", Theme.F_SMALL, Theme.TEXT_DIM), rcGbc);
        rcGbc.gridy++;
        rcGbc.insets = new Insets(0, 0, 8, 0);
        txtToId = StyledUI.createTextField("1000");
        rangeCard.add(txtToId, rcGbc);

        leftContainer.add(rangeCard, gbc);

        // Card 4: Operations
        gbc.gridy++;
        gbc.insets = new Insets(12, 0, 0, 0);
        JPanel actionCard = StyledUI.createCard(new GridLayout(0, 1, 0, 6));
        actionCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER),
            new EmptyBorder(12, 12, 12, 12)
        ));
        actionCard.add(StyledUI.createHeader("OPERATIONS"));

        btnLeakIcon = StyledUI.createButton("Kéo Icon", Theme.ACCENT);
        btnLeakIcon.addActionListener(e -> startIconLeakWorkflow());
        actionCard.add(btnLeakIcon);

        btnLeakPart = StyledUI.createButton("Kéo Part", Theme.ACCENT);
        btnLeakPart.addActionListener(e -> startPartLeakWorkflow());
        actionCard.add(btnLeakPart);

        btnLeakEffect = StyledUI.createButton("Kéo Effect", Theme.ACCENT);
        btnLeakEffect.addActionListener(e -> startEffectLeakWorkflow());
        actionCard.add(btnLeakEffect);

        btnLeakEffectAuto = StyledUI.createButton("Kéo Effect Auto", Theme.ACCENT);
        btnLeakEffectAuto.addActionListener(e -> startEffectAutoLeakWorkflow());
        actionCard.add(btnLeakEffectAuto);

        btnLeakFashion = StyledUI.createButton("Kéo Fashion", Theme.ACCENT);
        btnLeakFashion.addActionListener(e -> startFashionLeakWorkflow());
        actionCard.add(btnLeakFashion);

        btnLeakDataServer = StyledUI.createButton("Kéo DataServer", Theme.ACCENT);
        btnLeakDataServer.addActionListener(e -> startDataServerLeakWorkflow());
        actionCard.add(btnLeakDataServer);

        btnLeakMap = StyledUI.createButton("Kéo Map", Theme.ACCENT);
        btnLeakMap.addActionListener(e -> startMapLeakWorkflow());
        actionCard.add(btnLeakMap);

        btnLeakItem347 = StyledUI.createButton("Kéo Item 347", Theme.ACCENT);
        btnLeakItem347.addActionListener(e -> startItem347LeakWorkflow());
        actionCard.add(btnLeakItem347);

        btnLeakClanHT = StyledUI.createButton("Kéo Clan Hành Trình", Theme.ACCENT);
        btnLeakClanHT.addActionListener(e -> startClanHTLeakWorkflow());
        actionCard.add(btnLeakClanHT);

        btnLeakFullDataServer = StyledUI.createButton("Kéo Full DataServer (All Zooms)", Theme.ACCENT_HOVER);
        btnLeakFullDataServer.addActionListener(e -> startFullDataServerLeakWorkflow());
        actionCard.add(btnLeakFullDataServer);

        btnStopLeak = StyledUI.createButton("Dừng Quá Trình Kéo", Theme.ERROR);
        btnStopLeak.addActionListener(e -> stopLeakerLoops());
        actionCard.add(btnStopLeak);

        leftContainer.add(actionCard, gbc);

        return leftContainer;
    }

    private JPanel buildRightTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel title = StyledUI.createHeader("LEAK PROGRESS STATUS");
        panel.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 1, 0, 6));
        grid.setOpaque(false);

        progressIcon = createStyledProgressBar("Icon");
        progressPart = createStyledProgressBar("Part");
        progressEffect = createStyledProgressBar("Effect");
        progressEffectAuto = createStyledProgressBar("EffectAuto");
        progressFashion = createStyledProgressBar("Fashion");
        progressDataServer = createStyledProgressBar("DataServer");
        progressMap = createStyledProgressBar("Map");
        progressItem347 = createStyledProgressBar("Item 347");
        progressClanHT = createStyledProgressBar("Clan Hành Trình");

        grid.add(progressIcon);
        grid.add(progressPart);
        grid.add(progressEffect);
        grid.add(progressEffectAuto);
        grid.add(progressFashion);
        grid.add(progressDataServer);
        grid.add(progressMap);
        grid.add(progressItem347);
        grid.add(progressClanHT);

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JProgressBar createStyledProgressBar(String title) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setString(title + ": Idle");
        bar.setFont(Theme.F_SMALL);
        bar.setForeground(Theme.ACCENT);
        bar.setBackground(Theme.BG_DARK);
        bar.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        return bar;
    }

    private JPanel buildConsolePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(5, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(StyledUI.createHeader("CONSOLE OUTPUT"), BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnPanel.setOpaque(false);
        JButton btnClear = StyledUI.createButton("Clear", Theme.BG_CARD);
        btnClear.addActionListener(e -> console.setText(""));
        btnPanel.add(btnClear);

        JButton btnOpen = StyledUI.createButton("Open Dir", Theme.BG_CARD);
        btnOpen.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File(AppConfig.getPath("Data/LeakRes/")));
            } catch (Exception ex) {
                logError("Cannot open dir: " + ex.getMessage());
            }
        });
        btnPanel.add(btnOpen);
        top.add(btnPanel, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        console = new JTextArea();
        console.setEditable(false);
        console.setBackground(Theme.BG_DARK);
        console.setForeground(Theme.TEXT_MAIN);
        console.setFont(Theme.F_MONO);
        JScrollPane sp = new JScrollPane(console);
        StyledUI.styleScrollPane(sp);
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            if (console != null) {
                console.append("[" + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()) + "] " + msg + "\n");
                console.setCaretPosition(console.getDocument().getLength());
            }
        });
    }

    private void logError(String msg) {
        log("[ERROR] " + msg);
    }

    private void updateUIState() {
        boolean isConnected = (activeSession != null && activeSession.connected);
        boolean isGame = (activeSession != null && activeSession.isGameEntered);
        boolean isAnyLeaking = (leakingIcon || leakingPart || leakingEffect || leakingEffectAuto ||
                                leakingFashion || leakingDataServer || leakingMap || leakingItem347 || leakingClanHT);

        if (btnConnect != null) {
            btnConnect.setText(isConnected ? "Disconnect" : "Connect");
            btnConnect.setEnabled(!isAnyLeaking);
        }
        if (btnLogin != null) {
            btnLogin.setEnabled(isConnected && !isGame && !isAnyLeaking);
        }
        if (btnStopLeak != null) {
            btnStopLeak.setEnabled(isAnyLeaking);
        }
    }

    private boolean ensureSession(boolean needLogin) throws Exception {
        String host = txtHost.getText().trim();
        String portStr = txtPort.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (host.isEmpty() || portStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select/add a server.", "Missing server info", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (needLogin && (username.isEmpty() || password.isEmpty()
                || username.equalsIgnoreCase("tkmk") || password.equalsIgnoreCase("tkmk"))) {
            JOptionPane.showMessageDialog(this, "Valid account credentials are required for this action.", "Missing info", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int port = Integer.parseInt(portStr);
        int zoomLevel = comboZoom.getSelectedIndex() + 1;

        boolean isSessionReady = (activeSession != null && activeSession.connected && (activeSession.isGameEntered || !needLogin));
        if (!isSessionReady) {
            log("Auto-connecting to server " + host + ":" + port + "...");
            if (activeSession != null) {
                activeSession.disconnect();
            }

            console.setText("");

            activeSession = new LeakSession(host, port, username, password, zoomLevel);
            activeSession.start();

            // Wait for key exchange
            long tGame = System.currentTimeMillis();
            while (activeSession != null && (activeSession.isConnecting || activeSession.connected) && !activeSession.getKeyComplete) {
                if (System.currentTimeMillis() - tGame > 10000) {
                    throw new IOException("Connection or Handshake timeout!");
                }
                Thread.sleep(100);
            }

            if (activeSession != null && activeSession.connected && needLogin) {
                activeSession.sendLogin();
                long tLogin = System.currentTimeMillis();
                while (activeSession != null && activeSession.connected && !activeSession.isGameEntered) {
                    if (System.currentTimeMillis() - tLogin > 15000) {
                        throw new IOException("Login or Enter game timeout!");
                    }
                    Thread.sleep(100);
                }
            }
        }

        return (activeSession != null && activeSession.connected && (activeSession.isGameEntered || !needLogin));
    }

    private int[] parseBounds() {
        try {
            int fromVal = Integer.parseInt(txtFromId.getText().trim());
            int toVal = Integer.parseInt(txtToId.getText().trim());
            if (fromVal < 0 || toVal < 0 || fromVal > toVal) {
                JOptionPane.showMessageDialog(this, "Invalid ID Range! Please check that From ID <= To ID.", "Range Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return new int[]{fromVal, toVal};
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Range values must be valid integers.", "Range Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void resetProgressBar(JProgressBar bar) {
        if (bar != null) {
            SwingUtilities.invokeLater(() -> {
                bar.setMinimum(0);
                bar.setMaximum(100);
                bar.setValue(0);
                bar.setString("Idle");
                bar.setForeground(Theme.ACCENT);
            });
        }
    }

    private void startIconLeakWorkflow() {
        new Thread(() -> {
            try {
                resetProgressBar(progressIcon);
                if (!ensureSession(true)) return;
                int[] bounds = parseBounds();
                if (bounds == null) return;
                leakingIcon = true;
                iconSavedCount = 0;
                lastIconReceivedTime = System.currentTimeMillis();
                SwingUtilities.invokeLater(this::updateUIState);
                iconLeakLoop(bounds[0], bounds[1]);
            } catch (Exception ex) {
                logError("Failed to start Icon leak: " + ex.getMessage());
                leakingIcon = false;
                SwingUtilities.invokeLater(() -> {
                    if (progressIcon != null) {
                        progressIcon.setString("Lỗi");
                        progressIcon.setForeground(Theme.ERROR);
                    }
                    updateUIState();
                });
            }
        }).start();
    }

    private void startPartLeakWorkflow() {
        new Thread(() -> {
            try {
                resetProgressBar(progressPart);
                if (!ensureSession(true)) return;
                int[] bounds = parseBounds();
                if (bounds == null) return;
                leakingPart = true;
                partSavedCount = 0;
                lastPartReceivedTime = System.currentTimeMillis();
                SwingUtilities.invokeLater(this::updateUIState);
                partLeakLoop(bounds[0], bounds[1]);
            } catch (Exception ex) {
                logError("Failed to start Part leak: " + ex.getMessage());
                leakingPart = false;
                SwingUtilities.invokeLater(() -> {
                    if (progressPart != null) {
                        progressPart.setString("Lỗi");
                        progressPart.setForeground(Theme.ERROR);
                    }
                    updateUIState();
                });
            }
        }).start();
    }

    private void startEffectLeakWorkflow() {
        new Thread(() -> {
            try {
                resetProgressBar(progressEffect);
                if (!ensureSession(true)) return;
                int[] bounds = parseBounds();
                if (bounds == null) return;
                leakingEffect = true;
                effectSavedCount = 0;
                lastEffectReceivedTime = System.currentTimeMillis();
                SwingUtilities.invokeLater(this::updateUIState);
                effectLeakLoop(bounds[0], bounds[1]);
            } catch (Exception ex) {
                logError("Failed to start Effect leak: " + ex.getMessage());
                leakingEffect = false;
                SwingUtilities.invokeLater(() -> {
                    if (progressEffect != null) {
                        progressEffect.setString("Lỗi");
                        progressEffect.setForeground(Theme.ERROR);
                    }
                    updateUIState();
                });
            }
        }).start();
    }

    private void startEffectAutoLeakWorkflow() {
        new Thread(() -> {
            try {
                resetProgressBar(progressEffectAuto);
                if (!ensureSession(true)) return;
                int[] bounds = parseBounds();
                if (bounds == null) return;
                leakingEffectAuto = true;
                effectAutoSavedCount = 0;
                lastEffectAutoReceivedTime = System.currentTimeMillis();
                SwingUtilities.invokeLater(this::updateUIState);
                effectAutoLeakLoop(bounds[0], bounds[1]);
            } catch (Exception ex) {
                logError("Failed to start EffectAuto leak: " + ex.getMessage());
                leakingEffectAuto = false;
                SwingUtilities.invokeLater(() -> {
                    if (progressEffectAuto != null) {
                        progressEffectAuto.setString("Lỗi");
                        progressEffectAuto.setForeground(Theme.ERROR);
                    }
                    updateUIState();
                });
            }
        }).start();
    }

    private void startClanHTLeakWorkflow() {
        new Thread(() -> {
            try {
                resetProgressBar(progressClanHT);
                if (!ensureSession(true)) return;
                leakingClanHT = true;
                SwingUtilities.invokeLater(this::updateUIState);
                clanHTLeak();
            } catch (Exception ex) {
                logError("Failed to start Clan Journey leak: " + ex.getMessage());
                leakingClanHT = false;
                SwingUtilities.invokeLater(() -> {
                    if (progressClanHT != null) {
                        progressClanHT.setString("Lỗi");
                        progressClanHT.setForeground(Theme.ERROR);
                    }
                    updateUIState();
                });
            }
        }).start();
    }

    private void clanHTLeak() {
        if (activeSession != null) {
            activeSession.clanHTLeak();
        }
    }

    private void startFashionLeakWorkflow() {
        new Thread(() -> {
            try {
                resetProgressBar(progressFashion);
                if (!ensureSession(true)) return;
                leakingFashion = true;
                SwingUtilities.invokeLater(this::updateUIState);
                fashionLeak();
            } catch (Exception ex) {
                logError("Failed to start Fashion leak: " + ex.getMessage());
                leakingFashion = false;
                SwingUtilities.invokeLater(() -> {
                    if (progressFashion != null) {
                        progressFashion.setString("Lỗi");
                        progressFashion.setForeground(Theme.ERROR);
                    }
                    updateUIState();
                });
            }
        }).start();
    }

    private void startItem347LeakWorkflow() {
        new Thread(() -> {
            try {
                resetProgressBar(progressItem347);
                if (!ensureSession(true)) return;
                leakingItem347 = true;
                SwingUtilities.invokeLater(this::updateUIState);
                item347Leak();
            } catch (Exception ex) {
                logError("Failed to start Item 347 leak: " + ex.getMessage());
                leakingItem347 = false;
                SwingUtilities.invokeLater(() -> {
                    if (progressItem347 != null) {
                        progressItem347.setString("Lỗi");
                        progressItem347.setForeground(Theme.ERROR);
                    }
                    updateUIState();
                });
            }
        }).start();
    }

    private void item347Leak() {
        if (activeSession != null) {
            activeSession.item347Leak();
        }
    }

    private void startDataServerLeakWorkflow() {
        new Thread(() -> {
            try {
                resetProgressBar(progressDataServer);
                if (!ensureSession(false)) return;
                leakingDataServer = true;
                SwingUtilities.invokeLater(this::updateUIState);
                dataServerLeak();
            } catch (Exception ex) {
                logError("Failed to start DataServer leak: " + ex.getMessage());
                leakingDataServer = false;
                SwingUtilities.invokeLater(() -> {
                    if (progressDataServer != null) {
                        progressDataServer.setString("Lỗi");
                        progressDataServer.setForeground(Theme.ERROR);
                    }
                    updateUIState();
                });
            }
        }).start();
    }

    private void startMapLeakWorkflow() {
        new Thread(() -> {
            try {
                resetProgressBar(progressMap);
                if (!ensureSession(true)) return;
                int[] bounds = parseBounds();
                if (bounds == null) return;
                leakingMap = true;
                mapSavedCount = 0;
                lastMapReceivedTime = System.currentTimeMillis();
                SwingUtilities.invokeLater(this::updateUIState);
                mapLeakLoop(bounds[0], bounds[1]);
            } catch (Exception ex) {
                logError("Failed to start Map leak: " + ex.getMessage());
                leakingMap = false;
                SwingUtilities.invokeLater(() -> {
                    if (progressMap != null) {
                        progressMap.setString("Lỗi");
                        progressMap.setForeground(Theme.ERROR);
                    }
                    updateUIState();
                });
            }
        }).start();
    }

    private void mapLeakLoop(int start, int end) {
        int total = end - start + 1;
        log("[Map Leak] Bắt đầu gửi yêu cầu map từ ID " + start + " đến " + end + " (Tổng: " + total + ")...");
        SwingUtilities.invokeLater(() -> {
            progressMap.setMinimum(0);
            progressMap.setMaximum(Math.max(1, total));
            progressMap.setValue(0);
            progressMap.setString("Gửi: 0 / " + total + " | Đã lưu: 0");
            progressMap.setForeground(Theme.ACCENT);
        });

        int sent = 0;
        int batchSize = 5;
        for (int id = start; id <= end && leakingMap && activeSession != null && activeSession.connected; id++) {
            try {
                Message m = new Message((byte) 34);
                m.writer().writeShort((short) id);
                activeSession.sendMessage(m);
                sent++;

                final int currSent = sent;
                SwingUtilities.invokeLater(() -> {
                    progressMap.setValue(currSent);
                    progressMap.setString("Gửi: " + currSent + " / " + total + " | Đã lưu: " + mapSavedCount);
                });

                if (sent % batchSize == 0) {
                    Thread.sleep(200);
                } else {
                    Thread.sleep(80);
                }
            } catch (Exception e) {
                logError("[Map Leak] Lỗi gửi yêu cầu cho Map ID " + id + ": " + e.getMessage());
                break;
            }
        }

        if (activeSession == null || !activeSession.connected) {
            leakingMap = false;
            logError("[Map Leak] Mất kết nối server!");
            SwingUtilities.invokeLater(() -> {
                progressMap.setString("Mất kết nối (Đã lưu: " + mapSavedCount + ")");
                progressMap.setForeground(Theme.ERROR);
            });
            checkLeakerState();
            return;
        }

        if (!leakingMap) {
            log("[Map Leak] Người dùng đã dừng quá trình kéo map.");
            SwingUtilities.invokeLater(() -> {
                progressMap.setString("Đã dừng (Đã lưu: " + mapSavedCount + ")");
                progressMap.setForeground(Theme.WARNING);
            });
            checkLeakerState();
            return;
        }

        log("[Map Leak] Đã gửi xong toàn bộ yêu cầu. Đang chờ phản hồi dữ liệu từ server...");
        SwingUtilities.invokeLater(() -> {
            progressMap.setString("Đang nhận... (Đã lưu: " + mapSavedCount + ")");
            progressMap.setForeground(Theme.ACCENT);
        });

        long waitStart = System.currentTimeMillis();
        int lastSaved = mapSavedCount;
        while (leakingMap && activeSession != null && activeSession.connected) {
            long now = System.currentTimeMillis();
            if (mapSavedCount != lastSaved) {
                lastSaved = mapSavedCount;
                lastMapReceivedTime = now;
            }
            if (now - lastMapReceivedTime > 3500 || now - waitStart > 20000) {
                break;
            }
            try { Thread.sleep(200); } catch (InterruptedException ignored) { break; }
        }

        leakingMap = false;
        final int finalSaved = mapSavedCount;
        log("[Map Leak] Hoàn thành kéo map! Đã lưu: " + finalSaved + " map.");
        SwingUtilities.invokeLater(() -> {
            progressMap.setValue(progressMap.getMaximum());
            if (finalSaved > 0) {
                progressMap.setString("Hoàn thành (Đã lưu: " + finalSaved + ")");
                progressMap.setForeground(Theme.SUCCESS);
            } else {
                progressMap.setString("Hoàn tất (Không có map mới)");
                progressMap.setForeground(Theme.TEXT_DIM);
            }
        });
        checkLeakerState();
    }

    private void startFullDataServerLeakWorkflow() {
        // Prevent double-click: immediately mark as leaking and disable buttons
        if (leakingDataServer) return;
        leakingDataServer = true;
        SwingUtilities.invokeLater(() -> updateUIState());

        new Thread(() -> {
            try {
                resetProgressBars();
                
                String host = txtHost.getText().trim();
                String portStr = txtPort.getText().trim();
                String username = txtUsername.getText().trim();
                String password = txtPassword.getText().trim();

                if (host.isEmpty() || portStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select/add a server.", "Missing server info", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int port = Integer.parseInt(portStr);
                
                log("[Full DataServer] Starting sequential download for all zoom levels...");
                
                for (int zoom = 1; zoom <= 4; zoom++) {
                    log("[Full DataServer] === Target Zoom level " + zoom + " ===");
                    
                    // Disconnect previous session completely
                    if (activeSession != null) {
                        activeSession.disconnect();
                        activeSession = null;
                    }
                    Thread.sleep(1000); // Wait for socket cleanup
                    
                    // Create new session for this zoom level
                    LeakSession session = new LeakSession(host, port, username, password, zoom);
                    activeSession = session;
                    session.start();
                    
                    // Wait for connection + key exchange with proper flags
                    long tStart = System.currentTimeMillis();
                    while (session.isConnecting || (session.connected && !session.getKeyComplete)) {
                        if (System.currentTimeMillis() - tStart > 15000) {
                            throw new IOException("Connection or Handshake timeout for zoom " + zoom + "!");
                        }
                        Thread.sleep(100);
                    }
                    
                    // Verify connection is OK after handshake
                    if (!session.connected || !session.getKeyComplete) {
                        throw new IOException("Session failed to connect for zoom " + zoom);
                    }
                    
                    log("[DataServer Leak] Sending request to server for Zoom " + zoom + "...");
                    
                    // Send -38 request (no login needed for DataServer!)
                    Message m = new Message((byte) -38);
                    m.writer().writeByte((byte) zoom);
                    session.sendMessage(m);
                    
                    // Wait until download completes (-40 received sets leakingDataServer=false)
                    // We need to re-set it for each zoom iteration
                    leakingDataServer = true;
                    
                    long tWait = System.currentTimeMillis();
                    while (leakingDataServer && session.connected) {
                        if (System.currentTimeMillis() - tWait > 5 * 60 * 1000) {
                            log("[Full DataServer] WARNING: Timeout waiting for completion on Zoom " + zoom + ". Moving on.");
                            break;
                        }
                        Thread.sleep(200);
                    }
                    
                    log("[Full DataServer] Completed Zoom level " + zoom);
                    
                    // Re-set for next zoom iteration
                    leakingDataServer = true;
                }
                
                log("[Full DataServer] Successfully completed all Zoom levels!");
                
            } catch (Exception ex) {
                logError("[Full DataServer] Failed: " + ex.getMessage());
            } finally {
                if (activeSession != null) {
                    activeSession.disconnect();
                    activeSession = null;
                }
                leakingDataServer = false;
                SwingUtilities.invokeLater(() -> updateUIState());
            }
        }).start();
    }

    private void resetProgressBars() {
        SwingUtilities.invokeLater(() -> {
            if (progressIcon != null) { progressIcon.setValue(0); progressIcon.setString("Idle"); progressIcon.setForeground(Theme.ACCENT); }
            if (progressPart != null) { progressPart.setValue(0); progressPart.setString("Idle"); progressPart.setForeground(Theme.ACCENT); }
            if (progressEffect != null) { progressEffect.setValue(0); progressEffect.setString("Idle"); progressEffect.setForeground(Theme.ACCENT); }
            if (progressEffectAuto != null) { progressEffectAuto.setValue(0); progressEffectAuto.setString("Idle"); progressEffectAuto.setForeground(Theme.ACCENT); }
            if (progressFashion != null) { progressFashion.setValue(0); progressFashion.setString("Idle"); progressFashion.setForeground(Theme.ACCENT); }
            if (progressDataServer != null) { progressDataServer.setValue(0); progressDataServer.setString("Idle"); progressDataServer.setForeground(Theme.ACCENT); }
            if (progressMap != null) { progressMap.setValue(0); progressMap.setString("Idle"); progressMap.setForeground(Theme.ACCENT); }
            if (progressItem347 != null) { progressItem347.setValue(0); progressItem347.setString("Idle"); progressItem347.setForeground(Theme.ACCENT); }
            if (progressClanHT != null) { progressClanHT.setValue(0); progressClanHT.setString("Idle"); progressClanHT.setForeground(Theme.ACCENT); }
        });
    }

    private void toggleConnection() {
        if (activeSession != null && activeSession.connected) {
            stopLeakerLoops();
            activeSession.disconnect();
            activeSession = null;
            log("Session disconnected.");
            updateUIState();
        } else {
            String host = txtHost.getText().trim();
            String portStr = txtPort.getText().trim();

            if (host.isEmpty() || portStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select/add a server.", "Missing info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int port;
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Port must be an integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int zoomLevel = comboZoom.getSelectedIndex() + 1;
            console.setText("");
            
            btnConnect.setEnabled(false);
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            activeSession = new LeakSession(host, port, username, password, zoomLevel);
            activeSession.start();
        }
    }

    private void toggleLogin() {
        if (activeSession == null || !activeSession.connected) {
            JOptionPane.showMessageDialog(this, "Please CONNECT to a server first.", "Not Connected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (activeSession.isGameEntered) {
            log("Already entered the game.");
            return;
        }

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()
                || username.equalsIgnoreCase("tkmk") || password.equalsIgnoreCase("tkmk")) {
            JOptionPane.showMessageDialog(this, "Please enter valid account credentials.", "Missing info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnLogin.setEnabled(false);
        activeSession.username = username;
        activeSession.password = password;

        new Thread(() -> {
            try {
                activeSession.sendLogin();
                
                // Wait for login and game enter
                long tEntry = System.currentTimeMillis();
                while (activeSession != null && activeSession.connected && !activeSession.isGameEntered) {
                    if (System.currentTimeMillis() - tEntry > 15000) {
                        throw new IOException("Login or Enter game timeout!");
                    }
                    Thread.sleep(100);
                }
                log("Manual Login successful!");
            } catch (Exception e) {
                logError("Manual login failed: " + e.getMessage());
            } finally {
                SwingUtilities.invokeLater(() -> updateUIState());
            }
        }).start();
    }

    private void startLeakerLoop(String type) {
        boolean isDataServer = "dataServer".equals(type);
        if (activeSession == null || !activeSession.connected || (!activeSession.isGameEntered && !isDataServer)) {
            logError("Not connected or not entered game yet!");
            return;
        }

        // Parse bounds
        int fromVal = 0;
        int toVal = 100;
        try {
            fromVal = Integer.parseInt(txtFromId.getText().trim());
            toVal = Integer.parseInt(txtToId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Range values must be valid integers.", "Range Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fromVal < 0 || toVal < 0 || fromVal > toVal) {
            JOptionPane.showMessageDialog(this, "Invalid ID Range! Please check that From ID <= To ID.", "Range Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final int start = fromVal;
        final int end = toVal;

        log("Starting request loop for: " + type + " (Range: " + start + " - " + end + ")");
        
        if ("ALL".equals(type) || "ICON".equals(type)) {
            leakingIcon = true;
            new Thread(() -> iconLeakLoop(start, end)).start();
        }
        if ("ALL".equals(type) || "PART".equals(type)) {
            leakingPart = true;
            new Thread(() -> partLeakLoop(start, end)).start();
        }
        if ("ALL".equals(type) || "EFFECT".equals(type)) {
            leakingEffect = true;
            new Thread(() -> effectLeakLoop(start, end)).start();
        }
        if ("ALL".equals(type) || "EFFECTAUTO".equals(type)) {
            leakingEffectAuto = true;
            new Thread(() -> effectAutoLeakLoop(start, end)).start();
        }
        if ("ALL".equals(type) || "FASHION".equals(type)) {
            leakingFashion = true;
            new Thread(() -> fashionLeak()).start();
        }
        if ("ALL".equals(type) || "DATASERVER".equals(type)) {
            leakingDataServer = true;
            new Thread(() -> dataServerLeak()).start();
        }

        updateUIState();
    }

    private void stopLeakerLoops() {
        if (leakingIcon || leakingEffect || leakingEffectAuto || leakingPart || leakingFashion || leakingDataServer || leakingMap || leakingItem347 || leakingClanHT) {
            boolean wasIcon = leakingIcon;
            boolean wasPart = leakingPart;
            boolean wasEffect = leakingEffect;
            boolean wasEffAuto = leakingEffectAuto;
            boolean wasFashion = leakingFashion;
            boolean wasDataServer = leakingDataServer;
            boolean wasMap = leakingMap;
            boolean wasItem347 = leakingItem347;
            boolean wasClanHT = leakingClanHT;

            leakingIcon = false;
            leakingEffect = false;
            leakingEffectAuto = false;
            leakingPart = false;
            leakingFashion = false;
            leakingDataServer = false;
            leakingMap = false;
            leakingItem347 = false;
            leakingClanHT = false;

            log("Leaking request loops stopped by user.");

            SwingUtilities.invokeLater(() -> {
                if (wasIcon && progressIcon != null) { progressIcon.setString("Đã dừng (Đã lưu: " + iconSavedCount + ")"); progressIcon.setForeground(Theme.WARNING); }
                if (wasPart && progressPart != null) { progressPart.setString("Đã dừng (Đã lưu: " + partSavedCount + ")"); progressPart.setForeground(Theme.WARNING); }
                if (wasEffect && progressEffect != null) { progressEffect.setString("Đã dừng (Đã lưu: " + effectSavedCount + ")"); progressEffect.setForeground(Theme.WARNING); }
                if (wasEffAuto && progressEffectAuto != null) { progressEffectAuto.setString("Đã dừng (Đã lưu: " + effectAutoSavedCount + ")"); progressEffectAuto.setForeground(Theme.WARNING); }
                if (wasFashion && progressFashion != null) { progressFashion.setString("Đã dừng"); progressFashion.setForeground(Theme.WARNING); }
                if (wasDataServer && progressDataServer != null) { progressDataServer.setString("Đã dừng"); progressDataServer.setForeground(Theme.WARNING); }
                if (wasMap && progressMap != null) { progressMap.setString("Đã dừng (Đã lưu: " + mapSavedCount + ")"); progressMap.setForeground(Theme.WARNING); }
                if (wasItem347 && progressItem347 != null) { progressItem347.setString("Đã dừng"); progressItem347.setForeground(Theme.WARNING); }
                if (wasClanHT && progressClanHT != null) { progressClanHT.setString("Đã dừng"); progressClanHT.setForeground(Theme.WARNING); }
                updateUIState();
            });
        }
    }

    private void checkLeakerState() {
        SwingUtilities.invokeLater(this::updateUIState);
    }

    private void loadServers() {
        serverListModel.clear();
        File file = new File(AppConfig.getPath("Data/LeakRes/listip.txt"));
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println("GenZ:103.77.242.147:2239:0:1");
                pw.println("Htth3:14.225.198.121:2236:0:2");
                pw.println("TiTeo:sv1.haitactiteo.com:2229:0:0");
                pw.println("Lengend:163.61.73.85:2239:0:1");
                pw.println("LengendTest:163.61.73.85:2229:0:2");
                pw.println("HaiTacK:103.200.22.12:2239:0:3");
                pw.println("Blue:14.225.208.26:2239:0:2");
                pw.println("Sea:14.225.209.3:2239:0:1");
                pw.println("SeaTest:103.90.226.217:2239:0:3");
                pw.println("Free:14.225.192.250:2239:0:0");
                pw.println("Merry:27.0.12.18:2228:0:2");
                pw.println("SunyTeam:27.0.12.101:2228:0:1");
                pw.println("Mattroi:14.225.198.121:2239:0:2");
                pw.println("World:14.225.204.51:2239:0:3");
                pw.println("Go:14.225.211.104:2239:0:2");
                pw.println("Red:14.225.208.207:2239:0:1");
                pw.println("Dream:160.250.128.115:2239:0:0");
                pw.println("LangHT:14.225.198.121:2232:0:2");
                pw.println("LocalZ1:127.0.0.1:2229:0:1");
                pw.println("LocalZ2:127.0.0.1:2239:0:0");
            } catch (IOException e) {
                logError("Failed to create default listip.txt: " + e.getMessage());
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(":");
                if (parts.length >= 3) {
                    String name = parts[0];
                    String host = parts[1];
                    int port = Integer.parseInt(parts[2]);
                    int type = parts.length > 3 ? Integer.parseInt(parts[3]) : 0;
                    int zoomIndex = parts.length > 4 ? Integer.parseInt(parts[4]) : 0;
                    serverListModel.addElement(new ServerEntry(name, host, port, type, zoomIndex));
                }
            }
        } catch (Exception e) {
            logError("Failed to load listip.txt: " + e.getMessage());
        }
    }

    private void saveServers() {
        File file = new File(AppConfig.getPath("Data/LeakRes/listip.txt"));
        file.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (int i = 0; i < serverListModel.size(); i++) {
                pw.println(serverListModel.get(i).toLine());
            }
        } catch (IOException e) {
            logError("Failed to save listip.txt: " + e.getMessage());
        }
    }

    private void addOrSaveServer() {
        String name = txtName.getText().trim();
        String host = txtHost.getText().trim();
        String portStr = txtPort.getText().trim();

        if (name.isEmpty() || host.isEmpty() || portStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Name, Host and Port fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int port = Integer.parseInt(portStr);
            int zoomIdx = comboServerZoom.getSelectedIndex();
            ServerEntry newEntry = new ServerEntry(name, host, port, 0, zoomIdx);

            boolean updated = false;
            for (int i = 0; i < serverListModel.size(); i++) {
                if (serverListModel.get(i).name.equalsIgnoreCase(name)) {
                    serverListModel.set(i, newEntry);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                serverListModel.addElement(newEntry);
            }
            saveServers();
            log("Saved server: " + name);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Port must be a valid integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteServer() {
        int idx = serverList.getSelectedIndex();
        if (idx >= 0) {
            ServerEntry entry = serverListModel.remove(idx);
            saveServers();
            log("Deleted server: " + entry.name);
            txtName.setText("");
            txtHost.setText("");
            txtPort.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a server from the list to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void iconLeakLoop(int start, int end) {
        int total = end - start + 1;
        log("[Icon Leak] Bắt đầu gửi yêu cầu icon từ ID " + start + " đến " + end + " (Tổng: " + total + ")...");
        SwingUtilities.invokeLater(() -> {
            progressIcon.setMinimum(0);
            progressIcon.setMaximum(Math.max(1, total));
            progressIcon.setValue(0);
            progressIcon.setString("Gửi: 0 / " + total + " | Đã lưu: 0");
            progressIcon.setForeground(Theme.ACCENT);
        });

        int sent = 0;
        int batchSize = 10;
        for (int id = start; id <= end && leakingIcon && activeSession != null && activeSession.connected; id++) {
            try {
                Message m = new Message((byte) -51);
                m.writer().writeShort((short) id);
                activeSession.sendMessage(m);
                sent++;

                final int currSent = sent;
                SwingUtilities.invokeLater(() -> {
                    progressIcon.setValue(currSent);
                    progressIcon.setString("Gửi: " + currSent + " / " + total + " | Đã lưu: " + iconSavedCount);
                });

                if (sent % batchSize == 0) {
                    Thread.sleep(60);
                } else {
                    Thread.sleep(25);
                }
            } catch (Exception e) {
                logError("[Icon Leak] Lỗi gửi yêu cầu cho Icon ID " + id + ": " + e.getMessage());
                break;
            }
        }

        if (activeSession == null || !activeSession.connected) {
            leakingIcon = false;
            logError("[Icon Leak] Mất kết nối server!");
            SwingUtilities.invokeLater(() -> {
                progressIcon.setString("Mất kết nối (Đã lưu: " + iconSavedCount + ")");
                progressIcon.setForeground(Theme.ERROR);
            });
            checkLeakerState();
            return;
        }

        if (!leakingIcon) {
            log("[Icon Leak] Người dùng đã dừng quá trình kéo icon.");
            SwingUtilities.invokeLater(() -> {
                progressIcon.setString("Đã dừng (Đã lưu: " + iconSavedCount + ")");
                progressIcon.setForeground(Theme.WARNING);
            });
            checkLeakerState();
            return;
        }

        log("[Icon Leak] Đã gửi xong toàn bộ yêu cầu. Đang chờ phản hồi dữ liệu từ server...");
        SwingUtilities.invokeLater(() -> {
            progressIcon.setString("Đang nhận dữ liệu... (Đã lưu: " + iconSavedCount + ")");
            progressIcon.setForeground(Theme.ACCENT);
        });

        long waitStart = System.currentTimeMillis();
        int lastSaved = iconSavedCount;
        while (leakingIcon && activeSession != null && activeSession.connected) {
            long now = System.currentTimeMillis();
            if (iconSavedCount != lastSaved) {
                lastSaved = iconSavedCount;
                lastIconReceivedTime = now;
            }
            if (now - lastIconReceivedTime > 2500 || now - waitStart > 15000) {
                break;
            }
            try { Thread.sleep(200); } catch (InterruptedException ignored) { break; }
        }

        leakingIcon = false;
        final int finalSaved = iconSavedCount;
        log("[Icon Leak] Hoàn thành kéo icon! Đã lưu: " + finalSaved + " file.");
        SwingUtilities.invokeLater(() -> {
            progressIcon.setValue(progressIcon.getMaximum());
            if (finalSaved > 0) {
                progressIcon.setString("Hoàn thành (Đã lưu: " + finalSaved + ")");
                progressIcon.setForeground(Theme.SUCCESS);
            } else {
                progressIcon.setString("Hoàn tất (Server không có icon trong dải ID này)");
                progressIcon.setForeground(Theme.TEXT_DIM);
            }
        });
        checkLeakerState();
    }

    private void partLeakLoop(int start, int end) {
        int total = end - start + 1;
        log("[Part Leak] Bắt đầu gửi yêu cầu part từ ID " + start + " đến " + end + " (Tổng: " + total + ")...");
        SwingUtilities.invokeLater(() -> {
            progressPart.setMinimum(0);
            progressPart.setMaximum(Math.max(1, total));
            progressPart.setValue(0);
            progressPart.setString("Gửi: 0 / " + total + " | Đã lưu: 0");
            progressPart.setForeground(Theme.ACCENT);
        });

        int sent = 0;
        int batchSize = 10;
        for (int id = start; id <= end && leakingPart && activeSession != null && activeSession.connected; id++) {
            try {
                Message m = new Message((byte) -82);
                m.writer().writeShort((short) id);
                activeSession.sendMessage(m);
                sent++;

                final int currSent = sent;
                SwingUtilities.invokeLater(() -> {
                    progressPart.setValue(currSent);
                    progressPart.setString("Gửi: " + currSent + " / " + total + " | Đã lưu: " + partSavedCount);
                });

                if (sent % batchSize == 0) {
                    Thread.sleep(60);
                } else {
                    Thread.sleep(25);
                }
            } catch (Exception e) {
                logError("[Part Leak] Lỗi gửi yêu cầu cho Part ID " + id + ": " + e.getMessage());
                break;
            }
        }

        if (activeSession == null || !activeSession.connected) {
            leakingPart = false;
            logError("[Part Leak] Mất kết nối server!");
            SwingUtilities.invokeLater(() -> {
                progressPart.setString("Mất kết nối (Đã lưu: " + partSavedCount + ")");
                progressPart.setForeground(Theme.ERROR);
            });
            checkLeakerState();
            return;
        }

        if (!leakingPart) {
            log("[Part Leak] Người dùng đã dừng quá trình kéo part.");
            SwingUtilities.invokeLater(() -> {
                progressPart.setString("Đã dừng (Đã lưu: " + partSavedCount + ")");
                progressPart.setForeground(Theme.WARNING);
            });
            checkLeakerState();
            return;
        }

        log("[Part Leak] Đã gửi xong toàn bộ yêu cầu. Đang chờ phản hồi dữ liệu từ server...");
        SwingUtilities.invokeLater(() -> {
            progressPart.setString("Đang nhận dữ liệu... (Đã lưu: " + partSavedCount + ")");
            progressPart.setForeground(Theme.ACCENT);
        });

        long waitStart = System.currentTimeMillis();
        int lastSaved = partSavedCount;
        while (leakingPart && activeSession != null && activeSession.connected) {
            long now = System.currentTimeMillis();
            if (partSavedCount != lastSaved) {
                lastSaved = partSavedCount;
                lastPartReceivedTime = now;
            }
            if (now - lastPartReceivedTime > 2500 || now - waitStart > 15000) {
                break;
            }
            try { Thread.sleep(200); } catch (InterruptedException ignored) { break; }
        }

        leakingPart = false;
        final int finalSaved = partSavedCount;
        log("[Part Leak] Hoàn thành kéo part! Đã lưu: " + finalSaved + " file.");
        SwingUtilities.invokeLater(() -> {
            progressPart.setValue(progressPart.getMaximum());
            if (finalSaved > 0) {
                progressPart.setString("Hoàn thành (Đã lưu: " + finalSaved + ")");
                progressPart.setForeground(Theme.SUCCESS);
            } else {
                progressPart.setString("Hoàn tất (Server không có part trong dải ID này)");
                progressPart.setForeground(Theme.TEXT_DIM);
            }
        });
        checkLeakerState();
    }

    private void effectLeakLoop(int start, int end) {
        int total = end - start + 1;
        log("[Effect Leak] Bắt đầu gửi yêu cầu effect từ ID " + start + " đến " + end + " (Tổng: " + total + ")...");
        SwingUtilities.invokeLater(() -> {
            progressEffect.setMinimum(0);
            progressEffect.setMaximum(Math.max(1, total));
            progressEffect.setValue(0);
            progressEffect.setString("Gửi: 0 / " + total + " | Đã lưu: 0");
            progressEffect.setForeground(Theme.ACCENT);
        });

        int sent = 0;
        int batchSize = 10;
        for (int id = start; id <= end && leakingEffect && activeSession != null && activeSession.connected; id++) {
            try {
                Message m = new Message((byte) 74);
                m.writer().writeShort((short) id);
                activeSession.sendMessage(m);
                sent++;

                final int currSent = sent;
                SwingUtilities.invokeLater(() -> {
                    progressEffect.setValue(currSent);
                    progressEffect.setString("Gửi: " + currSent + " / " + total + " | Đã lưu: " + effectSavedCount);
                });

                if (sent % batchSize == 0) {
                    Thread.sleep(60);
                } else {
                    Thread.sleep(25);
                }
            } catch (Exception e) {
                logError("[Effect Leak] Lỗi gửi yêu cầu cho Effect ID " + id + ": " + e.getMessage());
                break;
            }
        }

        if (activeSession == null || !activeSession.connected) {
            leakingEffect = false;
            logError("[Effect Leak] Mất kết nối server!");
            SwingUtilities.invokeLater(() -> {
                progressEffect.setString("Mất kết nối (Đã lưu: " + effectSavedCount + ")");
                progressEffect.setForeground(Theme.ERROR);
            });
            checkLeakerState();
            return;
        }

        if (!leakingEffect) {
            log("[Effect Leak] Người dùng đã dừng quá trình kéo effect.");
            SwingUtilities.invokeLater(() -> {
                progressEffect.setString("Đã dừng (Đã lưu: " + effectSavedCount + ")");
                progressEffect.setForeground(Theme.WARNING);
            });
            checkLeakerState();
            return;
        }

        log("[Effect Leak] Đã gửi xong toàn bộ yêu cầu. Đang chờ phản hồi dữ liệu từ server...");
        SwingUtilities.invokeLater(() -> {
            progressEffect.setString("Đang nhận dữ liệu... (Đã lưu: " + effectSavedCount + ")");
            progressEffect.setForeground(Theme.ACCENT);
        });

        long waitStart = System.currentTimeMillis();
        int lastSaved = effectSavedCount;
        while (leakingEffect && activeSession != null && activeSession.connected) {
            long now = System.currentTimeMillis();
            if (effectSavedCount != lastSaved) {
                lastSaved = effectSavedCount;
                lastEffectReceivedTime = now;
            }
            if (now - lastEffectReceivedTime > 2500 || now - waitStart > 15000) {
                break;
            }
            try { Thread.sleep(200); } catch (InterruptedException ignored) { break; }
        }

        leakingEffect = false;
        final int finalSaved = effectSavedCount;
        log("[Effect Leak] Hoàn thành kéo effect! Đã lưu: " + finalSaved + " file.");
        SwingUtilities.invokeLater(() -> {
            progressEffect.setValue(progressEffect.getMaximum());
            if (finalSaved > 0) {
                progressEffect.setString("Hoàn thành (Đã lưu: " + finalSaved + ")");
                progressEffect.setForeground(Theme.SUCCESS);
            } else {
                progressEffect.setString("Hoàn tất (Server không có effect trong dải ID này)");
                progressEffect.setForeground(Theme.TEXT_DIM);
            }
        });
        checkLeakerState();
    }

    private void effectAutoLeakLoop(int start, int end) {
        int total = end - start + 1;
        log("[EffectAuto Leak] Bắt đầu gửi yêu cầu effect auto từ ID " + start + " đến " + end + " (Tổng: " + total + ")...");
        SwingUtilities.invokeLater(() -> {
            progressEffectAuto.setMinimum(0);
            progressEffectAuto.setMaximum(Math.max(1, total));
            progressEffectAuto.setValue(0);
            progressEffectAuto.setString("Gửi: 0 / " + total + " | Đã lưu: 0");
            progressEffectAuto.setForeground(Theme.ACCENT);
        });

        int sent = 0;
        int batchSize = 10;
        for (int id = start; id <= end && leakingEffectAuto && activeSession != null && activeSession.connected; id++) {
            try {
                Message m = new Message((byte) -44);
                m.writer().writeShort((short) id);
                activeSession.sendMessage(m);
                sent++;

                final int currSent = sent;
                SwingUtilities.invokeLater(() -> {
                    progressEffectAuto.setValue(currSent);
                    progressEffectAuto.setString("Gửi: " + currSent + " / " + total + " | Đã lưu: " + effectAutoSavedCount);
                });

                if (sent % batchSize == 0) {
                    Thread.sleep(60);
                } else {
                    Thread.sleep(25);
                }
            } catch (Exception e) {
                logError("[EffectAuto Leak] Lỗi gửi yêu cầu cho EffectAuto ID " + id + ": " + e.getMessage());
                break;
            }
        }

        if (activeSession == null || !activeSession.connected) {
            leakingEffectAuto = false;
            logError("[EffectAuto Leak] Mất kết nối server!");
            SwingUtilities.invokeLater(() -> {
                progressEffectAuto.setString("Mất kết nối (Đã lưu: " + effectAutoSavedCount + ")");
                progressEffectAuto.setForeground(Theme.ERROR);
            });
            checkLeakerState();
            return;
        }

        if (!leakingEffectAuto) {
            log("[EffectAuto Leak] Người dùng đã dừng quá trình kéo effect auto.");
            SwingUtilities.invokeLater(() -> {
                progressEffectAuto.setString("Đã dừng (Đã lưu: " + effectAutoSavedCount + ")");
                progressEffectAuto.setForeground(Theme.WARNING);
            });
            checkLeakerState();
            return;
        }

        log("[EffectAuto Leak] Đã gửi xong toàn bộ yêu cầu. Đang chờ phản hồi dữ liệu từ server...");
        SwingUtilities.invokeLater(() -> {
            progressEffectAuto.setString("Đang nhận dữ liệu... (Đã lưu: " + effectAutoSavedCount + ")");
            progressEffectAuto.setForeground(Theme.ACCENT);
        });

        long waitStart = System.currentTimeMillis();
        int lastSaved = effectAutoSavedCount;
        while (leakingEffectAuto && activeSession != null && activeSession.connected) {
            long now = System.currentTimeMillis();
            if (effectAutoSavedCount != lastSaved) {
                lastSaved = effectAutoSavedCount;
                lastEffectAutoReceivedTime = now;
            }
            if (now - lastEffectAutoReceivedTime > 2500 || now - waitStart > 15000) {
                break;
            }
            try { Thread.sleep(200); } catch (InterruptedException ignored) { break; }
        }

        leakingEffectAuto = false;
        final int finalSaved = effectAutoSavedCount;
        log("[EffectAuto Leak] Hoàn thành kéo effect auto! Đã lưu: " + finalSaved + " file.");
        SwingUtilities.invokeLater(() -> {
            progressEffectAuto.setValue(progressEffectAuto.getMaximum());
            if (finalSaved > 0) {
                progressEffectAuto.setString("Hoàn thành (Đã lưu: " + finalSaved + ")");
                progressEffectAuto.setForeground(Theme.SUCCESS);
            } else {
                progressEffectAuto.setString("Hoàn tất (Server không có effect auto trong dải ID này)");
                progressEffectAuto.setForeground(Theme.TEXT_DIM);
            }
        });
        checkLeakerState();
    }

    private void dataServerLeak() {
        if (activeSession != null) {
            activeSession.dataServerLeak();
        }
    }

    private void fashionLeak() {
        if (activeSession != null) {
            activeSession.fashionLeak();
        } else {
            SwingUtilities.invokeLater(() -> {
                progressFashion.setForeground(Theme.ERROR);
            });
            checkLeakerState();
        }
    }

    private class LeakSession {
        private String host;
        private int port;
        private String username;
        private String password;
        private int zoomLevel;

        private Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;
        private volatile boolean connected = false;
        private volatile boolean isConnecting = false;
        private volatile boolean isLoggedIn = false;
        private volatile boolean isGameEntered = false;

        private byte[] key = null;
        private boolean getKeyComplete = false;
        private byte curR = 0;
        private byte curW = 0;

        public LeakSession(String host, int port, String username, String password, int zoomLevel) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
            this.zoomLevel = zoomLevel;
        }

        private byte readKey(byte b) {
            byte res = (byte) ((key[curR] & 255) ^ (b & 255));
            curR = (byte) ((curR + 1) % key.length);
            return res;
        }

        private byte writeKey(byte b) {
            byte res = (byte) ((key[curW] & 255) ^ (b & 255));
            curW = (byte) ((curW + 1) % key.length);
            return res;
        }

        public void start() {
            isConnecting = true;
            new Thread(() -> {
                try {
                    log("Connecting to " + host + ":" + port + "...");
                    socket = new Socket();
                    socket.connect(new java.net.InetSocketAddress(host, port), 10000);
                    dis = new DataInputStream(socket.getInputStream());
                    dos = new DataOutputStream(socket.getOutputStream());
                    connected = true;
                    log("Connected! Requesting handshake key...");

                    // Send handshake request
                    sendMessage(new Message((byte) -27));

                    // Start reader loop
                    new Thread(this::readLoop).start();

                    // Wait for key exchange
                    long t0 = System.currentTimeMillis();
                    while (connected && !getKeyComplete) {
                        if (System.currentTimeMillis() - t0 > 10000) {
                            throw new IOException("Handshake timeout");
                        }
                        Thread.sleep(10);
                    }

                    if (!connected) return;
                    log("Key handshake succeeded! Connection verified.");

                } catch (Exception e) {
                    logError("Connection failed: " + e.getMessage());
                    disconnect();
                } finally {
                    isConnecting = false;
                    SwingUtilities.invokeLater(() -> {
                        btnConnect.setEnabled(true);
                        updateUIState();
                    });
                }
            }).start();
        }

        public void sendLogin() throws IOException {
            log("Sending login package...");
            Message loginMsg = new Message((byte) -2);
            loginMsg.writer().writeByte(0); // type = login
            loginMsg.writer().writeUTF(username);
            loginMsg.writer().writeUTF(password);
            loginMsg.writer().writeByte((byte) zoomLevel);
            loginMsg.writer().writeUTF("1.2.9");
            loginMsg.writer().writeByte(0);
            loginMsg.writer().writeByte(0); // char select index
            loginMsg.writer().writeUTF("Java");
            loginMsg.writer().writeUTF("checkmodhaitac:android:isolatedSplits=true");
            sendMessage(loginMsg);
        }

        public synchronized void sendMessage(Message m) throws IOException {
            if (dos == null) return;
            byte[] data = m.getData();
            if (getKeyComplete) {
                dos.writeByte(writeKey(m.command));
            } else {
                dos.writeByte(m.command);
            }

            if (data != null) {
                int len = data.length;
                if (getKeyComplete) {
                    dos.writeByte(writeKey((byte) (len >> 8)));
                    dos.writeByte(writeKey((byte) (len & 0xFF)));
                    byte[] enc = new byte[len];
                    for (int i = 0; i < len; i++) {
                        enc[i] = writeKey(data[i]);
                    }
                    dos.write(enc);
                } else {
                    dos.writeShort(len);
                    dos.write(data);
                }
            } else {
                if (getKeyComplete) {
                    dos.writeByte(writeKey((byte) 0));
                    dos.writeByte(writeKey((byte) 0));
                } else {
                    dos.writeShort(0);
                }
            }
            dos.flush();
        }

        public Message readMessage() throws IOException {
            byte command = dis.readByte();
            if (getKeyComplete) {
                command = readKey(command);
            }

            int size = 0;
            if (command == -39 || command == -101 || command == -93 || command == 76) {
                if (getKeyComplete) {
                    byte b1 = readKey(dis.readByte());
                    byte b2 = readKey(dis.readByte());
                    byte b3 = readKey(dis.readByte());
                    byte b4 = readKey(dis.readByte());
                    size = ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
                } else {
                    size = dis.readInt();
                }
            } else {
                if (getKeyComplete) {
                    byte b1 = readKey(dis.readByte());
                    byte b2 = readKey(dis.readByte());
                    size = ((b1 & 0xFF) << 8) | (b2 & 0xFF);
                } else {
                    size = dis.readUnsignedShort();
                }
            }

            if (size < 0 || size > 30_000_000) {
                throw new IOException("Invalid packet size: " + size + " for cmd: " + command);
            }

            byte[] data = new byte[size];
            if (size > 0) {
                dis.readFully(data);
            }

            if (getKeyComplete) {
                for (int i = 0; i < size; i++) {
                    data[i] = readKey(data[i]);
                }
            }

            return new Message(command, data);
        }

        private void readLoop() {
            try {
                while (connected) {
                    Message msg = readMessage();
                    if (msg == null) break;

                    if (msg.command == -27) {
                        byte keyLen = msg.reader().readByte();
                        key = new byte[keyLen];
                        for (int i = 0; i < keyLen; i++) {
                            key[i] = msg.reader().readByte();
                        }
                        for (int i = 0; i < key.length - 1; i++) {
                            key[i + 1] ^= key[i];
                        }
                        getKeyComplete = true;
                        curR = 0;
                        curW = 0;
                        continue;
                    }

                    if (leakingDataServer) {
                        log("[DEBUG] Received cmd: " + msg.command + " (data size: " + (msg.reader() != null ? msg.reader().available() : -1) + ")");
                    }
                    switch (msg.command) {
                        case -2:
                            log("Login OK! Access verified.");
                            isLoggedIn = true;
                            SwingUtilities.invokeLater(() -> updateUIState());
                            break;
                        case -4:
                            byte numChars = msg.reader().readByte();
                            log("Characters list retrieved (" + numChars + " found).");
                            if (numChars > 0) {
                                short charId = msg.reader().readShort();
                                log("Selecting character ID: " + charId + "...");
                                Message sel = new Message((byte) -9);
                                sel.writer().writeShort(charId);
                                sel.writer().writeByte((byte) 1);
                                sel.writer().writeShort(charId);
                                sendMessage(sel);
                            } else {
                                logError("Empty character list! Log in using client and create a character first.");
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(MainUI.this, 
                                        "Empty character list! Log in using client and create a character first.", 
                                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                                });
                                disconnect();
                            }
                            break;
                        case -10:
                            log("Entered game successfully (character info loaded).");
                            isLoggedIn = true;
                            isGameEntered = true;
                            SwingUtilities.invokeLater(() -> updateUIState());
                            break;
                        case 0:
                            handleMapResponse(msg);
                            break;
                        case -51:
                        case -101:
                            handleIconResponse(msg);
                            break;
                        case -82:
                            handlePartResponse(msg);
                            break;
                        case 74:
                        case 76:
                            handleEffectResponse(msg);
                            break;
                        case -44:
                            handleEffectAutoResponse(msg);
                            break;
                        case -19:
                            handleFashionResponse(msg);
                            break;
                        case -41:
                            handleResetValueUpdateImage(msg);
                            break;
                        case -39:
                            handleSaveImageAndroid(msg);
                            break;
                        case -40:
                            handleLoadImageAndroidOk(msg);
                            break;
                        case -11:
                            try {
                                short dialogId = msg.reader().readShort();
                                byte unk2 = msg.reader().readByte();
                                String title = msg.reader().readUTF();
                                String content = msg.reader().readUTF();
                                log("[Server Alert] " + title + ": " + content);
                                if (!isLoggedIn && !isGameEntered) {
                                    logError("Login rejected: " + content);
                                    SwingUtilities.invokeLater(() -> {
                                        JOptionPane.showMessageDialog(MainUI.this, content, title, JOptionPane.ERROR_MESSAGE);
                                    });
                                    disconnect();
                                }
                            } catch (Exception e) {
                                try {
                                    String text = msg.reader().readUTF();
                                    log("[Server Alert] " + text);
                                } catch (Exception ignored) {}
                            }
                            break;
                        case -33:
                            try {
                                String text = msg.reader().readUTF();
                                log("[Server Dialog] " + text);
                                if (!isLoggedIn && !leakingDataServer) {
                                    logError("Login failed: " + text);
                                    SwingUtilities.invokeLater(() -> {
                                        JOptionPane.showMessageDialog(MainUI.this, text, "Login Failed", JOptionPane.ERROR_MESSAGE);
                                    });
                                    disconnect();
                                }
                            } catch (Exception ignored) {}
                            break;
                        case -7:
                            handleItem347Response(msg);
                            break;
                        case -105:
                            handleItem4InfoResponse(msg);
                            break;
                        case -95:
                            handleClanHanhTrinhResponse(msg);
                            break;
                        default:
                            if (leakingDataServer) {
                                handleDataServerResponse(msg);
                            }
                            break;
                    }
                    msg.cleanup();
                }
            } catch (Exception e) {
                if (connected) {
                    String err = (e.getMessage() != null && !e.getMessage().isEmpty()) ? e.getMessage() : e.getClass().getSimpleName();
                    if (e instanceof java.io.EOFException) {
                        err = "Server disconnected (EOF)";
                    }
                    logError("Connection error: " + err);
                    disconnect();
                }
            }
        }

        private void handleIconResponse(Message msg) {
            try {
                short id = msg.reader().readShort();
                int size = msg.reader().available();
                if (size <= 0) return;
                byte[] data = new byte[size];
                msg.reader().readFully(data);

                File file = new File(AppConfig.getPath("Data/LeakRes/res/icon/" + zoomLevel + "/" + id + ".png"));
                file.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(data);
                }

                iconSavedCount++;
                lastIconReceivedTime = System.currentTimeMillis();
                final int count = iconSavedCount;
                log("[Icon Leak] Saved icon " + id + " to " + file.getName() + " (" + size + " bytes, tổng: " + count + ")");
                SwingUtilities.invokeLater(() -> {
                    if (leakingIcon && progressIcon != null) {
                        progressIcon.setString("Đang nhận... | Đã lưu: " + count);
                    }
                });
            } catch (Exception e) {
                logError("Failed to save icon/effect image: " + e.getMessage());
            }
        }

        private void handlePartResponse(Message msg) {
            try {
                short num = msg.reader().readShort();
                byte type = msg.reader().readByte();

                int len = 0;
                switch(type) {
                    case 0: len = 5; break;
                    case 1: len = 20; break;
                    case 2: len = 15; break;
                    case 3: len = 24; break;
                    case 4: len = 2; break;
                    case 5: len = 2; break;
                }

                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < len; i++) {
                    short imgId = msg.reader().readShort();
                    byte dx = msg.reader().readByte();
                    byte dy = msg.reader().readByte();
                    sb.append("[").append(imgId).append(",").append(dx).append(",").append(dy).append("]");
                    if (i < len - 1) sb.append(",");
                }
                sb.append("]");

                String sql = "INSERT INTO `parts` (`id`,`type`,`data`) VALUES (" + num + "," + type + ",'" + sb.toString() + "') "
                           + "ON DUPLICATE KEY UPDATE `type`=VALUES(`type`), `data`=VALUES(`data`);\n";

                File sqlFile = new File(AppConfig.getPath("Data/LeakRes/res/icon/part.sql"));
                sqlFile.getParentFile().mkdirs();
                synchronized (sqlFileLock) {
                    try (FileWriter fw = new FileWriter(sqlFile, true)) {
                        fw.write(sql);
                    }
                }

                partSavedCount++;
                lastPartReceivedTime = System.currentTimeMillis();
                final int count = partSavedCount;
                log("[Part Leak] Saved part " + num + " (Type " + type + ", tổng: " + count + ")");
                SwingUtilities.invokeLater(() -> {
                    if (leakingPart && progressPart != null) {
                        progressPart.setString("Đang nhận... | Đã lưu: " + count);
                    }
                });
            } catch (Exception e) {
                logError("Failed to save part: " + e.getMessage());
            }
        }

        private void handleEffectResponse(Message msg) {
            try {
                byte type = msg.reader().readByte();
                if (type != 0) return;

                int available = msg.reader().available();
                byte[] payload = new byte[available];
                msg.reader().readFully(payload);

                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(payload));
                short effId = dis.readShort();
                short len = dis.readShort();
                if (len <= 0 || effId <= 0) return;

                byte[] raw = new byte[len];
                dis.readFully(raw);
                byte[] img = new byte[dis.available()];
                dis.readFully(img);

                String baseDir = AppConfig.getPath("Data/LeakRes/res/effect/" + zoomLevel + "/");
                new File(baseDir + "data").mkdirs();
                new File(baseDir + "img").mkdirs();
                new File(baseDir + "sql").mkdirs();

                File dataFile = new File(baseDir + "data/" + effId + ".data");
                try (FileOutputStream fos = new FileOutputStream(dataFile)) {
                    fos.write(raw);
                }

                File encFile = new File(baseDir + "data/" + effId + ".enc");
                byte[] enc = new byte[raw.length];
                for (int i = 0; i < raw.length; i++) {
                    enc[i] = (byte) (raw[i] ^ 0x5A);
                }
                try (FileOutputStream fos = new FileOutputStream(encFile)) {
                    fos.write(enc);
                }

                String ext = isPng(img) ? ".png" : ".img";
                File imgFile = new File(baseDir + "img/" + effId + ext);
                try (FileOutputStream fos = new FileOutputStream(imgFile)) {
                    fos.write(img);
                }

                Hashtable parsed = parseRawEffect(raw);
                if (parsed != null) {
                    String sql = buildSqlEffect(effId, parsed, raw);
                    File sqlFile = new File(baseDir + "sql/" + effId + ".sql");
                    try (FileOutputStream fos = new FileOutputStream(sqlFile)) {
                        fos.write(sql.getBytes("UTF-8"));
                    }
                }

                effectSavedCount++;
                lastEffectReceivedTime = System.currentTimeMillis();
                final int count = effectSavedCount;
                log("[Effect Leak] Saved effect " + effId + " (raw: " + len + " bytes, img: " + img.length + " bytes, tổng: " + count + ")");
                SwingUtilities.invokeLater(() -> {
                    if (leakingEffect && progressEffect != null) {
                        progressEffect.setString("Đang nhận... | Đã lưu: " + count);
                    }
                });
            } catch (Exception e) {
                logError("Failed to save effect: " + e.getMessage());
            }
        }

        private void handleEffectAutoResponse(Message msg) {
            try {
                int available = msg.reader().available();
                byte[] dataeff = new byte[available];
                msg.reader().readFully(dataeff);

                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(dataeff));
                short id = dis.readShort();
                short dataLen = dis.readShort();

                byte[] binData = new byte[dataLen];
                dis.readFully(binData);
                byte[] imgData = new byte[dis.available()];
                dis.readFully(imgData);

                String baseDir = AppConfig.getPath("Data/LeakRes/res/effect_auto/x" + zoomLevel + "/");
                new File(baseDir + "data").mkdirs();
                new File(baseDir + "img").mkdirs();
                new File(baseDir + "sql").mkdirs();

                File binFile = new File(baseDir + "data/" + id + ".bin");
                try (FileOutputStream fos = new FileOutputStream(binFile)) {
                    fos.write(binData);
                }

                File imgFile = new File(baseDir + "img/" + id + ".png");
                try (FileOutputStream fos = new FileOutputStream(imgFile)) {
                    fos.write(imgData);
                }

                DataInputStream binDis = new DataInputStream(new ByteArrayInputStream(binData));
                LocalMainEffectAuto eff = new LocalMainEffectAuto(binDis);

                String jsonHashImage = buildJsonHashImage(eff.hashImage);
                String jsonMFrame = buildJsonMFrame(eff.mFrame);
                String jsonMRunFrame = buildJsonArrayShort(eff.mRunFrame);

                String sql = "INSERT INTO effect_auto_data(eff_id, hashImage, mFrame, mRunFrame, typeEffect, valueEffect) VALUES("
                        + id + ", '" + jsonHashImage + "', '" + jsonMFrame + "', '" + jsonMRunFrame + "', 0, 1) "
                        + "ON DUPLICATE KEY UPDATE hashImage=VALUES(hashImage), mFrame=VALUES(mFrame), mRunFrame=VALUES(mRunFrame);\n";

                File sqlFile = new File(baseDir + "sql/" + id + ".sql");
                try (FileOutputStream fos = new FileOutputStream(sqlFile)) {
                    fos.write(sql.getBytes("UTF-8"));
                }

                effectAutoSavedCount++;
                lastEffectAutoReceivedTime = System.currentTimeMillis();
                final int count = effectAutoSavedCount;
                log("[EffectAuto Leak] Saved effectauto " + id + " (raw: " + dataLen + " bytes, img: " + imgData.length + " bytes, tổng: " + count + ")");
                SwingUtilities.invokeLater(() -> {
                    if (leakingEffectAuto && progressEffectAuto != null) {
                        progressEffectAuto.setString("Đang nhận... | Đã lưu: " + count);
                    }
                });
            } catch (Exception e) {
                logError("Failed to save effectauto: " + e.getMessage());
            }
        }

        private void handleFashionResponse(Message msg) {
            try {
                byte cat = msg.reader().readByte();
                String title = msg.reader().readUTF();
                byte cat2 = msg.reader().readByte();
                short size = msg.reader().readShort();

                if (cat != 105) {
                    log("[Fashion Leak] Received unknown shop cat: " + cat);
                    return;
                }

                log("[Fashion Leak] Received " + size + " fashion templates from server.");

                File sqlFile = new File(AppConfig.getPath("Data/LeakRes/res/icon/fashion.sql"));
                sqlFile.getParentFile().mkdirs();

                // Delete old sql file if exists to overwrite with fresh data
                if (sqlFile.exists()) {
                    sqlFile.delete();
                }

                StringBuilder sqlBuilder = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    short id = msg.reader().readShort(); // Assuming version >= 129
                    String name = msg.reader().readUTF();
                    byte isUse = msg.reader().readByte();
                    short idIcon = msg.reader().readShort();
                    short point = msg.reader().readShort();
                    int price = msg.reader().readInt();
                    short price2 = msg.reader().readShort();
                    byte level = msg.reader().readByte();
                    byte wearLen = msg.reader().readByte();
                    short[] wear = new short[wearLen];
                    StringBuilder wearJson = new StringBuilder("[");
                    for (int j = 0; j < wearLen; j++) {
                        wear[j] = msg.reader().readShort();
                        wearJson.append(wear[j]);
                        if (j < wearLen - 1) {
                            wearJson.append(",");
                        }
                    }
                    wearJson.append("]");

                    // Format SQL matching fashiontemplate table
                    String sql = "INSERT INTO `fashiontemplate` (`id`, `icon`, `name`, `info`, `mwear`, `op`, `price`, `hsd`) VALUES ("
                               + id + ", " + idIcon + ", '" + name.replace("'", "''") + "', '', '" 
                               + wearJson.toString() + "', '[]', " + price + ", -1) "
                               + "ON DUPLICATE KEY UPDATE `icon`=VALUES(`icon`), `name`=VALUES(`name`), `info`=VALUES(`info`), `mwear`=VALUES(`mwear`), `price`=VALUES(`price`), `hsd`=VALUES(`hsd`);\n";
                    sqlBuilder.append(sql);
                }

                synchronized (sqlFileLock) {
                    try (FileWriter fw = new FileWriter(sqlFile, true)) {
                        fw.write(sqlBuilder.toString());
                    }
                }

                log("[Fashion Leak] Saved " + size + " fashion entries to " + sqlFile.getName());
                
                SwingUtilities.invokeLater(() -> {
                    progressFashion.setValue(100);
                    progressFashion.setString("Completed (" + size + ")");
                    progressFashion.setForeground(Theme.SUCCESS);
                });
            } catch (Exception e) {
                logError("Failed to parse/save fashion templates: " + e.getMessage());
                SwingUtilities.invokeLater(() -> {
                    progressFashion.setValue(0);
                    progressFashion.setString("Error");
                    progressFashion.setForeground(Theme.ERROR);
                });
            } finally {
                leakingFashion = false;
                checkLeakerState();
            }
        }

        private void handleClanHanhTrinhResponse(Message msg) {
            try {
                byte act = msg.reader().readByte();
                if (act == 99) {
                    short size = msg.reader().readShort();
                    log("[Clan Journey Leak] Received " + size + " templates from custom server.");

                    File sqlFile = new File(AppConfig.getPath("Data/LeakRes/res/icon/clan_hanhtrinh.sql"));
                    sqlFile.getParentFile().mkdirs();
                    if (sqlFile.exists()) {
                        sqlFile.delete();
                    }

                    StringBuilder sqlBuilder = new StringBuilder();
                    for (int i = 0; i < size; i++) {
                        short id = msg.reader().readShort();
                        short icon = msg.reader().readShort();
                        String name = msg.reader().readUTF();
                        String info = msg.reader().readUTF();
                        int rd = msg.reader().readInt();
                        
                        byte opSize = msg.reader().readByte();
                        StringBuilder opJson = new StringBuilder("[");
                        for (int j = 0; j < opSize; j++) {
                            byte opId = msg.reader().readByte();
                            int opParam = msg.reader().readInt();
                            opJson.append("[").append(opId).append(",").append(opParam).append("]");
                            if (j < opSize - 1) {
                                opJson.append(",");
                            }
                        }
                        opJson.append("]");

                        String sql = String.format(
                            "INSERT INTO `clan_hanhtrinh_template` (`id`, `icon`, `name`, `info`, `op`, `rd`) " +
                            "VALUES (%d, %d, '%s', '%s', '%s', %d) " +
                            "ON DUPLICATE KEY UPDATE `icon`=VALUES(`icon`), `name`=VALUES(`name`), `info`=VALUES(`info`), `op`=VALUES(`op`), `rd`=VALUES(`rd`);\n",
                            id, icon, name.replace("'", "''"), info.replace("'", "''"), opJson.toString(), rd
                        );
                        sqlBuilder.append(sql);
                    }

                    synchronized (sqlFileLock) {
                        try (FileWriter fw = new FileWriter(sqlFile, true)) {
                            fw.write(sqlBuilder.toString());
                        }
                    }
                    log("[Clan Journey Leak] Saved " + size + " entries to clan_hanhtrinh.sql");

                    SwingUtilities.invokeLater(() -> {
                        progressClanHT.setValue(100);
                        progressClanHT.setString("Completed (" + size + ")");
                        progressClanHT.setForeground(Theme.SUCCESS);
                    });
                } else if (act == 0) {
                    String shopTitle = msg.reader().readUTF();
                    short size = msg.reader().readShort();
                    log("[Clan Journey Leak] Received " + size + " templates from standard shop (" + shopTitle + ").");

                    File sqlFile = new File(AppConfig.getPath("Data/LeakRes/res/icon/clan_hanhtrinh.sql"));
                    sqlFile.getParentFile().mkdirs();
                    if (sqlFile.exists()) {
                        sqlFile.delete();
                    }

                    StringBuilder sqlBuilder = new StringBuilder();
                    for (int i = 0; i < size; i++) {
                        short id = msg.reader().readShort();
                        String name = msg.reader().readUTF();
                        String info = msg.reader().readUTF();
                        short icon = msg.reader().readShort();
                        String extraInfo = msg.reader().readUTF();
                        
                        byte opSize = msg.reader().readByte();
                        StringBuilder opJson = new StringBuilder("[");
                        for (int j = 0; j < opSize; j++) {
                            byte opId = msg.reader().readByte();
                            short opParam = msg.reader().readShort();
                            opJson.append("[").append(opId).append(",").append(opParam).append("]");
                            if (j < opSize - 1) {
                                opJson.append(",");
                            }
                        }
                        opJson.append("]");

                        String sql = String.format(
                            "INSERT INTO `clan_hanhtrinh_template` (`id`, `icon`, `name`, `info`, `op`, `rd`) " +
                            "VALUES (%d, %d, '%s', '%s', '%s', %d) " +
                            "ON DUPLICATE KEY UPDATE `icon`=VALUES(`icon`), `name`=VALUES(`name`), `info`=VALUES(`info`), `op`=VALUES(`op`), `rd`=VALUES(`rd`);\n",
                            id, icon, name.replace("'", "''"), info.replace("'", "''"), opJson.toString(), 0
                        );
                        sqlBuilder.append(sql);
                    }

                    synchronized (sqlFileLock) {
                        try (FileWriter fw = new FileWriter(sqlFile, true)) {
                            fw.write(sqlBuilder.toString());
                        }
                    }
                    log("[Clan Journey Leak] Saved " + size + " entries to clan_hanhtrinh.sql");

                    SwingUtilities.invokeLater(() -> {
                        progressClanHT.setValue(100);
                        progressClanHT.setString("Completed (" + size + ")");
                        progressClanHT.setForeground(Theme.SUCCESS);
                    });
                }
            } catch (Exception e) {
                logError("Failed to parse/save clan journey templates: " + e.getMessage());
                SwingUtilities.invokeLater(() -> {
                    progressClanHT.setValue(0);
                    progressClanHT.setString("Error");
                    progressClanHT.setForeground(Theme.ERROR);
                });
            } finally {
                leakingClanHT = false;
                checkLeakerState();
            }
        }

        private void handleDataServerResponse(Message msg) {
            try {
                int available = msg.reader().available();
                if (available <= 0) return;
                byte[] data = new byte[available];
                msg.reader().readFully(data);

                File file = new File(AppConfig.getPath("Data/LeakRes/res/datafromserver/x" + zoomLevel + "/cmd_" + msg.command + ".data"));
                file.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(data);
                }
                log("[DataServer Leak] Saved msg cmd " + msg.command + " to " + file.getName());
            } catch (Exception e) {
                logError("Failed to save DataServer data: " + e.getMessage());
            }
        }

        private void handleMapResponse(Message msg) {
            try {
                DataInputStream dis = msg.reader();
                short id = dis.readShort();
                byte zone_id = dis.readByte();
                byte typeViewPlayer = dis.readByte();
                short px = dis.readShort();
                short py = dis.readShort();
                int maxHp = dis.readInt();
                int curHp = dis.readInt();
                int maxMp = dis.readInt();
                int curMp = dis.readInt();
                byte b = dis.readByte();
                byte specMap = dis.readByte();

                if (b != 1) {
                    log("[Map Leak] Map ID " + id + " has no full data (b == 0), skipping.");
                    return;
                }

                // Read dataMap (tiles)
                int l0 = dis.readInt();
                byte[] dataMap = new byte[l0];
                dis.readFully(dataMap);

                // Read dataItem (static items)
                int l1 = dis.readInt();
                byte[] dataItem = new byte[l1];
                dis.readFully(dataItem);

                // Read vgos - read as unsigned to support maps with >= 128 portals
                int vgosCount = dis.readByte() & 0xFF;
                List<GameMap.Vgo> vgos = new ArrayList<>();
                for (int i = 0; i < vgosCount; i++) {
                    String mapGoName = dis.readUTF();
                    short xold = dis.readShort();
                    short yold = dis.readShort();
                    
                    GameMap.Vgo vgo = new GameMap.Vgo();
                    vgo.xold = xold;
                    vgo.yold = yold;
                    if (mapGoName.contains("|")) {
                        try {
                            String[] parts = mapGoName.split("\\|");
                            vgo.id_map_go = Short.parseShort(parts[1]);
                            vgo.xnew = Short.parseShort(parts[2]);
                            vgo.ynew = Short.parseShort(parts[3]);
                        } catch (Exception e) {
                            vgo.id_map_go = -1;
                            vgo.xnew = xold;
                            vgo.ynew = yold;
                        }
                    } else {
                        vgo.id_map_go = -1;
                        vgo.xnew = xold;
                        vgo.ynew = yold;
                    }
                    vgos.add(vgo);
                }

                // Read background & eff
                byte IDBack = dis.readByte();
                short HBack = dis.readShort();
                byte id_eff_map = dis.readByte();
                byte level = dis.readByte();
                byte typeChangeMap = dis.readByte();

                // Read mPosMapTrain if specMap == 3
                byte[][] mPosMapTrain = null;
                String strTimeChange = "";
                if (specMap == 3) {
                    byte cnt = dis.readByte();
                    mPosMapTrain = new byte[cnt][2];
                    for (int i = 0; i < cnt; i++) {
                        mPosMapTrain[i][0] = dis.readByte();
                        mPosMapTrain[i][1] = dis.readByte();
                    }
                    strTimeChange = dis.readUTF();
                }

                // Read name
                String name = dis.readUTF();

                // Build GameMap object
                GameMap map = new GameMap();
                map.id = id;
                map.name = name;
                map.max_zone = 20; // default
                map.max_player = 15; // default
                map.type_view_p = typeViewPlayer;
                map.isOnlinemap = 1;
                map.specMap = specMap;
                map.id_eff_map = id_eff_map;
                map.level = level;
                map.typeChangeMap = typeChangeMap;
                map.strTimeChange = strTimeChange;
                map.IDBack = IDBack;
                map.HBack = HBack;
                map.vgos = vgos;

                // Save map raw file
                File mapFile = new File(AppConfig.getPath("Data/LeakRes/res/map/" + id + ".map"));
                mapFile.getParentFile().mkdirs();
                try (DataOutputStream mapDos = new DataOutputStream(new FileOutputStream(mapFile))) {
                    mapDos.writeShort(id);
                    mapDos.writeByte(specMap);
                    mapDos.writeInt(dataMap.length);
                    mapDos.write(dataMap);
                    mapDos.writeInt(dataItem.length);
                    mapDos.write(dataItem);
                    mapDos.writeByte((byte) vgos.size());
                    for (GameMap.Vgo vgo : vgos) {
                        mapDos.writeUTF(vgo.id_map_go >= 0 ? ("|" + vgo.id_map_go + "|" + vgo.xnew + "|" + vgo.ynew) : "");
                        mapDos.writeShort(vgo.xold);
                        mapDos.writeShort(vgo.yold);
                    }
                    mapDos.writeByte(IDBack);
                    mapDos.writeShort(HBack);
                    mapDos.writeByte(id_eff_map);
                    mapDos.writeByte(level);
                    mapDos.writeByte(typeChangeMap);
                    if (specMap == 3 && mPosMapTrain != null) {
                        mapDos.writeByte((byte) mPosMapTrain.length);
                        for (byte[] p : mPosMapTrain) {
                            mapDos.writeByte(p[0]);
                            mapDos.writeByte(p[1]);
                        }
                        mapDos.writeUTF(strTimeChange);
                    }
                    mapDos.writeUTF(name);
                }

                // Save SQL
                String sql = String.format(
                    "INSERT INTO `map` (`id`, `name`, `dataMap`, `dataItem`, `specMap`, `id_eff_map`, `level`, `typeChangeMap`, `IDBack`, `HBack`) " +
                    "VALUES (%d, '%s', '%s', '%s', %d, %d, %d, %d, %d, %d) " +
                    "ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `specMap`=VALUES(`specMap`), `IDBack`=VALUES(`IDBack`), `HBack`=VALUES(`HBack`);\n",
                    id, name.replace("'", "''"), bytesToHex(dataMap), bytesToHex(dataItem), specMap, id_eff_map, level, typeChangeMap, IDBack, HBack
                );

                File sqlFile = new File(AppConfig.getPath("Data/LeakRes/res/map/map.sql"));
                sqlFile.getParentFile().mkdirs();
                synchronized (sqlFileLock) {
                    try (FileWriter fw = new FileWriter(sqlFile, true)) {
                        fw.write(sql);
                    }
                }

                mapSavedCount++;
                lastMapReceivedTime = System.currentTimeMillis();
                final int count = mapSavedCount;
                log("[Map Leak] Saved map " + id + " - " + name + " (total: " + count + ")");
                SwingUtilities.invokeLater(() -> {
                    if (leakingMap && progressMap != null) {
                        progressMap.setString("Đang nhận... | Đã lưu: " + count);
                    }
                });
            } catch (Exception e) {
                logError("Failed to parse map: " + e.getMessage());
            }
        }

        public void fashionLeak() {
            try {
                Message m = new Message((byte) -19);
                m.writer().writeByte((byte) 105);
                sendMessage(m);
            } catch (Exception e) {
                logError("Fashion leak request failed: " + e.getMessage());
            }
        }

        public void clanHTLeak() {
            try {
                Message m = new Message((byte) -95);
                m.writer().writeByte((byte) 99);
                sendMessage(m);
            } catch (Exception e) {
                logError("Clan HT leak request failed: " + e.getMessage());
            }
        }

        public void item347Leak() {
            try {
                Message m = new Message((byte) -7);
                sendMessage(m);
            } catch (Exception e) {
                logError("Item 347 leak request failed: " + e.getMessage());
            }
        }

        public void dataServerLeak() {
            try {
                Message m = new Message((byte) -38);
                m.writer().writeByte((byte) zoomLevel);
                sendMessage(m);
            } catch (Exception e) {
                logError("DataServer leak request failed: " + e.getMessage());
            }
        }

        private void handleResetValueUpdateImage(Message msg) {
            try {
                String text = msg.reader().readUTF();
                short max = msg.reader().readShort();
                dataServerCategory = text;
                dataServerTotal = max;
                dataServerCurrent = 0;
                log("[DataServer Leak] Nhóm dữ liệu: " + text + " (" + max + " files)...");
                SwingUtilities.invokeLater(() -> {
                    progressDataServer.setMinimum(0);
                    progressDataServer.setMaximum(Math.max(1, max));
                    progressDataServer.setValue(0);
                    progressDataServer.setString(text + " (0/" + max + ")");
                });
            } catch (Exception e) {
                logError("Failed to reset value update image: " + e.getMessage());
            }
        }

        private void handleSaveImageAndroid(Message msg) {
            try {
                String name = msg.reader().readUTF();
                int available = msg.reader().available();
                byte[] data = new byte[available];
                msg.reader().readFully(data);

                File file = new File(AppConfig.getPath("Data/LeakRes/res/datafromserver/x" + zoomLevel + "/" + name));
                file.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(data);
                }

                dataServerCurrent++;
                final int cur = dataServerCurrent;
                final int total = dataServerTotal;
                SwingUtilities.invokeLater(() -> {
                    if (progressDataServer != null) {
                        progressDataServer.setValue(cur);
                        progressDataServer.setString(dataServerCategory + " (" + cur + "/" + total + ")");
                    }
                });
            } catch (Exception e) {
                logError("Failed to save image Android: " + e.getMessage());
            }
        }

        private void handleLoadImageAndroidOk(Message msg) {
            log("[DataServer Leak] Hoàn tất tải nhóm dữ liệu DataServer cho zoom " + zoomLevel + "!");
            SwingUtilities.invokeLater(() -> {
                progressDataServer.setValue(progressDataServer.getMaximum());
                progressDataServer.setString("Hoàn thành (" + dataServerCurrent + " files)");
                progressDataServer.setForeground(Theme.SUCCESS);
            });
            leakingDataServer = false;
            checkLeakerState();
        }

        private void handleItem347Response(Message msg) {
            try {
                short id = msg.reader().readShort();
                String name = msg.reader().readUTF();
                log("[Item 347 Leak] Nhận template item 347 id: " + id + " - " + name);
                SwingUtilities.invokeLater(() -> {
                    if (progressItem347 != null) {
                        progressItem347.setString("ID: " + id + " (" + name + ")");
                        progressItem347.setForeground(Theme.ACCENT);
                    }
                });
            } catch (Exception e) {
                logError("Failed to parse item 347: " + e.getMessage());
            }
        }

        private void handleItem4InfoResponse(Message msg) {
            try {
                short id = msg.reader().readShort();
                String name = msg.reader().readUTF();
                log("[Item 4 Info Leak] Nhận template item 4 id: " + id + " - " + name);
            } catch (Exception e) {
                logError("Failed to parse item 4 info: " + e.getMessage());
            }
        }

        private boolean isPng(byte[] data) {
            if (data == null || data.length < 8) return false;
            return (data[0] & 0xFF) == 0x89 && data[1] == 'P' && data[2] == 'N' && data[3] == 'G';
        }

        private Hashtable parseRawEffect(byte[] raw) {
            try {
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(raw));
                Hashtable ht = new Hashtable();
                int numImg = dis.readByte() & 0xFF;
                int[][] imgs = new int[numImg][4];
                for (int i = 0; i < numImg; i++) {
                    dis.readByte(); // id
                    imgs[i][0] = dis.readUnsignedByte();
                    imgs[i][1] = dis.readUnsignedByte();
                    imgs[i][2] = dis.readUnsignedByte();
                    imgs[i][3] = dis.readUnsignedByte();
                }
                ht.put("imgs", imgs);

                int numFrames = dis.readShort();
                int[][][] frames = new int[numFrames][][];
                for (int j = 0; j < numFrames; j++) {
                    int numParts = dis.readByte() & 0xFF;
                    frames[j] = new int[numParts][3];
                    for (int k = 0; k < numParts; k++) {
                        frames[j][k][0] = dis.readShort();
                        frames[j][k][1] = dis.readShort();
                        frames[j][k][2] = dis.readByte() & 0xFF;
                    }
                }
                ht.put("frames", frames);

                int seqLen = dis.readShort();
                short[] seq = new short[seqLen];
                for (int s = 0; s < seqLen; s++) {
                    seq[s] = (short) (dis.readByte() & 0xFF);
                }
                ht.put("seq", seq);
                return ht;
            } catch (Exception e) {
                return null;
            }
        }

        private String buildSqlEffect(int effId, Hashtable parsed, byte[] raw) {
            int[][] imgs = (int[][]) parsed.get("imgs");
            int[][][] frames = (int[][][]) parsed.get("frames");
            short[] seq = (short[]) parsed.get("seq");
            String sImgs = buildJsonHashImage(imgs);
            String sFrames = buildJsonMFrame(frames);
            String sSeq = buildJsonArrayShort(seq);
            return "INSERT INTO `effect` (`id`, `data`, `hashImage`, `mFrame`, `mRunFrame`) VALUES ("
                    + effId + ", '" + bytesToHex(raw) + "', '" + sImgs + "', '" + sFrames + "', '" + sSeq + "') "
                    + "ON DUPLICATE KEY UPDATE `data`=VALUES(`data`), `hashImage`=VALUES(`hashImage`), `mFrame`=VALUES(`mFrame`), `mRunFrame`=VALUES(`mRunFrame`);\n";
        }

        private String buildJsonHashImage(int[][] hashImage) {
            if (hashImage == null) return "[]";
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < hashImage.length; i++) {
                sb.append("[").append(hashImage[i][0]).append(",").append(hashImage[i][1]).append(",")
                  .append(hashImage[i][2]).append(",").append(hashImage[i][3]).append("]");
                if (i < hashImage.length - 1) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }

        private String buildJsonMFrame(int[][][] mFrame) {
            if (mFrame == null) return "[]";
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < mFrame.length; i++) {
                sb.append("[");
                for (int j = 0; j < mFrame[i].length; j++) {
                    sb.append("[").append(mFrame[i][j][0]).append(",").append(mFrame[i][j][1]).append(",")
                      .append(mFrame[i][j][2]).append("]");
                    if (j < mFrame[i].length - 1) sb.append(",");
                }
                sb.append("]");
                if (i < mFrame.length - 1) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }

        private String buildJsonArrayShort(short[] arr) {
            if (arr == null) return "[]";
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i]);
                if (i < arr.length - 1) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }

        private String bytesToHex(byte[] bytes) {
            if (bytes == null) return "";
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }

        public void disconnect() {
            connected = false;
            getKeyComplete = false;
            isLoggedIn = false;
            isGameEntered = false;
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (Exception ignored) {}
            try {
                if (dis != null) dis.close();
            } catch (Exception ignored) {}
            try {
                if (dos != null) dos.close();
            } catch (Exception ignored) {}
            log("Disconnected from server.");
            SwingUtilities.invokeLater(() -> updateUIState());
        }

        private static class LocalMainEffectAuto {
            public int[][] hashImage;
            public int[][][] mFrame;
            public short[] mRunFrame;

            public LocalMainEffectAuto(DataInputStream dis) {
                try {
                    int numImages = dis.readByte() & 0xFF;
                    hashImage = new int[numImages][4];
                    for (int i = 0; i < numImages; i++) {
                        dis.readUnsignedByte(); // id
                        hashImage[i][0] = dis.readUnsignedByte(); // x
                        hashImage[i][1] = dis.readUnsignedByte(); // y
                        hashImage[i][2] = dis.readUnsignedByte(); // w
                        hashImage[i][3] = dis.readUnsignedByte(); // h
                    }
                    int numFrames = dis.readShort();
                    mFrame = new int[numFrames][][];
                    for (int j = 0; j < numFrames; j++) {
                        int numParts = dis.readByte() & 0xFF;
                        mFrame[j] = new int[numParts][3];
                        for (int k = 0; k < numParts; k++) {
                            mFrame[j][k][0] = dis.readShort(); // dx
                            mFrame[j][k][1] = dis.readShort(); // dy
                            mFrame[j][k][2] = dis.readByte() & 0xFF; // idSmall
                        }
                    }
                    int seqLen = dis.readShort();
                    mRunFrame = new short[seqLen];
                    for (int s = 0; s < seqLen; s++) {
                        mRunFrame[s] = (short) (dis.readByte() & 0xFF);
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    private static class ServerEntry {
        public String name;
        public String host;
        public int port;
        public int type;
        public int zoomIndex;

        public ServerEntry(String name, String host, int port, int type, int zoomIndex) {
            this.name = name;
            this.host = host;
            this.port = port;
            this.type = type;
            this.zoomIndex = zoomIndex;
        }

        public String toLine() {
            return name + ":" + host + ":" + port + ":" + type + ":" + zoomIndex;
        }

        @Override
        public String toString() {
            return name + " (" + host + ":" + port + ")";
        }
    }

    public static class Message {
        public byte command;
        private ByteArrayOutputStream os;
        private DataOutputStream dos;
        private ByteArrayInputStream is;
        private DataInputStream dis;

        public Message(byte command) {
            this.command = command;
            os = new ByteArrayOutputStream();
            dos = new DataOutputStream(os);
        }

        public Message(byte command, byte[] data) {
            this.command = command;
            if (data != null) {
                is = new ByteArrayInputStream(data);
                dis = new DataInputStream(is);
            }
        }

        public DataOutputStream writer() {
            return dos;
        }

        public DataInputStream reader() {
            return dis;
        }

        public byte[] getData() {
            if (os != null) {
                return os.toByteArray();
            }
            return new byte[0];
        }

        public void cleanup() {
            try {
                if (os != null) os.close();
                if (dos != null) dos.close();
                if (is != null) is.close();
                if (dis != null) dis.close();
            } catch (Exception ignored) {}
        }
    }
}
