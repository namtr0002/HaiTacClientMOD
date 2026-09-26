public class PetManager {
   public static mVector petList = new mVector();
   
   public static void clearPets() {
      petList.removeAllElements();
   }
   
   public static void addPet(PetData pet) {
      petList.addElement(pet);
   }
   
   public static PetData getPetById(short id) {
      for (int i = 0; i < petList.size(); i++) {
         PetData pet = (PetData)petList.elementAt(i);
         if (pet != null && pet.id == id) {
            return pet;
         }
      }
      return null;
   }
   
   public static PetData getPetByIndex(int index) {
      if (index >= 0 && index < petList.size()) {
         return (PetData)petList.elementAt(index);
      }
      return null;
   }
   
   public static int getPetCount() {
      return petList.size();
   }
}

class PetData {
   public short id;
   public String name;
   public short icon;
   public byte isUse;
   public byte upgrade;
   
   public PetData(short id, String name, short icon, byte isUse, byte upgrade) {
      this.id = id;
      this.name = name;
      this.icon = icon;
      this.isUse = isUse;
      this.upgrade = upgrade;
   }
}