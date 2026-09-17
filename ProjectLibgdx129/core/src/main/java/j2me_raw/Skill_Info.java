public final class Skill_Info extends MainItem {
   public byte typeSkill;
   public byte typeBuff;
   public byte typeDevil;
   public short typeEffSkill;
   public short perEffSpec;
   public short timeEffSpec;
   public short indexSkillInServer;
   public byte idEffSpec = 0;
   public byte nTarget;
   public short range;
   public short manaLost;
   public short rangeLan;
   public short percentLv;
   public static byte maxLv = 30;
   public int timeDelay;
   public int damage = 0;
   public mVector vecAtt = new mVector("Skill_Info.vecAtt");
   private static Plash plashTest;

   public Skill_Info(short index, short Id, short IdImage, byte type, byte typeBuff, String name, short typeEff, short range, short var9) {
      this.indexSkillInServer = index;
      super.ID = Id;
      super.idIcon = IdImage;
      this.typeSkill = type;
      this.typeBuff = typeBuff;
      super.name = name;
      this.range = range;
      this.typeEffSkill = typeEff;
      super.Lv_RQ = 0;
      super.indexSort = this.typeSkill;
      if (super.indexSort == 4) {
         super.indexSort = 1;
      }

      int width;
      if ((width = mFont.tahoma_7b_black.getWidth(name)) > 120) {
         super.BS = width + 8;
      }

      if (super.Lv_RQ > 0) {
         if (super.Lv_RQ == 20) {
            super.namepaint = name + " + " + T.max;
         } else {
            super.namepaint = name + " +" + super.LvUpgrade;
         }
      } else {
         super.namepaint = name;
      }

      plashTest = new Plash();
      Plash.getPlashData(plashTest.getTypePlash(this.typeEffSkill));
      plashTest = null;
   }

   public final void getData(byte nTarget, short rangeLan, int Damage, short Manacost, int CoolDown, byte var6, String Description, byte LvCur, short percentLv, byte typeDevil) {
      this.nTarget = nTarget;
      this.rangeLan = rangeLan;
      this.damage = Damage;
      this.manaLost = Manacost;
      this.timeDelay = CoolDown;
      super.info = Description;
      super.Lv_RQ = LvCur;
      this.percentLv = percentLv;
      this.typeDevil = typeDevil;
   }

   public final void setVecInfo(int w) {
      super.vecInfo.removeAllElements();
      String[] array = mFont.tahoma_7b_black.splitFontArray(super.info, w);

      int i;
      for(i = 0; i < array.length; ++i) {
         this.addInfo((String)array[i], (byte)0);
      }

      if (super.LvDevilSkill > 0 || super.phanTramDevilSkill > 0) {
         this.addInfo((String)(T.lvDevil + super.LvDevilSkill + " + " + super.phanTramDevilSkill + "%"), (byte)5);
      }

      if (this.typeSkill == 2) {
         this.addInfo((String)(T.typeSkill + ": " + T.mTypeSkill[this.typeSkill]), (byte)0);
         this.addInfo((String)(T.tacdung + ": " + T.mTacdung[this.typeBuff]), (byte)0);
      }

      int num2;
      if (this.typeSkill != 3 && this.typeSkill != 6) {
         if (this.typeSkill != 2) {
            this.addInfo((String)(T.DD + ": " + this.damage), (byte)0);
         }

         this.addInfo((String)(T.DE + ": " + this.manaLost), (byte)0);
         this.addInfo((String)(T.DF + ": " + this.nTarget), (byte)0);
         if (this.nTarget > 1) {
            this.addInfo((String)(T.DG + ": " + this.rangeLan), (byte)0);
         }

         StringBuffer var10001 = (new StringBuffer(String.valueOf(T.DH))).append(": ");
         num2 = this.timeDelay;
         this.addInfo((String)var10001.append(num2 / 1000 + "," + num2 % 1000 / 100 + "s").toString(), (byte)0);
      }

      i = 0;
      num2 = 0;
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;

      for(int var7 = 0; var7 < this.vecAtt.size(); ++var7) {
         MainInfoItem var8;
         if ((var8 = (MainInfoItem)this.vecAtt.elementAt(var7)).AA >= 28 && var8.AA < 32) {
            if (var8.AA == 28) {
               var4 = var8.AE;
            } else if (var8.AA == 29) {
               num2 = var8.AE;
            } else if (var8.AA == 30) {
               i = var8.AE;
            }
         } else if (var8.AA >= 64 && var8.AA <= 65) {
            if (var8.AA == 64) {
               var6 = var8.AE;
            } else if (var8.AA == 65) {
               var5 = var8.AE;
            }
         } else {
            this.AA((short)var8.AA, var8.AE, (byte)var8.AD);
         }
      }

      String var10;
      if (i > 0) {
         var10 = "null";
         if (var4 >= 0 && var4 <= T.mEffSpec.length) {
            var10 = T.mEffSpec[var4];
         }

         String var11 = MainItem.AA(num2, (byte)1) + " " + T.EQ + " " + var10 + " " + T.ER + " " + MainItem.AA(i, (byte)10);
         array = mFont.tahoma_7b_black.splitFontArray(var11, w);

         for(i = 0; i < array.length; ++i) {
            this.addInfo((String)array[i], (byte)0);
         }
      }

      if (var6 > 0) {
         var10 = MainItem.AA(var5, (byte)1) + " " + T.UQ + " " + MainItem.AA(var6, (byte)1);
         String[] var12 = mFont.tahoma_7b_black.splitFontArray(var10, w);

         for(num2 = 0; num2 < var12.length; ++num2) {
            this.addInfo((String)var12[num2], (byte)0);
         }
      }

   }

   public static MainImage getImage(short var0) {
      MainImage var1 = null;

      try {
         var1 = ObjectData.getImageAll(var0, ObjectData.hashImageSkill, (short)4000);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return var1;
   }

   public final mVector getActionInven() {
      mVector mVector = new mVector();
      if (super.Lv_RQ >= 0 && (this.typeSkill == 1 || this.typeSkill == 2 || this.typeSkill == 4)) {
         mVector.addElement(TabSkill.cmdSetHotKey);
      }

      return mVector;
   }

   public final void paint(mGraphics g, int x, int y) {
      MainImage var4;
      if ((var4 = getImage(super.idIcon)) != null && var4.img != null) {
         g.drawRegion((mImage)var4.img, x, y, 3);
         if (super.Lv_RQ > GameScreen.player.Lv) {
            g.drawRegion(AvMain.imgDelay, 0, 0, 20, 20, 0, x, y, 3);
         }
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, x, y, 0, 3, g);
      }

      if (super.LvDevilSkill > 0 && super.LvDevilSkill - 1 < AvMain.fraBorderSkill.nFrame) {
         AvMain.fraBorderSkill.drawFrame(super.LvDevilSkill - 1, x, y, 0, 3, g);
      }

   }

   public static void paintIcon(mGraphics g, int x, int y, short icon, byte lvDevil) {
      MainImage image;
      if ((image = getImage(icon)) != null && image.img != null) {
         g.drawRegion((mImage)image.img, x, y, 3);
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, x, y, 0, 3, g);
      }

      if (lvDevil > 0 && lvDevil - 1 < AvMain.fraBorderSkill.nFrame) {
         AvMain.fraBorderSkill.drawFrame(lvDevil - 1, x, y, 0, 3, g);
      }

   }

   public static Skill_Info getSkillFromID(short Id) {
      try {
         for(int var1 = 0; var1 < Player.vecListSkill.size(); ++var1) {
            Skill_Info skill_Info;
            if ((skill_Info = (Skill_Info)Player.vecListSkill.elementAt(var1)).ID == Id) {
               return skill_Info;
            }
         }
      } catch (Exception e) {
      }
      return null;
   }

   public final String getStrType() {
      switch(this.typeSkill) {
      case 1:
         return T.skillActive;
      case 2:
         return T.skillBuff;
      case 3:
         return T.skillPassive;
      case 4:
         return T.skillActiveSea;
      case 5:
      default:
         return "";
      case 6:
         return T.skillJob;
      }
   }
}
