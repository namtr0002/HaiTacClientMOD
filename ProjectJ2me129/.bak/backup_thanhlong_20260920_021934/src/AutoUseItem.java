public class AutoUseItem {
    public static boolean isAutoUse = false;
    public static short autoItemId = -1;
    public static byte autoItemCat = 4;
    public static String autoItemName = "";
    public static int autoUseCount = 0;
    public static int autoUseTotal = 0;
    public static int targetBagCount = 0;
    public static int lastBagCount = 0;
    public static int failedAttempts = 0;
    public static long lastUseTime = 0;

    public static MainItem curItem = null;
    public static short curId = -1;
    public static byte curCat = 4;
    public static long timeStartAuto = 0;

    public static void startAutoUse(MainItem item, int count) {
        if (item == null || count <= 0) {
            return;
        }
        isAutoUse = true;
        autoItemId = item.ID;
        autoItemCat = (byte) item.typeObject;
        autoItemName = item.name;
        autoUseCount = count;
        autoUseTotal = count;
        int currentInBag = item.numPotion;
        targetBagCount = Math.max(0, currentInBag - count);
        lastBagCount = currentInBag;
        failedAttempts = 0;
        lastUseTime = 0;
        timeStartAuto = System.currentTimeMillis();

        Interface_Game.addInfoPlayerNormal("Bắt đầu tự dùng " + autoItemName + " (x" + count + ")", mFont.tahoma_7_yellow);
    }

    public static void checkStopOnUserInput(String reason) {
        // Do not stop auto-use on general user input (touch, key press, movement).
        // Auto-use should only stop when completed, out of items, dead, disconnected, or manually stopped by user.
    }

    public static void stopAutoUse(String reason) {
        if (!isAutoUse) {
            return;
        }
        isAutoUse = false;
        autoItemId = -1;
        autoUseCount = 0;
        targetBagCount = 0;
        lastBagCount = 0;
        failedAttempts = 0;
        curItem = null;
        curId = -1;

        if (reason != null && reason.length() > 0) {
            Interface_Game.addInfoPlayerNormal("Dừng tự dùng: " + reason, mFont.tahoma_7_white);
        }
    }

    public static void update() {
        if (!isAutoUse) {
            return;
        }

        if (GameScreen.player == null || GameScreen.player.Hp <= 0 || GameScreen.player.isDie) {
            stopAutoUse("Nhân vật tử trận");
            return;
        }

        if (GameCanvas.currentScreen == GameCanvas.loginScr || GameCanvas.currentScreen == GameCanvas.CG || (GameCanvas.loadMapScr != null && GameCanvas.currentScreen == GameCanvas.loadMapScr)) {
            stopAutoUse("Chuyển màn hình");
            return;
        }

        if (Session_ME.getInstance() == null || !Session_ME.getInstance().AB()) {
            stopAutoUse("Mất kết nối");
            return;
        }

        long now = System.currentTimeMillis();

        MainItem invItem = MainItem.getItemVec(autoItemCat, autoItemId, Player.vecInventory);
        if (invItem == null || invItem.numPotion <= 0) {
            stopAutoUse("Đã hết vật phẩm trong hành trang");
            return;
        }

        if (invItem.numPotion <= targetBagCount) {
            stopAutoUse("Hoàn thành tự dùng");
            return;
        }

        if (invItem.typeObject == 4 && invItem instanceof Potion) {
            Potion potion = (Potion) invItem;
            if (potion.indexHotKey >= 0) {
                DelaySkill dSkill = DelaySkill.getDelay(potion.indexHotKey);
                if (dSkill != null && !dSkill.isCoolDown()) {
                    return;
                }
            }
            if (potion.BQ == 1 && GameScreen.player.Hp >= GameScreen.player.maxHp) {
                stopAutoUse("HP đã đầy");
                return;
            }
            if (potion.BQ == 2 && GameScreen.player.Mp >= GameScreen.player.maxMp) {
                stopAutoUse("MP đã đầy");
                return;
            }
        }

        int delay = 350;
        if (now - lastUseTime < delay) {
            return;
        }

        if (lastUseTime > 0) {
            if (invItem.numPotion >= lastBagCount) {
                failedAttempts++;
                if (failedAttempts >= 5) {
                    stopAutoUse("Vật phẩm không thể sử dụng");
                    return;
                }
            } else {
                failedAttempts = 0;
                lastBagCount = invItem.numPotion;
            }
        }

        lastUseTime = now;
        invItem.Use_Item();
    }
}