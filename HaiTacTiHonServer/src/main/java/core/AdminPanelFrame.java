package core;

import core.PanelManager.*;
import template.Option;
import template.GiftBox;
import template.ItemOptionTemplate;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * AdminPanelFrame - High-End Dark High-Contrast Java Swing GUI cho Admin Panel Game Server HTTH.
 *
 * Tối ưu hóa toàn diện với 8 Phân Hệ Quản Trị Chuyên Nghiệp:
 * 1. Dashboard & Bảo Trì Hệ Thống (Server Monitor, 1-Click Maintenance, Countdown, Kick All, Save DB, Reload, Test Mode).
 * 2. Lệnh GM Console & Broadcast (Console Live Logger, Command Parser, Loa Thế Giới & Popup Broadcast).
 * 3. Quản Lý & Buff Người Chơi (Online/Offline Inspector, Soi Túi/Rương/Trang bị/Pet/Thuyền, 1-Click Buff, Mute, Ban, Cứu kẹt, Đổi Pass).
 * 4. Mở / Đóng Phó Bản & Sự Kiện (10 Timed Dungeons & 17 Season Events Controller, Force Start/End, Cycle Adjuster).
 * 5. Quản Lý Giftcode (CRUD Mã Quà Tặng, Visual Item Selector, Reset Lượt Nhập).
 * 6. Gửi Thư & Quà Mail (Gửi Cá Nhân, Toàn Bộ Online, Toàn Bộ Server DB, Visual Gift Builder).
 * 7. Chỉnh Sửa Chỉ Số Item 3, Pet & Ép Đồ Custom (Item3 & Pet Template Realtime DB & RAM Editor, Custom God Item Generator).
 * 8. Tra Cứu Nhật Ký Lịch Sử SQL Historys (Multi-filter, Pagination, JSON Preview).
 */
public class AdminPanelFrame extends JFrame {

