package com.deplor.haitactihontool.config;

import java.util.HashMap;
import java.util.Map;

public class Lang {
   private static Lang.Locale currentLocale = Lang.Locale.VI;
   private static final Map<String, String[]> strings = new HashMap<>();

   private static void reg(String key, String vi, String en) {
      strings.put(key, new String[]{vi, en});
   }

   public static String get(String key) {
      String[] s = strings.get(key);
      return s == null ? key : s[currentLocale == Lang.Locale.VI ? 0 : 1];
   }

   public static void setLocale(Lang.Locale l) {
      currentLocale = l;
   }

   public static Lang.Locale getLocale() {
      return currentLocale;
   }

   static {
      reg("app_title", "HTTH TOOL SUITE", "HTTH TOOL SUITE");
      reg("login_title", "ĐĂNG NHẬP CÔNG CỤ", "LOGIN TOOL");
      reg("license_key", "MÃ BẢN QUYỀN", "LICENSE KEY");
      reg("machine_id", "MÃ MÁY (HWID)", "MACHINE ID (HWID)");
      reg("login_btn", "XÁC MINH VÀ KHỞI CHẠY", "VALIDATE AND LAUNCH");
      reg("status_ready", "Hệ thống sẵn sàng - nhập mã để tiếp tục", "System ready - enter key to continue");
      reg("status_verifying", "Đang xác thực thông tin...", "Verifying information...");
      reg("status_expired", "Hết hạn!", "Expired!");
      reg("tab_map", "BẢN ĐỒ", "MAPS");
      reg("tab_part", "PART", "PART");
      reg("tab_pet", "THÚ NUÔI", "PETS");
      reg("tab_tile", "TILE", "TILE");
      reg("tab_effect", "HIỆU ỨNG", "EFFECTS");
      reg("tab_effectauto", "EFFAUTO", "EFFAUTO");
      reg("tab_skill", "KỸ NĂNG", "SKILLS");
      reg("tab_leakres", "LEAK RESOURCE", "LEAK RESOURCE");
      reg("tab_tool", "TOOL", "TOOL");
      reg("tab_settings", "CÀI ĐẶT", "SETTINGS");
      reg("map_props", "THUỘC TÍNH BẢN ĐỒ", "MAP PROPERTIES");
      reg("map_name", "Tên bản đồ:", "Map Name:");
      reg("map_width", "Chiều rộng:", "Width:");
      reg("map_height", "Chiều cao:", "Height:");
      reg("map_tileset", "Mã TileSet:", "TileSet ID:");
      reg("map_level", "Cấp độ:", "Level:");
      reg("map_zones", "Số khu vực:", "Zones:");
      reg("map_players", "Số người chơi:", "Players:");
      reg("map_save_db", "LƯU VÀO CƠ SỞ DỮ LIỆU", "SAVE TO DATABASE");
      reg("grid", "Lưới", "Grid");
      reg("collision", "Va chạm", "Collision");
      reg("objects", "ĐỐI TƯỢNG", "OBJECTS");
      reg("npc", "NPC", "NPCs");
      reg("mob", "Quái", "Mobs");
      reg("tab_mob", "QUÁI VẬT", "MOBS");
      reg("add_npc", "Thêm NPC", "Add NPC");
      reg("del_npc", "Xóa NPC", "Delete NPC");
      reg("edit_npc", "Sửa NPC", "Edit NPC");
      reg("settings_lang", "Ngôn ngữ / Language", "Language / Ngôn ngữ");
      reg("settings_theme", "Giao diện / Theme", "Theme / Giao diện");
      reg("settings_data_dir", "Thư mục Data gốc", "Data Base Directory");
      reg("settings_data_dir_placeholder", "Nhập đường dẫn đến thư mục chứa Data...", "Enter path to Data folder...");
      reg("settings_data_dir_btn", "Chọn thư mục", "Choose Folder");
      reg("tool_pencil", "BÚT VẼ", "PENCIL");
      reg("tool_eraser", "TẨY", "ERASER");
      reg("tool_bucket", "ĐỔ MÀU", "BUCKET");
      reg("tool_collision", "VA CHẠM", "COLLISION");
      reg("tool_npc", "NPC", "NPC");
      reg("tool_mob", "QUÁI", "MOB");
      reg("tool_vgo", "CỔNG DỊCH CHUYỂN", "VGO GATE");
      reg("tool_undo", "HOÀN TÁC", "UNDO");
      reg("tool_redo", "LÀM LẠI", "REDO");
      reg("search_placeholder", "Tìm ID hoặc ID ảnh...", "Search ID or Image ID...");
      reg("folder_chooser_title", "CHỌN THƯ MỤC", "CHOOSE FOLDER");
      reg("btn_select", "CHỌN THƯ MỤC", "SELECT FOLDER");
      reg("btn_cancel", "Hủy", "Cancel");
      reg("btn_save", "  SAVE  ", "  SAVE  ");
      reg("part_save_title", "  ⬡  PART SAVE WORKSHOP", "  ⬡  PART SAVE WORKSHOP");
      reg("part_save_subtitle", "Rename IDs · Xuất SQL · Cấp độ thu phóng", "Rename IDs · SQL Export · Zoom Levels");
      reg("output_folder", "Thư mục Output:", "Output Folder:");
      reg("start_part_id", "ID Part bắt đầu:", "Start Part ID:");
      reg("start_img_id", "ID Ảnh bắt đầu:", "Start Img ID:");
      reg("btn_preview", "↻ Preview", "↻ Preview");
      reg("rename_preview", "  RENAME PREVIEW", "  RENAME PREVIEW");
      reg("col_part", "Part", "Part");
      reg("col_type", "Loại", "Type");
      reg("col_old_part", "ID Part Cũ", "Old Part ID");
      reg("col_new_part", "ID Part Mới", "New Part ID");
      reg("col_frames", "Số Frame", "Frames");
      reg("col_old_img", "ID Ảnh Cũ", "Old Img Start");
      reg("col_new_img", "ID Ảnh Mới", "New Img Start");
      reg("col_status", "Trạng thái", "Status");
      reg("status_ready_part", "Sẵn sàng. Nhập thông tin và nhấn SAVE.", "Ready. Enter info and click SAVE.");
      reg("status_saving", "Đang lưu...", "Saving...");
      reg("status_saving_percent", "Đang lưu... ", "Saving... ");
      reg("status_save_success", "✓ Lưu thành công vào: ", "✓ Save successful to: ");
      reg("status_save_success_dir", "✓ Lưu thành công: ", "✓ Save successful: ");
      reg("status_saved", "✓ Đã lưu", "✓ Saved");
      reg("status_cancel", "⚠ Đã hủy quá trình lưu.", "⚠ Save cancelled.");
      reg("err_select_folder", "⚠ Vui lòng chọn thư mục output!", "⚠ Please select output folder!");
      reg("err_create_folder", "⚠ Không tạo được thư mục: ", "⚠ Failed to create folder: ");
      reg("err_no_data", "⚠ Không có dữ liệu!", "⚠ No data available!");
      reg("err_no_effect_data", "⚠ Không có dữ liệu effect!", "⚠ No effect data!");
      reg("effect_save_title", "  ◈  EFFECT SAVE", "  ◈  EFFECT SAVE");
      reg("effect_save_subtitle", "Binary + Ảnh (x1/x2/x3/x4)", "Binary + Images (x1/x2/x3/x4)");
      reg("current_id", "ID Hiện tại:", "Current ID:");
      reg("new_id", "ID Mới:", "New ID:");
      reg("keep_id_hint", "  (để trống = giữ ID cũ)", "  (empty = keep old ID)");
      reg("save_binary", "Lưu binary data", "Save binary data");
      reg("save_images", "Lưu ảnh (4 zoom levels)", "Save images (4 zoom levels)");
      reg("save_images_atlas", "Lưu ảnh atlas (4 zoom levels)", "Save images atlas (4 zoom levels)");
      reg("output_preview", "  OUTPUT STRUCTURE PREVIEW", "  OUTPUT STRUCTURE PREVIEW");
      reg("effectauto_save_title", "  ◉  EFFECT AUTO SAVE", "  ◉  EFFECT AUTO SAVE");
      reg("effectauto_save_subtitle", "Auto Binary + Ảnh (x1/x2/x3/x4)", "Auto Binary + Images (x1/x2/x3/x4)");
      reg("lbl_fashion_array", "MÃ TRANG PHỤC:", "FASHION ARRAY:");
      reg("lbl_fashion_slots", "FASHION SLOTS", "FASHION SLOTS");
      reg("lbl_part_frames", "PART FRAMES", "PART FRAMES");
      reg("chk_play", "PLAY", "PLAY");
      reg("chk_flip", "FLIP (RIGHT)", "FLIP (RIGHT)");
      reg("chk_big_body", "BIG BODY", "BIG BODY");
      reg("lbl_frame_props", "FRAME PROPERTIES", "FRAME PROPERTIES");
      reg("btn_new_part", "+ NEW PART", "+ NEW PART");
      reg("btn_batch_update", "BATCH UPDATE IDS", "BATCH UPDATE IDS");
      reg("btn_browse_img", "BROWSE IMAGE", "BROWSE IMAGE");
      reg("lbl_img_id", "IMAGE ID:", "IMAGE ID:");
      reg("lbl_offset_x", "OFFSET X:", "OFFSET X:");
      reg("lbl_offset_y", "OFFSET Y:", "OFFSET Y:");
      reg("folder_btn_new", "+ Thư Mục Mới", "+ New Folder");
      reg("folder_btn_rename", "✏ Đổi Tên", "✏ Rename");
      reg("folder_btn_delete", "Xóa", "Delete");
      reg("folder_btn_refresh", "↺ Làm Mới", "↺ Refresh");
      reg("folder_loading", "  Đang tải...", "  Loading...");
      reg("folder_info_header", "THÔNG TIN THƯ MỤC", "FOLDER INFORMATION");
      reg("folder_info_name", "Tên:", "Name:");
      reg("folder_info_path", "Đường dẫn:", "Path:");
      reg("folder_info_subs", "Số thư mục con:", "Sub-folders count:");
      reg("folder_info_size", "Tổng dung lượng:", "Total size:");
      reg("folder_err_invalid_path", "Đường dẫn không hợp lệ hoặc không tồn tại.", "Invalid or non-existent path.");
      reg("folder_calculating", "Đang tính...", "Calculating...");
      reg("folder_err_select_first", "Hãy chọn một thư mục trước.", "Please select a folder first.");
      reg("folder_prompt_new_name", "Tên thư mục mới:", "New folder name:");
      reg("folder_title_input", "Nhập tên", "Enter Name");
      reg("folder_err_exists", "Thư mục đã tồn tại: ", "Folder already exists: ");
      reg("folder_success_created", "✔ Đã tạo: ", "✔ Created: ");
      reg("folder_err_create_fail", "✘ Không thể tạo thư mục (kiểm tra quyền).", "✘ Cannot create folder (check permissions).");
      reg("folder_prompt_rename", "Tên mới:", "New name:");
      reg("folder_err_rename_exists", "Đã tồn tại thư mục: ", "Folder already exists: ");
      reg("folder_success_renamed", "✔ Đã đổi tên thành: ", "✔ Renamed to: ");
      reg("folder_err_rename_fail", "✘ Không thể đổi tên (kiểm tra quyền).", "✘ Cannot rename folder (check permissions).");
      reg("folder_confirm_delete_title", "Xác nhận xóa", "Confirm Delete");
      reg(
         "folder_confirm_delete_msg",
         "Bạn có chắc muốn xóa thư mục:\n%s\n\nThư mục phải rỗng để xóa được.",
         "Are you sure you want to delete folder:\n%s\n\nThe folder must be empty to be deleted."
      );
      reg("folder_success_deleted", "✔ Đã xóa thư mục.", "✔ Deleted folder.");
      reg(
         "folder_err_delete_fail",
         "✘ Không thể xóa (thư mục có thể không rỗng hoặc thiếu quyền).",
         "✘ Cannot delete (folder might not be empty or lack permissions)."
      );
      reg("output_folder_placeholder", "Chọn hoặc gõ đường dẫn thư mục output...", "Select or type output folder path...");
      reg("btn_browse", "Duyệt...", "Browse...");
      reg("status_ready_row", "✓ Sẵn sàng", "✓ Ready");
      reg("status_ready_simple", "Sẵn sàng.", "Ready.");
      reg("status_error_prefix", "⚠ Lỗi: ", "⚠ Error: ");
      reg("effect_output_dir_placeholder", "Thư mục output...", "Output folder...");
      reg("effect_preview_available", "✓ Có sẵn", "✓ Available");
      reg("effect_preview_no_image", "✗ Chưa import ảnh", "✗ No image imported");
      reg("effect_preview_no_image_auto", "✗ Chưa import ảnh - hãy import trước", "✗ No image - import first");
      reg("effect_preview_frames", "Số frame: ", "Frames: ");
      reg("effect_atlas_lbl", "Atlas: ", "Atlas: ");
      reg("part_no_display", "Không có Part nào đang được hiển thị/chỉnh sửa trên canvas!", "No Part is currently displayed/edited on the canvas!");
      reg("part_notification", "Thông báo", "Notification");
      reg("part_deploy_success", " parts successfully deployed!", " parts successfully deployed!");
      reg("part_confirm_clear", "Xóa toàn bộ ảnh trong part này?", "Clear all images in this part?");
      reg("part_enter_image_ids", "Nhập ID ảnh (ngăn cách bằng dấu phẩy, ví dụ: 2001,2002,2003):", "Enter image IDs (comma-separated, e.g., 2001,2002,2003):");
      reg("load", "Load", "Load");
      reg("save_assets", "LƯU ASSETS", "SAVE ASSETS");
      reg("part_menu_batch_replace", "Thay thế hàng loạt ảnh", "Batch Replace All Images");
      reg("part_menu_clear", "Xóa sạch ảnh", "Clear All Images");
      reg("lbl_effect_id", "Mã Effect:", "Effect ID:");
      reg("lbl_auto_id", "Mã Auto ID:", "Auto ID:");
      reg("lbl_move", "Di chuyển:", "Move:");
      reg("lbl_body", "Cơ thể:", "Body:");
      reg("lbl_speed", "Tốc độ:", "Speed:");
      reg("btn_import_img", "Nhập Ảnh", "Import Img");
      reg("btn_import_texture", "Nhập Texture", "Import Texture");
      reg("btn_reset_view", "Đặt lại view", "Reset View");
      reg("btn_edit_mode", "Chế độ sửa", "Edit Mode");
      reg("btn_new", "Tạo mới", "New");
      reg("btn_save_simple", "Lưu", "Save");
      reg("part_dx", "Part DX:", "Part DX:");
      reg("part_dy", "Part DY:", "Part DY:");
      reg("sprite_id", "Sprite ID:", "Sprite ID:");
      reg("auto_type", "Auto Type:", "Auto Type:");
      reg("auto_value", "Auto Value:", "Auto Value:");
      reg("btn_flip_horiz", "Lật ngang", "Flip Horiz");
      reg("btn_render_on_top", "Vẽ đè lên", "Render on Top");
      reg("btn_flip_horiz_tooltip", "Lật ngược hình ảnh theo chiều ngang", "Flip the image horizontally");
      reg("btn_render_on_top_tooltip", "Vẽ đè lên nhân vật (nếu tắt sẽ vẽ phía sau)", "Draw on top of character (if off, draws behind)");
      reg("nudge_l_tooltip", "Dịch trái (Shift+Click: 10px, Ctrl+Click: 5px)", "Nudge left (Shift+Click: 10px, Ctrl+Click: 5px)");
      reg("nudge_u_tooltip", "Dịch lên (Shift+Click: 10px, Ctrl+Click: 5px)", "Nudge up (Shift+Click: 10px, Ctrl+Click: 5px)");
      reg("nudge_d_tooltip", "Dịch xuống (Shift+Click: 10px, Ctrl+Click: 5px)", "Nudge down (Shift+Click: 10px, Ctrl+Click: 5px)");
      reg("nudge_r_tooltip", "Dịch phải (Shift+Click: 10px, Ctrl+Click: 5px)", "Nudge right (Shift+Click: 10px, Ctrl+Click: 5px)");
      reg("nudge_reset_tooltip", "Reset tọa độ (0,0)", "Reset coordinates (0,0)");
      reg("btn_play_pause", "Phát/Tạm dừng", "Play/Pause");
      reg("effect_load_err", "Lỗi load effect ", "Error loading effect ");
      reg("effect_new_id_prompt", "ID effect mới:", "New effect ID:");
      reg("effect_invalid_id", "ID không hợp lệ", "Invalid ID");
      reg("effect_save_err_no_data", "Chưa có dữ liệu Effect!", "No Effect data available!");
      reg("effect_import_title", "Import Image Pipeline", "Import Image Pipeline");
      reg("effect_import_msg", "Chọn kiểu Import ảnh vào công cụ hiệu ứng:", "Select image import style for effect tool:");
      reg(
         "effect_import_prompt",
         "Chọn 1 hoặc NHIỀU ảnh Sprite đơn lẻ (Ctrl+Click để chọn nhiều)",
         "Select 1 or MULTIPLE individual Sprite images (Ctrl+Click to select multiple)"
      );
      reg("effect_import_read_err", "Không đọc được tệp ảnh hợp lệ nào!", "No valid image file could be read!");
      reg("effect_import_mode_msg", "Bạn muốn Nạp Mới hay Ghép Nối Tiếp các Sprite này?", "Do you want to completely reload or append these Sprites?");
      reg("effect_import_mode_title", "Chế độ Nhập Khẩu Ảnh", "Image Import Mode");
      reg("effect_import_rebuild_append", "Ghép nối tiếp hoàn tất! Tổng số: %d sprites.", "Append completed! Total: %d sprites.");
      reg("effect_import_rebuild_new", "Nạp mới hoàn tất! Ghép được: %d sprites.", "New import completed! Packed: %d sprites.");
      reg("effect_import_pack_err", "Lỗi nạp ghép ảnh: ", "Error packing images: ");
      reg("effect_imported_texture", "Imported %d sprites từ texture", "Imported %d sprites from texture");
      reg("effect_browse_title", "Chọn thư mục data effect (file binary không đuôi)", "Select effect data folder (extensionless binary files)");
      reg("effect_not_found", "Không tìm thấy data trong ", "No data found in ");
      reg("effect_count", " effects", " effects");
   }

   public static enum Locale {
      VI,
      EN;
   }
}
