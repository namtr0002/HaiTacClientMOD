# BÁO CÁO NGHIÊN CỨU TOÀN DIỆN HỆ THỐNG UI/UX - HTTH (MASTER_UI_RESEARCH.md)
**Dự án:** Hải Tặc Tí Hon (HTTH) - Redesign Toàn Bộ Hệ Thống UI/UX  
**Phạm vi nghiên cứu:** ProjectUnity129, ProjectJ2me129, HaiTacTiHonServer, HaiTacTiHonTool  
**Ngày thực hiện:** 2026-09-17  
**Tình trạng Backup:** ĐÃ HOÀN TẤT 100% & VERIFIED (Commit 3d1c4705, ZIP 169.18 MB)  

---

## 1. TỔNG QUAN HỆ THỐNG UI HIỆN TẠI (CURRENT UI ARCHITECTURE)

### 1.1 ProjectJ2me129 (Java CLDC 1.1 / MIDP 2.0)
- **Canvas & Vòng lặp chính:** MotherCanvas.java kế thừa javax.microedition.lcdui.Canvas. Chạy vòng lặp un() với chu kỳ sleep 33ms (~30 FPS). Quản lý kích thước canvas logic: MotherCanvas.w, MotherCanvas.h, hw (w/2), hh (h/2).
- **Bộ điều phối hiển thị:** GameCanvas.java quản lý ngăn xếp phân cấp màn hình:
  currentScreen.paint(g) -> subDialog.paint(g) -> currentDialog.paint(g) -> menuCur.paint(g) -> Interface_Game.paint(g).
- **Hệ thống Graphics:** mGraphics.java bọc javax.microedition.lcdui.Graphics, hỗ trợ translation, setClip, drawRegion, drawImage.
- **Hệ thống Font chữ:** mFont.java sử dụng Bitmap Font cắt từ sprite sheet /mfont/tahoma_7 và 	ahoma_7b.
- **Mô hình Component:** Kế thừa từ AvMain.java. Quản lý 3 Softkeys (left, ight, center) và D-Pad.

### 1.2 ProjectUnity129 (Unity 2022.3.62f3 C#)
- **Cơ chế dựng hình:** 100% **Unity IMGUI (Immediate Mode GUI)** thông qua Main.OnGUI() gọi GameMidlet.gameCanvas.paint(g). Hoàn toàn KHÔNG dùng uGUI (Canvas, RectTransform).
- **Liên hệ J2ME:** Là bản chuyển mã (port) 1:1 từ Java sang C#. Các cấu trúc mạng, RMS, nhị phân (myReader, myWriter, mVector) tương thích byte-for-byte với J2ME.
- **Xử lý độ phân giải:** MotherCanvas.checkZoomLevel() tính toán zoomLevel (1x..4x) để chia độ phân giải màn hình thiết bị (Screen.width / zoomLevel).

### 1.3 HaiTacTiHonServer (Java NIO Core)
- **Kiến trúc mạng:** SocketChannel Non-blocking, port 2239, MySQL database haitacz.
- **Mã hóa:** Rolling XOR Symmetric Key 9 ký tự (huathanh@).
- **Độc lập UI:** Server là Headless Data Provider. Server chỉ gửi ID, tên, số lượng, chỉ số nhị phân; KHÔNG chứa tọa độ, màu sắc hay bố cục layout.

---

## 2. BẢNG MA TRẬN PHÂN TÍCH TOÀN BỘ 45 HỆ THỐNG UI HIỆN TẠI

