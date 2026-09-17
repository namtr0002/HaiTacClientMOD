public final class MainClan {
   public String name;
   public String AB;
   public String AC;
   public int AD;
   public int AE;
   public int AF;
   public int AG;
   public int AH;
   public int AI;
   public int AJ;
   public int AK = 0;
   public int AL = 20;
   public int AM;
   public int AN;
   public int AO;
   public short ID;
   public short idIcon = -1;
   public byte AR = 0;
   public byte AS = 0;
   public byte AT = 0;
   public mVector vecMem = new mVector("MainClan.vecMem");
   public mVector vecChatClan = new mVector("MainClan.vecChatClan");
   public mVector vecAchi = new mVector("MainClan.vecAchi");
   public short[] AX = new short[5];

   public MainClan() {
   }

   public MainClan(short var1, short var2, byte var3) {
      this.ID = var1;
      this.idIcon = var2;
      this.AJ = var3;
   }

   public MainClan(short var1, String var2) {
      this.ID = var1;
      this.name = var2;
      this.AX = new short[5];
   }

   public static boolean AA() {
      return Player.QN == 0 || Player.QN == 1 || Player.QN == 2;
   }
}
