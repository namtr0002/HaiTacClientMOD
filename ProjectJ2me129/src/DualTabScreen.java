public class DualTabScreen extends MainScreen {
    public static final int SLOTS_PER_TAB = 126;
    public int curInvenPage = 0;
    public static final short[] TAB_ICON_IDS = new short[] { 0, 1, 2, 3, 4 };
    public static final short[] TAB_ICON_SEL_IDS = new short[] { 0, 1, 2, 3, 4 };

public static DualTabScreen instance;

public static final String[] TAB_NAMES = new String[] { "Trang Bị & Túi", "Tiềm Năng", "Kỹ Năng", "Nhiệm Vụ", "Chức Năng" };
public static final String[] SHORT_TAB_NAMES = new String[] { "Túi", "T.Năng", "K.Năng", "N.Vụ", "C.Năng" };
public static final String[] FILTER_NAMES = new String[] { "Tất Cả", "Trang Bị", "Dược Phẩm", "Đá/NL", "Khác" };

public int x;
public int y;
public int w;
public int h;
public int maxWShow;
public int lastCanvasW = -1;
public int lastCanvasH = -1;

public boolean isWide;
public int curMainTab = 0;
public int curInvenFilter = 0;
public boolean isRefresh = false;

public boolean[] isTabInitialized = new boolean[5];
public boolean[] isTabDirty = new boolean[5];
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
public boolean isDraggingEquip = false;
public int equipDragStartY = 0;
public int equipDragStartScrollY = 0;
public static final int EQUIP_PAGE_H = 116;

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
private static final String[] SHORT_ATT_NAMES = new String[] { "S.Mạnh", "P.Thủ", "T.Lực", "T.Thần", "N.Nhẹn" };

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
        super.backCMD = cmdClose;
        super.DB = cmdClose;
        super.DA = cmdAction;
        super.center = cmdMenuAction;
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

public void Show(MainScreen screen) {
    super.Show(screen);
    instance = this;
    lastCanvasW = MotherCanvas.w;
    lastCanvasH = MotherCanvas.h;

    if (GameCanvas.tabInven == null) {
        GameCanvas.tabInven = new TabInventory(T.AF, Player.vecInventory, (byte)0, MainTab.xTab);
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
invenList.AD = limY;
if (invenList.AB > limY) invenList.AB = limY;
if (invenList.AB < 0) invenList.AB = 0;
if (invenList.AC > limY) invenList.AC = limY;
if (invenList.AC < 0) invenList.AC = 0;
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
int statCount = (Player.RQ != null && Player.RQ.length > 0) ? Player.RQ.length : 6;
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
leftStatList.AD = limStatY;
if (leftStatList.AB > limStatY) leftStatList.AB = limStatY;
}
}

public static MainInfoItem[] getDefaultAttributeMInfo(int index, short totalVal) {
switch (index) {
case 0: // Sức mạnh: Tăng tấn công (1), Xuyên giáp (13), Chí mạng (10)
return new MainInfoItem[] {
new MainInfoItem((byte)1, totalVal * 5),
new MainInfoItem((byte)13, totalVal),
new MainInfoItem((byte)10, totalVal * 2)
};
case 1: // Phòng thủ: Phòng thủ (4), Kháng vật lý (26), Kháng phép (27)
return new MainInfoItem[] {
new MainInfoItem((byte)4, totalVal * 4),
new MainInfoItem((byte)26, totalVal),
new MainInfoItem((byte)27, totalVal)
};
case 2: // Thể lực: Máu HP (15), Hồi máu từ bình (23)
return new MainInfoItem[] {
new MainInfoItem((byte)15, totalVal * 20),
new MainInfoItem((byte)23, totalVal)
};
case 3: // Tinh thần: Năng lượng MP (16), Chí mạng phép (11), Xuyên kháng (14)
return new MainInfoItem[] {
new MainInfoItem((byte)16, totalVal * 15),
new MainInfoItem((byte)11, totalVal * 2),
new MainInfoItem((byte)14, totalVal)
};
case 4: // Nhanh nhẹn: Giảm hồi chiêu (25), Né tránh (12)
return new MainInfoItem[] {
new MainInfoItem((byte)25, totalVal),
new MainInfoItem((byte)12, totalVal)
};
default:
return new MainInfoItem[0];
}
}

public static int getAttriCardHeight(Class_CV att, int index) {
int subCount = 2;
if (att != null && att.AD != null && att.AD.length > 0) {
subCount = att.AD.length;
} else {
MainInfoItem[] def = getDefaultAttributeMInfo(index, (short)((att != null) ? (att.AA + att.AB) : 0));
if (def != null && def.length > 0) subCount = def.length;
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
int allInfoCount = (GameScreen.player != null && GameScreen.player.AF != null) ? GameScreen.player.AF.size() : 0;
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
infoList.AD = limInfoY;
if (infoList.AB > limInfoY) infoList.AB = limInfoY;
if (infoList.AB < 0) infoList.AB = 0;
if (infoList.AC > limInfoY) infoList.AC = limInfoY;
if (infoList.AC < 0) infoList.AC = 0;
}

// 2. Potential list (Cột Phải: 5 thẻ Tiềm Năng)
if (Player.QF == null || Player.QF.length == 0) {
Player.QF = new Class_CV[5];
}
for (int i = 0; i < Player.QF.length; i++) {
if (Player.QF[i] == null) {
String name = (i < T.VZ.length) ? T.VZ[i] : ("Thuộc tính " + (i + 1));
Player.QF[i] = new Class_CV((byte)i, (short)0, (short)0, name, getDefaultAttributeMInfo(i, (short)0));
} else if (Player.QF[i].AD == null || Player.QF[i].AD.length == 0) {
Player.QF[i].AD = getDefaultAttributeMInfo(i, (short)(Player.QF[i].AA + Player.QF[i].AB));
}
}

int numAtt = Player.QF.length;
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
Class_CV att = Player.QF[i];
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
attriList.AD = limAttriY;
if (attriList.AB > limAttriY) attriList.AB = limAttriY;
if (attriList.AB < 0) attriList.AB = 0;
if (attriList.AC > limAttriY) attriList.AC = limAttriY;
if (attriList.AC < 0) attriList.AC = 0;
}
}

