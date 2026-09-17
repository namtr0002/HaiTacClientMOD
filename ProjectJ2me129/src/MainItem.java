import java.io.DataInputStream;

public class MainItem {
    public final void paintColor(mGraphics g, int x, int y, int size) {
        this.AC(g, x, y, size);
    }
    public static int getColorName(byte color) {
        switch (color) {
            case 1: return 0x00FF00;
            case 2: return 0x0088FF;
            case 3: return 0xAA00FF;
            case 4: return 0xFF8800;
            case 5: return 0xFF0000;
            default: return 0xFFFFFF;
        }
    }
    public static String strGetPercent(short percent, int type) {
        return "" + percent + "%";
    }

   public int AB = 1;
   public int AC;
   public int AD;
   public int timeUse;
   public int AF;
   public int AG;
   public short ID;
   public short idIcon = -1;
   public short idPart = -1;
   public short AJ;
   public short Lv_RQ;
   public short numPotion = 1;
   public short indexHotKey;
   public short AN;
   public short maxTimeUse;
   public short AP;
   public short valueCheTac;
   public short AR;
   public short AS = 0;
   public byte indexSort = 0;
   public byte typeMaterial;
   public byte LvUpgrade = 0;
   public byte AW;
   public byte numLoKham;
   public byte AY;
   public byte AZ;
   public byte LvDevilSkill;
   public byte phanTramDevilSkill;
   public byte BC = -1;
   public byte isHoanMy;
   public byte valueKickAn;
   public byte typeSpec;
   public byte BG = 0;
   public short[] mDaKham;
   public short[] BI;
   public String name;
   public String namepaint = "";
   public String info;
   public String BM;
   public mVector vecInfo = new mVector("MainItem.vecInfo");
   public byte BO;
   public byte charClass;
   public byte BQ;
   public byte typeEquip;
   public int BS = 140;
   public int BT = 40;
   public short BU;
   public short BV = 0;
   public byte colorName;
   public byte typeObject;
   public byte BY;
   public static MainInfoItem[] BZ;
   public static MainInfoItem[] mNameAttributes = BZ;
   public static String[] DEFAULT_ATTR_NAMES = new String[] {
      "Tấn công", "Phòng thủ", "Chí mạng", "Xuyên giáp", "Né tránh",
      "Chính xác", "Máu", "Năng lượng", "Tốc độ", "Kháng tất cả"
   };
   public static byte[] DEFAULT_ATTR_PERCENT = new byte[10];

   public static void checkLoadDataAttri() {
      if (BZ == null) {
         GlobalService.getInstance().get_DATA((byte)2);
      }
   }
   public static MyHashTable CA = new MyHashTable();
   public static MyHashTable hashMaterialTem = new MyHashTable();
   public static MyHashTable CC = new MyHashTable();
   public static MyHashTable CD = new MyHashTable();
   public boolean CE = false;
   public boolean CF = false;
   public boolean CG = false;
   public boolean CH = true;
   public static mImage imgColorItem;
   private FrameImage AA = null;
   private boolean CP = false;
   private static Class_BS CQ = new Class_BS();
   public static Class_BS CJ = new Class_BS();
   private static Class_BS CR = new Class_BS();
   public CountDownTicket marketTime = new CountDownTicket();
   public static short[] CL;
   public static short[] CM;
   public boolean CN = false;
   public int CO = 0;
   private int CS = 0;
   private int CT = 0;
   private static int[] CU = new int[]{0, 10, 20, 30, 45, 60, 75, 90, 110, 130, 150, 150, 150, 150, 150, 150, 150};

   public MainItem() {
   }

   public MainItem(byte var1, short var2, short var3, int var4) {
      this.typeObject = var1;
      this.idIcon = var2;
      this.ID = var3;
      this.AB = var4;
      this.numPotion = 0;
   }

   public MainItem(byte var1, short var2, short var3) {
      this.typeObject = var1;
      this.idIcon = var2;
      this.ID = var3;
      this.numPotion = 0;
   }

   public MainItem(byte var1, short var2, short var3, String var4, byte var5) {
      this.typeObject = var1;
      this.ID = var2;
      this.idIcon = var3;
      this.name = var4;
      this.BO = var5;
      this.namepaint = var4;
   }

