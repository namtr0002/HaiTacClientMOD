using System;

public class DualTabScreen : MainScreen {
    public static int SLOTS_PER_TAB = 126;
    public int curInvenPage = 0;
    public static short[] TAB_ICON_IDS = new short[] { 0, 1, 2, 3, 4 };
    public static short[] TAB_ICON_SEL_IDS = new short[] { 0, 1, 2, 3, 4 };

public static DualTabScreen instance;
public static long lastTimeRequestPet = 0;
public static long lastTimeRequestDanhHieu = 0;

public static string[] TAB_NAMES = new string[] { "Trang Bị & Túi", "Tiềm Năng", "Kỹ Năng", "Nhiệm Vụ", "Chức Năng" };
public static string[] SHORT_TAB_NAMES = new string[] { "Túi", "T.Năng", "K.Năng", "N.Vụ", "C.Năng" };
public static string[] FILTER_NAMES = new string[] { "Tất Cả", "Trang Bị", "Dược Phẩm", "Đá/NL", "Khác" };

public int x;
public int y;
public int w;
public int h;
public int maxWShow;
public int lastCanvasW = -1;
public int lastCanvasH = -1;

public bool isWide;
public int curMainTab = 0;
public int curInvenFilter = 0;
public bool isRefresh = false;

public bool[] isTabInitialized = new bool[5];
public bool[] isTabDirty = new bool[5];
public static int keyFireCoolDown = 0;



public iCommand cmdClose;
public iCommand cmdAction;
public iCommand cmdMenuAction;

// Focus: 0 = Left Pane (Equip / Stats), 1 = Right/Content (Inven / Potential / Skills / Quests / Pets / ChucNang), 2 = Top Nav Tabs, 3 = Filter Bar (Tab 0)
public int focusPane = 1;

// Scroll helpers
public Scroll scrollInven = new Scroll();
public Scroll scrollInfo = new Scroll();
public Scroll scrollAttri = new Scroll();
public Scroll scrollSkill = new Scroll();
public Scroll scrollQuest = new Scroll();
public Scroll scrollPet = new Scroll();
public Scroll scrollLeftStat = new Scroll();
public Scroll scrollDanhHieu = new Scroll();
public Scroll scrollFeatureMenu = new Scroll();

// Tab 0: Inventory & Left Stat Box & Equip Paging
public ListNew invenList;
public ListNew leftStatList;
public mVector filteredItems = new mVector();
    public mVector vecInfoSS;
public int selectedInvenIndex = -1;
public int selectedEquipIndex = -1;
public MainItem selectedItemInfo = null;
public int itemTouchX = 0;
public int itemTouchY = 0;

public int equipSubTab = 0; // 0: Trang Bị Thường (0..7), 1: Thần Trang (8..15)
public int equipScrollY = 0;
public int equipToY = 0;
public bool isDraggingEquip = false;
public int equipDragStartY = 0;
public int equipDragStartScrollY = 0;
public static int EQUIP_PAGE_H = 116;

// Tab 1: Tiềm Năng & Thông Tin
public ListNew infoList;
public ListNew attriList;
public int selectedAttIndex = 0;
public int timeFocusAtt = 0;

// Tab 2: Kỹ Năng
public ListNew skillList;
public ListNew skillDetailList;
public Scroll scrollSkillDetail = new Scroll();
public mVector validSkills = new mVector();
public int selectedSkillIndex = 0;

// Tab 3: Nhiệm Vụ
public ListNew questList;
public int selectedQuestIndex = 0;

// Tab 4: Chức Năng (0: Menu Lựa Chọn, 1: Danh Hiệu, 2: Thú Cưng)
public int chucNangSubView = 0;
public int subViewMode = 0;
public int selectedChucNangIndex = 0;
public ListNew featureMenuList;
public ListNew danhHieuList;
public int selectedDanhHieuIndex = 0;
public DanhHieuInfo selectedDanhHieuInfo = null;
public DataSkillEff previewTitleEff = null;
public short lastPreviewTitleEffId = -1;
public ListNew petList;
public int selectedPetIndex = -1;
public MainItem selectedPetInfo = null;
public InputDialog inputDialogSkill = null;
private static string[] SHORT_ATT_NAMES = new string[] { "S.Mạnh", "P.Thủ", "T.Lực", "T.Thần", "N.Nhẹn" };

public static DualTabScreen gI() {
if (instance == null) {
instance = new DualTabScreen();
}
return instance;
}
    public DualTabScreen() {
        cmdClose = new iCommand(T.close, 0, this);
        cmdAction = new iCommand(T.select, 1, this);
        cmdMenuAction = new iCommand("Menu", 2, this);
        backCMD = cmdClose;
        
        center = cmdAction;
        center = cmdMenuAction;
    }

public void recalculateLayout() {
    w = MotherCanvas.w - 12;
    if (w > 560) w = 560;
    if (w < 120) w = MotherCanvas.w - 4;

    h = MotherCanvas.h - 16;
    if (h > 380) h = 380;
    if (h < 120) h = MotherCanvas.h - 4;

    x = MotherCanvas.hw - w / 2;
    y = MotherCanvas.hh - h / 2;
    maxWShow = 0;

    isWide = (MotherCanvas.w >= 280 && MotherCanvas.w >= MotherCanvas.h) || UILayoutEngine.isWidescreen();

    if (GameCanvas.isTouch && MainTab.fraCloseTab != null) {
        cmdClose.setPos(x + w - 16, y + 14, MainTab.fraCloseTab, "");
    }

    for (int i = 0; i < 5; i++) {
        isTabInitialized[i] = false;
    }

    initTab(curMainTab);
    updateSoftKeys();
}

public override void Show(MainScreen screen) {
    base.Show(screen);
    instance = this;
    keyFireCoolDown = 6;
    GameCanvas.clearAll();
    GameCanvas.clearKeyHold();
    GameCanvas.clearKeyPressed();
    lastCanvasW = MotherCanvas.w;
    lastCanvasH = MotherCanvas.h;

    if (GameCanvas.tabInven == null) {
        GameCanvas.tabInven = new TabInventory(T.tabInven, Player.vecInventory, (sbyte)0, MainTab.xTab);
        GameCanvas.tabInven.initCmd();
    }

    selectedInvenIndex = -1;
    selectedEquipIndex = -1;
    selectedItemInfo = null;
    selectedSkillIndex = 0;
    selectedQuestIndex = 0;
    selectedPetIndex = -1;
    selectedPetInfo = null;
    focusPane = 1;
    equipSubTab = 0;
    equipScrollY = 0;
    equipToY = 0;
    isDraggingEquip = false;

    if (curMainTab < 0 || curMainTab >= 5) {
        curMainTab = 0;
    }

    recalculateLayout();
}

public void initTab(int tabIndex) {
if (tabIndex < 0 || tabIndex >= 5) return;
switch (tabIndex) {
case 0:
initTab0();
break;
case 1:
initTab1();
break;
case 2:
initTab2();
break;
case 3:
initTab3();
break;
case 4:
initTab4();
break;
}
isTabInitialized[tabIndex] = true;
isTabDirty[tabIndex] = false;
}

public void initTab0() {
updateFilteredItems();
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int gridW = rightPaneW - 8;
int gridH = h - 73;
int slotStep = 30;
int cols = gridW / slotStep;
if (cols < 1) cols = 1;
int totalSlots = SLOTS_PER_TAB;
int totalRows = (totalSlots + cols - 1) / cols;
int limY = totalRows * slotStep - gridH;
if (limY < 0) limY = 0;
if (invenList == null) {
invenList = new ListNew(x + leftPaneW + 18, y + 50, gridW, gridH, 0, 0, limY, true);
} else {
invenList.cmxLim = limY;
if (invenList.cmtoX > limY) invenList.cmtoX = limY;
if (invenList.cmtoX < 0) invenList.cmtoX = 0;
if (invenList.cmx > limY) invenList.cmx = limY;
if (invenList.cmx < 0) invenList.cmx = 0;
}

// Khởi tạo ô cuộn chỉ số & vé bên trái
int paneX = x + 10;
int paneY = y + 28;
int paneH = h - 34;
int statY = paneY + 150;
int statH = paneH - 152;
if (statH < 36) statH = 36;
int boxX = paneX + 4;
int boxW = leftPaneW - 8;
int viewH = statH - 4;

int lineH = 14;
int currencyRows = 4;
int ticketRows = 3;
int topInfoRows = currencyRows + ticketRows;
int topInfoH = topInfoRows * lineH;
int dividerH = 4;
int statCount = (Player.InfoShortEquip != null && Player.InfoShortEquip.Length > 0) ? Player.InfoShortEquip.Length : 6;
int statRows = (statCount + 1) / 2;
int statH_content = statRows * lineH;
int totalStatContentH = 3 + topInfoH + dividerH + statH_content + 3;
int limStatY = totalStatContentH - viewH;
if (limStatY < 0) limStatY = 0;

if (leftStatList == null) {
leftStatList = new ListNew(boxX, statY + 2, boxW, viewH, 0, 0, limStatY, true);
} else {
leftStatList.x = boxX;
leftStatList.y = statY + 2;
leftStatList.maxW = boxW;
leftStatList.maxH = viewH;
leftStatList.cmxLim = limStatY;
if (leftStatList.cmtoX > limStatY) leftStatList.cmtoX = limStatY;
}
}

public static MainInfoItem[] getDefaultAttributeMInfo(int index, short totalVal) {
switch (index) {
case 0: // Sức mạnh: Tăng tấn công (1), Xuyên giáp (13), Chí mạng (10)
return new MainInfoItem[] {
new MainInfoItem((sbyte)1, totalVal * 5),
new MainInfoItem((sbyte)13, totalVal),
new MainInfoItem((sbyte)10, totalVal * 2)
};
case 1: // Phòng thủ: Phòng thủ (4), Kháng vật lý (26), Kháng phép (27)
return new MainInfoItem[] {
new MainInfoItem((sbyte)4, totalVal * 4),
new MainInfoItem((sbyte)26, totalVal),
new MainInfoItem((sbyte)27, totalVal)
};
case 2: // Thể lực: Máu HP (15), Hồi máu từ bình (23)
return new MainInfoItem[] {
new MainInfoItem((sbyte)15, totalVal * 20),
new MainInfoItem((sbyte)23, totalVal)
};
case 3: // Tinh thần: Năng lượng MP (16), Chí mạng phép (11), Xuyên kháng (14)
return new MainInfoItem[] {
new MainInfoItem((sbyte)16, totalVal * 15),
new MainInfoItem((sbyte)11, totalVal * 2),
new MainInfoItem((sbyte)14, totalVal)
};
case 4: // Nhanh nhẹn: Giảm hồi chiêu (25), Né tránh (12)
return new MainInfoItem[] {
new MainInfoItem((sbyte)25, totalVal),
new MainInfoItem((sbyte)12, totalVal)
};
default:
return new MainInfoItem[0];
}
}

public static int getAttriCardHeight(Main_Attribute att, int index) {
int subCount = 2;
if (att != null && att.minfo != null && att.minfo.Length > 0) {
subCount = att.minfo.Length;
} else {
MainInfoItem[] def = getDefaultAttributeMInfo(index, (short)((att != null) ? (att.value + att.valuePlus) : 0));
if (def != null && def.Length > 0) subCount = def.Length;
}
int cardH = 18 + subCount * 13 + 4;
if (cardH < 46) cardH = 46;
return cardH;
}
public void initTab1() {
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int leftPaneX = x + 10;
int leftPaneY = y + 28;
int leftPaneH = h - 34;
int listX = leftPaneX + 4;
int listY = leftPaneY + 46;
int listW = leftPaneW - 8;
int listH = leftPaneH - 50;

// 1. Info list (Cột Trái: Toàn bộ chỉ số từ Tab Thông Tin cũ)
int allInfoCount = (GameScreen.player != null && GameScreen.player.vecAllInfo != null) ? GameScreen.player.vecAllInfo.size() : 0;
int totalInfoH = allInfoCount * 15 + 8;
int limInfoY = totalInfoH - listH;
if (limInfoY < 0) limInfoY = 0;
if (infoList == null) {
infoList = new ListNew(listX, listY, listW, listH, 0, 0, limInfoY, true);
} else {
infoList.x = listX;
infoList.y = listY;
infoList.maxW = listW;
infoList.maxH = listH;
infoList.cmxLim = limInfoY;
if (infoList.cmtoX > limInfoY) infoList.cmtoX = limInfoY;
if (infoList.cmtoX < 0) infoList.cmtoX = 0;
if (infoList.cmx > limInfoY) infoList.cmx = limInfoY;
if (infoList.cmx < 0) infoList.cmx = 0;
}

// 2. Potential list (Cột Phải: 5 thẻ Tiềm Năng)
if (Player.mAttribute == null || Player.mAttribute.Length == 0) {
Player.mAttribute = new Main_Attribute[5];
}
for (int i = 0; i < Player.mAttribute.Length; i++) {
if (Player.mAttribute[i] == null) {
string name = (i < T.mAttribute.Length) ? T.mAttribute[i] : ("Thuộc tính " + (i + 1));
Player.mAttribute[i] = new Main_Attribute((sbyte)i, (short)0, (short)0, name, getDefaultAttributeMInfo(i, (short)0));
} else if (Player.mAttribute[i].minfo == null || Player.mAttribute[i].minfo.Length == 0) {
Player.mAttribute[i].minfo = getDefaultAttributeMInfo(i, (short)(Player.mAttribute[i].value + Player.mAttribute[i].valuePlus));
}
}

int numAtt = Player.mAttribute.Length;
if (selectedAttIndex >= numAtt) selectedAttIndex = numAtt - 1;
if (selectedAttIndex < 0) selectedAttIndex = 0;

int rightPaneX = leftPaneX + leftPaneW + 6;
int rightPaneY = y + 28;
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int rightPaneH = h - 34;
int rightListX = rightPaneX + 4;
int rightListY = rightPaneY + 20;
int rightListW = rightPaneW - 8;
int rightListH = rightPaneH - 24;

int totalAttriH = 0;
for (int i = 0; i < numAtt; i++) {
Main_Attribute att = Player.mAttribute[i];
totalAttriH += getAttriCardHeight(att, i) + 4;
}
int limAttriY = totalAttriH - rightListH;
if (limAttriY < 0) limAttriY = 0;
if (attriList == null) {
attriList = new ListNew(rightListX, rightListY, rightListW, rightListH, 0, 0, limAttriY, true);
} else {
attriList.x = rightListX;
attriList.y = rightListY;
attriList.maxW = rightListW;
attriList.maxH = rightListH;
attriList.cmxLim = limAttriY;
if (attriList.cmtoX > limAttriY) attriList.cmtoX = limAttriY;
if (attriList.cmtoX < 0) attriList.cmtoX = 0;
if (attriList.cmx > limAttriY) attriList.cmx = limAttriY;
if (attriList.cmx < 0) attriList.cmx = 0;
}
}

public void initTab2() {
updateSkillList();
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int skillCount = validSkills.size();
if (selectedSkillIndex >= skillCount) selectedSkillIndex = skillCount - 1;
if (selectedSkillIndex < 0) selectedSkillIndex = 0;
int limSkillY = skillCount * 34 - (h - 56);
if (limSkillY < 0) limSkillY = 0;
if (skillList == null) {
skillList = new ListNew(x + 10, y + 42, leftPaneW, h - 56, 0, 0, limSkillY, true);
} else {
skillList.x = x + 10;
skillList.y = y + 42;
skillList.maxW = leftPaneW;
skillList.maxH = h - 56;
skillList.cmxLim = limSkillY;
if (skillList.cmtoX > limSkillY) skillList.cmtoX = limSkillY;
if (skillList.cmtoX < 0) skillList.cmtoX = 0;
if (skillList.cmx > limSkillY) skillList.cmx = limSkillY;
if (skillList.cmx < 0) skillList.cmx = 0;
}

int rightPaneX = leftPaneW + 6 + x + 10;
int rightPaneY = y + 28;
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int rightPaneH = h - 34;
int detailViewH = rightPaneH - 38 - 26;
if (skillDetailList == null) {
skillDetailList = new ListNew(rightPaneX + 4, rightPaneY + 38, rightPaneW - 8, detailViewH, 0, 0, 0, true);
} else {
skillDetailList.x = rightPaneX + 4;
skillDetailList.y = rightPaneY + 38;
skillDetailList.maxW = rightPaneW - 8;
skillDetailList.maxH = detailViewH;
}
}

public void initTab3() {
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int questCount = (Player.vecQuest != null) ? Player.vecQuest.size() : 0;
int limQuestY = questCount * 34 - (h - 56);
if (limQuestY < 0) limQuestY = 0;
if (questList == null) {
questList = new ListNew(x + 10, y + 42, leftPaneW, h - 56, 0, 0, limQuestY, true);
} else {
questList.cmxLim = limQuestY;
if (questList.cmtoX > limQuestY) questList.cmtoX = limQuestY;
if (questList.cmtoX < 0) questList.cmtoX = 0;
if (questList.cmx > limQuestY) questList.cmx = limQuestY;
if (questList.cmx < 0) questList.cmx = 0;
}
}

public void initTab4() {
if (chucNangSubView == 0) {
initTabFeatureMenu();
} else if (chucNangSubView == 1) {
initTabDanhHieu();
} else if (chucNangSubView == 2) {
initTabPet();
}
}

public void initTabFeatureMenu() {
int paneX = x + 10;
int paneY = y + 28;
int paneW = w - 20;
int paneH = h - 34;

int listX = paneX + 6;
int listY = paneY + 23;
int listW = paneW - 12;
int listH = paneH - 29;

int featureCount = getFeatureItemCount();
int cols = isWide ? 2 : 1;
int itemH = (cols == 2) ? 38 : 42;
int rows = (featureCount + cols - 1) / cols;
int totalH = rows * (itemH + 4) + 6;
int limY = totalH - listH;
if (limY < 0) limY = 0;

if (featureMenuList == null) {
featureMenuList = new ListNew(listX, listY, listW, listH, 0, 0, limY, true);
} else {
featureMenuList.x = listX;
featureMenuList.y = listY;
featureMenuList.maxW = listW;
featureMenuList.maxH = listH;
featureMenuList.cmxLim = limY;
if (featureMenuList.cmtoX > limY) featureMenuList.cmtoX = limY;
if (featureMenuList.cmtoX < 0) featureMenuList.cmtoX = 0;
if (featureMenuList.cmx > limY) featureMenuList.cmx = limY;
if (featureMenuList.cmx < 0) featureMenuList.cmx = 0;
}

long now = mSystem.currentTimeMillis();
if ((Player.vecDanhHieu == null || Player.vecDanhHieu.size() == 0) && now - lastTimeRequestDanhHieu > 15000) {
    lastTimeRequestDanhHieu = now;
    GlobalService.gI().Send_DanhHieu((sbyte)0);
}
if ((Player.vecPet == null || Player.vecPet.size() == 0) && now - lastTimeRequestPet > 15000) {
    lastTimeRequestPet = now;
    GlobalService.gI().Send_Pet((sbyte)3);
}
}



public int getFeatureItemCount() {
        return 12;
    }

    public string getFeatureCategory(int index) {
        switch (index) {
            case 0: return "[DANH VỌNG]";
            case 1: return "[ĐỒNG HÀNH]";
            case 2: return "[BĂNG ĐẢNG]";
            case 3: return "[QUAN HỆ]";
            case 4: return "[TỔ ĐỘI]";
            case 5: return "[CHIẾN ĐẤU]";
            case 6: return "[SƯ ĐỒ]";
            case 7: return "[THƯƠNG MẠI]";
            case 8: return "[GIAO TIẾP]";
            case 9: return "[TRANG BỊ]";
            case 10: return "[HỆ THỐNG]";
            case 11: return "[TIỆN ÍCH]";
            default: return "[HỆ THỐNG]";
        }
    }

    public string getFeatureTitle(int index) {
        switch (index) {
            case 0: return "Danh Hiệu";
            case 1: return "Thú Cưng";
            case 2: return "Băng Hải Tặc";
            case 3: return "Bạn Bè & Thù";
            case 4: return "Nhóm Đội";
            case 5: return "Cờ PK & Đồ Sát";
            case 6: return "Sư Đồ (Bái Sư)";
            case 7: return "Chợ Đấu Giá";
            case 8: return "Loa & Chat";
            case 9: return "Đổi Set Đồ";
            case 10: return "Cài Đặt Tự Động";
            case 11: return "Menu Auto MOD";
            default: return "Chức Năng";
        }
    }

    public string getFeatureDesc(int index) {
        switch (index) {
            case 0: {
                int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
                string curTitleStr = "Chưa kích hoạt";
                if (Player.vecDanhHieu != null) {
                    for (int i = 0; i < Player.vecDanhHieu.size(); i++) {
                        DanhHieuInfo dh = (DanhHieuInfo)Player.vecDanhHieu.elementAt(i);
                        if (dh != null && dh.state == 2) {
                            curTitleStr = dh.name;
                            break;
                        }
                    }
                }
                return "Đang dùng: " + curTitleStr + " (" + dhCount + " danh hiệu)";
            }
            case 1: {
                int petCount = (Player.vecPet != null) ? Player.vecPet.size() : 0;
                string activePet = "Chưa xuất chiến";
                if (Player.vecPet != null) {
                    for (int i = 0; i < Player.vecPet.size(); i++) {
                        object obj = Player.vecPet.elementAt(i);
                        if (obj is MainItem) {
                            MainItem p = (MainItem)obj;
                            if (p != null && p.colorName == 1) {
                                activePet = p.name;
                                break;
                            }
                        }
                    }
                }
                return "Đang mang: " + activePet + " (" + petCount + " thú cưng)";
            }
            case 2: {
                if (GameScreen.player != null && GameScreen.player.clan != null && GameScreen.player.clan.name != null && GameScreen.player.clan.name.Length > 0) {
                    return "Băng: " + GameScreen.player.clan.name + " - Quản lý";
                }
                return "Chưa vào băng - Tìm & xin vào băng";
            }
            case 3: {
                int fCount = (Player.vecFriendList != null) ? Player.vecFriendList.size() : 0;
                return fCount + " bạn bè - Danh sách bạn & kẻ thù";
            }
            case 4: {
                if (Player.vecParty != null && Player.vecParty.size() > 0) {
                    return "Nhóm: " + Player.vecParty.size() + " thành viên - Quản lý đội";
                }
                return "Chưa vào nhóm - Mời & tạo nhóm";
            }
            case 5: {
                string pkName = "Tự do";
                if (GameScreen.player != null) {
                    if (GameScreen.player.typePK == 0) {
                        pkName = "Đồ Sát";
                    } else if (GameScreen.player.typePK > 0 && GameScreen.player.typePK < new string[]{"Đồ sát", "Sát nhân", "Đấu trường", "Chiến đấu", "Đỏ", "Lục", "Vàng", "Tím", "Cam", "Hồng"}.Length) {
                        pkName = new string[]{"Đồ sát", "Sát nhân", "Đấu trường", "Chiến đấu", "Đỏ", "Lục", "Vàng", "Tím", "Cam", "Hồng"}[GameScreen.player.typePK];
                    }
                }
                string dosatStr = (GameScreen.player != null && GameScreen.player.typePK == 0) ? "Đang Bật" : "Tắt";
                return "Cờ PK: " + pkName + " - Đồ sát: " + dosatStr;
            }
            case 6: {
                return "Bái sư học đạo & nhận đệ tử";
            }
            case 7: {
                return "Sàn đấu giá trang bị & vật phẩm";
            }
            case 8: {
                return "Kênh thế giới, thông báo & chat nhanh";
            }
            case 9: {
                return "Chuyển nhanh các set trang bị chiến đấu";
            }
            case 10: {
                return "Tự động đánh, tự ăn thức ăn & nhặt đồ";
            }
            case 11: {
                return "Menu Tính Năng MOD / Tiện Ích Hỗ Trợ Game";
            }
            default:
                return "";
        }
    }

    public string getFeatureSubText(int index) {
        return getFeatureDesc(index);
    }

