public class Cmd_Message {
   private Class_HQ AB = Session_ME.getInstance();
   protected Message m;

   public final void send() {
      this.AB.sendMessage(this.m);
      Message var10000 = this.m;
   }

   public final void init(byte var1) {
      this.m = new Message(var1);
   }
}
