package event.eboss;

import bot.Bot;
import event.EventData;
import model.Player;
import core.ZUtil;
import event.SuKienNoel;
import map.Zone;
import network.Service;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Santa — Ông già Noel, extends Bot (Player-like NPC).
 *
 * Extends Bot để tận dụng hệ thống join/leave/move như real player:
 *  - Santa xuất hiện trên map như 1 character (client thấy được)
 *  - Di chuyển ngẫu nhiên mỗi 3s
 *  - Chat popup mỗi 10s
 *  - Gửi yesno gift cho player
 *  - Tự đổi map định kỳ (reset)
 *
 * Cách dùng:
 *   Santa santa = new Santa("Santa #1", idMap);
 *   santa.isOff = false;  // bật lên
 *   // update() tự gọi từ SuKienNoel.update()
 */
public class SantaNoel extends Bot {

    // ======================================================
    //  STATE
    // ======================================================

    public boolean    isOff;
    public long       timeLeave;
    public long       lastTimeSendGift;
    public long       lastTimeMove;
    public byte       indexChat;
    public Set<Integer> saveID;
    
    // Smooth movement targets
    public short targetX = -1;
    public short targetY = -1;
    private long nextMoveTime = 0;

    // ======================================================
    //  CONSTRUCTOR
    // ======================================================

    /**
     * @param botName  Tên Santa hiển thị trên map
     * @param idMap    ID map khởi tạo
     */
    public SantaNoel(String botName, int idMap) throws Exception {
        super(database.IDManager.takeID(database.IDManager.SAN_TA), botName);
        this.saveID = new HashSet<>();
        this.isOff  = true;
        this.name   = botName;

        // Khởi tạo vị trí mặc định
        this.x = 500;
        this.y = 267;
        timeLeave        = System.currentTimeMillis() + (1000L * 60 * 3);
        lastTimeSendGift = 0;
        lastTimeMove     = 0;
        indexChat        = 0;

        // Join map khởi tạo
        Zone[] zones = Zone.getMapByID(idMap);
        if (zones != null && zones.length > 0) {
            join(zones[0], this.x, this.y);
        }
    }

    // ======================================================
    //  UPDATE — gọi từ SuKienNoel game loop
    // ======================================================

    @Override
    public void update() {
        super.update();
        if (!isOff) {
            updateOn();
        }
    }

    private void updateOn() {
        long now = System.currentTimeMillis();

        // Hết giờ -> đổi map
        if (now > timeLeave) {
            reset();
            return;
        }

        // Mỗi 10s: chat popup + gửi gift yesno
        if (now - lastTimeSendGift > 10_000) {
            sendChatPopup(indexChat);
            indexChat++;
            if (indexChat >= SuKienNoel.mChatSanta.length) indexChat = 0;

            saveID.clear();
            if (this.map != null) {
                for (Player player : this.map.players) {
                    if (player.isBot) continue;
                    saveID.add(player.IDPlayer);
                    EventData eventData = player.getDataEvent(SuKienNoel.ID_EVENT);
                    if (eventData != null && eventData.data[SuKienNoel.INDEX_GIFT_SANTA] < 10) {
                        player.setyesNoDialog(new model.YesNoDialog(player, -9966, "Noel",
                                "Bạn có muốn nhận quà Noel? Cần 1 Tất Noel để nhận",
                                new String[]{"Nhận quà", "Từ chối"},
                                new byte[]{-1, -1}));
                        player.getService().startYesNo();
                    }
                }
            }
            lastTimeSendGift = now;
        }

        // Di chuyển ngẫu nhiên mượt mà trên bản đồ
        if (targetX == -1 || targetY == -1) {
            short[] pt = bot.SmartMovement.getRandomGroundPoint(this.map);
            targetX = pt[0];
            targetY = pt[1];
        }

        double dist = Math.hypot(targetX - this.x, targetY - this.y);
        if (dist > 15) {
            bot.SmartMovement.moveTowards(this, targetX, targetY, 40); // speed = 40 per second (since Santa updates every 1s)
        } else {
            if (nextMoveTime == 0) {
                nextMoveTime = now + ZUtil.random(5000, 10000); // Đứng yên 5-10s
            } else if (now > nextMoveTime) {
                short[] pt = bot.SmartMovement.getRandomGroundPoint(this.map);
                targetX = pt[0];
                targetY = pt[1];
                nextMoveTime = 0;
            }
        }
    }

    // ======================================================
    //  RESET — đổi map ngẫu nhiên trong zone
    // ======================================================

    public void reset() {
        timeLeave        = System.currentTimeMillis() + (1000L * 60 * 5);
        lastTimeSendGift = System.currentTimeMillis();
        indexChat        = 0;
        saveID.clear();

        int targetMapId = (this.map != null && this.map.template != null) ? this.map.template.id : SuKienNoel.idMapSanta[ZUtil.random(SuKienNoel.idMapSanta.length)];
        Zone[] zones = Zone.getMapByID(targetMapId);
        if (zones != null && zones.length > 0) {
            int zIdx = ZUtil.random(0, zones.length - 1);
            changeMap(targetMapId, zIdx);
        }
    }

    /**
     * Đổi map (teleport Santa sang map/zone khác).
     */
    public void changeMap(int idMap, int zone) {
        Zone[] zones = Zone.getMapByID(idMap);
        if (zones == null || zones.length == 0) return;
        if (zone < 0 || zone >= zones.length) {
            zone = 0;
        }
        Zone targetZone = zones[zone];
        if (targetZone == null) {
            for (Zone z : zones) {
                if (z != null) {
                    targetZone = z;
                    break;
                }
            }
        }
        if (targetZone == null) return;

        // Rời map cũ
        if (this.map != null) {
            leave();
        }
        // Vào map mới
        join(targetZone, this.x, this.y);
    }

    // ======================================================
    //  PACKET HELPERS
    // ======================================================

    /**
     * Gửi chat popup của Santa tới tất cả player trong map.
     */
    public void sendChatPopup(int index) {
        if (this.map == null) return;
        try {
            network.Message m = new network.Message(17);
            m.writer().writeShort(this.index_map);
            m.writer().writeByte(0);
            m.writer().writeUTF(SuKienNoel.mChatSanta[index]);
            this.map.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gửi packet move Santa đến tất cả player trong map.
     */
    public void broadcastMove() {
        if (this.map == null) return;
        try {
            network.Message m = new network.Message(1);
            m.writer().writeByte(0);
            m.writer().writeShort(this.index_map);
            m.writer().writeShort(this.x);
            m.writer().writeShort(this.y);
            this.map.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
