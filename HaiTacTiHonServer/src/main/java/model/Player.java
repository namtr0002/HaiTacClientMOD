package model;
import ability.Ability;
import map.MapCanGoTo;
import map.MapManager;
import map.Zone;
import map.LeaveItemMap;
import event.EventData;
import skill.Skill_info;
import skill.Skill_Template;
import model.TichTieuRuby;
import model.ListNapHangNgay;
import model.ListTichNap;
import map.MapBossInfo;
import clan.Clan;
import zabstracts.AbsVongQuay;

import itemz.Item;
import map.zones.WorldWar;
import map.zones.TranChienLon;
import bot.BotTruyNa;
import bot.mercenary.MercenaryBot;
import map.zones.ChiemDao;
import map.zones.ThuLinhBienKhoi;
import map.zones.MapDaoKhoBau;
import map.zones.DauTruongTuDo;
import map.zones.TranChienKhongLo;
import map.Dame_Msg;
import zabstracts.AbsDungeon;
import event.SuKienHalloween;
import event.eboss.BiNgoMa;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import event.SuKienNoel;
import event.eboss.SantaNoel;
import event.eboss.QuaiVatTuyetNoel;
import activities.*;
import map.zones.BaoVePhaoDai;
import historys.zLog;
import achievement.ArchiDaily;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import core.Manager;
import network.Service;
import core.ZUtil;
import database.DbManager;
import event.EventManager;
import event.SuKienGioTo;
import event.SuKienTrongCay;
import event.SuKienKinhKibi;
import network.Message;
import network.Session;
import network.SessionManager;
import java.net.Socket;
import boss.SuperBossManager;
import event.Event;
import java.util.logging.Logger;
import map.Zone;
import map.MapCanGoTo;
import mob.Mob;
import map.Npc;
import map.Vgo;
import template.*;

public class Player {

    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    public Service getService() {
        if (this.service != null) {
            return this.service;
        }
        if (this.isDe && this instanceof DeTu) {
            Player mst = ((DeTu) this).master;
            if (mst != null && mst.getService() != null) {
                return mst.getService();
            }
        }
        return network.NoService.getInstance();
    }

    /**
     * Trả về Player chủ nhân nếu đây là Đệ tử hoặc Bot lính đánh thuê.
     * Nếu là người chơi bình thường, trả về chính instance này.
     */
    public Player getOwnerPlayer() {
        if (this instanceof DeTu) {
            DeTu dt = (DeTu) this;
            if (dt.master != null && dt.master != this) {
                return dt.master.getOwnerPlayer();
            }
        }
        if (this instanceof MercenaryBot) {
            MercenaryBot merc = (MercenaryBot) this;
            Player owner = SessionManager.PLAYERS_MAP.get(merc.ownerPlayerId);
            if (owner != null && owner != this) {
                return owner.getOwnerPlayer();
            }
        }
        return this;
    }

    public void setDefaultFashion(int clazz) {
        this.itfashionP = new ArrayList<>();
        this.fashion = new ArrayList<>();
        this.itemboat = new ArrayList<>();
        
        int id = 0, icon = 0;
        switch (clazz) {
            case 1: id = 5; icon = 1; break;
            case 2: id = 1; icon = 24; break;
            case 3: id = 2; icon = 28; break;
            case 4: id = 3; icon = 32; break;
            case 5: id = 4; icon = 36; break;
        }
        if (this.hair > 0) {
            icon = this.hair;
        } else {
            this.hair = (short) icon;
        }
        this.itfashionP.add(new ItemFashionP((short) id, (short) icon, (byte) 103, true));
        this.itfashionP.add(new ItemFashionP((short) 0, (short) 0, (byte) 108, true));
        
        ItemBoatP ib1 = new ItemBoatP(); ib1.id = 1; ib1.is_use = true; this.itemboat.add(ib1);
        ItemBoatP ib2 = new ItemBoatP(); ib2.id = 5; ib2.is_use = true; this.itemboat.add(ib2);
        ItemBoatP ib3 = new ItemBoatP(); ib3.id = 3; ib3.is_use = true; this.itemboat.add(ib3);
        ItemBoatP ib4 = new ItemBoatP(); ib4.id = 7; ib4.is_use = true; this.itemboat.add(ib4);
    }

    public void switchCharacter(Player target) throws IOException {
        if (target == null) return;
        Session session = this.conn;
        if (session == null) return;

        // Kiểm tra điều kiện: Đang hợp thể hoặc đệ tử về nhà thì không cho switch
        if (target instanceof model.DeTu) {
            model.DeTu dt = (model.DeTu) target;
            if (this.isFusion || dt.detuStatus == model.DeTu.STATUS_FUSION) {
                if (this.getService() != null) {
                    this.getService().send_box_ThongBao_OK("Không thể chuyển sang đệ tử khi đang trong trạng thái Hợp thể!");
                }
                return;
            }
            if (dt.detuStatus == model.DeTu.STATUS_HOME) {
                if (this.getService() != null) {
                    this.getService().send_box_ThongBao_OK("Đệ tử đang ở nhà! Vui lòng gọi đệ tử đi theo trước khi chuyển.");
                }
                return;
            }
        }

        // Save current character data to database
        flush(this, false);

        // Sync currencies before character switch
        if (target instanceof model.DeTu) {
            target.coin = this.coin;
            target.ruby = this.ruby;
            target.vnd = this.vnd;
            target.kimcuong = this.kimcuong;
            target.vang = this.vang;
        } else if (this instanceof model.DeTu) {
            target.coin = this.coin;
            target.ruby = this.ruby;
            target.vnd = this.vnd;
            target.kimcuong = this.kimcuong;
            target.vang = this.vang;
        }

        // Swap session and service references
        session.p = target;
        target.conn = session;
        this.conn = null;

        network.Service activeService = this.getService();
        target.setService(activeService);
        if (activeService != null) {
            activeService.setPlayer(target);
        }
        this.setService(null);

        // Remove both from Zone
        Zone z = this.map;
        if (z != null) {
            z.leave_map(this, 1);
            z.leave_map(target, 1);
        }

        // Ensure isDe is always consistent (DeTu is always true, Master is always false)
        this.isDe = (this instanceof model.DeTu);
        target.isDe = (target instanceof model.DeTu);

        // Fix: Reset isDeOnl properly after switch
        if (target instanceof model.DeTu) {
            // master -> detu: master không còn "cần update đệ" nữa (đệ đang active)
            this.isDeOnl = false;
        } else if (this instanceof model.DeTu) {
            // detu -> master: master cần biết đệ vẫn online để sync
            target.isDeOnl = true;
        }

        // Coordinates & Follower Setup
        if (target instanceof model.DeTu) {
            // master -> detu: detu takes master's current position
            target.x = this.x;
            target.y = this.y;
        } else {
            // detu -> master: master takes detu's position, detu is positioned beside master
            target.x = this.x;
            target.y = this.y;
            this.x = (short) (target.x + 25);
            this.y = (short) (target.y + 10);
            this.isdie = false;
            if (this.hp <= 0) {
                this.hp = this.ability != null ? this.ability.get_hp_max(true) : 100;
            }
            if (this.mp <= 0) {
                this.mp = this.ability != null ? this.ability.get_mp_max(true) : 100;
            }
        }

        // Register new active player in the active session map
        network.SessionManager.PLAYERS_MAP.remove(this.IDPlayer);
        network.SessionManager.PLAYERS_MAP.put(target.IDPlayer, target);

        // Recalculate stats for both characters
        this.setAbility();
        target.setAbility();

        // Reset cooldowns and ensure shortcut RMS slots are populated
        if (target.time_use_skill != null) {
            target.time_use_skill.clear();
        }
        target.ensureRms0();

        // Put target into the zone
        if (z != null) {
            target.map = z;
            
            // 1. Send Main_char_Info & Weapon_fashion first (just like login)
            //    isSwitch=true -> client sẽ clear skill/item cũ trước khi nhận data mới
            if (target.getService() != null) {
                target.getService().Main_char_Info(true);
                target.getService().Weapon_fashion(target, true);
            }
            
            // 2. Send settings/hotkeys (RMS sync) - phải gửi TRƯỚC skill list
            //    Để client có slot mapping mới trước khi nhận danh sách skill
            if (target.rms != null && session != null) {
                for (int i = 0; i < target.rms.length; i++) {
                    Message mRms = new Message(-33);
                    mRms.writer().writeByte(i);
                    if (target.rms[i] != null && target.rms[i].length > 0) {
                        mRms.writer().writeShort(target.rms[i].length);
                        mRms.writer().write(target.rms[i]);
                    } else {
                        // Gửi size=0 để client CLEAR slot này
                        mRms.writer().writeShort(0);
                    }
                    session.addmsg(mRms);
                    mRms.cleanup();
                }
            }

            // 3. Send skills - sau khi đã gửi RMS mới
            target.send_skill();

            // 4. Send ChangeMap (goto_map) to load the map (just like login)
            z.goto_map(target);
            try {
                Thread.sleep(150L);
            } catch (InterruptedException ignored) {}
            
            // 5. Fix: Send charWearing SAU goto_map với save_cache=false
            //    để client nhận đúng item data của target (không copy từ char cũ)
            if (target.getService() != null) {
                target.getService().charWearing(target, false);
            }
            
            // 6. Put follower into the zone (chỉ khi KHÔNG phải map PVP)
            this.map = z;
            if (z.map_vp == null) {
                // Chỉ đặt vào map khi đang switch đệ -> sư (this là DeTu, follower là đệ tử)
                // Khi switch sư -> đệ (target là DeTu), cất sư phụ đi, không hiển thị trên map
                if (this instanceof model.DeTu) {
                    z.enter_map(this);
                }
                // Ngược lại (this là master, target là DeTu): sư phụ đã được leave_map ở trên, không enter lại
            }
            
            // 7. Send inventories
            target.item.sendNumCellBag();
            target.item.sendNumCellBox();
            target.item.updateInventory(true);
            
            // 8. Send login_ok (just like login)
            if (target.getService() != null) {
                target.getService().login_ok(false);
                target.getService().sendSudoInfo(false);
            }
            
            // 9. Update info to others
            target.update_info_to_all();
            if (z.map_vp == null && z.players.contains(this)) {
                // Chỉ broadcast khi old player còn trong zone (switch đệ->sư: DeTu vẫn trong zone như follower)
                // Khi switch sư->đệ: master đã cất đi, không còn trong zone, bỏ qua
                this.update_info_to_all();
            }
            
            // 10. Clear stale menu state từ character cũ để NPC interaction tiếp theo không bị lỗi
            target.menus.clear();
            target.currentMenuOptions = null;
            this.menus.clear();
            this.currentMenuOptions = null;
        }
    }

    public boolean isBot = false;
    public boolean isPassGateCheck = false;
    public int killStreak = 0;
    public byte point_ww = 0;
    public short killWW = 0;
    public short deadWW = 0;
    public boolean isSpectator = false;
    public int map_id_spectator_old = -1;
    public short x_spectator_old = -1;
    public short y_spectator_old = -1;
    public int admin = 0;

    public void enterSpectatorMode(Zone targetZone, short targetX, short targetY) {
        try {
            if (targetZone == null) return;
            if (!this.isSpectator && this.map != null) {
                if (!Zone.map_cant_save_site(this.map.template.id)) {
                    this.save_previous_map();
                    this.map_id_spectator_old = this.map.template.id;
                    this.x_spectator_old = this.x;
                    this.y_spectator_old = this.y;
                } else {
                    if (this.pre_map_id > 0 && !Zone.map_cant_save_site(this.pre_map_id)) {
                        this.map_id_spectator_old = this.pre_map_id;
                        this.x_spectator_old = this.pre_x;
                        this.y_spectator_old = this.pre_y;
                    }
                }
            }
            this.isSpectator = true;
            this.type_pk = -1;

            // 1. Ẩn/cất đệ tử khỏi map
            if (this.detu != null) {
                if (this.detu.map != null) {
                    this.detu.map.leave_map(this.detu, 2);
                    this.detu.map = null;
                }
            }

            // 2. Ẩn/rời map toàn bộ lính đánh thuê khi vào chế độ xem trận
            bot.mercenary.MercenaryManager.gI().recallBots(this);

            // 3. Ẩn pet tàu
            if (this.ship_pet != null) {
                this.ship_pet.map = null;
            }

            // 4. Ẩn pet thường (MyPet)
            try {
                this.getService().pet(this, false);
            } catch (Exception ignored) {}

            map.Vgo vgo = new map.Vgo();
            vgo.map_go = new map.Zone[]{targetZone};
            vgo.xnew = targetX > 0 ? targetX : (short) (targetZone.template.maxW / 2);
            vgo.ynew = targetY > 0 ? targetY : (short) (targetZone.template.maxH / 2);
            this.goto_map(vgo);

            // Gửi lại gói ẩn pet khi đã vào map xem
            try {
                this.getService().pet(this, false);
            } catch (Exception ignored) {}
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public void exitSpectatorMode() {
        this.isSpectator = false;
        this.type_pk = -1;
        int returnMapId = (this.map_id_spectator_old > 0 && !Zone.map_cant_save_site(this.map_id_spectator_old))
                ? this.map_id_spectator_old
                : ((this.pre_map_id > 0 && !Zone.map_cant_save_site(this.pre_map_id))
                    ? this.pre_map_id
                    : ((this.id_map_save > 0 && !Zone.map_cant_save_site(this.id_map_save)) ? this.id_map_save : 1));

        short returnX = (this.x_spectator_old > 0) ? this.x_spectator_old : ((this.pre_x > 0) ? this.pre_x : -1);
        short returnY = (this.y_spectator_old > 0) ? this.y_spectator_old : ((this.pre_y > 0) ? this.pre_y : -1);
        byte returnZoneId = (this.pre_zone_id >= 0) ? this.pre_zone_id : 0;

        Zone[] zones = Zone.getMapByID(returnMapId);
        if (zones != null && zones.length > 0 && zones[0] != null) {
            try {
                Zone targetZone = zones[0];
                if (returnZoneId >= 0 && returnZoneId < zones.length && zones[returnZoneId] != null) {
                    targetZone = zones[returnZoneId];
                }

                if (returnX <= 0 || returnY <= 0) {
                    if (targetZone.template.vgos != null && !targetZone.template.vgos.isEmpty()) {
                        returnX = targetZone.template.vgos.get(0).xnew;
                        returnY = targetZone.template.vgos.get(0).ynew;
                    } else {
                        returnX = (short) (targetZone.template.maxW / 2);
                        returnY = (short) (targetZone.template.maxH / 2);
                    }
                }

                map.Vgo vgo = new map.Vgo();
                vgo.map_go = new map.Zone[]{targetZone};
                vgo.xnew = returnX;
                vgo.ynew = returnY;
                this.goto_map(vgo);

                // Hiện lại pet thường sau khi thoát chế độ xem
                try {
                    this.getService().pet(this, false);
                } catch (Exception ignored) {}

                // Hiện lại đệ tử nếu có và không phải ở nhà / hợp thể
                if (this.isDeOnl && this.detu != null && this.detu.detuStatus != DeTu.STATUS_HOME && this.detu.detuStatus != DeTu.STATUS_FUSION && this.map != null) {
                    this.detu.map = this.map;
                    this.detu.x = (short) (this.x + 30);
                    this.detu.y = this.y;
                    this.detu.xold = this.detu.x;
                    this.detu.yold = this.detu.y;
                    this.detu.syncMasterIdentity();
                    if (!this.map.players.contains(this.detu)) {
                        this.map.enter_map(this.detu);
                    }
                }

                // Triệu hồi lại lính đánh thuê cạnh người chơi sau khi thoát chế độ xem
                List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(this);
                if (activeMercs != null && this.map != null && !bot.mercenary.MercenaryBot.isRestrictedPvpMap(this.map)) {
                    for (bot.mercenary.MercenaryBot merc : activeMercs) {
                        if (merc != null && !merc.isdie) {
                            if (merc.map == null || !merc.map.equals(this.map)) {
                                merc.leave();
                                merc.syncOwnerIdentity(this);
                                merc.join(this.map, (short) (this.x + merc.getOffsetX(this)), (short) (this.y + merc.getOffsetY(this)));
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Player.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        } else {
            activities.TimedDungeonManager.teleportPlayerToMap(this, 1);
        }

        this.map_id_spectator_old = -1;
        this.x_spectator_old = -1;
        this.y_spectator_old = -1;
        this.pre_map_id = -1;
        this.pre_zone_id = -1;
        this.pre_x = -1;
        this.pre_y = -1;
    }

    // === Vị trí bản đồ trước khi vào PvP / Phó Bản ===
    public int pre_map_id = -1;
    public byte pre_zone_id = -1;
    public short pre_x = -1;
    public short pre_y = -1;

    /**
     * Lưu lại vị trí bản đồ hiện tại trước khi vào map PvP hoặc Phó Bản.
     * Chỉ lưu nếu đang đứng ở bản đồ bình thường (không phải phó bản, đấu trường, hoặc map không cho lưu site).
     */
    public void save_previous_map() {
        if (this.map != null && this.map.template != null) {
            int currentMapId = this.map.template.id;
            // Nếu đã lưu map ngoài hợp lệ rồi thì không ghi đè khi đang chuyển tầng phó bản
            if (this.pre_map_id > 0 && !Zone.map_cant_save_site(this.pre_map_id) && !Zone.is_map_dungeon(this.pre_map_id)) {
                return;
            }
            if (!this.map.isDungeon() && !Zone.map_cant_save_site(currentMapId) && !Zone.is_map_dungeon(currentMapId)
                    && this.map.map_vp == null && this.map.map_dungeon == null && this.dungeon == null) {
                this.pre_map_id = currentMapId;
                this.pre_zone_id = this.map.zone_id;
                this.pre_x = (this.x > 0) ? this.x : (short) (this.map.template.maxW / 2);
                this.pre_y = (this.y > 0) ? this.y : (short) (this.map.template.maxH / 2);
            }
        }
    }

    public void save_previous_map(Zone fromZone, short fromX, short fromY) {
        if (fromZone != null && fromZone.template != null) {
            int fromMapId = fromZone.template.id;
            // Nếu đã lưu map ngoài hợp lệ rồi thì không ghi đè khi đang chuyển tầng phó bản
            if (this.pre_map_id > 0 && !Zone.map_cant_save_site(this.pre_map_id) && !Zone.is_map_dungeon(this.pre_map_id)) {
                return;
            }
            if (!fromZone.isDungeon() && !Zone.map_cant_save_site(fromMapId) && !Zone.is_map_dungeon(fromMapId)
                    && fromZone.map_vp == null && fromZone.map_dungeon == null && this.dungeon == null) {
                this.pre_map_id = fromMapId;
                this.pre_zone_id = fromZone.zone_id;
                this.pre_x = (fromX > 0) ? fromX : (short) (fromZone.template.maxW / 2);
                this.pre_y = (fromY > 0) ? fromY : (short) (fromZone.template.maxH / 2);
            }
        }
    }

    /**
     * Đưa người chơi trở về bản đồ và tọa độ trước khi vào PvP / Phó Bản.
     * Nếu không có bản đồ hợp lệ được lưu, sẽ fallback về map làng tương ứng (hoặc id_map_save).
     */
    public void return_to_previous_map() {
        try {
            int targetMapId = this.pre_map_id;
            byte targetZoneId = this.pre_zone_id;
            short targetX = this.pre_x;
            short targetY = this.pre_y;

            // Xóa cache vị trí trước đó
            this.pre_map_id = -1;
            this.pre_zone_id = -1;
            this.pre_x = -1;
            this.pre_y = -1;

            if (targetMapId <= 0 || Zone.map_cant_save_site(targetMapId) || Zone.is_map_dungeon(targetMapId)) {
                // Ưu tiên fallback về làng của khu vực/đảo hiện tại (hoặc Thị Trấn Whiskey cho đấu trường)
                if (this.map != null && this.map.template != null) {
                    int village = Zone.getVillageMapId(this.map.template.id);
                    if (village > 0 && !Zone.map_cant_save_site(village) && !Zone.is_map_dungeon(village)) {
                        targetMapId = village;
                    }
                }
                // Nếu vẫn chưa có thì lấy id_map_save của người chơi
                if (targetMapId <= 0 || Zone.map_cant_save_site(targetMapId) || Zone.is_map_dungeon(targetMapId)) {
                    targetMapId = (this.id_map_save > 0 && !Zone.map_cant_save_site(this.id_map_save) && !Zone.is_map_dungeon(this.id_map_save)) ? this.id_map_save : 1;
                }
                targetX = -1;
                targetY = -1;
            }

            Zone[] zones = Zone.getMapByID(targetMapId);
            if (zones != null && zones.length > 0 && zones[0] != null) {
                Zone targetZone = zones[0];
                if (targetZoneId >= 0 && targetZoneId < zones.length && zones[targetZoneId] != null) {
                    targetZone = zones[targetZoneId];
                }

                if (targetX <= 0 || targetY <= 0) {
                    if (targetZone.template.vgos != null && !targetZone.template.vgos.isEmpty()) {
                        targetX = targetZone.template.vgos.get(0).xnew;
                        targetY = targetZone.template.vgos.get(0).ynew;
                    } else {
                        targetX = (short) (targetZone.template.maxW / 2);
                        targetY = (short) (targetZone.template.maxH / 2);
                    }
                }

                map.Vgo vgo = new map.Vgo();
                vgo.map_go = new Zone[]{targetZone};
                vgo.xnew = targetX;
                vgo.ynew = targetY;
                this.goto_map(vgo);
            } else {
                activities.TimedDungeonManager.teleportPlayerToMap(this, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            activities.TimedDungeonManager.teleportPlayerToMap(this, 1);
        }
    }

    public void update_hp() {
        if (this.map != null) {
            try {
                this.map.update_hp_mp_eff(this, null, 1, 0);
            } catch (Exception ignored) {}
        }
    }

    public void update_mp() {
        if (this.map != null) {
            try {
                this.map.update_hp_mp_eff(this, null, 1, 0);
            } catch (Exception ignored) {}
        }
    }

    public long pvpSearchStartTime = 0;
    public long pvpSearchDuration = 0;
    public Player lastAttacker = null;
    public long lastAttackedTime = 0;
    public boolean isClosed;
    // === Hợp Thể (Fusion) bonus fields ===
    public int fusionBonusDame = 0;  // Dame flat cộng từ hợp thể đệ tử
    public int fusionBonusHp   = 0;  // HP flat cộng từ hợp thể đệ tử
    public int fusionBonusMp   = 0;  // MP flat cộng từ hợp thể đệ tử
    public int fusionBonusDef  = 0;  // DEF flat cộng từ hợp thể đệ tử
    public boolean isFusion    = false; // Đang trong trạng thái hợp thể

    public boolean isRegisteredDungeon() {
        if (this.tableTickOption != null && this.tableTickOption.player != null && this.tableTickOption.player.equals(this)) return true;
        if (TranChienKhongLo.isPlayerInQueue(this)) return true;
        if (this.map != null && (Zone.is_map_dungeon(this.map.template.id) || this.map.map_DaoKhoBau != null || this.map.mapSanTrum != null || this.map.map_little_garden != null || this.map.map_thuLinhBienKhoi != null || this.map.map_vp != null)) {
            return true;
        }
        return false;
    }

    public HashMap<Integer, Long> time_use_skill = new HashMap<>();
    public long timeMoveBot;
    public long timeAtkBot;
    public long timeChatBot;
    public long timeLogBot;
    public long timeLogDetect;
    public Session conn;
    public transient java.util.List<mob.Mob> tempPokemonTargets;
    public short index_map;
    public int IDPlayer;
    public byte clazz;
    public byte tempTargetClazz;
    public String name;
    public short x;
    public short y;
    public boolean isMute = false;
    public int hp;
    public int mp;
    // === Calculated Stat Variables (Ability Cache) ===
    public int dame;
    public int def;
    public int hpMax;
    public int mpMax;
    public int agility;
    public int crit;
    public int pierce;
    public int miss;
    public int reactDame;
    public int resPhys;
    public int resMag;
    public int damePercent;
    public int defPercent;
    public int[] optionParams = new int[Math.max(256, template.ItemOptionTemplate.ENTRYS != null ? template.ItemOptionTemplate.ENTRYS.size() : 200)];
    public long time_regen_10s = 0L;
    public short level;
    public long exp;
    public long point1;
    public long point2;
    public long point3;
    public long point4;
    public long point5;
    public long timeEff;
    public Zone map;
    public int map_remove_type = 0;
    public int xold;
    public int yold;
    public short lastValidX = -1;
    public short lastValidY = -1;
    public long lastMovePacketTime = 0L;
    public boolean isdie;
    public byte last_index_join_item;
    public Set<String> id_meet_in_map = new HashSet<>();
    public int[] data_super_upgrade;
    public BlockingQueue<Integer> key_red_line = new LinkedBlockingQueue<>();
    public int time_key_red_line;
    public long time_pick_item_other;
    public long cd_ticket_next;
    public long cd_keyboss_next;
    public long cd_pvp_next;
    public byte[] is_combo;
    public long time_combo;
    public String[] data_yesno_gem;
    public int fee_trade;
    public byte danhLaChoang;
    public byte thanhLoc;
    public byte nenDau;
    public byte giaiPhongNangLuong;
    public Clan clan;
    /**
     * Danh sách phó bản player đang tham gia.
     * Key = getDungeonId() của AbsDungeon (số âm, ví dụ: -64, -65...).
     * Hỗ trợ đồng thời nhiều phó bản khác nhau (LienTang + ChiemDao...).
     */
    public java.util.Map<Integer, zabstracts.AbsDungeon> dungeons
            = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * [Compat helper] Trả về dungeon đầu tiên đang tham gia, hoặc null nếu không có.
     * Dùng cho code cũ chỉ cần 1 dungeon.
     */
    public zabstracts.AbsDungeon getDungeon() {
        if (dungeons == null || dungeons.isEmpty()) return null;
        return dungeons.values().iterator().next();
    }

    /**
     * Trả về dungeon theo dungeonId, hoặc null nếu không tham gia dungeon đó.
     */
    public zabstracts.AbsDungeon getDungeon(int dungeonId) {
        return dungeons == null ? null : dungeons.get(dungeonId);
    }

    /**
     * Thêm dungeon vào danh sách (khi join).
     */
    public void addDungeon(zabstracts.AbsDungeon d) {
        if (d == null) return;
        if (dungeons == null) dungeons = new java.util.concurrent.ConcurrentHashMap<>();
        dungeons.put(d.getDungeonId(), d);
        this.dungeon = d;
    }

    /**
     * Xóa dungeon khỏi danh sách (khi leave hoặc end).
     */
    public void removeDungeon(zabstracts.AbsDungeon d) {
        if (d == null || dungeons == null) return;
        dungeons.remove(d.getDungeonId());
        if (this.dungeon == d) {
            this.dungeon = getDungeon();
        }
    }

    /**
     * Xóa dungeon khỏi danh sách theo ID.
     */
    public void removeDungeon(int dungeonId) {
        if (dungeons == null) return;
        zabstracts.AbsDungeon removed = dungeons.remove(dungeonId);
        if (this.dungeon != null && this.dungeon == removed) {
            this.dungeon = getDungeon();
        }
    }

    /**
     * Kiểm tra player có đang trong bất kỳ dungeon nào không.
     */
    public boolean isInAnyDungeon() {
        return dungeons != null && !dungeons.isEmpty();
    }

    /**
     * Kiểm tra player có đang trong dungeon với ID cụ thể không.
     */
    public boolean isInDungeon(int dungeonId) {
        return dungeons != null && dungeons.containsKey(dungeonId);
    }

    /**
     * Xóa toàn bộ dungeon (dùng khi logout hoặc kick toàn bộ).
     */
    public void clearAllDungeons() {
        if (dungeons != null) dungeons.clear();
        this.dungeon = null;
    }

    /** Compat: some older code still uses p.dungeon as a single dungeon field. */
    public zabstracts.AbsDungeon dungeon;

    public zabstracts.AbsDungeon getCurrentDungeon() {
        if (dungeon != null) return dungeon;
        return getDungeon();
    }

    public void setCurrentDungeon(zabstracts.AbsDungeon d) {
        this.dungeon = d;
        if (d != null) {
            addDungeon(d);
        } else {
            clearAllDungeons();
        }
    }

    public Player pvp_target;
    public boolean pvp_accept;
    public boolean isBackTypePk;
    public int pvp_win;
    public int pvp_lose;
    public short id_ship_packet = -1;
    public Ship_pet ship_pet;
    public byte time_ship;
    public byte time_can_hs;

    public List<FriendTemp> enemy_list = new ArrayList<>();
    public zabstracts.AbsTableTickOption tableTickOption;
    public String[] name_ThoSanHaiTac;
    public Upgrade_Skin_Info upgrade_skin;
    public byte[] tool_dial;
    public List<ItemBag47> daHanhTrinh = new ArrayList<>();
    public List<Skill_info> tempSkillList;

    // === Craft & Upgrade Active State Objects ===
    private zabstracts.AbsCheTao currentCheTao;
    private zabstracts.AbsUpgrade currentUpgrade;
    private zabstracts.AbsUpgradeDevil currentUpgradeDevil;
    private zabstracts.AbsCombie currentCombie;

    public zabstracts.AbsCheTao getCheTao() {
        return this.currentCheTao;
    }

    public void setCheTao(zabstracts.AbsCheTao cheTao) {
        this.currentCheTao = cheTao;
    }

    public boolean showCheTao(int resultId) throws java.io.IOException {
        zabstracts.AbsCheTao cheTao = zabstracts.AbsCheTao.get(resultId);
        if (cheTao != null) {
            setCheTao(cheTao);
            cheTao.show_table(this);
            return true;
        }
        return false;
    }

    public zabstracts.AbsUpgrade getUpgrade() {
        return this.currentUpgrade;
    }

    public void setUpgrade(zabstracts.AbsUpgrade upgrade) {
        this.currentUpgrade = upgrade;
    }

    public boolean showUpgrade(byte type, byte action, short idItem, byte cat, short num) throws java.io.IOException {
        zabstracts.AbsUpgrade upgrade = zabstracts.AbsUpgrade.get(type);
        if (upgrade != null) {
            setUpgrade(upgrade);
            upgrade.process(this, type, action, idItem, cat, num);
            return true;
        }
        return false;
    }

    public zabstracts.AbsUpgradeDevil getUpgradeDevil() {
        return this.currentUpgradeDevil;
    }

    public void setUpgradeDevil(zabstracts.AbsUpgradeDevil upgradeDevil) {
        this.currentUpgradeDevil = upgradeDevil;
    }

    public boolean showUpgradeDevil(int id) throws java.io.IOException {
        zabstracts.AbsUpgradeDevil handler = zabstracts.AbsUpgradeDevil.get(id);
        if (handler != null) {
            setUpgradeDevil(handler);
            handler.show_table(this);
            return true;
        }
        return false;
    }

    public zabstracts.AbsCombie getCombie() {
        return this.currentCombie;
    }

    public void setCombie(zabstracts.AbsCombie combie) {
        this.currentCombie = combie;
    }

    public boolean showCombie(byte type, byte action, short idItem, byte cat, short num) throws java.io.IOException {
        zabstracts.AbsCombie combie = zabstracts.AbsCombie.get(type);
        if (combie != null) {
            setCombie(combie);
            combie.process(this, type, action, idItem, cat, num);
            return true;
        }
        return false;
    }
    public int tichLuy;
    public short ticket;
    public long vang;
    public int kimcuong;
    public int ruby;
    private int point_tich_tieu;
    public int level_so_tay;
    public int exp_so_tay;
    public int point_hang_dong;
    public int hd_max;
    public int diem_danh_event;
    public int so_tay_vip;
    public int active_so_tay;
    public boolean ischangemap = true;
    public short spawnX = -1;
    public short spawnY = -1;
    public boolean hasMovedFromSpawn = false;
    public short thongthao;
    public int pointPk;
    public long pointAttribute;
    public int typePirate = -1;
    public int indexGhostServer;
    public int pointSkill;
    public short head;
    public short hair;
    public short part_body = -1;
    public short part_leg = -1;
    public short part_ring = -1;
    public short part_weapon = -1;
    // Thần Trang Transformation State
    public boolean isTransformThanTrang = false;
    public int transformThanTrangSetId = 0;
    public long lastTransformThanTrangTime = 0L;
    public long transformThanTrangEndTime = 0L;
    public Item item = new Item(this);
    public Ability ability = new Ability(this);
    public BlockingQueue<Message> msgs = new LinkedBlockingQueue<>();
    public byte type_pk = -1;
    public ItemMap[] it_map;
    public List<EffTemplate> list_eff = new CopyOnWriteArrayList<>();
    public List<ActiveBuff> active_buffs = new CopyOnWriteArrayList<>();
    public long time_chat_ktg;
    public int use_item_3;
    public int[] tool_upgrade;
    public short item_upgrade_index = -1;
    public byte[][] rms;
    public Object currentMenuOptions;
    /** NPC ID đang được player mở menu (set bởi sendMenu, dùng trong handleMenu). */
    public short currentNpcId = 0;
    /** NPC ID sở hữu danh sách menus / currentMenuOptions hiện tại để chống đè/chạy nhầm handler của NPC khác. */
    public short currentMenuNpcId = 0;
    public List<model.Menu> menus = new ArrayList<>();

    public void resetMenuState() {
        if (this.menus != null) {
            this.menus.clear();
        }
        this.currentMenuOptions = null;
        this.currentNpcId = 0;
        this.currentMenuNpcId = 0;
    }

    public void ensureRms0() {
        if (this.rms == null || this.rms.length < 11) {
            byte[][] newRms = new byte[11][];
            if (this.rms != null) {
                System.arraycopy(this.rms, 0, newRms, 0, Math.min(this.rms.length, 11));
            }
            for (int i = 0; i < 11; i++) {
                if (newRms[i] == null) newRms[i] = new byte[0];
            }
            this.rms = newRms;
            if (this.rms[4] == null || this.rms[4].length == 0) {
                this.rms[4] = new byte[]{0, 18};
            }
        }
        if (this.rms[0] == null || this.rms[0].length == 0) {
            this.rms[0] = generateDefaultRms0();
        }
        if (this.rms[7] == null || this.rms[7].length == 0 || this.rms[7][0] == 0) {
            this.rms[7] = new byte[]{1};
        }
    }

    public byte[] generateDefaultRms0() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            // Player.hotkeyPlayer có 2 tab (j=0..1), mỗi tab 6 ô (k=0..5) -> tổng cộng 12 ô
            for (int tab = 0; tab < 2; tab++) {
                for (int slot = 0; slot < 6; slot++) {
                    if (slot == 2) {
                        // Nút đánh chính (imgFire giữa màn hình) -> Gán Chiêu 1 (ID 0)
                        dos.writeByte(1); // 1 = skill
                        dos.writeShort(0); // ID 0
                    } else if (slot == 0) {
                        // Nút phụ bên trái -> Gán Chiêu 4 (Thủy chiến - ID 3)
                        dos.writeByte(1); // 1 = skill
                        dos.writeShort(3); // ID 3
                    } else {
                        dos.writeByte(-1); // Trống
                    }
                }
            }
            dos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public boolean updateThanTrangRmsHotKey() {
        int fullSetId = ThanTrangConfig.getFullSetId(this);
        if (fullSetId > 0 && fullSetId <= ThanTrangConfig.TOTAL_SETS) {
            return updateThanTrangRmsHotKey(ThanTrangConfig.getSkillBySetId(fullSetId));
        }
        return false;
    }

    public boolean updateThanTrangRmsHotKey(int targetSkillId) {
        if (this.rms == null || this.rms.length == 0 || this.rms[0] == null || this.rms[0].length == 0) {
            return false;
        }
        if (!ThanTrangConfig.isThanTrangSkill(targetSkillId)) {
            return false;
        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(this.rms[0]);
            DataInputStream dis = new DataInputStream(bais);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            boolean modified = false;

            while (dis.available() > 0) {
                byte type = dis.readByte();
                dos.writeByte(type);
                if (type == -1) {
                    // Ô trống, không có byte theo sau
                } else if (type == 0) {
                    // Potion / Vật phẩm: 2 bytes itemId theo sau
                    if (dis.available() >= 2) {
                        short itemId = dis.readShort();
                        dos.writeShort(itemId);
                    }
                } else if (type == 1) {
                    // Kỹ năng: 2 bytes skillId theo sau
                    if (dis.available() >= 2) {
                        short skillId = dis.readShort();
                        if (ThanTrangConfig.isThanTrangSkill(skillId) || (skillId >= 4001 && skillId <= 4080)) {
                            if (skillId != targetSkillId) {
                                dos.writeShort(targetSkillId);
                                modified = true;
                                continue;
                            }
                        }
                        dos.writeShort(skillId);
                    }
                }
            }

            if (modified) {
                dos.flush();
                this.rms[0] = baos.toByteArray();
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }
    public List<Skill_info> skill_point = new ArrayList<>();
    public List<Skill_info> list_can_combo = new ArrayList<>();
    public Item_wear item_chuyenhoa_save_0;
    public Item_wear item_chuyenhoa_save_1;
    public Item_wear item_to_kham_ngoc;
    public short item_to_kham_ngoc_id_ngoc;
    public Player trade_target;
    public List<Item_wear> list_item_trade3 = new ArrayList<>();
    public List<ItemBag47> list_item_trade47 = new ArrayList<>();
    public long money_trade;
    public boolean is_lock_trade;
    public boolean is_accept_trade;
    public List<FriendTemp> friend_list = new ArrayList<>();
    public List<ItemFashionP2> fashion = new ArrayList<>();
    public List<ItemFashionP> itfashionP = new ArrayList<>();
    public long time_buff_hp_mp;
    public long time_potion_tick;
    public List<QuestP> list_quest = new ArrayList<>();
    public DateTime date;

    public String getPlayerCreatedAt() {
        return this.date != null ? this.date.toString() : "";
    }

    public String getAccountCreatedAt() {
        if (this.conn != null && this.conn.accCreatedAt != null && !this.conn.accCreatedAt.isEmpty()) {
            return this.conn.accCreatedAt;
        }
        return historys.HistoryManager.getAccountCreatedAt(this.IDPlayer);
    }

    public String getUsername() {
        if (this.conn != null && this.conn.user != null && !this.conn.user.isEmpty()) {
            return this.conn.user;
        }
        return historys.HistoryManager.getUsernameByPlayerId(this.IDPlayer);
    }

    public int getAccountId() {
        if (this.conn != null && this.conn.idUser > 0) {
            return this.conn.idUser;
        }
        return historys.HistoryManager.getAccountIdByPlayerId(this.IDPlayer);
    }

    public String getPlayerMd5Signature() {
        return historys.HistoryManager.getPlayerMd5(this);
    }

    public long timeOnline = 0L;
    public long loginTime = 0L;

    public long getTotalOnlineSeconds() {
        long sessionSec = 0L;
        if (this.loginTime > 0) {
            sessionSec = (System.currentTimeMillis() - this.loginTime) / 1000L;
        } else if (this.conn != null && this.conn.loginTime > 0) {
            sessionSec = (System.currentTimeMillis() - this.conn.loginTime) / 1000L;
        }
        return this.timeOnline + sessionSec;
    }

    public void saveOnlineTime() {
        long totalSec = getTotalOnlineSeconds();
        this.timeOnline = totalSec;
        this.loginTime = System.currentTimeMillis();
        if (this.conn != null) {
            this.conn.timeOnline = totalSec;
            this.conn.loginTime = System.currentTimeMillis();
        }
    }

    public boolean isOnlineEnough(int requiredHours) {
        if (requiredHours <= 0) return true;
        long requiredSeconds = (long) requiredHours * 3600L;
        return getTotalOnlineSeconds() >= requiredSeconds;
    }

    public String getOnlineDurationString() {
        long totalSec = getTotalOnlineSeconds();
        long hours = totalSec / 3600L;
        long mins = (totalSec % 3600L) / 60L;
        return hours + " giờ " + mins + " phút";
    }

    public Party party;
    public long betAmountFight;
    public String mbv;
    public boolean passBagOK;
    public long timeHuyMbv;
    public int[] data_yesno;
    /** Danh sách quà top phó bản chờ nhận (DungeonGiftMenu). Transient, không save DB. */
    public java.util.List<historys.DungeonRewardHistory.PendingReward> pendingDungeonRewards;
    public int[] map_tele;
    public int id_map_save = 1;
    public boolean wait_change_map;
    public List<ItemBoatP> itemboat = new ArrayList<>();
    public boolean is_show_hat;
    public boolean is_hide_fashion_hair = false;
    public boolean is_hide_fashion_head = false;
    public long vnd;
    public long bua;
    public byte percent_da_sieu_cap;
    public short pvp_ticket;
    public short key_boss;
    public MapBossInfo map_boss_info;
    public long pointAttributeThongThao;
    public List<Option> list_op_thongthao = new ArrayList<>();
    public short tocSuper;
    public int pvppoint;
    public int time_nvl;
    public long time_hs_little_garden;
    public boolean isTachTB = false;
    public byte time_ttvt;
    public long time_skill_decrease;
    public long time_can_mob_atk;
    public boolean is_show_weapon;
    public Player targetFight;
    public int wanted_point;
    public int tieuRuby;
    public int tieuTuan;
    public int tieuTong;
    public Wanted_Chest[] wanted_chest = new Wanted_Chest[2];
    public List<MyPet> my_pet = new ArrayList<>();
    public long time_change_map;
    public int diemdanh;
    public int diemdanh_ngay;
    // [RAM-FIX] Dùng ConcurrentHashMap thay long[10000] (78KB/player) — chỉ cấp entry khi cần
    private final java.util.concurrent.ConcurrentHashMap<Integer, Long> time_sk_map = new java.util.concurrent.ConcurrentHashMap<>();
    private final java.util.concurrent.ConcurrentHashMap<Integer, Long> time_it_map = new java.util.concurrent.ConcurrentHashMap<>();

    /** Lấy thời điểm kết thúc cooldown của skill ID. Trả 0 nếu chưa dùng lần nào (sẵn sàng). */
    public long getSkCooldown(int skillId) {
        Long t = time_sk_map.get(skillId);
        return (t != null) ? t : 0L;
    }

    /** Ghi thời điểm kết thúc cooldown cho skill ID. */
    public void setSkCooldown(int skillId, long timestamp) {
        time_sk_map.put(skillId, timestamp);
    }

    /** Xóa toàn bộ cooldown kỹ năng để có thể dùng chiêu ngay lập tức (dùng khi hồi sinh / hiệp đấu mới) */
    public void resetAllSkillCooldowns() {
        time_sk_map.clear();
    }

    /** Dung sai mạng tối đa (ms) cho phép gói tin đến sớm do network jitter / TCP buffering */
    public static final long NETWORK_LATENCY_TOLERANCE_MS = 150L;

    /** Kiểm tra xem kỹ năng có sẵn sàng xuất chiêu (đã hồi chiêu xong) hay không. */
    public boolean isSkillReady(int skillId) {
        long cd = getSkCooldown(skillId);
        if (cd <= 0L) return true;
        return (cd - NETWORK_LATENCY_TOLERANCE_MS <= System.currentTimeMillis());
    }

    /** Tính thời gian hồi chiêu thực tế (ms) của kỹ năng, xét thân pháp và hiệu ứng làm chậm hồi chiêu. */
    public long calculateSkillCooldown(skill.Skill_Template sk_temp) {
        if (sk_temp == null) return 0L;
        long delay = sk_temp.timeDelay;
        if (delay <= 0) return 0L;

        // Hiệu ứng làm chậm hồi chiêu +50% thời gian (Eff 208)
        if (this.get_eff(208) != null) {
            delay += delay / 2;
        }

        int agility = (this.ability != null) ? this.ability.get_agility(true) : 0;
        long cd = delay - ((delay * (long) agility) / 1000L);
        if (cd < 350L) {
            cd = 350L; // Giới hạn sàn tối thiểu 350ms (chuẩn đồng bộ Server & Client)
        }
        return cd;
    }

    /** Đăng ký cooldown kỹ năng sau khi xuất chiêu thành công.
     * Sử dụng rolling window: nếu gói tin đến sớm trong khoảng dung sai mạng, tiếp nối cooldown từ mốc cũ để tránh bug hụt đòn mà vẫn chống hack spam.
     */
    public void applySkillCooldown(int skillId, skill.Skill_Template sk_temp) {
        long cdDuration = calculateSkillCooldown(sk_temp);
        if (cdDuration <= 0L) return;
        long now = System.currentTimeMillis();
        long oldCd = getSkCooldown(skillId);
        long baseTime = (oldCd > now && (oldCd - now) <= NETWORK_LATENCY_TOLERANCE_MS) ? oldCd : now;
        long expireTime = baseTime + cdDuration;
        setSkCooldown(skillId, expireTime);
        if (sk_temp != null) {
            if (sk_temp.ID >= 0) setSkCooldown(sk_temp.ID, expireTime);
            if (sk_temp.indexSkillInServer > 0) setSkCooldown(sk_temp.indexSkillInServer, expireTime);
        }
    }

    /** Lấy thời điểm kết thúc cooldown của item (theo key cooldown). */
    public long getItemCooldown(int itemKey) {
        Long t = time_it_map.get(itemKey);
        return (t != null) ? t : 0L;
    }

    /** Ghi thời điểm kết thúc cooldown cho item. */
    public void setItemCooldown(int itemKey, long timestamp) {
        time_it_map.put(itemKey, timestamp);
    }

    /** Xóa toàn bộ cooldown item */
    public void resetAllItemCooldowns() {
        time_it_map.clear();
    }

    /** Kiểm tra xem item có sẵn sàng sử dụng hay không. Dung sai mạng 50ms. */
    public boolean isItemReady(int itemKey) {
        long cd = getItemCooldown(itemKey);
        if (cd <= 0L) return true;
        return (cd - 50L <= System.currentTimeMillis());
    }

    /** Đăng ký cooldown item sau khi dùng thành công. */
    public void applyItemCooldown(int itemKey, long durationMs) {
        if (durationMs <= 0L) {
            setItemCooldown(itemKey, 0L);
            return;
        }
        setItemCooldown(itemKey, System.currentTimeMillis() + durationMs);
    }

    /** Lấy key cooldown của item dựa vào type và ID template 4. */
    public static int getItemCooldownKey(template.ItemTemplate4 it_temp) {
        if (it_temp == null) return 0;
        if (it_temp.type == 1) return 501; // Bình HP
        if (it_temp.type == 2) return 502; // Bình MP
        if (it_temp.type > 2) return 500 + it_temp.type;
        return 10000 + it_temp.id;
    }
    public long limitTime;
    public byte countSkill;
    public int sanTrumId,numSanTrum,sanTrumLv;
    public boolean[] sanTrumTick;
    public int coin;
    public int tongnap;
    public int tongnap2;
    public int vip;
    public int huongnghiep;
    public boolean is_complete_dungeon;
    public byte[] tichTieuCheck;
    public byte[] tichHangNgayCheck;
    public byte[] tieuRubyCheck;
    public byte[] tieuTuanCheck;
    public byte[] tieuTongCheck;
    public byte[] tichNapTuanCheck;
    public LocalDateTime timeresetHangNgay = java.time.LocalDateTime.of(java.time.LocalDate.now(), java.time.LocalTime.MIDNIGHT);
    public LocalDateTime timeresetTuan = java.time.LocalDateTime.of(java.time.LocalDate.now(), java.time.LocalTime.MIDNIGHT);
    public int[] eff_save;
    public int aiDonLevel;
    public List<int[]> id_danh_hieu_da_so_huu = new ArrayList<>();
    public int id_danh_hieu_su_dung = -1;
    public int id_danh_hieu_doi = -1;
    public java.util.Set<Integer> disabledEffects = new java.util.HashSet<>();

    public void ensureTichHangNgayCheck() {
        if (this.tichHangNgayCheck == null || this.tichHangNgayCheck.length < ListNapHangNgay.ENTRY.size()) {
            byte[] newArr = new byte[ListNapHangNgay.ENTRY.size()];
            if (this.tichHangNgayCheck != null) {
                System.arraycopy(this.tichHangNgayCheck, 0, newArr, 0, Math.min(this.tichHangNgayCheck.length, newArr.length));
            }
            this.tichHangNgayCheck = newArr;
        }
    }

    public void ensureTieuRubyCheck() {
        if (this.tieuRubyCheck == null || this.tieuRubyCheck.length < TichTieuRuby.ENTRY.size()) {
            byte[] newArr = new byte[TichTieuRuby.ENTRY.size()];
            if (this.tieuRubyCheck != null) {
                System.arraycopy(this.tieuRubyCheck, 0, newArr, 0, Math.min(this.tieuRubyCheck.length, newArr.length));
            }
            this.tieuRubyCheck = newArr;
        }
    }

    public void ensureTichNapCheck() {
        if (this.tichTieuCheck == null || this.tichTieuCheck.length < ListTichNap.ENTRY.size()) {
            byte[] newArr = new byte[ListTichNap.ENTRY.size()];
            if (this.tichTieuCheck != null) {
                System.arraycopy(this.tichTieuCheck, 0, newArr, 0, Math.min(this.tichTieuCheck.length, newArr.length));
            }
            this.tichTieuCheck = newArr;
        }
    }

    public void ensureTieuTuanCheck() {
        if (this.tieuTuanCheck == null || this.tieuTuanCheck.length < TichTieuTuan.ENTRY.size()) {
            byte[] newArr = new byte[TichTieuTuan.ENTRY.size()];
            if (this.tieuTuanCheck != null) {
                System.arraycopy(this.tieuTuanCheck, 0, newArr, 0, Math.min(this.tieuTuanCheck.length, newArr.length));
            }
            this.tieuTuanCheck = newArr;
        }
    }

    public void ensureTieuTongCheck() {
        if (this.tieuTongCheck == null || this.tieuTongCheck.length < TichTieuTong.ENTRY.size()) {
            byte[] newArr = new byte[TichTieuTong.ENTRY.size()];
            if (this.tieuTongCheck != null) {
                System.arraycopy(this.tieuTongCheck, 0, newArr, 0, Math.min(this.tieuTongCheck.length, newArr.length));
            }
            this.tieuTongCheck = newArr;
        }
    }

    public void ensureTichNapTuanCheck() {
        if (this.tichNapTuanCheck == null || this.tichNapTuanCheck.length < ListTichNapTuan.ENTRY.size()) {
            byte[] newArr = new byte[ListTichNapTuan.ENTRY.size()];
            if (this.tichNapTuanCheck != null) {
                System.arraycopy(this.tichNapTuanCheck, 0, newArr, 0, Math.min(this.tichNapTuanCheck.length, newArr.length));
            }
            this.tichNapTuanCheck = newArr;
        }
    }

    public synchronized void resetWeeklyData(boolean updateDb) {
        this.tieuTuan = 0;
        this.ensureTieuTuanCheck();
        if (this.tieuTuanCheck != null) {
            for (int i = 0; i < this.tieuTuanCheck.length; i++) {
                this.tieuTuanCheck[i] = 0;
            }
        }
        this.ensureTichNapTuanCheck();
        if (this.tichNapTuanCheck != null) {
            for (int i = 0; i < this.tichNapTuanCheck.length; i++) {
                this.tichNapTuanCheck[i] = 0;
            }
        }
        if (updateDb) {
            this.flush(this, false);
        }
    }

    public synchronized void resetDailyData(boolean updateDb) {
        java.time.LocalDate currentDate = java.time.LocalDate.now();
        this.timeresetHangNgay = java.time.LocalDateTime.of(currentDate, java.time.LocalTime.MIDNIGHT);
        if (this.conn != null) {
            this.conn.naphangngay = 0;
            this.conn.update_naphangngay();
        }
        this.ensureTichHangNgayCheck();
        for (int i = 0; i < ListNapHangNgay.ENTRY.size() && i < this.tichHangNgayCheck.length; i++) {
            this.tichHangNgayCheck[i] = 0;
        }
        this.tieuRuby = 0;
        this.ensureTieuRubyCheck();
        for (int i = 0; i < TichTieuRuby.ENTRY.size() && i < this.tieuRubyCheck.length; i++) {
            this.tieuRubyCheck[i] = 0;
        }
        if (currentDate.getDayOfWeek() == java.time.DayOfWeek.MONDAY || (this.timeresetTuan != null && currentDate.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR) != this.timeresetTuan.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR))) {
            this.timeresetTuan = java.time.LocalDateTime.of(currentDate, java.time.LocalTime.MIDNIGHT);
            this.resetWeeklyData(false);
        }
        event.EventData temp_select = this.getDataEvent(2);
        if (temp_select != null && temp_select.data != null && temp_select.data.length > 5) {
            temp_select.data[5] = 0;
        }
        if (updateDb) {
            this.flush(this, false);
        }
    }

    public boolean isEffectDisabled(int effId) {
        return this.disabledEffects != null && this.disabledEffects.contains(effId);
    }

    public void setEffectDisabled(int effId, boolean disabled) {
        if (this.disabledEffects == null) this.disabledEffects = new java.util.HashSet<>();
        if (disabled) {
            this.disabledEffects.add(effId);
        } else {
            this.disabledEffects.remove(effId);
        }
    }
    public int nvlMax;
    public java.util.Set<Integer> completedRepeatQuestMaps = new java.util.HashSet<>();
    /** Biến tạm dùng khi load DB: max_bag được lưu trong point_inven */
    private transient int _loadedMaxBag = 0;
    /** Biến tạm dùng khi load DB: max_box được lưu trong point_inven */
    private transient int _loadedMaxBox = 0;

    public boolean hasCompletedRepeatQuestOfMap(int mapId) {
        return this.completedRepeatQuestMaps != null && this.completedRepeatQuestMaps.contains(mapId);
    }
    public int ttvtMax;
    public int ltMax;
    public int namiMax;
    public int mr3Max;
    public int aidonMax;
    public long timeEff2;
    public byte[] itemVongQuayOcSen;
    public int numQuayWC = 0;
    public List<EventData> eventData = new ArrayList<>();
    public int typeShop;
    public model.InputDialog inputDialog;
    public model.YesNoDialog yesNoDialog;
    public short id_map_ship_start = -1;
    public int num_van_buon_today = 0;

    public void setinputDialog(model.InputDialog dialog) {
        this.inputDialog = dialog;
    }

    public void setyesNoDialog(model.YesNoDialog dialog) {
        this.yesNoDialog = dialog;
    }

    public void sendAddChatYellow(String msg) {
        if (getService() != null) {
            getService().send_box_ThongBao_OK(msg);
        }
    }

    public void sendYesNo(String title, String text, String[] commands, byte[] icons, model.YesNoDialog.YesNoDialogHandler handler) {
        this.setyesNoDialog(new model.YesNoDialog(this, 100, title, text, commands, icons, handler));
        this.getService().startYesNo();
    }

    public void sendYesNo(String title, String text, String[] commands, model.YesNoDialog.YesNoDialogHandler handler) {
        this.sendYesNo(title, text, commands, model.YesNoDialog.generateDefaultIcons(commands), handler);
    }

    public void sendYesNo(String title, String text, model.YesNoDialog.YesNoDialogHandler handler) {
        this.sendYesNo(title, text, new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}, handler);
    }

    public void sendYesNo(String title, String text, model.YesNoDialog.SimpleConfirmHandler onConfirm) {
        this.sendYesNo(title, text, new String[]{"Đồng ý", "Hủy"}, new byte[]{2, 1}, (ok) -> {
            if (ok == 0 && onConfirm != null) {
                onConfirm.onConfirm();
            }
        });
    }

    public void sendYesNo(String title, String text, String[] commands, model.YesNoDialog.SimpleConfirmHandler onConfirm) {
        this.sendYesNo(title, text, commands, model.YesNoDialog.generateDefaultIcons(commands), (ok) -> {
            if (ok == 0 && onConfirm != null) {
                onConfirm.onConfirm();
            }
        });
    }

    public void sendInput(String title, String[] options, model.InputDialog.InputDialogHandler handler) {
        this.setinputDialog(new model.InputDialog(this, 100, title, options, handler));
        this.getService().startInput();
    }

    public long timeEnterMap;
    public ArchiDaily[] archiDaily;
    public achievement.ArchiPrivatePass[] archiPrivatePass;
    public String currentPassSeason = achievement.ArchiPrivatePass.CURRENT_SEASON;
    public String viewingPassSeason = achievement.ArchiPrivatePass.CURRENT_SEASON;
    public byte currentArchiveType = 0;
    public List<GiftBox> lg = new ArrayList<>();
    public long timeCh;
    public int idWeather;
    public int pointEvent1;
    public int pointEvent2;
    public int typeBXH;
    public int viewingEventId;
    public String nameNew;
    public boolean isShopSk;
    public BotTruyNa botTruyNa;
    public DeTu detu;
    public boolean isDe = false;
    public boolean isDeOnl = false;
    public String nameDe;
    public long timeDetuAtk;
    public int typeShopTichNap;
    public int typeShopTichTieu;
    public int typeShopTichNapSuKien;
    public int typeShopTichTieuSuKien;
    public int typeTichLuyCongDon = 4;
    public int useTAQ;
    public int typeVongQuay;
    public AbsVongQuay currentVongQuay;
    public boolean notifiedBagFull872 = false;
    public long nextCheckCard872Time = 0;
    public PlayerAutoSettings autoSettings = new PlayerAutoSettings();
//    public boolean canSave = true;

    public void checkAndAddSystemCard872() {
        if (this.isBot || this instanceof DeTu || this.conn == null) return;
        if (this.item == null || this.item.bag47 == null) return;

        // Nếu người chơi đã sở hữu Item 872 trong hành trang thì bỏ qua
        if (this.item.total_item_bag_by_id(4, 872) > 0) {
            return;
        }

        // Kiểm tra xem hành trang còn ô trống không
        if (this.item.able_bag() > 0) {
            boolean success = this.item.add_item_bag47(4, 872, 1);
            if (!success) {
                template.ItemBag47 card872 = new template.ItemBag47();
                card872.category = 4;
                card872.id = 872;
                card872.quant = 1;
                this.item.bag47.add(card872);
                success = true;
            }
            if (success) {
                try {
                    this.item.update_Inventory_bag(4, false);
                    if (this.getService() != null) {
                        this.getService().send_box_ThongBao_OK("Bạn đã nhận được Thẻ Hệ Thống (Item 872) vào hành trang!");
                    }
                } catch (Exception ignored) {}
                this.notifiedBagFull872 = false;
            }
        } else {
            // Hành trang đầy -> Thông báo 1 lần để người chơi dọn dẹp
            if (!this.notifiedBagFull872) {
                this.notifiedBagFull872 = true;
                try {
                    if (this.getService() != null) {
                        this.getService().send_box_ThongBao_OK("Hành trang của bạn đã đầy! Vui lòng dọn dẹp ô trống để nhận Thẻ Hệ Thống (Item 872).");
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    public Player() {
        this.service = new network.Service(this);
    }

    public Player(Session conn, String name) {
        this.conn = conn;
        this.name = name;
        this.service = new network.Service(this);
    }

    // ========================= LOAD / SAVE HELPERS =========================

    /**
     * Đọc một JSONObject từ string, hỗ trợ cả format JSONObject mới và JSONArray cũ.
     * Nếu string bắt đầu bằng '{' thì parse JSONObject, ngược lại trả null.
     */
    protected static JSONObject parseObj(String s) {
        if (s == null || s.isBlank()) return null;
        s = s.trim();
        if (!s.startsWith("{")) return null;
        try { return (JSONObject) JSONValue.parse(s); } catch (Exception e) { return null; }
    }

    /** Đọc JSONArray từ string (format cũ). */
    protected static JSONArray parseArr(String s) {
        if (s == null || s.isBlank()) return new JSONArray();
        try {
            Object o = JSONValue.parse(s);
            if (o instanceof JSONArray) return (JSONArray) o;
        } catch (Exception ignored) {}
        return new JSONArray();
    }

    /** Trả về long từ JSONObject key, fallback về dự phòng nếu không tìm thấy. */
    protected static long jLong(JSONObject o, String key, long def) {
        try { Object v = o.get(key); return v == null ? def : Long.parseLong(v.toString()); } catch (Exception e) { return def; }
    }
    protected static int jInt(JSONObject o, String key, int def) {
        try { Object v = o.get(key); return v == null ? def : Integer.parseInt(v.toString()); } catch (Exception e) { return def; }
    }
    protected static short jShort(JSONObject o, String key, short def) {
        try { Object v = o.get(key); return v == null ? def : Short.parseShort(v.toString()); } catch (Exception e) { return def; }
    }
    protected static byte jByte(JSONObject o, String key, byte def) {
        try { Object v = o.get(key); return v == null ? def : Byte.parseByte(v.toString()); } catch (Exception e) { return def; }
    }
    public static boolean jBool(JSONObject o, String key, boolean def) {
        try {
            Object v = o.get(key);
            if (v == null) return def;
            if (v instanceof Boolean) return (Boolean) v;
            String s = v.toString().trim();
            if (s.equalsIgnoreCase("true") || s.equals("1")) return true;
            if (s.equalsIgnoreCase("false") || s.equals("0")) return false;
            return Integer.parseInt(s) == 1;
        } catch (Exception e) {
            return def;
        }
    }
    protected static String jStr(JSONObject o, String key, String def) {
        try { Object v = o.get(key); return v == null ? def : v.toString(); } catch (Exception e) { return def; }
    }
    protected static JSONArray jArr(JSONObject o, String key) {
        try { Object v = o.get(key); return (v instanceof JSONArray) ? (JSONArray) v : new JSONArray(); } catch (Exception e) { return new JSONArray(); }
    }

    // ========================= SETUP (LOAD) =========================

    /**
     * Load toàn bộ dữ liệu player từ DB.
     * Hỗ trợ cả JSON format mới (JSONObject named-key) lẫn format cũ (JSONArray positional).
     * Dữ liệu cũ sẽ tự migration sang format mới khi save lại (flush).
     */
    public boolean setup() {
        return setup("players");
    }

    public boolean setup(String tableName) {
        String cacheKey = tableName + "_" + this.name;
        Map<String, Object> cachedData = database.CacheManager.gI().get(cacheKey);

        try (Connection connection = DbManager.gI().getConnect()) {
            if (cachedData != null) {
                try {
                    boolean success = setupWithRow(new database.DbResultRow(cachedData), connection);
                    if (success) {
                        try {
                            if (conn != null && conn.user != null) {
                                Long moneyVnd = SessionManager.CHECK_BUG.remove(conn.user + "_" + this.name);
                                if (moneyVnd != null && moneyVnd != this.get_vnd()) {
                                    zLog.gI().add_log("BUG_VND", conn.user + " " + name);
                                    conn.LockAccount();
                                    return false;
                                }
                            }
                        } catch (Exception ignored) {}
                        return true;
                    }
                } catch (Exception e) {
                    System.err.println("Error loading player from cache: " + e.getMessage() + ", falling back to DB...");
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM `" + tableName + "` WHERE `name` = ? LIMIT 1")) {
                ps.setString(1, this.name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return false;

                    // Convert ResultSet to Map
                    Map<String, Object> rowMap = new java.util.LinkedHashMap<>();
                    java.sql.ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        rowMap.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    
                    // Save to cache
                    database.CacheManager.gI().put(cacheKey, rowMap);

                    boolean success = setupWithRow(new database.DbResultRow(rowMap), connection);
                    if (success) {
                        try {
                            if (conn != null && conn.user != null) {
                                Long moneyVnd = SessionManager.CHECK_BUG.remove(conn.user + "_" + this.name);
                                if (moneyVnd != null && moneyVnd != this.get_vnd()) {
                                    zLog.gI().add_log("BUG_VND", conn.user + " " + name);
                                    conn.LockAccount();
                                    return false;
                                }
                            }
                        } catch (Exception ignored) {}
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Zone getSafeMap(int preferredMapId, int zoneId) {
        int[] candidateIds = new int[] { preferredMapId, 1, 0 };
        for (int mid : candidateIds) {
            if (mid < 0) continue;
            try {
                Zone[] zones = Zone.getMapByID(mid);
                if (zones != null && zones.length > 0) {
                    int zg = (zoneId >= 0 && zoneId < zones.length) ? zoneId : 0;
                    if (zg < zones.length && zones[zg] != null) {
                        return zones[zg];
                    }
                    for (Zone z : zones) {
                        if (z != null) return z;
                    }
                }
            } catch (Exception ignored) {}
        }
        try {
            for (map.Map m : MapManager.getInstance().getMaps()) {
                if (m != null && m.zones != null && m.zones.length > 0 && m.zones[0] != null) {
                    return m.zones[0];
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    private boolean setupWithRow(database.DbResultRow rs, Connection connection) throws Exception {

                eventData = new ArrayList<>();
                IDPlayer   = rs.getInt("id");
                index_map  = (short) IDPlayer;
                clazz      = rs.getByte("clazz");

                // --- Plain INT columns ---
                String inventoryRaw = rs.getString("inventory");
                JSONObject inventoryObj = parseObj(inventoryRaw);
                if (inventoryObj == null) inventoryObj = new JSONObject();

                pointEvent1     = inventoryObj.containsKey("point_event1") ? jInt(inventoryObj, "point_event1", 0) : rs.getInt("point_event1");
                pointEvent2     = inventoryObj.containsKey("point_event2") ? jInt(inventoryObj, "point_event2", 0) : rs.getInt("point_event2");
                pvppoint        = jInt(inventoryObj, "pvppoint", 0);
                diemdanh        = jInt(inventoryObj, "diemdanh", 0);
                diemdanh_ngay   = jInt(inventoryObj, "diemdanh_ngay", 0);
                wanted_point    = jInt(inventoryObj, "wanted_point", 0);
                ruby            = inventoryObj.containsKey("ruby") ? jInt(inventoryObj, "ruby", 0) : rs.getInt("ruby0");
                point_tich_tieu = inventoryObj.containsKey("point_tich_tieu") ? jInt(inventoryObj, "point_tich_tieu", 0) : rs.getInt("point_tich_tieu");
                level_so_tay    = inventoryObj.containsKey("level_so_tay") ? jInt(inventoryObj, "level_so_tay", 0) : rs.getInt("level_so_tay");
                exp_so_tay      = inventoryObj.containsKey("exp_so_tay") ? jInt(inventoryObj, "exp_so_tay", 0) : rs.getInt("exp_so_tay");
                point_hang_dong = inventoryObj.containsKey("point_hang_dong") ? jInt(inventoryObj, "point_hang_dong", 0) : rs.getInt("point_hang_dong");
                hd_max          = inventoryObj.containsKey("hd_max") ? jInt(inventoryObj, "hd_max", 3) : rs.getInt("hd_max");
                so_tay_vip      = inventoryObj.containsKey("so_tay_vip") ? jInt(inventoryObj, "so_tay_vip", 0) : rs.getInt("so_tay_vip");
                active_so_tay   = inventoryObj.containsKey("active_so_tay") ? jInt(inventoryObj, "active_so_tay", 0) : rs.getInt("active_so_tay");
                coin            = inventoryObj.containsKey("coin") ? jInt(inventoryObj, "coin", 0) : rs.getInt("coin");
                tongnap         = inventoryObj.containsKey("tongnap") ? jInt(inventoryObj, "tongnap", 0) : (conn != null ? conn.tongnap : 0);
                vip             = inventoryObj.containsKey("vip") ? jInt(inventoryObj, "vip", 0) : (conn != null ? conn.vip : 0);
                huongnghiep     = inventoryObj.containsKey("huongnghiep") ? jInt(inventoryObj, "huongnghiep", 0) : rs.getInt("huongnghiep");

                // --- Load admin role status ---
                if (conn != null && (conn.role == 1 || conn.is_admin == 1 || "admin".equalsIgnoreCase(conn.user) || "ad".equalsIgnoreCase(conn.user))) {
                    this.admin = 1;
                } else if ("admin".equalsIgnoreCase(this.name) || "ad".equalsIgnoreCase(this.name)) {
                    this.admin = 1;
                } else {
                    int accId = 0;
                    try { accId = rs.getInt("account_id"); } catch (Exception ignored) {}
                    if (accId <= 0 && conn != null) accId = conn.idUser;
                    if (accId > 0 && connection != null) {
                        try (PreparedStatement psAcc = connection.prepareStatement("SELECT `role`, `is_admin` FROM `account` WHERE `id` = ? LIMIT 1")) {
                            psAcc.setInt(1, accId);
                            try (ResultSet rsAcc = psAcc.executeQuery()) {
                                if (rsAcc.next()) {
                                    int r = rsAcc.getInt("role");
                                    int a = rsAcc.getInt("is_admin");
                                    if (r == 1 || a == 1) {
                                        this.admin = 1;
                                        if (conn != null) {
                                            conn.role = r;
                                            conn.is_admin = a;
                                        }
                                    }
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                }
                
                int tempDiemDanhEvent = 0;
                try { tempDiemDanhEvent = rs.getInt("diem_danh_event"); } catch (Exception ignored) {}
                diem_danh_event = inventoryObj.containsKey("diem_danh_event") ? jInt(inventoryObj, "diem_danh_event", 0) : tempDiemDanhEvent;

                if (inventoryObj.containsKey("time_online")) {
                    timeOnline = jLong(inventoryObj, "time_online", 0L);
                } else if (inventoryObj.containsKey("point_inven")) {
                    Object pinvObj = inventoryObj.get("point_inven");
                    if (pinvObj instanceof JSONObject) {
                        JSONObject pinv = (JSONObject) pinvObj;
                        if (pinv.containsKey("time_online")) {
                            timeOnline = jLong(pinv, "time_online", 0L);
                        }
                    } else if (pinvObj instanceof String) {
                        JSONObject pinv = parseObj((String) pinvObj);
                        if (pinv != null && pinv.containsKey("time_online")) {
                            timeOnline = jLong(pinv, "time_online", 0L);
                        }
                    }
                }
                loginTime = System.currentTimeMillis();

                if (inventoryObj.containsKey("auto_settings")) {
                    Object autoObj = inventoryObj.get("auto_settings");
                    if (autoObj instanceof JSONObject) {
                        autoSettings.fromJsonObject((JSONObject) autoObj);
                    }
                }

                String dateStr = null;
                try { dateStr = rs.getString("date"); } catch (Exception ignored) {}
                if (dateStr != null && !dateStr.isBlank()) {
                    date = DateTime.parse(dateStr);
                } else {
                    date = new DateTime();
                }

                // ---- level ----
                // Mới: {"lv":100,"exp":999,"tt":5}
                // Cũ: [100, 999, 5]
                {
                    String raw = rs.getString("level");
                    JSONObject o = parseObj(raw);
                    if (o != null) {
                        level     = jShort(o, "lv",  (short) 1);
                        exp       = jLong(o,  "exp", 0L);
                        thongthao = jShort(o, "tt",  (short) 0);
                    } else {
                        JSONArray a = parseArr(raw);
                        level     = a.size() > 0 ? Short.parseShort(a.get(0).toString()) : 1;
                        exp       = a.size() > 1 ? Long.parseLong(a.get(1).toString()) : 0L;
                        thongthao = a.size() > 2 ? Short.parseShort(a.get(2).toString()) : 0;
                    }
                }

                // ---- body (head/hair) — FIX: là cột JSON dược load và save ----
                // Mới: {"head":1,"hair":2}
                // Cũ: [1, 2]
                {
                    String raw = rs.getString("body_parts");
                    JSONObject o = parseObj(raw);
                    if (o != null) {
                        head = jShort(o, "head", (short) 0);
                        hair = jShort(o, "hair", (short) 0);
                        part_body = jShort(o, "body", (short) -1);
                        part_leg = jShort(o, "leg", (short) -1);
                        part_ring = jShort(o, "ring", (short) -1);
                        part_weapon = jShort(o, "weapon", (short) -1);
                    } else {
                        JSONArray a = parseArr(raw);
                        if (a != null && a.size() > 1) {
                            head = a.size() > 0 ? Short.parseShort(a.get(0).toString()) : 0;
                            hair = a.size() > 1 ? Short.parseShort(a.get(1).toString()) : 0;
                            part_body = a.size() > 2 ? Short.parseShort(a.get(2).toString()) : -1;
                            part_leg = a.size() > 3 ? Short.parseShort(a.get(3).toString()) : -1;
                            part_ring = a.size() > 4 ? Short.parseShort(a.get(4).toString()) : -1;
                            part_weapon = a.size() > 5 ? Short.parseShort(a.get(5).toString()) : -1;
                        } else {
                            String oldBody = null;
                            try {
                                oldBody = rs.getString("body");
                            } catch (Exception e) {}
                            if (oldBody != null) {
                                JSONArray oldA = parseArr(oldBody);
                                head = oldA.size() > 0 ? Short.parseShort(oldA.get(0).toString()) : 0;
                                hair = oldA.size() > 1 ? Short.parseShort(oldA.get(1).toString()) : 0;
                                part_body = oldA.size() > 2 ? Short.parseShort(oldA.get(2).toString()) : -1;
                                part_leg = oldA.size() > 3 ? Short.parseShort(oldA.get(3).toString()) : -1;
                                part_ring = oldA.size() > 4 ? Short.parseShort(oldA.get(4).toString()) : -1;
                                part_weapon = oldA.size() > 5 ? Short.parseShort(oldA.get(5).toString()) : -1;
                            } else {
                                head = 0; hair = 0; part_body = -1; part_leg = -1; part_ring = -1; part_weapon = -1;
                            }
                        }
                    }
                    // Sanitize head & hair values
                    if (head >= 1 && head <= 4) {
                        head = 0;
                    }
                    if (head < 0) {
                        head = 0;
                    }
                    if (hair <= 0) {
                        switch (clazz) {
                            case 1: hair = 1; break;
                            case 2: hair = 24; break;
                            case 3: hair = 28; break;
                            case 4: hair = 32; break;
                            case 5: hair = 36; break;
                            default: hair = 1; break;
                        }
                    }
                }

                // ---- potential ----
                // Mới: {"pts":0,"p1":0,"p2":0,"p3":0,"p4":0,"p5":0,"ttPts":0,"ttOps":[[id,v],...]}
                // Cũ: [pts, p1, p2, p3, p4, p5, ttPts, [[id,v],...]]
                {
                    String raw = rs.getString("potential");
                    JSONObject o = parseObj(raw);
                    list_op_thongthao = new ArrayList<>();
                    if (o != null) {
                        pointAttribute         = jLong(o, "pts",   0L);
                        point1                 = jLong(o, "p1",    0L);
                        point2                 = jLong(o, "p2",    0L);
                        point3                 = jLong(o, "p3",    0L);
                        point4                 = jLong(o, "p4",    0L);
                        point5                 = jLong(o, "p5",    0L);
                        pointAttributeThongThao= jLong(o, "ttPts", 0L);
                        pointSkill             = (int) jLong(o, "skPts", 0L);
                        JSONArray ops = jArr(o, "ttOps");
                        for (Object op : ops) {
                            JSONArray pair = (JSONArray) op;
                            list_op_thongthao.add(new Option(
                                Byte.parseByte(pair.get(0).toString()),
                                Integer.parseInt(pair.get(1).toString())));
                        }
                    } else {
                        JSONArray a = parseArr(raw);
                        pointAttribute          = a.size() > 0 ? Long.parseLong(a.get(0).toString()) : 0L;
                        point1                  = a.size() > 1 ? Long.parseLong(a.get(1).toString()) : 0L;
                        point2                  = a.size() > 2 ? Long.parseLong(a.get(2).toString()) : 0L;
                        point3                  = a.size() > 3 ? Long.parseLong(a.get(3).toString()) : 0L;
                        point4                  = a.size() > 4 ? Long.parseLong(a.get(4).toString()) : 0L;
                        point5                  = a.size() > 5 ? Long.parseLong(a.get(5).toString()) : 0L;
                        pointAttributeThongThao = a.size() > 6 ? Long.parseLong(a.get(6).toString()) : 0L;
                        if (a.size() > 7) {
                            JSONArray ops = (JSONArray) a.get(7);
                            for (Object op : ops) {
                                JSONArray pair = (JSONArray) op;
                                list_op_thongthao.add(new Option(
                                    Byte.parseByte(pair.get(0).toString()),
                                    Integer.parseInt(pair.get(1).toString())));
                            }
                        }
                    }
                }

                // ---- point_inven ----
                // Mới: {"gold":0,"gem":0,"vnd":0,"hammer":0,"accum":0,"pvpW":0,"pvpL":0,"ship":0,"hs":0,"nvl":0,"ttvt":0,"wanted":0,"ruby":0,"titles":[[id,lv],...]}
                // Cũ: positional array

                {
                    String raw = "{}";
                    Object __obj = inventoryObj.get("point_inven");
                    if (__obj != null) raw = __obj instanceof String ? (String)__obj : ((org.json.simple.JSONAware)__obj).toJSONString();
                    JSONObject o = parseObj(raw);
                    if (o != null) {
                        vang         = jLong(o,  "gold",   0L);
                        kimcuong     = jInt(o,   "gem",    0);
                        vnd          = jLong(o,  "vnd",    0L);
                        bua          = jLong(o,  "hammer", 0L);
                        tichLuy      = jInt(o,   "accum",  0);
                        pvp_win      = jInt(o,   "pvpW",   0);
                        pvp_lose     = jInt(o,   "pvpL",   0);
                        time_ship    = jByte(o,  "ship",   (byte)0);
                        time_can_hs  = jByte(o,  "hs",     (byte)0);
                        time_nvl     = jInt(o,   "nvl",    0);
                        time_ttvt    = jByte(o,  "ttvt",   (byte)0);
                        wanted_point = jInt(o,   "wanted", 0);
                        tieuRuby     = jInt(o,   "ruby",   0);
                        tieuTuan     = jInt(o,   "tieu_tuan", 0);
                        tieuTong     = jInt(o,   "tieu_tong", 0);
                        // max_bag / max_box (mới): mặc định 30 nếu chưa có
                        _loadedMaxBag = jInt(o, "max_bag", 0);
                        _loadedMaxBox = jInt(o, "max_box", 0);
                        JSONArray titles = jArr(o, "titles");
                        if (titles != null) {
                            for (int i = 0; i < titles.size(); i++) {
                                Object itemObj = titles.get(i);
                                JSONArray t = (itemObj instanceof JSONArray) ? (JSONArray) itemObj : parseArr(itemObj.toString());
                                if (t != null && t.size() >= 2) {
                                    int tid = Integer.parseInt(t.get(0).toString());
                                    int tactive = Integer.parseInt(t.get(1).toString());
                                    int exp = (t.size() >= 3) ? Integer.parseInt(t.get(2).toString()) : -1;
                                    id_danh_hieu_da_so_huu.add(new int[]{tid, tactive, exp});
                                    if (tactive == 1) {
                                        if (exp <= 0 || (long)exp * 1000L > System.currentTimeMillis()) {
                                            id_danh_hieu_su_dung = tid;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        JSONArray a = parseArr(raw);
                        vang         = a.size() > 0  ? Long.parseLong(a.get(0).toString()) : 0L;
                        kimcuong     = a.size() > 1  ? Integer.parseInt(a.get(1).toString()) : 0;
                        vnd          = a.size() > 2  ? Long.parseLong(a.get(2).toString()) : 0L;
                        bua          = a.size() > 3  ? Long.parseLong(a.get(3).toString()) : 0L;
                        tichLuy      = a.size() > 4  ? Integer.parseInt(a.get(4).toString()) : 0;
                        pvp_win      = a.size() > 5  ? Integer.parseInt(a.get(5).toString()) : 0;
                        pvp_lose     = a.size() > 6  ? Integer.parseInt(a.get(6).toString()) : 0;
                        time_ship    = a.size() > 7  ? Byte.parseByte(a.get(7).toString()) : 0;
                        time_can_hs  = a.size() > 8  ? Byte.parseByte(a.get(8).toString()) : 0;
                        time_nvl     = a.size() > 9  ? Integer.parseInt(a.get(9).toString()) : 0;
                        time_ttvt    = a.size() > 10 ? Byte.parseByte(a.get(10).toString()) : 0;
                        wanted_point = a.size() > 11 ? Integer.parseInt(a.get(11).toString()) : 0;
                        tieuRuby     = a.size() > 12 ? Integer.parseInt(a.get(12).toString()) : 0;
                        if (a.size() > 13) {
                            JSONArray js1 = (a.get(13) instanceof JSONArray) ? (JSONArray) a.get(13) : parseArr(a.get(13).toString());
                            if (js1 != null) {
                                for (int i = 0; i < js1.size(); ++i) {
                                    Object itemObj = js1.get(i);
                                    JSONArray js1_in = (itemObj instanceof JSONArray) ? (JSONArray) itemObj : parseArr(itemObj.toString());
                                    if (js1_in != null && js1_in.size() >= 2) {
                                        int tid = Integer.parseInt(js1_in.get(0).toString());
                                        int tactive = Integer.parseInt(js1_in.get(1).toString());
                                        int exp = (js1_in.size() >= 3) ? Integer.parseInt(js1_in.get(2).toString()) : -1;
                                        id_danh_hieu_da_so_huu.add(new int[]{tid, tactive, exp});
                                        if (tactive == 1) {
                                            if (exp <= 0 || (long)exp * 1000L > System.currentTimeMillis()) {
                                                id_danh_hieu_su_dung = tid;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ---- site ----
                // Mới: {"map":1,"zone":0,"hp":1000,"mp":500,"x":100,"y":200,"hat":1,"pk":0,"wpn":1,
                //         "tk":10,"tkcd":0,"pvptk":5,"pvpcd":0,"boss":3,"bosscd":0,
                //         "aidon":0,"nvl":50,"ttvt":20,"lt":25,"nami":5,"aidonM":20,"mr3":5}
                // Cũ: positional array[22]
                {
                    String raw = "{}";
                    Object __objSite = inventoryObj.get("site");
                    if (__objSite != null) raw = __objSite instanceof String ? (String)__objSite : ((org.json.simple.JSONAware)__objSite).toJSONString();
                    JSONObject o = parseObj(raw);
                    if (o != null) {
                        int mapId = jInt(o, "map", 1);
                        int zoneId = jInt(o, "zone", 0);
                        hp           = jInt(o, "hp", -1);
                        mp           = jInt(o, "mp", -1);
                        x            = jShort(o, "x", (short)0);
                        y            = jShort(o, "y", (short)0);
                        is_show_hat  = jBool(o, "hat", true);
                        pointPk      = jInt(o, "pk", 0);
                        is_show_weapon = jBool(o, "wpn", true);
                        is_hide_fashion_hair = jBool(o, "hfhair", false);
                        is_hide_fashion_head = jBool(o, "hfhead", false);
                        ticket           = jShort(o, "tk",     (short)0);
                        cd_ticket_next   = jLong(o,  "tkcd",   0L);
                        pvp_ticket       = jShort(o, "pvptk",  (short)0);
                        cd_pvp_next      = jLong(o,  "pvpcd",  0L);
                        key_boss         = jShort(o, "boss",   (short)0);
                        cd_keyboss_next  = jLong(o,  "bosscd", 0L);
                        aiDonLevel = jInt(o, "aidon",  0);
                        nvlMax     = jInt(o, "nvl",    50);
                        ttvtMax    = jInt(o, "ttvt",   20);
                        ltMax      = jInt(o, "lt",     25);
                        namiMax    = jInt(o, "nami",   5);
                        aidonMax   = jInt(o, "aidonM", 20);
                        mr3Max     = jInt(o, "mr3",    5);
                        pre_map_id = jInt(o, "pre_map", -1);
                        pre_zone_id = (byte) jInt(o, "pre_zone", -1);
                        pre_x      = jShort(o, "pre_x", (short) -1);
                        pre_y      = jShort(o, "pre_y", (short) -1);
                        try {
                            completedRepeatQuestMaps = new java.util.HashSet<>();
                            Object rqObj = o.get("rq_maps");
                            if (rqObj instanceof JSONArray) {
                                JSONArray arr = (JSONArray) rqObj;
                                for (Object item : arr) {
                                    if (item != null) completedRepeatQuestMaps.add(Integer.parseInt(item.toString()));
                                }
                            }
                        } catch (Exception ignored) {}
                        try {
                            Zone[] maps = Zone.getMapByID(mapId);
                            if (maps != null && maps.length > 0) {
                                int zg = (zoneId >= 0 && zoneId < maps.length) ? zoneId : 0;
                                if (zg != 0) {
                                    while (zg < (maps[zg].template.max_zone - 1)
                                            && maps[zg].getNumPlayerSlot() >= maps[zg].template.max_player) zg++;
                                }
                                this.map = maps[zg];
                            } else {
                                this.map = getSafeMap(this.id_map_save > 0 ? this.id_map_save : 1, 0);
                            }
                            // Safety: nếu map load về là map Săn Trùm (id=59) hoặc dynamic Săn Trùm đang chạy
                            // thì redirect về id_map_save để tránh bị kẹt
                            if (this.map != null && (this.map.template.id == 59 || this.map.mapSanTrum != null)) {
                                this.map = getSafeMap(this.id_map_save > 0 ? this.id_map_save : 1, 0);
                            }
                        } catch (Exception e) {
                            this.map = getSafeMap(this.id_map_save > 0 ? this.id_map_save : 1, 0);
                        }
                    } else {
                        JSONArray a = parseArr(raw);
                        try {
                            int mid = a.size() > 0 ? Integer.parseInt(a.get(0).toString()) : 1;
                            byte zoneId = a.size() > 1 ? Byte.parseByte(a.get(1).toString()) : 0;
                            Zone[] maps = Zone.getMapByID(mid);
                            if (maps != null && maps.length > 0) {
                                int zg = (zoneId >= 0 && zoneId < maps.length) ? zoneId : 0;
                                if (zg != 0) {
                                    while (zg < (maps[zg].template.max_zone - 1)
                                            && maps[zg].getNumPlayerSlot() >= maps[zg].template.max_player) zg++;
                                }
                                this.map = maps[zg];
                            } else {
                                this.map = getSafeMap(1, 0);
                            }
                        } catch (Exception e) {
                            this.map = getSafeMap(1, 0);
                        }
                        hp              = a.size() > 2  ? Integer.parseInt(a.get(2).toString()) : -1;
                        mp              = a.size() > 3  ? Integer.parseInt(a.get(3).toString()) : -1;
                        x               = a.size() > 4  ? Short.parseShort(a.get(4).toString()) : 0;
                        y               = a.size() > 5  ? Short.parseShort(a.get(5).toString()) : 0;
                        is_show_hat     = a.size() > 6  && Byte.parseByte(a.get(6).toString()) == 1;
                        pointPk         = a.size() > 7  ? Integer.parseInt(a.get(7).toString()) : 0;
                        ticket          = a.size() > 8  ? Short.parseShort(a.get(8).toString()) : 0;
                        cd_ticket_next  = a.size() > 9  ? Long.parseLong(a.get(9).toString()) : 0L;
                        pvp_ticket      = a.size() > 10 ? Short.parseShort(a.get(10).toString()) : 0;
                        cd_pvp_next     = a.size() > 11 ? Long.parseLong(a.get(11).toString()) : 0L;
                        key_boss        = a.size() > 12 ? Short.parseShort(a.get(12).toString()) : 0;
                        cd_keyboss_next = a.size() > 13 ? Long.parseLong(a.get(13).toString()) : 0L;
                        is_show_weapon  = a.size() <= 14 || Byte.parseByte(a.get(14).toString()) == 1;
                        aiDonLevel      = a.size() > 15 ? Byte.parseByte(a.get(15).toString()) : 0;
                        nvlMax          = a.size() > 16 ? Byte.parseByte(a.get(16).toString()) : 50;
                        ttvtMax         = a.size() > 17 ? Byte.parseByte(a.get(17).toString()) : 20;
                        ltMax           = a.size() > 18 ? Byte.parseByte(a.get(18).toString()) : 25;
                        namiMax         = a.size() > 19 ? Byte.parseByte(a.get(19).toString()) : 5;
                        aidonMax        = a.size() > 20 ? Byte.parseByte(a.get(20).toString()) : 20;
                        mr3Max          = a.size() > 21 ? Byte.parseByte(a.get(21).toString()) : 5;
                    }
                    if (this.map == null) {
                        this.map = getSafeMap(1, 0);
                    }
                    // Validate x/y bounds
                    if (this.map != null && this.map.template != null) {
                        if (x < 0 || x > this.map.template.maxW || y < 0 || y > this.map.template.maxH) {
                            x = (short)(this.map.template.maxW / 2);
                            y = (short)(this.map.template.maxH / 2);
                        }
                    }
                    // Ticket cooldown recovery
                    if (cd_ticket_next == 0 || ticket >= get_ticket_max())
                        cd_ticket_next = System.currentTimeMillis() + 600_000L;
                    while (ticket < get_ticket_max() && cd_ticket_next < System.currentTimeMillis()) {
                        ticket++; cd_ticket_next += 600_000L;
                    }
                    if (cd_pvp_next == 0 || pvp_ticket >= get_pvp_ticket_max())
                        cd_pvp_next = System.currentTimeMillis() + 7_200_000L;
                    while (pvp_ticket < get_pvp_ticket_max() && cd_pvp_next < System.currentTimeMillis()) {
                        pvp_ticket++; cd_pvp_next += 7_200_000L;
                    }
                    if (cd_keyboss_next == 0 || key_boss >= get_key_boss_max())
                        cd_keyboss_next = System.currentTimeMillis() + 3_600_000L;
                    while (key_boss < get_key_boss_max() && cd_keyboss_next < System.currentTimeMillis()) {
                        key_boss++; cd_keyboss_next += 3_600_000L;
                    }
                }

                // ---- mbv ----
                // Mới: {"pw":"","exp":-1}
                // Cũ: ["", -1]
                {
                    try {
                        String raw = rs.getString("mbv");
                        JSONObject o = parseObj(raw);
                        if (o != null) {
                            mbv        = o.containsKey("mabaove") ? jStr(o, "mabaove", "") : jStr(o, "pw", "");
                            timeHuyMbv = o.containsKey("timeHuyMaBaoVe") ? jLong(o, "timeHuyMaBaoVe", -1L) : jLong(o, "exp", -1L);
                        } else {
                            JSONArray a = parseArr(raw);
                            mbv        = a.size() > 0 ? a.get(0).toString() : "";
                            timeHuyMbv = a.size() > 1 ? Long.parseLong(a.get(1).toString()) : -1L;
                        }
                    } catch (Exception ex) { mbv = ""; timeHuyMbv = -1L; }
                    passBagOK = mbv.isEmpty();
                }

                // ---- tichluycheck ----
                try {
                    JSONArray a = inventoryObj.containsKey("tich_luy")
                        ? jArr(inventoryObj, "tich_luy")
                        : parseArr(rs.getString("tichluycheck"));
                    tichTieuCheck = new byte[Math.max(a.size(), ListTichNap.ENTRY.size())];
                    for (int i = 0; i < a.size(); i++) tichTieuCheck[i] = Byte.parseByte(a.get(i).toString());
                } catch (Exception ex) { tichTieuCheck = new byte[ListTichNap.ENTRY.size()]; }

                // ---- tieu_ruby_check ----
                try {
                    JSONArray a = inventoryObj.containsKey("tieu_ruby")
                        ? jArr(inventoryObj, "tieu_ruby")
                        : parseArr(rs.getString("tieu_ruby_check"));
                    int sz = Math.max(a.size(), TichTieuRuby.ENTRY.size());
                    tieuRubyCheck = new byte[sz];
                    for (int i = 0; i < a.size() && i < sz; i++) tieuRubyCheck[i] = Byte.parseByte(a.get(i).toString());
                } catch (Exception ex) { tieuRubyCheck = new byte[TichTieuRuby.ENTRY.size()]; }

                // ---- tieu_hangngay_check ----
                try {
                    JSONArray a = inventoryObj.containsKey("tieu_hangngay")
                        ? jArr(inventoryObj, "tieu_hangngay")
                        : parseArr(rs.getString("tieu_hangngay_check"));
                    tichHangNgayCheck = new byte[Math.max(a.size(), ListNapHangNgay.ENTRY.size())];
                    for (int i = 0; i < a.size(); i++) tichHangNgayCheck[i] = Byte.parseByte(a.get(i).toString());
                } catch (Exception ex) { tichHangNgayCheck = new byte[ListNapHangNgay.ENTRY.size()]; }

                // ---- tieu_tuan_check ----
                try {
                    JSONArray a = inventoryObj.containsKey("tieu_tuan_check") ? jArr(inventoryObj, "tieu_tuan_check") : null;
                    if (a != null) {
                        int sz = Math.max(a.size(), TichTieuTuan.ENTRY.size());
                        tieuTuanCheck = new byte[sz];
                        for (int i = 0; i < a.size() && i < sz; i++) tieuTuanCheck[i] = Byte.parseByte(a.get(i).toString());
                    } else {
                        tieuTuanCheck = new byte[TichTieuTuan.ENTRY.size()];
                    }
                } catch (Exception ex) { tieuTuanCheck = new byte[TichTieuTuan.ENTRY.size()]; }

                // ---- tieu_tong_check ----
                try {
                    JSONArray a = inventoryObj.containsKey("tieu_tong_check") ? jArr(inventoryObj, "tieu_tong_check") : null;
                    if (a != null) {
                        int sz = Math.max(a.size(), TichTieuTong.ENTRY.size());
                        tieuTongCheck = new byte[sz];
                        for (int i = 0; i < a.size() && i < sz; i++) tieuTongCheck[i] = Byte.parseByte(a.get(i).toString());
                    } else {
                        tieuTongCheck = new byte[TichTieuTong.ENTRY.size()];
                    }
                } catch (Exception ex) { tieuTongCheck = new byte[TichTieuTong.ENTRY.size()]; }

                // ---- nap_tuan_check ----
                try {
                    JSONArray a = inventoryObj.containsKey("nap_tuan_check") ? jArr(inventoryObj, "nap_tuan_check") : null;
                    if (a != null) {
                        int sz = Math.max(a.size(), ListTichNapTuan.ENTRY.size());
                        tichNapTuanCheck = new byte[sz];
                        for (int i = 0; i < a.size() && i < sz; i++) tichNapTuanCheck[i] = Byte.parseByte(a.get(i).toString());
                    } else {
                        tichNapTuanCheck = new byte[ListTichNapTuan.ENTRY.size()];
                    }
                } catch (Exception ex) { tichNapTuanCheck = new byte[ListTichNapTuan.ENTRY.size()]; }

                // ---- timeResetHangNgay ----
                {
                    java.sql.Timestamp ts = null;
                    try {
                        ts = rs.getTimestamp("timeResetHangNgay");
                    } catch (Exception ignored) {}
                    if (ts != null) {
                        timeresetHangNgay = ts.toLocalDateTime();
                    } else {
                        String s = rs.getString("timeResetHangNgay");
                        if (s != null && !s.trim().isEmpty() && !"0000-00-00 00:00:00".equals(s.trim())) {
                            s = s.trim();
                            if (s.contains(".")) {
                                s = s.substring(0, s.indexOf('.'));
                            }
                            try {
                                timeresetHangNgay = java.time.LocalDateTime.parse(s,
                                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            } catch (Exception ignored) {
                                timeresetHangNgay = java.time.LocalDateTime.of(java.time.LocalDate.now().minusDays(1), java.time.LocalTime.MIDNIGHT);
                            }
                        } else {
                            timeresetHangNgay = java.time.LocalDateTime.of(java.time.LocalDate.now().minusDays(1), java.time.LocalTime.MIDNIGHT);
                        }
                    }
                }

                eventData = new ArrayList<>();
                try (PreparedStatement psEv = connection.prepareStatement(
                    "SELECT `id`, `type`, `data`, `create_at` FROM `historys` WHERE `player_id` = ? AND (`type` = 'EVENT_DATA' OR `type` LIKE 'EVENT_DATA_%')"
                )) {
                    psEv.setInt(1, IDPlayer);
                    try (ResultSet rsEv = psEv.executeQuery()) {
                        while (rsEv.next()) {
                            int evRowId = rsEv.getInt("id");
                            String typeCol = rsEv.getString("type");
                            String rawEventData = rsEv.getString("data");
                            java.sql.Timestamp cAt = rsEv.getTimestamp("create_at");

                            if (!historys.HistoryManager.validatePlayerRecord(this, rawEventData, cAt)) {
                                try (PreparedStatement psDelStale = connection.prepareStatement("DELETE FROM `historys` WHERE `id` = ?")) {
                                    psDelStale.setInt(1, evRowId);
                                    psDelStale.executeUpdate();
                                } catch (Exception ignored) {}
                                continue;
                            }

                            String unwrapped = historys.HistoryManager.unwrapData(rawEventData);
                            if (unwrapped != null && !unwrapped.isBlank()) {
                                try {
                                    JSONArray arr = parseArr(unwrapped);
                                    if (arr == null) {
                                        JSONObject obj = parseObj(unwrapped);
                                        if (obj != null) {
                                            arr = new JSONArray();
                                            arr.add(obj);
                                        }
                                    }
                                    if (arr != null) {
                                        for (int i = 0; i < arr.size(); i++) {
                                            EventData ed = new EventData();
                                            Object elem = arr.get(i);
                                            if (elem instanceof JSONObject) {
                                                JSONObject eo = (JSONObject) elem;
                                                ed.eventID = jInt(eo, "ev", 0);
                                                ed.key = jStr(eo, "k", null);
                                                if (ed.key == null && typeCol != null && typeCol.startsWith("EVENT_DATA_")) {
                                                    ed.key = typeCol.substring("EVENT_DATA_".length());
                                                }
                                                
                                                JSONArray da = jArr(eo, "d");
                                                if (da != null) {
                                                    ed.data = new int[da.size()];
                                                    for (int j = 0; j < da.size(); j++) ed.data[j] = Integer.parseInt(da.get(j).toString());
                                                } else {
                                                    ed.data = new int[35];
                                                }
                                                
                                                if (eo.containsKey("pt")) {
                                                    JSONObject pt = (JSONObject) eo.get("pt");
                                                    for (Object key : pt.keySet()) {
                                                        ed.setPoint(key.toString(), Integer.parseInt(pt.get(key).toString()));
                                                    }
                                                }
                                            } else if (elem instanceof JSONArray) {
                                                JSONArray inner = (JSONArray) elem;
                                                ed.eventID = Integer.parseInt(inner.get(0).toString());
                                                JSONArray da = (JSONArray) inner.get(1);
                                                if (da != null) {
                                                    ed.data = new int[da.size()];
                                                    for (int j = 0; j < da.size(); j++) ed.data[j] = Integer.parseInt(da.get(j).toString());
                                                } else {
                                                    ed.data = new int[35];
                                                }
                                            }
                                            
                                            // Phân biệt chính xác theo eventID VÀ season key để không bao giờ trộn lẫn dữ liệu giữa các mùa
                                            EventData existing = null;
                                            for (EventData ex : eventData) {
                                                if (ex.eventID == ed.eventID && java.util.Objects.equals(ex.key, ed.key)) {
                                                    existing = ex;
                                                    break;
                                                }
                                            }
                                            if (existing != null) {
                                                if (ed.data != null) {
                                                    if (existing.data == null || existing.data.length < ed.data.length) {
                                                        existing.data = ed.data;
                                                    } else {
                                                        for (int k = 0; k < ed.data.length; k++) {
                                                            if (ed.data[k] > existing.data[k]) existing.data[k] = ed.data[k];
                                                        }
                                                    }
                                                }
                                                if (ed.points != null) {
                                                    for (java.util.Map.Entry<String, Integer> pEntry : ed.points.entrySet()) {
                                                        if (pEntry.getValue() > existing.getPoint(pEntry.getKey())) {
                                                            existing.setPoint(pEntry.getKey(), pEntry.getValue());
                                                        }
                                                    }
                                                }
                                            } else {
                                                eventData.add(ed);
                                            }
                                        }
                                    }
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                } catch (Exception ignored) {}
                load_data_event();

                // ---- myarchidaily ----
                // Mới: [{"id":1,"n":0,"s":0}, ...]
                // Cũ: [[id, currentNum, state], ...]
                try {
                    JSONArray arr = parseArr(rs.getString("myarchidaily"));
                    this.archiDaily = new ArchiDaily[arr.size()];
                    for (int i = 0; i < arr.size(); i++) {
                        int id; int currentNum; byte state;
                        Object elem = arr.get(i);
                        if (elem instanceof JSONObject) {
                            JSONObject eo = (JSONObject) elem;
                            id = jInt(eo, "id", 0); currentNum = jInt(eo, "n", 0); state = jByte(eo, "s", (byte)0);
                        } else {
                            JSONArray inner = (JSONArray) elem;
                            id = Integer.parseInt(inner.get(0).toString());
                            currentNum = Integer.parseInt(inner.get(1).toString());
                            state = Byte.parseByte(inner.get(2).toString());
                        }
                        ArchiDaily template = ArchiDaily.getTemplate(id);
                        if (template != null) {
                            ArchiDaily item = new ArchiDaily(template);
                            item.currentNum = currentNum; item.state = state;
                            this.archiDaily[i] = item;
                        }
                    }
                } catch (Exception e) { ArchiDaily.ramdomArchiDaily(this); }

                // ---- archi_private_pass (stored in historys table per season) ----
                try {
                    this.archiPrivatePass = achievement.ArchiPrivatePass.loadPassData(this, this.currentPassSeason);
                } catch (Exception e) {
                    achievement.ArchiPrivatePass.initPrivatePass(this);
                }

                // ---- wanted_chest ----
                // Mới: [{"id":1,"time":0,"max":10,"ruby":5}, ...]
                // Cũ: [[id, timeUse, maxTimeUse, Ruby], ...]
                this.wanted_chest = new Wanted_Chest[2];
                try {
                    String rawChest = "[]";
                    Object __objChest = inventoryObj.get("wanted_chest");
                    if (__objChest != null) rawChest = __objChest instanceof String ? (String)__objChest : ((org.json.simple.JSONAware)__objChest).toJSONString();
                    JSONArray arr = parseArr(rawChest);
                    for (int i = 0; i < arr.size() && i < 2; i++) {
                        this.wanted_chest[i] = new Wanted_Chest();
                        Object elem = arr.get(i);
                        if (elem instanceof JSONObject) {
                            JSONObject eo = (JSONObject) elem;
                            this.wanted_chest[i].id         = jShort(eo, "id",   (short)0);
                            this.wanted_chest[i].timeUse    = jLong(eo,  "time", 0L);
                            this.wanted_chest[i].maxTimeUse = jShort(eo, "max",  (short)0);
                            this.wanted_chest[i].Ruby       = jShort(eo, "ruby", (short)0);
                        } else {
                            JSONArray inner = (JSONArray) elem;
                            this.wanted_chest[i].id         = Short.parseShort(inner.get(0).toString());
                            this.wanted_chest[i].timeUse    = Long.parseLong(inner.get(1).toString());
                            this.wanted_chest[i].maxTimeUse = Short.parseShort(inner.get(2).toString());
                            this.wanted_chest[i].Ruby       = Short.parseShort(inner.get(3).toString());
                        }
                    }
                } catch (Exception ignored) {}

                // ---- mypet ----
                // Mới: [{"slot":0,"tid":1,"use":1,"time":0}, ...]
                // Cũ: [[pet_id, template_id, isUse, time], ...]
                my_pet = new ArrayList<>();
                try {
                    JSONArray arr = parseArr(rs.getString("mypet"));
                    for (int i = 0; i < arr.size(); i++) {
                        MyPet tempPet = new MyPet();
                        Object elem = arr.get(i);
                        if (elem instanceof JSONObject) {
                            JSONObject eo = (JSONObject) elem;
                            tempPet.id       = jShort(eo, "slot", (short)0);
                            tempPet.template = Pet.getTemplate(jShort(eo, "tid", (short)0));
                            tempPet.isUse    = jBool(eo, "use", false);
                            tempPet.time     = jLong(eo, "time", 0L);
                        } else {
                            JSONArray inner = (JSONArray) elem;
                            tempPet.id       = Short.parseShort(inner.get(0).toString());
                            tempPet.template = Pet.getTemplate(Short.parseShort(inner.get(1).toString()));
                            tempPet.isUse    = Byte.parseByte(inner.get(2).toString()) == 1;
                            tempPet.time     = Long.parseLong(inner.get(3).toString());
                        }
                        if (tempPet.template != null) my_pet.add(tempPet);
                    }
                } catch (Exception ignored) {}

                // ---- quest ----
                list_quest = new ArrayList<>();
                try {
                    JSONArray arr = parseArr(rs.getString("quest"));
                    for (int i = 0; i < arr.size(); i++) {
                        JSONArray qEntry = (JSONArray) arr.get(i);
                        QuestP temp = new QuestP();
                        temp.template = Quest.get_quest(Short.parseShort(qEntry.get(0).toString()));
                        JSONArray rows = (JSONArray) qEntry.get(1);
                        temp.data = new short[rows.size()][];
                        for (int j = 0; j < rows.size(); j++) {
                            JSONArray cells = (JSONArray) rows.get(j);
                            temp.data[j] = new short[cells.size()];
                            for (int k = 0; k < cells.size(); k++)
                                temp.data[j][k] = Short.parseShort(cells.get(k).toString());
                        }
                        list_quest.add(temp);
                    }
                } catch (Exception ignored) {}

                if (list_quest.isEmpty()) {
                    QuestP startQuest = new QuestP();
                    startQuest.template = Quest.get_quest((short) 0);
                    if (startQuest.template != null) {
                        startQuest.data = new short[startQuest.template.data_quest != null ? startQuest.template.data_quest.length : 0][];
                        for (int i = 0; i < startQuest.data.length; i++) {
                            startQuest.data[i] = new short[startQuest.template.data_quest[i].length];
                            for (int j = 0; j < startQuest.data[i].length; j++) {
                                startQuest.data[i][j] = startQuest.template.data_quest[i].length > 0 ? startQuest.template.data_quest[i][j] : 0;
                            }
                        }
                        list_quest.add(startQuest);
                    }
                }

                // ---- item inventory ----
                item = new Item(this);
                item.it_body = new Item_wear[itemz.Item.MAX_BODY];
                item.bag47 = new ArrayList<>();
                item.box47 = new ArrayList<>();
                item.save_item_wear = new ArrayList<>();
                item.save_item_47 = new ArrayList<>();
                daHanhTrinh = new ArrayList<>();

                // 1. Load pending items
                java.util.List<Item_wear> pendingBag3 = new java.util.ArrayList<>();
                for (Object o : parseArr(rs.getString("bag3"))) {
                    Item_wear t = new Item_wear();
                    Item.readUpdateItem(o.toString(), t);
                    if (t.template != null && ItemTemplate3.get_it_by_id(t.template.id) != null) pendingBag3.add(t);
                }

                // save_it3
                for (Object o : parseArr(rs.getString("save_it3"))) {
                    Item_wear t = new Item_wear();
                    Item.readUpdateItem(o.toString(), t);
                    if (t.template != null && ItemTemplate3.get_it_by_id(t.template.id) != null) item.save_item_wear.add(t);
                }

                // pendingBox3
                java.util.List<Item_wear> pendingBox3 = new java.util.ArrayList<>();
                for (Object o : parseArr(rs.getString("box3"))) {
                    Item_wear t = new Item_wear();
                    Item.readUpdateItem(o.toString(), t);
                    if (t.template != null && ItemTemplate3.get_it_by_id(t.template.id) != null) pendingBox3.add(t);
                }

                // it_body
                for (Object o : parseArr(rs.getString("it_body"))) {
                    Item_wear t = new Item_wear();
                    Item.readUpdateItem(o.toString(), t);
                    if (t.template != null && ItemTemplate3.get_it_by_id(t.template.id) != null) {
                        if (t.template.id == 11000 || t.template.typeEquip == 6 || t.index == 6) {
                            t.index = 6;
                            item.it_heart = t;
                            item.it_heart.typelock = 1;
                            item.it_body[6] = t;
                        } else if (t.template.typeEquip == 7 || t.index == 7) {
                            t.index = 7;
                            item.it_body[7] = t;
                        } else {
                            if (t.template.typeEquip >= 0 && t.template.typeEquip < item.it_body.length) {
                                t.index = t.template.typeEquip;
                            }
                            if (t.index >= 0 && t.index < item.it_body.length) {
                                item.it_body[t.index] = t;
                            }
                        }
                    }
                }
                // Đồng bộ an toàn Quả Tim giữa it_heart và it_body[6]
                if (item.it_heart != null) {
                    item.it_heart.index = 6;
                    item.it_heart.typelock = 1;
                    item.it_body[6] = item.it_heart;
                } else if (item.it_body.length > 6 && item.it_body[6] != null && (item.it_body[6].template.typeEquip == 6 || item.it_body[6].template.id == 11000)) {
                    item.it_heart = item.it_body[6];
                    item.it_heart.index = 6;
                    item.it_heart.typelock = 1;
                }

                // bag47
                for (Object o : parseArr(rs.getString("bag47"))) {
                    JSONArray a2 = (JSONArray) JSONValue.parse(o.toString());
                    if (a2 == null || a2.size() < 3) continue;
                    byte cat = Byte.parseByte(a2.get(0).toString());
                    short id = Short.parseShort(a2.get(1).toString());
                    int quant = Integer.parseInt(a2.get(2).toString());
                    if (quant <= 0 || !Item.isItemExist(cat, id)) continue;
                    while (quant > 0) {
                        int q = Math.min(quant, template.DataTemplate.MAX_ITEM_IN_BAG);
                        ItemBag47 t = new ItemBag47();
                        t.category = cat;
                        t.id = id;
                        t.quant = (short) q;
                        item.bag47.add(t);
                        quant -= q;
                    }
                }

                // Đảm bảo Thẻ Hệ Thống (ID 872) luôn có trong hành trang
                boolean hasCard872 = false;
                for (ItemBag47 it : item.bag47) {
                    if (it != null && it.category == 4 && it.id == 872) {
                        hasCard872 = true;
                        break;
                    }
                }
                if (!hasCard872) {
                    ItemBag47 card872 = new ItemBag47();
                    card872.category = 4;
                    card872.id = 872;
                    card872.quant = 1;
                    item.bag47.add(card872);
                }

                // box47
                for (Object o : parseArr(rs.getString("box47"))) {
                    JSONArray a2 = (JSONArray) JSONValue.parse(o.toString());
                    if (a2 == null || a2.size() < 3) continue;
                    byte cat = Byte.parseByte(a2.get(0).toString());
                    short id = Short.parseShort(a2.get(1).toString());
                    int quant = Integer.parseInt(a2.get(2).toString());
                    if (quant <= 0 || !Item.isItemExist(cat, id)) continue;
                    while (quant > 0) {
                        int q = Math.min(quant, template.DataTemplate.MAX_ITEM_IN_BAG);
                        ItemBag47 t = new ItemBag47();
                        t.category = cat;
                        t.id = id;
                        t.quant = (short) q;
                        item.box47.add(t);
                        quant -= q;
                    }
                }

                // 2. Tính toán max_bag và max_box chuẩn
                // Tổng item thực tế trong hành trang & rương đồ
                int totalBagSlots = pendingBag3.size() + item.bag47.size();
                int totalBoxSlots = pendingBox3.size() + item.box47.size();

                // Tính max_bag
                int targetBag = itemz.Item.DEFAULT_BAG;
                if (core.Manager.gI().isTestMode()) {
                    targetBag = itemz.Item.MAX_BAG_LIMIT;
                } else if (_loadedMaxBag > 0) {
                    targetBag = Math.min(itemz.Item.MAX_BAG_LIMIT, _loadedMaxBag);
                }
                targetBag = Math.min(itemz.Item.MAX_BAG_LIMIT, Math.max(targetBag, Math.max(itemz.Item.DEFAULT_BAG, totalBagSlots)));
                for (Item_wear t : pendingBag3) {
                    int idx = t.index & 0xFFFF;
                    if (idx >= targetBag && idx < itemz.Item.MAX_BAG_LIMIT) {
                        targetBag = idx + 1;
                    }
                }
                targetBag = Math.min(itemz.Item.MAX_BAG_LIMIT, targetBag);
                item.max_bag = (short) targetBag;
                item.bag3 = new Item_wear[targetBag];

                // Đặt item vào bag3 an toàn (tuyệt đối không để mất bất kỳ item nào)
                for (Item_wear t : pendingBag3) {
                    int idx = t.index & 0xFFFF;
                    boolean placed = false;
                    if (idx < item.bag3.length && item.bag3[idx] == null) {
                        item.bag3[idx] = t;
                        placed = true;
                    } else {
                        for (int s = 0; s < item.bag3.length; s++) {
                            if (item.bag3[s] == null) {
                                t.index = (short) s;
                                item.bag3[s] = t;
                                placed = true;
                                break;
                            }
                        }
                    }
                    if (!placed && item.bag3.length < itemz.Item.MAX_BAG_LIMIT) {
                        Item_wear[] newBag = new Item_wear[item.bag3.length + 1];
                        System.arraycopy(item.bag3, 0, newBag, 0, item.bag3.length);
                        t.index = (short) item.bag3.length;
                        newBag[t.index] = t;
                        item.bag3 = newBag;
                        item.max_bag = (short) item.bag3.length;
                    }
                }

                // Tính max_box
                int targetBox = itemz.Item.DEFAULT_BOX;
                if (core.Manager.gI().isTestMode()) {
                    targetBox = itemz.Item.MAX_BOX_LIMIT;
                } else if (_loadedMaxBox > 0) {
                    targetBox = Math.min(itemz.Item.MAX_BOX_LIMIT, _loadedMaxBox);
                }
                targetBox = Math.min(itemz.Item.MAX_BOX_LIMIT, Math.max(targetBox, Math.max(itemz.Item.DEFAULT_BOX, totalBoxSlots)));
                for (Item_wear t : pendingBox3) {
                    int idx = t.index & 0xFFFF;
                    if (idx >= targetBox && idx < itemz.Item.MAX_BOX_LIMIT) {
                        targetBox = idx + 1;
                    }
                }
                targetBox = Math.min(itemz.Item.MAX_BOX_LIMIT, targetBox);
                item.max_box = (short) targetBox;
                item.box3 = new Item_wear[targetBox];

                // Đặt item vào box3 an toàn (tuyệt đối không để mất bất kỳ item nào)
                for (Item_wear t : pendingBox3) {
                    int idx = t.index & 0xFFFF;
                    boolean placed = false;
                    if (idx < item.box3.length && item.box3[idx] == null) {
                        item.box3[idx] = t;
                        placed = true;
                    } else {
                        for (int s = 0; s < item.box3.length; s++) {
                            if (item.box3[s] == null) {
                                t.index = (short) s;
                                item.box3[s] = t;
                                placed = true;
                                break;
                            }
                        }
                    }
                    if (!placed && item.box3.length < itemz.Item.MAX_BOX_LIMIT) {
                        Item_wear[] newBox = new Item_wear[item.box3.length + 1];
                        System.arraycopy(item.box3, 0, newBox, 0, item.box3.length);
                        t.index = (short) item.box3.length;
                        newBox[t.index] = t;
                        item.box3 = newBox;
                        item.max_box = (short) item.box3.length;
                    }
                }

                // save_it47
                for (Object o : parseArr(rs.getString("save_it47"))) {
                    JSONArray a2 = (JSONArray) JSONValue.parse(o.toString());
                    if (a2 == null || a2.size() < 3) continue;
                    byte cat = Byte.parseByte(a2.get(0).toString());
                    short id = Short.parseShort(a2.get(1).toString());
                    short quant = Short.parseShort(a2.get(2).toString());
                    if (quant > 0 && Item.isItemExist(cat, id) && ((cat == 4 && id > 28) || cat == 7 || cat == 8)) {
                        ItemBag47 t = new ItemBag47();
                        t.category = cat;
                        t.id = id;
                        t.quant = quant;
                        item.save_item_47.add(t);
                    }
                }
                // hanhtrinh
                for (Object o : parseArr(rs.getString("hanhtrinh"))) {
                    JSONArray a2 = (JSONArray) JSONValue.parse(o.toString());
                    if (a2 == null || a2.size() < 3) continue;
                    byte cat = Byte.parseByte(a2.get(0).toString());
                    short id = Short.parseShort(a2.get(1).toString());
                    short quant = Short.parseShort(a2.get(2).toString());
                    if (cat >= 0 && quant >= 0 && ItemTemplate4.get_it_by_id(id) != null) {
                        ItemBag47 t = new ItemBag47();
                        t.category = cat;
                        t.id = id;
                        t.quant = quant;
                        daHanhTrinh.add(t);
                    }
                }

                // ---- rms ----
                rms = new byte[11][];
                {
                    JSONArray arr = parseArr(rs.getString("rms"));
                    for (int i = 0; i < arr.size() && i < rms.length; i++) {
                        Object obj = arr.get(i);
                        if (obj instanceof JSONArray) {
                            JSONArray row = (JSONArray) obj;
                            rms[i] = new byte[row.size()];
                            for (int j = 0; j < row.size(); j++) rms[i][j] = Byte.parseByte(row.get(j).toString());
                        } else if (obj != null) {
                            Object parsed = JSONValue.parse(obj.toString());
                            if (parsed instanceof JSONArray) {
                                JSONArray row = (JSONArray) parsed;
                                rms[i] = new byte[row.size()];
                                for (int j = 0; j < row.size(); j++) rms[i][j] = Byte.parseByte(row.get(j).toString());
                            }
                        }
                    }
                    for (int i = 0; i < rms.length; i++) if (rms[i] == null) rms[i] = new byte[0];
                }

                // ---- skill ----
                // Mới: [{"id":1,"exp":0,"lv":0,"dp":0}, ...]
                // Cũ: [[indexSkillInServer, exp, lvdevil, devilpercent], ...]
                skill_point = new ArrayList<>();
                {
                    boolean[] checkSkill = new boolean[2];
                    JSONArray arr = parseArr(rs.getString("skill"));
                    for (int i = 0; i < arr.size(); i++) {
                        Skill_info sk = new Skill_info();
                        Object elem = arr.get(i);
                        if (elem instanceof JSONObject) {
                            JSONObject eo = (JSONObject) elem;
                            sk.exp         = jLong(eo, "exp", 0L);
                            sk.temp        = Skill_Template.get_temp(jShort(eo, "id", (short)0), sk.exp);
                            sk.lvdevil     = jByte(eo, "lv", (byte)0);
                            sk.devilpercent= jByte(eo, "dp", (byte)0);
                        } else {
                            JSONArray inner = (JSONArray) JSONValue.parse(elem.toString());
                            sk.exp         = Long.parseLong(inner.get(1).toString());
                            if (sk.exp < -1) sk.exp = 0;
                            sk.temp        = Skill_Template.get_temp(Short.parseShort(inner.get(0).toString()), sk.exp);
                            sk.lvdevil     = Byte.parseByte(inner.get(2).toString());
                            sk.devilpercent= Byte.parseByte(inner.get(3).toString());
                        }
                        if (sk.temp != null) {
                            if (sk.temp.Lv_RQ > 0 && sk.exp < 0) {
                                sk.exp = 0;
                            }
                            if (sk.temp.ID == 1016) checkSkill[0] = true;
                            if (sk.temp.ID == 1017) checkSkill[1] = true;
                            skill_point.add(sk);
                        }
                    }
                    if (skill_point.isEmpty()) {
                        skill_point = new ArrayList<>(getDefaultPlayerSkills(clazz));
                    } else {
                        ensureDefaultPlayerSkills(clazz, skill_point);
                    }
                    ensureFactionSkill();
                }

                // ---- friend / enemy ----
                friend_list = new ArrayList<>();
                { JSONArray arr = parseArr(rs.getString("friend"));
                  for (int i = 0; i < arr.size(); i++) { FriendTemp t = new FriendTemp((JSONArray) JSONValue.parse(arr.get(i).toString())); friend_list.add(t); t.id = friend_list.indexOf(t); } }
                enemy_list = new ArrayList<>();
                { JSONArray arr = parseArr(rs.getString("enemy"));
                  for (int i = 0; i < arr.size(); i++) { FriendTemp t = new FriendTemp((JSONArray) JSONValue.parse(arr.get(i).toString())); enemy_list.add(t); t.id = enemy_list.indexOf(t); } }

                // ---- fashion ----
                // Cấu trúc JSON: {"fp":[...],"f2":[...],"boat":[...]}
                // Cũ: [[itfashionP...],[fashion...],[itemboat...]]
                itfashionP = new ArrayList<>();
                fashion    = new ArrayList<>();
                itemboat   = new ArrayList<>();
                {
                    String raw = rs.getString("fashion");
                    JSONObject fo = parseObj(raw);
                    if (fo != null) {
                        // Format mới
                        for (Object o : jArr(fo, "fp")) {
                            JSONObject eo = (JSONObject) o;
                            short fid = jShort(eo, "id", (short)0);
                            short ficon = jShort(eo, "icon", (short)0);
                            byte fcat = jByte(eo, "cat", (byte)0);
                            boolean fuse = jBool(eo, "use", false);
                            if (fcat == 103 || fcat == 108) {
                                if (ItemHair.get_item(fid, fcat) == null) continue;
                            }
                            itfashionP.add(new ItemFashionP(fid, ficon, fcat, fuse));
                        }
                        boolean hasEquippedFashion = false;
                        for (Object o : jArr(fo, "f2")) {
                            JSONObject eo = (JSONObject) o;
                            short fid = jShort(eo, "id", (short)0);
                            if (ItemFashion.get_item(fid) == null) continue;
                            ItemFashionP2 f2 = new ItemFashionP2();
                            f2.id = fid;
                            boolean wantUse = jBool(eo, "use", false);
                            if (wantUse && !hasEquippedFashion) {
                                f2.is_use = true;
                                hasEquippedFashion = true;
                            } else {
                                f2.is_use = false;
                            }
                            f2.level = jByte(eo, "lv", (byte)0); f2.expires = jLong(eo, "exp", -1L);
                            fashion.add(f2);
                        }
                        for (Object o : jArr(fo, "boat")) {
                            JSONObject eo = (JSONObject) o;
                            byte bid = jByte(eo, "id", (byte)0);
                            if (ItemBoat.get_item(bid) == null) continue;
                            ItemBoatP b = new ItemBoatP(); b.id = bid; b.is_use = jBool(eo, "use", false);
                            itemboat.add(b);
                        }
                    } else {
                        // Format cũ array[3]
                        JSONArray fa = parseArr(raw);
                        if (fa.size() > 0) {
                            JSONArray fp = (JSONArray) fa.get(0);
                            for (int i = 0; i < fp.size(); i++) {
                                JSONArray t = (JSONArray) fp.get(i);
                                short fid = Short.parseShort(t.get(1).toString());
                                short ficon = Short.parseShort(t.get(2).toString());
                                byte fcat = Byte.parseByte(t.get(0).toString());
                                boolean fuse = Byte.parseByte(t.get(3).toString()) == 1;
                                if (fcat == 103 || fcat == 108) {
                                    if (ItemHair.get_item(fid, fcat) == null) continue;
                                }
                                itfashionP.add(new ItemFashionP(fid, ficon, fcat, fuse));
                            }
                        }
                        if (fa.size() > 1) {
                            JSONArray f2arr = (JSONArray) fa.get(1);
                            boolean hasEquippedFashion = false;
                            for (int i = 0; i < f2arr.size(); i++) {
                                JSONArray t = (JSONArray) JSONValue.parse(f2arr.get(i).toString());
                                short fid = Short.parseShort(t.get(0).toString());
                                if (ItemFashion.get_item(fid) == null) continue;
                                ItemFashionP2 f2 = new ItemFashionP2();
                                f2.id = fid;
                                boolean wantUse = Byte.parseByte(t.get(1).toString()) == 1;
                                if (wantUse && !hasEquippedFashion) {
                                    f2.is_use = true;
                                    hasEquippedFashion = true;
                                } else {
                                    f2.is_use = false;
                                }
                                f2.level = Byte.parseByte(t.get(2).toString());
                                f2.expires = t.size() >= 4 ? Long.parseLong(t.get(3).toString()) : -1L;
                                fashion.add(f2);
                            }
                        }
                        if (fa.size() > 2) {
                            JSONArray boats = (JSONArray) fa.get(2);
                            for (int i = 0; i < boats.size(); i++) {
                                JSONArray t = (JSONArray) JSONValue.parse(boats.get(i).toString());
                                byte bid = Byte.parseByte(t.get(0).toString());
                                if (ItemBoat.get_item(bid) == null) continue;
                                ItemBoatP b = new ItemBoatP(); b.id = bid; b.is_use = Byte.parseByte(t.get(1).toString()) == 1;
                                itemboat.add(b);
                            }
                        }
                    }
                }

                // ---- eff (active buffs) ----
                list_eff = new CopyOnWriteArrayList<>();
                try {
                    JSONArray arr = parseArr(rs.getString("eff"));
                    for (int i = 0; i < arr.size(); i++) {
                        Object elem = arr.get(i);
                        if (elem instanceof JSONObject) {
                            JSONObject eo = (JSONObject) elem;
                            list_eff.add(new EffTemplate(jByte(eo,"id",(byte)0), jInt(eo,"p",0),
                                System.currentTimeMillis() + jLong(eo,"t",0L)));
                        } else {
                            JSONArray inner = (JSONArray) JSONValue.parse(elem.toString());
                            if (inner != null && inner.size() >= 3)
                                list_eff.add(new EffTemplate(
                                    Byte.parseByte(inner.get(0).toString()),
                                    Integer.parseInt(inner.get(1).toString()),
                                    System.currentTimeMillis() + Long.parseLong(inner.get(2).toString())));
                        }
                    }
                } catch (Exception ignored) {}


                // ---- eff_save ----
                try {
                    JSONArray a = parseArr(rs.getString("eff_save"));
                    eff_save = new int[a.size()];
                    for (int i = 0; i < a.size(); i++) eff_save[i] = Integer.parseInt(a.get(i).toString());
                } catch (Exception ex) { eff_save = new int[5]; }

                // ---- de tu (load by owner_id) ----
                try {
                    String initialName = (this.nameDe != null && !this.nameDe.isBlank()) ? this.nameDe : this.name;
                    try {
                        String nameDeVal = rs.getString("name_de");
                        if (nameDeVal != null && !nameDeVal.isBlank()) {
                            initialName = nameDeVal;
                            this.nameDe = nameDeVal;
                        }
                    } catch (Exception ignored) {}

                    DeTu pCreate = new DeTu(initialName, this);
                    if (pCreate.setup()) {
                        this.nameDe = pCreate.name;
                        pCreate.setin4();
                        pCreate.setUsername("a_test");
                        pCreate.index_map = (short)(IDPlayer + 25_000);
                        pCreate.map = null;
                        pCreate.x = -1;
                        pCreate.y = -1;
                        this.detu = pCreate;
                        this.isDeOnl = (pCreate.detuStatus != DeTu.STATUS_HOME && pCreate.detuStatus != DeTu.STATUS_FUSION);
                    } else {
                        this.detu = null;
                    }
                } catch (Exception e) {
                    System.err.println("Error loading disciple for player " + this.name + " (ID: " + IDPlayer + "): " + e.getMessage());
                }

                // ---- body (Ability) ----
                ability = new Ability(this);

                // ---- check/remove event items ----
                EventManager.dispatchCheckAndRemoveEventItems(this);
        return true;
    }

    public void setin4() throws IOException {
        xold = x;
        yold = y;
        // rankWanted = -1;
        if (this.map == null) {
            typePirate = -1;
            indexGhostServer = -1;
            if (!this.isBot) {
                type_pk = -1;
            }
        }
        Clan myClan = Clan.get_my_clan(this.IDPlayer, this.name);
        if (myClan != null) {
            clan = myClan;
        }
        // Recalculate player ability & stat cache
        setAbility();
        int hp_max = this.getHpMax();

        if (this.hp == -1) {
            this.hp = hp_max;
        }
        if (this.hp <= 0) {
            // Khi thoát game vào lại nếu chết: Tự động về đúng làng của map ngoài đang ở
            int currentMapId = (this.map != null && this.map.template != null) ? this.map.template.id : (this.id_map_save > 0 ? this.id_map_save : 1);
            int returnVillage = Zone.getVillageMapId(currentMapId);
            if (returnVillage <= 0) returnVillage = (this.id_map_save > 0) ? this.id_map_save : 1;
            this.id_map_save = returnVillage;

            Zone[] villageZones = Zone.getMapByID(returnVillage);
            if (villageZones != null && villageZones.length > 0 && villageZones[0] != null) {
                this.map = villageZones[0];
                this.x = 300;
                this.y = 300;
                if (villageZones[0].template != null && villageZones[0].template.npcs != null) {
                    for (int i = 0; i < villageZones[0].template.npcs.size(); i++) {
                        Npc npc_temp = villageZones[0].template.npcs.get(i);
                        if (npc_temp != null && "Bản đồ".equals(npc_temp.namegt)) {
                            this.x = npc_temp.x;
                            if (npc_temp.y < 250) {
                                this.y = (short) (npc_temp.y + 20);
                            } else {
                                this.y = (short) (npc_temp.y - 40);
                            }
                            break;
                        }
                    }
                }
            }
            int hp_after = hp_max / 10;
            if (hp_after < 1) hp_after = 100;
            this.hp = hp_after;
            this.isdie = false;
        } else {
            if (this.hp > hp_max) {
                this.hp = hp_max;
            }
            this.isdie = false;
        }
//        System.out.println(this.hp + " " +hp_max);
        if (this.mp == -1) {
            this.mp = this.ability.get_mp_max(true);
        }
        ischangemap = false;
        lastValidX = x;  // khởi tạo từ tọa độ DB
        lastValidY = y;
        spawnX = x;
        spawnY = y;
        hasMovedFromSpawn = false;
        time_change_map = System.currentTimeMillis() + 2000L; // delay 2s tranh auto change map ngay khi login
        msgs = new LinkedBlockingQueue<>();
        it_map = new ItemMap[3];
        tool_upgrade = new int[]{-1, -1};
        item_chuyenhoa_save_0 = null;
        item_chuyenhoa_save_1 = null;
        item_to_kham_ngoc = null;
        item_to_kham_ngoc_id_ngoc = -1;
        trade_target = null;
        list_item_trade3 = null;
        list_item_trade47 = null;
        money_trade = 0;
        fee_trade = 0;
        is_lock_trade = false;
        is_accept_trade = false;
        use_item_3 = -1;
        time_buff_hp_mp = System.currentTimeMillis() + 5000L;
        party = null;
        wait_change_map = true;
        id_meet_in_map = new HashSet<>();
        percent_da_sieu_cap = 35;
        key_red_line = new LinkedBlockingQueue<>();
        time_key_red_line = -1;
        time_pick_item_other = 0;
        is_combo = null;
        list_can_combo = new ArrayList<>();
        map_boss_info = null;
        if (item.it_heart != null) {
            item.it_heart.index = 6;
            item.it_heart.typelock = 1;
            item.it_body[6] = item.it_heart;
        } else if (item.it_body != null && item.it_body.length > 6 && item.it_body[6] != null && (item.it_body[6].template != null && (item.it_body[6].template.typeEquip == 6 || item.it_body[6].template.id == 11000))) {
            item.it_heart = item.it_body[6];
            item.it_heart.index = 6;
            item.it_heart.typelock = 1;
        }
        resetCountKichAn();

        tocSuper = 0;
        ship_pet = Ship_pet.get_pet(this);
        this.timeEnterMap = System.currentTimeMillis() + 60_000;
        try { refreshRecharge(); } catch (Exception ignored) {}
    }

    public void resetCountKichAn() {
        this.danhLaChoang = 0;
        this.thanhLoc = 0;
        this.nenDau = 0;
        this.giaiPhongNangLuong = 0;
        if (this.getService() != null) {
            if (this.ability != null && this.ability.get_kich_an(7) > 0) this.getService().send_count_kick_ava((byte) 7, (byte) 0);
            if (this.ability != null && this.ability.get_kich_an(8) > 0) this.getService().send_count_kick_ava((byte) 8, (byte) 0);
            if (this.ability != null && this.ability.get_kich_an(9) > 0) this.getService().send_count_kick_ava((byte) 9, (byte) 0);
            if (this.ability != null && this.ability.get_kich_an(10) > 0) this.getService().send_count_kick_ava((byte) 10, (byte) 0);
        }
    }

    public byte getDanhLaChoang() {
        if (danhLaChoang < 0) danhLaChoang = 0;
        return danhLaChoang;
    }

    public void setDanhLaChoang(int val) {
        this.danhLaChoang = (byte) Math.max(0, Math.min(100, val));
    }

    public void addDanhLaChoang(int max) {
        if (this.danhLaChoang < 0) this.danhLaChoang = 0;
        if (max <= 0) max = 20;
        if (this.danhLaChoang < max) {
            this.danhLaChoang++;
        } else {
            this.danhLaChoang = (byte) max;
        }
    }

    public void resetDanhLaChoang() {
        this.danhLaChoang = 0;
        if (this.getService() != null) {
            this.getService().send_count_kick_ava((byte) 7, (byte) 0);
        }
    }

    public byte getThanhLoc() {
        if (thanhLoc < 0) thanhLoc = 0;
        return thanhLoc;
    }

    public void setThanhLoc(int val) {
        this.thanhLoc = (byte) Math.max(0, Math.min(100, val));
    }

    public void addThanhLoc(int max) {
        if (this.thanhLoc < 0) this.thanhLoc = 0;
        if (max <= 0) max = 20;
        if (this.thanhLoc < max) {
            this.thanhLoc++;
        } else {
            this.thanhLoc = (byte) max;
        }
    }

    public void resetThanhLoc() {
        this.thanhLoc = 0;
        if (this.getService() != null) {
            this.getService().send_count_kick_ava((byte) 8, (byte) 0);
        }
    }

    public byte getNenDau() {
        if (nenDau < 0) nenDau = 0;
        return nenDau;
    }

    public void setNenDau(int val) {
        this.nenDau = (byte) Math.max(0, Math.min(100, val));
    }

    public void addNenDau(int max) {
        if (this.nenDau < 0) this.nenDau = 0;
        if (max <= 0) max = 20;
        if (this.nenDau < max) {
            this.nenDau++;
        } else {
            this.nenDau = (byte) max;
        }
    }

    public void resetNenDau() {
        this.nenDau = 0;
        if (this.getService() != null) {
            this.getService().send_count_kick_ava((byte) 9, (byte) 0);
        }
    }

    public byte getGiaiPhongNangLuong() {
        if (giaiPhongNangLuong < 0) giaiPhongNangLuong = 0;
        return giaiPhongNangLuong;
    }

    public void setGiaiPhongNangLuong(int val) {
        this.giaiPhongNangLuong = (byte) Math.max(0, Math.min(100, val));
    }

    public void addGiaiPhongNangLuong(int max) {
        if (this.giaiPhongNangLuong < 0) this.giaiPhongNangLuong = 0;
        if (max <= 0) max = 20;
        if (this.giaiPhongNangLuong < max) {
            this.giaiPhongNangLuong++;
        } else {
            this.giaiPhongNangLuong = (byte) max;
        }
    }

    public void resetGiaiPhongNangLuong() {
        this.giaiPhongNangLuong = 0;
        if (this.getService() != null) {
            this.getService().send_count_kick_ava((byte) 10, (byte) 0);
        }
    }

    public static class KichAnAccCandidate {
        public int id;
        public int currentCount;
        public int maxCount;
        public int itemCount;

        public KichAnAccCandidate(int id, int currentCount, int maxCount, int itemCount) {
            this.id = id;
            this.currentCount = currentCount;
            this.maxCount = maxCount;
            this.itemCount = Math.max(1, itemCount);
        }

        public int getRemaining() {
            return Math.max(0, maxCount - currentCount);
        }
    }

    public static int chooseAccumulativeKichAn(List<KichAnAccCandidate> candidates) {
        if (candidates == null || candidates.isEmpty()) return -1;
        if (candidates.size() == 1) return candidates.get(0).id;

        // Ưu tiên:
        // 1. Cái gần kích hoạt (remaining <= 2) -> tăng trọng số cao để dứt điểm kích hoạt
        // 2. Cái có điểm tích lũy thấp nhất -> tăng trọng số để cân bằng nếu chưa cái nào gần kích hoạt
        // 3. Phân bổ theo số lượng món trang bị (itemCount)
        boolean anyNearActivation = false;
        int minCount = Integer.MAX_VALUE;
        for (KichAnAccCandidate c : candidates) {
            if (c.getRemaining() <= 2) {
                anyNearActivation = true;
            }
            if (c.currentCount < minCount) {
                minCount = c.currentCount;
            }
        }

        int totalWeight = 0;
        int[] weights = new int[candidates.size()];
        for (int i = 0; i < candidates.size(); i++) {
            KichAnAccCandidate c = candidates.get(i);
            int w = c.itemCount * 10;
            if (c.getRemaining() <= 2) {
                int mult = Math.max(1, 4 - c.getRemaining()); // remaining = 1 -> x3, remaining = 2 -> x2
                w *= mult;
            } else if (!anyNearActivation && c.currentCount == minCount) {
                w = (w * 15) / 10; // +50% weight cho cái thấp nhất để cân bằng
            }
            weights[i] = Math.max(1, w);
            totalWeight += weights[i];
        }

        int roll = ZUtil.random(totalWeight);
        int running = 0;
        for (int i = 0; i < candidates.size(); i++) {
            running += weights[i];
            if (roll < running) {
                return candidates.get(i).id;
            }
        }
        return candidates.get(candidates.size() - 1).id;
    }

    public void accumulateOffensiveKichAn(map.MapService mapService) {
        if (this.ability == null) return;
        List<KichAnAccCandidate> list = new ArrayList<>();
        int ka7 = this.ability.get_kich_an(7);
        int max7 = Math.max(10, 16 - (ka7 - 1)); // 1 món = 16 hits, full 6 món = 11 hits
        if (ka7 > 0 && this.get_eff(407) == null) {
            list.add(new KichAnAccCandidate(7, this.getDanhLaChoang(), max7, ka7));
        }
        int ka8 = this.ability.get_kich_an(8);
        int max8 = Math.max(4, 8 - (ka8 - 1)); // 1 món = 8 hits, full 6 món = 4 hits
        if (ka8 > 0 && this.get_eff(408) == null) {
            list.add(new KichAnAccCandidate(8, this.getThanhLoc(), max8, ka8));
        }

        if (list.isEmpty()) return;
        int chosen = chooseAccumulativeKichAn(list);
        if (chosen == 7) {
            this.addDanhLaChoang(max7);
            if (this.getService() != null) {
                this.getService().send_count_kick_ava((byte) 7, this.getDanhLaChoang());
            }
        } else if (chosen == 8) {
            this.addThanhLoc(max8);
            if (this.getService() != null) {
                this.getService().send_count_kick_ava((byte) 8, this.getThanhLoc());
            }
            if (this.getThanhLoc() >= max8) {
                this.resetThanhLoc();
                this.add_new_eff(408, 1, 30_000);
                if (mapService != null) {
                    mapService.send_kich_an(this, this, 1, 8, 0, 0);
                } else if (this.map != null) {
                    this.map.getService().send_kich_an(this, this, 1, 8, 0, 0);
                }
                try {
                    this.send_kich_an();
                } catch (Exception ignored) {}
            }
        }
    }

    public void processDefensiveKichAnHit(Object attacker, map.Zone targetMap) {
        if (this.ability == null) return;
        List<KichAnAccCandidate> list = new ArrayList<>();
        int ka9 = this.ability.get_kich_an(9);
        int max9 = Math.max(9, 15 - (ka9 - 1)); // 1 món = 15 hits, full 6 món = 10 hits
        if (ka9 > 0 && this.get_eff(409) == null) {
            list.add(new KichAnAccCandidate(9, this.getNenDau(), max9, ka9));
        }
        int ka10 = this.ability.get_kich_an(10);
        int max10 = Math.max(12, 22 - (ka10 - 1) * 2); // 1 món = 22 hits, full 6 món = 12 hits
        if (ka10 > 0 && this.get_eff(410) == null) {
            list.add(new KichAnAccCandidate(10, this.getGiaiPhongNangLuong(), max10, ka10));
        }

        if (list.isEmpty()) return;
        int chosen = chooseAccumulativeKichAn(list);
        if (chosen == 9) {
            this.addNenDau(max9);
            if (this.getService() != null) {
                this.getService().send_count_kick_ava((byte) 9, this.getNenDau());
            }
            if (this.getNenDau() >= max9) {
                this.resetNenDau();
                this.add_new_eff(409, 1, 60_000);
                if (targetMap != null) {
                    Player pAtk = (attacker instanceof Player) ? (Player) attacker : this;
                    targetMap.getService().send_kich_an(pAtk, this, 1, 9, 0, 0);
                }
            }
        } else if (chosen == 10) {
            this.addGiaiPhongNangLuong(max10);
            if (this.getService() != null) {
                this.getService().send_count_kick_ava((byte) 10, this.getGiaiPhongNangLuong());
            }
            if (this.getGiaiPhongNangLuong() >= max10) {
                this.resetGiaiPhongNangLuong();
                this.add_new_eff(410, 1, 60_000);
                int time_eff = 1_500 + Math.min(1000, (ka10 - 1) * 200); // 1.5s -> 2.5s Choáng
                if (attacker instanceof Player) {
                    Player pAtk = (Player) attacker;
                    EffTemplate eff = pAtk.get_eff(201);
                    if (eff == null) {
                        pAtk.add_new_eff(201, 1, time_eff);
                    } else {
                        eff.time = System.currentTimeMillis() + time_eff;
                    }
                    if (targetMap != null) {
                        targetMap.getService().send_kich_an(pAtk, this, 1, 10, 5, 50);
                        targetMap.getService().send_choang(this, pAtk, time_eff);
                    }
                } else if (attacker instanceof mob.Mob) {
                    mob.Mob mobAtk = (mob.Mob) attacker;
                    mobAtk.isChoang = true;
                    mobAtk.timeChoang = System.currentTimeMillis() + time_eff;
                    if (targetMap != null) {
                        targetMap.getService().send_kich_an(this, this, 1, 10, 5, 50);
                        targetMap.getService().send_choang_mob(this, mobAtk, time_eff);
                    }
                }
            }
        }
    }

    public int get_level_percent() {
        try {
            if (level >= 100) {
                int tt = Math.max(0, Math.min((int) this.thongthao, Level.LEVEL_THONGTHAO.length - 1));
                long denom = Level.LEVEL_THONGTHAO[tt];
                if (denom <= 0) return 0;
                return (int) Math.min(1000L, Math.max(0L, (exp * 1000L) / denom));
            } else {
                int lvIdx = Math.max(0, Math.min((int) level - 1, Level.ENTRYS.length - 1));
                if (Level.ENTRYS[lvIdx] == null || Level.ENTRYS[lvIdx].exp <= 0) return 0;
                return (int) Math.min(1000L, Math.max(0L, (exp * 1000L) / Level.ENTRYS[lvIdx].exp));
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public void updateArchiDaily(int id) {
        if (this.archiDaily != null) {
            for (int i = 0; i < this.archiDaily.length; i++) {
                if (this.archiDaily[i] != null && (this.archiDaily[i].state == 0 || this.archiDaily[i].state == 1)
                        && this.archiDaily[i].id == id) {
                    this.archiDaily[i].currentNum++;
                }
            }
        }
        updateArchiPrivatePass(id);
    }

    public void updateArchiPrivatePass(int id) {
        if (this.archiPrivatePass == null) return;
        boolean updated = false;
        for (int i = 0; i < this.archiPrivatePass.length; i++) {
            if (this.archiPrivatePass[i] != null && (this.archiPrivatePass[i].state == 0 || this.archiPrivatePass[i].state == 1)
                    && this.archiPrivatePass[i].id == id) {
                this.archiPrivatePass[i].currentNum++;
                updated = true;
            }
        }
        if (updated) {
            achievement.ArchiPrivatePass.savePassData(this, this.currentPassSeason, this.archiPrivatePass);
        }
    }

    public ArchiDaily getArchiDaily(int id) {
        if (this.archiDaily == null) return null;
        for (int i = 0; i < this.archiDaily.length; i++) {
            if (this.archiDaily[i] != null && (this.archiDaily[i].state == 0 || this.archiDaily[i].state == 1)
                    && this.archiDaily[i].id == id) {
                return this.archiDaily[i];
            }
        }
        return null;
    }

    /**
     * Cho phép bot subclass (BotPlayerReal) set các private field.
     * Gọi từ BotPlayerReal.setupFromBotTable().
     */
    protected void setupBotFields(long vang, int kimcuong, int ruby,
                                   int point_tich_tieu, int level_so_tay, int exp_so_tay,
                                   int point_hang_dong, int hd_max, int so_tay_vip, int active_so_tay,
                                   int diem_danh_event) {
        this.vang             = vang;
        this.kimcuong         = kimcuong;
        this.ruby             = ruby;
        this.point_tich_tieu  = point_tich_tieu;
        this.level_so_tay     = level_so_tay;
        this.exp_so_tay       = exp_so_tay;
        this.point_hang_dong  = point_hang_dong;
        this.hd_max           = hd_max;
        this.so_tay_vip       = so_tay_vip;
        this.active_so_tay    = active_so_tay;
        this.diem_danh_event  = diem_danh_event;
    }

    @SuppressWarnings("unchecked")
    public synchronized int flush(Player p, boolean print) {
        return flush(p, print, "players");
    }

    public synchronized int flush(Player p, boolean print, String tableName) {
        if (p == null) return 1;
        if (p instanceof DeTu) return ((DeTu) p).saveDeTu(print);
        if (p.isBot && !(p instanceof bot.botplayer.BotPlayerReal)) return 1;

        if ("players_bot".equals(tableName) || p instanceof bot.botplayer.BotPlayerReal) {
            try (Connection connection = DbManager.gI().getConnect()) {
                return flushBotToDb(p, connection, print);
            } catch (Exception e) {
                System.err.println("Error flushing bot " + p.name + ": " + e.getMessage());
                return 1;
            }
        }

        int result = 0;
        // Các cột giữ nguyên và không đổi: cột nào là plain INT/VARCHAR thì giữ nguyên
        // Các JSON column: viết lại theo JSONObject named-key format
        String query = "UPDATE `" + tableName + "` SET "
            + "`clazz`=?, `level`=?, `date`=?, `body_parts`=?, `inventory`=?, "
            + "`potential`=?, `bag3`=?, `it_body`=?, `bag47`=?, `box47`=?, `box3`=?, "
            + "`save_it3`=?, `save_it47`=?, `hanhtrinh`=?, `rms`=?, `skill`=?, "
            + "`friend`=?, `enemy`=?, `fashion`=?, `eff`=?, `quest`=?, "
            + "`myarchidaily`=?, `mypet`=?, `mbv`=?, "
            + "`coin`=?, `exp`=?, "
            + "`timeResetHangNgay`=?, `eff_save`=? "
            + "WHERE `id`=" + p.IDPlayer + ";";

        try (Connection connection = DbManager.gI().getConnect()) {
            if (connection == null) {
                System.err.println("[Player] Cannot flush player " + p.name + ": Database connection unavailable.");
                return 1;
            }
            try (PreparedStatement ps = connection.prepareStatement(query)) {

            int idx = 0;

            // 0. clazz
            ps.setByte(++idx, p.clazz);

            // 1. level
            { JSONObject o = new JSONObject(); o.put("lv", p.level); o.put("exp", p.exp); o.put("tt", p.thongthao);
              ps.setNString(++idx, o.toJSONString()); }

            // 2. date
            ps.setNString(++idx, p.date != null ? p.date.toString() : org.joda.time.DateTime.now().toString());

            // 3. body_parts
            {
                JSONObject o = new JSONObject();
                o.put("head", p.head);
                o.put("hair", p.hair);
                
                short part_weapon = -1;
                if (p.item != null && p.item.it_body != null && p.item.it_body[0] != null && p.item.it_body[0].template != null) {
                    template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(p.item.it_body[0].template.id);
                    if (temp != null) {
                        part_weapon = temp.part;
                    }
                }
                p.part_weapon = part_weapon;
                o.put("weapon", part_weapon);
                
                short part_body = -1;
                if (p.item != null && p.item.it_body != null && p.item.it_body[3] != null && p.item.it_body[3].template != null) {
                    template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(p.item.it_body[3].template.id);
                    if (temp != null) {
                        part_body = temp.part;
                    }
                }
                p.part_body = part_body;
                o.put("body", part_body);
                
                short part_leg = -1;
                if (p.item != null && p.item.it_body != null && p.item.it_body[5] != null && p.item.it_body[5].template != null) {
                    template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(p.item.it_body[5].template.id);
                    if (temp != null) {
                        part_leg = temp.part;
                    }
                }
                p.part_leg = part_leg;
                o.put("leg", part_leg);
                
                short part_ring = -1;
                if (p.item != null && p.item.it_body != null && p.item.it_body[4] != null && p.item.it_body[4].template != null) {
                    template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(p.item.it_body[4].template.id);
                    if (temp != null) {
                        part_ring = temp.part;
                    }
                }
                p.part_ring = part_ring;
                o.put("ring", part_ring);
                
                ps.setNString(++idx, o.toJSONString());
            }

            // 4. inventory
            {
                JSONObject inv = new JSONObject();
                inv.put("pvppoint", p.pvppoint);
                inv.put("diemdanh", p.diemdanh);
                inv.put("diemdanh_ngay", p.diemdanh_ngay);
                inv.put("wanted_point", p.wanted_point);
                inv.put("time_online", p.getTotalOnlineSeconds());

                JSONObject oSite = new JSONObject();
                int currentMapId = (p.map != null && p.map.template != null) ? p.map.template.id : (p.id_map_save > 0 ? p.id_map_save : 1);
                int currentZoneId = (p.map != null) ? p.map.zone_id : 0;
                if (Zone.map_cant_save_site(currentMapId)) {
                    int xSave = -1, ySave = -1;
                    Zone[] mg = Zone.getMapByID(p.id_map_save > 0 ? p.id_map_save : 1);
                    if (mg != null && mg.length > 0 && mg[0].template != null && mg[0].template.npcs != null) {
                        for (int i = 0; i < mg[0].template.npcs.size(); i++) {
                            Npc npc = mg[0].template.npcs.get(i);
                            if (npc != null && "Bản đồ".equals(npc.namegt)) { xSave = npc.x; ySave = npc.y < 250 ? npc.y + 20 : npc.y - 40; break; }
                        }
                    }
                    if (xSave != -1) {
                        oSite.put("map", p.id_map_save > 0 ? p.id_map_save : 1); oSite.put("zone", 0); oSite.put("hp", p.hp); oSite.put("mp", p.mp); oSite.put("x", xSave); oSite.put("y", ySave);
                    } else {
                        oSite.put("map", 1); oSite.put("zone", 0); oSite.put("hp", p.hp); oSite.put("mp", p.mp); oSite.put("x", 830); oSite.put("y", 203);
                    }
                } else {
                    oSite.put("map", currentMapId); oSite.put("zone", currentZoneId);
                    oSite.put("hp", p.hp); oSite.put("mp", p.mp); oSite.put("x", p.x); oSite.put("y", p.y);
                }
                oSite.put("hat", p.is_show_hat ? 1 : 0); oSite.put("pk", p.pointPk); oSite.put("wpn", p.is_show_weapon ? 1 : 0);
                oSite.put("hfhair", p.is_hide_fashion_hair ? 1 : 0); oSite.put("hfhead", p.is_hide_fashion_head ? 1 : 0);
                oSite.put("tk", p.ticket); oSite.put("tkcd", p.cd_ticket_next);
                oSite.put("pvptk", p.pvp_ticket); oSite.put("pvpcd", p.cd_pvp_next);
                oSite.put("boss", p.key_boss); oSite.put("bosscd", p.cd_keyboss_next);
                oSite.put("aidon", p.aiDonLevel); oSite.put("nvl", p.nvlMax); oSite.put("ttvt", p.ttvtMax);
                oSite.put("lt", p.ltMax); oSite.put("nami", p.namiMax); oSite.put("aidonM", p.aidonMax); oSite.put("mr3", p.mr3Max);
                oSite.put("pre_map", p.pre_map_id); oSite.put("pre_zone", (int) p.pre_zone_id); oSite.put("pre_x", (int) p.pre_x); oSite.put("pre_y", (int) p.pre_y);
                JSONArray rqMaps = new JSONArray();
                if (p.completedRepeatQuestMaps != null) {
                    for (Integer mId : p.completedRepeatQuestMaps) {
                        if (mId != null) rqMaps.add(mId);
                    }
                }
                oSite.put("rq_maps", rqMaps);
                inv.put("site", oSite);

                JSONObject oInven = new JSONObject();
                oInven.put("gold", p.vang); oInven.put("gem", p.kimcuong); oInven.put("vnd", p.vnd); oInven.put("hammer", p.bua);
                oInven.put("accum", p.tichLuy); oInven.put("pvpW", p.pvp_win); oInven.put("pvpL", p.pvp_lose);
                oInven.put("ship", p.time_ship); oInven.put("hs", p.time_can_hs); oInven.put("nvl", p.time_nvl);
                oInven.put("ttvt", p.time_ttvt); oInven.put("wanted", p.wanted_point); oInven.put("ruby", p.tieuRuby);
                oInven.put("tieu_tuan", p.tieuTuan); oInven.put("tieu_tong", p.tieuTong);
                oInven.put("time_online", p.getTotalOnlineSeconds());
                oInven.put("max_bag", (int) (p.item != null ? (p.item.max_bag & 0xFFFF) : itemz.Item.DEFAULT_BAG));
                oInven.put("max_box", (int) (p.item != null ? (p.item.max_box & 0xFFFF) : itemz.Item.DEFAULT_BOX));
                JSONArray titles = new JSONArray();
                if (p.id_danh_hieu_da_so_huu != null) {
                    for (int[] idEntry : p.id_danh_hieu_da_so_huu) {
                        if (idEntry != null && idEntry.length >= 2) {
                            JSONArray item = new JSONArray();
                            item.add(idEntry[0]);
                            item.add(idEntry[1]);
                            if (idEntry.length >= 3) {
                                item.add(idEntry[2]);
                            }
                            titles.add(item);
                        }
                    }
                }
                oInven.put("titles", titles);
                inv.put("point_inven", oInven);

                JSONArray aChest = new JSONArray();
                if (p.wanted_chest != null) {
                    for (Wanted_Chest wc : p.wanted_chest) {
                        if (wc != null) { JSONObject o = new JSONObject(); o.put("id", wc.id); o.put("time", wc.timeUse); o.put("max", wc.maxTimeUse); o.put("ruby", wc.Ruby); aChest.add(o); }
                    }
                }
                inv.put("wanted_chest", aChest);
                inv.put("auto_settings", p.autoSettings != null ? p.autoSettings.toJsonObject() : new JSONObject());

                JSONArray aTichLuy = new JSONArray();
                if (p.tichTieuCheck != null) {
                    for (byte b : p.tichTieuCheck) aTichLuy.add(b);
                }
                inv.put("tich_luy", aTichLuy);

                JSONArray aTieuRuby = new JSONArray();
                if (p.tieuRubyCheck != null) {
                    for (byte b : p.tieuRubyCheck) aTieuRuby.add(b);
                }
                inv.put("tieu_ruby", aTieuRuby);

                JSONArray aTichHangNgay = new JSONArray();
                if (p.tichHangNgayCheck != null) {
                    for (byte b : p.tichHangNgayCheck) aTichHangNgay.add(b);
                }
                inv.put("tieu_hangngay", aTichHangNgay);

                JSONArray aTieuTuan = new JSONArray();
                if (p.tieuTuanCheck != null) {
                    for (byte b : p.tieuTuanCheck) aTieuTuan.add(b);
                }
                inv.put("tieu_tuan_check", aTieuTuan);

                JSONArray aTieuTong = new JSONArray();
                if (p.tieuTongCheck != null) {
                    for (byte b : p.tieuTongCheck) aTieuTong.add(b);
                }
                inv.put("tieu_tong_check", aTieuTong);

                JSONArray aNapTuan = new JSONArray();
                if (p.tichNapTuanCheck != null) {
                    for (byte b : p.tichNapTuanCheck) aNapTuan.add(b);
                }
                inv.put("nap_tuan_check", aNapTuan);

                inv.put("tongnap", p.getTongnap());
                inv.put("vip", p.getVip());
                inv.put("coin", p.coin);
                inv.put("point_event1", p.pointEvent1);
                inv.put("point_event2", p.pointEvent2);
                inv.put("point_tich_tieu", p.point_tich_tieu);
                inv.put("point_hang_dong", p.point_hang_dong);
                inv.put("level_so_tay", p.level_so_tay);
                inv.put("exp_so_tay", p.exp_so_tay);
                inv.put("so_tay_vip", p.so_tay_vip);
                inv.put("active_so_tay", p.active_so_tay);
                inv.put("ruby", p.ruby);
                inv.put("huongnghiep", p.huongnghiep);
                inv.put("diem_danh_event", p.diem_danh_event);
                inv.put("hd_max", p.hd_max);

                ps.setNString(++idx, inv.toJSONString());
            }

            // 5. potential
            {
                JSONObject o = new JSONObject();
                o.put("pts", p.pointAttribute); o.put("p1", p.point1); o.put("p2", p.point2);
                o.put("p3", p.point3); o.put("p4", p.point4); o.put("p5", p.point5);
                o.put("ttPts", p.pointAttributeThongThao);
                o.put("skPts", p.pointSkill);
                JSONArray ops = new JSONArray();
                if (p.list_op_thongthao != null) {
                    for (Option op : p.list_op_thongthao) {
                        if (op != null) {
                            JSONArray pair = new JSONArray(); pair.add(op.id); pair.add(op.getParam()); ops.add(pair);
                        }
                    }
                }
                o.put("ttOps", ops);
                ps.setNString(++idx, o.toJSONString());
            }

            // 6. bag3
            { JSONArray a = new JSONArray();
              if (p.item != null && p.item.bag3 != null) {
                  for (Item_wear it : p.item.bag3) {
                      if (it != null && it.template != null && ItemTemplate3.get_it_by_id(it.template.id) != null) {
                          JSONObject t = Item.it_data_to_json(it);
                          if (t != null && !t.isEmpty()) a.add(t);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 7. it_body
            { JSONArray a = new JSONArray();
              if (p.item != null && p.item.it_body != null) {
                  if (p.item.it_heart != null && p.item.it_heart.template != null && ItemTemplate3.get_it_by_id(p.item.it_heart.template.id) != null) {
                      p.item.it_heart.index = 6;
                      p.item.it_body[6] = p.item.it_heart;
                  } else if (p.item.it_body.length > 6 && p.item.it_body[6] != null && (p.item.it_body[6].template != null && (p.item.it_body[6].template.typeEquip == 6 || p.item.it_body[6].template.id == 11000))) {
                      p.item.it_heart = p.item.it_body[6];
                      p.item.it_heart.index = 6;
                  }
                  for (int i = 0; i < p.item.it_body.length; i++) {
                      Item_wear it = (i == 6 && p.item.it_heart != null) ? p.item.it_heart : p.item.it_body[i];
                      if (it != null && it.template != null && ItemTemplate3.get_it_by_id(it.template.id) != null) {
                          it.index = (short) i;
                          JSONObject t = Item.it_data_to_json(it);
                          if (t != null && !t.isEmpty()) a.add(t);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 8. bag47
            { JSONArray a = new JSONArray();
              if (p.item != null && p.item.bag47 != null) {
                  for (ItemBag47 it : p.item.bag47) {
                      if (it != null && it.quant > 0 && Item.isItemExist(it.category, it.id)) {
                          JSONArray t=new JSONArray();
                          t.add(it.category);
                          t.add(it.id);
                          t.add(it.quant);
                          a.add(t);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 9. box47
            { JSONArray a = new JSONArray();
              if (p.item != null && p.item.box47 != null) {
                  for (ItemBag47 it : p.item.box47) {
                      if (it != null && it.quant > 0 && Item.isItemExist(it.category, it.id)) {
                          JSONArray t=new JSONArray();
                          t.add(it.category);
                          t.add(it.id);
                          t.add(it.quant);
                          a.add(t);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 10. box3
            { JSONArray a = new JSONArray();
              if (p.item != null && p.item.box3 != null) {
                  for (Item_wear it : p.item.box3) {
                      if (it!=null && it.template != null && ItemTemplate3.get_it_by_id(it.template.id) != null) {
                          JSONObject t=Item.it_data_to_json(it);
                          if(t != null && !t.isEmpty()) a.add(t);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 11. save_it3
            { JSONArray a = new JSONArray();
              if (p.item != null && p.item.save_item_wear != null) {
                  for (Item_wear it : p.item.save_item_wear) {
                      if (it != null && it.template != null && ItemTemplate3.get_it_by_id(it.template.id) != null) {
                          JSONObject t=Item.it_data_to_json(it);
                          if(t != null && !t.isEmpty()) a.add(t);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 12. save_it47
            { JSONArray a = new JSONArray();
              if (p.item != null && p.item.save_item_47 != null) {
                  for (ItemBag47 it : p.item.save_item_47) {
                      if (it != null && it.quant > 0 && Item.isItemExist(it.category, it.id)) {
                          JSONArray t=new JSONArray();
                          t.add(it.category);
                          t.add(it.id);
                          t.add(it.quant);
                          a.add(t);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 13. hanhtrinh
            { JSONArray a = new JSONArray();
              if (p.daHanhTrinh != null) {
                  for (ItemBag47 it : p.daHanhTrinh) {
                      if (it != null && it.category >= 0 && it.quant >= 0 && ItemTemplate4.get_it_by_id(it.id) != null) {
                          JSONArray t=new JSONArray();
                          t.add(it.category);
                          t.add(it.id);
                          t.add(it.quant);
                          a.add(t);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 14. rms
            { JSONArray a = new JSONArray();
              if (p.rms != null) {
                  for (byte[] row : p.rms) {
                      if (row == null) { a.add(new JSONArray()); continue; }
                      JSONArray r = new JSONArray(); for (byte b : row) r.add(b); a.add(r);
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 15. skill
            { JSONArray a = new JSONArray();
              if (p.skill_point != null) {
                  for (Skill_info sk : p.skill_point) {
                      if (sk != null && sk.temp != null) {
                          JSONObject o = new JSONObject(); o.put("id", sk.temp.indexSkillInServer); o.put("exp", sk.exp); o.put("lv", sk.lvdevil); o.put("dp", sk.devilpercent); a.add(o);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 16. friend
            { JSONArray a = new JSONArray();
              if (p.friend_list != null) {
                  for (FriendTemp f : p.friend_list) if (f != null) a.add(f.toJSONArray());
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 17. enemy
            { JSONArray a = new JSONArray();
              if (p.enemy_list != null) {
                  for (FriendTemp f : p.enemy_list) if (f != null) a.add(f.toJSONArray());
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 18. fashion
            {
                JSONObject fo = new JSONObject();
                JSONArray fp = new JSONArray();
                if (p.itfashionP != null) {
                    for (ItemFashionP f : p.itfashionP) {
                        if (f != null) {
                            if (f.category == 103 || f.category == 108) {
                                if (ItemHair.get_item(f.id, f.category) == null) continue;
                            }
                            JSONObject o = new JSONObject();
                            o.put("cat", f.category);
                            o.put("id", f.id);
                            o.put("icon", f.icon);
                            o.put("use", f.is_use ? 1 : 0);
                            fp.add(o);
                        }
                    }
                }
                JSONArray f2 = new JSONArray();
                if (p.fashion != null) {
                    for (ItemFashionP2 f : p.fashion) {
                        if (f != null && ItemFashion.get_item(f.id) != null) {
                            JSONObject o = new JSONObject();
                            o.put("id", f.id);
                            o.put("use", f.is_use ? 1 : 0);
                            o.put("lv", f.level);
                            o.put("exp", f.expires);
                            f2.add(o);
                        }
                    }
                }
                JSONArray boats = new JSONArray();
                if (p.itemboat != null) {
                    for (ItemBoatP b : p.itemboat) {
                        if (b != null && ItemBoat.get_item(b.id) != null) {
                            JSONObject o = new JSONObject();
                            o.put("id", b.id);
                            o.put("use", b.is_use ? 1 : 0);
                            boats.add(o);
                        }
                    }
                }
                fo.put("fp", fp); fo.put("f2", f2); fo.put("boat", boats);
                ps.setNString(++idx, fo.toJSONString());
            }

            // 19. eff
            { JSONArray a = new JSONArray();
              if (p.list_eff != null) {
                  for (EffTemplate eff : p.list_eff) {
                      if (eff != null && EffTemplate.check_eff_can_save(eff.id)) { JSONObject o = new JSONObject(); o.put("id", eff.id); o.put("p", eff.param); o.put("t", eff.time - System.currentTimeMillis()); a.add(o); }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 20. quest
            { JSONArray a = new JSONArray();
              if (p.list_quest != null) {
                  for (QuestP qp : p.list_quest) {
                      if (qp != null && qp.template != null && qp.data != null) {
                          JSONArray entry = new JSONArray(); entry.add(qp.template.id);
                          JSONArray rows = new JSONArray();
                          for (short[] row : qp.data) {
                              if (row != null) {
                                  JSONArray cells = new JSONArray();
                                  for (short v : row) cells.add(v);
                                  rows.add(cells);
                              }
                          }
                          entry.add(rows); a.add(entry);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 21. myarchidaily
            { JSONArray a = new JSONArray();
              if (p.archiDaily != null) {
                  for (ArchiDaily ad : p.archiDaily) { if (ad != null) { JSONObject o = new JSONObject(); o.put("id", ad.id); o.put("n", ad.currentNum); o.put("s", ad.state); a.add(o); } }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 22. mypet
            { JSONArray a = new JSONArray();
              if (p.my_pet != null) {
                  for (MyPet pet : p.my_pet) {
                      if (pet != null && pet.template != null) {
                          JSONObject o = new JSONObject(); o.put("slot", pet.id); o.put("tid", pet.template.id); o.put("use", pet.isUse ? 1 : 0); o.put("time", pet.time); a.add(o);
                      }
                  }
              }
              ps.setNString(++idx, a.toJSONString()); }

            // 23. mbv
            { JSONObject o = new JSONObject(); o.put("mabaove", p.mbv != null ? p.mbv : ""); o.put("timeHuyMaBaoVe", p.timeHuyMbv);
              ps.setNString(++idx, o.toJSONString()); }

            // 24. coin
            ps.setInt(++idx, p.coin);

            // 25. exp
            { long expTotal = p.exp;
              if (Level.ENTRYS != null) {
                  for (int i = 0; i < (p.level-1) && i < Level.ENTRYS.length; i++) {
                      if (Level.ENTRYS[i] != null) expTotal += Level.ENTRYS[i].exp;
                  }
              }
              if (Level.LEVEL_THONGTHAO != null) {
                  for (int i = 0; i < p.thongthao && i < Level.LEVEL_THONGTHAO.length; i++) {
                      expTotal += Level.LEVEL_THONGTHAO[i];
                  }
              }
              ps.setLong(++idx, expTotal); }

            // timeResetHangNgay
            LocalDateTime tReset = p.timeresetHangNgay != null ? p.timeresetHangNgay : java.time.LocalDateTime.of(java.time.LocalDate.now().minusDays(1), java.time.LocalTime.MIDNIGHT);
            ps.setNString(++idx, tReset.format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // eff_save
            { JSONArray a = new JSONArray();
              if (p.eff_save != null) {
                  for (int v : p.eff_save) a.add(v);
              }
              ps.setNString(++idx, a.toJSONString()); }

            result = ps.executeUpdate();

            // Save eventData to historys grouped by season key / VARCHAR type
            try {
                if (p.eventData != null) {
                    java.util.Map<String, JSONArray> eventMap = new java.util.HashMap<>();
                    for (EventData ed : p.eventData) {
                        if (ed == null) continue;
                        if ((ed.data == null || ed.data.length == 0) && (ed.points == null || ed.points.isEmpty())) continue;
                        
                        JSONObject o = new JSONObject(); 
                        o.put("ev", ed.eventID);
                        if (ed.key != null) {
                            o.put("k", ed.key);
                        }
                        
                        if (ed.data != null && ed.data.length > 0) {
                            JSONArray d = new JSONArray(); 
                            for (int v : ed.data) d.add(v); 
                            o.put("d", d); 
                        }
                        
                        if (ed.points != null && !ed.points.isEmpty()) {
                            JSONObject pt = new JSONObject();
                            pt.putAll(ed.points);
                            o.put("pt", pt);
                        }

                        String keyType = historys.HistoryManager.formatKey("EVENT_DATA", ed.key);
                        eventMap.computeIfAbsent(keyType, k -> new JSONArray()).add(o);
                    }
                    
                    for (java.util.Map.Entry<String, JSONArray> entry : eventMap.entrySet()) {
                        historys.HistoryManager.saveData(connection, p, entry.getKey(), entry.getValue().toJSONString());
                    }
                }

                // Save archiPrivatePass to historys table
                if (p.archiPrivatePass != null) {
                    achievement.ArchiPrivatePass.savePassData(connection, p, p.currentPassSeason, p.archiPrivatePass);
                }
                // Flush pending picked items to historys
                historys.ItemPickupHistory.flushPlayer(connection, p);
            } catch (Exception ex) {
                System.err.println("Error saving eventData/pickupData to historys: " + ex.getMessage());
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (p.detu != null) { p.detu.map = p.map; flush(p.detu, false); }
        // Evict cache entry on save so subsequent re-login fetches updated data from DB
        database.CacheManager.gI().remove(tableName + "_" + p.name);
        return result;
    }

    public synchronized int flushBotToDb(Player p, Connection connection, boolean print) throws SQLException {
        String query = "UPDATE `players_bot` SET `clazz` = ?, `body` = ?, `level` = ?, `exp` = ?, `potential` = ?, `it_body` = ?, `skill` = ?, `eff` = ?, `fashion` = ?, `site` = ?, `quest` = ? WHERE `id` = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            int idx = 0;
            ps.setByte(++idx, p.clazz);

            // body
            {
                JSONObject o = new JSONObject();
                o.put("head", p.head);
                o.put("hair", p.hair);
                o.put("weapon", p.part_weapon);
                o.put("body", p.part_body);
                o.put("leg", p.part_leg);
                o.put("ring", p.part_ring);
                ps.setString(++idx, o.toJSONString());
            }

            // level
            {
                JSONObject o = new JSONObject();
                o.put("lv", p.level);
                o.put("exp", p.exp);
                o.put("tt", p.thongthao);
                ps.setString(++idx, o.toJSONString());
            }

            // exp
            ps.setLong(++idx, p.exp);

            // potential
            {
                JSONObject o = new JSONObject();
                o.put("attr", p.pointAttribute);
                o.put("p1", p.point1);
                o.put("p2", p.point2);
                o.put("p3", p.point3);
                o.put("p4", p.point4);
                o.put("p5", p.point5);
                o.put("tt", p.pointAttributeThongThao);
                JSONArray ops = new JSONArray();
                if (p.list_op_thongthao != null) {
                    for (Option op : p.list_op_thongthao) {
                        JSONObject jo = new JSONObject();
                        jo.put("id", op.id);
                        jo.put("param", op.getParam());
                        ops.add(jo);
                    }
                }
                o.put("op_tt", ops);
                ps.setString(++idx, o.toJSONString());
            }

            // it_body
            {
                JSONArray js = new JSONArray();
                if (p.item != null && p.item.it_body != null) {
                    if (p.item.it_heart != null && p.item.it_heart.template != null && ItemTemplate3.get_it_by_id(p.item.it_heart.template.id) != null) {
                        p.item.it_heart.index = 6;
                        p.item.it_body[6] = p.item.it_heart;
                    } else if (p.item.it_body.length > 6 && p.item.it_body[6] != null && (p.item.it_body[6].template != null && (p.item.it_body[6].template.typeEquip == 6 || p.item.it_body[6].template.id == 11000))) {
                        p.item.it_heart = p.item.it_body[6];
                        p.item.it_heart.index = 6;
                    }
                    for (int i = 0; i < p.item.it_body.length; i++) {
                        Item_wear it = (i == 6 && p.item.it_heart != null) ? p.item.it_heart : p.item.it_body[i];
                        if (it != null && it.template != null && ItemTemplate3.get_it_by_id(it.template.id) != null) {
                            it.index = (short) i;
                            JSONObject js_temp = Item.it_data_to_json(it);
                            if (js_temp != null && !js_temp.isEmpty()) js.add(js_temp);
                        }
                    }
                }
                ps.setString(++idx, js.toJSONString());
            }

            // skill
            {
                JSONArray js = new JSONArray();
                if (p.skill_point != null) {
                    for (Skill_info sk : p.skill_point) {
                        if (sk != null && sk.temp != null) {
                            JSONObject o = new JSONObject();
                            o.put("id", sk.temp.indexSkillInServer);
                            o.put("exp", sk.exp);
                            o.put("lv", sk.lvdevil);
                            o.put("pct", sk.devilpercent);
                            js.add(o);
                        }
                    }
                }
                ps.setString(++idx, js.toJSONString());
            }

            // eff
            {
                JSONArray js = new JSONArray();
                if (p.list_eff != null) {
                    for (EffTemplate eff_temp : p.list_eff) {
                        if (eff_temp != null && EffTemplate.check_eff_can_save(eff_temp.id)) {
                            JSONObject o = new JSONObject();
                            o.put("id", eff_temp.id);
                            o.put("param", eff_temp.param);
                            o.put("time", eff_temp.time - System.currentTimeMillis());
                            js.add(o);
                        }
                    }
                }
                ps.setString(++idx, js.toJSONString());
            }

            // fashion
            {
                JSONObject fo = new JSONObject();
                JSONArray fp = new JSONArray();
                if (p.itfashionP != null) {
                    for (ItemFashionP f : p.itfashionP) {
                        if (f != null) {
                            if (f.category == 103 || f.category == 108) {
                                if (ItemHair.get_item(f.id, f.category) == null) continue;
                            }
                            JSONObject o = new JSONObject();
                            o.put("cat", f.category);
                            o.put("id", f.id);
                            o.put("icon", f.icon);
                            o.put("use", f.is_use ? 1 : 0);
                            fp.add(o);
                        }
                    }
                }
                JSONArray f2 = new JSONArray();
                if (p.fashion != null) {
                    for (ItemFashionP2 f : p.fashion) {
                        if (f != null && ItemFashion.get_item(f.id) != null) {
                            JSONObject o = new JSONObject();
                            o.put("id", f.id);
                            o.put("use", f.is_use ? 1 : 0);
                            o.put("lv", f.level);
                            o.put("exp", f.expires);
                            f2.add(o);
                        }
                    }
                }
                JSONArray boats = new JSONArray();
                if (p.itemboat != null) {
                    for (ItemBoatP b : p.itemboat) {
                        if (b != null && ItemBoat.get_item(b.id) != null) {
                            JSONObject o = new JSONObject();
                            o.put("id", b.id);
                            o.put("use", b.is_use ? 1 : 0);
                            boats.add(o);
                        }
                    }
                }
                fo.put("fp", fp);
                fo.put("f2", f2);
                fo.put("boat", boats);
                ps.setString(++idx, fo.toJSONString());
            }

            // site
            {
                JSONObject o = new JSONObject();
                o.put("map", p.map != null ? p.map.template.id : 1);
                o.put("zone", p.map != null ? p.map.zone_id : 0);
                o.put("hp", p.hp);
                o.put("mp", p.mp);
                o.put("x", p.x);
                o.put("y", p.y);
                o.put("hat", p.is_show_hat);
                o.put("pk", p.pointPk);
                o.put("wpn", p.is_show_weapon);
                o.put("hfhair", p.is_hide_fashion_hair);
                o.put("hfhead", p.is_hide_fashion_head);
                ps.setString(++idx, o.toJSONString());
            }

            // quest
            {
                JSONArray a = new JSONArray();
                if (p.list_quest != null) {
                    for (QuestP qp : p.list_quest) {
                        if (qp != null && qp.template != null) {
                            JSONArray entry = new JSONArray(); entry.add(qp.template.id);
                            JSONArray rows = new JSONArray();
                            if (qp.data != null) {
                                for (short[] row : qp.data) { JSONArray cells=new JSONArray(); for (short v:row) cells.add(v); rows.add(cells); }
                            }
                            entry.add(rows); a.add(entry);
                        }
                    }
                }
                ps.setString(++idx, a.toJSONString());
            }

            // WHERE id
            ps.setInt(++idx, p.IDPlayer);

            int res = ps.executeUpdate();
            if (print) {
                // System.out.println("Saved bot " + p.name + " to database table players_bot.");
            }
            return res;
        }
    }

    public QuestP getMainQuest() {
        if (this.list_quest != null) {
            for (QuestP q : this.list_quest) {
                if (q != null && q.template != null && q.template.typeMainSub == 0 && q.template.id >= 0) {
                    return q;
                }
            }
        }
        return null;
    }

    public int getMaxUnlockedMapId() {
        if (core.Manager.gI().isTestMode()) {
            return 198;
        }
        int maxMapId = 9; 
        if (this.list_quest == null || this.list_quest.isEmpty()) {
            return 198;
        }
        QuestP quest_select = this.getMainQuest();
        if (quest_select != null && quest_select.template != null) {
            int qId = quest_select.template.id;
            for (int i = 0; i < MapCanGoTo.idQuest.length; i++) {
                if (MapCanGoTo.idQuest[i] > qId) {
                    if (i > 0) {
                        maxMapId = MapCanGoTo.idMap[i - 1];
                    }
                    break;
                }
            }
        } else {
            maxMapId = 198;
        }
        return maxMapId;
    }

    public void goto_map(Vgo vgo) throws IOException {
        this.resetMenuState();
        this.targetFight = null;
        this.pvp_target = null;
        if (this.isdie) {
            this.ischangemap = false;
            this.getService().send_box_ThongBao_OK("Bạn đang kiệt sức, hãy hồi sinh trước khi đi tiếp!");
            this.time_change_map = System.currentTimeMillis() + 2000L;
            return;
        }
        if (this.hp <= 0) {
            this.hp = (this.ability != null) ? Math.max(100, this.ability.get_hp_max(true) / 10) : 100;
        }
        this.ischangemap = false;
        this.xold = this.x;
        this.yold = this.y;
        Zone[] map_go = vgo.map_go;
        if (map_go == null || map_go.length == 0 || map_go[0] == null || map_go[0].template == null) {
            this.ischangemap = true;
            this.getService().send_box_ThongBao_OK("Chưa thể đi đến map này!");
            return;
        }
        short xnew = vgo.xnew;
        short ynew = vgo.ynew;
        if (this.map != null && this.map.template != null && map_go[0].template.id == 111 && this.map.template.id == 110) {
            if (this.party != null) {
                if (!this.party.list.get(0).name.equals(this.name)) {
                    this.getService().send_box_ThongBao_OK("Chỉ tổ đội trưởng mới có quyền bắt đầu vượt thác!");
                    return;
                }
                if (this.tableTickOption == null) {
                    this.tableTickOption = new functions.VuotThacLenTroiTick(this);
                    this.tableTickOption.listP = new java.util.ArrayList<>();
                    this.tableTickOption.listP.add(this);
                    for (int i = 0; i < this.party.list.size(); i++) {
                        Player p0 = Zone.get_player_by_name_allmap(this.party.list.get(i).name);
                        if (p0 != null && p0.index_map != this.index_map && p0.map != null && p0.map.equals(this.map)) {
                            this.tableTickOption.listP.add(p0);
                        }
                    }
                    this.tableTickOption.list_check = new byte[this.tableTickOption.listP.size()];
                    this.tableTickOption.list_check[0] = 1;
                    for (int i = 1; i < this.tableTickOption.list_check.length; i++) {
                        this.tableTickOption.list_check[i] = 0;
                    }
                    this.tableTickOption.show("Vượt thác lên trời");
                }
                return;
            } else {
                Zone[] redirect_map = Zone.getMapByID(109);
                if (redirect_map != null && redirect_map.length > 0) {
                    map_go = redirect_map;
                    xnew = 50;
                    ynew = 670;
                }
            }
        }

        if (!this.isPassGateCheck && this.map != null && this.map.template != null && ChiemDao.isMapHaveGate(this.map.template.id)
                && ((map_go[0].template.id >= 254 && map_go[0].template.id <= 258) || (map_go[0].template.id >= 261 && map_go[0].template.id <= 265))) {
            // Mở menu ChiemDaoMenu thay vì auto vào — player xem thông tin + chọn vào
            try {
                activities.ChiemDaoMenu.openMenu(this, this.map.template.id);
            } catch (Exception ignored) {}
            return;
        }

        BaoVePhaoDai bvd = (this.clan != null && this.clan.baoVePhaoDai != null) ? this.clan.baoVePhaoDai
                : (this.map != null && this.map.baoVePhaoDai != null ? this.map.baoVePhaoDai
                : (this.dungeon instanceof BaoVePhaoDai ? (BaoVePhaoDai) this.dungeon : null));
        if (bvd != null && map_go[0].IsMapBaoVePhaoDai()) {
            int id_map = map_go[0].template.id;
            map_go = new Zone[1];
            for (int i = 0; i < bvd.maps.size(); i++) {
                if (id_map == bvd.maps.get(i).template.id) {
                    map_go[0] = bvd.maps.get(i);
                    break;
                }
            }
            if (this.map != null && this.map.template != null && (this.map.template.id == 267 || this.map.template.id == 271) && this.get_eff(52) == null && this.get_eff(50) != null) {
                this.getService().send_box_ThongBao_OK("Đang trong thời gian hồi sinh");
                return;
            }
            boolean isTeamA = (this.clan != null && this.clan.equals(bvd.clanA)) || this.type_pk == 4;
            boolean isTeamB = (this.clan != null && this.clan.equals(bvd.clanB)) || this.type_pk == 5;
            if ((id_map == 267 && !isTeamA) || (id_map == 271 && !isTeamB)) {
                return;
            }
        }

        if (this.dungeon != null && this.dungeon.maps != null) {
            int id_map = map_go[0].template.id;
            for (int i = 0; i < this.dungeon.maps.size(); i++) {
                Zone z = this.dungeon.maps.get(i);
                if (z != null && z.template != null && id_map == z.template.id) {
                    map_go = new Zone[]{z};
                    break;
                }
            }
            switch (map_go[0].template.id) {
                case 168: {
                    xnew = 1490;
                    ynew = 260;
                    break;
                }
                case 169: {
                    xnew = 760;
                    ynew = 240;
                    break;
                }
                case 170: {
                    xnew = 100;
                    ynew = 245;
                    break;
                }
                case 171: {
                    xnew = 675;
                    ynew = 270;
                    break;
                }
                case 172: {
                    xnew = 113;
                    ynew = 240;
                    break;
                }
                case 173: {
                    xnew = 135;
                    ynew = 255;
                    break;
                }
                case 174: {
                    xnew = 121;
                    ynew = 225;
                    break;
                }
                case 175: {
                    xnew = 156;
                    ynew = 255;
                    break;
                }
                case 176: {
                    xnew = 190;
                    ynew = 245;
                    break;
                }
            }
        }
        //
        if (Zone.is_map_boss(map_go[0].template.id)) {
            int targetMapId = map_go[0].template.id;
            int reqQuest = MapCanGoTo.getRequiredQuestForBossMap(targetMapId);
            QuestP mq = this.getMainQuest();
            int curQId = (mq != null && mq.template != null) ? mq.template.id : 0;
            if (!core.Manager.gI().isTestMode() && curQId < reqQuest) {
                this.ischangemap = false;
                this.wait_change_map = false;
                this.time_change_map = System.currentTimeMillis() + 2000L;
                this.getService().send_box_ThongBao_OK("Bạn chưa đạt cấp độ nhiệm vụ chính tuyến (" + reqQuest + ") để vào map Săn Boss!");
                return;
            }
            // Auto advance statusQuest = 0 to 1 for boss main quest
            if (mq != null && mq.template != null && mq.template.statusQuest == 0
                    && MapCanGoTo.isDoingBossMainQuest(targetMapId, mq.template.id)) {
                try {
                    Quest.remove_old_and_send_next(this, mq);
                    mq = this.getMainQuest();
                    curQId = (mq != null && mq.template != null) ? mq.template.id : 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            boolean isDoingQuest = MapCanGoTo.isDoingBossMainQuest(targetMapId, curQId);
            if (map_boss_info == null) {
                map_boss_info = new MapBossInfo();
            }
            map_boss_info.map = map_go[0];
            map_boss_info.x_new = xnew;
            map_boss_info.y_new = ynew;
            String dialogMsg;
            if (isDoingQuest) {
                dialogMsg = map_go[0].template.name + " rất nguy hiểm. Bạn đang thực hiện nhiệm vụ chính tuyến, bạn có muốn vào khiêu chiến ngay không?";
            } else {
                dialogMsg = map_go[0].template.name + " rất nguy hiểm và phải mất 5 bánh mì để vô, bạn có thật sự muốn đi một mình?";
            }
            this.setyesNoDialog(new model.YesNoDialog(this, 16, "Thông báo",
                    dialogMsg,
                    new String[]{"Đồng ý", "Hủy"}, new byte[]{-1, -1}));
            this.getService().startYesNo();
        } else {
            int curMapId = (this.map != null && this.map.template != null) ? this.map.template.id : -1;
            int targetMapId = map_go[0].template.id;
            boolean sameVillage = (curMapId >= 0 && core.MenuController.isSameVillage(curMapId, targetMapId));
            int mapIDcanGo = checkQuest();
            if (!sameVillage && !Zone.isMapNoQuestLimit(targetMapId) && targetMapId > mapIDcanGo) {
                this.getService().send_box_ThongBao_OK("Hãy hoàn thành hết nhiệm vụ trước khi đến làng tiếp theo!");
                this.ischangemap = false;
                this.wait_change_map = false;
                this.time_change_map = System.currentTimeMillis() + 2000L;
                return;
            }
            if (this.hp > 0 && this.ship_pet != null && this.map != null && this.map.equals(this.ship_pet.map)
                    && Math.abs(this.x - this.ship_pet.x) < 200
                    && Math.abs(this.y - this.ship_pet.y) < 200) {
                this.ship_pet.map = null;
            }
            this.wait_change_map = true;
            Message m = new Message(30);
            this.addmsg(m);
            m.cleanup();
            if (this.map != null) {
                this.map.leave_map(this, 2);
            }
            int zone_into = 0;
            while (zone_into < (map_go.length - 1)
                    && map_go[zone_into].getNumPlayerSlot() >= map_go[zone_into].template.max_player) {
                zone_into++;
            }
            ///
            boolean send_boat = false;
            for (int i = 0; i < DataTemplate.mSea.length; i++) {
                if (DataTemplate.mSea[i][0] == map_go[zone_into].template.id
                        && (this.map == null || this.map.template == null || DataTemplate.mSea[i][1] != this.map.template.id)) {
                    send_boat = true;
                    break;
                }
            }
            if (zone_into >= map_go.length) {
                zone_into = map_go.length - 1;
            }
            this.map = map_go[zone_into];
            this.x = xnew;
            this.y = ynew;
            this.xold = this.x;
            this.yold = this.y;
            this.lastValidX = this.x;  // reset về tọa độ map mới
            this.lastValidY = this.y;
            this.lastMovePacketTime = System.currentTimeMillis();
            this.ischangemap = false;
            this.spawnX = this.x;
            this.spawnY = this.y;
            this.hasMovedFromSpawn = false;
            this.time_change_map = System.currentTimeMillis() + 1500L;
            this.map.goto_map(this);
            //
            this.getService().update_PK(this, true);
            this.getService().pet(this, true);
            this.map.send_boat(this, send_boat);
            Quest.update_map_have_side_quest(this, true);
            this.map.update_boat(this, this, true);
            if (this.id_danh_hieu_su_dung > 0) {
                activities.DanhHieu dhSelf = activities.DanhHieu.get_Id(this.id_danh_hieu_su_dung);
                if (dhSelf != null && dhSelf.idEff > 0) {
                    try {
                        Message mDh = new Message(74);
                        mDh.writer().writeByte(1);
                        mDh.writer().writeShort(this.index_map);
                        mDh.writer().writeShort(dhSelf.idEff);
                        mDh.writer().writeInt(-1);
                        mDh.writer().writeByte(0);
                        mDh.writer().writeByte(0);
                        this.addmsg(mDh);
                        mDh.cleanup();
                    } catch (Exception ignored) {}
                }
            }

            if (this.dungeon != null && Zone.is_map_dungeon(this.map.template.id)) {
                this.getService().send_time_cool_down(this.dungeon.time, "Thời gian", 0);
            }

            // 1. Thủ Lĩnh Biển Khơi (Map 178..184)
            if (this.map != null && (this.map.template.id >= 178 && this.map.template.id <= 184)) {
                int mid = this.map.template.id;
                if (mid == 179 || mid == 180 || mid == 184) {
                    if (this.clan != null && this.clan.map_create != null && this.clan.map_create.map_thuLinhBienKhoi != null) {
                        this.type_pk = (byte) this.clan.map_create.map_thuLinhBienKhoi.flag;
                    } else if (this.type_pk >= 4 && this.type_pk <= 7) {
                        // Giữ nguyên cờ đã gán
                    } else {
                        int hash = Math.abs(this.name != null ? this.name.hashCode() : (int) this.IDPlayer);
                        this.type_pk = (byte) (4 + (hash % 4));
                    }
                } else {
                    this.type_pk = -1; // Căn cứ an toàn 4 biển (178, 181, 182, 183)
                }
                this.getService().update_PK(this, this.type_pk != -1);
                ThuLinhBienKhoi.sendInfo(this);
            }
            // 2. Chiếm Đảo Bang Hội (Map 261..265 hoặc map_dungeon instanceof ChiemDao)
            else if (this.map != null && ((this.map.template.id >= 261 && this.map.template.id <= 265) || this.map.map_dungeon instanceof ChiemDao)) {
                clan.Clan topClan = ChiemDao.getClanTop(this.map.template.id);
                if (this.map.map_dungeon instanceof ChiemDao) {
                    ChiemDao cd = (ChiemDao) this.map.map_dungeon;
                    if (cd.occupyingClan != null) topClan = cd.occupyingClan;
                }
                boolean isDefender = (topClan != null && this.clan != null && (this.clan.id == topClan.id || (this.clan.name != null && this.clan.name.equalsIgnoreCase(topClan.name))));
                this.type_pk = (byte) (isDefender ? 5 : 4);
                this.getService().update_PK(this, true);
            }
            // 3. Phó bản PvP Băng (Map 120 / pvpBangMapFight)
            else if (this.map != null && (this.map.pvpBangMapFight != null || (this.map.template != null && this.map.template.id == 120))) {
                if (this.map.pvpBangMapFight != null) {
                    boolean isClan1 = this.clan != null && this.clan.equals(this.map.pvpBangMapFight.clan1);
                    this.type_pk = (byte) (isClan1 ? 4 : 5);
                    this.getService().update_PK(this, true);
                }
            }
            // 4. Bảo Vệ Pháo Đài (Map 267..271 / baoVePhaoDai)
            else if (this.map != null && (this.map.baoVePhaoDai != null || this.map.IsMapBaoVePhaoDai())) {
                if (this.map.baoVePhaoDai != null) {
                    boolean isClanA = this.clan != null && this.clan.equals(this.map.baoVePhaoDai.clanA);
                    this.type_pk = (byte) (isClanA ? 4 : 5);
                    this.getService().update_PK(this, true);
                }
            }
            // 5. Trận Chiến Khổng Lồ (Map 81 / map_little_garden)
            else if (this.map != null && (this.map.map_little_garden != null || this.map.template.id == 81)) {
                if (this.map.map_little_garden != null) {
                    boolean isClan1 = this.clan != null && this.clan.equals(this.map.map_little_garden.clan1);
                    this.type_pk = (byte) (isClan1 ? 4 : 5);
                    this.getService().update_PK(this, true);
                }
            }

            if (this.map != null && this.map.map_little_garden == null) {
                WorldWar.setType(this);
            }
        }
    }

    public void change_map(Vgo vgo) throws IOException {
        if (this.isdie) {
            this.ischangemap = false;
            this.getService().send_box_ThongBao_OK("Bạn đang kiệt sức, hãy hồi sinh trước khi đi tiếp!");
            this.time_change_map = System.currentTimeMillis() + 2000L;
            return;
        }
        if (this.hp <= 0) {
            this.hp = (this.ability != null) ? Math.max(100, this.ability.get_hp_max(true) / 10) : 100;
        }
        this.ischangemap = false;
        this.xold = this.x;
        this.yold = this.y;
        Zone[] map_go = vgo.map_go;
        if (map_go == null || map_go.length == 0 || map_go[0] == null || map_go[0].template == null) {
            this.getService().send_box_ThongBao_OK("Chưa thể đi đến map này!");
            return;
        }
        int currentMapId = (this.map != null && this.map.template != null) ? this.map.template.id : -1;
        int destMapId = map_go[0].template.id;
        boolean sameVillage = (currentMapId >= 0 && core.MenuController.isSameVillage(currentMapId, destMapId));
        int mapIDcanGo = checkQuest();
        if (Zone.isMapNoQuestLimit(destMapId) || sameVillage) {
            mapIDcanGo = 9999;
        }
        if (destMapId > mapIDcanGo) {
            this.getService().send_box_ThongBao_OK("Hãy hoàn thành hết nhiệm vụ trước khi đến làng tiếp theo!");
            return;
        }
        if (!core.Manager.gI().isTestMode() && currentMapId > 0 && !MapCanGoTo.isVillageMap(currentMapId) 
                && !Zone.isMapNoQuestLimit(destMapId) && !Zone.isMapNoQuestLimit(currentMapId)) {
            if (destMapId > currentMapId && !MapCanGoTo.isVillageMap(destMapId) && MapCanGoTo.isMapHaveRepeatQuest(currentMapId)) {
                if (!this.hasCompletedRepeatQuestOfMap(currentMapId)) {
                    this.getService().send_box_ThongBao_OK("Bạn cần hoàn thành nhiệm vụ lặp ở map " + this.map.template.name + " ít nhất 1 lần mới có thể mở map tiếp theo!");
                    return;
                }
            }
        }
        //
        Message m = new Message(30);
        this.addmsg(m);
        m.cleanup();
//        System.out.println("send msg 30");
        if (this.map != null) {
            this.map.leave_map(this, 2);
        }
        int zone_into = 0;
        while (zone_into < (map_go[zone_into].template.max_zone - 1)) {
            boolean isFull = map_go[zone_into].getNumPlayerSlot() >= map_go[zone_into].template.max_player;
            zabstracts.AbsBoss sBoss = boss.SuperBossManager.getActiveSuperBossInZone(map_go[zone_into]);
            boolean bossRestricted = (sBoss != null && !boss.SuperBossManager.checkLevelRequirement(this, sBoss));
            if (!isFull && !bossRestricted) {
                break;
            }
            zone_into++;
        }
        this.map = map_go[zone_into];
        this.x = (short) vgo.xnew;
        this.y = (short) vgo.ynew;
        this.xold = this.x;
        this.yold = this.y;
        this.lastValidX = this.x;  // reset về tọa độ map mới
        this.lastValidY = this.y;
        this.lastMovePacketTime = System.currentTimeMillis();
        this.spawnX = this.x;
        this.spawnY = this.y;
        this.hasMovedFromSpawn = false;
        this.time_change_map = System.currentTimeMillis() + 1500L;
        this.map.goto_map(this);
        this.getService().update_PK(this, true);
        this.getService().pet(this, true);
        Quest.update_map_have_side_quest(this, true);
        this.map.update_boat(this, this, true);
        if (this.map != null && this.map.map_little_garden == null) {
            WorldWar.setType(this);
        }
        if (this.ability != null) {
            this.ability.recalculatePlayerStats(this);
        }
    }

    public void update_exp(long exp_up, boolean multi) {
        if (get_eff(8) != null) {
            return;
        }
        if (multi) {
            exp_up *= Manager.gI().exp;
            // [TÍCH HỢP OPTION 33 & 67: Tăng xp đánh quái từ trang bị]
            if (this.ability != null) {
                int xpBonusPct = this.ability.total_param_item(33, true) + this.ability.total_param_item(67, true);
                if (xpBonusPct > 0) {
                    exp_up += (exp_up * (long) xpBonusPct) / 1000L;
                }
            }
            int vipBonus = core.VipManager.getVipExpBonusPercent(getVip());
            if (vipBonus > 0) {
                exp_up += (exp_up * vipBonus) / 100;
            }
            if (this.clan != null && this.clan.buff != null) {
                long nowMs = System.currentTimeMillis();
                for (template.EffTemplate b : this.clan.buff) {
                    if (b != null && b.time > nowMs) {
                        if (b.id == 0 || b.id == 1 || b.id == 4) {
                            exp_up += (exp_up * b.param) / 100;
                        }
                    }
                }
            }
        }

        // Cân bằng bot: Không cho phép bot vượt quá Top 30 người chơi thật
        if (this.isBot) {
            int maxBotLevel = bot.botplayer.GameAnalyzer.getTop30RealPlayerLevelCap();
            if (this.level >= maxBotLevel) {
                if (this.level < 100 && (this.exp + exp_up) >= Level.ENTRYS[this.level - 1].exp) {
                    this.exp = Level.ENTRYS[this.level - 1].exp - 1;
                    return;
                }
            }
        }

        if (this.level >= 100 && this.thongthao >= 99 && (this.exp + exp_up) >= Level.LEVEL_THONGTHAO[this.thongthao]) {

            return;
        }
        this.exp += exp_up;
        //
        if (this.level < 100 && this.exp >= Level.ENTRYS[this.level - 1].exp) {
            while (this.level < 100 && this.exp >= Level.ENTRYS[this.level - 1].exp) {
                this.exp -= Level.ENTRYS[this.level - 1].exp;
                this.level++;
                if (this.level < 100) {
                    this.pointAttribute += Level.ENTRYS[this.level - 1].tiemnang;
                } else {
                    this.pointAttribute += Level.ENTRYS[this.level - 2].tiemnang;
                }
            }
            try {
                this.getService().send_eff(0, 0);
                this.update_info_to_all();
                this.updateMoney();
                this.getService().CountDown_Ticket();
            } catch (Exception ignored) {}
        }
        if (this.level > 100) {
            this.level = 100;
            try { this.update_info_to_all(); } catch (Exception ignored) {}
        }
        if (level >= 100) {
            if (this.exp >= Level.LEVEL_THONGTHAO[this.thongthao]) {
                while (this.exp >= Level.LEVEL_THONGTHAO[this.thongthao]) {
                    this.exp -= Level.LEVEL_THONGTHAO[this.thongthao];
                    this.thongthao++;
                    this.pointAttributeThongThao++;
                }
                try {
                    this.getService().send_eff(0, 0);
                    this.update_info_to_all();
                    this.updateMoney();
                    this.getService().CountDown_Ticket();
                } catch (Exception ignored) {}
            }
        }
        if (this.thongthao > 99) {
            this.thongthao = 99;
            this.exp = Level.LEVEL_THONGTHAO[this.thongthao] - 1;
            try { this.update_info_to_all(); } catch (Exception ignored) {}
        }
        try {
            Message m = new Message(10);
            m.writer().writeShort(this.index_map);
            m.writer().writeShort(get_level_percent());
            m.writer().writeInt((int) exp_up);
            this.addmsg(m); // fix: dùng this.addmsg — bot override an toàn, không cần conn
            m.cleanup();
        } catch (Exception ignored) {}
    }

    public void request_live_from_die(Message m2) throws IOException {
        if (this.map != null && this.map.map_vp != null) {
            return;
        }
        byte type = m2.reader().readByte();
        if (this.map != null && (this.map.template.id == 81 || this.map.map_little_garden != null)) {
            if (type == 1) { // Hồi sinh tại chỗ
                if (pointPk < 20) {
                    this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                            ("Hồi sinh tại chỗ mất 500 beri, bạn có muốn hồi sinh không?"),
                            new String[]{"500", "Hủy"}, new byte[]{6, -1}));
                    this.getService().startYesNo();
                } else {
                    int fee = pointPk / 4;
                    this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                            ("Hồi sinh tại chỗ mất " + fee + " ruby, bạn có muốn hồi sinh không?"),
                            new String[]{"" + fee, "Hủy"}, new byte[]{7, -1}));
                    this.getService().startYesNo();
                }
            } else { // Về vùng an toàn / Căn cứ phe trong Phó Bản Khổng Lồ (Map 81)
                this.isdie = false;
                this.time_hs_little_garden = 0;
                if (this.ability != null) {
                    this.hp = this.ability.get_hp_max(true);
                    this.mp = this.ability.get_mp_max(true);
                }
                short spawnX = (short) (this.type_pk == 4 ? 350 : 1400);
                short spawnY = 260;
                this.x = spawnX;
                this.y = spawnY;
                this.xold = spawnX;
                this.yold = spawnY;
                try {
                    Vgo vgo = new Vgo();
                    vgo.map_go = new Zone[]{this.map};
                    vgo.xnew = spawnX;
                    vgo.ynew = spawnY;
                    this.goto_map(vgo);
                    if (this.getService() != null) {
                        this.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                        this.getService().use_potion(0, this.hp);
                        this.getService().use_potion(1, this.mp);
                        this.getService().update_PK(this, true);
                    }
                    this.map.change_flag(this, this.type_pk);
                } catch (Exception ignored) {}
                this.time_can_mob_atk = System.currentTimeMillis() + 1500L;
            }
            return;
        }
        if (this.map != null && (this.map.IsMapBaoVePhaoDai() || this.map.baoVePhaoDai != null)) {
            if (type == 1) { // Hồi sinh tại chỗ
                if (pointPk < 20) {
                    this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                            ("Hồi sinh tại chỗ mất 500 beri, bạn có muốn hồi sinh không?"),
                            new String[]{"500", "Hủy"}, new byte[]{6, -1}));
                    this.getService().startYesNo();
                } else {
                    int fee = pointPk / 4;
                    this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                            ("Hồi sinh tại chỗ mất " + fee + " ruby, bạn có muốn hồi sinh không?"),
                            new String[]{"" + fee, "Hủy"}, new byte[]{7, -1}));
                    this.getService().startYesNo();
                }
            } else { // Về làng của phe (Làng Đỏ - Map 267 hoặc Làng Xanh - Map 271)
                respawnBaoVePhaoDai();
            }
            return;
        }
        if (this.map != null && this.map.template != null && TranChienLon.isMapTranChienLon(this.map.template.id)) {
            if (type == 1) { // Hồi sinh tại chỗ
                if (pointPk < 20) {
                    this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                            ("Hồi sinh tại chỗ mất 500 beri, bạn có muốn hồi sinh không?"),
                            new String[]{"500", "Hủy"}, new byte[]{6, -1}));
                    this.getService().startYesNo();
                } else {
                    int fee = pointPk / 4;
                    this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                            ("Hồi sinh tại chỗ mất " + fee + " ruby, bạn có muốn hồi sinh không?"),
                            new String[]{"" + fee, "Hủy"}, new byte[]{7, -1}));
                    this.getService().startYesNo();
                }
            } else { // Về căn cứ phe trong Trận Chiến Lớn
                respawnTranChienLon();
            }
            return;
        }
        if (this.map != null && this.map.template != null && (this.map.template.id == 70 || this.map.template.id == 71 || this.map.template.id == 72 || this.map.template.id == 74)) {
            if (type == 1) { // Hồi sinh tại chỗ
                if (pointPk < 20) {
                    this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                            ("Hồi sinh tại chỗ mất 500 beri, bạn có muốn hồi sinh không?"),
                            new String[]{"500", "Hủy"}, new byte[]{6, -1}));
                    this.getService().startYesNo();
                } else {
                    int fee = pointPk / 4;
                    this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                            ("Hồi sinh tại chỗ mất " + fee + " ruby, bạn có muốn hồi sinh không?"),
                            new String[]{"" + fee, "Hủy"}, new byte[]{7, -1}));
                    this.getService().startYesNo();
                }
            } else { // Về Thị Trấn Whiskey
                respawnDauTruongTuDo();
            }
            return;
        }
        if (type == 1) { //
            if (WorldWar.runnning && this.map != null && !WorldWar.mapTranChienLon(this.map.template.id)) {
                conn.p.getService().send_box_ThongBao_OK("Không thể thực hiện thao tác này khi đang diễn ra lễ hội");
                return;
            }
            if (pointPk < 20) {
                this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                        ("Hồi sinh tại chỗ mất 500 beri, bạn có muốn hồi sinh không?"),
                        new String[]{"500", "Hủy"}, new byte[]{6, -1}));
                this.getService().startYesNo();
            } else {
                int fee = pointPk / 4;
                this.setyesNoDialog(new model.YesNoDialog(this, 14, "Thông báo",
                        ("Hồi sinh tại chỗ mất " + fee + " ruby, bạn có muốn hồi sinh không?"),
                        new String[]{"" + fee, "Hủy"}, new byte[]{7, -1}));
                this.getService().startYesNo();
            }
        } else { // ve lang
            if (this.isdie) {
                if (this.map != null && this.map.template != null && ((this.map.template.id >= 254 && this.map.template.id <= 258) || (this.map.template.id >= 261 && this.map.template.id <= 265))) {
                    respawnChiemDao();
                    return;
                }
                if (this.map != null && (this.map.template.id >= 178 && this.map.template.id <= 184)) {
                    if (this.clan != null && this.clan.map_create != null) {
                        this.isdie = false;
                        this.hp = this.ability.get_hp_max(true);
                        this.mp = this.ability.get_mp_max(true);
                        this.type_pk = -1;
                        Vgo vgo = new Vgo();
                        vgo.map_go = new Zone[]{this.clan.map_create};
                        vgo.xnew = 100;
                        vgo.ynew = 230;
                        this.goto_map(vgo);
                        if (this.getService() != null) {
                            this.getService().update_PK(this, false);
                            this.getService().use_potion(0, this.hp);
                            this.getService().use_potion(1, this.mp);
                        }
                        return;
                    }
                }
                this.isdie = false;
                int currentMapId = (this.map != null && this.map.template != null) ? this.map.template.id : (this.id_map_save > 0 ? this.id_map_save : 1);
                int returnVillage = Zone.getVillageMapId(currentMapId);
                if (returnVillage <= 0) returnVillage = (this.id_map_save > 0) ? this.id_map_save : 1;
                this.id_map_save = returnVillage;

                Vgo vgo = new Vgo();
                vgo.map_go = Zone.getMapByID(returnVillage);
                if (vgo.map_go == null || vgo.map_go.length == 0 || vgo.map_go[0] == null) {
                    vgo.map_go = Zone.getMapByID(1);
                }
                vgo.xnew = 300;
                vgo.ynew = 300;
                if (vgo.map_go != null && vgo.map_go.length > 0 && vgo.map_go[0] != null && vgo.map_go[0].template != null && vgo.map_go[0].template.npcs != null) {
                    for (int i = 0; i < vgo.map_go[0].template.npcs.size(); i++) {
                        Npc npc_temp = vgo.map_go[0].template.npcs.get(i);
                        if (npc_temp != null && "Bản đồ".equals(npc_temp.namegt)) {
                            vgo.xnew = npc_temp.x;
                            if (npc_temp.y < 250) {
                                vgo.ynew = (short) (npc_temp.y + 20);
                            } else {
                                vgo.ynew = (short) (npc_temp.y - 40);
                            }
                            break;
                        }
                    }
                }
                int hp_after_ = (this.ability != null) ? this.ability.get_hp_max(true) / 10 : 100;
                if (hp_after_ < 1) hp_after_ = 100;
                this.hp = hp_after_;
                this.goto_map(vgo);
                if (this.getService() != null) {
                    this.getService().use_potion(0, hp_after_);
                }
                this.time_can_mob_atk = System.currentTimeMillis() + 1200L;
            }
        }
    }

    public void respawnChiemDao() {
        try {
            if (this.map == null || this.map.template == null) return;
            this.isdie = false;
            this.type_pk = -1;
            this.time_hs_little_garden = 0;
            if (this.ability != null) {
                this.hp = this.ability.get_hp_max(true);
                this.mp = this.ability.get_mp_max(true);
            }
            if (this.getService() != null) {
                this.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                this.getService().update_PK(this, false);
                this.getService().use_potion(0, this.hp);
                this.getService().use_potion(1, this.mp);
            }

            int returnGateMapId = Zone.getIslandEntranceMap(this.map.template.id);
            Zone[] targetZones = Zone.getMapByID(returnGateMapId);
            if (targetZones != null && targetZones.length > 0 && targetZones[0] != null) {
                Vgo vgo = new Vgo();
                vgo.map_go = new Zone[]{targetZones[0]};
                vgo.xnew = 349;
                vgo.ynew = 345;
                this.goto_map(vgo);
            }
            this.time_can_mob_atk = System.currentTimeMillis() + 1500L;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void respawnBaoVePhaoDai() {
        try {
            BaoVePhaoDai bvd = (this.map != null && this.map.baoVePhaoDai != null) ? this.map.baoVePhaoDai
                    : (this.clan != null && this.clan.baoVePhaoDai != null ? this.clan.baoVePhaoDai
                    : (this.dungeon instanceof BaoVePhaoDai ? (BaoVePhaoDai) this.dungeon : null));
            if (bvd == null || bvd.isClose) {
                this.isdie = false;
                this.time_hs_little_garden = 0;
                this.type_pk = -1;
                if (this.ability != null) {
                    this.hp = this.ability.get_hp_max(true);
                    this.mp = this.ability.get_mp_max(true);
                }
                if (this.getService() != null) {
                    this.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                    this.getService().update_PK(this, false);
                    this.getService().use_potion(0, this.hp);
                    this.getService().use_potion(1, this.mp);
                }
                this.return_to_previous_map();
                return;
            }

            boolean isTeamA = (this.clan != null && this.clan.equals(bvd.clanA)) || this.type_pk == 4;
            Zone targetBase = isTeamA ? bvd.mapClanA : bvd.mapClanB;
            if (targetBase == null && bvd.maps != null) {
                int targetMapId = isTeamA ? 267 : 271;
                for (Zone z : bvd.maps) {
                    if (z != null && z.template != null && z.template.id == targetMapId) {
                        targetBase = z;
                        break;
                    }
                }
            }

            this.isdie = false;
            this.time_hs_little_garden = 0;
            if (this.ability != null) {
                this.hp = this.ability.get_hp_max(true);
                this.mp = this.ability.get_mp_max(true);
            }
            this.type_pk = (byte) (isTeamA ? 4 : 5);

            if (targetBase != null) {
                Vgo vgo = new Vgo();
                vgo.map_go = new Zone[]{targetBase};
                vgo.xnew = (short) (targetBase.template != null && targetBase.template.maxW > 0 ? targetBase.template.maxW / 2 : 300);
                vgo.ynew = (short) (targetBase.template != null && targetBase.template.maxH > 0 ? targetBase.template.maxH / 2 : 250);
                this.goto_map(vgo);
            }

            if (this.getService() != null) {
                this.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                this.getService().use_potion(0, this.hp);
                this.getService().use_potion(1, this.mp);
                this.getService().update_PK(this, true);
            }
            if (this.map != null) {
                this.map.change_flag(this, this.type_pk);
            }
            this.sendRevive();
            try {
                bvd.SendInfoMap(this);
            } catch (Exception ignored) {}
            this.time_can_mob_atk = System.currentTimeMillis() + 1500L;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRevive() {
        if (this.map == null) return;
        try {
            Message mRevive = new Message(6);
            mRevive.writer().writeShort(this.index_map);
            mRevive.writer().writeByte(0);
            int curHp = (this.hp > 0) ? this.hp : (this.ability != null ? this.ability.get_hp_max(true) : 1000);
            int curMp = (this.mp > 0) ? this.mp : (this.ability != null ? this.ability.get_mp_max(true) : 500);
            mRevive.writer().writeInt(curHp);
            mRevive.writer().writeInt(curMp);
            this.map.send_msg_all_p(mRevive, null, true);
            mRevive.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void respawnTranChienLon() {
        try {
            if (this.map == null || this.map.template == null) return;
            if (!TranChienLon.isMapTranChienLon(this.map.template.id)) return;
            this.isdie = false;
            this.time_hs_little_garden = 0;
            if (this.ability != null) {
                this.hp = this.ability.get_hp_max(true);
                this.mp = this.ability.get_mp_max(true);
            }
            short mapW = (short) (this.map.template.maxW > 0 ? this.map.template.maxW : 1600);
            short mapH = (short) (this.map.template.maxH > 0 ? this.map.template.maxH : 400);
            short spawnX;
            short spawnY = (short) (mapH / 2);
            if (this.huongnghiep == 1 || this.type_pk == TranChienLon.TYPE_PK_HAI_QUAN) {
                spawnX = 250; // Trại Hải Quân
            } else if (this.huongnghiep == 2 || this.type_pk == TranChienLon.TYPE_PK_HAI_TAC) {
                spawnX = (short) Math.max(300, mapW - 250); // Trại Hải Tặc
            } else {
                spawnX = (short) (mapW / 2); // Trại Quân Cách Mạng
            }
            this.x = spawnX;
            this.y = spawnY;
            this.xold = spawnX;
            this.yold = spawnY;
            Vgo vgo = new Vgo();
            vgo.map_go = new Zone[]{this.map};
            vgo.xnew = spawnX;
            vgo.ynew = spawnY;
            this.goto_map(vgo);
            TranChienLon.setType(this);
            if (this.getService() != null) {
                this.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                this.getService().use_potion(0, this.hp);
                this.getService().use_potion(1, this.mp);
                this.getService().update_PK(this, true);
            }
            this.map.change_flag(this, this.type_pk);
            this.sendRevive();
            this.time_can_mob_atk = System.currentTimeMillis() + 1500L;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void respawnDauTruongTuDo() {
        try {
            if (this.map == null || this.map.template == null) return;
            if (this.map.template.id != 70 && this.map.template.id != 71 && this.map.template.id != 72 && this.map.template.id != 74) return;
            if (this instanceof bot.Bot) return; // Bot tự xử lý hồi sinh trong đấu trường
            this.isdie = false;
            this.time_hs_little_garden = 0;
            if (this.ability != null) {
                this.hp = this.ability.get_hp_max(true);
                this.mp = this.ability.get_mp_max(true);
            }
            // Hủy cờ PK Đấu Trường Tự Do
            this.type_pk = -1;
            this.isBackTypePk = false;

            // Map 69: Thị Trấn Whiskey (Whiskey Peak)
            Zone[] whiskeyZones = Zone.getMapByID(69);
            Zone targetZone = (whiskeyZones != null && whiskeyZones.length > 0) ? whiskeyZones[0] : null;
            if (targetZone != null) {
                short spawnX = 450;
                short spawnY = 300;
                if (targetZone.template != null && targetZone.template.npcs != null) {
                    for (Npc npc : targetZone.template.npcs) {
                        if (npc != null && (npc.idmenu == -77 || "Ms. Gym".equals(npc.namegt) || (npc.name != null && npc.name.contains("Gym")))) {
                            spawnX = (short) (npc.x + (npc.x > 100 ? -30 : 30));
                            spawnY = (short) npc.y;
                            break;
                        }
                    }
                }
                Vgo vgo = new Vgo();
                vgo.map_go = new Zone[]{targetZone};
                vgo.xnew = spawnX;
                vgo.ynew = spawnY;
                this.goto_map(vgo);
            } else {
                this.return_to_previous_map();
            }

            if (this.getService() != null) {
                this.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                this.getService().use_potion(0, this.hp);
                this.getService().use_potion(1, this.mp);
                this.getService().update_PK(this, false);
            }
            if (this.map != null) {
                this.map.change_flag(this, this.type_pk);
            }
            this.sendRevive();
            this.time_can_mob_atk = System.currentTimeMillis() + 1500L;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMoney() throws IOException {
        this.item.updateMoney(false);
    }

    public long get_vang() {
        if (this.isBot) {
            return Math.max(this.vang, 10_000_000_000L);
        }
        return this.vang;
    }

    public int get_ngoc() {
        return this.kimcuong;
    }

    public int ruby() {
        return this.ruby;
    }
    
    public int get_point_tich_tieu() {
        return this.point_tich_tieu;
    }
    
    public int so_tay() {
        return this.level_so_tay;
    }
    
    public int get_diem_danh_ev() {
        return this.diem_danh_event;
    }
    
    public int exp_so_tay() {
        return this.exp_so_tay;
    }
    
     public int get_point_hang_dong() {
        return this.point_hang_dong;
    }
    
       public int get_hd_max() {
        return this.hd_max;
    }
    
    public int so_tay_vip() {
        return this.so_tay_vip;
    }
    
    
    public int active_so_tay() {
        return this.active_so_tay;
    }
      

    public long get_vnd() {
        return this.vnd;
    }

    public int get_coin() {
        return this.coin;
    }
    
    public synchronized int get_gcoin() throws IOException {
        if (conn == null || conn.user == null) return 0;
        String query = "SELECT `coin` FROM `account` WHERE BINARY `username` = ? LIMIT 1;";
        int coin = 0;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DbManager.gI().getConnect();
            ps = connection.prepareStatement(query);
            ps.setString(1, conn.user);
            rs = ps.executeQuery();
            if (rs.next()) {
                coin = rs.getInt("coin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return coin;
    }

    public int get_huongnghiep() {
        return this.huongnghiep;
    }

    public long get_bua() {
        return this.bua;
    }

    public synchronized void update_vang(long par) {
        this.vang = Math.min(2_000_000_000_000_000L, Math.max(0L, this.vang + par));
    }

    public synchronized void update_TieuRuby(long par) {
        if ((((long) par) + this.tieuRuby) < 2_000_000_000_000_000L) {
            this.tieuRuby += par;
        }
    }

    public synchronized void update_ngoc(long par) {
        this.kimcuong = (int) Math.min(2_000_000_000L, Math.max(0L, (long) this.kimcuong + par));
        if (par < 0) {
            point_tich_tieu -= par;
            tieuRuby -= par;
            tieuTuan -= par;
            tieuTong -= par;
        }
    }
    
     public synchronized void update_ngoc_ex(long par) {
        this.kimcuong = (int) Math.min(2_000_000_000L, Math.max(0L, (long) this.kimcuong + par));
    }

   public synchronized void update_tieuRuby(long par) {
        if ((((long) par) + this.tieuRuby) < 2_000_000_000L) {
            this.tieuRuby += par;

        }
    }
    
    public synchronized void update_point_tich_tieu(long par) {
        if ((((long) par) + this.point_tich_tieu) < 2_000_000_000L) {
            this.point_tich_tieu += par;

        }
    }
    
    public synchronized void update_level_so_tay(long par) {
        if ((((long) par) + this.level_so_tay) < 2_000_000_000L) {
            this.level_so_tay += par;

        }
    }
    
     public synchronized void update_diem_danh_ev(long par) {
        if ((((long) par) + this.diem_danh_event) < 2_000_000_000L) {
            this.diem_danh_event += par;

        }
    }
    
    public synchronized void update_exp_so_tay(long par) {
        if ((((long) par) + this.exp_so_tay) < 2_000_000_000L) {
            this.exp_so_tay += par;

        }
    }
    
    public synchronized void update_point_hang_dong(long par) {
        if ((((long) par) + this.point_hang_dong) < 2_000_000_000L) {
            this.point_hang_dong += par;

        }
    }
    
     public synchronized void update_hd_max(long par) {
        if ((((long) par) + this.hd_max) < 2_000_000_000L) {
            this.hd_max += par;

        }
    }
     public synchronized void update_so_tay_vip(long par) {
        if ((((long) par) + this.so_tay_vip) < 2_000_000_000L) {
            this.so_tay_vip += par;

        }
    }
     
      public synchronized void update_active_so_tay(long par) {
        if ((((long) par) + this.active_so_tay) < 2_000_000_000L) {
            this.active_so_tay += par;

        }
    }
    
    public synchronized void update_ruby(long par) {
        this.ruby = (int) Math.min(2_000_000_000L, Math.max(0L, (long) this.ruby + par));
    }

    public synchronized boolean update_gcoin(int coin_exchange) throws IOException {
        if (conn == null || conn.user == null) return false;
        String query = "SELECT `coin` FROM `account` WHERE BINARY `username` = ? LIMIT 1;";
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;
        try {
            connection = DbManager.gI().getConnect();
            ps = connection.prepareStatement(query);
            ps.setString(1, conn.user);
            rs = ps.executeQuery();
            if (!rs.next()) {
                this.getService().send_box_ThongBao_OK("Không tìm thấy tài khoản!");
                return false;
            }
            int coin_old = rs.getInt("coin");
            if ((long) coin_old + coin_exchange < 0) {
                this.getService().send_box_ThongBao_OK("Không đủ GCoin vui lòng nạp thêm tiền!");
                return false;
            }
            int coin_new = (int) Math.min(2_000_000_000L, Math.max(0L, (long) coin_old + coin_exchange));
            psUpdate = connection.prepareStatement("UPDATE `account` SET `coin` = ? WHERE BINARY `username` = ?");
            psUpdate.setInt(1, coin_new);
            psUpdate.setString(2, conn.user);
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            this.getService().send_box_ThongBao_OK("Đã xảy ra lỗi");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (psUpdate != null) psUpdate.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public synchronized boolean update_status(int status_exchange) throws IOException {
        if (conn == null || conn.user == null) return false;
        String query = "SELECT `status` FROM `account` WHERE BINARY `username` = ? LIMIT 1;";
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;
        try {
            connection = DbManager.gI().getConnect();
            ps = connection.prepareStatement(query);
            ps.setString(1, conn.user);
            rs = ps.executeQuery();
            if (!rs.next()) {
                this.getService().send_box_ThongBao_OK("Không tìm thấy tài khoản!");
                return false;
            }
            int status_old = rs.getInt("status");
            if ((long) status_old + status_exchange < 0) {
                this.getService().send_box_ThongBao_OK("Không đủ coin");
                return false;
            }
            int status_new = (int) Math.min(2_000_000_000L, Math.max(0L, (long) status_old + status_exchange));
            psUpdate = connection.prepareStatement("UPDATE `account` SET `status` = ? WHERE BINARY `username` = ?");
            psUpdate.setInt(1, status_new);
            psUpdate.setString(2, conn.user);
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            this.getService().send_box_ThongBao_OK("Đã xảy ra lỗi");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (psUpdate != null) psUpdate.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public synchronized void update_coin(long par) {
        this.coin = (int) Math.min(2_000_000_000L, Math.max(0L, (long) this.coin + par));
    }
    public synchronized void addEventPoint(int eventId, String pointKey, int dataIndex, int value) {
        event.Event ev = event.EventManager.gI().getEvent(eventId);
        if (ev != null) {
            event.EventData ed = ev.getOrCreateEventData(this);
            if (ed != null) {
                if (pointKey != null && !pointKey.trim().isEmpty()) {
                    ed.addPoint(pointKey.trim(), value);
                }
                if (dataIndex >= 0) {
                    if (ed.data == null || ed.data.length <= dataIndex) {
                        int[] nd = new int[Math.max(30, dataIndex + 1)];
                        if (ed.data != null) System.arraycopy(ed.data, 0, nd, 0, ed.data.length);
                        ed.data = nd;
                    }
                    ed.data[dataIndex] = (int) Math.min(2_000_000_000L, Math.max(0L, (long) ed.data[dataIndex] + value));
                }
                if (dataIndex == 20 || "point_event1".equals(pointKey) || "top_goi_hop_qua".equals(pointKey)) {
                    this.pointEvent1 = (int) Math.min(2_000_000_000L, Math.max(0L, (long) this.pointEvent1 + value));
                } else if (dataIndex == 21 || "point_event2".equals(pointKey) || "top_hop_qua_db".equals(pointKey)) {
                    this.pointEvent2 = (int) Math.min(2_000_000_000L, Math.max(0L, (long) this.pointEvent2 + value));
                }
                zLog.gI().add_log(this, "EVENT_POINT", "Event " + eventId + " point [" + pointKey + " / idx " + dataIndex + "] +" + value + " -> pt=" + (pointKey != null ? ed.getPoint(pointKey) : "N/A") + ", d=" + (dataIndex >= 0 ? ed.data[dataIndex] : "N/A"));
            }
        }
    }

    public synchronized void addEventPoint(int eventId, String pointKey, int value) {
        addEventPoint(eventId, pointKey, -1, value);
    }

    public int getEventPoint(int eventId, String pointKey) {
        event.Event ev = event.EventManager.gI().getEvent(eventId);
        if (ev != null) {
            event.EventData ed = ev.getOrCreateEventData(this);
            if (ed != null) {
                return ed.getPoint(pointKey);
            }
        }
        return 0;
    }

    public void setEventPoint(int eventId, String pointKey, int value) {
        event.Event ev = event.EventManager.gI().getEvent(eventId);
        if (ev != null) {
            event.EventData ed = ev.getOrCreateEventData(this);
            if (ed != null) {
                ed.setPoint(pointKey, value);
            }
        }
    }

    public synchronized void update_pointEvent1(int par) {
        int oldVal = this.pointEvent1;
        this.pointEvent1 = (int)Math.min(2_000_000_000L, Math.max(0L, (long)this.pointEvent1 + par));
        for (event.Event activeEv : event.EventManager.gI().getActiveEventsList()) {
            if (activeEv != null) {
                event.EventData ed = activeEv.getOrCreateEventData(this);
                if (ed != null) {
                    if (ed.data != null && ed.data.length > 20) {
                        ed.data[20] = (int)Math.min(2_000_000_000L, Math.max(0L, (long)ed.data[20] + par));
                    }
                    ed.addPoint("point_event1", par);
                    String key1 = event.Event.getEventPointKey1(activeEv.getId());
                    if (key1 != null && !key1.equals("point_event1")) {
                        ed.addPoint(key1, par);
                    }
                }
            }
        }
        if (oldVal != this.pointEvent1) {
            zLog.gI().add_log(this, "EVENT_POINT", "Thay đổi pointEvent1: " + oldVal + " -> " + this.pointEvent1 + " (change: " + par + ")");
        }
    }

    public synchronized void update_pointEvent2(int par) {
        int oldVal = this.pointEvent2;
        this.pointEvent2 = (int)Math.min(2_000_000_000L, Math.max(0L, (long)this.pointEvent2 + par));
        for (event.Event activeEv : event.EventManager.gI().getActiveEventsList()) {
            if (activeEv != null) {
                event.EventData ed = activeEv.getOrCreateEventData(this);
                if (ed != null) {
                    if (ed.data != null && ed.data.length > 21) {
                        ed.data[21] = (int)Math.min(2_000_000_000L, Math.max(0L, (long)ed.data[21] + par));
                    }
                    ed.addPoint("point_event2", par);
                    String key2 = event.Event.getEventPointKey2(activeEv.getId());
                    if (key2 != null && !key2.equals("point_event2")) {
                        ed.addPoint(key2, par);
                    }
                }
            }
        }
        if (oldVal != this.pointEvent2) {
            zLog.gI().add_log(this, "EVENT_POINT", "Thay đổi pointEvent2: " + oldVal + " -> " + this.pointEvent2 + " (change: " + par + ")");
        }
    }

    public synchronized void update_point_event(long par) {
        update_pointEvent1((int)par);
    }

    public synchronized void updateVnd(long par) {
        long old = this.vnd;
        this.vnd = Math.min(2_000_000_000L, Math.max(0L, this.vnd + par));
        String info = "changeExtol: " + ZUtil.number_format(old) + " -> " + ZUtil.number_format(this.vnd) + "\n";
        StackTraceElement[] th = Thread.currentThread().getStackTrace();
        for (int i = th.length - 3; i >= 2; i--) {
            info += (" <- " + th[i]);
        }
    }

    public synchronized void update_bua(long par) {
        this.bua = Math.min(2_000_000_000L, Math.max(0L, this.bua + par));
    }

    public EffTemplate get_eff(int id) {
        if (this.list_eff == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        for (EffTemplate temp : this.list_eff) {
            if (temp != null && temp.id == id && temp.time > now) {
                return temp;
            }
        }
        return null;
    }

    public EffTemplate get_eff_param(int id, int param) {
        if (this.list_eff == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        for (EffTemplate eff : this.list_eff) {
            if (eff != null && eff.id == id && eff.param == param && eff.time > now) {
                return eff;
            }
        }
        return null;
    }

    public boolean isChoang() {
        return get_eff(201) != null;
    }

    public boolean isCantMove() {
        return isdie || isChoang() || get_eff(208) != null;
    }

    public boolean isCantSkill() {
        return isdie || isChoang() || get_eff(215) != null;
    }

    public void wear_item(Item_wear it) throws IOException {
        if (it == null || it.template == null) {
            this.use_item_3 = -1;
            return;
        }
        Player notifyP = (this instanceof DeTu && ((DeTu) this).master != null) ? ((DeTu) this).master : this;
        if (this.level < it.template.level) {
            if (notifyP.conn != null && notifyP.getService() != null) {
                notifyP.getService().send_box_ThongBao_OK("Chưa đủ level");
            }
            this.use_item_3 = -1;
            return;
        }
        if (it.template.clazz != 0 && this.clazz != it.template.clazz) {
            if (notifyP.conn != null && notifyP.getService() != null) {
                notifyP.getService().send_box_ThongBao_OK("Không thể mặc vật phẩm này");
            }
            this.use_item_3 = -1;
            return;
        }
        if (it.valueKichAn != 12) {
            it.typelock = 1;
        }
        byte index_wear = it.template.typeEquip;
        if (index_wear >= 0 && index_wear < item.it_body.length) {
            int bagSlot = -1;
            if (it.index >= 0 && it.index < item.bag3.length && item.bag3[it.index] == it) {
                bagSlot = it.index;
            } else {
                for (int i = 0; i < item.bag3.length; i++) {
                    if (item.bag3[i] == it) {
                        bagSlot = i;
                        break;
                    }
                }
            }
            if (bagSlot != -1) {
                item.bag3[bagSlot] = null;
            }
            if (item.it_body[index_wear] != null) {
                Item_wear oldWear = item.it_body[index_wear];
                item.it_body[index_wear] = null;
                if (index_wear == 6) {
                    item.it_heart = null;
                }
                item.add_item_bag3(oldWear);
            }
            item.it_body[index_wear] = it;
            it.index = index_wear;
            if (index_wear == 6) {
                item.it_heart = it;
                item.it_heart.typelock = 1;
            }
        }
        // 1. Recalculate parts and stats immediately
        this.updateParts();
        this.setAbility();
        if (this.hp > this.hpMax) this.hp = this.hpMax;
        if (this.mp > this.mpMax) this.mp = this.mpMax;

        // 2. Update inventory
        item.updateInventory(false);

        // 3. Devil Fruit / Dial skill
        if (it.template.typeEquip == 7) {
            Skill_info sk_select = null;
            if (this.skill_point != null) {
                for (int i = 0; i < this.skill_point.size(); i++) {
                    if (this.skill_point.get(i).temp != null && this.skill_point.get(i).temp.indexSkillInServer == 660) {
                        sk_select = this.skill_point.get(i);
                        break;
                    }
                }
                if (sk_select == null) {
                    sk_select = new Skill_info();
                    sk_select.exp = 0;
                    sk_select.temp = Skill_Template.get_temp(660, 0);
                    sk_select.lvdevil = 0;
                    sk_select.devilpercent = 0;
                    this.skill_point.add(sk_select);
                    this.send_skill();
                }
            }
        }

        // 4. Visual and stat updates to client & map
        this.update_info_to_all();
        if (this.conn != null && this.getService() != null) {
            this.getService().UpdatePvpPoint();
        }
        this.use_item_3 = -1;
    }

    /**
     * Khởi tạo và mặc chuẩn trang bị Full 8 món cho Bot (dùng 'this' trực tiếp trên Player).
     * Đảm bảo không bị thiếu đồ hay NullPointer ở bất kỳ slot nào.
     * Quy tắc chuẩn xịn:
     *   - Slot 6 & 7 (Tim & Dial/Khiên): KHÔNG ĐƯỢC KÍCH ẨN/HOÀN MỸ (isHoanMy = 0, valueKichAn = -1).
     *   - Tim (Heart): Cấp min 1, max 100 theo cấp của bot.
     *   - Kích ẩn (valueKichAn): Tối đa trùng 2 món trên toàn bộ bộ đồ.
     */
    public static final int THAN_TRANG_START_ID = 2604;
    public static final int THAN_TRANG_END_ID = 2693;
    public static final int THAN_TRANG_SET_COUNT = 15;

    public static final String[] THAN_TRANG_15_SET_NAMES = new String[]{
        "Set Dung Nham (Akainu)",
        "Set Hàn Băng (Aokiji)",
        "Set Quang Tốc (Kizaru)",
        "Set Hắc Ám (Blackbeard)",
        "Set Sấm Sét (Enel)",
        "Set Chấn Động (Whitebeard)",
        "Set Phẫu Thuật (Law)",
        "Set Từ Tính (Kid)",
        "Set Rồng Độc (Magellan)",
        "Set Tình Yêu (Boa Hancock)",
        "Set Phượng Hoàng Lam Hỏa (Marco)",
        "Set Phật Quang (Sengoku)",
        "Set Bách Thú Kaido",
        "Set Hổ Răng Kiếm (Who's Who)",
        "Set T-Rex Bạo Chúa (Queen)"
    };

    /**
     * Lấy ID Thần Trang chuẩn xác trong 90 món / 15 set (ID 2604..2693).
     * @param setIndex Chỉ số set từ 0 đến 14 (15 set)
     * @param slot Offset món từ 0 đến 5 (0: Vũ khí, 1: Mũ, 2: Dây chuyền, 3: Áo, 4: Nhẫn, 5: Ủng)
     * @return ID trang bị từ 2604 đến 2693
     */
    public static int getThanTrangItemId(int setIndex, byte slot) {
        int safeSet = Math.max(0, Math.min(THAN_TRANG_SET_COUNT - 1, setIndex));
        int safeSlot = Math.max(0, Math.min(5, (int) slot));
        return THAN_TRANG_START_ID + (safeSet * 6) + safeSlot;
    }

    /**
     * Tương thích ngược: lấy ID Thần Trang theo slot (0..5) và clazz (1..5).
     * Ánh xạ thông minh về 15 set Thần Trang thật (2604..2693).
     */
    public static int getThanTrangTemplateId(byte slot, byte clazz) {
        int setIndex;
        switch (clazz) {
            case 1: setIndex = 0; break;  // Võ sĩ -> Dung Nham (2604..2609)
            case 2: setIndex = 1; break;  // Kiếm sĩ -> Hàn Băng (2610..2615)
            case 3: setIndex = 2; break;  // Đầu bếp -> Quang Tốc (2616..2621)
            case 4: setIndex = 4; break;  // Pháp sư -> Sấm Sét (2628..2633)
            case 5: setIndex = 7; break;  // Xạ thủ -> Từ Tính (2646..2651)
            default: setIndex = 0; break;
        }
        return getThanTrangItemId(setIndex, slot);
    }

    /**
     * Tự động chọn Set Thần Trang phù hợp (0..14) theo Class, Trái Ác Quỷ hoặc Tên nhân vật/bot.
     */
    public static int selectThanTrangSetIndex(byte clazz, int fruitId, String botName) {
        if (botName != null && !botName.isEmpty()) {
            String lower = botName.toLowerCase();
            if (lower.contains("akainu") || lower.contains("sakazuki") || lower.contains("dung nham")) return 0;
            if (lower.contains("aokiji") || lower.contains("kuzan") || lower.contains("băng")) return 1;
            if (lower.contains("kizaru") || lower.contains("borsalino") || lower.contains("quang tốc") || lower.contains("ánh sáng")) return 2;
            if (lower.contains("blackbeard") || lower.contains("teach") || lower.contains("râu đen") || lower.contains("hắc ám")) return 3;
            if (lower.contains("enel") || lower.contains("sấm sét") || lower.contains("lôi thần")) return 4;
            if (lower.contains("whitebeard") || lower.contains("râu trắng") || lower.contains("chấn động") || lower.contains("edward")) return 5;
            if (lower.contains("law") || lower.contains("phẫu thuật") || lower.contains("room") || lower.contains("kikoku")) return 6;
            if (lower.contains("kid") || lower.contains("từ tính") || lower.contains("punk") || lower.contains("railgun")) return 7;
            if (lower.contains("magellan") || lower.contains("độc") || lower.contains("hydra")) return 8;
            if (lower.contains("hancock") || lower.contains("boa") || lower.contains("tình yêu") || lower.contains("mero")) return 9;
            if (lower.contains("marco") || lower.contains("phượng hoàng") || lower.contains("lam hỏa")) return 10;
            if (lower.contains("sengoku") || lower.contains("phật quang") || lower.contains("phật tổ")) return 11;
            if (lower.contains("kaido") || lower.contains("bách thú") || lower.contains("thanh long")) return 12;
            if (lower.contains("who's who") || lower.contains("whos who") || lower.contains("hổ răng kiếm")) return 13;
            if (lower.contains("queen") || lower.contains("khủng long") || lower.contains("t-rex") || lower.contains("bạo chúa")) return 14;
        }

        // Ánh xạ theo Trái Ác Quỷ
        switch (fruitId) {
            case 32: return 0;   // Magma -> Dung Nham
            case 33: return 1;   // Ice -> Hàn Băng
            case 34: return 2;   // Light/Smoke -> Quang Tốc
            case 427: return 3;  // Dark -> Hắc Ám
            case 91: return 4;   // Rumble -> Sấm Sét
            case 93: return 5;   // Gura -> Chấn Động
            case 219: return 7;  // Bomb/Magnet -> Từ Tính
            case 160: return 10; // Phoenix -> Phượng Hoàng Lam Hỏa
            case 316: return 11; // Mochi/Buddha -> Phật Quang
            case 161: return 12; // Dragon -> Bách Thú Kaido
            case 90:  return 11; // Nika -> Phật Quang
        }

        // Ánh xạ theo Class môn phái
        switch (clazz) {
            case 1: { // Võ sĩ
                int[] pool = {0, 7, 10, 11};
                return pool[core.ZUtil.random(pool.length)];
            }
            case 2: { // Kiếm sĩ
                int[] pool = {1, 2, 5, 6, 12, 13};
                return pool[core.ZUtil.random(pool.length)];
            }
            case 3: { // Đầu bếp
                int[] pool = {0, 1, 2, 4, 10, 12};
                return pool[core.ZUtil.random(pool.length)];
            }
            case 4: { // Pháp sư
                int[] pool = {4, 8, 9, 3, 1};
                return pool[core.ZUtil.random(pool.length)];
            }
            case 5: { // Xạ thủ
                int[] pool = {7, 9, 14, 2, 4};
                return pool[core.ZUtil.random(pool.length)];
            }
            default:
                return core.ZUtil.random(THAN_TRANG_SET_COUNT);
        }
    }

    /**
     * Khởi tạo Bộ Trang Bị chuẩn cho Bot theo Bậc (Tier Seed: 1.0 .. 4.0+).
     * - Slot 0..5 (Trang bị thường): Luôn mặc đồ chuẩn Class & Level, Cường hóa +7..+16, Hoàn Mỹ, Kích Ẩn cấp 5, Khảm Đá.
     * - Slot 6: Tim (Heart).
     * - Slot 7: Dial.
     * - Slot 8..13 (Thần Trang): Khi Tier >= 3.5 (hoặc chỉ định), trang bị Full Bộ 6 món Thần Trang chuẩn 15 set (2604..2693).
     */
    public void setupBotEquip(byte clazz, short botLevel, double tier, boolean hasHoanMy, boolean hasKhamDa, boolean hasKichAn) {
        setupBotEquip(clazz, botLevel, tier, hasHoanMy, hasKhamDa, hasKichAn, -1);
    }

    public void setupBotEquip(byte clazz, short botLevel, double tier, boolean hasHoanMy, boolean hasKhamDa, boolean hasKichAn, int specificThanTrangSet) {
        if (this.item == null) {
            this.item = new itemz.Item(this);
        }
        if (this.item.it_body == null || this.item.it_body.length < itemz.Item.MAX_BODY) {
            this.item.it_body = new Item_wear[itemz.Item.MAX_BODY];
        }
        if (this.item.bag3 == null) {
            this.item.bag3 = new Item_wear[this.item.max_bag];
        }
        if (this.item.bag47 == null) {
            this.item.bag47 = new ArrayList<>();
        }

        byte botClazz = clazz > 0 ? clazz : 1;
        short timLevel = (short) Math.max(1, Math.min(100, (int) botLevel));

        // Xác định cấp độ cường hóa chuẩn theo tier cho đồ thường
        byte targetLevelUp;
        if (tier >= 3.5) { // Tier 4: VIP / Siêu VIP / Thần Thoại (+15..+16)
            targetLevelUp = (byte) (15 + core.ZUtil.random(2));
        } else if (tier >= 2.5) { // Tier 3: Cao Thủ (+13..+14)
            targetLevelUp = (byte) (13 + core.ZUtil.random(2));
        } else if (tier >= 1.5) { // Tier 2: Tầm Trung (+11..+12)
            targetLevelUp = (byte) (11 + core.ZUtil.random(2));
        } else { // Tier 1: Cơ bản (+7..+10)
            targetLevelUp = (byte) (7 + core.ZUtil.random(4));
        }

        // Chọn 1 Kích Ẩn chủ đạo đồng bộ cho set đồ thường (0: Bất tử, 1: Lời cảm ơn, 2: Lá chắn, 3: Khóa MP, 4: Bộc phá)
        byte primaryKichAn = (byte) core.ZUtil.random(5);

        // =====================================================================
        // 1. CÀI ĐẶT TRANG BỊ THƯỜNG (Slot 0..5), TIM (Slot 6), DIAL (Slot 7)
        // =====================================================================
        for (int slot = 0; slot < 8; slot++) {
            ItemTemplate3 template = null;
            short searchLevel = (slot == 6) ? timLevel : botLevel;
            template = findBestItemTemplate((byte) slot, botClazz, searchLevel);
            if (template != null && (template.typeEquip != slot || template.level > searchLevel || (template.clazz != 0 && template.clazz != botClazz))) {
                template = null;
            }

            if (template == null) {
                int defaultId = getDefaultItemTemplateId((byte) slot, botClazz);
                if (defaultId >= 0) {
                    ItemTemplate3 defTemp = ItemTemplate3.get_it_by_id((short) defaultId);
                    if (defTemp != null && defTemp.typeEquip == slot && (defTemp.clazz == 0 || defTemp.clazz == botClazz)) {
                        template = defTemp;
                    }
                }
            }

            // Slot 6 (Tim) & Slot 7 (Dial): Bắt buộc đúng typeEquip và cấp độ phù hợp
            if ((slot == 6 || slot == 7) && (botLevel < 15 || template == null || template.typeEquip != slot)) {
                this.item.it_body[slot] = null;
                if (slot == 6) {
                    this.item.it_heart = null;
                }
                continue;
            }

            if (template != null && template.typeEquip == slot) {
                Item_wear item = new Item_wear();
                item.setup_template_by_id(template);
                item.index = (short) slot;

                // Slot 6 (Tim): KHÔNG được kích ẩn / hoàn mỹ / khảm đá
                if (slot == 6) {
                    item.isHoanMy = 0;
                    item.valueKichAn = -1;
                    item.mdakham = new short[0];
                    item.option_item_2 = new ArrayList<>();

                    int minTimLv;
                    int maxTimLv;
                    if (tier >= 3.5) {
                        minTimLv = Math.max(1, Math.min(100, (int) (botLevel * 0.85)));
                        maxTimLv = Math.min(110, Math.max(minTimLv + 10, (int) (botLevel * 1.15 + 10)));
                    } else if (tier >= 2.5) {
                        minTimLv = Math.max(1, Math.min(90, (int) (botLevel * 0.70)));
                        maxTimLv = Math.min(110, Math.max(minTimLv + 10, (int) (botLevel * 0.95 + 5)));
                    } else if (tier >= 1.5) {
                        minTimLv = Math.max(1, Math.min(80, (int) (botLevel * 0.50)));
                        maxTimLv = Math.min(110, Math.max(minTimLv + 10, (int) (botLevel * 0.80)));
                    } else {
                        minTimLv = Math.max(1, Math.min(60, (int) (botLevel * 0.30)));
                        maxTimLv = Math.min(110, Math.max(minTimLv + 10, (int) (botLevel * 0.60)));
                    }
                    if (minTimLv > maxTimLv) minTimLv = maxTimLv;
                    item.levelUp = (byte) ZUtil.random(minTimLv, maxTimLv);

                    this.item.it_heart = item;
                } else if (slot == 7) { // Dial / Khiên
                    item.isHoanMy = 0;
                    item.valueKichAn = -1;
                    item.levelUp = (byte) Math.min(targetLevelUp, 10);
                    if (hasKhamDa && tier >= 1.5) {
                        setupBotItemGems(item, slot, tier, botClazz);
                    } else {
                        item.mdakham = new short[0];
                        item.option_item_2 = new ArrayList<>();
                    }
                } else {
                    item.levelUp = targetLevelUp;
                    item.isHoanMy = (byte) ((hasHoanMy || tier >= 1.5) ? 1 : 0);
                    // Đồng bộ Kích Ẩn để đạt >= 3 món kích hoạt buff hiệu ứng thực tế
                    item.valueKichAn = (hasKichAn || tier >= 1.5) ? primaryKichAn : -1;
                    if (hasKhamDa || tier >= 1.5) {
                        setupBotItemGems(item, slot, tier, botClazz);
                    }
                }

                this.item.it_body[slot] = item;
            } else {
                this.item.it_body[slot] = null;
                if (slot == 6) {
                    this.item.it_heart = null;
                }
            }
        }

        // =====================================================================
        // 2. CÀI ĐẶT BỘ THẦN TRANG (Slot 8..13: 6 Món Chuẩn ID 2604..2693)
        // =====================================================================
        if (tier >= 3.5 || specificThanTrangSet >= 0) {
            int setIndex = (specificThanTrangSet >= 0 && specificThanTrangSet < THAN_TRANG_SET_COUNT)
                    ? specificThanTrangSet
                    : selectThanTrangSetIndex(botClazz, 0, this.name);
            byte ttLevelUp = (byte) (tier >= 3.8 ? (15 + core.ZUtil.random(2)) : (tier >= 3.5 ? (12 + core.ZUtil.random(3)) : 10));

            for (byte piece = 0; piece < 6; piece++) {
                int ttItemId = getThanTrangItemId(setIndex, piece);
                int targetSlot = 8 + piece;

                ItemTemplate3 ttTemp = ItemTemplate3.get_it_by_id((short) ttItemId);
                if (ttTemp != null) {
                    Item_wear ttItem = new Item_wear();
                    ttItem.setup_template_by_id(ttTemp);
                    ttItem.index = (short) targetSlot;
                    ttItem.color = 8;
                    ttItem.typelock = 1;
                    ttItem.levelUp = ttLevelUp;
                    ttItem.isHoanMy = 0;
                    ttItem.valueKichAn = -1;
                    ttItem.numLoKham = 0;
                    ttItem.numHoleDaDuc = 0;
                    ttItem.mdakham = new short[0];
                    ttItem.valueChetac = 0;

                    List<Option> defaultOps = template.ThanTrangConfig.getThanTrangOptions(ttItemId);
                    ttItem.option_item = new ArrayList<>();
                    if (defaultOps != null && !defaultOps.isEmpty()) {
                        for (Option op : defaultOps) {
                            ttItem.option_item.add(new Option(op.id, op.getParam()));
                        }
                    }
                    ttItem.option_item_2 = new ArrayList<>();

                    this.item.it_body[targetSlot] = ttItem;
                }
            }

            // Kích hoạt nhận diện Full Set Thần Trang và hiệu ứng biến hình Thần Trang
            int fullSetId = model.ThanTrangConfig.getFullSetId(this);
            if (fullSetId > 0 && fullSetId <= model.ThanTrangConfig.TOTAL_SETS) {
                this.isTransformThanTrang = true;
                this.transformThanTrangSetId = (byte) fullSetId;
            }
        } else {
            // Dọn sạch các slot Thần Trang (8..13) nếu không đạt Tier
            for (int slot = 8; slot <= 13; slot++) {
                if (slot < this.item.it_body.length) {
                    this.item.it_body[slot] = null;
                }
            }
            this.isTransformThanTrang = false;
            this.transformThanTrangSetId = 0;
        }

        updateParts();
    }

    /**
     * Khởi tạo đá khảm (mdakham) chuẩn xịn cho từng món trang bị của Bot.
     * Tự động chọn đúng loại đá phù hợp cho từng vị trí trang bị (Vũ khí, Nón, Áo, Nhẫn, Dây chuyền, Quần, Dial).
     * Gọi Rebuild_Item.add_op_ngoc_kham_new để cộng chỉ số thực sự vào option_item_2 của món đồ.
     */
    public static void setupBotItemGems(Item_wear item, int slot, double tier, byte clazz) {
        if (item == null || item.template == null) return;
        if (slot == 6) return; // Slot 6 (Tim) không khảm đá

        item.mdakham = new short[0];
        item.option_item_2 = new ArrayList<>();

        int numSlots = 3;
        if (tier >= 3.5) {
            numSlots = 3; // Tier Thần Thoại: Full 3 lỗ
        } else if (tier >= 2.5) {
            numSlots = core.ZUtil.random(2, 4); // 2-3 lỗ
        } else if (tier >= 1.5) {
            numSlots = 2;
        } else {
            numSlots = core.ZUtil.random(1, 3); // 1-2 lỗ
        }
        item.numLoKham = (byte) numSlots;

        short[] gemPool = getGemPoolForSlotAndTier(slot, tier);
        if (gemPool == null || gemPool.length == 0) return;

        List<Short> usedGems = new ArrayList<>();

        for (int i = 0; i < numSlots; i++) {
            short chosenGem = -1;
            List<Short> availableGems = new ArrayList<>();
            for (short gId : gemPool) {
                if (!usedGems.contains(gId)) {
                    availableGems.add(gId);
                }
            }
            if (!availableGems.isEmpty()) {
                chosenGem = availableGems.get(core.ZUtil.random(availableGems.size()));
            } else {
                chosenGem = gemPool[core.ZUtil.random(gemPool.length)];
            }

            usedGems.add(chosenGem);
            itemz.Rebuild_Item.add_op_ngoc_kham_new(item, chosenGem);
        }
    }

    private static short[] getGemPoolForSlotAndTier(int slot, double tier) {
        switch (slot) {
            case 0: // Vũ Khí -> Sát Thương / Tấn Công
                if (tier >= 3.5) {
                    return new short[]{652, 653, 654, 655, 656}; // Thần Thoại (+320 Công, +25 Kháng)
                } else if (tier >= 2.5) {
                    return new short[]{55}; // Cấp 6 (+220 Atk)
                } else if (tier >= 1.5) {
                    return new short[]{53, 54, 55}; // Cấp 4-6
                } else {
                    return new short[]{50, 51, 52}; // Cấp 1-3
                }

            case 1: // Nón -> Giáp / Phòng Thủ
                if (tier >= 3.5) {
                    return new short[]{657, 658, 659, 660, 661}; // Thần Thoại (+100 Def, +25 Kháng)
                } else if (tier >= 2.5) {
                    return new short[]{61}; // Cấp 6
                } else if (tier >= 1.5) {
                    return new short[]{59, 60, 61};
                } else {
                    return new short[]{56, 57, 58};
                }

            case 2: // Dây Chuyền -> Xuyên Giáp / Sát Thương Chí Mạng
                if (tier >= 3.5) {
                    return new short[]{672, 673, 674, 675, 676}; // Thần Thoại (+150 Pierce, +25 Kháng)
                } else if (tier >= 2.5) {
                    return new short[]{79}; // Cấp 6
                } else if (tier >= 1.5) {
                    return new short[]{77, 78, 79};
                } else {
                    return new short[]{74, 75, 76};
                }

            case 3: // Áo -> Máu / HP
                if (tier >= 3.5) {
                    return new short[]{647, 648, 649, 650, 651}; // Thần Thoại (+320 HP, +25 Kháng)
                } else if (tier >= 2.5) {
                    return new short[]{49}; // Cấp 6 (+140 HP)
                } else if (tier >= 1.5) {
                    return new short[]{47, 48, 49};
                } else {
                    return new short[]{44, 45, 46};
                }

            case 4: // Nhẫn -> Chí Mạng & Kháng Chí Mạng
                if (tier >= 3.5) {
                    return new short[]{667, 668, 669, 670, 671}; // Thần Thoại (+150 Crit, +25 Kháng)
                } else if (tier >= 2.5) {
                    return new short[]{73}; // Cấp 6
                } else if (tier >= 1.5) {
                    return new short[]{71, 72, 73};
                } else {
                    return new short[]{68, 69, 70};
                }

            case 5: // Quần -> Phục Hồi HP / Potion
                if (tier >= 3.5) {
                    return new short[]{662, 663, 664, 665, 666}; // Thần Thoại (+150 Recovery, +25 Kháng)
                } else if (tier >= 2.5) {
                    return new short[]{67}; // Cấp 6
                } else if (tier >= 1.5) {
                    return new short[]{65, 66, 67};
                } else {
                    return new short[]{62, 63, 64};
                }

            case 7: // Dial / Khiên -> Đá Thần Thoại / Cấp 6
                if (tier >= 3.5) {
                    return new short[]{652, 667, 672}; // Thần Thoại
                } else if (tier >= 2.5) {
                    return new short[]{55, 61, 73};
                } else {
                    return new short[]{55, 49, 61};
                }

            default:
                return null;
        }
    }

    private byte selectDiverseKichAn(int[] kichAnCounts) {
        List<Integer> validIds = new ArrayList<>();
        for (int id = 1; id <= 5; id++) {
            if (kichAnCounts[id] < 2) {
                validIds.add(id);
            }
        }
        if (validIds.isEmpty()) return -1;
        int chosen = validIds.get(core.ZUtil.random(validIds.size()));
        kichAnCounts[chosen]++;
        return (byte) chosen;
    }

    private ItemTemplate3 findBestItemTemplate(byte typeEquip, byte clazz, short botLevel) {
        ItemTemplate3 best = null;
        int bestLevel = -1;

        if (ItemTemplate3.ENTRYS != null) {
            // 1. Tìm item khớp đúng Class và level <= botLevel
            for (ItemTemplate3 t : ItemTemplate3.ENTRYS) {
                if (t == null) continue;
                if (t.typeEquip != typeEquip) continue;
                if (t.clazz != 0 && t.clazz != clazz) continue;
                if (t.level > botLevel) continue;
                if (t.level >= bestLevel) {
                    bestLevel = t.level;
                    best = t;
                }
            }
        }
        if (best == null && ItemTemplate3.ENTRYS != null) {
            // 2. Tìm item dùng chung (clazz == 0) có level <= botLevel
            for (ItemTemplate3 t : ItemTemplate3.ENTRYS) {
                if (t == null) continue;
                if (t.typeEquip != typeEquip) continue;
                if (t.clazz != 0) continue;
                if (t.level > botLevel) continue;
                if (t.level >= bestLevel) {
                    bestLevel = t.level;
                    best = t;
                }
            }
        }
        return (best != null && best.typeEquip == typeEquip && best.level <= botLevel) ? best : null;
    }

    private int getDefaultItemTemplateId(byte typeEquip, byte clazz) {
        int baseClass = (clazz >= 1 && clazz <= 5) ? clazz : 1;
        switch (typeEquip) {
            case 0: return (baseClass - 1) * 8; // Vũ khí cấp 1
            case 3: return 40 + (baseClass - 1) * 8; // Áo cấp 1
            case 5: return 80 + (baseClass - 1) * 8; // Quần cấp 1
            default: return -1; // Các slot 1 (Nón), 2 (Dây chuyền), 4 (Nhẫn), 6 (Tim), 7 (Dial) không có đồ mặc định cấp 1
        }
    }

    public static Skill_info createBotSkillInfo(int index) {
        Skill_info sk = new Skill_info();
        sk.exp = 0;
        sk.temp = Skill_Template.get_temp(index, 0);
        if (sk.temp == null) {
            sk.exp = -1;
            sk.temp = Skill_Template.get_temp(index, -1);
            if (sk.temp != null && sk.temp.Lv_RQ == -1) {
                Skill_Template.learn_skill(sk);
            }
        }
        return (sk.temp != null) ? sk : null;
    }

    /**
     * Danh sách kỹ năng mặc định chuẩn khi tạo nhân vật người chơi mới theo chuẩn base HTTH gốc.
     * Gồm đầy đủ 16 kỹ năng:
     * - Chiêu 1: Đã học (Lv 1, exp 0)
     * - Chiêu 2, 3, 4 (Thủy chiến), 6 (Buff môn phái): Chưa học (Lv_RQ -1, exp -1)
     * - Kỹ năng Dial: Haki Quan Sát (667, ID 1015, exp -1)
     * - Kỹ năng bị động & hỗ trợ dùng chung:
     *   + 300: Bản năng sinh tồn (ID 1000, exp -1)
     *   + 305: Vận động viên Marathon (ID 1001, exp -1)
     *   + 310: Thôi miên (ID 1002, exp -1)
     *   + 315: Tekkai (ID 1003, exp -1)
     *   + 320: Ý chí của D (ID 1004, exp -1)
     *   + 325: Tinh thần võ sĩ đạo (ID 1005, exp -1)
     *   + 552: Nổ lực không ngừng (ID 1006, exp -1)
     *   + 557: Luyện tập gian khổ (ID 1007, exp -1)
     * - Kỹ năng Haki nâng cao:
     *   + 779: Haki Vũ Trang (ID 1016, exp -1)
     *   + 785: Haki Bá Vương (ID 1017, exp -1)
     */
    public static List<Skill_info> getDefaultPlayerSkills(int clazz) {
        List<Skill_info> list = new ArrayList<>();
        byte bClazz = (byte) Math.max(1, Math.min(5, clazz));

        // 1. Chiêu 1: Đã học (Lv 1, exp 0)
        int sk1Idx = (bClazz - 1) * 60;
        Skill_Template st1 = Skill_Template.get_temp(sk1Idx, 0);
        if (st1 == null) {
            st1 = zinterfaces.menus.AdminSystemMenu.getMatchingClassSkillTemplate(bClazz, 1, (byte) 1);
        }
        if (st1 != null) {
            Skill_info sk1 = new Skill_info();
            sk1.temp = st1;
            sk1.exp = 0;
            list.add(sk1);
        }

        // 2. Chiêu 2: Chưa học (Lv_RQ -1, exp -1)
        int sk2Idx = (bClazz - 1) * 60 + 20;
        Skill_Template st2 = Skill_Template.get_temp(sk2Idx, -1);
        if (st2 == null) {
            st2 = zinterfaces.menus.AdminSystemMenu.getMatchingClassSkillTemplate(bClazz, 2, (byte) -1);
        }
        if (st2 != null) {
            Skill_info sk2 = new Skill_info();
            sk2.temp = st2;
            sk2.exp = -1;
            list.add(sk2);
        }

        // 3. Chiêu 3: Chưa học (Lv_RQ -1, exp -1)
        int sk3Idx = (bClazz - 1) * 60 + 40;
        Skill_Template st3 = Skill_Template.get_temp(sk3Idx, -1);
        if (st3 == null) {
            st3 = zinterfaces.menus.AdminSystemMenu.getMatchingClassSkillTemplate(bClazz, 3, (byte) -1);
        }
        if (st3 != null) {
            Skill_info sk3 = new Skill_info();
            sk3.temp = st3;
            sk3.exp = -1;
            list.add(sk3);
        }

        // 4. Chiêu 4: Thủy chiến (Đã học Lv 1, exp 0)
        int sk4Idx = 375 + (bClazz - 1) * 20;
        Skill_Template st4 = Skill_Template.get_temp(sk4Idx, 0);
        if (st4 == null) {
            st4 = zinterfaces.menus.AdminSystemMenu.getMatchingClassSkillTemplate(bClazz, 4, (byte) 1);
        }
        if (st4 != null) {
            Skill_info sk4 = new Skill_info();
            sk4.temp = st4;
            sk4.exp = 0;
            list.add(sk4);
        }

        // 5. Chiêu 6: Buff môn phái (Lv_RQ -1, exp -1)
        int sk6Idx = 487 + (bClazz - 1) * 5;
        Skill_Template st6 = Skill_Template.get_temp(sk6Idx, -1);
        if (st6 == null) {
            st6 = zinterfaces.menus.AdminSystemMenu.getMatchingClassSkillTemplate(bClazz, 6, (byte) -1);
        }
        if (st6 != null) {
            Skill_info sk6 = new Skill_info();
            sk6.temp = st6;
            sk6.exp = -1;
            list.add(sk6);
        }

        // 6 -> 16. Kỹ năng dùng chung (Dial, Bị động, Hỗ trợ, Haki)
        int[] commonSkillIndices = {667, 300, 305, 310, 315, 320, 325, 552, 557, 779, 785};
        for (int idx : commonSkillIndices) {
            Skill_Template st = Skill_Template.get_temp(idx, -1);
            if (st != null) {
                Skill_info sk = new Skill_info();
                sk.temp = st;
                sk.exp = -1;
                list.add(sk);
            }
        }

        return list;
    }

    /**
     * Bổ sung đầy đủ các kỹ năng mặc định chưa học nếu tài khoản cũ bị thiếu.
     */
    public static void ensureDefaultPlayerSkills(int clazz, List<Skill_info> currentSkills) {
        if (currentSkills == null) return;
        List<Skill_info> defSkills = getDefaultPlayerSkills(clazz);
        for (Skill_info defSk : defSkills) {
            if (defSk == null || defSk.temp == null) continue;
            boolean exists = false;
            for (Skill_info curSk : currentSkills) {
                if (curSk != null && curSk.temp != null) {
                    if (curSk.temp.ID == defSk.temp.ID) {
                        exists = true;
                        // Tự động kích hoạt Chiêu 4 (Thủy chiến - ID 3) lên Đã Học Lv 1 nếu tài khoản cũ chưa học
                        if (curSk.temp.ID == 3 && (curSk.exp < 0 || curSk.temp.Lv_RQ < 0)) {
                            curSk.exp = 0;
                            curSk.temp = defSk.temp;
                        }
                        break;
                    }
                }
            }
            if (!exists) {
                currentSkills.add(defSk);
            }
        }
    }

    /**
     * Lấy toàn bộ danh sách Kỹ năng chuẩn xịn (Chủ động, Nộ, Nâng cao, Haki, Dial, Passives) cho Bot theo Class.
     */
    public static List<Skill_info> getFullBotSkills(byte clazz, short skillLv) {
        List<Skill_info> list = new ArrayList<>();
        byte botClazz = clazz > 0 ? clazz : 1;

        // 1. Chiêu chủ động cơ bản theo Class (Chiêu 1 -> Chiêu 3)
        int baseIdx = (botClazz - 1) * 60;
        int[] activeSkillIndices = new int[]{
            baseIdx,       // Chiêu 1 (Chiêu thường: 0, 60, 120, 180, 240)
            baseIdx + 20,  // Chiêu 2 (20, 80, 140, 200, 260)
            baseIdx + 40   // Chiêu 3 (40, 100, 160, 220, 280)
        };

        for (int idx : activeSkillIndices) {
            Skill_info sk = createBotSkillInfo(idx);
            if (sk != null) list.add(sk);
        }

        // 2. Chiêu Nộ / Tuyệt Kỹ Class
        int ultiIdx = switch (botClazz) {
            case 1 -> 566;
            case 2 -> 584;
            case 3 -> 602;
            case 4 -> 620;
            case 5 -> 638;
            default -> 566;
        };
        Skill_info ultiSk = createBotSkillInfo(ultiIdx);
        if (ultiSk != null) list.add(ultiSk);

        // 3. Chiêu chủ động nâng cao / Mở rộng theo Class
        int[] extraIndices = switch (botClazz) {
            case 1 -> new int[]{703, 708, 713};
            case 2 -> new int[]{718, 723, 728};
            case 3 -> new int[]{733, 738, 743};
            case 4 -> new int[]{748, 753, 758};
            case 5 -> new int[]{763, 768, 773};
            default -> new int[]{703, 708, 713};
        };
        for (int idx : extraIndices) {
            Skill_info sk = createBotSkillInfo(idx);
            if (sk != null) list.add(sk);
        }

        // 4. Haki Skills (Bá Vương, Vũ Trang, Quan Sát)
        int[] hakiIndices = new int[]{672, 679, 685};
        for (int idx : hakiIndices) {
            Skill_info sk = createBotSkillInfo(idx);
            if (sk != null) list.add(sk);
        }

        // 5. Dial & Phe Phái
        int[] factionIndices = new int[]{661, 691, 697};
        for (int idx : factionIndices) {
            Skill_info sk = createBotSkillInfo(idx);
            if (sk != null) list.add(sk);
        }

        // 6. Chiêu Bị Động (Passives)
        int[] commonPassives = new int[]{300, 305, 310, 315, 320, 325, 552, 557, 667};
        int classPassive1 = 0, classPassive2 = 0;
        switch (botClazz) {
            case 1: classPassive1 = 375; classPassive2 = 487; break;
            case 2: classPassive1 = 395; classPassive2 = 492; break;
            case 3: classPassive1 = 415; classPassive2 = 497; break;
            case 4: classPassive1 = 435; classPassive2 = 502; break;
            case 5: classPassive1 = 455; classPassive2 = 507; break;
        }
        for (int idx : commonPassives) {
            Skill_info sk = createBotSkillInfo(idx);
            if (sk != null) list.add(sk);
        }
        for (int idx : new int[]{classPassive1, classPassive2}) {
            if (idx > 0) {
                Skill_info sk = createBotSkillInfo(idx);
                if (sk != null) list.add(sk);
            }
        }

        return list;
    }

    /**
     * Khởi tạo và nâng cấp toàn bộ Kỹ năng chuẩn xịn cho Bot (dùng 'this' trực tiếp trên Player).
     * Nâng cấp đầy đủ từ Lv_RQ = -1 -> 1 -> ... -> targetSkillLv để gán sk.temp lên Skill_Template cấp cao,
     * khắc phục triệt để lỗi bot có skill xịn nhưng dùng ra hiệu ứng/chỉ số Lv 1.
     */
    public void setupBotSkills(byte clazz, short skillLv, double tier) {
        setupBotSkills(clazz, skillLv, tier, 0);
    }

    public void setupBotSkills(byte clazz, short skillLv, double tier, int fruitItemId) {
        int currency = (tier >= 3.5) ? 1 : (tier >= 2.5 ? 2 : 3);
        short scaledSkillLv = skillLv;
        if (skillLv >= 20 || this.level >= 20) {
            scaledSkillLv = (short) Math.max(20, Math.min(30, (int) Math.round(skillLv * (0.8 + (tier * 0.1)))));
        } else {
            scaledSkillLv = (short) Math.max(5, Math.min(20, (int) Math.round(skillLv * (0.7 + (tier * 0.1)))));
        }
        setupBotSkills(clazz, scaledSkillLv, currency, fruitItemId);
    }

    public void setupBotSkills(byte clazz, short skillLv, int currency) {
        setupBotSkills(clazz, skillLv, currency, 0);
    }

    public void setupBotSkills(byte clazz, short skillLv, int currency, int fruitItemId) {
        byte botClazz = clazz > 0 ? clazz : 1;
        this.skill_point = getFullBotSkills(botClazz, skillLv);
        if (this.skill_point == null) {
            this.skill_point = new ArrayList<>();
        }

        short targetSkillLv = (short) Math.max(1, Math.min(30, (int) skillLv));
        if (this.level >= 20 && targetSkillLv < 20) {
            targetSkillLv = (short) (20 + (this.level >= 80 ? 10 : this.level >= 50 ? 5 : 0));
        }

        for (skill.Skill_info sk : this.skill_point) {
            if (sk == null || sk.temp == null) continue;

            // 1. Học skill nếu chưa học (Lv_RQ == -1)
            if (sk.temp.Lv_RQ == -1) {
                skill.Skill_Template.learn_skill(sk);
            }

            // 2. Nâng cấp skill từng bước lên targetSkillLv để cập nhật sk.temp tương ứng
            if (sk.temp != null && sk.temp.Lv_RQ > 0) {
                int startLv = sk.temp.Lv_RQ;
                for (int lvl = startLv; lvl < targetSkillLv; lvl++) {
                    if (!skill.Skill_Template.upgrade_skill(sk, botClazz)) {
                        break;
                    }
                }
            }

            // 3. Đặt EXP tương ứng với Lv_RQ của skill đã nâng cấp
            int expIdx = Math.min(39, Math.max(0, (sk.temp != null && sk.temp.Lv_RQ > 0) ? sk.temp.Lv_RQ - 1 : targetSkillLv - 1));
            sk.exp = skill.Skill_info.EXP[expIdx];

            // 4. Đặt cấp Cường Hóa Ác Quỷ (lvdevil) cho kỹ năng
            sk.lvdevil = (byte) (currency == 1 || targetSkillLv >= 20 ? 5 : (currency == 2 || targetSkillLv >= 10 ? 3 : 2));
            sk.devilpercent = 100;
        }

        // 5. Tự động gán ĐÚNG 1 bộ Trái Ác Quỷ đại diện cho Bot
        int devilLv = (currency == 1 || targetSkillLv >= 20) ? 5 : (currency == 2 || targetSkillLv >= 10) ? 3 : 2;
        setupBotDevilFruit(fruitItemId, devilLv);

        // 6. Đăng ký tất cả kỹ năng chủ động vào time_use_skill với cooldown 0 để bot dùng được ngay
        if (this.time_use_skill != null && this.skill_point != null) {
            for (skill.Skill_info sk : this.skill_point) {
                if (sk != null && sk.temp != null) {
                    this.time_use_skill.put(sk.temp.ID, 0L);
                }
            }
        }
    }

    public static final int[] BOT_TOP_TIER_FRUITS = {
        90,  // Nika Thức Tỉnh (Sun God Nika)
        161, // Dragon (Rồng Kaido)
        160, // Phoenix (Phượng Hoàng Marco)
        32,  // Magma (Nham Thạch Akainu)
        33,  // Ice (Băng Giá Aokiji)
        93,  // Gura (Chấn Động Râu Trắng)
        427, // Dark (Bóng Tối Râu Đen)
        317  // Yamato
    };

    public static final int[] BOT_MID_TIER_FRUITS = {
        88,  // Flame (Lửa Sabo / Ace)
        34,  // Smoke / Light (Khói / Quang Tốc Kizaru)
        91,  // Rumble (Sấm Sét Enel)
        92,  // Sand (Cát Crocodile)
        316, // Mochi (Katakuri)
        240  // Barrier (Rào Chắn Bartolomeo)
    };

    public static final int[] BOT_BASIC_TIER_FRUITS = {
        220, // Spring (Lò Xo Bellamy)
        318, // Weight (Trọng Lượng)
        219  // Bomb (Bộc Phá Mr.5)
    };

    /**
     * Tự động chọn và gán Trái Ác Quỷ chuẩn cùng hệ thống Skill Nội Tại & Chủ Động cho Bot.
     * @param fruitItemId ID vật phẩm Trái Ác Quỷ. Tự động chọn theo phân cấp nếu <= 0.
     * @param maxLvDevil Cấp độ Cường Hóa Ác Quỷ (1..5).
     */
    public void setupBotDevilFruit(int fruitItemId, int maxLvDevil) {
        try {
            byte targetLvDevil = (byte) Math.max(1, Math.min(5, maxLvDevil));
            if (fruitItemId <= 0) {
                if (targetLvDevil >= 5) {
                    fruitItemId = BOT_TOP_TIER_FRUITS[core.ZUtil.random(BOT_TOP_TIER_FRUITS.length)];
                } else if (targetLvDevil >= 3) {
                    fruitItemId = BOT_MID_TIER_FRUITS[core.ZUtil.random(BOT_MID_TIER_FRUITS.length)];
                } else {
                    fruitItemId = BOT_BASIC_TIER_FRUITS[core.ZUtil.random(BOT_BASIC_TIER_FRUITS.length)];
                }
            }

            // Gán các chiêu chủ động / nội tại của Trái Ác Quỷ
            int reqFruitId = fruitItemId > 4000 ? fruitItemId : fruitItemId + 4000;
            this.get_skill_taq_new(reqFruitId);

            if (this.skill_point != null) {
                for (skill.Skill_info sk : this.skill_point) {
                    if (sk != null && sk.temp != null && sk.temp.ID > 2000) {
                        if (sk.temp.Lv_RQ == -1) {
                            skill.Skill_Template.learn_skill(sk);
                        }
                        if (sk.temp.Lv_RQ > 0) {
                            for (int lvl = sk.temp.Lv_RQ; lvl < 15; lvl++) {
                                skill.Skill_Template.upgrade_skill(sk, this.clazz);
                            }
                        }
                        sk.lvdevil = targetLvDevil;
                        sk.devilpercent = 100;
                    }
                }
            }
        } catch (Exception e) {
            core.Log.error("Player", "Lỗi setupBotDevilFruit: " + e.getMessage());
        }
    }

    public void updateParts() {
        if (this.item != null && this.item.it_body != null) {
            if (this.item.it_body[0] != null && this.item.it_body[0].template != null) {
                template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(this.item.it_body[0].template.id);
                if (temp != null) this.part_weapon = temp.part;
            } else {
                this.part_weapon = -1;
            }
            if (this.item.it_body[3] != null && this.item.it_body[3].template != null) {
                template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(this.item.it_body[3].template.id);
                if (temp != null) this.part_body = temp.part;
            } else {
                this.part_body = -1;
            }
            if (this.item.it_body[5] != null && this.item.it_body[5].template != null) {
                template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(this.item.it_body[5].template.id);
                if (temp != null) this.part_leg = temp.part;
            } else {
                this.part_leg = -1;
            }
            if (this.item.it_body[4] != null && this.item.it_body[4].template != null) {
                template.ItemTemplate3 temp = template.ItemTemplate3.get_it_by_id(this.item.it_body[4].template.id);
                if (temp != null) this.part_ring = temp.part;
            } else {
                this.part_ring = -1;
            }
        }
        // Fallback default body & leg parts based on 5 clazz (matches CreateChar_Screen) if missing (-1)
        if (this.part_body == -1) {
            switch (this.clazz) {
                case 1: this.part_body = 3; break;
                case 2: this.part_body = 26; break;
                case 3: this.part_body = 30; break;
                case 4: this.part_body = 34; break;
                case 5: this.part_body = 38; break;
                default: this.part_body = 3; break;
            }
        }
        if (this.part_leg == -1) {
            switch (this.clazz) {
                case 1: this.part_leg = 4; break;
                case 2: this.part_leg = 27; break;
                case 3: this.part_leg = 31; break;
                case 4: this.part_leg = 35; break;
                case 5: this.part_leg = 39; break;
                default: this.part_leg = 4; break;
            }
        }
        if (this.part_weapon == -1) {
            switch (this.clazz) {
                case 1: this.part_weapon = -1; break;
                case 2: this.part_weapon = 5; break;
                case 3: this.part_weapon = 180; break;
                case 4: this.part_weapon = 6; break;
                case 5: this.part_weapon = 7; break;
                default: this.part_weapon = -1; break;
            }
        }
        // Head is 0 (base face part 0) by default for ALL classes if unassigned (<0) or corrupted (1..4)
        if (this.head < 0 || (this.head >= 1 && this.head <= 4)) {
            this.head = 0;
        }
        // Auto-fix hair per class if missing/invalid (<=0)
        if (this.hair <= 0) {
            switch (this.clazz) {
                case 1: this.hair = 1; break;
                case 2: this.hair = 24; break;
                case 3: this.hair = 28; break;
                case 4: this.hair = 32; break;
                case 5: this.hair = 36; break;
                default: this.hair = 1; break;
            }
        }
    }

    /**
     * Thiết lập Ngoại Hình (Head/Hair/Part/Fashion/Boat) chuẩn xịn cho Bot theo Class và Bậc (Tier).
     */
    public void setupBotAppearance(byte clazz, double tier) {
        this.itfashionP = new ArrayList<>();
        this.fashion    = new ArrayList<>();
        this.itemboat   = new ArrayList<>();
        this.is_show_hat = true;

        byte botClazz = (clazz >= 1 && clazz <= 5) ? clazz : 1;

        // 0. ĐẶT MẶT (HEAD) - Mặc định Part 0 (Mặt Trái Xoan) cho tất cả class
        this.head = 0;

        // 1. TÓC CHUẨN XỊN THEO CLASS (Category 103)
        int defaultHairId, defaultHairIcon;
        switch (botClazz) {
            case 1: defaultHairId = 5; defaultHairIcon = 1;  break;  // Võ sĩ (Luffy hair)
            case 2: defaultHairId = 1; defaultHairIcon = 24; break;  // Kiếm sĩ (Zoro hair)
            case 3: defaultHairId = 2; defaultHairIcon = 28; break;  // Đầu bếp (Sanji hair)
            case 4: defaultHairId = 3; defaultHairIcon = 32; break;  // Bác sĩ (Chopper hair)
            case 5: defaultHairId = 4; defaultHairIcon = 36; break;  // Xạ thủ (Usopp hair)
            default: defaultHairId = 1; defaultHairIcon = 1; break;
        }

        this.hair = (short) defaultHairIcon;
        this.itfashionP.add(new template.ItemFashionP((short) defaultHairId, (short) defaultHairIcon, (byte) 103, true));

        // 2. NÓN / PHỤ KIỆN ĐẦU (Category 108) - Chỉ thêm nếu tier >= 1.5
        if (tier >= 1.5 && template.ItemHair.ENTRYS != null) {
            List<template.ItemHair> headList = new ArrayList<>();
            for (template.ItemHair h : template.ItemHair.ENTRYS) {
                if (h != null && h.type == 108) {
                    headList.add(h);
                }
            }
            if (!headList.isEmpty() && core.ZUtil.random(100) < 60) {
                template.ItemHair selHead = headList.get(core.ZUtil.random(headList.size()));
                this.itfashionP.add(new template.ItemFashionP(selHead.ID, selHead.idIcon, 108, true));
            }
        }

        // 3. THỜI TRANG (ItemFashionP2) - Bộ trang phục/cánh/skin
        if (template.ItemFashion.ENTRYS != null && !template.ItemFashion.ENTRYS.isEmpty()) {
            int outfitChance = (tier <= 1.0) ? 40 : (tier <= 2.0) ? 75 : 95;
            if (core.ZUtil.random(100) < outfitChance) {
                template.ItemFashion randomFashion = template.ItemFashion.ENTRYS.get(
                    core.ZUtil.random(template.ItemFashion.ENTRYS.size()));
                if (randomFashion != null) {
                    template.ItemFashionP2 f2 = new template.ItemFashionP2();
                    f2.id = randomFashion.ID;
                    f2.is_use = true;
                    f2.level = (byte) Math.min(10, (int) Math.round(tier * 2.5));
                    f2.expires = -1; // Vĩnh viễn
                    this.fashion.add(f2);
                }
            }
        }

        // 4. THUYỀN / VÁN LƯỚT (ItemBoatP)
        byte[][] boatSets = new byte[][]{
            {9, 10, 11, 12},  // Thuyền Gỗ
            {2, 4, 6, 8},     // Thuyền Rồng
            {1, 3, 5, 7}      // Thuyền Vàng
        };
        int setIdx = (tier >= 2.5) ? (core.ZUtil.random(100) < 60 ? 2 : 1)
                   : (tier >= 1.5 ? core.ZUtil.random(2) + 1 : 0);
        setIdx = Math.max(0, Math.min(setIdx, boatSets.length - 1));
        byte[] chosenBoat = boatSets[setIdx];
        for (byte bId : chosenBoat) {
            template.ItemBoatP ib = new template.ItemBoatP();
            ib.id = bId;
            ib.is_use = true;
            this.itemboat.add(ib);
        }

        // 5. Sync parts (body/leg/ring/weapon)
        updateParts();
    }

    private void setDefaultBotHair(byte clazz) {
        int id = 0, icon = 0;
        switch (clazz) {
            case 1: id = 5; icon = 1; break;
            case 2: id = 1; icon = 24; break;
            case 3: id = 2; icon = 28; break;
            case 4: id = 3; icon = 32; break;
            case 5: id = 4; icon = 36; break;
            default: id = 1; icon = 24; break;
        }
        this.itfashionP.add(new template.ItemFashionP((short) id, (short) icon, (byte) 103, true));
        this.hair = (short) icon;
    }

    /**
     * Tự động phân bổ điểm tiềm năng (pointAttribute) cho Bot hoặc Người chơi dựa theo Class.
     * Quy tắc chuẩn xịn:
     *  - Võ sĩ (Class 1): 70% Point 1 (Sức mạnh/Cận chiến), 30% Point 3 (Thế lực/Máu HP)
     *  - Kiếm sĩ (Class 2): 70% Point 1 (Sức mạnh/Kiếm), 30% Point 4 (Chí mạng/Tốc độ)
     *  - Đầu bếp (Class 3): 50% Point 1 (Sức mạnh), 50% Point 3 (Máu HP)
     *  - Bác sĩ (Class 4): 50% Point 2 (Tinh thần/Kháng), 50% Point 3 (Máu HP)
     *  - Xạ thủ (Class 5): 70% Point 1 (Sức mạnh/Tầm xa), 30% Point 5 (Né tránh/Phản xạ)
     */
    public void autoAllocatePotentialPoints(byte clazz) {
        int totalAttr = template.Level.get_total_point_by_level(this.level > 0 ? this.level : 1);
        this.pointAttribute = 0;

        byte bClazz = (clazz >= 1 && clazz <= 5) ? clazz : 1;
        long p1Alloc = 1, p2Alloc = 1, p3Alloc = 1, p4Alloc = 1, p5Alloc = 1;
        long allocable = Math.max(0, totalAttr - 4);

        // Kiểm tra xem đã có dữ liệu tỷ lệ cộng điểm học được từ người chơi thật chưa
        double[] learnedRatios = bot.botplayer.GameAnalyzer.getLearnedAttributeRatio(bClazz);
        if (learnedRatios != null && learnedRatios.length == 5) {
            p1Alloc = Math.min(80, 1 + (long)(allocable * learnedRatios[0]));
            p2Alloc = Math.min(80, 1 + (long)(allocable * learnedRatios[1]));
            p3Alloc = Math.min(80, 1 + (long)(allocable * learnedRatios[2]));
            p4Alloc = Math.min(80, 1 + (long)(allocable * learnedRatios[3]));
            p5Alloc = Math.min(80, 1 + (long)(allocable * learnedRatios[4]));
        } else {
            // Mẫu build chuẩn theo Class khi chưa có đủ dữ liệu học
            switch (bClazz) {
                case 1: { // Võ sĩ: 70% Point 1, 30% Point 3
                    long mainAdd = (long) (allocable * 0.70);
                    long subAdd = allocable - mainAdd;
                    p1Alloc = Math.min(80, 1 + mainAdd);
                    p3Alloc = Math.min(80, 1 + subAdd);
                    break;
                }
                case 2: { // Kiếm sĩ: 70% Point 1, 30% Point 4
                    long mainAdd = (long) (allocable * 0.70);
                    long subAdd = allocable - mainAdd;
                    p1Alloc = Math.min(80, 1 + mainAdd);
                    p4Alloc = Math.min(80, 1 + subAdd);
                    break;
                }
                case 3: { // Đầu bếp: 50% Point 1, 50% Point 3
                    long mainAdd = (long) (allocable * 0.50);
                    long subAdd = allocable - mainAdd;
                    p1Alloc = Math.min(80, 1 + mainAdd);
                    p3Alloc = Math.min(80, 1 + subAdd);
                    break;
                }
                case 4: { // Bác sĩ: 50% Point 2, 50% Point 3
                    long mainAdd = (long) (allocable * 0.50);
                    long subAdd = allocable - mainAdd;
                    p2Alloc = Math.min(80, 1 + mainAdd);
                    p3Alloc = Math.min(80, 1 + subAdd);
                    break;
                }
                case 5: { // Xạ thủ: 70% Point 1, 30% Point 5
                    long mainAdd = (long) (allocable * 0.70);
                    long subAdd = allocable - mainAdd;
                    p1Alloc = Math.min(80, 1 + mainAdd);
                    p5Alloc = Math.min(80, 1 + subAdd);
                    break;
                }
            }
        }

        this.point1 = p1Alloc;
        this.point2 = p2Alloc;
        this.point3 = p3Alloc;
        this.point4 = p4Alloc;
        this.point5 = p5Alloc;

        if (this.ability != null) {
            long hpMax = this.ability.get_hp_max(true);
            long mpMax = this.ability.get_mp_max(true);
            if (this.hp > hpMax || this.hp <= 0) this.hp = (int) hpMax;
            if (this.mp > mpMax || this.mp <= 0) this.mp = (int) mpMax;
        }
    }

    /**
     * Tự động cân bằng toàn diện Bot theo chỉ số, phẩm chất trang bị (Hoàn mỹ, Khảm đá, Kích ẩn) và sức mạnh người chơi thật.
     */
    public void setupBotBalancedAgainst(Player targetPlayer) {
        if (this instanceof bot.Bot) {
            bot.BotBalanceEngine.balanceAgainstPlayer((bot.Bot) this, targetPlayer);
        }
    }

    /**
     * Tự động hồi sinh Bot tại làng/bản đồ mặc định chuẩn 100%.
     */
    public void autoReviveAtVillage() {
        if (!this.isdie) return;
        this.isdie = false;
        if (this.ability != null) {
            this.hp = (int) this.ability.get_hp_max(true);
            this.mp = (int) this.ability.get_mp_max(true);
        }
        if (this.type_pk == WorldWar.TYPE_PK_HAI_TAC
                || this.type_pk == WorldWar.TYPE_PK_HAI_QUAN
                || this.type_pk == WorldWar.TYPE_PK_QUAN_CACH_MANG) {
            this.type_pk = -1;
        }
    }

    /**
     * Tìm quái sống gần nhất trong phạm vi maxDistance.
     */
    public mob.Mob findNearestMob(int maxDistance) {
        if (this.map == null || this.map.mobs == null) return null;
        mob.Mob best = null;
        double minDst = maxDistance;
        synchronized (this.map.mobs) {
            for (mob.Mob m : this.map.mobs.values()) {
                if (m == null || m.isdie || m.hp <= 0) continue;
                double dst = Math.hypot(m.x - this.x, m.y - this.y);
                if (dst < minDst) {
                    minDst = dst;
                    best = m;
                }
            }
        }
        return best;
    }

    /**
     * Tìm người chơi / bot địch gần nhất có thể tấn công trong phạm vi maxDistance.
     */
    public Player findNearestHostilePlayer(int maxDistance) {
        if (this.map == null || this.map.players == null) return null;
        Player best = null;
        double minDst = maxDistance;
        synchronized (this.map.players) {
            for (Player p : this.map.players) {
                if (p == null || p.equals(this) || p.isdie) continue;
                if (!canAttackTargetPlayer(p)) continue;
                double dst = Math.hypot(p.x - this.x, p.y - this.y);
                if (dst < minDst) {
                    minDst = dst;
                    best = p;
                }
            }
        }
        return best;
    }

    /**
     * Kiểm tra xem có thể tấn công player/bot p hay không (chế độ PK, Bang, Hải Tặc vs Hải Quân, Đồ sát).
     */
    public boolean canAttackTargetPlayer(Player p) {
        if (p == null || p.equals(this) || p.isdie || p.map == null || this.map == null) return false;
        if (this.isSpectator || p.isSpectator) return false;

        // Tuyệt đối không PK trong map Làng / Khu vực an toàn (trừ khi có ngoại lệ)
        if (Zone.isMapLang(this.map.template.id)) {
            return false;
        }
        // Trong map đang chờ bắt đầu trận / đếm ngược
        if (Zone.isWaitingOrUnstartedMatch(this.map) || (p.map != null && Zone.isWaitingOrUnstartedMatch(p.map))) {
            return false;
        }

        // Bảo vệ tân thủ / Hồi sinh (eff 7) và chưa bật cờ (type_pk == -1) -> Không thể bị tấn công và không thể tấn công người khác
        if ((p.get_eff(7) != null && p.type_pk == -1 && !this.isBot) || (this.get_eff(7) != null && this.type_pk == -1 && !this.isBot)) {
            return false;
        }

        // Mục tiêu đang bất tử (eff 9 - từ chối tử thần, eff 300 - khiên miễn nhiễm)
        if (p.get_eff(9) != null || p.get_eff(300) != null) {
            return false;
        }

        // Thách đấu cá nhân / Cừu hận PvP 1v1 (Chỉ được đánh khi đã vào MapPvp / Lôi đài và trận đấu đã bắt đầu status_pvp == 3)
        if (this.targetFight != null && this.targetFight.equals(p) && p.targetFight != null && p.targetFight.equals(this)) {
            if (this.map.map_vp != null) {
                return this.map.map_vp.status_pvp == 3;
            }
            return false;
        }

        // Kiểm tra quan hệ Chủ nhân / Đệ tử / Lính đánh thuê — Tuyệt đối không tấn công đồng đội
        int myOwnerId = this.IDPlayer;
        if (this instanceof DeTu && ((DeTu) this).master != null) {
            myOwnerId = ((DeTu) this).master.IDPlayer;
        } else if (this instanceof bot.mercenary.MercenaryBot) {
            myOwnerId = ((bot.mercenary.MercenaryBot) this).ownerPlayerId;
        }

        int targetOwnerId = p.IDPlayer;
        if (p instanceof DeTu && ((DeTu) p).master != null) {
            targetOwnerId = ((DeTu) p).master.IDPlayer;
        } else if (p instanceof bot.mercenary.MercenaryBot) {
            targetOwnerId = ((bot.mercenary.MercenaryBot) p).ownerPlayerId;
        }

        if (myOwnerId == targetOwnerId) {
            return false;
        }

        // Map Lôi Đài / Đấu Trường PvP
        if (this.map.map_vp != null) {
            if (this.map.map_vp.status_pvp != 3) {
                return false; // Chưa bắt đầu trận đấu / đang đếm ngược -> Không được đánh
            }
            if (this.type_pk >= 4 && p.type_pk >= 4) {
                return this.type_pk != p.type_pk;
            }
            return true;
        }

        // Tuyệt đối không tấn công đồng đội cùng Clan trong các phó bản & map thế giới
        if (this.clan != null && p.clan != null) {
            if (this.clan.id == p.clan.id || (this.clan.name != null && this.clan.name.equalsIgnoreCase(p.clan.name))) {
                return false;
            }
        }

        // 1. Phó bản Khổng Lồ (Little Garden / Map 81)
        if (this.map.map_little_garden != null || this.map.template.id == 81) {
            // Yêu cầu nghiêm ngặt: Cả 2 bên bắt buộc phải có cờ PK hợp lệ (Phe 4 hoặc Phe 5) mới được đánh nhau
            if (this.type_pk < 4 || this.type_pk > 5 || p.type_pk < 4 || p.type_pk > 5) {
                return false;
            }
            if (this.type_pk == 4 && p.type_pk == 5) return true;
            if (this.type_pk == 5 && p.type_pk == 4) return true;
            return false;
        }

        // 2. Phó bản Thủ Lĩnh Biển Khơi (Map 178..184)
        if (this.map.template != null && this.map.template.id >= 178 && this.map.template.id <= 184) {
            int mid = this.map.template.id;
            // Map 179 (Sảnh chiến), 180 (Hang quái), 184 (Kho báu): Cho phép PK giữa các phe khác nhau (4..7)
            if (mid == 179 || mid == 180 || mid == 184) {
                if (this.type_pk == -1 || p.type_pk == -1) return false;
                if (this.type_pk >= 4 && this.type_pk <= 7 && p.type_pk >= 4 && p.type_pk <= 7) {
                    return this.type_pk != p.type_pk;
                }
                if (this.clan != null && p.clan != null && !this.clan.equals(p.clan)) {
                    return true;
                }
                return false;
            }
            // Các map biển riêng (178, 181, 182, 183): Khu an toàn clan, không PK
            return false;
        }

        // 3. Bảo Vệ Pháo Đài (Map 267..271)
        if (this.map.baoVePhaoDai != null || this.map.IsMapBaoVePhaoDai()) {
            if (this.type_pk == 4 && p.type_pk == 5) return true;
            if (this.type_pk == 5 && p.type_pk == 4) return true;
            if (this.type_pk >= 4 && p.type_pk >= 4 && this.type_pk != p.type_pk) return true;
            if (this.clan != null && p.clan != null && !this.clan.equals(p.clan) && this.type_pk != -1 && p.type_pk != -1) return true;
            return false;
        }

        // 4. Map Phó bản PvP Băng (Map 120, pvpBangMapFight)
        if (this.map.pvpBangMapFight != null || (this.map.template != null && this.map.template.id == 120)) {
            if (this.map.pvpBangMapFight != null && this.map.pvpBangMapFight.status_pvp != 3) {
                return false; // Chưa bắt đầu chiến đấu -> Không cho đánh
            }
            if (this.type_pk == 4 && p.type_pk == 5) return true;
            if (this.type_pk == 5 && p.type_pk == 4) return true;
            if (this.type_pk == 14 && p.type_pk == 15) return true;
            if (this.type_pk == 15 && p.type_pk == 14) return true;
            if (this.type_pk >= 4 && p.type_pk >= 4 && this.type_pk != p.type_pk) return true;
            if (this.clan != null && p.clan != null && !this.clan.equals(p.clan)) return true;
            return false;
        }

        // 5. Chiếm Đảo Bang Hội (Map 254..258, 261..265, map_dungeon instanceof ChiemDao)
        if ((this.map.template != null && ((this.map.template.id >= 254 && this.map.template.id <= 258) || (this.map.template.id >= 261 && this.map.template.id <= 265)))
                || this.map.map_dungeon instanceof ChiemDao) {
            if (this.type_pk == -1 || p.type_pk == -1) return false;
            // Khác cờ PK -> Đánh nhau
            if (this.type_pk != p.type_pk) return true;
            // Cùng cờ PK nhưng khác bang -> Đánh nhau tranh cướp đảo
            if (this.clan != null && p.clan != null && !this.clan.equals(p.clan)) return true;
            return false;
        }

        // 8. Đại Chiến Thế Giới / World War (type_pk 11, 12, 13: Hải Tặc, Hải Quân, Cách Mạng)
        if (this.type_pk >= 11 && this.type_pk <= 13 && p.type_pk >= 11 && p.type_pk <= 13) {
            return this.type_pk != p.type_pk;
        }

        // 9. Bot Truy Nã / Bot PvP (chỉ tấn công đúng mục tiêu được giao)
        if (this instanceof bot.BotTruyNa && ((bot.BotTruyNa) this).pTarget != null && ((bot.BotTruyNa) this).pTarget.equals(p)) {
            return true;
        }
        if (p instanceof bot.BotTruyNa && ((bot.BotTruyNa) p).pTarget != null && ((bot.BotTruyNa) p).pTarget.equals(this)) {
            return true;
        }

        // 10. Đồ sát (type_pk == 0): Người bật đồ sát có thể đánh bất kỳ ai và bị người khác đánh
        if (this.type_pk == 0 || p.type_pk == 0) {
            return true;
        }

        // 11. Tội nhân / Quỷ đỏ (type_pk == 1): Bị tất cả người chơi tấn công
        if (p.type_pk == 1) {
            return true;
        }

        // 12. Cờ Bang Hội (type_pk == 2):
        if (this.type_pk == 2 && p.type_pk == 2) {
            if (this.clan != null && p.clan != null) {
                return !this.clan.equals(p.clan);
            }
            return true; // Khác bang
        }

        // 13. Cờ Phe Tự Do / Loạn đấu (type_pk == 3):
        if (this.type_pk == 3 && p.type_pk == 3) {
            return true;
        }
        if (this.type_pk == 3 && p.type_pk >= 4 && p.type_pk <= 10) {
            return true;
        }
        if (p.type_pk == 3 && this.type_pk >= 4 && this.type_pk <= 10) {
            return true;
        }

        // 14. Cờ màu (type_pk 4..10): Khác màu cờ thì đánh nhau
        if (this.type_pk >= 4 && this.type_pk <= 10 && p.type_pk >= 4 && p.type_pk <= 10) {
            return this.type_pk != p.type_pk;
        }

        // 15. Phe Hải Tặc (1) vs Hải Quân (2) — Chỉ khi cả 2 đều gia nhập phe và cùng bật cờ phe
        if (this.typePirate > 0 && p.typePirate > 0 && this.typePirate != p.typePirate) {
            if (this.type_pk != -1 && p.type_pk != -1) {
                return true;
            }
        }

        // Mặc định: type_pk == -1 (Hòa bình / train quái) -> Tuyệt đối không thể tấn công
        return false;
    }

    public void update_eff() throws IOException {
        if (this.timeHuyMbv != -1 && this.timeHuyMbv < System.currentTimeMillis()) {
            this.timeHuyMbv = -1;
            this.mbv = "";
            this.passBagOK = true;
        }
        long now = System.currentTimeMillis();
        List<EffTemplate> list_temp = null;
        if (this.list_eff != null && !this.list_eff.isEmpty()) {
            for (EffTemplate eff : this.list_eff) {
                if (eff != null && eff.time < now) {
                    if (list_temp == null) list_temp = new ArrayList<>(2);
                    list_temp.add(eff);
                }
            }
            if (list_temp != null) {
                this.list_eff.removeAll(list_temp);
                if (this.ability != null) {
                    this.ability.recalculatePlayerStats(this);
                }
            }
        }
        if (this.active_buffs != null && !this.active_buffs.isEmpty()) {
            this.active_buffs.removeIf(b -> b == null || b.expireTime <= now);
        }

        if (detu != null && detu.list_eff != null && !detu.list_eff.isEmpty()) {
            List<EffTemplate> list_temp_DT = null;
            for (EffTemplate eff : detu.list_eff) {
                if (eff != null && eff.time < now) {
                    if (list_temp_DT == null) list_temp_DT = new ArrayList<>(2);
                    list_temp_DT.add(eff);
                }
            }
            if (list_temp_DT != null) {
                detu.list_eff.removeAll(list_temp_DT);
                for (int i = 0; i < list_temp_DT.size(); i++) {
                    if (list_temp_DT.get(i).id == 7) {
                        try {
                            Message m2 = new Message(-71);
                            m2.writer().writeByte(1);
                            m2.writer().writeShort(detu.index_map);
                            m2.writer().writeByte(0);
                            m2.writer().writeInt(1); // time
                            if (conn != null && conn.p != null && conn.p.map != null) {
                                conn.p.map.send_msg_all_p(m2, conn.p, true);
                            }
                            m2.cleanup();
                            detu.update_info_to_all();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        //
        if (list_temp != null) {
            for (int i = 0; i < list_temp.size(); i++) {
                if (list_temp.get(i).id == 7) {
                    Message m2 = new Message(-71);
                    m2.writer().writeByte(1);
                    m2.writer().writeShort(conn.p.index_map);
                    m2.writer().writeByte(0);
                    m2.writer().writeInt(1); // time
                    conn.p.map.send_msg_all_p(m2, conn.p, true);
                    m2.cleanup();
                    this.update_info_to_all();
                } else if (list_temp.get(i).id == 50) {
                    this.setIsHsLol(0);
                } else if (list_temp.get(i).id == 19) {
                    if (this.map.template.id == 1000) {
                        if (this.pvp_target != null && !this.pvp_target.equals(this)
                                && !this.pvp_accept) {
                            Vgo vgo = new Vgo();
                            vgo.map_go = Zone.getMapByID(this.id_map_save > 0 ? this.id_map_save : 1);
                            if (vgo.map_go != null && vgo.map_go.length > 0 && vgo.map_go[0] != null && vgo.map_go[0].template != null) {
                                for (int i1 = 0; i1 < vgo.map_go[0].template.npcs.size(); i1++) {
                                    Npc npc_temp = vgo.map_go[0].template.npcs.get(i1);
                                    if (npc_temp.namegt.equals("Bản đồ") || npc_temp.namegt.equals("Zosaku") || (npc_temp.name != null && npc_temp.name.contains("Zosaku"))) {
                                        vgo.xnew = npc_temp.x;
                                        if (npc_temp.y < 250) {
                                            vgo.ynew = (short) (npc_temp.y + 20);
                                        } else {
                                            vgo.ynew = (short) (npc_temp.y - 40);
                                        }
                                        break;
                                    }
                                }
                                if (vgo.xnew == 0 || vgo.ynew == 0) {
                                    vgo.xnew = (short) (vgo.map_go[0].template.maxW / 2);
                                    vgo.ynew = (short) (vgo.map_go[0].template.maxH / 2);
                                }
                            }
                            this.goto_map(vgo);
                        } else if (this.pvp_target != null && !this.pvp_target.equals(this)
                                && this.pvp_accept) {
                            Pvp.show_table(this);
                            Pvp.start_find(this);
                            this.getService().send_box_ThongBao_OK("Đối thủ rời đi, bạn quay lại hàng chờ");
                        }
                    }
                } else if (list_temp.get(i).id == 20 && this.map.map_ThuThachVeThan != null) {
                    this.map.map_ThuThachVeThan.isFinish = true;
                } else if (list_temp.get(i).id == 21) {
                    this.update_info_to_all();
                    //
                    for (int j = 0; j < this.map.players.size(); j++) {
                        getService().charWearing(this.map.players.get(j), false);
                    }
                } else if (list_temp.get(i).id == 13) {
                    this.update_info_to_all();
                }
            }
            if (list_temp.size() > 0) {
                this.send_skill();
                this.update_info_to_all();
                list_temp.clear();
            }
        }
        if (this.isDeOnl && this.detu != null && !this.isSpectator) {
            this.detu.update();
        }
            if (event.EventManager.isActive(9)) {
                //         14,15,16,17
                int time_h = ZUtil.get_local_time_now().getHourOfDay();
                if (time_h == 11 || time_h == 12) {
                    EventData temp_select = null;
                    for (int i2 = 0; i2 < this.eventData.size(); i2++) {
                        if (this.eventData.get(i2).eventID == 9) {
                            temp_select = this.eventData.get(i2);
                            break;
                        }
                    }
                    if (temp_select != null && temp_select.data[SuKienTrongCay.SHOP_SIZE + 14] < 1) {
                        temp_select.data[SuKienTrongCay.SHOP_SIZE + 14]++;
                        temp_select.data[SuKienTrongCay.IDX_GIO_TRAI_CAY] += 3;
                    }
                } else if (time_h == 15 || time_h == 16) {
                    EventData temp_select = null;
                    for (int i2 = 0; i2 < this.eventData.size(); i2++) {
                        if (this.eventData.get(i2).eventID == 9) {
                            temp_select = this.eventData.get(i2);
                            break;
                        }
                    }
                    if (temp_select != null && temp_select.data[SuKienTrongCay.SHOP_SIZE + 16] < 1) {
                        temp_select.data[SuKienTrongCay.SHOP_SIZE + 16]++;
                        temp_select.data[SuKienTrongCay.IDX_GIO_TRAI_CAY] += 3;
                    }
                }
            }
        }

    public void update() {
        try {
            this.map_remove_type = 0;
        if (this.map == null) {
            return;
        }

        // Kiểm tra và tự động cấp Thẻ Quản Lý 872 nếu người chơi chưa có
        if (System.currentTimeMillis() >= this.nextCheckCard872Time) {
            this.nextCheckCard872Time = System.currentTimeMillis() + 5000L;
            checkAndAddSystemCard872();
        }

        // Cập nhật và dọn dẹp các hiệu ứng (buff / potion) hết hạn
        this.update_eff();

        // Kiểm tra hết hạn biến hình Thần Trang
        ThanTrangConfig.checkTransformExpire(this);

        // Xử lý hệ thống Auto cho người chơi (Auto HP/MP, Auto Revive, Auto Loot, Auto Clean)
        activities.AutoManager.updatePlayerAuto(this);
        
        // 1. idEff22 effect packet broadcasting
        int idEff22 = -1;
        if (idEff22 != -1 && this.timeEff2 < System.currentTimeMillis()) {
            this.timeEff2 = System.currentTimeMillis() + 8_000;
            //this.getService().addEffect(this.index_map, (short) idEff22, 8000, (byte) 0, (byte) 10);
        }
        
        // 2. Haki shock choang check
        if (!this.isdie && this.timeCh < System.currentTimeMillis() && this.map != null && !Zone.isMapLang(this.map.template.id)) {
            this.timeCh = System.currentTimeMillis() + 30_000;
            boolean suc = this.ability.getSkillHkChoang() > ZUtil.random(1200);
            if (suc) {
                // Choáng người chơi đối địch trong phạm vi 200 (chỉ khi được phép PK)
                if (this.map.can_PK && this.map.players != null) {
                    List<Player> listNear = new ArrayList<>();
                    for (int j = 0; j < this.map.players.size(); j++) {
                        Player p01 = this.map.players.get(j);
                        if (p01 != null && !p01.equals(this) && !p01.isdie && this.canAttackTargetPlayer(p01)
                                && Math.abs(p01.x - this.x) < 200 && Math.abs(p01.y - this.y) < 200) {
                            listNear.add(p01);
                        }
                    }
                    listNear.forEach(l -> {
                        try {
                            l.add_new_eff(201, 1, 2_000);
                            if (this.map != null) {
                                this.map.getService().send_choang(this, l, 2_000);
                            }
                        } catch (Exception e) {
                        }
                    });
                }
                // Choáng quái vật trong phạm vi 200 (PvE)
                if (this.map.list_mob != null) {
                    for (int j = 0; j < this.map.list_mob.length; j++) {
                        Mob mob = this.map.getMob(this.map.list_mob[j]);
                        if (mob != null && !mob.isdie && Math.abs(mob.x - this.x) < 200 && Math.abs(mob.y - this.y) < 200) {
                            mob.isChoang = true;
                            mob.timeChoang = System.currentTimeMillis() + 2_000;
                            this.map.getService().send_choang_mob(this, mob, 2_000);
                        }
                    }
                }
            }
        }
        
        // 3. HP/MP Buffs (Potions & Food)
        long now = System.currentTimeMillis();
        int hp_buff = 0;
        int mp_buff = 0;
        if (this.mp < 0) {
            this.mp = 0;
        }

        // Potion / Food Buff (ticking every 1000ms)
        if (this.time_potion_tick < now) {
            this.time_potion_tick = now + 1000L;
            EffTemplate eff = this.get_eff(0);
            if (!this.isdie && eff != null && eff.time > now) { // buff hp
                int p_hp = eff.param;
                if (this.clan != null) {
                    int buff_percent = 100;
                    if (this.clan.check_buff(2)) buff_percent += 25;
                    if (this.clan.check_buff(4)) buff_percent += 25;
                    p_hp = (p_hp * buff_percent) / 100;
                }
                hp_buff += p_hp;
            }
            // Đệ tử hồi HP (Eff 25)
            eff = this.get_eff(25);
            if (!this.isdie && eff != null && eff.time > now) {
                hp_buff += eff.param;
            }

            eff = this.get_eff(1);
            if (!this.isdie && eff != null && eff.time > now) { // buff mp
                int p_mp = eff.param;
                if (this.clan != null) {
                    int buff_percent = 100;
                    if (this.clan.check_buff(2)) buff_percent += 25;
                    if (this.clan.check_buff(4)) buff_percent += 25;
                    p_mp = (p_mp * buff_percent) / 100;
                }
                mp_buff += p_mp;
            }
            // Đệ tử hồi MP (Eff 26)
            eff = this.get_eff(26);
            if (!this.isdie && eff != null && eff.time > now) {
                mp_buff += eff.param;
            }
        }
        
        // 4. Area effects / skill buff vuon
        if (this.map != null && !Zone.isMapLang(this.map.template.id)) {
            if (this.get_eff(14) != null) {
                try {
                    Player p_target = null;
                    if (this.map.can_PK && this.map.players != null) {
                        for (int i = 0; i < this.map.players.size(); i++) {
                            Player p0 = this.map.players.get(i);
                            if (p0 != null && !p0.equals(this) && !p0.isdie && this.canAttackTargetPlayer(p0)
                                    && Math.abs(this.x - p0.x) < 250 && Math.abs(this.y - p0.y) < 250) {
                                p_target = p0;
                                break;
                            }
                        }
                    }
                    if (p_target != null) {
                        Message m = new Message(-15);
                        m.writer().writeByte(3);
                        m.writer().writeShort(p_target.index_map);
                        m.writer().writeByte(0);
                        m.writer().writeShort(0);
                        this.map.send_msg_all_p(m, this, true);
                        m.cleanup();
                        //
                        int dame_to_target = this.ability.get_dame(true);
                        dame_to_target = (dame_to_target * (100 - ZUtil.random(10))) / 100;
                        if (p_target.hp - dame_to_target > 0) {
                            p_target.hp -= dame_to_target;
                            p_target.lastAttacker = this;
                            p_target.lastAttackedTime = System.currentTimeMillis();
                            // STATUS_ATTACK: báo đệ tử ai đang tấn công sư phụ
                            if (p_target.detu != null
                                    && p_target.detu.detuStatus == DeTu.STATUS_ATTACK) {
                                p_target.detu.lastAttackerOfMaster = this;
                            }
                        } else {
                            p_target.hp = 1;
                        }
                        //
                        m = new Message(28);
                        m.writer().writeShort(p_target.index_map);
                        m.writer().writeByte(0);
                        m.writer().writeInt(p_target.hp);
                        m.writer().writeInt(p_target.ability.get_hp_max(true));
                        m.writer().writeShort(-1);
                        m.writer().writeShort(-1);
                        this.map.send_msg_all_p(m, this, true);
                        m.cleanup();
                    } else if (this.map.list_mob != null) {
                        List<Mob> list_random = new ArrayList<>();
                        for (int i11 = 0; i11 < this.map.list_mob.length; i11++) {
                            Mob mob = this.map.getMob(Integer.valueOf(this.map.list_mob[i11]));
                            if (mob != null) {
                                if (!mob.isdie && Math.abs(this.x - mob.x) < 200
                                        && Math.abs(this.y - mob.y) < 200) {
                                    list_random.add(mob);
                                }
                            }
                        }
                        if (list_random.size() > 0) {
                            Mob mob_select = list_random.get(ZUtil.random(list_random.size()));
                            Message m = new Message(-15);
                            m.writer().writeByte(3);
                            m.writer().writeShort(mob_select.index);
                            m.writer().writeByte(1);
                            m.writer().writeShort(0);
                            this.map.send_msg_all_p(m, this, true);
                            m.cleanup();
                            //
                            int dame_to_target = this.ability.get_dame(true);
                            dame_to_target = (dame_to_target * (100 - ZUtil.random(10))) / 100;
                            if (mob_select.isSieuTrum && mob_select.hp_max > 0) {
                                int max10 = (int) ((long) mob_select.hp_max * 10L / 100L);
                                if (max10 > 0 && dame_to_target > max10) {
                                    dame_to_target = max10;
                                }
                            }
                            if (mob_select.hp - dame_to_target > 0) {
                                mob_select.hp -= dame_to_target;
                            } else {
                                mob_select.hp = 1;
                            }
                            //
                            m = new Message(28);
                            m.writer().writeShort(mob_select.index);
                            m.writer().writeByte(1);
                            m.writer().writeInt(mob_select.hp);
                            m.writer().writeInt(mob_select.hp_max);
                            m.writer().writeShort(-1);
                            m.writer().writeShort(-1);
                            this.map.send_msg_all_p(m, this, true);
                            m.cleanup();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        // 5. Auto Potion & Natural Regeneration
        long regenInterval = (this.map != null && Zone.isMapLang(this.map.template.id)) ? 2000L : 2500L;
        if (this.time_buff_hp_mp < now) {
            this.time_buff_hp_mp = now + regenInterval;
            int autoHp = this.ability.get_hp_auto_buff(true);
            int autoMp = this.ability.get_mp_auto_buff(true);
            int curHpMax = this.ability.get_hp_max(true);
            int curMpMax = this.ability.get_mp_max(true);

            if (this.map != null && Zone.isMapLang(this.map.template.id)) {
                // Map Làng: Tự hồi phục tự nhiên 5% Max HP/MP (tối thiểu 50)
                autoHp += Math.max(50, curHpMax / 20);
                autoMp += Math.max(50, curMpMax / 20);
            } else {
                // Map thường: Tự hồi phục tự nhiên tối thiểu 2% Max HP/MP (tối thiểu 20)
                autoHp += Math.max(20, curHpMax / 50);
                autoMp += Math.max(20, curMpMax / 50);
            }
            hp_buff += autoHp;
            mp_buff += autoMp;
        }

        // [TÍCH HỢP OPTION 79: Hồi máu cuối mỗi 10s (Marco Set / Tái sinh)]
        if (!this.isdie && this.time_regen_10s < now) {
            this.time_regen_10s = now + 10_000L;
            int op79 = this.ability.total_param_item(79, true);
            if (op79 > 0) {
                int curMax = this.ability.get_hp_max(true);
                int heal10s = (int) ((curMax * (long) Math.min(250, op79)) / 1000L); // Tối đa 25% mỗi 10s
                if (heal10s > 0 && this.hp < curMax && this.get_eff(202) == null) {
                    hp_buff += heal10s;
                }
            }
        }

        int hp_max = this.ability.get_hp_max(true);
        int mp_max = this.ability.get_mp_max(true);
        int sendHp = (!this.isdie && this.hp < hp_max && hp_buff > 0 && this.get_eff(202) == null) ? hp_buff : 0;
        int sendMp = (!this.isdie && this.mp < mp_max && mp_buff > 0) ? mp_buff : 0;
        if (sendHp > 0 || sendMp > 0) {
            if (this instanceof DeTu) {
                this.hp = Math.min(hp_max, this.hp + sendHp);
                this.mp = Math.min(mp_max, this.mp + sendMp);
                Message m = new Message(-83);
                m.writer().writeShort(this.index_map);
                m.writer().writeByte(0);
                m.writer().writeInt(hp_max);
                m.writer().writeInt(this.hp);
                m.writer().writeInt(sendHp);
                m.writer().writeInt(mp_max);
                m.writer().writeInt(this.mp);
                m.writer().writeInt(sendMp);
                if (this.map != null) {
                    this.map.send_msg_all_p(m, this, true);
                }
                m.cleanup();
            } else {
                this.getService().use_potion(sendHp, sendMp);
            }
        }
        
        // 6. Effect 207 hp decrease
        if (this.get_eff(207) != null) {
            int hp_decrease = hp_max / 100;
            if (this.hp - hp_decrease > 0) {
                this.getService().use_potion(0, -hp_decrease);
            }
        }
        
        // 7. Relocation checks (ChiemDao, DaoKhoBau, etc.)
        if (this.map.mapSanTrum == null && ((this.map.template.id >= 261 && this.map.template.id <= 265)
                || (this.map.template.id >= 254 && this.map.template.id <= 258)) && !ChiemDao.isOpen()) {
            this.map_remove_type = 2;
        }
        // [FIX] Kick player ra khỏi map Săn Trùm khi hết thời gian (boss đã chết hoặc timeout)
        if (this.map.mapSanTrum != null && this.map.mapSanTrum.time < System.currentTimeMillis()) {
            this.map_remove_type = 2;
        }
        if (this.map.map_DaoKhoBau != null && this.map.map_DaoKhoBau.time < System.currentTimeMillis()) {
            this.map_remove_type = 2;
        }
        if (this.isdie && this.map.mapSanTrum == null 
                && ((this.map.template.id >= 261 && this.map.template.id <= 265) || (this.map.template.id >= 254 && this.map.template.id <= 258))
                && this.time_hs_little_garden <= System.currentTimeMillis()) {
            respawnChiemDao();
        }
        if (this.map.template.id == 179 && this.clan != null && this.clan.map_create != null) {
            if (this.isdie) {
                this.map_remove_type = 3;
            }
        }
        if ((this.map.template.id >= 178 && this.map.template.id <= 184 && !ThuLinhBienKhoi.isOpen())
                || (this.map.template.id >= 178 && this.map.template.id <= 184 && this.clan == null)) {
            this.map_remove_type = 2;
        }
        
        // 8. Fashion expiration
        for (int i12 = 0; i12 < this.fashion.size(); i12++) {
            long time = this.fashion.get(i12).expires;
            if (time != -1 && time < System.currentTimeMillis()) {
                this.fashion.get(i12).is_use = false;
                if (this.fashion.get(i12).id == 122) {
                    this.tocSuper = 0;
                }
                this.update_info_to_all();
                for (int i14 = 0; i14 < this.map.players.size(); i14++) {
                    this.getService().charWearing(this.map.players.get(i14), false);
                }
                this.getService().UpdateInfoMaincharInfo();
                this.getService().send_box_ThongBao_OK("Thời trang "
                        + ItemFashion.get_item(this.fashion.get(i12).id).name + " đã hết hạn sử dụng");
                this.fashion.remove(i12);
                i12--;
            }
        }
        
        // 9. Pet ship buff
        if (this.ship_pet != null && this.ship_pet.time_buff_hp < System.currentTimeMillis()
                && Zone.isMapLang(this.map.template.id)) {
            this.ship_pet.time_buff_hp = System.currentTimeMillis() + 2000L;
            if (this.ship_pet.hp < this.ship_pet.hp_max) {
                this.ship_pet.hp += 40;
                if (this.ship_pet.hp > this.ship_pet.hp_max) {
                    this.ship_pet.hp = this.ship_pet.hp_max;
                }
                try {
                    Message m = new Message(-83);
                    m.writer().writeShort(this.ship_pet.index_map);
                    m.writer().writeByte(0);
                    m.writer().writeInt(this.ship_pet.hp_max);
                    m.writer().writeInt(this.ship_pet.hp);
                    m.writer().writeInt(50);
                    m.writer().writeInt(this.ship_pet.hp_max);
                    m.writer().writeInt(0);
                    m.writer().writeInt(0);
                    this.map.send_msg_all_p(m, null, true);
                    m.cleanup();
                } catch (IOException e) {
                }
            }
        }
        
        // 10. Title effects
        if (this.timeEff < System.currentTimeMillis()) {
            this.timeEff = System.currentTimeMillis() + 7_000;
            List<Integer> idEff = new ArrayList<>();
            int dem16 = 0;
            int dem17 = 0;
            for (int j = 0; j < 6; j++) {
                if (this.item.it_body[j] != null && this.item.it_body[j].isThanTrang()) {
                    if (this.item.it_body[j].levelUp >= 16) {
                        dem16++;
                    }
                    if (this.item.it_body[j].levelUp == 17) {
                        dem17++;
                    }
                }
            }
            if (dem16 >= 6) {
                idEff.add(10);
                idEff.add(11);
            }
            if (dem17 >= 6) {
                idEff.add(12);
                idEff.add(13);
            }
            if (rank.Ranked.get_Thanh_tich_pvp2(this) == 0) {
                idEff.add(14);
            }
            if (rank.Ranked.get_Thanh_tich_pvp2(this) == 1) {
                idEff.add(15);
            }
            if (rank.Ranked.get_Thanh_tich_pvp2(this) == 2) {
                idEff.add(16);
            }
            if (rank.Ranked.get_rank_wanted2(this) == 0) {
                idEff.add(17);
            }
            for (int idF : idEff) {
                //this.getService().addEffect(this.index_map, (short) idF, 5000, (byte) 0, (byte) 10);
            }
        }
        
        // 11. My pet expiration
        for (int j = 0; j < this.my_pet.size(); j++) {
            if (this.my_pet.get(j).time != -1 && this.my_pet.get(j).time < System.currentTimeMillis()) {
                this.my_pet.remove(j);
                if (this.getService() != null) {
                    this.getService().pet(this, false);
                }
                if (this.map != null && this.map.players != null) {
                    for (int i123 = 0; i123 < this.map.players.size(); i123++) {
                        Player p02 = this.map.players.get(i123);
                        if (p02 != null && p02 != this && p02.getService() != null) {
                            p02.getService().pet(this, false);
                        }
                    }
                }
                break;
            }
        }
        
        // 12. Tickets countdown
        boolean ch = false;
        if (this.get_ticket() < this.get_ticket_max()
                && this.cd_ticket_next < System.currentTimeMillis()) {
            this.cd_ticket_next = System.currentTimeMillis() + (60_000L * 10);
            this.update_ticket(1);
            ch = true;
        }
        if (this.get_pvp_ticket() < this.get_pvp_ticket_max()
                && this.cd_pvp_next < System.currentTimeMillis()) {
            this.cd_pvp_next = System.currentTimeMillis() + (60_000L * 60 * 2);
            this.update_pvp_ticket(1);
            ch = true;
        }
        if (this.get_key_boss() < this.get_key_boss_max()
                && this.cd_keyboss_next < System.currentTimeMillis()) {
            this.cd_keyboss_next = System.currentTimeMillis() + (60_000L * 60 * 1);
            this.update_key_boss(1);
            ch = true;
        }
        if (ch) {
            this.updateMoney();
            this.getService().CountDown_Ticket();
        }
        
        // 13. Combo countdown
        if (this.is_combo != null && this.time_combo < System.currentTimeMillis()) {
            this.is_combo = null;
            this.getService().start_combo(0);
        }
        
        // 14. Special map respawn in Phó Bản Khổng Lồ (Map 81 / Little Garden)
        if (this.map != null && (this.map.template.id == 81 || this.map.map_little_garden != null)) {
            if (this.isdie && this.time_hs_little_garden <= System.currentTimeMillis()) {
                this.isdie = false;
                this.time_hs_little_garden = 0;
                if (this.ability != null) {
                    this.hp = this.ability.get_hp_max(true);
                    this.mp = this.ability.get_mp_max(true);
                }
                // Hồi sinh về vùng an toàn / căn cứ của phe mình (Phe 4 Dorry = 350, 260 | Phe 5 Brogy = 1400, 260)
                short spawnX = (short) (this.type_pk == 4 ? 350 : 1400);
                short spawnY = 260;
                this.x = spawnX;
                this.y = spawnY;
                this.xold = spawnX;
                this.yold = spawnY;
                try {
                    Vgo vgo = new Vgo();
                    vgo.map_go = new Zone[]{this.map};
                    vgo.xnew = spawnX;
                    vgo.ynew = spawnY;
                    this.goto_map(vgo);
                    if (this.getService() != null) {
                        this.getService().send_time_cool_down(System.currentTimeMillis(), "", 0);
                        this.getService().use_potion(0, this.hp);
                        this.getService().use_potion(1, this.mp);
                        this.getService().update_PK(this, true);
                    }
                    this.map.change_flag(this, this.type_pk);
                    this.sendRevive();
                } catch (Exception ignored) {}
                this.time_can_mob_atk = System.currentTimeMillis() + 1500L;
            }
        }

        // 15. Special map respawn in Bảo Vệ Pháo Đài (Map 267..271)
        if (this.map != null && (this.map.IsMapBaoVePhaoDai() || this.map.baoVePhaoDai != null)) {
            if (this.isdie && this.time_hs_little_garden <= System.currentTimeMillis()) {
                respawnBaoVePhaoDai();
            }
        }

        // 16. Special map respawn in Trận Chiến Lớn (Map 272..275)
        if (this.map != null && this.map.template != null && TranChienLon.isMapTranChienLon(this.map.template.id)) {
            if (this.isdie && this.time_hs_little_garden <= System.currentTimeMillis()) {
                respawnTranChienLon();
            }
        }

        // 17. Special map respawn in Đấu Trường Tự Do (Map 70, 71, 72, 74)
        if (this.map != null && this.map.template != null && (this.map.template.id == 70 || this.map.template.id == 71 || this.map.template.id == 72 || this.map.template.id == 74)) {
            if (this.isdie && this.time_hs_little_garden <= System.currentTimeMillis()) {
                respawnDauTruongTuDo();
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_new_eff(int id, int param, long time) {
        if (this.list_eff != null) {
            long expire = System.currentTimeMillis() + time;
            for (EffTemplate eff : this.list_eff) {
                if (eff != null && eff.id == id) {
                    eff.param = param;
                    eff.time = Math.max(eff.time, expire);
                    if (this.ability != null && id >= 100) {
                        this.ability.recalculatePlayerStats(this);
                    }
                    return;
                }
            }
            this.list_eff.add(new EffTemplate(id, param, expire));
            if (this.ability != null && id >= 100) {
                this.ability.recalculatePlayerStats(this);
            }
        }
    }

    public Skill_info get_skill_by_id(int id) {
        if (this.skill_point != null) {
            for (int i = 0; i < this.skill_point.size(); i++) {
                Skill_info sk = this.skill_point.get(i);
                if (sk != null && sk.temp != null && sk.temp.ID == id) {
                    return sk;
                }
            }
        }
        return null;
    }

    public long time_cooldown_haki_bv = 0;
    public long time_cooldown_haki_qs = 0;

    public void triggerHakiBaVuong(Player targetPlayer, Mob targetMob) {
        // Neutralized to prevent unwanted auto-effects/buffs
    }

    /**
     * Kích hoạt Haki Quan Sát: đã tắt để chống hiện rồng vàng và buff ngoài ý muốn
     */
    public void triggerHakiQuanSat() {
        // Neutralized to prevent unwanted auto-effects/buffs
    }

    public void update_die() {
        if (this.list_eff != null) {
            for (EffTemplate eff : this.list_eff) {
                if (eff != null && EffTemplate.check_eff_remove_when_die(eff.id)) {
                    eff.time = System.currentTimeMillis();
                }
            }
        }
    }

    public void plus_point(Message m2) throws IOException {
        byte index = m2.reader().readByte();
        short value = m2.reader().readShort();
        // [FIX BUG 1] Validate: chặn value âm (hack tăng vô hạn) và index ngoài phạm vi
        if (value <= 0) return;
        if (index < 0 || index > 4) return;
        if (this.pointAttribute >= value) {
            switch (index) {
                case 0: {
                    if ((this.point1 + value) > 80) {
                        return;
                    }
                    if (this.point1 < 80) {
                        this.point1 += value;
                    }
                    break;
                }
                case 1: {
                    if ((this.point2 + value) > 80) {
                        return;
                    }
                    if (this.point2 < 80) {
                        this.point2 += value;
                    }
                    break;
                }
                case 2: {
                    if ((this.point3 + value) > 80) {
                        return;
                    }
                    if (this.point3 < 80) {
                        this.point3 += value;
                    }
                    break;
                }
                case 3: {
                    if ((this.point4 + value) > 80) {
                        return;
                    }
                    if (this.point4 < 80) {
                        this.point4 += value;
                    }
                    break;
                }
                case 4: {
                    if ((this.point5 + value) > 80) {
                        return;
                    }
                    if (this.point5 < 80) {
                        this.point5 += value;
                    }
                    break;
                }
            }
            this.pointAttribute -= value;
            this.update_info_to_all();
        } else {
            this.getService().send_box_ThongBao_OK("Không đủ điểm tiềm năng");
        }
    }

    public void reset_point(int type) throws IOException {
        switch (type) {
            case 0: {
                this.pointAttribute = (short) Level.get_total_point_by_level(this.level);
                this.point1 = 1;
                this.point2 = 1;
                this.point3 = 1;
                this.point4 = 1;
                this.point5 = 1;
                // reset thong thao;
                this.pointAttributeThongThao = this.thongthao;
                this.list_op_thongthao.clear();
            }

        }
        this.update_info_to_all();
    }

    public void send_skill() throws IOException {
        // update list can combo
        list_can_combo.clear();

        // Đảm bảo kỹ năng phe luôn tồn tại khi đã chọn phe
        ensureFactionSkill();

        List<Skill_info> skillsToSend = new ArrayList<>();

        for (int i = 0; i < this.skill_point.size(); i++) {
            Skill_info sk_info = this.skill_point.get(i);
            if (sk_info == null || sk_info.temp == null) continue;

            int idx = sk_info.temp.indexSkillInServer;
            boolean isHaiTacSkill = (idx >= 691 && idx <= 696);
            boolean isHaiQuanSkill = (idx >= 697 && idx <= 702);
            boolean isCachMangSkill = (idx >= 803 && idx <= 808);

            if (isHaiTacSkill) {
                if (this.huongnghiep == 2) {
                    skillsToSend.add(sk_info);
                }
            } else if (isHaiQuanSkill) {
                if (this.huongnghiep == 1) {
                    skillsToSend.add(sk_info);
                }
            } else if (isCachMangSkill) {
                if (this.huongnghiep == 3) {
                    skillsToSend.add(sk_info);
                }
            } else {
                skillsToSend.add(sk_info);
            }
        }

        int fullSetId = ThanTrangConfig.getFullSetId(this);
        if (fullSetId > 0 && fullSetId <= ThanTrangConfig.TOTAL_SETS) {
            int[] setSkills = new int[] {
                ThanTrangConfig.getActiveSkill1(fullSetId),
                ThanTrangConfig.getActiveSkill2(fullSetId),
                ThanTrangConfig.getStatBuffSkill(fullSetId)
            };
            for (int skId : setSkills) {
                if (skId <= 0) continue;
                skill.Skill_Template st = skill.Skill_Template.get_temp_by_id(skId);
                if (st != null) {
                    if (st.ID <= 0) st.ID = st.indexSkillInServer > 0 ? st.indexSkillInServer : skId;
                    Skill_info sinfo = new Skill_info();
                    sinfo.temp = st;
                    sinfo.exp = 0;
                    skillsToSend.add(sinfo);
                }
            }
        }

        Message m = new Message(-7);
        m.writer().writeByte(3);
        m.writer().writeByte(skillsToSend.size());
        for (int i = 0; i < skillsToSend.size(); i++) {
            Skill_info sk_info = skillsToSend.get(i);
            write_data_skill(m.writer(), sk_info);
            if (sk_info.temp.ID < 3 && sk_info.temp.typeSkill == 1 && sk_info.temp.Lv_RQ > -1) {
                list_can_combo.add(sk_info);
            }
        }
        if (conn != null) {
            conn.addmsg(m);
        } else if (this instanceof DeTu && ((DeTu) this).master != null && ((DeTu) this).master.conn != null) {
            ((DeTu) this).master.conn.addmsg(m);
        }
    }

    public void ensureFactionSkill() {
        if (this.huongnghiep <= 0) return;
        int targetIdxMin = (this.huongnghiep == 1) ? 697 : ((this.huongnghiep == 2) ? 691 : 803);
        int targetIdxMax = (this.huongnghiep == 1) ? 702 : ((this.huongnghiep == 2) ? 696 : 808);
        int baseIdx = (this.huongnghiep == 1) ? 697 : ((this.huongnghiep == 2) ? 691 : 803);

        if (this.skill_point == null) {
            this.skill_point = new ArrayList<>();
        }

        boolean exists = false;
        for (Skill_info sk : this.skill_point) {
            if (sk != null && sk.temp != null
                    && sk.temp.indexSkillInServer >= targetIdxMin
                    && sk.temp.indexSkillInServer <= targetIdxMax) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            Skill_info sk = createFactionSkillInfo(baseIdx);
            if (sk != null) {
                this.skill_point.add(sk);
            }
        }
    }

    public static Skill_info createFactionSkillInfo(int baseIndex) {
        Skill_info sk = createBotSkillInfo(baseIndex);
        if (sk != null && sk.temp != null) {
            return sk;
        }
        if (skill.Skill_Template.ENTRYS != null) {
            skill.Skill_Template best = null;
            for (skill.Skill_Template st : skill.Skill_Template.ENTRYS) {
                if (st != null && st.indexSkillInServer == baseIndex) {
                    if (st.Lv_RQ >= 0) {
                        best = st;
                        break;
                    } else if (best == null) {
                        best = st;
                    }
                }
            }
            if (best != null) {
                sk = new Skill_info();
                sk.temp = best;
                sk.exp = (best.Lv_RQ >= 0) ? 0 : -1;
                if (sk.temp.Lv_RQ == -1) {
                    skill.Skill_Template.learn_skill(sk);
                }
                return sk;
            }
        }
        return null;
    }

    public void updateExpFactionSkill(long exp) throws IOException {
        if (this.huongnghiep <= 0) return;
        int targetIdxMin = (this.huongnghiep == 1) ? 697 : ((this.huongnghiep == 2) ? 691 : 803);
        int targetIdxMax = (this.huongnghiep == 1) ? 702 : ((this.huongnghiep == 2) ? 696 : 808);

        Skill_info factionSk = null;
        for (Skill_info sk : this.skill_point) {
            if (sk != null && sk.temp != null
                    && sk.temp.indexSkillInServer >= targetIdxMin
                    && sk.temp.indexSkillInServer <= targetIdxMax) {
                factionSk = sk;
                break;
            }
        }
        if (factionSk != null) {
            this.updateExpSkill(factionSk.temp.ID, exp);
        }
    }

    public void write_data_skill(DataOutputStream dos, Skill_info sk_info) throws IOException {
        dos.writeShort(sk_info.temp.indexSkillInServer);
        dos.writeShort(sk_info.temp.ID);
        dos.writeShort(sk_info.temp.idIcon);
        dos.writeByte(sk_info.temp.typeSkill);
        dos.writeByte(sk_info.temp.typeBuff);
        dos.writeUTF(sk_info.temp.name);
        dos.writeShort(sk_info.get_eff_skills()[0]);
        dos.writeShort(sk_info.temp.range);
        //
        dos.writeByte(sk_info.temp.nTarget);
        dos.writeShort(sk_info.temp.rangeLan);
        String info_sk = sk_info.temp.getInfo(sk_info.lvdevil, this.clazz);
        dos.writeInt(sk_info.get_dame(this));
        dos.writeShort(sk_info.temp.manaLost);
        dos.writeInt(sk_info.temp.timeDelay);
        dos.writeByte(sk_info.temp.nKick);
        dos.writeUTF(info_sk);
        dos.writeByte(sk_info.temp.Lv_RQ);
        dos.writeShort(sk_info.get_percent());
        dos.writeByte(sk_info.temp.typeDevil);
        //
        dos.writeByte(sk_info.temp.op.size());
        for (int j = 0; j < sk_info.temp.op.size(); j++) {
            dos.writeByte(sk_info.temp.op.get(j).id);
            dos.writeShort(sk_info.temp.op.get(j).getParam());
        }
        dos.writeByte(sk_info.temp.idEffSpec);
        if (sk_info.temp.idEffSpec > 0) {
            dos.writeShort(sk_info.temp.perEffSpec);
            dos.writeShort(sk_info.temp.timeEffSpec);
        }
        dos.writeByte(sk_info.lvdevil);
        dos.writeByte(sk_info.devilpercent);
    }

    public void updateExpSkill(int index, long exp) throws IOException {
        if (exp <= 0) {
            return;
        }
        exp *= Manager.gI().exp;
        exp *= 7;

        Skill_info sk_info = null;
        if (this.skill_point != null) {
            for (int i = 0; i < this.skill_point.size(); i++) {
                Skill_info temp = this.skill_point.get(i);
                if (temp != null && temp.temp != null && temp.temp.ID == index) {
                    sk_info = temp;
                    break;
                }
            }
        }
        if (sk_info != null && sk_info.temp != null && sk_info.exp > -1 && sk_info.temp.Lv_RQ > 0) {
            // Cấp skill < 5: Tăng tốc độ nhận EXP (nhân 3) để up nhanh hơn thường, cấp >= 5 tính như cũ
            if (sk_info.temp.Lv_RQ < 5) {
                exp *= 3;
            }
            if (sk_info.exp < 0) {
                sk_info.exp = 0;
            }
            sk_info.exp += exp;

            boolean upgraded = false;
            while (sk_info.temp != null && sk_info.temp.Lv_RQ > 0) {
                int lvIndex = sk_info.temp.Lv_RQ - 1;
                if (lvIndex < 0 || lvIndex >= Skill_info.EXP.length) {
                    break;
                }
                long exp_total = Skill_info.EXP[lvIndex];
                if (exp_total <= 0 || sk_info.exp < exp_total) {
                    break;
                }

                long leftover = sk_info.exp - exp_total;
                if (leftover < 0) {
                    leftover = 0;
                }
                if (Skill_Template.upgrade_skill(sk_info, this.clazz)) {
                    sk_info.exp = leftover;
                    upgraded = true;
                } else {
                    // Đã đạt cấp tối đa của kỹ năng (Lv 30 cho 3 chiêu chính, Lv 20 cho Thủy chiến)
                    sk_info.exp = 0;
                    break;
                }
            }

            if (upgraded) {
                this.sendSkillLevelUP(sk_info);
                this.send_skill();
                this.update_info_to_all();
            } else {
                Learn_Skill.send_skill_percent(this, sk_info);
            }
        }
    }

    public void setIsHsLol(int i) throws IOException {
        if (i == 1 && this.get_eff(52) != null) {
            return;
        }
        Message m = new Message(51);
        m.writer().writeByte(4);
        m.writer().writeByte(i);
        this.conn.addmsg(m);
    }

    public void addPointSkill(int indexHotKey, int points) throws IOException {
        if (indexHotKey < 0 || this.skill_point == null || indexHotKey >= this.skill_point.size()) {
            return;
        }
        if (points <= 0 || points > this.pointSkill) {
            return;
        }
        Skill_info sk_info = this.skill_point.get(indexHotKey);
        if (sk_info != null && sk_info.temp != null && sk_info.temp.Lv_RQ > 0) {
            int successUpgrades = 0;
            for (int k = 0; k < points; k++) {
                if (Skill_Template.upgrade_skill(sk_info, this.clazz)) {
                    successUpgrades++;
                } else {
                    break;
                }
            }
            if (successUpgrades > 0) {
                sk_info.exp = 0;
                this.pointSkill -= successUpgrades;
                this.send_skill();
                this.update_info_to_all();
                this.getService().Main_char_Info();
            } else {
                this.getService().send_box_ThongBao_OK("Không thể nâng cấp kỹ năng này");
            }
        }
    }

    public void sendSkillLevelUP(Skill_info sk_info) throws IOException {
        Message m = new Message(-28);
        m.writer().writeByte(1);
        write_data_skill(m.writer(), sk_info);
        if (conn != null) {
            conn.addmsg(m);
        }
        m.cleanup();
    }

    public int get_head() {
        if (this.isTransformThanTrang && this.transformThanTrangSetId >= 1 && this.transformThanTrangSetId <= ThanTrangConfig.TOTAL_SETS) {
            int[] parts = ThanTrangConfig.getTransformParts(this.transformThanTrangSetId);
            if (parts != null && parts.length > 0 && parts[0] > 0) {
                return parts[0];
            }
        }
        if (get_eff(21) != null) { // zoombi
            return 765;
        }
        if (!this.is_hide_fashion_head && this.fashion != null) {
            for (int i = 0; i < this.fashion.size(); i++) {
                if (this.fashion.get(i) != null && this.fashion.get(i).is_use) {
                    ItemFashion temp = ItemFashion.get_item(this.fashion.get(i).id);
                    if (temp != null && temp.mWearing != null && temp.mWearing.length > 6 && temp.mWearing[6] > 0) {
                        return temp.mWearing[6];
                    }
                }
            }
        }
        if (this.itfashionP != null) {
            for (int i = 0; i < this.itfashionP.size(); i++) {
                if (this.itfashionP.get(i) != null && this.itfashionP.get(i).category == 108 && this.itfashionP.get(i).is_use) {
                    return this.itfashionP.get(i).icon;
                }
            }
        }
        return this.head >= 0 ? this.head : 0;
    }

    public int get_hair() {
        if (this.isTransformThanTrang && this.transformThanTrangSetId >= 1 && this.transformThanTrangSetId <= ThanTrangConfig.TOTAL_SETS) {
            int[] parts = ThanTrangConfig.getTransformParts(this.transformThanTrangSetId);
            if (parts != null && parts.length > 3 && parts[3] > 0) {
                return parts[3];
            }
        }
        int res = this.hair;
        // 1. Tóc thời trang mua lẻ (category 103)
        if (this.itfashionP != null) {
            for (int i = 0; i < this.itfashionP.size(); i++) {
                if (this.itfashionP.get(i) != null && this.itfashionP.get(i).category == 103 && this.itfashionP.get(i).is_use) {
                    if (this.itfashionP.get(i).icon == 772) {
                        res = (this.itfashionP.get(i).icon + this.tocSuper);
                    } else {
                        res = this.itfashionP.get(i).icon;
                    }
                    break;
                }
            }
        }
        // 2. Bộ thời trang (set fashion) khi không bật che tóc thời trang
        if (!this.is_hide_fashion_hair && this.fashion != null) {
            for (int i = 0; i < this.fashion.size(); i++) {
                if (this.fashion.get(i) != null && this.fashion.get(i).is_use) {
                    ItemFashion temp = ItemFashion.get_item(this.fashion.get(i).id);
                    if (temp != null && temp.mWearing != null) {
                        // 2a. Nếu thời trang có tóc riêng (> 0)
                        if (temp.mWearing.length > 7 && temp.mWearing[7] > 0) {
                            return temp.mWearing[7];
                        }
                        // 2b. Nếu thời trang có mặt nạ/đầu trùm riêng và đang hiển thị (không che mặt) -> ẩn tóc rời (-2)
                        if (!this.is_hide_fashion_head && temp.mWearing.length > 6 && temp.mWearing[6] > 0) {
                            return -2;
                        }
                        // 2c. Nếu thời trang yêu cầu ẩn tóc (-2) do nón trùm, và nón đang hiển thị
                        if (temp.mWearing.length > 7 && temp.mWearing[7] == -2) {
                            if (this.is_show_hat && get_hat() > 0) {
                                return -2;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (res <= 0) {
            switch (this.clazz) {
                case 1: res = 1; break;
                case 2: res = 24; break;
                case 3: res = 28; break;
                case 4: res = 32; break;
                case 5: res = 36; break;
                default: res = 1; break;
            }
        }
        return res;
    }

    public void update_itfashionP(ItemFashionP temp_new, int category) throws IOException {
        temp_new.is_use = true;
        for (int i = 0; i < this.itfashionP.size(); i++) {
            if (this.itfashionP.get(i).category == category
                    && !this.itfashionP.get(i).equals(temp_new)) {
                this.itfashionP.get(i).is_use = false;
            }
        }
        this.update_wearing_to_all();
    }

    public ItemFashionP check_itfashionP(int id, int type) {
        for (int i = 0; i < this.itfashionP.size(); i++) {
            if (this.itfashionP.get(i).category == type && this.itfashionP.get(i).id == id) {
                return this.itfashionP.get(i);
            }
        }
        return null;
    }

    public ItemFashionP2 check_fashion(int id) {
        for (int i = 0; i < this.fashion.size(); i++) {
            if (this.fashion.get(i).id == id) {
                return this.fashion.get(i);
            }
        }
        return null;
    }

    public void update_fashionP2(ItemFashionP2 temp_new) throws IOException {
        temp_new.is_use = true;
        for (int i = 0; i < this.fashion.size(); i++) {
            ItemFashionP2 f = this.fashion.get(i);
            if (f != temp_new && f.id != temp_new.id) {
                f.is_use = false;
            }
        }
        this.update_wearing_to_all();
    }

    public short[] get_fashion() {
        if (get_eff(21) != null) {
            return new short[]{-1, -2, -1, 766, -1, 767, 765, -2};
        }
        if (this.fashion == null) {
            return null;
        }
        short[] result = null;
        for (int i = 0; i < this.fashion.size(); i++) {
            if (this.fashion.get(i) != null && this.fashion.get(i).is_use) {
                ItemFashion temp = ItemFashion.get_item(this.fashion.get(i).id);
                if (temp != null && temp.mWearing != null) {
                    result = new short[temp.mWearing.length];
                    for (int j = 0; j < temp.mWearing.length; j++) {
                        result[j] = temp.mWearing[j];
                    }
                    break;
                }
            }
        }
        if (result != null && this.item != null && this.item.it_body != null && this.item.it_body.length > 0 && this.item.it_body[0] != null && result[0] == -1) {
            result[0] = this.item.it_body[0].template != null ? this.item.it_body[0].template.part : -1;
        }
        return result;
    }

    /**
     * Lấy ID part chuẩn cho từng slot ngoại trang (0..7)
     */
    public short get_wearing_part(int slot) {
        if (this.isTransformThanTrang && this.transformThanTrangSetId >= 1 && this.transformThanTrangSetId <= ThanTrangConfig.TOTAL_SETS) {
            int[] parts = ThanTrangConfig.getTransformParts(this.transformThanTrangSetId);
            if (parts != null) {
                if (slot == 0 && parts.length > 5 && parts[5] > 0) return (short) parts[5]; // weapon
                if (slot == 1 && parts.length > 4 && parts[4] > 0) return (short) parts[4]; // hat
                if (slot == 3 && parts.length > 1 && parts[1] > 0) return (short) parts[1]; // body
                if (slot == 5 && parts.length > 2 && parts[2] > 0) return (short) parts[2]; // leg
            }
        }
        if (slot == 1) {
            return get_hat();
        }
        if (slot == 0 && !this.is_show_weapon) {
            return -1;
        }
        short[] fashion = this.get_fashion();
        if (fashion != null && slot < fashion.length && fashion[slot] != -1) {
            if (slot == 0 || slot == 3 || slot == 5) {
                return fashion[slot];
            }
        }
        Item_wear it_w = (this.item != null && this.item.it_body != null && slot < this.item.it_body.length) ? this.item.it_body[slot] : null;
        if (slot == 6 && this.item != null && this.item.it_heart != null) {
            it_w = this.item.it_heart;
        }
        if (it_w != null && it_w.template != null) {
            template.ItemTemplate3 t3 = template.ItemTemplate3.get_it_by_id(it_w.template.id);
            return t3 != null ? (short) t3.part : (short) it_w.template.part;
        }
        if (slot == 3) {
            return this.part_body;
        }
        if (slot == 5) {
            return this.part_leg;
        }
        if (slot == 0) {
            return this.part_weapon;
        }
        return -1;
    }

    public void remove_hairf() throws IOException {
        for (int i = 0; i < this.itfashionP.size(); i++) {
            if (this.itfashionP.get(i).category == 103) {
                this.itfashionP.get(i).is_use = false;
            }
        }
        this.update_wearing_to_all();
    }

    public void remove_fashion() throws IOException {
        for (int i = 0; i < this.fashion.size(); i++) {
            this.fashion.get(i).is_use = false;
        }
        this.update_wearing_to_all();
    }

    public void remove_headf() throws IOException {
        for (int i = 0; i < this.itfashionP.size(); i++) {
            if (this.itfashionP.get(i).category == 108) {
                this.itfashionP.get(i).is_use = false;
            }
        }
        this.update_wearing_to_all();
    }

    public void change_new_date() {
        if (date == null || this instanceof DeTu) {
            if (date == null) {
                date = DateTime.now();
            }
            return;
        }
        DateTime now = DateTime.now();
        if (!ZUtil.is_same_day(now, date)) {
            date = now;
            time_ship = 0;
            time_nvl = 0;
            diemdanh = 0;
            diemdanh_ngay = 0;
            nvlMax = 50;
            ttvtMax = 20;
            ltMax = 25;
            hd_max = 3;
            namiMax = 5;
            aidonMax = 20;
            mr3Max = 5;
            ArchiDaily.ramdomArchiDaily(this);
            this.resetDailyData(false);
            for (EventData ev : this.eventData) {
                if (ev.data != null) {
                    EventManager.dispatchResetDailyData(ev, ev.eventID);
                }
            }
            EventManager.dispatchCheckAndRemoveEventItems(this);
            if (event.EventManager.isActive(1)) {
                item.remove_item47(4, 189, item.total_item_bag_by_id(4, 189));
            }
            List<QuestP> listClear = new ArrayList<>();
            for (int i = 0; i < list_quest.size(); i++) {
                if (list_quest.get(i).template.id < -2000) {
                    listClear.add(list_quest.get(i));
                }
            }
            list_quest.removeAll(listClear);
            Quest.send_List_Quest(this, false);
        }
    }

    public Skill_info get_skill_temp(int idSkill) {
        if (this.skill_point != null) {
            for (int i = 0; i < this.skill_point.size(); i++) {
                Skill_info sk = this.skill_point.get(i);
                if (sk != null && sk.temp != null) {
                    if (sk.temp.ID == idSkill || sk.temp.indexSkillInServer == idSkill || sk.temp.idIcon == idSkill) {
                        return sk;
                    }
                    // Trái Phượng Hoàng (Marco): SQL id (4025..4028) & Eff id (4024, 4029)
                    if ((idSkill == 4025 || idSkill == 4024) && (sk.temp.indexSkillInServer == 817 || sk.temp.ID == 5032)) {
                        return sk;
                    }
                    if (idSkill == 4026 && (sk.temp.indexSkillInServer == 818 || sk.temp.ID == 5033)) {
                        return sk;
                    }
                    if ((idSkill == 4027 || idSkill == 4029) && (sk.temp.indexSkillInServer == 819 || sk.temp.ID == 5034)) {
                        return sk;
                    }
                    if (idSkill == 4028 && (sk.temp.indexSkillInServer == 820 || sk.temp.ID == 5035)) {
                        return sk;
                    }
                }
            }
            if (idSkill == 0 && !this.skill_point.isEmpty() && this.skill_point.get(0) != null) {
                return this.skill_point.get(0);
            }
        }
        if (ThanTrangConfig.isThanTrangSkill(idSkill)) {
            int fullSetId = ThanTrangConfig.getFullSetId(this);
            if (fullSetId > 0) {
                int ttSkill1 = ThanTrangConfig.getActiveSkill1(fullSetId);
                int ttSkill2 = ThanTrangConfig.getActiveSkill2(fullSetId);
                int ttBuff = ThanTrangConfig.getStatBuffSkill(fullSetId);
                int baseSkill = 4000 + (fullSetId - 1) * 5;
                if (idSkill == ttSkill1 || idSkill == ttSkill2 || idSkill == ttBuff || idSkill == 4001 + fullSetId || (idSkill >= baseSkill + 1 && idSkill <= baseSkill + 5)) {
                    skill.Skill_Template st = skill.Skill_Template.get_temp_by_id(idSkill);
                    if (st != null) {
                        if (st.ID <= 0) st.ID = st.indexSkillInServer > 0 ? st.indexSkillInServer : idSkill;
                        Skill_info skInfo = new Skill_info();
                        skInfo.temp = st;
                        skInfo.exp = 0;
                        return skInfo;
                    }
                }
            }
        }
        skill.Skill_Template fallbackSt = skill.Skill_Template.get_temp_by_id(idSkill);
        if (fallbackSt != null && (fallbackSt.typeSkill == 1 || fallbackSt.typeSkill == 4 || fallbackSt.typeSkill == 2)) {
            Skill_info skInfo = new Skill_info();
            skInfo.temp = fallbackSt;
            skInfo.exp = 0;
            return skInfo;
        }
        return null;
    }

    public void get_skill_taq_new(int id) throws IOException {
        if (id < 4000) {
            id += 4000;
        }
        List<Skill_info> list_remove = new ArrayList<>();
        for (int i = 0; i < this.skill_point.size(); i++) {
            Skill_info temp = this.skill_point.get(i);
            if (temp != null && temp.temp != null) {
                boolean isHaki = ((temp.temp.indexSkillInServer >= 660 && temp.temp.indexSkillInServer <= 666)
                        || (temp.temp.indexSkillInServer >= 672 && temp.temp.indexSkillInServer <= 690));
                if ((temp.temp.typeDevil > 0 || temp.temp.ID > 2000) && !isHaki) {
                // exp ac quy
                if (temp.devilpercent > 0 || temp.lvdevil > 0) {
                    int numPotion = 0;
                    switch (temp.lvdevil) {
                        case 1: {
                            numPotion = 1;
                            break;
                        }
                        case 2: {
                            numPotion = 2;
                            break;
                        }
                        case 3: {
                            numPotion = 3;
                            break;
                        }
                        case 4: {
                            numPotion = 5;
                            break;
                        }
                        case 5: {
                            numPotion = 8;
                            break;
                        }
                    }
                    if (numPotion > 0) {
                        this.item.add_item_bag47(4, 909, numPotion);
                        this.item.updateInventory(false);
                    }
                }
                //
                    Learn_Skill.remove_skill(this, temp);
                    list_remove.add(temp);
                }
            }
        }
        this.skill_point.removeAll(list_remove);
        list_remove.clear();
        switch (id) {
            case 4032: {
                int[] id_ = new int[]{478, 476, 475};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4033: {
                int[] id_ = new int[]{480, 479, 477};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4034: {
                int[] id_ = new int[]{483, 482, 481};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4088: {
                int[] id_ = new int[]{484, 485, 486};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4090: {
                int[] id_ = new int[]{514, 513, 512};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4091: {
                int[] id_ = new int[]{517, 516, 515};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4092: {
                int[] id_ = new int[]{523, 522, 519};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4093: {
                int[] id_ = new int[]{521, 520, 518};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4160: {
                int[] id_ = new int[]{527, 526, 525, 524};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4161: {
                int[] id_ = new int[]{531, 530, 529, 528};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4219: {
                int[] id_ = new int[]{538, 537, 536};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4220: {
                int[] id_ = new int[]{535, 534, 533};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4240: {
                int[] id_ = new int[]{542, 541, 539, 540};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4316: {
                int[] id_ = new int[]{548, 547, 546};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4317: {
                int[] id_ = new int[]{545, 544, 543};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4318: {
                // String[] name_ = new String[]{"Thần hộ thể", "Tăng trọng", "Sức nặng ngàn

                // cân"};
                int[] id_ = new int[]{551, 550, 549};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4427: {
                // String[] name_ = new String[]{"Dòng chảy ma pháp", "Ṿng xoáy ma pháp", "Giải
                // phóng", "Xoáy đen"};
                int[] id_ = new int[]{656, 657, 658, 659};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4428: {
                int[] id_ = new int[]{667};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4869: { // Trái ánh sáng
                int[] id_ = new int[]{791,792,793,794};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4870: { // Trái tình yêu
                int[] id_ = new int[]{799, 800, 801, 802};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4873: { // Trái nika
                int[] id_ = new int[]{795,796,797,798};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4912: { // Trái Venom (item 912)
                int[] id_ = new int[]{809, 810, 811, 812};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4913: { // Trái Thanh Long (item 913)
                int[] id_ = new int[]{813, 814, 815, 816};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
            case 4914: { // Trái Phượng Hoàng (item 914)
                int[] id_ = new int[]{817, 818, 819, 820};
                for (int i = 0; i < id_.length; i++) {
                    Skill_info sk_add = new Skill_info();
                    sk_add.exp = 0;
                    sk_add.temp = Skill_Template.get_temp(id_[i], sk_add.exp);
                    if (sk_add.temp != null) {
                        list_remove.add(sk_add);
                    }
                }
                break;
            }
        }
        this.skill_point.addAll(list_remove);
        list_remove.clear();
        this.send_skill();
        this.update_info_to_all();
        if (this.getService() != null) {
            this.getService().UpdateInfoMaincharInfo();
        }
    }

    public void update_wearing_to_all() throws IOException {
        this.update_info_to_all();
    }

    public void update_info_to_all() throws IOException {
        setAbility();
        if (this.hpMax > 0 && this.hp > this.hpMax) this.hp = this.hpMax;
        if (this.hp <= 0 && !this.isdie && this.hpMax > 0) this.hp = this.hpMax;
        if (this.mpMax > 0 && this.mp > this.mpMax) this.mp = this.mpMax;

        // 1. Cập nhật đầy đủ chỉ số và visual cho bản thân
        if (this.conn != null && this.getService() != null) {
            this.getService().Main_char_Info();
            try {
                this.getService().UpdateInfoMaincharInfo();
            } catch (Exception ignored) {}
            this.getService().charWearing(this, false);
            this.getService().Weapon_fashion(this, false);
            this.getService().pet(this, false);
            this.getService().getThanhTich(this);
            this.getService().update_PK(this, false);
        }

        // 2. Broadcast thanh HP/MP lên toàn map
        this.update_hp();

        // 3. Broadcast visual & trạng thái cho người chơi khác trong map (không duplicate packet)
        if (this.map != null && this.map.players != null) {
            for (int i = 0; i < this.map.players.size(); i++) {
                Player p0 = this.map.players.get(i);
                if (p0 != null && p0.index_map != this.index_map && p0.getService() != null) {
                    p0.getService().charWearing(this, false);
                    p0.getService().Weapon_fashion(this, false);
                    p0.getService().pet(this, false);
                    p0.getService().update_PK(this, false);
                    if (this.clan != null) {
                        Clan.send_me_to_other(this, p0, false);
                    }
                }
            }
        }
    }

    public void syncFullPlayerStats() {
        try {
            this.update_info_to_all();
            if (this.detu != null) {
                this.detu.clan = this.clan;
                this.detu.setAbility();
                this.detu.update_hp();
                this.detu.update_mp();
            }
            List<bot.mercenary.MercenaryBot> activeMercs = bot.mercenary.MercenaryManager.gI().getActiveBots(this);
            if (activeMercs != null) {
                for (bot.mercenary.MercenaryBot merc : activeMercs) {
                    merc.clan = this.clan;
                    merc.setAbility();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getNumPassive() {
        if (this.level < 10) {
            return 0;
        } else if (this.level < 20) {
            return 1;
        } else if (this.level < 30) {
            return 2;
        } else if (this.level < 40) {
            return 3;
        } else {
            return 4;
        }
    }

    public ItemBoatP check_itboat(int id) {
        for (int i = 0; i < this.itemboat.size(); i++) {
            if (this.itemboat.get(i).id == id) {
                return this.itemboat.get(i);
            }
        }
        return null;
    }

    public void update_new_part_boat(ItemBoatP temp_new) {
        if (temp_new == null) return;
        ItemBoat itbNew = ItemBoat.get_item(temp_new.id);
        if (itbNew == null) return;
        byte type_boat_new = itbNew.type;
        for (int i = 0; i < this.itemboat.size(); i++) {
            ItemBoatP ib = this.itemboat.get(i);
            if (ib != null && !ib.equals(temp_new)) {
                ItemBoat itb = ItemBoat.get_item(ib.id);
                if (itb != null && itb.type == type_boat_new) {
                    ib.is_use = false;
                }
            }
        }
    }

    public short[] get_part_boat() {
        short[] result = new short[]{0, 1, 2, 3};
        for (int i = 0; i < this.itemboat.size(); i++) {
            ItemBoatP ib = this.itemboat.get(i);
            if (ib != null && ib.is_use) {
                ItemBoat temp = ItemBoat.get_item(ib.id);
                if (temp != null && temp.type >= 0 && temp.type < result.length) {
                    result[temp.type] = temp.idimg;
                }
            }
        }
        return result;
    }

    public short get_hat() {
        if (!this.is_show_hat) {
            return -1;
        }
        short[] fashion = this.get_fashion();
        if (fashion != null && fashion.length > 1) {
            // Nếu thời trang có nón riêng (> 0)
            if (fashion[1] > 0) {
                return fashion[1];
            }
            // Nếu thời trang bắt buộc ẩn nón (-2)
            if (fashion[1] == -2) {
                return -1;
            }
            // Nếu thời trang có mặt nạ/đầu trùm riêng và không che mặt -> tự động ẩn nón trang bị thường (-1)
            if (!this.is_hide_fashion_head && fashion.length > 6 && fashion[6] > 0) {
                return -1;
            }
        }
        // Nếu không bị thời trang ghi đè/ẩn, lấy nón trang bị thường
        Item_wear it_w = (this.item != null && this.item.it_body != null && this.item.it_body.length > 1) ? this.item.it_body[1] : null;
        if (it_w != null && it_w.template != null) {
            template.ItemTemplate3 t3 = template.ItemTemplate3.get_it_by_id(it_w.template.id);
            return t3 != null ? (short) t3.part : (short) it_w.template.part;
        }
        return -1;
    }

    public byte get_index_full_set() {
        byte fullSet = -1;
        if (item != null && item.it_body != null && item.it_body.length >= 6
                && item.it_body[0] != null && item.it_body[1] != null && item.it_body[2] != null
                && item.it_body[3] != null && item.it_body[4] != null && item.it_body[5] != null) {
            int levelVuKhi = item.it_body[0].levelUp;
            int levelNon = item.it_body[1].levelUp;
            int levelDayChuyen = item.it_body[2].levelUp;
            int levelAo = item.it_body[3].levelUp;
            int levelNhan = item.it_body[4].levelUp;
            int levelQuan = item.it_body[5].levelUp;
            int minLevel = Math.min(levelVuKhi, Math.min(levelNon, Math.min(levelDayChuyen, Math.min(levelAo, Math.min(levelNhan, levelQuan)))));
            if (minLevel >= 15) fullSet = 5;
            else if (minLevel == 14) fullSet = 4;
            else if (minLevel == 13) fullSet = 3;
            else if (minLevel == 12) fullSet = 2;
            else if (minLevel == 11) fullSet = 1;
        }
        return fullSet;
    }

    public short get_percent_mana_use_skill() {
        return 0;
    }

    public void send_kich_an() throws IOException {
        this.resetAllSkillCooldowns();
        if (this.isBot) return; // bot không cần nhận packet reset skill
        Message m = new Message(57);
        m.writer().writeByte(9); // reset cooldown skill
        m.writer().writeShort(0);
        m.writer().writeShort(this.index_map);
        m.writer().writeByte(0);
        m.writer().writeByte(0);
        m.writer().writeInt(0);
        m.writer().writeShort(this.index_map);
        m.writer().writeByte(0);
        m.writer().writeByte(0);
        m.writer().writeInt(0);
        this.addmsg(m); // fix: dùng this.addmsg — an toàn với bot
        m.cleanup();
    }

    public QuestP get_quest(int id) {
        for (int i = 0; i < this.list_quest.size(); i++) {
            if (this.list_quest.get(i).template.index == id) {
                return this.list_quest.get(i);
            }
        }
        return null;
    }

    public void update_point_pk(int point) throws IOException {
        this.pointPk += point;
        if (this.pointPk < 0) {
            this.pointPk = 0;
        }
        if (this.pointPk > 100_000) {
            this.pointPk = 100_000;
        }
        if (this.isBot) return; // bot không cần packet PK — tránh NPE crash
        Message m = new Message(-45);
        m.writer().writeInt(this.pointPk);
        m.writer().writeByte(-1); // fake
        this.addmsg(m); // fix: dùng this.addmsg — an toàn với bot
        m.cleanup();
    }

    public void update_num_item_quest(int type, int id_mob, int value) throws IOException {
        if (this.list_quest == null || this.list_quest.isEmpty()) return;
        QuestP questCheck = null;
        for (int j = this.list_quest.size() - 1; j >= 0; j--) {
            QuestP tempP = this.list_quest.get(j);
            if (tempP != null && tempP.template != null && tempP.template.statusQuest == 1 && tempP.data != null) {
                for (int k = 0; k < tempP.data.length; k++) {
                    if (tempP.data[k] != null && tempP.data[k].length >= 4 && tempP.data[k][0] == type
                            && tempP.data[k][1] == id_mob && tempP.data[k][3] < tempP.data[k][2]) {
                        questCheck = tempP;
                        break;
                    }
                }
            }
            if (questCheck != null) {
                break;
            }
        }
        if (questCheck != null && questCheck.data != null) {
            boolean finished = true;
            for (int j = 0; j < questCheck.data.length; j++) {
                if (questCheck.data[j] == null || questCheck.data[j].length < 4) continue;
                if (questCheck.data[j][0] == type && questCheck.data[j][1] == id_mob
                        && questCheck.data[j][3] < questCheck.data[j][2]) {
                    questCheck.data[j][3] += value;
                    if (questCheck.data[j][3] >= questCheck.data[j][2]) {
                        questCheck.data[j][3] = questCheck.data[j][2];
                    } else {
                        finished = false;
                    }
                    // update notice quest progress
                    Message mq = new Message(25);
                    mq.writer().writeShort(id_mob);
                    mq.writer().writeByte(questCheck.data[j][0] == 1 ? 1 : 5);
                    mq.writer().writeShort(questCheck.data[j][3]);
                    mq.writer().writeShort(questCheck.data[j][2]);
                    this.addmsg(mq);
                    mq.cleanup();
                } else {
                    if (questCheck.data[j][3] < questCheck.data[j][2]) {
                        finished = false;
                    }
                }
            }
            if (finished) {
                Quest.remove_old_and_send_next(this, questCheck);
                // send notice quest finish
                Message mnext = new Message(-31);
                mnext.writer().writeByte(0);
                mnext.writer().writeUTF("Nhiệm vụ hoàn thành!");
                mnext.writer().writeByte(5);
                mnext.writer().writeShort(-1);
                this.addmsg(mnext);
                mnext.cleanup();
            }
        } else {
            for (int j = 0; j < this.list_quest.size(); j++) {
                QuestP tempP = this.list_quest.get(j);
                if (tempP != null && tempP.template != null && tempP.template.statusQuest == 2) {
                    Quest temp_old_quest = Quest.get_quest(tempP.template.id - 1);
                    if (temp_old_quest != null && temp_old_quest.statusQuest == 1 && temp_old_quest.data_quest != null && temp_old_quest.data_quest.length > 0) {
                        for (int i = 0; i < temp_old_quest.data_quest.length; i++) {
                            if (temp_old_quest.data_quest[i] != null && temp_old_quest.data_quest[i].length >= 3
                                    && temp_old_quest.data_quest[i][0] == type
                                    && temp_old_quest.data_quest[i][1] == id_mob) {
                                Message mq = new Message(25);
                                mq.writer().writeShort(id_mob);
                                mq.writer().writeByte(temp_old_quest.data_quest[i][0] == 1 ? 1 : 5);
                                mq.writer().writeShort(temp_old_quest.data_quest[i][2]);
                                mq.writer().writeShort(temp_old_quest.data_quest[i][2]);
                                this.addmsg(mq);
                                mq.cleanup();
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public int get_ticket() {
        return ticket;
    }

    public void update_ticket(int i) {
        if (i < 0 && this.cd_ticket_next < System.currentTimeMillis()) {
            this.cd_ticket_next = System.currentTimeMillis() + (60_000L * 10); // 10p
        }
        this.ticket += i;
        if (this.ticket >= this.get_ticket_max()) {
            // this.ticket = (short) this.get_ticket_max();
            this.cd_ticket_next = System.currentTimeMillis() + (60_000L * 10); // 10p
        }
        if (this.ticket >= 100) {
            this.ticket = (short) 100;
            this.cd_ticket_next = System.currentTimeMillis() + (60_000L * 10); // 10p
        }
    }

    public void update_pvp_ticket(int i) {
        if (i < 0 && this.cd_pvp_next < System.currentTimeMillis()) {
            this.cd_pvp_next = System.currentTimeMillis() + (60_000L * 60 * 2); // 2h
        }
        this.pvp_ticket += i;
        if (this.pvp_ticket >= this.get_pvp_ticket_max()) {
            // this.pvp_ticket = (byte) this.get_pvp_ticket_max();
            this.cd_pvp_next = System.currentTimeMillis() + (60_000L * 60 * 2); // 2h
        }
        if (this.pvp_ticket >= 100) {
            this.pvp_ticket = (byte) 100;
            this.cd_pvp_next = System.currentTimeMillis() + (60_000L * 60 * 2); // 2h
        }
    }

    public void update_key_boss(int i) {
        if (i < 0 && this.cd_keyboss_next < System.currentTimeMillis()) {
            this.cd_keyboss_next = System.currentTimeMillis() + (60_000L * 60 * 1); // 1h
        }
        this.key_boss += i;
        if (this.key_boss >= this.get_key_boss_max()) {
            // this.key_boss = (byte) this.get_key_boss_max();
            this.cd_keyboss_next = System.currentTimeMillis() + (60_000L * 60 * 1); // 1h
        }
        if (this.key_boss >= 100) {
            this.key_boss = (byte) 100;
            this.cd_keyboss_next = System.currentTimeMillis() + (60_000L * 60 * 1); // 1h
        }
    }

    public int get_ticket_max() {
        if (level < 10) {
            return 20;
        } else if (level < 20) {
            return 22;
        } else if (level < 30) {
            return 24;
        } else if (level < 40) {
            return 26;
        } else if (level < 50) {
            return 28;
        } else if (level <= 100) {
            return 30;
        } else {
            return 20;
        }
    }

    public int get_pvp_ticket_max() {
        if (level < 10) {
            return 3;
        } else if (level < 20) {
            return 4;
        } else if (level <= 100) {
            return 5;
        } else {
            return 3;
        }
    }

    public int get_key_boss_max() {
        if (level < 10) {
            return 3;
        } else if (level < 20) {
            return 4;
        } else if (level < 30) {
            return 5;
        } else if (level < 40) {
            return 6;
        } else if (level < 50) {
            return 7;
        } else if (level <= 100) {
            return 8;
        } else {
            return 3;
        }
    }

    public int get_pvp_ticket() {
        if (this.isBot) {
            return 999;
        }
        return this.pvp_ticket;
    }

    public int get_key_boss() {
        return this.key_boss;
    }

    public boolean check_already_have_devil_fruit() {
        short[] id_check = new short[]{};
        for (int i = 0; i < id_check.length; i++) {
            if (item.total_item_bag_by_id(4, id_check[i]) > 0
                    || item.total_item_box_by_id(4, id_check[i]) > 0) {
                return true;
            }
        }
        return false;
    }

    public int getTichLuy() {
        return this.coin;
    }

    public void update_pvpPoint(int i) {
        this.pvppoint += i;
        if (this.pvppoint < 0) {
            this.pvppoint = 0;
        }
    }

    public int get_pvpPoint() {
        return pvppoint;
    }

    public int percentLv() {
        if (template.Level.ENTRYS == null || level < 0 || level >= template.Level.ENTRYS.length) {
            return 100;
        }
        template.Level lev = template.Level.ENTRYS[level];
        if (lev == null || lev.exp <= 0) return 0;
        return (int) Math.min(100, Math.max(0, (exp * 100 / lev.exp)));
    }


    public int get_tyle_ghep_dial() {
        int result = 0;
        Skill_info sk_select = null;
        for (int i = 0; i < this.skill_point.size(); i++) {
            if (this.skill_point.get(i).temp.indexSkillInServer >= 661
                    && this.skill_point.get(i).temp.indexSkillInServer <= 666) {
                sk_select = this.skill_point.get(i);
                break;
            }
        }
        if (sk_select != null) {
            result = sk_select.temp.Lv_RQ + 7;
        }
        return (result * 3);
    }

    public ItemBag47 get_daHanhTrinh(int mapId) {
        int index = HanhTrinh.getIslandCategory(mapId);
        if (index > -1) {
            for (int i = 0; i < this.daHanhTrinh.size(); i++) {
                ItemBag47 item = this.daHanhTrinh.get(i);
                if (item != null && item.category == index && item.quant == 1) {
                    return item;
                }
            }
        }
        return null;
    }

    public int get_icon_daHanhTrinh(int cat) {
        for (int i = 0; i < this.daHanhTrinh.size(); i++) {
            ItemBag47 item = this.daHanhTrinh.get(i);
            if (item != null && item.category == cat && item.quant == 1) {
                ItemTemplate4 it = ItemTemplate4.get_it_by_id(item.id);
                if (it != null) {
                    return it.icon;
                }
            }
        }
        return -1;
    }

    public List<ItemBag47> get_list_daHanhTrinh_total(int mapId) {
        int index = HanhTrinh.getIslandCategory(mapId);
        List<ItemBag47> result = new ArrayList<>();
        if (index > -1) {
            for (int i = 0; i < this.daHanhTrinh.size(); i++) {
                ItemBag47 item = this.daHanhTrinh.get(i);
                if (item != null && item.category == index && item.quant == 0) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    public void update_wanted_point(int i) {
        long value = this.wanted_point;
        if ((value + i) <= 2_000_000_000L) {
            this.wanted_point += i;
            if (this.wanted_point < 0) {
                this.wanted_point = 0;
            }
        }
    }

    public int get_wanted_point() {
        return this.wanted_point;
    }

    public MyPet get_pet() {
        for (int i = 0; i < this.my_pet.size(); i++) {
            if (this.my_pet.get(i).isUse) {
                return this.my_pet.get(i);
            }
        }
        return null;
    }

    public int checkQuest() {
        if (core.Manager.gI().isTestMode()) {
            return 9999;
        }
        QuestP q = getMainQuest();
        if (q != null && q.template != null && q.template.typeMainSub == 0 && q.template.id >= 0) {
            int qId = q.template.id;
            if (qId < 24) {
                return 7; // Foosha
            } else if (qId < 49) {
                return 15; // Shells Town
            } else if (qId < 71) {
                return 23; // Orange
            } else if (qId < 99) {
                return 31; // Sirup
            } else if (qId < 119) {
                return 39; // Baratie
            } else if (qId < 141) {
                return 47; // Cocoyasi
            } else if (qId < 160) {
                return 63; // Loguetown / Mom Sinh Doi
            } else if (qId < 190) {
                return 77; // Whiskey Peak
            } else if (qId < 210) {
                return 90; // Little Garden / Horn
            } else if (qId < 242) {
                return 105; // Nanohana / Alabasta
            } else if (qId < 253) {
                return 127; // Jaya / Skypiea
            } else if (qId < 283) {
                return 211; // Water 7
            }
        }
        return 9999;
    }

    public void skipAllQuestsToEnd() {
        if (this.list_quest == null) {
            this.list_quest = new ArrayList<>();
        }
        QuestP mainQ = this.getMainQuest();
        if (mainQ == null) {
            mainQ = new QuestP();
            this.list_quest.add(0, mainQ);
        }
        mainQ.template = Quest.QUEST_FINISH;
        mainQ.data = new short[0][];

        if (this.item != null && this.item.bag47 != null) {
            List<template.ItemBag47> list_remove = new ArrayList<>();
            for (int i = 0; i < this.item.bag47.size(); i++) {
                if (this.item.bag47.get(i).category == 5) {
                    list_remove.add(this.item.bag47.get(i));
                }
            }
            this.item.bag47.removeAll(list_remove);
            this.item.updateInventory(false);
        }

        try {
            Quest.send_List_Quest(this, false);
            Quest.update_map_have_side_quest(this, false);
        } catch (Exception ignored) {}

        this.getService().send_box_ThongBao_OK("Đã bỏ qua toàn bộ nhiệm vụ! Toàn bộ bản đồ đã được mở khóa.");
    }

    public void resetQuestToBegin() {
        if (this.list_quest == null) {
            this.list_quest = new ArrayList<>();
        }
        this.list_quest.clear();
        QuestP startQuest = new QuestP();
        startQuest.template = Quest.get_quest((short) 0);
        if (startQuest.template != null) {
            startQuest.data = new short[startQuest.template.data_quest != null ? startQuest.template.data_quest.length : 0][];
            for (int i = 0; i < startQuest.data.length; i++) {
                startQuest.data[i] = new short[startQuest.template.data_quest[i].length];
                for (int j = 0; j < startQuest.data[i].length; j++) {
                    startQuest.data[i][j] = startQuest.template.data_quest[i][j];
                }
            }
            this.list_quest.add(startQuest);
        }

        try {
            Quest.send_List_Quest(this, false);
            Quest.update_map_have_side_quest(this, false);
        } catch (Exception ignored) {}

        this.getService().send_box_ThongBao_OK("Đã đặt lại nhiệm vụ về Tân Thủ (Nhiệm vụ 0). Hãy bắt đầu làm từ đầu!");
    }

    public EventData getDataEvent(int type) {
        for (EventData ev : eventData) {
            if (ev.eventID == type) {
                return ev;
            }
        }
        return null;
    }

    public EventData getDataEvent(int type, String seasonKey) {
        if (seasonKey == null || seasonKey.trim().isEmpty()) {
            return getDataEvent(type);
        }
        for (EventData ev : eventData) {
            if (ev.eventID == type && seasonKey.equals(ev.key)) {
                return ev;
            }
        }
        return null;
    }

    private void load_data_event() {
        for (event.Event ev : event.EventManager.gI().getActiveEventsList()) {
            if (ev == null) continue;
            EventData temp_select = null;
            String seasonKey = ev.getSeasonKey();
            for (EventData d : this.eventData) {
                if (d.eventID == ev.getId()) {
                    if (seasonKey != null && seasonKey.equals(d.key)) {
                        temp_select = d;
                        break;
                    }
                }
            }
            if (temp_select == null && (seasonKey == null || seasonKey.isEmpty())) {
                for (EventData d : this.eventData) {
                    if (d.eventID == ev.getId() && (d.key == null || d.key.isEmpty())) {
                        temp_select = d;
                        break;
                    }
                }
            }
            if (temp_select == null) {
                temp_select = new EventData();
                temp_select.eventID = ev.getId();
                temp_select.key = seasonKey; // Set clean season key
                temp_select.data = new int[35];
                this.eventData.add(temp_select);
            }
            if (temp_select.data == null || temp_select.data.length < 35) {
                int[] nd = new int[35];
                if (temp_select.data != null) System.arraycopy(temp_select.data, 0, nd, 0, temp_select.data.length);
                temp_select.data = nd;
            }
            event.EventManager.dispatchInitPlayerData(temp_select, ev.getId());
        }
    }

    public void updateHk(long xp) throws IOException {
//        xp *= 100;
        List<Skill_info> skSelect = new ArrayList<>();
        for (int i = 0; i < this.skill_point.size(); i++) {
            if (this.skill_point.get(i).temp.indexSkillInServer >= 672 && this.skill_point.get(i).temp.indexSkillInServer <= 690) {
                skSelect.add(this.skill_point.get(i));
            }
        }
        long[] xpUp = new long[]{xp};
        boolean[] b = new boolean[]{false};
        skSelect.forEach(l -> {
            l.exp += xpUp[0];
            if (l.temp.Lv_RQ < 6 && l.exp >= Skill_info.EXP[l.temp.Lv_RQ - 1]) {
                l.exp -= Skill_info.EXP[l.temp.Lv_RQ - 1];
                String name = l.temp.name;
                int old = l.temp.Lv_RQ;
                l.temp = Skill_Template.get_nextHk(name, old);
                b[0] = true;
            }
            if (l.temp.Lv_RQ == 6) {
                l.exp = 0;
            }
        });
        if (b[0]) {
            this.send_skill();
            this.update_info_to_all();
        }

    }

    public boolean checkPassRuong() throws IOException {
        if (!this.passBagOK) {
            new model.InputDialog(this, 13, "Nhập mã bảo vệ:", new String[]{""}).startInput();
        }
        return this.passBagOK;
    }

    public int getTieuRuby() {
        return this.tieuRuby;
    }

    public void removeEff(EffTemplate effTemplate) {
        if (this.list_eff != null && effTemplate != null) {
            this.list_eff.remove(effTemplate);
        }
    }

    // === Ability & Stat Cache Management ===
    public void setAbility() {
        int fullSetId = ThanTrangConfig.getFullSetId(this);
        if (fullSetId == 0 && (this.isTransformThanTrang || this.transformThanTrangSetId > 0)) {
            ThanTrangConfig.onPlayerUnequipOrCancel(this);
        }
        boolean hotkeyChanged = false;
        if (fullSetId > 0 && fullSetId <= ThanTrangConfig.TOTAL_SETS) {
            hotkeyChanged = this.updateThanTrangRmsHotKey(ThanTrangConfig.getSkillBySetId(fullSetId));
        }
        if (this.ability != null) {
            this.ability.setAbility(this);
        }
        try {
            this.send_skill();
            if (hotkeyChanged && this.getService() != null) {
                this.getService().sendRms((byte) 0);
            }
        } catch (Exception ignored) {}
    }

    // Standard Clean Getters for Player Stats
    public int getDame() { return dame; }
    public int getDef() { return def; }
    public int getHpMax() { return hpMax; }
    public int getMpMax() { return mpMax; }
    public int getAgility() { return agility; }
    public int getCrit() { return crit; }
    public int getPierce() { return pierce; }
    public int getMiss() { return miss; }
    public int getReactDame() { return reactDame; }
    public int getResPhys() { return resPhys; }
    public int getResMag() { return resMag; }
    public int getDamePercent() { return damePercent; }
    public int getDefPercent() { return defPercent; }

    // Backward compatibility aliases
    public int get_hp_max() { return getHpMax(); }
    public int get_hp_max(boolean have_eff) { return getHpMax(); }
    public int get_mp_max() { return getMpMax(); }
    public int get_mp_max(boolean have_eff) { return getMpMax(); }
    public int get_dame() { return getDame(); }
    public int get_dame(boolean have_eff) { return getDame(); }
    public int get_def() { return getDef(); }
    public int get_def(boolean have_eff) { return getDef(); }
    public int get_agility() { return getAgility(); }
    public int get_agility(boolean have_eff) { return getAgility(); }
    public int get_crit() { return getCrit(); }
    public int get_crit(boolean have_eff) { return getCrit(); }
    public int get_pierce() { return getPierce(); }
    public int get_pierce(boolean have_eff) { return getPierce(); }
    public int get_miss() { return getMiss(); }
    public int get_miss(boolean have_eff) { return getMiss(); }
    

    public void attackPlayer(Player[] targets, int idSkill, long dame) {
        if (!this.isBot) {
            bot.botplayer.GameAnalyzer.learnPlayerSkill(this.clazz, idSkill);
        }
        if (this.map != null) {
            try {
                this.Fire_Player(targets, this, idSkill, dame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void attackMob(mob.Mob[] targets, int idSkill, long dame) {
        if (!this.isBot) {
            bot.botplayer.GameAnalyzer.learnPlayerSkill(this.clazz, idSkill);
        }
        if (this.map != null) {
            try {
                long[] exp_up = this.Fire_Monster(targets, this, idSkill, dame);
                if (exp_up != null && (exp_up[0] > 0 || exp_up[1] > 0)) {
                    if (this instanceof DeTu) {
                        DeTu dt = (DeTu) this;
                        if (exp_up[0] > 0) {
                            dt.update_exp(exp_up[0], true);
                            if (dt.master != null) {
                                long masterExp = exp_up[0] / 2;
                                if (masterExp > 0) {
                                    dt.master.update_exp(masterExp, true);
                                }
                            }
                        }
                    } else if (this instanceof bot.mercenary.MercenaryBot) {
                        bot.mercenary.MercenaryBot merc = (bot.mercenary.MercenaryBot) this;
                        Player owner = merc.getOwner();
                        if (owner != null && exp_up[0] > 0) {
                            long ownerExp = exp_up[0] / 2;
                            if (ownerExp > 0) {
                                owner.update_exp(ownerExp, true);
                            }
                        }
                    } else if (this.isBot) {
                        this.update_exp(exp_up[0] + exp_up[1], true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Fire_Player(Player[] list_target, int idSkill, long dame) throws IOException {
        Fire_Player(list_target, this, idSkill, dame);
    }

    public void Fire_Player(Player[] list_target, Player p, int idSkill, long dame) throws IOException {
        if (p == null || list_target == null || p.map == null) {
            return;
        }
        Zone map = p.map;
        if (Zone.isWaitingOrUnstartedMatch(map)) {
            return;
        }
        Skill_info sk_temp = p.get_skill_temp(idSkill);
        if (sk_temp == null || sk_temp.temp == null || !map.can_PK
                || (map.map_vp != null && (map.map_vp.num_win_p1 == 3
                || map.map_vp.num_win_p2 == 3 || map.map_vp.status_pvp != 3))) {
            return;
        }
        int dame_plus_percent = 0;
        int dame_magic_plus_percent = p.ability.get_dame_ap();
        int crit_skill = p.ability.get_crit(true);
        int multi_dame_skill = p.ability.get_multi_dame_when_crit(true);
        boolean crit = false;
        //
        List<Dame_Msg> list = new ArrayList<>();
        long dame_mine_all = 0;
        long damebefore = dame;
        long dame2;
        EffTemplate eff;
        //
        for (int i = 0; i < list_target.length; i++) {
            Player p_target = list_target[i];
            if (p.isSpectator || (p_target != null && p_target.isSpectator)) {
                continue;
            }
            if (p_target != null && p_target.index_map != p.index_map && !p_target.isdie && !p.isdie
                    && (p_target.time_can_mob_atk - 1000) < System.currentTimeMillis()) {
                if (Zone.isWaitingOrUnstartedMatch(p_target.map)) {
                    continue;
                }
                if (!p.canAttackTargetPlayer(p_target)) {
                    continue;
                }
                ItemFashionP2 checkF = p.check_fashion(120);
                if (checkF != null && checkF.is_use && i == 0 && p_target.get_eff(21) == null) { // tt
                    // zombie
                    if (5 > ZUtil.random(120)) {
                        p_target.add_new_eff(21, ZUtil.random(28), 5000);
                        p_target.update_info_to_all();
                        //
                        if (map.players != null) {
                            for (int j = 0; j < map.players.size(); j++) {
                                p_target.getService().charWearing(map.players.get(j), false);
                            }
                        }
                    }
                }
                dame2 = (damebefore > 0) ? damebefore : Math.max(100L, (long) p.level * 50L);
                eff = p_target.get_eff(23);
                if (eff != null) { // vuong kaido thoi trang
                    dame2 /= 2;
                }
                dame2 = (dame2 * (1000L + dame_plus_percent)) / 1000L;
                long baseDame0 = (p.skill_point != null && !p.skill_point.isEmpty() && p.skill_point.get(0) != null) ? p.skill_point.get(0).get_dame(p) : 100;
                if (baseDame0 <= 0) baseDame0 = 100;
                long skDame = (sk_temp != null) ? sk_temp.get_dame(p) : baseDame0;
                if (skDame <= 0) skDame = baseDame0;
                dame2 = (dame2 * skDame) / baseDame0;
                long def = p_target.ability.get_def(true);
                def = (def * (1000L + (long) p_target.ability.get_def_percent(true))) / 1_000L;

                // [TÍCH HỢP OPTION 13 (Xuyên giáp), 50 (Giảm xuyên giáp đ/t), 70 (Giảm thủ cuối)]
                int netPierce = Math.max(0, p.ability.get_pierce(true) - p_target.ability.get_pierce_reduce());
                if (netPierce > 0) {
                    long piercePct = Math.min(850L, (long) netPierce); // Giới hạn xuyên giáp tối đa 85%
                    def = (def * (1000L - piercePct)) / 1000L;
                }
                int defReduce = (int) p.ability.get_def_target_reduce(); // Option 70
                if (defReduce > 0) {
                    long defRedPct = Math.min(500L, (long) defReduce); // Giới hạn giảm thủ cuối tối đa 50%
                    def = (def * (1000L - defRedPct)) / 1000L;
                }
                dame2 -= def;
                if (dame2 <= 0) {
                    dame2 = 1;
                }

                // [TÍCH HỢP OPTION 26: Kháng vật lý (Soft-cap 70%)]
                int resPhys = p_target.ability.get_dame_resist(true);
                if (resPhys > 0 && dame2 > 0) {
                    long resPhysPct = Math.min(700L, (long) resPhys);
                    dame2 = (dame2 * (1000L - resPhysPct)) / 1000L;
                    if (dame2 <= 0) {
                        dame2 = 1;
                    }
                }

                // [TÍCH HỢP CHỈ SỐ PVP CHUẨN XỊN: Option 81 (Sát thương PvP) & Option 82 (Giảm ST nhận từ người)]
                int pvpAtk = p.ability.get_pvp_dame();
                if (pvpAtk > 0 && dame2 > 0) {
                    dame2 = (dame2 * (1000L + (long) Math.min(500, pvpAtk))) / 1000L;
                }
                int pvpDef = p_target.ability.get_pvp_dame_reduce();
                if (pvpDef > 0 && dame2 > 0) {
                    dame2 = (dame2 * (1000L - (long) Math.min(600, pvpDef))) / 1000L;
                }

                // [TÍCH HỢP OPTION 83: Kháng chí mạng của đối thủ]
                int netCrit = crit_skill - p_target.ability.get_crit_reduce() - p_target.ability.get_crit_resist();
                crit = netCrit > ZUtil.random(1000);

                //
                long dame_mine = 0;
                Dame_Msg dame_inf = new Dame_Msg();
                dame_inf.data = new ArrayList<>();
                dame_inf.targetP = p_target;
                if (dame2 > 0 && idSkill != 0) {
                    dame_inf.dameM
                            = (skDame * (dame_magic_plus_percent))
                            / 1000;
                    // [TÍCH HỢP OPTION 27 (Kháng phép) & OPTION 65 (Xuyên kháng phép)]
                    if (dame_inf.dameM > 0) {
                        int resMag = p_target.ability.get_dame_resist_ap(true);
                        int mPierce = p.ability.get_magic_pierce(); // Option 65
                        int netResMag = Math.max(0, resMag - mPierce);
                        if (netResMag > 0) {
                            long resMagPct = Math.min(700L, (long) netResMag); // Soft-cap 70%
                            dame_inf.dameM = (dame_inf.dameM * (1000L - resMagPct)) / 1000L;
                        }
                    }
                }
                if (dame_inf.dameM < 0) {
                    dame_inf.dameM = 0;
                }
                if (dame2 > 0 && idSkill == 2038 || idSkill == 2041) {
                    if (p.get_eff(6) != null) {
                        dame2 = (dame2 * 115) / 100;
                    }
                    // vuong fashion bao dom + chim ung
                    for (int i12 = 0; i12 < p.fashion.size(); i12++) {
                        if ((p.fashion.get(i12).id == 33 || p.fashion.get(i12).id == 34)
                                && p.fashion.get(i12).is_use) {
                            dame2 = (dame2 * 115) / 100;
                            break;
                        }
                    }
                }
                int react_dame_
                        = p_target.ability.get_dame_react(true) - p.ability.get_dame_react_reduce() - p.ability.get_ignore_react();
                int MienThuong = p_target.ability.get_dame_skip(true) - p.ability.get_dame_skip_reduce();
                if (MienThuong < 0) {
                    MienThuong = 0;
                }
                if (MienThuong > 700) { // Giới hạn an toàn Hard-Cap 70%
                    MienThuong = 700;
                }

                if (p_target.ability.getGiamDame() > ZUtil.random(1200)) {
                    dame2 /= 10;
                }

                int get_miss = p_target.ability.get_miss(true) - p.ability.get_miss_reduce();
                // [TÍCH HỢP OPTION 77: Giảm né cuối & OPTION 90: Bỏ qua né tránh]
                int ignoreMiss = p.ability.get_miss_target_reduce() + p.ability.get_ignore_miss();
                if (ignoreMiss > 0) {
                    get_miss = Math.max(0, get_miss - ignoreMiss);
                }
                boolean miss = ((p.get_eff(205) != null || p_target.get_eff(24) != null
                        || get_miss > ZUtil.random(1000)));

                if (miss) { // miss
                    dame2 = 0;
                }
                int kich_an;
                if (dame2 > 0) {
                    // [KÍCH ẨN PHÒNG THỦ ĐỘC QUYỀN - 1 HIT CHỈ XÉT 1 HIỆU ỨNG]
                    if (p_target.get_eff(300) != null) {
                        dame2 = 0;
                        dame_inf.dameM = 0;
                    } else {
                        List<Integer> defKichAnList = new ArrayList<>();
                        for (int kaId = 0; kaId <= 3; kaId++) {
                            int kaCount = p_target.ability.get_kich_an(kaId);
                            if (kaCount > 0 && p_target.get_eff(400 + kaId) == null) {
                                for (int k = 0; k < kaCount; k++) {
                                    defKichAnList.add(kaId);
                                }
                            }
                        }
                        if (!defKichAnList.isEmpty()) {
                            int chosenDefKa = defKichAnList.get(ZUtil.random(defKichAnList.size()));
                            int kaCount = p_target.ability.get_kich_an(chosenDefKa);
                            if (chosenDefKa == 0) { // Bất tử (Tier S: 3.5% base)
                                int chance = 35 + Math.min(50, (kaCount - 1) * 10);
                                if (chance > ZUtil.random(1000)) {
                                    dame2 = 0;
                                    dame_inf.dameM = 0;
                                    int time_eff = 2; // 2.0s
                                    p_target.add_new_eff(300, 1, time_eff * 1_000);
                                    p_target.add_new_eff(400, 1, 60_000);
                                    map.getService().send_kich_an(p, p_target, time_eff, 0, 0, 0);
                                }
                            } else if (chosenDefKa == 1) { // Lời cảm ơn (Tier B Lỏ: 13.0% base)
                                int chance = 130 + Math.min(100, (kaCount - 1) * 20);
                                if (chance > ZUtil.random(1000)) {
                                    int hpMax = p_target.ability.get_hp_max(true);
                                    long healPct = 5L + (kaCount - 1);
                                    long damePct = 8L + (kaCount - 1) * 2L;
                                    int heal = (int) Math.min((hpMax * healPct) / 100L, (dame2 * damePct) / 100L);
                                    p_target.hp = Math.min(hpMax, p_target.hp + heal);
                                    p_target.add_new_eff(401, 1, 30_000);
                                    map.getService().send_kich_an(p, p_target, 1, 1, 0, heal);
                                    dame2 = 0;
                                    dame_inf.dameM = 0;
                                }
                            } else if (chosenDefKa == 2) { // Lá chắn (Tier A: 6.0% base)
                                int chance = 60 + Math.min(75, (kaCount - 1) * 15);
                                if (chance > ZUtil.random(1000)) {
                                    p_target.add_new_eff(402, 1, 60_000);
                                    map.getService().send_kich_an(p, p_target, 1, 2, 5, 50);
                                    int time_eff = 1_500 + Math.min(1000, (kaCount - 1) * 200); // 1.5s -> 2.5s Choáng
                                    eff = p.get_eff(201);
                                    if (eff == null) {
                                        p.add_new_eff(201, 1, time_eff);
                                    } else {
                                        eff.time = System.currentTimeMillis() + time_eff;
                                    }
                                    if (p_target.map != null) {
                                        p_target.map.getService().send_choang(p_target, p, time_eff);
                                    }
                                    dame2 = 0;
                                    dame_inf.dameM = 0;
                                }
                            } else if (chosenDefKa == 3) { // Khóa năng lượng (Tier S: 4.0% base)
                                int chance = 40 + Math.min(60, (kaCount - 1) * 12);
                                if (chance > ZUtil.random(1000)) {
                                    p_target.add_new_eff(403, 1, 60_000);
                                    long burnPct = 25L + Math.min(25L, (kaCount - 1) * 5L);
                                    int mp_burn = (int) ((p.mp * burnPct) / 100L);
                                    p.mp = Math.max(0, p.mp - mp_burn);
                                    map.getService().send_kich_an(p, p_target, 5, 3, 0, mp_burn);
                                    dame2 = 0;
                                    dame_inf.dameM = 0;
                                }
                            }
                        }
                    }

                    // [KÍCH ẨN TẤN CÔNG ĐỘC QUYỀN - 1 HIT CHỈ XÉT 1 HIỆU ỨNG]
                    if (dame2 > 0) {
                        ItemFashionP2 checkF128 = p.check_fashion(128); // Fashion 128
                        if (checkF128 != null && checkF128.is_use) {
                            if (50 > ZUtil.random(1000)) {
                                eff = p.get_eff(305);
                                if (eff != null) {
                                    crit = true;
                                } else {
                                    eff = p.get_eff(405);
                                    if (eff == null) {
                                        int time_eff = 3;
                                        p.add_new_eff(305, 1, time_eff * 1_000);
                                        map.getService().send_kich_an(p_target, p, time_eff, 5, 0, 0);
                                        p.add_new_eff(405, 1, 60_000);
                                        crit = true;
                                    }
                                }
                            }
                        }
                        
                        if (p.get_eff(305) != null) {
                            crit = true;
                        }

                        List<Integer> atkKichAnList = new ArrayList<>();
                        for (int kaId = 4; kaId <= 6; kaId++) {
                            int kaCount = p.ability.get_kich_an(kaId);
                            if (kaCount > 0 && p.get_eff(400 + kaId) == null) {
                                for (int k = 0; k < kaCount; k++) {
                                    atkKichAnList.add(kaId);
                                }
                            }
                        }
                        if (!atkKichAnList.isEmpty()) {
                            int chosenAtkKa = atkKichAnList.get(ZUtil.random(atkKichAnList.size()));
                            int kaCount = p.ability.get_kich_an(chosenAtkKa);
                            if (chosenAtkKa == 4) { // Bộc phá (Tier S: 4.0% base)
                                int chance = 40 + Math.min(75, (kaCount - 1) * 15);
                                if (chance > ZUtil.random(1000)) {
                                    p.add_new_eff(404, 1, 60_000);
                                    long mult = 14L + Math.min(6L, (kaCount - 1) * 1L); // x1.4 -> x2.0
                                    dame2 = (dame2 * mult) / 10L;
                                    dame_inf.dameM = (dame_inf.dameM * mult) / 10L;
                                    map.getService().send_kich_an(p_target, p, 1, 4, 0, (int) dame2);
                                }
                            } else if (chosenAtkKa == 5) { // Tập trung cao độ (Tier A: 6.5% base)
                                if (p.get_eff(305) != null) {
                                    crit = true;
                                } else {
                                    int chance = 65 + Math.min(75, (kaCount - 1) * 15);
                                    if (chance > ZUtil.random(1000)) {
                                        int time_eff = 3 + Math.min(3, (kaCount - 1));
                                        p.add_new_eff(305, 1, time_eff * 1_000);
                                        map.getService().send_kich_an(p_target, p, time_eff, 5, 0, 0);
                                        p.add_new_eff(405, 1, 60_000);
                                        crit = true;
                                    }
                                }
                            } else if (chosenAtkKa == 6) { // Ma cà rồng (Tier S: 4.0% base)
                                int chance = 40 + Math.min(75, (kaCount - 1) * 15);
                                if (chance > ZUtil.random(1000)) {
                                    p.add_new_eff(406, 1, 60_000);
                                    int hpMax = p.ability.get_hp_max(true);
                                    long healPct = 12L + Math.min(18L, (kaCount - 1) * 3L);
                                    long capPct = 8L + Math.min(12L, (kaCount - 1) * 2L);
                                    int hpHeal = (int) Math.min((hpMax * capPct) / 100L, (dame2 * healPct) / 100L);
                                    p.hp = Math.min(hpMax, p.hp + hpHeal);
                                    map.getService().send_kich_an(p_target, p, 1, 6, 0, hpHeal);
                                }
                            }
                        }
                    }

                    // [KÍCH ẨN TÍCH LŨY]
                    // 7: Đánh là choáng
                    int ka7 = p.ability.get_kich_an(7);
                    if (ka7 > 0) {
                        eff = p.get_eff(407);
                        if (eff == null) {
                            int per = Math.max(10, 16 - (ka7 - 1));
                            if (p.getDanhLaChoang() >= per) {
                                p.resetDanhLaChoang();
                                p.add_new_eff(407, 1, 60_000);
                                map.getService().send_kich_an(p_target, p, 1, 7, 5, 50);
                                int time_eff = 2_000 + Math.min(1000, (ka7 - 1) * 200); // 2.0s -> 3.0s
                                eff = p_target.get_eff(201);
                                if (eff == null) {
                                    p_target.add_new_eff(201, 1, time_eff);
                                } else {
                                    eff.time = System.currentTimeMillis() + time_eff;
                                }
                                if (p.map != null) {
                                    p.map.getService().send_choang(p, p_target, time_eff);
                                }
                            }
                        } else {
                            p.setDanhLaChoang(0);
                        }
                    }

                    // 8: Thanh lọc
                    int ka8 = p.ability.get_kich_an(8);
                    if (ka8 > 0) {
                        eff = p.get_eff(408);
                        if (eff == null) {
                            int per = Math.max(4, 8 - (ka8 - 1));
                            if (p.getThanhLoc() >= per) {
                                p.resetThanhLoc();
                                p.add_new_eff(408, 1, 30_000);
                                map.getService().send_kich_an(p_target, p, 1, 8, 0, 0);
                                p.send_kich_an();
                            }
                        } else {
                            p.setThanhLoc(0);
                        }
                    }

                    // 9 & 10: Kích ẩn phòng thủ tích lũy (Nén đau 15 hits / Giải phóng năng lượng 22 hits)
                    p_target.processDefensiveKichAnHit(p, map);

                    if (dame2 > 0) {
                        dame2 = (dame2 * (1000L + p.ability.get_percent_final_dame())) / 1000L;
                        if (dame2 > 1 && crit) {
                            dame2 = (dame2 * (1000L + multi_dame_skill)) / 1000L;
                            int dame_crit_decrease = p_target.ability.get_multi_dame_decrease();
                            dame2 = (dame2 * (1000L - Math.min(800L, (long) dame_crit_decrease))) / 1000L;
                            if (dame2 < 1) {
                                dame2 = 1;
                            }
                        }
                        //
                        int percent_hp_target = p.ability.get_dame_percent_hp_target();
                        if (damebefore > 0 && percent_hp_target > 0) {
                            long hp_target = p_target.hp;
                            hp_target = hp_target * (percent_hp_target) / 1000L;
                            hp_target = (hp_target * (1000 - ((MienThuong * 3) / 5))) / 1000;
                            dame2 += hp_target;
                        }
                        //
                        dame2 = (dame2 * (1000 - MienThuong)) / 1000;
                    }
                }

                // 11: Người bất tử (Hồi 0.6% Max HP mỗi món mỗi hit)
                kich_an = p.ability.get_kich_an(11);
                if (kich_an > 0 && p.get_eff(202) == null) {
                    int hp_max = p.ability.get_hp_max(true);
                    int hp_absorb_kichan = (int) ((hp_max * 6L * kich_an) / 1000L); // 0.6% HP per item
                    if (hp_absorb_kichan > 0) {
                        map.getService().send_kich_an(p_target, p, hp_absorb_kichan / 10, 11, 0, 0);
                        p.hp = Math.min(hp_max, p.hp + hp_absorb_kichan);
                    }
                }

                if ((p_target.get_eff(7) != null && p_target.type_pk == -1 && !p.isBot)
                        || p_target.get_eff(9) != null || p_target.get_eff(300) != null || miss) {
                    dame2 = 0;
                    dame_inf.dameM = 0;
                    dame_mine = 0;
                }
                
                dame_inf.dameP = dame2;
                long dame_to_target = dame2 + dame_inf.dameM;

                // [TÍCH HỢP OPTION 57: Sát thương chuẩn & OPTION 87: Kháng sát thương chuẩn]
                int trueDmgPct = p.ability.get_true_dame();
                if (trueDmgPct > 0 && !miss && p_target.get_eff(9) == null && p_target.get_eff(300) == null) {
                    long addedTrueDmg = (skDame * (long) trueDmgPct) / 1000L;
                    int resTrueDmg = p_target.ability.get_true_dame_resist(); // Option 87
                    if (resTrueDmg > 0) {
                        addedTrueDmg = (addedTrueDmg * (1000L - (long) Math.min(700, resTrueDmg))) / 1000L;
                    }
                    dame_to_target += addedTrueDmg;
                }

                if (p_target.get_eff(9) != null || p_target.get_eff(300) != null) {
                    dame_to_target = 0;
                } else if (dame_to_target > 0) {
                    dame_to_target = bot.BotBalanceEngine.applyCombatDamageBalance(p, p_target, dame_to_target, crit);
                    dame_inf.dameP = Math.max(1, dame_to_target - dame_inf.dameM);
                }

                if (dame_to_target > 0) {
                    p_target.hp -= dame_to_target;
                }
                if (p_target.get_eff(9) != null || p_target.get_eff(300) != null) {
                    if (p_target.hp < 1) p_target.hp = 1;
                }
                p_target.lastAttacker = p;
                p_target.lastAttackedTime = System.currentTimeMillis();
                if (p_target instanceof bot.Bot) {
                    ((bot.Bot) p_target).onAttackedBy(p);
                }

                // [PHẢN ĐÒN CHUẨN GỐC op14 + OPTION 38 (Kháng phản đòn) + OPTION 91 (Bỏ qua phản đòn)]
                if (dame_to_target >= 1 && !miss && p_target.get_eff(300) == null && p_target.get_eff(9) == null) {
                    int react_pct = p_target.ability.get_dame_react(true) - p.ability.get_dame_react_reduce() - p.ability.get_ignore_react();
                    if (react_pct > 0 && react_pct > ZUtil.random(1000)) {
                        long reflected = dame_to_target;
                        int antiReact = p.ability.get_anti_react(); // Option 38
                        if (antiReact > 0) {
                            reflected = (reflected * (1000L - Math.min(700L, (long) antiReact))) / 1000L;
                        }
                        dame_mine = reflected;
                    }
                }

                // [HẤP THỤ SÁT THƯƠNG DEFENDER op58 + op73 (HP) & op74 (MP)]
                long HapThuHP = 0;
                if (dame_to_target >= 1 && p_target.hp > 0 && !miss) {
                    int absorb_pct = p_target.ability.total_param_item(58, true) + p_target.ability.total_param_item(73, true);
                    if (absorb_pct > 0 && absorb_pct > ZUtil.random(1000)) {
                        long absorbHeal = (dame_to_target * (long) Math.min(120, absorb_pct)) / 1000L;
                        long maxAbsorbCap = (p_target.ability.get_hp_max(true) * 5L) / 100L; // Cap 5% Max HP
                        HapThuHP = Math.min(absorbHeal, maxAbsorbCap);
                        p_target.hp = Math.min(p_target.ability.get_hp_max(true), (int)(p_target.hp + HapThuHP));
                        p_target.getService().use_potion(0, (int) HapThuHP);
                    }
                    // [TÍCH HỢP OPTION 74: Chuyển hóa sát thương thành MP]
                    int absorbMpPct = p_target.ability.total_param_item(74, true);
                    if (absorbMpPct > 0 && absorbMpPct > ZUtil.random(1000)) {
                        long absorbMp = (dame_to_target * (long) Math.min(100, absorbMpPct)) / 1000L;
                        long maxMpCap = (p_target.ability.get_mp_max(true) * 5L) / 100L;
                        long actualMpHeal = Math.min(absorbMp, maxMpCap);
                        if (actualMpHeal > 0) {
                            p_target.mp = Math.min(p_target.ability.get_mp_max(true), (int)(p_target.mp + actualMpHeal));
                            p_target.getService().use_potion(1, (int) actualMpHeal);
                        }
                    }
                }

                // [HÚT HP & MP ATTACKER op59 + op21 (HP), op22 (MP) & OPTION 92: Kháng hút máu]
                if (dame_to_target >= 1 && p.hp > 0 && !miss) {
                    int lifesteal_pct = p.ability.get_HapThu_Hp();
                    long totalHeal = p.ability.get_hp_atk_absorb(true);
                    if (lifesteal_pct > 0) {
                        totalHeal += (dame_to_target * (long) Math.min(150, lifesteal_pct)) / 1000L;
                    }
                    int antiLifesteal = p_target.ability.get_anti_lifesteal(); // Option 92
                    if (antiLifesteal > 0) {
                        totalHeal = (totalHeal * (1000L - Math.min(700L, (long) antiLifesteal))) / 1000L;
                    }
                    if (totalHeal > 0) {
                        long maxHealCap = (p.ability.get_hp_max(true) * 8L) / 100L; // Cap 8% Max HP
                        long actualHeal = Math.min(totalHeal, maxHealCap);
                        p.hp = Math.min(p.ability.get_hp_max(true), (int)(p.hp + actualHeal));
                        p.getService().use_potion(0, (int) actualHeal);
                    }
                    // [TÍCH HỢP OPTION 22: Hút MP theo sát thương]
                    int mpStealPct = p.ability.get_mp_atk_absorb(true);
                    if (mpStealPct > 0) {
                        long mpHeal = (dame_to_target * (long) Math.min(100, mpStealPct)) / 1000L;
                        long maxMpHealCap = (p.ability.get_mp_max(true) * 5L) / 100L;
                        long actualMpHeal = Math.min(mpHeal, maxMpHealCap);
                        if (actualMpHeal > 0) {
                            p.mp = Math.min(p.ability.get_mp_max(true), (int)(p.mp + actualMpHeal));
                            p.getService().use_potion(1, (int) actualMpHeal);
                        }
                    }
                }

                // [TÍCH HỢP OPTION 40: Hút năng lượng (MP Steal flat)]
                if (dame_to_target >= 1 && p_target.mp > 0 && !miss) {
                    int manaStealFlat = p.ability.total_param_item(40, true);
                    if (manaStealFlat > 0) {
                        int stolen = Math.min(p_target.mp, manaStealFlat);
                        p_target.mp -= stolen;
                        p.mp = Math.min(p.ability.get_mp_max(true), p.mp + stolen);
                        p_target.getService().use_potion(1, -stolen);
                        p.getService().use_potion(1, stolen);
                    }
                }

                // tu choi tu than
                if (p_target.hp <= 0 && p_target.get_eff(10) == null
                        && p_target.ability.get_TuChoiTuThan() > 0) {
                    int time_eff = 5;
                    time_eff = (time_eff * (1000)) / 1000;
                    p_target.add_new_eff(9, 1, time_eff * 1_000);
                    time_eff = 150_000;
                    time_eff = (time_eff * (1000)) / 1000;
                    p_target.add_new_eff(10, 1, time_eff);
                    p_target.getService().send_eff(21, 50);
                    p_target.hp = Math.max(1, p_target.ability.get_hp_max(true) / 10);
                }
                //
                if (p_target.hp <= 0) {
                    p_target.hp = 0;
                    p_target.isdie = true;
                    DauTruongTuDo.UpdateInfo(p, p_target);
                    if (WorldWar.runnning && !WorldWar.mapTranChienLon(map.template.id) && map.map_little_garden == null) {
                        WorldWar.upPoint(p, p_target);
                    } else if (p.type_pk == 0 && p_target.type_pk != 0) {
                        int delta = p.level / 10 - p_target.level / 10;
                        int plus = (p.pointPk > 0) ? (p.pointPk / 5) : 0;
                        if (delta > 0) {
                            p.update_point_pk(100 + (delta * 100) + plus);
                        } else {
                            p.update_point_pk(100 + plus);
                        }
                        //
                        if (p_target.type_pk == -1 && p_target.typePirate == -1) {
                            while (p_target.enemy_list.size() > 50) {
                                p_target.enemy_list.remove(0);
                            }
                            FriendTemp enemy_add = null;
                            for (int j = 0; j < p_target.enemy_list.size(); j++) {
                                if (p_target.enemy_list.get(j).playerId == p.IDPlayer) {
                                    enemy_add = p_target.enemy_list.get(j);
                                    break;
                                }
                            }
                            if (enemy_add != null) {
                                int save_index = p_target.enemy_list.indexOf(enemy_add);
                                FriendTemp save = p_target.enemy_list.get(0);
                                p_target.enemy_list.set(0, enemy_add);
                                p_target.enemy_list.set(save_index, save);
                            } else {
                                enemy_add = new FriendTemp(p);
                                p_target.enemy_list.add(enemy_add);
                                if (p_target.enemy_list.size() >= 2) {
                                    enemy_add.id = p_target.enemy_list
                                            .get(p_target.enemy_list.size() - 2).id + 1;
                                } else {
                                    enemy_add.id = 0;
                                }
                            }
                        }
                    }
                    if (map.template.id == 81 && map.map_little_garden != null) {
                        p_target.time_hs_little_garden = System.currentTimeMillis() + 10_000L;
                        p_target.getService().send_time_cool_down(p_target.time_hs_little_garden, "Hồi sinh", 3);
                    } else if (map.template.id == 179) {
                        p_target.time_hs_little_garden = System.currentTimeMillis() + 10_000L;
                        p_target.getService().send_time_cool_down(p_target.time_hs_little_garden, "Hồi sinh", 3);
                        if (p.clan != null && p.clan.map_create != null && p.clan.map_create.map_thuLinhBienKhoi != null && p_target.clan != null) {
                            ThuLinhBienKhoi.updatePoint(p.clan, p.clan.map_create.map_thuLinhBienKhoi.flag, (p_target.clan.getTypeMem(p_target) == 0 ? 15 : (p_target.clan.getTypeMem(p_target) == 1 ? 10 : 5)));
                        }
                    }

                    if ((map.template.id >= 261 && map.template.id <= 265) || (map.template.id >= 254 && map.template.id <= 258)) {
                        p_target.time_hs_little_garden = System.currentTimeMillis() + 10_000L;
                        p_target.getService().send_time_cool_down(p_target.time_hs_little_garden, "Hồi sinh", 3);
                    }
                }
                // [KÍCH HOẠT HAKI BÁ VƯƠNG (ID 4026)]
                if (dame_inf.dameP > 0) {
                    p.triggerHakiBaVuong(p_target, null);
                }
                if (dame_inf.dameP > 0 && sk_temp.temp.idEffSpec > 0
                        && sk_temp.temp.idEffSpec < 17) {
                    eff = p_target.get_eff(200 + sk_temp.temp.idEffSpec);
                    if (eff == null) {
                        // [TÍCH HỢP OPTION 71: Kháng hiệu ứng & OPTION 84: Kháng choáng]
                        int ccResist = p_target.ability.get_cc_resist();
                        int stunResist = p_target.ability.get_stun_resist();
                        int reduce_Eff = Math.min(700, Math.max(0, p_target.ability.get_reduce_Eff() + ccResist + stunResist));
                        int basePer = sk_temp.temp.perEffSpec;
                        if (basePer > 0 && basePer <= 100) {
                            basePer *= 10;
                        }
                        // [TÍCH HỢP OPTION 75: Tăng % choáng]
                        int stunBoost = p.ability.get_stun_rate();
                        if (stunBoost > 0) {
                            basePer += stunBoost;
                        }
                        int percent = (basePer * (1000 - reduce_Eff)) / 1000;
                        if (percent > 0 && percent > ZUtil.random(1000)) {
                            int time = sk_temp.temp.timeEffSpec;
                            time = (time * (1000 - reduce_Eff)) / 1000;
                            if (time > 0) {
                                p_target.add_new_eff((200 + sk_temp.temp.idEffSpec), 1, (time * 100));
                                dame_inf.data.add(new Option_Dame_Msg(sk_temp.temp.idEffSpec, 1, time));
                                if (sk_temp.temp.idEffSpec == 16) {
                                    p_target.getService().addEffect(p_target.index_map, (short) 5, time * 100, (byte) 1, (byte) 10);
                                }
                            }
                        }
                    }
                }
                // [TÍCH HỢP OPTION 80: Tỷ lệ gây Điện giật & OPTION 85: Kháng tê liệt]
                if (dame_inf.dameP > 0 && p_target.get_eff(207) == null) {
                    int shockRate = p.ability.get_shock_rate();
                    if (shockRate > 0) {
                        int ccResist = p_target.ability.get_cc_resist();
                        int shockResist = p_target.ability.get_shock_resist();
                        int reduce_Eff = Math.min(700, Math.max(0, p_target.ability.get_reduce_Eff() + ccResist + shockResist));
                        int finalShockRate = (shockRate * (1000 - reduce_Eff)) / 1000;
                        if (finalShockRate > ZUtil.random(1000)) {
                            int shockTime = 20; // 2.0s
                            shockTime = (shockTime * (1000 - reduce_Eff)) / 1000;
                            if (shockTime > 0) {
                                p_target.add_new_eff(207, 1, shockTime * 100);
                                dame_inf.data.add(new Option_Dame_Msg(7, 1, shockTime));
                            }
                        }
                    }
                }
                if (crit && !miss && dame_inf.dameP > 0) {
                    dame_inf.data.add(new Option_Dame_Msg(1010, (int) dame_inf.dameP, 0));
                }
                if (HapThuHP > 0) {
                    dame_inf.data.add(new Option_Dame_Msg(1058, (int) HapThuHP, 0));
                }
                if (dame_mine > 0) {
                    dame_inf.data.add(new Option_Dame_Msg(1014, (int) dame_mine, 0));
                    dame_mine_all += dame_mine;
                }
                if (dame_inf.dameP > 0 || dame_inf.dameM > 0 || !dame_inf.data.isEmpty() || (p_target.get_eff(9) == null && p_target.get_eff(300) == null && (p_target.get_eff(7) == null || p_target.type_pk != -1))) {
                    list.add(dame_inf);
                }
            }
        }
        if (dame_mine_all > 0) {
            if (p.get_eff(9) != null || p.get_eff(300) != null) {
                dame_mine_all = 0;
            } else {
                p.hp -= dame_mine_all;
                if (p.hp <= 0 && p.get_eff(10) == null && p.ability.get_TuChoiTuThan() > 0) {
                    int time_eff = 5;
                    p.add_new_eff(9, 1, time_eff * 1_000);
                    p.add_new_eff(10, 1, 150_000);
                    p.getService().send_eff(21, 50);
                    p.hp = Math.max(1, p.ability.get_hp_max(true) / 10);
                } else if (p.hp <= 0) {
                    p.hp = 0;
                }
            }
            if (dame_mine_all > 0) {
                map.update_hp_mp_eff(p, null, 1, (int) -dame_mine_all);
            }
        }
        if (list.size() > 0) {
            short effId = (sk_temp != null && sk_temp.get_eff_skills() != null && sk_temp.get_eff_skills().length > 0) ? sk_temp.get_eff_skills()[0] : (short) 21;
            if (effId <= 0 && sk_temp != null && sk_temp.temp != null) {
                effId = sk_temp.temp.getTypeEffSkill();
            }
            if (effId <= 0) effId = 21;
            map.send_dame_msg(p, effId, list);
            if ((map.template != null && map.template.id >= 272 && map.template.id <= 275) || Zone.is_map_luyentap(map.template.id)) {
                p.updateExpFactionSkill(500);
            }
        }
        if (p.hp <= 0) {
            p.hp = 0;
            p.isdie = true;
            if (map.map_vp != null) {
                try {
                    Player p_in_pvp = null;
                    for (int i = 0; i < map.onlyPlayers.size(); i++) {
                        p_in_pvp = map.onlyPlayers.get(i);
                        if (!p_in_pvp.equals(p)) {
                            break;
                        }
                    }
                    if (p_in_pvp != null && !p_in_pvp.equals(p)) {
                        map.die_player(p, p_in_pvp);
                    }
                } catch (Exception e) {
                }
            } else {
                map.die_player(p, p);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            Player pTarget = list.get(i).targetP;
            if (pTarget.isdie) {
                map.die_player(pTarget, p);
            }
        }
    }

    public long[] Fire_Monster(Mob[] list_target, int idSkill, long dame) throws IOException {
        return Fire_Monster(list_target, this, idSkill, dame);
    }

    public long[] Fire_Monster(Mob[] list_target, Player p, int idSkill, long dame) throws IOException {
        long[] exp_up = new long[]{0, 0};
        if (p == null || list_target == null || p.map == null) {
            return exp_up;
        }
        Zone map = p.map;
        Skill_info sk_temp = p.get_skill_temp(idSkill);
        if (sk_temp == null || sk_temp.temp == null) {
            return exp_up;
        }
        int dame_plus_percent = 0;
        int dame_magic_plus_percent = p.ability.get_dame_ap();
        int crit_skill = p.ability.get_crit(true);
        int multi_dame_skill = p.ability.get_multi_dame_when_crit(true);
        boolean crit = crit_skill > ZUtil.random(1000);
        List<Dame_Msg> list = new ArrayList<>();
        HashMap<Integer, Integer> id_mob_die = new HashMap<>(); // quest relative to mob
        //
        final long damebefore = dame;
        long dame2;
        for (int i = 0; i < list_target.length; i++) {
            Mob mob_target = list_target[i];
            if (mob_target != null && mob_target.isdie && mob_target.time_refresh > 0 && mob_target.time_refresh <= System.currentTimeMillis()) {
                mob_target.isdie = false;
                mob_target.hp = mob_target.hp_max;
                mob_target.id_target = -1;
                mob_target.time_refresh = 0;
            }
            if (mob_target != null && !mob_target.isdie && !p.isdie) {
                dame2 = (damebefore > 0) ? damebefore : Math.max(100L, (long) p.level * 50L);
                dame2 = (dame2 * (1000L + dame_plus_percent)) / 1000L;
                // [FIX BUG 7] Bỏ crit = crit_skill > random(1000) — đã tính crit có trừ op69 bên ngoài loop (L7668)
                long dame_exp = dame2;
                if (dame2 > 1 && crit) {
                    dame2 = (dame2 * (1000L + multi_dame_skill)) / 1000L;
                }
                long baseDame0 = (p.skill_point != null && !p.skill_point.isEmpty() && p.skill_point.get(0) != null) ? p.skill_point.get(0).get_dame(p) : 100;
                if (baseDame0 <= 0) baseDame0 = 100;
                long skDame = (sk_temp != null) ? sk_temp.get_dame(p) : baseDame0;
                if (skDame <= 0) skDame = baseDame0;
                dame2 = (dame2 * skDame) / baseDame0;
                Dame_Msg dame_inf = new Dame_Msg();
                dame_inf.data = new ArrayList<>();
                dame_inf.targetM = mob_target;
                if (dame2 > 0 && idSkill != 0) {
                    dame_inf.dameM
                            = (skDame * (dame_magic_plus_percent))
                            / 1000;
                }
                dame2 = (dame2 * (1000L + p.ability.get_percent_final_dame())) / 1000L;
                if (idSkill == 2038 || idSkill == 2041) {
                    // skill bien hinh bao dom, chim ung
                    if (p.get_eff(6) != null) {
                        dame2 = (dame2 * 115) / 100;
                    }
                    // fashion bao dom + chim ung
                    for (int i12 = 0; i12 < p.fashion.size(); i12++) {
                        if ((p.fashion.get(i12).id == 33 || p.fashion.get(i12).id == 34)
                                && p.fashion.get(i12).is_use) {
                            dame2 = (dame2 * 115) / 100;
                            break;
                        }
                    }
                }
                boolean miss = (5 + mob_target.level / 10) > ZUtil.random(1000);
                if (miss) { // miss
                    dame2 = 0;
                } else if (dame2 <= 0) {
                    dame2 = Math.max(50L, (long) p.level * 20L);
                }
                if (dame2 > 0) {
                    dame2 -= (dame2 * ZUtil.random(10)) / 100;
                    
                    if (p.get_eff(305) != null) {
                        if (!crit && dame2 > 1) {
                            dame2 = (dame2 * (1000L + multi_dame_skill)) / 1000L;
                        }
                        crit = true;
                    }

                    // [KÍCH ẨN TẤN CÔNG ĐỘC QUYỀN KHI ĐÁNH QUÁI]
                    List<Integer> atkKichAnList = new ArrayList<>();
                    for (int kaId = 4; kaId <= 6; kaId++) {
                        int kaCount = p.ability.get_kich_an(kaId);
                        if (kaCount > 0 && p.get_eff(400 + kaId) == null) {
                            for (int k = 0; k < kaCount; k++) {
                                atkKichAnList.add(kaId);
                            }
                        }
                    }
                    if (!atkKichAnList.isEmpty()) {
                        int chosenAtkKa = atkKichAnList.get(ZUtil.random(atkKichAnList.size()));
                        int kaCount = p.ability.get_kich_an(chosenAtkKa);
                        if (chosenAtkKa == 4) { // Bộc phá (Tier S: 4.0% base)
                            int chance = 40 + Math.min(75, (kaCount - 1) * 15);
                            if (chance > ZUtil.random(1000)) {
                                p.add_new_eff(404, 1, 60_000);
                                long mult = 14L + Math.min(6L, (kaCount - 1) * 1L); // x1.4 -> x2.0
                                dame2 = (dame2 * mult) / 10L;
                                dame_inf.dameM = (dame_inf.dameM * mult) / 10L;
                            }
                        } else if (chosenAtkKa == 5) { // Tập trung cao độ (Tier A: 6.5% base)
                            if (p.get_eff(305) != null) {
                                crit = true;
                            } else {
                                int chance = 65 + Math.min(75, (kaCount - 1) * 15);
                                if (chance > ZUtil.random(1000)) {
                                    int time_eff = 3 + Math.min(3, (kaCount - 1));
                                    p.add_new_eff(305, 1, time_eff * 1_000);
                                    p.add_new_eff(405, 1, 60_000);
                                    if (!crit && dame2 > 1) {
                                        dame2 = (dame2 * (1000L + multi_dame_skill)) / 1000L;
                                    }
                                    crit = true;
                                }
                            }
                        } else if (chosenAtkKa == 6) { // Ma cà rồng (Tier S: 4.0% base)
                            int chance = 40 + Math.min(75, (kaCount - 1) * 15);
                            if (chance > ZUtil.random(1000)) {
                                p.add_new_eff(406, 1, 60_000);
                                int hpMax = p.ability.get_hp_max(true);
                                long healPct = 12L + Math.min(18L, (kaCount - 1) * 3L);
                                long capPct = 8L + Math.min(12L, (kaCount - 1) * 2L);
                                int hpHeal = (int) Math.min((hpMax * capPct) / 100L, (dame2 * healPct) / 100L);
                                p.hp = Math.min(hpMax, p.hp + hpHeal);
                                if (map != null) {
                                    map.getService().send_kich_an(p, p, 1, 6, 0, hpHeal);
                                }
                            }
                        }
                    }

                    // 7: Đánh là choáng
                    int ka7 = p.ability.get_kich_an(7);
                    if (ka7 > 0 && p.get_eff(407) == null) {
                        int per = Math.max(10, 16 - (ka7 - 1));
                        if (p.getDanhLaChoang() >= per) {
                            p.resetDanhLaChoang();
                            p.add_new_eff(407, 1, 60_000);
                            mob_target.isChoang = true;
                            mob_target.timeChoang = System.currentTimeMillis() + 2_000 + Math.min(1000, (ka7 - 1) * 200); // 2.0s -> 3.0s
                            if (map != null) {
                                map.getService().send_choang_mob(p, mob_target, 2_000 + Math.min(1000, (ka7 - 1) * 200));
                                map.getService().send_kich_an(p, p, 1, 7, 5, 50);
                            }
                        }
                    }

                    // 8: Thanh lọc
                    int ka8 = p.ability.get_kich_an(8);
                    if (ka8 > 0 && p.get_eff(408) == null) {
                        int per = Math.max(4, 8 - (ka8 - 1));
                        if (p.getThanhLoc() >= per) {
                            p.resetThanhLoc();
                            p.add_new_eff(408, 1, 30_000);
                            if (map != null) {
                                map.getService().send_kich_an(p, p, 1, 8, 0, 0);
                            }
                            p.send_kich_an();
                        }
                    }

                    // 11: Người bất tử (0.6% HP/hit mỗi món)
                    int ka11 = p.ability.get_kich_an(11);
                    if (ka11 > 0 && p.get_eff(202) == null) {
                        int hpMax = p.ability.get_hp_max(true);
                        int hpAbsorb = (int) ((hpMax * 6L * ka11) / 1000L); // 0.6% HP per item
                        if (hpAbsorb > 0) {
                            p.hp = Math.min(hpMax, p.hp + hpAbsorb);
                            if (map != null) {
                                map.getService().send_kich_an(p, p, hpAbsorb / 10, 11, 0, 0);
                            }
                        }
                    }
                }
                if (mob_target.mtemplate.mob_id == 132
                        || mob_target.mtemplate.mob_id == 171) {
                    dame2 = 1;
                    dame_inf.dameM = 0;
                }

                if (mob_target.mtemplate.mob_id == 133) {
                    dame2 /= 10;
                    dame_inf.dameM /= 10;

                    // Bắt buộc phải đánh hết Trụ Phụ (mob 132) trước mới được đánh Trụ Chính
                    int numAliveSubTurrets = ChiemDao.getAliveSubTurretCount(map);
                    if (numAliveSubTurrets > 0) {
                        dame2 = 0;
                        dame_inf.dameM = 0;
                    }
                    //
                    if (p.clan != null && ChiemDao.getClanTop(map.template.id) != null
                            && !p.clan.equals(ChiemDao.getClanTop(map.template.id))) {
                        dame2 /= 2;
                        dame_inf.dameM /= 2;
                    }
                    EffTemplate eff = p.get_eff(25);
                    if (eff != null) {
                        dame2 = 0;
                        dame_inf.dameM = 0;
                    }
                }
                if (mob_target.mtemplate.mob_id == 133 || mob_target.mtemplate.mob_id == 132) {
                    clan.Clan topClan = ChiemDao.getClanTop(map.template.id);
                    if (p.clan == null || (topClan != null && (p.clan.id == topClan.id || (p.clan.name != null && p.clan.name.equalsIgnoreCase(topClan.name))))) {
                        dame2 = 0;
                        dame_inf.dameM = 0;
                    }
                }
                if (mob_target.boss_inf != null && mob_target.isSieuTrum) {
                    if (mob_target.mtemplate.mob_id == 163) {
                        dame2 = dame2 * 25 / 100;
                        dame_inf.dameM = dame_inf.dameM * 25 / 100;
                    }

                    if (mob_target.mtemplate.mob_id == 135 || mob_target.mtemplate.mob_id == 140) {
                        dame2 = dame2 * 125 / 100;
                        dame_inf.dameM = dame_inf.dameM * 125 / 100;
                    }
                    
                    zabstracts.AbsBoss sBoss = mob_target.getBoss();
                    if (!boss.SuperBossManager.checkLevelRequirement(p, sBoss)) {
                        dame2 = 0;
                        dame_inf.dameM = 0;
                        boss.SuperBossManager.kickPlayerOutOfSuperBossZone(p, sBoss);
                        return exp_up;
                    }
                }
                if (map.template.id == 179 || (boss.BossTheGioi.mob != null && boss.BossTheGioi.mob.equals(mob_target)) || map.map_DaoKhoBau != null || (boss.BossPica.mob != null && boss.BossPica.mob.equals(mob_target))) {
                    dame2 = 1;
                    dame_inf.dameM = 0;
                }
                if (event.eboss.LucciGioTo.mob != null && event.eboss.LucciGioTo.mob.equals(mob_target)) {
                } else {
                    if (dame_inf.dameM > 50000) {
                        dame_inf.dameM = 50000;
                    }
                }

                boolean isDungeonBoss = mob_target.is_boss 
                        || mob_target.boss_inf != null 
                        || mob_target.typeSpecMonSter == 2
                        || (map != null && (map.mapSanTrum != null || map.map_LienTang != null || map.map_SieuLienTang != null || map.map_Mr3 != null || map.map_ThuThachVeThan != null || map.map_Hang != null || (map.vuonCam != null && mob_target.mtemplate != null && mob_target.mtemplate.mob_id == 36)));

                if (isDungeonBoss) {
                    int percent = p.ability.get_dame_skip_reduce();
                    // Chuẩn hóa sát thương phó bản: Người chơi giữ 90% dame chuẩn (+ xuyên giáp), không bị giảm 75% như trước
                    long bossDmgRatio = Math.min(1000L, 900L + percent);
                    dame2 = (dame2 * bossDmgRatio) / 1_000L;
                    dame_inf.dameM = (dame_inf.dameM * bossDmgRatio) / 1_000L;
                }

                boolean isBossTarget = isDungeonBoss || mob_target.is_boss || mob_target.isSieuTrum || mob_target.boss_inf != null;
                if (isBossTarget) {
                    // [TÍCH HỢP OPTION 33: Sát thương lên Boss]
                    int bossDmgBonus = p.ability.get_boss_dame();
                    if (bossDmgBonus > 0) {
                        dame2 = (dame2 * (1000L + (long) bossDmgBonus)) / 1000L;
                        dame_inf.dameM = (dame_inf.dameM * (1000L + (long) bossDmgBonus)) / 1000L;
                    }
                }

                if ("BiNgoMa".equals(mob_target.keyflag) || "QuaiVatTuyet".equals(mob_target.keyflag) || "Zombie".equals(mob_target.keyflag) || "DoiTruongHuyenThoai".equals(mob_target.keyflag)) {
                    dame2 = 1;
                    dame_inf.dameM = 0;
                }
                if ("BigMom".equals(mob_target.keyflag) || (mob_target.mtemplate != null && mob_target.mtemplate.mob_id == 172) || mob_target.iMob instanceof event.eboss.BigMom || "DuaBeThieuNhi".equals(mob_target.keyflag) || mob_target.iMob instanceof event.eboss.DuaBeThieuNhi) {
                    dame2 = 0;
                    dame_inf.dameM = 0;
                }
                long dame_to_target = dame2 + dame_inf.dameM;
                if (!miss && dame_to_target <= 0 && !"BigMom".equals(mob_target.keyflag) && (mob_target.mtemplate == null || mob_target.mtemplate.mob_id != 172)) {
                    dame_to_target = Math.max(50L, (long) p.level * 20L);
                    dame2 = dame_to_target;
                }

                // [TÍCH HỢP OPTION 57: Sát thương chuẩn khi đánh quái & Boss]
                int trueDmgParam = p.ability.get_true_dame();
                if (trueDmgParam > 0 && dame2 > 0) {
                    long addedTrue = (dame2 * (long) trueDmgParam) / 1000L;
                    if (isBossTarget) {
                        addedTrue = Math.min((dame2 * 25L) / 100L, addedTrue); // Giới hạn an toàn trên Boss
                    }
                    dame_to_target += addedTrue;
                }
                zabstracts.AbsBoss bossTarget = mob_target.getBoss();
                if (bossTarget != null) {
                    dame_to_target = bossTarget.modifyIncomingDamage(p, dame_to_target);
                }

                // [GIỚI HẠN SÁT THƯƠNG SIÊU TRÙM TỐI ĐA 10% HP BOSS/HIT]
                if (mob_target.isSieuTrum || (bossTarget != null && bossTarget.isSieuTrum())) {
                    if (mob_target.hp_max > 0) {
                        long max10Percent = (long) mob_target.hp_max * 10L / 100L;
                        if (max10Percent > 0 && dame_to_target > max10Percent) {
                            dame_to_target = max10Percent;
                        }
                    }
                }

                // [CHỐNG ONE-HIT QUÁI & BOSS PHÓ BẢN]
                boolean inDungeon = (map != null && (map.map_dungeon != null || Zone.is_map_dungeon(map.template.id) || map.mapSanTrum != null || map.map_LienTang != null || map.map_SieuLienTang != null || map.map_Mr3 != null || map.map_ThuThachVeThan != null || map.map_Hang != null || map.vuonCam != null || map.map_DaoKhoBau != null || (map.template != null && map.template.id >= 167 && map.template.id <= 177)));
                if (inDungeon && mob_target.hp_max > 0) {
                    if (isDungeonBoss) {
                        // Boss phó bản: Sát thương nhận tối đa 1 hit không vượt quá 25% Max HP (tối thiểu 4 hit)
                        long maxDmgPerHit = (mob_target.hp_max * 25L) / 100L;
                        if (maxDmgPerHit > 0 && dame_to_target > maxDmgPerHit) {
                            dame_to_target = maxDmgPerHit;
                        }
                    } else {
                        // Quái thường phó bản: Sát thương nhận tối đa 1 hit không vượt quá 60% Max HP
                        long maxDmgPerHit = (mob_target.hp_max * 60L) / 100L;
                        if (maxDmgPerHit > 0 && dame_to_target > maxDmgPerHit) {
                            dame_to_target = maxDmgPerHit;
                        }
                    }
                }

                // add dame vào top dame st
                if (mob_target.boss_inf != null && dame_to_target > 0) {
                    zabstracts.AbsBoss b = boss.BossManager.gI().getBossById(mob_target.boss_inf.id);
                    if (b != null) {
                        b.onDamage(p, dame_to_target);
                    }
                } else if (bossTarget != null && dame_to_target > 0) {
                    bossTarget.onDamage(p, dame_to_target);
                }
                if (mob_target.hp == mob_target.hp_max && dame_to_target >= mob_target.hp) {
                    long maxHOne = (mob_target.mtemplate != null && mob_target.mtemplate.hOne > 0)
                            ? (long) mob_target.mtemplate.hOne
                            : ((mob_target.hp_max * 40L) / 100L);
                    dame_to_target = Math.min(maxHOne, Math.max(1L, (long) mob_target.hp - 1));
                }
                int old_mob_hp = mob_target.hp;
                if (map.clan_resource != null) {
                    map.clan_resource.dame += dame_to_target;
                } else {
                    mob_target.hp -= dame_to_target;
                }
                if (bossTarget != null) {
                    bossTarget.checkHpMilestoneRewards(map, p, old_mob_hp, mob_target.hp, dame_to_target);
                }
                p.item.updateInventory(false);
                p.updateMoney();

                // [HÚT HP KHI ĐÁNH QUÁI op59 + op21]
                if (dame_to_target >= 1 && p.hp > 0 && !miss) {
                    int lifesteal_pct = p.ability.get_HapThu_Hp();
                    long totalHeal = p.ability.get_hp_atk_absorb(true);
                    if (lifesteal_pct > 0) {
                        totalHeal += (dame_to_target * (long) Math.min(150, lifesteal_pct)) / 1000L;
                    }
                    if (totalHeal > 0) {
                        long maxHealCap = (p.ability.get_hp_max(true) * 8L) / 100L; // Cap 8% Max HP
                        long actualHeal = Math.min(totalHeal, maxHealCap);
                        p.hp = Math.min(p.ability.get_hp_max(true), (int)(p.hp + actualHeal));
                        p.getService().use_potion(0, (int) actualHeal);
                    }
                }

                // [TÍCH HỢP OPTION 22: Hút MP khi đánh quái]
                if (dame_to_target >= 1 && p.mp < p.ability.get_mp_max(true) && !miss) {
                    int mpSteal = p.ability.get_param_by_id(22);
                    if (mpSteal > 0) {
                        long mpAbsorb = (dame_to_target * (long) Math.min(100, mpSteal)) / 1000L;
                        if (mpAbsorb > 0) {
                            int mpMax = p.ability.get_mp_max(true);
                            long maxMpCap = (mpMax * 5L) / 100L; // Cap 5% Max MP
                            int actualMp = (int) Math.min(mpAbsorb, maxMpCap);
                            p.mp = Math.min(mpMax, p.mp + actualMp);
                            p.getService().use_potion(1, actualMp);
                        }
                    }
                }
                
                if (!miss && dame_to_target > 0) {
                    dame_inf.dameP = Math.max(1, dame_to_target - dame_inf.dameM);
                } else {
                    dame_inf.dameP = dame2;
                }
                mob_target.id_target = p.index_map;
                if (mob_target.hp <= 0 && !mob_target.isdie) {
                    mob_target.hp = 0;
                    mob_target.isdie = true;
                    Player pOwner = (p != null) ? p.getOwnerPlayer() : p;
                    mob_target.mobDie(pOwner);
                    // [TÍCH HỢP OPTION 35: Hồi phục khi tiêu diệt mục tiêu]
                    if (pOwner != null && pOwner.hp > 0 && !pOwner.isdie) {
                        int killRegen = pOwner.ability.get_param_by_id(35);
                        if (killRegen > 0) {
                            int hpMax = pOwner.ability.get_hp_max(true);
                            int healAmount = (int) ((hpMax * (long) Math.min(200, killRegen)) / 1000L);
                            if (healAmount > 0) {
                                pOwner.hp = Math.min(hpMax, pOwner.hp + healAmount);
                                pOwner.getService().use_potion(0, healAmount);
                            }
                        }
                    }
                    if (mob_target.mtemplate != null && (mob_target.mtemplate.mob_id == 132 || mob_target.mtemplate.mob_id == 133)) {
                        mob_target.time_refresh = 0; // Trụ Chiếm Đảo tuyệt đối không tự động hồi sinh theo timer
                    } else {
                        mob_target.time_refresh = System.currentTimeMillis() + Mob.TIME_RESPAWN * 1000L;
                    }
                    exp_up[1] += Math.max(10, mob_target.level * 2);

                    // dungeon
                    if (map.baoVePhaoDai != null) {
                        map.baoVePhaoDai.UpdateDie(pOwner, mob_target);
                    }
                    if (map.map_little_garden != null && !map.map_little_garden.is_finish
                            && (pOwner.type_pk == 4 || pOwner.type_pk == 5)) {
                        LeaveItemMap.leave_item4_little_garden(map, mob_target, pOwner);
                    } else if (map.map_dungeon != null) {
                    } 
                    else {
                        // leave item
                        if (Math.abs(pOwner.level - mob_target.level) <= 10) {
                            if (15 > ZUtil.random(120)) {
                                LeaveItemMap.leave_item4(map, mob_target, pOwner);
                            } else if (5 > ZUtil.random(120)) {
                                // LeaveItemMap.leave_item3(map, mob_target, pOwner);
                            } else if (15 > ZUtil.random(120)) {
                                LeaveItemMap.leave_item7(map, mob_target, pOwner);
                            }
                        }
                        LeaveItemMap.leave_item_quest(map, mob_target, pOwner);
                    }
                    if (map.map_SieuLienTang != null
                            && (mob_target.mtemplate.mob_id == 4
                            || mob_target.mtemplate.mob_id == 10
                            || mob_target.mtemplate.mob_id == 16
                            || mob_target.mtemplate.mob_id == 23
                            || mob_target.mtemplate.mob_id == 29
                            || mob_target.mtemplate.mob_id == 36
                            || mob_target.mtemplate.mob_id == 43
                            || mob_target.mtemplate.mob_id == 78
                            || mob_target.mtemplate.mob_id == 70
                            || mob_target.mtemplate.mob_id == 79
                            || mob_target.mtemplate.mob_id == 68
                            || mob_target.mtemplate.mob_id == 92
                            || mob_target.mtemplate.mob_id == 87
                            || mob_target.mtemplate.mob_id == 88)) {
                        List<GiftBox> listGift = new ArrayList<>();
                        GiftBox.addGift(listGift, 4, ((p.level < 10 ? 10 : p.level) / 10 + 121), 1);
                        GiftBox.addGift(listGift, 4, 0, 100_000);
                        if (!listGift.isEmpty()) {
                            core.RewardService.sendGiftOrMail(p, 0, "Tiêu Diệt " + mob_target.mtemplate.name, "Phần thưởng", listGift, true);
                        }
                    }
                    if (map.map_Mr3 != null
                            && (mob_target.mtemplate.mob_id == 69
                            || mob_target.mtemplate.mob_id == 70)) {
                        List<GiftBox> listGift = new ArrayList<>();
                        GiftBox.addGift(listGift, 4, ((p.level < 10 ? 10 : p.level) / 10 + 121), 1);
                        GiftBox.addGift(listGift, 4, 0, 100_000);
                        if (!listGift.isEmpty()) {
                            core.RewardService.sendGiftOrMail(p, 0, "Tiêu Diệt " + mob_target.mtemplate.name, "Phần thưởng", listGift, true);
                        }
                    }
                    if (event.EventManager.isActive(3) && p.clan != null && p.clan.mob1 != null) {
                        for (int j = 0; j < p.clan.mob1.length; j++) {
                            if (p.clan.mob1[j] != null && p.clan.mob1[j].equals(mob_target)) {
                                map.remove_obj(mob_target.index, 1);
                                p.clan.mob1[j] = null;
                                List<GiftBox> listGift = new ArrayList<>();
                                short[] ids = new short[]{330, 328, 412, 411, 329, 410};
                                GiftBox.addGift(listGift, 4, ids[ZUtil.random(ids.length)], 1);
                                if (!listGift.isEmpty()) {
                                    for (int j2 = 0; j2 < map.players.size(); j2++) {
                                        Player p00 = map.players.get(j2);
                                        if (p00.clan != null && p00.clan.equals(p.clan)) {
                                            core.RewardService.sendGiftOrMail(p00, 0, "Tiêu Diệt " + mob_target.mtemplate.name, "Phần thưởng", listGift, true);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    // update quest relative to
                    if (!id_mob_die.containsKey((int) mob_target.mtemplate.mob_id)) {
                        id_mob_die.put((int) mob_target.mtemplate.mob_id, 1);
                    } else {
                        int oldvalue = id_mob_die.get((int) mob_target.mtemplate.mob_id);
                        id_mob_die.replace((int) mob_target.mtemplate.mob_id, oldvalue,
                                oldvalue + 1);
                    }

                    if (map.map_little_garden != null && !map.map_little_garden.is_finish
                            && (p.type_pk == 4 || p.type_pk == 5)) {
                        LeaveItemMap.leave_item4_little_garden(map, mob_target, p);
                    }
                    if (boss.BossTheGioi.mob != null && boss.BossTheGioi.mob.equals(mob_target)) {
                        boss.BossTheGioi.gI().onDeath(p);
                    }
                    if (event.eboss.LucciGioTo.mob != null && event.eboss.LucciGioTo.mob.equals(mob_target)) {
                        event.eboss.LucciGioTo.onDeath(p, mob_target);
                    }
                    if (event.eboss.KaidoRong.mob != null && event.eboss.KaidoRong.mob.equals(mob_target)) {
                        event.eboss.KaidoRong.onDeath(p, mob_target);
                    }
                    if (event.eboss.KingHoaTai.mob != null && event.eboss.KingHoaTai.mob.equals(mob_target)) {
                        event.eboss.KingHoaTai.onDeath(p, mob_target);
                    }
                    if (map.mapSanTrum != null && map.mapSanTrum.mob != null && map.mapSanTrum.mob.equals(mob_target)) {
                        map.remove_obj(mob_target.index, 1);
                        if (map.mobs != null) {
                            map.mobs.remove(mob_target.index);
                        }
                        for (int j = 0; j < map.players.size(); j++) {
                            Player get = map.players.get(j);
                            if (get == null || get.isBot || get.conn == null) continue;
                            List<GiftBox> listGift = new ArrayList<>();
                            int lv_ = mob_target.level;
                            GiftBox.addGift(listGift, 4, ((lv_ / 10) + 18), 1);
                            GiftBox.addGift(listGift, 4, 0, (ZUtil.random(10_000, 50_000)));
                            if (30 > ZUtil.random(120)) {
                                GiftBox.addGift(listGift, 4, ((lv_ / 10) + 111), 1);
                            }
                            // Append quà sự kiện (nếu có sự kiện đang chạy)
                            event.EventManager.dispatchMapDrop(map, listGift, lv_ / 10);
                            core.RewardService.sendGiftOrMail(get, 0, "Phần thưởng Săn Quái", "Phần thưởng", listGift, true);
                        }
                        if ((map.mapSanTrum.time - 10_000) > System.currentTimeMillis()) {
                            map.mapSanTrum.time = System.currentTimeMillis() + 10_000;
                            for (int j = 0; j < map.players.size(); j++) {
                                Player get = map.players.get(j);
                                if (get != null && get.getService() != null) {
                                    get.getService().send_time_cool_down(get.map.mapSanTrum.time, "Thời gian", 2);
                                }
                            }
                        }
                        map.mapSanTrum.mob = null;
                    }
                    // boss
                    if (mob_target.boss_inf != null && !(mob_target.boss_inf instanceof zabstracts.AbsWorldBoss)) {
                        Player pFind = null;
                        for (int j = 0; j < map.players.size(); j++) {
                            Player p00 = map.players.get(j);
                            if (p00.detu != null && p00.detu.equals(p)) {
                                pFind = p00;
                                break;
                            }
                        }
                        if (pFind == null) {
                            pFind = p;
                        }

                        zabstracts.AbsBoss b = mob_target.getBoss();
                        if (b == null) {
                            b = boss.BossManager.gI().getBossById(mob_target.boss_inf.id);
                        }

                        // Remove dead boss object from map immediately for all clients in zone
                        map.remove_obj(mob_target.index, 1);
                        if (b != null && b.index != mob_target.index) {
                            map.remove_obj(b.index, 1);
                        }
                        map.mobs.remove(mob_target.index);

                        if (b != null) {
                            b.isdie = true;
                            b.timeDeath = System.currentTimeMillis();
                            b.onDeath(pFind);
                        }
                    }
                    if (Zone.is_map_boss(map.template.id)) {
                        map.remove_obj(mob_target.index, 1);
                        if (map.mobs != null) {
                            map.mobs.remove(mob_target.index);
                        }
                        mob_target.isdie = true;
                        mob_target.hp = 0;
                        mob_target.time_refresh = Long.MAX_VALUE;
                        if (p != null && !p.isBot && p.conn != null) {
                            List<GiftBox> listGift = core.RewardService.getGiftMapBossByLevel(p);
                            core.RewardService.sendGiftOrMail(p, 0, "Phần thưởng Săn Quái", "Phần thưởng", listGift, true);
                        }
                        if (p.map_boss_info != null) {
                            p.map_boss_info.mob.remove(mob_target);
                            if (p.map_boss_info.mob.isEmpty()) {
                                MapBossInfo.remove(p.map_boss_info);
                            }
                        }
                        p.updateArchiDaily(3);
                    }
                    
                    if (boss.BossPica.mob != null && boss.BossPica.mob.equals(mob_target)) {
                        boss.BossPica.gI().onDeath(p);
                    }
                    if (event.EventManager.isActive(9) && map.map_DaoKhoBau != null && map.map_DaoKhoBau.mobs != null
                            && !map.map_DaoKhoBau.mobs.isEmpty() && map.map_DaoKhoBau.mobs.contains(mob_target)) {
                        List<GiftBox> list_gift = new ArrayList<>();
                        short[] ids = new short[]{159, 133, -10, 9, 4, 1, 362, 363, 364};
                        byte[] type = new byte[]{4, 4, 4, 7, 7, 7, 4, 4, 4};
                        if (mob_target.hp_max == 150) {
                            ids = new short[]{159, 133, -10, 9, 4, 1, 362, 363};
                            type = new byte[]{4, 4, 4, 7, 7, 7, 4, 4};
                        }
                        for (int j = 0; j < type.length; j++) {
                            if (ids[j] == -10) {
                                GiftBox.addGift(list_gift, type[j], (p.level < 10 ? 10 : p.level) / 10 + 111, 1);
                            } else {
                                GiftBox.addGift(list_gift, type[j], ids[j], 1);
                            }
                        }
                        if (list_gift.size() > 0 && p != null && !p.isBot && p.conn != null) {
                            core.RewardService.sendGiftOrMail(p, 1, "Đảo Kho Báu", "Tiêu diệt quái", list_gift, true);
                        }
                    }
                    if (map.template.id == 179 || map.template.id == 180 || map.template.id == 184) {
                        ThuLinhBienKhoi.onMobDie(p, mob_target, map);
                    }
                    if (mob_target.mtemplate.mob_id == 133) {
                        ChiemDao.onMainTurretDestroyed(map, p, mob_target);
                    }
                }
                // update exp
                long exp_up_add = 1;
                Player pOwnerExp = (p != null) ? p.getOwnerPlayer() : p;
                int checkLevel = p.level;
                if (Math.abs(checkLevel - mob_target.level) >= 10 && pOwnerExp != null && pOwnerExp != p) {
                    if (Math.abs(pOwnerExp.level - mob_target.level) < 10) {
                        checkLevel = pOwnerExp.level;
                    }
                }
                long a = checkLevel / 20;
                a = a == 0 ? 1 : a;
                long b = dame * a;
                long c = (mob_target.level - checkLevel) * a;
                exp_up_add = (b / 2) + (b * c / 100);

                // Tăng độ dễ cho level 1 cày nhanh chuẩn xịn
                if (checkLevel == 1) {
                    if (exp_up_add < 30) {
                        exp_up_add = 30; // Đảm bảo cấp 1 mỗi hit nhận tối thiểu 30 EXP
                    }
                    exp_up_add *= 2; // Tăng gấp đôi tốc độ cày cho cấp 1
                }

                if (Math.abs(checkLevel - mob_target.level) >= 10) {
                    exp_up_add = 0;
                }
                if (mob_target.mtemplate.mob_id == 4 || mob_target.mtemplate.mob_id == 10 || mob_target.mtemplate.mob_id == 16
                        || mob_target.mtemplate.mob_id == 23
                        || mob_target.mtemplate.mob_id == 29 || mob_target.mtemplate.mob_id == 36
                        || mob_target.mtemplate.mob_id == 43 || mob_target.mtemplate.mob_id == 68
                        || mob_target.mtemplate.mob_id == 78 || mob_target.mtemplate.mob_id == 92
                        || mob_target.mtemplate.mob_id == 112 || mob_target.mtemplate.mob_id == 163) {
                    exp_up_add = 0;
                }

                exp_up[0] += exp_up_add;
                if (Zone.is_map_luyentap(map.template.id)) {
                    exp_up[0] += ZUtil.random(5, 10);
                }
                if (crit && !miss && dame_inf.dameP > 0) {
                    dame_inf.data.add(new Option_Dame_Msg(1010, (int) dame_inf.dameP, 0));
                }
                // [KÍCH HOẠT HAKI BÁ VƯƠNG (ID 4026)]
                if (dame_inf.dameP > 0) {
                    p.triggerHakiBaVuong(null, mob_target);
                }
                if (dame_inf.dameP > 0 && sk_temp != null && sk_temp.temp != null && sk_temp.temp.idEffSpec > 0
                        && sk_temp.temp.idEffSpec < 17) {
                    int basePer = sk_temp.temp.perEffSpec;
                    if (basePer > 0 && basePer <= 100) {
                        basePer *= 10;
                    }
                    // [TÍCH HỢP OPTION 75: Tăng % choáng lên quái]
                    int stunBoost = p.ability.get_stun_rate();
                    if (stunBoost > 0) {
                        basePer += stunBoost;
                    }
                    if (basePer > 0 && basePer > ZUtil.random(1000)) {
                        int time = sk_temp.temp.timeEffSpec;
                        if (time > 0) {
                            dame_inf.data.add(new Option_Dame_Msg(sk_temp.temp.idEffSpec, 1, time));
                            long lockDuration = time * 100L;
                            mob_target.isChoang = true;
                            mob_target.timeChoang = Math.max(mob_target.timeChoang, System.currentTimeMillis() + lockDuration);
                        }
                    }
                }
                // [TÍCH HỢP OPTION 80: Tỷ lệ gây Điện giật lên quái]
                if (dame_inf.dameP > 0 && !mob_target.isChoang) {
                    int shockRate = p.ability.get_shock_rate();
                    if (shockRate > 0 && shockRate > ZUtil.random(1000)) {
                        int time = 20; // 2.0s
                        dame_inf.data.add(new Option_Dame_Msg(7, 1, time));
                        long lockDuration = time * 100L;
                        mob_target.isChoang = true;
                        mob_target.timeChoang = Math.max(mob_target.timeChoang, System.currentTimeMillis() + lockDuration);
                    }
                }
                list.add(dame_inf);
                if (Zone.is_map_luyentap(map.template.id)) {
                    p.updateExpSkill(5001, 3000);
                    p.updateExpSkill(5002, 3000);
                    p.updateExpSkill(5003, 3000);
                }
            }
        }
        if (list.size() > 0) {
            short effId = (sk_temp != null && sk_temp.get_eff_skills() != null && sk_temp.get_eff_skills().length > 0) ? sk_temp.get_eff_skills()[0] : (short) 21;
            if (effId <= 0 && sk_temp != null && sk_temp.temp != null) {
                effId = sk_temp.temp.getTypeEffSkill();
            }
            if (effId <= 0) effId = 21;
            map.send_dame_msg(p, effId, list);
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).targetM.isdie) {
                map.die_mob(list.get(i).targetM);
                p.updateArchiDaily(1);
                p.updateArchiDaily(3);
                p.updateArchiDaily(7);
                p.updateArchiDaily(8);
            }
        }
        EffTemplate eff = p.get_eff(2);
        if (eff != null) {
            exp_up[0] *= 2;
        }

        eff = p.get_eff(17);
        if (eff != null) {
            exp_up[0] *= 2;
        }
        // update quest
        if (id_mob_die.size() > 0) {
            for (java.util.Map.Entry<Integer, Integer> en : id_mob_die.entrySet()) {
                int id_mob = en.getKey();
                p.update_num_item_quest(1, id_mob, en.getValue());
            }
        }
        return exp_up;
    }

//    public void updateEverySecond() {
//        
//    }

    public void addmsg(network.Message m) throws java.io.IOException {
        if (this.conn != null) {
            this.conn.addmsg(m);
        }
    }

    public void setUsername(String user) {
        if (this.conn != null) {
            this.conn.user = user;
        }
    }

    public byte getConnStatus() {
        return this.conn != null ? this.conn.status : 0;
    }

    public int getTongnap() {
        if (this.tongnap > 0) return this.tongnap;
        return this.conn != null ? this.conn.tongnap : 0;
    }

    public void setTongnap(int tn) {
        this.tongnap = tn;
        if (this.conn != null) {
            this.conn.tongnap = tn;
        }
    }

    public int getVip() {
        return core.VipManager.syncAndGetPlayerVip(this);
    }

    public String getVipTitle() {
        return core.VipManager.getVipTitle(getVip());
    }

    public void setVip(int v) {
        this.vip = v;
        if (this.conn != null) {
            this.conn.vip = v;
        }
    }

    public int getNaphangngay() {
        return this.conn != null ? this.conn.naphangngay : 0;
    }

    public int getTongnap2() {
        if (this.tongnap2 > 0) return this.tongnap2;
        return this.conn != null ? this.conn.tongnap2 : 0;
    }

    public int getConnVersionInt() {
        return (this.conn != null && this.conn.versionInt > 0) ? this.conn.versionInt : 129;
    }

    public String getConnVersion() {
        return this.conn != null ? this.conn.version : "";
    }

    public byte getZoomlv() {
        return this.conn != null ? this.conn.zoomlv : 1;
    }

    public void disconnectSC() {
        if (this.conn != null) {
            this.conn.disconnectSC();
        }
    }

    public String getPass() {
        return this.conn != null ? this.conn.pass : "";
    }

    public void setPass(String pass) {
        if (this.conn != null) {
            this.conn.pass = pass;
        }
    }

    public int getCoin() {
        return this.conn != null ? this.conn.coin : 0;
    }

    public void refreshRecharge() {
        if (this.conn != null) {
            int accId = this.conn.idUser;
            try (java.sql.Connection connection = database.DbManager.gI().getConnect();
                 java.sql.PreparedStatement ps = connection.prepareStatement("SELECT `tongnap`, `tongnap2`, `naphangngay`, `vip` FROM `account` WHERE `id` = ?")) {
                ps.setInt(1, accId);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        this.conn.tongnap = rs.getInt("tongnap");
                        try { this.conn.tongnap2 = rs.getInt("tongnap2"); } catch (Exception ignored) { this.conn.tongnap2 = 0; }
                        this.conn.naphangngay = rs.getInt("naphangngay");
                        int dbVip = rs.getInt("vip");

                        // Theo dõi và đối soát tiến độ nạp trực tiếp từ historys (bảo đảm không bao giờ mất số liệu)
                        historys.RechargeHistory.RechargeSummary summary = historys.RechargeHistory.getRechargeSummary(accId);
                        if (summary != null) {
                            boolean needDbUpdate = false;
                            if (summary.today > this.conn.naphangngay) {
                                this.conn.naphangngay = (int) Math.min(Integer.MAX_VALUE, summary.today);
                                needDbUpdate = true;
                            }
                            if (summary.thisWeek > this.conn.tongnap2) {
                                this.conn.tongnap2 = (int) Math.min(Integer.MAX_VALUE, summary.thisWeek);
                                needDbUpdate = true;
                            }
                            if (summary.total > this.conn.tongnap) {
                                this.conn.tongnap = (int) Math.min(Integer.MAX_VALUE, summary.total);
                                needDbUpdate = true;
                            }
                            if (needDbUpdate) {
                                try (java.sql.PreparedStatement psUp = connection.prepareStatement(
                                        "UPDATE `account` SET `tongnap` = ?, `tongnap2` = ?, `naphangngay` = ? WHERE `id` = ?")) {
                                    psUp.setInt(1, this.conn.tongnap);
                                    psUp.setInt(2, this.conn.tongnap2);
                                    psUp.setInt(3, this.conn.naphangngay);
                                    psUp.setInt(4, accId);
                                    psUp.executeUpdate();
                                } catch (Exception ignored) {}
                            }
                        }

                        this.tongnap = this.conn.tongnap;
                        this.tongnap2 = this.conn.tongnap2;
                        int calcVip = core.VipManager.getVipByRecharge(this.tongnap);
                        int finalVip = Math.max(dbVip, calcVip);
                        this.conn.vip = finalVip;
                        this.vip = finalVip;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addRecharge(long amount) {
        if (amount <= 0) return;

        long oldTongnap = this.tongnap;
        if (this.conn != null) {
            this.conn.tongnap += (int) amount;
            this.conn.tongnap2 += (int) amount;
            this.conn.naphangngay += (int) amount;
        }
        this.tongnap += (int) amount;
        this.tongnap2 += (int) amount;

        int currentVip = this.vip;
        int calculatedVip = core.VipManager.getVipByRecharge(this.getTongnap());
        int finalVip = Math.max(currentVip, calculatedVip);
        finalVip = Math.max(0, Math.min(20, finalVip));
        this.setVip(finalVip);

        int accId = (this.conn != null) ? this.conn.idUser : 0;
        String user = (this.conn != null) ? this.conn.user : null;
        if (accId > 0 || user != null) {
            try (java.sql.Connection connection = database.DbManager.gI().getConnect()) {
                boolean hasTongnap2 = false;
                try (java.sql.ResultSet rs = connection.getMetaData().getColumns(null, null, "account", "tongnap2")) {
                    if (rs.next()) {
                        hasTongnap2 = true;
                    }
                } catch (Exception ignored) {}

                String whereClause = (accId > 0) ? "WHERE `id` = ?" : "WHERE BINARY `username` = ?";
                String sql = hasTongnap2
                    ? "UPDATE `account` SET `tongnap` = `tongnap` + ?, `tongnap2` = `tongnap2` + ?, `naphangngay` = `naphangngay` + ?, `vip` = ? " + whereClause
                    : "UPDATE `account` SET `tongnap` = `tongnap` + ?, `naphangngay` = `naphangngay` + ?, `vip` = ? " + whereClause;

                try (java.sql.PreparedStatement ps = connection.prepareStatement(sql)) {
                    int pIdx = 1;
                    ps.setLong(pIdx++, amount);
                    if (hasTongnap2) {
                        ps.setLong(pIdx++, amount);
                    }
                    ps.setLong(pIdx++, amount);
                    ps.setInt(pIdx++, finalVip);
                    if (accId > 0) {
                        ps.setInt(pIdx++, accId);
                    } else {
                        ps.setString(pIdx++, user);
                    }
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Ghi nhận lịch sử nạp vào bảng historys (type = NAP_THE) để đối soát và theo dõi
        try {
            historys.RechargeHistory.logRecharge(this, "NAP_THE", amount, oldTongnap, this.tongnap, "Nạp thẻ / Đổi Extol");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getIdEff() {
        if (this.id_danh_hieu_su_dung > 0) {
            int[] owned = getDanhHieuById(this.id_danh_hieu_su_dung);
            if (owned != null && owned.length >= 3 && owned[2] > 0 && (long)owned[2] * 1000L < System.currentTimeMillis()) {
                offAllDh();
                return -1;
            }
            return this.id_danh_hieu_su_dung;
        }
        if (this.id_danh_hieu_da_so_huu != null) {
            for (int i = 0; i < this.id_danh_hieu_da_so_huu.size(); i++) {
                int[] dh = this.id_danh_hieu_da_so_huu.get(i);
                if (dh != null && dh.length >= 2 && dh[1] == 1) {
                    if (dh.length >= 3 && dh[2] > 0 && (long)dh[2] * 1000L < System.currentTimeMillis()) {
                        dh[1] = 0;
                        continue;
                    }
                    return dh[0];
                }
            }
        }
        return -1;
    }

    public int getIdDanhHieu() {
        return getIdEff();
    }

    public boolean check_id_danhhieu(int id) {
        if (this.id_danh_hieu_da_so_huu != null) {
            for (int i = 0; i < this.id_danh_hieu_da_so_huu.size(); i++) {
                int[] dh = this.id_danh_hieu_da_so_huu.get(i);
                if (dh != null && dh.length >= 1 && dh[0] == id) {
                    if (dh.length >= 3 && dh[2] > 0 && (long)dh[2] * 1000L < System.currentTimeMillis()) {
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public int[] getDanhHieuById(int id) {
        if (this.id_danh_hieu_da_so_huu != null) {
            for (int i = 0; i < this.id_danh_hieu_da_so_huu.size(); i++) {
                int[] dh = this.id_danh_hieu_da_so_huu.get(i);
                if (dh != null && dh.length >= 1 && dh[0] == id) {
                    return dh;
                }
            }
        }
        return null;
    }

    public void offAllDh() {
        if (this.id_danh_hieu_da_so_huu != null) {
            for (int i = 0; i < this.id_danh_hieu_da_so_huu.size(); i++) {
                int[] dh = this.id_danh_hieu_da_so_huu.get(i);
                if (dh != null && dh.length >= 2) {
                    dh[1] = 0;
                }
            }
        }
        this.id_danh_hieu_su_dung = -1;
    }

    public void addDanhHieu(int id, int expireSec) {
        int expireTimestamp = (expireSec > 0) ? (int)((System.currentTimeMillis() / 1000L) + expireSec) : -1;
        int[] existing = getDanhHieuById(id);
        if (existing != null) {
            if (existing.length >= 3) {
                existing[2] = expireTimestamp;
            }
            return;
        }
        if (this.id_danh_hieu_da_so_huu == null) {
            this.id_danh_hieu_da_so_huu = new ArrayList<>();
        }
        this.id_danh_hieu_da_so_huu.add(new int[]{id, 0, expireTimestamp});
    }

    public void addDanhHieu(int id) {
        activities.DanhHieu dhTemplate = activities.DanhHieu.get_Id(id);
        int exp = (dhTemplate != null) ? dhTemplate.expire : -1;
        addDanhHieu(id, exp);
    }
}




