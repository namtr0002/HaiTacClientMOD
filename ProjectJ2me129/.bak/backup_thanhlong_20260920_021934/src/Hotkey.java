public final class Hotkey {
   public MainItem itemcur;
   public MainSkill skill;

   public final void setPotion(MainItem var1) {
      this.itemcur = var1;
      this.skill = null;
   }

   public final void setSkill(MainSkill skill, short IdIcon) {
      this.skill = skill;
      if (this.skill != null) {
         this.skill.idIcon = IdIcon;
         Skill_Info skillFromID = Skill_Info.getSkillFromID(skill.ID);
         if (skillFromID != null) {
            this.skill.isBuff = (skillFromID.typeSkill == 2);
            if (skillFromID.typeSkill == 2) {
               this.skill.setTypeBuff((byte)1, (short)46, (short)0);
            }
            this.skill.lvDevil = skillFromID.LvDevilSkill;
            this.skill.AG = skillFromID.typeDevil;
            if (this.skill.AA <= 0) {
               this.skill.AA = skillFromID.typeEffSkill;
            }
         }
      }
      this.itemcur = null;
   }

   public final int getIndexDelay() {
      if (this.skill != null) {
         return this.skill.AB;
      }
      if (this.itemcur != null) {
         return this.itemcur.indexHotKey;
      }
      return -1;
   }

   public final void paint(mGraphics g, int x, int y, int w) {
      if (this.skill != null) {
         this.skill.AA(g, x, y, this.skill.lvDevil);
      } else if (this.itemcur != null) {
         this.itemcur.AB(g, x, y, w, 0);
      }
   }

   public static void checkUpdatePotion(MainItem itemcheck) {
      if (Player.hotkeyPlayer != null) {
         for(int i = 0; i < Player.hotkeyPlayer.length; ++i) {
            if (Player.hotkeyPlayer[i] != null) {
               for(int j = 0; j < Player.hotkeyPlayer[i].length; ++j) {
                  if (Player.hotkeyPlayer[i][j] != null && Player.hotkeyPlayer[i][j].itemcur != null && itemcheck != null && Player.hotkeyPlayer[i][j].itemcur.typeObject == itemcheck.typeObject && Player.hotkeyPlayer[i][j].itemcur.ID == itemcheck.ID) {
                     Player.hotkeyPlayer[i][j].itemcur = itemcheck;
                  }
               }
            }
         }
      }
   }
}
