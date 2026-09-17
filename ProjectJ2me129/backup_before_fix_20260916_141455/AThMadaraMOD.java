public class AThMadaraMOD {
    public Player myChar;
    public static final int MENU_AUTO_W = 11;
    public static final int MENU_AUTO_H = 14;
    public static final int MENU_AUTO_OFFSET_X = 3;
    public static final int MENU_AUTO_OFFSET_Y = -20;
    public static final int MENU_TOUCH_PAD = 4;
    private static AThMadaraMOD instance;
    public static mVector pendingDrops = new mVector();
    // Trong AThMadaraMOD
    public static boolean shouldReturnToLastMap = false;
    public static int lastMapId = -1;
    public static int lastX = -1;
    public static int lastY = -1;
    private static long nextReturnTryAt = 0;
    private static boolean triedChangeMapOkOnce = false;

    public static AThMadaraMOD getInstance() {
        if (instance == null) {
            instance = new AThMadaraMOD();
        }
        return instance;
    }
    private boolean isPointerInRect(int px, int py, int rx, int ry, int rw, int rh) {
        return px >= rx && px <= rx + rw && py >= ry && py <= ry + rh;
    }
    public boolean handlePointerReleased(int px, int py) {
        if (!GameCanvas.isTouch || GameCanvas.currentScreen != GameCanvas.gameScr) {
            return false;
        }
        if (GameCanvas.currentDialog != null || (GameCanvas.menuCur != null && GameCanvas.menuCur.isShowMenu) || Player.isGhost) {
            return false;
        }
        int padExtra = 16;
        int rx = Interface_Game.xAutoFire + MENU_AUTO_OFFSET_X - MENU_TOUCH_PAD - padExtra;
        int ry = Interface_Game.yAutoFire + MENU_AUTO_OFFSET_Y - MENU_TOUCH_PAD - padExtra;
        int rw = MENU_AUTO_W + (MENU_TOUCH_PAD * 2) + padExtra * 2;
        int rh = MENU_AUTO_H + (MENU_TOUCH_PAD * 2) + padExtra * 2;
        if (isPointerInRect(px, py, rx, ry, rw, rh)) {
            Interface_Game.addInfoPlayerNormal("MenuAuto TAP", mFont.tahoma_7_yellow);
            this.openMenuAuto();
            return true;
        }
        return false;
    }
    public void openMenuAuto() {
        try {
            mVector menu = new mVector();
            menu.addElement(new iCommand("Tàn Sát", 108, GameCanvas.gameScr));
            menu.addElement(new iCommand("Auto PK", 109, GameCanvas.gameScr));
            menu.addElement(new iCommand("Auto Keep Level", 110, GameCanvas.gameScr));
            menu.addElement(new iCommand("Đóng băng quái", 111, GameCanvas.gameScr));
            menu.addElement(new iCommand("Gom quái", 112, GameCanvas.gameScr));
            menu.addElement(new iCommand("Mod Skill", 113, GameCanvas.gameScr));
            menu.addElement(new iCommand("Tốc độ game", 114, GameCanvas.gameScr));
            menu.addElement(new iCommand("Auto Liên Tầng", 115, GameCanvas.gameScr));
            menu.addElement(new iCommand("Auto Săn Trùm(Zosaku)", 116, GameCanvas.gameScr));
            menu.addElement(new iCommand("Auto Liên Tầng(Zosaku)", 117, GameCanvas.gameScr));
            menu.addElement(new iCommand("Tự hồi sinh["+(Player.isAutoRevice ? "Bật" : "Tắt")+"]", 118, GameCanvas.gameScr));
            menu.addElement(new iCommand("Tự động bán đồ", 119, GameCanvas.gameScr));
            GameCanvas.menu.startAt(menu, 2, "Menu Auto");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void openMenuSellItem() {
        try {
            MsgAutoSell dlg = new MsgAutoSell();
            dlg.init(); // khởi tạo (theo style MsgAutoSell)
            GameCanvas.subDialog = dlg; // show dialog (cùng kiểu với input/dialogs khác)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // trong class AThMadaraMOD (thêm các biến/hàm sau)
    public static mVector autoSellList = new mVector("AutoSellList"); // chứa short[]{typeObject, ID}

    // filter mode: 0 = all, 1 = only white (colorName==0), 2 = white+green (<=1)
    public static int AUTOSELL_FILTER = 0;

    // kiểm tra xem MainItem đã trong danh sách auto-sell chưa
    public static boolean isInAutoSell(MainItem mi) {
        if (mi == null) return false;
        for (int i = 0; i < autoSellList.size(); ++i) {
            short[] s = (short[]) autoSellList.elementAt(i);
            if (s != null && s[0] == (short)mi.typeObject && s[1] == (short)mi.ID) return true;
        }
        return false;
    }

    // toggle vật phẩm: nếu chưa có => add, có rồi => remove
    public static void toggleAutoSell(MainItem mi) {
        if (mi == null) return;
        for (int i = 0; i < autoSellList.size(); ++i) {
            short[] s = (short[]) autoSellList.elementAt(i);
            if (s != null && s[0] == (short)mi.typeObject && s[1] == (short)mi.ID) {
                autoSellList.removeElement(i);
                Interface_Game.addInfoPlayerNormal("Đã bỏ khỏi danh sách auto-sell: " + mi.name, mFont.tahoma_7_yellow);
                return;
            }
        }
        short[] entry = new short[2];
        entry[0] = (short) mi.typeObject;
        entry[1] = (short) mi.ID;
        autoSellList.addElement(entry);
        Interface_Game.addInfoPlayerNormal("Đã thêm vào danh sách auto-sell: " + mi.name, mFont.tahoma_7_yellow);
    }

    // xóa toàn bộ danh sách
    public static void clearAutoSellList() {
        autoSellList.removeAllElements();
        Interface_Game.addInfoPlayerNormal("Xóa toàn bộ danh sách auto-sell", mFont.tahoma_7_yellow);
    }

    // bỏ 1 item theo index danh sách autoSellList
    public static void removeAutoSellAt(int idx) {
        if (idx >= 0 && idx < autoSellList.size()) {
            short[] s = (short[]) autoSellList.elementAt(idx);
            autoSellList.removeElement(idx);
            Interface_Game.addInfoPlayerNormal("Đã xóa mục auto-sell", mFont.tahoma_7_yellow);
        }
    }
    
    // làm sạch cache: xóa các item không còn trong inventory
    public static void cleanAutoSellCache() {
        if (Player.vecInventory == null) return;
        
        mVector toRemove = new mVector();
        for (int i = 0; i < autoSellList.size(); ++i) {
            short[] s = (short[]) autoSellList.elementAt(i);
            if (s == null) {
                toRemove.addElement(i);
                continue;
            }
            
            // Kiểm tra xem item này còn trong inventory không
            boolean found = false;
            for (int j = 0; j < Player.vecInventory.size(); ++j) {
                MainItem mi = (MainItem) Player.vecInventory.elementAt(j);
                if (mi != null && mi.typeObject == s[0] && mi.ID == s[1]) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                toRemove.addElement(i);
            }
        }
        
        // Xóa các item không còn trong inventory
        for (int i = toRemove.size() - 1; i >= 0; --i) {
            int idx = (Integer) toRemove.elementAt(i);
            autoSellList.removeElement(idx);
        }
        
        if (toRemove.size() > 0) {
            Interface_Game.addInfoPlayerNormal("Đã làm sạch " + toRemove.size() + " item không còn trong túi", mFont.tahoma_7_yellow);
        }
    }
    
    public static void autoProcessItemsl() {
        if (Player.vecInventory == null || GameCanvas.gameTick % 20 != 0) return;
        for (int i = Player.vecInventory.size() - 1; i >= 0; i--) {
            MainItem mi = (MainItem) Player.vecInventory.elementAt(i);
            if (mi != null && isInAutoSell(mi)) {
                GlobalService.getInstance().Get_Item_Map(mi.ID, mi.typeObject);
                //Interface_Game.addInfoPlayerNormal("Tự động vứt: " + mi.name, mFont.tahoma_7_yellow);
            }
        }
    }

    // helper kiểm tra pending
    private static boolean isPending(MainItem mi) {
        if (mi == null) return false;
        for (int p = 0; p < pendingDrops.size(); ++p) {
            short[] s = (short[]) pendingDrops.elementAt(p);
            if (s != null && s[0] == (short)mi.typeObject && s[1] == (short)mi.ID) return true;
        }
        return false;
    }
    private static void addPending(MainItem mi) {
        if (mi == null) return;
        if (isPending(mi)) return;
        short[] entry = new short[2];
        entry[0] = (short) mi.typeObject;
        entry[1] = (short) mi.ID;
        pendingDrops.addElement(entry);
    }
    private static void removePendingIfNotExistsInInventory() {
        if (Player.vecInventory == null) return;
        mVector toRemove = new mVector();
        for (int i = 0; i < pendingDrops.size(); ++i) {
            short[] s = (short[]) pendingDrops.elementAt(i);
            boolean found = false;
            for (int j = 0; j < Player.vecInventory.size(); ++j) {
                MainItem mi = (MainItem) Player.vecInventory.elementAt(j);
                if (mi != null && mi.typeObject == s[0] && mi.ID == s[1]) {
                    found = true;
                    break;
                }
            }
            if (!found) toRemove.addElement(new Integer(i));
        }
        for (int k = toRemove.size() - 1; k >= 0; --k) {
            int idx = ((Integer) toRemove.elementAt(k)).intValue();
            if (idx >= 0 && idx < pendingDrops.size()) pendingDrops.removeElement(idx);
        }
        if (toRemove.size() > 0) {
            Interface_Game.addInfoPlayerNormal("Đã cập nhật trạng thái AutoDrop", mFont.tahoma_7_yellow);
        }
    }

    // sửa autoProcessItems
    public static void autoProcessItems() {
        if (Player.vecInventory == null || GameCanvas.gameTick % 20 != 0) return;

        // chỉ chạy khi bật AutoDrop
        if (!Player.isAutoFilterItems) return;

        // dọn các pending đã mất khỏi inventory
        removePendingIfNotExistsInInventory();

        for (int i = Player.vecInventory.size() - 1; i >= 0; --i) {
            MainItem mi = (MainItem) Player.vecInventory.elementAt(i);
            if (mi != null && isInAutoSell(mi)) {
                if (isPending(mi)) continue; // đã gửi rồi, chờ server
                if (mi.numPotion <= 0) continue;
                GlobalService.getInstance().item_drop((byte) i, (short) mi.numPotion);
                addPending(mi); // đánh dấu chờ xử lý
                Interface_Game.addInfoPlayerNormal("Tự động vứt: " + mi.name, mFont.tahoma_7_yellow);
            }
        }
    }
    // Trong AThMadaraMOD
    public static void returnToLastMap() {
        if (lastMapId <= 0) {
            Interface_Game.addInfoPlayerNormal("Không có map cũ để quay lại.", mFont.tahoma_7_yellow);
            shouldReturnToLastMap = false;
            return;
        }
        try {
            // Nếu chưa ở đúng map → thử 2 cách: 1) ép changeMapOk 1 lần; 2) NextMap nhịp nhàng.
            if (GameCanvas.loadmap == null || GameCanvas.loadmap.idMapLoadMap != lastMapId) {
                if (!triedChangeMapOkOnce && GameCanvas.loadmap != null) {
                    try {
                        GameCanvas.loadmap.idMapLoadMap = lastMapId;
                        GlobalService.getInstance().changeMapOk();
                        Interface_Game.addInfoPlayerNormal("Yêu cầu đổi map (OK) về map cũ...", mFont.tahoma_7_yellow);
                    } catch (Throwable ignored) {}
                    triedChangeMapOkOnce = true;
                    nextReturnTryAt = GameCanvas.timeNow + 500;
                    return;
                }
                if (GameCanvas.timeNow >= nextReturnTryAt) {
                    GlobalService.getInstance().NextMap((short) lastMapId);
                    Interface_Game.addInfoPlayerNormal("Đang chuyển về map cũ...", mFont.tahoma_7_yellow);
                    nextReturnTryAt = GameCanvas.timeNow + 500; // 0.5s
                }
                return; // giữ cờ để thử tiếp các tick sau (KHÔNG xoá cờ)
            }

            // Đã đúng map → khôi phục khu (zone) nếu có
            try {
                AThMadaraFunc.restoreSavedZoneIfAny();
            } catch (Throwable ignored) {}

            // Đã tới đúng map → đưa về toạ độ cũ nếu hợp lệ
            if (lastX > 0 && lastY > 0) {
                try {
                    GlobalService.getInstance().Obj_Move((short) lastX, (short) lastY);
                    if (GameScreen.player != null) {
                        GameScreen.player.x = lastX;
                        GameScreen.player.y = lastY;
                        GameScreen.player.CX = lastX;
                        GameScreen.player.CY = lastY;
                        GameScreen.player.isSendMove = true;
                    }
                } catch (Throwable ignored) {}
            }

            Interface_Game.addInfoPlayerNormal("Đã quay lại vị trí cũ.", mFont.tahoma_7_yellow);
            // Hoàn tất thành công, xoá cờ
            shouldReturnToLastMap = false;
            lastMapId = -1;
            lastX = -1;
            lastY = -1;
            nextReturnTryAt = 0;
            triedChangeMapOkOnce = false;
        } catch (Exception e) {
            Interface_Game.addInfoPlayerNormal("Lỗi khi quay lại map cũ.", mFont.tahoma_7_yellow);
            e.printStackTrace();
        }
    }
}
