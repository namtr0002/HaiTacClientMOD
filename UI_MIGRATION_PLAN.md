# KẾ HOẠCH MIGRATION THEO TỪNG MILESTONE - HTTH (UI_MIGRATION_PLAN.md)
**Dự án:** Hải Tặc Tí Hon (HTTH)  
**Tài liệu:** UI_MIGRATION_PLAN.md  

---

## 1. NGUYÊN TẮC MIGRATION AN TOÀN
- **Không thay đổi đồng loạt:** Triển khai theo từng nhóm màn hình độc lập.
- **Quy trình chuẩn cho mỗi Milestone:**
  IMPLEMENT CODE -> BUILD J2ME -> BUILD UNITY -> TEST RUNTIME -> FIX BUGS -> GIT COMMIT.
- Nếu có bất kỳ lỗi không tương thích nào xảy ra ở một Milestone: Ngay lập tức cô lập lỗi, không để lây lan sang các hệ thống khác.

---

## 2. LỘ TRÌNH 10 MILESTONE CHI TIẾT

| Milestone | Phân Hệ Triển Khai | Phạm Vi Mã Nguồn | Tiêu Chuẩn Nghiệm Thu |
|---|---|---|---|
| **M1** | **UI Core Framework & Theme Registry** | UIThemeManager, UITheme, UILayoutEngine, UIModalManager | Biên dịch 0 lỗi cả Java & C#, đăng ký đủ 10 theme |
| **M2** | **Login Theme Selector** | LoginScreen.java, LoginScreen.cs, FristLoginScreen | Nút chọn theme tại góc trái hoạt động, đổi theme tức thì và lưu RMS |
| **M3** | **UI Showcase Test Screen** | UIShowcaseScreen.java, UIShowcaseScreen.cs | Hiển thị trọn vẹn mọi component (Window, Button, Slot, Gauges) để kiểm thử visual |
| **M4** | **Hộp thoại & Thông báo** | MsgDialog, InputDialog, MsgShowGift, GameCanvas | Co giãn linh hoạt theo breakpoint, chữ không bị bẻ vụn |
| **M5** | **Hệ thống Menu NPC & Lựa chọn** | Menu.java, Menu.cs, QuickMenu | Menu ngữ cảnh hiển thị dạng card đẹp mắt, không giật lag |
| **M6** | **Cửa hàng Shop & Túi đồ** | MainTabShop, TabInventory, DualTabScreen | Sửa triệt để lỗi lệch cột và click xuyên thấu; kích hoạt 2 cột song song trên màn hình lớn |
| **M7** | **Bảng Nhân vật, Kỹ năng & Nhiệm vụ** | TabEquip, TabInfo, TabSkill, TabQuest | Kết nối chuẩn xác 16 trang bị và dữ liệu tiềm năng từ server |
| **M8** | **Chat, Hòm thư & Bang hội** | ChatTabScreen, Clan_Screen | Đạt chuẩn Zero-Allocation trong vòng lặp vẽ, tối ưu 100% GC |
| **M9** | **Main Battle HUD & Điều khiển** | Interface_Game.java, Interface_Game.cs | Thanh HP/MP/EXP co giãn, vệt máu mất dần, cụm phím kỹ năng công thái học |
| **M10**| **Kiểm thử Hồi quy Toàn diện & Tối ưu** | Toàn bộ 2 Client | Chạy mượt mà trên cả màn hình nhỏ 240x320 và màn hình lớn Full HD |
