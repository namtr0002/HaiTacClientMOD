public final class Class_CP {
   public byte AA;
   public short AB;
   public short AC;
   public short AD;
   public String AE = "";
   public static String[] AF = new String[]{"aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "jj"};

   public Class_CP(byte var1) {
      this.AA = var1;
   }

   public final void AA(short var1, short var2, short var3) {
      this.AB = var1;
      this.AC = var2;
      this.AD = var3;
      if (this.AD > this.AC) {
         this.AD = this.AC;
      }

      if (this.AA == 1) {
         this.AE = MainMonster.AD(var1).AL;
      } else {
         if (this.AA == 2) {
            this.AE = AF[var1];
         }

      }
   }

   public final void AA() {
      if (this.AA == 1) {
         this.AE = MainMonster.AD(this.AB).AL;
      }

   }
}
