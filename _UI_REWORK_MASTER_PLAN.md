# HAI TẶC TÍ HON - MASTER UI/UX ARCHITECTURE & REWORK PLAN
**Tài liệu:** `_UI_REWORK_MASTER_PLAN.md`  
**Dự án áp dụng:** `ProjectJ2me129`, `ProjectUnity129`, `HaiTacTiHonServer`  
**Tác giả:** SENIOR GAME UI ARCHITECT + UI/UX ENGINEER + J2ME/UNITY/SERVER/GRAPHICS PIPELINE TEAM  
**Ngày thiết lập:** 2026-09-17  
**Trạng thái:** Sẵn sàng phê duyệt & triển khai

---

## 1. ARCHITECTURE HIỆN TẠI

### 1.1 ProjectJ2me129 (Java CLDC 1.1 / MIDP 2.0)
* **Canvas & Game Loop:** `MotherCanvas.java` quản lý hiển thị toàn màn hình (`setFullScreenMode(true)`), kích thước màn hình logic (`MotherCanvas.w`, `h`, `hw`, `hh`) và vòng lặp `run()` với chu kỳ sleep 33ms (~30 FPS).
* **Điều phối hiển thị (`GameCanvas.java`):** Phân cấp render tuần tự: `currentScreen.paint(g)` $\rightarrow$ `subDialog.paint(g)` $\rightarrow$ `currentDialog.paint(g)` $\rightarrow$ `menuCur.paint(g)` $\rightarrow$ `Interface_Game.paint(g)`.
* **Graphics Wrapper (`mGraphics.java`):** Bọc đối tượng `javax.microedition.lcdui.Graphics`, hỗ trợ translation, setClip, drawRegion.
* **Hệ thống Font (`mFont.java`):** 100% Bitmap Font cắt từ sprite sheet (`/mfont/tahoma_7` và `tahoma_7b`).
* **Hệ thống UI Component:** Kế thừa từ `AvMain.java`. Quản lý 3 Softkeys (`left`, `right`, `center`) và D-Pad.

### 1.2 ProjectUnity129 (Unity 2022.3.62f3)
* **Cơ chế Rendering:** 100% **Unity IMGUI (Immediate Mode GUI)** thông qua `Main.OnGUI()` gọi `GameMidlet.gameCanvas.paint(g)`. Không sử dụng uGUI (`Canvas`, `RectTransform`, `EventSystem`).
* **Mối liên hệ với J2ME:** Là bản port trực tiếp 1:1 từ mã nguồn Java J2ME sang C#. Các cấu trúc I/O (`ByteArrayInputStream`, `DataInputStream`), RMS (`Rms.cs`), mạng (`Session_ME`), collections (`mVector`) giữ nguyên định dạng nhị phân và tư duy thiết kế của J2ME.
* **Xử lý độ phân giải:** `MotherCanvas.checkZoomLevel()` tính toán `zoomLevel` (1x, 2x, 3x, 4x) để chia độ phân giải thực tế của thiết bị thành canvas logic (`w = Screen.width / zoomLevel`).

### 1.3 HaiTacTiHonServer (Java Server Core)
* **Kiến trúc mạng:** Java NIO Non-blocking SocketChannel, phân tách luồng nhận (`receive-thread`) và luồng gửi (`send-thread` gom tối đa 32 message/batch) với bộ đệm `sendBuffer` 128KB.
* **Mã hóa:** Handshake key trao đổi qua cmd `-27`. Mã hóa Rolling XOR đối xứng bằng key chuỗi 9 ký tự (`huathanh@`).
* **Ranh giới:** Server là Headless Data Provider thuần túy. Server chỉ gửi ID, tên, chỉ số, phẩm cấp, dữ liệu nhị phân thô; hoàn toàn không chứa tọa độ hiển thị, màu sắc hay bố cục layout của Client.

---

## 2. UI INVENTORY (DANH MỤC TOÀN BỘ 45 HỆ THỐNG UI)

