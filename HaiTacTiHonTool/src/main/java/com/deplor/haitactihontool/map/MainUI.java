package com.deplor.haitactihontool.map;

import com.deplor.haitactihontool.config.Lang;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class MainUI extends JPanel {
   private GameMapCanvas canvas;
   private TilePalettePanel tilePalette;
   private TilePalettePanel waterPalette;
   private ItemPalettePanel itemPalette;
   private JList<GameMap> mapList;
   private DefaultListModel<GameMap> mapListModel;
   private JTextField txtName;
   private JTextField txtWidth;
   private JTextField txtHeight;
   private JTextField txtTileSet;
   private JTextField txtLevel;
   private JTextField txtMaxZone;
   private JTextField txtMaxPlayer;
   private JTextArea sqlOutput;
   private JTextArea jsonOutput;
   private JComboBox<String> sqlMode;
   private JComboBox<String> itemLayerCombo;
   private JTextField txtTemplateDx;
   private JTextField txtTemplateDy;
   private JTextField txtTemplateBlock;
   private JTextField txtTemplateTags;
   private boolean isUpdatingFields = false;
   private Timer updateTimer;
   private DefaultTableModel npcsModel;
   private DefaultTableModel mobsModel;
   private DefaultTableModel itemsModel;
   private DefaultTableModel vgosModel;
   private JTable tableNpcs;
   private JTable tableMobs;
   private JTable tableItems;
   private JTable tableVgos;

   public MainUI() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      this.initUI();
   }

   private void initUI() {
      this.canvas = new GameMapCanvas();
      this.canvas.setOnMapChanged(() -> {
         this.updateObjectLists();
         this.updateOutputs();
      });
      this.canvas.setOnMapResized(() -> {
         GameMap m = this.canvas.getMap();
         if (m != null) {
            this.isUpdatingFields = true;
            this.txtWidth.setText(String.valueOf(m.width));
            this.txtHeight.setText(String.valueOf(m.height));
            this.isUpdatingFields = false;
            this.updateOutputs();
         }
      });
      this.updateTimer = new Timer(500, e -> this.performUpdateOutputs());
      this.updateTimer.setRepeats(false);
      JScrollPane canvasScroll = new JScrollPane(this.canvas);
      StyledUI.styleScrollPane(canvasScroll);
      canvasScroll.getVerticalScrollBar().setUnitIncrement(32);
      canvasScroll.getHorizontalScrollBar().setUnitIncrement(32);
      ButtonGroup toolGroup = new ButtonGroup();
      JToggleButton btnPencil = this.createToolButton(Lang.get("tool_pencil"), GameMapCanvas.ToolMode.TILE_PENCIL, toolGroup);
      JToggleButton btnEraser = this.createToolButton(Lang.get("tool_eraser"), GameMapCanvas.ToolMode.TILE_ERASER, toolGroup);
      JToggleButton btnBucket = this.createToolButton(Lang.get("tool_bucket"), GameMapCanvas.ToolMode.TILE_BUCKET, toolGroup);
      JToggleButton btnCollision = this.createToolButton(Lang.get("tool_collision"), GameMapCanvas.ToolMode.COLLISION_TOGGLE, toolGroup);
      JToggleButton btnNpc = this.createToolButton(Lang.get("tool_npc"), GameMapCanvas.ToolMode.NPC_PENCIL, toolGroup);
      JToggleButton btnMob = this.createToolButton(Lang.get("tool_mob"), GameMapCanvas.ToolMode.MOB_PENCIL, toolGroup);
      JToggleButton btnVgo = this.createToolButton(Lang.get("tool_vgo"), GameMapCanvas.ToolMode.VGO_PENCIL, toolGroup);
      btnPencil.setSelected(true);
      this.canvas.setOnToolChanged(() -> {
         GameMapCanvas.ToolMode current = this.canvas.getTool();
         if (current == GameMapCanvas.ToolMode.TILE_PENCIL) {
            btnPencil.setSelected(true);
         } else if (current == GameMapCanvas.ToolMode.TILE_ERASER) {
            btnEraser.setSelected(true);
         } else if (current == GameMapCanvas.ToolMode.TILE_BUCKET) {
            btnBucket.setSelected(true);
         } else if (current == GameMapCanvas.ToolMode.COLLISION_TOGGLE) {
            btnCollision.setSelected(true);
         } else if (current == GameMapCanvas.ToolMode.NPC_PENCIL) {
            btnNpc.setSelected(true);
         } else if (current == GameMapCanvas.ToolMode.MOB_PENCIL) {
            btnMob.setSelected(true);
         } else if (current == GameMapCanvas.ToolMode.VGO_PENCIL) {
            btnVgo.setSelected(true);
         }
      });
      JPanel workspacePanel = new JPanel(new BorderLayout());
      workspacePanel.setOpaque(false);
      JPanel canvasToolbar = new JPanel(new FlowLayout(0, 0, 0));
      canvasToolbar.setBackground(Theme.BG_DARK);
      canvasToolbar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      canvasToolbar.add(btnPencil);
      canvasToolbar.add(btnEraser);
      canvasToolbar.add(btnBucket);
      canvasToolbar.add(btnCollision);
      canvasToolbar.add(btnNpc);
      canvasToolbar.add(btnMob);
      canvasToolbar.add(btnVgo);
      JPanel divider = new JPanel();
      divider.setPreferredSize(new Dimension(1, 34));
      divider.setBackground(Theme.BORDER);
      canvasToolbar.add(Box.createHorizontalStrut(10));
      canvasToolbar.add(divider);
      canvasToolbar.add(Box.createHorizontalStrut(10));
      JButton btnUndo = new JButton(Lang.get("tool_undo")) {
         private boolean hovered = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hovered = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hovered = false;
                  repaint();
               }
            });
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(this.hovered ? Theme.BG_HOVER : Theme.BG_CARD);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 6, 6);
            g2.setColor(Theme.BORDER);
            g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 6, 6);
            g2.setColor(this.hovered ? Color.WHITE : Theme.TEXT_DIM);
            g2.setFont(Theme.F_TINY);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (this.getWidth() - fm.stringWidth(this.getText())) / 2;
            int ty = (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(this.getText(), tx, ty);
            g2.dispose();
         }
      };
      btnUndo.setPreferredSize(new Dimension(75, 24));
      btnUndo.setContentAreaFilled(false);
      btnUndo.setBorderPainted(false);
      btnUndo.setFocusPainted(false);
      btnUndo.setCursor(new Cursor(12));
      btnUndo.addActionListener(e -> this.canvas.undo());
      JButton btnRedo = new JButton(Lang.get("tool_redo")) {
         private boolean hovered = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hovered = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hovered = false;
                  repaint();
               }
            });
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(this.hovered ? Theme.BG_HOVER : Theme.BG_CARD);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 6, 6);
            g2.setColor(Theme.BORDER);
            g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 6, 6);
            g2.setColor(this.hovered ? Color.WHITE : Theme.TEXT_DIM);
            g2.setFont(Theme.F_TINY);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (this.getWidth() - fm.stringWidth(this.getText())) / 2;
            int ty = (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(this.getText(), tx, ty);
            g2.dispose();
         }
      };
      btnRedo.setPreferredSize(new Dimension(75, 24));
      btnRedo.setContentAreaFilled(false);
      btnRedo.setBorderPainted(false);
      btnRedo.setFocusPainted(false);
      btnRedo.setCursor(new Cursor(12));
      btnRedo.addActionListener(e -> this.canvas.redo());
      JPanel actionWrapper = new JPanel(new FlowLayout(0, 4, 5));
      actionWrapper.setOpaque(false);
      actionWrapper.add(btnUndo);
      actionWrapper.add(btnRedo);
      canvasToolbar.add(actionWrapper);
      workspacePanel.add(canvasToolbar, "North");
      workspacePanel.add(canvasScroll, "Center");
      JPanel eastContent = this.buildSidebar();
      JScrollPane eastScroll = new JScrollPane(eastContent);
      eastScroll.setBorder(null);
      eastScroll.getVerticalScrollBar().setUnitIncrement(16);
      JSplitPane split = new JSplitPane(1, workspacePanel, eastScroll);
      split.setDividerSize(3);
      split.setContinuousLayout(true);
      split.setDividerLocation(1000);
      split.setResizeWeight(1.0);
      split.setBackground(Theme.BG_DARKER);
      split.setBorder(null);
      this.add(split, "Center");
      JPanel southDock = new JPanel(new BorderLayout());
      southDock.setPreferredSize(new Dimension(0, 240));
      southDock.setBackground(Theme.BG_DARK);
      southDock.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      JTabbedPane paletteTabs = new JTabbedPane();
      paletteTabs.setFont(Theme.F_SMALL);
      this.tilePalette = new TilePalettePanel(false);
      this.tilePalette.setTileSelectionListener(id -> {
         this.canvas.setSelectedTile(id);
         this.canvas.setTool(GameMapCanvas.ToolMode.TILE_PENCIL);
      });
      JScrollPane scrollTile = new JScrollPane(this.tilePalette);
      StyledUI.styleScrollPane(scrollTile);
      paletteTabs.addTab("  " + Lang.get("tab_tile") + "  ", scrollTile);
      this.waterPalette = new TilePalettePanel(true);
      this.waterPalette.setTileSelectionListener(id -> {
         int fWater = TileMapConfig.getfWater(this.canvas.getMap().getTileSetId());
         this.canvas.setSelectedTile(fWater - 1 + id);
         this.canvas.setTool(GameMapCanvas.ToolMode.TILE_PENCIL);
      });
      JScrollPane scrollWater = new JScrollPane(this.waterPalette);
      StyledUI.styleScrollPane(scrollWater);
      paletteTabs.addTab("  WATER  ", scrollWater);
      this.itemPalette = new ItemPalettePanel();
      this.itemPalette.setItemSelectionListener(id -> {
         this.canvas.setSelectedTemplateId(id);
         this.canvas.setTool(GameMapCanvas.ToolMode.ITEM_PENCIL);
         this.loadTemplateProperties(id);
      });
      JScrollPane scrollItem = new JScrollPane(this.itemPalette);
      StyledUI.styleScrollPane(scrollItem);
      JPanel itemPanel = new JPanel(new BorderLayout());
      itemPanel.add(scrollItem, "Center");
      JPanel layerControlPanel = new JPanel(new BorderLayout());
      layerControlPanel.setBackground(Theme.BG_DARK);
      layerControlPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
      JLabel layerLabel = StyledUI.createLabel("Item Layer:", Theme.F_TINY, Theme.TEXT_MUTED);
      this.itemLayerCombo = new JComboBox<>(
         new String[]{"-1 (No Data)", "0 (BG)", "1 (Bottom)", "2 (Middle)", "3 (Player Sorted)", "4 (Sorted)", "5 (Top)", "6 (Extra)"}
      );
      this.itemLayerCombo.setFont(Theme.F_TINY);
      this.itemLayerCombo.setBackground(Theme.BG_CARD);
      this.itemLayerCombo.setForeground(Theme.TEXT_MAIN);
      this.itemLayerCombo.setSelectedIndex(0);
      this.itemLayerCombo.addActionListener(e -> {
         int selectedLayer = this.itemLayerCombo.getSelectedIndex() - 1;
         this.canvas.setSelectedItemLayer(selectedLayer);
      });
      layerControlPanel.add(layerLabel, "West");
      layerControlPanel.add(this.itemLayerCombo, "Center");
      itemPanel.add(layerControlPanel, "South");
      paletteTabs.addTab("  ITEMS  ", itemPanel);
      ObjectPalettePanel objectPalette = new ObjectPalettePanel(obj -> {
         String s = (String)obj;
         if (s.startsWith("NPC:")) {
            this.canvas.setSelectedTemplateId(Short.parseShort(s.substring(4)));
            this.canvas.setTool(GameMapCanvas.ToolMode.NPC_PENCIL);
         } else if (s.startsWith("MOB:")) {
            this.canvas.setSelectedTemplateId(Short.parseShort(s.substring(4)));
            this.canvas.setTool(GameMapCanvas.ToolMode.MOB_PENCIL);
         } else if (s.startsWith("VGO:")) {
            String[] parts = s.substring(4).split(",");
            this.canvas.setTargetVgo(Short.parseShort(parts[0]), Short.parseShort(parts[1]), Short.parseShort(parts[2]));
            this.canvas.setTool(GameMapCanvas.ToolMode.VGO_PENCIL);
         }
      });
      JScrollPane scrollObject = new JScrollPane(objectPalette);
      StyledUI.styleScrollPane(scrollObject);
      JPanel objectsPanel = new JPanel(new BorderLayout());
      objectsPanel.setBackground(Theme.BG_DARK);
      JTabbedPane liveTabs = new JTabbedPane();
      liveTabs.setFont(Theme.F_TINY);
      JPanel panelNpcs = new JPanel(new BorderLayout());
      panelNpcs.setBackground(Theme.BG_DARK);
      String[] npcCols = new String[]{"Index", "ID", "Name", "Name GT", "Chat", "X", "Y", "Head", "Hair"};
      this.npcsModel = new DefaultTableModel(npcCols, 0) {
         @Override
         public boolean isCellEditable(int row, int col) {
            return false;
         }
      };
      this.tableNpcs = new JTable(this.npcsModel);
      this.styleTable(this.tableNpcs);
      JScrollPane scrollNpcs = new JScrollPane(this.tableNpcs);
      StyledUI.styleScrollPane(scrollNpcs);
      panelNpcs.add(scrollNpcs, "Center");
      JPanel editNpcPanel = new JPanel(new FlowLayout(0, 8, 4));
      editNpcPanel.setBackground(Theme.BG_DARK);
      JTextField tfNpcName = StyledUI.createTextField("");
      tfNpcName.setPreferredSize(new Dimension(80, 24));
      JTextField tfNpcNameGt = StyledUI.createTextField("");
      tfNpcNameGt.setPreferredSize(new Dimension(80, 24));
      JTextField tfNpcChat = StyledUI.createTextField("");
      tfNpcChat.setPreferredSize(new Dimension(100, 24));
      JTextField tfNpcX = StyledUI.createTextField("");
      tfNpcX.setPreferredSize(new Dimension(40, 24));
      JTextField tfNpcY = StyledUI.createTextField("");
      tfNpcY.setPreferredSize(new Dimension(40, 24));
      JTextField tfNpcHead = StyledUI.createTextField("");
      tfNpcHead.setPreferredSize(new Dimension(40, 24));
      JTextField tfNpcHair = StyledUI.createTextField("");
      tfNpcHair.setPreferredSize(new Dimension(40, 24));
      JButton btnUpdateNpc = StyledUI.createButton("UPDATE", Theme.ACCENT);
      btnUpdateNpc.setPreferredSize(new Dimension(75, 24));
      JButton btnDeleteNpc = StyledUI.createButton("DELETE", Theme.ERROR);
      btnDeleteNpc.setPreferredSize(new Dimension(75, 24));
      editNpcPanel.add(StyledUI.createLabel("Name:", Theme.F_TINY, Theme.TEXT_MUTED));
      editNpcPanel.add(tfNpcName);
      editNpcPanel.add(StyledUI.createLabel("Gt:", Theme.F_TINY, Theme.TEXT_MUTED));
      editNpcPanel.add(tfNpcNameGt);
      editNpcPanel.add(StyledUI.createLabel("Chat:", Theme.F_TINY, Theme.TEXT_MUTED));
      editNpcPanel.add(tfNpcChat);
      editNpcPanel.add(StyledUI.createLabel("X:", Theme.F_TINY, Theme.TEXT_MUTED));
      editNpcPanel.add(tfNpcX);
      editNpcPanel.add(StyledUI.createLabel("Y:", Theme.F_TINY, Theme.TEXT_MUTED));
      editNpcPanel.add(tfNpcY);
      editNpcPanel.add(StyledUI.createLabel("Hd:", Theme.F_TINY, Theme.TEXT_MUTED));
      editNpcPanel.add(tfNpcHead);
      editNpcPanel.add(StyledUI.createLabel("Hr:", Theme.F_TINY, Theme.TEXT_MUTED));
      editNpcPanel.add(tfNpcHair);
      editNpcPanel.add(btnUpdateNpc);
      editNpcPanel.add(btnDeleteNpc);
      panelNpcs.add(editNpcPanel, "South");
      liveTabs.addTab("NPCs", panelNpcs);
      this.tableNpcs.getSelectionModel().addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int row = this.tableNpcs.getSelectedRow();
            if (row != -1) {
               GameMap map = this.canvas.getMap();
               if (map != null && row < map.npcs.size()) {
                  GameMap.Npc npc = map.npcs.get(row);
                  tfNpcName.setText(npc.name);
                  tfNpcNameGt.setText(npc.namegt);
                  tfNpcChat.setText(npc.chat);
                  tfNpcX.setText(String.valueOf(npc.x));
                  tfNpcY.setText(String.valueOf(npc.y));
                  tfNpcHead.setText(String.valueOf(npc.head));
                  tfNpcHair.setText(String.valueOf(npc.hair));
               }
            }
         }
      });
      btnUpdateNpc.addActionListener(al -> {
         int row = this.tableNpcs.getSelectedRow();
         if (row != -1) {
            GameMap map = this.canvas.getMap();
            if (map != null && row < map.npcs.size()) {
               GameMap.Npc npc = map.npcs.get(row);

               try {
                  npc.name = tfNpcName.getText().trim();
                  npc.namegt = tfNpcNameGt.getText().trim();
                  npc.chat = tfNpcChat.getText().trim();
                  npc.x = Short.parseShort(tfNpcX.getText().trim());
                  npc.y = Short.parseShort(tfNpcY.getText().trim());
                  npc.head = Short.parseShort(tfNpcHead.getText().trim());
                  npc.hair = Short.parseShort(tfNpcHair.getText().trim());
                  this.updateObjectLists();
                  this.updateOutputs();
                  this.canvas.repaint();
               } catch (Exception var13x) {
                  JOptionPane.showMessageDialog(this, "Invalid format: " + var13x.getMessage());
               }
            }
         }
      });
      btnDeleteNpc.addActionListener(al -> {
         int row = this.tableNpcs.getSelectedRow();
         if (row != -1) {
            GameMap map = this.canvas.getMap();
            if (map != null && row < map.npcs.size()) {
               map.npcs.remove(row);
               this.updateObjectLists();
               this.updateOutputs();
               this.canvas.repaint();
            }
         }
      });
      JPanel panelMobs = new JPanel(new BorderLayout());
      panelMobs.setBackground(Theme.BG_DARK);
      String[] mobCols = new String[]{"Index", "Template ID", "X", "Y"};
      this.mobsModel = new DefaultTableModel(mobCols, 0) {
         @Override
         public boolean isCellEditable(int row, int col) {
            return false;
         }
      };
      this.tableMobs = new JTable(this.mobsModel);
      this.styleTable(this.tableMobs);
      JScrollPane scrollMobs = new JScrollPane(this.tableMobs);
      StyledUI.styleScrollPane(scrollMobs);
      panelMobs.add(scrollMobs, "Center");
      JPanel editMobPanel = new JPanel(new FlowLayout(0, 8, 4));
      editMobPanel.setBackground(Theme.BG_DARK);
      JTextField tfMobId = StyledUI.createTextField("");
      tfMobId.setPreferredSize(new Dimension(80, 24));
      JTextField tfMobX = StyledUI.createTextField("");
      tfMobX.setPreferredSize(new Dimension(50, 24));
      JTextField tfMobY = StyledUI.createTextField("");
      tfMobY.setPreferredSize(new Dimension(50, 24));
      JButton btnUpdateMob = StyledUI.createButton("UPDATE", Theme.ACCENT);
      btnUpdateMob.setPreferredSize(new Dimension(75, 24));
      JButton btnDeleteMob = StyledUI.createButton("DELETE", Theme.ERROR);
      btnDeleteMob.setPreferredSize(new Dimension(75, 24));
      editMobPanel.add(StyledUI.createLabel("Template ID:", Theme.F_TINY, Theme.TEXT_MUTED));
      editMobPanel.add(tfMobId);
      editMobPanel.add(StyledUI.createLabel("X:", Theme.F_TINY, Theme.TEXT_MUTED));
      editMobPanel.add(tfMobX);
      editMobPanel.add(StyledUI.createLabel("Y:", Theme.F_TINY, Theme.TEXT_MUTED));
      editMobPanel.add(tfMobY);
      editMobPanel.add(btnUpdateMob);
      editMobPanel.add(btnDeleteMob);
      panelMobs.add(editMobPanel, "South");
      liveTabs.addTab("Mobs", panelMobs);
      this.tableMobs.getSelectionModel().addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int row = this.tableMobs.getSelectedRow();
            if (row != -1) {
               GameMap map = this.canvas.getMap();
               if (map != null && row < map.list_mob.size()) {
                  GameMap.Mob mob = map.list_mob.get(row);
                  tfMobId.setText(String.valueOf(mob.templateId));
                  tfMobX.setText(String.valueOf(mob.x));
                  tfMobY.setText(String.valueOf(mob.y));
               }
            }
         }
      });
      btnUpdateMob.addActionListener(al -> {
         int row = this.tableMobs.getSelectedRow();
         if (row != -1) {
            GameMap map = this.canvas.getMap();
            if (map != null && row < map.list_mob.size()) {
               GameMap.Mob mob = map.list_mob.get(row);

               try {
                  mob.templateId = Integer.parseInt(tfMobId.getText().trim());
                  mob.x = Short.parseShort(tfMobX.getText().trim());
                  mob.y = Short.parseShort(tfMobY.getText().trim());
                  this.updateObjectLists();
                  this.updateOutputs();
                  this.canvas.repaint();
               } catch (Exception var9x) {
                  JOptionPane.showMessageDialog(this, "Invalid format: " + var9x.getMessage());
               }
            }
         }
      });
      btnDeleteMob.addActionListener(al -> {
         int row = this.tableMobs.getSelectedRow();
         if (row != -1) {
            GameMap map = this.canvas.getMap();
            if (map != null && row < map.list_mob.size()) {
               map.list_mob.remove(row);
               this.updateObjectLists();
               this.updateOutputs();
               this.canvas.repaint();
            }
         }
      });
      JPanel panelItems = new JPanel(new BorderLayout());
      panelItems.setBackground(Theme.BG_DARK);
      String[] itemCols = new String[]{"Index", "Template ID", "Tile X", "Tile Y", "dx", "dy", "Layer"};
      this.itemsModel = new DefaultTableModel(itemCols, 0) {
         @Override
         public boolean isCellEditable(int row, int col) {
            return false;
         }
      };
      this.tableItems = new JTable(this.itemsModel);
      this.styleTable(this.tableItems);
      JScrollPane scrollItems = new JScrollPane(this.tableItems);
      StyledUI.styleScrollPane(scrollItems);
      panelItems.add(scrollItems, "Center");
      JPanel editItemPanel = new JPanel(new FlowLayout(0, 8, 4));
      editItemPanel.setBackground(Theme.BG_DARK);
      JTextField tfItemTileX = StyledUI.createTextField("");
      tfItemTileX.setPreferredSize(new Dimension(40, 24));
      JTextField tfItemTileY = StyledUI.createTextField("");
      tfItemTileY.setPreferredSize(new Dimension(40, 24));
      JTextField tfItemDx = StyledUI.createTextField("");
      tfItemDx.setPreferredSize(new Dimension(40, 24));
      JTextField tfItemDy = StyledUI.createTextField("");
      tfItemDy.setPreferredSize(new Dimension(40, 24));
      JComboBox<String> cbItemLayer = new JComboBox<>(
         new String[]{"-1 (No Data)", "0 (BG)", "1 (Bottom)", "2 (Middle)", "3 (Player Sorted)", "4 (Sorted)", "5 (Top)", "6 (Extra)"}
      );
      cbItemLayer.setFont(Theme.F_TINY);
      cbItemLayer.setBackground(Theme.BG_CARD);
      cbItemLayer.setForeground(Theme.TEXT_MAIN);
      cbItemLayer.setPreferredSize(new Dimension(130, 24));
      JButton btnUpdateItem = StyledUI.createButton("UPDATE & SAVE TEMPLATE", Theme.SUCCESS);
      btnUpdateItem.setPreferredSize(new Dimension(190, 24));
      JButton btnDeleteItem = StyledUI.createButton("DELETE", Theme.ERROR);
      btnDeleteItem.setPreferredSize(new Dimension(75, 24));
      editItemPanel.add(StyledUI.createLabel("Tile X:", Theme.F_TINY, Theme.TEXT_MUTED));
      editItemPanel.add(tfItemTileX);
      editItemPanel.add(StyledUI.createLabel("Tile Y:", Theme.F_TINY, Theme.TEXT_MUTED));
      editItemPanel.add(tfItemTileY);
      editItemPanel.add(StyledUI.createLabel("dx:", Theme.F_TINY, Theme.TEXT_MUTED));
      editItemPanel.add(tfItemDx);
      editItemPanel.add(StyledUI.createLabel("dy:", Theme.F_TINY, Theme.TEXT_MUTED));
      editItemPanel.add(tfItemDy);
      editItemPanel.add(StyledUI.createLabel("Layer:", Theme.F_TINY, Theme.TEXT_MUTED));
      editItemPanel.add(cbItemLayer);
      editItemPanel.add(btnUpdateItem);
      editItemPanel.add(btnDeleteItem);
      panelItems.add(editItemPanel, "South");
      liveTabs.addTab("Placed Items", panelItems);
      this.tableItems.getSelectionModel().addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int row = this.tableItems.getSelectedRow();
            if (row != -1) {
               GameMap map = this.canvas.getMap();
               if (map != null && row < map.items.size()) {
                  ItemMapEntity item = map.items.get(row);
                  tfItemTileX.setText(String.valueOf(item.tileX));
                  tfItemTileY.setText(String.valueOf(item.tileY));
                  if (item.template != null) {
                     tfItemDx.setText(String.valueOf(item.template.dx));
                     tfItemDy.setText(String.valueOf(item.template.dy));
                     cbItemLayer.setSelectedIndex(item.template.layer + 1);
                  } else {
                     tfItemDx.setText("0");
                     tfItemDy.setText("0");
                     cbItemLayer.setSelectedIndex(0);
                  }
               }
            }
         }
      });
      btnUpdateItem.addActionListener(al -> {
         int row = this.tableItems.getSelectedRow();
         if (row != -1) {
            GameMap map = this.canvas.getMap();
            if (map != null && row < map.items.size()) {
               ItemMapEntity item = map.items.get(row);

               try {
                  int oldX = item.tileX;
                  int oldY = item.tileY;
                  int newX = Integer.parseInt(tfItemTileX.getText().trim());
                  int newY = Integer.parseInt(tfItemTileY.getText().trim());
                  if (oldX != newX || oldY != newY) {
                     map.removeItem(oldX, oldY);
                     item.tileX = newX;
                     item.tileY = newY;
                     map.addItem(item.templateId, newX, newY, cbItemLayer.getSelectedIndex() - 1);
                  }

                  if (item.template != null) {
                     item.template.dx = Short.parseShort(tfItemDx.getText().trim());
                     item.template.dy = Short.parseShort(tfItemDy.getText().trim());
                     item.template.layer = (byte)(cbItemLayer.getSelectedIndex() - 1);
                     TemplateManager.gI().saveItemTemplate(item.template);
                  }

                  map.removeItem(item.tileX, item.tileY);
                  map.addItem(item.templateId, item.tileX, item.tileY, cbItemLayer.getSelectedIndex() - 1);
                  this.updateObjectLists();
                  this.updateOutputs();
                  this.canvas.loadItemImages();
                  this.canvas.updateItemCache();
                  this.canvas.repaint();
               } catch (Exception var14x) {
                  JOptionPane.showMessageDialog(this, "Invalid format: " + var14x.getMessage());
               }
            }
         }
      });
      btnDeleteItem.addActionListener(al -> {
         int row = this.tableItems.getSelectedRow();
         if (row != -1) {
            GameMap map = this.canvas.getMap();
            if (map != null && row < map.items.size()) {
               ItemMapEntity item = map.items.get(row);
               map.removeItem(item.tileX, item.tileY);
               this.updateObjectLists();
               this.updateOutputs();
               this.canvas.repaint();
            }
         }
      });
      JPanel panelVgos = new JPanel(new BorderLayout());
      panelVgos.setBackground(Theme.BG_DARK);
      String[] vgoCols = new String[]{"Index", "Target Map ID", "X Old", "Y Old", "X New", "Y New"};
      this.vgosModel = new DefaultTableModel(vgoCols, 0) {
         @Override
         public boolean isCellEditable(int row, int col) {
            return false;
         }
      };
      this.tableVgos = new JTable(this.vgosModel);
      this.styleTable(this.tableVgos);
      JScrollPane scrollVgos = new JScrollPane(this.tableVgos);
      StyledUI.styleScrollPane(scrollVgos);
      panelVgos.add(scrollVgos, "Center");
      JPanel editVgoPanel = new JPanel(new FlowLayout(0, 8, 4));
      editVgoPanel.setBackground(Theme.BG_DARK);
      JTextField tfVgoMap = StyledUI.createTextField("");
      tfVgoMap.setPreferredSize(new Dimension(60, 24));
      JTextField tfVgoXOld = StyledUI.createTextField("");
      tfVgoXOld.setPreferredSize(new Dimension(40, 24));
      JTextField tfVgoYOld = StyledUI.createTextField("");
      tfVgoYOld.setPreferredSize(new Dimension(40, 24));
      JTextField tfVgoXNew = StyledUI.createTextField("");
      tfVgoXNew.setPreferredSize(new Dimension(40, 24));
      JTextField tfVgoYNew = StyledUI.createTextField("");
      tfVgoYNew.setPreferredSize(new Dimension(40, 24));
      JButton btnUpdateVgo = StyledUI.createButton("UPDATE", Theme.ACCENT);
      btnUpdateVgo.setPreferredSize(new Dimension(75, 24));
      JButton btnDeleteVgo = StyledUI.createButton("DELETE", Theme.ERROR);
      btnDeleteVgo.setPreferredSize(new Dimension(75, 24));
      editVgoPanel.add(StyledUI.createLabel("Target Map:", Theme.F_TINY, Theme.TEXT_MUTED));
      editVgoPanel.add(tfVgoMap);
      editVgoPanel.add(StyledUI.createLabel("X Old:", Theme.F_TINY, Theme.TEXT_MUTED));
      editVgoPanel.add(tfVgoXOld);
      editVgoPanel.add(StyledUI.createLabel("Y Old:", Theme.F_TINY, Theme.TEXT_MUTED));
      editVgoPanel.add(tfVgoYOld);
      editVgoPanel.add(StyledUI.createLabel("X New:", Theme.F_TINY, Theme.TEXT_MUTED));
      editVgoPanel.add(tfVgoXNew);
      editVgoPanel.add(StyledUI.createLabel("Y New:", Theme.F_TINY, Theme.TEXT_MUTED));
      editVgoPanel.add(tfVgoYNew);
      editVgoPanel.add(btnUpdateVgo);
      editVgoPanel.add(btnDeleteVgo);
      panelVgos.add(editVgoPanel, "South");
      liveTabs.addTab("VGOs", panelVgos);
      this.tableVgos.getSelectionModel().addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            int row = this.tableVgos.getSelectedRow();
            if (row != -1) {
               GameMap map = this.canvas.getMap();
               if (map != null && row < map.vgos.size()) {
                  GameMap.Vgo vgo = map.vgos.get(row);
                  tfVgoMap.setText(String.valueOf(vgo.id_map_go));
                  tfVgoXOld.setText(String.valueOf(vgo.xold));
                  tfVgoYOld.setText(String.valueOf(vgo.yold));
                  tfVgoXNew.setText(String.valueOf(vgo.xnew));
                  tfVgoYNew.setText(String.valueOf(vgo.ynew));
               }
            }
         }
      });
      btnUpdateVgo.addActionListener(al -> {
         int row = this.tableVgos.getSelectedRow();
         if (row != -1) {
            GameMap map = this.canvas.getMap();
            if (map != null && row < map.vgos.size()) {
               GameMap.Vgo vgo = map.vgos.get(row);

               try {
                  vgo.id_map_go = Short.parseShort(tfVgoMap.getText().trim());
                  vgo.xold = Short.parseShort(tfVgoXOld.getText().trim());
                  vgo.yold = Short.parseShort(tfVgoYOld.getText().trim());
                  vgo.xnew = Short.parseShort(tfVgoXNew.getText().trim());
                  vgo.ynew = Short.parseShort(tfVgoYNew.getText().trim());
                  this.updateObjectLists();
                  this.updateOutputs();
                  this.canvas.repaint();
               } catch (Exception var11x) {
                  JOptionPane.showMessageDialog(this, "Invalid format: " + var11x.getMessage());
               }
            }
         }
      });
      btnDeleteVgo.addActionListener(al -> {
         int row = this.tableVgos.getSelectedRow();
         if (row != -1) {
            GameMap map = this.canvas.getMap();
            if (map != null && row < map.vgos.size()) {
               map.vgos.remove(row);
               this.updateObjectLists();
               this.updateOutputs();
               this.canvas.repaint();
            }
         }
      });
      JSplitPane objectSplit = new JSplitPane(1, liveTabs, scrollObject);
      objectSplit.setDividerLocation(820);
      objectSplit.setDividerSize(2);
      objectSplit.setBackground(Theme.BG_DARK);
      objectSplit.setBorder(null);
      paletteTabs.addTab("  OBJECTS  ", objectSplit);
      southDock.add(paletteTabs, "Center");
      this.add(southDock, "South");
      this.loadInitialData();
   }

   private JPanel buildSidebar() {
      JPanel eastContent = new JPanel();
      eastContent.setLayout(new BoxLayout(eastContent, 1));
      eastContent.setBackground(Theme.BG_DARK);
      JPanel dbSection = this.createSection("PROJECT & DATABASE");
      JPanel formatPanel = new JPanel(new BorderLayout(5, 5));
      formatPanel.setOpaque(false);
      JLabel formatLabel = StyledUI.createLabel("DATA FORMAT: ", Theme.F_TINY, Theme.TEXT_MUTED);
      JComboBox<String> cbFormat = new JComboBox<>(new String[]{"NEW (SQL/JSON)", "OLD (BINARY)"});
      cbFormat.setFont(Theme.F_TINY);
      cbFormat.setSelectedIndex(MapDataLoader.getSaveFormat() == MapDataLoader.SaveFormat.OLD_FORMAT ? 1 : 0);
      cbFormat.addActionListener(e -> {
         MapDataLoader.SaveFormat selected = cbFormat.getSelectedIndex() == 1 ? MapDataLoader.SaveFormat.OLD_FORMAT : MapDataLoader.SaveFormat.NEW_FORMAT;
         MapDataLoader.setSaveFormat(selected);
         this.loadInitialData();
      });
      formatPanel.add(formatLabel, "West");
      formatPanel.add(cbFormat, "Center");
      dbSection.add(formatPanel);
      dbSection.add(Box.createVerticalStrut(10));
      JPanel fileTools = new JPanel(new GridLayout(4, 2, 5, 5));
      fileTools.setOpaque(false);
      JButton btnNewMap = StyledUI.createButton("NEW MAP", Theme.ACCENT);
      JButton btnSaveDb = StyledUI.createButton("SAVE DB", Theme.SUCCESS);
      JButton btnImportSql = StyledUI.createButton("IMPORT SQL", new Color(138, 43, 226));
      JButton btnExportClient = StyledUI.createButton("EXPORT CLIENT", new Color(0, 150, 200));
      JButton btnExportPng = StyledUI.createButton("\ud83d\udcf7 XUẤT ẢNH PNG", new Color(0, 180, 130));
      JButton btnExportBinary = StyledUI.createButton("\ud83d\udcbe XUẤT BINARY", new Color(220, 100, 0));
      JButton btnClearMap = StyledUI.createButton("CLEAR MAP", new Color(178, 34, 34));
      JButton btnPreviewMap = StyledUI.createButton("PREVIEW MAP", new Color(255, 140, 0));
      fileTools.add(btnNewMap);
      fileTools.add(btnSaveDb);
      fileTools.add(btnImportSql);
      fileTools.add(btnExportClient);
      fileTools.add(btnExportPng);
      fileTools.add(btnExportBinary);
      fileTools.add(btnClearMap);
      fileTools.add(btnPreviewMap);
      dbSection.add(fileTools);
      dbSection.add(Box.createVerticalStrut(6));
      JButton btnBatchExportAll = StyledUI.createButton("\ud83d\ude80 XUẤT TẤT CẢ MAP (PNG + BINARY + SQL)", new Color(75, 0, 130));
      btnBatchExportAll.setPreferredSize(new Dimension(0, 32));
      btnBatchExportAll.setMaximumSize(new Dimension(32767, 32));
      btnBatchExportAll.addActionListener(e -> this.exportBatchAll());
      dbSection.add(btnBatchExportAll);
      dbSection.add(Box.createVerticalStrut(6));
      JButton btnAutoGenMap = StyledUI.createButton("\ud83c\udfb2 AUTO GENERATE MAP", new Color(0, 150, 100));
      btnAutoGenMap.setPreferredSize(new Dimension(0, 32));
      btnAutoGenMap.setMaximumSize(new Dimension(32767, 32));
      btnAutoGenMap.addActionListener(e -> {
         Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
         AutoMapGeneratorDialog dlg = new AutoMapGeneratorDialog(owner, this.canvas.getMap());
         dlg.setApplyCallback(newMap -> {
            this.mapListModel.addElement(newMap);
            this.mapList.setSelectedValue(newMap, true);
            this.loadMapToEditor(newMap);
            JOptionPane.showMessageDialog(this, "✅ Map mới được tự động tạo thành công: " + newMap.name + " (#" + newMap.id + ")", "Auto Map Success", 1);
         });
         dlg.setVisible(true);
      });
      dbSection.add(btnAutoGenMap);
      dbSection.add(Box.createVerticalStrut(10));
      this.mapListModel = new DefaultListModel<>();
      this.mapList = new JList<>(this.mapListModel);
      this.mapList.setCellRenderer(new MainUI.MapListRenderer());
      this.mapList.setBackground(Theme.BG_CARD);
      this.mapList.setForeground(Theme.TEXT_MAIN);
      this.mapList.addListSelectionListener(e -> {
         if (!e.getValueIsAdjusting()) {
            GameMap selected = this.mapList.getSelectedValue();
            if (selected != null) {
               this.loadMapToEditor(selected);
            }
         }
      });
      JScrollPane listScroll = new JScrollPane(this.mapList);
      listScroll.setPreferredSize(new Dimension(280, 180));
      listScroll.setBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER));
      dbSection.add(listScroll);
      eastContent.add(dbSection);
      btnNewMap.addActionListener(e -> this.showNewMapDialog());
      btnSaveDb.addActionListener(e -> this.saveCurrentMap());
      btnImportSql.addActionListener(e -> this.showImportSqlDialog());
      btnExportClient.addActionListener(e -> this.exportClientData());
      btnExportPng.addActionListener(e -> this.exportCurrentMapPng());
      btnExportBinary.addActionListener(e -> this.exportCurrentMapBinary());
      btnPreviewMap.addActionListener(e -> this.previewMap());
      btnClearMap.addActionListener(e -> this.clearCurrentMap());
      JPanel viewSection = this.createSection("VIEW OPTIONS");
      JPanel viewGrid = new JPanel(new GridLayout(0, 2, 5, 2));
      viewGrid.setOpaque(false);
      JCheckBox chkGrid = StyledUI.createCheckBox("SHOW GRID");
      chkGrid.addActionListener(e -> this.canvas.setShowGrid(chkGrid.isSelected()));
      JCheckBox chkCollision = StyledUI.createCheckBox("COLLISIONS");
      chkCollision.addActionListener(e -> this.canvas.setShowCollision(chkCollision.isSelected()));
      JCheckBox chkNPC = StyledUI.createCheckBox("SHOW NPCS");
      chkNPC.setSelected(true);
      chkNPC.addActionListener(e -> this.canvas.setShowNPC(chkNPC.isSelected()));
      JCheckBox chkMob = StyledUI.createCheckBox("SHOW MOBS");
      chkMob.setSelected(true);
      chkMob.addActionListener(e -> this.canvas.setShowMob(chkMob.isSelected()));
      JCheckBox chkVgo = StyledUI.createCheckBox("SHOW VGOS");
      chkVgo.setSelected(true);
      chkVgo.addActionListener(e -> this.canvas.setShowVGO(chkVgo.isSelected()));
      viewGrid.add(chkGrid);
      viewGrid.add(chkCollision);
      viewGrid.add(chkNPC);
      viewGrid.add(chkMob);
      viewGrid.add(chkVgo);
      viewSection.add(viewGrid);
      eastContent.add(viewSection);
      JPanel propSection = this.createSection("MAP PROPERTIES");
      propSection.setLayout(new GridLayout(0, 1, 2, 4));
      this.txtName = this.addPropField(propSection, Lang.get("map_name"));
      this.txtWidth = this.addPropField(propSection, Lang.get("map_width"));
      this.txtHeight = this.addPropField(propSection, Lang.get("map_height"));
      this.txtTileSet = this.addPropField(propSection, Lang.get("map_tileset"));
      this.txtLevel = this.addPropField(propSection, Lang.get("map_level"));
      this.txtMaxZone = this.addPropField(propSection, Lang.get("map_zones"));
      this.txtMaxPlayer = this.addPropField(propSection, Lang.get("map_players"));
      eastContent.add(propSection);
      JPanel layerSection = this.createSection("LAYER VISIBILITY");
      JPanel layerGrid = new JPanel(new GridLayout(0, 2, 5, 2));
      layerGrid.setOpaque(false);
      String[] layerNames = new String[]{"Tiles", "Water", "Items BG", "Items Bottom", "Items Middle", "Items Player", "Items Sorted", "Items Top"};

      for (int i = 0; i < layerNames.length; i++) {
         int idx = i;
         JCheckBox cb = StyledUI.createCheckBox(layerNames[i]);
         cb.setSelected(true);
         cb.addActionListener(e -> this.canvas.setLayerVisible(idx, cb.isSelected()));
         layerGrid.add(cb);
      }

      layerSection.add(layerGrid);
      eastContent.add(layerSection);
      JPanel templateSection = this.createSection("ITEM TEMPLATE EDITOR");
      templateSection.setLayout(new GridLayout(0, 1, 2, 4));
      this.txtTemplateDx = this.addPropField(templateSection, "Offset X (dx)");
      this.txtTemplateDy = this.addPropField(templateSection, "Offset Y (dy)");
      this.txtTemplateBlock = this.addPropField(templateSection, "Block Points (x,y;x,y)");
      this.txtTemplateTags = this.addPropFieldNoAuto(templateSection, "Tags/Keywords (tree, house, rock)");
      JPanel templateBtnRow = new JPanel(new GridLayout(1, 2, 5, 0));
      templateBtnRow.setOpaque(false);
      JButton btnSaveTemplate = StyledUI.createButton("SAVE TEMPLATE", Theme.ACCENT);
      btnSaveTemplate.addActionListener(e -> this.saveTemplateProperties());
      JButton btnSaveTags = StyledUI.createButton("SAVE TAGS \ud83c\udff7", new Color(35195));
      btnSaveTags.addActionListener(e -> this.saveTemplateTags());
      templateBtnRow.add(btnSaveTemplate);
      templateBtnRow.add(btnSaveTags);
      templateSection.add(templateBtnRow);
      eastContent.add(templateSection);
      JPanel seasonSection = this.createSection("\ud83c\udfad  SEASONAL TRANSFORM");
      seasonSection.setLayout(new BoxLayout(seasonSection, 1));
      JPanel seasonRow = new JPanel(new BorderLayout(6, 0));
      seasonRow.setOpaque(false);
      JLabel lblSznPick = StyledUI.createLabel("Season:", Theme.F_TINY, Theme.TEXT_MUTED);
      lblSznPick.setPreferredSize(new Dimension(52, 24));
      final JComboBox<SeasonTheme> cmbSeasonSidebar = new JComboBox<>(SeasonThemeManager.gI().getAllThemes().toArray(new SeasonTheme[0]));
      cmbSeasonSidebar.setFont(new Font("Segoe UI Emoji", 0, 11));
      cmbSeasonSidebar.setBackground(Theme.BG_CARD);
      cmbSeasonSidebar.setForeground(Theme.TEXT_MAIN);
      cmbSeasonSidebar.setRenderer(new DefaultListCellRenderer() {
         @Override
         public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean foc) {
            super.getListCellRendererComponent(l, v, i, sel, foc);
            if (v instanceof SeasonTheme t) {
               this.setText(t.emoji + " " + t.name);
            }

            this.setBackground(sel ? Theme.ACCENT_SOFT : Theme.BG_CARD);
            this.setForeground(sel ? Color.WHITE : Theme.TEXT_MAIN);
            return this;
         }
      });
      seasonRow.add(lblSznPick, "West");
      seasonRow.add(cmbSeasonSidebar, "Center");
      seasonSection.add(seasonRow);
      seasonSection.add(Box.createVerticalStrut(6));
      JLabel lblSznStats = StyledUI.createLabel("\ud83c\udff7 Tag 0 items / select season", Theme.F_TINY, Theme.TEXT_DIM);
      lblSznStats.setAlignmentX(0.0F);
      seasonSection.add(lblSznStats);
      seasonSection.add(Box.createVerticalStrut(8));
      cmbSeasonSidebar.addActionListener(e -> {
         SeasonTheme theme = (SeasonTheme)cmbSeasonSidebar.getSelectedItem();
         GameMap map = this.canvas.getMap();
         if (theme != null && map != null) {
            SeasonThemeManager.TransformPreview prev = SeasonThemeManager.gI().previewTransform(map, theme);
            lblSznStats.setText(theme.emoji + " " + prev.matchedItems + " / " + prev.totalItems + " items matched");
         }
      });
      JButton btnMakeSeasonal = new JButton("✨  MAKE SEASONAL") {
         private boolean hovered = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hovered = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hovered = false;
                  repaint();
               }
            });
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            SeasonTheme t = (SeasonTheme)cmbSeasonSidebar.getSelectedItem();
            Color base = t != null ? t.accentColor : Theme.ACCENT;
            g2.setPaint(new GradientPaint(0.0F, 0.0F, this.hovered ? base.brighter() : base, 0.0F, this.getHeight(), base.darker()));
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", 1, 11));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (this.getWidth() - fm.stringWidth(this.getText())) / 2;
            int ty = (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(this.getText(), tx, ty);
            g2.dispose();
         }
      };
      btnMakeSeasonal.setPreferredSize(new Dimension(0, 32));
      btnMakeSeasonal.setMaximumSize(new Dimension(32767, 32));
      btnMakeSeasonal.setContentAreaFilled(false);
      btnMakeSeasonal.setBorderPainted(false);
      btnMakeSeasonal.setFocusPainted(false);
      btnMakeSeasonal.setCursor(new Cursor(12));
      btnMakeSeasonal.setAlignmentX(0.0F);
      btnMakeSeasonal.addActionListener(e -> {
         GameMap map = this.canvas.getMap();
         if (map == null) {
            JOptionPane.showMessageDialog(this, "Load một map trước!");
         } else {
            Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
            SeasonalTransformDialog dlg = new SeasonalTransformDialog(owner, map, transformedMap -> {
               this.mapListModel.addElement(transformedMap);
               this.mapList.setSelectedValue(transformedMap, true);
               this.loadMapToEditor(transformedMap);
               JOptionPane.showMessageDialog(this, "✅ Map mùa mới đã được tạo: " + transformedMap.name, "Transform Thành Công", 1);
            });
            SeasonTheme curTheme = (SeasonTheme)cmbSeasonSidebar.getSelectedItem();
            if (curTheme != null) {
               for (int i = 0; i < dlg.cmbSeason.getItemCount(); i++) {
                  if (dlg.cmbSeason.getItemAt(i).id.equals(curTheme.id)) {
                     dlg.cmbSeason.setSelectedIndex(i);
                     break;
                  }
               }
            }

            dlg.setVisible(true);
         }
      });
      seasonSection.add(btnMakeSeasonal);
      seasonSection.add(Box.createVerticalStrut(6));
      JLabel lblTip = StyledUI.createLabel(
         "<html><i>Tip: Gán Tags cho items, rồi nhấn nút để tự<br>chuyển map sang chủ đề mùa sự kiện!</i></html>", Theme.F_TINY, Theme.TEXT_DIM
      );
      lblTip.setAlignmentX(0.0F);
      seasonSection.add(lblTip);
      eastContent.add(seasonSection);
      JPanel outputSection = this.createSection("DATA EXPORT");
      String[] modes = new String[]{"INSERT SQL", "UPDATE SQL"};
      this.sqlMode = new JComboBox<>(modes);
      this.sqlMode.setFont(Theme.F_TINY);
      this.sqlMode.addActionListener(e -> this.updateOutputs());
      outputSection.add(this.sqlMode);
      outputSection.add(Box.createVerticalStrut(5));
      JTabbedPane outputTabs = new JTabbedPane();
      this.sqlOutput = this.createOutputArea();
      this.jsonOutput = this.createOutputArea();
      outputTabs.addTab("SQL", new JScrollPane(this.sqlOutput));
      outputTabs.addTab("JSON", new JScrollPane(this.jsonOutput));
      outputSection.add(outputTabs);
      eastContent.add(outputSection);
      return eastContent;
   }

   private JTextArea createOutputArea() {
      JTextArea a = new JTextArea();
      a.setBackground(Theme.BG_DARKER);
      a.setForeground(Theme.SUCCESS);
      a.setFont(Theme.F_MONO);
      a.setEditable(false);
      a.setLineWrap(true);
      a.setPreferredSize(new Dimension(0, 100));
      return a;
   }

   private JTextField addPropField(JPanel p, String label) {
      JLabel lbl = StyledUI.createLabel(label, Theme.F_TINY, Theme.TEXT_MUTED);
      p.add(lbl);
      JTextField f = StyledUI.createTextField("");
      f.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            this.update();
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            this.update();
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            this.update();
         }

         private void update() {
            if (!MainUI.this.isUpdatingFields) {
               SwingUtilities.invokeLater(() -> MainUI.this.applyProperties());
            }
         }
      });
      p.add(f);
      return f;
   }

   private JTextField addPropFieldNoAuto(JPanel p, String label) {
      JLabel lbl = StyledUI.createLabel(label, Theme.F_TINY, Theme.TEXT_MUTED);
      p.add(lbl);
      JTextField f = StyledUI.createTextField("");
      p.add(f);
      return f;
   }

   private JPanel createSection(String title) {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, 1));
      p.setOpaque(false);
      p.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER), new EmptyBorder(15, 15, 15, 15)));
      JLabel lbl = StyledUI.createLabel(title, Theme.F_TINY, Theme.ACCENT);
      p.add(lbl);
      p.add(Box.createVerticalStrut(12));
      return p;
   }

   private void loadMapToEditor(GameMap map) {
      this.isUpdatingFields = true;
      this.canvas.setMap(map);
      int tsId = map.getTileSetId();
      this.tilePalette.loadTileSet(tsId, "");
      this.waterPalette.loadTileSet(tsId, "");
      this.txtName.setText(map.name);
      this.txtWidth.setText(String.valueOf(map.width));
      this.txtHeight.setText(String.valueOf(map.height));
      this.txtTileSet.setText(String.valueOf(map.getTileSetId()));
      this.txtLevel.setText(String.valueOf(map.level));
      this.txtMaxZone.setText(String.valueOf(map.max_zone));
      this.txtMaxPlayer.setText(String.valueOf(map.max_player));
      this.updateObjectLists();
      this.updateOutputs();
      this.isUpdatingFields = false;
   }

   private void updateObjectLists() {
      GameMap map = this.canvas.getMap();
      if (map != null) {
         this.npcsModel.setRowCount(0);

         for (int i = 0; i < map.npcs.size(); i++) {
            GameMap.Npc n = map.npcs.get(i);
            this.npcsModel.addRow(new Object[]{i, n.iditem, n.name, n.namegt, n.chat, n.x, n.y, n.head, n.hair});
         }

         this.mobsModel.setRowCount(0);

         for (int i = 0; i < map.list_mob.size(); i++) {
            GameMap.Mob m = map.list_mob.get(i);
            this.mobsModel.addRow(new Object[]{i, m.templateId, m.x, m.y});
         }

         this.itemsModel.setRowCount(0);

         for (int i = 0; i < map.items.size(); i++) {
            ItemMapEntity it = map.items.get(i);
            short dx = 0;
            short dy = 0;
            int layer = -1;
            if (it.template != null) {
               dx = it.template.dx;
               dy = it.template.dy;
               layer = it.template.layer;
            }

            this.itemsModel.addRow(new Object[]{i, it.templateId, it.tileX, it.tileY, dx, dy, layer});
         }

         this.vgosModel.setRowCount(0);

         for (int i = 0; i < map.vgos.size(); i++) {
            GameMap.Vgo v = map.vgos.get(i);
            this.vgosModel.addRow(new Object[]{i, v.id_map_go, v.xold, v.yold, v.xnew, v.ynew});
         }
      }
   }

   private void updateOutputs() {
      if (this.updateTimer != null) {
         this.updateTimer.restart();
      }
   }

   private void performUpdateOutputs() {
      GameMap map = this.canvas.getMap();
      if (map != null && this.sqlMode != null) {
         int mode = this.sqlMode.getSelectedIndex();
         new Thread(() -> {
            try {
               String sqlStr = mode == 0 ? MapDataGenerator.generateSQL(map) : MapDataGenerator.generateUpdateSQL(map);
               String jsonStr = MapDataGenerator.generateJSON(map);
               SwingUtilities.invokeLater(() -> {
                  this.sqlOutput.setText(sqlStr);
                  this.jsonOutput.setText(jsonStr);
               });
            } catch (Exception var7) {
            }
         }).start();
      }
   }

   private void applyProperties() {
      GameMap map = this.canvas.getMap();
      if (map != null && !this.isUpdatingFields) {
         try {
            map.name = this.txtName.getText();
            int nw = Integer.parseInt(this.txtWidth.getText());
            int nh = Integer.parseInt(this.txtHeight.getText());
            if (nw > 127) {
               nw = 127;
            }

            if (nh > 127) {
               nh = 127;
            }

            int nts = Integer.parseInt(this.txtTileSet.getText());
            if (nw > 0 && nh > 0 && (nw != map.width || nh != map.height)) {
               map.resize(nw, nh);
            }

            if (nts != map.getTileSetId()) {
               map.setTileSetId(nts);
               this.canvas.loadTileImages();
               this.tilePalette.loadTileSet(nts, "");
               this.waterPalette.loadTileSet(nts, "");
            }

            map.level = (byte)Integer.parseInt(this.txtLevel.getText());
            map.max_zone = (byte)Integer.parseInt(this.txtMaxZone.getText());
            map.max_player = (byte)Integer.parseInt(this.txtMaxPlayer.getText());
            this.updateOutputs();
            this.canvas.revalidate();
            this.canvas.repaint();
         } catch (Exception var5) {
         }
      }
   }

   private void styleTable(JTable table) {
      table.setBackground(Theme.BG_CARD);
      table.setForeground(Theme.TEXT_MAIN);
      table.setGridColor(Theme.BORDER);
      table.setRowHeight(24);
      table.setSelectionBackground(Theme.ACCENT_SOFT);
      table.setSelectionForeground(Color.WHITE);
      table.setFont(Theme.F_TINY);
      table.setSelectionMode(0);
      table.setShowGrid(true);
      table.setIntercellSpacing(new Dimension(1, 1));
      table.getTableHeader().setBackground(Theme.BG_DARKER);
      table.getTableHeader().setForeground(Theme.TEXT_MUTED);
      table.getTableHeader().setFont(Theme.F_TINY);
      table.getTableHeader().setBorder(BorderFactory.createLineBorder(Theme.BORDER));
      table.getTableHeader().setReorderingAllowed(false);
   }

   private void saveCurrentMap() {
      this.showSaveExportDialog();
   }

   private void showSaveExportDialog() {
      GameMap current = this.canvas.getMap();
      List<GameMap> allMaps = new ArrayList<>();
      if (this.mapListModel != null) {
         for (int i = 0; i < this.mapListModel.size(); i++) {
            allMaps.add(this.mapListModel.get(i));
         }
      }

      Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
      MapSaveExportDialog dlg = new MapSaveExportDialog(owner, current, allMaps);
      dlg.setVisible(true);
   }

   private void loadTemplateProperties(short templateId) {
      ItemMapTemplate template = TemplateManager.gI().getItemTemplate(templateId);
      if (template != null) {
         this.isUpdatingFields = true;
         this.txtTemplateDx.setText(String.valueOf(template.dx));
         this.txtTemplateDy.setText(String.valueOf(template.dy));
         if (template.block != null && template.block.length > 0) {
            StringBuilder sb = new StringBuilder();

            for (int[] b : template.block) {
               if (sb.length() > 0) {
                  sb.append(";");
               }

               sb.append(b[0]).append(",").append(b[1]);
            }

            this.txtTemplateBlock.setText(sb.toString());
         } else {
            this.txtTemplateBlock.setText("");
         }

         String tagStr = SeasonKeywordStore.gI().getTagString(templateId);
         if (tagStr.isEmpty()) {
            String suggested = SeasonKeywordStore.gI().suggestTagsFromImageName(template.idImage);
            this.txtTemplateTags.setText(suggested);
            this.txtTemplateTags.setForeground(suggested.isEmpty() ? Theme.TEXT_DIM : new Color(6732650));
         } else {
            this.txtTemplateTags.setText(tagStr);
            this.txtTemplateTags.setForeground(Theme.TEXT_MAIN);
         }

         this.isUpdatingFields = false;
      }
   }

   private void saveTemplateProperties() {
      short templateId = this.canvas.getSelectedTemplateId();
      ItemMapTemplate template = TemplateManager.gI().getItemTemplate(templateId);
      if (template != null) {
         try {
            template.dx = Short.parseShort(this.txtTemplateDx.getText());
            template.dy = Short.parseShort(this.txtTemplateDy.getText());
            String blockStr = this.txtTemplateBlock.getText().trim();
            if (blockStr.isEmpty()) {
               template.block = new int[0][];
            } else {
               String[] points = blockStr.split(";");
               template.block = new int[points.length][2];

               for (int i = 0; i < points.length; i++) {
                  String[] coords = points[i].split(",");
                  template.block[i][0] = Integer.parseInt(coords[0].trim());
                  template.block[i][1] = Integer.parseInt(coords[1].trim());
               }
            }

            JOptionPane.showMessageDialog(this, "Template saved successfully!");
            this.canvas.loadItemImages();
            this.canvas.updateItemCache();
            this.canvas.repaint();
         } catch (Exception var7) {
            JOptionPane.showMessageDialog(this, "Error: " + var7.getMessage());
         }
      }
   }

   private void saveTemplateTags() {
      short templateId = this.canvas.getSelectedTemplateId();
      if (templateId < 0) {
         JOptionPane.showMessageDialog(this, "Chọn một item template trước!");
      } else {
         String tagStr = this.txtTemplateTags.getText().trim();
         SeasonKeywordStore.gI().setTagString(templateId, tagStr);
         boolean saved = SeasonKeywordStore.gI().saveToFile();
         if (saved) {
            this.txtTemplateTags.setForeground(Theme.TEXT_MAIN);
            this.itemPalette.repaint();
            JOptionPane.showMessageDialog(this, "✅ Đã lưu tags cho template #" + templateId + ": " + tagStr, "Tags Saved", 1);
         } else {
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi lưu tags!", "Error", 0);
         }
      }
   }

   private void exportClientData() {
      try {
         GameMap map = this.canvas.getMap();
         if (map == null) {
            JOptionPane.showMessageDialog(this, "No map loaded!");
            return;
         }

         String jsonStr = MapDataExporter.exportToSqlJson(map).toString(2);
         JDialog exportDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "EXPORT CLIENT DATA", true);
         exportDialog.setSize(600, 500);
         exportDialog.setLocationRelativeTo(this);
         JPanel contentPanel = new JPanel(new BorderLayout());
         contentPanel.setBackground(Theme.BG_DARKER);
         contentPanel.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 2));
         JPanel titleBar = new JPanel(new BorderLayout());
         titleBar.setBackground(Theme.BG_DARK);
         titleBar.setPreferredSize(new Dimension(0, 36));
         titleBar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
         JLabel titleLabel = StyledUI.createLabel("  EXPORT CLIENT DATA", Theme.F_TINY, Color.WHITE);
         titleBar.add(titleLabel, "West");
         JButton btnClose = new JButton("X") {
            {
               this.setFont(Theme.F_TINY);
               this.setForeground(Theme.TEXT_DIM);
               this.setContentAreaFilled(false);
               this.setBorderPainted(false);
               this.setFocusPainted(false);
               this.setCursor(new Cursor(12));
               this.addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseEntered(MouseEvent e) {
                     setForeground(Color.RED);
                  }

                  @Override
                  public void mouseExited(MouseEvent e) {
                     setForeground(Theme.TEXT_DIM);
                  }
               });
            }
         };
         btnClose.addActionListener(ex -> exportDialog.dispose());
         titleBar.add(btnClose, "East");
         contentPanel.add(titleBar, "North");
         JTextArea textArea = new JTextArea(jsonStr);
         textArea.setBackground(Theme.BG_DARKER);
         textArea.setForeground(Theme.SUCCESS);
         textArea.setFont(Theme.F_MONO);
         textArea.setEditable(false);
         JScrollPane scrollPane = new JScrollPane(textArea);
         scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
         contentPanel.add(scrollPane, "Center");
         JPanel footer = new JPanel(new FlowLayout(2, 10, 10));
         footer.setBackground(Theme.BG_DARK);
         footer.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
         JButton btnCopy = StyledUI.createButton("COPY TO CLIPBOARD", Theme.ACCENT);
         btnCopy.addActionListener(ex -> {
            textArea.selectAll();
            textArea.copy();
            JOptionPane.showMessageDialog(exportDialog, "Copied to clipboard!");
         });
         JButton btnCloseFooter = StyledUI.createButton("CLOSE", Theme.BG_CARD);
         btnCloseFooter.addActionListener(ex -> exportDialog.dispose());
         footer.add(btnCopy);
         footer.add(btnCloseFooter);
         contentPanel.add(footer, "South");
         exportDialog.setContentPane(contentPanel);
         exportDialog.setVisible(true);
      } catch (Exception var13) {
         JOptionPane.showMessageDialog(this, "Error: " + var13.getMessage());
      }
   }

   private void exportCurrentMapPng() {
      this.showSaveExportDialog();
   }

   private void exportCurrentMapBinary() {
      this.showSaveExportDialog();
   }

   private void exportBatchAll() {
      this.showSaveExportDialog();
   }

   private void previewMap() {
      try {
         GameMap map = this.canvas.getMap();
         if (map == null) {
            JOptionPane.showMessageDialog(this, "No map loaded!");
            return;
         }

         JDialog previewDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "MAP PREVIEW - " + map.name, false);
         previewDialog.setSize(800, 600);
         previewDialog.setLocationRelativeTo(this);
         JPanel previewPanel = new JPanel(new BorderLayout());
         previewPanel.setBackground(Theme.BG_DARKER);
         GameMapCanvas previewCanvas = new GameMapCanvas();
         previewCanvas.setMap(map);
         previewCanvas.setShowGrid(true);
         previewCanvas.setShowCollision(true);
         JScrollPane previewScroll = new JScrollPane(previewCanvas);
         StyledUI.styleScrollPane(previewScroll);
         previewPanel.add(previewScroll, "Center");
         JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 2));
         infoPanel.setBackground(Theme.BG_DARK);
         infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
         infoPanel.add(StyledUI.createLabel("Map ID: " + map.id, Theme.F_TINY, Theme.TEXT_MUTED));
         infoPanel.add(StyledUI.createLabel("Size: " + map.width + "x" + map.height, Theme.F_TINY, Theme.TEXT_MUTED));
         infoPanel.add(StyledUI.createLabel("Tileset: " + map.getTileSetId(), Theme.F_TINY, Theme.TEXT_MUTED));
         infoPanel.add(StyledUI.createLabel("Items: " + map.items.size(), Theme.F_TINY, Theme.TEXT_MUTED));
         infoPanel.add(StyledUI.createLabel("NPCs: " + map.npcs.size(), Theme.F_TINY, Theme.TEXT_MUTED));
         infoPanel.add(StyledUI.createLabel("Mobs: " + map.list_mob.size(), Theme.F_TINY, Theme.TEXT_MUTED));
         previewPanel.add(infoPanel, "South");
         previewDialog.setContentPane(previewPanel);
         previewDialog.setVisible(true);
      } catch (Exception var7) {
         JOptionPane.showMessageDialog(this, "Error: " + var7.getMessage());
      }
   }

   private void loadInitialData() {
      new Thread(() -> {
         List<GameMap> maps = MapDataLoader.loadAllMaps("");
         SwingUtilities.invokeLater(() -> {
            this.mapListModel.clear();

            for (GameMap m : maps) {
               this.mapListModel.addElement(m);
            }

            if (!maps.isEmpty()) {
               this.mapList.setSelectedIndex(0);
            } else {
               this.showNewMapDialog();
            }
         });
      }).start();
   }

   private void showNewMapDialog() {
      final JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "CREATE NEW MAP", true);
      dialog.setUndecorated(true);
      dialog.setSize(400, 340);
      dialog.setLocationRelativeTo(this);
      JPanel contentPanel = new JPanel(new BorderLayout());
      contentPanel.setBackground(Theme.BG_DARKER);
      contentPanel.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 2));
      JPanel titleBar = new JPanel(new BorderLayout());
      titleBar.setBackground(Theme.BG_DARK);
      titleBar.setPreferredSize(new Dimension(0, 36));
      titleBar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      JLabel titleLabel = StyledUI.createLabel("  CREATE NEW MAP", Theme.F_TINY, Color.WHITE);
      titleBar.add(titleLabel, "West");
      JButton btnClose = new JButton("X") {
         {
            this.setFont(Theme.F_TINY);
            this.setForeground(Theme.TEXT_DIM);
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setCursor(new Cursor(12));
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  setForeground(Color.RED);
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  setForeground(Theme.TEXT_DIM);
               }
            });
         }
      };
      btnClose.addActionListener(e -> dialog.dispose());
      titleBar.add(btnClose, "East");
      final Point[] dragPoint = new Point[]{null};
      titleBar.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            dragPoint[0] = e.getPoint();
         }
      });
      titleBar.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (dragPoint[0] != null) {
               Point p = e.getLocationOnScreen();
               dialog.setLocation(p.x - dragPoint[0].x, p.y - dragPoint[0].y);
            }
         }
      });
      contentPanel.add(titleBar, "North");
      JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
      form.setOpaque(false);
      form.setBorder(new EmptyBorder(20, 20, 20, 20));
      JTextField idF = StyledUI.createTextField("1");
      JTextField nameF = StyledUI.createTextField("New Map");
      JTextField wF = StyledUI.createTextField("30");
      JTextField hF = StyledUI.createTextField("30");
      JTextField tsF = StyledUI.createTextField("1");
      form.add(StyledUI.createLabel("Map ID:", Theme.F_TINY, Theme.TEXT_MUTED));
      form.add(idF);
      form.add(StyledUI.createLabel("Map Name:", Theme.F_TINY, Theme.TEXT_MUTED));
      form.add(nameF);
      form.add(StyledUI.createLabel("Width (Tiles):", Theme.F_TINY, Theme.TEXT_MUTED));
      form.add(wF);
      form.add(StyledUI.createLabel("Height (Tiles):", Theme.F_TINY, Theme.TEXT_MUTED));
      form.add(hF);
      form.add(StyledUI.createLabel("Tileset ID:", Theme.F_TINY, Theme.TEXT_MUTED));
      form.add(tsF);
      contentPanel.add(form, "Center");
      JPanel footer = new JPanel(new FlowLayout(2, 10, 10));
      footer.setBackground(Theme.BG_DARK);
      footer.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      JButton btnCancel = StyledUI.createButton("CANCEL", Theme.BG_CARD);
      btnCancel.addActionListener(e -> dialog.dispose());
      JButton btnCreate = StyledUI.createButton("CREATE", Theme.ACCENT);
      btnCreate.addActionListener(e -> {
         try {
            int id = Integer.parseInt(idF.getText().trim());
            String name = nameF.getText().trim();
            int w = Integer.parseInt(wF.getText().trim());
            int h = Integer.parseInt(hF.getText().trim());
            int ts = Integer.parseInt(tsF.getText().trim());
            if (w <= 0 || h <= 0 || w > 127 || h > 127) {
               JOptionPane.showMessageDialog(dialog, "Map width and height must be between 1 and 127.");
               return;
            }

            GameMap map = new GameMap(id, name, w, h, ts);
            this.mapListModel.addElement(map);
            this.mapList.setSelectedValue(map, true);
            this.loadMapToEditor(map);
            dialog.dispose();
         } catch (Exception var14x) {
            JOptionPane.showMessageDialog(dialog, "Error: " + var14x.getMessage(), "Validation Error", 0);
         }
      });
      footer.add(btnCancel);
      footer.add(btnCreate);
      contentPanel.add(footer, "South");
      dialog.setContentPane(contentPanel);
      dialog.setVisible(true);
   }

   private void showImportSqlDialog() {
      final JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "IMPORT MAP FROM SQL", true);
      dialog.setUndecorated(true);
      dialog.setSize(550, 400);
      dialog.setLocationRelativeTo(this);
      JPanel contentPanel = new JPanel(new BorderLayout());
      contentPanel.setBackground(Theme.BG_DARKER);
      contentPanel.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 2));
      JPanel titleBar = new JPanel(new BorderLayout());
      titleBar.setBackground(Theme.BG_DARK);
      titleBar.setPreferredSize(new Dimension(0, 36));
      titleBar.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER));
      JLabel titleLabel = StyledUI.createLabel("  IMPORT MAP FROM SQL", Theme.F_TINY, Color.WHITE);
      titleBar.add(titleLabel, "West");
      JButton btnClose = new JButton("X") {
         {
            this.setFont(Theme.F_TINY);
            this.setForeground(Theme.TEXT_DIM);
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setCursor(new Cursor(12));
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  setForeground(Color.RED);
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  setForeground(Theme.TEXT_DIM);
               }
            });
         }
      };
      btnClose.addActionListener(e -> dialog.dispose());
      titleBar.add(btnClose, "East");
      final Point[] dragPoint = new Point[]{null};
      titleBar.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            dragPoint[0] = e.getPoint();
         }
      });
      titleBar.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            if (dragPoint[0] != null) {
               Point p = e.getLocationOnScreen();
               dialog.setLocation(p.x - dragPoint[0].x, p.y - dragPoint[0].y);
            }
         }
      });
      contentPanel.add(titleBar, "North");
      JPanel centerPanel = new JPanel(new BorderLayout());
      centerPanel.setOpaque(false);
      centerPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
      JLabel hintLabel = StyledUI.createLabel("Paste SQL INSERT statement (contains table 'maps' structure) below:", Theme.F_TINY, Theme.TEXT_MUTED);
      centerPanel.add(hintLabel, "North");
      JTextArea sqlArea = new JTextArea();
      sqlArea.setBackground(Theme.BG_DARK);
      sqlArea.setForeground(Theme.SUCCESS);
      sqlArea.setCaretColor(Color.WHITE);
      sqlArea.setFont(Theme.F_MONO);
      sqlArea.setLineWrap(true);
      sqlArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
      JScrollPane scrollArea = new JScrollPane(sqlArea);
      StyledUI.styleScrollPane(scrollArea);
      centerPanel.add(scrollArea, "Center");
      contentPanel.add(centerPanel, "Center");
      JPanel footer = new JPanel(new FlowLayout(2, 10, 10));
      footer.setBackground(Theme.BG_DARK);
      footer.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
      JButton btnCancel = StyledUI.createButton("CANCEL", Theme.BG_CARD);
      btnCancel.addActionListener(e -> dialog.dispose());
      JButton btnImport = StyledUI.createButton("IMPORT MAP", Theme.ACCENT);
      btnImport.addActionListener(e -> {
         String sql = sqlArea.getText().trim();
         if (sql.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Please paste an SQL statement first.");
         } else {
            try {
               List<GameMap> parsedMaps = SQLMapLoader.parseInsertStatement(sql);
               if (parsedMaps == null || parsedMaps.isEmpty()) {
                  throw new Exception("Không thể phân tích dữ liệu map từ SQL.");
               }

               GameMap lastMap = null;

               for (GameMap map : parsedMaps) {
                  boolean exists = false;

                  for (int i = 0; i < this.mapListModel.size(); i++) {
                     if (this.mapListModel.get(i).id == map.id) {
                        this.mapListModel.set(i, map);
                        exists = true;
                        break;
                     }
                  }

                  if (!exists) {
                     this.mapListModel.addElement(map);
                  }

                  lastMap = map;
               }

               if (lastMap != null) {
                  this.mapList.setSelectedValue(lastMap, true);
                  this.loadMapToEditor(lastMap);
               }

               dialog.dispose();
               JOptionPane.showMessageDialog(this, "Đã import thành công " + parsedMaps.size() + " map!");
            } catch (Exception var11x) {
               JOptionPane.showMessageDialog(dialog, "Error parsing SQL: " + var11x.getMessage(), "Import Failed", 0);
               var11x.printStackTrace();
            }
         }
      });
      footer.add(btnCancel);
      footer.add(btnImport);
      contentPanel.add(footer, "South");
      dialog.setContentPane(contentPanel);
      dialog.setVisible(true);
   }

   private void clearCurrentMap() {
      GameMap map = this.canvas.getMap();
      if (map != null) {
         int r = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all tiles and collisions of this map?", "Clear Map Data", 0);
         if (r == 0) {
            Arrays.fill(map.mapPaint, 0);
            Arrays.fill(map.mapType, 0);
            map.npcs.clear();
            map.list_mob.clear();
            map.vgos.clear();
            map.list_boat.clear();
            this.canvas.repaint();
            this.updateObjectLists();
            this.updateOutputs();
         }
      }
   }

   private JToggleButton createToolButton(String text, GameMapCanvas.ToolMode mode, ButtonGroup group) {
      JToggleButton btn = new JToggleButton(text) {
         private boolean hovered = false;

         {
            this.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) {
                  hovered = true;
                  repaint();
               }

               @Override
               public void mouseExited(MouseEvent e) {
                  hovered = false;
                  repaint();
               }
            });
         }

         @Override
         protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = Theme.BG_DARK;
            if (this.isSelected()) {
               bg = Theme.BG_CARD;
            } else if (this.hovered) {
               bg = Theme.BG_HOVER;
            }

            g2.setColor(bg);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            if (this.isSelected()) {
               g2.setColor(Theme.ACCENT);
               g2.fillRect(0, this.getHeight() - 3, this.getWidth(), 3);
            } else if (this.hovered) {
               g2.setColor(Theme.BORDER_LIGHT);
               g2.fillRect(0, this.getHeight() - 3, this.getWidth(), 3);
            }

            g2.setColor(this.isSelected() ? Color.WHITE : (this.hovered ? Theme.TEXT_MAIN : Theme.TEXT_DIM));
            g2.setFont(Theme.F_TINY);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (this.getWidth() - fm.stringWidth(this.getText())) / 2;
            int ty = (this.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(this.getText(), tx, ty);
            g2.dispose();
         }
      };
      btn.setPreferredSize(new Dimension(88, 34));
      btn.setContentAreaFilled(false);
      btn.setBorderPainted(false);
      btn.setFocusPainted(false);
      btn.setCursor(new Cursor(12));
      btn.addActionListener(e -> this.canvas.setTool(mode));
      group.add(btn);
      return btn;
   }

   private static class MapListRenderer extends JPanel implements ListCellRenderer<GameMap> {
      private final JLabel idLabel;
      private final JLabel nameLabel;
      private static final Border NORMAL_BORDER = new EmptyBorder(8, 12, 8, 12);
      private static final Border SELECTED_BORDER = BorderFactory.createCompoundBorder(new MatteBorder(0, 3, 0, 0, Theme.ACCENT), new EmptyBorder(8, 9, 8, 12));

      public MapListRenderer() {
         super(new BorderLayout(6, 0));
         this.setOpaque(true);
         this.idLabel = StyledUI.createLabel("", Theme.F_MONO, Theme.TEXT_DIM);
         this.idLabel.setPreferredSize(new Dimension(36, 16));
         this.nameLabel = StyledUI.createLabel("", Theme.F_MAIN, Theme.TEXT_MAIN);
         this.add(this.idLabel, "West");
         this.add(this.nameLabel, "Center");
      }

      public Component getListCellRendererComponent(JList<? extends GameMap> list, GameMap map, int index, boolean isSelected, boolean cellHasFocus) {
         this.setBackground(isSelected ? Theme.ACCENT_SOFT : (index % 2 == 0 ? Theme.BG_DARK : Theme.BG_CARD));
         this.setBorder(isSelected ? SELECTED_BORDER : NORMAL_BORDER);
         if (map != null) {
            this.idLabel.setText(String.valueOf(map.id));
            this.idLabel.setForeground(isSelected ? Theme.ACCENT : Theme.TEXT_DIM);
            this.nameLabel.setText(map.name != null ? map.name : "Map " + map.id);
            this.nameLabel.setForeground(isSelected ? Color.WHITE : Theme.TEXT_MAIN);
         } else {
            this.idLabel.setText("");
            this.nameLabel.setText("");
         }

         return this;
      }
   }
}
