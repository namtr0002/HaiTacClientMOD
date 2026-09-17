package zabstracts;

import model.Player;
import network.Service;
import mob.Mob;
import map.Zone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * iDungeon — Abstract base class cho mọi phó bản (dungeon).
 *
 * ============================================================
 *  CÁCH TẠO PHÓ BẢN MỚI:
 * ============================================================
 *
 *  Bắt buộc override: create(), update(Zone)
 *
 *  Nếu muốn dùng flow join chuẩn — override thêm:
 *    canJoin(p), getJoinMode(), getJoinTitle(), getJoinDesc(),
 *    getKeyItemId(), getKeyItemType(), getKeyCost(), getDungeonId()
 *
 *  Code ngoài gọi:
 *    dungeon.requestJoin(p);   // mở dialog join
 *    dungeon.confirmJoin(p);   // sau khi player đồng ý
 * ============================================================
 */
public abstract class AbsDungeon implements zinterfaces.IDungeon {

    // ======================================================
    //  JOIN MODE — cơ chế hiển thị dialog
    // ======================================================

    public enum JoinMode {
        /** Hộp thoại Yes/No */
        YES_NO,
        /** Bảng chọn TableTickOption */
        TABLE_TICK,
        /** Join thẳng không cần xác nhận */
        DIRECT
    }

    // ======================================================
    //  STATE — field gốc (các subclass đang dùng)
    // ======================================================

    public List<Zone>    maps   = new CopyOnWriteArrayList<>();
    public List<Mob>     mobs   = new CopyOnWriteArrayList<>();
    public Set<Integer>  checkG = ConcurrentHashMap.newKeySet();
    public long          startTime;
    public long          time;       // alias: deadline timestamp (dùng rộng trong subclass)
    public long          timeLimit;
    public byte          mode;
    public String        instanceId;
    public String        creatorName;
    public List<String>  participantNames = new CopyOnWriteArrayList<>();

    // ======================================================
    //  ABSTRACT BẮT BUỘC — subclass phải override
    // ======================================================

    /** Tạo map, mob, init phó bản */
    public abstract void create();

    /** Game loop của phó bản (gọi từ Zone.update) */
    public abstract void update(Zone zone) throws IOException;

    // ======================================================
    //  JOIN CONDITIONS — override để tuỳ chỉnh
    //  Mặc định: không có điều kiện, không cần item
    // ======================================================

    /**
     * Kiểm tra điều kiện join.
     * Trả null -> OK, trả String -> hiển thị lỗi đó cho player.
     * Mặc định: luôn cho phép join.
     */
    public String canJoin(Player p) { return null; }

    /** Cơ chế join dialog. Mặc định: DIRECT (join thẳng) */
    public JoinMode getJoinMode() { return JoinMode.DIRECT; }

    /** Tiêu đề dialog join. */
    public String getJoinTitle() { return "Phó bản"; }

    /** Nội dung dialog / câu hỏi xác nhận. */
    public String getJoinDesc()  { return "Bạn có muốn vào phó bản không?"; }

    /**
     * ID item dùng làm vé/chìa khoá.
     * Trả -1 nếu không cần item (mặc định).
     */
    public int getKeyItemId()   { return -1; }

    /** Type item vé (4=item, 7=nguyên liệu). Mặc định: 4 */
    public int getKeyItemType() { return 4; }

    /** Số lượng item cần. Mặc định: 0 (không cần) */
    public int getKeyCost()     { return 0; }

    /**
     * ID duy nhất của phó bản — dùng làm idDialog cho YesNo routing.
     * Subclass override để tránh conflict. Quy ước: số âm, VD: -64, -65...
     * Mặc định: -64
     */
    public int getDungeonId() { return -64; }
    protected String key;

