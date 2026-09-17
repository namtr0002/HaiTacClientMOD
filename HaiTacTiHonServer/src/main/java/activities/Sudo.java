/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package activities;

import model.Player;
import network.Service;
import database.DbManager;
import network.Message;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import map.Zone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.Level;

/**
 *
 * @author Administrator
 */
public class Sudo {
      private static HashMap<String, Sudo> ENTRY = new HashMap<>();
   public int id;
   public int accountId;
   public String name;
   public short head;
   public short hair;
   public short hat;
   public byte chucInSudo;
   public short lvExp;
   public long exp;
   public int point;
   public List<String> nameRelative;

   public static void removeSudo(String name) {
      synchronized (ENTRY) {
         ENTRY.remove(name);
      }
   }

   public static String getOpBySize(int i, int level) {
      switch (i) {
         case 0:
            return "Tăng tấn công " + (5 + (2 * (level - 1))) + "%";
         case 1:
            return "Tốc độ hồi chiêu " + level + "%";
         case 2:
            return "Giảm né đối thủ " + level + "%";
      }
      return "";
   }

   public static void load(ResultSet rs) throws SQLException {
      historys.SudoHistory.load(rs);
   }

   public static void updateDb() {
      historys.SudoHistory.updateDb(ENTRY);
   }

   public short getExp() {
      if (this.lvExp <= 0) return 0;
      int idx = this.lvExp - 1;
      if (Level.ENTRYS == null || idx >= Level.ENTRYS.length || Level.ENTRYS[idx] == null || Level.ENTRYS[idx].exp <= 0) return 0;
      return (short) Math.min(1000, Math.max(0, (exp * 1000L) / Level.ENTRYS[idx].exp));
   }

   public static Sudo getSuDoByName(String name) {
      if (name == null || name.trim().isEmpty()) return null;
      synchronized (ENTRY) {
         return ENTRY.get(name);
      }
   }

   public static Sudo getSuDoById(int playerId) {
      if (playerId <= 0) return null;
      synchronized (ENTRY) {
         for (Sudo s : ENTRY.values()) {
            if (s != null && s.id == playerId) return s;
         }
      }
      return null;
   }

   public static Sudo getSuDo(Player p) {
      if (p == null) return null;
      Sudo s = getSuDoById(p.IDPlayer);
      if (s != null) return s;
      return getSuDoByName(p.name);
   }

   public static void addSuDoByName(String name, Sudo value) {
      if (name == null || name.trim().isEmpty() || value == null) return;
      synchronized (ENTRY) {
         ENTRY.put(name, value);
      }
   }
}
