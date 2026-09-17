import network.Message;
import network.MessageHandler;
import network.Session;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestMessageHandlerCmd74 {

    @BeforeClass
    public static void setUp() {
        core.Manager.gI().mysql_host = "localhost";
        core.Manager.gI().mysql_user = "root";
        core.Manager.gI().mysql_pass = "";
        core.Manager.gI().mysql_database = "haitacz";
    }

    @Test
    public void testCmd74EmptyPayload() {
        MessageHandler handler = new MessageHandler(null);
        Message msg = new Message((byte) 74, new byte[0]);
        try {
            handler.process_msg(msg);
        } catch (Exception e) {
            fail("Empty payload for cmd 74 should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testCmd74OneBytePayload() {
        MessageHandler handler = new MessageHandler(null);
        Message msg = new Message((byte) 74, new byte[]{0});
        try {
            handler.process_msg(msg);
        } catch (Exception e) {
            fail("1-byte payload for cmd 74 should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testCmd74TwoBytePayload() {
        MessageHandler handler = new MessageHandler(null);
        // 2 bytes short id = 10
        Message msg = new Message((byte) 74, new byte[]{0, 10});
        try {
            handler.process_msg(msg);
        } catch (Exception e) {
            fail("2-byte payload for cmd 74 should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testCmd74ThreeBytePayload() {
        MessageHandler handler = new MessageHandler(null);
        // 1 byte type = 0, 2 bytes short id = 10
        Message msg = new Message((byte) 74, new byte[]{0, 0, 10});
        try {
            handler.process_msg(msg);
        } catch (Exception e) {
            fail("3-byte payload for cmd 74 should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testCmdMinus44VariousPayloads() {
        MessageHandler handler = new MessageHandler(null);
        
        // 0 bytes
        Message msg0 = new Message((byte) -44, new byte[0]);
        try {
            handler.process_msg(msg0);
        } catch (Exception e) {
            fail("0-byte payload for cmd -44 should not throw exception: " + e.getMessage());
        }

        // 1 byte
        Message msg1 = new Message((byte) -44, new byte[]{1});
        try {
            handler.process_msg(msg1);
        } catch (Exception e) {
            fail("1-byte payload for cmd -44 should not throw exception: " + e.getMessage());
        }

        // 2 bytes
        Message msg2 = new Message((byte) -44, new byte[]{0, 5});
        try {
            handler.process_msg(msg2);
        } catch (Exception e) {
            fail("2-byte payload for cmd -44 should not throw exception: " + e.getMessage());
        }
    }
}