    public String getSeasonKey() {
        return key != null ? key : "Dungeon_" + getDungeonId();
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    // ======================================================
    //  LIFECYCLE HOOKS — optional override
    // ======================================================

    /** Hook sau khi player join thành công */
    public void onJoinSuccess(Player p) throws IOException {}

    /** Player thoát phó bản */
    public void leave(Player p) throws IOException {
        p.removeDungeon(this);
        if (this.instanceId != null) {
            database.DungeonSessionCache.gI().removeParticipant(this.instanceId, p.name);
        }
        database.DungeonSessionCache.gI().clear(p.name);
        if (p.isdie || p.hp <= 0) {
            p.isdie = false;
            p.hp = p.ability.get_hp_max(true);
            p.mp = p.ability.get_mp_max(true);
        }
    }

    /** Phó bản hết giờ */
    public void onTimeout() throws IOException {}

    /** Phó bản hoàn thành */
    public void onClear(Player p) throws IOException {}

    // ======================================================
    //  TEMPLATE: requestJoin — entry point từ menu
    // ======================================================

    /**
     * Bắt đầu flow join phó bản.
     * Kiểm tra canJoin() -> kiểm tra key item -> hiển thị dialog.
     * Code ngoài CHỈ gọi method này.
     */
    public final void requestJoin(Player p) throws IOException {
        // Kiểm tra điều kiện
        String err = canJoin(p);
        if (err != null) {
            p.getService().send_box_ThongBao_OK(err);
            return;
        }

        // Kiểm tra key item
        int keyId   = getKeyItemId();
        int keyCost = getKeyCost();
        if (keyId >= 0 && keyCost > 0) {
            int have = p.item.total_item_bag_by_id(getKeyItemType(), keyId);
            if (have < keyCost) {
                p.getService().send_box_ThongBao_OK(
                        "Bạn cần " + keyCost + " vé/chìa khoá để vào phó bản!\n(Hiện có: " + have + ")");
                return;
            }
        }

        // Hiển thị dialog
        switch (getJoinMode()) {
            case YES_NO:
                p.data_yesno = new int[]{getDungeonId()};
                p.setyesNoDialog(new model.YesNoDialog(p, getDungeonId(), getJoinTitle(), getJoinDesc(),
                        new String[]{"Đồng ý", "Huỷ"}, new byte[]{2, 1}));
                p.getService().startYesNo();
                break;
            case TABLE_TICK:
                sendTableTick(p);
                break;
            case DIRECT:
            default:
                confirmJoin(p);
                break;
        }
    }

    /**
     * Xác nhận join — gọi sau khi player bấm OK trên dialog.
     * Trừ key item -> teleport -> onJoinSuccess().
     */
    public final void confirmJoin(Player p) throws IOException {
        int keyId   = getKeyItemId();
        int keyCost = getKeyCost();
        if (keyId >= 0 && keyCost > 0) {
            int have = p.item.total_item_bag_by_id(getKeyItemType(), keyId);
            if (have < keyCost) {
                p.getService().send_box_ThongBao_OK("Không đủ vé/chìa khoá phó bản!");
                return;
            }
            p.item.remove_item47(getKeyItemType(), keyId, keyCost);
            p.item.updateInventory(false);
        }
        join(p);
        onJoinSuccess(p);
    }

    // ======================================================
    //  JOIN — teleport vào map (override nếu logic khác)
    // ======================================================

    /**
     * Teleport 1 player vào phó bản (map đầu tiên).
     */
    public void join(Player p) throws IOException {
        if (maps != null && !maps.isEmpty() && p != null) {
            p.save_previous_map();
            p.dungeon = this;
            p.addDungeon(this);
            map.Vgo vgo = new map.Vgo();
            vgo.map_go = new Zone[]{maps.get(0)};
            vgo.xnew   = getJoinX();
            vgo.ynew   = getJoinY();
            p.goto_map(vgo);
        }
    }

    /**
     * Teleport danh sách player vào phó bản (legacy API — giữ tương thích).
     */
    public void join(List<Player> listP) throws IOException {
        if (maps == null || maps.isEmpty() || listP == null) return;
        map.Vgo vgo = new map.Vgo();
        vgo.map_go = new Zone[]{maps.get(0)};
        vgo.xnew   = getJoinX();
        vgo.ynew   = getJoinY();
        for (Player p : listP) {
            if (p != null) {
                p.save_previous_map();
                p.dungeon = this;
                p.addDungeon(this);
                p.goto_map(vgo);
            }
        }
    }

    /** Toạ độ X khi teleport vào */
    public short getJoinX() { return 390; }

    /** Toạ độ Y khi teleport vào */
    public short getJoinY() { return 240; }

    // ======================================================
    //  TABLE TICK — override nếu dùng JoinMode.TABLE_TICK
    // ======================================================

    public void sendTableTick(Player p) throws IOException {}

    // ======================================================
    //  HELPERS
    // ======================================================

    public Mob get_mob(Player p, int id) {
        if (mobs != null && p != null) {
            for (Mob mob : mobs) {
                if (mob != null && (mob.map == null || p.map == null || p.map.equals(mob.map)) && mob.index == id) return mob;
            }
        }
        return null;
    }

    // ======================================================
    //  AUTO-CLEANUP — kiểm tra tất cả player rời map
    // ======================================================

    /**
     * Kiểm tra xem tất cả player (thật) đã rời khỏi zone chưa.
     * Bot player, DeTu, MercenaryBot không tính.
     *
     * @param zone Zone của phó bản
     * @return true nếu không còn player thật nào
     */
    protected boolean checkAllPlayersLeft(Zone zone) {
        if (zone == null || zone.players == null) return true;
        for (int i = 0; i < zone.players.size(); i++) {
            Player p = zone.players.get(i);
            if (p == null) continue;
            // Chỉ đếm player thật (có connection, không phải bot thuê, không phải đệ tử)
            if (p.conn != null && !p.isBot
                    && !(p instanceof model.DeTu)
                    && !(p instanceof bot.mercenary.MercenaryBot)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gọi khi dungeon kết thúc (end/timeout): xóa dungeon khỏi tất cả player trong maps
     * và dọn maps đi.
     * Subclass có thể override nếu cần logic riêng.
     */
    protected void notifyDungeonEnd() {
        database.DungeonSessionCache.gI().unregisterInstance(this);
        if (maps == null) return;
        for (Zone z : maps) {
            if (z == null || z.players == null) continue;
            List<Player> copy = new java.util.ArrayList<>(z.players);
            for (Player p : copy) {
                if (p != null) {
                    p.removeDungeon(this);
                    // Xóa cache cũ — dungeon đã end, không cần reconnect
                    database.DungeonSessionCache.gI().clear(p.name);
                }
            }
            z.map_dungeon = null;
        }
    }

    public List<Zone>   getMaps()   { return maps; }
    public List<Mob>    getMobs()   { return mobs; }
    public long         getTime()   { return time; }
    public byte         getMode()   { return mode; }

    public Set<Integer> getCheckG() {
        if (checkG == null) checkG = new HashSet<>();
        return checkG;
    }

    public boolean isTimedOut() {
        return time > 0 && System.currentTimeMillis() > time;
    }

    // iDungeon lifecycle default implementations
    @Override
    public void initialize() throws IOException {}
    @Override
    public void tick() throws IOException {}
    @Override
    public void spawn() throws IOException {}
    @Override
    public void finish(Player p) throws IOException {}
    @Override
    public void destroy() throws IOException {}
    @Override
    public void reset() throws IOException {}
    @Override
    public void timeout() throws IOException {}
    @Override
    public void reward(Player p) throws IOException {}
    @Override
    public void cleanup() throws IOException {}
}
