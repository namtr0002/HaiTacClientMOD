package com.deplor.haitactihontool.effect;

import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.config.ImageZoomHelper;
import com.deplor.haitactihontool.config.Lang;
import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.util.GifExportDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

public class MainUI extends JPanel {
   private static final Color BG = new Color(14, 14, 20);
   private static final Color TOOLBAR = new Color(20, 20, 28);
   private static final Color ACCENT = new Color(0, 110, 235);
   private static final Color ACCENT2 = new Color(0, 160, 80);
   private static final Color WARN = new Color(210, 110, 0);
   private static final Color FG_DIM = new Color(130, 130, 155);
   private static final Color FG = new Color(210, 210, 230);
   private static final Color BTN_BG = new Color(28, 28, 38);
   private final EffectSequencePanel sequencePanel = new EffectSequencePanel();
   private final EffectCanvas canvas = new EffectCanvas();
   private final EffectTimelineBar timelineBar = new EffectTimelineBar();
   private final EffectRightStudioPanel studioPanel = new EffectRightStudioPanel(false);
   private EffectBinaryParser parser;
   private final EffectWriter writer = new EffectWriter();
   private EffectModel currentModel;
   private String dataDir;
   private String imgDir;
   private JLabel infoLabel;
   private JToggleButton editToggle;
   private JCheckBox chkChar;
   private JCheckBox chkGrid;
   private JCheckBox chkAxis;
   private JCheckBox chkBounds;
   private JCheckBox chkLayers;
   private JComboBox<String> cbTypeMove;
   private JComboBox<String> cbBodySize;
   private Timer animTimer;
   private boolean isPlaying = false;
   private int animInterval = 120;

   public void refreshPaths() {
      String effectBase = AppConfig.getResolvedPath("path_effect", "Data/Effect/");
      effectBase = effectBase.replace('\\', '/');
      if (!effectBase.endsWith("/")) {
         effectBase = effectBase + "/";
      }

      File f = new File(effectBase);
      File dDir = new File(effectBase, "data");
      File iDir = new File(effectBase, "img");
      if (dDir.exists() && dDir.isDirectory()) {
         this.dataDir = dDir.getAbsolutePath().replace('\\', '/') + "/";
         this.imgDir = iDir.exists() && iDir.isDirectory() ? iDir.getAbsolutePath().replace('\\', '/') + "/" : effectBase + "img/";
      } else if (f.getName().equalsIgnoreCase("data")) {
         this.dataDir = effectBase;
         File parent = f.getParentFile();
         File parentImg = new File(parent, "img");
         this.imgDir = parentImg.exists() && parentImg.isDirectory()
            ? parentImg.getAbsolutePath().replace('\\', '/') + "/"
            : (parent != null ? parent.getAbsolutePath().replace('\\', '/') : "") + "/img/";
      } else {
         String appFallback = AppConfig.getAppDirectory().replace('\\', '/') + "/Data/Effect/";
         File appData = new File(appFallback, "data");
         if (appData.exists()) {
            this.dataDir = appFallback + "data/";
            this.imgDir = appFallback + "img/";
         } else {
            this.dataDir = effectBase + "data/";
            this.imgDir = effectBase + "img/";
         }
      }

      this.parser = new EffectBinaryParser(this.dataDir, this.imgDir);
   }

   public MainUI() {
      this.refreshPaths();
      this.setLayout(new BorderLayout());
      this.setBackground(BG);
      this.add(this.buildToolbar(), "North");
      this.add(this.buildWorkspace(), "Center");
      this.wireListeners();
      this.buildAnimTimer();
      this.setupHotkeys();
      int initialId = 1;
      File dFolder = new File(this.dataDir);
      if (dFolder.exists() && dFolder.isDirectory()) {
         File[] files = dFolder.listFiles();
         if (files != null && files.length > 0) {
            boolean has1 = false;
            int firstId = -1;

            for (File file : files) {
               try {
                  int fid = Integer.parseInt(file.getName());
                  if (fid == 1) {
                     has1 = true;
                  }

                  if (firstId == -1 || fid < firstId) {
                     firstId = fid;
                  }
               } catch (Exception var11) {
               }
            }

            if (has1) {
               initialId = 1;
            } else if (firstId != -1) {
               initialId = firstId;
            }
         }
      }

      this.loadEffect(initialId);
   }

