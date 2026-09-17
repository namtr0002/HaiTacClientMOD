package bot.botplayer.ai;

import bot.botplayer.BotPlayerReal;
import bot.botplayer.BotPlayerManager;
import bot.SmartMovement;
import map.Vgo;
import core.ZUtil;
import java.util.List;

/**
 * BotMovementController — Quản lý việc di chuyển, nhảy, bám theo, chuyển map của BotPlayer.
 */
public class BotMovementController {

    private final BotPlayerReal bot;
    private short lastX = -1;
    private short lastY = -1;
    private int stuckCount = 0;
    private short lastTargetX = -1;
    private short lastTargetY = -1;
    private long vgoEnterTime = 0;

    public BotMovementController(BotPlayerReal bot) {
        this.bot = bot;
    }

    /**
     * Di chuyển tới mục tiêu tx, ty từng bước một theo tốc độ chạy.
     */
    public void moveTowards(short tx, short ty) {
        if (bot.map == null || bot.isdie) return;

        int minX = SmartMovement.getSafeMinX(bot.map);
        int maxX = SmartMovement.getSafeMaxX(bot.map);
        int minY = SmartMovement.getSafeMinY(bot.map);
        int maxY = SmartMovement.getSafeMaxY(bot.map);

        tx = (short) Math.max(minX, Math.min(maxX, tx));
        ty = (short) Math.max(minY, Math.min(maxY, ty));

        if (bot.x < minX || bot.x > maxX || bot.y < minY || bot.y > maxY) {
            bot.x = (short) Math.max(minX, Math.min(maxX, bot.x));
            bot.y = (short) Math.max(minY, Math.min(maxY, bot.y));
        }

        int dx = tx - bot.x;
        int dy = ty - bot.y;
        double dist = Math.hypot(dx, dy);
        
        if (dist < 1) {
            stuckCount = 0;
            return;
        }

        // Tối ưu hóa Anti-Lag: Dịch chuyển tức thời nếu không có người chơi thật
        boolean hasRealPlayer = BotPlayerManager.hasRealPlayer(bot.map);
        if (!hasRealPlayer) {
            bot.x = tx;
            bot.y = ty;
            stuckCount = 0;
            return;
        }

        // Stuck detection
        if (tx == lastTargetX && ty == lastTargetY) {
            double movedDist = Math.hypot(bot.x - lastX, bot.y - lastY);
            if (movedDist < 2) {
                stuckCount++;
            }
        } else {
            stuckCount = 0;
            lastTargetX = tx;
            lastTargetY = ty;
        }

        lastX = bot.x;
        lastY = bot.y;

        short originalTx = tx;
        short originalTy = ty;

        // Nếu kẹt quá lâu (10 tick), dịch chuyển trực tiếp đến đích
        if (stuckCount >= 10) {
            bot.x = originalTx;
            bot.y = originalTy;
            stuckCount = 0;
            try {
                if (bot.map != null) {
                    bot.map.getService().move((byte) 0, bot.index_map, bot.x, bot.y);
                }
            } catch (Exception ignored) {}
            return;
        }

        // Logic đi chéo/trượt tường khi mới phát hiện kẹt (chỉ khi ở xa đích > 50)
        if (stuckCount >= 2 && stuckCount < 10 && dist > 50) {
            if (Math.abs(dx) > Math.abs(dy)) {
                tx = bot.x; // giữ nguyên vị trí X
                ty = (short) (bot.y + (stuckCount % 2 == 0 ? 25 : -25)); // trượt dọc
            } else {
                tx = (short) (bot.x + (stuckCount % 2 == 0 ? 25 : -25)); // trượt ngang
                ty = bot.y; // giữ nguyên vị trí Y
            }

            tx = (short) Math.max(minX, Math.min(maxX, tx));
            ty = (short) Math.max(minY, Math.min(maxY, ty));

            dx = tx - bot.x;
            dy = ty - bot.y;
            dist = Math.hypot(dx, dy);
        }

        // Random human delay: thi thoảng đứng lại ngập ngừng (không đi trong tick này)
        if (dist > 50 && ZUtil.random(1000) < 50) { 
            return;
        }

        short speed = 70; // Tốc độ chạy giống người chơi thật
        if (dist > speed) {
            bot.x += (short) (dx * speed / dist);
            bot.y += (short) (dy * speed / dist);
        } else {
            bot.x = tx;
            bot.y = ty;
        }

        bot.x = (short) Math.max(minX, Math.min(maxX, bot.x));
        bot.y = (short) Math.max(minY, Math.min(maxY, bot.y));

        try {
            if (bot.map != null) {
                bot.map.getService().move((byte) 0, bot.index_map, bot.x, bot.y);
            }
        } catch (Exception ignored) {}
    }

