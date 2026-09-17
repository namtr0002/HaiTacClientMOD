package event.eboss;

import bot.Bot;
import bot.IMove;
import ability.Ability;
import model.Player;
import core.Manager;
import core.ZUtil;
import event.EventData;
import itemz.MainItem;
import map.Zone;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DoiTruongHuyenThoai extends Bot {

    public static final String[] CHAT_BOSS = new String[]{
        "Tiến lên! Chiến thắng ở trong tầm tay ta!",
        "Hôm nay chúng ta sẽ vô địch!",
        "Ai có thể cản bước đội bóng của ta?",
        "Sút! Vào!",
        "Chuyền bóng cho ta nào!",
        "Một cú sút thần sầu!",
        "Hãy cùng tranh tài nào!",
        "Hãy bảo vệ khung thành của chúng ta!"
    };

    public final int slotId;
    public final String announcePrefix;
    public final int customMaxHp;
    private boolean handledDeath = false;
    public long timeLive = -1;

    public DoiTruongHuyenThoai(int slotId) throws Exception {
        super(-32000 - slotId, slotId == 0 ? "Đội Trưởng Đội Đức" : (slotId == 1 ? "Đội Trưởng Đội Brazil" : "Đội Trưởng Đội Pháp"));
        
        this.slotId = slotId;
        this.type_pk = -1;
        this.announcePrefix = slotId == 0 ? "Đội Trưởng Đội Đức" : (slotId == 1 ? "Đội Trưởng Đội Brazil" : "Đội Trưởng Đội Pháp");
        this.customMaxHp = slotId == 0 ? 1000000 : (slotId == 1 ? 2000000 : 3000000);
        this.level = 50;
        this.disableBaseChat = true; // disable default bot chats

        // Force delete existing bot from DB to get a fresh random class and random gear registration
        try (Connection conn = database.DbManager.gI().getConnect();
             Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM `players_bot` WHERE `name` = '" + this.name + "'");
        } catch (Exception ignored) {}

        // Setup loads details from DB (registerBot creates random class + default equips/skills)
        if (setup()) {
            this.setin4();
            this.init();
        }

        // Reset index_map to negative unique ID to prevent conflicts with real players
        this.IDPlayer = -32000 - slotId;
        this.index_map = (short) this.IDPlayer;

        // Override body to return our custom max HP
        this.ability = new Ability(this) {
            @Override
            public int get_hp_max(boolean have_eff) {
                return customMaxHp;
            }
        };

        // Reset HP to max
        this.hp = this.ability.get_hp_max(true);
        this.mp = this.ability.get_mp_max(true);
        this.type_pk = 0; // PK Red Name, no flag icon
    }

    @Override
    public void init() {
        this.setAttack(new bot.AttackAround());
        this.setMove(new BossMove());
    }

    @Override
    public void update() {
        if (this.isdie || this.hp <= 0) {
            if (!handledDeath) {
                handledDeath = true;
                this.hp = 0;
                this.isdie = true;
                try {
                    onDeath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }

        if (timeLive > 0 && System.currentTimeMillis() > timeLive) {
            this.hp = 0;
            this.isdie = true;
            leave();
            return;
        }

        // Custom soccer chat popup
        if (this.timeChatBot < System.currentTimeMillis()) {
            this.timeChatBot = System.currentTimeMillis() + 6000 + ZUtil.random(3000);
            if (this.map != null) {
                try {
                    this.map.send_chat_popup(0, this.index_map, CHAT_BOSS[ZUtil.random(CHAT_BOSS.length)]);
                } catch (Exception ignored) {}
            }
        }

        super.update();
    }

    public void onDeath() {
        Player killer = this.lastAttacker;
        if (killer == null) {
            if (this.map != null) {
                for (Player p : this.map.players) {
                    if (p != null && !p.isBot && p.conn != null) {
                        killer = p;
                        break;
                    }
                }
            }
        }

        if (killer != null) {
            onKillRewardAll(killer);
            onKillReward(killer);
            Manager.gI().chatKTG(0, killer.name + " đã hạ gục " + announcePrefix, 0);
        }

        leave();
    }

    public boolean isAlive() {
        return this.map != null && !this.isdie;
    }

    public void spawn(int mapId, int zoneId, short x, short y, long liveSec) {
        Zone[] zones = Zone.getMapByID(mapId);
        if (zones != null && zoneId < zones.length) {
            this.isdie = false;
            this.handledDeath = false;
            this.x = x;
            this.y = y;
            join(zones[zoneId], x, y);
            this.hp = this.ability.get_hp_max(true);
            this.mp = this.ability.get_mp_max(true);
            this.type_pk = 0;
            this.timeLive = (liveSec > 0) ? System.currentTimeMillis() + liveSec * 1000L : -1;
            
            // Broadcast PK state to all players in the zone
            Zone zone = zones[zoneId];
            for (Player p : zone.players) {
                if (p != null && !p.isBot && p.conn != null) {
                    try {
                        p.getService().update_PK(this, false);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    public void onKillRewardAll(Player killer) {
        for (Player p : killer.map.players) {
            if (p == null || p.isBot || p.conn == null) continue;

            // Check daily limit (100 same-map rewards/day)
            EventData ev = p.getDataEvent(13);
            if (ev != null && ev.data != null) {
                if (ev.data[3] >= 100) {
                    continue; // Skip players who hit limit
                }
                ev.data[3]++;
            }

            p.update_pointEvent1(1);

            // Reward: Beri (5000 to 6999) + random items (Bột Cường Hóa, Bột Vàng, etc.)
            int beriReward = ZUtil.random(5000, 6999);

            List<template.GiftBox> listGift = new ArrayList<>();
            listGift.add(new template.GiftBox(4, 0, beriReward));

            int rand = ZUtil.random(100);
            if (rand < 40) {
                listGift.add(new template.GiftBox(7, 1, 1)); // 1x Bột Cường Hóa
            } else if (rand < 60) {
                listGift.add(new template.GiftBox(7, 4, 1)); // 1x Bột Vàng
            } else if (rand < 80) {
                listGift.add(new template.GiftBox(4, 599, 1)); // 1x Vé World Cup
            } else if (rand < 90) {
                listGift.add(new template.GiftBox(4, 597, 1)); // 1x Bóng bạc
            }

            core.RewardService.sendGiftOrMail(p, 1, "Đấu Trường Rực Lửa", "Thưởng cùng khu vực hạ Boss", listGift, true);
        }
    }

    public void onKillReward(Player killer) {
        // Check daily limit (60 killer rewards/day per Boss)
        EventData ev = killer.getDataEvent(13);
        if (ev != null && ev.data != null) {
            if (ev.data[slotId] >= 60) {
                return;
            }
            ev.data[slotId]++;
        }

        // Reward: Large Beri (up to 15,000 Beri) + materials/tickets/rare items
        killer.update_pointEvent1(5);
        int beriReward = ZUtil.random(8000, 15000);

        List<template.GiftBox> listGift = new ArrayList<>();
        listGift.add(new template.GiftBox(4, 0, beriReward));
        listGift.add(new template.GiftBox(4, 599, ZUtil.random(1, 2))); // 1-2x Vé World Cup
        listGift.add(new template.GiftBox(4, 597, ZUtil.random(1, 2))); // 1-2x Bóng bạc

        int rand = ZUtil.random(100);
        if (rand < 30) {
            listGift.add(new template.GiftBox(7, 1, ZUtil.random(1, 3))); // 1-3x Bột Cường Hóa
        } else if (rand < 55) {
            listGift.add(new template.GiftBox(7, 4, ZUtil.random(1, 2))); // 1-2x Bột Vàng
        } else if (rand < 75) {
            listGift.add(new template.GiftBox(4, 598, 1)); // 1x Bóng vàng
        } else if (rand < 90) {
            listGift.add(new template.GiftBox(4, 609, 1)); // 1x Rương Worldcup
        } else if (rand < 95) {
            listGift.add(new template.GiftBox(4, 610, 1)); // 1x Cúp Worldcup
        } else {
            // Rare pet: Tuần lộc kuto (729) or Prometheus (689) or Black Zeus (728)
            int[] petIds = {729, 689, 728};
            listGift.add(new template.GiftBox(4, petIds[ZUtil.random(petIds.length)], 1));
        }

        core.RewardService.sendGiftOrMail(killer, 1, "Đấu Trường Rực Lửa", "Quà kết liễu Boss " + announcePrefix, listGift, true);
    }

    public static class BossMove implements IMove {
        private Player target;
        private short targetX = -1;
        private short targetY = -1;
        private long nextMoveTime = 0;

        private Player detect(Bot owner) {
            Zone map = owner.map;
            if (map == null) return null;
            for (int i = 0; i < map.players.size(); i++) {
                Player p = map.players.get(i);
                if (p != null && !p.isBot && !p.isdie) {
                    boolean canAttack = (owner.typePirate == 0 && p.typePirate == 2)
                            || (owner.typePirate == 2 && p.typePirate == 0)
                            || (owner.typePirate == 1 && p.typePirate == 2)
                            || (owner.typePirate == 2 && p.typePirate == 1)
                            || (owner.type_pk == 14 && p.type_pk == 15)
                            || (owner.type_pk == 15 && p.type_pk == 14)
                            || (owner.type_pk >= 11 && owner.type_pk <= 13 && p.type_pk >= 11 && p.type_pk <= 13 && owner.type_pk != p.type_pk)
                            || (owner.typePirate == 2 && p.typePirate == 2)
                            || (owner.type_pk == 0)
                            || (p.type_pk == 1)
                            || (owner.type_pk == 3 && p.type_pk == 3)
                            || (p.type_pk == 0)
                            || (owner.type_pk == 3 && p.type_pk >= 4 && p.type_pk <= 8)
                            || (p.type_pk == 3 && owner.type_pk >= 4 && owner.type_pk <= 8)
                            || (owner.type_pk >= 4 && owner.type_pk <= 8 && p.type_pk >= 4
                                && p.type_pk <= 8 && owner.type_pk != p.type_pk);
                    if (canAttack) {
                        return p;
                    }
                }
            }
            return null;
        }

        @Override
        public void move(Bot owner) {
            Zone map = owner.map;
            if (map == null || owner.isdie) {
                return;
            }

            if (target == null || target.isdie || target.map != map) {
                target = detect(owner);
            }

            if (target != null) {
                double dist = Math.hypot(target.x - owner.x, target.y - owner.y);
                if (dist > 400) {
                    owner.x = target.x;
                    owner.y = target.y;
                    bot.SmartMovement.moveTowards(owner, owner.x, owner.y, 0);
                } else if (dist > 40) {
                    bot.SmartMovement.moveTowards(owner, target.x, target.y, 35);
                }
                targetX = -1;
                targetY = -1;
            } else {
                long now = System.currentTimeMillis();
                if (targetX == -1 || targetY == -1) {
                    short[] pt = bot.SmartMovement.getRandomGroundPoint(map);
                    targetX = pt[0];
                    targetY = pt[1];
                }

                double dist = Math.hypot(targetX - owner.x, targetY - owner.y);
                if (dist > 15) {
                    bot.SmartMovement.moveTowards(owner, targetX, targetY, 30);
                } else {
                    if (nextMoveTime == 0) {
                        nextMoveTime = now + ZUtil.random(3000, 7000);
                    } else if (now > nextMoveTime) {
                        short[] pt = bot.SmartMovement.getRandomGroundPoint(map);
                        targetX = pt[0];
                        targetY = pt[1];
                        nextMoveTime = 0;
                    }
                }
            }
        }
    }
}
