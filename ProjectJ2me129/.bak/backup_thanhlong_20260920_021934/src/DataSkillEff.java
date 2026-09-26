
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public final class DataSkillEff {
   public byte Frame;
   public byte f;
   public long timeremove;
   public long timeGetBack;
   public mVector listFrame = new mVector();
   public mVector listAnima = new mVector();
   public SmallImage[] smallImage;
   public byte[][] frameChar = new byte[4][];
   public byte[] sequence;
   public int fw;
   public int fh;
   public byte[] indexSplash = new byte[4];
   private int loop;
   public byte waitLoop;
   public boolean isLoadData;
   public short idEff;
   public boolean canremove;
   public static MyHashTable ALL_EFF_DATA = new MyHashTable();
   public static MyHashTable ALL_IMAGE_EFF_DATA = new MyHashTable();
   public byte typeupdate;
   public static final byte NORMAL = 0;
   public static final byte REMOVE_BY_FRAME = 1;
   public static final byte REMOVE_BY_TIME = 2;
   public static final byte NO_REMOVE = 3;
   public int x;
   public int y;
   public byte typeMove;
   public static final byte CHOANG = 1;
   public static final byte BIENHINH = 2;
   public boolean isbuff;
   private int min;
   public boolean wantDestroy;
   private long lasttime;
   private long timelive;
   public boolean isChangeFlip = false;
   public int rotate = 0;

   static {
      new MyHashTable();
   }

   public DataSkillEff(short idEff, byte[] data) {
   }

   public DataSkillEff(short idEff, int time) {
      this(idEff, time, false);
   }

   public DataSkillEff(short idEff, int time, boolean changeFlip) {
      this.idEff = idEff;
      this.f = 0;
      this.isChangeFlip = changeFlip;
      switch (time) {
      case -1:
         this.typeupdate = 3;
         break;
      case 0:
         this.typeupdate = 1;
         break;
      default:
         this.typeupdate = 2;
         this.timelive = System.currentTimeMillis() + (long)time;
         break;
      }
      this.load(changeFlip);
   }

   public DataSkillEff(short idEff, int time, byte typeMove, byte loop) {
      this(idEff, time, typeMove, loop, false);
   }

   public DataSkillEff(short idEff, int time, byte typeMove, byte loop, boolean changeFlip) {
      this.idEff = idEff;
      this.typeMove = typeMove;
      this.waitLoop = loop;
      this.isChangeFlip = changeFlip;
      switch (time) {
      case -1:
         this.typeupdate = 3;
         break;
      case 0:
         this.typeupdate = 1;
         break;
      default:
         this.typeupdate = 2;
         this.timelive = System.currentTimeMillis() + (long)time;
         break;
      }
      this.load(changeFlip);
   }

   public DataSkillEff(short idEff, int time, byte typeMove, byte loop, int rotate) {
      this(idEff, time, typeMove, loop, false, rotate);
   }

   public DataSkillEff(short idEff, int time, byte typeMove, byte loop, boolean changeFlip, int rotate) {
      this(idEff, time, typeMove, loop, changeFlip);
      this.rotate = rotate;
   }

   public DataSkillEff(short idEff, int x, int y) {
      this(idEff, x, y, false);
   }

   public DataSkillEff(short idEff, int x, int y, boolean changeFlip) {
      this.idEff = idEff;
      this.x = x;
      this.y = y;
      this.typeupdate = 1;
      this.isChangeFlip = changeFlip;
      this.load(changeFlip);
   }

   public DataSkillEff(short idEff, int x, int y, int rotate) {
      this(idEff, x, y, false, rotate);
   }

   public DataSkillEff(short idEff, int x, int y, boolean changeFlip, int rotate) {
      this(idEff, x, y, changeFlip);
      this.rotate = rotate;
   }

   public DataSkillEff(byte[] array) {
      this.loadData(array, false);
   }

   public DataSkillEff(byte[] array, boolean changeFlip) {
      this.loadData(array, changeFlip);
   }

   public void applyTemplate(SkillEffTemplate t) {
      if (t == null) return;
      this.listFrame = t.listFrame;
      this.smallImage = t.smallImage;
      this.sequence = t.sequence;
      this.frameChar = t.frameChar;
      this.fw = t.fw;
      this.fh = t.fh;
      this.min = t.min;
      this.isLoadData = true;
   }

   public static SkillEffTemplate parseTemplate(byte[] array, boolean changeFlip) {
      if (array == null) {
         return null;
      }
      SkillEffTemplate t = new SkillEffTemplate();
      DataInputStream dataInputStream = null;
      try {
         dataInputStream = new DataInputStream(new ByteArrayInputStream(array));
         int num = dataInputStream.readByte();
         t.smallImage = new SmallImage[num];
         for (int i = 0; i < num; i++) {
            int imageId = dataInputStream.readUnsignedByte();
            if (imageId == 255) {
               t.smallImage[i] = new SmallImage(
                  dataInputStream.readUnsignedByte(),
                  dataInputStream.readShort(),
                  dataInputStream.readShort(),
                  dataInputStream.readShort(),
                  dataInputStream.readShort()
               );
            } else {
               t.smallImage[i] = new SmallImage(
                  imageId,
                  dataInputStream.readUnsignedByte(),
                  dataInputStream.readUnsignedByte(),
                  dataInputStream.readUnsignedByte(),
                  dataInputStream.readUnsignedByte()
               );
            }
         }
         int num2 = 0;
         int num3 = 10000;
         int num4 = dataInputStream.readShort();
         boolean parsedOk = false;
         try {
            for (int j = 0; j < num4; j++) {
               byte b = dataInputStream.readByte();
               mVector mVector2 = new mVector();
               mVector mVector3 = new mVector();
               for (int k = 0; k < b; k++) {
                  PartFrame partFrame = new PartFrame(dataInputStream.readShort(), dataInputStream.readShort(), dataInputStream.readByte());
                  int flipOrExtendedMarker = dataInputStream.readUnsignedByte();
                  if (flipOrExtendedMarker == 128) {
                     partFrame.flip = partFrame.AD = dataInputStream.readByte();
                     partFrame.rotate = dataInputStream.readShort();
                  } else {
                     partFrame.flip = partFrame.AD = (byte)flipOrExtendedMarker;
                  }
                  if (changeFlip) {
                     partFrame.flip = partFrame.AD = (byte)((partFrame.flip == 0) ? 1 : 0);
                     partFrame.rotate = (short)(-partFrame.rotate);
                  }
                  partFrame.onTop = partFrame.AE = dataInputStream.readByte();
                  if (partFrame.onTop == 0) {
                     mVector2.addElement(partFrame);
                  } else {
                     mVector3.addElement(partFrame);
                  }
                  int absDy = CRes.abs(partFrame.dy);
                  if (num2 < absDy) {
                     num2 = absDy;
                  }
                  if (absDy < num3) {
                     num3 = absDy;
                  }
               }
               t.listFrame.addElement(new FrameEff(mVector2, mVector3));
            }
            if (t.smallImage != null && t.smallImage.length > 0 && t.smallImage[0] != null) {
               t.fw = t.smallImage[0].w;
            }
            t.min = num3;
            t.fh = num2;
            short num5 = (short)dataInputStream.readUnsignedByte();
            t.sequence = new byte[num5];
            for (int l = 0; l < num5; l++) {
               t.sequence[l] = (byte)dataInputStream.readShort();
            }
            dataInputStream.readByte();
            num5 = (short)dataInputStream.readByte();
            t.frameChar[0] = new byte[num5];
            for (int m = 0; m < num5; m++) {
               t.frameChar[0][m] = dataInputStream.readByte();
            }
            num5 = (short)dataInputStream.readByte();
            t.frameChar[1] = new byte[num5];
            for (int n = 0; n < num5; n++) {
               t.frameChar[1][n] = dataInputStream.readByte();
            }
            num5 = (short)dataInputStream.readByte();
            t.frameChar[3] = new byte[num5];
            for (int num6 = 0; num6 < num5; num6++) {
               t.frameChar[3][num6] = dataInputStream.readByte();
            }
            t.frameChar[2] = t.frameChar[3];
            parsedOk = true;
         } catch (Exception e) {
            parsedOk = false;
         }

         if (!parsedOk) {
            t.listFrame.removeAllElements();
            try {
               dataInputStream.close();
            } catch (Exception ignored) {
            }
            dataInputStream = new DataInputStream(new ByteArrayInputStream(array));
            dataInputStream.readByte();
            for (int i = 0; i < num; i++) {
               int imageId = dataInputStream.readUnsignedByte();
               if (imageId == 255) {
                  dataInputStream.readUnsignedByte();
                  dataInputStream.readShort();
                  dataInputStream.readShort();
                  dataInputStream.readShort();
                  dataInputStream.readShort();
               } else {
                  dataInputStream.readUnsignedByte();
                  dataInputStream.readUnsignedByte();
                  dataInputStream.readUnsignedByte();
                  dataInputStream.readUnsignedByte();
               }
            }
            num4 = dataInputStream.readShort();
            num2 = 0;
            num3 = 10000;
            for (int j = 0; j < num4; j++) {
               byte b = dataInputStream.readByte();
               mVector mVector2 = new mVector();
               mVector mVector3 = new mVector();
               for (int k = 0; k < b; k++) {
                  PartFrame partFrame = new PartFrame(dataInputStream.readShort(), dataInputStream.readShort(), dataInputStream.readByte());
                  partFrame.flip = partFrame.AD = (byte)(changeFlip ? 1 : 0);
                  partFrame.onTop = partFrame.AE = 0;
                  mVector2.addElement(partFrame);
                  int absDy = CRes.abs(partFrame.dy);
                  if (num2 < absDy) {
                     num2 = absDy;
                  }
                  if (absDy < num3) {
                     num3 = absDy;
                  }
               }
               t.listFrame.addElement(new FrameEff(mVector2, mVector3));
            }
            if (t.smallImage != null && t.smallImage.length > 0 && t.smallImage[0] != null) {
               t.fw = t.smallImage[0].w;
            }
            t.min = num3;
            t.fh = num2;
            short num5 = dataInputStream.readShort();
            t.sequence = new byte[num5];
            for (int l = 0; l < num5; l++) {
               t.sequence[l] = (byte)dataInputStream.readShort();
            }
         }
      } catch (Exception e) {
      } finally {
         try {
            if (dataInputStream != null) {
               dataInputStream.close();
            }
         } catch (Exception ignored) {
         }
      }
      return t;
   }

   public void loadData(byte[] array) {
      this.loadData(array, this.isChangeFlip);
   }

   public void loadData(byte[] array, boolean changeFlip) {
      SkillEffTemplate t = parseTemplate(array, changeFlip);
      this.applyTemplate(t);
   }

   public void load() {
      this.load(this.isChangeFlip);
   }

   public void load(boolean changeFlip) {
      String key = String.valueOf(this.idEff);
      EffectData effectData = (EffectData)ALL_EFF_DATA.get(key);
      if (effectData == null) {
         byte[] rmsData = CRes.loadRMS("DataSkillEff" + this.idEff);
         if (rmsData != null && rmsData.length > 0) {
            effectData = readData(rmsData, false);
         }
      }
      if (effectData == null) {
         effectData = new EffectData();
         ALL_EFF_DATA.put(key, effectData);
         GlobalService.getInstance().getDataSkillEff((byte)0, (short)this.idEff);
      }
      if (effectData != null && effectData.data != null) {
         effectData.count = GameCanvas.timeNow / 1000L;
         SkillEffTemplate t = changeFlip ? effectData.templateFlip : effectData.templateNormal;
         if (t == null) {
            t = parseTemplate(effectData.data, changeFlip);
            if (changeFlip) {
               effectData.templateFlip = t;
            } else {
               effectData.templateNormal = t;
            }
         }
         this.applyTemplate(t);
      }
   }

   public boolean isHavedata() {
      if (this.isLoadData) {
         return true;
      }
      this.load(this.isChangeFlip);
      return this.isLoadData;
   }

   private mImage cachedImg;
   private long lastImgCheck;
   private String idEffStr;

   public void paintTopEff(mGraphics g, int x, int y, int hOne) {
      if (!this.isHavedata() || (this.typeupdate == 3 && this.Frame == -1) || this.Frame < 0 || this.Frame >= this.listFrame.size()) {
         return;
      }
      FrameEff frameEff = (FrameEff)this.listFrame.elementAt(this.Frame);
      if (frameEff == null) return;
      mImage image = this.getImage();
      if (image == null || image.image == null) return;
      int imgW = (image.width > 0) ? image.width : mImage.getImageWidth(image.image);
      int imgH = (image.height > 0) ? image.height : mImage.getImageHeight(image.image);
      try {
         mVector listPartTop = frameEff.listPartTop;
         if (listPartTop == null) return;
         int partCount = listPartTop.size();
         for (int i = 0; i < partCount; i++) {
            PartFrame partFrame = (PartFrame)listPartTop.elementAt(i);
            if (partFrame == null || this.smallImage == null || partFrame.idSmallImg < 0 || partFrame.idSmallImg >= this.smallImage.length) continue;
            SmallImage smallImage = this.smallImage[partFrame.idSmallImg];
            if (smallImage == null) continue;
            int dx = partFrame.dx;
            int dy = (partFrame.dy != 0) ? partFrame.dy : partFrame.AC;
            int totalRotate = partFrame.rotate;
            if (this.rotate != 0) {
               totalRotate += this.rotate;
               double rad = (double)this.rotate * (3.141592653589793 / 180.0);
               double cos = Math.cos(rad);
               double sin = Math.sin(rad);
               int rdx = (int)Math.round((double)dx * cos - (double)dy * sin);
               int rdy = (int)Math.round((double)dx * sin + (double)dy * cos);
               dx = rdx;
               dy = rdy;
            }
            int num = smallImage.w;
            int num2 = smallImage.h;
            int num3 = smallImage.x;
            int num4 = smallImage.y;
            if (num3 > imgW) num3 = 0;
            if (num4 > imgH) num4 = 0;
            if (num3 + num > imgW) num = imgW - num3;
            if (num4 + num2 > imgH) num2 = imgH - num4;
            int num5 = 0;
            if (hOne == 62 && this.min >= 50) {
               num5 = -8;
            }
            byte flip = (partFrame.flip != 0) ? partFrame.flip : partFrame.AD;
            g.drawRegion(image, num3, num4, num, num2, (flip == 1) ? 2 : 0, x + dx, y + dy + num5, 0, totalRotate);
         }
      } catch (Exception e) {
      }
   }

   public void paintTopEff(mGraphics g) {
      if (!this.isHavedata() || (this.typeupdate == 3 && this.Frame == -1) || this.Frame < 0 || this.Frame >= this.listFrame.size()) {
         return;
      }
      FrameEff frameEff = (FrameEff)this.listFrame.elementAt(this.Frame);
      if (frameEff == null) return;
      mImage image = this.getImage();
      if (image == null || image.image == null) return;
      int imgW = (image.width > 0) ? image.width : mImage.getImageWidth(image.image);
      int imgH = (image.height > 0) ? image.height : mImage.getImageHeight(image.image);
      try {
         mVector listPartTop = frameEff.listPartTop;
         if (listPartTop == null) return;
         int partCount = listPartTop.size();
         for (int i = 0; i < partCount; i++) {
            PartFrame partFrame = (PartFrame)listPartTop.elementAt(i);
            if (partFrame == null || this.smallImage == null || partFrame.idSmallImg < 0 || partFrame.idSmallImg >= this.smallImage.length) continue;
            SmallImage smallImage = this.smallImage[partFrame.idSmallImg];
            if (smallImage == null) continue;
            int dx = partFrame.dx;
            int dy = (partFrame.dy != 0) ? partFrame.dy : partFrame.AC;
            int totalRotate = partFrame.rotate;
            if (this.rotate != 0) {
               totalRotate += this.rotate;
               double rad = (double)this.rotate * (3.141592653589793 / 180.0);
               double cos = Math.cos(rad);
               double sin = Math.sin(rad);
               int rdx = (int)Math.round((double)dx * cos - (double)dy * sin);
               int rdy = (int)Math.round((double)dx * sin + (double)dy * cos);
               dx = rdx;
               dy = rdy;
            }
            int num = smallImage.w;
            int num2 = smallImage.h;
            int num3 = smallImage.x;
            int num4 = smallImage.y;
            if (num3 > imgW) num3 = 0;
            if (num4 > imgH) num4 = 0;
            if (num3 + num > imgW) num = imgW - num3;
            if (num4 + num2 > imgH) num2 = imgH - num4;
            byte flip = (partFrame.flip != 0) ? partFrame.flip : partFrame.AD;
            g.drawRegion(image, num3, num4, num, num2, (flip == 1) ? 2 : 0, this.x + dx, this.y + dy, 0, totalRotate);
         }
      } catch (Exception e) {
      }
   }

   public void AA(mGraphics g) {
      this.paintTopEff(g);
   }

   public void paint(mGraphics g) {
      this.paint(g, this.x, this.y);
   }

   public void paint(mGraphics g, int px, int py) {
      if (!this.isHavedata() || this.Frame < 0 || this.Frame >= this.listFrame.size()) {
         return;
      }
      FrameEff frameEff = (FrameEff)this.listFrame.elementAt(this.Frame);
      if (frameEff == null) return;
      mImage image = this.getImage();
      if (image == null || image.image == null) return;
      int imgW = (image.width > 0) ? image.width : mImage.getImageWidth(image.image);
      int imgH = (image.height > 0) ? image.height : mImage.getImageHeight(image.image);
      try {
         mVector listPartPaint = frameEff.getListPartPaint();
         if (listPartPaint == null) return;
         int partCount = listPartPaint.size();
         for (int i = 0; i < partCount; i++) {
            PartFrame partFrame = (PartFrame)listPartPaint.elementAt(i);
            if (partFrame == null || this.smallImage == null || partFrame.idSmallImg < 0 || partFrame.idSmallImg >= this.smallImage.length) continue;
            SmallImage smallImage = this.smallImage[partFrame.idSmallImg];
            if (smallImage == null) continue;
            int dx = partFrame.dx;
            int dy = (partFrame.dy != 0) ? partFrame.dy : partFrame.AC;
            int totalRotate = partFrame.rotate;
            if (this.rotate != 0) {
               totalRotate += this.rotate;
               double rad = (double)this.rotate * (3.141592653589793 / 180.0);
               double cos = Math.cos(rad);
               double sin = Math.sin(rad);
               int rdx = (int)Math.round((double)dx * cos - (double)dy * sin);
               int rdy = (int)Math.round((double)dx * sin + (double)dy * cos);
               dx = rdx;
               dy = rdy;
            }
            int num = smallImage.w;
            int num2 = smallImage.h;
            int num3 = smallImage.x;
            int num4 = smallImage.y;
            if (num3 > imgW) num3 = 0;
            if (num4 > imgH) num4 = 0;
            if (num3 + num > imgW) num = imgW - num3;
            if (num4 + num2 > imgH) num2 = imgH - num4;
            byte flip = (partFrame.flip != 0) ? partFrame.flip : partFrame.AD;
            g.drawRegion(image, num3, num4, num, num2, (flip == 1) ? 2 : 0, px + dx, py + dy, 0, totalRotate);
         }
      } catch (Exception e) {
      }
   }

   public void paintBottomEff(mGraphics g, int x, int y, int hOne) {
      if (!this.isHavedata() || (this.typeupdate == 3 && this.Frame == -1) || this.Frame < 0 || this.Frame >= this.listFrame.size()) {
         return;
      }
      FrameEff frameEff = (FrameEff)this.listFrame.elementAt(this.Frame);
      if (frameEff == null) return;
      mImage image = this.getImage();
      if (image == null || image.image == null) return;
      int imgW = (image.width > 0) ? image.width : mImage.getImageWidth(image.image);
      int imgH = (image.height > 0) ? image.height : mImage.getImageHeight(image.image);
      try {
         mVector listPartBottom = (frameEff.listPartBottom != null) ? frameEff.listPartBottom : frameEff.AB;
         if (listPartBottom == null) return;
         int partCount = listPartBottom.size();
         for (int i = 0; i < partCount; i++) {
            PartFrame partFrame = (PartFrame)listPartBottom.elementAt(i);
            if (partFrame == null || this.smallImage == null || partFrame.idSmallImg < 0 || partFrame.idSmallImg >= this.smallImage.length) continue;
            SmallImage smallImage = this.smallImage[partFrame.idSmallImg];
            if (smallImage == null) continue;
            int dx = partFrame.dx;
            int dy = (partFrame.dy != 0) ? partFrame.dy : partFrame.AC;
            int totalRotate = partFrame.rotate;
            if (this.rotate != 0) {
               totalRotate += this.rotate;
               double rad = (double)this.rotate * (3.141592653589793 / 180.0);
               double cos = Math.cos(rad);
               double sin = Math.sin(rad);
               int rdx = (int)Math.round((double)dx * cos - (double)dy * sin);
               int rdy = (int)Math.round((double)dx * sin + (double)dy * cos);
               dx = rdx;
               dy = rdy;
            }
            int num = smallImage.w;
            int num2 = smallImage.h;
            int num3 = smallImage.x;
            int num4 = smallImage.y;
            if (num3 > imgW) num3 = 0;
            if (num4 > imgH) num4 = 0;
            if (num3 + num > imgW) num = imgW - num3;
            if (num4 + num2 > imgH) num2 = imgH - num4;
            int num5 = 0;
            if (hOne == 62 && this.min >= 50) {
               num5 = -8;
            }
            byte flip = (partFrame.flip != 0) ? partFrame.flip : partFrame.AD;
            g.drawRegion(image, num3, num4, num, num2, (flip == 1) ? 2 : 0, x + dx, y + dy + num5, 0, totalRotate);
         }
      } catch (Exception e) {
      }
   }

   public void AB(mGraphics g, int x, int y, int hOne) {
      this.paintBottomEff(g, x, y, hOne);
   }

   public void paintBottomEff(mGraphics g) {
      if (!this.isHavedata() || (this.typeupdate == 3 && this.Frame == -1) || this.Frame < 0 || this.Frame >= this.listFrame.size()) {
         return;
      }
      FrameEff frameEff = (FrameEff)this.listFrame.elementAt(this.Frame);
      if (frameEff == null) return;
      mImage image = this.getImage();
      if (image == null || image.image == null) return;
      int imgW = (image.width > 0) ? image.width : mImage.getImageWidth(image.image);
      int imgH = (image.height > 0) ? image.height : mImage.getImageHeight(image.image);
      try {
         mVector listPartBottom = (frameEff.listPartBottom != null) ? frameEff.listPartBottom : frameEff.AB;
         if (listPartBottom == null) return;
         int partCount = listPartBottom.size();
         for (int i = 0; i < partCount; i++) {
            PartFrame partFrame = (PartFrame)listPartBottom.elementAt(i);
            if (partFrame == null || this.smallImage == null || partFrame.idSmallImg < 0 || partFrame.idSmallImg >= this.smallImage.length) continue;
            SmallImage smallImage = this.smallImage[partFrame.idSmallImg];
            if (smallImage == null) continue;
            int dx = partFrame.dx;
            int dy = (partFrame.dy != 0) ? partFrame.dy : partFrame.AC;
            int totalRotate = partFrame.rotate;
            if (this.rotate != 0) {
               totalRotate += this.rotate;
               double rad = (double)this.rotate * (3.141592653589793 / 180.0);
               double cos = Math.cos(rad);
               double sin = Math.sin(rad);
               int rdx = (int)Math.round((double)dx * cos - (double)dy * sin);
               int rdy = (int)Math.round((double)dx * sin + (double)dy * cos);
               dx = rdx;
               dy = rdy;
            }
            int num = smallImage.w;
            int num2 = smallImage.h;
            int num3 = smallImage.x;
            int num4 = smallImage.y;
            if (num3 > imgW) num3 = 0;
            if (num4 > imgH) num4 = 0;
            if (num3 + num > imgW) num = imgW - num3;
            if (num4 + num2 > imgH) num2 = imgH - num4;
            byte flip = (partFrame.flip != 0) ? partFrame.flip : partFrame.AD;
            g.drawRegion(image, num3, num4, num, num2, (flip == 1) ? 2 : 0, this.x + dx, this.y + dy, 0, totalRotate);
         }
      } catch (Exception e) {
      }
   }

   public void setFrame(int fr) {
      this.Frame = (byte)fr;
   }

   public mImage getImage() {
      long now = GameCanvas.timeNow;
      if (this.cachedImg != null && this.cachedImg.image != null && now - this.lastImgCheck < 1000L) {
         return this.cachedImg;
      }
      if (this.idEffStr == null) {
         this.idEffStr = String.valueOf(this.idEff);
      }
      EffectData obj = (EffectData)ALL_EFF_DATA.get(this.idEffStr);
      if (obj == null) return null;
      obj.count = now / 1000L;
      this.cachedImg = obj.image;
      this.lastImgCheck = now;
      return this.cachedImg;
   }

   public static EffectData readData(byte[] dataeff) {
      return readData(dataeff, true);
   }

   public static EffectData readData(byte[] dataeff, boolean isSave) {
      EffectData effectData = null;
      try {
         ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(dataeff);
         DataInputStream dataInputStream = new DataInputStream(bytearrayinputstream);
         short id = dataInputStream.readShort();
         byte[] data = new byte[dataInputStream.readShort()];
         dataInputStream.read(data);
         byte[] data2 = new byte[dataInputStream.available()];
         dataInputStream.read(data2);
         effectData = (EffectData)ALL_EFF_DATA.get(String.valueOf(id));
         if (effectData == null) {
            effectData = new EffectData();
            ALL_EFF_DATA.put(String.valueOf(id), effectData);
         }
         effectData.data = data;
         effectData.image = mImage.AA(data2);
         effectData.templateNormal = null;
         effectData.templateFlip = null;
         effectData.count = GameCanvas.timeNow / 1000L;
         if (isSave) {
            saveDataSkillEff(dataeff, id);
         }
      } catch (Exception e) {
      }
      return effectData;
   }

   public static void saveDataSkillEff(byte[] dataSave, short id) {
      try {
         CRes.saveRMS("DataSkillEff" + id, dataSave);
      } catch (Exception e) {
      }
   }

   public void setLoop(int loop) {
      this.loop = loop;
   }

   public void update() {
      if (!this.isHavedata()) {
         return;
      }
      int seqLen = (this.sequence != null) ? this.sequence.length : 0;
      try {
         switch (this.typeupdate) {
         case 0:
            this.f++;
            if (seqLen == 0 || this.f >= seqLen) {
               this.wantDestroy = true;
               this.f = 0;
            }
            this.Frame = (seqLen > 0 && this.f < seqLen) ? this.sequence[this.f] : (byte)0;
            break;
         case 1:
            this.f++;
            if (seqLen == 0 || this.f >= seqLen) {
               this.f = 0;
               this.wantDestroy = true;
            }
            this.Frame = (seqLen > 0 && this.f < seqLen) ? this.sequence[this.f] : (byte)0;
            break;
         case 2:
            this.f++;
            if (seqLen > 0 && this.f >= seqLen) {
               this.f = 0;
            }
            if (this.timelive - System.currentTimeMillis() < 0L) {
               this.wantDestroy = true;
            }
            this.Frame = (seqLen > 0 && this.f < seqLen) ? this.sequence[this.f] : (byte)0;
            break;
         case 3:
            this.f++;
            if (seqLen == 0 || this.f >= seqLen) {
               if (this.waitLoop > 0) {
                  if (System.currentTimeMillis() - this.lasttime > (long)(this.waitLoop * 1000)) {
                     this.lasttime = System.currentTimeMillis();
                     this.f = 0;
                  }
               } else {
                  this.f = 0;
               }
            }
            if (seqLen > 0 && this.f < seqLen) {
               this.Frame = this.sequence[this.f];
            } else if (seqLen == 0) {
               this.Frame = 0;
            } else {
               this.Frame = -1;
            }
            break;
         }
      } catch (Exception e) {
      }
   }

   public void AA() {
      this.update();
   }

   public void paintAutoCenter(mGraphics g, int boxX, int boxY, int boxW, int boxH) {
      int targetCenterX = boxX + boxW / 2;
      int targetCenterY = boxY + boxH / 2;

      if (!this.isHavedata() || this.listFrame == null || this.listFrame.size() == 0 || this.smallImage == null) {
         if (AvMain.imgLoadImage != null) {
            AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, targetCenterX, targetCenterY, 0, 3, g);
         }
         return;
      }

      int frameIdx = (int)this.Frame;
      if (frameIdx < 0 || frameIdx >= this.listFrame.size()) {
         frameIdx = 0;
      }

      FrameEff frameEff = (FrameEff)this.listFrame.elementAt(frameIdx);
      if (frameEff == null) {
         return;
      }

      int minX = 100000, maxX = -100000;
      int minY = 100000, maxY = -100000;
      boolean hasPart = false;

      mVector listTop = frameEff.listPartTop;
      if (listTop != null) {
         for (int i = 0; i < listTop.size(); i++) {
            PartFrame p = (PartFrame)listTop.elementAt(i);
            if (p != null && p.idSmallImg >= 0 && p.idSmallImg < this.smallImage.length && this.smallImage[p.idSmallImg] != null) {
               SmallImage sm = this.smallImage[p.idSmallImg];
               int px = p.dx;
               int py = (p.dy != 0) ? p.dy : p.AC;
               int pw = sm.w;
               int ph = sm.h;
               if (px < minX) minX = px;
               if (px + pw > maxX) maxX = px + pw;
               if (py < minY) minY = py;
               if (py + ph > maxY) maxY = py + ph;
               hasPart = true;
            }
         }
      }

      mVector listBottom = (frameEff.listPartBottom != null) ? frameEff.listPartBottom : frameEff.AB;
      if (listBottom != null) {
         for (int i = 0; i < listBottom.size(); i++) {
            PartFrame p = (PartFrame)listBottom.elementAt(i);
            if (p != null && p.idSmallImg >= 0 && p.idSmallImg < this.smallImage.length && this.smallImage[p.idSmallImg] != null) {
               SmallImage sm = this.smallImage[p.idSmallImg];
               int px = p.dx;
               int py = (p.dy != 0) ? p.dy : p.AC;
               int pw = sm.w;
               int ph = sm.h;
               if (px < minX) minX = px;
               if (px + pw > maxX) maxX = px + pw;
               if (py < minY) minY = py;
               if (py + ph > maxY) maxY = py + ph;
               hasPart = true;
            }
         }
      }

      int drawX = targetCenterX;
      int drawY = targetCenterY;

      if (hasPart && minX < maxX && minY < maxY) {
         int relMidX = (minX + maxX) / 2;
         int relMidY = (minY + maxY) / 2;
         drawX = targetCenterX - relMidX;
         drawY = targetCenterY - relMidY;
      }

      g.setClip(boxX + 1, boxY + 1, boxW - 2, boxH - 2);
      this.paintBottomEff(g, drawX, drawY, 0);
      this.paintTopEff(g, drawX, drawY, 0);
      GameCanvas.resetTrans(g);
   }
}
