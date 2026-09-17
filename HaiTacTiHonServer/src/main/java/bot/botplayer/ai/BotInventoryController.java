package bot.botplayer.ai;

import bot.botplayer.BotPlayerReal;
import network.Message;
import template.Item_wear;

/**
 * BotInventoryController — Quản lý nhặt đồ, dọn dẹp túi đồ và kiểm tra số ô trống.
 */
public class BotInventoryController {

    private final BotPlayerReal bot;

    public BotInventoryController(BotPlayerReal bot) {
        this.bot = bot;
    }

    /**
     * Nhặt các vật phẩm rơi dưới đất xung quanh nếu thuộc quyền sở hữu hoặc không chủ.
     */
    public void pickGroundItems() {
        if (bot.map == null || bot.map.list_it_map == null) {
            return;
        }
        for (int i = 0; i < bot.map.list_it_map.length; i++) {
            template.ItemMap itm = bot.map.list_it_map[i];
            if (itm == null) {
                continue;
            }
            if (itm.id_master == -1 || itm.id_master == bot.index_map) {
                try {
                    byte[] data = new byte[3];
                    data[0] = (byte) ((itm.index >> 8) & 0xFF);
                    data[1] = (byte) (itm.index & 0xFF);
                    data[2] = itm.category;
                    Message m = new Message((byte) 0, data);
                    bot.map.pick_item(bot, m);
                    m.cleanup();
                } catch (Exception ignored) {}
            }
        }
    }

    /**
     * Kiểm tra xem túi đồ đã đầy chưa (ít hơn 8 ô trống).
     */
    public boolean shouldSellTrash() {
        if (bot.item == null || bot.item.bag3 == null) {
            return false;
        }
        int freeSlots = 0;
        for (Item_wear it : bot.item.bag3) {
            if (it == null) {
                freeSlots++;
            }
        }
        return freeSlots < 8;
    }
    
    public static final int ITEM_HP_POTION = 173; // Cơm hộp hải tặc
    public static final int ITEM_MP_POTION = 174; // Quả Conache

    /**
     * Tự động mua bình máu/MP nếu hết.
     */
    public void buyPotions() {
        if (bot.item == null) return;
        try {
            int hpPots = bot.item.total_item_bag_by_id(4, ITEM_HP_POTION);
            if (hpPots < 50 && bot.get_vang() > 500_000L) {
                bot.update_vang(-100_000L);
                bot.item.add_item_bag47(4, ITEM_HP_POTION, 50 - hpPots);
            }
            int mpPots = bot.item.total_item_bag_by_id(4, ITEM_MP_POTION);
            if (mpPots < 50 && bot.get_vang() > 500_000L) {
                bot.update_vang(-100_000L);
                bot.item.add_item_bag47(4, ITEM_MP_POTION, 50 - mpPots);
            }
            bot.item.updateInventory(false);
        } catch (Exception ignored) {}
    }

