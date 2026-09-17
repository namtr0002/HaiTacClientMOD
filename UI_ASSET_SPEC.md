# ĐẶC TẢ TÀI NGUYÊN ĐỒ HỌA & ATLAS UI - HTTH (UI_ASSET_SPEC.md)
**Dự án:** Hải Tặc Tí Hon (HTTH)  
**Tài liệu:** UI_ASSET_SPEC.md  

---

## 1. QUY CHUẨN TÀI NGUYÊN (ASSET SPECIFICATION)
- **Định dạng:** PNG 32-bit (hỗ trợ kênh Alpha trong suốt chuẩn).
- **Phân giải đa nền tảng:**
  * x1 (Baseline J2ME & Low Res): Kích thước chuẩn Pixel-Art 100%.
  * x2 (HD Smartphone / 720p): Tỉ lệ scale 200% Nearest Neighbor sắc nét.
  * x3 / x4 (Full HD / 2K Retina): Tỉ lệ scale 300% - 400% chuẩn xác.

---

## 2. BỘ ASSET CHUẨN CHO TỪNG THEME (THEME ASSET ATLAS)

Mỗi theme sở hữu một bộ nhận diện đồ họa đồng bộ bao gồm:
1. **Window Frame (9-slice):**
   - 4 góc (TopLeft, TopRight, BotLeft, BotRight)
   - 4 cạnh lặp (Top, Bot, Left, Right)
   - 1 ảnh nền tâm (Center Fill Tile)
2. **Button Frame:**
   - Trạng thái Normal (Bình thường)
   - Trạng thái Hover / Focus (Khi chọn hoặc trỏ chuột)
   - Trạng thái Pressed (Khi ngón tay nhấn giữ)
   - Trạng thái Disabled (Vô hiệu hóa)
3. **Item Slot Frame (28x28px & 32x32px):**
   - 6 viền phẩm chất: Trắng, Xanh lá, Xanh lam, Tím, Cam, Đỏ Mythic.
   - Hiệu ứng viền phát sáng lấp lánh khi được chọn (isSelected).
4. **Gauge Bar (HP/MP/EXP):**
   - Khung viền thanh máu (Gauge Frame)
   - Thanh ruột gradient (Fill Bar)
   - Vệt máu mất dần (Damage Trail Bar)
5. **Icon Tiện ích:**
   - Nút Đóng (xclose.png / closetab.png)
   - Nút Mở rộng (cmd_mo.png)
   - Badge thông báo đỏ (drawBadge)
   - Icon phân loại Tab (Kiếm, Dược, Đá, Cuộn, Sao)

---

## 3. BẢO ĐẢM TƯƠNG THÍCH ATLAS GIỮA J2ME VÀ UNITY
- Tất cả FrameImage phải khai báo chính xác 
Frame, rameWidth, rameHeight.
- Tọa độ cắt không được sai lệch dù chỉ 1 pixel để tránh hiện tượng "chảy viền" (texture bleeding) trên màn hình độ phân giải cao.
