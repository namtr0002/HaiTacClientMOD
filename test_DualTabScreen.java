public class DualTabScreen extends MainScreen {
public static DualTabScreen instance;

public static final String[] TAB_NAMES = new String[] { "Trang Bị & Túi", "Tiềm Năng", "Kỹ Năng", "Nhiệm Vụ", "Thú Cưng" };
public static final String[] FILTER_NAMES = new String[] { "Tất Cả", "Trang Bị", "Dược Phẩm", "Đá/NL", "Khác" };

public int x;
public int y;
public int w;
public int h;
public int maxWShow;

public boolean isWide;
public int curMainTab = 0;
public int curInvenFilter = 0;
public boolean isRefresh = false;

public boolean[] isTabInitialized = new boolean[5];
public boolean[] isTabDirty = new boolean[5];



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

public void Show(MainScreen screen) {
super.Show(screen);
instance = this;

w = MotherCanvas.w - 12;
if (w > 560) w = 560;
if (w < 120) w = MotherCanvas.w - 4;

h = MotherCanvas.h - 16;
if (h > 270) h = 270;
if (h < 120) h = MotherCanvas.h - 4;

x = MotherCanvas.hw - w / 2;
y = MotherCanvas.hh - h / 2;
maxWShow = 0;

isWide = (w >= 280);

if (GameCanvas.isTouch && MainTab.fraCloseTab != null) {
cmdClose.setPos(x + w - 16, y + 14, MainTab.fraCloseTab, "");
}

if (GameCanvas.tabInven == null) {
GameCanvas.tabInven = new TabInventory(T.AF, Player.vecInventory, (byte)0, MainTab.xTab);
GameCanvas.tabInven.initCmd();
}

selectedInvenIndex = -1;
selectedEquipIndex = -1;
selectedItemInfo = null;
selectedSkillIndex = 0;
selectedQuestIndex = 0;
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

for (int i = 0; i < 5; i++) {
isTabInitialized[i] = false;
isTabDirty[i] = false;
}

if (curMainTab < 0 || curMainTab >= 5) {
curMainTab = 0;
}

initTab(curMainTab);
updateSoftKeys();
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
int statY = paneY + 144;
int statH = paneH - 148;
if (statH < 36) statH = 36;
int boxX = paneX + 4;
int boxW = leftPaneW - 8;
int viewH = statH - 4;

int lineH = 14;
int ticketRows = 3;
int ticketH = ticketRows * lineH;
int dividerH = 4;
int statCount = (Player.RQ != null && Player.RQ.length > 0) ? Player.RQ.length : 6;
int statRows = (statCount + 1) / 2;
int statH_content = statRows * lineH;
int totalStatContentH = 3 + ticketH + dividerH + statH_content + 3;
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
case 0: // Sức mạnh: Tăng tấn công, Xuyên giáp, Chí mạng
return new MainInfoItem[] {
new MainInfoItem((byte)1, totalVal * 5),
new MainInfoItem((byte)13, totalVal),
new MainInfoItem((byte)10, totalVal * 2)
};
case 4: // Nhanh nhẹn: Né tránh, Giảm hồi chiêu
return new MainInfoItem[] {
new MainInfoItem((byte)12, totalVal),
new MainInfoItem((byte)25, totalVal)
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
int itemH = 42;
int totalH = featureCount * itemH + 6;
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
default: return "Chức Năng " + (index + 1);
}
}

public String getFeatureCategory(int index) {
switch (index) {
case 0: return "[Nhân vật]";
case 1: return "[Nhân vật]";
case 2: return "[Bang hội]";
case 3: return "[Xã giao]";
case 4: return "[Đội ngũ]";
case 5: return "[Chiến đấu]";
case 6: return "[Sư đồ]";
case 7: return "[Giao dịch]";
case 8: return "[Giao tiếp]";
case 9: return "[Trang bị]";
case 10: return "[Auto game]";
case 11: return "[MOD VIP]";
default: return "[Tính năng]";
}
}

public String getFeatureSubText(int index) {
try {
switch (index) {
case 0: {
String curTitleStr = "Chưa dùng";
int dhCount = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
if (Player.vecDanhHieu != null) {
for (int i = 0; i < Player.vecDanhHieu.size(); i++) {
Object obj = Player.vecDanhHieu.elementAt(i);
if (obj instanceof DanhHieuInfo) {
DanhHieuInfo dh = (DanhHieuInfo)obj;
if (dh != null && dh.state == 2) {
curTitleStr = dh.name;
break;
}
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
return "Chat Loa thế giới & hòm thư";
}
case 9: {
return "Chuyển nhanh bộ trang bị đã lưu";
}
case 10: {
return "Tự đánh chiêu, nhặt đồ, bơm HP/MP";
}
case 11: {
String ts = "Tàn sát [" + AThMadaraMOD.getSlaughterStateName() + "] - Tự đánh [" + AThMadaraMOD.getTuDanhStateName() + "]";
return ts + " - Tốc độ: " + AThMadaraMOD.gameSpeed + " - FPS: " + MotherCanvas.targetFPS;
}
default:
return "Mở giao diện chức năng";
}
} catch (Exception e) {
return "Mở giao diện chức năng";
}
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
selectedItemInfo.updateHInfo();
int maxPopupH = Math.max(60, Math.min(h - 54, MotherCanvas.h - 54));
if (selectedItemInfo.BT > maxPopupH) {
selectedItemInfo.CO = selectedItemInfo.BT - maxPopupH;
} else {
selectedItemInfo.CO = 0;
}
MainTab.BQ = 0;
MainTab.BP = -1;
}
updateSoftKeys();
}

public void selectPet(MainItem pet, int touchX, int touchY) {
selectedPetInfo = pet;
itemTouchX = touchX;
itemTouchY = touchY;
if (selectedPetInfo != null) {
selectedPetInfo.updateHInfo();
int maxPopupH = Math.max(60, Math.min(h - 54, MotherCanvas.h - 54));
if (selectedPetInfo.BT > maxPopupH) {
selectedPetInfo.CO = selectedPetInfo.BT - maxPopupH;
} else {
selectedPetInfo.CO = 0;
}
MainTab.BQ = 0;
MainTab.BP = -1;
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
TabSkill.reloadCL();
validSkills.removeAllElements();

if (TabSkill.CL != null && TabSkill.CL.size() > 0) {
for (int i = 0; i < TabSkill.CL.size(); i++) {
Skill_Info sk = (Skill_Info)TabSkill.CL.elementAt(i);
if (sk != null && sk.Lv_RQ != -1) {
validSkills.addElement(sk);
}
}

public static int getItemCategory(MainItem item) {
if (item == null) return -1;

// 1: Trang Bị (Trang bị vũ khí, giáp, phụ kiện, thời trang, cánh, thần thú...)
if (item.typeObject == 3 || item.typeObject == 102 || item.typeObject == 103 || item.typeObject == 107) {
return 1;
}

String name = (item.name != null) ? item.name.toLowerCase() : "";

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

// ID ranges của các loại đá khảm / hải thạch / nâng cấp
if ((item.ID >= 44 && item.ID <= 79) || (item.ID >= 221 && item.ID <= 226) || 
(item.ID >= 321 && item.ID <= 327) || (item.ID >= 514 && item.ID <= 516) || 
(item.ID >= 644 && item.ID <= 646) || item.ID == 134 || item.ID == 135 || 
item.ID == 572 || item.ID == 683) {
return 3;
}

// Nhận diện theo tên tiếng Việt mở rộng cho Đá Khảm / Ngọc / Thạch / Nguyên liệu
if (name.indexOf("đá ") >= 0 || name.indexOf("da ") >= 0 || name.startsWith("đá") || name.startsWith("da") ||
name.indexOf("khảm") >= 0 || name.indexOf("kham") >= 0 ||
name.indexOf("ngọc") >= 0 || name.indexOf("ngoc") >= 0 ||
name.indexOf("cẩm thạch") >= 0 || name.indexOf("hải thạch") >= 0 || name.indexOf("thạch anh") >= 0 || 
name.indexOf("huyết thạch") >= 0 || name.indexOf("hắc thạch") >= 0 || name.indexOf("thạch") >= 0 ||
name.indexOf("tinh thể") >= 0 || name.indexOf("tinh the") >= 0 ||
name.indexOf("saphia") >= 0 || name.indexOf("sapphire") >= 0 || name.indexOf("topaz") >= 0 || name.indexOf("ruby") >= 0 ||
name.indexOf("kim cương") >= 0 || name.indexOf("diamond") >= 0 ||
name.indexOf("khoáng") >= 0 || name.indexOf("khoang") >= 0 || name.indexOf("quặng") >= 0 || 
}
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
if (validSkills != null && selectedSkillIndex >= 0 && selectedSkillIndex < validSkills.size()) {
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
int startSlotY = y + 28 + 28 + 16 + 1;
int slotGapY = 24;
int row = (selectedEquipIndex % 8) / 2;
boolean isLeft = (selectedEquipIndex % 2 == 0);
int px = isLeft ? (leftSlotX + 13) : (rightSlotX + 13);
int py = startSlotY + row * slotGapY + 13;
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
int startSlotY = y + 28 + 28 + 16 + 1;
int slotGapY = 24;
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
GameCanvas.AH();
GameCanvas.clearKeyPressed();
GameCanvas.menuCur.startAt(mActions, 2, cur.name);
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
GameCanvas.AH();
GameCanvas.clearKeyPressed();
GameCanvas.menuCur.startAt(mVector2, 2, T.BC);
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
if (super.mainScreen != null) {
super.mainScreen.Show(super.mainScreen.mainScreen);
} else {
GameCanvas.gameScr.Show();
}
}
}

if (MainTab.BI != null) MainTab.BI.updateTimeCountDownTicket();
if (MainTab.BJ != null) MainTab.BJ.updateTimeCountDownTicket();

if (curMainTab == 0) {
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

if (GameCanvas.tabInven != null && selectedItemInfo != null) {
GameCanvas.tabInven.itemCur = selectedItemInfo;
GameCanvas.tabInven.IdSelect = Player.vecInventory.indexOf(selectedItemInfo);
}
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
}
else if (chucNangSubView == 2 && petList != null) petList.AC();
}
switchTab(curMainTab + 1);
return;
}

// Key 5 / OK / FIRE / Enter (5, 12, 40)
if (GameCanvas.keyMyHold[5] || GameCanvas.isKeyPressed(5) || GameCanvas.keyMyHold[12] || GameCanvas.isKeyPressed(12) || GameCanvas.keyMyHold[40] || GameCanvas.isKeyPressed(40)) {
GameCanvas.AH();
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
focusPane = 1; // Nhảy thẳng xuống Hành Trang (Inventory Grid)
if (selectedInvenIndex < 0 && filteredItems.size() > 0) {
selectedInvenIndex = 0;
selectedItemInfo = (MainItem)filteredItems.elementAt(0);
selectItem(selectedItemInfo, 0, 0);
}
} else if (curMainTab == 1) {
focusPane = 0; // Nhảy xuống Cột Chỉ số
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
}
else if (chucNangSubView == 2 && petList != null) petList.AC();
}
}

public final void handleKeyPress() {
GameCanvas.ClearkeyMove(3);
focusPane = 1; // Xuống Hành Trang
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
// Chuyển nhanh trang bằng phím * (10) và # (11)
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
selectedEquipIndex = row * 2 + 1;
MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
selectItem(eq, 0, 0);
updateSoftKeys();
return;
}

if (GameCanvas.isKeyPressed(1) && selectedInvenIndex >= 0 && selectedInvenIndex < cols) {
GameCanvas.ClearkeyMove(1);
focusPane = 3; // Lên Filter Bar
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
focusPane = 0;
int row = selectedInvenIndex / cols;
if (row > 3) row = 3;
selectedEquipIndex = row * 2 + 1;
if (selectedEquipIndex > 7) selectedEquipIndex = 7;
MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
selectItem(eq, 0, 0);
updateSoftKeys();
return;
} else {
selectedInvenIndex--;
hasKeypadMove = true;
}
} else if (GameCanvas.isKeyPressed(2)) {
GameCanvas.ClearkeyMove(2);
if (selectedInvenIndex < totalSlots - 1) {
selectedInvenIndex++;
hasKeypadMove = true;
}
} else if (GameCanvas.isKeyPressed(1)) {
GameCanvas.ClearkeyMove(1);
if (selectedInvenIndex - cols >= 0) {
selectedInvenIndex -= cols;
hasKeypadMove = true;
} else {
focusPane = 3; // Lên Filter Bar
updateSoftKeys();
return;
}
} else if (GameCanvas.isKeyPressed(3)) {
GameCanvas.ClearkeyMove(3);
if (selectedInvenIndex + cols < totalSlots) {
selectedInvenIndex += cols;
hasKeypadMove = true;
}
}
selectItem(eq, 0, 0);
} else {
focusPane = 1; // Sang Hành Trang
int row = selectedEquipIndex / 2;
selectedInvenIndex = row * cols;
if (selectedInvenIndex >= filteredItems.size()) selectedInvenIndex = filteredItems.size() - 1;
if (selectedInvenIndex >= 0 && selectedInvenIndex < filteredItems.size()) {
MainItem it = (MainItem)filteredItems.elementAt(selectedInvenIndex);
selectItem(it, 0, 0);
}
updateSoftKeys();
return;
}
}

