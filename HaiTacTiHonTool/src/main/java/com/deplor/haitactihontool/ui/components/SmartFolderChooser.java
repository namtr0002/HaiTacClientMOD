package com.deplor.haitactihontool.ui.components;

import com.deplor.haitactihontool.config.Lang;
import com.deplor.haitactihontool.config.Theme;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class SmartFolderChooser extends JDialog {
   private static final Color BG = new Color(14, 14, 20);
   private static final Color BG2 = new Color(20, 20, 28);
   private static final Color BG3 = new Color(18, 18, 26);
   private static final Color ACCENT = new Color(100, 60, 200);
   private static final Color ACCENT_HOV = new Color(120, 75, 220);
   private static final Color ACCENT_SOFT = new Color(80, 50, 160, 60);
   private static final Color TEXT = new Color(200, 200, 215);
   private static final Color TEXT_DIM = new Color(130, 130, 150);
   private static final Color BORDER = new Color(36, 36, 52);
   private static final Color SUCCESS = new Color(60, 200, 120);
   private static final Color DANGER = new Color(220, 60, 60);
   private static final Color CRUMB_BG = new Color(30, 30, 40);
   private static final Color CRUMB_HOV = new Color(50, 40, 80);
   private static final Font F_TITLE = new Font("Segoe UI", 1, 14);
   private static final Font F_BODY = new Font("Segoe UI", 0, 13);
   private static final Font F_SMALL = new Font("Segoe UI", 0, 11);
   private static final Font F_BOLD = new Font("Segoe UI", 1, 13);
   private static final Font F_CRUMB = new Font("Segoe UI", 1, 12);
   private static final Font F_MONO = new Font("Consolas", 0, 12);
   private File selectedFolder = null;
   private String result = null;
   private JTree tree;
   private DefaultTreeModel treeModel;
   private JTextField pathField;
   private JPanel breadcrumbPanel;
   private JLabel infoName;
   private JLabel infoPath;
   private JLabel infoSubs;
   private JLabel infoSize;
   private JButton btnChoose;
   private Point dragStart;

   public SmartFolderChooser(Window owner, String initialPath) {
      super(owner, ModalityType.APPLICATION_MODAL);
      this.setUndecorated(true);
      this.setSize(720, 510);
      this.setLocationRelativeTo(owner);
      this.setBackground(BG);
      this.buildUI();
      this.addDragSupport();
      if (initialPath != null && !initialPath.isBlank()) {
         File init = new File(initialPath);
         if (init.exists() && init.isDirectory()) {
            this.navigateTo(init);
         }
      }
   }

   public static String showChoose(Component parent, String initialPath) {
      Window owner = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
      if (owner == null && parent instanceof Window) {
         owner = (Window)parent;
      }

      SmartFolderChooser dlg = new SmartFolderChooser(owner, initialPath);
      dlg.setVisible(true);
      return dlg.result;
   }

   private void buildUI() {
      JPanel root = new JPanel(new BorderLayout()) {
         @Override
         protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(SmartFolderChooser.BORDER);
            g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 12, 12);
            g2.dispose();
         }
      };
      root.setBackground(BG);
      root.setBorder(new MatteBorder(1, 1, 1, 1, BORDER));
      this.setContentPane(root);
      JPanel northStack = new JPanel();
      northStack.setLayout(new BoxLayout(northStack, 1));
      northStack.setOpaque(false);
      northStack.add(this.buildTitleBar());
      northStack.add(this.buildToolbar());
      JPanel bcHolder = new JPanel(new BorderLayout());
      bcHolder.setOpaque(false);
      bcHolder.setBackground(BG2);
      bcHolder.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      this.breadcrumbPanel = new JPanel(new FlowLayout(0, 2, 4));
      this.breadcrumbPanel.setOpaque(true);
      this.breadcrumbPanel.setBackground(BG2);
      JScrollPane bcScroll = new JScrollPane(this.breadcrumbPanel, 21, 31);
      bcScroll.setBorder(null);
      bcScroll.setOpaque(false);
      bcScroll.getViewport().setOpaque(false);
      bcHolder.add(bcScroll, "Center");
      JPanel pathRow = new JPanel(new BorderLayout(4, 0));
      pathRow.setOpaque(true);
      pathRow.setBackground(BG2);
      pathRow.setBorder(new EmptyBorder(4, 8, 4, 8));
      JLabel lbPath = new JLabel("▸");
      lbPath.setForeground(ACCENT);
      lbPath.setFont(F_BOLD);
      this.pathField = new JTextField();
      this.styleTextField(this.pathField);
      this.pathField.setFont(F_MONO);
      this.pathField.addActionListener(e -> this.navigateTo(new File(this.pathField.getText().trim())));
      pathRow.add(lbPath, "West");
      pathRow.add(this.pathField, "Center");
      northStack.add(bcHolder);
      northStack.add(pathRow);
      root.add(northStack, "North");
      JSplitPane split = new JSplitPane(1, this.buildTreePanel(), this.buildInfoPanel());
      split.setDividerLocation(390);
      split.setDividerSize(4);
      split.setBorder(null);
      split.setBackground(BG);
      split.setUI(new BasicSplitPaneUI() {
         @Override
         public BasicSplitPaneDivider createDefaultDivider() {
            return new BasicSplitPaneDivider(this) {
               {
                  this.setBackground(SmartFolderChooser.BORDER);
               }
            };
         }
      });
      root.add(split, "Center");
      root.add(this.buildFooter(), "South");
   }

   private JPanel buildTitleBar() {
      JPanel bar = new JPanel(new BorderLayout());
      bar.setBackground(BG2);
      bar.setPreferredSize(new Dimension(0, 40));
      bar.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      JLabel title = new JLabel("  [Folder]  " + Lang.get("folder_chooser_title"));
      title.setFont(F_TITLE);
      title.setForeground(TEXT);
      bar.add(title, "Center");
      JButton btnClose = this.makeIconButton("✕", DANGER);
      btnClose.addActionListener(e -> this.dispose());
      bar.add(btnClose, "East");
      MouseAdapter dragger = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            SmartFolderChooser.this.dragStart = e.getLocationOnScreen();
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            if (SmartFolderChooser.this.dragStart != null) {
               Point loc = SmartFolderChooser.this.getLocation();
               int dx = e.getLocationOnScreen().x - SmartFolderChooser.this.dragStart.x;
               int dy = e.getLocationOnScreen().y - SmartFolderChooser.this.dragStart.y;
               SmartFolderChooser.this.setLocation(loc.x + dx, loc.y + dy);
               SmartFolderChooser.this.dragStart = e.getLocationOnScreen();
            }
         }
      };
      bar.addMouseListener(dragger);
      bar.addMouseMotionListener(dragger);
      title.addMouseListener(dragger);
      title.addMouseMotionListener(dragger);
      return bar;
   }

   private JPanel buildToolbar() {
      JPanel bar = new JPanel(new FlowLayout(0, 6, 5));
      bar.setBackground(BG2);
      bar.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      JButton btnNew = this.makeToolbarButton(Lang.get("folder_btn_new"), SUCCESS);
      JButton btnRename = this.makeToolbarButton(Lang.get("folder_btn_rename"), ACCENT);
      JButton btnDelete = this.makeToolbarButton(Lang.get("folder_btn_delete"), DANGER);
      JButton btnRefresh = this.makeToolbarButton(Lang.get("folder_btn_refresh"), new Color(60, 150, 200));
      btnNew.addActionListener(e -> this.doNewFolder());
      btnRename.addActionListener(e -> this.doRename());
      btnDelete.addActionListener(e -> this.doDelete());
      btnRefresh.addActionListener(e -> this.refreshCurrentNode());
      bar.add(btnNew);
      bar.add(btnRename);
      bar.add(btnDelete);
      bar.add(Box.createHorizontalStrut(6));
      bar.add(this.makeVSeparator());
      bar.add(Box.createHorizontalStrut(6));
      bar.add(btnRefresh);
      return bar;
   }

   private JPanel buildBreadcrumb() {
      return this.breadcrumbPanel;
   }

   private void refreshBreadcrumb(File folder) {
      this.breadcrumbPanel.removeAll();
      List<File> parts = new ArrayList<>();

      for (File cur = folder; cur != null; cur = cur.getParentFile()) {
         parts.add(0, cur);
      }

      for (int i = 0; i < parts.size(); i++) {
         File part = parts.get(i);
         String label = part.getName().isEmpty() ? part.getPath() : part.getName();
         if (label.endsWith(File.separator)) {
            label = label.substring(0, label.length() - 1);
         }

         JButton crumb = this.makeCrumbButton("[" + label + "]", i < parts.size() - 1);
         crumb.addActionListener(e -> this.navigateTo(part));
         this.breadcrumbPanel.add(crumb);
         if (i < parts.size() - 1) {
            JLabel sep = new JLabel("›");
            sep.setForeground(TEXT_DIM);
            sep.setFont(F_CRUMB);
            this.breadcrumbPanel.add(sep);
         }
      }

      this.breadcrumbPanel.revalidate();
      this.breadcrumbPanel.repaint();
   }

   private JPanel buildTreePanel() {
      DefaultMutableTreeNode virtualRoot = new DefaultMutableTreeNode("Computer");
      File[] roots = File.listRoots();
      if (roots != null) {
         for (File drive : roots) {
            SmartFolderChooser.FolderNode driveNode = new SmartFolderChooser.FolderNode(drive);
            driveNode.add(new SmartFolderChooser.LoadingNode());
            virtualRoot.add(driveNode);
         }
      }

      this.treeModel = new DefaultTreeModel(virtualRoot);
      this.tree = new JTree(this.treeModel) {
         @Override
         public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof SmartFolderChooser.FolderNode fn) {
               return fn.getDisplayName();
            } else {
               return value instanceof SmartFolderChooser.LoadingNode
                  ? Lang.get("folder_loading")
                  : super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
            }
         }
      };
      this.tree.setRootVisible(false);
      this.tree.setShowsRootHandles(true);
      this.tree.setBackground(BG3);
      this.tree.setForeground(TEXT);
      this.tree.setFont(F_BODY);
      this.tree.setRowHeight(26);
      this.tree.setBorder(new EmptyBorder(4, 0, 4, 0));
      this.tree.setCellRenderer(new SmartFolderChooser.FolderTreeCellRenderer());
      this.tree.addTreeWillExpandListener(new TreeWillExpandListener() {
         @Override
         public void treeWillExpand(TreeExpansionEvent event) {
            TreePath path = event.getPath();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
            if (node.getChildCount() == 1 && node.getChildAt(0) instanceof SmartFolderChooser.LoadingNode && node instanceof SmartFolderChooser.FolderNode fn) {
               SmartFolderChooser.this.loadChildren(fn);
            }
         }

         @Override
         public void treeWillCollapse(TreeExpansionEvent event) {
         }
      });
      this.tree.addTreeSelectionListener(e -> {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.tree.getLastSelectedPathComponent();
         if (node instanceof SmartFolderChooser.FolderNode fn) {
            this.selectFolder(fn.getFolder());
         }
      });
      JScrollPane scroll = new JScrollPane(this.tree);
      this.styleSrollPane(scroll, BG3);
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(BG3);
      panel.setBorder(new MatteBorder(0, 0, 0, 1, BORDER));
      panel.add(scroll, "Center");
      return panel;
   }

   private JPanel buildInfoPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(BG);
      panel.setBorder(new EmptyBorder(16, 16, 16, 16));
      JLabel header = new JLabel(Lang.get("folder_info_header"));
      header.setFont(new Font("Segoe UI", 1, 11));
      header.setForeground(ACCENT);
      header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
      JPanel infoGrid = new JPanel(new GridBagLayout());
      infoGrid.setOpaque(false);
      infoGrid.setBorder(new EmptyBorder(12, 0, 0, 0));
      GridBagConstraints lc = new GridBagConstraints();
      lc.anchor = 18;
      lc.insets = new Insets(5, 0, 5, 12);
      lc.gridx = 0;
      lc.fill = 0;
      GridBagConstraints vc = new GridBagConstraints();
      vc.anchor = 18;
      vc.insets = new Insets(5, 0, 5, 0);
      vc.gridx = 1;
      vc.fill = 2;
      vc.weightx = 1.0;
      this.infoName = this.makeInfoValue("—");
      this.infoPath = this.makeInfoValue("—");
      this.infoSubs = this.makeInfoValue("—");
      this.infoSize = this.makeInfoValue("—");
      int row = 0;
      this.addInfoRow(infoGrid, lc, vc, row++, Lang.get("folder_info_name"), this.infoName);
      this.addInfoRow(infoGrid, lc, vc, row++, Lang.get("folder_info_path"), this.infoPath);
      this.addInfoRow(infoGrid, lc, vc, row++, Lang.get("folder_info_subs"), this.infoSubs);
      this.addInfoRow(infoGrid, lc, vc, row, Lang.get("folder_info_size"), this.infoSize);
      JLabel iconLabel = new JLabel("▰") {
         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            super.paintComponent(g2);
            g2.dispose();
         }
      };
      iconLabel.setFont(new Font("Segoe UI", 0, 48));
      iconLabel.setHorizontalAlignment(0);
      iconLabel.setBorder(new EmptyBorder(12, 0, 12, 0));
      JPanel top = new JPanel(new BorderLayout(0, 4));
      top.setOpaque(false);
      top.add(header, "North");
      top.add(iconLabel, "Center");
      panel.add(top, "North");
      panel.add(infoGrid, "Center");
      return panel;
   }

   private void addInfoRow(JPanel grid, GridBagConstraints lc, GridBagConstraints vc, int row, String label, JLabel valueLabel) {
      lc.gridy = row;
      vc.gridy = row;
      JLabel lbl = new JLabel(label);
      lbl.setFont(F_SMALL);
      lbl.setForeground(TEXT_DIM);
      grid.add(lbl, lc);
      grid.add(valueLabel, vc);
   }

   private JLabel makeInfoValue(String text) {
      JLabel l = new JLabel(text);
      l.setFont(F_BODY);
      l.setForeground(TEXT);
      return l;
   }

   private JPanel buildFooter() {
      JPanel bar = new JPanel(new FlowLayout(2, 10, 8));
      bar.setBackground(BG2);
      bar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER));
      JButton btnCancel = this.makeRoundedButton(Lang.get("btn_cancel").toUpperCase(), new Color(50, 50, 65), TEXT_DIM);
      this.btnChoose = this.makeRoundedButton("✔  " + Lang.get("btn_select"), ACCENT, Color.WHITE);
      this.btnChoose.setEnabled(false);
      btnCancel.addActionListener(e -> this.dispose());
      this.btnChoose.addActionListener(e -> {
         if (this.selectedFolder != null) {
            this.result = this.selectedFolder.getAbsolutePath();
         }

         this.dispose();
      });
      bar.add(btnCancel);
      bar.add(this.btnChoose);
      return bar;
   }

   private void navigateTo(File folder) {
      if (folder != null && folder.exists() && folder.isDirectory()) {
         this.selectFolder(folder);
         this.expandPathInTree(folder);
      } else {
         this.showToast(Lang.get("folder_err_invalid_path"));
      }
   }

   private void selectFolder(File folder) {
      this.selectedFolder = folder;
      this.pathField.setText(folder.getAbsolutePath());
      this.refreshBreadcrumb(folder);
      this.updateInfoPanel(folder);
      this.btnChoose.setEnabled(true);
   }

   private void updateInfoPanel(final File folder) {
      this.infoName.setText(folder.getName().isEmpty() ? folder.getAbsolutePath() : folder.getName());
      this.infoPath.setText("<html><body style='width:160px'>" + folder.getAbsolutePath() + "</body></html>");
      SwingWorker<int[], Void> worker = new SwingWorker<int[], Void>() {
         protected int[] doInBackground() {
            File[] kids = folder.listFiles(File::isDirectory);
            int subs = kids != null ? kids.length : 0;
            return new int[]{subs};
         }

         @Override
         protected void done() {
            try {
               int[] res = this.get();
               SmartFolderChooser.this.infoSubs.setText(String.valueOf(res[0]));
            } catch (Exception var2) {
            }
         }
      };
      worker.execute();
      this.infoSubs.setText(Lang.get("folder_calculating"));
      this.infoSize.setText("N/A");
   }

   private void expandPathInTree(File target) {
      List<File> parts = new ArrayList<>();

      for (File cur = target; cur != null; cur = cur.getParentFile()) {
         parts.add(0, cur);
      }

      DefaultMutableTreeNode virtualRoot = (DefaultMutableTreeNode)this.treeModel.getRoot();
      DefaultMutableTreeNode treeNode = null;

      for (int i = 0; i < virtualRoot.getChildCount(); i++) {
         if (virtualRoot.getChildAt(i) instanceof SmartFolderChooser.FolderNode fn
            && (fn.getFolder().equals(parts.get(0)) || parts.size() > 0 && fn.getFolder().getAbsolutePath().equalsIgnoreCase(parts.get(0).getAbsolutePath()))) {
            treeNode = fn;
            break;
         }
      }

      if (treeNode != null) {
         this.tree.expandPath(new TreePath(this.treeModel.getPathToRoot(treeNode)));

         for (int pi = 1; pi < parts.size(); pi++) {
            File partFile = parts.get(pi);
            if (treeNode.getChildCount() == 1 && treeNode.getChildAt(0) instanceof SmartFolderChooser.LoadingNode) {
               this.loadChildren((SmartFolderChooser.FolderNode)treeNode);
            }

            DefaultMutableTreeNode found = null;

            for (int ci = 0; ci < treeNode.getChildCount(); ci++) {
               if (treeNode.getChildAt(ci) instanceof SmartFolderChooser.FolderNode fn3
                  && fn3.getFolder().getAbsolutePath().equalsIgnoreCase(partFile.getAbsolutePath())) {
                  found = fn3;
                  break;
               }
            }

            if (found == null) {
               break;
            }

            treeNode = found;
            TreePath tp = new TreePath(this.treeModel.getPathToRoot(found));
            this.tree.expandPath(tp);
            this.tree.setSelectionPath(tp);
            this.tree.scrollPathToVisible(tp);
         }
      }
   }

   private void loadChildren(SmartFolderChooser.FolderNode node) {
      node.removeAllChildren();
      File folder = node.getFolder();
      File[] children = folder.listFiles(File::isDirectory);
      if (children != null) {
         Arrays.sort(children, Comparator.comparing(f -> f.getName().toLowerCase()));

         for (File child : children) {
            if (!child.isHidden()) {
               SmartFolderChooser.FolderNode childNode = new SmartFolderChooser.FolderNode(child);
               File[] grandChildren = child.listFiles(File::isDirectory);
               if (grandChildren != null && grandChildren.length > 0) {
                  childNode.add(new SmartFolderChooser.LoadingNode());
               }

               node.add(childNode);
            }
         }
      }

      this.treeModel.nodeStructureChanged(node);
   }

   private void refreshCurrentNode() {
      if (this.selectedFolder != null) {
         DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.treeModel.getRoot();
         SmartFolderChooser.FolderNode target = this.findNode(root, this.selectedFolder);
         if (target != null) {
            this.loadChildren(target);
         }

         this.updateInfoPanel(this.selectedFolder);
      }
   }

   private SmartFolderChooser.FolderNode findNode(DefaultMutableTreeNode parent, File target) {
      for (int i = 0; i < parent.getChildCount(); i++) {
         if (parent.getChildAt(i) instanceof SmartFolderChooser.FolderNode fn) {
            if (fn.getFolder().getAbsolutePath().equalsIgnoreCase(target.getAbsolutePath())) {
               return fn;
            }

            SmartFolderChooser.FolderNode deeper = this.findNode(fn, target);
            if (deeper != null) {
               return deeper;
            }
         }
      }

      return null;
   }

   private void doNewFolder() {
      if (this.selectedFolder == null) {
         this.showToast(Lang.get("folder_err_select_first"));
      } else {
         String name = this.showInlineInput(Lang.get("folder_prompt_new_name"), "NewFolder");
         if (name != null && !name.isBlank()) {
            name = name.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
            File newDir = new File(this.selectedFolder, name);
            if (newDir.exists()) {
               this.showToast(Lang.get("folder_err_exists") + name);
            } else {
               if (newDir.mkdir()) {
                  this.refreshCurrentNode();
                  this.navigateTo(newDir);
                  this.showToast(Lang.get("folder_success_created") + newDir.getName());
               } else {
                  this.showToast(Lang.get("folder_err_create_fail"));
               }
            }
         }
      }
   }

   private void doRename() {
      if (this.selectedFolder == null) {
         this.showToast(Lang.get("folder_err_select_first"));
      } else {
         String current = this.selectedFolder.getName();
         String newName = this.showInlineInput(Lang.get("folder_prompt_rename"), current);
         if (newName != null && !newName.isBlank() && !newName.equals(current)) {
            newName = newName.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
            File dest = new File(this.selectedFolder.getParentFile(), newName);
            if (dest.exists()) {
               this.showToast(Lang.get("folder_err_rename_exists") + newName);
            } else {
               File parent = this.selectedFolder.getParentFile();
               if (this.selectedFolder.renameTo(dest)) {
                  this.navigateTo(parent);
                  this.refreshCurrentNode();
                  this.showToast(Lang.get("folder_success_renamed") + dest.getName());
               } else {
                  this.showToast(Lang.get("folder_err_rename_fail"));
               }
            }
         }
      }
   }

   private void doDelete() {
      if (this.selectedFolder == null) {
         this.showToast(Lang.get("folder_err_select_first"));
      } else {
         int confirm = this.showDarkConfirm(
            Lang.get("folder_confirm_delete_title"), String.format(Lang.get("folder_confirm_delete_msg"), this.selectedFolder.getAbsolutePath())
         );
         if (confirm == 0) {
            File parent = this.selectedFolder.getParentFile();
            if (this.selectedFolder.delete()) {
               this.selectedFolder = null;
               this.btnChoose.setEnabled(false);
               if (parent != null) {
                  this.navigateTo(parent);
               }

               this.refreshCurrentNode();
               this.showToast(Lang.get("folder_success_deleted"));
            } else {
               this.showToast(Lang.get("folder_err_delete_fail"));
            }
         }
      }
   }

   private void addDragSupport() {
   }

   private String showInlineInput(String prompt, String defaultValue) {
      JPanel panel = new JPanel(new BorderLayout(0, 6));
      panel.setBackground(BG2);
      panel.setBorder(new EmptyBorder(8, 8, 8, 8));
      JLabel lbl = new JLabel(prompt);
      lbl.setFont(F_BODY);
      lbl.setForeground(TEXT);
      JTextField input = new JTextField(defaultValue);
      this.styleTextField(input);
      input.selectAll();
      panel.add(lbl, "North");
      panel.add(input, "Center");
      UIManager.put("OptionPane.background", BG2);
      UIManager.put("Panel.background", BG2);
      UIManager.put("OptionPane.messageForeground", TEXT);
      int result = JOptionPane.showConfirmDialog(this, panel, Lang.get("folder_title_input"), 2, -1);
      return result == 0 ? input.getText() : null;
   }

   private int showDarkConfirm(String title, String message) {
      JLabel lbl = new JLabel("<html><body style='width:280px;font-family:Segoe UI;font-size:12px'>" + message.replace("\n", "<br>") + "</body></html>");
      lbl.setForeground(TEXT);
      UIManager.put("OptionPane.background", BG2);
      UIManager.put("Panel.background", BG2);
      UIManager.put("OptionPane.messageForeground", TEXT);
      return JOptionPane.showConfirmDialog(this, lbl, title, 0, 2);
   }

   private void showToast(String msg) {
      JWindow toast = new JWindow(this);
      JLabel lbl = new JLabel("  " + msg + "  ");
      lbl.setFont(F_BODY);
      lbl.setForeground(Color.WHITE);
      lbl.setOpaque(true);
      lbl.setBackground(new Color(30, 28, 45));
      lbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ACCENT, 1), new EmptyBorder(6, 12, 6, 12)));
      toast.add(lbl);
      toast.pack();
      Point loc = this.getLocationOnScreen();
      int tx = loc.x + (this.getWidth() - toast.getWidth()) / 2;
      int ty = loc.y + this.getHeight() - toast.getHeight() - 16;
      toast.setLocation(tx, ty);
      toast.setVisible(true);
      Timer timer = new Timer(2500, e -> toast.dispose());
      timer.setRepeats(false);
      timer.start();
   }

   private JButton makeToolbarButton(String text, final Color accent) {
      JButton btn = new JButton(text) {
         private boolean hov = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hov = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hov = false;
                  repaint();
               }
            });
         }

         @Override
         public void setFont(Font f) {
            super.setFont(Theme.getFontForText(this.getText(), f));
         }

         @Override
         public void setText(String t) {
            super.setText(t);
            super.setFont(Theme.getFontForText(t, this.getFont()));
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(
               this.hov
                  ? new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 40)
                  : new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 18)
            );
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 8, 8);
            if (this.hov) {
               g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 120));
               g2.setStroke(new BasicStroke(1.0F));
               g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8);
            }

            g2.setColor(accent);
            g2.setFont(this.getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (this.getWidth() - fm.stringWidth(this.getText())) / 2, (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      btn.setFont(F_SMALL);
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      btn.setPreferredSize(new Dimension(130, 26));
      return btn;
   }

   private JButton makeRoundedButton(String text, final Color bg, final Color fg) {
      JButton btn = new JButton(text) {
         private boolean hov = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hov = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hov = false;
                  repaint();
               }
            });
         }

         @Override
         public void setFont(Font f) {
            super.setFont(Theme.getFontForText(this.getText(), f));
         }

         @Override
         public void setText(String t) {
            super.setText(t);
            super.setFont(Theme.getFontForText(t, this.getFont()));
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = this.hov ? bg.brighter() : bg;
            g2.setColor(c);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);
            g2.setColor(fg);
            g2.setFont(this.getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (this.getWidth() - fm.stringWidth(this.getText())) / 2, (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      btn.setFont(F_BOLD);
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      btn.setPreferredSize(new Dimension(170, 34));
      return btn;
   }

   private JButton makeIconButton(String text, final Color hoverColor) {
      JButton btn = new JButton(text) {
         private boolean hov = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hov = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hov = false;
                  repaint();
               }
            });
         }

         @Override
         public void setFont(Font f) {
            super.setFont(Theme.getFontForText(this.getText(), f));
         }

         @Override
         public void setText(String t) {
            super.setText(t);
            super.setFont(Theme.getFontForText(t, this.getFont()));
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (this.hov) {
               g2.setColor(new Color(hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue(), 60));
               g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }

            g2.setColor(this.hov ? hoverColor : SmartFolderChooser.TEXT_DIM);
            g2.setFont(this.getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (this.getWidth() - fm.stringWidth(this.getText())) / 2, (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      btn.setFont(F_BOLD);
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      btn.setPreferredSize(new Dimension(36, 40));
      return btn;
   }

   private JButton makeCrumbButton(String text, final boolean inactive) {
      JButton btn = new JButton(text) {
         private boolean hov = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hov = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hov = false;
                  repaint();
               }
            });
         }

         @Override
         public void setFont(Font f) {
            super.setFont(Theme.getFontForText(this.getText(), f));
         }

         @Override
         public void setText(String t) {
            super.setText(t);
            super.setFont(Theme.getFontForText(t, this.getFont()));
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bgC = this.hov ? SmartFolderChooser.CRUMB_HOV : SmartFolderChooser.CRUMB_BG;
            g2.setColor(bgC);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 6, 6);
            g2.setColor(inactive ? SmartFolderChooser.TEXT_DIM : SmartFolderChooser.ACCENT);
            g2.setFont(this.getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(this.getText(), (this.getWidth() - fm.stringWidth(this.getText())) / 2, (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
         }
      };
      btn.setFont(F_CRUMB);
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      FontMetrics fm = btn.getFontMetrics(F_CRUMB);
      btn.setPreferredSize(new Dimension(fm.stringWidth(text) + 16, 22));
      return btn;
   }

   private Component makeVSeparator() {
      JSeparator sep = new JSeparator(1);
      sep.setForeground(BORDER);
      sep.setPreferredSize(new Dimension(1, 18));
      return sep;
   }

   private void styleTextField(final JTextField field) {
      field.setBackground(new Color(24, 24, 34));
      field.setForeground(TEXT);
      field.setCaretColor(ACCENT);
      field.setFont(F_BODY);
      field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER, 1), new EmptyBorder(4, 8, 4, 8)));
      field.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent e) {
            field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(SmartFolderChooser.ACCENT, 1), new EmptyBorder(4, 8, 4, 8)));
         }

         @Override
         public void focusLost(FocusEvent e) {
            field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(SmartFolderChooser.BORDER, 1), new EmptyBorder(4, 8, 4, 8)));
         }
      });
   }

   private void styleSrollPane(JScrollPane sp, Color bg) {
      sp.setBorder(null);
      sp.setBackground(bg);
      sp.getViewport().setBackground(bg);
      sp.getVerticalScrollBar().setUI(new SmartFolderChooser.DarkScrollBarUI());
      sp.getHorizontalScrollBar().setUI(new SmartFolderChooser.DarkScrollBarUI());
      sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
      sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
      sp.getVerticalScrollBar().setUnitIncrement(20);
   }

   private static class DarkScrollBarUI extends BasicScrollBarUI {
      @Override
      protected JButton createDecreaseButton(int o) {
         return this.zeroBtn();
      }

      @Override
      protected JButton createIncreaseButton(int o) {
         return this.zeroBtn();
      }

      private JButton zeroBtn() {
         JButton b = new JButton();
         b.setPreferredSize(new Dimension(0, 0));
         b.setMinimumSize(new Dimension(0, 0));
         b.setMaximumSize(new Dimension(0, 0));
         return b;
      }

      @Override
      protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
         g.setColor(SmartFolderChooser.BG3);
         g.fillRect(r.x, r.y, r.width, r.height);
      }

      @Override
      protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
         if (!r.isEmpty() && this.scrollbar.isEnabled()) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(
               this.isThumbRollover()
                  ? new Color(SmartFolderChooser.ACCENT.getRed(), SmartFolderChooser.ACCENT.getGreen(), SmartFolderChooser.ACCENT.getBlue(), 180)
                  : new Color(60, 60, 80, 180)
            );
            g2.fillRoundRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2, 4, 4);
            g2.dispose();
         }
      }
   }

   private static class FolderNode extends DefaultMutableTreeNode {
      private final File folder;

      FolderNode(File folder) {
         super(folder);
         this.folder = folder;
      }

      File getFolder() {
         return this.folder;
      }

      String getDisplayName() {
         String name = this.folder.getName();
         return name.isBlank() ? this.folder.getAbsolutePath() : name;
      }
   }

   private class FolderTreeCellRenderer extends DefaultTreeCellRenderer {
      private static final Icon FOLDER_ICON = buildTextIcon("▶", 12);
      private static final Icon FOLDER_OPEN_ICN = buildTextIcon("▼", 12);
      private static final Icon DRIVE_ICON = buildTextIcon("⚙", 12);

      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
         super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
         this.setBackground(selected ? SmartFolderChooser.ACCENT : SmartFolderChooser.BG3);
         this.setOpaque(true);
         this.setForeground(selected ? Color.WHITE : SmartFolderChooser.TEXT);
         this.setBorderSelectionColor(null);
         this.setFont(SmartFolderChooser.F_BODY);
         this.setBorder(new EmptyBorder(2, 4, 2, 4));
         if (value instanceof SmartFolderChooser.FolderNode fn) {
            File f = fn.getFolder();
            boolean isDrive = f.getParentFile() == null;
            if (isDrive) {
               this.setIcon(DRIVE_ICON);
            } else {
               this.setIcon(expanded ? FOLDER_OPEN_ICN : FOLDER_ICON);
            }

            this.setText(fn.getDisplayName());
         } else if (value instanceof SmartFolderChooser.LoadingNode) {
            this.setIcon(null);
            this.setFont(SmartFolderChooser.F_SMALL);
            this.setForeground(SmartFolderChooser.TEXT_DIM);
         }

         return this;
      }

      private static Icon buildTextIcon(final String emoji, final int size) {
         return new Icon() {
            private final Font font = new Font("Segoe UI", 0, size);

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
               Graphics2D g2 = (Graphics2D)g.create();
               g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
               g2.setFont(this.font);
               g2.drawString(emoji, x, y + size);
               g2.dispose();
            }

            @Override
            public int getIconWidth() {
               return size + 2;
            }

            @Override
            public int getIconHeight() {
               return size + 4;
            }
         };
      }
   }

   private static class LoadingNode extends DefaultMutableTreeNode {
      LoadingNode() {
         super(Lang.get("folder_loading").trim());
      }
   }
}
