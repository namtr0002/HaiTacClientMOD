import java.io.DataInputStream;

public final class Potion extends MainItem {
   public static mVector AA = new mVector("Potion.vecPotionDonotData");
   public static mVector CP = new mVector("Potion.vecPotionDonotInfo");
   public short CQ = 0;
   public boolean CR = false;
   public static MyHashTable CS = new MyHashTable();

   public Potion(short var1, short var2, String var3, String var4, short var5) {
      super.ID = var1;
      super.idIcon = var2;
      super.name = var3;
      super.namepaint = var3;
      super.info = var4;
      super.AJ = var5;
      super.typeObject = 4;
      super.CG = true;
      this.AA(var4);
      super.CF = true;
      if (var5 == 0) {
         super.BM = T.AS;
      } else {
         super.BM = T.BN;
      }
   }

   private Potion(short var1) {
      super.ID = var1;
   }

   public Potion(byte var1, short var2, short var3, boolean var4) {
      super.typeObject = var1;
      super.ID = var2;
      super.numPotion = var3;
      super.CG = var4;
   }

   public Potion(byte var1, short var2, short var3, String var4, byte var5) {
      super(var1, var2, var3, var4, var5);
      super.indexSort = 0;
   }

   public final void AA(int var1, short var2, short var3, short var4, short var5, byte var6, String var7) {
      super.AF = var1;
      super.AJ = var2;
      super.BV = var3;
      this.CQ = var4;
      super.BU = var5;
      super.BQ = var6;
      super.BM = var7;
      super.indexHotKey = (short)(500 + super.BQ);
      if (super.LvUpgrade > 0) {
         super.namepaint = super.name + " +" + super.LvUpgrade;
      } else {
         super.namepaint = super.name;
      }
   }

   private static Potion AA(byte var0, short var1, short var2, String var3, String var4, byte var5, int var6, short var7, byte var8, short var9, short var10, short var11, String var12) {
      Potion var13;
      (var13 = new Potion((byte)4, var1, var2, var3, var5)).info = var4;
      var13.AF = var6;
      var13.AJ = var7;
      var13.BQ = var8;
      var13.BU = var9;
      var13.CQ = var11;
      var13.BV = var10;
      var13.BM = var12;
      var13.CR = true;
      return var13;
   }

   public final mVector AA(byte var1) {
      mVector var2 = new mVector();
      if (var1 == 0) {
         if (super.CN) {
            GameCanvas.tabInven.CV.caption = T.cmdQuickOpen;
            var2.addElement(GameCanvas.tabInven.CV);
         }

         if (super.BM.compareTo("null") != 0) {
            GameCanvas.tabInven.CD.caption = super.BM;
            var2.addElement(GameCanvas.tabInven.CD);
         }

         if (super.BQ == 1 || super.BQ == 2 || super.BQ == 40 || super.BQ == 84 || super.BQ == 100) {
            var2.addElement(GameCanvas.tabInven.CE);
         }

         if (super.BQ == 12) {
            var2.addElement(GameCanvas.tabInven.CM);
         } else {
            var2.addElement(GameCanvas.tabInven.CG);
         }
      } else if (var1 == 1) {
         var2.addElement(GameCanvas.tabInven.CN);
      } else if (var1 == 2) {
         var2.addElement(GameCanvas.tabInven.CJ);
         if (super.ID >= 44 && super.ID <= 79) {
            var2.addElement(GameCanvas.tabInven.CL);
         }
      } else if (var1 == 4) {
         var2.addElement(GameCanvas.CO.CD);
      } else if (var1 == 5) {
         if ((super.ID < 310 || super.ID > 315) && super.ID != 339) {
            boolean var10000;
            label66: {
               Potion var4 = this;
               if (MainItem.CL != null) {
                  for(int var3 = 0; var3 < MainItem.CL.length; ++var3) {
                     if (var4.ID == MainItem.CL[var3]) {
                        var10000 = true;
                        break label66;
                     }
                  }
               }

               var10000 = false;
            }

            if (!var10000) {
               return var2;
            }
         }

         var2.addElement(GameCanvas.CP.CO);
      }

      return var2;
   }

   public final mVector AB(byte var1) {
      mVector var2 = new mVector();
      if (var1 == 101) {
         var2.addElement(Class_BH.CG);
         var2.addElement(Class_BH.CF);
      } else if (super.CF) {
         var2.addElement(Class_BH.CK);
      } else {
         var2.addElement(Class_BH.CE);
      }

      return var2;
   }

   public final mVector getActionChest() {
      mVector var1;
      (var1 = new mVector()).addElement(TabChest.cmdGetPotion);
      if (super.ID >= 44 && super.ID <= 79) {
         var1.addElement(TabChest.cmdChucnang);
      } else {
         var1.addElement(TabChest.cmdUpgrade);
      }

      return var1;
   }