if (GameCanvas.isKeyPressed(0) && (selectedEquipIndex % 2 == 1)) {
GameCanvas.ClearkeyMove(0);
selectedEquipIndex -= 1;
MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
selectItem(eq, 0, 0);
return;
}

if (GameCanvas.isKeyPressed(1)) {
GameCanvas.ClearkeyMove(1);
if (selectedEquipIndex >= 10) {
selectedEquipIndex -= 2;
MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
selectItem(eq, 0, 0);
} else if (selectedEquipIndex >= 8) {
// Từ Thần Trang nhảy lên Trang Bị Thường
selectedEquipIndex -= 2;
equipToY = 0;
MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
selectItem(eq, 0, 0);
} else if (selectedEquipIndex >= 2) {
selectedEquipIndex -= 2;
MainItem eq = (MainItem)GameScreen.player.hashEquip.get("" + selectedEquipIndex);
selectItem(eq, 0, 0);
} else {
if (infoList.AB < 0) infoList.AB = 0;
}
if (GameCanvas.isKeyPressed(3) && infoList != null) {
GameCanvas.ClearkeyMove(3);
infoList.AB += 18;
if (infoList.AB > infoList.AD) infoList.AB = infoList.AD;
}

// Key 6 / Right moves to Tiềm Năng
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
boolean hasKeyMove = false;
if (GameCanvas.isKeyPressed(1) && selectedAttIndex == 0) {
GameCanvas.ClearkeyMove(1);
focusPane = 2; // Jump up to Top Nav Tabs
return;
}
if (danhHieuList != null) {
int itemH = 34;
int itemY = selectedDanhHieuIndex * itemH;
if (itemY < danhHieuList.AC) danhHieuList.AB = itemY;
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
int listH = leftPaneH - 22;

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
}

private void handleKeypadTab4() {
if (chucNangSubView == 0) {
int count = getFeatureItemCount();
if (focusPane == 1 && count > 0) {
if (GameCanvas.isKeyPressed(1) && selectedChucNangIndex == 0) {
GameCanvas.ClearkeyMove(1);
focusPane = 2;
updateSoftKeys();
return;
}
if (GameCanvas.isKeyPressed(1)) {
selectedChucNangIndex--;
GameCanvas.ClearkeyMove(1);
} else if (GameCanvas.isKeyPressed(3)) {
selectedChucNangIndex++;
GameCanvas.ClearkeyMove(3);
} else if (GameCanvas.isKeyPressed(0)) {
GameCanvas.ClearkeyMove(0);
switchTab(curMainTab - 1);
return;
} else if (GameCanvas.isKeyPressed(2)) {
GameCanvas.ClearkeyMove(2);
switchTab(curMainTab + 1);
return;
}
if (selectedChucNangIndex < 0) selectedChucNangIndex = 0;
if (selectedChucNangIndex >= count) selectedChucNangIndex = count - 1;

if (featureMenuList != null) {
int itemH = 38;
int itemY = selectedChucNangIndex * itemH;
if (itemY < featureMenuList.AC) featureMenuList.AB = itemY;
if (itemY + itemH > featureMenuList.AC + featureMenuList.maxH) featureMenuList.AB = itemY + itemH - featureMenuList.maxH;
}
updateSoftKeys();
}
} else if (chucNangSubView == 1) {
int totalDh = (Player.vecDanhHieu != null) ? Player.vecDanhHieu.size() : 0;
if (totalDh > 0 && focusPane == 1) {
if (GameCanvas.isKeyPressed(1) && selectedDanhHieuIndex == 0) {
GameCanvas.ClearkeyMove(1);
focusPane = 2;
return;
}
if (GameCanvas.isKeyPressed(1)) {
selectedDanhHieuIndex--;
GameCanvas.ClearkeyMove(1);
} else if (GameCanvas.isKeyPressed(3)) {
selectedDanhHieuIndex++;
GameCanvas.ClearkeyMove(3);
}
if (selectedDanhHieuIndex < 0) selectedDanhHieuIndex = 0;
if (selectedDanhHieuIndex >= totalDh) selectedDanhHieuIndex = totalDh - 1;
selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(selectedDanhHieuIndex);

if (danhHieuList != null) {
int itemH = 34;
int itemY = selectedDanhHieuIndex * itemH;
if (itemY < danhHieuList.AC) danhHieuList.AB = itemY;
if (itemY + itemH > danhHieuList.AC + danhHieuList.maxH) danhHieuList.AB = itemY + itemH - danhHieuList.maxH;
}
updateSoftKeys();
}
} else if (chucNangSubView == 2) {
int total = (Player.vecPet != null) ? Player.vecPet.size() : 0;
if (total > 0 && focusPane == 1) {
int cols = (w - 36) / 32;
if (cols < 1) cols = 1;
if (GameCanvas.isKeyPressed(1) && selectedPetIndex < cols) {
GameCanvas.ClearkeyMove(1);
focusPane = 2;
return;
}
if (GameCanvas.isKeyPressed(0)) { selectedPetIndex--; GameCanvas.ClearkeyMove(0); }
if (GameCanvas.isKeyPressed(2)) { selectedPetIndex++; GameCanvas.ClearkeyMove(2); }
if (GameCanvas.isKeyPressed(1)) { selectedPetIndex -= cols; GameCanvas.ClearkeyMove(1); }
if (GameCanvas.isKeyPressed(3)) { selectedPetIndex += cols; GameCanvas.ClearkeyMove(3); }
if (selectedPetIndex < 0) selectedPetIndex = 0;
if (selectedPetIndex >= total) selectedPetIndex = total - 1;
GameCanvas.ClearkeyMove(1);
focusPane = 2;
return;
}
if (GameCanvas.isKeyPressed(0)) { selectedPetIndex--; GameCanvas.ClearkeyMove(0); }
if (GameCanvas.isKeyPressed(2)) { selectedPetIndex++; GameCanvas.ClearkeyMove(2); }
if (GameCanvas.isKeyPressed(1)) { selectedPetIndex -= cols; GameCanvas.ClearkeyMove(1); }
if (GameCanvas.isKeyPressed(3)) { selectedPetIndex += cols; GameCanvas.ClearkeyMove(3); }
if (selectedPetIndex < 0) selectedPetIndex = 0;
} else if (GameCanvas.isKeyPressed(3)) {
selectedDanhHieuIndex++;
GameCanvas.ClearkeyMove(3);
}
if (selectedDanhHieuIndex < 0) selectedDanhHieuIndex = 0;
if (selectedDanhHieuIndex >= totalDh) selectedDanhHieuIndex = totalDh - 1;
selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(selectedDanhHieuIndex);

if (danhHieuList != null) {
int itemH = 34;
int itemY = selectedDanhHieuIndex * itemH;
if (itemY < danhHieuList.AC) danhHieuList.AB = itemY;
if (itemY + itemH > danhHieuList.AC + danhHieuList.maxH) danhHieuList.AB = itemY + itemH - danhHieuList.maxH;
}
updateSoftKeys();
}
} else if (chucNangSubView == 2) {
int total = (Player.vecPet != null) ? Player.vecPet.size() : 0;
if (total > 0 && focusPane == 1) {
int cols = (w - 36) / 32;
if (cols < 1) cols = 1;
if (GameCanvas.isKeyPressed(1) && selectedPetIndex < cols) {
GameCanvas.ClearkeyMove(1);
focusPane = 2;
return;
}
if (GameCanvas.isKeyPressed(0)) { selectedPetIndex--; GameCanvas.ClearkeyMove(0); }
if (GameCanvas.isKeyPressed(2)) { selectedPetIndex++; GameCanvas.ClearkeyMove(2); }
if (GameCanvas.isKeyPressed(1)) { selectedPetIndex -= cols; GameCanvas.ClearkeyMove(1); }
if (GameCanvas.isKeyPressed(3)) { selectedPetIndex += cols; GameCanvas.ClearkeyMove(3); }
if (selectedPetIndex < 0) selectedPetIndex = 0;
if (selectedPetIndex >= total) selectedPetIndex = total - 1;
MainItem pet = (MainItem)Player.vecPet.elementAt(selectedPetIndex);
selectPet(pet, 0, 0);
}
}
}

