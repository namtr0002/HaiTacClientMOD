import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestServerCommandsIPC {

    @Test
    public void testProcessCommandQueue() throws Exception {
        // 1. Initialize DB
        core.Manager.gI().mysql_host = "localhost";
        core.Manager.gI().mysql_user = "root";
        core.Manager.gI().mysql_pass = "";
        core.Manager.gI().mysql_database = "haitacz";

        // Insert a test command
        try (Connection conn = database.DbManager.gI().getConnect()) {
            assertNotNull("Connection should not be null", conn);
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO server_commands (command, status, created_by) VALUES (?, 0, 'junit_test')")) {
                ps.setString(1, "/help");
                ps.executeUpdate();
            }

            // 2. Process command queue
            core.ServerEventManager.processPendingServerCommands();

            // 3. Verify that the command was executed
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT status, response FROM server_commands WHERE command = '/help' AND created_by = 'junit_test' ORDER BY id DESC LIMIT 1");
                 ResultSet rs = ps.executeQuery()) {
                assertTrue("Should find executed command", rs.next());
                int status = rs.getInt("status");
                String response = rs.getString("response");
                assertEquals("Command status should be 2 (SUCCESS)", 2, status);
                assertNotNull("Response should not be null", response);
                assertTrue("Response should contain GM console help header", response.contains("DANH SÁCH LỆNH GM CONSOLE"));
                System.out.println("[TEST SUCCESS] Response received from IPC command: \n" + response);
            }
        }
    }

    public static void main(String[] args) {
        try {
            TestServerCommandsIPC t = new TestServerCommandsIPC();
            t.testProcessCommandQueue();
            System.out.println("ALL IPC TESTS PASSED SUCCESSFULLY!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