    /**
     * Sử dụng bình HP/MP thực tế. Trả về true nếu thành công.
     */
    public boolean usePotion(boolean isHp) {
        if (bot.item == null) return false;
        try {
            int potionId = isHp ? ITEM_HP_POTION : ITEM_MP_POTION;
            if (bot.item.total_item_bag_by_id(4, potionId) > 0) {
                bot.item.remove_item47(4, potionId, 1);
                bot.item.updateInventory(false);
                if (isHp) {
                    bot.hp += bot.ability.get_hp_max(true) * 4 / 10;
                    if (bot.hp > bot.ability.get_hp_max(true)) bot.hp = bot.ability.get_hp_max(true);
                } else {
                    bot.mp += bot.ability.get_mp_max(true) * 4 / 10;
                    if (bot.mp > bot.ability.get_mp_max(true)) bot.mp = bot.ability.get_mp_max(true);
                }
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    public boolean needsPotions() {
        if (bot.item == null) return false;
        int hpPots = bot.item.total_item_bag_by_id(4, ITEM_HP_POTION);
        int mpPots = bot.item.total_item_bag_by_id(4, ITEM_MP_POTION);
        return (hpPots < 5 || mpPots < 5) && bot.get_vang() > 200_000L;
    }

    /**
     * Tự động mở rộng ô hành trang và rương đồ khi túi gần đầy và đủ kinh phí.
     */
    public void autoExpandInventorySlots() {
        autoExpandStorage();
    }

    /**
     * Mở rộng túi đồ và rương đồ khi đạt điều kiện.
     */
    public void autoExpandStorage() {
        if (bot == null || bot.item == null) return;
        try {
            int freeSlots = 0;
            if (bot.item.bag3 != null) {
                for (Item_wear it : bot.item.bag3) {
                    if (it == null) freeSlots++;
                }
            }
            if (freeSlots < 6 && (bot.item.max_bag & 0xFFFF) < itemz.Item.MAX_BAG_LIMIT) {
                if (bot.get_vang() >= 500_000L) {
                    bot.update_vang(-200_000L);
                    bot.item.expand_bag(6);
                    bot.item.updateInventory(false);
                    bot.getSocialController().chat("Vừa mở rộng ô túi đồ cho thoải mái cày!");
                } else if (bot.get_ngoc() >= 10) {
                    bot.update_ngoc(-5);
                    bot.item.expand_bag(6);
                    bot.item.updateInventory(false);
                }
            }

            int freeBoxSlots = 0;
            if (bot.item.box3 != null) {
                for (Item_wear it : bot.item.box3) {
                    if (it == null) freeBoxSlots++;
                }
            }
            if (freeBoxSlots < 6 && (bot.item.max_box & 0xFFFF) < itemz.Item.MAX_BOX_LIMIT) {
                if (bot.get_vang() >= 500_000L) {
                    bot.update_vang(-200_000L);
                    bot.item.expand_box(6);
                    bot.item.updateInventory(false);
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Tự động kiểm tra hòm thư và nhận quà mail.
     */
    public void checkAndClaimMails() {
        if (bot == null || bot.item == null) return;
        try {
            java.util.List<core.MailService.MailEntry> mails = core.MailService.getActiveMails(bot.IDPlayer);
            if (mails != null && !mails.isEmpty()) {
                for (core.MailService.MailEntry mail : mails) {
                    if (mail != null && !mail.isClaimed && mail.gifts != null && !mail.gifts.isEmpty()) {
                        autoExpandStorage();
                        core.MailService.claimMailGift(bot, mail.id);
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Tự động mở các rương quà, hộp quà trong túi đồ (bag4).
     */
    public void autoOpenBoxesAndChests() {
        if (bot == null || bot.item == null) return;
        try {
            if (bot.item.bag47 != null) {
                for (int i = 0; i < bot.item.bag47.size(); i++) {
                    template.ItemBag47 item = bot.item.bag47.get(i);
                    if (item != null && item.category == 4 && item.quant > 0) {
                        if (item.id == 29 || item.id == 158 || (item.id >= 212 && item.id <= 218) || item.id == 283 || item.id == 284) {
                            model.UseItem.use_item_4(bot, item.id);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Tự động mua nguyên liệu nâng cấp (bột cường hóa, đá nâng cấp, búa).
     */
    public void buyUpgradeMaterials() {
        if (bot.item == null) return;
        try {
            // Mua/bổ sung Bột Cường Hóa (cat 7, id 0)
            int botCuongHoa = bot.item.total_item_bag_by_id(7, 0);
            if (botCuongHoa < 30 && bot.get_vang() > 500_000L) {
                bot.update_vang(-200_000L);
                bot.item.add_item_bag47(7, 0, 30);
                bot.item.updateInventory(false);
            }
            // Mua/bổ sung Đá Cường Hóa (cat 7, id 1)
            int daCuongHoa = bot.item.total_item_bag_by_id(7, 1);
            if (daCuongHoa < 20 && bot.get_vang() > 500_000L) {
                bot.update_vang(-200_000L);
                bot.item.add_item_bag47(7, 1, 20);
                bot.item.updateInventory(false);
            }
        } catch (Exception ignored) {}
    }
}

