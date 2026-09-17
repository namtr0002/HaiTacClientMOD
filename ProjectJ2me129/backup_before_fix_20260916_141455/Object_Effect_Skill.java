public final class Object_Effect_Skill {
   public short ID;
   public byte tem;
   public int hpShow;
   public int hpLast;
   public int hpMagic;
   public int[] mEffTypePlus = new int[0];
   public int[] AG = new int[0];
   public int[] mEff_Time_Plus = new int[0];

   public Object_Effect_Skill(short var1, byte var2) {
      this.ID = var1;
      this.tem = var2;
   }

   public final void AA(int var1, int var2, int var3) {
      this.hpShow = var1;
      this.hpLast = var2;
      this.hpMagic = var3;
   }
}
