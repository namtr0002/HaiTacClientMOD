import org.junit.Test;
import static org.junit.Assert.*;

import template.ItemTemplate3;
import template.Item_wear;
import template.Option;
import template.ThanTrangConfig;
import itemz.rebuilds.NangCapThanTrang;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestThanTrangBalance {

    @Test
    public void testAll15SetsCoverage() {
        assertEquals("Phải có đủ 15 bộ thần trang", 15, ThanTrangConfig.SET_NAMES.length);
        
        for (int id = 2604; id <= 2693; id++) {
            assertTrue("Item ID " + id + " phải là Thần Trang", ThanTrangConfig.isThanTrang(id));
            
            List<Option> defaultOps = ThanTrangConfig.getThanTrangOptions(id);
            assertNotNull("Default options không được null cho id " + id, defaultOps);
            assertTrue("Mỗi món thần trang phải có từ 5 đến 6 dòng options", defaultOps.size() >= 5 && defaultOps.size() <= 6);
            
            Set<Short> optionIds = new HashSet<>();
            for (Option op : defaultOps) {
                assertNotNull("Option không được null", op);
                assertTrue("Không được có option trùng lặp trong 1 món đồ: " + op.id + " ở item " + id, optionIds.add(op.id));
                assertTrue("Param option phải > 0", op.param > 0);
                if (ThanTrangConfig.isPercentOption(op.id)) {
                    assertTrue("Base % option phải <= 50 (5.0%): id " + op.id + " ở item " + id, op.param <= 50);
                }
                if (op.id >= 5 && op.id <= 9) {
                    assertTrue("Base tiềm năng phải <= 8 điểm: id " + op.id + " ở item " + id, op.param <= 8);
                }
            }
            
            List<ThanTrangConfig.ThanTrangOptionRange> ranges = ThanTrangConfig.getThanTrangOptionRanges(id);
            assertEquals("Số lượng range phải bằng số lượng option mặc định", defaultOps.size(), ranges.size());
            for (int i = 0; i < ranges.size(); i++) {
                ThanTrangConfig.ThanTrangOptionRange range = ranges.get(i);
                Option baseOp = defaultOps.get(i);
                assertEquals((int)baseOp.id, range.optionId);
                assertTrue("Min range phải <= Base", range.minParam <= baseOp.param);
                assertTrue("Max range phải >= Base", range.maxParam >= baseOp.param);
                assertTrue("Min range phải > 0", range.minParam > 0);
                if (ThanTrangConfig.isPercentOption(range.optionId)) {
                    assertTrue("Max % tinh luyện phải <= 55: option " + range.optionId + " ở item " + id, range.maxParam <= 55);
                }
                if (range.optionId >= 5 && range.optionId <= 9) {
                    assertTrue("Max tiềm năng tinh luyện phải <= 10 điểm: option " + range.optionId + " ở item " + id, range.maxParam <= 10);
                }
            }
        }
    }

    @Test
    public void testLevelScaling() {
        // Test level 0 -> 20 scaling for a Thần Trang weapon (typeequip = 8)
        int baseParam = 50; // % Tấn công (5.0%)
        Option op = new Option(1, baseParam);
        
        int p0 = op.getParam(8, 0, 0);
        assertEquals(baseParam, p0); // Tier 0: 100% -> 50
        
        int p10 = op.getParam(8, 10, 0);
        assertEquals((baseParam * 175) / 100, p10); // Tier 10: 175% -> 87 (8.7%)
        
        int p20 = op.getParam(8, 20, 0);
        assertEquals((baseParam * 250) / 100, p20); // Tier 20: 250% (2.5x) -> 125 (12.5%)

        // Test điểm tiềm năng (Option 5 Sức mạnh: base 6 điểm)
        // Công thức Thần Trang: result = param + (tier / 2)
        Option opStat = new Option(5, 6);
        int stat0 = opStat.getParam(8, 0, 0);
        assertEquals(6, stat0); // Tier 0: 6 điểm
        int stat10 = opStat.getParam(8, 10, 0);
        assertEquals(11, stat10); // Tier 10: 6 + 5 = 11 điểm
        int stat20 = opStat.getParam(8, 20, 0);
        assertEquals(16, stat20); // Tier 20: 6 + 10 = 16 điểm (vừa vặn, uy lực xứng danh Thần Trang, không bị vọt ảo)

        // Test với max tinh luyện 8 điểm tiềm năng
        Option opMaxStat = new Option(5, 8);
        int maxStat20 = opMaxStat.getParam(8, 20, 0);
        assertEquals(18, maxStat20); // Tier 20: 8 + 10 = 18 điểm
    }

    @Test
    public void testTinhLuyenReRoll() {
        Item_wear it = new Item_wear();
        it.template = new ItemTemplate3();
        it.template.id = 2604;
        it.template.typeEquip = 8;
        it.levelUp = 0;

        for (int i = 0; i < 100; i++) {
            NangCapThanTrang.TinhLuyenResult res = NangCapThanTrang.rollTinhLuyen(it);
            assertNotNull(res);
            assertNotNull(res.newOptions);
            assertEquals(6, res.newOptions.size());
            
            List<ThanTrangConfig.ThanTrangOptionRange> ranges = ThanTrangConfig.getThanTrangOptionRanges(2604);
            for (int j = 0; j < ranges.size(); j++) {
                Option rolled = res.newOptions.get(j);
                ThanTrangConfig.ThanTrangOptionRange r = ranges.get(j);
                assertEquals(r.optionId, (int)rolled.id);
                assertTrue("Param phải trong min-max range", rolled.param >= r.minParam && rolled.param <= r.maxParam);
            }
        }
    }

    @Test
    public void testIsMaxAllOptionsAndBurst() {
        Item_wear it = new Item_wear();
        it.template = new ItemTemplate3();
        it.template.id = 2604;
        it.template.typeEquip = 8;
        it.levelUp = 0;

        List<ThanTrangConfig.ThanTrangOptionRange> ranges = ThanTrangConfig.getThanTrangOptionRanges(2604);
        assertNotNull(ranges);
        assertFalse(ranges.isEmpty());

        // Empty options -> false
        assertFalse(NangCapThanTrang.isMaxAllOptions(it));

        // Incomplete options -> false
        it.option_item.add(new Option(ranges.get(0).optionId, ranges.get(0).maxParam));
        assertFalse(NangCapThanTrang.isMaxAllOptions(it));

        // All options set to max
        it.option_item.clear();
        for (ThanTrangConfig.ThanTrangOptionRange r : ranges) {
            int max = r.maxParam;
            if (ThanTrangConfig.isPercentOption(r.optionId) && max > 50) {
                max = 50;
            }
            it.option_item.add(new Option(r.optionId, max));
        }
        assertTrue("Tất cả dòng đạt max phải trả về true", NangCapThanTrang.isMaxAllOptions(it));

        // One option below max -> false
        it.option_item.get(0).param = (short)(it.option_item.get(0).param - 1);
        assertFalse("Một dòng chưa đạt max phải trả về false", NangCapThanTrang.isMaxAllOptions(it));

        // Test roll simulation reaches MAX ALL
        int maxAllHits = 0;
        int trials = 500;
        for (int i = 0; i < trials; i++) {
            NangCapThanTrang.TinhLuyenResult res = NangCapThanTrang.rollTinhLuyen(it);
            it.option_item.clear();
            it.option_item.addAll(res.newOptions);
            if (NangCapThanTrang.isMaxAllOptions(it)) {
                maxAllHits++;
            }
        }
        assertTrue("Trong 500 lần tinh luyện với tỉ lệ 5% bạo phát phải có ít nhất 1 lần MAX ALL (đạt " + maxAllHits + " lần)", maxAllHits > 0);
    }
}

