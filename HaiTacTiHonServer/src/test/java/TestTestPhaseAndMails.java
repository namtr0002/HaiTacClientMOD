import core.MailService;
import core.Manager;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class TestTestPhaseAndMails {

    @BeforeClass
    public static void setUp() {
        Manager.gI().mysql_host = "localhost";
        Manager.gI().mysql_user = "root";
        Manager.gI().mysql_pass = "";
        Manager.gI().mysql_database = "haitacz";
    }

    @Test
    public void testServerPhaseLoadAndAutoDetect() {
        Manager mgr = Manager.gI();
        assertNotNull(mgr);

        mgr.loadSettingsFromDb();
        String currentStatus = mgr.getServerStatus();
        System.out.println("[TEST] Loaded server_status: " + currentStatus);
        assertNotNull("Server status should not be null", currentStatus);
        assertFalse("Server status should not be empty", currentStatus.trim().isEmpty());

        if ("TEST".equalsIgnoreCase(currentStatus)) {
            assertTrue("isTestMode() must be true when in TEST status", mgr.isTestMode());
            assertTrue("isTestPhase() must be true when in TEST status", mgr.isTestPhase());
        } else if ("BETA".equalsIgnoreCase(currentStatus)) {
            assertTrue("isBetaPhase() must be true when in BETA status", mgr.isBetaPhase());
        }

        // Test TEST phase invariants: autoDetectAndUpdatePhase must not override TEST mode
        String origStatus = mgr.server_status;
        try {
            mgr.server_status = "TEST";
            assertEquals("TEST", mgr.getServerStatus());
            assertTrue("isTestMode() must be true", mgr.isTestMode());
            assertTrue("isTestPhase() must be true", mgr.isTestPhase());

            // Run autoDetectAndUpdatePhase with TEST status -> must retain TEST
            mgr.autoDetectAndUpdatePhase();
            System.out.println("[TEST] After autoDetectAndUpdatePhase (TEST mode), server_status: " + mgr.getServerStatus());
            assertEquals("TEST", mgr.getServerStatus());
            assertTrue("isTestPhase() must still be true after autoDetect", mgr.isTestPhase());
        } finally {
            mgr.server_status = origStatus;
        }
    }

    @Test
    public void testCreateAllTestMailTemplates() {
        List<MailService.MailEntry> list = MailService.createAllTestMailTemplates(1, 1, "admin");
        assertNotNull(list);
        System.out.println("[TEST] Total Test Mail Templates generated: " + list.size());
        assertTrue("Must generate at least 30 packages", list.size() >= 30);

        boolean foundTienTe = false;
        boolean foundNika = false;
        boolean foundThanTrang = false;
        boolean foundHaki = false;
        boolean foundGems = false;

        for (MailService.MailEntry m : list) {
            assertNotNull(m.title);
            assertTrue("Every template must be recognized as test mail", MailService.isTestMail(m));
            if (m.title.contains("Tiền Tệ")) foundTienTe = true;
            if (m.title.contains("Thần Thoại") || m.content.contains("Nika")) foundNika = true;
            if (m.title.contains("Thần Trang")) foundThanTrang = true;
            if (m.title.contains("Haki")) foundHaki = true;
            if (m.title.contains("Đá Khảm") || m.title.contains("Đá Thần Thoại")) foundGems = true;
        }

        assertTrue("Must contain Tiền Tệ package", foundTienTe);
        assertTrue("Must contain Trái Ác Quỷ package", foundNika);
        assertTrue("Must contain Thần Trang package", foundThanTrang);
        assertTrue("Must contain Haki package", foundHaki);
        assertTrue("Must contain Gems package", foundGems);
    }
}