1. **Login Screen (`LoginScreen`)**: Đăng nhập tài khoản, mật khẩu, chọn cụm máy chủ.
2. **First Login / Server Select (`FristLoginScreen`)**: Chọn server dạng lưới, phân trang, thông báo sự kiện.
3. **Loading / Map Transition (`LoadMapScreen`)**: Thanh tiến trình tải tài nguyên, chuyển cảnh đảo.
4. **Main Battle HUD (`Interface_Game`)**: Khung điều khiển chính khi di chuyển và chiến đấu trong map.
5. **Player Status HUD (`paintInfoPlayer`)**: Thanh HP, MP, EXP, cấp độ, avatar nhân vật, vệt máu mất dần (damage trail).
6. **Target Status HUD (`paintInfoFocus`)**: Thanh máu và thông tin quái vật/đối thủ đang chọn mục tiêu.
7. **Skill Bar / Controls (`Interface_Game`)**: Cụm phím kỹ năng cảm ứng (Radial Arc hoặc Linear bar), phím đánh thường, đổi mục tiêu.
8. **Buff / Debuff Icons**: Hiển thị trạng thái tăng/giảm lực chiến, thời gian tồn tại trên đầu nhân vật.
9. **Quest Tracker HUD**: Bảng theo dõi tiến độ nhiệm vụ thu nhỏ ở góc màn hình.
10. **Mini Information Panel**: Thông tin ping, FPS, tọa độ map, kênh hiện tại.
11. **Chat Screen (`ChatTabScreen`)**: Hệ thống chat 6 kênh (Hộp thư, Thế giới, Công cộng, Riêng tư, Bang hội, Hệ thống).
12. **Chat Bubble (`PopupChat`)**: Bong bóng chat nổi trên đầu nhân vật trong map.
13. **System Notifications / Toast (`GameScreen.addNotify`)**: Dòng chữ thông báo chạy ngang hoặc nổi ở giữa màn hình.
14. **Standard Dialog (`MsgDialog`)**: Hộp thoại thông báo text cơ bản có nút OK hoặc Đóng.
15. **NPC Dialog & Dynamic Menu (`MenuController` / `Menu`)**: Hộp thoại trò chuyện với NPC và danh sách lựa chọn chức năng.
16. **Quick Context Menu (`QuickMenu`)**: Menu thao tác nhanh khi chạm vào nhân vật khác (Giao dịch, Kết bạn, Đồ sát, Xem thông tin).
17. **Shop Giao Diện 5 Phái (`TabShop`)**: Cửa hàng vũ khí, trang phục theo môn phái.
18. **Shop Dược Phẩm & Tiêu Hao**: Quán ăn, máu, mana, vé sự kiện.
19. **Shop Nguyên Liệu & Đá Khảm**: Nơi mua bán nguyên liệu chế tác, đá đục lỗ.
20. **Rương Kho Đồ (`TabChest`)**: Kho chứa đồ cá nhân của người chơi tại NPC.
21. **Hành Trang Cá Nhân (`TabInventory` / `DualTabScreen`)**: 126 ô chứa vật phẩm, phân trang danh mục.
22. **Chi Tiết Vật Phẩm / Tooltip Popup (`MainTab.paintInfoEveryWhere`)**: Bảng so sánh chỉ số, phẩm màu, dòng thuộc tính, ngọc khảm.
23. **Giao Diện Trang Bị Đang Mặc (`TabEquip` / `DualTabScreen`)**: 16 ô trang bị thường và Thần trang.
24. **Bảng Thuộc Tính Nhân Vật (`TabInfo` / `DualTabScreen`)**: 5 điểm tiềm năng gốc và 48 chỉ số chiến đấu mở rộng.
25. **Cây Kỹ Năng & Nâng Cấp Skill (`TabSkill`)**: Danh sách kỹ năng chủ động, bị động, kỹ năng Trái Ác Quỷ.
26. **Bảng Nhiệm Vụ Chi Tiết (`TabQuest`)**: Quản lý nhiệm vụ chính tuyến, phụ tuyến, nhiệm vụ lặp.
27. **Tổ Đội / Nhóm (`PartyScreen`)**: Quản lý thành viên nhóm, máu đồng đội, mời/đuổi khỏi nhóm.
28. **Bang Hội (`Clan_Screen`)**: Thông tin bang, danh sách thành viên, cấp bậc, quỹ bang, cờ bang.
29. **Huy Hiệu Hành Trình Bang (`HuyHieuClanScreen`)**: Bảng nhiệm vụ tích lũy danh hiệu bang hội.
30. **Hòm Thư & Quà Tặng (`ChatTabScreen` / `MailService`)**: Danh sách thư, nhận đính kèm quà tặng.
31. **Danh Sách Bạn Bè & Kẻ Thù**: Quản lý quan hệ xã hội trong game.
32. **Bảng Xếp Hạng (Ranking)**: Top lực chiến, top cấp độ, top truy nã hải quân.
33. **Bản Đồ Thế Giới & Chuyển Map**: Chọn đảo di chuyển, thông tin tuyến đường Red Line.
34. **Cài Đặt Hệ Thống (Settings)**: Bật/tắt âm thanh, đồ họa thấp, chế độ tai thỏ, khóa màn hình, FPS target.
35. **Tùy Chọn Đồ Họa & Góc Nhìn (Options)**: Tùy chỉnh zoomLevel, hướng màn hình (Ngang/Dọc).
36. **Hộp Thoại Xác Nhận Yes/No (`ClientYesNo`)**: Xác nhận mua bán giá trị cao, xóa đồ, đổi kỹ năng.
37. **Cảnh Báo & Lỗi Hệ Thống**: Thông báo bảo trì, mất kết nối, sai phiên bản.
38. **Nhận Quà / GiftCode Dialog (`MsgShowGift`)**: Giao diện mở rương quà may mắn, nhận thưởng sự kiện.
39. **Giao Diện Sự Kiện (Event UI)**: Cây thông, vòng quay may mắn, mini-game theo mùa.
40. **Thông Tin Boss Thế Giới (`BigBossLittleGraden`)**: Máu boss liên server, bảng xếp hạng sát thương gây ra.
41. **Giao Diện PvP / Đấu Trường (`PvPScreen`)**: Ghép cặp lôi đài, đếm ngược thời gian thi đấu.
42. **Màn Hình Nâng Cấp / Cường Hóa (`ScreenUpgrade`)**: Giao diện đập đồ, ghép đá, chế tác trang bị.
43. **Màn Hình Giao Dịch Người Chơi (`TradeScreen`)**: 2 bên đặt đồ và Beri, xác nhận khóa và đồng ý giao dịch.
44. **Hộp Thoại Tự Kết Nối Lại (`MsgDialog.isAuroReconect`)**: Đếm ngược thử kết nối lại khi rớt mạng.
45. **Bàn Phím Ảo / Input Touch Feedback**: Con trỏ chuột ảo, hiệu ứng sóng chạm cảm ứng, phím D-Pad phản hồi lực.

---

## 3. MESSAGE & PROTOCOL INVENTORY