public void initTab2() {
updateSkillList();
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int skillCount = validSkills.size();
int limSkillY = skillCount * 34 - (h - 56);
if (limSkillY < 0) limSkillY = 0;
if (skillList == null) {
skillList = new ListNew(x + 10, y + 42, leftPaneW, h - 56, 0, 0, limSkillY, true);
} else {
skillList.x = x + 10;
skillList.y = y + 42;
skillList.maxW = leftPaneW;
skillList.maxH = h - 56;
skillList.AD = limSkillY;
if (skillList.AB > limSkillY) skillList.AB = limSkillY;
if (skillList.AB < 0) skillList.AB = 0;
if (skillList.AC > limSkillY) skillList.AC = limSkillY;
if (skillList.AC < 0) skillList.AC = 0;
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
int questCount = (Player.QI != null) ? Player.QI.size() : 0;
int limQuestY = questCount * 34 - (h - 56);
if (limQuestY < 0) limQuestY = 0;
if (questList == null) {
questList = new ListNew(x + 10, y + 42, leftPaneW, h - 56, 0, 0, limQuestY, true);
} else {
questList.AD = limQuestY;
if (questList.AB > limQuestY) questList.AB = limQuestY;
if (questList.AB < 0) questList.AB = 0;
if (questList.AC > limQuestY) questList.AC = limQuestY;
if (questList.AC < 0) questList.AC = 0;
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
featureMenuList.AD = limY;
if (featureMenuList.AB > limY) featureMenuList.AB = limY;
if (featureMenuList.AB < 0) featureMenuList.AB = 0;
if (featureMenuList.AC > limY) featureMenuList.AC = limY;
if (featureMenuList.AC < 0) featureMenuList.AC = 0;
}

if (Player.vecDanhHieu == null || Player.vecDanhHieu.size() == 0) {
GlobalService.getInstance().Send_DanhHieu((byte)0);
}
if (Player.vecPet == null || Player.vecPet.size() == 0) {
GlobalService.getInstance().Send_Pet((byte)3);
}
}



public int getFeatureItemCount() {
        return 12;
    }

    public String getFeatureCategory(int index) {
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

    public String getFeatureTitle(int index) {
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

    public String getFeatureDesc(int index) {
        switch (index) {
            case 0: {
                int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
                String curTitleStr = "Chưa kích hoạt";
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
                String activePet = "Chưa xuất chiến";
                if (Player.vecPet != null) {
                    for (int i = 0; i < Player.vecPet.size(); i++) {
                        Object obj = Player.vecPet.elementAt(i);
                        if (obj instanceof MainItem) {
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
                if (GameScreen.player != null && GameScreen.player.clan != null && GameScreen.player.clan.name != null && GameScreen.player.clan.name.length() > 0) {
                    return "Băng: " + GameScreen.player.clan.name + " - Quản lý";
                }
                return "Chưa vào băng - Tìm & xin vào băng";
            }
            case 3: {
                int fCount = (Player.RN != null) ? Player.RN.size() : 0;
                return fCount + " bạn bè - Danh sách bạn & kẻ thù";
            }
            case 4: {
                if (Player.vecParty != null && Player.vecParty.size() > 0) {
                    return "Nhóm: " + Player.vecParty.size() + " thành viên - Quản lý đội";
                }
                return "Chưa vào nhóm - Mời & tạo nhóm";
            }
            case 5: {
                String pkName = "Tự do";
                if (GameScreen.player != null) {
                    if (GameScreen.player.typePK == 0) {
                        pkName = "Đồ Sát";
                    } else if (GameScreen.player.typePK > 0 && GameScreen.player.typePK < T.VX.length) {
                        pkName = T.VX[GameScreen.player.typePK];
                    }
                }
                String dosatStr = (GameScreen.player != null && GameScreen.player.typePK == 0) ? "Đang Bật" : "Tắt";
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

    public String getFeatureSubText(int index) {
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
if (GameCanvas.gameScr.BM != null) GameCanvas.gameScr.BM.AD();
else GameCanvas.gameScr.commandPointer(40, 0);
} else {
Interface_Game.addInfoPlayerNormal("Bạn chưa tham gia bang hội!", mFont.tahoma_7_yellow);
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bạn chưa tham gia bang hội!");
}
break;
case 3:
{
mVector mVec = new mVector();
if (GameCanvas.gameScr.AZ != null) mVec.addElement(GameCanvas.gameScr.AZ);
if (GameCanvas.gameScr.BA != null) mVec.addElement(GameCanvas.gameScr.BA);
if (GameCanvas.gameScr.AY != null) mVec.addElement(GameCanvas.gameScr.AY);
GameCanvas.menu.startAt(mVec, 2, "Bạn Bè & Kẻ Thù");
break;
}
case 4:
if (Player.vecParty != null && Player.vecParty.size() > 0) {
if (GameCanvas.gameScr.AS != null) GameCanvas.gameScr.AS.AD();
else GameCanvas.gameScr.commandPointer(10, 0);
} else {
Interface_Game.addInfoPlayerNormal("Bạn chưa vào nhóm!", mFont.tahoma_7_yellow);
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bạn chưa vào nhóm!");
}
break;
case 5:
{
mVector mVec = new mVector();
if (GameCanvas.gameScr.AQ != null) mVec.addElement(GameCanvas.gameScr.AQ);
if (GameCanvas.gameScr.AR != null) mVec.addElement(GameCanvas.gameScr.AR);
GameCanvas.menu.startAt(mVec, 2, T.IQ);
break;
}
case 6:
if (GameScreen.player != null && GameScreen.player.PJ != null) {
if (GameCanvas.gameScr.BV != null) GameCanvas.gameScr.BV.AD();
else GameCanvas.gameScr.commandPointer(63, 0);
} else {
Interface_Game.addInfoPlayerNormal("Bạn chưa có sư đồ!", mFont.tahoma_7_yellow);
GameCanvas.Start_Normal_Only_CmdClose_DiaLog("Bạn chưa có sư đồ!");
}
break;
case 7:
if (GameCanvas.gameScr.BU != null) GameCanvas.gameScr.BU.AD();
break;
case 8:
if (GameCanvas.gameScr.BJ != null) GameCanvas.gameScr.BJ.AD();
else GameCanvas.gameScr.commandPointer(33, 0);
break;
case 9:
if (GameCanvas.gameScr.BP != null) GameCanvas.gameScr.BP.AD();
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
msgAutoFire.AA();
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
danhHieuList.AD = limY;
if (danhHieuList.AB > limY) danhHieuList.AB = limY;
if (danhHieuList.AB < 0) danhHieuList.AB = 0;
if (danhHieuList.AC > limY) danhHieuList.AC = limY;
if (danhHieuList.AC < 0) danhHieuList.AC = 0;
}

if (Player.vecDanhHieu == null || Player.vecDanhHieu.size() == 0) {
GlobalService.getInstance().Send_DanhHieu((byte)0);
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
int totalSlots = Math.max(Player.maxInventory, Math.max(petCount, petCols * 4));
int petRows = (totalSlots + petCols - 1) / petCols;
int limPetY = petRows * 32 - petGridH;
if (limPetY < 0) limPetY = 0;
if (petList == null) {
petList = new ListNew(x + 10, y + 42, petGridW, petGridH, 0, 0, limPetY, true);
} else {
petList.AD = limPetY;
if (petList.AB > limPetY) petList.AB = limPetY;
if (petList.AB < 0) petList.AB = 0;
if (petList.AC > limPetY) petList.AC = limPetY;
if (petList.AC < 0) petList.AC = 0;
}
if (Player.vecPet == null || Player.vecPet.size() == 0) {
GlobalService.getInstance().Send_Pet((byte)3);
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
GlobalService.getInstance().Send_DanhHieu((byte)0);
updateSoftKeys();
}

public void openPetSubView() {
chucNangSubView = 2;
focusPane = 1;
initTabPet();
GlobalService.getInstance().Send_Pet((byte)3);
updateSoftKeys();
}

public void selectItem(MainItem item, int touchX, int touchY) {
selectedItemInfo = item;
itemTouchX = touchX;
itemTouchY = touchY;
if (selectedItemInfo != null) {
    int maxPopupH = Math.max(60, Math.min(h - 54, MotherCanvas.h - 54));
    if (selectedItemInfo.BT > maxPopupH) {
        selectedItemInfo.CO = selectedItemInfo.BT - maxPopupH;
    } else {
        selectedItemInfo.CO = 0;
    }
    if (selectedItemInfo.typeObject == 3) {
        this.vecInfoSS = MainItem.AA(selectedItemInfo);
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

int maxPopupH = Math.max(60, Math.min(h - 54, MotherCanvas.h - 54));
if (selectedPetInfo.BT > maxPopupH) {
selectedPetInfo.CO = selectedPetInfo.BT - maxPopupH;
} else {
selectedPetInfo.CO = 0;
}


}
updateSoftKeys();
}

public void switchTab(int targetTab) {
switchTab(targetTab, false);
}

public void switchTab(int targetTab, boolean keepTopFocus) {
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
    } else if (TabSkill.CL != null && TabSkill.CL.size() > 0) {
        source = TabSkill.CL;
    }

    if (source != null) {
        for (int i = 0; i < source.size(); i++) {
            Skill_Info sk = (Skill_Info)source.elementAt(i);
            if (sk != null && sk.Lv_RQ != -1) {
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

        String name = (item.name != null) ? item.name.toLowerCase() : "";

        // 2: Dược Phẩm (Bình HP, MP, Bình thuốc, Thức ăn, Nước uống, Bình hồi phục)
        if (item.typeObject == 4) {
            if (item.BQ == 0 || item.BQ == 1 || item.BQ == 2 || item.BQ == 3 || item.BQ == 4 || item.BQ == 5 || item.BQ == 6 || item.BQ == 7 || item.BQ == 8 || item.BQ == 9 || item.BQ == 10 || item.BQ == 11) {
                return 2;
            }
            if (name.indexOf("máu") >= 0 || name.indexOf("mau") >= 0 || name.indexOf("hp") >= 0 ||
                name.indexOf("năng lượng") >= 0 || name.indexOf("nang luong") >= 0 || name.indexOf("mp") >= 0 ||
                name.indexOf("dược") >= 0 || name.indexOf("duoc") >= 0 || name.indexOf("thuốc") >= 0 || name.indexOf("thuoc") >= 0 ||
                name.indexOf("bình") >= 0 || name.indexOf("binh") >= 0 || name.indexOf("rượu") >= 0 || name.indexOf("ruou") >= 0 ||
                name.indexOf("thịt") >= 0 || name.indexOf("thit") >= 0 || name.indexOf("bánh") >= 0 || name.indexOf("banh") >= 0) {
                return 2;
            }
        }

        // Kiểm tra nếu là Rương / Hộp quà / Túi / Gói chứa đồ trước để không nhầm sang đá
        boolean isChestOrBox = name.startsWith("rương") || name.startsWith("ruong") || 
                               name.startsWith("hộp quà") || name.startsWith("hop qua") || 
                               name.startsWith("túi") || name.startsWith("tui") || 
                               name.startsWith("gói") || name.startsWith("goi");

        // 3: Đá & Nguyên Liệu (Gems, Inlaid stones, Upgrade stones, Crafting materials, Ores, Essences)
        if (!isChestOrBox) {
            // a. Các typeObject nguyên liệu chính
            if (item.typeObject == 7 || item.typeObject == 5 || (item instanceof MainMaterial)) {
                return 3;
            }

            // b. BQ (Hp_Mp_Other) của các loại đá khảm / đá thuộc tính / đá nâng cấp
            if (item.typeObject == 4) {
                if (item.BQ == 12 || item.BQ == 57 || item.BQ == 80 || 
                    item.BQ == 81 || item.BQ == 77 || item.BQ == 27 || 
                    item.BQ == 28 || item.BQ == -16 || item.BQ == -18 || 
                    item.BQ == 66 || item.BQ == 93 ||
                    (item.BQ == 40 && (name.indexOf("đá") >= 0 || name.indexOf("da ") >= 0))) {
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
            if (name.indexOf("đá ") >= 0 || name.indexOf("da ") >= 0 || name.startsWith("đá") || name.startsWith("da") ||
                name.indexOf("khảm") >= 0 || name.indexOf("kham") >= 0 ||
                name.indexOf("ngọc") >= 0 || name.indexOf("ngoc") >= 0 ||
                name.indexOf("cẩm thạch") >= 0 || name.indexOf("hải thạch") >= 0 || name.indexOf("thạch anh") >= 0 || 
                name.indexOf("huyết thạch") >= 0 || name.indexOf("hắc thạch") >= 0 || name.indexOf("thạch") >= 0 ||
                name.indexOf("tinh thể") >= 0 || name.indexOf("tinh the") >= 0 ||
                name.indexOf("saphia") >= 0 || name.indexOf("sapphire") >= 0 || name.indexOf("topaz") >= 0 || name.indexOf("ruby") >= 0 ||
                name.indexOf("kim cương") >= 0 || name.indexOf("diamond") >= 0 ||
                name.indexOf("khoáng") >= 0 || name.indexOf("khoang") >= 0 || name.indexOf("quặng") >= 0 || name.indexOf("quang") >= 0) {
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
this.invenList.AD = limY;
if (this.invenList.AC > limY) this.invenList.AC = limY;
if (this.invenList.AB > limY) this.invenList.AB = limY;
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
this.invenList.AC = 0;
this.invenList.AB = 0;
}
}

private void updateSoftKeys() {
super.DB = cmdClose;
super.backCMD = cmdClose;

if (curMainTab == 0) {
if (focusPane == 0 && selectedEquipIndex >= 0) {
cmdAction.caption = "Đổi";
cmdMenuAction.caption = "Đổi";
super.DA = cmdAction;
super.center = cmdMenuAction;
return;
}

MainItem cur = (selectedItemInfo != null) ? selectedItemInfo : null;
if (cur == null && focusPane == 1 && selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
cur = (MainItem)filteredItems.elementAt(selectedInvenIndex);
}

if (cur != null) {
if (GameCanvas.tabInven == null) {
GameCanvas.tabInven = new TabInventory(T.AF, Player.vecInventory, (byte)0, MainTab.xTab);
GameCanvas.tabInven.initCmd();
}
GameCanvas.tabInven.itemCur = cur;
GameCanvas.tabInven.IdSelect = Player.vecInventory.indexOf(cur);
mVector mActions = cur.getActionInven((byte)0);

if (mActions != null && mActions.size() > 0) {
iCommand primaryCmd = (iCommand)mActions.elementAt(0);
cmdAction.caption = primaryCmd.caption;
super.DA = cmdAction;

if (mActions.size() > 1) {
cmdMenuAction.caption = "Menu";
super.center = cmdMenuAction;
} else {
super.center = cmdAction;
}
return;
}
}
super.DA = null;
super.center = null;
return;
} else if (curMainTab == 1) {
if (focusPane == 1) {
cmdAction.caption = "+1 Điểm";
cmdMenuAction.caption = "Cộng";
super.DA = cmdAction;
super.center = cmdAction;
} else {
super.DA = null;
super.center = null;
}
return;
} else if (curMainTab == 2) {
if (focusPane == 1 && validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
if (curSk != null && curSk.Lv_RQ > 0 && (curSk.typeSkill == 1 || curSk.typeSkill == 2 || curSk.typeSkill == 4)) {
cmdAction.caption = "Gán Phím";
cmdMenuAction.caption = "Gán Phím";
super.DA = cmdAction;
super.center = cmdAction;
return;
}
}
super.DA = null;
super.center = null;
return;
} else if (curMainTab == 3) {
cmdAction.caption = "Chi Tiết";
cmdMenuAction.caption = "Chi Tiết";
super.DA = cmdAction;
super.center = cmdAction;
return;
} else if (curMainTab == 4) {
if (chucNangSubView == 0) {
cmdAction.caption = "Mở";
cmdMenuAction.caption = "Mở";
super.DA = cmdAction;
super.center = cmdAction;
return;
} else if (chucNangSubView == 1) {
if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) {
TitleActionBtn btn = (TitleActionBtn)selectedDanhHieuInfo.actionButtons.elementAt(0);
cmdAction.caption = btn.name;
cmdMenuAction.caption = btn.name;
super.DA = cmdAction;
super.center = cmdAction;
return;
}
super.DA = null;
super.center = null;
return;
} else if (chucNangSubView == 2) {
if (focusPane == 1 && selectedPetIndex >= 0 && Player.vecPet != null && selectedPetIndex < Player.vecPet.size()) {
MainItem p = (MainItem)Player.vecPet.elementAt(selectedPetIndex);
String petAct = (p != null && p.colorName == 1) ? "Tháo" : "Dùng";
cmdAction.caption = petAct;
cmdMenuAction.caption = petAct;
super.DA = cmdAction;
super.center = cmdAction;
return;
}
super.DA = null;
super.center = null;
return;
}
}
}

public void commandPointer(int index, int subIndex) {
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
mainSkill.AB = curSk.indexHotKey;
mainSkill.idIcon = curSk.idIcon;
mainSkill.isBuff = (curSk.typeSkill == 2);
mainSkill.lvDevil = curSk.LvDevilSkill;
mainSkill.AG = curSk.typeDevil;
Player.AA(subIndex, mainSkill, (MainItem)null);
Interface_Game.BP = 100;
keyFireCoolDown = 8;
}
}
break;
case 5:
GlobalService.getInstance().Add_Point_Attribute((byte)selectedAttIndex, (short)subIndex);
GameCanvas.end_Dialog();
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
GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.CW);
} else {
Class_AP var7 = new Class_AP();
var7.AA(listItem, popupX, popupY, 28, (slotIdx % 2 == 0) ? 2 : 0);
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
                boolean isLeft = (selectedEquipIndex % 2 == 0);
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
                    GameCanvas.tabInven = new TabInventory(T.AF, Player.vecInventory, (byte)0, MainTab.xTab);
                    GameCanvas.tabInven.initCmd();
                }
                GameCanvas.tabInven.itemCur = cur;
                GameCanvas.tabInven.IdSelect = Player.vecInventory.indexOf(cur);
                mVector mActions = cur.getActionInven((byte)0);
                if (mActions != null && mActions.size() > 0) {
                    iCommand primaryCmd = (iCommand)mActions.elementAt(0);
                    primaryCmd.perform();
                    isRefresh = true;
                }
            }
        } else if (curMainTab == 1) {
            addPotentialPoint(selectedAttIndex);
        } else if (curMainTab == 2) {
            if (focusPane == 1) {
                openSkillHotkeyMenu();
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
                        GlobalService.getInstance().Send_DanhHieuAction(selectedDanhHieuInfo.id, btn.actionId);
                    }
                }
            } else if (chucNangSubView == 2) {
                if (Player.vecPet != null && selectedPetIndex >= 0 && selectedPetIndex < Player.vecPet.size()) {
                    MainItem curPet = (MainItem)Player.vecPet.elementAt(selectedPetIndex);
                    if (curPet != null) {
                        boolean isEquipped = (curPet.colorName == 1);
                        GlobalService.getInstance().Send_Pet((byte)4, (byte)(isEquipped ? 0 : 1), curPet.ID);
                    }
                }
            }
        }
    }

    public void addPotentialPoint(int attIdx) {
if (attIdx < 0 || Player.QF == null || attIdx >= Player.QF.length || Player.QF[attIdx].AA >= 80) {
return;
}

if (Player.AS > 1) {
mVector mVector2 = new mVector();
int[] mNumAttri = new int[] { 1, 5, 10 };
int num2 = 0;
for (int i = 0; i < mNumAttri.length; i++) {
int num3 = mNumAttri[i];
if (num3 > Player.AS) {
num3 = Player.AS;
}
if (num3 > 80 - Player.QF[attIdx].AA) {
num3 = 80 - Player.QF[attIdx].AA;
}
iCommand iCommand2 = new iCommand("+" + num3, 5, num3, this);
if (GameCanvas.isTouch) {
iCommand2.AH = 3;
}
if (num2 != num3) {
num2 = num3;
mVector2.addElement(iCommand2);
}
if (mNumAttri[i] >= Player.AS) {
break;
}
}
String attName = (attIdx < T.VZ.length) ? T.VZ[attIdx] : Player.QF[attIdx].AC;
GameCanvas.Start_Normal_DiaLog_New(T.IZ + attName + "?", mVector2, true, T.tabAttribute);
} else if (Player.AS == 1) {
GlobalService.getInstance().Add_Point_Attribute((byte)attIdx, (short)1);
} else {
GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.GG);
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
boolean isLeft = (selectedEquipIndex % 2 == 0);
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
GameCanvas.tabInven = new TabInventory(T.AF, Player.vecInventory, (byte)0, MainTab.xTab);
GameCanvas.tabInven.initCmd();
}
GameCanvas.tabInven.itemCur = cur;
GameCanvas.tabInven.IdSelect = Player.vecInventory.indexOf(cur);
mVector mActions = cur.getActionInven((byte)0);
if (mActions != null && mActions.size() > 0) {
keyFireCoolDown = 8;
GameCanvas.clearAll();
GameCanvas.clearKeyHold();
GameCanvas.clearKeyPressed();
GameCanvas.AA(5);
GameCanvas.AB(5);
GameCanvas.AA(12);
GameCanvas.AB(12);
GameCanvas.AA(40);
GameCanvas.AB(40);
GameCanvas.menu.startAt(mActions, 2, cur.name);
}
}
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
GameCanvas.Start_Normal_Only_CmdClose_DiaLog(T.HT);
return;
}
mVector mVector2 = new mVector();
for (int i = 0; i < 6; i++) {
if (i != 2 && (GameCanvas.isTouch || i != 5)) {
iCommand iCommand2 = (GameCanvas.isTouch ? new iCommand(T.BD + " " + (i + 1), 4, i, this) : ((!TField.isQwerty) ? new iCommand(T.BD + " " + (i * 2 + 1), 4, i, this) : new iCommand(T.BD + " " + T.VY[i], 4, i, this)));
mVector2.addElement(iCommand2);
}
}
keyFireCoolDown = 8;
GameCanvas.clearAll();
GameCanvas.clearKeyHold();
GameCanvas.clearKeyPressed();
GameCanvas.AA(5);
GameCanvas.AB(5);
GameCanvas.AA(12);
GameCanvas.AB(12);
GameCanvas.AA(40);
GameCanvas.AB(40);
GameCanvas.menu.startAt(mVector2, 2, T.BC);
}
}

private void openQuestDetails() {
if (Player.QI != null && selectedQuestIndex >= 0 && selectedQuestIndex < Player.QI.size()) {
MainQuest curQ = (MainQuest)Player.QI.elementAt(selectedQuestIndex);
if (curQ != null) {
MsgDialog msgDialog = new MsgDialog();
msgDialog.AA(curQ, false);
GameCanvas.Start_Current_Dialog((MainDialog)msgDialog);
}
}
}

public void close() {
    if (curMainTab == 4 && chucNangSubView > 0) {
        chucNangSubView = 0;
        selectedDanhHieuInfo = null;
        selectedPetInfo = null;
        updateSoftKeys();
        return;
    }
    if (super.mainScreen != null) {
        super.mainScreen.Show(super.mainScreen.mainScreen);
    } else {
        GameCanvas.gameScr.Show();
    }
}

    public void update() {
        if (super.mainScreen != null) {
            super.mainScreen.update();
        }

        if (lastCanvasW != MotherCanvas.w || lastCanvasH != MotherCanvas.h) {
            lastCanvasW = MotherCanvas.w;
            lastCanvasH = MotherCanvas.h;
            recalculateLayout();
        }

        if (TabScreen.AA) {
            TabScreen.AA = false;
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
                        int newIdx = filteredItems.indexOf(selectedItemInfo);
                        if (newIdx >= 0 && !selectedItemInfo.CE && (selectedItemInfo.typeObject != 4 || selectedItemInfo.numPotion > 0)) {
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
                    GameCanvas.tabInven.IdSelect = Player.vecInventory.indexOf(selectedItemInfo);
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

        MainTab.BM = GameCanvas.gameTick % 120;
        if (MainTab.BG != null) MainTab.BG.updateTimeCountDownTicket();
        if (MainTab.BH != null) MainTab.BH.updateTimeCountDownTicket();
        if (MainTab.BI != null) MainTab.BI.updateTimeCountDownTicket();
        if (MainTab.BJ != null) MainTab.BJ.updateTimeCountDownTicket();

        if (curMainTab == 0) {
            if (equipScrollY != equipToY) {
                int diff = equipToY - equipScrollY;
                if (Math.abs(diff) <= 4) {
                    equipScrollY = equipToY;
                } else {
                    equipScrollY += diff / 3;
                }
            }
            if (invenList != null) {
                int oldCmx = invenList.AC;
                invenList.AC();
                if (invenList.AE && CRes.abs(invenList.AC - oldCmx) > 4) {
                    selectedItemInfo = null;
                    selectedInvenIndex = -1;
                }
            }
            if (leftStatList != null) {
                leftStatList.AC();
            }
        } else if (curMainTab == 1) {
            if (infoList != null) infoList.AC();
            if (attriList != null) attriList.AC();
            if (timeFocusAtt > 0) timeFocusAtt--;
        } else if (curMainTab == 2) {
            if (skillList != null) skillList.AC();
            if (skillDetailList != null) skillDetailList.AC();
        } else if (questList != null && curMainTab == 3) {
            questList.AC();
        } else if (curMainTab == 4) {
            if (chucNangSubView == 0 && featureMenuList != null) featureMenuList.AC();
            else if (chucNangSubView == 1) {
                if (danhHieuList != null) danhHieuList.AC();
                if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.idEff > 0) {
                    if (previewTitleEff == null || lastPreviewTitleEffId != selectedDanhHieuInfo.idEff) {
                        lastPreviewTitleEffId = selectedDanhHieuInfo.idEff;
                        previewTitleEff = new DataSkillEff(selectedDanhHieuInfo.idEff, -1, (byte)0, (byte)0);
                    }
                } else {
                    previewTitleEff = null;
                    lastPreviewTitleEffId = -1;
                }
                if (previewTitleEff != null) {
                    previewTitleEff.AA();
                }
            } else if (chucNangSubView == 2 && petList != null) {
                petList.AC();
            }
        }
        if (keyFireCoolDown > 0) keyFireCoolDown--;
    }

    public final void handleKeyPress() {
        if (keyFireCoolDown > 0) return;
        // Key 5 / OK / FIRE / Enter (5, 12, 40)
        if (GameCanvas.isKeyPressed(5) || GameCanvas.AK[5] || GameCanvas.isKeyPressed(12) || GameCanvas.AK[12] || GameCanvas.isKeyPressed(40) || GameCanvas.AK[40]) {
            keyFireCoolDown = 8;
            GameCanvas.clearAll();
            GameCanvas.clearKeyHold();
            GameCanvas.clearKeyPressed();
            GameCanvas.AA(5);
            GameCanvas.AB(5);
            GameCanvas.AA(12);
            GameCanvas.AB(12);
            GameCanvas.AA(40);
            GameCanvas.AB(40);
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

        // Softkey Menu on Key 13 / 41 (Back/Right)
        if (GameCanvas.keyMyHold[13] || GameCanvas.isKeyPressed(13) || GameCanvas.keyMyHold[41] || GameCanvas.isKeyPressed(41)) {
            GameCanvas.AH();
            GameCanvas.clearKeyPressed();
            close();
            return;
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

            boolean hasKeypadMove = false;
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
                        GameCanvas.tabInven.IdSelect = Player.vecInventory.indexOf(it);
                    }
                } else {
                    selectItem(null, 0, 0);
                    selectedInvenIndex = -1;
                }

                if (invenList != null && selectedInvenIndex >= 0) {
                    int itemSlotH = 30;
                    int itemY = (selectedInvenIndex / cols) * itemSlotH;
                    int invenViewH = (h - 34) - 38;
                    if (itemY < invenList.AC) invenList.AB = itemY;
                    if (itemY + itemSlotH > invenList.AC + invenViewH) invenList.AB = itemY + itemSlotH - invenViewH;
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
            if (GameCanvas.isKeyPressed(1) && infoList != null && infoList.AC <= 0) {
                GameCanvas.ClearkeyMove(1);
                focusPane = 2; // Jump up to Top Nav Tabs
                updateSoftKeys();
                return;
            }
            if (GameCanvas.isKeyPressed(1) && infoList != null) {
                GameCanvas.ClearkeyMove(1);
                infoList.AB -= 18;
                if (infoList.AB < 0) infoList.AB = 0;
            }
            if (GameCanvas.isKeyPressed(3) && infoList != null) {
                GameCanvas.ClearkeyMove(3);
                infoList.AB += 18;
                if (infoList.AB > infoList.AD) infoList.AB = infoList.AD;
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
            int maxAtt = (Player.QF != null && Player.QF.length > 0) ? Player.QF.length : 5;
            boolean keyMoved = false;

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
                    Class_CV att = (Player.QF != null && i < Player.QF.length) ? Player.QF[i] : null;
                    targetY += getAttriCardHeight(att, i) + 4;
                }
                Class_CV curAtt = (Player.QF != null && selectedAttIndex < Player.QF.length) ? Player.QF[selectedAttIndex] : null;
                int curH = getAttriCardHeight(curAtt, selectedAttIndex);
                int rightListH = (h - 34) - 24;
                if (targetY < attriList.AC) attriList.AB = targetY;
                if (targetY + curH > attriList.AC + rightListH) attriList.AB = targetY + curH - rightListH;
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
        int total = validSkills.size();
        if (total > 0 && focusPane == 1) {
            boolean hasKeyMove = false;
            if (GameCanvas.isKeyPressed(1) && selectedSkillIndex == 0) {
                GameCanvas.ClearkeyMove(1);
                focusPane = 2;
                return;
            }
            if (GameCanvas.isKeyPressed(1)) { selectedSkillIndex--; GameCanvas.ClearkeyMove(1); hasKeyMove = true; }
            if (GameCanvas.isKeyPressed(3)) { selectedSkillIndex++; GameCanvas.ClearkeyMove(3); hasKeyMove = true; }
            if (GameCanvas.isKeyPressed(0)) { GameCanvas.ClearkeyMove(0); switchTab(curMainTab - 1); return; }
            if (GameCanvas.isKeyPressed(2)) { GameCanvas.ClearkeyMove(2); switchTab(curMainTab + 1); return; }
            if (selectedSkillIndex < 0) selectedSkillIndex = 0;
            if (selectedSkillIndex >= total) selectedSkillIndex = total - 1;

            if (hasKeyMove && skillList != null) {
                int slotY = selectedSkillIndex * 34;
                if (slotY < skillList.AC) skillList.AB = slotY;
                if (slotY + 34 > skillList.AC + (h - 56)) skillList.AB = slotY + 34 - (h - 56);
            }
            updateSoftKeys();
        }
    }

    private void handleKeypadTab3() {
        int total = (Player.QI != null) ? Player.QI.size() : 0;
        if (total > 0 && focusPane == 1) {
            boolean hasKeyMove = false;
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
                if (slotY < questList.AC) questList.AB = slotY;
                if (slotY + 34 > questList.AC + (h - 56)) questList.AB = slotY + 34 - (h - 56);
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
                boolean keyMoved = false;

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

                if (featureMenuList != null) {
                    int row = selectedChucNangIndex / cols;
                    int itemY = row * (itemH + 4);
                    int listH = h - 34 - 29;
                    if (itemY < featureMenuList.AC) featureMenuList.AB = itemY;
                    if (itemY + itemH > featureMenuList.AC + listH) featureMenuList.AB = itemY + itemH - listH;
                }
                updateSoftKeys();
            }
        } else if (chucNangSubView == 1) {
            int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
            if (focusPane == 1 && dhCount > 0) {
                boolean keyMoved = false;
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
                    if (itemY < danhHieuList.AC) danhHieuList.AB = itemY;
                    if (itemY + itemH > danhHieuList.AC + (h - 34 - 30)) danhHieuList.AB = itemY + itemH - (h - 34 - 30);
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
                boolean hasPetMove = false;
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
                    if (petY < petList.AC) petList.AB = petY;
                    if (petY + petSlotStep > petList.AC + petViewH) petList.AB = petY + petSlotStep - petViewH;
                }
                updateSoftKeys();
            }
        }
    }

    public void paint(mGraphics g) {
        if (super.mainScreen != null) {
            super.mainScreen.paint(g);
        }
        GameCanvas.resetTrans(g);
        g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

        this.AD(g, x, y, w, h, 0);

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
        } catch (Exception e) {}

// Side money box removed for clean parchment UI

        GameCanvas.resetTrans(g);
        g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

        if (!GameCanvas.isTouch) {
            super.paint(g);
        }
    }

    private void paintTopNavTabs(mGraphics g) {
        int tabCount = TAB_NAMES.length;
        int tabW = (w - 44) / tabCount;
        int tabH = 18;
        int startX = x + 8;
        int startY = y + 8;

        for (int i = 0; i < tabCount; i++) {
            int curX = startX + i * tabW;
            boolean isSel = (i == curMainTab);
            boolean isFocused = (focusPane == 2 && isSel);

            AvMain.paintRect(g, curX, startY, tabW - 2, tabH, (byte)(isSel ? 1 : 0), (isSel ? 0 : 1));

            if (isFocused) {
                g.setColor(0xFFFF00);
                g.drawRect(curX - 1, startY - 1, tabW, tabH + 1);
            }

            if (isSel && AvMain.imgNenfocus != null) {
                g.drawRegion(AvMain.imgNenfocus, 2, 2, tabW - 2, tabH, 0, curX, startY, 0);
            }

            try {
                String tabName = (!isWide || tabW < 52) ? SHORT_TAB_NAMES[i] : TAB_NAMES[i];
                int textX = curX + (tabW - 2) / 2;
                int textY = startY + 3;
                if (isSel) {
                    mFont.tahoma_7b_yellow.drawStringAutoCenter(g, tabName, textX, textY, tabW - 4);
                } else {
                    mFont.tahoma_7_black.drawStringAutoCenter(g, tabName, textX, textY, tabW - 4);
                }

                // Huy hiệu thông báo góc tab
                boolean showBadge = false;
                if (i == 1 && Player.AS > 0) showBadge = true;
                else if (i == 2 && Player.isSkillready) showBadge = true;
                else if (i == 3 && TabQuest.BO) showBadge = true;
                else if (i == 4 && GameScreen.numMess > 0) showBadge = true;

                if (showBadge && MainEvent.imgNew != null && GameCanvas.gameTick % 10 < 8) {
                    g.drawRegion((mImage)MainEvent.imgNew, curX + tabW - 8, startY + 3, 3);
                }
            } catch (Exception e) {}
        }
    }

    private void paintTabEquipAndInven(mGraphics g) {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int paneX = x + 10;
        int paneY = y + 28;
        int paneH = h - 34;

        AvMain.paintRect(g, paneX, paneY, leftPaneW, paneH, (byte)0, 1);

        mFont.tahoma_7b_black.drawStringAutoCenter(g, GameScreen.player.name, paneX + leftPaneW / 2, paneY + 3, leftPaneW - 12);
        String lvStr;
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
        int equipAreaH = 120;

        int tabBtnW = (equipAreaW - 6) / 2;
        int tabBtnH = 13;
        int tabBtnY = equipAreaY + 1;
        boolean isTab0Active = (equipSubTab == 0);
        boolean isTab1Active = (equipSubTab == 1);

        String eqTab0Label = (tabBtnW < 52) ? "T.Bị" : "Trang Bị";
        String eqTab1Label = (tabBtnW < 52) ? "Thần" : "Thần Trang";

        // Nút "Trang Bị"
        AvMain.paintRect(g, equipAreaX + 2, tabBtnY, tabBtnW, tabBtnH, (byte)(isTab0Active ? 1 : 0), (isTab0Active ? 0 : 1));
        if (isTab0Active && AvMain.imgNenfocus != null) {
            g.drawRegion(AvMain.imgNenfocus, 2, 2, tabBtnW, tabBtnH, 0, equipAreaX + 2, tabBtnY, 0);
        }
        if (isTab0Active) mFont.tahoma_7b_yellow.drawStringAutoCenter(g, eqTab0Label, equipAreaX + 2 + tabBtnW / 2, tabBtnY + 1, tabBtnW - 4);
        else mFont.tahoma_7_white.drawStringAutoCenter(g, eqTab0Label, equipAreaX + 2 + tabBtnW / 2, tabBtnY + 1, tabBtnW - 4);

        // Nút "Thần Trang"
        AvMain.paintRect(g, equipAreaX + 4 + tabBtnW, tabBtnY, tabBtnW, tabBtnH, (byte)(isTab1Active ? 1 : 0), (isTab1Active ? 0 : 1));
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
            g.drawRegion((mImage)MainObject.imgShadow, charCenterX, charCenterY0 + GameScreen.player.hOne / 4, 3);
            GameScreen.player.AA(g, charCenterX, charCenterY0 + GameScreen.player.hOne / 4, true);
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
            GameScreen.player.paintCharShowWithOverride(g, charCenterX, charCenterY1, true, cw, ch, cb, cl);
        }

        GameCanvas.resetTrans(g);
        g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

        int statY = paneY + 150;
        int statH = paneH - 152;
        if (statH < 36) statH = 36;
        int boxX = paneX + 4;
        int boxW = leftPaneW - 8;
        AvMain.paintRect(g, boxX, statY, boxW, statH, (byte)0, 3);

        int viewH = statH - 4;
        int viewW = boxW - 4;

        int lineH = 14;
        int currencyRows = 4;
        int ticketRows = 3;
        int topInfoRows = currencyRows + ticketRows;
        int topInfoH = topInfoRows * lineH;
        int dividerH = 4;

        // 2. Tinh toan truoc so dong cua cac chi so de cuon dong chuan xac
        String[] defaultLabels = new String[] { "Att", "Magic", "Def", "Cri", "Cri.D", "Eva" };
        String[] labels = (T.mNameShortInfo != null && T.mNameShortInfo.length >= 6) ? T.mNameShortInfo : defaultLabels;
        int totalItems = (Player.RQ != null && Player.RQ.length > 0) ? Math.min(Player.RQ.length, labels.length) : 0;
        int halfW = boxW / 2 - 4;
        int actualStatLines = 0;

        if (totalItems > 0) {
            int calcIdx = 0;
            while (calcIdx < totalItems) {
                String l1 = (calcIdx < labels.length) ? labels[calcIdx] : ("Stat" + calcIdx);
                String v1 = (calcIdx < Player.RQ.length && Player.RQ[calcIdx] != null && Player.RQ[calcIdx].length() > 0) ? Player.RQ[calcIdx] : "0";
                String s1 = l1 + ": " + v1;
                mFont f1 = (calcIdx == 3 || calcIdx == 4 || calcIdx == 5) ? mFont.tahoma_7_yellow : mFont.tahoma_7_white;
                int w1 = f1.getWidth(s1);

                if (w1 > halfW - 2 || calcIdx + 1 >= totalItems) {
                    actualStatLines++;
                    calcIdx++;
                } else {
                    String l2 = (calcIdx + 1 < labels.length) ? labels[calcIdx + 1] : ("Stat" + (calcIdx + 1));
                    String v2 = (calcIdx + 1 < Player.RQ.length && Player.RQ[calcIdx + 1] != null && Player.RQ[calcIdx + 1].length() > 0) ? Player.RQ[calcIdx + 1] : "0";
                    String s2 = l2 + ": " + v2;
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
            leftStatList.AD = limStatY;
            if (leftStatList.AB > limStatY) leftStatList.AB = limStatY;
            if (leftStatList.AB < 0) leftStatList.AB = 0;
        }

        g.setClip(boxX + 2, statY + 2, viewW, viewH);
        g.translate(0, -leftStatList.AC);

        int col1X = boxX + 4;
        int col2X = boxX + boxW / 2 + 1;
        int curY = statY + 3;

        // 1. Tiền Tệ & Điểm
        if (AvMain.fraMoney != null) {
            // Beri (icon 0) - full row
            AvMain.fraMoney.drawFrame(0, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_yellow.drawString(g, AvMain.getDotNumber(Player.SN), col1X + 14, curY, 0);
            curY += lineH;

            // Ruby (icon 1) - full row
            AvMain.fraMoney.drawFrame(1, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_red.drawString(g, AvMain.getDotNumber((long)(GameScreen.player != null ? GameScreen.player.Ruby : 0)), col1X + 14, curY, 0);
            curY += lineH;

            // Extol (icon 7) - full row
            AvMain.fraMoney.drawFrame(7, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_green.drawString(g, AvMain.getDotNumber((long)(GameScreen.player != null ? GameScreen.player.PD : 0)), col1X + 14, curY, 0);
            curY += lineH;

            // Cống hiến / Bùa (icon 8) - full row
            AvMain.fraMoney.drawFrame(8, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_orange.drawString(g, AvMain.getDotNumber((long)(GameScreen.player != null ? GameScreen.player.PF : 0)), col1X + 14, curY, 0);
            curY += lineH;

            // 2. Vé & Bánh mì
            // Bánh mì (icon 2) | Vé PvP (icon 4)
            AvMain.fraMoney.drawFrame(2, col1X + 6, curY + 6, 0, 3, g);
            if (MainTab.BG != null && (MainTab.BG.timeCountDown <= 0 || MainTab.BM < 60)) {
                mFont.tahoma_7_white.drawString(g, Player.Ticket + "/" + Player.MaxTicket, col1X + 14, curY, 0);
            } else if (MainTab.BG != null) {
                MainTab.BG.paintCountDownTicketHour(g, mFont.tahoma_7_yellow, col1X + 14, curY, 0);
            }

            AvMain.fraMoney.drawFrame(4, col2X + 6, curY + 6, 0, 3, g);
            if (MainTab.BH != null && (MainTab.BH.timeCountDown <= 0 || MainTab.BM < 60)) {
                mFont.tahoma_7_white.drawString(g, Player.RG + "/" + Player.RJ, col2X + 14, curY, 0);
            } else if (MainTab.BH != null) {
                MainTab.BH.paintCountDownTicketHour(g, mFont.tahoma_7_yellow, col2X + 14, curY, 0);
            }
            curY += lineH;

            // Chìa khóa boss (icon 3) | x2 XP (icon 6)
            AvMain.fraMoney.drawFrame(3, col1X + 6, curY + 6, 0, 3, g);
            if (MainTab.BI != null && (MainTab.BI.timeCountDown <= 0 || MainTab.BM < 60)) {
                mFont.tahoma_7_white.drawString(g, Player.RH + "/" + Player.RK, col1X + 14, curY, 0);
            } else if (MainTab.BI != null) {
                MainTab.BI.paintCountDownTicketHour(g, mFont.tahoma_7_yellow, col1X + 14, curY, 0);
            }

            AvMain.fraMoney.drawFrame(6, col2X + 6, curY + 6, 0, 3, g);
            if (MainTab.BJ != null && MainTab.BJ.timeCountDown <= 0) {
                mFont.tahoma_7_white.drawString(g, "00:00", col2X + 14, curY, 0);
            } else if (MainTab.BJ != null) {
                MainTab.BJ.paintCountDownTicketHour(g, mFont.tahoma_7_yellow, col2X + 14, curY, 0);
            }
            curY += lineH;

            // Điểm PK (icon 5)
            AvMain.fraMoney.drawFrame(5, col1X + 6, curY + 6, 0, 3, g);
            mFont.tahoma_7_white.drawString(g, "PK: " + (GameScreen.player != null ? GameScreen.player.KN : 0), col1X + 14, curY, 0);
            curY += lineH;

            g.setColor(0x554433);
            g.fillRect(boxX + 6, curY + 1, boxW - 12, 1);
            curY += dividerH;
        }

        // 2. Vẽ 6 Chỉ số nhân vật tự động xuống dòng nếu dài hoặc index trước dài
        if (totalItems > 0) {
            int drawIdx = 0;
            while (drawIdx < totalItems) {
                String l1 = (drawIdx < labels.length) ? labels[drawIdx] : ("Stat" + drawIdx);
                String v1 = (drawIdx < Player.RQ.length && Player.RQ[drawIdx] != null && Player.RQ[drawIdx].length() > 0) ? Player.RQ[drawIdx] : "0";
                String s1 = l1 + ": " + v1;
                mFont f1 = (drawIdx == 3 || drawIdx == 4 || drawIdx == 5) ? mFont.tahoma_7_yellow : mFont.tahoma_7_white;
                int w1 = f1.getWidth(s1);

                if (w1 > halfW - 2 || drawIdx + 1 >= totalItems) {
                    f1.drawString(g, s1, col1X, curY, 0);
                    curY += lineH;
                    drawIdx++;
                } else {
                    String l2 = (drawIdx + 1 < labels.length) ? labels[drawIdx + 1] : ("Stat" + (drawIdx + 1));
                    String v2 = (drawIdx + 1 < Player.RQ.length && Player.RQ[drawIdx + 1] != null && Player.RQ[drawIdx + 1].length() > 0) ? Player.RQ[drawIdx + 1] : "0";
                    String s2 = l2 + ": " + v2;
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

        if (leftStatList.AD > 0) {
            scrollLeftStat.setInfo(boxX + boxW - 3, statY + 2, viewH, 0xAA8800);
            scrollLeftStat.setYScrool(leftStatList.AC, leftStatList.AD);
            scrollLeftStat.paint(g);
        }

        // BÊN PHẢI: HÀNH TRANG (INVENTORY GRID)
        int rightPaneX = paneX + leftPaneW + 6;
        int rightPaneY = y + 28;
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int rightPaneH = h - 34;

        AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (byte)0, 1);

        // 5 Filter Tabs
        int fw = (rightPaneW - 8) / FILTER_NAMES.length;
        int fy = rightPaneY + 4;
        for (int f = 0; f < FILTER_NAMES.length; f++) {
            int fx = rightPaneX + 4 + f * fw;
            boolean isSelFilter = (f == curInvenFilter);
            boolean isFocusedFilter = (focusPane == 3 && isSelFilter);

            AvMain.paintRect(g, fx, fy, fw - 2, 16, (byte)(isSelFilter ? 1 : 0), (isSelFilter ? 0 : 1));
            if (isFocusedFilter) {
                g.setColor(0xFFFF00);
                g.drawRect(fx - 1, fy - 1, fw, 17);
            }
            if (isSelFilter && AvMain.imgNenfocus != null) {
                g.drawRegion(AvMain.imgNenfocus, 2, 2, fw - 2, 16, 0, fx, fy, 0);
            }
            String fLabel = (!isWide || fw < 36) ? (f == 0 ? "Tất" : (f == 1 ? "Đồ" : (f == 2 ? "Dược" : (f == 3 ? "Đá" : "Khác")))) : FILTER_NAMES[f];
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
            g.translate(0, -invenList.AC);

            int totalSlots = SLOTS_PER_TAB;
            int totalRows = (totalSlots + cols - 1) / cols;

            int firstRow = (invenList.AC - invSlotSize) / slotStep;
            if (firstRow < 0) firstRow = 0;
            int lastRow = (invenList.AC + gridH + invSlotSize) / slotStep;
            if (lastRow >= totalRows) lastRow = totalRows - 1;

            int startSlot = firstRow * cols;
            int endSlot = Math.min(totalSlots, (lastRow + 1) * cols);

            for (int i = startSlot; i < endSlot; i++) {
                int col = i % cols;
                int row = i / cols;
                int slotX = gridX + gridOffsetX + col * slotStep;
                int slotY = gridY + row * slotStep;
                boolean isSelItem = (focusPane == 1 && i == selectedInvenIndex);

                AvMain.paintRect(g, slotX, slotY, invSlotSize, invSlotSize, (byte)(isSelItem ? 1 : 0), 3);

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

                        if (item.typeObject == 4) {
                            if (item.numPotion > 1) {
                                mFont.tahoma_7_white.drawString(g, "" + item.numPotion, slotX + invSlotSize - 2, slotY + invSlotSize - 9, 1);
                            }
                        } else if (item.numPotion > 1) {
                            mFont.tahoma_7_white.drawString(g, "" + item.numPotion, slotX + invSlotSize - 2, slotY + invSlotSize - 9, 1);
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

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (invenList.AD > 0) {
                scrollInven.setInfo(gridX + gridW - 3, gridY, gridH, 0xFF8800);
                scrollInven.setYScrool(invenList.AC, invenList.AD);
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
            boolean isCurTab = (p == curInvenPage);
            AvMain.paintRect(g, bx, tabBoxY, pageBtnW, tabBoxH, (byte)(isCurTab ? 1 : 0), (isCurTab ? 0 : 1));
            if (isCurTab) {
                g.setColor(0xFFFF00);
                g.drawRect(bx, tabBoxY, pageBtnW - 1, tabBoxH - 1);
                mFont.tahoma_7b_yellow.drawString(g, "" + (p + 1), bx + pageBtnW / 2, tabBoxY + 1, 2);
            } else {
                mFont.tahoma_7_white.drawString(g, "" + (p + 1), bx + pageBtnW / 2, tabBoxY + 1, 2);
            }
        }

        int totalInven = (Player.vecInventory != null) ? Player.vecInventory.size() : 0;
        String capStr = totalInven + "/" + Player.maxInventory;
        mFont.tahoma_7_white.drawString(g, capStr, rightPaneX + rightPaneW - 4, tabBoxY + 1, 1);

        // Tooltip Popup khi chọn món đồ
        if (selectedItemInfo != null) {
            int infoW = (selectedItemInfo.BS > 0) ? selectedItemInfo.BS : 140;
            int maxPopupH = Math.max(60, Math.min(h - 54, MotherCanvas.h - 54));
            int showH = selectedItemInfo.BT - selectedItemInfo.CO;
            if (showH <= 0 || showH > maxPopupH) showH = (selectedItemInfo.BT > 0) ? Math.min(selectedItemInfo.BT, maxPopupH) : 60;
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
                    AvMain.paintRect(g, infoX, btnY, 60, 18, (byte)1, 1);
                    mFont.tahoma_7b_black.drawString(g, "Tháo/Đổi", infoX + 30, btnY + 3, 2);
                } else {
                    if (GameCanvas.tabInven == null) {
                        GameCanvas.tabInven = new TabInventory(T.AF, Player.vecInventory, (byte)0, MainTab.xTab);
                        GameCanvas.tabInven.initCmd();
                    }
                    GameCanvas.tabInven.itemCur = selectedItemInfo;
                    GameCanvas.tabInven.IdSelect = Player.vecInventory.indexOf(selectedItemInfo);
                    mVector mActions = selectedItemInfo.getActionInven((byte)0);
                    if (mActions != null && mActions.size() > 0) {
                        if (mActions.size() == 1) {
                            AvMain.paintRect(g, infoX, btnY, 60, 18, (byte)1, 1);
                            mFont.tahoma_7b_black.drawString(g, ((iCommand)mActions.elementAt(0)).caption, infoX + 30, btnY + 3, 2);
                        } else {
                            AvMain.paintRect(g, infoX, btnY, 52, 18, (byte)1, 1);
                            mFont.tahoma_7b_black.drawString(g, ((iCommand)mActions.elementAt(0)).caption, infoX + 26, btnY + 3, 2);
                            AvMain.paintRect(g, infoX + 56, btnY, 52, 18, (byte)1, 1);
                            mFont.tahoma_7b_black.drawString(g, (mActions.size() > 1 ? ((iCommand)mActions.elementAt(1)).caption : "Menu"), infoX + 56 + 26, btnY + 3, 2);
                        }
                    }
                }
            }
        }
    }
private void paintEquipSlot(mGraphics g, int sx, int sy, int size, int equipType) {
boolean isSel = (focusPane == 0 && selectedEquipIndex == equipType);
boolean isDivine = (equipType >= 8);
AvMain.paintRect(g, sx, sy, size, size, (byte)(isSel ? 1 : 0), 3);

MainItem item = (MainItem)GameScreen.player.hashEquip.get("" + equipType);
if (item != null) {
item.AC(g, sx + size / 2, sy + size / 2, size);
item.paint(g, sx + size / 2, sy + size / 2, size);
if (item.LvUpgrade > 0) {
    mFont.tahoma_7b_green.drawString(g, "+" + item.LvUpgrade, sx + 2, sy + 1, 0);
}
} else if (AvMain.fraEquip != null) {
int frameIdx = equipType % 8;
if (frameIdx < AvMain.fraEquip.nFrame) {
AvMain.fraEquip.drawFrame(frameIdx, sx + size / 2, sy + size / 2, 0, 3, g);
}
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

private void paintAttBuffText(mGraphics g, String text, int id, int px, int py) {
int num = 0;
if (GameScreen.player != null && GameScreen.player.vecEffBuff != null) {
for (int i = 0; i < GameScreen.player.vecEffBuff.size(); i++) {
MainBuff mainBuff = (MainBuff)GameScreen.player.vecEffBuff.elementAt(i);
if (mainBuff == null || mainBuff.AH == null || mainBuff.AH.size() <= 0) continue;
for (int j = 0; j < mainBuff.AH.size(); j++) {
MainInfoItem mainInfoItem = (MainInfoItem)mainBuff.AH.elementAt(j);
if (mainInfoItem != null && mainInfoItem.AA == id) {
num += mainInfoItem.AE;
break;
}
}
}
}
if (GameScreen.player != null && GameScreen.player.AG != null) {
for (int k = 0; k < GameScreen.player.AG.size(); k++) {
MainInfoItem mainInfoItem2 = (MainInfoItem)GameScreen.player.AG.elementAt(k);
if (mainInfoItem2 != null && mainInfoItem2.AA == id) {
num += mainInfoItem2.AE;
break;
}
}
}
if (num != 0) {
int width = mFont.tahoma_7_white.getWidth(text);
byte isPercent = 0;
if (MainItem.BZ != null && id >= 0 && id < MainItem.BZ.length && MainItem.BZ[id] != null) {
   isPercent = MainItem.BZ[id].AC;
} else {
   isPercent = MainItem.getFallbackAttributePercent(id);
}
String st = MainItem.AA(num, isPercent);
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
AvMain.paintRect(g, leftPaneX, leftPaneY, leftPaneW, leftPaneH, (byte)0, 1);

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
Interface_Game.AA(g, (byte)1, GameScreen.player.Hp, GameScreen.player.maxHp, barX, leftPaneY + 18, 0, 9, barW, 0, false, GameScreen.player.KI, false, GameScreen.player.MA);

// MP
if (imgIcon != null) {
g.drawRegion(imgIcon, 0, 10, 10, 10, 0, leftPaneX + 6, leftPaneY + 30, 0);
}
Interface_Game.AA(g, (byte)2, GameScreen.player.Mp, GameScreen.player.maxMp, barX, leftPaneY + 30, 0, 9, barW, 0, false, 0, false, 0);

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
g.translate(0, -infoList.AC);

try {
int curY = listY + 2;
if (GameScreen.player != null && GameScreen.player.AF != null && GameScreen.player.AF.size() > 0) {
for (int i = 0; i < GameScreen.player.AF.size(); i++) {
MainInfoItem infoItem = (MainInfoItem)GameScreen.player.AF.elementAt(i);
if (infoItem != null) {
String infoStr = MainItem.AA(infoItem);
if (infoStr != null && infoStr.length() > 0 && !infoStr.equals("null")) {
mFont.tahoma_7_white.drawString(g, infoStr, listX + 4, curY, 0);
paintAttBuffText(g, infoStr, infoItem.AA, listX + 4, curY);
curY += 15;
}
}
}
} else if (Player.RQ != null && Player.RQ.length >= 4) {
mFont.tahoma_7_white.drawString(g, "Sát thương: " + Player.RQ[0], listX + 4, curY, 0);
curY += 15;
mFont.tahoma_7_white.drawString(g, "Giáp: " + Player.RQ[1], listX + 4, curY, 0);
curY += 15;
mFont.tahoma_7_yellow.drawString(g, "Chí mạng: " + Player.RQ[2], listX + 4, curY, 0);
curY += 15;
mFont.tahoma_7_yellow.drawString(g, "Kháng: " + Player.RQ[3], listX + 4, curY, 0);
curY += 15;
}
} catch (Exception var20) {
} finally {
GameCanvas.resetTrans(g);
g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);
}

if (infoList.AD > 0) {
scrollInfo.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
scrollInfo.setYScrool(infoList.AC, infoList.AD);
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

AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (byte)0, 1);

String pointStr = T.tabAttribute + ": " + Player.AS;
mFont.tahoma_7b_yellow.drawString(g, pointStr, rightPaneX + 10, rightPaneY + 5, 0);

if (Player.AS > 0 && GameCanvas.gameTick % 10 < 8 && MainEvent.imgNew != null) {
int pw = mFont.tahoma_7b_yellow.getWidth(pointStr);
g.drawImage(MainEvent.imgNew, rightPaneX + 10 + pw + 8, rightPaneY + 8, 3);
}

if (Player.QF == null || Player.QF.length == 0) {
Player.QF = new Class_CV[5];
}
for (int i = 0; i < Player.QF.length; i++) {
if (Player.QF[i] == null) {
String name = (i < T.VZ.length) ? T.VZ[i] : ("Thuộc tính " + (i + 1));
Player.QF[i] = new Class_CV((byte)i, (short)0, (short)0, name, getDefaultAttributeMInfo(i, (short)0));
} else if (Player.QF[i].AD == null || Player.QF[i].AD.length == 0) {
Player.QF[i].AD = getDefaultAttributeMInfo(i, (short)(Player.QF[i].AA + Player.QF[i].AB));
}
}

int numAtt = Player.QF.length;
int rightListX = rightPaneX + 4;
int rightListY = rightPaneY + 20;
int rightListW = rightPaneW - 8;
int rightListH = rightPaneH - 24;

if (attriList != null) {
g.setClip(rightListX, rightListY, rightListW, rightListH);
g.translate(0, -attriList.AC);

            int curCardY = rightListY;
            for (int i = 0; i < numAtt; i++) {
                try {
                    Class_CV att = Player.QF[i];
                    if (att == null) continue;

                    MainInfoItem[] minfo = (att.AD != null && att.AD.length > 0) ? att.AD : getDefaultAttributeMInfo(i, (short)(att.AA + att.AB));

                    int cardH = getAttriCardHeight(att, i);
                    int cardX = rightListX + 2;
                    int cardW = rightListW - 4;
                    boolean isSel = (focusPane == 1 && i == selectedAttIndex);

                    AvMain.paintRect(g, cardX, curCardY, cardW, cardH, (byte)(isSel ? 1 : 0), 3);

                    // Tên thuộc tính & điểm
                    String aName = (att.AC != null && att.AC.length() > 0) ? att.AC : ((i < T.VZ.length && T.VZ[i] != null) ? T.VZ[i] : ("Thuộc tính " + (i + 1)));
                    String attName = aName + ": " + att.AA;
                    mFont.tahoma_7b_white.drawString(g, attName, cardX + 8, curCardY + 3, 0);

                    if (att.AB > 0) {
                        int nw = mFont.tahoma_7b_white.getWidth(attName + " ");
                        mFont.tahoma_7b_blue.drawString(g, "+" + att.AB, cardX + 8 + nw, curCardY + 3, 0);
                    }

                    // Nút (+) bên phải
                    int btnW = 28;
                    int btnH = 22;
                    int btnX = cardX + cardW - btnW - 4;
                    int btnY = curCardY + 2;
                    boolean canAdd = (Player.AS > 0 && att.AA < 80);
                    int btnIdx = (!canAdd) ? 2 : ((timeFocusAtt > 0 && selectedAttIndex == i) ? 1 : 0);

                    if (AvMain.fraButtonTiemNang != null) {
                        AvMain.fraButtonTiemNang.drawFrame(btnIdx, btnX + btnW / 2, btnY + btnH / 2, 0, 3, g);
                    } else {
                        AvMain.paintRect(g, btnX, btnY, btnW, btnH, (byte)(canAdd ? 1 : 0), 1);
                        mFont.tahoma_7b_yellow.drawString(g, "+", btnX + btnW / 2, btnY + 2, 2);
                    }

                    // Hiển thị các dòng chỉ số cộng ở dưới điểm
                    int statY = curCardY + 17;
                    if (minfo != null) {
                        for (int j = 0; j < minfo.length; j++) {
                            if (minfo[j] != null) {
                                String subInfo = MainItem.AA(minfo[j]);
                                if (subInfo != null && subInfo.length() > 0 && !subInfo.equals("null")) {
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
                } catch (Exception e) {
                    curCardY += 46;
                }
            }

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (attriList.AD > 0) {
                scrollAttri.setInfo(rightPaneX + rightPaneW - 3, rightPaneY + 28, rightPaneH - 32, 0xFF8800);
                scrollAttri.setYScrool(attriList.AC, attriList.AD);
                scrollAttri.paint(g);
            }
        }
    }

    private void paintTabSkillDual(mGraphics g) {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int leftPaneX = x + 10;
        int leftPaneY = y + 28;
        int leftPaneH = h - 34;

        AvMain.paintRect(g, leftPaneX, leftPaneY, leftPaneW, leftPaneH, (byte)0, 1);
        mFont.tahoma_7b_yellow.drawString(g, "DANH SÁCH KỸ NĂNG", leftPaneX + leftPaneW / 2, leftPaneY + 4, 2);

        int listX = leftPaneX + 4;
        int listY = leftPaneY + 18;
        int listW = leftPaneW - 8;
        int listH = leftPaneH - 22;

        if (skillList != null) {
            g.setClip(listX, listY, listW, listH);
            g.translate(0, -skillList.AC);

            if (validSkills != null) {
                for (int i = 0; i < validSkills.size(); i++) {
                    Skill_Info sk = (Skill_Info)validSkills.elementAt(i);
                    if (sk == null) continue;
                    int sy = listY + i * 34;
                    boolean isSel = (focusPane == 1 && i == selectedSkillIndex);

                    AvMain.paintRect(g, listX, sy, listW, 32, (byte)(isSel ? 1 : 0), 3);

                    // Vẽ icon kỹ năng
                    sk.paint(g, listX + 16, sy + 16);
                    if (sk.phanTramDevilSkill > 0 && AvMain.imgLvDevilSkill != null) {
                        int num5 = sk.phanTramDevilSkill / 5;
                        num5 = ((num5 < 20) ? (num5 + 1) : ((num5 != 20) ? 22 : (num5 + 2)));
                        g.drawRegion(AvMain.imgLvDevilSkill, 0, 22 - num5, 22, num5, 0, listX + 16, sy + 16 + 11, 33);
                    }

                    // Tên kỹ năng
                    String skName = sk.name;
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
                        String dStr = (sk.typeSkill == 1) ? T.devilFruitA : ((sk.typeSkill == 3) ? T.devilFruitP : T.devilFruitB);
                        mFont.tahoma_7_green.drawString(g, dStr, listX + 36, sy + 16, 0);
                    } else if (sk.typeDevil == 2) {
                        String hStr = (sk.typeSkill == 1) ? T.devilHakiA : ((sk.typeSkill == 3) ? T.devilHakiP : T.devilHakiB);
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

            if (skillList.AD > 0) {
                scrollSkill.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                scrollSkill.setYScrool(skillList.AC, skillList.AD);
                scrollSkill.paint(g);
            }
        }

        int rightPaneX = leftPaneX + leftPaneW + 6;
        int rightPaneY = y + 28;
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int rightPaneH = h - 34;

        AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (byte)0, 1);

        if (selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
            Skill_Info curSk = (Skill_Info)validSkills.elementAt(selectedSkillIndex);
            if (curSk != null) {
                curSk.paint(g, rightPaneX + 22, rightPaneY + 22);
                if (curSk.phanTramDevilSkill > 0 && AvMain.imgLvDevilSkill != null) {
                    int num5 = curSk.phanTramDevilSkill / 5;
                    num5 = ((num5 < 20) ? (num5 + 1) : ((num5 != 20) ? 22 : (num5 + 2)));
                    g.drawRegion(AvMain.imgLvDevilSkill, 0, 22 - num5, 22, num5, 0, rightPaneX + 22, rightPaneY + 22 + 11, 33);
                }

                String sName = curSk.name;
                if (curSk.Lv_RQ == Skill_Info.maxLv) sName += " " + T.max;
                mFont.tahoma_7b_yellow.drawStringAuto(g, sName, rightPaneX + 42, rightPaneY + 8, rightPaneW - 50, 0);

                String tagType = "[Chiêu Chủ Động]";
                if (curSk.typeDevil == 1) {
                    tagType = "[Ác Quỷ - " + ((curSk.typeSkill == 1) ? "Chủ động" : ((curSk.typeSkill == 3) ? "Nội tại" : "Hỗ trợ")) + "]";
                } else if (curSk.typeDevil == 2) {
                    tagType = "[Haki - " + ((curSk.typeSkill == 1) ? "Chủ động" : ((curSk.typeSkill == 3) ? "Nội tại" : "Hỗ trợ")) + "]";
                } else if (curSk.typeSkill == 2) {
                    tagType = "[Chiêu Hỗ Trợ - " + (curSk.typeBuff < T.mTacdung.length ? T.mTacdung[curSk.typeBuff] : "Buff") + "]";
                } else if (curSk.typeSkill == 3) {
                    tagType = "[Chiêu Nội Tại - Bị động]";
                } else if (curSk.typeSkill == 4) {
                    tagType = "[Chiêu Trên Biển]";
                } else if (curSk.typeSkill == 6) {
                    tagType = "[Kỹ Năng Nghề]";
                }
                mFont.tahoma_7_green.drawString(g, tagType, rightPaneX + 42, rightPaneY + 22, 0);

                boolean canAssignKey = (curSk.Lv_RQ >= 0 && (curSk.typeSkill == 1 || curSk.typeSkill == 2 || curSk.typeSkill == 4));
                int detailViewY = rightPaneY + 38;
                int detailViewW = rightPaneW - 8;
                int detailViewH = rightPaneH - 38 - (canAssignKey ? 26 : 4);

                if (skillDetailList != null) {
                    g.setClip(rightPaneX + 4, detailViewY, detailViewW, detailViewH);
                    g.translate(0, -skillDetailList.AC);

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
                            String progressText = "Cấp: " + curSk.Lv_RQ + "/" + Skill_Info.maxLv + "  (Tiến trình: " + MainItem.strGetPercent(curSk.percentLv, 1) + ")";
                            mFont.tahoma_7_white.drawString(g, progressText, rightPaneX + 14, curDetailY, 0);
                            curDetailY += 14;
                            int pBarW = rightPaneW - 28;
                            if (pBarW > 10) {
                                Interface_Game.PaintHPMP(g, 3, curSk.percentLv, 100, rightPaneX + 14, curDetailY, 0, 9, pBarW, 1, false, 0, false, 0);
                                curDetailY += 13;
                            }
                        }
                    }

                    if (curSk.manaLost > 0) {
                        mFont.tahoma_7_blue.drawString(g, "Tiêu hao: " + curSk.manaLost + " MP", rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    if (curSk.timeDelay > 0) {
                        mFont.tahoma_7_yellow.drawString(g, "Thời gian hồi: " + (curSk.timeDelay / 1000) + "." + ((curSk.timeDelay % 1000) / 100) + " giây", rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    if (curSk.range > 0) {
                        mFont.tahoma_7_white.drawString(g, "Phạm vi: " + curSk.range + " px", rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    if (curSk.nTarget > 0) {
                        mFont.tahoma_7_white.drawString(g, "Số mục tiêu: " + curSk.nTarget, rightPaneX + 14, curDetailY, 0);
                        curDetailY += 14;
                    }

                    g.setColor(0x886633);
                    g.drawLine(rightPaneX + 14, curDetailY + 2, rightPaneX + rightPaneW - 14, curDetailY + 2);
                    curDetailY += 8;

                    if (curSk.strInfo != null && curSk.strInfo.length > 0) {
                        for (int lineIdx = 0; lineIdx < curSk.strInfo.length; lineIdx++) {
                            String infoLine = curSk.strInfo[lineIdx];
                            if (infoLine != null && infoLine.length() > 0) {
                                String[] wrappedLines = mFont.tahoma_7_white.splitFontArray(infoLine, rightPaneW - 28);
                                if (wrappedLines != null) {
                                    for (int wl = 0; wl < wrappedLines.length; wl++) {
                                        mFont.tahoma_7_white.drawString(g, wrappedLines[wl], rightPaneX + 14, curDetailY, 0);
                                        curDetailY += 13;
                                    }
                                }
                            }
                        }
                    }

                    GameCanvas.resetTrans(g);
                    g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

                    if (skillDetailList.AD > 0) {
                        scrollSkill.setInfo(rightPaneX + rightPaneW - 6, detailViewY, detailViewH, 0xFF8800);
                        scrollSkill.setYScrool(skillDetailList.AC, skillDetailList.AD);
                        scrollSkill.paint(g);
                    }
                }

                if (canAssignKey) {
                    int btnY = rightPaneY + rightPaneH - 24;
                    AvMain.paintRect(g, rightPaneX + 14, btnY, 80, 18, (byte)1, 1);
                    mFont.tahoma_7b_black.drawString(g, "Gán Phím Tắt", rightPaneX + 54, btnY + 3, 2);
                }
            }
        }
    }

    private void paintTabQuestDual(mGraphics g) {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int leftPaneX = x + 10;
        int leftPaneY = y + 28;
        int leftPaneH = h - 34;

        AvMain.paintRect(g, leftPaneX, leftPaneY, leftPaneW, leftPaneH, (byte)0, 1);
        mFont.tahoma_7b_yellow.drawString(g, "DANH SÁCH NHIỆM VỤ", leftPaneX + leftPaneW / 2, leftPaneY + 4, 2);

        int listX = leftPaneX + 4;
        int listY = leftPaneY + 18;
        int listW = leftPaneW - 8;
        int listH = leftPaneH - 22;

        if (questList != null) {
            g.setClip(listX, listY, listW, listH);
            g.translate(0, -questList.AC);

            if (Player.QI != null) {
                for (int i = 0; i < Player.QI.size(); i++) {
                    MainQuest q = (MainQuest)Player.QI.elementAt(i);
                    if (q == null) continue;
                    int qy = listY + i * 34;
                    boolean isSel = (focusPane == 1 && i == selectedQuestIndex);

                    AvMain.paintRect(g, listX, qy, listW, 32, (byte)(isSel ? 1 : 0), 3);

                    if (AvMain.fraQuest != null) {
                        AvMain.fraQuest.drawFrame(q.AB + 1, listX + 12, qy + 16, 0, 3, g);
                    }

                    mFont.tahoma_7b_white.drawStringAuto(g, q.AH + q.AA(), listX + 28, qy + 2, listW - 32, 0);
                    if (q.AL != null && q.AL.length() > 0) {
                        mFont.tahoma_7_white.drawStringAuto(g, q.AL, listX + 28, qy + 16, listW - 32, 0);
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

            if (questList.AD > 0) {
                scrollQuest.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                scrollQuest.setYScrool(questList.AC, questList.AD);
                scrollQuest.paint(g);
            }
        }

        int rightPaneX = leftPaneX + leftPaneW + 6;
        int rightPaneY = y + 28;
        int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
        int rightPaneH = h - 34;

        AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (byte)0, 1);

        if (Player.QI != null && selectedQuestIndex >= 0 && selectedQuestIndex < Player.QI.size()) {
            MainQuest curQ = (MainQuest)Player.QI.elementAt(selectedQuestIndex);
            if (curQ != null) {
                mFont.tahoma_7b_yellow.drawStringAuto(g, curQ.AH + curQ.AA(), rightPaneX + 14, rightPaneY + 8, rightPaneW - 24, 0);
                if (curQ.AL != null && curQ.AL.length() > 0) {
                    mFont.tahoma_7_blue.drawString(g, "Vị trí: " + curQ.AL, rightPaneX + 14, rightPaneY + 24, 0);
                }

                int startGoalY = rightPaneY + 40;
                if (curQ.AM != null && curQ.AM.size() > 0) {
                    for (int m = 0; m < curQ.AM.size() && m < 4; m++) {
                        Class_CP goal = (Class_CP)curQ.AM.elementAt(m);
                        if (goal != null) {
                            String gTxt = "- " + goal.AE + ": " + goal.AD + "/" + goal.AC;
                            mFont.tahoma_7_white.drawString(g, gTxt, rightPaneX + 14, startGoalY + m * 14, 0);
                        }
                    }
                    startGoalY += curQ.AM.size() * 14;
                }

                if (curQ.AK != null && curQ.AK.length() > 0) {
                    String[] qLines = mFont.tahoma_7_white.splitFontArray(curQ.AK, rightPaneW - 28);
                    if (qLines != null) {
                        for (int li = 0; li < qLines.length; li++) {
                            if (startGoalY + 14 > rightPaneY + rightPaneH - 28) break;
                            mFont.tahoma_7_white.drawString(g, qLines[li], rightPaneX + 14, startGoalY, 0);
                            startGoalY += 14;
                        }
                    }
                }

                int btnY = rightPaneY + rightPaneH - 24;
                AvMain.paintRect(g, rightPaneX + 14, btnY, 96, 18, (byte)1, 1);
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

if (index == 0) { // Danh Hiệu
FrameImage fra0 = QuickMenu.getFraQuickMenu(16);
if (fra0 != null && fra0.imgFrame != null && fra0.imgFrame.image != null) {
fra0.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraBanhLai != null) {
AvMain.fraBanhLai.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraPirate != null) {
AvMain.fraPirate.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "DH", x, y - 5, 2);
}
} else if (index == 1) { // Thú Cưng
FrameImage fra1 = QuickMenu.getFraQuickMenu(15);
if (fra1 != null && fra1.imgFrame != null && fra1.imgFrame.image != null) {
fra1.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraBorderClan2 != null) {
AvMain.fraBorderClan2.drawFrameNew(GameCanvas.gameTick / 4 % AvMain.fraBorderClan2.maxNumFrame, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "PET", x, y - 5, 2);
}
} else if (index == 2) { // Băng Hải Tặc
FrameImage fra2 = QuickMenu.getFraQuickMenu(10);
if (fra2 != null && fra2.imgFrame != null && fra2.imgFrame.image != null) {
fra2.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraBorderClan != null) {
AvMain.fraBorderClan.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "CLAN", x, y - 5, 2);
}
} else if (index == 3) { // Bạn Bè & Kẻ Thù
FrameImage fra3 = QuickMenu.getFraQuickMenu(0);
if (fra3 != null && fra3.imgFrame != null && fra3.imgFrame.image != null) {
fra3.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraStatusOnline != null) {
AvMain.fraStatusOnline.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "FR", x, y - 5, 2);
}
} else if (index == 4) { // Đội Ngũ
FrameImage fra4 = QuickMenu.getFraQuickMenu(6);
if (fra4 != null && fra4.imgFrame != null && fra4.imgFrame.image != null) {
fra4.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraBorderClan2 != null) {
AvMain.fraBorderClan2.drawFrameNew(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "PT", x, y - 5, 2);
}
} else if (index == 5) { // Trạng Thái PK
FrameImage fra5 = QuickMenu.getFraQuickMenu(3);
if (fra5 != null && fra5.imgFrame != null && fra5.imgFrame.image != null) {
fra5.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraPk != null) {
AvMain.fraPk.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "PK", x, y - 5, 2);
}
} else if (index == 6) { // Sư Đồ
FrameImage fra6 = QuickMenu.getFraQuickMenu(14);
if (fra6 != null && fra6.imgFrame != null && fra6.imgFrame.image != null) {
fra6.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraPirate != null) {
AvMain.fraPirate.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "SD", x, y - 5, 2);
}
} else if (index == 7) { // Chợ Đấu Giá
FrameImage fra7 = QuickMenu.getFraQuickMenu(13);
if (fra7 != null && fra7.imgFrame != null && fra7.imgFrame.image != null) {
fra7.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraMoney != null) {
AvMain.fraMoney.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "CHỢ", x, y - 5, 2);
}
} else if (index == 8) { // Kênh Thế Giới & Loa
FrameImage fra8 = QuickMenu.getFraQuickMenu(8);
if (fra8 != null && fra8.imgFrame != null && fra8.imgFrame.image != null) {
fra8.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fratf != null) {
AvMain.fratf.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "CHAT", x, y - 5, 2);
}
} else if (index == 9) { // Set Trang Bị
FrameImage fra9 = QuickMenu.getFraQuickMenu(12);
if (fra9 != null && fra9.imgFrame != null && fra9.imgFrame.image != null) {
fra9.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraUniform != null) {
AvMain.fraUniform.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "SET", x, y - 5, 2);
}
} else if (index == 10) { // Auto Game
FrameImage fra10 = QuickMenu.getFraQuickMenu(4);
if (fra10 != null && fra10.imgFrame != null && fra10.imgFrame.image != null) {
fra10.drawFrame(0, x, y, 0, 3, g);
} else if (AvMain.fraAutoFire != null) {
AvMain.fraAutoFire.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "AUTO", x, y - 5, 2);
}
} else if (index == 11) { // Tiện Ích MOD
FrameImage fraMod = (AvMain.fraMenuAuto != null) ? AvMain.fraMenuAuto : AvMain.fraCheck;
if (fraMod != null) {
int fr = (fraMod == AvMain.fraMenuAuto) ? ((GameCanvas.gameTick / 6) % 3) : 1;
fraMod.drawFrame(fr, x, y, 0, 3, g);
} else {
FrameImage fra1 = QuickMenu.getFraQuickMenu(1);
if (fra1 != null && fra1.imgFrame != null && fra1.imgFrame.image != null) {
fra1.drawFrame(0, x, y, 0, 3, g);
} else {
mFont.tahoma_7b_yellow.drawString(g, "MOD", x, y - 5, 2);
}
}
} else {
mFont.tahoma_7b_yellow.drawString(g, "*", x, y - 5, 2);
}
} catch (Exception e) {
}
}

private void paintMenuChucNang(mGraphics g) {
int paneX = x + 10;
int paneY = y + 28;
int paneW = w - 20;
int paneH = h - 34;

AvMain.paintRect(g, paneX, paneY, paneW, paneH, (byte)0, 1);

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
            g.translate(0, -featureMenuList.AC);

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
                    boolean isSel = (focusPane == 1 && i == selectedChucNangIndex);

                    // Nền hàng item: index 0 khi chọn (sáng hơn), index 3 khi không chọn (tối, tương phản rõ trên panel nền 1)
                    AvMain.paintRect(g, itemX + 2, itemY + 2, itemW - 4, itemH - 4, (byte)(isSel ? 1 : 0), (byte)(isSel ? 0 : 3));
                    if (isSel) {
                        g.setColor(0xFFFF00);
                        g.drawRect(itemX + 1, itemY + 1, itemW - 3, itemH - 3);
                        if (AvMain.imgNenfocus != null) {
                            g.drawRegion(AvMain.imgNenfocus, 2, 2, Math.min(itemW - 4, 32), Math.min(itemH - 4, 32), 0, itemX + 2, itemY + 2, 0);
                        }
                    }

                    // Ô chứa Icon PNG ở đầu hàng
                    int icBoxX = itemX + 4;
                    int icBoxY = itemY + (itemH - icBoxSize) / 2;
                    AvMain.paintRect(g, icBoxX, icBoxY, icBoxSize, icBoxSize, (byte)0, (byte)(isSel ? 3 : 1));

                    int icCenterX = icBoxX + icBoxSize / 2;
                    int icCenterY = icBoxY + icBoxSize / 2;

                    paintFeatureIcon(g, i, icCenterX, icCenterY);

                    // Tên chức năng và thông tin trạng thái
                    String category = getFeatureCategory(i);
                    String title = getFeatureTitle(i);
                    String subText = getFeatureSubText(i);

                    int textX = itemX + icBoxSize + 8;
                    int maxTextW = itemW - icBoxSize - 22;

                    if (cols == 2) {
                        // Chế độ màn hình ngang 2 cột
                        mFont.tahoma_7_yellow.drawString(g, category + " ", textX, itemY + 3, 0);
                        int badgeW = mFont.tahoma_7_yellow.getWidth(category + " ");
                        (isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white).drawString(g, title, textX + badgeW, itemY + 3, 0);

                        String subShort = subText;
                        if (mFont.tahoma_7_white.getWidth(subShort) > maxTextW && subShort.length() > 22) {
                            subShort = subShort.substring(0, 20) + "...";
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
                } catch (Exception e) {}
            }

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (featureMenuList.AD > 0) {
                scrollFeatureMenu.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                scrollFeatureMenu.setYScrool(featureMenuList.AC, featureMenuList.AD);
                scrollFeatureMenu.paint(g);
            }
        }
    }

    private void paintTabDanhHieu(mGraphics g) {
        int paneX = x + 10;
        int paneY = y + 28;
        int paneW = w - 20;
        int paneH = h - 34;

        AvMain.paintRect(g, paneX, paneY, paneW, paneH, (byte)0, 1);

        // Nút Quay Lại mini
        int backBtnX = paneX + 6;
        int backBtnY = paneY + 4;
        int backBtnW = 60;
        int backBtnH = 16;
        AvMain.paintRect(g, backBtnX, backBtnY, backBtnW, backBtnH, (byte)1, 1);
        mFont.tahoma_7b_black.drawString(g, "< Quay Lại", backBtnX + backBtnW / 2, backBtnY + 2, 2);

        mFont.tahoma_7b_yellow.drawString(g, "DANH HIỆU NHÂN VẬT", paneX + paneW / 2, paneY + 5, 2);

        int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
        mFont.tahoma_7_white.drawString(g, "(" + dhCount + ")", paneX + paneW - 10, paneY + 5, 1);

        if (isWide) {
            int listX = paneX + 6;
            int listY = paneY + 24;
            int listW = (paneW - 18) * 55 / 100;
            int listH = paneH - 30;

            AvMain.paintRect(g, listX, listY, listW, listH, (byte)0, 1);

            if (danhHieuList != null) {
                g.setClip(listX, listY, listW, listH);
                g.translate(0, -danhHieuList.AC);

                int itemH = 38;
                for (int i = 0; i < dhCount; i++) {
                    DanhHieuInfo dh = (DanhHieuInfo)Player.vecDanhHieu.elementAt(i);
                    if (dh == null) continue;

                    int itemY = listY + i * itemH;
                    boolean isSel = (i == selectedDanhHieuIndex);

                    AvMain.paintRect(g, listX + 2, itemY + 2, listW - 4, itemH - 4, (byte)(isSel ? 1 : 0), 1);
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

                    String firstOpt = (dh.optionsStr != null && dh.optionsStr.length() > 0) ? dh.optionsStr : "Không có thuộc tính";
                    int nlIdx = firstOpt.indexOf('\n');
                    if (nlIdx != -1) firstOpt = firstOpt.substring(0, nlIdx);
                    mFont.tahoma_7_white.drawString(g, firstOpt, listX + 8, itemY + 18, 0);
                }

                if (dhCount == 0) {
                    mFont.tahoma_7_white.drawString(g, "Đang tải danh hiệu...", listX + listW / 2, listY + listH / 2 - 6, 2);
                }

                GameCanvas.resetTrans(g);
                g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

                if (danhHieuList.AD > 0) {
                    scrollDanhHieu.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                    scrollDanhHieu.setYScrool(danhHieuList.AC, danhHieuList.AD);
                    scrollDanhHieu.paint(g);
                }
            }

            // Bảng Chi Tiết Danh Hiệu bên phải
            int detailX = listX + listW + 6;
            int detailY = listY;
            int detailW = paneW - listW - 18;
            int detailH = listH;

            AvMain.paintRect(g, detailX, detailY, detailW, detailH, (byte)0, 1);

            if (selectedDanhHieuInfo != null) {
                mFont.tahoma_7b_yellow.drawString(g, "★ " + selectedDanhHieuInfo.name + " ★", detailX + detailW / 2, detailY + 5, 2);

                // Khung Showcase Hiệu Ứng Động
                int stageX = detailX + 8;
                int stageY = detailY + 18;
                int stageW = detailW - 16;
                int stageH = 46;
                AvMain.paintRect(g, stageX, stageY, stageW, stageH, (byte)1, 1);
                g.setColor(0xFFA500);
                g.drawRect(stageX, stageY, stageW, stageH);

                if (selectedDanhHieuInfo.idEff > 0) {
                    if (previewTitleEff == null || lastPreviewTitleEffId != selectedDanhHieuInfo.idEff) {
                        lastPreviewTitleEffId = selectedDanhHieuInfo.idEff;
                        previewTitleEff = new DataSkillEff(selectedDanhHieuInfo.idEff, -1, (byte)0, (byte)0);
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

                if (selectedDanhHieuInfo.optionsStr != null && selectedDanhHieuInfo.optionsStr.length() > 0) {
                    String[] wrapped = mFont.tahoma_7_white.splitFontArray(selectedDanhHieuInfo.optionsStr, detailW - 16);
                    if (wrapped != null) {
                        for (int wIdx = 0; wIdx < wrapped.length && curTextY + 11 <= maxTextY; wIdx++) {
                            String line = wrapped[wIdx];
                            if (line.startsWith("Hạn") || line.startsWith("Còn") || line.indexOf("ngày") != -1 || line.indexOf("giờ") != -1 || line.indexOf("phút") != -1) {
                                mFont.tahoma_7_yellow.drawString(g, line, detailX + 8, curTextY, 0);
                            } else if (line.startsWith("Thuộc tính:") || line.startsWith("★")) {
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

                        AvMain.paintRect(g, actBtnX, actBtnY, actBtnW, 18, (byte)1, 1);
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

            AvMain.paintRect(g, listX, listY, listW, listH, (byte)0, 1);

            if (danhHieuList != null) {
                g.setClip(listX, listY, listW, listH);
                g.translate(0, -danhHieuList.AC);

                int itemH = 38;
                for (int i = 0; i < dhCount; i++) {
                    DanhHieuInfo dh = (DanhHieuInfo)Player.vecDanhHieu.elementAt(i);
                    if (dh == null) continue;

                    int itemY = listY + i * itemH;
                    boolean isSel = (i == selectedDanhHieuIndex);

                    AvMain.paintRect(g, listX + 2, itemY + 2, listW - 4, itemH - 4, (byte)(isSel ? 1 : 0), 1);
                    mFont titleFont = (dh.state == 2) ? mFont.tahoma_7b_green : (isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white);
                    titleFont.drawString(g, dh.name, listX + 8, itemY + 4, 0);

                    if (dh.state == 2) {
                        mFont.tahoma_7b_green.drawString(g, "[Đang Dùng]", listX + listW - 8, itemY + 4, 1);
                    } else if (dh.state == 1) {
                        mFont.tahoma_7_yellow.drawString(g, "[Đã Có]", listX + listW - 8, itemY + 4, 1);
                    } else {
                        mFont.tahoma_7_white.drawString(g, "[Chưa Có]", listX + listW - 8, itemY + 4, 1);
                    }

                    String firstOpt = (dh.optionsStr != null && dh.optionsStr.length() > 0) ? dh.optionsStr : "Không có thuộc tính";
                    int nlIdx = firstOpt.indexOf('\n');
                    if (nlIdx != -1) firstOpt = firstOpt.substring(0, nlIdx);
                    mFont.tahoma_7_white.drawString(g, firstOpt, listX + 8, itemY + 18, 0);
                }

                if (dhCount == 0) {
                    mFont.tahoma_7_white.drawString(g, "Đang tải danh hiệu...", listX + listW / 2, listY + listH / 2 - 6, 2);
                }

                GameCanvas.resetTrans(g);
                g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

                if (danhHieuList.AD > 0) {
                    scrollDanhHieu.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
                    scrollDanhHieu.setYScrool(danhHieuList.AC, danhHieuList.AD);
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

        AvMain.paintRect(g, paneX, paneY, paneW, paneH, (byte)0, 1);

        // Nút Quay Lại mini
        int backBtnX = paneX + 6;
        int backBtnY = paneY + 4;
        int backBtnW = 60;
        int backBtnH = 16;
        AvMain.paintRect(g, backBtnX, backBtnY, backBtnW, backBtnH, (byte)1, 1);
        mFont.tahoma_7b_black.drawString(g, "< Quay Lại", backBtnX + backBtnW / 2, backBtnY + 2, 2);

        int petCount = (Player.vecPet != null) ? Player.vecPet.size() : 0;
        String titleStr = "DANH SÁCH THÚ CƯNG (" + petCount + "/" + Player.maxInventory + ")";
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
            g.translate(0, -petList.AC);

            int totalSlots = Math.max(Player.maxInventory, Math.max(petCount, cols * 4));

            for (int i = 0; i < totalSlots; i++) {
                int col = i % cols;
                int row = i / cols;
                int slotX = gridX + col * slotStep;
                int slotY = gridY + row * slotStep;
                boolean isSel = (focusPane == 1 && i == selectedPetIndex);

                AvMain.paintRect(g, slotX, slotY, slotSize, slotSize, (byte)(isSel ? 1 : 0), 3);

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

            GameCanvas.resetTrans(g);
            g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

            if (petList.AD > 0) {
                scrollPet.setInfo(gridX + gridW - 3, gridY, gridH, 0xFF8800);
                scrollPet.setYScrool(petList.AC, petList.AD);
                scrollPet.paint(g);
            }
        }

        if (selectedPetInfo != null) {
            int infoW = (selectedPetInfo.BS > 0) ? selectedPetInfo.BS : 140;
            int maxPopupH = Math.max(60, Math.min(h - 54, MotherCanvas.h - 54));
            int showH = selectedPetInfo.BT - selectedPetInfo.CO;
            if (showH <= 0 || showH > maxPopupH) showH = (selectedPetInfo.BT > 0) ? Math.min(selectedPetInfo.BT, maxPopupH) : 60;
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
                boolean isEquipped = (selectedPetInfo.colorName == 1);
                AvMain.paintRect(g, infoX, btnY, 60, 18, (byte)1, 1);
                mFont.tahoma_7b_black.drawString(g, isEquipped ? "Tháo" : "Dùng", infoX + 30, btnY + 3, 2);
            }
        }
    }

    public void updatePointer() {
        super.updatePointer();

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

            if (GameCanvas.isPointerDown || GameCanvas.AQ) {
                if (!isDraggingEquip) {
                    if (GameCanvas.isPoint(equipAreaX, equipAreaY, equipAreaW, equipAreaH)) {
                        isDraggingEquip = true;
                        equipDragStartY = GameCanvas.AZ;
                        equipDragStartScrollY = equipScrollY;
                    }
                } else {
                    int deltaY = GameCanvas.AZ - equipDragStartY;
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

        if (cmdClose != null && cmdClose.perform(GameCanvas.AY, GameCanvas.AZ)) {
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
        int fw = (rightPaneW - 8) / FILTER_NAMES.length;
        int fy = rightPaneY + 4;
        for (int f = 0; f < FILTER_NAMES.length; f++) {
            int fx = rightPaneX + 4 + f * fw;
            if (GameCanvas.isPoint(fx, fy, fw - 2, 16)) {
                GameCanvas.isPointerSelect = false;
                selectItem(null, 0, 0);
                selectedInvenIndex = -1;
                curInvenFilter = f;
                focusPane = 3;
                updateFilteredItems();
                if (this.invenList != null) {
                    this.invenList.AC = 0;
                    this.invenList.AB = 0;
                }
                updateSoftKeys();
                return;
            }
        }

        // 3. Kiem tra nut popup tooltip
        if (selectedItemInfo != null) {
            int infoW = (selectedItemInfo.BS > 0) ? selectedItemInfo.BS : 140;
            int maxPopupH = Math.max(60, Math.min(h - 54, MotherCanvas.h - 54));
            int showH = selectedItemInfo.BT - selectedItemInfo.CO;
            if (showH <= 0 || showH > maxPopupH) showH = (selectedItemInfo.BT > 0) ? Math.min(selectedItemInfo.BT, maxPopupH) : 60;
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
                    int leftSlotX = paneX + 7;
                    int rightSlotX = paneX + leftPaneW - 25 - 7;
                    int startSlotY = equipAreaY + 18;
                    int slotGapY = 25;
                    int row = (selectedEquipIndex % 8) / 2;
                    boolean isLeft = (selectedEquipIndex % 2 == 0);
                    int px = isLeft ? (leftSlotX + 13) : (rightSlotX + 13);
                    int py = startSlotY + row * slotGapY + 13;
                    changeEquip(selectedEquipIndex, px, py);
                    return;
                }
            } else {
                if (GameCanvas.tabInven == null) {
                    GameCanvas.tabInven = new TabInventory(T.AF, Player.vecInventory, (byte)0, MainTab.xTab);
                    GameCanvas.tabInven.initCmd();
                }
                GameCanvas.tabInven.itemCur = selectedItemInfo;
                GameCanvas.tabInven.IdSelect = Player.vecInventory.indexOf(selectedItemInfo);
                mVector mActions = selectedItemInfo.getActionInven((byte)0);

                if (mActions != null && mActions.size() > 0) {
                    if (mActions.size() == 1) {
                        if (GameCanvas.isPoint(infoX, popBtnY, 60, 18)) {
                            GameCanvas.isPointerSelect = false;
                            ((iCommand)mActions.elementAt(0)).AD();
                            isRefresh = true;
                            return;
                        }
                    } else if (mActions.size() == 2) {
                        if (GameCanvas.isPoint(infoX, popBtnY, 52, 18)) {
                            GameCanvas.isPointerSelect = false;
                            ((iCommand)mActions.elementAt(0)).AD();
                            isRefresh = true;
                            return;
                        }
                        if (GameCanvas.isPoint(infoX + 56, popBtnY, 52, 18)) {
                            GameCanvas.isPointerSelect = false;
                            ((iCommand)mActions.elementAt(1)).AD();
                            isRefresh = true;
                            return;
                        }
                    } else {
                        if (GameCanvas.isPoint(infoX, popBtnY, 52, 18)) {
                            GameCanvas.isPointerSelect = false;
                            ((iCommand)mActions.elementAt(0)).AD();
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
                    selectItem(eq, GameCanvas.AY, GameCanvas.AZ);
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
                    selectItem(eq, GameCanvas.AY, GameCanvas.AZ);
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
            int relX = GameCanvas.AY - gridX - gridOffsetX;
            int relY = GameCanvas.AZ - gridY + (invenList != null ? invenList.AC : 0);
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
                            selectItem(it, GameCanvas.AY, GameCanvas.AZ);
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
        int leftPaneW = isWide ? Math.max(160, w * 48 / 100) : (w * 48 / 100);
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
            int numAtt = (Player.QF != null && Player.QF.length > 0) ? Player.QF.length : 5;
            int curCardY = rightListY;

            for (int i = 0; i < numAtt; i++) {
                Class_CV att = (Player.QF != null && i < Player.QF.length) ? Player.QF[i] : null;
                int cardH = getAttriCardHeight(att, i);
                int cardX = rightListX + 2;
                int cardW = rightListW - 4;
                int scrCardY = curCardY - attriList.AC;

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
                int sy = listY + i * 34 - skillList.AC;
                if (GameCanvas.isPoint(leftPaneX + 4, sy, listW, 32)) {
                    GameCanvas.isPointerSelect = false;
                    focusPane = 1;
                    if (selectedSkillIndex != i) {
                        selectedSkillIndex = i;
                        if (skillDetailList != null) { skillDetailList.AC = 0; skillDetailList.AB = 0; }
                    }
                    updateSoftKeys();
                    return;
                }
            }
        }

        int rightPaneX = leftPaneW + 6 + x + 10;
        int rightPaneY = y + 28;
        int rightPaneH = h - 34;
        int btnY = rightPaneY + rightPaneH - 24;

        if (GameCanvas.isPoint(rightPaneX + 14, btnY, 80, 18)) {
            GameCanvas.isPointerSelect = false;
            openSkillHotkeyMenu();
            return;
        }
    }

    private void handlePointerTab3() {
        int leftPaneW = isWide ? 160 : (w * 45 / 100);
        int leftPaneX = x + 10;
        int listY = y + 28 + 18;
        int listW = leftPaneW - 8;

        if (Player.QI != null && questList != null) {
            for (int i = 0; i < Player.QI.size(); i++) {
                int qy = listY + i * 34 - questList.AC;
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
                int relX = GameCanvas.AY - listX;
                int relY = GameCanvas.AZ - listY + featureMenuList.AC;
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
                int relY = GameCanvas.AZ - listY + danhHieuList.AC;
                int itemH = 38;
                int clickedIdx = relY / itemH;
                if (clickedIdx >= 0 && clickedIdx < dhCount) {
                    GameCanvas.isPointerSelect = false;
                    selectedDanhHieuIndex = clickedIdx;
                    selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(clickedIdx);
                    if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) {
                        TitleActionBtn btn = (TitleActionBtn)selectedDanhHieuInfo.actionButtons.elementAt(0);
                        if (btn != null) {
                            GlobalService.getInstance().Send_DanhHieuAction(selectedDanhHieuInfo.id, btn.actionId);
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
                int infoW = (selectedPetInfo.BS > 0) ? selectedPetInfo.BS : 140;
                int showH = selectedPetInfo.BT - selectedPetInfo.CO;
                if (showH <= 0 || showH > h - 42) showH = (selectedPetInfo.BT > 0) ? Math.min(selectedPetInfo.BT, h - 42) : 60;
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
                    boolean isEquipped = (selectedPetInfo.colorName == 1);
                    GlobalService.getInstance().Send_Pet((byte)4, (byte)(isEquipped ? 0 : 1), selectedPetInfo.ID);
                    selectPet(null, 0, 0);
                    selectedPetIndex = -1;
                    updateSoftKeys();
                    return;
                }
            }

            if (petList != null && GameCanvas.isPoint(gridX, gridY, gridW, gridH)) {
                int relX = GameCanvas.AY - gridX;
                int relY = GameCanvas.AZ - gridY + petList.AC;
                int clickedCol = relX / slotStep;
                int clickedRow = relY / slotStep;

                if (clickedCol >= 0 && clickedCol < cols && clickedRow >= 0) {
                    int clickedIdx = clickedRow * cols + clickedCol;
                    if (Player.vecPet != null && clickedIdx >= 0 && clickedIdx < Player.vecPet.size()) {
                        GameCanvas.isPointerSelect = false;
                        focusPane = 1;
                        MainItem curPet = (MainItem)Player.vecPet.elementAt(clickedIdx);
                        if (selectedPetIndex == clickedIdx && selectedPetInfo != null) {
                            boolean isEquipped = (curPet.colorName == 1);
                            GlobalService.getInstance().Send_Pet((byte)4, (byte)(isEquipped ? 0 : 1), curPet.ID);
                            selectPet(null, 0, 0);
                            selectedPetIndex = -1;
                        } else {
                            selectedPetIndex = clickedIdx;
                            selectPet(curPet, GameCanvas.AY, GameCanvas.AZ);
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