   public MainItem(byte var1, short var2, short var3, short var4, byte var5, byte var6) {
      this.typeObject = var1;
      this.ID = var2;
      this.idIcon = var3;
      this.numPotion = var4;
      this.colorName = var5;
      this.LvUpgrade = var6;
      if (this.LvUpgrade > 0) {
         this.namepaint = this.name + " +" + this.LvUpgrade;
      } else {
         this.namepaint = this.name;
      }
   }
   
   public MainItem(byte type, short id, short idicon, String name, byte colorname, byte upgrade) {
      this.typeObject = type;
      this.ID = id;
      this.idIcon = idicon;
      this.colorName = colorname;
      this.LvUpgrade = upgrade;
      if (this.LvUpgrade > 0) {
         this.namepaint = this.name + " +" + this.LvUpgrade;
      } else {
         this.namepaint = this.name;
      }
   }

   public final void AA(String var1) {
      if (var1.length() != 0) {
         String[] var3 = mFont.tahoma_7_white.splitFontArray(var1, this.BS);

         for(int var2 = 0; var2 < var3.length; ++var2) {
            this.addInfo((String)var3[var2], (byte)0);
         }

         if (!this.CG || this.numPotion > 1) {
            this.addInfo((String)(T.soluong + ": " + this.numPotion), (byte)0);
         }

      }
   }

   public static MainInfoItem getNameAttribute(int id) {
      if (BZ != null && id >= 0 && id < BZ.length && BZ[id] != null) {
         return BZ[id];
      }
      return null;
   }

   public static String getAttributeNameSafe(int id) {
      MainInfoItem it = getNameAttribute(id);
      if (it != null && it.AF != null && it.AF.length() > 0) {
         return it.AF;
      }
      return getFallbackAttributeName(id);
   }

   public static byte getAttributePercentSafe(int id) {
      MainInfoItem it = getNameAttribute(id);
      if (it != null) {
         return it.AC;
      }
      return getFallbackAttributePercent(id);
   }

   public static byte getAttributeColorSafe(int id) {
      MainInfoItem it = getNameAttribute(id);
      if (it != null && it.AB != 0) {
         return it.AB;
      }
      return 4;
   }

   public static String AA(short var0, int var1, int var2, int var3) {
      String var4 = getAttributeNameSafe(var0) + " ";
      byte isPercent = getAttributePercentSafe(var0);
      var1 = AA(var1, var3, var2);
      return var4 + AA(var1, isPercent);
   }

   public final void AA(short var1, int var2, byte var3) {
      String var4 = getAttributeNameSafe(var1) + " ";
      byte isPercent = getAttributePercentSafe(var1);
      byte defaultColor = getAttributeColorSafe(var1);
      var4 = var4 + AA(var2, isPercent);
      int var5 = mFont.tahoma_7b_black.getWidth(var4) + 4;
      if (this.AY == 1) {
         var5 += 12;
      }

      if (this.charClass > 0) {
         var5 += 12;
      }

      if (this.LvUpgrade > 0) {
         var5 += 16;
      }

      if (var5 > this.BS) {
         this.BS = var5;
      }

      byte var6 = defaultColor;
      if (var3 >= 0 && var3 <= 8) {
         var6 = var3;
      }

      this.vecInfo.addElement(new infoShow(var1, var2, var6, var3));
      this.AE();
   }

   public final void addInfo(short var1, int var2, byte var3, byte var4) {
      String var5 = getAttributeNameSafe(var1) + " ";
      byte defaultColor = getAttributeColorSafe(var1);
      var5 = var5 + AA(var2, (byte)1);
      int var6 = mFont.tahoma_7b_black.getWidth(var5) + 4;
      if (this.AY == 1) {
         var6 += 12;
      }

      if (this.charClass > 0) {
         var6 += 12;
      }

      if (this.LvUpgrade > 0) {
         var6 += 16;
      }

      if (var6 > this.BS) {
         this.BS = var6;
      }

      var4 = defaultColor;
      if (var3 >= 0 && var3 <= 8) {
         var4 = var3;
      }

      this.vecInfo.addElement(new infoShow(var1, var2, var4, var3));
      this.AE();
   }

   public final void addInfo(String var1, byte var2) {
      this.vecInfo.addElement(new infoShow(-1, 0, var1, var2, (byte)-1));
      this.AE();
   }

   public void addInfo(String var1, byte var2, byte var3) {
      this.vecInfo.addElement(new infoShow(-1, 0, var1, var2, var3));
      this.AE();
   }

