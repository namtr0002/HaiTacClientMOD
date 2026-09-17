public class UIModalManager {
    public static boolean isModalActive = false;
    public static int modalX;
    public static int modalY;
    public static int modalW;
    public static int modalH;

    public static void setModal(int x, int y, int w, int h) {
        modalX = x;
        modalY = y;
        modalW = w;
        modalH = h;
        isModalActive = true;
    }

    public static void clearModal() {
        isModalActive = false;
    }

    public static boolean isPointInModal(int px, int py) {
        if (!isModalActive) return false;
        return px >= modalX && px < modalX + modalW && py >= modalY && py < modalY + modalH;
    }

    public static boolean swallowIfInModal() {
        if (!isModalActive) return false;
        if (GameCanvas.isPoint(modalX, modalY, modalW, modalH)) {
            return true;
        }
        return false;
    }
}
