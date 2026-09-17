# -*- coding: utf-8 -*-
import os

target = r"C:\DepLor\HTTH\Team\_ThanTrangAnimationSkill\README.md"

content = """# BỘ TÀI NGUYÊN HOÀN CHỈNH: 16 SKILL THẦN TRANG (HAITACTIHON)
### Thư mục trung tâm lưu trữ toàn diện: `C:\\DepLor\\HTTH\\Team\\_ThanTrangAnimationSkill`

---

## 1. Giới Thiệu & Nguyên Tắc Triển Khai
Toàn bộ lịch sử thiết kế kỹ năng Thần Trang trong suốt quá trình phát triển (bao gồm Master Artwork 1024x1024, Concept Strips, Animation Poses, Animated GIFs, Texture Spritesheets, Icons 4 cấp Zoom game và HTML Cinematic Studios) đã được tập hợp, chuẩn hóa và đóng gói **độc lập 100%** trong thư mục này.

> [!IMPORTANT]
> **Tuân thủ quy tắc cô lập tuyệt đối (Zero Side-Effects)**:
> - Không sửa đè, không thêm bớt bất kỳ tệp tin nào trong server hay client bên ngoài.
> - Toàn bộ công cụ (tools), dữ liệu (database), hình ảnh gốc (img res), icon 4 zoom (icons), texture sprite (spritesheets) và studio thử nghiệm (demos) đều nằm gọn trong thư mục này.

---

## 2. Bảng Thông Số Kỹ Thuật 16 Kỹ Năng Thần Trang (Database Schema)

| Skill ID | Icon ID | Tên Kỹ Năng | Nhân Vật / Bộ Thần Trang | Sát Thương | Phạm Vi | Hiệu Ứng Đặc Biệt | Hệ Nguyên Tố |
| :---: | :---: | :--- | :--- | :---: | :---: | :--- | :--- |
| **4001** | **201** | Hỏa Diễm Thần Quyền | Sun God Nika / Ace (Hỏa Long) | 5,000 | 220px | Thiêu Đốt biển lửa | Hỏa Thần Nika |
| **4002** | **202** | Đại Phún Hỏa Volcano | Akainu (Hải Vương) | 5,000 | 220px | Nham thạch hủy diệt | Dung Nham Magma |
| **4003** | **203** | Kỷ Băng Hà Tuyệt Đối | Aokiji (Lôi Thần) | 5,000 | 220px | Đóng băng tuyệt đối | Băng Cực Hàn |
| **4004** | **204** | Bát Xích Quỳnh Khúc Ngọc | Kizaru (Phong Ma) | 5,000 | 220px | Mưa ngọc quang tử / Chóa mắt | Thánh Quang Light |
| **4005** | **205** | Hắc Ám Thôn Phệ Vô Tận | Râu Đen (Tử Thần) | 5,000 | 220px | Lỗ đen nuốt chửng, chặn di chuyển | Hắc Ám Void |
| **4006** | **206** | 200 Triệu Volt Thần Lôi | Enel (Băng Đế) | 5,000 | 220px | Sấm sét 200M Volt / Tê liệt | Lôi Thần Raijin |
| **4007** | **207** | Hải Chấn Toái Địa Cầu | Râu Trắng (Chấn Động) | 5,000 | 220px | Vỡ không gian, 50% Choáng 3s | Sóng Chấn Động Quake |
| **4008** | **208** | ROOM Gamma Knife | Trafalgar Law (Kim Cương) | 5,000 | 220px | Phân cắt & phá hủy nội tạng | Ope Ope Room |
| **4009** | **209** | Từ Trường Bộc Phá Đại Pháo | Eustass Kid (Hắc Ám) | 5,000 | 220px | Tụ lực từ tính bắn đại pháo | Từ Trường Damned Punk |
| **4010** | **210** | Cổ Độc Phán Quyết Venom | Magellan (Quang Minh) | 5,000 | 220px | Axit độc dược ăn mòn | Cự Độc Venom Hydra |
| **4011** | **211** | Mũi Tên Mê Hoặc Thạch Hóa | Boa Hancock (Tu La) | 5,000 | 220px | Hóa đá kẻ thù trúng phải | Trái Tim Mero Mero |
| **4012** | **212** | Phượng Hoàng Bất Tử Bộc Phá | Marco (Thánh Linh) | 5,000 | 220px | Lam hỏa phượng hoàng thiêu rụi | Lam Hỏa Bất Tử Điểu |
| **4013** | **213** | Đại Phật Sóng Xung Kích | Sengoku (Huyết Long) | 5,000 | 220px | Chưởng sóng xung kích vô song | Hoàng Kim Đại Phật |
| **4014** | **214** | Bát Quái Cửu Long Thiên | Kaido (Ma Thần) | 5,000 | 220px | Chùy lôi long sấm sét bách thú | Hắc Lôi Ma Thần |
| **4015** | **215** | Long Trảo Viêm Long Toái Địa | Sabo (Thiên Thần) | 5,000 | 220px | Trảo rồng lửa phá hủy mặt đất | Viêm Long Trảo |
| **4016** | **216** | Vận Thạch Thiên Giáng | Fujitora (Hỗn Độn) | 5,000 | 220px | Thiên thạch rơi tự do từ vũ trụ | Trọng Lực Gravity |

---

## 3. Kiến Trúc Cấu Trúc Thư Mục

```
C:\\DepLor\\HTTH\\Team\\_ThanTrangAnimationSkill\\
├── README.md                              # Tài liệu kỹ thuật tổng hợp
├── database\\
│   ├── skills_database.json               # Siêu dữ liệu 16 skills chuẩn theo database
│   ├── skill_vfx_metadata.json            # 96 components animation metadata (w, h, frames, anchor)
│   └── insert_16_skills.sql               # File SQL Insert chuẩn bảng skill MySQL
├── tools\\
│   ├── init_database.py                   # Tool khởi tạo và đồng bộ database JSON / SQL
│   ├── generate_all_16_icons.py           # Tool tạo 16 icon tròn bo viền alpha cho 4 cấp zoom + buff
│   ├── build_16_skills_vfx_pipeline.py    # Pipeline render 96 spritesheets x1/x2 và trích xuất frames
│   ├── generate_showcase_html.py          # Tool sinh Master Studio v8 & 16 demo chiêu thức độc lập
│   └── run_preview_server.py              # Web server mini local (port 8088) xem demo ngay
├── img_res\\
│   ├── authentic_circular_mask_88.png     # Mặt nạ alpha tròn chuẩn 88x88 gốc từ game
│   ├── master_art\\                        # 32 file tranh Master Artwork 1024x1024 (clean & original)
│   ├── reference_strips\\                  # 134+ file dải film strip, concept VFX, pose nhân vật
│   ├── previews_gif\\                      # 75+ file ảnh động GIF mô phỏng chiêu thức chiến đấu
│   └── contact_sheets\\                    # 39+ bảng tổng hợp contact sheets các tầng hiệu ứng
├── icons\\
│   ├── x4\\                                # 88x88 px (Zoom 4) cho icon 201..216 & skill 4001..4016
│   ├── x3\\                                # 66x66 px (Zoom 3)
│   ├── x2\\                                # 44x44 px (Zoom 2)
│   ├── x1\\                                # 22x22 px (Zoom 1)
│   ├── buff_x4\\                           # 44x44 px (Buff Zoom 4) cho 701..716 & 4501..4516
│   ├── buff_x3\\                           # 33x33 px
│   ├── buff_x2\\                           # 22x22 px
│   ├── buff_x1\\                           # 11x11 px
│   └── by_skill\\                          # Phân loại theo 16 thư mục chiêu thức riêng biệt
├── spritesheets\\                          # 96 Spritesheets chuẩn 1x (6 components * 16 skills)
├── spritesheets_x2\\                       # 96 Spritesheets HD 2x siêu nét
├── frames\\                                # Hàng trăm frame PNG tách lẻ theo từng chiêu và component
└── demos\\
    ├── index.html                         # Master Cinematic Studio v8 (Combat Sim + Inspector + Gallery)
    ├── 4001.html ... 4016.html            # 16 trang demo độc lập cho từng chiêu thức
    └── preview_icons_showcase.html        # Showcase so sánh trực quan toàn bộ icon 4 zoom
```

---

## 4. Hướng Dẫn Sử Dụng Nhanh

### 4.1 Khởi chạy Web Server xem Demo Studio
Để xem giao diện chiến đấu thời gian thực và kiểm tra animation:
```powershell
python tools/run_preview_server.py
```
Trình duyệt sẽ tự động mở:
- **Master Studio (16 chiêu)**: `http://localhost:8088/demos/index.html`
- **Icon Showcase Gallery**: `http://localhost:8088/demos/preview_icons_showcase.html`
- **Demo Độc Lập**: `http://localhost:8088/demos/4001.html`, `http://localhost:8088/demos/4013.html`, v.v.

### 4.2 Tái tạo Icon 4 Cấp Zoom
Nếu cần tinh chỉnh hoặc xuất lại toàn bộ icon:
```powershell
python tools/generate_all_16_icons.py
```

### 4.3 Tái tạo Spritesheet & Frames
Nếu cần re-render spritesheet cho toàn bộ 16 chiêu:
```powershell
python tools/build_16_skills_vfx_pipeline.py
```
"""

with open(target, "w", encoding="utf-8") as f:
    f.write(content)

print("Generated README.md in _ThanTrangAnimationSkill")
