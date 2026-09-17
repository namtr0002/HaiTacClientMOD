# TÀI LIỆU LỊCH SỬ PHÁT TRIỂN & HƯỚNG DẪN KỸ THUẬT TOÀN DIỆN
# DỰ ÁN: HAITACTIHONTOOL (HẢI TẶC TÍ HON TOOL)
**Tác giả & Phát triển**: Deplor Team (Mr. Nam) & AI Coding Assistant  
**Phiên bản**: 1.0-SNAPSHOT (Ultimate Edition)  
**Môi trường chuẩn**: Java JDK 22 / NetBeans / Maven / FlatLaf Dark Theme  
**Trạng thái khôi phục**: Đã phục hồi 100% toàn bộ 119 file mã nguồn, biên dịch sạch không lỗi (`BUILD SUCCESS`).

---

## I. TỔNG QUAN VỀ HAITACTIHONTOOL

`HaiTacTiHonTool` là bộ công cụ tối thượng (All-in-One Studio) được xây dựng chuyên biệt cho hệ thống game **Hải Tặc Tí Hon (HTTH)** trên cả 2 nền tảng: **J2ME** và **Unity (C#)**. Công cụ hỗ trợ đầy đủ quy trình làm việc từ giải mã, đọc định dạng file nhị phân gốc, chỉnh sửa trực quan (Visual GUI), tự động sinh tài nguyên, trích xuất dữ liệu từ game server, cho tới xuất và đóng gói dữ liệu game chuẩn.

Giao diện đồ họa được thiết kế theo chuẩn Dark Theme hiện đại (dựa trên FlatLaf), tích hợp tăng tốc đồ họa `Graphics2D`, khử răng cưa và cơ chế xử lý đa luồng mượt mà.

---

## II. LỊCH SỬ PHÁT TRIỂN CŨ & MỚI (CHRONOLOGY)

### 1. Giai đoạn Khởi tạo & Kiến trúc lõi (Tháng 06/2026)
- **Lý do ra đời**: Các dữ liệu đồ họa (Effect, Part, Map) của game Hải Tặc Tí Hon được lưu trữ dưới các định dạng file nhị phân nén độc quyền (`.bin`, mPart, effData, tileData). Việc chỉnh sửa thủ công bằng Hex Editor rất dễ gây hỏng dữ liệu và sai lệch hitbox, offset frame.
- **Nhiệm vụ lõi ban đầu**:
  - Xây dựng framework UI Dark Theme (`Theme.java`, `Lang.java`, `AppConfig.java`, `StyledUI.java`).
  - Hệ thống bảo mật bản quyền `ultimate_security`: Xác thực khóa cấp phép (`KeyAuth.java`), định danh phần cứng máy tính (`HWID.java`), bộ nạp bảo vệ bytecode (`EngineProtector.java`), giao diện đăng nhập cao cấp (`LoginUI.java`).
  - Trình phân giải định dạng nhị phân cơ sở (`DataInputStream` / `DataOutputStream` chuẩn big-endian của HTTH).

### 2. Phát triển Module Effect Studio & Part Studio (Tháng 07 - Tháng 08/2026)
- **Effect Studio**:
  - Đọc và ghi định dạng dữ liệu hiệu ứng kỹ năng (`EffectBinaryParser.java`, `EffectWriter.java`).
  - Hỗ trợ cấu trúc phân cấp: Model -> Frame (`EffFrame`) -> FramePart (`EffPartFrame`) -> SmallImage (`SmallImageDef`).
  - Công cụ cắt Sprite Sheet trực quan (`SpriteCutterDialog.java`): Tự động phát hiện viền ảnh rỗng (bounding box trimming), cắt theo lưới (Grid cutter) hoặc cắt theo alpha ranh giới.
  - Trình biên tập dòng thời gian (`EffectTimelineBar.java`), quản lý sequence chuyển động (`EffectSequencePanel.java`), danh mục frame preview (`EffectFrameCatalog.java`, `EffectBrowserDialog.java`).
- **Part Studio**:
  - Chỉnh sửa trang bị nhân vật: Tóc (Head), Áo (Body), Quần (Leg), Vũ khí (Weapon), Cánh (Wing/Coat)...
  - Mapping chính xác theo `CharInfoData.java` với 16+ pose hành động tiêu chuẩn của nhân vật trong HTTH (Đứng, Đi bộ, Tấn công 1, Tấn công 2, Nhảy, Bị thương, Gục...).
  - Canvas kéo thả offset tọa độ trực tiếp (`PartCanvas.java`, `OffsetUtility.java`, `PartSaveDialog.java`, `PartSelectorDialog.java`).

### 3. Nâng cấp Xuất Ảnh Động GIF & Batch Render (Đầu Tháng 09/2026)
- **Yêu cầu từ người dùng**: Tích hợp khả năng quay và xuất trực tiếp chuyển động của nhân vật, quái vật, effect ra file ảnh động GIF chất lượng cao để làm demo giới thiệu hoặc lưu trữ.
- **Hiện thực**:
  - Tích hợp bộ giải thuật mã hóa GIF hiệu năng cao không phụ thuộc thư viện ngoài: `AnimatedGifEncoder.java`, thuật toán giảm màu lượng tử `NeuQuant.java`, bộ nén chuỗi LZW `LZWEncoder.java`.
  - Cung cấp `GifExportHelper.java` và hộp thoại `GifExportDialog.java`: Hỗ trợ cấu hình độ phân giải (scale x1, x2, x4), tốc độ khung hình (FPS delay), màu nền hoặc trong suốt (Transparent background), vòng lặp vô hạn (Infinite loop).
  - Tích hợp công cụ `ExportPosesRunner.java`: Tự động quét và xuất hàng loạt toàn bộ 16 pose hành động của nhân vật ra ảnh tĩnh PNG sắc nét hoặc ảnh động GIF.

### 4. Nâng cấp Hệ thống Map Studio & Auto Map Generator (05/09/2026 - Đột phá lớn)
- **Động lực**: Map game HTTH truyền thống phải xếp từng tile bằng tay rất tốn thời gian. Người dùng yêu cầu khả năng phân tích tilemap và **tự động kiến tạo thế giới map mới (Procedural Auto Generation)**.
- **Hiện thực kiến trúc Map Studio toàn diện**:
  - `AutoMapEngine.java` (v9.0): Áp dụng thuật toán sinh thế giới giả ngẫu nhiên bằng sóng Perlin/Value Noise (`MapNoiseUtils.java`). Định nghĩa các dạng địa hình (Terrain Styles: `FLAT_WALKWAY`, `STEP_TERRACES`, `GENTLE_HILLS`, `CAVERN_DUNGEON`) kết hợp Biome Archetypes phân tầng thông minh (Tầng đất mặt, tầng lõi, vách đá, nền biển, hang động).
  - `AutoMapGeneratorDialog.java`: Giao diện cấu hình tham số sinh map (kích thước WxH, tỉ lệ đất/nước, phân bố quái vật `MobPattern`, tỉ lệ xuất hiện boss `spawnBoss`, số lượng cây cối/nhà cửa/hòm kho báu, cổng chuyển map VGO).
  - `SeasonalTransformDialog.java`, `SeasonThemeManager.java`, `SeasonKeywordStore.java`: Hệ thống biến đổi cảnh quan bản đồ theo 5 chủ đề mùa độc đáo (Mùa Hè Nhiệt Đới, Mùa Thu Lá Vàng, Mùa Đông Băng Tuyết, Lễ Hội Tết Cổ Truyền, Vùng Đảo Hải Tặc).
  - `BatchMapExporter.java`, `MapImageExporter.java`, `SQLMapLoader.java`: Hỗ trợ tải trực tiếp dữ liệu map từ Database MySQL của Server, xuất hàng loạt toàn bộ map ra ảnh render chuẩn hoặc dữ liệu nhị phân nén.

### 5. Phát triển Hệ thống Mob, Pet & Fashion V2
- **Mob Studio**: Thiết kế quái vật (`MobTemplate.java`, `MobDataManager.java`, `MobCanvas.java`, `MobPartSelectorDialog.java`). Xem trước toàn bộ frame di chuyển, tấn công, chịu đòn của quái vật trên canvas 60 FPS.
- **Pet Studio**: Thiết kế thú nuôi (`PetTemplate.java`, `PetDataManager.java`, `PetCanvas.java`). Quản lý sprite sheet, frame count, preview thú cưng bay lượn và hiệu ứng hỗ trợ.
- **Fashion V2 Studio**: Định dạng trang phục hiện đại đa lớp thế hệ mới (`FashionV2Model.java`, `FashionV2Canvas.java`, `FashionV2Parser.java`, `FashionV2Converter.java`, `FashionV2AtlasMerger.java`, `FashionV2Merger.java`, `KatakuriFashionV2Builder.java`). Hỗ trợ đóng gói sprite sheet atlas thành một texture duy nhất giúp tối ưu bộ nhớ GPU.

### 6. Module LeakRes Studio (Khai thác tài nguyên mạng từ Server)
- **Tính năng**: Cho phép kết nối trực tiếp đến Server game HTTH với vai trò là một client đặc biệt để tải trọn gói dữ liệu:
  - Bắt và xử lý các opcode packet mạng: `-2`, `-4`, `-9`, `-10`, `-11`, `-19`, `-27`, `-33`, `-38`, `-39`, `-40`, `-41`, `-44`, `-51`, `-82`, `-95`, `-101`, `-105`, `0`, `74`, `76`...
  - Tự động quét dải ID (ID Range) và tải về các loại tài nguyên: Icon Item, Icon Skill, Part nhân vật, Effect Skill, EffectAuto, Dữ liệu Map.
  - Giao diện có thanh tiến trình thời gian thực (`progressIcon`, `progressPart`, `progressEffect`...) và khung console theo dõi thông điệp mạng.

### 7. Module Tiện ích Đồ Họa Bổ Trợ (Tool & UI Components)
- `CharsetCryptoPanel.java`: Hỗ trợ mã hóa/giải mã xor/shift của game, chuyển đổi chuỗi tiếng Việt TCVN3, UTF-8.
- `DataConverterPanel.java`: Chuyển đổi dữ liệu ByteArray sang Base64, Hex, Int array.
- `IconResizerPanel.java`: Tự động co giãn kích thước icon chuẩn hóa cho cả J2ME và Unity (x1, x2, x3, x4) mà không bị nhòe pixel nhờ thuật toán nội suy lân cận gần nhất (Nearest Neighbor / Bicubic).
- `ImageEditorPanel.java`: Bảng chỉnh sửa pixel, tô màu trong suốt, cân bằng sáng tối nhanh.
- Hệ thống UI components cao cấp: `AsyncButton`, `BaseDarkDialog`, `BreadcrumbBar`, `FileWatcherService` (tự động reload tài nguyên khi file trên đĩa thay đổi), `ImagePreviewPanel`, `SmartFolderChooser`, `Toast` thông báo pop-up nhẹ nhàng.

---

## III. DANH MỤC 119 CLASS & KIẾN TRÚC MÃ NGUỒN

Dự án gồm **119 class** được tổ chức phân cấp rõ ràng theo các package chức năng:

```
com.deplor.haitactihontool
├── config/                         # Cấu hình hệ thống & Theme
│   ├── AppConfig.java              # Lưu trữ đường dẫn res, zoom, database config
│   ├── ImageZoomHelper.java        # Xử lý zoom ảnh tỉ lệ nguyên chuẩn game 2D
│   ├── Lang.java                   # Đa ngôn ngữ (Tiếng Việt / English)
│   └── Theme.java                  # Bảng màu FlatLaf Cyber Dark, font Segoe UI
│
├── effect/                         # Studio thiết kế & biên tập hiệu ứng skill
│   ├── EffFrame.java               # Cấu trúc 1 frame hiệu ứng
│   ├── EffPartFrame.java           # Cấu trúc mảnh ghép của frame (dx, dy, idSmall, flip)
│   ├── EffectBinaryParser.java     # Đọc dữ liệu nhị phân effect từ file .bin
│   ├── EffectBrowserDialog.java    # Trình duyệt chọn và xem trước danh sách effect
│   ├── EffectCanvas.java           # Canvas vẽ và mô phỏng hiệu ứng 60 FPS
│   ├── EffectCharRenderer.java     # Giả lập nhân vật làm nền để căn chỉnh hiệu ứng
│   ├── EffectFrameCatalog.java     # Quản lý danh mục các khung hình
│   ├── EffectFrameList.java        # Danh sách frame hiển thị
│   ├── EffectModel.java            # Mô hình dữ liệu Effect hoàn chỉnh
│   ├── EffectPartPanel.java        # Panel chỉnh sửa tọa độ chi tiết các mảnh ghép
│   ├── EffectRightStudioPanel.java # Panel điều khiển bên phải studio
│   ├── EffectSaveDialog.java       # Hộp thoại lưu và xuất dữ liệu hiệu ứng
│   ├── EffectSequencePanel.java    # Bảng biên tập chuỗi phát hoạt ảnh
│   ├── EffectTimelineBar.java      # Thanh timeline cuộn, tua, scrub animation
│   ├── EffectWriter.java           # Biên dịch model ra file nhị phân HTTH
│   ├── MainUI.java                 # Giao diện chính của tab Effect Studio
│   ├── PartQuickEditDialog.java    # Chỉnh sửa nhanh thông số part
│   ├── SequenceEditorDialog.java   # Hộp thoại chỉnh sửa chi tiết thứ tự chuỗi frame
│   ├── SmallImageDef.java          # Định nghĩa thông số mảnh ảnh nhỏ
│   ├── SpriteCutterDialog.java     # Hộp thoại cắt sprite sheet thông minh
│   ├── SpriteSheetViewer.java      # Trình xem sprite sheet tổng hợp
│   └── TextureImportDialog.java    # Nhập ảnh texture mới vào dữ liệu
│
├── effectauto/                     # Studio hiệu ứng tự động (Loop, Orbit, Scale)
│   ├── EffectAutoModel.java        # Mô hình hiệu ứng auto
│   ├── EffectAutoParser.java       # Đọc định dạng EffectAuto
│   ├── EffectAutoSaveDialog.java   # Hộp thoại lưu EffectAuto
│   ├── EffectAutoWriter.java       # Xuất định dạng file EffectAuto
│   └── MainUI.java                 # Giao diện chính EffectAuto Studio
│
├── fashionv2/                      # Studio thời trang thế hệ mới (Fashion V2)
│   ├── FashionV2AtlasMerger.java   # Ghép các part thành texture atlas duy nhất
│   ├── FashionV2Canvas.java        # Canvas hiển thị trang phục đa lớp V2
│   ├── FashionV2Converter.java     # Chuyển đổi định dạng V1 sang V2
│   ├── FashionV2DemoLauncher.java  # Khởi chạy bản demo thời trang
│   ├── FashionV2Merger.java        # Hợp nhất các thành phần trang phục
│   ├── FashionV2Model.java         # Data model của Fashion V2
│   ├── FashionV2Parser.java        # Đọc định dạng file Fashion V2
│   ├── KatakuriFashionV2Builder.java # Builder bộ trang phục mẫu Katakuri
│   └── MainUI.java                 # Giao diện tab Fashion V2
│
├── leakres/                        # Phân hệ khai thác tài nguyên từ máy chủ (Server Leak)
│   ├── MainUI.java                 # Giao diện điều khiển Socket client & tải res
│   └── Message.java                # Cấu trúc thông điệp packet game HTTH
│
├── map/                            # Hệ thống Studio Bản đồ & Auto Map Engine
│   ├── AutoMapEngine.java          # Bộ engine sinh bản đồ tự động bằng AI/Noise v9.0
│   ├── AutoMapGeneratorDialog.java # Hộp thoại cấu hình và sinh bản đồ tự động
│   ├── BatchMapExporter.java       # Công cụ xuất hàng loạt map
│   ├── ExportAllMapsRunner.java    # Luồng chạy xuất toàn bộ bản đồ
│   ├── GameMap.java                # Data model bản đồ (Tile, Mob, NPC, Vgo, Boat)
│   ├── GameMapCanvas.java          # Canvas chỉnh sửa map tương tác trực quan
│   ├── ImageCache.java             # Bộ nhớ đệm tối ưu hóa ảnh tile & item
│   ├── ItemMapEntity.java          # Thực thể vật phẩm/cây cối/nhà cửa trên map
│   ├── ItemMapTemplate.java        # Template định nghĩa item bản đồ
│   ├── ItemPalettePanel.java       # Bảng chọn các item trang trí map
│   ├── LoadingCallback.java        # Callback báo tiến độ load dữ liệu
│   ├── MainUI.java                 # Giao diện chính phân hệ Map Editor Studio
│   ├── MapDataExporter.java        # Xuất dữ liệu bản đồ ra nhị phân/SQL
│   ├── MapDataGenerator.java       # Trình hỗ trợ tạo cấu trúc dữ liệu bản đồ
│   ├── MapDataLoader.java          # Đọc dữ liệu map từ file local
│   ├── MapImageExporter.java       # Render bản đồ đầy đủ ra file ảnh lớn
│   ├── MapNoiseUtils.java          # Bộ tiện ích sinh nhiễu Value/Perlin Noise
│   ├── MapSaveExportDialog.java    # Hộp thoại lưu, backup và xuất map
│   ├── ObjectPalettePanel.java     # Bảng chọn NPC, Quái vật, Cổng VGO
│   ├── SQLMapLoader.java           # Nạp dữ liệu bản đồ trực tiếp từ MySQL Database
│   ├── SeasonKeywordStore.java     # Từ điển ánh xạ từ khóa theo mùa
│   ├── SeasonTheme.java            # Định nghĩa các chủ đề mùa
│   ├── SeasonThemeManager.java     # Trình quản lý chuyển đổi phong cách bản đồ
│   ├── SeasonalTransformDialog.java# Hộp thoại áp dụng chuyển đổi mùa cho bản đồ
│   ├── TemplateManager.java        # Quản lý template quái, npc, vật thể
│   ├── TileMapConfig.java          # Cấu hình tileset (kích thước tile 24x24)
│   └── TilePalettePanel.java       # Bảng chọn các tile địa hình để vẽ
│
├── mob/                            # Quản lý Quái vật & Boss
│   ├── MainUI.java                 # Giao diện Studio thiết kế quái vật
│   ├── MobCanvas.java              # Canvas mô phỏng chuyển động của Mob
│   ├── MobDataManager.java         # Quản lý nạp/lưu dữ liệu quái
│   ├── MobPartSelectorDialog.java  # Chọn part cho quái
│   └── MobTemplate.java            # Cấu trúc thông số của quái
│
├── npc/                            # Quản lý NPC & Lời thoại
│   └── MainUI.java                 # Giao diện quản lý danh sách NPC
│
├── part/                           # Quản lý Part trang phục nhân vật
│   ├── CharInfoData.java           # Dữ liệu 16 pose hành động chuẩn của nhân vật HTTH
│   ├── ExportPosesRunner.java      # Công cụ xuất hàng loạt pose ra frame PNG/GIF
│   ├── MainUI.java                 # Giao diện Studio thiết kế trang phục
│   ├── OffsetUtility.java          # Tiện ích tự động căn chỉnh tọa độ cân đối
│   ├── Part.java                   # Định nghĩa Part trang phục
│   ├── PartCanvas.java             # Canvas hiển thị trang phục trên cơ thể nhân vật
│   ├── PartDataManager.java        # Nạp và quản lý danh sách part
│   ├── PartImage.java              # Mảnh ảnh nhỏ trong part (id, dx, dy)
│   ├── PartSaveDialog.java         # Hộp thoại lưu và ghi đè part
│   ├── PartSelectorDialog.java     # Hộp thoại chọn part trực quan
│   └── mPart.java                  # Cấu trúc nạp part tương thích client J2ME
│
├── pet/                            # Quản lý Thú cưng
│   ├── MainUI.java                 # Giao diện thiết kế thú cưng
│   ├── PetCanvas.java              # Canvas mô phỏng bay/đi của thú cưng
│   ├── PetDataManager.java         # Quản lý dữ liệu thú nuôi
│   └── PetTemplate.java            # Thuộc tính thú cưng
│
├── tool/                           # Tiện ích đồ họa bổ trợ
│   ├── CharsetCryptoPanel.java     # Mã hóa và giải mã bảng ký tự game
│   ├── DataConverterPanel.java     # Chuyển đổi dữ liệu Hex/Base64/Binary
│   ├── IconResizerPanel.java       # Co giãn kích thước icon chuẩn Pixel-Art
│   ├── ImageEditorPanel.java       # Bảng chỉnh sửa pixel và màu sắc
│   └── MainUI.java                 # Giao diện tab Tool tiện ích
│
├── ui/                             # Giao diện ứng dụng chính & Components
│   ├── AuthUI.java                 # Giao diện ủy quyền người dùng
│   ├── BoostrapLoader.java         # Màn hình Splash tải ban đầu
│   ├── Bootstrap.java              # Entry point chính của ứng dụng (`main`)
│   ├── MainFrame.java              # Cửa sổ chính chứa các tab chức năng
│   └── components/                 # Các UI component giao diện độc quyền
│       ├── AsyncButton.java        # Nút bấm bất đồng bộ có icon xoay loading
│       ├── BaseDarkDialog.java     # Cửa sổ Dialog phong cách Dark Theme
│       ├── BreadcrumbBar.java      # Thanh điều hướng đường dẫn thư mục
│       ├── FileWatcherService.java # Tự động phát hiện file sửa đổi trên ổ đĩa
│       ├── ImagePreviewPanel.java  # Panel xem trước hình ảnh
│       ├── SleekFolderChooser.java # Hộp thoại chọn thư mục giao diện phẳng
│       ├── SmartFolderChooser.java # Trình duyệt thư mục thông minh
│       ├── StyledUI.java           # Factory tạo button, header, checkbox, combobox
│       ├── TaskManager.java        # Quản lý tiến trình nền
│       └── Toast.java              # Thông báo nổi (Toast Notification)
│
├── ultimate_security/              # Hệ thống phân quyền & Bảo vệ bản quyền
│   ├── EngineProtector.java        # Chống debugger & tamper classloader
│   ├── HWID.java                   # Tính toán mã định danh phần cứng máy tính
│   ├── KeyAuth.java                # Xác thực License Key trực tuyến / offline
│   ├── LoginUI.java                # Hộp thoại nhập Key kích hoạt
│   └── SessionInfo.java            # Lưu trữ phiên làm việc hợp lệ
│
└── util/                           # Các thuật toán & Định dạng mở rộng
    ├── AnimatedGifEncoder.java     # Bộ tạo ảnh động GIF đa khung hình
    ├── GifExportDialog.java        # Hộp thoại cấu hình xuất file GIF
    ├── GifExportHelper.java        # Luồng render frame và xuất GIF mượt mà
    ├── LZWEncoder.java             # Bộ nén dữ liệu LZW tiêu chuẩn GIF
    └── NeuQuant.java               # Giải thuật mạng Neural lượng tử hóa 256 màu
```

---

## IV. HƯỚNG DẪN BIÊN DỊCH VÀ SỬ DỤNG

### 1. Yêu cầu môi trường
- **Hệ điều hành**: Windows 10/11 (64-bit).
- **Java**: Oracle JDK 22 hoặc OpenJDK 22 (Đường dẫn khuyến nghị: `C:\Program Files\Java\jdk-22`).
- **Maven**: Apache Maven 3.9+ (Có thể sử dụng Maven tích hợp của NetBeans tại `C:\Program Files\NetBeans-20\netbeans\java\maven\bin\mvn.cmd`).

### 2. Lệnh biên dịch chuẩn
Mở PowerShell hoặc Command Prompt tại thư mục dự án `c:\DepLor\HTTH\Team\HaiTacTiHonTool`:

```powershell
# Thiết lập biến môi trường JDK 22
$env:JAVA_HOME = "C:\Program Files\Java\jdk-22"
$env:Path = "C:\Program Files\Java\jdk-22\bin;" + $env:Path

# Biên dịch toàn bộ mã nguồn
& "C:\Program Files\NetBeans-20\netbeans\java\maven\bin\mvn.cmd" compile

# Đóng gói tạo file JAR thực thi hoàn chỉnh (Fat Shaded Jar)
& "C:\Program Files\NetBeans-20\netbeans\java\maven\bin\mvn.cmd" package -DskipTests
```

Sau khi chạy xong, file thực thi sẽ nằm tại:
`c:\DepLor\HTTH\Team\HaiTacTiHonTool\target\HaiTacTiHonTool-1.0-SNAPSHOT.jar` (dung lượng ~6.3 MB, đã tích hợp đầy đủ mọi thư viện phụ thuộc).

### 3. Khởi chạy công cụ
```powershell
java -jar target\HaiTacTiHonTool-1.0-SNAPSHOT.jar
```

- **Mã kích hoạt miễn phí (Key Free)**: `haitactihontool`

### 4. Đóng gói ra bản Native .EXE độc lập (`build_ultimate.bat`)
Trong thư mục dự án đã có sẵn kịch bản `build_ultimate.bat`:
- Tự động gọi Maven clean & package.
- Tự động áp dụng ProGuard làm rối bytecode bảo vệ mã nguồn.
- Tự động gọi .NET 9.0 SDK biên dịch C# AOT Wrapper (`Wrapper.cs`) để tạo file thực thi `release\HaiTacTiHonTool.exe` độc lập cho người dùng cuối mà không để lộ file jar gốc.

---

## V. TỔNG KẾT & CAM KẾT HOÀN THÀNH

Toàn bộ dự án `HaiTacTiHonTool` đã được khôi phục **100% đầy đủ mọi chức năng, toàn bộ 119 class mã nguồn, file cấu hình, scripts**, đồng bộ trực tiếp về thư mục chuẩn `c:\DepLor\HTTH\Team\HaiTacTiHonTool` và đã được nghiệm thu với kết quả biên dịch sạch sẽ tuyệt đối (`BUILD SUCCESS`).