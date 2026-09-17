public final class SaveImageRMS implements Runnable {
   public static mVector vecSaveImage = new mVector("SaveImageRMS.vecSaveImage");
   public static mVector AB = new mVector("SaveImageRMS.vecSaveImageAndroid");

   public final void run() {
      while(vecSaveImage.size() > 0 || AB.size() > 0) {
         try {
            idSaveImage var1;
            if (vecSaveImage.size() > 0) {
               ObjectData.setToRms((var1 = (idSaveImage)vecSaveImage.elementAt(0)).AB, var1.AA);
               vecSaveImage.removeElement(0);
            }

            if (AB.size() > 0) {
               try {
                  UpdateImageScreen.AB = UpdateImageScreen.AA - AB.size();
                  ObjectData.saveImageToRmsAndroid((var1 = (idSaveImage)AB.elementAt(0)).AB, var1.AC);
                  Thread.sleep(5L);
               } catch (Exception var2) {
               }

               AB.removeElement(0);
            }
         } catch (Exception var3) {
         }
      }

   }

   public final void start() {
      (new Thread(this)).start();
   }
}
