package com.deplor.haitactihontool.pet;

public class PetTemplate {
   public int id;
   public String name;
   public int icon;
   public int type;
   public int frame;
   public String op;

   public PetTemplate() {
   }

   public PetTemplate(int id, String name, int icon, int type, int frame, String op) {
      this.id = id;
      this.name = name;
      this.icon = icon;
      this.type = type;
      this.frame = frame;
      this.op = op;
   }
}
