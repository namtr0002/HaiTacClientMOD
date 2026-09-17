package com.deplor.haitactihontool.tool;

import com.deplor.haitactihontool.config.Theme;
import com.deplor.haitactihontool.ui.components.StyledUI;
import com.deplor.haitactihontool.ultimate_security.KeyAuth;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class CharsetCryptoPanel extends JPanel {
   private JTextArea txtCharsetIn;
   private JTextArea txtCharsetOut;
   private JTextArea txtCryptoIn;
   private JTextArea txtCryptoOut;
   private static final Map<Character, Character> TCVN3_MAP = new HashMap<>();

   public CharsetCryptoPanel() {
      this.setLayout(new BorderLayout());
      this.setBackground(Theme.BG_DARKER);
      this.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.buildUI();
   }

   private void buildUI() {
      JPanel hdr = new JPanel(new BorderLayout());
      hdr.setOpaque(false);
      hdr.setBorder(new EmptyBorder(0, 0, 8, 0));
      JLabel title = StyledUI.createLabel("\ud83d\udd20 CHARSET & CRYPTO ENGINE (Bảng Mã, Tiếng Việt, Quantum Shield, Hashes)", Theme.F_TITLE, Theme.ACCENT);
      JLabel sub = StyledUI.createLabel(
         "Chuyển đổi bảng mã UTF-8 / Windows-1258 / TCVN3 / VNI, Unicode Escape, Tiếng Việt có/không dấu, HTTH V5 Quantum Shield, Hash MD5/SHA256/CRC32",
         Theme.F_TINY,
         Theme.TEXT_MUTED
      );
      hdr.add(title, "North");
      hdr.add(sub, "South");
      this.add(hdr, "North");
      JPanel body = new JPanel(new GridLayout(1, 2, 8, 0));
      body.setOpaque(false);
      body.add(this.buildLeftCharsetCard());
      body.add(this.buildRightCryptoCard());
      this.add(body, "Center");
   }

   private JPanel buildLeftCharsetCard() {
      JPanel p = new JPanel(new BorderLayout(0, 6));
      p.setBackground(Theme.BG_DARK);
      p.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER), new EmptyBorder(10, 10, 10, 10)));
      p.add(StyledUI.createLabel("\ud83d\udd24 BẢNG MÃ & XỬ LÝ CHUỖI (CHARSET)", Theme.F_BOLD, Theme.ACCENT), "North");
      JPanel center = new JPanel(new BorderLayout(0, 6));
      center.setOpaque(false);
      JPanel btnP = new JPanel(new GridLayout(2, 4, 3, 3));
      btnP.setOpaque(false);
      JButton btnNoAcc = StyledUI.createButton("Bỏ Dấu TV", Theme.BG_CARD);
      btnNoAcc.addActionListener(e -> this.removeAccents());
      btnP.add(btnNoAcc);
      JButton btnTcvn = StyledUI.createButton("TCVN3 -> Uni", Theme.BG_CARD);
      btnTcvn.addActionListener(e -> this.convTcvn3());
      btnP.add(btnTcvn);
      JButton btnEsc = StyledUI.createButton("Escape (\\u)", Theme.BG_CARD);
      btnEsc.addActionListener(e -> this.unicodeEscape());
      btnP.add(btnEsc);
      JButton btnUnesc = StyledUI.createButton("Unescape (\\u)", Theme.BG_CARD);
      btnUnesc.addActionListener(e -> this.unicodeUnescape());
      btnP.add(btnUnesc);
      JButton btnUrlEnc = StyledUI.createButton("URL Encode", Theme.BG_CARD);
      btnUrlEnc.addActionListener(e -> this.urlEncode());
      btnP.add(btnUrlEnc);
      JButton btnUrlDec = StyledUI.createButton("URL Decode", Theme.BG_CARD);
      btnUrlDec.addActionListener(e -> this.urlDecode());
      btnP.add(btnUrlDec);
      JButton btnHtmlEnc = StyledUI.createButton("HTML Encode", Theme.BG_CARD);
      btnHtmlEnc.addActionListener(e -> this.htmlEncode());
      btnP.add(btnHtmlEnc);
      JButton btnHtmlDec = StyledUI.createButton("HTML Decode", Theme.BG_CARD);
      btnHtmlDec.addActionListener(e -> this.htmlDecode());
      btnP.add(btnHtmlDec);
      center.add(btnP, "North");
      JPanel split = new JPanel(new GridLayout(2, 1, 0, 4));
      split.setOpaque(false);
      JPanel pIn = new JPanel(new BorderLayout(0, 2));
      pIn.setOpaque(false);
      pIn.add(StyledUI.createLabel("Văn Bản Đầu Vào:", Theme.F_TINY, Theme.WARNING), "North");
      this.txtCharsetIn = this.createTextArea(true);
      this.txtCharsetIn.setForeground(Theme.SUCCESS);
      pIn.add(new JScrollPane(this.txtCharsetIn), "Center");
      split.add(pIn);
      JPanel pOut = new JPanel(new BorderLayout(0, 2));
      pOut.setOpaque(false);
      pOut.add(StyledUI.createLabel("Kết Quả Chuyển Đổi:", Theme.F_TINY, Theme.ACCENT), "North");
      this.txtCharsetOut = this.createTextArea(false);
      pOut.add(new JScrollPane(this.txtCharsetOut), "Center");
      split.add(pOut);
      center.add(split, "Center");
      p.add(center, "Center");
      return p;
   }

   private JPanel buildRightCryptoCard() {
      JPanel p = new JPanel(new BorderLayout(0, 6));
      p.setBackground(Theme.BG_DARK);
      p.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, Theme.BORDER), new EmptyBorder(10, 10, 10, 10)));
      p.add(StyledUI.createLabel("\ud83d\udd10 HTTH CRYPTO & HASH LAB", Theme.F_BOLD, Theme.WARNING), "North");
      JPanel center = new JPanel(new BorderLayout(0, 6));
      center.setOpaque(false);
      JPanel btnP = new JPanel(new GridLayout(2, 2, 3, 3));
      btnP.setOpaque(false);
      JButton btnV5Enc = StyledUI.createButton("\ud83d\udd12 Mã Hóa V5 (Quantum Shield)", Theme.ACCENT);
      btnV5Enc.addActionListener(e -> this.v5Encrypt());
      btnP.add(btnV5Enc);
      JButton btnV5Dec = StyledUI.createButton("\ud83d\udd13 Giải Mã V5", Theme.BG_CARD);
      btnV5Dec.setForeground(Theme.SUCCESS);
      btnV5Dec.addActionListener(e -> this.v5Decrypt());
      btnP.add(btnV5Dec);
      JButton btnV4Enc = StyledUI.createButton("\ud83d\udd12 Mã Hóa V4", Theme.BG_CARD);
      btnV4Enc.addActionListener(e -> this.v4Encrypt());
      btnP.add(btnV4Enc);
      JButton btnGenKey = StyledUI.createButton("\ud83c\udfb2 Tạo Key Ngẫu Nhiên", Theme.BG_CARD);
      btnGenKey.setForeground(Theme.WARNING);
      btnGenKey.addActionListener(e -> this.genRandomKey());
      btnP.add(btnGenKey);
      center.add(btnP, "North");
      JPanel split = new JPanel(new GridLayout(2, 1, 0, 4));
      split.setOpaque(false);
      JPanel pIn = new JPanel(new BorderLayout(0, 2));
      pIn.setOpaque(false);
      pIn.add(StyledUI.createLabel("Dữ liệu Cần Mã Hóa / Băm:", Theme.F_TINY, Theme.TEXT_MUTED), "North");
      this.txtCryptoIn = this.createTextArea(true);
      this.txtCryptoIn.setForeground(Theme.SUCCESS);
      pIn.add(new JScrollPane(this.txtCryptoIn), "Center");
      split.add(pIn);
      JPanel pOut = new JPanel(new BorderLayout(0, 2));
      pOut.setOpaque(false);
      JButton btnCalcHash = StyledUI.createButton("⚡ Tính Tất Cả Mã Băm (MD5, SHA, CRC32)", Theme.BG_CARD);
      btnCalcHash.addActionListener(e -> this.calcAllHashes());
      pOut.add(btnCalcHash, "North");
      this.txtCryptoOut = this.createTextArea(false);
      pOut.add(new JScrollPane(this.txtCryptoOut), "Center");
      split.add(pOut);
      center.add(split, "Center");
      p.add(center, "Center");
      return p;
   }

   private void removeAccents() {
      String text = this.txtCharsetIn.getText().trim();
      String temp = Normalizer.normalize(text, Form.NFD);
      Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
      String result = pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
      this.txtCharsetOut.setText(result);
   }

   private void convTcvn3() {
      String text = this.txtCharsetIn.getText().trim();
      StringBuilder sb = new StringBuilder();

      for (char c : text.toCharArray()) {
         sb.append(TCVN3_MAP.getOrDefault(c, c));
      }

      this.txtCharsetOut.setText(sb.toString());
   }

   private void unicodeEscape() {
      String text = this.txtCharsetIn.getText().trim();
      StringBuilder sb = new StringBuilder();

      for (char c : text.toCharArray()) {
         if (c > 127) {
            sb.append(String.format("\\u%04x", Integer.valueOf(c)));
         } else {
            sb.append(c);
         }
      }

      this.txtCharsetOut.setText(sb.toString());
   }

   private void unicodeUnescape() {
      String text = this.txtCharsetIn.getText().trim();
      StringBuilder sb = new StringBuilder();
      int len = text.length();

      for (int i = 0; i < len; i++) {
         if (i + 5 < len && text.charAt(i) == '\\' && text.charAt(i + 1) == 'u') {
            try {
               int code = Integer.parseInt(text.substring(i + 2, i + 6), 16);
               sb.append((char)code);
               i += 5;
               continue;
            } catch (Exception var6) {
            }
         }

         sb.append(text.charAt(i));
      }

      this.txtCharsetOut.setText(sb.toString());
   }

   private void urlEncode() {
      try {
         this.txtCharsetOut.setText(URLEncoder.encode(this.txtCharsetIn.getText().trim(), "UTF-8"));
      } catch (Exception var2) {
      }
   }

   private void urlDecode() {
      try {
         this.txtCharsetOut.setText(URLDecoder.decode(this.txtCharsetIn.getText().trim(), "UTF-8"));
      } catch (Exception var2) {
      }
   }

   private void htmlEncode() {
      String t = this.txtCharsetIn.getText().trim();
      t = t.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
      this.txtCharsetOut.setText(t);
   }

   private void htmlDecode() {
      String t = this.txtCharsetIn.getText().trim();
      t = t.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&#39;", "'").replace("&amp;", "&");
      this.txtCharsetOut.setText(t);
   }

   private void v5Encrypt() {
      String text = this.txtCryptoIn.getText().trim();
      if (!text.isEmpty()) {
         String enc = KeyAuth.encryptV5(text);
         this.txtCryptoOut.setText("=== HTTH-V5 QUANTUM SHIELD ENCRYPTED ===\n" + enc);
      }
   }

   private void v5Decrypt() {
      String text = this.txtCryptoIn.getText().trim();
      if (!text.isEmpty()) {
         String dec = KeyAuth.decryptV5(text);
         this.txtCryptoOut.setText("=== HTTH-V5 DECRYPTED RESULT ===\n" + dec);
      }
   }

   private void v4Encrypt() {
      String text = this.txtCryptoIn.getText().trim();
      if (!text.isEmpty()) {
         String enc = KeyAuth.encryptV4(text);
         this.txtCryptoOut.setText("=== HTTH-V4 ENCRYPTED ===\n" + enc);
      }
   }

   private void calcAllHashes() {
      String text = this.txtCryptoIn.getText().trim();
      byte[] b = text.getBytes(StandardCharsets.UTF_8);
      CRC32 crc = new CRC32();
      crc.update(b);
      String crcVal = String.format("0x%08X", crc.getValue());
      String md5Val = this.hashHex("MD5", b);
      String sha1Val = this.hashHex("SHA-1", b);
      String sha256Val = this.hashHex("SHA-256", b);
      String sha512Val = this.hashHex("SHA-512", b);
      String out = "=== HASHES & CHECKSUM RESULTS ===\nCRC-32 : "
         + crcVal
         + "\nMD5    : "
         + md5Val
         + "\nSHA-1  : "
         + sha1Val
         + "\nSHA-256: "
         + sha256Val
         + "\nSHA-512: "
         + sha512Val
         + "\nBase64 : "
         + Base64.getEncoder().encodeToString(b)
         + "\nLength : "
         + b.length
         + " bytes | "
         + text.length()
         + " chars";
      this.txtCryptoOut.setText(out);
   }

   private String hashHex(String alg, byte[] data) {
      try {
         MessageDigest md = MessageDigest.getInstance(alg);
         byte[] dig = md.digest(data);
         StringBuilder sb = new StringBuilder();

         for (byte b : dig) {
            sb.append(String.format("%02X", b & 255));
         }

         return sb.toString();
      } catch (Exception var10) {
         return "N/A";
      }
   }

   private void genRandomKey() {
      String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
      Random r = new Random();
      StringBuilder rand = new StringBuilder();

      for (int i = 0; i < 16; i++) {
         rand.append(chars.charAt(r.nextInt(chars.length())));
      }

      String key = String.format("HTTH-%s-%s-%s-%s", rand.substring(0, 4), rand.substring(4, 8), rand.substring(8, 12), rand.substring(12, 16));
      this.txtCryptoIn.setText(key);
      this.v5Encrypt();
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

   static {
      char[][] pairs = new char[][]{
         {'µ', 'à'},
         {'¸', 'á'},
         {'¶', 'ả'},
         {'·', 'ã'},
         {'¹', 'ạ'},
         {'¨', 'ă'},
         {'»', 'ằ'},
         {'¾', 'ắ'},
         {'¼', 'ẳ'},
         {'½', 'ẵ'},
         {'Æ', 'ặ'},
         {'©', 'â'},
         {'Ç', 'ầ'},
         {'Ê', 'ấ'},
         {'È', 'ẩ'},
         {'É', 'ẫ'},
         {'Ë', 'ậ'},
         {'Ì', 'è'},
         {'Ð', 'é'},
         {'Î', 'ẻ'},
         {'Ï', 'ẽ'},
         {'Ñ', 'ẹ'},
         {'ª', 'ê'},
         {'Ò', 'ề'},
         {'Õ', 'ế'},
         {'Ó', 'ể'},
         {'Ô', 'ễ'},
         {'Ö', 'ệ'},
         {'ß', 'ò'},
         {'ã', 'ó'},
         {'á', 'ỏ'},
         {'â', 'õ'},
         {'ä', 'ọ'},
         {'«', 'ô'},
         {'å', 'ồ'},
         {'è', 'ố'},
         {'æ', 'ổ'},
         {'ç', 'ỗ'},
         {'é', 'ộ'},
         {'¬', 'ơ'},
         {'ê', 'ờ'},
         {'í', 'ớ'},
         {'ë', 'ở'},
         {'ì', 'ỡ'},
         {'î', 'ợ'},
         {'ï', 'ù'},
         {'ó', 'ú'},
         {'ñ', 'ủ'},
         {'ò', 'ũ'},
         {'ô', 'ụ'},
         {'\u00ad', 'ư'},
         {'õ', 'ừ'},
         {'ø', 'ứ'},
         {'ö', 'ử'},
         {'÷', 'ữ'},
         {'ù', 'ự'},
         {'×', 'ì'},
         {'Ý', 'í'},
         {'Ø', 'ỉ'},
         {'Ü', 'ĩ'},
         {'Þ', 'ị'},
         {'ú', 'ỳ'},
         {'ý', 'ý'},
         {'û', 'ỷ'},
         {'ü', 'ỹ'},
         {'þ', 'ỵ'},
         {'®', 'đ'},
         {'§', 'Đ'}
      };

      for (char[] p : pairs) {
         TCVN3_MAP.put(p[0], p[1]);
      }
   }
}