public void paint(mGraphics g) {
if (super.mainScreen != null) {
super.mainScreen.paint(g);
}
GameCanvas.resetTrans(g);
g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);
paintEquipSlot(g, rightSlotX, startSlotY1 + i * slotGapY, eqSlotSize, 8 + i * 2 + 1);
}
int charCenterY1 = page1Y + 80;
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

g.translate(0, equipScrollY);
g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

int statY = paneY + 144;
int statH = paneH - 148;
if (statH < 36) statH = 36;
int boxX = paneX + 4;
int boxW = leftPaneW - 8;
AvMain.paintRect(g, boxX, statY, boxW, statH, (byte)0, 3);

int viewH = statH - 4;
int viewW = boxW - 4;

int lineH = 14;
int ticketRows = 3;
int ticketH = ticketRows * lineH;
int dividerH = 4;
int statCount = (Player.RQ != null && Player.RQ.length > 0) ? Player.RQ.length : 6;
int statRows = (statCount + 1) / 2;
int statH_content = statRows * lineH;
int totalH = 3 + ticketH + dividerH + statH_content + 3;
int limStatY = totalH - viewH;
if (limStatY < 0) limStatY = 0;

if (leftStatList == null) {
leftStatList = new ListNew(boxX, statY + 2, boxW, viewH, 0, 0, limStatY, true);
} else {
leftStatList.AD = limStatY;
if (leftStatList.AB > limStatY) leftStatList.AB = limStatY;
if (leftStatList.AB < 0) leftStatList.AB = 0;
else if (curMainTab == 4) paintTabChucNang(g);
} catch (Exception e) {}