| Mã CMD | Phân loại | Chiều | Cấu trúc Dữ liệu Nhị phân chính | Vai trò UI tương ứng |
| :---: | :---: | :---: | :--- | :--- |
| **-27** | System | S $\leftrightarrow$ C | `byte keyLen`, `byte key0`, `byte[] keys` | Bắt tay mã hóa đường truyền Rolling XOR |
| **-11** | Dialog | S $\rightarrow$ C | `short id, byte type, UTF title, UTF text, byte cmdCount, [cmds...]` | Mở MessageBox, Confirm Yes/No, Dialog nhận thưởng |
| **-11** | Dialog | C $\rightarrow$ S | `short id, byte choiceValue` | Gửi lựa chọn nút bấm của người chơi lên server |
| **-81** | Dialog | S $\rightarrow$ C | `short id, UTF title, byte optCount, [UTF prompt...]` | Yêu cầu mở InputDialog nhập văn bản/số |
| **-58** | Dialog | C $\rightarrow$ S | `short id, byte count, [UTF value...]` | Gửi nội dung đã nhập lên server xử lý |
| **-31** | Notify | S $\rightarrow$ C | `byte type, UTF text, byte color, short icon` | Hiển thị thông báo Toast / Banner chạy trên màn hình |
| **47** | Dialog | S $\rightarrow$ C | `byte type=1` | Lệnh từ Server buộc đóng dialog hiện tại |
| **-19** | Menu | C $\rightarrow$ S | `short npcId` | Yêu cầu mở menu tương tác với NPC |
| **-20** | Menu | S $\rightarrow$ C | `byte menuType, short npcId, byte menuId, UTF title, byte optCount, [options...]` | Hiển thị Dynamic Menu ngữ cảnh có tiêu đề và danh sách nút |
| **-20** | Menu | C $\rightarrow$ S | `short npcId, byte menuId, byte selectedIndex` | Gửi chỉ số dòng menu người chơi vừa chọn |
| **-19** | Shop | S $\rightarrow$ C | `byte shopType, UTF title, byte cat, short count, [items...]` | Mở màn hình Cửa hàng / Rương kho tương ứng |
| **-18** | Shop | C $\rightarrow$ S | `byte shopType, short itemId, short quantity` | Gửi yêu cầu mua vật phẩm |
| **-21** | Shop | C $\rightarrow$ S | `byte action, byte cat, short itemId, short quantity, short slotIndex` | Bán vật phẩm cho shop hoặc vứt bỏ |
| **-12** | Inventory | S $\rightarrow$ C | `byte action, byte cat, [items / currencies / maxSlots...]` | Đồng bộ toàn bộ hoặc từng phần túi đồ và tiền tệ |
| **-32** | Chest | S $\rightarrow$ C | Tương tự format của `-12` áp dụng cho Rương đồ | Đồng bộ rương chứa đồ cá nhân |
| **19** | Equip | S $\rightarrow$ C | `short mapId, byte 0, short head, short hair, byte 16, [16 slots...]` | Đồng bộ 16 trang bị đang mặc trên người nhân vật |
| **-105** | Item | S $\leftrightarrow$ C | C$\rightarrow$S: `short itemId` \| S$\rightarrow$C: `short itemId, UTF description` | Truy vấn và nhận mô tả chi tiết của Dược phẩm / Item loại 4 |
| **-10** | Character | S $\rightarrow$ C | `short mapId, UTF name, 4 ints (HP/MP), 4 shorts, 5 attributes, 48 combat stats` | Đồng bộ toàn diện chỉ số chiến đấu của nhân vật |
| **-75** | Character | S $\rightarrow$ C | `short fashionId, short cdReduction, short manaReduction` | Cập nhật thời trang và % giảm thời gian hồi chiêu |
| **-42** | Character | S $\leftrightarrow$ C | C$\rightarrow$S: `UTF name` \| S$\rightarrow$C: Full profile đối thủ + 16 trang bị | Soi thông tin người chơi khác / đối thủ |
| **-7** | Skill | S $\rightarrow$ C | `byte 3, byte skillCount, [write_data_skill: id, damage, cd, options...]` | Tải toàn bộ cây kỹ năng và cấp độ skill |
| **-17** | Skill | C $\rightarrow$ S | `short skillId, short points` | Thao tác cộng điểm kỹ năng |
| **-16** | Character | C $\rightarrow$ S | `byte attrType, short points` | Thao tác cộng điểm tiềm năng (Sức mạnh, Thể lực...) |
| **-23** | Quest | S $\leftrightarrow$ C | S$\rightarrow$C: `byte subType, questData` \| C$\rightarrow$S: `byte action, short questIndex` | Hệ thống nhận, cập nhật, hủy và trả nhiệm vụ |
| **-110**| Mail | S $\leftrightarrow$ C | S$\rightarrow$C: `byte 2, int mailId` (xóa thư) \| C$\rightarrow$S: nhận quà / đọc thư | Thao tác quản lý hòm thư cá nhân |
| **-34** | Reward | S $\rightarrow$ C | `byte type, UTF title, UTF notice, byte count, [gifts: icon, num, color...]` | Popup nhận quà thưởng sự kiện / code |
| **-52** | Clan | S $\leftrightarrow$ C | S$\rightarrow$C: `byte type, clanData` \| C$\rightarrow$S: thao tác bang hội | Quản lý thành viên, kỹ năng và quỹ bang |
| **-25** | Party | S $\leftrightarrow$ C | S$\rightarrow$C: `byte type, partyData` \| C$\rightarrow$S: mời, rời, giải tán | Quản lý tổ đội và đồng đội trong map |

---

## 4. DIALOG INVENTORY
* **`MsgDialog` (Standard)**: Chiều rộng cũ bị giới hạn cứng $\le 200$ px. Hiển thị thông báo, nhận thưởng, xác nhận.
* **`InputDialog`**: Hộp thoại nhập tên nhân vật, số lượng mua/bán, mã giftcode, mã OTP. Chiều rộng cũ bị giới hạn cứng $\le 200$ px.
* **`MainDialog`**: Lớp cha quản lý mảng text đã wrap dòng (`strinfo`), tọa độ căn giữa `(AX, AY)`, quản lý timeout.
* **`MsgAutoFire`**: Hộp thoại hướng dẫn tự động đánh và hỗ trợ tân thủ.
* **`MsgShowGift`**: Popup chuyên dụng hiển thị danh sách quà tặng nhận được dạng lưới icon viền phẩm cấp.

---

## 5. SHOP INVENTORY
* **Phân loại Shop:**
  1. Shop Trang bị Kiếm khách, Hoa tiêu, Xạ thủ, Đầu bếp, Đấu sĩ (Type 0..4).
  2. Quán ăn / Hồi phục HP/MP (Type 20).
  3. Tiệm Nguyên liệu chế tác (Type 6).
  4. Cửa hàng Đá ghép / Khảm ngọc (Type 111).
  5. Cửa hàng Hạn giờ / Đổi điểm Sự kiện (Type 116, 118).
  6. Rương Kho chứa đồ (Type 99).
  7. Chuộc lại trang bị đã bán nhầm (Type 119).
* **Lưới ô Shop cũ:** Bị giới hạn $6 \times 6 = 36$ ô, cố định kích thước ô 28px hoặc 32px, không tận dụng được không gian khi mở trên màn hình Full HD / 2K.

---

## 6. ITEM UI INVENTORY
* **Ô Slot (`drawSlot`)**: Kích thước 28px (J2ME) / 32px (Unity). Cần viền sáng theo độ hiếm (Trắng, Xanh lá, Xanh lam, Tím, Cam, Đỏ Mythic).
* **Tooltip Chi tiết Trang bị (`paintInfoEveryWhere`)**:
  * Hiển thị: Icon, tên trang bị, cấp độ yêu cầu, cấp cường hóa (+1 đến +16), phẩm chất màu.
  * Chỉ số cơ bản: Tấn công, phòng thủ, máu...
  * Dòng ẩn / Kích hoạt: Kích hoạt ngũ hành, kích hoạt bộ trang bị.
  * Lỗ đục & Ngọc khảm: Trạng thái lỗ đục và ngọc gắn vào.
  * Hạn sử dụng: Thời gian đếm ngược hoặc vĩnh viễn.