    public short getFeatureIconId(int index) {
return -1;
}

public void selectFeatureItem(int index) {
selectedChucNangIndex = index;
switch (index) {
case 0:
openDanhHieuSubView();
break;
case 1:
openPetSubView();
break;
case 2:
if (GameScreen.player != null && GameScreen.player.clan != null) {
if (GameCanvas.gameScr.cmdClan != null) GameCanvas.gameScr.cmdClan.perform();
else GameCanvas.gameScr.commandPointer(40, 0);
} else {
Interface_Game.addInfoPlayerNormal("Bạn chưa tham gia bang hội!", mFont.tahoma_7_yellow);
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bạn chưa tham gia bang hội!");
}
break;
case 3:
{
mVector mVec = new mVector();
if (GameCanvas.gameScr.cmdFriendList != null) mVec.addElement(GameCanvas.gameScr.cmdFriendList);
if (GameCanvas.gameScr.cmdBlackList != null) mVec.addElement(GameCanvas.gameScr.cmdBlackList);
if (GameCanvas.gameScr.cmdAddFriend != null) mVec.addElement(GameCanvas.gameScr.cmdAddFriend);
GameCanvas.menu.startAt(mVec, 2, "Bạn Bè & Kẻ Thù");
break;
}
case 4:
if (Player.vecParty != null && Player.vecParty.size() > 0) {
if (GameCanvas.gameScr.cmdParty != null) GameCanvas.gameScr.cmdParty.perform();
else GameCanvas.gameScr.commandPointer(10, 0);
} else {
Interface_Game.addInfoPlayerNormal("Bạn chưa vào nhóm!", mFont.tahoma_7_yellow);
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bạn chưa vào nhóm!");
}
break;
case 5:
{
mVector mVec = new mVector();
if (GameCanvas.gameScr.cmdMenuPk != null) mVec.addElement(GameCanvas.gameScr.cmdMenuPk);
if (GameCanvas.gameScr.cmdSetDosat != null) mVec.addElement(GameCanvas.gameScr.cmdSetDosat);
GameCanvas.menu.startAt(mVec, 2, T.chonco);
break;
}
case 6:
if (GameScreen.player != null && GameScreen.player.sudo != null) {
if (GameCanvas.gameScr.cmdSudo != null) GameCanvas.gameScr.cmdSudo.perform();
else GameCanvas.gameScr.commandPointer(63, 0);
} else {
Interface_Game.addInfoPlayerNormal("Bạn chưa có sư đồ!", mFont.tahoma_7_yellow);
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bạn chưa có sư đồ!");
}
break;
case 7:
if (GameCanvas.gameScr.cmdDauGia != null) GameCanvas.gameScr.cmdDauGia.perform();
break;
case 8:
if (GameCanvas.gameScr.cmdShowWC != null) GameCanvas.gameScr.cmdShowWC.perform();
else GameCanvas.gameScr.commandPointer(33, 0);
break;
case 9:
if (GameCanvas.gameScr.cmdUniform != null) GameCanvas.gameScr.cmdUniform.perform();
else GameCanvas.gameScr.commandPointer(48, 0);
break;
case 10:
openAutoSubView();
break;
case 11:
AThMadaraMOD.getInstance().openMenuAuto();
break;
}
}

public void openAutoSubView() {
MsgAutoFire msgAutoFire = new MsgAutoFire();
msgAutoFire.setinfoAuto_Fire();
GameCanvas.Start_Current_Dialog((MainDialog)msgAutoFire);
}

public void initTabDanhHieu() {
int paneX = x + 10;
int paneY = y + 28;
int paneW = w - 20;
int paneH = h - 34;

int listX = paneX + 6;
int listY = paneY + 24;
int listW = isWide ? (paneW * 55 / 100) : (paneW - 12);
int listH = paneH - 30;

int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
int itemH = 34;
int totalH = dhCount * itemH + 6;
int limY = totalH - listH;
if (limY < 0) limY = 0;

if (danhHieuList == null) {
danhHieuList = new ListNew(listX, listY, listW, listH, 0, 0, limY, true);
} else {
danhHieuList.x = listX;
danhHieuList.y = listY;
danhHieuList.maxW = listW;
danhHieuList.maxH = listH;
danhHieuList.cmxLim = limY;
if (danhHieuList.cmtoX > limY) danhHieuList.cmtoX = limY;
if (danhHieuList.cmtoX < 0) danhHieuList.cmtoX = 0;
if (danhHieuList.cmx > limY) danhHieuList.cmx = limY;
if (danhHieuList.cmx < 0) danhHieuList.cmx = 0;
}

long nowDh = mSystem.currentTimeMillis();
if ((Player.vecDanhHieu == null || Player.vecDanhHieu.size() == 0) && nowDh - lastTimeRequestDanhHieu > 15000) {
lastTimeRequestDanhHieu = nowDh;
GlobalService.gI().Send_DanhHieu((sbyte)0);
} else {
if (selectedDanhHieuIndex >= 0 && selectedDanhHieuIndex < Player.vecDanhHieu.size()) {
selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(selectedDanhHieuIndex);
} else if (Player.vecDanhHieu.size() > 0) {
selectedDanhHieuIndex = 0;
selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(0);
}
}
}

public void initTabPet() {
int petGridW = w - 24;
int petGridH = h - 56;
int petCols = (petGridW - 12) / 32;
if (petCols < 1) petCols = 1;
int petCount = (Player.vecPet != null) ? Player.vecPet.size() : 0;
int totalSlots = System.Math.Max(Player.maxInventory, System.Math.Max(petCount, petCols * 4));
int petRows = (totalSlots + petCols - 1) / petCols;
int limPetY = petRows * 32 - petGridH;
if (limPetY < 0) limPetY = 0;
if (petList == null) {
petList = new ListNew(x + 10, y + 42, petGridW, petGridH, 0, 0, limPetY, true);
} else {
petList.cmxLim = limPetY;
if (petList.cmtoX > limPetY) petList.cmtoX = limPetY;
if (petList.cmtoX < 0) petList.cmtoX = 0;
if (petList.cmx > limPetY) petList.cmx = limPetY;
if (petList.cmx < 0) petList.cmx = 0;
}
if ((Player.vecPet == null || Player.vecPet.size() == 0) && mSystem.currentTimeMillis() - lastTimeRequestPet > 15000) {
    lastTimeRequestPet = mSystem.currentTimeMillis();
    GlobalService.gI().Send_Pet((sbyte)3);
}
}

public void onDanhHieuDataUpdated() {
initTabDanhHieu();
updateSoftKeys();
}

public void openDanhHieuSubView() {
chucNangSubView = 1;
focusPane = 1;
initTabDanhHieu();
updateSoftKeys();
}

public void openPetSubView() {
chucNangSubView = 2;
focusPane = 1;
initTabPet();
updateSoftKeys();
}

public void selectItem(MainItem item, int touchX, int touchY) {
selectedItemInfo = item;
itemTouchX = touchX;
itemTouchY = touchY;
if (selectedItemInfo != null) {
    int maxPopupH = System.Math.Max(60, System.Math.Min(h - 54, MotherCanvas.h - 54));
    if (selectedItemInfo.hInfo > maxPopupH) {
        selectedItemInfo.hRunInfo = selectedItemInfo.hInfo - maxPopupH;
    } else {
        selectedItemInfo.hRunInfo = 0;
    }
    if (selectedItemInfo.typeObject == 3) {
        this.vecInfoSS = MainItem.getInfoSS(selectedItemInfo);
    } else {
        this.vecInfoSS = null;
    }
} else {
    this.vecInfoSS = null;
}
updateSoftKeys();
}

public void selectPet(MainItem pet, int touchX, int touchY) {
selectedPetInfo = pet;
itemTouchX = touchX;
itemTouchY = touchY;
if (selectedPetInfo != null) {

int maxPopupH = System.Math.Max(60, System.Math.Min(h - 54, MotherCanvas.h - 54));
if (selectedPetInfo.hInfo > maxPopupH) {
selectedPetInfo.hRunInfo = selectedPetInfo.hInfo - maxPopupH;
} else {
selectedPetInfo.hRunInfo = 0;
}


}
updateSoftKeys();
}

public void switchTab(int targetTab) {
switchTab(targetTab, false);
}

public void switchTab(int targetTab, bool keepTopFocus) {
if (targetTab < 0) targetTab = 4;
if (targetTab > 4) targetTab = 0;
if (curMainTab != targetTab) {
selectedItemInfo = null;
selectedInvenIndex = -1;
selectedPetInfo = null;
selectedPetIndex = -1;
selectedEquipIndex = -1;
curMainTab = targetTab;
if (curMainTab == 4) {
chucNangSubView = 0;
}
if (keepTopFocus || focusPane == 2) {
focusPane = 2;
} else {
focusPane = 1;
}
if (!isTabInitialized[curMainTab] || isTabDirty[curMainTab] || curMainTab == 2) {
initTab(curMainTab);
}
if (focusPane == 1) {
    if (curMainTab == 0 && filteredItems != null && filteredItems.size() > 0) {
        selectedInvenIndex = 0;
        selectedItemInfo = (MainItem)filteredItems.elementAt(0);
        selectItem(selectedItemInfo, 0, 0);
    } else if (curMainTab == 1) {
        selectedAttIndex = 0;
    } else if (curMainTab == 2) {
        selectedSkillIndex = 0;
    } else if (curMainTab == 3) {
        selectedQuestIndex = 0;
    } else if (curMainTab == 4) {
        selectedChucNangIndex = 0;
    }
}
updateSoftKeys();
}
}

public void onPetDataUpdated() {
initTabPet();
updateSoftKeys();
}

public void markTabDirty(int tabIndex) {
if (tabIndex >= 0 && tabIndex < 5) {
isTabDirty[tabIndex] = true;
if (curMainTab == tabIndex) {
initTab(tabIndex);
updateSoftKeys();
}
}
}

public void onSkillUpdated() {
isTabDirty[2] = true;
if (curMainTab == 2) {
initTab2();
updateSoftKeys();
}
}

public void updateSkillList() {
    validSkills.removeAllElements();

    mVector source = null;
    if (Player.vecListSkill != null && Player.vecListSkill.size() > 0) {
        source = Player.vecListSkill;
    } else if (TabSkill.vecListSkillPaint != null && TabSkill.vecListSkillPaint.size() > 0) {
        source = TabSkill.vecListSkillPaint;
    }

    if (source != null) {
        // Nhóm 1: Chiêu chủ động nghề thông thường (typeSkill == 1, không phải Trái ác quỷ hay Haki)
        for (int i = 0; i < source.size(); i++) {
            Skill_Info sk = (Skill_Info)source.elementAt(i);
            if (sk != null && sk.Lv_RQ != -1 && sk.typeSkill == 1 && sk.typeDevil == 0) {
                validSkills.addElement(sk);
            }
        }
        // Nhóm 2: Kỹ năng Trái ác quỷ (typeDevil == 1)
        for (int i = 0; i < source.size(); i++) {
            Skill_Info sk = (Skill_Info)source.elementAt(i);
            if (sk != null && sk.Lv_RQ != -1 && sk.typeDevil == 1) {
                validSkills.addElement(sk);
            }
        }
        // Nhóm 3: Kỹ năng Haki (typeDevil == 2)
        for (int i = 0; i < source.size(); i++) {
            Skill_Info sk = (Skill_Info)source.elementAt(i);
            if (sk != null && sk.Lv_RQ != -1 && sk.typeDevil == 2) {
                validSkills.addElement(sk);
            }
        }
        // Nhóm 4: Chiêu hỗ trợ / Buff (typeSkill == 2)
        for (int i = 0; i < source.size(); i++) {
            Skill_Info sk = (Skill_Info)source.elementAt(i);
            if (sk != null && sk.Lv_RQ != -1 && sk.typeSkill == 2 && sk.typeDevil == 0) {
                validSkills.addElement(sk);
            }
        }
        // Nhóm 5: Chiêu nội tại / Bị động (typeSkill == 3)
        for (int i = 0; i < source.size(); i++) {
            Skill_Info sk = (Skill_Info)source.elementAt(i);
            if (sk != null && sk.Lv_RQ != -1 && sk.typeSkill == 3 && sk.typeDevil == 0) {
                validSkills.addElement(sk);
            }
        }
        // Nhóm 6: Chiêu trên biển (typeSkill == 4)
        for (int i = 0; i < source.size(); i++) {
            Skill_Info sk = (Skill_Info)source.elementAt(i);
            if (sk != null && sk.Lv_RQ != -1 && sk.typeSkill == 4) {
                validSkills.addElement(sk);
            }
        }
        // Nhóm 7: Kỹ năng nghề (typeSkill == 6)
        for (int i = 0; i < source.size(); i++) {
            Skill_Info sk = (Skill_Info)source.elementAt(i);
            if (sk != null && sk.Lv_RQ != -1 && sk.typeSkill == 6) {
                validSkills.addElement(sk);
            }
        }
        // Nhóm 8: Các kỹ năng hợp lệ còn lại nếu chưa được thêm
        for (int i = 0; i < source.size(); i++) {
            Skill_Info sk = (Skill_Info)source.elementAt(i);
            if (sk != null && sk.Lv_RQ != -1 && !validSkills.contains(sk)) {
                validSkills.addElement(sk);
            }
        }
    }
}

public static int getItemCategory(MainItem item) {
        if (item == null) return -1;

        // 1: Trang Bị (Trang bị vũ khí, giáp, phụ kiện, thời trang, cánh, thần thú...)
        if (item.typeObject == 3 || item.typeObject == 102 || item.typeObject == 103 || item.typeObject == 107) {
            return 1;
        }

        string name = (item.name != null) ? item.name.ToLower() : "";

        // 2: Dược Phẩm (Bình HP, MP, Bình thuốc, Thức ăn, Nước uống, Bình hồi phục)
        if (item.typeObject == 4) {
            if (item.Hp_Mp_Other == 0 || item.Hp_Mp_Other == 1 || item.Hp_Mp_Other == 2 || item.Hp_Mp_Other == 3 || item.Hp_Mp_Other == 4 || item.Hp_Mp_Other == 5 || item.Hp_Mp_Other == 6 || item.Hp_Mp_Other == 7 || item.Hp_Mp_Other == 8 || item.Hp_Mp_Other == 9 || item.Hp_Mp_Other == 10 || item.Hp_Mp_Other == 11) {
                return 2;
            }
            if (name.IndexOf("máu") >= 0 || name.IndexOf("mau") >= 0 || name.IndexOf("hp") >= 0 ||
                name.IndexOf("năng lượng") >= 0 || name.IndexOf("nang luong") >= 0 || name.IndexOf("mp") >= 0 ||
                name.IndexOf("dược") >= 0 || name.IndexOf("duoc") >= 0 || name.IndexOf("thuốc") >= 0 || name.IndexOf("thuoc") >= 0 ||
                name.IndexOf("bình") >= 0 || name.IndexOf("binh") >= 0 || name.IndexOf("rượu") >= 0 || name.IndexOf("ruou") >= 0 ||
                name.IndexOf("thịt") >= 0 || name.IndexOf("thit") >= 0 || name.IndexOf("bánh") >= 0 || name.IndexOf("banh") >= 0) {
                return 2;
            }
        }

        // Kiểm tra nếu là Rương / Hộp quà / Túi / Gói chứa đồ trước để không nhầm sang đá
        bool isChestOrBox = name.StartsWith("rương") || name.StartsWith("ruong") || 
                               name.StartsWith("hộp quà") || name.StartsWith("hop qua") || 
                               name.StartsWith("túi") || name.StartsWith("tui") || 
                               name.StartsWith("gói") || name.StartsWith("goi");

        // 3: Đá & Nguyên Liệu (Gems, Inlaid stones, Upgrade stones, Crafting materials, Ores, Essences)
        if (!isChestOrBox) {
            // a. Các typeObject nguyên liệu chính
            if (item.typeObject == 7 || item.typeObject == 5 || (item is MainMaterial)) {
                return 3;
            }

            // b. BQ (Hp_Mp_Other) của các loại đá khảm / đá thuộc tính / đá nâng cấp
            if (item.typeObject == 4) {
                if (item.Hp_Mp_Other == 12 || item.Hp_Mp_Other == 57 || item.Hp_Mp_Other == 80 || 
                    item.Hp_Mp_Other == 81 || item.Hp_Mp_Other == 77 || item.Hp_Mp_Other == 27 || 
                    item.Hp_Mp_Other == 28 || item.Hp_Mp_Other == -16 || item.Hp_Mp_Other == -18 || 
                    item.Hp_Mp_Other == 66 || item.Hp_Mp_Other == 93 ||
                    (item.Hp_Mp_Other == 40 && (name.IndexOf("đá") >= 0 || name.IndexOf("da ") >= 0))) {
                    return 3;
                }
            }

            // c. ID ranges của các loại đá khảm / hải thạch / nâng cấp
            if ((item.ID >= 44 && item.ID <= 79) || (item.ID >= 221 && item.ID <= 226) || 
                (item.ID >= 321 && item.ID <= 327) || (item.ID >= 514 && item.ID <= 516) || 
                (item.ID >= 644 && item.ID <= 646) || item.ID == 134 || item.ID == 135 || 
                item.ID == 572 || item.ID == 683) {
                return 3;
            }

            // d. Nhận diện theo tên tiếng Việt mở rộng cho Đá Khảm / Ngọc / Thạch / Nguyên liệu
            if (name.IndexOf("đá ") >= 0 || name.IndexOf("da ") >= 0 || name.StartsWith("đá") || name.StartsWith("da") ||
                name.IndexOf("khảm") >= 0 || name.IndexOf("kham") >= 0 ||
                name.IndexOf("ngọc") >= 0 || name.IndexOf("ngoc") >= 0 ||
                name.IndexOf("cẩm thạch") >= 0 || name.IndexOf("hải thạch") >= 0 || name.IndexOf("thạch anh") >= 0 || 
                name.IndexOf("huyết thạch") >= 0 || name.IndexOf("hắc thạch") >= 0 || name.IndexOf("thạch") >= 0 ||
                name.IndexOf("tinh thể") >= 0 || name.IndexOf("tinh the") >= 0 ||
                name.IndexOf("saphia") >= 0 || name.IndexOf("sapphire") >= 0 || name.IndexOf("topaz") >= 0 || name.IndexOf("ruby") >= 0 ||
                name.IndexOf("kim cương") >= 0 || name.IndexOf("diamond") >= 0 ||
                name.IndexOf("khoáng") >= 0 || name.IndexOf("khoang") >= 0 || name.IndexOf("quặng") >= 0 || name.IndexOf("quang") >= 0) {
                return 3;
            }
        }

        // 4: Khác
        return 4;
    }

    public void updateFilteredItems() {
filteredItems.removeAllElements();
if (Player.vecInventory == null) return;

int startIdx = this.curInvenPage * SLOTS_PER_TAB;
int endIdx = startIdx + SLOTS_PER_TAB;

if (this.curInvenFilter == 0) {
int invSize = Player.vecInventory.size();
for (int i = startIdx; i < endIdx && i < invSize; i++) {
MainItem item = (MainItem)Player.vecInventory.elementAt(i);
if (item != null) {
filteredItems.addElement(item);
}
}
} else {
mVector tempCatItems = new mVector();
int invSize = Player.vecInventory.size();
for (int i = 0; i < invSize; i++) {
MainItem item = (MainItem)Player.vecInventory.elementAt(i);
if (item == null) continue;

int cat = getItemCategory(item);
if (this.curInvenFilter == cat) {
tempCatItems.addElement(item);
}
}

int catSize = tempCatItems.size();
for (int i = startIdx; i < endIdx && i < catSize; i++) {
filteredItems.addElement(tempCatItems.elementAt(i));
}
}

if (this.invenList != null) {
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int gridW = rightPaneW - 8;
int gridH = h - 68;
int slotStep = 30;
int cols = gridW / slotStep;
if (cols < 1) cols = 1;
int totalSlots = SLOTS_PER_TAB;
int totalRows = (totalSlots + cols - 1) / cols;
int limY = totalRows * slotStep - gridH;
if (limY < 0) limY = 0;
this.invenList.cmxLim = limY;
if (this.invenList.cmx > limY) this.invenList.cmx = limY;
if (this.invenList.cmtoX > limY) this.invenList.cmtoX = limY;
}
}

public void setInvenPage(int page) {
if (page < 0) page = 0;
if (page > 9) page = 9;
this.curInvenPage = page;
this.selectedInvenIndex = -1;
this.selectedItemInfo = null;

updateFilteredItems();

if (this.invenList != null) {
this.invenList.cmx = 0;
this.invenList.cmtoX = 0;
}
}

private void updateSoftKeys() {

backCMD = cmdClose;
right = cmdClose;

if (curMainTab == 0) {
if (focusPane == 0 && selectedEquipIndex >= 0) {
cmdAction.caption = "Đổi";
cmdMenuAction.caption = "Đổi";
center = cmdAction;
center = cmdMenuAction;
return;
}

MainItem cur = (selectedItemInfo != null) ? selectedItemInfo : null;
if (cur == null && focusPane == 1 && selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
cur = (MainItem)filteredItems.elementAt(selectedInvenIndex);
}

if (cur != null) {
if (GameCanvas.tabInven == null) {
GameCanvas.tabInven = new TabInventory(T.tabInven, Player.vecInventory, (sbyte)0, MainTab.xTab);
GameCanvas.tabInven.initCmd();
}
GameCanvas.tabInven.itemCur = cur;
GameCanvas.tabInven.IdSelect = Player.vecInventory.IndexOf(cur);
mVector mActions = cur.getActionInven((sbyte)0);

if (mActions != null && mActions.size() > 0) {
iCommand primaryCmd = (iCommand)mActions.elementAt(0);
cmdAction.caption = primaryCmd.caption;
center = cmdAction;

if (mActions.size() > 1) {
cmdMenuAction.caption = "Menu";
center = cmdMenuAction;
} else {
center = cmdAction;
}
return;
}
}
center = null;
center = null;
return;
} else if (curMainTab == 1) {
if (focusPane == 1) {
cmdAction.caption = "+1 Điểm";
cmdMenuAction.caption = "Cộng";
center = cmdAction;
center = cmdAction;
} else {
center = null;
center = null;
}
return;
} else if (curMainTab == 2) {
    if (focusPane == 1 && validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
        Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
        if (curSk != null) {
            bool canAdd = (Player.pointSkill > 0 && curSk.Lv_RQ > 0 && curSk.Lv_RQ < Skill_Info.maxLv);
            bool canHotkey = (curSk.Lv_RQ > 0 && (curSk.typeSkill == 1 || curSk.typeSkill == 2 || curSk.typeSkill == 4));

            if (canAdd && canHotkey) {
                cmdAction.caption = "+ Điểm";
                cmdMenuAction.caption = "Gán Phím";
                left = cmdAction;
                center = cmdMenuAction;
                return;
            } else if (canAdd) {
                cmdAction.caption = "+ Điểm";
                cmdMenuAction.caption = "+ Điểm";
                left = cmdAction;
                center = cmdAction;
                return;
            } else if (canHotkey) {
                cmdAction.caption = "Gán Phím";
                cmdMenuAction.caption = "Gán Phím";
                left = cmdAction;
                center = cmdAction;
                return;
            }
        }
    }
    left = null;
    center = null;
    return;
} else if (curMainTab == 3) {
cmdAction.caption = "Chi Tiết";
cmdMenuAction.caption = "Chi Tiết";
center = cmdAction;
center = cmdAction;
return;
} else if (curMainTab == 4) {
if (chucNangSubView == 0) {
cmdAction.caption = "Mở";
cmdMenuAction.caption = "Mở";
center = cmdAction;
center = cmdAction;
return;
} else if (chucNangSubView == 1) {
if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) {
TitleActionBtn btn = (TitleActionBtn)selectedDanhHieuInfo.actionButtons.elementAt(0);
cmdAction.caption = btn.name;
cmdMenuAction.caption = btn.name;
center = cmdAction;
center = cmdAction;
return;
}
center = null;
center = null;
return;
} else if (chucNangSubView == 2) {
if (focusPane == 1 && selectedPetIndex >= 0 && Player.vecPet != null && selectedPetIndex < Player.vecPet.size()) {
MainItem p = (MainItem)Player.vecPet.elementAt(selectedPetIndex);
string petAct = (p != null && p.colorName == 1) ? "Tháo" : "Dùng";
cmdAction.caption = petAct;
cmdMenuAction.caption = petAct;
center = cmdAction;
center = cmdAction;
return;
}
center = null;
center = null;
return;
}
}
}

public override void commandPointer(int index, int subIndex) {
switch (index) {
case 0:
close();
break;
case 1:
doPrimaryAction();
break;
case 2:
doMenuAction();
break;
case 4:
if (validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
if (curSk != null) {
MainSkill mainSkill = new MainSkill(curSk.ID, curSk.typeEffSkill);
mainSkill.indexHotKey = curSk.indexHotKey;
mainSkill.idIcon = curSk.idIcon;
mainSkill.isBuff = (curSk.typeSkill == 2);
mainSkill.lvDevil = curSk.LvDevilSkill;
mainSkill.typeDevil = curSk.typeDevil;
Player.setHotKey(subIndex, mainSkill, null);
Interface_Game.timePaintIconSkill = 90;
keyFireCoolDown = 8;
}
}
break;
case 5:
GlobalService.gI().Add_Point_Attribute((sbyte)selectedAttIndex, (short)subIndex);
GameCanvas.end_Dialog();
break;
case 10:
if (validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
if (curSk != null && inputDialogSkill != null) {
int pts = 1;
try {
pts = int.Parse(inputDialogSkill.tfInput.getText());
if (pts < 1) pts = 1;
if (pts > Player.pointSkill) pts = Player.pointSkill;
} catch (Exception) {
pts = 1;
}
GlobalService.gI().Add_Point_Skill((short)curSk.indexHotKey, (short)pts);
GameCanvas.end_Dialog();
}
}
break;
case 11:
if (validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
if (curSk != null) {
GlobalService.gI().Add_Point_Skill((short)curSk.indexHotKey, 1);
GameCanvas.end_Dialog();
}
}
break;
}
}

private void changeEquip(int slotIdx, int popupX, int popupY) {
selectedItemInfo = null;
mVector listItem = new mVector();
int equipType = slotIdx;
MainItem curEquip = (MainItem)GameScreen.player.hashEquip.get("" + slotIdx);
if (curEquip != null) {
equipType = curEquip.typeEquip;
}
if (Player.vecInventory != null) {
for (int i = 0; i < Player.vecInventory.size(); i++) {
MainItem mainItem = (MainItem)Player.vecInventory.elementAt(i);
if (mainItem != null && mainItem.typeObject == 3 && mainItem.typeEquip == equipType && (mainItem.charClass == 0 || mainItem.charClass == GameScreen.player.clazz)) {
listItem.addElement(mainItem);
}
}
}
if (listItem.size() == 0) {
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Không có trang bị tương tự trong hành trang.");
} else {
MsgListItem var7 = new MsgListItem();
var7.setinfoListItem(listItem, popupX, popupY, 28, (slotIdx % 2 == 0) ? 2 : 0);
GameCanvas.Start_Current_Dialog((MainDialog)var7);
}
}