GameCanvas.resetTrans(g);
g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

if (!GameCanvas.isTouch) {
super.paint(g);
}
}

private void paintTopNavTabs(mGraphics g) {
int tabCount = TAB_NAMES.length;
int tabW = (w - 36) / tabCount;
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
short iconId = isSel ? TAB_ICON_SEL_IDS[i] : TAB_ICON_IDS[i];
MainImage iconImg = ObjectData.getImageAll(iconId, ObjectData.hashImageItemOther, (short)9000);
if (iconImg == null || iconImg.img == null) {
iconImg = ObjectData.getImageAll(TAB_ICON_IDS[i], ObjectData.hashImageItemOther, (short)9000);
}

String tabName = TAB_NAMES[i];
int nameW = isSel ? mFont.tahoma_7b_yellow.getWidth(tabName) : mFont.tahoma_7_black.getWidth(tabName);
boolean hasIcon = (iconImg != null && iconImg.img != null);
int iconW = hasIcon ? 14 : 0;
int gap = hasIcon ? 3 : 0;
int totalContentW = iconW + gap + nameW;

if (totalContentW <= tabW - 4) {
int contentStartX = curX + (tabW - 2 - totalContentW) / 2;
if (hasIcon) {
g.drawRegion((mImage)iconImg.img, contentStartX + iconW / 2, startY + tabH / 2, 3);
}
int textX = contentStartX + (hasIcon ? (iconW + gap) : 0);
if (isSel) {
mFont.tahoma_7b_yellow.drawString(g, tabName, textX, startY + 3, 0);
} else {
mFont.tahoma_7_black.drawString(g, tabName, textX, startY + 3, 0);
}
} else {
if (isSel) {
mFont.tahoma_7b_yellow.drawString(g, tabName, curX + (tabW - 2) / 2, startY + 3, 2);
} else {
mFont.tahoma_7_black.drawString(g, tabName, curX + (tabW - 2) / 2, startY + 3, 2);
}
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

mFont.tahoma_7b_black.drawString(g, GameScreen.player.name, paneX + leftPaneW / 2, paneY + 3, 2);
String lvStr = "Lv." + GameScreen.player.Lv + (GameScreen.player.LvThongThao > 0 ? " (TT " + GameScreen.player.LvThongThao + ")" : "");
scrollLeftStat.setInfo(boxX + boxW - 3, statY + 2, viewH, 0xAA8800);
scrollLeftStat.setYScrool(leftStatList.AC, leftStatList.AD);
scrollLeftStat.paint(g);
}

int rightPaneX = paneX + leftPaneW + 6;
int rightPaneY = y + 28;
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int rightPaneH = h - 34;

AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (byte)0, 1);

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

if (isSelFilter) {
mFont.tahoma_7b_yellow.drawString(g, FILTER_NAMES[f], fx + (fw - 2) / 2, fy + 2, 2);
} else {
mFont.tahoma_7_black.drawString(g, FILTER_NAMES[f], fx + (fw - 2) / 2, fy + 2, 2);
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


int rightPaneX = paneX + leftPaneW + 6;
int rightPaneY = y + 28;
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int rightPaneH = h - 34;

AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (byte)0, 1);

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

if (isSelFilter) {
mFont.tahoma_7b_yellow.drawString(g, FILTER_NAMES[f], fx + (fw - 2) / 2, fy + 2, 2);
} else {
mFont.tahoma_7_black.drawString(g, FILTER_NAMES[f], fx + (fw - 2) / 2, fy + 2, 2);
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

// Tối ưu Viewport Culling cho 126 ô của tab hiện tại
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
item.AC(g, slotX + invSlotSize / 2, slotY + invSlotSize / 2, invSlotSize);
}
item.paint(g, slotX + invSlotSize / 2, slotY + invSlotSize / 2, invSlotSize);

if (item.numPotion > 1) {
mFont.tahoma_7_yellow.drawString(g, String.valueOf(item.numPotion), slotX + invSlotSize - 2, slotY + invSlotSize - 10, 1);
}
if (item.typeObject == 4) {
DelaySkill.getDelay(item.indexHotKey).AA(g, slotX + 1, slotY + 1, invSlotSize - 1);
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

int curMoneyX = rightPaneX + 4;
if (AvMain.fraMoney != null && GameScreen.player != null) {
// Beli (Frame 0)
String strBeri = formatMoneyShort(Player.SN);
AvMain.fraMoney.drawFrame(0, curMoneyX + 6, footY + 4, 0, 3, g);
mFont.tahoma_7b_yellow.drawString(g, strBeri, curMoneyX + 13, footY, 0);
curMoneyX += 13 + mFont.tahoma_7b_yellow.getWidth(strBeri) + 6;

// Ruby (Frame 1)
String strRuby = formatMoneyShort(GameScreen.player.Ruby);
AvMain.fraMoney.drawFrame(1, curMoneyX + 6, footY + 4, 0, 3, g);
mFont.tahoma_7b_red.drawString(g, strRuby, curMoneyX + 13, footY, 0);
curMoneyX += 13 + mFont.tahoma_7b_red.getWidth(strRuby) + 6;

// Extol / Coin / VNĐ (Frame 7)
String strVnd = formatMoneyShort(GameScreen.player.PD);
AvMain.fraMoney.drawFrame(7, curMoneyX + 6, footY + 4, 0, 3, g);
mFont.tahoma_7b_green.drawString(g, strVnd, curMoneyX + 13, footY, 0);
curMoneyX += 13 + mFont.tahoma_7b_green.getWidth(strVnd) + 6;
}

int totalInven = (Player.vecInventory != null) ? Player.vecInventory.size() : 0;
String capStr = totalInven + "/" + Player.maxInventory;
int capW = mFont.tahoma_7_black.getWidth(capStr);
mFont.tahoma_7_black.drawString(g, capStr, rightPaneX + rightPaneW - 4, footY, 1);

// List 10 số thứ tự tab hành trang (gọn gàng 1..10)
int tabAreaLeft = curMoneyX + 4;
int tabAreaRight = rightPaneX + rightPaneW - capW - 6;
int tabAreaW = tabAreaRight - tabAreaLeft;
int tabBoxY = footY - 1;
int tabBoxH = 14;
int numTabs = 10;

if (tabAreaW >= 150) {
int btnGap = 2;
int tabBtnW = Math.min(16, (tabAreaW - (numTabs - 1) * btnGap) / numTabs);
if (tabBtnW < 12) tabBtnW = 12;
int totalTabsW = numTabs * tabBtnW + (numTabs - 1) * btnGap;
int startTabsX = tabAreaLeft + (tabAreaW - totalTabsW) / 2;

for (int t = 0; t < numTabs; t++) {
int bx = startTabsX + t * (tabBtnW + btnGap);
boolean isCurPage = (t == curInvenPage);

AvMain.paintRect(g, bx, tabBoxY, tabBtnW, tabBoxH, (byte)(isCurPage ? 1 : 0), (isCurPage ? 0 : 1));
if (isCurPage && AvMain.imgNenfocus != null) {
g.drawRegion(AvMain.imgNenfocus, 2, 2, tabBtnW, tabBoxH, 0, bx, tabBoxY, 0);
}

if (isCurPage) {
mFont.tahoma_7b_yellow.drawString(g, String.valueOf(t + 1), bx + tabBtnW / 2, tabBoxY + 3, 2);
} else {
mFont.tahoma_7_black.drawString(g, String.valueOf(t + 1), bx + tabBtnW / 2, tabBoxY + 3, 2);
}
}
} else if (tabAreaW >= 42) {
int sBoxW = Math.min(70, tabAreaW);
int sBoxX = tabAreaLeft + (tabAreaW - sBoxW) / 2;

AvMain.paintRect(g, sBoxX, tabBoxY, sBoxW, tabBoxH, (byte)0, 1);
mFont.tahoma_7b_yellow.drawString(g, "Tab " + (curInvenPage + 1), sBoxX + sBoxW / 2, tabBoxY + 3, 2);

if (curInvenPage > 0) {
mFont.tahoma_7b_white.drawString(g, "<", sBoxX + 4, tabBoxY + 3, 0);
}
if (curInvenPage < numTabs - 1) {
mFont.tahoma_7b_white.drawString(g, ">", sBoxX + sBoxW - 4, tabBoxY + 3, 1);
}
}

if (selectedItemInfo != null) {
int infoW = (selectedItemInfo.BS > 0) ? selectedItemInfo.BS : 140;
int maxPopupH = Math.max(60, Math.min(h - 54, MotherCanvas.h - 54));
int showH = selectedItemInfo.BT - selectedItemInfo.CO;
if (showH <= 0 || showH > maxPopupH) showH = (selectedItemInfo.BT > 0) ? Math.min(selectedItemInfo.BT, maxPopupH) : 60;
mFont.tahoma_7b_black.drawString(g, "Menu", infoX + 82, btnY + 3, 2);
}
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
} else if (id >= 0 && id < MainItem.DEFAULT_ATTR_PERCENT.length) {
isPercent = MainItem.DEFAULT_ATTR_PERCENT[id];
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
}
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
if (curSk.rangeLan > 0) {
mFont.tahoma_7_white.drawString(g, T.DG + ": " + curSk.rangeLan, rightPaneX + 14, curDetailY, 0);
curDetailY += 14;
}