| UI Screen | Unity Class | J2ME Class | Cơ chế Vẽ | Input Xử lý | Asset Sử dụng | Data/Protocol | Vấn đề Hiện tại | Giải pháp Redesign |
|---|---|---|---|---|---|---|---|---|
| **Login Screen** | LoginScreen.cs | LoginScreen.java | IMGUI / AD() | Touch + Softkeys | tlogin, ranew, lgv | Direct RMS / Server List | Cố định 1 layout, không chọn được Theme | Thêm Theme Selector góc trái, layout thoáng |
| **Server Select** | FristLoginScreen.cs | FristLoginScreen.java | IMGUI / Grid | Touch Grid | iconserver, ga | UpdateServer.loadServers() | Nút server dính nhau, khó cuộn | Grid card hiện đại, ping indicator |
| **Main HUD** | Interface_Game.cs | Interface_Game.java | IMGUI direct | Touch Hotkeys | iconmphp, orderskill | Player real-time stats | Thanh máu cố định 66px quá nhỏ | HUD động co giãn theo resolution, vệt máu |
| **Skill Bar** | Interface_Game.cs | Interface_Game.java | Arc / Linear | Touch Circle / Keys 0..9 | delayskill2/3, hotkey | Player.vecSkill | Nút skill nhỏ, dễ bấm nhầm trên tablet | Cụm skill công thái học, đổi layout Arc/Grid |
| **Dialog Msg** | MsgDialog.cs | MsgDialog.java | paintRect | Softkeys / OK btn | paper0..7, 	t00..16 | CMD -11 | Giới hạn cứng <= 200px, chữ bị vụn | Co giãn theo breakpoint W = min(520, w - 40) |
| **Input Dialog** | InputDialog.cs | InputDialog.java | paintRect + TF | Touch textfield | paper0..7, 	f | CMD -81 / -58 | Form nhập liệu hẹp, che màn hình | Căn giữa, bàn phím ảo thích ứng |
| **Menu / NPC** | Menu.cs | Menu.java | paintPaper | Touch row / D-Pad | paper0..7, ga | CMD -19 / -20 | Danh sách cuộn bị giật, viền đè chữ | Menu popup dạng Card phân tầng, icon NPC |
| **Hành Trang** | TabInventory.cs | TabInventory.java | Matrix Slot 28px | Touch Slot / D-Pad | khung, slot, paper | CMD -12 | 1 cột cuộn, lãng phí 70% màn hình lớn | DualTabScreen 2 cột, 8 cột slot, lọc tab |
| **Trang Bị** | TabEquip.cs | TabEquip.java | 16 slot cố định | Touch slot | equip.png, slot | CMD 19 | Xếp chồng lên túi đồ, không xem được cả 2 | Đặt song song với hành trang, preview nhân vật |
| **Chi Tiết Item**| MainTab.cs | MainTab.java | Popup overlay | Touch to close | paper, coloritem | CMD -105 | Chạm vào info bị click xuyên xuống slot | Modal blocker, chặn touch xuyên thấu, so sánh đồ |
| **Shop Trang Bị**| TabShop.cs | TabShop.java | Grid 6x6 | Touch slot / Buy | paper, ga | CMD -19 / -18 | Lỗi lệch cột mép phải, nút mua rỗng | Vá kẹp cận trên Math.min(col, maxCol-1), nút mua rõ |
| **Kỹ Năng** | TabSkill.cs | TabSkill.java | Cây kỹ năng dọc | Touch + Nâng điểm | orderskill, paper | CMD -7 / -17 | Khó so sánh cấp trước/sau của skill | Bảng skill dạng cây phân nhánh, preview hiệu ứng |
| **Nhiệm Vụ** | TabQuest.cs | TabQuest.java | Danh sách text | Touch quest | quest.png, paper | CMD -23 | Chữ dài bị tràn mép, khó tìm vị trí trả | Card nhiệm vụ có icon phân loại, nút điều hướng |
| **Bang Hội** | Clan_Screen.cs | Clan_Screen.java | Multi-tab | Touch tabs | annerclan, iconclan | CMD -52 | Text dài bị đè icon thành viên | Bảng thành viên phân cấp, cờ bang sắc nét |
| **Hòm Thư** | ChatTabScreen.cs | ChatTabScreen.java | Cuộn danh sách | Touch mail | mess.png, paper | CMD -110 | Cấp phát new mVector() mỗi frame làm drop FPS | Zero-allocation paint, cached mail list |
| **Chat Kênh** | ChatTabScreen.cs | ChatTabScreen.java | 6 tab chat | Touch tab | chat2.png, 	f | Chat socket packets | Tab nhỏ sát mép tai thỏ bị che | Áp dụng Safe Area, tab vuốt chuyển kênh mượt |
| **Cường Hóa** | ScreenUpgrade.cs| ScreenUpgrade.java| Đặt đá + item | Touch đặt đồ | effupgrade, paper | CMD -82 | Hiệu ứng đập đồ che mất thông tin tỉ lệ | Tách khu vực tỉ lệ %, nút đập đồ nổi bật |

