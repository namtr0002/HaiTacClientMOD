public final class MainSkill {
   public short AA;
   public short AB;
   public short idIcon;
   public short ID;
   public short AE;
   public short AF;
   public short AG;
   public int AH;
   public int AI;
   public int AJ;
   public long AK;
   public mVector AL = new mVector("MainSkill.vecPos");
   public byte AM = 0;
   public byte AN;
   public byte Dirbuff;
   public byte lvDevil;
   public boolean isBuff = false;
   public boolean AQ = false;
   public int AR = 0;
   public int AS;
   private static short[] AT = new short[]{100, 120, 100, 32, 100, 100, 100, 120, 80, 100, 120, 120, 120, 64, 48, 48, 48, 48, 120, 120, 80, 48, 100, 48, 64, 48, 100, 100, 64, 100, 32, 120, 48, 48, 100, 100, 120, 120, 48, 100, 48, 100, 120, 120, 48, 48, 100, 100, 100, 120, 120, 48, 48, 100, 100, 120, 120, 80, 80, 80, 80, 120, 120, 100, 48, 60, 60, 60, 60, 60, 60, 84, 84, 32, 48, 84, 48, 120, 32, 32, 32, 60, 60, 60, 60, 60, 60, 60, 48, 60, 32, 32, 84, 84, 48, 84, 84, 84, 84, 48, 72, 72, 84, 48, 84, 48, 84, 48, 84, 84, 84, 48, 48, 84, 84, 72, 48, 72, 48, 84, 72, 60, 60, 60, 60, 60, 60, 60, 48, 48, 48, 48, 48, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 60, 60, 120, 120, 360, 60, 60};

   public MainSkill(short var1, short var2) {
      this.AA = var2;
      this.ID = var1;
      if (var1 == 4005 || var2 == 4005) {
         this.idIcon = 4005;
      }
      if ((var1 >= 1010 && var1 <= 1014) || (var1 >= 487 && var1 <= 511)) {
         this.AQ = true;
      }

   }

   public final void setTypeBuff(byte var1, short var2, short var3) {
      this.AM = var1;
      this.AE = var2;
      this.AF = var3;
      this.AK = GameCanvas.timeNow;
   }

   public final void AA(int var1, int var2, mVector var3, byte var4) {
      this.AI = var1;
      this.AJ = var2;
      this.AL = var3;
      this.AN = var4;
      this.Dirbuff = var4;
   }

   public final void AA(mGraphics var1, int var2, int var3, byte var4) {
      short drawIcon = this.idIcon;
      if ((this.ID == 4005 || this.AA == 4005) && (drawIcon == 201 || drawIcon == 205 || drawIcon <= 0)) {
         drawIcon = 4005;
      }
      Skill_Info.paintIcon(var1, var2, var3, drawIcon, var4);
   }

   public final void AA() {
      if (this.AA >= 4001 && this.AA <= 4016) {
         this.AR = 220;
      } else if (this.AA == 4017 || this.AA == 4021 || this.AA == 4024 || this.AA == 4025 || this.AA == 4027 || this.AA == 4028) {
         this.AR = 240;
      } else if (this.AA == 4018) {
         this.AR = 360;
      } else if (this.AA == 4019 || this.AA == 4022 || this.AA == 4023 || this.AA == 4026 || this.AA == 4029) {
         this.AR = 120;
      } else if (this.AA > AT.length - 1) {
         this.AR = 48;
      } else {
         this.AR = AT[this.AA];
      }
   }
}