private void doPrimaryAction() {
        if (curMainTab == 0) {
            if (focusPane == 0 && selectedEquipIndex >= 0) {
                int leftPaneW = isWide ? 160 : (w * 45 / 100);
                int paneX = x + 10;
                int leftSlotX = paneX + 7;
                int rightSlotX = paneX + leftPaneW - 25 - 7;
                int startSlotY = y + 28 + 28 + 18;
                int slotGapY = 25;
                int row = (selectedEquipIndex % 8) / 2;
                bool isLeft = (selectedEquipIndex % 2 == 0);
                int px = isLeft ? (leftSlotX + 13) : (rightSlotX + 13);
                int py = startSlotY + row * slotGapY + 13;
                changeEquip(selectedEquipIndex, px, py);
                return;
            }

            MainItem cur = (selectedItemInfo != null) ? selectedItemInfo : null;
            if (cur == null && focusPane == 1 && selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
                cur = (MainItem)filteredItems.elementAt(selectedInvenIndex);
            }

            if (cur != null) {
                if (GameCanvas.tabInven == null) {
                    GameCanvas.tabInven = new TabInventory(T.tabInven, Player.vecInventory, (sbyte)0, MainTab.xTab);
                    GameCanvas.tabInven.initCmd();
                }
                GameCanvas.tabInven.itemCur = cur;
                GameCanvas.tabInven.IdSelect = Player.vecInventory.IndexOf(cur);
                mVector mActions = cur.getActionInven((sbyte)0);
                if (mActions != null && mActions.size() > 0) {
                    iCommand primaryCmd = (iCommand)mActions.elementAt(0);
                    primaryCmd.perform();
                    isRefresh = true;
                }
            }
        } else if (curMainTab == 1) {
            addPotentialPoint(selectedAttIndex);
        } else if (curMainTab == 2) {
            if (focusPane == 1 && validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
                Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
                if (curSk != null) {
                    if (Player.pointSkill > 0 && curSk.Lv_RQ > 0 && curSk.Lv_RQ < Skill_Info.maxLv) {
                        addPointSkill(curSk);
                    } else if (curSk.Lv_RQ > 0 && (curSk.typeSkill == 1 || curSk.typeSkill == 2 || curSk.typeSkill == 4)) {
                        openSkillHotkeyMenu();
                    }
                }
            }
        } else if (curMainTab == 3) {
            openQuestDetails();
        } else if (curMainTab == 4) {
            if (chucNangSubView == 0) {
                selectFeatureItem(selectedChucNangIndex);
            } else if (chucNangSubView == 1) {
                if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) {
                    TitleActionBtn btn = (TitleActionBtn)selectedDanhHieuInfo.actionButtons.elementAt(0);
                    if (btn != null) {
                        GlobalService.gI().Send_DanhHieuAction(selectedDanhHieuInfo.id, btn.actionId);
                    }
                }
            } else if (chucNangSubView == 2) {
                if (Player.vecPet != null && selectedPetIndex >= 0 && selectedPetIndex < Player.vecPet.size()) {
                    MainItem curPet = (MainItem)Player.vecPet.elementAt(selectedPetIndex);
                    if (curPet != null) {
                        bool isEquipped = (curPet.colorName == 1);
                        GlobalService.gI().Send_Pet((sbyte)4, (sbyte)(isEquipped ? 0 : 1), curPet.ID);
                    }
                }
            }
        }
    }

    public void addPotentialPoint(int attIdx) {
if (attIdx < 0 || Player.mAttribute == null || attIdx >= Player.mAttribute.Length || Player.mAttribute[attIdx].value >= 80) {
return;
}

if (Player.pointAttribute > 1) {
mVector mVector2 = new mVector();
int[] mNumAttri = new int[] { 1, 5, 10 };
int num2 = 0;
for (int i = 0; i < mNumAttri.Length; i++) {
int num3 = mNumAttri[i];
if (num3 > Player.pointAttribute) {
num3 = Player.pointAttribute;
}
if (num3 > 80 - Player.mAttribute[attIdx].value) {
num3 = 80 - Player.mAttribute[attIdx].value;
}
iCommand iCommand2 = new iCommand("+" + num3, 5, num3, this);
if (GameCanvas.isTouch) {
iCommand2.setTypeRed();
}
if (num2 != num3) {
num2 = num3;
mVector2.addElement(iCommand2);
}
if (mNumAttri[i] >= Player.pointAttribute) {
break;
}
}
string attName = (attIdx < T.mAttribute.Length) ? T.mAttribute[attIdx] : Player.mAttribute[attIdx].name;
GameCanvas.Start_Normal_DiaLog_New("Bạn muốn cộng bao nhiêu điểm tiềm năng vào " + attName + "?", mVector2, true, T.tabAttribute);
} else if (Player.pointAttribute == 1) {
GlobalService.gI().Add_Point_Attribute((sbyte)attIdx, (short)1);
} else {
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bạn không còn điểm tiềm năng?");
}
}

private void doMenuAction() {
if (curMainTab == 0) {
if (focusPane == 0 && selectedEquipIndex >= 0) {
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int paneX = x + 10;
int leftSlotX = paneX + 7;
int rightSlotX = paneX + leftPaneW - 25 - 7;
int startSlotY = y + 28 + 28 + 18;
int slotGapY = 25;
int row = (selectedEquipIndex % 8) / 2;
bool isLeft = (selectedEquipIndex % 2 == 0);
int px = isLeft ? (leftSlotX + 13) : (rightSlotX + 13);
int py = startSlotY + row * slotGapY + 13;
changeEquip(selectedEquipIndex, px, py);
return;
}

MainItem cur = (selectedItemInfo != null) ? selectedItemInfo : null;
if (cur == null && focusPane == 1 && selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
cur = (MainItem)filteredItems.elementAt(selectedInvenIndex);
}

if (cur != null) {
if (GameCanvas.tabInven == null) {
GameCanvas.tabInven = new TabInventory(T.tabInven, Player.vecInventory, (sbyte)0, MainTab.xTab);
GameCanvas.tabInven.initCmd();
}
GameCanvas.tabInven.itemCur = cur;
GameCanvas.tabInven.IdSelect = Player.vecInventory.IndexOf(cur);
mVector mActions = cur.getActionInven((sbyte)0);
if (mActions != null && mActions.size() > 0) {
keyFireCoolDown = 8;
GameCanvas.clearAll();
GameCanvas.clearKeyHold();
GameCanvas.clearKeyPressed();
GameCanvas.menu.startAt(mActions, 2, cur.name);
}
}
} else if (curMainTab == 2) {
if (focusPane == 1 && validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
if (curSk != null && curSk.Lv_RQ > 0 && (curSk.typeSkill == 1 || curSk.typeSkill == 2 || curSk.typeSkill == 4)) {
openSkillHotkeyMenu();
}
}
}
}

public void addPointSkill(Skill_Info sk) {
if (sk == null) return;
if (Player.mLvSkill != null && sk.indexHotKey >= 0 && sk.indexHotKey < Player.mLvSkill.Length) {
if (Player.mLvSkill[sk.indexHotKey] >= Skill_Info.maxLv) {
GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.maxLvSkill);
return;
}
}
if (Player.pointSkill > 1) {
inputDialogSkill = GameCanvas.Start_Input_Dialog(T.nhappoint, new iCommand(T.cmdSetPoint, 10, 0, this), isNum: true, T.congDiem);
GameCanvas.subDialog = inputDialogSkill;
} else if (Player.pointSkill == 1) {
GameCanvas.Start_Normal_DiaLog(T.add1Point, new iCommand(T.congDiem, 11, 0, this), isCmdClose: true);
} else {
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bạn không còn điểm kỹ năng.");
}
}

private void openSkillHotkeyMenu() {
if (validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
if (curSk == null || curSk.Lv_RQ <= 0) {
return;
}
if (curSk.typeSkill == 3 || curSk.typeSkill == 6) {
return;
}
if ((LoadMap.specMap == 4 && curSk.typeSkill == 1) || (LoadMap.specMap != 4 && curSk.typeSkill == 4)) {
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Kỹ năng này chỉ được sử dụng khi đánh trên biển");
return;
}
mVector mVector2 = new mVector();
for (int i = 0; i < 6; i++) {
if (i != 2 && (GameCanvas.isTouch || i != 5)) {
iCommand iCommand2 = (GameCanvas.isTouch ? new iCommand("Phím " + " " + (i + 1), 4, i, this) : ((!TField.isQwerty) ? new iCommand("Phím " + " " + (i * 2 + 1), 4, i, this) : new iCommand("Phím " + " " + T.mKeyQty[i], 4, i, this)));
mVector2.addElement(iCommand2);
}
}
keyFireCoolDown = 8;
GameCanvas.clearAll();
GameCanvas.clearKeyHold();
GameCanvas.clearKeyPressed();
GameCanvas.menu.startAt(mVector2, 2, T.cmdHotKey);
}
}

private void openQuestDetails() {
if (Player.vecQuest != null && selectedQuestIndex >= 0 && selectedQuestIndex < Player.vecQuest.size()) {
MainQuest curQ = (MainQuest)Player.vecQuest.elementAt(selectedQuestIndex);
if (curQ != null) {
MsgDialog msgDialog = new MsgDialog();
msgDialog.setinfoQuest(curQ, isNew: false);
GameCanvas.Start_Current_Dialog((MainDialog)msgDialog);
}
}
}