// 4. Thuộc tính & Hiệu ứng đặc biệt (từ vecAtt)
if (curSk.vecAtt != null && curSk.vecAtt.size() > 0) {
int numSpec = 0, probSpec = 0, typeSpec = 0;
int bpoPercent = 0, bpoVal = 0;
for (int va = 0; va < curSk.vecAtt.size(); va++) {
MainInfoItem mi = (MainInfoItem)curSk.vecAtt.elementAt(va);
if (mi == null) continue;
if (mi.AA >= 28 && mi.AA < 32) {
if (mi.AA == 28) typeSpec = mi.AE;
else if (mi.AA == 29) probSpec = mi.AE;
else if (mi.AA == 30) numSpec = mi.AE;
} else if (mi.AA >= 64 && mi.AA <= 65) {
if (mi.AA == 64) bpoVal = mi.AE;
else if (mi.AA == 65) bpoPercent = mi.AE;
} else {
String attStr = MainItem.AA(mi);
if (attStr != null && attStr.length() > 0 && !attStr.equals("null")) {
if (curDetailY + 14 <= rightPaneY + rightPaneH - 28) {
mFont.tahoma_7_green.drawString(g, attStr, rightPaneX + 14, curDetailY, 0);
curDetailY += 14;
}
}
}
}
if (numSpec > 0) {
String specName = (typeSpec >= 0 && typeSpec < T.mEffSpec.length) ? T.mEffSpec[typeSpec] : "Hiệu ứng";
String specStr = MainItem.AA(probSpec, (byte)1) + " " + T.EQ + " " + specName + " " + T.ER + " " + MainItem.AA(numSpec, (byte)10);
if (curDetailY + 14 <= rightPaneY + rightPaneH - 28) {
mFont.tahoma_7_green.drawString(g, specStr, rightPaneX + 14, curDetailY, 0);
curDetailY += 14;
}
}
if (bpoVal > 0) {
String bpoStr = MainItem.AA(bpoPercent, (byte)1) + " " + T.UQ + " " + MainItem.AA(bpoVal, (byte)1);
if (curDetailY + 14 <= rightPaneY + rightPaneH - 28) {
mFont.tahoma_7_green.drawString(g, bpoStr, rightPaneX + 14, curDetailY, 0);
curDetailY += 14;
}
}
}

// 5. Mô tả kỹ năng
if (curSk.info != null && curSk.info.length() > 0) {
String[] lines = mFont.tahoma_7_white.splitFontArray(curSk.info, rightPaneW - 28);
if (lines != null) {
for (int li = 0; li < lines.length; li++) {
if (curDetailY + 14 > rightPaneY + rightPaneH - 28) break;
mFont.tahoma_7_white.drawString(g, lines[li], rightPaneX + 14, curDetailY, 0);
curDetailY += 14;
}
}
}