   public final void addInfoFrist(String var1, byte var2) {
      this.vecInfo.insertElementAt(new infoShow(-1, 0, var1, var2, (byte)-1), 0);
      this.AE();
   }

   private void AE() {
      this.BT = (this.vecInfo.size() + 1) * GameCanvas.hText;
      if (this.numLoKham > 0) {
         this.BT += 22;
      }

      if (this.isHoanMy == 1) {
         this.BT += 14;
      }

      if (this.BT > MainTab.AH - GameCanvas.hCommand * 3 / 2) {
         this.CO = this.BT - (MainTab.AH - GameCanvas.hCommand * 3 / 2);
      } else {
         this.CO = 0;
      }
   }

   public static String AA(int var0, byte var1) {
      String var2 = "";
      if (var1 == 0) {
         var2 = var2 + var0;
      } else if (var1 == 1) {
         var2 = var2 + var0 / 10 + "," + CRes.abs(var0) % 10 + "%";
      } else if (var1 == 2) {
         var2 = var2 + var0 / 100 + "," + CRes.abs(var0) % 100 + "%";
      } else if (var1 == 10) {
         var2 = var2 + var0 / 10 + "," + CRes.abs(var0) % 10 + "s";
      }

      return var2;
   }

   public final void AC(mGraphics var1, int var2, int var3, int var4) {
      if ((this.colorName << 5) + var4 <= mImage.getImageHeight(imgColorItem.image)) {
         var1.drawRegion(imgColorItem, 0, this.colorName << 5, var4, var4, 0, var2, var3, 3);
      }

   }

   public void paint(mGraphics var1, int var2, int var3, int var4) {
      MainImage var5;
      if ((var5 = this.getImage()) != null && var5.img != null) {
         this.AA(var1, var5, var2, var3);
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, var2, var3, 0, 3, var1);
      }

