import org.junit.Test;
import static org.junit.Assert.*;

import model.Player;
import template.ItemTemplate3;
import template.Item_wear;
import itemz.Item;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class TestBotEquipThanTrang {

    @Test
    public void testBotEquipSetupAndSerialization() {
        if (ItemTemplate3.ENTRYS == null) {
            ItemTemplate3.ENTRYS = new java.util.ArrayList<>();
        }
        for (int id = 2604; id <= 2693; id++) {
            if (ItemTemplate3.get_it_by_id((short) id) == null) {
                int piece = (id - 2604) % 6;
                byte typeEquip = (byte) (8 + piece);
                ItemTemplate3 t = new ItemTemplate3();
                t.id = (short) id;
                t.name = "Than Trang " + id;
                t.clazz = 0;
                t.typeEquip = typeEquip;
                t.icon = (short) (271 + (id - 2604));
                t.level = 100;
                t.color = 8;
                t.typelock = 1;
                t.option_item = new java.util.ArrayList<>();
                t.option_item_2 = new java.util.ArrayList<>();
                t.mdakham = new short[0];
                t.initDefaultOptionsFromCode();
                ItemTemplate3.ENTRYS.add(t);
            }
        }
        for (int id = 0; id <= 50; id++) {
            if (ItemTemplate3.get_it_by_id((short) id) == null) {
                byte typeEquip = (byte) (id % 8);
                ItemTemplate3 t = new ItemTemplate3();
                t.id = (short) id;
                t.name = "Item " + id;
                t.clazz = 1;
                t.typeEquip = typeEquip;
                t.icon = 1;
                t.level = 50;
                t.color = 5;
                t.typelock = 1;
                t.option_item = new java.util.ArrayList<>();
                t.option_item_2 = new java.util.ArrayList<>();
                t.mdakham = new short[0];
                t.initDefaultOptionsFromCode();
                ItemTemplate3.ENTRYS.add(t);
            }
        }

        Player bot = new Player();
        bot.isBot = true;
        bot.clazz = 1;
        bot.level = 90;
        bot.name = "BotAkainu";

        bot.setupBotEquip((byte) 1, (short) 90, 4.0, true, true, true, 0);

        assertNotNull("it_body must not be null", bot.item.it_body);
        assertTrue("it_body length must be >= 14", bot.item.it_body.length >= 14);

        for (int slot = 0; slot < 6; slot++) {
            Item_wear it = bot.item.it_body[slot];
            assertNotNull("Slot " + slot + " must have normal equipment", it);
            assertTrue("Slot " + slot + " levelUp must be >= 10", it.levelUp >= 10);
            assertEquals("Slot " + slot + " should be Hoan My", 1, it.isHoanMy);
            assertTrue("Slot " + slot + " should have Kich An", it.valueKichAn >= 0);
        }

        assertNotNull("Slot 6 (Heart) must not be null", bot.item.it_body[6]);
        assertNotNull("it_heart must not be null", bot.item.it_heart);
        assertNotNull("Slot 7 (Dial) must not be null", bot.item.it_body[7]);

        for (int piece = 0; piece < 6; piece++) {
            int slot = 8 + piece;
            Item_wear tt = bot.item.it_body[slot];
            assertNotNull("Than Trang slot " + slot + " must not be null", tt);
            assertNotNull("Than Trang template must not be null", tt.template);
            int expectedId = 2604 + piece;
            assertEquals("Than Trang ID must be " + expectedId, expectedId, tt.template.id);
            assertEquals("Than Trang color must be 8", 8, tt.getColor());
            assertTrue("Than Trang levelUp must be >= 10", tt.levelUp >= 10);
            assertNotNull("Than Trang options must not be null", tt.option_item);
            assertTrue("Than Trang must have options", tt.option_item.size() >= 5);
        }

        assertTrue("Bot with Tier 4 should have isTransformThanTrang = true", bot.isTransformThanTrang);
        assertTrue("Bot transformThanTrangSetId must be > 0", bot.transformThanTrangSetId > 0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        for (int i = 0; i < 16; i++) {
            Item_wear it_w = (i < bot.item.it_body.length) ? bot.item.it_body[i] : null;
            if (it_w != null && it_w.template != null) {
                try {
                    dos.writeByte(1);
                    Item.readUpdateItem(dos, it_w, bot);
                    dos.writeShort(bot.get_wearing_part(i));
                } catch (Exception e) {
                    fail("Serialization failed at slot " + i + ": " + e.getMessage());
                }
            } else {
                try {
                    dos.writeByte(0);
                } catch (Exception e) {
                    fail("Failed to write empty slot: " + e.getMessage());
                }
            }
        }

        byte[] bytes = baos.toByteArray();
        assertTrue("Serialized packet must have positive length", bytes.length > 0);
        System.out.println("Serialization SUCCESS! Total bytes: " + bytes.length);
    }
}
