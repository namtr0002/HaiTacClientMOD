import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public class TcpClient {
   SocketConnection socketp;

   public TcpClient() {
   }

   public TcpClient(String host, int port) {
      try {
         this.socketp = (SocketConnection)Connector.open("socket://" + host + ":" + port);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public void close() {
      try {
         this.socketp.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   public DataOutputStream AB() {
      try {
         return this.socketp.openDataOutputStream();
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public DataInputStream AC() {
      try {
         return this.socketp.openDataInputStream();
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }
}
