
import java.util.Calendar;

public final class AThMadaraFunc {
    private static final int TELEPORT_MAX_TILES = 6;
    private static java.util.Map<MainMonster,int[]> gomSavedGlobal = null;
    private static boolean gomFollowPlayer = false;
    static boolean gomUseMapCenter = false;
    public static int gomOffsetX = 0;
    public static int gomOffsetY = 0;
    public static int gomFixedX = 0;
    public static int gomFixedY = 0;
    public static void autoFireMoveToTarget(Player p) {
        if (p == null) return;
        if (p.skillCurrent != null) return;
        try {
            if (Player.typeAutoBuff == 1 && MsgAutoFire.value != null) {
                if (GameCanvas.gameTick % 5 == 1) {
                    for (int i = 0; i < MsgAutoFire.value.length; i++) {
                        short id = MsgAutoFire.value[i][0];
                        short enabled = MsgAutoFire.value[i][1];
                        if (enabled != 1) continue;
                        Skill_Info sk = Skill_Info.getSkillFromID(id);
                        if (sk == null) continue;
                        if (p.getManaNeedUse((int) sk.manaLost) > p.Mp) continue;
                        if (!DelaySkill.getDelay(sk.indexHotKey).isCoolDown()) continue;
                        try {
                            boolean started = p.beginPlayerFire(sk);
                            if (started) {
                                p.timeFristSkill = GameCanvas.timeNow;
                                return;
                            }
                        } catch (Throwable ex) { ex.printStackTrace(); }
                    }
                }
            }
        } catch (Throwable t) { t.printStackTrace(); }

        if (gomSavedGlobal != null && gomUseMapCenter) {
            try {
                p.x = gomFixedX;
                p.y = gomFixedY;
                p.toX = gomFixedX; p.toY = gomFixedY;
                p.toXNew = gomFixedX; p.toYNew = gomFixedY;
                p.vx = 0; p.vy = 0;
                p.posTransRoad = null;
            } catch (Throwable ignored) {}
        }

        if (!p.isAutoFireNew108) return;

        MainObject tgt = GameScreen.objFocus;
        // Chỉ cho phép: quái/boss. Nếu focus hiện tại không hợp lệ, bỏ focus và tìm lại.
        if (tgt == null || !isAllowedTargetForAuto(p, tgt)) {
            GameScreen.objFocus = null;
            // tìm mục tiêu hợp lệ gần nhất
            int best = Integer.MAX_VALUE;
            MainObject bestObj = null;
            for (int i = 0; i < GameScreen.vecPlayers.size(); i++) {
                MainObject o = (MainObject) GameScreen.vecPlayers.elementAt(i);
                if (o == null) continue;
                if (!isAllowedTargetForAuto(p, o)) continue;
                if (o.isRemove || o.isDie || o.Hp <= 0) continue;
                int d = MainObject.getDistance(p.x, p.y, o.x, o.y);
                if (d < best) {
                    best = d;
                    bestObj = o;
                }
            }
            if (bestObj != null) {
                GameScreen.objFocus = bestObj;
                tgt = bestObj;
            } else {
                return;
            }
        }

        if (tgt.isRemove || tgt.Hp <= 0 || tgt.isDie) {
            try { p.nextMonster(); } catch (Throwable ignored) {}
            tgt = GameScreen.objFocus;
            if (tgt == null || !isAllowedTargetForAuto(p, tgt)) return;
        }
        // Nếu gom theo center thì bỏ qua kiểm tra attackable; còn lại phải hợp lệ
        if (!isAllowedTargetForAuto(p, tgt) && (gomSavedGlobal == null || !gomUseMapCenter)) return;

        Hotkey[] row = p.hotkeyPlayer != null ? p.hotkeyPlayer[p.currentTab] : null;
        if (row == null || row.length == 0) return;
        int n = row.length;
        boolean isAutoNew = p.isAutoFireNew108;
        if (isAutoNew) {
            if (p.IndexFire < 0 || p.IndexFire >= n) p.IndexFire = 0;
        }

        int tileSize = GameCanvas.loadmap != null && GameCanvas.loadmap.wTile > 0
                       ? GameCanvas.loadmap.wTile
                       : (LoadMap.wTile > 0 ? LoadMap.wTile : 24);
        double teleportMaxPixels = TELEPORT_MAX_TILES * tileSize;

        for (int iter = 0; iter < n; ++iter) {
            int slot = isAutoNew ? ((p.IndexFire + iter) % n) : iter;
            Hotkey hk = row[slot];
            if (hk == null) continue;
            if (hk.itemcur != null) {
                try {
                    p.setActionHotKey(slot);
                    if (isAutoNew) p.IndexFire = (slot + 1) % n;
                } catch (Throwable ex) { ex.printStackTrace(); }
                break;
            }
            if (hk.skill != null) {
                Skill_Info sk = Skill_Info.getSkillFromID(hk.skill.ID);
                if (sk == null) continue;
                if (!DelaySkill.getDelay(sk.indexHotKey).isCoolDown()) continue;
                if (p.getManaNeedUse((int) sk.manaLost) > p.Mp) continue;
                if (sk.typeSkill == 2) {
                    try {
                        p.setActionHotKey(slot);
                        p.timeFristSkill = GameCanvas.timeNow;
                        if (isAutoNew) p.IndexFire = (slot + 1) % n;
                    } catch (Throwable ex) { ex.printStackTrace(); }
                    break;
                }
                if (gomSavedGlobal != null && gomUseMapCenter) {
                    try {
                        p.setActionHotKey(slot);
                        p.timeFristSkill = GameCanvas.timeNow;
                        if (isAutoNew) p.IndexFire = (slot + 1) % n;
                    } catch (Throwable ex) { ex.printStackTrace(); }
                    break;
                }
                double dx = p.x - tgt.x;
                double dy = p.y - tgt.y;
                double dist = Math.sqrt(dx * dx + dy * dy);
                // Sửa: teleport khi quá xa (dist > max), không phải khi quá gần
                boolean doTeleport = dist > teleportMaxPixels;

                if (doTeleport) {
                    int tx = (tgt.x / tileSize) * tileSize + tileSize / 2;
                    int ty = (tgt.y / tileSize) * tileSize + tileSize / 2;
                    sendTeleport(p, tx, ty);
                    p.posTransRoad = null;
                    try { p.vx = 0; p.vy = 0; } catch (Throwable ignored) {}
                }

                try {
                    p.setActionHotKey(slot);
                    p.timeFristSkill = GameCanvas.timeNow;
                    if (isAutoNew) p.IndexFire = (slot + 1) % n;
                } catch (Throwable ex) { ex.printStackTrace(); }
                break;
            }
        }
    }

