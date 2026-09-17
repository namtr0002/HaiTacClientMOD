package bot;

import model.Player;
import core.ZUtil;
import map.Zone;
import map.Npc;
import mob.Mob;
import map.Vgo;
import network.Message;
import java.io.IOException;

public class SmartMovement {

    /**
     * Lấy khoảng biên an toàn X tối thiểu của map.
     */
    public static int getSafeMinX(Zone map) {
        return 60;
    }

    /**
     * Lấy khoảng biên an toàn X tối đa của map.
     */
    public static int getSafeMaxX(Zone map) {
        if (map == null || map.template == null) return 800;
        int maxW = map.template.maxW;
        if (maxW <= 120) return 800;
        return maxW - 60;
    }

    /**
     * Lấy khoảng biên an toàn Y tối thiểu (đỉnh cao nhất cho phép) của map.
     */
    public static int getSafeMinY(Zone map) {
        if (map == null || map.template == null) return 120;
        int maxH = map.template.maxH;
        if (maxH <= 150) return 50;
        return Math.max(60, maxH - 450);
    }

    /**
     * Lấy khoảng biên an toàn Y tối đa (đáy thấp nhất cho phép) của map.
     */
    public static int getSafeMaxY(Zone map) {
        if (map == null || map.template == null) return 320;
        int maxH = map.template.maxH;
        if (maxH <= 100) return 260;
        return maxH - 30;
    }

    /**
     * Lấy một điểm ngẫu nhiên an toàn trên mặt đất / sàn đi bộ của bản đồ.
     */
    public static short[] getRandomGroundPoint(Zone map) {
        if (map == null || map.template == null) {
            return new short[]{500, 260};
        }

        int minX = getSafeMinX(map);
        int maxX = getSafeMaxX(map);
        if (maxX <= minX) maxX = minX + 200;

        short rx = (short) ZUtil.random(minX, maxX);

        // 1. Ưu tiên lấy theo độ cao mặt đất thực tế từ Mob có sẵn trong map
        if (map.mobs != null && !map.mobs.isEmpty()) {
            java.util.List<Mob> mobList = new java.util.ArrayList<>(map.mobs.values());
            if (!mobList.isEmpty()) {
                Mob m = mobList.get(ZUtil.random(mobList.size()));
                if (m != null && m.y > 50 && m.y < getSafeMaxY(map)) {
                    short ry = (short) (m.y + ZUtil.random(-10, 10));
                    ry = (short) Math.max(getSafeMinY(map), Math.min(getSafeMaxY(map), ry));
                    return new short[]{rx, ry};
                }
            }
        }

        // 2. Ưu tiên thứ 2: lấy theo độ cao đứng của NPC trong map
        if (map.template.npcs != null && !map.template.npcs.isEmpty()) {
            Npc n = map.template.npcs.get(ZUtil.random(map.template.npcs.size()));
            if (n != null && n.y > 50 && n.y < getSafeMaxY(map)) {
                short ry = (short) (n.y + ZUtil.random(-10, 10));
                ry = (short) Math.max(getSafeMinY(map), Math.min(getSafeMaxY(map), ry));
                return new short[]{rx, ry};
            }
        }

        // 3. Ưu tiên thứ 3: lấy theo cổng VGO trong map
        if (map.template.vgos != null && !map.template.vgos.isEmpty()) {
            Vgo v = map.template.vgos.get(ZUtil.random(map.template.vgos.size()));
            if (v != null && v.yold > 50 && v.yold < getSafeMaxY(map)) {
                short ry = (short) (v.yold + ZUtil.random(-10, 10));
                ry = (short) Math.max(getSafeMinY(map), Math.min(getSafeMaxY(map), ry));
                return new short[]{rx, ry};
            }
        }

        // 4. Tính theo độ cao map chuẩn nếu không có Mob/NPC/VGO
        int maxH = map.template.maxH > 100 ? map.template.maxH : 380;
        int hBack = map.template.HBack;
        int groundY;
        if (hBack > 50 && hBack < maxH) {
            groundY = maxH - hBack + 20;
        } else {
            groundY = maxH - 70;
        }

        groundY = Math.max(getSafeMinY(map), Math.min(getSafeMaxY(map), groundY));
        short ry = (short) (groundY + ZUtil.random(-15, 15));
        ry = (short) Math.max(getSafeMinY(map), Math.min(getSafeMaxY(map), ry));

        return new short[]{rx, ry};
    }

    /**
     * Di chuyển Player/Bot từng bước mượt mà tới mục tiêu (tx, ty) theo tốc độ speed.
     * Đảm bảo TUYỆT ĐỐI không bao giờ văng ra khỏi biên an toàn của map.
     */
    public static void moveTowards(Player p, short tx, short ty, int speed) {
        Zone map = p.map;
        if (map == null || p.isdie) {
            return;
        }

        int minX = getSafeMinX(map);
        int maxX = getSafeMaxX(map);
        int minY = getSafeMinY(map);
        int maxY = getSafeMaxY(map);

        // Giới hạn tọa độ đích trong vùng an toàn
        tx = (short) Math.max(minX, Math.min(maxX, tx));
        ty = (short) Math.max(minY, Math.min(maxY, ty));

        // Kiểm tra nếu vị trí hiện tại của bot đang bị kẹt ngoài map -> kéo ngay về vùng an toàn
        if (p.x < minX || p.x > maxX || p.y < minY || p.y > maxY) {
            p.x = (short) Math.max(minX, Math.min(maxX, p.x));
            p.y = (short) Math.max(minY, Math.min(maxY, p.y));
        }

        int dx = tx - p.x;
        int dy = ty - p.y;
        double dist = Math.hypot(dx, dy);

        if (dist <= speed || speed <= 0) {
            p.x = tx;
            p.y = ty;
        } else {
            p.x += (short) (dx * speed / dist);
            p.y += (short) (dy * speed / dist);
        }

        // Đảm bảo sau bước di chuyển, tọa độ vẫn luôn nằm trong biên an toàn
        p.x = (short) Math.max(minX, Math.min(maxX, p.x));
        p.y = (short) Math.max(minY, Math.min(maxY, p.y));

        // Gửi gói tin di chuyển đồng bộ tới các người chơi khác trong map
        try {
            Message m = new Message(1);
            m.writer().writeByte(0);
            m.writer().writeShort(p.index_map);
            m.writer().writeShort(p.x);
            m.writer().writeShort(p.y);
            map.send_msg_all_p(m, null, true);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Giữ tọa độ Player/Bot trong vùng an toàn của map hiện tại (Safe Clamp).
     */
    public static void clampToSafeBounds(Player p) {
        if (p == null || p.map == null) return;
        Zone map = p.map;
        int minX = getSafeMinX(map);
        int maxX = getSafeMaxX(map);
        int minY = getSafeMinY(map);
        int maxY = getSafeMaxY(map);

        p.x = (short) Math.max(minX, Math.min(maxX, p.x));
        p.y = (short) Math.max(minY, Math.min(maxY, p.y));
    }
}
