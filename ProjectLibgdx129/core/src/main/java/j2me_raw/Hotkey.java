public final class Hotkey {
   public MainItem itemcur;
   public MainSkill skill;

   public final void setPotion(MainItem var1) {
      this.itemcur = var1;
      this.skill = null;
   }

   public final void setSkill(MainSkill skill, short IdIcon) {
      this.skill = skill;
      this.skill.idIcon = IdIcon;
      this.itemcur = null;
      Skill_Info skillFromID;
      if ((skillFromID = Skill_Info.getSkillFromID(skill.ID)).typeSkill == 2) {
         this.skill.setTypeBuff((byte)1, (short)46, (short)0);
      }

      this.skill.lvDevil = skillFromID.LvDevilSkill;
   }

   public final byte getIndexDelay() {
      if (this.itemcur != null) {
         return 0;
      } else {
         return (byte)(this.skill != null ? 1 : -1);
      }
   }

   public static void checkUpdatePotion(MainItem itemcheck) {
      for(int i = 0; i < Player.hotkeyPlayer.length; ++i) {
         for(int j = 0; j < Player.hotkeyPlayer[i].length; ++j) {
            if (Player.hotkeyPlayer[i][j].itemcur != null && Player.hotkeyPlayer[i][j].itemcur.typeObject == itemcheck.typeObject && Player.hotkeyPlayer[i][j].itemcur.ID == itemcheck.ID) {
               Player.hotkeyPlayer[i][j].itemcur = itemcheck;
            }
         }
      }

   }
}