* **So sánh Trang bị (Item Compare)**: Tự động đối chiếu với món đồ đang mặc cùng slot, hiển thị số chênh lệch màu xanh lá (tăng) hoặc đỏ (giảm).

---

## 7. INPUT INVENTORY
* **Touch Event Engine:** `pointerPressed`, `pointerDragged`, `pointerReleased` được quy đổi về tọa độ logic `(px, py)` thông qua `zoomLevel`.
* **Trạng thái Pointer:** `isPointerDown` (đang chạm), `isPointerSelect` (chạm và nhấc tay không rê chuột = Click), `isPointerMove` (kéo trượt vượt ngưỡng 15px).
* **Keypad & Softkeys:**
  * D-Pad: Phím 2 (Lên), 8 (Xuống), 4 (Trái), 6 (Phải), 5/Fire (Chọn).
  * Softkeys: Phím `-6` (Trái - Menu/Chọn), `-7` (Phải - Đóng/Thoát).
  * Phím số 0–9 gán cho hotkey sử dụng bình máu/mana và kỹ năng nhanh.
* **Controls ảo trên màn hình cảm ứng:** Cụm Arc Skill đa hướng góc dưới bên phải và phím D-Pad di chuyển góc dưới bên trái.

---

## 8. ASSET INVENTORY
* **Tài nguyên J2ME (`ProjectJ2me129/res`):**
  * Bitmap Fonts: `/mfont/tahoma_7`, `/mfont/tahoma_7b`.
  * Giao diện cổ điển: `imgPaper` (giấy cuộn nền), `imgButton` (nút gỗ viền), `imgKhungItem` (ô slot 28px), `imgTrangTri`.
* **Tài nguyên Unity (`ProjectUnity129/Assets/Resources`):**
  * TrueType Fonts: `fontbu.ttf`, `fontnho.ttf`, `fontx1.ttf`.
  * Sprite Sheets & Textures: thư mục `res/x1` (1x pixel), `res/x2` (2x pixel).
* **Asset Đồ họa Vector Hiện đại mới (`ModernUI`):**
  * Hệ thống render thủ tục không tốn RAM texture: Gradient chuyển sắc 6 bước, Card bo góc, Viền Slate/Gold kim loại, Thanh máu phát sáng có vệt trail, Badge thông báo đỏ.

---

## 9. BUG LIST & CURRENT DEFECTS (10 LỖI TRỌNG YẾU)

1. **Bug 01 (Touch Click-through trong Shop/Kho):** Khi mở bảng thông tin vật phẩm (`ShowInfo`), bấm vào bảng Info lại kích hoạt chọn ô đồ nằm ngầm bên dưới.
2. **Bug 02 (Lệch tâm nút bấm trong `iCommand`):** Nút bấm dấu cộng `+` cường hóa tính theo tọa độ góc nhưng hit-test kiểm tra theo tâm nút, gây lệch vùng chạm 15–20 pixel.
3. **Bug 03 (Mất Layout khi xoay màn hình / đổi kích thước):** `TabScreen` chỉ tính tọa độ 1 lần lúc mở game, khi xoay màn hình đồ họa vẽ vị trí mới nhưng vùng chạm giữ tọa độ cũ.
4. **Bug 04 (Tràn cột lưới ô `MainTabShop:241`):** Chạm vào mép phải ô cuối cùng làm công thức chia ra `col = BW`, kích hoạt nhảy sang dòng kế tiếp.
5. **Bug 05 (Lệch vùng chạm do quán tính cuộn `ListNew`):** Khi danh sách đang cuộn mượt theo quán tính, chạm vào ô đồ bị nhận sai vị trí do độ trễ nội suy tọa độ.
6. **Bug 06 (Khung Dialog bị teo nhỏ trên màn hình lớn):** `MsgDialog` và `InputDialog` bị giới hạn cứng $\le 200$ px, khiến chữ bị bẻ vụn và trống trải trên màn hình HD/FHD.
7. **Bug 07 (Thanh HUD máu/mana quá bé):** Thanh HP cố định dài 66px, avatar 88x45px, rất khó quan sát trên màn hình điện thoại hiện đại.
8. **Bug 08 (Nút bấm popup trong Shop không ăn cảm ứng):** Phương thức gán vị trí nút `AO()` tại `MainTabShop.java:348` để rỗng khiến nút mua không có tọa độ touch.
9. **Bug 09 (Lãng phí bộ nhớ và giật lag GC trong Hòm thư):** Hàm `paintMailListView` cấp phát `new mVector()` mỗi frame (90 allocations/giây) làm máy J2ME bị drop FPS.
10. **Bug 10 (Không tận dụng Safe Area phần cứng trên thiết bị tai thỏ):** Unity đã tính toán `safeLeft/safeRight` nhưng UI vẫn dùng cờ thủ công `isTaiTho` cộng bừa vài pixel.

---

## 10. ROOT CAUSE (PHÂN TÍCH NGUYÊN NHÂN GỐC RỄ)

* **Root Cause 1:** Thiếu cơ chế **Event Swallowing (Nuốt sự kiện)**. Lớp UI con vẽ đè lên lớp cha nhưng không chiếm quyền ưu tiên của con trỏ chuột, để sự kiện xuyên thấu xuống dưới.
* **Root Cause 2:** Xung đột chuẩn hệ quy chiếu giữa **Center Anchor** của nút bấm lệnh (`iCommand`) và **Top-Left Anchor** của cửa sổ chứa (`MainTab`).
* **Root Cause 3:** Thiếu hàm tái khởi tạo Responsive `init()` khi có sự kiện `refreshDisplay()` hoặc thay đổi kích thước cửa sổ.
* **Root Cause 4:** Công thức toán học chia lấy phần nguyên `(AY - AO) / AE` không có hàm kẹp cận trên `Math.min(col, cols - 1)`.
* **Root Cause 5:** Cấp phát động đối tượng trong vòng lặp vẽ `paint()` vi phạm nguyên lý cơ bản của lập trình Game J2ME.

