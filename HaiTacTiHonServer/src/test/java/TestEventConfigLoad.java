import event.Event;
import event.EventManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class TestEventConfigLoad {

    @Test
    public void testParseArgsFormatWithKey() {
        String argsConfig = "#event_id,is_active,time,timedropitem,timechangeitem,timeremoveitem,timex2pay,key\n" +
                "#Cấu hình: #event_id|is_active(1:Bật/0:Tắt)|thời gian sự kiện|thời gian rơi đồ|thời gian đổi đồ|thời gian xóa đồ|thời gian x2 nạp|key/season|\n" +
                "1,0,0:00:00 10/7/2026 > 23:59:59 20/7/2026,0:00:00 10/7/2026 > 23:59:59 21/7/2026,0:00:00 10/7/2026 > 23:59:59 22/7/2026,0:00:00 10/7/2026 > 23:59:59 23/7/2026,0:00:00 10/7/2026 > 23:59:59 24/7/2026,TRUNG_THU_2026\n" +
                "2,0,,,,,,TET_2026\n" +
                "8,0,0:00:00 15/6/2026 > 23:59:59 15/10/2026,0:00:00 15/6/2026 > 23:59:59 15/10/2026,0:00:00 15/6/2026 > 23:59:59 15/10/2026,0:00:00 15/6/2026 > 23:59:59 15/10/2026,0:00:00 15/6/2026 > 23:59:59 15/10/2026,HE_2026\n" +
                "13,1,0:00:00 15/6/2026 > 23:59:59 01/7/2026,0:00:00 15/6/2026 > 23:59:59 01/7/2026,0:00:00 15/6/2026 > 23:59:59 01/7/2026,0:00:00 15/6/2026 > 23:59:59 01/7/2026,0:00:00 15/6/2026 > 23:59:59 01/7/2026,DAU_TRUONG_2026\n" +
                "17,0,,,,,,BIGMOM_2026";

        List<EventManager.EventConfigEntry> entries = EventManager.parseEventConfig(argsConfig);
        assertNotNull(entries);
        assertEquals(5, entries.size());

        EventManager.EventConfigEntry e1 = entries.get(0);
        assertEquals(1, e1.eventId);
        assertEquals(0, e1.isActive);
        assertEquals("0:00:00 10/7/2026 > 23:59:59 20/7/2026", e1.time);
        assertEquals("0:00:00 10/7/2026 > 23:59:59 21/7/2026", e1.timedropitem);
        assertEquals("0:00:00 10/7/2026 > 23:59:59 22/7/2026", e1.timechangeitem);
        assertEquals("0:00:00 10/7/2026 > 23:59:59 23/7/2026", e1.timeremoveitem);
        assertEquals("0:00:00 10/7/2026 > 23:59:59 24/7/2026", e1.timex2pay);
        assertEquals("TRUNG_THU_2026", e1.season);

        EventManager.EventConfigEntry e13 = entries.get(3);
        assertEquals(13, e13.eventId);
        assertEquals(1, e13.isActive);
        assertEquals("DAU_TRUONG_2026", e13.season);

        Event ev1 = EventManager.gI().getEvent(1);
        assertNotNull(ev1);
        ev1.loadConfig(e1.time, e1.timeend, e1.timex2pay, e1.timechangeitem, e1.timedropitem, e1.timeremoveitem, e1.season);
        assertEquals("TRUNG_THU_2026", ev1.getSeasonKey());
        assertEquals("TRUNG_THU_2026", ev1.getKey());

        System.out.println("[TEST] testParseArgsFormatWithKey passed successfully! Season key = " + ev1.getSeasonKey());
    }

    @Test
    public void testDuplicateEventIdsMultiSeason() {
        String multiSeasonConfig = 
                "# Cùng ID 1 (Trung thu) nhưng có 2 mùa khác nhau:\n" +
                "1,1,0:00:00 10/7/2026 > 23:59:59 20/7/2026,0:00:00 10/7/2026 > 23:59:59 21/7/2026,0:00:00 10/7/2026 > 23:59:59 22/7/2026,0:00:00 10/7/2026 > 23:59:59 23/7/2026,0:00:00 10/7/2026 > 23:59:59 24/7/2026,TRUNG_THU_2026\n" +
                "1,1,0:00:00 10/7/2027 > 23:59:59 20/7/2027,0:00:00 10/7/2027 > 23:59:59 21/7/2027,0:00:00 10/7/2027 > 23:59:59 22/7/2027,0:00:00 10/7/2027 > 23:59:59 23/7/2027,0:00:00 10/7/2027 > 23:59:59 24/7/2027,TRUNG_THU_2027\n";

        List<EventManager.EventConfigEntry> entries = EventManager.parseEventConfig(multiSeasonConfig);
        assertEquals(2, entries.size());
        assertEquals(1, entries.get(0).eventId);
        assertEquals("TRUNG_THU_2026", entries.get(0).season);
        assertEquals(1, entries.get(1).eventId);
        assertEquals("TRUNG_THU_2027", entries.get(1).season);

        System.out.println("[TEST] testDuplicateEventIdsMultiSeason parsed 2 seasons for Event 1 successfully!");
    }

    @Test
    public void testParseUserJson() {
        String jsonStr = "[\n" +
                "  {\n" +
                "    \"timex2pay\": \"0:00:00 10/7/2026 > 23:59:59 24/7/2026\",\n" +
                "    \"timechangeitem\": \"0:00:00 10/7/2026 > 23:59:59 22/7/2026\",\n" +
                "    \"event_id\": 1,\n" +
                "    \"is_active\": 0,\n" +
                "    \"time\": \"0:00:00 10/7/2026 > 23:59:59 20/7/2026\",\n" +
                "    \"timeremoveitem\": \"0:00:00 10/7/2026 > 23:59:59 23/7/2026\",\n" +
                "    \"timedropitem\": \"0:00:00 10/7/2026 > 23:59:59 21/7/2026\",\n" +
                "    \"key\": \"TRUNG_THU_JSON\"\n" +
                "  }\n" +
                "]";

        List<EventManager.EventConfigEntry> entries = EventManager.parseEventConfig(jsonStr);
        assertNotNull(entries);
        assertEquals(1, entries.size());

        EventManager.EventConfigEntry obj0 = entries.get(0);
        assertEquals(1, obj0.eventId);
        assertEquals(0, obj0.isActive);
        assertEquals("0:00:00 10/7/2026 > 23:59:59 20/7/2026", obj0.time);
        assertEquals("TRUNG_THU_JSON", obj0.season);

        Event ev1 = EventManager.gI().getEvent(1);
        assertNotNull(ev1);
        ev1.loadConfig(obj0.time, obj0.timeend, obj0.timex2pay, obj0.timechangeitem, obj0.timedropitem, obj0.timeremoveitem, obj0.season);
        assertEquals("TRUNG_THU_JSON", ev1.getSeasonKey());
        System.out.println("[TEST] testParseUserJson passed successfully! Loaded Event 1 config: time = " + ev1.time);
    }

    @Test
    public void testDateParsingAndRanges() {
        java.util.Date d1 = Event.parseEventDate("0:00:00 10/7/2026", true);
        assertNotNull(d1);

        java.util.Date d2 = Event.parseEventDate("23:59:59 24/7/2026", false);
        assertNotNull(d2);
        assertTrue(d2.after(d1));

        java.util.Date dStart = Event.getEventStartDate("0:00:00 10/7/2026 > 23:59:59 24/7/2026");
        assertNotNull(dStart);

        java.util.Date dEnd = Event.getEventEndDate("0:00:00 10/7/2026 > 23:59:59 24/7/2026");
        assertNotNull(dEnd);
        assertTrue(dEnd.after(dStart));
        System.out.println("[TEST] testDateParsingAndRanges passed successfully! dStart=" + dStart + ", dEnd=" + dEnd);
    }

    @Test
    public void testEvent2010ItemConstants() {
        assertEquals(590, event.SuKien20Thang10.ITEM_GAU_BONG);
        assertEquals(591, event.SuKien20Thang10.ITEM_CANH_HOA);
        assertEquals(592, event.SuKien20Thang10.ITEM_HOP_QUA_1);
        assertEquals(593, event.SuKien20Thang10.ITEM_HOP_QUA_2);
        assertEquals(594, event.SuKien20Thang10.ITEM_HOP_QUA_3);
        assertEquals(595, event.SuKien20Thang10.ITEM_HOP_QUA_4);
        assertEquals(596, event.SuKien20Thang10.ITEM_HOP_QUA_DB);
        System.out.println("[TEST] testEvent2010ItemConstants passed successfully!");
    }

    public static void main(String[] args) {
        TestEventConfigLoad t = new TestEventConfigLoad();
        t.testParseArgsFormatWithKey();
        t.testDuplicateEventIdsMultiSeason();
        t.testParseUserJson();
        t.testDateParsingAndRanges();
        t.testEvent2010ItemConstants();
        System.out.println("[ALL TESTS PASSED]");
    }
}