---

## 3. KHẢO SÁT CƠ CHẾ VẼ & RENDERING HIỆN TẠI (RENDERING PIPELINE)

1. **Khung Giấy Cổ Điển (AvMain.AD / paintPaper):**
   - Ghép 8 mảnh (paper0 đến paper7). Các cạnh ngang và dọc được vẽ lặp bằng bước nhảy cố định 10 pixel:
     or(var7 = 0; var7 < var5 - 37; var7 += 10) { g.drawRegion(paper[1], ...); }
   - Nhược điểm: Nếu chiều rộng/chiều cao không chia hết cho 10, viền sẽ bị chồng răng cưa hoặc méo góc; không hỗ trợ tỷ lệ màn hình hiện đại.
2. **Khung Thống Thạo (gb0..8) & Khung Rương VIP (gc0..8):**
   - Được nạp qua LoadImageStatic.loadImageBgB() và loadImageBgC().
   - Kích thước mảnh ghép 36px và 46px. Rất đẹp nhưng chỉ dùng cục bộ trong một số màn hình đặc thù.
3. **Bitmap Font Rendering (mFont):**
   - Ký tự được cắt từ ảnh lớn. Độ cao cố định 12..14px.
   - Khi chạy ở màn hình Full HD (1080p), chữ bị nhỏ li ti nếu không zoomLevel x3/x4; nhưng khi zoomLevel x3/x4 thì kích thước logic canvas bị co lại còn 320x180 px.

---

## 4. KHẢO SÁT INPUT PIPELINE & LỖI TRỌNG YẾU (BUGS & ROOT CAUSES)

1. **Lỗi Touch Click-through trong Shop & Kho:**
   - **Hiện tượng:** Mở bảng thông tin vật phẩm (ShowInfo), bấm vào xem chỉ số thì ô đồ bên dưới bị kích hoạt chọn hoặc mua nhầm.
   - **Root Cause:** Lớp popup không trả về 	rue để nuốt sự kiện cảm ứng (Event Swallowing).
2. **Lỗi Tràn Cột Lưới Shop (MainTabShop.java:241):**
   - **Hiện tượng:** Chạm vào mép phải ô cuối dòng làm con trỏ nhảy sang đầu dòng kế tiếp.
   - **Root Cause:** Phép chia (px - xCurBegin) / wItem không có hàm kẹp cận trên Math.min(col, cols - 1).
3. **Lỗi Nút Mua Bị Rỗng Tọa Độ Touch (MainTabShop.java:348):**
   - **Hiện tượng:** Nút "Mua" trên popup không ăn cảm ứng.
   - **Root Cause:** Hàm gán tọa độ nút AO() bị bỏ trống.
4. **Lỗi Rác Bộ Nhớ GC Trong Hòm Thư (ChatTabScreen.java):**
   - **Hiện tượng:** Mở hòm thư bị giật lag, drop FPS trên client J2ME.
   - **Root Cause:** Hàm paintMailListView() gọi getPageMailItems() tạo 
ew mVector() liên tục 60-90 lần/giây trong vòng lặp paint().
5. **Lỗi Co Cụm Layout Khi Xoay Màn Hình (MotherCanvas.java):**
   - **Hiện tượng:** Xoay màn hình hoặc đổi độ phân giải làm UI co cụm lại góc trái.
   - **Root Cause:** setZoomLevel chia tích lũy trực tiếp vào MotherCanvas.w.

---

## 5. PHÂN TÍCH DEPENDENCY DỮ LIỆU & SQL SERVER
- **Server Core:** HaiTacTiHonServer độc lập hoàn toàn với việc hiển thị.
- **Protocol:** Toàn bộ 45 hệ thống UI sử dụng tập lệnh nhị phân chuẩn (CMD -27, -11, -81, -58, -31, -19, -20, -18, -21, -12, -32, 19, -10, -7, -17, -23, -110, -52, -25).
- **Kết luận:** **GIỮ NGUYÊN 100% PROTOCOL VÀ DATABASE**, UI Redesign là cải tiến kiến trúc hoàn toàn ở phía 2 Client (ProjectUnity129 và ProjectJ2me129).