      this.AD(var1, var2, var3, var4, 0);
   }

   private void AA(mGraphics var1, int var2, int var3, short var4) {
      if (this.AS > 0) {
         var1.drawRegion((mImage)AvMain.imgBgnum, var2 + MainTab.AE / 2 - 11, var3 + MainTab.AE / 2 - 6, 3);
         mFont var5 = mFont.tahoma_7_yellow;
         if (this.AS > this.numPotion) {
            var5 = mFont.tahoma_7_red;
         }

         var5.drawString(var1, this.numPotion + "/" + this.AS, var2 + MainTab.AE / 2 - 11, var3 + MainTab.AE / 2 - 9 - 2, 2);
      } else {
         if (var4 > 1) {
            var1.drawRegion((mImage)AvMain.imgBgnum, var2 + MainTab.AE / 2 - 11, var3 + MainTab.AE / 2 - 6, 3);
            mFont.tahoma_7_yellow.drawString(var1, "" + var4, var2 + MainTab.AE / 2 - 11, var3 + MainTab.AE / 2 - 9 - 2, 2);
         }

      }
   }

   public void AD(mGraphics var1, int var2, int var3, int var4) {
   }

   public final void AE(mGraphics var1, int var2, int var3, int var4) {
      this.AA(var1, var2, var3, this.numPotion);
   }

   public final void AB(mGraphics var1, int var2, int var3, int var4, int var5) {
      MainImage var6;
      if ((var6 = this.getImage()) != null && var6.img != null) {
         this.AA(var1, var6, var2, var3);
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, var2, var3, 0, 3, var1);
      }

      this.AD(var1, var2, var3, var4, 1);
   }

   public final void AC(mGraphics var1, int var2, int var3, int var4, int var5) {
      MainImage var6;
      if ((var6 = this.getImage()) != null && var6.img != null) {
         this.AA(var1, var6, var2, var3);
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, var2, var3, 0, 3, var1);
      }

      if (this.LvUpgrade > 0) {
         CR.AA(var2, var3, this.LvUpgrade, var4 - 4, var1, 1, true);
      }

   }

   public final void AD(mGraphics var1, int var2, int var3, int var4, int var5) {
      if (this.BC >= 0 && this.BC <= 2) {
         AvMain.fraUniform.drawFrame(this.BC, var2 - var4 / 2 + 2, var3 + var4 / 2 - 9, 0, 0, var1);
      }

      if (this.AY == 1) {
         var1.drawRegion((mImage)AvMain.imgLock, var2 + var4 / 2 - 1 - 8, var3 - var4 / 2 + 2, 0);
      }

      int var6;
      if (this.LvUpgrade > 0) {
         var6 = this.LvUpgrade;
         if (this.typeSpec == 1) {
            if (this.LvUpgrade > 100) {
               var6 = 10;
            } else {
               var6 = this.LvUpgrade % 10;
            }
         }

         CQ.AA(var2, var3, var6, var4 - 4, var1, var5, true);
      }

      if (this.mDaKham != null) {
         for(var6 = 0; var6 < this.mDaKham.length; ++var6) {
            var5 = (this.mDaKham[var6] - 44) / 6;
            if (this.mDaKham[var6] >= 324 && this.mDaKham[var6] <= 326) {
               var5 = GameCanvas.gameTick / 5 % 6;
            } else if (this.mDaKham[var6] >= 241 && this.mDaKham[var6] <= 270) {
               var5 = (this.mDaKham[var6] - 241) / 5;
            } else if (this.mDaKham[var6] >= 368 && this.mDaKham[var6] <= 373 || this.mDaKham[var6] >= 362 && this.mDaKham[var6] <= 367 || this.mDaKham[var6] >= 647 && this.mDaKham[var6] <= 682) {
               var5 = 6;
            }

            int var7 = var2 - var4 / 2 + 5 + var6 / 2 * 9;
            int var8 = var3 - var4 / 2 + 5;
            if (var6 % 2 == 1) {
               var7 = var2 + var4 / 2 - 5 - var6 / 2 * 9;
               var8 = var3 + var4 / 2 - 5;
            }

            if (var5 >= 0 && var5 < AvMain.fraEffItem.nFrame) {
               if ((this.mDaKham[var6] < 241 || this.mDaKham[var6] > 270) && (this.mDaKham[var6] < 368 || this.mDaKham[var6] > 373) && (this.mDaKham[var6] < 647 || this.mDaKham[var6] > 682)) {
                  AvMain.fraEffItem.drawFrame((var5 << 1) + GameCanvas.gameTick / 5 % 2, var7, var8, 0, 3, var1);
               } else {
                  AvMain.fraEffItem2.drawFrame((var5 << 1) + GameCanvas.gameTick / 5 % 2, var7, var8, 0, 3, var1);
               }
            }
         }
      }

   }

   public final MainImage AF() {
      MainImage var1 = null;
      if (this.typeObject == 3) {
         var1 = ObjectData.getImageAll(this.idIcon, ObjectData.hashImageItem, (short)3000);
      }

      if (this.typeObject == 7) {
         var1 = ObjectData.getImageAll(this.idIcon, ObjectData.hashImageMaterialPotion, (short)6500);
      }

      if (this.typeObject == 4) {
         var1 = ObjectData.getImageAll(this.idIcon, ObjectData.hashImagePotion, (short)2000);
      }

      if (this.typeObject == 100) {
         var1 = ObjectData.getImageAll(this.idIcon, ObjectData.hashImageItemOther, (short)9000);
      }

      if (this.typeObject == 104) {
         var1 = ObjectData.getImageAll(this.idIcon, ObjectData.hashImageSkill, (short)4000);
      }

      if (this.typeObject == 105) {
         var1 = ObjectData.getImageAll(this.idIcon, ObjectData.HashImageFashion, (short)20000);
      }

      if (this.typeObject == 110) {
         var1 = ObjectData.getImageAll(this.idIcon, ObjectData.HashImageOtherNew, (short)23000);
      }

      return var1;
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4, int var5, byte var6, short var7) {
      this.AA(var1, var2, var3, var4, 0, var6, var7, true);
   }

   public final void AA(mGraphics var1, int var2, int var3, int var4, int var5, byte var6) {
      this.AA(var1, var2, var3, var4, 0, var6, this.numPotion, true);
   }

   private void AA(mGraphics var1, int var2, int var3, int var4, int var5, byte var6, short var7, boolean var8) {
      MainImage var9;
      if ((var9 = this.AF()) != null && var9.img != null) {
         this.AA(var1, var9, var2, var3);
      } else {
         AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, var2, var3, 0, 3, var1);
      }

      if (this.typeObject == 3) {
         AvMain.AB(var6).drawString(var1, "Lv." + this.LvUpgrade, var2 + MainTab.AE / 2 - 2, var3 + MainTab.AE / 2 - 9 - 2, 1);
      } else {
         this.AA(var1, var2, var3, var7);
      }

      this.AD(var1, var2, var3, var4, var5);
   }

   public final void AA(mGraphics var1, MainImage var2, int var3, int var4) {
      if (!this.CP && var2 != null && var2.img != null) {
         int var7 = mImage.getImageWidth(var2.img.image);
         if (mImage.getImageHeight(var2.img.image) / 2 >= var7) {
            this.AA = new FrameImage(var2.img, var7, var7);
         }

         this.CP = true;
      }

      if (this.AA != null) {
         byte var9;
         if (this.CT >= this.AA.nFrame - 1) {
            var9 = 15;
         } else {
            var9 = 3;
         }

         if (CRes.abs(GameCanvas.gameTick - this.CS) > var9) {
            ++this.CT;
            if (this.CT >= this.AA.nFrame) {
               this.CT = 0;
            }

            this.CS = GameCanvas.gameTick;
         }

         this.AA.drawFrame(this.CT > this.AA.nFrame - 1 ? 0 : this.CT, var3, var4, 0, 3, var1);
      } else {
         var1.drawRegion((mImage)var2.img, var3, var4, 3);
      }
   }

   public void AA(mGraphics var1, int var2, int var3, int var4, int var5) {
      this.AA(var1, var2, var3, var4, 0, (byte)5);
   }

   public MainImage getImage() {
      return this.AF();
   }

   public static void removeUpdateItemVec(byte var0, mVector var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         MainItem var3;
         if ((var3 = (MainItem)var1.elementAt(var2)).typeObject == var0) {
            var1.removeElement(var3);
            --var2;
         }
      }

   }

   public static MainItem getItemVec(byte var0, short var1, mVector var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         MainItem var4;
         if ((var4 = (MainItem)var2.elementAt(var3)).typeObject == var0 && var4.ID == var1) {
            return var4;
         }
      }

      return null;
   }

   public void Use_Item() {
   }

   public mVector getActionInven(byte var1) {
      if (this.typeObject == 110) {
         mVector var2 = new mVector();
         // Hiển thị nút dựa trên trạng thái pet
         if (this.colorName == 1) {
            // Pet đang được sử dụng -> hiện nút "Tháo"
            var2.addElement(GameCanvas.tabInvenClan.CP); // CP = Tháo
         } else {
            // Pet chưa được sử dụng -> hiện nút "Sử dụng"  
            var2.addElement(GameCanvas.tabInvenClan.CD); // CD = Sử dụng
         }
         return var2;
      } else {
         return null;
      }
   }

   public mVector getActionShop(byte var1) {
      mVector var2;
      (var2 = new mVector()).addElement(TabShop.CE);
      return var2;
   }

   public mVector getActionChest() {
      return null;
   }

   public mVector getActionUpgrade() {
      // Pet items no longer have upgrade actions
      return null;
   }

   public mVector getActionSplit() {
      return null;
   }

   public static mVector AH() {
      mVector var0 = new mVector();
      if (TradeScreen.instance != null) {
         var0.addElement(TradeScreen.instance.AS);
      }

      return var0;
   }

   public static String getFallbackAttributeName(int id) {
      switch (id) {
      case 0: return "Tấn công";
      case 1: return "Tấn công";
      case 2: return "Tấn công phép";
      case 3: return "Sát thương";
      case 4: return "Phòng thủ";
      case 5: return "Sức mạnh";
      case 6: return "Phòng thủ";
      case 7: return "Thể lực";
      case 8: return "Tinh thần";
      case 9: return "Nhanh nhẹn";
      case 10: return "Chí mạng";
      case 11: return "Chí mạng phép";
      case 12: return "Né tránh";
      case 13: return "Xuyên giáp";
      case 14: return "Xuyên kháng phép";
      case 15: return "Máu";
      case 16: return "Năng lượng";
      case 17: return "Chính xác";
      case 18: return "Hồi máu";
      case 19: return "Hồi năng lượng";
      case 20: return "Tốc độ chạy";
      case 21: return "Kháng tất cả";
      case 22: return "Kháng vật lý";
      case 23: return "Hồi máu từ bình";
      case 24: return "Hồi năng lượng từ bình";
      case 25: return "Giảm hồi chiêu";
      case 26: return "Kháng vật lý";
      case 27: return "Kháng phép";
      case 28: return "Phản đòn";
      case 29: return "Hút máu";
      case 30: return "Hút năng lượng";
      case 31: return "Sát thương chí mạng";
      case 32: return "Kháng chí mạng";
      case 33: return "Kháng choáng";
      case 34: return "Kháng đóng băng";
      case 35: return "Kháng trúng độc";
      case 36: return "Kháng tê liệt";
      case 37: return "Kháng bỏng";
      case 38: return "Tăng kinh nghiệm";
      case 39: return "Tăng beri";
      case 40: return "May mắn";
      case 41: return "Kháng mù";
      case 42: return "Kháng trói";
      case 43: return "Kháng hóa đá";
      case 44: return "Xuyên né tránh";
      case 45: return "Chính xác tuyệt đối";
      case 46: return "Sát thương cuối";
      case 47: return "Giảm hiệu ứng";
      case 48: return "Hồi phục sau chiến đấu";
      case 49: return "Sức mạnh bộc phát";
      case 50: return "Miễn nhiễm sát thương";
      case 51: return "Bỏ qua phòng thủ";
      case 52: return "Bỏ qua kháng phép";
      case 53: return "Bỏ qua sát thương";
      case 54: return "Hút sinh lực đối thủ";
      case 55: return "Kháng bạo kích";
      case 56: return "Tăng máu (%)";
      case 57: return "Tăng năng lượng (%)";
      case 58: return "Tăng tấn công (%)";
      case 59: return "Tăng phòng thủ (%)";
      case 60: return "Giảm sức mạnh đối thủ";
      default: return "Chỉ số " + id;
      }
   }

   public static byte getFallbackAttributePercent(int id) {
      switch (id) {
      case 10:
      case 11:
      case 12:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
      case 46:
      case 47:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
         return 1;
      default:
         return 0;
      }
   }

   public static String AA(MainInfoItem var0) {
      if (var0 == null) {
         return "";
      }
      String name = null;
      byte ispercent = 0;
      if (BZ != null && var0.AA >= 0 && var0.AA < BZ.length && BZ[var0.AA] != null) {
         name = BZ[var0.AA].AF;
         ispercent = BZ[var0.AA].AC;
      }
      if (name == null || name.length() == 0 || name.equals("null")) {
         name = getFallbackAttributeName(var0.AA);
         ispercent = getFallbackAttributePercent(var0.AA);
      }
      return name + " " + AA(var0.AE, ispercent);
   }

   public static void AA(DataInputStream var0, boolean var1) {
      if (var0 == null) {
         GlobalService.getInstance().get_DATA((byte)2);
      } else {
         try {
            short var2;
            BZ = new MainInfoItem[var2 = var0.readShort()];

            for(int var3 = 0; var3 < var2; ++var3) {
               String var4 = var0.readUTF();
               BZ[var3] = new MainInfoItem(var4, var0.readByte(), var0.readByte());
            }

            if (var1) {
               SaveRms.AA(GlobalService.AF = var0.readShort(), "VerdataAttri");
            }

            var0.close();
         } catch (Exception var5) {
         }
      }
   }

   public static mVector SortVecItem(mVector var0) {
      int var4 = var0.size();

      int var1;
      int var2;
      int var3;
      for(var1 = 0; var1 < var4 - 1; ++var1) {
         var3 = var1;

         for(var2 = var1 + 1; var2 < var4; ++var2) {
            if (((MainItem)var0.elementAt(var2)).indexSort < ((MainItem)var0.elementAt(var3)).indexSort) {
               var3 = var2;
            }
         }

         if (var3 != var1) {
            AA(var0, var1, var3);
         }
      }

      boolean var6 = false;

      for(var1 = 0; var1 < var4 - 1; ++var1) {
         var3 = var1;

         for(var2 = var1 + 1; var2 < var4; ++var2) {
            if (((MainItem)var0.elementAt(var2)).typeObject == 4 && ((MainItem)var0.elementAt(var2)).ID < ((MainItem)var0.elementAt(var3)).ID) {
               var3 = var2;
            }
         }

         if (var3 != var1) {
            AA(var0, var1, var3);
         }
      }

      return var0;
   }

   private static void AA(mVector var0, int var1, int var2) {
      Object var3 = var0.elementAt(var2);
      var0.setElementAt(var0.elementAt(var1), var2);
      var0.setElementAt(var3, var1);
   }

   public static mVector AA(MainItem var0) {
      mVector var1 = new mVector();
      if (var0 != null && (var0.charClass == GameScreen.player.clazz || var0.charClass <= 0) && var0.typeObject == 3) {
         MainItem var2;
         if ((var2 = (MainItem)GameScreen.player.LR.get("" + var0.typeEquip)) != null) {
            for(int var3 = 0; var3 < var0.vecInfo.size(); ++var3) {
               infoShow var4 = (infoShow)var0.vecInfo.elementAt(var3);
               boolean var5 = false;
               if (var4.AD >= 0 && var4.AC == infoShow.HARDCODE_INFO_CO_BAN) {
                  for(int var6 = 0; var6 < var2.vecInfo.size(); ++var6) {
                     infoShow var7 = (infoShow)var2.vecInfo.elementAt(var6);
                     if (var4.AD == var7.AD) {
                        int var8 = var4.AE - var7.AE;
                        byte var9 = 6;
                        byte percent4 = getAttributePercentSafe(var4.AD);
                        String var10 = AA(var8, percent4);
                        if (var8 >= 0) {
                           var10 = "+" + AA(var8, percent4);
                           var9 = 1;
                        }

                        var1.addElement(new infoShow(-1, var8, var10, var9, (byte)-1));
                        var5 = true;
                        break;
                     }
                  }
               }

               if (!var5) {
                  var1.addElement(new infoShow(-1, 0, "", (byte)0, (byte)-1));
               }
            }
         }

         if (var1.size() == 0) {
            var1 = null;
         }

         return var1;
      } else {
         return null;
      }
   }

   public static mVector AA(MainItem var0, int var1) {
      mVector var2 = new mVector();
      if (var0 == null || var0.charClass != GameScreen.player.clazz && var0.charClass > 0) {
         return null;
      } else {
         MainItem var3;
         if ((var3 = (MainItem)GameScreen.player.LR.get("" + var0.typeEquip)) != null) {
            for(int var4 = 0; var4 < var0.vecInfo.size(); ++var4) {
               infoShow var5 = (infoShow)var0.vecInfo.elementAt(var4);
               boolean var6 = false;
               if (var5.AD >= 0 && var5.AC == infoShow.HARDCODE_INFO_CO_BAN) {
                  for(int var7 = 0; var7 < var3.vecInfo.size(); ++var7) {
                     infoShow var8 = (infoShow)var3.vecInfo.elementAt(var7);
                     if (var5.AD == var8.AD) {
                        int var9 = AA((int)var5.AE, var0.LvUpgrade, (int)var1) - var8.AE;
                        byte var10 = 6;
                        byte percent5 = getAttributePercentSafe(var5.AD);
                        String var11 = AA(var9, percent5);
                        if (var9 >= 0) {
                           var11 = "+" + AA(var9, percent5);
                           var10 = 1;
                        }

                        var2.addElement(new infoShow(-1, var9, var11, var10, (byte)-1));
                        var6 = true;
                        break;
                     }
                  }
               }

               if (!var6) {
                  var2.addElement(new infoShow(-1, 0, "", (byte)0, (byte)-1));
               }
            }
         }

         if (var2.size() == 0) {
            var2 = null;
         }

         return var2;
      }
   }

   private static int AA(int var0, int var1, int var2) {
      return var2 == var1 ? var0 : (var0 = var0 * 100 / (CU[var1] + 100)) + var0 * CU[var2] / 100;
   }

   public boolean AI() {
      return false;
   }

   public void setTimeMarket(int var1) {
      this.timeUse = var1;
      this.marketTime.setCountDown(var1);
   }

   public static String getDataKichAn(Item var0) {
      String var1;
      if ((var1 = (String)CD.get("" + var0.valueKickAn)) == null) {
         CD.put("" + var0.valueKickAn, "");
         GlobalService.getInstance().AH((byte)96, (short)var0.valueKickAn);
         var1 = "";
      }

      if (var1.length() == 0) {
         Item.vecItemKichAnCheckInfo.addElement(var0);
      }

      return var1;
   }

   public final boolean AA(short var1) {
      if (this.info == null || this.info.length() == 0) {
         this.AP = var1;
         var1 = this.AP;
         String var10001;
         String var2;
         if ((var2 = (String)Potion.CS.get("" + var1)) == null) {
            Potion.CS.put("" + var1, "");
            GlobalService.getInstance().AI(var1);
            var10001 = "";
         } else {
            var10001 = var2;
         }

         this.info = var10001;
         if (this.info.length() == 0) {
            return false;
         }
      }

      this.AA(this.info);
      return true;
   }
}
