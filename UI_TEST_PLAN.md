# KẾ HOẠCH KIỂM THỬ TOÀN DIỆN (UI_TEST_PLAN.md)
**Dự án:** Hải Tặc Tí Hon (HTTH)  
**Tài liệu:** UI_TEST_PLAN.md  

---

## 1. KIỂM THỬ BIÊN DỊCH (BUILD VERIFICATION)
1. **Client J2ME:**
   - Command: 	est_compile.bat trong ProjectJ2me129.
   - Tiêu chuẩn: Exit Code 0, 0 warning, 0 error.
2. **Client Unity:**
   - Compiler: Roslyn C# Compiler / Unity Editor CLI.
   - Tiêu chuẩn: Toàn bộ script trong ProjectUnity129/Assets/Scripts/Assembly-CSharp biên dịch sạch 100%.
3. **Server Core:**
   - Command: mvn clean package -DskipTests trong HaiTacTiHonServer.
   - Tiêu chuẩn: BUILD SUCCESS.

---

## 2. KIỂM THỬ TƯƠNG TÁCH & PHÒNG CHỐNG LỖI (INPUT & BUG TESTS)
1. **Test Chặn Click Xuyên Thấu (Modal Click-Through Test):**
   - Mở cửa hàng NPC Johny -> Mở popup chi tiết trang bị -> Chạm vào vùng thông tin -> Xác nhận ô trang bị nằm ngầm bên dưới KHÔNG bị chọn nhầm.
2. **Test Lưới Ô Shop & Túi Đồ:**
   - Chạm vào từng ô từ cột 0 đến cột cuối cùng -> Xác nhận vùng chọn (Selection Box) trùng khớp hoàn toàn với vị trí ngón tay chạm.
3. **Test Phím Cứng J2ME:**
   - Sử dụng phím mũi tên 2, 4, 6, 8 và phím 5 để duyệt menu và mua bán đồ -> Xác nhận di chuyển mượt mà.
4. **Test Nhập Liệu Bàn Phím:**
   - Mở InputDialog nhập tên nhân vật, số lượng mua, giftcode -> Xác nhận hiển thị rõ ràng, không bị che khuất bàn phím ảo.

---

## 3. KIỂM THỬ ĐỘ PHÂN GIẢI & ĐA MÀN HÌNH (RESPONSIVE TESTS)
1. **Độ phân giải Màn hình Nhỏ ( \times 320$,  \times 240$):**
   - Xác nhận kích hoạt chế độ Compact Mode.
   - Giao diện 1 cột truyền thống hiển thị trọn vẹn, không tràn chữ, không đè nút.
2. **Độ phân giải Màn hình Lớn ( \times 720$,  \times 1080$, Tablet, PC):**
   - Xác nhận tự động kích hoạt DualTabScreen (Bố cục 2 cột song song).
   - Tận dụng tối đa diện tích màn hình, cột trang bị bên trái và túi đồ bên phải hiển thị hài hòa.
3. **Màn hình Dài Tỉ lệ .5:9$, $ (Tai thỏ / Đục lỗ):**
   - Xác nhận tự động cộng lề an toàn Safe Area, không bị camera che mất nút điều hướng.

---

## 4. KIỂM THỬ HIỆU NĂNG & RÁC BỘ NHỚ (PERFORMANCE & GC TESTS)
1. **Kiểm tra Garbage Collector J2ME:**
   - Theo dõi số lần kích hoạt GC và lượng RAM giải phóng khi mở hòm thư liên tục 60 giây.
   - Tiêu chuẩn: 0 đối tượng cấp phát mới trong vòng lặp paint().
2. **Kiểm tra FPS Unity:**
   - Tiêu chuẩn: Giữ vững 60 FPS ổn định trên thiết bị di động tầm trung, không bị drop khung hình khi mở giao diện lớn.
