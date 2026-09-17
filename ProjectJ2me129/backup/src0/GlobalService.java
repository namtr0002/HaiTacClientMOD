public final class GlobalService extends Cmd_Message {
   private static GlobalService instance;
   public static boolean AB = false;
   public static boolean AC = false;
   public static short AD;
   public static short AE;
   public static short AF;
   public static short AG;
   public static short AH;
   public static short AI;
   public static short AJ;
   public static short AK;
   public static short AL;
   public static short AM;

   public static GlobalService getInstance() {
      if (instance == null) {
         instance = new GlobalService();
      }

      return instance;
   }

   public final void Login(String user, String pass, byte type) {
      this.init((byte)-2);
      try {
         super.m.writer().writeByte(type);
         super.m.writer().writeUTF(user);
         super.m.writer().writeUTF(pass);
         if (GameCanvas.isIos()) {
            super.m.writer().writeByte(GameMidlet.ZOOM_IOS);
         } else {
            super.m.writer().writeByte(Ageticon.fakezoomlv);
         }
         super.m.writer().writeUTF("1.2.9");
         super.m.writer().writeByte(0);
         super.m.writer().writeByte(ListChar_Screen.IndexCharSelected);
         super.m.writer().writeUTF(GameMidlet.loginPlus());
         super.m.writer().writeUTF("checkmodhaitac:android:isolatedSplits=true");
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      this.send();
   }

   public final void Obj_Move(short x, short y) {
      this.init((byte)1);

      try {
         super.m.writer().writeShort(x);
         super.m.writer().writeShort(y);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      this.send();
   }

   public final void load_image(short id, short cat) {
      if (cat == 10000 && id >= 10000) {
         cat = 26000;
         id = (short)(id - 10000);
      }
      this.init((byte)-51);
      try {
         super.m.writer().writeShort(id + cat);
      } catch (Exception var3) {
      }
      this.send();
   }

   public final void char_info(short id) {
      this.init((byte)-5);
      try {
         super.m.writer().writeShort(id);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void monster_info(short var1) {
      this.init((byte)4);
      try {
         super.m.writer().writeShort(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void get_DATA(byte type) {
      this.init((byte)-7);
      try {
         super.m.writer().writeByte(type);
      } catch (Exception var2) {
      }
      this.send();
      (new StringBuffer("getdata=")).append(type).toString();
   }

   public final void Player_Fire(short idSkill, byte CatBeFire, mVector VecBeFire) {
      this.init((byte)2);
      boolean var4 = false;

      try {
         super.m.writer().writeShort(idSkill);
         super.m.writer().writeByte(CatBeFire);
         byte b = (byte)VecBeFire.size();
         super.m.writer().writeByte(b);
         if (b == 0) {
            return;
         }

         for(int i = 0; i < b; ++i) {
            Object_Effect_Skill object_Effect_Skill = (Object_Effect_Skill)VecBeFire.elementAt(i);
            super.m.writer().writeShort(object_Effect_Skill.ID);
         }
      } catch (Exception e) {
      }

      this.send();
   }

   public final void Player_Revice(byte var1) {
      this.init((byte)6);
      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }
      this.send();
   }

   public final void Get_Xp_Map_Train(byte type) {
      this.init((byte)9);

      try {
         super.m.writer().writeByte(type);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void Select_Char(short id, byte type, short idSupport) {
      if (GameCanvas.tickSelectChar <= 0) {
         this.init((byte)-9);

         try {
            super.m.writer().writeShort(id);
            super.m.writer().writeByte(type);
            super.m.writer().writeShort(idSupport);
         } catch (Exception var4) {
         }

         this.send();
         GameCanvas.tickSelectChar = 100;
      }
   }

   public final void Del_Char(byte type, short id) {
      this.init((byte)-37);
      try {
         super.m.writer().writeByte(type);
         super.m.writer().writeShort(id);
      } catch (Exception ex) {
      }
      this.send();
   }

   public final void Create_Char(String var1, byte var2, short var3, short var4) {
      this.init((byte)-8);

      try {
         super.m.writer().writeUTF(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
         super.m.writer().writeShort(var4);
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void Choice_Dialog_server(short id, byte value) {
      this.init((byte)-11);

      try {
         super.m.writer().writeShort(id);
         super.m.writer().writeByte(value);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void Get_Item_Map(short id, byte cat) {
      this.init((byte)12);

      try {
         super.m.writer().writeShort(id);
         super.m.writer().writeByte(cat);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void Use_Potion(short id) {
      this.init((byte)-13);

      try {
         super.m.writer().writeShort(id);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void Set_PK(byte type, byte action) {
      this.init((byte)14);

      try {
         super.m.writer().writeByte(type);
         super.m.writer().writeByte(action);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void Add_Point_Attribute(byte ID, short value) {
      this.init((byte)-16);

      try {
         super.m.writer().writeByte(ID);
         super.m.writer().writeShort(value);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void Add_Point_Skill(short var1, short var2) {
      this.init((byte)-17);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AA(byte var1, short var2, short var3, byte var4) {
      (new StringBuffer("Buy_Item_Potion ")).append(var2).toString();
      this.init((byte)-18);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeShort(var3);
         if (var1 == 116 || var1 == 118) {
            super.m.writer().writeByte(var4);
         }
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void AA(short var1, byte var2, byte var3) {
      this.init((byte)-20);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AD(short var1) {
      if (GameScreen.player != null) {
         this.Obj_Move((short)GameScreen.player.x, (short)GameScreen.player.y);
      }

      this.init((byte)-19);

      try {
         super.m.writer().writeShort(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AC(short var1, byte var2) {
      this.init((byte)-22);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void Sell_Item(byte var1, short var2, byte var3, short var4) {
      this.init((byte)-21);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
         super.m.writer().writeShort(var4);
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void AC(byte var1, short var2) {
      this.init((byte)-23);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AD(byte var1, short var2) {
      this.init((byte)-25);

      try {
         super.m.writer().writeByte(var1);
         if (var1 == 0 || var1 == 2 || var1 == 4 || var1 == 6) {
            super.m.writer().writeShort(var2);
         }
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AA(String var1) {
      this.init((byte)17);
      if (var1.compareTo("super") == 0 && (GameScreen.player.BW == 772 || GameScreen.player.BW == 773 || GameScreen.player.BW == 774)) {
         this.init((byte)68);
         this.send();
      }

      try {
         super.m.writer().writeUTF(var1);
      } catch (Exception var3) {
      }

      this.send();
      GameCanvas.chatTabScr.AB(T.CR, "", GameScreen.player.name + ": " + var1, (byte)1, false);
   }

   public final void AA(String var1, String var2) {
      this.init((byte)18);

      try {
         super.m.writer().writeUTF(var1);
         super.m.writer().writeUTF(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AB(String var1, String var2) {
      this.init((byte)-26);

      try {
         super.m.writer().writeUTF(var1);
         super.m.writer().writeUTF(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void Save_RMS_Server(byte var1, byte var2, byte[] var3) {
      this.init((byte)-33);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
         if (var3 != null) {
            int var5 = var3.length;
            super.m.writer().writeShort(var5);

            for(int var6 = 0; var6 < var5; ++var6) {
               super.m.writer().writeByte(var3[var6]);
            }
         }
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AA(byte var1, int var2) {
      this.init((byte)-29);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeInt(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AA(byte var1, byte var2, byte var3) {
      this.init((byte)-30);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void Chest(byte var1, short var2, byte var3, int var4) {
      (new StringBuffer("chestList ")).append(var1).append(" ").append(var2).append(" ").append(var3).append(" ").append(var4).toString();
      this.init((byte)-32);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
         super.m.writer().writeInt(var4);
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void AB() {
      this.init((byte)-6);
      this.send();
   }

   public final void AA(byte var1, short var2, byte var3) {
      this.init((byte)-35);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
      } catch (Exception var4) {
      }

      this.send();
      GameCanvas.CW = 4500;
   }

   public final void AB(short var1, byte var2, mVector var3) {
      this.init((byte)20);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3.size());

         for(int var5 = 0; var5 < var3.size(); ++var5) {
            Object_Effect_Skill var6 = (Object_Effect_Skill)var3.elementAt(var5);
            super.m.writer().writeShort(var6.ID);
         }
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AE(byte var1, short var2) {
      this.init((byte)-28);

      try {
         super.m.writer().writeByte(0);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AE(short var1) {
      this.init((byte)22);

      try {
         super.m.writer().writeShort(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AB(byte var1, byte var2) {
      this.init((byte)23);

      try {
         super.m.writer().writeByte(0);
         super.m.writer().writeByte(var2);
      } catch (Exception var3) {
      }

      this.send();
      GameCanvas.AA(T.BP, true);
   }

   public final void AA(int var1) {
      this.init((byte)-36);

      try {
         super.m.writer().writeInt(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void Request_Image_Android() {
      this.init((byte)-38);
      try {
         if (GameCanvas.isIos()) {
            super.m.writer().writeByte(GameMidlet.ZOOM_IOS);
         } else {
            super.m.writer().writeByte(Ageticon.fakezoomlv);
         }
      } catch (Exception var1) {
      }

      this.send();
   }

   public final void Show_Player_Info(String var1) {
      this.init((byte)-42);

      try {
         super.m.writer().writeUTF(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AE() {
      this.init((byte)-43);
      this.send();
   }

   public final void getDataEffAuto(short var1) {
      this.init((byte)-44);

      try {
         super.m.writer().writeShort(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void getDataSkillEff(byte type, short id) {
      this.init((byte)74);
      try {
         super.m.writer().writeByte(0);
         super.m.writer().writeShort(id);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void Update_Pk_Point() {
      this.init((byte)-45);
      this.send();
   }

   public final void World_Chanel(byte var1, String var2) {
      this.init((byte)-46);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeUTF(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void Upgrade_Item(byte type, short idItem, byte bery_gem) {
      this.init((byte)-48);
      try {
         super.m.writer().writeByte(type);
         super.m.writer().writeShort(idItem);
         super.m.writer().writeByte(bery_gem);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void Split_Item(byte var1, byte var2, short var3, byte var4, short var5) {
      this.init((byte)-50);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
         super.m.writer().writeByte(var4);
         super.m.writer().writeShort(var5);
      } catch (Exception var6) {
      }

      this.send();
   }

   public final void AG(byte var1, short var2) {
      this.init((byte)79);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AA(byte var1, short var2, byte var3, int var4, String var5) {
      this.init((byte)-49);

      try {
         super.m.writer().writeByte(var1);
         if (var1 != 1 && var1 != 6) {
            if (var1 == 2) {
               super.m.writer().writeUTF(var5);
            }
         } else {
            super.m.writer().writeShort(var2);
            super.m.writer().writeByte(var3);
            super.m.writer().writeInt(var4);
         }
      } catch (Exception var6) {
      }

      this.send();
   }

   public final void AE(byte var1) {
      this.init((byte)-53);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void ghost(byte var1) {
      this.init((byte)-55);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void changeMapOk() {
      this.init((byte)0);
      try {
         super.m.writer().writeShort(GameCanvas.loadmap.idMapLoadMap);
         super.m.writer().writeByte(0);
      } catch (Exception var1) {
      }

      this.send();
   }

   public final void OkChangeMapLink() {
      this.init((byte)30);
      this.send();
   }

   public final void AA(short var1, String[] var2) {
      this.init((byte)-58);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2.length);

         for(int var4 = 0; var4 < var2.length; ++var4) {
            super.m.writer().writeUTF(var2[var4]);
         }
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AI() {
      this.init((byte)-62);
      this.send();
   }

   public final void AG(byte var1) {
      this.init((byte)-63);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AJ() {
      this.init((byte)-65);
      this.send();
   }

   public final void AB(byte var1, byte var2, short var3, byte var4, short var5) {
      this.init((byte)-67);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
         super.m.writer().writeByte(var4);
         super.m.writer().writeShort(var5);
         super.m.writer().writeShort(-1);
      } catch (Exception var6) {
      }

      this.send();
   }

   public final void AA(byte var1, byte var2, short var3, byte var4, short var5, short var6) {
      this.init((byte)-67);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(7);
         super.m.writer().writeShort(var3);
         super.m.writer().writeByte(var4);
         super.m.writer().writeShort(1);
         super.m.writer().writeShort(var6);
      } catch (Exception var7) {
      }

      this.send();
   }

   public final void AB(byte var1, byte var2, short var3, byte var4, short var5, short var6) {
      this.init((byte)-67);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(7);
         super.m.writer().writeShort(var3);
         super.m.writer().writeByte(var4);
         super.m.writer().writeShort(1);
         super.m.writer().writeShort(var6);
      } catch (Exception var7) {
      }

      this.send();
   }

   public final void AC(String var1, String var2) {
      this.init((byte)-68);

      try {
         super.m.writer().writeUTF(var1);
         super.m.writer().writeUTF(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AK() {
      this.init((byte)-70);
      this.send();
   }

   public final void Auto_revice(byte type) {
      this.init((byte)-71);
      try {
         super.m.writer().writeByte(1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AC(byte var1, byte var2) {
      this.init((byte)-72);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AI(byte var1) {
      this.init((byte)-74);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(ReadMessenge.AG);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void Clan_CMD(byte var1, String var2, int var3, byte var4) {
      this.init((byte)-52);

      try {
         super.m.writer().writeByte(var1);
         if (var1 != 0 && var1 != 5 && var1 != 1 && var1 != 2 && var1 != 3) {
            if (var1 != 11 && var1 != 10 && var1 != 12) {
               if (var1 == 6) {
                  super.m.writer().writeByte(var4);
                  super.m.writer().writeByte(var3);
               } else if (var1 != 7 && var1 != 16) {
                  if (var1 == 14) {
                     super.m.writer().writeShort(var3);
                     super.m.writer().writeByte(var4);
                  }
               } else {
                  super.m.writer().writeUTF(var2);
                  super.m.writer().writeInt(var3);
               }
            } else {
               super.m.writer().writeInt(var3);
            }
         } else {
            super.m.writer().writeUTF(var2);
            super.m.writer().writeByte(var4);
         }
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void AA(byte var1, short var2, short var3) {
      this.init((byte)-77);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         if (var1 == 2 || var1 == 3) {
            super.m.writer().writeShort(var3);
         }
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AA(String[] var1) {
      this.init((byte)-103);

      try {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            super.m.writer().writeUTF(var1[var2]);
         }
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void NextMap(short var1) {
      this.init((byte)34);

      try {
         super.m.writer().writeShort(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AD(byte var1, byte var2) {
      this.init((byte)37);

      try {
         super.m.writer().writeByte(1);
         super.m.writer().writeByte(var1);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void getDataPart(short var1) {
      this.init((byte)-82);

      try {
         super.m.writer().writeShort(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AI(short var1) {
      this.init((byte)-105);

      try {
         super.m.writer().writeShort(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AJ(short var1) {
      this.init((byte)41);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(2);
      } catch (Exception var2) {
      }

      this.send();
   }

   public static short AC(String var0) {
      if (var0.length() >= 3 && var0.substring(0, 2).compareTo("et") == 0) {
         String var1;
         if (var0.length() >= 4) {
            if ((var1 = var0.substring(0, 4)).compareTo("et10") == 0) {
               return 30;
            }

            if (var1.compareTo("et11") == 0) {
               return 31;
            }

            if (var1.compareTo("et12") == 0) {
               return 32;
            }

            if (var1.compareTo("et13") == 0) {
               return 33;
            }

            if (var1.compareTo("et14") == 0) {
               return 34;
            }
         }

         if ((var1 = var0.substring(0, 3)).compareTo("et0") == 0) {
            return 20;
         }

         if (var1.compareTo("et1") == 0) {
            return 21;
         }

         if (var1.compareTo("et2") == 0) {
            return 22;
         }

         if (var1.compareTo("et3") == 0) {
            return 23;
         }

         if (var1.compareTo("et4") == 0) {
            return 24;
         }

         if (var1.compareTo("et5") == 0) {
            return 25;
         }

         if (var1.compareTo("et6") == 0) {
            return 26;
         }

         if (var1.compareTo("et7") == 0) {
            return 27;
         }

         if (var1.compareTo("et8") == 0) {
            return 28;
         }

         if (var1.compareTo("et9") == 0) {
            return 29;
         }
      }

      return -1;
   }

   public final void AE(byte var1, byte var2) {
      this.init((byte)43);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AC(byte var1, byte var2, short var3, byte var4, short var5) {
      this.init((byte)44);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
         super.m.writer().writeByte(var4);
         super.m.writer().writeShort(var5);
      } catch (Exception var6) {
      }

      this.send();
   }

   public final void AA(mVector var1) {
      this.init((byte)46);

      try {
         for(int var2 = 0; var2 < var1.size(); ++var2) {
            MainObject var3;
            if ((var3 = (MainObject)var1.elementAt(var2)).typeObject == 0) {
               super.m.writer().writeShort(var3.ID);
            }
         }
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AB(byte var1, short var2, byte var3, short var4) {
      this.init((byte)45);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
         super.m.writer().writeShort(var4);
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void AJ(byte var1) {
      this.init((byte)47);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
      (new StringBuffer("Send event=")).append(var1).toString();
   }

   public final void AF(byte var1, byte var2) {
      this.init((byte)75);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AH(byte var1, short var2) {
      this.init((byte)48);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AI(byte var1, short var2) {
      this.init((byte)49);

      try {
         super.m.writer().writeByte(0);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AK(byte var1) {
      this.init((byte)-85);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AJ(byte var1, short var2) {
      this.init((byte)-86);

      try {
         super.m.writer().writeByte(0);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AD(short var1, short var2) {
      this.init((byte)-87);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AL() {
      this.init((byte)-84);
      this.send();
   }

   public final void AA(byte var1, byte var2, byte var3, String var4) {
      this.init((byte)51);

      try {
         super.m.writer().writeByte(2);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
         super.m.writer().writeUTF(var4);
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void AL(byte var1) {
      this.init((byte)54);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AM(byte var1) {
      (new StringBuffer("vao nao>>>>>>>>>>> goilen=")).append(var1).toString();
      this.init((byte)82);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AN(byte var1) {
      this.init((byte)77);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AA(byte var1, byte var2, short var3) {
      (new StringBuffer("QUAY_HUY_HIEU>>>>>>>>>>> goilen=")).append(var1).append(" type = ").append(var2).append(" id = ").append(var3).toString();
      this.init((byte)-95);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AB(byte var1, byte var2, short var3) {
      (new StringBuffer("Send_Pet>>>>>>>>>>> goilen=")).append(4).append(" type = ").append(var2).append(" id = ").append(var3).toString();
      this.init((byte)-80);

      try {
         super.m.writer().writeByte(4);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void Send_Pet(byte var1) {
      (new StringBuffer("Send_Pet>>>>>>>>>>> goilen=")).append(3).toString();
      this.init((byte)-80);

      try {
         super.m.writer().writeByte(3);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void TaiXiu(byte var1, byte var2, int var3, byte var4, byte var5) {
      (new StringBuffer("Tai Xiu >>>>>>>>>>> goilen=")).append(1).append(" tienDatCuoc = ").append(var3).append(" cua = ").append(var4).toString();
      this.init((byte)80);

      try {
         super.m.writer().writeByte(0);
         super.m.writer().writeByte(1);
         super.m.writer().writeInt(var3);
         super.m.writer().writeByte(var4);
         super.m.writer().writeByte(var5);
      } catch (Exception var6) {
      }

      this.send();
   }

   public final void TaiXiu(byte var1, byte var2) {
      (new StringBuffer("Tai Xiu >>>>>>>>>>> goilen=")).append(var2).toString();
      this.init((byte)80);

      try {
         super.m.writer().writeByte(0);
         super.m.writer().writeByte(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void VotBanhChung(byte var1, byte var2, byte var3) {
      (new StringBuffer("Vot banh >>>>>>>>>>> action=")).append(1).append(" status=").append(var3).toString();
      this.init((byte)80);

      try {
         super.m.writer().writeByte(1);
         super.m.writer().writeByte(1);
         super.m.writer().writeByte(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AC(byte var1, short var2, byte var3) {
      this.init((byte)59);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AD(byte var1, short var2, byte var3) {
      this.init((byte)101);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AA(byte var1, mVector var2) {
      this.init((byte)101);

      try {
         super.m.writer().writeByte(6);
         super.m.writer().writeByte(var2.size());

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            Class_DS var3 = (Class_DS)var2.elementAt(var5);
            super.m.writer().writeShort(var3.AG);
         }
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AP(byte var1) {
      this.init((byte)60);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AM() {
      this.init((byte)61);

      try {
         super.m.writer().writeUTF(GameMidlet.AG);
         super.m.writer().writeUTF(GameMidlet.AH);
      } catch (Exception var1) {
      }

      this.send();
   }

   public final void AN() {
      this.init((byte)63);
      this.send();
   }

   public final void AA(byte var1, short var2, byte var3, byte var4) {
      this.init((byte)66);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
         super.m.writer().writeByte(var4);
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void AB(byte var1, short var2, byte var3, byte var4) {
      this.init((byte)-94);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
         super.m.writer().writeByte(var4);
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void AA(byte var1, byte var2, short var3, byte var4, byte var5) {
      this.init((byte)81);

      try {
         super.m.writer().writeByte(1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeShort(var3);
         super.m.writer().writeByte(var4);
         super.m.writer().writeByte(var5);
      } catch (Exception var6) {
      }

      this.send();
   }

   public final void AK(byte var1, short var2) {
      this.init((byte)81);

      try {
         super.m.writer().writeByte(4);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AB(byte var1, mVector var2) {
      this.init((byte)81);

      try {
         super.m.writer().writeByte(2);
         super.m.writer().writeByte(var2.size());

         for(var1 = 0; var1 < var2.size(); ++var1) {
            MainItem var3 = (MainItem)var2.elementAt(var1);
            super.m.writer().writeShort(var3.ID);
            super.m.writer().writeByte(var3.typeObject);
         }
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AA(byte var1, short var2, mVector var3) {
      this.init((byte)81);

      try {
         super.m.writer().writeByte(6);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3.size());

         for(var1 = 0; var1 < var3.size(); ++var1) {
            MainItem var5 = (MainItem)var3.elementAt(var1);
            super.m.writer().writeShort(var5.ID);
         }
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AB(short var1, byte var2, byte var3) {
      this.init((byte)69);

      try {
         super.m.writer().writeShort(var1);
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AL(byte var1, short var2) {
      this.init((byte)-89);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AE(byte var1, short var2, byte var3) {
      this.init((byte)-89);

      try {
         super.m.writer().writeByte(var1);
         super.m.writer().writeShort(var2);
         super.m.writer().writeByte(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AH(byte var1, byte var2) {
      this.init((byte)-90);

      try {
         super.m.writer().writeByte(1);
         super.m.writer().writeByte(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AQ(byte var1) {
      this.init((byte)-108);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
      }

      this.send();
   }

   public final void AB(byte var1, String var2, int var3, byte var4) {
      this.init((byte)-108);

      try {
         super.m.writer().writeByte(var1);
         if (var1 != 15 && var1 != 5 && var1 != 16 && var1 != 2 && var1 != 3) {
            if (var1 != 19 && var1 != 10 && var1 != 12) {
               if (var1 == 6) {
                  super.m.writer().writeByte(0);
                  super.m.writer().writeByte(var3);
               } else if (var1 != 18 && var1 != 20) {
                  if (var1 == 14) {
                     super.m.writer().writeShort(var3);
                     super.m.writer().writeByte(0);
                  }
               } else {
                  super.m.writer().writeUTF(var2);
                  super.m.writer().writeInt(var3);
               }
            } else {
               super.m.writer().writeInt(var3);
            }
         } else {
            super.m.writer().writeUTF(var2);
            super.m.writer().writeByte(0);
         }
      } catch (Exception var5) {
      }

      this.send();
   }

   public final void AM(byte var1, short var2) {
      this.init((byte)-97);

      try {
         super.m.writer().writeByte(1);
         super.m.writer().writeShort(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AI(byte var1, byte var2) {
      this.init((byte)-96);

      try {
         super.m.writer().writeByte(1);
         super.m.writer().writeByte(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AC(byte var1, byte var2, byte var3) {
      this.init((byte)-91);

      try {
         super.m.writer().writeByte(var2);
         super.m.writer().writeByte(var3);
      } catch (Exception var4) {
      }

      this.send();
   }

   public final void AJ(byte var1, byte var2) {
      this.init(var1);

      try {
         super.m.writer().writeByte(var2);
      } catch (Exception var3) {
      }

      this.send();
   }

   public final void AR(byte var1) {
      this.init((byte)-107);

      try {
         super.m.writer().writeByte(var1);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.send();
   }
   
    public final void item_drop(byte index, short num) {
        this.init((byte)11);
        try {
            super.m.writer().writeByte(index);
            super.m.writer().writeShort(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.send();
    }

}
