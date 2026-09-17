package com.deplor.haitactihontool.tool;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataConverterPanel extends JPanel {
   private JTextArea txtStreamIn;
   private JTextArea txtStreamOut;
   private JTextArea txtEffOut;
   private JSONObject lastDecodedEffect = null;
   private JTextArea txtArrIn;
   private JTextArea txtArrOut;
   private JTextField txtTableName;
   private JTextArea txtSqlIn;
   private JTextArea txtSqlOut;

   public DataConverterPanel() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      this.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.buildUI();
   }

   private void buildUI() {
      JPanel hdr = new JPanel(new BorderLayout());
      hdr.setOpaque(false);
      hdr.setBorder(new EmptyBorder(0, 0, 8, 0));
      JLabel title = StyledUI.createLabel("⚡ HTTH DATA & BINARY STUDIO (Stream Parser, SQL, JSON, Arrays)", Theme.F_TITLE, Theme.ACCENT);
      JLabel sub = StyledUI.createLabel(
         "Giải mã nhị phân Java DataInputStream, HTTH Effect Data, Hex Dump, Mảng Code Byte Array (Java, C, Python), JSON <-> SQL Generator",
         Theme.F_TINY,
         Theme.TEXT_MUTED
      );
      hdr.add(title, "North");
      hdr.add(sub, "South");
      this.add(hdr, "North");
      JTabbedPane tabs = new JTabbedPane();
      tabs.setFont(Theme.F_BOLD);
      tabs.setBackground(Theme.BG_DARK);
      tabs.setForeground(Theme.TEXT_MAIN);
      tabs.addTab("\ud83e\uddec Giải Mã Binary (DataInputStream)", this.buildStreamParserTab());
      tabs.addTab("✨ HTTH Effect & Skill Decoder", this.buildEffectDecoderTab());
      tabs.addTab("\ud83d\udd04 Hex <-> Byte Arrays (Java/C/Python)", this.buildArraysTab());
      tabs.addTab("\ud83d\udcca JSON / CSV <-> SQL Generator", this.buildSqlTab());
      this.add(tabs, "Center");
   }

   private JPanel buildStreamParserTab() {
      JPanel p = new JPanel(new BorderLayout(0, 8));
      p.setBackground(Theme.BG_DARK);
      p.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel topBar = new JPanel(new FlowLayout(0, 6, 0));
      topBar.setOpaque(false);
      JButton btnOpen = StyledUI.createButton("\ud83d\udcc1 Mở File Binary...", Theme.BG_CARD);
      btnOpen.addActionListener(e -> this.loadBinaryFile());
      topBar.add(btnOpen);
      JButton btnParse = StyledUI.createButton("⚡ Phân Tích Chuỗi Hex", Theme.ACCENT);
      btnParse.addActionListener(e -> this.parseHexStream());
      topBar.add(btnParse);
      p.add(topBar, "North");
      JPanel split = new JPanel(new GridLayout(1, 2, 8, 0));
      split.setOpaque(false);
      JPanel pLeft = new JPanel(new BorderLayout(0, 4));
      pLeft.setOpaque(false);
      pLeft.add(StyledUI.createLabel("Dữ liệu Nhị Phân / Chuỗi Hex Đầu Vào:", Theme.F_BOLD, Theme.WARNING), "North");
      this.txtStreamIn = this.createTextArea(true);
      this.txtStreamIn.setForeground(Theme.SUCCESS);
      pLeft.add(new JScrollPane(this.txtStreamIn), "Center");
      split.add(pLeft);
      JPanel pRight = new JPanel(new BorderLayout(0, 4));
      pRight.setOpaque(false);
      pRight.add(StyledUI.createLabel("Kết Quả Giải Mã Tuần Tự (Decoded Types):", Theme.F_BOLD, Theme.ACCENT), "North");
      this.txtStreamOut = this.createTextArea(false);
      pRight.add(new JScrollPane(this.txtStreamOut), "Center");
      split.add(pRight);
      p.add(split, "Center");
      return p;
   }

   private void loadBinaryFile() {
      JFileChooser fc = new JFileChooser(".");
      if (fc.showOpenDialog(this) == 0) {
         try {
            File f = fc.getSelectedFile();
            byte[] data = Files.readAllBytes(f.toPath());
            StringBuilder sb = new StringBuilder();

            for (byte b : data) {
               sb.append(String.format("%02X ", b & 255));
            }

            this.txtStreamIn.setText(sb.toString().trim());
            this.parseRawBytes(data);
         } catch (Exception var9) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc file: " + var9.getMessage());
         }
      }
   }

   private void parseHexStream() {
      String raw = this.txtStreamIn.getText().replaceAll("[^0-9a-fA-F]", "");
      if (!raw.isEmpty()) {
         try {
            int len = raw.length();
            byte[] data = new byte[len / 2];

            for (int i = 0; i < len; i += 2) {
               data[i / 2] = (byte)((Character.digit(raw.charAt(i), 16) << 4) + Character.digit(raw.charAt(i + 1), 16));
            }

            this.parseRawBytes(data);
         } catch (Exception var5) {
            JOptionPane.showMessageDialog(this, "Chuỗi Hex không hợp lệ: " + var5.getMessage());
         }
      }
   }

   private void parseRawBytes(byte[] data) {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("=== TOTAL SIZE: %d bytes ===\n\n", data.length));
      int offset = 0;

      for (int total = data.length; offset < total; offset++) {
         int remaining = total - offset;
         String rowInfo = String.format("[@0x%04X | %04d] ", offset, offset);
         int bVal = data[offset] & 255;
         byte sbVal = data[offset];
         sb.append(rowInfo).append(String.format("HEX: 0x%02X | Byte: %4d (u:%3d)", bVal, sbVal, bVal));
         if (remaining >= 2) {
            short sVal = (short)((data[offset] & 255) << 8 | data[offset + 1] & 255);
            int usVal = (data[offset] & 255) << 8 | data[offset + 1] & 255;
            sb.append(String.format(" | Short: %6d", sVal));
            if (usVal > 0 && offset + 2 + usVal <= total) {
               try {
                  String cand = new String(data, offset + 2, usVal, "UTF-8");
                  boolean printable = true;

                  for (char c : cand.toCharArray()) {
                     if (c < ' ' && c != '\r' && c != '\n' && c != '\t') {
                        printable = false;
                     }
                  }

                  if (printable && !cand.isEmpty()) {
                     sb.append(String.format(" | UTF: \"%s\"", cand));
                  }
               } catch (Exception var17) {
               }
            }
         }

         if (remaining >= 4) {
            int iVal = (data[offset] & 255) << 24 | (data[offset + 1] & 255) << 16 | (data[offset + 2] & 255) << 8 | data[offset + 3] & 255;
            sb.append(String.format(" | Int: %11d", iVal));
         }

         sb.append("\n");
      }

      this.txtStreamOut.setText(sb.toString());
   }

   private JPanel buildEffectDecoderTab() {
      JPanel p = new JPanel(new BorderLayout(0, 8));
      p.setBackground(Theme.BG_DARK);
      p.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel topBar = new JPanel(new FlowLayout(0, 6, 0));
      topBar.setOpaque(false);
      JButton btnOpen = StyledUI.createButton("\ud83d\udcc1 Mở File Effect Binary (VD: data/1)", Theme.BG_CARD);
      btnOpen.addActionListener(e -> this.loadEffectData());
      topBar.add(btnOpen);
      JButton btnSql = StyledUI.createButton("\ud83d\udcca Xuất SQL INSERT INTO effect...", Theme.ACCENT);
      btnSql.addActionListener(e -> this.effectToSql());
      topBar.add(btnSql);
      JButton btnJson = StyledUI.createButton("\ud83d\udcc4 Xuất JSON Đầy Đủ", Theme.BG_CARD);
      btnJson.setForeground(Theme.SUCCESS);
      btnJson.addActionListener(e -> this.effectToJson());
      topBar.add(btnJson);
      p.add(topBar, "North");
      this.txtEffOut = this.createTextArea(false);
      p.add(new JScrollPane(this.txtEffOut), "Center");
      return p;
   }

   private void loadEffectData() {
      JFileChooser fc = new JFileChooser(".");
      if (fc.showOpenDialog(this) == 0) {
         try {
            File f = fc.getSelectedFile();
            byte[] data = Files.readAllBytes(f.toPath());
            String effId = f.getName();
            this.lastDecodedEffect = decodeHtthEffect(data, effId);
            this.txtEffOut.setText("=== DECODED HTTH EFFECT ID: " + effId + " ===\n\n" + this.lastDecodedEffect.toString(2));
         } catch (Exception var5) {
            JOptionPane.showMessageDialog(this, "Lỗi giải mã effect: " + var5.getMessage());
         }
      }
   }

   public static JSONObject decodeHtthEffect(byte[] data, String effId) throws Exception {
      DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
      int numImg = dis.readUnsignedByte();
      JSONArray parts = new JSONArray();

      for (int i = 0; i < numImg; i++) {
         int pid = dis.readUnsignedByte();
         int px = dis.readShort();
         int py = dis.readShort();
         int pw = dis.readShort();
         int ph = dis.readShort();
         JSONObject p = new JSONObject();
         p.put("id", pid);
         p.put("x", px);
         p.put("y", py);
         p.put("w", pw);
         p.put("h", ph);
         parts.put(p);
      }

      int numFrame = dis.readUnsignedByte();
      JSONArray frames = new JSONArray();

      for (int i = 0; i < numFrame; i++) {
         int numParts = dis.readUnsignedByte();
         JSONArray subparts = new JSONArray();

         for (int j = 0; j < numParts; j++) {
            int dx = dis.readShort();
            int dy = dis.readShort();
            int idImg = dis.readUnsignedByte();
            JSONObject sp = new JSONObject();
            sp.put("dx", dx);
            sp.put("dy", dy);
            sp.put("id_img", idImg);
            subparts.put(sp);
         }

         frames.put(subparts);
      }

      int numSeq = dis.available() >= 2 ? dis.readShort() : 0;
      JSONArray seq = new JSONArray();

      for (int i = 0; i < numSeq && dis.available() > 0; i++) {
         seq.put(dis.available() >= 2 ? dis.readShort() : dis.readUnsignedByte());
      }

      StringBuilder hex = new StringBuilder();

      for (byte b : data) {
         hex.append(String.format("%02X", b & 255));
      }

      JSONObject root = new JSONObject();
      root.put("effect_id", effId);
      root.put("num_img", numImg);
      root.put("img_parts", parts);
      root.put("num_frame", numFrame);
      root.put("frames", frames);
      root.put("num_sequence", seq.length());
      root.put("sequence", seq);
      root.put("raw_hex", hex.toString());
      return root;
   }

   private void effectToSql() {
      if (this.lastDecodedEffect == null) {
         JOptionPane.showMessageDialog(this, "Vui lòng mở file Effect trước!");
      } else {
         String sql = String.format(
            "INSERT INTO `effect` (`id`, `data`, `num_frame`, `num_img`) VALUES (%s, UNHEX('%s'), %d, %d);",
            this.lastDecodedEffect.optString("effect_id", "1"),
            this.lastDecodedEffect.optString("raw_hex"),
            this.lastDecodedEffect.optInt("num_frame"),
            this.lastDecodedEffect.optInt("num_img")
         );
         this.txtEffOut.setText("-- SQL INSERT STATEMENT GENERATED:\n" + sql + "\n\n-- JSON Structure:\n" + this.lastDecodedEffect.toString(2));
      }
   }

   private void effectToJson() {
      if (this.lastDecodedEffect != null) {
         JFileChooser fc = new JFileChooser(".");
         fc.setSelectedFile(new File("effect_" + this.lastDecodedEffect.optString("effect_id") + ".json"));
         if (fc.showSaveDialog(this) == 0) {
            try {
               Files.write(fc.getSelectedFile().toPath(), this.lastDecodedEffect.toString(2).getBytes("UTF-8"));
               JOptionPane.showMessageDialog(this, "Đã lưu JSON thành công!");
            } catch (Exception var3) {
               JOptionPane.showMessageDialog(this, "Lỗi lưu file: " + var3.getMessage());
            }
         }
      }
   }

   private JPanel buildArraysTab() {
      JPanel p = new JPanel(new BorderLayout(0, 8));
      p.setBackground(Theme.BG_DARK);
      p.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel topBar = new JPanel(new FlowLayout(0, 6, 0));
      topBar.setOpaque(false);
      JButton btnJava = StyledUI.createButton("☕ Java byte[]", Theme.ACCENT);
      btnJava.addActionListener(e -> this.convToJavaArray());
      topBar.add(btnJava);
      JButton btnC = StyledUI.createButton("⚙️ C/C++ uint8_t[]", Theme.BG_CARD);
      btnC.addActionListener(e -> this.convToCArray());
      topBar.add(btnC);
      JButton btnPy = StyledUI.createButton("\ud83d\udc0d Python bytes", Theme.BG_CARD);
      btnPy.addActionListener(e -> this.convToPyArray());
      topBar.add(btnPy);
      JButton btnJson = StyledUI.createButton("\ud83d\udcc4 JSON Array", Theme.BG_CARD);
      btnJson.setForeground(Theme.SUCCESS);
      btnJson.addActionListener(e -> this.convToJsonArray());
      topBar.add(btnJson);
      JButton btnB64 = StyledUI.createButton("\ud83d\udd12 Base64", Theme.BG_CARD);
      btnB64.setForeground(Theme.WARNING);
      btnB64.addActionListener(e -> this.convToBase64());
      topBar.add(btnB64);
      p.add(topBar, "North");
      JPanel split = new JPanel(new GridLayout(1, 2, 8, 0));
      split.setOpaque(false);
      JPanel pLeft = new JPanel(new BorderLayout(0, 4));
      pLeft.setOpaque(false);
      pLeft.add(StyledUI.createLabel("Dữ liệu vào (Hex / Text):", Theme.F_BOLD, Theme.WARNING), "North");
      this.txtArrIn = this.createTextArea(true);
      pLeft.add(new JScrollPane(this.txtArrIn), "Center");
      split.add(pLeft);
      JPanel pRight = new JPanel(new BorderLayout(0, 4));
      pRight.setOpaque(false);
      pRight.add(StyledUI.createLabel("Mã nguồn Mảng Code (Output):", Theme.F_BOLD, Theme.ACCENT), "North");
      this.txtArrOut = this.createTextArea(false);
      pRight.add(new JScrollPane(this.txtArrOut), "Center");
      split.add(pRight);
      p.add(split, "Center");
      return p;
   }

   private byte[] getInputBytes() {
      String raw = this.txtArrIn.getText().trim();
      if (raw.isEmpty()) {
         return new byte[0];
      } else {
         String clean = raw.replaceAll("[^0-9a-fA-F]", "");
         if (clean.length() >= 2 && clean.length() % 2 == 0) {
            try {
               int len = clean.length();
               byte[] data = new byte[len / 2];

               for (int i = 0; i < len; i += 2) {
                  data[i / 2] = (byte)((Character.digit(clean.charAt(i), 16) << 4) + Character.digit(clean.charAt(i + 1), 16));
               }

               return data;
            } catch (Exception var6) {
            }
         }

         return raw.getBytes(StandardCharsets.UTF_8);
      }
   }

   private void convToJavaArray() {
      byte[] b = this.getInputBytes();
      StringBuilder sb = new StringBuilder("byte[] data = new byte[] {\n");

      for (int i = 0; i < b.length; i++) {
         if (i % 12 == 0) {
            sb.append("    ");
         }

         int val = b[i] & 255;
         sb.append(val > 127 ? String.format("(byte) 0x%02X", val) : String.format("0x%02X", val));
         if (i < b.length - 1) {
            sb.append(", ");
         }

         if ((i + 1) % 12 == 0 && i < b.length - 1) {
            sb.append("\n");
         }
      }

      sb.append(String.format("\n}; // length: %d", b.length));
      this.txtArrOut.setText(sb.toString());
   }

   private void convToCArray() {
      byte[] b = this.getInputBytes();
      StringBuilder sb = new StringBuilder(String.format("const uint8_t data[%d] = {\n", b.length));

      for (int i = 0; i < b.length; i++) {
         if (i % 16 == 0) {
            sb.append("    ");
         }

         sb.append(String.format("0x%02X", b[i] & 255));
         if (i < b.length - 1) {
            sb.append(", ");
         }

         if ((i + 1) % 16 == 0 && i < b.length - 1) {
            sb.append("\n");
         }
      }

      sb.append("\n};");
      this.txtArrOut.setText(sb.toString());
   }

   private void convToPyArray() {
      byte[] b = this.getInputBytes();
      StringBuilder sb = new StringBuilder("data = bytes([\n");

      for (int i = 0; i < b.length; i++) {
         if (i % 16 == 0) {
            sb.append("    ");
         }

         sb.append(String.format("0x%02X", b[i] & 255));
         if (i < b.length - 1) {
            sb.append(", ");
         }

         if ((i + 1) % 16 == 0 && i < b.length - 1) {
            sb.append("\n");
         }
      }

      sb.append(String.format("\n]) # len=%d", b.length));
      this.txtArrOut.setText(sb.toString());
   }

   private void convToJsonArray() {
      byte[] b = this.getInputBytes();
      JSONArray arr = new JSONArray();

      for (byte val : b) {
         arr.put(val & 255);
      }

      this.txtArrOut.setText(arr.toString());
   }

   private void convToBase64() {
      byte[] b = this.getInputBytes();
      this.txtArrOut.setText(Base64.getEncoder().encodeToString(b));
   }

   private JPanel buildSqlTab() {
      JPanel p = new JPanel(new BorderLayout(0, 8));
      p.setBackground(Theme.BG_DARK);
      p.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel topBar = new JPanel(new FlowLayout(0, 6, 0));
      topBar.setOpaque(false);
      topBar.add(StyledUI.createLabel("Tên Bảng SQL:", Theme.F_BOLD, Theme.TEXT_MAIN));
      this.txtTableName = StyledUI.createTextField("item_template");
      this.txtTableName.setPreferredSize(new Dimension(140, 28));
      topBar.add(this.txtTableName);
      JButton btnInsert = StyledUI.createButton("⚡ Tạo SQL INSERT", Theme.ACCENT);
      btnInsert.addActionListener(e -> this.generateSqlInsert());
      topBar.add(btnInsert);
      JButton btnUpdate = StyledUI.createButton("⚡ Tạo SQL UPDATE", Theme.BG_CARD);
      btnUpdate.setForeground(Theme.WARNING);
      btnUpdate.addActionListener(e -> this.generateSqlUpdate());
      topBar.add(btnUpdate);
      p.add(topBar, "North");
      JPanel split = new JPanel(new GridLayout(1, 2, 8, 0));
      split.setOpaque(false);
      JPanel pLeft = new JPanel(new BorderLayout(0, 4));
      pLeft.setOpaque(false);
      pLeft.add(StyledUI.createLabel("Dữ liệu JSON Array hoặc CSV:", Theme.F_BOLD, Theme.WARNING), "North");
      this.txtSqlIn = this.createTextArea(true);
      this.txtSqlIn
         .setText(
            "[\n  {\"id\": 1, \"name\": \"Gậy Như Ý\", \"type\": 0, \"price\": 1000},\n  {\"id\": 2, \"name\": \"Mũ Rơm\", \"type\": 1, \"price\": 500}\n]"
         );
      pLeft.add(new JScrollPane(this.txtSqlIn), "Center");
      split.add(pLeft);
      JPanel pRight = new JPanel(new BorderLayout(0, 4));
      pRight.setOpaque(false);
      pRight.add(StyledUI.createLabel("Câu Lệnh SQL Được Tạo (Output):", Theme.F_BOLD, Theme.ACCENT), "North");
      this.txtSqlOut = this.createTextArea(false);
      pRight.add(new JScrollPane(this.txtSqlOut), "Center");
      split.add(pRight);
      p.add(split, "Center");
      return p;
   }

   private void generateSqlInsert() {
      String raw = this.txtSqlIn.getText().trim();
      String table = this.txtTableName.getText().trim();
      if (table.isEmpty()) {
         table = "table_name";
      }

      if (!raw.isEmpty()) {
         try {
            JSONArray arr = new JSONArray(raw.startsWith("[") ? raw : "[" + raw + "]");
            if (arr.isEmpty()) {
               return;
            }

            JSONObject first = arr.getJSONObject(0);
            List<String> cols = new ArrayList<>(first.keySet());
            StringBuilder colsSb = new StringBuilder();

            for (int i = 0; i < cols.size(); i++) {
               colsSb.append("`").append(cols.get(i)).append("`");
               if (i < cols.size() - 1) {
                  colsSb.append(", ");
               }
            }

            StringBuilder valuesSb = new StringBuilder();

            for (int ix = 0; ix < arr.length(); ix++) {
               JSONObject row = arr.getJSONObject(ix);
               valuesSb.append("  (");

               for (int j = 0; j < cols.size(); j++) {
                  String col = cols.get(j);
                  Object val = row.opt(col);
                  if (val == null || val == JSONObject.NULL) {
                     valuesSb.append("NULL");
                  } else if (val instanceof Number) {
                     valuesSb.append(val);
                  } else {
                     valuesSb.append("'").append(val.toString().replace("'", "''")).append("'");
                  }

                  if (j < cols.size() - 1) {
                     valuesSb.append(", ");
                  }
               }

               valuesSb.append(")");
               if (ix < arr.length() - 1) {
                  valuesSb.append(",\n");
               }
            }

            String sql = String.format("INSERT INTO `%s` (%s) VALUES\n%s;", table, colsSb, valuesSb);
            this.txtSqlOut.setText(sql);
         } catch (Exception var13) {
            JOptionPane.showMessageDialog(this, "Lỗi parse JSON: " + var13.getMessage());
         }
      }
   }

   private void generateSqlUpdate() {
      String raw = this.txtSqlIn.getText().trim();
      String table = this.txtTableName.getText().trim();
      if (table.isEmpty()) {
         table = "table_name";
      }

      if (!raw.isEmpty()) {
         try {
            JSONArray arr = new JSONArray(raw.startsWith("[") ? raw : "[" + raw + "]");
            StringBuilder sql = new StringBuilder();

            for (int i = 0; i < arr.length(); i++) {
               JSONObject row = arr.getJSONObject(i);
               Object idVal = row.opt("id");
               StringBuilder sets = new StringBuilder();
               int count = 0;

               for (String k : row.keySet()) {
                  if (!"id".equals(k)) {
                     if (count++ > 0) {
                        sets.append(", ");
                     }

                     Object v = row.opt(k);
                     if (v == null || v == JSONObject.NULL) {
                        sets.append(String.format("`%s` = NULL", k));
                     } else if (v instanceof Number) {
                        sets.append(String.format("`%s` = %s", k, v));
                     } else {
                        sets.append(String.format("`%s` = '%s'", k, v.toString().replace("'", "''")));
                     }
                  }
               }

               if (idVal != null) {
                  sql.append(String.format("UPDATE `%s` SET %s WHERE `id` = %s;\n", table, sets, idVal));
               } else {
                  sql.append(String.format("UPDATE `%s` SET %s;\n", table, sets));
               }
            }

            this.txtSqlOut.setText(sql.toString());
         } catch (Exception var13) {
            JOptionPane.showMessageDialog(this, "Lỗi parse JSON: " + var13.getMessage());
         }
      }
   }

   private JTextArea createTextArea(boolean editable) {
      JTextArea t = new JTextArea();
      t.setBackground(new Color(8, 8, 12));
      t.setForeground(new Color(205, 214, 244));
      t.setFont(Theme.F_MONO);
      t.setEditable(editable);
      t.setCaretColor(Color.WHITE);
      return t;
   }
}
