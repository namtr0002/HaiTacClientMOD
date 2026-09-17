# THIẾT KẾ HỆ THỐNG DỰNG HÌNH ĐỒ HỌA MỚI - HTTH (UI_RENDER_SYSTEM.md)
**Dự án:** Hải Tặc Tí Hon (HTTH)  
**Tài liệu:** UI_RENDER_SYSTEM.md  

---

## 1. NGUYÊN TẮC QUẢN TRỊ BỘ NHỚ & HIỆU NĂNG (PERFORMANCE & GC)
1. **Quy tắc Zero-Allocation trong vòng lặp Paint:**
   - Tuyệt đối cấm khởi tạo đối tượng động (
ew mVector(), 
ew StringBuffer(), 
ew Rect()) bên trong bất kỳ hàm paint(), paintInfo(), paintTab() nào.
   - Sử dụng các biến bộ đệm tĩnh tái sử dụng (eusableVector, eusableStringBuilder).
   - Kết quả: Triệt tiêu 100% hiện tượng Garbage Collection Spike gây khựng hình (micro-stuttering) trên máy J2ME và mobile.
2. **Quản lý An toàn Khung Cắt (Clip Viewport Safety):**
   - Mọi hàm vẽ danh sách cuộn (ListNew, Scroll) sau khi g.setClip(x, y, w, h) BẮT BUỘC phải gọi GameCanvas.resetTrans(g) để phục hồi viewport toàn màn hình.
   - Ngăn chặn triệt để lỗi biến mất các thành phần UI vẽ sau danh sách cuộn.

---

## 2. KIẾN TRÚC RENDER THEO 2 NỀN TẢNG (DUAL PLATFORM BACKEND)

### 2.1 Backend J2ME (Java CLDC 1.1)
- Dựa trên mGraphics.java bọc javax.microedition.lcdui.Graphics.
- Kỹ thuật vẽ khung hình học:
  * Kết hợp thuật toán illGradientRect 6 bước chuyển sắc.
  * Tận dụng tối đa bộ ảnh 9-slice đã nạp trong RAM (paper0..7, papern0..7, gb0..8, gc0..8).
  * Không nạp texture nặng quá 256KB vào heap memory.

### 2.2 Backend Unity (C# IMGUI 2022.3)
- Dựa trên mGraphics.cs gọi Graphics.DrawTexture và GUI.DrawTexture.
- Tối ưu hóa Draw Call:
  * Tạo các texture đơn sắc và gradient 1x64px cached tĩnh trong mGraphics.cachedTextures.
  * Tránh gọi GUI.color liên tục giữa các lệnh vẽ; gom nhóm (batching) các lệnh vẽ cùng màu sắc.
  * Hỗ trợ bộ phóng to zoomLevel chuẩn xác để điểm ảnh pixel art sắc nét tuyệt đối (Point/Nearest Neighbor filtering), không bị mờ nhòe bilinear.

---

## 3. THUẬT TOÁN DỰNG HÌNH VECTOR CHUYỂN SẮC CHUẨN XÁC
`java
public static void fillGradientRect(mGraphics g, int x, int y, int w, int h, int topColor, int botColor) {
    if (w <= 0 || h <= 0) return;
    int steps = (h < 6) ? h : 6;
    if (steps <= 1) {
        g.setColor(topColor);
        g.fillRect(x, y, w, h);
        return;
    }
    int r1 = (topColor >> 16) & 0xFF, g1 = (topColor >> 8) & 0xFF, b1 = topColor & 0xFF;
    int r2 = (botColor >> 16) & 0xFF, g2 = (botColor >> 8) & 0xFF, b2 = botColor & 0xFF;
    int stepH = h / steps;
    int remH = h % steps;
    int curY = y;
    for (int i = 0; i < steps; i++) {
        int r = r1 + ((r2 - r1) * i) / (steps - 1);
        int gg = g1 + ((g2 - g1) * i) / (steps - 1);
        int b = b1 + ((b2 - b1) * i) / (steps - 1);
        g.setColor((r << 16) | (gg << 8) | b);
        int curHeight = stepH + (i == steps - 1 ? remH : 0);
        g.fillRect(x, curY, w, curHeight);
        curY += curHeight;
    }
}
`

---

## 4. HỆ THỐNG HIỂN THỊ CHỮ (FONT RENDERING SYSTEM)
- **Cấu trúc phân cấp Font Tokens:**
  * FONT_TITLE: Dùng cho tiêu đề cửa sổ lớn (mFont.tahoma_7b_yellow / 	ahoma_7b_white).
  * FONT_SUB_HEADER: Dùng cho tên tab, mục thuộc tính (mFont.tahoma_7b_yellow).
  * FONT_BODY: Dùng cho mô tả, hội thoại, thông số thường (mFont.tahoma_7_white).
  * FONT_NUMBER: Dùng cho số lượng vật phẩm, cấp độ (mFont.tahoma_7b_white).
  * FONT_RARITY: 6 màu font tương ứng với 6 cấp phẩm trang bị (Trắng, Xanh lá, Xanh lam, Tím, Cam, Đỏ Mythic).
- **Thuật toán Ngắt dòng Tiếng Việt (Text Wrap):**
  * Sử dụng mFont.splitFontArray(text, maxW) có hỗ trợ ký tự có dấu UTF-8.
  * Tự động đo độ rộng thực tế trước khi in, nếu chữ vượt quá maxW sẽ tự ngắt xuống dòng và tăng chiều cao container hDia = Math.max(hDia, headerH + lines.length * lineHeight + padding).
