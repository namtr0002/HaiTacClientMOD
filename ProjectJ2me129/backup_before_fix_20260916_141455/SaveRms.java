import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class SaveRms {
   public static String userLast = "";
   public static byte[] datahotKeySkill;

   public static void AA(String user, String pass) {
      if (user.length() != 0 && pass.length() != 0) {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         DataOutputStream var3 = new DataOutputStream(var2);

         try {
            var3.writeUTF(user);
            var3.writeUTF(pass);
            var3.writeByte(ListChar_Screen.IndexCharSelected);
            var3.writeByte((byte)GameCanvas.IndexServer);
            CRes.saveRMS("MAIN_user_pass", var2.toByteArray());
            var3.close();
         } catch (Exception var4) {
         }
      }
   }

   private static boolean AL() {
      byte[] var0;
      if ((var0 = CRes.loadRMS("MAIN_user_pass")) == null) {
         if (GameCanvas.IndexServer > GameCanvas.strListServer[GameCanvas.language].length) {
            GameCanvas.IndexServer = 0;
         }

         return false;
      } else {
         try {
            ByteArrayInputStream var2 = new ByteArrayInputStream(var0);
            DataInputStream var3 = new DataInputStream(var2);
            GameCanvas.loginScr.AC.AB(var3.readUTF());
            GameCanvas.loginScr.AD.AB(var3.readUTF());
            ListChar_Screen.IndexCharSelected = var3.readByte();
            if (var3.available() > 0) {
               if ((GameCanvas.IndexServer = var3.readByte()) > GameCanvas.strListServer[GameCanvas.language].length) {
                  GameCanvas.IndexServer = 0;
               }
            } else {
               GameCanvas.IndexServer = GameCanvas.strListServer[GameCanvas.language].length - 1;
            }

            var3.close();
         } catch (Exception var1) {
         }

         return true;
      }
   }

   public static void AA(String var0) {
      if (var0.length() != 0) {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         DataOutputStream var2 = new DataOutputStream(var1);

         try {
            var2.writeUTF(var0);
            CRes.saveRMS("MAIN_user_last", var1.toByteArray());
            var2.close();
         } catch (Exception var3) {
         }
      }
   }

   public static void AA() {
      byte[] var0;
      if ((var0 = CRes.loadRMS("MAIN_user_last")) != null) {
         try {
            ByteArrayInputStream var2 = new ByteArrayInputStream(var0);
            DataInputStream var3;
            userLast = (var3 = new DataInputStream(var2)).readUTF();
            var3.close();
            if (userLast.length() >= 10) {
               userLast.substring(0, 9);
               return;
            }
         } catch (Exception var1) {
         }

      }
   }

   public static String AB() {
      byte[] var0;
      if ((var0 = CRes.loadRMS("MAIN_ip_last")) == null) {
         return "";
      } else {
         try {
            ByteArrayInputStream var3 = new ByteArrayInputStream(var0);
            DataInputStream var4;
            String var1 = (var4 = new DataInputStream(var3)).readUTF();
            var4.close();
            return var1;
         } catch (Exception var2) {
            return "";
         }
      }
   }

   public final void loadBeginGame() {
      AL();
      GlobalService.AH = AC("VerdataCharPart");
      GlobalService.AD = AC("VerdataMon");
      GlobalService.AF = AC("VerdataAttri");
      GlobalService.AE = AC("VerdataPotion");
      GlobalService.AG = AC("VerdataNameMap");
      GlobalService.AJ = AC("VerdataItemMap");
      GlobalService.AK = AC("VerdataImageSave");
      GlobalService.AI = AC("VerdataNamePotionquest");
      GlobalService.AL = AC("VerdataUpgradeSave");
   }

   public static void AD() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         for(int var2 = 0; var2 < Player.hotkeyPlayer.length; ++var2) {
            for(int var3 = 0; var3 < Player.hotkeyPlayer[var2].length; ++var3) {
               var1.writeByte(Player.hotkeyPlayer[var2][var3].getIndexDelay());
               if (Player.hotkeyPlayer[var2][var3].getIndexDelay() == 0) {
                  var1.writeShort(Player.hotkeyPlayer[var2][var3].itemcur.ID);
               } else if (Player.hotkeyPlayer[var2][var3].getIndexDelay() == 1) {
                  var1.writeShort(Player.hotkeyPlayer[var2][var3].skill.ID);
               }
            }
         }

         GlobalService.getInstance().Save_RMS_Server((byte)1, (byte)0, (byte[])var0.toByteArray());
         var1.close();
      } catch (Exception var4) {
      }
   }

   public static void loadHotKey(byte[] var0) {
      int var7;
      if (var0 == null) {
         for(var7 = 0; var7 < Player.hotkeyPlayer.length; ++var7) {
            Skill_Info var8;
            if (Player.vecListSkill.size() > 0 && (var8 = (Skill_Info)Player.vecListSkill.elementAt(0)) != null) {
               MainSkill var10;
               (var10 = new MainSkill(var8.ID, (short)-1)).AB = var8.indexHotKey;
               var10.idIcon = var8.idIcon;
               var10.isBuff = var8.typeSkill == 2;
               Player.hotkeyPlayer[var7][2].setSkill(var10, var10.idIcon);
            }
         }

      } else {
         try {
            ByteArrayInputStream var6 = new ByteArrayInputStream(var0);
            DataInputStream var1 = new DataInputStream(var6);

            for(int var2 = 0; var2 < Player.hotkeyPlayer.length; ++var2) {
               for(var7 = 0; var7 < Player.hotkeyPlayer[var2].length; ++var7) {
                  byte var3;
                  if ((var3 = var1.readByte()) != -1) {
                     short var4 = var1.readShort();
                     if (var3 == 0) {
                        MainItem var11;
                        if ((var11 = MainItem.getItemVec((byte)4, var4, Player.vecInventory)) != null) {
                           Player.hotkeyPlayer[var2][var7].setPotion(var11);
                        }
                     } else {
                        Skill_Info var12;
                        if (var3 == 1 && (var12 = Skill_Info.getSkillFromID(var4)) != null) {
                           MainSkill var14;
                           (var14 = new MainSkill(var12.ID, (short)-1)).AB = var12.indexHotKey;
                           var14.idIcon = var12.idIcon;
                           var14.isBuff = var12.typeSkill == 2;
                           Player.hotkeyPlayer[var2][var7].setSkill(var14, var14.idIcon);
                        }
                     }
                  }
               }
            }

            var1.close();
            Skill_Info var9;
            if ((var9 = (Skill_Info)Player.vecListSkill.elementAt(0)) != null) {
               for(var7 = 0; var7 < Player.hotkeyPlayer.length; ++var7) {
                  if (Player.vecListSkill.size() > 0 && (Player.hotkeyPlayer[var7][2].skill == null || Player.hotkeyPlayer[var7][2].skill.ID != var9.ID)) {
                     MainSkill var13;
                     (var13 = new MainSkill(var9.ID, (short)-1)).AB = var9.indexHotKey;
                     var13.idIcon = var9.idIcon;
                     var13.isBuff = var9.typeSkill == 2;
                     Player.hotkeyPlayer[var7][2].setSkill(var13, var13.idIcon);
                  }
               }

               return;
            }
         } catch (Exception var5) {
         }

      }
   }

   public final void AA(byte var1, byte[] var2) {
      if (var1 == 0) {
         datahotKeySkill = var2;
         loadHotKey(var2);
      } else {
         byte[] var10;
         ByteArrayInputStream var11;
         DataInputStream var12;
         if (var1 == 1) {
            var10 = var2;
            if (var2 == null) {
               return;
            }

            try {
               var11 = new ByteArrayInputStream(var10);
               if (Player.isMPHP = (var12 = new DataInputStream(var11)).readByte() == 1) {
                  Class_AI.AA = var12.readByte();
                  Class_AI.AB = var12.readByte();
                  Class_AI.AC = var12.readByte();
               }

               var12.close();
            } catch (Exception var9) {
               return;
            }
         } else {
            if (var1 == 2) {
               AC(var2);
               return;
            }

            if (var1 == 3) {
               var10 = var2;
               if (var2 == null) {
                  return;
               }

               try {
                  var11 = new ByteArrayInputStream(var10);
                  Player.QP = (var12 = new DataInputStream(var11)).readByte();
                  var12.close();
               } catch (Exception var8) {
                  return;
               }
            } else {
               if (var1 == 4) {
                  if (var2 == null) {
                     GameScreen.CV = 0;
                     return;
                  }

                  try {
                     var11 = new ByteArrayInputStream(var2);
                     GameScreen.CV = (var12 = new DataInputStream(var11)).readShort();
                     var12.close();
                     return;
                  } catch (Exception var3) {
                     return;
                  }
               }

               if (var1 == 5) {
                  AD(var2);
                  return;
               }

               if (var1 == 6) {
                  AB(var2);
                  return;
               }

               if (var1 == 7) {
                  if (var2 != null) {
                     try {
                        var11 = new ByteArrayInputStream(var2);
                        GameScreen.isShowSkillBuff = (var12 = new DataInputStream(var11)).readByte() == 1;
                        var12.close();
                        return;
                     } catch (Exception var4) {
                        return;
                     }
                  }
               } else if (var1 == 8) {
                  if (var2 != null) {
                     try {
                        var11 = new ByteArrayInputStream(var2);
                        GameScreen.CW = (var12 = new DataInputStream(var11)).readByte();
                        var12.close();
                        return;
                     } catch (Exception var5) {
                        return;
                     }
                  }
               } else if (var1 == 9) {
                  if (var2 != null) {
                     try {
                        var11 = new ByteArrayInputStream(var2);
                        GameScreen.isShowSkillPlayer = (var12 = new DataInputStream(var11)).readByte() == 1;
                        var12.close();
                        return;
                     } catch (Exception var6) {
                        return;
                     }
                  }
               } else if (var1 == 10 && var2 != null) {
                  try {
                     var11 = new ByteArrayInputStream(var2);
                     GameScreen.isShowNhanVat = (var12 = new DataInputStream(var11)).readByte() == 1;
                     var12.close();
                     return;
                  } catch (Exception var7) {
                  }
               }
            }
         }

      }
   }

   public static void AE() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeByte(Player.QP);
         GlobalService.getInstance().Save_RMS_Server((byte)1, (byte)3, (byte[])var0.toByteArray());
         var1.close();
      } catch (Exception var2) {
      }
   }

   public static void AA(short var0, String var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      DataOutputStream var3 = new DataOutputStream(var2);

      try {
         var3.writeShort(var0);
         CRes.saveRMS("MAIN_" + var1, var2.toByteArray());
         var3.close();
      } catch (Exception var4) {
      }
   }

   private static short AC(String var0) {
      byte[] var3 = CRes.loadRMS("MAIN_" + var0);
      short var1 = -1;

      try {
         if (var3 != null) {
            ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
            DataInputStream var5;
            var1 = (var5 = new DataInputStream(var4)).readShort();
            var5.close();
         }
      } catch (Exception var2) {
      }

      return var1;
   }

   public static void saveData(byte[] var0, String var1) {
      try {
         CRes.saveRMS("MAIN_" + var1, var0);
      } catch (Exception var2) {
      }
   }

   public static DataInputStream AB(String var0) {
      byte[] var3 = CRes.loadRMS("MAIN_" + var0);
      DataInputStream var1 = null;
      if (var3 == null) {
         return null;
      } else {
         try {
            ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
            var1 = new DataInputStream(var4);
         } catch (Exception var2) {
         }

         return var1;
      }
   }

   public static void AF() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeByte((byte)(Player.isMPHP ? 1 : 0));
         if (Player.isMPHP) {
            var1.writeByte((byte)Class_AI.AA);
            var1.writeByte((byte)Class_AI.AB);
            var1.writeByte((byte)Class_AI.AC);
         }

         GlobalService.getInstance().Save_RMS_Server((byte)1, (byte)1, (byte[])var0.toByteArray());
         var1.close();
      } catch (Exception var2) {
      }
   }

   public static void AG() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeByte((byte)Player.QK.size());

         for(int var2 = 0; var2 < Player.QK.size(); ++var2) {
            Class_FA var3 = (Class_FA)Player.QK.elementAt(var2);
            var1.writeByte(var3.AA);
            var1.writeByte(var3.AB);

            for(int var4 = 0; var4 < 8; ++var4) {
               if (var4 < var3.AC.length) {
                  var1.writeShort(var3.AC[var4]);
               } else {
                  var1.writeShort(-1);
               }
            }
         }

         GlobalService.getInstance().Save_RMS_Server((byte)1, (byte)6, (byte[])var0.toByteArray());
         var1.close();
      } catch (Exception var5) {
      }
   }

   private static void AB(byte[] var0) {
      if (var0 != null) {
         try {
            ByteArrayInputStream var6 = new ByteArrayInputStream(var0);
            DataInputStream var7;
            byte var1 = (var7 = new DataInputStream(var6)).readByte();

            for(int var2 = 0; var2 < var1; ++var2) {
               Class_FA var3;
               (var3 = new Class_FA()).AA = var7.readByte();
               var3.AB = var7.readByte();
               var3.AC = new short[8];

               for(int var4 = 0; var4 < 8; ++var4) {
                  var3.AC[var4] = var7.readShort();
               }

               Player.QK.addElement(var3);
            }

            var7.close();
            Class_FA.AA(false);
         } catch (Exception var5) {
         }
      }
   }

   public static void AH() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeByte((byte)(Player.isGetItem ? 1 : 0));
         if (Player.isGetItem) {
            for(int var2 = 0; var2 < Class_AH.AA.length; ++var2) {
               var1.writeByte((byte)Class_AH.AA[var2]);
            }
         }

         GlobalService.getInstance().Save_RMS_Server((byte)1, (byte)2, (byte[])var0.toByteArray());
         var1.close();
      } catch (Exception var3) {
      }
   }

   public static void AI() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeByte((byte)(GameScreen.isShowSkillBuff ? 1 : 0));
         GlobalService.getInstance().Save_RMS_Server((byte)1, (byte)7, (byte[])var0.toByteArray());
         var1.close();
      } catch (Exception var2) {
      }
   }

   private static void AC(byte[] var0) {
      if (var0 != null) {
         try {
            ByteArrayInputStream var3 = new ByteArrayInputStream(var0);
            DataInputStream var4;
            if (Player.isGetItem = (var4 = new DataInputStream(var3)).readByte() == 1) {
               for(int var1 = 0; var1 < Class_AH.AA.length; ++var1) {
                  Class_AH.AA[var1] = var4.readByte();
               }
            }

            var4.close();
         } catch (Exception var2) {
         }
      }
   }

   public static void AJ() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeShort((short)GameScreen.CV);
         GlobalService.getInstance().Save_RMS_Server((byte)1, (byte)4, (byte[])var0.toByteArray());
         var1.close();
      } catch (Exception var2) {
      }
   }

   public static void AK() {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();
      DataOutputStream var1 = new DataOutputStream(var0);

      try {
         var1.writeByte(Player.typeAutoFireMain);
         if (MsgAutoFire.value == null) {
            var1.writeByte(0);
         } else {
            var1.writeByte((byte)MsgAutoFire.value.length);

            for(int var2 = 0; var2 < MsgAutoFire.value.length; ++var2) {
               var1.writeShort(MsgAutoFire.value[var2][0]);
               var1.writeByte((byte)MsgAutoFire.value[var2][1]);
            }
         }

         var1.writeByte(Player.AutoRevice);
         GlobalService.getInstance().Save_RMS_Server((byte)1, (byte)5, (byte[])var0.toByteArray());
         var1.close();
      } catch (Exception var3) {
      }
   }

   private static void AD(byte[] var0) {
      if (var0 != null) {
         try {
            ByteArrayInputStream var4 = new ByteArrayInputStream(var0);
            DataInputStream var5;
            if ((Player.typeAutoFireMain = (var5 = new DataInputStream(var4)).readByte()) == -1) {
               Player.AutoFireCur = -1;
            }

            Player.typeAutoBuff = 0;
            byte var1;
            if ((var1 = var5.readByte()) > 0) {
               MsgAutoFire.value = new short[var1][];

               for(int var2 = 0; var2 < var1; ++var2) {
                  MsgAutoFire.value[var2] = new short[2];
                  MsgAutoFire.value[var2][0] = var5.readShort();
                  MsgAutoFire.value[var2][1] = var5.readByte();
                  if (MsgAutoFire.value[var2][1] == 1) {
                     Player.typeAutoBuff = 1;
                  }
               }
            }

            Player.AutoRevice = var5.readByte();
            var5.close();
         } catch (Exception var3) {
         }

         if (Player.AutoRevice == 1) {
            Interface_Game.addInfoPlayerNormal(T.MH, mFont.tahoma_7_yellow);
         }

      }
   }
}