public void close() {
    GameCanvas.clearAll();
    GameCanvas.clearKeyHold();
    GameCanvas.clearKeyPressed();
    if (curMainTab == 4 && chucNangSubView > 0) {
        chucNangSubView = 0;
        selectedDanhHieuInfo = null;
        selectedPetInfo = null;
        updateSoftKeys();
        return;
    }
    if (lastScreen != null) {
        lastScreen.Show();
    } else {
        GameCanvas.gameScr.Show();
    }
}

    public override void update() {
        if (lastScreen != null) {
            lastScreen.update();
        }

        if (lastCanvasW != MotherCanvas.w || lastCanvasH != MotherCanvas.h) {
            lastCanvasW = MotherCanvas.w;
            lastCanvasH = MotherCanvas.h;
            recalculateLayout();
        }

        if (TabScreen.isRefresh) {
            TabScreen.isRefresh = false;
            isRefresh = true;
        }

        if (isRefresh) {
            isRefresh = false;
            markTabDirty(0);
            markTabDirty(1);
            markTabDirty(2);
            markTabDirty(4);
            if (curMainTab == 0) {
                if (focusPane == 0 && selectedEquipIndex >= 0) {
                    MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
                    selectItem(eq, itemTouchX, itemTouchY);
                } else if (focusPane == 1) {
                    if (selectedItemInfo != null) {
                        int newIdx = filteredItems.IndexOf(selectedItemInfo);
                        if (newIdx >= 0 && !selectedItemInfo.isRemove && (selectedItemInfo.typeObject != 4 || selectedItemInfo.numPotion > 0)) {
                            selectedInvenIndex = newIdx;
                            selectItem(selectedItemInfo, itemTouchX, itemTouchY);
                        } else {
                            if (selectedInvenIndex >= filteredItems.size()) {
                                selectedInvenIndex = filteredItems.size() - 1;
                            }
                            if (selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
                                MainItem it = (MainItem)filteredItems.elementAt(selectedInvenIndex);
                                selectItem(it, itemTouchX, itemTouchY);
                            } else {
                                selectItem(null, 0, 0);
                                selectedInvenIndex = -1;
                            }
                        }
                    } else {
                        if (selectedInvenIndex >= filteredItems.size()) {
                            selectedInvenIndex = filteredItems.size() - 1;
                        }
                        if (selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
                            MainItem it = (MainItem)filteredItems.elementAt(selectedInvenIndex);
                            selectItem(it, itemTouchX, itemTouchY);
                        } else {
                            selectItem(null, 0, 0);
                            selectedInvenIndex = -1;
                        }
                    }
                }

                if (GameCanvas.tabInven != null && selectedItemInfo != null) {
                    GameCanvas.tabInven.itemCur = selectedItemInfo;
                    GameCanvas.tabInven.IdSelect = Player.vecInventory.IndexOf(selectedItemInfo);
                }
                updateSoftKeys();
            } else if (curMainTab == 2) {
                initTab2();
                updateSoftKeys();
            }
        }

        if (maxWShow < w) {
            maxWShow += 60;
            if (maxWShow > w) maxWShow = w;
        }

        MainTab.valuephantram = GameCanvas.gameTick % 120;
        if (MainTab.CDTicket != null) MainTab.CDTicket.updateTimeCountDownTicket();
        if (MainTab.CDPvP != null) MainTab.CDPvP.updateTimeCountDownTicket();
        if (MainTab.CDKeyBoss != null) MainTab.CDKeyBoss.updateTimeCountDownTicket();
        if (MainTab.CDx2XP != null) MainTab.CDx2XP.updateTimeCountDownTicket();

        if (curMainTab == 0) {
            if (equipScrollY != equipToY) {
                int diff = equipToY - equipScrollY;
                if (Math.Abs(diff) <= 4) {
                    equipScrollY = equipToY;
                } else {
                    equipScrollY += diff / 3;
                }
            }
            if (invenList != null) {
                int oldCmx = invenList.cmx;
                invenList.moveCamera();
                if (invenList.pointerIsDowning && CRes.abs(invenList.cmx - oldCmx) > 4) {
                    selectedItemInfo = null;
                    selectedInvenIndex = -1;
                }
            }
            if (leftStatList != null) {
                leftStatList.moveCamera();
            }
        } else if (curMainTab == 1) {
            if (infoList != null) infoList.moveCamera();
            if (attriList != null) attriList.moveCamera();
            if (timeFocusAtt > 0) timeFocusAtt--;
        } else if (curMainTab == 2) {
            if (skillList != null) skillList.moveCamera();
            if (skillDetailList != null) skillDetailList.moveCamera();
        } else if (questList != null && curMainTab == 3) {
            questList.moveCamera();
        } else if (curMainTab == 4) {
            if (chucNangSubView == 0 && featureMenuList != null) featureMenuList.moveCamera();
            else if (chucNangSubView == 1) {
                if (danhHieuList != null) danhHieuList.moveCamera();
                if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.idEff > 0) {
                    if (previewTitleEff == null || lastPreviewTitleEffId != selectedDanhHieuInfo.idEff) {
                        lastPreviewTitleEffId = selectedDanhHieuInfo.idEff;
                        previewTitleEff = new DataSkillEff(selectedDanhHieuInfo.idEff, -1, (sbyte)0, (sbyte)0);
                    }
                } else {
                    previewTitleEff = null;
                    lastPreviewTitleEffId = -1;
                }
                if (previewTitleEff != null) {
                    previewTitleEff.update();
                }
            } else if (chucNangSubView == 2 && petList != null) {
                petList.moveCamera();
            }
        }
        if (keyFireCoolDown > 0) keyFireCoolDown--;
    }

    public override void updatekey() {
        handleKeyPress();
    }

    public void handleKeyPress() {
        if (keyFireCoolDown > 0) return;

        // Softkey Back/Close on Key 13 / 41 (Back/Right/Esc/F2)
        if (GameCanvas.keyMyHold[13] || GameCanvas.isKeyPressed(13) || GameCanvas.keyMyHold[41] || GameCanvas.isKeyPressed(41)) {
            GameCanvas.clearAll();
            GameCanvas.clearKeyHold();
            GameCanvas.clearKeyPressed();
            if (curMainTab == 4 && chucNangSubView != 0) {
                chucNangSubView = 0;
                selectedPetInfo = null;
                selectedDanhHieuInfo = null;
                selectedChucNangIndex = 0;
                focusPane = 1;
                updateSoftKeys();
                return;
            }
            close();
            return;
        }

        // Key 5 / OK / FIRE / Enter (5, 12, 40)
        if (GameCanvas.isKeyPressed(5) || GameCanvas.keyMyPressed[5] || GameCanvas.isKeyPressed(12) || GameCanvas.keyMyPressed[12] || GameCanvas.isKeyPressed(40) || GameCanvas.keyMyPressed[40]) {
            keyFireCoolDown = 8;
            GameCanvas.clearAll();
            GameCanvas.clearKeyHold();
            GameCanvas.clearKeyPressed();
            doPrimaryAction();
            return;
        }

        // Focus 2: Top Nav Tabs
        if (focusPane == 2) {
            if (GameCanvas.isKeyPressed(0)) {
                GameCanvas.ClearkeyMove(0);
                switchTab(curMainTab - 1, true);
                return;
            }
            if (GameCanvas.isKeyPressed(2)) {
                GameCanvas.ClearkeyMove(2);
                switchTab(curMainTab + 1, true);
                return;
            }
            if (GameCanvas.isKeyPressed(3)) {
                GameCanvas.ClearkeyMove(3);
                if (curMainTab == 0) {
                    focusPane = 1;
                    if (selectedInvenIndex < 0 && filteredItems.size() > 0) {
                        selectedInvenIndex = 0;
                        selectedItemInfo = (MainItem)filteredItems.elementAt(0);
                        selectItem(selectedItemInfo, 0, 0);
                    }
                } else if (curMainTab == 1) {
                    focusPane = 0;
                } else if (curMainTab == 2) {
                    focusPane = 1;
                    selectedSkillIndex = 0;
                } else if (curMainTab == 3) {
                    focusPane = 1;
                    selectedQuestIndex = 0;
                } else if (curMainTab == 4) {
                    focusPane = 1;
                    selectedChucNangIndex = 0;
                }
                updateSoftKeys();
                return;
            }
        }

        // Focus 3: Filter Bar (Tab 0)
        if (focusPane == 3) {
            if (GameCanvas.isKeyPressed(0)) {
                GameCanvas.ClearkeyMove(0);
                selectedItemInfo = null;
                selectedInvenIndex = -1;
                curInvenFilter--;
                if (curInvenFilter < 0) curInvenFilter = 4;
                updateFilteredItems();
                updateSoftKeys();
                return;
            }
            if (GameCanvas.isKeyPressed(2)) {
                GameCanvas.ClearkeyMove(2);
                selectedItemInfo = null;
                selectedInvenIndex = -1;
                curInvenFilter++;
                if (curInvenFilter > 4) curInvenFilter = 0;
                updateFilteredItems();
                updateSoftKeys();
                return;
            }
            if (GameCanvas.isKeyPressed(1)) {
                GameCanvas.ClearkeyMove(1);
                focusPane = 2; // Up to Top Nav Tabs
                updateSoftKeys();
                return;
            }
            if (GameCanvas.isKeyPressed(3)) {
                GameCanvas.ClearkeyMove(3);
                focusPane = 1; // Down to Inven Grid
                if (selectedInvenIndex < 0 && filteredItems.size() > 0) {
                    selectedInvenIndex = 0;
                    selectedItemInfo = (MainItem)filteredItems.elementAt(0);
                    selectItem(selectedItemInfo, 0, 0);
                }
                updateSoftKeys();
                return;
            }
        }

        if (curMainTab == 0) {
            handleKeypadTab0();
        } else if (curMainTab == 1) {
            handleKeypadTab1();
        } else if (curMainTab == 2) {
            handleKeypadTab2();
        } else if (curMainTab == 3) {
            handleKeypadTab3();
        } else if (curMainTab == 4) {
            handleKeypadTab4();
        }
    }

    private void handleKeypadTab0() {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int gridW = rightPaneW - 8;
        int cols = gridW / 30;
        if (cols < 1) cols = 1;

        if (focusPane == 1) {
            // Chuyen nhanh trang bang phim * (10) va # (11)
            if (GameCanvas.isKeyPressed(10) || GameCanvas.keyMyHold[10]) {
                GameCanvas.ClearkeyMove(10);
                GameCanvas.clearKeyHold(10);
                setInvenPage(curInvenPage > 0 ? curInvenPage - 1 : 9);
                return;
            }
            if (GameCanvas.isKeyPressed(11) || GameCanvas.keyMyHold[11]) {
                GameCanvas.ClearkeyMove(11);
                GameCanvas.clearKeyHold(11);
                setInvenPage((curInvenPage + 1) % 10);
                return;
            }

            if (GameCanvas.isKeyPressed(0) && selectedInvenIndex >= 0 && selectedInvenIndex % cols == 0) {
                GameCanvas.ClearkeyMove(0);
                focusPane = 0;
                int row = selectedInvenIndex / cols;
                if (row > 3) row = 3;
                selectedEquipIndex = (equipSubTab == 1 ? 8 : 0) + row * 2 + 1;
                MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
                selectItem(eq, 0, 0);
                updateSoftKeys();
                return;
            }

            if (GameCanvas.isKeyPressed(1) && selectedInvenIndex >= 0 && selectedInvenIndex < cols) {
                GameCanvas.ClearkeyMove(1);
                focusPane = 3; // Len Filter Bar
                updateSoftKeys();
                return;
            }

            bool hasKeypadMove = false;
            if (GameCanvas.isKeyPressed(0)) { selectedInvenIndex--; GameCanvas.ClearkeyMove(0); hasKeypadMove = true; }
            if (GameCanvas.isKeyPressed(2)) { selectedInvenIndex++; GameCanvas.ClearkeyMove(2); hasKeypadMove = true; }
            if (GameCanvas.isKeyPressed(1)) { selectedInvenIndex -= cols; GameCanvas.ClearkeyMove(1); hasKeypadMove = true; }
            if (GameCanvas.isKeyPressed(3)) { selectedInvenIndex += cols; GameCanvas.ClearkeyMove(3); hasKeypadMove = true; }

            if (hasKeypadMove) {
                if (selectedInvenIndex < 0) selectedInvenIndex = 0;
                if (selectedInvenIndex >= filteredItems.size()) selectedInvenIndex = filteredItems.size() - 1;

                if (selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
                    MainItem it = (MainItem)filteredItems.elementAt(selectedInvenIndex);
                    selectItem(it, 0, 0);
                    if (GameCanvas.tabInven != null) {
                        GameCanvas.tabInven.itemCur = it;
                        GameCanvas.tabInven.IdSelect = Player.vecInventory.IndexOf(it);
                    }
                } else {
                    selectItem(null, 0, 0);
                    selectedInvenIndex = -1;
                }

                if (invenList != null && selectedInvenIndex >= 0) {
                    int itemSlotH = 30;
                    int itemY = (selectedInvenIndex / cols) * itemSlotH;
                    int invenViewH = (h - 34) - 38;
                    if (itemY < invenList.cmx) invenList.cmtoX = itemY;
                    if (itemY + itemSlotH > invenList.cmx + invenViewH) invenList.cmtoX = itemY + itemSlotH - invenViewH;
                }
                updateSoftKeys();
            }
        } else if (focusPane == 0) {
            // Toggle Trang Bi Thuong (0..7) vs Than Trang (8..15) tren Keypad bang phim 7, 9, * (10), # (11)
            if (GameCanvas.isKeyPressed(7) || GameCanvas.keyMyHold[7] ||
                GameCanvas.isKeyPressed(9) || GameCanvas.keyMyHold[9] ||
                GameCanvas.isKeyPressed(10) || GameCanvas.keyMyHold[10] ||
                GameCanvas.isKeyPressed(11) || GameCanvas.keyMyHold[11]) {
                GameCanvas.ClearkeyMove(7);
                GameCanvas.clearKeyHold(7);
                GameCanvas.ClearkeyMove(9);
                GameCanvas.clearKeyHold(9);
                GameCanvas.ClearkeyMove(10);
                GameCanvas.clearKeyHold(10);
                GameCanvas.ClearkeyMove(11);
                GameCanvas.clearKeyHold(11);
                equipSubTab = (equipSubTab == 0) ? 1 : 0;
                equipToY = (equipSubTab == 1) ? EQUIP_PAGE_H : 0;
                selectedEquipIndex = (equipSubTab == 1) ? 8 : 0;
                selectItem((MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex), 0, 0);
                updateSoftKeys();
                return;
            }

            // Left Grid: 8 Equipment Slots (0..7 Thuong, 8..15 Than Trang)
            int baseSlot = (equipSubTab == 1) ? 8 : 0;
            if (selectedEquipIndex < baseSlot) selectedEquipIndex = baseSlot;

            if (GameCanvas.isKeyPressed(2)) {
                GameCanvas.ClearkeyMove(2);
                if ((selectedEquipIndex - baseSlot) % 2 == 0) {
                    selectedEquipIndex++;
                    MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
                    selectItem(eq, 0, 0);
                } else {
                    focusPane = 1; // Sang Hanh Trang
                    int row = (selectedEquipIndex - baseSlot) / 2;
                    selectedInvenIndex = row * cols;
                    if (selectedInvenIndex >= filteredItems.size()) selectedInvenIndex = filteredItems.size() - 1;
                    if (selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
                        MainItem it = (MainItem)filteredItems.elementAt(selectedInvenIndex);
                        selectItem(it, 0, 0);
                    }
                    updateSoftKeys();
                    return;
                }
            } else if (GameCanvas.isKeyPressed(0)) {
                GameCanvas.ClearkeyMove(0);
                if ((selectedEquipIndex - baseSlot) % 2 == 1) {
                    selectedEquipIndex--;
                    MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
                    selectItem(eq, 0, 0);
                }
            } else if (GameCanvas.isKeyPressed(1)) {
                GameCanvas.ClearkeyMove(1);
                if (selectedEquipIndex - baseSlot >= 2) {
                    selectedEquipIndex -= 2;
                    MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
                    selectItem(eq, 0, 0);
                } else if (equipSubTab == 1) {
                    // Tu Than Trang (slot 8, 9) nhay len Trang Bi Thuong (slot 6, 7)
                    equipSubTab = 0;
                    equipToY = 0;
                    selectedEquipIndex = 6 + (selectedEquipIndex % 2);
                    MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
                    selectItem(eq, 0, 0);
                } else {
                    focusPane = 2; // Jump up to Top Nav Tabs
                    updateSoftKeys();
                    return;
                }
            } else if (GameCanvas.isKeyPressed(3)) {
                GameCanvas.ClearkeyMove(3);
                if (selectedEquipIndex - baseSlot <= 5) {
                    selectedEquipIndex += 2;
                    MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
                    selectItem(eq, 0, 0);
                } else if (equipSubTab == 0) {
                    // Tu Trang Bi Thuong (slot 6, 7) nhay xuong Than Trang (slot 8, 9)
                    equipSubTab = 1;
                    equipToY = EQUIP_PAGE_H;
                    selectedEquipIndex = 8 + (selectedEquipIndex % 2);
                    MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
                    selectItem(eq, 0, 0);
                } else {
                    focusPane = 1; // Xuong cuoi Than Trang thi nhay sang Hanh Trang
                    if (selectedInvenIndex < 0 && filteredItems.size() > 0) selectedInvenIndex = 0;
                    if (selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
                        MainItem it = (MainItem)filteredItems.elementAt(selectedInvenIndex);
                        selectItem(it, 0, 0);
                    }
                    updateSoftKeys();
                    return;
                }
            }
            updateSoftKeys();
        }
    }

    private void handleKeypadTab1() {
        if (focusPane == 0) {
            // Left Table: Scroll full stats list
            if (GameCanvas.isKeyPressed(1) && infoList != null && infoList.cmx <= 0) {
                GameCanvas.ClearkeyMove(1);
                focusPane = 2; // Jump up to Top Nav Tabs
                updateSoftKeys();
                return;
            }
            if (GameCanvas.isKeyPressed(1) && infoList != null) {
                GameCanvas.ClearkeyMove(1);
                infoList.cmtoX -= 18;
                if (infoList.cmtoX < 0) infoList.cmtoX = 0;
            }
            if (GameCanvas.isKeyPressed(3) && infoList != null) {
                GameCanvas.ClearkeyMove(3);
                infoList.cmtoX += 18;
                if (infoList.cmtoX > infoList.cmxLim) infoList.cmtoX = infoList.cmxLim;
            }

            // Key 6 / Right moves to Tiem Nang
            if (GameCanvas.isKeyPressed(2)) {
                GameCanvas.ClearkeyMove(2);
                focusPane = 1;
                selectedAttIndex = 0;
                updateSoftKeys();
                return;
            }
            if (GameCanvas.isKeyPressed(0)) {
                GameCanvas.ClearkeyMove(0);
                switchTab(curMainTab - 1);
                return;
            }
        } else if (focusPane == 1) {
            // Right Table: Potential Cards
            int maxAtt = (Player.mAttribute != null && Player.mAttribute.Length > 0) ? Player.mAttribute.Length : 5;
            bool keyMoved = false;

            if (GameCanvas.isKeyPressed(1) && selectedAttIndex == 0) {
                GameCanvas.ClearkeyMove(1);
                focusPane = 2; // Jump up to Top Nav Tabs
                updateSoftKeys();
                return;
            }
            if (GameCanvas.isKeyPressed(1)) { selectedAttIndex--; GameCanvas.ClearkeyMove(1); keyMoved = true; }
            if (GameCanvas.isKeyPressed(3)) { selectedAttIndex++; GameCanvas.ClearkeyMove(3); keyMoved = true; }
            if (selectedAttIndex < 0) selectedAttIndex = maxAtt - 1;
            if (selectedAttIndex >= maxAtt) selectedAttIndex = 0;

            if (keyMoved && attriList != null) {
                int targetY = 0;
                for (int i = 0; i < selectedAttIndex; i++) {
                    Main_Attribute att = (Player.mAttribute != null && i < Player.mAttribute.Length) ? Player.mAttribute[i] : null;
                    targetY += getAttriCardHeight(att, i) + 4;
                }
                Main_Attribute curAtt = (Player.mAttribute != null && selectedAttIndex < Player.mAttribute.Length) ? Player.mAttribute[selectedAttIndex] : null;
                int curH = getAttriCardHeight(curAtt, selectedAttIndex);
                int rightListH = (h - 34) - 24;
                if (targetY < attriList.cmx) attriList.cmtoX = targetY;
                if (targetY + curH > attriList.cmx + rightListH) attriList.cmtoX = targetY + curH - rightListH;
            }

            // Key 4 / Left moves to Chi So
            if (GameCanvas.isKeyPressed(0)) {
                GameCanvas.ClearkeyMove(0);
                focusPane = 0;
                updateSoftKeys();
                return;
            }
            if (GameCanvas.isKeyPressed(2)) {
                GameCanvas.ClearkeyMove(2);
                switchTab(curMainTab + 1);
                return;
            }
        }
    }

    private void handleKeypadTab2() {
        int total = (validSkills != null) ? validSkills.size() : 0;
        if (total > 0 && focusPane == 1) {
            // Scroll detail pane using * (10) and # (11)
            if (GameCanvas.isKeyPressed(10) || GameCanvas.keyMyHold[10]) {
                GameCanvas.ClearkeyMove(10);
                GameCanvas.clearKeyHold(10);
                if (skillDetailList != null) {
                    skillDetailList.cmtoX -= 25;
                    if (skillDetailList.cmtoX < 0) skillDetailList.cmtoX = 0;
                }
                return;
            }
            if (GameCanvas.isKeyPressed(11) || GameCanvas.keyMyHold[11]) {
                GameCanvas.ClearkeyMove(11);
                GameCanvas.clearKeyHold(11);
                if (skillDetailList != null) {
                    skillDetailList.cmtoX += 25;
                    if (skillDetailList.cmtoX > skillDetailList.cmxLim) skillDetailList.cmtoX = skillDetailList.cmxLim;
                }
                return;
            }

            bool hasKeyMove = false;
            if (GameCanvas.isKeyPressed(1) && selectedSkillIndex == 0) {
                GameCanvas.ClearkeyMove(1);
                focusPane = 2;
                updateSoftKeys();
                return;
            }
            if (GameCanvas.isKeyPressed(1)) { selectedSkillIndex--; GameCanvas.ClearkeyMove(1); hasKeyMove = true; }
            if (GameCanvas.isKeyPressed(3)) { selectedSkillIndex++; GameCanvas.ClearkeyMove(3); hasKeyMove = true; }
            if (GameCanvas.isKeyPressed(0)) { GameCanvas.ClearkeyMove(0); switchTab(curMainTab - 1); return; }
            if (GameCanvas.isKeyPressed(2)) { GameCanvas.ClearkeyMove(2); switchTab(curMainTab + 1); return; }
            if (selectedSkillIndex < 0) selectedSkillIndex = 0;
            if (selectedSkillIndex >= total) selectedSkillIndex = total - 1;

            if (hasKeyMove) {
                if (skillDetailList != null) {
                    skillDetailList.cmx = 0;
                    skillDetailList.cmtoX = 0;
                }
                if (skillList != null) {
                    int slotY = selectedSkillIndex * 34;
                    if (slotY < skillList.cmx) skillList.cmtoX = slotY;
                    if (slotY + 34 > skillList.cmx + (h - 56)) skillList.cmtoX = slotY + 34 - (h - 56);
                }
            }
            updateSoftKeys();
        }
    }

    private void handleKeypadTab3() {
        int total = (Player.vecQuest != null) ? Player.vecQuest.size() : 0;
        if (total > 0 && focusPane == 1) {
            bool hasKeyMove = false;
            if (GameCanvas.isKeyPressed(1) && selectedQuestIndex == 0) {
                GameCanvas.ClearkeyMove(1);
                focusPane = 2;
                return;
            }
            if (GameCanvas.isKeyPressed(1)) { selectedQuestIndex--; GameCanvas.ClearkeyMove(1); hasKeyMove = true; }
            if (GameCanvas.isKeyPressed(3)) { selectedQuestIndex++; GameCanvas.ClearkeyMove(3); hasKeyMove = true; }
            if (GameCanvas.isKeyPressed(0)) { GameCanvas.ClearkeyMove(0); switchTab(curMainTab - 1); return; }
            if (GameCanvas.isKeyPressed(2)) { GameCanvas.ClearkeyMove(2); switchTab(curMainTab + 1); return; }
            if (selectedQuestIndex < 0) selectedQuestIndex = 0;
            if (selectedQuestIndex >= total) selectedQuestIndex = total - 1;

            if (hasKeyMove && questList != null) {
                int slotY = selectedQuestIndex * 34;
                if (slotY < questList.cmx) questList.cmtoX = slotY;
                if (slotY + 34 > questList.cmx + (h - 56)) questList.cmtoX = slotY + 34 - (h - 56);
            }
            updateSoftKeys();
        }
    }

    private void handleKeypadTab4() {
        if (chucNangSubView == 0) {
            int count = getFeatureItemCount();
            if (focusPane == 1 && count > 0) {
                int cols = isWide ? 2 : 1;
                int itemH = (cols == 2) ? 38 : 42;
                bool keyMoved = false;

                if (cols == 2) {
                    if (GameCanvas.isKeyPressed(1)) {
                        GameCanvas.ClearkeyMove(1);
                        if (selectedChucNangIndex < 2) {
                            focusPane = 2;
                            updateSoftKeys();
                            return;
                        }
                        selectedChucNangIndex -= 2;
                        keyMoved = true;
                    } else if (GameCanvas.isKeyPressed(3)) {
                        GameCanvas.ClearkeyMove(3);
                        if (selectedChucNangIndex + 2 < count) {
                            selectedChucNangIndex += 2;
                            keyMoved = true;
                        }
                    } else if (GameCanvas.isKeyPressed(0)) {
                        GameCanvas.ClearkeyMove(0);
                        if (selectedChucNangIndex % 2 == 1) {
                            selectedChucNangIndex--;
                            keyMoved = true;
                        } else {
                            switchTab(curMainTab - 1);
                            return;
                        }
                    } else if (GameCanvas.isKeyPressed(2)) {
                        GameCanvas.ClearkeyMove(2);
                        if (selectedChucNangIndex % 2 == 0 && selectedChucNangIndex + 1 < count) {
                            selectedChucNangIndex++;
                            keyMoved = true;
                        } else {
                            switchTab(curMainTab + 1);
                            return;
                        }
                    }
                } else {
                    if (GameCanvas.isKeyPressed(1) && selectedChucNangIndex == 0) {
                        GameCanvas.ClearkeyMove(1);
                        focusPane = 2;
                        updateSoftKeys();
                        return;
                    }
                    if (GameCanvas.isKeyPressed(1)) {
                        selectedChucNangIndex--;
                        GameCanvas.ClearkeyMove(1);
                        keyMoved = true;
                    } else if (GameCanvas.isKeyPressed(3)) {
                        selectedChucNangIndex++;
                        GameCanvas.ClearkeyMove(3);
                        keyMoved = true;
                    } else if (GameCanvas.isKeyPressed(0)) {
                        GameCanvas.ClearkeyMove(0);
                        switchTab(curMainTab - 1);
                        return;
                    } else if (GameCanvas.isKeyPressed(2)) {
                        GameCanvas.ClearkeyMove(2);
                        switchTab(curMainTab + 1);
                        return;
                    }
                }

                if (selectedChucNangIndex < 0) selectedChucNangIndex = 0;
                if (selectedChucNangIndex >= count) selectedChucNangIndex = count - 1;

                if (keyMoved && featureMenuList != null) {
                    int row = selectedChucNangIndex / cols;
                    int itemY = row * (itemH + 4);
                    int listH = h - 34 - 29;
                    if (itemY < featureMenuList.cmx) featureMenuList.cmtoX = itemY;
                    if (itemY + itemH > featureMenuList.cmx + listH) featureMenuList.cmtoX = itemY + itemH - listH;
                }
                updateSoftKeys();
            }
        } else if (chucNangSubView == 1) {
            int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
            if (focusPane == 1 && dhCount > 0) {
                bool keyMoved = false;
                if (GameCanvas.isKeyPressed(1) && selectedDanhHieuIndex == 0) {
                    GameCanvas.ClearkeyMove(1);
                    focusPane = 2;
                    updateSoftKeys();
                    return;
                }
                if (GameCanvas.isKeyPressed(1)) {
                    selectedDanhHieuIndex--;
                    GameCanvas.ClearkeyMove(1);
                    keyMoved = true;
                } else if (GameCanvas.isKeyPressed(3)) {
                    selectedDanhHieuIndex++;
                    GameCanvas.ClearkeyMove(3);
                    keyMoved = true;
                } else if (GameCanvas.isKeyPressed(0)) {
                    GameCanvas.ClearkeyMove(0);
                    chucNangSubView = 0;
                    selectedDanhHieuInfo = null;
                    updateSoftKeys();
                    return;
                }
                if (selectedDanhHieuIndex < 0) selectedDanhHieuIndex = 0;
                if (selectedDanhHieuIndex >= dhCount) selectedDanhHieuIndex = dhCount - 1;

                selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(selectedDanhHieuIndex);
                if (keyMoved && danhHieuList != null) {
                    int itemH = 38;
                    int itemY = selectedDanhHieuIndex * itemH;
                    if (itemY < danhHieuList.cmx) danhHieuList.cmtoX = itemY;
                    if (itemY + itemH > danhHieuList.cmx + (h - 34 - 30)) danhHieuList.cmtoX = itemY + itemH - (h - 34 - 30);
                }
                updateSoftKeys();
            }
        } else if (chucNangSubView == 2) {
            int total = (Player.vecPet != null) ? Player.vecPet.size() : 0;
            int paneW = w - 20;
            int gridW = paneW - 16;
            int slotStep = 28 + 3;
            int cols = gridW / slotStep;
            if (cols < 1) cols = 1;

            if (focusPane == 1 && total > 0) {
                if (GameCanvas.isKeyPressed(1) && selectedPetIndex < cols) {
                    GameCanvas.ClearkeyMove(1);
                    focusPane = 2;
                    updateSoftKeys();
                    return;
                }
                if (GameCanvas.isKeyPressed(0) && selectedPetIndex % cols == 0) {
                    GameCanvas.ClearkeyMove(0);
                    chucNangSubView = 0;
                    selectedPetInfo = null;
                    updateSoftKeys();
                    return;
                }
                bool hasPetMove = false;
                if (GameCanvas.isKeyPressed(0)) { selectedPetIndex--; GameCanvas.ClearkeyMove(0); hasPetMove = true; }
                if (GameCanvas.isKeyPressed(2)) { selectedPetIndex++; GameCanvas.ClearkeyMove(2); hasPetMove = true; }
                if (GameCanvas.isKeyPressed(1)) { selectedPetIndex -= cols; GameCanvas.ClearkeyMove(1); hasPetMove = true; }
                if (GameCanvas.isKeyPressed(3)) { selectedPetIndex += cols; GameCanvas.ClearkeyMove(3); hasPetMove = true; }
                if (selectedPetIndex < 0) selectedPetIndex = 0;
                if (selectedPetIndex >= total) selectedPetIndex = total - 1;
                MainItem pet = (MainItem)Player.vecPet.elementAt(selectedPetIndex);
                selectPet(pet, 0, 0);

                if (hasPetMove && petList != null) {
                    int petSlotStep = 28 + 3;
                    int petY = (selectedPetIndex / cols) * petSlotStep;
                    int petViewH = (h - 34) - 30;
                    if (petY < petList.cmx) petList.cmtoX = petY;
                    if (petY + petSlotStep > petList.cmx + petViewH) petList.cmtoX = petY + petSlotStep - petViewH;
                }
                updateSoftKeys();
            }
        }
    }

    public override void paint(mGraphics g) {
        if (lastScreen != null) {
            lastScreen.paint(g);
        }
        GameCanvas.resetTrans(g);
        g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

        this.paintPaper(g, x, y, w, h, 0);

        if (Interface_Game.imgHoavan != null && (UIThemeManager.getCurrentTheme() == null || UIThemeManager.getCurrentTheme().id == 0)) {
            g.drawImage(Interface_Game.imgHoavan, x + 6, y + 6, 0);
            g.drawRegion(Interface_Game.imgHoavan, 0, 0, 23, 23, 2, x + w - 29, y + 6, 0);
            g.drawRegion(Interface_Game.imgHoavan, 0, 0, 23, 23, 1, x + 6, y + h - 29, 0);
            g.drawRegion(Interface_Game.imgHoavan, 0, 0, 23, 23, 3, x + w - 29, y + h - 29, 0);
        }

        if (GameCanvas.isTouch && cmdClose != null) {
            cmdClose.paint(g, cmdClose.xCmd, cmdClose.yCmd);
        }

        paintTopNavTabs(g);

        try {
            if (curMainTab == 0) {
                paintTabEquipAndInven(g);
            } else if (curMainTab == 1) {
                paintTabTiemNangAndInfo(g);
            } else if (curMainTab == 2) {
                paintTabSkillDual(g);
            } else if (curMainTab == 3) {
                paintTabQuestDual(g);
            } else if (curMainTab == 4) {
                paintTabChucNang(g);
            }
        } catch (Exception) {}

// Side money box removed for clean parchment UI

        GameCanvas.resetTrans(g);
        g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

        if (!GameCanvas.isTouch) {
            base.paint(g);
        }
    }

    private void paintTopNavTabs(mGraphics g) {
        int tabCount = TAB_NAMES.Length;
        int tabW = (w - 44) / tabCount;
        int tabH = 18;
        int startX = x + 8;
        int startY = y + 8;

        for (int i = 0; i < tabCount; i++) {
            int curX = startX + i * tabW;
            bool isSel = (i == curMainTab);
            bool isFocused = (focusPane == 2 && isSel);

            AvMain.paintRect(g, curX, startY, tabW - 2, tabH, (sbyte)(isSel ? 1 : 0), (isSel ? 0 : 1));

            if (isFocused) {
                g.setColor(0xFFFF00);
                g.drawRect(curX - 1, startY - 1, tabW, tabH + 1);
            }

            if (isSel && AvMain.imgNenfocus != null) {
                g.drawRegion(AvMain.imgNenfocus, 2, 2, tabW - 2, tabH, 0, curX, startY, 0);
            }

            try {
                string tabName = (!isWide || tabW < 52) ? SHORT_TAB_NAMES[i] : TAB_NAMES[i];
                int textX = curX + (tabW - 2) / 2;
                int textY = startY + 3;
                if (isSel) {
                    mFont.tahoma_7b_yellow.drawStringAutoCenter(g, tabName, textX, textY, tabW - 4);
                } else {
                    mFont.tahoma_7_black.drawStringAutoCenter(g, tabName, textX, textY, tabW - 4);
                }

                // Huy hiệu thông báo góc tab
                bool showBadge = false;
                if (i == 1 && Player.pointAttribute > 0) showBadge = true;
                else if (i == 2 && Player.isSkillready) showBadge = true;
                else if (i == 3 && TabQuest.isNewQuest) showBadge = true;
                else if (i == 4 && GameScreen.numMess > 0) showBadge = true;

                if (showBadge && MainEvent.imgNew != null && GameCanvas.gameTick % 10 < 8) {
                    g.drawImage(MainEvent.imgNew, curX + tabW - 8, startY + 3, 3);
                }
            } catch (Exception) {}
        }
    }

    private void paintTabEquipAndInven(mGraphics g) {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int paneX = x + 10;
        int paneY = y + 28;
        int paneH = h - 34;

        AvMain.paintRect(g, paneX, paneY, leftPaneW, paneH, (sbyte)0, 1);

        mFont.tahoma_7b_black.drawStringAutoCenter(g, GameScreen.player.name, paneX + leftPaneW / 2, paneY + 3, leftPaneW - 12);
        string lvStr;
        if (GameScreen.player.Lv >= 100) {
            lvStr = "Lv." + GameScreen.player.Lv + (GameScreen.player.LvThongThao > 0 ? " (TT " + GameScreen.player.LvThongThao + ")" : "");
        } else {
            lvStr = "Lv." + GameScreen.player.Lv;
        }
        mFont.tahoma_7_yellow.drawString(g, lvStr, paneX + leftPaneW / 2, paneY + 14, 2);

        int expBarW = leftPaneW - 20;
        int expBarH = 3;
        int expBarX = paneX + 10;
        int expBarY = paneY + 25;
        g.setColor(0x222222);
        g.fillRect(expBarX, expBarY, expBarW, expBarH);
        int percent = (int)GameScreen.player.percentLv;
        if (percent > 1000) percent = 1000;
        if (percent < 0) percent = 0;
        int expFillW = expBarW * percent / 1000;
        if (expFillW > 0) {
            g.setColor(0x00FF00);
            g.fillRect(expBarX, expBarY, expFillW, expBarH);
        }

        int eqSlotSize = 25;
        int leftSlotX = paneX + 7;
        int rightSlotX = paneX + leftPaneW - eqSlotSize - 7;
        int equipAreaX = paneX + 4;
        int equipAreaY = paneY + 28;
        int equipAreaW = leftPaneW - 8;

        int tabBtnW = (equipAreaW - 6) / 2;
        int tabBtnH = 13;
        int tabBtnY = equipAreaY + 1;
        bool isTab0Active = (equipSubTab == 0);
        bool isTab1Active = (equipSubTab == 1);

        String eqTab0Label = (tabBtnW < 52) ? "T.Bị" : "Trang Bị";
        String eqTab1Label = (tabBtnW < 52) ? "Thần" : "Thần Trang";

        // Nút "Trang Bị"
        AvMain.paintRect(g, equipAreaX + 2, tabBtnY, tabBtnW, tabBtnH, (sbyte)(isTab0Active ? 1 : 0), (isTab0Active ? 0 : 1));
        if (isTab0Active && AvMain.imgNenfocus != null) {
            g.drawRegion(AvMain.imgNenfocus, 2, 2, tabBtnW, tabBtnH, 0, equipAreaX + 2, tabBtnY, 0);
        }
        if (isTab0Active) mFont.tahoma_7b_yellow.drawStringAutoCenter(g, eqTab0Label, equipAreaX + 2 + tabBtnW / 2, tabBtnY + 1, tabBtnW - 4);
        else mFont.tahoma_7_white.drawStringAutoCenter(g, eqTab0Label, equipAreaX + 2 + tabBtnW / 2, tabBtnY + 1, tabBtnW - 4);

        // Nút "Thần Trang"
        AvMain.paintRect(g, equipAreaX + 4 + tabBtnW, tabBtnY, tabBtnW, tabBtnH, (sbyte)(isTab1Active ? 1 : 0), (isTab1Active ? 0 : 1));
        if (isTab1Active && AvMain.imgNenfocus != null) {
            g.drawRegion(AvMain.imgNenfocus, 2, 2, tabBtnW, tabBtnH, 0, equipAreaX + 4 + tabBtnW, tabBtnY, 0);
        }
        if (isTab1Active) mFont.tahoma_7b_yellow.drawStringAutoCenter(g, eqTab1Label, equipAreaX + 4 + tabBtnW + tabBtnW / 2, tabBtnY + 1, tabBtnW - 4);
        else mFont.tahoma_7_white.drawStringAutoCenter(g, eqTab1Label, equipAreaX + 4 + tabBtnW + tabBtnW / 2, tabBtnY + 1, tabBtnW - 4);

        int viewClipY = equipAreaY + 16;
        int viewClipH = 104;
        int charCenterX = paneX + leftPaneW / 2;

        g.setClip(equipAreaX, viewClipY, equipAreaW, viewClipH);
        g.translate(0, -equipScrollY);

        int slotGapY = 25;

        // PAGE 0: Trang Bị Thường (0..7)
        int page0Y = viewClipY;
        int startSlotY0 = page0Y + 2;
        for (int i = 0; i < 4; i++) {
            paintEquipSlot(g, leftSlotX, startSlotY0 + i * slotGapY, eqSlotSize, i * 2);
            paintEquipSlot(g, rightSlotX, startSlotY0 + i * slotGapY, eqSlotSize, i * 2 + 1);
        }
        int charCenterY0 = page0Y + 86;
        if (MainObject.imgShadow != null) {
            g.drawImage(MainObject.imgShadow, charCenterX, charCenterY0 + 4, 3);
        }
        if (GameScreen.player != null) {
            g.drawImage(MainObject.imgShadow, charCenterX, charCenterY0 + GameScreen.player.hOne / 4, 3);
            GameScreen.player.paintCharShow(g, charCenterX, charCenterY0 + GameScreen.player.hOne / 4, 0, isNhip: true);
        }

        // PAGE 1: Thần Trang (8..15)
        int page1Y = viewClipY + EQUIP_PAGE_H;
        int startSlotY1 = page1Y + 2;
        for (int i = 0; i < 4; i++) {
            paintEquipSlot(g, leftSlotX, startSlotY1 + i * slotGapY, eqSlotSize, 8 + i * 2);
            paintEquipSlot(g, rightSlotX, startSlotY1 + i * slotGapY, eqSlotSize, 8 + i * 2 + 1);
        }
        int charCenterY1 = page1Y + 86;
        if (MainObject.imgShadow != null) {
            g.drawImage(MainObject.imgShadow, charCenterX, charCenterY1 + 4, 3);
        }
        if (GameScreen.player != null) {
            short cw = (short)-2, ch = (short)-2, cb = (short)-2, cl = (short)-2;
            MainItem eq8 = (MainItem)GameScreen.player.hashEquip.get("8");
            if (eq8 != null) cw = (eq8.idPart > 0) ? eq8.idPart : ((eq8.ID == 2603) ? (short)184 : (short)-2);
            MainItem eq9 = (MainItem)GameScreen.player.hashEquip.get("9");
            if (eq9 != null) ch = (eq9.idPart > 0) ? eq9.idPart : ((eq9.ID == 2600) ? (short)222 : (short)-2);
            MainItem eq11 = (MainItem)GameScreen.player.hashEquip.get("11");
            if (eq11 != null) cb = (eq11.idPart > 0) ? eq11.idPart : ((eq11.ID == 2602) ? (short)223 : (short)-2);
            MainItem eq13 = (MainItem)GameScreen.player.hashEquip.get("13");
            if (eq13 != null) cl = (eq13.idPart > 0) ? eq13.idPart : ((eq13.ID == 2601) ? (short)224 : (short)-2);
            GameScreen.player.paintCharShowWithOverride(g, charCenterX, charCenterY1, 0, true, cw, ch, cb, cl);
        }

        GameCanvas.resetTrans(g);
        g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

        int statY = paneY + 150;
        int statH = paneH - 152;
        if (statH < 36) statH = 36;
        int boxX = paneX + 4;
        int boxW = leftPaneW - 8;
        AvMain.paintRect(g, boxX, statY, boxW, statH, (sbyte)0, 3);

        int viewH = statH - 4;
        int viewW = boxW - 4;

        int lineH = 14;
        int currencyRows = 4;
        int ticketRows = 3;
        int topInfoRows = currencyRows + ticketRows;
        int topInfoH = topInfoRows * lineH;
        int dividerH = 4;

        // 2. Tinh toan truoc so dong cua cac chi so de cuon dong chuan xac
        string[] defaultLabels = new string[] { "Att", "Magic", "Def", "Cri", "Cri.D", "Eva" };
        string[] labels = (T.mNameShortInfo != null && T.mNameShortInfo.Length >= 6) ? T.mNameShortInfo : defaultLabels;
        int totalItems = (Player.InfoShortEquip != null && Player.InfoShortEquip.Length > 0) ? System.Math.Min(Player.InfoShortEquip.Length, labels.Length) : 0;
        int halfW = boxW / 2 - 4;
        int actualStatLines = 0;

        if (totalItems > 0) {
            int calcIdx = 0;
            while (calcIdx < totalItems) {
                string l1 = (calcIdx < labels.Length) ? labels[calcIdx] : ("Stat" + calcIdx);
                string v1 = (calcIdx < Player.InfoShortEquip.Length && Player.InfoShortEquip[calcIdx] != null && Player.InfoShortEquip[calcIdx].Length > 0) ? Player.InfoShortEquip[calcIdx] : "0";
                string s1 = l1 + ": " + v1;
                mFont f1 = (calcIdx == 3 || calcIdx == 4 || calcIdx == 5) ? mFont.tahoma_7_yellow : mFont.tahoma_7_white;
                int w1 = f1.getWidth(s1);

                if (w1 > halfW - 2 || calcIdx + 1 >= totalItems) {
                    actualStatLines++;
                    calcIdx++;
                } else {
                    string l2 = (calcIdx + 1 < labels.Length) ? labels[calcIdx + 1] : ("Stat" + (calcIdx + 1));
                    string v2 = (calcIdx + 1 < Player.InfoShortEquip.Length && Player.InfoShortEquip[calcIdx + 1] != null && Player.InfoShortEquip[calcIdx + 1].Length > 0) ? Player.InfoShortEquip[calcIdx + 1] : "0";
                    string s2 = l2 + ": " + v2;
                    mFont f2 = (calcIdx + 1 == 3 || calcIdx + 1 == 4 || calcIdx + 1 == 5) ? mFont.tahoma_7_yellow : mFont.tahoma_7_white;
                    int w2 = f2.getWidth(s2);

                    if (w2 <= halfW - 2) {
                        actualStatLines++;
                        calcIdx += 2;
                    } else {
                        actualStatLines++;
                        calcIdx++;
                    }
                }
            }
        } else {
            actualStatLines = 1;
        }

        int statH_content = actualStatLines * lineH;
        int totalH = 3 + topInfoH + dividerH + statH_content + 3;
        int limStatY = totalH - viewH;
        if (limStatY < 0) limStatY = 0;

        if (leftStatList == null) {
            leftStatList = new ListNew(boxX, statY + 2, boxW, viewH, 0, 0, limStatY, true);
        } else {
            leftStatList.cmxLim = limStatY;
            if (leftStatList.cmtoX > limStatY) leftStatList.cmtoX = limStatY;
            if (leftStatList.cmtoX < 0) leftStatList.cmtoX = 0;
        }

        g.setClip(boxX + 2, statY + 2, viewW, viewH);
        g.translate(0, -leftStatList.cmx);

        int col1X = boxX + 4;
        int col2X = boxX + boxW / 2 + 1;
        int curY = statY + 3;

        // 1. Tiền Tệ & Điểm
        if (AvMain.fraMoney != null) {
            // Beri (icon 0) - full row
            AvMain.fraMoney.drawFrame(0, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_yellow.drawString(g, AvMain.getDotNumber(Player.beliTest), col1X + 14, curY, 0);
            curY += lineH;

            // Ruby (icon 1) - full row
            AvMain.fraMoney.drawFrame(1, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_red.drawString(g, AvMain.getDotNumber((long)(GameScreen.player != null ? GameScreen.player.gem : 0)), col1X + 14, curY, 0);
            curY += lineH;

            // Extol (icon 7) - full row
            AvMain.fraMoney.drawFrame(7, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_green.drawString(g, AvMain.getDotNumber((long)(GameScreen.player != null ? GameScreen.player.vnd : 0)), col1X + 14, curY, 0);
            curY += lineH;

            // Cống hiến / Bùa (icon 8) - full row
            AvMain.fraMoney.drawFrame(8, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_orange.drawString(g, AvMain.getDotNumber((long)(GameScreen.player != null ? GameScreen.player.bua : 0)), col1X + 14, curY, 0);
            curY += lineH;

            // 2. Vé & Bánh mì
            // Bánh mì (icon 2) | Vé PvP (icon 4)
            AvMain.fraMoney.drawFrame(2, col1X + 6, curY + 6, 0, 3, g);
            if (MainTab.CDTicket != null && (MainTab.CDTicket.timeCountDown <= 0 || MainTab.valuephantram < 60)) {
                mFont.tahoma_7_white.drawString(g, Player.ticket + "/" + Player.maxTicket, col1X + 14, curY, 0);
            } else if (MainTab.CDTicket != null) {
                MainTab.CDTicket.paintCountDownTicketHour(g, mFont.tahoma_7_yellow, col1X + 14, curY, 0);
            }

            AvMain.fraMoney.drawFrame(4, col2X + 6, curY + 6, 0, 3, g);
            if (MainTab.CDPvP != null && (MainTab.CDPvP.timeCountDown <= 0 || MainTab.valuephantram < 60)) {
                mFont.tahoma_7_white.drawString(g, Player.keyBoss + "/" + Player.maxKeyboss, col2X + 14, curY, 0);
            } else if (MainTab.CDPvP != null) {
                MainTab.CDPvP.paintCountDownTicketHour(g, mFont.tahoma_7_yellow, col2X + 14, curY, 0);
            }
            curY += lineH;

            // Chìa khóa boss (icon 3) | x2 XP (icon 6)
            AvMain.fraMoney.drawFrame(3, col1X + 6, curY + 6, 0, 3, g);
            if (MainTab.CDKeyBoss != null && (MainTab.CDKeyBoss.timeCountDown <= 0 || MainTab.valuephantram < 60)) {
                mFont.tahoma_7_white.drawString(g, Player.PvPticket + "/" + Player.maxPvPticket, col1X + 14, curY, 0);
            } else if (MainTab.CDKeyBoss != null) {
                MainTab.CDKeyBoss.paintCountDownTicketHour(g, mFont.tahoma_7_yellow, col1X + 14, curY, 0);
            }

            AvMain.fraMoney.drawFrame(6, col2X + 6, curY + 6, 0, 3, g);
            if (MainTab.CDx2XP != null && MainTab.CDx2XP.timeCountDown <= 0) {
                mFont.tahoma_7_white.drawString(g, "00:00", col2X + 14, curY, 0);
            } else if (MainTab.CDx2XP != null) {
                MainTab.CDx2XP.paintCountDownTicketHour(g, mFont.tahoma_7_yellow, col2X + 14, curY, 0);
            }
            curY += lineH;

            // Điểm PK (icon 5)
            AvMain.fraMoney.drawFrame(5, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_white.drawString(g, "PK: " + (GameScreen.player != null ? GameScreen.player.pointPk : 0), col1X + 14, curY, 0);
            curY += lineH;

            g.setColor(0x554433);
            g.fillRect(boxX + 6, curY + 1, boxW - 12, 1);
            curY += dividerH;
        }

        // 2. Vẽ 6 Chỉ số nhân vật tự động xuống dòng nếu dài hoặc index trước dài
        if (totalItems > 0) {
            int drawIdx = 0;
            while (drawIdx < totalItems) {
                string l1 = (drawIdx < labels.Length) ? labels[drawIdx] : ("Stat" + drawIdx);
                string v1 = (drawIdx < Player.InfoShortEquip.Length && Player.InfoShortEquip[drawIdx] != null && Player.InfoShortEquip[drawIdx].Length > 0) ? Player.InfoShortEquip[drawIdx] : "0";
                string s1 = l1 + ": " + v1;
                mFont f1 = (drawIdx == 3 || drawIdx == 4 || drawIdx == 5) ? mFont.tahoma_7_yellow : mFont.tahoma_7_white;
                int w1 = f1.getWidth(s1);

                if (w1 > halfW - 2 || drawIdx + 1 >= totalItems) {
                    f1.drawString(g, s1, col1X, curY, 0);
                    curY += lineH;
                    drawIdx++;
                } else {
                    string l2 = (drawIdx + 1 < labels.Length) ? labels[drawIdx + 1] : ("Stat" + (drawIdx + 1));
                    string v2 = (drawIdx + 1 < Player.InfoShortEquip.Length && Player.InfoShortEquip[drawIdx + 1] != null && Player.InfoShortEquip[drawIdx + 1].Length > 0) ? Player.InfoShortEquip[drawIdx + 1] : "0";
                    string s2 = l2 + ": " + v2;
                    mFont f2 = (drawIdx + 1 == 3 || drawIdx + 1 == 4 || drawIdx + 1 == 5) ? mFont.tahoma_7_yellow : mFont.tahoma_7_white;
                    int w2 = f2.getWidth(s2);

                    if (w2 <= halfW - 2) {
                        f1.drawString(g, s1, col1X, curY, 0);
                        f2.drawString(g, s2, col2X, curY, 0);
                        curY += lineH;
                        drawIdx += 2;
                    } else {
                        f1.drawString(g, s1, col1X, curY, 0);
                        curY += lineH;
                        drawIdx++;
                    }
                }
            }
        } else {
            mFont.tahoma_7_white.drawString(g, "HP: " + AvMain.getDotNumber(GameScreen.player.Hp), col1X, curY, 0);
            mFont.tahoma_7_white.drawString(g, "MP: " + AvMain.getDotNumber(GameScreen.player.Mp), col2X, curY, 0);
        }

        GameCanvas.resetTrans(g);
        g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

        if (leftStatList.cmxLim > 0) {
            scrollLeftStat.setInfo(boxX + boxW - 3, statY + 2, viewH, 0xAA8800);
            scrollLeftStat.setYScrool(leftStatList.cmx, leftStatList.cmxLim);
            scrollLeftStat.paint(g);
        }

        // BÊN PHẢI: HÀNH TRANG (INVENTORY GRID)
        int rightPaneX = paneX + leftPaneW + 6;
        int rightPaneY = y + 28;
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int rightPaneH = h - 34;

        AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (sbyte)0, 1);

        // 5 Filter Tabs
        int fw = (rightPaneW - 8) / FILTER_NAMES.Length;
        int fy = rightPaneY + 4;
        for (int f = 0; f < FILTER_NAMES.Length; f++) {
            int fx = rightPaneX + 4 + f * fw;
            bool isSelFilter = (f == curInvenFilter);
            bool isFocusedFilter = (focusPane == 3 && isSelFilter);

            AvMain.paintRect(g, fx, fy, fw - 2, 16, (sbyte)(isSelFilter ? 1 : 0), (isSelFilter ? 0 : 1));
            if (isFocusedFilter) {
                g.setColor(0xFFFF00);
                g.drawRect(fx - 1, fy - 1, fw, 17);
            }
            if (isSelFilter && AvMain.imgNenfocus != null) {
                g.drawRegion(AvMain.imgNenfocus, 2, 2, fw - 2, 16, 0, fx, fy, 0);
            }
            string fLabel = (!isWide || fw < 36) ? (f == 0 ? "Tất" : (f == 1 ? "Đồ" : (f == 2 ? "Dược" : (f == 3 ? "Đá" : "Khác")))) : FILTER_NAMES[f];
            if (isSelFilter) {
                mFont.tahoma_7b_yellow.drawString(g, fLabel, fx + (fw - 2) / 2, fy + 2, 2);
            } else {
                mFont.tahoma_7_black.drawString(g, fLabel, fx + (fw - 2) / 2, fy + 2, 2);
            }
        }

        int footY = rightPaneY + rightPaneH - 15;
        int gridX = rightPaneX + 4;
        int gridY = rightPaneY + 22;
        int gridW = rightPaneW - 8;
        int gridH = footY - 2 - gridY;
        int invSlotSize = 28;
        int slotStep = invSlotSize + 2;
        int cols = gridW / slotStep;
        if (cols < 1) cols = 1;
        int gridOffsetX = (gridW - cols * slotStep) / 2;

        if (invenList != null) {
            g.setClip(gridX, gridY, gridW, gridH);
            g.translate(0, -invenList.cmx);

            int totalSlots = SLOTS_PER_TAB;
            int totalRows = (totalSlots + cols - 1) / cols;

            int firstRow = (invenList.cmx - invSlotSize) / slotStep;
            if (firstRow < 0) firstRow = 0;
            int lastRow = (invenList.cmx + gridH + invSlotSize) / slotStep;
            if (lastRow >= totalRows) lastRow = totalRows - 1;

            int startSlot = firstRow * cols;
            int endSlot = Math.Min(totalSlots, (lastRow + 1) * cols);
            int prevW = MainTab.wItem;
            MainTab.wItem = invSlotSize;

            for (int i = startSlot; i < endSlot; i++) {
                int col = i % cols;
                int row = i / cols;
                int slotX = gridX + gridOffsetX + col * slotStep;
                int slotY = gridY + row * slotStep;
                bool isSelItem = (focusPane == 1 && i == selectedInvenIndex);

                AvMain.paintRect(g, slotX, slotY, invSlotSize, invSlotSize, (sbyte)(isSelItem ? 1 : 0), 3);

                if (i < filteredItems.size()) {
                    MainItem item = (MainItem)filteredItems.elementAt(i);
                    if (item != null) {
                        if (item.typeObject == 3 || item.typeObject == 102 || item.typeObject == 103) {
                            item.paintColor(g, slotX + invSlotSize / 2, slotY + invSlotSize / 2, invSlotSize);
                        }
                        item.paint(g, slotX + invSlotSize / 2, slotY + invSlotSize / 2, invSlotSize);

                        if (item.colorName != 0) {
                            g.setColor(MainItem.getColorName(item.colorName));
                            g.drawRect(slotX, slotY, invSlotSize - 1, invSlotSize - 1);
                        }

                        if (item.LvUpgrade > 0) {
                            mFont.tahoma_7b_green.drawString(g, "+" + item.LvUpgrade, slotX + 2, slotY + 1, 0);
                        }
                    }
                }

                if (isSelItem) {
                    g.setColor(0xFFFF00);
                    g.drawRect(slotX - 1, slotY - 1, invSlotSize + 1, invSlotSize + 1);
                    g.drawRect(slotX - 2, slotY - 2, invSlotSize + 3, invSlotSize + 3);
                    if (AvMain.imgNenfocus != null) {
                        g.drawRegion(AvMain.imgNenfocus, 2, 2, invSlotSize, invSlotSize, 0, slotX, slotY, 0);
                    }
                }
            }
            MainTab.wItem = prevW;

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (invenList.cmxLim > 0) {
                scrollInven.setInfo(gridX + gridW - 3, gridY, gridH, 0xFF8800);
                scrollInven.setYScrool(invenList.cmx, invenList.cmxLim);
                scrollInven.paint(g);
            }
        }

        // Footer: Dãy 10 Nút Chuyển Tab (1..10) & Sức chứa hành trang
        int tabBoxY = rightPaneY + rightPaneH - 15;
        int tabBoxH = 13;
        int pageBtnW = (rightPaneW >= 220) ? 14 : 12;
        int tabGap = (rightPaneW >= 220) ? 2 : 1;
        int tabStartX = rightPaneX + 4;

        for (int p = 0; p < 10; p++) {
            int bx = tabStartX + p * (pageBtnW + tabGap);
            bool isCurTab = (p == curInvenPage);
            AvMain.paintRect(g, bx, tabBoxY, pageBtnW, tabBoxH, (sbyte)(isCurTab ? 1 : 0), (isCurTab ? 0 : 1));
            if (isCurTab) {
                g.setColor(0xFFFF00);
                g.drawRect(bx, tabBoxY, pageBtnW - 1, tabBoxH - 1);
                mFont.tahoma_7b_yellow.drawString(g, "" + (p + 1), bx + pageBtnW / 2, tabBoxY + 1, 2);
            } else {
                mFont.tahoma_7_white.drawString(g, "" + (p + 1), bx + pageBtnW / 2, tabBoxY + 1, 2);
            }
        }

        int totalInven = (Player.vecInventory != null) ? Player.vecInventory.size() : 0;
        string capStr = totalInven + "/" + Player.maxInventory;
        mFont.tahoma_7_white.drawString(g, capStr, rightPaneX + rightPaneW - 4, tabBoxY + 1, 1);

        // Tooltip Popup khi chọn món đồ
        if (selectedItemInfo != null) {
            int infoW = (selectedItemInfo.wInfo > 0) ? selectedItemInfo.wInfo : 140;
            int maxPopupH = System.Math.Max(60, System.Math.Min(h - 54, MotherCanvas.h - 54));
            int showH = selectedItemInfo.hInfo - selectedItemInfo.hRunInfo;
            if (showH <= 0 || showH > maxPopupH) showH = (selectedItemInfo.hInfo > 0) ? System.Math.Min(selectedItemInfo.hInfo, maxPopupH) : 60;
            if (showH < 30) showH = 30;

            int infoX, infoY;
            int spaceLeft = x;
            int spaceRight = MotherCanvas.w - (x + w);

            if (spaceLeft >= infoW + 6) {
                infoX = x - infoW - 6;
            } else if (spaceRight >= infoW + 6) {
                infoX = x + w + 6;
            } else if (focusPane == 1) {
                infoX = x + 10;
            } else {
                infoX = rightPaneX + 4;
            }

            if (itemTouchY > 0) {
                infoY = itemTouchY - showH / 2;
                if (infoY + showH + 24 > y + h - 6) infoY = y + h - 6 - showH - 24;
                if (infoY < y + 28) infoY = y + 28;
            } else {
                infoY = y + 28 + (h - 34 - showH - 24) / 2;
                if (infoY < y + 28) infoY = y + 28;
            }

            MainTab.paintInfoEveryWhere(g, selectedItemInfo, this.vecInfoSS, 0, infoX, infoY, infoW, showH, false, null, 0);

            if (GameCanvas.isTouch) {
                int btnY = infoY + showH + 3;
                if (focusPane == 0) {
                    AvMain.paintRect(g, infoX, btnY, 60, 18, (sbyte)1, 1);
                    mFont.tahoma_7b_black.drawString(g, "Tháo/Đổi", infoX + 30, btnY + 3, 2);
                } else {
                    if (GameCanvas.tabInven == null) {
                        GameCanvas.tabInven = new TabInventory(T.tabInven, Player.vecInventory, (sbyte)0, MainTab.xTab);
                        GameCanvas.tabInven.initCmd();
                    }
                    GameCanvas.tabInven.itemCur = selectedItemInfo;
                    GameCanvas.tabInven.IdSelect = Player.vecInventory.IndexOf(selectedItemInfo);
                    mVector mActions = selectedItemInfo.getActionInven((sbyte)0);
                    if (mActions != null && mActions.size() > 0) {
                        if (mActions.size() == 1) {
                            AvMain.paintRect(g, infoX, btnY, 60, 18, (sbyte)1, 1);
                            mFont.tahoma_7b_black.drawString(g, ((iCommand)mActions.elementAt(0)).caption, infoX + 30, btnY + 3, 2);
                        } else {
                            AvMain.paintRect(g, infoX, btnY, 52, 18, (sbyte)1, 1);
                            mFont.tahoma_7b_black.drawString(g, ((iCommand)mActions.elementAt(0)).caption, infoX + 26, btnY + 3, 2);
                            AvMain.paintRect(g, infoX + 56, btnY, 52, 18, (sbyte)1, 1);
                            mFont.tahoma_7b_black.drawString(g, (mActions.size() > 1 ? ((iCommand)mActions.elementAt(1)).caption : "Menu"), infoX + 56 + 26, btnY + 3, 2);
                        }
                    }
                }
            }
        }
    }
private void paintEquipSlot(mGraphics g, int sx, int sy, int size, int equipType) {
bool isSel = (focusPane == 0 && selectedEquipIndex == equipType);
bool isDivine = (equipType >= 8);
AvMain.paintRect(g, sx, sy, size, size, (sbyte)(isSel ? 1 : 0), 3);

MainItem item = (MainItem)GameScreen.player.hashEquip.get("" + equipType);
if (item != null) {
item.paintColor(g, sx + size / 2, sy + size / 2, size);
item.paint(g, sx + size / 2, sy + size / 2, size);
if (item.LvUpgrade > 0) {
    mFont.tahoma_7b_green.drawString(g, "+" + item.LvUpgrade, sx + 2, sy + 1, 0);
}
} else {
int frameIdx = equipType % 8;
AvMain.paintEquipSilhouette(g, frameIdx, sx + size / 2, sy + size / 2);
}

if (isDivine) {
g.setColor(0xFFAA00);
g.drawRect(sx, sy, size - 1, size - 1);
}

if (isSel) {
g.setColor(0xFFFF00);
g.drawRect(sx - 1, sy - 1, size + 1, size + 1);
g.drawRect(sx - 2, sy - 2, size + 3, size + 3);
if (AvMain.imgNenfocus != null) {
g.drawRegion(AvMain.imgNenfocus, 2, 2, size, size, 0, sx, sy, 0);
}
}
}

private void paintAttBuffText(mGraphics g, string text, int id, int px, int py) {
int num = 0;
if (GameScreen.player != null && GameScreen.player.vecBuffCur != null) {
for (int i = 0; i < GameScreen.player.vecBuffCur.size(); i++) {
MainBuff mainBuff = (MainBuff)GameScreen.player.vecBuffCur.elementAt(i);
if (mainBuff == null || mainBuff.vecInfoAtt == null || mainBuff.vecInfoAtt.size() <= 0) continue;
for (int j = 0; j < mainBuff.vecInfoAtt.size(); j++) {
MainInfoItem mainInfoItem = (MainInfoItem)mainBuff.vecInfoAtt.elementAt(j);
if (mainInfoItem != null && mainInfoItem.id == id) {
num += mainInfoItem.value;
break;
}
}
}
}
if (GameScreen.player != null && GameScreen.player.vecAllInfoParty != null) {
for (int k = 0; k < GameScreen.player.vecAllInfoParty.size(); k++) {
MainInfoItem mainInfoItem2 = (MainInfoItem)GameScreen.player.vecAllInfoParty.elementAt(k);
if (mainInfoItem2 != null && mainInfoItem2.id == id) {
num += mainInfoItem2.value;
break;
}
}
}
if (num != 0) {
int width = mFont.tahoma_7_white.getWidth(text);
sbyte isPercent = 0;
if (MainItem.mNameAttributes != null && id >= 0 && id < MainItem.mNameAttributes.Length && MainItem.mNameAttributes[id] != null) {
    isPercent = MainItem.mNameAttributes[id].ispercent;
} else {
    isPercent = MainItem.getFallbackAttributePercent(id);
}
string st = MainItem.strGetPercent(num, isPercent);
if (num > 0) {
st = "+" + st;
mFont.tahoma_7_green.drawString(g, st, px + width + 4, py, 0);
} else {
mFont.tahoma_7_red.drawString(g, st, px + width + 4, py, 0);
}
}
}

private void paintTabTiemNangAndInfo(mGraphics g) {
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int leftPaneX = x + 10;
int leftPaneY = y + 28;
int leftPaneH = h - 34;

// =====================================================================
// CỘT TRÁI: CHỈ SỐ NHÂN VẬT (Toàn bộ chỉ số từ Tab Thông Tin cũ)
// =====================================================================
AvMain.paintRect(g, leftPaneX, leftPaneY, leftPaneW, leftPaneH, (sbyte)0, 1);

if (focusPane == 0) {
g.setColor(0xFFFF00);
g.drawRect(leftPaneX - 1, leftPaneY - 1, leftPaneW + 1, leftPaneH + 1);
}

mFont.tahoma_7b_yellow.drawString(g, "CHỈ SỐ NHÂN VẬT", leftPaneX + leftPaneW / 2, leftPaneY + 4, 2);

// VẼ THANH HP & MP TRÊN ĐẦU CỘT CHỈ SỐ
if (GameScreen.player != null) {
int barX = leftPaneX + 18;
int barW = leftPaneW - 24;
mImage imgIcon = (GameScreen.player.Lv >= 100) ? Interface_Game.imgIconMPHP2 : Interface_Game.imgIconMPHP;

// HP
if (imgIcon != null) {
g.drawRegion(imgIcon, 0, 0, 10, 10, 0, leftPaneX + 6, leftPaneY + 18, 0);
}
Interface_Game.PaintHPMP(g, (sbyte)1, GameScreen.player.Hp, GameScreen.player.maxHp, barX, leftPaneY + 18, 0, 9, barW, 0, false, GameScreen.player.HpEff, false, GameScreen.player.lvHeart);

// MP
if (imgIcon != null) {
g.drawRegion(imgIcon, 0, 10, 10, 10, 0, leftPaneX + 6, leftPaneY + 30, 0);
}
Interface_Game.PaintHPMP(g, (sbyte)2, GameScreen.player.Mp, GameScreen.player.maxMp, barX, leftPaneY + 30, 0, 9, barW, 0, false, 0, false, 0);

// Đường phân cách
g.setColor(0x886633);
g.drawLine(leftPaneX + 6, leftPaneY + 43, leftPaneX + leftPaneW - 6, leftPaneY + 43);
}

int listX = leftPaneX + 4;
int listY = leftPaneY + 46;
int listW = leftPaneW - 8;
int listH = leftPaneH - 50;

if (infoList != null) {
g.setClip(listX, listY, listW, listH);
g.translate(0, -infoList.cmx);

try {
int curY = listY + 2;
if (GameScreen.player != null && GameScreen.player.vecAllInfo != null && GameScreen.player.vecAllInfo.size() > 0) {
for (int i = 0; i < GameScreen.player.vecAllInfo.size(); i++) {
MainInfoItem infoItem = (MainInfoItem)GameScreen.player.vecAllInfo.elementAt(i);
if (infoItem != null) {
string infoStr = MainItem.getInfoEveryWhere(infoItem);
if (infoStr != null && infoStr.Length > 0 && !(infoStr == "null")) {
mFont.tahoma_7_white.drawString(g, infoStr, listX + 4, curY, 0);
paintAttBuffText(g, infoStr, infoItem.id, listX + 4, curY);
curY += 15;
}
}
}
} else if (Player.InfoShortEquip != null && Player.InfoShortEquip.Length >= 4) {
mFont.tahoma_7_white.drawString(g, "Sát thương: " + Player.InfoShortEquip[0], listX + 4, curY, 0);
curY += 15;
mFont.tahoma_7_white.drawString(g, "Giáp: " + Player.InfoShortEquip[1], listX + 4, curY, 0);
curY += 15;
mFont.tahoma_7_yellow.drawString(g, "Chí mạng: " + Player.InfoShortEquip[2], listX + 4, curY, 0);
curY += 15;
mFont.tahoma_7_yellow.drawString(g, "Kháng: " + Player.InfoShortEquip[3], listX + 4, curY, 0);
curY += 15;
}
} catch (Exception) {
} finally {
GameCanvas.resetTrans(g);
g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);
}

if (infoList.cmxLim > 0) {
scrollInfo.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
scrollInfo.setYScrool(infoList.cmx, infoList.cmxLim);
scrollInfo.paint(g);
}
}

// =====================================================================
// CỘT PHẢI: TIỀM NĂNG (5 Thẻ Tiềm Năng cũ)
// =====================================================================
int rightPaneX = leftPaneX + leftPaneW + 6;
int rightPaneY = y + 28;
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int rightPaneH = h - 34;

AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (sbyte)0, 1);

string pointStr = T.tabAttribute + ": " + Player.pointAttribute;
mFont.tahoma_7b_yellow.drawString(g, pointStr, rightPaneX + 10, rightPaneY + 5, 0);

if (Player.pointAttribute > 0 && GameCanvas.gameTick % 10 < 8 && MainEvent.imgNew != null) {
int pw = mFont.tahoma_7b_yellow.getWidth(pointStr);
g.drawImage(MainEvent.imgNew, rightPaneX + 10 + pw + 8, rightPaneY + 8, 3);
}

if (Player.mAttribute == null || Player.mAttribute.Length == 0) {
Player.mAttribute = new Main_Attribute[5];
}
for (int i = 0; i < Player.mAttribute.Length; i++) {
if (Player.mAttribute[i] == null) {
string name = (i < T.mAttribute.Length) ? T.mAttribute[i] : ("Thuộc tính " + (i + 1));
Player.mAttribute[i] = new Main_Attribute((sbyte)i, (short)0, (short)0, name, getDefaultAttributeMInfo(i, (short)0));
} else if (Player.mAttribute[i].minfo == null || Player.mAttribute[i].minfo.Length == 0) {
Player.mAttribute[i].minfo = getDefaultAttributeMInfo(i, (short)(Player.mAttribute[i].value + Player.mAttribute[i].valuePlus));
}
}

int numAtt = Player.mAttribute.Length;
int rightListX = rightPaneX + 4;
int rightListY = rightPaneY + 20;
int rightListW = rightPaneW - 8;
int rightListH = rightPaneH - 24;

if (attriList != null) {
g.setClip(rightListX, rightListY, rightListW, rightListH);
g.translate(0, -attriList.cmx);

            int curCardY = rightListY;
            for (int i = 0; i < numAtt; i++) {
                try {
                    Main_Attribute att = Player.mAttribute[i];
                    if (att == null) continue;

                    MainInfoItem[] minfo = (att.minfo != null && att.minfo.Length > 0) ? att.minfo : getDefaultAttributeMInfo(i, (short)(att.value + att.valuePlus));

                    int cardH = getAttriCardHeight(att, i);
                    int cardX = rightListX + 2;
                    int cardW = rightListW - 4;
                    bool isSel = (focusPane == 1 && i == selectedAttIndex);

                    AvMain.paintRect(g, cardX, curCardY, cardW, cardH, (sbyte)(isSel ? 1 : 0), 3);

                    // Tên thuộc tính & điểm
                    string aName = (att.name != null && att.name.Length > 0) ? att.name : ((i < T.mAttribute.Length && T.mAttribute[i] != null) ? T.mAttribute[i] : ("Thuộc tính " + (i + 1)));
                    string attName = aName + ": " + att.value;
                    mFont.tahoma_7b_white.drawString(g, attName, cardX + 8, curCardY + 3, 0);

                    if (att.valuePlus > 0) {
                        int nw = mFont.tahoma_7b_white.getWidth(attName + " ");
                        mFont.tahoma_7b_blue.drawString(g, "+" + att.valuePlus, cardX + 8 + nw, curCardY + 3, 0);
                    }

                    // Nút (+) bên phải
                    int btnW = 28;
                    int btnH = 22;
                    int btnX = cardX + cardW - btnW - 4;
                    int btnY = curCardY + 2;
                    bool canAdd = (Player.pointAttribute > 0 && att.value < 80);
                    int btnIdx = (!canAdd) ? 2 : ((timeFocusAtt > 0 && selectedAttIndex == i) ? 1 : 0);

                    if (AvMain.fraButtonTiemNang != null) {
                        AvMain.fraButtonTiemNang.drawFrame(btnIdx, btnX + btnW / 2, btnY + btnH / 2, 0, 3, g);
                    } else {
                        AvMain.paintRect(g, btnX, btnY, btnW, btnH, (sbyte)(canAdd ? 1 : 0), 1);
                        mFont.tahoma_7b_yellow.drawString(g, "+", btnX + btnW / 2, btnY + 2, 2);
                    }

                    // Hiển thị các dòng chỉ số cộng ở dưới điểm
                    int statY = curCardY + 17;
                    if (minfo != null) {
                        for (int j = 0; j < minfo.Length; j++) {
                            if (minfo[j] != null) {
                                string subInfo = MainItem.getInfoEveryWhere(minfo[j]);
                                if (subInfo != null && subInfo.Length > 0 && !(subInfo == "null")) {
                                    mFont.tahoma_7_green.drawString(g, subInfo, cardX + 12, statY + j * 13, 0);
                                }
                            }
                        }
                    }

                    if (isSel) {
                        g.setColor(0xFFFF00);
                        g.drawRect(cardX - 1, curCardY - 1, cardW + 1, cardH + 1);
                        if (AvMain.imgNenfocus != null) {
                            g.drawRegion(AvMain.imgNenfocus, 2, 2, cardW, cardH, 0, cardX, curCardY, 0);
                        }
                    }

                    curCardY += cardH + 4;
                } catch (Exception) {
                    curCardY += 46;
                }
            }

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (attriList.cmxLim > 0) {
                scrollAttri.setInfo(rightPaneX + rightPaneW - 3, rightPaneY + 28, rightPaneH - 32, 0xFF8800);
                scrollAttri.setYScrool(attriList.cmx, attriList.cmxLim);
                scrollAttri.paint(g);
            }
        }
    }

    private void paintTabSkillDual(mGraphics g) {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int leftPaneX = x + 10;
        int leftPaneY = y + 28;
        int leftPaneH = h - 34;

        AvMain.paintRect(g, leftPaneX, leftPaneY, leftPaneW, leftPaneH, (sbyte)0, 1);
        mFont.tahoma_7b_yellow.drawString(g, "DANH SÁCH KỸ NĂNG", leftPaneX + leftPaneW / 2, leftPaneY + 4, 2);

        int listX = leftPaneX + 4;
        int listY = leftPaneY + 18;
        int listW = leftPaneW - 8;
        int listH = leftPaneH - 22;

        if (skillList != null) {
            g.setClip(listX, listY, listW, listH);
            g.translate(0, -skillList.cmx);

            if (validSkills != null) {
                for (int i = 0; i < validSkills.size(); i++) {
                    Skill_Info sk = (Skill_Info)validSkills.elementAt(i);
                    if (sk == null) continue;
                    int sy = listY + i * 34;
                    bool isSel = (focusPane == 1 && i == selectedSkillIndex);

                    AvMain.paintRect(g, listX, sy, listW, 32, (sbyte)(isSel ? 1 : 0), 3);

                    // Vẽ icon kỹ năng
                    sk.paint(g, listX + 16, sy + 16);
                    if (sk.phanTramDevilSkill > 0 && AvMain.imgLvDevilSkill != null) {
                        int num5 = sk.phanTramDevilSkill / 5;
                        num5 = ((num5 < 20) ? (num5 + 1) : ((num5 != 20) ? 22 : (num5 + 2)));
                        g.drawRegion(AvMain.imgLvDevilSkill, 0, 22 - num5, 22, num5, 0, listX + 16, sy + 16 + 11, 33);
                    }

                    // Tên kỹ năng
                    string skName = sk.name;
                    if (sk.Lv_RQ == Skill_Info.maxLv) {
                        skName = sk.name + " " + T.max;
                    }
                    if (sk.Lv_RQ < 0) {
                        mFont.tahoma_7b_blue.drawStringAuto(g, skName, listX + 36, sy + 2, listW - 40, 0);
                    } else {
                        mFont.tahoma_7b_white.drawStringAuto(g, skName, listX + 36, sy + 2, listW - 40, 0);
                    }

                    // Dòng phụ: Cấp độ và % tiến trình theo từng loại kỹ năng
                    if (sk.typeDevil == 1) {
                        string dStr = (sk.typeSkill == 1) ? T.devilFruitA : ((sk.typeSkill == 3) ? T.devilFruitP : T.devilFruitB);
                        mFont.tahoma_7_green.drawString(g, dStr, listX + 36, sy + 16, 0);
                    } else if (sk.typeDevil == 2) {
                        string hStr = (sk.typeSkill == 1) ? T.devilHakiA : ((sk.typeSkill == 3) ? T.devilHakiP : T.devilHakiB);
                        mFont.tahoma_7_green.drawString(g, hStr, listX + 36, sy + 16, 0);
                    } else if (sk.typeSkill == 2) {
                        mFont.tahoma_7_white.drawString(g, "Lv: " + sk.Lv_RQ, listX + 36, sy + 16, 0);
                    } else if (sk.typeSkill == 3) {
                        mFont.tahoma_7_white.drawString(g, "Bị động  Lv: " + sk.Lv_RQ, listX + 36, sy + 16, 0);
                    } else if (sk.typeSkill == 4) {
                        mFont.tahoma_7_white.drawString(g, "Tàu biển  Lv: " + sk.Lv_RQ, listX + 36, sy + 16, 0);
                    } else if (sk.typeSkill == 6) {
                        mFont.tahoma_7_white.drawString(g, "Nghề  Lv: " + sk.Lv_RQ, listX + 36, sy + 16, 0);
                    } else {
                        mFont.tahoma_7_white.drawString(g, "Cấp: " + sk.Lv_RQ + "/" + Skill_Info.maxLv, listX + 36, sy + 16, 0);
                    }

                    if (isSel) {
                        g.setColor(0xFFFF00);
                        g.drawRect(listX - 1, sy - 1, listW + 1, 33);
                        if (AvMain.imgNenfocus != null) {
                            g.drawRegion(AvMain.imgNenfocus, 2, 2, listW, 32, 0, listX, sy, 0);
                        }
                    }
                }
            }

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (skillList.cmxLim > 0) {
                scrollSkill.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                scrollSkill.setYScrool(skillList.cmx, skillList.cmxLim);
                scrollSkill.paint(g);
            }
        }

        int rightPaneX = leftPaneX + leftPaneW + 6;
        int rightPaneY = y + 28;
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int rightPaneH = h - 34;

        AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (sbyte)0, 1);

        if (selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
            Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
            if (curSk != null) {
                curSk.paint(g, rightPaneX + 22, rightPaneY + 22);
                if (curSk.phanTramDevilSkill > 0 && AvMain.imgLvDevilSkill != null) {
                    int num5 = curSk.phanTramDevilSkill / 5;
                    num5 = ((num5 < 20) ? (num5 + 1) : ((num5 != 20) ? 22 : (num5 + 2)));
                    g.drawRegion(AvMain.imgLvDevilSkill, 0, 22 - num5, 22, num5, 0, rightPaneX + 22, rightPaneY + 22 + 11, 33);
                }

                string sName = curSk.name;
                if (curSk.Lv_RQ == Skill_Info.maxLv) sName += " " + T.max;
                mFont.tahoma_7b_yellow.drawStringAuto(g, sName, rightPaneX + 42, rightPaneY + 8, rightPaneW - 50, 0);

                string tagType = "[Chiêu Chủ Động]";
                if (curSk.typeDevil == 1) {
                    tagType = "[Ác Quỷ - " + ((curSk.typeSkill == 1) ? "Chủ động" : ((curSk.typeSkill == 3) ? "Nội tại" : "Hỗ trợ")) + "]";
                } else if (curSk.typeDevil == 2) {
                    tagType = "[Haki - " + ((curSk.typeSkill == 1) ? "Chủ động" : ((curSk.typeSkill == 3) ? "Nội tại" : "Hỗ trợ")) + "]";
                } else if (curSk.typeSkill == 2) {
                    tagType = "[Chiêu Hỗ Trợ - " + (curSk.typeBuff < T.mTacdung.Length ? T.mTacdung[curSk.typeBuff] : "Buff") + "]";
                } else if (curSk.typeSkill == 3) {
                    tagType = "[Chiêu Nội Tại - Bị động]";
                } else if (curSk.typeSkill == 4) {
                    tagType = "[Chiêu Trên Biển]";
                } else if (curSk.typeSkill == 6) {
                    tagType = "[Kỹ Năng Nghề]";
                }
                mFont.tahoma_7_green.drawString(g, tagType, rightPaneX + 42, rightPaneY + 22, 0);

                bool canAddPoint = (Player.pointSkill > 0 && curSk.Lv_RQ > 0 && curSk.Lv_RQ < Skill_Info.maxLv);
                bool canAssignKey = (curSk.Lv_RQ > 0 && (curSk.typeSkill == 1 || curSk.typeSkill == 2 || curSk.typeSkill == 4));
                bool hasBottomAction = canAddPoint || canAssignKey;

                int detailViewY = rightPaneY + 38;
                int detailViewW = rightPaneW - 8;
                int detailViewH = rightPaneH - 38 - (hasBottomAction ? 28 : 6);

                if (skillDetailList != null) {
                    g.setClip(rightPaneX + 4, detailViewY, detailViewW, detailViewH);
                    g.translate(0, -skillDetailList.cmx);

                    int curDetailY = detailViewY + 2;

                    if (curSk.typeDevil == 1) {
                        mFont.tahoma_7_white.drawString(g, "Cấp chiêu: " + curSk.Lv_RQ, rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                        mFont.tahoma_7_green.drawString(g, "Cấp Ác Quỷ: " + curSk.LvDevilSkill + "  (+" + curSk.phanTramDevilSkill + "%)", rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    } else if (curSk.typeDevil == 2) {
                        mFont.tahoma_7_white.drawString(g, "Cấp chiêu: " + curSk.Lv_RQ, rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                        mFont.tahoma_7_green.drawString(g, "Cấp Haki: " + curSk.LvDevilSkill + "  (+" + curSk.phanTramDevilSkill + "%)", rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    } else if (curSk.typeSkill == 3 || curSk.typeSkill == 2 || curSk.typeSkill == 6) {
                        mFont.tahoma_7_white.drawString(g, "Cấp độ kỹ năng: " + curSk.Lv_RQ, rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                        if (curSk.LvDevilSkill > 0 || curSk.phanTramDevilSkill > 0) {
                            mFont.tahoma_7_green.drawString(g, T.lvDevil + curSk.LvDevilSkill + "  (+" + curSk.phanTramDevilSkill + "%)", rightPaneX + 14, curDetailY, 0);
                            curDetailY += 14;
                        }
                    } else {
                        if (curSk.Lv_RQ >= Skill_Info.maxLv) {
                            mFont.tahoma_7_green.drawString(g, "Cấp độ: " + curSk.Lv_RQ + "/" + Skill_Info.maxLv + " (" + T.maxLv + ")", rightPaneX + 14, curDetailY, 0);
                            curDetailY += 14;
                        } else {
                            string progressText = "Cấp: " + curSk.Lv_RQ + "/" + Skill_Info.maxLv + "  (Tiến trình: " + MainItem.strGetPercent(curSk.percentLv, 1) + ")";
                            mFont.tahoma_7_white.drawString(g, progressText, rightPaneX + 14, curDetailY, 0);
                            curDetailY += 14;
                            int pBarW = rightPaneW - 28;
                            if (pBarW > 10) {
                                Interface_Game.PaintHPMP(g, 3, curSk.percentLv, 100, rightPaneX + 14, curDetailY, 0, 9, pBarW, 1, false, 0, false, 0);
                                curDetailY += 13;
                            }
                        }
                    }

                    // Sát thương chiêu
                    if (curSk.damage > 0 && curSk.typeSkill != 2 && curSk.typeSkill != 3 && curSk.typeSkill != 6) {
                        mFont.tahoma_7_white.drawString(g, "Sát thương: " + curSk.damage, rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    // MP tiêu hao
                    if (curSk.manaLost > 0) {
                        mFont.tahoma_7_blue.drawString(g, "Tiêu hao: " + curSk.manaLost + " MP", rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    // Thời gian hồi chiêu
                    if (curSk.timeDelay > 0) {
                        mFont.tahoma_7_yellow.drawString(g, "Thời gian hồi: " + MainItem.getTimeDelay(curSk.timeDelay), rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    // Phạm vi sử dụng
                    if (curSk.range > 0) {
                        mFont.tahoma_7_white.drawString(g, "Phạm vi: " + curSk.range + " px", rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    // Số mục tiêu
                    if (curSk.nTarget > 0) {
                        mFont.tahoma_7_white.drawString(g, "Số mục tiêu: " + curSk.nTarget, rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    // Phạm vi lan
                    if (curSk.rangeLan > 0) {
                        mFont.tahoma_7_white.drawString(g, "Phạm vi lan: " + curSk.rangeLan + " px", rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    // Thuộc tính cộng thêm & Hiệu ứng đặc biệt từ vecAtt
                    if (curSk.vecAtt != null && curSk.vecAtt.size() > 0) {
                        int specTime = 0, specRate = 0, specId = 0, bocPhaRate = 0, bocPhaDmg = 0;
                        bool hasAttHeader = false;

                        for (int j = 0; j < curSk.vecAtt.size(); j++) {
                            MainInfoItem attItem = (MainInfoItem)curSk.vecAtt.elementAt(j);
                            if (attItem == null) continue;

                            if (attItem.id >= 28 && attItem.id < 32) {
                                if (attItem.id == 28) specId = attItem.value;
                                else if (attItem.id == 29) specRate = attItem.value;
                                else if (attItem.id == 30) specTime = attItem.value;
                            } else if (attItem.id >= 64 && attItem.id <= 65) {
                                if (attItem.id == 64) bocPhaDmg = attItem.value;
                                else if (attItem.id == 65) bocPhaRate = attItem.value;
                            } else {
                                if (!hasAttHeader) {
                                    g.setColor(0x886633);
                                    g.drawLine(rightPaneX + 14, curDetailY + 2, rightPaneX + rightPaneW - 14, curDetailY + 2);
                                    curDetailY += 6;
                                    hasAttHeader = true;
                                }

                                string attName = MainItem.getAttributeNameSafe(attItem.id);
                                sbyte isPer = MainItem.getAttributePercentSafe(attItem.id);
                                sbyte colorVal = (attItem.colorMain != 0) ? attItem.colorMain : MainItem.getAttributeColorSafe(attItem.id);
                                string valFormatted = (attItem.value > 0 ? "+" : "") + MainItem.strGetPercent(attItem.value, isPer);
                                string fullLine = attName + ": " + valFormatted;

                                mFont f = AvMain.setTextColor(colorVal);
                                if (f == null) f = mFont.tahoma_7_white;
                                string[] wrapped = f.splitFontArray(fullLine, rightPaneW - 28);
                                if (wrapped != null) {
                                    for (int wl = 0; wl < wrapped.Length; wl++) {
                                        f.drawString(g, wrapped[wl], rightPaneX + 14, curDetailY, 0);
                                        curDetailY += 13;
                                    }
                                }
                            }
                        }

                        if (specTime > 0) {
                            if (!hasAttHeader) {
                                g.setColor(0x886633);
                                g.drawLine(rightPaneX + 14, curDetailY + 2, rightPaneX + rightPaneW - 14, curDetailY + 2);
                                curDetailY += 6;
                                hasAttHeader = true;
                            }
                            string effName = "null";
                            if (specId >= 0 && specId < T.mEffSpec.Length) {
                                effName = T.mEffSpec[specId];
                            }
                            string specStr = MainItem.strGetPercent(specRate, 1) + " " + T.gay + " " + effName + " " + T.trong + " " + MainItem.strGetPercent(specTime, 10);
                            string[] wrappedSpec = mFont.tahoma_7_yellow.splitFontArray(specStr, rightPaneW - 28);
                            if (wrappedSpec != null) {
                                for (int wl = 0; wl < wrappedSpec.Length; wl++) {
                                    mFont.tahoma_7_yellow.drawString(g, wrappedSpec[wl], rightPaneX + 14, curDetailY, 0);
                                    curDetailY += 13;
                                }
                            }
                        }

                        if (bocPhaDmg > 0) {
                            if (!hasAttHeader) {
                                g.setColor(0x886633);
                                g.drawLine(rightPaneX + 14, curDetailY + 2, rightPaneX + rightPaneW - 14, curDetailY + 2);
                                curDetailY += 6;
                                hasAttHeader = true;
                            }
                            string bpStr = MainItem.strGetPercent(bocPhaRate, 1) + " " + T.bocPhaAtt + " " + MainItem.strGetPercent(bocPhaDmg, 1);
                            string[] wrappedBp = mFont.tahoma_7_yellow.splitFontArray(bpStr, rightPaneW - 28);
                            if (wrappedBp != null) {
                                for (int wl = 0; wl < wrappedBp.Length; wl++) {
                                    mFont.tahoma_7_yellow.drawString(g, wrappedBp[wl], rightPaneX + 14, curDetailY, 0);
                                    curDetailY += 13;
                                }
                            }
                        }
                    }

                    // idEffSpec hiệu ứng đặc biệt
                    if (curSk.idEffSpec > 0) {
                        string effSpecDesc = "Hiệu ứng: " + curSk.perEffSpec + "% " + ((curSk.idEffSpec >= 0 && curSk.idEffSpec < T.mEffSpec.Length) ? T.mEffSpec[curSk.idEffSpec] : ("" + curSk.idEffSpec)) + " (" + (curSk.timeEffSpec / 1000) + "s)";
                        string[] wrappedEff = mFont.tahoma_7_yellow.splitFontArray(effSpecDesc, rightPaneW - 28);
                        if (wrappedEff != null) {
                            for (int wl = 0; wl < wrappedEff.Length; wl++) {
                                mFont.tahoma_7_yellow.drawString(g, wrappedEff[wl], rightPaneX + 14, curDetailY, 0);
                                curDetailY += 13;
                            }
                        }
                    }

                    // Mô tả chi tiết kỹ năng (curSk.info hoặc fallback curSk.strInfo)
                    string rawDesc = curSk.info;
                    if ((rawDesc == null || rawDesc.Length == 0) && curSk.strInfo != null && curSk.strInfo.Length > 0) {
                        for (int sIdx = 0; sIdx < curSk.strInfo.Length; sIdx++) {
                            if (curSk.strInfo[sIdx] != null && curSk.strInfo[sIdx].Length > 0) {
                                if (rawDesc == null || rawDesc.Length == 0) rawDesc = curSk.strInfo[sIdx];
                                else rawDesc = rawDesc + "\n" + curSk.strInfo[sIdx];
                            }
                        }
                    }

                    if (rawDesc != null && rawDesc.Trim().Length > 0) {
                        g.setColor(0x886633);
                        g.drawLine(rightPaneX + 14, curDetailY + 2, rightPaneX + rightPaneW - 14, curDetailY + 2);
                        curDetailY += 6;

                        string[] descLines = mFont.tahoma_7_white.splitFontArray(rawDesc, rightPaneW - 28);
                        if (descLines != null) {
                            for (int wl = 0; wl < descLines.Length; wl++) {
                                mFont.tahoma_7_white.drawString(g, descLines[wl], rightPaneX + 14, curDetailY, 0);
                                curDetailY += 13;
                            }
                        }
                    }

                    // Tính toán chiều cao nội dung chi tiết & giới hạn cuộn
                    int totalContentH = curDetailY - (detailViewY + 2);
                    int limDetail = totalContentH - detailViewH;
                    if (limDetail < 0) limDetail = 0;
                    skillDetailList.cmxLim = limDetail;
                    if (skillDetailList.cmtoX > limDetail) skillDetailList.cmtoX = limDetail;
                    if (skillDetailList.cmtoX < 0) skillDetailList.cmtoX = 0;
                    if (skillDetailList.cmx > limDetail) skillDetailList.cmx = limDetail;
                    if (skillDetailList.cmx < 0) skillDetailList.cmx = 0;

                    GameCanvas.resetTrans(g);
                    g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

                    if (skillDetailList.cmxLim > 0) {
                        scrollSkill.setInfo(rightPaneX + rightPaneW - 6, detailViewY, detailViewH, 0xFF8800);
                        scrollSkill.setYScrool(skillDetailList.cmx, skillDetailList.cmxLim);
                        scrollSkill.paint(g);
                    }
                }

                // Các nút hành động phía dưới
                int btnY = rightPaneY + rightPaneH - 24;
                if (canAddPoint) {
                    int btnCongX = rightPaneX + 10;
                    int btnCongW = (rightPaneW - 26) / 2;
                    AvMain.paintRect(g, btnCongX, btnY, btnCongW, 20, (sbyte)1, 1);
                    mFont.tahoma_7b_black.drawStringAutoCenter(g, "+ Điểm (" + Player.pointSkill + ")", btnCongX + btnCongW / 2, btnY + 4, btnCongW - 4);

                    if (canAssignKey) {
                        int btnGanX = btnCongX + btnCongW + 6;
                        int btnGanW = btnCongW;
                        AvMain.paintRect(g, btnGanX, btnY, btnGanW, 20, (sbyte)1, 1);
                        mFont.tahoma_7b_black.drawStringAutoCenter(g, "Gán Phím", btnGanX + btnGanW / 2, btnY + 4, btnGanW - 4);
                    }
                } else if (canAssignKey) {
                    int btnGanX = rightPaneX + 10;
                    int btnGanW = System.Math.Min(100, rightPaneW - 20);
                    AvMain.paintRect(g, btnGanX, btnY, btnGanW, 20, (sbyte)1, 1);
                    mFont.tahoma_7b_black.drawStringAutoCenter(g, "Gán Phím Tắt", btnGanX + btnGanW / 2, btnY + 4, btnGanW - 4);
                }
            }
        }
    }

    private void paintTabQuestDual(mGraphics g) {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int leftPaneX = x + 10;
        int leftPaneY = y + 28;
        int leftPaneH = h - 34;

        AvMain.paintRect(g, leftPaneX, leftPaneY, leftPaneW, leftPaneH, (sbyte)0, 1);
        mFont.tahoma_7b_yellow.drawString(g, "DANH SÁCH NHIỆM VỤ", leftPaneX + leftPaneW / 2, leftPaneY + 4, 2);

        int listX = leftPaneX + 4;
        int listY = leftPaneY + 18;
        int listW = leftPaneW - 8;
        int listH = leftPaneH - 22;

        if (questList != null) {
            g.setClip(listX, listY, listW, listH);
            g.translate(0, -questList.cmx);

            if (Player.vecQuest != null) {
                for (int i = 0; i < Player.vecQuest.size(); i++) {
                    MainQuest q = (MainQuest)Player.vecQuest.elementAt(i);
                    if (q == null) continue;
                    int qy = listY + i * 34;
                    bool isSel = (focusPane == 1 && i == selectedQuestIndex);

                    AvMain.paintRect(g, listX, qy, listW, 32, (sbyte)(isSel ? 1 : 0), 3);

                    if (AvMain.fraQuest != null) {
                        AvMain.fraQuest.drawFrame(q.statusQuest + 1, listX + 12, qy + 16, 0, 3, g);
                    }

                    mFont.tahoma_7b_white.drawStringAuto(g, q.name + " (" + q.getMainSub() + ")", listX + 28, qy + 2, listW - 32, 0);
                    if (q.strNPC_Map != null && q.strNPC_Map.Length > 0) {
                        mFont.tahoma_7_white.drawStringAuto(g, q.strNPC_Map, listX + 28, qy + 16, listW - 32, 0);
                    }

                    if (isSel) {
                        g.setColor(0xFFFF00);
                        g.drawRect(listX - 1, qy - 1, listW + 1, 33);
                        if (AvMain.imgNenfocus != null) {
                            g.drawRegion(AvMain.imgNenfocus, 2, 2, listW, 32, 0, listX, qy, 0);
                        }
                    }
                }
            }

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (questList.cmxLim > 0) {
                scrollQuest.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                scrollQuest.setYScrool(questList.cmx, questList.cmxLim);
                scrollQuest.paint(g);
            }
        }

        int rightPaneX = leftPaneX + leftPaneW + 6;
        int rightPaneY = y + 28;
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int rightPaneH = h - 34;

        AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (sbyte)0, 1);

        if (Player.vecQuest != null && selectedQuestIndex >= 0 && selectedQuestIndex < Player.vecQuest.size()) {
            MainQuest curQ = (MainQuest)Player.vecQuest.elementAt(selectedQuestIndex);
            if (curQ != null) {
                mFont.tahoma_7b_yellow.drawStringAuto(g, curQ.name + " (" + curQ.getMainSub() + ")", rightPaneX + 14, rightPaneY + 8, rightPaneW - 24, 0);
                if (curQ.strNPC_Map != null && curQ.strNPC_Map.Length > 0) {
                    mFont.tahoma_7_blue.drawString(g, "Vị trí: " + curQ.strNPC_Map, rightPaneX + 14, rightPaneY + 24, 0);
                }

                int startGoalY = rightPaneY + 40;
                if (curQ.vecTypeQuest != null && curQ.vecTypeQuest.size() > 0) {
                    for (int m = 0; m < curQ.vecTypeQuest.size() && m < 4; m++) {
                        DataQuest goal = (DataQuest)curQ.vecTypeQuest.elementAt(m);
                        if (goal != null) {
                            string gTxt = "- " + goal.nameItem + ": " + goal.numCur + "/" + goal.numMax;
                            mFont.tahoma_7_white.drawString(g, gTxt, rightPaneX + 14, startGoalY + m * 14, 0);
                        }
                    }
                    startGoalY += curQ.vecTypeQuest.size() * 14;
                }

                if (curQ.strShowDialog != null && curQ.strShowDialog.Length > 0) {
                    string[] qLines = mFont.tahoma_7_white.splitFontArray(curQ.strShowDialog, rightPaneW - 28);
                    if (qLines != null) {
                        for (int li = 0; li < qLines.Length; li++) {
                            if (startGoalY + 14 > rightPaneY + rightPaneH - 28) break;
                            mFont.tahoma_7_white.drawString(g, qLines[li], rightPaneX + 14, startGoalY, 0);
                            startGoalY += 14;
                        }
                    }
                }

                int btnY = rightPaneY + rightPaneH - 24;
                AvMain.paintRect(g, rightPaneX + 14, btnY, 96, 18, (sbyte)1, 1);
                mFont.tahoma_7b_black.drawString(g, "Xem Chi Tiết", rightPaneX + 62, btnY + 3, 2);
            }
        }
    }

    private void paintTabChucNang(mGraphics g) {
        if (chucNangSubView == 0) {
            paintMenuChucNang(g);
        } else if (chucNangSubView == 1) {
            paintTabDanhHieu(g);
        } else if (chucNangSubView == 2) {
            paintTabPetFullGrid(g);
        }
    }
private void paintFeatureIcon(mGraphics g, int index, int x, int y) {
try {
if (index == 0) { // Danh Hiệu: dùng trực tiếp quick_16, không dùng icon
FrameImage fra0 = QuickMenu.getFraQuickMenu(16);
if (fra0 != null && fra0.imgFrame != null && fra0.imgFrame.image != null) {
fra0.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "DH", x, y - 5, 2);
}
return;
}

short iconId = getFeatureIconId(index);
if (iconId >= 0) {
MainImage img = ObjectData.getImageAll(iconId, ObjectData.hashImageItemOther, (short)9000);
if (img != null && img.img != null) {
g.drawImage(img.img, x, y, 3);
return;
} else if (AvMain.imgLoadImage != null) {
AvMain.imgLoadImage.drawFrame(GameCanvas.gameTick % AvMain.imgLoadImage.nFrame, x, y, 0, 3, g);
return;
}
}

if (index == 1) { // Thú Cưng
FrameImage fra1 = (QuickMenu.fraQuickMenu != null && 15 >= 0 && 15 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[15] : null);
if (fra1 != null && fra1.imgFrame != null && fra1.imgFrame.image != null) {
fra1.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraBorderClan2 != null) {
AvMain.fraBorderClan2.drawFrameNew(GameCanvas.gameTick / 4 % AvMain.fraBorderClan2.maxNumFrame, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "PET", x, y - 5, 2);
}
} else if (index == 2) { // Băng Hải Tặc
FrameImage fra2 = (QuickMenu.fraQuickMenu != null && 10 >= 0 && 10 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[10] : null);
if (fra2 != null && fra2.imgFrame != null && fra2.imgFrame.image != null) {
fra2.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraBorderClan != null) {
AvMain.fraBorderClan.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "CLAN", x, y - 5, 2);
}
} else if (index == 3) { // Bạn Bè & Kẻ Thù
FrameImage fra3 = (QuickMenu.fraQuickMenu != null && 0 >= 0 && 0 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[0] : null);
if (fra3 != null && fra3.imgFrame != null && fra3.imgFrame.image != null) {
fra3.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraStatusOnline != null) {
AvMain.fraStatusOnline.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "FR", x, y - 5, 2);
}
} else if (index == 4) { // Đội Ngũ
FrameImage fra4 = (QuickMenu.fraQuickMenu != null && 6 >= 0 && 6 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[6] : null);
if (fra4 != null && fra4.imgFrame != null && fra4.imgFrame.image != null) {
fra4.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraBorderClan2 != null) {
AvMain.fraBorderClan2.drawFrameNew(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "PT", x, y - 5, 2);
}
} else if (index == 5) { // Trạng Thái PK
FrameImage fra5 = (QuickMenu.fraQuickMenu != null && 3 >= 0 && 3 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[3] : null);
if (fra5 != null && fra5.imgFrame != null && fra5.imgFrame.image != null) {
fra5.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraPk != null) {
AvMain.fraPk.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "PK", x, y - 5, 2);
}
} else if (index == 6) { // Sư Đồ
FrameImage fra6 = (QuickMenu.fraQuickMenu != null && 14 >= 0 && 14 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[14] : null);
if (fra6 != null && fra6.imgFrame != null && fra6.imgFrame.image != null) {
fra6.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraPirate != null) {
AvMain.fraPirate.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "SD", x, y - 5, 2);
}
} else if (index == 7) { // Chợ Đấu Giá
FrameImage fra7 = (QuickMenu.fraQuickMenu != null && 13 >= 0 && 13 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[13] : null);
if (fra7 != null && fra7.imgFrame != null && fra7.imgFrame.image != null) {
fra7.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraMoney != null) {
AvMain.fraMoney.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "CHỢ", x, y - 5, 2);
}
} else if (index == 8) { // Kênh Thế Giới & Loa
FrameImage fra8 = (QuickMenu.fraQuickMenu != null && 8 >= 0 && 8 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[8] : null);
if (fra8 != null && fra8.imgFrame != null && fra8.imgFrame.image != null) {
fra8.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fratf != null) {
AvMain.fratf.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "CHAT", x, y - 5, 2);
}
} else if (index == 9) { // Set Trang Bị
FrameImage fra9 = (QuickMenu.fraQuickMenu != null && 12 >= 0 && 12 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[12] : null);
if (fra9 != null && fra9.imgFrame != null && fra9.imgFrame.image != null) {
fra9.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraUniform != null) {
AvMain.fraUniform.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "SET", x, y - 5, 2);
}
} else if (index == 10) { // Auto Game
FrameImage fra10 = (QuickMenu.fraQuickMenu != null && 4 >= 0 && 4 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[4] : null);
if (fra10 != null && fra10.imgFrame != null && fra10.imgFrame.image != null) {
fra10.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraAutoFire != null) {
AvMain.fraAutoFire.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "AUTO", x, y - 5, 2);
}
} else if (index == 11) { // Tiện Ích MOD
FrameImage fraMod = (AvMain.fraAutoFire != null) ? AvMain.fraAutoFire : AvMain.fraCheck;
if (fraMod != null) {
int fr = (fraMod == AvMain.fraAutoFire) ? ((GameCanvas.gameTick / 6) % 3) : 1;
fraMod.drawFrame(fr, x, y, 0, 3, g);
} else {
FrameImage fra1 = (QuickMenu.fraQuickMenu != null && 1 >= 0 && 1 < QuickMenu.fraQuickMenu.Length ? QuickMenu.fraQuickMenu[1] : null);
if (fra1 != null && fra1.imgFrame != null && fra1.imgFrame.image != null) {
fra1.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "MOD", x, y - 5, 2);
}
}
} else {
mFont.tahoma_7b_yellow.drawString(g, "*", x, y - 5, 2);
}
} catch (Exception) {
}
}

private void paintMenuChucNang(mGraphics g) {
int paneX = x + 10;
int paneY = y + 28;
int paneW = w - 20;
int paneH = h - 34;

AvMain.paintRect(g, paneX, paneY, paneW, paneH, (sbyte)0, 1);

mFont.tahoma_7b_yellow.drawString(g, "CHỨC NĂNG HỆ THỐNG", paneX + paneW / 2, paneY + 5, 2);
g.setColor(0x886633);
g.drawLine(paneX + 16, paneY + 19, paneX + paneW - 16, paneY + 19);

int listX = paneX + 6;
int listY = paneY + 23;
int listW = paneW - 12;
int listH = paneH - 29;

if (featureMenuList == null) {
initTabFeatureMenu();
}

int featureCount = getFeatureItemCount();

        if (featureMenuList != null) {
            g.setClip(listX, listY, listW, listH);
            g.translate(0, -featureMenuList.cmx);

            int cols = isWide ? 2 : 1;
            int colGap = 6;
            int itemW = (cols == 2) ? ((listW - colGap) / 2) : listW;
            int itemH = (cols == 2) ? 38 : 42;
            int icBoxSize = (cols == 2) ? 30 : 34;

            for (int i = 0; i < featureCount; i++) {
                try {
                    int col = i % cols;
                    int row = i / cols;
                    int itemX = listX + col * (itemW + colGap);
                    int itemY = listY + row * (itemH + 4);
                    bool isSel = (focusPane == 1 && i == selectedChucNangIndex);

                    // Nền hàng item: index 0 khi chọn (sáng hơn), index 3 khi không chọn (tối, tương phản rõ trên panel nền 1)
                    AvMain.paintRect(g, itemX + 2, itemY + 2, itemW - 4, itemH - 4, (sbyte)(isSel ? 1 : 0), (sbyte)(isSel ? 0 : 3));
                    if (isSel) {
                        g.setColor(0xFFFF00);
                        g.drawRect(itemX + 1, itemY + 1, itemW - 3, itemH - 3);
                        if (AvMain.imgNenfocus != null) {
                            g.drawRegion(AvMain.imgNenfocus, 2, 2, System.Math.Min(itemW - 4, 32), System.Math.Min(itemH - 4, 32), 0, itemX + 2, itemY + 2, 0);
                        }
                    }

                    // Ô chứa Icon PNG ở đầu hàng
                    int icBoxX = itemX + 4;
                    int icBoxY = itemY + (itemH - icBoxSize) / 2;
                    AvMain.paintRect(g, icBoxX, icBoxY, icBoxSize, icBoxSize, (sbyte)0, (sbyte)(isSel ? 3 : 1));

                    int icCenterX = icBoxX + icBoxSize / 2;
                    int icCenterY = icBoxY + icBoxSize / 2;

                    paintFeatureIcon(g, i, icCenterX, icCenterY);

                    // Tên chức năng và thông tin trạng thái
                    string category = getFeatureCategory(i);
                    string title = getFeatureTitle(i);
                    string subText = getFeatureSubText(i);

                    int textX = itemX + icBoxSize + 8;
                    int maxTextW = itemW - icBoxSize - 22;

                    if (cols == 2) {
                        // Chế độ màn hình ngang 2 cột
                        mFont.tahoma_7_yellow.drawString(g, category + " ", textX, itemY + 3, 0);
                        int badgeW = mFont.tahoma_7_yellow.getWidth(category + " ");
                        (isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white).drawString(g, title, textX + badgeW, itemY + 3, 0);

                        string subShort = subText;
                        if (mFont.tahoma_7_white.getWidth(subShort) > maxTextW && subShort.Length > 22) {
                            subShort = subShort.Substring(0, 20) + "...";
                        }
                        (isSel ? mFont.tahoma_7_yellow : mFont.tahoma_7_white).drawString(g, subShort, textX, itemY + 19, 0);
                    } else {
                        // Chế độ màn hình dọc 1 cột
                        mFont.tahoma_7_yellow.drawString(g, category + " ", textX, itemY + 5, 0);
                        int badgeW = mFont.tahoma_7_yellow.getWidth(category + " ");
                        (isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white).drawString(g, title, textX + badgeW, itemY + 5, 0);
                        (isSel ? mFont.tahoma_7_yellow : mFont.tahoma_7_white).drawString(g, subText, textX, itemY + 22, 0);
                    }

                    (isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7_white).drawString(g, ">", itemX + itemW - 8, itemY + itemH / 2 - 4, 2);
                } catch (Exception) {}
            }

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (featureMenuList.cmxLim > 0) {
                scrollFeatureMenu.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                scrollFeatureMenu.setYScrool(featureMenuList.cmx, featureMenuList.cmxLim);
                scrollFeatureMenu.paint(g);
            }
        }
    }

    private void paintTabDanhHieu(mGraphics g) {
        int paneX = x + 10;
        int paneY = y + 28;
        int paneW = w - 20;
        int paneH = h - 34;

        AvMain.paintRect(g, paneX, paneY, paneW, paneH, (sbyte)0, 1);

        // Nút Quay Lại mini
        int backBtnX = paneX + 6;
        int backBtnY = paneY + 4;
        int backBtnW = 60;
        int backBtnH = 16;
        AvMain.paintRect(g, backBtnX, backBtnY, backBtnW, backBtnH, (sbyte)1, 1);
        mFont.tahoma_7b_black.drawString(g, "< Quay Lại", backBtnX + backBtnW / 2, backBtnY + 2, 2);

        mFont.tahoma_7b_yellow.drawString(g, "DANH HIỆU NHÂN VẬT", paneX + paneW / 2, paneY + 5, 2);

        int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
        mFont.tahoma_7_white.drawString(g, "(" + dhCount + ")", paneX + paneW - 10, paneY + 5, 1);

        if (isWide) {
            int listX = paneX + 6;
            int listY = paneY + 24;
            int listW = (paneW - 18) * 55 / 100;
            int listH = paneH - 30;

            AvMain.paintRect(g, listX, listY, listW, listH, (sbyte)0, 1);

            if (danhHieuList != null) {
                g.setClip(listX, listY, listW, listH);
                g.translate(0, -danhHieuList.cmx);

                int itemH = 38;
                for (int i = 0; i < dhCount; i++) {
                    DanhHieuInfo dh = (DanhHieuInfo)Player.vecDanhHieu.elementAt(i);
                    if (dh == null) continue;

                    int itemY = listY + i * itemH;
                    bool isSel = (i == selectedDanhHieuIndex);

                    AvMain.paintRect(g, listX + 2, itemY + 2, listW - 4, itemH - 4, (sbyte)(isSel ? 1 : 0), 1);
                    if (isSel) {
                        g.setColor(0xFFFF00);
                        g.drawRect(listX + 1, itemY + 1, listW - 3, itemH - 3);
                        if (AvMain.imgNenfocus != null) {
                            g.drawRegion(AvMain.imgNenfocus, 2, 2, listW - 4, itemH - 4, 0, listX + 2, itemY + 2, 0);
                        }
                    }

                    mFont titleFont = (dh.state == 2) ? mFont.tahoma_7b_green : (isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white);
                    titleFont.drawString(g, dh.name, listX + 8, itemY + 4, 0);

                    if (dh.state == 2) {
                        mFont.tahoma_7b_green.drawString(g, "[Đang Dùng]", listX + listW - 8, itemY + 4, 1);
                    } else if (dh.state == 1) {
                        mFont.tahoma_7_yellow.drawString(g, "[Đã Có]", listX + listW - 8, itemY + 4, 1);
                    } else {
                        mFont.tahoma_7_white.drawString(g, "[Chưa Có]", listX + listW - 8, itemY + 4, 1);
                    }

                    string firstOpt = (dh.optionsStr != null && dh.optionsStr.Length > 0) ? dh.optionsStr : "Không có thuộc tính";
                    int nlIdx = firstOpt.IndexOf('\n');
                    if (nlIdx != -1) firstOpt = firstOpt.Substring(0, nlIdx);
                    mFont.tahoma_7_white.drawString(g, firstOpt, listX + 8, itemY + 18, 0);
                }

                if (dhCount == 0) {
                    mFont.tahoma_7_white.drawString(g, "Đang tải danh hiệu...", listX + listW / 2, listY + listH / 2 - 6, 2);
                }

                GameCanvas.resetTrans(g);
                g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

                if (danhHieuList.cmxLim > 0) {
                    scrollDanhHieu.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                    scrollDanhHieu.setYScrool(danhHieuList.cmx, danhHieuList.cmxLim);
                    scrollDanhHieu.paint(g);
                }
            }

            // Bảng Chi Tiết Danh Hiệu bên phải
            int detailX = listX + listW + 6;
            int detailY = listY;
            int detailW = paneW - listW - 18;
            int detailH = listH;

            AvMain.paintRect(g, detailX, detailY, detailW, detailH, (sbyte)0, 1);

            if (selectedDanhHieuInfo != null) {
                mFont.tahoma_7b_yellow.drawString(g, "★ " + selectedDanhHieuInfo.name + " ★", detailX + detailW / 2, detailY + 5, 2);

                // Khung Showcase Hiệu Ứng Động
                int stageX = detailX + 8;
                int stageY = detailY + 18;
                int stageW = detailW - 16;
                int stageH = 46;
                AvMain.paintRect(g, stageX, stageY, stageW, stageH, (sbyte)1, 1);
                g.setColor(0xFFA500);
                g.drawRect(stageX, stageY, stageW, stageH);

                if (selectedDanhHieuInfo.idEff > 0) {
                    if (previewTitleEff == null || lastPreviewTitleEffId != selectedDanhHieuInfo.idEff) {
                        lastPreviewTitleEffId = selectedDanhHieuInfo.idEff;
                        previewTitleEff = new DataSkillEff(selectedDanhHieuInfo.idEff, -1, (sbyte)0, (sbyte)0);
                    }
                    if (previewTitleEff != null) {
                        previewTitleEff.paintAutoCenter(g, stageX, stageY, stageW, stageH);
                    }
                }

                int statusY = stageY + stageH + 4;
                if (selectedDanhHieuInfo.state == 2) {
                    mFont.tahoma_7b_green.drawString(g, "[★ ĐANG KÍCH HOẠT ★]", detailX + detailW / 2, statusY, 2);
                } else if (selectedDanhHieuInfo.state == 1) {
                    mFont.tahoma_7_yellow.drawString(g, "[✔ ĐÃ SỞ HỮU]", detailX + detailW / 2, statusY, 2);
                } else {
                    mFont.tahoma_7_white.drawString(g, "[✖ CHƯA SỞ HỮU]", detailX + detailW / 2, statusY, 2);
                }

                g.setColor(0x664422);
                g.drawLine(detailX + 8, statusY + 13, detailX + detailW - 8, statusY + 13);

                int actBtnAreaH = (selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) ? 26 : 4;
                int maxTextY = detailY + detailH - actBtnAreaH;
                int curTextY = statusY + 17;

                if (selectedDanhHieuInfo.optionsStr != null && selectedDanhHieuInfo.optionsStr.Length > 0) {
                    string[] wrapped = mFont.tahoma_7_white.splitFontArray(selectedDanhHieuInfo.optionsStr, detailW - 16);
                    if (wrapped != null) {
                        for (int wIdx = 0; wIdx < wrapped.Length && curTextY + 11 <= maxTextY; wIdx++) {
                            string line = wrapped[wIdx];
                            if (line.StartsWith("Hạn") || line.StartsWith("Còn") || line.IndexOf("ngày") != -1 || line.IndexOf("giờ") != -1 || line.IndexOf("phút") != -1) {
                                mFont.tahoma_7_yellow.drawString(g, line, detailX + 8, curTextY, 0);
                            } else if (line.StartsWith("Thuộc tính:") || line.StartsWith("★")) {
                                mFont.tahoma_7b_yellow.drawString(g, line, detailX + 8, curTextY, 0);
                            } else {
                                mFont.tahoma_7_white.drawString(g, line, detailX + 8, curTextY, 0);
                            }
                            curTextY += 11;
                        }
                    }
                }

                // Nút Hành Động (Server gửi về)
                if (selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) {
                    int actBtnY = detailY + detailH - 22;
                    int actBtnW = detailW - 20;
                    int actBtnX = detailX + 10;

                    for (int b = 0; b < selectedDanhHieuInfo.actionButtons.size(); b++) {
                        TitleActionBtn btn = (TitleActionBtn)selectedDanhHieuInfo.actionButtons.elementAt(b);
                        if (btn == null) continue;

                        AvMain.paintRect(g, actBtnX, actBtnY, actBtnW, 18, (sbyte)1, 1);
                        if (btn.style == 1) {
                            mFont.tahoma_7b_green.drawString(g, btn.name, actBtnX + actBtnW / 2, actBtnY + 3, 2);
                        } else if (btn.style == 2) {
                            mFont.tahoma_7b_red.drawString(g, btn.name, actBtnX + actBtnW / 2, actBtnY + 3, 2);
                        } else {
                            mFont.tahoma_7b_yellow.drawString(g, btn.name, actBtnX + actBtnW / 2, actBtnY + 3, 2);
                        }
                    }
                }
            } else {
                mFont.tahoma_7_white.drawString(g, "Chọn một danh hiệu để xem chi tiết", detailX + detailW / 2, detailY + detailH / 2 - 6, 2);
            }
        } else {
            // Màn hình hẹp (Narrow)
            int listX = paneX + 6;
            int listY = paneY + 24;
            int listW = paneW - 12;
            int listH = paneH - 30;

            AvMain.paintRect(g, listX, listY, listW, listH, (sbyte)0, 1);

            if (danhHieuList != null) {
                g.setClip(listX, listY, listW, listH);
                g.translate(0, -danhHieuList.cmx);

                int itemH = 38;
                for (int i = 0; i < dhCount; i++) {
                    DanhHieuInfo dh = (DanhHieuInfo)Player.vecDanhHieu.elementAt(i);
                    if (dh == null) continue;

                    int itemY = listY + i * itemH;
                    bool isSel = (i == selectedDanhHieuIndex);

                    AvMain.paintRect(g, listX + 2, itemY + 2, listW - 4, itemH - 4, (sbyte)(isSel ? 1 : 0), 1);
                    mFont titleFont = (dh.state == 2) ? mFont.tahoma_7b_green : (isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white);
                    titleFont.drawString(g, dh.name, listX + 8, itemY + 4, 0);

                    if (dh.state == 2) {
                        mFont.tahoma_7b_green.drawString(g, "[Đang Dùng]", listX + listW - 8, itemY + 4, 1);
                    } else if (dh.state == 1) {
                        mFont.tahoma_7_yellow.drawString(g, "[Đã Có]", listX + listW - 8, itemY + 4, 1);
                    } else {
                        mFont.tahoma_7_white.drawString(g, "[Chưa Có]", listX + listW - 8, itemY + 4, 1);
                    }

                    string firstOpt = (dh.optionsStr != null && dh.optionsStr.Length > 0) ? dh.optionsStr : "Không có thuộc tính";
                    int nlIdx = firstOpt.IndexOf('\n');
                    if (nlIdx != -1) firstOpt = firstOpt.Substring(0, nlIdx);
                    mFont.tahoma_7_white.drawString(g, firstOpt, listX + 8, itemY + 18, 0);
                }

                if (dhCount == 0) {
                    mFont.tahoma_7_white.drawString(g, "Đang tải danh hiệu...", listX + listW / 2, listY + listH / 2 - 6, 2);
                }

                GameCanvas.resetTrans(g);
                g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

                if (danhHieuList.cmxLim > 0) {
                    scrollDanhHieu.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                    scrollDanhHieu.setYScrool(danhHieuList.cmx, danhHieuList.cmxLim);
                    scrollDanhHieu.paint(g);
                }
            }
        }
    }

    private void paintTabPetFullGrid(mGraphics g) {
        int paneX = x + 10;
        int paneY = y + 28;
        int paneW = w - 20;
        int paneH = h - 34;

        AvMain.paintRect(g, paneX, paneY, paneW, paneH, (sbyte)0, 1);

        // Nút Quay Lại mini
        int backBtnX = paneX + 6;
        int backBtnY = paneY + 4;
        int backBtnW = 60;
        int backBtnH = 16;
        AvMain.paintRect(g, backBtnX, backBtnY, backBtnW, backBtnH, (sbyte)1, 1);
        mFont.tahoma_7b_black.drawString(g, "< Quay Lại", backBtnX + backBtnW / 2, backBtnY + 2, 2);

        int petCount = (Player.vecPet != null) ? Player.vecPet.size() : 0;
        string titleStr = "DANH SÁCH THÚ CƯNG (" + petCount + "/" + Player.maxInventory + ")";
        mFont.tahoma_7b_yellow.drawString(g, titleStr, paneX + paneW / 2, paneY + 6, 2);

        int gridX = paneX + 8;
        int gridY = paneY + 22;
        int gridW = paneW - 16;
        int gridH = paneH - 30;
        int slotSize = 28;
        int gap = 3;
        int slotStep = slotSize + gap;
        int cols = gridW / slotStep;
        if (cols < 1) cols = 1;

        if (petList != null) {
            g.setClip(gridX, gridY, gridW, gridH);
            g.translate(0, -petList.cmx);

            int totalSlots = System.Math.Max(Player.maxInventory, System.Math.Max(petCount, cols * 4));
            int prevW = MainTab.wItem;
            MainTab.wItem = slotSize;

            for (int i = 0; i < totalSlots; i++) {
                int col = i % cols;
                int row = i / cols;
                int slotX = gridX + col * slotStep;
                int slotY = gridY + row * slotStep;
                bool isSel = (focusPane == 1 && i == selectedPetIndex);

                AvMain.paintRect(g, slotX, slotY, slotSize, slotSize, (sbyte)(isSel ? 1 : 0), 3);

                if (i < petCount) {
                    MainItem petItem = (MainItem)Player.vecPet.elementAt(i);
                    if (petItem != null) {
                        petItem.paint(g, slotX + slotSize / 2, slotY + slotSize / 2, slotSize);

                        if (petItem.LvUpgrade > 0) {
                            mFont.tahoma_7b_green.drawString(g, "+" + petItem.LvUpgrade, slotX + 2, slotY + 1, 0);
                        }
                        if (petItem.colorName == 1) {
                            mFont.tahoma_7_yellow.drawString(g, "★", slotX + slotSize - 8, slotY + 1, 0);
                        }
                    }
                }

                if (isSel) {
                    g.setColor(0xFFFF00);
                    g.drawRect(slotX - 1, slotY - 1, slotSize + 1, slotSize + 1);
                    g.drawRect(slotX - 2, slotY - 2, slotSize + 3, slotSize + 3);
                    if (AvMain.imgNenfocus != null) {
                        g.drawRegion(AvMain.imgNenfocus, 2, 2, slotSize, slotSize, 0, slotX, slotY, 0);
                    }
                }
            }
            MainTab.wItem = prevW;

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (petList.cmxLim > 0) {
                scrollPet.setInfo(gridX + gridW - 3, gridY, gridH, 0xFF8800);
                scrollPet.setYScrool(petList.cmx, petList.cmxLim);
                scrollPet.paint(g);
            }
        }

        if (selectedPetInfo != null) {
            int infoW = (selectedPetInfo.wInfo > 0) ? selectedPetInfo.wInfo : 140;
            int maxPopupH = System.Math.Max(60, System.Math.Min(h - 54, MotherCanvas.h - 54));
            int showH = selectedPetInfo.hInfo - selectedPetInfo.hRunInfo;
            if (showH <= 0 || showH > maxPopupH) showH = (selectedPetInfo.hInfo > 0) ? System.Math.Min(selectedPetInfo.hInfo, maxPopupH) : 60;
            if (showH < 30) showH = 30;

            int infoX, infoY;
            if (itemTouchX > 0 && itemTouchY > 0) {
                infoX = itemTouchX + 10;
                if (infoX + infoW > x + w - 8) infoX = itemTouchX - infoW - 10;
                if (infoX < x + 8) infoX = x + 8;
                infoY = itemTouchY - showH / 2;
                if (infoY + showH + 24 > y + h - 6) infoY = y + h - 6 - showH - 24;
                if (infoY < y + 28) infoY = y + 28;
            } else {
                infoX = x + 10;
                infoY = y + 28 + (h - 34 - showH - 24) / 2;
                if (infoY < y + 28) infoY = y + 28;
            }

            MainTab.paintInfoEveryWhere(g, selectedPetInfo, null, 0, infoX, infoY, infoW, showH, false, null, 0);

            if (GameCanvas.isTouch) {
                int btnY = infoY + showH + 3;
                bool isEquipped = (selectedPetInfo.colorName == 1);
                AvMain.paintRect(g, infoX, btnY, 60, 18, (sbyte)1, 1);
                mFont.tahoma_7b_black.drawString(g, isEquipped ? "Tháo" : "Dùng", infoX + 30, btnY + 3, 2);
            }
        }
    }

    public override void updatePointer() {
        base.updatePointer();

        int tabCount = 5;
        int tabW = (w - 44) / tabCount;
        int tabH = 18;
        int startX = x + 8;
        int startY = y + 8;

        if (GameCanvas.isPointerSelect) {
            for (int i = 0; i < tabCount; i++) {
                int curX = startX + i * tabW;
                if (GameCanvas.isPoint(curX, startY, tabW - 2, tabH)) {
                    GameCanvas.isPointerSelect = false;
                    switchTab(i);
                    return;
                }
            }
        }

        if (curMainTab == 0) {
            int leftPaneW = isWide ? 160 : (w * 45 / 100);
            int paneX = x + 10;
            int equipAreaX = paneX + 4;
            int equipAreaY = y + 28 + 28;
            int equipAreaW = leftPaneW - 8;
            int equipAreaH = 120;

            if (GameCanvas.isPointerDown || GameCanvas.isPointerMove) {
                if (!isDraggingEquip) {
                    if (GameCanvas.isPoint(equipAreaX, equipAreaY, equipAreaW, equipAreaH)) {
                        isDraggingEquip = true;
                        equipDragStartY = GameCanvas.py;
                        equipDragStartScrollY = equipScrollY;
                    }
                } else {
                    int deltaY = GameCanvas.py - equipDragStartY;
                    if (deltaY < -15 && equipSubTab == 0) {
                        // Vuốt lên -> chuyển sang Thần Trang
                        equipSubTab = 1;
                        equipToY = EQUIP_PAGE_H;
                        selectedEquipIndex = 8;
                        selectItem((MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex), 0, 0);
                        updateSoftKeys();
                    } else if (deltaY > 15 && equipSubTab == 1) {
                        // Vuốt xuống -> chuyển về Trang Bị Thường
                        equipSubTab = 0;
                        equipToY = 0;
                        selectedEquipIndex = 0;
                        selectItem((MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex), 0, 0);
                        updateSoftKeys();
                    }
                }
            } else {
                isDraggingEquip = false;
            }

            if (invenList != null) invenList.update_Pos_UP_DOWN();
            if (leftStatList != null) leftStatList.update_Pos_UP_DOWN();
        } else if (curMainTab == 1) {
            if (infoList != null) infoList.update_Pos_UP_DOWN();
            if (attriList != null) attriList.update_Pos_UP_DOWN();
        } else if (curMainTab == 2) {
            if (skillList != null) skillList.update_Pos_UP_DOWN();
            if (skillDetailList != null) skillDetailList.update_Pos_UP_DOWN();
        } else if (questList != null && curMainTab == 3) {
            questList.update_Pos_UP_DOWN();
        } else if (curMainTab == 4) {
            if (chucNangSubView == 0 && featureMenuList != null) featureMenuList.update_Pos_UP_DOWN();
            else if (chucNangSubView == 1 && danhHieuList != null) danhHieuList.update_Pos_UP_DOWN();
            else if (chucNangSubView == 2 && petList != null) petList.update_Pos_UP_DOWN();
        }

        if (GameCanvas.isPointerSelect) {
            if (curMainTab == 0) handlePointerTab0();
            else if (curMainTab == 1) handlePointerTab1();
            else if (curMainTab == 2) handlePointerTab2();
            else if (curMainTab == 3) handlePointerTab3();
            else if (curMainTab == 4) handlePointerTab4();
        }

        if (cmdClose != null && cmdClose.perform(GameCanvas.px, GameCanvas.py)) {
            close();
        }
    }

    private void handlePointerTab0() {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int rightPaneX = x + 10 + leftPaneW + 6;
        int rightPaneY = y + 28;
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int rightPaneH = h - 34;

        int paneX = x + 10;
        int equipAreaX = paneX + 4;
        int equipAreaY = y + 28 + 28;
        int equipAreaW = leftPaneW - 8;
        int tabBtnW = (equipAreaW - 6) / 2;
        int tabBtnH = 13;
        int tabBtnY = equipAreaY + 1;

        // 1. Kiem tra bam 2 nut chuyen tab "Trang Bi" / "Than Trang"
        if (GameCanvas.isPoint(equipAreaX + 2, tabBtnY, tabBtnW, tabBtnH)) {
            GameCanvas.isPointerSelect = false;
            equipSubTab = 0;
            equipToY = 0;
            selectedEquipIndex = 0;
            selectItem((MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex), 0, 0);
            updateSoftKeys();
            return;
        }
        if (GameCanvas.isPoint(equipAreaX + 2 + tabBtnW + 2, tabBtnY, tabBtnW, tabBtnH)) {
            GameCanvas.isPointerSelect = false;
            equipSubTab = 1;
            equipToY = EQUIP_PAGE_H;
            selectedEquipIndex = 8;
            selectItem((MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex), 0, 0);
            updateSoftKeys();
            return;
        }

        // 2. Kiem tra filter bar
        int fw = (rightPaneW - 8) / FILTER_NAMES.Length;
        int fy = rightPaneY + 4;
        for (int f = 0; f < FILTER_NAMES.Length; f++) {
            int fx = rightPaneX + 4 + f * fw;
            if (GameCanvas.isPoint(fx, fy, fw - 2, 16)) {
                GameCanvas.isPointerSelect = false;
                selectItem(null, 0, 0);
                selectedInvenIndex = -1;
                curInvenFilter = f;
                focusPane = 3;
                updateFilteredItems();
                if (this.invenList != null) {
                    this.invenList.cmx = 0;
                    this.invenList.cmtoX = 0;
                }
                updateSoftKeys();
                return;
            }
        }

        // 3. Kiem tra nut popup tooltip
        if (selectedItemInfo != null) {
            int infoW = (selectedItemInfo.wInfo > 0) ? selectedItemInfo.wInfo : 140;
            int maxPopupH = System.Math.Max(60, System.Math.Min(h - 54, MotherCanvas.h - 54));
            int showH = selectedItemInfo.hInfo - selectedItemInfo.hRunInfo;
            if (showH <= 0 || showH > maxPopupH) showH = (selectedItemInfo.hInfo > 0) ? System.Math.Min(selectedItemInfo.hInfo, maxPopupH) : 60;
            if (showH < 30) showH = 30;

            int infoX, infoY;
            int spaceLeft = x;
            int spaceRight = MotherCanvas.w - (x + w);

            if (spaceLeft >= infoW + 6) {
                infoX = x - infoW - 6;
            } else if (spaceRight >= infoW + 6) {
                infoX = x + w + 6;
            } else if (focusPane == 1) {
                infoX = x + 10;
            } else {
                infoX = rightPaneX + 4;
            }

            if (itemTouchY > 0) {
                infoY = itemTouchY - showH / 2;
                if (infoY + showH + 24 > y + h - 6) infoY = y + h - 6 - showH - 24;
                if (infoY < y + 28) infoY = y + 28;
            } else {
                infoY = y + 28 + (h - 34 - showH - 24) / 2;
                if (infoY < y + 28) infoY = y + 28;
            }
            int popBtnY = infoY + showH + 3;

            if (focusPane == 0) {
                if (GameCanvas.isPoint(infoX, popBtnY, 60, 18)) {
                    GameCanvas.isPointerSelect = false;
                    int popLeftSlotX = paneX + 7;
                    int popRightSlotX = paneX + leftPaneW - 25 - 7;
                    int popStartSlotY = equipAreaY + 18;
                    int popSlotGapY = 25;
                    int row = (selectedEquipIndex % 8) / 2;
                    bool isLeft = (selectedEquipIndex % 2 == 0);
                    int px = isLeft ? (popLeftSlotX + 13) : (popRightSlotX + 13);
                    int py = popStartSlotY + row * popSlotGapY + 13;
                    changeEquip(selectedEquipIndex, px, py);
                    return;
                }
            } else {
                if (GameCanvas.tabInven == null) {
                    GameCanvas.tabInven = new TabInventory(T.tabInven, Player.vecInventory, (sbyte)0, MainTab.xTab);
                    GameCanvas.tabInven.initCmd();
                }
                GameCanvas.tabInven.itemCur = selectedItemInfo;
                GameCanvas.tabInven.IdSelect = Player.vecInventory.IndexOf(selectedItemInfo);
                mVector mActions = selectedItemInfo.getActionInven((sbyte)0);

                if (mActions != null && mActions.size() > 0) {
                    if (mActions.size() == 1) {
                        if (GameCanvas.isPoint(infoX, popBtnY, 60, 18)) {
                            GameCanvas.isPointerSelect = false;
                            ((iCommand)mActions.elementAt(0)).perform();
                            isRefresh = true;
                            return;
                        }
                    } else if (mActions.size() == 2) {
                        if (GameCanvas.isPoint(infoX, popBtnY, 52, 18)) {
                            GameCanvas.isPointerSelect = false;
                            ((iCommand)mActions.elementAt(0)).perform();
                            isRefresh = true;
                            return;
                        }
                        if (GameCanvas.isPoint(infoX + 56, popBtnY, 52, 18)) {
                            GameCanvas.isPointerSelect = false;
                            ((iCommand)mActions.elementAt(1)).perform();
                            isRefresh = true;
                            return;
                        }
                    } else {
                        if (GameCanvas.isPoint(infoX, popBtnY, 52, 18)) {
                            GameCanvas.isPointerSelect = false;
                            ((iCommand)mActions.elementAt(0)).perform();
                            isRefresh = true;
                            return;
                        }
                        if (GameCanvas.isPoint(infoX + 56, popBtnY, 52, 18)) {
                            GameCanvas.isPointerSelect = false;
                            doMenuAction();
                            return;
                        }
                    }
                }
            }
        }

        // 4. Kiem tra o trang bi (8 o trai)
        int eqSlotSize = 25;
        int leftSlotX = paneX + 7;
        int rightSlotX = paneX + leftPaneW - eqSlotSize - 7;
        int startSlotY = equipAreaY + 18;
        int slotGapY = 25;

        for (int i = 0; i < 4; i++) {
            int sy = startSlotY + i * slotGapY;
            if (GameCanvas.isPoint(leftSlotX, sy, eqSlotSize, eqSlotSize)) {
                GameCanvas.isPointerSelect = false;
                focusPane = 0;
                int slotIdx = (equipSubTab == 0) ? (i * 2) : (8 + i * 2);
                MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + slotIdx);
                if (selectedEquipIndex == slotIdx && selectedItemInfo != null) {
                    changeEquip(slotIdx, leftSlotX + 13, sy + 13);
                } else {
                    selectedEquipIndex = slotIdx;
                    selectItem(eq, GameCanvas.px, GameCanvas.py);
                }
                return;
            }
            if (GameCanvas.isPoint(rightSlotX, sy, eqSlotSize, eqSlotSize)) {
                GameCanvas.isPointerSelect = false;
                focusPane = 0;
                int slotIdx = (equipSubTab == 0) ? (i * 2 + 1) : (8 + i * 2 + 1);
                MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + slotIdx);
                if (selectedEquipIndex == slotIdx && selectedItemInfo != null) {
                    changeEquip(slotIdx, rightSlotX + 13, sy + 13);
                } else {
                    selectedEquipIndex = slotIdx;
                    selectItem(eq, GameCanvas.px, GameCanvas.py);
                }
                return;
            }
        }

        // 5. Kiem tra o hanh trang (Right Pane)
        int gridX = rightPaneX + 4;
        int gridY = rightPaneY + 22;
        int gridW = rightPaneW - 8;
        int gridH = rightPaneH - 38;
        int cols = gridW / 30;
        if (cols < 1) cols = 1;

        // Kiem tra day 10 nut chuyen tab hanh trang [1]..[10]
        int tabBoxY = rightPaneY + rightPaneH - 15;
        int tabBoxH = 13;
        int pageBtnW = (rightPaneW >= 220) ? 14 : 12;
        int tabGap = (rightPaneW >= 220) ? 2 : 1;
        int tabStartX = rightPaneX + 4;
        for (int p = 0; p < 10; p++) {
            int bx = tabStartX + p * (pageBtnW + tabGap);
            if (GameCanvas.isPoint(bx - 1, tabBoxY - 2, pageBtnW + 2, tabBoxH + 4)) {
                setInvenPage(p);
                GameCanvas.isPointerSelect = false;
                return;
            }
        }

        if (GameCanvas.isPoint(gridX, gridY, gridW, gridH)) {
            int slotStep = 30;
            int gridOffsetX = (gridW - cols * slotStep) / 2;
            int relX = GameCanvas.px - gridX - gridOffsetX;
            int relY = GameCanvas.py - gridY + (invenList != null ? invenList.cmx : 0);
            if (relX >= 0) {
                int clickedCol = relX / slotStep;
                int clickedRow = relY / slotStep;

            if (clickedCol >= 0 && clickedCol < cols && clickedRow >= 0) {
                int clickedIdx = clickedRow * cols + clickedCol;
                if (clickedIdx >= 0 && clickedIdx < filteredItems.size()) {
                    GameCanvas.isPointerSelect = false;
                    focusPane = 1;
                    MainItem it = (MainItem)filteredItems.elementAt(clickedIdx);
                    if (selectedInvenIndex == clickedIdx && selectedItemInfo != null) {
                        doPrimaryAction();
                    } else {
                        selectedInvenIndex = clickedIdx;
                        selectItem(it, GameCanvas.px, GameCanvas.py);
                    }
                    return;
                } else {
                    selectedInvenIndex = -1;
                    selectItem(null, 0, 0);
                    return;
                }
            }
        }
        }

        if (selectedItemInfo != null) {
            selectItem(null, 0, 0);
            selectedInvenIndex = -1;
        }
    }

    private void handlePointerTab1() {
        int leftPaneW = isWide ? System.Math.Max(160, w * 48 / 100) : (w * 48 / 100);
        int leftPaneX = x + 10;
        int leftPaneY = y + 28;
        int leftPaneH = h - 34;

        int rightPaneX = leftPaneX + leftPaneW + 6;
        int rightPaneY = y + 28;
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int rightPaneH = h - 34;
        int rightListX = rightPaneX + 4;
        int rightListY = rightPaneY + 20;
        int rightListW = rightPaneW - 8;
        int rightListH = rightPaneH - 24;

        if (GameCanvas.isPoint(leftPaneX, leftPaneY, leftPaneW, leftPaneH)) {
            focusPane = 0;
            updateSoftKeys();
        }

        if (attriList != null && GameCanvas.isPoint(rightListX, rightListY, rightListW, rightListH)) {
            int numAtt = (Player.mAttribute != null && Player.mAttribute.Length > 0) ? Player.mAttribute.Length : 5;
            int curCardY = rightListY;

            for (int i = 0; i < numAtt; i++) {
                Main_Attribute att = (Player.mAttribute != null && i < Player.mAttribute.Length) ? Player.mAttribute[i] : null;
                int cardH = getAttriCardHeight(att, i);
                int cardX = rightListX + 2;
                int cardW = rightListW - 4;
                int scrCardY = curCardY - attriList.cmx;

                // Plus button bounds on right of card
                int btnW = (cardW < 135) ? 22 : 28;
                int btnH = 22;
                int btnX = cardX + cardW - btnW - 4;
                int btnY = scrCardY + 2;

                if (GameCanvas.isPoint(btnX - 4, btnY - 4, btnW + 8, btnH + 8)) {
                    GameCanvas.isPointerSelect = false;
                    focusPane = 1;
                    selectedAttIndex = i;
                    timeFocusAtt = 4;
                    addPotentialPoint(i);
                    return;
                }

                if (GameCanvas.isPoint(cardX, scrCardY, cardW, cardH)) {
                    GameCanvas.isPointerSelect = false;
                    focusPane = 1;
                    selectedAttIndex = i;
                    updateSoftKeys();
                    return;
                }

                curCardY += cardH + 4;
            }
        }
    }

    private void handlePointerTab2() {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int leftPaneX = x + 10;
        int listY = y + 28 + 18;
        int listW = leftPaneW - 8;

        if (validSkills != null && skillList != null) {
            for (int i = 0; i < validSkills.size(); i++) {
                int sy = listY + i * 34 - skillList.cmx;
                if (GameCanvas.isPoint(leftPaneX + 4, sy, listW, 32)) {
                    GameCanvas.isPointerSelect = false;
                    focusPane = 1;
                    if (selectedSkillIndex != i) {
                        selectedSkillIndex = i;
                        if (skillDetailList != null) { skillDetailList.cmx = 0; skillDetailList.cmtoX = 0; }
                    }
                    updateSoftKeys();
                    return;
                }
            }
        }

        int rightPaneX = leftPaneW + 6 + x + 10;
        int rightPaneY = y + 28;
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int rightPaneH = h - 34;
        int btnY = rightPaneY + rightPaneH - 24;

        if (validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
            Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
            if (curSk != null) {
                bool canAddPoint = (Player.pointSkill > 0 && curSk.Lv_RQ > 0 && curSk.Lv_RQ < Skill_Info.maxLv);
                bool canAssignHotkey = (curSk.Lv_RQ > 0 && (curSk.typeSkill == 1 || curSk.typeSkill == 2 || curSk.typeSkill == 4));

                if (canAddPoint) {
                    int btnCongX = rightPaneX + 10;
                    int btnCongW = (rightPaneW - 26) / 2;
                    if (GameCanvas.isPoint(btnCongX, btnY - 2, btnCongW, 22)) {
                        GameCanvas.isPointerSelect = false;
                        addPointSkill(curSk);
                        return;
                    }

                    if (canAssignHotkey) {
                        int btnGanX = btnCongX + btnCongW + 6;
                        int btnGanW = btnCongW;
                        if (GameCanvas.isPoint(btnGanX, btnY - 2, btnGanW, 22)) {
                            GameCanvas.isPointerSelect = false;
                            openSkillHotkeyMenu();
                            return;
                        }
                    }
                } else if (canAssignHotkey) {
                    int btnGanX = rightPaneX + 10;
                    int btnGanW = System.Math.Min(100, rightPaneW - 20);
                    if (GameCanvas.isPoint(btnGanX, btnY - 2, btnGanW, 22)) {
                        GameCanvas.isPointerSelect = false;
                        openSkillHotkeyMenu();
                        return;
                    }
                }
            }
        }
    }

    private void handlePointerTab3() {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int leftPaneX = x + 10;
        int listY = y + 28 + 18;
        int listW = leftPaneW - 8;

        if (Player.vecQuest != null && questList != null) {
            for (int i = 0; i < Player.vecQuest.size(); i++) {
                int qy = listY + i * 34 - questList.cmx;
                if (GameCanvas.isPoint(leftPaneX + 4, qy, listW, 32)) {
                    GameCanvas.isPointerSelect = false;
                    focusPane = 1;
                    selectedQuestIndex = i;
                    updateSoftKeys();
                    return;
                }
            }
        }

        int rightPaneX = leftPaneW + 6 + x + 10;
        int rightPaneY = y + 28;
        int rightPaneH = h - 34;
        int btnY = rightPaneY + rightPaneH - 24;

        if (GameCanvas.isPoint(rightPaneX + 14, btnY, 96, 18)) {
            GameCanvas.isPointerSelect = false;
            openQuestDetails();
            return;
        }
    }

    private void handlePointerTab4() {
        int paneX = x + 10;
        int paneY = y + 28;
        int paneW = w - 20;
        int paneH = h - 34;

        if (chucNangSubView == 0) {
            int listX = paneX + 6;
            int listY = paneY + 23;
            int listW = paneW - 12;
            int listH = paneH - 29;
            int featureCount = getFeatureItemCount();

            if (featureMenuList != null && GameCanvas.isPoint(listX, listY, listW, listH)) {
                int relX = GameCanvas.px - listX;
                int relY = GameCanvas.py - listY + featureMenuList.cmx;
                int cols = isWide ? 2 : 1;
                int colGap = 6;
                int itemW = (cols == 2) ? ((listW - colGap) / 2) : listW;
                int itemH = (cols == 2) ? 38 : 42;
                int clickedCol = relX / (itemW + colGap);
                int clickedRow = relY / (itemH + 4);

                if (clickedCol >= 0 && clickedCol < cols && clickedRow >= 0) {
                    int clickedIdx = clickedRow * cols + clickedCol;
                    if (clickedIdx >= 0 && clickedIdx < featureCount) {
                        GameCanvas.isPointerSelect = false;
                        focusPane = 1;
                        selectedChucNangIndex = clickedIdx;
                        selectFeatureItem(clickedIdx);
                        return;
                    }
                }
            }
            return;
        }

        // Nút Quay Lại mini dùng chung cho Danh Hiệu và Thú Cưng
        int backBtnX = paneX + 6;
        int backBtnY = paneY + 4;
        int backBtnW = 60;
        int backBtnH = 16;
        if (GameCanvas.isPoint(backBtnX, backBtnY, backBtnW, backBtnH)) {
            GameCanvas.isPointerSelect = false;
            chucNangSubView = 0;
            selectedDanhHieuInfo = null;
            selectedPetInfo = null;
            focusPane = 1;
            updateSoftKeys();
            return;
        }

        if (chucNangSubView == 1) {
            int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
            int listX = paneX + 6;
            int listY = paneY + 24;
            int listW = paneW - 12;
            int listH = paneH - 30;

            if (danhHieuList != null && GameCanvas.isPoint(listX, listY, listW, listH)) {
                int relY = GameCanvas.py - listY + danhHieuList.cmx;
                int itemH = 38;
                int clickedIdx = relY / itemH;
                if (clickedIdx >= 0 && clickedIdx < dhCount) {
                    GameCanvas.isPointerSelect = false;
                    selectedDanhHieuIndex = clickedIdx;
                    selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(clickedIdx);
                    if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) {
                        TitleActionBtn btn = (TitleActionBtn)selectedDanhHieuInfo.actionButtons.elementAt(0);
                        if (btn != null) {
                            GlobalService.gI().Send_DanhHieuAction(selectedDanhHieuInfo.id, btn.actionId);
                        }
                    }
                    updateSoftKeys();
                    return;
                }
            }
            return;
        }

        if (chucNangSubView == 2) {
            int gridX = paneX + 8;
            int gridY = paneY + 22;
            int gridW = paneW - 16;
            int gridH = paneH - 30;
            int slotSize = 28;
            int gap = 3;
            int slotStep = slotSize + gap;
            int cols = gridW / slotStep;
            if (cols < 1) cols = 1;

            if (selectedPetInfo != null) {
                int infoW = (selectedPetInfo.wInfo > 0) ? selectedPetInfo.wInfo : 140;
                int showH = selectedPetInfo.hInfo - selectedPetInfo.hRunInfo;
                if (showH <= 0 || showH > h - 42) showH = (selectedPetInfo.hInfo > 0) ? System.Math.Min(selectedPetInfo.hInfo, h - 42) : 60;
                if (showH < 30) showH = 30;

                int infoX, infoY;
                if (itemTouchX > 0 && itemTouchY > 0) {
                    infoX = itemTouchX + 10;
                    if (infoX + infoW > x + w - 8) infoX = itemTouchX - infoW - 10;
                    if (infoX < x + 8) infoX = x + 8;
                    infoY = itemTouchY - showH / 2;
                    if (infoY + showH + 24 > y + h - 6) infoY = y + h - 6 - showH - 24;
                    if (infoY < y + 28) infoY = y + 28;
                } else {
                    infoX = x + 10;
                    infoY = y + 28 + (h - 34 - showH - 24) / 2;
                    if (infoY < y + 28) infoY = y + 28;
                }
                int btnY = infoY + showH + 3;

                if (GameCanvas.isPoint(infoX, btnY, 60, 18)) {
                    GameCanvas.isPointerSelect = false;
                    bool isEquipped = (selectedPetInfo.colorName == 1);
                    GlobalService.gI().Send_Pet((sbyte)4, (sbyte)(isEquipped ? 0 : 1), selectedPetInfo.ID);
                    selectPet(null, 0, 0);
                    selectedPetIndex = -1;
                    updateSoftKeys();
                    return;
                }
            }

            if (petList != null && GameCanvas.isPoint(gridX, gridY, gridW, gridH)) {
                int relX = GameCanvas.px - gridX;
                int relY = GameCanvas.py - gridY + petList.cmx;
                int clickedCol = relX / slotStep;
                int clickedRow = relY / slotStep;

                if (clickedCol >= 0 && clickedCol < cols && clickedRow >= 0) {
                    int clickedIdx = clickedRow * cols + clickedCol;
                    if (Player.vecPet != null && clickedIdx >= 0 && clickedIdx < Player.vecPet.size()) {
                        GameCanvas.isPointerSelect = false;
                        focusPane = 1;
                        MainItem curPet = (MainItem)Player.vecPet.elementAt(clickedIdx);
                        if (selectedPetIndex == clickedIdx && selectedPetInfo != null) {
                            bool isEquipped = (curPet.colorName == 1);
                            GlobalService.gI().Send_Pet((sbyte)4, (sbyte)(isEquipped ? 0 : 1), curPet.ID);
                            selectPet(null, 0, 0);
                            selectedPetIndex = -1;
                        } else {
                            selectedPetIndex = clickedIdx;
                            selectPet(curPet, GameCanvas.px, GameCanvas.py);
                        }
                        return;
                    } else {
                        selectPet(null, 0, 0);
                        selectedPetIndex = -1;
                        return;
                    }
                }
            }

            if (selectedPetInfo != null) {
                selectPet(null, 0, 0);
                selectedPetIndex = -1;
            }
        }
    }

}