    /**
     * Di chuyển ngẫu nhiên quanh tọa độ hiện tại.
     */
    public void moveRandom() {
        if (bot.map == null) return;
        short[] pt = SmartMovement.getRandomGroundPoint(bot.map);
        moveTowards(pt[0], pt[1]);
    }

    /**
     * Đi men theo con đường di chuyển liên bản đồ (pathfinding).
     */
    public void traversePath() {
        List<Integer> path = bot.currentPath;
        int idx = bot.pathIndex;
        if (path == null || idx < 0 || idx >= path.size()) {
            bot.state = "FARM";
            bot.currentPath = null;
            return;
        }
        map.Zone curMap = bot.map;
        if (curMap == null || curMap.template == null || curMap.template.vgos == null) {
            return;
        }

        int nextMapId = path.get(idx);
        Vgo nextVgo = null;
        for (Vgo vgo : curMap.template.vgos) {
            if (vgo.id_map_go == nextMapId) {
                nextVgo = vgo;
                break;
            }
        }

        if (nextVgo != null) {
            double dist = Math.hypot(nextVgo.xold - bot.x, nextVgo.yold - bot.y);
            if (dist > 50) {
                vgoEnterTime = 0; // Reset delay if bot moves away
                moveTowards(nextVgo.xold, nextVgo.yold);
            } else {
                long now = System.currentTimeMillis();
                if (vgoEnterTime == 0) {
                    vgoEnterTime = now + 200 + ZUtil.random(300); // Trễ ngắn 0.2s - 0.5s tự nhiên
                    return;
                }
                if (now < vgoEnterTime) {
                    return; // Đang đứng chờ ở portal
                }
                vgoEnterTime = 0; // Reset cho lần chuyển map tiếp theo

                try {
                    map.Zone[] map_go = nextVgo.map_go;
                    if (map_go == null || map_go.length == 0 || map_go[0] == null || map_go[0].template == null) {
                        bot.currentPath = null;
                        bot.state = "FARM";
                        bot.ischangemap = false;
                        return;
                    }

                    int destMapId = map_go[0].template.id;
                    boolean blocked = false;
                    if (destMapId == 254 && bot.level >= 50) blocked = true;
                    else if (destMapId == 255 && !(bot.level >= 40 && bot.level <= 59)) blocked = true;
                    else if (destMapId == 257 && !(bot.level >= 50 && bot.level <= 79)) blocked = true;
                    else if (destMapId == 258 && bot.level < 60) blocked = true;

                    if (!blocked) {
                        // Enforce quest map unlock limit — chặn 100% bot chưa làm nhiệm vụ
                        int mapIDcanGo = bot.checkQuest();
                        if (!map.Zone.isMapNoQuestLimit(destMapId) && destMapId > mapIDcanGo) {
                            // Bypass CHỈ được phép nếu bot đang đi chính xác đến map nhiệm vụ (idMapHelp)
                            // Không bypass theo NPC id vì đó là idmenu, không phải map id
                            boolean isQuestPath = false;
                            template.QuestP activeQuest = bot.getMainQuest();
                            if (activeQuest != null && activeQuest.template != null
                                    && activeQuest.template.statusQuest == 1) {
                                // Chỉ bypass khi đang ở phase LÀM nhiệm vụ (status=1)
                                // và destMapId chính xác là map nhiệm vụ cần đến
                                int qHelpMap = activeQuest.template.idMapHelp;
                                if (qHelpMap > 0 && destMapId == qHelpMap) {
                                    isQuestPath = true;
                                }
                            }
                            if (!isQuestPath) {
                                // Chặn: reset về FARM, không cho đi qua
                                bot.currentPath = null;
                                bot.state = "FARM";
                                bot.ischangemap = false;
                                if (core.ZUtil.random(100) < 20 && bot.map != null) {
                                    try {
                                        bot.map.send_chat_popup(0, bot.index_map, "Chưa xong nv, chưa đi được!");
                                    } catch (Exception ignored) {}
                                }
                                return;
                            }
                        }
                    }

                    if (blocked) {
                        bot.currentPath = null;
                        bot.state = "FARM";
                        bot.ischangemap = false;
                        return;
                    }

                    bot.ischangemap = true;
                    // Thay vì gọi goto_map() chờ phản hồi từ client, tự động cho bot sang map mới
                    bot.leave();
                    bot.x = nextVgo.xnew;
                    bot.y = nextVgo.ynew;

                    if (bot.dungeon != null && bot.dungeon.maps != null) {
                        int id_map = map_go[0].template.id;
                        for (int i = 0; i < bot.dungeon.maps.size(); i++) {
                            map.Zone z = bot.dungeon.maps.get(i);
                            if (z != null && z.template != null && id_map == z.template.id) {
                                map_go = new map.Zone[]{z};
                                break;
                            }
                        }
                        // Thiết lập tọa độ xuất hiện chính xác cho từng ải phó bản
                        switch (map_go[0].template.id) {
                            case 168 -> { bot.x = 1490; bot.y = 260; }
                            case 169 -> { bot.x = 760;  bot.y = 240; }
                            case 170 -> { bot.x = 100;  bot.y = 245; }
                            case 171 -> { bot.x = 675;  bot.y = 270; }
                            case 172 -> { bot.x = 113;  bot.y = 240; }
                            case 173 -> { bot.x = 135;  bot.y = 255; }
                            case 174 -> { bot.x = 121;  bot.y = 225; }
                            case 175 -> { bot.x = 156;  bot.y = 230; }
                            case 176 -> { bot.x = 142;  bot.y = 255; }
                        }
                    }
                    if (map_go != null && map_go.length > 0) {
                        map.Zone selectedZone = BotPlayerManager.findBestZoneForBot(bot, destMapId);
                        if (selectedZone != null) {
                            bot.join(selectedZone, bot.x, bot.y);
                        } else {
                            bot.join(map_go[0], bot.x, bot.y);
                        }
                    }
                    
                    bot.pathIndex++;
                    if (bot.pathIndex >= path.size()) {
                        bot.state = "FARM";
                        bot.currentPath = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (bot.targetMapId > 0) {
                bot.currentPath = Pathfinder.findPath(curMap.template.id, bot.targetMapId);
                bot.pathIndex = 0;
            } else {
                bot.state = "FARM";
            }
        }
    }

    /**
     * Chạy xa khỏi mục tiêu (kiting) khi HP thấp.
     */
    public void kiteTarget(short targetX, short targetY) {
        int dx = bot.x - targetX;
        int dy = bot.y - targetY;
        double dist = Math.hypot(dx, dy);
        
        // Nếu đã ở xa thì không cần chạy thêm
        if (dist > 200) {
            return;
        }

        // Tạo vector di chuyển ra xa
        if (dist == 0) {
            dx = ZUtil.random(-1, 2);
            dy = ZUtil.random(-1, 2);
            if (dx == 0 && dy == 0) dx = 1;
            dist = Math.hypot(dx, dy);
        }

        short speed = 20; // Chạy nhanh hơn bình thường một chút
        short tx = (short) (bot.x + (dx * speed / dist) * 3);
        short ty = (short) (bot.y + (dy * speed / dist) * 3);

        // Đảm bảo không vượt quá giới hạn map
        map.MapTemplate tmpl = (bot.map != null) ? bot.map.template : null;
        if (tmpl != null) {
            if (tx < 0) tx = 0;
            if (ty < 0) ty = 0;
            if (tmpl.maxW > 0 && tx > tmpl.maxW) {
                tx = tmpl.maxW;
            }
            if (tmpl.maxH > 0 && ty > tmpl.maxH) {
                ty = tmpl.maxH;
            }
        }
        
        moveTowards(tx, ty);
    }
}
