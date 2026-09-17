public class MenuMeScr extends MainScreen {
    private static MenuMeScr instance;

    public static MenuMeScr gI() {
        if (instance == null) {
            instance = new MenuMeScr();
        }
        return instance;
    }

    public void performAction(int actionType, MainScreen backScr) {
        if (backScr == null) {
            backScr = (MainScreen)GameCanvas.gameScr;
        }
        if (GameCanvas.isCompactMode()) {
            GameCanvas.tabInven.AA((byte)0);
            GameCanvas.tabAllScr.Show(backScr);
            if (GameCanvas.currentScreen == GameCanvas.tabAllScr) {
                switch (actionType) {
                    case 0: // Hanh trang
                        GameCanvas.tabAllScr.idSelect = 1;
                        break;
                    case 1: // Trang bi
                        GameCanvas.tabAllScr.idSelect = 0;
                        break;
                    case 2: // Tiem nang
                        GameCanvas.tabAllScr.idSelect = 2;
                        break;
                    case 3: // Ky nang
                        GameCanvas.tabAllScr.idSelect = 4;
                        break;
                    case 4: // Nhiem vu
                        GameCanvas.tabAllScr.idSelect = 3;
                        break;
                    default:
                        if (GameCanvas.tabAllScr.AB != null && GameCanvas.tabAllScr.AB.size() == 6) {
                            GameCanvas.tabAllScr.idSelect = 5;
                        } else {
                            GameCanvas.tabAllScr.idSelect = 0;
                        }
                        break;
                }
                GameCanvas.tabAllScr.setTabSelect();
                if (GameCanvas.tabAllScr.AC != null) {
                    GameCanvas.tabAllScr.AC.AB();
                }
            }
            return;
        }

        switch (actionType) {
            case 0: // Hnh trang
                DualTabScreen.gI().curMainTab = 0;
                DualTabScreen.gI().focusPane = 1;
                DualTabScreen.gI().Show(backScr);
                break;
            case 1: // Trang b?
                DualTabScreen.gI().curMainTab = 0;
                DualTabScreen.gI().focusPane = 0;
                DualTabScreen.gI().Show(backScr);
                break;
            case 2: // Ti?m n?ng & Thng tin
                DualTabScreen.gI().curMainTab = 1;
                DualTabScreen.gI().Show(backScr);
                break;
            case 3: // K? n?ng
                DualTabScreen.gI().curMainTab = 2;
                DualTabScreen.gI().Show(backScr);
                break;
            case 4: // Nhi?m v?
                DualTabScreen.gI().curMainTab = 3;
                DualTabScreen.gI().Show(backScr);
                break;
            case 5: // Th c?ng
                DualTabScreen.gI().curMainTab = 4;
                DualTabScreen.gI().openPetSubView();
                DualTabScreen.gI().Show(backScr);
                break;
            case 6: // Danh hi?u
                DualTabScreen.gI().curMainTab = 4;
                DualTabScreen.gI().openDanhHieuSubView();
                DualTabScreen.gI().Show(backScr);
                break;
            case 7: // Ch?c n?ng
            default:
                DualTabScreen.gI().curMainTab = 4;
                DualTabScreen.gI().subViewMode = 0;
                DualTabScreen.gI().Show(backScr);
                break;
        }
    }

    public void performAction(int actionType) {
        this.performAction(actionType, (MainScreen)GameCanvas.gameScr);
    }
}