---

## 11. VISUAL DESIGN SYSTEM (HỆ THỐNG NHẬN DIỆN THIẾT KẾ ĐỘC QUYỀN)

Định hướng phong cách: **PIRATE ADVENTURE + OCEAN SLATE + NAUTICAL GOLD + MODERN MOBILE RPG**. Không sao chép giao diện game khác, tạo bản sắc riêng biệt cho Hải Tặc Tí Hon.

### 11.1 Bảng Màu Chuẩn (Color Palette Tokens)
* **Màu Nền (Backgrounds):**
  * `COLOR_BG_DARK = 0x0C131F`: Nền đen hải quân sâu thẳm.
  * `COLOR_BG_WINDOW = 0x111B2C`: Nền cửa sổ chính.
  * `COLOR_BG_CARD = 0x18253B`: Nền thẻ thành phần / panel con.
  * `COLOR_BG_CARD_HOVER = 0x273C5E`: Nền khi chạm hoặc focus vào thẻ.
* **Màu Kim Loại Hải Quân (Nautical Gold & Slate Borders):**
  * `COLOR_GOLD_BRIGHT = 0xFFE066`: Viền vàng phát sáng khi được chọn.
  * `COLOR_GOLD_BASE = 0xD4AF37`: Vàng đồng hải tặc truyền thống.
  * `COLOR_GOLD_DARK = 0x8C681E`: Đổ bóng viền kim loại.
  * `COLOR_BORDER_SLATE = 0x2D415E`: Viền khung phân cách đá phiến.
* **Màu Thanh Chỉ Số (Combat Gauges):**
  * HP: `0xFF3B56` (Top) $\rightarrow$ `0xB3001E` (Bottom), vệt máu mất dần: `0xFFB3BA`.
  * MP: `0x00D9FF` (Top) $\rightarrow$ `0x0066CC` (Bottom).
  * EXP / Thần trang: `0x2ECC71` (Top) $\rightarrow$ `0x1B7A43` (Bottom).
* **Màu Phẩm Cấp Trang Bị (6 Tiers Rarity):**
  * Cấp 0 (Thường / Trắng): `0x95A5A6` (Nền: `0x1A222D`)
  * Cấp 1 (Tốt / Xanh lá): `0x2ECC71` (Nền: `0x142B20`)
  * Cấp 2 (Hiếm / Xanh lam): `0x3498DB` (Nền: `0x142738`)
  * Cấp 3 (Tuyệt phẩm / Tím): `0x9B59B6` (Nền: `0x261B33`)
  * Cấp 4 (Truyền thuyết / Cam): `0xF39C12` (Nền: `0x332612`)
  * Cấp 5 (Thần thoại / Đỏ Mythic): `0xE74C3C` (Nền: `0x331414`)

### 11.2 Nút Bấm Chuẩn (Button Hierarchy)
* `BTN_GOLD`: Nút hành động chính (Mua, Trang bị, Nâng cấp, Đồng ý).
* `BTN_BLUE`: Nút điều hướng, chuyển tab, thông tin chi tiết.
* `BTN_GREEN`: Nút sử dụng, hồi phục, nhận thưởng.
* `BTN_RED`: Nút hủy bỏ, từ chối, vứt bỏ, tháo đồ.
* `BTN_GRAY`: Nút vô hiệu hóa hoặc đóng cửa sổ phụ.

---

## 12. RESPONSIVE STRATEGY (CHIẾN LƯỢC 2 CHẾ ĐỘ THÍCH ỨNG)

```
                              [Screen Size Check]
                                       │
                  ┌────────────────────┴────────────────────┐
                  ▼                                         ▼
      [MODE A: SMALL SCREEN]                    [MODE B: LARGE SCREEN]
   - Điều kiện:                              - Điều kiện:
     + MotherCanvas.w < 320                    + MotherCanvas.w >= 320
     hoặc MotherCanvas.h < 260                 và MotherCanvas.h >= 260
                  │                                         │
                  ▼                                         ▼
      [Legacy Layout Preserved]                 [Modern Responsive Engine]
   - Bố cục 1 cột cuộn                       - Layout 2 cột song song (Master-Detail)
   - Giữ nguyên TabScreen cổ điển            - Kích hoạt DualTabScreen & ModernUI
   - 3 Phím mềm Softkeys                     - Dialog co giãn W = min(480, w - 40)
   - Vá 100% các lỗi lệch touch              - Cột trang bị bên trái, túi đồ bên phải
```

### Quy tắc Breakpoint động:
* **Compact ($W < 320$ hoặc $H < 260$):** Giữ bố cục 1 cột truyền thống, sửa sạch các lỗi lệch tọa độ, bảo đảm chạy mượt trên máy Java và màn hình nhỏ.
* **Standard Tablet / Phone ($320 \le W \le 580$):** Kích hoạt giao diện 2 cột `DualTabScreen` với độ rộng $W = W_{screen} - 20$, túi đồ 6 cột.
* **Large / PC / Ultrawide ($W > 580$):** Cửa sổ đặt ở tâm màn hình $W = \min(680, W_{screen} \times 0.85)$, túi đồ mở rộng lên 8 cột, khoảng cách spacing thích ứng theo tỉ lệ màn hình.

---

## 13. J2ME STRATEGY (TỐI ƯU HÓA PROJECTJ2ME129)
* **Khai thác `ModernUI.java`:** Vẽ giao diện phẳng và gradient bằng thủ tục vector `fillGradientRect`, không nạp thêm bất kỳ file ảnh texture nặng nào vào bộ nhớ RAM.
* **Nguyên tắc Không Cấp Phát trong Paint (Zero-Allocation Paint):**
  * Tái sử dụng đối tượng `mVector` và `StringBuffer` tĩnh trong `ChatTabScreen` và `Interface_Game`.
  * Tuyệt đối không gọi `new` trong chu trình vẽ của frame.
* **An toàn Bộ nhớ Clip:** Luôn dùng `GameCanvas.resetTrans(g)` để khôi phục viewport sau khi cuộn danh sách, ngăn chặn lỗi biến mất UI con.

---

