package com.deplor.haitactihontool.mob;

public class MobTemplate {
   public int id;
   public String name;
   public short level;
   public short hOne;
   public int hp;
   public byte typemove;
   public byte ishuman;
   public byte typemonster;
   public String idicon;
   public String skill;
   public int dame;

   public MobTemplate(
      int id, String name, short level, short hOne, int hp, byte typemove, byte ishuman, byte typemonster, String idicon, String skill, int dame
   ) {
      this.id = id;
      this.name = name;
      this.level = level;
      this.hOne = hOne;
      this.hp = hp;
      this.typemove = typemove;
      this.ishuman = ishuman;
      this.typemonster = typemonster;
      this.idicon = idicon;
      this.skill = skill;
      this.dame = dame;
   }

   public MobTemplate cloneMob(int newId) {
      return new MobTemplate(
         newId, this.name + " (Copy)", this.level, this.hOne, this.hp, this.typemove, this.ishuman, this.typemonster, this.idicon, this.skill, this.dame
      );
   }
}
