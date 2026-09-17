
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
    public static long lastUserInteractionTime = 0L;

    public static boolean isUserInteracting(Player p) {
        boolean interacting = false;
        if (GameCanvas.isPointerDown || GameCanvas.isPointerSelect) {
            interacting = true;
        }
        if (GameCanvas.isKeyPressed(0) || GameCanvas.isKeyPressed(1) || GameCanvas.isKeyPressed(2) || GameCanvas.isKeyPressed(3)) {
            interacting = true;
        }
        if (GameCanvas.AL != null) {
            if (GameCanvas.AL[2] || GameCanvas.AL[4] || GameCanvas.AL[6] || GameCanvas.AL[8]
                || GameCanvas.AL[22] || GameCanvas.AL[24] || GameCanvas.AL[26] || GameCanvas.AL[28]) {
                interacting = true;
            }
        }
        if (p != null && (p.posTransRoad != null || p.Action == 1)) {
            interacting = true;
        }

        if (interacting) {
            lastUserInteractionTime = GameCanvas.timeNow;
            return true;
        }
        return (GameCanvas.timeNow - lastUserInteractionTime < 2500L);
    }

    public static boolean isSkillEnabledInAuto(short skillId) {
        if (MsgAutoFire.value != null && MsgAutoFire.value.length > 0) {
            for (int i = 0; i < MsgAutoFire.value.length; i++) {
                if (MsgAutoFire.value[i][0] == skillId) {
                    return MsgAutoFire.value[i][1] == 1;
                }
            }
        }
        return true;
    }

    public static boolean isSkillUsable(Player p, Skill_Info sk, boolean isAttackSkill) {
        if (sk == null) return false;
        if (sk.Lv_RQ < 0) return false;
        if (GameCanvas.loadmap != null && GameCanvas.loadmap.mapLang()) return false;
        if (sk.typeSkill == 3 || sk.typeSkill == 6) return false;

        boolean isSeaMap = (LoadMap.specMap == 4);
        if (isSeaMap) {
            if (sk.typeSkill == 1) return false;
        } else {
            if (sk.typeSkill == 4) return false;
        }

        if (isAttackSkill) {
            if (isSeaMap) {
                if (sk.typeSkill != 4 && sk.typeSkill != 0) return false;
            } else {
                if (sk.typeSkill != 1 && sk.typeSkill != 0) return false;
            }
        } else {
            if (sk.typeSkill != 2) return false;
            if (sk.typeBuff == 3) {
                if (p == null || GameScreen.objFocus == null || !p.AC(GameScreen.objFocus)) {
                    return false;
                }
            }
        }

        if (!isSkillEnabledInAuto(sk.ID)) return false;

        DelaySkill d = DelaySkill.getDelay(sk.indexHotKey);
        if (d != null && !d.isCoolDown()) return false;

        if (p != null && p.getManaNeedUse((int) sk.manaLost) > p.Mp) return false;

        if (p != null) {
            if (p.Hp <= 0 || p.isDie || p.Action == 4) return false;
            if (p.vecEffspec != null) {
                for (int i = 0; i < p.vecEffspec.size(); i++) {
                    Class_BR eff = (Class_BR) p.vecEffspec.elementAt(i);
                    if (eff != null && (eff.typeEffect == 1 || eff.typeEffect == 5)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void autoFireMoveToTarget(Player p) {
        if (p == null || p.Hp <= 0 || p.isDie || p.Action == 4) return;
        if (GameCanvas.loadmap != null && GameCanvas.loadmap.mapLang()) return;
        if (p.skillCurrent != null) return;

        boolean isAutoActive = p.isAutoFireNew108 || (Player.AutoFireCur > 0 && Interface_Game.isAutoFireInterface);
        if (!isAutoActive) return;

        try {
            if (Player.typeAutoBuff == 1 && MsgAutoFire.value != null && !isUserInteracting(p)) {
                if (GameCanvas.gameTick % 5 == 1) {
                    for (int i = 0; i < MsgAutoFire.value.length; i++) {
                        short id = MsgAutoFire.value[i][0];
                        short enabled = MsgAutoFire.value[i][1];
                        if (enabled != 1) continue;
                        Skill_Info sk = Skill_Info.getSkillFromID(id);
                        if (sk == null) continue;
                        if (!isSkillUsable(p, sk, false)) continue;
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

        MainObject tgt = GameScreen.objFocus;
        if (tgt == null || !isAllowedTargetForAuto(p, tgt) || tgt.isRemove || tgt.Hp <= 0 || tgt.isDie) {
            GameScreen.objFocus = null;
            int best = Integer.MAX_VALUE;
            MainObject bestObj = null;
            if (GameScreen.vecPlayers != null) {
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
                try {
                    GameScreen.addEffectEnd_ObjTo((short)24, 0, tgt.x, tgt.y, (short)tgt.ID, (byte)tgt.typeObject, (byte)0, (MainObject)null);
                    Interface_Game.isPaintInfoFocus = true;
                } catch (Throwable ignored) {}
            } else {
                return;
            }
        }

        Hotkey[] row = p.hotkeyPlayer != null ? p.hotkeyPlayer[p.currentTab] : null;
        int n = (row != null) ? row.length : 0;
        if (n > 0) {
            if (p.IndexFire < 0 || p.IndexFire >= n) p.IndexFire = 0;
        }

        int tileSize = GameCanvas.loadmap != null && GameCanvas.loadmap.wTile > 0
                       ? GameCanvas.loadmap.wTile
                       : (LoadMap.wTile > 0 ? LoadMap.wTile : 24);
        boolean hasFired = false;

        if (row != null && n > 0) {
            for (int iter = 0; iter < n; ++iter) {
                int slot = (p.IndexFire + iter) % n;
                Hotkey hk = row[slot];
                if (hk == null) continue;

                if (hk.skill != null) {
                    Skill_Info sk = Skill_Info.getSkillFromID(hk.skill.ID);
                    if (sk == null) continue;

                    if (sk.typeSkill == 2) {
                        if (!isSkillUsable(p, sk, false)) continue;
                        try {
                            boolean started = p.beginPlayerFire(sk);
                            if (started) {
                                p.timeFristSkill = GameCanvas.timeNow;
                                p.IndexFire = (slot + 1) % n;
                                hasFired = true;
                                break;
                            }
                        } catch (Throwable ex) { ex.printStackTrace(); }
                        continue;
                    }

                    if (!isSkillUsable(p, sk, true)) continue;

                    int skillRange = sk.range > 0 ? (int) sk.range : 60;
                    double dx = p.x - tgt.x;
                    double dy = p.y - tgt.y;
                    double dist = Math.sqrt(dx * dx + dy * dy);

                    if (dist > skillRange) {
                        if (AThMadaraMOD.autoCombatMoveMode == 0) {
                            int tx = (tgt.x / tileSize) * tileSize + tileSize / 2;
                            int ty = (tgt.y / tileSize) * tileSize + tileSize / 2;
                            sendTeleport(p, tx, ty);
                            p.posTransRoad = null;
                            try { p.vx = 0; p.vy = 0; } catch (Throwable ignored) {}
                        } else {
                            int targetTileX = tgt.x / tileSize;
                            int targetTileY = tgt.y / tileSize;
                            int playerTileX = p.x / tileSize;
                            int playerTileY = p.y / tileSize;
                            if (p.posTransRoad == null && GameCanvas.loadmap != null) {
                                p.posTransRoad = GameCanvas.loadmap.AA(targetTileX, targetTileY, playerTileX, playerTileY, 30, p);
                            }
                            if (p.posTransRoad == null) {
                                p.toX = tgt.x;
                                p.toY = tgt.y;
                            }
                            return;
                        }
                    }

                    try {
                        boolean started = p.beginPlayerFire(sk);
                        if (started) {
                            p.timeFristSkill = GameCanvas.timeNow;
                            p.IndexFire = (slot + 1) % n;
                            hasFired = true;
                            break;
                        }
                    } catch (Throwable ex) { ex.printStackTrace(); }
                }
            }
        }

        // Fallback: Neu cac o hotkey trong hoac chua san sang / bi chan, tu dung skill co ban danh quai
        if (!hasFired && Player.vecListSkill != null && Player.vecListSkill.size() > 0) {
            for (int k = 0; k < Player.vecListSkill.size(); k++) {
                Skill_Info skNorm = (Skill_Info) Player.vecListSkill.elementAt(k);
                if (skNorm == null) continue;
                if (!isSkillUsable(p, skNorm, true)) continue;

                int skillRange = skNorm.range > 0 ? (int) skNorm.range : 60;
                double dx = p.x - tgt.x;
                double dy = p.y - tgt.y;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist > skillRange) {
                    if (AThMadaraMOD.autoCombatMoveMode == 0) {
                        int tx = (tgt.x / tileSize) * tileSize + tileSize / 2;
                        int ty = (tgt.y / tileSize) * tileSize + tileSize / 2;
                        sendTeleport(p, tx, ty);
                        p.posTransRoad = null;
                        try { p.vx = 0; p.vy = 0; } catch (Throwable ignored) {}
                    } else {
                        int targetTileX = tgt.x / tileSize;
                        int targetTileY = tgt.y / tileSize;
                        int playerTileX = p.x / tileSize;
                        int playerTileY = p.y / tileSize;
                        if (p.posTransRoad == null && GameCanvas.loadmap != null) {
                            p.posTransRoad = GameCanvas.loadmap.AA(targetTileX, targetTileY, playerTileX, playerTileY, 30, p);
                        }
                        if (p.posTransRoad == null) {
                            p.toX = tgt.x;
                            p.toY = tgt.y;
                        }
                        return;
                    }
                }

                try {
                    boolean started = p.beginPlayerFire(skNorm);
                    if (started) {
                        p.timeFristSkill = GameCanvas.timeNow;
                        hasFired = true;
                        break;
                    }
                } catch (Throwable ignored) {}
            }
        }
    }

    public static void updateAutoPotion(Player p) {
        if (p == null || p.Hp <= 0 || p.isDie) return;
        if (p.maxHp > 0 && (p.Hp * 100 / p.maxHp < 35 || (p.maxMp > 0 && p.Mp * 100 / p.maxMp < 20))) {
            if (GameCanvas.gameTick % 10 == 0 && Player.vecInventory != null) {
                for (int inv = 0; inv < Player.vecInventory.size(); inv++) {
                    MainItem mi = (MainItem) Player.vecInventory.elementAt(inv);
                    if (mi != null && mi.typeObject == 3) {
                        GlobalService.getInstance().Use_Potion((short) mi.ID);
                        break;
                    }
                }
            }
        }
    }

    public static void updateHutItem(Player p) {
        if (p == null || p.isDie || p.Hp <= 0) return;
        if (GameCanvas.gameTick % 6 != 0) return;
        int maxDist = AThMadaraMOD.isHutItem ? 1000 : 140;

        if (GameScreen.vecPlayers != null) {
            for (int i = 0; i < GameScreen.vecPlayers.size(); i++) {
                MainObject obj = (MainObject) GameScreen.vecPlayers.elementAt(i);
                if (obj != null && !obj.isRemove) {
                    if (obj.typeObject == 3 || obj.typeObject == 4 || obj.typeObject == 5 || obj.typeObject == 7) {
                        int d = MainObject.getDistance(p.x, p.y, obj.x, obj.y);
                        if (d <= maxDist) {
                            GlobalService.getInstance().Get_Item_Map(obj.ID, obj.typeObject);
                            break;
                        }
                    }
                }
            }
        }

        if (LoadMap.mItemMap != null && LoadMap.mItemMap.length > 3 && LoadMap.mItemMap[3] != null && LoadMap.mItemMap[3].size() > 0) {
            for (int im = 0; im < LoadMap.mItemMap[3].size(); im++) {
                MainItemMap item = (MainItemMap) LoadMap.mItemMap[3].elementAt(im);
                if (item != null && !item.isRemove) {
                    int distItem = MainObject.getDistance(p.x, p.y, item.x, item.y);
                    if (distItem <= maxDist) {
                        GlobalService.getInstance().Get_Item_Map(item.IDItem, item.AC);
                        break;
                    }
                }
            }
        }
    }

    // Chỉ đánh: quái/boss (typeObject==1), còn sống, có thể tấn công và không phải trong làng
    private static boolean isAllowedTargetForAuto(Player p, MainObject t) {
        if (t == null) return false;
        if (GameCanvas.loadmap != null && GameCanvas.loadmap.mapLang()) return false;
        if (t.typeObject == 1 && !t.isDie && t.Hp > 0 && !t.isRemove) {
            if (p != null && !p.AC(t)) return false;
            if (AThMadaraMOD.isSlaughterActive && AThMadaraMOD.slaughterTargetName != null) {
                if (!AThMadaraMOD.slaughterTargetName.equals(t.name)) return false;
                if (AThMadaraMOD.slaughterTargetLevel > 0 && t.Lv != AThMadaraMOD.slaughterTargetLevel) return false;
            }
            return true;
        }
        return false;
    }

    public static java.util.Map<MainMonster,int[]> startGomAllFollow(Player p, int offsetX, int offsetY, boolean follow, boolean useMapCenter) {
        java.util.Map<MainMonster,int[]> saved = new java.util.HashMap<>();
        if (p == null) return saved;

        try {
            mVector vec = GameScreen.vecPlayers;
            if (vec == null) return saved;

            int targetX = p.x + offsetX;
            int targetY = p.y + offsetY;
            gomFixedX = targetX; gomFixedY = targetY;
            gomUseMapCenter = useMapCenter;

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

            if (GameScreen.vecPlayers != null) {
                for (int i = 0; i < GameScreen.vecPlayers.size(); ++i) {
                    Object o = GameScreen.vecPlayers.elementAt(i);
                    if (o instanceof MainMonster) {
                        MainMonster m = (MainMonster) o;
                        if (m == null || m.isRemove || m.isDie || m.Hp <= 0) continue;
                        if (!gomSavedGlobal.containsKey(m)) {
                            try {
                                gomSavedGlobal.put(m, new int[]{m.x, m.y, m.toX, m.toY, m.toXNew, m.toYNew, m.vx, m.vy});
                            } catch (Throwable ignored) {}
                        }
                        m.gomX = targetX; m.gomY = targetY;
                        m.x = targetX; m.y = targetY;
                        m.toX = targetX; m.toY = targetY;
                        m.toXNew = targetX; m.toYNew = targetY;
                        m.vx = 0; m.vy = 0;
                        m.isGom = true;
                    }
                }
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
            // Vẽ tên HaiTacZet ở giữa màn hình bên trên cùng
            if (AThMadaraMOD.isShowTopTitle) {
                mFont.tahoma_7b_black.drawString(g, "HaiTacZet", MotherCanvas.hw + 1, 3, 2);
                mFont.tahoma_7b_yellow.drawString(g, "HaiTacZet", MotherCanvas.hw, 2, 2);
            }

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

            int textY = chatY + imgH + 2;
            int textW = mFont.tahoma_7_white.getWidth(timeStr);
            int drawX = chatX + (imgW / 2) - (textW / 2);
            if (drawX < 2) drawX = 2;
            if (drawX + textW > MotherCanvas.w - 2) {
                drawX = MotherCanvas.w - 2 - textW;
            }
            mFont.tahoma_7_white.drawString(g, timeStr, drawX, textY, 0);

            // Hiển thị FPS và Tốc độ game ngay dưới thời gian
            String fpsSpeedStr = "FPS: " + MotherCanvas.currentFPS + " | Speed: x" + AThMadaraMOD.gameSpeed;
            int textW2 = mFont.tahoma_7_yellow.getWidth(fpsSpeedStr);
            int drawX2 = chatX + (imgW / 2) - (textW2 / 2);
            if (drawX2 < 2) drawX2 = 2;
            if (drawX2 + textW2 > MotherCanvas.w - 2) {
                drawX2 = MotherCanvas.w - 2 - textW2;
            }
            mFont.tahoma_7_yellow.drawString(g, fpsSpeedStr, drawX2, textY + 11, 0);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
   
    public static void autoSellItem(Player p) {
        //để đây code sau
    }

    public static void autoReviceV1(Player p) {
        if (p == null || !p.isDie) return;
        if (AThMadaraMOD.autoReviveMode == 0 && !Player.isAutoRevice) return;
        try {
            if (GameCanvas.loadmap != null) {
                AThMadaraMOD.lastMapId = GameCanvas.loadmap.idMapLoadMap;
            }
            if (GameScreen.player != null) {
                AThMadaraMOD.lastX = GameScreen.player.x;
                AThMadaraMOD.lastY = GameScreen.player.y;
            }
            try {
                AThMadaraFunc.saveCurrentZone();
            } catch (Throwable ignored) {}

            int mode = AThMadaraMOD.autoReviveMode;
            if (mode == 0 && Player.isAutoRevice) {
                mode = 4;
            }

            if (mode == 4) {
                GlobalService.getInstance().Player_Revice((byte)0);
                if (AThMadaraMOD.isAutoReMap || p.isAutoReMap) {
                    AThMadaraMOD.shouldReturnToLastMap = true;
                    Interface_Game.addInfoPlayerNormal("Đã hồi sinh về làng. Sẽ quay lại map cũ...", mFont.tahoma_7_yellow);
                } else {
                    AThMadaraMOD.shouldReturnToLastMap = false;
                    Interface_Game.addInfoPlayerNormal("Đã hồi sinh về làng.", mFont.tahoma_7_yellow);
                }
            } else if (mode == 1 || mode == 2) {
                GlobalService.getInstance().Player_Revice((byte)1);
                AThMadaraMOD.shouldReturnToLastMap = false;
                Interface_Game.addInfoPlayerNormal("Đã hồi sinh tại chỗ.", mFont.tahoma_7_yellow);
            } else if (mode == 3) {
                if (AThMadaraMOD.reviveFallbackTime == 0) {
                    AThMadaraMOD.reviveFallbackTime = GameCanvas.timeNow;
                    GlobalService.getInstance().Player_Revice((byte)1);
                    AThMadaraMOD.shouldReturnToLastMap = false;
                    Interface_Game.addInfoPlayerNormal("Đang hồi sinh tại chỗ...", mFont.tahoma_7_yellow);
                } else if (GameCanvas.timeNow - AThMadaraMOD.reviveFallbackTime > 4000) {
                    GlobalService.getInstance().Player_Revice((byte)0);
                    AThMadaraMOD.reviveFallbackTime = 0;
                    if (AThMadaraMOD.isAutoReMap || p.isAutoReMap) {
                        AThMadaraMOD.shouldReturnToLastMap = true;
                        Interface_Game.addInfoPlayerNormal("Hồi sinh tại chỗ thất bại, về làng & quay lại map...", mFont.tahoma_7_yellow);
                    }
                }
            }
            GameCanvas.end_Dialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static boolean isPlayerFullyRevived(Player p) {
        if (p == null) return false;
        return p.Hp > 0 && !p.isDie && GameCanvas.loadmap != null;
    }
}
