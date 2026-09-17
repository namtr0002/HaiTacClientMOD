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

        if (AThMadaraMOD.isSlaughterPausedByMove) return;
        if (AThMadaraMOD.isSlaughterPausedByQuest) return;
        if (p.vx != 0 || p.vy != 0 || GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(1) || GameCanvas.isKeyPressed(2) || GameCanvas.isKeyPressed(3)) return;

        boolean isAutoNew = p.isAutoFireNew108 || AThMadaraMOD.isTuDanhActive;

        // 1. Auto Buff Skills (do not require target)
        try {
            if (Player.typeAutoBuff == 1 && MsgAutoFire.value != null) {
                if (GameCanvas.gameTick % 5 == 1) {
                    for (int i = 0; i < MsgAutoFire.value.length; i++) {
                        short id = MsgAutoFire.value[i][0];
                        short enabled = MsgAutoFire.value[i][1];
                        if (enabled != 1) continue;
                        Skill_Info sk = Skill_Info.getSkillFromID(id);
                        if (sk == null) continue;
                        if (sk.typeSkill == 2) { // Buff skill
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

        if (AThMadaraMOD.isSlaughterPausedByQuest) return;

        if (!isAutoNew) return;

        MainObject tgt = GameScreen.objFocus;
        // Chỉ cho phép: quái/boss/người hợp lệ theo cấu hình. Nếu focus hiện tại không hợp lệ, bỏ focus và tìm lại.
        if (tgt == null || !isAllowedTargetForAuto(p, tgt)) {
            GameScreen.objFocus = null;
            // tìm mục tiêu hợp lệ gần nhất
            int best = Integer.MAX_VALUE;
            MainObject bestObj = null;
            
            // Nếu chọn "Tất cả" (slaughterSelectType == 0) và không chọn mục tiêu cụ thể, thì ưu tiên tìm theo slaughterPriority
            if (AThMadaraMOD.slaughterSelectType == 0 && AThMadaraMOD.slaughterTargetName == null) {
                int prioType = AThMadaraMOD.slaughterPriority; // 1 = Quái, 2 = Người
                int firstType = (prioType == 1) ? 1 : 0;
                int secondType = (prioType == 1) ? 0 : 1;
                
                // Quét loại ưu tiên thứ nhất
                for (int i = 0; i < GameScreen.vecPlayers.size(); i++) {
                    MainObject o = (MainObject) GameScreen.vecPlayers.elementAt(i);
                    if (o == null || o.typeObject != firstType) continue;
                    if (!isAllowedTargetForAuto(p, o)) continue;
                    if (o.isRemove || o.isDie || o.Hp <= 0) continue;
                    int d = MainObject.getDistance(p.x, p.y, o.x, o.y);
                    if (d < best) {
                        best = d;
                        bestObj = o;
                    }
                }
                
                // Nếu không tìm thấy, quét loại thứ hai
                if (bestObj == null) {
                    best = Integer.MAX_VALUE;
                    for (int i = 0; i < GameScreen.vecPlayers.size(); i++) {
                        MainObject o = (MainObject) GameScreen.vecPlayers.elementAt(i);
                        if (o == null || o.typeObject != secondType) continue;
                        if (!isAllowedTargetForAuto(p, o)) continue;
                        if (o.isRemove || o.isDie || o.Hp <= 0) continue;
                        int d = MainObject.getDistance(p.x, p.y, o.x, o.y);
                        if (d < best) {
                            best = d;
                            bestObj = o;
                        }
                    }
                }
            } else {
                // Quét thông thường (không phân biệt hoặc có mục tiêu cụ thể)
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
        int n = (row != null) ? row.length : 0;

        // Check for items on the hotkey bar
        if (row != null) {
            if (p.IndexFire < 0 || p.IndexFire >= n) p.IndexFire = 0;
            for (int iter = 0; iter < n; iter++) {
                int slot = (p.IndexFire + iter) % n;
                Hotkey hk = row[slot];
                if (hk != null && hk.itemcur != null) {
                    try {
                        p.setActionHotKey(slot);
                        p.IndexFire = (slot + 1) % n;
                        return; // item used, stop
                    } catch (Throwable ex) { ex.printStackTrace(); }
                }
            }
        }

        int tileSize = GameCanvas.loadmap != null && GameCanvas.loadmap.wTile > 0
                       ? GameCanvas.loadmap.wTile
                       : (LoadMap.wTile > 0 ? LoadMap.wTile : 24);
        double teleportMaxPixels = TELEPORT_MAX_TILES * tileSize;

        // 2. Auto Active Skills (requires target, uses MsgAutoFire.value if present)
        boolean isAutoActiveSkillOff = (Player.typeAutoFireMain == -1 || Player.AutoFireCur == -1);
        boolean hasCastedActiveSkill = false;
        try {
            if (!isAutoActiveSkillOff && MsgAutoFire.value != null && MsgAutoFire.value.length > 0) {
                int activeSkillCount = 0;
                for (int i = 0; i < MsgAutoFire.value.length; i++) {
                    short id = MsgAutoFire.value[i][0];
                    short enabled = MsgAutoFire.value[i][1];
                    if (enabled != 1) continue;
                    Skill_Info sk = Skill_Info.getSkillFromID(id);
                    if (sk == null) continue;
                    
                    if (sk.typeSkill == 1 || sk.typeSkill == 4) { // Active skill
                        if (Player.typeAutoFireMain == 2 && activeSkillCount > 0) {
                            break; // Mode "Chiêu 1": chỉ dùng 1 chiêu active đầu tiên
                        }
                        activeSkillCount++;
                        if (p.getManaNeedUse((int) sk.manaLost) > p.Mp) continue;
                        if (!DelaySkill.getDelay(sk.indexHotKey).isCoolDown()) continue;
                        
                        double dx = p.x - tgt.x;
                        double dy = p.y - tgt.y;
                        double dist = Math.sqrt(dx * dx + dy * dy);
                        boolean doTeleport = dist > teleportMaxPixels;
                        
                        if (doTeleport && (gomSavedGlobal == null || !gomUseMapCenter)) {
                            int tx = (tgt.x / tileSize) * tileSize + tileSize / 2;
                            int ty = (tgt.y / tileSize) * tileSize + tileSize / 2;
                            int[] safePos = getSafePositionAwayFromPortals(tx, ty, p.x, p.y);
                            tx = safePos[0];
                            ty = safePos[1];
                            sendTeleport(p, tx, ty);
                            p.posTransRoad = null;
                            try { p.vx = 0; p.vy = 0; } catch (Throwable ignored) {}
                        }
                        
                        try {
                            boolean started = p.beginPlayerFire(sk);
                            if (started) {
                                p.timeFristSkill = GameCanvas.timeNow;
                                return;
                            }
                        } catch (Throwable ex) { ex.printStackTrace(); }
                    }
                }
                hasCastedActiveSkill = true; // Checked active skills list
            }
        } catch (Throwable t) { t.printStackTrace(); }

        // Fallback to hotkey bar active skills if MsgAutoFire.value is not configured/empty
        if (!isAutoActiveSkillOff && !hasCastedActiveSkill && row != null) {
            for (int iter = 0; iter < n; ++iter) {
                int slot = (p.IndexFire + iter) % n;
                Hotkey hk = row[slot];
                if (hk == null || hk.skill == null) continue;
                Skill_Info sk = Skill_Info.getSkillFromID(hk.skill.ID);
                if (sk == null) continue;
                if (sk.typeSkill == 2) continue; // Buff handled above
                if (Player.typeAutoFireMain == 2 && iter > 0) break; // Mode "Chiêu 1"
                
                if (p.getManaNeedUse((int) sk.manaLost) > p.Mp) continue;
                if (!DelaySkill.getDelay(sk.indexHotKey).isCoolDown()) continue;
                
                double dx = p.x - tgt.x;
                double dy = p.y - tgt.y;
                double dist = Math.sqrt(dx * dx + dy * dy);
                boolean doTeleport = dist > teleportMaxPixels;
                
                if (doTeleport && (gomSavedGlobal == null || !gomUseMapCenter)) {
                    int tx = (tgt.x / tileSize) * tileSize + tileSize / 2;
                    int ty = (tgt.y / tileSize) * tileSize + tileSize / 2;
                    int[] safePos = getSafePositionAwayFromPortals(tx, ty, p.x, p.y);
                    tx = safePos[0];
                    ty = safePos[1];
                    sendTeleport(p, tx, ty);
                    p.posTransRoad = null;
                    try { p.vx = 0; p.vy = 0; } catch (Throwable ignored) {}
                }
                
                try {
                    p.setActionHotKey(slot);
                    p.timeFristSkill = GameCanvas.timeNow;
                    p.IndexFire = (slot + 1) % n;
                    return;
                } catch (Throwable ex) { ex.printStackTrace(); }
            }
        }
    }

    private static boolean isAllowedTargetForAuto(Player p, MainObject t) {
        if (t == null) return false;
        if (t == p) return false;
        if (t.isRemove || t.isDie || t.Hp <= 0) return false;

        // Chỉ cho phép Quái (typeObject == 1) hoặc Người (typeObject == 0)
        if (t.typeObject != 1 && t.typeObject != 0) return false;

        // Nếu có nhắm mục tiêu cụ thể
        if (AThMadaraMOD.slaughterTargetName != null) {
            if (AThMadaraMOD.slaughterTargetTypeObject != -1) {
                if (t.typeObject != AThMadaraMOD.slaughterTargetTypeObject) return false;
            }
            if (t.typeObject == 1) {
                return t.name.equals(AThMadaraMOD.slaughterTargetName) && t.Lv == AThMadaraMOD.slaughterTargetLevel;
            } else {
                return t.name.equals(AThMadaraMOD.slaughterTargetName);
            }
        }

        // Lọc theo loại slaughterSelectType
        if (AThMadaraMOD.slaughterSelectType == 1) { // Chỉ Quái
            return t.typeObject == 1;
        } else if (AThMadaraMOD.slaughterSelectType == 2) { // Chỉ Người
            return t.typeObject == 0;
        } else { // Tất cả
            return t.typeObject == 1 || t.typeObject == 0;
        }
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

            // Dynamically scan and add new monsters from GameScreen.vecPlayers
            mVector vec = GameScreen.vecPlayers;
            if (vec != null) {
                for (int i = 0; i < vec.size(); ++i) {
                    Object o = vec.elementAt(i);
                    if (o instanceof MainMonster) {
                        MainMonster m = (MainMonster) o;
                        if (m == null || m.isRemove) continue;
                        if (!gomSavedGlobal.containsKey(m)) {
                            // New monster found! Save its original positions
                            try {
                                gomSavedGlobal.put(m, new int[]{m.x, m.y, m.toX, m.toY, m.toXNew, m.toYNew, m.vx, m.vy});
                            } catch (Throwable ignored) {}
                        }
                    }
                }
            }

            // Clean up any monsters that are dead or removed from gomSavedGlobal to avoid memory leaks
            java.util.Iterator<MainMonster> keyIt = gomSavedGlobal.keySet().iterator();
            while (keyIt.hasNext()) {
                MainMonster m = keyIt.next();
                if (m == null || m.isRemove || m.isDie) {
                    keyIt.remove();
                }
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

    public static void sendTeleport(Player p, int x, int y) {
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
   
    private static boolean reviceSentThisDeath = false;
    public static void autoReviceV1(Player p) {
        // Guard: chỉ chạy khi đang chết, chưa gửi revive lần này, không đang load map
        if (p == null || !p.isDie || AThMadaraMOD.autoReviveMode == 0) return;
        if (GameCanvas.loadMapScr != null && GameCanvas.currentScreen == GameCanvas.loadMapScr) return;
        if (reviceSentThisDeath) return;
        
        try {
            // Lưu map và vị trí trước khi chết (không bị overwrite sau khi hồi sinh)
            if (GameCanvas.loadmap != null && AThMadaraMOD.lastMapId <= 0) {
                AThMadaraMOD.lastMapId = GameCanvas.loadmap.idMapLoadMap;
            }
            if (GameScreen.player != null && AThMadaraMOD.lastX <= 0) {
                AThMadaraMOD.lastX = GameScreen.player.x;
                AThMadaraMOD.lastY = GameScreen.player.y;
            }
            // Lưu khu hiện tại để khôi phục sau
            try { AThMadaraFunc.saveCurrentZone(); } catch (Throwable ignored) {}
            
            // Nếu hồi sinh về làng (mode == 4), ta gửi gói hồi sinh miễn phí ngay lập tức
            if (AThMadaraMOD.autoReviveMode == 4) {
                if (AThMadaraMOD.isAutoReMap && !AThMadaraMOD.isDungeonMap(AThMadaraMOD.lastMapId)) {
                    AThMadaraMOD.shouldReturnToLastMap = true;
                } else {
                    AThMadaraMOD.shouldReturnToLastMap = false;
                }
                AThMadaraMOD.triedChangeMapOkOnce = false;
                AThMadaraMOD.nextReturnTryAt = 0;
                
                // Lưu trạng thái chế độ để khôi phục sau
                if (GameScreen.player != null) {
                    AThMadaraMOD.wasSlaughterActive = GameScreen.player.isAutoFireNew108;
                    AThMadaraMOD.wasTuDanhActive = AThMadaraMOD.isTuDanhActive;
                    GameScreen.player.isAutoFireNew108 = false;
                    AThMadaraMOD.isTuDanhActive = false;
                }
                
                reviceSentThisDeath = true;
                GlobalService.getInstance().Player_Revice((byte)0);
                Interface_Game.addInfoPlayerNormal("Tự hồi sinh về làng...", mFont.tahoma_7_yellow);
            } else {
                // Đối với hồi sinh tại chỗ (Beri/Ruby/Tất cả), ta đợi nhận Dialog từ server.
                // Thêm bộ đếm thời gian an toàn: Nếu sau 6 giây nằm chết mà chưa hồi sinh được, tự động gửi hồi sinh về làng.
                if (AThMadaraMOD.reviveFallbackTime == 0) {
                    AThMadaraMOD.reviveFallbackTime = GameCanvas.timeNow + 6000;
                    AThMadaraMOD.reviveFallbackDialogId = -1;
                    AThMadaraMOD.reviveFallbackTownIndex = -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Reset flag khi player sống lại (được gọi khi nhận HP > 0 từ server)
    public static void onPlayerRevived() {
        reviceSentThisDeath = false;
    }
   
    public static void autoSellItem(Player p) {
       
    }
    private static byte savedZone = -1;
    public static void saveCurrentZone() {
        if (GameCanvas.loadMapScr != null) {
            savedZone = GameCanvas.loadMapScr.area;
        }
    }

    public static void restoreSavedZoneIfAny() {
        if (savedZone >= 0 && savedZone <= 20) {
            try {
                GlobalService.getInstance().AB((byte)0, savedZone);
                Interface_Game.addInfoPlayerNormal("Đang khôi phục khu " + savedZone + "...", mFont.tahoma_7_yellow);
            } catch (Throwable ignored) {}
        }
    }
    
    // Kiểm tra xem player đã hồi sinh hoàn toàn chưa
    public static boolean isPlayerFullyRevived(Player p) {
        if (p == null) return false;
        return p.Hp > 0 && !p.isDie && GameCanvas.loadmap != null;
    }

    public static int[] getSafePositionAwayFromPortals(int targetX, int targetY, int playerX, int playerY) {
        int tx = targetX;
        int ty = targetY;
        if (LoadMap.vecPointChange != null) {
            for (int i = 0; i < LoadMap.vecPointChange.size(); i++) {
                Point pt = (Point) LoadMap.vecPointChange.elementAt(i);
                if (pt != null) {
                    int dist = MainObject.getDistance(tx, ty, pt.x, pt.y);
                    if (dist < 60) {
                        double dx = tx - pt.x;
                        double dy = ty - pt.y;
                        double len = Math.sqrt(dx * dx + dy * dy);
                        if (len > 0) {
                            tx = pt.x + (int)(dx / len * 65);
                            ty = pt.y + (int)(dy / len * 65);
                        } else {
                            tx += 65;
                        }
                    }
                }
            }
        }
        return new int[]{tx, ty};
    }
}