   private JPanel buildToolbar() {
      JPanel bar = new JPanel(new FlowLayout(0, 5, 5));
      bar.setBackground(TOOLBAR);
      bar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(38, 38, 52)));
      JButton btnBrowse = actionBtn("Duyệt File", ACCENT);
      btnBrowse.setToolTipText("Duyệt tìm và nạp Effect từ thư mục dữ liệu");
      btnBrowse.addActionListener(e -> this.openBrowser());
      bar.add(btnBrowse);
      JButton btnNew = actionBtn("Tạo Mới", new Color(38, 38, 52));
      btnNew.setToolTipText("Tạo một Effect mới hoàn toàn");
      btnNew.addActionListener(e -> this.promptNewEffect());
      bar.add(btnNew);
      JButton btnCutter = actionBtn("Cắt Sprite", new Color(160, 80, 0));
      btnCutter.setToolTipText("Mở công cụ cắt Sprite từ ảnh Atlas");
      btnCutter.addActionListener(e -> this.openSpriteCutter());
      bar.add(btnCutter);
      JButton btnImport = actionBtn("Import Atlas", new Color(110, 60, 170));
      btnImport.setToolTipText("Nhập ảnh PNG mới (Auto-pack hoặc Spritesheet)");
      btnImport.addActionListener(e -> this.importImage());
      bar.add(btnImport);
      JButton btnSave = actionBtn("Lưu Dữ Liệu", ACCENT2);
      btnSave.setToolTipText("Lưu Effect (Tùy chọn Binary, Image, Animated GIF, SQL, JSON)");
      btnSave.addActionListener(e -> this.saveEffect());
      bar.add(btnSave);
      bar.add(vsep());
      this.chkChar = chk("Nhân vật", true);
      this.chkGrid = chk("Lưới", true);
      this.chkAxis = chk("Trục", true);
      this.chkBounds = chk("Viền", false);
      this.chkLayers = chk("Lớp", true);

      for (JCheckBox c : new JCheckBox[]{this.chkChar, this.chkGrid, this.chkAxis, this.chkBounds, this.chkLayers}) {
         bar.add(c);
      }

      bar.add(vsep());
      bar.add(lbl("Di chuyển:"));
      this.cbTypeMove = new JComboBox<>(new String[]{"0: Chuẩn", "1: Khóa", "2: Biến hình"});
      styleCombo(this.cbTypeMove);
      this.cbTypeMove.setPreferredSize(new Dimension(85, 24));
      this.cbTypeMove.addActionListener(e -> {
         int idx = this.cbTypeMove.getSelectedIndex();
         if (idx >= 0) {
            this.canvas.setTypeMove(idx);
         }
      });
      bar.add(this.cbTypeMove);
      bar.add(lbl("Thân:"));
      this.cbBodySize = new JComboBox<>(new String[]{"48: Chuẩn", "62: Lớn"});
      styleCombo(this.cbBodySize);
      this.cbBodySize.setPreferredSize(new Dimension(80, 24));
      this.cbBodySize.addActionListener(e -> {
         int idx = this.cbBodySize.getSelectedIndex();
         if (idx >= 0) {
            this.canvas.setHOne(idx == 0 ? 48 : 62);
         }
      });
      bar.add(this.cbBodySize);
      bar.add(vsep());
      this.editToggle = new JToggleButton("Chế Độ Sửa");
      this.editToggle.setFont(new Font("Segoe UI", 1, 10));
      this.editToggle.setBackground(BTN_BG);
      this.editToggle.setForeground(Color.WHITE);
      this.editToggle.setFocusPainted(false);
      this.editToggle.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)), new EmptyBorder(3, 8, 3, 8)));
      this.editToggle.addItemListener(e -> {
         boolean on = this.editToggle.isSelected();
         this.canvas.setEditMode(on);
         this.editToggle.setBackground(on ? WARN : BTN_BG);
      });
      bar.add(this.editToggle);
      JButton btnReset = actionBtn("Reset View", BTN_BG);
      btnReset.addActionListener(e -> this.canvas.resetView());
      bar.add(btnReset);
      bar.add(vsep());
      this.infoLabel = new JLabel("—");
      this.infoLabel.setFont(new Font("Segoe UI", 0, 10));
      this.infoLabel.setForeground(FG_DIM);
      bar.add(this.infoLabel);
      return bar;
   }

   private JComponent buildWorkspace() {
      JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
      centerPanel.setBackground(BG);
      centerPanel.add(this.wrapCanvas(), "Center");
      centerPanel.add(this.timelineBar, "South");
      JSplitPane centerRightSplit = new JSplitPane(1, centerPanel, this.studioPanel);
      centerRightSplit.setDividerLocation(0.68);
      centerRightSplit.setResizeWeight(0.68);
      centerRightSplit.setBorder(null);
      centerRightSplit.setDividerSize(4);
      JSplitPane mainSplit = new JSplitPane(1, this.sequencePanel, centerRightSplit);
      mainSplit.setDividerLocation(230);
      mainSplit.setResizeWeight(0.18);
      mainSplit.setBorder(null);
      mainSplit.setDividerSize(4);
      return mainSplit;
   }

   private JScrollPane wrapCanvas() {
      JScrollPane sp = new JScrollPane(this.canvas);
      sp.setBorder(null);
      sp.setBackground(new Color(18, 18, 22));
      sp.getViewport().setBackground(new Color(18, 18, 22));
      sp.getHorizontalScrollBar().setUnitIncrement(20);
      sp.getVerticalScrollBar().setUnitIncrement(20);
      return sp;
   }

   private void wireListeners() {
      this.chkChar.addActionListener(e -> this.canvas.setShowChar(this.chkChar.isSelected()));
      this.chkGrid.addActionListener(e -> this.canvas.setShowGrid(this.chkGrid.isSelected()));
      this.chkAxis.addActionListener(e -> this.canvas.setShowAxis(this.chkAxis.isSelected()));
      this.chkBounds.addActionListener(e -> this.canvas.setShowPartBounds(this.chkBounds.isSelected()));
      this.chkLayers.addActionListener(e -> this.canvas.setUseLayers(this.chkLayers.isSelected()));
      this.sequencePanel.setListener(new EffectSequencePanel.OnSequenceActionListener() {
         @Override
         public void onSeqPosSelected(int seqPos) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.canvas.setSeqPos(seqPos);
               MainUI.this.timelineBar.selectPos(seqPos);
               int curF = MainUI.this.currentModel.resolveSeqFrame(seqPos);
               MainUI.this.studioPanel.selectFrame(curF);
               MainUI.this.canvas.setSelectedPart(-1);
               MainUI.this.canvas.repaint();
            }
         }

         @Override
         public void onAddSeqStep(int insertPos, int frameIdx) {
            if (MainUI.this.currentModel != null) {
               int newPos = MainUI.this.currentModel.addSeqStep(insertPos, frameIdx);
               MainUI.this.refreshAllViews(-1, newPos);
            }
         }

         @Override
         public void onDeleteSeqStep(int pos) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.deleteSeqStep(pos);
               int nextPos = Math.max(0, pos - 1);
               MainUI.this.refreshAllViews(-1, nextPos);
            }
         }

         @Override
         public void onChangeSeqTarget(int pos, int newFrameIdx) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.setSeqStepTarget(pos, newFrameIdx);
               MainUI.this.refreshAllViews(newFrameIdx, pos);
               MainUI.this.infoLabel.setText("Đã đổi đích Bước #" + (pos + 1) + " trỏ vào Frame #" + newFrameIdx);
            }
         }

         @Override
         public void onReorderSeq(int fromPos, int toPos) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.reorderSeqStep(fromPos, toPos);
               MainUI.this.refreshAllViews(-1, toPos);
            }
         }

         @Override
         public void onApplySequence(int[] newSequence) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.setCustomSequence(newSequence);
               MainUI.this.currentModel.sanitizeSequence();
               MainUI.this.refreshAllViews(-1, 0);
               MainUI.this.infoLabel.setText("Đã cập nhật chuỗi Sequence (" + MainUI.this.currentModel.getSeqLen() + " bước)");
            }
         }

         @Override
         public void onAutoSequence() {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.autoGenerateSequenceLinear();
               MainUI.this.currentModel.sanitizeSequence();
               MainUI.this.refreshAllViews(-1, 0);
               MainUI.this.infoLabel.setText("Đã tự động tạo Sequence 1:1 theo " + MainUI.this.currentModel.getFrameCount() + " Frames");
            }
         }

         @Override
         public void onOpenSequenceEditor() {
            MainUI.this.timelineBar.promptSequenceEditor();
         }

         @Override
         public void onExportGif() {
            MainUI.this.openGifExportDialog(false);
         }
      });
      this.timelineBar.setListener(new EffectTimelineBar.OnTimelineActionListener() {
         @Override
         public void onSeqPosSelected(int seqPos) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.canvas.setSeqPos(seqPos);
               MainUI.this.sequencePanel.selectPos(seqPos);
               int curF = MainUI.this.currentModel.resolveSeqFrame(seqPos);
               MainUI.this.studioPanel.selectFrame(curF);
               MainUI.this.canvas.setSelectedPart(-1);
               MainUI.this.canvas.repaint();
            }
         }

         @Override
         public void onAddSeqStep(int insertPos, int frameIdx) {
            if (MainUI.this.currentModel != null) {
               int newPos = MainUI.this.currentModel.addSeqStep(insertPos, frameIdx);
               MainUI.this.refreshAllViews(-1, newPos);
            }
         }

         @Override
         public void onDeleteSeqStep(int pos) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.deleteSeqStep(pos);
               int nextPos = Math.max(0, pos - 1);
               MainUI.this.refreshAllViews(-1, nextPos);
            }
         }

         @Override
         public void onChangeSeqTarget(int pos, int newFrameIdx) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.setSeqStepTarget(pos, newFrameIdx);
               MainUI.this.refreshAllViews(newFrameIdx, pos);
               MainUI.this.infoLabel.setText("Đã đổi đích Sequence #" + (pos + 1) + " trỏ vào Frame #" + newFrameIdx);
            }
         }

         @Override
         public void onReorderSeq(int fromPos, int toPos) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.reorderSeqStep(fromPos, toPos);
               MainUI.this.refreshAllViews(-1, toPos);
            }
         }

         @Override
         public void onApplySequence(int[] newSequence) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.setCustomSequence(newSequence);
               MainUI.this.currentModel.sanitizeSequence();
               MainUI.this.refreshAllViews(-1, 0);
            }
         }

         @Override
         public void onTogglePlay() {
            MainUI.this.togglePlay();
         }

         @Override
         public void onStepSeq(int delta) {
            MainUI.this.stepSeq(delta);
         }

         @Override
         public void onJumpSeq(int pos) {
            MainUI.this.jumpSeq(pos);
         }

         @Override
         public void onSpeedChanged(int intervalMs) {
            MainUI.this.animInterval = intervalMs;
            if (MainUI.this.animTimer != null) {
               MainUI.this.animTimer.setDelay(MainUI.this.animInterval);
            }
         }
      });
      this.studioPanel.setListener(new EffectRightStudioPanel.OnStudioActionListener() {
         @Override
         public void onFrameSelected(int frameIdx) {
            if (MainUI.this.currentModel != null && MainUI.this.currentModel.frames != null && frameIdx < MainUI.this.currentModel.frames.length) {
               MainUI.this.canvas.setDirectFrameIdx(frameIdx);
               MainUI.this.canvas.setSelectedPart(-1);
               MainUI.this.canvas.repaint();
            }
         }

         @Override
         public void onAddFrame() {
            if (MainUI.this.currentModel != null) {
               int newIdx = MainUI.this.currentModel.addFrame(-1, EffFrame.createEmpty());
               MainUI.this.currentModel.sanitizeSequence();
               MainUI.this.refreshAllViews(newIdx, -1);
               MainUI.this.infoLabel.setText("Đã tạo mới Frame #" + newIdx);
            }
         }

         @Override
         public void onDuplicateFrame(int frameIdx) {
            if (MainUI.this.currentModel != null && MainUI.this.currentModel.frames != null && frameIdx < MainUI.this.currentModel.frames.length) {
               int newIdx = MainUI.this.currentModel.duplicateFrame(frameIdx);
               MainUI.this.currentModel.sanitizeSequence();
               MainUI.this.refreshAllViews(newIdx, -1);
               MainUI.this.infoLabel.setText("Đã nhân bản Frame #" + frameIdx + " ➔ Frame #" + newIdx);
            }
         }

         @Override
         public void onDeleteFrame(int frameIdx) {
            if (MainUI.this.currentModel != null && MainUI.this.currentModel.frames != null) {
               int ans = JOptionPane.showConfirmDialog(MainUI.this, "Bạn có chắc muốn XÓA Frame " + frameIdx + " không?", "Xác nhận xóa Frame", 0, 2);
               if (ans == 0) {
                  boolean ok = MainUI.this.currentModel.deleteFrame(frameIdx);
                  if (ok) {
                     MainUI.this.currentModel.sanitizeSequence();
                     int selF = Math.max(0, Math.min(frameIdx, MainUI.this.currentModel.getFrameCount() - 1));
                     MainUI.this.refreshAllViews(selF, -1);
                     MainUI.this.infoLabel.setText("Đã xóa Frame #" + frameIdx);
                  }
               }
            }
         }

         @Override
         public void onInsertFrameToSeq(int frameIdx) {
            if (MainUI.this.currentModel != null) {
               int newStep = MainUI.this.currentModel.addSeqStep(-1, frameIdx);
               MainUI.this.refreshAllViews(-1, newStep);
               MainUI.this.infoLabel.setText("Đã chèn Frame #" + frameIdx + " vào bước Sequence #" + (newStep + 1));
            }
         }

         @Override
         public void onReorderFrame(int fromIdx, int toIdx) {
            if (MainUI.this.currentModel != null) {
               MainUI.this.currentModel.reorderFrames(fromIdx, toIdx);
               MainUI.this.currentModel.sanitizeSequence();
               MainUI.this.refreshAllViews(toIdx, -1);
            }
         }

         @Override
         public void onPartSelected(int partIdx) {
            MainUI.this.canvas.setSelectedPart(partIdx);
            MainUI.this.canvas.repaint();
         }

         @Override
         public void onAddPart(int spriteId) {
            EffFrame frame = MainUI.this.canvas.getCurrentFrame();
            if (frame != null) {
               EffPartFrame newPart = new EffPartFrame(0, 0, spriteId, 0, 1);
               frame.allParts.add(newPart);
               frame.rebuildLayers();
               int newPartIdx = frame.allParts.size() - 1;
               MainUI.this.studioPanel.updatePartsTable();
               MainUI.this.studioPanel.selectPartRow(newPartIdx);
               MainUI.this.canvas.setSelectedPart(newPartIdx);
               MainUI.this.refreshAllViews(MainUI.this.canvas.getCurrentFrameIndex(), -1);
               MainUI.this.infoLabel.setText("Đã thêm Part mới với Sprite #" + spriteId);
            }
         }

         @Override
         public void onDuplicatePart(int partIdx) {
            EffFrame frame = MainUI.this.canvas.getCurrentFrame();
            if (frame != null && partIdx >= 0 && partIdx < frame.allParts.size()) {
               EffPartFrame orig = frame.allParts.get(partIdx);
               EffPartFrame dup = new EffPartFrame(orig.dx + 4, orig.dy + 4, orig.idSmallImg, orig.flip, orig.onTop, orig.rotate);
               frame.allParts.add(dup);
               frame.rebuildLayers();
               int newPartIdx = frame.allParts.size() - 1;
               MainUI.this.studioPanel.updatePartsTable();
               MainUI.this.studioPanel.selectPartRow(newPartIdx);
               MainUI.this.canvas.setSelectedPart(newPartIdx);
               MainUI.this.refreshAllViews(MainUI.this.canvas.getCurrentFrameIndex(), -1);
               MainUI.this.infoLabel.setText("Đã nhân bản Part #" + partIdx);
            }
         }

         @Override
         public void onDeletePart(int partIdx) {
            EffFrame frame = MainUI.this.canvas.getCurrentFrame();
            if (frame != null && partIdx >= 0 && partIdx < frame.allParts.size()) {
               frame.allParts.remove(partIdx);
               frame.rebuildLayers();
               MainUI.this.studioPanel.updatePartsTable();
               MainUI.this.canvas.setSelectedPart(-1);
               MainUI.this.refreshAllViews(MainUI.this.canvas.getCurrentFrameIndex(), -1);
               MainUI.this.infoLabel.setText("Đã xóa Part #" + partIdx);
            }
         }

         @Override
         public void onMovePartLayer(int partIdx, int delta) {
            EffFrame frame = MainUI.this.canvas.getCurrentFrame();
            if (frame != null && partIdx >= 0 && partIdx < frame.allParts.size()) {
               int target = partIdx + delta;
               if (target >= 0 && target < frame.allParts.size()) {
                  Collections.swap(frame.allParts, partIdx, target);
                  frame.rebuildLayers();
                  MainUI.this.studioPanel.updatePartsTable();
                  MainUI.this.studioPanel.selectPartRow(target);
                  MainUI.this.canvas.setSelectedPart(target);
                  MainUI.this.refreshAllViews(MainUI.this.canvas.getCurrentFrameIndex(), -1);
               }
            }
         }

         @Override
         public void onPartPropsChanged(int partIdx, EffPartFrame part) {
            MainUI.this.canvas.repaint();
            MainUI.this.canvas.forceThumbRebuild();
         }

         @Override
         public void onSpriteSelected(int spriteId, SmallImageDef def) {
            MainUI.this.infoLabel.setText("Sprite #" + spriteId + " (" + def.w + "×" + def.h + ")");
         }

         @Override
         public void onOpenSpriteCutter() {
            MainUI.this.openSpriteCutter();
         }

         @Override
         public void onImportAtlas() {
            MainUI.this.importImage();
         }

         @Override
         public void onAutoPropsChanged() {
         }
      });
      this.canvas.setClickListener((idx, part) -> this.studioPanel.selectPartRow(idx));
      this.canvas.setMoveListener((idx, part) -> {
         this.studioPanel.updatePartsTable();
         this.studioPanel.selectPartRow(idx);
      });
   }

   private void refreshAllViews(int selectFrameIdx, int selectSeqPos) {
      this.canvas.forceThumbRebuild();
      this.sequencePanel.setModel(this.currentModel, this.canvas.getThumbs());
      this.timelineBar.setModel(this.currentModel, this.canvas.getThumbs());
      this.studioPanel.setModel(this.currentModel, this.canvas.getThumbs());
      if (selectFrameIdx >= 0) {
         this.studioPanel.selectFrame(selectFrameIdx);
         this.canvas.setDirectFrameIdx(selectFrameIdx);
      } else if (selectSeqPos >= 0) {
         this.sequencePanel.selectPos(selectSeqPos);
         this.timelineBar.selectPos(selectSeqPos);
         this.canvas.setSeqPos(selectSeqPos);
         int curF = this.currentModel != null ? this.currentModel.resolveSeqFrame(selectSeqPos) : 0;
         this.studioPanel.selectFrame(curF);
      }

      this.canvas.repaint();
      this.updateInfoLabel();
   }

   private void buildAnimTimer() {
      this.animTimer = new Timer(this.animInterval, e -> {
         if (this.isPlaying && this.currentModel != null && this.currentModel.getSeqLen() != 0) {
            int len = this.currentModel.getSeqLen();
            int next = (this.canvas.getSeqPos() + 1) % len;
            this.canvas.setSeqPos(next);
            this.timelineBar.selectPos(next);
            this.sequencePanel.selectPos(next);
            int curF = this.currentModel.resolveSeqFrame(next);
            this.studioPanel.selectFrame(curF);
         }
      });
   }

   private void togglePlay() {
      if (this.isPlaying) {
         this.stopPlay();
      } else {
         this.startPlay();
      }
   }

   private void startPlay() {
      this.isPlaying = true;
      this.timelineBar.setPlaying(true);
      this.animTimer.start();
   }

   private void stopPlay() {
      this.isPlaying = false;
      this.timelineBar.setPlaying(false);
      this.animTimer.stop();
   }

   private void stepSeq(int d) {
      if (this.currentModel != null && this.currentModel.getSeqLen() != 0) {
         int len = this.currentModel.getSeqLen();
         int next = ((this.canvas.getSeqPos() + d) % len + len) % len;
         this.canvas.setSeqPos(next);
         this.timelineBar.selectPos(next);
         this.sequencePanel.selectPos(next);
         int curF = this.currentModel.resolveSeqFrame(next);
         this.studioPanel.selectFrame(curF);
      }
   }

   private void jumpSeq(int pos) {
      if (this.currentModel != null && this.currentModel.getSeqLen() != 0) {
         int len = this.currentModel.getSeqLen();
         pos = Math.max(0, Math.min(pos, len - 1));
         this.canvas.setSeqPos(pos);
         this.timelineBar.selectPos(pos);
         this.sequencePanel.selectPos(pos);
         int curF = this.currentModel.resolveSeqFrame(pos);
         this.studioPanel.selectFrame(curF);
      }
   }

   public void loadEffect(final int id) {
      this.refreshPaths();
      this.infoLabel.setText("Đang tải Effect #" + id + "...");
      (new SwingWorker<EffectModel, Void>() {
         protected EffectModel doInBackground() throws Exception {
            return MainUI.this.parser.parse(id);
         }

         @Override
         protected void done() {
            try {
               MainUI.this.currentModel = this.get();
               MainUI.this.stopPlay();
               MainUI.this.applyLoadedModel();
               MainUI.this.infoLabel.setText("Đã nạp Effect ID #" + id);
               if (MainUI.this.currentModel.smallImages == null || MainUI.this.currentModel.smallImages.length == 0) {
                  MainUI.this.openSpriteCutter();
               }
            } catch (Exception var2) {
               MainUI.this.createNewEffectDefault(id);
               MainUI.this.infoLabel.setText("Tạo mới Effect ID #" + id + " (Chưa có dữ liệu)");
            }
         }
      }).execute();
   }

   private void createNewEffectDefault(int id) {
      this.currentModel = EffectWriter.createBlank(id);
      this.currentModel.frames = new EffFrame[]{EffFrame.createEmpty()};
      this.currentModel.sequence = new int[]{0};
      File imgFile = new File(this.imgDir, id + ".png");
      if (imgFile.exists()) {
         try {
            BufferedImage img = ImageIO.read(imgFile);
            if (img != null) {
               int zoom = ImageZoomHelper.detectImageZoom(img, null);
               if (zoom != 4) {
                  img = ImageZoomHelper.scaleToZoom4(img, zoom);
               }

               this.currentModel.atlasImage = img;
            }
         } catch (Exception var5) {
         }
      }

      this.applyLoadedModel();
   }

   private void promptNewEffect() {
      String input = JOptionPane.showInputDialog(this, "Nhập ID Effect mới cần tạo:", "1");
      if (input != null && !input.trim().isEmpty()) {
         try {
            int id = Integer.parseInt(input.trim());
            this.createNewEffectDefault(id);
         } catch (Exception var3) {
            JOptionPane.showMessageDialog(this, "ID phải là số nguyên dương!", "Lỗi", 0);
         }
      }
   }

   private void applyLoadedModel() {
      this.canvas.setModel(this.currentModel);
      this.sequencePanel.setModel(this.currentModel, this.canvas.getThumbs());
      this.timelineBar.setModel(this.currentModel, this.canvas.getThumbs());
      this.studioPanel.setModel(this.currentModel, this.canvas.getThumbs());
      this.canvas.setSeqPos(0);
      this.sequencePanel.selectPos(0);
      this.timelineBar.selectPos(0);
      this.studioPanel.selectFrame(0);
      if (this.cbTypeMove != null) {
         this.cbTypeMove.setSelectedIndex(0);
      }

      if (this.cbBodySize != null) {
         this.cbBodySize.setSelectedIndex(0);
      }

      this.updateInfoLabel();
   }

   private void updateInfoLabel() {
      if (this.currentModel == null) {
         this.infoLabel.setText("—");
      } else {
         this.infoLabel
            .setText(
               String.format(
                  "ID:%d | %d Frames | %d Seq | %d Sprites | Atlas: %s",
                  this.currentModel.id,
                  this.currentModel.getFrameCount(),
                  this.currentModel.getSeqLen(),
                  this.currentModel.smallImages != null ? this.currentModel.smallImages.length : 0,
                  this.currentModel.atlasImage != null ? this.currentModel.atlasImage.getWidth() + "×" + this.currentModel.atlasImage.getHeight() : "None"
               )
            );
      }
   }

   private void openBrowser() {
      this.refreshPaths();
      Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
      EffectBrowserDialog dlg = new EffectBrowserDialog(owner, this, this.dataDir, this.imgDir);
      dlg.setVisible(true);
      Integer id = dlg.getSelectedId();
      if (id != null) {
         this.loadEffect(id);
      }
   }

   private void openSpriteCutter() {
      if (this.currentModel == null) {
         JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc tạo hiệu ứng trước!");
      } else {
         Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
         SpriteCutterDialog dlg = new SpriteCutterDialog(owner, this.currentModel, this);
         dlg.setVisible(true);
         if (dlg.isConfirmed()) {
            this.refreshAllViews(0, 0);
            this.infoLabel.setText("Đã cập nhật danh sách Sprite của hiệu ứng!");
         }
      }
   }

   private void saveEffect() {
      if (this.currentModel == null) {
         JOptionPane.showMessageDialog(this, Lang.get("effect_save_err_no_data"));
      } else {
         this.refreshPaths();
         Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
         EffectSaveDialog dlg = new EffectSaveDialog(owner, this.currentModel, this.dataDir);
         dlg.setVisible(true);
         if (dlg.isSaved()) {
            this.infoLabel.setText("Đã lưu Effect #" + this.currentModel.id + " ➔ " + dlg.getOutputPath());
         }
      }
   }

   private void openGifExportDialog(boolean allMode) {
      if (this.currentModel == null) {
         JOptionPane.showMessageDialog(this, "Vui lòng chọn hiệu ứng trước khi xuất GIF!");
      } else {
         this.refreshPaths();
         Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
         GifExportDialog dlg = new GifExportDialog(owner, GifExportDialog.TargetType.EFFECT, this.currentModel, this.dataDir);
         dlg.setPaths(this.dataDir, this.imgDir);
         dlg.setVisible(true);
      }
   }

   private void openExportWorkshop() {
      if (this.currentModel != null) {
         this.refreshPaths();
         Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
         new MainUI.ExportWorkshopDialog(owner, this.currentModel, this.dataDir).setVisible(true);
      }
   }

   private void importImage() {
      Frame owner = (Frame)SwingUtilities.getWindowAncestor(this);
      JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
      fc.setDialogTitle("Chọn hình ảnh PNG Atlas để nhập");
      fc.setFileFilter(new FileNameExtensionFilter("Ảnh PNG (*.png)", "png"));
      if (fc.showOpenDialog(owner) == 0) {
         try {
            BufferedImage img = ImageIO.read(fc.getSelectedFile());
            if (img != null) {
               if (this.currentModel == null) {
                  this.currentModel = EffectWriter.createBlank(1);
               }

               this.currentModel.atlasImage = img;
               this.openSpriteCutter();
            }
         } catch (Exception var4) {
            JOptionPane.showMessageDialog(owner, "Không thể đọc file ảnh: " + var4.getMessage());
         }
      }
   }

   private void setupHotkeys() {
      InputMap im = this.getInputMap(2);
      ActionMap am = this.getActionMap();
      im.put(KeyStroke.getKeyStroke(32, 0), "togglePlay");
      am.put("togglePlay", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Component f = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (!(f instanceof JTextComponent)) {
               MainUI.this.togglePlay();
            }
         }
      });
      im.put(KeyStroke.getKeyStroke(44, 0), "stepBack");
      am.put("stepBack", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Component f = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (!(f instanceof JTextComponent)) {
               MainUI.this.stepSeq(-1);
            }
         }
      });
      im.put(KeyStroke.getKeyStroke(46, 0), "stepForward");
      am.put("stepForward", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Component f = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (!(f instanceof JTextComponent)) {
               MainUI.this.stepSeq(1);
            }
         }
      });
   }

   private static JLabel lbl(String t) {
      JLabel l = new JLabel(t);
      l.setFont(new Font("Segoe UI", 0, 10));
      l.setForeground(FG_DIM);
      return l;
   }

   private static JButton actionBtn(String t, final Color bg) {
      final JButton b = new JButton(t);
      b.setFont(new Font("Segoe UI", 1, 10));
      b.setForeground(Color.WHITE);
      b.setBackground(bg);
      b.setFocusPainted(false);
      b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(55, 55, 75)), new EmptyBorder(4, 10, 4, 10)));
      b.setCursor(Cursor.getPredefinedCursor(12));
      b.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent e) {
            b.setBackground(bg.brighter());
         }

         @Override
         public void mouseExited(MouseEvent e) {
            b.setBackground(bg);
         }
      });
      return b;
   }

   private static JCheckBox chk(String t, boolean sel) {
      JCheckBox c = new JCheckBox(t, sel);
      c.setOpaque(false);
      c.setForeground(FG_DIM);
      c.setFont(new Font("Segoe UI", 0, 10));
      c.setFocusPainted(false);
      return c;
   }

   private static JSeparator vsep() {
      JSeparator s = new JSeparator(1);
      s.setPreferredSize(new Dimension(1, 18));
      s.setForeground(new Color(45, 45, 60));
      return s;
   }

   private static void styleCombo(JComboBox<?> cb) {
      cb.setBackground(new Color(28, 28, 36));
      cb.setForeground(new Color(210, 210, 225));
      cb.setFont(new Font("Segoe UI", 0, 10));
      cb.setBorder(BorderFactory.createLineBorder(new Color(55, 55, 70)));
   }

   private class ExportWorkshopDialog extends JDialog {
      ExportWorkshopDialog(Frame owner, EffectModel model, String initialDir) {
         super(owner, "Effect Export Workshop", true);
         this.setSize(480, 360);
         this.setLocationRelativeTo(owner);
         this.getContentPane().setBackground(new Color(20, 20, 28));
         this.setLayout(new BorderLayout(10, 10));
         JPanel header = new JPanel(new BorderLayout());
         header.setBackground(new Color(28, 28, 38));
         header.setBorder(new EmptyBorder(10, 12, 10, 12));
         JLabel title = new JLabel("XUẤT BẢN HIỆU ỨNG #" + model.id);
         title.setFont(new Font("Segoe UI", 1, 14));
         title.setForeground(Theme.ACCENT);
         header.add(title, "West");
         this.add(header, "North");
         JPanel center = new JPanel(new GridLayout(4, 1, 6, 6));
         center.setOpaque(false);
         center.setBorder(new EmptyBorder(10, 16, 10, 16));
         JCheckBox chkBin = new JCheckBox("Binary Game Data (File không đuôi)", true);
         JCheckBox chkJson = new JCheckBox("JSON Object (effect_" + model.id + ".json)", true);
         JCheckBox chkSql = new JCheckBox("SQL Insert Script (effect_" + model.id + ".sql)", true);

         for (JCheckBox c : new JCheckBox[]{chkBin, chkJson, chkSql}) {
            c.setOpaque(false);
            c.setForeground(Color.WHITE);
            center.add(c);
         }

         this.add(center, "Center");
         JPanel footer = new JPanel(new FlowLayout(2, 8, 8));
         footer.setOpaque(false);
         JButton btnExport = MainUI.actionBtn("XUẤT BẢN TẤT CẢ", MainUI.ACCENT);
         btnExport.addActionListener(e -> {
            try {
               File dir = new File(initialDir);
               if (!dir.exists()) {
                  dir.mkdirs();
               }

               if (chkBin.isSelected()) {
                  MainUI.this.writer.write(model, new File(dir, String.valueOf(model.id)));
               }

               if (chkJson.isSelected()) {
                  MainUI.this.writer.writeText(MainUI.this.writer.toJsonObject(model).toString(2), new File(dir, "effect_" + model.id + ".json"));
               }

               if (chkSql.isSelected()) {
                  MainUI.this.writer.writeText(MainUI.this.writer.generateEffectDataSql(model), new File(dir, "effect_" + model.id + ".sql"));
               }

               JOptionPane.showMessageDialog(this, "Xuất bản thành công vào thư mục:\n" + dir.getAbsolutePath(), "Thành công", 1);
               this.dispose();
            } catch (Exception var8x) {
               JOptionPane.showMessageDialog(this, "Lỗi khi xuất bản: " + var8x.getMessage(), "Lỗi", 0);
            }
         });
         JButton btnClose = MainUI.actionBtn("Đóng", new Color(45, 45, 55));
         btnClose.addActionListener(e -> this.dispose());
         footer.add(btnClose);
         footer.add(btnExport);
         this.add(footer, "South");
      }
   }
}