## 14. UNITY STRATEGY (TỐI ƯU HÓA PROJECTUNITY129)
* **Giữ vững Kiến trúc IMGUI:** Không đưa uGUI Canvas nặng nề vào làm phá vỡ kiến trúc game loop hiện tại; triển khai `ModernUI.cs` đồng bộ 100% logic với J2ME.
* **Tự động áp dụng Safe Area:** Đọc `MobileInputManager.safeLeft` và `safeRight` để tự động cộng lề cho các thanh điều hướng và menu cạnh mép màn hình.
* **Bảo vệ Vùng chạm (Modal Event Blocker):** Thêm cơ chế chặn chạm trong `MainTabShop.cs` để khi popup xuất hiện, vùng chạm không bị lọt xuống lưới đồ bên dưới.

---

## 15. SERVER IMPACT (ẢNH HƯỞNG ĐỐI VỚI SERVER)
* **Khẳng định:** **GIỮ NGUYÊN 100% MÃ NGUỒN CỦA HAITACTIHONSERVER.**
* **Lý do:** Server đã truyền đầy đủ toàn bộ dữ liệu nghiệp vụ nhị phân cần thiết. Việc thiết kế lại UI, responsive layout và sửa lỗi lệch vùng chạm là trách nhiệm thuần túy ở phía Client. Không chỉnh sửa Server để triệt tiêu mọi rủi ro về gameplay, logic combat, kinh tế và network.

---

## 16. MIGRATION PLAN (PHÂN KỲ 14 BƯỚC TRIỂN KHAI)

* **PHASE 0: Backup & Verification (ĐÃ HOÀN THÀNH 100%)**
* **PHASE 1: Khảo sát Kiến trúc Toàn diện (ĐÃ HOÀN THÀNH 100%)**
* **PHASE 2: Hoàn thiện Design System & Token màu (`ModernUI.java` & `ModernUI.cs`)**
* **PHASE 3: Xây dựng Responsive Engine & Breakpoint (`GameCanvas.isCompactMode()`, `MotherCanvas`)**
* **PHASE 4: Chuẩn hóa Bộ Component dùng chung (Modern Window, Button, Slot, Card, Badge, Progress Bar)**
* **PHASE 5: Nâng cấp Toàn bộ Hộp thoại & Thông báo (`MsgDialog`, `InputDialog`, `MsgShowGift`)**
* **PHASE 6: Nâng cấp Hệ thống Menu & NPC Dialogue (`Menu.java`, `Menu.cs`)**
* **PHASE 7: Triển khai Cửa hàng & Hành trang Hiện đại (`DualTabScreen`, vá lỗi lệch touch Shop)**
* **PHASE 8: Nâng cấp Bảng Nhân vật, Kỹ năng & Nhiệm vụ trong `DualTabScreen`**
* **PHASE 9: Tối ưu Giao diện Chat & Hộp thư (`ChatTabScreen`) loại bỏ GC lag**
* **PHASE 10: Tinh chỉnh Main Battle HUD (`Interface_Game`) hỗ trợ cả 2 chế độ**
* **PHASE 11: Tối ưu hóa Unity (Safe Area, Touch Raycast Blocker)**
* **PHASE 12: Tối ưu hóa J2ME (Kiểm soát GC, memory footprint)**
* **PHASE 13: Đồng bộ và Tích hợp Toàn diện 2 Client**
* **PHASE 14: Kiểm thử Hồi quy Toàn bộ Hệ thống (Regression Testing)**

---

## 17. TEST PLAN (KẾ HOẠCH KIỂM THỬ TOÀN DIỆN)

### A. Kiểm thử Biên dịch (Build Tests)
* J2ME: `cmd /c "test_compile.bat"` trong `ProjectJ2me129` $\rightarrow$ Exit Code 0, 0 compile errors.
* Unity: Biên dịch toàn bộ C# scripts trong `ProjectUnity129` $\rightarrow$ 0 compilation errors.
* Server: `cmd /c "build.bat"` trong `HaiTacTiHonServer` $\rightarrow$ `BUILD SUCCESS`.

### B. Kiểm thử Độ phân giải & Responsive (Responsive Tests)
* Màn hình nhỏ ($240 \times 320$, $320 \times 240$): Kiểm tra kích hoạt chế độ Compact, giữ đúng UI cũ, không bị che chữ, không tràn màn hình.
* Màn hình chuẩn ($1280 \times 720$, $1920 \times 1080$): Kiểm tra tự động chuyển Modern UI, hiển thị 2 cột song song mượt mà.
* Màn hình dài ($19.5:9$, $20:9$): Kiểm tra lề an toàn Safe Area, không bị đè lên tai thỏ/nốt ruồi.
* Tablet ($4:3$ / iPad): Kiểm tra không bị tràn viền nút đóng.

### C. Kiểm thử Thao tác Chạm & Phím (Input Tests)
* Test chạm từng ô slot đồ trong Shop và Hành trang: Visual Rect = Touch Rect = Logical Rect.
* Test popup xem thông tin trang bị: Bấm vào popup không làm kích hoạt ô đồ bên dưới.
* Test các nút bấm trong Dialog, Menu NPC, bàn phím ảo.
* Test phím cứng trên J2ME (D-Pad, phím số, Softkeys).

---

## 18. ROLLBACK PLAN (KẾ HOẠCH KHÔI PHỤC DỰ PHÒNG)

