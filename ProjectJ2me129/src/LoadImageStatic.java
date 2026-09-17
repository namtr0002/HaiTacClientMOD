public class LoadImageStatic {
   public int AA;
   public int AB;
   public int AC;
   public int AD;
   public int AE;
   public boolean AF;
   private boolean AG;

   public LoadImageStatic(int var1) {
      this.AA = 0;
      this.AC = 2;
      this.AF = false;
      this.AG = false;
      this.AB = var1;
   }

   public final void AA(String var1, mFont var2) {
      int var3 = var2.getWidth(var1);
      this.AD = 0;
      this.AE = 0;
      this.AG = false;
      if (var3 > this.AB) {
         this.AA = var3 - this.AB + 20;
         this.AF = true;
      } else {
         this.AF = false;
      }
   }

   public final void AA() {
      if (this.AF) {
         ++this.AE;
         if (this.AE > 10) {
            if (!this.AG) {
               this.AD += this.AC;
            } else {
               this.AD -= this.AC;
            }
         }

         if (this.AD > this.AA || this.AD < -10) {
            this.AG = !this.AG;
            return;
         }
      } else {
         this.AD = 0;
         this.AE = 0;
      }

   }

   public LoadImageStatic() {
   }

   public static void LoadAllImage() {
      SmallImage.loadBigRMS();
      Interface_Game.imgInfoServer = LoadNewInterface("/backinfo.png");
      AvMain.imgBgnum = LoadNewInterface("/bgnum.png");
      AvMain.imgBgnum2 = LoadNewInterface("/bgnum2.png");
      MainObject.imgShadow = LoadNewInterface("/shadow.png");
      AvMain.imgCombo = LoadNewInterface("/combo.png");
      AvMain.imgNenfocus = LoadNewInterface("/nenfocus.png");
      AvMain.imgShadowSmall = LoadNewInterface("/shadowsmall.png");
      AvMain.imgDelay = LoadNewInterface("/delayskill.png");
      AvMain.fraIconMenu = new FrameImage(mImage.createImage("/point/iconmenu.png"), 30, 30);
      AvMain.fraIconHome = new FrameImage(mImage.createImage("/point/iconhome.png"), 30, 30);
      AvMain.fraEffOpen = new FrameImage(mImage.createImage("/interface/effopen.png"), 25, 25);
      AvMain.fraEffDasieucap = new FrameImage(mImage.createImage("/interface/eff_dasieucap.png"), 20, 20);

      try {
         AvMain.fraPlus = new FrameImage(mImage.createImage("/interface/iconplus.png"), 24, 24);
      } catch (Exception var1) {
      }

      AvMain.fraUniform = new FrameImage(mImage.createImage("/interface/coloruniform.png"), 8, 8);
      AvMain.fraBtLogin = new FrameImage(mImage.createImage("/interface/btlogin.png"), 19, 19);
      AvMain.fraNew = new FrameImage(mImage.createImage("/interface/franew.png"), 27, 21);
      AvMain.fraDelay2 = new FrameImage(LoadNewInterface("/delayskill3.png"), 24, 24);
      AvMain.fraDelay = new FrameImage(LoadNewInterface("/delayskill2.png"), 32, 32);
      GameScreen.interfaceGame.load_Image_Pointer();
      Interface_Game.imgIconMPHP = mImage.createImage("/interface/iconmphp.png");
      Interface_Game.imgIconMPHP2 = mImage.createImage("/interface/iconmphp2.png");
      MsgDialog.fraImgWaiting = new FrameImage(mImage.createImage("/interface/waiting2.png"), 21, 21);
      AvMain.imgSelect = mImage.createImage("/interface/selected_hand.png");
      AvMain.imgHotKey = mImage.createImage("/interface/hotkey.png");
      AvMain.imgEffCur = mImage.createImage("/interface/effcur.png");
      Interface_Game.imgBorderNoti = mImage.createImage("/interface/bordernoti.png");
      Interface_Game.imgBorderNoti2 = mImage.createImage("/interface/bordernoti2.png");
      Interface_Game.fraBorderNoti = new FrameImage(mImage.createImage("/interface/bordernoti3.png"), 27, 27);
      Interface_Game.fraBorderNoti4 = new FrameImage(mImage.createImage("/interface/bordernoti4.png"), 21, 17);
      if (!GameCanvas.isTouch) {
         AvMain.imgMess = mImage.createImage("/interface/mess2.png");
      } else {
         AvMain.imgMess = mImage.createImage("/interface/mess.png");
      }

      AvMain.imgChat = mImage.createImage("/interface/chat2.png");
      AvMain.imgBorderCombo = mImage.createImage("/interface/bordercombo.png");
      AvMain.imgDaKham = mImage.createImage("/interface/slot.png");
      AvMain.imgDonateClan = mImage.createImage("/interface/donateclan.png");
      AvMain.imgLvClan = mImage.createImage("/interface/lvclan.png");
      AvMain.imgBannerClan = mImage.createImage("/interface/bannerclan.png");
      AvMain.imgPlusClan = mImage.createImage("/interface/plusclan.png");
      AvMain.imgBorderIcon = mImage.createImage("/interface/bordericon.png");
      AvMain.imgReward = mImage.createImage("/interface/reward.png");
      AvMain.imgTabClan = mImage.createImage("/interface/tabclan.png");
      AvMain.imgChatClan = mImage.createImage("/interface/chatclan.png");
      AvMain.fraIconNpc = new FrameImage(mImage.createImage("/interface/iconnpc.png"), 18, 18);
      AvMain.fraShadowFocus = new FrameImage(mImage.createImage("/interface/shadowfocus.png"), 30, 16);
      AvMain.imgEye = mImage.createImage("/interface/eye.png");
      AvMain.imgXp = mImage.createImage("/interface/iconxp.png");
      AvMain.imgcheck = mImage.createImage("/interface/iconcheck.png");
      AvMain.fraIconfocus = new FrameImage(mImage.createImage("/interface/iconfocus.png"), 14, 14);
      AvMain.fraStatusOnline = new FrameImage(mImage.createImage("/interface/statusonline.png"), 5, 5);
      AvMain.imgIconDel = mImage.createImage("/interface/icondel.png");
      AvMain.imgDieChar = mImage.createImage("/interface/icondie.png");
      AvMain.imgBeri = mImage.createImage("/interface/wanted14.png");
      AvMain.imgArrowListServer = mImage.createImage("/interface/arrowlist.png");
      AvMain.imgInfoLock = mImage.createImage("/interface/infolock.png");
      AvMain.imgInfoClass = mImage.createImage("/interface/infoclass.png");
      AvMain.imgInfoStar = mImage.createImage("/interface/infostar.png");
      AvMain.imgBannerRuong = mImage.createImage("/interface/bannerruong0.png");
      AvMain.imgLoadImage = new FrameImage(mImage.createImage("/interface/loadimg.png"), 16, 16);
      AvMain.fraComboSkill = new FrameImage(mImage.createImage("/interface/g0.png"), 14, 14);
      AvMain.imgButton = new mImage[2];

      int i;
      for(i = 0; i < AvMain.imgButton.length; ++i) {
         AvMain.imgButton[i] = mImage.createImage("/interface/button" + i + ".png");
      }

      AvMain.imgLock = mImage.createImage("/interface/lock.png");
      AvMain.imgHinhnhan = mImage.createImage("/interface/hinhnhan.png");
      AvMain.imgLvDevilSkill = mImage.createImage("/interface/lvdevilskill.png");
      MainTab.mImgTab = new mImage[2];

      for(i = 0; i < MainTab.mImgTab.length; ++i) {
         MainTab.mImgTab[i] = mImage.createImage("/interface/tab" + i + ".png");
      }

      MainTab.fraCloseTab = new FrameImage(mImage.createImage("/point/closetab.png"), 18, 18);
      MainTab.fraCloseTab2 = new FrameImage(mImage.createImage("/interface/xclose.png"), 18, 18);
      MainTab.fraCloseTab3 = new FrameImage(mImage.createImage("/point/closetab3.png"), 18, 18);
      MainTab.fraCmdMo = new FrameImage(mImage.createImage("/point/cmd_mo.png"), 71, 26);
      AvMain.fraMoney = new FrameImage(mImage.createImage("/interface/money.png"), 12, 12);
      AvMain.fraPk = new FrameImage(mImage.createImage("/interface/iconpk.png"), 12, 12);
      AvMain.fraPk2 = new FrameImage(mImage.createImage("/interface/iconpk2.png"), 16, 16);
      AvMain.fraPirate = new FrameImage(mImage.createImage("/interface/iconpirate.png"), 11, 14);
      AvMain.fraDiePlayer = new FrameImage(mImage.createImage("/interface/icondie.png"), 38, 44);
      AvMain.fraQuest = new FrameImage(mImage.createImage("/interface/quest.png"), 13, 19);
      AvMain.fratf = new FrameImage(mImage.createImage("/interface/tf.png"), 6, 6);
      AvMain.fraIconWanted = new FrameImage(mImage.createImage("/interface/iconwanted.png"), 16, 16);
      AvMain.fratf1 = new FrameImage(mImage.createImage("/interface/tf1.png"), 6, 6);
      AvMain.fraCheck = new FrameImage(mImage.createImage("/interface/check.png"), 13, 13);
      AvMain.fraTwoTab = new FrameImage(mImage.createImage("/interface/two_tab.png"), 11, 16);
      AvMain.fraIconClan = new FrameImage(mImage.createImage("/interface/iconclan.png"), 23, 16);
      AvMain.fraBorderClan = new FrameImage(mImage.createImage("/interface/borderclan.png"), 4, 4);
      AvMain.fraBorderClan2 = new FrameImage(mImage.createImage("/interface/borderclan2.png"), 20, 26, 5);
      AvMain.fraEffItem = new FrameImage(mImage.createImage("/interface/effitem.png"), 7, 7);
      AvMain.fraEffItem2 = new FrameImage(mImage.createImage("/interface/effitem_2.png"), 9, 9);
      AvMain.fraBorderSkill = new FrameImage(mImage.createImage("/interface/borderskill.png"), 26, 26);
      AvMain.fraEventMoon = new FrameImage(253, 20, 20, 3);

      for(i = 0; i < PopupChat.mPopup.length; ++i) {
         PopupChat.mPopup[i] = mImage.createImage("/interface/chatpopup" + i + ".png");
      }

      AvMain.mImgThanhTich = new mImage[2];

      for(i = 0; i < 2; ++i) {
         AvMain.mImgThanhTich[i] = mImage.createImage("/interface/thanhtich" + i + ".png");
      }

      Item_Drop.imgMainIconXp = new MainImage(mImage.createImage("/interface/iconxp.png"));
      MainEvent.fraEvent = new FrameImage(mImage.createImage("/interface/event.png"), 22, 22);
      MainEvent.imgNew = mImage.createImage("/interface/new.png");
      MainItem.imgColorItem = mImage.createImage("/interface/coloritem.png");
      Boat.fraPirateUnity = new FrameImage(mImage.createImage("/bg/pirate.png"), 18, 15);
      Boat.imgShip = mImage.createImage("/bg/ship.png");
      if (GameCanvas.isTouch) {
         AvMain.fraMoveTo = new FrameImage(mImage.createImage("/interface/moveto.png"), 32, 11);
         AvMain.fraAutoFire = new FrameImage(mImage.createImage("/point/other_10.png"), 18, 18);
         AvMain.fraMenuAuto = new FrameImage(mImage.createImage("/interface/iconpirate.png"), 11, 14);
      }

      loadImageLanguage();
      LoadLowGraphic();
      LoadWantedPaperSrcImg();
   }

   public static void loadImageLanguage() {
      Interface_Game.mImgPvPType = null;
      if (GameCanvas.language == 1) {
         AvMain.imgLg = mImage.createImage("/interface/lgv_e.png");
      } else if (MotherCanvas.h >= 240) {
         AvMain.imgLg = mImage.createImage("/interface/lgv.png");
      } else {
         AvMain.imgLg = mImage.createImage("/interface/lgv_2.png");
      }

      LoginScreen.hLogo = mImage.getImageHeight(AvMain.imgLg.image) / 2;
   }

   public static mImage LoadNewInterface(String str) {
      String var1;
      if (GameCanvas.lowGraphic) {
         var1 = "/w_interface";
      } else {
         var1 = "/interface";
      }

      return mImage.createImage(var1 + str);
   }

   public static void LoadImgPvP() {
      AvMain.imgPvpVs = mImage.createImage("/interface/pvpvs.png");
      AvMain.imgPvpOk = mImage.createImage("/interface/pvpok.png");
      AvMain.imgPvpObjdef = mImage.createImage("/interface/pvpobjdef.png");
   }

   public static void LoadLowGraphic() {
      if (!GameCanvas.lowGraphic) {
         AvMain.imgPaperDoc = new mImage[8];

         int var0;
         for(var0 = 0; var0 < AvMain.imgPaperDoc.length; ++var0) {
            AvMain.imgPaperDoc[var0] = mImage.createImage("/interface/paper_" + var0 + ".png");
         }

         AvMain.mimgBgA = new mImage[26];

         for(var0 = 0; var0 < AvMain.mimgBgA.length; ++var0) {
            AvMain.mimgBgA[var0] = mImage.createImage("/interface/bga" + var0 + ".png");
         }

         AvMain.mimgWanted = new mImage[18];

         for(var0 = 0; var0 < AvMain.mimgWanted.length; ++var0) {
            AvMain.mimgWanted[var0] = mImage.createImage("/interface/wanted" + var0 + ".png");
         }

         AvMain.fraImgEffOnMap0 = new FrameImage(LoadNewInterface("/eff_inmap0.png"), 12, 12);
         AvMain.imgPaper = new mImage[8];

         for(var0 = 0; var0 < AvMain.imgPaper.length; ++var0) {
            AvMain.imgPaper[var0] = mImage.createImage("/interface/paper" + var0 + ".png");
         }

         Boat.imgEffSea = mImage.createImage("/bg/boateff.png");
         Boat.imgEffSea2 = mImage.createImage("/bg/boateff2.png");
         Boat.fraEffSea = new FrameImage(mImage.createImage("/bg/sea4.png"), 24, 24);
         Boat.fraEffSea2 = new FrameImage(mImage.createImage("/bg/sea5.png"), 72, 13);
         Boat.fraEffSea3 = new FrameImage(mImage.createImage("/bg/sea6.png"), 45, 13);
         Interface_Game.imgHoavan = mImage.createImage("/interface/hoavan.png");
         MsgDialog.fraAutoMpHp = new FrameImage(mImage.createImage("/interface/automphp.png"), 20, 20);
         loadImageEffBoat();
         AvMain.fraIconServer = new FrameImage(mImage.createImage("/interface/iconserver.png"), 37, 45);
         AvMain.fraBtBanhlai = new FrameImage(mImage.createImage("/interface/frabanhlai.png"), 53, 53);
         AvMain.fraBorderWanted = new FrameImage(mImage.createImage("/interface/borderwanted.png"), 13, 28);
         AvMain.fraButtonTiemNang = new FrameImage(mImage.createImage("/interface/button_tiemnang.png"), 30, 28);
         AvMain.fraEffBoss = new FrameImage(mImage.createImage("/interface/effboss.png"), 32, 22);
         Interface_Game.imgInfo = mImage.createImage("/interface/info.png");
         AvMain.mImgRoomW = new mImage[5];

         for(var0 = 0; var0 < AvMain.mImgRoomW.length; ++var0) {
            AvMain.mImgRoomW[var0] = mImage.createImage("/interface/roomw" + var0 + ".png");
         }

         AvMain.fraEquip = new FrameImage(mImage.createImage("/interface/equip.png"), 18, 19);
         AvMain.fraThongThao = new FrameImage(mImage.createImage("/interface/thong_thao.png"), 89, 21);
         AvMain.fraBanhLai = new FrameImage(mImage.createImage("/interface/banh_lai.png"), 33, 33);
         AvMain.mimgWanted2 = new mImage[29];
      }

   }

   public static void loadImageEffBoat() {
      if (!GameCanvas.lowGraphic) {
         if (LoadMapScreen.isMapSky == 1) {
            Boat.imgEffSea = mImage.createImage("/bg/boateff4.png");
            Boat.fraEffSea = new FrameImage(mImage.createImage("/bg/sea4fly.png"), 24, 24);
            Boat.fraEffSea2 = new FrameImage(mImage.createImage("/bg/sea5fly.png"), 72, 13);
            Boat.fraEffSea3 = new FrameImage(mImage.createImage("/bg/sea6fly.png"), 45, 13);
            return;
         }

         Boat.imgEffSea = mImage.createImage("/bg/boateff.png");
         Boat.imgEffSea2 = mImage.createImage("/bg/boateff2.png");
         Boat.imgEffSea3 = mImage.createImage("/bg/boateff3.png");
         Boat.fraEffSea = new FrameImage(mImage.createImage("/bg/sea4.png"), 24, 24);
         Boat.fraEffSea2 = new FrameImage(mImage.createImage("/bg/sea5.png"), 72, 13);
         Boat.fraEffSea3 = new FrameImage(mImage.createImage("/bg/sea6.png"), 45, 13);
         Boat.fraEffSea4 = new FrameImage(mImage.createImage("/bg/sea7.png"), 24, 24);
      }

   }

   public static FrameImage loadFraImage(String var0, int var1, int var2) {
      return new FrameImage(mImage.createImage(var0), var1, var2);
   }

   private static void LoadWantedPaperSrcImg() {
      AvMain.imgDemoWanted = mImage.createImage("/interface/0_demo1.png");
      AvMain.imgTrangTri = mImage.createImage("/interface/1_wanted.png");
      AvMain.imgDuoi = new mImage[4];

      int i;
      for(i = 0; i < AvMain.imgDuoi.length; ++i) {
         AvMain.imgDuoi[i] = mImage.createImage("/interface/4_duoi_" + (i + 1) + ".png");
      }

      AvMain.imgGiua = new mImage[4];

      for(i = 0; i < AvMain.imgGiua.length; ++i) {
         AvMain.imgGiua[i] = mImage.createImage("/interface/4_giua_" + (i + 1) + ".png");
      }

      AvMain.imgPhai = new mImage[5];

      for(i = 0; i < AvMain.imgPhai.length; ++i) {
         AvMain.imgPhai[i] = mImage.createImage("/interface/4_phai_" + (i + 1) + ".png");
      }

      AvMain.imgTrai = new mImage[5];

      for(i = 0; i < AvMain.imgTrai.length; ++i) {
         AvMain.imgTrai[i] = mImage.createImage("/interface/4_trai_" + (i + 1) + ".png");
      }

      AvMain.imgTren = new mImage[4];

      for(i = 0; i < AvMain.imgTren.length; ++i) {
         AvMain.imgTren[i] = mImage.createImage("/interface/4_tren_" + (i + 1) + ".png");
      }

      (AvMain.imgGoc = new mImage[4])[0] = mImage.createImage("/interface/4_giay_tren_trai.png");
      AvMain.imgGoc[1] = mImage.createImage("/interface/4_giay_tren_phai.png");
      AvMain.imgGoc[2] = mImage.createImage("/interface/4_giay_duoi_trai.png");
      AvMain.imgGoc[3] = mImage.createImage("/interface/4_giay_duoi_phai.png");
      (AvMain.imgKhung = new mImage[2])[0] = mImage.createImage("/interface/khung.png");
      AvMain.imgKhung[1] = mImage.createImage("/interface/khung_1.png");
      AvMain.imgComplete = mImage.createImage("/interface/complete.png");
      AvMain.imgKhungMem = mImage.createImage("/interface/1.png");
      AvMain.imgKhungItem = mImage.createImage("/interface/03.png");
      AvMain.fraCmdNhanNapThe = new FrameImage(mImage.createImage("/interface/04.png"), 52, 20);
      AvMain.fraNauBanh = new FrameImage(mImage.createImage("/interface/pvpnew1.png"), 3);
      AvMain.fraTrongCay = new FrameImage(mImage.createImage("/interface/typecay.png"), 3);
      AvMain.imgDialogTrangTri = new mImage[17];

      for(i = 0; i < 17; ++i) {
         if (i < 10) {
            AvMain.imgDialogTrangTri[i] = mImage.createImage("/interface/tt0" + i + ".png");
         } else {
            AvMain.imgDialogTrangTri[i] = mImage.createImage("/interface/tt" + i + ".png");
         }
      }

   }
}
