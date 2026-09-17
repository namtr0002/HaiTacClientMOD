import bot.Bot;
import bot.BotBalanceEngine;
import model.*;
import org.junit.Test;
import zabstracts.AbsListTichNap;
import zabstracts.AbsTichTieuRuby;

import static org.junit.Assert.*;

public class TestMasterTaskVerification {

    @Test
    public void testFactoryRegistration() {
        // Test AbsListTichNap factory instances
        AbsListTichNap napTong = AbsListTichNap.get(0);
        assertNotNull("AbsListTichNap type 0 (Tổng) should exist", napTong);
        assertEquals(0, napTong.getType());

        AbsListTichNap napNgay = AbsListTichNap.get(2);
        assertNotNull("AbsListTichNap type 2 (Ngày) should exist", napNgay);
        assertEquals(2, napNgay.getType());

        AbsListTichNap napTuan = AbsListTichNap.get(5);
        assertNotNull("AbsListTichNap type 5 (Tuần) should exist", napTuan);
        assertEquals(5, napTuan.getType());

        // Test AbsTichTieuRuby factory instances
        AbsTichTieuRuby tieuNgay = AbsTichTieuRuby.get(1);
        assertNotNull("AbsTichTieuRuby type 1 (Ngày) should exist", tieuNgay);
        assertEquals(1, tieuNgay.getType());

        AbsTichTieuRuby tieuTuan = AbsTichTieuRuby.get(5);
        assertNotNull("AbsTichTieuRuby type 5 (Tuần) should exist", tieuTuan);
        assertEquals(5, tieuTuan.getType());

        AbsTichTieuRuby tieuTong = AbsTichTieuRuby.get(6);
        assertNotNull("AbsTichTieuRuby type 6 (Tổng) should exist", tieuTong);
        assertEquals(6, tieuTong.getType());
    }

    @Test
    public void testRechargeSpendMilestones() {
        // Check TichTieuRuby (12 milestones)
        assertEquals("TichTieuRuby should have 12 milestones", 12, TichTieuRuby.ENTRY.size());
        assertEquals("TichTieuRuby milestone 12 should be 5,000,000 Ruby", 5_000_000, TichTieuRuby.ENTRY.get(11).num);

        // Check TichTieuTuan (7 milestones)
        assertEquals("TichTieuTuan should have 7 milestones", 7, TichTieuTuan.ENTRY.size());
        assertEquals("TichTieuTuan milestone 1 should be 50,000 Ruby", 50_000, TichTieuTuan.ENTRY.get(0).num);
        assertEquals("TichTieuTuan milestone 7 should be 5,000,000 Ruby", 5_000_000, TichTieuTuan.ENTRY.get(6).num);

        // Check TichTieuTong (7 milestones)
        assertEquals("TichTieuTong should have 7 milestones", 7, TichTieuTong.ENTRY.size());
        assertEquals("TichTieuTong milestone 1 should be 100,000 Ruby", 100_000, TichTieuTong.ENTRY.get(0).num);
        assertEquals("TichTieuTong milestone 7 should be 20,000,000 Ruby", 20_000_000, TichTieuTong.ENTRY.get(6).num);

        // Check ListTichNapTuan (7 milestones)
        assertEquals("ListTichNapTuan should have 7 milestones", 7, ListTichNapTuan.ENTRY.size());
        assertEquals("ListTichNapTuan milestone 1 should be 50,000 VNĐ", 50_000, ListTichNapTuan.ENTRY.get(0).num);
        assertEquals("ListTichNapTuan milestone 7 should be 5,000,000 VNĐ", 5_000_000, ListTichNapTuan.ENTRY.get(6).num);

        // Check Rebalanced Economy: ListNapHangNgay max ruby is reduced
        assertEquals("ListNapHangNgay milestone 1 is 20k VND", 20_000, ListNapHangNgay.ENTRY.get(0).num);
        assertEquals("ListNapHangNgay milestone 7 is 2M VND", 2_000_000, ListNapHangNgay.ENTRY.get(6).num);

        // Check Rebalanced Economy: ListTichNap milestone 10 is 20M VND
        assertEquals("ListTichNap has 10 milestones", 10, ListTichNap.ENTRY.size());
        assertEquals("ListTichNap milestone 10 is 20M VND", 20_000_000, ListTichNap.ENTRY.get(9).num);
    }

    @Test
    public void testBotStatTierBalancing() throws Exception {
        if (skill.Skill_Template.ENTRYS == null) {
            skill.Skill_Template.ENTRYS = new java.util.ArrayList<>();
        }
        Player player = new Player();
        player.level = 80;
        player.hp = 1_000_000;
        player.hpMax = 1_000_000;
        player.mp = 50_000;
        player.mpMax = 50_000;
        player.dame = 100_000;
        player.def = 20_000;
        player.crit = 500;

        Bot normalBot = new Bot(1, "NormalBot");
        BotBalanceEngine.balanceAgainstPlayer(normalBot, player, Bot.StatTier.NORMAL);

        // Normal bot stats should be around 50-60% of player stats
        assertTrue("Normal bot HP should be <= player hpMax", normalBot.hpMax < player.hpMax);
        assertTrue("Normal bot HP should be >= 40% of player hpMax", normalBot.hpMax >= (int) (player.hpMax * 0.40));
        assertTrue("Normal bot Dame should be <= 70% of player dame", normalBot.dame <= (int) (player.dame * 0.70));

        Bot vipBot = new Bot(2, "VipBot");
        BotBalanceEngine.balanceAgainstPlayer(vipBot, player, Bot.StatTier.VIP);

        // VIP bot stats should be around 95-105% (capped at 110%)
        assertTrue("VIP bot HP should be >= 90% of player hpMax", vipBot.hpMax >= (int) (player.hpMax * 0.90));
        assertTrue("VIP bot Dame should be >= 90% of player dame", vipBot.dame >= (int) (player.dame * 0.90));
        assertTrue("VIP bot Dame capped at 115%", vipBot.dame <= (int) (player.dame * 1.15));
    }

    @Test
    public void testWantedTimeoutConstants() {
        assertEquals("Wanted timeout must be 12 seconds", 12_000L, activities.Wanted.MATCHMAKING_TIMEOUT_MS);
    }
}
