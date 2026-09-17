public final class MainBuff {
   public MainObject AA;
   public short IdBuff;
   public short AC;
   public byte AD;
   public int timeBuff;
   private int AL;
   private int AM;
   private int f;
   private int AO = 0;
   private long timeBegin;
   private byte AQ = 1;
   private FrameImage fraBuff;
   private FrameImage AS;
   private int AT;
   private int AU;
   private FrameImage[] AV;
   public boolean isRemove = false;
   public boolean AG = false;
   public mVector AH = new mVector("MainBuff.vecInfoAtt");
   private mVector vecEffBuff = new mVector("MainBuff.vecEffBuff");
   public short AI;
   public short AJ;
   public short AK;
   private int[] AX;
   private int AY;
   private int AZ;
   private int BA;

   public MainBuff(short var1) {
      this.IdBuff = var1;
      this.AD = 0;
      this.AQ = 6;
      byte var2 = 12;
      Point var3;
      int var5;
      switch(this.IdBuff) {
      case 1002:
         this.fraBuff = new FrameImage(110, 5, 5);
         if (GameCanvas.isLowGraOrWP_PvP()) {
            var2 = 6;
         }

         for(var5 = 0; var5 < var2; ++var5) {
            (var3 = new Point(CRes.random_Am_0(22), 8 - CRes.random(18))).frame = CRes.random(this.fraBuff.nFrame);
            var3.x2 = var3.x;
            var3.y2 = var3.y;
            var3.vx = 0;
            var3.vy = -CRes.random(2, 4);
            var3.fRe = CRes.random(15, 20);
            this.vecEffBuff.addElement(var3);
         }

         return;
      case 1003:
         this.fraBuff = new FrameImage(90, 26, 19);
         return;
      case 1010:
      case 1011:
      case 1012:
      case 1013:
      case 1014:
         this.fraBuff = new FrameImage(176, 3, 25, 1);
         var2 = 16;
         if (GameCanvas.isLowGraOrWP_PvP()) {
            var2 = 8;
         }

         this.AO = var2;

         for(var5 = 0; var5 < var2; ++var5) {
            var3 = new Point();
            this.createPointNewBuff(var3);
            var3.vy = -5;
            this.vecEffBuff.addElement(var3);
         }

         return;
      case 1019:
      case 1020:
      case 1021:
      case 1022:
      case 1023:
         if (var1 == 1019) {
            this.AM = -7886265;
         } else if (var1 == 1020) {
            this.AM = -4816733;
         } else if (var1 == 1021) {
            this.AM = -1992897;
         } else if (var1 == 1022) {
            this.AM = -9466447;
         } else if (var1 == 1023) {
            this.AM = -4970203;
         }

         var2 = 16;
         if (GameCanvas.isLowGraOrWP_PvP()) {
            var2 = 8;
         }

         this.AO = var2;

         for(var5 = 0; var5 < var2; ++var5) {
            (var3 = new Point()).x = CRes.random_Am_0(15);
            var3.y = 10 - CRes.random(40);
            var3.dis = 1 + CRes.random(3);
            var3.vy = -4;
            this.vecEffBuff.addElement(var3);
         }

         return;
      case 2004:
         this.fraBuff = new FrameImage(mImage.createImage("/test_eff/skill3/eff.png"), 7);
         return;
      case 2008:
         this.fraBuff = new FrameImage(105, 5, 5);
         var2 = 16;
         if (GameCanvas.isLowGraOrWP_PvP()) {
            var2 = 8;
         }

         for(var5 = 0; var5 < var2; ++var5) {
            var3 = new Point();
            var3.frame = CRes.random(4);
            var3.x = -16 + ((var5 % 8 + 1) / 2 << 3);
            var3.y = 0;
            if (var5 % 2 == 1) {
               var3.y = -50;
            }

            var3.x2 = var3.x;
            var3.y2 = var3.y;
            var3.vx = 0;
            if (var5 % 2 == 1) {
               var3.vy = 3;
            } else {
               var3.vy = -3;
            }

            var3.fRe = 20;
            if (var5 >= 8) {
               var3.y = -25;
               var3.f = var3.fRe / 2;
            }

            this.vecEffBuff.addElement(var3);
         }

         return;
      case 2010:
         this.fraBuff = new FrameImage(105, 5, 5);
         this.AS = new FrameImage(110, 5, 5);
         var2 = 16;
         if (GameCanvas.isLowGraOrWP_PvP()) {
            var2 = 8;
         }

         for(var5 = 0; var5 < var2; ++var5) {
            var3 = new Point();
            var3.x = -16 + ((var5 / 2 + 1) / 2 << 3);
            var3.y = 0;
            if (var5 % 2 == 1) {
               var3.dis = 1;
               var3.y = -50;
               var3.frame = CRes.random(this.AS.nFrame);
            } else {
               var3.dis = 0;
               var3.frame = CRes.random(this.fraBuff.nFrame);
            }

            var3.x2 = var3.x;
            var3.y2 = var3.y;
            var3.vx = 0;
            if (var5 % 2 == 1) {
               var3.vy = 5;
            } else {
               var3.vy = -5;
            }

            var3.fRe = 10;
            this.vecEffBuff.addElement(var3);
         }

         return;
      case 2017:
         this.AQ = 2;
         this.AL = 5;
         this.fraBuff = new FrameImage(180, 32, 63);
         return;
      case 2018:
      case 2019:
      case 2020:
         this.AD = -1;
         this.AQ = 3;
         this.fraBuff = new FrameImage(203, 33, 24);
         return;
      case 2028:
         this.fraBuff = new FrameImage(249, 42, 19);
         this.AD = -1;
         return;
      case 2032:
         this.fraBuff = new FrameImage(248, 40, 15);
         this.AD = -1;
         return;
      case 2035:
         this.fraBuff = new FrameImage(308, 43, 30);
         this.AQ = 2;
         this.AD = -1;
         return;
      case 2042:
         this.fraBuff = new FrameImage(250, 57, 57);
         this.AQ = 4;
         this.AX = new int[]{0, 1, 2, 1};
         this.AL = 10;
         return;
      case 2053:
         this.AV = new FrameImage[]{new FrameImage(360, 32, 40), new FrameImage(361, 32, 40), new FrameImage(362, 32, 40), new FrameImage(363, 32, 40), new FrameImage(364, 32, 40), new FrameImage(365, 32, 40)};
         this.AQ = 2;
         this.AD = -1;
         this.AX = new int[]{0, 1, 2, 3, 4, 5};
         return;
      case 2054:
         this.fraBuff = new FrameImage(mImage.createImage("/eff/n2.png"), 66, 15, 55, 1);
         this.AS = new FrameImage(mImage.createImage("/eff/n3.png"), 67, 3, 25, 1);
         this.AB();
         return;
      case 2057:
         return;
      case 2059://Bộc phá
         this.fraBuff = new FrameImage(401, 46, 21);
         this.AD = -1;
         return;
      case 2060:
         this.fraBuff = new FrameImage(432, 46, 21);
         this.AS = new FrameImage(400, 3);
         this.AU = 0;
      default:
         return;
      case 2067:
         this.fraBuff = new FrameImage(425, 5);
         this.AQ = 3;
         this.AD = -1;
         return;
      case 2070:
         this.fraBuff = new FrameImage(424, 3);
         this.AQ = 3;
         this.AD = -1;
      }
   }

   private void AB() {
      for(int var1 = 0; var1 < 10; ++var1) {
         Point var2;
         (var2 = new Point(this.AY + CRes.random_Am_0(20), this.AZ - CRes.random(30) + 10)).vy = -2 - CRes.random(4);
         var2.dis = 0;
         var2.frame = CRes.random(this.AS.nFrame);
         var2.fRe = CRes.random(12, 20);
         this.vecEffBuff.addElement(var2);
      }

   }

   public final void AA(int var1) {
      this.timeBegin = GameCanvas.timeNow;
      this.timeBuff = var1;
   }

   public final void setYlech(MainObject obj) {
      if (obj != null) {
         switch(this.IdBuff) {
         case 1002:
            this.AL = 0;
            return;
         case 1003:
            this.AL = -obj.hOne / 2 + 5;
            return;
         case 2053:
            this.AL = -obj.hOne / 2 + 5;
         }
      }

   }

   public final void paint(mGraphics g, int x, int y) {
      int var4;
      Point point;
      int var6;
      Point point2;
      switch(this.IdBuff) {
      case 1002:
      case 2008:
         for(var4 = 0; var4 < this.vecEffBuff.size(); ++var4) {
            point = (Point)this.vecEffBuff.elementAt(var4);
            this.fraBuff.drawFrame((point.frame + point.f) % this.fraBuff.nFrame, x + point.x, y + point.y, 0, 3, g);
         }

         return;
      case 1003:
         this.fraBuff.drawFrame(GameCanvas.gameTick / this.AQ % this.fraBuff.nFrame, x, y + this.AL, 0, 3, g);
         return;
      case 1010:
      case 1011:
      case 1012:
      case 1013:
      case 1014:
         for(var4 = 0; var4 < this.vecEffBuff.size(); ++var4) {
            point = (Point)this.vecEffBuff.elementAt(var4);
            this.fraBuff.drawFrameNew(point.frame + point.dis * 5, x + point.x, y + point.y, 0, 3, g);
         }

         return;
      case 1019:
      case 1020:
      case 1021:
      case 1022:
      case 1023:
         g.setColor(this.AM);

         for(var6 = 0; var6 < this.vecEffBuff.size(); ++var6) {
            point2 = (Point)this.vecEffBuff.elementAt(var6);
            g.fillRect(x + point2.x, y + point2.y, point2.dis, point2.dis);
         }

         return;
      case 2004:
         this.fraBuff.drawFrame(this.AT, x, y, 0, 3, g);
         return;
      case 2010:
         for(var4 = 0; var4 < this.vecEffBuff.size(); ++var4) {
            if ((point = (Point)this.vecEffBuff.elementAt(var4)).dis == 0) {
               this.fraBuff.drawFrame((point.frame + point.f) % this.fraBuff.nFrame, x + point.x, y + point.y, 0, 3, g);
            } else {
               this.AS.drawFrame((point.frame + point.f) % this.AS.nFrame, x + point.x, y + point.y, 0, 3, g);
            }
         }

         return;
      case 2017:
         if ((var4 = GameCanvas.gameTick / this.AQ % (this.fraBuff.nFrame + 1)) < this.fraBuff.nFrame) {
            this.fraBuff.drawFrame(var4, x, y + this.AL, 0, 33, g);
            return;
         }
         break;
      case 2018:
         if (GameCanvas.gameTick % 24 < 8) {
            this.fraBuff.drawFrame(GameCanvas.gameTick / this.AQ % 2, x, y + this.AL, 0, 3, g);
            return;
         }
         break;
      case 2019:
         if (GameCanvas.gameTick % 24 >= 16) {
            this.fraBuff.drawFrame(4 + GameCanvas.gameTick / this.AQ % 2, x, y + this.AL, 0, 3, g);
            return;
         }
         break;
      case 2020:
         if (GameCanvas.gameTick % 24 >= 8 && GameCanvas.gameTick % 24 < 16) {
            this.fraBuff.drawFrame(2 + GameCanvas.gameTick / this.AQ % 2, x, y + this.AL, 0, 3, g);
            return;
         }
         break;
      case 2028:
         for(var6 = 0; var6 < this.vecEffBuff.size(); ++var6) {
            point2 = (Point)this.vecEffBuff.elementAt(var6);
            if (this.fraBuff.getImageFrame() != null) {
               g.drawRegion(this.fraBuff.getImageFrame(), 0, point2.frame * this.fraBuff.frameHeight, this.fraBuff.frameWidth, 10, 0, x + point2.x, y + point2.y - 9 + 3, 33);
            }
         }

         return;
      case 2032:
         for(var6 = 0; var6 < this.vecEffBuff.size(); ++var6) {
            point2 = (Point)this.vecEffBuff.elementAt(var6);
            if (this.fraBuff.getImageFrame() != null) {
               g.drawRegion(this.fraBuff.getImageFrame(), 0, point2.frame * this.fraBuff.frameHeight, this.fraBuff.frameWidth, 8, 0, x + point2.x, y + point2.y - 7 + 3, 33);
            }
         }

         return;
      case 2035:
      case 2067:
      case 2070:
         this.fraBuff.drawFrame(GameCanvas.gameTick / this.AQ % this.fraBuff.nFrame, x, y + this.AL, 0, 3, g);
         return;
      case 2042:
         var6 = GameCanvas.gameTick / this.AQ % this.AX.length;
         this.fraBuff.drawFrame(this.AX[var6], x, y + this.AL, 0, 33, g);
         return;
      case 2053:
         var4 = GameCanvas.gameTick / this.AQ % this.AX.length;
         this.AV[this.AX[var4]].drawFrame(0, x, y + this.AL, 0, 3, g);
         return;
      case 2054:
         for(var4 = 0; var4 < this.vecEffBuff.size(); ++var4) {
            if ((point = (Point)this.vecEffBuff.elementAt(var4)).dis == 1) {
               this.fraBuff.drawFrameNew(point.f / 2 % this.fraBuff.nFrame, point.x, point.y, 0, 33, g);
            } else if (point.dis == 0) {
               this.AS.drawFrameNew((point.frame + point.f / 2) % this.AS.nFrame, point.x, point.y, 0, 3, g);
            }
         }

         this.AY = x;
         this.AZ = y;
         return;
      case 2059://Bộc phá
         for(var4 = 0; var4 < this.vecEffBuff.size(); ++var4) {
            point = (Point)this.vecEffBuff.elementAt(var4);
            if (this.fraBuff.getImageFrame() != null) {
               g.drawRegion(this.fraBuff.getImageFrame(), 0, point.frame * this.fraBuff.frameHeight, this.fraBuff.frameWidth, 21, 0, x + point.x, y + point.y + 5, 33);
            }
         }

         return;
      case 2060:
         if (this.AS != null) {
            this.AS.drawFrame(this.AU, this.AA.x, this.AA.y - this.AA.hOne, 0, 3, g);
         }

         for(var4 = 0; var4 < this.vecEffBuff.size(); ++var4) {
            point = (Point)this.vecEffBuff.elementAt(var4);
            if (this.fraBuff.getImageFrame() != null) {
               g.drawRegion(this.fraBuff.getImageFrame(), 0, point.frame * this.fraBuff.frameHeight, this.fraBuff.frameWidth, 10, 0, x + point.x, y + point.y - 5, 33);
            }
         }
      }

   }

   public final void AA(mGraphics var1, int var2, int var3, MainObject var4) {
      int var7;
      Point var8;
      switch(this.IdBuff) {
      case 2028:
         for(var7 = 0; var7 < this.vecEffBuff.size(); ++var7) {
            var8 = (Point)this.vecEffBuff.elementAt(var7);
            if (this.fraBuff.getImageFrame() != null) {
               var1.drawRegion(this.fraBuff.getImageFrame(), 0, var8.frame * this.fraBuff.frameHeight + 10, this.fraBuff.frameWidth, 9, 0, var2 + var8.x, var3 + var8.y + 3, 33);
            }
         }

         return;
      case 2032:
         for(var7 = 0; var7 < this.vecEffBuff.size(); ++var7) {
            var8 = (Point)this.vecEffBuff.elementAt(var7);
            if (this.fraBuff.getImageFrame() != null) {
               var1.drawRegion(this.fraBuff.getImageFrame(), 0, var8.frame * this.fraBuff.frameHeight + 8, this.fraBuff.frameWidth, 7, 0, var2 + var8.x, var3 + var8.y + 3, 33);
            }
         }

         return;
      case 2053:
         var7 = GameCanvas.gameTick / this.AQ % this.AX.length;
         this.AV[this.AX[var7]].drawFrame(1, var2, var3 + this.AL, 0, 3, var1);
         return;
      case 2060:
         if (this.AS != null) {
            this.AS.drawFrame(this.AU, var4.x, var4.y - var4.hOne, 0, 3, var1);
         }

         for(int var5 = 0; var5 < this.vecEffBuff.size(); ++var5) {
            Point var6 = (Point)this.vecEffBuff.elementAt(var5);
            if (this.fraBuff.getImageFrame() != null) {
               var1.drawRegion(this.fraBuff.getImageFrame(), 0, var6.frame * this.fraBuff.frameHeight, this.fraBuff.frameWidth, 21, 0, var2 + var6.x, var3 + var6.y + 5, 33);
            }
         }
      default:
      }
   }

   public final void AA() {
      Point var10000;
      int var1;
      Point var2;
      Point var3;
      label213:
      switch(this.IdBuff) {
      case 1002:
      case 2008:
      case 2010:
         var1 = 0;

         while(true) {
            if (var1 >= this.vecEffBuff.size()) {
               break label213;
            }

            (var2 = (Point)this.vecEffBuff.elementAt(var1)).update();
            if (var2.f >= var2.fRe) {
               var2.f = 0;
               var2.x = var2.x2;
               var2.y = var2.y2;
            }

            ++var1;
         }
      case 1010:
      case 1011:
      case 1012:
      case 1013:
      case 1014:
         var1 = 0;

         while(true) {
            if (var1 >= this.vecEffBuff.size()) {
               break label213;
            }

            var10000 = var2 = (Point)this.vecEffBuff.elementAt(var1);
            var10000.x += var2.vx;
            var2.y += var2.vy;
            if (var2.y <= -90) {
               if (var1 >= this.AO) {
                  this.vecEffBuff.removeElement(var1);
                  --var1;
               } else {
                  this.createPointNewBuff(var2);
               }
            }

            ++var1;
         }
      case 1019:
      case 1020:
      case 1021:
      case 1022:
      case 1023:
         var1 = 0;

         while(true) {
            if (var1 >= this.vecEffBuff.size()) {
               break label213;
            }

            var10000 = var2 = (Point)this.vecEffBuff.elementAt(var1);
            var10000.x += var2.vx;
            var2.y += var2.vy;
            if (var2.y <= -70) {
               if (var1 >= this.AO) {
                  this.vecEffBuff.removeElement(var1);
                  --var1;
               } else {
                  var2.x = CRes.random_Am_0(15);
                  var2.y = 10 - CRes.random(40);
                  var2.dis = 1 + CRes.random(3);
               }
            }

            ++var1;
         }
      case 2004:
         if (GameCanvas.gameTick % 3 == 0) {
            ++this.AT;
            if (this.AT > this.fraBuff.maxNumFrame) {
               this.AT = 0;
            }
         }
         break;
      case 2017:
         if (GameCanvas.gameTick / this.AQ % (this.fraBuff.nFrame + 1) == this.fraBuff.nFrame) {
            if (this.AD != -1) {
               this.AD = -1;
            } else {
               this.AD = 0;
            }
         }
         break;
      case 2028:
      case 2032:
         ++this.f;

         for(var1 = 0; var1 < this.vecEffBuff.size(); ++var1) {
            if ((var2 = (Point)this.vecEffBuff.elementAt(var1)).dis == 1) {
               ++var2.f;
               if (var2.f >= var2.fRe) {
                  this.vecEffBuff.removeElement(var2);
                  --var1;
               }
            }
         }

         if (this.f % 12 == 0) {
            (var3 = new Point()).y = 4;
            var3.frame = 0;
            var3.dis = 1;
            var3.fRe = 8;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 2) {
            (var3 = new Point()).y = -2;
            var3.frame = 0;
            var3.dis = 1;
            var3.fRe = 7;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 4) {
            (var3 = new Point()).y = -8;
            var3.frame = 1;
            var3.dis = 1;
            var3.fRe = 6;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 6) {
            (var3 = new Point()).y = -14;
            var3.frame = 2;
            var3.dis = 1;
            var3.fRe = 5;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 8) {
            (var3 = new Point()).y = -20;
            var3.frame = 3;
            var3.dis = 1;
            var3.fRe = 4;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 10) {
            (var3 = new Point()).y = -26;
            var3.frame = 3;
            var3.dis = 1;
            var3.fRe = 3;
            this.vecEffBuff.addElement(var3);
         }
         break;
      case 2054:
         ++this.f;
         if (GameCanvas.gameTick % 5 == 0) {
            this.AB();
         }

         this.AY += this.BA;
         if (this.f == 6) {
            (var3 = new Point(this.AY, this.AZ)).vy = 0;
            var3.dis = 1;
            var3.frame = 0;
            var3.fRe = 10;
            this.vecEffBuff.addElement(var3);
         }

         if (this.f == 8) {
            this.BA = -25;
         }

         for(var1 = 0; var1 < this.vecEffBuff.size(); ++var1) {
            (var2 = (Point)this.vecEffBuff.elementAt(var1)).update();
            if (var2.dis == 1) {
               var2.vy = this.BA;
            }

            if (var2.f >= var2.fRe) {
               this.vecEffBuff.removeElement(var2);
               --var1;
            }
         }

         if (this.f < 10 && this.f % 3 == 2) {
            for(var1 = 0; var1 < 4; ++var1) {
               (var2 = new Point(this.AY + CRes.random_Am_0(20), this.AZ - CRes.random(30) + 10)).vy = -2 - CRes.random(4);
               var2.dis = 0;
               var2.frame = CRes.random(this.AS.nFrame);
               var2.fRe = CRes.random(12, 20);
               this.vecEffBuff.addElement(var2);
            }
         }
         break;
      case 2059://Bộc phá
         ++this.f;

         for(var1 = 0; var1 < this.vecEffBuff.size(); ++var1) {
            if ((var2 = (Point)this.vecEffBuff.elementAt(var1)).dis == 1) {
               ++var2.f;
               if (var2.f >= var2.fRe) {
                  this.vecEffBuff.removeElement(var2);
                  --var1;
               }
            }
         }

         if (this.f % 12 == 0) {
            (var3 = new Point()).y = 4;
            var3.frame = 0;
            var3.dis = 1;
            var3.fRe = 8;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 2) {
            (var3 = new Point()).y = 4;
            var3.frame = 0;
            var3.dis = 1;
            var3.fRe = 7;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 4) {
            (var3 = new Point()).y = 4;
            var3.frame = 1;
            var3.dis = 1;
            var3.fRe = 6;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 6) {
            (var3 = new Point()).y = 4;
            var3.frame = 2;
            var3.dis = 1;
            var3.fRe = 5;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 8) {
            (var3 = new Point()).y = 4;
            var3.frame = 3;
            var3.dis = 1;
            var3.fRe = 4;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 10) {
            (var3 = new Point()).y = 4;
            var3.frame = 3;
            var3.dis = 1;
            var3.fRe = 3;
            this.vecEffBuff.addElement(var3);
         }
         break;
      case 2060:
         if (GameCanvas.gameTick % 5 == 0) {
            ++this.AU;
            if (this.AU > 2) {
               this.AU = 0;
            }
         }

         ++this.f;

         for(var1 = 0; var1 < this.vecEffBuff.size(); ++var1) {
            if ((var2 = (Point)this.vecEffBuff.elementAt(var1)).dis == 1) {
               ++var2.f;
               if (var2.f >= var2.fRe) {
                  this.vecEffBuff.removeElement(var2);
                  --var1;
               }
            }
         }

         if (this.f % 12 == 0) {
            (var3 = new Point()).y = 4;
            var3.frame = 0;
            var3.dis = 1;
            var3.fRe = 8;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 2) {
            (var3 = new Point()).y = 4;
            var3.frame = 0;
            var3.dis = 1;
            var3.fRe = 7;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 4) {
            (var3 = new Point()).y = 4;
            var3.frame = 1;
            var3.dis = 1;
            var3.fRe = 6;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 6) {
            (var3 = new Point()).y = 4;
            var3.frame = 2;
            var3.dis = 1;
            var3.fRe = 5;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 8) {
            (var3 = new Point()).y = 4;
            var3.frame = 3;
            var3.dis = 1;
            var3.fRe = 4;
            this.vecEffBuff.addElement(var3);
         } else if (this.f % 12 == 10) {
            (var3 = new Point()).y = 4;
            var3.frame = 3;
            var3.dis = 1;
            var3.fRe = 3;
            this.vecEffBuff.addElement(var3);
         }
      }

      if (GameCanvas.timeNow - this.timeBegin > (long)this.timeBuff) {
         this.isRemove = true;
      }

   }

   private Point createPointNewBuff(Point var1) {
      var1.frame = CRes.random(5);
      var1.x = CRes.random_Am_0(15);
      var1.y = 10 - CRes.random(40);
      if (this.IdBuff == 1010) {
         var1.dis = 0;
      } else if (this.IdBuff == 1011) {
         var1.dis = 1;
      } else if (this.IdBuff == 1012) {
         var1.dis = 3;
      } else if (this.IdBuff == 1013) {
         var1.dis = 2;
      } else if (this.IdBuff == 1014) {
         var1.dis = 4;
      }

      return var1;
   }
}
