import org.junit.Test;
import static org.junit.Assert.*;

import template.ItemTemplate3;
import template.Item_wear;
import template.Option;
import itemz.UpgradeDial;
import java.util.List;
import java.util.ArrayList;

public class TestDialStats {

    @Test
    public void testAllDialTemplatesHaveOptions() {
        for (int id = 12001; id <= 12017; id++) {
            List<Option> ops = ItemTemplate3.getDefaultDialOptions(id);
            assertNotNull("Dial " + id + " must have options", ops);
            assertFalse("Dial " + id + " options must not be empty", ops.isEmpty());
            assertTrue("Dial " + id + " must have at least 2 options", ops.size() >= 2);

            assertEquals("First option must be Atk % (id 1)", 1, (int) ops.get(0).id);
            assertTrue("Atk % must be > 0", ops.get(0).getParam() > 0);

            for (Option op : ops) {
                assertTrue("Dial must NEVER have Option 56 (Mau cuoi) - template " + id, op.id != 56);
            }
        }
    }

    @Test
    public void testDialCucPhamHas8Options() {
        List<Option> ops = ItemTemplate3.getDefaultDialOptions(12017);
        assertEquals("Dial Cuc Pham must have exactly 8 options", 8, ops.size());
        assertEquals(350, ops.get(0).getParam());
        assertEquals(17, (int) ops.get(1).id);
        assertEquals(40, ops.get(1).getParam());
        assertEquals(4, (int) ops.get(2).id);
        assertEquals(35, ops.get(2).getParam());
        assertEquals(53, (int) ops.get(3).id);
        assertEquals(25, ops.get(3).getParam());
    }

    @Test
    public void testSanitizeAndAutoRecoverDial() {
        Item_wear dial = new Item_wear();
        dial.template = new ItemTemplate3();
        dial.template.id = 12017;
        dial.template.name = "Dial Cuc Pham";
        dial.template.typeEquip = 7;
        dial.template.color = 8;
        dial.option_item = new ArrayList<>();
        dial.option_item.add(new Option(1, 280));
        UpgradeDial.sanitizeDialOptions(dial);
        assertEquals("Dial Cuc Pham must be restored to 8 options", 8, dial.option_item.size());
        assertEquals(1, (int) dial.option_item.get(0).id);
        assertEquals(17, (int) dial.option_item.get(1).id);
        assertEquals(4, (int) dial.option_item.get(2).id);
        assertEquals(53, (int) dial.option_item.get(3).id);
        assertEquals(25, dial.option_item.get(3).getParam());
    }

    @Test
    public void testDialUpgradeScale() {
        Option atk = new Option(1, 350);
        Option mienthuong = new Option(53, 25);
        Option hp = new Option(17, 40);
        assertEquals(350, atk.getParam(7, 0, 0));
        assertEquals(25, mienthuong.getParam(7, 0, 0));
        assertEquals(40, hp.getParam(7, 0, 0));
        assertEquals(1050, atk.getParam(7, 5, 0));
        assertEquals(75, mienthuong.getParam(7, 5, 0));
        assertEquals(120, hp.getParam(7, 5, 0));
    }
}