    // Palette Màu Sắc High-Contrast Dark Theme
    public static final Color COLOR_BG = new Color(15, 23, 42);          // #0F172A Slate Dark Base
    public static final Color COLOR_PANEL = new Color(30, 41, 59);        // #1E293B Container
    public static final Color COLOR_CARD = new Color(51, 65, 85);          // #334155 Card Border/Header
    public static final Color COLOR_TEXT = new Color(255, 255, 255);       // #FFFFFF Trắng Tinh
    public static final Color COLOR_TEXT_MUTED = new Color(203, 213, 225); // #CBD5E1 Xám Sáng
    public static final Color COLOR_ACCENT = new Color(56, 189, 248);      // #38BDF8 Xanh Cyan
    public static final Color COLOR_GREEN = new Color(74, 222, 128);       // #4ADE80 Xanh Lá Sáng
    public static final Color COLOR_RED = new Color(248, 113, 113);        // #F87171 Đỏ
    public static final Color COLOR_ORANGE = new Color(251, 146, 60);      // #FB923C Cam
    public static final Color COLOR_YELLOW = new Color(253, 224, 71);      // #FDE047 Vàng Sáng
    public static final Color COLOR_INPUT_BG = new Color(15, 23, 42);     // #0F172A Nền Nhập Liệu
    public static final Color COLOR_PURPLE = new Color(192, 132, 252);     // #C084FC Tím

    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 13);
    public static final Font FONT_MONO_BOLD = new Font("Consolas", Font.BOLD, 13);

    // Auto Refresh
    private JCheckBox chkAutoRefresh;
    private javax.swing.Timer timerAutoRefresh;
    private JLabel lblServerStatusBadge;

    // Tab 1 Dashboard
    private JLabel lblRealPlayers;
    private JLabel lblBotsCount;
    private JLabel lblSessionsCount;
    private JLabel lblRamUsage;
    private JLabel lblActiveDungeons;
    private JLabel lblActiveEvents;
    private JLabel lblThreadsCount;
    private JLabel lblMaintenanceCountdown;
    private JCheckBox chkAdminTestMode;
    private JTextField txtMaintSeconds;
    private JTextField txtMaintReason;

    // Tab 2 Console
    private JTextArea txtConsoleLog;
    private JTextField txtCommandInput;
    private JTextField txtWorldChatMsg;
    private JComboBox<String> cbWorldChatColor;
    private JTextField txtPopupMsg;

    // Tab 3 Players
    private JTextField txtPlayerSearch;
    private DefaultTableModel modelPlayers;
    private JTable tblPlayers;
    private JLabel lblSelectedPlayerInfo;
    private String currentSelectedPlayer = "";
    private JTextField txtBuffAmount;
    private JTabbedPane tabPlayerDetails;
    private DefaultTableModel modelEquipItems;
    private DefaultTableModel modelBagItem3;
    private DefaultTableModel modelBoxItem3;
    private DefaultTableModel modelBagSimple;
    private DefaultTableModel modelBoxSimple;
    private DefaultTableModel modelPlayerPets;
    private DefaultTableModel modelPlayerBoats;

    // Tab 4 Dungeons & Events
    private DefaultTableModel modelDungeons;
    private JTable tblDungeons;
    private DefaultTableModel modelEvents;
    private JTable tblEvents;
    private JCheckBox chkMasterDungeon;

    // Tab 5 Giftcode
    private JTextField txtGiftSearch;
    private DefaultTableModel modelGiftcodes;
    private JTable tblGiftcodes;

    // Tab 6 Mail
    private JRadioButton rbMailSingle;
    private JRadioButton rbMailOnline;
    private JRadioButton rbMailServerAll;
    private JTextField txtMailTarget;
    private JTextField txtMailSender;
    private JTextField txtMailTitle;
    private JTextArea txtMailContent;
    private JTextField txtMailExpireDays;
    private DefaultTableModel modelMailGifts;
    private JTable tblMailGifts;
    private List<GiftBox> currentMailGiftList = new ArrayList<>();

    // Tab 7 Item3 & Pet Stat Customizer
    private JTextField txtSearchItem3;
    private DefaultTableModel modelItem3Templates;
    private JTable tblItem3Templates;
    private JTextField txtItem3Id, txtItem3Name, txtItem3Class, txtItem3Type, txtItem3Icon, txtItem3Level, txtItem3Color, txtItem3Beri, txtItem3Lock, txtItem3Holes, txtItem3HoanMy, txtItem3Part, txtItem3LoKham;
    private JTextArea txtItem3Op1, txtItem3Op2, txtItem3Mdakham;

    private JTextField txtSearchPet;
    private DefaultTableModel modelPetTemplates;
    private JTable tblPetTemplates;
    private JTextField txtPetId, txtPetName, txtPetType, txtPetIcon, txtPetFrame;
    private JTextArea txtPetOptions;

    // Custom Item Builder
    private JTextField txtCustomItemTargetPlayer;
    private JTextField txtCustomItemTemplateId;
    private JComboBox<String> cbCustomItemColor;
    private JComboBox<String> cbCustomItemLevelUp;
    private JCheckBox chkCustomItemHoanMy;
    private JComboBox<String> cbCustomItemKichAn;
    private JTextArea txtCustomItemOptions;

    // Tab 8 History SQL
    private JTextField txtHistAccId;
    private JTextField txtHistPlayerId;
    private JComboBox<String> cbHistType;
    private JTextField txtHistKeyword;
    private JTextArea txtHistDetail;
    private DefaultTableModel modelHistory;
    private JTable tblHistory;
    private int historyCurrentOffset = 0;
    private static final int HISTORY_LIMIT = 50;

    // Tab 9 Boss & Đệ Hoang & Siêu Trùm
    private DefaultTableModel modelBossAndWild;
    private JTable tblBossAndWild;
    private JLabel lblStatSuperBossLive;
    private JLabel lblStatWorldBossLive;
    private JLabel lblStatWildDeTuLive;
    private JLabel lblStatTotalEntities;
    private JComboBox<String> cbBossCategoryFilter;
    private JComboBox<String> cbBossStatusFilter;
    private JTextField txtBossSearch;
    private List<boss.BossAndWildManager.TrackedEntityDTO> currentTrackedEntities = new ArrayList<>();

    private JComboBox<String> cbRandomSpawnType;
    private JTextField txtRandomSpawnCount;
    private JCheckBox chkNoDupMap;
    private JCheckBox chkNoDupZone;
    private JCheckBox chkRandomKtg;

    private JComboBox<String> cbSpecificEntity;
    private JTextField txtCustomMapId;
    private JTextField txtCustomZoneId;
    private JTextField txtCustomCount;
    private JCheckBox chkCustomAllZones;
    private JCheckBox chkCustomKtg;

    public AdminPanelFrame() {
        super("HTTH - SUPREME ADMIN CONTROL PANEL [2026 EDITION]");
        initUI();
    }

    private void initUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1280, 850);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        setContentPane(mainPanel);

        // Header Bar
        mainPanel.add(createHeaderBar(), BorderLayout.NORTH);

        // Tabbed Container
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FONT_BOLD);
        tabbedPane.setBackground(COLOR_PANEL);
        tabbedPane.setForeground(COLOR_TEXT);

        tabbedPane.addTab("Dashboard & Bảo Trì", createDashboardTab());
        tabbedPane.addTab("Lệnh GM & Broadcast", createConsoleTab());
        tabbedPane.addTab("Quản Lý & Buff Người Chơi", createPlayersTab());
        tabbedPane.addTab("Boss & Đệ Hoang & Siêu Trùm", createBossAndWildManagementTab());
        tabbedPane.addTab("Phó Bản & Sự Kiện", createDungeonsTab());
        tabbedPane.addTab("Quản Lý Giftcode", createGiftcodeTab());
        tabbedPane.addTab("Gửi Thư & Quà Mail", createMailTab());
        tabbedPane.addTab("Chỉnh Sửa Item 3 & Pet & Ép Đồ", createItemAndPetEditorTab());
        tabbedPane.addTab("Tra Cứu Nhật Ký SQL", createHistoryTab());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Setup Auto Refresh (3s)
        timerAutoRefresh = new javax.swing.Timer(3000, e -> {
            if (chkAutoRefresh != null && chkAutoRefresh.isSelected()) {
                refreshDashboardAsync();
            }
        });
        timerAutoRefresh.start();

        // Initial Load
        refreshAllAsync();
    }

    // =========================================================================
    // HEADER BAR
    // =========================================================================

    private JPanel createHeaderBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(10, 16, 10, 16));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);

        JLabel title = new JLabel("HTTH SUPREME ADMIN PANEL");
        title.setFont(FONT_HEADER);
        title.setForeground(COLOR_ACCENT);
        leftPanel.add(title);

        lblServerStatusBadge = new JLabel(" ● SERVER ONLINE ", JLabel.CENTER);
        lblServerStatusBadge.setFont(FONT_BOLD);
        lblServerStatusBadge.setForeground(COLOR_BG);
        lblServerStatusBadge.setBackground(COLOR_GREEN);
        lblServerStatusBadge.setOpaque(true);
        lblServerStatusBadge.setBorder(new EmptyBorder(3, 8, 3, 8));
        leftPanel.add(lblServerStatusBadge);

        panel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        chkAutoRefresh = new JCheckBox("Auto-Refresh (3s)", true);
        chkAutoRefresh.setFont(FONT_BOLD);
        chkAutoRefresh.setForeground(COLOR_TEXT);
        chkAutoRefresh.setOpaque(false);
        rightPanel.add(chkAutoRefresh);

        JButton btnSaveAll = createStyledButton("Lưu DB", COLOR_ACCENT);
        btnSaveAll.addActionListener(e -> {
            PanelManager.gI().saveAllDataAsync().thenAccept(ok -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, ok ? "Lưu toàn bộ dữ liệu DB thành công!" : "Lưu thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                });
            });
        });
        rightPanel.add(btnSaveAll);

        JButton btnReloadConfigs = createStyledButton("Reload Configs", COLOR_GREEN);
        btnReloadConfigs.addActionListener(e -> {
            PanelManager.gI().reloadAllConfigs().thenAccept(ok -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, ok ? "Đã nạp lại toàn bộ cấu hình, events, phó bản, shop!" : "Nạp lại thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    refreshAllAsync();
                });
            });
        });
        rightPanel.add(btnReloadConfigs);

        JButton btnQuickBaoTri = createStyledButton("Bảo Trì 10s", COLOR_RED);
        btnQuickBaoTri.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "CẢNH BÁO: Bạn có chắc chắn muốn kích hoạt BẢO TRÌ máy chủ sau 10 giây?",
                    "Xác nhận Bảo Trì", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                PanelManager.gI().scheduleMaintenance(10, "Bảo trì định kỳ máy chủ");
                appendConsoleLog("[WARNING] Đã lên lịch đếm ngược bảo trì sau 10 giây!");
            }
        });
        rightPanel.add(btnQuickBaoTri);

        JButton btnRefreshAll = createStyledButton("Làm Mới", COLOR_PURPLE);
        btnRefreshAll.addActionListener(e -> refreshAllAsync());
        rightPanel.add(btnRefreshAll);

        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    // =========================================================================
    // TAB 1: DASHBOARD & BẢO TRÌ
    // =========================================================================

    private JPanel createDashboardTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Stat Cards Grid
        JPanel cardsGrid = new JPanel(new GridLayout(2, 4, 12, 12));
        cardsGrid.setOpaque(false);

        lblRealPlayers = new JLabel("0", JLabel.CENTER);
        lblBotsCount = new JLabel("0", JLabel.CENTER);
        lblSessionsCount = new JLabel("0", JLabel.CENTER);
        lblRamUsage = new JLabel("0 / 0 MB", JLabel.CENTER);
        lblActiveDungeons = new JLabel("0", JLabel.CENTER);
        lblActiveEvents = new JLabel("0", JLabel.CENTER);
        lblThreadsCount = new JLabel("0", JLabel.CENTER);
        lblMaintenanceCountdown = new JLabel("Không", JLabel.CENTER);

        cardsGrid.add(createStatCard("NGƯỜI CHƠI ONLINE", lblRealPlayers, COLOR_GREEN));
        cardsGrid.add(createStatCard("BOTS HOẠT ĐỘNG", lblBotsCount, COLOR_ACCENT));
        cardsGrid.add(createStatCard("TỔNG SESSIONS", lblSessionsCount, COLOR_ORANGE));
        cardsGrid.add(createStatCard("RAM SỬ DỤNG", lblRamUsage, COLOR_PURPLE));
        cardsGrid.add(createStatCard("PHÓ BẢN HOẠT ĐỘNG", lblActiveDungeons, COLOR_YELLOW));
        cardsGrid.add(createStatCard("SỰ KIỆN ĐANG CHẠY", lblActiveEvents, COLOR_GREEN));
        cardsGrid.add(createStatCard("SỐ THREADS CHẠY", lblThreadsCount, COLOR_ACCENT));
        cardsGrid.add(createStatCard("ĐẾM NGƯỢC BẢO TRÌ", lblMaintenanceCountdown, COLOR_RED));

        panel.add(cardsGrid, BorderLayout.NORTH);

        // Lower Maintenance & Server Controls
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        centerPanel.setOpaque(false);

        // Maintenance Box
        JPanel maintPanel = createTitledPanel("ĐIỀU KHIỂN BẢO TRÌ MÁY CHỦ");
        maintPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        maintPanel.add(createLabel("Thời gian đếm ngược (giây):"), gbc);
        gbc.gridx = 1;
        txtMaintSeconds = createTextField("30");
        maintPanel.add(txtMaintSeconds, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        maintPanel.add(createLabel("Lý do bảo trì:"), gbc);
        gbc.gridx = 1;
        txtMaintReason = createTextField("Bảo trì nâng cấp tính năng định kỳ");
        maintPanel.add(txtMaintReason, gbc);

        JPanel maintBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        maintBtns.setOpaque(false);

        JButton btnStartMaint = createStyledButton("Kích Hoạt Bảo Trì", COLOR_RED);
        btnStartMaint.addActionListener(e -> {
            try {
                int s = Integer.parseInt(txtMaintSeconds.getText().trim());
                String r = txtMaintReason.getText().trim();
                int cf = JOptionPane.showConfirmDialog(this, "Bắt đầu đếm ngược bảo trì sau " + s + "s?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (cf == JOptionPane.YES_OPTION) {
                    PanelManager.gI().scheduleMaintenance(s, r);
                    appendConsoleLog("[MAINTENANCE] Đã lên lịch bảo trì sau " + s + "s. Lý do: " + r);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Số giây không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        maintBtns.add(btnStartMaint);

        JButton btnCancelMaint = createStyledButton("Hủy Lệnh Bảo Trì", COLOR_YELLOW);
        btnCancelMaint.addActionListener(e -> {
            if (PanelManager.gI().cancelMaintenance()) {
                JOptionPane.showMessageDialog(this, "Đã hủy bỏ đếm ngược bảo trì thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không có lịch bảo trì nào đang chờ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });
        maintBtns.add(btnCancelMaint);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        maintPanel.add(maintBtns, gbc);

        centerPanel.add(maintPanel);

        // System Control Box
        JPanel sysCtrlPanel = createTitledPanel("ĐIỀU KHIỂN HỆ THỐNG & KICK");
        sysCtrlPanel.setLayout(new GridLayout(4, 1, 8, 8));

        chkAdminTestMode = new JCheckBox("Chế độ Admin Test Mode (server_admin = true)", core.Manager.gI().server_admin);
        chkAdminTestMode.setFont(FONT_BOLD);
        chkAdminTestMode.setForeground(COLOR_YELLOW);
        chkAdminTestMode.setOpaque(false);
        chkAdminTestMode.addActionListener(e -> {
            PanelManager.gI().setAdminTestMode(chkAdminTestMode.isSelected());
            appendConsoleLog("[SYSTEM] Admin Test Mode chuyển thành: " + chkAdminTestMode.isSelected());
        });
        sysCtrlPanel.add(chkAdminTestMode);

        JButton btnKickAll = createStyledButton("Kick Toàn Bộ Người Chơi (Kick All Clients)", COLOR_ORANGE);
        btnKickAll.addActionListener(e -> {
            int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn kick TOÀN BỘ người chơi đang kết nối không?", "Kick All", JOptionPane.YES_NO_OPTION);
            if (cf == JOptionPane.YES_OPTION) {
                PanelManager.gI().kickAllPlayers("Admin thực hiện ngắt kết nối toàn máy chủ");
                appendConsoleLog("[SYSTEM] Đã kick toàn bộ người chơi!");
            }
        });
        sysCtrlPanel.add(btnKickAll);

        JButton btnFlushSave = createStyledButton("Lưu Dữ Liệu Ngay (Flush SaveData & Cache)", COLOR_ACCENT);
        btnFlushSave.addActionListener(e -> {
            PanelManager.gI().saveAllDataAsync().thenAccept(ok -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, ok ? "Lưu dữ liệu thành công!" : "Lưu thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                });
            });
        });
        sysCtrlPanel.add(btnFlushSave);

        JButton btnReloadAll = createStyledButton("Nạp Lại Toàn Bộ Cấu Hình & Templates (Hot Reload)", COLOR_GREEN);
        btnReloadAll.addActionListener(e -> {
            PanelManager.gI().reloadAllConfigs().thenAccept(ok -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, ok ? "Reload toàn bộ configs & templates thành công!" : "Reload thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    refreshAllAsync();
                });
            });
        });
        sysCtrlPanel.add(btnReloadAll);

        centerPanel.add(sysCtrlPanel);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // TAB 2: LỆNH GM & BROADCAST
    // =========================================================================

    private JPanel createConsoleTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Console Log Output
        txtConsoleLog = new JTextArea();
        txtConsoleLog.setBackground(new Color(10, 15, 26));
        txtConsoleLog.setForeground(COLOR_GREEN);
        txtConsoleLog.setFont(FONT_MONO);
        txtConsoleLog.setEditable(false);
        txtConsoleLog.setText("=== HTTH GM CONSOLE INITIALIZED ===\nGõ /help để xem hướng dẫn toàn bộ lệnh quản trị.\n");

        JScrollPane scrollLog = new JScrollPane(txtConsoleLog);
        scrollLog.setBorder(new LineBorder(COLOR_CARD, 1));
        panel.add(scrollLog, BorderLayout.CENTER);

        // Command Input Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setOpaque(false);

        JPanel cmdInputBar = new JPanel(new BorderLayout(8, 0));
        cmdInputBar.setOpaque(false);

        JLabel lblPrompt = new JLabel(" GM > ");
        lblPrompt.setFont(FONT_MONO_BOLD);
        lblPrompt.setForeground(COLOR_ACCENT);
        cmdInputBar.add(lblPrompt, BorderLayout.WEST);

        txtCommandInput = createTextField("");
        txtCommandInput.setFont(FONT_MONO);
        txtCommandInput.addActionListener(e -> executeConsoleCommand());
        cmdInputBar.add(txtCommandInput, BorderLayout.CENTER);

        JPanel cmdBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        cmdBtns.setOpaque(false);

        JButton btnRunCmd = createStyledButton("Thực Thi", COLOR_ACCENT);
        btnRunCmd.addActionListener(e -> executeConsoleCommand());
        cmdBtns.add(btnRunCmd);

        JButton btnHelpCmd = createStyledButton("/help", COLOR_YELLOW);
        btnHelpCmd.addActionListener(e -> {
            txtCommandInput.setText("/help");
            executeConsoleCommand();
        });
        cmdBtns.add(btnHelpCmd);

        JButton btnClearLog = createStyledButton("Xóa Log", COLOR_CARD);
        btnClearLog.addActionListener(e -> txtConsoleLog.setText(""));
        cmdBtns.add(btnClearLog);

        cmdInputBar.add(cmdBtns, BorderLayout.EAST);
        bottomPanel.add(cmdInputBar, BorderLayout.NORTH);

        // Broadcast Tools
        JPanel bcPanel = createTitledPanel("CÔNG CỤ PHÁT LOA THẾ GIỚI & THÔNG BÁO POPUP");
        bcPanel.setLayout(new GridLayout(2, 1, 6, 6));

        // World Chat
        JPanel wcRow = new JPanel(new BorderLayout(8, 0));
        wcRow.setOpaque(false);
        wcRow.add(createLabel("Loa KTG: "), BorderLayout.WEST);
        txtWorldChatMsg = createTextField("Thông báo từ Ban Quản Trị Server Hải Tặc Tí Hon!");
        wcRow.add(txtWorldChatMsg, BorderLayout.CENTER);

        JPanel wcRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        wcRight.setOpaque(false);
        cbWorldChatColor = new JComboBox<>(new String[]{"Đỏ (Loa VIP)", "Trắng", "Xanh Cyan", "Vàng"});
        cbWorldChatColor.setBackground(COLOR_INPUT_BG);
        cbWorldChatColor.setForeground(COLOR_TEXT);
        wcRight.add(cbWorldChatColor);

        JButton btnSendWc = createStyledButton("Gửi Loa KTG", COLOR_ORANGE);
        btnSendWc.addActionListener(e -> {
            String msg = txtWorldChatMsg.getText().trim();
            if (!msg.isEmpty()) {
                int col = cbWorldChatColor.getSelectedIndex() == 0 ? 1 : 0;
                PanelManager.gI().broadcastWorldChat(msg, col);
                appendConsoleLog("[KTG BROADCAST] " + msg);
            }
        });
        wcRight.add(btnSendWc);
        wcRow.add(wcRight, BorderLayout.EAST);
        bcPanel.add(wcRow);

        // Popup Notice
        JPanel popupRow = new JPanel(new BorderLayout(8, 0));
        popupRow.setOpaque(false);
        popupRow.add(createLabel("Popup OK: "), BorderLayout.WEST);
        txtPopupMsg = createTextField("Máy chủ đang diễn ra sự kiện cực hấp dẫn!");
        popupRow.add(txtPopupMsg, BorderLayout.CENTER);

        JButton btnSendPopup = createStyledButton("Gửi Popup Toàn Server", COLOR_RED);
        btnSendPopup.addActionListener(e -> {
            String msg = txtPopupMsg.getText().trim();
            if (!msg.isEmpty()) {
                PanelManager.gI().broadcastPopupNotice(msg);
                appendConsoleLog("[POPUP BROADCAST] " + msg);
            }
        });
        popupRow.add(btnSendPopup, BorderLayout.EAST);
        bcPanel.add(popupRow);

        bottomPanel.add(bcPanel, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void executeConsoleCommand() {
        String line = txtCommandInput.getText().trim();
        if (line.isEmpty()) return;
        appendConsoleLog("GM > " + line);
        txtCommandInput.setText("");

        CompletableFuture.supplyAsync(() -> PanelManager.gI().executeAdminCommand(line))
                .thenAccept(res -> SwingUtilities.invokeLater(() -> appendConsoleLog(res)));
    }

    private void appendConsoleLog(String text) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        txtConsoleLog.append("[" + time + "] " + text + "\n");
        txtConsoleLog.setCaretPosition(txtConsoleLog.getDocument().getLength());
    }

    // =========================================================================
    // TAB 3: QUẢN LÝ & BUFF NGƯỜI CHƠI
    // =========================================================================

    private JPanel createPlayersTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(460);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        // Left: Player Table & Search
        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.setOpaque(false);

        JPanel searchBar = new JPanel(new BorderLayout(6, 0));
        searchBar.setOpaque(false);
        txtPlayerSearch = createTextField("");
        txtPlayerSearch.addActionListener(e -> refreshPlayersListAsync());
        searchBar.add(txtPlayerSearch, BorderLayout.CENTER);

        JButton btnSearch = createStyledButton("Tìm", COLOR_ACCENT);
        btnSearch.addActionListener(e -> refreshPlayersListAsync());
        searchBar.add(btnSearch, BorderLayout.EAST);

        leftPanel.add(searchBar, BorderLayout.NORTH);

        String[] cols = {"ID", "AccID", "Tên", "Cấp", "Beri", "Ruby", "Map"};
        modelPlayers = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblPlayers = createStyledTable(modelPlayers);
        tblPlayers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblPlayers.getSelectedRow() >= 0) {
                String name = tblPlayers.getValueAt(tblPlayers.getSelectedRow(), 2).toString();
                currentSelectedPlayer = name;
                inspectPlayerAsync(name);
            }
        });

        // Right Click Menu on Player
        JPopupMenu popMenu = new JPopupMenu();
        JMenuItem miKick = new JMenuItem("Kick Khỏi Server");
        miKick.addActionListener(e -> {
            if (!currentSelectedPlayer.isEmpty()) {
                PanelManager.gI().kickPlayer(currentSelectedPlayer, "Kick bởi Admin");
                refreshPlayersListAsync();
            }
        });
        popMenu.add(miKick);

        JMenuItem miBan = new JMenuItem("Khóa Tài Khoản (Ban)");
        miBan.addActionListener(e -> {
            if (!currentSelectedPlayer.isEmpty()) {
                PanelManager.gI().banPlayer(currentSelectedPlayer, "Admin Ban");
                refreshPlayersListAsync();
            }
        });
        popMenu.add(miBan);

        JMenuItem miUnstuck = new JMenuItem("Cứu Kẹt (Tele Map 1)");
        miUnstuck.addActionListener(e -> {
            if (!currentSelectedPlayer.isEmpty()) {
                PanelManager.gI().resetPlayerLocation(currentSelectedPlayer);
                JOptionPane.showMessageDialog(this, "Đã cứu kẹt đưa " + currentSelectedPlayer + " về Map 1!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        popMenu.add(miUnstuck);

        tblPlayers.setComponentPopupMenu(popMenu);
        leftPanel.add(new JScrollPane(tblPlayers), BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);

        // Right: Inspector & Buff Action Center
        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setOpaque(false);

        // Player Info Header
        JPanel infoHeader = createTitledPanel("THÔNG TIN CHI TIẾT NHÂN VẬT");
        infoHeader.setLayout(new BorderLayout(8, 8));
        lblSelectedPlayerInfo = new JLabel("Chọn một người chơi từ danh sách để xem thông tin chi tiết và thao tác.");
        lblSelectedPlayerInfo.setFont(FONT_REGULAR);
        lblSelectedPlayerInfo.setForeground(COLOR_TEXT);
        infoHeader.add(lblSelectedPlayerInfo, BorderLayout.CENTER);

        rightPanel.add(infoHeader, BorderLayout.NORTH);

        // Action & Buff Controls
        JPanel actionControls = createTitledPanel("THAO TÁC QUẢN TRỊ & BUFF NHANH");
        actionControls.setLayout(new GridLayout(3, 1, 6, 6));

        // Buff row 1
        JPanel buffRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        buffRow1.setOpaque(false);
        buffRow1.add(createLabel("Số lượng buff: "));
        txtBuffAmount = createTextField("1000000");
        txtBuffAmount.setPreferredSize(new Dimension(110, 26));
        buffRow1.add(txtBuffAmount);

        JButton btnBuffBeri = createStyledButton("+ Beri", COLOR_YELLOW);
        btnBuffBeri.addActionListener(e -> applyBuff("beri"));
        buffRow1.add(btnBuffBeri);

        JButton btnBuffRuby = createStyledButton("+ Ruby", COLOR_RED);
        btnBuffRuby.addActionListener(e -> applyBuff("ruby"));
        buffRow1.add(btnBuffRuby);

        JButton btnBuffVnd = createStyledButton("+ VND", COLOR_GREEN);
        btnBuffVnd.addActionListener(e -> applyBuff("vnd"));
        buffRow1.add(btnBuffVnd);

        JButton btnBuffExp = createStyledButton("+ EXP", COLOR_PURPLE);
        btnBuffExp.addActionListener(e -> applyBuff("exp"));
        buffRow1.add(btnBuffExp);

        JButton btnBuffSp = createStyledButton("+ SP", COLOR_ACCENT);
        btnBuffSp.addActionListener(e -> applyBuff("sp"));
        buffRow1.add(btnBuffSp);

        actionControls.add(buffRow1);

        // Buff row 2
        JPanel buffRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        buffRow2.setOpaque(false);

        JButton btnBuffHaki = createStyledButton("+ Haki", COLOR_ORANGE);
        btnBuffHaki.addActionListener(e -> applyBuff("haki"));
        buffRow2.add(btnBuffHaki);

        JButton btnBuffLevel = createStyledButton("+ Level", COLOR_YELLOW);
        btnBuffLevel.addActionListener(e -> applyBuff("level"));
        buffRow2.add(btnBuffLevel);

        JButton btnBuffTickets = createStyledButton("+ Vé/Key", COLOR_ACCENT);
        btnBuffTickets.addActionListener(e -> {
            applyBuff("ticket");
            applyBuff("key");
        });
        buffRow2.add(btnBuffTickets);

        JButton btnHeal = createStyledButton("Hồi Đầy Máu/MP", COLOR_GREEN);
        btnHeal.addActionListener(e -> applyBuff("hpmp"));
        buffRow2.add(btnHeal);

        JButton btnBuffMax = createStyledButton("BUFF MAX FULL", COLOR_RED);
        btnBuffMax.addActionListener(e -> applyBuff("full"));
        buffRow2.add(btnBuffMax);

        actionControls.add(buffRow2);

        // Account actions row
        JPanel accRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        accRow.setOpaque(false);

        JButton btnUnstuck = createStyledButton("Cứu Kẹt (Map 1)", COLOR_ACCENT);
        btnUnstuck.addActionListener(e -> {
            if (!currentSelectedPlayer.isEmpty()) {
                PanelManager.gI().resetPlayerLocation(currentSelectedPlayer);
                JOptionPane.showMessageDialog(this, "Đã cứu kẹt đưa " + currentSelectedPlayer + " về Map 1!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                inspectPlayerAsync(currentSelectedPlayer);
            }
        });
        accRow.add(btnUnstuck);

        JButton btnMute = createStyledButton("Cấm Chat", COLOR_ORANGE);
        btnMute.addActionListener(e -> {
            if (!currentSelectedPlayer.isEmpty()) {
                PanelManager.gI().mutePlayer(currentSelectedPlayer, 60, "Admin cấm chat");
                inspectPlayerAsync(currentSelectedPlayer);
            }
        });
        accRow.add(btnMute);

        JButton btnUnmute = createStyledButton("Gỡ Cấm Chat", COLOR_GREEN);
        btnUnmute.addActionListener(e -> {
            if (!currentSelectedPlayer.isEmpty()) {
                PanelManager.gI().unmutePlayer(currentSelectedPlayer);
                inspectPlayerAsync(currentSelectedPlayer);
            }
        });
        accRow.add(btnUnmute);

        JButton btnBan = createStyledButton("Khóa Acc", COLOR_RED);
        btnBan.addActionListener(e -> {
            if (!currentSelectedPlayer.isEmpty()) {
                int cf = JOptionPane.showConfirmDialog(this, "Khóa tài khoản của " + currentSelectedPlayer + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (cf == JOptionPane.YES_OPTION) {
                    PanelManager.gI().banPlayer(currentSelectedPlayer, "Admin Ban");
                    refreshPlayersListAsync();
                }
            }
        });
        accRow.add(btnBan);

        JButton btnChangePass = createStyledButton("Đổi Mật Khẩu", COLOR_PURPLE);
        btnChangePass.addActionListener(e -> {
            if (!currentSelectedPlayer.isEmpty()) {
                String newPass = JOptionPane.showInputDialog(this, "Nhập mật khẩu mới cho " + currentSelectedPlayer + ":");
                if (newPass != null && !newPass.trim().isEmpty()) {
                    boolean ok = PanelManager.gI().changeAccountPassword(currentSelectedPlayer, newPass);
                    JOptionPane.showMessageDialog(this, ok ? "Đổi mật khẩu thành công!" : "Đổi mật khẩu thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        accRow.add(btnChangePass);

        actionControls.add(accRow);
        rightPanel.add(actionControls, BorderLayout.SOUTH);

        // SubTab Details (Equip / Bag / Box / Pets / Boats)
        tabPlayerDetails = new JTabbedPane();
        tabPlayerDetails.setFont(FONT_BOLD);
        tabPlayerDetails.setBackground(COLOR_PANEL);
        tabPlayerDetails.setForeground(COLOR_TEXT);

        // Equip
        modelEquipItems = new DefaultTableModel(new String[]{"Slot", "Tên Trang Bị", "Phẩm Chất", "Cấp +", "Chỉ Số Options", "Đá Khảm"}, 0);
        tabPlayerDetails.addTab("Đang Mặc", new JScrollPane(createStyledTable(modelEquipItems)));

        // Bag Item3
        modelBagItem3 = new DefaultTableModel(new String[]{"Index", "Tên Trang Bị", "Phẩm Chất", "Cấp +", "Chỉ Số Options", "Đá Khảm"}, 0);
        tabPlayerDetails.addTab("Hành Trang Item3", new JScrollPane(createStyledTable(modelBagItem3)));

        // Box Item3
        modelBoxItem3 = new DefaultTableModel(new String[]{"Index", "Tên Trang Bị", "Phẩm Chất", "Cấp +", "Chỉ Số Options", "Đá Khảm"}, 0);
        tabPlayerDetails.addTab("Rương Đồ Item3", new JScrollPane(createStyledTable(modelBoxItem3)));

        // Bag Simple
        modelBagSimple = new DefaultTableModel(new String[]{"Loại", "ID", "Tên Vật Phẩm", "Số Lượng"}, 0);
        tabPlayerDetails.addTab("Hành Trang Item 4 & 7", new JScrollPane(createStyledTable(modelBagSimple)));

        // Box Simple
        modelBoxSimple = new DefaultTableModel(new String[]{"Loại", "ID", "Tên Vật Phẩm", "Số Lượng"}, 0);
        tabPlayerDetails.addTab("Rương Item 4 & 7", new JScrollPane(createStyledTable(modelBoxSimple)));

        // Pets
        modelPlayerPets = new DefaultTableModel(new String[]{"ID", "Tên Thú Cưng / Pet", "Trạng Thái", "Chỉ Số Buff"}, 0);
        tabPlayerDetails.addTab("Thú Cưng (Pets)", new JScrollPane(createStyledTable(modelPlayerPets)));

        // Boats
        modelPlayerBoats = new DefaultTableModel(new String[]{"ID", "Tên Thuyền / Ván", "Đang Dùng"}, 0);
        tabPlayerDetails.addTab("Thuyền & Ván Lướt", new JScrollPane(createStyledTable(modelPlayerBoats)));

        rightPanel.add(tabPlayerDetails, BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private void applyBuff(String type) {
        if (currentSelectedPlayer == null || currentSelectedPlayer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một người chơi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            long amt = Long.parseLong(txtBuffAmount.getText().trim());
            boolean ok = PanelManager.gI().buffPlayer(currentSelectedPlayer, type, amt);
            if (ok) {
                appendConsoleLog("[BUFF] Đã buff " + type + " (" + amt + ") cho " + currentSelectedPlayer);
                inspectPlayerAsync(currentSelectedPlayer);
            } else {
                JOptionPane.showMessageDialog(this, "Buff thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inspectPlayerAsync(String name) {
        CompletableFuture.supplyAsync(() -> PanelManager.gI().getPlayerDetail(name))
                .thenAccept(detail -> SwingUtilities.invokeLater(() -> {
                    if (detail == null || detail.basicInfo == null) {
                        lblSelectedPlayerInfo.setText("Không thể lấy dữ liệu nhân vật: " + name);
                        return;
                    }
                    PlayerInfoDTO b = detail.basicInfo;
                    String statusText = b.isOnline ? "<font color='#4ADE80'>ONLINE</font>" : "<font color='#F87171'>OFFLINE</font>";
                    String muteText = b.isMuted ? " | <font color='#FB923C'>BỊ CẤM CHAT</font>" : "";
                    String banText = b.isBanned ? " | <font color='#F87171'>BỊ KHÓA ACC</font>" : "";

                    lblSelectedPlayerInfo.setText("<html><b>Nhân vật:</b> <font color='#38BDF8'>" + b.name + "</font> (ID: " + b.playerId + ", AccID: " + b.accountId + ") [" + statusText + muteText + banText + "]<br>"
                            + "<b>Level:</b> " + b.level + " | <b>EXP:</b> " + b.exp + " | <b>Vị trí:</b> " + b.mapName + " (Khu " + b.zoneId + ") | <b>IP:</b> " + b.ip + "<br>"
                            + "<b>Beri:</b> <font color='#FDE047'>" + String.format("%,d", b.beri) + "</font> | <b>Ruby:</b> <font color='#F87171'>" + String.format("%,d", b.ruby) + "</font> | <b>VND:</b> <font color='#4ADE80'>" + String.format("%,d", b.extol) + "</font> | <b>SP:</b> " + b.spPoint + " | <b>Haki:</b> " + b.haki + "</html>");

                    // Clear tables
                    modelEquipItems.setRowCount(0);
                    modelBagItem3.setRowCount(0);
                    modelBoxItem3.setRowCount(0);
                    modelBagSimple.setRowCount(0);
                    modelBoxSimple.setRowCount(0);
                    modelPlayerPets.setRowCount(0);
                    modelPlayerBoats.setRowCount(0);

                    // Equip
                    for (ItemWearDTO it : detail.equippedItems) {
                        StringBuilder ops = new StringBuilder();
                        for (OptionDTO op : it.options) ops.append(op.name).append(" (+").append(op.param).append(") ");
                        modelEquipItems.addRow(new Object[]{it.index, it.name, getColorName(it.color), "+" + it.levelUp, ops.toString(), it.mdakham != null ? it.mdakham.length : 0});
                    }

                    // Bag3
                    for (ItemWearDTO it : detail.bagItem3) {
                        StringBuilder ops = new StringBuilder();
                        for (OptionDTO op : it.options) ops.append(op.name).append(" (+").append(op.param).append(") ");
                        modelBagItem3.addRow(new Object[]{it.index, it.name, getColorName(it.color), "+" + it.levelUp, ops.toString(), it.mdakham != null ? it.mdakham.length : 0});
                    }

                    // Box3
                    for (ItemWearDTO it : detail.boxItem3) {
                        StringBuilder ops = new StringBuilder();
                        for (OptionDTO op : it.options) ops.append(op.name).append(" (+").append(op.param).append(") ");
                        modelBoxItem3.addRow(new Object[]{it.index, it.name, getColorName(it.color), "+" + it.levelUp, ops.toString(), it.mdakham != null ? it.mdakham.length : 0});
                    }

                    // Bag 4 & 7
                    for (SimpleItemDTO s : detail.bagItems4) {
                        modelBagSimple.addRow(new Object[]{"Item 4", s.id, s.name, s.quantity});
                    }
                    for (SimpleItemDTO s : detail.bagItems7) {
                        modelBagSimple.addRow(new Object[]{"Item 7", s.id, s.name, s.quantity});
                    }

                    // Box 4 & 7
                    for (SimpleItemDTO s : detail.boxItems4) {
                        modelBoxSimple.addRow(new Object[]{"Item 4", s.id, s.name, s.quantity});
                    }
                    for (SimpleItemDTO s : detail.boxItems7) {
                        modelBoxSimple.addRow(new Object[]{"Item 7", s.id, s.name, s.quantity});
                    }

                    // Pets
                    for (PetDetailDTO p : detail.pets) {
                        StringBuilder ops = new StringBuilder();
                        for (OptionDTO op : p.options) ops.append(op.name).append(" (+").append(op.param).append(") ");
                        modelPlayerPets.addRow(new Object[]{p.id, p.name, p.isUsing ? "Đang mang" : "Chưa mang", ops.toString()});
                    }

                    // Boats
                    for (BoatDetailDTO bDto : detail.boats) {
                        modelPlayerBoats.addRow(new Object[]{bDto.id, bDto.name, bDto.isUsing ? "Đang lái" : "Trong kho"});
                    }
                }));
    }

    private String getColorName(int color) {
        switch (color) {
            case 0: return "Trắng";
            case 1: return "Xanh Dương";
            case 2: return "Vàng";
            case 3: return "Tím";
            case 4: return "Cam";
            case 5: return "Đỏ";
            case 8: return "Thần Trang";
            default: return "Màu " + color;
        }
    }

    // =========================================================================
    // TAB 4: MỞ / ĐÓNG PHÓ BẢN & SỰ KIỆN
    // =========================================================================

    private JPanel createDungeonsTab() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Timed Dungeons
        JPanel dungeonPanel = createTitledPanel("QUẢN LÝ 10 PHÓ BẢN THỜI GIAN (TIMED DUNGEONS)");
        dungeonPanel.setLayout(new BorderLayout(8, 8));

        JPanel topDungeonBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topDungeonBar.setOpaque(false);
        chkMasterDungeon = new JCheckBox("Master Switch (Bật toàn bộ phó bản)", true);
        chkMasterDungeon.setFont(FONT_BOLD);
        chkMasterDungeon.setForeground(COLOR_YELLOW);
        chkMasterDungeon.setOpaque(false);
        chkMasterDungeon.addActionListener(e -> {
            PanelManager.gI().setMasterDungeonSwitch(chkMasterDungeon.isSelected());
            refreshDungeonsAsync();
        });
        topDungeonBar.add(chkMasterDungeon);

        JButton btnStartRound = createStyledButton("Bắt Đầu Đợt Mới", COLOR_GREEN);
        btnStartRound.addActionListener(e -> {
            int row = tblDungeons.getSelectedRow();
            if (row >= 0) {
                String dId = tblDungeons.getValueAt(row, 0).toString();
                PanelManager.gI().forceStartDungeon(dId);
                refreshDungeonsAsync();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 phó bản!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });
        topDungeonBar.add(btnStartRound);

        JButton btnEndRound = createStyledButton("Kết Thúc Đợt", COLOR_RED);
        btnEndRound.addActionListener(e -> {
            int row = tblDungeons.getSelectedRow();
            if (row >= 0) {
                String dId = tblDungeons.getValueAt(row, 0).toString();
                PanelManager.gI().forceEndDungeon(dId);
                refreshDungeonsAsync();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 phó bản!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });
        topDungeonBar.add(btnEndRound);

        JButton btnToggleDungeon = createStyledButton("Bật/Tắt PB", COLOR_ACCENT);
        btnToggleDungeon.addActionListener(e -> {
            int row = tblDungeons.getSelectedRow();
            if (row >= 0) {
                String dId = tblDungeons.getValueAt(row, 0).toString();
                boolean curActive = Boolean.parseBoolean(tblDungeons.getValueAt(row, 6).toString());
                PanelManager.gI().toggleDungeon(dId, !curActive);
                refreshDungeonsAsync();
            }
        });
        topDungeonBar.add(btnToggleDungeon);

        dungeonPanel.add(topDungeonBar, BorderLayout.NORTH);

        String[] dCols = {"ID Phó Bản", "Tên Hoạt Động", "Trạng Thái", "Còn Lại (s)", "Chạy (phút)", "Chờ (phút)", "Kích Hoạt", "Thông Tin"};
        modelDungeons = new DefaultTableModel(dCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDungeons = createStyledTable(modelDungeons);
        dungeonPanel.add(new JScrollPane(tblDungeons), BorderLayout.CENTER);
        panel.add(dungeonPanel);

        // Season Events
        JPanel eventPanel = createTitledPanel("QUẢN LÝ 17 SỰ KIỆN MÙA (SEASON EVENTS)");
        eventPanel.setLayout(new BorderLayout(8, 8));

        JPanel topEventBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topEventBar.setOpaque(false);

        JButton btnToggleEvent = createStyledButton("Bật / Tắt Sự Kiện", COLOR_ACCENT);
        btnToggleEvent.addActionListener(e -> {
            int row = tblEvents.getSelectedRow();
            if (row >= 0) {
                int evId = Integer.parseInt(tblEvents.getValueAt(row, 1).toString());
                boolean curActive = Boolean.parseBoolean(tblEvents.getValueAt(row, 3).toString());
                boolean ok = PanelManager.gI().toggleEvent(evId, !curActive);
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Không thể chuyển trạng thái (Xung đột mùa hoặc trùng lịch)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                refreshEventsAsync();
            }
        });
        topEventBar.add(btnToggleEvent);

        eventPanel.add(topEventBar, BorderLayout.NORTH);

        String[] eCols = {"Schedule ID", "Event ID", "Tên Sự Kiện", "Kích Hoạt", "Mùa Sự Kiện", "Thời Gian Chạy"};
        modelEvents = new DefaultTableModel(eCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblEvents = createStyledTable(modelEvents);
        eventPanel.add(new JScrollPane(tblEvents), BorderLayout.CENTER);
        panel.add(eventPanel);

        return panel;
    }

    // =========================================================================
    // TAB 5: QUẢN LÝ GIFTCODE
    // =========================================================================

    private JPanel createGiftcodeTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topBar.setOpaque(false);

        topBar.add(createLabel("Tìm code:"));
        txtGiftSearch = createTextField("");
        txtGiftSearch.setPreferredSize(new Dimension(160, 26));
        txtGiftSearch.addActionListener(e -> refreshGiftcodesAsync());
        topBar.add(txtGiftSearch);

        JButton btnSearchGift = createStyledButton("Tìm Kiếm", COLOR_ACCENT);
        btnSearchGift.addActionListener(e -> refreshGiftcodesAsync());
        topBar.add(btnSearchGift);

        JButton btnNewGift = createStyledButton("+ Tạo Giftcode Mới", COLOR_GREEN);
        btnNewGift.addActionListener(e -> openGiftcodeEditorDialog(null));
        topBar.add(btnNewGift);

        JButton btnEditGift = createStyledButton("Sửa Code Đã Chọn", COLOR_YELLOW);
        btnEditGift.addActionListener(e -> {
            int row = tblGiftcodes.getSelectedRow();
            if (row >= 0) {
                String code = tblGiftcodes.getValueAt(row, 0).toString();
                openGiftcodeEditorDialog(code);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 giftcode từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });
        topBar.add(btnEditGift);

        JButton btnResetUsed = createStyledButton("Xóa Lịch Sử Đã Nhập", COLOR_PURPLE);
        btnResetUsed.addActionListener(e -> {
            int row = tblGiftcodes.getSelectedRow();
            if (row >= 0) {
                String code = tblGiftcodes.getValueAt(row, 0).toString();
                int cf = JOptionPane.showConfirmDialog(this, "Xóa lịch sử người đã nhập của code [" + code + "] để cho phép nhận lại?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (cf == JOptionPane.YES_OPTION) {
                    PanelManager.gI().resetGiftCodeUsed(code);
                    refreshGiftcodesAsync();
                }
            }
        });
        topBar.add(btnResetUsed);

        JButton btnDeleteGift = createStyledButton("Xóa Code", COLOR_RED);
        btnDeleteGift.addActionListener(e -> {
            int row = tblGiftcodes.getSelectedRow();
            if (row >= 0) {
                String code = tblGiftcodes.getValueAt(row, 0).toString();
                int cf = JOptionPane.showConfirmDialog(this, "Xác nhận xóa vĩnh viễn giftcode [" + code + "]?", "Xóa", JOptionPane.YES_NO_OPTION);
                if (cf == JOptionPane.YES_OPTION) {
                    PanelManager.gI().deleteGiftCode(code);
                    refreshGiftcodesAsync();
                }
            }
        });
        topBar.add(btnDeleteGift);

        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Mã Giftcode", "Đã Nhập / Giới Hạn", "Beri", "Ruby", "Item Đính Kèm", "Thông Báo", "Kích Hoạt (MTV)", "Tài Khoản Chỉ Định"};
        modelGiftcodes = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblGiftcodes = createStyledTable(modelGiftcodes);
        panel.add(new JScrollPane(tblGiftcodes), BorderLayout.CENTER);

        return panel;
    }

    private void openGiftcodeEditorDialog(String existingCode) {
        JDialog dlg = new JDialog(this, (existingCode != null ? "Sửa Giftcode: " + existingCode : "Tạo Giftcode Mới"), true);
        dlg.setSize(550, 480);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COLOR_BG);
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtCode = createTextField(existingCode != null ? existingCode : "CODE_" + ZUtil.random(1000, 9999));
        JTextField txtLimit = createTextField("99999");
        JTextField txtBeri = createTextField("500000000");
        JTextField txtRuby = createTextField("500000");
        JTextField txtNotice = createTextField("Bạn nhận được quà Giftcode!");
        JTextArea txtItems = new JTextArea(4, 30);
        txtItems.setBackground(COLOR_INPUT_BG);
        txtItems.setForeground(COLOR_TEXT);
        txtItems.setFont(FONT_MONO);
        txtItems.setText("[[4,0,500000000],[4,1,500000],[4,908,500000000],[7,0,999],[7,10,999]]");
        JTextField txtSpecial = createTextField("");

        // Load existing
        if (existingCode != null) {
            List<GiftCodeDTO> all = PanelManager.gI().getAllGiftCodes(existingCode);
            for (GiftCodeDTO d : all) {
                if (d.giftname.equalsIgnoreCase(existingCode)) {
                    txtCode.setText(d.giftname);
                    txtLimit.setText(String.valueOf(d.gioihan));
                    txtBeri.setText(String.valueOf(d.beri));
                    txtRuby.setText(String.valueOf(d.ruby));
                    txtNotice.setText(d.notice);
                    txtItems.setText(d.itemJson);
                    txtSpecial.setText(d.special);
                    break;
                }
            }
        }

        g.gridx = 0; g.gridy = 0; p.add(createLabel("Mã Giftcode:"), g);
        g.gridx = 1; p.add(txtCode, g);

        g.gridx = 0; g.gridy = 1; p.add(createLabel("Giới hạn lượt nhập:"), g);
        g.gridx = 1; p.add(txtLimit, g);

        g.gridx = 0; g.gridy = 2; p.add(createLabel("Thưởng Beri:"), g);
        g.gridx = 1; p.add(txtBeri, g);

        g.gridx = 0; g.gridy = 3; p.add(createLabel("Thưởng Ruby:"), g);
        g.gridx = 1; p.add(txtRuby, g);

        g.gridx = 0; g.gridy = 4; p.add(createLabel("Thông báo:"), g);
        g.gridx = 1; p.add(txtNotice, g);

        g.gridx = 0; g.gridy = 5; p.add(createLabel("Item JSON:"), g);
        g.gridx = 1; p.add(new JScrollPane(txtItems), g);

        g.gridx = 0; g.gridy = 6; p.add(createLabel("Acc chỉ định:"), g);
        g.gridx = 1; p.add(txtSpecial, g);

        JButton btnSave = createStyledButton("LƯU GIFTCODE", COLOR_GREEN);
        btnSave.addActionListener(e -> {
            try {
                GiftCodeDTO dto = new GiftCodeDTO();
                dto.giftname = txtCode.getText().trim();
                dto.gioihan = Integer.parseInt(txtLimit.getText().trim());
                dto.beri = Integer.parseInt(txtBeri.getText().trim());
                dto.ruby = Integer.parseInt(txtRuby.getText().trim());
                dto.notice = txtNotice.getText().trim();
                dto.itemJson = txtItems.getText().trim();
                dto.special = txtSpecial.getText().trim();
                dto.mtv = 0;
                boolean ok = PanelManager.gI().saveOrUpdateGiftCode(dto);
                if (ok) {
                    JOptionPane.showMessageDialog(dlg, "Lưu giftcode thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    dlg.dispose();
                    refreshGiftcodesAsync();
                } else {
                    JOptionPane.showMessageDialog(dlg, "Lưu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Lỗi dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        g.gridx = 0; g.gridy = 7; g.gridwidth = 2;
        p.add(btnSave, g);

        dlg.setContentPane(p);
        dlg.setVisible(true);
    }

    // =========================================================================
    // TAB 6: GỬI THƯ & QUÀ MAIL
    // =========================================================================

    private JPanel createMailTab() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel formPanel = createTitledPanel("SOẠN THẢO THƯ HỆ THỐNG & QUÀ ĐÍNH KÈM");
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Target Radio
        JPanel targetGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        targetGroup.setOpaque(false);
        rbMailSingle = new JRadioButton("Gửi 1 Người Chơi Cụ Thể", true);
        rbMailOnline = new JRadioButton("Gửi Toàn Bộ Người Online", false);
        rbMailServerAll = new JRadioButton("Gửi Toàn Bộ Server (Database All)", false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMailSingle); bg.add(rbMailOnline); bg.add(rbMailServerAll);
        for (JRadioButton rb : new JRadioButton[]{rbMailSingle, rbMailOnline, rbMailServerAll}) {
            rb.setFont(FONT_BOLD);
            rb.setForeground(COLOR_TEXT);
            rb.setOpaque(false);
            targetGroup.add(rb);
        }

        g.gridx = 0; g.gridy = 0; formPanel.add(createLabel("Đối tượng nhận:"), g);
        g.gridx = 1; formPanel.add(targetGroup, g);

        txtMailTarget = createTextField("");
        g.gridx = 0; g.gridy = 1; formPanel.add(createLabel("Tên người nhận (nếu gửi 1 người):"), g);
        g.gridx = 1; formPanel.add(txtMailTarget, g);

        txtMailSender = createTextField("BQT Hải Tặc Tí Hon");
        g.gridx = 0; g.gridy = 2; formPanel.add(createLabel("Người gửi:"), g);
        g.gridx = 1; formPanel.add(txtMailSender, g);

        txtMailTitle = createTextField("Quà Tặng Từ Ban Quản Trị");
        g.gridx = 0; g.gridy = 3; formPanel.add(createLabel("Tiêu đề thư:"), g);
        g.gridx = 1; formPanel.add(txtMailTitle, g);

        txtMailContent = new JTextArea(3, 30);
        txtMailContent.setBackground(COLOR_INPUT_BG);
        txtMailContent.setForeground(COLOR_TEXT);
        txtMailContent.setFont(FONT_REGULAR);
        txtMailContent.setText("Chúc bạn có những giờ phút trải nghiệm game thật vui vẻ!");
        g.gridx = 0; g.gridy = 4; formPanel.add(createLabel("Nội dung thư:"), g);
        g.gridx = 1; formPanel.add(new JScrollPane(txtMailContent), g);

        txtMailExpireDays = createTextField("14");
        g.gridx = 0; g.gridy = 5; formPanel.add(createLabel("Hạn nhận (ngày):"), g);
        g.gridx = 1; formPanel.add(txtMailExpireDays, g);

        panel.add(formPanel, BorderLayout.NORTH);

        // Gift Builder Center
        JPanel giftBuilderPanel = createTitledPanel("DANH SÁCH QUÀ ĐÍNH KÈM (GIFTS)");
        giftBuilderPanel.setLayout(new BorderLayout(8, 8));

        JPanel addGiftBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        addGiftBar.setOpaque(false);

        JComboBox<String> cbGiftType = new JComboBox<>(new String[]{"Beri (Vàng)", "Ruby (Ngọc)", "Extol (VND)", "Kinh Nghiệm (EXP)", "Item 3 (Trang bị)", "Item 4", "Item 7"});
        cbGiftType.setBackground(COLOR_INPUT_BG);
        cbGiftType.setForeground(COLOR_TEXT);
        addGiftBar.add(cbGiftType);

        addGiftBar.add(createLabel("ID:"));
        JTextField txtGiftId = createTextField("0");
        txtGiftId.setPreferredSize(new Dimension(60, 26));
        addGiftBar.add(txtGiftId);

        addGiftBar.add(createLabel("Số lượng:"));
        JTextField txtGiftQuant = createTextField("1000000");
        txtGiftQuant.setPreferredSize(new Dimension(100, 26));
        addGiftBar.add(txtGiftQuant);

        JButton btnAddGift = createStyledButton("+ Thêm Quà", COLOR_GREEN);
        btnAddGift.addActionListener(e -> {
            try {
                int typeIdx = cbGiftType.getSelectedIndex();
                int id = Integer.parseInt(txtGiftId.getText().trim());
                int quant = Integer.parseInt(txtGiftQuant.getText().trim());
                GiftBox gb = new GiftBox();

                switch (typeIdx) {
                    case 0: // Beri
                        ItemTemplate4 tBeri = ItemTemplate4.get_it_by_id(0);
                        gb = new GiftBox(tBeri, quant);
                        break;
                    case 1: // Ruby
                        ItemTemplate4 tRuby = ItemTemplate4.get_it_by_id(1);
                        gb = new GiftBox(tRuby, quant);
                        break;
                    case 2: // Extol
                        ItemTemplate4 tExtol = ItemTemplate4.get_it_by_id(908);
                        gb = new GiftBox(tExtol, quant);
                        break;
                    case 3: // EXP
                        gb.type = 99;
                        gb.id = 0;
                        gb.num = quant;
                        gb.name = "Kinh Nghiệm (" + quant + ")";
                        break;
                    case 4: // Item 3
                        ItemTemplate3 t3 = ItemTemplate3.get_it_by_id(id);
                        if (t3 != null) gb = new GiftBox(t3, quant);
                        else { JOptionPane.showMessageDialog(this, "Không tìm thấy Item 3 ID " + id, "Lỗi", JOptionPane.ERROR_MESSAGE); return; }
                        break;
                    case 5: // Item 4
                        ItemTemplate4 t4 = ItemTemplate4.get_it_by_id(id);
                        if (t4 != null) gb = new GiftBox(t4, quant);
                        else { JOptionPane.showMessageDialog(this, "Không tìm thấy Item 4 ID " + id, "Lỗi", JOptionPane.ERROR_MESSAGE); return; }
                        break;
                    case 6: // Item 7
                        ItemTemplate7 t7 = ItemTemplate7.get_it_by_id(id);
                        if (t7 != null) gb = new GiftBox(t7, quant);
                        else { JOptionPane.showMessageDialog(this, "Không tìm thấy Item 7 ID " + id, "Lỗi", JOptionPane.ERROR_MESSAGE); return; }
                        break;
                }

                currentMailGiftList.add(gb);
                modelMailGifts.addRow(new Object[]{gb.type, gb.id, gb.name, gb.num});
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        addGiftBar.add(btnAddGift);

        JButton btnClearGifts = createStyledButton("Xóa Tất Cả Quà", COLOR_RED);
        btnClearGifts.addActionListener(e -> {
            currentMailGiftList.clear();
            modelMailGifts.setRowCount(0);
        });
        addGiftBar.add(btnClearGifts);

        giftBuilderPanel.add(addGiftBar, BorderLayout.NORTH);

        modelMailGifts = new DefaultTableModel(new String[]{"Type", "ID", "Tên Phần Thưởng", "Số Lượng"}, 0);
        tblMailGifts = createStyledTable(modelMailGifts);
        giftBuilderPanel.add(new JScrollPane(tblMailGifts), BorderLayout.CENTER);

        panel.add(giftBuilderPanel, BorderLayout.CENTER);

        // Send Button Bottom
        JButton btnSendMail = createStyledButton("GỬI THƯ & QUÀ NGAY (SEND MAIL)", COLOR_ACCENT);
        btnSendMail.setFont(FONT_HEADER);
        btnSendMail.setPreferredSize(new Dimension(200, 42));
        btnSendMail.addActionListener(e -> {
            String sender = txtMailSender.getText().trim();
            String title = txtMailTitle.getText().trim();
            String content = txtMailContent.getText().trim();
            long days = 14;
            try { days = Long.parseLong(txtMailExpireDays.getText().trim()); } catch (Exception ignore) {}
            long durationMs = days * 24 * 3600 * 1000L;

            if (rbMailSingle.isSelected()) {
                String target = txtMailTarget.getText().trim();
                if (target.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tên người nhận!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean ok = PanelManager.gI().sendMailToPlayer(target, sender, title, content, new ArrayList<>(currentMailGiftList), durationMs);
                JOptionPane.showMessageDialog(this, ok ? "Đã gửi thư tới " + target + " thành công!" : "Gửi thư thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else if (rbMailOnline.isSelected()) {
                int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn gửi thư này tới TOÀN BỘ người chơi đang ONLINE?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (cf == JOptionPane.YES_OPTION) {
                    int sent = PanelManager.gI().sendMailToAllOnline(sender, title, content, new ArrayList<>(currentMailGiftList), durationMs);
                    JOptionPane.showMessageDialog(this, "Đã gửi thư tới " + sent + " người chơi online thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (rbMailServerAll.isSelected()) {
                int cf = JOptionPane.showConfirmDialog(this, "CẢNH BÁO: Gửi thư tới TOÀN BỘ tài khoản trong DATABASE?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (cf == JOptionPane.YES_OPTION) {
                    PanelManager.gI().sendMailToServerAll(sender, title, content, new ArrayList<>(currentMailGiftList), durationMs).thenAccept(total -> {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, "Đã gửi thư tới toàn bộ " + total + " tài khoản trong DB!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        });
                    });
                }
            }
        });

        panel.add(btnSendMail, BorderLayout.SOUTH);
        return panel;
    }

    // =========================================================================
    // TAB 7: CHỈNH SỬA TEMPLATE ITEM 3, PET & ÉP ĐỒ
    // =========================================================================

    private JPanel createItemAndPetEditorTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setFont(FONT_BOLD);
        subTabs.setBackground(COLOR_PANEL);
        subTabs.setForeground(COLOR_TEXT);

        // SubTab 1: Item 3 Editor
        subTabs.addTab("Chỉnh Sửa Template Item 3", createItem3EditorPanel());

        // SubTab 2: Pet Editor
        subTabs.addTab("Chỉnh Sửa Template Pet", createPetEditorPanel());

        // SubTab 3: Custom God Item Builder
        subTabs.addTab("Tạo Đồ Thần Trang / Đồ Custom Cho Player", createCustomItemBuilderPanel());

        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createItem3EditorPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setOpaque(false);
        topBar.add(createLabel("Tìm kiếm Item 3:"));
        txtSearchItem3 = createTextField("");
        txtSearchItem3.setPreferredSize(new Dimension(180, 26));
        txtSearchItem3.addActionListener(e -> searchItem3Async());
        topBar.add(txtSearchItem3);

        JButton btnSearch = createStyledButton("Tìm", COLOR_ACCENT);
        btnSearch.addActionListener(e -> searchItem3Async());
        topBar.add(btnSearch);

        panel.add(topBar, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(380);
        split.setOpaque(false);
        split.setBorder(null);

        modelItem3Templates = new DefaultTableModel(new String[]{"ID", "Tên Trang Bị", "Phái", "Type", "Cấp", "Màu"}, 0);
        tblItem3Templates = createStyledTable(modelItem3Templates);
        tblItem3Templates.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblItem3Templates.getSelectedRow() >= 0) {
                int id = Integer.parseInt(tblItem3Templates.getValueAt(tblItem3Templates.getSelectedRow(), 0).toString());
                loadItem3Detail(id);
            }
        });
        split.setLeftComponent(new JScrollPane(tblItem3Templates));

        // Right Editor Form
        JPanel form = createTitledPanel("THÔNG SỐ TEMPLATE ITEM 3");
        form.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtItem3Id = createTextField(""); txtItem3Id.setEditable(false);
        txtItem3Name = createTextField("");
        txtItem3Class = createTextField("0");
        txtItem3Type = createTextField("0");
        txtItem3Icon = createTextField("0");
        txtItem3Level = createTextField("1");
        txtItem3Color = createTextField("0");
        txtItem3Beri = createTextField("0");
        txtItem3Lock = createTextField("0");
        txtItem3Holes = createTextField("0");
        txtItem3HoanMy = createTextField("0");
        txtItem3Part = createTextField("-1");
        txtItem3LoKham = createTextField("0");
        txtItem3Op1 = new JTextArea(3, 20); txtItem3Op1.setBackground(COLOR_INPUT_BG); txtItem3Op1.setForeground(COLOR_TEXT); txtItem3Op1.setFont(FONT_MONO);
        txtItem3Op2 = new JTextArea(2, 20); txtItem3Op2.setBackground(COLOR_INPUT_BG); txtItem3Op2.setForeground(COLOR_TEXT); txtItem3Op2.setFont(FONT_MONO);
        txtItem3Mdakham = new JTextArea(1, 20); txtItem3Mdakham.setBackground(COLOR_INPUT_BG); txtItem3Mdakham.setForeground(COLOR_TEXT); txtItem3Mdakham.setFont(FONT_MONO);

        g.gridx = 0; g.gridy = 0; form.add(createLabel("ID:"), g); g.gridx = 1; form.add(txtItem3Id, g);
        g.gridx = 0; g.gridy = 1; form.add(createLabel("Tên:"), g); g.gridx = 1; form.add(txtItem3Name, g);
        g.gridx = 0; g.gridy = 2; form.add(createLabel("Phái (Class):"), g); g.gridx = 1; form.add(txtItem3Class, g);
        g.gridx = 0; g.gridy = 3; form.add(createLabel("Type Trang Bị:"), g); g.gridx = 1; form.add(txtItem3Type, g);
        g.gridx = 0; g.gridy = 4; form.add(createLabel("Icon ID:"), g); g.gridx = 1; form.add(txtItem3Icon, g);
        g.gridx = 0; g.gridy = 5; form.add(createLabel("Cấp Yêu Cầu:"), g); g.gridx = 1; form.add(txtItem3Level, g);
        g.gridx = 0; g.gridy = 6; form.add(createLabel("Màu Sắc (Color 0-8):"), g); g.gridx = 1; form.add(txtItem3Color, g);
        g.gridx = 0; g.gridy = 7; form.add(createLabel("Số Lỗ Đục:"), g); g.gridx = 1; form.add(txtItem3Holes, g);
        g.gridx = 0; g.gridy = 8; form.add(createLabel("Option 1 (id,param):"), g); g.gridx = 1; form.add(new JScrollPane(txtItem3Op1), g);
        g.gridx = 0; g.gridy = 9; form.add(createLabel("Option 2 Kích Ẩn:"), g); g.gridx = 1; form.add(new JScrollPane(txtItem3Op2), g);

        JButton btnSaveItem3 = createStyledButton("LƯU ITEM 3 VÀO DB VÀ RELOAD RAM NGAY", COLOR_GREEN);
        btnSaveItem3.addActionListener(e -> saveCurrentItem3Template());
        g.gridx = 0; g.gridy = 10; g.gridwidth = 2; form.add(btnSaveItem3, g);

        split.setRightComponent(new JScrollPane(form));
        panel.add(split, BorderLayout.CENTER);

        return panel;
    }

    private void searchItem3Async() {
        String q = txtSearchItem3.getText().trim();
        CompletableFuture.supplyAsync(() -> PanelManager.gI().searchItem3Templates(q, 100))
                .thenAccept(list -> SwingUtilities.invokeLater(() -> {
                    modelItem3Templates.setRowCount(0);
                    for (Item3TemplateDTO d : list) {
                        modelItem3Templates.addRow(new Object[]{d.id, d.name, d.clazz, d.typeEquip, d.level, getColorName(d.color)});
                    }
                }));
    }

    private void loadItem3Detail(int id) {
        List<Item3TemplateDTO> list = PanelManager.gI().searchItem3Templates(String.valueOf(id), 1);
        if (list.isEmpty()) return;
        Item3TemplateDTO d = list.get(0);
        txtItem3Id.setText(String.valueOf(d.id));
        txtItem3Name.setText(d.name);
        txtItem3Class.setText(String.valueOf(d.clazz));
        txtItem3Type.setText(String.valueOf(d.typeEquip));
        txtItem3Icon.setText(String.valueOf(d.icon));
        txtItem3Level.setText(String.valueOf(d.level));
        txtItem3Color.setText(String.valueOf(d.color));
        txtItem3Holes.setText(String.valueOf(d.numHoleDaDuc));

        StringBuilder sb1 = new StringBuilder();
        for (OptionDTO op : d.op1) sb1.append(op.id).append(",").append(op.param).append("\n");
        txtItem3Op1.setText(sb1.toString());

        StringBuilder sb2 = new StringBuilder();
        for (OptionDTO op : d.op2) sb2.append(op.id).append(",").append(op.param).append("\n");
        txtItem3Op2.setText(sb2.toString());
    }

    private void saveCurrentItem3Template() {
        try {
            Item3TemplateDTO dto = new Item3TemplateDTO();
            dto.id = Integer.parseInt(txtItem3Id.getText().trim());
            dto.name = txtItem3Name.getText().trim();
            dto.clazz = Integer.parseInt(txtItem3Class.getText().trim());
            dto.typeEquip = Integer.parseInt(txtItem3Type.getText().trim());
            dto.icon = Integer.parseInt(txtItem3Icon.getText().trim());
            dto.level = Integer.parseInt(txtItem3Level.getText().trim());
            dto.color = Integer.parseInt(txtItem3Color.getText().trim());
            dto.numHoleDaDuc = Integer.parseInt(txtItem3Holes.getText().trim());

            for (String line : txtItem3Op1.getText().split("\n")) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split("[,:]");
                if (p.length >= 2) dto.op1.add(new OptionDTO(Integer.parseInt(p[0].trim()), "", Integer.parseInt(p[1].trim())));
            }
            for (String line : txtItem3Op2.getText().split("\n")) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split("[,:]");
                if (p.length >= 2) dto.op2.add(new OptionDTO(Integer.parseInt(p[0].trim()), "", Integer.parseInt(p[1].trim())));
            }

            boolean ok = PanelManager.gI().saveItem3Template(dto);
            JOptionPane.showMessageDialog(this, ok ? "Đã lưu template Item 3 ID " + dto.id + " thành công!" : "Lưu thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            searchItem3Async();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createPetEditorPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setOpaque(false);
        topBar.add(createLabel("Tìm kiếm Pet:"));
        txtSearchPet = createTextField("");
        txtSearchPet.setPreferredSize(new Dimension(180, 26));
        txtSearchPet.addActionListener(e -> searchPetsAsync());
        topBar.add(txtSearchPet);

        JButton btnSearch = createStyledButton("Tìm", COLOR_ACCENT);
        btnSearch.addActionListener(e -> searchPetsAsync());
        topBar.add(btnSearch);

        panel.add(topBar, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(380);
        split.setOpaque(false);
        split.setBorder(null);

        modelPetTemplates = new DefaultTableModel(new String[]{"ID", "Tên Thú Cưng / Pet", "Icon", "Frame", "Type"}, 0);
        tblPetTemplates = createStyledTable(modelPetTemplates);
        tblPetTemplates.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblPetTemplates.getSelectedRow() >= 0) {
                int id = Integer.parseInt(tblPetTemplates.getValueAt(tblPetTemplates.getSelectedRow(), 0).toString());
                loadPetDetail(id);
            }
        });
        split.setLeftComponent(new JScrollPane(tblPetTemplates));

        // Pet Editor Form
        JPanel form = createTitledPanel("THÔNG SỐ & CHỈ SỐ BUFF PET");
        form.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtPetId = createTextField(""); txtPetId.setEditable(false);
        txtPetName = createTextField("");
        txtPetType = createTextField("0");
        txtPetIcon = createTextField("0");
        txtPetFrame = createTextField("0");
        txtPetOptions = new JTextArea(5, 20); txtPetOptions.setBackground(COLOR_INPUT_BG); txtPetOptions.setForeground(COLOR_TEXT); txtPetOptions.setFont(FONT_MONO);

        g.gridx = 0; g.gridy = 0; form.add(createLabel("ID:"), g); g.gridx = 1; form.add(txtPetId, g);
        g.gridx = 0; g.gridy = 1; form.add(createLabel("Tên Pet:"), g); g.gridx = 1; form.add(txtPetName, g);
        g.gridx = 0; g.gridy = 2; form.add(createLabel("Type:"), g); g.gridx = 1; form.add(txtPetType, g);
        g.gridx = 0; g.gridy = 3; form.add(createLabel("Icon ID:"), g); g.gridx = 1; form.add(txtPetIcon, g);
        g.gridx = 0; g.gridy = 4; form.add(createLabel("Frame ID:"), g); g.gridx = 1; form.add(txtPetFrame, g);
        g.gridx = 0; g.gridy = 5; form.add(createLabel("Chỉ số Option (id,param):"), g); g.gridx = 1; form.add(new JScrollPane(txtPetOptions), g);

        JButton btnSavePet = createStyledButton("LƯU PET VÀO DB VÀ RELOAD RAM NGAY", COLOR_GREEN);
        btnSavePet.addActionListener(e -> saveCurrentPetTemplate());
        g.gridx = 0; g.gridy = 6; g.gridwidth = 2; form.add(btnSavePet, g);

        split.setRightComponent(new JScrollPane(form));
        panel.add(split, BorderLayout.CENTER);

        return panel;
    }

    private void searchPetsAsync() {
        String q = txtSearchPet.getText().trim();
        CompletableFuture.supplyAsync(() -> PanelManager.gI().searchPetTemplates(q))
                .thenAccept(list -> SwingUtilities.invokeLater(() -> {
                    modelPetTemplates.setRowCount(0);
                    for (PetTemplateDTO d : list) {
                        modelPetTemplates.addRow(new Object[]{d.id, d.name, d.icon, d.frame, d.type});
                    }
                }));
    }

    private void loadPetDetail(int id) {
        List<PetTemplateDTO> list = PanelManager.gI().searchPetTemplates(String.valueOf(id));
        if (list.isEmpty()) return;
        PetTemplateDTO d = list.get(0);
        txtPetId.setText(String.valueOf(d.id));
        txtPetName.setText(d.name);
        txtPetType.setText(String.valueOf(d.type));
        txtPetIcon.setText(String.valueOf(d.icon));
        txtPetFrame.setText(String.valueOf(d.frame));

        StringBuilder sb = new StringBuilder();
        for (OptionDTO op : d.options) sb.append(op.id).append(",").append(op.param).append("  // ").append(op.name).append("\n");
        txtPetOptions.setText(sb.toString());
    }

    private void saveCurrentPetTemplate() {
        try {
            PetTemplateDTO dto = new PetTemplateDTO();
            dto.id = Integer.parseInt(txtPetId.getText().trim());
            dto.name = txtPetName.getText().trim();
            dto.type = Integer.parseInt(txtPetType.getText().trim());
            dto.icon = Integer.parseInt(txtPetIcon.getText().trim());
            dto.frame = Integer.parseInt(txtPetFrame.getText().trim());

            for (String line : txtPetOptions.getText().split("\n")) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.contains("//")) line = line.substring(0, line.indexOf("//")).trim();
                String[] p = line.split("[,:]");
                if (p.length >= 2) dto.options.add(new OptionDTO(Integer.parseInt(p[0].trim()), "", Integer.parseInt(p[1].trim())));
            }

            boolean ok = PanelManager.gI().savePetTemplate(dto);
            JOptionPane.showMessageDialog(this, ok ? "Đã lưu Pet ID " + dto.id + " thành công!" : "Lưu thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            searchPetsAsync();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCustomItemBuilderPanel() {
        JPanel panel = createTitledPanel("TẠO TRANG BỊ THẦN TRANG / GOD-ITEM GỬI THẲNG CHO PLAYER");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtCustomItemTargetPlayer = createTextField("");
        txtCustomItemTemplateId = createTextField("12003"); // Dial Thần Thoại mặc định

        cbCustomItemColor = new JComboBox<>(new String[]{"Trắng (0)", "Xanh (1)", "Vàng (2)", "Tím (3)", "Cam (4)", "Đỏ (5)", "Thần Trang (8)"});
        cbCustomItemColor.setSelectedIndex(6); // Thần trang
        cbCustomItemColor.setBackground(COLOR_INPUT_BG); cbCustomItemColor.setForeground(COLOR_TEXT);

        cbCustomItemLevelUp = new JComboBox<>(new String[]{"+0", "+1", "+2", "+3", "+4", "+5", "+6", "+7", "+8", "+9", "+10", "+11", "+12", "+13", "+14", "+15", "+16 (MAX)"});
        cbCustomItemLevelUp.setSelectedIndex(16);
        cbCustomItemLevelUp.setBackground(COLOR_INPUT_BG); cbCustomItemLevelUp.setForeground(COLOR_TEXT);

        chkCustomItemHoanMy = new JCheckBox("Trang Bị Hoàn Mỹ (x1.1 Chỉ Số)", true);
        chkCustomItemHoanMy.setFont(FONT_BOLD); chkCustomItemHoanMy.setForeground(COLOR_YELLOW); chkCustomItemHoanMy.setOpaque(false);

        cbCustomItemKichAn = new JComboBox<>(new String[]{"Không", "Ẩn 0", "Ẩn 1", "Ẩn 2", "Ẩn 3", "Ẩn 4", "Ẩn 5", "Ẩn 6", "Ẩn 7", "Ẩn 8", "Ẩn 9", "Ẩn 10", "Ẩn 11", "Ẩn 12"});
        cbCustomItemKichAn.setSelectedIndex(0);
        cbCustomItemKichAn.setBackground(COLOR_INPUT_BG); cbCustomItemKichAn.setForeground(COLOR_TEXT);

        txtCustomItemOptions = new JTextArea(4, 25);
        txtCustomItemOptions.setBackground(COLOR_INPUT_BG); txtCustomItemOptions.setForeground(COLOR_TEXT); txtCustomItemOptions.setFont(FONT_MONO);
        txtCustomItemOptions.setText("0,50000  // Tấn công vật lý +50,000\n1,50000  // Tấn công ma thuật +50,000\n4,100000 // HP +100,000\n14,5000  // Chí mạng +50.00%\n26,3000  // Xuyên giáp +30.00%");

        g.gridx = 0; g.gridy = 0; panel.add(createLabel("Tên người chơi nhận:"), g);
        g.gridx = 1; panel.add(txtCustomItemTargetPlayer, g);

        g.gridx = 0; g.gridy = 1; panel.add(createLabel("Template ID Item 3:"), g);
        g.gridx = 1; panel.add(txtCustomItemTemplateId, g);

        g.gridx = 0; g.gridy = 2; panel.add(createLabel("Phẩm chất (Color):"), g);
        g.gridx = 1; panel.add(cbCustomItemColor, g);

        g.gridx = 0; g.gridy = 3; panel.add(createLabel("Cấp cường hóa:"), g);
        g.gridx = 1; panel.add(cbCustomItemLevelUp, g);

        g.gridx = 0; g.gridy = 4; panel.add(createLabel("Thuộc tính hoàn mỹ:"), g);
        g.gridx = 1; panel.add(chkCustomItemHoanMy, g);

        g.gridx = 0; g.gridy = 5; panel.add(createLabel("Kích ẩn:"), g);
        g.gridx = 1; panel.add(cbCustomItemKichAn, g);

        g.gridx = 0; g.gridy = 6; panel.add(createLabel("Chỉ số Options Custom:"), g);
        g.gridx = 1; panel.add(new JScrollPane(txtCustomItemOptions), g);

        JButton btnGiveCustom = createStyledButton("TẠO VÀ GỬI TRANG BỊ NGAY VÀO TÚI PLAYER", COLOR_ORANGE);
        btnGiveCustom.setFont(FONT_SUBHEADER);
        btnGiveCustom.addActionListener(e -> {
            try {
                String target = txtCustomItemTargetPlayer.getText().trim();
                if (target.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tên người chơi nhận!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int tplId = Integer.parseInt(txtCustomItemTemplateId.getText().trim());
                int color = cbCustomItemColor.getSelectedIndex() == 6 ? 8 : cbCustomItemColor.getSelectedIndex();
                int lvlUp = cbCustomItemLevelUp.getSelectedIndex();
                int hm = chkCustomItemHoanMy.isSelected() ? 1 : 0;
                int ka = cbCustomItemKichAn.getSelectedIndex() - 1;

                List<Option> ops = new ArrayList<>();
                for (String line : txtCustomItemOptions.getText().split("\n")) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    if (line.contains("//")) line = line.substring(0, line.indexOf("//")).trim();
                    String[] p = line.split("[,:]");
                    if (p.length >= 2) ops.add(new Option(Integer.parseInt(p[0].trim()), Integer.parseInt(p[1].trim())));
                }

                short[] daKham = new short[]{367, 367, 367, 367, 367, 367, 367, 367}; // 8 viên đá cấp 6 max
                boolean ok = PanelManager.gI().giveCustomItem3(target, tplId, color, lvlUp, hm, ka, ops, null, daKham);
                JOptionPane.showMessageDialog(this, ok ? "Đã gửi trang bị đặc biệt cho " + target + " thành công!" : "Gửi thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        g.gridx = 0; g.gridy = 7; g.gridwidth = 2;
        panel.add(btnGiveCustom, g);

        return panel;
    }

    // =========================================================================
    // TAB 8: TRA CỨU NHẬT KÝ SQL HISTORYS
    // =========================================================================

    private JPanel createHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Filters Bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterBar.setOpaque(false);

        filterBar.add(createLabel("AccID:"));
        txtHistAccId = createTextField(""); txtHistAccId.setPreferredSize(new Dimension(65, 26));
        filterBar.add(txtHistAccId);

        filterBar.add(createLabel("PlayerID:"));
        txtHistPlayerId = createTextField(""); txtHistPlayerId.setPreferredSize(new Dimension(65, 26));
        filterBar.add(txtHistPlayerId);

        filterBar.add(createLabel("Type:"));
        cbHistType = new JComboBox<>(new String[]{"TẤT CẢ", "MAIL_BOX", "TRADE", "MARKET", "VONG_QUAY_OC_SEN", "AUTH", "BUG", "EVENT_DATA", "ADMIN_PANEL", "ADMIN_BUFF", "NAP_THE"});
        cbHistType.setBackground(COLOR_INPUT_BG); cbHistType.setForeground(COLOR_TEXT);
        filterBar.add(cbHistType);

        filterBar.add(createLabel("Từ khóa:"));
        txtHistKeyword = createTextField(""); txtHistKeyword.setPreferredSize(new Dimension(140, 26));
        filterBar.add(txtHistKeyword);

        JButton btnSearchHist = createStyledButton("Tìm Nhật Ký", COLOR_ACCENT);
        btnSearchHist.addActionListener(e -> { historyCurrentOffset = 0; refreshHistoryAsync(); });
        filterBar.add(btnSearchHist);

        JButton btnPrev = createStyledButton("< Trước", COLOR_CARD);
        btnPrev.addActionListener(e -> {
            if (historyCurrentOffset >= HISTORY_LIMIT) {
                historyCurrentOffset -= HISTORY_LIMIT;
                refreshHistoryAsync();
            }
        });
        filterBar.add(btnPrev);

        JButton btnNext = createStyledButton("Sau >", COLOR_CARD);
        btnNext.addActionListener(e -> {
            historyCurrentOffset += HISTORY_LIMIT;
            refreshHistoryAsync();
        });
        filterBar.add(btnNext);

        panel.add(filterBar, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setDividerLocation(380);
        split.setOpaque(false);

        String[] cols = {"ID", "AccID", "PlayerID", "Loại (Type)", "Thời Gian", "Dữ Liệu Tóm Tắt"};
        modelHistory = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHistory = createStyledTable(modelHistory);
        tblHistory.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblHistory.getSelectedRow() >= 0) {
                int row = tblHistory.getSelectedRow();
                String data = tblHistory.getValueAt(row, 5).toString();
                txtHistDetail.setText(formatJsonPretty(data));
            }
        });
        split.setTopComponent(new JScrollPane(tblHistory));

        txtHistDetail = new JTextArea();
        txtHistDetail.setBackground(new Color(10, 15, 26));
        txtHistDetail.setForeground(COLOR_GREEN);
        txtHistDetail.setFont(FONT_MONO);
        txtHistDetail.setEditable(false);
        split.setBottomComponent(new JScrollPane(txtHistDetail));

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private String formatJsonPretty(String raw) {
        if (raw == null) return "";
        try {
            Object obj = org.json.simple.JSONValue.parse(raw);
            if (obj != null) return obj.toString();
        } catch (Exception ignore) {}
        return raw;
    }

    // =========================================================================
    // TAB 9: BOSS & ĐỆ HOANG & SIÊU TRÙM TAB
    // =========================================================================

    private JPanel createBossAndWildManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. STAT CARDS (NORTH)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 12, 12));
        statsPanel.setOpaque(false);

        lblStatSuperBossLive = new JLabel("0 / 10", JLabel.CENTER);
        lblStatWorldBossLive = new JLabel("0", JLabel.CENTER);
        lblStatWildDeTuLive = new JLabel("0", JLabel.CENTER);
        lblStatTotalEntities = new JLabel("0", JLabel.CENTER);

        statsPanel.add(createStatCard("SIÊU TRÙM ĐANG SỐNG", lblStatSuperBossLive, COLOR_RED));
        statsPanel.add(createStatCard("BOSS THƯỜNG & THẾ GIỚI", lblStatWorldBossLive, COLOR_ORANGE));
        statsPanel.add(createStatCard("ĐỆ TỬ HOANG (SERVER)", lblStatWildDeTuLive, COLOR_GREEN));
        statsPanel.add(createStatCard("TỔNG THỰC THỂ QUẢN LÝ", lblStatTotalEntities, COLOR_ACCENT));

        panel.add(statsPanel, BorderLayout.NORTH);

        // 2. CENTER: JSplitPane (Top = Live Table & Actions, Bottom = Supreme Spawning Controls)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setOpaque(false);
        splitPane.setDividerLocation(360);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);

        // --- TOP: LIVE TRACKER TABLE ---
        JPanel tableContainer = createTitledPanel("THEO DÕI VỊ TRÍ REALTIME: BOSS, SIÊU TRÙM & ĐỆ HOANG");
        tableContainer.setLayout(new BorderLayout(8, 8));

        // Filter Bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterBar.setOpaque(false);

        filterBar.add(createLabel("Loại:"));
        cbBossCategoryFilter = new JComboBox<>(new String[]{"Tất Cả", "SIÊU TRÙM", "BOSS THƯỜNG", "BOSS THẾ GIỚI", "ĐỆ HOANG"});
        cbBossCategoryFilter.setBackground(COLOR_INPUT_BG);
        cbBossCategoryFilter.setForeground(COLOR_TEXT);
        cbBossCategoryFilter.addActionListener(e -> applyBossFilter());
        filterBar.add(cbBossCategoryFilter);

        filterBar.add(createLabel("Trạng thái:"));
        cbBossStatusFilter = new JComboBox<>(new String[]{"Tất Cả", "Chỉ Đang Sống", "Chỉ Chưa Xuất Hiện / Đã Chết"});
        cbBossStatusFilter.setBackground(COLOR_INPUT_BG);
        cbBossStatusFilter.setForeground(COLOR_TEXT);
        cbBossStatusFilter.addActionListener(e -> applyBossFilter());
        filterBar.add(cbBossStatusFilter);

        filterBar.add(createLabel("Tìm kiếm:"));
        txtBossSearch = createTextField("");
        txtBossSearch.setPreferredSize(new Dimension(160, 28));
        txtBossSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyBossFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyBossFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyBossFilter(); }
        });
        filterBar.add(txtBossSearch);

        JButton btnRefreshBoss = createStyledButton("Làm Mới (F5)", COLOR_ACCENT);
        btnRefreshBoss.addActionListener(e -> refreshBossAndWildAsync());
        filterBar.add(btnRefreshBoss);

        tableContainer.add(filterBar, BorderLayout.NORTH);

        // Table
        String[] cols = new String[]{"Loại", "ID", "Tên Thực Thể", "Tên Map (ID)", "Khu", "Tọa Độ", "HP / Max HP", "Cấp", "Trạng Thái"};
        modelBossAndWild = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblBossAndWild = createStyledTable(modelBossAndWild);
        JScrollPane scrollTable = new JScrollPane(tblBossAndWild);
        scrollTable.getViewport().setBackground(COLOR_PANEL);
        scrollTable.setBorder(new LineBorder(COLOR_CARD, 1));
        tableContainer.add(scrollTable, BorderLayout.CENTER);

        // Table Action Bar
        JPanel tblActionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tblActionBar.setOpaque(false);

        JButton btnTeleport = createStyledButton("Bay Đến Vị Trí", COLOR_GREEN);
        btnTeleport.addActionListener(e -> handleTeleportToSelectedEntity());
        tblActionBar.add(btnTeleport);

        JButton btnKill = createStyledButton("Tiêu Diệt / Xóa", COLOR_RED);
        btnKill.addActionListener(e -> handleKillSelectedEntity());
        tblActionBar.add(btnKill);

        JButton btnClearAllWild = createStyledButton("Dọn Dẹp All Đệ Hoang", COLOR_ORANGE);
        btnClearAllWild.addActionListener(e -> {
            int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn dọn dẹp toàn bộ Đệ Tử Hoang trên server?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (cf == JOptionPane.YES_OPTION) {
                PanelManager.gI().clearAllWildDeTuAsync().thenAccept(c -> {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Đã dọn dẹp " + c + " đệ tử hoang!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        refreshBossAndWildAsync();
                    });
                });
            }
        });
        tblActionBar.add(btnClearAllWild);

        JButton btnKillAllBosses = createStyledButton("Tiêu Diệt All Boss", COLOR_RED);
        btnKillAllBosses.addActionListener(e -> {
            int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn tiêu diệt toàn bộ Boss đang sống?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (cf == JOptionPane.YES_OPTION) {
                PanelManager.gI().killAllBossesAsync().thenAccept(c -> {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Đã tiêu diệt " + c + " boss đang sống!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        refreshBossAndWildAsync();
                    });
                });
            }
        });
        tblActionBar.add(btnKillAllBosses);

        tableContainer.add(tblActionBar, BorderLayout.SOUTH);
        splitPane.setTopComponent(tableContainer);

        // --- BOTTOM: SUPREME SPAWN CONTROLLER ---
        JPanel spawnContainer = new JPanel(new GridLayout(1, 2, 12, 12));
        spawnContainer.setOpaque(false);

        // Sub-Panel 1: Random Spawn (Không trùng map/khu)
        JPanel randomSpawnPanel = createTitledPanel("1. SPAWN RANDOM SỐ LƯỢNG (KHÔNG TRÙNG MAP / KHU)");
        randomSpawnPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        randomSpawnPanel.add(createLabel("Loại Thực Thể:"), gbc);
        gbc.gridx = 1;
        cbRandomSpawnType = new JComboBox<>(new String[]{"Đệ Tử Hoang", "Siêu Trùm", "Boss Thường / Thế Giới"});
        cbRandomSpawnType.setBackground(COLOR_INPUT_BG);
        cbRandomSpawnType.setForeground(COLOR_TEXT);
        randomSpawnPanel.add(cbRandomSpawnType, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        randomSpawnPanel.add(createLabel("Số Lượng Cần Spawn:"), gbc);
        gbc.gridx = 1;
        txtRandomSpawnCount = createTextField("5");
        randomSpawnPanel.add(txtRandomSpawnCount, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel chkPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chkPanel1.setOpaque(false);
        chkNoDupMap = new JCheckBox("Không trùng Map", true);
        chkNoDupMap.setOpaque(false);
        chkNoDupMap.setFont(FONT_BOLD);
        chkNoDupMap.setForeground(COLOR_GREEN);
        chkNoDupZone = new JCheckBox("Không trùng Khu", true);
        chkNoDupZone.setOpaque(false);
        chkNoDupZone.setFont(FONT_BOLD);
        chkNoDupZone.setForeground(COLOR_ACCENT);
        chkRandomKtg = new JCheckBox("Thông Báo KTG", true);
        chkRandomKtg.setOpaque(false);
        chkRandomKtg.setFont(FONT_BOLD);
        chkRandomKtg.setForeground(COLOR_YELLOW);
        chkPanel1.add(chkNoDupMap);
        chkPanel1.add(chkNoDupZone);
        chkPanel1.add(chkRandomKtg);
        randomSpawnPanel.add(chkPanel1, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton btnExecuteRandomSpawn = createStyledButton("SPAWN RANDOM NGAY", COLOR_GREEN);
        btnExecuteRandomSpawn.addActionListener(e -> handleExecuteRandomSpawn());
        randomSpawnPanel.add(btnExecuteRandomSpawn, gbc);

        spawnContainer.add(randomSpawnPanel);

        // Sub-Panel 2: Custom Spawn (Chỉ định Map / Khu hoặc Random)
        JPanel customSpawnPanel = createTitledPanel("2. SPAWN TÙY CHỈNH (NHẬP MAP & KHU HOẶC ĐỂ TRỐNG = RANDOM)");
        customSpawnPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4, 6, 4, 6);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.anchor = GridBagConstraints.WEST;

        gbc2.gridx = 0; gbc2.gridy = 0;
        customSpawnPanel.add(createLabel("Đối Tượng:"), gbc2);
        gbc2.gridx = 1;
        cbSpecificEntity = new JComboBox<>(new String[]{
            "Đệ Tử Hoang (Random Phái)",
            "Đệ Tử Hoang (1. Kiếm Sĩ)",
            "Đệ Tử Hoang (2. Xạ Thủ)",
            "Đệ Tử Hoang (3. Võ Sĩ)",
            "Đệ Tử Hoang (4. Đầu Bếp)",
            "Đệ Tử Hoang (5. Hoa Tiêu)",
            "Siêu Trùm - Buggi (16)",
            "Siêu Trùm - Kurol (23)",
            "Siêu Trùm - Mr3 (135)",
            "Siêu Trùm - Wapol (136)",
            "Siêu Trùm - Smoker (136)",
            "Siêu Trùm - Crocodile (137)",
            "Siêu Trùm - Enel (139)",
            "Siêu Trùm - Lucci Báo (163)",
            "Siêu Trùm - Along (168)",
            "Siêu Trùm - Zoombie (168)",
            "Boss Thế Giới (172)",
            "Boss Pica (173)"
        });
        cbSpecificEntity.setBackground(COLOR_INPUT_BG);
        cbSpecificEntity.setForeground(COLOR_TEXT);
        customSpawnPanel.add(cbSpecificEntity, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 1;
        customSpawnPanel.add(createLabel("Map ID (Bỏ trống = Random):"), gbc2);
        gbc2.gridx = 1;
        txtCustomMapId = createTextField("");
        customSpawnPanel.add(txtCustomMapId, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 2;
        customSpawnPanel.add(createLabel("Khu (1, 2... | Trống = Tự động):"), gbc2);
        gbc2.gridx = 1;
        txtCustomZoneId = createTextField("");
        customSpawnPanel.add(txtCustomZoneId, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 3;
        customSpawnPanel.add(createLabel("Số Lượng:"), gbc2);
        gbc2.gridx = 1;
        txtCustomCount = createTextField("1");
        customSpawnPanel.add(txtCustomCount, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 4; gbc2.gridwidth = 2;
        JPanel chkPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chkPanel2.setOpaque(false);
        chkCustomAllZones = new JCheckBox("Spawn All Khu (Mỗi khu [SL] đệ)", false);
        chkCustomAllZones.setOpaque(false);
        chkCustomAllZones.setFont(FONT_BOLD);
        chkCustomAllZones.setForeground(COLOR_GREEN);
        chkCustomAllZones.addActionListener(e -> {
            if (chkCustomAllZones.isSelected()) {
                txtCustomZoneId.setEnabled(false);
                txtCustomZoneId.setText("Tất cả khu");
            } else {
                txtCustomZoneId.setEnabled(true);
                txtCustomZoneId.setText("");
            }
        });
        chkPanel2.add(chkCustomAllZones);

        chkCustomKtg = new JCheckBox("Thông Báo KTG", true);
        chkCustomKtg.setOpaque(false);
        chkCustomKtg.setFont(FONT_BOLD);
        chkCustomKtg.setForeground(COLOR_YELLOW);
        chkPanel2.add(chkCustomKtg);
        customSpawnPanel.add(chkPanel2, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 5; gbc2.gridwidth = 2;
        JButton btnExecuteCustomSpawn = createStyledButton("TIẾN HÀNH SPAWN", COLOR_ACCENT);
        btnExecuteCustomSpawn.addActionListener(e -> handleExecuteCustomSpawn());
        customSpawnPanel.add(btnExecuteCustomSpawn, gbc2);

        spawnContainer.add(customSpawnPanel);
        splitPane.setBottomComponent(spawnContainer);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private void handleTeleportToSelectedEntity() {
        int row = tblBossAndWild.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 thực thể trên bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int entityId = Integer.parseInt(tblBossAndWild.getValueAt(row, 1).toString());
        String cat = tblBossAndWild.getValueAt(row, 0).toString();

        boss.BossAndWildManager.TrackedEntityDTO targetDTO = currentTrackedEntities.stream()
                .filter(e -> e.id == entityId && e.category.equals(cat))
                .findFirst().orElse(null);

        if (targetDTO == null || targetDTO.rawEntity == null) {
            JOptionPane.showMessageDialog(this, "Thực thể này hiện chưa xuất hiện trên bản đồ!", "Không thể dịch chuyển", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<model.Player> realPlayers = zinterfaces.menus.AdminSystemMenu.getRealOnlinePlayers();
        model.Player adminPlayer = realPlayers.stream().filter(zinterfaces.menus.AdminSystemMenu::isAdmin).findFirst().orElse(null);
        if (adminPlayer == null && !realPlayers.isEmpty()) {
            adminPlayer = realPlayers.get(0);
        }

        if (adminPlayer == null) {
            JOptionPane.showMessageDialog(this, "Không có Admin hoặc người chơi nào đang online để dịch chuyển!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.Player finalAdmin = adminPlayer;
        PanelManager.gI().teleportAdminToEntityAsync(finalAdmin, targetDTO.rawEntity).thenAccept(ok -> {
            SwingUtilities.invokeLater(() -> {
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Đã dịch chuyển [" + finalAdmin.name + "] đến cạnh " + targetDTO.name + "!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Dịch chuyển thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        });
    }

    private void handleKillSelectedEntity() {
        int row = tblBossAndWild.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 thực thể trên bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int entityId = Integer.parseInt(tblBossAndWild.getValueAt(row, 1).toString());
        String cat = tblBossAndWild.getValueAt(row, 0).toString();

        boss.BossAndWildManager.TrackedEntityDTO targetDTO = currentTrackedEntities.stream()
                .filter(e -> e.id == entityId && e.category.equals(cat))
                .findFirst().orElse(null);

        if (targetDTO == null || targetDTO.rawEntity == null) {
            JOptionPane.showMessageDialog(this, "Thực thể này chưa xuất hiện!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn tiêu diệt / xóa " + targetDTO.name + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf == JOptionPane.YES_OPTION) {
            PanelManager.gI().killEntityAsync(targetDTO.rawEntity).thenAccept(ok -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Đã xử lý xong!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshBossAndWildAsync();
                });
            });
        }
    }

    private void handleExecuteRandomSpawn() {
        String type = (String) cbRandomSpawnType.getSelectedItem();
        int count = 1;
        try {
            count = Integer.parseInt(txtRandomSpawnCount.getText().trim());
            if (count <= 0) count = 1;
        } catch (Exception e) {
            count = 1;
        }

        boolean noDupMap = chkNoDupMap.isSelected();
        boolean noDupZone = chkNoDupZone.isSelected();
        boolean notifyKtg = chkRandomKtg.isSelected();

        if ("Đệ Tử Hoang".equals(type)) {
            PanelManager.gI().spawnWildDeTuAsync(null, null, null, count, noDupMap, noDupZone, notifyKtg).thenAccept(list -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Đã spawn thành công " + list.size() + " Đệ Tử Hoang!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshBossAndWildAsync();
                });
            });
        } else if ("Siêu Trùm".equals(type)) {
            PanelManager.gI().spawnSuperBossAsync(null, null, null, count, noDupMap, noDupZone, notifyKtg).thenAccept(list -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Đã spawn thành công " + list.size() + " Siêu Trùm!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshBossAndWildAsync();
                });
            });
        } else {
            PanelManager.gI().spawnNormalBossAsync(null, null, null, count, noDupMap, noDupZone, notifyKtg).thenAccept(list -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Đã spawn thành công " + list.size() + " Boss!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshBossAndWildAsync();
                });
            });
        }
    }

    private void handleExecuteCustomSpawn() {
        String selected = (String) cbSpecificEntity.getSelectedItem();
        Integer mapId = null;
        Integer zoneId = null;
        int count = 1;

        String mapStr = txtCustomMapId.getText().trim();
        if (!mapStr.isEmpty()) {
            try { mapId = Integer.parseInt(mapStr); } catch (Exception ignored) {}
        }

        String zoneStr = txtCustomZoneId.getText().trim();
        boolean allZones = (chkCustomAllZones != null && chkCustomAllZones.isSelected());
        if (zoneStr.equalsIgnoreCase("all") || zoneStr.equals("-1") || zoneStr.equals("*") || zoneStr.contains("Tất cả")) {
            allZones = true;
            zoneId = null;
        } else if (!zoneStr.isEmpty() && !allZones) {
            try {
                int rawZ = Integer.parseInt(zoneStr);
                // Khu trong game hiển thị 1-based (Khu 1, Khu 2...).
                // Nếu người dùng nhập >= 1 thì zoneId = rawZ - 1. Nếu nhập 0 thì zoneId = 0.
                zoneId = rawZ > 0 ? (rawZ - 1) : 0;
            } catch (Exception ignored) {}
        }

        try {
            count = Integer.parseInt(txtCustomCount.getText().trim());
            if (count <= 0) count = 1;
        } catch (Exception ignored) {}

        if (allZones && mapId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Map ID cụ thể để spawn vào tất cả các khu của map đó!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        final Integer finalMapId = mapId;
        final Integer finalZoneId = zoneId;
        final int finalCount = count;
        final boolean finalAllZones = allZones;
        final boolean notifyKtg = chkCustomKtg.isSelected();

        if (selected != null && selected.startsWith("Đệ Tử Hoang")) {
            Integer clazz = null;
            if (selected.contains("1. Kiếm Sĩ")) clazz = 1;
            else if (selected.contains("2. Xạ Thủ")) clazz = 2;
            else if (selected.contains("3. Võ Sĩ")) clazz = 3;
            else if (selected.contains("4. Đầu Bếp")) clazz = 4;
            else if (selected.contains("5. Hoa Tiêu")) clazz = 5;

            PanelManager.gI().spawnWildDeTuAsync(finalMapId, finalZoneId, clazz, finalCount, finalAllZones, false, !finalAllZones, notifyKtg).thenAccept(list -> {
                SwingUtilities.invokeLater(() -> {
                    String locInfo;
                    if (finalMapId != null) {
                        locInfo = "Map " + finalMapId;
                        if (finalAllZones) {
                            locInfo += " (TẤT CẢ CÁC KHU, mỗi khu " + finalCount + " đệ)";
                        } else if (finalZoneId != null) {
                            locInfo += " (Khu " + (finalZoneId + 1) + ")";
                        }
                    } else {
                        locInfo = "Random Map";
                    }
                    JOptionPane.showMessageDialog(this, "Đã spawn thành công " + list.size() + " Đệ Tử Hoang tại " + locInfo + "!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshBossAndWildAsync();
                });
            });
        } else if (selected.startsWith("Siêu Trùm")) {
            int mobId = 16;
            if (selected.contains("16")) mobId = 16;
            else if (selected.contains("23")) mobId = 23;
            else if (selected.contains("135")) mobId = 135;
            else if (selected.contains("136")) mobId = 136;
            else if (selected.contains("137")) mobId = 137;
            else if (selected.contains("139")) mobId = 139;
            else if (selected.contains("163")) mobId = 163;
            else if (selected.contains("168")) mobId = 168;

            final int finalMobId = mobId;
            PanelManager.gI().spawnSuperBossAsync(finalMobId, finalMapId, finalZoneId, finalCount, false, false, notifyKtg).thenAccept(list -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Đã spawn thành công " + list.size() + " Siêu Trùm!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshBossAndWildAsync();
                });
            });
        } else if (selected.contains("172")) {
            // Boss Thế Giới
            PanelManager.gI().spawnNormalBossAsync(172, finalMapId, finalZoneId, 1, false, false, notifyKtg).thenAccept(list -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Đã triệu hồi Boss Thế Giới thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshBossAndWildAsync();
                });
            });
        } else if (selected.contains("173")) {
            // Boss Pica
            PanelManager.gI().spawnNormalBossAsync(173, finalMapId, finalZoneId, 1, false, false, notifyKtg).thenAccept(list -> {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Đã triệu hồi Boss Pica thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshBossAndWildAsync();
                });
            });
        }
    }

    private void applyBossFilter() {
        if (modelBossAndWild == null) return;
        modelBossAndWild.setRowCount(0);
        if (currentTrackedEntities == null) return;

        String catFilter = cbBossCategoryFilter != null ? (String) cbBossCategoryFilter.getSelectedItem() : "Tất Cả";
        String statusFilter = cbBossStatusFilter != null ? (String) cbBossStatusFilter.getSelectedItem() : "Tất Cả";
        String search = txtBossSearch != null ? txtBossSearch.getText().trim().toLowerCase() : "";

        for (boss.BossAndWildManager.TrackedEntityDTO e : currentTrackedEntities) {
            if (e == null) continue;
            if (!catFilter.equals("Tất Cả") && !e.category.equalsIgnoreCase(catFilter)) continue;
            if (statusFilter.equals("Chỉ Đang Sống") && !e.isAlive) continue;
            if (statusFilter.equals("Chỉ Chưa Xuất Hiện / Đã Chết") && e.isAlive) continue;

            if (!search.isEmpty()) {
                boolean match = e.name.toLowerCase().contains(search)
                        || String.valueOf(e.id).contains(search)
                        || String.valueOf(e.mapId).contains(search)
                        || e.mapName.toLowerCase().contains(search)
                        || String.valueOf(e.zoneId + 1).contains(search);
                if (!match) continue;
            }

            modelBossAndWild.addRow(new Object[]{
                e.category,
                e.id,
                e.name,
                e.mapId > 0 ? (e.mapName + " (" + e.mapId + ")") : "Chưa xuất hiện",
                e.zoneId >= 0 ? ("Khu " + (e.zoneId + 1)) : "-",
                (e.x >= 0 && e.y >= 0) ? ("[" + e.x + ", " + e.y + "]") : "-",
                e.getHpDisplay(),
                e.level,
                e.statusText
            });
        }
    }

    // =========================================================================
    // ASYNC DATA LOADERS
    // =========================================================================

    public void refreshAllAsync() {
        refreshDashboardAsync();
        refreshPlayersListAsync();
        refreshBossAndWildAsync();
        refreshDungeonsAsync();
        refreshEventsAsync();
        refreshGiftcodesAsync();
        searchItem3Async();
        searchPetsAsync();
        refreshHistoryAsync();
    }

    public void refreshBossAndWildAsync() {
        PanelManager.gI().getAllBossAndWildEntitiesAsync().thenAccept(list -> {
            SwingUtilities.invokeLater(() -> {
                currentTrackedEntities = list;
                long superLive = list.stream().filter(e -> e.category.equals("SIÊU TRÙM") && e.isAlive).count();
                long normalLive = list.stream().filter(e -> (e.category.equals("BOSS THƯỜNG") || e.category.equals("BOSS THẾ GIỚI")) && e.isAlive).count();
                long wildLive = list.stream().filter(e -> e.category.equals("ĐỆ HOANG") && e.isAlive).count();

                if (lblStatSuperBossLive != null) lblStatSuperBossLive.setText(superLive + " / 10");
                if (lblStatWorldBossLive != null) lblStatWorldBossLive.setText(String.valueOf(normalLive));
                if (lblStatWildDeTuLive != null) lblStatWildDeTuLive.setText(String.valueOf(wildLive));
                if (lblStatTotalEntities != null) lblStatTotalEntities.setText(String.valueOf(list.size()));

                applyBossFilter();
            });
        });
    }

    private void refreshDashboardAsync() {
        CompletableFuture.supplyAsync(() -> PanelManager.gI().getServerStats())
                .thenAccept(stats -> SwingUtilities.invokeLater(() -> {
                    lblRealPlayers.setText(String.valueOf(stats.get("real_players_online")));
                    lblBotsCount.setText(String.valueOf(stats.get("bots_online")));
                    lblSessionsCount.setText(String.valueOf(stats.get("total_connected_sessions")));
                    lblRamUsage.setText(stats.get("ram_used_mb") + " / " + stats.get("ram_total_mb") + " MB");
                    lblActiveDungeons.setText(String.valueOf(stats.get("active_dungeons_count")));
                    lblActiveEvents.setText(String.valueOf(stats.get("active_events_count")));
                    lblThreadsCount.setText(String.valueOf(stats.get("threads_count")));

                    int cd = (Integer) stats.get("maintenance_countdown");
                    if (cd > 0) {
                        lblMaintenanceCountdown.setText(cd + "s (" + stats.get("maintenance_reason") + ")");
                        lblServerStatusBadge.setText(" ● BẢO TRÌ TRONG " + cd + "S ");
                        lblServerStatusBadge.setBackground(COLOR_RED);
                    } else if (Boolean.TRUE.equals(stats.get("is_baotri"))) {
                        lblMaintenanceCountdown.setText("ĐÃ BẢO TRÌ");
                        lblServerStatusBadge.setText(" ● ĐÃ ĐÓNG BẢO TRÌ ");
                        lblServerStatusBadge.setBackground(COLOR_RED);
                    } else {
                        lblMaintenanceCountdown.setText("Không");
                        lblServerStatusBadge.setText(" ● SERVER ONLINE ");
                        lblServerStatusBadge.setBackground(COLOR_GREEN);
                    }
                }));
    }

    private void refreshPlayersListAsync() {
        String filter = txtPlayerSearch != null ? txtPlayerSearch.getText().trim() : "";
        CompletableFuture.supplyAsync(() -> PanelManager.gI().getOnlinePlayerList(filter, 100, 0))
                .thenAccept(list -> SwingUtilities.invokeLater(() -> {
                    modelPlayers.setRowCount(0);
                    for (PlayerInfoDTO p : list) {
                        modelPlayers.addRow(new Object[]{p.playerId, p.accountId, p.name, p.level, String.format("%,d", p.beri), String.format("%,d", p.ruby), p.mapName});
                    }
                }));
    }

    private void refreshDungeonsAsync() {
        CompletableFuture.supplyAsync(() -> PanelManager.gI().getAllDungeonsStatus())
                .thenAccept(list -> SwingUtilities.invokeLater(() -> {
                    modelDungeons.setRowCount(0);
                    for (DungeonStatusDTO d : list) {
                        modelDungeons.addRow(new Object[]{d.id, d.name, d.stateName, d.remainingSeconds, d.runDurationMinutes, d.cooldownMinutes, d.isEnabled, d.rewardInfo});
                    }
                }));
    }

    private void refreshEventsAsync() {
        CompletableFuture.supplyAsync(() -> PanelManager.gI().getAllEventsConfig())
                .thenAccept(list -> SwingUtilities.invokeLater(() -> {
                    modelEvents.setRowCount(0);
                    for (EventInfoDTO e : list) {
                        modelEvents.addRow(new Object[]{e.id, e.eventId, e.name, e.isActive, e.seasonName, e.time});
                    }
                }));
    }

    private void refreshGiftcodesAsync() {
        String search = txtGiftSearch != null ? txtGiftSearch.getText().trim() : "";
        CompletableFuture.supplyAsync(() -> PanelManager.gI().getAllGiftCodes(search))
                .thenAccept(list -> SwingUtilities.invokeLater(() -> {
                    modelGiftcodes.setRowCount(0);
                    for (GiftCodeDTO g : list) {
                        modelGiftcodes.addRow(new Object[]{g.giftname, g.luotnhap + " / " + g.gioihan, String.format("%,d", g.beri), String.format("%,d", g.ruby), g.itemJson, g.notice, g.mtv == 1 ? "Có" : "Không", g.special});
                    }
                }));
    }

    private void refreshHistoryAsync() {
        Integer accId = null;
        try { if (!txtHistAccId.getText().trim().isEmpty()) accId = Integer.parseInt(txtHistAccId.getText().trim()); } catch (Exception ignore) {}
        Integer pId = null;
        try { if (!txtHistPlayerId.getText().trim().isEmpty()) pId = Integer.parseInt(txtHistPlayerId.getText().trim()); } catch (Exception ignore) {}
        String type = (cbHistType.getSelectedIndex() > 0) ? cbHistType.getSelectedItem().toString() : "";
        String kw = txtHistKeyword.getText().trim();

        final Integer fAcc = accId; final Integer fPid = pId; final String fType = type; final String fKw = kw;
        CompletableFuture.supplyAsync(() -> PanelManager.gI().getHistoryLogs(fAcc, fPid, fType, fKw, HISTORY_LIMIT, historyCurrentOffset))
                .thenAccept(logs -> SwingUtilities.invokeLater(() -> {
                    modelHistory.setRowCount(0);
                    for (HistoryLogDTO h : logs) {
                        String shortData = h.data != null ? (h.data.length() > 80 ? h.data.substring(0, 80) + "..." : h.data) : "";
                        modelHistory.addRow(new Object[]{h.id, h.accountId, h.playerId, h.type, h.createdAt, h.data});
                    }
                }));
    }

    // =========================================================================
    // UI HELPER BUILDERS
    // =========================================================================

    public static JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(COLOR_BG);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JTextField createTextField(String text) {
        JTextField tf = new JTextField(text);
        tf.setBackground(COLOR_INPUT_BG);
        tf.setForeground(COLOR_TEXT);
        tf.setCaretColor(COLOR_ACCENT);
        tf.setFont(FONT_REGULAR);
        tf.setBorder(new LineBorder(COLOR_CARD, 1));
        return tf;
    }

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BOLD);
        lbl.setForeground(COLOR_TEXT_MUTED);
        return lbl;
    }

    public static JPanel createTitledPanel(String title) {
        JPanel p = new JPanel();
        p.setBackground(COLOR_PANEL);
        TitledBorder tb = BorderFactory.createTitledBorder(new LineBorder(COLOR_CARD, 1), " " + title + " ", TitledBorder.LEFT, TitledBorder.TOP, FONT_SUBHEADER, COLOR_ACCENT);
        p.setBorder(tb);
        return p;
    }

    public static JPanel createStatCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(COLOR_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(COLOR_CARD, 1), new EmptyBorder(10, 12, 10, 12)));

        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(FONT_BOLD);
        lblTitle.setForeground(COLOR_TEXT_MUTED);
        card.add(lblTitle, BorderLayout.NORTH);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accent);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    public static JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(COLOR_PANEL);
        table.setForeground(COLOR_TEXT);
        table.setGridColor(COLOR_CARD);
        table.setFont(FONT_REGULAR);
        table.setRowHeight(26);
        table.getTableHeader().setBackground(COLOR_CARD);
        table.getTableHeader().setForeground(COLOR_ACCENT);
        table.getTableHeader().setFont(FONT_BOLD);
        table.setSelectionBackground(new Color(56, 189, 248, 80));
        table.setSelectionForeground(COLOR_TEXT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                if (!isSel) {
                    comp.setBackground(r % 2 == 0 ? COLOR_PANEL : new Color(25, 34, 50));
                }
                setForeground(COLOR_TEXT);
                return comp;
            }
        });
        return table;
    }
}