    // Chỉ đánh: quái/boss (typeObject==1). Bỏ qua NPC và người chơi.
    private static boolean isAllowedTargetForAuto(Player p, MainObject t) {
        if (t == null) return false;
        return t.typeObject == 1;
    }

    public static java.util.Map<MainMonster,int[]> startGomAllFollow(Player p, int offsetX, int offsetY, boolean follow, boolean useMapCenter) {
        java.util.Map<MainMonster,int[]> saved = new java.util.HashMap<>();
        if (p == null) return saved;

        try {
            mVector vec = GameScreen.vecPlayers;
            if (vec == null) return saved;

            int targetX;
            int targetY;
            if (useMapCenter && GameCanvas.loadmap != null) {
                int maxW = GameCanvas.loadmap.maxWMap;
                int maxH = (int) (GameCanvas.loadmap.maxHMap >= 0 ? GameCanvas.loadmap.maxHMap : (GameCanvas.h > 0 ? GameCanvas.h : maxW));
                targetX = maxW / 2 + offsetX;
                targetY = maxH / 2 + offsetY;
                gomFixedX = targetX; gomFixedY = targetY;
                gomUseMapCenter = true;
            } else {
                targetX = p.x + offsetX;
                targetY = p.y + offsetY;
                gomUseMapCenter = false;
            }

            for (int i = 0; i < vec.size(); ++i) {
                Object o = vec.elementAt(i);
                if (o instanceof MainMonster) {
                    MainMonster m = (MainMonster) o;
                    if (m == null || m.isRemove) continue;
                    try {
                        saved.put(m, new int[]{m.x, m.y, m.toX, m.toY, m.toXNew, m.toYNew, m.vx, m.vy});
                    } catch (Throwable ignored) {}
                    m.gomX = targetX; m.gomY = targetY;
                    m.x = targetX; m.y = targetY;
                    m.toX = targetX; m.toY = targetY;
                    m.toXNew = targetX; m.toYNew = targetY;
                    m.vx = 0; m.vy = 0;
                    m.isGom = true;
                }
            }

            gomSavedGlobal = saved;
            gomFollowPlayer = follow;
            gomOffsetX = offsetX;
            gomOffsetY = offsetY;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return saved;
    }

    public static void updateGomPositions(Player p) {
        if (p == null || gomSavedGlobal == null) return;
        try {
            int targetX;
            int targetY;
            if (gomUseMapCenter) {
                targetX = gomFixedX;
                targetY = gomFixedY;
            } else if (gomFollowPlayer) {
                targetX = p.x + gomOffsetX;
                targetY = p.y + gomOffsetY;
            } else {
                java.util.Iterator<java.util.Map.Entry<MainMonster,int[]>> it = gomSavedGlobal.entrySet().iterator();
                if (!it.hasNext()) return;
                MainMonster first = it.next().getKey();
                if (first == null) return;
                targetX = first.gomX;
                targetY = first.gomY;
            }

            for (MainMonster m : gomSavedGlobal.keySet()) {
                if (m == null) continue;
                m.gomX = targetX; m.gomY = targetY;
                m.x = targetX; m.y = targetY;
                m.toX = targetX; m.toY = targetY;
                m.toXNew = targetX; m.toYNew = targetY;
                m.vx = 0; m.vy = 0;
                m.isGom = true;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void stopGomAll(java.util.Map<MainMonster,int[]> saved) {
        java.util.Map<MainMonster,int[]> toRestore = saved != null ? saved : gomSavedGlobal;
        if (toRestore == null) return;
        try {
            for (java.util.Map.Entry<MainMonster,int[]> e : toRestore.entrySet()) {
                MainMonster m = e.getKey();
                int[] s = e.getValue();
                if (m == null || s == null || s.length < 8) continue;
                try {
                    m.x = s[0]; m.y = s[1];
                    m.toX = s[2]; m.toY = s[3];
                    m.toXNew = s[4]; m.toYNew = s[5];
                    m.vx = s[6]; m.vy = s[7];
                } catch (Throwable ignored) {}
                m.isGom = false;
                m.gomX = 0; m.gomY = 0;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (gomSavedGlobal != null) gomSavedGlobal.clear();
            gomSavedGlobal = null;
            gomFollowPlayer = false;
            gomUseMapCenter = false;
            gomOffsetX = gomOffsetY = 0;
            gomFixedX = gomFixedY = 0;
        }
    }
    private static boolean isAttackable(MainObject t) {
        return t != null && !t.returnAction() && !t.isDie && t.Hp > 0 && !t.isRemove;
    }

    private static void sendTeleport(Player p, int x, int y) {
        try {
            GlobalService.getInstance().Obj_Move((short) x, (short) y);
            p.x = x; p.y = y;
            try { p.CX = x; p.CY = y; p.isSendMove = true; } catch (Throwable ignored) {}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
   public static void paintShowDateTime(mGraphics g) {
        try {
            Calendar cal = CRes.getTime();
            String timeStr = String.format(
                "%02d:%02d:%02d %02d-%02d-%04d",
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR)
            );
            int imgW = 22, imgH = 16;
            int chatX = Interface_Game.xNumMess;
            int chatY = Interface_Game.yNumMess;
            try {
                chatY += Interface_Game.numMess.yNum;
            } catch (Throwable ignored) {}

            int textY = chatY + imgH + 4;
            int textW = mFont.tahoma_7_white.getWidth(timeStr);
            int drawX = chatX + (imgW / 2) - (textW / 2);
            if (drawX < 2) drawX = 2;
            if (drawX + textW > MotherCanvas.w - 2) {
                drawX = MotherCanvas.w - 2 - textW;
            }
            mFont.tahoma_7_white.drawString(g, timeStr, drawX, textY, 0);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
   
   public static void autoSellItem(Player p) {
       //để đây code sau
   }

    // Trong AThMadaraFunc.autoReviceV1
    public static void autoReviceV1(Player p) {
        if (p == null || !p.isDie || !p.isAutoRevice) return;
        try {
            // Lưu ID map và toạ độ hiện tại (để quay lại đúng vị trí)
            if (GameCanvas.loadmap != null) {
                AThMadaraMOD.lastMapId = GameCanvas.loadmap.idMapLoadMap;
            }
            if (GameScreen.player != null) {
                AThMadaraMOD.lastX = GameScreen.player.x;
                AThMadaraMOD.lastY = GameScreen.player.y;
            }
            // Lưu khu hiện tại nếu có (dùng message 23 để set lại)
            try {
                AThMadaraFunc.saveCurrentZone();
            } catch (Throwable ignored) {}

            // Bật cờ: sau hồi sinh → quay lại map cũ
            AThMadaraMOD.shouldReturnToLastMap = true;

            // Gửi hồi sinh về làng
            GlobalService.getInstance().Player_Revice((byte)0);
            Interface_Game.addInfoPlayerNormal("Đã hồi sinh. Sẽ quay lại map cũ...", mFont.tahoma_7_yellow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lưu khu hiện tại (nếu server/phía client có khái niệm khu). Ở đây ta đọc từ giao thức nếu có.
    private static byte savedZone = -1;
    public static void saveCurrentZone() {
        // Không tìm thấy biến zone trong client, nhưng có message 23 dùng để đổi khu.
        // Ta không thể đọc khu hiện tại từ client ⇒ dựa vào server-side if any.
        // Mặc định giữ -1 nếu không xác định được.
        // Nếu bạn có biến current zone, gán nó vào savedZone ở đây.
    }

    public static void restoreSavedZoneIfAny() {
        if (savedZone >= 0 && savedZone <= 9) {
            try {
                GlobalService.getInstance().AB((byte)0, savedZone);
            } catch (Throwable ignored) {}
        }
    }
    
    
}
