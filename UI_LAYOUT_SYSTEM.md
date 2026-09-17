# THIẾT KẾ HỆ THỐNG LAYOUT & RESPONSIVE ĐA ĐỘ PHÂN GIẢI - HTTH (UI_LAYOUT_SYSTEM.md)
**Dự án:** Hải Tặc Tí Hon (HTTH)  
**Tài liệu:** UI_LAYOUT_SYSTEM.md  

---

## 1. NGUYÊN TẮC THIẾT KẾ LAYOUT ENGINE MỚI
1. **Tuyệt đối không dùng tọa độ cố định tuyệt đối (Hardcoded coordinates):** Tất cả tọa độ phải được tính theo tỷ lệ màn hình thực tế và điểm neo (Anchor).
2. **Kẹp biên an toàn (Safe Clamping):** Không bao giờ để UI vượt ra ngoài màn hình (x < 0, y < 0, x + w > Screen.w, y + h > Screen.h).
3. **Kích thước mục tiêu chạm tối thiểu (Touch Target):** Mọi nút bấm cảm ứng phải có kích thước tối thiểu  \times 28$ pixel logic (tương đương 44px vật lý).

---

## 2. HỆ THỐNG NEO TỌA ĐỘ (ANCHORING SYSTEM)

`
TopLeft (TL)               TopCenter (TC)               TopRight (TR)
┌───────────────────────────────────────────────────────────────────┐
│ [Player HUD]             [Boss / Event]             [Minimap/Ping]│
│                                                                   │
│                                                                   │
│ [Quest Tracker]          [DIALOGS / WINDOWS]                      │
│                                (CENTER)                           │
│                                                                   │
│                                                                   │
│ [D-Pad / Move]                                      [Skill Bar]   │
└───────────────────────────────────────────────────────────────────┘
BottomLeft (BL)            BottomCenter (BC)         BottomRight (BR)
`

- **TopLeft:** Neo Main Battle HUD (Avatar, HP, MP, EXP, Level).
- **TopCenter:** Neo Boss Status Bar, Thanh thông báo sự kiện, Thông báo chạy ngang (Marquee).
- **TopRight:** Neo Tọa độ map, Kênh chat, Nút Cài đặt nhanh, Nút thu nhỏ.
- **Center:** Neo các cửa sổ lớn (DualTabScreen, ShopScreen, ScreenUpgrade, MsgDialog).
- **BottomLeft:** Neo Bộ chọn Theme ([ Giao diện: Gốc ▼ ]), Phím di chuyển D-Pad ảo (nếu bật touch move).
- **BottomRight:** Neo Cụm phím kỹ năng (Arc Skill cluster), phím Đổi mục tiêu, phím Tấn công cơ bản.

---

## 3. QUY TẮC PHÂN CHIA BREAKPOINT RESPONSIVE

### 3.1 Breakpoint Compact (Màn hình nhỏ)
- **Điều kiện:** .w < 360$ px HOẶC .h < 280$ px.
- **Áp dụng cho:** Điện thoại cổ điển chạy Java J2ME, Nokia s40/s60, máy Android màn hình nhỏ.
- **Bố cục:**
  * Giữ nguyên giao diện 1 cột cuộn truyền thống (TabScreen).
  * 3 phím mềm Softkeys đáy màn hình (Trái: Menu, Giữa: Chọn, Phải: Đóng).
  * Thanh HUD HP/MP giữ chuẩn dài 66px, tránh che quái vật và địa hình.

### 3.2 Breakpoint Large Screen (Màn hình lớn / Hiện đại)
- **Điều kiện:** .w \ge 360$ px VÀ .h \ge 280$ px.
- **Áp dụng cho:** Hầu hết smartphone hiện đại (720p, 1080p, 2K), máy tính bảng và bản PC.
- **Bố cục Master-Detail (DualTabScreen):**
  * Độ rộng cửa sổ: {window} = \min(680, MotherCanvas.w \times 0.90)$.
  * Chiều cao cửa sổ: {window} = \min(420, MotherCanvas.h \times 0.88)$.
  * **Cột trái (Master Panel - 38% W):**
    - Khung Avatar nhân vật chất lượng cao.
    - 16 ô trang bị đang mặc (Vũ khí, Nón, Áo, Quần, Giày, Găng, Nhẫn, Dây chuyền, Thần trang...).
    - Bảng thông tin 5 tiềm năng và 48 chỉ số chiến đấu.
  * **Cột phải (Detail Panel - 62% W):**
    - 5 Tab phân loại (Trang bị, Dược phẩm, Nguyên liệu, Nhiệm vụ, Đặc biệt).
    - Lưới hành trang mở rộng 8 cột $\times$ 4..6 hàng (32..48 ô hiển thị cùng lúc).
    - Thẻ chi tiết vật phẩm được chọn (Item Detail Card) nằm cố định ở đáy cột phải, tích hợp nút "Sử dụng", "Vứt", "Nâng cấp", "So sánh".

---

## 4. QUẢN LÝ VÙNG AN TOÀN PHẦN CỨNG (HARDWARE SAFE AREA)
- Đọc tự động từ MobileInputManager.safeLeft và MobileInputManager.safeRight trên Unity.
- Đối với màn hình có tai thỏ / nốt ruồi:
  * Tự động cộng lề an toàn: xLeft = Math.max(10, safeLeft + 4).
  * xRight = MotherCanvas.w - Math.max(10, safeRight + 4).
- Tuyệt đối không để nút Đóng (Close Button) hoặc HUD bị khuất dưới camera đục lỗ.
