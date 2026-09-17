
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public final class DataSkillEff {
   private byte Frame;
   private byte f;
   private mVector listFrame = new mVector();
   private SmallImage[] smallImage;
   private byte[][] frameChar;
   private byte[] sequence;
   private byte[] indexSplash;
   private byte waitLoop;
   private boolean isLoadData;
   public short idEff;
   public static MyHashTable ALL_EFF_DATA = new MyHashTable();
   private byte typeupdate;
   private int x;
   private int y;
   public byte typeMove;
   private int min;
   public boolean wantDestroy;
   private long lasttime;
   private long timelive;

   static {
      new MyHashTable();
   }

   public DataSkillEff(short idEff, int time) {
      new mVector();
      this.frameChar = new byte[4][];
      this.indexSplash = new byte[4];
      this.idEff = 0;
      this.typeupdate = 0;
      this.min = 0;
      this.idEff = idEff;
      this.f = 0;
      if (time == -1) {
         this.typeupdate = 3;
      } else if (time == 0) {
         this.typeupdate = 1;
      } else {
         this.typeupdate = 2;
         this.timelive = System.currentTimeMillis() + (long)time;
      }

      this.load();
   }

   public DataSkillEff(short var1, int var2, byte var3, byte var4) {
      new mVector();
      this.frameChar = new byte[4][];
      this.indexSplash = new byte[4];
      this.idEff = 0;
      this.typeupdate = 0;
      this.min = 0;
      this.idEff = var1;
      this.typeMove = var3;
      this.waitLoop = var4;
      if (var2 == -1) {
         this.typeupdate = 3;
      } else if (var2 == 0) {
         this.typeupdate = 1;
      } else {
         this.typeupdate = 2;
         this.timelive = System.currentTimeMillis() + (long)var2;
      }

      this.load();
   }

   public DataSkillEff(short id, int x, int y) {
      new mVector();
      this.frameChar = new byte[4][];
      this.indexSplash = new byte[4];
      this.idEff = 0;
      this.typeupdate = 0;
      this.min = 0;
      this.idEff = id;
      this.x = x;
      this.y = y;
      this.typeupdate = 1;
      this.load();
   }

   private void loadData(byte[] var1) {
      if (var1 != null) {
         DataInputStream var2 = null;

         try {
            this.listFrame.removeAllElements();
            this.smallImage = null;
            byte var21 = (var2 = new DataInputStream(new ByteArrayInputStream(var1))).readByte();
            this.smallImage = new SmallImage[var21];

            int var3;
            for(var3 = 0; var3 < var21; ++var3) {
               this.smallImage[var3] = new SmallImage(var2.readUnsignedByte(), var2.readUnsignedByte(), var2.readUnsignedByte(), var2.readUnsignedByte(), var2.readUnsignedByte());
            }

            var3 = 0;
            int var22 = 10000;
            short var4 = var2.readShort();

            for(int var5 = 0; var5 < var4; ++var5) {
               byte var6 = var2.readByte();
               mVector var7 = new mVector();
               mVector var8 = new mVector();

               for(int var9 = 0; var9 < var6; ++var9) {
                  PartFrame var10;
                  (var10 = new PartFrame(var2.readShort(), var2.readShort(), var2.readByte())).AD = var2.readByte();
                  var10.AE = var2.readByte();
                  if (var10.AE == 0) {
                     var7.addElement(var10);
                  } else {
                     var8.addElement(var10);
                  }

                  if (var3 < CRes.abs(var10.AC)) {
                     var3 = CRes.abs(var10.AC);
                  }

                  if (CRes.abs(var10.AC) < var22) {
                     var22 = CRes.abs(var10.AC);
                  }
               }

               this.listFrame.addElement(new FrameEff(var7, var8));
            }

            short var10000 = this.smallImage[0].w;
            this.min = var22;
            short var23 = (short)var2.readUnsignedByte();
            this.sequence = new byte[var23];

            int var25;
            for(var25 = 0; var25 < var23; ++var25) {
               this.sequence[var25] = (byte)var2.readShort();
            }

            var2.readByte();
            byte var24 = var2.readByte();
            this.frameChar[0] = new byte[var24];

            for(var25 = 0; var25 < var24; ++var25) {
               this.frameChar[0][var25] = var2.readByte();
            }

            var24 = var2.readByte();
            this.frameChar[1] = new byte[var24];

            for(var25 = 0; var25 < var24; ++var25) {
               this.frameChar[1][var25] = var2.readByte();
            }

            var24 = var2.readByte();
            this.frameChar[3] = new byte[var24];

            for(var25 = 0; var25 < var24; ++var25) {
               this.frameChar[3][var25] = var2.readByte();
            }

            this.isLoadData = true;

            try {
               this.indexSplash[0] = (byte)(this.frameChar[0].length - 7);
               this.indexSplash[1] = (byte)(this.frameChar[1].length - 7);
               this.indexSplash[2] = (byte)(this.frameChar[2].length - 7);
               this.indexSplash[3] = (byte)(this.frameChar[3].length - 7);
            } catch (Exception var18) {
            }

            this.indexSplash[0] = var2.readByte();
            this.indexSplash[1] = var2.readByte();
            this.indexSplash[2] = var2.readByte();
            this.indexSplash[3] = this.indexSplash[2];
            return;
         } catch (Exception var19) {
         } finally {
            try {
               var2.close();
            } catch (Exception var17) {
            }

         }

      }
   }

   private void load() {
      EffectData effectData;
      if ((effectData = (EffectData)ALL_EFF_DATA.get(String.valueOf(this.idEff))) == null) {
         effectData = new EffectData();
         ALL_EFF_DATA.put(String.valueOf(this.idEff), effectData);
         GlobalService.getInstance().getDataSkillEff((byte)0, (short)this.idEff);
      }
      if (effectData != null && effectData.data != null) {
         effectData.count = GameCanvas.timeNow / 1000L;
         this.loadData(effectData.data);
         this.isLoadData = true;
      }

   }

   private boolean isHavedata() {
      if (this.isLoadData) {
         return true;
      } else {
         this.load();
         return false;
      }
   }

   public final void paintTopEff(mGraphics g, int x, int y, int hOne) {
      if (this.isHavedata()) {
         if (this.typeupdate != 3 || this.Frame != -1) {
            if (this.Frame < this.listFrame.size()) {
               FrameEff frameEff = (FrameEff)this.listFrame.elementAt(this.Frame);

               try {
                  mVector listPartTop = frameEff.listPartTop;

                  for(int i = 0; i < listPartTop.size(); ++i) {
                     PartFrame partFrame = (PartFrame)listPartTop.elementAt(i);
                     SmallImage smallImage = this.smallImage[partFrame.idSmallImg];
                     mImage image;
                     if ((image = this.getImage()) != null && image.image != null) {
                        short dx = partFrame.dx;
                        int num = smallImage.w;
                        int num2 = smallImage.h;
                        short num3 = smallImage.x;
                        short num4 = smallImage.y;
                        if (num3 > mImage.getImageWidth(image.image)) {
                           num3 = 0;
                        }

                        if (num4 > mImage.getImageHeight(image.image)) {
                           num4 = 0;
                        }

                        if (num3 + num > mImage.getImageWidth(image.image)) {
                           num = mImage.getImageWidth(image.image) - num3;
                        }

                        if (num4 + num2 > mImage.getImageHeight(image.image)) {
                           num2 = mImage.getImageHeight(image.image) - num4;
                        }

                        byte var14 = 0;
                        if (hOne == 62 && this.min >= 50) {
                           var14 = -8;
                        }

                        g.drawRegion(image, num3, num4, num, num2, partFrame.AD == 1 ? 2 : 0, x + dx, y + partFrame.AC + var14, 0);
                     }
                  }

                  return;
               } catch (Exception var15) {
                  var15.printStackTrace();
                  (new StringBuffer(" loi tai e  ")).append(this.idEff).toString();
               }
            }

         }
      }
   }

   public final void AA(mGraphics var1) {
      if (this.isHavedata()) {
         if (this.typeupdate != 3 || this.Frame != -1) {
            if (this.Frame < this.listFrame.size()) {
               FrameEff var2 = (FrameEff)this.listFrame.elementAt(this.Frame);

               try {
                  mVector var12 = var2.listPartTop;

                  for(int var3 = 0; var3 < var12.size(); ++var3) {
                     PartFrame var4 = (PartFrame)var12.elementAt(var3);
                     SmallImage var5 = this.smallImage[var4.idSmallImg];
                     mImage var6;
                     if ((var6 = this.getImage()) != null && var6.image != null) {
                        short var7 = var4.dx;
                        int var8 = var5.w;
                        int var9 = var5.h;
                        short var10 = var5.x;
                        short var13 = var5.y;
                        if (var10 > mImage.getImageWidth(var6.image)) {
                           var10 = 0;
                        }

                        if (var13 > mImage.getImageHeight(var6.image)) {
                           var13 = 0;
                        }

                        if (var10 + var8 > mImage.getImageWidth(var6.image)) {
                           var8 = mImage.getImageWidth(var6.image) - var10;
                        }

                        if (var13 + var9 > mImage.getImageHeight(var6.image)) {
                           var9 = mImage.getImageHeight(var6.image) - var13;
                        }

                        var1.drawRegion(var6, var10, var13, var8, var9, var4.AD == 1 ? 2 : 0, this.x + var7, this.y + var4.AC, 0);
                     }
                  }

                  return;
               } catch (Exception var11) {
                  var11.printStackTrace();
                  (new StringBuffer(" loi tai e  ")).append(this.idEff).toString();
               }
            }

         }
      }
   }

   public final void AB(mGraphics var1, int var2, int var3, int var4) {
      if (this.isHavedata()) {
         if (this.typeupdate != 3 || this.Frame != -1) {
            if (this.Frame < this.listFrame.size()) {
               FrameEff var5 = (FrameEff)this.listFrame.elementAt(this.Frame);

               try {
                  mVector var16 = var5.AB;

                  for(int var6 = 0; var6 < var16.size(); ++var6) {
                     PartFrame var7 = (PartFrame)var16.elementAt(var6);
                     SmallImage var8 = this.smallImage[var7.idSmallImg];
                     mImage var9;
                     if ((var9 = this.getImage()) != null && var9.image != null) {
                        short var10 = var7.dx;
                        int var11 = var8.w;
                        int var12 = var8.h;
                        short var13 = var8.x;
                        short var17 = var8.y;
                        if (var13 > mImage.getImageWidth(var9.image)) {
                           var13 = 0;
                        }

                        if (var17 > mImage.getImageHeight(var9.image)) {
                           var17 = 0;
                        }

                        if (var13 + var11 > mImage.getImageWidth(var9.image)) {
                           var11 = mImage.getImageWidth(var9.image) - var13;
                        }

                        if (var17 + var12 > mImage.getImageHeight(var9.image)) {
                           var12 = mImage.getImageHeight(var9.image) - var17;
                        }

                        byte var14 = 0;
                        if (var4 == 62 && this.min >= 50) {
                           var14 = -8;
                        }

                        var1.drawRegion(var9, var13, var17, var11, var12, var7.AD == 1 ? 2 : 0, var2 + var10, var3 + var7.AC + var14, 0);
                     }
                  }

                  return;
               } catch (Exception var15) {
               }
            }

         }
      }
   }

   public final void paintBottomEff(mGraphics var1) {
      if (this.isHavedata()) {
         if (this.typeupdate != 3 || this.Frame != -1) {
            if (this.Frame < this.listFrame.size()) {
               FrameEff var2 = (FrameEff)this.listFrame.elementAt(this.Frame);

               try {
                  mVector var12 = var2.AB;

                  for(int var3 = 0; var3 < var12.size(); ++var3) {
                     PartFrame var4 = (PartFrame)var12.elementAt(var3);
                     SmallImage var5 = this.smallImage[var4.idSmallImg];
                     mImage var6;
                     if ((var6 = this.getImage()) != null && var6.image != null) {
                        short var7 = var4.dx;
                        int var8 = var5.w;
                        int var9 = var5.h;
                        short var10 = var5.x;
                        short var13 = var5.y;
                        if (var10 > mImage.getImageWidth(var6.image)) {
                           var10 = 0;
                        }

                        if (var13 > mImage.getImageHeight(var6.image)) {
                           var13 = 0;
                        }

                        if (var10 + var8 > mImage.getImageWidth(var6.image)) {
                           var8 = mImage.getImageWidth(var6.image) - var10;
                        }

                        if (var13 + var9 > mImage.getImageHeight(var6.image)) {
                           var9 = mImage.getImageHeight(var6.image) - var13;
                        }

                        var1.drawRegion(var6, var10, var13, var8, var9, var4.AD == 1 ? 2 : 0, this.x + var7, this.y + var4.AC, 0);
                     }
                  }

                  return;
               } catch (Exception var11) {
               }
            }

         }
      }
   }

   private mImage getImage() {
      EffectData var1;
      (var1 = (EffectData)ALL_EFF_DATA.get(String.valueOf(this.idEff))).count = GameCanvas.timeNow / 1000L;
      return var1.image;
   }

   public static EffectData readData(byte[] var0) {
      EffectData var1 = null;
      try {
         ByteArrayInputStream var2 = new ByteArrayInputStream(var0);
         DataInputStream var8;
         short var3 = (var8 = new DataInputStream(var2)).readShort();
         byte[] var4 = new byte[var8.readShort()];
         var8.read(var4);
         byte[] var5 = new byte[var8.available()];
         var8.read(var5);
         if ((var1 = (EffectData)ALL_EFF_DATA.get(String.valueOf(var3))) != null) {
            var1.data = var4;
            var1.image = mImage.AA(var5);
            var1.count = GameCanvas.timeNow / 1000L;
         }

         short var10 = var3;
         byte[] var9 = var0;

         try {
            CRes.saveRMS("DataSkillEff" + var10, var9);
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      } catch (Exception var7) {
      }
      Ageteff.save(var0); // add this
      return var1;
   }

   public final void AA() {
      if (this.isHavedata()) {
         if (this.listFrame.size() > 0 || this.sequence.length > 0) {
            try {
               switch(this.typeupdate) {
               case 0:
                  ++this.f;
                  if (this.f > this.sequence.length) {
                     this.wantDestroy = true;
                     this.f = 0;
                  }

                  this.Frame = this.sequence[this.f];
                  return;
               case 1:
                  ++this.f;
                  if (this.f > this.sequence.length) {
                     this.f = 0;
                     this.wantDestroy = true;
                  }

                  this.Frame = this.sequence[this.f];
                  return;
               case 2:
                  ++this.f;
                  if (this.f > this.sequence.length) {
                     this.f = 0;
                  }

                  if (this.timelive - System.currentTimeMillis() < 0L) {
                     this.wantDestroy = true;
                  }

                  this.Frame = this.sequence[this.f];
                  return;
               case 3:
                  ++this.f;
                  if (this.f > this.sequence.length) {
                     label52: {
                        if (this.waitLoop > 0) {
                           if (System.currentTimeMillis() - this.lasttime <= (long)(this.waitLoop * 1000)) {
                              break label52;
                           }

                           this.lasttime = System.currentTimeMillis();
                        }

                        this.f = 0;
                     }
                  }

                  if (this.f < this.sequence.length) {
                     this.Frame = this.sequence[this.f];
                     return;
                  } else {
                     this.Frame = -1;
                     return;
                  }
               default:
               }
            } catch (Exception var1) {
            }
         }
      }
   }
}