// 6. Nút Gán Phím
if (curSk.Lv_RQ > 0 && (curSk.typeSkill == 1 || curSk.typeSkill == 2 || curSk.typeSkill == 4)) {
int btnY = rightPaneY + rightPaneH - 24;
AvMain.paintRect(g, rightPaneX + 14, btnY, 80, 18, (byte)1, 1);
mFont.tahoma_7b_black.drawString(g, "Gán Phím", rightPaneX + 54, btnY + 3, 2);
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

mFont.tahoma_7b_white.drawString(g, q.AH + q.AA(), listX + 28, qy + 2, 0);
if (q.AL != null && q.AL.length() > 0) {
mFont.tahoma_7_white.drawString(g, q.AL, listX + 28, qy + 16, 0);
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
mFont.tahoma_7b_yellow.drawString(g, curQ.AH + curQ.AA(), rightPaneX + 14, rightPaneY + 8, 0);
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
public void updatePointer() {
super.updatePointer();

int tabCount = 5;
int tabW = (w - 36) / tabCount;
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
if (invenList != null) invenList.update_Pos_UP_DOWN();
if (leftStatList != null) leftStatList.update_Pos_UP_DOWN();
}
if (infoList != null && curMainTab == 1) {
infoList.update_Pos_UP_DOWN();
}
if (skillList != null && curMainTab == 2) {
skillList.update_Pos_UP_DOWN();
}
else if (curMainTab == 2) handlePointerTab2();
else if (curMainTab == 3) handlePointerTab3();
else if (curMainTab == 4) handlePointerTab4();

if (GameCanvas.isPoint(x + w - 30, y + 4, 30, 30)) {
GameCanvas.isPointerSelect = false;
close();
}
}

private void handlePointerTab0() {
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int rightPaneX = x + 10 + leftPaneW + 6;
int rightPaneY = y + 28;
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int rightPaneH = h - 34;

int fw = (rightPaneW - 8) / 5;
int fy = rightPaneY + 4;
for (int f = 0; f < 5; f++) {
int fx = rightPaneX + 4 + f * fw;
if (GameCanvas.isPoint(fx, fy, fw - 2, 16)) {
GameCanvas.isPointerSelect = false;
selectItem(null, 0, 0);
selectedInvenIndex = -1;
curInvenFilter = f;
focusPane = 3;
updateFilteredItems();
updateSoftKeys();
return;
}
}

if (selectedItemInfo != null) {
int infoW = (selectedItemInfo.BS > 0) ? selectedItemInfo.BS : 140;
int maxPopupH = Math.max(60, Math.min(h - 54, MotherCanvas.h - 54));
int showH = selectedItemInfo.BT - selectedItemInfo.CO;
if (showH <= 0 || showH > maxPopupH) showH = (selectedItemInfo.BT > 0) ? Math.min(selectedItemInfo.BT, maxPopupH) : 60;
if (showH < 30) showH = 30;
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int rightPaneH = h - 34;

int fw = (rightPaneW - 8) / 5;
int fy = rightPaneY + 4;
int rightPaneY = y + 28;
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int rightPaneH = h - 34;

AvMain.paintRect(g, rightPaneX, rightPaneY, rightPaneW, rightPaneH, (byte)0, 1);

if (Player.QI != null && selectedQuestIndex >= 0 && selectedQuestIndex < Player.QI.size()) {
MainQuest curQ = (MainQuest)Player.QI.elementAt(selectedQuestIndex);
if (curQ != null) {
mFont.tahoma_7b_yellow.drawString(g, curQ.AH + curQ.AA(), rightPaneX + 14, rightPaneY + 8, 0);
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

int itemH = 42;
int icBoxSize = 34;

for (int i = 0; i < featureCount; i++) {
try {
int itemY = listY + i * itemH;
boolean isSel = (focusPane == 1 && i == selectedChucNangIndex);

// Nền hàng item: index 0 khi chọn (sáng hơn), index 3 khi không chọn (tối, tương phản rõ trên panel nền 1)
AvMain.paintRect(g, listX + 2, itemY + 2, listW - 4, itemH - 4, (byte)(isSel ? 1 : 0), (byte)(isSel ? 0 : 3));
if (isSel) {
g.setColor(0xFFFF00);
g.drawRect(listX + 1, itemY + 1, listW - 3, itemH - 3);
if (AvMain.imgNenfocus != null) {
g.drawRegion(AvMain.imgNenfocus, 2, 2, Math.min(listW - 4, 32), Math.min(itemH - 4, 32), 0, listX + 2, itemY + 2, 0);
}
}

// Ô chứa Icon PNG ở đầu hàng (34x34)
int icBoxX = listX + 5;
int icBoxY = itemY + (itemH - icBoxSize) / 2;
AvMain.paintRect(g, icBoxX, icBoxY, icBoxSize, icBoxSize, (byte)0, (byte)(isSel ? 3 : 1));

int icCenterX = icBoxX + icBoxSize / 2;
int icCenterY = icBoxY + icBoxSize / 2;

paintFeatureIcon(g, i, icCenterX, icCenterY);

// Tên chức năng và thông tin trạng thái
String category = getFeatureCategory(i);
String title = getFeatureTitle(i);
String subText = getFeatureSubText(i);

int textX = listX + icBoxSize + 10;
mFont.tahoma_7_yellow.drawString(g, category + " ", textX, itemY + 5, 0);
int badgeW = mFont.tahoma_7_yellow.getWidth(category + " ");
(isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white).drawString(g, title, textX + badgeW, itemY + 5, 0);
(isSel ? mFont.tahoma_7_yellow : mFont.tahoma_7_white).drawString(g, subText, textX, itemY + 22, 0);

// Mũi tên điều hướng ở cuối
(isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7_white).drawString(g, ">", listX + listW - 10, itemY + 14, 2);
} catch (Exception e) {
}
}

if (selectedDanhHieuInfo != null) {
mFont.tahoma_7b_yellow.drawString(g, "★ " + selectedDanhHieuInfo.name + " ★", detailX + detailW / 2, detailY + 5, 2);

// KHUNG SHOWCASE HIỆU ỨNG ĐỘNG
int stageX = detailX + 8;
int stageY = detailY + 18;
int stageW = detailW - 16;
int stageH = 46;
AvMain.paintRect(g, stageX, stageY, stageW, stageH, (byte)1, 1);
g.setColor(0xFFA500);
g.drawRect(stageX, stageY, stageW, stageH);

int effCenterX = stageX + stageW / 2;
int effCenterY = stageY + 30;

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
String[] optLines = mFont.tahoma_7_white.splitFontArray(selectedDanhHieuInfo.optionsStr, detailW - 16);
if (optLines != null) {
for (int l = 0; l < optLines.length && curTextY + 11 <= maxTextY; l++) {
String line = optLines[l];
if (line == null) continue;
if (line.startsWith("Hạn") || line.startsWith("Còn") || line.indexOf("ngày") >= 0 || line.indexOf("giờ") >= 0 || line.indexOf("phút") >= 0) {
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

// Nút Hành Động
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
// Màn hình nhỏ (Narrow)
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
(dh.state == 2 ? mFont.tahoma_7b_green : (isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white)).drawString(g, dh.name, listX + 8, itemY + 4, 0);

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
int itemH = 38;
for (int i = 0; i < huCount; i++) {
HieuUngInfo hu = (HieuUngInfo)HieuUngManager.vecHieuUng.elementAt(i);
if (hu == null) continue;

int itemY = listY + i * itemH;
boolean isSel = (i == selectedHieuUngIndex);

AvMain.paintRect(g, listX + 2, itemY + 2, listW - 4, itemH - 4, (byte)(isSel ? 1 : 0), 1);
if (isSel) {
g.setColor(0xFFFF00);
g.drawRect(listX + 1, itemY + 1, listW - 3, itemH - 3);
if (AvMain.imgNenfocus != null) {
g.drawRegion(AvMain.imgNenfocus, 2, 2, listW - 4, itemH - 4, 0, listX + 2, itemY + 2, 0);
}
}

mFont titleFont = (hu.state == 1) ? mFont.tahoma_7b_green : (isSel ? mFont.tahoma_7b_yellow : mFont.tahoma_7b_white);
titleFont.drawString(g, hu.name, listX + 8, itemY + 4, 0);

if (hu.state == 1) {
mFont.tahoma_7b_green.drawString(g, "[BẬT]", listX + listW - 8, itemY + 4, 1);
} else {
mFont.tahoma_7_red.drawString(g, "[TẮT]", listX + listW - 8, itemY + 4, 1);
}

String catStr = (hu.category != null && hu.category.length() > 0) ? "[" + hu.category + "] " : "";
String desc = (hu.optionsStr != null && hu.optionsStr.length() > 0) ? hu.optionsStr : "Hiệu ứng nhân vật";
int nlIdx = desc.indexOf('\n');
if (nlIdx != -1) desc = desc.substring(0, nlIdx);
mFont.tahoma_7_white.drawString(g, catStr + desc, listX + 8, itemY + 18, 0);
}

if (huCount == 0) {
mFont.tahoma_7_white.drawString(g, "Không có hiệu ứng nào", listX + listW / 2, listY + listH / 2 - 6, 2);
}

GameCanvas.resetTrans(g);
g.setClip(0, 0, MotherCanvas.w, MotherCanvas.h);

if (hieuUngList.AD > 0) {
scrollHieuUng.setInfo(listX + listW - 3, listY, listH, 0xFF8800);
scrollHieuUng.setYScrool(hieuUngList.AC, hieuUngList.AD);
scrollHieuUng.paint(g);
}
}

public void updatePointer() {
super.updatePointer();

int tabCount = 5;
int tabW = (w - 36) / tabCount;
int tabH = 18;
int startX = x + 8;
int startY = y + 8;

if (GameCanvas.isPointerSelect) {
for (int i = 0; i < tabCount; i++) {
int curX = startX + i * tabW;
if (GameCanvas.isPoint(curX, startY, tabW - 2, tabH)) {
GameCanvas.isPointerSelect = false;
switchTab(i, true);
return;
}
}
}

if (curMainTab == 0) {
if (invenList != null) invenList.update_Pos_UP_DOWN();
if (leftStatList != null) leftStatList.update_Pos_UP_DOWN();
}
if (infoList != null && curMainTab == 1) {
infoList.update_Pos_UP_DOWN();
}
if (attriList != null && curMainTab == 1) {
attriList.update_Pos_UP_DOWN();
}
if (curMainTab == 2) {
if (skillList != null) skillList.update_Pos_UP_DOWN();
if (skillDetailList != null) skillDetailList.update_Pos_UP_DOWN();
}
if (questList != null && curMainTab == 3) {
questList.update_Pos_UP_DOWN();
}
if (curMainTab == 4) {
if (chucNangSubView == 0 && featureMenuList != null) {
featureMenuList.update_Pos_UP_DOWN();
} else if (chucNangSubView == 1 && danhHieuList != null) {
danhHieuList.update_Pos_UP_DOWN();
} else if (chucNangSubView == 2 && petList != null) {
petList.update_Pos_UP_DOWN();
}
}

// Gesture swipe on left pane equip area
if (curMainTab == 0) {
int leftPaneW = isWide ? 160 : (w * 45 / 100);
int paneX = x + 10;
int equipAreaY = y + 28 + 28;
int equipAreaH = 114;

if (GameCanvas.isPointerDown) {
if (!isDraggingEquip && GameCanvas.isPoint(paneX, equipAreaY, leftPaneW, equipAreaH)) {
isDraggingEquip = true;
equipDragStartY = GameCanvas.AZ;
equipDragStartScrollY = equipScrollY;
} else if (isDraggingEquip) {
int dy = GameCanvas.AZ - equipDragStartY;
equipScrollY = equipDragStartScrollY - dy;
if (equipScrollY < -20) equipScrollY = -20 + (equipScrollY + 20) / 3;
if (equipScrollY > EQUIP_PAGE_H + 20) equipScrollY = EQUIP_PAGE_H + 20 + (equipScrollY - EQUIP_PAGE_H - 20) / 3;
}
} else {
if (isDraggingEquip) {
isDraggingEquip = false;
int dragDist = GameCanvas.AZ - equipDragStartY;
if (dragDist < -20) {
equipToY = EQUIP_PAGE_H;
} else if (dragDist > 20) {
equipToY = 0;
} else {
equipToY = (equipScrollY > EQUIP_PAGE_H / 2) ? EQUIP_PAGE_H : 0;
}
}
}
}

if (GameCanvas.AQ) {
GameCanvas.isPointerSelect = false;
return;
}

if (!GameCanvas.isPointerSelect) return;

if (curMainTab == 0) handlePointerTab0();
else if (curMainTab == 1) handlePointerTab1();
else if (curMainTab == 2) handlePointerTab2();
else if (curMainTab == 3) handlePointerTab3();
else if (curMainTab == 4) handlePointerTab4();

if (GameCanvas.isPoint(x + w - 30, y + 4, 30, 30)) {
GameCanvas.isPointerSelect = false;
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
equipToY = EQUIP_PAGE_H;
return;
}

int fw = (rightPaneW - 8) / 5;
int fy = rightPaneY + 4;
for (int f = 0; f < 5; f++) {
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
int startSlotY = equipAreaY + 16 + 1;
int slotGapY = 24;
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

int eqSlotSize = 25;
int leftSlotX = paneX + 7;
int rightSlotX = paneX + leftPaneW - eqSlotSize - 7;
int startSlotY = equipAreaY + 16 + 1;
int slotGapY = 24;

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

int statBoxY = y + 28 + 144;
int statBoxH = h - 34 - 148;
}
return;
} else {
selectedInvenIndex = -1;
selectItem(null, 0, 0);
return;
}
}
}

// Touch on Bottom Tab Selector (sau tiền, trước số lượng ô)
int footY = rightPaneY + rightPaneH - 15;
int curMoneyX = rightPaneX + 4;
if (AvMain.fraMoney != null && GameScreen.player != null) {
String strBeri = formatMoneyShort(Player.SN);
curMoneyX += 13 + mFont.tahoma_7b_yellow.getWidth(strBeri) + 6;
String strRuby = formatMoneyShort(GameScreen.player.Ruby);
curMoneyX += 13 + mFont.tahoma_7b_red.getWidth(strRuby) + 6;
String strVnd = formatMoneyShort(GameScreen.player.PD);
curMoneyX += 13 + mFont.tahoma_7b_green.getWidth(strVnd) + 6;
int gridOffsetX = (gridW - cols * slotStep) / 2;

if (GameCanvas.isPoint(gridX, gridY, gridW, gridH)) {
int relX = GameCanvas.AY - (gridX + gridOffsetX);
int relY = GameCanvas.AZ - gridY + invenList.AC;
int clickedCol = relX / slotStep;
int clickedRow = relY / slotStep;

if (clickedCol >= 0 && clickedCol < cols && clickedRow >= 0) {
int clickedIdx = clickedRow * cols + clickedCol;
if (clickedIdx >= 0 && clickedIdx < filteredItems.size()) {
GameCanvas.isPointerSelect = false;
focusPane = 1;

if (selectedInvenIndex == clickedIdx && selectedItemInfo != null) {
doMenuAction();
} else {
selectedInvenIndex = clickedIdx;
MainItem it = (MainItem)filteredItems.elementAt(clickedIdx);
selectItem(it, GameCanvas.AY, GameCanvas.AZ);
if (GameCanvas.tabInven != null) {
GameCanvas.tabInven.itemCur = selectedItemInfo;
GameCanvas.tabInven.IdSelect = Player.vecInventory.indexOf(selectedItemInfo);
}
}
return;
} else {
selectedInvenIndex = -1;
selectItem(null, 0, 0);
return;
}
}
}

// Touch on Bottom Tab Selector (sau tiền, trước số lượng ô)
int footY = rightPaneY + rightPaneH - 15;
int totalInven = (Player.vecInventory != null) ? Player.vecInventory.size() : 0;
String capStr = totalInven + "/" + Player.maxInventory;
int capW = mFont.tahoma_7_black.getWidth(capStr);
int capX = rightPaneX + rightPaneW - capW - 3;
int tabBoxY = footY - 1;
int tabBoxH = 14;
int numTabs = 10;

if (rightPaneW >= 240) {
int curMoneyX = rightPaneX + 4;
if (AvMain.fraMoney != null && GameScreen.player != null) {
String strBeri = formatMoneyShort(Player.SN);
curMoneyX += 13 + mFont.tahoma_7b_yellow.getWidth(strBeri) + 5;
String strRuby = formatMoneyShort(GameScreen.player.Ruby);
curMoneyX += 13 + mFont.tahoma_7b_red.getWidth(strRuby) + 5;
GameCanvas.isPointerSelect = false;
selectItem(null, 0, 0);
selectedInvenIndex = -1;
setInvenPage(t);
return;
}
}
} else if (tabAreaW >= 42) {
int sBoxW = Math.min(70, tabAreaW);
int sBoxX = tabAreaLeft + (tabAreaW - sBoxW) / 2;
if (GameCanvas.isPoint(sBoxX, tabBoxY - 2, sBoxW, tabBoxH + 4)) {
GameCanvas.isPointerSelect = false;
selectItem(null, 0, 0);
selectedInvenIndex = -1;
if (GameCanvas.AY < sBoxX + 20 && curInvenPage > 0) {
GameCanvas.isPointerSelect = false;
selectItem(null, 0, 0);
selectedInvenIndex = -1;
setInvenPage(t);
return;
}
}
} else {
int sBoxW = (rightPaneW < 140) ? 38 : 48;
int sBoxX = capX - sBoxW - 3;
if (GameCanvas.isPoint(sBoxX - 2, tabBoxY - 2, sBoxW + 4, tabBoxH + 4)) {
GameCanvas.isPointerSelect = false;
selectItem(null, 0, 0);
selectedInvenIndex = -1;
if (GameCanvas.AY < sBoxX + 14) {
setInvenPage(curInvenPage > 0 ? curInvenPage - 1 : numTabs - 1);
} else if (GameCanvas.AY > sBoxX + sBoxW - 14) {
setInvenPage(curInvenPage < numTabs - 1 ? curInvenPage + 1 : 0);
} else {
setInvenPage((curInvenPage + 1) % numTabs);
}
return;
}
}

if (selectedItemInfo != null) {
selectItem(null, 0, 0);
selectedInvenIndex = -1;
}
}

private void handlePointerTab1() {
int leftPaneW = isWide ? 150 : (w * 42 / 100);
int leftPaneX = x + 10;
int leftPaneY = y + 28;
int leftPaneH = h - 34;

if (infoList != null && GameCanvas.isPointer(leftPaneX + 4, leftPaneY + 18, leftPaneW - 8, leftPaneH - 22)) {
infoList.AB();
focusPane = 0;
updateSoftKeys();
}

int rightPaneX = x + 10 + leftPaneW + 6;
int rightPaneY = y + 28;
int listH = paneH - 30;

int detailX = listX + listW + 6;
int detailY = listY;
int detailW = paneW - listW - 18;
int detailH = listH;

// Click action button
if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) {
int actBtnY = detailY + detailH - 22;
int actBtnW = detailW - 20;
int actBtnX = detailX + 10;

if (GameCanvas.isPoint(actBtnX, actBtnY, actBtnW, 18)) {
GameCanvas.isPointerSelect = false;
TitleActionBtn btn = (TitleActionBtn)selectedDanhHieuInfo.actionButtons.elementAt(0);
if (btn != null) {
GlobalService.getInstance().Send_DanhHieuAction(selectedDanhHieuInfo.id, btn.actionId);
}
return;
}
}

// Click list item
if (danhHieuList != null && GameCanvas.isPoint(listX, listY, listW, listH)) {
int relY = GameCanvas.AZ - listY + danhHieuList.AC;
int itemH = 38;
int clickedIdx = relY / itemH;
if (clickedIdx >= 0 && clickedIdx < dhCount) {
GameCanvas.isPointerSelect = false;
selectedDanhHieuIndex = clickedIdx;
selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(clickedIdx);
updateSoftKeys();
return;
int itemH = 34;

int totalSkills = (validSkills != null) ? validSkills.size() : 0;

if (skillList != null && GameCanvas.isPoint(listX, listY, leftPaneW, listH)) {
int relY = GameCanvas.AZ - listY + skillList.AC;
int clickedIdx = relY / itemH;
if (clickedIdx >= 0 && clickedIdx < totalSkills) {
GameCanvas.isPointerSelect = false;
focusPane = 1;
if (selectedSkillIndex != clickedIdx) {
selectedSkillIndex = clickedIdx;
if (skillDetailList != null) { skillDetailList.AC = 0; skillDetailList.AB = 0; }
}
updateSoftKeys();
return;
}
}

int rightPaneX = leftPaneW + 6 + x + 10;
int rightPaneY = y + 28;
int rightPaneH = h - 34;
int btnY = rightPaneY + rightPaneH - 24;

if (GameCanvas.isPoint(rightPaneX + 14, btnY, 96, 18)) {
GameCanvas.isPointerSelect = false;
openSkillHotkeyMenu();
return;
}
}

private void handlePointerTab3() {
int leftPaneW = isWide ? 160 : (w * 44 / 100);
int listX = x + 10;
int listY = y + 28;
updateSoftKeys();
}

int rightPaneX = x + 10 + leftPaneW + 6;
int rightPaneY = y + 28;
int rightPaneW = w - leftPaneW - (isWide ? 20 : 16);
int rightPaneH = h - 34;

int rowH = 26;
int startY = rightPaneY + 22;

if (attriList != null && GameCanvas.isPoint(rightPaneX, startY, rightPaneW, 5 * rowH)) {
int relY = GameCanvas.AZ - startY + attriList.AC;
int clickedRow = relY / rowH;
if (clickedRow >= 0 && clickedRow < 5) {
GameCanvas.isPointerSelect = false;
focusPane = 1;
selectedAttIndex = clickedRow;

int btnW = 32;
int btnH = 16;
int btnX = rightPaneX + rightPaneW - btnW - 10;
int btnY = startY + clickedRow * rowH + 4 - attriList.AC;

if (GameCanvas.isPoint(btnX - 4, btnY - 2, btnW + 8, btnH + 4)) {
addPotentialPoint(clickedRow);
} else {
updateSoftKeys();
}
return;
}
}
}

private void handlePointerTab2() {
int leftPaneW = isWide ? 160 : (w * 44 / 100);
int listX = x + 10;
int listY = y + 28;
int listH = h - 34;
int itemH = 34;

int detailY = listY;
int detailW = paneW - listW - 18;
int detailH = listH;

// Click action button
if (selectedDanhHieuInfo != null && selectedDanhHieuInfo.actionButtons != null && selectedDanhHieuInfo.actionButtons.size() > 0) {
int actBtnY = detailY + detailH - 22;
int actBtnW = detailW - 20;
int actBtnX = detailX + 10;

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
}
return;
}
}

// Click list item
if (danhHieuList != null && GameCanvas.isPoint(listX, listY, listW, listH)) {
int relY = GameCanvas.AZ - listY + danhHieuList.AC;
int itemH = 38;
int clickedIdx = relY / itemH;
if (clickedIdx >= 0 && clickedIdx < dhCount) {
GameCanvas.isPointerSelect = false;
selectedDanhHieuIndex = clickedIdx;
selectedDanhHieuInfo = (DanhHieuInfo)Player.vecDanhHieu.elementAt(clickedIdx);
updateSoftKeys();
return;
}
}
} else {
int listX = paneX + 6;
int listY = paneY + 24;
int listW = paneW - 12;
int listH = paneH - 30;

if (danhHieuList != null && GameCanvas.isPoint(listX, listY, listW, listH)) {
int relY = GameCanvas.AZ - listY + danhHieuList.AC;
int itemH = 38;
int clickedIdx = relY / itemH;
if (clickedIdx >= 0 && clickedIdx < dhCount) {
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
updateSoftKeys();
return;
}
}
}
return;
}
}
}

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
