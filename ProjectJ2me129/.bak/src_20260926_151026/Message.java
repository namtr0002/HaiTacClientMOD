import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class Message {
   public byte command;
   private ByteArrayOutputStream baos = null;
   private DataOutputStream dos = null;
   private ByteArrayInputStream bais = null;
   private DataInputStream dis = null;
   public byte isOld = 0;

   public Message() {
   }

   public Message(byte cmd) {
      this.command = cmd;
      this.baos = new ByteArrayOutputStream();
      this.dos = new DataOutputStream(this.baos);
   }

   Message(byte cmd, byte[] data) {
      this.command = cmd;
      this.bais = new ByteArrayInputStream(data);
      this.dis = new DataInputStream(this.bais);
   }

   public final byte[] getData() {
      return this.baos.toByteArray();
   }

   public final DataInputStream reader() {
      return this.dis;
   }

   public final DataOutputStream writer() {
      return this.dos;
   }
}
