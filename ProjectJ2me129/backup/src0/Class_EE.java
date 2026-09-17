import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

final class Class_EE implements CommandListener {
   private TField AA;

   Class_EE(TField var1) {
      this.AA = var1;
   }

   public final void commandAction(Command var1, Displayable var2) {
      TField.AJ = false;
      TField.AA(this.AA, false);
      if (var1.getLabel().equals("OK")) {
         this.AA.AB(this.AA.AK.getString());
         if (Class_IO.AB) {
            if (Class_IO.AA().AA.getText().length() > 0) {
               Class_IO.AA().AF();
            }

            Class_IO.AB = false;
         } else if (GameCanvas.currentScreen == GameCanvas.chatTabScr) {
            if (GameCanvas.chatTabScr.AN.AT != null) {
               GameCanvas.chatTabScr.AN.AA(GameScreen.player.name);
            }
         } else if (GameCanvas.ClanScr != null && GameCanvas.currentScreen == GameCanvas.ClanScr && GameCanvas.ClanScr.AN.AT != null) {
            GameCanvas.ClanScr.AN.f_();
         }
      }

      if (var1.getLabel().equals("Cancel")) {
         Class_IO.AB = false;
      }

      Display.getDisplay(GameMidlet.AD).setCurrent(GameMidlet.AC);
      GameMidlet.AC.setFullScreenMode(true);
      System.gc();
   }
}