Nếu có bất kỳ sự cố không mong muốn nào xảy ra:
1. Bản sao lưu đầy đủ đã được niêm phong tại:
   `C:\DepLor\HTTH\Team\_BACKUP_UI_REWORK\20260917_045255\`
   và file ZIP:
   `C:\DepLor\HTTH\Team\_BACKUP_UI_REWORK\HTTH_UI_REWORK_BACKUP_20260917_045255.zip` (679.68 MB).
2. Lệnh khôi phục nhanh bằng robocopy:
   * Khôi phục J2ME: `robocopy "_BACKUP_UI_REWORK\20260917_045255\01_ProjectJ2me129" "ProjectJ2me129" /E /MT:32`
   * Khôi phục Unity: `robocopy "_BACKUP_UI_REWORK\20260917_045255\02_ProjectUnity129" "ProjectUnity129" /E /MT:32`
   * Khôi phục Server: `robocopy "_BACKUP_UI_REWORK\20260917_045255\03_HaiTacTiHonServer" "HaiTacTiHonServer" /E /MT:32`
   * Khôi phục Database: `c:\xampp\mysql\bin\mysql.exe -u root haitacz < "_BACKUP_UI_REWORK\20260917_045255\04_SQL\haitacz_full_dump_20260917_045255.sql"`

---

## 19. RISK LIST (QUẢN TRỊ RỦI RO)

| Rủi ro | Mức độ | Biện pháp Phòng ngừa & Xử lý |
| :--- | :---: | :--- |
| **Tràn bộ nhớ RAM trên máy J2ME** | Cao | Dùng hình học vector `ModernUI`, không thêm ảnh lớn, cấm tuyệt đối `new` trong hàm `paint()`. |
| **Lệch vùng chạm trên tỉ lệ màn hình lạ** | Trung bình | Neo tọa độ touch trực tiếp vào tọa độ vẽ của container, không dùng số hardcode. |
| **Sai lệch giao thức mạng nhị phân** | Cao | Giữ nguyên 100% Server và toàn bộ hàm đọc packet trong `ReadMessenge.java`. |
| **Mất chức năng trên màn hình nhỏ** | Cao | Chế độ Compact giữ nguyên 100% luồng logic của `TabScreen` và softkeys cũ. |

---

## 20. FILE-BY-FILE MODIFICATION PLAN (KẾ HOẠCH CHI TIẾT TỪNG FILE)

### 20.1 Nhóm Nền tảng & Điều phối Responsive (`GameCanvas`, `MotherCanvas`)
1. **`ProjectJ2me129/src/GameCanvas.java` & `ProjectUnity129/Assets/Scripts/Assembly-CSharp/GameCanvas.cs`:**
   * **Class:** `GameCanvas`
   * **Method:** `isCompactMode()`
   * **Current Logic:** Hardcoded `return false;`
   * **Problem:** Luôn ép dùng 1 chế độ, không phân biệt được màn hình nhỏ cổ điển và màn hình lớn hiện đại.
   * **Change:**
     ```java
     public static boolean isCompactMode() {
         return MotherCanvas.w < 320 || MotherCanvas.h < 260;
     }
     ```
   * **Why:** Cho phép tự động chuyển đổi mượt mà giữa Chế độ A (Cổ điển) và Chế độ B (Hiện đại).
   * **Risk:** Thấp.
   * **Test:** Chạy trên độ phân giải 240x320 $\rightarrow$ `isCompactMode() == true`; chạy trên 800x480 $\rightarrow$ `false`.

2. **`ProjectJ2me129/src/MotherCanvas.java` & `ProjectUnity129/Assets/Scripts/Assembly-CSharp/MotherCanvas.cs`:**
   * **Class:** `MotherCanvas`
   * **Method:** `setZoomLevel(int zl)` / `refreshDisplay()`
   * **Current Logic:** Chia tích lũy `MotherCanvas.w` làm co rút màn hình nếu gọi nhiều lần.
   * **Problem:** Lỗi lệch kích thước khi xoay màn hình hoặc đổi độ phân giải.
   * **Change:** Lưu kích thước vật lý gốc `physicW = super.getWidth(); physicH = super.getHeight();` và luôn tính `w = physicW / zoomLevel`.
   * **Why:** Giữ kích thước canvas logic ổn định tuyệt đối.
   * **Risk:** Thấp.
   * **Test:** Thay đổi kích thước cửa sổ liên tục không bị co cụm UI.

### 20.2 Nhóm Design System & Component Dùng Chung (`ModernUI`)
3. **`ProjectJ2me129/src/ModernUI.java` & `ProjectUnity129/Assets/Scripts/Assembly-CSharp/ModernUI.cs`:**
   * **Class:** `ModernUI`
   * **Method:** Bổ sung `paintModernDialogFrame(...)`, `paintModernTooltip(...)`, `paintModernTabHeader(...)`
   * **Current Logic:** Đã có nền tảng card, button, progress bar, slot.
   * **Problem:** Chưa có bộ hàm chuẩn hóa cho khung Dialog và Tooltip nổi.
   * **Change:** Viết hoàn chỉnh các hàm dựng khung cửa sổ hiện đại, hỗ trợ hiệu ứng bóng mờ (shadow), viền vàng kim loại và viền phân cấp độ hiếm.
   * **Why:** Đem lại nhận diện hình ảnh sang trọng, đồng bộ trên toàn bộ game.
   * **Risk:** Không có rủi ro logic vì chỉ là các hàm vẽ đồ họa thuần túy.
   * **Test:** Render thử nghiệm trên canvas hiển thị sắc nét.

### 20.3 Nhóm Hộp thoại & Thông báo (`MsgDialog`, `InputDialog`)
4. **`ProjectJ2me129/src/MsgDialog.java` & `ProjectUnity129/Assets/Scripts/Assembly-CSharp/MsgDialog.cs`:**
   * **Class:** `MsgDialog`
   * **Method:** `setinfo(...)`, `paint(...)`
   * **Current Logic:** `wDia = MotherCanvas.w - 30; if (wDia > 200) wDia = 200;`
   * **Problem:** Hộp thoại bị cố định tối đa 200px khiến chữ bị cắt vụn trên màn hình lớn.
   * **Change:**
     ```java
     if (GameCanvas.isCompactMode()) {
         super.wDia = Math.min(220, MotherCanvas.w - 20);
     } else {
         super.wDia = Math.min(420, MotherCanvas.w - 60);
     }
     super.hDia = ...; // Tính linh hoạt theo số dòng text wrap
     ```
     Tích hợp `ModernUI.paintModernWindow` khi ở chế độ màn hình lớn.
   * **Why:** Hộp thoại cân đối, thoáng đãng, dễ đọc trên mọi thiết bị.
   * **Risk:** Cần đảm bảo hàm wrap text (`mFont.splitFontArray`) chạy đúng với `wDia` mới.
   * **Test:** Mở hộp thoại thông báo dài trên cả 2 độ phân giải.

5. **`ProjectJ2me129/src/InputDialog.java` & `ProjectUnity129/Assets/Scripts/Assembly-CSharp/InputDialog.cs`:**
   * **Class:** `InputDialog`
   * **Method:** `init(...)`, `paint(...)`
   * **Current Logic:** `wDia <= 200`, ô nhập text ngắn cụn.
   * **Change:** Mở rộng `wDia` lên tối đa 380px trên màn hình lớn, căn giữa form nhập liệu, viền card bo góc.
   * **Why:** Thao tác nhập văn bản và mật khẩu thoải mái trên thiết bị cảm ứng.
   * **Risk:** Thấp.
   * **Test:** Nhập text trên mobile và PC.

### 20.4 Nhóm Cửa hàng, Túi đồ & Sửa Lỗi Lệch Chạm (`MainTabShop`, `TabInventory`, `DualTabScreen`)
6. **`ProjectJ2me129/src/MainTabShop.java` & `ProjectUnity129/Assets/Scripts/Assembly-CSharp/MainTabShop.cs`:**
   * **Class:** `MainTabShop`
   * **Method:** `updatePointer()`, `paintInfo(...)`
   * **Current Logic:** Không chặn va chạm của Tooltip Info; công thức tính index ô bị tràn cột mép phải.
   * **Problem:** Gây lỗi nghiêm trọng: chạm vào bảng thông tin vật phẩm lại bị click xuyên thấu vào ô đồ bên dưới; chạm mép phải nhảy sang hàng sau.
   * **Change:**
     1. Chặn touch khi bảng Info đang mở:
        ```java
        if (this.isShowInfo && GameCanvas.isPoint(this.xInfo, this.yInfo, this.wInfo, this.hInfo)) {
            // Nuốt sự kiện touch, không cho xuyên thấu xuống lưới đồ bên dưới
            if (GameCanvas.isPointerSelect) {
                // Xử lý nút bấm trên bảng Info nếu có
            }
            return;
        }
        ```
     2. Thêm hàm kẹp cận trên cho cột:
        ```java
        int col = (GameCanvas.px - this.xCurBegin) / MainTab.wItem;
        if (col >= this.maxNumItemW) col = this.maxNumItemW - 1;
        ```
     3. Khôi phục hàm gán vị trí nút bấm `AO()` tại dòng 348 để nút "Mua" trên popup hoạt động chính xác.
   * **Why:** Triệt tiêu hoàn toàn 100% lỗi lệch touch và bấm nhầm trong Shop / Kho.
   * **Risk:** Cần kiểm tra kỹ logic cuộn danh sách.
   * **Test:** Bấm vào vật phẩm trong shop, thử bấm các nút trên popup xem có bị click xuyên thấu hay không.

7. **`ProjectJ2me129/src/DualTabScreen.java` & `ProjectUnity129/Assets/Scripts/Assembly-CSharp/DualTabScreen.cs`:**
   * **Class:** `DualTabScreen`
   * **Method:** `init()`, `paint()`, `updatePointer()`
   * **Current Logic:** Màn hình 2 cột hiện đại nhưng một số bảng phụ chưa responsive đầy đủ theo breakpoint.
   * **Change:**
     - Thiết lập làm giao diện người chơi mặc định khi `!GameCanvas.isCompactMode()`.
     - Phân chia tỷ lệ cân đối: Cột trái (Trang bị & Thuộc tính - 38% chiều rộng), Cột phải (Túi đồ 126 ô & Bộ lọc tab - 62% chiều rộng).
     - Kết nối hiển thị nhịp nhàng với dữ liệu 16 trang bị từ Server (`cmd = 19`).
   * **Why:** Mang lại trải nghiệm UI Master-Detail hiện đại tương đương các game mobile RPG hàng đầu.
   * **Risk:** Thấp, mã nguồn `DualTabScreen` đã được chứng minh chạy mượt trong dự án.
   * **Test:** Mở túi đồ trên màn hình lớn, thử chuyển các tab Trang bị, Tiềm năng, Kỹ năng, Nhiệm vụ, Danh hiệu.

### 20.5 Nhóm Chat, Hòm Thư & Tối ưu Hiệu năng (`ChatTabScreen`)
8. **`ProjectJ2me129/src/ChatTabScreen.java` & `ProjectUnity129/Assets/Scripts/Assembly-CSharp/ChatTabScreen.cs`:**
   * **Class:** `ChatTabScreen`
   * **Method:** `paintMailListView(...)`
   * **Current Logic:** Gọi `getPageMailItems()` tạo `new mVector()` liên tục trong mỗi frame vẽ.
   * **Problem:** Tạo ra 90 đối tượng Vector/giây, kích hoạt Garbage Collector liên tục làm khựng hình, drop FPS.
   * **Change:** Sử dụng mảng/vector tiền cấp phát (`cachedMailList`), chỉ cập nhật và lọc lại danh sách khi có thư mới hoặc khi người chơi chuyển trang/tab.
   * **Why:** Đạt chuẩn Zero-Allocation trong Paint Loop, bảo đảm game chạy 60 FPS mượt mà.
   * **Risk:** Thấp.
   * **Test:** Mở hòm thư, theo dõi bộ đếm allocations và FPS không bị drop.

### 20.6 Nhóm Main HUD & Điều khiển Trận đấu (`Interface_Game`)
9. **`ProjectJ2me129/src/Interface_Game.java` & `ProjectUnity129/Assets/Scripts/Assembly-CSharp/Interface_Game.cs`:**
   * **Class:** `Interface_Game`
   * **Method:** `paintInfoPlayer(...)`, `paintInfoFocus(...)`
   * **Current Logic:** Cố định thanh HP/MP dài 66px, avatar 88x45px.
   * **Change:**
     - Khi ở chế độ `isCompactMode()`: Giữ nguyên 100% tọa độ và thanh máu cũ.
     - Khi ở chế độ màn hình lớn: Chuyển sang `ModernUI.paintModernPlayerHUD`: thanh HP/MP mở rộng dài 120–160px, hiển thị rõ số máu thực tế và phần trăm, avatar bo góc có viền mạ vàng, hiệu ứng vệt máu mất dần (`trailPlayerHp`).
   * **Why:** Thanh máu và trạng thái nhân vật rõ ràng, trực quan, chuyên nghiệp khi giao chiến.
   * **Risk:** Thấp.
   * **Test:** Đánh quái và bị quái đánh thử nghiệm trên cả màn hình nhỏ và lớn.

---

*Toàn bộ kế hoạch trên đã được thẩm định đồng bộ bởi toàn bộ đội ngũ kiến trúc sư. Tuyệt đối không thay đổi gameplay logic, không sửa Server, không phá vỡ giao diện cũ trên màn hình nhỏ.*
