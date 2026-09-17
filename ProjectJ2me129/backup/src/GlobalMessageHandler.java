public final class GlobalMessageHandler extends Cmd_Message implements IMessageHandler {
   private GlobalLogicHandler globalLogicHandler = new GlobalLogicHandler();
   private static GlobalMessageHandler me;

   public static GlobalMessageHandler getInstance() {
      if (me == null) {
         me = new GlobalMessageHandler();
      }

      return me;
   }

   public final void GlobalLogicHandlerV() {
      GlobalLogicHandler g = this.globalLogicHandler;
   }

   public final void AA() {
      GlobalLogicHandler var10000 = this.globalLogicHandler;
      if (GameCanvas.currentScreen != GameCanvas.CQ) {
         String var1 = T.ES;
         if (GameCanvas.infoDisConnect != null && GameCanvas.infoDisConnect.length() > 10) {
            var1 = GameCanvas.infoDisConnect;
            GameCanvas.infoDisConnect = "";
         }

         GameScreen.BZ = new iCommand(T.QJ, 46, GameCanvas.gameScr);
         GameCanvas.Start_Normal_DiaLog(var1, GameScreen.BZ, true);
      }

   }

   public final void AB() {
      GameCanvas.AS();
   }

   public final void onMessage(Message m) {
      int[] var10000 = new int[]{1, -51, 4, 48, -82, 100, -80, 24, -83, 10};
      ReadMessenge rmsg;
      switch(m.command) {
      case -108:
         GameCanvas.readMessage.Read_Sudo(m);
         return;
      case -107:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.dataListServer(m);
         return;
      case -106:
      case -102:
      case -99:
      case -98:
      case -87:
      case -76:
      case -68:
      case -65:
      case -46:
      case -43:
      case -38:
      case -36:
      case -27:
      case -22:
      case -21:
      case -18:
      case -17:
      case -16:
      case -14:
      case -9:
      case -8:
      case -3:
      case -1:
      case 8:
      case 34:
      case 43:
      case 46:
      case 56:
      case 68:
      case 78:
      case 83:
      case 84:
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      default:
         (new StringBuffer(">>>>>>>>client k co cmd=")).append(m.command).toString();
         return;
      case -105:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EV(m);
         return;
      case -104:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EH(m);
         return;
      case -103:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DX(m);
         return;
      case -101:
      case -51:
         GameCanvas.readMessage.SetImage(m);
         return;
      case -100:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CY(m);
         return;
      case -97:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FS(m);
         return;
      case -96:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FT(m);
         return;
      case -95:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BF(m);
         return;
      case -94:
         GameCanvas.readMessage.CQ(m);
         return;
      case -93:
      case -92:
         GameCanvas.readMessage.loadImage(m);
         return;
      case -91:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FP(m);
         return;
      case -90:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FQ(m);
         return;
      case -89:
         GameCanvas.readMessage.FU(m);
         return;
      case -88:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AH();
         return;
      case -86:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.ER(m);
         return;
      case -85:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EQ(m);
         return;
      case -84:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EO(m);
         return;
      case -83:
         GameCanvas.readMessage.BA(m);
         return;
      case -82:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.ReadPartNew(m);
         return;
      case -81:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DW(m);
         return;
      case -80:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Pet(m);
         return;
      case -79:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DU(m);
         return;
      case -78:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AR(m);
         return;
      case -77:
         GameCanvas.readMessage.DT(m);
         return;
      case -75:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DR(m);
         return;
      case -74:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DO(m);
         return;
      case -73:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DQ(m);
         return;
      case -72:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DP(m);
         return;
      case -71:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Auto_Revice(m);
         return;
      case -70:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DM(m);
         return;
      case -69:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DL(m);
         return;
      case -67:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DK(m);
         return;
      case -66:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DJ(m);
         return;
      case -64:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DH(m);
         return;
      case -63:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.PvP(m);
         return;
      case -62:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DE(m);
         return;
      case -61:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DD(m);
         return;
      case -60:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DC(m);
         return;
      case -59:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DB(m);
         return;
      case -58:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DA(m);
         return;
      case -57:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Frist_Login(m);
         return;
      case -56:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CX(m);
         return;
      case -55:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.ghost(m);
         return;
      case -54:
         GameCanvas.readMessage.CV(m);
         return;
      case -53:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CU(m);
         return;
      case -52:
         GameCanvas.readMessage.DS(m);
         return;
      case -50:
         GameCanvas.readMessage.CS(m);
         return;
      case -49:
         GameCanvas.readMessage.CT(m);
         return;
      case -48:
         GameCanvas.readMessage.Upgrade_Item(m);
         return;
      case -47:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.setWeather(m);
         return;
      case -45:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.update_Pk_Point(m);
         return;
      case -44:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.loadDataEff(m);
         return;
      case -42:
         GameCanvas.readMessage.ShowInfoPlayer(m);
         return;
      case -41:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.resetValueUpdateImage(m);
         return;
      case -40:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.LoadImageAndroidOk();
         return;
      case -39:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CH(m);
         return;
      case -37:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CB(m);
         return;
      case -35:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BV(m);
         return;
      case -34:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BU(m);
         return;
      case -33:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BO(m);
         return;
      case -32:
         GameCanvas.readMessage.update_Chest(m);
         return;
      case -31:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BS(m);
         return;
      case -30:
         GameCanvas.readMessage.BR(m);
         return;
      case -29:
         GameCanvas.readMessage.BQ(m);
         return;
      case -28:
         GameCanvas.readMessage.BP(m);
         return;
      case -26:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Register(m);
         return;
      case -25:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Party(m);
         return;
      case -24:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.get_Info_NPC(m);
         return;
      case -23:
         GameCanvas.readMessage.List_Quest(m);
         return;
      case -20:
         GameCanvas.readMessage.Dynamic_Menu(m);
         return;
      case -19:
         GameCanvas.readMessage.Shop_NPC(m);
         return;
      case -15:
         GameCanvas.readMessage.Effect_Obj(m);
         return;
      case -13:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.use_Potion(m);
         return;
      case -12:
         GameCanvas.readMessage.update_Inventory(m);
         return;
      case -11:
         GameCanvas.readMessage.Dialog_More_server(m);
         return;
      case -10:
         GameCanvas.readMessage.Main_char_Info(m);
         return;
      case -7:
         GameCanvas.readMessage.getData(m);
         return;
      case -6:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Check_Data_Ver(m);
         return;
      case -5:
         GameCanvas.readMessage.char_info(m);
         return;
      case -4:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.ListChar(m);
         return;
      case -2:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.login_Ok();
         return;
      case 0:
         GameCanvas.readMessage.ChangeMap(m);
         return;
      case 1:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.ObjectMove(m);
         return;
      case 2:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.remove_Char(m);
         return;
      case 3:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AG(m);
         return;
      case 4:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.monsterInfo(m);
         return;
      case 5:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AK(m);
         return;
      case 6:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Revice_Player(m);
         return;
      case 7:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AL(m);
         return;
      case 9:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AP(m);
         return;
      case 10:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AN(m);
         return;
      case 11:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Item_Drop(m);
         return;
      case 12:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.GetItemMap(m);
         return;
      case 13:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AW(m);
         return;
      case 14:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.update_PK(m);
         return;
      case 15:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BC(m);
         return;
      case 16:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BE(m);
         return;
      case 17:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BL(m);
         return;
      case 18:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BM(m);
         return;
      case 19:
         GameCanvas.readMessage.BN(m);
         return;
      case 20:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BW(m);
         return;
      case 21:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BX(m);
         return;
      case 22:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.BY(m);
         return;
      case 23:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AreaStatus(m);
         return;
      case 24:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CC(m);
         return;
      case 25:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CD(m);
         return;
      case 26:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CE(m);
         return;
      case 27:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CF(m);
         return;
      case 28:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CG(m);
         return;
      case 29:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.CJ(m);
         return;
      case 30:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Ok_Change_Map_Link();
         return;
      case 31:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DG(m);
         return;
      case 32:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.DI(m);
         return;
      case 33:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AV(m);
         return;
      case 35:
         GameCanvas.readMessage.ChangeMapNonData(m);
         return;
      case 36:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.PvP_Thong_Bao(m);
         return;
      case 37:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EA(m);
         return;
      case 38:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EB(m);
         return;
      case 39:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.ED(m);
         return;
      case 40:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EE(m);
         return;
      case 41:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EF(m);
         return;
      case 42:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EG(m);
         return;
      case 44:
         GameCanvas.readMessage.EI(m);
         return;
      case 45:
         GameCanvas.readMessage.EJ(m);
         return;
      case 47:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EK(m);
         return;
      case 48:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EN(m);
         return;
      case 49:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.Max_Level(m);
         return;
      case 50:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AG();
         return;
      case 51:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.ES(m);
         return;
      case 52:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EU(m);
         return;
      case 53:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EW(m);
         return;
      case 54:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FD(m);
         return;
      case 55:
         GameCanvas.readMessage.EX(m);
         return;
      case 57:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EY(m);
         return;
      case 58:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EZ(m);
         return;
      case 59:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FA(m);
         return;
      case 60:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FH(m);
         return;
      case 61:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AI();
         return;
      case 62:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FJ(m);
         return;
      case 63:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FK(m);
         return;
      case 64:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FM(m);
         return;
      case 65:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FL(m);
         return;
      case 66:
         GameCanvas.readMessage.CP(m);
         return;
      case 67:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FO(m);
         return;
      case 69:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FI(m);
         return;
      case 70:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.AO(m);
         return;
      case 71:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EM(m);
         return;
      case 72:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FV(m);
         return;
      case 73:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FW(m);
         return;
      case 74:
      case 76:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.getDataEff(m);
         return;
      case 75:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.EL(m);
         return;
      case 77:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FF(m);
         return;
      case 79:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FC(m);
         return;
      case 80:
         GameCanvas.readMessage.FB(m);
         return;
      case 81:
         GameCanvas.readMessage.CR(m);
         return;
      case 82:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FE(m);
         return;
      case 99:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.FN(m);
         return;
      case 100:
         rmsg = GameCanvas.readMessage;
         ReadMessenge.ET(m);
         return;
      case 101:
         GameCanvas.readMessage.FG(m);
      }
   }
}
