import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public final class LoadMap {
   public static mVector vecPointChange = new mVector("Loadmap.vecPointChange");
   public static MyHashTable hashMapItem = new MyHashTable();
   public static mVector[] mItemMap = new mVector[6];
   private static mVector BE = new mVector();
   public static byte[] mLockMap;
   public static short[] mMapLang;
   public static short[][] mSea;
   public static int[] mTranPointChangeMap = new int[]{5, 4, 1, 0};
   public int idMapLoadMap;
   public int mapW;
   public int mapH;
   public int limitW;
   public int limitH;
   private int BF;
   public int maxX;
   public int maxY;
   public int maxWMap;
   public int maxHMap;
   public int idLastMap;
   public static int idTile = -1;
   public static int wTile = 24;
   private int[] BG;
   public int[] AT;
   private byte[][] BH;
   private static MainImage imgTile;
   private static MainImage imgTileWater;
   public static int timeVibrateScreen = 0;
   public static int AV = 0;
   public static int AW = 0;
   public static byte AX = 0;
   public static byte specMap = 0;
   public static byte AZ;
   public static byte BA;
   private static String[] mNameMap = new String[]{"aa", "bb", "cc", "dd", "ee", "ff"};
   public static String[] BB;
   public static boolean isOnlineMap = true;
   private int fStand;
   private int fWater;
   public static int BD = 0;
   private int BN;
   private int BO;

   public LoadMap() {
      this.maxX = MotherCanvas.w / wTile + 1;
      this.maxY = MotherCanvas.h / wTile + 1;

      for(int i = 0; i < mItemMap.length; ++i) {
         mItemMap[i] = new mVector();
      }

   }

   public final void loadmap(byte[] var1) {
      try {
         ByteArrayInputStream var5 = new ByteArrayInputStream(var1);
         DataInputStream var6 = new DataInputStream(var5);
         this.mapW = var6.readByte();
         this.mapH = var6.readByte();
         byte num = var6.readByte();
         if (idTile != num) {
            idTile = num;
            imgTile = null;
            imgTileWater = null;
            this.fWater = mLockMap[idTile << 1];
            this.fStand = mLockMap[(idTile << 1) + 1];
            ObjectData.getImageOther((short)idTile, (short)20);
            if (idTile != 11 && idTile != 14) {
               if (LoadMapScreen.isMapSky != 0) {
                  LoadMapScreen.isMapSky = 0;
                  LoadImageStatic.loadImageEffBoat();
               }

               LoadMapScreen.isMapSky = 0;
            } else {
               if (LoadMapScreen.isMapSky != 1) {
                  LoadMapScreen.isMapSky = 1;
                  LoadImageStatic.loadImageEffBoat();
               }

               LoadMapScreen.isMapSky = 1;
            }
         }

         this.maxWMap = this.mapW * wTile;
         this.maxHMap = this.mapH * wTile;
         this.limitW = this.maxWMap - MotherCanvas.w;
         this.limitH = this.maxHMap - MotherCanvas.h;
         MainScreen.cameraMain.setAll(this.limitW, this.limitH, GameScreen.player.x - MotherCanvas.hw, GameScreen.player.y - MotherCanvas.hh);
         this.BG = new int[this.mapW * this.mapH];
         this.AT = new int[this.mapW * this.mapH];
         this.BH = new byte[this.mapW][];

         int var7;
         for(var7 = 0; var7 < this.mapW; ++var7) {
            this.BH[var7] = new byte[this.mapH];
         }

         this.BF = this.mapW * this.mapH;

         for(var7 = 0; var7 < this.mapW * this.mapH; ++var7) {
            byte var3 = var6.readByte();
            this.BG[var7] = var3;
            if (var3 < this.fStand && var3 != 0) {
               if (var3 >= this.fWater) {
                  this.AT[var7] = 2;
               } else {
                  this.AT[var7] = 0;
               }
            } else {
               this.AT[var7] = 1;
            }
         }

      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   public final void load_ItemMap(byte[] var1) {
      for(int var2 = 0; var2 < mItemMap.length; ++var2) {
         mItemMap[var2].removeAllElements();
      }

      try {
         ByteArrayInputStream var10 = new ByteArrayInputStream(var1);
         DataInputStream var12;
         short var11 = (var12 = new DataInputStream(var10)).readShort();

         int var3;
         for(var3 = 0; var3 < var11; ++var3) {
            MainItemMap var5;
            if ((var5 = AA(var12.readShort())) == null) {
               var12.readShort();
               var12.readShort();
            } else {
               short var6 = var12.readShort();
               short var7 = var12.readShort();
               if (!GameCanvas.isLowGraOrWP_PvP() || var5.AC == 2 || var5.AF.length > 0 || !var5.AG) {
                  Class_CD var8 = new Class_CD(var5.IDItem, var5.IDImage, var5.dx, var5.dy, var5.AF, var5.AC);
                  if (var5.IDImage == 312) {
                     var8.AA = 0;
                     if (var3 % 4 == 0) {
                        var8.AA = 1;
                     }
                  }

                  this.AA(var6, var7, var5.AF);
                  var8.AB(var6 * wTile, var7 * wTile);
                  if (var5.AC == -1) {
                     BE.addElement(var8);
                  } else {
                     var8.AA();
                     if (var5.AC != 6) {
                        mItemMap[var5.AC].addElement(var8);
                     }
                  }
               }
            }
         }

         short num2;
         if (!GameCanvas.isLowGraOrWP_PvP() && (num2 = var12.readShort()) > 0) {
            for(int i = 0; i < num2; ++i) {
               byte var14 = var12.readByte();
               String text = "";

               for(int k = 0; k < var14; ++k) {
                  text = text + (char)var12.readByte();
               }

               var14 = var12.readByte();
               String text2 = "";

               for(int m = 0; m < var14; ++m) {
                  text2 = text2 + (char)var12.readByte();
               }

               mItemMap[3].addElement(GameScreen.addEffectAuto(text, text2));
            }
         }

         for(var3 = 0; var3 < mItemMap.length; ++var3) {
            CRes.quickSort(mItemMap[var3]);
         }

      } catch (Exception var9) {
         var9.printStackTrace();
      }
   }

   public final void paint(mGraphics var1) {
      int var2 = MainScreen.cameraMain.xCam / wTile - 1;
      int var3 = MainScreen.cameraMain.yCam / wTile - 1;
      if (var2 < 0) {
         var2 = 0;
      }

      if (var3 < 0) {
         var3 = 0;
      }

      int var4 = var2 + this.maxX + 2;
      int var5 = var3 + this.maxY + 2;
      if (var4 > this.mapW) {
         var4 = this.mapW;
      }

      if (var5 > this.mapH) {
         var5 = this.mapH;
      }

      if ((imgTileWater == null || imgTileWater.img == null) && !GameCanvas.lowGraphic) {
         imgTileWater = ObjectData.getImageOther((short)idTile, (short)70);
      }

      if (imgTile != null && imgTile.img != null) {
         for(var2 = var2; var2 < var4; ++var2) {
            for(int var6 = var3; var6 < var5; ++var6) {
               int var7;
               if ((var7 = var6 * this.mapW + var2) < this.BG.length) {
                  var7 = this.BG[var7] - 1;
                  if (!GameCanvas.lowGraphic && idTile == 3 && var7 >= 35 && var7 <= 37 && GameCanvas.gameTick % 14 < 7) {
                     byte var9 = 0;
                     if (GameCanvas.gameTick / 14 % 2 == 0) {
                        var9 = 3;
                     }

                     if (imgTileWater != null && imgTileWater.img != null) {
                        var1.drawRegion(imgTileWater.img, (var9 + var7 - 35) / 10 * wTile, (var9 + var7 - 35) % 10 * wTile, wTile, wTile, 0, var2 * wTile, var6 * wTile, 0);
                     }
                  } else if (!GameCanvas.lowGraphic && var7 >= this.fWater - 1 && var7 < this.fStand - 1 && GameCanvas.gameTick % 14 < 7 && idTile != 3) {
                     int var8 = 0;
                     if (GameCanvas.gameTick / 14 % 2 == 0) {
                        var8 = this.fStand - this.fWater;
                     }

                     if (imgTileWater != null && imgTileWater.img != null) {
                        var1.drawRegion(imgTileWater.img, (var8 + var7 - (this.fWater - 1)) / 10 * wTile, (var8 + var7 - (this.fWater - 1)) % 10 * wTile, wTile, wTile, 0, var2 * wTile, var6 * wTile, 0);
                     }
                  } else if (var7 >= 0 && imgTile != null && imgTile.img != null) {
                     var1.drawRegion(imgTile.img, var7 / 10 * wTile, var7 % 10 * wTile, wTile, wTile, 0, var2 * wTile, var6 * wTile, 0);
                  }
               }
            }
         }

      } else {
         imgTile = ObjectData.getImageOther((short)idTile, (short)20);
         var1.setColor(-1331884);
         var1.fillRect(MainScreen.cameraMain.xCam, MainScreen.cameraMain.yCam, MotherCanvas.w, MotherCanvas.h);
      }
   }

   public final int AA(int var1, int var2) {
      int var3;
      return (var3 = var2 / wTile * this.mapW + var1 / wTile) <= this.BF && var1 >= 0 && var1 < this.limitW + MotherCanvas.w && var2 >= 0 && var2 < this.limitH + MotherCanvas.h ? this.AT[var3] : 1;
   }

   public final int AB(int var1, int var2) {
      return var2 / wTile * this.mapW + var1 / wTile;
   }

   private static MainItemMap AA(short var0) {
      MainItemMap var1;
      if ((var1 = (MainItemMap)hashMapItem.get("" + var0)) == null) {
         (var1 = new MainItemMap(var0)).AG = false;
         hashMapItem.put("" + var0, var1);
         GlobalService.getInstance().AH((byte)98, (short)var0);
         if (BD == 0) {
            BD = 1000;
         }

         ++BD;
      }

      return var1;
   }

   private void AA(int var1, int var2, int[][] var3) {
      try {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var1 + var3[var4][0] >= 0 && var1 + var3[var4][0] < this.mapW && var2 + var3[var4][1] >= 0 && var2 + var3[var4][1] < this.mapH) {
               this.AT[(var2 + var3[var4][1]) * this.mapW + var1 + var3[var4][0]] = 1;
            }
         }

      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public static byte AA() {
      return (byte)(AX + 1);
   }

   public static boolean AA(int var0) {
      return var0 == -1 || var0 == 1;
   }

   public final short[] AA(int var1, int var2, int var3, int var4, int var5, MainObject var6) {
      return this.AA(var1, var2, var3, var4, var5, var6, 0);
   }

   private short[] AA(int var1, int var2, int var3, int var4, int var5, MainObject var6, int var7) {
      if (MainObject.getDistance(var1 * wTile, var2 * wTile, var3 * wTile, var4 * wTile) <= wTile) {
         return null;
      } else if (var1 >= 0 && var2 >= 0 && var1 < wTile * this.mapW && var2 < wTile * this.mapH) {
         this.BN = 0;
         this.BO = 0;
         var1 -= this.BN;
         var2 -= this.BO;
         var3 -= this.BN;
         var4 -= this.BO;

         int var8;
         int var9;
         for(var7 = 0; var7 < GameCanvas.loadmap.BH.length; ++var7) {
            for(var8 = 0; var8 < GameCanvas.loadmap.BH[var7].length; ++var8) {
               if ((var9 = (this.BO + var8) * GameCanvas.loadmap.mapW + this.BN + var7) < GameCanvas.loadmap.AT.length - 1) {
                  if (var6.typeActionBoat != 0 || GameCanvas.loadmap.AT[var9] != 1 && GameCanvas.loadmap.AT[var9] != -1) {
                     GameCanvas.loadmap.BH[var7][var8] = 0;
                  } else {
                     GameCanvas.loadmap.BH[var7][var8] = -1;
                  }
               }
            }
         }

         int var11;
         int var12;
         int var13;
         if (specMap == 4) {
            label257:
            for(var7 = 0; var7 < GameScreen.vecPlayers.size(); ++var7) {
               MainObject var23;
               if ((var23 = (MainObject)GameScreen.vecPlayers.elementAt(var7)) != var6 && var23.boatSea != null && var23.boatSea.ID == var23.ID) {
                  var23.boatSea.AI();
                  var9 = var23.boatSea.AY;
                  var8 = var23.boatSea.AX;
                  int var19 = var23.boatSea.AW;
                  int var18 = var23.boatSea.AV;
                  LoadMap var17 = this;
                  int var10 = 0;

                  while(true) {
                     while(true) {
                        if (var10 > var8) {
                           continue label257;
                        }

                        var11 = 0;

                        while(var11 <= var9) {
                           var12 = var18 - var8 / 2 + var10;
                           var13 = var19 - var9 / 2 + var11;
                           if (var11 != var9 && var11 + 24 > var9) {
                              if (var11 + 24 > var9) {
                                 var11 = var9;
                              }
                           } else {
                              var11 += 24;
                           }

                           if (var12 >= 0 && var12 < var17.maxWMap && var13 >= 0 && var13 < var17.maxHMap) {
                              var17.BH[var12 / 24][var13 / 24] = -1;
                           }
                        }

                        if (var10 != var8 && var10 + 24 > var8) {
                           if (var10 + 24 > var8) {
                              var10 = var8;
                           }
                        } else {
                           var10 += 24;
                        }
                     }
                  }
               }
            }
         }

         var8 = var1;
         var9 = var2;
         short var21 = (short)var1;
         short var24 = (short)var2;
         GameCanvas.loadmap.BH[var1][var2] = 1;
         short var22 = 2;
         var13 = GameCanvas.loadmap.BH.length;
         int var14 = GameCanvas.loadmap.BH[0].length;
         int var15 = 0;

         do {
            ++var15;
            if (var15 > 1000) {
               return new short[var5 + 1];
            }

            var11 = -1;
            var12 = -1;
            if (var8 + 1 < var13 && GameCanvas.loadmap.BH[var8 + 1][var9] == 0) {
               GameCanvas.loadmap.BH[var8 + 1][var9] = (byte)var22;
               var11 = var8 + 1;
               var12 = var9;
            }

            if (var8 - 1 >= 0 && GameCanvas.loadmap.BH[var8 - 1][var9] == 0) {
               GameCanvas.loadmap.BH[var8 - 1][var9] = (byte)var22;
               if (var11 == -1 || CRes.AA(var11, var12, var3, var4) > CRes.AA(var8 - 1, var9, var3, var4)) {
                  var11 = var8 - 1;
                  var12 = var9;
               }
            }

            if (var9 + 1 < var14 && GameCanvas.loadmap.BH[var8][var9 + 1] == 0) {
               GameCanvas.loadmap.BH[var8][var9 + 1] = (byte)var22;
               if (var11 == -1 || CRes.AA(var11, var12, var3, var4) > CRes.AA(var8, var9 + 1, var3, var4)) {
                  var11 = var8;
                  var12 = var9 + 1;
               }
            }

            if (var9 - 1 >= 0 && GameCanvas.loadmap.BH[var8][var9 - 1] == 0) {
               GameCanvas.loadmap.BH[var8][var9 - 1] = (byte)var22;
               if (var11 == -1 || CRes.AA(var11, var12, var3, var4) > CRes.AA(var8, var9 - 1, var3, var4)) {
                  var11 = var8;
                  var12 = var9 - 1;
               }
            }

            byte var16 = -1;
            if (var11 != -1) {
               var16 = 0;
               var8 = var11;
               var9 = var12;
            } else {
               var9 = 1000;
               var8 = 1000;
            }

            for(short var25 = 0; var25 < var13; ++var25) {
               for(short var26 = 0; var26 < var14; ++var26) {
                  if (GameCanvas.loadmap.BH[var25][var26] > 1) {
                     byte[][] var29 = GameCanvas.loadmap.BH;
                     if ((var25 + 1 < var29.length && var29[var25 + 1][var26] == 0 ? true : (var25 - 1 >= 0 && var29[var25 - 1][var26] == 0 ? true : (var26 + 1 < var29[var25].length && var29[var25][var26 + 1] == 0 ? true : var26 - 1 >= 0 && var29[var25][var26 - 1] == 0))) && GameCanvas.loadmap.BH[var25][var26] + CRes.AA(var25, var26, var3, var4) < var22 + CRes.AA(var8, var9, var3, var4)) {
                        var8 = var25;
                        var9 = var26;
                        var22 = GameCanvas.loadmap.BH[var25][var26];
                        var16 = 0;
                     }
                  }
               }
            }

            if (var8 == var3 && var9 == var4) {
               if (var22 >= 127) {
                  return new short[var5 + 1];
               }

               int var28 = 0;
               short[] var27 = new short[var22];

               try {
                  while(true) {
                     var27[var28] = (short)((var8 << 8) + var9);
                     if (var8 + 1 < var13 && GameCanvas.loadmap.BH[var8 + 1][var9] == GameCanvas.loadmap.BH[var8][var9] - 1) {
                        var8 = (short)(var8 + 1);
                     } else if (var8 - 1 >= 0 && GameCanvas.loadmap.BH[var8 - 1][var9] == GameCanvas.loadmap.BH[var8][var9] - 1) {
                        var8 = (short)(var8 - 1);
                     } else if (var9 + 1 < var14 && GameCanvas.loadmap.BH[var8][var9 + 1] == GameCanvas.loadmap.BH[var8][var9] - 1) {
                        var9 = (short)(var9 + 1);
                     } else if (var9 - 1 >= 0 && GameCanvas.loadmap.BH[var8][var9 - 1] == GameCanvas.loadmap.BH[var8][var9] - 1) {
                        var9 = (short)(var9 - 1);
                     }

                     if (var8 == var21 && var9 == var24) {
                        var27[var22 - 1] = (short)((var1 << 8) + var2);
                        return var27;
                     }

                     ++var28;
                  }
               } catch (Exception var20) {
                  return new short[var5 + 1];
               }
            }

            if (var16 != 0) {
               return new short[var5 + 1];
            }
         } while(++var22 <= var5);

         return new short[var22];
      } else {
         return null;
      }
   }

   public final void AA(int var1, int var2, int var3, int var4) {
      for(int var5 = 0; var5 < var3 / 24; ++var5) {
         for(int var6 = 0; var6 < var4 / 24; ++var6) {
            int var7 = var1 - var3 / 2 + var5 * 24 + 13;
            int var8 = var2 - var4 / 2 + var6 * 24 + 13;
            if (var7 >= 0 && var7 < this.maxWMap && var8 >= 0 && var8 < this.maxHMap) {
               this.AT[var8 / 24 * this.mapW + var7 / 24] = 1;
            }
         }
      }

   }

   public final boolean mapLang() {
      for(int var1 = 0; var1 < mMapLang.length; ++var1) {
         if (this.idMapLoadMap == mMapLang[var1]) {
            return true;
         }
      }

      return false;
   }

   public static void AA(DataInputStream var0, boolean var1) {
      if (var0 == null) {
         GlobalService.getInstance().get_DATA((byte)6);
      } else {
         try {
            short var2;
            mNameMap = new String[var2 = var0.readShort()];

            for(int var3 = 0; var3 < var2; ++var3) {
               mNameMap[var3] = var0.readUTF();
            }

            if (var1) {
               SaveRms.AA(GlobalService.AG = var0.readShort(), "VerdataNameMap");
            }

            var0.close();
         } catch (Exception var4) {
         }
      }
   }

   public static String AB(int var0) {
      return var0 >= mNameMap.length ? "" : mNameMap[var0];
   }

   public static void AB(DataInputStream var0, boolean var1) {
      try {
         short var11 = var0.readShort();

         for(short var2 = 0; var2 < var11; ++var2) {
            short var3 = var0.readShort();
            byte var6 = var0.readByte();
            short var4 = var0.readShort();
            short var5 = var0.readShort();
            byte var7;
            int[][] var8 = new int[var7 = var0.readByte()][];

            for(int var9 = 0; var9 < var7; ++var9) {
               var8[var9] = new int[2];
               var8[var9][0] = var0.readByte();
               var8[var9][1] = var0.readByte();
            }

            hashMapItem.put("" + var2, new MainItemMap(var2, var3, var4, var5, var8, var6));
         }

         SaveRms.AA(GlobalService.AJ = var0.readShort(), "VerdataItemMap");
         var0.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      }
   }

   public static void AA(DataInputStream var0) {
      try {
         short var4 = var0.readShort();
         short var1 = var0.readShort();
         byte var5 = var0.readByte();
         short var2 = var0.readShort();
         short var3 = var0.readShort();
         byte var6;
         int[][] var7 = new int[var6 = var0.readByte()][];

         for(int var8 = 0; var8 < var6; ++var8) {
            var7[var8] = new int[2];
            var7[var8][0] = var0.readByte();
            var7[var8][1] = var0.readByte();
         }

         hashMapItem.put("" + var4, new MainItemMap(var4, var1, var2, var3, var7, var5));
         var0.close();
      } catch (Exception var9) {
      }
   }

   public final void AC() {
      for(int var1 = 0; var1 < BE.size(); ++var1) {
         Class_CD var2;
         MainItemMap var3;
         if ((var3 = AA((var2 = (Class_CD)BE.elementAt(var1)).IDItem)).AG) {
            if (!GameCanvas.isLowGraOrWP_PvP() || var3.AC == 2 || var3.AF.length > 0) {
               Class_CD var4 = new Class_CD(var2.IDItem, var3.IDImage, var3.dx, var3.dy, var3.AF, var3.AC);
               this.AA(var2.x / 24, var2.y / 24, var3.AF);
               var4.AB(var2.x, var2.y);
               var4.AA();
               if (var3.IDImage == 312) {
                  var4.AA = 0;
                  if (var1 % 4 == 0) {
                     var4.AA = 1;
                  }
               }

               if (var4.AC != 6) {
                  mItemMap[var4.AC].addElement(var4);
               }
            }

            BE.removeElement(var1);
            --var1;
         }
      }

   }

   public static byte AA(byte var0) {
      return (byte)(var0 + 1);
   }
}