   public final mVector AC() {
      mVector var1 = new mVector();
      if (SplitScreen.instance != null && SplitScreen.instance.AS != null && (SplitScreen.instance.BG == 1 || SplitScreen.instance.BG == 4 || SplitScreen.instance.BG == 8 || SplitScreen.instance.BG == 10 || SplitScreen.instance.BG == 11 || SplitScreen.instance.BG == 13 || SplitScreen.instance.BG == 14 || SplitScreen.instance.BG == 2 && (super.ID == 323 || super.ID == 339) || SplitScreen.instance.BG == 19 && super.ID == 457 || SplitScreen.instance.BG == 21)) {
         var1.addElement(SplitScreen.instance.AS);
      }

      return var1;
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4) {
      if (super.CF) {
         MainImage var5;
         if ((var5 = getIconClan(super.idIcon)) != null && var5.img != null) {
            this.AA(var1, var5, var2, var3);
         } else {
            AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, var2, var3, 0, 3, var1);
         }
      } else {
         super.AA(var1, var2, var3, var4);
         this.AE(var1, var2, var3, var4);
         if (super.idIcon >= 210 && super.idIcon <= 239 || super.idIcon >= 319 && super.idIcon <= 324) {
            AvMain.fraEffDasieucap.drawFrame(GameCanvas.BJ % AvMain.fraEffDasieucap.nFrame, var2, var3, 0, 3, var1);
         }

      }
   }

   public final void AD(mGraphics var1, int var2, int var3, int var4) {
      if (super.CF) {
         MainImage var9;
         if ((var9 = getIconClan(super.idIcon)) != null && var9.img != null) {
            this.AA(var1, var9, var2, var3);
         } else {
            AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, var2, var3, 0, 3, var1);
         }
      } else {
         super.AA(var1, var2, var3, (int)32);
         short var8 = super.numPotion;
         if (super.AS > 0) {
            var1.drawRegion((mImage)AvMain.imgBgnum, var2 + MainTab.AE / 2 - 11, var3 + MainTab.AE / 2 - 6, 3);
            mFont var10 = mFont.tahoma_7_yellow;
            if (super.AS > super.numPotion) {
               var10 = mFont.tahoma_7_red;
            }

            var10.drawString(var1, super.numPotion + "/" + super.AS, var2 + MainTab.AE / 2 - 11, var3 + MainTab.AE / 2 - 9 - 2, 2);
         } else {
            var1.drawRegion((mImage)AvMain.imgBgnum, var2 + MainTab.AE / 2 - 11, var3 + MainTab.AE / 2 - 6, 3);
            mFont.tahoma_7_yellow.drawString(var1, "" + var8, var2 + MainTab.AE / 2 - 11, var3 + MainTab.AE / 2 - 9 - 2, 2);
         }

         if (super.idIcon >= 210 && super.idIcon <= 239 || super.idIcon >= 319 && super.idIcon <= 324) {
            AvMain.fraEffDasieucap.drawFrame(GameCanvas.BJ % AvMain.fraEffDasieucap.nFrame, var2, var3, 0, 3, var1);
         }

      }
   }

   public static MainImage getIconClan(short var0) {
      return var0 == -1 ? null : ObjectData.getImageAll(var0, ObjectData.hashImageIconClan, (short)7000);
   }

   public static MainImage AC(short var0) {
      return var0 == -1 ? null : ObjectData.getImageAll(var0, ObjectData.hashImageIconClanBig, (short)22000);
   }

   public static Potion AD(short var0) {
      Potion var1;
      if ((var1 = (Potion)MainItem.CA.get("" + var0)) == null) {
         var1 = new Potion(var0);
         MainItem.CA.put("" + var0, var1);
         GlobalService.getInstance().AH((byte)4, (short)var0);
      }

      return var1;
   }

   public final MainImage AD() {
      MainImage var1 = null;

      try {
         if (super.idIcon == -1) {
            return null;
         }

         var1 = ObjectData.getImageAll(super.idIcon, ObjectData.hashImagePotion, (short)2000);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return var1;
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4, int var5) {
      super.AA(var1, var2, var3, var4);
      if (super.numPotion >= 0) {
         var1.drawRegion((mImage)AvMain.imgBgnum, var2, var3 + 18 + var5, 3);
         mFont.tahoma_7_yellow.drawString(var1, "" + super.numPotion, var2, var3 + 13 + var5, 2);
      }

   }

   public final void Use_Item() {
      if (super.typeObject == 8) {
         GlobalService.getInstance().Clan_CMD((byte)14, "", super.ID, super.typeObject);
      } else if (super.BQ == 40 && DelaySkill.getDelay(super.indexHotKey).isCoolDown()) {
         short var1;
         if ((var1 = GameScreen.AL()) == -1) {
            GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.QU);
         } else {
            GlobalService.getInstance().AD(super.ID, var1);
         }

         Player.AA(super.indexHotKey, super.BU, true, (byte)0);
      } else {
         if ((GameScreen.player.Action != 4 || super.BQ != 1 && super.BQ != 2) && DelaySkill.getDelay(super.indexHotKey).isCoolDown()) {
            if (super.BQ == 1 && GameScreen.player.Hp == GameScreen.player.maxHp) {
               Interface_Game.addInfoPlayerNormal(T.HU, mFont.tahoma_7_white);
               return;
            }

            if (super.BQ == 2 && GameScreen.player.Mp == GameScreen.player.maxMp) {
               Interface_Game.addInfoPlayerNormal(T.HV, mFont.tahoma_7_white);
               return;
            }

            GlobalService.getInstance().Use_Potion(super.ID);
            if (super.BQ == 3 || super.BQ == 4) {
               MsgShowGift.AA = this;
            }

            Player.AA(super.indexHotKey, super.BU, true, (byte)0);
         }

      }
   }

   public static void UpdateDataPotion(DataInputStream var0, boolean var1, byte var2) {
      if (var0 == null) {
         if (var2 == 4) {
            GlobalService.getInstance().get_DATA((byte)28);
         }

         if (var2 == 8) {
            GlobalService.getInstance().get_DATA((byte)29);
         }

      } else {
         try {
            short var3 = var0.readShort();

            for(int var4 = 0; var4 < var3; ++var4) {
               short var5 = var0.readShort();
               short var6 = var0.readShort();
               String var7 = var0.readUTF();
               short var8 = -1;
               String var9 = "";
               if (var2 == 4) {
                  var8 = var0.readShort();
               } else {
                  var9 = var0.readUTF();
               }

               int var10 = var0.readInt();
               short var11 = var0.readShort();
               byte var12 = var0.readByte();
               byte var13 = var0.readByte();
               short var14 = var0.readShort();
               short var15 = var0.readShort();
               short var16 = var0.readShort();
               String var17 = var0.readUTF();
               Potion var19;
               (var19 = AA((byte)4, var5, var6, var7, var9, var12, var10, var11, var13, var14, var15, var16, var17)).AP = var8;
               if (var2 == 4) {
                  MainItem.CA.put("" + var5, var19);
               }

               if (var2 == 8) {
                  MainItem.CC.put("" + var5, var19);
               }
            }

            if (var1) {
               if (var2 == 4) {
                  SaveRms.AA(GlobalService.AE = var0.readShort(), "VerdataPotion");
               }

               if (var2 == 8) {
                  SaveRms.AA(GlobalService.AM = var0.readShort(), "VerdataPotionClan");
               }
            }

            var0.close();
         } catch (Exception var18) {
            var18.printStackTrace();
         }
      }
   }

   public static void AA(DataInputStream var0, byte var1) {
      try {
         short var15 = var0.readShort();
         short var2 = var0.readShort();
         String var3 = var0.readUTF();
         boolean var4 = false;
         String var5 = "";
         short var17 = var0.readShort();
         int var6 = var0.readInt();
         short var7 = var0.readShort();
         byte var8 = var0.readByte();
         byte var9 = var0.readByte();
         short var10 = var0.readShort();
         short var11 = var0.readShort();
         short var12 = var0.readShort();
         String var13 = var0.readUTF();
         Potion var16;
         (var16 = AA((byte)4, var15, var2, var3, var5, var8, var6, var7, var9, var10, var11, var12, var13)).AP = var17;
         var16.CR = true;
         MainItem.CA.put("" + var15, var16);
         var0.close();
      } catch (Exception var14) {
         var14.printStackTrace();
      }
   }

   public static void AE() {
      for(int var0 = 0; var0 < AA.size(); ++var0) {
         Potion var1 = (Potion)AA.elementAt(var0);
         Potion var2;
         if ((var2 = (Potion)MainItem.CA.get("" + var1.ID)).CR) {
            var1.idIcon = var2.idIcon;
            var1.name = var2.name;
            var1.BO = var2.BO;
            var1.AA(var2.AF, var2.AJ, var2.BV, var2.CQ, var2.BU, var2.BQ, var2.BM);
            if (!var1.AA(var2.AP)) {
               CP.addElement(var1);
            }

            AA.removeElement(var0);
            --var0;
         }
      }

   }

   public static void AA(short var0, String var1) {
      for(int var2 = 0; var2 < CP.size(); ++var2) {
         Potion var3;
         if ((var3 = (Potion)CP.elementAt(var2)).AP == var0) {
            var3.info = var1;
            var3.AA(var1);
            CP.removeElement(var3);
            --var2;
         }
      }

   }
}
